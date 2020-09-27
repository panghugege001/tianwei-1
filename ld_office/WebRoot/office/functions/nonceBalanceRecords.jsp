<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>实时额度记录</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>
<body>
<s:form action="nonceBalanceRecords" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
记录 --> 实时额度记录<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<font color="red"><s:fielderror></s:fielderror></font>
<div id="excel_menu">
<font color="red">[时间全部为北京时间]</font><br/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
会员帐号:<s:textfield name="loginname"></s:textfield>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易类型</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易前账户额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易后账户额度</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">交易时间</td>
            </tr>
			<c:set var="pageBetAmountSum" value="0" scope="request"></c:set>
			<c:set var="pagePayoffSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}" >
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.passport"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.TransType@getText(#fc.transType)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.transNo"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.srcAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.transAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.desAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.transTime"/></td>
            </tr>
            
            <s:set var="srcAmountValue" value="#fc.srcAmount" scope="request"></s:set>
            <s:set var="transAmountValue" value="#fc.transAmount" scope="request"></s:set>
            <s:set var="desAmountValue" value="#fc.desAmount" scope="request"></s:set>
            <c:set var="srcAmountValueSum" value="${srcAmountValueSum+srcAmountValue}" scope="request"></c:set>
            <c:set var="transAmountValueSum" value="${transAmountValueSum+transAmountValue}"  scope="request"></c:set>
            <c:set var="desAmountValueSum" value="${desAmountValueSum+desAmountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	   	 <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="3">当页总计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.srcAmountValueSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.transAmountValueSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.desAmountValueSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="2"></td>
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

