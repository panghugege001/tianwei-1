<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="/css/safeCenter.css">

</head> 
<body id="pageSafeCenter">
<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
<div class="safecenter-banner-wp">
    <div class="safecenter-con-box">
        <div class="safecenter-tab-box">
            <a href="javascript:;" id="tag-password" target-data="tab-password" class="">忘记登录密码</a>
            <a href="javascript:;" id="tag-account" target-data="tab-account" class="mid-a">忘记登录帐号</a>
            <a href="javascript:;" id="tag-unlock" target-data="tab-Unlock">自助解冻帐号</a>
            <a href="javascript:;" id="tag-unbank" target-data="tab-Unbank" class="mid-a">解绑银行卡</a>
            <!--<a href="javascript:;" id="tag-unpdq" target-data="tab-Unpdq">重置密保</a>-->
        </div>
    </div>
</div>
<div class="safecenter-con-wp">
    <div class="safecenter-con-box">
        <div id="tab-password" class="safecenter-tab-item active">
            <div class="con-item-box">
                <i class="icon icon1"></i>
                <h5>通过短信找回密码</h5>
                <p>如果你的手机号还在正常使用，请选择此方式</p>
                <a href="javascript:;" data-toggle="modal" data-target="#j-modal-sms" class="btn-gotofind">立即找回</a>
            </div>
            <!-- <div class="con-item-box">
                <i class="icon icon2"></i> 
                <h5>通过邮件找回密码</h5>
                <p>如果你的邮箱还在正常使用，请选择此方式</p>
                <a  href="javascript:;" data-toggle="modal" data-target="#j-modal-email" class="btn-gotofind">立即找回</a>
            </div>  -->         
            <div class="con-item-box">
                <i class="icon icon3"></i>
                <h5>通过客服找回密码</h5>
                <p>联系在线客服验证信息，立即找回密码</p>
                <a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" class="btn-gotofind">立即找回</a>
            </div>
        </div>
        <div id="tab-account" class="safecenter-tab-item">
            <div class="con-item-box">
                <i class="icon icon1"></i>
                <h5>通过短信找回帐号</h5>
                <p>如果你的手机号还在正常使用，请选择此方式</p>
                <a href="javascript:;" data-toggle="modal" data-target="#j-modal-sms-act" class="btn-gotofind">立即找回</a>
            </div>
            <!-- <div class="con-item-box">
                <i class="icon icon2"></i>
                <h5>通过邮件找回帐号</h5>
                <p>如果你的邮箱还在正常使用，请选择此方式</p>
                <a  href="javascript:;" data-toggle="modal" data-target="#j-modal-email-act" class="btn-gotofind">立即找回</a>
            </div> -->          
            <div class="con-item-box">
                <i class="icon icon3"></i>
                <h5>通过客服找回帐号</h5>
                <p>联系在线客服验证信息，立即找回密码</p>
                <a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" class="btn-gotofind">立即找回</a>
            </div>
        </div>
        <div id="tab-Unlock" class="safecenter-tab-item">
            <div id="untab1">
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <div class="ui-ipt divipt">
                        <h3 style="color: #e40200;">确认帐号信息，解除冻结 <span id="pwTab" style="">通过原密码解冻</span></h3>
                        <p>解除冻结后，可正常登录，管理资金进入和帐号信息</p>
                    </div>
                </div>
                <form action="" method="post">
                    <div class="ui-form-item">
                        <label class="ui-label">游戏帐号：</label>
                        <input class="ui-ipt" name="loginname" id="unlock_loginname" type="text" required>
                        <span class="wrong-tip">游戏帐号输入错误，请重新输入!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">注册姓名：</label>
                        <input class="ui-ipt" name="accountName" id="unlock_accountName" type="text" required>
                        <span class="wrong-tip">注册姓名输入错误，请重新输入!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">注册手机号：</label>
                        <input class="ui-ipt" maxlength="11" name="phone" id="unlock_phone" type="text" required>
                        <span class="wrong-tip">请输入正确的手机号码!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">注册邮箱：</label>
                        <input class="ui-ipt" name="email" id="unlock_email" type="text" required>
                        <span class="wrong-tip">请输入正确的邮箱帐号!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <input type="button" class="btn-gotofind" id="unlockBtn" onclick="getBackAcc()" value="去解冻">
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <div class="ui-ipt divipt contact_kf">
                            <p>记不清了？<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" class="c-red">请联系在线客服</a></p>
                        </div>
                    </div>
                </form>
            </div>

            <div id="untab2" style="display: none">
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <div class="ui-ipt divipt">
                        <h3>通过原密码解冻账号 <span id="IntrTab" style=" color: #fff; background: #ef8934; padding: 10px; margin-left: 20px; cursor: pointer; ">通过具体个人信息解冻</span></h3>
                    </div>
                </div>
                <form action="" method="post">
                    <div class="ui-form-item">
                        <label class="ui-label">游戏帐号：</label>
                        <input class="ui-ipt" name="loginname" id="unlock2_loginname" type="text" required>
                        <span class="wrong-tip">游戏帐号输入错误，请重新输入!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">游戏密码：</label>
                        <input class="ui-ipt" name="password" id="unlock_password" type="password" required>
                        <span class="wrong-tip">游戏密码输入错误，请重新输入!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <input type="button" class="btn-gotofind" id="pwUnlockBtn" onclick="unAccPw()" value="去解冻">
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <div class="ui-ipt divipt contact_kf">
                            <p>记不清了？<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" style="color: #e40200;">请联系在线客服</a></p>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div id="tab-Unbank" class="safecenter-tab-item">
            <div class="ui-form-item">
                <label class="ui-label"></label>
                <div class="ui-ipt divipt">
                    <h3 style="color: #e40200;">请确认输入已绑定过的卡号</h3>
                </div>
            </div>
            <form action="" method="post">
                <div class="ui-form-item">
                    <label class="ui-label">解绑银行卡号：</label>
                    <input class="ui-ipt" name="cardno" id="unbank_cardno" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" required>
                    <span class="wrong-tip">银行卡号输入错误，请重新输入!</span>
                </div>
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <input type="button" id="unbankBtn" class="btn-find" onclick="unBankNo()" value="去解绑">
                </div>
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <div class="ui-ipt divipt contact_kf">
                        <p>记不清了？<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" style="color: #e40200;">请联系在线客服</a></p>
                    </div>
                </div>
            </form>
        </div>

        <!--<div id="tab-Unpdq" class="safecenter-tab-item">-->
            <!--<div class="ui-form-item">-->
                <!--<label class="ui-label"></label>-->
                <!--<div class="ui-ipt divipt">-->
                    <!--<h3>请确认重置密保信息</h3>-->
                <!--</div>-->
            <!--</div>-->
            <!--<form action="" method="post">-->
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label">注册姓名：</label>-->
                    <!--<input class="ui-ipt" name="accountName" id="unpdq_accountName" type="text" required>-->
                    <!--<span class="wrong-tip">注册姓名输入错误，请重新输入!</span>-->
                <!--</div>-->
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label">注册手机号：</label>-->
                    <!--<input class="ui-ipt" maxlength="11" name="phone" id="unpdq_phone" type="text" required>-->
                    <!--<span class="wrong-tip">请输入正确的手机号码!</span>-->
                <!--</div>-->
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label">注册邮箱：</label>-->
                    <!--<input class="ui-ipt" name="email" id="unpdq_email" type="text" required>-->
                    <!--<span class="wrong-tip">请输入正确的邮箱帐号!</span>-->
                <!--</div>-->
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label">游戏密码：</label>-->
                    <!--<input class="ui-ipt" name="password" id="unpdq_password" type="password" required>-->
                    <!--<span class="wrong-tip">游戏密码输入错误，请重新输入!</span>-->
                <!--</div>-->
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label"></label>-->
                    <!--<input type="button" id="unpdqBtn" class="btn-find" onclick="resetPdq()" value="确认重置">-->
                <!--</div>-->
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label"></label>-->
                    <!--<div class="ui-ipt divipt contact_kf">-->
                        <!--<p>记不清了？<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" class="c-red">请联系在线客服</a></p>-->
                    <!--</div>-->
                <!--</div>-->
            <!--</form>-->


        <!--</div>-->
    </div>
