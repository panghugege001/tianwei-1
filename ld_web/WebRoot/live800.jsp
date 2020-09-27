<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String infoValue=(String)request.getSession(true).getValue("infoValue4Live800");
if(infoValue==null)infoValue="";
%>
<html>
<head>

<title></title>
 
 <link rel="stylesheet" type="text/css" href="${ctx}/style/styleoverlay.css" />
 <script type="text/javascript" src="${ctx}/js/jquery18.js"></script>

</head>
<style type="text/css">
	body{font-family:"Microsoft Yahei"}
	*{margin:0;padding:0;}
	.ReleaseFail{ background-color:#fff; padding:60px 20px 10px;}
	.ReleaseFail h3{font-weight: normal; color:#f00; font-size: 22px;text-align: center;}
	.ReleaseFail h4{font-weight: normal; font-size: 16px;padding-left: 20px;}
	.ReleaseFail-tool{
        margin-bottom: 20px; overflow:hidden; font-size:16px; color:#cc0000;}
	.ReleaseFail-tool a, .ReleaseFail-tool input.sub{ width:150px; height:34px; overflow:hidden; text-decoration:none; display:inline-block; background-color:#2d97dd;text-align:center; line-height:34px; color:#fff;  -webkit-transition: all 0.35s ease;
  transition: all 0.35s ease;}
  .ReleaseFail-tool input.sub{ border:none; float:left; cursor:pointer;font-family:"Microsoft Yahei"}
   .ReleaseFail-tool .loign-info{ overflow:hidden; margin-bottom:10px;}
 .ReleaseFail-tool .text { width:320px;padding: 10px 0 10px 10px; border: thin solid #d1d1d1;  color: #323232;  outline: 0 !important;}
  .ReleaseFail-tool input.input_code {  width: 141px;padding: 10px 0 10px 10px;  margin-right: 10px;border: thin solid #d1d1d1;}
	.ReleaseFail-tool a:hover, .ReleaseFail-tool input.sub:hover{ color:#fff; text-decoration:none; background-color:#f60; border-color: #257cb5;}
	
</style> 
<body>

<div id="login-box">
	<div class="ReleaseFail">

        <div class="ReleaseFail-tool">
            <div class="login_info">
                <div class="loign-info"><input type="text" id="loginname" class="text name"
                    name="loginname"  placeholder="请输入您的用户名" value="" maxlength="15"  /></div>

                <div class="loign-info"><input type="password" id="password" name="password"
                    class="text ps2" value="" placeholder="请输入您的密码" maxlength="15"  /></div>
                <div class="loign-info code">
                    <c:url value="${ctx}/asp/validateCodeForIndex.aspx"
                        scope="request" var="imgCode" />
                    <input type="text" id="code" class="input_code"
                        name="validateCode" placeholder="请输入验证码" maxlength="4" />
                    <img id="imgCode" src="" title=""
                        onclick="document.getElementById('imgCode').src='${imgCode}?'+Math.random();"
                        class="img_code" style="height:30px; vertical-align:middle;" />
                </div>

                <input type="button" class="sub" onclick="return login();" value="登录" />

            <!-- 	<a
                    href="http://kf1.learnsaas.com/chat/chatClient/chatbox.jsp?companyID=185780&jid=9344608930&configID=41061&chatType=1&accept=1&enterurl=http://www.e68.ph/"
                    target="_blank" class="forget">忘记密码</a> -->
                <a href="/forgotPassword.asp" target="_parent" class="forget" style=" margin-left:5px;">忘记密码</a>
                <a href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=<%=infoValue %>" target="_blank">在线客服</a>
            </div>
        </div>
        <p style="margin-bottom: 16px;">温馨提示</p>
        <p style="margin-bottom: 16px; color: #f00;">亲爱的天威娱乐玩家：为了更快速有效的为您处理问题，请您先登陆您的游戏账户，感谢您的配合</p>
          <!--  <p class="tc c9"><span id="jumpTo">10</span>秒后自动跳转   -->
<!-- 			<script type="text/javascript">countDown(10,'http://192.168.0.15:9991/weblogin.html');</script>  </p> -->
        	<!--<script type="text/javascript">countDown(30,'http://chat.pt777.com/chat/chatClient/chatbox.jsp?companyID=462&configID=56&jid=');</script>  </p>-->
            
            
           <!--<p class="tc c9"> <a href="http://chat.pt777.com/chat/chatClient/chatbox.jsp?companyID=462&configID=56&jid=">如果您的浏览器没有自动跳转，请点击这里</a></p>-->
    </div>
</div>

<script type="text/javascript">
    $(function(){
        //reloadCode();
        $("#code").focus(function(){
        	reloadCode();
        });
    });
    function reloadCode(){
        document.getElementById('imgCode').src='${ctx}/asp/validateCodeForIndex.aspx?r='+Math.random();
    }
    function countDown(secs,surl){
        //alert(surl);
        var jumpTo = document.getElementById('jumpTo');
        jumpTo.innerHTML=secs;
        if(--secs>0){
            setTimeout("countDown("+secs+",'"+surl+"')",1000);
        }
        else{
            location.href=surl;
        }
    }
    //玩家登陆
    function login(){
        var loginname=$("#loginname").val();
        if(loginname==""||loginname=="帐　号:"){
            alert("账号不能为空！");
            return false;
        }
        var password=$("#password").val();
        if(password==""||password=="密　码:"){
            alert("密码不能为空！");
            return false;
        }
        var code=$("#code").val();
        if(code==""||code=="验证码:"){
            alert("验证码不能为空！");
            return false;
        }
        openProgressBar();
        $.post("${ctx}/asp/login.aspx", {
            "loginname":loginname, "password":password,"imageCode":code
        }, function (returnedData, status) {
            if ("success" == status) {
                reloadCode();
                if(returnedData=="SUCCESS"){
                    window.location.href="${ctx}/";

                }else{
                    closeProgressBar();
                    alert(returnedData);
                    var str2='已被锁';
                    if(returnedData.indexOf(str2)>-1){
                        window.location="../forgotPassword.jsp";
                    }
                }
            }
        });
        return true;
    }
</script>
</body>  
</html>