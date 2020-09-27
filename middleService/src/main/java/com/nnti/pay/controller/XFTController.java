package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XfPayVo;
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
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/3/1.
 * 讯付通
 */
@Controller
@RequestMapping("/xft")
public class XFTController extends BasePayController {

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

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        XfPayVo vo = JSON.readValue(mp.getRemain(), XfPayVo.class);
        vo.setWxZfb(mp.getMerchantCode(), orderId, amount, loginName, mp.getNotifyUrl(), mp.getShopUrl());
        vo.setRandom(MyUtils.randomByLength(4));

        Map paramMap = MyUtils.describe(vo, "amount", "callBackUrl", "callBackViewUrl", "goodsName", "merNo", "netway", "orderNum", "random");
        String metaSignJsonStr = JSON.writeValueAsString(paramMap);

        String sign = DigestUtils.md5DigestAsHex(metaSignJsonStr, mp.getSignKey());

        paramMap.put("sign", sign);

        String param = "data=" + JSON.writeValueAsString(paramMap);
        log.info(orderId + ":xf zfb_wx请求参数：" + param);

        String result = MyWebUtils.getHttpContentByBtParam(mp.getPayUrl(), param);

        log.info(orderId + ":返回参数：" + result);

        XfPayVo responseVo = JSON.readValue(result, XfPayVo.class);
        Assert.isTrue("00".equals(responseVo.getStateCode()), responseVo.getMsg());

        //验签
        paramMap = MyUtils.describe(responseVo, "merNo", "msg", "orderNum", "qrcodeUrl", "stateCode");

        metaSignJsonStr = JSON.writeValueAsString(paramMap);

        sign = DigestUtils.md5DigestAsHex(metaSignJsonStr, mp.getSignKey());

        Assert.isTrue(sign.equals(responseVo.getSign()), "签名验证失败");

        log.info(orderId + ":返回二维码：" + responseVo.getQrcodeUrl());

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(responseVo.getQrcodeUrl());

        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(String data, HttpServletRequest req, HttpServletResponse response) throws Exception {

        String msg;
        log.info("讯付 接收参数：" + data);

        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.XFT_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            XfPayVo vo = gson.fromJson(data, XfPayVo.class);

            Assert.isTrue("00".equals(vo.getPayResult()), "讯付支付返回支付结果为处理不成功：State：" + vo.getPayResult());

            MerchantPay mp = merchantPayService.findByMerNo(vo.getMerNo());
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            Map param = MyUtils.describe(vo, "merNo", "netway", "orderNum", "amount", "goodsName", "payResult", "payDate");

            String jsonStr = gson.toJson(param);

            log.info("验签参数：" + jsonStr);

            String sign = DigestUtils.md5DigestAsHex(jsonStr, mp.getSignKey());

            Assert.isTrue(sign.equals(vo.getSign()), "签名校验失败orderId ：" + vo.getOrderNum());

            Double amount = NumericUtil.div(Double.valueOf(vo.getAmount()), 100);

            Boolean lockFlag = false;
            String loginName = vo.getGoodsName();

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {

                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {

                if (lockFlag) {

                    tradeService.doPayFlow(vo.getOrderNum(), amount.toString(), loginName, mp, "",null);

                    return "0";
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
        }
        return "-1";
    }

}
