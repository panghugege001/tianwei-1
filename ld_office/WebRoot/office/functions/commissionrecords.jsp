<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>代理佣金明细查询</title>
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
<s:form action="queryCommissionrecords" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
结算 --> 代理佣金明细查询--><a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">

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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">年份</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">月份</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">代理账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">洗码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">首存优惠</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">AG/AGIN/BBIN/PT/KENO/KENO2/SB输赢</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">六合彩投注额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">其他优惠</td>
			  <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">备注</td>
            </tr>
            <s:set var="sumXimaAmount" value="0.0" scope="request" />
            <s:set var="sumFirstDepositAmount" value="0" scope="request" />
            <s:set var="sumOtherAmount" value="0" scope="request" />
            <s:set var="sumAgAmount" value="0" scope="request" />
            <s:set var="sumSixLotteryAmount" value="0" scope="request" />
            <s:iterator var="fc" value="%{#request.result}" >
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.id.year"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.id.month"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.parent"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.id.loginname"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.ximaAmount"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.firstDepositAmount"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.agAmount"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.sixLotteryBet"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.otherAmount"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
            </tr>
            <s:set var="sumXimaAmount" value="%{#request.sumXimaAmount+#fc.ximaAmount}" scope="request" />
            <s:set var="sumFirstDepositAmount" value="%{#request.sumFirstDepositAmount+#fc.firstDepositAmount}" scope="request" />
            <s:set var="sumOtherAmount" value="%{#request.sumOtherAmount+#fc.otherAmount}" scope="request" />
            <s:set var="sumAgAmount" value="%{#request.sumAgAmount+#fc.agAmount}" scope="request" />
            <s:set var="sumSixLotteryAmount" value="%{#request.sumSixLotteryAmount+#fc.sixLotteryBet}" scope="request" />
  	 	 </s:iterator>
  	 	   <!-- 当页小计 -->
              <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="4">当页小计</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumXimaAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumFirstDepositAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumAgAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumSixLotteryAmount)"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumOtherAmount)"/></td> 
            </tr>
            <!-- 2013 年7月改版，开始以新的模式计算佣金 -->
  	 	  <s:if test="%{#request.year >= 2014 || (#request.year >= 2013 && #request.month>6)}">
  	 	     <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="5">计算佣金公式：(扣掉平台费的平台输赢-洗码优惠-首存优惠-其他优惠)*${param.crate*100}% </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="4">(${param.eaProfitAmount}+<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumAgAmount)"/>-<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumXimaAmount)"/>-<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumFirstDepositAmount)"/>
              -<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumOtherAmount)"/>)*${param.crate*100}% &nbsp; = &nbsp;<font color="red">${(param.eaProfitAmount+sumAgAmount-sumXimaAmount-sumFirstDepositAmount-sumOtherAmount)*param.crate}</font></td>
            </tr>
          </s:if>
          <s:else>
          	<tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="5">计算佣金公式(EA报表利润+AG/BBIN/PT/KENO/SUN/SB输赢*30%-洗码优惠*30%-首存优惠*30%-其他优惠*30%)</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="4">${param.eaProfitAmount}+<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumAgAmount)"/>*30%-<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumXimaAmount)"/>*30%-<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumFirstDepositAmount)"/>*30%
              -<s:property value="@dfh.utils.NumericUtil@formatDouble(#request.sumOtherAmount)"/>*30% &nbsp; = &nbsp;<font color="red">${param.eaProfitAmount+sumAgAmount*0.3-sumXimaAmount*0.3-sumFirstDepositAmount*0.3-sumOtherAmount*0.3}</font></td>
            </tr>
          </s:else>
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

