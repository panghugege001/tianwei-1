package com.nnti.pay.controller;

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
import com.nnti.pay.controller.vo.ZPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * @author pony
 * @Description 知付
 */
@Controller
@RequestMapping("/zhip")
public class ZPayController extends BasePayController {

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

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"300");
        Assert.notEmpty(orderId);

        ZPayVo vo = new ZPayVo();
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        
        String Mode = "";
        String BankCode = "";
        if (merchantPay.getPayWay().equals(2)) {
        	Mode = "4";
        	BankCode="WEIXIN";
        } else if (merchantPay.getPayWay().equals(1)) {
        	Mode = "8";;// 支付宝
        	BankCode="ALIPAY";
        }

        vo.setOnline_pay(orderId,Mode,BankCode,orderAmount,merchantPay.getMerchantCode(),  merchantPay.getNotifyUrl(), merchantPay.getShopUrl(), loginName + "," + merchantPay.getId());
        
        Map<String, Object> maps = MyUtils.describe(vo,"CustomerId", "Mode", "BankCode", "Money", "UserId",
        		"CallBackUrl", "ReturnUrl", "Message");

        String signature = DigestUtils.getSortedSign(maps, "&Key="+merchantPay.getSignKey()).toLowerCase();

        vo.setSign(signature);
		log.info("签名 "+signature);
		maps.put("Sign", signature);    
        log.info(orderId + ":请求参数：" + maps);
        String paramsStr = MyUtils.obj2UrlParamSorted(false,maps);
        
        String result = MyWebUtils.getHttpContentByBtParam(merchantPay.getPayUrl(),paramsStr);
        System.out.println(result);
	    
	    JSONObject json = JSONObject.fromObject(result);
        
        boolean status = json.getBoolean("Status");
        int code = json.getInt("Code");
        
        Assert.isTrue(status, ErrorCode.SC_30000_111.getType());
        Assert.isTrue(code==0, ErrorCode.SC_30000_111.getType());
        
        JSONObject Data = json.getJSONObject("Data");
        String Url = Data.getString("Url");
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(Url);
        returnVo.setParams(maps);

        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(ZPayVo vo, HttpServletRequest req) {
        log.info("知付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        Integer status = vo.getStatus();
        String attach = vo.getMessage();
        String sign = vo.getSign();
        String orderid = vo.getCustomerId();
        String amount = vo.getMoney();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.ZBPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = attach.split(",")[0];
            String merId = attach.split(",")[1];

            Assert.isTrue(status == 1, ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            Map<String, Object> maps = MyUtils.describe(vo,"CustomerId", "OrderId","Money", "Status", "Time","Message","Type");

            String _sign = DigestUtils.getSortedSign(maps, "&Key="+mp.getSignKey()).toLowerCase();
            
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
