package com.nnti.pay.controller;

import java.util.Arrays;
import java.util.List;
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
import com.nnti.pay.controller.vo.P686PayVo;
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
 * @Description 686支付
 */
@Controller
@RequestMapping("/p686")
public class P686Controller extends BasePayController {

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
        
        P686PayVo vo = new P686PayVo();
        
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        
        List list = Arrays.asList(new Object[]{"10","20","30","50","100","200","300"});

        String payCode = "";
        if (mp.getPayWay().equals(2)) {
        	payCode="wxpay";
            Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>10,20,30,50,100,200,300");
        } else if (mp.getPayWay().equals(1)) {
        	payCode="alipay";
        }
        
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(),payCode,  orderAmount,orderId, mp.getNotifyUrl(), loginName + "," + mp.getId());

        Map<String, Object> maps = MyUtils.describe(vo,"code", "payCode", "tradeAmount", "outOrderNo", "notifyUrl","goodsClauses");
        
        String signature = DigestUtils.getSortedSign(maps, "&key="+mp.getSignKey()).toLowerCase();

        vo.setSign(signature);
		log.info("签名 "+signature);
		maps.put("sign", signature);    
        log.info(orderId + ":请求参数：" + maps);
        String paramsStr = MyUtils.obj2UrlParamSorted(false,maps);
        
        String result = MyWebUtils.getHttpContentByBtParam(mp.getPayUrl(),paramsStr);
        System.out.println(result);
        
	    JSONObject json = JSONObject.fromObject(result);
        
        String payState = json.getString("payState");
        String message = json.getString("message");
        if("订单匹配不足".equals(message)){
        	message = "下单失败，请您重新下单！";
        }
        Assert.isTrue("success".equals(payState), message);

        String url = json.getString("url");
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(url);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(P686PayVo vo, HttpServletRequest req) {
        log.info("686支付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String msg = vo.getMsg();
        String code = vo.getCode();
        String remark = vo.getGoodsClauses();
        String orderid = vo.getOutOrderNo();
        String amount = vo.getTradeAmount();
        String sign = vo.getSign();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            

            Assert.isTrue("0".equals(code), ErrorCode.SC_30000_111.getType());
            Assert.isTrue("SUCCESS".equals(msg), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            Map<String, Object> maps = MyUtils.describe(vo,"outOrderNo", "goodsClauses", "tradeAmount", "shopCode", "code","nonStr","msg");
            
            String _sign = DigestUtils.getSortedSign(maps, "&key="+mp.getSignKey()).toLowerCase();    
            
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
