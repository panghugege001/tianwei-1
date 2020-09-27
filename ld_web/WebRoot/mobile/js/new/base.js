/**
 * 全局配置
 * @type {{baseUrl: string, imgCodeUrl: string}}
 */

if(getMobileKind() == "IOS") {
					window.onpageshow = function (e) {
		 if (e.persisted) {
		       window.location.reload(true)
		 } 
}
				}
!function (w) {
    var Global = w.Global || {};
    Global.config = {
        'baseUrl': '/',
        'imgCodeUrl': '/mobi/mobileValidateCode.aspx'
    };
    w.Global = Global;
}(window);

/**
 * 头部rem设计*/
(function (win, lib) {
    var doc = win.document;
    var docEl = doc.documentElement;
    var dpr = 0;
    var tid;
    var isIPhone = win.navigator.appVersion.match(/iphone/gi);
    var devicePixelRatio = win.devicePixelRatio;
    if (isIPhone) {
        if (devicePixelRatio >= 3 && (!dpr || dpr >= 3)) {
            dpr = 3
        } else if (devicePixelRatio >= 2 && (!dpr || dpr >= 2)) {
            dpr = 2
        } else {
            dpr = 1
        }
    } else {
        dpr = 1
    }

    function refreshRem() {
        var width = docEl.getBoundingClientRect().width;
        // if (width / dpr > 540) {
        //     width = 540 * dpr
        // }
        docEl.style.fontSize = (width/10) + 'px';
    }

    win.addEventListener('resize', function () {
        clearTimeout(tid);
        tid = setTimeout(refreshRem, 300)
    }, false);
    win.addEventListener('pageshow', function (e) {
        if (e.persisted) {
            clearTimeout(tid);
            tid = setTimeout(refreshRem, 300)
        }
    }, false);
    refreshRem();
    window.getMobileKind = function () {
        if (navigator.userAgent.match(/Android/i))
            return 'Android';
        if (navigator.userAgent.match(/iPhone/i) ||
            navigator.userAgent.match(/iPad/i) ||
            navigator.userAgent.match(/iPod/i))
            return 'IOS';
        if (navigator.userAgent.match(/Windows Phone/i))
            return 'Windows Phone';
        return 'other';
    }

})(window, (window['lib'] || (window['lib'] = {})));
/*
 * Util
 **/
!function (win, $) {
    var Util = window.Util || {};
    Util.getQueryString = function (name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }
    Util.getCookie = function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }
    Util.setCookie = function (cname, cvalue, time) {
        time = time || 7
        if (typeof cvalue == "undefined" || cvalue == "") {
            return false;
        }
        var d = new Date();
        d.setTime(d.getTime() + (1000 * 3600 * 24 * time)); // 1000毫秒 * 60秒 * 10 * 6 ＝ 60min
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }
    root = typeof exports !== "undefined" && exports !== null ? exports : win;
    root.Util = Util;
}(window);

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
 * loading
 */

function Loader(conf) {
    var _this = this;
    var _conf = {
        id: 'ajax-loading',
        text: '加载中'
    };
    $.extend(_conf, conf);

    var _textId = _conf.id + '-text';
    var _loadHtml = [
        '<div id = "' + _conf.id + '" class="loader">',
        //	'	<div class="screen"></div>',
        '	<div class="loader-block">',
        '		<div class="loading"></div>',
        '		<div id="' + _textId + '" class="text"></div>',
        '	</div>',
        '</div>'
    ].join('');

    var _$loader;
    var _$text;
    var _openCount = 0;

    if (!document.getElementById(_conf.id)) {
        $('body').append(_loadHtml);
        _$loader = $('#' + _conf.id);
        _$text = $('#' + _textId);
    } else {
        _$loader = $('#' + _conf.id);
        _$text = $('#' + _textId);
    }

    /**
     * open loading
     *
     */
    _this.open = function (msg) {
        if (msg) {
            _$text.html(msg);
        } else {
            _$text.html(_conf.text);
        }
        _$loader.addClass('show');
        $('body').addClass('loader-hidden');
        _openCount++;
    }
    /**
     * open close
     */
    _this.close = function () {
        if (--_openCount > 0) return;
        _openCount = 0;

        _$loader.removeClass('show');
        $('body').removeClass('loader-hidden');
    }
}

//扩展 string.format

String.prototype.format = function () {
    var args =arguments;
    return this.replace(/\{(\d+)\}/g, function (g0, g1) {
        return args[+g1];
    });
};
String.format = function (str) {
    var arr = Array.prototype.slice.call(arguments);
    return String.prototype.format.apply(str.toString(),arr.slice(1));
}
String.prototype.toDate=function(){
  if(/(\-|\/)/g.test(this)){
    return new Date(this.replace(/-/g,"/"));
  }else if(/^\d+$/g.test(this)){
    return new Date(Number(this));
  }else{
    var arr='';
    if(new Date(this)=="Invalid Date"){
      arr=this.toString().replace(/[\:\-\/]/,",")
    }
    return new Date(arr);
  }
}
Date.prototype.format = function (obj) { //author: meizz
var fmt=obj||'yyyy-MM-dd';
  var o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "h+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

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
/**
 *
 */
//限制輸入數字
function NumberInput(el) {
    var $e;
    if (el instanceof Element) {
        $e = $(el);
    } else if (typeof el === "string") {
        $e = $('#' + el);
        if (!$e.get(0)) return;
    } else {
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
 * jquery form to json map
 * @returns {{}}
 */
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
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