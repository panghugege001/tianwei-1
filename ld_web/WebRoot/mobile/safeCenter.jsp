<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
    <jsp:include page="/mobile/commons/linkSource.jsp"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
    <link rel="stylesheet" type="text/css" href="/mobile/css/lib/mui-0.2.1/mui.min.css">
    <link href="/css/safecenter/layer.css?2.0" type="text/css" rel="styleSheet" id="layermcss">
    <link href="/css/safecenter/safeCenter.css?v=2" type="text/css" rel="styleSheet">
</head>

<body>

<div id="j-title-content" class="contents">
    <div class="center-top">
        <header class="common-header j-common-header j-come-back">
           <a href="/mobile/new/index.jsp"><i class="iconfont icon-arrow-left left-button arrow-title"></i></a>
            <div class='header-title'>自助安全中心</div>
            <i class="iconfont icon-cs comm-other-button"></i>
        </header>
    </div>

    <div class="center-list">
    	<div class="banner-box">
    		<img src="/mobile/images/safeBanner.jpg" alt=""> 
    	</div>
        <ul>
            <li id="tag-password" class="title">忘记密码<img src="/mobile/images/r-arr.png" alt=""></li>
            <li id="tag-account" class="title">忘记账号<img src="/mobile/images/r-arr.png" alt=""></li>
            <li id="tag-unlock" class="title">解冻账号<img src="/mobile/images/r-arr.png" alt=""></li>
            <li id="tag-unbank" class="title">解绑银行卡<img src="/mobile/images/r-arr.png" alt=""></li>
            <!--<li id="tag-unpdq" class="title">重置密保<img src="/mobile/images/r-arr.png" alt=""></li>-->
        </ul>
    </div>
