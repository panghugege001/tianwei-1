<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE >
<html>
<head>
    <title>天威-存款中心</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0,user-scalable=no">
    <link rel="stylesheet" type="text/css" href="/mobile/css/lib/mui-0.2.1/mui.min.css">
    <link href="/js/layer/mobile/need/layer.css?2.0" type="text/css" rel="styleSheet" id="layermcss">
    <link rel="stylesheet" type="text/css" href="/mobile/app/css/funds.css?v=90000002"/>
    <style></style>
</head>
<body>

<div class="contents j-title-content">
    <div class="center-top">
        <header class="common-header j-common-header j-come-back"><i class="left-button deposit-sprites arrow"
                                                                     onclick="goBackApp();"></i>
            <div class="header-title">快速存款</div>
        </header>
        <div class="o-wrapper">
            <div class="balance">
                <p class="amount-tit">主账户余额</p>
                <div><span>${session.customer.credit}</span> 元</div>
            </div>
        </div>
    </div>
    <div class="center-list pay-tabctrl">

        <div class="tab-btn subtitle-btn">

            <a href="javascript:;" id="alipay-title" class="span span2">
                支付宝
                <i class="zeng">赠1.5%</i>
            </a>

            <a href="javascript:;" id="bank-title" class="span span2">
                银行卡支付
                <i class="zeng">赠1.5%</i>
            </a>

            <a href="javascript:;" id="wechat-title" class="span span2 stop">
                微信支付
                <i class="zeng">赠1.5%</i>
            </a>

            <a href="javascript:;" id="jiuan-title" class="span span2 stop">
                久安支付
                <i class="zeng">赠2%</i>
            </a>

            <a href="javascript:;" id="qq-title" class="span span2 stop">
                QQ支付
            </a>

            <a href="javascript:;" id="other-title" class="span span2 stop">
                其他支付
            </a>

        </div>

    </div>

    <div class="contents j-page-content">


        <!-- 支付宝支付 -->
        <div class="alipay-page full-page">
            <header class="common-header j-common-header">
                <div class="header-title">支付宝</div>
            </header>

            <div class="pay-content">

                <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <p class="font16 my-input-title step1-title">存款方式</p>

                    <div class="tab-btn subtitle-btn step1-title">
                        <a href="javascript:;" id="zfbmc-subtitle" data-target="zfbmc-page" data-value="2"
                           class="span">
                            <div class="tit-zf">支付宝转账</div>
                            <div class="tab-txt">稳定，单笔上限300万</div>
                            <i class="zeng">赠1.5%</i>
                        </a>
                        <a href="javascript:;" id="zfbQR-subtitle" data-target="zfbQR-page"
                           class="span span2 stop">支付宝扫码支付
                            <div class="tab-txt">便捷，适合小额存款</div>
                        </a>
                    </div>

                    <div class="tab-contents">
                        <div class="alipay01 tabdiv" id="zfbmc-page">
                            <div class="step1">
                                <p class="font16 my-input-title">存款通道</p>
                                <div class="pay-method payway-list">
                                <span class="payway-show j-payway-show active" data-max="3000000" data-min="10"
                                      data-value="2">
                                <span class="text ">通道1</span>
                            </span>
                                <span class="payway-show j-payway-show" data-max="3000000" data-min="1"
                                data-value="22">
                                <span class="text ">通道2</span>
                                </span>
                            </div>
                            <p class="font16 my-input-title">存款姓名</p>
                            <input type="text" class="quick-name my-input" placeholder="请输入您转账支付宝的姓名">
                            <p class="name-tips red"></p>

                                <p class="font16 my-input-title">存款金额</p>
                                <input type="number" class="quick-money my-input money-input" value=""
                                       placeholder="10元-300万">
                                <p class="error-tips red"></p>
                                <%--<p class="input-tips">输入并存入带2位小数的金额，可更快上分。</p>--%>
                                <div class="space-2"></div>
                                <!-- <div class="gray-tips c-red">* 提示风险：请关闭您的微信定位服务，感谢配合！</div> -->
                                <div class="gray-tips">* 支付宝转账每日23:20-01:00左右有延迟到帐现象，若转账状态成功后仍未到账，请咨询客服。</div>
                                <div class="space-2"></div>
                                <div class="bottom-btn quick-submit">获取收款账号</div>
                                <div class="space-5"></div>
                            </div>
                            <div class="step2" style="display:none;">
                                <p class="font16 my-input-title">存款信息</p>
                                <ul class="mui-formTable">
                                    <li>
                                        <span class="left-title">游戏账号</span>
                                        <span class="right-txt"><span class="quick-confirm-account"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">存款姓名</span>
                                        <span class="right-txt"><span class="quick-confirm-name"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">存款金额</span>
                                        <span class="right-txt"><span class="quick-confirm-money"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">回馈</span>
                                        <span class="right-txt"><span class="quick-confirm-feedback"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">所获得金额</span>
                                        <span class="right-txt"><span class="quick-confirm-amount"></span></span>
                                    </li>
                                </ul>

                                <div class="space-1"></div>
                                <p class="text-center red f16">务必依照此金额转账才会到账</p>
                                <div class="space-1"></div>

                                <p class="font16 my-input-title">收款账号</p>
                                <ul class="mui-formTable">
                                    <li><p class="tips red">仅本次有效，下次存款请重新获取</p></li>
                                    <li>
                                        <span class="left-title">收款银行</span>
                                        <span class="right-txt"><input type="text" class="my-input qqbankname"
                                                                       readonly="readonly"
                                                                       id="sbankname"></span>
                                    </li>
                                    <li>
                                        <span class="left-title">收款账号</span>
                                        <span class="right-txt">
                        <input type="text" class="my-input qqaccountno" id="saccountno" readonly="readonly">
                        <button class="btn-copy" data-clipboard-action="copy"
                                data-clipboard-target="#saccountno">复制</button>
                    </span>
                                    <li>
                                        <span class="left-title">收款人姓名</span>
                                        <span class="right-txt">
                        <input type="text" class="my-input qqaccountname" id="saccountname" readonly="readonly">
                        <button class="btn-copy" data-clipboard-action="copy"
                                data-clipboard-target="#saccountname">复制</button>
                    </span>
                                    </li>

                                </ul>

                                <div class="space-2"></div>
                                <div class="text-center">
                                    <div class="space-1"></div>
                                    <div class="quick-renew bottom-btn">存款失败，重新获取新的收款账户</div>
                                    <div class="space-1"></div>
                                    <div class="quick-success bottom-btn">我已存款</div>
                                </div>

                                <div class="space-5"></div>

                            </div>
                            <%--<div class="step3" style="display:none;">--%>
                            <%--<p class="font2 my-input-title red">您需要存入金额：</p>--%>
                            <%--<p class="font4 my-input-title red quota"></p>--%>
                            <%--<div class="space-2"></div>--%>
                            <%--<div class="gray-tips red font2">温馨提示：请您存入该金额，否则存款无法到账，本订单有效时间为2小时</div>--%>
                            <%--<div class="space-2"></div>--%>
                            <%--<div class="tips"><input type="checkbox" class="must-checked" id="checked-z"><label--%>
                            <%--for="checked-z">我已明白需要转账--%>
                            <%--<span class="quota red"></span> 元</label></div>--%>
                            <%--<div class="tips"><input type="checkbox" class="must-checked1" id="checked-z1"><label--%>
                            <%--for="checked-z1">本人已同意，如未转账--%>
                            <%--<span class="quota red"></span> 元，导致系统无法匹配存款，天威概不负责！</label></div>--%>
                            <%--<div class="space-2"></div>--%>
                            <%--<div class="fast-next bottom-btn mt10">下一步</div>--%>
                            <%--<div class="space-5"></div>--%>
                            <%--</div>--%>
                        </div>
                        <div class="alipay02 tabdiv" id="zfbQR-page" style="display:none;">
                            <p class="font16 my-input-title">存款金额</p>
                            <input type="number" class="my-input money-input" value=""
                                   placeholder="1-3000">
                            <p class="error-tips red"></p>
                            <div class="fast-amount">
                                <span class="num-btn" data-value="30">30</span>
                                <span class="num-btn" data-value="50">50</span>
                                <span class="num-btn" data-value="100">100</span>
                                <span class="num-btn" data-value="200">200</span>
                                <span class="num-btn" data-value="500">500</span>
                                <span class="num-btn" data-value="1000">1000</span>
                                <span class="num-btn" data-value="2000">2000</span>
                                <span class="num-btn" data-value="3000">3000</span>
                                <span class="num-btn hide" data-value="5000">5000</span>
                                <span class="num-btn hide" data-value="10000">10000</span>
                                <span class="num-btn hide" data-value="20000">20000</span>
                                <span class="num-btn hide" data-value="50000">50000</span>
                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>

                            <p class="font16 my-input-title">支付通道</p>
                            <div id="alipay-curpay" class=" zfbQR-reload pay-method payway-list"></div>
                            <div class="gray-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <!-- <div class="gray-tips">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取</div> -->
                            <div class="space-1"></div>
                            <div class="space-2"></div>
                            <div class="bottom-btn" id="zfbQR-confirm">确认支付</div>

                            <div class="space-1"></div>
                            <div class="gray-tips">温馨提示</div>
                            <div class="gray-tips">* 因为支付通道限制，支付宝支付通道需要扣除手续1%—5%手续费，不同支付通道收取的费率不同，此费用由第三方收取。</div>

                            <div class="space-5"></div>

                        </div>

                    </div>

                </div>
            </div>
        </div>

        <!-- 银行支付 -->
        <div class="bank-page full-page">
            <header class="common-header j-common-header">
                <div class="header-title">网上银行</div>
            </header>
            <div class="pay-content">
                <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <p class="font16 my-input-title step1-title">存款方式</p>
                    <div class="tab-btn subtitle-btn step1-title">
                        <a href="javascript:;" id="bank-subtitle" data-value="1"
                           data-target="bank-page"
                           class="span active">
                            手机/网银
                            <div class="tab-txt">推荐已开通网银用户使用</div>
                            <i class="zeng">赠1.5%</i>
                        </a>
                        <%--<a href="javascript:;" id="cloud-subtitle" data-value="5"--%>
                        <%--data-target="cloud-page"--%>
                        <%--class="span">--%>
                        <%--云闪付--%>
                        <%--<div class="tab-txt">推荐已开通网银用户使用</div>--%>
                        <%--<i class="zeng">赠1.5%</i>--%>
                        <%--</a>--%>
                        <a href="javascript:;" id="yinlian-subtitle" data-target="yinlian-page"
                           class="span span2 stop">银联支付
                            <div class="tab-txt">任意银联APP可扫码</div>
                        </a>

                        <a href="javascript:;" id="thirdpay-subtitle" data-target="thirdpay-page"
                           class="span span2 stop">在线支付
                            <div class="tab-txt">推荐已开通网银用户使用</div>
                        </a>
                        <a href="javascript:;" id="speedpay-subtitle" data-target="speedpay-page"
                           class="span span2 stop">快捷支付
                            <div class="tab-txt">验证身份证号、短信即可</div>
                        </a>
                    </div>
                    <div class="tab-contents">
                        <div class="alipay01 tabdiv" id="bank-page">
                            <div class="step1">


                                <p class="font16 my-input-title">存款通道</p>
                                <div class="pay-method payway-list">
                            <span class="payway-show j-payway-show active" data-max="3000000" data-min="10"
                                  data-value="1">
                                <span class="text ">通道1</span>
                            </span>
                                    <%--<span class="payway-show j-payway-show" data-max="3000000" data-min="10"--%>
                                    <%--data-value="7">--%>
                                    <%--<span class="text ">通道2</span>--%>
                                    <%--</span>--%>
                                </div>
                                <p class="font16 my-input-title">存款姓名</p>
                                <input type="text" class="quick-name my-input" placeholder="请输入您转账的姓名">
                                <p class="name-tips red"></p>
                                <p class="font16 my-input-title">存款金额</p>
                                <input type="number" class="quick-money my-input money-input"
                                       placeholder="10元-300万">
                                <p class="error-tips red"></p>

                                <div class="space-2 clear"></div>
                                <div class="bottom-btn quick-submit">获取收款账号</div>
                                <div class="space-5"></div>

                            </div>
                            <div class="step2" style="display:none;">
                                <p class="font16 my-input-title">存款信息</p>

                                <ul class="mui-formTable">
                                    <li>
                                        <span class="left-title">游戏账号</span>
                                        <span class="right-txt"><span class="quick-confirm-account"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">存款姓名</span>
                                        <span class="right-txt"><span class="quick-confirm-name"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">存款金额</span>
                                        <span class="right-txt"><span class="quick-confirm-money"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">回馈</span>
                                        <span class="right-txt"><span class="quick-confirm-feedback"></span></span>
                                    </li>
                                    <li>
                                        <span class="left-title">所获得金额</span>
                                        <span class="right-txt"><span class="quick-confirm-amount"></span></span>
                                    </li>
                                </ul>

                                <div class="space-1"></div>
                                <p class="text-center red f16">务必依照此金额转账才会到账</p>
                                <div class="space-1"></div>

                                <p class="font16 my-input-title">收款账号</p>
                                <ul class="mui-formTable">
                                    <li><p class="tips red">仅本次有效，下次存款请重新获取</p></li>
                                    <li>
                                        <span class="left-title">收款银行</span>
                                        <span class="right-txt"><input type="text" class="my-input qqbankname"
                                                                       readonly="readonly"
                                                                       id="ccbankname"></span>
                                    </li>
                                    <li>
                                        <span class="left-title">收款账号</span>
                                        <span class="right-txt">
    <input type="text" class="my-input qqaccountno" id="ccaccountno" readonly="readonly">
    <button class="btn-copy" data-clipboard-action="copy" data-clipboard-target="#ccaccountno">复制</button>
    </span>
                                    </li>
                                    </li>
                                    <li id="fastpay-area" style="display:none;">
                                        <span class="left-title">开户地区</span>
                                        <span class="right-txt">
                        <input type="text" class="my-input qqarea" id="sacarea" readonly="readonly">
                    </span>
                                    </li>
                                    <li>
                                        <span class="left-title">收款人姓名</span>
                                        <span class="right-txt">
    <input type="text" class="my-input qqaccountname" id="ccaccountname" readonly="readonly">
    <button class="btn-copy" data-clipboard-action="copy" data-clipboard-target="#ccaccountname">复制</button>
    </span>
                                    </li>
                                    <li id="fuyan-box">
                                        <span class="left-title">附言:</span>
                                        <span class="right-txt">
        <input id="ccmefuyan" class="my-input qqmefuyan" type="text" readonly="readonly">
        <button class="btn-copy" data-clipboard-action="copy" data-clipboard-target="#ccmefuyan">复制</button>
    </span>
                                    </li>

                                </ul>

                                <div class="space-2"></div>
                                <div class="text-center">
                                    <div class="quick-renew bottom-btn">存款失败，重新获取新的收款账户</div>
                                    <div class="space-1"></div>
                                    <div class="quick-success bottom-btn">我已存款</div>
                                </div>
                                <div class="space-5"></div>
                            </div>
                            <%--<div class="step3" style="display:none;">--%>
                            <%--<p class="font2 my-input-title red">您需要存入金额：</p>--%>
                            <%--<p class="font4 my-input-title red quota"></p>--%>
                            <%--<div class="space-2"></div>--%>
                            <%--<div class="gray-tips red font2">温馨提示：请您存入该金额，否则存款无法到账，本订单有效时间为2小时</div>--%>
                            <%--<div class="space-2"></div>--%>
                            <%--<div class="tips"><input type="checkbox" class="must-checked" id="checked-b"><label--%>
                            <%--for="checked-b">我已明白需要转账--%>
                            <%--<span class="quota red"></span> 元</label></div>--%>
                            <%--<div class="tips"><input type="checkbox" class="must-checked1" id="checked-b1"><label--%>
                            <%--for="checked-b1">本人已同意，如未转账--%>
                            <%--<span class="quota red"></span> 元，导致系统无法匹配存款，天威概不负责！</label></div>--%>
                            <%--<div class="space-2"></div>--%>
                            <%--<div class="fast-next bottom-btn mt10">下一步</div>--%>
                            <%--<div class="space-5"></div>--%>
                            <%--</div>--%>
                        </div>
                        <%--<div class="alipay02 tabdiv" id="cloud-page" style="display:none;">--%>
                        <%--<div class="step1">--%>
                        <%--<p class="font16 my-input-title">银行卡号</p>--%>
                        <%--<div class="relative">--%>
                        <%--<input type="text" class="cloud-card my-input" maxlength="4"--%>
                        <%--placeholder="请输入您要银行卡号末四位"--%>
                        <%--onkeyup="value=value.replace(/[^\d]/g,'');">--%>
                        <%--<p class="name-tips red"></p>--%>
                        <%--</div>--%>

                        <%--<p class="font16 my-input-title">存款金额</p>--%>
                        <%--<input type="text" class="my-input money-input" maxlength="10"--%>
                        <%--placeholder="存款金额1元-300万" onkeyup="value=value.replace(/[^\d]/g,'');">--%>
                        <%--<p class="error-tips red"></p>--%>

                        <%--<div class="bottom-btn cloud-submit mt10" data-type="4">获取收款账号</div>--%>
                        <%--<div class="space-2"></div>--%>

                        <%--<p class="font16 my-input-title text-center"><font color="red">云闪付APP下载二维码</font></p>--%>
                        <%--<div class="text-center">--%>
                        <%--<img src="/images/deposit/yunshanfu_qrcode.png" style="width: 200px;"/>--%>
                        <%--</div>--%>

                        <%--<p class="font16 my-input-title">温馨提示</p>--%>
                        <%--<div class="gray-tips">1.云闪付请下载云闪付APP才能进行存款支付</div>--%>
                        <%--<div class="gray-tips"><font color="red">2.云闪付存款需要填写正确支付银行卡号末四码系统才会正确匹配到帐。</font></div>--%>
                        <%--<div class="gray-tips"><font color="red">3.支付金额需要与填写存款金额一样系统才会匹配上分。</font></div>--%>
                        <%--<div class="space-5"></div>--%>
                        <%--</div>--%>
                        <%--<div class="step2" style="display:none;">--%>

                        <%--<p class="font16 my-input-title">存款信息</p>--%>
                        <%--<ul class="mui-formTable">--%>
                        <%--<li>--%>
                        <%--<span class="left-title">游戏账号</span>--%>
                        <%--<span class="right-txt"><span class="quick-confirm-account"></span></span>--%>
                        <%--</li>--%>
                        <%--<li>--%>
                        <%--<span class="left-title">存款金额</span>--%>
                        <%--<span class="right-txt"><span class="quick-confirm-money"></span></span>--%>
                        <%--</li>--%>
                        <%--<li>--%>
                        <%--<span class="left-title">回馈</span>--%>
                        <%--<span class="right-txt"><span class="quick-confirm-feedback"></span></span>--%>
                        <%--</li>--%>
                        <%--<li>--%>
                        <%--<span class="left-title">所获得金额</span>--%>
                        <%--<span class="right-txt"><span class="quick-confirm-amount"></span></span>--%>
                        <%--</li>--%>
                        <%--</ul>--%>

                        <%--<div class="space-1"></div>--%>
                        <%--<p class="text-center red f16">务必依照此金额转账才会到账</p>--%>
                        <%--<div class="space-1"></div>--%>

                        <%--<p class="font16 my-input-title">收款账号</p>--%>
                        <%--<img src="" id="cImgCode" style="width:100%;"/>--%>
                        <%--<div class="space-2"></div>--%>
                        <%--<div class="btn-box">--%>
                        <%--&lt;%&ndash;<div class="cloud-restart bottom-btn mui-btn--gray">重新填写</div>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;data-modal="cloudPayModal"&ndash;%&gt;--%>
                        <%--<div class="j-teach-btn bottom-btn mui-btn--yellow"--%>
                        <%--onclick="window.open('http://cn.unionpay.com/zt/2017/139595361/','_blank');">--%>
                        <%--查看云闪付教程--%>
                        <%--</div>--%>
                        <%--<div class="space-1"></div>--%>
                        <%--<div class="cloud-success bottom-btn mt10">我已经成功付款</div>--%>
                        <%--</div>--%>


                        <%--<div class="space-1"></div>--%>
                        <%--<p class="font16 my-input-title">温馨提示</p>--%>
                        <%--<div class="gray-tips">1.云闪付请下载云闪付APP才能进行存款支付</div>--%>
                        <%--<div class="gray-tips"><font color="red">2.云闪付存款需要填写正确支付银行卡号末四码系统才会正确匹配到帐。</font></div>--%>
                        <%--<div class="gray-tips"><font color="red">3.支付金额需要与填写存款金额一样系统才会匹配上分。</font></div>--%>
                        <%--<div class="gray-tips">4.如果您的款项10分钟未能到账，请联系24小时在线客服</div>--%>
                        <%--<div class="space-5"></div>--%>
                        <%--</div>--%>
                        <%--</div>--%>
                        <div class="alipay03 tabdiv" id="yinlian-page" style="display:none;">
                            <p class="font16 my-input-title">存款金额 </p>
                            <input type="number" class="my-input money-input"
                                   placeholder="0.00">
                            <p class="input-tips red error-tips">
                                <%--请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。--%>
                            </p>
                            <%--<p class="input-tips red error-tips">请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。</p>--%>
                            <div class="fast-amount">
                                <span class="num-btn num-btn100" data-value="50">50</span>
                                <span class="num-btn num-btn100" data-value="100">100</span>
                                <span class="num-btn num-btn100" data-value="200">200</span>
                                <span class="num-btn num-btn500" data-value="500">500</span>
                                <span class="num-btn num-btn1000" data-value="1000">1000</span>
                                <span class="num-btn num-btn2000" data-value="2000">2000</span>
                                <span class="num-btn num-btn3000" data-value="3000">3000</span>
                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>
                            <p class="font16 my-input-title">支付通道</p>
                            <div class=" yinlian-reload pay-method payway-list"></div>
                            <div class="space-1"></div>
                            <div class="gray-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <div class="space-1"></div>
                            <div class="gray-tips">
                                <p>* 银联扫码，可使用任意银联APP或云闪付APP扫码支付。</p>
                                <!-- <p style="display: none">* 2、需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取</p> -->
                            </div>
                            <div class="space-1"></div>
                            <div class="space-2"></div>
                            <div class="bottom-btn " id="yinlian-confirm">确认支付</div>
                            <div class="space-5"></div>
                        </div>

                        <div class="alipay05 tabdiv" id="thirdpay-page" style="display:none;">
                            <p class="font16 my-input-title">存款金额</p>
                            <input type="number" class="my-input money-input" placeholder="1-3000">
                            <p class="error-tips red"></p>
                            <div class="fast-amount">
                                <span class="num-btn num-btn100" data-value="50">50</span>
                                <span class="num-btn num-btn100" data-value="100">100</span>
                                <span class="num-btn num-btn100" data-value="200">200</span>
                                <span class="num-btn num-btn500" data-value="500">500</span>
                                <span class="num-btn num-btn1000" data-value="1000">1000</span>
                                <span class="num-btn num-btn2000" data-value="2000">2000</span>
                                <span class="num-btn num-btn3000" data-value="3000">3000</span>
                                <span class="num-btn hide" data-value="5000">5000</span>
                                <span class="num-btn hide" data-value="10000">10000</span>
                                <span class="num-btn hide" data-value="50000">50000</span>
                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>
                            <p class="font16 my-input-title">支付通道</p>
                            <div class=" bankpay-reload pay-method payway-list"></div>
                            <div class="space-1"></div>
                            <div class="payway-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <div class="space-1"></div>
                            <div class="my-input bank-choose" onclick="$('.j-bank-box').show()">
                                支付银行
                                <span class="bank-list-txt j-bank-list-txt"></span><i class="i-arrow-r"></i>
                            </div>
                            <%--<div class="payway-tips">*需要承担<span class="payway-fee">0.8</span>%手续费，费用由第三方收取</div>--%>

                            <p class="bank-tips red"></p>
                            <div></div>
                            <div class="bank-box j-bank-box" style="display:none;">
                                <ul class="my-bank-list j-bank-list" id="bank-list">
                                    <li class="payWayBtn " data-value="ABC" value="农业银行"><i
                                            class="bank-sprite ABC"></i><span class="text">农业银行</span></li>
                                </ul>
                                <div class="bottom-btn" onclick="checkBank()">确定</div>
                                <div class="space-2"></div>
                            </div>


                            <div class="space-2"></div>
                            <div class="bottom-btn " id="thirdpay-confirm">确认支付</div>
                            <div class="space-5"></div>

                        </div>
                        <div class="alipay06 tabdiv" id="speedpay-page" style="display:none;">
                            <p class="font16 my-input-title">存款金额</p>
                            <input type="number" class="my-input money-input" placeholder="1-3000">
                            <p class="bank-tips red"></p>
                            <div class="fast-amount">
                                <span class="num-btn num-btn100" data-value="50">50</span>
                                <span class="num-btn num-btn100" data-value="100">100</span>
                                <span class="num-btn num-btn100" data-value="200">200</span>
                                <span class="num-btn num-btn500" data-value="500">500</span>
                                <span class="num-btn num-btn1000" data-value="1000">1000</span>
                                <span class="num-btn num-btn2000" data-value="2000">2000</span>
                                <span class="num-btn num-btn3000" data-value="3000">3000</span>
                                <span class="num-btn" data-value="5000">5000</span>
                                <span class="num-btn" data-value="10000">10000</span>
                                <span class="num-btn" data-value="50000">50000</span>
                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>
                            <div class="space-1"></div>
                            <p class="font16 my-input-title">支付通道</p>
                            <div class="speedpay-reload pay-method payway-list"></div>
                            <div class="payway-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <!-- <div class="payway-tips">*需要承担<span class="payway-fee">0.8</span>%手续费，费用由第三方收取</div> -->

                            <div class="space-2"></div>
                            <div class="bottom-btn " id="speedpay-confirm">确认支付</div>
                            <div class="space-5"></div>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 微信支付 -->
        <div class="wechat-page full-page">
            <header class="common-header j-common-header">
                <div class="header-title">微信支付</div>
            </header>
            <div class="pay-content">

                <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <div class="j-subtitle">
                        <p class="font16 my-input-title">存款方式</p>
                        <div class="tab-btn subtitle-btn">

                            <a href="javascript:;" id="wexinSpeed-subtitle"
                               data-target="wexinSpeed-page"
                               class="span span2 stop">微信快捷
                                <div class="tab-txt">便捷，最高上限3000</div>
                                <i class="zeng">推荐</i>
                            </a>

                            <a href="javascript:;" id="wexinQR-subtitle" data-target="wexinQR-page"
                               class="span span2 stop">微信扫码
                                <div class="tab-txt">二维码识别</div>
                            </a>

                            <%--<a href="javascript:;" id="wechat-subtitle" data-target="wechat-page"--%>
                               <%--data-value="4"--%>
                               <%--class="span">--%>
                                <%--微信转账--%>
                                <%--<div class="tab-txt">稳定，单笔上限300万</div>--%>
                                <%--<i class="zeng">赠1.5%</i>--%>
                            <%--</a>--%>

                        </div>
                    </div>
                    <div class="tab-contents tab-contents-wechat">

                        <div class="alipay01 tabdiv" id="wexinSpeed-page">
                            <p class="font16 my-input-title">存款金额</p>
                            <input type="number" class="my-input money-input" placeholder="1-5000">
                            <p class="error-tips red"></p>
                            <div class="fast-amount">

                                <span class="num-btn" data-value="100">100</span>
                                <span class="num-btn" data-value="200">200</span>
                                <span class="num-btn" data-value="300">300</span>
                                <span class="num-btn" data-value="400">400</span>
                                <span class="num-btn" data-value="500">500</span>
                                <span class="num-btn" data-value="600">600</span>
                                <span class="num-btn" data-value="700">700</span>
                                <span class="num-btn" data-value="800">800</span>
                                <span class="num-btn" data-value="900">900</span>
                                <span class="num-btn" data-value="1000">1000</span>


                                <%--<span class="num-btn" data-value="99">99</span>--%>
                                <%--<span class="num-btn" data-value="199">199</span>--%>
                                <%--<span class="num-btn" data-value="299">299</span>--%>
                                <%--<span class="num-btn" data-value="399">399</span>--%>
                                <%--<span class="num-btn" data-value="499">499</span>--%>
                                <%--<span class="num-btn" data-value="599">599</span>--%>
                                <%--<span class="num-btn" data-value="699">699</span>--%>
                                <%--<span class="num-btn" data-value="799">799</span>--%>
                                <%--<span class="num-btn" data-value="899">899</span>--%>
                                <%--<span class="num-btn" data-value="999">999</span>--%>

                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>

                            <p class="font16 my-input-title">支付通道</p>
                            <div class="wexinSpeed-reload pay-method payway-list"></div>

                            <div class="space-1"></div>
                            <div class="gray-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <%--<div class="gray-tips">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取</div>--%>


                            <div class="space-2"></div>
                            <%--<div class="ezgif-box">--%>
                            <%--<img src="../images/ezgif.gif" alt="" class="ezgif">--%>
                            <%--<span class="bottom-btn" id="closeGif">关闭教程</span>--%>
                            <%--</div>--%>

                            <%--<div class="bottom-btn" id="courseGif">查看教程</div>--%>
                            <div class="bottom-btn" id="wexinSpeed-confirm">确认支付</div>

                            <div class="space-1"></div>
                            <div class="gray-tips">温馨提示</div>
                            <div class="gray-tips">* 因为支付通道限制，微信支付通道需要扣除手续1%—5%手续费，不同支付通道收取的费率不同，此费用由第三方收取。</div>

                            <div class="space-5"></div>
                        </div>

                        <div class="alipay02 tabdiv" id="wexinQR-page">
                            <p class="font16 my-input-title">存款金额</p>
                            <input type="number" class="my-input money-input" placeholder="1-3000">
                            <p class="error-tips red"></p>
                            <div class="fast-amount">
                                <span class="num-btn" data-value="100">100</span>
                                <span class="num-btn" data-value="200">200</span>
                                <span class="num-btn" data-value="300">300</span>
                                <span class="num-btn" data-value="500">500</span>
                                <span class="num-btn" data-value="600">600</span>
                                <span class="num-btn" data-value="800">800</span>
                                <span class="num-btn" data-value="1000">1000</span>
                                <span class="num-btn" data-value="2000">2000</span>
                                <span class="num-btn" data-value="3000">3000</span>
                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>

                            <p class="font16 my-input-title">支付通道</p>
                            <div id="curWechatpay" class="wexinQR-reload pay-method payway-list"></div>

                            <div class="space-1"></div>
                            <div class="gray-tips">若选择的通道无法存款，请点选其他支付通道</div>

                            <div class="space-2"></div>
                            <div class="bottom-btn " id="wexinQR-confirm">确认支付</div>

                            <div class="space-1"></div>
                            <div class="gray-tips">温馨提示</div>
                            <div class="gray-tips">* 因为支付通道限制，微信支付通道需要扣除手续1%—5%手续费，不同支付通道收取的费率不同，此费用由第三方收取。</div>

                            <div class="space-5"></div>
                        </div>

                        <%--<div class="alipay03 tabdiv" id="wechat-page">--%>
                            <%--<div class="step1">--%>

                                <%--<p class="font16 my-input-title">存款通道</p>--%>
                                <%--<div class="pay-method payway-list">--%>
                        <%--<span class="payway-show j-payway-show active" data-max="3000000" data-min="10"--%>
                              <%--data-value="4">--%>
                        <%--<span class="text ">通道1</span>--%>
                        <%--</span>--%>
                                    <%--&lt;%&ndash;<span class="payway-show j-payway-show" data-max="3000000" data-min="10"&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;data-value="7">&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;<span class="text ">通道2</span>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;</span>&ndash;%&gt;--%>
                                <%--</div>--%>
                                <%--<p class="font16 my-input-title">存款姓名</p>--%>
                                <%--<input type="text" class="quick-name my-input" placeholder="请输入您转账的姓名">--%>
                                <%--<p class="name-tips red"></p>--%>

                                <%--<p class="font16 my-input-title">存款金额</p>--%>
                                <%--<input type="number" class="quick-money my-input money-input" value=""--%>
                                       <%--placeholder="10元-300万">--%>
                                <%--<p class="error-tips red"></p>--%>
                                <%--&lt;%&ndash;<p class="input-tips">输入并存入带2位小数的金额，可更快上分。</p>&ndash;%&gt;--%>

                                <%--<div class="space-2 clear"></div>--%>
                                <%--<!-- <div class="gray-tips c-red">* 提示风险：请关闭您的微信定位服务，感谢配合！</div> -->--%>
                                <%--<div class="gray-tips">* 使用微信转账，请将微信升级至最新版本。</div>--%>
                                <%--<div class="gray-tips">* 微信转账每天22:30至01:00例行结算维护；此期间请先使用其他存款方式。</div>--%>
                                <%--<div class="gray-tips">* 请务必保存存款成功截图，未到账时可提供给官网客服。</div>--%>
                                <%--<div class="space-2 clear"></div>--%>

                                <%--<div class="bottom-btn quick-submit">获取收款账号</div>--%>
                            <%--</div>--%>
                            <%--<div class="step2" style="display:none;">--%>
                                <%--<p class="font16 my-input-title">存款信息</p>--%>
                                <%--<ul class="mui-formTable">--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">游戏账号</span>--%>
                                        <%--<span class="right-txt"><span class="quick-confirm-account"></span></span>--%>
                                    <%--</li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">存款姓名</span>--%>
                                        <%--<span class="right-txt"><span class="quick-confirm-name"></span></span>--%>
                                    <%--</li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">存款金额</span>--%>
                                        <%--<span class="right-txt"><span class="quick-confirm-money"></span></span>--%>
                                    <%--</li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">回馈</span>--%>
                                        <%--<span class="right-txt"><span class="quick-confirm-feedback"></span></span>--%>
                                    <%--</li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">所获得金额</span>--%>
                                        <%--<span class="right-txt"><span class="quick-confirm-amount"></span></span>--%>
                                    <%--</li>--%>
                                <%--</ul>--%>

                                <%--<div class="space-1"></div>--%>
                                <%--<p class="text-center red f16">务必依照此金额转账才会到账</p>--%>
                                <%--<div class="space-1"></div>--%>

                                <%--<p class="font16 my-input-title">收款账号</p>--%>
                                <%--<ul class="mui-formTable">--%>
                                    <%--<li><p class="tips red">仅本次有效，下次存款请重新获取</p></li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">收款银行</span>--%>
                                        <%--<span class="right-txt"><input type="text" class="my-input qqbankname"--%>
                                                                       <%--readonly="readonly"--%>
                                                                       <%--id="qqbankname"></span>--%>
                                    <%--</li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">收款账号</span>--%>
                                        <%--<span class="right-txt">--%>
                        <%--<input type="text" class="my-input qqaccountno" id="qqaccountno" readonly="readonly">--%>
                        <%--<button class="btn-copy" data-clipboard-action="copy"--%>
                                <%--data-clipboard-target="#qqaccountno">复制</button>--%>
                        <%--</span>--%>
                                    <%--</li>--%>
                                    <%--<li>--%>
                                        <%--<span class="left-title">收款人姓名</span>--%>
                                        <%--<span class="right-txt">--%>
                        <%--<input type="text" class="my-input qqaccountname" id="qqaccountname" readonly="readonly">--%>
                        <%--<button class="btn-copy" data-clipboard-action="copy"--%>
                                <%--data-clipboard-target="#qqaccountname">复制</button>--%>
                        <%--</span>--%>
                                    <%--</li>--%>

                                <%--</ul>--%>
                                <%--&lt;%&ndash;<div class="red f16">请务必保存存款成功截图，未到账可提供给在线客服上分。</div>&ndash;%&gt;--%>
                                <%--<div class="space-2"></div>--%>
                                <%--<div class="text-center">--%>
                                    <%--<div class="quick-renew bottom-btn">存款失败，重新获取新的收款账户</div>--%>
                                    <%--<div class="space-1"></div>--%>
                                    <%--<div class="quick-success bottom-btn">我已存款</div>--%>
                                    <%--<div class="space-1"></div>--%>
                                    <%--<div class="fast-how bottom-btn">秒存教学</div>--%>
                                <%--</div>--%>

                                <%--<div class="space-5"></div>--%>

                            <%--</div>--%>
                            <%--&lt;%&ndash;<div class="step3" style="display:none;">&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<p class="font2 my-input-title red">您需要存入金额：</p>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<p class="font4 my-input-title red quota"></p>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="space-2"></div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="gray-tips red font2">温馨提示：请您存入该金额，否则存款无法到账，本订单有效时间为2小时</div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="space-2"></div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="tips"><input type="checkbox" class="must-checked" id="checked-w"><label&ndash;%&gt;--%>
                            <%--&lt;%&ndash;for="checked-w">我已明白需要转账&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<span class="quota red"></span> 元</label></div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="tips"><input type="checkbox" class="must-checked1" id="checked-w1"><label&ndash;%&gt;--%>
                            <%--&lt;%&ndash;for="checked-w1">本人已同意，如未转账&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<span class="quota red"></span> 元，导致系统无法匹配存款，天威概不负责！</label></div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="space-2"></div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="fast-next bottom-btn mt10">下一步</div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div class="space-5"></div>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                        <%--</div>--%>

                    </div>

                </div>
            </div>
        </div>


        <!--久安支付-->
        <div class="jiuan-page full-page">
            <header class="common-header j-common-header">
                <div class="header-title">久安支付</div>
            </header>

            <div class="pay-group">
                <div id="jiuan-deposit-page" class="pay-content">
                    <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">

                        <p class="font16 my-input-title step1-title">存款方式</p>
                        <div class="tab-btn subtitle-btn">
                            <a href="javascript:;" class="span stop" id="jiuan-subtitle">
                                久安支付
                                <div class="tab-txt">稳定，资金安全无风险</div>
                                <i class="zeng2">赠2%</i>
                            </a>
                        </div>

                        <div class="tab-contents">
                            <div class="tabdiv">
                                <p class="font16 my-input-title">存款金额 </p>
                                <input type="number" class="my-input money-input"
                                       placeholder="0.00">
                                <p class="input-tips red error-tips">
                                    <%--请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。--%>
                                </p>
                                <%--<p class="input-tips red error-tips">请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。</p>--%>
                                <div class="fast-amount">
                                    <span class="num-btn num-btn100" data-value="50">50</span>
                                    <span class="num-btn num-btn100" data-value="100">100</span>
                                    <span class="num-btn num-btn100" data-value="200">200</span>
                                    <span class="num-btn num-btn500" data-value="500">500</span>
                                    <span class="num-btn num-btn1000" data-value="1000">1000</span>
                                    <span class="num-btn num-btn2000" data-value="2000">2000</span>
                                    <span class="num-btn num-btn3000" data-value="3000">3000</span>
                                    <span class="num-btn otherbtn" data-value="other">其他金额</span>
                                </div>
                                <p class="font16 my-input-title">支付通道</p>
                                <div class="pay-method payway-list"></div>

                                <div class="space-1"></div>

                                <div class="bottom-btn mt10" id="jiuan-confirm">确认支付</div>

                                <div class="space-1"></div>
                                <div class="j-teach-btn bottom-btn mui-btn--yellow"
                                     onclick="window.open('http://www.jiuan365.com','_blank');">
                                    点击下载久安钱包APP
                                </div>

                                <div class="space-1"></div>

                                <%--久安账户通--%>
                                <div class="mui-col-xs32-12 accountJuan clear">
                                    <div class="title">久安账户通</div>
                                    <div class="space-1"></div>
                                    <div class="clear">
                                        <div id="bindingJuan">
                                            <div class="fl">
                                                <p>我已有久安钱包账号：</p>
                                                <p>将我的天威账户绑定久安钱包</p>
                                            </div>
                                            <div class="fr">
                                                <p class="Juanbtn" onclick="bindingJuan()">立即绑定</p>
                                            </div>
                                            <form id="bindJuanForm" action="https://www.9security.com/userBind"
                                                  method="get"
                                                  target="_blank">
                                                <input id="bindId" name="merchantId" type="hidden"/>
                                                <input id="bindUrl" name="notifyUrl" type="hidden"/>
                                                <input id="bindName" name="merchantUserName" type="hidden"/>
                                                <input id="bindcallBack" name="callBackUrl" type="hidden"/>
                                            </form>
                                        </div>
                                        <div class="space-1"></div>
                                        <div id="syncJuan">
                                            <div class="fl">
                                                <p>我没有久安钱包账号：</p>
                                                <p>用我的天威账户同步至久安钱包</p>
                                            </div>
                                            <div class="fr">
                                                <p class="Juanbtn" onclick="syncJuan()">立即同步</p>
                                            </div>
                                            <form id="syncJuanForm" action="https://www.9security.com/quickCreate"
                                                  method="get"
                                                  target="_blank">
                                                <input id="syncId" name="merchantId" type="hidden"/>
                                                <input id="syncUrl" name="notifyUrl" type="hidden"/>
                                                <input id="syncName" name="merchantUserName" type="hidden"/>
                                                <input id="synccallBack" name="callBackUrl" type="hidden"/>
                                            </form>
                                        </div>
                                    </div>

                                    <div class="space-1"></div>

                                    <div class="mui-col-xs32-12 clear">
                                        <p class="font16 my-input-title">温馨提示</p>
                                        <div class="gray-tips">1.请先下载久安钱包进行充值。</div>
                                        <div class="gray-tips">2.人民币：UET比例为1:100。</div>
                                        <div class="gray-tips">3.使用久安钱包进行C2C交易安全无风险。</div>
                                        <div class="gray-tips">4.若存款未到账请联系在线客服。</div>
                                        <div class="gray-tips">5.更多帮助? <a
                                                href="http://www.longdobbs.com/forum.php?mod=viewthread&tid=2823"
                                                target="_blank" style="color: red">查看操作指南</a>。
                                        </div>
                                    </div>
                                </div>
                                <%--久安账户通--%>

                                <div class="space-5"></div>

                            </div>

                        </div>

                    </div>
                </div>
                <div id="jiuan-confirm-page" class="pay-content" style="display:none;">

                    <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">

                        <div class="text-center">

                            <div class="space-3"></div>

                            <img src="/images/deposit/jiuan_logo.jpg" style="width:100px;"/>

                            <div class="space-2"></div>
                            <p>如果您的设备已安装"久安钱包"APP，请点击"继续支付"，将自动跳转支付页面</p>
                            <div class="space-3"></div>
                        </div>

                        <p class="font16 my-input-title">存款信息</p>

                        <div class="text-center">
                            <table class="confirm-table">
                                <tr>
                                    <td>游戏账号</td>
                                    <td class="d-account"></td>
                                </tr>
                                <tr>
                                    <td>存款金额</td>
                                    <td class="d-money"></td>
                                </tr>
                                <tr>
                                    <td>回馈</td>
                                    <td class="d-feedback"></td>
                                </tr>
                                <tr>
                                    <td>所获得</td>
                                    <td class="d-amount"></td>
                                </tr>
                            </table>
                        </div>

                        <div class="space-2"></div>

                        <div class="bottom-btn mt10" id="junaLobby" onclick="junaLobby();"
                             style="display: none">
                            前往久安钱包交易付款
                        </div>
                        <form id="junaLobbyForm"
                              action="https://www.9security.com/autoLogin" method="get"
                              target="_blank">
                            <input id="lobbyId" name="merchantId" type="hidden"/>
                            <input id="lobbyToken" name="token" type="hidden"/>
                        </form>

                        <div class="btn-group">

                            <div class="j-teach-btn bottom-btn mui-btn--yellow"
                                 onclick="window.open('http://www.jiuan365.com','_blank');">
                                下载APP
                            </div>

                            <div class="bottom-btn mt10" id="jiuan-submit">确认支付</div>
                        </div>

                    </div>
                </div>
            </div>

        </div>

        <!--QQ支付-->
        <div class="qq-page full-page">
            <header class="common-header j-common-header">
                <div class="header-title">QQ钱包支付</div>
            </header>
            <div class="pay-content">

                <div id="qq-page" class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">

                    <p class="font16 my-input-title step1-title">存款方式</p>
                    <div class="tab-btn subtitle-btn">
                        <a href="javascript:;" id="qq-subtitle" class="span stop">
                            QQ钱包支付
                            <div class="tab-txt">便捷，最高上限3000</div>

                        </a>
                    </div>

                    <div class="tab-contents">
                        <div class="alipay02 tabdiv">
                            <p class="font16 my-input-title">存款金额 </p>
                            <input type="number" id="qq-money" class="my-input money-input" placeholder="0.00">
                            <p class="input-tips red error-tips">
                                <%--请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。--%>
                            </p>
                            <%--<p class="input-tips red error-tips">请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。</p>--%>
                            <div class="fast-amount">
                                <span class="num-btn num-btn100" data-value="50">50</span>
                                <span class="num-btn num-btn100" data-value="100">100</span>
                                <span class="num-btn num-btn100" data-value="200">200</span>
                                <span class="num-btn num-btn500" data-value="500">500</span>
                                <span class="num-btn num-btn1000" data-value="1000">1000</span>
                                <span class="num-btn num-btn2000" data-value="2000">2000</span>
                                <span class="num-btn num-btn3000" data-value="3000">3000</span>
                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>
                            <p class="font16 my-input-title">支付通道</p>
                            <div class=" zfbQR-reload pay-method payway-list"></div>
                            <div class="space-1"></div>
                            <div class="gray-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <!-- <div class="gray-tips">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取</div> -->
                            <div class="space-1"></div>
                            <div class="space-2"></div>
                            <div class="bottom-btn " id="qq-confirm">确认支付</div>
                            <div class="space-5"></div>
                        </div>

                    </div>

                </div>
            </div>
        </div>

        <!--其他支付-->
        <div class="other-page full-page">
            <header class="common-header j-common-header">
                <div class="header-title">其他支付</div>
            </header>

            <div class="pay-content">
                <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
                    <p class="font16 my-input-title step1-title">存款方式</p>
                    <div class="tab-btn subtitle-btn">
                        <a href="javascript:;" id="jd-subtitle" data-target="jd-page" class="span stop">
                            京东支付
                            <div class="tab-txt">便捷，最高上限3000</div>
                        </a>
                        <a href="javascript:;" id="dcard-subtitle" data-target="dcard-page" class="span stop">
                            点卡支付
                            <div class="tab-txt">便捷，点卡充值</div>
                        </a>
                    </div>

                    <div class="tab-contents">
                        <div id="jd-page" class="alipay01 tabdiv">
                            <p class="font16 my-input-title">存款金额 </p>
                            <input type="number" id="jd-money" class="my-input money-input" placeholder="0.00">
                            <p class="input-tips red error-tips">
                                <%--请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。--%>
                            </p>
                            <%--<p class="input-tips red error-tips">请输入<span id="j-qqMin">1</span>元-<span id="j-qqMax">3000</span>元之间的金额。</p>--%>
                            <div class="fast-amount">
                                <span class="num-btn num-btn100" data-value="50">50</span>
                                <span class="num-btn num-btn100" data-value="100">100</span>
                                <span class="num-btn num-btn100" data-value="200">200</span>
                                <span class="num-btn num-btn500" data-value="500">500</span>
                                <span class="num-btn num-btn1000" data-value="1000">1000</span>
                                <span class="num-btn num-btn2000" data-value="2000">2000</span>
                                <span class="num-btn num-btn3000" data-value="3000">3000</span>

                                <span class="num-btn otherbtn" data-value="other">其他金额</span>
                            </div>
                            <p class="font16 my-input-title">支付通道</p>
                            <div class=" jd-reload pay-method payway-list"></div>
                            <div class="space-1"></div>
                            <div class="gray-tips">若选择的通道无法存款，请点选其他支付通道</div>
                            <!-- <div class="gray-tips">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取</div> -->
                            <div class="space-1"></div>
                            <div class="space-2"></div>
                            <div class="bottom-btn " id="jd-confirm">确认支付</div>
                            <div class="space-5"></div>
                        </div>
                        <div id="dcard-page" class="alipay02 tabdiv">

                            <p class="font16 my-input-title">支付点卡</p>
                            <div class="card-name" id="cardTable">
                                <span class="num-btn on" data-value="50">加载中...</span>
                            </div>
                            <div class="dcard-num j-dcard-num">
                                <p class="font16 my-input-title">选择面额</p>
                                <div class="fast-amount money-list">
                                </div>
                            </div>
                            <p class="font16 my-input-title" id="display-dcard-pay"></p>


                            <div class="tab-contents">
                                <div class="pay-dcard-inputs j-pay-dcard-inputs hide">
                                    <p class="font16 my-input-title">点卡卡号</p>
                                    <input type="text" class="my-input" id="card-no" placeholder="">
                                    <p class="font16 my-input-title">点卡密码</p>
                                    <input type="text" class="my-input" id="card-password" placeholder="">

                                </div>
                                <p class="error-tips dcard-pwd-error-message"></p>

                                <div class="space-2"></div>
                                <div class="bottom-btn card-submit">确定支付</div>
                                <div class="bottom-btn mui-btn--gray mt20 dcardFeeBtn">查看费率表</div>
                                <div class="space-5"></div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--点卡费率表说明-->
        <div id="dcardfee-page" class="full-page">
            <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">

                <div class="space-2"></div>
                <h3 class="text-center">点卡费率表</h3>
                <div class="space-2"></div>

                <table id="cardFeeTable">
                    <tbody>
                    <tr>
                        <th>点卡类型</th>
                        <th>费率</th>
                        <th>存100实际到账</th>
                    </tr>
                    <tr>
                        <td align="center">移动神州卡</td>
                        <td align="center">0.05</td>
                        <td align="center">95.00</td>
                    </tr>
                    <tr>
                        <td align="center">电信国卡</td>
                        <td align="center">0.05</td>
                        <td align="center">95.00</td>
                    </tr>
                    <tr>
                        <td align="center">联通一卡通</td>
                        <td align="center">0.05</td>
                        <td align="center">95.00</td>
                    </tr>
                    <tr>
                        <td align="center">征途一卡通</td>
                        <td align="center">0.13</td>
                        <td align="center">87.00</td>
                    </tr>
                    <tr>
                        <td align="center">QQ币充值卡</td>
                        <td align="center">0.14</td>
                        <td align="center">86.00</td>
                    </tr>
                    <tr>
                        <td align="center">完美一卡通</td>
                        <td align="center">0.14</td>
                        <td align="center">86.00</td>
                    </tr>
                    <tr>
                        <td align="center">骏网一卡通</td>
                        <td align="center">0.16</td>
                        <td align="center">84.00</td>
                    </tr>
                    <tr>
                        <td align="center">盛付通一卡通</td>
                        <td align="center">0.17</td>
                        <td align="center">83.00</td>
                    </tr>
                    <tr>
                        <td align="center">天宏一卡通</td>
                        <td align="center">0.17</td>
                        <td align="center">83.00</td>
                    </tr>
                    <tr>
                        <td align="center">搜狐一卡通</td>
                        <td align="center">0.16</td>
                        <td align="center">84.00</td>
                    </tr>
                    <tr>
                        <td align="center">久游一卡通</td>
                        <td align="center">0.2</td>
                        <td align="center">80.00</td>
                    </tr>
                    <tr>
                        <td align="center">纵游一卡通</td>
                        <td align="center">0.16</td>
                        <td align="center">84.00</td>
                    </tr>
                    <tr>
                        <td align="center">网易一卡通</td>
                        <td align="center">0.14</td>
                        <td align="center">86.00</td>
                    </tr>
                    </tbody>
                </table>

                <div class="space-2"></div>
                <div class="bottom-btn" onclick="returnDcardPage();">返回</div>
                <div class="space-5"></div>

            </div>
        </div>

        <!--微信秒存转帐说明-->
        <div id="wechat-desc-page" class="full-page">
            <div class="mui-col-xs32-11 mui-col-xs32-offset-1 mui-col-xs64-5">
                <div class="relative">
                    <div class="btn-copy" onclick="returnWechatPage();">返回</div>
                </div>
                <div class="space-2"></div>
                <h3 class="text-center">微信转账流程</h3>

                <div class="space-2"></div>
                <p>步骤一：点击微信【钱包】</p>
                <div><img src="/images/deposit/img1.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤二：点击微信【钱包】-【收付款】页面</p>
                <div><img src="/images/deposit/img2.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤三：选择【转账到银行卡功能】</p>
                <div><img src="/images/deposit/img3.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤四：对应填写获取到的收款账户名与收款卡号</p>
                <div><img src="/images/deposit/img4.jpg"/></div>

                <div class="space-2"></div>
                <p>步骤五：务必填写系统生成的存款金额，方可自动到账</p>
                <div><img src="/images/deposit/img5.jpg"/></div>

                <div class="space-2"></div>
                <div class="bottom-btn" onclick="returnWechatPage();">返回</div>
                <div class="space-5"></div>
            </div>
        </div>

    </div>
