<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>实时转账记录</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>
<body>
<s:form action="queryRemoteTransfer" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
账户 --> 实时转账记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<font color="red"><s:fielderror></s:fielderror></font>
<div id="excel_menu">
转入/转出:<s:select name="isTransferIn" list="#{0:'转入',1:'转出'}"  emptyOption="true"/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
帐号:<s:textfield name="loginname"></s:textfield>
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
          	  <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">转账编号</td>
          	  <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">转账类型</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">帐号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">额度变量</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">加入时间</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr>
             <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.billNo"/></td>
             <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:if test="#fc.isTransferIn">转入</s:if><s:else>转出</s:else></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.passport"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
               <s:if test="#fc.isTransferIn">
               <s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/>
                    </s:if>
                    <s:else>
                    -<s:property value="(@dfh.utils.NumericUtil@formatDouble(#fc.amount))"/>
                    </s:else>
               </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.billTime"/></td>
              <s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
             <s:if test="#fc.isTransferIn">
               <c:set var="amountValueSum" value="${amountValueSum+amountValue}"  scope="request"></c:set>
             </s:if>
             <s:else>
              <c:set var="amountValueSum" value="${amountValueSum-amountValue}"  scope="request"></c:set>
             </s:else>
            </tr>
  	 	 </s:iterator>
  	 	  <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="3">当页总计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountValueSum)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="1"></td>
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

