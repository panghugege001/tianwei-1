package com.nnti.pay.controller;

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
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.CHPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description 附言支付
 */
@Controller
@RequestMapping("/fy")
public class FYController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 微信、支付宝附言
     */
	@ResponseBody
    @RequestMapping(value = "/zfb_wx", method = RequestMethod.POST)
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

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
 
        if(bankcode == null){
        	bankcode = "";
        }
		
		ReturnVo returnVo = new ReturnVo();
        returnVo.setType("7");
        returnVo.setData("/erweima/"+mp.getMerchantCode());
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(CHPayVo vo, HttpServletRequest req) {
        log.info("CHPay 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String r1_Code = vo.getR1_Code();
        String r8_MP = vo.getR8_MP();
        String hmac = vo.getHmac();
        String orderid = vo.getR6_Order();
        String amount = vo.getR3_Amt();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = r8_MP.split(",")[0];
            String merId = r8_MP.split(",")[1];
            

            Assert.isTrue(r1_Code.equals("1"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = changeNull(vo.getP1_MerId())+changeNull(vo.getR0_Cmd())+changeNull(vo.getR1_Code())+changeNull(vo.getR2_TrxId())+
            				   changeNull(vo.getR3_Amt())+changeNull(vo.getR4_Cur())+changeNull(vo.getR5_Pid())+changeNull(vo.getR6_Order())+
            				   changeNull(vo.getR8_MP())+changeNull(vo.getR9_BType())+changeNull(vo.getRo_BankOrderId())+changeNull(vo.getRp_PayDate());
            //System.out.println("paramsStr:"+paramsStr);
            String _hmac = DigestUtils.hmacSign(paramsStr,mp.getSignKey());
            
            Assert.isTrue(_hmac.equals(hmac), ErrorCode.SC_30000_114.getType());

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
    
    private String changeNull(String src){
    	if(src==null){
    		return "";
    	}
    	return src;
    }
}
