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
<jsp:include page="/pages/navbar.jsp"></jsp:include>
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">在线支付管理</h3>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form-inline" role="form" id="payOrder_form">
                    <div class="form-group">
                        <label class="sr-only" for="type">订单状态</label>
                        <select class="form-control" name="type" id="type">
                            <option value="">--请选择--</option>
                            <option value="1">待处理</option>
                            <option value="2">已取消</option>
                            <option value="3">成功</option>
                            <option value="4">未支付</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="createTimeStart">开始时间</label>
                        <input type="text" name="createTimeStart" id="createTimeStart">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="createTimeEnd">结束时间</label>
                        <input type="text" name="createTimeEnd" id="createTimeEnd">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="billNo">支付单号</label>
                        <input type="text" name="billNo" id="billNo">
                    </div>
                    <button type="button" class="btn btn-default" id="payOrder_query">查询</button>
                </form>
            </div>
        </div>
        <div>
            <table id="dataTable"></table>
        </div>
    </div>
</div>
<div id="toolbar"></div>
<script>

    var $table = $("#dataTable");
    var $myModal = $('#myModal');

    $(function () {

        initTable();

        $("#payOrder_query").click(function () {
            query();
        });

        $("#payOrder_add").click(function () {
            $.ajax({
                type: "POST",
                url: '/payOrder/add',
                success: function (data) {
                    $myModal.html(data);
                    $myModal.modal('show');
                }
            });
        });
    });

    function initTable() {
        $table.bootstrapTable({
            method: 'post',
            toolbar: '#toolbar',
            pagination: true,
            sidePagination: "server",
            queryParams: queryParams,
            url: "/payOrder/query",
            showRefresh: true,
            columns: [
                {title: "支付订单号", field: "billNo"},
                {title: "支付平台", field: "payPlatform"},
                {title: "状态", field: "type"},
                {title: "支付类型", field: "newaccount"},
                {title: "会员账号", field: "loginName"},
                {title: "会员姓名", field: "aliasName"},
                {title: "金额", field: "money"},
                {title: "来源IP", field: "ip"},
                {title: "加入时间", field: "createTime"},
                {title: "支付时间", field: "returnTime", formatter: amountCut_format},
                {title: "说明", field: "msg", formatter: amountCut_format},
                {title: "操作", field: "id", formatter: operator_format}
            ]
        });
    }

    function queryParams(params) {
        return params;
    }

    function operator_format(id) {
        return "<button type='button' class='btn btn-primary' onclick='payOrder_edit(" + id + ")'>修改</button>";
    }

    function amountCut_format(value) {
        if (value == 0) {
            value = "没有限制";
        } else if (value == 1) {
            value = "1000-";
        } else if (value == 2) {
            value = "1000+";
        }
        return "<button type='button' class='btn btn-info'>" + value + "</button>";
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
        var form = $("#payOrder_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/payOrder/query',
            data: form,
            success: function (data) {
                $table.bootstrapTable('load', data);
            }
        });
    }

    function paySwitch_edit(id, _switch) {

        if (confirm("确定要" + (_switch == 1 ? "开启" : "关闭") + "吗？")) {
            $.ajax({
                type: "POST",
                url: '/payOrder/add_update',
                data: {id: id, paySwitch: _switch},
                success: function (data) {
                    $myModal.modal('hide');
                    query();
                }
            });
        }
    }

    function add_update() {
        var form = $("#payOrder_modal_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/payOrder/add_update',
            data: form,
            success: function (data) {
                $myModal.modal('hide');
                query();
            }
        });
    }

    function payOrder_delete(id) {
        if (confirm("确定要删除吗？")) {
            $.ajax({
                type: "POST",
                url: '/payOrder/delete',
                data: {id: id},
                success: function () {
                    query();
                }
            });
        }
    }

    function payOrder_edit(id) {
        $.ajax({
            type: "POST",
            url: '/payOrder/edit',
            data: {id: id},
            success: function (data) {
                $myModal.html(data);
                $myModal.modal('show');
            }
        });
    }
</script>
</body>
</html>
