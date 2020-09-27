package com.nnti.pay.controller;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.api.rb.security.MD5Util;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.RRPayVO;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * 人人支付
 */
@Controller
@RequestMapping("/rr")
public class RRController extends BasePayController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    @ResponseBody
    @RequestMapping("/online_pay")
    public Response scanCodePayment(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        if (null == payRequestVo) {

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "参数转换成对象为【空】！");
        }

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        String orderAmount = payRequestVo.getOrderAmount();
        Integer usetype = payRequestVo.getUsetype();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);

        if (!(NumericUtil.isMoney(orderAmount))) {

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "充值金额格式错误！");
        }

        DecimalFormat df = new DecimalFormat(".00");

        orderAmount = df.format(Double.parseDouble(orderAmount));

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        if (null == user) {

            throw new BusinessException(ErrorCode.SC_30000_107.getCode(), String.format(ErrorCode.SC_30000_107.getType(), loginName));
        }

        if (!(Constant.MONEY_CUSTOMER.equals(user.getRole()))) {

            throw new BusinessException(ErrorCode.SC_30000_101.getCode(), ErrorCode.SC_30000_101.getType());
        }

        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);
        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

        Map<String, Object> valuesMap = mapper.readValue(mp.getRemain(), Map.class);
        
        String way = "";
        if (Constant.USE_TYPE_WEB == usetype) {
        	loginName = "wap_" + loginName;
        	way = String.valueOf(valuesMap.get("wap"));
        } else {
            way = String.valueOf(valuesMap.get("pc"));
        }

        RRPayVO vo = new RRPayVO();

        vo.setMid(mp.getMerchantCode());
        vo.setOid(orderId);
        vo.setAmt(orderAmount);
        vo.setWay(way);
        vo.setBack(mp.getShopUrl());
        vo.setNotify(mp.getNotifyUrl());
        vo.setRemark(loginName + "," + mp.getId());

        String sign = MD5Util.string2MD5(vo.getMid() + vo.getOid() + vo.getAmt() + vo.getWay() + vo.getBack() + vo.getNotify() + mp.getSignKey());

        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "mid", "oid", "amt", "way", "back", "notify", "remark", "sign");

        log.info("玩家" + loginName + "通过人人支付进行支付，订单号为：" + orderId + "，请求参数为：" + pays);

        ReturnVo returnVo = new ReturnVo();

        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/online_pay_return")
    public String scanCodePaymentCallback(RRPayVO vo, HttpServletRequest req) throws Exception {

        log.info("人人支付回调方法接收参数：" + MyWebUtils.getRequestParameters(req));

        String oid = vo.getOid();
        String amt = vo.getAmt();
        String code = vo.getCode();
        String remark = vo.getRemark();
        String sign = vo.getSign();

        Assert.isTrue(validationTrustIp(req, DictionaryType.RRPAY_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

        if (!("100".equals(code))) {

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "支付订单结果异常！");
        }

        String loginName = remark.split(",")[0];
        String merId = remark.split(",")[1];

        Assert.notEmpty(loginName, merId);

        MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));

        if (null == mp) {
            throw new BusinessException(ErrorCode.SC_30000_102.getCode(), ErrorCode.SC_30000_102.getType());
        }

        if (2 == mp.getUseable()) {

            log.info("处理订单号" + oid + "时因支付通道已禁用，不能继续执行匹配操作！");

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "支付通道已禁用！");
        }

        String signature = MD5Util.string2MD5(vo.getMid() + vo.getOid() + vo.getAmt() + vo.getWay() + vo.getCode() + mp.getSignKey());

        if (!(sign.equals(signature))) {

            log.error("签名数据对比失败，第三方传入的签名数据为：" + sign + "，对参数进行加密后的签名数据为：" + signature);

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "签名验证异常！");
        }

        final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

        Boolean lockFlag = false;

        try {

            lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
        } catch (Exception e) {

            log.error("回调时玩家" + loginName + "获取锁发生异常，异常信息：", e);

            lockFlag = true;
        }

        try {

            if (lockFlag) {

                tradeService.doPayFlow(oid, amt, loginName, mp, "", null);

                return ok;
            } else {

                log.error("回调时玩家" + loginName + "未能获取锁，无法执行请求对应的方法....");
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {

                lock.release();
            } catch (Exception e) {

                log.error("回调时玩家" + loginName + "释放锁发生异常, 异常信息：" + e);
            }
        }

        return error;
    }
}
