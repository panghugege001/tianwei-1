<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
		<title>内部代理记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
		

		$(function () {
		    $("#checkAllBox").bind("click", function () {
		    	if($(this).attr("checked") == "checked" || $(this).attr("checked") == true){
		        	$("[name = item]:checkbox").attr("checked", true);
		    	}else{
		    		$("[name = item]:checkbox").attr("checked", false);
		    	}
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
		    });
		});

		function band(btn){
			var belongto = $("#belongto").val();
			if(belongto == ''){
				alert("请输入所属用户");
				return;
			}
			var result = new Array();
			var ids ;
			$("[name = item][checked]:checkbox").each(function(){
				result.push($(this).attr("value"));
			});
			var len = result.length ;
			if(len>0){
				if(confirm("共选中"+len+"条数据，确认执行？")){
					btn.disabled=true;
			   		var ids = "";
			   		for(var item in result) {
			   			ids = ids+"'"+result[item]+"',";
			   		}
			   		
			   		$.post("/office/batchUpdate.do",{"ids":ids,"belongto":belongto},function(data){
			   			btn.disabled=false;
			   			alert(data);
			   			var frm=document.getElementById("mainform");
						 frm.submit();
			   		});
				}
			}else{
				alert("请选择需要执行的数据");
			}
		}
		
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


function responseMethod(data){
	alert(data.responseText);
}

function saveAgentAddress(){
	if(confirm("确认添加？")){
		var data = $("#_saveAgentAddressForm").serialize();
		console.log(data);
		$.post("/office/addAgentAddress.do?"+data,function(data){
			alert(data);
		});
	}
}

function modifyAddress(btn,oldaddress){
	btn.disabled=true;
	var address=window.prompt("您是否要提交，请填写新代理地址,否则请点取消:","");
	if(address){
		 $.post("/office/modifyAgentSite.do",{"address":address,"oldaddress":oldaddress},function(data){
			 alert(data);
		 });

	}else{
		btn.disabled=false;
	}	
}
</script>
	</head>
	<body>
		<p>
			--&gt; 内部代理记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getInternalAgencies" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										开始时间:
										<s:textfield name="startTime" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{startTime}" cssClass="Wdate" />
									</td>
									<td>
										代理帐号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td colspan="2">
										每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>
								</tr>
								<tr align="left">
									<td>
										结束时间:
										<s:textfield name="endTime" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td>
										代理类型:<s:select name="agentType" list="#{1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="false"></s:select>
									</td>
									<td>
										所属用户:<s:textfield cssStyle="width:90px" id="belongto" name="belongto" size="32" />
										<input type="button" value="批量绑定" onclick="band(this)"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<br />
		<div id="middle" style="position: absolute; top: 200px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="910px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td bgcolor="#0084ff" align="center"  style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="60px"><input type="checkbox" id="checkAllBox" /></td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
										所属用户
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
										代理账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
										状态
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
										类型
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										创建时间
									</td>
									<!-- <td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="130px">
										操作
									</td> -->
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									
									
									<tr >
										<s:if test="#fc[4]=='' ||  #fc[4]==null">
							            	<td align="center">
												<input type="checkbox" name="item" value="<s:property value="#fc[0]"/>" />
					            			</td>
										</s:if>
										<s:else> 
											<td>
							            	</td>
										</s:else>
										<td align="center" width="90px">
											<s:property value="#fc[4]"/>
										</td>
										<td align="center" width="60px">
											<a href="/office/getUserhavinginfo.do?loginname=${fc[0]}" target="_blank">${fc[0]}</a>
										</td>
										<td align="center" width="90px">
											<s:if test="#fc[3]==0">启用</s:if>
											<s:if test="#fc[3]==1">禁用</s:if>
										</td>
										<td align="center" width="90px">
											<s:if test="#fc[1]==1">SEO</s:if>
											<s:if test="#fc[1]==2">电销</s:if>
											<s:if test="#fc[1]==3">推广</s:if>
											<s:if test="#fc[1]==4">广告</s:if>
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[2]" />
										</td>
										<%-- <td>
											<input type="button" value="修改" onclick="modifyAddress(this , '<s:property value="#fc.address" />')"/>
										</td> --%>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="7" align="right" bgcolor="66b5ff" align="center">
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

