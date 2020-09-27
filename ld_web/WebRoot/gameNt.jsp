<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="dfh.action.vo.AnnouncementVO"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<s:include value="/title.jsp"></s:include>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/game.css?v=11"/>

	</head>
<body>
	<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
	<div class="banner">
		<img src="${ctx}/images/slot-banner.jpg?v=1" alt="" style="width: 100%;"/>
	</div>
	<div class="game-wp wp cfx">
		<div class="game-top cfx">
			<div class="winner-msg-outer" id="j-marquee-winner-list">
				<ul class="winner-msg cfx" >
					<jsp:include page="/tpl/winnerList.jsp"></jsp:include>
				</ul>
			</div>

		</div>
		<ul class="game-btn-goup">
			<li><a href="/pt.asp"><i class="icon icon-game-pt"></i>PT老虎机</a></li>
			<li><a href="/asp/ttLogin.aspx"><i class="icon icon-game-ttg"></i>MG/TTG老虎机</a></li>
			<li class="active"><a href="/gameNt.jsp"><i class="icon icon-game-nt"></i>NT老虎机</a></li>
			<li><a href="/gameQt.jsp"><i class="icon icon-game-qt"></i>QT老虎机</a></li>

		</ul>

		<aside class="game-aside">
			<h2>NT老虎机</h2>
			<ul class="tab-hd tab-game-catetory" id="j-game-catetory">
				<li class="active"><a data-toggle="tab" href="#tab-classicsGames">经典游戏</a></li>
				<li><a data-toggle="tab" href="#tab-electricGames">电动老虎机</a></li>
				<li><a data-toggle="tab" href="#tab-amassGames">累积游戏</a></li>
				<li><a data-toggle="tab" href="#tab-miniGames">迷你游戏</a></li>
				<!-- <li><a data-toggle="tab1" href="javascript:void(0);" onclick="record();">投注记录</a></li> -->
			</ul>
		</aside>
		<div class="tab-bd gamelist-wp">
			<div id="j-gamelist-tpl"></div>
			<div id="record-tpl" style="display: none;">
				<iframe width="100%" height="780" border="0" scrolling="no" frameborder="0" allowtransparency="true"></iframe>
			</div>
		</div>
	</div>
	<input id='j-hander' type="hidden" value='<%=session.getAttribute("nt_session") %>'>
	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	<script id="tpl" type="text/x-handlebars-template">

			<li class="game-info">
				<div class="game-pic">
					<img src="${ctx}/images/ntgames/{{pic}}" width="156" height="153" alt=""/>
				</div>
				<div class="name"><h4>{{name}}</h4></div>
				<div class="ename">{{eName}}</div>
				<div class="game-play">
					<a href="http://load.sdjdlc.com/disk2/netent/?game={{id}}&key={{key}}&language=cn"
					   target="_blank" class="btn-game play">进入游戏</a>
					<a href="http://load.sdjdlc.com/disk2/netent/demo.html?game={{id}}&language=cn"
					   target="_blank" class="btn-game demo">试玩游戏</a>
				</div>
			</li>

	</script>
	<script>
		$(function(){
			var handleVal=$('#j-hander').val(),
					html='',
					tpl=$('#tpl').html();
					//key='92ad95acfc8e1cf0d6284d042af76d';

			$(document).on('click','.game-info .play',function(){
				if(!handleVal||handleVal=='null'){
					alert('请先登录！');
					return false;
				}
			});

			$.getJSON('${ctx}/js/json/nt.json?v=7',function(data){
			 	//模版填充数据
			 	builHtml(data);
			 	$('#j-gamelist-tpl').html(html);
				$('#j-gamelist-tpl>ul:eq(0)').addClass('active');
			 });

			function builHtml(data){

				var typeGame,typeGameList;
				for(var i=0;i<data.length;i++){
					typeGame=data[i];
					typeGameList=data[i].gameList;
					html +='<ul id="tab-{{type}}" class="tab-panel gamelist">'.replace(/\{\{type\}\}/g,typeGame.type);

					for(var j=0;j<typeGameList.length;j++){
						var  obj=typeGameList[j];
						html += tpl.replace(/\{\{pic\}\}/g,obj.pic)
								.replace(/\{\{name\}\}/g,obj.name)
								.replace(/\{\{id\}\}/g,obj.id)
								.replace(/\{\{key\}\}/g,handleVal)
								.replace(/\{\{eName\}\}/g,obj.eName||'');
					}
					html +='</ul>';
				}

			};
			
			var record = function(){
				$("#j-gamelist-tpl").hide();
				$("#record-tpl").find("iframe").attr("src", "${ctx}/gameNtRecord.jsp").show();
			};
			
			$(".tab-hd").find("a[data-toggle='tab']").each(function(){
				//为游戏a标签绑定click事件显示游戏列表div
				$(this).click(function(){
					$("#j-gamelist-tpl").show();
					$("#record-tpl").hide();
				})
			});

			$("#j-game-catetory").find("a[data-toggle='tab']").on({
				'show.bs.tab':function(){
					var $activeTarget= $($(this).attr('href'));
					$activeTarget.addClass('before-show');
				},
				'shown.bs.tab':function(){
					var $activeTarget= $($(this).attr('href'));
					setTimeout(function(){
						$activeTarget.removeClass('before-show');
					},60);
				}
			});
		
		});
	</script>

</body>
</html>

