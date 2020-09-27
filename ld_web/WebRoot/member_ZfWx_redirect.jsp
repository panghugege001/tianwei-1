<%@ page language="java" import="java.util.*,java.text.*,org.apache.commons.lang.StringUtils"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <script src="/js/qrcode.js" type="text/javascript"></script>
		<script src="/js/jquery18.js" type="text/javascript"></script>
</head>
<body>
<div style="width: 520px;height: 250px; border-bottom: 2px solid #0785cc;border-top:2px solid #0785cc; text-align:center;padding:10px 0;margin: 0 auto;position:relative; ">
	    <div id="qrcode" style="width:200px;height:200px;margin:0 auto;overflow:hidden; text-align: center; border:1px solid #ddd;z-index:1; " ></div>
	    <img id="dinpayLogo" src="/images/dinpay.png" style="z-index:2;position:absolute;left:50%;top:50%;margin: -56px 0 0 -32px;display: none"/>
	    <div ><img src="/images/scanning.png" align="middle" style="margin:10px auto;width:100px;height: 30px;"/></div>
	    <input id="scan2d" value=${qrcode} style="width:300px;" type="hidden"/>
    </div>
    <script type="text/javascript" language="javascript">
  var  qrcode= $("#scan2d").val();
if(qrcode!=null||qrcode!=""){
	$("#qrcode").html("");
	var oQRCode = new QRCode("qrcode", {
					   width : 200,
					   height : 200
	                 });
					  oQRCode.clear();
					  $("#dinpayLogo").css("display","block");
					  oQRCode.makeCode(qrcode);
					  $("#scan2d").val(qrcode);
}
	
</script>
</body>
</html>