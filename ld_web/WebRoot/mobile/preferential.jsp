<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE >
<html>
	<head>
		<jsp:include page="commons/common.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/preferential.css?v=10" />
	</head> 
	<body>
		<div class="tab-bd">
		<div class="main-wrap">
			<div class="header-margin"></div>
			<div id="page-index" data-page-index="" class="content tab-panel active">
				<div class="pt40"></div>
				 <ul class="nav-prom-list tab-block" id="j-prom-nav">
				 	<li class="active tab-button"><a data-type="" href="mobile/preferential.jsp">全部</a></li>
					<li class="tab-button"><a data-type="PHO" href="javascript:;">手机</a></li>
					<li class="tab-button"><a data-type="LON" href="javascript:;">长期</a></li>
					<li class="tab-button"><a data-type="LIM" href="javascript:;">限时</a></li>
					<li class="tab-button"><a data-type="TOP" href="javascript:;">免费彩金</a></li>
				</ul>
				<div class="space-2"></div>
				<div id="j-container" class="page-boxs">

				 </div>
			</div>
			<div id="page-detail" class="tab-panel">
				<div class="page-inner">
					<div class="space-2"></div>
					<div class="content-close"> <a class="back-action" data-toggle="tab" href="#page-index">X</a></div>
					<div class="j-content">

					</div>
				</div>

			</div>
			<div class="footer-margin"></div>
		</div>
		</div>

		<jsp:include page="commons/footer.jsp" />
		<script type="text/javascript">

			headerBar.setTitle('最新优惠');
 
			headerBar.scrollHide(true);
			headerBar.bind('hide',function(){
				$('.main-wrap .tab-block').css('top',0);
			});
			headerBar.bind('show',function(){
				$('.main-wrap .tab-block').css('top',headerBar.defHeight);
			});
			
			 $(function(){
				new createPages();
			});

			
			function createPages(){
				var that=this;
				this.tpl=[
					'<div class="mb40 promotion-info page show" data-type="{{type}}">',
					'<a href="{{link}}" data-url="{{url}}" id="{{id}}"><img src="{{image}}"  class="w" alt=""><div class="text">{{title}}</div></a>',
					'</div>'
				].join('');

				this.$container=$('#j-container');
				 this.$navBtn=$('#j-prom-nav').find('a'); 
				  this.$pageIndex=$('#page-index');
				this.$pageDetail=$('#page-detail');

				this.getData=function () {  
					return $.getJSON('/data/promotion/promotion.json?v=0132');   
				};
				this.buildHtml=function(data){
					var htmlArr=[];
					for (var i = 0; i < data.length; i++) {
						var obj=data[i];
						htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
							if(obj&&$1==='link'){
								return obj['link']||'javascript:;';
							}
							return obj&&obj[$1]||'';
						}));
					}
					return htmlArr.join('');
				};

				this.eventHandle=function () {
					that.$navBtn.click(function () {
						var type=$(this).data('type');
						$(this).closest('li').addClass('active').siblings().removeClass('active');

						if(type){
							$('.promotion-info').hide();
							$('.promotion-info[data-type*="'+type+'"]').css('display','block');
						}else{
							$('.promotion-info[data-type*="'+type+'"]').css('display','block');
						}
						 

					});

					$(document).on('click','.promotion-info a',function () {
						var url= $(this).data('url');
						var imgUrl=$(this).find('img').attr('src');
						that.$pageIndex.removeClass('active');
						that.$pageDetail.addClass('active').find('.j-content').html('<div class="text-center">加载中...</div>');
						if(url){
							$.get(url,function (data) {
								 
								that.$pageDetail.find('.j-content').html('<div class="promotion-content">'+data+'</div>');
							});
							return false;
						}
					});
					 
				};
				this.init=function () {
					var def=that.getData();

					def.done(function (data) {
						that.$container.html(that.buildHtml(data.data));
						that.eventHandle();
                        setTimeout(function(){
                            var url=window.location.search;
                            console.log(url);
                            console.log(url.replace("?",'#'),$(url.replace("?",'#')).html())
                            $(url.replace("?",'#')).click()
                        })
					});
				};
				this.init();
			}
		</script>
	</body>
</html>