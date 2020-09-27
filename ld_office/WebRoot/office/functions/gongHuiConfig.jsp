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
	
		<title>工会配置</title>
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
function deleteGongHuiData() {
    if(confirm("你确定要清空所有数据吗,请谨慎操作？")){
        $.post("/office/deleteGongHuiDataAll.do",function(data){
            alert(data);
        });
    }
}
function deleteData(id){
    if(confirm("确定？")){
        $.post("/office/deleteDataOne.do",{"id":id},function(data){
            alert(data);
            $("#gongHuiConfigs").submit();
        });
    }
}

function changeIsused(id , isused , btn){
	btn.disabled = true;
	if(confirm("确定？")){
		$.post("/office/changeState.do",{"id":id,"state":isused},function(data){
			alert(data);
			$("#gongHuiConfigs").submit();
		});
	}
}
</script>
	</head>
	<body>
		<p>
			--&gt; 工会配置
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="/office/getGongHuiConfigs.do"
				 theme="simple" id="gongHuiConfigs">
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="860px">
								<tr align="left">
									
									<td>
										 工会分组:
										<input type="text" name="part" />
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
										工会名称:
										<input type="text" name="name" />
									</td>
									
									<td>
										<a href="/office/functions/addGongHuiConfig.jsp" target="_blank" style="color:red;font-size: 14px;">新增工会配置</a>
									</td>
									<td>
										<input type="button" onclick="deleteGongHuiData()" value="清空记录"/>
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
		<div id="middle" style="position: absolute; top: 145px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1110px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('title');" width="130px">
										工会分组
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('title');" width="130px">
										工会名称
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('percent');" width="130px">
										工会会长
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('betMultiples');" width="130px">
										开始报名时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('limitMoney');" width="130px">
										结束报名时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="300px">
										用户等级
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										工会上限人数
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										游戏平台
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										开启状态
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										width="130px">
										创建人
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('isused');" width="130px">
										备注
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');" width="130px">
										操作
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">

									
										<td align="center" width="60px">
											<s:property value="#fc.part" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.name" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.president" />
										</td>
										<td align="center" width="60px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTime" />
										</td>
										<td align="center" width="90px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTime" />
										</td>
									<td align="center" width="300px">
										<s:if test="#fc.level.contains(\"0\")"><s:property value="@dfh.model.enums.VipLevel@getText(0)"/>，</s:if>
										<s:if test="#fc.level.contains(\"1\")"><s:property value="@dfh.model.enums.VipLevel@getText(1)"/>，</s:if>
										<s:if test="#fc.level.contains(\"2\")"><s:property value="@dfh.model.enums.VipLevel@getText(2)"/>，</s:if>
										<s:if test="#fc.level.contains(\"3\")"><s:property value="@dfh.model.enums.VipLevel@getText(3)"/>，</s:if>
										<s:if test="#fc.level.contains(\"4\")"><s:property value="@dfh.model.enums.VipLevel@getText(4)"/>，</s:if>
										<s:if test="#fc.level.contains(\"5\")"><s:property value="@dfh.model.enums.VipLevel@getText(5)"/>，</s:if>
										<s:if test="#fc.level.contains(\"6\")"><s:property value="@dfh.model.enums.VipLevel@getText(6)"/>，</s:if>
										<s:if test="#fc.level.contains(\"7\")"><s:property value="@dfh.model.enums.VipLevel@getText(7)"/>，</s:if>
										<s:if test="#fc.level.contains(\"8\")"><s:property value="@dfh.model.enums.VipLevel@getText(8)"/>&nbsp;</s:if>
									</td>

									<td align="center" width="90px">
											<s:property value="#fc.max" />
										</td>
										<td align="center" width="90px">
											<s:property value="#fc.game" />
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.state==0"><input type="button" value="未开启,点击开启" onclick="changeIsused(<s:property value="#fc.id" />,1 ,this)"/></s:if>
											<s:if test="#fc.state==1"><input type="button" value="已开启,点击关闭" onclick="changeIsused(<s:property value="#fc.id" />,0 ,this)"/></s:if>
										</td>
										<td align="center" width="90px">
											<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
										</td>
									<td align="center" width="90px">
										<s:property value="#fc.creator" />
									</td>
										<td align="center" width="90px">
											<s:property value="#fc.remark" />
										</td>
										<td align="center" width="90px">
											<a href="/office/gongHuiMembersList.do?size=20&id=<s:property value="#fc.id" />" target="_blank">查看成员</a>
											<input type="button" value="删除" onclick="deleteData(<s:property value="#fc.id" />)">
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td colspan="13" align="right" bgcolor="66b5ff" align="center">
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

