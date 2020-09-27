<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/gamelive.css?v=0137" />
		<link rel="stylesheet" href="${ctx}/css/lotterylanding.css?v=12215" />
	</head>

	<body>
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		<!-- <div class="live_bannar"></div> -->
			<div class="live">
				<img src="/images/live/live_AG.png" />
				<!--<div class="overlay">
					<a href="/gameAginRedirect.aspx" class="j-	">进入游戏</a>
				</div>-->
				<div class="btn_foot">
					<ul>
						<li><a class="j-check pc_btn ag_pc_btn" href="/gameAginRedirect.aspx"></a></li>
						<li><a href="javascript:;" class="show_code wb_btn ag_wb_btn"></a></li>
					</ul>					
				</div>
				<div class="code_hide ag_code_hide">
					<img src="images/live/agzr.jpg" />
					<span>扫码下载AG手机端</span>
				</div>
			</div>
			<div class="lottery-landing">
				<div class="landing-center w_1200">
					<div class="landing-z">
		                <div class="right-box">
		                    <h2 class="title">VR彩票</h2>
		                    <p class="tip">多样彩票 刺激享乐</p>
		                    <div class="game-lsit-text">
		                        <h3>游戏种类：</h3>
		                        <p>北京赛车、幸运飞艇、时时彩、快8、六合彩等</p>
		                    </div>
		                    <ul class="landing-btn">
		                        <li class="web"><a href="javascript:;"><b></b>网页版</a></li>
		                        <!-- <li class="app"><a href=""><b></b>客户端</a></li> -->
		                        <li class="phone show_ewm"><a href="javascript:;"><b></b>手机版</a></li>
		                    </ul>
		                </div>
		                <div class="app_code">
		                        <img src="/images/appxiazai/longduapp.png">
		                        <p>扫码下载手机APP</p>
		                </div>
		            </div>
		        </div>
		    </div>
			<!-- <div class="live2">
				<img src="images/lottery/og.png" /> 
				<div class="overlay2">
					<a href="/gameNTwoRedirect.aspx" class="j-check">进入游戏</a>
				</div>
				<div class="btn_foot">
					<ul>
						<li><a class="j-check pc_btn n2_pc_btn" href="/gameNTwoRedirect.aspx"></a></li>
						<li><a href="javascript:;" class="show_code wb_btn n2_wb_btn"></a></li>
					</ul>					
				</div>
				<div class="code_hide  n2_code_hide">
					<img src="images/live/100.jpg" />
					<span>扫码下载N2手机端</span>
				</div>				
			</div>	 -->
			<!-- <div class="live2 live3" style="margin-top: 0px;">
				<img src="https://www.qnapplehu.com/longdu/bbinlive.png" /> -->
				<!--<div class="overlay2">
					<a href="/gameNTwoRedirect.aspx" class="j-check">进入游戏</a>
				</div>-->
				<!-- <div class="btn_foot">
					<ul>
						<li><a class="j-check pc_btn n2_pc_btn" href="/game/bbinLogin.aspx?gameKind=live"></a></li>
						<li><a href="javascript:;" class="show_code wb_btn n3_wb_btn"></a></li>
					</ul>					
				</div>
				<div class="code_hide  n3_code_hide">
					<img src="/images/appxiazai/longduapp.png" />
					<span>扫码下载手机APP</span>
				</div>				
			</div>	 -->
		<input type="hidden" id="j-gameurl" value="${session.gameurl}">
		<input type="hidden" id="j-slotKey" value="${session.slotKey}">
		<input type="hidden" id="j-referWebsite" value="${session.referWebsite}">
		<input type="hidden" id="j-isLogin" value="${session.customer!=null}">
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>		
	</body>
<script>
	$(function(){
		$(".ag_wb_btn").click(function(){
			$(".ag_code_hide").show();
		})
		$(".live>img").click(function(){
			$(".ag_code_hide").hide();
		})
		
		$(".n2_wb_btn").click(function(){
			$(".n2_code_hide").show();
		})
		$(".live2>img").click(function(){
			$(".n2_code_hide").hide();
		})
		$(".n3_wb_btn").click(function(){
			$(".n3_code_hide").show();
		})
		$(".live3>img").click(function(){
			$(".n3_code_hide").hide();
		})		
	})
</script>
</html>