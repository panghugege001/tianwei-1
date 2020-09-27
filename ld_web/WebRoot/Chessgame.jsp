<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>

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
						<span class="scale"><img src="images/live/761-game07.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								炸金花
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=zjh" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/761-game08.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								龙虎
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=doratora" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/761-game09.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								抢庄牛牛
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=bull/mrob" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/761-game10.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								通比牛牛
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=bull/fair" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/761-game11.png"></span>
						<div class="w_192" style="width: 234px;">
							<p>
								<label>761</label><br />
								<span>牛牛看牌抢庄</span>
							</p>
						</div>
						<div class="w_192 p_30" style="width: 141px;">
							<span ><a href="/game/cg761Login.aspx?gameCode=bull/rob" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/761-game12.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								奔驰宝马
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=carbrand" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li>
						<span class="scale"><img src="images/live/761-game01.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								761牛牛
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=bull" target="_blank" class="pc_btn j-check"></a></span>
						</div>
						<!--<div class="fisshing_zeiz">
							<a href="javascript:;"  onclick="mwGame('22')">进入游戏</a>
						</div>						-->
					</li>
					<li class="AG_fishing">
						<span class="scale"><img src="images/live/761-game02.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								德州扑克
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=texas" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li>
						<span class="scale"><img src="images/live/761-game03.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								欢乐三十秒
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=baccarat" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>
					<li>
						<span class="scale"><img src="images/live/761-game04.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								李逵捕鱼
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=fish" target="_blank" class="pc_btn j-check"></a></span>
						</div>
					</li>

					<li>
						<span class="scale"><img src="images/live/761-game05.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								百人牛牛
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=hundredbull" target="_blank" class="pc_btn j-check"></a></span>
						</div>				
					</li>						
					<li>
						<span class="scale"><img src="images/live/761-game06.png"></span>
						<div class="w_192">
							<p>
								<label>761</label><br />
								飞禽走兽
							</p>
						</div>
						<div class="w_192 p_30">
							<span ><a href="/game/cg761Login.aspx?gameCode=animal" target="_blank" class="pc_btn j-check"></a></span>
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