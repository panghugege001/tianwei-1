<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link rel ="stylesheet" type="text/css" href ="${ctx}/css/agtrybox.css" />
<script type="text/javascript" src="${ctx}/js/lib/jquery-1.11.2.min.js"></script>
<script>
   function saveAgRegister(){
       var userPhone=$("#userPhone").val();
       if (userPhone == "") {
           alert("手机号码不能为空！");
           return false;
       }
       if (userPhone.length != 11) {
           alert("输入的手机号码有误！");
           return false;
       }
       if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(userPhone))){ 
           alert("输入的手机号码有误！"); 
           return false; 
       } 
       var imageCode=$("#imageCode").val();
       if (imageCode == "") {
           alert("验证码不能为空！");
           return false;
       }
       openProgressBar();
       $.post("${ctx}/asp/getAgTryAccounts.aspx", {
           "userPhone":userPhone,"imageCode":imageCode
       }, function (returnedData, status) {
           if ("success" == status) {
              changAgTryimg();
              closeProgressBar();
              var result=returnedData;
              if (result == "success") {
                 alert(result);
                 $("#imageCode").val("");
                 $("#agPassword").val("");
                 changeimg();
              } else {
                 alert(result);
                 $("#imageCode").val("");
                 $("#agPassword").val("");
              }
        }
       });
   	   return false;
   }
   
   function agTryLogin(){
    	var userPhone=$("#userPhone").val();
    	if (userPhone == "") {
        	alert("手机号码不能为空！");
        	return false;
    	}
    	if (userPhone.length != 11) {
       	 	alert("输入的手机号码有误！");
        	return false;
    	}
    	if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(userPhone))){ 
        	alert("输入的手机号码有误！"); 
        	return false; 
    	} 
    	var imageCode=$("#imageCode").val();
    	if (imageCode == "") {
        	alert("验证码不能为空！");
        	return false;
    	}
    	var agPassword=$("#agPassword").val();
    	if (agPassword == "") {
        	alert("密码不能为空！");
        	return false;
    	}
    	$("#agLogin").attr("target","blank");
    	$("#agLogin").submit();    
   }
   
   function changAgTryimg() {
       var myimg = document.getElementById("imgAgRegisterTryCode");
       var now = new Date();
       myimg.src = "${ctx}/asp/agTryValidateCodeForIndex.aspx?r=" + now.getTime();
   }
	   
   //隐藏注册页面
   function closeAgRegister(){
      $('#agRegister').animate({'top':$(window).scrollTop() + $(window).height()},550,'easeInOutBack',function(){
		   $("#agRegister").hide();
		   $('#screen').hide();
		   $('html,body').css({'overflow':'auto'});
	  });
   }
   
 //打开进度条
	  function openProgressBar(){
	     var h = $(document).height();
	     $(".showbox").css({"z-index": "99999" });
		 $(".overlay").css({"height": h });
		 $(".overlay").css({'display':'block','opacity':'0.8'});
		 $(".showbox").stop(true).animate({'margin-top':'300px','opacity':'1'},200);	
	  }
 
	  //关闭进度条
	  function closeProgressBar(){
	     $(".showbox").css({"z-index": "-99999" });
	     $(".showbox").stop(true).animate({'margin-top':'250px','opacity':'0'},400);
		 $(".overlay").css({'display':'none','opacity':'0'});	
	  }
</script>

<div id="boxIn5">
	<div class="items">
		<ul id="money_in"> 
			<li class="on">2015年度AG试玩 百家乐赢点大赛</li>
		</ul>
		<div class="item item1 on">
		<form id=agLogin action="${ctx}/asp/agTryLogin.aspx" method="post"
		name="agLogin">
			<table>
				<tr>
					<td class="first firstFont">试玩手机号：</td>
					<td><input class="textInput" type="text" placeholder="" name="userPhone" maxlength="11" id="userPhone" >&nbsp;<span class="pContFont">用于接收试玩密码</span></td>
				</tr>
				<tr>
					<td class="first firstFont">验  证  码: </td>
					<td><input class="textInput" type="text" name="imageCode" id="imageCode" maxlength="4">&nbsp;<img
							id="imgAgRegisterTryCode"
							src="${ctx}/asp/agTryValidateCodeForIndex.aspx"
							style="height: 31px;vertical-align: middle;width: 77px;"
							onclick="document.getElementById('imgAgRegisterTryCode').src='${ctx}/asp/agTryValidateCodeForIndex.aspx?r='+Math.random();" /></td>
				</tr>
				<tr>
					<td class="first firstFont">试玩AG密码：</td>
					<td colspan="2"><input class="textInput1" type="password"  name="agPassword" maxlength="20"></td>
				</tr>
				<tr>
					<td class="first">&nbsp;</td>
					<td colspan="2">
						<input type="reset" class="sub" value="获取密码" onclick="return saveAgRegister();" />
						<input type="button" class="sub2" value="提交" onclick="return agTryLogin();" />
					</td>
				</tr>
				
			</table>
			</form>
		</div>
	</div>
		

</div>
	
 