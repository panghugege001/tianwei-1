<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!DOCTYPE html>
<html>
<head lang="zh-cn">
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="${ctx}/css/user.css?v=11089"/>
</head>
<body>
<div class="pay-page">
	<div class="pay-online-wp">
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>

		<form  method="post" name="checkform" class="ui-form">
			<div class="ui-form-item">
				<label class="ui-label">账户：</label>
				<span class="ipt-value">${session.customer.loginname}</span>
			</div>
			<div class="ui-form-item">
				<label class="ui-label">账户结余：</label><span class="ipt-value c-red">${session.customer.credit}</span>
			</div>
			<div class="ui-form-item">
				<label class="ui-label" for="mar_questionid">密保问题：</label>
				<s:select id="mar_questionid"  name="questionid" list="%{#application.QuestionEnum}" cssClass="ui-ipt"
						  listKey="code"
						  listValue="text"></s:select>
			</div>
			<div class="ui-form-item">
				<label class="ui-label">您的回答：</label><input class="ui-ipt" type="text" id="mar_answer" name="answer" required="required">
			</div>
			<div class="ui-form-item">
				<label class="ui-label">登录密码：</label><input class="ui-ipt" type="password" id="mar_pwd" name="answer" required="required">
			</div>
			<div class="ui-form-item">
				<input class="btn btn-danger" type="button" value="提交" onclick="commitBindingQuestion()" >
			</div>
		</form>
	</div>

