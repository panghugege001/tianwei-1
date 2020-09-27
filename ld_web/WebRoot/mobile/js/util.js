
/**
 * 全局配置
 * @type {{baseUrl: string, imgCodeUrl: string}}
 */

!function (w) {
    var Global=w.Global||{};

    Global.config={
        'baseUrl':'/',
        'imgCodeUrl':'/mobi/mobileValidateCode.aspx'
    };

    w.Global=Global;
}(window);

/*! CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
 ============================================================ */
+function(a){"use strict";function b(){var c,a=document.createElement("bootstrap"),b={WebkitTransition:"webkitTransitionEnd",MozTransition:"transitionend",OTransition:"oTransitionEnd otransitionend",transition:"transitionend"};for(c in b)if(void 0!==a.style[c])return{end:b[c]};return!1}a.fn.emulateTransitionEnd=function(b){var e,c=!1,d=this;return a(this).one("bsTransitionEnd",function(){c=!0}),e=function(){c||a(d).trigger(a.support.transition.end)},setTimeout(e,b),this},a(function(){a.support.transition=b(),a.support.transition&&(a.event.special.bsTransitionEnd={bindType:a.support.transition.end,delegateType:a.support.transition.end,handle:function(b){return a(b.target).is(this)?b.handleObj.handler.apply(this,arguments):void 0}})})}(jQuery);


/* ========================================================================
 * Bootstrap: tab.js v3.3.5
 * http://getbootstrap.com/javascript/#tabs
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */

