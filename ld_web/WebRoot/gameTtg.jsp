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
			<div class="winner" style="display: none;">
				<div id="j-count-num" class="count-num"></div>
			</div>
		</div>
		<ul class="game-btn-goup">
			<li><a href="/pt.asp"><i class="icon icon-game-pt"></i>PT老虎机</a></li>
			<li class="active"><a href="/asp/ttLogin.aspx"><i class="icon icon-game-ttg"></i>MG/TTG老虎机</a></li>
			<li><a href="/loginNT.aspx"><i class="icon icon-game-nt"></i>NT老虎机</a></li>
			<li><a href="/gameQt.jsp"><i class="icon icon-game-qt"></i>QT老虎机</a></li>
		</ul>

		<aside class="game-aside">
			<h2>TTG老虎机</h2>
			<ul class="tab-hd tab-game-catetory">
				<li class="active"><a data-toggle="tab" href="#tab-newGames">最新游戏</a></li>
				<li><a data-toggle="tab" href="#tab-hotGames">热门游戏</a></li>
				<li class="j-sub-nav dropdown-toggle"><a data-toggle="tab" href="#tab-slotGames">老虎机</a>
					<div class="sub-nav">
						<a data-toggle="tab" href="#tab-1line">【1条赔付线】</a>
						<a data-toggle="tab" href="#tab-9line">【9条赔付线】</a>
						<a data-toggle="tab" href="#tab-10line">【10或15条赔付线】</a>
						<a data-toggle="tab" href="#tab-20line">【20条赔付线】</a>
						<a data-toggle="tab" href="#tab-25line">【25条赔付线】</a>
						<a data-toggle="tab" href="#tab-30line">【30条赔付线】</a>
						<a data-toggle="tab" href="#tab-40line">【40或50条赔付线】</a>
                        <a data-toggle="tab" href="#tab-60line">【60条赔付线】</a>
						<a data-toggle="tab" href="#tab-1024line">【1024条赔付线】</a>
                        <a data-toggle="tab" href="#tab-234line">【234条赔付线】</a>
					</div>
				</li>
				<li class="j-sub-nav dropdown-toggle"><a data-toggle="tab" href="#tab-mg-allGame">MG小游戏</a>
					<div class="sub-nav">
						<a data-toggle="tab" href="#tab-mg-popularGame">【知名游戏】</a>
						<a data-toggle="tab" href="#tab-mg-hotGame">【热门游戏】</a>
						<a data-toggle="tab" href="#tab-mg-otherGame">【Other】</a>
						<a data-toggle="tab" href="#tab-mg-otherGame1">【Other1】</a>
					</div>
				</li>
                <!--
				<li><a data-toggle="tab" href="#tab-cardGames">纸牌</a></li>
				<li><a data-toggle="tab" href="#tab-desktopGames">桌面游戏</a></li>
				<li><a data-toggle="tab" href="#tab-electronicPorkerGames">电子扑克</a></li>-
				<li class="j-sub-nav dropdown-toggle"><a data-toggle="tab" href="#tab-mg-allGame">MG小游戏</a>
					<div class="sub-nav">
						<a data-toggle="tab" href="#tab-mg-popularGame">【知名游戏】</a>
						<a data-toggle="tab" href="#tab-mg-hotGame">【热门游戏】</a>
						<a data-toggle="tab" href="#tab-mg-otherGame">【Other】</a>
						<a data-toggle="tab" href="#tab-mg-otherGame1">【Other1】</a>
					</div>
				</li>
                <li class="j-sub-nav dropdown-toggle"><a data-toggle="tab" href="#tab-ucGame">UC8游戏</a>
					<div class="sub-nav">
						<a data-toggle="tab" href="#tab-ucfivepay">【5条赔付线】</a>
						<a data-toggle="tab" href="#tab-ucninepay">【9条赔付线】</a>
						<a data-toggle="tab" href="#tab-uctwentyfivepay">【25条赔付线】</a>
						<a data-toggle="tab" href="#tab-ucthirtypay">【30条赔付线】</a>
                        <a data-toggle="tab" href="#tab-ucfortypay">【40条赔付线】</a>
                        <a data-toggle="tab" href="#tab-ucfiftypay">【50条赔付线】</a>
 						<a data-toggle="tab" href="#tab-uconeonepay">【100条赔付线】</a>
					</div>
				</li>
                 -->
                
				<li><a data-toggle="tab" href="#tab-history">游戏记录</a></li>
			</ul>
		</aside>
		<div class="tab-bd gamelist-wp">
			<div id="j-gamelist-tpl"></div>

		</div>
	</div>
	<input id='j-hander' type="hidden" value='<%=session.getAttribute("TTplayerhandle") %>'>
	<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	<script id="tpl" type="text/x-handlebars-template">

			<li class="game-info">
				<div class="game-pic">
					<img src="${ctx}/images/ttggames/{{pic}}" width="156" height="180" alt=""/>
				</div>
				<div class="name"><h4>{{name}}</h4></div>
				<div class="ename">{{eName}}</div>
				<div class="game-play">
					<a href="http://pff.ttms.co/casino/generic/game/game.html?gameSuite=flash&gameName={{code}}&lang=zh-cn&playerHandle=999999&gameType=0&gameId={{id}}&account=FunAcct "
					   target="_blank" class="btn-game demo">试玩游戏</a>
					<a href="https://ams-games.ttms.co/casino/generic/game/game.html?playerHandle={{handle}}&account=CNY&gameName={{code}}&gameId={{id}}&lang=zh-cn&t={{timestamp}}"
					   target="_blank" class="btn-game play j-paly">进入游戏</a>
				</div>
			</li>

	</script>
	<script id="mg-tpl" type="text/x-handlebars-template">

		<li class="game-info">
			<div class="game-pic">
				<img src="${ctx}/images/ttggames/{{pic}}" width="156" height="180" alt=""/>
			</div>
			<div class="name"><h4>{{name}}</h4></div>
			<div class="ename">{{eName}}</div>
			<div class="game-play">
				<a href="https://ams-games.ttms.co/casino/generic/game/game.html?playerHandle={{handle}}&account=CNY&gameName={{code}}&gameId={{id}}&lang=zh-cn&t={{timestamp}}"
				   target="_blank" class="btn-game play j-paly">进入游戏</a>
			</div>
		</li>

	</script>
	<script>
		$(function(){

			var handleVal=$('#j-hander').val(),
					html='',
					tpl=$('#tpl').html(),
					mgTpl=$('#mg-tpl').html();

			$(document).on('click','.game-info .j-paly',function(){
				if(!handleVal||handleVal=='null'){
					alert('请先登录！');
					return false;
				}
			});

			$.when(
					$.getJSON('${ctx}/js/json/ttg.json?v=6'),
					$.getJSON('${ctx}/js/json/mg.json'))
					.then(function(dataTtg,dataMg){
						//模版填充数据
						builHtml('ttg',dataTtg[0]);
						builHtml('mg',dataMg[0]);
						var game_his = $.cookie("ttg_history"/* , {path:cookie_path} */);
			            if (game_his){
			    	       	var gh_json = $.parseJSON(game_his);
			    	       	//var his_html = template({games:gh_json});
			    	       	//$('#j-gamelist-tpl').append(his_html);
			    	       	builHtml('ttg',gh_json);
			            }
						$('#j-gamelist-tpl').html(html);
						$('#j-gamelist-tpl>ul:eq(0)').addClass('active in');
					});

			function builHtml(type,data){

				var typeGame,typeGameList,tmpTpl;
				for(var i=0;i<data.length;i++){
					typeGame=data[i];
					typeGameList=data[i].gameList;
					if(type=='ttg'){
						html +='<ul id="tab-{{type}}" class="tab-panel gamelist fade">'.replace(/\{\{type\}\}/g,typeGame.type);
						tmpTpl=tpl;
					}else if(type=='mg'){
						html +='<ul id="tab-mg-{{type}}" class="tab-panel gamelist fade">'.replace(/\{\{type\}\}/g,typeGame.type);
						tmpTpl=mgTpl;
					}

					for(var j=0;j<typeGameList.length;j++){
						var  obj=typeGameList[j];
						html += tmpTpl.replace(/\{\{pic\}\}/g,obj.pic)
								.replace(/\{\{name\}\}/g,obj.name)
								.replace(/\{\{id\}\}/g,obj.id)
								.replace(/\{\{code\}\}/g,obj.code)
								.replace(/\{\{timestamp\}\}/g,Math.random())
								.replace(/\{\{eName\}\}/g,obj.eName||'')
								.replace(/\{\{handle\}\}/g,handleVal);
					}
					html +='</ul>';
				}

			}
		});
		
		//为每一个开始游戏绑定记录"最近游戏记录"事件
	    $("#j-gamelist-tpl").delegate(".play", "click", function(){
	    	var me = $(this);
	    	var pt_history = $.cookie("ttg_history"/* , {path:cookie_path} */); //获取已有的记录
	    	var game_his;
	    	var name = me.parent().prev().prev().text(), //获取当前点击事件的游戏参数
	    		ename = me.parent().prev().text();
	    	var _href = me.attr("href");
	    	var handle = _href.substring(_href.indexOf("playerHandle=")+13, _href.indexOf("&account"));
	    	var code = _href.substring(_href.indexOf('gameName=')+9, _href.indexOf("&gameId"));
	    	var id = _href.substring(_href.indexOf("gameId=")+7, _href.indexOf("&lang"));
	    	var _src = me.parent().prev().prev().prev().find("img").attr("src");
	    	var pic = _src.substring(_src.lastIndexOf("/")+1);
	    	var gh_json;
	    	if (pt_history == undefined) { //当history为空时,创建初始history
				game_his = $.parseJSON('[{"type":"history","gameList":[{"name":"'+ name +'","eName":"'+ ename +'","handle":"'+ handle +'","id":"'+ id +'","code":"'+ code +'","pic":"'+ pic +'"}]}]');
	    	} else {
	    		game_his = $.parseJSON(pt_history);
	    		var g_l = game_his[0].gameList; //更新history,包括判断重复,最大允许10个记录
	    		for (var i=0;i<g_l.length;i++){
	    			if (g_l[i].code == code){
	    				return true;
	    			}
	    		}
	    		if (g_l.length >= 10) {
	    			g_l[0] = null;
	    			var new_g_l = new Array(g_l.length-1);
	    			for (var i=0;i<g_l.length;i++){ //处理第一个记录为null的问题
	        			if (g_l[i]==null){
	        				continue;
	        			} else {
	        				new_g_l[i-1] = g_l[i];
	        			}
	        		}
	    			g_l = new_g_l;
	    		}
	    		g_l.push($.parseJSON('{"name":"'+ name +'","eName":"'+ ename +'","handle":"'+ handle +'","id":"'+ id +'","code":"'+ code +'","pic":"'+ pic +'"}'));
	   			game_his[0].gameList = g_l;
	    	}
	    	var c_j = JSON.stringify(game_his);
	    	$.cookie("ttg_history", c_j, {expires:7 , path:"/" });
	    	
	    	return true;
	    });


	</script>

</body>
</html>

