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
	return true;
}
</script>
<s:form action="mgPlayCheck" namespace="/office" name="mainform" id="mainform" theme="simple" onsubmit="return checkFormItem()">
<div id="excel_menu_left">
记录 --> MG输赢统计 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align: center;">
开始时间: <s:textfield name="stringStartTime" size="20" onfocus="WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{stringStartTime}" id="startTimeInput"/>
结束时间:<s:textfield name="stringEndTime" size="20" onfocus="WdatePicker({startDate:'%y-%M-{%d+1} 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{stringEndTime}" id="endTimeInput"/>
帐号:<s:textfield name="loginname" size="20" id="loginnameInput"></s:textfield>
<s:submit value="查询"></s:submit>
</div>

<br/>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">投注</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">退回</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">盈利</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">累计奖池</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">玩家总盈利</td>
              <td bgcolor="#0084ff" align="center" style="font-size: 13px;; color: #FFFFFF; font-weight: bold;">操作</td>
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="item" value="%{#request.playList}">
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#item.loginname"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#item.bet)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#item.refund)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#item.win)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#item.progressivewin)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#item.net)"/></td>
              <td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
			    <a href="${ctx}/office/queryMGGameDetail.do?loginname=${item.loginname}&start=${startTime}&end=${endTime}&size=20" target="_blank">投注详情</a>
			  </td>
            </tr>
            <c:set var="betSum" value="${betSum + item.bet}" scope="request"></c:set>
            <c:set var="netSum" value="${netSum + item.net}" scope="request"></c:set>
  	 	 </s:iterator>
  	 	 <tr>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">合计 </td>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
  	 	  	  <s:property value="@dfh.utils.NumericUtil@double2String(#request.betSum)" /> 
			 </td>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"> </td>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"> </td>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"> </td>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
  	 	  	  <s:property value="@dfh.utils.NumericUtil@double2String(#request.netSum)" /> 
  	 	  	  </td>
  	 	  	 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"> </td>
  	 	  </tr>
  	 	 <tr>
  	 	  	 <td colspan="7" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">共  ${fn:length(playList)} 条     </td>
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

