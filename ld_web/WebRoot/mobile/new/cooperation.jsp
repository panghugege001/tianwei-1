<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="代理合营" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/lib/swiper/swiper.css" />
	</head>

	<body>
		<title>天威</title>
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide">
					<img src="/mobile/img/banner/agent/agent1.jpg" style="width:100%" />
				</div>
				<div class="swiper-slide">
					<img src="/mobile/img/banner/agent/agent2.jpg" style="width:100%" />
				</div>
				<div class="swiper-slide">
					<img src="/mobile/img/banner/agent/agent3.jpg" style="width:100%" />
				</div>
			</div>
			<div class="swiper-pagination">
			</div>
		</div>
		<div class="cooperation-nav">
			<div class="item active">品牌介绍</div>
			<div class="item">佣金计划</div>
			<div class="item">圆梦计划</div>
			<div class="item">常见问题</div>
			<div class="item">联系我们</div>
			<a href="/mobile/new/login.jsp?agent" onclick="return agentLogin()" class="item">代理登录</a>
			<a href="/mobile/new/agentRegister.jsp" class="item">代理注册</a>
		</div>

		<div class="cooperation-content">
			<div class="introduce active">
				<div class="title">品牌介绍</div>

				天威创建2017年，隶属于总公司LUCKY STAR ENTERTAINMENT LIMITED，总公司注册创立于2005年，2009年获得Firstcagayan正规博彩牌照NO.020-A，公司历经10年之久，全球领先秒提技术迅速占领亚洲市场份额，凭其雄厚的实力及业界优质关系网，致力打造亚洲最佳在线娱乐平台。
				<div class="title">公司文化</div>

				天威城秉承以人为本，科技立身为宗旨，为业界研发打造10秒到账「秒提、秒存、秒返水」技术，致力为博彩界打造提款最迅速的公司。
				<div class="title">品牌优势</div>
				<p>
					1、提款：20万以下提款10秒到账，全球首发。<br> 2、服务：7x24小时在线服务，提供优质服务。
					<br> 3、 安全：采用128位ssl高标准加密技术。<br> 4、 佣金结算：采用全球最优质的结算方式日结佣金。<br> 5、产品丰富：PT、DT、MG、PNG、TTG、NT、QT、AG、沙巴体育、N2。
					<br> 6、强大的品牌优势：多年业界运营实践经验，咨询丰富。
					<br> 7、平台自动化：自助优惠，自助洗码，独立研发。
				</p>

			</div>
			<div class="plan">
				<div class="title"> 佣金结算</div>
				无需任何费用，零成本拓展你的财富之路 —— 圆你的boss梦。<br /> 您只需要推广网络推广或者是身边朋友介绍成为我们的玩家，天威会根据代理每月游戏的净利润收入按照佣金比例计算佣金。
				<table>
					<thead>
						<tr>
							<th>等级</th>
							<th>当月活跃会员数</th>
							<th>当月净利润(RMB)</th>
							<th>佣金比例</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td>≥3</td>
							<td>0-300.000</td>
							<td>30%</td>
						</tr>
						<tr>
							<td>2</td>
							<td>≥15</td>
							<td>300.001-1.000.000</td>
							<td>35%</td>
						</tr>
						<tr>
							<td>3</td>
							<td>≥40</td>
							<td>1.000.001-2.000.000</td>
							<td>40%</td>
						</tr>
						<tr>
							<td>4</td>
							<td>≥100</td>
							<td>≥2.000.001</td>
							<td>45%</td>
						</tr>
						<tr>
							<td>结算方式</td>
							<td colspan="3">佣金计算</td>
						</tr>
						<tr>
							<td style="background: #dfa85a; color: #FFFFFF;">日结佣金</td>
							<td colspan="3" style="background: #dfa85a; color: #FFFFFF;">
								老虎机佣金（PT/DT/MG/PNG/TTG/NT/QT/AG老虎机/AGIN捕鱼平台输赢*85％-返水-优惠）*佣金比例<br>其他平台佣金:(AG/SB/N2等吉他平台输赢X90%-返水-优惠）*佣金比例
							</td>
						</tr>
					</tbody>
				</table>
				<div class="title"> 佣金日结标准</div>
				<p>
					电子类:（PT/DT/MG/PNG/TTG/NT/QT/AG老虎机/AGIN捕鱼）所有老虎机平台包括捕鱼，均可享受日结，每日下午5点之后结算。<br> 提款要求：佣金≥100元可随时进行提款。
					<br> 注：每月1-5号是月结算时间，此时间段内日结算佣金不更新，于6号统一执行。
					<br> 其他类：（包含真人，体育）。
					<br> 老代理：历史佣金累计15万以上，且连续6个月有佣金记录，可升级为VIP代理，享有佣金日结。
					<br> 新代理：累计佣金30万以上，每月满足活跃会员要求，才能晋级VIP代理，享有日结佣金。
					<br> 提款要求：每月月初1-5号为其他平台佣金额度提款时间，请在这个时间段内申请提款，如果规定时间内未提款，佣金将累计至下月。
				</p>
				<div class="title"> 注意事项</div>
				<p>
					1、享受日结的代理如果连续一个月没有新增一位活跃会员，平台第二个月将其取消日结模式，如果在第二个月有开发会员了，会在次月开启日结。<br> 2、若合作伙伴自己的游戏账号注册到自己代理线下，取消代理资格且佣金不予发放。
					<br> 3、代理许谨慎填写代理信息，如有填写错误信息，导致无法出款，恕天威概不负责。
					<br> 4、若代理下线出现套取佣金或优惠的会员有权力拒绝服务该用户，并在不通知的情况下冻结，关闭相关游戏账号与代理账号，不退换任何的款项。
					<br> 5、绑定游戏账户必须与代理账号的姓名、银行卡相同，且不能是在自己代理下线注册的游戏账号
					<br> 6、加盟发展三月以上代理，如当月没有新增三个活跃会员，佣金不予出款。
				</p>
			</div>
			<div class="dream">
				<div class="title">热情招募合伙人，百万圆您BOSS梦</div>
				<p>
					“天高任鸟飞，海阔凭鱼跃”为了让您有更广阔的“天空”，为了让您有更好的“舞台”，针对每日辛苦的代理合作商。 公司特举办——2017年“热情招募合伙人，百万圆您BOSS梦”活动。
				</p>
				<a href="/mobile/new/agentRegister.jsp">
					<img class="dream-img" src="/mobile/img/dream.png" alt="" />
				</a>
				<div class="title">具体活动内容如下：</div>
				<p>
					单月创造净利润达到100万，或一年内累加净利润达到500万，即可申请 —— “圆梦”资助。<br> 公司为您量身打造“专属品牌”。
					<br> 公司将为您提供技术、客服、财务（现金流）等相应配套模式，助您无忧无虑当老板。
					<br> 加盟“天威”，跨越“财富之颠”
					<br> 只要您有梦想、野心、信心！天威将圆您boss梦。
					<br> 只要您有梦想、野心、信心！天威将圆您boss梦。
				</p>
			</div>
			<div class="question">
				<ul>
					<li class="item">
						<div class="item-title">1、怎样注册代理？</div>
						<div class="item-content">A:登陆代理页面即可看到“申请加盟”字样，点击即可来到注册页面，注册的时候，请仔细阅读我们的代理合作协议说明书。</div>
					</li>
					<li class="item">
						<div class="item-title">2、合营代理有任何费用吗？</div>
						<div class="item-content">A:这是0投资的赚钱方式，您是无需花费任何费用开始，仅仅只需点击“立即注册”，注册后您将拥有一个独一无二的合营官网链接，然后便能开始向您的会员宣传推广赚取佣金了。</div>
					</li>
					<li class="item">
						<div class="item-title">3、怎样赚取佣金？支付佣金是怎么样的？</div>
						<div class="item-content">A:只要您注册了代理账号之后，会生成一个您独立的连接，然后您可以推广自己的连接，让其他人使用您的连接注册并且游戏。然后他们产生负盈利的话，你就有佣金了。我们会按照日结累计，只要您满足佣金累计到100元以上即可随时申请提款。</div>
					</li>
					<li class="item">
						<div class="item-title">4.我怎么能知道我一个月挣了多少钱？</div>
						<div class="item-content">A:天威拥有强大的技术团队，所有合营人员都可以自己登陆自己的账号查看到自己下线的输赢情况（登陆账号——点击账户管理——佣金明细）。</div>
					</li>
					<li class="item">
						<div class="item-title">5.我如何知道注册的会员是不是在我的下线？</div>
						<div class="item-content">A:如果您推荐的会员有注册成功，那么您直接可以在自己的后台看到他的情况。（登陆账号——点击账户管理——下线会员)。</div>
					</li>
					<li class="item">
						<div class="item-title">6.如何能保证我的佣金可以结算给我？</div>
						<div class="item-content">A:天威所有的结算系统都是采用了强大技术创造的全自动结算模式。只要您下线会员有产生负盈利，那么系统会自动结算完毕，添加到合营账户中，您只需要申请提款即可。</div>
					</li>
					<li class="item">
						<div class="item-title">7.下线会员无法打开连接怎么处理？</div>
						<div class="item-content">A:天威是属于菲律宾正规博弈老虎机娱乐城，国内一些具有拦截性质的浏览器会产生一些影响，建议会员使用谷歌浏览器进入，您也可以联系我们的在线客服索要备用网址。</div>
					</li>
					<li class="item">
						<div class="item-title">8.下线会员的输赢情况我可以看到吗？</div>
						<div class="item-content">A:合营人员可以在自己的合营账号看到下线会员所有的存款、提款、输赢等信息！所有的信息都在账户管理可以看到的！</div>
					</li>
					<li class="item">
						<div class="item-title">9.合营连接是否可以修改？</div>
						<div class="item-content">A：如果您需要修改您的合营链接，那么您可以联系我们的代理专员，提供您的注册信息，进行修改您自己熟悉的链接！</div>
					</li>
					<li class="item">
						<div class="item-title">10.长时间没有登录忘记了密码，或者输入5次错误密码被冻结怎么办？</div>
						<div class="item-content">A：如果忘记密码或者账号被锁定，您可以直接点击“登入”后面的“忘记密码”使用邮箱后者电话进行重置，重置成功之后账号自动恢复正常的!</div>
					</li>
					<li class="item">
						<div class="item-title">11.日结佣金需要达到一个什么样的条件？</div>
						<div class="item-content">A：老虎机佣金，所有代理无需申请，自动日结不需要任何条件！<br /> B：真人佣金，请参照官网的佣金计划进行查看，达到第二条以上即可申请成为VIP代理，享受真人日结佣金。
						</div>
					</li>
					<li class="item">
						<div class="item-title">12.我可以自己注册到自己的下线游戏吗?</div>
						<div class="item-content">A：天威对这种事情是禁止的，具体的相关条例，请参照我们代理合作协议的责任与条款！</div>
					</li>
				</ul>
			</div>
			<div class="contact">
				<div class="title">联系我们</div>
				<div>
					QQ(代理)：3002864181 <br /> SWpe(代理)：mk@LONGDU800.com
					<br /> 邮箱(代理)：mk@LONGDU800.
					<br /> 天威市场部工作时间：周一至周六13:00——22:00。
				</div>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/lib/swiper/swiper.js"></script>
		<script type="text/javascript">
			new Swiper('.swiper-container', {
				pagination: '.swiper-pagination'
			});

			$('.introduction li').click(function() {
				$(this).toggleClass('active');
			})

			$('.cooperation-nav div.item').click(function() {
				$(this).addClass('active').siblings().removeClass("active");
				$('.cooperation-content>div').eq($(this).index()).addClass('active').siblings().removeClass("active");
			})

			$('.question .item').click(function() {
				$(this).toggleClass('active');
			})
		</script>
		<script>
			function agentLogin () {
				window.header.logout(true);
			}
		</script>
	</body>

</html>