<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>MG输赢统计</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
</head>
<body>
<script type="text/javascript">
function checkFormItem(){
	if($.trim($('#startTimeInput').val()) == '') {alert('请选择开始时间');return false;}
	if($.trim($('#endTimeInput').val()) == '') {alert('请选择结束时间');return false;}
	if($.trim($('#loginnameInput').val()) == '') {alert('请填写帐号');return false;}
	if(isNaN($('#gameidInput').val())){alert('投注局号需为数字');return false;}
	return true;
}
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}
</script>
<s:form action="queryMGGameDetail" namespace="/office" name="mainform" id="mainform" theme="simple" onsubmit="return checkFormItem()">
<div id="excel_menu_left">
风险控制 --> 实时输赢记录-->MG详细投注内容
</div>
<div id="excel_menu" style="text-align: center;">
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" id="startTimeInput"/>
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" id="endTimeInput"/>
投注局号:<s:textfield name="gameid" size="20" id="gameidInput"></s:textfield>
帐号:<s:textfield name="loginname" size="20" id="loginnameInput"></s:textfield>
每页记录:<s:select cssStyle="width:90px" list="%{#application.PageSizes}" name="size"></s:select>
<s:hidden name="pageIndex" />
<s:submit value="查询"></s:submit>
</div>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">帐号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">中奖信息</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">投注局号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">投注时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">金额(单位：分)</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">余额(单位：分)</td>
            </tr>
            <s:iterator var="item" value="%{#request.page.pageContents}">
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#item.loginname"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#item.actionType"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#item.gameid"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#item.actiontime" /></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@@abs(#item.amount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@@abs(#item.balance)"/></td>
            </tr>
  	 	 </s:iterator>
			<tr>
				<td colspan="6" align="right" bgcolor="66b5ff" align="center">
					${page.jsPageCode}
				</td>
			</tr>
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
</s:form>
<c:import url="/office/script.jsp" />
</body>
</html>

