package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.EncryptionUtil;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XbPayVo;
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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/2/13.
 * 新贝 支付
 */
@Controller
@RequestMapping("/xb")
public class XBController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    @ResponseBody
    @RequestMapping("zfb_wx")
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);
        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String customerIp = "127.0.0.1";
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        String orderDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        XbPayVo vo = gson.fromJson(mp.getRemain(), XbPayVo.class);
        vo.setZfbWx(mp.getMerchantCode(), orderAmount, orderId, mp.getNotifyUrl(), mp.getShopUrl(), orderDate, mp.getSignKey(), "", loginName, customerIp, mp.getApiUrl());
        String join = MyUtils.join("=[", "]", "Version", vo.getVersion(), "MerchantCode", vo.getMerchantCode(), "OrderId", vo.getOrderId(),
                "Amount", orderAmount.toString(), "AsyNotifyUrl", vo.getAsyNotifyUrl(), "SynNotifyUrl", vo.getSynNotifyUrl(),
                "OrderDate", orderDate, "TradeIp", customerIp, "PayCode", vo.getPayCode(), "TokenKey", mp.getSignKey()) + "]";
        vo.setSignValue(EncryptionUtil.encryptPassword(join).toUpperCase());

        Map pays = MyUtils.describe(vo, "url", "Version", "MerchantCode", "OrderId", "Amount", "AsyNotifyUrl", "SynNotifyUrl",
                "OrderDate", "TradeIp", "PayCode", "Remark1", "Remark2", "SignValue");
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":xb online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(XbPayVo vo, HttpServletRequest req) {
        log.info("新贝接收参数：" + MyWebUtils.getRequestParameters(req));

        String version = vo.getVersion();
        String merchantCode = vo.getMerchantCode();
        String orderId = vo.getOrderId();
        String orderDate = vo.getOrderDate();
        String tradeIp = vo.getTradeIp();
        String serialNo = vo.getSerialNo();
        String amount = vo.getAmount();
        String payCode = vo.getPayCode();
        String state = vo.getState();
        String remark2 = vo.getRemark2();
        String signValue = vo.getSignValue();
        String finishTime = vo.getFinishTime();

        String msg;

        DecimalFormat df = new DecimalFormat("#.00");
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.XB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue("8888".equals(state), "新贝支付宝支付返回支付结果为处理不成功：State：" + state);

            MerchantPay mp = merchantPayService.findByMerNo(merchantCode);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String join = MyUtils.join("=[", "]", "Version", version, "MerchantCode", merchantCode, "OrderId", orderId,
                    "OrderDate", orderDate, "TradeIp", tradeIp, "SerialNo", serialNo, "Amount", df.format(Double.valueOf(amount)),
                    "PayCode", payCode, "State", state, "FinishTime", finishTime, "TokenKey", mp.getSignKey()) + "]";

            String mySign = EncryptionUtil.encryptPassword(join).toUpperCase();
            log.info("mySign:" + mySign);
            Assert.isTrue(mySign.equals(signValue), "新贝支付宝支付回调签名验证失败：orderId ：" + orderId);

            Boolean lockFlag = false;
            String loginName = remark2;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));
            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(orderId, amount, loginName, mp, "",null);
                    return OK;
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
            log.error("新贝支付宝或微信回调异常：", e);
        }

        return ERROR;
    }


    /*** 网银支付 */
    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        log.info("xb online_pay支付开始");

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String bankCode = payRequestVo.getBankCode();
        String customerIp = payRequestVo.getCustomerIp();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, usetype, orderAmount, bankCode);
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

        // 客户端IP（选填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        // 商家定单时间(必填)        
        String orderDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());

        XbPayVo vo = gson.fromJson(mp.getRemain(), XbPayVo.class);
        vo.setOnline(mp.getMerchantCode(), bankCode, orderAmount, orderId, mp.getNotifyUrl(), mp.getShopUrl(), orderDate, mp.getSignKey(), "", loginName + "," + mp.getId(), customerIp, mp.getApiUrl());
        String join = MyUtils.join("=[", "]", "Version", vo.getVersion(), "MerchantCode", vo.getMerchantCode(), "OrderId", vo.getOrderId(),
                "Amount", orderAmount.toString(), "AsyNotifyUrl", vo.getAsyNotifyUrl(), "SynNotifyUrl", vo.getSynNotifyUrl(),
                "OrderDate", orderDate, "TradeIp", customerIp, "PayCode", vo.getPayCode(), "TokenKey", mp.getSignKey()) + "]";

        vo.setSignValue(EncryptionUtil.encryptPassword(join).toUpperCase());

        Map pays = MyUtils.describe(vo, "url", "Version", "MerchantCode", "OrderId", "Amount", "AsyNotifyUrl", "SynNotifyUrl",
                "OrderDate", "TradeIp", "PayCode", "Remark1", "Remark2", "SignValue");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":xb online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 网银支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(XbPayVo vo, HttpServletRequest req) {

        log.info("新贝网银支付回掉接收参数：" + MyWebUtils.getRequestParameters(req));

        String version = vo.getVersion();
        String merchantCode = vo.getMerchantCode();
        String orderId = vo.getOrderId();
        String orderDate = vo.getOrderDate();
        String tradeIp = vo.getTradeIp();
        String serialNo = vo.getSerialNo();
        String amount = vo.getAmount();
        String payCode = vo.getPayCode();
        String state = vo.getState();
        String remark2 = vo.getRemark2();
        String signValue = vo.getSignValue();
        String finishTime = vo.getFinishTime();

        DecimalFormat df = new DecimalFormat("#.00");
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.XB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue("8888".equals(state), "新贝网银支付返回支付结果为处理不成功：State：" + state);

            String loginName = remark2.split(",")[0];
            String merId = remark2.split(",")[1];

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String join = MyUtils.join("=[", "]", "Version", version, "MerchantCode", merchantCode, "OrderId", orderId,
                    "OrderDate", orderDate, "TradeIp", tradeIp, "SerialNo", serialNo, "Amount",
                    df.format(Double.valueOf(amount)), "PayCode", payCode, "State", state, "FinishTime", finishTime,
                    "TokenKey", mp.getSignKey()) + "]";

            String mySign = EncryptionUtil.encryptPassword(join).toUpperCase();
            Assert.isTrue(mySign.equals(signValue), "新贝网银支付回调签名验证失败：orderId ：" + orderId);

            Boolean lockFlag = false;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
                    GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(orderId, amount, loginName, mp, "",null);
                    return OK;
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
            log.error("新贝支付宝或微信回调异常：", e);
        } catch (Exception e) {
            log.error("新贝支付宝或微信回调异常：", e);
        }

        return null;
    }


    /**** 点卡支付 后台处理*/
    @ResponseBody
    @RequestMapping("point_card")
    public Response point_card(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);
        Long platformId = payRequestVo.getPlatformId();
        String orderAmount = payRequestVo.getOrderAmount();
        String loginName = payRequestVo.getLoginName();
        Integer usetype = payRequestVo.getUsetype();
        String cardNo = payRequestVo.getCardNo();
        String cardCode = payRequestVo.getCardCode();
        String cardPassword = payRequestVo.getCardPassword();
        String customerIp = payRequestVo.getCustomerIp();

        Assert.notEmpty(platformId, orderAmount, loginName, cardNo, cardCode, cardPassword, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        cardCode = cardCode.trim();
        cardNo = cardNo.trim();
        cardPassword = cardPassword.trim();
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        String type = mp.getPayCenterUrl().replaceAll("/", "") + "_type";
        validationCard(type, cardCode);

        validationAmountCutAll(loginName, mp);

        String prefix = loginName + "_" + cardCode;

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, prefix, "100");

        XbPayVo vo = gson.fromJson(mp.getRemain(), XbPayVo.class);

        String order_no = orderId;
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());
        // 客户端IP（选填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        // 商家定单时间(必填)
        String orderDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        vo.setPointCard(mp.getMerchantCode(), cardCode, orderAmount, orderId, mp.getNotifyUrl(), mp.getShopUrl(), orderDate, mp.getSignKey(), "", loginName + "," + mp.getId(), customerIp, mp.getApiUrl(), cardNo, cardPassword);

        String join = MyUtils.join("=[", "]", "Version", vo.getVersion(), "MerchantCode", vo.getMerchantCode(), "OrderId", vo.getOrderId(),
                "Amount", orderAmount.toString(), "AsyNotifyUrl", vo.getAsyNotifyUrl(), "SynNotifyUrl", vo.getSynNotifyUrl(),
                "OrderDate", orderDate, "TradeIp", customerIp, "PayCode", vo.getPayCode(), "TokenKey", mp.getSignKey()) + "]";

        vo.setSignValue(EncryptionUtil.encryptPassword(join).toUpperCase());

        Map pays = MyUtils.describe(vo, "Version", "MerchantCode", "OrderId", "Amount", "AsyNotifyUrl", "SynNotifyUrl",
                "OrderDate", "TradeIp", "PayCode", "CardNo", "CardPassword", "Remark1", "Remark2", "SignValue");
        log.info(orderId + ":xb 请求参数：" + pays);

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":xb point_card_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 点卡支付回调 */
    @ResponseBody
    @RequestMapping(value = "/point_card_return")
    public String point_card_return(XbPayVo vo, HttpServletRequest req) {

        log.info("新贝点开支付回掉接收参数：" + MyWebUtils.getRequestParameters(req));

        String version = vo.getVersion();
        String merchantCode = vo.getMerchantCode();
        String orderId = vo.getOrderId();
        String orderDate = vo.getOrderDate();
        String tradeIp = vo.getTradeIp();
        String serialNo = vo.getSerialNo();
        String amount = vo.getAmount();
        String payCode = vo.getPayCode();
        String state = vo.getState();
        String remark2 = vo.getRemark2();
        String signValue = vo.getSignValue();
        String finishTime = vo.getFinishTime();

        DecimalFormat df = new DecimalFormat("#.00");
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.XB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue("8888".equals(state), "新贝点开支付返回支付结果为处理不成功：State：" + state);

            String loginName = remark2.split(",")[0];
            String merId = remark2.split(",")[1];

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String join = MyUtils.join("=[", "]", "Version", version, "MerchantCode", merchantCode, "OrderId", orderId,
                    "OrderDate", orderDate, "TradeIp", tradeIp, "SerialNo", serialNo, "Amount",
                    df.format(Double.valueOf(amount)), "PayCode", payCode, "State", state, "FinishTime", finishTime,
                    "TokenKey", mp.getSignKey()) + "]";

            String mySign = EncryptionUtil.encryptPassword(join).toUpperCase();
            Assert.isTrue(mySign.equals(signValue), "新贝点卡支付回调签名验证失败：orderId ：" + orderId);

            Boolean lockFlag = false;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
                    GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayPointCardFlow(orderId, Double.valueOf(amount), loginName, payCode, mp,null,null);
                    return OK;
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
            log.error("新贝点卡回调异常：", e);
        } catch (Exception e) {
            log.error("新贝点卡回调异常：", e);
        }

        return null;
    }

}
