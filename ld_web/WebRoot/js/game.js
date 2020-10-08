/*
 * ie兼容性处理,添加为数组添加includes 和 filter 方法
 * */
if (!Array.prototype.includes) {
    Array.prototype.includes = function(searchElement /* , fromIndex */) {
        'use strict';
        if (this == null) {
            throw new TypeError('Array.prototype.includes called on null or undefined');
        }

        var O = Object(this);
        var len = parseInt(O.length, 10) || 0;
        if (len === 0) {
            return false; 
        }
        var n = parseInt(arguments[1], 10) || 0;
        var k;
        if (n >= 0) {
            k = n; 
        } else {
            k = len + n;
            if (k < 0) {k = 0;}
        }
        var currentElement;
        while (k < len) {
            currentElement = O[k];
            if (searchElement === currentElement ||
                (searchElement !== searchElement && currentElement !== currentElement)) { // NaN
																							// !==
																							// NaN
                return true;
            }
            k++;
        }
        return false;
    };
}

if (!Array.prototype.filter) {
    Array.prototype.filter = function(fun/* , thisArg */) {
        'use strict';

        if (this === void 0 || this === null) {
            throw new TypeError();
        }

        var t = Object(this);
        var len = t.length >>> 0;
        if (typeof fun !== 'function') {
            throw new TypeError();
        }

        var res = [];
        var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
        for (var i = 0; i < len; i++) {
            if (i in t) {
                var val = t[i];

                // NOTE: Technically this should Object.defineProperty at
                // the next index, as push can be affected by
                // properties on Object.prototype and Array.prototype.
                // But that method's new, and collisions should be
                // rare, so use the more-compatible alternative.
                if (fun.call(thisArg, val, i, t)) {
                    res.push(val);
                }
            }
        }

        return res;
    };
}

function arrayContain(array,values){
    array=array||[];
    values=values||[];
    if(array.length<values.length) return false;
    var ret=0;
    for (var i = 0; i < values.length; i++) {
        array.includes(values[i]) && ret++;

    }
    if(values.length===ret) return true;

    return false;
}

