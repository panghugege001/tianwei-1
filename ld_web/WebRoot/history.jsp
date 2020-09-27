<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<link rel="stylesheet" href="${ctx}/css/history.css?v=1210"/>
</head>
<body>
<div class="header-top">
	<div class="container">
		<a class="btn fl" target="_blank" href="${ctx}/">返回主页</a>
		<a class="btn fr" target="_blank" href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=">在线客服</a>
		<!-- <a class="btn fr" target="_blank" href="/asp/bbsIndex.aspx">官方论坛</a> -->
	</div>
</div>
<div class="page-bg">
	<div class="top-h"></div>
	<div class="container top-area text-center">
		<div class="mb20"><img src="/images/logo.png" alt=""></div>
		<h1 class="mb20">来天威欢乐斗虎</h1>
		<div class="text c-strong link-action">
			<a href="/slotGame.jsp?showtype=PT" target="_blank">老虎机</a>
			<a href="/gameAginBuyu.aspx" target="_blank">AG捕鱼</a>
			<a href="/live.jsp" target="_blank">百家乐</a>
			<a href="/gameSport.aspx" target="_blank">体育</a>
			<a href="/phone.jsp" target="_blank">手机投注</a>
		</div>
		<div class="action-group" id="j-action">
			<a class="btn" data-type="ALL" href=""><img class="icon" src="/images/history/tv.png" alt=""> 天威风采</a>
			<a class="btn" data-type="SUP" href=""><img class="icon" src="/images/history/money.png" alt=""> 天威赞助</a>
			<a class="btn" data-type="OTH" href=""><img class="icon" src="/images/history/heart.png" alt=""> 天威代言</a>
		</div>
	</div>
	<div class="time-line">
		<div class="container">
			<div class="aside-nav fl" id="myScrollspy">
				<ul class="nav nav-tabs nav-year"  id="j-nav">
					<li class="active"><a href="#year-2017">2017年</a></li>
					<li><a href="#year-2016">2016年</a></li>
					<li><a href="#year-2015">2015年</a></li>
					<li><a href="#year-2014">2014年</a></li>
					<li><a href="#year-2013">2013年</a></li>
				</ul>
			</div>
			<div class="main">
				<div class="time-list">
					<div class="line"></div>
					<div id="year-2017">
						<div class="item cfx" data-type="OTH">
							<div class="dot"></div>
							<div class="date">1月</div>

							<a href="/topic/represent/home.jsp" target="_blank"><div class="pic"><img src="/images/history/201612.png" width="228" height="119" alt=""></div>
								<div class="cotent"><h3 class="tit">天威诚邀名模“于芷晴”倾情代言</h3>
									<div class="desc">2017年，天威诚邀新一代宅男女神“于芷晴”倾情代言，新的一年，芷晴宝宝陪您一起玩转天威赢大奖！</div>
								</div></a>
						</div>
					</div>
					<div id="year-2016">
						<div class="item cfx" data-type="SUP">
							<div class="dot"></div>
							<div class="date">10月</div>

							<a href="/asp/queryConcertBet.aspx" target="_blank"><div class="pic"><img src="/images/history/201610.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威冠名赞助刘若英广州演唱会</h3>
								<div class="desc">2016年10月，天威冠名赞助 《刘若英Renext 我敢 世界巡回演唱会》广东佛山站，用“我敢”带给大家一场不一样的音乐盛宴。</div>
							</div></a>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">9月</div>

							<a href="/topic/live.jsp" target="_blank"><div class="pic"><img src="/images/history/201609.jpg" width="228" height="119" alt=""></div>
								<div class="cotent"><h3 class="tit">天威直播秀【尤里直播回顾】</h3>
									<div class="desc">9月24日，天威宝贝尤里来到天威。主播尤里是性感火辣的代名词，傲人的身材，妖艳的容貌更是让玩家欲罢不能。欲知详情可看
										精彩回顾哦！</div>
								</div></a>
						</div>
                        <div class="item cfx">
                            <div class="dot"></div>

                            <a href="/topic/live.jsp" target="_blank"><div class="pic"><img src="/images/history/201609-02.jpg" width="228" height="119" alt=""></div>
                                <div class="cotent"><h3 class="tit">天威直播秀【妮妮直播回顾】</h3>
                                    <div class="desc">紧随时代潮流，引领行业先锋。天威为各位玩家带来了完美的视觉大餐。9月18日 天威宝贝妮妮与大家见面。妮妮以青春可爱
                                        著称，一言一行透漏出的娇滴；可爱 让天威玩家留恋忘返；</div>
                                </div></a>
                        </div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">8月</div>

							<div class="pic"><img src="/images/history/201608.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威改版</h3>
								<div class="desc">天威改版隆重登场~引领新潮流展开与众不同的官网版面~享受快捷的1元存提，体验细致游戏乐趣，让我们一起来见证!</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="date">7月</div>
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201607.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">新增N2 live真人</h3>
								<div class="desc">N2 live真人上线啦~苹果以及安卓系统都可以进行游戏</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201607-2.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">新增AG电子老虎机上线</h3>
								<div class="desc">众所期盼的水果拉霸、视觉效果又不失风趣的老虎机纷纷上线</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201607-3.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">1元存提、新增快捷支付</h3>
								<div class="desc">天威全方位升级，用实力说话存、提款只需【1元】，一元存的方便，一元提款实力信誉的象征，新增快捷支付上线，让您多元
									化选择存款方式。
								</div>
							</div>
						</div>
						<div class="item cfx" data-type="SUP">
							<div class="dot"></div>
							<a href="/topic/match.jsp" target="_blank"><div class="pic"><img src="/images/history/201607-4.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威赞助中德体育</h3>
								<div class="desc">2016年7月，天威赞助最强足球赛事“中德国际足球邀请赛”为国足事业贡献了一份力量</div>
							</div></a>
						</div>

						<div class="item cfx">
							<div class="date">4月</div>
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201604.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">累积奖池疯狂爆发!</h3>
								<div class="desc">一夜致富!分别两位玩家在4/12当天在天威PT老虎机，喜中刀锋战士35万奖池以及紫色热11万奖池~真是喜上加喜啊</div>
							</div>
						</div>
					</div>
					<div id="year-2015">
						<div class="item cfx">
							<div class="date">9月</div>
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201409.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">支付宝秒提功能正式上线</h3>
								<div class="desc">跨时代的用心开发，避免资金流动的记录查询，安全、迅捷、方便。</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="date">7月</div>
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201507.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">TTG老虎机平台隆重上线</h3>
								<div class="desc">2D美式风格画风，业界最具特色的老虎机平台，游戏丰富精彩不断。</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201507-2.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">EBET 真人娱乐荣幸上线</h3>
								<div class="desc">简单易上手、趣味性强、稳定性高，传统性百家乐玩家的最爱。</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">6月</div>
							<div class="pic"><img src="/images/history/201606.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">最低1元即可存款</h3>
								<div class="desc">天威存款系统再升级最低1元即可存款搂~，任何存款方式都可以哦～还等什么，赶紧参予啰～再也不怕不够１０元无法游戏了</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201606-2.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">全新游戏捕鱼游戏</h3>
								<div class="desc">AGIN捕鱼游戏上线啦~，使用电脑立即开启捕鱼行动，手机下载AGIN客户端即可畅玩捕鱼啦！转账到AGIN即可立即捕鱼哦。</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201606-3.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">苹果也追随</h3>
								<div class="desc">不仅可以使用安卓系统游戏PT~ 连苹果也跟上脚步了呢~，即日起使用苹果登入网页即可畅游PT老虎机</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">3月</div>
							<div class="pic"><img src="/images/history/201503.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">支付宝秒存功能正式上线</h3>
								<div class="desc">保障资金流动记录的第一步，大量用户熟悉的转帐平台，轻松操作，瞬间到帐。</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="date">2月</div>
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201502.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威PT自助存送功能已开启</h3>
								<div class="desc">无需等客服帮您服务，申请瞬间派发！就是要这么高效率！</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201502-2.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威 『秒提』更上一层楼！</h3>
								<div class="desc">当天提款无上线，秒提最高提高至２０万！有实力才敢承诺，赶紧体验瞬间二十万入帐的快感吧！（仅限银行秒提）</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="date">1月</div>
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201501.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">一夜致富</h3>
								<div class="desc">恭喜天威玩家kw***在1月18日--23日一周时间在天威的EA厅用赢利150多万，又是一个短期致富的案例，该玩家的阶段性超高命中率
									以及敢打敢拼的性格真是让佩服，恭喜他了！
								</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="pic"><img src="/images/history/201501-2.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">全平台自助返水上线，开创行业先河</h3>
								<div class="desc">天威开创行业先河，全平台随时自助返水，给提供玩家们快速又便捷的服务，现在只要到帐户管理的资金管理处点击自助返水解决
									您所有困扰！！在天威一夜致富！恭喜天威玩家mt***在12月18日六合彩喜中特码中得192885元的高额奖金。
								</div>
							</div>
						</div>
					</div>
					<div id="year-2014">
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">11月</div>
							<div class="pic"><img src="/images/history/201411.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威单笔提款无上限啦</h3>
								<div class="desc">天威提款新改革，打破以前5分钟提款的单笔最高19万的限制，改为提款单笔提款无上限！ 给您更好的体验，更好的服务，您不用
									再担心账户资金分笔提款的麻烦！一步到位，才是我们服务的宗旨！！ 天威在这期待您来体验！！
								</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">10月</div>
							<div class="pic"><img src="/images/history/201410.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">老虎机奖池连连爆出，牛人100元博中16万</h3>
								<div class="desc">天威周年庆好运当头，老虎机奖池连连爆出，牛人100元博中16万，恭喜吉林会员 LYYG*** 存款100元在PT电动吃角子老虎机中喜
									中 16.2万元 累计大奖，老虎机以小搏大，天威运气爆棚，现在加入天威，圆您一夜暴富梦。
								</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">9月</div>
							<div class="pic"><img src="/images/history/201409.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威老虎机牛人中得奖池奖金27万</h3>
								<div class="desc">恭喜会员 XIAOXI*** 存款1000元在PT老虎机钻石谷游戏中喜中 22.3万元累计大奖，老虎机以小搏大，天威运气爆棚，现在加入
									天威，圆您一夜暴富梦。
								</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">8月</div>
							<div class="pic"><img src="/images/history/201408.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">天威对客户提款限额开放至单日500万</h3>
								<div class="desc">我们为了您对您的资金更放心，特将单日提款限额调整至500万！大额再也不用一笔一笔的提款了。另外对于玩的大的会员，只要存
									款单笔50万即可将台红调整到10万进行游戏，让您游戏更刺激。
								</div>
							</div>
						</div>
						<div class="item cfx">
							<div class="dot"></div>
							<div class="date">3月</div>
							<div class="pic"><img src="/images/history/201403.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">KENO平台已经上线</h3>
								<div class="desc">天威强势推出KG平台,不光只有真人和体育,现在起在天威也能享受到彩票游戏的魅力,KENO、SSC、PK10，满足众多彩票密的需求，
									并且配合KG2.0%的超高返水登场，每周的KENO比赛更是魅力无穷，来尝试一下彩票游戏的魅力吧!!
								</div>
							</div>
						</div>
					</div>
					<div id="year-2013">
						<div class="item cfx">
							<div class="dot"></div>
							<div class="year">2013年</div>
							<div class="date">10月</div>
							<div class="pic"><img src="/images/history/201310.jpg" width="228" height="119" alt=""></div>
							<div class="cotent"><h3 class="tit">秒存秒提秒返水</h3>
								<div class="desc">天威09年进入中国市场，已经陪伴大家走过了4年的风风雨雨，经过4年的发展，天威现在的技术已经在行业领先，首创的秒提更是
									给力，取款5秒到账，秒存秒提秒返水，秒杀新时代~俨然天威已经成为了新时代的风向标~让我们期待它辉煌的未来吧！
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<script>
	$(function () {

		var item=$('.time-list').find('.item');
        var $sp=$('#myScrollspy');

        openAffix(true);

		$('#j-action').find('a').on('click',function(){
			var type=$(this).data('type');
			switch (type){
				case 'ALL':
					item.show();
                    openAffix(true);
					break;
				case 'SUP':
					item.hide();
					item.filter('[data-type="SUP"]').show();
                    openAffix(false);
					break;
				case 'OTH':
					item.hide();
					item.filter('[data-type="OTH"]').show();
					openAffix(false);
					break;
			}
			return false;

		});

        function openAffix(check){
            if(check){
                $(document).ready(function(){
                    $("#j-nav").affix({offset: {top: 800}});
                });
                $sp.attr('id','myScrollspy');
                $('body').scrollspy({ target: '#myScrollspy' });
            }else{
                $sp.attr('id','myScrollspy11');

            }
        }
	});
</script>
</body>
</html>
