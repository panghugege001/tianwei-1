/*! CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
 ============================================================ */
+function(a){"use strict";function b(){var c,a=document.createElement("bootstrap"),b={WebkitTransition:"webkitTransitionEnd",MozTransition:"transitionend",OTransition:"oTransitionEnd otransitionend",transition:"transitionend"};for(c in b)if(void 0!==a.style[c])return{end:b[c]};return!1}a.fn.emulateTransitionEnd=function(b){var e,c=!1,d=this;return a(this).one("bsTransitionEnd",function(){c=!0}),e=function(){c||a(d).trigger(a.support.transition.end)},setTimeout(e,b),this},a(function(){a.support.transition=b(),a.support.transition&&(a.event.special.bsTransitionEnd={bindType:a.support.transition.end,delegateType:a.support.transition.end,handle:function(b){return a(b.target).is(this)?b.handleObj.handler.apply(this,arguments):void 0}})})}(jQuery);


/*
 * ========================================================================
 * Bootstrap: tab.js v3.3.5 http://getbootstrap.com/javascript/#tabs
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc. Licensed under MIT
 * (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ========================================================================
 */   

+function(a){"use strict";function c(c){return this.each(function(){var d=a(this),e=d.data("bs.tab");e||d.data("bs.tab",e=new b(this)),"string"==typeof c&&e[c]()})}var d,e,b=function(b){this.element=a(b)};b.VERSION="3.3.5",b.TRANSITION_DURATION=150,b.prototype.show=function(){var e,f,g,h,b=this.element,c=b.closest("ul:not(.dropdown-menu)"),d=b.data("target");d||(d=b.attr("href"),d=d&&d.replace(/.*(?=#[^\s]*$)/,"")),b.parent("li").hasClass("active")||(e=c.find(".active:last a"),f=a.Event("hide.bs.tab",{relatedTarget:b[0]}),g=a.Event("show.bs.tab",{relatedTarget:e[0]}),e.trigger(f),b.trigger(g),g.isDefaultPrevented()||f.isDefaultPrevented()||(h=a(d),this.activate(b.closest("li"),c),this.activate(h,h.parent(),function(){e.trigger({type:"hidden.bs.tab",relatedTarget:b[0]}),b.trigger({type:"shown.bs.tab",relatedTarget:e[0]})})))},b.prototype.activate=function(c,d,e){function h(){f.removeClass("active").find("> .dropdown-menu > .active").removeClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded",!1),c.addClass("active").find('[data-toggle="tab"]').attr("aria-expanded",!0),g?(c[0].offsetWidth,c.addClass("in")):c.removeClass("fade"),c.parent(".dropdown-menu").length&&c.closest("li.dropdown").addClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded",!0),e&&e()}var f=d.find("> .active"),g=e&&a.support.transition&&(f.length&&f.hasClass("fade")||!!d.find("> .fade").length);f.length&&g?f.one("bsTransitionEnd",h).emulateTransitionEnd(b.TRANSITION_DURATION):h(),f.removeClass("in")},d=a.fn.tab,a.fn.tab=c,a.fn.tab.Constructor=b,a.fn.tab.noConflict=function(){return a.fn.tab=d,this},e=function(b){b.preventDefault(),c.call(a(this),"show")},a(document).on("click.bs.tab.data-api",'[data-toggle="tab"]',e).on("click.bs.tab.data-api",'[data-toggle="pill"]',e)}(jQuery);


/*
 * ========================================================================
 * Bootstrap: modal.js v3.3.5 http://getbootstrap.com/javascript/#modals
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc. Licensed under MIT
 * (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ========================================================================
 */

+function(a){"use strict";function c(c,d){return this.each(function(){var e=a(this),f=e.data("bs.modal"),g=a.extend({},b.DEFAULTS,e.data(),"object"==typeof c&&c);f||e.data("bs.modal",f=new b(this,g)),"string"==typeof c?f[c](d):g.show&&f.show(d)})}var d,b=function(b,c){this.options=c,this.$body=a(document.body),this.$element=a(b),this.$dialog=this.$element.find(".modal-dialog"),this.$backdrop=null,this.isShown=null,this.originalBodyPad=null,this.scrollbarWidth=0,this.ignoreBackdropClick=!1,this.options.remote&&this.$element.find(".modal-content").load(this.options.remote,a.proxy(function(){this.$element.trigger("loaded.bs.modal")},this))};b.VERSION="3.3.5",b.TRANSITION_DURATION=300,b.BACKDROP_TRANSITION_DURATION=150,b.DEFAULTS={backdrop:!0,keyboard:!0,show:!0},b.prototype.toggle=function(a){return this.isShown?this.hide():this.show(a)},b.prototype.show=function(c){var d=this,e=a.Event("show.bs.modal",{relatedTarget:c});this.$element.trigger(e),this.isShown||e.isDefaultPrevented()||(this.isShown=!0,this.checkScrollbar(),this.setScrollbar(),this.$body.addClass("modal-open"),this.escape(),this.resize(),this.$element.on("click.dismiss.bs.modal",'[data-dismiss="modal"]',a.proxy(this.hide,this)),this.$dialog.on("mousedown.dismiss.bs.modal",function(){d.$element.one("mouseup.dismiss.bs.modal",function(b){a(b.target).is(d.$element)&&(d.ignoreBackdropClick=!0)})}),this.backdrop(function(){var f,e=a.support.transition&&d.$element.hasClass("fade");d.$element.parent().length||d.$element.appendTo(d.$body),d.$element.show().scrollTop(0),d.adjustDialog(),e&&d.$element[0].offsetWidth,d.$element.addClass("in"),d.enforceFocus(),f=a.Event("shown.bs.modal",{relatedTarget:c}),e?d.$dialog.one("bsTransitionEnd",function(){d.$element.trigger("focus").trigger(f)}).emulateTransitionEnd(b.TRANSITION_DURATION):d.$element.trigger("focus").trigger(f)}))},b.prototype.hide=function(c){c&&c.preventDefault(),c=a.Event("hide.bs.modal"),this.$element.trigger(c),this.isShown&&!c.isDefaultPrevented()&&(this.isShown=!1,this.escape(),this.resize(),a(document).off("focusin.bs.modal"),this.$element.removeClass("in").off("click.dismiss.bs.modal").off("mouseup.dismiss.bs.modal"),this.$dialog.off("mousedown.dismiss.bs.modal"),a.support.transition&&this.$element.hasClass("fade")?this.$element.one("bsTransitionEnd",a.proxy(this.hideModal,this)).emulateTransitionEnd(b.TRANSITION_DURATION):this.hideModal())},b.prototype.enforceFocus=function(){a(document).off("focusin.bs.modal").on("focusin.bs.modal",a.proxy(function(a){this.$element[0]===a.target||this.$element.has(a.target).length||this.$element.trigger("focus")},this))},b.prototype.escape=function(){this.isShown&&this.options.keyboard?this.$element.on("keydown.dismiss.bs.modal",a.proxy(function(a){27==a.which&&this.hide()},this)):this.isShown||this.$element.off("keydown.dismiss.bs.modal")},b.prototype.resize=function(){this.isShown?a(window).on("resize.bs.modal",a.proxy(this.handleUpdate,this)):a(window).off("resize.bs.modal")},b.prototype.hideModal=function(){var a=this;this.$element.hide(),this.backdrop(function(){a.$body.removeClass("modal-open"),a.resetAdjustments(),a.resetScrollbar(),a.$element.trigger("hidden.bs.modal")})},b.prototype.removeBackdrop=function(){this.$backdrop&&this.$backdrop.remove(),this.$backdrop=null},b.prototype.backdrop=function(c){var f,g,d=this,e=this.$element.hasClass("fade")?"fade":"";if(this.isShown&&this.options.backdrop){if(f=a.support.transition&&e,this.$backdrop=a(document.createElement("div")).addClass("modal-backdrop "+e).appendTo(this.$body),this.$element.on("click.dismiss.bs.modal",a.proxy(function(a){return this.ignoreBackdropClick?(this.ignoreBackdropClick=!1,void 0):(a.target===a.currentTarget&&("static"==this.options.backdrop?this.$element[0].focus():this.hide()),void 0)},this)),f&&this.$backdrop[0].offsetWidth,this.$backdrop.addClass("in"),!c)return;f?this.$backdrop.one("bsTransitionEnd",c).emulateTransitionEnd(b.BACKDROP_TRANSITION_DURATION):c()}else!this.isShown&&this.$backdrop?(this.$backdrop.removeClass("in"),g=function(){d.removeBackdrop(),c&&c()},a.support.transition&&this.$element.hasClass("fade")?this.$backdrop.one("bsTransitionEnd",g).emulateTransitionEnd(b.BACKDROP_TRANSITION_DURATION):g()):c&&c()},b.prototype.handleUpdate=function(){this.adjustDialog()},b.prototype.adjustDialog=function(){var a=this.$element[0].scrollHeight>document.documentElement.clientHeight;this.$element.css({paddingLeft:!this.bodyIsOverflowing&&a?this.scrollbarWidth:"",paddingRight:this.bodyIsOverflowing&&!a?this.scrollbarWidth:""})},b.prototype.resetAdjustments=function(){this.$element.css({paddingLeft:"",paddingRight:""})},b.prototype.checkScrollbar=function(){var b,a=window.innerWidth;a||(b=document.documentElement.getBoundingClientRect(),a=b.right-Math.abs(b.left)),this.bodyIsOverflowing=document.body.clientWidth<a,this.scrollbarWidth=this.measureScrollbar()},b.prototype.setScrollbar=function(){var a=parseInt(this.$body.css("padding-right")||0,10);this.originalBodyPad=document.body.style.paddingRight||"",this.bodyIsOverflowing&&this.$body.css("padding-right",a+this.scrollbarWidth)},b.prototype.resetScrollbar=function(){this.$body.css("padding-right",this.originalBodyPad)},b.prototype.measureScrollbar=function(){var b,a=document.createElement("div");return a.className="modal-scrollbar-measure",this.$body.append(a),b=a.offsetWidth-a.clientWidth,this.$body[0].removeChild(a),b},d=a.fn.modal,a.fn.modal=c,a.fn.modal.Constructor=b,a.fn.modal.noConflict=function(){return a.fn.modal=d,this},a(document).on("click.bs.modal.data-api",'[data-toggle="modal"]',function(b){var d=a(this),e=d.attr("href"),f=a(d.attr("data-target")||e&&e.replace(/.*(?=#[^\s]+$)/,"")),g=f.data("bs.modal")?"toggle":a.extend({remote:!/#/.test(e)&&e},f.data(),d.data());d.is("a")&&b.preventDefault(),f.one("show.bs.modal",function(a){a.isDefaultPrevented()||f.one("hidden.bs.modal",function(){d.is(":visible")&&d.trigger("focus")})}),c.call(f,g,this)})}(jQuery);

/*
 * ========================================================================
 * Bootstrap: carousel.js v3.3.5 http://getbootstrap.com/javascript/#carousel
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc. Licensed under MIT
 * (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ========================================================================
 */

+function(a){"use strict";function c(c){return this.each(function(){var d=a(this),e=d.data("bs.carousel"),f=a.extend({},b.DEFAULTS,d.data(),"object"==typeof c&&c),g="string"==typeof c?c:f.slide;e||d.data("bs.carousel",e=new b(this,f)),"number"==typeof c?e.to(c):g?e[g]():f.interval&&e.pause().cycle()})}var d,e,b=function(b,c){this.$element=a(b),this.$indicators=this.$element.find(".carousel-indicators"),this.options=c,this.paused=null,this.sliding=null,this.interval=null,this.$active=null,this.$items=null,this.options.keyboard&&this.$element.on("keydown.bs.carousel",a.proxy(this.keydown,this)),"hover"==this.options.pause&&!("ontouchstart"in document.documentElement)&&this.$element.on("mouseenter.bs.carousel",a.proxy(this.pause,this)).on("mouseleave.bs.carousel",a.proxy(this.cycle,this))};b.VERSION="3.3.5",b.TRANSITION_DURATION=600,b.DEFAULTS={interval:5e3,pause:"hover",wrap:!0,keyboard:!0},b.prototype.keydown=function(a){if(!/input|textarea/i.test(a.target.tagName)){switch(a.which){case 37:this.prev();break;case 39:this.next();break;default:return}a.preventDefault()}},b.prototype.cycle=function(b){return b||(this.paused=!1),this.interval&&clearInterval(this.interval),this.options.interval&&!this.paused&&(this.interval=setInterval(a.proxy(this.next,this),this.options.interval)),this},b.prototype.getItemIndex=function(a){return this.$items=a.parent().children(".item"),this.$items.index(a||this.$active)},b.prototype.getItemForDirection=function(a,b){var e,f,c=this.getItemIndex(b),d="prev"==a&&0===c||"next"==a&&c==this.$items.length-1;return d&&!this.options.wrap?b:(e="prev"==a?-1:1,f=(c+e)%this.$items.length,this.$items.eq(f))},b.prototype.to=function(a){var b=this,c=this.getItemIndex(this.$active=this.$element.find(".item.active"));if(!(a>this.$items.length-1||0>a))return this.sliding?this.$element.one("slid.bs.carousel",function(){b.to(a)}):c==a?this.pause().cycle():this.slide(a>c?"next":"prev",this.$items.eq(a))},b.prototype.pause=function(b){return b||(this.paused=!0),this.$element.find(".next, .prev").length&&a.support.transition&&(this.$element.trigger(a.support.transition.end),this.cycle(!0)),this.interval=clearInterval(this.interval),this},b.prototype.next=function(){return this.sliding?void 0:this.slide("next")},b.prototype.prev=function(){return this.sliding?void 0:this.slide("prev")},b.prototype.slide=function(c,d){var j,k,l,m,e=this.$element.find(".item.active"),f=d||this.getItemForDirection(c,e),g=this.interval,h="next"==c?"left":"right",i=this;return f.hasClass("active")?this.sliding=!1:(j=f[0],k=a.Event("slide.bs.carousel",{relatedTarget:j,direction:h}),this.$element.trigger(k),k.isDefaultPrevented()?void 0:(this.sliding=!0,g&&this.pause(),this.$indicators.length&&(this.$indicators.find(".active").removeClass("active"),l=a(this.$indicators.children()[this.getItemIndex(f)]),l&&l.addClass("active")),m=a.Event("slid.bs.carousel",{relatedTarget:j,direction:h}),a.support.transition&&this.$element.hasClass("slide")?(f.addClass(c),f[0].offsetWidth,e.addClass(h),f.addClass(h),e.one("bsTransitionEnd",function(){f.removeClass([c,h].join(" ")).addClass("active"),e.removeClass(["active",h].join(" ")),i.sliding=!1,setTimeout(function(){i.$element.trigger(m)},0)}).emulateTransitionEnd(b.TRANSITION_DURATION)):(e.removeClass("active"),f.addClass("active"),this.sliding=!1,this.$element.trigger(m)),g&&this.cycle(),this))},d=a.fn.carousel,a.fn.carousel=c,a.fn.carousel.Constructor=b,a.fn.carousel.noConflict=function(){return a.fn.carousel=d,this},e=function(b){var d,g,h,e=a(this),f=a(e.attr("data-target")||(d=e.attr("href"))&&d.replace(/.*(?=#[^\s]+$)/,""));f.hasClass("carousel")&&(g=a.extend({},f.data(),e.data()),h=e.attr("data-slide-to"),h&&(g.interval=!1),c.call(f,g),h&&f.data("bs.carousel").to(h),b.preventDefault())},a(document).on("click.bs.carousel.data-api","[data-slide]",e).on("click.bs.carousel.data-api","[data-slide-to]",e),a(window).on("load",function(){a('[data-ride="carousel"]').each(function(){var b=a(this);c.call(b,b.data())})})}(jQuery);

/*
 * ========================================================================
 * Bootstrap: scrollspy.js v3.3.7 http://getbootstrap.com/javascript/#scrollspy
 * ========================================================================
 * Copyright 2011-2016 Twitter, Inc. Licensed under MIT
 * (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ========================================================================
 */

+function(a){"use strict";function b(c,d){this.$body=a(document.body),this.$scrollElement=a(c).is(document.body)?a(window):a(c),this.options=a.extend({},b.DEFAULTS,d),this.selector=(this.options.target||"")+" .nav li > a",this.offsets=[],this.targets=[],this.activeTarget=null,this.scrollHeight=0,this.$scrollElement.on("scroll.bs.scrollspy",a.proxy(this.process,this)),this.refresh(),this.process()}function c(c){return this.each(function(){var d=a(this),e=d.data("bs.scrollspy"),f="object"==typeof c&&c;e||d.data("bs.scrollspy",e=new b(this,f)),"string"==typeof c&&e[c]()})}b.VERSION="3.3.7",b.DEFAULTS={offset:10},b.prototype.getScrollHeight=function(){return this.$scrollElement[0].scrollHeight||Math.max(this.$body[0].scrollHeight,document.documentElement.scrollHeight)},b.prototype.refresh=function(){var b=this,c="offset",d=0;this.offsets=[],this.targets=[],this.scrollHeight=this.getScrollHeight(),a.isWindow(this.$scrollElement[0])||(c="position",d=this.$scrollElement.scrollTop()),this.$body.find(this.selector).map(function(){var b=a(this),e=b.data("target")||b.attr("href"),f=/^#./.test(e)&&a(e);return f&&f.length&&f.is(":visible")&&[[f[c]().top+d,e]]||null}).sort(function(a,b){return a[0]-b[0]}).each(function(){b.offsets.push(this[0]),b.targets.push(this[1])})},b.prototype.process=function(){var g,a=this.$scrollElement.scrollTop()+this.options.offset,b=this.getScrollHeight(),c=this.options.offset+b-this.$scrollElement.height(),d=this.offsets,e=this.targets,f=this.activeTarget;if(this.scrollHeight!=b&&this.refresh(),a>=c)return f!=(g=e[e.length-1])&&this.activate(g);if(f&&a<d[0])return this.activeTarget=null,this.clear();for(g=d.length;g--;)f!=e[g]&&a>=d[g]&&(void 0===d[g+1]||a<d[g+1])&&this.activate(e[g])},b.prototype.activate=function(b){var c,d;this.activeTarget=b,this.clear(),c=this.selector+'[data-target="'+b+'"],'+this.selector+'[href="'+b+'"]',d=a(c).parents("li").addClass("active"),d.parent(".dropdown-menu").length&&(d=d.closest("li.dropdown").addClass("active")),d.trigger("activate.bs.scrollspy")},b.prototype.clear=function(){a(this.selector).parentsUntil(this.options.target,".active").removeClass("active")};var d=a.fn.scrollspy;a.fn.scrollspy=c,a.fn.scrollspy.Constructor=b,a.fn.scrollspy.noConflict=function(){return a.fn.scrollspy=d,this},a(window).on("load.bs.scrollspy.data-api",function(){a('[data-spy="scroll"]').each(function(){var b=a(this);c.call(b,b.data())})})}(jQuery);

/*
 * ========================================================================
 * Bootstrap: affix.js v3.3.7 http://getbootstrap.com/javascript/#affix
 * ========================================================================
 * Copyright 2011-2016 Twitter, Inc. Licensed under MIT
 * (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ========================================================================
 */
+function(a){"use strict";function c(c){return this.each(function(){var d=a(this),e=d.data("bs.affix"),f="object"==typeof c&&c;e||d.data("bs.affix",e=new b(this,f)),"string"==typeof c&&e[c]()})}var d,b=function(c,d){this.options=a.extend({},b.DEFAULTS,d),this.$target=a(this.options.target).on("scroll.bs.affix.data-api",a.proxy(this.checkPosition,this)).on("click.bs.affix.data-api",a.proxy(this.checkPositionWithEventLoop,this)),this.$element=a(c),this.affixed=null,this.unpin=null,this.pinnedOffset=null,this.checkPosition()};b.VERSION="3.3.7",b.RESET="affix affix-top affix-bottom",b.DEFAULTS={offset:0,target:window},b.prototype.getState=function(a,b,c,d){var h,i,j,e=this.$target.scrollTop(),f=this.$element.offset(),g=this.$target.height();return null!=c&&"top"==this.affixed?c>e?"top":!1:"bottom"==this.affixed?null!=c?e+this.unpin<=f.top?!1:"bottom":a-d>=e+g?!1:"bottom":(h=null==this.affixed,i=h?e:f.top,j=h?g:b,null!=c&&c>=e?"top":null!=d&&i+j>=a-d?"bottom":!1)},b.prototype.getPinnedOffset=function(){var a,c;return this.pinnedOffset?this.pinnedOffset:(this.$element.removeClass(b.RESET).addClass("affix"),a=this.$target.scrollTop(),c=this.$element.offset(),this.pinnedOffset=c.top-a)},b.prototype.checkPositionWithEventLoop=function(){setTimeout(a.proxy(this.checkPosition,this),1)},b.prototype.checkPosition=function(){var c,d,e,f,g,h,i,j;if(this.$element.is(":visible")){if(c=this.$element.height(),d=this.options.offset,e=d.top,f=d.bottom,g=Math.max(a(document).height(),a(document.body).height()),"object"!=typeof d&&(f=e=d),"function"==typeof e&&(e=d.top(this.$element)),"function"==typeof f&&(f=d.bottom(this.$element)),h=this.getState(g,c,e,f),this.affixed!=h){if(null!=this.unpin&&this.$element.css("top",""),i="affix"+(h?"-"+h:""),j=a.Event(i+".bs.affix"),this.$element.trigger(j),j.isDefaultPrevented())return;this.affixed=h,this.unpin="bottom"==h?this.getPinnedOffset():null,this.$element.removeClass(b.RESET).addClass(i).trigger(i.replace("affix","affixed")+".bs.affix")}"bottom"==h&&this.$element.offset({top:g-c-f})}},d=a.fn.affix,a.fn.affix=c,a.fn.affix.Constructor=b,a.fn.affix.noConflict=function(){return a.fn.affix=d,this},a(window).on("load",function(){a('[data-spy="affix"]').each(function(){var b=a(this),d=b.data();d.offset=d.offset||{},null!=d.offsetBottom&&(d.offset.bottom=d.offsetBottom),null!=d.offsetTop&&(d.offset.top=d.offsetTop),c.call(b,d)})})}(jQuery);


/**
 * 工具函数
 */
!function(window,$){
    var Util=window.Util||{};

    /**
	 * 获取url参数值
	 * 
	 * @param name
	 * @param url
	 * @returns {*}
	 */
    Util.getQueryString=function (name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    root = typeof exports !== "undefined" && exports !== null ? exports : window;

    root.Util = Util;
}(window);

/**
 * 页面初始化函数
 */
!function(window){
    var Page=window.Page||{},
        $window=$(window);

    /**
	 * 无缝滚动
	 * 
	 * @param target
	 *            操作id
	 * @param sp
	 *            速度
	 * @constructor
	 */
    Page.Marquee=function(target,sp,direction){
        var $container = (typeof target === 'string') ? $(target) : target,
            container=$container[0],
            $marqueeItem = $container.find('[data-marquee-item]'),
            $last = $container.find('[data-marquee-item]:last'),

            speed = sp || 10,
            dir=direction || 'top';

        if($last.length){
            var last=$last[0],
                rolling;

            if(dir=='top'){
                $marqueeItem.clone().appendTo($container);
                var height=last.offsetTop+last.offsetHeight;
                rolling = function(){
                    if(container.scrollTop == height){
                        container.scrollTop = 0;
                    }else{
                        container.scrollTop++;
                    }
                };
            }else if(dir=='right'){
                $marqueeItem.clone().appendTo($container.find('ul'));
                rolling = function(){
                    if(container.scrollLeft == last.offsetLeft){
                        container.scrollLeft = 0;
                    }else{
                        container.scrollLeft++;
                    }
                }
            }


            var timer = setInterval(rolling,speed);// 设置定时器
            $container.on('mouseenter',function(){
                clearInterval(timer);
            });
            $container.on('mouseleave',function() {
                timer=setInterval(rolling,speed);// 鼠标移开时重设定时器
            });
        }
    };

    /**
	 * 动态加载 src 引用
	 * 
	 * @param ele
	 */
    Page.loadSrc=function(ele){
        var $this=$(ele),
            $iframe=$this.find('[data-src]'),
            src=$iframe.data('src');

        src&&$iframe.attr('src',src);
    };

    Page.scrollAtive=function(target,num){
        var $target=(typeof target==='string')? $(target):target,
            top=num||$target.offset().top,
            activeClass=$target.data('animation')||'active';

        if(top>380) top-=380;

        var checkPosition=function(){
            var _diffTop=top-$window.scrollTop();
            _diffTop>0&&_diffTop<$(document).height()?$target.addClass(activeClass):$target.removeClass(activeClass);
        };

        $window.on('scroll',checkPosition);
        $window.on('resize',checkPosition);
    };

    /**
	 * 弹框弹出时的事件,
	 */
    Page.modalEvent=function () {
        var _targets=['#modal-reg','#modal-login'];
        var _showEvent=function(event){
            Page.loadSrc(event.currentTarget);
        };

        $('[data-modal-load]').on('show.bs.modal',_showEvent);
    };


    /**
	 * 侧边栏电话回拨
	 */
    Page.recall=function(){
        var $rebackCall  = $('#j-reback-call');

        $rebackCall.length && $rebackCall.on('click',function(){
            var phonenum = $("#phonenum").val();
            $.post("/asp/makeCall.aspx", {'phonenum':phonenum}, function (data) {
                alert(data);
            });
        });
    };
    /**
	 * 回到顶部
	 */
    Page.gotop=function(){
        var $gotop=$('#j-gotop');

        $gotop.on('click',function () {
            $('html,body').animate({'scrollTop':0},300);
        })
    };
    /**
	 * 收藏网页
	 */
    Page.AddFavorite=function(url,title){
        try {
            window.external.addFavorite(url, title)
        } catch (c) {
            try {
                window.sidebar.addPanel(title, url, "")
            } catch (d) {
                alert("加入收藏失败，请使用Ctrl+D进行添加");
            }
        }
    };
    /**
	 * 设置为主页
	 */
    Page.SetHome=function(url){
        if(document.all) {
            document.body.style.behavior = 'url(#default#homepage)';
            document.body.setHomePage(url);
        }else if (window.sidebar) {
            if (window.netscape){
                try {
                    netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect")
                } catch (err) {
                    alert("此操作被浏览器拒绝！");
                }
                var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
                prefs.setCharPref('browser.startup.homepage',vrl);
            }
        }else {
            alert("此操作被浏览器拒绝！");
        }

    };
    /**
	 * 判断登录状态
	 * 
	 * @returns {boolean}
	 */
    Page.isLogin=function(){
        var username= $('#j-username').val();
        if(username) return true;
        return false;
    };
    /**
	 * 登录
	 * 
	 * @returns {boolean}
	 */
    Page.login=function(ele,frommodal){
        var loginname=$('#j-name2').val(),
            password=$('#j-pwd2').val(),
            $target=$(ele),
            code=$('#j-code2').val();
            var $form=$target.closest("form.ui-form");
         if(frommodal == 'modal'){
         	loginname = $('#j-name').val();
         	password=$('#j-pwd').val();
         	 if($form.length>0){
            loginname =$form.find('input[id="j-name"]').val();
            password=$form.find('input[id="j-pwd"]').val();
        	}
         }else {
         	if($form.length>0){
	            loginname =$form.find('input[id="j-name2"]').val();
	            password=$form.find('input[id="j-pwd2"]').val();
        	}
         }
        if(loginname==""||loginname=="帐 号"){
            alert("账号不能为空！");
            return false;
        }
        if(password==""||password=="密 码"){
            alert("密码不能为空！");
            return false;
        }
        /*
		 * if(code==""||code=="验证码"){ alert("验证码不能为空！"); return false; }
		 */
        $target.prop('disabled',true);
		
		 $.ajax({
			 
			 url: "/asp/generateVerificationCode.aspx?r=" + Math.random(),
            type: "post", // 请求方式
            async: false,
            success: function (response) {
				
				$.post("/asp/login.aspx", {
                "loginname":loginname, "password":password/* ,"imageCode":code */},
            function (returnedData) {
                $target.prop('disabled',false);
                if(returnedData=="SUCCESS"){
                	if(frommodal == 'modal'){
						window.location.reload();
						return false;
                	}
                    if(loginname.substr(0, 2) == "a_"){
                        window.location.href="/agentManage.aspx";
                    }else{
                        window.location='/asp/payPage.aspx?#user-vip';
                    }
					/*
					 * if(model){
					 * 
					 * }else{ window.location.reload(); }
					 */
                }else{
                    $('#j-codeimg').attr('src','/asp/validateCodeForIndex.aspx?r='+Math.random());
                    alert(returnedData);
                    var str2='已被锁';
                    if(returnedData.indexOf(str2)>-1){
                        $('#modal-forget').modal('show');
                    }
                }
            }).fail(function(){
				$target.prop('disabled',false);
				alert('系统繁忙!');
			});
				
				
			}
		 })
		
		
        

        return false;
    };
    /**
	 * topBanner
	 * 
	 * @returns {boolean}
	 */
    Page.topBanner=function(){
        var $topBanner= $('#top-banner');

        $topBanner.find('.close').on('click',function(){
            $topBanner.slideUp('fast');
            return false;
        });

    };
    Page.init=function () {
        Page.modalEvent();
        Page.recall();
        Page.gotop();
        Page.topBanner();

        // 加入收藏
        var $btnAddFavorite=$('#');
        $btnAddFavorite.length && $btnAddFavorite.on('click',Page.AddFavorite(window.location,document.title));

        $('[data-scroll]').each(function () {
            Page.scrollAtive($(this));
        });
    };

    $(function () {
        Page.init();
    });

    root = typeof exports !== "undefined" && exports !== null ? exports : window;

    root.Page = Page;
}(window,jQuery);

// 打开进度条
function openProgressBar(){
    var html=['<div id="ui-loading" style="display: block;">',
            '<div class="ui-overlay"></div>',
            '<div class="ui-load-cnt">加载中...</div>',
            '</div>'].join(''),
        $loading= $('#ui-loading');
    $loading.length===0 && $('body').append(html);
    $loading.show();
}
// 关闭进度条
function closeProgressBar(){
    $('#ui-loading').hide();
}
// 让指定的DIV始终显示在屏幕正中间
function setDivCenter(divName){
    var top = ($(window).height() - $(divName).height())/2;
    var left = ($(window).width() - $(divName).width())/2;
    var scrollTop = $(document).scrollTop();
    var scrollLeft = $(document).scrollLeft();
    $(divName).css( { position : 'absolute', 'top' : top + scrollTop, left : left + scrollLeft } ).show();
}


// 退出系统
function logout(){
    openProgressBar();
    $.post("/asp/logout.aspx", {
    }, function (returnedData, status) {
        if ("success" == status) {
            window.location.href="/";
        }
    });
}
// 签到
function dosign(){
    openProgressBar();
    $.ajax({
        url : "/asp/doSignRecord.aspx",
        type : "post", // 请求方式
        dataType : "text", // 响应的数据类型
        data :"",
        async : true, // 异步
        success : function(msg) {
            closeProgressBar();
           // qiandao();
            alert(msg);
        }
    });
}
// 签到余额
function refreshQdBalance(){
	var gameCode = "qd";
    $.post("/asp/getGameMoney.aspx",
    		{
		"gameCode" : gameCode
	},
        function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $('.u_qdmoney').text(returnedData);
                // $("#j-checkin-money").html(returnedData+"元");
            }
        });
}
// 红包余额
function refreshHbBalance(){
	var gameCode = "redrain";
	$
	.post(
			"/asp/getGameMoney.aspx",
			{
				"gameCode" : gameCode
			},
			function(returnedData, status) {
			     if ("success" == status) {
		                closeProgressBar();
		                $('.u_redmoney').text(returnedData);
		            }
			});
}
// 刷新余额
function refreshBalance(){ 
    $.ajax({
        type: "post",
        url: "/asp/refreshUserBalance.aspx",
        cache: false,
        beforeSend:function() {
            $(".u_money").html("正在刷新..");
        },
        success : function(data){
            $('.j-balance').text(data);
            $('.u_money').text(data);
        },
        error: function(){alert("服务繁忙");}
    });
}
// 获取站内信
function getLetterCount(){
    $.post("/asp/getGuestbookCountNew.aspx", {
    }, function (data) {
        $(".j-letter").html(data);
    });
}
$(function(){
    var userName=$('#j-username').val();
    if(userName){
        getLetterCount();
       // qiandao();
    }


});
