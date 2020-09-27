<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新增最新优惠配置</title>
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<form action="/app/addLatestPreferential.do" name="mainform" id="mainform" method="post" enctype="multipart/form-data">
				<table border="0">
					<tbody>
						<tr>
							<td>优惠类型：</td>
							<td>
								<s:select list="#{ '000': '全部优惠', '001': '专题优惠', '002': '长期优惠','003':'限时优惠','004':'其他优惠' }" 
								 name="latestPreferential.type" id="type" listKey="key" 
								 listValue="value" cssStyle="width: 100px;"></s:select>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>活动标题：</td>
							<td>
								<input type="text" name="latestPreferential.activityTitle" id="activityTitle" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>活动简介：</td>
							<td>
								<textarea rows="3" cols="21" name="latestPreferential.activitySummary" id="activitySummary"></textarea>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>活动图片：</td>
							<td>
								<input type="file" name="activityImage" />
							</td>
						</tr>
						<tr>
							<td>活动开始时间：</td>
							<td>
								<s:textfield name="latestPreferential.activityStartTime" id="activityStartTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>活动结束时间：</td>
							<td>
								<s:textfield name="latestPreferential.activityEndTime" id="activityEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>APP首页显示：</td>
							<td>
								<s:select list="#{ '1': '是' , '0': '否' }" name="latestPreferential.isNew" id="isNew" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0"></s:select>
							</td>
						</tr>
						<tr id="isNewTr">
							<td>APP首页显示图片：</td>
							<td>
								<input type="file" name="newImage" />
							</td>
						</tr>
						<tr>
							<td>是否开启：</td>
							<td>
								<s:select list="#{ '1': '启用', '0': '禁用' }" name="latestPreferential.isActive" id="isActive" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple"></s:select>
								<span style="color: red;">*</span>
							</td>
						</tr>
						<tr>
							<td>是否手机端优惠：</td>
							<td>
								<s:select list="#{ '1': '是', '0': '否' }" name="latestPreferential.isPhone" id="isPhone" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple"></s:select>
							</td>
						</tr>
						<tr>
							<td>优惠领取人数：</td>
							<td>
								<input type="text" name="latestPreferential.receiveNumber" id="receiveNumber" />
							</td>
						</tr>
						<tr>
							<td>活动内容：</td>
							<td>
								<!-- 加载编辑器的容器   -->
								<script id="editor" type="text/plain" style="width: 1024px; height: 400px;"></script>	
							</td>
						</tr>
						
						<tr>
							<td>跳转地址：</td>
							<td>
								<input type="text" name="latestPreferential.openUrl" id="openUrl" />
								(注：优惠类型为专题优惠、论坛优惠时跳转地址。如果是app内部查看详情，不需要跳转浏览器，请不要填该内容)
							</td>
						</tr>						
						<tr>
							<td>添加到U乐风采：</td>
							<td>
								<s:select list="#{ '1': '是' , '0': '否' }" name="latestPreferential.isQyStyle" id="isQyStyle" listKey="key" listValue="value" cssStyle="width: 100px;" theme="simple" value="0"></s:select>
							</td>
						</tr>						
						
						<tr>
							<td colspan="2" align="center">
								<input type="button" value="提 交" id="addBtn" />
							</td>
						</tr>
					</tbody>	
				</table>
				<input type="hidden" name="latestPreferential.activityContent" id="activityContent" />	
			</form>	
		</div>
		<script type="text/javascript" src="${ctx}/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
		<script type="text/javascript" src="<c:url value='${ctx}/scripts/My97DatePicker/WdatePicker.js' />"></script>
	    <script type="text/javascript" src="${ctx}/js/ueditor.config.js"></script>
	    <script type="text/javascript" src="${ctx}/js/ueditor.all.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/lang/zh-cn/zh-cn.js"></script>
		<script type="text/javascript">
			
			$(document).ready(function () {
				
				var ue = UE.getEditor('editor');
				
				$("#isNewTr").hide();
				
				$("#isNew").change(function () {
					
					if (this.value == "0") {
						
						$("#isNewTr").hide();
					} else if (this.value == "1") {
						
						$("#isNewTr").show();
					}
				});
				
				$("#addBtn").click(function () {
					
					var self = this;
					
					$("#activityContent").val(ue.getContent());
					
					if (confirm("确定创建该记录吗？")) {
						
						if (isNull($("#activityTitle").val())) {
						
							alert("活动标题不能为空！");
							return;
						}
						
						if (isNull($("#activitySummary").val())) {
							
							alert("活动简介不能为空！");
							return;
						}
						
						var beginDate = $("#activityStartTime").val();
						var endDate = $("#activityEndTime").val();
						
						if (isNull(beginDate)) {
						
							alert("活动开始时间不能为空！");
							return;
						}
						
						if (isNull(endDate)) {
							
							alert("活动结束时间不能为空！");
							return;
						}
						
						var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
						var d2 = new Date(endDate.replace(/\-/g, "\/"));  
						
						if (d1 >= d2) {
							
							alert("活动开始时间不能大于活动结束时间！");
							return;
						}
						
						$(self).attr("disabled", "disabled");

						$('#mainform').ajaxSubmit(function(data) {

							$(self).removeAttr("disabled");

							alert(data);

							if (data.indexOf('成功') != -1) {

								var _parentWin = window.opener;
								_parentWin.mainform.submit();
								window.close();
							}
						});

						return false;
					}
				});
			});

			function isNull(v) {

				if (v == null || v == "" || v == "null" || v == undefined) {

					return true;
				}

				return false;
			};
		</script>
	</body>
</html>	