﻿
<%@page import="java.util.GregorianCalendar" %>
<%@page import="java.util.Calendar" %>
<%@page import="dfh.utils.Constants" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dfh.model.Users" %>
<!DOCTYPE html>
<html>

<head>
    <title>天威</title>
    <base href="<%=request.getRequestURL()%>"/>
    <jsp:include page="${ctx}/title.jsp"></jsp:include>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/user.css?v=10013"/>
    <link rel="stylesheet" href="${ctx}/css/center_box1.css?v=90000002"/>
    <script type="text/javascript" src="${ctx}/js/lib/layer/layer.js"></script>
    <script type="text/javascript" src="${ctx}/js/clip.js"></script>
    <!--    <script src="/js/highcharts.js"></script>-->
    <script src="js/user/usercheckin.js?v=6"></script>
</head>
<style>
    .mull {
        position: fixed;
        top: 0px;
        bottom: 0px;
        background: rgba(0, 0, 0, .5);
        width: 100%;
        height: 100%; 
        z-index: 8;
        display: none;
    } 

    .mas_show {
        width: 300px;
        height: 140px;
        background: #EEEEEE;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-top: -70px;
        margin-left: -150px;
        z-index: 9;
        border-radius: 10px;
        color: #333;
    }

    .title_mag {
        height: 80px;
        line-height: 80px;
        text-align: center;
        font-size: 16px;
    }

    .msg_btn ul li {
        width: 50%;
        text-align: center;
        font-size: 16px;
        border-top: 1px solid #D1D1D1;
        float: left;
        height: 60px;
        line-height: 60px;
        cursor: pointer;
    }

    .msg_btn ul li a {
        display: inline-block;
        width: 100%;
    }
</style>
<body class="user_body">

