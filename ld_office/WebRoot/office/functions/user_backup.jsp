<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户列表</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

var http_request;
var checkBtn;
var userName;
function queryCredit(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

function queryAgCredit(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteAgCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}
//agin
function queryAgInCredit(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteAgInCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

//sb
function querySbCredit(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteSbCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

//bbin
function queryBbinCredit(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteBbinCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

function queryKenoCredit(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteKenoCredit.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

function queryKenoCredit2(loginname,btn) {
	checkBtn=btn;
	userName=loginname;
	checkBtn.disabled=true;
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
		http_request.open("POST",'<c:url value='/office/queryRemoteKenoCredit2.do' />',true);
		http_request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		http_request.send("loginname="+loginname);
}

function querySixLotteryCredit(loginname,btn){
    var action="/office/queryRemoteSixLotteryCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryEbetCredit(loginname,btn){
    var action="/office/queryEbetCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryGPICredit(loginname,btn){
    var action="/office/queryGPICredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}
function process() {
	if (http_request.readyState == 4) {
		var chkResult = http_request.responseText;
		alert("用户"+userName+"的总额度:"+chkResult);
		checkBtn.disabled=false;
	}
}


function changeUserStatus(_username,_isenable){
	var action="/office/enableUser.do";
	var remark=window.prompt("启用/禁用会员原因（不可为空）,否则请点取消:","");
	if(remark.indexOf('&')!=-1||remark.indexOf('?')!=-1){
		alert("备注内容不可以包含如下符号：? 、& ");
		return;
	}
	if(remark){
		remark=remark.Trim();
		if(remark){
			var xmlhttp = new Ajax.Request(    
					action,
			        {    
			            method: 'post',
			            parameters:"remark="+remark+"&isEnable="+_isenable+"&loginname="+_username+"&r="+Math.random(),
			            onComplete: responseMethod  
			        }
		    	);
		}else{
			alert("备注不可为空");
		}
		 
	}	
}

function queryNewPtCredit(loginname,btn){
    var action="/office/queryNewPtCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}


function queryPtCredit(id){
    var action="/office/queryPtCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"id="+id+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function changeRate(_username,_platform){
	var rate=window.prompt("修改反水率（不可为空,3位小数点,且反水范围0.001-0.015）,否则请点取消:","");
	var action="/office/editRateUser.do";
	var reg = "^0.0[0-1]{1}[0-9]{1}$";
	var re = new RegExp(reg);
	if(rate!=null && rate!="" && rate.search(re)!=-1 && rate>0 && rate <=0.015){
		rate=rate.Trim();
		var xmlhttp = new Ajax.Request(    
					action,
			        {    
			            method: 'post',
			            parameters:"rate="+rate+"&loginname="+_username+"&r="+Math.random()+"&platform="+_platform,
			            onComplete: responseMethod  
			        }
		    	);
	}else{
		alert("反水率格式不正确");
	}
}

function limitMethod(loginName){
    var limit=$("#limit"+loginName).val();
     if(loginName==""){
       alert("玩家账号不能为空！");
       return;
    }
    if(limit==""){
       alert("限额不能为空！");
       return;
    }
    var action="/office/limitMethod.do";
    var xmlhttp = new Ajax.Request(    
		 action,
	     {    
			 method: 'post',
			 parameters:"limit="+limit+"&loginname="+loginName+"&time="+new Date(),
			 onComplete: responseMethod  
	     }
    );
}

function responseMethod(data){
	alert(data.responseText);
	document.getElementById("mainform").submit();
}

String.prototype.Trim=function()
{
        return this.replace(/(^\s*)|(\s*$)/g,"");
}

function _updateUsersForCS(){
	if(!confirm("确认更新客服标示？")){
		return ;
	}
	var ele = $(this);
	$("#updateUsersForCSBtn").attr("disabled", true);
	$.post("/office/updateUsersForCS.do",function(){
		$("#updateUsersForCSBtn").removeAttr("disabled");
		alert("更新完毕！");
	});
}

//解除pt优惠限制
function relievePtLimit(loginname){
	if(!confirm("确定解除"+loginname+"限制？")){
		return ; 
	}
 	$.post("/office/relievePtLimit.do",{"loginname":loginname},function(data){
		alert(data);
	});
}

function changeIntro(loginname){
	var intro = window.prompt("是否确认修改"+loginname+"的推荐码？","");
	if(intro){
		$.post("/office/editUser.do",{"loginname":loginname,"intro":intro},function(data){
			alert(data) ;
		});
	}
}

function changePartner(loginname){
	var partner = window.prompt("是否确认修改"+loginname+"的推荐码？","");
	if(partner){
		$.post("/office/editAgentPartner.do",{"loginname":loginname,"partner":partner},function(data){
			alert(data) ;
		});
	}
}
</script>
<s:form action="queryUserBackup" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
账户 --> 备份用户列表<a href="javascript:history.back();"> <font color="red">上一步</font></a>
</div>
<c:if test="${sessionScope.operator.authority eq  'sale_manager' || sessionScope.operator.authority eq 'boss' }">
	<!-- <input type="button" value="更新客服标识码" onclick="_updateUsersForCS();" id="updateUsersForCSBtn"/> -->
</c:if>
<div id="excel_menu">
<font color="red">[当你输入了会员帐号，时间不再起效]</font>
<table>
<tr align="left">
<td>用户类型:<s:select name="roleCode" list="%{#application.UserRole}" listKey="code" listValue="text" emptyOption="true"/></td>
<td>
会员等级:<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/>
</td>
<!-- <td>最后登陆时间:<s:select name="listLoginDay" list="%{#application.DateIntervalType}" listKey="code" listValue="text" emptyOption="true"></s:select></td> -->
<td>
开始时间: <s:textfield name="start" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
</td>
<td>
结束时间:<s:textfield name="end" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
</td>
<td>帐号:<s:textfield name="loginname" size="10"/></td>
<td>上级代理:<s:textfield name="agent" size="10"/></td>
</tr>
<tr align="left">
<td>代理网址:<s:textfield name="referWebsite" size="10"/></td>
<td>昵称:<s:textfield name="aliasName" size="12"/></td>
<td>真实姓名:<s:textfield name="accountName" size="8"/></td>
<td>邮箱:<s:textfield name="email" size="15"/></td>
<td>电话:<s:textfield name="phone" size="15"/></td>
<td>会员状态:<s:select name="status" list="#{'0':'启用','1':'禁用'}" emptyOption="true"/></td>
</tr>
<tr align="left">
<td>是否存款:<s:select name="isCashin" list="#{'0':'已存款','1':'未存款'}" emptyOption="true"/></td>
<td>开户IP:<s:textfield name="registerIp" size="10"/></td>
<td>用户ID:<s:textfield name="id" size="10"/></td>
<td>来源网址:<s:textfield name="howToKnow" size="10"/></td>
<td>每页:<s:select list="%{#application.PageSizes}" name="size"></s:select></td>
<td>警告等级:<s:select list="%{#application.WarnLevel}" listKey="code" listValue="text" name="warnflag" emptyOption="true"></s:select></td>
<td><s:submit value="查询"></s:submit></td>
</tr>
<tr align="left">
<td>推荐码:<s:textfield name="intro" size="10"/></td>
<td>邀请码:<s:textfield name="invitecode" size="10"/></td>
<td>QQ:<s:textfield name="qq" size="10"/></td>
<td>农行随机码:<s:textfield name="randnum" size="10" maxlength="4"/></td>
<td>代理推荐码:<s:textfield name="partner" size="10"/></td>
<td>最后登录ip:<s:textfield name="lastLoginIp" size="10"/></td>
</tr>
</table>

<s:hidden name="pageIndex" value="1"></s:hidden>
<s:hidden name="order" value="desc"></s:hidden>
<s:hidden name="by" value="createtime"></s:hidden>
</div>

<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">帐号</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">警告等级</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">反水率</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">最高洗码优惠</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">客户操作系统</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">上级代理</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">推荐码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">邀请码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">e路发额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">平台额度</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">类型 </td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">真实姓名 </td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">代理网址 </td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">开户时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">开户IP</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">开户地区</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">上次登录时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">上次登录IP</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">上次登录地区</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">登录次数</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">操作</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">日限额</td>
            </tr>
            
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
              <s:url action="getUserhavinginfoBackup" namespace="/office" var="getUserhavinginfourl"><s:param name="loginname" value="%{#fc.loginname}"/></s:url>
              <a target="_blank" href='<s:property value="%{getUserhavinginfourl}"/>' title="点击查看修改当前用户的基本信息"> <s:property value="#fc.loginname"/></a>
             </td>
              <s:url escapeAmp="false" value="/office/functions/setWarnLevel.jsp" var="setWarnLevelUrl"><s:param name="loginname" value="#fc.loginname" /><s:param name="warnflag" value="#fc.warnflag" /><s:param name="warnremark" value="#fc.warnremark" /></s:url>
              <td 
              <c:choose>
             	<c:when test="${fc.warnflag eq 0}">bgcolor="#e4f2ff"</c:when>
             	<c:when test="${fc.warnflag eq 1}">bgcolor="#f3deac"</c:when>
             	<c:when test="${fc.warnflag eq 2}">bgcolor="#fb8d8d"</c:when>
             	<c:otherwise>bgcolor="#9beda3"</c:otherwise>
             </c:choose>
               align="center" style="font-size:13px;">
              	<a target="_blank" href="<s:property value='setWarnLevelUrl'/>" ><s:property value="@dfh.model.enums.WarnLevel@getText(#fc.warnflag)"/></a>
              </td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
               	<!-- <table>
               		<tr><td>EA:<a href="javascript:changeRate('${fc.loginname }','ea');"><fmt:formatNumber pattern="0.000" value="${fc.rate}"></fmt:formatNumber></a></td>
               		<td>BBIN:<a href="javascript:changeRate('${fc.loginname }','bbin');"><fmt:formatNumber pattern="0.000" value="${fc.bbinrate}"></fmt:formatNumber></a></td>
               		</tr>
               		<tr>
               		<td>AG:<a href="javascript:changeRate('${fc.loginname }','ag');"><fmt:formatNumber pattern="0.000" value="${fc.agrate}"></fmt:formatNumber></a></td>
               		<td>KENO:<a href="javascript:changeRate('${fc.loginname }','keno');"><fmt:formatNumber pattern="0.000" value="${fc.kenorate}"></fmt:formatNumber></a></td>
               		</tr>
               		<tr>
               		<td>AGIN:<a href="javascript:changeRate('${fc.loginname }','agin');"><fmt:formatNumber pattern="0.000" value="${fc.aginrate}"></fmt:formatNumber></a></td>
               		<td>SB:<a href="javascript:changeRate('${fc.loginname }','sb');"><fmt:formatNumber pattern="0.000" value="${fc.sbrate}"></fmt:formatNumber></a></td>
               		</tr>
               		<tr>
               		<td>PT:<a href="javascript:changeRate('${fc.loginname }','pt');"><fmt:formatNumber pattern="0.000" value="${fc.skyrate}"></fmt:formatNumber></a></td>
               		<td></td>
               		</tr>
               	</table> -->
               </td>
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
               	<!-- <table>
               		<tr><td>EA:${fc.earebate}</td>
               		<td>BBIN:${fc.bbinrebate}</td>
               		</tr>
               		<tr>
               		<td>AG:${fc.agrebate}</td>
               		<td>KENO:${fc.kenorebate}</td>
               		</tr>
               		<tr>
               		<td>AGIN:${fc.aginrebate}</td>
               		<td>SB:${fc.sbrebate}</td>
               		</tr>
               		<tr>
               		<td>PT:${fc.ptrebate}</td>
               		<td>
               		  <s:url action="getUserUpateinfo" namespace="/office" var="getUserUpateinfourl"><s:param name="loginname" value="%{#fc.loginname}"/></s:url>
               		<a target="_blank" href='<s:property value="%{getUserUpateinfourl}"/>' title="点击查看修改当前用户的基本信息">修改优惠</a></td>
               		</tr>
               	</table> -->
               </td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.clientos"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.agent"/></td>
              <c:choose>
              	<c:when test="${fc.role eq 'AGENT'}">
	              		<c:choose>
	              		<c:when test="${sessionScope.operator.authority eq  'sale_manager' || sessionScope.operator.authority eq 'boss' }">
	              			<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a href="javascript:changePartner('${fc.loginname }');">Aedit:<s:property value="#fc.partner"/></a></td>
	              		</c:when>
	              		<c:otherwise>
		             		<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.partner"/></td>
		            	</c:otherwise>
	              		</c:choose>
	              	</c:when>
              	<c:otherwise>
              		<c:choose>
		              	<c:when test="${sessionScope.operator.authority eq  'sale_manager' || sessionScope.operator.authority eq 'boss' }">
		              		<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a href="javascript:changeIntro('${fc.loginname }');">edit:<s:property value="#fc.intro"/></a></td>
		              	</c:when>
		              	<c:otherwise>
		             		<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.intro"/></td>
		              	</c:otherwise>
	              </c:choose>
              	</c:otherwise>
              </c:choose>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.invitecode"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.credit"/></td>
          
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
              <!-- <table>
              	<tr><td><input type="button" id="checkBtn" value="EA" onclick="queryCredit('<s:property value="#fc.loginname"/>',this)"/> </td>
              	<td><input type="button" id="checkagBtn" value="BBIN" onclick="queryBbinCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	</tr>
              	<tr>
<%--               	<td><input type="button" id="checkagBtn" value="AG" onclick="queryAgCredit('<s:property value="#fc.loginname"/>',this)"/></td> --%>
              	<td><input type="button" id="checkagBtn" value="EBET" onclick="queryEbetCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	<td><input type="button" id="checkagBtn" value="KENO" onclick="queryKenoCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	</tr>
              	<tr>
              	<td><input type="button" id="checkagBtn" value="AGIN" onclick="queryAgInCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	<td><input type="button" id="checkagBtn" value="SB" onclick="querySbCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	</tr>
              	<tr>
              	<td><input type="button" id="checkagBtn" value="PT" onclick="queryPtCredit('<s:property value="#fc.id"/>',this)"/></td>
              	<td><input type="button" id="checkagBtn" value="NEWPT" onclick="queryNewPtCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	</tr>
              	<tr>
              	<td><input type="button" id="checkagBtn" value="KENO2" onclick="queryKenoCredit2('<s:property value="#fc.loginname"/>',this)"/></td>
              	<td><input type="button" id="checkagBtn" value="六合彩" onclick="querySixLotteryCredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	</tr>
              	<tr>
              	<td><input type="button" id="checkagBtn" value="GPI" onclick="queryGPICredit('<s:property value="#fc.loginname"/>',this)"/></td>
              	
              	</tr>
              </table> -->
              </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:if test="#fc.role=='AGENT'">代理</s:if><s:else><s:property value="@dfh.model.enums.VipLevel@getText(#fc.level)"/></s:else></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
			<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
				<s:property value="#fc.accountName"/>
			</s:if>
			<s:else>
				<s:property value="#fc.accountName.substring(0,1)+'**'"/>
			</s:else> --%>
			<s:property value="#fc.accountName"/>
</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.referWebsite"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a href="http://ip.chinaz.com/?IP=<s:property value='#fc.registerIp'/>&action=2" target="new" title="点击查询"><s:property value="#fc.registerIp"/></a></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.area"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.lastLoginTime"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a href="http://ip.chinaz.com/?IP=<s:property value='#fc.lastLoginIp'/>&action=2" target="new" title="点击查询"><s:property value="#fc.lastLoginIp"/></a></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.lastarea"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.loginTimes"/></td>
              <s:url escapeAmp="false" value="/office/functions/setLevel.jsp" var="setLevelUrl"><s:param name="loginname" value="#fc.loginname" /><s:param name="level" value="#fc.level" /></s:url>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><!-- <a target="_blank" href="<s:property value='setLevelUrl'/>">设定等级</a>&nbsp;|&nbsp;<s:if test="#fc.flag==@dfh.utils.Constants@ENABLE"><a href="javascript:changeUserStatus('${fc.loginname }',false)">禁用</a></s:if><s:else><a href="javascript:changeUserStatus('${fc.loginname }',true)">启用</a></s:else>
              		<c:if test="${fc.shippingcodePt !='' &&  fc.shippingcodePt != null}"><a href="javascript:relievePtLimit('${fc.loginname}');">&nbsp;|&nbsp;解除pt优惠限制</a></c:if> -->
              </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:if test="#fc.flag==@dfh.utils.Constants@ENABLE">正常</s:if><s:elseif test="#fc.flag==2">密码被锁</s:elseif><s:else><font color='red'>禁用</font></s:else></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
                     <!-- <select name="limit" id="limit${fc.loginname}" onchange="limitMethod('${fc.loginname}');">
                          <c:if test="${fc.creditlimit==-1}">
                               <option value="-1">
									无限制
								</option>
								<option value="0">
									最高0
								</option>
								<option value="1000">
									最高1000
								</option>
								<option value="5000">
									最高5000
								</option>
                          </c:if>
                           <c:if test="${fc.creditlimit==0}">
                               <option value="0">
									最高0
								</option>
                               <option value="-1">
									无限制
								</option>
								<option value="1000">
									最高1000
								</option>
								<option value="5000">
									最高5000
								</option>
                          </c:if>
                           <c:if test="${fc.creditlimit==1000}">
                               <option value="1000">
									最高1000
								</option>
                               <option value="-1">
									无限制
								</option>
								<option value="0">
									最高0
								</option>
								<option value="5000">
									最高5000
								</option>
                          </c:if>
                           <c:if test="${fc.creditlimit==5000}">
                               <option value="5000">
									最高5000
								</option>
                               <option value="-1">
									无限制
								</option>
								<option value="0">
									最高0
								</option>
								<option value="1000">
									最高1000
								</option>
                          </c:if>
                 	</select> -->
                 </td>              
            </tr>
  	 	 </s:iterator>
            <tr>
              <td colspan="22" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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

