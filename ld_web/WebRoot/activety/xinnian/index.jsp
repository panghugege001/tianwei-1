<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0"> 
		<title>新年钜惠</title>
		<link rel="stylesheet" href="css/index.css?V=2" />
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
   </head>
	<body> 
		<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>
		<div class="new_ye"></div>
			<div class="w_1200">
				<div class="w_1030">
					<ul>
						<li class="action"><img src="img/yuandan_text.png"></li>
						<li><img src="img/xinnian_text.png"></li>
					</ul>
				</div>
				<div class="xinnian_beijing">
					<p>
						活动对象：天威全体会员。<br>
						活动时间：2018年1月1日起。<br>
						活动内容：1月1日当天存款达888，除了赠送88元旦红包除外，天威在1/2~1/7日加码每天赠送红包，彩金领不停，天威给您新年新希望。						
					</p>
					<div class="xinnian_table">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>活动对象</th>
								<th>元旦每日加赠彩金 </th>
								<th>流水倍数</th>
								<th>最高提款</th>
							</tr>
							<tr>
								<td>新会员</td>
								<td>18</td>
								<td>8</td>
								<td>888</td>
							</tr>	
							<tr>
								<td>忠实及以上</td>
								<td>28</td>
								<td>8</td>
								<td>3888</td>
							</tr>								
						</table>						
					</div>
					<p>
					温馨提示：天威元旦加赠金仅限于1月1日达到存款888要求的玩家领取哦！<br />					
					活动规则：<br />
					1、需在1月1日存款达到888额度的记录，才可以领取天威加赠彩金。<br />
					2、此优惠无需申请，会在当日的18:00已红包优惠卷代码方式派发至站内信，在复制红包代码至手机
					      红包优惠券使用。<br />
					3、此加赠优惠彩金达到流水即可提款。<br />
					4、此优惠与反水共享。<br />
					5、为了提高娱乐性，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，
					      同一住址，同一银行卡和发现玩家非法套利优惠，天威有权将红利扣除并冻结账号。<br />
					6、天威享有修订权和最终解释权。						
					</p>
				</div>				
				<div class="xinnian_beijing" style="display: none;">
					<p>
						活动对象：2018年1月1日~1月7日期间新注册会员。<br>
						活动时间：2018年1月1日~1月31日。<br>
						活动内容：1月1日~ 1月7日期间新注册会员在期间内存款达到相对应的要求，就在8日、18日、28日、额外加赠额外红包，给您满满的新希望						
					</p>
					<div class="xinnian_table">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>1日至7日累计金额</th>
								<th>8、18、28日加赠彩金 </th>
								<th>流水倍数</th>
							</tr>
							<tr>
								<td>300-499</td>
								<td>18</td>
								<td>8</td>
							</tr>
							<tr>
								<td>500-999</td>
								<td>28</td>
								<td>8</td>
							</tr>
							<tr>
								<td>1000-2999</td>
								<td>38</td>
								<td>8</td>
							</tr>
							<tr>
								<td>3000-4999</td>
								<td>58</td>
								<td>8</td>
							</tr>	
							<tr>
								<td>5000及以上</td>
								<td>68</td>
								<td>8</td>
							</tr>						
						</table>						
					</div>
					<p>
						活动规则：<br />
						1、需在1月1日至1月7日存款达对应累积金额，就可以领取加赠红包。<br />
						2、此优惠无需申请，会在当日的18:00已红包优惠卷代码方式派发至站内信，在复制红包代码至手机<br />
						      红包优惠券使用。<br />
						3、此加赠优惠彩金达到流水即可提款。<br />
						4、此优惠与反水共享。<br />
						5、为了提高娱乐性，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，
						      同一住址，同一银行卡和发现玩家非法套利优惠，天威有权将红利扣除并冻结账号。<br />
						6、天威享有修订权和最终解释权。						
					</p>
				</div>
			</div>		
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>	
	</body>
	<script>
		$(function(){
			$(".w_1030 ul li").click(function(){
				$(".w_1030 ul li").removeClass("action");
				$(this).addClass("action");
				$(".w_1200").find(".xinnian_beijing").hide();
				$(".w_1200").find(".xinnian_beijing").eq($(this).index()).show();
			})
		})
	</script>	
</html>