<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="/css/sitemap.css?v=9">
  <style>
  	.xiazaitext{
  		text-align: center;
  		width: 100%;
  	}
  	.appxiazai{
  		width: 100%;
  	}
  	.c-huangse{
  		color: #dfa85a;
  	}
  	.app_footer{
    width: 100%;
    float: left;
    margin-top: 30px;
    margin-bottom: 30px;  		
  	}
  	
  	.app_footer p{
  		line-height: 30px;
  	}
  	.input_box input{
	    width: 240px;
	    line-height: 35px;
	    text-align: center;  		
  	}
  	.appxiazai2{
  		width: 100%;
  	}
  	.r_rigth_box{
  		height: auto;
  	}
  	.appxiazai2 .xiazaitext{
  		width: 100%;
  	}
  	.xiazaitext ul li{
  		float: left;
  		width:49%;
  		padding: 30px;
  	}
  		.xiazaitext ul li .w_50{
  			width: 50%;
  			padding: 20px;
  			float: left;
  		}
  		.xiazaitext ul li .w_50 img{
	    width: 135px;
	    height: 135px;  			
  		}
  		.xiazaitext ul li .w_50 em{
  				display: inline-block;
  		}
  	.xiazaitext ul li:nth-of-type(1){
     border-right: 1px solid #ccc; 		
  	}
  	.xiazaitext ul li span{
	    color: #dfa85a;
	    font-size: 18px;  		
  	}
  	.xiazaitext ul li input[type="text"]{
    width: 80%;
    height: 35px;	
    text-align: center;
    margin-top: 20px;
  	}
  	.a_appbtn{
    display: inline-block;
    width: 120px;
    height: 38px;
    line-height: 38px;
    background: #dfa85a;
    color: #FFFFFF;
    text-align: center;
    cursor: pointer;
    border-radius: 10px;
    float: left;
    width: 80%;
    margin: 0 auto;
    margin-left: 38px;  		
  	}
  	.span_tow{
    display: inline-block;
    margin-top: 10px;  		
  	}
  	.w_135 >img{ width: 135px; margin-top: 20px;}
  </style>
