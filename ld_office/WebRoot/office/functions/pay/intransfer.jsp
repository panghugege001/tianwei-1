<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>银行转账</title>
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
    <div class="panel-heading">银行转账</div>
    <div class="panel-body">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-11">
                    <div class="panel panel-success">
                        <div class="panel-heading">转出账户</div>
                        <div id="treeview_out" class=""></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-11">
                    <div class="panel panel-info">
                        <div class="panel-heading">转入账户</div>
                        <div id="treeview_in" class=""></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-body">
                <form class="form-horizontal" role="form" id="intransfer_form">
                    <input type="hidden" id="output" name="output">
                    <input type="hidden" id="input" name="input">
                    <input type="hidden" value="${loginname}" id="loginname" name="loginname">
                    <div class="form-group text-left">
                        <label class="col-sm-5 control-label"><p id="output_p">转出：</p></label>
                        <label class="col-sm-5 control-label"><p id="input_p">转入：</p></label>
                        <br/>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="merchantCode">转账方式</label>
                        <div class="col-sm-6">
                            <label class="radio-inline">
                                <input type="radio" name="transferflag" value="0" checked>手工转账(manual transfer)
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="transferflag" value="1">自动转账(auto transfer)
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="amount">转出金额(amount):</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="amount" id="amount">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="fee">手续费(fee):</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="fee" id="fee" value="0">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="number">转出数量(笔):</label>
                        <div class="col-sm-6">
                            <input type="number" class="form-control" min="1" max="10" name="number" id="number"
                                   value="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="remark">备注:</label>
                        <div class="col-sm-6">
                            <textarea class="form-control" name="remark" id="remark" rows="6"></textarea>
                        </div>
                    </div>

                    <div class="text-center">
                        <button type="button" class="btn btn-default" onclick="intransfer_save(this)">提交</button>
                        <span>&nbsp;&nbsp;&nbsp;</span>
                        <button type="reset" class="btn btn-default">重置</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        $.ajax({
            type: "POST",
            url: '/intransfer/intransfer_tree.do',
            async: false,
            success: function (data) {
                var treeview_out = $('#treeview_out').treeview({
                    showCheckbox: true,
                    multiSelect: false,
                    levels: 1,
                    data: data,
                    onNodeChecked: function (event, node) {
                        treeview_out.treeview('uncheckAll', {silent: true});
                        if (node.leaf) {
                            treeview_out.treeview('checkNode', [node.nodeId, {silent: true}]);
                            $("#output_p").text("转出：" + node.text);
                            $("#output").val(node.id);
                        }
                    },
                    onNodeUnchecked: function (event, node) {
                        $("#output").val("");
                    }
                });
                var treeview_in = $('#treeview_in').treeview({
                    showCheckbox: true,
                    multiSelect: false,
                    levels: 1,
                    data: data,
                    onNodeChecked: function (event, node) {
                        treeview_in.treeview('uncheckAll', {silent: true});
                        if (node.leaf) {
                            treeview_in.treeview('checkNode', [node.nodeId, {silent: true}]);
                            $("#input_p").text("转入：" + node.text);
                            $("#input").val(node.id);
                        }
                    },
                    onNodeUnchecked: function (event, node) {
                        $("#input").val("");
                    }
                });
            }
        });

    })

    function intransfer_save(that) {
        $(that).hide();
        var output = $("#output").val();
        var input = $("#input").val();
        var amount = $("#amount").val();
        var fee = $("#fee").val();
        var number = $("#number").val();
        var loginname = $("#loginname").val();
        if (!output) {
            alert("请选择转出账户!");
            $(that).show();
            return false;
        }
        if (!input) {
            alert("请选择转入账户!");
            $(that).show();
            return false;
        }
        if (!fee) {
            alert("请手续费不能为空!");
            $(that).show();
            return false;
        }
        if (!amount) {
            alert("金额不能为空!");
            $(that).show();
            return false;
        }
        if (!loginname) {
            alert("请重新登陆!");
        $(that).show();
            return false;
        }
        if (number > 10) {
            alert("提案数量不能超过10!");
            $(that).show();
            return false;
        }
        if (number <= 0) {
            alert("提案数量不能小于0!");
            $(that).show();
            return false;
        }
        var form = $("#intransfer_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/intransfer/intransfer_save.do',
            async: false,
            data: form,
            success: function (data) {
            	$("#amount").val("");
                $("#fee").val("0");
                $("#number").val("1");
                $("#remark").val("1");
                alert(data);
            },
            error:function (dat) {
                alert(dat.responseText);
            }
        });
        $(that).show();
    }

</script>
</body>
</html>