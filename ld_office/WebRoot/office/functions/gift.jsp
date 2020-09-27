<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>礼品活动信息</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="excel_menu_left">
			记录&nbsp;-->&nbsp;活动礼品寄送&nbsp;-->&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</div>
		<a href="/office/functions/addGift.jsp" target="_blank" style="margin-right: 180px; color: red; font-weight: bold; display: inline; font-size: 16px;">新增活动礼品</a>
		<div id="excel_menu" style="position: absolute; top: 60px; left: 0px;">
			<s:form action="queryGiftList" namespace="/office" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1450px">	
								<tr align="left">
									<td width="50px">
										类型:
									</td>
									<td width="150px">
										<s:select list="#{ '礼品登记': '礼品登记', '限时礼品': '限时礼品' }" listKey="key" listValue="value" headerKey="" headerValue="" name="type"></s:select>
									</td>
									<td width="50px">
										标题:
									</td>
									<td width="150px">
										<s:textfield cssStyle="width: 115px" name="title" size="20" />
									</td>
									<td width="120px">
										活动开始时间:
									</td>
									<td width="195px">
										<s:textfield name="activityBeginTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
									</td>
									<td width="120px">
										活动结束时间:
									</td>
									<td width="195px">
										<s:textfield name="activityEndTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" />
									</td>
									<td width="80px">
										每页记录:
									</td>
									<td width="120px">
										<s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select>
										<s:set name="by" value="'createTime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
										<s:hidden name="pageIndex" />
									</td>
									<td rowspan="5">
										<s:submit value="查询"></s:submit>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>	
		</div>
		<div id="middle" style="position: absolute; top: 150px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">类型</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">标题</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 55px;">存款要求</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">存款开始时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">存款结束时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 55px;">投注要求</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">投注开始时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">投注结束时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">活动开始时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">活动结束时间</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 55px;">申请列表</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">开放等级</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">备注</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">操作</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="left" style="font-size: 13px; width: 55px;">
										<s:property value="#fc.type" />
									</td>
									<td align="left" style="font-size: 13px;">
										<s:property value="#fc.title" />
									</td>
									<td align="left" style="font-size: 13px;">
										<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.depositAmount)" />
									</td>
									<td align="left" style="font-size: 13px; width: 124px;">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTimeDeposit" />
									</td>
									<td align="left" style="font-size: 13px; width: 124px;">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTimeDeposit" />
									</td>
									<td align="left" style="font-size: 13px;">
										<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.betAmount)" />
									</td>
									<td align="left" style="font-size: 13px; width: 124px;">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTimeBet" />
									</td>
									<td align="left" style="font-size: 13px; width: 124px;">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTimeBet" />
									</td>
									<td align="left" style="font-size: 13px; width: 124px;">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTime" />
									</td>
									<td align="left" style="font-size: 13px; width: 124px;">
										<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTime" />
									</td>
									<td align="left" style="font-size: 13px;">
										<a href="/office/giftOrderList.do?id=<s:property value="#fc.id"/>&size=20" target="_blank">申请名单</a>
									</td>
									<td align="left" style="font-size: 13px;">
										<s:property value="@dfh.utils.Utils@getLevelStr(#fc.levels)" />
									</td>
									<td align="left" style="font-size: 13px;">
										<s:property value="#fc.remark" />
									</td>
									<td align="left" style="font-size: 13px;">
										<s:if test="#fc.status==1">
											<input type="button" value="禁用" onclick="optGift(<s:property value="#fc.id"/>, '0')" />
										</s:if>
										<s:if test="#fc.status==0">
											<input type="button" value="启用" onclick="optGift(<s:property value="#fc.id"/>, '1')" />
										</s:if>
									</td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="14" align="right" bgcolor="66b5ff" align="center" style="font-size: 13px;">${page.jsPageCode}</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">
		
		function gopage(val) {
		    
			document.mainform.pageIndex.value = val;
		    document.mainform.submit();
		}

		function orderby(by) {
			
			if (document.mainform.order.value == "desc") {
				
				document.mainform.order.value = "asc";
			} else {
				
				document.mainform.order.value = "desc";
			}
				
			document.mainform.by.value = by;
			document.mainform.submit();
		}
		
		// 禁用/启动按钮处理方法
		function optGift(id, status) {
		
			$.ajax({ 
				type: "post", 
				url: "/office/optGift.do", 
				data: { "id": id, "status": status },
				cache: false,  
				async: false,
				error: function() {
				
					alert("系统错误");
				},
				complete: function(result) {
				
					alert(result.responseText);
					
					$('#mainform').submit();
				}
		     });
		}
	</script>
</html>