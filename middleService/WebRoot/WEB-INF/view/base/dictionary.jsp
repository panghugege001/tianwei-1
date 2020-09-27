<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>支付分发平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <jsp:include page="/pages/common.jsp"></jsp:include>
</head>
<body>
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">字典项管理</h3>
    </div>
    <div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form-inline" role="form" id="dictionary_form">
                    <div class="form-group">
                        <label class="sr-only" for="dictType">字典大项</label>
                        <input type="text" class="form-control" name="dictType" id="dictType" placeholder="字典大项">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="dictName">字典项名称</label>
                        <input type="text" class="form-control" name="dictName" id="dictName" placeholder="字典项名称">
                    </div>
                    <div class="checkbox">
                        <label><input type="checkbox" name="useable" value="1">可用</label>
                        <label><input type="checkbox" name="useable" value="0">禁用</label>
                    </div>
                    <button type="button" class="btn btn-default" id="dictionary_query">查询</button>
                    <button type="button" class="btn btn-default" id="dictionary_add">添加</button>
                </form>
            </div>
        </div>
        <table class="table table-condensed">
            <thead>
            <tr>
                <th>字典大项</th>
                <th>字典项名称</th>
                <th>字典项值</th>
                <th>字典项描述</th>
                <th>排序</th>
                <th>是否可用</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="dicationary_tbody"></tbody>
        </table>
    </div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
</div>

<script>
    query();
    function query() {
        var form = $("#dictionary_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/dictionary/query',
            data: form,
            success: function (data) {
                $("#dicationary_tbody").html(data);
            }
        });
    }

    $("#dictionary_query").click(function () {
        query();
    });

    $("#dictionary_add").click(function () {
        $.ajax({
            type: "POST",
            url: '/dictionary/add',
            async: false,
            success: function (data) {
                $('#myModal').html(data);
                $('#myModal').modal('show');
            }
        });
    });

    function add_update() {
        var form = $("#dictionary_modal_form").serializeObject();
        $.ajax({
            type: "POST",
            url: '/dictionary/add_update',
            data: form,
            success: function (data) {
                $('#myModal').modal('hide');
                query();
            }
        });
    }

    function dictionary_delete(id) {
        if (confirm("确定要删除吗？")) {
            $.ajax({
                type: "POST",
                url: '/dictionary/delete',
                data: {id: id},
                success: function () {
                    query();
                }
            });
        }
    }

    function dictionary_edit(id) {
        $.ajax({
            type: "POST",
            url: '/dictionary/edit',
            data: {id: id},
            async: false,
            success: function (data) {
                $('#myModal').html(data);
                $('#myModal').modal('show');
            }
        });
    }
</script>
</body>
</html>