<div class="index-bg about-bj">
    <jsp:include page="${ctx}/tpl/header.jsp?v=1"></jsp:include>
    <div class="user_center"></div>
    <div class="container w_357">
        <jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
        <div class="cfx about-main">
            <div class="gb-sidenav">
                <jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>

            </div>
            <div class="gb-main-r tab-bd user-main">
                <div id="tab_deposit" class="user-tab-box tab-panel">
                    <jsp:include page="${ctx}/tpl/funds.jsp"></jsp:include>
                </div>
                <!--}存款-->

                <!--提款{-->
                <div id="tab_withdraw" class="user-tab-box tab-panel">
                    <div class="qukuang_box">
                        <ul>
                            <li class="active">
                                <a href="#qukuang" data-toggle="tab" aria-expanded="true">我要提款</a>
                            </li>
                            <li>
                                <a href="#yinhangka" data-toggle="tab" aria-expanded="true">我的银行卡</a>
                            </li>
                            <li>
                                <a data-toggle="tab" id="triggerSet" href="#tab-setpaypass">设置提款密码</a>
                            </li>
<!--                             <li>
                                <a href="#mibao" data-toggle="tab" aria-expanded="true">绑定密保</a>
                            </li> -->
                        </ul>
                    </div>
                    <div class="tab-panel active" id="qukuang">
                        <form method="post" id="requestform" action="" name="checkform" class="userform ui-form">
                            <input type="hidden" id="accountName" value="${session.customer.accountName}"/>
                            <div class="ui-form-item">
                                <label class="ui-label" for="ipt_username">天威账户：</label>
                                <span class="span_name">${customer.loginname }</span>
                                <label class="user_lable">账户余额：</label>
                                <span class="u_money">${customer.credit}元</span>
                            </div>
                            <div class="ui-form-item">
                                <label class="ui-label" for="ipt_password">帐户密码：</label>
                                <%--防止自动填充--%> <input type="password" style="display: none;"/>
                                <input name="password" id="mar_password" placeholder="游戏账户登录密码" class="ui-ipt"
                                       type="password">
                            </div>
                            <div class="ui-form-item">
                                <label class="ui-label" for="ipt_bank">取款银行：</label>
                                <!--<s:select id="bank" name="bank" onchange="getWithDrawBankNo(this.value)"
                                          list="%{#application.IssuingBankEnum}" cssClass="ui-ipt" listKey="issuingBank"
                                          listValue="issuingBankCode"></s:select>-->
                                <select id="bank" name="bank" onchange="getWithDrawBankNo(this.value)">
                                    <option>请选择</option>
                                </select>

                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label" for="accountNo">银行卡号：</label>
                                <input id="accountNo" name="accountNo" placeholder="请输入正确银行卡号/钱包地址" class="ui-ipt"
                                       type="text" value="" readonly>
                                <!--                            <a href="/manageData.jsp?card_binding" class="link cee6">绑定卡号</a>-->
                            </div>
                            <div class="ui-form-item" style="display: none">
                                <label class="ui-label" for="bankAddress">开户银行：</label>
                                <input type="text" class="ui-ipt" name="bankAddress" id="bankAddress" readonly>
                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label" for="mar_amount">取款金额：</label>
                                <input id="mar_amount" name="amount" placeholder="请输入金额" class="ui-ipt" type="text"
                                       value="">
                            </div>

                            <div class="ui-form-item j_questionflag"  style="display: none;">
                                <label class="ui-label" for="mar_questionid">密保问题：</label>
                                <s:select id="mar_questionid" name="questionid" list="%{#application.QuestionEnum}"
                                          cssClass="ui-ipt" listKey="code" listValue="text"></s:select>

                            </div>
                            <div class="ui-form-item j_questionflag">
                                <label class="ui-label" for="mar_answer">取款密码：</label>
                                <input style="width: 200px" id="mar_answer" placeholder="请输入您的取款密码" class="ui-ipt" type="password">
                                <input class="btn btn-danger user-btn-mod" onclick="$('#triggerSet').trigger('click')" type="button" style="position: relative;bottom: 4px;" value="设置提款密码">
                            </div>

                            <%--
                            <div class="ui-form-item">
                                <label class="ui-label">取款时间：</label>
                                <%
                                if (0 == user.getIsCashin()) {
                                %>
                                <label class="radio">
                                    <input type="radio" name="msflag" value="0">5分钟提款
                                </label>
                                <label class="radio">
                                    <input type="radio" name="msflag" value="1" checked>秒付提款（金牌赌神以下提款100-50000金牌赌神及以上100-200000）
                                </label>
                                <%
                                } else {
                                %>
                                <label class="radio">
                                    <input type="radio" name="msflag" value="0" checked>5分钟提款
                                </label>
                                <label class="radio">
                                    <input type="radio" name="msflag" value="1">秒付提款（金牌赌神以下提款100-50000金牌赌神及以上100-200000）
                                </label>
                                <%
                                }
                                %>
                            </div>
                            --%>
                            <%--<p class="mb10 c-red">支付宝为独立的第三方支付系统，不同于传统的现金流，不受银行监控，您的游戏资金更加有保障。</p>--%>
                            <div class="ui-form-item">
                                <label><input type="checkbox" value="male" name="agree" checked="checked">我已经读过
                                    <a class="" style="color: #50588c;" target="_blank"
                                       href="/aboutus.jsp#tab-agreement">《提款须知》</a>，并已清楚了解其规则。</label>
                            </div>
                            <div class="user_qukuang">
                                <input class="btn" type="button" id="J_btn_draw" onclick="return tkConfirm()"
                                       value="确认提交">
                            </div>
                        </form>
                        <div class="prompt-info" style="padding-left: 165px;">
                            <h3 class="tit c-huangse">温馨提示</h3>
                            <p>
                                1.提款验证以天威账号金额为基准，游戏账户的提款需要转账到天威账户。<br/> 2.中午11:50 - 14:00,AG后台结算时间提款会顺延到 14:00
                                后审批出款，谢谢配合。<br/>
                                3.建议您使用“久安钱包”申请提款，资金更便捷和安全。
                            </p>
                        </div>
                    </div>
                    <div class="tab-panel" id="yinhangka">
                        <ul class="user_bank">
                            <li class="add_bank">
                                <a href="javascript:;" class="bangding" style="display:inline-block;"><img
                                        src="images/user/add_back.jpg"/></a>
                            </li>

                        </ul>

                        <!--绑定银行卡{-->
                        <!--<div id="tab_card_binding" class="user-tab-box tab-panel">
                                <form class="ui-form" id="blindCardForm">
                                    <div class="ui-form-item">
                                        <label class="ui-label rq-value">银行账户：</label>
                                        <select id="bdbank1" class="ui-ipt" onchange="showyzmDiv(this.value)">
                                            <option value="">请选择</option>
                                            <s:iterator value="%{#application.IssuingBankEnum}" var="bk">
                                                <option value=<s:property value="#bk.issuingBankCode" />>
                                                <s:property value="#bk.issuingBank" />
                                                </option>
                                            </s:iterator>
                                        </select>
                                    </div>
                                    <div class="ui-form-item">
                                        <label class="ui-label rq-value" for="bankno">卡/折号：</label>
                                        <input type="text" name="bdbankno" id="bdbankno1" class="ui-ipt" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" data-rule-register-bdbankno="true" maxlength="20" placeholder="请输入正确银行卡号15-20位纯数字" />

                                    </div>
                                    <div class="ui-form-item">
                                        <%--防止自动填充--%> <input type="password" style="display: none;" />
                                        <label class="ui-label rq-value" for="password_login">登录密码：</label>
                                        <input type="password" class="ui-ipt" id="bdpassword1" maxlength="15" name="bdpassword" placeholder="您的帐户登陆密码" />
                                    </div>
                                    <div class="ui-form-item" id="zfbyzmDiv" style="display:none;">
                                        <label class="ui-label" for="password_login">验证码：</label>
                                        <input type="text" class="ui-ipt" id="bindingCode" maxlength="6" />
                                        <span class="checkcode1">
                    <a href="javascript:void(0)" class="disFlag" id="sendAlipayPhoneVoiceCodeBtn">语音验证</a>
                    <a href="javascript:void(0)" class="disFlag" id="sendAlipayPhoneCodeBtn">短信验证</a>
                </span>
                                    </div>
                                    <div class="my_bank">
                                        <input type="button" class="btn my_bank_tijiao" value="提交" onclick="return checkbandingform();" style="margin:0" />
                                        <input type="reset" onclick="clearBandingform();" class="btn my_bank_null" value="重置" style="margin:0 0 0 26px" />
                                    </div>
                                    <div class="prompt-info">
                                        <h3 class="tit c-huangse">温馨提示:</h3>
                                        <p>1.绑定银行卡/折号，可以免去您重复输入卡/折号的繁琐步骤 </p>
                                        <p class="c-red">2.只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。银行卡号绑定位数为10到30位。</p>
                                        <%--<p class="tip">支付宝为独立的第三方支付系统，不同于传统的现金流，不受银行监控，您的游戏资金更加安全有保障。</p>--%>
                                    </div>
                                </form>
                            </div>-->
                        <!--}绑定银行卡-->

                    </div>
                    <div id="tab-setpaypass" class="user-tab-box tab-panel">
                        <input type="text" id="noPayPassword" hidden value="${session.payPassword == null}">
                        <form id="passwordPayform" class="ui-form"  method="post">
                        <c:if test="${session.payPassword == null}"> 
                                <div class="ui-form-item">
                                  <label class="ui-label rq-value">登录密码：</label>
                                  <input type="password" class="ui-ipt" name="password" id="loginPassWord" placeholder="" autocomplete="off" required/>
                                  <span class="ipt-tip">*请输入登录密码</span> 
                                </div>

                                <div class="ui-form-item">
                                  <label class="ui-label rq-value">取款密码：</label>
                                  <input type="password" class="ui-ipt" id="newPayPassWord" autocomplete="off" name="new_content" placeholder=""  required  />
                                  <span class="ipt-tip">*请输入取款密码</span> 
                                </div>          
                        </c:if>
                        <c:if test="${session.payPassword != null}">
                                <div class="ui-form-item">
                                  <label class="ui-label rq-value">取款密码：</label>
                                  <input type="password" class="ui-ipt" name="content" id="oldPayPassWord" placeholder="" autocomplete="off" required/>
                                   <span class="ipt-tip">*请输入取款密码</span>
                                </div> 
                                <div class="ui-form-item">
                                  <label class="ui-label rq-value">新取款密码：</label>
                                  <input type="password" class="ui-ipt" id="newPayPassWord" autocomplete="off" name="new_content" placeholder=""  required  />
                                  <span class="ipt-tip">*请输入新取款密码</span>
                                </div>
                        </c:if>
                                <div class="ui-form-item">
                                  <input type="submit" class="btn btn-danger user-btn-mod" value="提交" onclick="return updateDatePayPassword()">
                                </div>
                          </form>
                          <div class="prompt-info" data-pay-way="3">
                                 <h3 class="tit">温馨提示:</h3>
                                <p class="c-red">1.取款密码为6位纯数字。例如：123456。</p>
                                <p class="c-red">2.已经设置取款密码，需要修改，请输入原始的取款密码。</p>
                                <p class="c-red">3.若忘记取款密码，请联系在线客服核实身份后重置取款密码。</p>
                                <p class="c-red">4.重置提款密码后，在原取款密码中任意输入6位数字，然后输入新取款密码即可完成绑定。</p>
                            </div>
                    </div>
                </div>
                <!--}提款-->

                <!--转账{-->
                <div id="tab_transfer" class="user-tab-box tab-panel">
                    <div class="zhuanzhang">
                        <ul>
                            <li class="active">
                                <a href="#tab-onetransger" data-toggle="tab" aria-expanded="true">转账</a>
                            </li>
                            <li>
                                <a href="#tab-redpacket" data-toggle="tab" aria-expanded="true">红包转账</a>
                            </li>
                        </ul>
                    </div>
                    <div class="tab-panel active" id="tab-onetransger">
                        <div class="zhuanzhang_box">
                            <form method="post" name="form1" class="ui-form">
                                <div class="ui-form-item">
                                    <label class="ui-label" for="">来源账户：</label>
                                    <select name="source_acc" id="transferGameOut" class="ui-ipt"
                                            onchange="transferMoneryOut(this.value);">
                                        <option value="self" selected="selected">天威账户</option>
                                        <option value="newpt">PT账户</option>
                                        <!-- <option value="dt">DT账户</option> -->
                                        <!-- <option value="mg">MG账户</option> -->
                                        <!-- <option value="png">PNG账户</option> -->
                                        <option value="agin">AG账户</option>
                                        <option value="ttg">TTG账户</option>
                                        <!-- <option value="qt">QT账户</option> -->
                                        <!-- <option value="nt">NT账户</option> -->
                                        <option value="n2live">N2Live账户</option>
                                        <option value="mwg">MWG大满贯帐户</option>
                                        <option value="qd">签到余额</option>
                                        <option value="sba">沙巴体育账户</option>
                                        <option value="chess">761棋牌</option>
                                         <option value="kyqp">开元棋牌</option>
                                         <option value="vr">VR彩票</option>
                                        <option value="fish">捕鱼帐户</option>
                                        <option value="pb">平博体育</option>
                                        <option value="bbin">BBIN帐户</option>
                                        <option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
                                        <option value="fanya">泛亚电竞</option>
                                        <%--
                                        <option value="n2live">N2Live账户</option>
                                        <option value="ebetapp">EBet真人账户</option>
                                        --%>
                                    </select>
                                    <span id="transferMoneryOutDiv" class="c-red">加载中..</span>
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label" for="">目标账户：</label>
                                    <select name="target_acc" id="transferGameIn" class="ui-ipt"
                                            onchange="transferMoneryIn(this.value);">
                                        <option value="newpt" selected>PT账户</option>
                                        <!--                     <option value="mg">MG账户</option>-->
                                        <!--                    <option value="png">PNG账户</option>-->
                                        <option value="self">天威账户</option>
                                        <!--                  <option value="dt">DT账户</option>-->
                                        <option value="agin">AG账户</option>
                                        <option value="ttg">TTG账户</option>
                                        <!--<option value="qt">QT账户</option>
                                        <option value="nt">NT账户</option>-->
                                        <option value="n2live">N2Live账户</option>
                                        <option value="mwg">MWG大满贯帐户</option>
                                        <option value="qd">签到余额</option>
                                        <option value="sba">沙巴体育账户</option>
                                        <option value="chess">761棋牌</option>
                                         <option value="kyqp">开元棋牌</option>
                                         <option value="vr">VR彩票</option>
                                        <option value="fish">捕鱼帐户</option>
                                        <option value="pb">平博体育</option>
                                        <option value="bbin">BBIN帐户</option>
                                        <option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
                                        <option value="fanya">泛亚电竞</option>

                                        <%--
                                        <option value="n2live">N2Live账户</option>
                                        <option value="ebetapp">EBet真人账户</option>
                                        --%>
                                    </select>
                                    <span id="transferMoneryInDiv" class="c-red">加载中..</span>
                                </div>
                                <div class="select_money">
                                    <ul>
                                        <li>10</li>
                                        <li>500</li>
                                        <li>1000</li>
                                        <li>5000</li>
                                        <li>其他金额</li>
                                    </ul>
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label" for="">转账金额：</label>
                                    <input id="transferGameMoney" class="ui-ipt" maxlength="10" type="text"/>
                                    <span class="c-red">请输入转账金额</span>
                                </div>
                                <div class="ui-form-item">
                                    <input type="button" class="btn yes_tijiao" onclick="return transferMonery();"
                                           value="提交" style="margin-left: -25px;"/>
                                    <input type="button" class="btn yes_huigui" value="一键额度回归" onclick="onekeyMonery();"
                                           style="margin:0 0 0 26px">

                                </div>
                            </form>
                        </div>
                        <div class="prompt-info">
                            <h3 class="tit c-huangse">温馨提示：</h3>
                            <p>1.老虎机账户"支持pt-SWwind、DT、MG、PNG、QT、NT，若要游玩以上平台请将额度转入"老虎机账户"即可。</p>
                            <p>2.请在户内转账前进行平台激活方可转账成功。</p>
                            <p>3.户内转账只支持整数转账。</p>
                            <p>4.进行户内转账时，请先关闭正在进行的游戏页面，避免出现错误。</p>
                        </div>
                    </div>
                    <div class="tab-panel" id="tab-redpacket">
                        <div class="zhuanzhang_box ">

                            <div class="ui-form-item">
                                <label class="ui-label" for="">来源账户：</label>
                                <select id="redRainOut" onchange="redRainMoneryOut(this.value);" class="ui-ipt">
                                    <option value="self" selected="selected">天威账户</option>
                                    <option value="redrain"> 红包账户</option>
                                </select>
                                <span id="redRainOutDiv" class="c-red">0.0元</span>
                            </div>
                            <div class="ui-form-item">
                                <label class="ui-label" for="">目标账户：</label>
                                <select id="redRainIn" onchange="redRainMoneryIn(this.value);" class="ui-ipt">
                                    <option value="">请选择目标帐户</option>
                                    <option value="redrain">红包账户</option>
                                </select>
                                <span id="redRainInDiv" class="c-red">请选择账户!</span>
                            </div>
                            <div class="ui-form-item">
                                <label class="ui-label" for="">转账金额：</label>
                                <input type="text" class="ui-ipt" id="redRainMoney" maxlength="10">
                            </div>
                            <div class="ui-form-item">
                                <input type="button" onclick="return redRainMonery();" class="btn yes_tijiao"
                                       value="执行转账">
                            </div>

                            <!--<div class="mb20 clearfix">-->
                            <!--<p class="user-text fl">好友账户</p>-->
                            <!--<p class="fl">-->
                            <!--<input type="text" id="redrainToFriendName" maxlength="10" class="ui-ipt" placeholder="请填写好友的游戏账号">-->
                            <!--</p>-->
                            <!--</div>-->
                            <!--<div class="mb20 clearfix">-->
                            <!--<p class="user-text fl"><label>转账金额</label></p>-->
                            <!--<p class="fl">-->
                            <!--<input type="text" id="redrainToFriendMoney" maxlength="10" class="ui-ipt" placeholder="转账金额至少10元">-->
                            <!--</p>-->
                            <!--</div>-->

                            <!--<div class="mb20 clearfix">-->
                            <!--<p class="user-text fl"></p>-->
                            <!--<p class="fl w460">-->
                            <!--<input type="button" onclick="return redrainForFriend('tOFriend');" class="btn submit-btn" value="转给好友">-->
                            <!--</p>-->
                            <!--</div>-->
                            <div class="prompt-info">
                                <p class="tit c-huangse">温馨提示：</p>
                                <p>1、存款红包一天可领取一次。</p>
                                <p>2、满10元可以转到任意游戏平台，1倍流水即可提款。</p>
                            </div>
                        </div>
                    </div>
                </div>
                <!--}转账-->

                <!--推荐好友{-->
                <div id="tab-friends" class="user-tab-box tab-panel">
                    <div class="friends_box">
                        <ul>
                            <li class="active">
                                <a href="#tuijian-friends" data-toggle="tab" aria-expanded="true">推荐好友</a>
                            </li>
                            <li>
                                <a href="#record-hy" id="friends-records-action" data-toggle="tab" aria-expanded="true"
                                   data-href="/asp/queryfriendRecord.aspx">推荐记录</a>
                            </li>
                        </ul>
                    </div>
                    <div id="tuijian-friends" class="tab-panel active">
                        <div class="ui-form" style="min-height: 400px;">
                            <div class="ewm-box">
                                <h3 class="ewm-header">专属推荐二维码</h3>
                                <div id="fCode"></div>
                            </div>
                            <div class="link-box">
                                <h3 class="ewm-header">专属推荐链接</h3>
                                <a href="javascript:;" class="link" id='friendurl'></a>
                                <a href="javascript:;" class="btn-sm url-btn" data-clipboard-action="copy"
                                   data-clipboard-target="#friendurl">复制</a>
                                <div class="prompt-info">
                                    <h3 class="tit" style="color: #dfa85a">温馨提醒:</h3>
                                    <p>1.点击复制，可保存推荐链接，提供给您的好友进行注册。</p>
                                    <p>2.扫码即可下载，方便快速!</p>
                                </div>
                            </div>
                            <table class="table">
                                <thead>
                                <tr>
                                    <td>天威所有会员</td>
                                    <td>被推荐人存款</td>
                                    <td>彩金</td>
                                    <td>流水倍数</td>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td rowspan="5">推荐人</td>
                                    <td>100</td>
                                    <td>18</td>
                                    <td rowspan="5">10</td>
                                </tr>
                                <tr>
                                    <td>1000</td>
                                    <td>38</td>
                                </tr>
                                <tr>
                                    <td>5000</td>
                                    <td>68</td>
                                </tr>
                                <tr>
                                    <td>10000</td>
                                    <td>168</td>
                                </tr>
                                <tr>
                                    <td>50000</td>
                                    <td>688</td>
                                </tr>
                                </tbody>
                            </table>
                            <div>活动规则：</div>
                            <div class="tab-content2 tab-item-con active" style="padding: 20px;">
                                <p>1、登入游戏帐号进入【我/帐户管理】 → 【推广好友】 → 【网址推广】</p>
                                <p>复制您的链结分享给好友进行注册，并且存款游戏，推荐人即可获得对应彩金。</p>
                                <p>2、申请方式：发送邮件到tianwei661@gmail.com申请。</p>
                                <p>标题：邀请好友双向彩金，</p>
                                <p>内容：被邀请人帐号、邀请人帐号。</p>
                                <p>3、每个存款阶段皆可申请一次彩金，达到流水倍数即可提款。</p>
                                <p>4、如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡，同一电脑等其他任何不正常
                                    投注行为，我们将有权冻结账号并收回盈利。</p>
                                <p>5、天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
                            </div>
                        </div>
                    </div>
                    <!--好友推荐记录-->
                    <div id="record-hy" class="page tab-panel record">
                        <iframe src="" width="100%" height="780" border="0" scrolling="no" frameborder="0"
                                allowtransparency="true" class="if-record">
                        </iframe>
                    </div>
                </div>
                <!--}推荐好友-->

                <!--    自助优惠-->
                <div id="tab-youhui" class="user-tab-box tab-panel">
                    <div class="youhui_mull">
                        <ul>
                            <li class="active">
                                <a data-toggle="tab" href="#tab-pt2">体验金</a>
                            </li>
                            <li class="">
                                <a data-toggle="tab" href="#tab-persent">自助存送</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-help" onclick="losePromoRecord(1);">救援金</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-return" onclick="getXimaEndTime($('#platform').val());">自助返水</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-level" data-url="/asp/queryBetOfPlatform.aspx"
                                   onclick="loadIframe(this);">自助晋级</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-coupon">优惠券专区</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-chouma">免费筹码</a>
                            </li>
                            <li>
                                <a data-toggle="tab"  href="#tab-birthday">生日礼金</a>
                            </li>
                        </ul>
                    </div>
                    <div id="tab-weeksent" class="user-tab-box tab-panel">
                        <div id="weekSentRecordDiv" class="new"></div>
                    </div>
                       <!--生日礼金 -->
                    <div id="tab-birthday" class="user-tab-box tab-panel">
                        <div class="birthday-box">
                           
                            <div class="birthday-body">
                                <!-- 可以领取 -->
                                <div class="birthday-succes">
                                    <div class="item"><span class="s-title">会员生日：</span> 
                                        <b class="input" id="birthday-birth">...</b> 
                                        <a class="birthday-user-right" id="draw-record">领取记录</a>
                                    </div>
                                    <div class="item"><span class="s-title">会员等级：</span> <b class="input" id="vip-grade">。。。</b></div>
                                    <div class="item"><span class="s-title" style="position: relative;top: -58px;">生日礼金：</span>  
                                        <div class="ticket">
                                            <img src="/images/usermanage/bo1.png"">
                                            <div class="ticket-top">
                                                <span class="b-moeny"><b id="birthday-moeny">0</b>元</span>
                                            </div>
                                           <!--  <div class="ticket-bottom">
                                                有效期：2018.08.09--2018.08.15
                                            </div> -->
                                        </div>
                                    </div>
                                   
                                    <a class="birthday-btn gray" id="redCoupon-submit">立即领取</a>
                                </div>
                                <!-- 已经领取过 -->
                                <!-- <div class="birthday-carry">
                                    <p>生日礼金<br/>
                                        <span>88</span>元<br/>
                                        天威期待与您的下一次相约！
                                    </p>
                                </div> -->
                                <!-- 不可以领取 -->
                               <!--  <div class="birthday-no">
                                    <p>亲爱的<span class="vip-grade1"><span>会员，在生日前后三天内您可领取生日礼金！</p>
                                    <a class="birthday-btn-no">立即领取</a>
                                </div> -->
                            </div>
                        </div>
                        <div class="birthday-text">
                            <div class="birthday-text-left">
                                <p>一年一度的生日，除了蛋糕、鲜花，别忘了还有天威的 生日大红包哦！生日当天的等级越高，领到的红包越大。</p>
                                <h3 class="yeelow-te">温馨提示</h3>
                                <ol>
                                    <li>请注意每年您生日的前后三天可以进行自助领取礼金，例如5月12日生日，可在5月9-15日领取，逾期无法进行补发。
                                    </li>
                                    <li>生日礼金无需流水直接派发至您的主账户里。</li>
                                    <li>若有任何疑问请咨询在线客服。</li>
                                </ol>
                            </div>
                            <div class="birthday-text-right">
                                <table>
                                    <tr>
                                        <th>VIP等级</th>
                                        <th>生日礼包(元)</th>
                                    </tr>
                                    <tr>
                                        <td>天将</td>
                                        <td>18</td>
                                    </tr>
                                    <tr>
                                        <td>天王</td>
                                        <td>88</td>
                                    </tr>
                                    <tr>
                                        <td>星君</td>
                                        <td>188</td>
                                    </tr>
                                    <tr>
                                        <td>真君</td>
                                        <td>388</td>
                                    </tr>
                                    <tr>
                                        <td>仙君</td>
                                        <td>888</td>
                                    </tr>
                                    <tr>
                                        <td>帝君</td>
                                        <td>1888</td>
                                    </tr>
                                    <tr>
                                        <td>天尊</td>
                                        <td>2888</td>
                                    </tr>
                                    <tr>
                                        <td>天帝</td>
                                        <td>3888</td>
                                    </tr>
                                </table>
                                
                            </div>
                        </div>
                    </div>
                    <!--救援金{-->
                    <div id="tab-help" class="user-tab-box tab-panel">
                        <div id="losepromoRecordDiv" class="new"></div>
                        <div class="prompt-info">
                            <p>温馨提示</p>
                            <p class="c-red">救援金有效时间为60天（从派发当天开始计算），规定时间内未领取会自动取消。</p>
                        </div>
                    </div>
                    <!--}救援金-->
                    <!--体验金{-->
                    <div id="tab-pt2" class="user-tab-box active tab-panel">
                        <div class="pay-step-info">
                            <ol id="status" class="pay-step">
                                <li class="active"><em></em>
                                    <p class="step-tit">注册信息</p>
                                </li>
                                <li><em></em>
                                    <p class="step-tit">填写电话号码</p>
                                </li>
                                <li><em></em>
                                    <p class="step-tit">银行卡号验证</p>
                                </li>
                                <li><em></em>
                                    <p class="step-tit">完成</p>
                                </li>
                            </ol>
                        </div>
                        <div class="tab-bd">
                            <div class="tab-panel active">
                                <div class="ui-form-item">
                                    <label class="ui-label">用户昵称：</label> <input type="text" class="ui-ipt"
                                                                                 value="${customer.loginname}"
                                                                                 disabled="disabled">
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label">真实姓名：</label> <input type="text" class="ui-ipt"
                                                                                 value='<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.accountName, 1)+'
                                                                                 ** '" />'
                                                                                 disabled="disabled">
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label">手机号：</label> <input type="text" class="ui-ipt"
                                                                                value='<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.phone, 6)+'
                                                                                ***** '" />'
                                                                                disabled="disabled">
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label">邮箱：</label> <input type="text" class="ui-ipt"
                                                                               value='<s:property value="' ****
                                    '+@dfh.utils.StringUtil@subStrLast(#session.customer.email, 10)" />'
                                                                               disabled="disabled">
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label">注册IP：</label> <input type="text" class="ui-ipt"
                                                                                 value="${customer.registerIp}"
                                                                                 disabled="disabled">
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label">此次登录IP：</label> <input type="text" class="ui-ipt"
                                                                                   value="${customer.lastLoginIp}"
                                                                                   disabled="disabled">
                                </div>
                                <div class="ui-form-item">
                                    <a href="javascript:void(0);" class="btn btn-pay pt8next" data="1"
                                       title="下一步">下一步</a>
                                </div>
                            </div>
                            <div class="tab-panel fade">

                                <form id="phoneCheckValid">

                                    <div class="next-box1 ui-form">
                                        <div class="ui-form-item">
                                            <label for="" class="ui-label">电话号码：</label>
                                            <input type="text" name="aliasName" class="ui-ipt"
                                                   value='<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.phone, 6)+'
                                                   ***** '" />'
                                                   disabled="disabled">
                                        </div>
                                        <div class="ui-form-item">
                                            <!-- <a href="javascript:;" class="btn btn-danger" id="sendPhoneVoiceCodeBtn"
                                               style="">语音验证</a> -->
                                            <a href="javascript:;" class="btn btn-danger" id="sendPhoneCodeBtn"
                                               style="">短信验证</a>
                                        </div>
                                        <div class="ui-form-item">
                                            <label for="" class="ui-label">输入验证码：</label>
                                            <input type="text" id="phoneCode" class="ui-ipt">
                                        </div>
                                        <div class="ui-form-item">
                                            <a href="javascript:void(0);" class="btn btn-danger pt8next" data="2"
                                               title="下一步">下一步</a>
                                        </div>
                                    </div>
                                    <div class="next-box2 unis prompt-info">
                                        <h3 class="tit c-red">温馨提示</h3>
                                        <p id="_valideteTextId">1.短信安全验证(请用您注册时的手机号码发送短信 #(1)到#(2))</p>
                                        <p> 2.短信发送成功,<span class="c-red">请于15秒后点击"下一步"</span></p>
                                        <p>3.必须使用<span class="c-red">注册时填写的手机号码</span>进行发送短信!</p>
                                        <div class="ui-form-item">
                                            <a href="javascript:void(0);" class="btn btn-pay pt8next" data="2"
                                               title="下一步">下一步</a>
                                        </div>
                                    </div>
                                    <!-- 验证码嵌入位置 -->
                                    <div class="wrap">
                                        <div id="captcha-target" style="display: inline-block;width: 339px;"></div>
                                    </div>
                                </form>
                            </div>
                            <div class="tab-panel fade">
                                <div class="ui-form">
                                    <div class="ui-form-item">
                                        <label class="ui-label">开户行：</label>
                                        <select id="pt8_bank" name="" style="height: 38px;"
                                                onchange="getbankno(this.value);">
                                            <option value="">请选择</option>
                                            <option value="工商银行" selected="selected">工商银行</option>
                                            <option value="招商银行">招商银行</option>
                                            <option value="上海农村商业银行">上海农村商业银行</option>
                                            <option value="农业银行">农业银行</option>
                                            <option value="建设银行">建设银行</option>
                                            <option value="交通银行">交通银行</option>
                                            <option value="民生银行">民生银行</option>
                                            <option value="光大银行">光大银行</option>
                                            <option value="兴业银行">兴业银行</option>
                                            <option value="上海浦东银行">上海浦东银行</option>
                                            <option value="广东发展银行">广东发展银行</option>
                                            <option value="深圳发展银行">深圳发展银行</option>
                                            <option value="中国银行">中国银行</option>
                                            <option value="中信银行">中信银行</option>
                                            <option value="邮政银行">邮政银行</option>
                                        </select>
                                    </div>

                                    <div class="ui-form-item">
                                        <label class="ui-label">银行卡号：</label>
                                        <input type="text" id="yhBankNo" name="yhBankNo" class="ui-ipt"
                                               disabled="disabled">
                                    </div>
                                    <div class="ui-form-item">
                                        <a href="javascript:void(0);" class="btn btn-pay pt8next" data="3" title="下一步">下一步</a>
                                    </div>
                                </div>

                                <p class="c-red">温馨提示：此处只为查看银行绑定信息，不用填写，直接点击下一步即可！</p>
                            </div>

                            <div class="tab-panel fade">
                                <div class="ui-form">
                                    <div class="ui-form-item">
                                        <lable class="ui-lable">请选择您要转入的老虎机游戏平台：</lable>
                                        <label><input type="radio" name="myslotchoice" value="PT"
                                                      checked="checked"><span>PT</span></label>
                                        <label><input type="radio" name="myslotchoice"
                                                      value="MG"><span>MG</span></label>
                                        <label><input type="radio" name="myslotchoice"
                                                      value="DT"><span>DT</span></label>
                                        <label><input type="radio" name="myslotchoice"
                                                      value="TTG"><span>TTG</span></label>
                                        <label><input type="radio" name="myslotchoice"
                                                      value="NT"><span>NT</span></label>
                                        <label><input type="radio" name="myslotchoice"
                                                      value="QT"><span>QT</span></label>
                                        <a href="javascript:void(0);" class="btn btn-pay pt8sub" id="sub"
                                           title="确定">确定</a>
                                    </div>
                                </div>

                            </div>

                        </div>

                    </div>
                    <!--}体验金-->
                    <!--
                            <div id="tab-coupon" class="user-tab-box tab-panel">
                              <div>
                                  <h1 class="tab-tit">优惠券专区</h1>
                              </div>
                              <form class="userform" action="transferInforCoupon" id="transferform"
                                    name="transferInForm"  method="post" onsubmit="return submitRemit()">
                                <div class="ui-form-item">
                                  <label class="ui-label">从天威账户转账到</label>
                                  <select class="ui-ipt" id="couponType" name="couponType">
                                    <option>--请选择平台--</option>
                                     <option value="ttg">TTG</option>
                                     <option value="pt">PT</option>
                                     <option value="nt">NT</option>
                                     <option value="qt">QT</option>
                                  </select>
                                </div>

                                <div class="ui-form-item">
                                  <label class="ui-label">存款金额：</label>
                                  <input type="text" class="ui-ipt" style="font-weight:bold;" name="couponRemit" id="couponRemit"/>
                                </div>

                                <div class="ui-form-item">
                                  <label class="ui-label">优惠代码：</label>
                                  <input type="text" class="ui-ipt" name="couponCode" id="couponCode"/>
                                </div>
                                <div class="ui-form-item">
                                  <input type="button" class="btn btn-danger" value="提交" onclick="submitRemit();"/>
                                </div>
                              </form>
                              <div class="prompt-info">
                                <h2 class="tit">优惠券说明</h2>

                                <p>优惠券使用最低金额为100元,请选择正确的游戏平台，填写红利代码，确认提交，红利金额会自动添加到您转到的游戏平台里。</p>

                                <p>PT/TTG/NT/QT平台优惠券，需PT/TTG/NT/QT游戏账户低于5元才能使用存送优惠券。达到相应的有效投注额要求或PT/TTG/NT/QT游戏账户低于5元，才能再次进行PT/TTG/NT/QT户内转账。</p>
                                <p>优惠券有效期为30天，请您在有效期内进行使用。</p>
                                <p>如何得到优惠券，请留意天威最新的相关优惠信息。</p>
                              </div>
                            </div>
                             -->

                    <div id="tab-coupon" class="tab-panel">
                        <div class="tab-bd">
                            <div id="tab-coupon1" class="tab-panel active">
                                <div class="user-tab-box" style="margin:0;">
                                    <div class="zizhucunsog_box">
                                        <form class="userform" action="transferInforCoupon" id="transferform"
                                              name="transferInForm" method="post" onsubmit="return submitRemit()">
                                            <div class="ui-form-item">
                                                <label class="ui-label">从天威账户转账到</label>
                                                <select class="ui-ipt" id="platform">
                                                    <option value="pttiger">PT</option>
                                                    <option value="ttg">TTG</option>
                                                    <option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
                                                </select>
                                            </div>

                                            <div class="ui-form-item">
                                                <label class="ui-label">存款金额：</label>
                                                <input type="text" class="ui-ipt" style="font-weight:bold;"
                                                       name="couponRemit" id="couponRemit"/>
                                            </div>

                                            <div class="ui-form-item">
                                                <label class="ui-label">优惠代码：</label>
                                                <input type="text" class="ui-ipt" name="couponCode" id="couponCode"/>
                                            </div>
                                            <div class="user_tijiao">
                                                <input type="button" class="btn" value="提交" onclick="submitRemit();"/>
                                            </div>
                                        </form>
                                        <div class="prompt-info p_120">
                                            <h2 class="tit c-huangse">优惠券说明</h2>

                                            <p>100％存送优惠券：最低存款金额50元，流水倍数需15倍，上限588。</p>
                                            <p>88％存送优惠券：最低存款金额50元，流水倍数需13倍，上限888。</p>
                                            <p>68％存送优惠券：最低存款金额50元，流水倍数需10倍，上限1888。</p>
                                            <p>1.只限老虎机平台使用，需游戏账户低于5元才能使用存送优惠券，填写红利代码，确认游戏平台，提交后在相关的游戏里面会自动得到优惠礼金。</p>
                                            <p>2.达到流水倍数或游戏账户低于5元即可进行转入转出。</p>
                                            <p>3.优惠券为30天有效期限，逾时未使用恕不进行补发。</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div id="tab-coupon2" class="tab-panel">
                                <div class="user-tab-box">
                                    <div class="zizhucunsog_box">
                                        <form class="userform" class="ui-form" action="transferInforRedCoupon"
                                              id="transferRedform" name="transferInRedForm" method="post"
                                              onsubmit="return submitRedCouponRemit()">

                                            <div class="ui-form-item">
                                                <label class="ui-label">优惠代码：</label>
                                                <input type="text" class="ui-ipt" name="redcouponCode"
                                                       id="redcouponCode"/>
                                            </div>

                                            <div class="ui-form-item">
                                                <label class="ui-label">从天威账户转账到</label>
                                                <select class="ui-ipt" id="redcouponType" name="redcouponType">
                                                    <option>--请选择平台--</option>
                                                    <option value="ttg">TTG</option>
                                                    <option value="pt">PT</option>
                                                    <option value="nt">NT</option>
                                                    <option value="qt">QT</option>
                                                    <option value="mg">MG</option>
                                                    <option value="dt">DT</option>
                                                </select>
                                            </div>

                                            <div class="user_tijiao">
                                                <input type="button" class="btn" value="提交"
                                                       onclick="submitRedCouponRemit();"/>
                                            </div>
                                        </form>
                                        <div class="prompt-info p_120">
                                            <h2 class="tit c-huangse">红包优惠说明</h2>
                                            <p>1.不限平台使用，请选择正确的游戏平台。填写红利代码，确认提交， 红利金额会自动添加到您转到的游戏平台里。</p>
                                            <p>
                                                2.PT/TTG/NT/QT红包优惠券，需PT/TTG/NT/QT游戏账户低于5元才能使用红包优惠券。达到相应的有效投注额要求或PT/TTG/NT/QT游戏账户低于5元，才能再次进行PT/TTG/NT/QT户内转账。</p>
                                            <p>3.红包优惠券有效期为30天，请您在有效期内进行使用。</p>
                                            <p>4.如何得到红包优惠券，请留意天威最新的相关优惠信息。</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--免费筹码        -->
                    <div id="tab-chouma" class="user-tab-box tab-panel">
                        <div>
                            <!--<h1 class="tab-tit">自助优惠 ><span class="cee6">免费筹码</span></h1>
                                                            <ul id="j-pay-couma" class="user-nav tab-nav" style="margin:0;">
                                                                <li class="active"><a href="#tab-chouma" data-toggle="tab">免费筹码</a></li>
                                                                <%--<li><a href="#tab-coupon2" data-toggle="tab">红包优惠券</a></li>--%>
                                                            </ul>                      -->
                        </div>
                        <div class="zizhucunsog_box">
                            <form class="ui-form" action="" method="post">
                                <div class="ui-form-item">
                                    <label class="ui-label">会员等级：</label>
                                    <input type="text" class="ui-ipt" readonly
                                           value="<s:property value="
                                           @dfh.model.enums.VipLevel@getText(#session.customer.level) "/>">
                                </div>

                                <div class="ui-form-item need-hide" style="display: none;">
                                    <label class="ui-label">目标账户：</label>
                                    <select id="j-chipPaltform" class="ui-ipt">
                                        <!-- <option value="" selected="">请选择游戏平台</option>
            <option value="PT">PT</option>
            <option value="TTG">TTG</option>
            <option value="QT">QT</option>
            <option value="NT">NT</option>
            <option value="MG">MG</option>
            <option value="DT">DT</option>
            <option value="PNG">PNG</option> -->
                                    </select>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">筹码金额：</label>
                                    <input id="moneyVal" type="text" class="ui-ipt" readonly value="">
                                </div>
                                <div class="user_tijiao">
                                    <input type="button" class="btn" value="立即领取" onclick="getFreeChip()">
                                </div>
                                <div class="prompt-info p_120">
                                    <h3 class="tit c-huangse">温馨提示：</h3>
                                    <p>1.忠实会员及以上会员，每月可领取一次VIP免费筹码。</p>
                                    <p>2.免费筹码以您领取时的VIP等级来进行派发。</p>
                                    <p>3.免费筹码领取后，直接添加到天威主帐户。</p>
                                    <p>4.免费筹码无需流水即可提款。</p>
                                    <p>5.领取过程中有任何疑问，请咨询24小时在线客服。</p>
                                </div>
                            </form>
                        </div>
                    </div>
                  

                    <!--自助存送{-->
                    <div id="tab-persent" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">自助优惠 ><span class="cee6"> 自助存送</span></h1>
                        <s:url action="execXima" namespace="/asp" var="execXimaUrl"></s:url>
                        <div class="zizhucunsog_box">
                            <input type="button" class="btn bcbs-btn" value="笔存笔送" id="openBcbsModal"/>
                            <form class="ui-form" id="selform" action="${execXimaUrl }" method="post"
                                  onsubmit="return checkSubmit()">
                                <div class="ui-form-item">
                                    <label class="ui-label">选择平台：</label>
                                    <select class="ui-ipt" onchange="youHuiNameChange(this.value);" id="youhuiName">
                                        <option value="">---请选择平台---</option>
                                        <option value="6001">PT存送优惠</option>
                                        <!--<option value="6002">MG存送优惠</option>-->
                                        <!-- <option value="6003">DT存送优惠</option> -->
                                        <!--<option value="6004">QT存送优惠</option>-->
                                        <!--<option value="6005">NT存送优惠</option>-->
                                        <option value="6006">TTG存送优惠</option>
                                        <!--<option value="6007">PNG存送优惠</option>-->
                                        <option value="6008">AG真人存送优惠</option>
                                        <option value="6009">老虎机存送优惠(SW.MG.DT.PNG.QT.NT)</option>
                                    </select>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">优惠类型：</label>
                                    <select name="youhuiType" id="youhuiType" class="ui-ipt"
                                            onchange="youHuiTypeChange(this.value);">
                                        <option value="">---请选择存送类型---</option>
                                        <!-- <option value="590">PT首存优惠</option>
                                            <option value="591">PT次存优惠</option>
                                            <option value="592">EA次存优惠</option> -->
                                        <%--
                                        <option value="593">AG次存优惠</option>
                                        <option value="594">AGIN次存优惠</option>
                                        --%>
                                        <%--
                                        <option value="595">BBIN次存优惠</option>
                                        <option value="596">EBET首存优惠</option>
                                        <option value="597">EBET次存优惠</option>
                                        --%>
                                        <!-- <option value="598">TTG首存优惠</option>
                                            <option value="599">TTG次存优惠</option> -->
                                    </select>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">转账金额：</label>
                                    <input type="text" class="ui-ipt" name="transferMoney" id="transferMoney"
                                           onkeyup="clearNoNum(this);" onblur="getSelfYouhuiAmount(this.value);"/>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">红利金额：</label>
                                    <input type="text" class="ui-ipt" name="giftMoney" id="giftMoney" readonly/>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">流水倍数：</label>
                                    <input type="text" class="ui-ipt" style="margin-top:5px;" name="waterTimes"
                                           id="waterTimes" readonly/>
                                </div>
                                <div class="user_tijiao">
                                    <input type="button" class="btn" value="提交"
                                           onclick="return checkSelfYouHuiSubmit();"/>
                                </div>
                            </form>
                            <div class="prompt-info p_120">
                                <h3 class="tit c-huangse">温馨提示</h3>
                                <p>1.每天 00:00 - 01:00 系统结算时间,短暂时间无法使用自助存送</p>
                                <p>2.申请存送后彩金会自动添加到相应平台，您直接进入游戏即可</p>
                                <p>3.老虎机存送优惠，存款10元后方可自助操作</p>
                                <p>4.DT老虎机仅支持整数转账</p>
                            </div>
                            <div id="bcbs-mk"></div>
                            <div id="bcbsTable" class="bcbs-table">
                                <table class="table">
                                    <tbody>
                                    <tr>
                                        <th>单笔存款金额</th>
                                        <th>存送比率</th>
                                        <th>PT/TTG/老虎机钱包</th>
                                        <th>流水倍数</th>
                                    </tr>
                                    <tr>
                                        <td>10 - 499.99</td>
                                        <td>15%</td>
                                        <td>各可5次</td>
                                        <td rowspan="4">18倍</td>
                                    </tr>
                                    <tr>
                                        <td>500-4999.99</td>
                                        <td>20%</td>
                                        <td>各可6次</td>
                                    </tr>
                                    <tr>
                                        <td>5000-9999.99</td>
                                        <td>25%</td>
                                        <td>各可7次</td>
                                    </tr>
                                    <tr>
                                        <td>10000及以上</td>
                                        <td>30%</td>
                                        <td>各可8次</td>
                                    </tr>
                                    </tbody>
                                </table>
                                <input type="button" class="btn bcbs-btn" value="关闭弹窗" id="closeBcbsModal"/>
                            </div>
                        </div>
                    </div>
                    <!--}自助存送-->

                    <!-- 开户彩金-->
                    <div id="tab-caijin" class="user-tab-box tab-panel">
                        <h3 style="font-size: 22px;">下载app即送8-88彩金</h3>
                        <div class="fl box-hd">
                            <p>
                                IOS系统下载<br/>
                                下载方法：【1】<br/>
                                使用手机二维码扫描软件扫码下载
                            </p>
                            <div>
                                <img src="/images/appxiazai/longduapp.png"/>
                            </div>
                            <p>
                                下载方法：【2】<br/>
                                手机任意浏览器输入网址<br/>
                                www.tianwei4.com
                            </p>
                        </div>
                        <div class="fl box-hd">
                            <p>
                                Android系统下载<br/>
                                下载方法：【1】<br/>
                                使用手机二维码扫描软件扫码下载
                            </p>
                            <div>
                                <img src="/images/appxiazai/longduapp.png"/>
                            </div>
                            <p>
                                下载方法：【2】<br/>
                                手机任意浏览器输入网址<br/>
                                www.tianwei4.com
                            </p>
                        </div>
                        <div class="prompt-info p_120" style="padding-left: 82px;">
                            <h3 class="tit c-huangse" style="text-align: left;">温馨提示：</h3>
                            <p>
                                1.若苹果手机安装时出现未信任 请您进入手机点击通用-设置-设备管理点击对应条目添加信任即可！<br/>
                                2.登录游戏帐号后点击“帐户-自助优惠-自助体验金即可领取。<br/>
                            </p>
                        </div>
                    </div>

                    <!--自助反水{-->
                    <div id="tab-return" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">自助返水</h1>
                        <div class="zizhucunsog_box">
                            <s:url action="execXima" namespace="/asp" var="execXimaUrl"></s:url>
                            <form class="ui-form" id="selform2" action="${execXimaUrl }" method="post"
                                  onsubmit="return checkSubmit()">
                                <div class="ui-form-item">
                                    <label class="ui-label">游戏平台：</label>
                                    <select class="ui-ipt" id="platform2">
                                        <option value="pttiger">PT</option>
                                        <option value="ttg">TTG</option>
                                        <option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
                                    </select>
                                </div>
                                <div id="otherList">
                                    <div class="ui-form-item">
                                        <label class="ui-label" for="startTime">起点时间：</label>
                                        <input type="text" class="ui-ipt" readonly name="startTime" id="startTime"/>
                                    </div>

                                    <div class="ui-form-item">
                                        <label class="ui-label" for="endTime">截止时间：</label>
                                        <input type="text" class="ui-ipt" name="endTime" id="endTime"/>
                                        <!--                                        <input type="text" class="ui-ipt" name="endTime" id="endTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" onchange="getAutoXimaObject();"/> -->
                                    </div>

                                    <div class="ui-form-item">
                                        <label class="ui-label" for="validAmount">有效投注额：</label>
                                        <input type="text" class="ui-ipt" name="validAmount" id="validAmount" readonly/>
                                    </div>

                                    <div class="ui-form-item">
                                        <label class="ui-label" for="rate">返水比率：</label>
                                        <input type="text" class="ui-ipt" name="rate" id="rate" readonly/>
                                    </div>

                                    <div class="ui-form-item">
                                        <label class="ui-label" for="ximaAmount">返水金额：</label>
                                        <input type="text" class="ui-ipt" name="ximaAmount" id="ximaAmount" readonly/>
                                    </div>
                                    <div class="user_tijiao">
                                        <input type="button" class="btn" value="提交" onclick="return checkEaSubmit();"/>
                                    </div>
                                </div>
                                <div id="slotList" style="display: none;">
                                    <table class="tb-datalist tb-form userTable mt30" width="100%" cellspacing="0"
                                           cellpadding="0">
                                        <thead>
                                        <tr>
                                            <th>平台名称</th>
                                            <th>起始时间</th>
                                            <th>截止时间</th>
                                            <th>有效投注额</th>
                                            <th>返水比例</th>
                                            <th>返水金额</th>
                                        </tr>
                                        </thead>
                                        <tbody>

                                        </tbody>
                                        <tfoot class="total-count">
                                        <tr>
                                            <td colspan="5">合计金额</td>
                                            <td><span class="totalCount">0</span></td>
                                        </tr>
                                        </tfoot>
                                    </table>
                                    <!--<div class="total-count">
                                      <span>合计</span>
                                      <span class="totalCount">100</span>
                                    </div>-->
                                    <!--<tfoot class="total-count">
                                      <tr>
                                        <td>合计</td>
                                        <td></td>
                                        <td></td>
                                        <td><span class="totalCount">100</span></td>
                                      </tr>
                                    </tfoot>-->
                                    <input class="nextBtn btn btn-danger zizhusw" type="button" value="领取"
                                           onclick="return newExecXimaSubmit();" style="margin-left: 0">
                                </div>
                                <div class="prompt-info p_120">
                                    <h3 class="tit c-huangse">注意事项：</h3>
                                    <p>
                                        1.自助返水限定PT、MG、QT、NT、TTG、DT老虎机。（PT老虎机带有奖池类的无法进行自助返水，隔天进行系统返水。）<br/>
                                        2.自助返水的最低金额为1元。（要是您没达到自助返水的最低金额是不能自助返水的）。<br/>
                                        3.每天的00：00—03：00和12：00—15：00为系统结算时间，暂不能申请自助反水。自助返水结算数据时各平台统计时间为:PT、MG、QT、NT、TTG、DT结算的是前一天0点到23点59分59(上述为北京时间)。<br/>
                                        4.自助返水无次数限制，自助返水申请提交后，会在5分钟内通过审核，返水金额会自动添加到您的天威账号中。<br/>
                                    </p>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!--}自助反水-->
                    <div id="tab-level" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">自助优惠 ><span class="cee6"> 自助晋级</span></h1>
                        <div class="j-iframe"></div>
                        <div class="prompt-info" style="padding-top: 120px;">
                            <h2 class="tit c-huangse">温馨提示：</h2>
                            <p>1.当月只可晋级一次，如当月15号达到星级赌神流水要求选择自助晋级，30号达到钻石赌神流水要求将无法晋级</p>
                        </div>
                    </div>
                    <div id="tab-ptcrazy" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">自助优惠 ><span class="cee6"> PT疯狂礼金领取</span></h1>
                        <div id="ptBigBangDiv" class="new"></div>
                    </div>
                    <!--签到{-->
                    <div id="tab-checkin" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">自助优惠 ><span class="cee6"> 每日签到</span></h1>
                        <div class="ui-form">
                            <div class="ui-form-item">
                                <label for="" class="ui-label">签到奖金账户余额：</label>
                                <span class="c-red" id="qdmoney1"></span>
                            </div>
                            <div class="ui-form-item">
                                <label for="" class="ui-label">签到奖金账户：</label>
                                <select class="ui-ipt" id="signType">
                                    <option value=""> 请选择</option>
                                    <option value="ttg"> TTG</option>
                                    <option value="pt"> PT</option>
                                    <option value="nt"> NT</option>
                                    <option value="qt"> QT</option>
                                    <option value="mg"> MG</option>
                                    <option value="dt"> DT</option>
                                </select>
                            </div>
                            <div class="ui-form-item">
                                <label for="" class="ui-label">签到金额：</label>
                                <input type="text" class="ui-ipt" id="signRemit">
                            </div>
                            <div class="ui-form-item">
                                <a href="javascript:;" class="btn btn-pay" onclick="return submitSignRemit();">提交</a>
                            </div>
                        </div>

                        <div class="prompt-info">
                            <h3 class="tit">温馨提示</h3>
                            <p>1.每日存款10元以上，便会激活签到系统，每日仅能签到一次。</p>
                            <p>2.每次签到，签到彩金会自动加总，彩金达到10元以上，便可选择转入DT、MG、QT、NT、PNG、SW平台进行游戏。</p>
                            <p>3.签到彩金无流水限制。</p>
                        </div>
                    </div>
                    <!--}签到-->
                    <!--积分中心{-->
                    <div id="tab-point" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">自助优惠 ><span class="cee6"> 积分中心</span></h1>
                        <div class="ui-form">
                            <div class="ui-form-item">
                                <label for="" class="ui-label">可用积分：</label>
                                <span class="c-red" id="friendPoint"></span>
                            </div>
                            <div class="ui-form-item">
                                <label for="" class="ui-label">历史总积分：</label>
                                <span class="c-red" id="totalfriendPoint"></span>
                            </div>
                            <div class="ui-form-item">
                                <label for="" class="ui-label">当前兑换比率：</label>
                                <span class="c-red">
                                          <c:if test="${session.customer.level==0}">500积分兑换1元</c:if>
                            <c:if
                                    test="${session.customer.level==1}">400积分兑换1元</c:if>
                            <c:if
                                    test="${session.customer.level==2}">325积分兑换1元</c:if>
                            <c:if
                                    test="${session.customer.level==3}">280积分兑换1元</c:if>
                            <c:if
                                    test="${session.customer.level==4}">245积分兑换1元</c:if>
                            <c:if
                                    test="${session.customer.level==5}">220积分兑换1元</c:if>
                            <c:if
                                    test="${session.customer.level==6}">100积分兑换1元</c:if>
                                                ;可兑换奖金为：<span id='moneypoint'></span>
                                    </span>
                            </div>
                            <div class="ui-form-item">
                                <label for="" class="ui-label">积分兑换奖金到天威账户：</label>
                                <input type="text" id="pointRemit" class="ui-ipt"><span class="c-red">请输入兑换金额</span>
                            </div>
                            <div class="ui-form-item">
                                <a href="javascript:;" class="btn btn-pay" onclick="return submitPointRemit();">提交</a>
                            </div>
                        </div>

                        <div class="prompt-info">
                            <h3 class="tit">温馨提示:</h3>
                            <p>1.188体育平台投注不计算在积分内. </p>
                            <p>2.每天下午17:00-18:00派发前一天投注额所产生的积分。</p>
                            <p>3.积分兑换奖金处输入您对换的金额。</p>
                        </div>
                    </div>
                    <!--}积分中心-->
                    <%--
                    <div id="tab-task" class="user-tab-box tab-panel checkin-info">
                        <h1 class="tab-tit">摇摇乐</h1>
                        <div class="ui-form-item">
                            <label for="" class="ui-label">摇摇乐任务奖池：</label>
                            <span class="c-red" id="j-task-balance"></span>
                        </div>
                        <div class="ui-form-item">
                            <label for="" class="ui-label">转到E68账户：</label>
                            <input type="text" class="ui-ipt" id="j-task-amount">
                        </div>
                        <div class="ui-form-item">
                            <a href="javascript:;" class="btn btn-danger" onclick="return transferTaskAmount();">领取</a>
                        </div>
                        <div class="prompt-info">
                            <h3 class="tit">温馨提示</h3>
                            <p>【任务奖池】彩金累计必须大于10元。</p>
                            <p>详细记录请点击【账户清单】--【摇摇乐记录】查看</p>
                        </div>
                    </div>
                    --%>

                    <%--
                    <div id="tab-friends" class="user-tab-box tab-panel">--%>
                    <%--<h1 class="tab-tit">推荐好友</h1>--%>

                    <%--
                    <div class="ui-form">--%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label class="ui-label">您的推荐链接为：</label>--%>
                    <%--<a href="javascript:;" class="link" id='friendurl'></a>--%>
                    <%--<a href="javascript:;" class="btn-sm btn-copy j-copy">复制</a>--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label for="" class="ui-label">推荐奖金账户余额：</label>--%>
                    <%--<span class="c-red" id="friendmoney"></span>--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label for="" class="ui-label">从推荐奖金账户转到：</label>--%>
                    <%--<select class="ui-ipt" id="friendType">--%>
                    <%--
                    <option value=""> 请选择</option>
                    --%>
                    <%--
                    <option value="ttg"> TTG</option>
                    --%>
                    <%--
                    <option value="pt"> PT</option>
                    --%>
                    <%--
                    <option value="nt"> NT</option>
                    --%>
                    <%--
                    <option value="qt"> QT</option>
                    --%>
                    <%--
                    <option value="mg"> MG</option>
                    --%>
                    <%--
                    <option value="dt"> DT</option>
                    --%>
                    <%--</select>--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label for="" class="ui-label">转账金额：</label>--%>
                    <%--<input type="text" class="ui-ipt" id="friendRemit">--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label for="" class="ui-label"></label>--%>
                    <%--<a href="javascript:;" class="btn btn-danger" onclick="return submitFriendRemit();">提交</a>--%>
                    <%--
                </div>
                --%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="prompt-info">--%>
                    <%--<h3 class="tit">温馨提示:</h3>--%>
                    <%--<p>1.您的好友必须从您的<span class="c-red">专属链接</span>进来<span class="c-red">注册</span>。</p>--%>
                    <%--<p>2.您的好友必须成功<span class="c-red">领取体验金</span>才能成为您介绍的朋友，<span
                            class="c-red">这是永久性的</span>！</p>--%>
                    <%--<p>3.被推荐人当日产生的负盈利30%，将会在次日18点前派发予推荐人！</p>--%>
                    <%--<p>4.如有任何疑问请及时联系24小时在线客服 </p>--%>
                    <%--
                </div>
                --%>
                    <%--
                </div>
                --%>
                    <!-- 守护女神 -->
                    <%--
                    <div id="tab-goddess" class="user-tab-box tab-panel">--%>
                    <%--<h1 class="tab-tit">守护女神</h1>--%>
                    <%--
                    <div class="ui-form" id="goddessapply">--%>
                    <%--<label class="label" for="" class="ui-label">女神报名：</label>--%>
                    <%--<select id="applygoddess" name="applygoddess" class="ipt-txt">--%>
                    <%--
                    <option value="0" selected="selected"> 请选择</option>
                    --%>
                    <%--
                    <option value="BAISHIMOLINAI">白石茉莉奈</option>
                    --%>
                    <%--
                    <option value="DONGYUEFENG">冬月枫</option>
                    --%>
                    <%--
                    <option value="SHENGUANSHIZHI">神关诗织</option>
                    --%>
                    <%--
                    <option value="CHONGTIANXINGLI">冲田杏梨</option>
                    --%>
                    <%--
                    <option value="SANSHANGYOUYA">三上悠亚</option>
                    --%>
                    <%--</select>--%>
                    <%--<input type="button" class="btn btn-danger" value="报名" onclick="return goddessApply();">--%>
                    <%--
                </div>
                --%>

                    <%--
                    <div class="prompt-info">--%>
                    <%--<h3 class="tit">温馨提示:</h3>--%>
                    <%--<p>1.您的好友必须从您的<span class="c-red">专属链接</span>进来<span class="c-red">注册</span>。</p>--%>
                    <%--<p>2.您的好友必须成功<span class="c-red">领取体验金</span>才能成为您介绍的朋友，<span
                            class="c-red">这是永久性的</span>！</p>--%>
                    <%--<p>3.您的好友申请存送优惠您才有彩金，您推荐的好友每申请一笔存送优惠，您就有一笔彩金，您推荐的朋友越多，拿到的彩金也就越多哦！</p>--%>
                    <%--<p>4.如有任何疑问请及时联系24小时在线客服 </p>--%>
                    <%--
                </div>
                --%>
                    <%--
                </div>
                --%>

                    <!-- 全民闯关 -->
                    <%--
                    <div id="tab-qmcg" class="tab-panel user-tab-box">--%>
                    <%--<h1 class="tab-tit">天威闯关</h1>--%>
                    <%--
                    <div class="ui-form">--%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label class="ui-label" for="">闯关报名：</label>--%>
                    <%--<select id="applytype" name="applytype" class="ui-ipt">--%>
                    <%--
                    <option value="0" selected="selected"> 请选择</option>
                    --%>
                    <%--
                    <option value="1">天威-不屈白银</option>
                    --%>
                    <%--
                    <option value="2">天威-荣耀黄金</option>
                    --%>
                    <%--
                    <option value="3">天威-华贵铂金</option>
                    --%>
                    <%--
                    <option value="4">天威-璀璨钻石</option>
                    --%>
                    <%--
                    <option value="5">天威-最强王者</option>
                    --%>
                    <%--</select>--%>
                    <%--<input type="button" class="btn btn-danger" value="今日报名"
                               onclick="return EmigratedApply();">--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label class="ui-label">闯关奖金余额：</label>--%>
                    <%--<span id="emigratedMoney" class="red ml10"> <img
                            src="${ctx}/images/waiting.gif"/></span>--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label class="ui-label">目标账户：</label>--%>
                    <%--<select id="emigratedType" name="emigratedType" class="ui-ipt">--%>
                    <%--
                    <option value="0" selected="selected"> 请选择</option>
                    --%>
                    <%--
                    <option value="pt"> PT账户</option>
                    --%>
                    <%--
                    <option value="ttg"> TTG账户</option>
                    --%>
                    <%--
                    <option value="nt"> NT账户</option>
                    --%>
                    <%--
                    <option value="qt"> QT账户</option>
                    --%>
                    <%--</select>--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label class="ui-label">转账金额：</label>--%>
                    <%--<input type="text" id="emigratedRemit" maxlength="10" class="ui-ipt"/>--%>
                    <%--
                </div>
                --%>
                    <%--
                    <div class="ui-form-item">--%>
                    <%--<label class="ui-label"></label>--%>
                    <%--<input type="button" class="btn btn-danger" value="提交"
                               onclick="return submitEmigratedRemit();"/>--%>
                    <%--<input type="button" class="btn btn-danger" value="领取昨日奖励"
                               onclick="return doEmigrated();"/>--%>
                    <%--
                </div>
                --%>
                    <%--
                </div>
                --%>

                    <%--
                    <div class="prompt-info">--%>
                    <%--<h3 class="tit">活动规则</h3>--%>

                    <%--<p>闯关分为5个等级。当天存款100及以上就可以选择当日的任务级别进行报名。</p>--%>

                    <%--<p>每个平台达到相应流水就可以领取奖金，全部通关再送通关奖金，领取以后会在闯关奖金账户，满10元可以转到任意的平台，需20倍流水。</p>--%>

                    <%--<p>奖金与其他活动共享，当天达到条件后，次日10:00-24:00之前请到每日任务-天威闯关领取，逾期不能领取</p>--%>

                    <%--
                </div>
                --%>
                    <%--
                </div>
                --%>
                </div>

                <!--每日任务-->
                <div id="tab_mrrw" class="user-tab-box tab-panel">
                    <ul class="tab-muen-item clearfix user_mull" id="user_mull">
                        <li class="active">
                            <a href="#tab-user" data-toggle="tab">每日签到</a>
                        </li>
                        <li>
                            <a href="#tab-money" data-toggle="tab" aria-expanded="false">存款红包</a>
                        </li>
                        <!-- <li>
                            <a href="#tab-cunsong" data-toggle="tab" aria-expanded="false">每日存送</a>
                        </li> -->
                    </ul>
                    <div id="tab-user" class="tab-panel active letter-c tab_box">
                        <div class="tab-box" id="tab-checkIn">
                            <div class="tab-bd">
                                <div class="tab-bd-box">
                                    <!--签到中心-->
                                    <div id="tab-checkInCenter" class="tab-box">

                                        <div class="qiandao-con clearfix">

                                            <div class="qiandao-left">
                                                <div class="qiandao_top">
                                                    <p style="font-size: 16px;">签到余额:<span id="todayGet"
                                                                                           class="todayGet"
                                                                                           style="font-size: 24px;">0.00</span>
                                                    </p>
                                                    <div class="qiandao-top">
                                                        <div class="just-qiandao qiandao-sprits" id="j-qdA">立即签到</div>
                                                    </div>
                                                    <span class="xian_no1"></span>
                                                </div>
                                                <div class="qiandao-bottom">
                                                    <h3>温馨提示</h3>
                                                    <p style="color: #FFFFFF;">
                                                        累计签到7日，并达到每日存款要求， 即可领取红包存送优惠券。
                                                    </p>
                                                    <span class="show_qiandao">签到说明</span>
                                                </div>
                                            </div>

                                            <div class="qiandao-right">
                                                <div class="qiandao-left-top clearfix">
                                                    <div class="current-date"
                                                         style="text-align: center;font-size: 20px;"></div>
                                                </div>
                                                <ol class="cycle">
                                                    <li>周日</li>
                                                    <li>周一</li>
                                                    <li>周二</li>
                                                    <li>周三</li>
                                                    <li>周四</li>
                                                    <li>周五</li>
                                                    <li>周六</li>
                                                </ol>
                                                <div class="qiandao-main" id="js-qiandao-main">
                                                    <ul class="qiandao-list clearfix" id="js-qiandao-list">
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--签到说明-->
                                    <div id="tab-checkInInfo" class="tab-box tab-panel">
                                        <h3 class="qiandaotetle">签到说明</h3>
                                        <div class="modal-content" role="document">
                                                    <div class="pro-text">
                                                        <h2>每日签到:每日登入活跃获彩金</h2>
                                                        <h3>活动对象：</h3>
                                                        <p>天威所有会员</p>
                                                        <h3>活动时间:</h3>
                                                        <p>长期有效</p> 
                                                        <h3>活动内容：</h3>
                                                        <p>当月存款10元以上，每天即可在个人中心签到，每次签到彩金0.5元会自动转入『签到余额』，当累计超过10元以上，即可在个人中心-转账，选择签到余额转入老虎机平台游戏。</p>
                                                        <h3>活动规则：</h3>
                                                        <p>1.此活动彩金需完成10倍流水即可提款。</p>
                                                        <p>2.礼金账户累计大于10元，即可在『自助优惠』→『每日签到』选择转至老虎机游戏。</p>
                                                        <p>3.本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一/相似IP地址，同一银行卡，同一电脑等其他任何不正常行为，一经发现，我们将保留冻结帐户盈利及余额的权利。</p>
                                                        <p>4.本活动天威娱乐享有最终解释权。</p>
                                                    </div>
                                        </div>
                                        <span class="back_qiandao">返回签到</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="tab-money" class="tab-panel tab_box">
                        <div class="form_box meirhb">
                            <form method="post" name="form2" style="font-size: 14px;">
                                <table border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td>红包余额：</td>
                                        <td><span id="hbMoney"></span></td>
                                    </tr>
                                    <tr>
                                        <td>红包类型：</td>
                                        <td>
                                            <select id="hbSelect" name="hbSelect" class="input">

                                            </select>
                                        </td>
                                        <td class="sub" id="lingqu">
                                            <input type="button" value="领取红包" onclick="return doHB();"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>红包使用：</td>
                                        <td>
                                            <select id="hbType" name="hbType" class="input">
                                                <option value="0" selected="selected">请选择</option>
                                                <option value="pt">PT</option>
                                                <option value="ttg">TTG</option>
                                                <option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>转账金额：</td>
                                        <td><input type="text" class="text" id="hbRemit"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" class="sub" id="tijiao" style="padding-top: 30px;">
                                            <input type="button" onclick="return submitHBRemit();" class="mt10"
                                                   value="提交" style="margin-left:180px; padding: 0 50px;">
                                        </td>
                                    </tr>
                                </table>
                            </form>
                            <p class="p_style">
                                <span class="c-huangse">温馨提示:</span><br/> 1.同时段红包只能领取一类红包
                                <br/> 2.满10元可以转到任意的平台，需5倍流水
                            </p>
                        </div>
                    </div>
                    <div id="tab-cunsong" class="tab-panel tab_box">
                        <form class="ui-form" style="font-size: 14px;">
                            <div class="ui-form-item">
                                <label class="ui-label">存送优惠类型：</label>
                                <select name="youhuiType" id="youhuiType1" class="ui-ipt"
                                        onchange="youHuiTypeChange1(this.value);">
                                    <option value="">---请选择存送类型---</option>
                                </select>
                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label">转账金额：</label>
                                <input class="ui-ipt" type="text" name="transferMoney" id="transferMoney1"
                                       onblur="getSelfYouhuiAmount1(this.value);"/>
                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label">红利金额：</label>
                                <input class="ui-ipt" readonly type="text" name="giftMoney1" id="giftMoney1"/>
                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label">流水倍数：</label>
                                <input class="ui-ipt" type="text" name="waterTimes" id="waterTimes1"
                                       readonly="readonly"/>
                            </div>
                            <div class="ui-form-item" id="tijiao2">
                                <input type="button" class="btn" value="提交" onclick="return checkSelfYouHuiSubmit1();"/>
                            </div>
                        </form>
                        <div class="prompt-info" style=" padding-left: 150px; color: #727272; padding-bottom: 20px;">
                            <h3 class="c-huangse">温馨提示</h3>
                            <p>1.每天 00:00 - 01:00 系统结算时间,暂无法使用每日存送。</p>
                            <p>2.每日存送最低存款10元即可申请。</p>
                            <p>3.每日存送申请成功后系统会自动派发您相应的游戏平台账户，请登录查看并游戏。</p>
                        </div>
                    </div>
                </div>

                <!--账户清单    -->

                <div id="j-record-form" class="tab-bd user-main zhangdan user-tab-box tab-panel">
                    <ul id="j-records-action" class="ul-sidebar">
                        <li class="active">
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/depositRecords.aspx">在线存款</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/transferRecords.aspx">户内转账</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/cashinRecords.aspx">手工存款</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/withdrawRecords.aspx">提款记录</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/consRecords.aspx">优惠活动</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/couponRecords.aspx">优惠券</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/selfDetail_member.jsp">自助返水</a>
                        </li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/depositOrderRecord.aspx">存款附言</a>
                        </li>
                        <li onclick="redrecord()">
                            <a href="#j-record-form" data-toggle="tab" data-val="girl">红包雨</a>
                        </li>
                        <%--
                        <li><a href="#j-record-form" data-toggle=tab data-href="/asp/queryTaskRecords.aspx"></a></li>
                        <li>
                            <a href="#j-record-form" data-toggle="tab" data-href="/asp/queryfriendRecord.aspx">好友推荐</a>
                        </li>
                        <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/querypointRecord.aspx">积分中心</a>
                        </li>
                        <li><a href="#j-record-form" data-toggle="tab" data-val="girl"
                               data-href="/asp/queryFlowerRanking.aspx">守护女神</a></li>
                        --%>
                    </ul>
                    <div style="clear: both;">
                        <iframe src="" width="100%" height="780" border="0" scrolling="no" frameborder="0"
                                allowtransparency="true" class="if-record">
                        </iframe>
                        <div id="j-ret" style="padding-top: 10px;">
                            <table id="list-table1" class="table data-table">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>标题</th>
                                    <th>个数</th>
                                    <th>金额</th>
                                    <th>活动时间</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                            <div id="redRainpages" class="pagination">

                                <div class="pagination">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--会员信-->

                <div id="user-vip" class="user-tab-box tab-panel active">
                    <div class="ul_sidebar">
                        <ul>
                            <!--<li class="active">
                                <a href="#tab-centre" data-toggle="tab"
                                   aria-expanded="false">个人中心</a>
                            </li>-->
                            <li class="active">
                                <a data-toggle="tab" href="#tab-personal">个人资料</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-card-binding" class="check_name">银行卡绑定</a>
                            </li>
                            <li>
                                <a data-toggle="tab" href="#tab-repass">修改密码</a>
                            </li>
                            <!--<li>
                                    <a data-toggle="tab" href="#tab-sms" aria-expanded="true">短信通知</a>
                                </li>-->
                        </ul>
                    </div>
                    <div id="tab-personal" class="user-tab-box tab-panel active">
                        <div class="user_ziliao">
                            <s:url action="change_info" namespace="/asp" var="modifyUserInfoUrl"></s:url>
                            <form id="j-modify-form" action="${modifyUserInfoUrl}" method="post" class="ui-form">
                                <table class="tb-form">
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">天威账户：</label>
                                                <span class="ipt-value">${session.customer.loginname}</span>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">注册货币：</label><span class="ipt-value">人民币</span>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">账户余额：</label><span
                                                    class="ipt-value money_18">${session.customer.credit}<label>元</label></span>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">真实姓名：</label>
                                                <input class="ui-ipt" type="text" name="accountName" id="accountNameX"
                                                       data-rule-chinese-name="true" placeholder="请输入中文姓名"
                                                       value='<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1)" />'/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">出生年月：</label>
                                                <input class="ui-ipt" type="text" name="birthday" id="birthday"
                                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false"
                                                       readonly placeholder="请输入正确的生日日期" value="${session.birthday}"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">联络电话：</label><input class="ui-ipt" type="text"
                                                                                            value='<s:property value="@dfh.utils.StringUtil@mobilePhoneFormat(#session.customer.phone)"/>'
                                                                                            disabled>
                                            </div>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">邮箱：</label>
                                                <input class="ui-ipt" type="text" name='email' data-rule-email="true"
                                                       id='emailx'
                                                       value='<s:property value="@dfh.utils.StringUtil@emailFormat(#session.customer.email)"/>'/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">QQ：</label><input name="qq" class="ui-ipt"
                                                                                          type="text"
                                                                                          value='<s:property value="@dfh.utils.StringUtil@qqFormat(#session.customer.qq)" />'
                                                                                          data-rule-digits="true"
                                                                                          placeholder="请输入QQ"
                                                                                          maxlength="20">
                                            </div>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td>
                                            <div class="">
                                                <label class="ui-label">微信号：</label>
                                                <input name="microchannel" class="ui-ipt" type="text"
                                                       value='<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.microchannel, 0.7,0)"/>'
                                                       placeholder="请输入微信号" maxlength="20">
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <!--<td>
                                            <div style=" margin-left:130px;">
                                                <label>
                                        <s:if test="#session.customer.sms==0">
                                            <input type="checkbox" name="sms" class="chk" checked="checked"/>
                                        </s:if>
                                        <s:else>
                                            <input type="checkbox" name="sms" class="chk"/>
                                        </s:else>
                                        我要接收会员通讯及最新优惠计划
                                    </label>
                                            </div>
                                        </td>-->
                                    </tr>
                                    <tr>
                                        <td>
                                            <div><input class="btn tijiao" type="submit" value="确认提交" id="J_sub_info">
                                            </div>
                                        </td>
                                    </tr>
                                </table>

                                <!--  <div class="ui-form-item">
                <label class="ui-label rq-value">用户昵称：</label><input name="aliasName" class="ui-ipt" value="${session.customer.aliasName}" type="text" required maxlength="12" placeholder="请输入用户昵称">
              </div>-->

                                <!--  <div class="ui-form-item">
                <label class="ui-label">邮寄地址：</label><input name="mailaddress" class="ui-ipt" type="text"  value="<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.mailaddress, 3)+'******'" />"  maxlength="50" placeholder="请输入邮寄地址">
              </div>-->

                            </form>
                        </div>
                    </div>
                    <div id="tab-repass" class="user-tab-box tab-panel">
                        <div class="update_pass">
                            <h1 class="tab-tit">会员信息 ><span class="cee6"> 修改密码</span></h1>
                            <s:url action="change_pws" namespace="/asp" var="changePwdUrl"></s:url>
                            <form id="passwordform" class="ui-form" name="frmAction"
                                  action="${ctx}/asp/change_pwsAjax.aspx" method="post">
                                <div class="ui-form-item">
                                    <label class="ui-label">原密码：</label>
                                    <input type="password" class="ui-ipt" name="password" placeholder="请输入原密码"
                                           autocomplete="off" required/>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">新密码：</label>
                                    <input type="password" id="ipt_pwd" class="ui-ipt" autocomplete="off"
                                           name="new_password" placeholder="请输入新密码" maxlength="12" required
                                           data-rule-rangelength="8,12" data-rule-password1="true"/>
                                </div>

                                <div class="ui-form-item">
                                    <label class="ui-label">确认密码：</label>
                                    <input type="password" class="ui-ipt" autocomplete="off" name="sPass2"
                                           maxlength="12" placeholder="请输入确认密码" required data-rule-equalto="#ipt_pwd"/>
                                </div>

                                <div class="tijiao_box">
                                    <input type="submit" class="btn" value="确认提交" id="J_sub_repass">
                                </div>

                            </form>
                        </div>
                    </div>

                    <div id="tab-card-binding" class="user-tab-box tab-panel">
                        <div class="yinhangka_box">
                            <h1 class="tab-tit">会员信息 ><span class="cee6"> 银行卡绑定</span></h1>
                            <form id="blindCardForm" class="userform ui-form" action="${ctx}/asp/bandingBankno.aspx"
                                  method="post">
                                <div class="ui-form-item" style="display: none">
                                    <label class="ui-label rq-value">银行/支付宝账户：</label>
                                    <select id="bdbank" class="ui-ipt" onchange="showyzmDiv(this.value)">
                                        <option value="">请选择</option>
                                        <s:iterator value="%{#application.IssuingBankEnum}" var="bk">
                                            <option value=
                                                        <s:property value="#bk.issuingBankCode"/>
                                            >
                                                <s:property value="#bk.issuingBank"/>
                                            </option>
                                        </s:iterator>
                                    </select>
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label rq-value" for="bankno">卡/折号：</label>
                                    <input type="text" name="bdbankno" id="bdbankno" class="ui-ipt" maxlength="100"
                                           autocomplete="off"/>
                                </div>
                                <div class="ui-form-item c-red">
                                    <label class="ui-label" for="bankno">银行名称：</label>
                                    <input type="text" name="checkbankname" disabled id="checkbankname"
                                           class="ui-ipt c-red" autocomplete="off"/>
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label rq-value" for="password_login">登录密码：</label>
                                    <%--防止自动填充--%> <input type="password" style="display: none;"/>
                                    <input type="password" class="ui-ipt" id="bdpassword" maxlength="15"
                                           name="bdpassword" autocomplete="off"/>
                                </div>
                                <div class="ui-form-item" id="zfbyzmDiv" style="display:none;">
                                    <label class="ui-label" for="password_login">验证码：</label>
                                    <input type="text" class="ui-ipt" id="bindingCode" maxlength="6"/>
                                    <span class="checkcode1">
                                <a href="javascript:void(0)" class="disFlag link"
                                   id="sendAlipayPhoneVoiceCodeBtn">语音验证</a>
                                <a href="javascript:void(0)" class="disFlag link" id="sendAlipayPhoneCodeBtn">短信验证</a>
                            </span>
                                </div>
                                <div class="tijiao_box">
                                    <input class="btn" type="button" value="确认提交" id="J_sub_bindCard"
                                           onclick="return checkbandingform();"/>
                                </div>
                            </form>
                            <div class="prompt-info p_280">
                                <h3 class="tit c-huangse">温馨提示:</h3>
                                <p>1.绑定银行卡/折号，可以免去您重复输入卡/折号的繁琐步骤 </p>
                                <p class="c-red">2.只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。银行卡号绑定位数15-20位。</p>
                            </div>
                        </div>
                    </div>

                    <div id="tab-sms" class="user-tab-box tab-panel">
                        <h1 class="tab-tit">会员信息 ><span class="cee6"> 短信通知</span></h1>

                        <%
                            Users customer = (Users) session
                                    .getAttribute(Constants.SESSION_CUSTOMERID);
                            String service = customer.getAddress();
                            if (service == null) {
                                service = "";
                            }
                        %>
                        <form class="ui-form" method="post">
                            <div class="ui-form-item">
                                <label class="ui-label">修改密码通知：</label> 短信 <input type="checkbox" name="service"
                                                                                  value="3" <%=service.indexOf("3") ==
                                    -1 ? "" : "checked"%> />
                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label">提款申请通知：</label> 短信 <input type="checkbox" name="service"
                                                                                  value="5" <%=service.indexOf("5") ==
                                    -1 ? "" : "checked"%> />
                            </div>

                            <div class="ui-form-item">
                                <label class="ui-label">存款执行通知：</label> 短信 <input type="checkbox" name="service"
                                                                                  value="9" <%=service.indexOf("9") ==
                                    -1 ? "" : "checked"%> />
                            </div>
                            <div class="ui-form-item">
                                <label class="ui-label">自助晋级礼金通知：</label> 短信 <input type="checkbox" name="service"
                                                                                    value="2" <%=service.indexOf("2") ==
                                    -1 ? "" : "checked"%> />
                            </div>
                            <%--
                            <div class="ui-form-item">
                                <label class="ui-label rq-value">系统反水通知：</label>
                                短信 <input type="checkbox" name="service"
                                          value="7" <%=service.indexOf("7") == -1 ? "" : "checked"%> />
                            </div>
                            --%>

                            <div class="ui-form-item">
                                <input type="button" class="btn btn-pay" value="提交" onclick="return smsServie();"/>
                            </div>

                        </form>
                        <div class="prompt-info">
                            <h3 class="tit">温馨提示:</h3>
                            <p>1.请根据实际情况选择相应的服务，以免造成不必要的打扰。 </p>
                            <p class="c-red">2.“提款申请通知”和“存款执行通知”均为100元以上才能生效。</p>
                        </div>
                    </div>
                    <!--<div id="tab-centre" class="tab-panel tab_box active" style="position:relative;">
                        <div class="deposite-type">
                            <div class="deposite-item deposite-bank">
                                <div class="deposite-title">存款红包</div>
                                <div class="deposite-main">
                                    存款满200即可领取每日红包
                                    <a class="alcito_show"
                                       data-toggle="tab" href="#tab-meir" aria-expanded="true"
                                       onclick='setTimeout($("[href=#tab-money]").click())'>
                                        立即领取
                                    </a>
                                </div>
                            </div>
                            <div class="deposite-item  flash">
                                <div class="deposite-title">秒存红包</div>
                                <div class="deposite-main">
                                    秒存存款满300即可领取红包
                                    <a target="_blank"
                                       href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19">立即领取</a>
                                </div>
                            </div>
                        </div>
                        <div id="j-account-information" style="width: 884px; height: 400px; margin: 0 auto; display: none;">

                        </div>
                        <span class="float-text" style="color: red; display: none;">每日</span>
                    </div>-->
                </div>
                <!--站内信-->
                <div id="tab-massages" class="user-tab-box tab-panel">
                    <div class="ul_sidebar">
                        <ul>
                            <li class="active">
                                <a href="#tab-one" data-toggle="tab" aria-expanded="true">收件箱</a>
                            </li>
                            <li>
                                <a href="#tab-two" data-toggle="tab" aria-expanded="true">发件箱</a>
                            </li>
                        </ul>
                    </div>
                    <div id="tab-one" class="tab-panel active letter-b" style="margin-top: 0px;">
                        <ul class="post-list" id="j-letterList"></ul>
                    </div>
                    <div id="tab-two" class="tab-panel letter-b">
                        <div class="user-tab-box ui-form" style="margin:0">
                            <div class="ui-form-item">
                                <label class="ui-label">标题：</label>
                                <input type="text" name="guestbook.title" id="letter-title" autocomplete="off"
                                       class="ui-ipt" style="width: 400px;">
                            </div>
                            <div class="ui-form-item">
                                <label class="ui-label">内容：</label>
                                <textarea name="guestbook.content" id="letter-content" class="ui-ipt"
                                          style="width: 400px;height: 200px;"></textarea>
                            </div>
                            <div class="massage_tijiao">
                                <input type="button" class="btn" id="btn-submit" onclick="saveLetter();" value="确认发送">
                            </div>
                        </div>
                    </div>
                </div>
                <div id="tab-suggestion" class="user-tab-box tab-panel">
                    <ul class="user-nav tab-nav">
                        <li class="active" style="margin-left: 36px;">
                            <a href="javascript:;" data-toggle="tab"><i class="iconfont icon-email"></i>问题返馈</a>
                        </li>
                        <li style="float: right;margin-right: 30px;"><a href="javascript:;" data-toggle="tab"
                                                                        onclick="slideSuggestionForm()"><i
                                class="iconfont icon-write"></i>提问题</a></li>
                    </ul>
                    <div class="user-main tab-bd">
                        <div class="tab-panel active">
                            <ul class="post-list" id="j-suggestionList">
                                <!--                     <li class="sugges-item list-head">
                                                        <span class="list-item">类型</span>
                                                        <span class="list-item">提交时间</span>
                                                    </li>
                                                    <li class="sugges-item">
                                                        <div class="clearfix">
                                                            <span class="list-item c-red">10咨询网站登录异常</span>
                                                            <span class="list-item">2018-04-17 09:27:10</span>
                                                            <a href="" class="list-item c-yellow text-r">查看</a>
                                                        </div>
                                                    </li> -->
                                <!--                <div class="detail-wp" style="height: 100px;">
                                                         <h3>尊敬的天威会员</h3>
                                                         <p>
                                                             您好！这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。这边提醒你。
                                                         </p>
                                                         <p>天威客服部</p>
                                                     </div> -->
                                <!--                     <li class="sugges-item">
                                                        <span class="list-item c-red">10咨询网站登录异常</span>
                                                        <span class="list-item">2018-04-17 09:27:10</span>
                                                        <a href="" class="list-item c-yellow text-r">查看</a>
                                                    </li>
                                                    <li class="sugges-item">
                                                        <span class="list-item c-red">10咨询网站登录异常</span>
                                                        <span class="list-item">2018-04-17 09:27:10</span>
                                                        <a href="" class="list-item c-yellow text-r">查看</a>
                                                    </li> -->
                            </ul>
                        </div>
                        <div id="tab-gongdan">
                            <div class="user-tab-box ui-form">
                                <h1 class="suggestion-title">提交问题<span class="suggestionclose-btn"
                                                                       onclick="colseSuggestionForm()">&times;</span>
                                </h1>
                                <div class="ui-form-item">
                                    <label class="ui-label">您的问题：</label>
                                    <select id="suggestion-type" name="suggestion-type" title="问题" class="ui-ipt">
                                        <option selected="selected" value="">请选择</option>
                                        <option value="咨询存款和提款">咨询存款和提款</option>
                                        <option value="咨询账户问题">咨询账户问题</option>
                                        <option value="咨询红利及优惠">咨询红利及优惠</option>
                                        <option value="咨询网站登录异常">咨询网站登录异常</option>
                                        <option value="咨询代理业务">咨询代理业务</option>
                                        <option value="咨询开户">咨询开户</option>
                                        <option value="投诉和建议">投诉和建议</option>
                                    </select>
                                </div>
                                <div class="ui-form-item">
                                    <label class="ui-label">问题具体描述：</label>
                                    <textarea id="suggestion-content" name="suggestion-content" class="ui-ipt" rich="0"
                                              style="width: 400px; height: 200px" title="描述"></textarea>

                                </div>
                                <div class="ui-form-item">
                                    <button class="btn btn-danger" id="btn-submit" onclick="saveSuggestion();">创建
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div id="suggestion-mark"></div>
                    </div>
                </div>


                <!--专属客服-->
                <div id="tab-service" class="user-tab-box tab-panel">
                    <div class="ul_sidebar">
                        <ul>
                            <li class="active">
                                <a href="#tab-service-column" data-toggle="tab" aria-expanded="true">专属客服</a>
                            </li>
                        </ul>
                    </div>
                    <div id="tab-service-column" class="tab-panel active">
                        <img id="ewm-pic" src="" alt="">
                        <p>专属客服提供您宾至如归服务,如情人般的呵护</p>
                    </div>
                </div>
                <style>
                    #tab-service-column {
                        text-align: center;
                        padding: 100px 0;
                    }

                    #tab-service-column p {
                        margin-top: 15px;
                        font-size: 16px
                    }

                    #ewm-pic {
                        width: 210px;
                        height: 210px;
                    }
                </style>
                <script>
                    $.get('/asp/queryQRcode.aspx', function (data) {
                        if ((data.length && data[0].address)) {
                            $('#ewm-pic').attr('src', data[0].address)
                        }
                    }, 'json')
                </script>

            </div>
        </div>

    </div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

