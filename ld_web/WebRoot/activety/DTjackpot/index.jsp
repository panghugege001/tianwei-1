<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<!DOCTYPE html>
<html>
    <head>
    	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>百万奖池</title>
		<script type="text/javascript" src="js/jquery-1.11.1.min.js" ></script>
		<script type="text/javascript" src="js/bootstrap.min.js" ></script>
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<link rel="stylesheet" href="css/dt_jackpot.css?v=6" />
			<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    </head>
	<body class="center_body">
		<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>		
		<div class="div_body">
			<div class="jackpot_text">
				<div class="container">
					<div class="box_text">
						<img src="img/jackpot_text.jpg" />
					</div>
					<div class="jackpot_btn col-md-4 col-md-push-4" style="border: none;">
						<ul style="display: none;">
							<li class="action">活动1</li>
							<li>活动2</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="table_box">
				<div class="container container_n1" style="display: none;">
					<div class="jackpot_center">
						<p>
							<h3>黄金周DT流水战</h3>
							2017/12/1 至 2017/12/31<br />
							DT老虎机达当日流水，即可获得免费彩金，流水越高彩金越多，天威力挺会员拼百万奖池
						</p>						
					</div>					
					<div class="w_table hd_1 col-md-8 col-md-offset-2">
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th>日流水</th>
								<th>彩金</th>
								<th>流水倍数</th>
							</tr>
							<tr>
								<td>1000-2999</td>
								<td>18</td>
								<td>5</td>
							</tr>
							<tr>
								<td>3000以上</td>
								<td>38</td>
								<td>5</td>
							</tr>
							<tr>
								<td>5000以上</td>
								<td>58</td>
								<td>5</td>
							</tr>
							<tr>
								<td>10000以上</td>
								<td>68</td>
								<td>5</td>
							</tr>
							<tr>
								<td>50000以上</td>
								<td>388</td>
								<td>5</td>
							</tr>
							<tr>
								<td>100000以上</td>
								<td>888</td>
								<td>5</td>
							</tr>						
						</table>
					<div class="massage" style="float: left;">
							<h3 style="font-size: 16px;">活动规则：</h3>
							<p style="font-size: 16px;">
								1、当日存款100元，即可参与黄金周DT流水战<br />
								2、日流水计算当日00:00:00 - 23:59:59。<br />
								3、达到对应流水即可获得免费彩金，无需向在线客服申请，彩金将在次日18点前派发，5倍流水即可提款。<br />
								4、免费彩金仅支持PT、DT、MG、TTG、QT、NT、PNG，以红包优惠券形式派发。<br />
								5、红包优惠券领取方式：站内信领取代码后→手机登入官网→自助优惠→红包优惠券。<br />
								6、此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名、同一邮件地址、同一相似IP地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑等其他任何不正常投注行为，一经发现，天威将保留冻结您的账户盈利及余额的权利。<br />
								7、关于老虎机累计奖池平台费收取,若您中累计大奖在人民币5万以上，平台方将会收取您20%的平台费用于平台建设。<br />
								8、此活动天威具有最终解释权。<br />								
							</p>
						</div>							
					</div>						
				</div>
				<div class="container container_n1">
					<div class="jackpot_center">
						<p>
							<h3>疯狂DT拼奖池</h3>
							活动已结束<br />
							天威力挺会员拼百万奖池，天天存送助您一臂之力，力拼百万奖池
						</p>						
					</div>					
					<div class="w_table hd_2 col-md-8 col-md-offset-2">
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th colspan="8">拼奖池存送优惠</th>
							</tr>
							<tr>
								<td></td>
								<td>周一</td>
								<td>周二</td>
								<td>周三</td>
								<td>周四</td>
								<td>周五</td>
								<td>周六</td>
								<td>周天</td>								
							</tr>
							<tr>
								<td>新会员</td>
								<td>存30送18</td>
								<td>存200送100</td>
								<td>存300送180</td>
								<td>存500送230</td>
								<td>存30送18</td>
								<td>存200送100</td>
								<td>存300送180</td>
							</tr>
							<tr>
								<td rowspan="2">忠实及以上会员</td>
								<td>存30送21</td>
								<td>存200送128</td>
								<td>存300送228</td>
								<td>存500送300</td>
								<td>存30送21</td>
								<td>存200送128</td>
								<td>存300送228</td>								
							</tr>	
						</table>
						<!--<div class="massage">
							<h3>活动规则：</h3>
							<p>
								1、当日存款100元，即可参与黄金周DT流水战<br />
								2、日流水计算当日00:00:00 - 23:59:59。<br />
								3、达到对应流水即可获得免费彩金，无需向在线客服申请，彩金将在次日18点前派发，5倍流水即可提款。<br />
								4、免费彩金仅支持PT、DT、MG、TTG、QT、NT、PNG，以红包优惠券形式派发。<br />
								5、红包优惠券领取方式：站内信领取代码后→手机登入官网→自助优惠→红包优惠券。<br />
								6、此项优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名、同一邮件地址、同一相似IP地址、同一住址、同一借记卡/信用卡、同一银行账户、同一电脑等其他任何不正常投注行为，一经发现，天威将保留冻结您的账户盈利及余额的权利。<br />
								7、关于老虎机累计奖池平台费收取,若您中累计大奖在人民币5万以上，平台方将会收取您20%的平台费用于平台建设。<br />
								8、此活动天威具有最终解释权。<br />								
							</p>
						</div>-->						
					</div>
					<div class="w_table hd_2 col-md-8 col-md-offset-2">
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th colspan="6">存送对照表</th>
							</tr>
							<tr>
								<td rowspan="5">新会员</td>
								<td>存送优惠</td>
								<td>最高彩金</td>
								<td>流水倍数</td>
								<td>最低存款</td>
								<td>申请次数</td>
							</tr>
							<tr>
								<td>存30送18</td>
								<td>18</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td>存200送100</td>
								<td>100</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td>存300送180</td>
								<td>180</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td>存500送230</td>
								<td>230</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td rowspan="5">忠实及以<br />上会员</td>
								<td>存送优惠</td>
								<td>最高彩金</td>
								<td>流水倍数</td>
								<td>最低存款</td>
								<td>申请次数</td>
							</tr>
							<tr>
								<td>存30送21</td>
								<td>21</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td>存200送128</td>
								<td>128</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td>存300送228</td>
								<td>228</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>
							<tr>
								<td>存500送300</td>
								<td>300</td>
								<td>20倍</td>
								<td>10</td>
								<td>1</td>
							</tr>								
						</table>
						<div class="massage" style="float: left;">
							<h3 style="font-size: 16px;">活动规则：</h3>
							<p style="font-size: 16px;">
								1、达到指定存款金额即可至自助优惠进行申请。<br />
								2.、提款要求:达到对应存送的提款流水倍数即可进行提款，例如：存款30送18:(存款30+18)的20倍流水。<br />
								3.、此优惠需全程在天威DT老虎机平台，如发现违规投注天威有权收回红利和非法盈利额。<br />
								4.、本次优惠和返水共享，凡是只靠此优惠套取优惠者，我们有权发至游戏平台方审核，严重者扣除盈利及本金并且禁用会员账号。<br />
								5、关于老虎机累计奖池平台费收取,若您中累计大奖在人民币5万以上，平台方将会收取您20%的平台费用于平台建设。<br />
								6、天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。<br />
								<span style="color: red;">温馨提示：3D老虎机、四圣兽、福禄寿、英雄荣耀、龙凤呈祥、白蛇传、赛亚烈战、封神榜、西游降妖、财神到、新年到、武松传、圣域传说、赤壁之战、哆啦A梦、降妖传奇、阿拉丁、劲爆篮球、疯狂转转转、夜店之王、摇滚之夜、梦幻森林、五行世界、水浒传、传奇之路---以上为总奖池开启的游戏款项。</span>
							</p>
						</div>							
					</div>					
				</div>	
			</div>
		</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>	
<!--<script type="text/javascript">
	$(function(){
		$(".jackpot_btn li").click(function(){
			$(".jackpot_btn").find("li").removeClass("action");
			$(this).addClass("action");
			$(".container_n1").hide();
			$(".container_n1").eq($(this).index()).show();
		})
	})
</script>-->

<script>
	$(function(){
		var user=$("#j-username").val();
		console.log(user)
		if(user){
			$(".r_rigth").hide();
		}else{
			$(".r_rigth").show();
		}
	})
</script>

	</body>
	
</html>