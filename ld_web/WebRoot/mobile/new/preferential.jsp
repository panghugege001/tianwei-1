<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE >
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="最新优惠" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/index.css?v=1000" />
	</head>

	<body>
		<div id="page-index">
			<ul class="preferential-nav" id="j-prom-nav">
				<li class="active">
					<a data-type="LON" href="javascript:;">全部</a>
				</li>
				<!-- <li>
					<a data-type="PHO" href="javascript:;">手机</a>
				</li>
				<li>
					<a data-type="LON" href="javascript:;">长期</a>
				</li> -->
				<li>
					<a data-type="LIM" href="javascript:;">限时</a>
				</li>
				<!-- <li>
					<a data-type="TOP" href="javascript:;">免费</a>
				</li> -->
				<li>
					<a data-type="OVER" href="javascript:;">过期</a>
				</li>
			</ul>
			<div id="j-container" class="preferential-lists">
				<!--<div class="item">
					<img src="" alt="" />
					<div class="txt"></div>
				</div>-->
			</div>
		</div>

		<div id="page-detail" class="preferential-etail">
			<div class="content-close">
				<a class="back-action">X</a>
			</div>
			<div class="j-content">
			</div>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript">
			function createPages() {
				var that = this;
				that.tpl = [
					'<a class="item" data-id="{{id}}" data-type="{{type}}" data-url="{{url}}" href="{{link}}">',
					'	<img src="{{image}}" alt="" />',
					'	<div class="txt">{{startDate}}{{endDate}}</div>',
					'</a>'
				].join('');
				that.$container = $('#j-container');
				that.$navBtn = $('#j-prom-nav a');
				that.$pageIndex = $('#page-index');
				that.$pageDetail = $('#page-detail');

				this.getData = function() {
					return $.getJSON('/data/promotion/promotion_new.json?v=0136');
				};
				this.buildHtml = function(data) {
					var htmlArr = [];
					for(var i = 0; i < data.length; i++) {
						var obj = data[i];
						htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm, function($0, $1) {
							if(obj && $1 === 'link') {
								return obj['link'] || 'javascript:;';
							}
							if($1 === 'startDate' && obj[$1] != '') {
								return '优惠时间&nbsp;&nbsp;&nbsp;' + obj[$1] + '至'
							}
							if($1 === 'endDate' && obj["startDate"] == '') {
								return obj['title'];
							}
							return obj[$1] || '';
						}));
					}
					return htmlArr.join('');
				};

				this.eventHandle = function() {
					that.$navBtn.click(function() {
						var type = $(this).data('type');
						$(this).closest('li').addClass('active').siblings().removeClass('active');
						if(type) {
							that.$container.find('a.item').hide();
							that.$container.find('a.item[data-type*="' + type + '"]').css('display', 'block');
						} else {
							that.$container.find('a.item[data-type*="' + type + '"]').css('display', 'block');
						}
					});
					that.$container.on('click', 'a.item', function() {
						var url = $(this).data('url');
						if(url) {
							that.$pageIndex.hide();
							that.$pageDetail.show().find('.j-content').html('<div class="text-center">加载中...</div>');
							$.get(url, function(data) {
								that.$pageDetail.find('.j-content').html('<div class="promotion-content">' + data + '</div>');
							});
							return false;
						}
					});

					$(".preferential-etail .content-close").click(function() {
						that.$pageIndex.show();
						that.$pageDetail.hide()
					})
				};
				this.init = function() {
					that.getData().done(function(data) {
						that.$container.html(that.buildHtml(data.data));
						that.eventHandle();
						setTimeout(function() {
							var $url = window.location.search.trim();

							if($url) {
								$($url.replace("?", "[data-id=") + "]").click();
							}
						})
					});
				};
				this.init();
			}
			new createPages();
		</script>
		<script>
		</script>
	</body>

</html>