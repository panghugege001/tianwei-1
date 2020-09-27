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

    String sb = request.getParameter("sb");

    int of = sb.indexOf("?");
    if (of > 0) {
        sb = sb.substring(0, of);
    }
    int line = sb.indexOf("/");
    String code = sb.substring(0, line);
    String rt = sb.substring(line, sb.length());
    String url = "";
    if ("test".equals(code)) {
        url = "http://119.28.11.28:7080" + rt + ".php";
    } else if ("e68".equals(code)) {
        url = "http://pay.jiekoue68.com:2112/third/rt.aspx?rt=" + rt;
    } else if ("qy".equals(code)) {
        url = "http://pay.huidiaoqy.com:6226/third/rt.php?rt=" + rt;
    } else if ("l8".equals(code)) {
        url = "http://pay.huidiaolong8.com:6336/third/rt.php?rt=" + rt;
    } else if ("777".equals(code)) {
        url = "http://pay.yhback.com:6446/third/rt.php?rt=" + rt;
    } else if ("dc".equals(code)) {
        url = "http://pay.jiekoudream.com:2226/third/rt.php?rt=" + rt;
    } else if ("ule".equals(code)) {
        url = "http://pay.ulback.com:3113/third/rt.php?rt=" + rt;
    } else if ("ws".equals(code)) {
        url = "http://pay.jiekouws.com:5555/third/rt.php?rt=" + rt;
    } else if ("ql".equals(code)) {
        url = "http://pay.jiekouqle.com:1777/third/rt.php?rt=" + rt;
    } else if ("uf".equals(code)) {
        url = "http://pay.jiekouufa.com:1666/third/rt.php?rt=" + rt;
    } else if ("lh".equals(code)) {
        url = "http://pay.longhuback.com:10080/third/rt.php?rt=" + rt;
    } else if ("ld".equals(code)) {
        url = "http://pay.longduback.com:11080/third/rt.aspx?rt=" + rt;
    } else if ("zb".equals(code)) {
        url = "http://pay.zbback.com:12080/third/rt.php?rt=" + rt;
    }

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