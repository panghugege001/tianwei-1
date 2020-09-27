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
function queryBitCredit(loginname,btn){
	var action="/office/queryBitCredit.do";
	var xmlhttp = new Ajax.Request(
		action,
		{
			method: 'post',
			parameters:"loginname="+loginname+"&r="+Math.random(),
			onComplete: responseMethod
		}
	);
}
$('html').bind('keypress', function(e){
   if(e.keyCode == 13){
      return false;
   }
});

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

function _editRemark(btn){
    var result = new Array();
    var ids ;
    $("[name = item][checked]:checkbox").each(function(){
        result.push($(this).attr("value"));
    });
    var len = result.length ;
    if(len>0){
        var ivr=window.prompt("电销功能：共选中"+len+"条数据，确认执行？","");
        if(ivr || ivr==""){
            btn.disabled=true;
            var ids = result.join(",") ;
            var agent = $("#agent").val();
            var intro = $("#intro").val();

            if(agent !=null && agent != ''){
                $.post("/office/executeEditRemark.do",{"ids":ids,"ivr":ivr,"by":agent},function(data){
                    btn.disabled=false;
                    alert(data);
                });
            }else if (intro!=null && intro !='' ){
                $.post("/office/executeEditRemark.do",{"ids":ids,"ivr":ivr,"intro":intro},function(data){
                    btn.disabled=false;
                    alert(data);
                });

            }else {

                alert("电销代码必须输入");
            }

        }
    }else{
        alert("请选择需要执行的数据");
    }
}
function queryRedRainCredit(loginname,btn){
    var action="/office/queryRedRainCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
}
function queryKyqpCredit(loginname,btn){
    var action="/office/queryKyqpCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
}

