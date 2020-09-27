package com.nnti.pay.controller;

import java.util.Map;
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
import com.nnti.common.utils.MyUtils;
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

import net.sf.json.JSONObject;

/**
 * @author pony
 * @Description 畅汇支付
 */
@Controller
@RequestMapping("/ch")
public class CHController extends BasePayController {

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
        
        //orderAmount = orderAmount+"."+((int)Math.random()*90+10);//2位小数

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        
        CHPayVo vo = JSON.readValue(mp.getRemain(), CHPayVo.class);

        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
            }else if (mp.getPayWay().equals(7)) {
                type = vo.getWap();// wap QQ
            }else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                //type = bankcode;// 在线支付
            }else if (mp.getPayWay().equals(4)) {
                type = vo.getWap();// 快捷
            }else if (mp.getPayWay().equals(10)) {
                type = vo.getWap();// wap 京东
            }else if (mp.getPayWay().equals(13)) {
                type = vo.getWap();// wap 银联
            }
        } else {
            if (mp.getPayWay().equals(2)) {
                type = vo.getPc();//PC微信扫码
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getPc();//PCQQ
            } else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                //type = bankcode;// 在线支付
            } else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();//快捷
            } else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// wap 京东
            } else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// wap 银联
            }
        }
        if(bankcode == null){
        	bankcode = "";
        }
        // 组织请求数据
        vo.setOnline_pay("Buy",mp.getMerchantCode(),orderId,"CNY", orderAmount,"war3","","",mp.getNotifyUrl(), loginName + "," + mp.getId(),type,bankcode,"",mp.getShopUrl());
        String param = vo.getP0_Cmd()+vo.getP1_MerId()+vo.getP2_Order()+vo.getP3_Cur()+vo.getP4_Amt()+vo.getP5_Pid()+vo.getP8_Url()+vo.getP9_MP()
        			   +vo.getPa_FrpId()+vo.getPg_BankCode()+vo.getPi_Url();

        String hmac = DigestUtils.hmacSign(param,mp.getSignKey());
        log.info("签名：" + hmac);
        vo.setHmac(hmac);

        ReturnVo returnVo = new ReturnVo();
        Map pays = MyUtils.describe(vo, "p0_Cmd", "p1_MerId", "p2_Order", "p3_Cur", "p4_Amt", "p5_Pid", "p6_Pcat", "p7_Pdesc", "p8_Url", "p9_MP",
        		"pa_FrpId", "pg_BankCode", "ph_Ip", "pi_Url", "hmac");
        log.info(orderId + ":请求参数：" + pays);

        Integer payWay = mp.getPayWay();
        if(payWay ==13||payWay == 10 || ((payWay == 1 || payWay == 2 ||payWay == 7)  && usetype == Constant.USE_TYPE_PC )){
        	String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        	
        	log.info(orderId + ":返回结果：" + result);
        	
        	JSONObject json = JSONObject.fromObject(result);
        	String r0_Cmd = json.getString("r0_Cmd");
        	String r1_Code = json.getString("r1_Code");
        	if(!"1".equals(r1_Code)){
        		Assert.isTrue(false, "支付失败");
        	}
        	String p1_MerId = json.getString("p1_MerId");
        	String r2_TrxId = json.getString("r2_TrxId");
        	String r3_PayInfo = json.getString("r3_PayInfo");
        	String r4_Amt = json.getString("r4_Amt");
        	String r7_Desc = json.getString("r7_Desc");
        	String rhmac = json.getString("hmac");
        	
        	String returnS =r0_Cmd+p1_MerId+r1_Code+r2_TrxId+r3_PayInfo+r4_Amt+r7_Desc;
        	String _rhmac = DigestUtils.hmacSign(returnS,mp.getSignKey());
            log.info("请求返回签名：" + _rhmac);
            /*if(!_rhmac.equals(rhmac)){
        		Assert.isTrue(false, "支付失败");
        	}*/
        	
        	returnVo.setType("2");
        	returnVo.setData(r3_PayInfo);
        }
        if(payWay ==4 || ((payWay == 1 || payWay == 2 ||payWay == 7) && usetype == Constant.USE_TYPE_WEB)){
            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        }
        
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
