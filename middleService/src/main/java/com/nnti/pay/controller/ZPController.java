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
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.ZPPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * @author pony
 * @Description 直付支付
 */
@Controller
@RequestMapping("/zp")
public class ZPController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 所有支付
     */
    @SuppressWarnings({ "unchecked" })
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
        MerchantPay mp = merchantPayService.findById(platformId);

        validationMerchantPay(orderAmount, mp);
        
        
        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "300");
        
        Double d = Double.parseDouble(orderAmount) * 100; //现在验证完了在转化为分
        orderAmount = String.valueOf(d.intValue());
        
        ZPPayVo vo = JSON.readValue(mp.getRemain(), ZPPayVo.class);

        String mobile = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            mobile = "Y";
        } else {
        	mobile = "N";
        }
        String timestamp = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(),"easypay.trade.pay",orderAmount,mp.getNotifyUrl(),mp.getShopUrl(), timestamp,
        		"apple",orderId,mobile, loginName + "," + mp.getId());
        

        Map<String, Object> maps = MyUtils.describe(vo, "key", "method", "money", "notify",  "platform", "timestamp",
        		 "title", "trade_no","memo","mobile","redirect");
        String paramsStr = MyUtils.obj2UrlParamSorted(false,maps);
        String sign = DigestUtils.md5(paramsStr+"&secret="+mp.getSignKey());
        sign = sign.toLowerCase();
        log.info("签名：" + sign);
        vo.setSign(sign);

        maps.put("sign", sign);
        paramsStr = paramsStr + "&sign="+sign;
        log.info(orderId + ":请求参数：" + paramsStr);
        
        String result = MyWebUtils.getHttpContentByBtParam(mp.getPayUrl(),paramsStr);
        System.out.println(result);
	    
	    JSONObject json = JSONObject.fromObject(result);
	    
	    Assert.isTrue(json.containsKey("page_url"), "获取支付链接异常，请稍后在尝试");
        
        String page_url = json.getString("page_url");
        String status = json.getString("status");
        String message = json.getString("message");
        
        Assert.isTrue("SUCCESS".equals(status), message);
	    
	    ReturnVo returnVo = new ReturnVo();
	    returnVo.setType("1");
	    returnVo.setUrl(page_url);
	    returnVo.setParams(null);
	    return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(ZPPayVo vo, HttpServletRequest req) {
        log.info("直付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String remark = vo.getMemo();
        String sign = vo.getSign();
        String orderid = vo.getTrade_no();
        String amount = vo.getMoney();
        
        try {
        	amount = NumericUtil.div(Double.valueOf(amount), 100)+"";
            Assert.isTrue(validationTrustIp(req, DictionaryType.ZPPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            

            //Assert.isTrue(result.equals("1"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            

            Map<String, Object> maps = MyUtils.describe(vo, "key", "sn", "trade_no", "platform",  "title", "memo",
            		 "money", "sysid","finish","time");
            String paramsStr = MyUtils.obj2UrlParamSorted(false,maps);
            String _sign = DigestUtils.md5(paramsStr+"&secret="+mp.getSignKey());
            
            Assert.isTrue(_sign.equalsIgnoreCase(sign), ErrorCode.SC_30000_114.getType());

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
                    return "SUCCESS";
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
