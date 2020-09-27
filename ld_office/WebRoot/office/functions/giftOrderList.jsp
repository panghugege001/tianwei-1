<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>礼品活动订单信息</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 60px; left: 0px;">
			<s:form action="giftOrderList" namespace="/office" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1450px">	
								<tr align="left">
									<td width="50px">
										账号:
									</td>
									<td width="150px">
										<s:textfield name="loginname" size="15" />
									</td>
									<td width="80px">
										每页记录:
									</td>
									<td width="120px">
										<s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select>
										<s:set name="by" value="'applyDate'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
										<s:hidden name="pageIndex" />
										<s:hidden name="id" />
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
			  				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
				        		<tr>
				        			<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">账号</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 55px;">等级</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">姓名</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">收货地址</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">手机号码</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 124px;">申请时间</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 55px;">状态</td>
				           	  		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold;">操作</td>
				            	</tr>
	            				<s:iterator var="fc" value="%{#request.page.pageContents}">
		            			<tr>
		            				<td align="center" style="font-size: 13px;">
		            					<s:property value="#fc.loginname"/>
		            				</td>
		              				<td align="center" style="font-size: 13px;">
		              					<s:property value="@dfh.model.enums.VipLevel@getText(#fc.level)"/>
		              				</td>
		              				<td align="center" style="font-size: 13px;">
		              					<s:property value="#fc.realname"/>
		              				</td>
		              				<td align="center" style="font-size: 13px;">
		              					<s:property value="#fc.address"/>
		             				</td>
		              				<td align="center" style="font-size: 13px;">
		              					<s:property value="#fc.cellphoneNo"/>
		              				</td>
		              				<td align="center" style="font-size: 13px;">
		              					<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.applyDate"/>
		              				</td>
					             	<td align="center" style="font-size: 13px;">
					              		<s:if test="#fc.status==0">未发货</s:if>
										<s:elseif test="#fc.status==1">已发货</s:elseif>
									  	<s:elseif test="#fc.status==2">已签收</s:elseif>
									  	<s:elseif test="#fc.status==3">已取消</s:elseif>   
					          		</td>
		              				<td align="center">
		              	 				<s:if test="#fc.status==0">
											<input type="button" value="修改收货信息" onclick="modifyApplyInfo(<s:property value="#fc.id"/>,'<s:property value="#fc.realname"/>','<s:property value="#fc.address"/>','<s:property value="#fc.cellphoneNo"/>')" />
						 				</s:if>
		              				</td>
		            			</tr>
		  	 	 				</s:iterator>
	            				<tr>
				              		<td colspan="7" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
										${page.jsPageCode} 
				              		</td>
	            				</tr>
	          				</table>
		  				</div>
					</div>
				</div>
	  		</div>
		</div>
	</body>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">

	function gopage(val) {
    
		document.mainform.pageIndex.value = val;
		document.mainform.submit();
	};

	function orderby(by) {
	
		if (document.mainform.order.value == "desc") {
			
			document.mainform.order.value = "asc";	
		} else {
			
			document.mainform.order.value = "desc";	
		}
		
		document.mainform.by.value = by;
		
		document.mainform.submit();
	};
	
	function modifyApplyInfo(id, name, address, cellphoneNo) {
	
		var height = window.screen.height;
		var width = window.screen.width;
		
		window.open("<%=basePath%>/office/functions/modifyGiftApplyInfo.jsp?id=" + id + "&addressee=" + encodeURI(name) + "&address=" + encodeURI(address) + "&cellphoneNo=" + cellphoneNo, 'applyInfo', 'height=350,width=500,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,top=' + (height - 400) / 2 + ',left=' + (width - 300) / 2);	
	};
</script>
</html>