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
<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.2.js"></script>
<script type="text/javascript">
  function agentinfoAnalysis(){
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

 function lick(loginname){
	 var href = "&countProxyFirst_start="+$("#countProxyFirst_start").val()+"&countProxyFirst_end="+$("#countProxyFirst_end").val();
	 window.open("/office/getUserhavinginfoSCSH.do?loginname="+loginname+href,"_blank");

 }

 $(function(){
	var countProxyFirst_start = $("#countProxyFirst_start");
	var countProxyFirst_end = $("#countProxyFirst_end");
	if(countProxyFirst_start.val() == null || countProxyFirst_start.val() == ""){
		$("#countProxyFirst_start").val($("#_startTime").val());
	}
	if(countProxyFirst_end.val() == null || countProxyFirst_end.val() == ""){
		$("#countProxyFirst_end").val($("#_endTime").val());
	}
 });



</script>
<style>
 /* .table-head{padding-right:17px;background-color:#999;color:#000;} */
 .table-body{width:100%; height:1000px;overflow-y:scroll;}
 .table-head table , .table-body table{width:100%;}
 .table-body table tr:nth-child(2n+1){background-color:#f2f2f2;}
</style>
</head>
<body>

<div id="excel_menu_left">
代理分析 --> 代理信息分析<a href="javascript:history.back();"> <font color="red">上一步</font></a>
</div>


<s:form action="queryAgentInfoAnalysis" onsubmit="return agentinfoAnalysis();" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<font color="red"><s:fielderror></s:fielderror></font>
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1280px">
								<tr align="left">
									<td>
										代理账号:<s:textfield name="loginname" ></s:textfield>
									</td>
									<td>
									时间间隔(天):<s:select name="intvalday" emptyOption="true" list="#{'7':'一周内','14':'两周内','31':'一个月内'}"  ></s:select>
									</td>
									<td>
										起始时间:
										<s:textfield id="_startTime" name="stringStartTime" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
									</td>
									<td>
										代理注册起始时间:
										<s:textfield id="_startDate" name="startDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{startDate}"/>
									</td>
									<td>未登录间隔(天):<s:select name="nintvalday" emptyOption="true" list="#{'31':'一个月以上','58':'两个月以上','88':'三个月以上'}"  ></s:select></td>
									<td>首存用户起始时间：<s:textfield id="countProxyFirst_start" onchange="changeTime()" name="countProxyFirst_start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{countProxyFirst_start}" /></td>
									<td></td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>
								</tr>
								<tr align="left">
                                    <td>
                                    	代理推荐码:<s:textfield name="partner" cssStyle="width:60px;"></s:textfield>
									</td>
									<td>
										代理类型:<s:select name="agentType" list="#{-1:'代理',1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="true" ></s:select>
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
										<s:textfield id="_endDate" name="endDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{endDate}"/>
									</td>
									<td>警告等级:<s:select list="%{#application.WarnLevel}" listKey="code" listValue="text" name="warnflag" emptyOption="true"></s:select></td>
									<td>首存用户结束时间：<s:textfield id="countProxyFirst_end" onchange="changeTime()" name="countProxyFirst_end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{countProxyFirst_end}" /></td>b
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
		  <table border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
            	<th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 3%"></th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 7%">代理账号</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 3%">警告级别</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 7%">注册时间</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 7%">活跃会员量</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">代理推荐码</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">代理类型</th>
               <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">会员注册量</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">存款人数</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">有效代理</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">投注额</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">游戏输赢</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">存款额度</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">提款额度</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">好友推荐金</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">总佣金</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">老虎机佣金</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;width: 5%">真人佣金</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;;width: 5%" title="点击排序" onclick="orderby('intvalday');">时间间隔(天)</th>
              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;;width: 5%" title="点击排序" onclick="orderby('profitall');">盈利额</th>
            </tr>
           <s:iterator value="#request.page" var="agentAnalysis" status="ps">
           <tr>
           	<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 3%"><s:property value="#ps.index+1" /></td>

            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;;width: 7%"><a name="hrefs" href="#" onclick="lick('<s:property value="#agentAnalysis.loginname"/>')" ><s:property value="#agentAnalysis.loginname"/></a></td>
            <td
            	<c:choose>
            		<c:when test="${agentAnalysis.warnflag eq 0}">bgcolor="#e4f2ff"</c:when>
            		<c:when test="${agentAnalysis.warnflag eq 1}">bgcolor="#f3deac"</c:when>
            		<c:when test="${agentAnalysis.warnflag eq 2}">bgcolor="#fb8d8d"</c:when>
            		<c:otherwise>bgcolor="#9beda3"</c:otherwise>
            	</c:choose>
            	align="center"  style="font-size:13px;width: 3%">
            <s:property value="@dfh.model.enums.WarnLevel@getText(#agentAnalysis.warnflag)"/>
            </td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;;width: 7%"><s:property value="#agentAnalysis.createtime"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="#agentAnalysis.agentActiveCount"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="#agentAnalysis.partner"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%">
            <c:choose>
					<c:when test="${agentAnalysis.agentType eq 1}">SEO</c:when>
					<c:when test="${agentAnalysis.agentType eq 2}">电销</c:when>
					<c:when test="${agentAnalysis.agentType eq 3}">推广</c:when>
					<c:when test="${agentAnalysis.agentType eq 4}">广告</c:when>
			</c:choose>
			</td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="#agentAnalysis.regnum"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="#agentAnalysis.depnum"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%">
	           	<c:choose>
	           		<c:when test="${agentAnalysis.isValid ne 1}"></c:when>
					<c:when test="${agentAnalysis.isValid eq 1}">有效</c:when>
	           	</c:choose>
           	</td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.betall)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.agentprofitall)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.depositall)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.drawall)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.agentFriendBonus)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.agentdrwalall)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.slotaccount)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.credit)"/></td>
            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;width: 5%"><s:property value="#agentAnalysis.day"/></td>
            <td <s:if test="#agentAnalysis.profitAllAmount>=100000">bgcolor="red"</s:if><s:else>bgcolor="#e4f2ff"</s:else> align="center"  style="font-size:13px;width: 5%">
            	<s:property value="@dfh.utils.NumericUtil@formatDouble(#agentAnalysis.profitall)"/>
            </td>
           </tr>
           </s:iterator>
           <s:set value="#request.pageSum" var="pgSum"></s:set>
           <tr bgcolor="#e4f2ff">
              <td  align="right"   colspan="7">总计:</td>
               <td  align="right"  >注册人数：<s:property value="#pgSum[0]"/></td>
               <td  align="right"  >存款人数：<s:property value="#pgSum[1]"/></td>
               <td  align="right"  >有效代理数：<s:property value="#pgSum[9]"/></td>
               <td  align="right"  >投注额：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[2])"/></td>
              <td  align="right"  >游戏输赢：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[3])"/></td>
              <td  align="right"  >存款数：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[4])"/></td>
              <td  align="right"  >提款数：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[5])"/></td>
              <td  align="right"  >好友推荐金总数：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[8])"/></td>
              <td  align="right"  >历史佣金：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[6])"/></td>
              <td align="center"   colspan="3"></td>
              <td  align="right"  >输赢总数：<s:property value="@dfh.utils.NumericUtil@double2String(#pgSum[7])"/></td>
            </tr>
           </table>
           </div>
           </div>
	  </div>
	</div>
	</div>

<c:import url="/office/script.jsp" />
</body>
</html>