</div>
<div id="j-page-content" class="contents" style="display: none;">
    <!--忘记密码-->
    <div id="f_pw-page" class="full-page">
        <header class="common-header j-common-header j-come-back">
           <i onclick="closeContent()" class="iconfont icon-arrow-left left-button arrow-title"></i>
            <div class='header-title'>忘记密码</div>
            <i class="iconfont icon-cs comm-other-button"></i>
        </header>

        <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
            <div class="tab-btn">
                <a href="javascript:;" class="nav_s active" data-target="forget_sms">短信找回</a>
                <a href="javascript:;" class="nav_s" data-target="forget_email">邮箱找回</a>
            </div>
            <div class="tab-contents">
                <!--短信找回-->
                <div class="content-item active" id="forget_sms">
                    <p class="font16 my-input-title">游戏账号</p>
                    <input type="text" class="my-input ga-input">
                    <p class="w_tips">请输入正确游戏账号</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">注册手机号</p>
                    <input type="number" class="my-input phone-input" maxlength="11">
                    <p class="w_tips">请输入正确电话号码</p>
                    <div class="space-2"></div>

                    <div class="bottom-btn j-submitBtn" onclick="sendDx()">找回密码</div>
                    <div class="space-5"></div>
                </div>
                <!--邮箱找回-->
                <div class="content-item" id="forget_email">
                    <p class="font16 my-input-title">游戏账号</p>
                    <input type="text" class="my-input ga-input">
                    <p class="w_tips">请输入正确游戏账号</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">注册邮箱</p>
                    <input type="text" class="my-input email-input">
                    <p class="w_tips">请输入正确注册邮箱</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">验证码</p>
                    <input type="number" class="my-input code-input">
                    <img id="imgCode1" src="/asp/agTryValidateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新"
                         onclick="this.src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random()" class="img_code" style="font-size: 16px;">
                    <p class="w_tips">验证码输入错误，请重新输入!</p>
                    <div class="space-2"></div>

                    <div class="bottom-btn j-submitBtn" onclick="sendEmails()">找回密码</div>
                    <div class="space-5"></div>
                </div>
            </div>
        </div>
    </div>

    <!--忘记账号-->
    <div id="f_ac-page" class="full-page">

        <header class="common-header j-common-header j-come-back">
           <i onclick="closeContent()" class="iconfont icon-arrow-left left-button arrow-title"></i>
            <div class='header-title'>忘记账号</div>
            <i class="iconfont icon-cs comm-other-button"></i>
        </header>

        <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
            <div class="tab-btn">
                <a href="javascript:;" class="nav_s active" data-target="forget_a_sms">短信找回</a>
                <a href="javascript:;" class="nav_s" data-target="forget_a_email">邮箱找回</a>
            </div>
            <div class="tab-contents">
                <!--短信找回-->
                <div class="content-item active" id="forget_a_sms">
                    <p class="font16 my-input-title">注册手机号</p>
                    <input type="number" class="my-input phone-input" maxlength="11">
                    <p class="w_tips">请输入正确注册手机号</p>
                    <div class="space-2"></div>

                    <!--<p class="font16 my-input-title">验证码</p>-->
                    <!--<input type="text" class="my-input code-input">-->
                    <!--<img id="imgCode2" src="/asp/agTryValidateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新"-->
                         <!--onclick="this.src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random()" class="img_code" style="font-size: 16px;">-->
                    <!--<p class="w_tips">验证码输入错误，请重新输入!</p>-->
                    <!--<div class="space-2"></div>-->

                    <div class="bottom-btn j-submitBtn" onclick="getForgetAccBySms()">找回账号</div>
                    <div class="space-5"></div>
                </div>
                <!--邮箱找回-->
                <div class="content-item" id="forget_a_email">

                    <p class="font16 my-input-title">注册邮箱</p>
                    <input type="text" class="my-input email-input">
                    <p class="w_tips">请输入正确注册邮箱</p>
                    <div class="space-2"></div>

                    <!--<p class="font16 my-input-title">验证码</p>-->
                    <!--<input type="text" class="my-input code-input">-->
                    <!--<img id="imgCode3" src="/asp/agTryValidateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新"-->
                         <!--onclick="this.src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random()" class="img_code" style="font-size: 16px;">-->
                    <!--<p class="w_tips">验证码输入错误，请重新输入!</p>-->
                    <!--<div class="space-2"></div>-->

                    <div class="bottom-btn j-submitBtn" onclick="getForgetAccByEm()">找回账号</div>
                    <div class="space-5"></div>
                </div>
            </div>
        </div>
    </div>

    <!--解冻账号-->
    <div id="un_ac-page" class="full-page">

        <header class="common-header j-common-header j-come-back">
           <i onclick="closeContent()" class="iconfont icon-arrow-left left-button arrow-title"></i>
            <div class='header-title'>自主解冻账号</div>
            <i class="iconfont icon-cs comm-other-button"></i>
        </header>

        <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
            <div class="tab-btn">
                <a href="javascript:;" class="nav_s active" data-target="un_ac_info">个人信息解冻</a>
                <a href="javascript:;" class="nav_s"  data-target="un_ac_pw">密码解冻</a>
            </div>
            <div class="tab-contents">
                <!--个人信息解冻-->
                <div class="content-item active" id="un_ac_info">
                    <p class="font16 my-input-title">游戏账号</p>
                    <input type="text" class="my-input ga-input">
                    <p class="w_tips">请输入正确游戏账号</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">注册姓名</p>
                    <input type="text" class="my-input name-input">
                    <p class="w_tips">请输入正确姓名</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">注册手机号</p>
                    <input type="number" class="my-input phone-input" maxlength="11">
                    <p class="w_tips">请输入正确电话号码</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">注册邮箱</p>
                    <input type="text" class="my-input email-input">
                    <p class="w_tips">请输入正确注册邮箱</p>
                    <div class="space-2"></div>

                    <div class="bottom-btn j-submitBtn" onclick="getBackAcc()">去解冻</div>
                    <div class="space-5"></div>
                </div>
                <!--密码解冻-->
                <div class="content-item" id="un_ac_pw">

                    <p class="font16 my-input-title">游戏账号</p>
                    <input type="text" class="my-input ga-input">
                    <p class="w_tips">请输入正确游戏账号</p>
                    <div class="space-2"></div>

                    <p class="font16 my-input-title">游戏密码</p>
                    <input type="password" class="my-input pw-input">
                    <p class="w_tips">请输入正确游戏密码</p>
                    <div class="space-2"></div>


                    <div class="bottom-btn j-submitBtn" onclick="unAccPw()">去解冻</div>
                    <div class="space-5"></div>
                </div>
            </div>
        </div>
    </div>

    <!--解绑银行卡-->
    <div id="un_bank-page" class="full-page">

        <header class="common-header j-common-header j-come-back">
           <i onclick="closeContent()" class="iconfont icon-arrow-left left-button arrow-title"></i>
            <div class='header-title'>解绑银行卡</div>
            <i class="iconfont icon-cs comm-other-button"></i>
        </header>

        <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
            <div class="content-item active" id="un_bank">

                <p class="font16 my-input-title">解绑银行卡号</p>
                <input type="number" class="my-input bankc-input">
                <p class="w_tips">请输入正确银行卡号</p>
                <div class="space-2"></div>

                <div class="bottom-btn j-submitBtn" onclick="unBankNo()">去解绑</div>
                <div class="space-5"></div>
            </div>
        </div>
    </div>

    <!--重置密保-->
    <div id="rst_pw-page" class="full-page">
        <header class="common-header j-common-header j-come-back">
           <i onclick="closeContent()" class="iconfont icon-arrow-left left-button arrow-title"></i>
            <div class='header-title'>重置密保</div>
            <i class="iconfont icon-cs comm-other-button"></i>
        </header>

        <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
            <div class="content-item active" id="un_rst">

                <p class="font16 my-input-title">注册姓名</p>
                <input type="text" class="my-input name-input">
                <p class="w_tips">请输入正确姓名</p>
                <div class="space-2"></div>

                <p class="font16 my-input-title">注册手机号</p>
                <input type="number" class="my-input phone-input" maxlength="11">
                <p class="w_tips">请输入正确电话号码</p>
                <div class="space-2"></div>

                <p class="font16 my-input-title">注册邮箱</p>
                <input type="text" class="my-input email-input">
                <p class="w_tips">请输入正确注册邮箱</p>
                <div class="space-2"></div>

                <p class="font16 my-input-title">游戏密码</p>
                <input type="password" class="my-input pw-input">
                <p class="w_tips">请输入正确游戏密码</p>
                <div class="space-2"></div>

                <div class="bottom-btn j-submitBtn" onclick="resetPdq()">确认重置</div>
                <div class="space-5"></div>
            </div>
        </div>
    </div>
