<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@page import="dfh.model.Users" %>
<!DOCTYPE html> 
<html>
<head>        
  	<jsp:include page="${ctx}/title.jsp"></jsp:include>
  	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<link rel="stylesheet" href="${ctx}/css/user.css?v=1111"/>
	<script type="text/javascript" src="${ctx}/js/lib/layer/layer.js"></script>
</head>
<body>
<jsp:include page="/tpl/header.jsp"></jsp:include>
<div class="maiocunTips j-maiocunTips hidden">
	<div>
		<h4>温馨提示</h4>
		<p>您有订单未完成支付，请您先核实。若未支付，需要重新建立订单，请先作废之前订单!<br>请您按照您输入的存款信息进行存款，方可实时到账！！！</p>
		<div>
			<span class="j-maiocunRemove">订单作废</span>
			<span class="j-maiocunClose">取消</span>
		</div>
		<span class="icon-close iconClose2 j-maiocunClose"></span>
	</div>
</div>
<div class="userBg">
	<div class="wrap">
		<jsp:include page="/tpl/userNav.jsp"></jsp:include>
		<div class="userBox tab-bd">
			<jsp:include page="/tpl/userCommon.jsp"></jsp:include>

			<div  id="tab-user-Center" class="userCon tab-panel active">
				<!-- <div class="userInfoCon" id="j-discount">
					<h2><span class="lineB">自助优惠</span></h2>
					<ul class="user-menu-n">
						<li><a href="${ctx}/manageCoupons.php?tab_id=tab-jyj" data-action="#tab-jyj" class="u-col1"><span class="user-icon u-jyj"></span>自助救援金</a></li>
						<li><a href="${ctx}/manageCoupons.php?tab_id=tab-return" data-action="#tab-return" class="u-col2"><span class="user-icon u-hs"></span>自助返水</a></li>
						<li><a href="${ctx}/manageCoupons.php?tab_id=tab-level" data-action="#tab-level" class="u-col3"><span class="user-icon u-jj"></span>自助晋级</a></li>
					</ul>
				</div> -->
				<div class="userInfoCon">
					<h2><span class="lineB">资金存入</span></h2>
					<ol class="user-menu-n j-deposit">
						<li><a href="javascript:;" class="u-col9 fontSize15"><span class="user-icon u-quick"></span>支付宝,网银秒存</a></li>
						<li><a data-action="tab-pay-wechat" href="${ctx}/userManage.php?tab_id=tab-pay-wechat" class="u-col4"><span class="user-icon u-weChat"></span>微信支付</a></li>
						<li><a data-action="tab-pay-zfb" href="${ctx}/userManage.php?tab_id=tab-pay-zfb" class="u-col5"><span class="user-icon u-alipay"></span>支付宝存款</a></li>
						<li><a data-action="tab-pay-online" href="${ctx}/userManage.php?tab_id=tab-pay-online" onclick="addHF();" class="u-col6"><span class="user-icon u-online"></span>在线支付</a></li>
						<li><a data-action="tab-pay-card" href="${ctx}/userManage.php?tab_id=tab-pay-card" class="u-col7"><span class="user-icon u-card"></span>点卡存款</a></li>
						<%--<li><a data-action="tab-pay-bank" href="${ctx}/userManage.php?tab_id=tab-pay-bank" onclick="bankInfos();" class="u-col8"><span class="user-icon u-netSilver"></span>网银转账</a></li>--%>
						<li><a data-action="tab-hc-online" href="${ctx}/userManage.php?tab_id=tab-hc-online" onclick="bankInfos();" class="u-col8"><span class="user-icon u-netSilver"></span>快捷支付</a></li>

					</ol>
				</div>
				<div class="userListBox">
					<div class="userActiv">
						<h5>本月最新活动</h5>
						<a href="/activety/cj/index.jsp" class="uActiveImg"><img src="/images/banner/b320.jpg" alt=""></a>
						<a href="/promotion.jsp" class="uActiveImg"><img src="images/user/ad/userImg1.jpg" alt=""></a>
						<a href="/promotion.jsp" class="uActiveImg"><img src="images/user/ad/userImg3.jpg?v=2" alt=""></a>
					</div>
				</div>
			</div>

			<div id="tab-deposit" class="userCon tab-panel"><!-- id="userTab2"-->
				<ul class="userMenu">
					<li class="active"><a data-toggle="tab" href="#tab-pay-mc" class="hvrUnderlineLeft">秒存转账<span class="icon-sanj uBt"></span></a></li>
					<li><a data-toggle="tab" href="#tab-pay-zfb" class="hvrUnderlineLeft">支付宝<span class="icon-sanj uBt"></span></a></li>
					<li><a data-toggle="tab" href="#tab-pay-wechat" class="hvrUnderlineLeft">微信存款<span class="icon-sanj uBt"></span></a></li>
					<li><a data-toggle="tab" href="#tab-pay-online" onclick="addHF();" class="hvrUnderlineLeft">第三方在线支付<span class="icon-sanj uBt"></span></a></li>
					<li><a data-toggle="tab" href="#tab-hc-online" class="hvrUnderlineLeft">快捷支付<span class="icon-sanj uBt"></span></a></li>
					<!--					<li><a data-toggle="tab" href="#tab-pay-bank" onclick="bankInfos();" class="hvrUnderlineLeft">网银转帐<span class="icon-sanj uBt"></span></a></li>-->
					<li><a data-toggle="tab" href="#tab-pay-card" class="hvrUnderlineLeft">点卡支付<span class="icon-sanj uBt"></span></a></li>
					<!--<li><a href="javascript:;" class="hvrUnderlineLeft">附言存款模式<span class="icon-sanj uBt"></span></a></li>-->
				</ul>
				<div class="userListBox userListBox2 tab-bd">
					<div id="tab-pay-mc" class="userRow tab-panel active">
						<div class="mcContent j-mcContent">
							<!--<h2>存款步骤：</h2>
							<ol>
								<li>1.选择存款方式并填写存款信息，填完后点击下一步。</li>
								<li>2.为了让你的存款迅速到帐，请务必按照此流程操作，存款信息只需填写一次，系统会自动保存供下次选择。</li>
							</ol>-->
							<dl class="userForm">
								<dt>&nbsp;</dt>
								<dd>
									<h1>秒存（手机转账，网银转账，支付宝转账）</h1>
									<h3 class="red">使用该通道，存款加赠0.5%！</h3>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>存款方式：</dt>
								<dd>
									<select id="quick-save-type" class="userFriendSel">
										<option value="2" selected="selected">支付宝转账</option>
										<option value="0">手机银行转账</option>
										<option value="1">网上银行转账</option>
									</select>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>存款姓名：</dt>
								<dd><input type="text" id="quick-save-username" maxlength="10" class="user-Ipt"></dd>
							</dl>
							<!--<dl class="userForm hidden">
								<dt>存款银行：</dt>
								<dd>
									<select id="quick-save-bank" class="userFriendSel">
										<option>请选择</option>
										<option>中国工商银行</option>
										<option>中国建设银行</option>
										<option>中国交通银行</option>
										<option>中国招商银行</option>
										<option>中国农业银行</option>
										<option>中国民生银行</option>
										<option>中国银行</option>
										<option>中国邮政</option>
										<option>光大银行</option>
										<option>广发银行</option>
										<option>兴业银行</option>
									</select>
								</dd>
							</dl>
							<dl class="userForm hidden">
								<dt>存款卡号：</dt>
								<dd><input type="text" id="quick-save-card" class="user-Ipt" maxlength="20" placeholder="请输入您的存款卡号..."></dd>
							</dl>-->
							<dl class="userForm">
								<dt>存款金額：</dt>
								<dd><input type="text" id="quick-save-money" class="user-Ipt" maxlength="10" placeholder="存款金额额度为1元-300万"></dd>
							</dl>
							<dl>
								<dt></dt>
								<dd><span class="money_prompt">请您输入小数点，以确保实时到账！(如10.11)</span></dd>
							</dl>
							<dl class="userForm">
								<dt></dt>
								<dd>
									<input type="button" id="quick-save-next" class="nextBtn ml148" value="下一步">
