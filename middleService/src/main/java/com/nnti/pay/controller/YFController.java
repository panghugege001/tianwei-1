package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.YfPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/2/10.
 * 优付平台
 */
@Controller
@RequestMapping("/yf")
public class YFController extends BasePayController {

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
        String customer_ip = payRequestVo.getCustomerIp();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //新玩家需满一周才能支付
        //validationNewUser(user, -7);

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        YfPayVo vo = gson.fromJson(mp.getRemain(), YfPayVo.class);
        vo.setWxZfb(vo.getBANK_CODE(), "", "", vo.getINPUT_CHARSET(), mp.getMerchantCode(), mp.getNotifyUrl(), orderAmount,
                orderId, mp.getSystemCode(), "", "", vo.getREFERER(), loginName, mp.getShopUrl(), vo.getVERSION(), mp.getSignKey());
        //拼接参数加密串,按字母升序, 结尾加KEY
        String str = MyUtils.obj2UrlParam(vo, true, "BANK_CODE",
                "CUSTOMER_IP", "CUSTOMER_PHONE", "INPUT_CHARSET", "MER_NO",
                "NOTIFY_URL", "ORDER_AMOUNT", "ORDER_NO", "PRODUCT_NAME",
                "PRODUCT_NUM", "RECEIVE_ADDRESS", "REFERER", "RETURN_PARAMS",
                "RETURN_URL", "VERSION", "KEY");
        //Sign 签名
        vo.setSIGN(DigestUtils.signByMD5(str));
        vo.setApiUrl(mp.getApiUrl());

        Map pays = MyUtils.describe(vo, "VERSION", "INPUT_CHARSET",
                "RETURN_URL", "NOTIFY_URL", "BANK_CODE", "MER_NO", "ORDER_NO", "ORDER_AMOUNT", "PRODUCT_NAME", "PRODUCT_NUM",
                "REFERER", "CUSTOMER_IP", "CUSTOMER_PHONE", "RECEIVE_ADDRESS", "RETURN_PARAMS", "SIGN", "apiUrl");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":yf online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(YfPayVo vo, HttpServletRequest req) throws Exception {

        log.info("yf zfb_wx_return接收参数：" + MyWebUtils.getRequestParameters(req));

        String mer_no = vo.getMer_no();
        String order_amount = vo.getOrder_amount();
        String order_no = vo.getOrder_no();
        String trade_no = vo.getTrade_no();
        String trade_params = vo.getTrade_params();
        String trade_status = vo.getTrade_status();
        String trade_time = vo.getTrade_time();
        String postsign = vo.getSign();

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.YF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue(trade_status.equals(success), ErrorCode.SC_30000_111.getType());

            MerchantPay mp = merchantPayService.findByMerNo(mer_no);
            Assert.notEmpty(mp);

            vo.setKEY(mp.getSignKey());
            String str = MyUtils.obj2UrlParam(vo, true, "mer_no", "order_amount", "order_no", "trade_no", "trade_params", "trade_status", "trade_time", "KEY");

            String sign = DigestUtils.signByMD5(str);

            Assert.isTrue(postsign.equals(sign), ErrorCode.SC_30000_114.getType());

            Boolean lockFlag = false;
            String loginName = trade_params;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(trade_no, order_amount, loginName, mp, "",null);
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
        } catch (BusinessException e) {
            log.error("回调异常：", e);
        }
        return error;
    }
}
