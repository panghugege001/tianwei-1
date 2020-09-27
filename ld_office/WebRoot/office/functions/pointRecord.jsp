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
		<title>积分记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
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


function responseMethod(data){
	alert(data.responseText);
}

function clearErrortimes(loginname){
	if(confirm("是否确定重置？")){
		$.post("/office/clearErrorTimes.do",{"loginname":loginname},function(data){
			alert(data);
		});
	}
}
		

//派发昨日积分
function payYesterdayPoint(){
	  var choice=confirm("您确认要派发吗？", function() { }, null);
	  var yesterday =$("#today").val();
	 if(choice){
	  $.ajax({
		  url:"/office/payYesterdayPoint.do",
		  type:"post",
		  dataType:"text",
		 // data:"id="+ids,
		  data:{'yesterday':yesterday},
		  async:false,
		  success : function(msg){
			  alert(msg);
		  }
		  
	  })
}
}

</script>
	</head>
	<body>
		<p>
			--&gt; 积分记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getPointRecords" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1450px">
								<tr align="left">
									<td>
										玩家帐号:
										<s:textfield cssStyle="width:100px" name="username" size="20" />
									</td>
									<td align="left" width="60px">
														开始时间:
													</td>
													<td width="60px" align="left">
														<s:textfield name="startTime" size="18" 
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														结束时间:
													</td>
													<td align="left">
														<s:textfield name="endTime" size="18" cssStyle="width:150px" 
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										</td>
									<td colspan="2">
										每页记录:
										<s:select cssStyle="width:60px"
											list="%{#application.PageSizes}" name="size"></s:select>
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td colspan="1">
										类别:
										<s:select cssStyle="width:60px" list="#{'':'全部',0:'积分收入',1:'积分支出',2:'积分总余额'}" key="key"   value="%{type}"
								 name='type'></s:select>
									</td>
									<td colspan="1">
										游戏平台:
										<s:select cssStyle="width:60px" list="#{'':'全部','ttg':'ttg','pttiger':'pttiger','ptother':'ptother','nt':'nt','qt':'qt','ea':'ea','ebet':'ebet','ag':'ag','agin':'agin','bbin':'bbin'}" key="key"   value="%{platform}"
								 name='platform'></s:select>
									</td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:22px;" value="查询"></s:submit>
									</td>
									<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='sale_manager' || #session.operator.authority=='finance' " >
									<td>
									<s:textfield name="today" id="today" size="8" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="true" value="%{today}" cssClass="Wdate" />
									</td>
									<td rowspan="2">
										<button onclick="payYesterdayPoint()">派发所选日期前一天积分</button>
									</td>
									</s:if>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
		</div>
		<br />
		<br />
		<div id="middle" style="position: absolute; top: 145px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1310px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('username');" width="130px">
										玩家账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										更新时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('type');" width="130px">
										类别
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('type');" width="130px">
										游戏平台
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('sumamount');" width="130px">
										金额(元)
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										t width="130px">
										积分值（可用）
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										t width="130px">
										历史总积分值
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										t width="180px">
										备注
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr >
										<td align="center" width="60px">
											<s:property value="#fc.username" />
										</td>
										<td align="center" width="90px">
										<s:if test="#fc.type!=2">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
										</s:if>
										<s:else>
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.updatetime" />
										</s:else>
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.type==0">
												积分收入
											</s:if>
											<s:elseif test="#fc.type==1">
												积分支出
											</s:elseif>
											<s:else>
												积分总余额
											</s:else>
										</td>
										<td align="center" width="30px">
											<s:property value="#fc.platform" />
										</td>
										<td align="center" width="30px">
											<s:property value="#fc.sumamount" />
										</td>
										<td align="center" width="30px">
										<s:if test="#fc.type!=2">
											<s:property value="#fc.points" />
										</s:if>
										<s:else>
										<s:property value="#fc.totalpoints" />
										</s:else>
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.oldtotalpoints" />
										</td>
										
										<td align="center" width="60px">
											<s:property value="#fc.remark" />
										</td>
									</tr>
								</s:iterator>
									<%-- <tr>
										<td bgcolor="#e4f2ff" align="right" colspan="3">
											合计:
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="1">
											金额：
											<s:property
												value="@dfh.utils.NumericUtil@double2String((#request.page.statics1))" />
										</td>
										<td bgcolor="#e4f2ff" align="center" colspan="2">
											积分值：
											<s:property
												value="@dfh.utils.NumericUtil@double2String((#request.page.statics2))" />
										</td>
										<td bgcolor="#e4f2ff" align="center" >
										</td>
									</tr> --%>
								<tr>
									<td colspan="8" align="right" bgcolor="66b5ff" align="center">
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

