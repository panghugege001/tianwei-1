<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>支付页面</title>
</head>
<c:if test="${message.code != '10000'}">
    <body>
    <h3 id="message" data-code="${message.code}">${message.desc}</h3>
    </body>
</c:if>
<c:if test="${message.code == '10000'}">
    <body onload="submitOrder()">
    <input id="payUrl" type="hidden" value="${message.data.url}">
    <form id="payForm" name="payForm" method="post">
        <c:forEach items="${message.data.params}" var="item">
            <input type="hidden" name="${item.key}" value='${item.value}'>
        </c:forEach>
    </form>

    <h3 style="display: none" id="massage"></h3>
    <div id="main_qrcode" style="display:none">
        <h2 align="center">请扫描下面的二维码进行支付</h2>
        <div id="qrcode"
             style="width:221px;height:221px;margin:0 auto;overflow:hidden; text-align: center; border:1px solid #ddd;z-index:1; "></div>
        <div></div>
        <input id="scan2d" value="${message.data.data}" type="hidden"/>
    </div>
    </body>
</c:if>
</html>
<script src="/js/jquery18.js" type="text/javascript"></script>
<script src="/js/qrcode.js" type="text/javascript"></script>
<script type="application/javascript">
    function submitOrder() {
        var formObject = {};
        var platformId = "1";
        var showType ="";
        var formArray =$("#payForm").serializeArray();

        $.each(formArray,function(i,item){
            if(item.name!="platformId"&&item.name!="showType") {
                formObject[item.name] = item.value;
            } else {
                if(item.name=="platformId") {
                    platformId = item.value;
                } else {
                    showType = item.value;
                }
            }
        });
        var data = JSON.stringify(formObject);
        $.ajax({
            type: "post",
            url: $("#payUrl").val(),
            async: false,
            data: data,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(payReturnData) {
                if(payReturnData.status==0) {
                    if(showType=="2") {
                        $("#scan2d").val(payReturnData.qrCode)
                        var qrcode = $("#scan2d").val();
                        if (qrcode) {
                            $("#qrcode").html("");
                            var oQRCode = new QRCode("qrcode", {
                                width: 220,
                                height: 220
                            });
                            oQRCode.clear();
                            $("#main_qrcode").css("display", "block");
                            $("#dinpayLogo").css("display", "block");
                            oQRCode.makeCode(qrcode);
                            $("#scan2d").val(qrcode);
                        } else {
                            $("#main_qrcode").css("display", "none");
                            $("body").html("<h1>支付异常</h1>");
                        }
                    } else {
                        window.location.href = payReturnData.qrCode;
                    }
                } else {
                    $("#massage").html(payReturnData.message);
                    $("#massage").show();
                }
            },
            error:function() {
                $("#massage").html("系统异常");
                $("#massage").show();
            }
        });
    }
</script>