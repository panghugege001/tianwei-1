<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<style>
		.content-box {
			margin-bottom: 2rem;
			border: .2rem solid #c8c8c8;
			border-radius: .5rem;
			background: url(/mobile/images/contentBg.png?v=2) bottom repeat, #fff
		}
		
		.mui-select label,
		.mui-textfield>label {
			position: inherit;
			display: initial
		}
		
		.mui-select:after {
			top: 2.2em
		}
		
		.mui-textfield--float-label>img {
			top: 55%;
			right: 0
		}
		
		.mui-textfield>label {
			line-height: inherit
		}
		
		.mui-btn {
			margin: 0
		}
		
		.mui-btn+.mui-btn {
			margin: 0;
			padding: 0
		}
		
		.content-info table {
			width: 100%;
			border-collapse: collapse;
			border-spacing: 0;
			text-align: center;
			line-height: 1.5em
		}
		
		.conten-infot>ul>li {
			display: inline-block;
			width: 100%;
			border-bottom: 1px solid #eee;
			background-color: #fff
		}
		
		.content-info>ul>li.active .title .active-img {
			transform: rotate(90deg);
			-ms-transform: rotate(90deg);
			-moz-transform: rotate(90deg);
			-webkit-transform: rotate(90deg);
			-o-transform: rotate(90deg)
		}
		
		.content-info>ul>li.active .page {
			display: block
		}
		
		.content-info>ul>li .title {
			position: relative;
			width: 100%;
			padding: .3em 0 .3em .8em;
			letter-spacing: 2px;
			text-align: left;
			background-color: #fff
		}
		
		.content-info>ul>li .title>.active-img {
			display: inline-block;
			float: right;
			margin-right: 15px;
			transform: rotate(0deg);
			-ms-transform: rotate(0deg);
			-moz-transform: rotate(0deg);
			-webkit-transform: rotate(0deg);
			-o-transform: rotate(0deg);
			transition: transform .5s ease-in-out;
			-webkit-transition: transform .5s ease-in-out;
			-moz-transition: transform .5s ease-in-out;
			-o-transition: transform .5s ease-in-out;
			-ms-transition: transform .5s ease-in-out;
			width: .6em;
			margin-top: 1.2em;
		}
		
		.content-info>ul>li .page {
			display: none
		}
		.table{
			margin-bottom: 30px;
		}
		
		.content-info>ul>li .page .content {
			display: inline-block;
			padding: .5em;
			overflow: hidden;
			line-height: 100%
		}
		
		.content-info>ul>li .page .content p {
			line-height: 1.5
		}
		
		.content-info>ul>li .page .content .title2 {
			line-height: 100%;
			text-align: center
		}
		
		.content-info>ul>li .page .content .title3 {
			line-height: 100%;
			text-align: center
		}
		
		.content-info>ul>li .page .content .panel {
			padding: .5em
		}
		
		.content-info>ul>li .page .content .panel ol {
			padding-left: 25px;
			line-height: 120%;
			letter-spacing: 1px
		}
		
		.content-info>ul>li .page .content .panel ol li {
			margin-bottom: .5em
		}
		
		.qiandao-right {
			padding: 10px;
			width: 625px;
			margin: 0 auto;
			background-color: #342923;
			margin-bottom: 50px;
			display: block;
		}
		
		.qiandao-left {
			float: left;
			width: 174px;
			height: 110px;
			border: 1px solid #dcdcdc;
			font-size: 14px;
			line-height: 20px;
			text-align: center
		}
		
		.qiandao-history {
			float: right;
			width: 92px;
			height: 36px;
			border-radius: 4px;
			background-color: #09affe;
			color: #fff;
			text-align: center;
			font-size: 1pc;
			line-height: 36px;
			cursor: pointer
		}
		
		.qiandao-history:hover {
			background-color: #7087c3
		}
		
		.just-qiandao {
			margin: 0 auto 10px;
			width: 100%;
			height: 50px;
			line-height: 50px;
			cursor: pointer;
			background-color: #09affe;
			color: #fff;
			text-align: center;
			font-size: 18px;
		}
		
		.qiandao-right .cycle {
			height: 85px;
			background-color: #fffff2;
			font-size: 18px;
			line-height: 85px;
			border-radius: 10px 10px 0 0;
			margin-bottom: 10px;
			color: #000;
		}
		
		.qiandao-right .cycle li {
			float: left;
			font-size: 25px;
			width: 85px;
			text-align: center;
		}
		
		.qiandao-top .done {
			background-color: #787876;
			cursor: no-drop;
		}
		
		.qiandao-notic {
			color: #b25d06;
			text-align: center;
			font-size: 14px
		}
		
		.qiandao-main {
			overflow: hidden;
			width: 100%;
		}
		
		.qiandao-list li {
			position: relative;
			float: left;
			margin: 0 2px 2px 0;
			width: 84px;
			height: 84px;
			background-image: url(../images/user/qiandao_day.png);
			background-position: 0 0
		}
		
		.qiandao-list li.date1 {
			background-position: -430px 0
		}
		
		.qiandao-list li.date2 {
			background-position: -516px 0
		}
		
		.qiandao-list li.date3 {
			background-position: 0 -86px
		}
		
		.qiandao-list li.date4 {
			background-position: -86px -86px
		}
		
		.qiandao-list li.date5 {
			background-position: -172px -86px
		}
		
		.qiandao-list li.date6 {
			background-position: -258px -86px
		}
		
		.qiandao-list li.date7 {
			background-position: -344px -86px
		}
		
		.qiandao-list li.date8 {
			background-position: -430px -86px
		}
		
		.qiandao-list li.date9 {
			background-position: -516px -86px
		}
		
		.qiandao-list li.date10 {
			background-position: 0 -172px
		}
		
		.qiandao-list li.date11 {
			background-position: -86px -172px
		}
		
		.qiandao-list li.date12 {
			background-position: -172px -172px
		}
		
		.qiandao-list li.date13 {
			background-position: -258px -172px
		}
		
		.qiandao-list li.date14 {
			background-position: -344px -172px
		}
		
		.qiandao-list li.date15 {
			background-position: -430px -172px
		}
		
		.qiandao-list li.date16 {
			background-position: -516px -172px
		}
		
		.qiandao-list li.date17 {
			background-position: 0 -258px
		}
		
		.qiandao-list li.date18 {
			background-position: -86px -258px
		}
		
		.qiandao-list li.date19 {
			background-position: -172px -258px
		}
		
		.qiandao-list li.date20 {
			background-position: -258px -258px
		}
		
		.qiandao-list li.date21 {
			background-position: -344px -258px
		}
		
		.qiandao-list li.date22 {
			background-position: -430px -258px
		}
		
		.qiandao-list li.date23 {
			background-position: -516px -258px
		}
		
		.qiandao-list li.date24 {
			background-position: 0 -344px
		}
		
		.qiandao-list li.date25 {
			background-position: -86px -344px
		}
		
		.qiandao-list li.date26 {
			background-position: -172px -344px
		}
		
		.qiandao-list li.date27 {
			background-position: -258px -344px
		}
		
		.qiandao-list li.date28 {
			background-position: -344px -344px
		}
		
		.qiandao-list li.date29 {
			background-position: -430px -344px
		}
		
		.qiandao-list li.date30 {
			background-position: -516px -344px
		}
		
		.qiandao-list li.date31 {
			background-position: 0 -430px
		}
		
		.qiandao-list li .qiandao-icon {
			position: absolute;
			top: 0;
			left: 0;
			z-index: 2;
			display: none;
			width: 85px;
			height: 85px;
			background: url(../images/user/gougou.png) no-repeat center center;
			background-position: 14px 21px;
		}
		
		.qiandao-list li.qiandao .qiandao-icon {
			display: block
		}
		
		.qiandao-layer {
			position: fixed;
			top: 0;
			bottom: 0;
			left: 0;
			z-index: 888;
			display: none;
			width: 100%
		}
		
		.qiandao-layer-bg {
			width: 100%;
			height: 100%;
			background-color: #000;
			opacity: .55;
			filter: alpha(opacity=55)
		}
		
		.qiandao-layer-con {
			position: absolute;
			top: 50%;
			left: 50%;
			z-index: 999;
			padding-top: 30px;
			border: 3px #33b23f solid;
			border-radius: 5px;
			background-color: #fff
		}
		
		.qiandao-history-layer .qiandao-layer-con {
			margin: -257px 0 0 -293px;
			width: 586px;
			height: 484px
		}
		
		.close-qiandao-layer {
			position: absolute;
			top: 13px;
			right: 13px;
			width: 1pc;
			height: 1pc;
			background-position: -228px -51px
		}
		
		.qiandao-history-inf {
			margin-top: 25px;
			color: #666;
			text-align: center;
			font-size: 14px
		}
		
		.qiandao-history-inf li {
			float: left;
			width: 25%
		}
		
		.qiandao-history-inf li h4 {
			color: #33b23f;
			font-size: 40px;
			line-height: 50px
		}
		
		.qiandao-history-table {
			overflow: hidden;
			margin: 20px;
			-webkit-border-radius: 5px 5px 0 0;
			-moz-border-radius: 5px 5px 0 0;
			border-radius: 5px 5px 0 0;
			-o-border-radius: 5px 5px 0 0;
			-ms-border-radius: 5px 5px 0 0
		}
		
		.qiandao-history-table table {
			width: 100%;
			color: #666;
			text-align: center;
			font-size: 1pc;
			border-spacing: 0
		}
		
		.qiandao-history-table table th {
			width: 33.3%;
			background-color: #f2f2f2;
			text-align: center;
			line-height: 40px
		}
		
		.qiandao-history-table td {
			width: 33.3%;
			border: 1px #e5e5e5 dashed;
			line-height: 34px
		}
		
		.qiandao-active .qiandao-layer-con {
			margin: -232px 0 0 -211px;
			width: 422px;
			height: 434px
		}
		
		.yiqiandao {
			margin: 36px 0 0 40px;
			color: #666;
			font-size: 14px;
			line-height: 38px
		}
		
		.yiqiandao .yiqiandao-icon {
			float: left;
			margin: 0 25px;
			width: 178px;
			height: 38px;
			background-position: -217px 0
		}
		
		.qiandao-jiangli {
			position: relative;
			margin: 45px auto;
			width: 335px;
			height: 170px;
			background-position: 0 -146px
		}
		
		.qiandao-jiangli span {
			position: absolute;
			top: 58px;
			left: 50px;
			display: block;
			width: 178px;
			height: 106px;
			color: #ff7300;
			text-align: center;
			font-weight: bolder;
			font-size: 30px;
			line-height: 106px
		}
		
		.qiandao-jiangli span em {
			padding-left: 5px;
			font-style: normal;
			font-size: 1pc
		}
		
		.qiandao-share {
			display: block;
			margin: 60px auto 0;
			width: 318px;
			height: 3pc;
			border-radius: 5px;
			background-color: #4ab854;
			color: #fff;
			text-align: center;
			text-decoration: none;
			font-size: 18px;
			line-height: 3pc
		}
		
		.qiandao-share:hover {
			background-color: #3e9d46
		}
		
		.tab-sign-style {
			display: none;
		}
		
		.mui-btn {
			margin: 0
		}
		
		.mui-btn+.mui-btn {
			margin: 0;
			padding: 0
		}
		
		.check-btn-styl {
			text-align: center;
			padding: 3em 0 2em;
		}
		
		.check-btn-styl .yel-btn-styl {
			background-color: #EE6325;
			color: #000;
			opacity: 1;
			float: none;
			display: inline-block;
			height: 50px;
			line-height: 50px;
			color: #FFFFFF;
		}
		
		.check-btn-styl .checkaccount {
		    color: #fff;
		    position: relative;
		    top: 5px;
		    left: 20px;
		    /* display: inherit; */
		    background: red;
		    width: 40%;
		    /* margin: 0 auto; */
		    /* margin-top: -18px; */
		    display: inline-block;
		    height: 50px;
		    line-height: 50px;
		}
		
		.qiandao-right .current-date {
			text-align: center;
			font-size: 25px;
		}
		
		.current-date.pd2em {
			padding: 1em 0;
			color: #fff;
		}
		
		#tab-checkInInfo .table thead th {
			background-color: #09affe;
			width: 1%;
			height: 50px;
			font-size: 14px;
			font-weight: normal;
			text-align: center;
			color: #fff;
		}
		
		#tab-checkInInfo .table tbody td {
			width: 1%;
			height: 50px;
			font-size: 14px;
			font-weight: normal;
			text-align: center;
		}
		/* #tab-checkInInfo .table {border: 1px solid #ccc;} */
		
		.table th{
			border: 1px solid #ccc;
			line-height: 34px;
			text-align: center;				
		}
		.table td {
			border: 1px solid #ccc;
			line-height: 34px;
			text-align: center;			
		}
		
		.close-acc {
		    display: inline-block;
		    /* padding: 5px 90px; */
		    background-color: #EE6325;
		    color: #fff;
		    position: absolute;
		    top: -45px;
		    display: inline-block;
		    width: 46%;
		    padding: 10px;
		    left: 50%;
		    margin-left: -22%;
		}
		
		.qd_mpney {
			color: #FFFFFF;
			line-height: 30px;
			text-align: center;
			padding-bottom: 20px;
		}
		
		.close-btn-wp {
			height: 5rem;
			text-align: center;
		}
		
		@media screen and (max-width: 768px) {
			.qiandao-right {
				zoom: 0.6;
			}
		}
		
		@media screen and (max-width: 480px) {
			.qiandao-right {
				zoom: 0.56;
			}
		}
		
		@media screen and (max-width: 375px) {
			.qiandao-right {
				zoom: 0.48;
			}
		}
		
		@media screen and (max-width: 320px) {
			.qiandao-right {
				zoom: 0.4;
			}
		}
		
		.c-red {
			color: #e40200;
		}
		.muee{
	    position: absolute;
	    top: 84px;
	    /* width: 654px; */
	    left: 50%;
	    margin-left: -95px;		
		}
		#xianshi{ display: none;}
		.muee ul li{
		    float: left;
		    background-color: #EE6325;
		    margin-left: 10px;
		    color: #fff;
		    padding: 5px 10px;		
		}
		.muee ul .acction{
		    background: red;
		}
		
		.xianshi table{ width: 100%;}
	</style>

	<body>
			<div class="muee">
				<ul>
					<li class="acction">签到中心</li>
					<li>签到说明</li>
				</ul>
			</div>	
		<div class="content" style="padding:0 1rem;position: relative; margin-top: 120px;">
			<div class="tab-sign disp">
				<!-- 				<div style="padding: 20px 30px;" class="mui-textfield">
	<label style="font-size:14px">累计签到奖金:</label>
			  	  	<input style="margin-top: 10px;" id="sign-balance" type="text" readonly />
			  	</div> -->
				<div class="qiandao-right">
					<div class="qiandao-left-top clearfix">
						<div class="current-date pd2em">
						</div>
						<p class="qd_mpney">签到余额<span id="todayGet">0.00</span></p>
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
						<div class="mui-buttons mui-col-xs32-12 check-btn-styl">
							<button class="yel-btn-styl mui-btn mui-btn--raised block mui-col-xs32-6 btn_qiandao" id="j-qdA">签到</button>
						</div>
					</div>
				</div>

				<span class="space-1"></span>
			</div>
			<div class="xianshi disp" id="xianshi">
				<!-- 				<div class="point-money sing-balance-box">签到余额：<span id='sign-balance'></span></div> -->
				<!-- 				<p class="top-text" style="padding:20px;position:relative;"><span class="c-red"></span></p> -->

				<table class="table" cellspacing="1">
					<thead>
						<tr>
							<th></th>
							<th>每日签到</th>
							<th>日存款要求</th>
							<th>最大提款</th>
							<th>累积7日</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>新会员</td>
							<td>3元</td>
							<td>50元</td>
							<td>888</td>
							<td>18元红包</td>
						</tr>
						<tr>
							<td>忠实vip</td>
							<td>6元</td>
							<td>100元</td>
							<td>3888</td>
							<td>28元红包</td>
						</tr>
						<tr>
							<td>星级vip</td>
							<td>8元</td>
							<td>300元</td>
							<td>5888</td>
							<td>38元红包</td>
						</tr>
						<tr>
							<td>黄金vip</td>
							<td>10元</td>
							<td>500元</td>
							<td>8888</td>
							<td>68%存送</td>
						</tr>
						<tr>
							<td>白金vip</td>
							<td>15元</td>
							<td>700元</td>
							<td>12888</td>
							<td>88％存送</td>
						</tr>		
						<tr>
							<td>钻石vip</td>
							<td>30元</td>
							<td>无要求</td>
							<td>21888</td>
							<td>100%存送</td>
						</tr>	
						<tr>
							<td>至尊vip</td>
							<td>50元</td>
							<td>无要求</td>
							<td>38888</td>
							<td>120%存送</td>
						</tr>							
					</tbody>
				</table>
				<p class="c-red">连续签到7日，并达到每日存款要求，即可领取红包/存送优惠券</p>
				<p>1.转帐要求是根据您当日存款，达到存款要求后签到奖金自动开启户内转帐。</p>
				<p>2.所有签到奖金流水需要达到10倍流水即可提款，提款金额不得超过最大提款的额度。</p>
				<p>3.签到奖金每月最后一天的23:59:59自动清0，请及时进行游戏。</p>
				<p>4.签到彩金需转入PT/DT/MG/TTG/NT/QT，任一平台进行游戏。</p>
				<p>5.连续签到7日，并达每日的存款要求，即可向在线客服领取红包/存送优惠券（请在三天内联系在线客服，在线客服会为您派发红包/存送优惠券！)逾期则视为自动放弃。</p>

				<div class="haclass"><p class="close-btn-wp" style="margin-top: 20px; display: none;"><span class="checkTab close-acc">关闭说明</span></p></div>
			</div>
 
		</div>
		<script type="text/javascript">
			//签到说明
			$('.checkTab').on('click', function() {
				$(this).parents('.tab-sign').hide().siblings().show();
			});
		</script>
		<script>
			$(function() {
				var signFun = function() {

					$(".clearfix").find("li").eq(0).addClass("active")

					var dateArray = [];

					var $dateBox = $("#js-qiandao-list"),
						$currentDate = $(".current-date"),
						$qiandaoBnt = $("#j-qdA"),
						_html = '',
						_handle = true,
						myDate = new Date();
					$currentDate.text(parseInt(myDate.getMonth() + 1) + '月份签到');

					var monthFirst = new Date(myDate.getFullYear(), parseInt(myDate.getMonth()), 1).getDay();

					var d = new Date(myDate.getFullYear(), parseInt(myDate.getMonth() + 1), 0);
					var totalDay = d.getDate();

					for(var i = 0; i < 42; i++) {
						_html += ' <li><div class="qiandao-icon"></div></li>'
					}
					$dateBox.html(_html)

					var $dateLi = $dateBox.find("li");
					$.ajax({
						type: "get",
						url: "/asp/findSignrecord.aspx",
						data: "",
						cache: false,
						async: true,
						success: function(data) {
							var obj = eval(data);
							for(var i = 0; i < obj.length; i++) {
								var day = new Date(obj[i].timeStr).getDate() - 1;
								dateArray.push(day);

							}
							for(var i = 0; i < totalDay; i++) {
								$dateLi.eq(i + monthFirst).addClass("date" + parseInt(i + 1));
								for(var j = 0; j < dateArray.length; j++) {
									if(i == dateArray[j]) {
										$dateLi.eq(i + monthFirst).addClass("qiandao");
									}
								}
							}
						},
						error: function() {

						}
					});

					$(".date" + myDate.getDate()).addClass('able-qiandao');

					$dateBox.on("click", "li", function() {
						if($(this).hasClass('able-qiandao') && _handle) {
							$(this).addClass('qiandao');

						}
					})

					function qiandaoFun() {
						$qiandaoBnt.html('已签到');
					}

					function qianDao() {
						$(".date" + myDate.getDate()).addClass('qiandao');
					}
				}();

				function openLayer(a, Fun) {
					$('.' + a).fadeIn(Fun)
				}

				var closeLayer = function() {
					$("body").on("click", ".close-qiandao-layer", function() {
						$(this).parents(".qiandao-layer").fadeOut()
					})
				}();

				$("#js-qiandao-history").on("click", function() {
					openLayer("qiandao-history-layer", myFun);

					function myFun() {
						console.log(1)
					}
				})

				$.get('asp/checkSignRecord.aspx', function(data) {
					if(data === true) {
						$("#j-qdA").html("已签到")
					} else {
						$("#j-qdA").html("未签到(点击签到)")
					}
				})

				$(".btn_qiandao").click(function() {
					$.ajax({
						url: "/asp/doSignRecord.aspx",
						type: "post", // 请求方式
						dataType: "text", // 响应的数据类型
						data: "",
						async: true, // 异步
						success: function(msg) {

							alert(msg);
							if(msg.indexOf("不满足") > 0) {
								return;
							} else {
								$("#j-qdA").html("已签到")
								checkInSignAmount();

							}
						},
					});
				})

				//查询签到总余额
				function checkInSignAmount() {
					$.ajax({
						url: "/asp/querySignAmount.aspx",
						type: "post", // 请求方式
						dataType: "text", // 响应的数据类型
						data: "",
						async: true, // 异步
						success: function(msg) {
							$("#todayGet").html("");
							$("#todayGet").html(Math.floor(msg) + "元");
							//					$("#j-check_in2").html("");
							//					$("#j-check_in2").html(msg+"元");
						},
					});
				}
				checkInSignAmount();
				
				$(".muee ul li").click(function(){
				$(".muee ul li").removeClass("acction");
				$(this).addClass("acction");
				$(".content").find(".disp").hide();
				$(".content").find(".disp").eq($(this).index()).show();
				})
			});	
		</script>
	</body>

</html>