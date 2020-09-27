<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!-- KY棋牌 -->
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/Chessgame.css?v=0139" />
	</head>

	<body>
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		<div class="index_bj" id="chessBox">
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
			<div class="w_1200 chessbox" id="chessOne" style="position: relative;">
				<ul class="Fishing_game h_45">
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/damanguang1.png"></span>
						<div class="w_192">
							<p>
								<label>MW</label><br />
								斗地主
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="javascript:;" class="pc_btn" onclick="mwGame('20')"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="javascript:;"  onclick="mwGame('20')">进入游戏</a>
						</div>-->
					</li>
					<li>
						<span class="scale"><img src="images/live/damanguang2.png"></span>
						<div class="w_192">
							<p>
								<label>MW</label><br />
								好运5扑克
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="javascript:;" class="pc_btn" onclick="mwGame('22')"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="javascript:;"  onclick="mwGame('22')">进入游戏</a>
						</div>						-->
					</li>
					<li>
						<span class="scale"><img src="images/live/damanguang3.png"></span>
						<div class="w_192">
							<p>
								<label>MW</label><br />
								百乐牛牛
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="javascript:;" class="pc_btn" onclick="mwGame('139')"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="javascript:;"  onclick="mwGame('139')">进入游戏</a>
						</div>						-->
					</li>					
				</ul>				
			</div>
			<div class="w_1200 chessbox" id="chessTwo" style="position: relative;">
					<a href="javascritp:;" class="arrow arrowLeft"><img src="/images/live/left.png" alt=""></a>
					<a href="javascritp:;" class="arrow arrowRight"><img src="/images/live/right.png" alt=""></a>
				<div id="chessTwoBox">
				<ul class="Fishing_game h_45">
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-qznn.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								抢庄牛牛
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=830" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-zjh.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								炸金花
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=220" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-jszjh.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								急速炸金花
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=230" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-dzpk.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								德州扑克
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=620" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-tbnn.png"></span>
						<div class="w_192" style="width: 234px;">
							<p>
								<label>KY</label><br />
								<span>通比牛牛</span>
							</p>
						</div>
						<div class="w_192 p_30" style="width: 141px;">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=870" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-sbg.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								二八杠
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=720" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li>
						<span class="scale"><img src="images/live/ky/ky-qzpj.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								抢庄牌九
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=730" target="_blank" class="pc_btn j-check"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="javascript:;"  onclick="mwGame('22')">进入游戏</a>
						</div>						-->
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/ky/ky-sg.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								三公
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=860" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li>
						<span class="scale"><img src="images/live/ky/ky-xywz.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								幸运五张
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=380" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li>
						<span class="scale"><img src="images/live/ky/ky-sss.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								十三水
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=630" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>

					<li>
						<span class="scale"><img src="images/live/ky/ky-ddz.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								斗地主
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=610" target="_blank" class="pc_btn j-check"></a></span>
						</div>				
					</li>						
					<li>
						<span class="scale"><img src="images/live/ky/ky-yzlh.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								押注庄龙虎
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=900" target="_blank" class="pc_btn j-check"></a></span>
						</div>					
					</li>
					<li>
						<span class="scale"><img src="images/live/ky/ky-hlhb.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								欢乐红包
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=880" target="_blank" class="pc_btn j-check"></a></span>
						</div>					
					</li>
					<li>
						<span class="scale"><img src="images/live/ky/ky-21d.png"></span>
						<div class="w_192">
							<p>
								<label>KY</label><br />
								21 点
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/kyqpLogin.aspx?gameCode=600" target="_blank" class="pc_btn j-check"></a></span>
						</div>					
					</li>
				</ul>
				</div>
			</div>
		</div>
		<input type="hidden" id="j-gameurl" value="${session.gameurl}">
		<input type="hidden" id="j-slotKey" value="${session.slotKey}">
		<input type="hidden" id="j-referWebsite" value="${session.referWebsite}">
		<input type="hidden" id="j-isLogin" value="${session.customer!=null}">
		<div style="width: 100%; height: 300px;"></div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>		
	</body>
<script>
	$(function(){
		
		$(".j-check").click(function(){
			if('${session.customer==null}'=='true'){
				alert('请登入后，再进行游戏！')
				return false;
			}			
		})

		var $chessBox = $('#chessBox');
		var pageId = window.location.hash;
		$chessBox.length &&  $chessBox.find(pageId).show().siblings('.chessbox').hide();
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
	<script>
		var $accountUl = $('#chessTwo').find('ul') ,length = $accountUl.find('li').length,mgWidth = 400,count = 0;
		$accountUl.width(400 * length);
		$('#chessTwo').find('.arrow').on('click',function (e) {
			var $this = $(this);
			if($this.hasClass('arrowRight')){

				if(Math.abs(count) == length - 3){
					return false;
				}
				count -= 1;
				$accountUl.stop().animate({'marginLeft': mgWidth*count + 'px'},
						100, function() {
							/* stuff to do after animation is complete */
						});
			}else {
				if(count == 0){
					return false;
				}
				count += 1;
				$accountUl.stop().animate({'marginLeft': mgWidth*count + 'px'},
						300, function() {
							/* stuff to do after animation is complete */
						});
			}
		})
	</script>
</html>