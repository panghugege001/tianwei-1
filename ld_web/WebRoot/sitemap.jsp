<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0">
  <link rel="stylesheet" href="/css/sitemap.css?v=9">
  <link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_226486_fqtbqjfka66iggb9.css">
</head>
<body class="user_body">
	<div class="">
		<div id="header" class="gb-header">
			<div class="header-top-wp">
				<div class="container">
					<div class="link-info fl">
						<a href="/" class="item">< &nbsp返回首页</a>
					</div>
					<ul class="fr link-info">
						<li class="item"><a class="i-btn" href="${ctx}/promotion.jsp" target="_blank">最新优惠</a></li>
						<li class="item"><a class="i-btn" data-toggle="modal" data-target="#modal-license" href="javascript:void(0);" >牌照展示</a></li>
<!--						<li class="item"><a class="i-btn one" href="javascript:alert('敬请期待')">天威公会</a></li>-->
					</ul>
				</div>
			</div>
			<div class="header-pc" id="header">
				<div class="left-btn">
					<div id="back-btn"><a href="/"><i class="iconfont icon-arrow-left"></i></a></div>
				</div>
				<span id="h-title">线路测试</span>
			</div>
			<div class="user_center"></div>
		</div>
		<div class="container">
			<div class="vip-box user-profile">
				<div class="pz_box">
					<img src="images/qr/xianlutest.png" />
				</div>
				<a class="resft"  onclick="myrefresh()">刷新</a>
				<div id="ceshu">
					<ul class="ul-test">
						<li><a href="http://www.tianwei4.com/" target="_blank"><b class="b1">01</b><br />线路</a><p>www.tianwei4.com</p></li> 
						<li><a href="http://www.tianwei5.com/" target="_blank"><b class="b1">02</b><br />线路</a><p>www.tianwei5.com</p></li>
						<li><a href="http://www.tianwei6.com/" target="_blank"><b class="b1">03</b><br />线路</a><p>www.tianwei6.com</p></li>
						<li><a href="http://www.tianwei7.com/" target="_blank"><b class="b1">04</b><br />线路</a><p>www.tianwei7.com</p></li>
						<li><a href="http://www.tianwei8.com/" target="_blank"><b class="b1">05</b><br />线路</a><p>www.tianwei8.com</p></li>
						<li><a href="http://www.tianwei9.com/" target="_blank"><b class="b1">06</b><br />线路</a><p>www.tianwei9.com</p></li>
					</ul>
				</div>
				<div class="left_tsxt">
					上方数值反应时间越小，网站速度越快，请按 <span class="r_yanse">Ctrl+D</span> 收藏，点击刷新即可查看新数<i class="shuaxin" onclick="myrefresh()"></i>
				</div>
				<div class="tc tc_btn">
					<a style="display: inline-block;" href="/down/dns.rar" >DNS 修复</a><a href="/down/PT_VPN_v1.1.exe" class="btn-nav" >加速器</a>
				</div>
			</div>
		</div>
		<div class="container container_bj">
				<div class="gb-sidenav">
					<ul>
						<li class="action">手机APP下载</li>
						<li>DT苹果客户端</li>
						<li>DT安卓客户端</li>
						<li>AG捕鱼客户端</li>
						<li>PT客户端</li>
						<li>MW客户端</li>
					</ul>
				</div>
				<div class="r_rigth_box">
						<div class="xiazaiimg">
								<img src="images/qr/sitem_bj.jpg" />
						</div>
						<div class="xiazaitext">
							<p>
								<span>方法1：</span>使用浏览器打开tianwei4.com下载安装享受手机客户端游戏。
							</p>
							<p style="margin-top: 30px;">
								<span>方法2：</span>扫码工具扫码下载。
							</p>
						</div>
						<div class="erweima">
								<div class="appxiazai xiazai">
									<div class="w_220">
											<img src="/images/appxiazai/longduapp.png" />
											<span>苹果客户端</span>
									</div>
									<div class="w_220">
											<img src="/images/appxiazai/longduapp.png" />
											<span>安卓客户端</span>
									</div>									
								</div>
								<div class="appxiazai2 xiazai" style="display: none;">
									<div class="w_220">
											<img src="/images/qr/dt-ios.png" />
											<span>DT苹果客户端</span>
									</div>								
								</div>
								<div class="appxiazai2 xiazai" style="display: none;">
									<div class="w_220">
											<img src="/images/qr/dt-app.png" />
											<span>DT安卓客户端</span>
									</div>								
								</div>		
								<div class="appxiazai2 xiazai" style="display: none;">
									<div class="w_220">
											<img src="/images/qr/ag-ewm.png" />
											<span>AG捕鱼客户端</span>
									</div>								
								</div>		
								<div class="appxiazai2 xiazai" style="display: none;">
									<div class="w_220">
											<img src="/images/qr/pt-ewm.png" />
											<span>PT客户端</span>
									</div>								
								</div>
								<div class="appxiazai2 xiazai" style="display: none;">
									<div class="w_220">
											<img src="/images/appxiazai/MW.jpg" />
											<span>MW客户端</span>
									</div>								
								</div>									
						</div>
				</div>
		</div>
		
	</div>
	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	<!--手动修复本地DNS{-->
	<div class="modal fade" id="modal-dns" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
	  <div class="modal-dialog lg mt10" role="document">
	    <div class="modal-content" style="height: 700px;overflow: auto;">
	      <div class="modal-hd">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">手动修复本地DNS</h4>
	      </div>
	      <div class="modal-bd m-content">
	          <p class="f20 mb20">手动修复本地DNS</p>
	          <p>第一步：点击左下角的开始，打开控制面板，查看方式为类别，进入网络和 Internet —— 网络和共享中心</p>
	          <img class="mb20" src="/images/sitemap/help/1/1.jpg" alt="">
	          <img class="mb20" src="/images/sitemap/help/1/1-2.jpg" alt="">
	          <p>第二步：点击更改适配器设置</p>
	          <img class="mb20" src="/images/sitemap/help/1/2.jpg" alt="">
	          <p>第三步：右键点击当前活动的网络连接，点弹出菜单中的属性。</p>
	          <img class="mb20" src="/images/sitemap/help/1/3.jpg" alt="">
	          <p>第四部：选中 Internet 协议版本 4（TCP/IPv4），再点属性</p>
	          <img class="mb20" src="/images/sitemap/help/1/4.jpg" alt="">
	          <img class="mb20" src="/images/sitemap/help/1/4-2.jpg" alt="">
	          <p>第五步：选中使用下面的 DNS 服务器地址，填写首选 DNS 服务器和备用 DNS 服务器（推荐 8.8.8.8 或者 8.8.4.4），然后点击确定。</p>
	      </div>
	    </div>
	  </div>
	</div>
<!--}手动修复本地DNS-->
	<script type="text/javascript" >
		$(function(){
			 test();
			 
			 $(".gb-sidenav ul li").click(function(){
			 	 $(".gb-sidenav ul li").removeClass("action");
			 	 $(this).addClass("action")
			 	 $(".erweima").find(".xiazai").hide();
			 	 $(".erweima").find(".xiazai").eq($(this).index()).show();
			 })
			 
			 
		})
		function test() {
			var span, d = document, li, a, i, lis = d.getElementById('ceshu').getElementsByTagName('li');
			for (i = 0; li = lis[i++];) {
				a = li.getElementsByTagName('a')[0];
				if (!a) { continue; }
				span = d.createElement('span');
				span.ctime = new Date();
				span.innerHTML = '测速中...<img src="' + a.href + '" border="0" width="1" height="1" onerror="testresult(this)" />';
				li.appendChild(span);
			}
		};

		var iconIndex = 0;

		function testresult(img) {
			var span = img.parentNode;
			//  start
			var li = span.parentNode;
			var a = li.getElementsByTagName('a')[0];
			a.style.backgroundImage = 'url(/images/qr/qiuqiu' +'.png)';
			//  end
			var n = 'em';
			if (!testresult.isrun) { n = 'b id="this"'; testresult.isrun = true; }
			span.innerHTML = '<' + n + '>响应' + ((new Date() - span.ctime) / 1000).toFixed(2) + '秒</' + n + '>';
			if( document.getElementById('this')){
				 $(".ul-test li").addClass("aaa");
			}else{
				 $(".ul-test li").removeClass("aaa"); 
			}
		};
		
		function myrefresh()
		{
		   window.location.reload();
		}		
	</script>
	
</body> 
</html>