!(function(w){
    var SlotMg=SlotMg||{};

    var $filter=$('#j-filter'),
        $filterBtn=$filter.find('a'),
        $resetBtn=$('#j-resetBtn'),
        $gameMenu=$('#j-gameMenu'),
        $toggleBtn=$('#j-toggleBtn'),
        $gameContainer=$('#j-gameContainer'),
        isLogin=false,  // 登录状态
        collectGames=[], // 收藏游戏数据
        historyGames=[], // 游戏历史记录数据
        collectTmpGames=[], // 缓存的收藏游戏数据
        tpl=['<div class="game-info {{class}}" id="{{id}}" data-subtype="{{subType}}" data-tag="{{tag}}" data-json=\'{{json}}\'>',
            ' <i class="{{jackpot}}"></i>',
            '       <div class="game-pic">',
            '          <img class="lazy" data-original="{{categoryPic}}games/{{pic}}?v=qasf" width="260" height="116" alt="">',
            '       </div>',
            '       <div class="name">',
            '           <h4>{{name}}<br><span class="eName_text">{{eName}}</span></h4>', 
            '       </div>',
            '       <div class="hover-bar">',
            '       <div class="game-brief">',
      /*
		 * ' <div class="biref-top">', ' <p class="fl">人气:<span>{{like}}</span></p>', '
		 * <p class="fr like">★★★☆☆</p>', ' </div>',
		 */
            '           <div class="btn-wp text-center">{{linkDemo}}{{linkPlay}}</div>',
            '<div class="game_titlename">{{name}}<br><span class="eName_text">{{eName}}</span></div>', 
            '       </div>',
            '<a {{isFavorite}} data-state="0" class="collect j-login"  href="javascript:;">{{collectAction}}</a> </div>',
            '  </div>'].join('');

    var DtConfig={
        gameUrl:$('#j-gameurl').val()||'',
        slotKey:$('#j-slotKey').val()||'', 
        referWebsite:$('#j-referWebsite').val()||''
    };

    SlotMg.Reg=null;
    SlotMg.DataList=[];
    SlotMg.DataUrl={
        /*PT:['/data/slot/ptNew.json?v=90000007'], 
        TTG: ['/data/slot/ttg.json?v=90000009'],
        PTSW:['/data/slot/ptsw.json?v=90000007'],   */
        MGS:['/data/slot/mgs.json?v=90000007'],
        BBIN:['/data/slot/bbin.json?v=90000007'],
       /* PNG:['/data/slot/png.json?v=90000007'],
        QT:['/data/slot/qt.json?v=90000007'],*/
        DT:['/data/slot/dt.json?v=90000007'],
        CQ9:['/data/slot/cq9.json?v=90000007'],
        PG:['/data/slot/pg.json?v=90000007']
        /*NT:['/data/slot/nt.json?v=90000007'],
        AG:['/data/slot/ag.json?v=90000007']*/
        
    };

    SlotMg.init=function(){ 
        isLogin=$('#j-isLogin').val()==='true';

        /*
		 * SlotMg.DataList=getPt();
		 * SlotMg.DataList=SlotMg.DataList.concat(getTtg());
		 * SlotMg.DataList=SlotMg.DataList.concat(getQt());
		 * SlotMg.DataList=SlotMg.DataList.concat(getNt());
		 * SlotMg.DataList=SlotMg.DataList.concat(getMg());
		 * SlotMg.DataList=SlotMg.DataList.concat(getDt());
		 */

        SlotMg.event();
        SlotMg.menu();
        SlotMg.search();

        $(document).on('click','.j-login',function(){
            if(!isLogin){
                alert('请先登录账户！！');
                $('#modal-login').modal('show');
                return false;
            }
        });

        // 获取收藏游戏

        if(isLogin){
            var def=SlotMg.queryCollectGames();

            def.done(function(data){
                if(typeof data=== 'string'){
                    collectGames=[];
                }else{
                    collectGames=JSON.parse(data.gameList);
                }
                favoriteInit(true);

            }).fail(function(){
                alert('获取数据失败');
            });

            function favoriteInit(){
                // 收藏游戏点击事件
                $(document).on('click','.game-info .collect',function(){
                    var $that=$(this),
                        state=$that.attr('data-state'),
                        tmpObj=$that.closest('.game-info').data('json');

                    if(state=='0'){ // 没有收藏
                        $that.attr('data-state',1)
                            .html('<i class="iconfont icon-heart2"></i>已收藏');
                        tmpObj.isCollect=true;
                        SlotMg.saveCollectGames(tmpObj,false);
                        // console.table(collectGames);
                    }else if(state=='1'){// 已经收藏
                        $that.attr('data-state',0)
                            .html('<i class="iconfont icon-heart"></i>添加收藏');
                        SlotMg.saveCollectGames(tmpObj,true);
                        // console.table(collectGames);
                    }
                });
                $(document).on('click','.game-info .collect[data-favorite]',function(){
                    var $that=$(this),
                        tmpObj=$that.closest('.game-info').data('json');
                    SlotMg.saveCollectGames(tmpObj,true);
                    // console.table(collectGames);
                    $that.closest('.game-info').remove();
                });

                // 收藏游戏列表
                $('#j-favoriteAction').on('click',function(){
                    if(isLogin===true){
                        SlotMg.builHtml(collectGames);
                        SlotMg.lazyload();
                    }
                });
            }

        }

        // 游戏记录
        // =======

        try{
            if(localStorage.getItem('hisotryGames')) {
                historyGames=JSON.parse(localStorage.getItem('hisotryGames'))
            } else {
                historyGames=[];
                localStorage.setItem('hisotryGames','');
            }
            $(document).on('click','.game-info .btn',function(e){
                var tmpObjStr=$(this).closest('.game-info').data('json');
                SlotMg.saveHistory(tmpObjStr);
            });
        }catch(err){
            console.log('游戏记录出错');
        }
        // 游戏历史记录列表
        $('#j-historyAction').on('click',function(){
            SlotMg.builHtml(historyGames);
            SlotMg.lazyload();
        });
    };

    SlotMg.event=function(){
        $filterBtn.on('click',function(e){
            e.preventDefault();
            var $this=$(e.currentTarget),
                type=$this.data('toggle'),
                $parent=$this.closest('.search-row');

            if(type==='game-tab'){
                SlotMg.reset();
                var href=$this.attr('href');

                !$(href).hasClass('active') && SlotMg.setActiveClass($(href));
            }

            SlotMg.setActiveClass($this);
            SlotMg.showGames();
        });

        $toggleBtn.on('click',SlotMg.toggleShow);
        $resetBtn.on('click',SlotMg.reset);

    };

    /**
	 * 根据游戏的大类动态获取游戏数据
	 * 
	 * @param type
	 * @param callback
	 */
    SlotMg.getByCategory=function(type,callback){
        var urls=SlotMg.DataUrl[type];
        if(!urls) return;

        if(urls==='load'){
            callback();
            return;
        }
        var dfds=[];
        for (var i = 0; i < urls.length; i++) {
            dfds.push($.getJSON(urls[i],function(data){
                SlotMg.DataList=SlotMg.DataList.concat(data);
            }));
        }
        $.when.apply(null,dfds)
            .done(function(){
                // console.log('done')
                SlotMg.DataUrl[type]='load';
                callback();
            })
            .fail(function(){

            });
    };

    /**
	 * 设置过滤信息
	 */
    SlotMg.setFilter=function(){
        var $btn=$filter.find('.tab-hd a.active,.tab-panel.active a.active');
        var tmpObj={
            'category':'', // 老虎机平台类型
            'type':'',  // 老虎机类型 :经典,电动吃角子
            'line':'', // 老虎机线性类型
            'subType':'', // 第二种类型类型
            'tag':[]
        };
        tmpObj.category=$filter;
        var tmp={'tag':[]};
        $.each($btn, function(index,obj){
            var dataValue=$(obj).data('value');
            if(dataValue){
                var tagvalue=dataValue['tag'];
                if(tagvalue){
                    !tmp.tag.includes(tagvalue)&&tmp.tag.push(tagvalue);
                }else{
                    tmp=$.extend(tmp,dataValue);
                }
            }
        });

        var ret=$.extend(tmpObj,tmp);

        builReg(ret.tag);

        function builReg(filterArr){
            if(filterArr==0) return;
            var retStr='';
            for (var i = 0; i < filterArr.length; i++) {
                retStr += '(?=.*,'+filterArr[i]+')';
            }
            // retStr=retStr.replace(/\|+$/, '');
            SlotMg.Reg=new RegExp('^'+retStr+'.*$');

        }
       /*
		 * console.group('filter信息'); console.log(ret); console.log(SlotMg.Reg);
		 * console.groupEnd();
		 */

        return ret;
    };
    /**
	 * 获取查询条件返回数组的形式
	 */
    SlotMg.getWhere=function(arr){
        var ret=[];
        for(var p in arr){
            if(arr[p]){
                if(p=='tag'){
                    arr[p].length>0
                    && ret.push('SlotMg.Reg.test(","+el.'+p+'.join(","))');
                    // ret.push('el.'+p+'.includes("'+FilterObj[p]+'")');
                }else{
                    ret.push('el.'+p+'=="'+arr[p]+'"');
                }
            }
        }
        return ret;
    };
    /**
	 * 多条件查找游戏获取游戏
	 */
    SlotMg.showGames=function(){

        var filter=SlotMg.setFilter();

        SlotMg.getByCategory(filter.category,function(){
            var whereArr=SlotMg.getWhere(filter);
            if(whereArr.length){
                var _funStr=' return '+whereArr.join(' && ');
                var _tmpFun = new Function("el",_funStr);  // 根据动态生成查询条件,动态生成方法
/*
 * console.group('动态function字符串'); console.log(_funStr); console.groupEnd();
 */
                var _d= SlotMg.DataList.filter(_tmpFun);

                SlotMg.builHtml(_d);
            }else{
                SlotMg.builHtml(SlotMg.DataList.slice(0,100));// 最多只获取100个数据
            }

            SlotMg.setCollectState();
        });

        // JSON.stringify(getPt());
    };
    /**
	 * 游戏顶部菜单点击事件
	 */
    SlotMg.menu=function(){
        $gameMenu.find('a').on('click',function(e){
            SlotMg.reset();
            var $this=$(e.currentTarget);
            if(!$this.attr('data-value')) return;

            var value=$this.attr('data-value').replace(/"/g,'\\\"');
            var obj=$this.data('value');

            SlotMg.setActiveClass($this.closest('li'));

            if(obj.category){
                var a= $filter.find('a[data-value="'+value+'"]')
                    .trigger('click');
            }else if(obj.tag){
                var $ptTab=$('#tab-pt');
                SlotMg.reset();
                SlotMg.setActiveClass($ptTab);
                var b= $ptTab.find('a[data-value="'+value+'"]')
                    .trigger('click');

            }

        })
    };

    SlotMg.setActiveClass=function($ele){
        $ele.addClass('active').siblings().removeClass('active');
    };

    /**
	 * 过滤查询信息
	 */
    SlotMg.reset=function(){
        var _tmp=$filter.find('.search-row a:first-child');
        SlotMg.setActiveClass(_tmp);
    };
    SlotMg.getStart=function(){

    };
    /**
	 * 获取随机数
	 * 
	 * @param min
	 *            开始的数
	 * @param max
	 *            结束的数
	 * @param int
	 *            小数点位数
	 * @returns {string}
	 */
    SlotMg.getRandom=function(min, max,int) {
        var ret= Math.random() * (max - min) + min;
        int=int||0;

        return ret.toFixed(int);
    };
    SlotMg.toggleShow=function(){
        $filter.slideToggle();
    };
    /**
	 * 查找输入框
	 */
    SlotMg.search=function(){
        var $searchForm=$('#j-searchForm'), // 查找表单
            $searchIpt=$searchForm.find('.j-ipt'), // 查找输入框
            $searchSelect=$searchForm.find('.j-select'), // 查找结果显示在下拉菜单
            $searchBtn=$searchForm.find('.j-btnSearch'), // 查找按钮
            $selectAction=$searchSelect.find('a'),// 下拉菜单的item
            searchList=[];

        function get(v){
            if(!v) return;
            searchList= SlotMg.DataList.filter(function(el){
                return el.name.indexOf(v)!=-1
                    ||el.eName.toLowerCase().indexOf(v)!=-1;
            });
        }
        function buildSelect(){
            if(searchList==0){
                $searchSelect.html('').slideUp();
                return;
            }
            var _ret=[];
            $.each(searchList,function(i,o){
                _ret.push('<a href="javascript:;" data-id="'+o.id+'">'+o.name+'</a>');
                if(i==9) return false;
            });
            $searchSelect.html(_ret.join('')).slideDown();
        }

        $searchIpt.on('keyup',function () {
            searchList=[];
            var v=$searchIpt.val();
            if(v){
                get($searchIpt.val());
                buildSelect();
            }

        });
        $searchForm.on('click','.j-select a',function(e){
            var _id=$(e.currentTarget).data('id');
            $searchIpt.val($(e.currentTarget).text());

            $searchSelect.slideUp();

            searchList= SlotMg.DataList.filter(function(el){
                return el.id==_id;
            });
            SlotMg.builHtml(searchList);
            SlotMg.setCollectState();
        });

        $searchBtn.on('click',function(){
            $searchIpt.val();
            get($searchIpt.val());
            SlotMg.builHtml(searchList);
            SlotMg.setCollectState();
        });

        $searchIpt.on('focusout',function(){
            searchList=[];
            $searchSelect.slideUp();
        });
    };
    /**
	 * 获取收藏游戏
	 */
    SlotMg.queryCollectGames=function(){
        return $.getJSON('/asp/queryGameStatus.aspx');
    };

    /**
	 * 保存收藏游戏
	 */
    SlotMg.saveCollectGames=function(obj,isDel){
        if(!obj) return;
        var tmpIndex=-1;
        if(collectGames){
            $.each(collectGames, function(index, item) {
                if(item.id === obj.id) {
                    tmpIndex=index;
                    return false;
                }
            });
            if(tmpIndex!==-1&&!isDel){ // 添加模式，找不到才进行添加操作
                return;
            }
        }
        if(isDel){ // 删除操作
            collectGames.splice(tmpIndex,1);
        }else{
            collectGames.unshift(obj);
            collectGames.length>20 && collectGames.slice(0,19);
        }

        $.post('/asp/saveOrUpdateGameStatus.aspx',{'gameList':JSON.stringify(collectGames)},function(data){
        })
    };

    SlotMg.setCollectState=function(){
        if(isLogin){
            $.each(collectGames,function(index,ele){
                $('#'+ele.id).find('.collect').attr('data-state',1)
                    .html('<i class="iconfont icon-heart2"></i>已收藏');
            });
        }

    };

    SlotMg.saveHistory=function(obj){
        if(!obj) return;
        var tmp;
        if(historyGames){
            tmp= historyGames.filter(function(item){
                return item.id===obj.id;
            });
            if(tmp.length>0){
                return;
            }
        }
        historyGames.unshift(obj);
        if(historyGames.length>20){
            historyGames=historyGames.slice(0,19);
        }
        window.localStorage.setItem('hisotryGames',JSON.stringify(historyGames));
        
    };

    /**
	 * 获取试玩连接
	 * 
	 * @param obj
	 * @returns {string}
	 */
    SlotMg.getLinkDemo=function(obj){
        if(obj.state=='PLA') return '';
        if (obj.category == 'BBIN') return '';
        if (obj.category == 'CQ9') return '';
        if(obj.category=='MGS'&&obj.state=='DEM') return ''
        var _tmp='';
        switch(obj.category){ 
            case 'PT':
                _tmp='http://cache.download.banner.happypenguin88.com/casinoclient.html?mode=offline&affiliates=1&language=zh-cn&game={{id}}';
                break;
            case 'SW':
				_tmp="/game/gameLoginPtSky.php?mode=real&gameCode="+obj.id+"&lobby=" + window.location.host + "/slotGame.jsp";
				break;                     
            case 'QT':
            	_tmp = '/gameQT.aspx?gameCode={{id}}&isfun=1&type={{type}}';
				_tmp = _tmp.replace(/\{\{type\}\}/g, obj.subType === 'H5' ? '0' : '1');
				break;
            case 'NT':
                if(obj.subType&&obj.subType=='QUN'){
                    _tmp='https://cecom-static.casinomodule.com/games/wolfcub-client/game/wolfcub-client.xhtml?gameName=wolf-cub.desktop&staticServer=https%3A%2F%2Fcecom-static.casinomodule.com%2F&targetElement=gameContainer&casinoId=mrsmithcasino&allowFullScreen=false&mobileParams.lobbyURL=https%253A%252F%252Fwww.mrsmithcasino.co.uk%252Fslots&gameId={{id}}_sw&server=https%3A%2F%2Fcecom-game.casinomodule.com%2F&lang=en&sessId=DEMO-USD&operatorId=unbranded';
                }else if(obj.subType&&obj.subType=='XI'){
                    _tmp='https://cecom-static.casinomodule.com/games/bloodsuckers2_mobile_html/game/bloodsuckers2_mobile_html.xhtml?gameName=blood-suckers-2.desktop&staticServer=https://cecom-static.casinomodule.com/&targetElement=gameContainer&casinoId=mrsmithcasino&allowFullScreen=false&mobileParams.lobbyURL=https%3A%2F%2Fm.richslots.it&gameId={{id}}_sw&server=https://cecom-game.casinomodule.com/&lang=cn&sessId=&operatorId=touch&sessId=DEMO-2564448594028 ===> demo bloodsuckers2';
                }else{
                    _tmp='http://load.sdjdlc.com/disk2/netent/demo.html?game={{id}}&language=cn';
                }
                break;
            case 'TTG':
				_tmp = 'http://ams5-games.ttms.co/casino/generic/game/game.html?gameSuite=flash&gameName={{code}}&lang=zh-cn&playerHandle=999999&gameId={{id}}&account=FunAcct';
				break;
			case 'TTG-MG':
				_tmp = 'http://ams5-games.ttms.co/casino/generic/game/game.html?gameSuite=flash&gameName={{code}}&lang=zh-cn&playerHandle=999999&gameId={{id}}&account=FunAcct';
				break;
            case 'DT':
                _tmp='http://play.dreamtech8.com/playSlot.aspx?gameCode={{id}}&isfun=1&type=dt&language=zh_CN';
                break;
            case 'MGS':
                if(obj.subType=='H5'){
                    _tmp='https://mobile22.gameassists.co.uk/MobileWebServices_40/casino/game/launch/UFAcom/{{id}}/zh-ch?loginType=VanguardSessionToken&isPracticePlay=true&casinoId=2712&isRGI=true&authToken=&lobbyurl=/slotGame.jsp?showtype=MGS'
                }else{
                    _tmp='http://redirector3.valueactive.eu/Casino/Default.aspx?applicationid=1024&theme=quickfire&usertype=5&sext1=demo&sext2=demo&csid=2712&serverid=2712&variant=TNG&ul=zh&gameid={{id}}';
                }
                 break;
            case 'PNG':
                _tmp='/gamePNGFlashForTp.aspx?practice=1&gameCode={{id}}';
                break;
            case 'AG':
                _tmp = '/asp/agTryLogin.aspx?gameType={{id}}';
                break;
            case 'PG':
                _tmp="/game/pgLogin.aspx?gameCode="+obj.code+"&demoMode=1";
                break;
            default:
                break;
        }

        _tmp= _tmp.replace(/\{\{id\}\}/g,obj.id)
            .replace(/\{\{code\}\}/g,obj.code)
            .replace(/\{\{type\}\}/g,obj.type);
        return '<a href="'+_tmp+'" target="_blank" class="btn btn-demo">试玩游戏</a>';
    };
    /**
	 * 获取进入游戏连接
	 * 
	 * @param obj
	 * @returns {string}
	 */
    SlotMg.getLinkPlay=function(obj){
        if(obj.category=='DT'&&obj.state=='DEM') return ''; // 判断状态是否为试玩
        
        var _tmp='';
        switch(obj.category){
            case 'PT':
                _tmp='/loginGame.aspx?gameCode={{id}}';
                break;
            case 'SW':
				_tmp="/game/gameLoginPtSky.php?mode=real&gameCode="+obj.id+"&lobby=" + window.location.host + "/slotGame.jsp";
				break;                
            case 'QT':
            	_tmp = '/gameQT.aspx?gameCode={{id}}&isfun=0&type={{type}}';
				_tmp = _tmp.replace(/\{\{type\}\}/g, obj.subType === 'H5' ? '0' : '1');
				break;
            case 'NT':
                _tmp='/loginNT.aspx?game={{id}}';
                break;
            case 'TTG':
				_tmp = '/asp/ttLogin.aspx?gameName={{code}}&gameId={{id}}&lang=zh-cn';
				break;
			case 'TTG-MG':
				_tmp = '/asp/ttLogin.aspx?gameName={{code}}&gameId={{id}}&lang=zh-cn';
				break;
            case 'DT':
                // _tmp='{{gameurl}}/publishr/gamestart.php?slotKey={{slotKey}}&language={{language}}&gameCode={{id}}&isfun=0&closeUrl={{referWebsite}}';
                // /loginDT.aspx?&language={{language}}&gameCode={{id}}&isfun=0&language=zh_CN&clientType=0&closeUrl={{referWebsite}}&closeUrl={{referWebsite}}
                _tmp='/game/gameLoginDT.aspx?&gameCode={{id}}&isfun=0&language=zh_CN&clientType=0';
                break;
            case 'MGS':
                if(obj.subType=='H5'){
                    _tmp='/gameMGS4H5Desktop.aspx?gameCode={{id}}'
                }else{
                    _tmp="/gameMGS.aspx?itemId="+obj.itemId+"&appId="+obj.appId+"&demoMode=0";
                }
                break;
            case 'CQ9':
                _tmp="/game/cq9Login.aspx?gameCode="+obj.code;
                break;
            case 'PG':
                _tmp="/game/pgLogin.aspx?gameCode="+obj.code+"&demoMode=0";
                break;
            case 'PNG':
                _tmp='/gamePNGFlashForTp.aspx?practice=0&gameCode={{id}}';
                break;
            case 'AG':
                _tmp = '/asp/loginAgSlot.aspx?gameType={{id}}';
                break;
            case 'BBIN':
                _tmp = '/game/bbinSlotLogin.aspx?gameCode={{id}}';
                break;
            default:
                break;
        }
        _tmp= _tmp.replace(/\{\{id\}\}/g,obj.id)
            .replace(/\{\{mode\}\}/g, obj.mode)
            .replace(/\{\{code\}\}/g,obj.code)
            .replace(/\{\{type\}\}/g,obj.type)
            .replace(/\{\{itemId\}\}/g,obj.itemId)
            .replace(/\{\{appId\}\}/g,obj.appId);

        return '<a href="'+_tmp+'" target="'+obj.category+'Game" class="j-login btn btn-play">进入游戏</a>';
    };

    SlotMg.builHtml=function(data){
        var _ret=[],animaClass='';
        $.each(data,function (i, o) {
             if(i<9){
                animaClass='a-fadeinT';
            }
            var caPic=o.category.toLowerCase();
            if(o.category=='TTG-MG'){
                caPic='ttg';
            }/*
				 * else if(o.category=='TTG'){ caPic='/images/'+caPic; }
				 */else{
                caPic='/images/'+caPic;
            }
            if(o.category == 'DT' && o.subType =='jackpot'){
                var jackpot='jackpot-icon';
            }                  
            _ret.push(tpl.replace(/\{\{pic\}\}/g,o.pic)
                .replace(/\{\{name\}\}/g,o.name)
                .replace(/\{\{id\}\}/g,o.id)
                .replace(/\{\{class\}\}/g,animaClass)
                .replace(/\{\{categoryPic\}\}/g,caPic)
                .replace(/\{\{key\}\}/g,'')
                .replace(/\{\{eName\}\}/g,o.eName||'')
                .replace(/\{\{jackpot\}\}/g,jackpot)    
                .replace(/\{\{tag\}\}/g,o.tag.join(','))
                .replace(/\{\{json\}\}/g,JSON.stringify(o))
                .replace(/\{\{subType\}\}/g,o.subType)
                .replace(/\{\{score\}\}/g,SlotMg.getRandom(5,10,1))
                .replace(/\{\{collectAction\}\}/g,o.isCollect?'<i class="iconfont icon-heart2"></i>已收藏':'<i class="iconfont icon-heart"></i>添加收藏')
                .replace(/\{\{isFavorite\}\}/g,o.isCollect?' data-favorite ':'')
                /* .replace(/\{\{like\}\}/g,SlotMg.getRandom(18859,70059)) */
                .replace(/\{\{linkDemo\}\}/g,SlotMg.getLinkDemo(o))
                .replace(/\{\{linkPlay\}\}/g,SlotMg.getLinkPlay(o)));
        });
        $gameContainer.html(_ret);
        SlotMg.lazyload();

    };

    SlotMg.lazyload=function(){
        $('img.lazy').lazyload();
    };

    w.SlotMg=SlotMg;
})(window);

!function(){
    function UserMange(){

    }

    // 游戏转账
    function transferMonery(){
        var transferGameOut=$("#j-transferGameOut").val();
        var transferGameIn=$("#j-transferGameIn").val();
        var transferGameMoney=$("#j-transferGameMoney").val();

        if(transferGameOut==""){
            alert("来源账号不能为空！");
            return false;
        }
        if(transferGameIn==""){
            alert("目标账号不能为空！");
            return false;
        }

        if(transferGameMoney==""){
            alert("转账金额不能为空！");
            return false;
        }
        if(isNaN(transferGameMoney)){
            alert("转账金额只能是数字!");
            return false;
        }
        if(transferGameOut=="ld" && transferGameIn=="ld"){
            alert("天威账户不能转账到天威账户！");
            return false;
        }
        /*
		 * if(transferGameIn=='newpt' && parseInt(transferGameMoney)<20){
		 * alert('PT转入金额不能少于20元'); return false; }
		 */
        if(transferGameOut=="ld" || transferGameIn=="ld"){
            openProgressBar();
            $.post("/asp/updateGameMoney.aspx", {
                "transferGameOut":transferGameOut,
                "transferGameIn":transferGameIn,
                "transferGameMoney":transferGameMoney
            }, function (returnedData, status) {
                if ("success" == status) {
                    // transferMoneryOut(transferGameOut);
                    // transferMoneryIn(transferGameIn);
                    closeProgressBar();
                    alert(returnedData);
                }
            });
        }else{
            alert("游戏之间不能对转！");
            return false;
        }
    }

    function init(){
        $('#j-transferAction').click(function(){
            transferMonery();
        });
    }

    init();
}();

$(function () {
    jackPot();
    getWinner();
    var type=Util.getQueryString('showtype'),
        $gameMenu=$('#j-gameMenu');
    SlotMg.init();
    if(type){
        $gameMenu.find('a[data-tab="'+type+'"]').trigger('click');
    }else{
        $gameMenu.find('a[data-tab="PT"]').trigger('click');
    }
 
    // subType="DT-1" 类型的老虎机判断
    $(document).on('click','.game-info[data-subtype="DT-1"] .btn',function(){
        if(isIE()||!checkwebgl()){ // ie 浏览器及不支持webgl的
            $('#j-tip').modal('show');
            return false;
        }
        return true;
    });
 
    // 判断是否支持webgl
    function checkwebgl(){
        var cvs = document.createElement('canvas');
        var contextNames = ['webgl','experimental-webgl','moz-webgl','webkit-3d'];
        var ctx;
        if(navigator.userAgent.indexOf('MSIE') >= 0) {
            try{
                ctx = WebGLHelper.CreateGLContext(cvs, 'canvas');
            }catch(e){}
        } else{
            for(var i = 0; i < contextNames.length; i++){
                try{
                    ctx = cvs.getContext(contextNames[i]);
                    if(ctx){
                        break;
                    }
                }catch(e){

                }
            }
        }
        if(ctx){
            return true;
        }
        return false;
    }
    // ie? 判断是否是ie浏览器
    function isIE() {
        if (!!window.ActiveXObject || "ActiveXObject" in window)
            return true;
        else
            return false;
    }
    function jackPot(){
        if(CountUp){
            var options = {
                useEasing : true,
                useGrouping : true,
                separator : ',',
                decimal : '.',
                prefix : '',
                formate : true,
                suffix : ''
            };
            var demo = new CountUp("j-jackpotCount1", 219457295, 919457295, 2, 50000000000, options);
            demo.start();
        }

    }

        /**
		 * 获取中奖名单
		 */
    function getWinner(){
        $.getJSON('/data/winner.json',function (data) {
            var tmp= builHtml(data);
            var $winnerMarquee=$('#j-winner-box');
            $winnerMarquee.html(tmp);

            Page.Marquee($winnerMarquee,30,'top');

            function builHtml(data){
                var htmlArr=[],
                    tpl=$('#tpl').html();
                for(var i=0;i<data.length;i++){
                    var  obj=data[i];
                    obj.win=numberUpperFormat(obj.win||0);
                    htmlArr.push(tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
                        return obj&&obj[$1]||'';
                    }));
                }

                return htmlArr.join('');
            }
        });
    }
    /**
	 * 数字转化为中文单位
	 * 
	 * @param input
	 * @returns {string}
	 * @constructor
	 */
    function numberUpperFormat(input) {
        // num - 位数
        // 简单理解后面要有三个0，则是千，4个零，则是万。当然不一定是零，位数到了就行，反正都会省略掉（未做四舍五入）
        // 可以随意的增删改units内容实现单位的配置
        var units = [
            {num: 3, unit: '千'},
            {num: 4, unit: '万'},
            {num: 6, unit: '百万'},
            {num: 7, unit: '千万'},
            {num: 8, unit: '亿'}
        ];
        // 精确到整数，直接用js自带方法input.toFixed(0)也可以
        var num = parseFloat(input).toFixed(0);
        if (num.length <= 4) {
            // 项目相关需求，万以下不加单位
            return num;
        }
        // 保证前面至少留一位
        var len = num.length - 1;
        var isFind = false;
        for (var i = 0, length = units.length; i < length; i++) {
            var item = units[i];
            if (len >= item.num && len < units[i + 1].num) {
                isFind = true;
            } else if (i === (length - 2)) {
                isFind = true;
                item = units[++i];
            }
            if (isFind) {
                // 确认区间后，返回前几位加上单位
                // return ;
                return num.slice(0,num.length-item.num)+item.unit;
            }
        }
    }
});