<%--
<div id="user-gift" style="display: none;">
    &lt;%&ndash;<button class="close">×</button>&ndash;%&gt;
    <a href="javascript:void(0)" class="gift-cnt" data-toggle="modal" data-target="#j-modal-gift">
        <button class="button-apply">点击领取 <span>►</span></button>
    </a>
</div>
--%>
<!--领取奖品弹窗{-->
<%--
<div class="modal fade" id="j-modal-gift" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">领取奖品</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-bd">
                <iframe data-src="${ctx}/applyGift.jsp" src="" width="558" height="360" scrolling="no"
                        frameborder="0"></iframe>
            </div>
        </div>
    </div>
</div>
--%>
<!--}领取奖品弹窗-->

<!--支付宝弹窗{-->
<div class="modal fade" id="j-modal-zfb" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">绑定支付宝</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-bd">
                <%--<h2 class="j-zfb-msg c-red" data-ac></h2>--%>
                <div class="ui-form-item">
                    <label class="ui-label rq-value" for="bankno">支付宝帐号：</label>
                    <input id="j-zfb-account" type="text" class="ui-ipt" maxlength="30" autocomplete="off">
                </div>
                <div class="ui-form-item">
                    <label class="ui-label rq-value" for="bankno">游戏密码：</label>
                    <input type="password" style="display: none;">
                    <input id="j-zfb-password" type="password" class="ui-ipt" maxlength="30" autocomplete="off">
                </div>
                <div class="ui-form-item">
                    <input class="btn btn-danger" type="button" value="提交" onclick="saveAlipayAccount()">
                </div>
                <div class="prompt-info">
                    <h3 class="tit">温馨提示:</h3>
                    <p>1.支付宝“二维码”扫描存款，必须用您绑定的支付宝账号进行存款，否则无法实时到账。</p>
                    <p>2.每位会员只能绑定一个支付宝帐号。</p>
                    <p>3.请再次确认您要绑定的支付宝帐号是否正确。</p>
                    <p>4.如有疑问请联系QQ客服：8000112061。</p>
                </div>
            </div>
        </div>
    </div>
</div>
<!--}支付宝弹窗-->

