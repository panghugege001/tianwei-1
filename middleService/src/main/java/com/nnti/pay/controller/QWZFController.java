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
import com.nnti.pay.controller.vo.QwPayVo;
import com.nnti.pay.controller.vo.ReturnVo;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/1/31.
 */
@Controller
@RequestMapping("/qw")
public class QWZFController extends BasePayController {

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

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");
        Assert.notEmpty(orderId);

        if (!MyUtils.isNotEmpty(payerIp)) {
            payerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        QwPayVo vo = gson.fromJson(mp.getRemain(), QwPayVo.class);
        vo.setZfbWx(mp.getMerchantCode(), orderId, mp.getNotifyUrl(), mp.getShopUrl(), orderAmount, loginName, payerIp);

        String param = MyUtils.obj2UrlParam(vo, true, "parter", "type", "value", "orderid", "callbackurl");
        String sign = DigestUtils.signByMD5(param, mp.getSignKey());
        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "parter", "type", "value", "orderid", "callbackurl", "hrefbackurl", "attach", "payerIp", "sign");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":qw online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(QwPayVo vo, HttpServletRequest req) {

        log.info("qw 接收参数：" + MyWebUtils.getRequestParameters(req));

        String orderid = vo.getOrderid();
        String opstate = vo.getOpstate();
        String ovalue = vo.getOvalue();
        String sign = vo.getSign();
        String cid = vo.getCid();
        String ekaorderid = vo.getEkaorderid();
        String ekatime = vo.getEkatime();
        String attach = vo.getAttach();
        String msg = vo.getMsg();

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.QW_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue(MyUtils.isNotEmpty(orderid), ErrorCode.SC_30000_111.getType());

            //根据商户号查询商户信息
            MerchantPay mp = merchantPayService.findById(Long.parseLong(cid));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String param = MyUtils.obj2UrlParam(vo, true, "orderid", "opstate", "ovalue");
            String _sign = DigestUtils.signByMD5(param, mp.getSignKey());

            Assert.isTrue(sign.equals(_sign), ErrorCode.SC_30000_114.getType());

            Boolean lockFlag = false;
            String loginName = attach;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(orderid, ovalue, loginName, mp, "",null);
                    return "opstate=0";
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
        return "opstate=-2";
    }
}
