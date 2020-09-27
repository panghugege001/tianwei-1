<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>
<body>
<div class="vip-bg">
	<jsp:include page="${ctx}/tpl/vheader.jsp"></jsp:include>
	
	<div class="container">
		<div class="top-h fr"><img style="margin-left: -60px;" src="/images/vip/vip-tit.png?v=1" class="a-fadeinB">
			<c:choose>
					<c:when test="${session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER'}">
					 <a href="/asp/payPage.aspx?showid=tab_deposit" class="more a-ring" ></a>
					</c:when>
					<c:otherwise>
					<a href="javascript:alert('您好，请先登录！');" class="more a-ring" ></a>
					</c:otherwise>
				</c:choose> 
		</div>
		<div class="cl"></div>
		<div class="vip-box mb40">
			 <ul class="ul-level">
				<li class="active"><a data-toggle="tab" href="#tab-vip" class="vip"></a><p>全部</p></li>
				<li><a data-toggle="tab" href="#tab-zhong" class="zhong"></a><p>忠实vip</p></li>
				<li><a data-toggle="tab" href="#tab-xing" class="xing"></a><p>星级vip</p></li>
				<li><a data-toggle="tab" href="#tab-huang" class="huang"></a><p>黄金vip</p></li>
				<li><a data-toggle="tab" href="#tab-bai" class="bai"></a><p>白金vip</p></li>
				<li><a data-toggle="tab" href="#tab-zuan" class="zuan"></a><p>钻石vip</p></li>
				<li><a data-toggle="tab" href="#tab-zhi" class="zhi"></a><p>至尊vip</p></li>
			 </ul>
			<div class="box-tab"> 
				<div id="tab-vip" class="tab-panel active">
					<p class="p1"><span class="cee6">晋级要求：</span>A、B、C 满足任一条件即可晋级 <span class="cee6">保级要求：</span><span class="c-red">所有平台月总流水的20%</span>或<span class="c-red">老虎机平台月总流水的30%</span></span>满足任一条件即可保级。</p>
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						<tr>
							<td>忠实vip</td>
							<td>免费彩金8元<br />
							晋级礼金88元<br />
							生日礼金88元<br />
							返水上限28888元<br />
							存送优惠券15％，最高2888元，12倍水
							</td>
							<td>A.所有平台总投注40万<br />
								B.所有老虎机平台总投注10万<br />
								C.当月累积存款5万
							</td>
						</tr>
						<tr>
							<td>星级vip</td>
							 
							<td> 免费彩金38元<br />
							晋级礼金188元<br />
							生日礼金188元<br />
							返水上限28888元<br />
							存送优惠券16％，最高2888元，12倍水
							 </td>
							 <td>A.所有平台总投注80万<br />
							B.所有老虎机平台总投注60万<br />
							C.当月累积存款10万<br />
							</td>
						</tr>
						<tr>
							<td>黄金vip</td>
							<td>免费彩金88元<br />
							晋级礼金588元<br />
							生日礼金588元<br />
							返水上限28888元<br />
							存送优惠券17％，最高2888元，12倍水
							</td>
							<td>A.所有平台总投注150万<br />
							B.所有老虎机平台总投注120万<br />
							C.当月累积存款20万
							</td>
						</tr>
						<tr>
							<td>白金vip</td>
							<td>免费彩金188元<br />
							晋级礼金888元<br />
							生日礼金888元<br />
							返水上限88888元<br />
							存送优惠券18％，最高2888元，12倍水
							 </td>
							 <td>A.所有平台总投注400万<br />
							B.所有老虎机平台总投注200万<br />  
							C.当月累积存款30万
							</td>
						</tr>
						<tr>
							<td>钻石vip</td>
							<td> 免费彩金588元<br />
								晋级礼金1888元<br />
								生日礼金1888元<br />
								返水上限88888元<br />
								存送优惠券19％，最高2888元，12倍水

							</td>
							<td>
								A.所有平台总投注800万<br />
								B.所有老虎机平台总投注500万<br />
								C.当月累积存款50万<br /> 
							</td>
						</tr>
						<tr>
							<td>至尊vip</td>
							<td> 免费彩金1288元<br />
							晋级礼金5888元<br />
							生日礼金5888元<br />
							返水上限88888元<br />
							存送优惠券20％，最高2888元，12倍水
							</td>
							<td>
								连续三个月≥1000万
							</td>
						</tr>
					</tbody></table>
					<p class="mt20"><strong>备注：</strong></p>
					<p class="mt20">晋级条件中的A、B、C 满足任一条件即可晋级; <span class="cee6">保级要求：</span><span class="c-red">所有平台月总流水的20%</span>或<span class="c-red">老虎机平台月总流水的30%</span>满足任一条件即可保级.</p>
					<p>例: 星级保级要求总流水 80万 * 20% =16万 或 老虎机平台60万 * 30% = 18万</p>

					<p class="mt20">1、每个月5号系统自动审核进行升级；系统以晋级条件中这的A、B、C来进行升级。</p>
					<p>2、会员有达到晋级条件中的A，B或C，可以进入，“账户管理”—“自助晋级”自行操作。</p>
					<p>3、所有会员申请优惠的会员等级按照每个月5号的等级为准。</p>
					<p>（例如：该玩家申请优惠时1号的等级为黄金赌神，15号的等级为星级赌神，则按照5号晋级之后等级进行计算）</p>
					<p>4、未达到保级要求将自动降一级，将不再另行通知。</p>
					<p>5、会员每个月只享有一次晋级的机会，且各等级晋级礼金每位玩家只可以获得一次。</p>
					<p>6、生日礼金(生日礼金需在您生日当天向在线客服或QQ客服进行申请）、晋级礼金及每月免费筹码，无需流水，可直接提款。</p>
					<p>7、PT/TTG/老虎机钱包（DT、MG、QT、NT、PNG、SW）次存优惠会员进入“账户管理”—“自助存送”自行操作，一个玩家一天可以在笔存笔送优惠申请78次。</p>
				</div>
				<div id="tab-zhong" class="tab-panel">
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						<tr>
							<td>忠实vip</td>
							<td>免费彩金8元<br />
							晋级礼金88元<br />
							生日礼金88元<br />
							返水上限28888元<br />
							存送优惠券15％，最高2888元，12倍水
							</td>
							<td>A.所有平台总投注40万<br />
								B.所有老虎机平台总投注10万<br />
								C.当月累积存款5万
							</td>
						</tr>
						 
					</tbody></table>
				</div>
				<div id="tab-xing" class="tab-panel">
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						 
						<tr>
							<td>星级vip</td>
							 
							<td> 免费彩金38元<br />
							晋级礼金188元<br />
							生日礼金188元<br />
							返水上限28888元<br />
							存送优惠券16％，最高2888元，12倍水
							 </td>
							 <td>A所有平台总投注80万<br />
							B.所有老虎机平台总投注60万<br />
							C.当月累积存款10万<br />
							</td>
						</tr>
						 
					</tbody></table>
				</div>
				<div id="tab-huang" class="tab-panel">
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						 
						<tr>
							<td>黄金vip</td>
							<td>免费彩金88元<br />
							晋级礼金588元<br />
							生日礼金588元<br />
							返水上限28888元<br />
							存送优惠券17％，最高2888元，12倍水
							</td>
							<td>A.所有平台总投注150万<br />
							B.所有老虎机平台总投注120万<br />
							C.当月累积存款20万
							</td>
						</tr>
					 
					</tbody></table>
				</div>
				<div id="tab-bai" class="tab-panel">
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						 
						<tr>
							<td>白金vip</td>
							<td> 费彩金188元<br />
							晋级礼金888元<br />
							生日礼金888元<br />
							返水上限88888元<br />
							存送优惠券18％，最高2888元，12倍水
							 </td>
							 <td>A.所有平台总投注400万<br />
							B.所有老虎机平台总投注200万<br />  
							C.当月累积存款30万
							</td>
						</tr>
						 
					</tbody></table>
				</div>
				<div id="tab-zuan" class="tab-panel">
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						 
						<tr>
							<td>钻石vip</td>
							<td> 免费彩金588元<br />
								晋级礼金1888元<br />
								生日礼金1888元<br />
								返水上限88888元<br />
								存送优惠券19％，最高2888元，12倍水

							</td>
							<td>
								A.所有平台总投注800万<br />
								B.所有老虎机平台总投注500万<br />
								C.当月累积存款50万<br /> 
							</td>
						</tr>
						 
					</tbody></table>
				</div>
				<div id="tab-zhi" class="tab-panel">
					<table class="table mb20">
						<tbody>
						<tr>
							<th>会员等级</th>
							<th>免费彩金</th>
							<th>晋级要求</th>
						</tr>
						 
						<tr>
							<td>至尊vip</td>
							<td> 免费彩金1288元<br />
							晋级礼金5888元<br />
							生日礼金5888元<br />
							返水上限88888元<br />
							存送优惠券20％，最高2888元，12倍水
							</td>
							<td>
								连续三个月≥1000万
							</td>
						</tr>
					</tbody></table>
				</div>
			</div>
		</div>

		 
	</div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

</body>
</html>
