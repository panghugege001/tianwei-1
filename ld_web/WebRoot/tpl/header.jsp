<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/headerCommon.jsp"></jsp:include>
<style>
li:hover .AG_box {
	display: block;
}

li:hover .tiyubox {
	display: block;
}

li:hover .AGbuyu {
	display: block;
}

.new_tiyu {
	display: inline-block;
}

input:-webkit-autofill, textarea:-webkit-autofill, select:-webkit-autofill
	{
	background: #171b35!#171b35;
}

.drop_subnav:before {
	
}

.drop_subnav_i {
	background: url(../images/hover_top.png) no-repeat;
	position: absolute;
	top: -17px;
	display: inline-block;
	width: 156px;
	height: 50px;
}

.AGbuyu_i {
	background: url(../images/hover_top.png) no-repeat;
	position: absolute;
	top: -17px;
	display: inline-block;
	width: 156px;
	height: 50px;
}

.AG_box_i {
	background: url(../images/hover_top.png) no-repeat;
	position: absolute;
	top: -17px;
	display: inline-block;
	width: 156px;
	height: 50px;
}

.tiyubox_i {
	background: url(../images/hover_top.png) no-repeat;
	position: absolute;
	top: -17px;
	display: inline-block;
	width: 156px;
	height: 50px;
}

.AGbuyu {
	display: none;
	padding: 5px 0;
	position: absolute;
	z-index: 999;
	width: 156px;
	background-color: #3c1f00;
	left: -58px;
	border: 2px solid #ee6325;
	border-top: none;
	margin-top: 15px;
}

.AG_box {
	display: none;
	padding: 5px 0;
	position: absolute;
	z-index: 999;
	width: 156px;
	background-color: #3c1f00;
	left: -40px;
	border: 2px solid #ee6325;
	border-top: none;
	margin-top: 15px;
}

.tiyubox {
	display: none;
	padding: 5px 0;
	position: absolute;
	z-index: 999;
	width: 156px;
	background-color: #3c1f00;
	left: -18px;
	border: 2px solid #ee6325;
	border-top: none;
	margin-top: 15px;
}

.AGbuyu ul li {
	text-align: center;
	position: relative;
	padding: 5px 0px;
	font-size: 16px;
}

.AGbuyu ul li a {
	width: 100%;
	height: 100%;
}

.AGbuyu ul li {
	color: #ee6325;
}

.AGbuyu ul li a:hover {
	color: #e5ab90;
}

.AGbuyu ul li a:after {
	content: "";
	height: 0px;
}

.tiyubox ul li {
	text-align: center;
	position: relative;
	padding: 5px 0px;
	font-size: 16px;
}

.tiyubox ul li a {
	width: 100%;
	height: 100%;
}

.tiyubox ul li {
	color: #ee6325;
}

.tiyubox ul li a:hover {
	color: #e5ab90;
}

.tiyubox ul li a:after {
	content: "";
	height: 0px;
}

.AG_box ul li {
	text-align: center;
	position: relative;
	padding: 5px 0px;
	font-size: 16px;
}

.AG_box ul li a {
	width: 100%;
	height: 100%;
}

.AG_box ul li {
	color: #ee6325;
}

.AG_box ul li a:hover {
	color: #e5ab90;
}

.AG_box ul li a:after {
	content: "";
	height: 0px;
}

#header .header-top-wp .item.tousu-email {
	text-align: center;
	position: absolute;
	left: 350px;
	top: 10px;
	line-height: 18px;
}

.sub-nav ul li a:after {
	list-style-type: none;
}
</style>

