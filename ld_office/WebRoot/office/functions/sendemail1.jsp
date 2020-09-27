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
$(function () {
      $(".button_wxz").hover(function () {
           $(this).removeClass("button_wxz").addClass("button_xz");
      }, function () {
           $(this).removeClass("button_xz").addClass("button_wxz");
      });
 });	

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

//最多15个汉字
function JAlert_Progressbar_Dialog(divId) {
    buttonJAlertStyle();
    $("#" + divId).dialog({
        resizable:false,
        height:352,
        width:520,
        modal:false
    });
}

function submitEmailAction(email){
   var html="<table><tr><td class='label_search_td_play'>标题：</td><td><input type='text' class='input' id='titleEp' style='width: 200px;'/></td></tr><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><div class='button_wxz' onclick='submitEmailAction1(\""+email+"\");' style='margin-left: 370px;'>邮件发送</div></td></tr></table>";
   $("#JqAlert").html(html);
   JAlert_Progressbar_Dialog("JqAlert");
}

//鼠标经过button样式
function buttonJAlertStyle() {
    $(".button_wxz").hover(function () {
        $(this).removeClass("button_wxz").addClass("button_xz");
    }, function () {
        $(this).removeClass("button_xz").addClass("button_wxz");
    });
}

function submitEmailAction1(email){
   if(email==""){
      alert("邮箱不能为空！");
      return;
   }
   var title=$("#titleEp").val();
   var remark=$("#remark").val();
   var action = "/office/getSubmitEmailAction.do";
   if(title == ""){
         alert("标题不能为空！"); 
         return;
	}
	if(remark == ""){
         alert("内容不能为空！"); 
         return;
	}
    /*var xmlhttp = new Ajax.Request(action,
		 {    
		       method: 'post',
		       parameters:"email="+email+"&title="+title+"&remark="+remark+"&r="+Math.random(),
		       onComplete: responseMethod  
	      }
	 );*/
	 var params = {"email":email, "title":title, "remark":remark};
	 $.ajax({
		type: "post",
		cache: false, 
		url: action,
		data: params,
		complete: responseMethod
	 });
	 buttonCancel("JqAlert");
}




function buttonCancel(divId) {
    $("#" + divId).dialog("close");
}



function responseMethod(data){
   $("#JqAlertTwo").html("<table width='300px'><tr><td><br><img src='${pageContext.request.contextPath}/images/presentment.png' height='40px' width='40px'></td><td align='left' class='label_search_td_play'><br>" + data.responseText + "</td></tr><tr><td colspan='2'><hr></td></tr><tr><td></td><td><table><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><div class='button_wxz' onclick='JAlert10Cancel();'>取消</div></td></tr></table></td></tr></table>"); 
   JAlert10Dialog("JqAlertTwo");
}
  
 function JAlert10Cancel() {
    $("#JqAlertTwo").dialog("close");
} 


//浏览器提示框
function JAlert10Dialog(divId) {
    buttonJAlertStyle();
    $("#" + divId).dialog({
          resizable:false,
          height:235,
          width:360,
          modal:true
     });
}

//浏览器提示框
function JAlertJdtDialog(divId) {
    buttonJAlertStyle();
    $("#" + divId).dialog({
          resizable:false,
          height:280,
          width:320,
          modal:true
     });
}


function responseMethodTwo(data){
   $("#JqAlertTwo2").dialog("close");
   $("#JqAlertTwo").html("<table width='300px'><tr><td><br><img src='${pageContext.request.contextPath}/images/presentment.png' height='40px' width='40px'></td><td align='left' class='label_search_td_play'><br>" + data.responseText + "</td></tr><tr><td colspan='2'><hr></td></tr><tr><td></td><td><table><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><div class='button_wxz' onclick='JAlert10Cancel();'>取消</div></td></tr></table></td></tr></table>"); 
   JAlert10Dialog("JqAlertTwo");
}

$(function () {
    $("#checkAllBox").bind("click", function () {
    	if($(this).attr("checked") == "checked"){
        	$("[name = item]:checkbox").attr("checked", true);
    	}else{
    		$("[name = item]:checkbox").attr("checked", false);
    	}
    });

    $("[name = item]:checkbox").bind("click", function () {
    	if($(this).attr("checked") != "checked"){
    		$("#checkAllBox").attr("checked", false);
    	}
    });
   
    //邮件群发
	$("#emailQunFa").unbind().bind("click",function(){
		var result = new Array();
        $("[name = item]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                result.push($(this).attr("value"));
            }
        });
        var len = result.length ;
        if(len > 0){
        	if(confirm("共选中"+len+"个邮箱，确认发送？")){
        		var ids = result.join(",") ;
        		var html="<table><tr><td class='label_search_td_play'>标题：</td><td><input type='text' class='input' id='titleEp' style='width: 200px;'/></td></tr><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><div class='button_wxz' onclick='youjianQunFaCommit(\""+ids+"\");' style='margin-left: 370px;'>邮件发送</div></td></tr></table>";
        		$("#JqAlert").html(html);
        		JAlert_Progressbar_Dialog("JqAlert");
        	}
        }else{
        	alert("请选择您要发送的数据。");
        }	
    });
    
	

