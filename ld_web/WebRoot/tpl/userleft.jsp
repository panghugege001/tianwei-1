<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<ul class="ul-user-menu navlist ul-sidebar" id="j-user-nav">
    <div>
        <a data-toggle="tab" href="#tab_deposit" class="user_money deposit">钱包中心</a>
    </div>
    <li class="qiandao_class">
        <a class="alcito_show" data-toggle="tab" href="#tab_mrrw"><i
                class="user_icon z_index4"></i><span>每日任务</span></a>
    </li>
    <li class="cunkuang deposit">
        <a class="alcito_show" data-toggle="tab" href="#tab_deposit"><i
                class="user_icon"></i><span>存款</span></a>
    </li>
    <li class="zhuanzhangle">
        <a class="alcito_show" data-toggle="tab" href="#tab_transfer"><i class="user_icon z_index1"></i><span>转账</span></a>
    </li>
    <li class="tikuang">
        <a class="alcito_show" data-toggle="tab" href="#tab_withdraw"><i class="user_icon z_index2"></i><span>提款</span></a>
    </li>
    <!--<%-- <li><a onclick='queryfriendMoney()' data-toggle="tab" href="#tab-friends" style="position: relative"><i class="friends-hot"></i>分享好友获彩金</a></li>--%>
            <li><a data-toggle="tab" href="#tab_card_binding" >绑定银行卡</a></li>
            <li class="active"><a data-toggle="tab" href="#tab-persent">自助存送</a></li>
            <%--<li><a data-toggle="tab" href="#tab-pt2" onclick="isBlindingCard();">体验金</a></li>--%>
            <%--<li><a onclick='getEmigratedMoeny()' data-toggle="tab" href="#tab-qmcg" >闯关</a></li>--%>
            <li><a data-toggle="tab" href="#tab-help" onclick="losePromoRecord(1);">救援金</a></li>
            <%--<li><a data-toggle="tab" href="#tab-checkin" onclick='querySignAmount()'>签到处</a></li>--%>
            <%--<li onclick='queryPoint()'><a data-toggle="tab" href="#tab-point" >积分中心</a></li>--%>
            <%-- <li><a data-toggle="tab" href="#tab-weeksent" onclick="weekSentRecord(1);">周周回馈</a></li>--%>
            <li><a data-toggle="tab" href="#tab-return" onclick="getXimaEndTime($('#platform').val());">自助返水</a></li>
            <li><a data-toggle="tab" href="#tab-level" data-url="${ctx}/asp/queryBetOfPlatform.aspx" onclick="loadIframe(this);">自助晋级</a></li>
            <%--<li><a data-toggle="tab" href="#tab-ptcrazy" onclick="queryPTBigBang();">PT疯狂彩金</a></li>--%>
            <li><a data-toggle="tab" href="#tab-coupon">优惠券专区</a></li>
            -->
    <li id="zzyouhui">
        <a class="alcito_show" data-toggle="tab" href="#tab-youhui"><i class="user_icon z_index3"></i><span>自助优惠</span></a>
    </li>
    <li><a class="alcito_show" id="tj-friend" onclick='queryfriendMoney()' data-toggle="tab" href="#tab-friends"><i
            class="user_icon z_index6"></i><span>推荐好友</span></a></li>
    <li>
        <a class="alcito_show" data-toggle="tab" href="#j-record-form"><i
                class="user_icon z_index5"></i><span>账户清单</span></a>
    </li>
    <div>
        <a class="userzhongxin" data-toggle="tab" href="#user-vip">个人中心</a>
    </div>
    <li class="user-vip active">
        <a class="alcito_show" data-toggle="tab" href="#user-vip" onclick="Highchart()"><i
                class="user_icon z_index6"></i><span>会员信息</span></a>
    </li>
    <li class="tab-meir">
        <a class="alcito_show" data-toggle="tab" href="#tab-massages"><i class="user_icon z_index7"></i><span>站内信</span></a>
    </li>
    <li style="display: none" id="isShowService" class="tab-meir">
        <a class="alcito_show" data-toggle="tab" href="#tab-service"><i class="user_icon z_index6"></i><span>专属客服</span></a>
    </li>
    <!-- <li class="tab-suggestion">
      <a class="alcito_show" data-toggle="tab" href="#tab-suggestion"><i class="user_icon z_index5"></i><span>问题反馈</span></a>
    </li> -->
    <!--<li><a href="/manageRecords.jsp"><b class="account"></b>账户清单<em></em></a>
     	<ul id="j-records-action" class="ul-sidebar">
            <li class="active"><a href="#j-record-form" data-toggle="tab" data-href="/asp/depositRecords.aspx">在线存款记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/transferRecords.aspx">户内转账记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/cashinRecords.aspx">手工存款记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/withdrawRecords.aspx">提款记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/consRecords.aspx">优惠活动记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/couponRecords.aspx">优惠券记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/selfDetail_member.jsp">自助返水记录</a></li>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/depositOrderRecord.aspx">存款附言记录</a></li>
            <%-- <li><a href="#j-record-form" data-toggle=tab data-href="/asp/queryTaskRecords.aspx"></a></li>--%>
            <li><a href="#j-record-form" data-toggle="tab" data-href="/asp/queryfriendRecord.aspx">好友推荐记录</a></li>
            <%--<li><a href="#j-record-form" data-toggle="tab" data-href="/asp/querypointRecord.aspx">积分中心</a></li>--%>
            <%-- <li><a href="#j-record-form" data-toggle="tab" data-val="girl" data-href="/asp/queryFlowerRanking.aspx">守护女神</a></li>--%>
        </ul>





     </li>     -->

