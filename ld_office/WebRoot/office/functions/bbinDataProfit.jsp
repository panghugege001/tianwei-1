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
		<title>BBIN平台游戏记录</title>
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
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
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
function submitAction(btn){
   btn.disabled=true;
   var result = confirm("你确定要提交BBIN系统洗码?")
   if (result) {
     	 var action = "/batchxima/addBbinXimaProposal.do";
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    );
  }
}

function responseMethod(data){
	alert(data.responseText);
}
</script>
	</head>
	<body>
		<p>
			记录 --&gt; 龙都平台游戏记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getBbinList" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">

					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									<td>
										龙都真人帐号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td>
										开始时间:
										<s:textfield name="startTime" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="%{startTime}" cssClass="Wdate" />
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

									</td>
									<td>
										结束时间:
										<s:textfield name="endTime" id="end" size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
											My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'day'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
										
									</td>
                                    <td>
										<input type="button" onclick="submitAction(this);" value="系统洗码"/>
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
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" width="80px">
										BBIN平台账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('dl');" width="90px">
										单量
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('sy');" width="90px">
										输赢
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('tze');" width="90px">
										投注额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('sjtz');" width="90px">
										实际投注
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('hy');" width="90px">
										会员
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('dls');" width="130px">
										代理商
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('startday');" width="130px">
										开始时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('endday');" width="130px">
										结束时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('day');" width="130px">
										创建时间
									</td>
								</tr>
								<c:set var="ztzeSum" value="0" scope="request"></c:set>
								<c:set var="zsySum" value="0" scope="request"></c:set>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<s:if test="#fc.tzje>=500000">
										<c:set var="bgcolor" value="#FF9999" />
									</s:if>
									<s:elseif test="#fc.tzje>=50000">
										<c:set var="bgcolor" value="#D20000" />
									</s:elseif>
									<s:elseif test="#fc.tzje>=5000">
										<c:set var="bgcolor" value="#FFABCE" />
									</s:elseif>
									<s:else>
										<c:set var="bgcolor" value="#e4f2ff" />
									</s:else>
									<tr bgcolor="${bgcolor}">
										<td align="center" width="60px">
											<s:property value="#fc.loginname" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.dl)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.sy)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.tze)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.sjtz)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.hy)" />
										</td>
										<td align="center" width="90px">
											<s:property
												value="@dfh.utils.NumericUtil@formatDouble(#fc.dls)" />
										</td>
										<td align="center" width="90px">
											${fc.startday}
										</td>
										<td align="center" width="90px">
											${fc.endday}
										</td>
										<td align="center" width="90px">
											${fc.day}
										</td>
									</tr>
									<s:set var="amountValue" value="#fc.sy" scope="request"></s:set>
									<c:set var="ztzeSum" value="${ztzeSum+amountValue}"
										scope="request"></c:set>

									<s:set var="amountPayOutValue" value="#fc.tze" scope="request"></s:set>
									<c:set var="zsySum" value="${zsySum+amountPayOutValue}"
										scope="request"></c:set>
								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="2">
										当页小计:
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.ztzeSum)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.zsySum)" />
									</td>
									<td align="right">
									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
								</tr>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="2">
										总计:
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
									<td align="right">

									</td>
								</tr>

								<tr>
									<td colspan="16" align="right" bgcolor="66b5ff" align="center">
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

