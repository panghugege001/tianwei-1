<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
	<body>
	     活动管理 --&gt; 新增活动返水时间
	    <a href="${pageContext.request.contextPath}/office/functions/activity.jsp"><font color="red">上一步</font></a>
	    <br />
	    <br />
		<div id="excel_menu">
			<s:form action="addActivity" onsubmit="submitonce(this);"
				namespace="/office" name="mainform" id="mainform" theme="simple">
				<table align="left">
					<tr>
						<td>
							活动名称:
						</td>
						<td>
							<s:textfield name="activityName" size="30" />
						</td>
					</tr>
					<tr>
						<td>
							开始时间:
						</td>
						<td>
							<s:textfield name="start" size="18"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{startTime}" cssClass="Wdate" />
						</td>
					</tr>
					<tr>
						<td>
							开始时间:
						</td>
						<td>
							<s:textfield name="end" size="18"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{endTime}" cssClass="Wdate" />
						</td>
					</tr>
					<tr>
						<td>
							返水比例:
						</td>
						<td>
							<s:textfield name="percent" size="30" />
							填入如：0.008 0.015
						</td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">
							备注:
						</td>
						<td>
						    <s:textarea name="remark" rows="8" cols="60"></s:textarea>
						</td>
					</tr>
					<tr>
						<td align="center">
							<s:submit value="提交" onclick="return submitFrom();" />
						</td>
						<td></td>
					</tr>
				</table>
			</s:form>
		</div>
		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">

						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>