<!--救援金弹窗-->
<div class="modal fade" id="j-modal-rescue" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">选择老虎机平台</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-bd">
                <input type="hidden" class="j-hd-id"/><input type="hidden" class="j-hd-url"/>
                <div class="ui-form">
                    <div class="ui-form-item" style="padding-left:50px;">
                        <label for="" class="ui-label" style="width:100%;text-align:center;margin-left: 0;">请选择您喜欢的老虎机平台,
                            确定后我们不接受任何重新转至其他老虎机平台的申请</label>
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="pttiger"
                                                 checked="checked">PT老虎机
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="ttg">TTG老虎机
                    </div>
                    <div class="ui-form-item">
                        <input type="button" class="btn btn-danger j-btn-apply" value="确定">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--绑定密保{-->
<div class="modal fade in" id="modal-bind-answer" tabindex="-1" role="dialog" data-modal-load
     aria-labelledby="myModalLabel">
    <div class="modal-dialog lg" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">绑定密保</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
            </div>
            <div class="modal-bd">
                <form method="post" class="ui-form" style="padding:0">
                    <div class="ui-form-item">
                        <label class="ui-label">天威账户：</label>
                        <span class="ipt-value">${session.customer.loginname}</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">账户结余：</label><span
                            class="ipt-value c-red">${session.customer.credit}</span>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label rq-value" for="mar_questionid">密保问题：</label>
                        <s:select id="mar_questionid_mb2" name="questionid" list="%{#application.QuestionEnum}"
                                  cssClass="ui-ipt" listKey="code" listValue="text"></s:select>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label rq-value">您的回答：</label><input class="ui-ipt" style="margin-left: 4px;"
                                                                             type="text" id="mar_answer2" name="answer"
                                                                             required>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label rq-value">登录密码：</label><input class="ui-ipt" style="margin-left: 4px;"
                                                                             type="password" id="mar_pwd" name="answer"
                                                                             required>
                    </div>
                    <div class="ui-form-item">
                        <input class="btn btn-danger" type="button" value="提交" onclick="commitBindingQuestion()">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!--}绑定密保-->

