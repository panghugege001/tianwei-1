<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp"></jsp:include>
		<link rel="stylesheet" href="/mobile/css/loader.css">
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>

	<body>
		<div class="header">
			<a class="left-btn" href="/mobile/app/slotGame.jsp">
				<i class="iconfont icon-arrow-left"></i>返回
			</a>
			历史记录
			<a class="server-btn" id="edit-btn">编辑</a>
		</div>
		<div class="slot_game_item_list " id="j-gameContainer">

		</div>

		<div class="operate" id='operate'>
			<a id="j-all" href="javascript:;" class="btn btn07">全选</a>
			<a id="j-delete" href="javascript:;" class="btn btn06">删除</a>
		</div>
		<input id="j-baseUrl" type="hidden" value="<%=request.getRequestURL().toString().replace(request.getServletPath(), " ") %>">
		<input id="j-ptToken" type="hidden" value="">
		<input id="j-ntToken" type="hidden" value="${session.nt_session}">
		<input id="j-dtToken" type="hidden" value="${session.slotKey}">
		<input id="j-dtGameurl" type="hidden" value="${session.gameurl}">
		<input id="j-referWebsite" type="hidden" value="${session.referWebsite}">
		<input id="j-username" type="hidden" value="">
		<input id="j-isLogin" type="hidden" value="${session.customer!=null}">
		<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1202"></script>
		<script src="/mobile/js/Loader.js?v=5"></script>
		<script src="/mobile/app/js/jquery.lazyload-v1.9.1.min.js"></script>
		<script src="/mobile/js/new/game.js?v=25"></script>
		<script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>
		<script>
			$(function() {
				var history_data = localStorage.getItem('history');
				var now_time = new Date().getTime() - 1000 * 3600 * 24 * 7;
				history_data = JSON.parse(history_data) || []
				var seven = [],
					after = [];
				$.each(history_data, function(i, item) {
					if(item.cktime > now_time) { //最近一周
						seven.push(item)
					} else {
						after.push(item)
					}
				})
				SlotMg.GameContainer.show()
				if(seven.length > 0) {
					SlotMg.GameContainer.append('<h3 class="section-title" style="clear: both;"><span>最近一周</span></h3>');
					SlotMg.builHtml(seven, true);
				}
				if(after.length > 0) {
					SlotMg.GameContainer.append('<h3 class="section-title"  style="clear: both;"><span>更早</span></h3>');
					SlotMg.builHtml(after, true);
				}

				SlotMg.GameContainer.on("click", ".slot_game_item .layout_image_hover_text", function() {
					var icon = $('i', this);
					if(icon.hasClass("active")) {
						icon.removeClass("active")
					} else {
						icon.addClass("active")
					}
				})
				$("#j-all").click(function() {
					$('.slot_game_item i').addClass('active')
				})

				$("#j-delete").click(function() {
					var newdata =[];
					$('.slot_game_item i.active').each(function(i, dom) {
						var pardom = $(dom).closest('.slot_game_item')
						item_data = pardom.data('json');
						for (var i = 0; i < history_data.length; i++) {
							if(history_data[i].cktime === item_data.cktime) {
								history_data.splice(i,1);
								$(pardom).remove();
								break;
							}
						}
					})
					localStorage.setItem('history', JSON.stringify(history_data));
					$("#edit-btn").click();
				})

				var edit = false;
				$("#edit-btn").click(function() {
					edit = !edit;
					if(edit) {
						$(".slot_game_item i,#operate").show();
					} else {
						$('.slot_game_item i').removeClass('active')
						$(".slot_game_item i,#operate").hide();
					}
					$(this).text((!edit ? "编辑" : "取消"))
				})
			})
		</script>
		<style>
			.slot_game_item_list .section-title {
				margin: 0 -0.333333rem 0.333333rem;
			}
		</style>
	</body>

</html>