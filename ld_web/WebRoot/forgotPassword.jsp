<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>  
	<link rel="stylesheet" href="/css/live.css?v=1">
	<style>html,body{height: 100%;} </style>
</head>
<body>
<div class="live-page">
	<div class="live-box">
		<i class="f-phone"></i> 
		<div class="text-center"><h2>A:根据短信找回密码？</h2></div>
		<a href="javascript:void(0);" data-toggle="modal" data-target="#j-modal-sms" class="link"></a>
	</div>
	<!-- <div class="live-box">
		<i class="f-email"></i>
		<div class="text-center"><h2>B:根据邮箱找回密码？</h2></div>
		<a  href="javascript:void(0);" data-toggle="modal" data-target="#j-modal-email" class="link"></a>
	</div> -->
	<div class="live-box">
		<i class="f-cs"></i>
		<div class="text-center"><h2>C:联系客服找回密码？</h2></div>
		<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" id="" class="link" target="_blank"></a>
	</div>
</div>

<!--help_page 开始-->


<div id="j-modal-sms" class="modal" style="display: none;background:#eae9e8;">
  <div class="modal-dialog modal-md for-pass" id="for-password" >
    <div class="modal-content" style="width:675px;height: 357px; border:none">
      
      <div class="modal-bd no-padding">
        <form action="" method="post" class="ui-form">
		  <div class="ui-form-item ft">根据短信找回密码？</div>		  
          <div class="ui-form-item">
            <label class="ui-label rq-value" for="ipt_username">帐号</label>
            <input class="ui-ipt" name="username" id="ipt_username" type="text" required>
          </div>
          <div class="ui-form-item">
            <label class="ui-label rq-value" for="ipt_phone">电话号码</label>
            <input class="ui-ipt" maxlength="11" name="phone" id="ipt_phone" type="text" required>
          </div>
            <div class="ui-form-item">
                <label class="ui-label rq-value" for="ipt_verificationcode">验证码</label>
                <input class="ui-ipt" name="verificationcode" id="ipt_verificationcode" type="text" required>
                <img id="imgCode" src="${ctx}/asp/validateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新" onclick="changeImage(0)" class="img_code">
            </div>
          
       
          
          
          <div class="ui-form-item">
            <input type="button" class="index_btn" onclick="sendDx()" id='sendPhoneCodeBtn' value="找回密码">
          </div>
		     <!-- 验证码嵌入位置 -->
	      <div class="wrap" >
	             <div id="captcha-target" class="captcha"></div>
	      </div>
        </form>

      </div>
    </div>
  </div>
</div>

<!-- <div id="j-modal-email" class="modal" style="display: none;background:#eae9e8;">
  <div class="modal-dialog modal-md" style="margin:0">
    <div class="modal-content" style="height: 257px; border:none">
      
      <div class="modal-bd no-padding">
        <form action="" method="post" class="ui-form">
		  <div class="ui-form-item ft">根据邮箱找回密码？</div>		  
          <div class="ui-form-item">
            <label class="ui-label rq-value" for="ipt_username2">帐号</label>
            <input class="ui-ipt" name="username" id="ipt_username2" type="text">
          </div>
          <div class="ui-form-item">
            <label class="ui-label rq-value" for="ipt_mail2">邮箱</label>
            <input class="ui-ipt" name="phone" id="ipt_mail2" type="email">
          </div>
          <div class="ui-form-item">
            <label class="ui-label rq-value" for="ipt_verificationcode2">验证码</label>
            <input class="ui-ipt" name="verificationcode" id="ipt_verificationcode2" type="text">
            <img id="imgCode1" src="${ctx}/asp/validateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新" onclick="changeImage(1)" class="img_code">
          </div>
          <div class="ui-form-item">
            <input type="button" class="index_btn" value="找回密码" onclick="sendEmails()">
          </div>
        </form>
      </div>
    </div>

  </div>  
</div>
 -->


<script src="//js.touclick.com/js.touclick?b=68aca137-f3c5-457b-87a4-8a46880b1e66" ></script>
 

