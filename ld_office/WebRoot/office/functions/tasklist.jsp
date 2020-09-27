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
		<title>玩家摇摇乐任务记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function changeState(id){
	$.post("/office/changeState.do",{"id":id},function(data){
		alert(data);
		window.location.href="/office/getTaskList.do";
	});
}

</script>
	</head>
	<body>
		<p>
			--&gt; 摇摇乐任务记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<br />
		<div id="middle" style="position: absolute; top: 145px; left: 0px">
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
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('type');" width="130px">
										类型
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('title');" width="130px">
										标题
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('giftmoney');" width="130px">
										礼金
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('requireData');" width="130px">
										要求
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('disable');" width="130px">
										状态
									</td>
									<td>
										操作
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									
									<tr <s:if test="#fc.disable==0"> style="color: red;"</s:if> >
										<td align="center" width="60px">
											<s:if test="#fc.type==1">PT次存</s:if>
											<s:if test="#fc.type==2">TTG次存</s:if>
											<s:if test="#fc.type==3">微信存款</s:if>
											<s:if test="#fc.type==4">PT1万流水</s:if>
											<s:if test="#fc.type==5">TTG三万流水</s:if>
											<s:if test="#fc.type==6">PT3万流水</s:if>
											<s:if test="#fc.type==7">存款5000</s:if>
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.title" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.giftmoney" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.requireData" />
										</td>
										<td align="center" width="90px">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.disable==0">启用</s:if>
											<s:if test="#fc.disable==1">禁用</s:if>
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.disable==0"><a href="javascript:changeState(<s:property value="#fc.id" />)">点击禁用</a></s:if>
											<s:if test="#fc.disable==1"><a href="javascript:changeState(<s:property value="#fc.id" />)">点击启用</a></s:if>
										</td>
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

