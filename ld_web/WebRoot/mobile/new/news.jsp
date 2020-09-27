<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE >
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="公告中心" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css" />
	</head>
	<body>
		<div id="j-news" class="news-lists">
			<div class="item">
				<!--<div class="title">
					<i class="iconfont icon-arrow-next"></i>
					<div class="text">{{title}}</div>
					<div class="time"> {{time}} <i class="fr iconfont icon-downjiantou"></i></div>
				</div>
				<div class="content">{{content}}</div>-->
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			var fp_bbout_element_id = 'fpBB';
			var io_bbout_element_id = 'ioBB';
			var io_install_stm = false;
			var io_exclude_stm = 12;
			var io_install_flash = false;
			var io_enable_rip = true;
			$(document).ready(function() {
				var cpuVal = $("#cpuid").val()
				if(typeof(cpuVal) == "undefined" || cpuVal == null || '' == cpuVal || cpuVal == 'null' || !cpuVal || cpuVal.length < 1) {
					var ioBBVal = $("#ioBB").val();
					$.post("${ctx}/asp/addcpuid.aspx", {
						"cpuid": ioBBVal
					}, function(returnedData, status) {
						if("success" == status) {}
					});
				}
			});
		</script>
		<script type="text/javascript">
			function NewsPage() {
				var that = this;
				that.$container = $('#j-news');
				that.tpl = [
					'<div class="item">',
					'	<div class="title">',
					'		<i class="iconfont icon-arrow-next"></i>',
					'		<div class="text">{{title}}</div>',
					'		<div class="time"> {{time}} <i class="fr iconfont icon-downjiantou"></i></div>',
					'	</div>',
					'	<div class="content">{{content}}</div>',
					'</div>',
				].join('');
				that.getData = function() {
					$.getJSON('/mobi/getAllNews.aspx', function(data) {
						if(data.success) {
							that.$container.html(that.buildHtml(data.data));
							that.eventHandle();
						}
					});
				};
				that.buildHtml = function(data) {
					var htmlArr = [];
					for(var i = 0; i < data.length; i++) {
						var obj = data[i];
						htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm, function($0, $1) {
							return obj && obj[$1] || '';
						}));
					}
					return htmlArr.join('');
				};
				that.eventHandle = function() {
					that.$container.find(".title").click(function() {
						var $that = $(this);
						var $parent = $that.closest('.item');
						var $content = $parent.find('.content');
						if($parent.hasClass('active')) {
							$content.slideUp();
							$parent.removeClass('active');
						} else {
							$content.slideDown();
							$parent.addClass('active');
						}
					});
				};
				that.getData();
			}
			new NewsPage();
		</script>
	</body>

</html>