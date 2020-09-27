package com.nnti.pay.controller;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.BPPayVo;
import com.nnti.pay.controller.vo.HPPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * @author pony
 * @Description HPay支付
 */
@Controller
@RequestMapping("/hp")
public class HPController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 所有支付
     */
    @SuppressWarnings("rawtypes")
	@ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String bankcode = payRequestVo.getBankCode();
        
        String customerIp = payRequestVo.getCustomerIp();
        
        if (StringUtils.isBlank(customerIp)) {

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
        
        HPPayVo vo = new HPPayVo();
        
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            /*vo.setBankcode("aph5");
            vo.setChanneltype("");
            vo.setDevicetype("3");*/
        } else {
        }
        vo.setCustomno(orderId);
        vo.setUsercode(mp.getMerchantCode());
        vo.setMoney(orderAmount);
        vo.setBankcode("ap");
        vo.setProductname("apple");
        vo.setScantype("ap");
        vo.setNotifyurl(mp.getNotifyUrl());
        vo.setBackurl(mp.getShopUrl());
        vo.setBuyerip(customerIp);
        vo.setMd5key(mp.getSignKey());
        vo.setPageurl(mp.getShopUrl());
        String orderTime = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        vo.setSendtime(orderTime);

        String svsign=vo.getUsercode()+"|"+vo.getCustomno()+"|"+ vo.getScantype()+"|"+vo.getNotifyurl()+"|"+vo.getMoney()+"|"+vo.getSendtime()+"|"+vo.getBuyerip()+"|"+vo.getMd5key();
        String vsign= DigestUtils.signByMD5(svsign);
        log.info("签名：" + vsign);
        vo.setSign(vsign);

        Map pays = MyUtils.describe(vo, "usercode","customno",  "productname","money", "scantype", "sendtime","notifyurl", "buyerip","sign");
        log.info(orderId + ":请求参数：" + pays);

        String result = MyWebUtils.sendFormMsgFastPay(JSON.writeValueAsString(pays), mp.getPayUrl());
        System.out.println("请求返回结果："+result);
        JSONObject json = JSONObject.fromObject(result);
        
        boolean success = json.getBoolean("success");
        String resultMsg = json.getString("resultMsg");
        
        Assert.isTrue(success, resultMsg);
        
        JSONObject data = json.getJSONObject("data");
        String usercode = data.getString("usercode");
        String orderno = data.getString("orderno");
        String scanurl = data.getString("scanurl");
        String tjmoney = data.getString("tjmoney");
        String status = data.getString("status");
        String md5key = mp.getSignKey();
        String sign = data.getString("sign");
        String _sign=DigestUtils.signByMD5(usercode +orderno+scanurl+tjmoney+status+md5key);
        
        Assert.isTrue(sign.equals(_sign), "数据签名异常");
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(scanurl);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(BPPayVo vo, HttpServletRequest req) {
        log.info("HPay 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String result = vo.getStatus();
        String remark = vo.getRemark();
        String sign = vo.getSign();
        String orderid = vo.getOrderId();
        String amount = vo.getAmount();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            

            Assert.isTrue(result.equals("1"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = "MerchantCode=["+vo.getMerchantCode()+"]OrderId=["+orderid+"]OutTradeNo=["+vo.getOutTradeNo()+"]Amount=["+amount+"]OrderDate=["+vo.getOrderDate()+"]BankCode=["+vo.getBankCode()+"]Remark=["+remark+"]Status=["+result+"]Time=["+vo.getTime()+"]TokenKey=["+mp.getSignKey()+"]";
    		String _sign = DigestUtils.signByMD5(paramsStr).toUpperCase();
            
            Assert.isTrue(_sign.equals(sign), ErrorCode.SC_30000_114.getType());

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
                    tradeService.doPayFlow(orderid, amount, loginName, mp, "",null);
                    return "OK";
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
        return "";
    }
}
