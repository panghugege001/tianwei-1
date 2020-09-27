<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>户内转账补单</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<s:head />
	</head>
	<body style="background: #b6d9e4; font-size: 11px">
		<div id="excel_menu_left">
			操作 --> 户内转账补单
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<s:form action="repairGameOrder" onsubmit="submitonce(this);"
				namespace="/office" theme="simple">
				<table align="left">
					<tr>
						<td colspan="2">
							<s:fielderror />
						</td>
					</tr>
					<tr>
						<td colspan="2" height="10px">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<font color="blue">以下填写内容，请保持与游戏转账单号一致；备注可为空</font>
						</td>
					</tr>
					<tr>
						<td>
							会员账号:
						</td>
						<td>
							<s:textfield name="loginname" size="35" />
						</td>
					</tr>
					<tr>
						<td>
							转账单号:
						</td>
						<td>
							<s:textfield name="billno" size="35" />
						</td>
					</tr>
					<tr>
						<td>
							转账时间:
						</td>
						<td>
							<s:textfield name="payTime" size="35"
								value="%{@dfh.utils.DateUtil@getNow()}"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								cssClass="Wdate"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							转账平台:
						</td>
						<td>
							<s:select cssStyle="width:150px" list="#{'ea':'ea','ag':'ag','agin':'agin','bbin':'bbin','keno2':'keno2','sb':'sb','pt':'pt','ebet':'ebet','ttg':'ttg','jc':'ssc','nt':'nt','qt':'qt','dt':'dt','mg':'mg','ebetapp':'ebetapp','n2live':'n2live','png':'png','mwg':'mwg','slot':'slot','红包雨':'redrain','761':'761','bbin':'bbin','pb':'pb体育','bit':'bit游戏','kyqp':'开元棋牌','vr':'vr彩票'}" key="key"
								value="value" name="payPlatform"></s:select>
						</td>
					</tr>
					<tr>
						<td align="right">
							金 额:
						</td>
						<td>
							<s:textfield name="payAmount" size="35" />
						</td>
					</tr>
					<tr>
						<td align="right" valign="top">
							备 注:
						</td>
						<td>
							<s:textarea name="remark" cols="28" rows="5" />
						</td>
					</tr>
					<tr>
						<td align="center" colspan="2">
							<s:submit value="提  交" />
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

