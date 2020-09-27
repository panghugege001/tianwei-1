<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<%@include file="/office/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>电话记录</title>
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
	</head>
	<style type="text/css">
/*.search_margin{*/ /*margin-left:8px;*/ /*}*/
.label_search_td_play {
	font-family: Tahoma;
	font-size: 15px;
	/*font-size: 11px;*/
	line-height: 28px;
	font-weight: bold;
	/* text-align: center;*/
	text-transform: capitalize;
	color: #000000;
	text-decoration: none;
	padding-right: 1px;
}

.input {
	font-family: Tahoma;
	font-size: 18px;
	/*font-size: 11px;*/
	font-weight: normal;
	/*text-transform: capitalize;*/
	text-decoration: none;
	background-color: #FFFFFF;
	border: 1px solid #336699;
	line-height: 16px;
	height: 22px;
	float: left;
	margin-top: 2px;
}

.button_wxz {
    margin-top: 1px;
    font-family: Tahoma;
    font-size: 15px;
    line-height: 28px;
    font-weight: bold;
    text-decoration: none;
    text-align: center;
    float: left;
    cursor: pointer;
    background-image: url(${pageContext.request.contextPath}/images/button_wxz.jpg);
    background-repeat: no-repeat;
    height: 30px;
    width: 87px;
    margin-left: 8px;
}

.button_xz {
    margin-top: 1px;
    font-family: Tahoma;
    font-size: 15px;
    line-height: 28px;
    color: #FFFFFF;
    font-weight: bold;
    text-decoration: none;
    text-align: center;
    float: left;
    cursor: pointer;
    background-image: url(${pageContext.request.contextPath}/images/button_xz.jpg);
    background-repeat: no-repeat;
    height: 30px;
    width: 87px;
    margin-left: 8px;
}
</style>
	<script type="text/javascript">

function gopage(val){
    $("#pageIndex").val(val);//清空  
    $("#mainform").submit();
}
function orderby(by){
    var order=$("#order").val();;
	if(order=="desc"){
	    $("#order").val("asc");
    }else{
		$("#order").val("desc");
	}
	$("#by").val(by);
	$("#mainform").submit();
}

function upload(){
	var file=$("#file").val();
	if(file==""){
	   alert("请选择文件！");
	   return false;
	}
   	$("#mainformTwo").submit();
    return false;
}

//玩家数据统计
function statisticData(){
	if(confirm("该操作持续较长时间,确认执行更新操作吗？请确保所选时间无误。")){
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var cs = $("#cs").val();
		if(null==startTime||startTime==''){
			alert("开始时间不能为空");
			return;
		}
		if(null==endTime||endTime==''){
			alert("结束时间不能为空");
			return;
		}
	$.ajax({
		url : "/office/statisticData.do",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data : {"beginTime":startTime,"endTime":endTime,"cs":cs},
		async : false, // 异步
		success : function(msg) {
			alert(msg);
		},
	});
	}
}


//更新邮件是否有效
function showEmail(){
	
	if(confirm("该操作持续较长时间,确认执行更新操作吗？请确保所选时间无误。")){
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		
		
		if(null==startTime||startTime==''){
			alert("开始时间不能为空");
			return;
		}
		if(null==endTime||endTime==''){
			alert("结束时间不能为空");
			return;
		}
		
		window.location.href='/batchxima/downloadMailExcel.do?start='+startTime+'&end='+ endTime;
		
	}
}


</script>
	<body>
		<s:hidden value="%{#session.operator.cs}" id="csText"></s:hidden>
		<s:hidden value="%{#session.operator.authority}" id="authorityText"></s:hidden>
		<s:hidden value="%{#session.operator.type}" id="typeText"></s:hidden>
		<div id="JqAlert" title="温馨提示"
			style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);">
		</div>
		<div id="JqAlertTwo" title="温馨提示"
			style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);">
		</div>
		<div id="JqAlertTwo2" title="温馨提示">
		</div>
		<div>
			其他 --&gt; 邮件检测
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1350px" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr align="left">
									<td colspan="12" align="left">
										
											<table border="0" cellpadding="0" cellspacing="0">
											    <s:form action="queryMail" namespace="/office" name="mainform" id="mainform" theme="simple">
											    	<input type="hidden" name="pageIndex" value="1" id="pageIndex" />
											    
												<tr>
													<td align="right" width="60px">
														开始时间:
													</td>
													<td width="80px" align="left">
														<s:textfield name="start" size="18" id="startTime"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														结束时间:
													</td>
													<td align="left">
														<s:textfield name="end" size="18" cssStyle="width:150px"  id="endTime"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
														
													</td>
													
											 	  <td align="right">
														邮箱状态:
													</td>
													<td align="left">
														<s:select name="emailflag" list="#{'':'全部','1':'无效','0':'有效'}" listKey="key" listValue="value"/>
													</td>
													
											
													
													<td align="right" width="60px">
														每页:
													</td>
													<td align="left">
														<s:select cssStyle="width:120px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
														<td align="left">
															<s:submit cssStyle="width:100px; height:30px;" value="查询"></s:submit>
															
											         	</td>
												</tr>
												</s:form>
												<tr>
													<s:form action="addMail" namespace="/batchxima" enctype="multipart/form-data" name="mainformTwo" id="mainformTwo" theme="simple" onsubmit="submitonce(this);">
														<td align="right" width="60px">
															<input type="file" id="file" name="myFile" />
														</td>
														<td align="right" width="60px">
															<input type="button" value="上传" style="width: 60px; height: 30px;" onclick="return upload();" />
														</td>
													</s:form>
													<td>
														<a href="${pageContext.request.contextPath}/files/phone.xls">模板下载</a>
													</td>
												      <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'" >
													     <td align="left" width="120px">
															<input type="button" value="下载有效邮件地址" style="width: 120px; height: 30px;" onclick="return showEmail();" />
													      </td>
											          </s:if>
												   </tr>
											</table>
									</td>
								</tr>
							</table>
							<table width="1340px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<tr>
									
								
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										邮箱
									</td>
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										邮箱是否有效
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr>
										
										
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.email" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="null==#fc.status">
												未知
											</s:if>
											<s:elseif test="#fc.status ==0">
												有效
											</s:elseif>
											<s:elseif test="#fc.status ==1">
												无效
											</s:elseif>
										</td>
										
									</tr>
								</s:iterator>
								<tr>
									<td colspan="18" align="right" bgcolor="66b5ff" align="center">
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

