package com.nnti.pay.controller;

import java.util.Arrays;
import java.util.List;
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
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.WFPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description 五福支付
 */
@Controller
@RequestMapping("/wf")
public class WFController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 所有支付
     */
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

        WFPayVo vo = JSON.readValue(mp.getRemain(), WFPayVo.class);

        String type = "";
        String svcName = "UniThirdPay";
        String showCashier = "1";
        List list = Arrays.asList(new Object[]{"10","20","30","40","50","60","70","80","90","100","200","300","400","500","600","700","800","900","1000"});
        if (usetype == Constant.USE_TYPE_WEB) {
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
                if(Double.parseDouble(orderAmount) < 2000){
                	Assert.isTrue(list.contains(orderAmount), "该支付2000以下只支持如下下单金额：</br>10、20、30、40、50、60、70、80、90</br>100、200、300、400、500、600、700、800、900、1000");
                }
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
            }else if (mp.getPayWay().equals(7)) {
                type = vo.getWap();// wap QQ
            }else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                type = bankcode;// 在线支付
                svcName= "gatewayPay";
            }else if (mp.getPayWay().equals(4)) {
                type = vo.getWap();// 快捷
                svcName= "wapQuickPay";
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
                type = bankcode;// 在线支付
            } else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();//快捷
                svcName= "pcQuickPay";
            } else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// wap 京东
            } else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// wap 银联
            }
        }
        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        
        Double d = Double.parseDouble(orderAmount) * 100; //现在验证完了在转化为分
        orderAmount = String.valueOf(d.intValue());
        // 组织请求数据
        vo.setOnline_pay(svcName,mp.getMerchantCode(),orderId,type,"apple", orderAmount,"","","", mp.getNotifyUrl(), mp.getShopUrl(),showCashier, loginName + "," + mp.getId());

        Map<String, Object> maps = MyUtils.describe(vo, "svcName", "merId", "merchOrderId", "tranType",  "pName", "amt",
       		 "notifyUrl","retUrl","showCashier","merData");
       /*String paramsStr = vo.getAmt()+vo.getMerData()+vo.getMerId()+vo.getMerchOrderId()+vo.getNotifyUrl()+vo.getpName()+
    		   vo.getRetUrl()+vo.getShowCashier()+vo.getSvcName()+vo.getTranType();*/
       String paramsStr = MyUtils.obj2UrlParamNoKey(false, maps);
       String sign = DigestUtils.md5(paramsStr+mp.getSignKey());
       log.info("签名：" + sign);
       vo.setMd5value(sign);
       
       maps.put("md5value", sign);

       log.info(orderId + ":五福支付请求参数：" + maps);
       ReturnVo returnVo = new ReturnVo();
      /* Integer payWay = mp.getPayWay();
		if (payWay == 13
				|| ((payWay == 1 || payWay == 2 || payWay == 7 || payWay == 10) && usetype == Constant.USE_TYPE_WEB)) {
			String result = MyWebUtils.getHttpContentByBtParam(mp.getPayUrl(), MyUtils.obj2UrlParamSorted(false,maps));

			log.info(orderId + ":返回结果：" + result);

			JSONObject json = JSONObject.fromObject(result);
			String code = json.getString("Code");
			if (!"200".equals(code)) {
				Assert.isTrue(false, json.getString("Message"));
			}

			String backQrCodeUrl = json.getString("QrCodeUrl");

			returnVo.setType("2");
			returnVo.setData(backQrCodeUrl);
		}
		if ((payWay == 1 || payWay == 2 || payWay == 7 || payWay == 10) && usetype == Constant.USE_TYPE_PC) {
		}*/
		returnVo.setType("1");
		returnVo.setUrl(mp.getPayUrl());
		returnVo.setParams(maps);

       return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(WFPayVo vo, HttpServletRequest req) {
        log.info("五福支付回调 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String status = vo.getStatus();
        String remark = vo.getMerData();
        String sign = vo.getMd5value();
        String orderid = vo.getMerchOrderId();
        String amount = vo.getAmt();
        try {
        	amount = NumericUtil.div(Double.valueOf(amount), 100)+"";
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            

            Assert.isTrue("0".equals(status), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            Map<String, Object> maps = MyUtils.describe(vo, "status", "orderStatusMsg", "merchOrderId", "orderId",  "amt", "tranTime",
              		 "merId","merData");
            String paramsStr = MyUtils.obj2UrlParamNoKey(false, maps);
            String _sign = DigestUtils.md5(paramsStr+mp.getSignKey());
            
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
