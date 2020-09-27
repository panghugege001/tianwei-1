<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="好友推荐中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="friends-tab">
			<a href="javascript:;" class="tab-item active">好友推荐</a>
			<a class="tab-item" id="friends-records-action" data-toggle="tab" aria-expanded="true" data-href="${ctx}/asp/queryfriendRecord.aspx">推荐记录</a>

		</div>
		<div class="friends-tab-content">
			<div class="page-friends active">
				<div class="form-warp no-icon">
					<div class="form-tips">推荐方式1<span class="c-red">(专属链接)</span></div>
					<div class="form-group">
						<input class="form-control" value="tianwei4.com" id='friendUl'></input>
						<div class="form-code" onclick="copyNum('#friendUl')">复制</div>
					</div>
					<div class="form-tips">推荐方式2<span class="c-red">(专属二维码)</span></div>
					<div class="down-qrcode">
						<div id="fCode"></div>
						<div class="qrcode-tips">
							<div class="h3">温馨提示：</div>
								<p>1.点击复制，可保存推荐链接，提供给您的好友进行注册。</p>
	                            <p>2.扫码即可下载，方便快速!</p>
						</div>
					</div>
					<div class="friends-table">
						<table>
							<thead>
								<tr>
									<td>天威所有会员</td>
									<td>被推荐人存款</td>
									<td>彩金</td>
									<td>流水倍数</td>
								</tr>
							</thead>
							<tbody>
								<tr><td rowspan="5">推荐人</td><td>100</td><td>18</td><td rowspan="5">10</td></tr>
								<tr><td>1000</td><td>38</td></tr>
								<tr><td>5000</td><td>58</td></tr>
								<tr><td>10000</td><td>168</td></tr>
								<tr><td>50000</td><td>688</td></tr>
							</tbody>
						</table>
					</div>
					<div class="text-tips">
						<div class="h3">活动规则</div>
	                    <p>1、登入游戏帐号进入【我/帐户管理】 → 【推广好友】 → 【网址推广】</p>
	                    <p>复制您的链结分享给好友进行注册，并且存款游戏，推荐人即可获得对应彩金。</p>
	                    <p>2、申请方式：发送邮件到tianwei661@gmail.com申请。</p>
	                    <p>标题：邀请好友双向彩金，</p>
	                    <p>内容：被邀请人帐号、邀请人帐号。</p>
	                    <p>3、每个存款阶段皆可申请一次彩金，达到流水倍数即可提款。</p>
	                    <p>4、如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡，同一电脑等其他任何不正常
	                    投注行为，我们将有权冻结账号并收回盈利。</p>
	                    <p>5、天威保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
					</div>
				</div>
				<div class="copy-done">
					<i class="iconfont icon-duihaocheckmark17"></i>
					<div>复制成功</div>
				</div>
			</div>
			<div class="friends-grid">
                <div id="record-hy">
                    <iframe src="" width="100%" height="780" border="0" scrolling="no" frameborder="0"
                            allowtransparency="true" class="if-record">
                    </iframe>
                </div>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script src="${ctx}/js/lib/jquery.qrcode.min.js"></script>
		<script type="text/javascript">
			// 思路：要想复制到剪贴板，必须先选中这段文字。
			function copyNum(dom) {
				var NumClip = $(dom).get(0);
				var NValue = NumClip.value;
				var valueLength = NValue.length;
				selectText(NumClip, 0, valueLength);
				if(document.execCommand('copy', false, null)) {
					document.execCommand('copy', false, null) // 执行浏览器复制命令
					console.log("已复制,赶紧分享给朋友吧");
					$(".copy-done").fadeIn(1000)
					setTimeout(function() {
						$(".copy-done").fadeOut(1000)
					}, 1500)
				} else {
					console.log("不兼容");
				}

			}
			// input自带的select()方法在苹果端无法进行选择，所以需要自己去写一个类似的方法
			// 选择文本。createTextRange(setSelectionRange)是input方法
			function selectText(textbox, startIndex, stopIndex) {
				if(textbox.createTextRange) { //ie
					var range = textbox.createTextRange();
					range.collapse(true);
					range.moveStart('character', startIndex); //起始光标
					range.moveEnd('character', stopIndex - startIndex); //结束光标
					range.select(); //不兼容苹果
				} else { //firefox/chrome
					textbox.setSelectionRange(startIndex, stopIndex);
					textbox.focus();
				}
			}
		</script>
		<script>
				var $url = $('#friendUl');

				function _queryFriendBonue(){
					mobileManage.getLoader().open('获取链接中');
					$.post('mobi/queryFriendBonue.aspx', function(result, status) {
						if(result.success){
							var friendurl = window.location.origin+"?friendcode="+ result.data.url;
							$url.val( friendurl);
							$("#fCode").qrcode({width: 150,height:150,text: friendurl});
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}
				
				_queryFriendBonue();
		</script>
		<script>
			$('.friends-tab a').on('click',function () {
				var $this=$(this),index = $this.index();
				$this.addClass('active').siblings('').removeClass('active');
				$('.friends-tab-content>div').eq(index).addClass('active').siblings('').removeClass('active');
			})
		</script>
		<script>
		    var $recordsIframe = $('#record-hy'),
		        $recordsAction = $('#friends-records-action'),
		        $iframe = $recordsIframe.find('iframe');
		    $(function () {
		        $recordsAction.on('click', (function () {
		            var $this = $(this);
		                $iframe.show();
		                $iframe.attr('src', '');
		                $iframe.attr('src', $this.attr('data-href'));
		        }));

		        $recordsAction.find('a').first().trigger('click');
		    });
		</script>
		<script>
			   window.onload = function () {
        		var url = window.location.search;
        		if (url == "?tj-link") {
		            $('#friends-records-action').trigger('click');
		        }
        	}
		</script>
<!-- 		<script>
	function friendsGrid () {
		mobileManage.getLoader().open('获取好友列表');
		$.post('${ctx}/mobi/queryHistory.aspx', function(result, status) {
			if(result.success){
				console.log(result)
			}else{
				alert(result.message);
			}
			mobileManage.getLoader().close();
		});
	}
</script> -->
	</body>

</html>