</div>

<input type="hidden" value="${session.customer.level}" id="j-num-level"/>
<input type="hidden" value="${session.customer.loginname}" id="j-loginName"/>
<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="/js/layer/mobile/layer.js"></script>
<!-- <script type="text/javascript" src="/js/plugins/clip.js"></script> -->
<script>
    $('.comm-other-button').click(function(){
        window.location.href='https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19';
    });
    $('.center-list .title').on('click', function () {
        console.log(111)
        var ind = $('.center-list .title').index(this);
        $('#j-title-content').hide();
        $('#j-page-content').show();
        $('#j-page-content .full-page').eq(ind).show().siblings('.full-page').hide();
    });
    $('.tab-btn a').on('click',function () {
        var $that = $(this);
        var $target = $that.attr("data-target");
        if ($that.hasClass('active')) {
            return false;
        } else {
            $that.addClass('active').siblings().removeClass('active');
        }
        $("#" + $target).addClass("active").siblings().removeClass("active");
    })
    function closeContent() {
        $('#j-title-content').show();
        $('#j-page-content').hide();
    }
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = location.search.substr(1).match(reg);
        if (r != null) return unescape(decodeURI(r[2])); return null;
    }
//URL打开子导航
    function getTag() {
        var tagItem = getQueryString('tag');
        if(tagItem=="tag-password"){
            $('#tag-password').click();
        }else if(tagItem=="tag-account"){
            $('#tag-account').click();
        }else if(tagItem=="tag-unlock"){
            $('#tag-unlock').click();
        }else if(tagItem=="tag-unbank"){
            $('#tag-unbank').click();
        }else {
        }
    }
    getTag();
