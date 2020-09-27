<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>支付分发平台</title>
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
    <script type="text/javascript" src="/scripts/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">支付平台配置管理</h3>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form-inline" role="form" id="merchantPay_form">
                    <input type="hidden" id="loginname" value="${loginname}">
                    <div class="form-group">
                        <label class="sr-only" for="merchantCode">商户号</label>
                        <input type="text" class="form-control" name="merchantCode" id="merchantCode" placeholder="商户号">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="payName">商户名称</label>
                        <input type="text" class="form-control" name="payName" id="payName" placeholder="商户名称">
                    </div>
                    <div class="form-group">
                        <label class="control-label">支付方式</label>
                        <select class="form-control" name="payWay">
                            <option value="">--请选择--</option>
                            <option value="1">支付宝</option>
                            <option value="2">微信</option>
                            <option value="3">在线支付</option>
                            <option value="4">快捷支付</option>
                            <option value="5">点卡支付</option>
                            <option value="6">微信秒存</option>
                            <option value="7">QQ支付</option>
                            <option value="8">通联转账</option>
                            <option value="10">京东支付</option>
                            <option value="12">久安支付</option>
                            <option value="13">银联支付</option>
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
                    <span>&nbsp;&nbsp;</span>
                    <div class="form-group">
                        <label class="control-label">是否禁用：</label>
                        <select name="useable" class="form-control">
                            <option value="">-请选择-</option>
                            <option value="1" selected="selected">启用</option>
                            <option value="2">禁用</option>
                        </select>
                    </div>
                    <span>&nbsp;&nbsp;&nbsp;</span>
                    <button type="button" class="btn btn-default" id="merchantPay_query">查询</button>
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

    });

    function initTable() {
        $table.bootstrapTable({
            columns: [
                {title: "ID", field: "id", align: "center", valign: "middle", sortable: true},
                {title: "商户号", field: "merchantCode"},
                {title: "商户名称", field: "payName"},
                {title: "显示名称", field: "showName"},
                {title: "简写", field: "payPlatform"},
                {title: "别名", field: "asName"},
                {title: "自身费率", field: "fee", sortable: true, formatter: percent_format},
                {title: "手机费率", field: "phoneFee", sortable: true, formatter: percent_format},
                {title: "pc费率", field: "pcFee", sortable: true, formatter: percent_format},
                {title: "最小支付", field: "minPay", sortable: true},
                {title: "最大支付", field: "maxPay", sortable: true},
                {title: "累计存款金额", field: "totalAmount", sortable: true},
                {title: "下发金额", field: "amount", sortable: true},
                {title: "使用类型", field: "usetype", sortable: true, formatter: usetype_format},
                {title: "是否禁用", field: "useable", sortable: true, formatter: useable_format},
                {title: "支付开关", field: "paySwitch", sortable: true, formatter: paySwitch_format},
                {title: "付金额限制", field: "amountCut", formatter: amountCut_format},
                {title: "备注", field: "remark"},
                {title: "操作", field: "id", formatter: operator_format}
            ], data: []
        });
    }

    function percent_format(value) {
        return value + "%";
    }

    function operator_format(id) {
        return "<button type='button' class='btn btn-primary' onclick='merchantPay_edit(" + id + ")'>修改</button>";
    }

    function amountCut_format(value) {
        if (value == 0) {
            value = "没有限制";
        }
        return "<button type='button' class='btn btn-info'>" + value + "</button>";
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

    function useable_format(value, row) {
        var _switch = "";
        if (value == 1) {
            _switch = "<button type='button' class='btn btn-success' onclick='useable_edit(" + row.id + "," + (value + 1) + ")'>启用</button>";
        } else if (value == 2) {
            _switch = "<button type='button' class='btn btn-danger' onclick='useable_edit(" + row.id + "," + (value - 1) + ")'>禁用</button>";
        }
        return _switch;
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
            url: '/bankinfo/merchantpay_query.do',
            data: form,
            success: function (data) {
                if (data) {
                    $table.bootstrapTable('load', $.parseJSON(data));
                } else {
                    alert("会话已失效，请重新登陆!");
                    top.location.href = "<s:url value='/i18nlogin.do' />";
                }
            }
        });
    }

    function useable_edit(id, _switch) {
        if (confirm("确定要" + (_switch == 1 ? "启用" : "禁用") + "吗？")) {
            $.ajax({
                type: "POST",
                url: '/bankinfo/merchantpay_add_update.do',
                data: {
                    id: id, useable: _switch,
                    loginname: $("#loginname").val()
                },
                success: function (data) {
                    if (data == "SUCCESS") {
                        query();
                    } else {
                        alert("更新失败!");
                    }
                }
            });
        }
    }

    function paySwitch_edit(id, _switch) {
        console.dir(_switch);
        if (confirm("确定要" + (_switch == 1 ? "开启" : "关闭") + "吗？")) {
            $.ajax({
                type: "POST",
                url: '/bankinfo/merchantpay_add_update.do',
                data: {
                    id: id,
                    paySwitch: _switch,
                    loginname: $("#loginname").val()
                },
                success: function (data) {
                    if (data == "SUCCESS") {
                        query();
                    } else {
                        alert("更新失败!");
                    }
                }
            });
        }
    }

    function add_update() {
    	
    	var arr = [];

    	$("input[name='level']:checked").each(function() {

    		arr.push($(this).val());
    	});
    	
    	$("#levels").val(arr.join(","));
    	
        var form = $("#merchantPay_modal_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/bankinfo/merchantpay_add_update.do',
            data: form,
            success: function (data) {
                if (data == "SUCCESS") {
                    $myModal.modal('hide');
                    query();
                } else {
                    alert("更新失败!");
                }
            }
        });
    }

    function merchantPay_edit(id) {
        $.ajax({
            type: "POST",
            url: '/bankinfo/merchantpay_edit.do',
            data: {id: id, loginname: $("#loginname").val()},
            success: function (data) {
                if (data) {
                    $myModal.html(data);
                    $myModal.modal('show');
                } else {
                    alert("会话已失效，请重新登陆!");
                    top.location.href = "<s:url value='/i18nlogin.do' />";
                }
            }
        });
    }
</script>
</body>
</html>
