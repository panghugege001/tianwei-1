<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>久安钱包</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0,user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${ctx}/jiuanPay/css/jiuan.css?v=666" />
    <script src="${ctx}/js/jquery18.js" type="text/javascript"></script>
    <script src="${ctx}/js/qrcode.js" type="text/javascript"></script>
</head>
<body>

<header class="header" >
    <div class="header-in">
        <img src="${ctx}/jiuanPay/images/logo.png" class="logo">
        <div class="logo-right hidden-lg">您好，欢迎使用久安支付！ </div>
    </div>

</header>
<div class="content-box">
    <div class="content" id="pageOne">
        <div class="content01">
            <div class="book-box">
            </div>
        </div>
        <div class="content02 mobile animated bounceInRight">
            <div class="c-l">
                <p class="c-l-title"> 久安扫码支付</p>
                <div class="qrcode-box">
                    <div id="qrcode"
                         style="width:221px;height:221px;text-align: center; border:1px solid #ddd;z-index:1; "></div>
                     <div></div>
		                <input id="scan2d" value="${message.data.data}" type="hidden"/>
		                <script type="text/javascript" language="javascript">
		                    var qrcode = $("#scan2d").val();
		                    if (qrcode) {
		                        $("#qrcode").html("");
		                        var oQRCode = new QRCode("qrcode", {
		                            width: 220,
		                            height: 220
		                        });
		                        oQRCode.clear();
		                        $("#dinpayLogo").css("display", "block");
		                        oQRCode.makeCode(qrcode);
		                        $("#scan2d").val(qrcode);
		                    } else {
		                        $("#main_qrcode").css("display", "none");
		                        $("body").html("<h1>支付异常</h1>");
		                    }
		               </script>
                </div>
                <p class="i-scan">打开久安钱包<br>扫一扫</p>
            </div>
            <div class="c-r">
                <img src="${ctx}/jiuanPay/images/phone.png">
                <p>
                    不会使用？请参照上图<br>
                    没有久安钱包APP？ <a href="http://www.jiuan365.com" class="blue" target="_blank">前往下载 > </a>
                </p>
            </div>

        </div>
        <div class="clear"></div>
    </div>
</div>
<footer class="footer">
    <div  class="footer-in">
		<span class="fl hidden-lg">
			久安钱包<br>支持支付方式:
		</span>
        <img src="${ctx}/jiuanPay/images/pay.png">
    </div>
</footer>
</body>
</html>