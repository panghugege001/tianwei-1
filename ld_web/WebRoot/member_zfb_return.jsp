<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@page import="dfh.utils.AlipayUtil"%>
<%@page import="dfh.utils.AxisUtil"%>
<%
	/* *
	功能：支付宝页面跳转同步通知页面
	版本：3.2
	日期：2011-03-17
	说明：
	以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
	//***********页面功能说明***********
	该页面可在本机电脑测试
	可放入HTML等美化页面的代码、商户业务逻辑程序代码
	TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
	TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
	//********************************
	* */
	//获取支付宝GET过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		params.put(name, valueStr);
	}
	
	//获取支付宝的通知返回参数//
	//商户订单号

	String out_trade_no = request.getParameter("out_trade_no");

	//支付宝交易号

	String trade_no = request.getParameter("trade_no");

	//交易状态
	String trade_status = request.getParameter("trade_status");

	//交易金额
	String total_fee = request.getParameter("total_fee");
	
	//公用回传参数
	String extra_common_param = request.getParameter("extra_common_param");
	
	//卖家账号
	String seller_email = request.getParameter("seller_email");
	
	//通知时间
	String notify_time = request.getParameter("notify_time");
	
	
	//获取支付宝的通知返回参数//
	
	//计算得出通知验证结果
	boolean verify_result = AlipayUtil.verify(params);
	
	if(verify_result){//验证成功
		if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
			//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
			if (total_fee == null) {
				total_fee = "0.00";
			}
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderZfb", new Object[] { out_trade_no, Double.parseDouble(total_fee), extra_common_param, trade_no, notify_time }, String.class);
			if (returnmsg == null) {
				out.println("交易成功,你支付的" + Double.parseDouble(total_fee) + "元已经到账,此次交易的订单号为" + trade_no);
			} else {
				out.println("交易失败:" + returnmsg);
			}
		}else{
			out.println("交易失败" + trade_status);
		}
	}else{
		//验签失败 业务结束
		out.println("验证失败,请仔细核对交易信息");
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	</head>
	<body>
		<!-- 此处可添加页面展示  提示相关信息给消费者  -->
	</body>
</html>