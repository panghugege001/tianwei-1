<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>礼品申请信息</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div id="excel_menu" style="position: absolute; top: 60px; left: 0px;">
			<s:form action="activityList" namespace="/office" name="mainform" id="mainform" theme="simple">
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
				        			<td bgcolor="#0084ff" align="center" style="font-size: 13px;width: 100px; color: #FFFFFF; font-weight: bold;">账号</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; width: 100px;color: #FFFFFF; font-weight: bold; width: 55px;">等级</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px;width: 100px; color: #FFFFFF; font-weight: bold;">姓名</td>
									<td bgcolor="#0084ff" align="center" style="font-size: 13px;width: 100px; color: #FFFFFF; font-weight: bold;">活动标题</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; width: 200px; color: #FFFFFF; font-weight: bold;">收货地址</td>
				              		<td bgcolor="#0084ff" align="center" style="font-size: 13px; color: #FFFFFF; font-weight: bold; width: 124px;">申请时间</td>
				           	  		<td bgcolor="#0084ff" align="center" style="font-size: 13px;width: 100px; color: #FFFFFF; font-weight: bold;">备注</td>
				            	</tr>
	            				<s:iterator var="fc" value="%{#request.page.pageContents}">
		            			<tr>
		            				<td align="center" style="font-size: 13px;width: 100px;">
		            					<s:property value="#fc.username"/>
		            				</td>
		              				<td align="center" style="font-size: 13px;width: 100px;">
		              					<s:property value="@dfh.model.enums.VipLevel@getText(#fc.level)"/>
		              				</td>
		              				<td align="center" style="font-size: 13px;width: 100px;">
		              					<s:property value="#fc.accountname"/>
		              				</td>
									<td align="center" style="font-size: 13px;width: 100px;">
										<s:property value="#fc.title"/>
									</td>
		              				<td align="center" style="font-size: 13px;width: 200px;">
		              					<s:property value="#fc.address"/>
		             				</td>
		              				<td align="center" style="font-size: 13px;">
		              					<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/>
		              				</td>
		              				<td align="center" style="font-size: 13px;">
										<s:property value="#fc.remark"/>
		              				</td>
		            			</tr>
		  	 	 				</s:iterator>
	            				<tr>
				              		<td colspan="8" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
	

</script>
</html>