</div>

<div id="j-modal-sms" class="modal" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-hd">
                <h4 class="modal-title">短信找回登录密码</h4>
                <button type="button" class="close" data-dismiss="modal">×</button>
            </div>
            <div class="modal-bd">
                <form action="" method="post">
                    <div class="ui-form-item">
                        <label class="ui-label">游戏帐号：</label>
                        <input class="ui-ipt" name="username" id="ipt_username" type="text" required>
                        <span class="wrong-tip">游戏帐号输入错误，请重新输入!</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">注册手机号：</label>
                        <input class="ui-ipt" maxlength="11" name="phone" id="ipt_phone" type="text" required>
                        <span class="wrong-tip">手机号输入错误，请重新输入!</span>
                    </div>                    
                    <!-- 验证码嵌入位置 -->
                    <div class="wrap tc" style="padding-left: 168px;">
                        <div id="captcha-target" style="display: inline-block;width: 290px;"></div>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <input type="button" class="btn-gotofind"  onclick="sendDx()" id='sendPhoneCodeBtn' value="确定">
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <div class="ui-ipt divipt contact_kf">
                            <p>记不清了？<a href="/safeCenter.jsp" class="c-red">更换其它找回方式</a></p>
                    </div>
                </div> 
                </form>
            </div>
        </div>
    </div>
