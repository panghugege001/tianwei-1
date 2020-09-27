<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="myModalLabel">
                修改支付平台配置
            </h4>
        </div>
        <form class="form-horizontal" id="merchantPay_modal_form">
            <div class="modal-body">

                <input type="hidden" name="id" value="${vo.id}">
                <input type="hidden" id="loginname" name="loginname" value="${loginname}">
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="merchantCode">商户号</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="merchantCode" id="merchantCode"
                               value="${vo.merchantCode}" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="payName">商户名称</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="payName" id="payName" value="${vo.payName}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="showName">显示名称</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="showName" id="showName"
                               value="${vo.showName}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="payPlatform">支付名称简写</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="payPlatform" id="payPlatform"
                               value="${vo.payPlatform}" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="payPlatform">支付名称别名</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="asName" id="asName" value="${vo.asName}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="fee">自身费率(%)</label>
                    <div class="col-sm-5">
                        <input type="number" min="0" max="100" class="form-control" name="fee" id="fee"
                               value="${vo.fee}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="phoneFee">手机费率(%)</label>
                    <div class="col-sm-5">
                        <input type="number" min="0" max="100" class="form-control" name="phoneFee" id="phoneFee"
                               value="${vo.phoneFee}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="pcFee">PC费率(%)</label>
                    <div class="col-sm-5">
                        <input type="number" min="0" max="100" class="form-control" name="pcFee" id="pcFee"
                               value="${vo.pcFee}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="minPay">最小支付金额(￥)</label>
                    <div class="col-sm-5">
                        <input type="number" min="1" class="form-control" name="minPay" id="minPay"
                               value="${vo.minPay}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="maxPay">最大支付金额(￥)</label>
                    <div class="col-sm-5">
                        <input type="number" min="1" class="form-control" name="maxPay" id="maxPay"
                               value="${vo.maxPay}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="remark">备注</label>
                    <div class="col-sm-5">
                        <input type="text" class="form-control" name="remark" id="remark"
                               value="${vo.remark}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="usetype">使用类型</label>
                    <div class="col-sm-5">
                        <select class="form-control" name="usetype">
                            <option value="">-请选择-</option>
                            <option value="1" <c:if test="${vo.usetype == 1}">selected="selected"</c:if>>WAP</option>
                            <option value="2" <c:if test="${vo.usetype == 2}">selected="selected"</c:if>>PC
                            </option>
                            <option value="3" <c:if test="${vo.usetype == 3}">selected="selected"</c:if>>ALL
                            <option value="4" <c:if test="${vo.usetype == 4}">selected="selected"</c:if>>TEST
                            </option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label" for="amountCut">支付金额限制</label>
                    <div class="col-sm-5">
                        <input type="number" class="form-control" name="amountCut" id="amountCut" value="${vo.amountCut}">
                        <label class="label label-info">0表示没有限制，大于或者小于0的数值，都视为充值阀门开启。</label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label">支付开关</label>
                    <div class="col-sm-5 checkbox">
                        <label><input type="radio" name="paySwitch" value="1" class="control radio"
                                      <c:if test="${vo.paySwitch == 1}">checked="checked"</c:if>>开启</label>
                        <label><input type="radio" name="paySwitch" value="2" class="radio"
                                      <c:if test="${vo.paySwitch == 2}">checked="checked"</c:if>>关闭</label>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否禁用</label>
                    <div class="col-sm-5 checkbox">
                        <label><input type="radio" name="useable" value="1" class="control radio"
                                      <c:if test="${vo.useable == 1}">checked="checked"</c:if>>启用</label>
                        <label><input type="radio" name="useable" value="2" class="radio"
                                      <c:if test="${vo.useable == 2}">checked="checked"</c:if>>禁用</label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="add_update()">提交</button>
                </div>
            </div>
        </form>
    </div>
</div>

