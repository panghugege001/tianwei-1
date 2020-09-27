	<%@page errorPage="500.jsp" pageEncoding="UTF-8"%>
		<%@taglib uri="/struts-tags" prefix="s"%>
		<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
		<s:if test="#session.operator!=null">
			<c:redirect url="/html/home/home.html" /> 
		</s:if>
		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
		<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>天威娱乐后台</title>
		<link href="/css/error.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
		<style type="text/css">
		<!--
		body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
		background-color: #ca6433;
		}

		#admin {
		width: 450px;
		padding-top: 104px;
		margin: auto;
		}

		.ctext {
		font-size: 12px;
		color: #000000;
		text-align: left;
		letter-spacing: 0.07em;
		}

		.ctexttitle {
		color: #ffffff;
		text-align: left;
		letter-spacing: 0.07em;
		font-weight: bold;
		text-align: center;
		}
		-->
		</style>
	<script>
	var onSubmit = function(token) {
		document.mainform.deviceID.value = token
		document.mainform.submit();
	};
	
	  var onloadCallback = function() {
		  var site_key = "";
		  $.ajax({ 
		        type: "post", 
		        url: "/office/getSite_key.do", 
		        cache: false, 
		        async: false,
		        data:{
		        },
		        success : function(data){
		        	site_key = data;
		        },
		        error: function(){alert("系统错误");},
				complete: function(){
				}
		  });
		  if(site_key != ""){
		    grecaptcha.render('recaptcha', {
		      'sitekey' : site_key,
		      'callback' : onSubmit
		    });
		  }else{
			  //onSubmit("");//没开启验证的域名
		  }
	  };
	</script>
	<script src='https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit' async defer></script>
		</head>

		<body>
		<div id="wrapper">
		<div id="admin">
		<div align="center"
		style=" border:1px dotted #cf9165; background:#f1d1a8">
		<div class="ctexttitle"
		style="background:#e76d06; height:30px; line-height:30px;">
		<span i18n_key="longduGlobalMgr">天威娱乐后台</span>
		</div>

		<s:form action="/operator/officeLogin.do" name="mainform" id="mainform"  theme="css_xhtml">
	  		<input type="hidden" id="deviceID" name="deviceID" />
			<table width="380" border="0" align="center" cellpadding="0"
			cellspacing="0" class="ctext">
			<tr>
				<td colspan="2"><span style="color:red"><s:fielderror></s:fielderror></span></td>
			</tr>
			<tr>
				<td width="109" height="33" style="padding-left:2px"><span i18n_key="username">用户名</span>:</td>
				<td colspan="2"><label> <s:textfield name="loginname" id="loginname" size="20" maxlength="12" cssStyle="border:1px solid #2a0e02" onfocus="true" /></label></td>
			</tr>
			<tr>
				<td width="109" height="33" style="padding-left:2px"><span i18n_key="password">密码</span>:</td>
				<td height="33" colspan="2"><s:password name="password" size="20" cssStyle="border:1px solid #2a0e02" maxlength="20" /></td>
			</tr>
			<%-- <tr>
				<td colspan="3" height="33" align="center" id="recaptchaTd">
          			<div class="g-recaptcha" data-sitekey="6LeBrzMUAAAAAM-gFw4d8FsOP4_jLHXDO9YN5K7z"></div>	
          		</td>
			<td height="33"><span style="padding-left:2px"><span i18n_key="verifyCode">验证码</span>:</span></td>
			<s:url action="validateCodeForIndex" namespace="/jsp" var="imgCode"></s:url>
			<td width="90" height="33"><s:textfield name="validateCode" size="8" cssStyle="border:1px solid #2a0e02" maxlength="10" onfocus="document.getElementById('imgCode').style.display='block';document.getElementById('imgCode').src='%{imgCode}?r='+Math.random();" /></td>
			<td width="79"><img id="imgCode" src="" title="如果看不清验证码，请点图片刷新" onclick="document.getElementById('imgCode').src='<c:url value='/jsp/validateCodeForIndex.do' />?r='+Math.random();" style="cursor: pointer;display:none;" /></td>
			</tr> --%>
			
			<%-- <tr>
			<td height="33"><span style="padding-left:2px"><span i18n_key="mobilelVerifyCode">手机验证码</span>:</span></td>
			<td width="90" height="33"><s:textfield name="smsValidPwd" size="8" cssStyle="border:1px solid #2a0e02" maxlength="10" /></td>
			<td width="129"><button type="button" class="btn btn-primary" onclick="sendSms();"><span i18n_key="getMobileCode">获取手机验证码</span></button></td>
			</tr> --%>
			
			<%-- <tr>
			<td height="33"><span style="padding-left:2px"><span i18n_key="emailVerifyCode">邮箱验证码</span>:</span></td>
			<td width="90" height="33"><s:textfield name="mailCode" size="8" cssStyle="border:1px solid #2a0e02" maxlength="10" /></td>
			<td width="129"><button type="button" class="btn btn-primary" onclick="sendEmail();"><span i18n_key="sendEmail">发送邮件</span></button></td>
			</tr> --%>
			<tr>
			<td width="109" height="33">&nbsp;</td>
			<td height="33" colspan="2"><button id="recaptcha"
			class="btn btn-primary">
			<span i18n_key="login">登陆</span>
			</button></td>
			</tr>
			<tr>
			<td><a id="a_i18n_en">English</a></td>
			<td><a id="a_i18n_cn">中文</a></td>
			</tr>
			</table>
		</s:form>
		</div>
		</div>
		</div>
		<c:import url="/commons/office_script.jsp" />
		</body>
		<script type="text/javascript">
		document.getElementById('loginname').focus();
		function sendEmail() {
		var loginname = document.getElementById('loginname').value;
		if (window.XMLHttpRequest) { // if Mozilla, Safari etc
		http_request = new XMLHttpRequest();
		if (http_request.overrideMimeType) {
		http_request.overrideMimeType("text/xml");
		}
		} else {
		if (window.ActiveXObject) { // if IE
		try {
		http_request = new ActiveXObject("Msxml2.XMLHTTP");
		}
		catch (e) {
		try {
		http_request = new ActiveXObject("Microsoft.XMLHTTP");
		}
		catch (e) {
		}
		}
		}
		}
		http_request.onreadystatechange = process;
		http_request.open("POST",'<c:url value='/office/sendLoginEmail.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
		}
		function process() {
		if (http_request.readyState == 4) {
		var chkResult = http_request.responseText;
		alert(chkResult);
		}
		}

		function sendSms(){
		var loginname = $("#loginname").val();

		if(loginname == null || loginname == ''){
		alert("请先输入用户名");
		return;
		}
		$.ajax({
		type: "post",
		url: "/office/sendSmsPwd.do",
		cache: false,
		async: false,
		data:{
		"loginname" : loginname
		},
		timeout:600000,

		success : function(data){console.log(data);alert(data);},
		error: function(){alert("系统错误");},
		complete: function(){
		/* $("#distributeBtn").attr("disabled", false); */
		}
		});
		}

		$("#a_i18n_en").click(function () {
		localStorage.setItem("i18n_language","en");
		i18n("en");
		});

		$("#a_i18n_cn").click(function () {
		localStorage.setItem("i18n_language","zh-CN");
		i18n("cn");
		});

		function i18n(language) {
		var i18nKeyArray = "";
		$("[i18n_key]").each(function() {
		i18nKey = $(this).attr("i18n_key");
		if( i18nKey != null && i18nKey != "" && i18nKey != "null") {
		i18nKeyArray = i18nKeyArray + i18nKey + ",";
		}
		});

		if(i18nKeyArray == "") {
		return;
		}
		i18nKeyArray = i18nKeyArray.substr(0,i18nKeyArray.length-1);

		$.ajax({
		type: "GET",
		async: false,
		url: "/i18n/getI18nMap.do",
		data: {"language":language,"i18nKeyArray":i18nKeyArray},
		dataType: "json", //表示返回值类型，不必须
		error: function(response) {
		alert(response.code);
		},
		success: function(response) {
		var code  = response.code;
		if(code == "0000") {
		var i18nList = response.data;
		if(i18nList == null || i18nList.length == 0) {
		return;
		}
		for (var index in i18nList) {
		$("[i18n_key="+ i18nList[index].key +"]").text(i18nList[index].value);
		}

		}else {
		alert(response.desc);
		}
		}
		});
		}

		initI18n();

		function initI18n() {
		var language = getLanguage();
		if(language != 'zh-CN' && language != 'zh') {
		localStorage.setItem("i18n_language","en");
		i18n("en");
		}
		}

		function getLanguage(){
		var language = localStorage.getItem("i18n_language");
		if(language != null) {
		return language;
		}else if (navigator.languages != undefined) {
		return navigator.languages[0];
		}else if(navigator.browserLanguage != undefined) {
		return navigator.browserLanguage;
		}else if(navigator.systemLanguage != undefined) {
		return navigator.systemLanguage;
		}else {
		return navigator.language;
		}
		}

		</script>
		</html>
