package dfh.action.customer;

import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.action.SubActionSupport;
import dfh.jubaobar.pay.JubaoPay;
import dfh.jubaobar.pay.RSA;
import dfh.security.EncryptionUtil;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.utils.AxisUtil;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DigestUtils;
import dfh.utils.MD5;
import dfh.utils.Yom;

public class LfWxReturnAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;   

	private Logger log = Logger.getLogger(LfWxReturnAction.class);
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	     
	public String payLfwxReturn() {
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String apiCode=(String)this.getRequest().getParameter("apiCode");// 接口编号
			String versionCode=(String)this.getRequest().getParameter("versionCode");//版本号
			//String currency="CNY";// 币种
			String amount=(String)this.getRequest().getParameter("amount");//订单金额
			String handlerResult=(String)this.getRequest().getParameter("handlerResult");//处理结果
			String tradeOrderCode=(String)this.getRequest().getParameter("tradeOrderCode");//交易订单号
			String outOrderId=(String)this.getRequest().getParameter("outOrderId");//唯一订单号
			String inputCharset=(String)this.getRequest().getParameter("inputCharset");//：UTF-8
			String signType=(String)this.getRequest().getParameter("signType");//：MD5
			String partner=(String)this.getRequest().getParameter("partner");//合作方编号
			String returnParam=(String)this.getRequest().getParameter("returnParam");//将请求参数原值返回
			String sign=(String)this.getRequest().getParameter("sign"); 
			String key="53CD1F703BD380CA7C6EB730A018715B";
			if(!"0000".equals(handlerResult)){
				log.info("乐富微信支付返回支付结果为处理不成功：handlerResult："+handlerResult);
				return null;
			}
			String signstr="amount="+amount+"&apiCode="+apiCode+/*"&currency="+currency+*/"&handlerResult="+handlerResult+"&inputCharset="+inputCharset
					+"&outOrderId="+outOrderId+"&partner="+partner+"&returnParam="+returnParam+"&signType="+signType
					+"&tradeOrderCode="+tradeOrderCode+"&versionCode="+versionCode+key;
			String mySign = DigestUtils.md5DigestAsHex(signstr.getBytes("UTF-8"));
			if(!mySign.equals(sign)){
				log.info("乐富微信支付回调签名验证失败：outOrderId："+outOrderId);
				return null;
			}
			
			log.info("TRADE 接收订单号为" + outOrderId );
			///***************************begin
			/*String str = "payReturn"+OrdId+TrxId ;
			Long nowTiem = System.currentTimeMillis();
	        Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String, Long> entry = it.next();
	            String keyStr = entry.getKey();
	            Long longTime = cacheMap.get(keyStr);
	            if (nowTiem - longTime > 1000 * 30) {
	                it.remove();
	            }
	        }
	        if (cacheMap.get(str) != null) {
//	            GsonUtil.GsonObject("60秒内不能重复提交同一平台。");
	        	log.info("汇付30秒内重复提交");
	            return null;
	        }
	        cacheMap.put(str, nowTiem);*/
			///***************************end
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayLfWxzf", new Object[] { outOrderId, Double.parseDouble(amount),returnParam,0}, String.class);
			if (returnmsg == null) {
				log.info("交易成功,你支付的" + amount + "元已经到账,此次交易的订单号为" + outOrderId );
				out.print("SUCCESS");
				return null;
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("乐富微信异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	/***
	 * 迅联宝支付宝
	 * @return
	 */
	public String payXlbZfbReturn(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String apiName = (String)this.getRequest().getParameter("apiName");//PAY_RESULT_NOTIFY
			String notifyTime = (String)this.getRequest().getParameter("notifyTime");//通知时间
			String tradeAmt = (String)this.getRequest().getParameter("tradeAmt"); //实际的支付金额
			String merchNo = (String)this.getRequest().getParameter("merchNo");//商户号
			String merchParam = (String)this.getRequest().getParameter("merchParam");  //支付时上送的商户参数
			String orderNo = (String)this.getRequest().getParameter("orderNo");//商户订单号
			String tradeDate = (String)this.getRequest().getParameter("tradeDate");//商户交易日期
			String accNo = (String)this.getRequest().getParameter("accNo");//支付平台订单号
			String accDate = (String)this.getRequest().getParameter("accDate");//支付平台订单支付日期
			String orderStatus =(String)this.getRequest().getParameter("orderStatus");//处理结果0 未支付，1 成功，2失败
			String signMsg =(String)this.getRequest().getParameter("signMsg");//处理结果
			String key="c97f72752acc35fe4f3ab45c06a78b87";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("\n\n\n");
			log.info("==============迅联宝支付宝支付回调======================");
			log.info("");
			log.info("\n\n\n");
			if(!merchNo.equals("210001440011109")){
				log.error("商户号不正确");
				return "商户号不正确";
			}
			if(!orderStatus.equals("1")){
				log.error("充值失败");
				return "充值失败";
			}
			String srcMsg = String.format("apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s",
							apiName, notifyTime, tradeAmt, merchNo,
							merchParam, orderNo, tradeDate, accNo, accDate,
							orderStatus);
			// 验证签名
			System.out.println("srcMsg==="+srcMsg);
			if (!signMsg.equalsIgnoreCase(signByMD5(srcMsg, key))) {
				log.error("迅联宝支付宝验证前面失败");
				return "验证失败";
			}
			log.info("X--X迅联宝支付宝支付接收订单号为" + orderNo );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayXlbZfb", new Object[] { orderNo, Double.parseDouble(tradeAmt),merchParam,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("迅联宝支付宝支付交易成功,你支付的" + tradeAmt + "元已经到账,此次交易的订单号为" + orderNo );
				out.print("SUCCESS");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("迅联宝异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public String payXbwxReturn(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String version = (String)this.getRequest().getParameter("Version");
			String merchantCode = (String)this.getRequest().getParameter("MerchantCode");
			String orderId = (String)this.getRequest().getParameter("OrderId"); //我们的订单号
			String orderDate = (String)this.getRequest().getParameter("OrderDate");
			String tradeIp = (String)this.getRequest().getParameter("TradeIp");
			
			String serialNo = (String)this.getRequest().getParameter("SerialNo");//新贝平台交易流水号
			String amount = (String)this.getRequest().getParameter("Amount");
			
			String payCode = (String)this.getRequest().getParameter("PayCode");
			String state = (String)this.getRequest().getParameter("State");//处理结果
			String remark2 = (String)this.getRequest().getParameter("Remark2");//提交订单附带的账号信息
			String signValue = (String)this.getRequest().getParameter("SignValue");//：MD5
			String finishTime = (String)this.getRequest().getParameter("FinishTime");//：MD5
			String key="4mBvoq1dtL5eke9qrzy1fNwFGNSb4rXm";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			if(!"8888".equals(state)){
				log.info("新贝微信支付返回支付结果为处理不成功：State："+state);
				return null;
			}
			if(!merchantCode.equals("E02934")){
				log.info("商户号不正确"+merchantCode);
				return null;
			}
			String mySign = "Version=["+version+"]MerchantCode=["+merchantCode+"]OrderId=["+orderId+"]OrderDate=["+orderDate+"]TradeIp=["+tradeIp+"]SerialNo=["+serialNo+"]Amount=["+amount+"]PayCode=["+payCode+"]State=["+state+"]FinishTime=["+finishTime+"]TokenKey=["+key+"]";
			String mySign1 = "Version=["+version+"]MerchantCode=["+merchantCode+"]OrderId=["+orderId+"]OrderDate=["+orderDate+"]TradeIp=["+tradeIp+"]SerialNo=["+serialNo+"]Amount=["+df.format(Double.valueOf(amount))+"]PayCode=["+payCode+"]State=["+state+"]FinishTime=["+finishTime+"]TokenKey=["+key+"]";
			log.info("mySign---------->"+mySign+"\n");
			mySign = EncryptionUtil.encryptPassword(mySign).toUpperCase();
			mySign1 = EncryptionUtil.encryptPassword(mySign1).toUpperCase();    
			if(!mySign.equals(signValue) && !mySign1.equals(signValue)){
				log.info("新贝微信支付回调签名验证失败：orderId ："+orderId);
				return null;
			}
			 
			log.info("T--T新贝微信支付接收订单号为" + orderId );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayXbWxzf", new Object[] { orderId, Double.parseDouble(amount),remark2,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("新贝支付交易成功,你支付的" + amount + "元已经到账,此次交易的订单号为" + orderId );
				out.print("OK");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("新贝微信异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public String payKdWxZfReturn(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			String P_UserId = (String)this.getRequest().getParameter("P_UserId");
			String P_OrderId = (String)this.getRequest().getParameter("P_OrderId");   
			String P_FaceValue = (String)this.getRequest().getParameter("P_FaceValue"); //面值/金额
			String P_ChannelId = (String)this.getRequest().getParameter("P_ChannelId");
			String P_Price = (String)this.getRequest().getParameter("P_Price");  //实际充值金额
			
			String P_Notic = (String)this.getRequest().getParameter("P_Notic");//新贝平台交易流水号    
			String P_ErrCode = (String)this.getRequest().getParameter("P_ErrCode");
			
			String P_ErrMsg = (String)this.getRequest().getParameter("P_ErrMsg");
			String P_PostKey = (String)this.getRequest().getParameter("P_PostKey");//处理结果
			String SalfStr = "2fed5e622429470fbbe7879ec8f6f315";  
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("\n\n\n");
			log.info("==============口袋微信支付回调======================");
			log.info("P_UserId:"+P_UserId+"  P_OrderId:"+P_OrderId+"  P_FaceValue:"+P_FaceValue+"  P_ChannelId:"+P_ChannelId+"  P_Price:"+P_Price
					+"  P_Notic:"+P_Notic+"  P_ErrCode:"+P_ErrCode+"  P_ErrMsg:"+P_ErrMsg+"  P_PostKey:"+P_PostKey);
			log.info("\n\n\n");
			
			if(!P_UserId.equals("1002397")){
				log.error("商户号不正确");
				return "商户号不正确";
			}   
			if(!P_ErrCode.equals("0")){
				log.error("充值失败");
				return "充值失败";
			}
			
			P_PostKey = EncryptionUtil.encryptPassword(P_UserId+"|"+P_OrderId+"|"+""+"|"+""+"|"+P_Price+"|"+P_ChannelId+"|"+SalfStr);
			if(!P_PostKey.equals(P_PostKey)){
				log.info("口袋微信支付回调签名验证失败：orderId ："+P_OrderId);
				return null;
			}
			
			log.info("X--X口袋微信支付接收订单号为" + P_OrderId );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayKdWxZf", new Object[] { P_OrderId, Double.parseDouble(P_Price),P_Notic,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("口袋微信支付交易成功,你支付的" + P_Price + "元已经到账,此次交易的订单号为" + P_OrderId );
//				out.print("OK");
				out.print("errcode=0");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("口袋微信支付异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	
	/****
	 * 口袋微信支付2 回调
	 * 
	 */
	public String payKdWxZfReturn2(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			String P_UserId = (String)this.getRequest().getParameter("P_UserId");
			String P_OrderId = (String)this.getRequest().getParameter("P_OrderId");
			String P_FaceValue = (String)this.getRequest().getParameter("P_FaceValue"); //面值/金额
			String P_ChannelId = (String)this.getRequest().getParameter("P_ChannelId");
			String P_Price = (String)this.getRequest().getParameter("P_Price");  //实际充值金额
			
			String P_Notic = (String)this.getRequest().getParameter("P_Notic");//新贝平台交易流水号
			String P_ErrCode = (String)this.getRequest().getParameter("P_ErrCode");
			
			String P_ErrMsg = (String)this.getRequest().getParameter("P_ErrMsg");
			String P_PostKey = (String)this.getRequest().getParameter("P_PostKey");//处理结果
			String SalfStr = "017f1310e0ac4e43b79e2d70f4ce69a1";
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("\n\n\n");
			log.info("==============口袋微信支付2回调======================");
			log.info("P_UserId:"+P_UserId+"  P_OrderId:"+P_OrderId+"  P_FaceValue:"+P_FaceValue+"  P_ChannelId:"+P_ChannelId+"  P_Price:"+P_Price
					+"  P_Notic:"+P_Notic+"  P_ErrCode:"+P_ErrCode+"  P_ErrMsg:"+P_ErrMsg+"  P_PostKey:"+P_PostKey);
			log.info("\n\n\n");
			
			if(!P_UserId.equals("1002400")){
				log.error("商户号不正确");
				return "商户号不正确";
			}
			if(!P_ErrCode.equals("0")){
				log.error("充值失败");   
				return "充值失败";
			}
			
			P_PostKey = EncryptionUtil.encryptPassword(P_UserId+"|"+P_OrderId+"|"+""+"|"+""+"|"+P_Price+"|"+P_ChannelId+"|"+SalfStr);
			if(!P_PostKey.equals(P_PostKey)){
				log.info("口袋微信2支付回调签名验证失败：orderId ："+P_OrderId);
				return null;
			}
			
			log.info("X--X口袋微信2支付接收订单号为" + P_OrderId );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayKdWxZf2", new Object[] { P_OrderId, Double.parseDouble(P_Price),P_Notic,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("口袋微信2支付交易成功,你支付的" + P_Price + "元已经到账,此次交易的订单号为" + P_OrderId );
//				out.print("OK");
				out.print("errcode=0");
				return null;   
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {   
			System.out.println("\n\n\n**************************************************");
			e.printStackTrace();
			out.print("ERROR");    
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	/*****
	 * 口袋支付宝 and 口袋支付宝2 and 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3  回调
	 * @return
	 */
	public String payKdWxZfsReturn(){
		PrintWriter out = null;
		String P_OrderId = null ;
		try {    
			out = this.getResponse().getWriter();
			this.getResponse().setCharacterEncoding("UTF-8");
			System.out.println("============口袋支付宝  and 口袋支付宝2 and 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3============");
			
			String P_UserId = (String)this.getRequest().getParameter("P_UserId");
			P_OrderId = (String)this.getRequest().getParameter("P_OrderId");
			String P_FaceValue = (String)this.getRequest().getParameter("P_FaceValue"); //面值/金额   
			String P_ChannelId = (String)this.getRequest().getParameter("P_ChannelId");
			String P_Price = (String)this.getRequest().getParameter("P_Price");  //实际充值金额
			String P_Notic = (String)this.getRequest().getParameter("P_Notic");//新贝平台交易流水号
			String P_ErrCode = (String)this.getRequest().getParameter("P_ErrCode");
			String P_ErrMsg = (String)this.getRequest().getParameter("P_ErrMsg");
			String P_PostKey = (String)this.getRequest().getParameter("P_PostKey");//处理结果
			String P_CardId = (String)this.getRequest().getParameter("P_CardId");//处理结果
			String P_CardPass = (String)this.getRequest().getParameter("P_CardPass");//处理结果
			String P_PayMoney = (String)this.getRequest().getParameter("P_PayMoney");//处理结果
			
			
			
			String SalfStr = null;
			String payName="";
			log.info("商户号:"+P_UserId);
			// 充值状态
			if (!P_ErrCode.equals("0")) {
				log.error("充值失败");
				return "充值失败";
			}
			// 口袋微信支付1
			if (P_UserId.equals(Constants.KDWX1_MerNo)) {
				SalfStr = Constants.KDWX1_KEY;
				payName = "口袋微信支付";
			}
			// 口袋微信支付2
			else if (P_UserId.equals(Constants.KDWX2_MerNo)) {
				SalfStr = Constants.KDWX2_KEY;
				payName = "口袋微信支付2";
			}
			// 口袋微信支付3
			else if (P_UserId.equals(Constants.KDWX3_MerNo)) {
				SalfStr = Constants.KDWX3_KEY;
				payName = "口袋微信支付3";
			}
			// 口袋支付宝2
			else if (P_UserId.equals(Constants.KDZFB2_MerNo)) {
				SalfStr = Constants.KDZFB2_KEY;
				payName = "口袋支付宝2";
			}
			//口袋支付宝
			else if(P_UserId.equals(Constants.KDZFB_MerNo)){
				SalfStr = Constants.KDZFB_KEY;
				payName="口袋支付宝";
			}
           else {
				log.error("商户号不正确");
				return "商户号不正确";
			}
			log.info("\n\n\n");
			log.info("P_UserId:"+P_UserId+"  P_OrderId:"+P_OrderId+"  P_FaceValue:"+P_FaceValue+"  P_ChannelId:"+P_ChannelId+"  P_Price:"+P_Price
					+"  P_Notic:"+P_Notic+"  P_ErrCode:"+P_ErrCode+"  P_ErrMsg:"+P_ErrMsg+"  P_PostKey:"+P_PostKey);
			
			//String signMsg = EncryptionUtil.encryptPassword(P_UserId+"|"+P_OrderId+"|"+""+"|"+""+"|"+P_FaceValue+"|"+P_ChannelId+"|"+SalfStr);
			System.out.println(P_UserId+"|"+P_OrderId+"|"+""+"|"+""+"|"+P_FaceValue+"|"+P_ChannelId+"|"+P_FaceValue+"|"+""+"|"+SalfStr+"-------------------------");    
			String signMsg = EncryptionUtil.encryptPassword(P_UserId+"|"+P_OrderId+"|"+P_CardId+"|"+P_CardPass+"|"+P_FaceValue+"|"+P_ChannelId+"|"+P_PayMoney+"|"+P_ErrCode+"|"+SalfStr);
			
			System.out.println(signMsg+"===签名值sinMsg===");   
			if(!P_PostKey.equals(signMsg)){  
				log.info(payName+"支付回调签名验证失败：orderId ："+P_OrderId);
				return null;
			}
			
			log.info("X--X"+payName+"支付接收订单号为" + P_OrderId );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayKdWxZfs", new Object[] { P_OrderId, Double.parseDouble(P_Price),payName,P_Notic,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info(payName+"支付交易成功,你支付的" + P_Price + "元已经到账,此次交易的订单号为" + P_OrderId );
				out.print("errcode=0");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			System.out.println("===========异常订单号========="+P_OrderId);
			e.printStackTrace();
			out.print("ERROR");
			//LOG.info("支付异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	public String payKdZfReturn(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String P_UserId = (String)this.getRequest().getParameter("P_UserId");
			String P_OrderId = (String)this.getRequest().getParameter("P_OrderId");
			String P_FaceValue = (String)this.getRequest().getParameter("P_FaceValue"); //面值/金额
			String P_ChannelId = (String)this.getRequest().getParameter("P_ChannelId");
			String P_Price = (String)this.getRequest().getParameter("P_Price");  //实际充值金额
			
			String P_Notic = (String)this.getRequest().getParameter("P_Notic");//新贝平台交易流水号
			String P_ErrCode = (String)this.getRequest().getParameter("P_ErrCode");
			
			String P_ErrMsg = (String)this.getRequest().getParameter("P_ErrMsg");
			String P_PostKey = (String)this.getRequest().getParameter("P_PostKey");//处理结果
			String SalfStr = "4f4b9ebd2ba344ccbd70a3977c71b431";
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("\n\n\n");
			log.info("==============口袋支付回调======================");
			log.info("P_UserId:"+P_UserId+"  P_OrderId:"+P_OrderId+"  P_FaceValue:"+P_FaceValue+"  P_ChannelId:"+P_ChannelId+"  P_Price:"+P_Price
					+"  P_Notic:"+P_Notic+"  P_ErrCode:"+P_ErrCode+"  P_ErrMsg:"+P_ErrMsg+"  P_PostKey:"+P_PostKey);
			log.info("\n\n\n");
			
			if(!P_UserId.equals("1001242")){
				log.error("商户号不正确");
				return "商户号不正确";
			}
			if(!P_ErrCode.equals("0")){
				log.error("充值失败");
				return "充值失败";
			}
			
			P_PostKey = EncryptionUtil.encryptPassword(P_UserId+"|"+P_OrderId+"|"+""+"|"+""+"|"+P_Price+"|"+P_ChannelId+"|"+SalfStr);
			if(!P_PostKey.equals(P_PostKey)){
				log.info("口袋支付回调签名验证失败：orderId ："+P_OrderId);
				return null;
			}
			
			log.info("X--X口袋支付接收订单号为" + P_OrderId );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayKdZf", new Object[] { P_OrderId, Double.parseDouble(P_Price),P_Notic,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("口袋支付交易成功,你支付的" + P_Price + "元已经到账,此次交易的订单号为" + P_OrderId );
				out.print("OK");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("口袋支付异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	/**
	 * 汇付宝支付宝支付回调
	 * @return
	 */
	public String payHhbZfReturn(){
		PrintWriter out = null;
		String hhb_key = "";
		try {
			log.info("汇付宝回调开始---------");
			out = this.getResponse().getWriter();
			String result = (String)this.getRequest().getParameter("result");
			
			if (result.equals("1")) { //1为成功，其余均为失败
				String pay_message = (String)this.getRequest().getParameter("pay_message");
				String agent_id = (String)this.getRequest().getParameter("agent_id");
				String jnet_bill_no = (String)this.getRequest().getParameter("jnet_bill_no"); //汇付宝交易订单号
				String agent_bill_id = (String)this.getRequest().getParameter("agent_bill_id");
				String pay_type = (String)this.getRequest().getParameter("pay_type");
				String pay_amt = (String)this.getRequest().getParameter("pay_amt");
				String remark = (String)this.getRequest().getParameter("remark");
				String sign = (String)this.getRequest().getParameter("sign");
				
				//验证签名
				String c_sign = "result="+result+"&agent_id="+agent_id+"&jnet_bill_no="+jnet_bill_no+"&agent_bill_id="+agent_bill_id+
						"&pay_type="+pay_type+"&pay_amt="+pay_amt+"&remark="+remark+"&key="+hhb_key;
				log.info("汇付宝回调参数："+c_sign);
				
				c_sign = EncryptionUtil.encryptPassword(c_sign);
				if (!c_sign.equals(sign)){
					log.error("汇付宝验签失败，orderId="+agent_bill_id);
					out.print("error");
					return null;
				}
				
				String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayHhbZf", new Object[] { agent_bill_id, Double.parseDouble(pay_amt),remark,0}, String.class);
				log.info(returnmsg);
				if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
					log.info("汇付宝支付交易成功,你支付的" + pay_amt + "元已经到账,此次交易的订单号为" + agent_bill_id );
					out.print("OK");
					return null;
				}else{
					out.print(returnmsg);
				}
				return null;
				
			} else {
				log.error("汇付宝支付失败");
				out.print("ok");
			}
			
		} catch (Exception e){
			log.error("汇付宝回调异常", e);
			out.print("error");
		} finally {
			if (out != null){
				out.close();
			}
		}
		return null;
	}
	
	
	
	

	/**
	 * 汇付宝微信支付回调
	 * @return
	 */
	public String payHhbWxZfReturn(){
		PrintWriter out = null;
		String hhb_key = "834499D4528140A88BE8478B";
		try {
			log.info("-----------------------------汇付宝微信回调开始-----------------------------");
			out = this.getResponse().getWriter();
			String result = (String)this.getRequest().getParameter("result");
			
			if (result.equals("1")) { //1为成功，其余均为失败
				String pay_message = (String)this.getRequest().getParameter("pay_message");
				String agent_id = (String)this.getRequest().getParameter("agent_id");
				String jnet_bill_no = (String)this.getRequest().getParameter("jnet_bill_no"); //汇付宝交易订单号
				String agent_bill_id = (String)this.getRequest().getParameter("agent_bill_id");
				String pay_type = (String)this.getRequest().getParameter("pay_type");
				String pay_amt = (String)this.getRequest().getParameter("pay_amt");
				String remark = (String)this.getRequest().getParameter("remark");
				String sign = (String)this.getRequest().getParameter("sign");
				
				//验证签名
				String c_sign = "result="+result+"&agent_id="+agent_id+"&jnet_bill_no="+jnet_bill_no+"&agent_bill_id="+agent_bill_id+
						"&pay_type="+pay_type+"&pay_amt="+pay_amt+"&remark="+remark+"&key="+hhb_key;
				log.info("汇付宝微信回调参数："+c_sign);
				
				c_sign = EncryptionUtil.encryptPassword(c_sign);
				if (!c_sign.equals(sign)){
					log.error("汇付宝微信验签失败，orderId="+agent_bill_id);
					out.print("error");
					return null;
				}
				
				String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayHhbWxZf", new Object[] { agent_bill_id, Double.parseDouble(pay_amt),remark,0}, String.class);
				log.info(returnmsg);
				if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
					log.info("汇付宝微信支付交易成功,你支付的" + pay_amt + "元已经到账,此次交易的订单号为" + agent_bill_id );
					out.print("OK");
					return null;
				}else{
					out.print(returnmsg);
				}
				return null;
				
			} else {
				log.error("汇付宝微信支付失败");
				out.print("ok");
			}
			
		} catch (Exception e){
			log.error("汇付宝微信回调异常", e);
			out.print("error");
		} finally {
			if (out != null){
				out.close();
			}
		}
		return null;
	}
	
	
	/********
	 * 聚宝支付宝 回调
	 * @return
	 */
	public String payJubZfbReturn(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			log.info("==============聚宝支付宝回调======================");
			String message = (String)this.getRequest().getParameter("message");
			String signature = (String)this.getRequest().getParameter("signature");
			
			RSA.intialize();
			// 解密，校验签名，并处理业务逻辑处理
			JubaoPay jubaopay = new JubaoPay();
			boolean result = jubaopay.decrypt(message, signature);
			String payid = null;
			String amount = null;
			String remark = null;
			String orderNo = null;
			String state = null;
			String modifyTime = null;
			String realReceive = null;
			// 签名验证成功
			if(result){
				payid = jubaopay.getEncrypt("payid");
				amount = jubaopay.getEncrypt("amount");
				remark = jubaopay.getEncrypt("remark");
				orderNo = jubaopay.getEncrypt("orderNo");
				state = jubaopay.getEncrypt("state");
				realReceive = jubaopay.getEncrypt("realReceive");
				modifyTime = jubaopay.getEncrypt("modifyTime");
			} else {
				// 签名验证失败
				payid = "签名验证失败";
			}
			
			if (!"2".equals(state)){
				log.error("==============订单"+orderNo+"，回调接收的支付状态未成功");
				out.print("ERROR");
				return null;
			}
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("X--X聚宝支付宝接收订单号为" + orderNo );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayJubZfB", new Object[] { payid, Double.parseDouble(amount),remark,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("聚宝支付宝交易成功,你支付的" + amount + "元已经到账,此次交易的订单号为" + orderNo);
				out.print("SUCCESS");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("聚宝支付宝异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	

	
	/***
	 * 迅联宝微信  回调
 	 * @return 
	 */
	public String payXlbReturn(){       
		
		log.info("==============迅联宝支付回调======================");
		PrintWriter out = null;   
		try {
			out = this.getResponse().getWriter();
			String apiName = (String)this.getRequest().getParameter("apiName");//PAY_RESULT_NOTIFY
			String notifyTime = (String)this.getRequest().getParameter("notifyTime");//通知时间
			String tradeAmt = (String)this.getRequest().getParameter("tradeAmt"); //实际的支付金额   
			String merchNo = (String)this.getRequest().getParameter("merchNo");//商户号
			String merchParam = (String)this.getRequest().getParameter("merchParam");  //支付时上送的商户参数
			String orderNo = (String)this.getRequest().getParameter("orderNo");//商户订单号
			String tradeDate = (String)this.getRequest().getParameter("tradeDate");//商户交易日期
			String accNo = (String)this.getRequest().getParameter("accNo");//支付平台订单号
			String accDate = (String)this.getRequest().getParameter("accDate");//支付平台订单支付日期
			String orderStatus =(String)this.getRequest().getParameter("orderStatus");//处理结果0 未支付，1 成功，2失败
			String signMsg =(String)this.getRequest().getParameter("signMsg");//处理结果
			String notifyType =(String)this.getRequest().getParameter("notifyType");//1服务器，商户需回写应答。
			String key="5020b2c37008b436ae330f9fb567a139";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("\n\n\n");      
			log.info("");
			log.info("\n\n\n");    
			if(!merchNo.equals("210001520011131")){
				log.error("商户号不正确");     
				return "商户号不正确";
			}
			if(!orderStatus.equals("1")){
				log.error("充值失败");
				return "充值失败";
			}
			String srcMsg = String.format("apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s",
							apiName, notifyTime, tradeAmt, merchNo,
							merchParam, orderNo, tradeDate, accNo, accDate,
							orderStatus);
			// 验证签名
			System.out.println("srcMsg==="+srcMsg);
			if (!signMsg.equalsIgnoreCase(signByMD5(srcMsg, key))) {
				log.error("迅联宝验证前面失败");
				return "验证失败";
			}
			log.info("X--X迅联宝支付接收订单号为" + orderNo );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayXlb", new Object[] { orderNo, Double.parseDouble(tradeAmt),merchParam,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("迅联宝支付交易成功,你支付的" + tradeAmt + "元已经到账,此次交易的订单号为" + orderNo );
				out.print("SUCCESS");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			out.print("ERROR");
			//LOG.info("迅联宝异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	

	/***
	 * 迅联宝网银回调
	 * @return
	 */
	public String payXlbWyReturn(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String apiName = (String)this.getRequest().getParameter("apiName");//PAY_RESULT_NOTIFY
			String notifyTime = (String)this.getRequest().getParameter("notifyTime");//通知时间
			String tradeAmt = (String)this.getRequest().getParameter("tradeAmt"); //实际的支付金额
			String merchNo = (String)this.getRequest().getParameter("merchNo");//商户号
			String merchParam = (String)this.getRequest().getParameter("merchParam");  //支付时上送的商户参数
			String orderNo = (String)this.getRequest().getParameter("orderNo");//商户订单号
			String tradeDate = (String)this.getRequest().getParameter("tradeDate");//商户交易日期
			String accNo = (String)this.getRequest().getParameter("accNo");//支付平台订单号
			String accDate = (String)this.getRequest().getParameter("accDate");//支付平台订单支付日期
			String orderStatus =(String)this.getRequest().getParameter("orderStatus");//处理结果0 未支付，1 成功，2失败
			String signMsg =(String)this.getRequest().getParameter("signMsg");//处理结果
			String notifyType =(String)this.getRequest().getParameter("notifyType");//1服务器，商户需回写应答。
			String key="6bd669e55f2bc5ea1859da37e455a19d";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("\n\n\n");
			log.info("==============迅联宝网银支付回调======================");
			log.info("");
			log.info("\n\n\n");
			if(!merchNo.equals("210000520010301")){
				log.error("商户号不正确");
				return "商户号不正确";
			}
			if(!orderStatus.equals("1")){
				log.error("充值失败");
				return "充值失败";
			}
			String srcMsg = String.format("apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s",
							apiName, notifyTime, tradeAmt, merchNo,
							merchParam, orderNo, tradeDate, accNo, accDate,
							orderStatus);
			// 验证签名
			System.out.println("srcMsg==="+srcMsg);
			if (!signMsg.equalsIgnoreCase(signByMD5(srcMsg, key))) {
				log.error("迅联宝网银验证前面失败");
				return "验证失败";
			}
			log.info("X--X迅联宝网银支付接收订单号为" + orderNo );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayXlbWy", new Object[] { orderNo, Double.parseDouble(tradeAmt),merchParam,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("迅联宝网银支付交易成功,你支付的" + tradeAmt + "元已经到账,此次交易的订单号为" + orderNo );
				out.print("SUCCESS");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("迅联宝网银异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	

	/********
	 * 优付支付宝 and 微信 回调
	 * @return
	 */
	public String payYfZfReturn(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			String mer_no = (String)this.getRequest().getParameter("mer_no").trim();//商户号
			String order_amount = (String)this.getRequest().getParameter("order_amount").trim();//订单金额,小数点后3位
			String order_no = (String)this.getRequest().getParameter("order_no").trim(); //优付订单号
			String trade_no = (String)this.getRequest().getParameter("trade_no").trim();//商户订单号
			String trade_params = (String)this.getRequest().getParameter("trade_params").trim();  //返回参数
			String trade_status = (String)this.getRequest().getParameter("trade_status").trim();//订单状态,success才成功,其余状态不返回
			String trade_time = (String)this.getRequest().getParameter("trade_time").trim();//订单日期.时间戳
			String postsign = (String)this.getRequest().getParameter("sign").trim();//服务器返回的签名
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			
			log.info("===================优付支付宝 and 微信 回调======================");
			String key = null;
			String payName="";
			log.info("商户号:"+mer_no); 
			log.info("\n\n\n");
			if(!trade_status.equals("success")){      
				log.error("充值失败");
				return "充值失败";   
			}
			//优付支付宝
            if(mer_no.equals(Constants.YFZFB_MerNo)){
            	key = Constants.YFZFB_KEY;
            	payName="优付支付宝";
			}
            //优付微信
            else if(mer_no.equals(Constants.YFWX_MerNo)){
            	key = Constants.YFWX_KEY;
            	payName="优付微信";
			}
            else{
				log.error("商户号不正确");
				return "商户号不正确";
			}
			//拼接参数加密串,按字母升序, 结尾加KEY
			String str = "mer_no="+mer_no+"&order_amount="+order_amount+"&order_no="+order_no+"&trade_no="+trade_no+
							"&trade_params="+trade_params+"&trade_status="+trade_status+"&trade_time="+trade_time+"&KEY="+key;
			//Sign 签名
			String sign = Yom.getMD5(str);
			
			// 验证签名
			System.out.println("str==="+str);
			System.out.println("回调签名==="+sign);
			if (!postsign.equals(sign) && !trade_status.equals("success")) {     
				log.error(payName+"验证失败");
				return "验证失败";
			}
			log.info("X--X"+payName+"接收订单号为" + trade_no );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayYfZf", new Object[] { trade_no, Double.parseDouble(order_amount),payName,trade_params,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info(payName+"交易成功,你支付的" + order_amount + "元已经到账,此次交易的订单号为" + trade_no );
				out.print("success");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("优付支付异常：" + e.toString());
			e.printStackTrace();
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	
	
	

	
	public static String signByMD5(String sourceData, String key)
			throws Exception {
		String data = sourceData + key;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] sign = md5.digest(data.getBytes());

		return Bytes2HexString(sign).toUpperCase();
	}
	
	/**
	 * 将byte数组转成十六进制的字符串
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer(b.length);
		String hex = "";
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);

			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}
	
	/*****
	 * 新贝支付宝
	 * @return
	 */
	
	public String payXbZfbReturn(){
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			String version = (String)this.getRequest().getParameter("Version");
			String merchantCode = (String)this.getRequest().getParameter("MerchantCode");
			String orderId = (String)this.getRequest().getParameter("OrderId"); //我们的订单号
			String orderDate = (String)this.getRequest().getParameter("OrderDate");
			String tradeIp = (String)this.getRequest().getParameter("TradeIp");
			
			String serialNo = (String)this.getRequest().getParameter("SerialNo");//新贝平台交易流水号
			String amount = (String)this.getRequest().getParameter("Amount");
			
			String payCode = (String)this.getRequest().getParameter("PayCode");
			String state = (String)this.getRequest().getParameter("State");//处理结果
			String remark2 = (String)this.getRequest().getParameter("Remark2");//提交订单附带的账号信息
			String signValue = (String)this.getRequest().getParameter("SignValue");//：MD5
			String finishTime = (String)this.getRequest().getParameter("FinishTime");//：MD5
			String key="9eHznTmbwqYn4hBxd7PGcaEkLKFc8XBH";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			
			if(!"8888".equals(state)){
				log.info("新贝支付宝支付返回支付结果为处理不成功：State："+state);
				return null;
			}
			if(!merchantCode.equals("E03148")){
				log.info("商户号不正确"+merchantCode);
				return null;
			}
			String mySign = "Version=["+version+"]MerchantCode=["+merchantCode+"]OrderId=["+orderId+"]OrderDate=["+orderDate+"]TradeIp=["+tradeIp+"]SerialNo=["+serialNo+"]Amount=["+amount+"]PayCode=["+payCode+"]State=["+state+"]FinishTime=["+finishTime+"]TokenKey=["+key+"]";
			String mySign1 = "Version=["+version+"]MerchantCode=["+merchantCode+"]OrderId=["+orderId+"]OrderDate=["+orderDate+"]TradeIp=["+tradeIp+"]SerialNo=["+serialNo+"]Amount=["+df.format(Double.valueOf(amount))+"]PayCode=["+payCode+"]State=["+state+"]FinishTime=["+finishTime+"]TokenKey=["+key+"]";
			log.info("mySign---------->"+mySign+"\n");
			mySign = EncryptionUtil.encryptPassword(mySign).toUpperCase();
			mySign1 = EncryptionUtil.encryptPassword(mySign1).toUpperCase();
			if(!mySign.equals(signValue) && !mySign1.equals(signValue)){
				log.info("新贝支付宝支付回调签名验证失败：orderId ："+orderId);
				return null;
			}
			
			log.info("T--T新贝支付宝支付接收订单号为" + orderId );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayXbZfbzf", new Object[] { orderId, Double.parseDouble(amount),remark2,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("新贝支付宝支付交易成功,你支付的" + amount + "元已经到账,此次交易的订单号为" + orderId );
				out.print("OK");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("新贝支付宝异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/********
	 * 银宝支付宝 回调
	 * @return
	 */
	public String payYbZfbReturn(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			String partner = (String)this.getRequest().getParameter("partner").trim();//商户号
			String ordernumber = (String)this.getRequest().getParameter("ordernumber").trim();//商户订单号
			String orderstatus = (String)this.getRequest().getParameter("orderstatus").trim(); //订单结果 1:支付成功，非1为支付失败
			String paymoney = (String)this.getRequest().getParameter("paymoney").trim();//金额
			String sysnumber = (String)this.getRequest().getParameter("sysnumber").trim();  //银宝流水号
			String attach = (String)this.getRequest().getParameter("attach").trim();//备注信息
			String sign = (String)this.getRequest().getParameter("sign").trim();//服务器返回的签名
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("===================银宝支付宝回调======================");
			log.info("");
			log.info("\n\n\n");
			
			String key = null;
			String payName="";
			log.info("商户号:"+partner);
			//充值状态
		    if(!orderstatus.equals("1")){
				log.error("充值失败");
				return "充值失败";
			}
			//银宝支付宝
            if(partner.equals(Constants.YBZFB_MerNo)){
            	key = Constants.YBZFB_KEY;
            	payName="银宝支付宝";
			}
            else{
				log.error("商户号不正确");
				return "商户号不正确";
			}
			
			//校验MD5码是不是正确的
			String tempStr="partner="+partner+"&ordernumber="+ordernumber+"&orderstatus="+orderstatus+"&paymoney="+paymoney;
			System.out.println("验签串码："+tempStr);
			//Sign 签名
			String signMsg= signByMD5(tempStr, key);
			System.out.println("回调签名："+sign);
			
			if (!signMsg.equals(sign) && !orderstatus.equals("1")) {     
				log.error("银宝支付宝验证失败");
				return "验证失败";
			}
			log.info("X--X银宝支付宝接收订单号为" + ordernumber );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayYbZfb", new Object[] { ordernumber, Double.parseDouble(paymoney),payName,attach,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info("银宝支付宝交易成功,你支付的" + paymoney + "元已经到账,此次交易的订单号为" + ordernumber );
				out.print("success");
				return null;
			}else{
				out.print(returnmsg);
			}
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("银宝支付宝异常：" + e.toString());
			e.printStackTrace();
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	/********
	 * 千网支付宝 and 微信 回调
	 * @return
	 */
	public String payQwZfReturn(){
		PrintWriter out = null;
		try {
			this.getResponse().setCharacterEncoding("UTF-8");
			out = this.getResponse().getWriter();
			String orderid = (String)this.getRequest().getParameter("orderid").trim();//订单号 
			String opstate = (String)this.getRequest().getParameter("opstate").trim();//支付结果
			String ovalue = (String)this.getRequest().getParameter("ovalue").trim(); //支付金额
			String sign = (String)this.getRequest().getParameter("sign").trim();//签名
			String cid = (String)this.getRequest().getParameter("cid").trim();  //商户号
			String ekaorderid = (String)this.getRequest().getParameter("ekaorderid").trim();  //亿卡订单号
			String ekatime = (String)this.getRequest().getParameter("ekatime").trim();//亿卡订单时间
			String attach = (String)this.getRequest().getParameter("attach").trim();//备注信息，上行提交参数原样返回
			String msg = (String)this.getRequest().getParameter("msg").trim();//支付结果中文说明
			String  key= "";
			String 	payName = "";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			log.info("===================千网 支付宝 and 微信 回调======================");
			log.info("订单号:"+orderid+"---商户号---"+cid);
			log.info("\n\n\n");
			if(orderid ==null || orderid.equals("")){        
				log.error("充值失败");
				return "充值失败";
			}
			//千网支付宝
            if(cid.equals(Constants.QWZFB_MerNo)){
            	key = Constants.QWZFB_KEY;
            	payName="千网支付宝";
			}
        	//千网微信
            else if(cid.equals(Constants.QWWX_MerNo)){
            	key = Constants.QWWX_KEY;
            	payName="千网微信";
			}
            else{
				log.error("商户号不正确");
				return "商户号不正确";
			}
			//拼接参数加密串,按字母升序
			String tempStr = MD5.EkaPayCardBackMd5Sign(orderid,opstate,ovalue,key);
			// 验证签名
			System.out.println("tempStr"+tempStr);
			if (!sign.equals(tempStr)) {     
				log.error(payName+"验证失败");
				out.println("opstate=-2");
				return "验证失败";
			}
			log.info("X--X"+payName+"接收订单号为" + orderid );
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayQwZf", new Object[] { orderid, Double.parseDouble(ovalue),payName,attach,0}, String.class);
			log.info(returnmsg);
			if (returnmsg == null || returnmsg.equals("此笔交易已经支付成功")) {
				log.info(payName+"交易成功,你支付的" + ovalue + "元已经到账,此次交易的订单号为" + orderid );
				out.println("opstate=0");
				return null;
			}else{
				out.println("opstate=-2");
			}
			return null;
		} catch (Exception e) {
			out.println("opstate=-2");
			//LOG.info("千网支付异常：" + e.toString());
			e.printStackTrace();
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	
	
   private String payment_result;
   private String deposit_result;
	

	public String getPayment_result() {
		return payment_result;
	}
	
	public void setPayment_result(String payment_result) {
		this.payment_result = payment_result;
	}
	
	
	public String getDeposit_result() {
	return deposit_result;
	}
	
	public void setDeposit_result(String deposit_result) {
		this.deposit_result = deposit_result;
	}
   
	
	
	/***
	 * 	秒付宝提款 回调
	 * @return
	 */
	public String  returnNotify(){
		PrintWriter out = null;
		
		log.info("-------------------------------------------秒付宝提款回调------------------------------------------------");   
		log.info("payment_result:"+payment_result);
		log.info("\n\n\n");
		try {
			out = this.getResponse().getWriter();
			String url = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("middleservice.url"));
			
			HttpClient httpClient = new HttpClient();
			
			HttpClientParams params = new HttpClientParams();   
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);
			
			httpClient.setParams(params);
			System.out.println("url:"+url);
			PostMethod method = new PostMethod(url + "/withdraw/notify");   
			method.setRequestHeader("Connection", "close");
			method.setParameter("data", payment_result);
			method.setParameter("product",Constants.PRODUCT_NAME);

			
			int statusCode = httpClient.executeMethod(method);
			System.out.println("statusCode:"+statusCode);
	    	if (statusCode != HttpStatus.SC_OK) {
	    		out.print("[提示]系统繁忙，请稍后再试！");
	    		return "[提示]系统繁忙，请稍后再试！";
			} else {
				byte[] responseBody = method.getResponseBody();
		    	String responseString = new String(responseBody);
		    	System.out.println("responseString:"+responseString);   
		    	out.print(responseString);
		    	return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("[提示]系统繁忙，请稍后再试！");
    		return "[提示]系统繁忙，请稍后再试！";
		}finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	

	
	private static ObjectMapper mapper = new ObjectMapper();
	
	/*******
	 * 通联秒存回调
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void tlDepositCallback() throws Exception {

		PrintWriter out = null;

		log.info("-------------------------------------------通联存款记录回调-------------------------------------------");
		log.info("deposit_result：" + deposit_result);

		Map<String, String> dataMap = mapper.readValue(deposit_result, Map.class);
		dataMap.put("product", Constants.PRODUCT_NAME);
		
		String data = mapper.writeValueAsString(dataMap);
		
		String requestJson = app.util.AESUtil.getInstance().encrypt(data);
		
		try {

			out = this.getResponse().getWriter();

			String url = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("middleservice.url"));

			HttpClient httpClient = new HttpClient();

			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 50000);

			httpClient.setParams(params);

			PostMethod method = new PostMethod(url + "/pay/deposit/list");
			method.setRequestHeader("Connection", "close");
			method.setParameter("requestData", requestJson);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				out.print("[提示]系统繁忙，请稍后再试！");
			} else {

				byte[] responseBody = method.getResponseBody();
				String responseString = new String(responseBody);

				Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
				responseString = resultMap.get("responseData");
				responseString = app.util.AESUtil.getInstance().decrypt(responseString);

				resultMap = mapper.readValue(responseString, Map.class);

				log.info("通联访问中间件返回值："+resultMap.toString());
				
				if ("10000".equals(resultMap.get("code"))) {

					out.print("ok");
				} else {

					out.print(resultMap.get("message"));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			out.print("[提示]系统繁忙，请稍后再试！");
		} finally {
			
			if (out != null) {
				
				out.flush();
				out.close();
			}
		}
	}
	
	

	

	
	
	
	
}
