package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.JhPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.jh.CertificateCoder;
import com.nnti.pay.security.lf.json.JSONObject;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.commons.codec.binary.Base64;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.nnti.common.utils.MyWebUtils.getListNamevaluepair;

/**
 * Created by wander on 2017/4/26.
 * 聚合 支付
 */
@Controller
@RequestMapping("/jh")
public class JhController extends BasePayController {
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

        String remark = loginName;

        validationAmountCutAll(loginName, mp);
        if (usetype == Constant.USE_TYPE_WEB) {
            remark = "wap_" + loginName;
        }

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, remark, "_");

        String amount = NumericUtil.mul(Double.valueOf(orderAmount), 100).longValue() + "";

        JhPayVo vo = JSON.readValue(mp.getRemain(), JhPayVo.class);
        String extend_params = "{\"test\":\"" + mp.getId() + "__" + remark + "\"}";
        String payDate = (new Date().getTime() / 1000) + "";
        vo.setZfbWx(mp.getMerchantCode(), payDate, orderId, amount, "1", remark, mp.getNotifyUrl(), mp.getShopUrl(), customerIp, extend_params);

        String signSrc = MyUtils.obj2UrlParam(true, MyUtils.describe(vo, "service", "partner", "charset", "pay_type", "sign_type", "timestamp",
                "trade_no", "trade_amount", "quantity", "subject", "bank_code", "notify_url", "return_url", "ip", "extend_params"));

        log.info("签名数据：" + signSrc);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String certificatePath = classLoader.getResource(vo.getCertificatePath()).getFile();
        String keyStorePath = classLoader.getResource(vo.getKeyStorePath()).getFile();
        String keyStorePassword = vo.getPassword();
        String aliasPassword = vo.getPassword();
        String alias = vo.getAlias();
        log.info("路径：" + certificatePath);
        log.info("路径2：" + keyStorePath);

        String sign = Base64.encodeBase64String(CertificateCoder.sign(signSrc.getBytes("utf-8"), keyStorePath, alias, keyStorePassword, aliasPassword));

        log.info("签名：" + sign);

        String paramsSrc = MyUtils.obj2UrlParam(true, MyUtils.describe(vo, "trade_no", "trade_amount", "quantity", "subject", "bank_code", "notify_url", "return_url", "ip", "extend_params"));
        String params = CertificateCoder.encryptByPublicKey(paramsSrc.getBytes(), certificatePath, 0);

        vo.setSign(sign);
        vo.setParams(params);

        Map pays = MyUtils.describe(vo, "service", "partner", "charset", "pay_type", "sign_type", "timestamp", "sign", "params");
        log.info("请求参数：" + pays);

        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), getListNamevaluepair(pays));

        log.info("获取结果：" + result);

        String data = "", webchatUrl;

        JSONObject jsonObject = new JSONObject(result);
        String status = jsonObject.getString("status");//200
        String code = jsonObject.getString("code");//10000
        String msg = jsonObject.getString("msg");//true

        Assert.isTrue("10000".equals(code) && "true".equals(msg), msg);

        String resultSrc = jsonObject.getString("result");//解密

        resultSrc = CertificateCoder.decryptByPrivateKey(Base64.decodeBase64(URLDecoder.decode(resultSrc)), keyStorePath, alias, keyStorePassword, aliasPassword, 1);

        JSONObject jsonObject2 = new JSONObject(resultSrc);
        String qr_url = jsonObject2.getString("qr_url");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(qr_url);

        log.info(orderId + ":返回二维码：" + qr_url);
        return transfer(returnVo);
    }

    /*** 支付宝 微信 支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(JhPayVo vo, HttpServletRequest req) {

        log.info("jh 接收参数：" + MyWebUtils.getRequestParameters(req));

        String extend_params = vo.getExtend_params();
        String trade_amount = vo.getTrade_amount();
        String trade_no = vo.getTrade_no();

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.JH_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            String loginName, merId;
            JSONObject jsonObject = new JSONObject(extend_params);

            String split[] = jsonObject.getString("test").split("__");
            merId = split[0];
            loginName = split[1];

            Double amount = NumericUtil.div(Double.valueOf(trade_amount), 100);

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            JhPayVo remainVo = gson.fromJson(mp.getRemain(), JhPayVo.class);

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
                    tradeService.doPayFlow(trade_no, amount.toString(), loginName, mp, "",null);
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
            log.error("交易异常：", e);
        }
        return error;
    }

}
