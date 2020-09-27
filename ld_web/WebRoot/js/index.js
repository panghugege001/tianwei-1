$(function () {
    function Init() {
        //closeTopAd();
        //showTip();
        getWinner();
        //asideBar(); 
		galleryInit()
        getNotice();
        jackPot();
        getPost();
        tabChange();
    } 
 
    Init();
    $('#ts-know').click(function () {
        if($(this).prop("checked") == true){
            sessionStorage.setItem('first', 'on');
        }else {
            sessionStorage.setItem('first', null);
        }
        
        
    }) 

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
            var demo = new CountUp("j-jackpotCount", 219457295, 919457295, 2, 50000000000, options);
            demo.start();
        }

    }

    function getNotice(){
        var $indexModal = $('#modal-index'),
            $activityModal = $('#modal-activity'),
            picList = {
                type1: '/images/modal-activity-1.png',
                type2: '/images/index/tanchang_bj.png',
                type3: '/images/modal-activity-newyear.png',
                type4: '/images/modal-activity-christmas.png'
            };
        $.post('/asp/checkConfigSystem.aspx',
            {'typeNo':'type002','itemNo':'001'},
            function (data) {
                if (data) {
                    var strs = data.split('#'),
                        tit = strs[0],
                        content = strs[1],
                        type = '';
                    content = content.substring(0, content.length - 1);

                    $indexModal.find('.notice-cnt').html(content);

                    if (tit.indexOf('1.') != -1) {
                        type = 'type1';
                    }
                    if (tit.indexOf('2.') != -1) {
                        type = 'type2';
                        $indexModal.addClass('type2');
                    }
                    if (tit.indexOf('3.') != -1) {
                        type = 'type3';
                        $indexModal.addClass('type3');
                    }

                    if (type) {
                        tit = tit.substring(2, tit.length);
                        $indexModal.find('.notice-tit').html(tit);
                        if (sessionStorage.getItem('first') == 'on') {
                            return false;
                        }else {
                            setTimeout(showModal($indexModal, picList[type]), 800);
                        }
                    }
                }
            });
    }

    function asideBar(){
        //六周年广告
        var adSexennial = $('#ad-sexennial');
        setTimeout(function () {
            adSexennial.addClass('active');
        }, 1000);

        adSexennial.find('.close').on('click', function () {
            adSexennial.removeClass('active');
        });
    }

    /**
     * 显示首页弹框
     * @param obj
     * @param picurl
     */
    function showModal(obj, picurl) {
        if (picurl) {
            var img = new Image();
            img.src = picurl;
            //obj.find('.modal-dialog').css('background-image', 'url(' + picurl + ')');
            /*图片加载完成后显示提示框*/
            $(img).load(function () {
                obj.modal('show');
                fireworks();
            });
        } else {
            obj.modal('show');
            fireworks();
        }
    }

	// 图片切换
    function galleryInit(){
        var $target=$('#j-gallery'),
            $item=$target.find('.gallery-item');

        function setActive() {
            $(this).addClass('active').siblings().removeClass('active');
            $(this).find('.gallery-icon').hide()
            $(this).siblings().find('.gallery-icon').show();
        }

        $item.on('click mouseover',setActive);
        //$item.on('mouseover',setActive);
    }	
	
    /**
     * 论坛、活动日历
     */
    function tabChange(){
        $('#post-box .title li').hover(function(){
            $(this).addClass('active').siblings().removeClass('active');

            $('#post-box .post-list-box ul').eq($(this).index()).show().siblings().hide();
        })
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
     * 帖子信息
     */
    function getPost(){
        /*$.getJSON('/asp/queryBbsTie.aspx',{type:'2,37,38',size:6},function (data) { 
        	console.log(data)
            var tmp= builHtml(data);
            var $postList=$('#j-postList');
            $postList.html(tmp);

        });

        function builHtml(data){
            var html='',
                tpl=$('#j-tplPost').html(),
                link='http://www.longdobbs.com/forum.php?mod=viewthread&tid=';
            for(var i=0;i<data.length;i++){
                var  obj=data[i];
                html += tpl.replace(/\{\{link\}\}/g,link+obj.id+'&extra=page=1')
                    .replace(/\{\{title\}\}/g,obj.title)
                    .replace(/\{\{createDate\}\}/g,obj.createDate);
            }

            return html;
        }*/
    }

    function closeTopAd() {
        var $adtop = $('#ad-top'),
            $snow = $('#ele-snow');
        $adtop.find('.close').on('click', function () {
            $adtop.slideUp(150, function () {
                $snow.css('height', 148);
            });
        });
    }

    function showTip(){
        var $tip=$('#j-check-tip');
        if($tip.length){
            setTimeout(function(){
                $tip.addClass('active');
                setTimeout(function(){
                    $tip.hide(500);
                },10000);
            },1200);
        }
    }

    function banner(){
       //轮播广告组件 //angelosong
		var mySlider = function (opt) {
			this.sliderID = document.getElementById(opt.sliderID);
			this.picID = document.getElementById(opt.sliderID+'-pic'),this.pic = this.picID.getElementsByTagName('li');
			this.navID = document.getElementById(opt.sliderID+'-nav'),this.nav = this.navID.getElementsByTagName('li');
			if(opt.textNav)this.textNav = true,this.textID = document.getElementById(opt.sliderID+'-text'),this.text = this.textID.getElementsByTagName('li');
			if(opt.textSlider)this.textSlider = true,this.textWidth = opt.textWidth,this.textNum = opt.textNum;
			if(opt.arrow)this.prev = document.getElementById(opt.sliderID+'-prev'),this.next = document.getElementById(opt.sliderID+'-next');
			this.speed = opt.speed ? opt.speed : 3000;
			this.sliderProgress();
		}
		mySlider.prototype.sliderProgress = function () {
			var self = this,sn = 1,nn = 0;
			//轮播切换
			var chg = function (num) {
				for(i=0;i<self.pic.length;i++){
					self.pic[i].className='';
					self.nav[i].className='';
					if(self.text)self.text[i].className='';
				}
				self.pic[num].className='on';
				self.nav[num].className='on';
				if(self.text)self.text[num].className='on';
				if(self.textSlider){//轮播广告文字滑条
					var tn = self.textNum-1;
					if(num>tn){self.textID.style.marginLeft='-'+260*(num-tn)+'px'}
					//else if(num==4){self.textID.style.marginLeft='-520px'}
					else {self.textID.style.marginLeft='0'}
				}
				nn=num;
			}
			//轮播自动执行
			var auto=function(){
				chg(sn);
				sn++;
				if(sn==self.pic.length)sn=0;
			}
			//轮播计数器
			var start=function(){self.pic.move = setInterval(auto,self.speed)}
			//轮播停止
			var end=function(){clearInterval(self.pic.move)}
			//轮播导航
			for(j=0;j<self.pic.length;j++){
				self.nav[j].num=j;
				self.nav[j].onmouseover=function(){
					end();
					sn=this.num;
					chg(sn);
				}
				if(self.textNav){//轮播广告文字滑条
					self.text[j].num=j;
					self.text[j].onmouseover=function(){
						end();
						sn=this.num;
						chg(sn);
					}
				}
			}
			//轮播箭头
			if(self.prev){
				self.prev.onclick=function(){
					end();
					nn--;
					if(nn<0)nn=self.pic.length-1;
					chg(nn);
				}
				self.next.onclick=function(){
					end();
					nn++;
					if(nn==self.pic.length)nn=0;
					chg(nn);
				}
			};
			//轮播悬停停止/运行
			self.sliderID.onmouseover = end;
			self.sliderID.onmouseout = start;
			//执行
			start();
		}
		//焦点轮播
		var newSlider = new mySlider({
			sliderID:'focus',//轮播ID
			//speed:200,//轮播速度，默认3秒
			textNav:true,//是否有文字导航
			textSlider:true,//文字导航是否超出一屏
			textWidth:260,//每个文字导航的宽度
			textNum:3,//文字导航的同屏个数
			arrow:true//是否有左右箭头
		})
		
    }

    function fireworks(){
        if(!window.requestAnimationFrame){
            return false;
        }

        // when animating on canvas, it is best to use requestAnimationFrame instead of setTimeout or setInterval
// not supported in all browsers though and sometimes needs a prefix, so we need a shim
        window.requestAnimFrame = ( function() {
            return window.requestAnimationFrame ||
                window.webkitRequestAnimationFrame ||
                window.mozRequestAnimationFrame ||
                function( callback ) {
                    window.setTimeout( callback, 1000 / 60 );
                };
        })();

 

// now we are going to setup our function placeholders for the entire demo

 
 

// create firework
        function Firework( sx, sy, tx, ty ) {
            // actual coordinates
            this.x = sx;
            this.y = sy;
            // starting coordinates
            this.sx = sx;
            this.sy = sy;
            // target coordinates
            this.tx = tx;
            this.ty = ty;
            // distance from starting point to target
            this.distanceToTarget = calculateDistance( sx, sy, tx, ty );
            this.distanceTraveled = 0;
            // track the past coordinates of each firework to create a trail effect, increase the coordinate count to create more prominent trails
            this.coordinates = [];
            this.coordinateCount = 3;
            // populate initial coordinate collection with the current coordinates
            while( this.coordinateCount-- ) {
                this.coordinates.push( [ this.x, this.y ] );
            }
            this.angle = Math.atan2( ty - sy, tx - sx );
            this.speed = 2;
            this.acceleration = 1.05;
            this.brightness = random( 50, 70 );
            // circle target indicator radius
            this.targetRadius = 1;
        }

// update firework
        Firework.prototype.update = function( index ) {
            // remove last item in coordinates array
            this.coordinates.pop();
            // add current coordinates to the start of the array
            this.coordinates.unshift( [ this.x, this.y ] );

            // cycle the circle target indicator radius
            if( this.targetRadius < 8 ) {
                this.targetRadius += 0.3;
            } else {
                this.targetRadius = 1;
            }

            // speed up the firework
            this.speed *= this.acceleration;

            // get the current velocities based on angle and speed
            var vx = Math.cos( this.angle ) * this.speed,
                vy = Math.sin( this.angle ) * this.speed;
            // how far will the firework have traveled with velocities applied?
            this.distanceTraveled = calculateDistance( this.sx, this.sy, this.x + vx, this.y + vy );

            // if the distance traveled, including velocities, is greater than the initial distance to the target, then the target has been reached
            if( this.distanceTraveled >= this.distanceToTarget ) {
                createParticles( this.tx, this.ty );
                // remove the firework, use the index passed into the update function to determine which to remove
                fireworks.splice( index, 1 );
            } else {
                // target not reached, keep traveling
                this.x += vx;
                this.y += vy;
            }
        }

// draw firework
        Firework.prototype.draw = function() {
            ctx.beginPath();
            // move to the last tracked coordinate in the set, then draw a line to the current x and y
            ctx.moveTo( this.coordinates[ this.coordinates.length - 1][ 0 ], this.coordinates[ this.coordinates.length - 1][ 1 ] );
            ctx.lineTo( this.x, this.y );
            ctx.strokeStyle = 'hsl(' + hue + ', 100%, ' + this.brightness + '%)';
            ctx.stroke();

            ctx.beginPath();
            // draw the target for this firework with a pulsing circle
            ctx.arc( this.tx, this.ty, this.targetRadius, 0, Math.PI * 2 );
            ctx.stroke();
        }

// create particle
        function Particle( x, y ) {
            this.x = x;
            this.y = y;
            // track the past coordinates of each particle to create a trail effect, increase the coordinate count to create more prominent trails
            this.coordinates = [];
            this.coordinateCount = 5;
            while( this.coordinateCount-- ) {
                this.coordinates.push( [ this.x, this.y ] );
            }
            // set a random angle in all possible directions, in radians
            this.angle = random( 0, Math.PI * 2 );
            this.speed = random( 1, 10 );
            // friction will slow the particle down
            this.friction = 0.95;
            // gravity will be applied and pull the particle down
            this.gravity = 1;
            // set the hue to a random number +-50 of the overall hue variable
            this.hue = random( hue - 50, hue + 50 );
            this.brightness = random( 50, 80 );
            this.alpha = 1;
            // set how fast the particle fades out
            this.decay = random( 0.015, 0.03 );
        }

// update particle
        Particle.prototype.update = function( index ) {
            // remove last item in coordinates array
            this.coordinates.pop();
            // add current coordinates to the start of the array
            this.coordinates.unshift( [ this.x, this.y ] );
            // slow down the particle
            this.speed *= this.friction;
            // apply velocity
            this.x += Math.cos( this.angle ) * this.speed;
            this.y += Math.sin( this.angle ) * this.speed + this.gravity;
            // fade out the particle
            this.alpha -= this.decay;

            // remove the particle once the alpha is low enough, based on the passed in index
            if( this.alpha <= this.decay ) {
                particles.splice( index, 1 );
            }
        }

// draw particle
        Particle.prototype.draw = function() {
            ctx. beginPath();
            // move to the last tracked coordinates in the set, then draw a line to the current x and y
            ctx.moveTo( this.coordinates[ this.coordinates.length - 1 ][ 0 ], this.coordinates[ this.coordinates.length - 1 ][ 1 ] );
            ctx.lineTo( this.x, this.y );
            ctx.strokeStyle = 'hsla(' + this.hue + ', 100%, ' + this.brightness + '%, ' + this.alpha + ')';
            ctx.stroke();
        }

// create particle group/explosion
        function createParticles( x, y ) {
            // increase the particle count for a bigger explosion, beware of the canvas performance hit with the increased particles though
            var particleCount = 30;
            while( particleCount-- ) {
                particles.push( new Particle( x, y ) );
            }
        }

// main demo loop
        function loop() {
            // this function will run endlessly with requestAnimationFrame
            requestAnimFrame( loop );

            // increase the hue to get different colored fireworks over time
            //hue += 0.5;

            // create random color
            hue= random(0, 360 );

            // normally, clearRect() would be used to clear the canvas
            // we want to create a trailing effect though
            // setting the composite operation to destination-out will allow us to clear the canvas at a specific opacity, rather than wiping it entirely
            ctx.globalCompositeOperation = 'destination-out';
            // decrease the alpha property to create more prominent trails
            ctx.fillStyle = 'rgba(0, 0, 0, 0.5)';
            ctx.fillRect( 0, 0, cw, ch );
            // change the composite operation back to our main mode
            // lighter creates bright highlight points as the fireworks and particles overlap each other
            ctx.globalCompositeOperation = 'lighter';

            // loop over each firework, draw it, update it
            var i = fireworks.length;
            while( i-- ) {
                fireworks[ i ].draw();
                fireworks[ i ].update( i );
            }

            // loop over each particle, draw it, update it
            var i = particles.length;
            while( i-- ) {
                particles[ i ].draw();
                particles[ i ].update( i );
            }

            // launch fireworks automatically to random coordinates, when the mouse isn't down
            if( timerTick >= timerTotal ) {
                if( !mousedown ) {
                    // start the firework at the bottom middle of the screen, then set the random target coordinates, the random y coordinates will be set within the range of the top half of the screen
                    fireworks.push( new Firework( cw / 2, ch, random( 0, cw ), random( 0, ch / 2 ) ) );
                    timerTick = 0;
                }
            } else {
                timerTick++;
            }

            // limit the rate at which fireworks get launched when mouse is down
            if( limiterTick >= limiterTotal ) {
                if( mousedown ) {
                    // start the firework at the bottom middle of the screen, then set the current mouse coordinates as the target
                    fireworks.push( new Firework( cw / 2, ch, mx, my ) );
                    limiterTick = 0;
                }
            } else {
                limiterTick++;
            }
        }
 


    }

    /**
     * 数字转化为中文单位
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


(function(jQuery) {

    $.fn.BreakingNews = function(settings) {
        var defaults = {
            title: '公告',
            width: '100%',
            autoplay: true,
            timer: 3000,
            modulid: 'brekingnews',
            effect: 'fade' //or slide
        };
        var settings = $.extend(defaults, settings);

        return this.each(function() {
            settings.modulid = "#" + $(this).attr("id");
            var timername = settings.modulid;
            var activenewsid = 1;

            if (settings.effect == 'slide')
                $(settings.modulid + ' ul li').css({
                    'display': 'block'
                });
            else
                $(settings.modulid + ' ul li').css({
                    'display': 'none'
                });

            $(settings.modulid + ' .bn-title').html(settings.title);
            $(settings.modulid).css({
                'width': settings.width
            });
            $(settings.modulid + ' ul').css({
                'left': $(settings.modulid + ' .bn-title').width() + 0
            });
            $(settings.modulid + ' ul li').eq(parseInt(activenewsid - 1)).css({
                'display': 'block'
            });

            // Arrows Click Events ......
            $(settings.modulid + ' .btn-arrows span').click(function(e) {
                if ($(this).attr('class') == "btn-arrows-left")
                    BnAutoPlay('prev');
                else
                    BnAutoPlay('next');
            });

            // Timer events ...............
            if (settings.autoplay == true) {
                timername = setInterval(function() {
                    BnAutoPlay('next')
                }, settings.timer);
                $(settings.modulid).hover(function() {
                        clearInterval(timername);
                    },
                    function() {
                        timername = setInterval(function() {
                            BnAutoPlay('next')
                        }, settings.timer);
                    }
                );
            } else {
                clearInterval(timername);
            }

            //timer and click events function ...........
            function BnAutoPlay(pos) {
                if (pos == "next") {
                    if ($(settings.modulid + ' ul li').length > activenewsid)
                        activenewsid++;
                    else
                        activenewsid = 1;
                } else {
                    if (activenewsid - 2 == -1)
                        activenewsid = $(settings.modulid + ' ul li').length;
                    else
                        activenewsid = activenewsid - 1;
                }

                if (settings.effect == 'fade') {
                    $(settings.modulid + ' ul li').css({
                        'display': 'none'
                    });
                    $(settings.modulid + ' ul li').eq(parseInt(activenewsid - 1)).fadeIn();
                } else {
                    $(settings.modulid + ' ul').animate({
                        'marginTop': -($(settings.modulid + ' ul li').height()) * (activenewsid - 1)
                    });
                }
            }

        });

    }; 
 
})(jQuery);
$("#breakingnews").BreakingNews({
    timer: 3000,
    width: '475px',
    autoplay: true,
    effect: "slide"
});
 

   		$(".index_title").find("li").click(function(){
 			$(".index_center_no3").find(".post-list-box").hide();
 			$(".index_center_no3").find(".post-list-box").eq($(this).index()).show();

  		})  

