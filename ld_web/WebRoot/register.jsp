<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@page import="dfh.utils.Constants" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html> 
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <style>html,body{height: 100%;} </style>
</head>
<body>
<div class="reg-page">
    <s:url action="checkUserExsit" namespace="/asp" var="checkUserExsitUrl"></s:url>
    <s:url action="validateCodeForIndex" namespace="/asp" var="validateCodeForIndexUrl"></s:url>
    <s:url action="register" namespace="/asp" var="registerUrl"></s:url>

    <div class="mb20 text-center">带有<span class="c-red">*</span>项目栏为必填写信息。</div>

    <form id="registrationtable" name="registrationtable" action="${registerUrl}" method="post" class="reg-form ui-form">
        <div class="m-row">
            <div class="cell col-6">
                <%-- <div class="ui-form-item" style="display:none;">
                <label for="" class="ui-label rq-value">货币：</label>
                <input class="ui-ipt" type="text" name="rmb" value="人民币" disabled="disabled"/>

                <span class="ipt-tip">
                  一旦注册货币将不能更换
                </span>
            </div>--%>
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">用户名：</label>
                    <input class="ui-ipt" type="text" maxlength="10" name="loginname" id="loginnameId" data-rule-register-username="true"  placeholder="6-10位小写字母与数字组合"/>
                    <span class="ipt-tip">6-10位小写字母与数字组合</span>
                </div>
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">密码：</label>
                    <input type="password" style="display: none;"/>
                    <input class="ui-ipt"   type="password" maxlength="12" name="password" id="passwordRegister" placeholder="由8-12个数字或英文字母组成" data-rule-password2="true" data-rule-notequalto="true" required/>
                    <span class="ipt-tip">由8-12个数字或英文字母组成</span>
                </div>
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">确认密码：</label>
                    <input class="ui-ipt"  type="password" name="confirm_password" maxlength="12" id="confirmPasswordRegister" placeholder="请再次填写密码" data-rule-equalTo="#passwordRegister" required />

                    <span class="ipt-tip">请再次填写密码</span>
                </div>
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">电子邮箱：</label>
                    <input class="ui-ipt" type="text" name="email" id="email" maxlength="30" placeholder="请输入你的邮箱，建议@gmail.com" data-rule-email="true" required />

                    <span class="ipt-tip">请输入你的邮箱，建议@gmail.com</span>
                </div>
                 
            </div>
            <div class="cell col-6">
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">姓名：</label>
                    <input class="ui-ipt" type="text" name="accountName" id="accountName" maxlength="10" placeholder="与银行账户姓名相同，否则无法提款" data-rule-chinese-name="true" required/>

                    <span class="ipt-tip">与银行账户姓名相同，否则无法提款</span>
                </div>
              <%--  <div class="ui-form-item" style="display:none;">
                    <label for="" class="ui-label rq-value">昵称：</label>
                    <input class="ui-ipt" type="text" name="aliasName" id="aliasName" maxlength="10" placeholder="请输入您的昵称"/>

                    <span class="ipt-tip">
                      10个以内的汉字、英文字母或数字
                    </span>
                </div>--%>

                <div class="ui-form-item">
                    <label for="" class="ui-label rq-label">生日：</label>
                    <input class="ui-ipt" type="text" name="birthday" id="birthday" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" readonly placeholder="用于抽查是否年满18岁，发放生日礼金"/>

                    <span class="ipt-tip">用于抽查是否年满18岁，发放生日礼金</span>
                </div>

               <%-- <div class="ui-form-item" style="display:none;">
                    <label for="" class="ui-label">邀请码：</label>
                    <input class="ui-ipt" type="text" name="intro" id="intro" value="" maxlength="50" placeholder="请输入邀请码"/>

                    <span class="ipt-tip">
                      可以不填
                    </span>
                </div>--%>
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">手机号码：</label>
                    <input class="ui-ipt" type="text" name="phone" id="phone" maxlength="11"  placeholder="若申请优惠，我们须和本人进行电话核实" data-rule-register-phone="true" required/>

                    <span class="ipt-tip">若申请优惠，我们须和本人进行电话核实</span>
                </div>
                 
                <div class="ui-form-item">
                    <label for="" class="ui-label rq-value">验证码：</label>
                    <input style="width: 100px;" class="ui-ipt" type="text" name="validateCode" id="validateCode" placeholder="请输入验证码" required data-msg-required="请输入验证码"/>
                    <img id="validateCodeRegister" src="${validateCodeForIndexUrl}" title="如果看不清验证码，请点图片刷新" onclick="this.src='${validateCodeForIndexUrl}?r='+Math.random();" class="img_code" style="height: 38px;width: 92px;"/>
                    <span class="ipt-tip">请输入验证码</span>
                </div>
            </div>
        </div>
        <div class="form-ft">
            <div class="text"><p>温馨提醒：请确认您的注册信息，一经注册，信息将无法进行修改。</p>
                <input id="ipt-coupon-plant" type="checkbox" checked="checked" name="sms" required data-msg-required="请选择是否同意协议"/>
				<label for="ipt-coupon-plant" class="check-box"></label>
                我愿意接收<a target="_blank" href="/aboutus.jsp#tab-agreement" class="link c-blue">《用户协议》</a>和<a target="_blank" href="/aboutus.jsp#tab-privacy" class="link c-blue">《隐私条款》</a>
                
            </div>
            <div class="text-center mt20">
                <input type="submit" class="btn btn-primary btn-reg" value="确认注册" />
            </div>
        </div>
		<div class="reg-bottom mt20">
			<img src="${ctx}/images/register/money.png" class="money a-swing" /><img src="${ctx}/images/register/reg-r.jpg" class="pic fl" /><img src="${ctx}/images/register/reg-l.jpg" class="pic fl"/>
		</div>
    </form>
