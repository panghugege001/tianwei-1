<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>胜负预测</title>
		<link rel="stylesheet" type="text/css" href="css/index.css" />
		<link rel="stylesheet" href="/css/base.css?v=2" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	</head>
	
	<body id="ztid">
		<jsp:include page="${ctx}/activety/common/activety_header.jsp"></jsp:include>
		<div class="bg-box">
			<div class="container-box">
				<div class="step-1-box">
					<div class="title">
						<img src="img/jb1.png" alt="" class="jb1">
						<img src="img/text1.png" alt="">
					</div>
					<div class="ball-box">
						<img src="img/jbd1.png" alt="" class="jbd1">
						<img src="img/jbd2.png" alt="" class="jbd2">
						<img src="img/man1.png" alt="" class="man1">
						<img src="img/tm.png" alt="" class="tm">
						<img src="img/win.png" alt="" class="win">
						<img src="img/lost.png" alt="" class="lost">
					</div>
				</div>
				<div class="step-2-box">
					<img src="img/jb2.png" alt="" class="jb2">
					<div class="mobi-title">
						<img src="img/title-2.png" alt="">
					</div>
					<div class="text-box1">
						<p><span class="c-gold">活动对象：</span>&nbsp;&nbsp;天威所有会员</p>
						<p><span class="c-gold">活动时间：</span>&nbsp;&nbsp;2018年7月1日 - 2018年7月31日</p>
						<p><span class="c-gold">活动内容：</span>&nbsp;&nbsp;预测你今日运气，选择此场战役“胜”或 “负”</p>
					</div>
					<div class="title-box">
						<img src="img/text2.png" alt="">						
					</div>

					<div class="text-box2">
						<p><span class="c-gold">选择【胜】：</span>&nbsp;&nbsp;存款金额达成18倍流水，完成流水限制</p>
						<p><span class="c-gold">选择【负】：</span>&nbsp;&nbsp;帐户内金额低于5元以下，解除流水限制</p>

					</div>
					<div class="ball-box clearfix">
						<div class="start-left">

							<i class="jb4"></i>
							<img src="img/start1.png" alt="">
							<p>选择您拿手的平台进行开战，百战百胜</p>
							<div class="chose-box">
								<i class="arrow-left arrow-btn"></i>
								<i class="arrow-right arrow-btn"></i>
								<div class="game-item-box">
									<div class="game-item item-pt active" data-value="6001">
										<img src="img/pt-icon.png" alt="">
										<p>PT老虎机</p>
									</div>
									<div class="game-item item-ttg" data-value="6006">
										<img src="img/ttg-icon.png" alt="">
										<p>TTG老虎机</p>
									</div>
									<div class="game-item item-other" data-value="6009">
										<img src="img/other-icon.png" alt="">
										<p>老虎机钱包</p>
									</div>
								</div>

							</div>
						</div>
						<div class="start-right">
							<i class="jb5"></i>
							<img src="img/start2.png" alt="">
							<s:url action="execXima" namespace="/asp" var="execXimaUrl"></s:url>
							<p>输入欲转帐金额，预测胜负后，即可开战</p>
	                         <form class="ui-form" id="selform" action="${execXimaUrl }" method="post"
	                                  onsubmit="return checkSubmit()">
	                                <div class="ui-form-box" style="display: none;">
	                                    <label class="ui-label">选择平台：</label>
	                                    <select class="ui-ipt" onchange="youHuiNameChange(this.value);" id="youhuiName">
	                                        <option value="6001">PT存送优惠</option>
	                                        <option value="6006">TTG存送优惠</option>
	                                        <option value="6009">老虎机存送优惠(SW.MG.DT.PNG.QT.NT)</option>
	                                    </select>
	                                </div>
	                                <div class="ui-form-box">
	                                    <select name="youhuiType" id="youhuiType" class="ui-ipt-2"
	                                            onchange="youHuiTypeChange(this.value);">
	                                        <option value="">---请选择存送类型---</option>
	                                    </select>
	                                </div>

	                                <div class="ui-form-box">
	                                    <input type="text" class="ui-ipt" placeholder="请输入挑战金额" name="transferMoney" id="transferMoney" onkeyup="clearNoNum(this);"/>
	                                </div>
	                                <div class="ui-form-box">
	                                    <input type="text" class="ui-ipt" placeholder="红利金额" name="giftMoney" id="giftMoney" readonly/>
	                                </div>
	                                <div class="ui-form-box">
	                                    <input type="text" class="ui-ipt" placeholder="流水倍数" style="margin-top:5px;" name="waterTimes"
	                                           id="waterTimes" readonly/>
	                                </div>
	                                <div class="ui-for-box checkbox">
	                                	<input type="radio" name="type" id="radioWin" data-type="1"><label for="radioWin">竞猜胜利</label>
										<input type="radio" name="type" id="radioLost" data-type="0"><label for="radioLost">竞猜失败</label>
	                                </div>
	                                <div class="user_tijiao">
	                                    <input type="button" class="btn" value="领取红利去开战"
	                                           onclick="return checkSelfYouHuiSubmit();"/>
	                                </div>
	                            </form>
						</div>
					</div>
				</div>

				<div class="step-3-box">
					<div class="mobi-title">
						<img src="img/title-1.png" alt="">
					</div>
						<p>1、申请方式：输入金额 > 选择对应的游戏平台  > 选定此次战役胜/负>领取红利开战>前往战场</p>
						<p>2、领取彩金: 竟猜”胜负”后, 达成条件谘询在线客服获得挑战彩金。（竞猜奖励为按照申请挑战金额*50%，15倍水）</p>
						<p>3、此优惠10元起即可参加，每个账户选择一个平台只能竟猜一次。（以申请最近一次的次存记录作为依据申请。）</p>
						<p>4、活动彩金，仅限于在PT / TTG / SLOT老虎机游戏平台进行使用。</p>
						<p>5、本次优惠和返水共享。</p>
						<p>6、凡是只靠此优惠套取优惠者，我们有权发至游戏平台方审核，严重者扣除盈利及本金并且禁用会员账号。</p>
						<p>7、天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
				</div>
			</div>
		</div>
		<jsp:include page="${ctx}/activety/common/activety_footer.jsp"></jsp:include>
		<script src="js/index.js"></script>
	</body>
</html>