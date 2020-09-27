<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="myModalLabel">
                修改字典项
            </h4>
        </div>
        <form class="form-horizontal" id="dictionary_modal_form">
            <div class="modal-body">

                <input type="hidden" name="id" value="${vo.id}">
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="dictType">字典大项</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="dictType" id="dictType"
                               value="${vo.dictType}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="dictName">字典项名称</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="dictName" id="dictName"
                               value="${vo.dictName}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="dictValue">字典项值</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="dictValue" id="dictValue"
                               value="${vo.dictValue}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="dictDesc">字典项描述</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="dictDesc" id="dictDesc"
                               value="${vo.dictDesc}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="orderBy">排序</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="orderBy" id="orderBy" value="${vo.orderBy}">
                    </div>
                </div>
                <div class="col-sm-8">
                    <div class="checkbox">
                        <label><input type="checkbox" name="useable" value="1"
                                      <c:if test="${vo.useable == 1}>">checked="checked"</c:if>>可用</label>
                        <label><input type="checkbox" name="useable" value="0"
                                      <c:if test="${vo.useable == 0}>">checked="checked"</c:if>>禁用</label>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="add_update()">提交</button>
            </div>
        </form>
    </div>
</div>

