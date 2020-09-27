<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <title>瑞狗迎春</title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
    <link rel="stylesheet" href="/css/base.css?v=2"/>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <style>
        body {
            background: url("./img/bg.png");
        }

        .newyear-dog {
            background: url("./img/dog.jpg") top no-repeat;
            padding-top: 1072px;
            padding-bottom: 250px;
            background-size: 100%;
            color: #333333;
        }

        .newyear-nav {
            width: 1200px;
            margin: 0 auto;
            position: relative;
        }
        .newyear-nav a img{
            transition: all 0.5s;
        }
        .newyear-nav a img:hover{
            transform:scale(1.2);
        }
        .newyear-nav a img, .newyear-nav a.active img.active {
            display: inline-block;
        }

        .newyear-nav a img.active, .newyear-nav a.active img {
            display: none;
        }

        .newyear-nav a.nav1 {
            position: absolute;
            left: -230px;
            top: 63px;
        }

        .newyear-nav a.nav2 {
            position: absolute;
            right: -230px;
            top: 63px;
        }

        .newyear-dog table {
            border-spacing: 1px;
            border-collapse: inherit;
            font-size: 20px;
            text-align: center;
            margin: 20px 0;
            width: 100%;
        }

        .newyear-dog table th {
            color: #fff7d2;
            text-align: center;
            background-color: #d41e1c;
            height: 50px;
            font-size: 22px;
        }

        .newyear-dog table td {
            height: 40px;
            color: #333;
            padding: 0;
            margin: 0;
            background-color: #deb682;
        }

        .newyear-panel .title {
            color: #d41d1c;
            text-align: center;
            font-size: 58px;
            line-height: 1;
            padding: 0 70px 44px;
            font-weight: bold;
            background: url("./img/line.png") bottom no-repeat;
            margin-bottom: 38px;
        }

        .newyear-panel small {
            display: block;
            color: #333;
            margin-top: 30px;
            font-weight: 400;
            font-size: 24px;
            line-height: 44px;
        }

        .newyear-panel p {
            margin: 10px 0;
            line-height: 33px;
            font-size: 20px;
        }

        .newyear-panel span {
            color: #b68039;
        }

        .newyear-panel {
            width: 1200px;
            margin: 0 auto;
            font-size: 20px;
            padding: 100px 97px 0;
            height: 1457px;
            position: relative;
            background: url("./img/conentbg.png") no-repeat;
        }

        .newyear-panel .cloud1 {
            position: absolute;
            top: 110px;
            left: -50px;
        }

        .newyear-panel .cloud2 {
            position: absolute;
            top: 296px;
            right: -82px;
        }

        .newyear-panel .cloud3 {
            position: absolute;
            bottom: 466px;
            right: -304px;
        }

        .newyear-panel .cloud4 {
            position: absolute;
            bottom: 41px;
            left: -184px;
        }

        .newyear-panel .cloud5 {
            position: absolute;
            bottom: -70px;
            right: -226px;
        }

        @media screen and (max-width: 750px) {
            .newyear-dog {
                padding: 50% 0 0;
            }

            .newyear-panel > img {
                display: none !important;
            }

            .newyear-panel {
                background: none;
                padding: 5%;
                width: auto;
            }

            .newyear-panel .title {
                background: none;
                padding: 0;
                line-height: 1.2;
            }

            .newyear-nav a.nav1 {
                z-index: 1;
                position: fixed;
                left: 0;
                top: 10%;
            }

            .newyear-nav a.nav2 {
                z-index: 1;
                position: fixed;
                right: 0;
                top: 10%;
            }

            .newyear-nav img {
                width: 40%;
            }
        }
    </style>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>