//验证
    $('input.my-input').on('blur',function () {
        if($(this).hasClass('code-input')){
            $(this).next().next('.w_tips').css('visibility','hidden');
        }
        $(this).next('.w_tips').css('visibility','hidden');
    })
    function checkInput ($that,message) {
        $that.next('.w_tips').css('visibility','inherit');
        if(message){
            $that.next('.w_tips').html(message)
        }
    }

    //忘记密码短信找回
    function sendDx(){
        var name = $("#forget_sms .ga-input").val();
        var phone = $("#forget_sms .phone-input").val();
        if(null==name||name==""){
            checkInput($("#forget_sms .ga-input"));
            return false;
        }
        if(null==phone||phone==""){
            checkInput($("#forget_sms .phone-input"));
            return false;
        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/getPwd/getbackPwdByDx.aspx",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            data : "name="+name+"&phone=" + phone+"&check_address=0&check_key=0&sid=0",
            async : false, // 异步
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
                        $("#forget_sms .ga-input").val('');
                        $("#forget_sms .phone-input").val('');
                    }
                });
            },
        });
    }
    //忘记密码邮箱找回
    function sendEmails(){

        var name = $("#forget_email .ga-input").val();
        var yxdz = $("#forget_email .email-input").val();
        var code = $("#forget_email .code-input").val();
        if(null==name||name==""){
            checkInput($("#forget_email .ga-input"));
            return false;
        }
        if(null==yxdz||yxdz==""){
            checkInput($("#forget_email .email-input"));
            return false;
        }
        if(null==code||code==""){
            checkInput($("#imgCode1"));
            return false;
        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/getPwd/getbackPwdByEmail.aspx",
            type : "post",
            dataType : "text",
            data : "name="+name+"&yxdz=" + yxdz+"&code="+code,
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
                        $('#imgCode1').src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random();
                        $("#forget_email .ga-input").val('');
                        $("#forget_email .email-input").val('');
                        $("#forget_email .code-input").val('');
                    }
                });
            }
        });
    }
    //忘记账户短信找回
    function getForgetAccBySms(){
        var phone = $("#forget_a_sms .phone-input").val();
//        var code = $("#forget_a_sms .code-input").val();
        if(null==phone||phone==""){
            checkInput($("#forget_a_sms .phone-input"));
            return false;
        }
//        if(null==code||code==""){
//            checkInput($("#imgCode2"));
//            return false;
//        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/asp/getForgetAccbySms.aspx",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            data : {phone:phone},
            async : false, // 异步
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
//                        $('#imgCode2').src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random();
                        $("#forget_a_sms .phone-input").val('');
//                        $("#forget_a_sms .code-input").val('');
                    }
                });
            },
        });
    }
    // 邮箱找回帐号
    function getForgetAccByEm(){
        var email = $("#forget_a_email .email-input").val();
//        var code = $("#forget_a_email .code-input").val();
        if(null==email||email==""){
            checkInput($("#forget_a_email .email-input"));
            return false;
        }
//        if(null==code||code==""){
//            checkInput($("#imgCode3"));
//            return false;
//        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/asp/getForgetAccbyEmail.aspx?",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            data : {email:email},
            async : false, // 异步
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
//                        $('#imgCode3').src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random();
                        $("#forget_a_email .email-input").val('');
//                        $("#forget_a_email .code-input").val('');
                    }
                });
            },
        });
    }
    //个人信息解冻
    function getBackAcc() {
        var loginname = $("#un_ac_info .ga-input").val();
        var accountName = $("#un_ac_info .name-input").val();
        var phone = $("#un_ac_info .phone-input").val();
        var email = $("#un_ac_info .email-input").val();
        if(null==loginname||loginname==""){
            checkInput($("#un_ac_info .ga-input"));
            return false;
        }
        if(null==accountName||accountName==""){
            checkInput($("#un_ac_info .name-input"));
            return false;
        }
        if(null==phone||phone==""){
            checkInput($("#un_ac_info .phone-input"));
            return false;
        }
        if(null==email||email==""){
            checkInput($("#un_ac_info .email-input"));
            return false;
        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/asp/unlockAccountByInfo.aspx?",
            type : "post",
            dataType : "text",
            data : {loginname:loginname,accountName:accountName,phone:phone,email:email},
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
                        $("#un_ac_info .ga-input").val('');
                        $("#un_ac_info .name-input").val('');
                        $("#un_ac_info .phone-input").val('');
                        $("#un_ac_info .email-input").val('');
                    }
                });
            }
        });
    }
    //原密码解冻
    function unAccPw() {
        var loginname = $("#un_ac_pw .ga-input").val();
        var password = $("#un_ac_pw .pw-input").val();
        if(null==loginname||loginname==""){
            checkInput($("#un_ac_pw .ga-input"));
            return false;
        }
        if(null==password||password==""){
            checkInput($("#un_ac_pw .pw-input"));
            return false;
        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/asp/unlockAccountByPassword.aspx?",
            type : "post",
            dataType : "text",
            data : {loginname:loginname,password:password},
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
                        $("#un_ac_pw .ga-input").val('');
                        $("#un_ac_pw .pw-input").val('');
                    }
                });
            }
        });
    }
    //解绑银行卡号
    function unBankNo() {
        var bankno = $("#un_bank .bankc-input").val();
        if('${session.customer==null}'=='true'){
            if('${session.customer==null}'=='true'){
                layer.open({
                    content: [
                        '请您登录后操作'
                    ],
                    btn: ['前往登录', '取消'],
                    yes: function (index, layero) {
                        window.location.href = "/mobile/login.jsp";
                    },
                    no: function (index) {
                        layer.close(index);
                    }
                });
                return false;
            }
            return false;
        }
        if(null==bankno||bankno==""){
            checkInput($("#un_bank .bankc-input"));
            return false;
        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/asp/unBindBankinfo.aspx?",
            type : "post",
            dataType : "text",
            data : {bankno:bankno},
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
                        $("#un_bank .bankc-input").val('');
                    }
                });
            }
        });
    }
    //重置密保
    function resetPdq() {
        var accountName = $("#un_rst .name-input").val();
        var phone = $("#un_rst .phone-input").val();
        var email = $("#un_rst .email-input").val();
        var password = $("#un_rst .pw-input").val();
        if('${session.customer==null}'=='true'){
            layer.open({
                content: [
                    '请您登录后操作'
                ],
                btn: ['前往登录', '取消'],
                yes: function (index, layero) {
                    window.location.href = "/mobile/login.jsp";
                },
                no: function (index) {
                    layer.close(index);
                }
            });
            return false;
        }
        if(null==accountName||accountName==""){
            checkInput($("#un_rst .name-input"));
            console.log('121');
            return false;
        }
        if(null==phone||phone==""){
            checkInput($("#un_rst .phone-input"));
            return false;
        }
        if(null==email||email==""){
            checkInput($("#un_rst .email-input"));
            return false;
        }
        if(null==password||password==""){
            checkInput($("#un_rst .pw-input"));
            return false;
        }
        layer.open({type: 2, content: '处理中'});
        $.ajax({
            url : "/asp/unbindQuestion.aspx?",
            type : "post",
            dataType : "text",
            data : {accountName:accountName,phone:phone,email:email,password:password},
            success : function(msg) {
                layer.closeAll();
                layer.open({
                    content: [
                        msg
                    ],
                    btn: ["关闭"],
                    yes: function (index) {
                        layer.closeAll();
                        $("#un_rst .name-input").val('');
                        $("#un_rst .phone-input").val('');
                        $("#un_rst .email-input").val('');
                        $("#un_rst .pw-input").val('');
                    }
                });
            }
        });
    }
</script>
</body>
</html>