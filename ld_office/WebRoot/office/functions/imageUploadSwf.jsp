<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
	</head>
	<body>
		<div id="excel_menu_left" style="margin-top: 20px;">
			官网优惠 --&gt; 官网优惠图片新增
		</div>
		<s:form action="uploadImageSwf" namespace="/office" theme="simple"
			enctype="multipart/form-data">
			<table>
				<tr>
					<td align="right">
						图片名称:
					</td>
					<td>
						<s:textfield name="imageName" />
					</td>
				</tr>
				<tr>
					<td align="right">
						开始时间:
					</td>
					<td>
						<s:textfield name="startTime" size="18"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"
							value="%{startTime}" cssClass="Wdate" />
					</td>
				</tr>

				<tr>
					<td align="right">
						结束时间:
					</td>
					<td>
						<s:textfield name="endTime" id="end" size="18"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"
							value="%{endTime}" cssClass="Wdate" />
					</td>
				</tr>
				<tr>
						<td valign="top">
							备注:
						</td>
						<td>
						<s:textarea name="remark" style="width: 300px; height: 100px;"></s:textarea>
						</td>
					</tr>
				<tr>
					<td align="right">
						首页旋转Flash图:
					</td>
					<td>
						<s:file name="image004" />
					</td>
				</tr>
				<tr>
					<td>

					</td>
					<td>
						<s:submit value="提交" />
					</td>
				</tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
