<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <title>百万红包雨</title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
    <link rel="stylesheet" href="/css/base.css?v=2"/>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <style>
        body {
            background: url("./img/bground.jpg") !important;
            padding: 0;
            margin: 0;
        }

        .redpacket {
            background: url("./img/hongbao.png") top no-repeat;
            background-size: 100%;
            padding-top: 806px;
            font-size: 21px;
            position: relative;
            padding-bottom: 209px;
            overflow: hidden;
        }
        .txt-table {
            background: url("./img/txgbg.jpg") 0 no-repeat;
            background-size: 100% 100%;
            height: 1349px;
            width: 1200px;
            margin: 0 auto;
            padding: 100px 108px 0;
            line-height: 40px;
            color: #333333;
        }

        span.packet-title {
            color: #d27e0d;
        }

        .txt-table table {
            width: 100%;
            border-spacing: 1px;
            text-align: center;
            border-collapse: inherit;;
            margin: 20px 0;
        }

        .txt-table table th {
            background: #da2c1a;
            color: #ffe9bb;
            height: 40px
        }
        .txt-table table td {
            background-color: #d27e0d;
            color: #ffe9bb;
            height: 40px
        }

        @media screen and (max-width: 750px) {
            .txt-table {
                height: 1349px;
                width: auto;
                margin: 0 auto;
                padding: 10% 10% 0;
            }

            .redpacket {
                padding-top: 45%;
                padding-bottom: 0;
            }

            .gb-footer {
                display: none !important;
            }
        }
    </style>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
<style>
            .activety_muee a {
            border:1px solid transparent;
        }
</style>
<div class="redpacket">
    <div class="txt-table">
        <div><span class="packet-title">活动对象：</span>天威所有会员。</div>
        <div><span class="packet-title">活动时间：</span>2018年8月1日-8月31日</div>
        <div><span class="packet-title">活动内容：</span>达到存款活跃度即可参予疯抢红包。</div>
        <table>
            <tr>
                <th>全民抢红包资格</th>
                <th>红包降落时间</th>
                <th>流水倍数</th>
            </tr>
            <tr>
                <td>每日达到10元存款活跃度</td>
                <td>以官网、app、公告通知为主</td>
                <td>8</td>
            </tr>
        </table>
        <div>
            <span class="packet-title">活动规则：</span>
        </div>
        <p>1、每日达到10元存款活跃度，以APP或官网弹窗、公告为主。</p>
        <p>2、如何参与：官网通知活动时间后，在活动当日达到存款活跃度并登录APP即可参与疯抢红包。</p>
        <p>3、红包雨金额可转入游戏平台，需8倍流水，达到100即可提款，与反水共享,提款最高上限为888元。</p>
        <p>4、此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名、同一邮件地址、 同一相似IP地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑等其他任何不正常投注行为 一经发现，天威将保留冻结您的账户盈利及余额的权利。</p>
        <p>5、天威享有修订权和最终解释权。</p>
    </div>

</div>
<div class="modal fade in" id="redpacket-form">
  <div class="redpacket-form">
    <div class="over-txt">活动结束</div>
    <div class="tips-txt" style=""> </div>
    <div class="btnss"></div>
    <div class="closesss"> </div>
  </div>
</div>
<style>
    .modal {
        background: rgba(0, 0, 0, .8);
    }
    .redpacket-form {
        background: url("/images/ahongbao/model.png") no-repeat;
        position: absolute;
        top: 50%;
        left: 50%;
        margin: -229px 0 0 -157px;
        padding-top:157px;
        width: 315px;
        height: 458px;
    }
    .redpacket-form input {
        width: 90%;
        display: block;
        margin: 0 auto;
        height: 48px;
        border: 1px solid #ccc;
        font-size: 16px;
        text-indent: 0.5em;
    }
    .redpacket-form .over-txt{
        height: 57px;
        color: #f0d23a;
        font-size: 36px;
        visibility:hidden;
        text-align: center;
        font-weight: bold;
    }
    .redpacket-form .btnss { 
        width: 209px;
        height: 60px;
        margin: 8px auto 0;
        cursor: pointer;
        font-size: 28px;
        text-align: center;
        line-height: 60px;
        color: #542e10;
        font-weight: bold;
    }

    .redpacket-form .closesss {
        position: absolute;
        bottom: 11px;
        cursor: pointer;
        width: 20px;
        height: 20px;
        left: 148px;
    }
    .tips-txt{
        height:106px;line-height:48px;text-align: center;
        color: #f0d23a;
        font-size: 22px;
        white-space: nowrap;
    }
</style>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script>
 $(function () {
        var $H = $(window).outerHeight() + 200;
        var $W = $(window).outerWidth();
        var $redpacket = $(".redpacket");
        $(".redpacket-form .closesss,.redpacket-form .btnss").click(function () {
            $("#redpacket-form").hide();
        })
        var over=false;
        function redpacket() {
            if(over)return;
            var randomnum = Math.ceil(Math.random() * 15);
            var imgdon = $('<img style="position: fixed; cursor: pointer;z-index: 10;" src="/images/ahongbao/' + randomnum + '.png" >');
            $(imgdon).css({
                left: Math.ceil(Math.random() * $W),
                top: Math.ceil(Math.random() * 100 - 100)
            })
            imgdon.hover(function () {
                $(imgdon).stop();
            }, function () {
                $(imgdon).animate({top: $H}, 10000, function () {
                    imgdon.remove()
                });
            })
            imgdon.click(function () {
                $.ajax({
                    url: '/redrain/receiveCoupon.aspx',
                    data: {
                        title: 'redrain',
                        platform: 'pc'
                    },
                    type: 'post',
                    success: function (data) {
                        if (data.code == '204') {
                            $('#redpacket-form').show();
                            $('.tips-txt').html('可惜了<br>红包是空的')
                            $('.btnss').text("谢谢参与")
                        } else if (data.code == '203') {
                            over=true;
                            $('#redpacket-form,#redpacket-form .over-txt').show();
                            $('#redpacket-form .over-txt').css({visibility:''});
                            $('.btnss').html("<a style='color:#542e10' href='/userManage.jsp?tab_transfer'>进入红包账户</a>")
                            $('.tips-txt').html('你总共获得'+data.times+'个红包<br>累计获得红包金额'+data.depositAmount);
                        } else if (data.code == '200') {
                            $('#redpacket-form').show();
                            $('.btnss').text("继续参与");
                            $('.tips-txt').html('成功抢到 1 个红包<br>'+data.msg);
                        } else if (data.code == '500' || data.code == '400') {
                            alert(data.msg);
                        } else {
                            alert(data.msg||data);
                        }
                    },
                    error: function (errTxt) {
                        alert(errTxt)
                    }
                })
            })
            $("body").append(imgdon);
            setTimeout(function () {
                $(imgdon).animate({top: $H}, 6000, function () {
                    imgdon.remove()
                });
            })
            setTimeout(redpacket, Math.random() * 1000)
        }
            $.post('/redrain/getRainSwitch.aspx',{title: 'redrain',platform:"pc"},function(data){
                if (data.code == '200') {
                    redpacket()
                }
            })
    })
</script>
</body>
</html>