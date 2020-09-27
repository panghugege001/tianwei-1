<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="/css/phone.css?v=4">
</head>
<body>

<div class="index-bg">
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
  <div class="container">
  <div class="h415 text-center">
	<img src="${ctx}/images/phone/tit.png" class="a-bounce" /><img src="${ctx}/images/phone/tit1.png" class="mt30 a-bounceinB" />
  </div>
  <div class="cl"></div>
  <div class="about-main phone-main">
   <div class="gb-sidenav">
		<ul class="navlist" id="j-aboutusMenu">
		  <li class="active"><a data-toggle="tab" href="#tab-app">官网手机APP</a></li>	
		  <li><a data-toggle="tab" href="#tab-pt">PT客户端</a></li>
		  <li><a data-toggle="tab" href="#tab-dt">DT客户端</a></li>  
		  <li><a data-toggle="tab" href="#tab-ag">AG真人</a></li>
		</ul>
	</div>
    <div class="gb-main-r tab-bd">
		<div id="tab-app" class="tab-panel active">
			<h4>官网手机APP</h4>
			<div class="m-content">
				<h5>下载方式1（支持苹果、安卓系统手机）</h5>
				<p>使用手机二维码扫描软件扫码下载</p>
				<div class="w147">
					<img src="${ctx}/images/qr/ld-app.png" width="134" height="130">
				</div>
				<h5 class="mt50">下载方式2</h5>
				<p>使用手机浏览器输入网址即可进行下载</p>
				<span class="net">www.tianwei4.com</span>
				 
				<img src="${ctx}/images/phone/app-left.jpg" class="pic">
			</div>
		 
		</div>
		<div id="tab-pt" class="tab-panel">
			<h4>PT客户端</h4>
			<div class="m-content">
				<h5>下载方式1（安卓系统）</h5>
				<p>使用手机二维码扫描软件扫码下载</p>
				<div class="w147">
					<img src="${ctx}/images/qr/pt-ewm.png?v=1" width="134" height="130">
				</div>
				<h5 class="mt50">下载方式2</h5>
				<p>安卓手机浏览器输入网址</p>
				<span class="net">https://www.duofa1.com/web/pt/ptNewClient.apk</span>
				<h5 class="mt50">PT电脑客户端下载</h5>
				<p>点击下载按钮下载电脑客户端</p>
				<a href="http://cdn.sunrise8888.com/longdu/setupglx.exe" style="display: inline-block; width: 120px; height: 38px; line-height: 38px; background: #ee6325; color: #FFFFFF; text-align: center; cursor: pointer; border-radius: 10px;">点击下载</a>
				<p style="margin-top:50px;">注：	<p>	
				<p>1、安卓手机: 手机使用谷歌浏览器进入官网之后即可以看到PT老虎机，亦可下载PT客户端进行游戏。</p>
				<p>2、苹果手机：打开手机谷歌浏览器或自带Safari浏览器输入天威官网，登陆游戏账号可以查看到PT老虎机。</p>
				<p>3、请勿使用微信或QQ扫描进行下载。</p>
				<p>4、如果打开客户端时，遇到提示需要更新，请按步骤进行更新操作。</p>
				<img src="${ctx}/images/phone/pt-right.jpg" class="pic">
			</div>
		 
		</div>
		<div id="tab-dt" class="tab-panel">
			<h4>DT客户端</h4>
			<div class="m-content">
				<h5>下载方式1（安卓系统）</h5>
				<p>使用手机二维码扫描软件扫码下载</p>
				<div class="ewm">
					<span><img src="/images/qr/dt-ios.png" width="134" height="130"></span>
					<p>苹果手机</p>
				</div>
				<div class="ewm" style="margin-left:50px;"> 
					<span><img src="/images/qr/dt-app.png" width="134" height="130"></span>
					<p>安卓手机</p> 
				</div>	
				<p style="margin-top:50px;">注：	<p>	
				<p>安卓下载“扫码神器”扫码下载（使用微信扫码无法下载）</p>
				<h5 class="mt50">下载方式2（支持苹果、安卓系统手机）</h5>
				<p>【一】安卓手机：使用手机浏览器输入网址，即可进行下载。或下载到电脑，使用数据线传输到手机安装.</p>
				<span class="net">http://down.dreamtech.asia/LONGDU/android.html</span>
				<p style="margin-top:50px;">【二】苹果手机：使用手机浏览器输入网址 即可进行下载<p>	
				<span class="net">http://down.dreamtech.asia/LONGDU/ios.html</span> 
				<img src="${ctx}/images/phone/dt-right.jpg" class="pic">
			</div>
		 
		</div>
		<div id="tab-ag" class="tab-panel">
			<h4>AG捕鱼</h4>
			<div class="m-content">
				<h5>下载方式（支持苹果、安卓系统手机）</h5>  
				<p>手机浏览器输入网址下载客户端</p>
				<span class="net">   http://agmbet.com</span>
				<p>使用手机二维码扫描软件扫码下载</p>
				<div style="width: 147px; height: 147px;">
					<img src="images/phone/agzr.jpg" style="width: 100%;">
				</div>
				<p style="margin-top:50px;">温馨提示：<p>	
				<p>若忘记AG手机客户端登录密码请在电脑上登陆AG国际厅，<br />点击左下"立即体验"，重新扫码即可</p>
				<img src="${ctx}/images/phone/ag-right.jpg" class="pic">
			</div>
		 
		</div>	
	</div> 
  
  
  </div>
   
  </div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script>
  $(function(){
    var  $tabAboutUs=$('#j-aboutusMenu');
    //关于我们标签选中
    $tabAboutUs.length && $tabAboutUs.find('a[href="'+window.location.hash+'"]').tab('show');
  });
</script>

</body>
</html>