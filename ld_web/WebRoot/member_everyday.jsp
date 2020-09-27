<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>用户中心</title>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/user.css?v=5"/>
    <base href="<%=request.getRequestURL()%>"/>
    <style type="text/css">
        .unis{ display:none;}
    </style>
</head>
<body>

<div class="index-bg about-bj">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
    <div class="container">
        <jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>

        <div class="cfx">
            <div class="gb-sidenav">
                <div class="box mb20">
                    <ul class="navlist" id="j-userNav">
                        <li class="active"><a data-toggle="tab" href="#tab-daily-tasks">天天有惊喜</a></li>
                        <li onclick='queryPoint()'><a data-toggle="tab" href="#tab-point" >积分中心</a></li>
                        <%--<li><a data-toggle="tab" href="#tab-checkin" onclick='querySignAmount()'>签到处</a></li>--%>
                        <li><a data-toggle="tab" href="#tab-ptcrazy" onclick="queryPTBigBang();">PT疯狂彩金</a></li>
                    </ul>
                </div>

                <div class="box">
                    <h1 class="gb-title mb">天威APP</h1>
                    <div class="text-center">
                        <img src="/images/qr/lehu-app.png" alt="" class="w mb" width="138" height="138">
                        <p>扫描二维码</p>
                    </div>
                </div>
            </div>
            <div class="gb-main-r tab-bd user-main">

                <!--天天首存{-->
                <div id="tab-daily-tasks" class="user-tab-box active tab-panel">
                    <h1 class="tab-tit">天天有惊喜</h1>
                    <%--<s:url action="execXima" namespace="/asp" var="execXimaUrl"></s:url>--%>
                    <form class="ui-form">
                        <div class="ui-form-item">
                            <label class="ui-label">存送优惠类型：</label>
                            <select name="youhuiType" id="youhuiType1" class="ui-ipt" onchange="youHuiTypeChange1(this.value);" >
                                <option value="">---请选择存送类型---</option>
                            </select>
                        </div>

                        <div class="ui-form-item">
                            <label class="ui-label">转账金额：</label>
                            <input class="ui-ipt" type="text"  name="transferMoney" id="transferMoney1" onblur="getSelfYouhuiAmount1(this.value);"/>
                        </div>

                        <div class="ui-form-item">
                            <label class="ui-label">红利金额：</label>
                            <input class="ui-ipt" readonly type="text" name="giftMoney1" id="giftMoney1"  />
                        </div>

                        <div class="ui-form-item">
                            <label class="ui-label">流水倍数：</label>
                            <input class="ui-ipt" type="text" name="waterTimes" id="waterTimes1" readonly />
                        </div>
                        <div class="ui-form-item">
                            <input type="button" class="btn btn-danger" value="提交"
                                   onclick="return checkSelfYouHuiSubmit1();"/>
                        </div>
                    </form>
                    <div class="prompt-info">
                        <h3 class="tit">温馨提示</h3>
                        <p>1.每天 00:00 - 01:00 系统结算时间,短暂无法使用天天有惊喜。</p>
                        <p>2.天天有惊喜最低50元才可申请。</p>
                        <p>3.天天有惊喜申请成功后系统会自动派发您相应的游戏平台账户，请登录查看并游戏。</p>
                    </div>
                </div>
                <!--}天天首存-->

                <!--积分中心{-->
                <div id="tab-point" class="user-tab-box tab-panel">
                    <h1 class="tab-tit">积分中心</h1>
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
                            <a href="javascript:;" class="btn btn-danger" onclick="return submitPointRemit();">提交</a>
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

                <!--签到{-->
                <%--<div id="tab-checkin" class="user-tab-box tab-panel">
                    <h1 class="tab-tit">每日签到</h1>
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
                            <a href="javascript:;" class="btn btn-danger" onclick="return submitSignRemit();">提交</a>
                        </div>
                    </div>

                    <div class="prompt-info">
                        <h3 class="tit">温馨提示</h3>
                        <p>每日存款10元以上，便会激活签到系统，每日仅能签到一次。</p>
                        <p>每次签到，签到彩金会自动加总，彩金达到10元以上，便可选择转入TTG、PT进行游戏。</p>
                        <p>此彩金无须流水限制。</p>
                    </div>
                </div>--%>
                <!--}签到-->
                

                <!--PT疯狂礼金领取{-->
                <div id="tab-ptcrazy" class="user-tab-box tab-panel">
                    <h1 class="tab-tit">PT疯狂礼金领取</h1>
                    <div id="ptBigBangDiv" class="new"></div>
                </div>
                <!--PT疯狂礼金领取{-->
            </div>
        </div>

    </div>
</div>

<div class="modal fade" id="j-modal-rescue" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">选择老虎机平台</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-bd">
                <input type="hidden" class="j-hd-id"/><input type="hidden" class="j-hd-url"/>
                <div class="ui-form">
                    <div class="ui-form-item">
                        <label for="" class="ui-label">请选择您喜欢的老虎机平台, 确定后我们不接受任何重新转至其他老虎机平台的申请</label>
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="pttiger" checked="checked">PT老虎机
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="ttg">TTG老虎机
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="nt">NT老虎机
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="qt">QT老虎机
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="mg">MG老虎机
                        &nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="dt">DT老虎机
                    </div>
                    <div class="ui-form-item">
                        <input type="button" class="btn btn-danger j-btn-apply" value="确定">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<!-- <script type='text/javascript' src='http://js.touclick.com/js.touclick?b=0304e3d8-6d75-4bce-946a-06ada1cc5f4e&pf=api&v=v2-2' async></script> -->
<!-- <script type='text/javascript' src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js' async></script> -->
<script src="//js.touclick.com/js.touclick?b=68aca137-f3c5-457b-87a4-8a46880b1e66" ></script>
<script type="text/javascript" src="${ctx}/js/manageCoupons.js?v=5"></script>
<script type="text/javascript" src="${ctx}/js/self.js"></script>
<script src="${ctx}/js/lib/ZeroClipboard.min.js"></script>
<script>
    $(function(){
        //queryPoint();
    });
</script>
</body>
</html>