function queryVRCredit(loginname,btn){
    var action="/office/queryVRCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
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
function queryAginCredit(loginname,btn){
    var action="/office/queryAginCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

//bbin
function queryBbinCredit(loginname,btn){
    var action="/office/queryBbinCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
}
function queryPBCredit(loginname,btn){
    var action="/office/queryPBCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
}
function queryFanyaCredit(loginname,btn){
    var action="/office/queryFanyaCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
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

function queryTTGCredit(loginname,btn){
    var action="/office/queryTTGCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryQTCredit(loginname,btn){
    var action="/office/queryQTCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function query761Credit(loginname,btn){
	var action="/office/query761Credit.do";
	var xmlhttp = new Ajax.Request(
			action,
			{
				method: 'post',
				parameters:"loginname="+loginname+"&r="+Math.random(),
				onComplete: responseMethod
			}
	);
}

function queryNTCredit(loginname,btn){
    var action="/office/queryNTCredit.do";
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

function queryEBetAppCredit(loginname,btn){
	var action="/office/queryRemoteEBetAppCredit.do";
	var xmlhttp = new Ajax.Request(
			action,
			{
				method: 'post',
				parameters:"loginname="+loginname+"&r="+Math.random(),
				onComplete: responseMethod
			}
	);
}

function getNTwoBalance(loginname,btn){
	var action="/office/getNTwoBalance.do";
	var xmlhttp = new Ajax.Request(
			action,
			{
				method: 'post',
				parameters:"loginname="+loginname+"&r="+Math.random(),
				onComplete: responseMethod
			}
	);
}
function querySbaCredit(loginname,btn){
	var action="/office/querySbaCredit.do";
	var xmlhttp = new Ajax.Request(
			action,
			{
				method: 'post',
				parameters:"loginname="+loginname+"&r="+Math.random(),
				onComplete: responseMethod
			}
	);
}
function queryMWGCredit(loginname,btn){
	var action="/office/queryMWGCredit.do";
	var xmlhttp = new Ajax.Request(
			action,
			{
				method: 'post',
				parameters:"loginname="+loginname+"&r="+Math.random(),
				onComplete: responseMethod
			}
	);
}
function queryAgentSlotAccount(loginname,btn){
    var action="/office/queryAgentSlotAccount.do";
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

function querySwCredit(loginname,btn){
    var action="/office/querySwCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryDtCredit(loginname,btn){
    var action="/office/queryDtCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
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

function queryMGCredit(loginname,btn){
	var action="/office/queryMGCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryFishCredit(loginname,btn){
    var action="/office/queryFishCredit.do";
    var xmlhttp = new Ajax.Request(
        action,
        {
            method: 'post',
            parameters:"loginname="+loginname+"&r="+Math.random(),
            onComplete: responseMethod
        }
    );
}

function querySlotCredit(loginname,btn){
    var action="/office/querySlotCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryPNGCredit(loginname,btn){
	var action="/office/queryPNGCredit.do";
	var xmlhttp = new Ajax.Request(    
		action,
			   {    
			      method: 'post',
			      parameters:"loginname="+loginname+"&r="+Math.random(),
			      onComplete: responseMethod  
		}
     );
}

function queryChessCredit(loginname,btn){
	var action="/office/queryChessCredit.do";
	var xmlhttp = new Ajax.Request(
		action,
		{
			method: 'post',
			parameters:"loginname="+loginname+"&r="+Math.random(),
			onComplete: responseMethod
		}
	);
}

$(function () {
    $("#checkAllBox").bind("click", function () {
    	if($(this).attr("checked") == "checked" || $(this).attr("checked") == true){
        	$("[name = item]:checkbox").attr("checked", true);
    	}else{
    		$("[name = item]:checkbox").attr("checked", false);
    	}
    });
    $("[name = item]:checkbox").bind("click", function () {
    	if($(this).attr("checked") != "checked"){
    		$("#checkAllBox").attr("checked", false);
    	}
    	var flag = true ;
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == undefined){
    			flag = false ;
    		}else{
    			flag = flag && $(this).attr("checked");
    		}
    	});
    	if(flag){
    		$("#checkAllBox").attr("checked", true);
    	}
    });
});
function _executeCall(btn){
	var result = new Array();
	var ids ;
	$("[name = item][checked]:checkbox").each(function(){
		result.push($(this).attr("value"));
	});
	var len = result.length ;
	if(len>0){
		var ivr=window.prompt("共选中"+len+"条数据，确认执行？播放语音的需填入语音ID","");
		if(ivr || ivr==""){
			btn.disabled=true;
    		var ids = result.join(",") ;
    		var type = "${sessionScope.operator.type}";
    		if(type !=null && type != '' ){
    			$.post("/office/executeCallUserTM.do",{"ids":ids,"ivr":ivr},function(data){
	    			btn.disabled=false;
	    			alert(data);
	    		});
    		}
    		var cs = "${sessionScope.operator.cs}";
			if(cs !=null && cs != '' ){
	    		$.post("/office/executeCall.do",{"ids":ids,"ivr":ivr},function(data){
	    			btn.disabled=false;
	    			alert(data);
	    		});
    		}
    	}
	}else{
		alert("请选择需要执行的数据");
	}
}

//临时处理
function queryPTDetail(loginname,btn){
	var action="/office/queryPTDetail.do";
	var xmlhttp = new Ajax.Request(
			action,
			{
				method: 'post',
				parameters:"loginname="+loginname+"&r="+Math.random(),
				onComplete: responseMethod
			}
	);
}

</script>
<s:form action="queryUser" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
<span i18n_key="account">账户</span> --> <span i18n_key="userList">用户列表</span><a href="javascript:history.back();"> <font color="red" i18n_key="previousStep">上一步</font></a>
</div>
<c:if test="${sessionScope.operator.authority eq  'sale_manager' || sessionScope.operator.authority eq 'boss' || sessionScope.operator.username eq 'toby' || sessionScope.operator.username eq 'yan751028'}">
	<button onclick="_updateUsersForCS();" id="updateUsersForCSBtn" i18n_key="updateCSIDNumber">更新客服标识码</button>
</c:if>
	<button onclick="_executeCall(this);" i18n_key="batchCall">群呼</button>
	<input type="button" value="备注清空" onclick="_editRemark(this);"/>
<div id="excel_menu">
<font color="red" i18n_key="userAccountNotice">[当你输入了会员帐号，时间不再起效]</font>
<table>
<tr align="left">
	<td><span i18n_key="userType">用户类型</span>:<s:select name="roleCode" list="%{#application.UserRole}" listKey="code" listValue="text" emptyOption="true"/></td>
	<td><span i18n_key="userLevel">会员等级</span>:<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/></td>
	<!-- <td>最后登陆时间:<s:select name="listLoginDay" list="%{#application.DateIntervalType}" listKey="code" listValue="text" emptyOption="true"></s:select></td> -->
	<td><span i18n_key="startTime">开始时间</span>: <s:textfield name="stringStartTime" size="15" onfocus="WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{stringStartTime}" /></td>
	<td><span i18n_key="endTime">结束时间</span>:<s:textfield name="stringEndTime" size="15" onfocus="WdatePicker({startDate:'%y-%M-{%d+1} 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{stringEndTime}" /></td>
	<td><span i18n_key="account">帐户</span>:<s:textfield name="loginname" size="10"/></td>
	<td><span i18n_key="superiorAgent">上级代理</span>:<s:textfield name="agent" id="agent" size="10"/></td>
</tr>
<tr align="left">
	<td><span i18n_key="agentWebsite">代理网址</span>:<s:textfield name="referWebsite" size="10"/></td>
	<%-- <td>昵称:<s:textfield name="aliasName" size="12"/></td> --%>
	<td><span i18n_key="realName">真实姓名</span>:<s:textfield name="accountName" size="8"/></td>
	<td><span i18n_key="email">邮箱</span>:<s:textfield name="email" size="15"/></td>
	<td><span i18n_key="phone">电话</span>:<s:textfield name="phone" size="15"/></td>
	<td><span i18n_key="memberStatus">会员状态</span>:<s:select name="status" list="#{'0':'启用','1':'禁用'}" emptyOption="true"/></td>
	<td><span i18n_key="birthday">生日</span>:<s:textfield name="birthday" size="12" onfocus="WdatePicker({dateFmt:'MM-dd'})" My97Mark="false" value="%{birthday}" /></td>
</tr>
<tr align="left">
<td><span i18n_key="whetherDeposit">是否存款</span>:<s:select name="isCashin" list="#{'0':'已存款','1':'未存款'}" emptyOption="true"/></td>
<td><span i18n_key="opening">开户</span>IP:<s:textfield name="registerIp" size="10"/></td>
<%-- <td>用户ID:<s:textfield name="id" size="10"/></td> --%>
<td><span i18n_key="sourceUrl">来源网址</span>:<s:textfield name="howToKnow" size="10"/></td>
<td><span i18n_key="eachPage">每页</span>:<s:select list="%{#application.PageSizes}" name="size"></s:select></td>
<td><span i18n_key="warnLevel">警告等级</span>:<s:select list="%{#application.WarnLevel}" listKey="code" listValue="text" name="warnflag" emptyOption="true"></s:select></td>
<td><span i18n_key="phoneStatus">电话状态</span>:<s:select name="sms" list="#{'0':'未拨打','1':'已接通','2':'未接通','3':'被挂断','4':'空号'}"  emptyOption="true"/></td>
</tr>
<tr align="left">
<td><span i18n_key="recommendationNumber">推荐码</span>:<s:textfield id="intro" name="intro" size="10"/></td>
<td><span i18n_key="invitationNumber">邀请码</span>:<s:textfield name="invitecode" size="10"/></td>
<td>QQ:<s:textfield name="qq" size="10"/></td>
<td><span i18n_key="ABCRandomNumber">农行随机码</span>:<s:textfield name="randnum" size="10" maxlength="4"/></td>
<td><span i18n_key="agentRecommendationNumber">代理推荐码</span>:<s:textfield name="partner" size="10"/></td>
<td><span i18n_key="lastLoginIp">最后登录ip</span>:<s:textfield name="lastLoginIp" size="10"/></td>
<td></td>
</tr>
<tr align="left">
	<td><span i18n_key="breakPeriod">未游戏周期</span>:<s:select list="%{#application.DaysNumber}" listKey="code" listValue="text" name="dayNumflag"  emptyOption="true"/></td>
	<td>备注:<s:textfield name="warnremark"/></td>
	<td>最近未拨打天数:<s:select list="%{#application.CallDaysNumber}" listKey="code" listValue="text" name="callDayNum"  emptyOption="true"/></td>
	<td></td>
	<td><button type="submit" i18n_key="query">查询</button></td>
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
            <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><input type="checkbox" id="checkAllBox" /></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="account">账户</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="warnLevel">警告等级</span></td>
              <%-- <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">反水率</td> --%>
              <!-- <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">最高洗码优惠</td> -->
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="clientOS">客户操作系统</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="superiorAgent">上级代理</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="recommendationNumber">推荐码</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="invitationNumber">邀请码</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="longduAmount">龙都额度</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="platformAmount">平台额度</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="type">类型</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="realName">真实姓名</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="agentWebsite">代理网址</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="openingTime">开户时间</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="openingIp">开户IP</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="openingArea">开户地区</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="lastLoginTime">上次登录时间</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="lastLoginIp">上次登录IP</span></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="lastLoginArea">上次登录地区</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="loginTimes">登录次数</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="operation">操作</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="status">状态</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="dailyAmount">日限额</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="phoneStatus">电话状态</span></td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold"><span i18n_key="comment">备注</span></td>
            </tr>
            
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr>
            <%-- <s:if test="#fc.intro.contains('cs') || #fc.intro.contains('ts')"> --%>
		            	<td>
								<input type="checkbox" name="item" value="<s:property value="#fc.loginname"/>" />
	            			</td>
				<%-- </s:if>
				<s:else>
					<td>
	            	</td>
				</s:else> --%>
			
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
              <s:url action="getUserhavinginfo" namespace="/office" var="getUserhavinginfourl"><s:param name="loginname" value="%{#fc.loginname}"/></s:url>
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
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.clientos"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.agent"/></td>
              <c:choose>
              	<c:when test="${fc.role eq 'AGENT'}">
	              		<c:choose>
	              		<c:when test="${sessionScope.operator.authority eq 'boss' }">
	              			<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a href="javascript:changePartner('${fc.loginname }');">Aedit:<s:property value="#fc.partner"/></a></td>
	              		</c:when>
	              		<c:otherwise>
		             		<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.partner"/></td>
		            	</c:otherwise>
	              		</c:choose>
	              	</c:when>
              	<c:otherwise>
              		<c:choose>
		              	<c:when test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq  'market_manager' }">
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
              <table>
              	<c:choose>
              		<c:when test="${fc.role eq 'AGENT' || sessionScope.operator.authority eq  'market_manager' &&  sessionScope.operator.username ne  'zera'}">
		              	<tr><td><input type="button" value="老虎机佣金" onclick="queryAgentSlotAccount('<s:property value="#fc.loginname"/>',this)"/></td></tr>
	              	</c:when> 
	              	<c:otherwise>
		              	<tr>
		              		<td><input type="button" id="checkptBtn" value="NEWPT" onclick="queryNewPtCredit('<s:property value="#fc.loginname"/>',this)"/></td>
							<td><input type="button" id="checkswBtn" value="SW" onclick="querySwCredit('<s:property value="#fc.loginname"/>',this)"/></td>
	              		</tr>
		              	<tr>
							<td><input type="button" id="checkMgBtn" value="MG" onclick="queryMGCredit('<s:property value="#fc.loginname"/>',this)"/></td>
							<td><input type="button" id="checkPNGBtn" value="PNG" onclick="queryPNGCredit('<s:property value="#fc.loginname"/>',this)"/></td>
		              	</tr>
						<tr>
							<td><input type="button" id="checkdtBtn" value="DT" onclick="queryDtCredit('<s:property value="#fc.loginname"/>',this)"/></td>
							<td><input type="button" id="checkqtBtn" value="QT" onclick="queryQTCredit('<s:property value="#fc.loginname"/>',this)"/></td>
						</tr>
						<tr>
		              		<td><input type="button" id="check761Btn" value="761" onclick="query761Credit('<s:property value="#fc.loginname"/>',this)"/></td>
							<td><input type="button" id="checkSbaBtn" value="SBA" onclick="querySbaCredit('<s:property value="#fc.loginname"/>',this)"/></td>
						</tr>
						<tr>
							<%-- <td><input type="button" id="checkbbinBtn" value="BBIN" onclick="queryRemoteBbinCredit('<s:property value="#fc.loginname"/>',this)"/></td> --%>
							<td><input type="button" id="checkagBtn" value="AGIN" onclick="queryAginCredit('<s:property value="#fc.loginname"/>',this)"/></td>
							<td><input type="button" id="checkTtgBtn" value="TTG" onclick="queryTTGCredit('<s:property value="#fc.loginname"/>',this)"/></td>
						</tr>
						<tr>
							<%-- <td><input type="button" id="checkRedRainBtn" value="红包雨钱包" onclick="queryRedRainCredit('<s:property value="#fc.loginname"/>',this)"/></td> --%>
						</tr>
			          </c:otherwise>
		            </c:choose>
              </table>
              </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:if test="#fc.role=='AGENT'">代理</s:if><s:else><s:property value="@dfh.model.enums.VipLevel@getText(#fc.level)"/></s:else></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
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
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><a target="_blank" href="<s:property value='setLevelUrl'/>">设定等级</a>&nbsp;|&nbsp;<s:if test="#fc.flag==@dfh.utils.Constants@ENABLE"><a href="javascript:changeUserStatus('${fc.loginname }',false)">禁用</a></s:if><s:else><a href="javascript:changeUserStatus('${fc.loginname }',true)">启用</a></s:else>
              		<c:if test="${fc.shippingcodePt !='' &&  fc.shippingcodePt != null}"><a href="javascript:relievePtLimit('${fc.loginname}');">&nbsp;|&nbsp;解除pt优惠限制</a></c:if>
              </td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:if test="#fc.flag==@dfh.utils.Constants@ENABLE">正常</s:if><s:elseif test="#fc.flag==2">密码被锁</s:elseif><s:else><font color='red'>禁用</font></s:else></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
                     <select name="limit" id="limit${fc.loginname}" onchange="limitMethod('${fc.loginname}');">
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
                 	</select>
                 </td>   
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
                 	<c:if test="${fc.sms==0}">未拨打</c:if>
                 	<c:if test="${fc.sms==1}">已接通</c:if>
                 	<c:if test="${fc.sms==2}">未接通</c:if>
                 	<c:if test="${fc.sms==3}">被挂断</c:if>
                 	<c:if test="${fc.sms==4}">空号</c:if>
                 </td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
                 	${fc.warnremark}
                 </td> 
            </tr>
  	 	 </s:iterator>
            <tr>
              <td colspan="24" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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

<script type="text/javascript" src="/js/initI18n.js"></script>
</body>
</html>