</div>

<!-- <div id="j-modal-email" class="modal" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-hd">
                <h4 class="modal-title">通过邮箱找回密码</h4>
                <button type="button" class="close" data-dismiss="modal">×</button>
            </div>
            <div class="modal-bd">
                <div class="ui-form-item">
                    <label class="ui-label">帐号：</label>
                    <input class="ui-ipt" name="username" id="ipt_username_em" type="text">
                    <span class="wrong-tip">游戏帐号输入错误，请重新输入!</span>
                </div>
                <div class="ui-form-item">
                    <label class="ui-label">邮箱：</label>
                    <input class="ui-ipt" name="phone" id="ipt_mail_em" type="email">
                    <span class="wrong-tip">邮箱输入错误，请重新输入!</span>
                </div>
                <div class="ui-form-item">
                    <label class="ui-label">图形验证：</label>
                    <input class="ui-ipt" name="verificationcode_em" id="ipt_verificationcode_em" type="text">
                    <span class="wrong-tip">验证码输入错误，请重新输入!</span>
                    <img id="imgCode1" src="${ctx}/asp/agTryValidateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新" onclick="this.src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random()"  class="img_code" style="font-size: 16px;">
                </div>
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <input type="button" id="sendEmailBtn" class="btn-gotofind" value="确定" onclick="sendEmails()">
                </div>
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <div class="ui-ipt divipt contact_kf">
                        <p>记不清了？<a href="/safeCenter.jsp" class="c-red">更换其它找回方式</a></p>
                </div>
            </div>
        </div>
    </div>
</div>
</div> -->

<div id="j-modal-sms-act" class="modal" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-hd">
                <h4 class="modal-title">短信找回登录帐号</h4>
                <button type="button" class="close" data-dismiss="modal">×</button>
            </div>
            <div class="modal-bd">
                <form action="" method="post">
                    <div class="ui-form-item">
                        <label class="ui-label">注册手机号：</label>
                        <input class="ui-ipt" maxlength="11" name="phone" id="ipt_phone_act" type="text" required>
                        <span class="wrong-tip">手机号输入错误，请重新输入!</span>
                    </div>                    
                    <!--<div class="ui-form-item">-->
                        <!--<label class="ui-label">图形验证：</label>-->
                        <!--<input class="ui-ipt" name="verificationcode" id="ipt_verificationcode_act" type="text">-->
                        <!--<span class="wrong-tip">验证输入错误，请重新输入!</span>-->
                        <!--<img id="imgCode2" src="${ctx}/asp/agTryValidateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新" onclick="this.src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random()"  class="img_code" style="font-size: 16px;">-->
                    <!--</div>-->
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <input type="button" class="btn-gotofind" onclick="getForgetAccBySms()" id='sendPhoneCodeBtn_act' value="确定">
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label"></label>
                        <div class="ui-ipt divipt contact_kf">
                            <p>记不清了？<a href="/safeCenter.jsp" class="c-red">更换其它找回方式</a></p>
                    </div>
                </div> 
                </form>
            </div>
        </div>
    </div>
