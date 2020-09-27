<%@page import="com.nnti.pay.api.rb.mb.utils.QuickPayApplyResponseEntity"%>
<%@page import="com.nnti.pay.api.rb.mb.utils.SignUtil"%>
<%@page import="com.nnti.pay.api.rb.mb.utils.Merchant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.io.PrintWriter"%>
<%@ page import="java.util.*" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>摩宝支付 - 快捷支付</title>
    <link rel="stylesheet" href="../resources/assets/css/style.css" />
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
</head>
<body>
<div class="container">
    <div class="header">
        <h3>摩宝支付 - 快捷支付：</h3>
    </div>
    <div class="main">
        <div class="response-info">
            <p>
                <%
                    try {
                    	 Map<String, String> pays = (Map)request.getAttribute("pays");
                    	 String service = pays.get("service").toString();
                    	 String version = pays.get("version").toString();
                    	 String merId = pays.get("merId").toString();
                    	 String tradeNo = pays.get("tradeNo").toString();
                    	 String tradeDate = pays.get("tradeDate").toString();
                    	 String amount = pays.get("amount").toString();
                    	 String extra = pays.get("extra").toString();
                    	 String notifyUrl = pays.get("notifyUrl").toString();
                    	 String clientIp = pays.get("clientIp").toString();
                    	 String sign = pays.get("sign").toString();
                    	 String summary = pays.get("summary").toString();
                    	 String bankcard = pays.get("bankcard").toString();
                    	 String bankname = pays.get("bankname").toString();
                    	 String phoneNumber = pays.get("phoneNumber").toString();
                    	 String url = pays.get("url").toString();


                    	 System.out.println("service: " + service);
                    	 System.out.println("version: " + version);
                    	 System.out.println("merId: " + merId);
                     	
                    	
                        // 组织请求数据
                        Map<String, String> paramsMap = new HashMap<String, String>();
                        request.setCharacterEncoding("UTF-8");
                        paramsMap.put("service", service);
                        paramsMap.put("version", version);
                        paramsMap.put("merId", merId);
                        paramsMap.put("tradeNo", tradeNo);
                        paramsMap.put("tradeDate",tradeDate);
                        paramsMap.put("amount", amount);
                        paramsMap.put("notifyUrl", notifyUrl);
                        paramsMap.put("extra", extra);
                        paramsMap.put("summary", summary);
                        paramsMap.put("expireTime", "");
                        paramsMap.put("clientIp", clientIp);
                        paramsMap.put("cardType", "1");
                        paramsMap.put("cardNo", bankcard);
                        paramsMap.put("cardName", bankname);
                        paramsMap.put("idCardNo", "421125198910129876");
                        paramsMap.put("mobile", phoneNumber);
                        paramsMap.put("cvn2", "");
                        paramsMap.put("validDate", "");
                        
                        System.out.println("map: " + paramsMap.toString());

                        String paramsStr = Merchant.generateQuickPayApplyRequest(paramsMap);
                        String signMsg = SignUtil.signData(paramsStr);
                        System.out.println("签名: " + signMsg);
                        paramsStr += "&sign=" + signMsg;

                        String payGateUrl = url;
                        

                        // 发送请求并接收返回
                        System.out.println(paramsStr);
                        String responseMsg = Merchant.transact(paramsStr, payGateUrl);
                        System.out.println("===" + responseMsg);

                        //解析返回数据
                        QuickPayApplyResponseEntity entity = new QuickPayApplyResponseEntity();
                        entity.parse(responseMsg);

                        if (entity.getCode().equals("00")) {
                            StringBuffer sbHtml = new StringBuffer();
                            sbHtml.append("<form method=\"post\" action=\"/pay/quickPay_confirm\">");
                            sbHtml.append("<input type=\"hidden\"  name=\"opeNo\" value=\""+ entity.getOpeNo() +"\" />");
                            sbHtml.append("<input type=\"hidden\"  name=\"opeDate\" value=\""+ entity.getOpeDate() +"\" />");
                            sbHtml.append("<input type=\"hidden\"  name=\"sessionID\" value=\""+ entity.getSessionID() +"\" />");
                            sbHtml.append("<ul>");
                            sbHtml.append("<li>");
                            sbHtml.append("<label>短信验证码</label>");
                            sbHtml.append("<input type=\"text\" name=\"dymPwd\" maxlength=\"6\" />");
                            sbHtml.append("</li>");
                            sbHtml.append("<li style=\"margin-top: 50px\">");
                            sbHtml.append("<label></label>");
                            sbHtml.append("<button type=\"submit\">确认支付</button>");
                            sbHtml.append("</li>");
                            sbHtml.append("</ul>");
                            sbHtml.append("</form>");

                            out.println(sbHtml);
                        } else {
                            out.println(entity.getDesc());
                        }

                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                %>
            </p>
        </div>
    </div>
</div>
</body>
</html>