<div id="header" class="gb-header">
	<!--顶部{-->
	<div class="header-top-wp">
		<div class="container w_1038">
			<div class="new_logo"
				style="position: absolute; left: -75px; top: 45px;">
				<a href="/index.jsp"><img src="../images/new_logo.png"></a>
			</div>
			<div class="link-info fl" style="width: 100%">
				<a data-toggle="modal" data-target="#modal-license"
					href="javascript:void(0);" class="item">牌照展示</a> <span class="null">|</span>
				<a href="/sitemap.jsp" target="_blank" class="item">备用网址</a> <span
					class="null">|</span>
				<!--				<a href="javascript:alert('敬请期待')" target="_blank" class="item">天威公会</a>-->
				<!-- <a href="javascript:;" class="item" onclick="alert('[提示]开始进行缓存清除，完毕后将会刷新网站！');window.location.reload(true);">清除缓存</a> -->
				<!-- <a # class="item">社区互动</a>
				<span class="null">|</span> -->
				<a href="/safeCenter.jsp" class="item">自助中心</a>

				<c:if test="${session.customer==null}">
					<ul class="fr link-info">
						<li class="f_flat"><span id="header_user"></span> <span><input
								type="text" id="j-name2" placeholder="账号"></span></li>
						<li class="f_flat"><span id="header_password"></span> <span><input
								type="password" id="j-pwd2" placeholder="密码"
								style="text-indent: 40px;"></span></li>
						<input type="hidden" name="code" id="code" maxlength="4"
							autocomplete="off" placeholder="验证码" />
						<li class="item"><span class="span_btn">
								<button class="i-btn one" id="j-login"
									onclick="Page.login(this,true);">登录</button>
						</span></li>
						<li class="item"><span class="span_btn"> <a
								class="i-btn one newuser" href="/register2.jsp">注册</a>
						</span></li>
						<li class="item"><span class="span_btn"> <a
								class="i-btn" href="/safeCenter.jsp"> <img
									src="../images/user/yaoshi.png" /> 忘记密码
							</a>
						</span></li>
					</ul>
				</c:if>
				<c:if
					test="${session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER'}">
					<ul class="fr link-info">
						<li class="item">欢迎回来:<span class="c-strong">${customer.loginname }</span></li>
						<li class="item">等级:<span class="c-strong"><s:property
									value="@dfh.model.enums.VipLevel@getText(#session.customer.level)" /></span></li>
						<li class="item"><a class="" href="javascript:;">余额:<span
								class="j-balance" class="c-strong">${customer.credit}</span></a></li>
						<!--<li class="item">
							<a href="${ctx}/manageLetter.jsp">站内信:<span class="j-letter c-strong">0</span></a>
						</li>-->
						<li class="item"><a class="i-btn two" class="check_name"
							href="/asp/payPage.aspx?tab_deposit">存款</a></li>
						<li class="item"><a class="i-btn once"
							href="/asp/payPage.aspx?tab_withdraw">提款</a></li>
						<!--<li class="item">
							<a class="i-btn" href="/asp/payPage.aspx?showid=tab_transfer">转账</a>
						</li>-->
						<%-- <li class="item"><a class="i-btn three" href="javascript:;" onclick="dosign();">签到彩金(<span id="j-checkin-money"></span>)</a></li>--%>
						<li class="item"><a class="three" href="/userManage.jsp">账户管理</a>
						</li>
						<li class="item"><a class="four" href="javascript:void(0);"
							onclick="return logout();">安全退出</a></li>
					</ul>
				</c:if>
				<c:if
					test="${session.customer!=null && session.customer.role ne 'MONEY_CUSTOMER'}">
					<input type="hidden" value="${session.slotAccount}"
						class="laohuji2" />
					<input type="hidden" value="${session.slotAccount}" class="qita2" />
					<ul class="fr link-info">
						<li class="item">欢迎回来:<span class="c-strong">${customer.loginname }</span></li>
						<li class="item">等级: <c:if
								test="${session.AGENTVIP ==null || session.AGENTVIP eq '0'}">
								<span class="c-strong">代理</span>
							</c:if> <c:if
								test="${session.AGENTVIP!=null && session.AGENTVIP eq '1'}">
								<span class="c-strong">VIP代理</span>
							</c:if>
						</li>
						<li class="item"><a class="balance-info" href="javascript:;">帐户余额:<span
								class="c-strong">${session.slotAccount}</span><i
								class="iconfont icon-refresh"></i>
						</a></li>
						<!--<li class="item" >
							<a class="balance-info" id="qita" href="javascript:;">其它账户结余:<span class="c-strong">${customer.credit}</span><i class="iconfont icon-refresh"></i> </a>
						</li>						-->
						<li class="item"><span class="span_btn"> <a
								class="i-btn one newuser" href="/agentManageNew.jsp?tab-tk">提款</a>
						</span></li>
						<li class="item"><a class="three"
							href="/agentManageNew.jsp?tab-personal">账户管理</a></li>
						<li class="item"><a class="four" href="javascript:void(0);"
							onclick="return logout();">安全退出</a></li>
					</ul>
				</c:if>

			</div>
		</div>
		<!--}顶部-->
		<!--菜单{-->
		<div class="nav-wp">
			<div class="container">
				<ul id="j-headerNav" class="gb-nav">
					<!-- <li class="active">
						<a href="/index.jsp">
							<label>首页</label>
							<span class="en_text">HOME</span>
							<i></i>
						</a>
					</li> -->
					<li class="liafter"><a href="/slotGame.jsp?showtype=PT"
						class="laohugame"> <label>老虎机</label> <span class="en_text">SLOTS</span>
							<!--<i class="icon-hot"></i>--> <i></i>
					</a></li>
					<li class="liafter"><a href="#" class="live_game">
							<label>真人彩票</label> <span class="en_text">LIVE&LOTTERY</span> <i></i>
					</a></li>
					<li class="liafter"><a href="#" class="sport_box"> <label>电竞体育</label>
							<span class="en_text">SPORTS</span> <i></i>
					</a> <!-- <div class="sport_game_box">
							<div class="w_1060">
								<ul style="margin-left: 415px;">
									<li class="game_sb">
										<a href="javascript:;" class="sbLogin" ><span><label>沙巴体育</label></span></a>
									</li>
									<li class="game_pb">
										<a href="${ctx}/game/PBUserLogin.aspx " class="j-play" target="_blank"><span><label>平博体育</label></span></a>
									</li>

								</ul>
							</div>
						</div> --></li>
					<!-- <li>
						<a href="/LotteryLanding.jsp" >
							<label>彩票</label>
							<span class="en_text">LOTTERY</span>
							<i></i>
						</a>
					</li> -->
					<li class="liafter"><a href="#" class="Fishing_box"> <label>捕鱼棋牌</label>
							<span class="en_text">FISHING&CHESS</span> <i></i>
					</a> <!-- <div class="Fishing_game_box">
							<div class="w_1060">
								<ul style="margin-left: 552px;">
									<li class="game_ag">
										<a href="/gameAginBuyuRedirect.aspx" class="j-check"><span><label>AG捕鱼</label></span></a>
									</li>
									<li class="game_pt">--%>
										<a href="/loginGame.aspx?gameCode=cashfi" class="j-check"><span><label>PT捕鱼</label></span></a>
									</li>	
									<li class="game_ttg">
										<a href="/asp/ttLogin.aspx?gameName=EGIGame&gameType=0&gameId=14900&lang=zh-cn&deviceType=web" class="j-check"><span><label>GG捕鱼</label></span></a>
									</li>
									<li class="game_mw">
										<a href="javascript:;" onclick="mwGame('1051')"><span><label>MW捕鱼</label></span></a>
									</li>
									<li class="game_Hql">
										<a href="/game/gameLoginDTFish.aspx" class="j-check"><span><label>钱来捕鱼</label></span></a>
									</li>
								</ul>
							</div>
						</div> --></li>

					<!-- <li >
						<a href="/Chessgame.jsp#chessTwo" class="game_mwbox">
							<label>棋牌</label>
							<span class="en_text">CHESS GAMES</span>
						<i></i>
						</a>
						<div class="game_mw_box">
							<div class="w_1060">
								<ul style="margin-left:650px;">
									<li class="game_mw">
										<a href="/Chessgame.jsp#chessOne" target="_blank"><span><label>大满贯</label></span></a>
									</li>
									<li class="game_761">
										<a href="/Chessgame.jsp#chessTwo"  target="_blank"><span><label>761棋牌</label></span></a>
									</li>
									<li class="game_ky">
										<a href="/Kygame.jsp#three"  target="_blank"><span><label>KY棋牌</label></span></a>
									</li>
								</ul>
							</div>
						</div>
					</li> -->
					<!-- <li>
						<a href="/esprots.jsp">
							<label>电竞</label>
							<span class="en_text">E-SPORTS</span>
						<i></i>
						</a>
					</li> -->

					<li class="liafter">
						<!--class="j-check" --> <a href="/promotion.jsp"
						class="promotion_box"> <label>最新优惠</label> <span
							class="en_text">PROMOTIONS</span> <i></i>
					</a>

					</li>

					<!-- <li>
						<a class="last" href="/agent.jsp">
							<label>代理加盟</label>
							<span class="en_text">JOIN AGENT</span>
						</a>
					</li> -->
					<div class="sub-nav-box sub-nav">
						<div class="wrapper">
							<!--老虎机-->
							<ul>
								<!--  <li>
				                    <a href="/slotGame.jsp?showtype=PT">
				                        <i class="bg-pt_icon"></i><span>PT</span>
				                    </a>
				                </li>
				                <li>
				                    <a href="/slotGame.jsp?showtype=PTSW">
				                        <i class="bg-ptsw_icon"></i><span>SW</span>
				                    </a>
				                </li> -->
								<li><a href="/slotGame.jsp?showtype=MGS"> <i
										class="bg-mg_icon"></i><span>MG</span>
								</a></li>

								<li><a href="/slotGame.jsp?showtype=DT"> <i
										class="bg-dt_icon"></i><span>DT</span>
								</a></li>
								<li><a href="/slotGame.jsp?showtype=PG"> <i
										class="bg-qt_icon"></i><span>PG</span>
								</a></li>
								<li><a href="/slotGame.jsp?showtype=CQ9"> <i
										class="bg-qt_icon"></i><span>CQ9</span>
								</a></li>
								<li><a href="/slotGame.jsp?showtype=BBIN"> <i
										class="bg-qt_icon"></i><span>BBIN</span>
								</a></li>

								<!--              <li>
				                    <a href="/slotGame.jsp?showtype=PNG">
				                        <i class="bg-png_icon"></i><span>PNG</span>
				                    </a>
				                </li> -->

								<!--   <li>
				                    <a href="/slotGame.jsp?showtype=TTG">
				                        <i class="bg-ttg_icon"></i><span>TTG</span>
				                    </a>
				                </li> -->
								<!--  <li>
				                    <a href="/slotGame.jsp?showtype=NT">
				                        <i class="bg-nt_icon"></i><span>NT</span>
				                    </a>
				                </li>
				                 -->

							</ul>
							<!--百家乐-->
							<ul>
								<li><a href="javascript:;" onclick="bgLiveGame()">
										<i class="bg-ag_icon"></i><span>BG真人</span>
								</a></li>
							</ul>
							<!--体育-->
							<ul>
								<li><a href="javascript:;" onclick="bbSportGame()"> <i class="bg-bbin_icon"></i><span>BB体育</span>
								</a></li>
								<li><a href="javascript:;" onclick="sportGame()"> <i class="bg-sb_icon"></i><span>沙巴体育</span>
								</a></li>
							</ul>
							<!--捕鱼游戏-->
							<ul>
								<li><a href="javascript:;" onclick="bbinFishLogin('30599')"
									title="捕鱼游戏"> <i class="bg-pt_icon"></i><span>捕鱼达人</span>
								</a></li>
	<!-- 							<li><a href="javascript:;" onclick="bbinFishLogin('30598')"
									title="捕鱼游戏"> <i class="bg-pt_icon"></i><span>捕鱼达人2</span>
								</a></li> -->
								<li><a href="javascript:;" onclick="bbinFishLogin('38001')"
									title="捕鱼游戏"> <i class="bg-pt_icon"></i><span>捕鱼大师</span>
								</a></li>
			<!-- 					<li><a href="javascript:;" onclick="bbinFishLogin('38002')"
									title="捕鱼游戏"> <i class="bg-pt_icon"></i><span>富贵渔场</span>
								</a></li> -->
							</ul>
							<!--棋牌游戏-->
							<!--  <ul>
				            	<li>
				                    
				                    
				                        <a href="javascript:;" onclick="alert('请先登录帐号！');" title="棋牌游戏">
				                            <i class="bg-ag_icon"></i><span>AG棋牌厅</span>
				                        </a>
				                    
				                </li>
				                <li>
				                    
				                    
				                        <a href="javascript:;" onclick="alert('请先登录帐号！');" title="棋牌游戏">
				                            <i class="bg-ag_icon"></i><span>AG龙虎</span>
				                        </a>
				                    
				                </li>
				                <li>
					                
					                
					                    <a href="javascript:;" onclick="alert('请先登录帐号！');">
					                        <i class="bg-716_icon"></i><span>棋乐游棋牌</span>
					                    </a>
					                
				                </li>
				                <li>
					                
					                
					                    <a href="javascript:;" onclick="alert('请先登录帐号！');">
					                        <i class="bg-716_icon"></i><span>百人牛牛</span>
					                    </a>
					                
				                </li>
				                <li>
					                
					                
					                    <a href="javascript:;" onclick="alert('请先登录帐号！');">
					                        <i class="bg-716_icon"></i><span>炸金花</span>
					                    </a>
					                
				                </li>
				                <li>
					                
					                
					                    <a href="javascript:;" onclick="alert('请先登录帐号！');">
					                        <i class="bg-716_icon"></i><span>德州扑克</span>
					                    </a>
					                
				                </li>
				                

				            </ul> -->
							<!--最新优惠-->
							<!-- <ul>
				                <li> 
				                    <a href="/promotions.jsp">
				                        <i class="bg-hot_icon"></i><span style="width:70px">最新优惠</span>
				                    </a>
				                </li>
				                <li>
				                    <a href="/yabo/2017Christmas/index.jsp?v=50000305">
				                        <i class="bg-new_icon"></i><span style="width:70px">每月礼物</span>
				                    </a>
				                </li>
				            </ul> -->

							<!--下载中心-->
							<!-- <ul> -->

							<!-- <li id="header_appdown">
				                    <a href="javascript:;">
				                        <i class="bg-down_icon"></i><span>官网APP</span>
				                    </a>
				                </li>-->
							<!-- <li> 
				                    <a href="/download/downloadPtClient.jsp">
				                        <i class="bg-down_icon"></i><span></span>
				                    </a>
				                </li>
				                <li>
				                    <a href="/download/downloadPt.jsp">
				                        <i class="bg-pt_icon"></i><span>PT电脑端</span>
				                    </a>
				                </li> -->
							<!-- <li>
				                    <a href="/download/downloadDt.jsp">
				                        <i class="bg-dt_icon"></i><span>DT手机端</span>
				                    </a>
				                </li> -->
							<!--  <li>
				                    <a href="/download/downloadmobile.jsp">
				                        <i class="bg-down_icon"></i><span>PT手机端</span>
				                    </a>
				                </li> -->
							<!-- <li>
				                    <a href="/download/downloadEbet.jsp">
				                        <i class="bg-ebet_icon"></i><span>EBET手机端</span>
				                    </a>
				                </li>
				                <li>
				                    <a href="/download/downloadAg.jsp">
				                        <i class="bg-ag_icon"></i><span>AG手机端</span>
				                    </a>
				                </li> -->
							<!-- </ul> -->
						</div>
					</div>
				</ul>
			</div>
			<input type="hidden" id="j-isLogin" value="${session.customer!=null}">

		</div>

		<!--}菜单-->
		<script>
			!function() {
				var menuBtns = document.querySelectorAll('#j-headerNav>li>a'), pathName = window.location.href
						.replace(window.location.origin, '');
				for (var i = 0; i < menuBtns.length; i++) {
					var ele = menuBtns[i];
					var href = ele.getAttribute('href');
					if (ele.parentNode.className != 'center') {
						href === pathName ? ele.parentNode.className = 'active'
								: ele.parentNode.className = '';
					}
				}
			}();
		</script>
		<script>
			$(function() {
				var lhj = $(".laohuji2").val();
				var qt = $(".qita2").val();
				var sun = parseInt(lhj) + parseInt(qt);
				$(".daili_money2").text(sun)
				$(".ck_app").click(function() {
					$.ajax({
						url : "/asp/checkAgentURLogin.aspx",
						type : "post",
						dataType : "text",
						success : function(data) {
							if ('false' == data) {
								alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
							} else {
								window.location.href = "/phone.jsp";
							}
						}
					});

				})

				$(".ck_xiazai")
						.click(
								function() {
									$
											.ajax({
												url : "/asp/checkAgentURLogin.aspx",
												type : "post",
												dataType : "text",
												success : function(data) {
													if ('false' == data) {
														alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
													} else {
														window.location.href = "https://www.qnappld.com/longdu.exe";
													}
												}
											});
								})

				$(".sbLogin").click(function() {
					$.get("/asp/sbLogin.aspx", function(data) {
						if (data.code == 0) {
							alert(data.msg)
						} else if (data.code == -1) {
							alert(data.msg)
						} else {
							window.location.href = "/sport.jsp";
						}
					})
				})

				$("#wjpassww").click(function() {
					$("#modal-forget").find(".modal-hd").show();
				})

				$(".laohugame").parent().hover(function() {
					$(".drop_subnav").show();
				}, function() {
					$(".drop_subnav").hide();
				})

				$(".live_game").parent().hover(function() {
					$(".live_mull").show();
				}, function() {
					$(".live_mull").hide();
				})

				$(".Fishing_box").parent().hover(function() {
					$(".Fishing_game_box").show();
				}, function() {
					$(".Fishing_game_box").hide();
				})
				$(".sport_box").parent().hover(function() {
					$(".sport_game_box").show();
				}, function() {
					$(".sport_game_box").hide();
				})

				$(".game_mwbox").parent().hover(function() {
					$(".game_mw_box").show();
				}, function() {
					$(".game_mw_box").hide();
				})
				$(".promotion_box").parent().hover(function() {
					$(".promotion_box_nav").show();
				}, function() {
					$(".promotion_box_nav").hide();
				})

				//				$(".show_pt").hover(function(){
				//					$(this).css("background-image","url(../images/game_pt.png)");
				//					$(".show_pt").text();
				//				})

			})
		</script>

		<script>
			$(function() {

				$(".j-check").click(function() {
					if ('${session.customer==null}' == 'true') {
						alert('请登入后，再进行游戏！')
						return false;
					}
				})
			})
		</script>

		<script>
			function mwGame(type) {
				if ($("#j-isLogin").val()) {
					openProgressBar();
					$.post("/asp/mwgLogin.aspx", {
						"gameCode" : type
					}, function(response) {
						closeProgressBar();
						if (response.code == 0) {
							window.location.href = response.data;
						} else {
							alert(response.msg);
						}
					})
				} else {
					alert('您好，请先登录！');
				}
			}
			
			function sportGame(type) {
				if ($("#j-isLogin").val()) {
					openProgressBar();
					$.post("/game/sbLogin.aspx", function(response) {
						closeProgressBar();
						if (response.code == 0) {
							window.location.href = response.data;
						} else {
							alert(response.msg);
						}
					})
				} else {
					alert('您好，请先登录！');
				}
			}

			
			function bbSportGame(type) {
				if ($("#j-isLogin").val()) {
					openProgressBar();
					$.post("/game/bbSportLogin.aspx", function(response) {
						closeProgressBar();
						if (response.code == 0) {
							window.location.href = response.data;
						} else {
							alert(response.msg);
						}
					})
				} else {
					alert('您好，请先登录！');
				}
			}
			
			function bgLiveGame(type) {
				if ($("#j-isLogin").val()) {
					openProgressBar();
					$.post("/game/bgLogin.aspx", function(response) {
						closeProgressBar();
						if (response.code == 0) {
							window.location.href = response.data;
						} else {
							alert(response.msg);
						}
					})
				} else {
					alert('您好，请先登录！');
				}
			}
			function bbinFishLogin(type) {
				if ($("#j-isLogin").val()) {
					openProgressBar();
				$.post("/game/bbinFishLogin.aspx", {
						"gameCode" : type
					},  function(response) {
						closeProgressBar();
						if (response.code == 0) {
							window.location.href = response.data;
						} else {
							alert(response.msg);
						}
					})
				} else {
					alert('您好，请先登录！');
				}
			}
			
			document.onkeydown = function(event) {
				var e = event || window.event;
				if (e && e.keyCode == 13) { //回车键的键值为13
					$("#j-login").trigger('click'); //调用登录按钮的登录事件
				}
			};
		</script>
	</div>