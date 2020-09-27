<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>信用预支</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-ui-1.8.21.custom.min.js"></script>
					<style type="text/css">
/*.search_margin{*/ /*margin-left:8px;*/ /*}*/
.label_search_td_play {
	font-family: Tahoma;
	font-size: 15px;
	/*font-size: 11px;*/
	line-height: 28px;
	font-weight: bold;
	/* text-align: center;*/
	text-transform: capitalize;
	color: #FFFFFF;
	text-decoration: none;
	padding-right: 1px;
}

.input {
	font-family: Tahoma;
	font-size: 18px;
	/*font-size: 11px;*/
	font-weight: normal;
	/*text-transform: capitalize;*/
	text-decoration: none;
	background-color: #FFFFFF;
	border: 1px solid #336699;
	line-height: 16px;
	height: 22px;
	float: left;
	margin-top: 2px;
}
</style>
		<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getProxyAdvance.do' />";
frm.submit();
}
</script>
	</head>
	<body>
		<div id="excel_menu_left">
			操作 --> 信用预支
		</div>
		<div id="excel_menu">
			<p align="left">
				<s:fielderror />
				<s:actionerror />
			</p>
			<s:form action="addProxyCashOutProposal" onsubmit="submitonce(this);"
				namespace="/office" name="mainform" id="mainform" theme="simple">
				<table align="left">
					<tr>
						<td>
							预支帐号<span style="color:red">*</span>:
						</td>
						<td>
							<input type="text" name="loginname" value="${loginname}" class="input" style="width: 250px;"
								onblur="loadInfo();" />
						</td>
					</tr>
					<tr>
						<td>
							预支金额<span style="color:red">*</span>:
						</td>
						<td>
							<input type="text" name="amount" value="${amount}" class="input" style="width: 250px;"/>
						</td>
					</tr>
					<tr>
						<td>
							银行帐号<span style="color:red">*</span>:
						</td>
						<td>
							<input type="text" name="accountNo" value="${userbank.bankno}" class="input" style="width: 250px;"/>
						</td>
					</tr>
					<tr>
						<td>
							帐号姓名<span style="color:red">*</span>:
						</td>
						<td>
							<input type="text" name="accountName" value="${accountName}" class="input" style="width: 250px;"/>
						</td>
					</tr>
					<tr>
						<td>
							银行类别<span style="color:red">*</span>:
						</td>
						<td>
							<input type="text" name="bank" value="${userbank.bankname}" class="input" style="width: 250px;"/>
						</td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">
							备注<span style="color:red">*</span>:
						</td>
						<td>
							<textarea name="remark" class="input"
								style="width: 300px; height: 100px;"></textarea>
						</td>
					</tr>
					<tr>
						<td align="center">
							<input type="submit" value="提交" />
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

