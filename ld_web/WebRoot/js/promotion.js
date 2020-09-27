(function () {
     
    var tpl=[
        '<div data-type="{{type}}" class="promotion-item {{class}}" data-id="{{id}}">',
          '<a class="promotion-info">',
            '<div class="pic"> <img class="lazy w" width="264" height="163" data-original="{{image}}"> </div>',  
            '<div class="promotion-desc">',
              '<h1 class="promotion-tit"> {{title}}</h1>',
            '</div>',
	          '<div class="go_btn_box">',
	          	'<a class="go_btn" data-url="{{url}}" data-id="{{id}}" {{link}}>查看详情</a>',
	          	'<div class="promotion_title">{{title}}</div>',
	          '</div>',               
          '</a>',
          '<div class="promotion-content modal fade in" data-load="0" id="modal-{{id}}" tabindex="-1" role="dialog" data-modal-load="" aria-labelledby="myModalLabel" ><div class="text-center"> <img src="/images/loading.gif" width="80" height="80" alt=""> </div> </div>',
        '</div>'].join('');

    var config={
        'version':'0102',
        'dataUrl':'/data/promotion/newpromotion.json', 
        'menu':'#j-menu',
        'listWrapper':'#j-promotion-list',
        'infoClass':'.go_btn_box',
        'contentClass':'.promotion-content',
        'itemClass':'.promotion-item'
    };
    function getData(){ 
        var ret=[];
        var item= $('.promotion-list').find('.promotion-item');
        $.each(item,function(){
            var $this=$(this),
                title=$this.find('.promotion-tit').html(),
                img=$this.find('.pic img').attr('data-original'),
                time='2016-07-01';
            ret.push({"type"    : "|SL",
                "title" : title,
                "url"  : img,
                "image": img,
                "link":"",
                "startDate" : "2016-07-01",
                "endDate" : "2016-07-30"
            });
        });
    }

    function Promotion(){
        if(!this instanceof Promotion){
            return new Promotion();
        }
        this.$menu=$(config.menu);
        this.$menuItem=this.$menu.find('a');
        this.$promotions=null;
        this.$listWrapper=$(config.listWrapper);
        this.tplHtml=tpl;

        //
        //this.init();
      /*  this.navToggle();  //侧边菜单点击事件
        this.infoToggle();  //活动详情点击事件
        this.infoHide(); // 活动收缩点击事件*/
    }

    /**
     * 获取相差的天数
     */
    Promotion.prototype._diffDay=function(startDate,endDate){
        var convertToDate=function(tmp){
           if(Object.prototype.toString.call(tmp).toLowerCase()==='[object date]') return tmp;
            return new Date(tmp);
        };
        var sD =convertToDate(startDate),
            eD =convertToDate(endDate);

        return parseInt((sD.getTime()-eD.getTime())/(24*60*60*1000));
    };
    /**
     * 获取url参数值
     */
    Promotion.prototype.getQueryString=function (name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    };

    /**
     * 优惠活动侧边菜单点击事件
     */
    Promotion.prototype.navToggle=function (){
        var _this=this;
        this.$menuItem.on('click',function(){
            var type=$(this).data('type');
            var parent=$(this).parent();
            parent.toggleClass('active').siblings().removeClass('active');
            _this.$promotions.hide();

            _this.showByType(type);
            return false;
        });
    };
    Promotion.prototype.lazyload=function(){
        $('img.lazy').lazyload();
    };
    /**
     * 根据类型显示活动
     */
    Promotion.prototype.showByType=function(type){
        type=type||'ALL';
        if(type==='ALL'){
            $.each(this.$promotions,function(index,ele){
                var $this=$(ele);
                $this.data('type').indexOf('|OVER')==-1?$this.fadeIn():$this.hide();
            });
            this.lazyload();
        }else{
            $.each(this.$promotions,function(index,ele){
                var $this=$(ele);
                $this.data('type').indexOf('|'+type)!=-1?$this.fadeIn():$this.hide();
            });
            this.lazyload();
        }
    };
    /**
     * 活动详情的显示或者隐藏
     */
    Promotion.prototype.infoToggle=function(){
        $(document).on('click',config.infoClass,function(e){
            var _$current=$(e.currentTarget); 
            console.log(_$current)
            var url= _$current.find(".go_btn").data('url');
            console.log(url)
            var $content=_$current.parents('.promotion-item').find('.promotion-content')
            if(!url) return;
            // 已经加载过的return
            if($content.data('load')==='1') return;
            var show=function(data){
                $content.attr('data-load',1).html(data);
                
            };

            $.get(url,show).fail(function () {
                throw '无法获取的活动详情:'+url;
            });

            //return false;
        });
    };

    Promotion.prototype.show=function(id){
        var _this=this;
        function showById(id) {
            var $target=_this.$promotions.filter('[data-id="'+id+'"]');
            if($target.length  ==0) return;
            var  top=$target.offset().top;

            $('html, body').stop().animate({
                scrollTop: top
            },1200);
            $target.find(config.go_btn_box).trigger('click');
        }
        function callback(data) {
            if(data.success){
                var _data=data.data,
                    html=[],
                    currentDate=new Date();

                for(var i=0;i<_data.length;i++){
                    var  obj=_data[i]; //距离结束几天
                    if(obj.startDate){ // 不是长期有效的活动
                        /*if(new Date(obj.startDate)>currentDate || new Date(obj.endDate)<currentDate){
                            continue; //不在时间段的不显示出来
                        }*/
                    }

                    html.push(_this.tplHtml.replace(/\{\{title\}\}/g,obj.title)
                        .replace(/\{\{url\}\}/g,obj.url?obj.url+'?v='+config.version:'')
                        .replace(/\{\{type\}\}/g,obj.type)
                        .replace(/\{\{id\}\}/g,obj.id)
                        .replace(/\{\{endDate\}\}/g,obj.startDate===1?'本活动长期有效':obj.startDate+''+obj.endDate)
                       .replace(/\{\{link\}\}/g,obj.link?'href="'+obj.link+'" target="_blank" ':'href="javascript:;"data-toggle="modal" data-target="#modal-'+obj.id+'"')
                        .replace(/\{\{image\}\}/g,obj.image+'?v='+config.version));
                }
				
                _this.$promotions=_this.$listWrapper.html(html.join('')).find(config.itemClass);

                _this.navToggle();
                _this.infoToggle();
                _this.infoHide();

                _this.$menuItem.first().trigger('click');
                id&&showById(id);
            }

        }
        //获取活动列表
        $.getJSON(config.dataUrl+'?v='+config.version,callback).fail(function () {
            throw '无法获取到活动列表'+config.dataUrl;
        });

    };
    /**
     * 隐藏活动详情按钮事件
     */
    Promotion.prototype.infoHide=function(){
        $(document).on('click',config.contentClass+' .btn-reback',function(e){
            var _$current=$(e.currentTarget);
            var top= _$current.closest(config.itemClass).offset().top;
            _$current.closest(config.contentClass).slideUp();
            $('html, body').animate({
                scrollTop: top
            }, 350);
			
            return false;
        });
    };
    Promotion.prototype.init=function () {
        //var _this=this;

    };

    root = typeof exports !== "undefined" && exports !== null ? exports : window;

    root.Promotion = Promotion;
}).call(this);

$(function () {
    var p=new Promotion(),
        id = p.getQueryString('showid');
    p.show(id);
});
