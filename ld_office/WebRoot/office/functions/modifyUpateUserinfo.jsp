<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="dfh.security.EncryptionUtil"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看用户基本信息</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
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
	</head>
	<body>
		<div id="excel_menu_left">
			操作 --&gt; 更新用户优惠
		</div>
		<br/>
		<s:form action="updateRebate" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<table align="left" border="0">
				<tr>
					<td style="text-align: right;">
						当前会员账号:
					</td>
					<td>
						<input type="hidden" name="loginname" class="input"
							value="<s:property value="%{#request.user.loginname}"/>" />
							<s:property value="%{#request.user.loginname}"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						EA:
					</td>
					<td>
						<input name="earebate" type="text" class="input"
							value="<s:property value="%{#request.user.earebate}"/>" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						BBIN:
					</td>
					<td>
						<input name="bbinrebate" type="text" class="input"
							value="<s:property value="%{#request.user.bbinrebate}"/>" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						AG:
					</td>
					<td>
						<input name="agrebate" type="text" class="input"
							value="<s:property value="%{#request.user.agrebate}"/>" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						AGIN:
					</td>
					<td>
						<input name="aginrebate" type="text" class="input"
							value="<s:property value="%{#request.user.aginrebate}"/>" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						KENO:
					</td>
					<td>
						<input name="kenorebate" type="text" class="input"
							value="<s:property value="%{#request.user.kenorebate}"/>" />
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						SB:
					</td>
					<td>
						<input name="sbrebate" type="text" class="input"
							value="<s:property value="%{#request.user.sbrebate}"/>" />
					</td>
				</tr>
				<!-- 
				<tr>
					<td style="text-align: right;">
						PT:
					</td>
					<td>
						<input name="ptrebate" type="text" class="input"
							value="<s:property value="%{#request.user.ptrebate}"/>" />
					</td>
				</tr> -->
				<tr>
					<td>
					</td>
					<td>
						<s:submit value="提交"></s:submit>
					</td>
				</tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
