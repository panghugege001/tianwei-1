<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>代理VIP</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.poshytip.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tip-skyblue/tip-skyblue.css" type="text/css" />
		
		<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}
function _executeAgentVip(btn){
   var result = confirm("确认?") ;
   if (result) {
   		btn.disabled=true;
     	var action = "/office/executeAgentVip.do";
		$.post(action , {} , function(data){
			btn.disabled=false;
			alert(data);
		});
  }
}
function _modifyAgentVip(btn , level){
	var result = new Array();
	var ids ;
	$("[name = item][checked]:checkbox").each(function(){
		result.push($(this).attr("value"));
	});
	var len = result.length ;
	if(len>0){
		if(confirm("共选中"+len+"条数据，确认执行？")){
			btn.disabled=true;
    		var ids = result.join(",") ;
    		$.post("/batchxima/executeAgentVip.do",{"ids":ids,"level":level},function(data){
    			btn.disabled=false;
    			alert(data);
    		});
    	}
	}else{
		alert("请选择需要执行的数据");
	}
}
$(function () {
    $("#checkAllBox").bind("click", function () {
    	if($(this).attr("checked") == "checked"){
        	$("[name = item]:checkbox").attr("checked", true);
    	}else{
    		$("[name = item]:checkbox").attr("checked", false);
    	}
    	_changeColor();
    });
    $("[name = item]:checkbox").bind("click", function () {
    	if($(this).attr("checked") != "checked"){
    		$("#checkAllBox").attr("checked", false);
    	}
    	var flag = true ;
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == undefined){
    			flag = false ;
    		}else{
    			flag = flag && $(this).attr("checked");
    		}
    	});
    	if(flag){
    		$("#checkAllBox").attr("checked", true);
    	}
    	_changeColor();
    });
    
    function _changeColor(){
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == "checked"){
    			$(this).parent().parent().find("td").css('background-color','rgb(226, 104, 104)');
    		}else{
    			$(this).parent().parent().find("td").css('background-color','white');
    		}
    	});
    }
    
});

</script>
	</head>
	<body>
		<p>
			记录 --&gt; 代理VIP
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getAgentVip" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										代理账号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									
									<td>
										起始时间:
										<s:textfield name="startTime"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{startTime}"/>
									</td>
									<td>
										新老代理：<s:select list="#{'':'','0':'新代理','1':'老代理'}" listKey="key" listValue="value" name="flag"></s:select>
										是否VIP：<s:select list="#{'':'','0':'普通','1':'VIP'}" listKey="key" listValue="value" name="level"></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>
								</tr>
								<tr align="left">
									<td>
									每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="endTime"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{endTime}"/>
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
                                    <td>
	                                    <c:if test="${sessionScope.operator.username eq 'daisy' || sessionScope.operator.username eq 'yan'}">
											<input type="button" value="提交" onclick="_executeAgentVip(this);"/>
											<input type="button" value="升级为代理VIP" onclick="_modifyAgentVip(this , 1);"/>
											<input type="button" value="降级为普通代理" onclick="_modifyAgentVip(this , 0);"/>
										</c:if>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</div>
		<br />
		<br />
		
		<br />
		<br />
		<div id="middle" style="position: absolute; top: 155px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1100px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td bgcolor="#0084ff" align="center">
										<input type="checkbox" id="checkAllBox" />
									</td>
									
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('id.agent');" width="80px">
										代理账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('id.createdate');" width="90px">
										数据日期
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('registertime');" width="130px">
										注册时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('activeuser');" width="130px">
										活跃会员数
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('activemonth');" width="130px">
										活跃时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('historyfee');" width="90px">
										历史佣金总额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('level');" width="90px">
										是否为代理VIP
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('createtime');" width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('remark');" width="130px">
										备注
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr bgcolor="${bgcolor}">
										<td align="center" >
												<input type="checkbox" name="item" value="<s:property value="#fc.id.agent" />@<s:property value="#fc.id.createdate" />" >
										</td>
										
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											${fc.id.agent}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.id.createdate}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.registertime}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.activeuser}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.activemonth}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.historyfee}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.level == 0">普通</s:if>
											<s:if test="#fc.level == 1">VIP</s:if>
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.createtime}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc.remark}
										</td>
									</tr>
									
								</s:iterator>

								<tr>
									<td colspan="21" align="right" bgcolor="66b5ff" align="center">
										${page.jsPageCode}
									</td>
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

