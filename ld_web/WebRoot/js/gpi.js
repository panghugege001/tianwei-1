$(function(){
	//滚动到banner
    setTimeout(function(){
        $('html,body').stop(true).animate({scrollTop:700},600);
    },600);
    
    var html='',
        tpl=$('#tpl').html(),
        gpipopup=$('#gpipopup').html();
    $.getJSON('/js/json/gpi.json',function(data){
        //模版填充数据
        builHtml(data);
        //拼接cookie中的最近游戏记录
       	var game_his = $.cookie("gpi_history"/* , {path:cookie_path} */);
        if (game_his){
	       	var gh_json = $.parseJSON(game_his);
	       	builHtml(gh_json);
	       	//var his_html = template({games:gh_json});
	       	//$('#j-gamelist-tpl').append(his_html);
        }
        $('#j-gamelist-tpl').html(html);
        $('#j-gamelist-tpl>ul[id=tab_pngGames]').addClass('active in');
    });

    function builHtml(data){

        var typeGame,typeGameList;
        for(var i=0;i<data.length;i++){
    		typeGame=data[i];
            typeGameList=data[i].gameList;

            html +='<ul id="tab_{{type}}" class="tab-panel gamelist fade">'.replace(/\{\{type\}\}/g,typeGame.type);
			if(typeGame.type=='pngGames' || typeGame.type=='bsGames'){
				for(var j=0;j<typeGameList.length;j++){
	                var  obj=typeGameList[j];
	                html += gpipopup.replace(/\{\{pic\}\}/g,obj.pic)
	                    .replace(/\{\{name\}\}/g,obj.name)
	                    .replace(/\{\{id\}\}/g,obj.id)
						.replace(/\{\{eName\}\}/g,obj.eName||'')
	                    .replace(/\{\{type\}\}/g,typeGame.type);
            	}
			}else if(typeGame.type=="ctxmGames"){
				for(var j=0;j<typeGameList.length;j++){
	                var  obj=typeGameList[j];
	                html += gpipopup.replace(/\{\{pic\}\}/g,obj.pic)
	                    .replace(/\{\{name\}\}/g,obj.name)
	                    .replace(/\{\{id\}\}/g,obj.gid)
						.replace(/\{\{eName\}\}/g,obj.eName||'')
	                    .replace(/\{\{type\}\}/g,typeGame.type);
            	}
			}else if(typeGame.type=='history'){ //为了gpi游戏记录单独增加的判断
				for(var j=0;j<typeGameList.length;j++){
					var  obj=typeGameList[j];
					if (obj.game=='pngGames' || obj.game=='bsGames'){
						html += gpipopup.replace(/\{\{pic\}\}/g,obj.pic)
						.replace(/\{\{name\}\}/g,obj.name)
						.replace(/\{\{id\}\}/g,obj.id)
						.replace(/\{\{eName\}\}/g,obj.eName||'')
						.replace(/\{\{type\}\}/g,typeGame.type);
					} else if (obj.game=="ctxmGames"){
						html += gpipopup.replace(/\{\{pic\}\}/g,obj.pic)
	                    	.replace(/\{\{name\}\}/g,obj.name)
	                    	.replace(/\{\{id\}\}/g,obj.gid)
	                    	.replace(/\{\{eName\}\}/g,obj.eName||'')
	                    	.replace(/\{\{type\}\}/g,typeGame.type);
					} else {
						html += tpl.replace(/\{\{pic\}\}/g,obj.pic)
	                    	.replace(/\{\{name\}\}/g,obj.name)
	                    	.replace(/\{\{eName\}\}/g,obj.eName||'')
	                    	.replace(/\{\{gid\}\}/g,obj.gid);
					}
            	}
			}else {
				for(var j=0;j<typeGameList.length;j++){
	                var  obj=typeGameList[j];
	                html += tpl.replace(/\{\{pic\}\}/g,obj.pic)
	                    .replace(/\{\{name\}\}/g,obj.name)
						.replace(/\{\{eName\}\}/g,obj.eName||'')
	                    .replace(/\{\{gid\}\}/g,obj.gid);
            	}
			}
            html +='</ul>';
        }

    }
    
  //为每一个开始游戏绑定记录"最近游戏记录"事件
    $("#j-gamelist-tpl").delegate(".play", "click", function(){
    	var me = $(this),
    		pt_history = $.cookie("gpi_history"/* , {path:cookie_path} */), //获取已有的记录
    		game_his;
    	var name = me.parent().prev().prev().text(),
    		ename = me.parent().prev().text();//获取当前点击事件的游戏参数
    	var _href = me.attr("href"),
    		gid = _href.substring(_href.indexOf('gameCode=')+9, _href.indexOf('&isfun')),
    		id = _href.substring(_href.indexOf('(\'')+2, _href.indexOf('\',')),
    		_src = me.parent().prev().prev().prev().find("img").attr("src"),
    		pic = _src.substring(_src.lastIndexOf("/")+1),
    		game = me.parents(".gamelist").attr("id").substring(4),
    		type = _href.substring(_href.indexOf("0, '")+4, _href.indexOf("')"));
    	var gh_json;
    	if (pt_history == undefined) { //当history为空时,创建初始history
    		game_his = $.parseJSON('[{"type":"history","gameList":[{"name":"'+ name +'","eName":"'+ ename +'","gid":"'+ gid +'","id":"'+ id +'","pic":"'+ pic +'","game":"'+ game +'","type":"'+ type +'"}]}]');
    	} else {
    		game_his = $.parseJSON(pt_history);
    		var g_l = game_his[0].gameList; //更新history,包括判断重复,最大允许10个记录
    		for (var i=0;i<g_l.length;i++){
    			if (g_l[i].name == name){
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
    		g_l.push($.parseJSON('{"name":"'+ name +'","eName":"'+ ename +'","gid":"'+ gid +'","id":"'+ id +'","pic":"'+ pic +'","game":"'+ game +'","type":"'+ type +'"}'));
    		game_his[0].gameList = g_l;
    	}
    	var c_j = JSON.stringify(game_his);
    	$.cookie("gpi_history", c_j, {expires:7, path:"/"});
    	
    	return true;
    });
    
});
