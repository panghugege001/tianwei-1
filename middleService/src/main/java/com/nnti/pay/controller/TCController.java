package com.nnti.pay.controller;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

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
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.TCPayVO;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description 天诚
 */
@Controller
@RequestMapping("/tc")
public class TCController extends BasePayController {

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
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);

        validationAmountCutAll(loginName, merchantPay);

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");
        Assert.notEmpty(orderId);

        TCPayVO vo = JSON.readValue(merchantPay.getRemain(), TCPayVO.class);
        String qrtype = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            qrtype = vo.getWap();
        } else {
        	qrtype = vo.getPc();
        }
        String sendtime = (new Date().getTime() / 1000) + "";
        vo.setOnline_pay(merchantPay.getMerchantCode(),qrtype,orderId,orderAmount,sendtime, merchantPay.getNotifyUrl(), merchantPay.getShopUrl());
        vo.setRisklevel("1");
        
        String content = "merchant="+vo.getMerchant()+"&qrtype="+ vo.getQrtype()+"&customno="+ vo.getCustomno()+"&money="+vo.getMoney()+"&sendtime="+vo.getSendtime()
        +"&notifyurl="+vo.getNotifyurl()+"&backurl="+vo.getBackurl()+"&risklevel="+vo.getRisklevel();
        String signature = DigestUtils.signByMD5(content+merchantPay.getSignKey());
        vo.setSign(signature);

        Map<String, Object> maps = MyUtils.describe(vo,"merchant", "qrtype", "customno", "money", "sendtime",
        		"notifyurl", "backurl", "risklevel","sign");
		log.info("签名 "+signature);
        log.info(orderId + ":请求参数：" + maps);
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(merchantPay.getPayUrl());
        returnVo.setParams(maps);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(TCPayVO vo, HttpServletRequest req) {
        log.info("天诚支付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String status = vo.getState();
        String sign = vo.getSign();
        String customno = vo.getCustomno();
        String amount = vo.getMoney();
        String qrtype = vo.getQrtype();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.ZBPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = customno.split("_")[2];
            if(qrtype.contains("h5")){
            	loginName = "wap_"+loginName;
            }
            String merId = "78";

            Assert.isTrue("1".equals(status), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String content = "merchant="+vo.getMerchant()+"&qrtype="+ vo.getQrtype()+"&customno="+ vo.getCustomno()+"&sendtime="+vo.getSendtime()+"&orderno="+vo.getOrderno()
            +"&money="+vo.getMoney()+"&paytime="+vo.getPaytime()+"&state="+vo.getState();
            String _sign = DigestUtils.signByMD5(content+mp.getSignKey());
            
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
                    tradeService.doPayFlow(customno, amount, loginName, mp, "",null);
                    return "success";
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
