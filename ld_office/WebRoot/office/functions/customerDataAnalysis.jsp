<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>输赢信息分析</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
  function customerinfoAnalysis(){
  	if(""==document.getElementById('_startTime').value|| ""==document.getElementById('_endTime').value){
  		alert("时间不允许为空！");
  		return false;
  	}
	document.getElementById('sub_bottom').disabled=true;
	return true;
 }
 function editUserIntro(_loginname){
 	var value = window.prompt("修改用户【 "+_loginname+" 】推荐码！","");
 	var action="/office/editUser.do";
 	if(value){
 		var xmlhttp = new Ajax.Request(    
					action,
			        {    
			            method: 'post',
			            parameters:"loginname="+_loginname+"&intro="+value+"&r="+Math.random(),
			            onComplete: responseMethod  
			        }
		    	);
 	}else{
 		alert("参数不允许为空!");
 	}
 }
 
 function responseMethod(data){
	alert(data.responseText);
	document.getElementById("mainform").submit();
 }
 
 function orderby(by){
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}
</script>
</head>
<body>

<div id="excel_menu_left">
客户分析 --> 输赢信息分析<a href="javascript:history.back();"> <font color="red">上一步</font></a>
</div>

<s:form action="queryCustomerInfoAnalysisNew" onsubmit="return customerinfoAnalysis();" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<font color="red"><s:fielderror></s:fielderror></font>
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1280px">
								<tr align="left">
									<td>
										用户账号:<s:textfield name="loginname" ></s:textfield>
									</td>
									<td>
										会员等级:<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/>
									</td>
									<td>
										起始时间:
										<s:textfield id="_startTime" name="stringStartTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
									</td>
									<td>
										代理注册起始时间:
										<s:textfield name="startDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{startDate}"/>
									</td>
									<td>
										代理账号:<s:textfield name="agent" ></s:textfield>
									</td>
									<td>未登录间隔(天):<s:select name="nintvalday" emptyOption="true" list="#{'31':'一个月以上','58':'两个月以上','88':'三个月以上'}"  ></s:select></td>
									<td>
										代理类型:<s:select name="agentType" list="#{-1:'代理',1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="true"></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>
								</tr>
								<tr align="left">
									<td>
										推荐码:<s:textfield name="intro" ></s:textfield>
									</td>
                                    <td>
                                    	代理推荐码:<s:textfield name="partner" cssStyle="width:60px;"></s:textfield>
									</td>
									<td>
										结束时间:
										<s:textfield id="_endTime" name="stringEndTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
										<s:set name="by" value="'profit'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}"/>
										<s:hidden name="by" value="%{by}"/>
									</td>
									<td>
										代理注册结束时间:
										<s:textfield name="endDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{endDate}"/>
									</td>
									<td>警告等级:<s:select list="%{#application.WarnLevel}" listKey="code" listValue="text" name="warnflag" emptyOption="true"></s:select></td>
									<td>
									时间间隔(天):<s:select name="intvalday" emptyOption="true" list="#{'7':'一周内','14':'两周内','31':'一个月内'}"  ></s:select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">警告级别</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员等级</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">上级代理</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">推荐码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('tdeposit');">存款总额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('netpaymoney');">在线支付总额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('withdraw');">提款总额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">好友推荐金</td>
              
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">用户注册时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">用户最后登录时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('intvalday');">时间间隔(天)</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginTimes');">用户登录次数</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('profit');">盈利额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('profitall');">总盈利额</td>
            </tr>

           <s:iterator value="#request.page" var="customerAnalysis" status="ps">
           <tr>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#ps.index+1" /></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a target="_blank" href='/office/getUserhavinginfo.do?loginname=<s:property value="#customerAnalysis.loginname"/>'><s:property value="#customerAnalysis.loginname"/></a></td>
            <td 
            	<c:choose>
            		<c:when test="${customerAnalysis.warnflag eq 0}">bgcolor="#e4f2ff"</c:when>
            		<c:when test="${customerAnalysis.warnflag eq 1}">bgcolor="#f3deac"</c:when>
            		<c:when test="${customerAnalysis.warnflag eq 2}">bgcolor="#fb8d8d"</c:when>
            		<c:otherwise>bgcolor="#9beda3"</c:otherwise>
            	</c:choose>
            	align="center"  style="font-size:13px;">
            <s:property value="@dfh.model.enums.WarnLevel@getText(#customerAnalysis.warnflag)"/>
            </td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
				<s:property value="@dfh.model.enums.VipLevel@getText(#customerAnalysis.level)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#customerAnalysis.agent"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
            
            <c:if test="${sessionScope.operator.authority eq 'boss'}"><a href="#" onclick="editUserIntro('<s:property value="#customerAnalysis.loginname"/>');"/></c:if>
            <s:if test="#customerAnalysis.intro !=null && #customerAnalysis.intro!=''"><s:property value="#customerAnalysis.intro"/></s:if>
            <s:else>无</s:else>
            </td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis.depositAmount)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis.netpayAmount)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis.withdrawAmount)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis.friendBonus)"/></td>
            
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#customerAnalysis.regTime"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#customerAnalysis.lastLoginTime"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#customerAnalysis.day"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#customerAnalysis.loginTimes"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#customerAnalysis.profileAmount)"/></td>
            <td <s:if test="#customerAnalysis.profitAllAmount>=100000">bgcolor="red"</s:if><s:else>bgcolor="#e4f2ff"</s:else> align="center"  style="font-size:13px;">
            		<s:property value="#customerAnalysis.profitAllAmount"/>
            </td>
           </tr>
           </s:iterator>
           <s:set value="#request.pageSum" var="pgSum"></s:set>
           <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager' || (#request.loginname != null && #request.loginname != '')">
           <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="6">总计:</td>
               <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[0])"/></td>
               <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[1])"/></td>
               <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[2])"/></td>
               <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[5])"/></td>
              <td align="center"   colspan="4"></td>
              <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[3])"/></td>
              <td  align="right"  ><s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[4])"/></td>
            </tr>
            </s:if>
           </table>
	  </div>
	</div>
	</div>
  </div>
</div>

<c:import url="/office/script.jsp" />
</body>
</html>

