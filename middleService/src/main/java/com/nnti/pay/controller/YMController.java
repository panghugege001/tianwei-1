package com.nnti.pay.controller;

import java.util.Date;
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
import com.nnti.pay.controller.vo.YMPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * 2018/06/12. 怡秒支付
 */
@Controller
@RequestMapping("/ym")
public class YMController extends BasePayController {

	@Autowired
	private IBasicService basicService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private ITradeService tradeService;

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

		// 根据商户号查询商户信息
		MerchantPay mp = merchantPayService.findById(platformId);
		validationMerchantPay(orderAmount, mp);
		
		validationAmountCutAll(loginName, mp);
		
		String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
		
		YMPayVo vo = gson.fromJson(mp.getRemain(), YMPayVo.class);
		
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
                type = bankcode;// 在线支付
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
                type = bankcode;// 在线支付
            } else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();//快捷
            } else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// wap 京东
            } else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// wap 银联
            }
        }
        vo.setPay_bankcode(type);


		String orderDate = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());
		vo.setOnlinePay(mp.getMerchantCode(), orderId, orderDate, mp.getNotifyUrl(), mp.getShopUrl(), orderAmount, loginName+","+mp.getId(),"abc", "1", "abc");

		String paramsStr = MyUtils.obj2UrlParam(vo, true, "pay_amount", "pay_applydate", "pay_bankcode", "pay_callbackurl",
				"pay_memberid", "pay_notifyurl", "pay_orderid");
		vo.setPay_md5sign(DigestUtils.md5(paramsStr + "&key=" + mp.getSignKey()));
		log.info("签名 " +vo.getPay_md5sign());

		Map pays = MyUtils.describe(vo, "pay_md5sign", "pay_memberid", "pay_orderid", "pay_applydate", "pay_bankcode",
				"pay_notifyurl", "pay_callbackurl", "pay_amount", "pay_attach","pay_productname","pay_productnum","pay_productdesc","pay_producturl");

		log.info(orderId + "请求报文:" + JSON.writeValueAsString(pays));

		ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":ym online_pay请求参数：" + pays);
        return transfer(returnVo);
	}
	
	

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(YMPayVo vo, HttpServletRequest req) {

        log.info("ym 接收参数：" + MyWebUtils.getRequestParameters(req));

        String sign = vo.getSign();
        String remark = vo.getAttach();
        String returncode = vo.getReturncode();

		try {

			Assert.isTrue(validationTrustIp(req, DictionaryType.YMPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			Assert.isTrue("00".equals(returncode), "返回支付结果为处理不成功：State：" + vo.getReturncode());

            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];

            //根据商户号查询商户信息
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

			String paramsStr = MyUtils.obj2UrlParam(vo, true, "amount", "datetime", "memberid", "orderid",
					"returncode","transaction_id");
			String _sign = DigestUtils.md5(paramsStr + "&key=" + mp.getSignKey());

			log.info("_sign:" + _sign);
			Assert.isTrue(sign.equals(_sign), "YM支付回调签名验证失败：orderId ：" + vo.getOrderid());

			Boolean lockFlag = false;
			final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginName));
			try {
				lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
			} catch (Exception e) {
				log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
				lockFlag = true;
			}

			try {
				if (lockFlag) {
					tradeService.doPayFlow(vo.getOrderid(), vo.getAmount(), loginName, mp, "",null);
					return OK;
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
			log.error("YM支付回调异常：", e);
		}

		return "";
	}
}
