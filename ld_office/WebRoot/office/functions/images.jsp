<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<%@include file="/office/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<link
			href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.21.custom.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-ui-1.8.21.custom.min.js"></script>
		<script type="text/javascript">
	function gopage(val) {
		document.mainform.pageIndex.value = val;
		document.mainform.submit();
	}
    function updateService(id,ip){
       $("#JqAlertTwo2").html("<table width='300px'><tr><td><img src='${pageContext.request.contextPath}/images/jdtiao.gif' height='200px;' width='280px;'/></td></tr></table>"); 
       JAlertJdtDialog("JqAlertTwo2");
       var action = "/office/updateServiceImage.do";
         var xmlhttp = new Ajax.Request(    
		   action,
		  {    
		       method: 'post',
		       parameters:"hdImage.id="+id+"&serviceIp="+ip+"&r="+Math.random(),
		       onComplete: responseMethod 
	      }
	     );
    }
    function openCloseImage(id,imageStatus){
       var action = "/office/updateImageStatusServiceImage.do";
         var xmlhttp = new Ajax.Request(    
		   action,
		  {    
		       method: 'post',
		       parameters:"hdImage.id="+id+"&imageStatus="+imageStatus+"&r="+Math.random(),
		       onComplete: responseMethod 
	      }
	     );
    }
    
    
    //浏览器提示框
function JAlertJdtDialog(divId) {
    $("#" + divId).dialog({
          resizable:false,
          height:280,
          width:320,
          modal:true
     });
}

function responseMethod(data){
   $("#JqAlertTwo2").dialog("close");
   alert(data.responseText);
   document.getElementById("mainform").submit();
}
    
</script>
	</head>
	<body>
		<div id="JqAlertTwo2" title="正在更新图片到服务器">
		</div>
		<s:form action="queryImage" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div id="excel_menu_left">
				官网图片 --> 官网活动图片列表
			</div>
			<div id="excel_menu">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="1">
					<tr align="left">
						<td>
							开始时间:
						</td>
						<td>
							<s:textfield name="start" size="16"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{startTime}" />
						</td>
						<td>
							结束时间:
						</td>
						<td>
							<s:textfield name="end" size="16"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{endTime}" />
						</td>
						<td>
							图片名称:
						</td>
						<td>
							<s:textfield name="imageName"></s:textfield>
						</td>
						<td>
							官网显示状态:
						</td>
						<td>
							<s:select cssStyle="width:80px" list="#{'':'','0':'关闭','1':'开启'}"
								name="imageStatus" listKey="key" listValue="value" emptyOption="false"></s:select>
						</td>
						<td>
							每页:
						</td>
						<td>
							<s:select list="%{#application.PageSizes}" name="size"></s:select>
						</td>
						<td>
							<s:submit value="查询" />
						</td>
						<td>
							<a href="/office/functions/imageUpload.jsp" style="color: black;">新增官网图片</a>
							<a href="/office/functions/imageUploadSwf.jsp"
								style="color: black;">新增官网首页转轮</a>
						</td>
					</tr>
				</table>
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:hidden name="id" id="guestid"></s:hidden>
				<s:hidden name="by" value="createtime"></s:hidden>
			</div>
			<br />
			<br />
			<br />
			<br />
			<div id="middle">
				<div id="right">
					<div id="right_01">
						<div id="right_001">
							<div id="right_02">
								<div id="right_03"></div>
							</div>
							<div id="right_04">
								<table width="98%" border="0" align="center" cellpadding="0"
									cellspacing="1" bgcolor="#99c8d7">
									<tr>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											名称
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											开始时间
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											结束时间
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											创建时间
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											首页小图
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											活动头图
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											活动大图
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											首页旋转Flash图
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											备注
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											操作
										</td>
										<td bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											同步更新
										</td>
									</tr>
									<s:iterator var="s" value="%{#request.page.pageContents}">
										<tr>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.imageName" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.imageStart" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.imageEnd" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.createdate" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="<s:property value="#s.image001" />" target="_blank"><img
														src="<s:property value="#s.image001" />" height="50px;"
														width="100px;" /> </a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="<s:property value="#s.image002" />" target="_blank"><img
														src="<s:property value="#s.image002" />" height="50px;"
														width="100px;" /> </a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="<s:property value="#s.image003" />" target="_blank"><img
														src="<s:property value="#s.image003" />" height="50px;"
														width="100px;" /> </a>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:if test="#s.image004!=null">
													<a href="<s:property value="#s.image004" />"
														target="_blank"><object
															classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
															codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0"
															width="100" height="50">
															<param name="wmode" value="transparent" />
															<param name="movie"
																value="<s:property value="#s.image004" />" />
															<param name="quality" value="high" />
															<embed src="<s:property value="#s.image004" />"
																width="100" height="50" quality="high"
																pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash"
																type="application/x-shockwave-flash" wmode="transparent"></embed>
														</object> </a>
												</s:if>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<s:property value="#s.remark" />
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<a href="/office/updateImagePage.do?hdImage.id=${s.id}">修改</a>
												<s:if test="#s.imageStatus==0">
													<a href="javaScript:void(0);" onclick="return openCloseImage(${s.id},'1');">开启</a>
												</s:if>
												<s:else>
													<a href="javaScript:void(0);" onclick="return openCloseImage(${s.id},'0');">关闭</a>
												</s:else>
											</td>
											<td bgcolor="#e4f2ff" align="center" style="font-size: 13px;">
												<%
												    
													dfh.model.HdImage image = (dfh.model.HdImage) request.getAttribute("s");
													String imageIps[] = image.getImageIp().split("、");
													if (imageIps != null && imageIps.length > 0) {
														for (String imageIp : imageIps) {
															request.setAttribute("imageIp", imageIp);
												%>
												<c:if test="${imageIp!=null && imageIp!=''}">
													<a href="javaScript:void(0);"
														onclick="return updateService(${s.id},'${imageIp}');">${imageIp}</a>
												</c:if>
												<%
													}
													}
												%>
											</td>
										</tr>
									</s:iterator>
									<tr>
										<td colspan="12" align="right" bgcolor="66b5ff" align="center"
											style="font-size: 13px;">
											${page.jsPageCode}
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>

