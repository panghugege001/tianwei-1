<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<link rel="stylesheet" type="text/css" href="/style/manage_data.css" />
<link rel="stylesheet" type="text/css" href="/style/styleoverlay.css" />

<script type="text/javascript" src="/js/jquery18.js"></script>


<script type="text/javascript" src="/js/scrollable.js"></script>

<script type="text/javascript">

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

	$(function() {
		$("#wizard").scrollable({
			onSeek : function(event, i) {//切换tab样式
				$("#status li").removeClass("active").eq(i).addClass("active");
			},
			onBeforeSeek : function(event, i) {
				if (i == 1) {//判断同姓名
					//是否开启PT8元优惠
					$.post("${ctx}/asp/haveSameInfo.aspx",{},function(data){
						 if(!(data == 'success')){
				     			alert(data);
				     			$("#goback").trigger("click");
				     	 }else{
				     		 var display = $(".disFlag").css("display");
				     		 if(display == "none"){
				     			$(".disFlag").css("display","inline-block");
				     		 }
				     	 }
					});
					
				} else if (i == 2) {//判断电话号码
					var phoneCode = $("#phoneCode").val();
					if('' == phoneCode){
						alert("验证码不能为空");
						return false ;
					}
					var flag2 = checkPhoneCode(phoneCode);
					return flag2 ;
				}
				
				else if (i == 3) {//填写银行卡号
					openProgressBar();
					var flag3 = repeatBankCards();
					closeProgressBar();
					if(!flag3){
						return flag3 ;
					}
				} else if (i == 4) {//判断同姓名
					openProgressBar();
					$.post("${ctx}/asp/commitPT8Self.aspx",{"code":$("#phoneCode").val(),"platform":$("#wizard input[name='mychoice']:checked").val()},function(data){
						alert(data);
						closeProgressBar();
					});
				}
			}
		});
		
		//发送验证码
		$("#sendPhoneCodeBtn").on("click",function(){
			/*********触点**********/
			  var is_checked = false;
			        window.TouClick.Start({
			            website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
			            position_code: 0,
			            args: { 'this_form': $("#phoneCheckValid")[0] },
			            captcha_style: { 'left': '50%', 'top': '60%' },
			            onSuccess: function (args, check_obj)
			            {
			            	//console.log(args);
			            	//console.log(check_obj);
			                is_checked = true;
			                var this_form = args.this_form;
			                var hidden_input_key = document.createElement('input');
			                hidden_input_key.name = 'check_key';
			                hidden_input_key.value = check_obj.check_key;
			                hidden_input_key.type = 'hidden';
			                //将二次验证口令赋值到隐藏域
			                this_form.appendChild(hidden_input_key);
			                var hidden_input_address = document.createElement('input');
			                hidden_input_address.name = 'check_address';
			                hidden_input_address.value = check_obj.check_address;
			                hidden_input_address.type = 'hidden';
			                //将二次验证地址赋值到隐藏域
			                this_form.appendChild(hidden_input_address);
			                //再次执行 tou_submit 函数
			                //$("#btn").attr("disabled","");
			                //$("#btn").css("background","#EAE1E1");
			                //this_form.submit();
			                
			                openProgressBar();
			  			  var check_address = $("input[name='check_address']").eq(0).val();
			  			  var check_key = $("input[name='check_key']").eq(0).val();
			  			$.post("${ctx}/asp/sendPhoneSmsCode.aspx",{"check_address":check_address,"check_key":check_key},function(data){
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
		
		$("#sendPhoneTwoCodeBtn").on("click",function(){
			openProgressBar();
			$.post("${ctx}/asp/sendPhoneSmsTwoCode.aspx",{},function(data){
				alert(data);
				closeProgressBar();
			});
		});
		
		$("#sendPhoneVoiceCodeBtn").on("click",function(){
			/*********触点**********/
			  var is_checked = false;
			        window.TouClick.Start({
			            website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
			            position_code: 0,
			            args: { 'this_form': $("#phoneCheckValid")[0] },
			            captcha_style: { 'left': '50%', 'top': '60%' },
			            onSuccess: function (args, check_obj)
			            {
			            	//console.log(args);
			            	//console.log(check_obj);
			                is_checked = true;
			                var this_form = args.this_form;
			                var hidden_input_key = document.createElement('input');
			                hidden_input_key.name = 'check_key';
			                hidden_input_key.value = check_obj.check_key;
			                hidden_input_key.type = 'hidden';
			                //将二次验证口令赋值到隐藏域
			                this_form.appendChild(hidden_input_key);
			                var hidden_input_address = document.createElement('input');
			                hidden_input_address.name = 'check_address';
			                hidden_input_address.value = check_obj.check_address;
			                hidden_input_address.type = 'hidden';
			                //将二次验证地址赋值到隐藏域
			                this_form.appendChild(hidden_input_address);
			                
			                openProgressBar();
			  			  var check_address = $("input[name='check_address']").eq(0).val();
			  			  var check_key = $("input[name='check_key']").eq(0).val();
			  			$.post("${ctx}/asp/sendPhoneVoiceCode.aspx",{"check_address":check_address,"check_key":check_key},function(data){
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
	});

//是否有相同姓名等。。。
function haveSameInfo(){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "${ctx}/asp/haveSameInfo.aspx",
         async : false,
         success : function(data){
        	 if(!(data == 'success')){
     			alert(data);
     		}else{
     			flag = true ;
     		}
         }
    });
	return flag ;
}

function checkPhoneCode(phoneCode){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "${ctx}/asp/checkPhoneCode.aspx",
         data : "code="+phoneCode,
         async : false,
         success : function(data){
        	 if(!(data == 'right')){
     			alert(data);
     		}else{
     			flag = true ;
     		}
         }
    });
	return flag ;
}

function repeatBankCards(){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "${ctx}/asp/repeatBankCards.aspx",
         async : false,
         success : function(data){
        	 if(!(data == 'success')){
     			alert(data);
     		}else{
     			flag = true ;
     		}
         }
    });
	return flag ;
}

function getbankno(_bankname){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "${ctx}/asp/searchBankno.aspx",
         data : "bankname="+_bankname+"&r="+Math.random(),
         async : false,
         success : function(data){
        	if(data == 1){
       			$("#yhBankNo").val("");
       		}else{
       			$("#yhBankNo").val(data.split("|||")[0]);
       		}
         }
    });
	return flag ;
}


</script>
<style type="text/css">
#wizard h3{height: 60px;
line-height: 60px;
font-size: 20px;color: #383431; margin:20px 0; font-family:"Microsoft Yahei";}
</style>
<body style="background-color: transparent">
<div class="overlay"></div>
<div id="AjaxLoading" class="showbox">
	<div class="loadingWord"><img src="/images/waiting.gif" />验证中，请稍候...</div>
</div>

	<div id="wizard">
		
		<ul id="status">
			<li class="active"><i>1</i>同姓名</li>
			<li><i>2</i>填写电话号码</li>
			<li><i>3</i>填写银行卡号</li>
			<li><i>4</i>完成</li>
		</ul>
		<div class="items">
			<!--同姓名-->
			<div class="page">
				<div class="manage_data">
					<div class="md_in">
						<span>用户昵称：</span> <input type="text"  class="text" value="${customer.loginname}" readonly>
					</div>
					<div class="md_in">
						<span>真实姓名：</span> <input type="text"  class="text" value="${customer.accountName}" readonly>
					</div>
					<div class="md_in">
						<span>手机号：</span> <input type="text"  class="text" value="${customer.phone}" readonly>
					</div>
					<div class="md_in">
						<span>邮箱：</span> <input type="text" class="text" value="${customer.email}" readonly>
					</div>
					<div class="md_in">
						<span>注册IP：</span> <input type="text" class="text" value="${customer.registerIp}" readonly>
					</div>
					<div class="md_in">
						<span>此次登录IP：</span> <input type="text" class="text" value="${customer.lastLoginIp}" readonly>
					</div>
					<div class="go_sub">
						<a href="javascript:void(0)" class="button blue medium next" style="margin-top: 25px;" title="下一步">下一步</a>
					</div>
				</div>
			</div>
			<!--电话号码-->
			<div class="page">
				<div class="manage_data">
					<div class="md_in">
					<form id="phoneCheckValid">
						<span>电话号码：</span> <input type="text" name="aliasName" class="text" value="${customer.phone}" readonly>
                        <span class="checkcode1">
                            <a href="javascript:void(0)" class="disFlag" id="sendPhoneVoiceCodeBtn" style="display: none;">语音验证</a>
                        	<a href="javascript:void(0)" class="disFlag" id="sendPhoneCodeBtn" style="display: none;">短信验证</a>
                        	<!-- <a href="javascript:void(0)" class="disFlag" id="sendPhoneTwoCodeBtn" style="display: none;">短信验证二</a> -->
                        </span>
                       </form>
					</div>
					<div class="md_in">
						<span>输入验证码：</span> <input type="text" id="phoneCode" class="text" >
					</div>

					<div class="go_sub">
						<input type="hidden" class="button blue medium prev" value="上一步" id="goback"/>
						<a href="javascript:void(0)" class="button blue medium next" style="margin-top: 25px;" title="下一步">下一步</a>
					</div>
				</div>
			</div>
			<!--银行卡号-->
			<div class="page">
				<div class="manage_data">
					<div class="md_in">
						<span style="width: 110px;">卡户行：</span> 
							<s:select  onchange="getbankno(this.value);" cssStyle="height: 38px;" list="%{#application.IssuingBankEnum}" listKey="issuingBank" listValue="issuingBankCode" ></s:select>
					</div>
					
					<div class="md_in">
						<span style="width: 110px;">银行卡号：</span> <input type="text" id="yhBankNo" name="yhBankNo" class="text" readonly>
					</div>

					<div class="go_sub">
						<a href="javascript:void(0)" class="button blue medium next" style="margin-top: 25px;" title="下一步">下一步</a>
					</div>
				</div>
			</div>
			<!---->
			<div class="page">

				<div class="md_in">
					<%-- <div class="infocell">
						真实姓名：<span>杨东玲 </span>
					</div>
					<div class="infocell">
						真实姓名：<span>杨东玲 </span>
					</div>
					<div class="infocell">
						真实姓名：<span>杨东玲 </span>
					</div> --%>

				</div>
				<div class="go_sub go_sub1">
					<a href="javascript:void(0)" class="button blue medium next" id="sub" tile="确定" style="margin-left: 214px;">确定</a>
				</div>
			</div>
		</div>
	</div>
<style type="text/css">
#AjaxLoading {
    border: 1px solid #8cbeda;
    color: #37a;
    font-size: 12px;
    font-weight: bold;
}

#AjaxLoading div.loadingWord {
    background: none repeat scroll 0 0 #fff;
    border: 2px solid #d6e7f2;
    height: 50px;
    line-height: 50px;
    width: 180px;
}

.overlay {
    background: none repeat scroll 0 0 #f6f4f5;
    bottom: 0;
    display: none;
    height: 100%;
    left: 0;
    position: fixed;
    right: 0;
    top: 0;
    width: 100%;
    z-index: 99998;
}
.showbox {
    left: 50%;
    margin-left: -80px;
    margin-top: -60px;
    opacity: 0;
    position: fixed;
    top: 0;
    z-index: 99999;
}

</style>
<script type='text/javascript' charset='utf-8' src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js' ></script>

</body>