<!--									<input type="button" id="chkbankhistory" class="nextBtn ml10" value="查看历史银行记录">-->
								</dd>
							</dl>
							<div class="sTips j-sTips">
								<div>
									<div class="sTips_box">
										<h2 class="tit">温馨提示:</h2>
										<p>1.请注意！每次存款请<strong class="red_c font18" style="font-size:18px;font-weight:800;">先下订单生成附言</strong>再进行存款，这样才能快速到账！</p>
										<p>2.每天23：00-01：00为招行维护时间，此时间段内存款将会延迟到帐，如有问题请及时联系在线客服</p>
										<span class="j-sTipsClose">确定</span>
									</div>
								</div>
							</div>
						</div>
						<div class="mcContent j-mcContentRecord hidden">
							<h1>历史银行记录</h1>
							<p>&nbsp;</p>
							<table border="1" class="tablelist">
								<thead>
								<tr>
									<th>编号</th>
									<th>汇款银行</th>
									<th>姓名</th>
									<th>卡号</th>
									<th>操作</th>
								</tr>
								</thead>
								<tbody id="tbody">
								</tbody>
							</table>
							<div class="jsPage"></div>
							<dl class="userForm">
								<dt></dt>
								<dd><input type="button" class="nextBtn ml148 j-bank-history-back" value="返回"></dd>
							</dl>
						</div>
						<div class="mcContent j-bank-confirm hidden">
							<dl class="userForm">
								<dt>&nbsp;</dt>
								<dd>
									<h1>我们的收款账户</h1>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>收款银行：</dt>
								<dd><input type="text" id="sbankname" readonly="readonly" disabled="disabled" class="user-Ipt"></dd>
							</dl>
							<dl class="userForm">
								<dt>收款账号：</dt>
								<dd>
									<input type="text" id="saccountno" readonly="readonly" disabled="disabled" class="user-Ipt">
									<a href="javascript:;" class="copy copy1 bdBtn" data-clipboard-text="">复制</a>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>收款人姓名：</dt>
								<dd>
									<input type="text" id="saccountname" readonly="readonly" disabled="disabled" class="user-Ipt">
									<a href="javascript:;" class="copy copy1 bdBtn" data-clipboard-text="">复制</a>
								</dd>
							</dl>
							<dl class="userForm" class="j-mefuyan">
								<dt>附言：</dt>
								<dd>
									<input type="text" id="mefuyan" readonly="readonly" disabled="disabled" class="user-Ipt">
									<a href="javascript:;" class="copy copy1 bdBtn" data-clipboard-text="">复制</a>
								</dd>
							</dl>
							<h3 class="red j-mcTips">*请务必在转账汇款的备注中填写此汇款附言，存款将在1分钟之内添加到您的游戏账号中</h3>
							<dl class="userForm mt20">
								<dt>&nbsp;</dt>
								<dd>
									<h1>您的存款信息</h1>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>存款方式：</dt>
								<dd><input type="text" id="quick-confirm-type" readonly="readonly" disabled="disabled" class="user-Ipt"></dd>
							</dl>
							<dl class="userForm">
								<dt>存款姓名：</dt>
								<dd><input type="text" id="quick-confirm-username" readonly="readonly" disabled="disabled" class="user-Ipt"></dd>
							</dl>
							<!--<dl class="userForm j-quick-confirm-bank">
								<dt>存款银行：</dt>
								<dd><input type="text" id="quick-confirm-bank" readonly="readonly" disabled="disabled" class="user-Ipt"></dd>
							</dl>
							<dl class="userForm j-quick-confirm-card">
								<dt>存款卡号：</dt>
								<dd><input type="text" id="quick-confirm-card" readonly="readonly" disabled="disabled" class="user-Ipt"></dd>
							</dl>-->
							<dl class="userForm">
								<dt>存款金额：</dt>
								<dd><input type="text" id="quick-confirm-money" readonly="readonly" disabled="disabled" class="user-Ipt"></dd>
							</dl>
							<dl class="userForm">
								<dt></dt>
								<dd>
									<input type="button" id="bank-success" class="nextBtn ml148" value="我已成功存款">
									<input type="button" class="nextBtn ml10 j-bank-history-back" value="返回">
								</dd>
							</dl>
						</div>
						<div class="userPrompt">
							<h3 class="red">温馨提示：</h3>
							<p>
								1.请务必按照系统提示消息进行存款，银行卡转账“附言”必须填写，支付宝转账无需附言完成之后请点击“我已成功存款”，否则您的款项将无法及时到账<br>
								2.如果您的款项10分钟未能到账，请联系24小时在线客服！<br>
								3.此存款方式无需手续费。<br>
							</p>
						</div>
					</div>
					<div id="tab-pay-zfb" class="userRow tab-panel">
						<div class="userRBox">
							<c:forEach var="item" items="${payWayVos}">
								<c:if test="${item.payWay==1}">
									<h4>支付宝在线存款</h4>
									<ul>
										<li class="active">
											<a href="${ctx}/asp/zfb_pay.php?platformId=${item.id}&payUrl=${item.payCenterUrl}" target="_blank">
												<em><img src="images/user/on.png" alt=""></em>
												<img src="${ctx}/images/enter-alipay2.jpg" class="zImg" />
											</a>
										</li>
									</ul>
									<h3>温馨提示：</h3>
									<p>1.支付宝04：00——05：00为系统维护时间，所以在此时间段进行支付宝存款，存款将会延迟到账。</p>
									<p>2.使用支付宝存款最低存款<span class="red_b">${item.minPay}</span>元,最高存款<span class="red_b">${item.maxPay}</span>元。</p>
									<p>3.使用支付宝支付,第三方需收取<span class="red_b"><fmt:formatNumber value="${item.fee}"/>%</span>的手续费。</p>
								</c:if>
							</c:forEach>
						</div>
					</div>
					<div id="tab-pay-wechat" class="userRow tab-panel">
						<div class="userRBox">
							<c:forEach var="item" items="${payWayVos}">
								<c:if test="${item.payWay==2}">
									<ul>
										<li class="active">
											<a href="${ctx}/asp/wx_pay.php?platformId=${item.id}&payUrl=${item.payCenterUrl}" target="_blank">
												<em><img src="images/user/on.png" alt=""></em>
												<img src="${ctx}/images/enter-wechit.jpg" class="zImg">
											</a>
										</li>
									</ul>
									<div class="noteinfor">
										<h3>温馨提示</h3>
										<p>1.微信存款最低存款<span class="red_b">${item.minPay}</span>元,最高存款<span class="red_b">${item.maxPay}</span>元。</p>
										<p>2.使用微信支付,第三方需收取<span class="red_b"><fmt:formatNumber value="${item.fee}"/>%</span>的手续费。</p>
									</div>
								</c:if>
							</c:forEach>

							<!--微信存款额度验证-->
							<%--<c:if test="${wxValidaTeAmout != null}">
								<h4>微信存款额度验证</h4>
								<ul>
									<li class="active">
										<a href="${ctx}/wechatDepositRedirect.php" target="_blank">
											<em><img src="images/user/on.png" alt=""></em>
											<img src="${ctx}/images/enter-wechit.jpg" class="zImg">
										</a>
									</li>
								</ul>
								<div class="noteinfor">
									<h3>温馨提示</h3>
									<p>1.此通道不收手续费。</p>
									<p>2.如有疑问请联系客服。</p>
								</div>
							</c:if>--%>
						</div>
					</div>
					<div id="tab-pay-online" class="userRow tab-panel">
						<div class="userRBox">
							<c:forEach var="item" items="${payWayVos}">
								<c:if test="${item.payWay==3}">
									<ul>
										<li class="active">
											<a href="${ctx}/asp/online.php?platformId=${item.id}&payUrl=${item.payCenterUrl}" target="_blank">
												<em><img src="images/user/on.png" alt=""></em>
												<img src="${ctx}/images/enter-online.jpg" alt="" class="zImg">
											</a>
										</li>
									</ul>
									<h3>温馨提示：</h3>
									<p>单笔存款最低额度为${item.minPay}元，最高${item.maxPay}元</p>
								</c:if>
							</c:forEach>
						</div>
					</div>
					<div id="tab-hc-online" class="userRow tab-panel">
						<div class="userRBox">
							<c:forEach var="item" items="${payWayVos}">
								<c:if test="${item.payWay==4}">
									<ul>
										<li class="active">
											<a href="${ctx}/asp/quick.php?platformId=${item.id}&payUrl=${item.payCenterUrl}" target="_blank">
												<em><img src="images/user/on.png" alt=""></em>
												<img src="${ctx}/images/enter-online.jpg" alt="" class="zImg" />
											</a>
										</li>
									</ul>
									<h3>温馨提示：</h3>
									<p>单笔存款最低额度为${item.minPay}元，最高${item.maxPay}元</p>
								</c:if>
							</c:forEach>
						</div>
					</div>
					<!--<div id="tab-pay-bank" class="userRow tab-panel">
						<div class="userRBox">
							<ul id="j-bank-list">
								<li class="active">
									<a href="#j-bank-info" data-toggle="tab" data-url="/tpl/bank/bank-icbc.html" title="工商银行">
										<em><img src="images/user/on.png" alt=""></em>
										<img src="/images/bank/bank-icbc.png" class="zImg" alt="">
									</a>
								</li>
								<li>
									<a href="#j-bank-info" data-toggle="tab" data-url="/tpl/bank/bank-cmb.html" title="招商银行">
										<em><img src="images/user/on.png" alt=""></em>
										<img src="/images/bank/bank-cmb.png" class="zImg" alt="招商银行">
									</a>
								</li>
								<li>
									<a href="#j-bank-info" data-toggle="tab" data-url="/tpl/bank/bank-ccb.html" title="建设银行">
										<em><img src="images/user/on.png" alt=""></em>
										<img src="/images/bank/bank-ccb.png" class="zImg" alt="建设银行">
									</a>
								</li>
								<li>
									<a href="#j-bank-info" data-toggle="tab" data-url="/tpl/bank/bank-boc.html" title="中国银行">
										<em><img src="images/user/on.png" alt=""></em>
										<img src="/images/bank/bank-boc.png" class="zImg" alt="中国银行">
									</a>
								</li>
								<li>
									<a href="#j-bank-info" data-toggle="tab" data-url="/tpl/bank/bank-bcm.html" title="交通银行">
										<em><img src="images/user/on.png" alt=""></em>
										<img src="/images/bank/bank-bcm.png" class="zImg" alt="交通银行">
									</a>
								</li>
								<li>
									<a href="#j-bank-info" data-toggle="tab" data-url="/tpl/bank/bank-gdb.html" title="广发银行">
										<em><img src="images/user/on.png" alt=""></em>
										<img src="/images/bank/bank-gdb.png" class="zImg" alt="广发银行">
									</a>
								</li>
							</ul>
							<form style="display:block;" id="j-bank-info"></form>
						</div>
					</div>-->
					<div id="tab-pay-card" class="userRow tab-panel">
						<div class="userRBox">
							<c:forEach var="item" items="${payWayVos}">
								<c:if test="${item.payWay==5}">
									<ul>
										<li class="active">
											<a href="${ctx}/asp/point_card.php?platformId=${item.id}&payUrl=${item.payCenterUrl}" target="_blank">
												<em><img src="images/user/on.png" alt=""></em>
												<img src="${ctx}/images/enter-dianka.jpg" alt="" class="zImg" />
											</a>
										</li>
									</ul>
									<h3>温馨提示：</h3>
									<div class="noteinfor">
										<h3>温馨提示：</h3>
										<p>1.存款最低金额为<span class="red_b">10元</span>，点卡充值会扣取相应的手续费用，存款稍等2分钟之后直接刷新账户余额查询，如果未到账，请及时提供点卡卡号联系在线客服。</p>
										<p>2.存款金额小于点卡面值可以继续使用卡密存款，选择充值金额大于点卡面值，所成功的也是只有面值金额减去手续费用之后的额度。<span class="red_b">（100的点卡可以先充值50，再充值50；50的点卡选择100进行充值，也是只有50扣除手续费之后的额度）</span> </p>
											<%--<p>3.使用点卡前请注册一个智汇宝账户，方便您点卡充值未成功后额度将自动返还到智汇宝账号中，若没有申请智汇宝账号，点卡充值未成功，额度将不予返还，智汇宝注册地址：<a href="http://www.ddbill.com" target="_blank">www.ddbill.com</a>。</p>--%>
										<p>3.存款过程中有任何疑问请联系我们的在线客服或者QQ客服、智付客服QQ：4008822311。</p>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
			<div id="tab-withdraw" class="userInfoCon tab-panel">
				<h2><span class="lineB">我要提款</span></h2>
				<div class="userInfoBox selBox">
					<div class="userCouBox j-withdrawTips hidden">
						<div class="userPrompt">
							<h3>温馨提示：</h3>
							<p>请完善您的姓名，QQ，微信，生日，再申请提款</p>
							<a href="/manageData.php" class="nextBtn">完善个人信息</a>
						</div>
					</div>
					<div class="userCouBox j-withdraw" style="display:block;">
						<dl class="userForm">
							<dt>提款须知：</dt>
							<dd>平台余额需要转账到主账户方可进行提款申请！</dd>
						</dl>
						<form action="">
							<dl class="userForm">
								<dt>U乐游戏帐号：</dt>
								<dd>
									<input type="text" class="user-Ipt readly" disabled="disabled" value="${session.customer.loginname}">
								</dd>
							</dl>
							<dl class="userForm">
								<dt>U乐游戏余额：</dt>
								<dd>
									<input type="text" class="user-Ipt readly" disabled="disabled" value="${session.customer.credit}">
								</dd>
							</dl>
							<dl class="userForm" style="display:none">
								<dt>提款密码：</dt>
								<dd>
									<input type="password" class="user-Ipt" id="tkPassword" />
								</dd>
							</dl>
							<dl class="userForm">
								<dt>提款银行：</dt>
								<dd>
									<select id="tkBank" class="userFriendSel" onchange="getWithDrawBankNo(this.value)">
										<option value=""> 请选择银行 </option>
										<%--<option value="支付宝"> 支付宝 </option>--%>
										<option value="工商银行"> 工商银行 </option>
										<option value="招商银行"> 招商银行 </option>
										<option value="上海农村商业银行"> 上海农村商业银行 </option>
										<option value="农业银行"> 农业银行 </option>
										<option value="建设银行"> 建设银行 </option>
										<option value="交通银行"> 交通银行 </option>
										<option value="民生银行"> 民生银行 </option>
										<option value="光大银行"> 光大银行 </option>
										<option value="兴业银行"> 兴业银行 </option>
										<option value="上海浦东银行"> 上海浦东银行 </option>
										<option value="广东发展银行"> 广东发展银行 </option>
										<option value="深圳发展银行"> 深圳发展银行 </option>
										<option value="中国银行"> 中国银行 </option>
										<option value="中信银行"> 中信银行 </option>
										<option value="邮政银行"> 邮政银行 </option>
									</select>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>银行卡号/支付号：</dt>
								<dd>
									<input type="text" class="user-Ipt" id="tkAccountNo" readonly />
								</dd>
							</dl>
							<dl class="userForm">
								<dt>提款金额：</dt>
								<dd>
									<input type="text" class="user-Ipt" id="tkAmount" />
								</dd>
							</dl>
							<dl class="userForm">
								<dt>保密问题：</dt>
								<dd>
									<select id="mar_questionid" class="userFriendSel">
										<option value="1">您最喜欢的明星名字？</option>
										<option value="2">您最喜欢的职业？</option>
										<option value="3">您最喜欢的城市名称？</option>
										<option value="4">对您影响最大的人名字是？</option>
										<option value="5">您就读的小学名称？</option>
										<option value="6">您最熟悉的童年好友名字是？</option>
									</select>
									<span>还未绑定密保？<a href="${ctx}/bindValidAnswer.jsp" class="bdBtn" target="_blank" id="a_blindingQuestion">点击绑定</a></span>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>保密答案：</dt>
								<dd>
									<input type="text" class="user-Ipt" id="mar_answer" />
								</dd>
							</dl>
							<dl class="userForm">
								<dt>&nbsp;</dt>
								<dd><label><input type="checkbox" class="userCheck" id="tkAgree" />我已经读过<a href="#" class="links">《提款须知》</a>，并已清楚了解其规则。</label></dd>
							</dl>
							<%--<a href="javascript:;" class="nextBtn">提 交</a>--%>
							<input class="nextBtn" type="button" value="提交" onclick="return tkWithdrawal();" />
						</form>
					</div>
				</div>
			</div>
			<div id="tab-transfer" class="userInfoCon tab-panel">
				<h2><span class="lineB">我要转账</span></h2>
				<div class="userInfoBox selBox">
					<div class="userCouBox" style="display:block;">
						<form action="">
							<%--<dl class="userForm">
								<dt>来源账户：</dt>
								<dd>
									<select name="" id="gameShow4" class="userFriendSel">
										<option value="ule">--U乐账户--</option>
										<option value="ule">--PT乐账户--</option>
										<option value="ule">--MG乐账户--</option>
										<option value="ule">--TTG乐账户--</option>
										<option value="ule">--NT乐账户--</option>
										<option value="ule">--QT乐账户--</option>
										<option value="ule">--DT乐账户--</option>
									</select>
									<span>尚未登录！请重新登录！</span>
								</dd>
							</dl>--%>
							<dl class="userForm">
								<dt>来源账户：</dt>
								<dd>
									<select class="userFriendSel" onchange="transferMoneryOut(this.value);" id="transferGameOut">
										<option value="ul" selected="selected">U乐账户 </option>
										<option value="newpt">PT账户 </option>
										<option value="mg">MG账户</option>
										<option value="ttg">TTG账户 </option>
										<option value="nt">NT账户 </option>
										<option value="qt">QT账户 </option>
										<option  value="dt">DT账户 </option>
										<option  value="png">PNG账户 </option>
										<option  value="agin">AG账户 </option>
										<option value="ebetapp">EBet真人账户</option>
									</select>
									<span id="transferMoneryOutDiv"></span>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>目标账户：</dt>
								<dd>
									<select class="userFriendSel" id="transferGameIn" onchange="transferMoneryIn(this.value);">
										<option value="ul">U乐账户 </option>
										<option value="newpt">PT账户 </option>
										<option value="mg" selected="selected">MG账户</option>
										<option value="ttg">TTG账户 </option>
										<option value="nt">NT账户 </option>
										<option value="qt">QT账户 </option>
										<option value="dt">DT账户 </option>
										<option value="png">PNG账户 </option>
										<option value="agin">AG账户 </option>
										<option value="ebetapp">EBet真人账户</option>
									</select>
									<span class="transferMoneryInDiv"></span>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>转账金额：</dt>
								<dd>
									<input type="text" class="user-Ipt" id="transferGameMoney" maxlength="10">
								</dd>
							</dl>
							<input type="button" class="nextBtn" value="提 交" onclick="return transferMonery();" />
						</form>
						<div class="userPrompt">
							<h3>温馨提示：</h3>
							<p>1、请在户内转账前进行平台激活方可转账成功。</p>
							<p>2、户内转账只支持整数转账。</p>
							<p>3、进行户内转账时，请先关闭正在进行的游戏页面，避免出现错误。</p>
						</div>
					</div>
				</div>
			</div>
			<div id="tab-card-binding" class="userInfoCon tab-panel">
				<h2><span class="lineB">银行卡绑定</span></h2>
				<div class="userInfoBox selBox">
					<div class="userCouBox" style="display:block;">
						<form method="post" id="blindCardForm">
							<dl class="userForm">
								<dt>支付银行：</dt>
								<dd>
									<select class="userFriendSel" id="bdbank1">
										<option value="">请选择</option>
										<s:iterator value="%{#application.IssuingBankEnum}" var="bk">
											<option value=<s:property value="#bk.issuingBankCode"/>>
												<s:property value="#bk.issuingBank" />
											</option>
										</s:iterator>
									</select>
									<%--<span>尚未登录！请重新登录！</span>--%>
								</dd>
							</dl>
							<dl class="userForm">
								<dt>卡/折号：</dt>
								<dd><input type="user-Ipt" id="bdbankno1" class="user-Ipt" maxlength="100" /></dd>
							</dl>
							<dl class="userForm">
								<dt>登录密码：</dt>
								<dd>
									<input type="password" class="user-Ipt" id="bdpassword1" maxlength="15"/><span>请输入游戏账号密码</span>
								</dd>
							</dl>
							<input type="button" class="nextBtn" value="提交" onclick="checkbandingform1();" />
							<div class="userPrompt">
								<h3>温馨提示：</h3>
								<p>绑定银行卡/折号，可以免去您重复输入卡/折号的繁锁步骤!只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号；如需解绑，请与在线客服联系.<br>
									登录密码为游戏帐号密码。</p>
								<%--<p>支付宝只能绑定一个帐号；如需解绑，请与在线客服联系.<br>
                                    拼音或者短音验证仅限于“支付宝绑定”</p>--%>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div id="tab-zfb-binding" class="userInfoCon tab-panel">
				<h2><span class="lineB">支付宝绑定</span></h2>
				<div class="userInfoBox selBox">
					<div class="userCouBox" style="display:block;">
						<dl class="userForm">
							<dt>&nbsp;</dt>
							<dd><p class="red_b text-left j-zfb-msg2"></p></dd>
						</dl>
						<dl class="userForm">
							<dt>支付宝：</dt>
							<dd>
								<input type="text" id="id_zfb_account" class="user-Ipt" maxlength="30">
							</dd>
						</dl>
						<dl class="userForm">
							<dt>登录密码：</dt>
							<dd>
								<input type="password" class="user-Ipt" id="id_zfb_password" maxlength="15"><span>请输入游戏账号密码</span>
							</dd>
						</dl>
						<input type="button" class="nextBtn" value="确定" onclick="saveAlipayAccount1();">
					</div>
				</div>
			</div>
			<div id="tab-urgeOrder" class="userInfoCon tab-panel tab-panel">
				<h2><span class="lineB">我要催账</span></h2>
				<div class="userInfoBox userCon selBox"><!-- id="userTab"-->
					<ul class="userMenu modal-tabnav dunTit">
						<li class="active"><a href="#tab-reminder-form" data-toggle="tab" class="hvrUnderlineLeft">催账申请<span class="icon-sanj uBt"></span></a></li>
						<li><a href="#tab-reminder-list" data-toggle="tab" onClick="urgeOrderRecord(1);" class="hvrUnderlineLeft">催账记录清单<span class="icon-sanj uBt"></span></a></li>
					</ul>
					<div class="userCouBox tab-bd"><!--style="display:block;"-->
						<div id="tab-reminder-form" class="userDunBox mt30 tab-panel active"><!-- style="display:block;"-->
							<iframe id="submitUrgeOrderFrame" frameborder="none" scrolling="no" style="height:350px;width:100%" src="${ctx}/submitUrgeOrder.jsp"></iframe>
							<div class="userPrompt">
								<h3>温馨提示：</h3>
								<p>1.每天最多只能提交待处理+处理失败的催账单5条。</p>
								<p>2.工行附言存款,如果存款人姓名和注册人姓名不一致的时候需要提供截图或是工行电子回执单号。</p>
							</div>
						</div>
						<div id="tab-reminder-list" class="userDunBox tb-datalistbox tab-panel">
							<div id="czDiv"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
