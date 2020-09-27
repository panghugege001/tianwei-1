<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE >
<html>
<head>
    <script type="text/javascript" src="mobile/js/ModeControl.js"></script>
    <script type="text/javascript" src="/js/layer/mobile/layer.js"></script>
</head>
<link rel="stylesheet" href="/mobile/css/deposit.css?v=666"/>

<body>
<!--内容-->

<ul>

    <!-- 秒存 -->
    <li>
        <button class="title icon_quick" id="deposit-fast" class="title icon_quick"><span
                class="i-right icon_jj"></span>支付宝、微信、网银存款<span style="color: red;">加赠0.5%</span></button>
        <div id="deposit-fast-page-1" class="page">
            <!--极速转账-->
            <div class="tab-content">
                <!--		<div class="space-2"></div>-->
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-10">
                    <!--		<font color="red">使用该通道，存款加赠0.5%！</font>-->
                    <div class="space-2"></div>
                    <div class="mui-select">
                        <div id="deposit-fast-type"></div>
                        <label>银行种类</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-fast-name" type="text">
                        <label>存款姓名</label>
                    </div>
                    <div id="selectpay" class="mui-select">
                        <input id="deposit-fast-bank" type="text">
                        <label>存款银行</label>
                    </div>
                    <div id="card" class="testfueld">
                        <input id="deposit-fast-card" type="text" data-rule-digits="true" minlength="16" maxlength="20"
                               placeholder="请输入您的存款卡号...">
                        <label>存款卡号</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-fast-money" type="text" data-rule-digits="true" maxlength="10"
                               placeholder="存款金额额度为1元-300万">
                        <label>存款金额</label>

                    </div>
                    <%--<span class="money_prompt">请您输入小数点，以确保实时到账！(如10.11)</span>--%>
                    <!--<div class="testfueld">
		<div id="btn-history" class="mui-btn mui-btn--raised mui-btn--primary small">查看历史银行记录</div>
		</div>-->
                    <div class="space-2"></div>
                    <div id="deposit-fast-submit" class="mui-btn mui-btn--raised mui-btn--primary block ">下一步</div>
                </div>

                <div class="space-2"></div>
            </div>
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                <div class="h3"><strong>温馨提示：</strong></div>
                <p>
                    <span style="color: red;">使用秒存转账赠0.5%，满300再赠免费红包。</span><br/>
                    请务必按照系统提示消息进行存款，银行卡转账“附言”必须填写，支付宝转账无需附言完成之后请点击“我已成功存款”，否则您的款项将无法及时到账<br/>
                    如果您的款项10分钟未能到账，请联系24小时在线客服！<br/>
                    此存款方式无需手续费

                </p>
                <div class="space-2"></div>
            </div>
        </div>

        <div id="deposit-fast-page-2" class="page" style="display:none;">
            <!--极速转账-->
            <div class="tab-content">
                <!--		<div class="space-2"></div>-->
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-10">
                    <font class="red red_content">我们的收款账户</font>
                    <div class="space-2"></div>
                    <div class="testfueld">
                        <input type="text" readonly disabled="disabled" id="sbankname">
                        <label>收款银行</label>
                    </div>
                    <div class="testfueld">
                        <input type="text" id="saccountno" readonly>
                        <button class="btn-copy" data-clipboard-action="copy" data-clipboard-target="#saccountno">复制
                        </button>
                        <label>收款账号</label>
                    </div>
                    <div class="testfueld">
                        <input type="text" id="saccountname" readonly>
                        <button class="btn-copy" data-clipboard-action="copy" data-clipboard-target="#saccountname">复制
                        </button>
                        <label>收款人姓名</label>
                    </div>
                    <div id="fyan" class="testfueld">
                        <input id="mefuyan" type="text" readonly>
                        <button class="btn-copy" data-clipboard-action="copy" data-clipboard-target="#mefuyan">复制
                        </button>
                        <label>附言</label>
                        <%--<div id="fyantip" class="message">请在“转账用途”或“附言”处填写您的附言，款项才能实时到账！</div>--%>
                    </div>
                    <div id="fyan2" class="testfueld">
                        <div class="message">请务必在转账时按照您的存款信息转账，存款将在1分钟之内添加到您的游戏账号中</div>
                    </div>
                    <div class="space-2"></div>
                </div>
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-10">
                    <font class="red red_content">您的存款信息</font>
                    <div class="space-2"></div>
                    <div class="testfueld">
                        <input type="text" id="quick-confirm-type" readonly disabled="disabled">
                        <label>存款方式</label>
                    </div>
                    <div class="testfueld">
                        <input type="text" id="quick-confirm-username" readonly disabled="disabled">
                        <label>存款姓名</label>
                    </div>
                    <!--<div id="ckyh" class="testfueld">
			<input type="text" id="quick-confirm-bank" readonly="readonly" disabled="disabled">
			<label>存款银行</label>
		</div>
		<div id="ckkh" class="testfueld">
			<input type="text" id="quick-confirm-card" readonly="readonly" disabled="disabled">
			<label>存款卡号</label>
		</div>-->
                    <div class="testfueld">
                        <input type="text" id="quick-confirm-money" readonly disabled="disabled">
                        <label>存款金额</label>
                    </div>

                    <div class="space-2"></div>

                    <div class="mui-btn mui-btn--raised mui-btn--primary block mb25 fast-wexin-chk"
                         style="padding:0px;">查看微信转账流程
                    </div>
                    <div id="deposit-fast-success" class="mui-btn mui-btn--raised mui-btn--primary block">
                        我已成功付款
                    </div>
                </div>


            </div>
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                <div class="h3"><strong>温馨提示：</strong></div>
                <p>
                    <span style="color: red;">使用秒存转账赠0.5%，满300再赠免费红包。</span><br/>
                    请务必按照系统提示消息进行存款，银行卡转账“附言”必须填写，支付宝转账无需附言完成之后请点击“我已成功存款”，否则您的款项将无法及时到账<br/>
                    如果您的款项10分钟未能到账，请联系24小时在线客服！<br/>
                    此存款方式无需手续费

                </p>
                <div class="space-2"></div>
            </div>
        </div>

        <div id="deposit-fast-page-3" class="deposit-fast-page" style="display:none;">
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5" style="    width: 100%;
    padding: 20px;">
                <p class="font2 my-input-title red">您需要存入金额：</p>
                <p class="font4 my-input-title red" id="real-save-money"></p>
                <div class="space-2"></div>
                <div class="gray-tips red font2">温馨提示：请您存入该金额，否则存款无法到账，本订单有效时间为2小时</div>
                <div class="space-2"></div>
                <div class="tips"><input type="checkbox" id="must-checked">我已明白需要转账 <span
                        class="save-money red"></span>元
                </div>
                <div class="tips"><input type="checkbox" id="must-checked1">本人已同意，如未转账 <span
                        class="save-money red"></span>元，导致系统无法匹配存款，天威概不负责！
                </div>
                <div class="space-2"></div>
                <div class="mui-btn mui-btn--raised mui-btn--primary block fast-next">下一步</div>
            </div>
        </div>

        <!--微信秒存转帐说明-->
        <div id="wechat-desc-page" class="deposit-fast-page" style="display:none;">
            <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
                <div class="relative">
                    <div class="btn-copy" onclick="returnWechatPage();">返回</div>
                </div>
                <div class="space-2"></div>
                <h3>微信转账流程</h3>

                <div class="space-2"></div>
                <p>步骤一：下载最新微信版本登录</p>

                <div class="space-2"></div>
                <p>步骤二：点击头像下方的钱包</p>
                <div><img src="/images/deposit/5-1.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤三：点击左上角收付款</p>
                <div><img src="/images/deposit/1-2.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤四：点击最下方的转账到银行卡</p>
                <div><img src="/images/deposit/2-2.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤五：使用系统提示的金额进行转账</p>
                <div><img src="/images/deposit/3-3.jpg"/></div>
                <div class="space-2"></div>

                <div><img src="/images/deposit/4-4.jpg"/></div>
                <div class="space-2"></div>

                <div class="btn-copy" style="position: static;" onclick="returnWechatPage();">返回</div>
                <div class="space-5"></div>
            </div>
        </div>

    </li>

    <!-- 云闪付 -->
    <li>
        <button class="title icon_quick" id="deposit-cloud" class="title icon_quick"><span
                class="i-right icon_jj"></span>云闪付<span style="color: red;">加赠0.5%</span></button>
        <div id="deposit-cloud-page-1" class="page">
            <!--极速转账-->
            <div class="tab-content">
                <!--		<div class="space-2"></div>-->
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-10">
                    <div class="space-2"></div>
                    <div class="testfueld">
                        <input id="deposit-cloud-card" type="text" data-rule-digits="true" maxlength="4"
                               placeholder="请输入您的存款卡号..." onkeyup="value=value.replace(/[^\d]/g,'');">
                        <label>存款卡号</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-cloud-money" type="text" data-rule-digits="true" maxlength="10"
                               placeholder="存款金额额度为1元-300万" onkeyup="value=value.replace(/[^\d]/g,'');">
                        <label>存款金额</label>

                    </div>
                    <div class="space-2"></div>

                    <div class="text-center">
                        <div style="margin-bottom:5px;"><font color="red">云闪付APP下载二维码</font></div>
                        <img src="/images/deposit/yunshanfu_qrcode.png" width="200">
                    </div>

                    <div class="space-2"></div>

                    <div id="deposit-cloud-submit" class="mui-btn mui-btn--raised mui-btn--primary block ">下一步</div>
                </div>

                <div class="space-2"></div>
            </div>
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                <div class="h3"><strong>温馨提示：</strong></div>
                <p>
                    <span style="color: red;">1.云闪付请下载云闪付APP才能进行存款支付。</span><br/>
                    2.云闪付存款需要填写正确支付银行卡号末四码系统才会正确匹配到帐。<br/>
                    3.支付金额需要与填写存款金额一样系统才会匹配上分。<br/>
                    <span class="dark" style="color: red;">4.使用云闪付秒存转账赠0.5%，满300再赠免费红包。</span>
                </p>
                <div class="space-2"></div>
            </div>
        </div>

        <div id="deposit-cloud-page-2" class="deposit-fast-page" style="display:none;">
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5" style="    width: 100%;
    padding: 20px;">
                <p class="font2 my-input-title red">您需要存入金额：</p>
                <p class="font4 my-input-title red quota"></p>
                <div class="space-2"></div>
                <div class="gray-tips red font2">温馨提示：请您存入该金额，否则存款无法到账，本订单有效时间为2小时</div>
                <div class="space-2"></div>
                <div class="tips"><input type="checkbox" id="j-cloudpay-checked">
                    <label for="j-cloudpay-checked">我已明白需要转账<span class="quota red"></span>元</label>
                </div>
                <div class="tips"><input type="checkbox" id="j-cloudpay-checked1">
                    <label for="j-cloudpay-checked1">本人已同意，如未转账<span
                            class="quota red"></span>元，导致系统无法匹配存款，天威概不负责！</label>
                </div>
                <div class="space-2"></div>
                <div class="mui-btn mui-btn--raised mui-btn--primary block "
                     id="j-cloud-agree">
                    下一步
                </div>
            </div>
        </div>
        <div id="deposit-cloud-page-3" class="page" style="display:none;">
            <!--极速转账-->
            <div class="tab-content">
                <!--		<div class="space-2"></div>-->
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-10">
                    <font class="red red_content">我们的收款账户</font>
                    <div class="space-2"></div>
                    <div class="text-center">
                        <img id="cImgCode" style="width:80%;">
                    </div>

                    <div class="space-2"></div>

                    <div class="mui-btn mui-btn--raised mui-btn--primary block mb25 "
                         onclick="window.open('http://cn.unionpay.com/zt/2017/139595361/','_blank');"
                         style="padding:0px;">查看云闪付流程
                    </div>
                    <div id="deposit-cloud-success" class="mui-btn mui-btn--raised mui-btn--primary block">
                        我已成功付款
                    </div>
                </div>


            </div>
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                <div class="h3"><strong>温馨提示：</strong></div>
                <p>
                    <span style="color: red;">使用秒存转账赠0.5%，满300再赠免费红包。</span><br/>
                    请务必按照系统提示消息进行存款，银行卡转账“附言”必须填写，支付宝转账无需附言完成之后请点击“我已成功存款”，否则您的款项将无法及时到账<br/>
                    如果您的款项10分钟未能到账，请联系24小时在线客服！<br/>
                    此存款方式无需手续费

                </p>
                <div class="space-2"></div>
            </div>
        </div>


    </li>

    <!-- 久安支付 -->
    <li>
        <button class="title icon_weixin" id="deposit-jiuan"><span class="i-right icon_jj"></span>久安支付<span
                style="color: red;">加赠1%</span></button>
        <div class="page">
            <div class="tab-content" id="deposit-jiuan-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="mui-select">
                        <select id="m-deposit-jiuan-pay">
                            <option value="">请选择</option>
                        </select>
                        <label>久安支付</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-jiuan-money" type="text" placeholder="0.00">
                        <label>存款金额</label>
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block mt10"
                         onclick="window.open('http://www.jiuan365.com','_blank');">点击下载久安APP
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block mt10"
                         onclick="window.open('http://www.longdobbs.com/forum.php?mod=viewthread&tid=2823&highlight=%E4%B9%85%E5%AE%89', '_blank');">
                        点我了解久安教程
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block mt10" id="deposit-jiuan-submit">确定支付
                    </div>
                    <div id="deposit-jiuan-message" class="weixin-message"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>1.请先下载久安钱包进行充值。</p>
                    <p>2.人民币：UET比例为1:100。</p>
                    <p>3.使用久安钱包进行C2C交易安全无风险。</p>
                    <p>4.若存款未到账请联系在线客服。</p>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- 微信支付 -->
    <li>
        <button class="title icon_weixin" id="deposit-weixin"><span class="i-right icon_jj"></span>微信支付</button>
        <div class="page">
            <div class="tab-content" id="deposit-weixin-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="mui-select">
                        <select id="m-deposit-weixin-pay">
                            <option value="">请选择</option>
                        </select>
                        <label>微信支付</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-weixin-money" type="text" placeholder="0.00">
                        <label>存款金额</label>
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-weixin-submit">确定支付</div>
                    <div id="deposit-weixin-message" class="weixin-message"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>
                        1.【微信扫一扫支付】单笔最低存款1元，最高单笔存款3000 元。<br/>
                        2.【微信扫一扫支付】第三方需收取0.8%-2.5%不等的手续费。<br/>
                        3.请不要使用微信绑定的信用卡进行微信支付！如果使用信用卡支付，导致金额不能及时到账，我方恕不负责。<br/>
                        4.存款过程中如有任何问题，请及时联系24小时在线客服进行咨询。
                    </p>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- 支付宝在线支付 -->
    <li>
        <button class="title icon_zhifubao" id="deposit-zfbQR2" style="display: block;"><span
                class="i-right icon_jj"></span>支付宝在线支付
        </button>
        <div class="page">
            <!--支付宝在线-->
            <div class="tab-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="mui-select">
                        <select id="m-deposit-zfbQR2-pay">
                            <option value="">请选择</option>
                        </select>
                        <label style="margin: 0px;font-size: .9em;">支付宝在线支付</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-zfbQR2-money" type="text" placeholder="0">
                        <label>充值金额</label>
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-zfbQR2-submit">开始充值</div>
                    <div class="space-2"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>
                        1.【支付宝支付】最低存款金额<span class="c-red">10元</span>，单笔最高<span class="c-red">3000元</span>。
                    </p>
                    <p>
                        2.【支付宝支付】第三方需收取<span class="c-red">0.9%-2.5%</span>手续费。
                    </p>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- QQ支付 -->
    <li>
        <button class="title icon_weixin" id="deposit-QQ"><span class="i-right icon_jj"></span>QQ支付</button>
        <div class="page">
            <div class="tab-content" id="deposit-QQ-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="mui-select">
                        <select id="m-deposit-QQ-pay">
                            <option value="">请选择</option>
                        </select>
                        <label>QQ支付</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-QQ-money" type="text" placeholder="0.00">
                        <label>存款金额</label>
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-QQ-submit">确定支付</div>
                    <div id="deposit-QQ-message" class="weixin-message"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>
                        1.QQ支付存款最低<span class="c-red">2元</span>。
                    </p>
                    <p>
                        2.【微信扫一扫】需要承担<span class="c-red">1.2%-1.5%</span>手续费，收费标准根据第三方支付通道为主，手续费由第三方收取
                    </p>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- 京东支付 -->
    <li>
        <button class="title icon_weixin" id="deposit-jd"><span class="i-right icon_jj"></span>京东支付</button>
        <div class="page">
            <div class="tab-content" id="deposit-jd-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="mui-select">
                        <select id="m-deposit-jd-pay">
                            <option value="">请选择</option>
                        </select>
                        <label>京东支付</label>
                    </div>
                    <div class="testfueld">
                        <input id="deposit-jd-money" type="text" placeholder="0.00">
                        <label>存款金额</label>
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-jd-submit">确定支付</div>
                    <div id="deposit-jd-message" class="weixin-message"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>
                        1.京东支付存款最低<span class="c-red">2元</span>。
                    </p>
                    <p>
                        2.京东支付需要承担<span class="c-red">1.2%-1.5%</span>手续费，收费标准根据第三方支付通道为主，手续费由第三方收取
                    </p>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- 快捷存款 -->
    <li>
        <button class="title icon_mm" id="deposit-speedPay"><span class="i-right icon_jj"></span>快捷存款</button>
        <div class="page">
            <!--快捷存款-->
            <div class="tab-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">

                    <span class="space-1"></span>
                    <div class="mui-select iconfont">
                        <label>快捷支付</label> <select id="m-deposit-speedpay"
                                                    tabindex="0">
                        <option value="">请选择</option>
                    </select>
                    </div>

                    <span class="space-1"></span>


                    <div class="testfueld iconfont">
                        <label>充值金额</label>
                        <input id="deposit-speedPay-money" type="text" placeholder="0">
                    </div>


                    <div class="space-1"></div>

                    <div class="mbkj hidden">

                        <div class="testfueld">
                            <label>银行卡号</label>
                            <input type="text" class="my-input" id="speedpay-bankcard" placeholder="银行卡号"
                                   onkeyup="value=value.replace(/[^\d]/g,'');">
                        </div>
                        <div class="space-1"></div>
                        <div class="testfueld">
                            <label>银行卡户名</label>
                            <input type="text" class="my-input" id="speedpay-bankname" placeholder="银行卡户名">
                        </div>
                        <div class="space-1"></div>

                        <div class="testfueld">
                            <label>手机号</label>
                            <input type="text" class="my-input" id="speedpay-phoneNumber" placeholder="手机号"
                                   onkeyup="value=value.replace(/[^\d]/g,'');"
                                   maxlength="11">
                        </div>

                        <div class="space-1"></div>
                    </div>

                    <div class="mifkj hidden">
                        <div class="testfueld">
                            <label>银行卡号</label>
                            <input type="text" class="my-input" id="mifkj-bankcard" placeholder="银行卡号"
                                   onkeyup="value=value.replace(/[^\d]/g,'');">
                        </div>
                        <div class="space-1"></div>

                        <div class="mui-select iconfont">
                            <label>银行卡户名</label>
                            <select id="mifkj-bankname" class="my-input"></select>
                        </div>
                        <div class="space-1"></div>

                    </div>

                    <div class="dbkj hidden">
                        <div class="mui-select iconfont">
                            <label>银行卡户名</label>
                            <select id="dbkj-bankname" class="my-input"></select>
                        </div>
                        <div class="space-1"></div>
                    </div>


                    <div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-speedPay-submit">开始充值</div>
                    <div class="space-2"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>
                        单笔存款最低额度为1元，最高5000元。<br/>
                        如有疑问请联系QQ客服： 800134430。<br/>
                    </p>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- 在线支付 -->
    <li>
        <button class="title icon_zaixian" id="deposit-thirdPay"><span class="i-right icon_jj"></span>在线支付</button>
        <div class="page">
            <!--第三方-->
            <div class="tab-content" id="deposit-thirdPay-content">
                <div class="space-2"></div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="sanfang">
                        <input id="deposit-thirdPay-money" type="text" placeholder="0.00">
                        <label>充值金额</label>
                    </div>
                    <div class="mui-select">
                        <select id="m-deposit-thirdPay-pay" tabindex="0">
                            <option value="">请选择</option>
                        </select>
                        <label>在线支付</label>
                    </div>
                    <div class="mui-select">
                        <select class="" id="m-deposit-thirdPay-bank">
                            <option value="">请选择</option>
                        </select>
                        <label>银行种类</label>
                    </div>
                    <div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-thirdPay-submit">开始充值</div>
                    <div class="space-2"></div>
                </div>
                <div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="space-2"></div>
                </div>
                <div class="h3 bold">温馨提示：</div>
                <p>
                    最低存款额度为<strong class="red" id="j-onlineMin">2</strong>元，最高<strong class="red"
                                                                                      id="j-onlineMax">50000</strong>元。<br/>
                    在线支付支持大陆各大银行以及信用卡，并且是及时到账的。<br/>
                    若支付成功未及时到账，请立即联系我们的在线客服。<br/>
                </p>
            </div>
        </div>
    </li>

    <!--点卡支付-->
    <li>
        <button class="title icon_cun" id="deposit-card"><span class="i-right icon_jj"></span>点卡支付</button>
        <div class="page">
            <div class="tab-content">
                <div class="space-2"></div>
                <div id="deposit-card-form" style="text-align:center;color:red;"></div>

                <div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-6 mui-col-xs64-offset-0">
                    <div class="space-2"></div>
                    <div class="h3"><strong>温馨提示：</strong></div>
                    <p>
                        存款最低金额为<span style="color:red;">1</span>元，点卡充值会扣取相应的手续费用。<br/>
                        存款过程中有任何疑问请联系我们的在线客服或者QQ客服、智付客服QQ：4008822311。<br/>
                        <span style="color:red;">建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的。到账金额比实际存款金额少。到账比例，请查看费率表。</span><br/>
                    </p>
                    <div class="space-2"></div>
                    <table>
                        <tbody id="cardTable">

                        </tbody>
                    </table>
                    <div class="space-2"></div>
                </div>
            </div>
        </div>
    </li>

    <!-- 通用支付 -->
    <%--<li>--%>
    <%--<button class="title icon_weixin" id="deposit-gather"><span class="i-right icon_jj"></span>通用支付,<span--%>
    <%--style="color: red;">微信、支付宝、QQ钱包</span></button>--%>
    <%--<div class="page">--%>
    <%--<div class="tab-content" id="deposit-gather-content">--%>
    <%--<div class="space-2"></div>--%>
    <%--<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">--%>
    <%--<img src="/images/gather-demo.jpg?v=1" style="width:100%;"/>--%>
    <%--<div class="space-2"></div>--%>
    <%--<p><font color="red">请扫描二维码</font></p>--%>
    <%--<img src="" style="width:100%;" id="j-gather-codeimg" style="width:100%;"/>--%>
    <%--<p><font color="red">请扫码后在备注栏填写您的游戏账号：(例如:longdu) 可立即到账,账号写错或不写则无法到账!!!--%>
    <%--重要：二维码实时更新，请务必每次刷新</font></p>--%>
    <%--</div>--%>
    <%--<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5">--%>
    <%--<div class="h3"><strong>温馨提示：</strong></div>--%>
    <%--<p>1.二维码实时更新，请每次刷新。</p>--%>
    <%--<p>2.单笔最低存款20元，最高存款5000。</p>--%>
    <%--<p>3.通用支付需承担0.35%的手续费，手续费由第三方收取。</p>--%>
    <%--<p>4.如果您已支付 请等待系统处理此笔订单，如有疑问可以随时联系在线客服进行谘询。</p>--%>
    <%--<div class="space-2"></div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</li>--%>
