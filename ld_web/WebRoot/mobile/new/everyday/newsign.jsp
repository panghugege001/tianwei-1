<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="每日签到" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
		<style>
			.form-warp .btn-submit[disabled] {
				background: #dfa85a;
			}
		</style>
	</head> 

	<body>

		<div id='contents'>
			<div class="sign-calendar">
				<div class="title"><span class="current-date"></span>月份<span id="signhtml"></span></div>
				<ol class="cycle">
					<li>周日</li>
					<li>周一</li>
					<li>周二</li>
					<li>周三</li>
					<li>周四</li>
					<li>周五</li>
					<li>周六</li>
				</ol>
				<ul class="qiandao-list clearfix" id="js-qiandao-list">
				</ul>
			</div>
			<div class="sign-bottom">
				<div class="title">
					签到余额<span id="todayGet" class="c-ylow">0.00</span>
					<span id='sign-explain' class="sign-explain">签到说明</span>
				</div>
				<div class="text-tips">
					<div class="h3">温馨提示</div>
					连续签到7日，并达到每日存款要求， <br />即可领取红包存送优惠券。
				</div>
			</div>
			<div class="form-warp">
				<button class="btn-submit" disabled='' id="j-qdA">签到</button>
			</div>
			<br /><br />
		</div>
		<div class="hidden m-grid-table" id="xianshi">
				<div id="page-detail" class="preferential-etail" style="display: block;">
				<div class="j-content"><div class="promotion-content"><div class="modal-dialog" role="document"> 
		<div class="modal-content">
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
	</div>	
		</div>
				<div class="haclass">
					<p class="btn-submit js-closess" style=" margin: 0 auto; color: #fff; width: 80%; background: #dfa85a; height: 35px; text-align: center; line-height: 35px; border-radius: 10px;cursor: pointer;" >关闭说明</p>
				</div>
				<br />
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			$(function() {
				//签到说明
				$('#sign-explain,.js-closess').on('click', function() {
					$("#xianshi,#contents").toggle();
				});
				var signFun = function() {
					$(".clearfix").find("li").eq(0).addClass("active")

					var dateArray = [];

					var $dateBox = $("#js-qiandao-list"),
						$currentDate = $(".current-date"),
						$qiandaoBnt = $("#j-qdA"),
						_html = '',
						_handle = true,
						myDate = new Date();

					$currentDate.text(parseInt(myDate.getMonth() + 1));
					var monthFirst = new Date(myDate.getFullYear(), parseInt(myDate.getMonth()), 1).getDay();
					var d = new Date(myDate.getFullYear(), parseInt(myDate.getMonth() + 1), 0);
					var totalDay = d.getDate();
					for(var i = 0; i < 42; i++) {
						_html += '<li></li>'
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
								$dateLi.eq(i + monthFirst).text(i + 1);
								for(var j = 0; j < dateArray.length; j++) {
									if(i == dateArray[j]) {
										$dateLi.eq(i + monthFirst).addClass("qiandao");
									}
								}
							}
						},
						error: function() {}
					});
				}();

				function openLayer(a, Fun) {

				}

				$("body").on("click", ".close-qiandao-layer", function() {
					$(this).parents(".qiandao-layer").fadeOut()
				})

				$.get('asp/checkSignRecord.aspx', function(data) {
					if(data === true) {
						$("#j-qdA,#signhtml").html("已签到")
					} else {
						$("#j-qdA").html("未签到(点击签到)")
						$("#signhtml").html("未签到")
						$("#j-qdA").removeAttr('disabled')
					}
				})
				$("#j-qdA").click(function() {
					$.ajax({
						url: "/asp/doSignRecord.aspx",
						type: "post", // 请求方式
						dataType: "text", // 响应的数据类型
						async: true, // 异步
						success: function(msg) {
							alert(msg);
							if(msg.indexOf("不满足") > 0) {
								return;
							} else {
								$("#j-qdA").attr('disabled', '')
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
							$("#todayGet").html(Math.floor(msg) + "元");
						},
					});
				}
				checkInSignAmount();
				$(".muee ul li").click(function() {
					$(".muee ul li").removeClass("acction");
					$(this).addClass("acction");
					$(".content").find(".disp").hide();
					$(".content").find(".disp").eq($(this).index()).show();
				})
			});
		</script>
	</body>

</html>