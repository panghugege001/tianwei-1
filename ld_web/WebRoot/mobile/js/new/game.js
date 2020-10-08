/*
 * ie兼容性处理,添加为数组添加includes 和 filter 方法
 * */
if(!Array.prototype.includes) {
	Array.prototype.includes = function(searchElement /*, fromIndex*/ ) {
		'use strict';
		if(this == null) {
			throw new TypeError('Array.prototype.includes called on null or undefined');
		}

		var O = Object(this);
		var len = parseInt(O.length, 10) || 0;
		if(len === 0) {
			return false;
		}
		var n = parseInt(arguments[1], 10) || 0;
		var k;
		if(n >= 0) {
			k = n;
		} else {  
			k = len + n;
			if(k < 0) {
				k = 0;
			}
		}
		var currentElement;
		while(k < len) {
			currentElement = O[k];
			if(searchElement === currentElement ||
				(searchElement !== searchElement && currentElement !== currentElement)) { // NaN !== NaN
				return true;
			}
			k++;
		}
		return false;
	};
}

if(!Array.prototype.filter) {
	Array.prototype.filter = function(fun /*, thisArg*/ ) {
		'use strict';

		if(this === void 0 || this === null) {
			throw new TypeError();
		}

		var t = Object(this);
		var len = t.length >>> 0;
		if(typeof fun !== 'function') {
			throw new TypeError();
		}

		var res = [];
		var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
		for(var i = 0; i < len; i++) {
			if(i in t) {
				var val = t[i];

				// NOTE: Technically this should Object.defineProperty at
				//       the next index, as push can be affected by
				//       properties on Object.prototype and Array.prototype.
				//       But that method's new, and collisions should be
				//       rare, so use the more-compatible alternative.
				if(fun.call(thisArg, val, i, t)) {
					res.push(val);
				}
			}
		}

		return res;
	};
}

function arrayContain(array, values) {
	array = array || [];
	values = values || [];
	if(array.length < values.length) return false;
	var ret = 0;
	for(var i = 0; i < values.length; i++) {
		array.includes(values[i]) && ret++;
	}
	return values.length === ret;
}

