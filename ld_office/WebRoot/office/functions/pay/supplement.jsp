<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/office/include.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>补单页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <link href="/resources/js/bootstrap3/bootstrap-table.css?v=2" rel="stylesheet">
    <link href="/resources/js/bootstrap3/css/bootstrap.css?v=2" rel="stylesheet">
    <link href="/resources/js/bootstrap3/bootstrap-treeview.min.css?v=2" rel="stylesheet">
    <script src="/resources/js/jquery-3.1.1.min.js?v=2"></script>
    <script src="/resources/js/bootstrap3/js/bootstrap.js?v=2"></script>
    <script src="/resources/js/bootstrap3/bootstrap-table.js?v=2"></script>
    <script src="/resources/js/bootstrap3/bootstrap-table-zh-CN.js?v=2"></script>
    <script src="/resources/js/bootstrap3/bootstrap-treeview.js?v=2"></script>
    <script src="/resources/js/base.js?v=1"></script>

</head>
<body>
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">手工补单</h3>
    </div>
    <div class="panel-body">
        <form id="supplementForm" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderid">商户订单号</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="orderid" id="orderid">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginname">支付平台</label>
                <div class="col-sm-4">
                    <select class="form-control" id="platformId" name="platformId" onchange="platformChange(this)">
                        <c:forEach var="item" items="${pays}">
                            <option value="${item.id}" data_type="${item.payWay}">${item.payName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginname">用户名</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" name="loginname" id="loginname">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="money">金额</label>
                <div class="col-sm-4">
                    <input type="number" min="1" class="form-control" name="money" id="money">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="button" class="btn btn-default" onclick="supplement()">提交</button>
                    <button type="reset" class="btn btn-default">重置</button>
                </div>
            </div>
        </form>
    </div>
    <script>
        function supplement() {
            if (!$("#orderid").val()) {
                alert("请输入商户订单号");
                return false;
            }
            if (!$("#loginname").val()) {
                alert("请输入用户名");
                return false;
            }
            if (!$("#money").val()) {
                alert("请输入金额");
                return false;
            }
            var data = $("#supplementForm").serializeObject();
            $.post("/bankinfo/supplement_save.do", data, function (data) {
                console.dir(data);
                if (data.code == "10000") {
                    alert("补单成功,等待审核...");
                } else {
                    alert(data.desc)
                }
            });
        }

    </script>
</div>
</body>
</html>