</ul>


<script type="text/javascript" src="mobile/js/clip.js"></script>
<script type="text/javascript">

    var _TEST_MODE = 0;

    //作废订单
    function destroyPayOrder(id) {
        layer.open({
            content: '确定作废当前订单!!'
            , btn: ['<span class="bule">确认</span>', '<span class="orange">取消</span>']
            , yes: function (index) {

                $.post("/asp/discardOrder.aspx", {id: $('#deposit-weixinfast-fuyan').val()}, function (data) {
                    if (data == '1') {
                        $('#deposit-weixinfast-page-1').removeClass('hidden');
                        $('#deposit-weixinfast-page-2').addClass('hidden');
                    }
                }).fail(function () {
                    alert("废除订单失败");
                });
                layer.close(index);
            }
        });


    };

    /*复制*/
    function copyToClipboard(elem) {
        // create hidden text element, if it doesn't already exist
        var targetId = "_hiddenCopyText_";
        var isInput = elem.tagName === "INPUT" || elem.tagName === "TEXTAREA";
        var origSelectionStart, origSelectionEnd;
        if (isInput) {
            // can just use the original source element for the selection and copy
            target = elem;
            origSelectionStart = elem.selectionStart;
            origSelectionEnd = elem.selectionEnd;
        } else {
            // must use a temporary form element for the selection and copy
            target = document.getElementById(targetId);
            if (!target) {
                var target = document.createElement("textarea");
                target.style.position = "absolute";
                target.style.left = "-9999px";
                target.style.top = "0";
                target.id = targetId;
                document.body.appendChild(target);
            }
            target.textContent = elem.textContent;
        }
        // select the content
        var currentFocus = document.activeElement;
        target.focus();
        target.setSelectionRange(0, target.value.length);
        // copy the selection
        var succeed;
        try {
            succeed = document.execCommand("copy");
        } catch (e) {
            succeed = false;
        }
        // restore original focus
        if (currentFocus && typeof currentFocus.focus === "function") {
            currentFocus.focus();
        }
        if (isInput) {
            // restore prior selection
            elem.setSelectionRange(origSelectionStart, origSelectionEnd);
        } else {
            // clear temporary content
            target.textContent = "";
        }
        alert("复制成功请粘贴")
        return succeed;


    }

    $(function () {

        if (_TEST_MODE == 0) {
            var _payWay = '/asp/pay_way.aspx';
        } else {
            var _payWay = '/data/deposit/payWay99.json?v=1';
        }

        $(".deposit-content ul li button").hide();

        mobileManage.getLoader().open('处理中');
        var DCardPaytype = false;
        $.ajax({
            type: 'POST',
            url: _payWay,
            dataType: "json",
            data: {usetype: 1},
            error: function (result) {
                alert(result);
            },
            success: function (result) {
                mobileManage.getLoader().close();

                $("#deposit-fast").show();
                $("#deposit-cloud").show();

                if (result.desc == '成功') {


                    for (var i in result.data) {
                        if (result.data[i].payWay == 1) {
                            $("#deposit-zfbQR2").show();
                            $('#m-deposit-zfbQR2-pay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '"  data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '">' + result.data[i].showName + '</option>')
                        }
                        if (result.data[i].payWay == 2) {
                            $("#deposit-weixin").show();
                            $('#m-deposit-weixin-pay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '" data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '">' + result.data[i].showName + '</option>')
                        }
                        if (result.data[i].payWay == 3) {
                            $("#deposit-thirdPay").show();
                            $('#m-deposit-thirdPay-pay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '"  data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '">' + result.data[i].showName + '</option>')
                        }
                        if (result.data[i].payWay == 4) {
                            $("#deposit-speedPay").show();
                            $('#m-deposit-speedpay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '"  data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '" data-platform="' + result.data[i].payPlatform + '">' + result.data[i].showName + '</option>')
                        }
                        if (result.data[i].payWay == 5) {
                            $("#deposit-card").show();
                            $('#deposit-card').attr('data-url', result.data[i].payCenterUrl);
                            $('#deposit-card').attr('data-id', result.data[i].id);
                            $('#deposit-card').attr('data-type', true);
                        }
                        if (result.data[i].payWay == 7) {
                            $("#deposit-QQ").show();
                            $('#m-deposit-QQ-pay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '" data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '">' + result.data[i].showName + '</option>')
                        }
                        if (result.data[i].payWay == 10) {
                            $("#deposit-jd").show();
                            $('#m-deposit-jd-pay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '" data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '">' + result.data[i].showName + '</option>')
                        }
                        if (result.data[i].payWay == 12) {
                            $("#deposit-jiuan").show();
                            $('#m-deposit-jiuan-pay').append('<option data-url="' + result.data[i].payCenterUrl + '" value="' + result.data[i].id + '" data-max="' + result.data[i].maxPay + '" data-min="' + result.data[i].minPay + '" data-fee="' + result.data[i].fee + '">' + result.data[i].showName + '</option>')
                        }
                    }

                    $('#m-deposit-thirdPay-pay').change(function () {
                        var max = $('#j-onlineMax');
                        var min = $('#j-onlineMin');
                        max.html($(this).find("option:selected").data('max'));
                        min.html($(this).find("option:selected").data('min'));
                        if ($('#m-deposit-thirdPay-pay').val() == '') {
                            var str = '<option value="">请选择</option>';
                            $('#m-deposit-thirdPay-bank').html(str);
                        }
                        else {
                            $.ajax({
                                type: 'POST',
                                url: "/asp/pay_bank.aspx",
                                dataType: "json",
                                data: {platformId: $('#m-deposit-thirdPay-pay').val()},
                                error: function (result) {
                                    alert(result);
                                },
                                success: function (result) {
                                    if (result.desc == '成功') {
                                        var str = '<option value="">请选择</option>';
                                        for (var i in result.data) {
                                            str += '<option value="' + result.data[i].dictValue + '">' + result.data[i].dictName + '</option>'
                                        }
                                        $('#m-deposit-thirdPay-bank').html(str);
                                    }
                                    else {
                                        alert("获取银行错误");
                                    }
                                },
                                fail: function (response) {
                                    alert("获取银行错误");
                                }
                            })
                        }
                    })
                }
                else {
                    alert('数据读取失败');
                }

            },
            fail: function (response) {
                alert("维护中");
            }
        });
    });

    function DepositPage() {
        //设定只能输入数字
        NumberInput('deposit-thirdPay-money');
        //取得上次存款模式
        var depositMode = mobileManage.getSessionStorage('fundsManage').depositMode || '0';

        var _$view = $('.deposit-content');
        var _deposit = {};

        var _$titles = $('ul>li>.title');

        _$titles.bind('click', _titleClickEvent);

        function _titleClickEvent() {

            var _$titles = $(this);
            if (_$titles.parent().hasClass('active')) {
                _$titles.parent().removeClass('active');
            } else {
                //关闭其他打开的
                _$view.find('ul>li.active').removeClass('active');
                _$titles.parent().addClass('active');
            }

//            $(".common-header .nav-left .back-action").attr("href", "/mobile/deposit.jsp");

            if (!_deposit[this.id]) {

                switch (this.id) {
                    case 'deposit-fast':
                        _deposit[this.id] = new _FastManage();
                        break;
                    case 'deposit-thirdPay':
                        _deposit[this.id] = new _ThirdPayManage();
                        break;
                    case 'deposit-weixin':
                        _deposit[this.id] = new _WeixinManage();
                        break;
                    case 'deposit-QQ':
                        _deposit[this.id] = new _QQManage();
                        break;
                    case 'deposit-card':
                        _deposit[this.id] = new _DCardManage();
                        break;
                    case 'deposit-zfbQR2':
                        _deposit[this.id] = new _ZFBQR2Manage();
                        break;
                    case 'deposit-speedPay':
                        _deposit[this.id] = new _SpeedPayManage();
                        break;
                    case 'deposit-gather':
                        _deposit[this.id] = new _GatherManage();
                        break;
                    case 'deposit-jd':
                        _deposit[this.id] = new _JDManage();
                        break;
                    case 'deposit-jiuan':
                        _deposit[this.id] = new _JIUANManage();
                        break;
                    default:
                }
            }
        }


        var _gatherPayWay = "";
        if (_TEST_MODE == 0) {
            _gatherPayWay = "/mobi/getGatherDeposti.aspx";
        } else {
            _gatherPayWay = "/data/deposit/gather_false.json";
        }

        $(function () {
            _GatherManageInit();
            _cloudPayManage();
        });

        function _GatherManageInit() {
            $("#deposit-gather").parents("li").addClass("hidden");
//            $.post(_gatherPayWay, {"platformID": "tlzf"}, function (response) {
//                if (response.success != true) {
//                    $("#deposit-gather").parents("li").addClass("hidden");
//                }
//            }).fail(function () {
//                $("#deposit-gather").parents("li").addClass("hidden");
//            });
        }


        function _GatherManage() {
//            $.post(_gatherPayWay, {"platformID": "tlzf"}, function (response) {
//                if (response.success == true) {
//                    $('#j-gather-codeimg').attr('src', response.data.bank.zfbImgCode);
//                }
//            });
        }


        //_init();
        /**
         * 初始化
         */
        function _init() {
            mobileManage.getLoader().open('初始化');
            mobileManage.getTPPManage().getAllPayments(function (result) {
                mobileManage.getLoader().close();
                if (!result.success) return;

                filterPayPage(result.data);

            });
        }

        /**
         * 支付方式开关
         */
        function filterPayPage(data) {
            /**
             * 支付方式开关
             */
            var payPageArr = {
//	 				'deposit-zfbQR1':['汇潮'],
//	 				'deposit-zfbRemark':['汇潮'],
//	 				'deposit-thirdPay':['汇潮'],
//					'deposit-WXValidate':[],
                'deposit-zfbQR2': ["口袋支付", '优付支付宝', '新贝支付宝', '银宝支付宝', '千网支付宝', '口袋支付宝2', '迅联宝支付宝'],
                'deposit-WXValidate': ['微信额度验证'],
                'deposit-weixin': ['口袋微信支付', '新贝微信', '乐富微信', '智付微信', '智付微信1', '口袋支付', '口袋微信支付2', '口袋微信支付3', '迅联宝', '优付微信', '千网微信', '讯付通微信', '多宝微信'],
                'deposit-card': ['智付点卡1'],
                'deposit-speedPay': ['汇潮']
            };

            var open = {};
            for (var i in payPageArr) {
                open[i] = false;
                for (var j in data) {
                    if (payPageArr[i].indexOf(data[j]) != -1) {
                        open[i] = true;
                        break;
                    }
                }
            }
            //移除没有使用的
            for (var i in open) {
                if (!open[i] && _$view.find('#' + i).length > 0) {
                    _$view.find('#' + i).parent().remove();
                }
            }
        }


        // 云闪付
        function _cloudPayManage() {

            var saveCard = "", saveMoney = "";
            var $parent = "";

            var $saveCard = "", $saveMoney = "";
            var $saveSubmitBtn = $("#deposit-cloud-submit");
            var $saveRestartBtn = $(".cloud-restart");
            var $saveSuccessBtn = $("#deposit-cloud-success");

            var $quota = $(".quota");
            var $saveCheck = $("#j-cloudpay-checked");
            var $saveCheck1 = $("#j-cloudpay-checked1");
            var $agreeSubmitBtn = $("#j-cloud-agree");

            function _init() {

                $saveSubmitBtn.click(function () {
                    $parent = $(this).parents(".pay-content");
                    _cloudPay();
                });

                $saveRestartBtn.click(function (e) {
                    e.preventDefault();
                    layer.closeAll();
                    _resetCloudPay();
                });

                $saveSuccessBtn.click(function (e) {
                    e.preventDefault();
                    layer.open({
                        content: [
                            "如果您已经转账成功，请您稍等1---2分钟查看您的主账户。如果您没有进行转账，请您根据存款流程进行存款"
                        ],
                        btn: ["关闭"],
                        yes: function (index) {
                            layer.closeAll();
                            _resetCloudPay();
                        }
                    });
                });

            }

            function _chkInfo() {

                var msg = "";

                $saveCard = $("#deposit-cloud-card");
                $saveMoney = $("#deposit-cloud-money");

                saveCard = $saveCard.val().trim();
                saveMoney = $saveMoney.val().trim();

                if (saveCard == '' || saveCard == null || !saveCard) {
                    msg = "请填写您的银行卡号";
                    alert(msg);
                    return false;
                }

                if (saveCard.length != 4) {
                    msg = "请输入银行卡号四位数";
                    alert(msg);
                    return false;
                }

                if (saveMoney == '' || saveMoney == null || !saveMoney) {
                    msg = "请输入存款金额";
                    alert(msg);
                    return false;
                }
                if (saveMoney < 1 || saveMoney > 3000000) {
                    msg = "请输入1元到300万的金额";
                    alert(msg);
                    return false;
                }

                if (isNaN(saveMoney)) {
                    msg = "存款金额不得包含汉字";
                    alert(msg);
                    return false;
                }

                var reg = /^[1-9]\d*$/;
                if (!reg.test(saveMoney)) {
                    msg = "请输入整数";
                    alert(msg);
                    return false;
                }

                return true;
            }

            function _cloudPay() {

                var chkInfo = _chkInfo();

                if (!chkInfo) {
                    return false;
                }

                $saveSubmitBtn.attr("disabled", true);

                $quota.html(saveMoney);

                var formData = {
                    banktype: '5',
                    uaccountname: "云闪付",
                    ubankno: saveCard,
                    amount: saveMoney
                };

                // 同意书页面
                $("#deposit-cloud-page-2").show().siblings().hide();

                // 同意书
                $agreeSubmitBtn.off('click').on("click", function (e) {
                    e.preventDefault();
                    if ($saveCheck.prop('checked') && $saveCheck1.prop('checked')) {
                        // 创建订单
                        _deposit(formData);
                        return true;
                    } else {
                        alert("请同意天威存款条例！");
                        return false;
                    }
                });
            }


            function _deposit(formData) {

                document.documentElement.scrollTop = 0;
                document.body.scrollTop = 0;

                var newDepositUrl = "";

                if (_TEST_MODE == 1) {
                    newDepositUrl = "/data/deposit/newDeposit1.json";
                } else {
                    newDepositUrl = "/asp/getNewdeposit.aspx";
                }

                var jsonData = ajaxPost(newDepositUrl, formData);
                var massage = jsonData.massage;

                if (!massage) {
                    _showDepositInfo(jsonData)
                } else {

                    if (jsonData['force'] == true) {

                        layer.closeAll('loading');
                        //layer询问框
                        layer.open({
                            content: [
                                '您上一笔订单未支付，可点击作废订单，系统将生成新的存款信息'
                            ],
                            btn: ['作废上笔订单，生成新订单', '取消'],
                            yes: function () {

                                layer.closeAll();

                                formData['force'] = true;

                                if (_TEST_MODE == 1) {
                                    newDepositUrl = "/data/deposit/newDeposit2.json";
                                } else {
                                    newDepositUrl = "/asp/getNewdeposit.aspx";
                                }

                                var jsonData = ajaxPost(newDepositUrl, formData);
                                if (!jsonData.massage) {
                                    _showDepositInfo(jsonData);
                                } else {
                                    alert(jsonData.massage);
                                    _resetCloudPay();
                                    return false;
                                }
                            }

                        });
                    } else {
                        alert(jsonData.massage);
                        _resetCloudPay();
                        return false;
                    }

                }

            }


            function _showDepositInfo(response) {

                var zfbImgCode = response.zfbImgCode;

                if (zfbImgCode != "") {
                    $("#cImgCode").attr("src", zfbImgCode);
                    $("#deposit-cloud-page-3").show().siblings().hide();
                } else {
                    alert("二维码获取错误，请刷新再试！");
                }
            }


            // 重置秒存
            function _resetCloudPay() {
                $saveCard.val('');
                $saveMoney.val('');
                $quota.val('');
                $saveCheck.prop('checked', false);
                $saveCheck1.prop('checked', false);
                $saveSubmitBtn.attr('disabled', false);
                $("#deposit-cloud-page-1").show().siblings().hide();
            }

            _init();
        }

        //极速转帐Manage
        function _FastManage() {

            var _fast = this;
            var _bankInfos;
            var _nextFlag = false;

            var _TEST_MODE = 0;
            var saveType = "";
            var quickSaveData = "";

            _fast.$btnHistory = $('#btn-history');

            // 存款方式
            _fast.fastBank = new MobileComboBox({
                appendId: 'deposit-fast-type',
                cls: '',
                valueName: 'value',
                displayName: 'name',
                datas: [
                    {value: '2', name: '支付宝转账', bankType: '支付宝转账'},
                    {value: '0', name: '手机银行转账', bankType: '手机银行转账'},
                    {value: '1', name: '网上银行转账', bankType: '网上银行转账'},
                    {value: '4', name: '微信转账', bankType: '微信转账'}
                ],
                onChange: function (e) {
                    var $that = $("#m-deposit-fast-type");
                    var value = $that.find("option:selected").val();

                    if (value == "2") {
                        $("#card").hide();
                        $("#selectpay").hide();
                    } else {

                    }
                }
            });

            $("#card").hide();
            $("#selectpay").hide();

            // 存款銀行
            _fast.fastBank = new MobileComboBox({
                appendId: 'deposit-fast-bank',
                cls: '',
                valueName: 'value',
                displayName: 'name',
                datas: [
                    {value: '', name: '请选择', bankType: '请选择'},
                    {value: '中国工商银行', name: '中国工商银行', bankType: '中国工商银行'},
                    {value: '中国建设银行', name: '中国建设银行', bankType: '中国建设银行'},
                    {value: '中国交通银行', name: '中国交通银行', bankType: '中国交通银行'},
                    {value: '中国招商银行', name: '中国招商银行', bankType: '中国招商银行'},
                    {value: '中国农业银行', name: '中国农业银行', bankType: '中国农业银行'},
                    {value: '中国民生银行', name: '中国民生银行', bankType: '中国民生银行'},
                    {value: '中国银行', name: '中国银行', bankType: '中国银行'},
                    {value: '中国邮政', name: '中国邮政', bankType: '中国邮政'},
                    {value: '光大银行', name: '光大银行', bankType: '光大银行'},
                    {value: '广发银行', name: '广发银行', bankType: '广发银行'},
                    {value: '兴业银行', name: '兴业银行', bankType: '兴业银行'}
                ],
                onChange: function (e) {

                }
            });


            _fast.$btnHistory.click(function () {
                mobileManage.getModel().showHistoryBank('historyBank');
            });

            _fast.$btnFast = $("#deposit-fast-submit");
            // 下一步
            _fast.$btnFast.click(function (e) {
                e.preventDefault();

                var msg = "";
                saveType = $("#m-deposit-fast-type").val();
                var saveName = $("#deposit-fast-name").val();
                var saveCard = $("#deposit-fast-card").val();
                var saveBank = $("#m-deposit-fast-bank").find(":selected").val();
                var saveMoney = $("#deposit-fast-money").val();

                if (msg == "" && !saveType || saveType == "") {
                    msg = "[提示]请选择存款方式!";
                }

                if (msg == "" && !saveName || saveName == "") {
                    msg = "[提示]请填写您的存款姓名!";
                }

                var reg = /^[1-9]\d*(\.\d{1,2})?$/;
                if (msg == "" && !reg.test(saveMoney)) {
                    msg = "[提示]存款金额不得包含汉字或符号!";
                }

                if (msg == "" && !saveMoney || saveMoney == "") {
                    msg = "[提示]请选择存款金额!";
                    return false;
                }

                if (msg != "") {
                    alert(msg);
                    return false;
                }
                else {
                    if (saveType == 2) {
                        saveBank = "支付宝";
                    }

                    if (saveBank == "请选择") {
                        saveBank = "";
                    }

                    var formData = {
                        "banktype": saveType,
                        "uaccountname": saveName,
                        "ubankname": saveBank,
                        "ubankno": saveCard,
                        "amount": saveMoney
                    };

                    if (_nextFlag == false) {

                        mobileManage.getLoader().open('处理中');
                        _fast.$btnFast.attr("disabled", true);

                        if (saveType == 4) {

                            var quotaUrl = "";

                            if (_TEST_MODE == "1") {
                                quotaUrl = "/data/deposit/getWxZzQuota.json";
                            } else {
                                quotaUrl = "/asp/getWxZzQuota.aspx";
                            }

                            $.post(quotaUrl, {'amount': saveMoney}, function (response) {
                                var amount = response;
                                if (amount > 0) {
                                    $("#real-save-money").html(amount);
                                    $(".save-money").html(amount);
                                } else {
                                    alert("获取信息错误!");
                                    _fast.$btnFast.attr("disabled", false);
                                    return false;
                                }
                            }).fail(function () {
                                alert("获取信息错误!");
                                _fast.$btnFast.attr("disabled", false);
                                return false;
                            });
                        }

                        var newDepositUrl = "";

                        if (_TEST_MODE == 1) {
                            newDepositUrl = "/data/deposit/newDeposit1.json";
                        } else {
                            newDepositUrl = "/asp/getNewdeposit.aspx";
                        }

                        $.ajax({
                            type: 'POST',
                            url: newDepositUrl,
                            dataType: "json",
                            data: formData,
                            error: function (response) {
                                alert(response);
                            },
                            success: function (response) {
                                mobileManage.getLoader().close();

                                var massage = response.massage;
                                if (!massage) {
                                    _creatDepositInfo(response);

                                } else {
                                    if (response['force'] === true) {// 有订单
                                        var ret = confirm('您有订单未完成支付，请您点击确定作废之前订单在建立新订单进行付款!!');
                                        if (ret) {
                                            formData['force'] = true;
                                            mobileManage.getLoader().open('处理中');
                                            $.post(newDepositUrl, formData, function (response) {
                                                mobileManage.getLoader().close();
                                                if (!response.massage) {
                                                    _creatDepositInfo(response);
                                                } else {
                                                    alert(massage);
                                                }

                                            }).fail(function () {
                                                mobileManage.getLoader().close();
                                                alert('获取信息异常');
                                            });
                                        } else {
                                            mobileManage.getLoader().close();
                                        }
                                    } else {
                                        alert(massage);
                                    }
                                }


                            },
                            fail: function (response) {
                                alert("维护中");
                            }
                        });

                    }
                }
            });

            function _creatDepositInfo(jsonData) {
                $("#deposit-fast-submit").hide().attr("disabled", "disabled");
                _nextFlag = true;

                var massage = jsonData.massage;
                if (massage == null || massage == '' || massage == undefined) {

                    if (saveType == 4) {
                        $("#deposit-fast-page-3").show().siblings().hide();
                        $(".fast-wexin-chk").show();
                        $("#fyan").hide();
                    } else {
                        $("#deposit-fast-page-2").show().siblings().hide();
                        $(".fast-wexin-chk").hide();
                        $("#fyan").show();
                    }

                    quickSaveData = {
                        "type": $("#m-deposit-fast-type").val(),
                        "type_cn": $("#m-deposit-fast-type option:selected").text(),
                        "username": $("#deposit-fast-name").val(),
                        "bank": $("#m-deposit-fast-bank").val(),
                        "card": $("#deposit-fast-card").val(),
                        "money": $("#deposit-fast-money").val(),
                        "depositCode": $("#quick-save-depositCode").val()
                    };

                    $("#quick-confirm-type").val(quickSaveData["type_cn"]);
                    $("#quick-confirm-money").val(jsonData["amount"]);
                    $("#quick-confirm-username").val(quickSaveData["username"]);
                    $("#quick-confirm-bank").val(quickSaveData["bank"]);
                    $("#quick-confirm-card").val(quickSaveData["card"]);


                    $("#sbankname").val(jsonData.bankname);
                    $("#saccountno").val(jsonData.accountno);
                    $("#saccountname").val(jsonData.username);
                    $("#mefuyan").val(jsonData.zfbImgCode);

                    _nextFlag = false;

                }
                else {
                    $("#deposit-fast-page-1").show().siblings().hide();

                    alert(massage);
                }
            };

            $("#deposit-fast-success").click(function (e) {
                e.preventDefault();
                window.location.reload();
            });

            $(".fast-next").click(function (e) {
                if ($('#must-checked').prop('checked') && $('#must-checked1').prop('checked')) {
                    $("#deposit-fast-page-2").show().siblings().hide();
                } else {
                    alert('请同意天威存款条例！');
                }
            });

            $(".fast-wexin-chk").click(function (e) {
                $("#wechat-desc-page").show("").siblings().hide("");
            });

            alert('温馨提示:\n1.请在每次存款时刷新获取最新收款账户名，避免直接存款，若没有获取最新存款账户名导致存入关闭账户，则无法处理您的款项\n2.每天23:00-01:00 为招行维护时间，此时间段内存款将会延迟到帐，如有问题请及时联系在线客服');
        }

        //快捷存款
        function _SpeedPayManage() {
            var _$money = $('#deposit-speedPay-money');
            $('#deposit-speedPay-submit').click(_doPay);

            $("#m-deposit-speedpay").change(function () {

                var $e = $('#m-deposit-speedpay option:selected');
                var platform = $e.data("platform");
                var id = $e.val();

                if (platform.indexOf("mbkj") > -1) {
                    $(".mbkj").removeClass("hidden");
                } else {
                    $(".mbkj").addClass("hidden");
                }

                if (platform.indexOf("mifkj") > -1) {
                    $(".mifkj").removeClass("hidden");
                    getPayBank(id, "#mifkj-bankname");
                } else {
                    $(".mifkj").addClass("hidden");
                }

                if (platform.indexOf("dbkj") > -1) {
                    $(".dbkj").removeClass("hidden");
                    getPayBank(id, "#dbkj-bankname");
                } else {
                    $(".dbkj").addClass("hidden");
                }

            });

            function _doPay() {

                var $e = $('#m-deposit-speedpay option:selected');
                var platform = $e.data("platform");

                var formData = {
                    platformId: $e.attr('value'),
                    orderAmount: _$money.val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $e.data('url'),
                    usetype: 1
                };

                if (platform == "mbkj") {
                    var $bankcard = $("#speedpay-bankcard").val();
                    var $bankname = $("#speedpay-bankname").val();
                    var $phoneNumber = $("#speedpay-phoneNumber").val();

                    if ($bankcard == "") {
                        alert("[提示]请填写银行卡!");
                        return false;
                    } else if ($bankname == "") {
                        alert("[提示]请填写银行卡户名!");
                        return false;

                    } else if ($phoneNumber == "") {
                        alert("[提示]请填写手机号!");
                        return false;
                    }


                    formData.bankcard = $bankcard;
                    formData.bankname = $bankname;
                    formData.phoneNumber = $phoneNumber;
                }

                if (platform.indexOf("mifkj") > -1) {

                    var $bankcard = $("#mifkj-bankcard").val();
                    var $bankname = $("#mifkj-bankname option:selected").val();

                    if ($bankcard == "") {
                        alert("[提示]请填写银行卡!");
                        return false;
                    } else if ($bankname == "") {
                        alert("[提示]请填写银行卡户名!");
                        return false;
                    }

                    formData.bankcard = $bankcard;
                    formData.bankname = $bankname;
                }

                if (platform.indexOf("dbkj") > -1) {

                    var $bankname = $("#dbkj-bankname option:selected").val();

                    if ($bankname == "") {
                        alert("[提示]请填写银行卡户名!");
                        return false;
                    }

                    formData.bankCode = $bankname;
                }


                if (_TEST_MODE == 1) {
                    console.log(formData)
                } else {
                    mobileManage.getTPPManage().payTo(formData);
                }
            }
        }

        //第三方支付Manage
        function _ThirdPayManage() {
            var _thridPay = this;

            //第三方支付
            function _doThirdPay() {
                if ($("#m-deposit-thirdPay-bank").val() == '') {
                    alert('请选择银行');
                    return;
                }

                var formData = {
                    platformId: $('#m-deposit-thirdPay-pay').val(),
                    orderAmount: $('#deposit-thirdPay-money').val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $("#m-deposit-thirdPay-pay").find("option:selected").data('url'),
                    bankCode: $("#m-deposit-thirdPay-bank").val(),
                    usetype: 1
                };

                mobileManage.getTPPManage().payTo(formData);
            }

            $('#deposit-thirdPay-submit').click(_doThirdPay);
        }

        //支付宝
        function _ZFBQR1Manage() {
            var that = this;
            that.account = $('#deposit-zfbQR1-account');
            that.image = $('#deposit-zfbQR1-image');
            that.button = $('#deposit-zfbQR1-bind');
            that.button.click(function () {
                mobileManage.getModel().open('zfbBind');
            });

            //查询支付宝绑定
            mobileManage.getLoader().open('载入中');
            mobileManage.getBankManage().getAlipayAccount(function (result) {
                if (result.success) {
                    if (result.data.bind) {
                        that.account.val(result.data.account);
                        if (result.data.auth) {
                            that.image.append('<img style="width:200px;" src="' + result.data.image + '"/>');
                        } else {
                            that.image.append('<div style="color:red;text-align:left;padding:5px;">' + result.message + '</div>');
                        }
                        that.button.html('修改绑定');
                    } else {
                        that.button.html('绑定');
                    }
                } else {
                    alert(result.message)
                }
                mobileManage.getLoader().close();
            });
        }

        //支付宝在线支付
        function _ZFBQR2Manage() {
            var _zfbqr2 = this;
            _zfbqr2.$money = $('#deposit-zfbQR2-money');
            _zfbqr2.$submit = $('#deposit-zfbQR2-submit');
            _zfbqr2.$submit.click(_submit);
            //支付宝
            $('#m-deposit-zfbQR2-pay').change(function () {
                var fee = $('#j-zfbFee');
                var max = $('#j-zfbMax');
                var min = $('#j-zfbMin');
                fee.html($("#m-deposit-zfbQR2-pay").find("option:selected").data('fee'))
                max.html($("#m-deposit-zfbQR2-pay").find("option:selected").data('max'))
                min.html($("#m-deposit-zfbQR2-pay").find("option:selected").data('min'))
            });

            function _submit() {
                var formData = {
                    platformId: $('#m-deposit-zfbQR2-pay').val(),
                    orderAmount: $('#deposit-zfbQR2-money').val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $("#m-deposit-zfbQR2-pay").find("option:selected").data('url'),
                    usetype: 1
                };

                mobileManage.getTPPManage().payTo(formData);
            }
        }

        //久安支付
        function _JIUANManage() {

            var _weixin = this;
            var _$money = $('#deposit-jiuan-money');

            $('#deposit-jiuan-submit').click(_doPay);

            //微信
            $('#m-deposit-jiuan-pay').change(function () {
                var fee = $('#j-jiuanFee');
                var max = $('#j-jiuanMax');
                var min = $('#j-jiuanMin');
                fee.html($("#m-deposit-jiuan-pay").find("option:selected").data('fee'))
                max.html($("#m-deposit-jiuan-pay").find("option:selected").data('max'))
                min.html($("#m-deposit-jiuan-pay").find("option:selected").data('min'))
            });

            function _doPay() {
                var formData = {
                    platformId: $('#m-deposit-jiuan-pay').val(),
                    orderAmount: $('#deposit-jiuan-money').val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $("#m-deposit-jiuan-pay").find("option:selected").data('url'),
                    usetype: 1
                };

                mobileManage.getTPPManage().payTo(formData);
            }
        }

        //ＱＱManage
        function _QQManage() {

            var _weixin = this;
            var _$money = $('#deposit-QQ-money');

            $('#deposit-QQ-submit').click(_doPay);

            //微信
            $('#m-deposit-QQ-pay').change(function () {
                var fee = $('#j-qqFee');
                var max = $('#j-qqMax');
                var min = $('#j-qqMin');
                fee.html($("#m-deposit-QQ-pay").find("option:selected").data('fee'))
                max.html($("#m-deposit-QQ-pay").find("option:selected").data('max'))
                min.html($("#m-deposit-QQ-pay").find("option:selected").data('min'))
            });

            function _doPay() {
                var formData = {
                    platformId: $('#m-deposit-QQ-pay').val(),
                    orderAmount: $('#deposit-QQ-money').val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $("#m-deposit-QQ-pay").find("option:selected").data('url'),
                    usetype: 1
                };

                mobileManage.getTPPManage().payTo(formData);
            }
        }

        //京东支付
        function _JDManage() {

            var _weixin = this;
            var _$money = $('#deposit-jd-money');

            $('#deposit-jd-submit').click(_doPay);

            //微信
            $('#m-deposit-jd-pay').change(function () {
                var fee = $('#j-jdFee');
                var max = $('#j-jdMax');
                var min = $('#j-jdMin');
                fee.html($("#m-deposit-jd-pay").find("option:selected").data('fee'))
                max.html($("#m-deposit-jd-pay").find("option:selected").data('max'))
                min.html($("#m-deposit-jd-pay").find("option:selected").data('min'))
            });

            function _doPay() {
                var formData = {
                    platformId: $('#m-deposit-jd-pay').val(),
                    orderAmount: $('#deposit-jd-money').val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $("#m-deposit-jd-pay").find("option:selected").data('url'),
                    usetype: 1
                };

                mobileManage.getTPPManage().payTo(formData);
            }
        }

        //微信Manage
        function _WeixinManage() {
            var _weixin = this;
            var _$money = $('#deposit-weixin-money');

            $('#deposit-weixin-submit').click(_doPay);

            //微信
            $('#m-deposit-weixin-pay').change(function () {
                var fee = $('#j-weixinFee');
                var max = $('#j-weixinMax');
                var min = $('#j-weixinMin');
                fee.html($("#m-deposit-weixin-pay").find("option:selected").data('fee'))
                max.html($("#m-deposit-weixin-pay").find("option:selected").data('max'))
                min.html($("#m-deposit-weixin-pay").find("option:selected").data('min'))
            });

            function _doPay() {
                var formData = {
                    platformId: $('#m-deposit-weixin-pay').val(),
                    orderAmount: $('#deposit-weixin-money').val(),
                    loginName: '${session.customer.loginname}',
                    payUrl: $("#m-deposit-weixin-pay").find("option:selected").data('url'),
                    usetype: 1
                };

                mobileManage.getTPPManage().payTo(formData);
            }
        }

        //点卡支付
        function _DCardManage() {
            var _dCard = this;
            var _$form = $('#deposit-card-form');
            var _cardCredit = {};


            /**
             * 初始化
             */
            function _init() {
                _dCard.$cardNo = $('#deposit-card-no');
                _dCard.$cardPassword = $('#deposit-card-password');

                //点卡类型
                _dCard.cardCode = new MobileComboBox({
                    appendId: 'deposit-card-code',
                    cls: '',
                    valueName: 'value',
                    displayName: 'name',
                    datas: [
                        {"value": "", "name": "请选择点卡"}],
                    onChange: function (e) {
                        _dCard.$cardNo.val('');
                        _dCard.$cardPassword.val('');

                        var arr = _cardCredit[_dCard.cardCode.getValue()];
                        var brr = arr.split(",");
                        var html = '<option value="">请选择</option>';
                        for (var i = 0; i < brr.length; i++) {
                            html += '<option value="' + brr[i] + '">' + brr[i] + '</option>'
                        }
                        $("#m-deposit-card-money").html(html);
                    }
                });

                _dCard.cardCredit = new MobileComboBox({
                    appendId: 'deposit-card-money',
                    cls: '',
                    valueName: 'value',
                    displayName: 'name',
                    datas: [{"value": "", "name": "请选择"}],
                    onChange: function (e) {
                    }
                });

                $('#deposit-card-submit').click(_dCardPay);
                _queryPayBank();
            }

            /**
             * 查询点卡
             */
            function _queryPayBank() {
                var _platformId = $("#deposit-card").data('id');
                $.ajax({
                    method: 'POST',
                    url: "${ctx}/asp/pay_bank.aspx",
                    data: {platformId: _platformId},
                    success: function (response) {
                        var jsonData = $.parseJSON(response);
                        _queryCardCredit(jsonData);
                    }
                });

//                    var jsonData = '{"code":"10000","desc":"成功","data":[{"dictName":"100011","dictValue":"0.13","dictShow":"5,10,20,15,30,50","dictDesc":"网易一卡通"},{"dictName":"100010","dictValue":"0.06","dictShow":"20,30,50,100,300,500","dictDesc":"联通充值卡"},{"dictName":"100009","dictValue":"0.12","dictShow":"15,30,50,100","dictDesc":"完美一卡通"},{"dictName":"100008","dictValue":"0.06","dictShow":"50,100","dictDesc":"中国电信卡"},{"dictName":"100007","dictValue":"0.13","dictShow":"5,10,15,30,60,100,200","dictDesc":"腾讯Q币卡"},{"dictName":"100006","dictValue":"0.16","dictShow":"5,10,15,20,25,30,50,100","dictDesc":"久游一卡通"},{"dictName":"100005","dictValue":"0.16","dictShow":"5,6,9,10,14,15,20,30,50,100,200,300,500,1000","dictDesc":"骏网一卡通"},{"dictName":"100004","dictValue":"0.13","dictShow":"5,10,15,25,30,35,45,50,100,300,350,1000","dictDesc":"盛大一卡通"},{"dictName":"100003","dictValue":"0.13","dictShow":"10,15,20,25,30,50,60,100,300,468,500","dictDesc":"征途游戏卡"},{"dictName":"100002","dictValue":"0.05","dictShow":"10,20,30,50,100,300,500","dictDesc":"神州行充值卡"},{"dictName":"100001","dictValue":"0.16","dictShow":"10,15,30,50,100","dictDesc":"纵游一卡通"}]}';
//                    jsonData = JSON.parse(data);
//                    _queryCardCredit(jsonData);
            }

            /**
             * 查询点卡面额
             */
            function _queryCardCredit(jsonData) {
                var html = "";
                var descHtml = "<tr><th colspan='2'>点卡支付业务费率</th></tr><tr><th>点卡类型</th><th>费率</th></tr>";
                var $cardTable = $("#cardTable");

                $.each(jsonData.data, function (index, vo) {
                    _cardCredit[vo.dictName] = vo.dictShow;
                    html += "<option value=\"" + vo.dictName + "\">" + vo.dictDesc + "</option>";
                    descHtml += "<tr><td>" + vo.dictDesc + "</td><td>" + vo.dictValue + "</td></tr>";
                });

                $cardTable.html(descHtml);
                $("#m-deposit-card-code").append(html);
            }

            /**
             * 检查点卡支付是否开启
             */
            function _chekcWork() {
                mobileManage.getLoader().open("处理中");
                mobileManage.getTPPManage().payＷay(function (result) {
                    mobileManage.getLoader().close();
                    if (result.data) {
                        _$form.replaceWith([
                            '<div class="mui-col-xs32-11 ml4">',
                            '	<span class="space-1"></span>',
                            '	<div class="mui-select iconfont">',
                            '		<label>点卡类型</label>',
                            '		<div id="deposit-card-code"></div>',
                            '	</div>',
                            '		<div class="message">提示：可选择点卡支付</div>',
                            '	<span class="space-1"></span>',
                            '	<div class="mui-select iconfont">',
                            '		<label>存款额度</label>',
                            '		<div id="deposit-card-money"></div>',
                            '	</div>',
                            '	<span class="space-1"></span>',
                            '	<div class="testfueld">',
                            '		<label>点卡卡号</label>',
                            '		<input id="deposit-card-no" type="text" >',
                            '		</div>',
                            '	<span class="space-1"></span>',
                            '	<div class="testfueld">',
                            '		<label>点卡密码</label>',
                            '		<input id="deposit-card-password" type="password" >',
                            '		</div>',
                            '	<div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-card-submit">确定支付</div>',
                            '</div>'].join(''));

                        //初始化物件
                        _init();
                    } else {
                        _$form.html(result.message);
                    }
                });
            }

            /**
             * 点卡支付
             */
            function _dCardPay() {
                var formData = {
                    platformId: $('#deposit-card').data('id'),
                    orderAmount: _dCard.cardCredit.getValue(),
                    usetype: 1,
                    loginName: '${session.customer.loginname}',
                    cardCode: _dCard.cardCode.getValue(),
                    cardNo: _dCard.$cardNo.val(),
                    cardPassword: _dCard.$cardPassword.val(),
                    payUrl: $('#deposit-card').data('url')
                };


                mobileManage.getTPPManage().payCardTo(formData);

                /*
                 var formData = {
                 card_code: _dCard.cardCode.getValue(),
                 card_no: _dCard.$cardNo.val(),
                 card_password: _dCard.$cardPassword.val(),
                 money: _dCard.cardCredit.getValue()
                 };
                 mobileManage.getLoader().open("处理中");
                 mobileManage.getTPPManage().dCardPay(formData, function (result) {
                 mobileManage.getLoader().close();
                 alert(result.message);
                 if (result.success) {
                 _dCard.$cardNo.val('');
                 _dCard.$cardPassword.val('');
                 _dCard.cardCredit.setValue('');
                 }
                 });*/

            }

            _chekcWork();
        }

    }

    function getPayBank(id, target) {
        if (_TEST_MODE == 0) {
            var payWayUrl = "/asp/pay_bank.aspx";
        } else {
            var payWayUrl = "/data/deposit/payBank.json";
        }

        $.post(payWayUrl, {platformId: id}, function (result) {

            if (_TEST_MODE == 0) {
                result = $.parseJSON(result)
            }
            if (result.desc == '成功') {
                var str = '<option value="">请选择</option>';

                for (var i in result.data) {
                    str += '<option value="' + result.data[i].dictValue + '">' + result.data[i].dictName + '</option>'
                }

                $(target).html(str);
            }
        });
    }

    //验证输入input
    function clearNoNum(_value) {
        // 事件中进行完整字符串检测
        var patt = /^((?!0)\d+\.[0-9][1-9])$/g;
        if (!patt.test(_value)) {
            // 错误提示相关代码，边框变红、气泡提示什么的
            alert("输入金额格式错误");
            return true;
        }
        return false;
    }


    function mykeypress(obj, e) {
        // 在 keyup 事件中拦截错误输入
        var keynum;
        if (window.event) { // IE
            keynum = e.keyCode;
        } else if (e.which) { // Netscape/Firefox/Opera
            keynum = e.which;
        }
        var sCharCode = String.fromCharCode(keynum);
        var sValue = obj.value;
        if (/[^0-9.]/g.test(sCharCode) || __getRegex(sCharCode).test(sValue)) {
            return false;
        }

        /**
         * 根据用户输入的字符获取相关的正则表达式
         * @param  {string} sCharCode 用户输入的字符，如 'a'，'1'，'.' 等等
         * @return {regexp} patt 正则表达式
         */
        function __getRegex(sCharCode) {
            var patt;
            if (/[0]/g.test(sCharCode)) {
                // 判断是否为空
                patt = /^$/g;
            } else if (/[.]/g.test(sCharCode)) {
                // 判断是否已经包含 . 字符或者为空
                patt = /((\.)|(^$))/g;
            } else if (/[0-9]/g.test(sCharCode)) {
                // 判断是否已经到达小数点后两位
                patt = /\.\d{2}$/g;
            }
            return patt;
        }
    }

    function checkWXZFBMoney(money) {
        var rex = /\d+[.]\d{2}$/g;
        if (rex.test(money)) return true;
        else return false;
    }

    <%--app版提示 ios webview内不支持alert--%>
    var tipsBox2 = $('.j-alert3');
    var tipsTxt = $('.my-alert-con');

    function showTips(text) {
        tipsBox2.css('display', 'block')
        tipsTxt.html(text);
    }

    function closeTips() {
        tipsBox.css('display', 'none');
        tipsTxt.html('');
    }

    function DepositPage2() {

        //设定只能输入数字
        NumberInput('deposit-thirdPay-money');
        //取得上次存款模式
        var depositMode = mobileManage.getSessionStorage('fundsManage').depositMode || '0';

        var _$view = $('.deposit-content');
        var _deposit = {};

        var _$titles = $('ul>li>.title');

        _$titles.bind('click', _titleClickEvent);

        function _titleClickEvent() {

            var _$titles = $(this);
            if (_$titles.parent().hasClass('active')) {
                _$titles.parent().removeClass('active');
            } else {
                //关闭其他打开的
                _$view.find('ul>li.active').removeClass('active');
                _$titles.parent().addClass('active');
            }

            if (!_deposit[this.id]) {
                switch (this.id) {
                    case 'deposit-zfbQR1':
                        _deposit[this.id] = new _ZFBQR1Manage();
                        break;
                    case 'deposit-fast':
                        _deposit[this.id] = new _FastManage();
                        break;
                    case 'deposit-thirdPay':
                        _deposit[this.id] = new _ThirdPayManage();
                        break;
                    case 'deposit-weixin':
                        _deposit[this.id] = new _WeixinManage();
                        break;
                    case 'deposit-card':
                        _deposit[this.id] = new _DCardManage();
                        break;
                    case 'deposit-zfbRemark':
                        _deposit[this.id] = new _ZFBRemarkManage();
                        break;
                    case 'deposit-zfbQR2':
                        _deposit[this.id] = new _ZFBQR2Manage();
                        break;
                    case 'deposit-speedPay':
                        _deposit[this.id] = new _SpeedPayManage();
                        break;
                    case 'deposit-WXValidate':
                        _deposit[this.id] = new _WXValidateManage();
                        break;
                    default:
                }
            }
        }
    }

    _init();


    /**
     * 初始化
     */
    function _init() {
        mobileManage.getLoader().open('初始化');
        mobileManage.getTPPManage().getAllPayments(function (result) {
            mobileManage.getLoader().close();
            if (!result.success) return;

            filterPayPage(result.data);

        });
    }

    /**
     * 支付方式开关
     */
    function filterPayPage(data) {
        /**
         * 支付方式开关
         */
        var payPageArr = {
            //	 				'deposit-zfbQR1':['汇潮'],
            //	 				'deposit-zfbRemark':['汇潮'],
            //	 				'deposit-thirdPay':['汇潮'],
            'deposit-WXValidate': ['微信额度验证'],
            //					'deposit-WXValidate':[],
            'deposit-zfbQR2': ['讯付通支付宝', '口袋支付', '聚宝支付宝', '优付支付宝', '新贝支付宝', '银宝支付宝', '千网支付宝', '口袋支付宝2', '口袋支付宝3', '迅联宝支付宝', '金海哲alipay', '多宝支付宝'],
            'deposit-weixin': ['讯付通微信', '多宝微信', '口袋微信支付', '口袋微信支付2', '口袋微信支付3', '新贝微信', '乐富微信', '智付微信', '智付微信1', '口袋支付', '口袋微信支付2', '迅联宝', '汇付宝微信', '优付微信', '千网微信'],
            'deposit-card': ['智付点卡2'],
            'deposit-speedPay': ['汇潮']
        };

        var open = {};
        for (var i in payPageArr) {
            open[i] = false;
            for (var j in data) {
                if (payPageArr[i].indexOf(data[j]) != -1) {
                    open[i] = true;
                    break;
                }
            }
        }
//	//移除没有使用的
//	for(var i in open){
//	if(!open[i]&&_$view.find('#'+i).length>0){
//	_$view.find('#'+i).parent().remove();
//	}
//	}
    }

    /*
     *极速转帐Manage
     */
    function _FastManage() {

        var _TEST_MODE = 0;

        var _fast = this;
        var _nextFlag = false;
        var _bankInfos;

        var saveType = "";
        var quickSaveData = "";
        //设置默认选中的方式为 支付宝
        $("#m-deposit-fast-type").val(2);
        $("#card").hide();
        $("#selectpay").hide();

        // 存款方式
        _fast.fastBank = new MobileComboBox({
            appendId: 'deposit-fast-type',
            cls: '',
            valueName: 'value',
            displayName: 'name',
            datas: [
                {value: '2', name: '支付宝转账', bankType: '支付宝转账'},
                <%--{value: '', name: '请选择', bankType: '请选择'},--%>
                {value: '1', name: '网上银行转账', bankType: '网上银行转账'},
                {value: '0', name: '手机银行转账', bankType: '手机银行转账'},
                {value: '4', name: '微信转账', bankType: '微信转账'}

            ],
            onChange: function (e) {
                var $that = $("#m-deposit-fast-type");
                $that.show();
                var value = $that.find("option:selected").val();

                if (value == "2") {
                    $("#card").hide();
                    $("#selectpay").hide();
                    $(".show-for-none").hide();
                    $(".show-for-alipay").show();
                    $(".hide-for-alipay").hide();
                    $('#deposit-fast-name').attr('placeholder', '实际转账的支付宝姓名');
                } else {
                    $('#deposit-fast-name').attr('placeholder', '请输入姓名');

                    $(".show-for-none").hide();
                    $(".hide-for-alipay").show();
                    $(".show-for-alipay").hide();
                }
            }
        });

        $("#deposit-fast-submit").click(function (e) {
            e.preventDefault();
            var msg = "";
            saveType = $("#m-deposit-fast-type").val();
            var saveName = $("#deposit-fast-name").val();
            var saveCard = $("#deposit-fast-card").val();
            var saveBank = $("#m-deposit-fast-bank").find(":selected").val();
            var saveMoney = $("#deposit-fast-money").val();

            if (msg == "" && !saveType || saveType == "") {
                msg = "[提示]请选择存款方式!";
            }

            if (msg == "" && !saveName || saveName == "") {
                msg = "[提示]请填写您的存款姓名!";
            }

            var reg = /^[1-9]\d*(\.\d{1,2})?$/;
            if (saveName == '' || saveName == null || !saveName) {
                msg = "[提示]请填写您的存款姓名!";
                alert(msg);
                return false;
            }
            if (saveMoney == '' || saveMoney == null || !saveMoney) {
                msg = "[提示]请选择存款金额!";
                alert(msg);
                return false;
            }
            if (!reg.test(saveMoney)) {
                msg = "[提示]存款金额不得包含汉字或符号!";
                alert(msg);
                return false;
            }

            mobileManage.ajax({
                url: 'mobi/systemConfig.aspx',
                param: {
                    typeNo: 'type888',
                    itemNo: '001',
                    systemConfigFlag: '是'
                },
                callback: function (result) {
                    if (result.success) {
                        var array = result.message.split('#');
                        if (saveType == 2 && array[1].indexOf('临时维护关闭') > -1 || saveType == 2 && array[0].indexOf('临时维护关闭') > -1 || saveType == 2 && array[0].indexOf('临时维护') > -1 || saveType == 2 && array[0].indexOf('关闭') > -1) {
                            mobileManage.getModel().open('goOrDownload', [{
                                title: array[0],
                                content: [
                                    array[1]
                                ].join(''),
                                goGameText: '关闭',
                                downloadText: '<span class="deleter-btn">选择其他方式</span>',
                                goDownloadFn: function (e, model) {
                                    //选择其他方式 按钮
                                    mobileManage.getModel().close();
                                    window.location.reload();
                                },
                                goGameFn: function (e, model) {
                                    //关闭
                                    mobileManage.getModel().close();
                                    return false;
                                }
                            }]);
                        } else {
                            mobileManage.getModel().open('goOrDownload', [{
                                title: array[0],
                                content: [
                                    array[1]
                                ].join(''),
                                goGameText: '继续支付',
                                downloadText: '<span class="deleter-btn">选择其他方式</span>',
                                goDownloadFn: function (e, model) {
                                    //选择其他方式 按钮
                                    mobileManage.getModel().close();
                                    window.location.reload();
                                },
                                goGameFn: function (e, model) {
                                    //继续支付
                                    mobileManage.getModel().close();
                                    nextStep(e)
                                }
                            }]);
                        }
                    } else {
                        nextStep(e)
                    }
                }
            });


        });

        function nextStep(e) {
            e.preventDefault();
            document.documentElement.scrollTop = 0;
            document.body.scrollTop = 0;
            $(".show-for-none").show();
            $(".hide-for-alipay").hide();
            $(".show-for-alipay").hide();
            var msg = "";
            saveType = $("#m-deposit-fast-type").val();
            var saveName = $("#deposit-fast-name").val();
            var saveCard = $("#deposit-fast-card").val();
            var saveBank = $("#m-deposit-fast-bank").find(":selected").val();
            var saveMoney = $("#deposit-fast-money").val();

            if (msg == "" && !saveType || saveType == "") {
                msg = "[提示]请选择存款方式!";
            }

            if (msg == "" && !saveName || saveName == "") {
                msg = "[提示]请填写您的存款姓名!";
            }

            var reg = /^[1-9]\d*$/;
            if (saveName == '' || saveName == null || !saveName) {
                msg = "[提示]请填写您的存款姓名!";
                alert(msg);
                return false;
            }
            if (saveMoney == '' || saveMoney == null || !saveMoney) {
                msg = "[提示]请选择存款金额!";
                alert(msg);
                return false;
            }
            if (!reg.test(saveMoney)) {
                msg = "[提示]请输入正确的金额!";
                alert(msg);
                return false;
            }
            if (!reg.test(saveMoney)) {
                msg = "[提示]存款金额不得包含汉字或符号!";
            }

            if (msg == "" && !saveMoney || saveMoney == "") {
                msg = "[提示]请选择存款金额!";
            }


            if (msg != "") {
                alert(msg);
                return false;
            }
            else {

                if (saveType == 2) {
                    saveBank = "支付宝";
                }

                if (saveBank == "请选择") {
                    saveBank = "";
                }

                var jsonData = "";
                var formData = {
                    "banktype": saveType,
                    "uaccountname": saveName,
                    "ubankname": saveBank,
                    "ubankno": saveCard,
                    "amount": saveMoney
                };
                if (_nextFlag == false) {
                    mobileManage.getLoader().open('处理中');

                    var newDepositUrl = "";

                    if (_TEST_MODE == 1) {
                        newDepositUrl = "/data/deposit/newDeposit1.json";
                    } else {
                        newDepositUrl = "/asp/getNewdeposit.aspx";
                    }

                    $.ajax({
                        type: 'POST',
                        url: newDepositUrl,
                        data: formData,
                        error: function (response) {
                            alert(response);
                        },
                        success: function (response) {
                            mobileManage.getLoader().close();
                            var massage = response.massage;
                            if (!massage) {
                                _creatDepositInfo(response);

                            } else {
                                if (response['force'] === true) {
                                    <%--showConfirmbox('您有订单未完成支付，请您先核实。若未支付，需要重新建立订单，请先作废之前订单!',response);--%>
                                    mobileManage.getModel().open('goOrDownload', [{
                                        title: '温馨提示',
                                        content: [
                                            '<p>您有订单未完成支付，请您先核实。若未支付，需要重新建立订单，请先作废之前订单!</p>',
                                            '<p>请您按照您输入的存款信息进行存款，方可实时到账！！！</p>'
                                        ].join(''),
                                        goGameText: '<span class="deleter-btn">作废订单</span>',
                                        downloadText: '取消',
                                        goDownloadFn: function (e, model) {
                                            //取消按钮
                                            mobileManage.getModel().close();
                                        },
                                        goGameFn: function (e, model) {
                                            formData['force'] = true;
                                            mobileManage.getLoader().open('处理中');
                                            $.post(newDepositUrl, formData, function (response) {
                                                mobileManage.getLoader().close();
                                                mobileManage.getModel().close();
                                                if (!response.massage) {
                                                    _creatDepositInfo(response);
                                                } else {
                                                    showTips(massage);
                                                }
                                            }).fail(function () {
                                                mobileManage.getLoader().close();
                                                showTips('获取信息异常');
                                            });
                                        }
                                    }]);

                                }
                                else {
                                    alert(massage);
                                    mobileManage.getLoader().close();
                                    return;
                                }

                            }
                        },
                        fail: function (response) {
                            alert("维护中");
                        }
                    });
                }

            }
        }

        function _creatDepositInfo(response) {
            $("#deposit-fast-submit").hide().attr("disabled", "disabled");
            _nextFlag = true;
            var massage = response.massage;
            if (massage == null || massage == '' || massage == undefined) {

                $("#deposit-fast-page-1").hide();
                $("#deposit-fast-page-2").show();

                quickSaveData = {
                    "type": $("#m-deposit-fast-type").val(),
                    "type_cn": $("#m-deposit-fast-type option:selected").text(),
                    "username": $("#deposit-fast-name").val(),
                    "bank": $("#m-deposit-fast-bank").val(),
                    "card": $("#deposit-fast-card").val(),
                    "money": $("#deposit-fast-money").val(),
                    "depositCode": $("#quick-save-depositCode").val()
                };

                $("#quick-confirm-type").val(quickSaveData["type_cn"]);
                $("#quick-confirm-money").val(response["amount"]);
                $("#quick-confirm-username").val(quickSaveData["username"]);
                $("#quick-confirm-bank").val(quickSaveData["bank"]);
                $("#quick-confirm-card").val(quickSaveData["card"]);

                $("#sbankname").val(response.bankname);
                $("#saccountno").val(response.accountno);
                $("#saccountname").val(response.username);
                $("#mefuyan").val(response.depositId);

                _nextFlag = false;
            }
            else {
                $("#deposit-fast-page-1").show().siblings().hide();
                alert(massage);
            }

        }


        $("#deposit-fast-success,#bank-back-all").click(function (e) {
            e.preventDefault();
            window.location.reload();
        });
    }


    //第三方支付Manage
    function _ThirdPayManage() {
        var _thridPay = this;

        //支付方式
        _thridPay.thirdPayPay = new MobileComboBox({
            appendId: 'deposit-thirdPay-pay',
            cls: '',
            valueName: 'value',
            displayName: 'name',
            datas: [{value: '', name: '请选择'}],
            onChange: function (e) {
                if ("智付点卡" == _thridPay.thirdPayPay.getValue()) {
                    $('#m-deposit-thirdPay-bank').next().html('点卡种类');
                    $('#deposit-point-content-1').hide();
                    $('#deposit-point-content-2').show();
                } else {
                    $('#m-deposit-thirdPay-bank').next().html('银行种类');
                    $('#deposit-point-content-2').hide();
                    $('#deposit-point-content-1').show();
                }
                _queryPayBank();
            }
        });

        //银行
        _thridPay.thirdPayBank = new MobileComboBox({
            appendId: 'deposit-thirdPay-bank',
            cls: '',
            valueName: 'value',
            displayName: 'name',
            datas: [{value: '', name: '请选择'}],
            onChange: function (e) {

            }
        });

        //查詢支付方式下拉資料
//	function queryPay(){
//	var filterPay = ['汇潮'];
//	mobileManage.getLoader().open('载入中');
//	mobileManage.getTPPManage().queryPayDatas(function(result){
//	if(result.success){
//	var data = [{name: "请选择", value: ""}];
//	for(var i in result.data){
//	if(filterPay.indexOf(result.data[i].value)==-1){
//	data.push(result.data[i]);
//	}
//	}
//	_thridPay.thirdPayPay.loadData(data);
//	_thridPay.thirdPayPay.setValue(_thridPay.thirdPayPay.getValue());
//	}else{
//	alert(result.message);
//	}
//	mobileManage.getLoader().close();
//	});
//	}


        function _doThirdPay() {
            var formData = {
                payId: _thridPay.thirdPayPay.getValue(),
                money: $('#deposit-thirdPay-money').val(),
                bankName: _thridPay.thirdPayBank.getValue()
            };
            mobileManage.getLoader().open("处理中");
            mobileManage.getTPPManage().pay(formData, function (result) {
                if (result.success) {

                } else {
                    alert(result.message);
                }
                mobileManage.getLoader().close();
            });
        }


        //取得银行
//	function _queryPayBank(){
//	mobileManage.getTPPManage().queryBankDataByPayId(_thridPay.thirdPayPay.getValue(),function(result){
//	if(result.success){
//	_thridPay.thirdPayBank.loadData(result.data);
//	}
//	});
//	}
//
//	$('#deposit-thirdPay-submit').click(_doThirdPay);
//	queryPay();
//	}

        //支付宝
        function _ZFBQR1Manage() {
            var that = this;
            that.account = $('#deposit-zfbQR1-account');
            that.image = $('#deposit-zfbQR1-image');
            that.button = $('#deposit-zfbQR1-bind');
            that.button.click(function () {
                mobileManage.getModel().open('zfbBind');
            });

            //查询支付宝绑定
            mobileManage.getLoader().open('载入中');
            mobileManage.getBankManage().getZFBQR(function (result) {
                if (result.success) {
                    if (result.data.bind) {
                        that.account.val(result.data.account);
                        if (result.data.auth) {
                            that.image.append('<img style="width:200px;" src="' + result.data.image + '"/>');
                        } else {
                            that.image.append('<div style="color:red;text-align:left;padding:5px;">' + result.message + '</div>');
                        }
                        that.button.html('修改绑定');
                    } else {
                        that.button.html('绑定');
                    }
                } else {
                    alert(result.message)
                }
                mobileManage.getLoader().close();
            });
        }

        //支付宝在线支付
        function _ZFBQR2Manage() {
            var _zfbqr2 = this;
            _zfbqr2.$money = $('#deposit-zfbQR2-money');
            _zfbqr2.$submit = $('#deposit-zfbQR2-submit');
            _zfbqr2.$submit.click(_submit);

            //支付方式
            _zfbqr2.zfbqr2Pay = new MobileComboBox({
                appendId: 'deposit-zfbQR2-pay',
                cls: '',
                valueName: 'value',
                displayName: 'name',
                datas: [{value: '', name: '请选择'}],
                onChange: function (e) {
                }
            });

            //查詢支付方式下拉資料
            function _queryPay() {
                mobileManage.getLoader().open('载入中');
                mobileManage.getTPPManage().queryZfbqr2(function (result) {
                    mobileManage.getLoader().close();
                    if (result.success) {
                        if (result.data.length > 0) {
                            result.data.unshift({value: '', name: '请选择'});
                            _zfbqr2.zfbqr2Pay.loadData(result.data);
                        } else {
                            _zfbqr2.zfbqr2Pay.loadData([{value: '', name: '维护中'}]);
                        }
                    } else {
                        alert(result.message);
                    }
                });
            }

            function _submit() {
                var formData = {
                    payId: _zfbqr2.zfbqr2Pay.getValue(),
                    money: _zfbqr2.$money.val(),
                    bankName: _zfbqr2.zfbqr2Pay.getValue()
                };
                /* if('新贝支付宝'==_zfbqr2.zfbqr2Pay.getValue()){
                 if(!checkWXZFBMoney(_zfbqr2.$money.val())){
                 alert('请存款两位小数金额。如10.26、5.30、6.02');
                 return;
                 }
                 } */
                mobileManage.getLoader().open("处理中");
                mobileManage.getTPPManage().pay(formData, function (result) {
                    if (result.success) {

                    } else {
                        alert(result.message);
                    }
                    mobileManage.getLoader().close();
                });
            }

            _queryPay();
        }

        //微信Manage
        function _WeixinManage() {
            var _weixin = this;
            var _$money = $('#deposit-weixin-money');

            $('#deposit-weixin-submit').click(_doPay);

            //支付方式
            _weixin.weixinPay = new MobileComboBox({
                appendId: 'deposit-weixin-pay',
                cls: '',
                valueName: 'value',
                displayName: 'name',
                datas: [{value: '', name: '请选择'}],
                onChange: function (e) {
                }
            });

            //查詢支付方式下拉資料
            function _queryPay() {
                mobileManage.getLoader().open('载入中');
                mobileManage.getTPPManage().queryWeixin(function (result) {
                    mobileManage.getLoader().close();
                    if (result.success) {
                        if (result.data.length > 0) {
                            result.data.unshift({value: '', name: '请选择'});
                            _weixin.weixinPay.loadData(result.data);
                        } else {
                            _weixin.weixinPay.loadData([{value: '', name: '维护中'}]);
                        }
                    } else {
                        alert(result.message);
                    }
                });
            }

            //乐富微信
            function _doPay() {

                if (_weixin.weixinPay.getValue().indexOf('zfwx') != -1) {
                    var formData = {
                        payType: _weixin.weixinPay.getValue(),
                        order_amount: _$money.val(),
                        bank_code: 'ZF_WX'
                    };
                    if (_$money.val() > 3000) {
                        alert("[提示]存款金额不能超过3000！");
                        return false;
                    }

                    mobileManage.getTPPManage().formSubmit('asp/ZfWxRedirect.aspx', formData);
                } else if (_weixin.weixinPay.getValue().indexOf('xftwx') != -1) { //讯付通微信
                    var formData = {
                        payType: _weixin.weixinPay.getValue(),
                        amount: _$money.val(),
                        payType: 'xftwx'
                    };
                    /* if(_$money.val()>1000){
                     alert("[提示]存款金额不能超过1000！");
                     return false;
                     } */

                    mobileManage.getTPPManage().formSubmit('asp/XftZfbRedirect.aspx', formData);
                    return;
                } else if (_weixin.weixinPay.getValue().indexOf('多宝微信') != -1) {

                    var formData = {
                        payId: _weixin.weixinPay.getValue(),
                        money: _$money.val(),
                        bankName: _weixin.weixinPay.getValue()
                    };
                    mobileManage.getLoader().open("处理中");
                    mobileManage.getTPPManage().pay(formData, function (result) {
                        if (result.success) {
                            window.location.href = result.data.qrcode;
                            return;
                        } else {
                            alert(result.message);
                        }
                    });
                } else {
                    var formData = {
                        payId: _weixin.weixinPay.getValue(),
                        money: _$money.val(),
                        bankName: _weixin.weixinPay.getValue()
                    };
                    mobileManage.getLoader().open("处理中");
                    mobileManage.getTPPManage().pay(formData, function (result) {
                        console.log(result);
                        if (result.success) {

                        } else {
                            alert(result.message);
                        }
                        mobileManage.getLoader().close();
                    });
                }
            }

            _queryPay();
        }

        /**
         * 点卡支付
         *
         */
        function _DCardManage() {
            var _dCard = this;
            var _$form = $('#deposit-card-form');
            var _cardCredit = {};


            /**
             * 初始化
             */
            function _init() {
                _dCard.$cardNo = $('#deposit-card-no');
                _dCard.$cardPassword = $('#deposit-card-password');


                //点卡类型
                _dCard.cardCode = new MobileComboBox({
                    appendId: 'deposit-card-code',
                    cls: '',
                    valueName: 'value',
                    displayName: 'name',
                    datas: [
                        {"value": "", "name": "请选择点卡"},
                        {"value": "YDSZX", "name": "移动神州行"},
                        {"value": "DXGK", "name": "电信国卡"},
                        {"value": "LTYKT", "name": "联通一卡通"},
                        {"value": "QBCZK", "name": "QQ币充值卡"},
                        {"value": "JWYKT", "name": "骏网一卡通"},
                        {"value": "WMYKT", "name": "完美一卡通"},
                        {"value": "ZTYKT", "name": "征途一卡通"},
                        {"value": "WYYKT", "name": "网易一卡通"},
                        {"value": "SFYKT", "name": "盛付通一卡通"},
                        {"value": "SHYKT", "name": "搜狐一卡通"},
                        {"value": "JYYKT", "name": "九游一卡通"},
                        {"value": "THYKT", "name": "天宏一卡通"},
                        {"value": "TXYKT", "name": "天下一卡通"},
                        {"value": "TXYKTZX", "name": "天下一卡通专项"}],
                    onChange: function (e) {
                        _changeCardCredit();
                        _dCard.$cardNo.val('');
                        _dCard.$cardPassword.val('');
                    }
                });

                _dCard.cardCredit = new MobileComboBox({
                    appendId: 'deposit-card-money',
                    cls: '',
                    valueName: 'value',
                    displayName: 'name',
                    datas: [{"value": "", "name": "请选择"}],
                    onChange: function (e) {
                    }
                });

                $('#deposit-card-submit').click(_dCardPay);
            }

            /**
             * 查询点卡面额
             */
            function _queryCardCredit(callback) {
                mobileManage.ajax({
                    url: 'mobile/json/card.json',
                    callback: function (result) {
                        if (result.success) {
                            _cardCredit = result.data;
                        } else {
                            alert(result.message);
                        }
                        callback();
                    }
                })
            }

            /**
             * 建立点卡面额选单
             */
            function _changeCardCredit() {
                var arr = _cardCredit[_dCard.cardCode.getValue()];
                var datas = [{"value": "", "name": "请选择"}];
                for (var i in arr) {
                    datas.push({"value": arr[i], "name": arr[i]});
                }
                _dCard.cardCredit.loadData(datas);
            }

            /**
             * 检查点卡支付是否开启
             */
            function _chekcWork() {
                mobileManage.getLoader().open("处理中");
                mobileManage.getTPPManage().getDCardPayWork(function (result) {
                    mobileManage.getLoader().close();
                    if (result.success) {
                        _$form.replaceWith([
                            '<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-5">',
                            '	<div class="mui-select">',
                            '		<div id="deposit-card-code"></div>',
                            '		<label>点卡类型</label>',
                            '		<div class="message">提示：可选择点卡支付</div>',
                            '	</div>',
                            '	<div class="mui-select">',
                            '		<div id="deposit-card-money"></div>',
                            '		<label>存款额度</label>',
                            '	</div>',
                            '	<div class="testfueld">',
                            '		<input id="deposit-card-no" type="text" >',
                            '		<label>点卡卡号</label>',
                            '	</div>',
                            '	<div class="testfueld">',
                            '		<input id="deposit-card-password" type="text" >',
                            '		<label>点卡密码</label>',
                            '	</div>',
                            '	<div class="mui-btn mui-btn--raised mui-btn--primary block" id="deposit-card-submit">确定支付</div>',
                            '	<div class="space-2"></div>',
                            '</div>'].join(''));

                        //先查询点卡余额再初始化物件
                        _queryCardCredit(_init);
                    } else {
                        _$form.html(result.message);
                    }
                });
            }

            /**
             * 点卡支付
             */
            function _dCardPay() {
                var formData = {
                    card_code: _dCard.cardCode.getValue(),
                    card_no: _dCard.$cardNo.val(),
                    card_password: _dCard.$cardPassword.val(),
                    money: _dCard.cardCredit.getValue()
                };
                mobileManage.getLoader().open("处理中");
                mobileManage.getTPPManage().dCardPay(formData, function (result) {
                    mobileManage.getLoader().close();
                    alert(result.message);
                    if (result.success) {
                        _dCard.$cardNo.val('');
                        _dCard.$cardPassword.val('');
                        _dCard.cardCredit.setValue('');
                    }
                });
            }

            _chekcWork();
        }

        //支付宝附言存款
        function _ZFBRemarkManage() {
            var _zfbRemark = this;
            _zfbRemark.$loginName = $('#deposit-zfbRemark-loginName');
            _zfbRemark.$userName = $('#deposit-zfbRemark-userName');
            _zfbRemark.$accountNo = $('#deposit-zfbRemark-accountNo');
            _zfbRemark.$remark = $('#deposit-zfbRemark-remark');

            $('#deposit-zfbRemark-submit').click(_submitClick);

            mobileManage.getLoader().open("载入资料中");
            mobileManage.getBankManage().getZFBBankInfo(function (result) {
                if (result.success) {
                    _zfbRemark.$loginName.val(result.data.loginName);
                    _zfbRemark.$userName.val(result.data.userName);
                    _zfbRemark.$accountNo.val(result.data.accountNo);
                    _zfbRemark.$remark.val(result.data.vpnpassword);
                } else {
                    alert('请重新刷新网页！');
                }
                mobileManage.getLoader().close();
            });


            /**
             * 支付宝跳转
             *
             */
            function _submitClick() {
                window.location.href = 'https://shenghuo.alipay.com/send/payment/fill.htm';
            }
        }

//        /**
//         * 快捷存款
//         */
//        function _SpeedPayManage() {
//            var that = this;
//            var _$money = $('#deposit-speedPay-money');
//            $('#deposit-speedPay-submit').click(_doPay);
//
//
//            function _doPay() {
//                var formData = {
//                    payId: '汇潮',
//                    money: _$money.val(),
//                    bankName: 'NOCARD'
//                };
//                var $e = $('#m-deposit-speedpay option:selected');
//                var platform = $e.data("platform");
//
//                if (platform == "mbkj") {
//                    var $bankcard = $("#speedpay-bankcard").val();
//                    var $bankname = $("#speedpay-bankname").val();
//                    var $phoneNumber = $("#speedpay-phoneNumber").val();
//
//                    if ($bankcard == "") {
//                        alert("[提示]请填写银行卡!");
//                        return false;
//                    } else if ($bankname == "") {
//                        alert("[提示]请填写银行卡户名!");
//                        return false;
//
//                    } else if ($phoneNumber == "") {
//                        alert("[提示]请填写手机号!");
//                        return false;
//                    }
//
//
//                    formData.bankcard = $bankcard;
//                    formData.bankname = $bankname;
//                    formData.phoneNumber = $phoneNumber;
//                }
//
//                if (platform.indexOf("mifkj") > -1) {
//
//                    var $bankcard = $("#mifkj-bankcard").val();
//                    var $bankname = $("#mifkj-bankname option:selected").val();
//
//                    if ($bankcard == "") {
//                        alert("[提示]请填写银行卡!");
//                        return false;
//                    } else if ($bankname == "") {
//                        alert("[提示]请填写银行卡户名!");
//                        return false;
//                    }
//
//                    formData.bankcard = $bankcard;
//                    formData.bankname = $bankname;
//                }
//
//                if (_TEST_MODE == 1) {
//                    console.log(formData)
//                } else {
//
//                    mobileManage.getLoader().open("处理中");
//                    mobileManage.getTPPManage().pay(formData, function (result) {
//                        if (result.success) {
//
//                        } else {
//                            alert(result.message);
//                        }
//                        mobileManage.getLoader().close();
//                    });
//                }
//            }
//        }

        function _WXValidateManage() {
            var that = this;
            that.amount = $('#deposit-WXValidate-amount');
            that.amountArea = $('#WXValidate-input-area');
            that.payInfo = $('#deposit-WXValidate-payInfo');
            that.button = $('#deposit-WXValidate-submit');
            that.discardButton = $('#deposit-WXValidate-discard');
            that.errorMsg = $("#deposit_WXValidate_error_msg");
            var orderId = '';
            var payInfoHtml = '';

            //查询支付宝绑定
            mobileManage.getLoader().open('载入中');
            mobileManage.getBankManage().getWXValidatePayInfo(function (result) {
                if (result.success) {
                    if (result.data.wxBank.zfbImgCode) {
                        payInfoHtml = '<img style="width:250px;" src="' + result.data.wxBank.zfbImgCode + '"/>';
                    } else {
                        payInfoHtml = '<span style="font-size:20px;">微信账号：<font color="red">' + result.data.wxBank.accountno + '<red></span>';
                    }
                    if (result.data.wxValidaTeAmout != undefined) {
                        console.log("有订单!");
                        orderId = result.data.wxValidaId;
                        existOrderContent(result.data.wxValidaTeAmout, payInfoHtml);
                    } else {
                        console.log("无订单!");
                    }
                    that.errorMsg.css('display', 'none');
                    $("#bankCard").val(result.data.wxBank.accountno);
                } else {
                    that.errorMsg.html('<span style="font-size:20px;"><font color="red">' + result.message + '<red></span>');
                    that.errorMsg.css('display', '');
                    that.amountArea.remove();
                    that.button.remove();
                    alert(result.message);
                }
                mobileManage.getLoader().close();
                // 					mobileManage.getModel().open('confirm',[{
                // 						title:'使用说明',
                // 						message:[
                // 							'<img style="width:100%;" src="/mobile/images/wxvalidate.png?v=1">'
                // 						]
                // 					}]);
            });

            that.button.click(function () {
                var depositAmount = that.amount.val();
                if (clearNoNum(depositAmount)) return;
                if (depositAmount == '' || $.trim(depositAmount) == '') {
                    return;
                }
                if (depositAmount < 10) {
                    alert('最低存款10元！');
                    return;
                }
                if (depositAmount > 5000) {
                    alert('不能超过5000元！');
                    return;
                }
                mobileManage.getLoader().open("处理中");
                $.post("asp/createValidateAmountPayOrderTwo.aspx", {
                    amount: that.amount.val(),
                    bankcard: $("#bankCard").val(),
                    type: "1"
                }, function (data) {
                    if (data.code == '1') {
                        existOrderContent(data.amount, payInfoHtml);
                        mobileManage.getBankManage().getWXValidatePayInfo(function (result) {
                            if (result.success) {
                                orderId = result.data.wxValidaId;
                            }
                        });
                    } else {
                        alert(data.msg);
                    }
                }).fail(function () {
                    alert("生成订单失败");
                }).always(function () {
                    mobileManage.getLoader().close();
                });
            });

            that.discardButton.click(function () {
                mobileManage.getLoader().open("处理中");
                $.post("${ctx}/asp/discardDepositOrder.aspx", {id: orderId}, function (data) {
                    if (data.code == '1') {
                        location.href = "/mobile/fundsManage.jsp";
                    }
                }).fail(function () {
                    alert("废除订单失败");
                }).always(function () {
                    mobileManage.getLoader().close();
                });
            });

            function existOrderContent(wxValidaTeAmout, payInfoHtml) {
                that.discardButton.css('display', '');
                var str = '<span style="font-size:20px;">请您存入&nbsp;&nbsp;<span style="font-size:28px; color:red">' + wxValidaTeAmout + '&nbsp;元</span>，否则无法到账!</span>'
                $('#depositResult').html(str);
                that.payInfo.append(payInfoHtml + '<div><b><font color="red">如果您已支付 请等待系统处理此笔订单,如果您未支付,请进行支付，如有疑问可以随时联系在线客服进行谘询。<font><b></div>');
                that.amountArea.remove();
                that.button.remove();
            }

        }

    }

    function returnWechatPage() {
        $("#deposit-fast-page-2").show().siblings().hide();
    }

    //验证输入input
    function clearNoNum(_value) {
        // 事件中进行完整字符串检测
        var patt = /^((?!0)\d+\.[0-9][1-9])$/g;
        if (!patt.test(_value)) {
            // 错误提示相关代码，边框变红、气泡提示什么的
            alert("输入金额格式错误");
            return true;
        }
        return false;
    }


    function mykeypress(obj, e) {
        // 在 keyup 事件中拦截错误输入
        var keynum;
        if (window.event) { // IE
            keynum = e.keyCode;
        } else if (e.which) { // Netscape/Firefox/Opera
            keynum = e.which;
        }
        var sCharCode = String.fromCharCode(keynum);
        var sValue = obj.value;
        if (/[^0-9.]/g.test(sCharCode) || __getRegex(sCharCode).test(sValue)) {
            return false;
        }

        /**
         * 根据用户输入的字符获取相关的正则表达式
         * @param  {string} sCharCode 用户输入的字符，如 'a'，'1'，'.' 等等
         * @return {regexp} patt 正则表达式
         */
        function __getRegex(sCharCode) {
            var patt;
            if (/[0]/g.test(sCharCode)) {
                // 判断是否为空
                patt = /^$/g;
            } else if (/[.]/g.test(sCharCode)) {
                // 判断是否已经包含 . 字符或者为空
                patt = /((\.)|(^$))/g;
            } else if (/[0-9]/g.test(sCharCode)) {
                // 判断是否已经到达小数点后两位
                patt = /\.\d{2}$/g;
            }
            return patt;
        }
    }


    function checkWXZFBMoney(money) {
        var rex = /\d+[.]\d{2}$/g;
        if (rex.test(money)) return true;
        else return false;
    }


    var clipboard = new Clipboard('.btn-copy');
    clipboard.on('success', function (e) {
        <%--alert('复制成功')--%>
    });

    clipboard.on('error', function (e) {
        <%--alert('复制失败，请重新复制')--%>
    });

    $(function () {
        $("#selectpay").hide();
        $("#card").hide();

        $(".deposit-content ul li").click(function () {
            $(".account-info").hide();
            $(".deposit-content ul li").hide();
            $(this).show();
//	$(this).find(".title").css({
//		"color":"red",
//	"text-align": "center"
//		});
        });

        $(".back-action").click(function () {
            if ($("#funds-deposit-page ul li").find(".title").parent().hasClass("active")) {

                $("#funds-deposit-page ul li").find(".title").parent().removeClass("active");
                $("#funds-deposit-page ul li").find(".title").removeAttr("disabled");

                $(".account-info").show();
                $(".deposit-content ul li").show();
                $("#deposit-weixin-tips").find("li").show();

                $("#deposit-cloud-page-1, #deposit-fast-page-1").show().siblings().hide();
                $("#deposit-cloud-page-1, #deposit-fast-page-1").attr("style", "");
                $("#deposit-fast, #deposit-cloud").show();

                $("#deposit-fast-name, #deposit-fast-money").val("");
                $("#deposit-cloud-card, #deposit-cloud-money").val("");

                $("#deposit-fast-submit").show().removeAttr("disabled");

                return false;
            }
        });

        $("#funds-deposit-page ul li").find(".title").click(function () {
            if ($("#funds-deposit-page ul li").find(".title").parent().hasClass("active")) {
                $("#funds-deposit-page ul li").find(".title").attr('disabled', "true");
            }
        });

    })
</script>
<style>
    .hidden {
        display: none !important;
    }

</style>
</body>
</html>