</div>

<script type='text/javascript' src="${ctx}/js/lib/jquery.validate.min.js"></script>
<script type='text/javascript' src="${ctx}/js/lib/jquery.validate.config.js"></script>
<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
<script>
    $(function(){

        //验证密码，字母头同时为大小写字母和数字的密码
        $.validator.addMethod('notEqualTo', function (value, element) {
            var reg = $('#loginnameId').val();
            return this.optional(element) || value!=reg;
        }, '密码不能和用户名相同');

        function chekObj(key,id){
            return {
                url:'/asp/sjyz.aspx',
                type:'post',
                data:{
                    'sjType':key,
                    'sjValue':function(){
                        return $('#'+id).val();
                    }
                },
                dataFilter:function(data){
                    if (data == '\"1\"') {
                        return true;
                    }else {
                        return false;
                    }
                }
            }
        }



        $('#registrationtable').validate({
            rules:{
                'loginname': {
                    required: true,
                    email   : false,
                    remote  : chekObj('yhm','loginnameId')
                },
                'email':{
                    required:true

                },
                'phone':{
                    required:true

                }
            },
            messages:{
                'loginname':{
                    required:'请填写用户名',
                    'register-username':'用户名由6-10个数字或英文字母组成',
                    remote: '用户名重复,请重新填写！'
                }
            },
            onfocus: true,
            onkeyup: false,
            errorClass:"error-tip",
            errorPlacement:function(error,element){
                error.appendTo(element.parent());
                element.siblings('.ipt-tip').hide();
            },
            submitHandler:function(form) {
                $.post('/asp/register.aspx',$(form).serialize(),
                        function(data){
                        	Debug;
                            document.getElementById('validateCodeRegister').src='${ctx}/asp/validateCodeForIndex.aspx?r='+Math.random();
                            if(data==="SUCCESS"){
                                alert("注册成功，请登录");
                                <%--window.location.href="${ctx}/";--%>
                                window.parent.$('#modal-success').modal('show');
                                window.parent.$('#modal-reg').modal('hide');
                            }else if(data=="帐号被禁用 :请联系客服!"){
                                $(form).find('input').val('');
                                alert(data);
                            }else{
                                $("#passwordRegister").val("");
                                $("#confirmPasswordRegister").val("");
                                $("#validateCodeRegister").val("");
                                alert(data);
                            }
                        })
            }
        })
    });
</script>
</body>
</html>
