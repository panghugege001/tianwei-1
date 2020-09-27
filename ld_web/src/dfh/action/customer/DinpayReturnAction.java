package dfh.action.customer;

import java.io.PrintWriter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.enums.CommonZfEnum;
import dfh.utils.AxisUtil;
import dfh.utils.RSAWithSoftware;
import dfh.utils.ZfSendRequestUtil;
import dfh.utils.ZfWxSendRequestUtil;

public class DinpayReturnAction extends SubActionSupport {

	private Logger log = Logger.getLogger(DinpayReturnAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String payReturn() {
		try {
			/*******************************************************************
			 * 功能：智付页面跳转同步通知页面 版本：3.0 日期：2013-08-01 说明：
			 * 以下代码仅为了方便商户安装接口而提供的样例具体说明以文档为准，商户可以根据自己网站的需要，按照技术文档编写。
			 */
			// 获取智付GET过来反馈信息
			// 商号号
			String merchant_code = this.getRequest().getParameter("merchant_code");

			// 通知类型
			String notify_type = this.getRequest().getParameter("notify_type");

			// 通知校验ID
			String notify_id = this.getRequest().getParameter("notify_id");

			// 接口版本
			String interface_version = this.getRequest().getParameter("interface_version");

			// 签名方式
			String sign_type = this.getRequest().getParameter("sign_type");

			// 签名
			String dinpaySign = this.getRequest().getParameter("sign");

			// 商家订单号
			String order_no = this.getRequest().getParameter("order_no");

			// 商家订单时间
			String order_time = this.getRequest().getParameter("order_time");

			// 商家订单金额
			String order_amount = this.getRequest().getParameter("order_amount");

			// 回传参数
			String extra_return_param = this.getRequest().getParameter("extra_return_param");

			// 智付交易定单号
			String trade_no = this.getRequest().getParameter("trade_no");

			// 智付交易时间
			String trade_time = this.getRequest().getParameter("trade_time");

			// 交易状态 SUCCESS 成功 FAILED 失败
			String trade_status = this.getRequest().getParameter("trade_status");

			// 银行交易流水号
			String bank_seq_no = this.getRequest().getParameter("bank_seq_no");

			/**
			 * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
			 * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
			 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
			 */
			PrintWriter out = null;
			log.info("************提交IP是:"+getIp()+"**********"+trade_no);
			String drOne="220.231.196.30"; 
			String drTwo="220.231.196.18";
			String drThree="116.204.15.196";
			String drFour="218.17.149.162";
			String drFive="116.204.15.196";                           
			String drSix="58.251.77.162";
			if (!drOne.contains(getIp()) && !drTwo.contains(getIp()) && !drThree.contains(getIp())&& !drFour.contains(getIp())&& !drFive.contains(getIp())&& !drSix.contains(getIp())) {
				try {
					out = this.getResponse().getWriter();
					out.print("提交失败！");
					out.flush();
				} catch (Exception e) {
					log.info(e.toString());
				} finally {
					if (out != null) {
						out.close();
					}
				}
				log.info("该IP拒绝访问！"+getIp());
				return "";
			}
			// 组织订单信息
			StringBuilder signStr = new StringBuilder();
			if (bank_seq_no!=null && !bank_seq_no.equals("")) {
				signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
			}
			if (extra_return_param!=null && !extra_return_param.equals("")) {
				signStr.append("extra_return_param=").append(extra_return_param).append("&");
			}
			signStr.append("interface_version=V3.0").append("&");
			signStr.append("merchant_code=").append(merchant_code).append("&");
			if (notify_id!=null && !notify_id.equals("")) {
				// signStr.append("notify_id=").append(notify_id).append("&notify_type=page_notify&");
				signStr.append("notify_id=").append(notify_id).append("&notify_type=" + notify_type + "&");
			}

			signStr.append("order_amount=").append(order_amount).append("&");
			signStr.append("order_no=").append(order_no).append("&");
			signStr.append("order_time=").append(order_time).append("&");
			signStr.append("trade_no=").append(trade_no).append("&");
			signStr.append("trade_status=").append(trade_status).append("&");
			if (trade_time != "") {
				signStr.append("trade_time=").append(trade_time).append("&");
			}
			String key = "";
			if(merchant_code.equals("2030000006")){
			      key = "sdkbg6ckajlfv8f03g8_";
			}else if(merchant_code.equals("2030028802")){
			      key = "uqajaj9298hsau2a832_";
			}else if(merchant_code.equals("2030020118")){
			      key = "e68a3o92asdfs343464_";
			}else if(merchant_code.equals("2030020119")){
			      key = "e68a45p32i49sdfsfs3_";
			}else if(merchant_code.equals("2000295555")){
				key = "98adKLs2DSghaas8f_5dvaawp";
			}else{
				CommonZfEnum cmzf = CommonZfEnum.getCommonZfByMerchantCode(merchant_code);
				if(cmzf != null)
					key = cmzf.getMerchantKey();
			}
			Boolean flag = null ; 
			if(sign_type.equals("RSA-S")){
				System.out.println("=============智付微信 RSA-S方式=================\n\n");
				String signInfo = signStr.toString() ;
				if(signInfo.endsWith("&")){
					signInfo = signInfo.substring(0, signInfo.length()-1) ;
				}
				System.out.println("=====signInfo:====="+signInfo);
				flag = RSAWithSoftware.validateSignByPublicKey(signInfo, ZfWxSendRequestUtil.ZF_PUBLIC_KEY, dinpaySign);//智付后台公钥
			}else{
				signStr.append("key=").append(key);
				String sign = DigestUtils.md5Hex(signStr.toString()); // 注意与支付签名不同
				flag = dinpaySign.equals(sign) ;
			}
			// 此处对String进行加密
			// 比较智付返回的签名串与商家这边组装的签名串是否一致
			if (flag) {
				// 验签成功
				if (trade_status.equals("SUCCESS")) {
					log.info("数据到达成功,此次订单号为" + trade_no);
					if (order_amount == null) {
						order_amount = "0.00";
					}
					String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderZf", new Object[] { trade_no, Double.parseDouble(order_amount), extra_return_param, bank_seq_no, trade_time,merchant_code }, String.class);
					if (returnmsg == null) {
						try {
							out = this.getResponse().getWriter();
							out.print("SUCCESS");
							out.flush();
							log.info("交易成功,你支付的" + order_amount + "元已经到账,此次交易的订单号为" + trade_no);
						} catch (Exception e) {
							log.info(e.toString());
						} finally {
							if (out != null) {
								out.close();
							}
						}
					} else {
						out = this.getResponse().getWriter();
						out.print("SUCCESS");
						out.flush();
						log.info("插入数据交易失败:" + returnmsg + ",订单号为" + trade_no);
					}
				} else {
					log.info("交易失败："+ trade_no);
				}
			} else {
				log.info("验证失败,请仔细核对交易信息"+ trade_no);
			}
			if (out != null)
				out.close();
		} catch (Exception e) {
			log.info(e.toString());
		}
		return null;
	}
	
	
	
	
	
	
	/**************************************
	 * 通用智付 回调
	 * @return
	 */
	
	public String payZfReturn() {
		try {
			
			System.out.println("==========================通用支付回调开始===========================");
			/*******************************************************************
			 * 功能：智付页面跳转同步通知页面 版本：3.0 日期：2013-08-01 说明：
			 * 以下代码仅为了方便商户安装接口而提供的样例具体说明以文档为准，商户可以根据自己网站的需要，按照技术文档编写。
			 */
			// 获取智付GET过来反馈信息
			// 商号号
			String merchant_code = this.getRequest().getParameter("merchant_code");

			// 通知类型
			String notify_type = this.getRequest().getParameter("notify_type");

			// 通知校验ID
			String notify_id = this.getRequest().getParameter("notify_id");

			// 接口版本
			String interface_version = this.getRequest().getParameter("interface_version");

			// 签名方式
			String sign_type = this.getRequest().getParameter("sign_type");

			// 签名
			String dinpaySign = this.getRequest().getParameter("sign");

			// 商家订单号
			String order_no = this.getRequest().getParameter("order_no");

			// 商家订单时间
			String order_time = this.getRequest().getParameter("order_time");

			// 商家订单金额
			String order_amount = this.getRequest().getParameter("order_amount");

			// 回传参数
			String extra_return_param = this.getRequest().getParameter("extra_return_param");

			// 智付交易定单号
			String trade_no = this.getRequest().getParameter("trade_no");

			// 智付交易时间
			String trade_time = this.getRequest().getParameter("trade_time");

			// 交易状态 SUCCESS 成功 FAILED 失败
			String trade_status = this.getRequest().getParameter("trade_status");

			// 银行交易流水号
			String bank_seq_no = this.getRequest().getParameter("bank_seq_no");

			/**
			 * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
			 * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
			 * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
			 */
			PrintWriter out = null;
			log.info("************提交IP是:"+getIp());
			String drOne="220.231.196.30"; 
			String drTwo="220.231.196.18";
			String drThree="116.204.15.196";
			String drFour="218.17.149.162";
			String drFive="116.204.15.196";                           
			String drSix="58.251.77.162";
			if (!drOne.contains(getIp()) && !drTwo.contains(getIp()) && !drThree.contains(getIp())&& !drFour.contains(getIp())&& !drFive.contains(getIp())&& !drSix.contains(getIp())) {
				try { 
					out = this.getResponse().getWriter();
					out.print("提交失败！");
					out.flush();
				} catch (Exception e) {
					log.info(e.toString());
				} finally {
					if (out != null) {
						out.close();
					}
				}
				log.info("该IP拒绝访问！"+getIp());
				return "";
			}

			// 组织订单信息
			StringBuilder signStr = new StringBuilder();
			if (bank_seq_no!=null && !bank_seq_no.equals("")) {
				signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
			}
			if (extra_return_param!=null && !extra_return_param.equals("")) {
				signStr.append("extra_return_param=").append(extra_return_param).append("&");
			}
			signStr.append("interface_version=V3.0").append("&");
			signStr.append("merchant_code=").append(merchant_code).append("&");
			if (notify_id!=null && !notify_id.equals("")) {
				// signStr.append("notify_id=").append(notify_id).append("&notify_type=page_notify&");
				signStr.append("notify_id=").append(notify_id).append("&notify_type=" + notify_type + "&");
			}

			signStr.append("order_amount=").append(order_amount).append("&");
			signStr.append("order_no=").append(order_no).append("&");
			signStr.append("order_time=").append(order_time).append("&");
			signStr.append("trade_no=").append(trade_no).append("&");
			signStr.append("trade_status=").append(trade_status).append("&");
			if (trade_time != "") {
				signStr.append("trade_time=").append(trade_time);
			}
			Boolean flag = null ; 
			if(sign_type.equals("RSA-S")){
				String signInfo = signStr.toString() ;
				if(signInfo.endsWith("&")){
					signInfo = signInfo.substring(0, signInfo.length()-1) ;
				}
				System.out.println("=====通用支付signInfo:====="+signInfo);
				//通用智付1
				if(merchant_code.equals("2000295699")){
					flag = RSAWithSoftware.validateSignByPublicKey(signInfo, ZfSendRequestUtil.ZF_PUBLIC_KEY, dinpaySign);
				}
				//通用智付2
				if(merchant_code.equals("2000295640")){
					flag = RSAWithSoftware.validateSignByPublicKey(signInfo, ZfSendRequestUtil.ZF_PUBLIC_KEY2, dinpaySign);
				}
			}
			System.out.println("通用支付flag:================"+flag+"===================");
			System.out.println("通用支付trade_status:================"+trade_status+"===================");
			
			// 将组装好的信息MD5签名
			// 此处对String进行加密
			// 比较智付返回的签名串与商家这边组装的签名串是否一致
			if (flag) {
				// 验签成功
				if (trade_status.equals("SUCCESS")) {
					if (order_amount == null) {
						order_amount = "0.00";
					}
					String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderZf", new Object[] { trade_no, Double.parseDouble(order_amount), extra_return_param, bank_seq_no, trade_time,merchant_code }, String.class);
					System.out.println(returnmsg+"returnmsg------------------------");
					if (returnmsg == null) {
						try {
							out = this.getResponse().getWriter();
							out.print("SUCCESS");
							out.flush();
							log.info("交易成功,你支付的" + order_amount + "元已经到账,此次交易的订单号为" + trade_no);
						} catch (Exception e) {
							log.info(e.toString());
						} finally {
							if (out != null) {
								out.close();
							}
						}
					} else {
						out = this.getResponse().getWriter();
						out.print("SUCCESS");
						out.flush();
						log.info("交易失败:" + returnmsg + ",订单号为" + trade_no);
					}
				} else {
					log.info("交易失败");
				}
			} else {
				log.info("验证失败,请仔细核对交易信息");
			}
		} catch (Exception e) {
			log.info(e.toString());
		}
		return null;
	}
	
}