function duanXinQunFaCommit(ids){
	var remark = $("#remark").val() ;
	if(remark.length>63){
		alert("短信内容不能超过63个字符");
		return ;
	}
	$.post("/office/duanXinQunFa.do",{"ids":ids,"remark":remark},function(respData){
		alert(respData);
	});
}
function youjianQunFaCommit(ids){
	var remark = $("#remark").val() ;
	remark = remark.replace(/%/g, "%25");  
	remark = remark.replace(/\&/g, "%26");  
	remark = remark.replace(/\+/g, "%2B"); 
	var zt = $("#titleEp").val() ;
	if(null==ids&&isd==''){
		alert('请选择邮件接收者！');
		return;
	}
	$.ajax({
		url : "/office/youjianQunFa.do",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data : "ids="+ids+"&remark=" + remark+"&zt="+zt,
		async : false, // 异步
		success : function(msg) {
			alert(msg);
		},
	});
	
	
	
	/* $.post("/office/youjianQunFa.do",{"ids":ids,"remark":remark,"zt":zt},function(respData){
		alert(respData);
	}); */
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
			记录 --&gt; 电话记录
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
							<table width="1350px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<tr align="left">
									<td colspan="12" align="left">
										<s:form action="queryPhone" namespace="/office"
											name="mainform" id="mainform" theme="simple">
											<table border="0" cellpadding="0" cellspacing="0">
												<tr>
													<td align="right" width="60px">
														姓名:
													</td>
													<td width="80px" align="left">
														<input type="hidden" name="pageIndex" value="1"
															id="pageIndex" />
														<input type="hidden" name="by" value="createTime" id="by" />
														<input type="hidden" name="order" value="desc" id="order" />
														<s:textfield name="loginname" size="15" />
													</td>
													<td align="right" width="60px">
														是否注册:
													</td>
													<td width="110px" align="left">
														<s:select cssStyle="width:150px" name="isreg"
															list="%{#application.UserRegisterStatus}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
													<td align="right" width="60px">
														手机状态:
													</td>
													<td align="left">
														<s:select cssStyle="width:150px" name="phonestatus"
															list="%{#application.PhoneStatus}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
													<td align="right" width="60px">
														负责专员:
													</td>
													<td width="110px" align="left">
													<s:if test='#session.operator.cs==null || #session.operator.cs==""|| #session.operator.cs=="all"'> 
													<s:textfield name="cs" id="cs" size="15"  />
													</s:if>
													<s:else>
													<s:textfield name="cs" id="cs" size="15" value="%{#session.operator.cs}"  readonly="true"/>
													</s:else>
													</td>
													<td rowspan="5" width="60px">
														<s:submit cssStyle="width:100px; height:30px;" value="查询"></s:submit>
														<!-- <input type="button" onclick="phoneAll();" value="邀请码批量发送（短信）" />
														<input type="button" onclick="emailAll();" value="邀请码批量发送（邮箱）" /> -->
														<!-- <input type="button" onclick="emailHtmlAll();" value="活动信息批量发送（邮箱）" /> -->
														<input type="button"  id="duanXinQunFa" value="短信群发" />
														<input type="button"  id="emailQunFa" value="邮件群发" />
														<!-- <input type="button"  id="emailQunFa" value="邮件群发" /> -->
														
														<input type="button"  id="phoneQunHu" value="批量呼叫" />
													</td>
												</tr>
												<tr>
													<td align="right" width="60px">
														电话:
													</td>
													<td width="80px" align="left">
														<s:textfield name="phone" size="15" />
													</td>
													<td align="right" width="60px">
														是否存款:
													</td>
													<td align="left">
														<s:select cssStyle="width:150px" name="isdeposit"
															list="%{#application.UserDepositStatus}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
													<td align="right" width="60px">
														客户状态:
													</td>
													<td align="left">
														<s:select cssStyle="width:150px" name="userstatus"
															list="%{#application.UserStatus}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
													<td align="right" width="60px">
														每页:
													</td>
													<td align="left">
														<s:select cssStyle="width:120px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
												</tr>
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
													<td align="right" width="60px">邮箱:</td>
													<td><s:textfield name="email" size="15" /></td>
													<td align="right" width="60px">类型</td>
													<td>
													<s:if test='#session.operator.type==null || #session.operator.type==""|| #session.operator.type=="all" || #session.operator.type.contains("cs") '> 
													<s:textfield name="type" size="15" />
													</s:if>
													<s:else>
													<s:textfield name="type" size="15" value="%{#session.operator.type}"  readonly="true"/>
													</s:else>
													</td>
												</tr>
												<tr>
												<td align="right" width="60px">邀请码</td>
													<td><s:textfield name="shippingCode" size="15" />
													
													</td>
													
													<%-- <td align="right">
														更新开始时间:
													</td>
													<td align="right">
														<s:textfield id='beginTime'
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate"/>
													</td>
													
													<td align="left"><input type="button" value="更新未存款玩家数据"
																style="width: 160px; height: 30px;"
																onclick="updateotherCustomInof()" />
													</td> 
													--%>
													<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'" >
													<td align="left"><input type="button" value="更新玩家邮箱状态"
																style="width: 160px; height: 30px;"
																onclick="updateEmailFlagForUser()" />
													</td> 
													</s:if>--%>
												    <td align="right">
														更新开始时间:
													</td>
													<td align="right">
														<s:textfield id='DeEndDate'
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate"/>
													</td>
													<td align="right">
														邮箱状态:
													</td>
													<td align="left">
														<s:select name="emailflag" list="#{'':'全部','0':'无效','1':'有效','2':'未知'}" listKey="key" listValue="value"/>
													</td>
													<td align="left"><input type="button" value="更新玩家存款状态"
																style="width: 160px; height: 30px;"
																onclick="updatedeposit()" />
													</td>
													<td align="left"><input type="button" value="玩家数据统计"
																style="width: 160px; height: 30px;"
																onclick="statisticData()" />
													</td>
													
												</tr>
												</s:form>
												<tr>
												<td></td>
													<s:form action="addPhone" namespace="/batchxima"
														enctype="multipart/form-data" name="mainformTwo"
														id="mainformTwo" theme="simple"
														onsubmit="submitonce(this);">
														<td align="right" width="60px">
															<input type="file" id="file" name="myFile" />
														</td>
														<td align="right" width="60px">
															<input type="button" value="上传"
																style="width: 60px; height: 30px;"
																onclick="return upload();" />
														</td>
													</s:form>
													<td>
														<a
															href="${pageContext.request.contextPath}/files/phone.xls">模板下载</a>
													</td>
												<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'" >
													<td align="left" width="120px">
															<input type="button" value="更新邮件是否有效"
																style="width: 120px; height: 30px;"
																onclick="return updateEmail();" />
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
									<td bgcolor="#0084ff" align="center">
										<input type="checkbox" id="checkAllBox">
									</td>
									
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('id');">
										编号
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('name');">
										姓名
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold" title="点击排序"
										onclick="orderby('isreg');">
										是否注册
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold" title="点击排序"
										onclick="orderby('isdeposit');">
										是否存款
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('phonestatus');">
										手机状态
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold" title="点击排序"
										onclick="orderby('userstatus');">
										客户状态
									</td>
									<s:if test='#session.operator.type!="all"'> 
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										邮箱
									</td>
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										邮箱是否有效
									</td>
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										QQ
									</td>
									</s:if>
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										发送邮箱
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										电话
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										发送短信
									</td>
									<td bgcolor="#0084ff" align="center" width="100px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										座机号1
									</td>
									<td bgcolor="#0084ff" align="center" width="20px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										座机号2
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('cs');">
										专员
									</td>
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createTime');">
										创建时间
									</td>
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										邀请码
									</td>
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										类型
									</td>
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										导入批次
									</td>
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										信息
									</td>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold">
										操作
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr>
										<td align="center" >
											<input type="checkbox" name="item" value="<s:property value="#fc.id"  />">
										</td>
									
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.id" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'" >
												<s:property value="#fc.name"/>
											</s:if>
											<s:else>
												<s:property value="#fc.name.substring(0,1)+'**'"/>
											</s:else> --%><s:property value="#fc.name"/>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.isreg==0">
												   未注册
												</s:if>
											<s:else>
												   已注册
												</s:else>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.isdeposit==0">
												   未存款
												</s:if>
											<s:else>
												   已存款
												</s:else>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.phonestatus==0">
												   未拨打
												</s:if>
											<s:elseif test="#fc.phonestatus==1">
												   正常
												</s:elseif>
											<s:elseif test="#fc.phonestatus==2">
												   无人接听
												</s:elseif>
											<s:elseif test="#fc.phonestatus==4">
												   可用
												</s:elseif>
											<s:else>
												   空号
												</s:else>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.userstatus==0">
												   未处理
												</s:if>
											<s:elseif test="#fc.userstatus==1">
												   良好
												</s:elseif>
											<s:elseif test="#fc.userstatus==2">
												   一般
												</s:elseif>
											<s:else>
												   拒绝
												</s:else>
										</td>
										<s:if test='#session.operator.type!="all"'> 
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.email" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="null==#fc.emailflag">
												未知
											</s:if>
											<s:elseif test="#fc.emailflag ==0">
												无效
											</s:elseif>
											<s:elseif test="#fc.emailflag ==1">
												有效
											</s:elseif>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.qq" />
										</td>
										</s:if>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.email !=''">
												<input type="button"
													onclick="submitEmailAction('${fc.email}');" value="发送邮箱" />
											</s:if>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<c:if test="${sessionScope.operator.authority eq 'boss'}">
												<s:if test="#fc.phone !=''">
													<s:property value="#fc.phone" />
												</s:if>
											</c:if>
											<c:if test="${sessionScope.operator.authority ne 'boss'}">
												<s:if test="#fc.phone !=''">
												*******<s:property value="#fc.phone.substring(7,11)" />
												</s:if>
											</c:if>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.phone !=''">
												<input type="button"
													onclick="submitPhoneAction('${fc.phone}');" value="发送短信" />
											</s:if>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="100px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.phone !=''">
																								<c:if
				test="${sessionScope.operator.phonenoBL eq null || sessionScope.operator.phonenoBL eq ''
				}">
											
												<select onchange="changeQyExten(this.value,${id});">
		         								<c:if test="${sessionScope.operator.authority eq 'boss' }">
														<option value="801" selected="selected">
															801
														</option>
														<option value="802" selected="selected">
															802
														</option>
													</c:if>
													<option value="803" selected="selected">
														803
													</option>
													<option value="804">
														804
													</option>
													<option value="805">
														805
													</option>
													<option value="806">
														806
													</option>
													<option value="807">
														807
													</option>
													<option value="808">
														808
													</option>
													<option value="809">
														809
													</option>
													<option value="810">
														810
													</option>
													<option value="811">
														811
													</option>
													<option value="812">
														812
													</option>
													<option value="813">
														813
													</option>
													<option value="814">
														814
													</option>
													<option value="815">
														815
													</option>
													<option value="816">
														816
													</option>
													<option value="817">
														817
													</option>
													<option value="818">
														818
													</option>
													<option value="819">
														819
													</option>
													<option value="820">
														820
													</option>
													<option value="821">
														821
													</option>
													<option value="822">
														822
													</option>
													
													<c:forEach var="i" begin="888101" end="888130" step="1">
													  <option value="${i}">
															${i}
														</option>
													</c:forEach>
						<option value=801>
							比邻801
						</option>
						<option value=802>
							比邻802
						</option>
						<option value=803>
							比邻803
						</option>
						<option value=804>
							比邻804
						</option>
						<option value=805>
							比邻805
						</option>
						<option value=806>
							比邻806
						</option>
						<option value=807>
							比邻807
						</option>
						<option value=808>
							比邻808
						</option>
												</select>
												<%
													dfh.model.Customer cs = (dfh.model.Customer) request
															.getAttribute("fc");
													if (cs.getPhone() != null && !cs.getPhone().equals("")) {
														try{
														long l = Long.parseLong(cs.getPhone());
														String p = "1" + String.valueOf(l);
														l = Long.parseLong(p) * 11 + 159753;
														request.setAttribute("encryptPhone", l);
														long l2 = Long.parseLong(cs.getPhone());
														l2 = l2 * 11 + 159753;
														request.setAttribute("encryptPhone2", l2);
														} catch (Exception e1) {
															System.out.println("玩家手机号格式错误"+cs.getRemark());
														}
													}
												%>
												<!--  <a id="_call_${id}"
													href="http://192.168.0.8:12121/bridge/callctrl?callee=${encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
													target="_blank">呼叫该用户(内)</a>
												<a id="_call_${id}"
													href="http://115.29.227.189:12121/bridge/callctrl?callee=${encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
													target="_blank">呼叫该用户(外)</a>-->
												<a id="_callOne_${id}"
													href="http://47.90.12.131:12121/bridge/callctrl?callee=${encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
													target="_blank">CALL(国信)</a>	
													
												<a id="_callTwo_${id}" style="display: none;" 
												href="http://203.88.165.54/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
												target="_blank">CALL(废弃)</a>
												
												<a id="_callThree_${id}"
												href="http://123.1.186.200/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
												target="_blank">CALL(比邻-外)</a>
												
												<a id="_callFour_${id}" style="display: none;" 
												href="http://192.168.100.5/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
												target="_blank">CALL(比邻-内)</a>
									
												<a id="_callFive_${id}"
												href="http://210.51.190.109/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
												target="_blank">CALL(比邻-外(新))</a>
												
												<a id="_callSix_${id}"
												href="http://210.51.190.17/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
												target="_blank">CALL(比邻2016)</a>
													
										</c:if>		
										
										
										
											<c:if
				test="${sessionScope.operator.phonenoBL ne null && sessionScope.operator.phonenoBL ne ''
				}">
												<%
													dfh.model.Customer cs = (dfh.model.Customer) request
															.getAttribute("fc");
													if (cs.getPhone() != null && !cs.getPhone().equals("")) {
														try{
														long l = Long.parseLong(cs.getPhone());
														String p = "1" + String.valueOf(l);
														l = Long.parseLong(p) * 11 + 159753;
														request.setAttribute("encryptPhone", l);
														long l2 = Long.parseLong(cs.getPhone());
														l2 = l2 * 11 + 159753;
														request.setAttribute("encryptPhone2", l2);
													} catch (Exception e1) {
														System.out.println("玩家手机号格式错误"+cs.getRemark());
													}
													}
												%>
												<%-- <a id="_call_${id}"
													href="http://192.168.1.10:12121/bridge/callctrl?callee=${encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
													target="_blank">呼叫该用户(内)</a> --%>
													<%-- <a id="_call_${id}"
													href="http://115.29.227.189:12121/bridge/callctrl?callee=${encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=${sessionScope.operator.phonenoGX}"
													target="_blank">呼叫该用户(外)</a> --%>
 													<a id="_callOne_${id}"
													href="http://47.90.12.131:12121/bridge/callctrl?callee=${encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=${sessionScope.operator.phonenoGX}"
													target="_blank">CALL(国信)</a>
													
													<a id="_callTwo_${id}" style="display: none;" 
													href="http://203.88.165.54/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
													target="_blank">CALL(废弃)</a>
													
													<a id="_callThree_${id}"
													href="http://123.1.186.200/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
													target="_blank">CALL(比邻-外)</a>
													
													<a id="_callFour_${id}" style="display: none;" 
													href="http://192.168.100.5/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
													target="_blank">CALL(比邻-内)</a>
													
													<a id="_callFive_${id}"
													href="http://210.51.190.109/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
													target="_blank">CALL(比邻-外(新))</a>
													
													<a id="_callSix_${id}"
													href="http://210.51.190.17/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
													target="_blank">CALL(比邻2016)</a>
										
										</c:if>			
										
			
						
											</s:if>
										</td>
										
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="20px" style="font-weight: bold; cursor: pointer;">
											<!-- <s:if test="#fc.phone !=''">
												<select onchange="changeE68Exten(this.value,${id});">
													<c:if test="${sessionScope.operator.authority eq 'boss' }">
														<option value="8001" selected="selected">
															8001
														</option>
														<option value="8002" selected="selected">
															8002
														</option>
													</c:if>
													<option value="8003" selected="selected">
														8003
													</option>
													<option value="8004">
														8004
													</option>
													<option value="8005">
														8005
													</option>
													<option value="8006">
														8006
													</option>
													<option value="8007">
														8007
													</option>
													<option value="8008">
														8008
													</option>
													<option value="8009">
														8009
													</option>
													<option value="8010">
														8010
													</option>
												</select>
												<a id="_call2_${id}"
													href="https://192.168.0.160/flycallsystem/webdail.php?call=${encryptPhone2}&prefix=0&exten=8003"
													target="_blank">呼叫该用户</a>
											</s:if>-->
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.cs" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.createTime" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:set name="compareBeginTime" value="new java.text.SimpleDateFormat('yyyy-MM-dd').parse('2014-11-11')"></s:set>
											<s:set name="compareEndTime" value="new java.text.SimpleDateFormat('yyyy-MM-dd').parse('2014-11-19')"></s:set>
											
											<c:if test=" (#fc.createTime.getTime() < #compareBeginTime.getTime() || #fc.createTime.getTime() > #compareEndTime.getTime()) ">
												<s:property value="#fc.shippingCode" />
											</c:if>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.type" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.batch" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.remark" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<a target="_blank"
												href="/office/getCustomerPno.do?id=<s:property value="#fc.id"/>"
												style="cursor: pointer" title="">修改</a>
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