(function(w) {
	var SlotMg = SlotMg || {};
	var $filter = $('#j-filter'),
		$filterBtn = $filter.find('a'),
		$resetBtn = $('#j-resetBtn'),
		$gameBackBtn = $('.back-gameindex'),
		$gameMenu = $('#j-gameMenu'),
		$toggleBtn = $('#j-toggleBtn'),
		isLogin = false, //登录状态
		ptClient = '0',
		dtClient = '0',
		oldCategory = '',
		collectGames = [], //收藏游戏数据
		historyGames = [], //游戏历史记录数据
		tpl = ['<div id="{{id}}"  class="slot_game_item game-info" data-json=\'{{json}}\'>',
			'     <div class="layout_image_hover_text" >',
			'             <i class="{{jackpot}}"></i>',
			'             <img class="game_img lazy" {{lazyLoad}}="{{categoryPic}}{{pic}}">',
			'     </div>',
			'     <div class="game_item_operations tc">',
			'         <div class="game-text">{{name}}</div>',
			'         {{linkPlay}}',
			'         {{linkDemo}}',
			'     </div>',
			'</div>'
		].join('');

	var DtConfig = {
		gameUrl: $('#j-gameurl').val() || '',
		slotKey: $('#j-slotKey').val() || '',
		referWebsite: $('#j-referWebsite').val() || ''
	};

	// 游戏配置信息
	var GameConfig = {
		baseUrl: $('#j-baseUrl').val(),
		ptToken: $('#j-ptToken').val(),
		ntToken: $('#j-ntToken').val(),
		dtToken: $('#j-dtToken').val(),
		dtGameurl: $('#j-dtGameurl').val(),
		dtReferWebSite: $('#-dtReferWebSite').val(),
		userName: $('#j-username').val(),
		isLogin: $('#j-isLogin').val(),
		loginName: $('#j-username').val().toString().toUpperCase()
	};

	SlotMg.Reg = null;
	SlotMg.GameContainer = $('#j-gameContainer');
	SlotMg.IsLazyLoad = false;
	SlotMg.DataList = [];
	SlotMg.DataUrl = {
		PT: ['/mobile/json/slot/ptPhone.json?v=90000007'],
		PTSW: ['/mobile/json/slot/ptsw.json?v=90000007'],
		PNG: ['/mobile/json/slot/pngPhone.json?v=90000007'],
		DT: ['/mobile/json/slot/dtPhone.json?v=90000007'],
		MGS: ['/mobile/json/slot/mgsPhone.json?v=90000007'],	
		QT: ['/mobile/json/slot/qtPhone.json?v=90000007'],
		TTG: ['/mobile/json/slot/ttgPhone.json?v=90000007'],
		NT: ['/mobile/json/slot/ntPhone.json?v=90000007'],
		AG: ['/mobile/json/slot/ag.json?v=90000007'],
		BBIN: ['/mobile/json/slot/bbinPhone.json?v=90000007'],
	    CQ9: ['/mobile/json/slot/cq9Phone.json?v=90000007'],
	    PG: ['/mobile/json/slot/pgPhone.json?v=90000007']
	};

	SlotMg.ImgUrl = {
		PT: ['/mobile/images/ptgames/'],
		PTSW: ['/images/ptswgames/'],
		PNG: ['/images/pnggames/'],
		DT: ['/images/dtgames/'],
		MGS: ['/images/mgsgames/'],		
		QT: ['/images/qtgames/'],
		TTG: ['/images/ttggames/'],
		NT: ['/images/ntgames/'],
		AG: ['/images/aggames/'],
		BBIN: ['/images/bbingames/'],
		CQ9: ['/images/cq9games/'],
		PG: ['/images/pggames/']
	};

	SlotMg.ptClient = {
		category: "PT",
		code: "",
		eName: "最佳老虎机平台",
		id: "ptclient",
		line: "",
		name: "PT客戶端下载",
		pic: "ptclient2.png",
		state: 1,
		subType: "ptclient",
		href: "https://www.duofa1.com/web/pt/ptNewClient.apk",
		tag: ["NEW", "HOT"],
		type: "SLO"
	};

	SlotMg.dtClient = {
		category: "DT",
		code: "",
		eName: "最佳老虎机平台",
		id: "dtclient",
		line: "",
		name: "DT客戶端下载",
		pic: "dtclient2.png",
		state: 1,
		subType: "DT-2",
		href: "",
		tag: ["NEW", "HOT"],
		type: "SLO"
	};

	SlotMg.isIOS = function() {
		var u = navigator.userAgent;
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
		var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
		return isiOS;
	};

	SlotMg.init = function() {
		isLogin = $('#j-isLogin').val() === 'true';
		SlotMg.event();
		SlotMg.search();
		//正式游戏
		$("#j-gameContainer").on('click', '.btn-play', function() {
			if(isLogin) {
				var $that = $(this),
				obj = $that.closest('.game-info').data('json');
				SlotMg.linkAction(obj, 0);
				
			}else if($(this).hasClass('j-login')){
				window.alert('请先登录账户！！');
			}
			return false;
		});

		//试玩事件处理
		$("#j-gameContainer").on('click', '.btn-try', function() {
			var $that = $(this),
				obj = $that.closest('.game-info').data('json');
			SlotMg.linkAction(obj, 1);
		});

		//游戏记录

	};

	SlotMg.event = function() {
		$filterBtn.on('click', function(e) {
			e.preventDefault();
			var $this = $(e.currentTarget),
				type = $this.data('toggle');
			SlotMg.setActiveClass($this);
			if($this.is('[data-value]')) {
				SlotMg.showGames();
			}
			return false;
		});
		$toggleBtn.on('click', SlotMg.toggleShow);
		$resetBtn.on('click', SlotMg.reset);
		$gameBackBtn.on('click', SlotMg.reset);
	};

	/**
	 * 根据游戏的大类动态获取游戏数据
	 * @param type
	 * @param callback
	 */
	SlotMg.getByCategory = function(type, callback) {
		var urls = SlotMg.DataUrl['PT'];

		if(urls === 'load') {
			callback();
			return;
		}
		var dfds = [];

		for(var i in SlotMg.DataUrl) {
			dfds.push($.getJSON(SlotMg.DataUrl[i], function(data) {
				SlotMg.DataList = SlotMg.DataList.concat(data);
			}));

		}
		$.when.apply(null, dfds)
			.done(function() {
				SlotMg.DataUrl['PT'] = 'load';
				callback();
			})
			.fail(function() {
				console.log('游戏加载失败');
			});
	};

	/**
	 *设置过滤信息
	 */
	SlotMg.setFilter = function() {

		var tmpObj = {
			'category': 'PT', //老虎机平台类型
			'type': '', //老虎机类型 :经典,电动吃角子
			'line': '', // 老虎机线性类型
			'subType': '', // 第二种类型类型
			'tag': []
		};

		tmpObj.category = $filter.find('.filter_dropdown_content_sec .filter_item_content .filter_item.active a').data("tab");
		if(oldCategory == "" || oldCategory != tmpObj.category) {
			var category = "#" + tmpObj.category;
			$(".item").removeClass("active");
			$(category).addClass("active");

			oldCategory = tmpObj.category;
			SlotMg.resetClass();
		}

		var $btn = $filter.find('.filter_dropdown_content_sec .filter_item_content .filter_item.active a');
		// 取資料
		var tmp = {
			'tag': []
		};
		$.each($btn, function(index, obj) {
			var dataValue = $(obj).data('value');
			if(dataValue) {
				var tagvalue = dataValue['tag'];
				if(tagvalue) {
					!tmp.tag.includes(tagvalue) && tmp.tag.push(tagvalue);
				} else {
					tmp = $.extend(tmp, dataValue);
				}
			}
		});

		var ret = $.extend(tmpObj, tmp);
		builReg(ret.tag);

		function builReg(filterArr) {
			if(filterArr == 0) return;
			var retStr = '';
			for(var i = 0; i < filterArr.length; i++) {
				retStr += '(?=.*,' + filterArr[i] + ')';
			}
			//retStr=retStr.replace(/\|+$/, '');
			SlotMg.Reg = new RegExp('^' + retStr + '.*$');
		}

		// console.group('filter信息');
		// console.log(ret);
		// console.log(SlotMg.Reg);
		// console.groupEnd();

		return ret;
	};
	/**
	 * 获取查询条件返回数组的形式
	 */
	SlotMg.getWhere = function(arr) {
		var ret = [];
		for(var p in arr) {
			if(arr[p]) {
				if(p == 'tag') {
					arr[p].length > 0 &&
						ret.push('SlotMg.Reg.test(","+el.' + p + '.join(","))');
					//ret.push('el.'+p+'.includes("'+FilterObj[p]+'")');
				} else if(p == 'line') {
					ret.push('SlotMg.LineCp("' + arr['line'] + '",el.' + p + ')');
				} else {
					ret.push('el.' + p + '=="' + arr[p] + '"');
				}
			}
		}
		return ret;
	};
	/**
	 * 判断是否在集合范围
	 * @param rang
	 * @param val
	 * @returns {boolean}
	 * @constructor
	 */
	SlotMg.LineCp = function(rang, val) {
		if(val != "" || typeof val != "undefined") {
			val = parseInt(val);
			var r = rang.split('-'),
				start = parseInt(r[0]),
				end = r[1] || '';

			if(end) {
				if(start <= val && val <= end) {
					return true;
				}
			} else {
				if(start <= val) {
					return true;
				}
			}
			return false;
		}
	};

	/**
	 * 多条件查找游戏获取游戏
	 */
	SlotMg.showGames = function() {

		// 替換分類
		var filter = SlotMg.setFilter();
		var category = filter.category;

		if(category != "" && typeof category != "undefined") {

			//          if( category == "AG" || category == "SBA"){
			//              return false;
			//          }

			if(category == "PT" && ptClient != 1) {

				var isiOS = SlotMg.isIOS();

				if(isiOS) {
					SlotMg.ptClient["state"] = 0;
				}

				SlotMg.DataList.unshift(SlotMg.ptClient);
				ptClient = 1;
			}

			if(category == "DT" && dtClient != 1) {

				var isiOS = SlotMg.isIOS();

				if(isiOS) {
					SlotMg.dtClient["href"] = "http://down.dreamtech.asia/LONGDU/ios.html";
				} else {
					SlotMg.dtClient["href"] = "http://down.dreamtech.asia/LONGDU/android.html";
				}

				SlotMg.DataList.unshift(SlotMg.dtClient);
				dtClient = 1;
			}

			Util.setCookie("slotgame", category);
		}

		SlotMg.getByCategory(filter.category, function() {
			var whereArr = SlotMg.getWhere(filter);
			if(whereArr.length) {
				var _funStr = ' return ' + whereArr.join(' && ');
				var _tmpFun = new Function("el", _funStr); // 根据动态生成查询条件,动态生成方法
				var _d = SlotMg.DataList.filter(_tmpFun);
				SlotMg.builHtml(_d);
			} else {
				SlotMg.builHtml(SlotMg.DataList.slice(0, 100)); // 最多只获取100个数据
				SlotMg.builHtml(SlotMg.DataList); // 最多只获取100个数据
			}
			SlotMg.setCollectState();
		});
	};

	SlotMg.setActiveClass = function($ele) {
		var id = $ele.attr("id");
		if(id == "aggame" || id == "sportgame") {
			return false;
		}
		var $target = $ele;
		if($target.parent().hasClass('filter_item')) {
			$target = $ele.parent();
		}
		$target.addClass('active').siblings().removeClass('active');
	};

	/**
	 * 过滤查询信息
	 */
	SlotMg.reset = function() {
		$filter.find('.filter_dropdown_content_sec').each(function(i, e) {
			if(i != 0) {
				var that = $(this);
				var item_li = that.find("ul li:eq(0)");
				var item_a = that.find("ul li:eq(0) a");
				SlotMg.setActiveClass(item_li);
				SlotMg.toggleClick(item_a);
			}
		});
		$('.j-ipt').val('');
	};

	SlotMg.resetClass = function() {
		$filter.find('.filter_dropdown_content_sec').each(function(i, e) {
			if(i != 0) {
				var that = $(this);
				var item_li = that.find("ul li:eq(0)");
				SlotMg.setActiveClass(item_li);
			}
		});
	};

	/**
	 * 获取随机数
	 * @param min 开始的数
	 * @param max 结束的数
	 * @param int 小数点位数
	 * @returns {string}
	 */
	SlotMg.getRandom = function(min, max, int) {
		var ret = Math.random() * (max - min) + min;
		int = int || 0;

		return ret.toFixed(int);
	};
	SlotMg.toggleShow = function() {
		$filter.slideToggle();
	};
	SlotMg.toggleClick = function(obj) {
		obj.trigger("click");
	};
	/**
	 * 查找输入框
	 */
	SlotMg.search = function() {
		var $searchForm = $('#j-searchForm'), // 查找表单
			$searchIpt = $searchForm.find('.j-ipt'), // 查找输入框
			$searchSelect = $searchForm.find('.j-select'), // 查找结果显示在下拉菜单
			$searchBtn = $searchForm.find('.j-btnSearch'), //查找按钮
			$selectAction = $searchSelect.find('a'), //下拉菜单的item
			searchList = [];

		function get(v) {
			if(!v || v == '') return;
			searchList = SlotMg.DataList.filter(function(el) {
				return el.name.indexOf(v) != -1 ||
					el.eName.toLowerCase().indexOf(v) != -1;
			});
		}

		function buildSelect() {
			if(searchList == 0) {
				$searchSelect.html('').slideUp();
				return;
			}
			var _ret = [];
			$.each(searchList, function(i, o) {
				_ret.push('<a href="javascript:;" data-id="' + o.id + '">' + o.name + '</a>');
				if(i == 9) return false;
			});
			$searchSelect.html(_ret.join('')).slideDown();
		}

		$searchIpt.on('keyup', function() {
			searchList = [];
			var v = $searchIpt.val();

			if(v != '' && v != null) {
				$('.j-btnSearch').addClass('active');
				$('.j-btnSearch').removeAttr('disabled');
				get($searchIpt.val());
				buildSelect();
			} else {
				$('.j-btnSearch').attr('disabled');
				$('.j-btnSearch').removeClass('active')
			}

		});
		$searchForm.on('click', '.j-select a', function(e) {
			var _id = $(e.currentTarget).data('id');
			$searchIpt.val($(e.currentTarget).text());

			$searchSelect.slideUp();

			searchList = SlotMg.DataList.filter(function(el) {
				return el.id == _id;
			});
			SlotMg.builHtml(searchList);
			// SlotMg.setCollectState();
		});

		$searchBtn.on('click', function() {
			$searchIpt.val();
			if($searchIpt.val() != '') {
				$('.j-btnSearch').addClass('active');
				$('.j-btnSearch').removeAttr('disabled');
				get($searchIpt.val());
				SlotMg.builHtml(searchList);
			} else {
				//showTips('查询条件不能为空')
			}
			//
			// if(v !='' && v != null){
			//     $('.j-btnSearch').addClass('active');
			//     $('.j-btnSearch').removeAttr('disabled');
			//     get($searchIpt.val());
			//     buildSelect();
			// }else{
			//     $('.j-btnSearch').attr('disabled');
			//     $('.j-btnSearch').removeClass('active')
			// }

			// SlotMg.setCollectState();
		});

		$searchIpt.on('focusout', function() {
			searchList = [];
			$searchSelect.slideUp();
		});
	};
	/**
	 * 获取收藏游戏
	 */
	SlotMg.queryCollectGames = function() {
		// return $.getJSON('/mobi/queryGameStatus.aspx');
	};

	/**
	 * 保存收藏游戏
	 */
	SlotMg.saveCollectGames = function(obj, isDel) {
		// if(!obj) return;
		// var tmpIndex=-1;
		// if(collectGames){
		//     $.each(collectGames, function(index, item) {
		//         if(item.id === obj.id) {
		//             tmpIndex=index;
		//             return false;
		//         }
		//     });
		//     if(tmpIndex!==-1&&!isDel){ //添加模式，找不到才进行添加操作
		//         return;
		//     }
		// }
		// if(isDel){ //删除操作
		//     collectGames.splice(tmpIndex,1);
		// }else{
		//     collectGames.unshift(obj);
		//     collectGames.length>20 && collectGames.slice(0,19);
		// }
		//
		// $.post('/mobi/saveOrUpdateGameStatus.aspx',{'gameList':JSON.stringify(collectGames)},function(data){
		// })
	};

	SlotMg.setCollectState = function() {
		// if(isLogin){
		//     $.each(collectGames,function(index,ele){
		//         $('#'+ele.id).find('.collect').attr('data-state',1).addClass('faved');
		//             //.html('<i class="iconfont icon-heart2"></i>已收藏');
		//     });
		// }

	};

	SlotMg.saveHistory = function(obj) {
		// if(!obj) return;
		// var tmp;
		// if(historyGames){
		//     tmp= historyGames.filter(function(item){
		//         return item.id===obj.id;
		//     });
		//     if(tmp.length>0){
		//         return;
		//     }
		// }
		// historyGames.unshift(obj);
		// if(historyGames.length>20){
		//     historyGames=historyGames.slice(0,19);
		// }
		// window.localStorage.setItem('hisotryGames',JSON.stringify(historyGames));
		var name = 'history'
		if(window.localStorage) {
			var data = localStorage.getItem('history');
			if(data) {
				data = JSON.parse(data)
			} else {
				data = []
			}
			var tmp = data.filter(function(item) {
				return item.id === obj.id;
			});
			if(tmp.length > 0) {
				return;
			}
			if(data.length > 20) {
				data = data.slice(0, 19);
			}
			$.extend(obj, {cktime: new Date().getTime()});
			data.push(obj)
			localStorage.setItem('history', JSON.stringify(data))
		}
	};
	SlotMg.linkAction = function(obj, isFun) {
		SlotMg.saveHistory(obj)
		if(isFun == "0") {
			var baseurl = window.location.origin;
			switch(obj.category) {
				case 'PT':
					window.location.href = '/app/ptH5Login.aspx?gameCode=' + obj.id + '&fromApp=app';
					break;
				case 'PTSW':
					window.location.href = "/game/gameLoginPtSW.aspx?mode=real&gameCode=" + obj.id + "&lobby=" + window.location.host + "/mobile/app/slotGame.jsp";
					break;
				case 'MGS':
					$.post('/mobi/gameH5MGS.aspx', {
						itemId: obj.itemId,
						appId: obj.appId,
						fromApp: 'app',
						demo: isFun
					}, function(result) {
						if(result.success) {
							window.location.href = result.data.url;
						} else {
							alert(result.message);
						}
					});
					break;
				case 'CQ9':
					$.post('/mobi/gameH5CQ9.aspx', {
						gameCode: obj.code,
						fromApp: 'app',
						demo: isFun
					}, function(result) {
						if(result.success) {
							window.location.href = result.data.url;
						} else {
							alert(result.message);
						}
					});
					break;
			   case 'PG':
					$.post('/mobi/gameH5PG.aspx', {
						gameCode: obj.code,
						fromApp: 'app',
						demo: isFun
					}, function(result) {
						if(result.success) {
							window.location.href = result.data.url;
						} else {
							alert(result.message);
						}
					});
					break;
				case 'DT':
					if(obj.type == 'DEM') {
						alert('正式游戏，敬请期待!'); //判断状态是否为试玩
						return false;
					}

					//window.location.href = "/game/gameLoginDT.aspx?isfun=0&gameCode=" + obj.id + "&language=zh_CN&clientType=1";

					                    $.post('/mobi/loginDT.aspx',function(result){
					                        var dtUrl= '/dtGames.aspx?slotKey={0}&language=zh_CN&gameCode={1}&isfun=0&type=null&closeUrl={2}';
					                        if(result.success){
					                            window.location.href = result.data.url+String.format(dtUrl,result.data.key,obj.id,result.data.referWebsite);
					                        }else{
					                            alert(result.message);
					                        }
					                    })

					break;
				case 'PNG':
					var gid = obj.id;
					$.post('/gamePNGFlashForTp.aspx', {
						practice: 0,
						gameCode: gid,
						fromApp: 'app'
					}, function(result) {
						if(result.success) {
							mobileManage.getLoader().close();
							window.location.href = result.data.url;
						} else {
							alert(result.message);
						}
					});
					break;
				case 'QT':
					SlotMg.load_qtgame(obj.id, isFun, 'qtGames', window.location.href + '?platform=QT');
					break;
				case 'NT':
					var ntUrl = 'http://load.sdjdlc.com/disk2/netent/?game={0}&language=cn&lobbyUrl={1}&key={2}';
					/*if(GameConfig.ntToken){
					    window.location.href=String.format(ntUrl,obj.id,window.location.href+'?platform=NT',GameConfig.ntToken);
					}else{*/
					mobileManage.getLoader().open("进入游戏中");
					$.post('/mobi/getNTGame.aspx', {
						gameCode: obj.id
					}, function(result) {
						if(result.success) {
							mobileManage.getLoader().close();
							window.location.href = String.format(ntUrl, obj.id, window.location.href + '?platform=NT', result.message);
						} else {
							alert(result.message);
						}
					});
					//                    }
					break;
				case 'TTG':
					var gameUri = 'asp/ttLogin.aspx?gameName={{code}}&gameType=0&gameId={{id}}&lang=zh-cn&devicetype=mobile';
					gameUri = gameUri.replace(/\{\{code\}\}/g, obj.code)
						.replace(/\{\{id\}\}/g, obj.id);
					window.location.href = gameUri;
					break;
				case 'AG':
					var gameUri = '/asp/loginAgSlot.aspx?gameType={{id}}&lang=zh-cn&deviceType=mobile';
					gameUri = gameUri.replace(/\{\{id\}\}/g, obj.id);
					window.location.href = gameUri;
					break;
				case 'BBIN':
                    /*alert('BBIN游戏，敬请期待!');*/
                    var gameUri = '/game/bbinMobiLogin.aspx?gameCode={{id}}';
                    gameUri = gameUri.replace(/\{\{id\}\}/g, obj.id);
                    window.location.href = gameUri;
                    break;
			}
		} else {

			switch(obj.category) {
				case 'PT':
					window.location.href = '/app/ptH5Login.aspx?gameCode=' + obj.id;
					break;
				case 'PTSW':
					window.location.href = "/game/gameLoginPtSW.aspx?mode=fun&gameCode=" + obj.id + "&lobby=" + window.location.host + "/mobile/app/slotGame.jsp";
					break;
				case 'MGS':
					//var gameUri = "https://mobile22.gameassists.co.uk/MobileWebServices_40/casino/game/launch/yahucom/" + obj.code + "/zh?loginType=VanguardSessionToken&isPracticePlay=true&casinoId=2712&isRGI=true&authToken=&lobbyurl=" + window.location.host + "/mobile/app/slotGame.jsp";

					//window.location.href = gameUri;
					$.post('/mobi/gameH5MGS.aspx', {
						itemId: obj.itemId,
						appId: obj.appId,
						fromApp: 'app',
						demo: isFun
					}, function(result) {
						if(result.success) {
							window.location.href = result.data.url;
						} else {
							alert(result.message);
						}
					});
					break;
				case 'PG':
					$.post('/mobi/gameH5PG.aspx', {
						gameCode: obj.code,
						fromApp: 'app',
						demo: isFun
					}, function(result) {
						if(result.success) {
							window.location.href = result.data.url;
						} else {
							alert(result.message);
						}
					});
					break;
				case 'DT':
					window.location.href = 'http://play.dreamtech8.com/playSlot.aspx?gameCode=' + obj.id + '&isfun=1&type=dt';
					break;
				case 'PNG':
					var gameUri = "https://bsicw.playngonetwork.com/casino/PlayMobile?pid={{pid}}&gid={{gid}}&lang=zh_CN&practice=1";
					gameUri = gameUri.replace(/\{\{pid\}\}/g, "100" + obj.code)
						.replace(/\{\{gid\}\}/g, obj.id);
					window.location.href = gameUri;
					break;
				case 'QT':
					SlotMg.load_qtgame(obj.id, isFun, 'qtGames', window.location.href + '?platform=QT');
					break;
				case 'NT':
					var ntUrl = 'http://load.sdjdlc.com/nt/demo.html?game={0}&language=cn&lobbyUrl={1}';
					window.location.href = String.format(ntUrl, obj.id, window.location.href + '?platform=NT');
					break;
				case 'TTG':
					var gameUri = 'http://pff.ttms.co/casino/default/game/casino5.html?gameName={{code}}&gameType=0&deviceType=mobile&lang=zh-cn&playerHandle=999999&gameId={{id}}&account=FunAcct&lobbyUrl={{baseUrl}}';
					gameUri = gameUri.replace(/\{\{code\}\}/g, obj.code)
						.replace(/\{\{id\}\}/g, obj.id)
						.replace(/\{\{baseUrl\}\}/g, GameConfig.baseUrl);

					window.location.href = gameUri;
					break;
				case 'AG':
					var gameUri = '/asp/agTryLogin.aspx?gameType={{id}}&lang=zh-cn&deviceType=mobile';
					gameUri = gameUri.replace(/\{\{id\}\}/g, obj.id);
					window.location.href = gameUri;
					break;
			}
		}

	};

	SlotMg.load_qtgame = function (gameCode, isfun, gameType, origin) {
        $.post('/mobi/getQTGame.aspx', {
                gameCode: gameCode,
                isfun: isfun,
                gameType: gameType,
                origin: origin,
                fromApp: 'app'
            },
            function (result) {
                if (result.success) {
                    window.location.href = result.data.url;
                } else {
                    alert(result.message);
                }
            });
    };

	/**
	 * 获取试玩连接
	 * @param obj
	 * @returns {string}
	 */
	SlotMg.getLinkDemo = function(obj) {
		// pt 没有试玩游戏
		if(obj.category == 'PT' || obj.category == 'BBIN' || typeof obj.href != "undefined") {
			return "";
		} else {
			return '<div class="o-btn btn-try">免费试玩</div>';
		}
	};

	/**
	 * 获取进入游戏连接
	 * @param obj
	 * @returns {string}
	 */
	SlotMg.getLinkPlay = function(obj) {
		if(typeof obj.href != "undefined") {
			return '<a href="' + obj.href + '" target="_blank"><div class="o-btn">立即下载</div></a>'
		}
		if((obj.type == "DEM" && obj.category == 'DT') || (obj.state == "DEM" && obj.category == 'DT')) {
			return ''
		} else {
			return '<div class="j-login o-btn btn-play">立即游戏</div>';
		}
	};

	SlotMg.builHtml = function(data,append) {
		var _ret = [],
			animaClass = '';
		$.each(data, function(i, o) {
			var caPic = '';
			// 遊戲總開關
			if(o.state == 0) {
				return true;
			}
			// DT DT-2 不支持手機  jackpot 奖池的判断，有奖池时候替换图标的类名
			if(o.category == 'DT' && o.subType == 'jackpot') {
				var jackpot = 'jackpot-icon'
			}
			var obj = tpl.replace(/\{\{pic\}\}/g, o.pic)
				.replace(/\{\{name\}\}/g, o.name)
				.replace(/\{\{id\}\}/g, o.id)
				.replace(/\{\{class\}\}/g, animaClass)
				.replace(/\{\{categoryPic\}\}/g, SlotMg.ImgUrl[o.category])
				.replace(/\{\{jackpot\}\}/g, jackpot)
				.replace(/\{\{key\}\}/g, '')
				.replace(/\{\{eName\}\}/g, o.eName || '')
				.replace(/\{\{json\}\}/g, JSON.stringify(o))
				.replace(/\{\{subType\}\}/g, o.subType)
				.replace(/\{\{lazyLoad\}\}/g, SlotMg.IsLazyLoad ? 'data-original' : 'src')
				.replace(/\{\{linkDemo\}\}/g, SlotMg.getLinkDemo(o))
				.replace(/\{\{linkPlay\}\}/g, SlotMg.getLinkPlay(o));

			_ret.push(obj);

			// .replace(/\{\{like\}\}/g,SlotMg.getRandom(18859,70059))
			// .replace(/\{\{collectAction\}\}/g,o.isCollect?' faved':'')
			// .replace(/\{\{isFavorite\}\}/g,o.isCollect?' data-favorite ':'')
		});
		if(append===true){
			SlotMg.GameContainer.append(_ret);	
		}
		else{
			SlotMg.GameContainer.html(_ret);
		}
		SlotMg.lazyload();
	};
	SlotMg.lazyload = function() {
		if(SlotMg.IsLazyLoad) {
			$('img.lazy').lazyload();
		}
	};
	window._SlotMg = SlotMg;
	w.SlotMg = SlotMg;
})(window);
$(function() {
	var type = '',
		platform = Util.getQueryString('platform'),
		$gameMenu = $('#j-filter');
	SlotMg.init();
	if(platform == "" || typeof platform == 'undefined' || platform == null) {
		type = 'PT';
	} else {
		type = platform;
	}
	if(type != "") {
		if(type == "MG") {
			type = "MGS";
		}
		$gameMenu.find('a[data-tab="' + type + '"]').trigger('click');
	} else {
		$gameMenu.find('a[data-tab="ALL"]').trigger('click');
	}
	// 收缩展示
	var $filterDropdown = $('#filter_dropdown_trigger'),
		$filterContent = $gameMenu,
		$filterResetBtns = $('.filter_dropdown_content .btn_wrap'),
		$btnSlidup = $('#j-slideup'),
		$overlay = $('.overlay'),
		$body = $("body,html");
	function toggleShowContent() {
		if($filterContent.is(':hidden')
		&&$(".slatform-screening .menu_item:eq(0)").hasClass("active")
		) {
			
			$body.addClass("overflow");
			$filterContent.show()
		} else {
			$body.removeClass("overflow");
			$filterContent.hide();
		}
		return false;
	}

	$filterDropdown.click(toggleShowContent);
	$btnSlidup.click(toggleShowContent);
	$(".overlay").on("click", toggleShowContent);
	
	$(".slatform-screening .menu_item").on("click", function() {
		$("#j-filter").hide();
		var $target = $(this);
		if($target.attr('data-type') == 'slot'){
			$('#filter-2').show();
		}else {
			$('#filter-2').hide();
		}
		var ind = $(this).index();
		$target.addClass('active').siblings().removeClass('active');
		$(".slot-item-list").eq(ind).addClass('active').siblings().removeClass('active');
		$(".slot_game_item_list").eq(ind).addClass('active').siblings().removeClass('active');
	});
	$("#header").hide();
});