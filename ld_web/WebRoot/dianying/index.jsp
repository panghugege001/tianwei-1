<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>游天威免费看大片</title>
		<script type="text/javascript" src="js/jquery-1.11.1.min.js" ></script>
		<script type="text/javascript" src="js/bootstrap.min.js" ></script>
		<link rel="stylesheet" href="css/bootstrap.min.css?v=1" />
		<link rel="stylesheet" href="css/index.css?v=1" />  
			<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    </head>
	<body class="center_body">
		<div class="header">
			<div class="container img_boxcenter">
				<div class="img_box">
					<span class="l_73"><a href="/index.jsp" style="display: inline-block; width: 100%; height: 100%;"><img src="img/logo.png"></a></span>
					<ul class="fr link-info">
						<li class="item"><button class="i-btn one" data-toggle="modal" data-target="#modal-login">登录</button></li>
						<li class="item"><button class="i-btn one" data-toggle="modal" data-target="#modal-reg">免费注册</button></li>
						<li class="item">
							<a class="i-btn" href="/index.jsp">返回首页</a>
						</li>
					</ul>								
				</div>				
			</div>
		</div>
		
		<div class="body_box">
		<div class="bannar_box"></div> 
		<div class="container padding_5">
			<div class="title">
				<i><img src="img/dianying.png"></i>
			</div>
			<div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
			  <!-- Indicators -->
			  <ol class="carousel-indicators">
			    <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
			    <li data-target="#carousel-example-generic" data-slide-to="1"></li>
			  </ol>
			
			  <!-- Wrapper for slides -->
			  <div class="carousel-inner TV_box" role="listbox">
			    <div class="item active">
			      <ul>
			      	<li>
						<span><img src="img/list1.jpg" alt="..."></span>      		
			      	</li>
			      	<li>
						<span><img src="img/list2.png" alt="..."></span>
						<span><img src="img/list3.png"></span>
			      	</li>
			      	<li>
						<span><img src="img/list4.png" alt="..."></span>  
						<span><img src="img/list5.png" alt="..."></span> 			
			      	</li>      	
			      </ul>
			
			    </div>
			    <div class="item">
			      <ul>
			      	<li>
						<span><img src="img/list1.jpg" alt="..."></span>      		
			      	</li>
			      	<li>
						<span><img src="img/list2.png" alt="..."></span>
						<span><img src="img/list3.png"></span>
			      	</li>
			      	<li>
						<span><img src="img/list4.png" alt="..."></span>  
						<span><img src="img/list5.png" alt="..."></span> 			
			      	</li>      	
			      </ul>
			    </div>
			  </div>
			
			  <!-- Controls -->
			  <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
			    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
			    <span class="sr-only">Previous</span>
			  </a>
			  <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
			    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			    <span class="sr-only">Next</span>
			  </a>
			</div>
		</div>
		<div class="book_box">
			<div class="container padding_10">
			<div class="title">
				<i><img src="img/xiaoshuo.png"></i>
			</div>				
				<div id="carousel-example-generic2" class="carousel slide" data-ride="carousel">				
				  <!-- Wrapper for slides -->
				  <div class="carousel-inner Book" role="listbox">
				    <div class="item active">
						<ul>
							<li>
								<img src="img/book1.png">
								<h3 class="title_span">灭秦</h3>
								<p>
									作者：龙人 播者：旭东 大陆玄幻武侠第一人——龙人著...
								</p>
							</li>
							<li>
								<img src="img/book2.png">
								<h3 class="title_span">十娘画骨香</h3>
								<p>小白领遇车祸，居然穿越</p>
							</li>
							<li>
								<img src="img/book3.jpg">
								<h3 class="title_span">你的青春有几天</h3>
								<p>播者：紫宸 本书共17回。 这本书集结了作者...</p>
							</li>
							<li>
								<img src="img/bokk4.jpg">
								<h3 class="title_span">所向披靡</h3>
								<p>作者：郭小贝 世上原本是没有恶人的，是这个世道把...</p>
							</li>
						</ul>
				    </div>
				    <div class="item">
						<ul>
							<li>
								<img src="img/book1.png">
								<h3 class="title_span">灭秦</h3>
								<p>
									作者：龙人 播者：旭东 大陆玄幻武侠第一人——龙人著...
								</p>
							</li>
							<li>
								<img src="img/book2.png">
								<h3 class="title_span">十娘画骨香</h3>
								<p>小白领遇车祸，居然穿越</p>
							</li>
							<li>
								<img src="img/book3.jpg">
								<h3 class="title_span">你的青春有几天</h3>
								<p>播者：紫宸 本书共17回。 这本书集结了作者...</p>
							</li>
							<li>
								<img src="img/bokk4.jpg">
								<h3 class="title_span">所向披靡</h3>
								<p>作者：郭小贝 世上原本是没有恶人的，是这个世道把...</p>
							</li>
						</ul>
				    </div>
				  </div>
				
				  <!-- Controls -->
				  <a class="left carousel-control bookleft" href="#carousel-example-generic2" role="button" data-slide="prev">
				    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
				    <span class="sr-only">Previous</span>
				  </a>
				  <a class="right carousel-control bookrigth" href="#carousel-example-generic2" role="button" data-slide="next">
				    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
				    <span class="sr-only">Next</span>
				  </a>
				</div>				
			</div>
		</div>
		<div class="container padding_50">
			<h1>周周存款周周送，每周限量100名</h1>
			<div class="cunkuang">
				<ul>
					<li>
						<div class="cunkuang_top clor_1">爱奇艺</div>
						<div class="TV_text">
							<span>免费看电影</span>
							<p>一个月</p>
						</div>
					</li>
					<li>
						<div class="cunkuang_top clor_2">优酷</div>
						<div class="TV_text">
							<span>免费看电影</span>
							<p>一个月</p>
						</div>
					</li>
					<li>
						<div class="cunkuang_top clor_3">起点中文网</div>
						<div class="TV_text">
							<label>1000点数</label>
						</div>
					</li>
					<li>
						<div class="cunkuang_top clor_4">晋江文学城</div>
						<div class="TV_text">
							<label>1000点数</label>
						</div>
					</li>					
				</ul>
			</div>
			<div class="foot_text">
				<p>
					1.周存款计算时间为：周一00:00:00~周日23:59:59<br />                    
					2.当周存款达到1000元，请联系在线客服，提供相应视频或小说网站的【帐号】及【密码】给在线客服，次日18点前为您充值完毕。    <br />                 
					3.达到存款要求的玩家，任挑选一项视频网站或小说网站充值。         <br />            
					4.此优惠活动同姓名、同电话、同银行卡、同IP地址的会员只能享受一次。<br />                     
					5.若个人原因导致视频网站帐号遭禁用，天威不予二次充值。      <br />               
					6.天威保留对本次活动的修改，修订和最终解释权，以及在无通知情况下修改本次活动的权力。				
				</p>				
			</div>
		</div>
			<div class="footer_box">友情提示：博彩有风险，量力乐其中 Copyright © 2016 天威 All Rights Reserved</div>		
			</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>			
	</body>
</html>