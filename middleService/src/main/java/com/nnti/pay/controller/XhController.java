package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XhPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/3/31.
 * 信汇支付
 */
@Controller
@RequestMapping("/xh")
public class XhController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    @ResponseBody
    @RequestMapping("/zfb_wx")
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);
        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String payerIp = payRequestVo.getCustomerIp();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        Assert.notEmpty(orderId);

        if (!MyUtils.isNotEmpty(payerIp)) {
            payerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        XhPayVo vo = gson.fromJson(mp.getRemain(), XhPayVo.class);
        vo.setZfbWx(mp.getMerchantCode(), orderId, mp.getNotifyUrl(), mp.getShopUrl(), orderAmount, loginName + "," + mp.getId(), mp.getSystemCode());

        String param = MyUtils.obj2UrlParam(vo, true, "version", "method", "partner", "banktype", "paymoney", "ordernumber", "callbackurl");
        String sign = DigestUtils.signByMD5(param, mp.getSignKey());
        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "partner", "version", "method", "banktype", "paymoney", "ordernumber", "callbackurl", "hrefbackurl", "goodsname", "attach", "isshow", "sign");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":xh 请求参数：" + pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(XhPayVo vo, HttpServletRequest req) {

        log.info("xh 接收参数：" + MyWebUtils.getRequestParameters(req));

        String partner = vo.getPartner();//商户号
        String ordernumber = vo.getOrdernumber();//商户订单号
        String orderstatus = vo.getOrderstatus();//状态
        String paymoney = vo.getPaymoney();//金额
        String sysnumber = vo.getSysnumber();//仁信订单号
        String attach = vo.getAttach();//备注信息
        String sign = vo.getSign();//签名

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.RX_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue(MyUtils.isNotEmpty(ordernumber), ErrorCode.SC_30000_111.getType());

            String loginName = attach.split(",")[0];
            String merId = attach.split(",")[1];
            Assert.notEmpty(loginName, merId);

            //根据商户号查询商户信息
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String param = MyUtils.obj2UrlParam(vo, true, "partner", "ordernumber", "orderstatus", "paymoney");
            String _sign = DigestUtils.signByMD5(param, mp.getSignKey());

            Assert.isTrue(sign.equals(_sign), ErrorCode.SC_30000_114.getType());

            Boolean lockFlag = false;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            log.info("lock:" + lock);

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(ordernumber, paymoney, loginName, mp, "",null);
                    return ok;
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
        return error;
    }
}
