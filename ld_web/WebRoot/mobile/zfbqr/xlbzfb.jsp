<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<base href="../../">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1, maximum-scale=1.0" />
	<link rel="stylesheet" type="text/css" href="mobile/css/wxqrcode.css" />
	
	<script type="text/javascript" src="mobile/js/lib/jquery/jquery-1.10.2.min.js" ></script>
	<script type="text/javascript" src="mobile/js/lib/jquery/qrcode.js"></script>
	<script type="text/javascript" src="mobile/js/lib/jquery/jquery.qrcode.js"></script>
</head>
<body>
	<div class="qrcode-block">
		<div class="qrcode" id="qrcode" align="center"></div>
<!-- 		<img class="logo" src="/images/dinpay.png" /> -->
	</div>
	<div class="message" align="center">
		支付宝扫码支付
<!-- 		<img src="/images/scanning.png"/> -->
	</div>
	<script type="text/javascript" language="javascript">
		var flag = $('body').width()<=480;
		$('body').addClass(flag?'mobile':'desktop');
		
  		var qrcodeUrl= '<%=request.getParameter("qrcode")%>';
		if (qrcodeUrl&&qrcodeUrl!='null') {
			var width = 200,height = 200;
			
			if(flag){
				width = $('body').width()*0.8;
				height = width;
			}
			$("#qrcode").qrcode({width : width,height : height,text:qrcodeUrl});
			$(".qrcode-block .logo").addClass("show");
		}else{
			alert('二维码产生失败，请重新操作！');
			window.history.back();
		}
	</script>
</body>
</html>