</div>
</body>
</html>

<input type="hidden" value="${session.customer.level}" id="j-num-level"/>
<input type="hidden" value="${session.customer.loginname}" id="j-loginName"/>
<input type="hidden" id="fast-real-money">

<script type="text/javascript" src="/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/js/layer/mobile/layer.js"></script>
<script type="text/javascript" src="/mobile/js/clip.js"></script>
<script type="text/javascript" src="/mobile/app/js/deposit.js?v=90000002"></script>
<script type="text/javascript" src="/js/syncJiuan.js?v=10"></script>
<script>

    var mobileorApp = window.location.href;

    //j-come-back 返回的判断
    $(window).load(function () {
        if (mobileorApp.indexOf('openMobile') > -1) {
            $(".j-come-back").html("<i class='left-button deposit-sprites arrow' onclick='history.back()'></i><div class='header-title'>快速存款</div>");
        } else {
            $(".j-come-back").html("<i class='left-button deposit-sprites arrow' onclick='goBackApp();'></i><div class='header-title'>快速存款</div>");
        }
    });

    function goBackApp() {
        if (mobileorApp.indexOf('openMobile') > -1) {
            window.location.href = "/mobile/new/index.jsp"
        } else {
            window.location.href = 'longduwebapp://Home';
        }
    }

    $('#courseGif').on('click', function () {
        $('.ezgif-box').slideDown('400', function () {
        });
    })
    $('#closeGif').on('click', function () {
        $('.ezgif-box').slideUp('400', function () {
        });
    })

</script>