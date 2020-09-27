<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/Fishing.css?v=001" />
	</head>

	<body>
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		<div class="index_bj">
			<div class="h_820">
				<!--<div class="w_center">
					<div class="qiehuan_btn">
						<ul>
							<li class="left_btn prev"></li>
							<li class="right_btn_by"></li>
						</ul>
					</div>						
				</div>-->
			</div>
			<div class="w_1200" style="position: relative;">
				
			<div class="btn_div">
				<span  class="left_btn prev">
					<img  src="images/live/left.png"/>
				</span>
				<span class="rigth_btn next">
					<img src="images/live/right.png" />
				</span>
			</div>				
				
				<ul class="Fishing_game h_45">
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/AG.png"></span>
						<div class="w_192">
							<p>
								<label>AG</label><br />
								捕鱼王
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/gameAginBuyuRedirect.aspx" class="pc_btn ag_pc_btn j-check"></a></span>
							<span><a  href="javascript:;" class="wb_btn ag_wb_btn"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="/gameAginBuyuRedirect.aspx" class="j-check">进入游戏</a>
						</div>-->
						<div class="Fishing_code AG_code">
							<img src="images/live/agzr.jpg" />
							<span>扫码下载AG客户端</span>
						</div>
					</li>
					<%--<li class="PT_fishing">--%>
						<%--<span class="scale"><img src="images/live/PT.png"></span>--%>
						<%--<div class="w_192">--%>
							<%--<p>--%>
								<%--<label>PT</label><br />--%>
								<%--深海捕鱼--%>
							<%--</p>--%>
						<%--</div>--%>
						<%--<div class="w_192 p_30">--%>
							<%--<span ><a href="/loginGame.aspx?gameCode=cashfi" class="pc_btn ag_pc_btn j-check"></a></span>--%>
							<%--<span><a  href="javascript:;" class="wb_btn pt_wb_btn"></a></span>--%>
						<%--</div>--%>
						<%--<div class="Fishing_code PT_code">--%>
							<%--<img src="images/live/1511834414.png" />--%>
							<%--<span>扫码下载PT客户端</span>--%>
						<%--</div>						--%>
						<%--<!--<div class="fisshing_zeiz">--%>
							<%--<a href="/loginGame.aspx?gameCode=cashfi" class="j-check">进入游戏</a>--%>
						<%--</div>						-->--%>
					<%--</li>--%>
<!-- 					<li class="GG_fishing">
						<span class="scale"><img src="images/live/GG.png"></span>
						<div class="w_192">
							<p>
								<label>GG</label><br />
								捕鱼天下
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/asp/ttLogin.aspx?gameName=EGIGame&gameType=0&gameId=14900&lang=zh-cn&deviceType=web" class="pc_btn ag_pc_btn j-check"></a></span>
							<span><a  href="javascript:;" class="wb_btn gg_wb_btn"></a></span>
						</div>

						<div class="Fishing_code GG_code">
							<img src="images/live/longduapp.png" />
							<span>扫码下载APP客户端</span>
						</div>							
					</li> -->
					<li class="MW_fishing">
						<span class="scale"><img src="images/live/damanguang.png"></span>
						<div class="w_192">
							<p>
								<label>MW</label><br />
								千炮捕鱼
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="javascript:;" onclick="mwGame('1051')" class="pc_btn ag_pc_btn j-check"></a></span>
							<span><a  href="javascript:;" class="wb_btn mw_wb_btn"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="javascript:;" onclick="mwGame('50')">进入游戏</a>
						</div>	
										-->
						<div class="Fishing_code MW_code">
							<img src="images/live/1511834509.png" />
							<span>扫码下载MW客户端</span>
						</div>											
			</li>
					
					<li class="qianlai_fishing">
						<span class="scale"><img src="images/live/qianlai.png"></span>
						<div class="w_192">
							<p>
								<label>HYG</label><br />
								钱来捕鱼
							</p>
						</div>
						
						<div class="w_192 p_30">
							<span ><a href="/game/gameLoginDTFish.aspx" class="pc_btn ag_pc_btn"></a></span>
							<span><a  href="javascript:;" class="wb_btn qianlai_btn"></a></span>
						</div>
						<div class="Fishing_code QL_code">
							<img src="/images/appxiazai/longduapp.png" /> 
							<span>扫码下载APP客户端</span> 
						</div>												
					</li> 					
				</ul>				
			</div>				
		</div>
		<input type="hidden" id="j-gameurl" value="${session.gameurl}">
		<input type="hidden" id="j-slotKey" value="${session.slotKey}">
		<input type="hidden" id="j-referWebsite" value="${session.referWebsite}">
		<input type="hidden" id="j-isLogin" value="${session.customer!=null}">
		<div style="width: 100%; height: 300px;"></div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>		
	</body>
<script type="text/javascript" src="../js/superslide.2.1.js"></script> 
<script>
		jQuery(".index_bj").slide({titCell: ".qiehuan_btn ul", mainCell: ".w_1200 ul", autoPage: true, effect: "left", autoPlay: true, vis: 3, trigger: "click" });
</script>
<script>
	$(function(){
		$(".AG_fishing").click(function(){
			$(".AG_code").hide();
		})
		
		$(".ag_wb_btn").on('click',function(e){
			e.stopPropagation();
			$(".AG_code").show();
		})
		
		$(".PT_fishing").click(function(){
			$(".PT_code").hide();
		})
		
		$(".pt_wb_btn").on('click',function(e){
			e.stopPropagation();
			$(".PT_code").show();
		})	
		
		$(".GG_fishing").click(function(){
			$(".GG_code").hide();
		})
		
		$(".gg_wb_btn").on('click',function(e){
			e.stopPropagation();
			$(".GG_code").show();
		})
		
		$(".MW_fishing").click(function(){
			$(".MW_code").hide();
		})
		
		$(".mw_wb_btn").on('click',function(e){
			e.stopPropagation();
			$(".MW_code").show();
		})
		
		$(".qianlai_btn").on('click',function(e){
			e.stopPropagation();
			$(".QL_code").show();
		})			
		$(".qianlai_fishing").click(function(){
			$(".QL_code").hide();
		})		
		
	})
</script>
<script>
        function mwGame(type) {
        if ($("#j-isLogin").val()) {
            openProgressBar();
            $.post("/asp/mwgLogin.aspx", {"gameCode": type}, function (response) {
                closeProgressBar();
                if (response.code == 0) {
                    window.location.href=response.data;
                } else {
                    alert(response.msg);
                }
            })
        } else {
            alert('您好，请先登录！');
        }
    }		
</script>
</html>