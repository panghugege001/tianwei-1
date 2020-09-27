<%@page import="com.nnti.pay.api.rb.mb.utils.QuickPayConfirmResponseEntity"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                        // 组织请求数据
                        Map<String, String> paramsMap = new HashMap<String, String>();
                        request.setCharacterEncoding("UTF-8");
                        paramsMap.put("service", "TRADE.QUICKPAY.CONFIRM");
                        paramsMap.put("version", "1.0.0.0");
                        paramsMap.put("merId", "2017083144010127");
                        paramsMap.put("opeNo", request.getParameter("opeNo"));
                        paramsMap.put("opeDate", request.getParameter("opeDate"));
                        paramsMap.put("sessionID", request.getParameter("sessionID"));
                        paramsMap.put("dymPwd", request.getParameter("dymPwd"));

                        String paramsStr = Merchant.generateQuickPayConfirmRequest(paramsMap);
                        String signMsg = SignUtil.signData(paramsStr);
                        System.out.println("签名: " + signMsg);
                        paramsStr += "&sign=" + signMsg;

                        String payGateUrl = "http://gate.starspay.com/cooperate/gateway.cgi";

                        // 发送请求并接收返回
                        System.out.println(paramsStr);
                        String responseMsg = Merchant.transact(paramsStr, payGateUrl);
                        System.out.println("===" + responseMsg);

                        QuickPayConfirmResponseEntity entity = new QuickPayConfirmResponseEntity();
                        entity.parse(responseMsg);

                        out.println(entity.getDesc());

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
