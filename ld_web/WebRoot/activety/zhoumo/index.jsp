<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0"> 
		<title>周末狂欢</title>
		<link rel="stylesheet" href="css/bootstrap.min.css?v=1" />
		<link rel="stylesheet" href="css/index.css?v=9" />
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
   </head>
	<body> 
		<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>
		<div class="zm_body_div">
			<div class="huabang"><img src="img/huabang.png"></div>			
			<div class="container">
				<div class="w_1000">
					<div class="title_text"><img src="img/zhoumotext.png"></div>
					<div class="heider_ul">
						<ul>
							<li>
								活动对象：<span>全体会员</span>
							</li>
							<li>
								活动时间：<span>2018年6月1日-6月30日</span>
							</li>
							<li>
								活动内容：<span>活动当天存款达到要求转入至MG或PNG平台，即可免费获得相应平台派发相对应<br>的奖励游戏局数，让您小额存款中大奖。</span>
							</li>
							<li>
								活动要求：<span>在指定活动时间内存款达到要求，即可在次日下午18:00前获得相对应的奖励局。</span>
							</li>
						</ul> 
					</div>
					<div class="zhoumo_table">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>活动时间</th>
								<th>当日存款要求</th>
								<th>免费局数</th>
								<th>PNG游戏</th>
								<th>MG游戏</th>
							</tr>
							<tr>
								<td rowspan="6">周六到周日</td>
								<td>50元</td>
								<td>10局</td>
								<td rowspan="7">
									<img src="img/baoshi.png" style="width: 174px;">
									<span style="width: 100%; text-align: center;display: inline-block;">五彩宝石</span>
								</td>
								<td rowspan="7"><img src="img/100.png" style="width: 174px;"><span style="width: 100%; text-align: center;display: inline-block;">富裕人生</span></td>								
							</tr>
							<tr>
								<td>100-499</td>
								<td>15局</td>
							</tr>
							<tr>
								<td>500-1999</td>
								<td>30局</td>
							</tr>	
							<tr>
								<td>2000-4999</td>
								<td>40局</td>
							</tr>
							<tr>
								<td>5000-9999</td>
								<td>50局</td>
							</tr>	
							<tr>
								<td>10000以上</td>
								<td>80局</td>
							</tr>							
						</table>
					</div>
					<div class="user_text_box">
						<div class="usertext">
							<i><img src="img/wanjia.png"></i>
							<span class="c_huangse">玩家质疑：</span>
							<span>奖励游戏局都能送？肯定后台有人操作!</span>
						</div>		
						<div class="admin_text">
							<i><img src="img/erge.png"></i>
							<span class="c_huangse">天威二哥：</span>
							<span>玩家投注真钱游戏的时候，系统无法设置任何一局奖励游戏，也无法改变任何投注中奖结果，这是PNG/MG游戏平台方给予的奖励活动，针对达到要求的玩家给予相对的奖励，会在游戏开始前派发奖励到指定游戏内，是无法干预或是操控任何一局真钱投注的，请放心呢！</span>
						</div>						
					</div>
					
					<div class="foot_text">
						<h3 class="c_huangse">活动规则</h3>
						<p>
							1、此活动无需申请，达到存款 要求即可获得相应平台额外派发指定游戏奖励游戏局，存款额度越高就可以获得更多的奖励 游戏局数。会员需要在免费游戏派发之前激活PNG/MG游戏平台，符合资格才可获得奖励。<br />
							2、奖励局派发时间：次日下午18点之前会以站内信通知获奖玩家，直接进入指定游戏即可开始 免费游戏。<br />
							3、免费奖励游有效期限，须在派发后的24小时内进入游戏完成投注，逾期作废不予补偿。<br />
							4、奖励游戏不限定设备进行游戏，手机网页版，手机APP，电脑网页版均可进行游戏。<br />
							5、该优惠可与返水共享，若发现同一玩家多帐号重复申请此优惠活动，天威将有权扣除该 红利和用此红利所赢得的金额。<br />
							6、天威保留修改、解释本次规则的权利；以及在无通知的情况下作出改变本次活动的权利。						
						</p>
					</div>
					
				</div>
			</div>
		</div>	
			<div class="yunceng">
				
			</div>		
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>	
	</body>
</html>