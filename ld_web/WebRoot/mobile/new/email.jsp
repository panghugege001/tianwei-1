<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="站内信" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body class="email-page" style="background: #eeeeee;">
		<div class="email-nav">
			<div class="item active">收件箱</div>
			<div class="item ">发件箱</div>
		</div>
		<div id="tab-warp">
			<div id="j-container" class="news-lists">
				<div class="item">
					<!--<div class="title">
					<i class="iconfont icon-arrow-next"></i>
					<div class="text">{{title}}</div>
					<div class="time"> {{time}} <i class="fr iconfont icon-downjiantou"></i></div>
				</div>
				<div class="content">{{content}}</div>-->
				</div>
			</div>
			<div class="outbox-view hidden no-icon">
				<div class="form-tips">标题</div>
				<div class="form-group">
					<input id='letter-title' type="text" placeholder="请填写信信息标题" class="form-control" />
					<div class="ipt-clear undone"></div>
				</div>
				<div class="form-tips">内容</div>
				<div class="form-group form-textarea">
					<textarea id='letter-content' class="form-control" cols="8" placeholder="请填写信息内容"></textarea>
				</div>
				<div class="submit-warp">
					<span id='content-count'>255</span>
					<div class="submit fr" onclick="saveLetter()">立即发送</div>
				</div>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/UserManage.js?v=1020"></script>
		<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1210"></script>
		<script type="text/javascript">
			$('.email-nav .item').click(function() {
				$(this).addClass('active').siblings().removeClass('active')
				$('#tab-warp>div').eq($(this).index()).show().siblings().hide();
			})

			function EmailPage() {
				var that = this;
				this.tpl = [
					'<div class="item layout-item{{isRead}}" data-id="{{id}}">',
					'<div class="title" >',
					'	<i class="iconfont icon-arrow-next"></i>',
					'	<div class="text">{{title}}</div>',
					'	<div class="time"> {{createDate}} <i class="fr iconfont icon-downjiantou"></i></div>',
					'</div>',
					'<div class="content">{{content}}</div>',
					'</div>'
				].join('');
				this.$container = $('#j-container');
				this.getData = function() {
					var formData = {
						pageIndex: 1,
						total: 0,
						size: 20
					};

					return $.getJSON('/mobi/queryEmail.aspx', formData, function(data) {
						if(data.success) {
							that.$container.html(that.buildHtml(data.data.records));
							that.eventHandle();
						}
					});
				};
				this.buildHtml = function(data) {
					var htmlArr = [];
					for(var i = 0; i < data.length; i++) {
						var obj = data[i];
						htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm, function($0, $1) {
							if(obj) {
								if($1 === 'isRead') {
									return obj[$1] === true ?'' : ' noread';
								}
								return obj[$1] || '';
							}
							return '';
						}));
					}
					return htmlArr.join('');
				};
				this.eventHandle = function() {
					$(".layout-item .title").click(function() {
						var $that = $(this);
						var $parent = $that.closest('.layout-item');
						var $content = $parent.find('.content');
						
						if($parent.hasClass('active')) {
							$content.slideUp();
							$parent.removeClass('active');
						} else {
							$content.slideDown();
							$parent.addClass('active');
							if(!$parent.hasClass('load')) {
								$parent.removeClass("noread")
								mobileManage.getUserManage().readEmail({
									emailId: $parent.data('id')
								}, function(result) {
									if(result.success) {
										$content.html(result.data.content.replace(/\r\n/g, '<br>'));
										$parent.addClass('load read');
										$('#email-unreadCount').html(result.data.unreadCount);
									}
								});
							}
						}

					});
				};
				this.getData();
			}
			new EmailPage();
			$('#letter-content').keyup(function() {
				$('#content-count').text(255 - $(this).val().length)
			})
			/* L4 站内信 发信
			 ========================= */
			function saveLetter() {
				var title = $("#letter-title").val();
				var content = $("#letter-content").val();

				if(title == "") {
					alert("标题不能为空！");
					return false;
				}

				if(title != "" && title.length > 25) {
					alert("标题过长！");
					return false;
				}

				if(content == "") {
					alert("回复信息不能为空！");
					return false;
				}

				if(content != "" && content.length > 255) {
					alert("回复信息过长！");
					return false;
				}
				$.ajax({
					url: "/asp/saveBookDate.aspx",
					type: "POST",
					cache: false,
					data: "guestbook.title=" + title + "&guestbook.content=" + content,
					complete: function(data) {
						if("200" == data.status) {
							$("#letter-title, #letter-content").val("");
							alert("站内信已发送！");
							letterService(1);
						} else {
							alert("无内容返回！")
						}
					}
				});
				return true;
			}
		</script>
	</body>

</html>