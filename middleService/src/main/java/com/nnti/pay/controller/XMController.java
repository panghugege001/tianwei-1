package com.nnti.pay.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.payutils.HttpsUtil;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.security.EncryptionUtil;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XbPayVo;
import com.nnti.pay.controller.vo.XlbResponseVo;
import com.nnti.pay.controller.vo.XmPayVo;
import com.nnti.pay.controller.vo.XmResponseVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * Created by addis on 2017/7/26. 新码 支付
 */
@Controller
@RequestMapping("/xm")
public class XMController extends BasePayController {  

	@Autowired
	private IBasicService basicService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private ITradeService tradeService;

	@ResponseBody
	@RequestMapping("zfb_wx")
	public Response zfb_wx(HttpServletRequest req,
			@RequestParam(value = "requestData", required = false) String requestData) throws Exception {

		PayRequestVo payRequestVo = pasePayRequest(req, requestData);
		String loginName = payRequestVo.getLoginName();
		Long platformId = payRequestVo.getPlatformId();
		Integer usetype = payRequestVo.getUsetype();
		String customerIp = "127.0.0.1";
		String orderAmount = payRequestVo.getOrderAmount();

		Assert.notEmpty(loginName, platformId, orderAmount, usetype);
		loginName = loginName.trim();

		Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");
		
		String amount = NumericUtil.mul(Double.valueOf(orderAmount), 100).longValue() + "";

		User user = basicService.getUser(loginName);
		validationLoginName(loginName, user);

		// 根据商户号查询商户信息
		MerchantPay mp = merchantPayService.findById(platformId);
		validationMerchantPay(orderAmount, mp);

		validationAmountCutAll(loginName, mp);

		String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

		if (usetype == Constant.USE_TYPE_WEB) {
			loginName = "wap_" + loginName;
		}

		String orderDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		XmPayVo vo = gson.fromJson(mp.getRemain(), XmPayVo.class);
		
		
		vo.setZfbWx(mp.getMerchantCode(), orderId, amount, mp.getNotifyUrl(), "abc", "abc",loginName + "," + mp.getId(), orderDate);

		String paramsStr = MyUtils.obj2UrlParam(vo, true, "attach_content", "back_notify_url","branch_id", "messageid", "nonce_str",
				"out_trade_no", "pay_type", "prod_desc", "prod_name", "total_fee");
		vo.setSign(DigestUtils.signByMD5Byaddis(paramsStr, mp.getSignKey()).toUpperCase());
		log.info("签名 "+vo.getSign());

		Map pays = MyUtils.describe(vo, "sign", "messageid", "out_trade_no", "back_notify_url", "branch_id",
				"prod_name", "prod_desc", "pay_type", "total_fee", "nonce_str","attach_content");

		log.info(orderId + "请求报文:" + JSON.writeValueAsString(pays));
		

		byte[] resByte = HttpsUtil.httpsPost(mp.getPayUrl(), JSON.writeValueAsString(pays));
		if (null == resByte) {
			log.error("返回报文为空");
		} else {
			log.info("返回数据:" + new String(resByte, "UTF-8"));
			XmResponseVo responseVo = gson.fromJson(new String(resByte, "UTF-8"), XmResponseVo.class);
		    Assert.isTrue(responseVo.getResultCode().equals("00"), responseVo.getResultDesc());
		    Assert.isTrue(responseVo.getResCode().equals("00"), responseVo.getResDesc());
		    
		    String qrcode = responseVo.getPayUrl();

	        log.info(orderId + ":返回二维码：" + qrcode);
	        ReturnVo returnVo = new ReturnVo();
	        returnVo.setType("2");
	        returnVo.setData(qrcode);
	        return transfer(returnVo);
		}
		return null;
	} 
 
	@ResponseBody
	@RequestMapping("/zfb_wx_return")
	public String zfb_wx_return(XmPayVo vo, HttpServletRequest req) {
	    log.info("新码接收回调通知参数:"+vo);
	    String attach_content = vo.getAttachContent();  
		
		DecimalFormat df = new DecimalFormat("#.00");
		try {

			Assert.isTrue(validationTrustIp(req, DictionaryType.XM_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
			
			String loginName = attach_content.split(",")[0];
	        String merId = attach_content.split(",")[1];

			Assert.isTrue("02".equals(vo.getStatus()), "新码支付宝支付返回支付结果为处理不成功：State：" + vo.getStatus());

			MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
			Assert.notEmpty(mp);
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
			
	        
	        String paramsStr = MyUtils.obj2UrlParam(vo, true,"attachContent", "branchId", "createTime", "nonceStr", "orderAmt",
					"orderNo", "outTradeNo", "payType", "productDesc", "resCode","resDesc","resultCode","resultDesc","status");
			String _sign = DigestUtils.signByMD5Byaddis(paramsStr, mp.getSignKey()).toUpperCase();
	        
			

			log.info("_sign:" + _sign);
			Assert.isTrue(vo.getSign().equals(_sign), "新码支付宝支付回调签名验证失败：orderId ：" + vo.getOutTradeNo());
			
			Double amount = NumericUtil.div(Double.valueOf(vo.getOrderAmt()), 100);

			Boolean lockFlag = false;
			final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginName));
			log.info("lock:" + lock);
			try {
				lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
			} catch (Exception e) {
				log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
				lockFlag = true;
			}

			try {
				if (lockFlag) {
					tradeService.doPayFlow(vo.getOutTradeNo(), amount.toString(), loginName, mp, "",null);
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
			log.error("新码支付宝或微信回调异常：", e);
		}

		return ERROR;
	}
}
