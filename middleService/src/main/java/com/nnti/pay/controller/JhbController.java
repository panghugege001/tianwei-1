package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.JhbPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.jhb.AbsJhb;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/5/16.
 * 聚汇宝支付
 */
@Controller
@RequestMapping("/jhb")
public class JhbController extends BasePayController {
    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /*** 微信支付*/
    @ResponseBody
    @RequestMapping("/zfb_wx")
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String customerIp = payRequestVo.getCustomerIp();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, usetype, orderAmount);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "100");

        // 商家定单时间(必填)
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        JhbPayVo vo = JSON.readValue(mp.getRemain(), JhbPayVo.class);
        vo.setZfbWx(mp.getMerchantCode(), orderAmount, orderId, mp.getSystemCode(), mp.getNotifyUrl(), loginName + "," + mp.getId());

        Map pays = MyUtils.describe(vo, "merchno", "amount", "traceno", "payType", "goodsName", "notifyUrl", "cust1");
        pays.put("signature", AbsJhb.signature(pays, mp.getSignKey()));
        String url = AbsJhb.buildUrl("passivePay", pays);
        log.info(orderId + ":请求参数：" + url);
        String result = AbsJhb.receiveBySend(url, "GBK");

        log.info(orderId + ":shb返回结果：" + result);

        String code, data = "";
        if (MyUtils.isNotEmpty(result)) {
            vo = JSON.readValue(result, JhbPayVo.class);
            if ("00".equals(vo.getRespCode())) {
                data = vo.getBarCode();
            } else {
                throw new BusinessException(ErrorCode.SC_10001.getType(), vo.getMessage());
            }
        } else {
            throw new BusinessException(ErrorCode.SC_10001.getType(), "第三方返回空值");
        }

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(data);

        log.info(orderId + ":返回二维码：" + data);
        return transfer(returnVo);
    }

    /*** 支付宝 微信 支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(JhbPayVo vo, HttpServletRequest req, HttpServletResponse resp) {

        try {

            Map param = MyWebUtils.getRequestParameters(req);
            log.info("jhb接收参数：" + param);

            String status = vo.getStatus();
            String signature = vo.getSignature();
            String remark = vo.getCust1();
            String amount = vo.getAmount();
            String traceno = vo.getTraceno();
            String merchName = vo.getMerchName();

            Assert.isTrue(validationTrustIp(req, DictionaryType.JHB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

//            param.remove("signature");
//            String signatureKey = AbsJhb.signature(param, mp.getSignKey());
//
//            log.info("签名：" + signatureKey);

            if (status.equals("1")) {

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
                        tradeService.doPayFlow(traceno, amount, loginName, mp, "",null);
                        return success;
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
            }
        } catch (Exception e) {
            log.error("交易异常：", e);
        }
        return error;
    }

}
