package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.JhzPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.jhz.RSASignature;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import net.sf.json.JSONObject;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/3/14.
 * 金海哲支付
 */
@Controller
@RequestMapping("/jhz")
public class JhzController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 支付宝支付 或者微信支付
     */
    @ResponseBody
    @RequestMapping(value = "/zfb_wx", method = RequestMethod.POST)
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        String amount = NumericUtil.mul(Double.valueOf(orderAmount), 100).longValue() + "";

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");
        Assert.notEmpty(orderId);

        JhzPayVo vo = JSON.readValue(mp.getRemain(), JhzPayVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        String payDate = (new Date().getTime() / 1000) + "";
        vo.setWxZfb(mp.getMerchantCode(), orderId, amount, mp.getNotifyUrl(), mp.getShopUrl(), mp.getSignKey(), payDate, loginName + "," + mp.getMerchantCode(), "r2", "r3");

        String content = MyUtils.join("|", "|", vo.getMerchantNo(), vo.getRequestNo(), amount, mp.getShopUrl(), mp.getNotifyUrl(), payDate, vo.getAgencyCode(), vo.getRemark1(), vo.getRemark2(), vo.getRemark3());

        String sign = RSASignature.sign(content, mp.getSignKey());
        vo.setSignature(sign);

        Map pays = MyUtils.describe(vo, "merchantNo", "requestNo", "amount", "pageUrl", "backUrl",
                "payDate","payMethod", "remark1", "remark2", "remark3", "agencyCode", "signature");

        log.info(orderId + ":jhz online_pay请求参数：" + pays);
        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));

        log.info(orderId + ":返回结果：" + result);

        JSONObject json = new JSONObject().fromObject(result);
        String sign_y = json.getString("sign");
        json.remove("sign");
        String res_json = json.toString().trim();

        String backQrCodeUrl;

        if (RSASignature.doCheck(res_json, sign_y, vo.getPublicKey())) {
            backQrCodeUrl = json.getString("backQrCodeUrl");
        } else {
            log.error(orderId + ":验签失败：" + sign_y);
            throw new BusinessException(ErrorCode.SC_30000_114);
        }

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(backQrCodeUrl);

        return transfer(returnVo);
    }

    /*** 支付宝 微信 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(JhzPayVo vo, HttpServletRequest req) {

        log.info("jhz 接收参数：" + MyWebUtils.getRequestParameters(req));

        String msg = vo.getMsg();
        String ret = vo.getRet();
        String sign = vo.getSign();

        JSONObject msgjson = JSONObject.fromObject(msg);
        String money = msgjson.getString("money");//金额
        String orderDate = msgjson.getString("orderDate");//订单时间
        String no = msgjson.getString("no");//订单号
        String remarks = msgjson.getString("remarks");//备注

        String content = ret + "|" + msg;

        try {
            String loginName = remarks.split(",")[0];
            String merNo = remarks.split(",")[1];

            Assert.isTrue(validationTrustIp(req, DictionaryType.JHZ_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Double amount = NumericUtil.div(Double.valueOf(money), 100);

            MerchantPay mp = merchantPayService.findByMerNo(merNo);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            JhzPayVo jvo = JSON.readValue(mp.getRemain(), JhzPayVo.class);

            Assert.isTrue(RSASignature.doCheck(content, sign, jvo.getPublicKey(), "UTF-8"), ErrorCode.SC_30000_114.getType());

            Boolean lockFlag = false;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(no, amount.toString(), loginName, mp, "",null);
                    return SUCCESS;
                } else {
                    log.error("玩家：" + loginName + "未能获取锁，无法执行请求对应的方法....");
                }
            } catch (Exception e) {
                log.error("异常：" + e);
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    log.error("玩家：" + loginName + "释放锁发生异常, 异常信息：" + e);
                }
            }

        } catch (Exception e) {
            log.error("回调异常：", e);
        }
        return ERROR;
    }
}
