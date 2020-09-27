<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>输赢报表</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript">
  function reportinfo(){
   document.getElementById("mainform").action="<c:url value='/office/queryReportInfo.do'/>";
   document.getElementById("mainform").submit();
	  }
</script>
</head>
<body>

<div id="excel_menu_left">
各项报表 --> 输赢报表<a href="javascript:history.back();"> <font color="red">上一步</font></a>
</div>

<s:form action="queryReport" onsubmit="document.getElementById('sub_bottom').disabled=true;" namespace="/office" name="mainform" id="mainform" theme="simple">
<font color="red"><s:fielderror></s:fielderror></font>
<div id="excel_menu">
<font color="red">[时间全部为北京时间] [如果输入用户账号 用户类型将不起效]</font><br/>
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
用户账号:<s:textfield name="loginname"></s:textfield>
用户类型:<s:select list="#{'MONEY_CUSTOMER':'真钱会员','FREE_CUSTOMER':'试玩会员'}" name="roleCode"></s:select>
<s:submit value="查询" id="sub_bottom"></s:submit>
</div>
</s:form>
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
              <s:if test="#request.page.loginname!=null">
                <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员账号</td>
              </s:if>
              <s:else>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">参与投注人数</td>
              </s:else>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">总投注金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">有效投注额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">输赢金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">公司获利比例</td>
            </tr>
            <s:if test="#request.page!=null">
               <tr>
               <s:if test="#request.page.loginname!=null">
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#request.page.loginname"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.sumBillAmount)"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.sumFactAmount)"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.sumResult)"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#request.page.winPercent"/></td>
               </s:if>
               <s:else>
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;font-weight: bold;cursor: pointer;" title="点击查看详细" onclick="reportinfo();"><s:property value="#request.page.sumAttend"/></td>
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;font-weight: bold;cursor: pointer;" title="点击查看详细" onclick="reportinfo();"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.sumBillAmount)"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;font-weight: bold;cursor: pointer;" title="点击查看详细" onclick="reportinfo();"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.sumFactAmount)"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;font-weight: bold;cursor: pointer;" title="点击查看详细" onclick="reportinfo();"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.page.sumResult)"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;font-weight: bold;cursor: pointer;" title="点击查看详细" onclick="reportinfo();"><s:property value="#request.page.winPercent"/></td>
              </s:else>
            </tr>
            </s:if>
            
          </table>

          <s:if test="#request.reportlist!=null">
          <br/><br/>
           <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
           <tr>
            <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员账号</td>
            <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">总投注金额</td>
            <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">有效投注额</td>
            <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">输赢金额</td>
            <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">公司获利比例</td>
           </tr>
           
           <s:iterator value="#request.reportlist" var="report">
           <tr>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#report.loginname"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#report.sumBillAmount)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#report.sumFactAmount)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#report.sumResult)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#report.winPercent"/></td>
           </tr>
           </s:iterator>
           </table>
          </s:if>
          
	  </div>
	</div>
	</div>
  </div>
</div>

<c:import url="/office/script.jsp" />
</body>
</html>