</div>

<div id="j-modal-email-act" class="modal" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-hd">
                <h4 class="modal-title">通过邮箱找回帐号</h4>
                <button type="button" class="close" data-dismiss="modal">×</button>

            </div>
            <div class="modal-bd">
                <div class="ui-form-item">
                    <label class="ui-label">邮箱：</label>
                    <input class="ui-ipt" name="email" id="ipt_email_act" type="email">
                    <span class="wrong-tip">邮箱输入错误，请重新输入!</span>
                </div>
                <!--<div class="ui-form-item">-->
                    <!--<label class="ui-label">图形验证：</label>-->
                    <!--<input class="ui-ipt" name="verificationcode" id="ipt_verificationcode2_act_act" type="text">-->
                    <!--<span class="wrong-tip">验证码输入错误，请重新输入!</span>-->
                    <!--<img id="imgCode3" src="${ctx}/asp/agTryValidateCodeForIndex.aspx" title="如果看不清验证码，请点图片刷新" onclick="this.src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random()"  class="img_code" style="font-size: 16px;">-->
                <!--</div>-->
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <input type="button" class="btn-gotofind" onclick="getForgetAccByEm();" id="senEmailBtn_act" value="确定">
                </div>
                <div class="ui-form-item">
                    <label class="ui-label"></label>
                    <div class="ui-ipt divipt contact_kf">
                        <p>记不清了？<a href="/safeCenter.jsp" class="c-red">更换其它找回方式</a></p>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<a  href="javascript:;" data-toggle="modal" data-target="#j-modal-sendok" style="display: none;"></a><!-- 触发密码发送成功弹框 -->
<div id="j-modal-sendok" class="modal safecenter-modal" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <h2>新密码发送成功</h2>
            <p><i class="icon_right"><img src="" alt=""></i>恭喜您，新密码已发送至手机短信188******请打开手机短信查看新密码。</p>
            <p>为了确保密码安全请及时修改哦！</p>
            <a href="/safeCenter.jsp" class="btn-gotofind modal-btn">知道了</a>
        </div>
    </div>
</div>

<a  href="javascript:;" data-toggle="modal" data-target="#j-modal-unblockok" style="display: none;"></a><!-- 触发解冻成功弹框 -->
<div id="j-modal-unblockok" class="modal safecenter-modal" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <h2>解冻成功</h2>
            <p><i class="icon_right"><img src="" alt=""></i>解冻成功！</p>
            <p>您的游戏帐号已成功解冻！</p>
            <a href="/safeCenter.jsp" class="btn-gotofind modal-btn">知道了</a>
        </div>
    </div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include> 
<script>
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = location.search.substr(1).match(reg);
        if (r != null) return unescape(decodeURI(r[2])); return null;
    }

    // TAB切换
    $('.safecenter-tab-box').find('a').on('click',function () {
        var $this = $(this),thisId = $this.attr('target-data');
        $this.addClass('active').siblings('.active').removeClass('active');
        $('#'+thisId).addClass('active').siblings('.active').removeClass('active');
    })
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
            $('#tag-password').click();
        }
    }
    getTag();