</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script type="text/javascript" src="${ctx }/js/base.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.min.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.config.js"></script>
<script type="text/javascript">
  	$(function(){
  		//修改导航栏active
      $('#j-user-nav>li').eq(2)
              .addClass('active')
              .siblings('.active')
              .removeClass('active');
  	    
  	    $('#j-modify-form').validate({
			submitHandler:function(form){
				$.ajaxForm('post','${ctx}/asp/change_info.aspx',$(form).serialize(),function(){
					alert('修改成功');
					window.location.reload();
				});
			}
		});
  	    
  	  	//支付宝绑定手机号验证
  	  	$("#sendAlipayPhoneVoiceCodeBtn").on("click",function(){
  	  		/*********触点**********/
  	  		  var is_checked = false;
  	  		        window.TouClick.Start({
  	  		            website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
  	  		            position_code: 0,
  	  		            args: { 'this_form': $("#blindCardForm")[0] },
  	  		            captcha_style: { 'left': '50%', 'top': '60%' },
  	  		            onSuccess: function (args, check_obj)
  	  		            {
  	  		            	//console.log(args);
  	  		            	//console.log(check_obj);
  	  		                is_checked = true;
  	  		                var this_form = args.this_form;
  	  		                var hidden_input_key = document.createElement('input');
  	  		                hidden_input_key.name = 'check_key1';
  	  		                hidden_input_key.value = check_obj.check_key;
  	  		                hidden_input_key.type = 'hidden';
  	  		                //将二次验证口令赋值到隐藏域
  	  		                this_form.appendChild(hidden_input_key);
  	  		                var hidden_input_address = document.createElement('input');
  	  		                hidden_input_address.name = 'check_address1';
  	  		                hidden_input_address.value = check_obj.check_address;
  	  		                hidden_input_address.type = 'hidden';
  	  		                //将二次验证地址赋值到隐藏域
  	  		                this_form.appendChild(hidden_input_address);
  	  		                
  	  		                openProgressBar();
  	  		  			  var check_address = $("input[name='check_address1']").eq(0).val();
  	  		  			  var check_key = $("input[name='check_key1']").eq(0).val();
  	  		  			  
  	  		  			$.post("${ctx}/asp/sendAlipayPhoneVoiceCode.aspx",{"check_address":check_address,"check_key":check_key},function(data){
  	  		  				alert(data);
  	  		  				closeProgressBar();
  	  		  			});
  	  		            },
  	  		            onError: function (args)
  	  		            {
  	  		                //启用备用方案
  	  		            }
  	  		        });
  	  		  /*********触点**********/
  	  	});
  	  	
  	  	$("#sendAlipayPhoneCodeBtn").on("click",function(){
  	  		/*********触点**********/
  	  		  var is_checked = false;
  	  		        window.TouClick.Start({
  	  		            website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
  	  		            position_code: 0,
  	  		            args: { 'this_form': $("#blindCardForm")[0] },
  	  		            captcha_style: { 'left': '50%', 'top': '60%' },
  	  		            onSuccess: function (args, check_obj)
  	  		            {
  	  		            	//console.log(args);
  	  		            	//console.log(check_obj);
  	  		                is_checked = true;
  	  		                var this_form = args.this_form;
  	  		                var hidden_input_key = document.createElement('input');
  	  		                hidden_input_key.name = 'check_key1';
  	  		                hidden_input_key.value = check_obj.check_key;
  	  		                hidden_input_key.type = 'hidden';
  	  		                //将二次验证口令赋值到隐藏域
  	  		                this_form.appendChild(hidden_input_key);
  	  		                var hidden_input_address = document.createElement('input');
  	  		                hidden_input_address.name = 'check_address1';
  	  		                hidden_input_address.value = check_obj.check_address;
  	  		                hidden_input_address.type = 'hidden';
  	  		                //将二次验证地址赋值到隐藏域
  	  		                this_form.appendChild(hidden_input_address);
  	  		                
  	  		                openProgressBar();
  	  		  			  var check_address = $("input[name='check_address1']").eq(0).val();
  	  		  			  var check_key = $("input[name='check_key1']").eq(0).val();
  	  		  			$.post("${ctx}/asp/sendAlipayPhoneSmsCode.aspx",{"check_address":check_address,"check_key":check_key},function(data){
  	  		  				alert(data);
  	  		  				closeProgressBar();
  	  		  			});
  	  		            },
  	  		            onError: function (args)
  	  		            {
  	  		                //启用备用方案
  	  		            }
  	  		        });
  	  		  /*********触点**********/
  	  	});
  	    
  	  	clearBandingform();
	});
  	
	//绑定银行卡
	function checkbandingform(){
		if(!window.confirm("确定吗？")){
			return false;
		}
		var bdbankno=$("#bdbankno").val();
		if(bdbankno==""){
			alert("[提示]卡/折号不可为空！");
			return false;
		}
//		if(bdbankno.length>30||bdbankno.length<10){
//			alert("[提示]卡/折号长度只能在10-30位之间");
//			return false;
//		}
		var bdbank=$("#bdbank").val();
		if(bdbank==""){
			alert("[提示]银行不能为空！");
			return false;
		}
		var bdpassword=$("#bdpassword").val();
		if(bdpassword==""){
			alert("[提示]登录密码不可以为空");
			return false;
		}
		var bindingCode = $("#bindingCode").val();
		openProgressBar();
		$.post("${ctx}/asp/bandingBankno.aspx", {
			"password":bdpassword,
			"bankname":bdbank,
			"bankno":bdbankno,
			"bankaddress":"none",
	        "bindingCode":bindingCode
		}, function (returnedData, status) {
			if ("success" == status) {
				if(returnedData=="SUCCESS"){
					alert("绑定成功！");
				}else{
					closeProgressBar();
					alert(returnedData);
				}
			}
		});
	}
	
	//重置绑定
	function clearBandingform(){
	   $("#bdbankno").val("");
	   $("#bdpassword").val("");
	}
	
	function showyzmDiv(bank){
		 if(bank == "支付宝"){
			 $("#zfbyzmDiv").attr("style", "display:block;");
		 }else{
			 $("#zfbyzmDiv").attr("style", "display:none;");
		 }
	}
	
	function commitBindingQuestion(){
		var questionid = $("#mar_questionid").val();
		var answer = $("#mar_answer").val();
		var password = $("#mar_pwd").val();
		$.post("${ctx}/asp/saveQuestion.aspx", {
	          "questionid" : questionid,
	          "answer":answer,
	          "password":password
	      }, function (data) {
	          closeProgressBar();
		      alert(data);
	      });
		
	}
</script>
<script type='text/javascript' charset='utf-8' src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js' ></script>
</body>
</html>