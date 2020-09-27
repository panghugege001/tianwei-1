<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>新增流水假数据</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
			
		<s:head />
		<script type="text/javascript">
			function checkConcertData(e){
				var rn = false;
				var bet = $('#fee').val();
				var loginname = $('#checkLoginname').val();
				if (isNaN(bet)||bet==""){
					alert("流水金额必须填写数字!");
					return false;
				}else if (loginname==""){
						alert("用户名必须填写!");
						return false;
				} else {
					rn = window.confirm("确认数据都已输入并提交?");
				}
				return rn;
			}		
		</script>
	</head>
	<body style="background: #b6d9e4; font-size: 11px">
		<div id="excel_menu_left">
			操作 --> 新增 演唱会活动流水
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<s:form action="modifiedConcertBet" onsubmit="return checkConcertData(this);" namespace="/office" theme="simple">
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
							<font color="blue">以下填写内容，请确保全部填写</font>
						</td>
					</tr>
					<tr>
						<td>
							账号:
						</td>
						<td>
							<s:textfield name="loginname" size="15" id="checkLoginname" value=""/>
						</td>
					</tr>
					<tr>
						<td>
							流水金额:
						</td>
						<td>
							<s:textfield name="fee" size="15"  id="fee"/>
						</td>
					</tr>
					<tr>
						<td>
							更新周期:
						</td>
						<td>
						   <s:select cssStyle="width:90px" list="#{'1':'1','2':'2','3':'3','4':'4'}" name="id" id="sel"></s:select>
						</td>
					</tr>
					<tr>
						<td>
						 更新时间:
						</td>
						<td>
							<s:textfield name="createdate" size="15"
								value="%{@dfh.utils.MatchDateUtil@lastDayOfMonth()}"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								cssClass="Wdate"></s:textfield>
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