+function(a){"use strict";function c(c){return this.each(function(){var d=a(this),e=d.data("bs.tab");e||d.data("bs.tab",e=new b(this)),"string"==typeof c&&e[c]()})}var d,e,b=function(b){this.element=a(b)};b.VERSION="3.3.5",b.TRANSITION_DURATION=150,b.prototype.show=function(){var e,f,g,h,b=this.element,c=b.closest("ul:not(.dropdown-menu)"),d=b.data("target");d||(d=b.attr("href"),d=d&&d.replace(/.*(?=#[^\s]*$)/,"")),b.parent("li").hasClass("active")||(e=c.find(".active:last a"),f=a.Event("hide.bs.tab",{relatedTarget:b[0]}),g=a.Event("show.bs.tab",{relatedTarget:e[0]}),e.trigger(f),b.trigger(g),g.isDefaultPrevented()||f.isDefaultPrevented()||(h=a(d),this.activate(b.closest("li"),c),this.activate(h,h.parent(),function(){e.trigger({type:"hidden.bs.tab",relatedTarget:b[0]}),b.trigger({type:"shown.bs.tab",relatedTarget:e[0]})})))},b.prototype.activate=function(c,d,e){function h(){f.removeClass("active").find("> .dropdown-menu > .active").removeClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded",!1),c.addClass("active").find('[data-toggle="tab"]').attr("aria-expanded",!0),g?(c[0].offsetWidth,c.addClass("in")):c.removeClass("fade"),c.parent(".dropdown-menu").length&&c.closest("li.dropdown").addClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded",!0),e&&e()}var f=d.find("> .active"),g=e&&a.support.transition&&(f.length&&f.hasClass("fade")||!!d.find("> .fade").length);f.length&&g?f.one("bsTransitionEnd",h).emulateTransitionEnd(b.TRANSITION_DURATION):h(),f.removeClass("in")},d=a.fn.tab,a.fn.tab=c,a.fn.tab.Constructor=b,a.fn.tab.noConflict=function(){return a.fn.tab=d,this},e=function(b){b.preventDefault(),c.call(a(this),"show")},a(document).on("click.bs.tab.data-api",'[data-toggle="tab"]',e).on("click.bs.tab.data-api",'[data-toggle="pill"]',e)}(jQuery);

/*
 * 全局的弹框，tip 工具类
 * http://sufangyu.github.io
 * 1.0.3(2016-07-15)
 */
!function(a,b){var d,e,f,h,i,k,l,m,n=function(){var a,c;if(!(b(".dialog-wrap").length>0)){switch(clearTimeout(l),clearTimeout(m),k.onBeforeShow(),b("body").append(dialogWrapper=b('<div class="dialog-wrap '+k.dialogClass+'"></div>')),dialogWrapper.append(d=b('<div class="dialog-overlay"></div>'),e=b('<div class="dialog-content dialog-content-animate"></div>')),solveTapBug=b('<div class="solve-tap-bug" style="margin:0;padding:0;border:none;background:rgba(255,255,255,0.01); -webkit-tap-highlight-color:rgba(0,0,0,0); width:100%; height:100%; position:fixed; top:0px; left:0px;"></div>').insertBefore(dialogWrapper),k.type){case"alert":k.showTitle&&e.append(f=b('<div class="dialog-content-hd"><h4 class="dialog-content-title">'+k.titleText+"</h4></div>")),e.append(contentBd=b('<div class="dialog-content-bd">'+k.contentHtml+"</div>")),e.append(contentFt=b('<div class="dialog-content-ft"></div>')),contentFt.append(i=b('<button class="dialog-btn dialog-btn-ok '+k.buttonClass.ok+'" >'+k.buttonText.ok+"</button>"));break;case"confirm":k.showTitle&&e.append(f=b('<div class="dialog-content-hd"><h4 class="dialog-content-title">'+k.titleText+"</h4></div>")),e.append(contentBd=b('<div class="dialog-content-bd">'+k.contentHtml+"</div>")),e.append(contentFt=b('<div class="dialog-content-ft"></div>')),contentFt.append(h=b('<button class="dialog-btn dialog-btn-cancel '+k.buttonClass.cancel+'" >'+k.buttonText.cancel+"</button>"),i=b('<button class="dialog-btn dialog-btn-ok '+k.buttonClass.ok+'" >'+k.buttonText.ok+"</button>"));break;case"info":a=k.contentHtml||'<img class="info-icon" src="'+k.infoIcon+'" alt="'+k.infoText+'" /><p class="info-text">'+k.infoText+"</p>",e.append(contentBd=b('<div class="dialog-content-bd">'+a+"</div>")),dialogWrapper.addClass("dialog-wrap-info"),e.addClass("dialog-content-info").removeClass("dialog-content-animate");break;case"tips":c=k.contentHtml||(k.infoIcon?'<img class="info-icon" src="'+k.infoIcon+'" alt="'+k.infoText+'" />':"")+'<span class="info-text">'+k.infoText+"</span>",e.append(contentBd=b('<div class="dialog-content-bd">'+c+"</div>")),dialogWrapper.addClass("dialog-wrap-tips"),e.addClass("dialog-content-tips").removeClass("dialog-content-animate")}setTimeout(function(){dialogWrapper.addClass("dialog-wrap-show"),k.onShow(),r()},20),setTimeout(function(){q()},100)}},o=function(){t.tap(b(i),function(){return k.onClickOk(),b.dialog.close(),!1}),t.tap(b(h),function(){return k.onClickCancel(),b.dialog.close(),!1}),k.overlayClose&&t.tap(b(d),function(){b.dialog.close()}),k.autoClose>0&&p(),b(document).on("touchmove",function(a){return b(dialogWrapper).find(b(a.target)).length?!1:!0}),s()},p=function(){clearTimeout(m),m=window.setTimeout(function(){b.dialog.close()},k.autoClose)},q=function(){var c,d,g,h,a=b(window).height();b(contentBd).removeAttr("style"),b(e).height()>=a-10&&(c=b(f).height()+parseInt(b(f).css("margin-top"))+parseInt(b(f).css("margin-bottom"))+parseInt(b(f).css("padding-top"))+parseInt(b(f).css("padding-bottom")),d=b(contentFt).height()+parseInt(b(contentFt).css("margin-top"))+parseInt(b(contentFt).css("margin-bottom"))+parseInt(b(contentFt).css("padding-top"))+parseInt(b(contentFt).css("padding-bottom")),g=parseInt(b(contentBd).css("margin-top"))+parseInt(b(contentBd).css("margin-bottom"))+parseInt(b(contentBd).css("padding-top"))+parseInt(b(contentBd).css("padding-bottom")),h=a-c-d-g-50,b(contentBd).css({"max-height":h,"overflow-y":"auto"}))},r=function(){b(window).on("resize",function(){clearTimeout(a);var a=window.setTimeout(function(){q()},100)})},s=function(){var a={x:0,y:0,top:0,left:0};b(contentBd).on("touchstart",function(){a.x=event.changedTouches[0].clientX,a.y=event.changedTouches[0].clientY,0==a.top&&(a.top=b(this).scrollTop()),0==a.left&&(a.left=b(this).scrollLeft())}).on("touchmove",function(){b(this).scrollTop(a.top-event.changedTouches[0].clientY+a.y),b(this).scrollLeft(a.left-event.changedTouches[0].clientX+a.x)}).on("touchend",function(b){(0!=a.x||0!=a.y)&&(a={x:0,y:0,top:0,left:0}),b.preventDefault()})},t={tap:function(a,b){var c,d,e;"ontouchstart"in window||"ontouchstart"in document?c=!0:window.navigator.msPointerEnabled&&(c=!0),c?(a.on("touchstart",function(a){var b=a.touches?a.touches[0]:a.originalEvent.touches[0];d=b.clientX,e=b.clientY}),a.on("touchend",function(a){var c=a.changedTouches?a.changedTouches[0]:a.originalEvent.changedTouches[0];endTx=c.clientX,endTy=c.clientY,Math.abs(d-endTx)<6&&Math.abs(e-endTy)<6&&b(),a.preventDefault()})):a.on("click",function(){b()})}};b.dialog=function(a){return k=b.extend({},b.fn.dialog.defaults,a),b.dialog.init(),this},b.dialog.init=function(){n(),o()},b.dialog.close=function(){k.onBeforeClosed(),dialogWrapper.removeClass("dialog-wrap-show"),l=setTimeout(function(){dialogWrapper.remove(),k.onClosed()},100),b(document).on("touchmove",function(){return!0}),setTimeout(function(){solveTapBug.remove()},350)},b.dialog.update=function(a){a.infoText&&e.find(".info-text").html(a.infoText),a.infoIcon&&e.find(".info-icon").attr("src",a.infoIcon),a.autoClose>0&&window.setTimeout(function(){a.onBeforeClosed&&a.onBeforeClosed(),b.dialog.close(),a.onClosed&&a.onClosed()},a.autoClose)},b.fn.dialog=function(){return this},b.fn.dialog.defaults={type:"alert",titleText:"信息提示",showTitle:!0,contentHtml:"",dialogClass:"",autoClose:0,overlayClose:!1,drag:!1,buttonText:{ok:"确定",cancel:"取消","delete":"删除"},buttonClass:{ok:"",cancel:"","delete":""},infoText:"",infoIcon:"/images/icon/info.png",onClickOk:function(){},onClickCancel:function(){},onClickClose:function(){},onBeforeShow:function(){},onShow:function(){},onBeforeClosed:function(){},onClosed:function(){}}}(window,window.Zepto||window.jQuery);

/*! artDialog.js
 ============================================================ */
!function(){function b(c){var d=a[c],e="exports";return"object"==typeof d?d:(d[e]||(d[e]={},d[e]=d.call(d[e],b,d[e],d)||d[e]),d[e])}function c(b,c){a[b]=c}var a={};c("jquery",function(){return jQuery}),c("popup",function(a){function f(){this.destroyed=!1,this.__popup=b("<div />").attr({tabindex:"-1"}).css({display:"none",position:"absolute",left:0,top:0,bottom:"auto",right:"auto",margin:0,padding:0,outline:0,border:"0 none",background:"transparent"}).html(this.innerHTML).appendTo("body"),this.__backdrop=b("<div />"),this.node=this.__popup[0],this.backdrop=this.__backdrop[0],c++}var b=a("jquery"),c=0,d=!("minWidth"in b("html")[0].style),e=!d;return b.extend(f.prototype,{node:null,backdrop:null,fixed:!1,destroyed:!0,open:!1,returnValue:"",autofocus:!0,align:"bottom left",backdropBackground:"#000",backdropOpacity:.7,innerHTML:"",className:"ui-popup",show:function(a){var c,e;return this.destroyed?this:(c=this,e=this.__popup,this.__activeElement=this.__getActive(),this.open=!0,this.follow=a||this.follow,this.__ready||(e.addClass(this.className),this.modal&&this.__lock(),e.html()||e.html(this.innerHTML),d||b(window).on("resize",this.__onresize=function(){c.reset()}),this.__ready=!0),e.addClass(this.className+"-show").attr("role",this.modal?"alertdialog":"dialog").css("position",this.fixed?"fixed":"absolute").show(),this.__backdrop.show(),this.reset().focus(),this.__dispatchEvent("show"),this)},showModal:function(){return this.modal=!0,this.show.apply(this,arguments)},close:function(a){return!this.destroyed&&this.open&&(void 0!==a&&(this.returnValue=a),this.__popup.hide().removeClass(this.className+"-show"),this.__backdrop.hide(),this.open=!1,this.blur(),this.__dispatchEvent("close")),this},remove:function(){if(this.destroyed)return this;this.__dispatchEvent("beforeremove"),f.current===this&&(f.current=null),this.__unlock(),this.__popup.remove(),this.__backdrop.remove(),this.blur(),d||b(window).off("resize",this.__onresize),this.__dispatchEvent("remove");for(var a in this)delete this[a];return this},reset:function(){var a=this.follow;return a?this.__follow(a):this.__center(),this.__dispatchEvent("reset"),this},focus:function(){var d,a=this.node,c=f.current;return c&&c!==this&&c.blur(!1),b.contains(a,this.__getActive())||(d=this.__popup.find("[autofocus]")[0],!this._autofocus&&d?this._autofocus=!0:d=a,this.__focus(d)),f.current=this,this.__popup.addClass(this.className+"-focus"),this.__zIndex(),this.__dispatchEvent("focus"),this},blur:function(){var a=this.__activeElement,b=arguments[0];return b!==!1&&this.__focus(a),this._autofocus=!1,this.__popup.removeClass(this.className+"-focus"),this.__dispatchEvent("blur"),this},addEventListener:function(a,b){return this.__getEventListener(a).push(b),this},removeEventListener:function(a,b){var d,c=this.__getEventListener(a);for(d=0;d<c.length;d++)b===c[d]&&c.splice(d--,1);return this},__getEventListener:function(a){var b=this.__listener;return b||(b=this.__listener={}),b[a]||(b[a]=[]),b[a]},__dispatchEvent:function(a){var c,b=this.__getEventListener(a);for(this["on"+a]&&this["on"+a](),c=0;c<b.length;c++)b[c].call(this)},__focus:function(a){try{this.autofocus&&!/^iframe$/i.test(a.nodeName)&&a.focus()}catch(b){}},__getActive:function(){var a,b,c;try{return a=document.activeElement,b=a.contentDocument,c=b&&b.activeElement||a}catch(d){}},__zIndex:function(){var a=f.zIndex++;this.__popup.css("zIndex",a),this.__backdrop.css("zIndex",a-1),this.zIndex=a},__center:function(){var a=this.__popup,c=b(window),d=b(document),e=this.fixed,f=e?0:d.scrollLeft(),g=e?0:d.scrollTop(),h=c.width(),i=c.height(),j=a.width(),k=a.height(),l=(h-j)/2+f,m=382*(i-k)/1e3+g,n=a[0].style;n.left=Math.max(parseInt(l),f)+"px",n.top=Math.max(parseInt(m),g)+"px"},__follow:function(a){var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,c=a.parentNode&&b(a),d=this.__popup;return this.__followSkin&&d.removeClass(this.__followSkin),c&&(e=c.offset(),e.left*e.top<0)?this.__center():(f=this,g=this.fixed,h=b(window),i=b(document),j=h.width(),k=h.height(),l=i.scrollLeft(),m=i.scrollTop(),n=d.width(),o=d.height(),p=c?c.outerWidth():0,q=c?c.outerHeight():0,r=this.__offset(a),s=r.left,t=r.top,u=g?s-l:s,v=g?t-m:t,w=g?0:l,x=g?0:m,y=w+j-n,z=x+k-o,A={},B=this.align.split(" "),C=this.className+"-",D={top:"bottom",bottom:"top",left:"right",right:"left"},E={top:"top",bottom:"top",left:"left",right:"left"},F=[{top:v-o,bottom:v+q,left:u-n,right:u+p},{top:v,bottom:v-o+q,left:u,right:u-n+p}],G={left:u+p/2-n/2,top:v+q/2-o/2},H={left:[w,y],top:[x,z]},b.each(B,function(a,b){F[a][b]>H[E[b]][1]&&(b=B[a]=D[b]),F[a][b]<H[E[b]][0]&&(B[a]=D[b])}),B[1]||(E[B[1]]="left"===E[B[0]]?"top":"left",F[1][B[1]]=G[E[B[1]]]),C+=B.join("-"),f.__followSkin=C,c&&d.addClass(C),A[E[B[0]]]=parseInt(F[0][B[0]]),A[E[B[1]]]=parseInt(F[1][B[1]]),d.css(A),void 0)},__offset:function(a){var e,f,g,h,i,j,k,l,m,c=a.parentNode,d=c?b(a).offset():{left:a.pageX,top:a.pageY};return a=c?a:a.target,e=a.ownerDocument,f=e.defaultView||e.parentWindow,f==window?d:(g=f.frameElement,h=b(e),i=h.scrollLeft(),j=h.scrollTop(),k=b(g).offset(),l=k.left,m=k.top,{left:d.left+l-i,top:d.top+m-j})},__lock:function(){var a=this,c=this.__popup,d=this.__backdrop,g={position:"fixed",left:0,top:0,width:"100%",height:"100%",overflow:"hidden",userSelect:"none",opacity:0,background:this.backdropBackground};c.addClass(this.className+"-modal"),f.zIndex=f.zIndex+2,this.__zIndex(),e||b.extend(g,{position:"absolute",width:b(window).width()+"px",height:b(document).height()+"px"}),d.css(g).animate({opacity:this.backdropOpacity},150).insertAfter(c).attr({tabindex:"0"}).on("focus",function(){a.focus()})},__unlock:function(){this.modal&&(this.__popup.removeClass(this.className+"-modal"),this.__backdrop.remove(),delete this.modal)}}),f.zIndex=1024,f.current=null,f}),c("dialog-config",{content:'<span class="ui-dialog-loading">Loading..</span>',title:"",statusbar:"",button:null,ok:null,cancel:null,okValue:"ok",cancelValue:"cancel",cancelDisplay:!0,width:"",height:"",padding:"",skin:"",quickClose:!1,cssUri:"../css/ui-dialog.css",innerHTML:'<div i="dialog" class="ui-dialog"><div class="ui-dialog-arrow-a"></div><div class="ui-dialog-arrow-b"></div><div class="ui-dialog-grid"><div i="header" class="ui-dialog-header"><div i="title" class="ui-dialog-title"></div></div><div i="body" class="ui-dialog-body"><div i="content" class="ui-dialog-content"></div></div><div i="footer" class="ui-dialog-footer"><div i="statusbar" class="ui-dialog-statusbar"></div><div i="button" class="ui-dialog-button"></div></div></div></div>'}),c("dialog",function(a){var f,g,h,i,j,k,l,m,n,b=a("jquery"),c=a("popup"),d=a("dialog-config"),e=d.cssUri;return e&&(f=a[a.toUrl?"toUrl":"resolve"],f&&(e=f(e),e='<link rel="stylesheet" href="'+e+'" />',b("base")[0]?b("base").before(e):b("head").append(e))),g=0,h=new Date-0,i=!("minWidth"in b("html")[0].style),j="createTouch"in document&&!("onmousemove"in document)||/(iPhone|iPad|iPod)/i.test(navigator.userAgent),k=!i&&!j,l=function(a,c,d){var f,i,e=a=a||{};return("string"==typeof a||1===a.nodeType)&&(a={content:a,fixed:!j}),a=b.extend(!0,{},l.defaults,a),a._=e,f=a.id=a.id||h+g,(i=l.get(f))?i.focus():(k||(a.fixed=!1),a.quickClose&&(a.modal=!0,e.backdropOpacity||(a.backdropOpacity=0)),b.isArray(a.button)||(a.button=[]),void 0!==d&&(a.cancel=d),a.cancel&&a.button.push({id:"cancel",value:a.cancelValue,callback:a.cancel,display:a.cancelDisplay}),void 0!==c&&(a.ok=c),a.ok&&a.button.push({id:"ok",value:a.okValue,callback:a.ok,autofocus:!0}),l.list[f]=new l.create(a))},m=function(){},m.prototype=c.prototype,n=l.prototype=new m,l.create=function(a){var e,d=this;return b.extend(this,new c),e=b(this.node).html(a.innerHTML),this.options=a,this._popup=e,b.each(a,function(a,b){"function"==typeof d[a]?d[a](b):d[a]=b}),a.zIndex&&(c.zIndex=a.zIndex),e.attr({"aria-labelledby":this._$("title").attr("id","title:"+this.id).attr("id"),"aria-describedby":this._$("content").attr("id","content:"+this.id).attr("id")}),this._$("close").css("display",this.cancel===!1?"none":"").attr("title",this.cancelValue).on("click",function(a){d._trigger("cancel"),a.preventDefault()}),this._$("dialog").addClass(this.skin),this._$("body").css("padding",this.padding),e.on("click","[data-id]",function(a){var c=b(this);c.attr("disabled")||d._trigger(c.data("id")),a.preventDefault()}),a.quickClose&&b(this.backdrop).on("onmousedown"in document?"mousedown":"click",function(){d._trigger("cancel")}),this._esc=function(a){var b=a.target,e=b.nodeName,f=/^input|textarea$/i,g=c.current===d,h=a.keyCode;!g||f.test(e)&&"button"!==b.type||27===h&&d._trigger("cancel")},b(document).on("keydown",this._esc),this.addEventListener("remove",function(){b(document).off("keydown",this._esc),delete l.list[this.id]}),g++,l.oncreate(this),this},l.create.prototype=n,b.extend(n,{content:function(a){return this._$("content").empty("")["object"==typeof a?"append":"html"](a),this.reset()},title:function(a){return this._$("title").text(a),this._$("header")[a?"show":"hide"](),this},width:function(a){return this._$("content").css("width",a),this.reset()},height:function(a){return this._$("content").css("height",a),this.reset()},button:function(a){var c,d,e;return a=a||[],c=this,d="",e=0,this.callbacks={},"string"==typeof a?d=a:b.each(a,function(a,b){b.id=b.id||b.value,c.callbacks[b.id]=b.callback;var f="";b.display===!1?f=' style="display:none"':e++,d+='<button type="button" data-id="'+b.id+'"'+f+(b.disabled?" disabled":"")+(b.autofocus?' autofocus class="ui-dialog-autofocus"':"")+">"+b.value+"</button>"}),this._$("footer")[e?"show":"hide"](),this._$("button").html(d),this},statusbar:function(a){return this._$("statusbar").html(a)[a?"show":"hide"](),this},_$:function(a){return this._popup.find("[i="+a+"]")},_trigger:function(a){var b=this.callbacks[a];return"function"!=typeof b||b.call(this)!==!1?this.close().remove():this}}),l.oncreate=b.noop,l.getCurrent=function(){return c.current},l.get=function(a){return void 0===a?l.list:l.list[a]},l.list={},l.defaults=d,l}),c("drag",function(a){var b=a("jquery"),c=b(window),d=b(document),e="createTouch"in document,f=document.documentElement,g=!("minWidth"in f.style),h=!g&&"onlosecapture"in f,i="setCapture"in f,j={start:e?"touchstart":"mousedown",over:e?"touchmove":"mousemove",end:e?"touchend":"mouseup"},k=e?function(a){return a.touches||(a=a.originalEvent.touches.item(0)),a}:function(a){return a},l=function(){this.start=b.proxy(this.start,this),this.over=b.proxy(this.over,this),this.end=b.proxy(this.end,this),this.onstart=this.onover=this.onend=b.noop};return l.types=j,l.prototype={start:function(a){return a=this.startFix(a),d.on(j.over,this.over).on(j.end,this.end),this.onstart(a),!1},over:function(a){return a=this.overFix(a),this.onover(a),!1},end:function(a){return a=this.endFix(a),d.off(j.over,this.over).off(j.end,this.end),this.onend(a),!1},startFix:function(a){return a=k(a),this.target=b(a.target),this.selectstart=function(){return!1},d.on("selectstart",this.selectstart).on("dblclick",this.end),h?this.target.on("losecapture",this.end):c.on("blur",this.end),i&&this.target[0].setCapture(),a},overFix:function(a){return a=k(a)},endFix:function(a){return a=k(a),d.off("selectstart",this.selectstart).off("dblclick",this.end),h?this.target.off("losecapture",this.end):c.off("blur",this.end),i&&this.target[0].releaseCapture(),a}},l.create=function(a,e){var k,m,n,o,f=b(a),g=new l,h=l.types.start,i=function(){},j=a.className.replace(/^\s|\s.*/g,"")+"-drag-start",p={onstart:i,onover:i,onend:i,off:function(){f.off(h,g.start)}};return g.onstart=function(b){var q,r,s,e="fixed"===f.css("position"),g=d.scrollLeft(),h=d.scrollTop(),i=f.width(),l=f.height();k=0,m=0,n=e?c.width()-i+k:d.width()-i,o=e?c.height()-l+m:d.height()-l,q=f.offset(),r=this.startLeft=e?q.left-g:q.left,s=this.startTop=e?q.top-h:q.top,this.clientX=b.clientX,this.clientY=b.clientY,f.addClass(j),p.onstart.call(a,b,r,s)},g.onover=function(b){var c=b.clientX-this.clientX+this.startLeft,d=b.clientY-this.clientY+this.startTop,e=f[0].style;c=Math.max(k,Math.min(n,c)),d=Math.max(m,Math.min(o,d)),e.left=c+"px",e.top=d+"px",p.onover.call(a,b,c,d)},g.onend=function(b){var c=f.position(),d=c.left,e=c.top;f.removeClass(j),p.onend.call(a,b,d,e)},g.off=function(){f.off(h,g.start)},e?g.start(e):f.on(h,g.start),p},l}),c("dialog-plus",function(a){var b=a("jquery"),c=a("dialog"),d=a("drag");return c.oncreate=function(a){var h,i,j,c=a.options,e=c._,f=c.url,g=c.oniframeload;if(f&&(this.padding=c.padding=0,h=b("<iframe />"),h.attr({src:f,name:a.id,width:"100%",height:"100%",allowtransparency:"yes",frameborder:"no",scrolling:"no"}).on("load",function(){var b;try{b=h[0].contentWindow.frameElement}catch(d){}b&&(c.width||a.width(h.contents().width()),c.height||a.height(h.contents().height())),g&&g.call(a)}),a.addEventListener("beforeremove",function(){h.attr("src","about:blank").remove()},!1),a.content(h[0]),a.iframeNode=h[0]),!(e instanceof Object))for(i=function(){a.close().remove()},j=0;j<frames.length;j++)try{if(e instanceof frames[j].Object){b(frames[j]).one("unload",i);break}}catch(k){}b(a.node).on(d.types.start,"[i=title]",function(b){a.follow||(a.focus(),d.create(a.node,b))})},c.get=function(a){var b,d,e,f;if(a&&a.frameElement){b=a.frameElement,d=c.list;for(f in d)if(e=d[f],e.node.getElementsByTagName("iframe")[0]===b)return e}else if(a)return c.list[a]},c}),window.dialog=b("dialog-plus")}();
/**
 * 
 */
//限制輸入數字
function NumberInput(el){
	var $e ;
	if(el instanceof Element){
		$e = $(el);
	}else if(typeof el === "string"){
		$e = $('#'+el);
		if(!$e.get(0))return;
	}else{
		return;
	}
	
	
	$e.keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) ||
             // Allow: Ctrl+C
            (e.keyCode == 67 && e.ctrlKey === true) ||
             // Allow: Ctrl+X
            (e.keyCode == 88 && e.ctrlKey === true) ||
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
}

//判断Mobile装置
function isMobile(){
	return (/AppleWebKit.*Mobile/i.test(navigator.userAgent) 
		|| /Android/i.test(navigator.userAgent) 
		|| /BlackBerry/i.test(navigator.userAgent) 
		|| /IEMobile/i.test(navigator.userAgent) 
		|| (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent)));
}

function getMobileKind(){
	if(navigator.userAgent.match(/Android/i))
		return 'Android';
	if(navigator.userAgent.match(/iPhone/i)
	 || navigator.userAgent.match(/iPad/i)
	 || navigator.userAgent.match(/iPod/i))
		return 'IOS';
	if(navigator.userAgent.match(/Windows Phone/i))
		return 'Windows Phone';
	return 'other';
}


/**
 * 延迟不重复执行相同action
 * @param {string} name 动作名称
 * @param {int} delay 延迟时间
 * @param {function} fn 执行的方法
 * 
 */
var delayAction = (function(){
	var _timers = {};
	return function(name,delay,fn){
		//实际执行
		function excute(){
			fn();
			delete _timers[name];
		}
		return (function(){
			if(_timers[name]){
				clearTimeout(_timers[name]);
				_timers[name] = setTimeout(excute,delay);
				return;
			}
			_timers[name] = setTimeout(excute,delay);
		})();
	};
})();


/**
 * String format
 */
String.format = function() {
    var theString = arguments[0];

    for (var i = 1; i < arguments.length; i++) {
        var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
        theString = theString.replace(regEx, arguments[i]);
    }
    return theString;
};

/**
 * Date format
 */
Date.replaceChars = {
    shortMonths: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    longMonths: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
    shortDays: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    longDays: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],

    // Day
    d: function() { return (this.getDate() < 10 ? '0' : '') + this.getDate(); },
    D: function() { return Date.replaceChars.shortDays[this.getDay()]; },
    j: function() { return this.getDate(); },
    l: function() { return Date.replaceChars.longDays[this.getDay()]; },
    N: function() { return this.getDay() + 1; },
    S: function() { return (this.getDate() % 10 == 1 && this.getDate() != 11 ? 'st' : (this.getDate() % 10 == 2 && this.getDate() != 12 ? 'nd' : (this.getDate() % 10 == 3 && this.getDate() != 13 ? 'rd' : 'th'))); },
    w: function() { return this.getDay(); },
    z: function() { var d = new Date(this.getFullYear(),0,1); return Math.ceil((this - d) / 86400000); }, // Fixed now
    // Week
    W: function() { var d = new Date(this.getFullYear(), 0, 1); return Math.ceil((((this - d) / 86400000) + d.getDay() + 1) / 7); }, // Fixed now
    // Month
    F: function() { return Date.replaceChars.longMonths[this.getMonth()]; },
    m: function() { return (this.getMonth() < 9 ? '0' : '') + (this.getMonth() + 1); },
    M: function() { return Date.replaceChars.shortMonths[this.getMonth()]; },
    n: function() { return this.getMonth() + 1; },
    t: function() { var d = new Date(); return new Date(d.getFullYear(), d.getMonth(), 0).getDate() }, // Fixed now, gets #days of date
    // Year
    L: function() { var year = this.getFullYear(); return (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)); },   // Fixed now
    o: function() { var d  = new Date(this.valueOf());  d.setDate(d.getDate() - ((this.getDay() + 6) % 7) + 3); return d.getFullYear();}, //Fixed now
    Y: function() { return this.getFullYear(); },
    y: function() { return ('' + this.getFullYear()).substr(2); },
    // Time
    a: function() { return this.getHours() < 12 ? 'am' : 'pm'; },
    A: function() { return this.getHours() < 12 ? 'AM' : 'PM'; },
    B: function() { return Math.floor((((this.getUTCHours() + 1) % 24) + this.getUTCMinutes() / 60 + this.getUTCSeconds() / 3600) * 1000 / 24); }, // Fixed now
    g: function() { return this.getHours() % 12 || 12; },
    G: function() { return this.getHours(); },
    h: function() { return ((this.getHours() % 12 || 12) < 10 ? '0' : '') + (this.getHours() % 12 || 12); },
    H: function() { return (this.getHours() < 10 ? '0' : '') + this.getHours(); },
    i: function() { return (this.getMinutes() < 10 ? '0' : '') + this.getMinutes(); },
    s: function() { return (this.getSeconds() < 10 ? '0' : '') + this.getSeconds(); },
    u: function() { var m = this.getMilliseconds(); return (m < 10 ? '00' : (m < 100 ?'0' : '')) + m; },
    // Timezone
    e: function() { return "Not Yet Supported"; },
    I: function() {
        var DST = null;
        for (var i = 0; i < 12; ++i) {
            var d = new Date(this.getFullYear(), i, 1);
            var offset = d.getTimezoneOffset();

            if (DST === null) DST = offset;
            else if (offset < DST) { DST = offset; break; }                     else if (offset > DST) break;
        }
        return (this.getTimezoneOffset() == DST) | 0;
    },
    O: function() { return (-this.getTimezoneOffset() < 0 ? '-' : '+') + (Math.abs(this.getTimezoneOffset() / 60) < 10 ? '0' : '') + (Math.abs(this.getTimezoneOffset() / 60)) + '00'; },
    P: function() { return (-this.getTimezoneOffset() < 0 ? '-' : '+') + (Math.abs(this.getTimezoneOffset() / 60) < 10 ? '0' : '') + (Math.abs(this.getTimezoneOffset() / 60)) + ':00'; }, // Fixed now
    T: function() { var m = this.getMonth(); this.setMonth(0); var result = this.toTimeString().replace(/^.+ \(?([^\)]+)\)?$/, '$1'); this.setMonth(m); return result;},
    Z: function() { return -this.getTimezoneOffset() * 60; },
    // Full Date/Time
    c: function() { return this.format("Y-m-d\\TH:i:sP"); }, // Fixed now
    r: function() { return this.toString(); },
    U: function() { return this.getTime() / 1000; }
};

Date.prototype.format = function(format) {
    var returnStr = '';
    var replace = Date.replaceChars;
    for (var i = 0; i < format.length; i++) {       var curChar = format.charAt(i);         if (i - 1 >= 0 && format.charAt(i - 1) == "\\") {
        returnStr += curChar;
    }
    else if (replace[curChar]) {
        returnStr += replace[curChar].call(this);
    } else if (curChar != "\\"){
        returnStr += curChar;
    }
    }
    return returnStr;
};


if(!String.format){
    String.format = function() {
        var theString = arguments[0];

        for (var i = 1; i < arguments.length; i++) {
            var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
            theString = theString.replace(regEx, arguments[i]);
        }
        return theString;
    };
}


/**
 * jquery form to json map
 * @returns {{}}
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

 