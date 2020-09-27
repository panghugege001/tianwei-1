<%@page import="java.util.GregorianCalendar" %>
<%@page import="java.util.Calendar" %>
<%@page import="dfh.utils.Constants" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dfh.model.Users" %>


<div class="user_cun">
    <ul>
        <li class="active">
            <a href="#tab-one" data-toggle="tab" aria-expanded="true">存款</a>
        </li>
    </ul>
</div>
<div class="list list1 on">
    <div id="box1" style="min-height: 455px;">
        <span class="money_type">支付方式</span>
        <ul id="money_in" class="money_in1 clearfix">
            <li id="deposit-zfb">
                <div class="wrap-box">
                    <div class="mask"></div>
                    支付宝
                </div>
            </li>
            <li id="deposit-bank">
                <div class="wrap-box">
                    <div class="mask"></div>
                    银行卡
                </div>
            </li>
            <li id="deposit-wexin">
                <div class="wrap-box">
                    <div class="mask"></div>
                    微信支付
                    
                </div>
            </li>

            <li id="deposit-jiuan">
                <div class="wrap-box">
                    <div class="mask"></div>
                    久安支付
                    
                </div>
            </li>

            <li id="deposit-qq">
                <div class="wrap-box">
                    <div class="mask"></div>
                    QQ钱包
                </div>
            </li>

            <li id="deposit-other">
                <div class="wrap-box">
                    <div class="mask"></div>
                    其他支付
                </div>
            </li>

            <%--<li id="deposit-cloud">--%>
            <%--<div class="wrap-box">--%>
            <%--<div class="mask"></div>--%>
            <%--云闪付<span class="fan-btn">赠</span>--%>
            <%--</div>--%>
            <%--</li>--%>

        </ul>
        <div class="items pay-list">

            <!-- 支付宝 -->
            <div class="item item1">
                <table>
                    <tr>
                        <td class="sub-title">存款方式</td>
                        <td class="pay-box-list" colspan="2">
                            <div id="deposit-zfbfast-subtitle" data-target="deposit-zfbfast-page"
                                 class="pay-box active" data-bonus="1.5%"
                                 data-min="1" data-max="3000000">
                                <div class="zhi_box">
                                    <img src="images/user/zhi.png"/>
                                </div>
                                <div class="wrap-box">
                                    <span class="zh1">赠1.5%</span>
                                    <h3>支付宝转账</h3>
                                    <span>稳定，单笔上限300万</span>
                                </div>
                            </div>
                            <div id="deposit-zfbQR-subtitle" data-target="deposit-zfbQR-page"
                                 class="pay-box">
                                <div class="zhi_box">
                                    <img src="images/user/zhi.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>支付宝扫码支付</h3>
                                    <span>便捷，适合小额存款</span>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>

                <div id="deposit-zfbfast-page" class="fastpay tab-panel active">
                    <div class="deposit-fastpay-1">
                        <table>
                            <tr>
                                <td class="sub-title">存款通道</td>
                                <td colspan="2">
                                    <div class="payway-list">
                                        <div class="payWayBtn active" data-max="3000000"
                                             data-min="1" data-value="2">
                                            <span class="text">通道1</span>
                                        </div>

                                        <div class="payWayBtn" data-max="3000000"
                                        data-min="1" data-value="22">
                                        <span class="text">通道2</span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="sub-title">存款姓名</td>
                                <td class="wd260"><input type="text" class="input fastpay-username"
                                                         maxlength="10" placeholder="您转账支付宝的真实姓名">
                                </td>
                                <td><span class="red fastpay-name-error-message hidden"
                                          style="font-size:14px;">您转账支付宝的真实姓名。</span>
                                </td>
                            </tr>

                            <tr>
                                <td class="sub-title">存款金额</td>
                                <td class="wd260">
                                    <input type="text" id="zhifubaoq" class="money-input input"
                                           maxlength="10" placeholder="1元-300万">
                                </td>
                                <td>
                                                        <span class="red money-error-message hidden"
                                                              style="font-size:14px;">请输1元-300万之间的金额。</span>
                                </td>
                            </tr>
                            <%--<tr>--%>
                            <%--<td></td>--%>
                            <%--<td colspan="2" class="zhifubao_money">--%>
                            <%--<div class="zhifubao_type">--%>
                            <%--<ul>--%>
                            <%--<li>10</li>--%>
                            <%--<li>500</li>--%>
                            <%--<li>1000</li>--%>
                            <%--<li>3000</li>--%>
                            <%--<li>其他金额</li>--%>
                            <%--</ul>--%>
                            <%--</div>--%>
                            <%--</td>--%>
                            <%--</tr>--%>
                            <tr>
                                <td class="sub-title">实际到账</td>
                                <td>
                                    <div class="money-pay">0元</div>
                                </td>
                            </tr>

                            <tr>
                                <td class="sub-title"></td>
                                <td colspan=""><input type="button"
                                                      class="btn orange-btn fastpay-next-btn public_btn"
                                                      value="获取收款账号"></td>
                            </tr>
                            <tr>
                                <td colspan="3" class="tip-box"><span class="dark">支付宝转账每日23:20-01:00左右有延迟到帐现象，若转账状态成功后仍未到账，请咨询客服。</span>
                                </td>
                            </tr>
                            <!--                                                 <tr>
                                <td colspan="3" class="tip-box"><span class="dark"
                                                                      style="color: red;">使用支付宝秒存转账赠1.5%，满300再赠免费红包</span>
                                </td>
                            </tr> -->
                            </tbody>
                        </table>
                    </div>

                    <div class="deposit-fastpay-2 hidden">
                        <div class="clearfix" style="margin: 15px 0px;">
                            <div class="fl sub-title">存款信息</div>
                            <div class="fl cun_massage">
                                <table class="table1">
                                    <tr>
                                        <th>存款姓名</th>
                                        <th>存款金额</th>
                                    </tr>
                                    <tr>
                                        <td class="fastpay-confirm-username"></td>
                                        <td class="fastpay-confirm-money"></td>

                                    </tr>
                                </table>
                            </div>
                        </div>

                        <div class="space-1"></div>

                        <div class="clearfix">
                            <div class="fl sub-title">收款账号</div>
                            <div class="fl">
                                <table class="table1 table-deposit" id="shou_zfb">

                                    <tr>
                                        <th>收款银行</th>
                                        <th>收款卡号</th>
                                        <th>收款姓名</th>

                                    </tr>
                                    <tr>
                                        <td class="relative"><span class="sbankname"></span></td>
                                        <td>
                                            <span class="saccountno"></span>
                                            <a class="copy copy1" data-clipboard-action="copy"
                                               data-clipboard-text="">复制</a>
                                        </td>
                                        <td>
                                                                <span class="saccountname"
                                                                      style="padding-top:10px;"></span>
                                            <a class="copy copy1"
                                               style="margin-top:7px; display: inline-block;"
                                               data-clipboard-action="copy" data-clipboard-text="">复制</a>
                                            <a href="https://shenghuo.alipay.com/transfercore/fill.htm"
                                               target="_blank" id="zhuanzhang_a">
                                                <div class="payWayBtn noactive"
                                                     style="float:right;">
                                                    <span class="text text-zfb">去转账</span>
                                                </div>
                                            </a>
                                        </td>
                                        <p>（仅本次有效，下次存款请重新获取）</p>
                                    </tr>

                                </table>

                            </div>
                            <div class="clearfix"></div>
                            <div style="margin: 30px 0px; padding-left: 70px;">

                                <input type="button" value="重新填写" id="no_iszz"
                                       class="deposit-btn2 fastpay-return-btn">

                                <input type="button" value="我已成功转账" id="ok_iszz"
                                       class="deposit-btn2 fastpay-success-btn orange-btn">

                                <input type="button" value="存款失败，重新获取新的收款账户"
                                       class="deposit-btn2 fastpay-renew-btn orange-btn">

                            </div>
                            <div class="prompt-info" style="padding-left: 20px; float: left;">
                                <h3 class="tit c-huangse">温馨提示</h3>
                                <p>1.【支付宝支付】最低存款金额10元，单笔最高3000元。</p>
                                <p>2.【支付宝支付】第三方需收取0.9%-2.5%手续费。</p>
                                <p>3.【支付宝支付】手机扫码后请长按二维码，点击识别图中二维码进行识别。</p>
                            </div>
                        </div>

                    </div>

                    <div class="deposit-fastpay-3 hidden">
                        <p class="tip1">存款确认中</p>
                        <p>您的存款正在核实中........</p>
                        <p class="tip2">
                            请核对存款信息是否一致，如果信息一致，款项通常会在10分钟内到账，您可以点击刷新余额查看。如果超时未到账，请联系
                            <a href="javascript:;" target="_blank" onclick="getCsOnDuty();"
                               class="avt">在线客服</a>，进行咨询。
                        </p>
                        <table class="table1 j-count-table">
                            <tr>
                                <td>存款信息</td>
                                <td>预计倒计时</td>
                            </tr>
                            <tr>
                                <td>存款金额：<span class="fastpay-confirm-money"></span>
                                </td>
                                <td rowspan="6" class="text-center"><span
                                        class="fastpay-time">10:00</span></td>
                            </tr>
                            <tr>
                                <td>存款姓名：<span class="fastpay-confirm-username"></span>
                                </td>
                            </tr>
                            <tr>
                                <td>存款方式：<span>支付宝转账</span></td>
                            </tr>
                            <%--
                            <tr>--%>
                            <%--
                            <td>订单时间：<span></span></td>
                            --%>
                            <%--
                        </tr>
                        --%>
                        </table>

                        <table class="table1 j-cs-table hidden">
                            <tr>
                                <td>存款信息</td>
                                <td class="noborderBottom pl30">如果您已经正确存款：</td>
                            </tr>
                            <tr>
                                <td>存款金额：<span class="fastpay-confirm-money"></span>
                                </td>
                                <td class="text-center noborderBottom noborderTop">
                                    <input type="button" value="联系在线客服" class="deposit-btn2"
                                           onclick="getCsOnDuty();"></td>
                            </tr>
                            <tr>
                                <td>存款姓名：<span class="fastpay-confirm-username"></span>
                                </td>
                                <td class="noborderBottom noborderTop pl30">如果您未存款
                                </td>
                            </tr>
                            <tr>
                                <td>存款方式：<span>支付宝转账</span></td>
                                <td class="text-center noborderTop pl3">
                                    <a href="javascript:;" class="fastpay-return-btn">返回重新存款>></a>
                                </td>
                            </tr>
                            <%--
                            <tr>--%>
                            <%--
                            <td>订单时间：<span></span></td>
                            --%>
                            <%--
                        </tr>
                        --%>
                        </table>

                        <div class="text-center" style="margin:15px; ">
                            <input type="button" value="我还未存款，返回重新填写"
                                   class="deposit-btn3 fastpay-return-btn">
                        </div>
                    </div>

                    <div class="deposit-fastpay-4 hidden">
                        <div class="text-center" style="margin:50px; ">
                            <%--<img src="/images/fastpay-success.png"/>--%>
                        </div>

                        <div class="text-center" style="margin:50px;">
                            <input type="button" value="确定" class="deposit-btn3 fastpay-return-btn">
                        </div>
                    </div>

                    <div class="deposit-fastpay-10 hidden">
                        <div class="text-center relative">
                            <h3 class="red">您需要存入金额：</h3>
                            <h1 class="real-save-money red mt20 mb25"></h1>
                            <h3 class="red mb25">温馨提示：请您存入该金额，否则存款无法到账<br/>本订单有效时间为2小时</h3>
                            <div class="tips"><input type="checkbox" class="must-checked">我已明白需要转账
                                <span
                                        class="save-money red"></span> 元
                            </div>
                            <div class="tips"><input type="checkbox" class="must-checked1">本人已同意，如未转账
                                <span
                                        class="save-money red"></span> 元，导致系统无法匹配存款，天威概不负责！
                            </div>
                            <input type="button" class="deposit-btn2 orange-btn"
                                   value="下一步" onclick="jumpNext(this)">
                        </div>
                    </div>
                </div>

                <div id="deposit-zfbQR-page" class="tab-panel">
                    <table>
                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list deposit-zfbQR2-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"> <span class="brown tip-box-top">
                                                                        若选择的通道无法存款，请点选其他支付通道
                                                                    </span></td>
                        </tr>
                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="10-3000">
                            </td>
                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-zfbQR2-submit"
                                                   class="btn orange-btn" value="生成二维码"></td>
                        </tr>
                    </table>
                    <div class="prompt-info" style="padding-left: 20px; float: left;">
                        <h3 class="tit c-huangse">温馨提示</h3>
                        <p>
                            【支付宝支付】因为支付通道限制，支付宝支付通道需要扣除手续1%-4.5%手续费，不同支付通道收取的费率不同，此费用由第三方收取。
                        </p>
                    </div>
                </div>
            </div>

            <!-- 银行卡 -->
            <div class="item item4">
                <table>
                    <tr>
                        <td class="sub-title">存款方式</td>
                        <td class="pay-box-list" colspan="2" id="cunkfs">
                            <div id="deposit-fastpay-subtitle" data-target="deposit-fastpay-page"
                                 data-bonus="1.5%" data-min="1" data-max="3000000"
                                 class="pay-box active">
                                <div class="shouji_box">
                                    <img src="images/user/shouji.png"/>
                                </div>
                                <div class="wrap-box">
                                    <span class="zh1">赠1.5%</span>
                                    <h3>手机/网银转账</h3>
                                    <span>已开通手机网银的用户</span>
                                    <!--<div class="coupon">返赠1%</div>-->
                                </div>
                            </div>
                            <div id="deposit-yinlian-subtitle" data-target="deposit-yinlian-page"
                                 class="pay-box stop">


                                <div class="zhi_box" style="    padding: 15px 0px 0px 5px;">
                                    <img src="images/user/UP-icon.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>银联支付</h3>
                                    <span>任意银联APP可扫码</span>
                                </div>

                            </div>
                            <div id="deposit-thirdpay-subtitle" data-target="deposit-thirdpay-page"
                                 class="pay-box">
                                <div class="yinhang_box">
                                    <img src="images/user/yinhang.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>在线支付</h3>
                                    <span>已开通网银的用户</span>
                                </div>
                            </div>
                            <div id="deposit-speedpay-subtitle" data-target="deposit-speedpay-page"
                                 class="pay-box stop">
                                <div class="kuaijie_box">
                                    <img src="images/user/dian.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>快捷支付</h3>
                                    <span>验证身份证号、短信即可</span>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>

                <div id="deposit-fastpay-page" class="fastpay tab-panel active">
                    <div class="deposit-fastpay-1">
                        <table>
                            <tr>
                                <td class="sub-title">存款通道</td>
                                <td colspan="2">
                                    <div class="payway-list">
                                        <div class="payWayBtn active" data-max="3000000"
                                             data-min="1" data-value="1">
                                            <span class="text">通道1</span>
                                        </div>

                                        <%--<div class="payWayBtn" data-max="3000000"--%>
                                        <%--data-min="1" data-value="7">--%>
                                        <%--<span class="text">通道2</span>--%>
                                        <%--</div>--%>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="sub-title">存款姓名</td>
                                <td class="wd260"><input type="text" class="input fastpay-username"
                                                         maxlength="10" placeholder="您转账的真实姓名">
                                </td>
                                <td><span class="red fastpay-name-error-message hidden"
                                          style="font-size:14px;">您转账的真实姓名。</span>
                                </td>
                            </tr>
                            <tr>
                                <td class="sub-title">存款金额</td>
                                <td class="wd260"><input type="text" class="money-input input"
                                                         maxlength="10" placeholder="1元-300万">
                                </td>
                                <td>
                                                        <span class="red money-error-message hidden"
                                                              style="font-size:14px;">请输1元-300万之间的金额。</span>
                                </td>
                            </tr>

                            <%--<tr>--%>
                            <%--<td></td>--%>
                            <%--<td colspan="2">--%>
                            <%--<div class="money-list btn-list">--%>
                            <%--<input type="button" data-value="10" value="10">--%>
                            <%--<input type="button" data-value="500" value="500">--%>
                            <%--<input type="button" data-value="1000" value="1000"--%>
                            <%--class="">--%>
                            <%--<input type="button" data-value="3000" value="3000"--%>
                            <%--class="">--%>
                            <%--<input type="button" data-value="other" value="其他金额">--%>
                            <%--</div>--%>
                            <%--</td>--%>
                            <%--</tr>--%>
                            <%--<tr>--%>
                            <%--<td></td>--%>
                            <%--<td colspan="2" class="tip-box"><span>输入并存入带2位小数的金额，可更快上分</span>--%>
                            <%--</td>--%>
                            <%--</tr>--%>
                            <tr>
                                <td class="sub-title">实际到账</td>
                                <td>
                                    <div class="money-pay">0元</div>
                                </td>
                            </tr>

                            <!--                                                 <tr>
                                <td></td>
                                <td colspan="2" class="tip-box"><span style="color: red;">使用网银秒存转账赠1.5%，满300再赠免费红包</span>
                                </td>
                            </tr> -->
                            <tr>
                                <td class="sub-title"></td>
                                <td colspan="2"><input type="button"
                                                       class="btn orange-btn fastpay-next-btn"
                                                       value="获取收款账号"></td>
                            </tr>

                            </tbody>
                        </table>
                    </div>

                    <div class="deposit-fastpay-2 hidden">
                        <div class="clearfix" style="margin: 15px 0px;">
                            <div class="fl sub-title">存款信息</div>
                            <div class="fl cun_massage">
                                <table class="table1">
                                    <tr>
                                        <th>存款姓名</th>
                                        <th>存款金额</th>
                                    </tr>
                                    <tr>
                                        <td class="fastpay-confirm-username"></td>
                                        <td class="fastpay-confirm-money bigger"></td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                        <div class="space-1"></div>

                        <div class="clearfix">
                            <div class="fl sub-title">收款账号</div>
                            <div class="fl">
                                <table class="table1 table-deposit">
                                    <tr>
                                        <td colspan="2" class="red noborder vertical-align"
                                            style="padding: 0px;">
                                            <p>（仅本次有效，下次存款请重新获取）</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>收款银行</th>
                                        <th>收款卡号</th>
                                        <th class="fastpay-area" style="display:none;">开户地区</th>
                                        <th>收款姓名</th>
                                    </tr>
                                    <tr>
                                        <td class="relative"><span class="sbankname"></span></td>
                                        <td>
                                            <span class="saccountno"></span>
                                            <a class="copy copy1" data-clipboard-action="copy"
                                               data-clipboard-text="">复制</a>
                                        </td>
                                        <td class="fastpay-area" style="display:none;">
                                            <span class="sacarea"></span>
                                        </td>
                                        <td>
                                            <span class="saccountname"></span>
                                            <a class="copy copy1" data-clipboard-action="copy"
                                               data-clipboard-text="">复制</a>
                                        </td>
                                    </tr>
                                </table>

                            </div>
                            <div class="clearfix"></div>
                            <div style="margin:15px 0px;">

                                <input type="button" value="重新填写"
                                       class="deposit-btn2 fastpay-return-btn">

                                <input type="button" value="我已成功转账"
                                       class="deposit-btn2 fastpay-success-btn orange-btn">

                                <input type="button" value="存款失败，重新获取新的收款账户"
                                       class="deposit-btn2 fastpay-renew-btn orange-btn">

                            </div>
                        </div>

                    </div>

                    <div class="deposit-fastpay-3 hidden">
                        <p class="tip1">存款确认中</p>
                        <p>您的存款正在核实中........</p>
                        <p class="tip2">
                            请核对存款信息是否一致，如果信息一致，款项通常会在10分钟内到账。如果超时未到账，请联系
                            <a href="javascript:;" target="_blank" onclick="getCsOnDuty();"
                               class="avt">在线客服</a>，进行咨询。
                        </p>
                        <table class="table1 j-count-table">
                            <tr>
                                <td>存款信息</td>
                                <td>预计倒计时</td>
                            </tr>
                            <tr>
                                <td>存款金额：<span class="fastpay-confirm-money"></span>
                                </td>
                                <td rowspan="6" class="text-center"><span
                                        class="fastpay-time">10:00</span></td>
                            </tr>
                            <tr>
                                <td>存款姓名：<span class="fastpay-confirm-username"></span>
                                </td>
                            </tr>
                            <tr>
                                <td>存款方式：<span>手机/网银转账</span></td>
                            </tr>
                            <%--
                            <tr>--%>
                            <%--
                            <td>订单时间：<span></span></td>
                            --%>
                            <%--
                        </tr>
                        --%>
                        </table>

                        <table class="table1 j-cs-table hidden">
                            <tr>
                                <td>存款信息</td>
                                <td class="noborderBottom pl30">如果您已经正确存款：</td>
                            </tr>
                            <tr>
                                <td>存款金额：<span class="fastpay-confirm-money"></span>
                                </td>
                                <td class="text-center noborderBottom noborderTop">
                                    <input type="button" value="联系在线客服" class="deposit-btn2"
                                           onclick="getCsOnDuty();"></td>
                            </tr>
                            <tr>
                                <td>存款姓名：<span class="fastpay-confirm-username"></span>
                                </td>
                                <td class=" noborderBottom noborderTop pl30">
                                    如果您未存款：
                                </td>
                            </tr>
                            <tr>
                                <td>存款方式：<span>手机/网银转账</span></td>
                                <td class="text-center  noborderTop pl30">
                                    <a href="javascript:;" class="fastpay-return-btn">返回重新存款>></a>
                                </td>
                            </tr>
                            <%--
                            <tr>--%>
                            <%--
                            <td>订单时间：<span></span></td>
                            --%>
                            <%--
                        </tr>
                        --%>
                        </table>

                        <div class="text-center" style="margin:15px; ">
                            <input type="button" value="我还未存款，返回重新填写"
                                   class="deposit-btn3 fastpay-return-btn">
                        </div>
                    </div>

                    <div class="deposit-fastpay-4 hidden">
                        <div class="text-center" style="margin:50px; ">
                            <%--<img src="/images/fastpay-success.png"/>--%>
                        </div>

                        <div class="text-center" style="margin:50px;">
                            <input type="button" value="确定" class="deposit-btn3 fastpay-return-btn">
                        </div>
                    </div>

                    <div class="deposit-fastpay-10 hidden">
                        <div class="text-center relative">
                            <h3 class="red">您需要存入金额：</h3>
                            <h1 class="real-save-money red mt20 mb25"></h1>
                            <h3 class="red mb25">温馨提示：请您存入该金额，否则存款无法到账<br/>本订单有效时间为2小时</h3>
                            <div class="tips"><input type="checkbox" class="must-checked">我已明白需要转账
                                <span
                                        class="save-money red"></span> 元
                            </div>
                            <div class="tips"><input type="checkbox" class="must-checked1">本人已同意，如未转账
                                <span
                                        class="save-money red"></span> 元，导致系统无法匹配存款，天威概不负责！
                            </div>
                            <input type="button" class="deposit-btn2 orange-btn"
                                   value="下一步" onclick="jumpNext(this)">
                        </div>
                    </div>
                </div>

                <div id="deposit-yinlian-page" class="tab-panel">
                    <table>
                        <tbody>

                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-yinlian-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"><span
                                    class="brown tip-box-top">若选择的通道无法存款，请点选其他支付通道</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1-3000">
                            </td>

                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list  btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-yinlian-submit"
                                                   class="btn orange-btn" value="生成二维码">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" class="tip-box">
                                <p class="dark">1、银联扫码，可使用任意银联APP或云闪付APP扫码支付。</p>
                                <p class="dark">2、需要承担<span class="payway-fee">1.2</span>%手续费，费用有第三方收取。

                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <%--<p class="dark">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取。</p>--%>
                </div>

                <div id="deposit-thirdpay-page" class="tab-panel">
                    <table>

                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-thirdPay-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"><span class="brown tip-box-top">
                                                                        若选择的通道无法存款，请点选其他支付通道
                                                                    </span></td>
                        </tr>

                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1元-1万">
                            </td>

                            <td>
                                <span class="red money-error-message hidden">请输1元-1万之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list  btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title vertical-align"
                                style="position: relative;top: 14px;">支付银行
                            </td>
                            <td colspan="2">
                                <div id="bank-list" class="btn-list"></div>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><span class="red bank-error-message hidden" style="font-size:14px;">请选择支付银行</span>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-thirdpay-submit"
                                                   class="btn orange-btn" value="去支付"></td>
                        </tr>

                        </tbody>
                    </table>
                </div>

                <div id="deposit-speedpay-page" class="tab-panel">
                    <table>
                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-speedPay-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"> <span class="brown tip-box-top">
                                                                        若选择的通道无法存款，请点选其他支付通道
                                                                    </span></td>
                        </tr>
                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1-3000">
                            </td>
                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list  btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr class="mbkj hidden">
                            <td class="sub-title">银行卡号</td>
                            <td class="wd260"><input type="text"
                                                     class="input"
                                                     id="speedpay-bankcard"
                                                     placeholder="银行卡号"
                                                     onkeyup="value=value.replace(/[^\d]/g,'');">
                            </td>
                        </tr>
                        <tr class="mbkj hidden">
                            <td class="sub-title">银行户名</td>
                            <td class="wd260"><input type="text"
                                                     class="input"
                                                     id="speedpay-bankname"
                                                     placeholder="银行户名">
                            </td>
                        </tr>
                        <tr class="mbkj hidden">
                            <td class="sub-title">手机号</td>
                            <td class="wd260"><input type="text"
                                                     class="input"
                                                     id="speedpay-phoneNumber"
                                                     placeholder="手机号"
                                                     onkeyup="value=value.replace(/[^\d]/g,'');"
                                                     maxlength="11">
                            </td>
                        </tr>

                        <tr class="mifkj hidden">
                            <td class="sub-title">银行卡号</td>
                            <td class="wd260"><input type="text"
                                                     class="input"
                                                     id="mifkj-bankcard"
                                                     placeholder="银行卡号"
                                                     onkeyup="value=value.replace(/[^\d]/g,'');">
                            </td>
                        </tr>
                        <tr class="mifkj hidden">
                            <td class="sub-title">银行户名</td>
                            <td class="wd260"><select id="mifkj-bankname" class="input"></select>
                            </td>
                        </tr>

                        <tr class="dbkj hidden">
                            <td class="sub-title">银行户名</td>
                            <td class="wd260"><select id="dbkj-bankname" class="input"></select>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>

                            <td colspan="2"><input type="button" id="deposit-speedpay-submit"
                                                   class="btn orange-btn" value="去支付"></td>
                        </tr>

                        </tr>

                    </table>

                </div>

            </div>

            <!--微信支付-->
            <div class="item item2" id="weixinbox">
                <div class="one">
                    <table>
                        <tr>
                            <td class="sub-title">存款方式</td>
                            <td class="pay-box-list" colspan="2">

                                <div id="deposit-wexinQR-subtitle"
                                     data-target="deposit-wexinQR-page" class="pay-box ">
                                    <div class="zhi_box">
                                        <img src="images/user/weixin_inco.png"/>
                                    </div>
                                    <div class="wrap-box">
                                        <h3>微信扫码支付</h3>
                                        <span>便捷，最高上限3000</span>
                                    </div>
                                </div>

                                <%--<div id="deposit-wexinfast-subtitle"--%>
                                     <%--data-target="deposit-wexinfast-page" class="pay-box"--%>
                                     <%--data-bonus="1.5%" data-min="1"--%>
                                     <%--data-max="3000000">--%>
                                    <%--<div class="zhi_box">--%>
                                        <%--<img src="images/user/weixin_inco.png"/>--%>
                                    <%--</div>--%>
                                    <%--<div class="wrap-box">--%>
                                        <%--<span class="zh1">赠1.5%</span>--%>
                                        <%--<h3>微信转账</h3>--%>
                                        <%--<span>稳定，单笔上限300万</span>--%>
                                        <%--&lt;%&ndash;--%>
                                        <%--<div class="coupon">返赠1%</div>--%>
                                        <%--&ndash;%&gt;--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            </td>
                        </tr>
                    </table>


                    <div id="deposit-wexinQR-page" class="tab-panel ">
                        <table>
                            <tr>
                                <td class="sub-title">支付通道</td>
                                <td colspan="2">
                                    <div class="payway-list  deposit-wexinQR-reload"></div>
                                </td>
                            </tr>
                            <tr class="payway-list-box">
                                <td></td>
                                <td class="tip-box"> <span class="brown tip-box-top">
                                                                        若选择的通道无法存款，请点选其他支付通道
                                                                    </span></td>
                            </tr>
                            <tr>
                                <td class="sub-title">存款金额</td>
                                <td class="wd260"><input type="text" class="money-input input"
                                                         maxlength="10" placeholder="1-3000">
                                </td>
                                <td>
                                                        <span class="red money-error-message hidden"
                                                              style="font-size:14px;">请输1-3000之间的金额。</span>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <div class="money-list btn-list">
                                        <input type="button" data-value="10" value="10">
                                        <input type="button" data-value="500" value="500">
                                        <input type="button" data-value="1000" value="1000">
                                        <input type="button" data-value="3000" value="3000">
                                        <input type="button" data-value="other" value="其他金额">
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="sub-title">实际到账</td>
                                <td>
                                    <div class="money-pay">0元</div>
                                </td>
                            </tr>

                            <tr>
                                <td class="sub-title"></td>
                                <td colspan="2"><input type="button" id="deposit-wexinQR-submit"
                                                       class="btn orange-btn" value="生成二维码"></td>
                            </tr>
                            <tr>
                                <td colspan="3" class="tip-box">
                                    <span class="dark">* 因为支付通道限制，微信支付通道需要扣除手续1%—5%手续费。</span><br>
                                    <span class="dark">* 不同支付通道收取的费率不同，此费用由第三方收取。</span><br>

                                </td>
                            </tr>

                            </tbody>
                        </table>

                        <%--<p class="dark" style="padding-left: 76px;">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取。</p>--%>
                    </div>

                    <%--<div id="deposit-wexinfast-page" class="tab-panel">--%>
                        <%--<div class="deposit-fastpay-1">--%>
                            <%--<table>--%>
                                <%--<tr>--%>
                                    <%--<td class="sub-title">存款通道</td>--%>
                                    <%--<td colspan="2">--%>
                                        <%--<div class="payway-list">--%>
                                            <%--<div class="payWayBtn active" data-max="3000000"--%>
                                                 <%--data-min="1" data-value="4">--%>
                                                <%--<span class="text">通道1</span>--%>
                                            <%--</div>--%>

                                            <%--&lt;%&ndash;<div class="payWayBtn" data-max="3000000"&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;data-min="1" data-value="7">&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;<span class="text">通道2</span>&ndash;%&gt;--%>
                                            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                                        <%--</div>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td class="sub-title">存款姓名</td>--%>
                                    <%--<td class="wd260"><input type="text"--%>
                                                             <%--class="input fastpay-username"--%>
                                                             <%--maxlength="10"--%>
                                                             <%--placeholder="您转账微信的真实姓名">--%>
                                    <%--</td>--%>
                                    <%--<td><span--%>
                                            <%--class="red fastpay-name-error-message hidden"--%>
                                            <%--style="font-size:14px;">您转账微信的真实姓名。</span>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>

                                <%--<tr>--%>
                                    <%--<td class="sub-title">存款金额</td>--%>
                                    <%--<td class="wd260"><input type="text"--%>
                                                             <%--class="money-input input"--%>
                                                             <%--maxlength="10"--%>
                                                             <%--placeholder="1元-300万">--%>
                                    <%--</td>--%>
                                    <%--<td>--%>
                                                                        <%--<span class="red money-error-message hidden"--%>
                                                                              <%--style="font-size:14px;">请输1元-300万之间的金额。</span>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<td class="sub-title">实际到账</td>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<td>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<div class="money-pay">0元</div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</td>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>

                                <%--<tr>--%>
                                    <%--<td class="sub-title"></td>--%>
                                    <%--<td colspan="2"><input type="button"--%>
                                                           <%--class="btn orange-btn fastpay-next-btn"--%>
                                                           <%--value="获取收款账号"></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td colspan="3" class="tip-box"><span--%>
                                            <%--class="dark">*使用微信转账，请将微信升级至最新版本。</span>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <%--<!--                                                    <tr>--%>
                                    <%--<td colspan="3" class="tip-box"><span--%>
                                            <%--class="dark" style="color: red;">*使用微信秒存转账赠1.5%，满300再赠免费红包</span>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td colspan="3" class="tip-box"><span--%>
                                            <%--class="dark" >*使用微信秒存，请务必关闭<span style="color: red;">微信定位</span>在进行存款。</span>--%>
                                    <%--</td>--%>
                                <%--</tr> -->--%>

                                <%--</tbody>--%>
                            <%--</table>--%>
                        <%--</div>--%>

                        <%--<div class="deposit-fastpay-2 hidden">--%>
                            <%--<div class="clearfix" style="margin: 15px 0px;">--%>
                                <%--<div class="fl sub-title">存款信息</div>--%>
                                <%--<div class="fl">--%>
                                    <%--<table class="table1">--%>
                                        <%--<tr>--%>
                                            <%--<td>存款姓名</td>--%>
                                            <%--<td class="fastpay-confirm-username"></td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>存款金额</td>--%>
                                            <%--<td class="fastpay-confirm-money bigger"></td>--%>
                                        <%--</tr>--%>
                                    <%--</table>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <%--<div class="space-1"></div>--%>

                            <%--<div class="clearfix">--%>
                                <%--<div class="fl sub-title">收款账号</div>--%>
                                <%--<div class="fl">--%>
                                    <%--<table class="table1 table-deposit">--%>
                                        <%--<tr>--%>
                                            <%--<td colspan="2"--%>
                                                <%--class="red noborder vertical-align"--%>
                                                <%--style="padding: 0px;">--%>
                                                <%--<p>（仅本次有效，下次存款请重新获取）</p>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>收款银行</td>--%>
                                            <%--<td class="relative"><span--%>
                                                    <%--class="sbankname"></span></td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>收款卡号</td>--%>
                                            <%--<td>--%>
                                                <%--<span class="saccountno"></span>--%>
                                                <%--<a class="copy copy1"--%>
                                                   <%--data-clipboard-action="copy"--%>
                                                   <%--data-clipboard-text="">复制</a>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>收款姓名</td>--%>
                                            <%--<td>--%>
                                                <%--<span class="saccountname"></span>--%>
                                                <%--<a class="copy copy1"--%>
                                                   <%--data-clipboard-action="copy"--%>
                                                   <%--data-clipboard-text="">复制</a>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>
                                    <%--</table>--%>

                                <%--</div>--%>
                                <%--<div class="clearfix"></div>--%>
                                <%--<div style="margin: 30px 0px;">--%>

                                    <%--<input type="button" value="重新填写"--%>
                                           <%--class="deposit-btn2 fastpay-return-btn">--%>

                                    <%--<input type="button" value="我已成功转账"--%>
                                           <%--class="deposit-btn2 fastpay-success-btn orange-btn">--%>

                                    <%--<input type="button" value="存款失败，重新获取新的收款账户"--%>
                                           <%--class="deposit-btn2 fastpay-renew-btn orange-btn">--%>

                                    <%--<a class=" arrow-teach" data-href=".deposit-fastpay-5"--%>
                                       <%--href="javascript:;" style="    position: relative;--%>
    <%--bottom: -12px;color:red;">如何转账？不懂点我</a>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                        <%--</div>--%>

                        <%--<div class="deposit-fastpay-3 hidden">--%>
                            <%--<p class="tip1">存款确认中</p>--%>
                            <%--<p>您的存款正在核实中........</p>--%>
                            <%--<p class="tip2">--%>
                                <%--请核对存款信息是否一致，如果信息一致，款项通常会在10分钟内到账，您可以点击刷新余额查看。如果超时未到账，请联系<a--%>
                                    <%--href="javascript:;" target="_blank"--%>
                                    <%--onclick="getCsOnDuty();" class="avt">在线客服</a>，进行咨询。--%>
                            <%--</p>--%>
                            <%--<table class="table1 j-count-table">--%>
                                <%--<tr>--%>
                                    <%--<td>存款信息</td>--%>
                                    <%--<td>预计倒计时</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td>存款金额：<span class="fastpay-confirm-money"></span>--%>
                                    <%--</td>--%>
                                    <%--<td rowspan="6" class="text-center"><span--%>
                                            <%--class="fastpay-time">10:00</span></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td>存款姓名：<span--%>
                                            <%--class="fastpay-confirm-username"></span>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td>存款方式：<span class="fastpay-confirm-type">支付宝转账</span></td>--%>
                                <%--</tr>--%>
                                <%--&lt;%&ndash;--%>
                                <%--<tr>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;--%>
                                <%--<td>订单时间：<span></span></td>--%>
                                <%--&ndash;%&gt;--%>
                                <%--&lt;%&ndash;--%>
                            <%--</tr>--%>
                            <%--&ndash;%&gt;--%>
                            <%--</table>--%>

                            <%--<table class="table1 j-cs-table hidden">--%>
                                <%--<tr>--%>
                                    <%--<td>存款信息</td>--%>
                                    <%--<td class="noborderBottom pl30">如果您已经正确存款：</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td>存款金额：<span class="fastpay-confirm-money"></span>--%>
                                    <%--</td>--%>
                                    <%--<td class="text-center noborderBottom noborderTop">--%>
                                        <%--<input type="button" value="联系在线客服"--%>
                                               <%--class="deposit-btn2"--%>
                                               <%--onclick="getCsOnDuty();"></td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td>存款姓名：<span--%>
                                            <%--class="fastpay-confirm-username"></span>--%>
                                    <%--</td>--%>
                                    <%--<td class="noborderBottom noborderTop pl30">如果您未存款--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                                <%--<tr>--%>
                                    <%--<td>存款方式：<span class="fastpay-confirm-type">支付宝转账</span></td>--%>
                                    <%--<td class="text-center noborderTop pl3"><a--%>
                                            <%--href="javascript:;"--%>
                                            <%--class="fastpay-return-btn">返回重新存款>></a></td>--%>
                                <%--</tr>--%>
                                <%--&lt;%&ndash;--%>
                                <%--<tr>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;--%>
                                <%--<td>订单时间：<span></span></td>--%>
                                <%--&ndash;%&gt;--%>
                                <%--&lt;%&ndash;--%>
                            <%--</tr>--%>
                            <%--&ndash;%&gt;--%>
                            <%--</table>--%>

                            <%--<div class="text-center" style="margin:15px; ">--%>
                                <%--<input type="button" value="我还未存款，返回重新填写"--%>
                                       <%--class="deposit-btn3 fastpay-return-btn">--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<div class="deposit-fastpay-4 hidden">--%>
                            <%--<div class="text-center" style="margin:50px; ">--%>
                                <%--<img src="/images/fastpay-success.png"/>--%>
                            <%--</div>--%>

                            <%--<div class="text-center" style="margin:50px;">--%>
                                <%--<input type="button" value="确定"--%>
                                       <%--class="deposit-btn3 fastpay-return-btn">--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<!-- 微信秒存说明1 -->--%>
                        <%--<div class="deposit-fastpay-5 hidden">--%>
                            <%--<div class="text-center relative">--%>
                                <%--<p class="mt20 mb25">步骤一：请先确认您的微信版本为最新版本</p>--%>
                                <%--<img src="/images/deposit/5-1.jpg" class="mb25"/>--%>

                                <%--<div class="cfx"></div>--%>

                                <%--<input type="button" class="deposit-btn2 orange-btn arrow-teach"--%>
                                       <%--value="已了解，继续存款"--%>
                                       <%--data-href=".deposit-fastpay-2">--%>
                                <%--<input type="button" class="deposit-btn2 arrow-teach"--%>
                                       <%--value="存款步骤（下一步）"--%>
                                       <%--data-href=".deposit-fastpay-6">--%>

                                <%--<img src="/images/deposit/arrow-left.png" class="arrow-right"--%>
                                     <%--data-href=".deposit-fastpay-6"/>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<!-- 微信秒存说明2 -->--%>
                        <%--<div class="deposit-fastpay-6 hidden">--%>
                            <%--<div class="text-center relative">--%>
                                <%--<p class="mt20 mb25">步骤二：点击微信【钱包】-【收付款】页面</p>--%>
                                <%--<img src="/images/deposit/1-2.jpg" class="mb25"/>--%>

                                <%--<div class="cfx"></div>--%>

                                <%--<input type="button" class="deposit-btn2 orange-btn arrow-teach"--%>
                                       <%--value="已了解，继续存款"--%>
                                       <%--data-href=".deposit-fastpay-2">--%>
                                <%--<input type="button" class="deposit-btn2 arrow-teach"--%>
                                       <%--value="存款步骤（下一步）"--%>
                                       <%--data-href=".deposit-fastpay-7">--%>

                                <%--<img src="/images/deposit/arrow-right.png" class="arrow-left"--%>
                                     <%--data-href=".deposit-fastpay-5"/>--%>
                                <%--<img src="/images/deposit/arrow-left.png" class="arrow-right"--%>
                                     <%--data-href=".deposit-fastpay-7"/>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<!-- 微信秒存说明3 -->--%>
                        <%--<div class="deposit-fastpay-7 hidden">--%>
                            <%--<div class="text-center relative">--%>
                                <%--<p class="mt20 mb25">步骤三：选择【转账到银行卡功能】</p>--%>
                                <%--<img src="/images/deposit/2-2.jpg" class="mb25"/>--%>

                                <%--<div class="cfx"></div>--%>

                                <%--<input type="button" class="deposit-btn2 orange-btn arrow-teach"--%>
                                       <%--value="已了解，继续存款"--%>
                                       <%--data-href=".deposit-fastpay-2">--%>
                                <%--<input type="button" class="deposit-btn2  arrow-teach"--%>
                                       <%--value="存款步骤（下一步）"--%>
                                       <%--data-href=".deposit-fastpay-8">--%>

                                <%--<img src="/images/deposit/arrow-right.png" class="arrow-left"--%>
                                     <%--data-href=".deposit-fastpay-6"/>--%>
                                <%--<img src="/images/deposit/arrow-left.png" class="arrow-right"--%>
                                     <%--data-href=".deposit-fastpay-8"/>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<!-- 微信秒存说明4 -->--%>
                        <%--<div class="deposit-fastpay-8 hidden">--%>
                            <%--<div class="text-center relative">--%>
                                <%--<p class="mt20 mb25">步骤四：对应填写获取到的收款账户名与收款卡号</p>--%>
                                <%--<img src="/images/deposit/3-3.jpg" class="mb25"/>--%>

                                <%--<div class="cfx"></div>--%>

                                <%--<input type="button" class="deposit-btn2 orange-btn arrow-teach"--%>
                                       <%--value="已了解，继续存款"--%>
                                       <%--data-href=".deposit-fastpay-2">--%>
                                <%--<input type="button" class="deposit-btn2  arrow-teach"--%>
                                       <%--value="存款步骤（下一步）"--%>
                                       <%--data-href=".deposit-fastpay-9">--%>

                                <%--<img src="/images/deposit/arrow-right.png" class="arrow-left"--%>
                                     <%--data-href=".deposit-fastpay-7"/>--%>
                                <%--<img src="/images/deposit/arrow-left.png" class="arrow-right"--%>
                                     <%--data-href=".deposit-fastpay-9"/>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<!-- 微信秒存说明5 -->--%>
                        <%--<div class="deposit-fastpay-9 hidden">--%>
                            <%--<div class="text-center relative">--%>
                                <%--<p class="mt20 mb25">步骤五：务必填写系统生成的存款金额，方可自动到账</p>--%>
                                <%--<img src="/images/deposit/4-4.jpg" class="mb25"/>--%>

                                <%--<div class="cfx"></div>--%>

                                <%--<input type="button" class="deposit-btn2 orange-btn arrow-teach"--%>
                                       <%--value="已了解，继续存款"--%>
                                       <%--data-href=".deposit-fastpay-2">--%>

                                <%--<img src="/images/deposit/arrow-right.png" class="arrow-left"--%>
                                     <%--data-href=".deposit-fastpay-8"/>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<div class="deposit-fastpay-10 hidden">--%>
                            <%--<div class="text-center relative">--%>
                                <%--<h3 class="red">您需要存入金额：</h3>--%>
                                <%--<h1 class="real-save-money red mt20 mb25"></h1>--%>
                                <%--<h3 class="red mb25">温馨提示：请您存入该金额，否则存款无法到账<br/>本订单有效时间为2小时</h3>--%>
                                <%--<div class="tips"><input type="checkbox" class="must-checked">我已明白需要转账--%>
                                    <%--<span--%>
                                            <%--class="save-money red"></span> 元--%>
                                <%--</div>--%>
                                <%--<div class="tips"><input type="checkbox" class="must-checked1">本人已同意，如未转账--%>
                                    <%--<span--%>
                                            <%--class="save-money red"></span> 元，导致系统无法匹配存款，天威概不负责！--%>
                                <%--</div>--%>
                                <%--<input type="button" class="deposit-btn2 orange-btn"--%>
                                       <%--value="下一步" onclick="jumpNext(this)">--%>
                            <%--</div>--%>
                        <%--</div>--%>

                    <%--</div>--%>

                </div>

            </div>

            <!--久安支付-->
            <div class="item item8">
                <div id="deposit-jiuan-page" class="one relative">
                    <table>
                        <tbody>
                        <tr>
                            <td class="sub-title">存款方式</td>
                            <td class="pay-box-list" colspan="2">
                                <div class="pay-box active" data-bonus="2%">
                                    <div class="zhi_box">
                                        <img src="images/user/jiuan_logo.png"
                                             style="width: 40px; top: 0px;left: -7px;"/>
                                    </div>
                                    <div class="wrap-box">
                                        <span class="zh2">赠2%</span>
                                        <h3>久安支付</h3>
                                        <span>稳定，资金安全无风险</span>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-jiuan-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"><span
                                    class="brown tip-box-top">若选择的通道无法存款，请点选其他支付通道</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1-3000">
                            </td>

                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-jiuan-submit"
                                                   class="btn orange-btn" value="生成二维码">

                                <input type="button" class="btn orange-btn " value="查看久安钱包流程"
                                       onclick="window.open('http://www.longdobbs.com/forum.php?mod=viewthread&tid=2823&highlight=%E4%B9%85%E5%AE%89','_blank');">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" class="tip-box">
                                <span class="dark">*请先下载久安钱包进行充值。</span><br>
                                <span class="dark">*人民币：UET比例为1:100。</span><br>
                                <span class="dark">*使用久安钱包进行C2C交易安全无风险。</span><br>
                                <span class="dark" style="color: red;">*若存款未到账请联系在线客服。</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <div class="junaShow" style="position: absolute;right: -10px;bottom: 0px;">
                        <div class="text-center mb5"><font color="red">久安钱包APP下载二维码</font><br/>
                            <img src="/images/user/jiuan_qrcode.png" style="width:140px;">
                        </div>

                        <div>
                            <div class="space-1"></div>
                            <p>我已有久安钱包账号，将我的天威账户
                                <a href="javascript:void(0);" onclick="bindingJuan();"
                                   class="bindingJuan">绑定久安钱包</a>
                            </p>
                            <form id="bindJuanForm"
                                  action="https://www.9security.com/userBind"
                                  method="get" target="_blank">
                                <input id="bindId" name="merchantId" type="hidden"/>
                                <input id="bindUrl" name="notifyUrl" type="hidden"/>
                                <input id="bindName" name="merchantUserName" type="hidden"/>
                                <input id="bindcallBack" name="callBackUrl" type="hidden"/>
                            </form>

                            <div class="space-1"></div>
                            <p>我没有久安钱包账号，用我的天威账户
                                <a href="javascript:void(0);" onclick="syncJuan();"
                                   class="syncJuan">同步久安钱包</a></p>
                            <form id="syncJuanForm"
                                  action="https://www.9security.com/quickCreate"
                                  method="get" target="_blank">
                                <input id="syncId" name="merchantId" type="hidden"/>
                                <input id="syncUrl" name="notifyUrl" type="hidden"/>
                                <input id="syncName" name="merchantUserName" type="hidden"/>
                                <input id="synccallBack" name="callBackUrl" type="hidden"/>
                            </form>
                        </div>
                    </div>
                    <div class="junaShow2" style="position: absolute;right: -10px;bottom: 0px;">
                        <img src="/images/deposit/juna_lobby.png" width="139">
                        <p id="junaLobby" style="margin-top:10px;">
                            <a class="junaLobby" href="javascript:void(0);"
                               onclick="junaLobby()">打开钱包交易付款</a>
                        </p>
                        <form id="junaLobbyForm"
                              action="https://www.9security.com/autoLogin" method="get"
                              target="_blank">
                            <input id="lobbyId" name="merchantId" type="hidden"/>
                            <input id="lobbyToken" name="token" type="hidden"/>
                        </form>
                    </div>

                    <%--<p class="dark">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取。</p>--%>
                </div>

                <%--<div class="jiuan-pop-mark"></div>--%>
                <%--<div class="jiuan-pop jiuan-bd-step1 dialog-wrap">--%>
                <%--<h2 class="title">钱包绑定条款</h2>--%>
                <%--<div class="content-box">--%>
                <%--<h3 class="black-text first-row-text">将天威账户绑定久安钱包：</h3>--%>
                <%--<p>您是否同意久安钱包获取您在天威上的<span class="c-red">相关非敏感类信息。</span></p>--%>
                <%--<p>天威承诺：久安钱包<span class="c-red">无权从天威获取</span>您的联系方式，余额，等身份<span--%>
                <%--class="c-red">敏感类信息。</span></p>--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="agree-btn" onclick="bdStep1Event()">同意并绑定</span>--%>
                <%--<span class="cancle-btn">不同意</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="jiuan-pop jiuan-bd-step2 faild">--%>
                <%--<h2 class="title">绑定久安包</h2>--%>
                <%--<div class="content-box">--%>
                <%--<h3 class="c-red black-text first-row-text">久安帐号：</h3>--%>
                <%--<input class="jiuan-input" id="jiuAnId" type="text"--%>
                <%--placeholder="请输入您的久安帐号、手机号、邮箱">--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="agree-btn" onclick="bdStep2Event()">确定</span>--%>
                <%--<span class="cancle-btn">取消</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="jiuan-pop jiuan-bd-step3 success">--%>
                <%--<h2 class="title">温馨提示</h2>--%>
                <%--<div class="content-box">--%>
                <%--<h3 class="black-text first-row-text"><span class="danger-icon">&times;</span>&nbsp;&nbsp;绑定失败！检测久安帐号失败。--%>
                <%--</h3>--%>
                <%--<div class="red-div"></div>--%>
                <%--<p class="black-text">错误原因：</p>--%>
                <%--<p class="c-red">1、您输入的久安帐号可能有误</p>--%>
                <%--<p class="c-red">2、系统繁忙</p>--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="agree-btn" onclick="bdStep3Event()">返回重新绑定</span>--%>
                <%--<span class="cancle-btn">关闭</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="jiuan-pop jiuan-tb-step1 dialog-wrap">--%>
                <%--<h2 class="title">钱包同步条款</h2>--%>
                <%--<div class="content-box">--%>
                <%--<h3 class="black-text first-row-text">用天威账户同步久安钱包：</h3>--%>
                <%--<p>您是否同意久安钱包获取您在天威上的<span class="c-red">相关非敏感类信息？</span></p>--%>
                <%--<p>天威承诺：久安钱包<span class="c-red">无权从天威获取</span>您的联系方式，余额，等身份<span--%>
                <%--class="c-red">敏感类信息。</span></p>--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="agree-btn" onclick="tbStep1Event()">同意并同步</span>--%>
                <%--<span class="cancle-btn">取消</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="jiuan-pop jiuan-tb-step2 faild">--%>
                <%--<h2 class="title">温馨提示</h2>--%>
                <%--<div class="content-box">--%>
                <%--<h3 class="c-red black-text first-row-text">数据同步失败！</h3>--%>
                <%--<p class="black-text">您的久安国际账号<span id="userID" class="c-red">asdf</span>已经被同步久安钱包，不可重复同步！！--%>
                <%--</p>--%>
                <%--<p>若该同步非您本人操作，请联系久安客服找回！</p>--%>
                <%--<p>久安客服：000-000000000</p>--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="cancle-btn">我知道了</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="jiuan-pop jiuan-tb-step3 success">--%>
                <%--<h2 class="title">温馨提示</h2>--%>
                <%--<div class="content-box">--%>
                <%--<h3 class="black-text first-row-text">数据同步成功！</h3>--%>
                <%--<p>您的久安账号为“天威前缀+您的天威账号”：</p>--%>
                <%--<p class="c-red" id="jiuAnId"></p>--%>
                <%--<p>您的久安密码为您的天威登陆密码</p>--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="cancle-btn">我知道了</span>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="jiuan-pop jiuan-bar-box">--%>
                <%--<div class="content-box">--%>
                <%--<p class="text-view first-row-text">数据同步中，请耐心等待......--%>
                <%--<span id="tbCount" class="c-red">45%</span>--%>
                <%--</p>--%>
                <%--<div class="jiuan-bar-wp">--%>
                <%--<div class="jiuan-bar"><img src="/images/user/bar.png" alt=""></div>--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div class="btn-box">--%>
                <%--<span class="agree-btn" id="finishBtn" disabled="disabled">确定</span>--%>
                <%--</div>--%>
                <%--</div>--%>

            </div>

            <!--QQ钱包-->
            <div class="item item3">
                <div id="deposit-qq-page" class="one">
                    <table>
                        <tbody>
                        <tr>
                            <td class="sub-title">存款方式</td>
                            <td class="pay-box-list" colspan="2">
                                <div class="pay-box active">
                                    <div class="zhi_box">
                                        <img src="images/user/qq_icon.png"/>
                                    </div>
                                    <div class="wrap-box">
                                        <h3>QQ支付</h3>
                                        <span>便捷，最高上限3000</span>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-qq-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"><span
                                    class="brown tip-box-top">若选择的通道无法存款，请点选其他支付通道</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1-3000">
                            </td>

                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list  btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-qq-submit"
                                                   class="btn orange-btn" value="生成二维码"></td>
                        </tr>

                        </tbody>
                    </table>
                    <%--<p class="dark">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取。</p>--%>
                </div>
            </div>

            <!--点卡-->
            <div class="item item5" id="user_dianka">

                <table>
                    <tr>
                        <td class="sub-title">存款方式</td>
                        <td class="pay-box-list" colspan="2" id="cunkfs">
                            <div id="deposit-jd-subtitle" data-target="deposit-jd-page"
                                 class="pay-box active">
                                <div class="zhi_box">
                                    <img src="images/user/jd-icon.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>京东支付</h3>
                                    <span>便捷，最高上限3000</span>
                                </div>
                            </div>

                            <div id="deposit-meituan-subtitle" data-target="deposit-meituan-page"
                                 class="pay-box">
                                <div class="zhi_box">
                                    <img src="images/user/mt-icon.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>美团支付</h3>
                                    <span>便捷，最高上限3000</span>
                                </div>
                            </div>
                            <div id="deposit-dcard-subtitle" data-target="deposit-dcard-page"
                                 class="pay-box">
                                <div class="yinhang_box">
                                    <img src="images/user/yinhang.png"/>
                                </div>
                                <div class="wrap-box">
                                    <h3>点卡支付</h3>
                                    <span>便捷，点卡充值</span>
                                </div>
                            </div>

                        </td>
                    </tr>
                </table>

                <div id="deposit-jd-page" class="one tab-panel ">
                    <table>
                        <tbody>

                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-jd-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"><span
                                    class="brown tip-box-top">若选择的通道无法存款，请点选其他支付通道</span>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1-3000">
                            </td>

                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list  btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-jd-submit"
                                                   class="btn orange-btn" value="生成二维码"></td>
                        </tr>

                        </tbody>
                    </table>
                    <%--<p class="dark">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取。</p>--%>
                </div>

                <div id="deposit-meituan-page" class="one tab-panel">
                    <table>
                        <tbody>

                        <tr>
                            <td class="sub-title">支付通道</td>
                            <td colspan="2">
                                <div class="payway-list  deposit-meituan-reload"></div>
                            </td>
                        </tr>
                        <tr class="payway-list-box">
                            <td></td>
                            <td class="tip-box"><span
                                    class="brown tip-box-top">若选择的通道无法存款，请点选其他支付通道</span>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title">存款金额</td>
                            <td class="wd260"><input type="text" class="money-input input"
                                                     maxlength="10" placeholder="1-3000">
                            </td>

                            <td>
                                                    <span class="red money-error-message hidden"
                                                          style="font-size:14px;">请输1-3000之间的金额。</span>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td colspan="2">
                                <div class="money-list  btn-list">
                                    <input type="button" data-value="10" value="10">
                                    <input type="button" data-value="500" value="500">
                                    <input type="button" data-value="1000" value="1000">
                                    <input type="button" data-value="3000" value="3000">
                                    <input type="button" data-value="other" value="其他金额">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="sub-title">实际到账</td>
                            <td>
                                <div class="money-pay">0元</div>
                            </td>
                        </tr>

                        <tr>
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-meituan-submit"
                                                   class="btn orange-btn" value="生成二维码"></td>
                        </tr>

                        </tbody>
                    </table>
                    <%--<p class="dark">*需要承担<span class="payway-fee"></span>%手续费，费用由第三方收取。</p>--%>
                </div>

                <div id="deposit-dcard-page" class="one tab-panel">
                    <table>
                        <tbody>
                        <tr>
                            <td class="sub-title vertical-align"
                                style="position: relative; top: 14px;">支付点卡
                            </td>
                            <td id="cardTable" class="card-list btn-list"></td>
                            <td class="vertical-align"
                                style="width: 130px;position: relative;top: 5px;left: -15px;">
                                                    <span class="red dcard-error-message hidden"
                                                          style="font-size:14px;">请选择支付点卡。</span>
                            </td>
                        </tr>
                        <tr class="dcard-choose-value hidden" id="dianka_type">
                            <td class="sub-title">选择面额</td>
                            <td colspan="2">
                                <div class="money-list btn-list" id="dianka_box"></div>
                            </td>
                        </tr>
                        <tr class="dcard-choose-value hidden">
                            <td></td>
                            <td class="tip-box"><span
                                    class="tip-box-top red dcard-value-error-message hidden"
                                    style="font-size:14px;">请选择点卡面额。</span></td>
                        </tr>
                        <tr class="dcard-form hidden" id="dcard-form">
                            <td class="sjdz">实际到账</td>
                            <td id="display-dcard-pay" class="ft26 c-huangse">0<span>元</span></td>
                        </tr>
                        <tr class="dcard-form hidden">
                            <td>卡号</td>
                            <td><input type="text" id="deposit-card-no" class="input"/></td>
                        </tr>
                        <tr class="dcard-form hidden">
                            <td>密码</td>
                            <td><input type="text" id="deposit-card-password" class="input"/></td>
                        </tr>
                        <tr class="dcard-form hidden">
                            <td></td>
                            <td class="tip-box"><span class="red dcard-pwd-error-message hidden"
                                                      style="font-size:14px;">卡号密码无效，请重新输入。</span>
                            </td>
                        </tr>
                        <tr class="dcard-form hidden">
                            <td class="sub-title"></td>
                            <td colspan="2"><input type="button" id="deposit-card-submit"
                                                   class="btn orange-btn" value="确定支付"></td>
                        </tr>

                        </tbody>
                    </table>
                    <div class="prompt-info" style="padding-left: 20px; float: left;">
                        <h3 class="tit c-huangse">温馨提示</h3>
                        <p>
                            1.为了避免掉单情况的发生，请您在支付完成后，需等“支付成功”页面跳转出来，点商城取货或通知客户，再关闭页面,以免掉单，感谢配合。<br>
                            2.建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的，到账金额比实际存款金额少，到账比例请查看
                            <span class="diankatable">点卡支付费率表。</span>
                        </p>
                    </div>
                </div>

            </div>

            <div class="diankabiao">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td colspan="2" style="background: #fffff;">点卡支付费率表</td>
                    </tr>
                    <tr>
                        <th>卡类型</th>
                        <th>费率点</th>
                    </tr>
                    <tr>
                        <td>移动神州行</td>
                        <td>0.05</td>
                    </tr>
                    <tr>
                        <td>电信国卡</td>
                        <td>0.05</td>
                    </tr>
                    <tr>
                        <td>QQ币充值卡</td>
                        <td>0.14</td>
                    </tr>
                    <tr>
                        <td>联通一卡通</td>
                        <td>0.05</td>
                    </tr>
                    <tr>
                        <td>骏网一卡通</td>
                        <td>0.16</td>
                    </tr>
                    <tr>
                        <td>完美一卡通</td>
                        <td>0.14</td>
                    </tr>
                    <tr>
                        <td>征途一卡通</td>
                        <td>0.13</td>
                    </tr>
                    <tr>
                        <td>网易一卡通</td>
                        <td>0.14</td>
                    </tr>
                    <tr>
                        <td>搜狐一卡通</td>
                        <td>0.16</td>
                    </tr>
                    <tr>
                        <td>久游一卡通</td>
                        <td>0.20</td>
                    </tr>
                    <tr>
                        <td>天宏一卡通</td>
                        <td>0.17</td>
                    </tr>
                    <tr>
                        <td>盛付通一卡通</td>
                        <td>0.14</td>
                    </tr>
                </table>
                <div class="prompt-info" style="padding-left: 20px; float: left;">
                    <h3 class="tit c-huangse">温馨提示</h3>
                    <p>
                        建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的，到账金额比实际存款金额少，到账比例请查看<span
                            class="diankatable">点卡支付费率表。</span>
                    </p>
                </div>
            </div>

            <!--云闪付-->
            <%--<div class="item item6" id="cloudbox">--%>
            <%--<div class="one">--%>
            <%--<table>--%>
            <%--<tr>--%>
            <%--<td class="sub-title">存款方式</td>--%>
            <%--<td class="pay-box-list" colspan="2">--%>
            <%--<div id="deposit-cloud-subtitle"--%>
            <%--data-target="deposit-cloud-page" class="pay-box active"--%>
            <%--data-value="5" data-bonus="1.5%" data-min="1"--%>
            <%--data-max="3000000">--%>
            <%--<div class="kuaijie_box">--%>
            <%--<img src="images/user/dian.png">--%>
            <%--</div>--%>
            <%--<div class="wrap-box">--%>
            <%--<span class="zh1">赠1.5%</span>--%>
            <%--<h3>云闪付</h3>--%>
            <%--<span>稳定，单笔上限300万</span>--%>
            <%--</div>--%>
            <%--</div>--%>

            <%--</td>--%>
            <%--</tr>--%>
            <%--</table>--%>

            <%--<div id="deposit-cloud-page" class="tab-panel active">--%>
            <%--<div class="deposit-cloudpay-1 relative">--%>
            <%--<table>--%>
            <%--<tr>--%>
            <%--<td class="sub-title">存款卡号</td>--%>
            <%--<td class="wd260"><input type="text"--%>
            <%--class="input cloud-card"--%>
            <%--maxlength="4"--%>
            <%--placeholder="请输入银行卡末四位数字号码"--%>
            <%--onkeyup="value=value.replace(/[^\d]/g,'');">--%>
            <%--</td>--%>
            <%--<td><span--%>
            <%--class="red cloud-card-error-message hidden"--%>
            <%--style="font-size:14px;">请输入银行卡末四位数字号码。</span>--%>
            <%--</td>--%>
            <%--</tr>--%>

            <%--<tr>--%>
            <%--<td class="sub-title">存款金额</td>--%>
            <%--<td class="wd260"><input type="text"--%>
            <%--class="money-input input"--%>
            <%--maxlength="10"--%>
            <%--placeholder="1元-300万"--%>
            <%--onkeyup="value=value.replace(/[^\d]/g,'');">--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<span class="red money-error-message hidden"--%>
            <%--style="font-size:14px;">请输1元-300万之间的金额。</span>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
            <%--<td class="sub-title">实际到账</td>--%>
            <%--<td>--%>
            <%--<div class="money-pay">0元</div>--%>
            <%--</td>--%>
            <%--</tr>--%>

            <%--<tr>--%>
            <%--<td class="sub-title"></td>--%>
            <%--<td colspan="2">--%>

            <%--<input type="button"--%>
            <%--class="btn orange-btn cloudpay-next-btn"--%>
            <%--value="获取收款账号">--%>
            <%--<input type="button"--%>
            <%--class="btn orange-btn "--%>
            <%--value="查看云闪付流程"--%>
            <%--onclick="window.open('http://cn.unionpay.com/zt/2017/139595361/','_blank');">--%>

            <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
            <%--<td colspan="3" class="tip-box">--%>
            <%--<span class="dark">*云闪付请下载云闪付APP才能进行存款支付。</span><br>--%>
            <%--<span class="dark">*云闪付存款需要填写正确支付银行卡号末四码系统才会正确匹配到帐。</span><br>--%>
            <%--<span class="dark">*支付金额需要与填写存款金额一样系统才会匹配上分。</span><br>--%>
            <%--<!-- <span class="dark" style="color: red;">*使用云闪付秒存转账赠1.5%，满300再赠免费红包。</span> -->--%>
            <%--</td>--%>
            <%--</tr>--%>

            <%--</tbody>--%>
            <%--</table>--%>

            <%--<div style="position: absolute;right: 0px;bottom: 0px;width: 160px;">--%>
            <%--<div class="text-center mb5"><font color="red">云闪付APP下载二维码</font>--%>
            <%--<img src="/images/deposit/yunshanfu_qrcode.png"--%>
            <%--style="width:140px;">--%>
            <%--</div>--%>

            <%--</div>--%>
            <%--</div>--%>

            <%--&lt;%&ndash;<div class="deposit-cloudpay-2 hidden">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="text-center relative">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<p class="font2 my-input-title red">您需要存入金额：</p>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<p class="font4 my-input-title red quota"></p>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="space-2"></div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="gray-tips red font2">温馨提示：请您存入该金额，否则存款无法到账，本订单有效时间为2小时&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="space-2"></div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="tips"><input type="checkbox" id="j-cloudpay-checked">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<label for="j-cloudpay-checked">我已明白需要转账<span&ndash;%&gt;--%>
            <%--&lt;%&ndash;class="quota red"></span>元</label>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="tips"><input type="checkbox" id="j-cloudpay-checked1">&ndash;%&gt;--%>
            <%--&lt;%&ndash;<label for="j-cloudpay-checked1">本人已同意，如未转账<span&ndash;%&gt;--%>
            <%--&lt;%&ndash;class="quota red"></span>元，导致系统无法匹配存款，天威概不负责！</label>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<div class="space-2"></div>&ndash;%&gt;--%>

            <%--&lt;%&ndash;<input type="button" id="j-cloud-agree"&ndash;%&gt;--%>
            <%--&lt;%&ndash;class="deposit-btn2 orange-btn" value="下一步">&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

            <%--<div class="deposit-cloudpay-3 hidden">--%>
            <%--<div class="clearfix" style="margin: 15px 0px;">--%>
            <%--<div class="sub-title">存款信息</div>--%>
            <%--<img src="" id="cImgCode" style="width:300px;">--%>

            <%--<div class="relative" style="margin: 30px 0px;">--%>
            <%--<input type="button" value="我已成功转账"--%>
            <%--class="deposit-btn2 cloudpay-success-btn orange-btn">--%>
            <%--<input type="button" value="重新填写"--%>
            <%--class="deposit-btn2 cloudpay-return-btn">--%>
            <%--<input type="button" value="查看云闪付流程"--%>
            <%--class="deposit-btn2 orange-btn"--%>
            <%--onclick="window.open('http://cn.unionpay.com/zt/2017/139595361/','_blank');">--%>

            <%--<div style="    position: absolute;--%>
            <%--right: 0px;--%>
            <%--bottom: 100px;--%>
            <%--width: 150px;">--%>
            <%--<div class="text-center"><font--%>
            <%--color="red">云闪付APP下载二维码</font></div>--%>
            <%--<img src="/images/deposit/yunshanfu_qrcode.png"--%>
            <%--style="width:100%;">--%>
            <%--</div>--%>
            <%--</div>--%>

            <%--</div>--%>

            <%--</div>--%>

            <%--</div>--%>

            <%--</div>--%>

            <%--</div>--%>


            <!--一码通-->

            <%--<div class="item item6">--%>
            <%--<div class="one">--%>
            <%--<table>--%>
            <%--<tbody>--%>
            <%--<tr>--%>
            <%--<td class="vertical-align">扫描支付</td>--%>
            <%--<td><img src="" class="wd250" id="j-gather-codeimg"/>--%>
            <%--</td>--%>
            <%--<td class="relative">--%>
            <%--<p class="red">*请每次存款时重新获取二维码</p>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
            <%--<td></td>--%>
            <%--&lt;%&ndash;<td class="text-center"><img src="/images/commonpay.png"/></td>&ndash;%&gt;--%>
            <%--<td>--%>
            <%--<p class="red">（1个二维码，支持多个支付软件）</p>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
            <%--<td class="sub-title">添加备注</td>--%>
            <%--<td>--%>
            <%--<font style="position: relative;top: 2px;"--%>
            <%--color="red">${session.customer.loginname}</font>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
            <%--<td></td>--%>
            <%--<td style="position: relative;top: -100px;left: 90px;">--%>
            <%--<div class="fl text-right bottom">--%>
            <%--<p style="position: relative;top: -5px;">--%>
            <%--扫描后<br/>按右图方式填写</p>--%>
            <%--</div>--%>
            <%--<div class="fl bottom">--%>
            <%--&lt;%&ndash;<img src="/images/gather-arrow.png" style="bottom: -200px;"/>&ndash;%&gt;--%>
            <%--</div>--%>
            <%--</td>--%>
            <%--<td>--%>
            <%--<div class="fl showpaypic">--%>
            <%--<span class="span"> ${session.customer.loginname} <span--%>
            <%--class="red" style="color:red;">&lt;&lt; 填写您的游戏账号</span></span>--%>
            <%--</div>--%>
            <%--</td>--%>
            <%--</tr>--%>
            <%--</tbody>--%>
            <%--</table>--%>
            <%--<p class="dark">*1、支付限额10-5000元</p>--%>
            <%--<p class="dark">*2、准确将您游戏账号，添加至备注，否则将会错误添加至其他账号</p>--%>
            <%--<p class="dark">*3、付款手续费0.35% </p>--%>
            <%--</div>--%>
            <%--</div>--%>

        </div>
    </div>
</div>
<a target="_blank" href="http://www.longdobbs.com/forum.php?mod=viewthread&tid=2732&page=1&extra=#pid9864"><img ˝src="/images/jiuansx.jpg" alt=""></a>
