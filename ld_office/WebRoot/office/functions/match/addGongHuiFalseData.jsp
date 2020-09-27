<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>新增工会成员虚拟数据</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<s:head />
		<script type="text/javascript">
			function checkSlotsData(e){
				var rn = false;
				var win = document.getElementsByName("smWeekly.win")[0].value;
				if (isNaN(win) || win==''){
					alert('盈利金额必须填写整数!');
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
			操作 --> 新增工会成员虚拟数据
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<s:form action="addGongHuiFalseData" onsubmit="return checkSlotsData(this);"
				namespace="/office" theme="simple">
				<s:hidden name="mtype" value="insert"></s:hidden>
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
						<td>
							用户名:
						</td>
						<td>
							<s:textfield name="guildStaff.username" size="15" />
						</td>
					</tr>


					<tr>
						<td>
							选择工会:
						</td>
						<td>
							<s:select name="guildStaff.guildId" cssStyle="width:100px" list="#request.data" listKey="id"
									  listValue="name" ></s:select>
						</td>
					</tr>
					<tr>
						<td>
							数据更新时间:
						</td>
						<td>
							<s:textfield name="guildStaff.updatetime" size="15"
										 onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
										 cssClass="Wdate"></s:textfield>
						</td>
					</tr>


					<tr>
						<td>
							总存款金额:
						</td>
						<td>
							<s:textfield name="guildStaff.deposet" size="15" />
						</td>
					</tr>
					<tr>
						<td>
							总老虎机流水金额:
						</td>
						<td>
							<s:textfield name="guildStaff.gameAmount" size="15" />
						</td>
					</tr>
					<tr>
						<td>
							玩家加入时间:
						</td>
						<td>
							<s:textfield name="guildStaff.joinTime" size="15"
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