</div>

<jsp:include page="/tpl/footer.jsp"></jsp:include>
<!--支付宝弹窗{-->
<div class="modal fade" id="j-modal-zfb" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
	 style="display: none;">
	<div class="modal-dialog" role="document">
		<div class="modal-content modalRad">
			<div class="modal-hd">
				<h2 class="modal-title">绑定支付宝</h2>
				<button type="button" class="close closeTop" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			</div>
			<div class="modal-bd">
				<table class="tb-form">
					<tr class="userForm">
						<td class="first">支付宝帐号：</td>
						<td><input id="j-zfb-account" type="text"  class="user-Ipt" maxlength="30" autocomplete="off"></td>
					</tr>
					<tr class="userForm">
						<td class="first">游戏密码：</td>
						<td><input type="password" style="display:none;">
							<input id="j-zfb-password" type="password"  class="user-Ipt" maxlength="30" autocomplete="off"></td>
					</tr>
					<tr>
						<td class="first"></td>
						<td><input class="btnTj" type="button" value="提交" onclick="saveAlipayAccount()"></td>
					</tr>
				</table>
				<div class="noteinfor">
					<h2 class="tit">温馨提示</h2>
					<p>1. 使用支付宝“二维码”扫描存款，需要先绑定您的支付宝账号</p>
					<p>2. 支付宝“二维码”扫描存款，必须用您绑定的支付宝账号进行存款，否则无法实时到账</p>
					<p>3. 每位会员只能绑定一个支付宝帐号</p>
					<p>4. 支付宝04：00——05：00为系统维护时间，所以在此时间段进行支付宝存款，存款将会延迟到账</p>
					<p>5. 支付宝存款最低存款1元</p>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		var tab_id = '${tab_id}';
		if(tab_id) {
			$('#j-user-menu a[href="#tab-deposit"]').trigger('click');
			$('#tab-deposit a[href="#'+tab_id+'"]').trigger('click');
		}

		if ('${session.customer.accountName}' == '' || '${session.customer.birthday}' == '') {
			$('.j-withdrawTips').removeClass('hidden');
			$('.j-withdraw').addClass('hidden');
		}

		$('.j-sTipsClose').on('click',function () {
			$('.j-sTips').addClass('hidden');
		})

		$('.j-deposit a').eq(0).on('click',function () {
			$('#j-top-action > a').eq(0).click();
		})

	});
</script>
<!--}支付宝弹窗-->
<jsp:include page="/tpl/script.jsp"></jsp:include>
<script src="/js/lib/ZeroClipboard.min.js"></script>
<script type="text/javascript" src="/js/lib/layer/layer.js"></script>
<script type="text/javascript" src="/js/userManage.js?v=28"></script>
<script type="text/javascript" src="/js/user/userQuickSave.js?v=7"></script>
</body>
</html>