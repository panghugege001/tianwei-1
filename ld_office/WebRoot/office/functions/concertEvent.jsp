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
		<title>月流水晋级御龙VIP</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript">
	function gopage(val)
	{
		document.mainform.pageIndex.value=val;
		document.mainform.submit();
	}
	
	function updateYulongVipData(){

		var sel=$('#sel').val();
		$("#update_concert").attr("disabled",true);
		$.ajax({
			url:"${ctx}/office/updateConcertBetOnDay.do",
			type:"get",
			data:"id="+sel,
			timeout:0,
			success:function(data){
				alert(data);
				$("#update_concert").attr("disabled",false);
				$("#update_concert").val("更新流水数据");
			},
			error:function(){
				alert("操作超时导致结果未知,请稍后点击查询查看情况..");
			}
		});
		$("#update_concert").val("正在更新流水,请稍后.");
	}
	
	
	function display_y(id,ev){
		var me = ev;
		$.ajax({
			url:"${ctx}/office/updateConcertDisplay.do",
			type:"post",
			data:"id="+id,
			timeout:10000,
			success:function(data){
				if(data==0){	
					$(me).parent().prev().html("<font style='color: green;'>启用</font>");
					$(me).remove();
				
				} else {
					$(me).parent().prev().html("<font style='color: red;'>禁用</font>");
					$(me).remove();
				}
				
				 alert("修改成功");
			},
			error:function(){
				alert("操作超时导致结果未知,请稍后点击查询查看情况..");
			}
		});
	}
	
	function updateRankingData(){

		var sel=$('#sel').val();
		$("#ranking").attr("disabled",true);
		$.ajax({
			url:"${ctx}/office/updateRankingData.do",
			type:"get",
			data:{
				round:sel
			},
			timeout:0,
			success:function(data){
				alert(data);
			},
			error:function(){
				alert("操作超时导致结果未知,请稍后点击 流水排名..");
			}
		});
		$("#ranking").attr("disabled",false);
	}
	
	
</script>
	</head>
	<body>
		<p>
			统计 --&gt; 活动流水统计
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="queryConcertBet" namespace="/office" name="mainform" id="mainform" theme="simple">
			 <s:hidden name="pageIndex" value="1"/>
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td width="300">
										用户名：
										<input type="text" id="j-loginname" name="loginname" value=""/>
									</td>
								
									<td width="200">
										每页记录:
										<s:select cssStyle="width:90px" list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										<s:select cssStyle="width:90px" list="#{'5':'流水统计','2':'抢金子活动','1':'演唱会活动'}" name="type" id="type"></s:select>
									</td>
									<td>
										<s:submit cssStyle="width:65px; height:35px;" value="查询"></s:submit>
									</td>

								</tr>
								<tr align="left">
									<td>
									
										<input id="update_concert" type="button" value="更新流水数据" onclick="updateYulongVipData();"/>
									</td>
									<td>
									
										<input id="ranking" type="button" value="流水排名" onclick="updateRankingData();"/>
									</td>
										<td width="200">
										 查询/ 更新周期:
										<s:select cssStyle="width:90px" list="#{'1':'1','2':'2','3':'3','4':'4'}" name="id" id="sel"></s:select>
									</td>
									<td>
										<a href="/office/functions/addConcertRecord.jsp" target="_blank" id="add_yulongvip"><h3>新增数据</h3></a>
									</td>
                                    <td colspan="2">
										<h3><p style="color: red;">${errorMsg }</p></h3>
										<!--  <input type="button" onclick="submitAction(this);" value="系统洗码"/> -->
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<div id="middle" style="position: absolute; top: 155px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
						<table>
							<tr><td>
							<table width="880px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="80px">
										用户名
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="90px">
										开始时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="90px">
										结束时间
									</td>
										<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="90px">
										更新最后时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="90px">
										报名时间
									</td>
									<!-- <td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('endTime');" width="130px">
										结束时间
									</td> -->
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('bet');" width="100px">
										流水金额
									</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="50px">
										排名
									</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="50px">
										是否正式
									</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="50px">
										禁用启用状态
									</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="50px">
										操作
									</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" width="50px">
										流水修改
									</td>
<!-- 									<td align="center" -->
<!-- 										style="color: #FFFFFF; font-weight: bold; cursor: pointer;" -->
<!-- 										width="80px"> -->
<%-- 										<s:form action="yulongVipShow" namespace="/office" id="showform" theme="simple">控制 <input type="submit" value="全部显示"/></s:form> --%>
<!-- 									</td> -->
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
									<tr bgcolor="${bgcolor}">
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.startTime"/>
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:date name="#fc.endTime" format="yyyy-MM-dd HH:mm:ss" />
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:date name="#fc.lastTime" format="yyyy-MM-dd HH:mm:ss" />
										</td>
										
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											 <s:date name="#fc.createtime" format="yyyy-MM-dd HH:mm:ss" />
										</td>
										<%-- <td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc[3]"/>
										</td> --%>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.bet"/>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.ranking"/>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:if test='#fc.display==1'><font>1</font></s:if>
											<s:else><font>0</font></s:else>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:if test='#fc.active==1'><font style="color:red;">禁用</font></s:if>
											<s:else><font style="color: green;">启用</font></s:else>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">

											<input type="button" onclick="display_y(<s:property value='#fc.id'/>,this);" value="修改"/> 
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
										    <a href="/office/functions/editConcertRecord.jsp?id=<s:property value='#fc.id'/>"  target="_blank">修改流水</a>
										</td>
									</tr>

								</s:iterator>

						
								<tr>
									<td colspan="21" align="right" bgcolor="66b5ff" align="center">
										${page.jsPageCode}
									</td>
								</tr>
							</table>
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