<!--收件箱详情{-->
<div class="modal fade" id="modal-letter" role="dialog" data-backdrop="static" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">收件箱详情</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
            </div>
            <div class="modal-bd letter-cnt">
                <h1 class="j-tit tit"></h1>
                <div class="j-time time"></div>
                <div class="j-content article">加载中...</div>
            </div>
        </div>

    </div>
</div>

<div class="mull">
    <div class="mas_show">
        <div class="title_mag">
            请先完善个人信息
        </div>
        <div class="msg_btn">
            <ul>
                <!--<li class="user-vip" style="width: 100%;"><a class="alcito_show" data-toggle="tab" href="#user-vip"><i
   class="user_icon z_index6"></i><span>确认</span></a></li>-->
                <li id="sdasd" style="width: 100%;">
                    确认
                </li>
            </ul>
        </div>
    </div>
</div>

<!--}收件箱详情-->
<script src="${ctx}/js/lib/jquery.qrcode.min.js"></script>
<script src="${ctx}/js/lib/ZeroClipboard.min.js"></script>
<script>
    $(document).ready(function () {
        $(".qiandao_class a,.cunkuang a,.zhuanzhangle a,.tikuang a,#zzyouhui").click(function(){
              if ($("#xingming").val().trim() == '' || $("#youxiang").val().trim() == '') {
                $(".mull").show();
                return false
            }
            return true;
        })

              
        $("#sdasd").click(function () {
            $(".mull").hide();
            $('.user-vip a').click();
        })

        $("#zzyouhui").find("a").click(function () {
            if ($("#xingming").val() == '' || $("#youxiang").val() == '') {
                $(".mull").show();
                return false;
            }
            isBlindingCard();
            return true;
        })

        //是否存在礼品
        // isExistGift();

        //判断支付宝是否已经绑定
        getAlipayAccount();
        $('#j-modal-gift').on('show.bs.modal', function () {
            var $iframe = $(this).find('iframe');
            $iframe.attr('src', $iframe.attr('data-src'));

        });

        var clip = new ZeroClipboard($('.btn-copy'));
        clip.on('aftercopy', function (e) {
            var target = $(e.target);
            var val = $.trim(e.data['text/plain']);
            if (!val) {
                if (target.siblings('.remark-value').length) {
                    alert('请先获取附言');
                }
                return;
            }

            target.html('复制成功');
            setTimeout(function () {
                target.html('复制')
            }, 1000);
        });

        //将所有银行存款验证码一次性赋值
        $('.deposit-code').attr('src', '/asp/depositValidateCode.aspx?r=' + Math.random());
        /* 获取银行卡号和开户银行
                 * =====*/
        getWithDrawBankNo($("#bank").val());
        //初始化的时候 来源账户和目标账户的金额
        transferMoneryOut($("#transferGameOut").val());
        transferMoneryIn($("#transferGameIn").val());

        var bankList = $('#j-bank-list');
        //选择银行事件
        bankList.find('a').on('click', function () {
            var target = $(this).attr('href');
            // 刷新验证码
            $(target).find('.deposit-code').attr('src', '${ctx}/asp/depositValidateCode.aspx?r=' + Math.random());
            if ($(target).length == 0) {
                // 隐藏不存在的target
                $('#j-bank-list').siblings('.tab-bd').find('.tab-panel.active').removeClass('active')
            }
        });

        //支付宝绑定手机号验证
        $("#sendAlipayPhoneVoiceCodeBtn").on("click", function () {
            /*********触点**********/
            var is_checked = false;
            window.TouClick.Start({
                website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
                position_code: 0,
                args: {'this_form': $("#blindCardForm")[0]}, 
                captcha_style: {'left': '50%', 'top': '60%'},
                onSuccess: function (args, check_obj) {
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

                    $.post("${ctx}/asp/sendAlipayPhoneVoiceCode.aspx", {
                        "check_address": check_address,
                        "check_key": check_key
                    }, function (data) {
                        alert(data);
                        closeProgressBar();
                    });
                },
                onError: function (args) {
                    //启用备用方案
                }
            });
            /*********触点**********/
        });

        $("#sendAlipayPhoneCodeBtn").on("click", function () {
            /*********触点**********/
            var is_checked = false;
            window.TouClick.Start({
                website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
                position_code: 0,
                args: {'this_form': $("#blindCardForm")[0]},
                captcha_style: {'left': '50%', 'top': '60%'},
                onSuccess: function (args, check_obj) {
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
                    $.post("${ctx}/asp/sendAlipayPhoneSmsCode.aspx", {
                        "check_address": check_address,
                        "check_key": check_key
                    }, function (data) {
                        alert(data);
                        closeProgressBar();
                    });
                },
                onError: function (args) {
                    //启用备用方案
                }
            });
            /*********触点**********/
        });

        clearBandingform();

        //是否绑定密保问题
        isBlindingQuestion();

    });

    function isExistGift() {
        $.ajax({
            url: "${ctx}/asp/isExistGift.aspx",
            type: "post",
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data) {
                    $('#user-gift').show();
                }
            }
        });
    }

    //保存支付宝绑定帐号
    function saveAlipayAccount() {
        var ac = $(".j-zfb-msg").data("ac");
        var str = "",
            $account = $("#j-zfb-account"),
            $password = $("#j-zfb-password");
        if (!$account.val()) {
            alert('支付宝帐号不能为空,请重新输入！');
            return false;
        }

        if (!$password.val()) {
            alert('密码不能为空,请重新输入！');
            return false;
        }
        if (/.*[\u4e00-\u9fa5]+.*$/.test($account.val())) {
            alert('支付宝帐号不能有中文,请重新输入！');
            return false;
        }

        if (ac == "1") {
            str = "请您确认是否更换支付宝帐号";
        } else {
            str = "请您再次确认支付宝帐号是否正确";
        }

        if (confirm(str)) {
            openProgressBar();
            $.post("/asp/saveAlipayAccount.aspx", {
                "account": $account.val(),
                "password": $password.val()
            }, function (data) {
                getAlipayAccount();
                alert(data);
                closeProgressBar();
                $account.val('');
                $password.val('');
                //$('#j-modal-zfb').modal('hide');
            });
        }
    }

    //获取支付宝绑定的帐号
    function getAlipayAccount() {
        var zfbMsg = $('.j-zfb-msg'),
            account = zfbMsg.find('.j-account-ret'),
            zfbAction = $('#j-zfb-action');
        $.post("/asp/getAlipayAccount.aspx", function (data) {
            if (data === 'empty') {
                zfbMsg.data("ac", "0");
                zfbMsg.html("请先绑定您的支付宝存款账号：  " + '<a href="javascript:;" class="btn btn-pay" data-toggle="modal" data-target="#j-modal-zfb">绑定</a>');
                zfbAction.on('click', modalZfb);

            } else if (data && data !== 'empty') {
                var alipayAccount = data.alipayAccount;
                var length = alipayAccount.length;
                var str = "";
                for (var i = 0; i < length; i++) {
                    if (i < (length / 2)) {
                        str += alipayAccount[i];
                    } else {
                        str += "*";
                    }
                }
                zfbMsg.data("ac", "1");
                zfbMsg.html('您目前绑定的存款账号是：' + str + '&nbsp;&nbsp;<a href="javascript:;" class="btn btn-pay" data-toggle="modal" data-target="#j-modal-zfb">修改绑定</a>');
                zfbAction.off('click');
            }
        });
    }

    function modalZfb() {
        alert('请先绑定支付宝帐号');
        $('#j-modal-zfb').modal('show');
        return false;
    }

    function isBlindingQuestion() {
        $.ajax({
            url: "${ctx}/asp/isBildingQuestion.aspx",
            type: "post",
            dataType: "text",
            cache: false,
            success: function (data) {
                if (data == "1") {
                    $("#a_blindingQuestion").on('click', function () {
                        alert('您已经绑定过密保问题');
                        return false;
                    });
                }
            }
        });
    }

    /* 获取备注
             * =======*/
    function getRemarkAgain(obj, bankname) {
        var bank = "";
        if (bankname == "icbc") {
            bank = "工商银行";
        } else if (bankname == "cmb") {
            bank = "招商银行";
        } else if (bankname == "boc") {
            bank = "中国银行";
        } else if (bankname == "cgbchina") {
            bank = "广发银行";
        } else if (bankname == "bankcomm") {
            bank = "交通银行";
        } else if (bankname == "ccb") {
            bank = "建设银行";
        } else if (bankname == "zfb") {
            bank = "支付宝";
        } else if (bankname == "abc") {
            bank = "农业银行";
        }
        //var code = $("input[name='" + bankname + "InputText']").eq(0).val();
        var code = $(obj).siblings('.ipt-checkcode').val();
        var username = $(obj).siblings('.hd_username').val();
        if (confirm("确定重新生成附言？")) {
            $.post("${ctx}/asp/getRemarkAgain.aspx", {
                "bankname": bank,
                "depositCode": code,
                "username": username
            }, function (respData) {
                if (respData.length == 5 || respData.length == 4) {
                    alert("存款订单已生成，请一定使用新的附言进行付款");
                    /* $(".leaveMsg" + bankname).eq(0).html(respData);*/
                    $(obj).siblings('.remark-value').html(respData).siblings('.btn-copy').attr('data-clipboard-text', respData);
                } else {
                    alert(respData);
                }
                $('.deposit-code').attr('src', '/asp/depositValidateCode.aspx?r=' + Math.random());
            });

        }
    }

    function clearNoNum(obj) {
        //只能输入大于0的整数
        obj.value = obj.value.replace(/\D|^0/g, "");
    }

    function createValidatedPayOrder() {
        var depositAmount = $("#depositAmountInput").val();
        if (depositAmount < 100) return;
        //openProgressBar();
        $("#validateAmountDepositBankInfo").empty();
        var b_validate = validateDepositBankCardInfo();
        if (b_validate) {
            $.post("${ctx}/asp/createValidatedPayOrder.aspx", {amount: depositAmount}, function (data) {
                if (data.code == '1') {
                    $('#validateAmountDepositResult').text('我们能够为您处理的金额为：' + data.amount + ' 元。 请确保存入该指定金额，否则会导致存款无法到帐。');
                    //validateDepositBankCardInfo();
                } else {
                    $("#validateAmountDepositBankInfo").empty();
                    $('#validateAmountDepositResult').text('');
                    alert(data.msg);
                }
            }).fail(function () {
                $("#validateAmountDepositBankInfo").empty();
                $('#validateAmountDepositResult').text('');
                alert("生成订单失败");
            }).always(function () {
                closeProgressBar();
            });
        } else {
            $("#validateAmountDepositBankInfo").empty();
            $('#validateAmountDepositResult').text('');
            alert("当前系统无法处理额度验证存款，对此给您带来的不便我们深表歉意。");
            closeProgressBar();
        }
    }

    /* 额度存款验证
             * ===========*/
    function validateDepositBankCardInfo() {
        var ret = false;
        $.ajax({
            type: "post",
            url: "${ctx}/asp/getValidateDepositBankInfo.aspx",
            cache: false,
            async: false,
            success: function (data) {
                if (data != null) {
                    $("#validateAmountDepositBankInfo").append("<tr><th>账户名：</th><td><span style='color:red;'>" + data.username + "</td><th>开户行：</th><td><span style='color:red;'>" + data.bankname + "</span></td></tr>");
                    $("#validateAmountDepositBankInfo").append("<tr><th>帐号：</th><td><span style='color:red;'>" + data.accountno + "</td><th>存款验证QQ：</th><td><span style='color:red;'>800134430</span></td></tr>");
                    ret = true;
                } else {
                    ret = false;
                }
            },
            error: function () {
                ret = false;
            }
        });
        return ret;
    }

    function getbankno(_bankname) {

        if (_bankname == '') {
            alert("银行名称不能为空");
            return;
        }

        $.post('/asp/searchBankno.aspx', {'bankname': _bankname, 'r': Math.random()}, function (data) {
            if ($.trim(data) === '1') {
                document.checkform.accountNo.value = "";
                document.checkform.bankAddress.value = "";
            } else {
                var recvData = $.trim(data).split("|||");
                document.checkform.accountNo.value = recvData[0];
                document.checkform.bankAddress.value = recvData[1];
            }
        });
        /* if(_bankname == "支付宝"){
                 $(".j_questionflag").css("display","");
                 }else{
                 $(".j_questionflag").css("display","none");
                 } */
    }

    //获取提款银行的状态
    function getWithDrawBankStatus(bankname) {
        if (bankname == '') {
            return;
        }
        openProgressBar();

        var status = "ERROR";
        $.ajax({
            type: "POST",
            async: false,
            url: "/asp/getWithDrawBankStatus.aspx",
            data: {"bankname": bankname},
            error: function (response) {
                closeProgressBar();
                alert(response);
            },
            success: function (response) {
                closeProgressBar();
                status = response;
            }
        });
        return status;
    }

    //获取提款银行的账号信息
    function getWithDrawBankNo(bankname) {
        if (bankname == '') {
            return;
        }
        var status = getWithDrawBankStatus(bankname);
        if (status == "MAINTENANCE") {
            $("#tkAccountNo").val("");
            $("#tkBankAddress").val("");
            alert("银行系统维护中,请选择其他银行或稍后再试");
            return;
        }
        getbankno(bankname);
    }

    //提交提款确认
    function tkConfirm() {
        var tkamount = $("#mar_amount").val();
        if (document.checkform.password.value == '') {
            alert("[提示]密码不可为空！");
            document.checkform.password.focus();
            return false;
        }

        if (document.checkform.bank.options[document.checkform.bank.selectedIndex].value == "") {
            alert("[提示]请选择卡折种类！");
            document.checkform.bank.focus();
            return false;
        }
        if (document.checkform.accountNo.value == '') {
            alert("[提示]卡折号不可为空！");
            document.checkform.accountNo.focus();
            return false;
        }
        var spassword = document.checkform.accountNo;
        if (spassword.value != "" && (spassword.value.length > 100)) {
            alert("[提示]卡折号长度不能大于100！");
            document.checkform.accountNo.focus();
            return;
        }

//      if (document.checkform.bankAddress.value == '') {
//          alert("[提示]开户网点不可为空！");
//          document.checkform.bankAddress.focus();
//          return false;
//      }
        if (document.checkform.amount.value == '') {
            alert("[提示]提款金额不可为空！");
            document.checkform.amount.focus();
            return false;
        }
        /* if (document.checkform.amount.value < 1) {
                 alert("[提示]单次提款金额最低1元");
                 document.checkform.amount.focus();
                 return false;
                 } */
        if (document.checkform.amount.value < 100) {
            alert("[提示]单次提款金额最低100元");
            document.checkform.amount.focus();
            return false;
        }
        var rex = /^[1-9][0-9]*$/;
        if (!rex.test(tkamount)) {
            alert("抱歉，提款金额只能是整数哦。");
            return false;
        }
        var password = $("#mar_password").val();
        var bank = $("#bank").val();
        var accountNo = $("#accountNo").val();
        var bankAddress = $("#bankAddress").val();
        var amount = $("#mar_amount").val();
        var questionid = $("#mar_questionid").val();
        var answer = $("#mar_answer").val();
        if (answer == '') {
            alert("提款密码不能为空");
            return;
        }

        var html = "";
        html += '<table border="1" style="line-height: 30px;font-size:14px; color:#000000;width: 95%;margin: 10px auto;">';
        html += '<tr>';
        html += '<td>账户姓名:' + $("#accountName").val() + '</td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td>银行名称:' + bank + '</td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td>银行账号:' + accountNo + '</td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td>金额:' + amount + '</td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td style="text-align:center;"><input class="btn btn-danger" type="button" value="提交" onclick="return drawMoney();" /></td>';
        html += '</tr>';
        html += '<tr>';
        html += '<td><font color="red">温馨提示:如您的注册姓名与您的收款账户姓名不一致,将导致提款失败!请您联系在线客服!</font></td>';
        html += '</tr>';
        html += '</table>';

        layer.open({
            title: ['收款人资料', 'text-align:center;'],
            type: 1,
            area: ['600px', '360px'],
            shadeClose: false, // 点击遮罩关闭
            content: html
        });

    }
    function drawMoney() {
        openProgressBar();
        var password = $("#mar_password").val();
        var bank = $("#bank").val();
        var accountNo = $("#accountNo").val();
        var bankAddress = $("#bankAddress").val();
        var amount = $("#mar_amount").val();
        var questionid = $("#mar_questionid").val();
        var answer = $("#mar_answer").val();
        if (answer == '') {
            alert("提款密码不能为空!");
            return;
        }
        if (amount < 100) {
            alert("提款金额不能小于100");
            return;
        }
        console.log({
            "password": password,
            "bank": bank,
            "accountNo": accountNo,
            "bankAddress": bankAddress,
            "amount": amount
        });
        $.post("${ctx}/asp/withdraw.aspx", {
            "password": password,
            "bank": bank,
            "accountNo": accountNo,
            "bankAddress": bankAddress,
            "amount": amount,
            // "msflag"     : msflag,
            "questionid": 7,
            "answer": answer
        }, function (data) {
            layer.closeAll();
            closeProgressBar();
            if (data == "bindingPlease") {
                alert("请绑定密保问题");
                window.location.href = "${ctx}/userManage.jsp";
            }
            if (data == "主账户额度不足") {
                alert(data);
                layer.closeAll();
                closeProgressBar();
            } else {
                alert(data);
            }

        });
    }

    //获取游戏金额
    function transferMoneryIn(gameCode) {
        if (gameCode != "") {
            $("#transferMoneryInDiv").html("加载中..");
            $.post("${ctx}/asp/getGameMoney.aspx", {
                "gameCode": gameCode
            }, function (returnedData, status) {
                if ("success" == status) {
                    $("#transferMoneryInDiv").html(returnedData);
                    //$('.j-balance').html(returnedData);
                    //updateBalance(gameCode,returnedData);
                }
            });
        }
    }

    //获取游戏金额
    var notNeedShowList = $('#transferGameIn option:not([value="newpt"],[value="ttg"],[value="slot"])')

    function transferMoneryOut(gameCode) {
        if (gameCode == 'qd') {
            notNeedShowList.hide()
        } else {
            notNeedShowList.show()
        }
        if (gameCode != "") {
            $("#transferMoneryOutDiv").html("加载中..");
            $.post("${ctx}/asp/getGameMoney.aspx", {
                "gameCode": gameCode
            }, function (returnedData, status) {
                if ("success" == status) {
                    $("#transferMoneryOutDiv").html(returnedData);
                    //updateBalance(gameCode,returnedData);
                }
            });
        }
    }

    //更新余额
    function updateBalance(code, value) {
        if (code === 'e68') {
            $('.j-balance-total').html(value);
        } else if (code === $('#j-select-balance').val()) {
            $('#j-topBalance').html(value);
        }
    }

    //游戏转账
    function transferMonery() {
        var transferGameOut = $("#transferGameOut").val();
        /* if (transferGameOut == "") {
                    alert("来源账号不能为空！");
                    return false;
                } */
        var transferGameIn = $("#transferGameIn").val();
        /* if (transferGameIn == "") {
                    alert("目标账号不能为空！");
                    return false;
                } */
        var transferGameMoney = $("#transferGameMoney").val();
        /* if (transferGameMoney == "") {
                    alert("转账金额不能为空！");
                    return false;
                }
                if (isNaN(transferGameMoney)) {
                    alert("转账金额只能是数字!");
                    return false;
                }
                if (transferGameOut == "ld" && transferGameIn == "ld") {
                    alert("天威账户不能转账到天威账户！");
                    return false;
                }

                if (transferGameOut == "qd" && transferGameIn == "qd") {
                    alert("签到账户不能转账到签到账户");
                    return false;
                }
                if (transferGameOut == "qd" && transferGameIn == "ld") {
                    alert("签到账户不能转账到主账户");
                    return false;
                } */
        /* if(transferGameIn=='newpt' && parseInt(transferGameMoney)<20){
                 alert('PT转入金额不能少于20元');
                 return false;
                 }*/
        //if (transferGameOut == "ld" || transferGameIn == "ld" || transferGameOut == "qd" || transferGameIn == "qd") {
        openProgressBar();
        $.post("${ctx}/asp/updateGameMoney.aspx", {
            "transferGameOut": transferGameOut,
            "transferGameIn": transferGameIn,
            "transferGameMoney": transferGameMoney
        }, function (returnedData, status) {
            if ("success" == status) {
                transferMoneryOut(transferGameOut);
                transferMoneryIn(transferGameIn);
                closeProgressBar();
                alert(returnedData);
            }
        });
        /* } else {
                    alert("游戏之间不能对转！");
                    return false;
                } */
    }

    function chageNav2(target) {
        var $nav = $('#j-userNav');
        if (target) {
            $nav.find('a[href="#' + target + '"]').trigger('click');
        }
    }

    function onekeyMonery() {
        var jsonData = ajaxPost("/asp/oneKeyGameMoney.aspx");
        if (jsonData == null || jsonData == "" || typeof jsonData == "undefined") {

            alert("一键回归成功!");
            //window.location.reload();
            window.location.href = "/asp/payPage.aspx?showid=tab_transfer";
        } else {
            alert(jsonData);
        }
    }

    function ajaxPost(url, parm) {
        openProgressBar();
        var RESULT;
        $.ajax({
            url: url,
            type: "post",
            data: parm,
            cache: false,
            async: false,
            timeout: 3000,
            success: function (jsonData) {
                RESULT = jsonData;
                closeProgressBar();
                return RESULT;
            }
        });

        return RESULT;
    }

    function user_bank() {
        $.get('/asp/queryBankAll.aspx', function (data) {
            if (data.length >= 3) {
                $(".add_bank").hide();
            }
            for (var i = 0; i < data.length; i++) {
                var bankno = data[i].bankno   ;
                var  nuber = bankno.substr(bankno.length-4);
                var $html = "<li><div class='user_bank_massage'><a onclick='untied(\""+nuber+"\")' class='untied'>解绑</a><span>" + data[i].bankname + "</span><span>" + data[i].bankno + "</span><span></span></div></li>";
                $(".user_bank").prepend($html)
            }
        })
    }

    function untied(data){
        var _html ='<div class="untied-alert">'
                +  '<p class="warning">您是否确认解绑此银行卡(尾号'+data+')?</p>'
                +  '<div class="tips">请输入您的卡号</div>'
                +  '<div class="car"><span>银行卡号</span><input  type="text" data="carNber"></div>'    
                +  '</div>' 
        layer.open({
            title:'解除绑定银行卡',
            area: ['400px', '225px'],
            content:_html,
            btn:['确定','取消'],
            yes:function(index, layero){
                // index, layero
                var nber = $("[data='carNber']").val()
                RemoverbackCar(nber,index)
                // 
            },
            no:function(){

            }
        })
    }


    //只能输入数字
    function onlyNum(that){
        that.value=that.value.replace(/\D/g,"");
    }
    //解除绑定银行卡
    function RemoverbackCar(bankno,index){
        if(bankno.length == ''){
            layer.msg('不能为空',{skin:'tips-alert'});
            return;
        }
        $.post("/asp/unBindBankinfo.aspx", {
            "bankno": bankno
        }, function (result) {
           layer.msg(result,{skin:'tips-alert'});
           layer.close(index);
        });
    }

    //重置绑定
    function clearBandingform() {
        $("#bdbankno").val("");
        $("#bdpassword").val("");
    }

    function showyzmDiv(bank) {
        if (bank == "支付宝") {
            $("#zfbyzmDiv").attr("style", "display:block;");
        } else {
            $("#zfbyzmDiv").attr("style", "display:none;");
        }
    }

    function commitBindingQuestion() {
        var questionid = $("#mar_questionid_mb").val();
        /*alert('2:' + questionid);*/
        var answer = $("#mar_answer3").val();
        var password = $("#mar_pwd2").val();
        if (!questionid || !answer || !password) {
            alert('请填写完整的密保信息!');
            return;
        }
        openProgressBar();

        $.post("${ctx}/asp/saveQuestion.aspx", {
            "questionid": questionid,
            "answer": answer,
            "password": password
        }, function (data) {
            closeProgressBar();
            alert(data);
            if (data.indexOf('成功') != -1) {
                window.location.href = '${ctx}/asp/payPage.aspx?showid=tab_withdraw';
            }
        });

    }

    //催账记录
    function urgeOrderRecord(pageIndex) {
        if (pageIndex <= 1) {
            pageIndex = 1;
        }
        openProgressBar();
        $.post("${ctx}/asp/queryUrgeOrderPage.aspx", {
            "pageIndex": pageIndex,
            "size": 5
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#czDiv").html("");
                $("#czDiv").html(returnedData);
            }
        });
        return false;
    }

    //好友推荐奖金转账
    function submitFriendRemit() {
        var signType = $("#friendType").val();
        var signRemit = $("#friendRemit").val();
        if (signType == "") {
            alert("请选择平台！");
            return false;
        }
        if (signRemit != "") {
            if (isNaN(signRemit)) {
                alert("转账金额非有效数字！");
                return false;
            }
            if (signRemit < 5) {
                alert("转账金额不能小于5元！");
                return false;
            }
            var rex = /^[1-9][0-9]*$/;
            if (!rex.test(signRemit)) {
                alert("抱歉，好友推荐金额只能是整数哦。");
                return false;
            }
        } else {
            alert("请输入金额！");
            return false;
        }
        openProgressBar();
        $.post("/asp/transferInforFriend.aspx", {
            "signType": signType,
            "signRemit": signRemit,
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                alert(returnedData);
            }
        });
        return false;
    }

    //好友推荐奖金余额
    function queryfriendMoney() {
        openProgressBar();
        $.post("/asp/queryfriendBonue.aspx",
            function (returnedData, status) {
                if ("success" == status) {
                    closeProgressBar();
                    var strs = returnedData.split('#');
                    $("#friendmoney").html(strs[0] + "元");
                    var friendurl = document.domain + "?friendcode=" + strs[1];
                    $("#friendurl").html(friendurl).attr('href', friendurl);
                    $("#fCode").qrcode({width: 200, height: 200, text: friendurl});
                    $(".j-copy").attr("data-clipboard-text", document.domain + "?friendcode=" + strs[1]);

                    var clip1 = new ZeroClipboard($('.j-copy'));
                    clip1.on('aftercopy', function (e) {
                        var val = $.trim(e.data['text/plain']);
                        if (val === '' || val === undefined) return;
                        var target = $(e.target);
                        target.html('复制成功')
                        setTimeout(function () {
                            target.html('复制')
                        }, 1000);
                    });
                }
            });
        return false;
    }

    queryfriendMoney();

    //查询玩家红包余额
    function getHBMoney(type) {
        $("#hbMoney").html("<img src='/images/waiting.gif'>");
        $.post("/asp/getHBMoney.aspx", {"type": 0}, function (returnedData, status) {
            if ("success" == status && returnedData != '') {
                $("#hbMoney").html("" + returnedData + "元");
            }
        });
        queryHBSelect();
    }

    // 获取玩家可领红包

    function queryHBSelect() {
        $.post("/asp/queryHBSelect.aspx", function (returnedData, status) {
            if ("success" == status) {
                $("#hbSelect").html("");
                $("#hbSelect").html(returnedData);
            }
        });
    }

    function doHB() {
        var hbSelect = $("#hbSelect").val();
        if (hbSelect == null || hbSelect == '') {
            alert("请选择红包类型！");
            return;
        }
        openProgressBar();
        $.post("/asp/doHB.aspx", {
            "sid": hbSelect
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                getHBMoney();
                alert(returnedData);
            }
        });
        return false;

    }

    function submitHBRemit() {
        var hbType = $("#hbType").val();
        var hbRemit = $("#hbRemit").val();
        if (hbType == "") {
            alert("请选择平台！");
            return false;
        }
        if (hbMoney != "") {
            if (isNaN(hbRemit)) {
                alert("转账金额非有效数字！");
                return false;
            }
            if (hbRemit < 10) {
                alert("转账金额必须大于10！");
                return false;
            }
        }
        openProgressBar();
        $.post("/asp/submitHBRemit.aspx", {
            "type": hbType,
            "transferGameIn": hbRemit
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                getHBMoney();
                alert(returnedData);
            }
        });
        return false;
    }

    getHBMoney();

    // ---存提款红包结束

    function getbank_money() {
        $.get('/asp/queryBankAll.aspx', function (data) {
            for (var i = 0; i < data.length; i++) {
//              console.log(data[i].bankname)

                var $html = $("<option value='" + data[i].bankname + "'>" + data[i].bankname + "</option>")

                $("#bank").append($html)
            }
        })
    }

    getbank_money()

    var clipboard = new Clipboard('.url-btn');

    clipboard.on('success', function (e) {
        console.log(e);
    });

    clipboard.on('error', function (e) {
        console.log(e);
    });

    $(function () {
        user_bank();
        $(".select_money ul li").click(function () {
            $(".select_money ul li").removeClass("action");
            $(this).addClass("action");
            var that = parseInt($(this).text());
            if ($(this).text() != "其他金额") {
                $("#transferGameMoney").val(that);
            } else {
                $("#transferGameMoney").val("")
                $("#transferGameMoney").focus();
            }
        })

        $("#zhifubaoq").click(function () {
            $(".zhifubao_type li").last().addClass("action").siblings().removeClass("action");
        });

        $(".zhifubao_type ul li").click(function () {
            $(".zhifubao_type ul li").removeClass("action");
            $(this).addClass("action");
            var that = parseInt($(this).text());
            if ($(this).text() != "其他金额") {
                $("#zhifubaoq").val(that);
            } else {
                $("#zhifubaoq").val("")
                $("#zhifubaoq").focus();
            }
        })

        $(".add_bank").click(function () {
            $("#tab_card_binding").show();
            $(".user_bank").hide();

        })

        $(".qukuang_box ul li").eq(1).click(function () {
            $("#tab_card_binding").hide();
            $(".user_bank").show();
        })

        $("#tab-money").removeClass("active");

        $(".show_qiandao").click(function () {
            $("#tab-checkInCenter").hide();
            $("#tab-checkInInfo").show();
        })
        $("#user_mull li").eq(0).click(function () {
            $("#tab-checkInCenter").show();
            $("#tab-checkInInfo").hide();
        })

        $(".back_qiandao").click(function () {
            $("#tab-checkInInfo").hide();
            $("#tab-checkInCenter").show();
        })

    })

    var pageInfo = {
        'pageIndex': 1,
        'size': 10
    };
    var $recordsIframe = $('#j-record-form'),
        $recordsAction = $('#j-records-action'),
        $iframe = $recordsIframe.find('iframe'),
        $recordTitle = $recordsIframe.find('.tab-tit1');
    $(function () {
        // console.log('test');
        $recordsAction.find('a').on('click', (function () {
            var $this = $(this);
            $recordTitle.text($this.text());
            if ($this.data('val') == 'girl') {
                $iframe.hide();
                $('#j-ret').show();
//                $.post($this.attr('data-href'), pageInfo, function (data) {
//                    $('#j-ret').html(data);
//                })
            } else {
                $iframe.show();
                $('#j-ret').hide();
//                $('#j-ret').html('');
                $iframe.attr('src', '');
                $iframe.attr('src', $this.attr('data-href'));

            }

        }));

        $recordsAction.find('a').first().trigger('click');
    });

    $(function () {

        if ($("#birthday").val().trim() != '') {
            $("#birthday").attr("disabled", '').removeAttrs('onfocus');
        }
        if ($("#accountNameX").val() != '') {
            $("#accountNameX").attr("disabled", 'disabled').removeAttr('data-rule-chinese-name')
        }
        if ($("#emailx").val() != '') {
            $("#emailx").attr("disabled", 'disabled').removeAttr('data-rule-email')
        }

        //改变菜单

        $("#J_sub_info").click(function () {
            if ($("#accountNameX").val() == '') {
                alert("请填写真实姓名")
                return false;
            }

            if ($("#emailx").val() == '') {
                alert("请填写邮箱")
                return false;
            }

        })

        $('#j-modify-form').validate({
            submitHandler: function (form) {
                $.post('${ctx}/asp/change_infoAjax.aspx', $(form).serialize(), function (data) {
                    alert(data);
                    window.location.reload();
                });

            }
        });

        $('#passwordform').validate({
            submitHandler: function (form) {
                $.post('${ctx}/asp/change_pwsAjax.aspx', $(form).serialize(), function (data) {
                    alert(data);
                    window.location.reload();
                });
            }
        });

        //支付宝绑定手机号验证
        $("#sendAlipayPhoneVoiceCodeBtn").on("click", function () {
            /*********触点**********/
            var is_checked = false;
            window.TouClick.Start({
                website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
                position_code: 0,
                args: {'this_form': $("#blindCardForm")[0]},
                captcha_style: {'left': '50%', 'top': '60%'},
                onSuccess: function (args, check_obj) {
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

                    $.post("${ctx}/asp/sendAlipayPhoneVoiceCode.aspx", {
                        "check_address": check_address,
                        "check_key": check_key
                    }, function (data) {
                        alert(data);
                        closeProgressBar();
                    });
                },
                onError: function (args) {
                    //启用备用方案
                }
            });
            /*********触点**********/
        });

        $("#sendAlipayPhoneCodeBtn").on("click", function () {
            /*********触点**********/
            var is_checked = false;
            window.TouClick.Start({
                website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
                position_code: 0,
                args: {'this_form': $("#blindCardForm")[0]},
                captcha_style: {'left': '50%', 'top': '60%'},
                onSuccess: function (args, check_obj) {
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
                    $.post("${ctx}/asp/sendAlipayPhoneSmsCode.aspx", {
                        "check_address": check_address,
                        "check_key": check_key
                    }, function (data) {
                        alert(data);
                        closeProgressBar();
                    });
                },
                onError: function (args) {
                    //启用备用方案
                }
            });
            /*********触点**********/
        });

        clearBandingform();
    });
    $("#bdbankno").blur(function () {
        $.post("/asp/getBankInfo.aspx", {bankno: $(this).val()}, function (data) {
            if (data && data.issuebankname) {
                $("#checkbankname").val(data.issuebankname)
            } else {
                $("#checkbankname").val(data)
            }
        })
    })
    // 检测个人信息完情况
    $('.check_name').on('click', function () {
        if ($("#xingming").val().trim() == '' && $("#youxiang").val().trim() == '') {
            $(".mull").show();
            return false
        }
        return true;
    })

    //绑定银行卡
    function checkbandingform() {
        if (!window.confirm("确定吗？")) {
            return false;
        }
        var bdbankno = $("#bdbankno").val();
        if (bdbankno == "") {
            alert("[提示]卡/折号不可为空！");
            return false;
        }
//        if (bdbankno.length > 30 || bdbankno.length < 10) {
//            alert("[提示]卡/折号长度只能在10-30位之间");
//            return false;
//        }
        // var bdbank = $("#bdbank").val();
        // if (bdbank == "") {
        //     alert("[提示]银行不能为空！");
        //     return false;
        // }
        var bdbank = $("#checkbankname").val();
        if (bdbank == "" || bdbank.toString().indexOf("不支持") > -1) {
            alert('正在检查银行卡或不支持此银行卡');
            return false;
        }

        var bdpassword = $("#bdpassword").val();
        if (bdpassword == "") {
            alert("[提示]登录密码不可以为空");
            return false;
        }
        var bindingCode = $("#bindingCode").val();
        openProgressBar();
        $.post("${ctx}/asp/bandingBankno.aspx", {
            "password": bdpassword,
            "bankname": bdbank,
            "bankno": bdbankno,
            "bankaddress": "none",
            "bindingCode": bindingCode
        }, function (returnedData, status) {
            if ("success" == status) {
                if (returnedData == "SUCCESS") {
                    alert("绑定成功！");
                    window.location.reload();
                } else {
                    closeProgressBar();
                    alert(returnedData);
                    // window.location.reload();
                }
            }
        });
    }

    //重置绑定
    function clearBandingform() {
        $("#bdbankno").val("");
        $("#bdpassword").val("");
    }

    function showyzmDiv(bank) {
        if (bank == "支付宝") {
            $("#zfbyzmDiv").attr("style", "display:block;");
        } else {
            $("#zfbyzmDiv").attr("style", "display:none;");
        }
    }

    //短信服务
    function smsServie() {
        var str = "";
        $("input[name='service']:checkbox").each(function () {
            if ($(this).prop("checked")) {
                str += $(this).val() + ","
            }
        })
        /* if (str.length <= 0) {
                    alert("未选中数据！");
                    return false;
                } */
        openProgressBar();
        $.post("/asp/chooseservice.aspx", {
            "service": str
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                alert(returnedData);
                //window.location.reload();
            }
        });
        return false;
    }

    window.onload = function () {
        var url = window.location.search;
        if (url == "?qiandao") {
            $(".qiandao_class").find("a").click();
        }

        if (url == "?tab_deposit") {
            $(".cunkuang").find("a").click();
        }

        if (url == "?tab_withdraw") {
            $(".tikuang").find("a").click();
        }

        if (url == "?tab_transfer") {
            $(".zhuanzhangle").find("a").click();
        }

        if (url == "?user-vip") {
            $(".user-vip").find("a").click();
        }
 
        if (url == "?user-vip") {
            $(".user-vip").find("a").click();
        }
        if (url == "?tab_mrrw") {
            $(".qiandao_class").find("a").click();
            $('#tab_mrrw').find('[href="#tab-money"]').click();
        }
        if (url == "?tab-friends") {
            $('#tj-friend').trigger('click');
        }
        if (url == "?tj-link") {
            $('#tj-friend').trigger('click');
            $('#friends-records-action').trigger('click');
        }
        if (url == "?tab-massages") {
            $(".tab-meir").find("a").click();
        }

        $("#qiandao_money").text($("#todayGet").text())
        $("#hongbao_money").text($("#hbMoney").text())
    }

    $(function () {
        var $tabAboutUs = $('#j-user-nav');
        //关于我们标签选中
        $tabAboutUs.length && $tabAboutUs.find('a[href="' + window.location.hash + '"]').tab('show');

        $(".bangding").click(function () {
            $("#j-user-nav li").find('a[href="#user-vip"]').tab('show');
        })

        $("#j-user-nav").find(".tikuang").click(function () {
            $("#tab_withdraw ul li").find('a[href="#qukuang"]').tab('show');
        })

        var getStr = function (url) {
            if (!url) return false;
            return url.split("?")[1];
        }
        var action = getStr(window.location.href);
        if (action == "tab_red_envelope") {

        }


    });
