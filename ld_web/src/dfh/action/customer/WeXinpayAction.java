package dfh.action.customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;

public class WeXinpayAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;
	// 公众账号ID
	private String appid;
	// 商户号
	private String mch_id;
	// 设备号 网页端'web'
	private String device_info;
	// 随机字符串
	private String nonce_str;
	// 数字签名
	private String sign;
	// 商品描述
	private String body;
	// 商品详细描述---可以不填
	private String detail;
	// 回调的时候原样返回字段
	private String attach;
	// 商品订单号
	private String out_trade_no;
	// 货币类型---可以不填
	private String fee_type;
	// 这里单位为元 不能有小数点 金额
	private String order_amount;
	// 来源ip
	private String spbill_create_ip;
	// 开始时间
	private String time_start;
	// 结束时间
	private String time_expire;

	private String goods_tag;
	// 回调通知
	private String notify_url;
	// 支付方式
	private String trade_type;
	// 商品id 可以自定义
	private String product_id;
	// 支付方式
	private String payType;

	private String error_info;

	public void wxpayRedirect() {
		try {
			// 检测用户是否登录
			Users user = (Users) getHttpSession().getAttribute(
					Constants.SESSION_CUSTOMERID);
			if (user == null) {
				setError_info("[提示]登录已过期，请从首页重新登录");
				GsonUtil.GsonObject(getError_info());
				return ;
			}

			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			request.getSession().removeAttribute("weixinpayUrl");// 把上一个支付url移除
			Date oldDate=(Date)request.getSession().getAttribute("weixinpaytime");
		//	request.getSession().setAttribute("weixinpaytime", DateUtil.getMontSecond(date, second));
			// / 判断在线支付是否存在
			Const constPay = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
					"selectDeposit", new Object[] { "微信支付直连" }, Const.class);
			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				GsonUtil.GsonObject(getError_info());
				return ;
			}
			// 判断在线支付是否在维护
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				GsonUtil.GsonObject(getError_info());
				return ;
			}
			// 判断订单金额是否为空
			if (!isNotNullAndEmpty(order_amount)) {
				setError_info("[提示]存款额度不能为空！");
				GsonUtil.GsonObject(getError_info());
				return ;
			}
			try {
				/*if (Integer.parseInt(order_amount) < 1) {
					setError_info("[提示]1元以上或者1元才能存款！");
					GsonUtil.GsonObject(getError_info());
					return ;
				}*/
				if (Integer.parseInt(order_amount) > 1000) {
					setError_info("[提示]存款金额不能超过1000！");
					GsonUtil.GsonObject(getError_info());
					return ;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是整数！");
				GsonUtil.GsonObject(getError_info());
				return ;
			}
			// 获取商家订单号
			String orderNo = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
					"getOrderWeiXinPayNo", new Object[] { user.getLoginname(),
							order_amount, constPay.getId() }, String.class);
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				GsonUtil.GsonObject(getError_info());
				return ;
			}
			/** 必须* */
			appid = "wx5305485f86eb41ab";
			/** 必须* */
			mch_id = "1301486901";
			/** 必须* */
			device_info = "WEB";
			/** 必须* */
			nonce_str = orderNo;
			/** 必须* */
			sign = sign;
			/** 必须* */
			body = "123123123";
			/** 必须* */
			attach = orderNo;
			/** 必须* */
			out_trade_no = orderNo;
			/** 必须* */
			order_amount = order_amount;
			/** 必须* */
			spbill_create_ip = "127.0.0.1";
			/** 可选* */
			time_start = "";
			/** 可选* */
			time_expire = "";
			/** 必须* */
			notify_url = "http://pay.168.tl/asp/weixinpayReturn.aspx";
			/** 必须* */
			trade_type = "NATIVE";

			/** 必须* */
			product_id = System.currentTimeMillis() + "";
			URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());
			String xmlInfo = getXmlInfo();
			out.write(new String(xmlInfo.getBytes("UTF-8")));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			StringBuffer sbf = new StringBuffer("");
			for (line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
				sbf.append(line);
			}
			Iterator Elements = parse(sbf.toString());
			String resultcode = "";
			String weixinpayUrl = "";
			while (Elements.hasNext()) {
				Element elem = (Element) Elements.next();
				System.out.println("节点" + elem.getName() + "\ttext="
						+ elem.getText());
				if (elem.getName().equals("return_msg")) {
					resultcode = elem.getText();
				} else if (elem.getName().equals("code_url")) {
					weixinpayUrl = elem.getText();
				}
			}
			if (!"OK".equalsIgnoreCase(resultcode)) {
				setError_info("[提示]微信支付失败，请重试!");
			}
			GsonUtil.GsonObject("http://www.thonky.com/qr-encoder/encode.php?maxw=270&ec=0&s="+weixinpayUrl);
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("[提示]网络繁忙,请稍后再试！");
			GsonUtil.GsonObject(getError_info());
		}
	}

	/**
	 * 判断数据是否为空
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNotNullAndEmpty(String str) {
		boolean b = false;
		if (null != str && str.trim().length() > 0) {
			b = true;
		}
		return b;
	}

	/**
	 * 组合xml 接口所需格式
	 * 
	 * @return
	 */
	private String getXmlInfo() {
		StringBuffer stringA = new StringBuffer("appid=" + appid);
		stringA.append("&attach=" + attach);
		stringA.append("&body=" + body);
		stringA.append("&device_info=" + device_info);
		stringA.append("&mch_id=" + mch_id);
		stringA.append("&nonce_str=" + nonce_str);
		stringA.append("&notify_url=" + notify_url);
		stringA.append("&out_trade_no=" + out_trade_no);
		stringA.append("&product_id=" + product_id);
		stringA.append("&spbill_create_ip=" + spbill_create_ip);
		stringA.append("&total_fee=" + Integer.parseInt(order_amount) * 100);
		stringA.append("&trade_type=" + trade_type);
		String stringSignTemp = stringA
				+ "&key=skJsaUlsN1qj2sQlzinlAAZHWEPd1s1p";
		System.out.println("stringSignTemp===" + stringSignTemp);
		String sign = EncryptionUtil.encryptPassword(stringSignTemp);
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append(" <appid>" + appid + "</appid>");// 公众账号ID
		sb.append(" <mch_id>" + mch_id + "</mch_id>"); // 商户号
		sb.append(" <device_info>" + device_info + "</device_info>"); // 设备号
																		// 网页端'web'
		sb.append(" <nonce_str>" + nonce_str + "</nonce_str>"); // 随机字符串
		sb.append(" <sign>" + sign + "</sign>"); // 数字签名
		sb.append(" <body>" + body + "</body>");
		sb.append(" <attach>" + attach + "</attach>"); // 回调的时候原样返回
		sb.append(" <out_trade_no>" + out_trade_no + "</out_trade_no>");
		sb.append(" <total_fee>" + Integer.parseInt(order_amount) * 100
				+ "</total_fee>"); // 这里单位为分 不能有小数点
		sb.append(" <spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>");
		sb.append(" <notify_url>" + notify_url + "</notify_url>"); // 返回通知的url
		sb.append(" <trade_type>" + trade_type + "</trade_type>"); // 扫码支付
		sb.append(" <product_id>" + product_id + "</product_id>"); // 商品id 可以自定义
		sb.append("</xml>");
		System.out.println("sbf===" + sb.toString());
		return sb.toString();
	}

	private String getRandom(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(62);// [0,62)
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public static Iterator parse(String protocolXML) {
		try {
			Document doc = (Document) DocumentHelper.parseText(protocolXML);
			Element books = doc.getRootElement();
			System.out.println("根节点" + books.getName());
			// Iterator users_subElements =
			// books.elementIterator("UID");//指定获取那个元素
			Iterator Elements = books.elementIterator();
			return Elements;
			/*
			 * while(Elements.hasNext()){ Element user =
			 * (Element)Elements.next();
			 * System.out.println("节点"+user.getName()+"\ttext="+user.getText());
			 * List subElements = user.elements(); // List user_subElements =
			 * user.elements("username");指定获取那个元素 //
			 * System.out.println("size=="+subElements.size()); // for( int
			 * i=0;i<subElements.size();i++){ // Element ele =
			 * (Element)subElements.get(i); //
			 * System.out.print(ele.getName()+" : "+ele.getText()+" "); // }
			 * System.out.println(); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
