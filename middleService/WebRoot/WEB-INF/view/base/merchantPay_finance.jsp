<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>支付分发平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <jsp:include page="/pages/common.jsp"></jsp:include>
</head>
<body>
<%
    session.setAttribute("operator", "${loginname}");
%>
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">财务专员-支付平台配置</h3>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form-inline" role="form" id="merchantPay_form">
                    <input type="hidden" name="useable" value="1">
                    <input type="hidden" id="loginname" value="${loginname}">
                    <div class="form-group">
                        <label class="sr-only" for="payName">商户名称</label>
                        <input type="text" class="form-control" name="payName" id="payName" placeholder="商户名称">
                    </div>
                    <span>&nbsp;&nbsp;</span>
                    <div class="form-group">
                        <label class="control-label">支付方式</label>
                        <select class="form-control" name="payWay" id="payWay">
                            <option value="">--请选择--</option>
                            <option value="1">支付宝</option>
                            <option value="11">支付宝附言</option>
                            <option value="2">微信</option>
                            <option value="3">在线支付</option>
                            <option value="4">快捷支付</option>
                            <option value="5">点卡支付</option>
                        </select>
                    </div>
                    <span>&nbsp;&nbsp;</span>
                    <div class="form-group">
                        <label class="control-label">支付开关：</label>
                        <select name="paySwitch" class="form-control">
                            <option value="">-请选择-</option>
                            <option value="1" selected="selected">开启</option>
                            <option value="2">关闭</option>
                        </select>
                    </div>
                    <span>&nbsp;&nbsp;&nbsp;</span>
                    <button type="button" class="btn btn-default" id="merchantPay_query">查询</button>
                    <span>&nbsp;&nbsp;</span>
                    <button type="reset" class="btn btn-default" id="merchantPay_query">重置</button>
                </form>
            </div>
        </div>
        <div>
            <table id="dataTable"></table>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
</div>
<script>

    var $table = $("#dataTable");
    var $myModal = $('#myModal');

    $(function () {

        initTable();

        $("#merchantPay_query").click(function () {
            query();
        });

        $("#merchantPay_add").click(function () {
            $.ajax({
                type: "POST",
                url: '/merchantPay/add',
                success: function (data) {
                    $myModal.html(data);
                    $myModal.modal('show');
                }
            });
        });
    });

    function initTable() {
        $table.bootstrapTable({
            columns: [
                {title: "商户名称", field: "payName"},
                {title: "显示名称", field: "showName"},
                {title: "简写", field: "payPlatform"},
                {title: "别名", field: "asName"},
                {title: "自身费率", field: "fee", sortable: true, formatter: percent_format},
                {title: "手机费率", field: "phoneFee", sortable: true, formatter: percent_format},
                {title: "pc费率", field: "pcFee", sortable: true, formatter: percent_format},
                {title: "最小支付", field: "minPay", sortable: true},
                {title: "最大支付", field: "maxPay", sortable: true},
                {title: "使用类型", field: "usetype", sortable: true, formatter: usetype_format},
                {title: "支付开关", field: "paySwitch", formatter: paySwitch_format},
                {title: "付金额限制", field: "amountCut", formatter: amountCut_format},
                {title: "备注", field: "remark"},
            ], data: []
        });
    }

    function percent_format(value) {
        return value + "%";
    }

    function operator_format(id) {
        return "<button type='button' class='btn btn-primary' onclick='merchantPay_edit(" + id + ")'>修改</button>";
    }

    function usetype_format(value) {
        if (value == 1) {
            value = "WAP";
        } else if (value == 2) {
            value = "PC";
        } else if (value == 3) {
            value = "ALL";
        } else if (value == 4) {
            value = "TEST"
        }
        return "<button type='button' class='btn btn-info'>" + value + "</button>";
    }

    function amountCut_format(value, row) {
        var div = $("<div>")
        var input = $("<input type='number' class='form-control' value="+value+" onchange='amountCut_edit(" + row.id + ",this" + ")'>");
        var select = $("<select class='form-control' onchange='amountCut_edit(" + row.id + ",this" + ")'>");
        div.append(input);
        return $(div).html();
    }

    function paySwitch_format(value, row) {
        var _switch = "";
        if (value == 1) {
            _switch = "<button type='button' class='btn btn-success' onclick='paySwitch_edit(" + row.id + "," + (value + 1) + ")'>开启</button>";
        } else if (value == 2) {
            _switch = "<button type='button' class='btn btn-warning' onclick='paySwitch_edit(" + row.id + "," + (value - 1) + ")'>关闭</button>";
        }
        return _switch;
    }

    function query() {
        var form = $("#merchantPay_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/merchantPay/query',
            data: form,
            success: function (data) {
                $table.bootstrapTable('load', data);
            }
        });
    }

    function amountCut_edit(id, that) {
        console.dir(that);
        var value = $(that).val();
        if (confirm("确定要改吗？")) {
            $.ajax({
                type: "POST",
                url: '/merchantPay/add_update',
                data: {
                    id: id, amountCut: value,
                    loginname: $("#loginname").val()
                },
                success: function (data) {
                    $myModal.modal('hide');
                    query();
                }
            });
        }
    }

    function useable_edit(id, _switch) {
        if (confirm("确定要" + (_switch == 1 ? "启用" : "禁用") + "吗？")) {
            $.ajax({
                type: "POST",
                url: '/merchantPay/add_update',
                data: {
                    id: id, useable: _switch,
                    loginname: $("#loginname").val()
                },
                success: function (data) {
                    if (data == "SUCCESS") {
                        query();
                    } else {
                        location.href = "/common/error";
                    }
                }
            });
        }
    }

    function paySwitch_edit(id, _switch) {
        if (confirm("确定要" + (_switch == 1 ? "开启" : "关闭") + "吗？")) {
            $.ajax({
                type: "POST",
                url: '/merchantPay/add_update',
                data: {
                    id: id, paySwitch: _switch,
                    loginname: $("#loginname").val()
                },
                success: function (data) {
                    if (data == "SUCCESS") {
                        query();
                    } else {
                        location.href = "/common/error";
                    }
                }
            });
        }
    }


</script>
</body>
</html>