</script>

<input type="hidden" value="${session.customer.level}" id="j-myLevel">
<input type="hidden" id="deposit-loginname" value="${session.customer.loginname}">
<input type="hidden" id="typeName" value="<%request.getParameter("action");%>"/>
<input type="hidden" id="xingming" value="${session.customer.accountName}"/>
<input type="hidden" id="youxiang" value="${session.customer.email}"/>

<%--
<script src="${ctx}/js/user/userQuickSave.js?v=0316"></script>
--%>
<script type="text/javascript" src="${ctx}/js/allInOnePay.js?v=90000002"></script>

<!--<script type='text/javascript' charset='utf-8'
        src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js'></script>
<script src="//js.touclick.com/js.touclick?b=68aca137-f3c5-457b-87a4-8a46880b1e66"></script>-->
<script type="text/javascript" src="${ctx}/js/manageCoupons.js?v=19"></script>
<script type="text/javascript" src="${ctx}/js/self.js"></script>
<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.min.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.config.js"></script>
<script src="/js/userLetter.js?v=10"></script>
<script src="/js/syncJiuan.js?v=10"></script>
<script>
    !function () {
        suggestionService(1);
    }();

    !function () {
        letterService(1);
//      Highcharts.chart('j-account-information');
    }();

    //个人中心柱形图

    //  function Highchart() {
    ////        设置背景颜色
    //      Highcharts.setOptions({
    //          colors: ['#df5555', '#7087c3', '#5cb4c8', '#DDDF00', '#09affe']
    //      });
    //      $.ajax({
    //          url: "/asp/queryProfit.aspx",
    //          type: "post",
    //          success: function (data) {
    //              var obj = eval('(' + data + ')');
    //              var yingLi = Number(obj['yingLi']);
    //              var tiKuan = Number(obj['tiKuan']);
    //              var touZhu = Number(obj['touZhu']);
    //              var mc = Number(obj['mc']);
    //              var online = Number(obj['online']);
    //              // Create the chart
    //              Highcharts.chart('j-account-information', {
    //                  chart: {
    //                      type: 'column'
    //                  },
    //                  xAxis: {
    //                      type: 'category'
    //                  },
    //                  yAxis: {
    //                      title: {
    //                          text: '金额'
    //                      },
    //                      minRange: 100
    //                  },
    //                  legend: {
    //                      enabled: false
    //                  },
    //                  plotOptions: {
    //                      series: {
    //                          borderWidth: 0,
    //                          dataLabels: {
    //                              enabled: true,
    //                              format: '{point.y:.1f}元'
    //                          }
    //                      }
    //                  },
    //                  series: [{
    //                      name: 'Brands',
    //                      colorByPoint: true,
    //                      data: [
    ////                            {
    ////                            name: '盈利',
    ////                            y: yingLi
    ////                        }, {
    ////                            name: '提款',
    ////                            y: tiKuan
    ////                        },
    //                          {
    //                              name: '总存款',
    //                              y: online + mc
    //                          },
    //                          {
    //                              name: '秒存存款',
    //                              y: mc
    //                          }
    ////                            ,{
    ////                                name: '投注额',
    ////                                y: touZhu
    ////                            }
    //                      ]
    //                  }]
    //              });
    //          },
    //          fail: function () {
    //              console.log('fail');
    //          }
    //      });
    //  }
    //
    //  Highchart();

    //游戏转账
    function redRainMonery() {
        var redRainOut = $("#redRainOut").val();
        var redRainIn = $("#redRainIn").val();
        var redRainMoney = $("#redRainMoney").val();
        var rex = /^[0-9]+$/;
        if (redRainOut && redRainIn && redRainMoney) {
            if (redRainMoney < 10) {
                alert("转帐金额不能少于10元!")
            }
            else if (!rex.test(redRainMoney)) {
                alert("转账金额只能是整数哦。");
            }
            else {
                if (redRainOut == 'redrain') {
                    openProgressBar();
                    $.post("/redrain/transferInforRedRain.aspx", {
                        "signType": redRainIn,
                        "redRainRemit": redRainMoney
                    }, function (returnedData, status) {
                        if ("success" == status) {
                            redRainMoneryOut(redRainOut);
                            redRainMoneryIn(redRainIn);
                            closeProgressBar();
                            alert(returnedData);
                        }
                    })
                }
                else {
                    openProgressBar();
                    //主账户转红包雨账户
                    $.post("/asp/updateGameMoney.aspx", {
                        "transferGameIn": 'redrain',
                        "transferGameMoney": redRainMoney
                    }, function (returnedData, status) {
                        if ("success" == status) {
                            redRainMoneryOut(redRainOut);
                            redRainMoneryIn(redRainIn);
                            closeProgressBar();
                            alert(returnedData);
                        }
                    })
                }
            }
            //红包账户转游戏平台

        } else {
            alert("请输入正确金额!")
        }
    }

    function redrainForFriend() {
        var loginname = $("#redrainToFriendName").val();
        var redRainRemit = $("#redrainToFriendMoney").val();
        var rex = /^[0-9]+$/;

        if (loginname == '') {
            alert("请输入好友账户!")
        }
        else if (redRainRemit == '') {
            alert("请输入转账金额!")
        }
        else if (redRainRemit < 10) {
            alert("转账金额至少10元!")
        }
        else if (!rex.test(redRainRemit)) {
            alert("转账金额只能是整数哦。");
        }
        else if (loginname && redRainRemit) {
            openProgressBar();
            $.post("/redrain/transferInforRedRaintoUser.aspx", {
                "loginname": loginname,
                "redRainRemit": redRainRemit
            }, function (returnedData, status) {
                if ("success" == status) {
                    closeProgressBar();
                    alert(returnedData);
                }
            })
        }
    }

    //获取游戏金额
    function redRainMoneryOut(gameCode) {
        $("#redRainIn").html('');
        $("#redRainOutDiv").html("<img src='/images/waiting.gif'>");
//        $("#redRainInDiv").html("<img src='/images/waiting.gif'>");
        if (gameCode == "self") {
            $.post("/asp/getGameMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainOutDiv").html(returnedData);
                        $("#redRainIn").html('<option value="">请选择目标帐户</option>' + '<option value="redrain">红包账户</option>')
                    }
                });
        }
        else if (gameCode == "redrain") {
            $.post("/redrain/queryRedRainMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {

                        $("#redRainOutDiv").html(returnedData);
                        $("#redRainIn").html(
                            '<option value="">请选择目标帐户</option>' +
                            '<option value="newpt"> PT账户</option>' +
                            '<option value="ttg"> TTG账户</option>' +
                            '<option value="slot">老虎机钱包账户(SW,MG,DT,PNG,QT,NT)</option>'
                        )
                    }
                });
        }
    }

    //获取游戏金额
    function redRainMoneryIn(gameCode) {
        $("#redRainInDiv").html("<img src='/images/waiting.gif'>");
        if (gameCode == "redrain") {
            $.post("/redrain/queryRedRainMoney.aspx",
            		
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainInDiv").html(returnedData);
                    }
                });
        }
        else {
            $.post("/asp/getGameMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainInDiv").html(returnedData);
                    }
                });

        }
    }

    var ceoList = "";

    function redrecord() {
        //红包雨记录
        $.post("/redrain/queryRedRainCount.aspx", function (returnedData, status) {
            if ("success" == status) {
                ceoList = returnedData;
                // console.log(ceoList);
                pageContents(1);
            }
        });
    }

    function pageContents(index) {
        var num = 8;
        var total = ceoList.length;
        if (ceoList.length < 1) {
            $("#list-table1 tbody").html("<tr><td colspan='5'>查无资料</td></tr>");
            return false;
        }

        var html = "";
        var dataList = ceoList;
        var dataStart = index;
        var dataEnd = index * num;

        if (index > 1) {
            dataStart = ((index - 1) * num) + 1;
        }
        // console.log(dataStart);

        for (var i = dataStart - 1; i < dataEnd; i++) {

            if (dataList[i]) {
                html += "<tr>";
                html += "<td>" + (i + 1) + "</td>";
                html += "<td>" + dataList[i]['typeId'] + "</td>";
                html += "<td>" + dataList[i]['times'] + "</td>";
                html += "<td>" + dataList[i]['money'] + "</td>";
                html += "<td>" + dataList[i]['date'] + "</td>";
                html += "</tr>";
            }

        }
        // 分页记录
        var paginationHtml = "";
        var page_max = Math.ceil(total / num);
        var page_next = index + 1;
        var page_past = index - 1;
        paginationHtml += '<div class="pagination" style="height: 40px;">';
        paginationHtml += '每页&nbsp;' + num + '&nbsp;条记录&nbsp;&nbsp;';
        paginationHtml += '第&nbsp;' + index + '/' + page_max;
        paginationHtml += '&nbsp;页&nbsp;&nbsp;&nbsp;';
        paginationHtml += '<a href="javascript:void(0);" onclick="pageContents(1);">首页</a>&nbsp;';
        if (index != 1) {
            paginationHtml += '<a href="javascript:void(0);" onclick="pageContents(' + page_past + ');">上一页</a>&nbsp;&nbsp;';
        }
        if (index != page_max) {
            paginationHtml += '<a href="javascript:void(0);" onclick="pageContents(' + page_next + ');">下一页</a>&nbsp;&nbsp;';
        }
        paginationHtml += '<a href="javascript:void(0);" onclick="pageContents(' + page_max + ');">尾页</a>&nbsp;';
        paginationHtml += '</div>';
        // append 第一个：邮件列表, 第二个：分页记录
        $("#list-table1 tbody").html(html)
        $("#redRainpages").html(paginationHtml);
    }

    var $friendsRecord = $('#record-hy'),
        $friendsRecordsAction = $('#friends-records-action'),
        $friendsIframe = $friendsRecord.find('iframe');
    $(function () {
        $friendsRecordsAction.on('click', (function () {
            var $this = $(this);
            $friendsIframe.show();
            $friendsIframe.attr('src', '');
            $friendsIframe.attr('src', $this.attr('data-href'));
        }));

        $friendsRecordsAction.find('a').first().trigger('click');
    });
