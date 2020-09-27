<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<!DOCTYPE html>
<html>
    <head>
    	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>每日存送</title>
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<link rel="stylesheet" href="css/index.min.css?v=2" />
			<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    </head>
	<body>
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>	
		<style>
		.activety_header {
			background: #1f2531;
			}.activety_logo{
				left:-80px;
			}
		</style>
		<div class="wr-container">
			<div id="main">
				<div class="wr main-content">
					<!-- <a href="#" class="logo"><img src="img/logo.png" /></a> -->
					<div class="title-text" style="padding-top: 20px;">
						<h1>每日存、每日送</h1>
						<h2>最高1888</h2>
						<h4>助您一臂之力打虎更轻松</h4>
					</div>

					<!--table-->
					<div class="table">
						<table cellpadding="0" cellspacing="0">
							<tbody class="show_game">
								<tr>
									<td>
										<span class="title">活动对象</span>
										<p>天威全体会员</p>
									</td><td rowspan="2">
										<span class="title">温馨提示</span>
										<p>1.每天存送比例随机。</p>
										<p>2.每天有最低存款要求和最高彩金的要求。</p>
										<p>3.每天具体的活动内容，请参考官网弹窗与公告的通知。</p>
									</td>
								</tr>
								<tr>
									<td>
										<span class="title">活动时间</span>
										<p>2019年1月1日起</p>
									</td>
									
								</tr>
								<tr>
									<td colspan="2">
										<span class="title">活动内容</span>
										<table class="sm-table" border="0" cellspacing="0" cellpadding="0">
											<tbody><tr>
												<td>游戏平台</td>
												<td>存送比例</td>
												<td>流水倍数</td>
												<td>最高金额</td>
											</tr>
											<tr>
												<td>所有老虎机平台</td>
												<td>30%-200%</td>
												<td>20倍</td>
												<td>1888</td>
											</tr>
										</tbody></table>
									</td>
								</tr>
								<tr>
									<td class="noborder" colspan="2">
										<span class="title">活动规则</span>
										<div>
											<p>
												1、申请方式：个人中心-自助优惠-额外存送。申请此优惠后禁止玩PT“古怪猴子”和“疯狂7”两款游戏，若使用每日首存的彩金游玩，我们将有权冻结账号并收回盈利。
											</p>
											<p>
												2、本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡，同一电脑等其他任何不正常行为，我们将有权冻结账号并收回盈利。
											</p>
											<p>
												3、本活动天威娱乐享有最终解释权。
											</p>
										</div>
									</td>
								</tr>								
							</tbody>						
						</table>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
		<script>
			(function(win) {
				var pageWidth = 19.2; /////设计图宽度640/100
				var docEl = document.documentElement;
				windowScale = function() {
					docEl.style.fontSize = docEl.getBoundingClientRect().width / pageWidth +
						'px';
				}
				windowScale();
				window.onresize = windowScale;
			})(window);
		</script>
		<script>
			$(function(){
				$(".li_box ul li").click(function(){
					$(".li_box ul li").removeClass("action");
					$(this).addClass("action");
					$(".show_game").hide();
					$(".show_game").eq($(this).index()).show();
				})
			})
		</script>
	</body>
	
</html>