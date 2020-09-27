<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>支付页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <script src="/js/jquery18.js" type="text/javascript"></script>
    <script src="/js/qrcode.js" type="text/javascript"></script>
</head>
<body>
<c:if test="${message.code != '10000'}">
    <h2 align="center" id="massage" data-code="${message.code}">${message.desc}</h2>
</c:if>
<c:if test="${message.code == '10000'}">
    <div id="main_qrcode">
        <h2 align="center">请扫描下面的二维码进行支付</h2>
        <c:choose>
            <c:when test="${message.data.type eq '3'}">
                <div style="width:221px;height:221px;margin:0 auto;overflow:hidden; text-align: center; border:1px solid #ddd;z-index:1; ">
                    <img src="${message.data.data}" width="220" height="220">
                </div>
            </c:when>
            <c:otherwise>
                <div id="qrcode"
                     style="width:221px;height:221px;margin:0 auto;overflow:hidden; text-align: center; border:1px solid #ddd;z-index:1; "></div>
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
            </c:otherwise>
        </c:choose>
    </div>
</c:if>
</body>
</html>