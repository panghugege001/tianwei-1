<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户列表</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.21.custom.css"
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

<body>

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



function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

(function () {
    $(".button_wxz").hover(function () {
         $(this).removeClass("button_wxz").addClass("button_xz");
    }, function () {
         $(this).removeClass("button_xz").addClass("button_wxz");
    });
});
$(function () {
    _querySwitch();
});

function _querySwitch() {
    $.get("/office/querySMSPlatformSwitch.do", function (data) {
        if (data != "") {
            $("#btnSwitch").val("已开启,点击关闭");
            $("#switchValue").val("是");
        } else {
            $("#btnSwitch").val("未开启,点击开启");
            $("#switchValue").val("否");
        }
    });
}

function _executeSwitch () {
    var flag = $("#switchValue").val();
    $.post("/office/updateSMSPlatformSwitch.do", { "flag": flag }, function (data) {
        if (data === "SUCCESS") {
            alert("操作成功！");
        } else {
            alert("操作失败！");
        }
        _querySwitch();
    });
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

function buttonJAlertStyle() {
    $(".button_wxz").hover(function () {
        $(this).removeClass("button_wxz").addClass("button_xz");
    }, function () {
        $(this).removeClass("button_xz").addClass("button_wxz");
    });
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

	function _executeSendSms(btn){
		var result = new Array();
		var ids ;
		var maxlen=63;
		$("[name = item][checked]:checkbox").each(function(){
			result.push($(this).attr("value"));
		});
		var len = result.length ;
		if(len>0){
			var ids = result.join(",") ;
			var html="<table><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 155px;'></textarea></td></tr><tr><td colspan='2'><input type='button' value='短信发送' onclick='duanXinQunFaCommit(\""+ids+"\",this);' style='margin-left: 370px;'></div></td></tr></table>";
			$("#JqAlert").html(html);
    		JAlert_Progressbar_Dialog("JqAlert");
    		
    		
		}else{
			alert("请选择需要执行的数据");
		}
		
	}
	
	
		
	function duanXinQunFaCommit(ids,btn){
		        btn.disabled=true;
				var remark = $("#remark").val() ;
				if(remark.length>63){
					alert("短信内容不能超过63个字符");
					return ;
				}
				
				$.post("/office/sendSmsByUsersList.do",{"ids":ids,"content":remark},function(data){
					 btn.disabled=false;
		    			alert(data);
		    			$("#JqAlert").dialog("close");
		    			$("#JqAlert").hide();
		    		});
			}

function _executeSendPhone () {
    var html = "<table><tr><td class='label_search_td_play' valign='top' width='77px'>手机号码：</td><td><textarea name='phone' class='input' id='phone' style='width: 400px; height: 105px;'></textarea></td></tr><tr><td class='label_search_td_play' valign='top'>内容：</td><td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 105px;'></textarea></td></tr><tr><td colspan='2'><input type='button' value='手机号码发送' onclick='haoMaQunFaCommit(this);' style='margin-left: 370px;'></td></tr></table>";
    $("#JqAlert").html(html);
    JAlert_Progressbar_Dialog("JqAlert");
}

function haoMaQunFaCommit(btn) {
    btn.disabled = true;
    var remark = $("#remark").val();
    if (remark.length > 63) {
        alert("短信内容不能超过63个字符");
        btn.disabled = false;
        return;
    }
    var phone = $("#phone").val();
    $.post("/office/sendSmsByPhoneList.do", { "phone": phone, "content": remark }, function(data) {
        btn.disabled = false;
        alert(data);
        $("#JqAlert").dialog("close");
        $("#JqAlert").hide();
    });
}
	
</script>

<s:form action="queryUserSms" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
账户 --> 用户列表<a href="javascript:history.back();"> <font color="red">上一步</font></a>
</div>
	<c:if test="${sessionScope.operator.username eq 'tmyilia' || sessionScope.operator.username eq 'martin'}">
		<input type="hidden" id="switchValue" value="" />
		<input type="button" id="btnSwitch" value="" onclick="_executeSwitch()" />
	</c:if>
	<input type="button" value="短信发送" onclick="_executeSendSms(this);"/>
	<input type="button" value="手机号码发送" onclick="_executeSendPhone(this)" />

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
<td>上级代理:<s:textfield id="agent" name="agent" size="10"/></td>
</tr>
<tr align="left">
<td>代理网址:<s:textfield name="referWebsite" size="10"/></td>
<%-- <td>昵称:<s:textfield name="aliasName" size="12"/></td> --%>
<td>真实姓名:<s:textfield name="accountName" size="8"/></td>
<td>邮箱:<s:textfield name="email" size="15"/></td>
<td>电话:<s:textfield name="phone" size="15"/></td>
<td>会员状态:<s:select name="status" list="#{'0':'启用','1':'禁用'}" emptyOption="true"/></td>
<td>生日:<s:textfield name="birthday" size="12" onfocus="WdatePicker({dateFmt:'MM-dd'})" My97Mark="false" value="%{birthday}" /></td>
</tr>
<tr align="left">
<td>是否存款:<s:select name="isCashin" list="#{'0':'已存款','1':'未存款'}" emptyOption="true"/></td>
<td>开户IP:<s:textfield name="registerIp" size="10"/></td>
<%-- <td>用户ID:<s:textfield name="id" size="10"/></td> --%>
<td>来源网址:<s:textfield name="howToKnow" size="10"/></td>
<td>每页:<s:select list="%{#application.PageSizeSms}" name="size"></s:select></td>
<td>警告等级:<s:select list="%{#application.WarnLevel}" listKey="code" listValue="text" name="warnflag" emptyOption="true"></s:select></td>
<td>电话状态:<s:select name="sms" list="#{'0':'未知','1':'可用','2':'不可用'}"  emptyOption="true"/></td>
</tr>
<tr align="left">
<td>推荐码:<s:textfield id="intro" name="intro" size="10"/></td>
<td>邀请码:<s:textfield name="invitecode" size="10"/></td>
<td>QQ:<s:textfield name="qq" size="10"/></td>
<td>农行随机码:<s:textfield name="randnum" size="10" maxlength="4"/></td>
<td>代理推荐码:<s:textfield name="partner" size="10"/></td>
<td>最后登录ip:<s:textfield name="lastLoginIp" size="10"/></td>
<td></td>
</tr>
<tr align="left">
	<td>
	   未游戏玩家:
	   <s:select list="%{#application.DaysNumber}" listKey="code" listValue="text" name="dayNumflag"  emptyOption="true"/>
	</td>
	<td></td>
	<td></td>
	<td></td>
	<td><s:submit value="查询"></s:submit></td>
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
	    <div id="JqAlert" title="温馨提示"
			style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);">
	    </div>
		  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
	
		
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><input type="checkbox" id="checkAllBox" /></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">帐号</td>
             
            </tr>
            
            <s:iterator var="fc" value="%{#request.page.pageContents}">
              <tr>
            	<td>
					<input type="checkbox" name="item" value="<s:property value="#fc.loginname"/>" />
       			</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
             <s:url action="getUserhavinginfo" namespace="/office" var="getUserhavinginfourl"><s:param name="loginname" value="%{#fc.loginname}"/></s:url>
              <a target="_blank" href='<s:property value="%{getUserhavinginfourl}"/>' title="<s:text name='Account.list.usernametip'/>"> <s:property value="#fc.loginname"/></a>
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
</body>
</html>