</script>
<script type="text/javascript">

    $('input.ui-ipt').on('blur',function () {
        $(this).siblings('.wrong-tip').hide();
    })
    $('#pwTab').on('click',function () {
        $('#untab1').hide();
        $('#untab2').show();
    })
    $('#IntrTab').on('click',function () {
        $('#untab1').show();
        $('#untab2').hide();
    })
    function checkInput ($that,message) {
        $that.siblings('.wrong-tip').show();
        if(message){
            $that.siblings('.wrong-tip').html(message)
        }
    }
    //重置密保
    function resetPdq() {
        var accountName = $("#unpdq_accountName").val();
        var phone = $("#unpdq_phone").val();
        var email = $("#unpdq_email").val();
        var password = $("#unpdq_password").val();
        if('${session.customer==null}'=='true'){
            alert('请先登录您的账号');
            return false;
        }
        if(null==accountName||accountName==""){
            closeProgressBar1();
            checkInput($("#unpdq_accountName"));
            return false;
        }
        if(null==phone||phone==""){
            closeProgressBar1();
            checkInput($("#unpdq_phone"));
            return false;
        }
        if(null==email||email==""){
            closeProgressBar1();
            checkInput($("#unpdq_email"));
            return false;
        }
        if(null==password||password==""){
            closeProgressBar1();
            checkInput($("#unpdq_password"));
            return false;
        }
        openProgressBar1();
        $.ajax({
            url : "/asp/unbindQuestion.aspx?",
            type : "post",
            dataType : "text",
            data : {accountName:accountName,phone:phone,email:email,password:password},
            success : function(msg) {
                closeProgressBar1();
                alert(msg);
                $("#unpdq_password").val('');
                $("#unpdq_accountName").val('');
                $("#unpdq_phone").val('');
                $("#unpdq_email").val('');
            }
        });
    }

    //解绑银行卡号
    function unBankNo() {
        var bankno = $("#unbank_cardno").val();
        if('${session.customer==null}'=='true'){
            alert('请先登录您的账号');
            return false;
        }
        if(null==bankno||bankno==""){
            closeProgressBar1();
            checkInput($("#unbank_cardno"));
            return false;
        }
        openProgressBar1();
        $.ajax({
            url : "/asp/unBindBankinfo.aspx?",
            type : "post",
            dataType : "text",
            data : {bankno:bankno},
            success : function(msg) {
                closeProgressBar1();
                alert(msg);
                $("#unbank_cardno").val('');
            }
        });
    }


    //原密码解冻
    function unAccPw() {
        var password = $("#unlock_password").val();
        var loginname = $("#unlock2_loginname").val();
        if(null==loginname||loginname==""){
            closeProgressBar1();
            checkInput($("#unlock2_loginname"));
            return false;
        }
        if(null==password||password==""){
            closeProgressBar1();
            checkInput($("#unlock_password"));
            return false;
        }
        openProgressBar1();
        $.ajax({
            url : "/asp/unlockAccountByPassword.aspx?",
            type : "post",
            dataType : "text",
            data : {loginname:loginname,password:password},
            success : function(msg) {
                closeProgressBar1();
                alert(msg);
                $("#unlock2_loginname").val('');
                $("#unlock_password").val('');
            }
        });
    }

    //帐号解冻
    function getBackAcc() {
        var loginname = $("#unlock_loginname").val();
        var accountName = $("#unlock_accountName").val();
        var phone = $("#unlock_phone").val();
        var email = $("#unlock_email").val();
        if(null==loginname||loginname==""){
            closeProgressBar1();
            checkInput($("#unlock_loginname"));
            return false;
        }
        if(null==accountName||accountName==""){
            closeProgressBar1();
            checkInput($("#unlock_accountName"));
            return false;
        }
        if(null==phone||phone==""){
            closeProgressBar1();
            checkInput($("#unlock_phone"));
            return false;
        }        
        if(null==email||email==""){
            closeProgressBar1();
            checkInput($("#unlock_email"));
            return false;
        }
        openProgressBar1();
        $.ajax({
            url : "/asp/unlockAccountByInfo.aspx?",
            type : "post",
            dataType : "text",
            data : {loginname:loginname,accountName:accountName,phone:phone,email:email},
            success : function(msg) {
                closeProgressBar1();
                alert(msg);
                $("#unlock_loginname").val('');
                $("#unlock_accountName").val('');
                $("#unlock_phone").val('');
                $("#unlock_email").val('');
            }
        });
    }
    //打开进度条
    function openProgressBar1(){
        var h = $(document).height();
        $(".showbox1").css({"z-index": "99999" });
        $(".overlay1").css({"height": h });
        $(".overlay1").css({'display':'block','opacity':'0.8'});
        $(".showbox1").stop(true).animate({'margin-top':'300px','opacity':'1'},200);
    }
    //关闭进度条
    function closeProgressBar1(){
        $(".showbox1").css({"z-index": "-99999" });
        $(".showbox1").stop(true).animate({'margin-top':'250px','opacity':'0'},400);
        $(".overlay1").css({'display':'none','opacity':'0'});
    }

    //document.oncontextmenu=stopFuntion;
    //邮件找回密码
    function sendEmails(){
        var name = $("#ipt_username_em").val();
        var yxdz = $("#ipt_mail_em").val();
        var code = $("#ipt_verificationcode_em").val();
        if(null==name||name==""){
            closeProgressBar1();
            checkInput($("#ipt_username_em"),'请输入正确的用户名');
            return false;
        }
        if(null==yxdz||yxdz==""){
            closeProgressBar1();
            checkInput($("#ipt_mail_em"));
            return false;
        }
        if(null==code||code==""){
            closeProgressBar1();
            checkInput($("#ipt_verificationcode_em"));
            return false;
        }
        openProgressBar1();
        $.ajax({
            url : "/getPwd/getbackPwdByEmail.aspx",
            type : "post",
            dataType : "text",
            data : "name="+name+"&yxdz=" + yxdz+"&code="+code,
            success : function(msg) {
                closeProgressBar1();
                alert(msg);
                $('#imgCode1').src='/asp/agTryValidateCodeForIndex.aspx?r='+Math.random();
                $("#ipt_username_em").val('');
                $("#ipt_mail_em").val('');
                $("#ipt_verificationcode_em").val('');
            }
        });
    }    
    //发送验证码
    function sendDx_bak(){
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
                openProgressBar1();
                var check_address =check_obj.check_address;
                var check_key =check_obj.check_key;
                $.ajax({
                    url : "/getPwd/getbackPwdByDx.aspx",
                    type : "post",
                    dataType : "text",
                    data : "name="+name+"&phone=" + phone+"&check_address="+check_address+"&check_key="+check_key,
                    async : false,
                    success : function(msg) {
                        closeProgressBar1();
                        $("#ipt_username").val('');
                        $("#ipt_phone").val('');
                        alert(msg);
                    }
                });
            }
        });
        /*********触点**********/
    }


    //短信找回密码 发送验证码 (点触验证码 addis)
    function sendDx(){
        /*********触点**********/
        var is_checked = false;
        var name = $("#ipt_username").val();
        var phone = $("#ipt_phone").val();
    
        $.ajax({
            url : "/getPwd/getbackPwdByDx.aspx",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            data : "name="+name+"&phone=" + phone+"&check_address=0&check_key=0&sid=0",
            async : false, // 异步
            success : function(msg) {
                closeProgressBar();
                $("#ipt_username").val('');
                $("#ipt_phone").val('');
                alert(msg);
            },
        });
    
        /*********触点**********/
    }
        // 短信找回帐号
    function getForgetAccBySms(){
        var is_checked = false;
        var phone = $("#ipt_phone_act").val();
//        var code = $("#ipt_verificationcode_act").val();
        if(null==phone||phone==""){
            closeProgressBar1();
            checkInput($("#ipt_phone_act"));
            return false;
        }
//        if(null==code||code==""){
//            closeProgressBar1();
//            checkInput($("#ipt_verificationcode_act"));
//            return false;
//        }
        $.ajax({
            url : "/asp/getForgetAccbySms.aspx",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            data : {phone:phone},
            async : false, // 异步
            success : function(msg) {
                closeProgressBar();
                $("#ipt_phone_act").val('');
                alert(msg);
            },
        });
    }
        // 邮箱找回帐号
    function getForgetAccByEm(){
        var is_checked = false;
        var email = $("#ipt_email_act").val();
//        var code = $("#ipt_verificationcode2_act_act").val();
        if(null==email||email==""){
            closeProgressBar1();
            checkInput($("#ipt_email_act"));
            return false;
        }
//        if(null==code||code==""){
//            closeProgressBar1();
//            checkInput($("#ipt_verificationcode2_act_act"));
//            return false;
//        }
        $.ajax({
            url : "/asp/getForgetAccbyEmail.aspx?",
            type : "post", // 请求方式
            dataType : "text", // 响应的数据类型
            data : {email:email},
            async : false, // 异步
            success : function(msg) {
                closeProgressBar();
                $("#ipt_email_act").val('');
                alert(msg);
            },
        });
    }

</script>

</body>
</html>