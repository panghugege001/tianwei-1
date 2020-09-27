<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>投注记录</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>
<body>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}
</script>
<s:form action="queryBetRecord" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
记录 --> 投注记录<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<font color="red">[时间全部为北京时间]</font><br/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
会员帐号:<s:textfield name="loginname"></s:textfield>
游戏类型:<s:select name="gmCode" list="%{#application.GameType}" listKey="code" listValue="text" emptyOption="true"/>
玩法:<s:select name="playCode" list="%{#application.PlayCodeType}" listKey="code" listValue="text" emptyOption="true"/><br/>
游戏期数:<s:textfield name="drawNo"></s:textfield>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex" value="1"></s:hidden>
</div>
<br/><br/>

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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">游戏类型</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">玩法</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">游戏期数</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">投注金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">派彩</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">投注时间</td>
            </tr>
			<c:set var="pageBetAmountSum" value="0" scope="request"></c:set>
			<c:set var="pagePayoffSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}" >
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.passport"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.GameType@getText(#fc.gmCode)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.PlayCodeType@getText(#fc.playCode)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.drawNo"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.billAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.result)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.billTime"/></td>
            </tr>
            <s:set var="billAmountValue" value="#fc.billAmount" scope="request"></s:set>
            <s:set var="resultValue" value="#fc.result" scope="request"></s:set>
            <c:set var="billAmountValueSum" value="${billAmountValueSum+billAmountValue}" scope="request"></c:set>
            <c:set var="resultValueSum" value="${resultValueSum+resultValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	   	 <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="4">当页小计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.billAmountValueSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.resultValueSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2"></td>
            </tr>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="4">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics1}"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics2}"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2"></td>
            </tr>
            <tr>
              <td colspan="8" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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

