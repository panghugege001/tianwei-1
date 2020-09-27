<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>
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
                    <label class="col-sm-3 control-label">开放等级</label>
                    <div class="col-sm-5 checkbox">
                    	<label><input type='checkbox' onclick='checkAllLevel(this)' />全选</label>
						<label><input type="checkbox" name="level" value="0" class="radio">天兵</label>
                        <label><input type="checkbox" name="level" value="1" class="radio">天将</label>
                        <label><input type="checkbox" name="level" value="2" class="radio">天王</label>
                        <label><input type="checkbox" name="level" value="3" class="radio">星君</label>
                        <label><input type="checkbox" name="level" value="4" class="radio">真君</label>
                        <label><input type="checkbox" name="level" value="5" class="radio">仙君</label>
                        <label><input type="checkbox" name="level" value="6" class="radio">帝君</label> 
                        <label><input type="checkbox" name="level" value="7" class="radio">天尊</label> 
                        <label><input type="checkbox" name="level" value="8" class="radio">天帝</label>
                    </div>
                </div>
                <input type="hidden" id="levels" name="levels" />
                <div class="form-group">
             		<label class="col-sm-3 control-label">注册时间</label>
	                <div class="col-sm-5">
	                	<s:textfield id="registerTime" name="registerTimeStr" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate"/>
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

<script type="text/javascript">
	
	$(function() {
		
		var times = "${requestScope.vo.registerTime}";
		
		if (times != "" && times != null) {
		
			var d = new Date(times);
			var r = d.format("yyyy-MM-dd HH:mm:ss");
			
			$("#registerTime").val(r);  
		} else {
			
			$("#registerTime").val("");  
		}
		
		var levels = "${requestScope.vo.levels}";
		
		if (levels != "" && levels != null) {
			
			var arr = levels.split(",");
			
			$("input[name='level']").each(function() {

				var str = $(this).val();
				
				for (var i = 0, len = arr.length; i < len; i++) {
					
					var value = arr[i];
					
					if (value == str) {

						$(this).attr("checked", "true");
					}	
				}
			});
		}
	});
	
	function checkAllLevel(self) {

		$("[name='level']:checkbox").attr("checked", $(self).is(':checked'));
	};
	
	Date.prototype.format = function(fmt) {    
	    var o = {    
	        "M+": this.getMonth() + 1, //月份     
	        "d+": this.getDate(), //日     
	        "H+": this.getHours(), //小时     
	        "m+": this.getMinutes(), //分     
	        "s+": this.getSeconds(), //秒     
	        "q+": Math.floor((this.getMonth() + 3) / 3),
	        "S": this.getMilliseconds() //毫秒     
	    };  
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));    
	    for (var k in o)    
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));    
	    return fmt;    
	}
</script>