</script>
<script>
    // 自助存送内部弹窗
    $('#openBcbsModal').on('click', function () {
        $('#bcbsTable').add('#bcbs-mk').show();
    })
    $('#closeBcbsModal').add('#bcbs-mk').on('click', function () {
        $('#bcbsTable').add('#bcbs-mk').hide();
    })

</script>
<script>
        //设置提款密码
        function updateDatePayPassword() {
            var newPayPassWord = $("#newPayPassWord").val(),
                data = {};
            // console.log($("#noPayPassword").val())
            if ($("#loginPassWord").val()) {
                data.password = $("#loginPassWord").val();
            } else {
                data.content = $("#oldPayPassWord").val()
            }
            data.new_content = newPayPassWord;
            openProgressBar();
            $.post("/asp/change_pwsPayAjax.aspx", data, function (returnedData, status) {
                if ("success" == status) {
                    closeProgressBar();
                    alert(returnedData);
                    data = null;
                } else {
                    closeProgressBar();
                    alert(returnedData);
                }
            });
            return false;
        }

        // 生日礼金
        $(function(){
            var _id = '';
            var _state =false;
            var _isDraw = false;
            var _vip =['新会员','忠实VIP','星级VIP','黄金VIP','白金VIP','钻石VIP','至尊VIP']
            function queryBirthday(){
                $("#redCoupon-submit .birthday-name").html($())
                $.post("/asp/getBirthdayMoney.aspx",{}, function (result) {
                    console.log(result)
                    $('#birthday-birth').html(result.birthday)
                    $('#vip-grade,#vip-grade1,.vip-grade1').html(_vip[result.level])
                    $('#birthday-moeny').html(result.amount)
                    $('.birthday-moeny').html(result.amount)
                    _state = result.state
                    _isDraw = result.isDraw
                    _id=result.id;
                    if(_isDraw){
                        $('#redCoupon-submit').addClass('gray')
                        $('#redCoupon-submit').html('已领取')
                    }else{
                        $('#redCoupon-submit').removeClass('gray');
                        $('#redCoupon-submit').html('领取')
                    }
                    if(!_state){
                        $('#redCoupon-submit').html('还未到时间')
                    }
                });

            }
            queryBirthday()
            $("#draw-record").click(function(){
                 var _table = '';
                  $.post("/asp/queryBirthdayRecords.aspx",{}, function (result) {
                    var arr = JSON.parse(result)
                    if(arr.length<=0){
                        layer.msg('还没有记录',{skin:'birthday-class'});
                        return
                    }
                    for(var i=0;i<arr.length;i++){
                        _table += '<tr><td>'+arr[i].activityName+'</td><td>'+arr[i].activityPercent+'</td><td>'+arr[i].remark+'</td></tr><tr><td>'
                    }
                    layer.open({
                      type: 1 //Page层类型
                      ,title: '领取记录'
                      ,skin:'birthday-succes-alert'
                      ,area: ['430px','300px']
                      ,shade: 0.6 //遮罩 透明度
                      ,closeBtn: 0
                      ,anim:5 //0-6的动画形式，-1不开启
                      ,content: '<div class="alert"><table><tr><th>类别</th><th>金额</th><th>时间</th>'+_table+'</table></div>'
                      ,btn:['我知道了']
                    }); 
                  })
               

                
            })
            $('#tab-birthday #redCoupon-submit ,.birthday-btn-no').on('click',function(){
                if(_isDraw) return;
                $.post("/asp/drawBirthdayMoney.aspx", {id:_id}, function (result) {
                    if(result.state){
                        layer.open({
                          type: 1 //Page层类型
                          ,title: '领取礼金'
                          ,skin:'birthday-succes-alert'
                          ,area: ['430px']
                          ,shade: 0.6 //遮罩 透明度
                          ,closeBtn: 0
                          ,anim:5 //0-6的动画形式，-1不开启
                          ,content: '<div class="alert"><p class="b-moeny">'+result.message+'</p></div>'
                          ,btn:['我知道了']
                        }); 
                    }else{
                        layer.msg(result.message,{skin:'birthday-class'});
                    }
                    queryBirthday()
                });
            })
        })
</script>
</body>

</html>

