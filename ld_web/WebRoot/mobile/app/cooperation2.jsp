<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

	<jsp:include page="tpl/appCommon.jsp" />
	
	<link rel="stylesheet" type="text/css" href="mobile/app/css/cooperation2.css?v=3" />

	<div class="main-wrap" style="overflow-x: inherit !important;">
		<div class="container">
			<div class="header-margin"></div>
			<div class="content">
				<ul>
					<li class="active">
						<%--<div class="title">加盟注册</div>--%>
						<div class="page">
							<div class="content-in">
								<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span>代理账户</p>
									<input id="agent-account" type="text" placeholder="a_ 开头，由3-15个数字或英文母" required>
							    	<%--<div class="message"></div>--%>
							  	</div>
								<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span>登录密码</p>
							  	  	<input id="agent-password" type="password" placeholder="由6-16个数字或英文字母组成" required>
									<p class="tips-txt"></p>
							  	</div>
								<div class="coop-inputs">
									 <p class="s-title"><span class="red">* </span>确认密码</p>
							  	  	<input id="agent-confirmPassword" type="password" placeholder="再次填写登录密码" required>
							  	</div>
								<div class="coop-inputs">
									 <p class="s-title"><span class="red">* </span>真实姓名</p>
							  	  	<input id="agent-name" type="text" placeholder="必须与您的银行帐户名称相同，否则不能出款" required>
							  	</div>
								<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span>代理网址</p>
							  	  	<input id="agent-url" type="text" placeholder="填写2-6个数字或者是字母,您用来推广的网址" required>
							  	</div>
								<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span>电子邮箱</p>
							  	  	<input id="agent-email" type="text" placeholder="忘记密码时，取回密码的唯一途径" required>
							  	</div>
								<div class="coop-inputs">
									<p  class="s-title"><span class="red">* </span>联系电话</p>
							  	  	<input id="agent-phone" type="text" placeholder="加盟负责人及时联系您" required>
							  	</div>
								<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span> QQ/SWpe</p>
							  	  	<input id="agent-qq" type="text" required>
							    	<div class="message">	您可以通过QQ/SWpe方便与我们取得联系</div>
							  	</div>
								<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span>  微信号</p>
							  	  	<input id="agent-wechat" type="text" >
							    	<div class="message">通过微信方便与我们取得联系</div>
							  	</div>
								<div class="coop-inputs">
									<p class="s-title">加盟推荐码</p>
							  	  	<input id="agent-partner" type="text" >
							  	</div>
							  	<div class="coop-inputs">
									<p class="s-title"><span class="red">* </span>验证码</p>
							  	  	<input id="agent-code" class="security-code" type="text" maxlength="4">
							    	<div class="message">如果看不清验证码，请点图片刷新</div>
							    	<img id="agent-image" class="agent-image" title="如果看不清验证码，请点图片刷新" class="security-img" />
							  	</div>
								<div class="red font14">请确认资料是否填写正确，如有乱填将不审核，此账号为代理账号不能进行游戏</div>
							  	<div class="">
								    <label class="mui-checkbox">
								      <input id="agent-check1" type="checkbox">
								      	本人已经超过合法年龄以及本人在此网站的所有活动并没有抵触本人所身在的国家所管辖的法律。同时接受天威 &lt;用户协议&gt;。
								    </label>
								    <span class="agreement">查看天威 &lt;用户协议&gt;</span>
								</div>
						    	<div id="agent-submit" class="agent-btn">注册</div>
						    </div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>

	<%--查看协议--%>
	<div class="agreement-box">
		<div class="agreement-box-in">
			<%--<span class="close-btn"></span>--%>
			<div  class="agreement-detail">
				<h3 class="agreement-title">合作协议书</h3>
				<div class="hzbox">
					<h2>一、协议双方</h2>
					<p>
					甲方（授权方）天威<br/>
					乙方（获权方）代理方<br/>
					甲乙双方秉承着合作共赢的事宜达成协议，详情协议如下

					</p>
					<h2>二、协议双方说明</h2>
					<p>
					a，当您要注册合作账户时，请您详细地看下我们的合作协议书，再考虑是否愿意合作。<br/>
					b，当您注册完账户后，代理部会为您审核是否通过，以邮件或QQ的形式告知乙方。<br/>
					c，本协议在甲方乙方同意下立即生效(注册合作账户审核通过后默认生效)。<br/>
					</p>
					<h2>三、关于天威</h2>
					<p>天威隶属于—PIONEER SOLUTIONS TECHNOLOGY DEVELOPMENT LIMITED。注册于菲律宾，并持有菲律宾PAGCOR合法博彩牌照。公司历经10年之久，以全球领先秒提技术迅速占领亚洲市场份额，凭其雄厚的实力及业界优质关系网，致力打造亚洲最佳在线娱乐平台。</p>
					<h2>公司文化</h2>
						<p>天威秉承以人为本，科技立身的宗旨，为业界研发打造10秒到账「秒提」技术，致力为博彩界打造提款最迅速的公司。</p>
					<h2>品牌优势</h2>
					<p>
						① 提款：20万以下提款10秒到账，全球首发。<br />
						② 服务：7x24小时在线服务，提供优质服务。<br />
						③ 安全：天威采用128位SSL高标准加密技术。<br />
						④ 佣金结算：天威采用全球最快捷的日佣金结算方式。<br />
						⑤ 产品丰富：涵盖世界领先的合法博彩娱乐PT平台，TTG平台，NT平台及QT平台等。<br />
						⑥ 强大的品牌优势：多年业界运营实践经验，资历丰富。<br />
						⑦ 平台自动化：自助优惠，自助实时返水，独立研发。
					</p>

					<h2>四，佣金结算时间和方式</h2>
					<h2>1，佣金方案</h2>
					<p>您只需要网络推广或是介绍您身边朋友成为我们的玩家，天威会根据代理每月游戏的净利润收入按照佣金比例计算佣金。</p>
					<table class="my-table" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
					<th>等级</th>
					<th>当月活跃会员数</th>
					<th>当月净利润(RMB)</th>
					</tr>
					<tr>
					<td>1</td>
					<td>≥3</td>
					<td>0-300，000</td>
					</tr>
					<tr>
					<td>2</td>
					<td>≥15 </td>
					<td>300，001-1，000，000</td>
					</tr>
					<tr>
					<td>3</td>
					<td>≥40</td>
					<td>1，000，001-2，000，000</td>
					</tr>
					<tr>
					<td>4</td>
					<td>≥100</td>
					<td>≥2，000，001</td>
					</tr>
					</tbody>
					</table>
					<table class="my-table" cellpadding="0" cellspacing="0" style="margin-top:15px;">
					<tbody>
					<tr>
					<th>结算方式</th>
					<th>佣金计算</th>
					</tr>
					<tr style=" color:red">
					<td>日/月结佣金</td>
					<td>( PT、TTG、QT、NT平台输赢x85%-优惠-反水)x 佣金比例</td>
					</tr>

					</tbody>
					</table>
					<h2>2，佣金日结标准：</h2>
					<p><span class="red">老虎机计算公式：(平台输赢x85%-优惠-反水）=纯利润 纯利润x佣金比例=佣金。</span><br />
					电子类：包含PT\TTG\QT\NT\MG）。<br />
					享受标准：所有老虎机代理终身享受日结佣金。<br />
					提款要求：<span class="red">代理账户老虎机平台佣金额度大于100元即可提款。<br /></span>
					备注: <span class="red">(1-5号此时间段佣金不更新，于6号统一执行！)。<br /> </span>
					<span class="red">注：享受日结的代理如果连续两个月没有新增一位活跃会员，将在第三个月的时候取消其日结模式，如果在第三个月有开发会员，在次月开启日结。<br /></span>
					其他类：AG<br />
					其他类计算公式：(平台输赢x90%-优惠-反水）=纯利润 纯利润x佣金比例=佣金。<br />
					老代理：历史佣金累计15万以上，且连续6个月有佣金记录，可升级为VIP代理，享有佣金日结。<br />
					新代理：累计佣金30万以上，每月满足活跃会员要求，才能晋级VIP代理，享有日结佣金。<br />
					提款要求：每月月初1-5号为提款时间，请在这个时间段内申请其它平台的佣金额度，如果规定时间内未提款，佣金将累计至下月。<br />
					<span class="red">注：最终综合评定方可晋级，佣金负值累计。若合作伙伴自己的游戏账号注册 到自己代理下线，取消代理资格，佣金不予发放。</span><br /><br />

					玩家输赢里面包含了平台给玩家的所有优惠红利 以及公司向平台方支付的平台费用，天威合作伙伴需共同承担相应比例的费用包括：<br />
					（1）优惠——天威给予玩家的现金红利都计算在输赢里面。<br />
					（2）反水——天威每日会给玩家相应的反水，反水计算在玩家输赢里面。<br />
					（3）平台费用——天威需提款游戏平台供应商10%~15%的平台费用。<br />
					合作伙伴必须严格遵守天威相关条款条例，以免造成不必要的误会。<br />
					</p>

					<h2>3，佣金计算标准</h2>
					<p>
						1. 针对其他类佣金发放日为每月1—5号。(结算时间为自然月：上月1号：00:00点至本月1号00:00)。<br />
						2. 当月达到3位有效会员且佣金大于100元。<br />
						3. 有效会员标准，当月存款大于500元，投注额大于1000元。<br />
						4. 若当月未达到发放标准，佣金会累计至某月达到要求后一起发放。<br />
						5. 若当月代理佣金为负值，会累计至下月，等至累计正数且要求达到后佣金发放。<br />
						6. 若合作伙伴自己的游戏账号注册到自己代理下线，取消代理资格，佣金不予发放。<br />
					</p>
					<h2>五，责任与条款</h2>
					<p>
						1，甲乙双方应严格遵守天威《合作协议》以及其他协议相关条款条例<br />
						2，乙方在甲方公司代理推广过程中，如有不诚实，违规操作恶意毁损天威声誉，没有遵守《合作协议》甲方有权拒绝向乙方支付佣金的权利，参与天威推广的同时，已确认同意接受天威相关合作条款。<br />
						3，乙方同意本协议项下的合作均为天威后台为准，该数据也将作为解决争端时的有效判断依据，最终解释权归甲方所有。<br />
						4，甲方不能将自己的游戏账号注册到自己的代理下线，如有发现，自己的游戏账号输赢不计算在佣金内，如有恶意注册，佣金不予发放，且永久关闭合作账号。<br />
						5，如有玩家在乙方推荐的过程中，乙方或玩家输入错误合作连接注册进来的玩家为不计算在乙方下线玩家，公司概不负责。<br />
						6，一人只有一个合作代理账户，若乙方多个代理账户，所以账号输赢将会叠加计算。<br />
						7，甲方有权修改佣金的计算方式以及佣金比例。<br />
						8，乙方注册合作账户之后，此协议生效，有任何争议此协议为最终解释权。<br />
						9，在没有任何预先通知下，甲方有权更改以上条款和条例，解释权归甲方所有。<br />
						10，若合作代理和下线某个玩家IP一直是相同，次玩家输赢将不计算在下线输赢内。<br />
						11，请详细阅读以上条款条例，根据自身实际情况定夺是否能胜任我们的合作伙伴，若有任何的争议。<br />
					</p>
					<h2>六，法律监督和仲裁</h2>
					<p>
	1，无论是在任何法律条例的管制下，此协议都将受制于菲律宾政府的法律。任何与此协议相关的法律行为必须被带到菲律宾政府。您认可并认真阅读了此协议并同意其所有的规则及条款，在任何时候你确认您已经自主评估了参与的意愿并不依赖于任何在此协议之外的言论、保证和声明。
	<br />2，若乙方起诉甲方，根据以上规定需要菲律宾当地法庭做出最后的裁决其仲裁语言为英语。
	<br />3，天威《合作协议》请仔细阅读，出现合作争议本合作协议为最终解释权。<br />
					</p>
				<h3 class="red">再次申明：合作代理本人的游戏账号禁止注册到自己的代理线下。</h3>
			</div>
				<span class="agent-close-btn">关闭</span>
			</div>

			</div>

		</div>
	</div>


	<jsp:include page="tpl/appFooter.jsp" />
	<script type="text/javascript">
	var $tscreen = $('.header-margin');
	$tscreen.hide();

		var $account = $('#agent-account');
		var $password = $('#agent-password');
		var $confirmPassword = $('#agent-confirmPassword');
		var $name = $('#agent-name');
		var $url = $('#agent-url');
		var $email = $('#agent-email');
		var $phone = $('#agent-phone');
		var $qq = $('#agent-qq');
		var $wechat = $('#agent-wechat');
		var $partner = $('#agent-partner');
		var $imageCode = $('#agent-code');
		var $check1 = $('#agent-check1');
		var $image = $('#agent-image');
		$image.attr('src',mobileManage.getSecurityCodeUrl()+'?'+Math.random());




		//刷新验证码
		$image.click(function(){
			$image.attr('src',mobileManage.getSecurityCodeUrl()+'?'+Math.random());
		});
		
		//注册
		$('#agent-submit').click(function(){
			var formData = {
				account:$account.val(),
				password:$password.val(),
				confirmPassword:$confirmPassword.val(),
				name:$name.val(),
				email:$email.val(),
				url:$url.val(),
				phone:$phone.val(),
				qq:$qq.val(),
				wechat:$wechat.val(),
				partner:$partner.val(),
				check1:$check1.is(':checked')?1:0,
				imageCode:$imageCode.val()
			};
			mobileManage.getLoader().open('注册中');
			mobileManage.getUserManage().registerAgent(formData, function(result){
				if(result.success){
					alert(result.message);
					window.location.href=window.location.origin+'/mobile/app/agent/index.html';
				}else{
					mobileManage.getLoader().close();
					$imageCode.val('');
					$image.attr('src','mobi/mobileValidateCode.aspx?'+Math.random());
					showTips(result.message);

				}
			});
		});
		
		<%--查看协议--%>
	$('.agreement').click(function(){
	   showAgreement();
	});
	$('.agent-close-btn').click(function(){
	   closeAgreement();
	});

	function showAgreement(){
		$('.agreement-box').css('top','0')
		$('.agreement-box').show()
	}
	function closeAgreement(){
		$('.agreement-box').css('top','-100%');
		$('.agreement-box').hide()
	}
	</script>