<body>
<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>
<div class="newyear-dog">
    <div class="newyear-nav">
        <a class="nav1 active" href="javascript:void(0)">
            <img src="./img/nav1.png" alt="">
            <img src="./img/nav11.png" alt="" class="active">
        </a>
        <a class="nav2" href="javascript:void(0)">
            <img src="./img/nav2.png" alt="">
            <img src="./img/nav22.png" class="active" alt="">
        </a>
    </div>
    <div class="panel-warp">
        <div class="newyear-panel" >
            <img class="cloud1" src="./img/cloud1.png" alt="">
            <img class="cloud2" src="./img/cloud2.png" alt="">
            <img class="cloud3" src="./img/cloud3.png" alt="">
            <img class="cloud4" src="./img/cloud4.png" alt="">
            <img class="cloud5" src="./img/dog.png" alt="">
            <div class="title">
                年度回馈礼包 与您一同迎新年
                <small>盼望的是美好，送来的是祝福，愿天威所有会员在新的一年心想事成、新年快乐！</small>
            </div>
            <p><span>活动对象：</span>2017年期间注册会员。</p>
            <p><span>活动时间：</span>2018年2月15日。</p>
            <p><span>活动内容：</span>天威为感谢您这一年来的支持与鼓励，年度回馈礼包最高16888元，与您一同迎新年。</p>
            <table>
                <tr>
                    <th>活动对象</th>
                    <th>累计存款</th>
                    <th>回馈礼金</th>
                    <th>流水倍数</th>
                </tr>
                <tr>
                    <td rowspan="10">2017年期间注册会员</td>
                    <td>5000-10000</td>
                    <td>28</td>
                    <td rowspan="10">8倍</td>
                </tr>
                <tr>
                    <td>10001以上</td>
                    <td>38</td>
                </tr>
                <tr>
                    <td>20001以上</td>
                    <td>68</td>
                </tr>
                <tr>
                    <td>50000以上</td>
                    <td>188</td>
                </tr>
                <tr>
                    <td>100,000以上</td>
                    <td>388</td>
                </tr>
                <tr>
                    <td>500,000以上</td>
                    <td>588</td>
                </tr>
                <tr>
                    <td>1,000,000以上</td>
                    <td>2888</td>
                </tr>
                <tr>
                    <td>3,000,000以上</td>
                    <td>5888</td>
                </tr>
                <tr>
                    <td>5,000,000以上</td>
                    <td>8888</td>
                </tr>
                <tr>
                    <td>8,000,000以上</td>
                    <td>16888</td>
                </tr>
            </table>
            <span>活动规则：</span>
            <p>1、天威注册会员累计存款达到即可领取回馈礼包。</p>
            <p>2、注册时间计算：2017/1/1 至2017/12/31 期间注册的会员。</p>
            <p>3、存款计算：2017/1/1 至2017/12/31 累计存款。</p>
            <p>4、彩金无需申请，活动结束后次日18点前派发至您站内信， 达到流水倍数即可提款。</p>
            <p>5、本优惠活动只针对娱乐性质的会员，一个帐号只限领取一次，如发现用户拥有超过一个账户，
                包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡， 同一电脑等其他任
                何不正常投注行为，一经发现，天威将保留冻结帐户盈利及余额的权利。</p>
            <p> 6、此活动天威具有最终解释权。</p>
        </div>
        <div style="display: none" class="newyear-panel">
            <img class="cloud1" src="./img/cloud1.png" alt="">
            <img class="cloud2" src="./img/cloud2.png" alt="">
            <img class="cloud3" src="./img/cloud3.png" alt="">
            <img class="cloud4" src="./img/cloud4.png" alt="">
            <img class="cloud5" src="./img/dog.png" alt="">
            <div class="title">
                2018瑞狗迎新春 快乐过新年
                <small> 送上天威一份真诚的祝福，愿您在新的一年里，拥有更安康、更快乐的时光， 祝您新年快乐。欢乐天威过年打虎乐逍遥。</small>
            </div>

            <p><span>活动对象：</span>天威全体会员。</p>
            <p><span>活动时间：</span>2018年2月15日 — 2018年2月21日。</p>
            <p><span>活动内容：</span>春节打虎乐逍遥，春节黄金周完成活动条件即可领取对应打虎彩金，新年彩金领不完、
                快快乐乐过好年。</p>
            <table>
                <tr>
                    <th>日期</th>
                    <th>存款</th>
                    <th>打虎金额</th>
                </tr>
                <tr>
                    <td>2月15日</td>
                    <td>50</td>
                    <td>30元</td>
                </tr>
                <tr>
                    <td>2月16日</td>
                    <td>100</td>
                    <td>30元</td>
                </tr>
                <tr>
                    <td>2月17日</td>
                    <td>150</td>
                    <td>90元</td>
                </tr>
                <tr>
                    <td>2月18日</td>
                    <td>300</td>
                    <td>50%</td>
                </tr>
                <tr>
                    <td>2月19日</td>
                    <td>500</td>
                    <td>50%</td>
                </tr>
                <tr>
                    <td>2月20日</td>
                    <td>1000</td>
                    <td>50%</td>
                </tr>
                <tr>
                    <td>2月21日</td>
                    <td>2000</td>
                    <td>1200元</td>
                </tr>
                <tr>
                    <td>连续七日</td>
                    <td>完成以上存款额度</td>
                    <td>免费赠送1888</td>
                </tr>
            </table>
            <span>活动规则：</span>
            <p>1、参加瑞狗新年活动会员，达到当日最低存款即可获得对应新年打虎彩金。</p>
            <p>2、申请方式：存款后请勿移动金额→帐户管理→自助优惠→打虎彩金进行申请，完成流水即可提款。</p>
            <p>3、完成黄金周连续七日活动，可向在线客服申请1888元免费彩金。</p>
            <p>4、为了提高娱乐性，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，
                同一住址，同一银行卡和发现玩家非法套利优惠，天威有权将红利扣除并冻结账号。</p>
            <p>5、天威享有修订权和最终解释权。
        </div>
    </div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script src="/js/jquery/jquery-1.7.2.min.js"></script>
<script>
    $(function () {
        $('.newyear-nav a').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            $('.panel-warp .newyear-panel').eq($(this).index())
                .show().siblings().hide();
        })
    })
</script>
</body>
</html>