</head>
<body class="user_body">
	<div class="">
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		<div class="container">
		</div>
		<div class="container container_bj" style="margin-top: 40px;">
				<div class="gb-sidenav">
					<ul>
						<!-- <li class="action">手机APP下载</li> -->
						<li>PT客户端</li>
						<!-- <li>DT客户端</li>
						<li>AG真人</li>
						<li>MW客户端</li> -->
						<li><a href="https://www.google.com/chrome" target="_blank">Chrome浏览器下载</a></li>
						<li><a href="https://get.adobe.com/flashplayer/?loc=cn" target="_blank">Flash插件下载</a></li>
					</ul>
				</div>
				<div class="r_rigth_box">
						<div class="xiazaiimg">
								<img src="images/qr/sitem_bj.jpg" />
						</div>
						<div class="erweima">
								<!-- <div class="appxiazai xiazai">
								<div class="xiazaitext">
									<ul>
										<li>
												<span>下载方式1（支持苹果、安卓系统手机）</span><br /><br />使用手机二维码扫描软件扫码下载
												<div class="w_135"><img src="images/appxiazai/longduapp.png"></div>												
										</li>
										<li>
											<span>下载方式2</span><br /><br />使用手机浏览器输入网址即可进行下载
											<input type="text" value="www.tianwei4.com" />
										</li>
									</ul>
								</div> 																	
								</div>-->
								<div class="appxiazai2 xiazai">
									<div class="xiazaitext">
											<ul>
													<li>
														<span>下载方式1（安卓系统）</span><br /><br />使用手机二维码扫描软件扫码下载
														<div class="w_135"><img src="images/appxiazai/pt-ewm.png"></div>														
													</li>
													<li>
															<span>下载方式2</span><br /><br />安卓手机浏览器输入网址
															<input type="text"  value="https://www.duofa1.com/web/pt/ptNewClient.apk"/>
															<span class="span_tow">PT电脑客户端下载</span><br /> <br />点击下载按钮下载电脑客户端
															<a href="http://cdn.sunrise8888.com/longdu/setupglx.exe" class="a_appbtn">点击下载</a>
													</li>
											</ul>
									</div>
									<div class="app_footer">
										<p>
											<span class="c-huangse">温馨提示</span><br /><br />
											
											1、安卓手机: 手机使用谷歌浏览器进入官网之后即可以看到PT老虎机，亦可下载PT客户端进行游戏。<br />
											
											2、苹果手机：打开手机谷歌浏览器或自带Safari浏览器输入天威官网，登陆游戏账号可以查看到PT老虎机。<br />
											
											3、请勿使用微信或QQ扫描进行下载。<br />
											
											4、如果打开客户端时，遇到提示需要更新，请按步骤进行更新操作。											
										</p>
									</div>
								</div>
								<!-- <div class="appxiazai2 xiazai" style="display: none;">
									<div class="xiazaitext">
										<ul>
												<li>
													<span>下载方式1（安卓系统）</span><br /><br />使用手机二维码扫描软件扫码下载
													<div class="w_50"><img src="images/appxiazai/dt-ios.png"><em>苹果手机</em></div>
													<div class="w_50"><img src="images/appxiazai/dt-app.png"><em>安卓手机</em></div>
													<p class="c-huangse">
														安卓下载“扫码神器”扫码下载（使用微信扫码无法下载）
													</p>
												</li>
												<li>
													<span>下载方式2（支持苹果、安卓系统手机）</span><br /><br />【一】安卓手机：使用手机浏览器输入网址，即可进行下载。或下载到电脑，使用数据线传输到手机安装.
													<input type="text" value="http://down.dreamtech.asia/LONGDU/android.html" />
													<p style="margin-top: 50px;">【二】苹果手机：使用手机浏览器输入网址 即可进行下载</p>
													<input type="text" value="http://down.dreamtech.asia/LONGDU/ios.html" />
												</li>
										</ul>
									</div>
								</div> -->		
								<!-- <div class="appxiazai2 xiazai" style="display: none;">
								<div class="xiazaitext">
									<ul>
										<li>
												<span>下载方式1（支持苹果、安卓系统手机）</span><br /><br />使用手机二维码扫描软件扫码下载
												<div class="w_135"><img src="images/appxiazai/agzr.jpg"></div>												
										</li>
										<li>
											<span>下载方式2</span><br /><br />使用手机浏览器输入网址即可进行下载
											<input type="text" value="http://agmbet.com" />
										</li>
									</ul>
								</div>
									<div class="app_footer">
										<p>
											<span class="c-huangse">温馨提示</span><br /><br />
												若忘记AG手机客户端登录密码请在电脑上登陆AG国际厅，<br />
												点击左下"立即体验"，重新扫码即可<br />							
										</p>
									</div>								
								</div> -->	
								<!-- <div class="appxiazai2 xiazai" style="display: none;">
								<div class="xiazaitext">
									<ul>
										<li>
												<span>下载方式1（支持苹果、安卓系统手机）</span><br /><br />使用手机二维码扫描软件扫码下载
												<div class="w_135"><img style="width: 175px;" src="images/appxiazai/MW.jpg"></div>												
										</li>
										<li>
											<span>下载方式2</span><br /><br />使用手机浏览器输入网址即可进行下载
											<input type="text" value="https://www.666wins.com/as-lobby/mob/qrcode1_cn.html" />
										</li>
									</ul>
								</div>							
								</div> -->								
								<!-- <div class="appxiazai2 xiazai" style="display: none;">
									<div class="w_220">
											<img src="/images/qr/pt-ewm.png" />
											<span>PT客户端</span>
									</div>								
								</div>	 -->								
						</div>
				</div>
		</div>
		
	</div>
	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	<script type="text/javascript" >
		$(function(){
			 $(".gb-sidenav ul li").click(function(){
			 	 $(".gb-sidenav ul li").removeClass("action");
			 	 $(this).addClass("action")
			 	 $(".erweima").find(".xiazai").hide();
			 	 $(".erweima").find(".xiazai").eq($(this).index()).show();
			 })
			 
			 
		})


	</script>
	
</body> 
</html>