</ul>

<script>
    $.get('/asp/queryQRcode.aspx', function (data) {
        if ((data.length && data[0].address)) {
            $('#isShowService').show()
        } else {
            $('#isShowService').hide()
        }
    }, 'json')
</script>


 <!-- <script>
    $(function () {
        var menuBtns = document.querySelectorAll('#j-user-nav>li>.alcito_show'),
            pathName = window.location.href.replace(window.location.origin, '');
        for (var i = 0; i < menuBtns.length; i++) {
            var ele = menuBtns[i];
            var href = ele.getAttribute('href');
            console.log()
            if (ele.parentNode.className != 'center') {
                href === pathName ? ele.className = 'action alcito_show' : ele.className = 'alcito_show';
            }
        }
        $(".ul-sidebar li:first-child").addClass('on');
        $('.ul-sidebar li').click(function () {
            $(this).siblings('li').removeClass('on');
            $(this).addClass('on');
        })
        tabShow();

        $('.tikuang').click(function () {
            if ($(this).data("clk") != '1') {
                $(this).data("clk", 1)
                layer.open({
                    content: '<div style="text-align:center"><h2 style="font-size:16px;font-weight: bold;">建议使用【久安钱包】进行提款采用区块链技术，资金更便利更安全，去中心化，不受监控绝对安全保密，久安钱包你的安全提款选择</h2><br><img width="150" src="/images/jiuan.jpg" /></div>',
                    title: false,
                    shadeClose: true,
                    time: 0, //不自动关闭
                    btn: ['了解久安', '我知道了'],
                    yes: function (index) {
                        window.location = 'http://www.longdobbs.com/forum.php?mod=viewthread&tid=2732&extra=page%3D1'

                    },
                    no: function () {
                        layer.close(index);
                    }
                });

            }
        })
    })

    // 显示标签页
    function tabShow() {
        var target = Util.getQueryString('showid');
        console.log(target)
        chageNav(target);
    }

    function chageNav(target) {
        var $nav = $('.ul-sidebar');
        if (target) {
            $nav.find('a[href="#' + target + '"]').trigger('click');
        }
    }
</script> -->