<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<%@include file="/office/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>pt8元优惠劵</title>
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
function changeE68Exten(num,id){
		var href = document.getElementById("_call2_"+id).href;
		if(href){
			var replaceStr = href.substring(0,href.length-4)+num;
			document.getElementById("_call2_"+id).href = replaceStr;
		}
}
function changeQyExten(num,id){
	var href = document.getElementById("_call_"+id).href;
	if(href){
		var replaceStr = href.substring(0,href.length-3)+num;
		document.getElementById("_call_"+id).href = replaceStr;
	}
}
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
        height:320,
        width:520,
        modal:true
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
   var action = "/office/sendPt8CouponSingleEmailAction.do";
   if(title == ""){
         alert("标题不能为空！"); 
         return;
	}
	if(remark == ""){
         alert("内容不能为空！"); 
         return;
	}
    var xmlhttp = new Ajax.Request(action,
		 {    
		       method: 'post',
		       parameters:"email="+email+"&title="+title+"&remark="+remark+"&r="+Math.random(),
		       onComplete: responseMethod  
	      }
	 );
	 buttonCancel("JqAlert");
}

function emailHtmlAll(){
   var html="<table><tr><td class='label_search_td_play'>标题：</td><td><input type='text' class='input' id='titleEp' style='width: 200px;'/></td></tr><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><div class='button_wxz' onclick='emailHtmlAll1();' style='margin-left: 370px;'>邮件发送</div></td></tr></table>";
   $("#JqAlert").html(html);
   JAlert_Progressbar_Dialog("JqAlert");

}

function emailHtmlAll1(){
   var title=$("#titleEp").val();
   var remark=$("#remark").val();
   var action = "/office/getSubmitEmailAllAction.do";
   if(title == ""){
         alert("标题不能为空！"); 
         return;
	}
	if(remark == ""){
         alert("内容不能为空！"); 
         return;
	}
    var xmlhttp = new Ajax.Request(action,
		 {    
		       method: 'post',
		       parameters:"title="+title+"&remark="+remark+"&r="+Math.random(),
		       onComplete: responseMethod  
	      }
	 );
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


function submitPhoneAction1(phone){
   if(phone==""){
      alert("电话号码不能为空！");
      return;
   }
   var title=$("#titleEp").val();
   var remark=$("#remark").val();
   if(title == ""){
         alert("标题不能为空！"); 
         return;
	}
	if(remark == ""){
         alert("内容不能为空！"); 
         return;
	}
	var action = "/office/getSubmitPhoneAction.do";
    var xmlhttp = new Ajax.Request(action,
		 {    
		       method: 'post',
		       parameters:"phone="+phone+"&title="+title+"&remark="+remark+"&r="+Math.random(),
		       onComplete: responseMethod  
	      }
	 );
	 buttonCancel("JqAlert");
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
   $("#JqAlertTwo2").html("<table width='300px'><tr><td><img src='${pageContext.request.contextPath}/images/jdtiao.gif' height='200px;' width='280px;'/></td></tr></table>"); 
   JAlertJdtDialog("JqAlertTwo2");
   var action = "/office/sendPt8CouponEmailAction.do";
         var xmlhttp = new Ajax.Request(    
		   action,
		  {    
		       method: 'post',
		       parameters:"",
		       onComplete: responseMethodTwo  
	      }
	     );
}

function phoneAll(){
   $("#JqAlertTwo2").html("<table width='300px'><tr><td><img src='${pageContext.request.contextPath}/images/jdtiao.gif' height='200px;' width='280px;'/></td></tr></table>"); 
   JAlertJdtDialog("JqAlertTwo2");
   var action = "/office/getSendPhoneAction.do";
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

</script>
	<body>
		<div id="JqAlert" title="温馨提示"
			style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);">
		</div>
		<div id="JqAlertTwo" title="温馨提示"
			style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);">
		</div>
		<div id="JqAlertTwo2" title="温馨提示">
		</div>
		<div>
			记录 --&gt; pt8元优惠劵
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
											<table width="1250px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<tr align="left">
									<td colspan="12" align="left">
										<s:form action="queryPtCoupon" namespace="/office"
											name="mainform" id="mainform" theme="simple">
											<input type="hidden" name="by" value="createtime" id="by" />
											<input type="hidden" name="order" value="<s:property value='%{#request.order}' />" id="order" />
											<table border="0" cellpadding="0" cellspacing="0">
												<tr>
													<td align="right" width="60px">邮箱:</td>
													<td><s:textfield name="email" size="15" /></td>
													
													<td align="right" width="60px">
														是否使用:
													</td>
													<td width="110px" align="left">
														<s:select cssStyle="width:150px" name="isreg"
															list="%{#application.PtCouponUseType}" listKey="code"
															listValue="text" emptyOption="true" />
													</td>
													
													<td align="right" width="60px">
														每页:
													</td>
													<td align="left">
														<s:select cssStyle="width:120px"
															list="%{#application.PageSizes}" name="size"></s:select>
													</td>
													
													
													<td rowspan="2" width="60px">
														<s:submit cssStyle="width:100px; height:30px;" value="查询"></s:submit>
														<input type="button" onclick="emailAll();" value="优惠劵批量发送（邮箱）" />
													</td>
												</tr>
												<tr>
													<td align="right" width="60px">
														开始时间:
													</td>
													<td width="80px" align="left">
														<s:textfield name="start" size="18"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="60px">
														结束时间:
													</td>
													<td align="left">
														<s:textfield name="end" size="18" cssStyle="width:150px"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
														
													</td>
													
													<td align="right" width="60px">邀请码</td>
													<td><s:textfield name="code" size="15" /></s:form></td>
												</tr>
												<tr>
												<td></td>
													<s:form action="addPtCoupon" namespace="/xima"
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
															href="${pageContext.request.contextPath}/files/ptCoupon.xls">模板下载</a>
													</td>
												</tr>
											</table>
									</td>
								</tr>
							</table>
							<table width="1250px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<tr>
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('id');">
										编号
									</td>
									
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold" title="点击排序"
										onclick="orderby('type');">
										是否使用
									</td>
									
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										邮箱
									</td>
									
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createtime');">
										创建时间
									</td>
									<td bgcolor="#0084ff" align="center" width="130px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;">
										优惠码
									</td>
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										发送邮箱
									</td>
									<td bgcolor="#0084ff" align="center" width="90px"
										style="color: #FFFFFF; font-weight: bold">
										备注
									</td>
									<!-- <td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold">
										操作
									</td> -->
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.id" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.type==0">
												   未使用
												</s:if>
											<s:else>
												   已使用
												</s:else>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.email" />
										</td>
										
										
										
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.createtime" />
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.code" />
										</td>
										
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc.email !=''">
												<input type="button"
													onclick="submitEmailAction('${fc.email}');" value="发送邮箱" />
											</s:if>
										</td>
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<s:property value="#fc.remark" />
										</td>
										<%-- <td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											<a target="_blank"
												href="/office/getCustomerPno.do?id=<s:property value="#fc.id"/>"
												style="cursor: pointer" title="">修改</a>
										</td> --%>
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

