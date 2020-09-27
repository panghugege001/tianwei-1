(function(){

})();

$(function(){

	//agent_banner开始


		var $move = $('#ban ul .move');
		var $item = $('#items .item');
	
	$move.siblings().click(function() {
        var $el = $(this);
        var leftPos = $el.position().left;
        var topPos = $el.position().top;
        var ind=$(this).index();
       // var colors = $move.siblings().not('.default').css('color');
        var $current = $('#items .on');
		
		if($('#items').attr('class') == 'leftright'){
			$(this).addClass('on').siblings().removeClass('on').css({'color':'#7d6549'});
			$item.eq(ind-1).addClass('on').removeClass('off').siblings().removeClass('on').addClass('off');
			var itemW = $item[0].offsetWidth;
			for(var i=0;i<$item.length;i++){$item.eq(i).css({'left':i*itemW});}
			var movenum = ind - 1 - $current.index();
			$('#items').stop(true,true).animate({'left':'-='+movenum*itemW},1000,'easeInOutCirc');
		}else if($('#items').attr('class') == 'promotion') {
			$(this).addClass('on').siblings().removeClass('on').css({'color':'#7d6549'});
			var arr = ($(this).attr('class')).split(" ");
			var classStr = arr[0];
			var lengthnum = $('#items ' + classStr).length;
			$('#items .item').children().removeClass('on');		
			var $de = $item.detach();
			$de.each(function(i,n){if($(n).hasClass(classStr)){$('#items').append($de[i]);}});
			setTimeout(function(){
				var tops = $('#content_bg .main').offset().top;
				$('html,body').animate({scrollTop:tops},500);
			},50);
		
		}else{
			$(this).addClass('on').siblings().removeClass('on').css({'color':'#b7b3b1'});
			$item.eq(ind-1).addClass('on').removeClass('off').siblings().removeClass('on').addClass('off');
		}

       $move.stop(true,true).animate({ left: leftPos,top:topPos},"200",function(){
        	$move.siblings('.on').css({'color':'#17130f'});
        })
    });	
	
	
	$(".agent_banner .big li").each(function(){
		$(this).css("background-image","url("+$(this).attr("bg")+")");	
	});
	
	new FadeAdver({
		btns :$(".agent_banner .btns span"),
		divs : $(".agent_banner .big li")	
	});
/*	$(window).bind('scroll',goscroll);
	$(window).bind('resize',goscroll);
	function goscroll(){
		setTimeout(function(){
			$('#content_page').each(function() {
				var eachdiv = $(this).offset().top;
				var scroH = $(window).scrollTop();
				var totop = scroH - eachdiv + 80;
				var eachleft = $(this).find('.agent_page1').offset().left;
				//alert(scroH - eachdiv);  
				if(eachdiv <scroH){
					//alert(totop);
					//$(this).find('.showleft').stop().animate({'top':totop},'easeOutElastic');
					$(this).find('#ban .items').css({'position':'fixed','top':'0px','left':eachleft});
					$(this).css({'background-attachment':'fixed'});
				}else{
					$(this).find('#ban .items').css({'position':'absolute','top':'0px','left':'0px'});
					$(this).css({'background-attachment':'scroll'});
				}             
            });
		},50);

	}*/
	
	
}); 
function intval(v) {
    v = parseInt(v);
    return isNaN(v) ? 0 : v;
}
// 获取元素信息
function getPos(e) {
    var l = 0;
    var t = 0;
    var w = intval(e.style.width);
    var h = intval(e.style.height);
    var wb = e.offsetWidth;
    var hb = e.offsetHeight;
    while (e.offsetParent) {
        l += e.offsetLeft + (e.currentStyle ? intval(e.currentStyle.borderLeftWidth) : 0);
        t += e.offsetTop + (e.currentStyle ? intval(e.currentStyle.borderTopWidth) : 0);
        e = e.offsetParent;
    }
    l += e.offsetLeft + (e.currentStyle ? intval(e.currentStyle.borderLeftWidth) : 0);
    t += e.offsetTop + (e.currentStyle ? intval(e.currentStyle.borderTopWidth) : 0);
    return {
        x: l,
        y: t,
        w: w,
        h: h,
        wb: wb,
        hb: hb
    };
}
// 获取滚动条信息
function getScroll() {
    var t, l, w, h;
    if (document.documentElement && document.documentElement.scrollTop) {
        t = document.documentElement.scrollTop;
        l = document.documentElement.scrollLeft;
        w = document.documentElement.scrollWidth;
        h = document.documentElement.scrollHeight;
    } else if (document.body) {
        t = document.body.scrollTop;
        l = document.body.scrollLeft;
        w = document.body.scrollWidth;
        h = document.body.scrollHeight;
    }
    return {
        t: t,
        l: l,
        w: w,
        h: h
    };
}
// 锚点(Anchor)间平滑跳转 
function scroller(el, duration) {
    if (typeof el != 'object') {
        el = document.getElementById(el);
    }
    if (!el) return;
    var z = this;
    z.el = el;
    z.p = getPos(el);
    z.s = getScroll();
    z.clear = function() {
        window.clearInterval(z.timer);
        z.timer = null
    };
    z.t = (new Date).getTime();
    z.step = function() {
        var t = (new Date).getTime();
        var p = (t - z.t) / duration;
        if (t >= duration + z.t) {
            z.clear();
            window.setTimeout(function() {
                z.scroll(z.p.y, z.p.x)
            },
            13);
        } else {
            st = (( - Math.cos(p * Math.PI) / 2) + 0.5) * (z.p.y - z.s.t) + z.s.t;
            sl = (( - Math.cos(p * Math.PI) / 2) + 0.5) * (z.p.x - z.s.l) + z.s.l;
            z.scroll(st, sl);
        }
    };
    z.scroll = function(t, l) {
        window.scrollTo(l, t)
    };
    z.timer = window.setInterval(function() {
        z.step();
    },
    13);
}