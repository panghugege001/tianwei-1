<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0"> 
		<title>庆圣诞迎元旦</title>
		<link rel="stylesheet" href="css/index.css?v=6" />
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
   </head>
	<body> 
		<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>
		<div class="bannar_box">
			<div class="bannar_no1 bannar_index" style="display: none;">
				<div class="container">
					<div class="shengdan_text">
						<img src="img/shengdan_text.png" />
					</div>		
					<div class="qiaozhong">
						<ul>
							<li><span onclick="click_qiao()"></span></li>
							<li><label id="user_size">0</label></li>
						</ul>
					</div>					
				</div>
			</div> 
			<div class="bannar_no2 bannar_index">
				<div class="container">
					<div class="yuandan_text">
						<img src="img/yuandan_text.png" />
					</div>							
				</div> 
			</div>			
		</div>
		<div class="container tab_qiehuan" style="display: none;">
			<ul>
				<li><img src="img/shengdan_btn_1.png"></li>
				<li><img src="img/shengdanbtn_2.png"></li>
			</ul>
		</div>
		<div class="container box_1 yuansheng" style="display: none;">
			<div class="box_heiadr">
				<p>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一年一度的圣诞节终于到来啦！在圣诞节这一天，传说圣诞老公公会把愿望都实现～而在这个应景的节庆里，
					天威推出圣诞节活动，要帮助大家在圣诞节，把美丽的愿望都实现～赶紧来到天威圣诞树下，跟着一同许愿，
					让天威把你圣诞愿望都实现吧！					
				</p>
			</div>
			<div class="box_body">
				<div class="box_body_title">活动时间：<span>2017年12月18日—2017年12月24日</span></div>
				<table cellpadding="0" cellspacing="0">
					<tr>
						<th>参加人数</th>
						<th>每位参加会员可获奖金</th>
						<th>流水倍数</th>
					</tr>
					<tr>
						<td>达到100人</td>
						<td>18</td>
						<td>10倍</td>
					</tr>
					<tr>
						<td>达到300人</td>
						<td>38</td>
						<td>10倍</td>
					</tr>
					<tr>
						<td>达到500人</td>
						<td>68</td>
						<td>10倍</td>
					</tr>
					<tr>
						<td>达到1000人</td>
						<td>88</td>
						<td>10倍</td>
					</tr>
					<tr>
						<td>达到1500人</td>
						<td>138</td>
						<td>10倍</td>
					</tr>
					<tr>
						<td>达到2000人</td>
						<td>168</td>
						<td>10倍</td>
					</tr>					
				</table>
				<p class="h_60">
					例：敲钟人数达到1000人，则每位敲钟会员皆可获得彩金88元，只需10倍流水即可提款
				</p>
				<div class="box_1_foot">
					<h3>活动规则</h3>
					<p>
						1、天威圣诞树！越多会员参加，优惠越丰富！<br />
						2、只要在活动期间，2017年12月18日—2017年12月24日活动期间内，累计存款达到1000，即可自助报名参加。<br />
						3、在活动截止前，敲钟会员达到特定人数，每位敲钟会员皆可获得特定优惠。<br />
						4、每位符合敲钟资格之会员可享有一次敲钟机会。<br />
						5、活动彩金在活动结束后，次日18点之前派发，达到流水倍数即可提款。<br />
						6、本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡，<br />
						      同一电脑等其他任何不正常投注行为，一经发现，天威将保留冻结帐户盈利及余额的权利。<br />
						7、此活动天威具有最终解释权。					
					</p>
				</div>				
			</div>
		</div>
		<div class="container box_2 yuansheng">
			<div class="box_heiadr">
				<p>
					新年福来到，旺财送大礼				
				</p>
			</div>
			<div class="box_body">
				<div class="box_body_title">活动时间：<span>2018年1月1日</span></div>
				<table cellpadding="0" cellspacing="0">
					<tr>
						<th>活动对象</th>
						<th>当日存款</th>
						<th>彩金</th>
						<th>流水倍数</th>
					</tr>
					<tr>
						<td>全体会员</td>
						<td>888</td>
						<td>88</td>
						<td>10倍</td>
					</tr>
				</table>
				<div class="box_1_foot">
					<h3>活动规则</h3>
					<p>
						1、会员达到指定存款即可获得对应彩金。<br />
						2、此优惠联系在线客服申请。<br />
						3、存款计算时间2018年1月1号 00:00:00-23:59:59。<br />
						4、达到流水倍数即可提款。<br />
						5、本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡，
						      同一电脑等其他任何不正常投注行为，一经发现，天威将保留冻结帐户盈利及余额的权利。<br />
						6、此活动天威具有最终解释权。					
					</p>
				</div>				
			</div>
		</div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>	
	</body>
	<script>
		$(function(){
			$(".tab_qiehuan ul li").click(function(){
				$(".bannar_index").hide();
				$(".bannar_index").eq($(this).index()).show();
				$(".yuansheng").hide();
				$(".yuansheng").eq($(this).index()).show();
				
			})
			
		})
	</script>
	<script>
		
		function maasges(msg){
			var $html=$("<div class='msgbj'><div class='mass_box'><div class='mass_title'>温馨提示<span class='guanbi'></span></div><div class='mas_body'>"+msg+"</div></div></div>");
			$("body").append($html);
		}
		
			function click_qiao(){
				$.post('/asp/chrismesCount.aspx',function(data){
					maasges(data)
				})
			}
			
			$(function(){
				$.get('/asp/queryChrismesCount.aspx',function(data){
					$("#user_size").text(data+730)
				})
			})		
			
	</script>
	
	<script>
		$(function(){
        $(document).on('click','.msgbj',function(){
			$(".msgbj").hide();
        });	
		})
	</script>
	
</html>