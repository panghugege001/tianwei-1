<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <link href="/resources/js/bootstrap3/css/bootstrap.css?v=2" rel="stylesheet">
    <script src="/resources/js/jquery-3.1.1.min.js?v=2"></script>
    <script src="/resources/js/bootstrap3/js/bootstrap.js?v=2"></script>
</head>
<body>
<div class="tab-pane">
    <div class="container panel">
        <form id="dinpayRedirect" action="https://shenghuo.alipay.com/send/payment/fill.htm" method=post
              name="dinpayRedirect" id="dinpayRedirect">
            <input type="hidden" name="attach" id="attach" value="${vo.loginName}"/>
            <div class="row">
                <div class="col-sm-2">
                    <h3>账户 名</h3>
                </div>
                <div class="col-sm-4">
                    <h3>${vo.loginName}</h3>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-2">
                    <h3>账户余额</h3>
                </div>
                <div class="col-sm-4">
                    <h3>${vo.credit}元</h3>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-2">
                    <h3>支付宝账号</h3>
                </div>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="optEmail" readonly="readonly"
                           value="${bankInfo.accountNo}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-2">
                    <h3>附言</h3>
                </div>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="title" readonly="readonly"
                           value="${bankInfo.vpnPassword}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-8">
                    <a class="btn" href="https://www.alipay.com/" target="_blank">跳转到支付宝</a>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-8">
                    <div class="alert alert-warning" role="alert">为了您的存款能够安全快速到账， 请勿修改收款人和付款说明，祝您游戏愉快，谢谢</div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>