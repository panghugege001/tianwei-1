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


function submitPhoneAction(phone){
   var html="<table><tr><td class='label_search_td_play'>标题：</td><td><input type='text' class='input' id='titleEp' style='width: 200px;'/></td></tr><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><div class='button_wxz' onclick='submitPhoneAction1(\""+phone+"\");' style='margin-left: 370px;'>短信发送</div></td></tr></table>";
   $("#JqAlert").html(html);
   JAlert_Progressbar_Dialog("JqAlert");
}


//更新邮件是否有效
function updateEmail(){
	if(confirm("该操作持续较长时间,确认执行更新操作吗？请确保所选时间无误。")){
	$.ajax({
		url : "/office/updateEmailFlag.do",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data : {},
		async : true, // 异步
		success : function(msg) {
			alert("更新操作已在后台执行");
		},
	});
	}
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

function emailAll(){
   alert("暂时屏蔽");
   return;
   $("#JqAlertTwo2").html("<table width='300px'><tr><td><img src='${pageContext.request.contextPath}/images/jdtiao.gif' height='200px;' width='280px;'/></td></tr></table>"); 
   JAlertJdtDialog("JqAlertTwo2");
   var action = "/office/getSendEmailAction.do";
         var xmlhttp = new Ajax.Request(    
		   action,
		  {    
		       method: 'post',
		       parameters:"",
		       onComplete: responseMethodTwo  
	      }
	     );
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
    //短信群发
    $("#duanXinQunFa").unbind().bind("click",function(){
    	var result = new Array();
        $("[name = item]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                result.push($(this).attr("value"));
            }
        });
        var len = result.length ;
        if(len > 0){
        	if(confirm("共选中"+len+"个手机号，确认发送？")){
        		var ids = result.join(",") ;
        		var html="<table><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><div class='button_wxz' onclick='duanXinQunFaCommit(\""+ids+"\");' style='margin-left: 370px;'>短信发送</div></td></tr></table>";
        		$("#JqAlert").html(html);
        		JAlert_Progressbar_Dialog("JqAlert");
        	}
        }else{
        	alert("请选择您要发送的数据。");
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
    
    
});


function youjianQunFaCommit(ids){
	
	console.log(ids);
	var remark = $("#remark").val() ;

	ids = ids.replace(/%/g, "%25");  
	ids = ids.replace(/\&/g, "%26");  
	ids = ids.replace(/\+/g, "%2B"); 
	
	remark = remark.replace(/%/g, "%25");  
	remark = remark.replace(/\&/g, "%26");  
	remark = remark.replace(/\+/g, "%2B"); 
	var zt = $("#titleEp").val() ;
	if(null==ids&&isd==''){
		alert('请选择邮件接收者！');
		return;
	}
	$.ajax({
		url : "/office/sendYiYeEmailSubscription.do",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data : "ids="+ids+"&remark=" + remark+"&zt="+zt,
		async : false, // 异步
		success : function(msg) {
			var json=JSON.parse(msg);
			if (json.success == "200") {
				 alert(json.msg1);
			} else {
				alert(json.errormsg);
			}
		},
	});
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
										
											<table border="0" cellpadding="0" cellspacing="0">
											<s:form action="checkYiYeEmailSubscription" namespace="/office" name="mainform" id="mainform" theme="simple">
												<input type="hidden" name="pageIndex" value="1" id="pageIndex" />
												<tr>
													<td align="right" width="60px">
														开始时间:
													</td>
													<td width="80px" align="left">
														<s:textfield name="start" size="18" id="startTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														结束时间:
													</td>
													<td align="left">
														<s:textfield name="end" size="18" cssStyle="width:150px"  id="endTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" cssClass="Wdate" />
														
													</td>		
													<td align="right" width="60px">
													每页:
													</td>		
													<td align="left">
														<s:select cssStyle="width:120px" list="%{#application.PageSizes}" name="size"></s:select>
													</td>										
													<td align="right" width="60px">
													</td>
													<td width="60px" align="left">
													<td rowspan="5" width="60px">
														<s:submit cssStyle="width:100px; height:30px;" value="查询"></s:submit>
														<input type="button" style="margin-top:20px;width:100px; height:30px;"  id="emailQunFa" value="邮件群发" />
													</td>
		 
												</tr>
												</s:form>
											</table>
									</td>
								</tr>
							</table>
							<table width="1340px" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
								<tr>
									<td bgcolor="#0084ff" align="center"  width="60px">
										<input type="checkbox" id="checkAllBox">
									</td>
									
									<td bgcolor="#0084ff" align="center" width="60px" style="color: #FFFFFF; font-weight: bold; cursor: pointer;" title="点击排序" onclick="orderby('id');">
										编号
									</td>
									<td bgcolor="#0084ff" align="center" width="90px" style="color: #FFFFFF; font-weight: bold">
										邮箱
									</td>								
									<td bgcolor="#0084ff" align="center" width="130px" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										订阅时间
									</td>
		
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}" status="status" >
									<tr>
										<td align="center" bgcolor="#F0FFF0" align="center" width="20px" style="font-weight: bold; cursor: pointer;">
											<input type="checkbox" name="item" value="<s:property value="#fc.loginname"  />">
										</td>							
										<td align="center" bgcolor="#F0FFF0" align="center" width="60px" style="font-weight: bold; cursor: pointer;">
                                                <s:property value="#status.index+1" />
                                        </td>
										<td align="center" bgcolor="#F0FFF0" align="center" width="60px" style="font-weight: bold; cursor: pointer;">
												<s:property value="#fc.email" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center" width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.remark" /> 
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

