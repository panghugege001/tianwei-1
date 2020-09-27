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
import com.nnti.pay.controller.vo.ThPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.th.MD5Encoder;
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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/3/13.
 * 通汇支付
 */
@Controller
@RequestMapping("/th")
public class THController extends BasePayController {

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
        String customerIp = payRequestVo.getCustomerIp();
        String domain = payRequestVo.getDomain();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();

        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }

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

        if (!MyUtils.isNotEmpty(domain)) {
            domain = getHost(req);
        }

        ThPayVo vo = JSON.readValue(mp.getRemain(), ThPayVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());

        vo.setWxZfb(mp.getMerchantCode(), orderId, orderAmount, order_time, "", "1", loginName + "," + mp.getId(), mp.getNotifyUrl(),
                mp.getShopUrl(), domain, customerIp);

        Map pays = MyUtils.describe(vo, "input_charset", "notify_url", "return_url", "pay_type", "bank_code", "merchant_code", "order_no",
                "order_amount", "order_time", "product_name", "product_num", "req_referer", "customer_ip", "customer_phone", "receive_address",
                "return_params");
        String url_param = MyUtils.obj2UrlParam(false, pays) + "&key=" + mp.getSignKey();
        log.info("url_param:" + url_param);
        String sign = MD5Encoder.encode(url_param, "UTF-8");

        pays.put("sign", sign);
        log.info(orderId + ":请求参数：" + pays);
        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        log.info(orderId + ":返回结果：" + result);

        String data = MyUtils.matcher("<url>(.*?)</url>", result);
        if (!MyUtils.isNotEmpty(data) || data.contains("errorCode")) {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "二维码为空或者交易失败");
        }

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(data);

        log.info(orderId + ":返回二维码：" + data);
        return transfer(returnVo);
    }

    /*** 口袋支付宝 微信 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(ThPayVo vo, HttpServletRequest req) {

        log.info("th 接收参数：" + MyWebUtils.getRequestParameters(req));

        String merchant_code = vo.getMerchant_code();
        String notify_type = vo.getNotify_type();
        String order_no = vo.getOrder_no();
        String sign = vo.getSign();
        String order_amount = vo.getOrder_amount();
        String order_time = vo.getOrder_time();
        String return_params = vo.getReturn_params();
        String trade_no = vo.getTrade_no();
        String trade_time = vo.getTrade_time();
        String trade_status = vo.getTrade_status();

        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.TH_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue(trade_status.equals("success"), ErrorCode.SC_30000_111.getType());

            String loginName = return_params.split(",")[0];
            String merId = return_params.split(",")[1];

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            Map param = MyUtils.describe(vo, "merchant_code", "notify_type", "order_no", "order_amount", "order_time", "return_params", "trade_no", "trade_time", "trade_status");

            String signMsg = DigestUtils.signByMD5(MyUtils.obj2UrlParam(false, param) + "&key=" + mp.getSignKey());

            log.info("签名：" + signMsg);

            Assert.isTrue(sign.equals(signMsg), ErrorCode.SC_30000_114.getType());


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
                    tradeService.doPayFlow(order_no, order_amount, loginName, mp, "",null);
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

        } catch (Exception e) {
            log.error("回调异常：", e);
        }
        return error;
    }
}
