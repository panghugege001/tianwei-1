<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.NameValuePair" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.entity.UrlEncodedFormEntity" %>
<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.impl.client.DefaultHttpClient" %>
<%@ page import="org.apache.http.message.BasicNameValuePair" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html>
<head>

</head>
<body>

<%

    Map<String, String[]> pays = request.getParameterMap();

    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    int i = 0;
    for (Map.Entry<String, String[]> entry : pays.entrySet()) {
        NameValuePair n = new BasicNameValuePair(entry.getKey(), entry.getValue()[0]);
        nameValuePairs.add(n);
    }
    
    String url = "http://trade.id888.cn:8880/cgi-bin/netpayment/pay_gate.cgi";

    HttpClient client = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(url);
	
    UrlEncodedFormEntity uefEntity;
    uefEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
    httpPost.setEntity(uefEntity);
    HttpResponse resp = client.execute(httpPost);
  	 
    byte b_ret[] = new byte[1024];
    PrintWriter pw = response.getWriter();
    resp.getEntity().getContent().read(b_ret);
    String result = new String(b_ret);
	
	
    pw.write(result);

%>

</body>
</html>