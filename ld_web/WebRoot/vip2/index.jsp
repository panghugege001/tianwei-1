<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0"> 
		<title>VIP中心</title>
		<link rel="stylesheet" href="css/bootstrap.min.css?v=2" />
		<link rel="stylesheet" href="css/index.css?v=1" />
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<script type="text/javascript" src="js/jquery.hovertreescroll.js" ></script>

    </head>
    <style>
#header .link-info{
    margin-left: 55px;	
}
.gb-nav > li{
	width: 128px;
}    	
    </style>
	<body class="vip_body">

		<div class="header">
			<a href="">返回首页</a>
		</div>
		<div class="vip_bannar">
			<div class="bannar_text">
				<img  src="img/text_banar.png" />
			</div>
			<div class="container">
				<div class="col-md-12 vip_listbox">
					<ul>
						<li>
							<a href="javascript:scroll('section-1');"><img src="img/mui_list1.png"></a>
						</li>
						<li>
							<a href="javascript:scroll('section-2');"><img src="img/mui_list2.png"></a>
						</li>
						<li>
							<a href="javascript:scroll('section-3');"><img src="img/mui_list3.png"></a>
						</li>						
					</ul>
				</div> 
				
				<div class="section" id="section-1">
					<div class="section_title1">
						<span class="title1_img"><img src="img/vip_title1.png" /></span>
						<div class="section_list col-md-12 col-md-offset-1">
							<div class="row">
								<ul>
									<li class="col-md-2">
										<span><img src="img/title1_list1.png"></span>
										<label>生日礼物</label>
									</li>
									<li class="col-md-2">
										<span><img src="img/title1_list2.png"></span>
										<label>免费筹码</label>
									</li>
									<li class="col-md-2" id="clear">
										<span><img src="img/title1_list3.png"></span>
										<label>晋级礼金</label>
									</li>
									<li class="col-md-2">
										<span><img src="img/title1_list4.png"></span>
										<label>高额反水</label>
									</li>
									<li class="col-md-2">
										<span><img src="img/title1_list5.png"></span>
										<label>高额存送</label>
									</li>									
								</ul>
							</div>
						</div>
						<div class="section_table1">
							<div class="vip_table1">
								<table cellpadding="0" cellspacing="0">
									<tr>
										<th>VIP等级</th>
										<th>生日礼金</th>
										<th>免费筹码</th>
										<th>晋级礼金</th>
										<th>高额反水</th>
										<th>高额存送</th>
									</tr>
									<tr>
										<td>忠实VIP</td>
										<td>88</td>
										<td>8</td>
										<td>88</td>
										<td>最高0.6%</td>
										<td>存送15%</td>
									</tr>
									<tr>
										<td>星级VIP</td>
										<td>188</td>
										<td>38</td>
										<td>188</td>
										<td>最高0.8%</td>
										<td>存送16%</td>
									</tr>
									<tr>
										<td>黄金VIP</td>
										<td>588</td>
										<td>88</td>
										<td>588</td>
										<td>最高0.8%</td>
										<td>存送17%</td>
									</tr>
									<tr>
										<td>白金VIP</td>
										<td>888</td>
										<td>188</td>
										<td>888</td>
										<td>最高0.8%</td>
										<td>存送18%</td>
									</tr>
									<tr>
										<td>钻石VIP</td>
										<td>1888</td>
										<td>588</td>
										<td>1888</td>
										<td>最高1.0%</td>
										<td>存送19%</td>
									</tr>
									<tr>
										<td>至尊VIP</td>
										<td>5888</td>
										<td>1288</td>
										<td>5888</td>
										<td>最高1.0%</td>
										<td>存送20%</td>
									</tr>									
								</table>								
							</div>
						</div>
					</div>
				</div>			
				<div class="section2" id="section-2">
					<div class="section_title1">
						<span class="title1_img"><img src="img/vip_title2.png" /></span>
					</div>
					<div class="col-md-12 vip_box">
						<ul>
							<li class="col-md-2 active">
								<i>
									<span>忠实</span>
									<span>VIP</span> 									
								</i>
							</li>
							<li class="col-md-2">
								<i>
									<span>星级</span>
									<span>VIP</span>									
								</i>								
							</li>
							<li class="col-md-2">
								<i>
									<span>黄金</span>
									<span>VIP</span>									
								</i>								
							</li>
							<li class="col-md-2">
								<i>
									<span>白金</span>
									<span>VIP</span>										
								</i>							
							</li>
							<li class="col-md-2">
								<i>
									<span>钻石</span>
									<span>VIP</span>										
								</i>							
							</li>
							<li class="col-md-2">
								<i>
									<span>至尊</span>
									<span>VIP</span>									
								</i>								
							</li>
						</ul>
					</div>
					<div class="vip_table2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>VIP等级</th>
								<th>赠送福利</th>
								<th>晋级要求</th>
							</tr>
							<tr>
								<td rowspan="6">
									<div class="vip_icon">
										<span class="user_vip">忠实</span>
										<span class="user_lv">VIP</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>免费彩金8元</td>
								<td rowspan="6">
									<p>
										A.所有平台总投注40万<br />
										B.所有老虎机平台总投注10万<br />
										C.当月累积存款5万										
									</p>
								</td>
							</tr>
							<tr>
								<td>晋级礼金88元</td>
							</tr>
							<tr>
								<td>生日礼金88元</td>
							</tr>	
							<tr>
								<td>返水上限28888元</td>
							</tr>
							<tr>
								<td>存送优惠券15％，最高2888元，12倍水</td>
							</tr>							
						</table>
					</div>						
					<div class="vip_table2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>VIP等级</th>
								<th>赠送福利</th>
								<th>晋级要求</th>
							</tr>
							<tr>
								<td rowspan="6">
									<div class="vip_icon">
										<span class="user_vip">星级</span>
										<span class="user_lv">VIP</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>免费彩金38元</td>
								<td rowspan="6">
									<p>
										A.所有平台总投注80万<br />
										B.所有老虎机平台总投注60万<br />
										C.当月累积存款10万										
									</p>
								</td>
							</tr>
							<tr>
								<td>晋级礼金188元</td>
							</tr>
							<tr>
								<td>生日礼金188元</td>
							</tr>	
							<tr>
								<td>返水上限28888元</td>
							</tr>
							<tr>
								<td>存送优惠券16％，最高2888元，12倍水</td>
							</tr>							
						</table>
					</div>					
					<div class="vip_table2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>VIP等级</th>
								<th>赠送福利</th>
								<th>晋级要求</th>
							</tr>
							<tr>
								<td rowspan="6">
									<div class="vip_icon">
										<span class="user_vip">黄金</span>
										<span class="user_lv">VIP</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>免费彩金88元</td>
								<td rowspan="6">
									<p>
										A.所有平台总投注150万<br />
										B.所有老虎机平台总投注120万<br />
										C.当月累积存款20万										
									</p>
								</td>
							</tr>
							<tr>
								<td>晋级礼金588元</td>
							</tr>
							<tr>
								<td>生日礼金588元</td>
							</tr>	
							<tr>
								<td>返水上限28888元</td>
							</tr>
							<tr>
								<td>存送优惠券17％，最高2888元，12倍水</td>
							</tr>							
						</table>
					</div>
					<div class="vip_table2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>VIP等级</th>
								<th>赠送福利</th>
								<th>晋级要求</th>
							</tr>
							<tr>
								<td rowspan="6">
									<div class="vip_icon">
										<span class="user_vip">白金</span>
										<span class="user_lv">VIP</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>免费彩金188元</td>
								<td rowspan="6">
									<p>
										A.所有平台总投注400万<br />
										B.所有老虎机平台总投注200万<br />
										C.当月累积存30万										
									</p>
								</td>
							</tr>
							<tr>
								<td>晋级礼金888元</td>
							</tr>
							<tr>
								<td>生日礼金888元</td>
							</tr>	
							<tr>
								<td>返水上限88888元</td>
							</tr>
							<tr>
								<td>存送优惠券18％，最高2888元，12倍水</td>
							</tr>							
						</table>
					</div>	
					<div class="vip_table2">
							<table cellpadding="0" cellspacing="0">
								<tr>
									<th>VIP等级</th>
									<th>赠送福利</th>
									<th>晋级要求</th>
								</tr>
								<tr>
									<td rowspan="6">
										<div class="vip_icon">
											<span class="user_vip">钻石</span>
											<span class="user_lv">VIP</span>
										</div>
									</td>
								</tr>
								<tr>
									<td>免费彩金588元</td>
									<td rowspan="6">
										<p>
											A.所有平台总投注800万<br />
											B.所有老虎机平台总投注500万<br />
											C.当月累积存款50万										
										</p>
									</td>
								</tr>
								<tr>
									<td>晋级礼金1888元</td>
								</tr>
								<tr>
									<td>生日礼金1888元</td>
								</tr>	
								<tr>
									<td>返水上限88888元</td>
								</tr>
								<tr>
									<td>存送优惠券19％，最高2888元，12倍水</td>
								</tr>							
							</table>
						</div>
				<div class="vip_table2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th>VIP等级</th>
								<th>赠送福利</th>
								<th>晋级要求</th>
							</tr>
							<tr>
								<td rowspan="6">
									<div class="vip_icon" id="zhizun">
										<span class="user_vip">至尊</span>
										<span class="user_lv">VIP</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>免费彩金1288元</td>
								<td rowspan="6">
									<p>
										连续三个月≥1000万									
									</p>
								</td>
							</tr>
							<tr>
								<td>晋级礼金5888元</td>
							</tr>
							<tr>
								<td>生日礼金5888元</td>
							</tr>	
							<tr>
								<td>返水上限88888元</td>
							</tr>
							<tr>
								<td>存送优惠券20％，最高2888元，12倍水</td>
							</tr>							
						</table>
					</div>							
				</div>
				<div class="section3" id="section-3">
					<div class="section_title2">
						<span class="title1_img">
							<img src="img/vip_title3.png" />
						</span>
					</div>
					<div class="huodong">
						<ul>
							<li><span>晋级条件：</span>晋级条件中的A、B、C 满足任一条件即可晋级; </li>
							<li><span>保级要求：</span>所有平台月总流水的20%或老虎机平台月总流水的30%满足任一条件即可保级；</li>
							<li><span>举例说明：</span>星级保级要求总流水 80万 * 20% =16万 或 老虎机平台60万 * 30% = 18万。</li>
						</ul>
					</div>
					<div class="guize">
						<p>
							1、每个月5号系统自动审核进行升级,系统以晋级条件中这的A、B、C来进行升级。<br />
							2、会员有达到晋级条件中的A，B或C，可以进入，“账户管理”—“自助晋级”自行操作。<br />
							3、所有会员申请优惠的会员等级按照每个月5号的等级为准。
							      （例如：该玩家申请优惠时1号的等级为黄金赌神，15号的等级为星级赌神，则按照5号晋级之后等级进行计算）<br />
							4、未达到保级要求将自动降一级，将不再另行通知。<br />
							5、会员每个月只享有一次晋级的机会，且各等级晋级礼金每位玩家只可以获得一次。<br />
							6、生日礼金(生日礼金需在您生日当天向在线客服或QQ客服进行申请）、晋级礼金及每月免费筹码，无需流水，可直接提款。<br />
							7、PT/TTG/老虎机钱包（DT、MG、QT、NT、PNG、SW）次存优惠会员进入“账户管理”—“自助存送”自行操作，一个玩家一天可以在笔存笔送优惠申请78次。							
						</p>
					</div>
				</div>
			</div>
		
		</div>
	</body>
<script>
	$(".vip_table2").eq(0).show();
	$(".vip_box ul li").click(function(){
		$(".vip_box ul li").removeClass("active");
		$(this).addClass("active");
		$(".vip_table2").hide();
		$(".vip_table2").eq($(this).index()).show();
	})
</script>
<script>  
	function scroll(id) {  
	$("#" + id).HoverTreeScroll(1000);  
	}  
</script> 
</html>