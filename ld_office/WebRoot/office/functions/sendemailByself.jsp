<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<%@include file="/office/include.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>自建邮件群发</title>
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
       });

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

/* function submitEmailAction(email){
   var html="<table><tr><td class='label_search_td_play'>标题：</td><td><input type='text' class='input' id='titleEp' style='width: 200px;'/></td></tr><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><div class='button_wxz' onclick='submitEmailAction1(\""+email+"\");' style='margin-left: 370px;'>邮件发送</div></td></tr></table>";
   $("#JqAlert").html(html);
   JAlert_Progressbar_Dialog("JqAlert");
} */

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

    //邮件群发
	function sendemail(){
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
		url : "/office/updateSendemail.do",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data : "ids="+ids+"&remark=" + remark+"&zt="+zt,
		async : false, // 异步
		success : function(msg) {
			alert(msg);
		},
	});
}

function upload(){
	var file=$("#file").val();
	if(file==""){
	   alert("请选择文件！");
	   return false;
	}
	alert(file);
   	$("#mainformTwo").submit();
    return true;
}

function gopage(val){
    $("#pageIndex").val(val);//清空  
    $("#mainform").submit();
}
</script>
	</head>
	<body>
		<div id="JqAlert" title="温馨提示"
			style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);">
		</div>
		<div>
		<p>
			--&gt; 自建邮件服务器发送邮件
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		</div>
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getSendemail" namespace="/office"
				name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1450px">
								<tr align="left">
									<td>
										邮件地址:
										<s:textfield  name="email" size="20" />
									</td>
									<td align="right" width="80px">
										邮箱是否有效:
									</td>
									<td align="left">
										<s:select name="emailflag" list="#{'':'全部','0':'无效','1':'有效','2':'未检测','3':'未知'}" listKey="key" listValue="value"/>
									</td>
									<td align="right" width="80px">
										邮箱是否发送:
									</td>
									<td align="left">
										<s:select name="sendflag" list="#{'':'全部','0':'未发送','1':'待发送','2':'已发送','3':'发送失败'}" listKey="key" listValue="value"/>
									</td>
									<td align="left" width="60px">
														开始时间:
													</td>
													<td width="80px" align="left">
														<s:textfield name="startTime" size="18" 
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{startTime}" cssClass="Wdate" />
													</td>
													<td align="right" width="70px">
														结束时间:
													</td>
													<td align="left">
														<s:textfield name="endTime" size="18" cssStyle="width:150px" 
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
															My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										</td>
										<td align="right" width="80px"> 每页记录:</td>
									<td align="left">
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
										<s:hidden name="pageIndex"  value="1" id="pageIndex" />
										<s:set name="by" value="'createtime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
									<td>
										<s:submit cssStyle="width:65px; height:22px;" value="查询"></s:submit>
									</td>
									<td>
										<input type="button"  id="emailQunFa" onclick="sendemail()"  value="邮件群发" />
									</td>
								</tr>
								<tr>
						<%-- 			<s:form action="addemail" namespace="/office"
														enctype="multipart/form-data" name="mainformTwo"
														id="mainformTwo" theme="simple"
														onsubmit="submitonce(this);">
														<td align="right" width="60px">
															<input type="file" id="file" name="myFile" />
														</td>
														<td align="left" width="60px">
															<input type="button" value="上传"
																style="width: 60px; height: 30px;"
																onclick="return upload();" />
														</td>
								   </s:form> --%>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</s:form>
			 <s:form action="addEmail" namespace="/office" method ="post" enctype="multipart/form-data">
											文件:<input type="file" id="file" name="myFile"   size="20"/><input type="submit" value="提交" />
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
							<table width="1210px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td bgcolor="#0084ff" align="center">
										<input type="checkbox" id="checkAllBox">
									</td>
									
									<td bgcolor="#0084ff" align="center" width="60px"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('id');">
										编号
									</td>
									
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('email');" width="130px">
										邮件地址
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('emailflag');" width="130px">
										邮件是否有效
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('sendflag');" width="130px">
										邮件是否发送
									</td>
									<td align="center" onclick="orderby('isopen');"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="130px">
										邮件是否被打开
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										 width="680px">
										备注
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr >
										<td align="center" >
											<input type="checkbox" name="item" value="<s:property value="#fc.id"  />">
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.id" />
										</td>
										<td align="center" width="60px">
											<s:property value="#fc.email" />
										</td>
										<td align="center" width="90px">
										<s:if test="#fc.emailflag==0">
												无效
											</s:if>
											<s:elseif test="#fc.emailflag==1">
												有效
											</s:elseif>
											<s:elseif test="#fc.emailflag==2">
												未检测
											</s:elseif>
											<s:elseif test="#fc.emailflag==3">
												未知
											</s:elseif>
										</td>
										<td align="center" width="90px">
											<s:if test="#fc.sendflag==0">
												未发送
											</s:if>
											<s:elseif test="#fc.sendflag==1">
												待发送
											</s:elseif>
											<s:elseif test="#fc.sendflag==2">
												已发送
											</s:elseif>
											<s:elseif test="#fc.sendflag==3">
												发送失败
											</s:elseif>
										</td>
										<td align="center" width="90px">
										<s:if test="#fc.isopen==0">
												未打开
											</s:if>
											<s:elseif test="#fc.isopen==0">
												已打开
											</s:elseif>
										</td>
										<td align="center" width="680px">
											<s:property value="#fc.remark" />
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