<script type="text/javascript">

	//document.oncontextmenu=stopFuntion;
	//发送邮件
	function sendEmails(){
		var name = $("#ipt_username2").val();
		var yxdz = $("#ipt_mail2").val();
		var code = $("#ipt_verificationcode2").val();
		if(null==name||name==""){
			closeProgressBar();
			alert('请输入用戶名');
			return false;
		}
		if(null==yxdz||yxdz==""){
			closeProgressBar();
			alert('请输入邮箱地址！');
			return false;
		}
		if(null==code||code==""){
			closeProgressBar();
			alert('请输入验证码！');
			return false;
		}
		openProgressBar();
		$.ajax({
			url : "/getPwd/getbackPwdByEmail.aspx",
			type : "post", // 请求方式
			dataType : "text", // 响应的数据类型
			data : "name="+name+"&yxdz=" + yxdz+"&code="+code,
			async : true, // 异步
			success : function(msg) {
				closeProgressBar();
				alert(msg);
				changeImage(1);
				$("#ipt_username2").val('');
				$("#ipt_mail2").val('');
				$("#ipt_verificationcode2").val('');
			},
		});
	}

	//发送验证码
	function sendDx_bakv4(){
		/*********触点**********/
		var is_checked = false;
		var name = $("#ipt_username").val();
		var phone = $("#ipt_phone").val();
		window.TouClick.Start({
			website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
			position_code: 0,
			args: { 'this_form': $("#phoneCheckValid")[0] },
			captcha_style: { 'left': '50%', 'top': '60%' },
			onSuccess: function (args, check_obj)
			{
				is_checked = true;
				openProgressBar();
				var check_address =check_obj.check_address;
				var check_key =check_obj.check_key;
				$.ajax({
					url : "/getPwd/getbackPwdByDx.aspx",
					type : "post", // 请求方式
					dataType : "text", // 响应的数据类型
					data : "name="+name+"&phone=" + phone+"&check_address="+check_address+"&check_key="+check_key,
					async : false, // 异步
					success : function(msg) {
						closeProgressBar();
						$("#ipt_username").val('');
						$("#ipt_phone").val('');
						alert(msg);
					},
				});
			},
		});
		/*********触点**********/

	}
	
	//发送验证码
	function sendDx_bak(){
		/*********触点**********/
		var is_checked = false;
		var name = $("#ipt_username").val();
		var phone = $("#ipt_phone").val();
	    TouClick().start({
            modal:true,
            position:"center",
            fit:true, 
            checkCode:"123",   
            onSuccess : function(obj)
			{
				is_checked = true;
				openProgressBar();
				var check_address =obj.checkAddress;
				var check_key =obj.token;
				$.ajax({
					url : "/getPwd/getbackPwdByDx.aspx",
					type : "post", // 请求方式
					dataType : "text", // 响应的数据类型
					data : "name="+name+"&phone=" + phone+"&check_address="+check_address+"&check_key="+check_key,
					async : false, // 异步
					success : function(msg) {
						closeProgressBar();
						$("#ipt_username").val('');
						$("#ipt_phone").val('');
						alert(msg);
					},
				});
			},
		});
		/*********触点**********/
	}
	
	
	
	//发送验证码 (点触验证码 addis)
    function sendDx_bak(){
        /*********触点**********/
        var is_checked = false;
        var name = $("#ipt_username").val();
        var phone = $("#ipt_phone").val();
        TouClick().start({
                    modal:true,
                    position:"center",
                    fit:true, 
                    checkCode:"123",
                    onSuccess : function(obj){
                   	     is_checked = true;
                         openProgressBar();
                         var check_address =obj.checkAddress;
                         var check_key =obj.token;
                         $.ajax({
                             url : "/getPwd/getbackPwdByDx.aspx",
                             type : "post", // 请求方式
                             dataType : "text", // 响应的数据类型
                             data : "name="+name+"&phone=" + phone+"&check_address="+check_address+"&check_key="+check_key,
                             async : false, // 异步
                             success : function(msg) {
                                 closeProgressBar();
                                 $("#ipt_username").val('');
                                 $("#ipt_phone").val('');
                                 alert(msg);
                             },
                         });
                    },
                });
        /*********触点**********/
    }
	
  //发送验证码 (点触验证码 addis)
    function sendDx(){
        /*********触点**********/
        var is_checked = false;
        var name = $("#ipt_username").val();
        var phone = $("#ipt_phone").val();
        var code = $("#ipt_verificationcode").val();
        if(null==name||name==""){
            closeProgressBar();
            alert('请输入用戶名');
            return false;
        }
        if(null==phone||phone==""){
            closeProgressBar();
            alert('请输入电话号码！');
            return false;
        }
        if(null==code||code==""){
            closeProgressBar();
            alert('请输入验证码！');
            return false;
        }
        openProgressBar();
        $.ajax({
            url : "/getPwd/getbackPwdByDx.aspx",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            //data : "name="+name+"&phone=" + phone+"&check_address="+ obj.checkAddress+"&check_key="+obj.token+"&sid="+obj.sid,
            data : "name="+name+"&phone=" + phone+"&code="+code,
            async : false, // 异步
            success : function(msg) {
                closeProgressBar();
                changeImage(0);
                $("#ipt_username").val('');
                $("#ipt_phone").val('');
                $("#ipt_verificationcode").val('');
                alert(msg);
            },
        });
        /*
         * @param 嵌入点ID
         * @param 配置参数
         */
        /*  TouClick('captcha-target',{
             onSuccess : function(obj){
                 $("token").value = obj.token;
                 $("checkAddress").value = obj.checkAddress;
                 $("sid").value = obj.sid;

             }
         }); */
        /*********触点**********/
    }


    function changeImage(index){
        if(index == 0){
            document.getElementById('imgCode').src='/asp/validateCodeForIndex.aspx?'+Math.random();
        } else {
            document.getElementById('imgCode1').src='/asp/validateCodeForIndex.aspx?'+Math.random();
        }

    }

</script>


</body>
</html>