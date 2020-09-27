$(function () {
    const TESTMODEL = 0;//测试
    var getAllLuckyDrawPresent,
        getAllPointsPresents,
        luckyDraw

    if (TESTMODEL) {
        getAllLuckyDrawPresent = './data/getAllLuckyDrawPresent.json'
        getAllPointsPresents = './data/getAllPointsPresents.json'
        luckyDraw = './data/luckyDraw.json'
    } else {
        getAllLuckyDrawPresent = '/points/getAllLuckyDrawPresent.aspx'
        getAllPointsPresents = '/points/getAllPointsPresents.aspx'
        luckyDraw = '/points/luckyDraw.aspx'
    }

    // 等级图
    $('.level-image').attr('src', '/images/youhui/le-' + (levelNum + 1) + '.png');
    var $icon = $('.p-welfare a'); //icon

    if(!levelNum){
        $icon.find('div').css('opacity','.5')
    }
    if(levelNum<2){
        $icon.eq(0).find('div').css('opacity','.5')
    }
    if(levelNum<1) {
        $icon.eq(1).find('div').css('opacity', '.5')
    }


    // // icon提示
    // $icon.hover(function(){
    //     $(this).find('.tip').fadeIn(150)
    // },function(){
    //     $(this).find('.tip').fadeOut(150)
    // })

    // 我的兑换记录
    $.getJSON('/points/pointsRecord.aspx', {flag: 2}, function (data) {
        var $dataBox = $('.myrecord-list-box ul');
        if (data.code === CODE) {
            if (data.data.length) { 
                var dataList = data.data;
                var html = '';
                for (var i = 0; i < dataList.length; i++) {
                    var item = dataList[i]
                    html += '<li>' + item.updateTime + ' 兑换 ' + item.name + '</li>'
                }
                $dataBox.html(html);

                if(data.data.length>20){
                    // 滚动
                    jQuery(".list").slide({
                        mainCell: ".bd ul",
                        effect: "top",
                        interTime: 1500,
                        autoPlay: true,
                        autoPage: true
                    });
                    $('.tempWrap').css('height', 'auto')
                }
            }else{
                $dataBox.append('<li class="no-data">暂无数据</li>')
            }

        }
    })

    // 兑换记录
    $('.open-exchange-record').click(function () {
        showRecord(2)
    })

    // 会员中奖信息
    $.getJSON('/points/pointsRecord.aspx', {flag: 3}, function (data) {
        var $dataBox = $('.user-record .list ul');
        if (data.code === CODE) {
            if (data.data.length) {
                var dataList = data.data.slice(0, 100);
                var html = '';
                for (var i = 0; i < dataList.length; i++) {
                    var item = dataList[i]
                    var property = filterAttrVal(item.luckyDrawPresentType, item.property)
                    if (item.luckyDrawPresentType == 'money' || item.luckyDrawPresentType == 'phoneFee' || item.luckyDrawPresentType == 'phoneData') {
                        property = ''
                    }
                    html += '<li>恭喜会员 ' + formatStr(item.loginName, 2, 2, 4) + ' 抽中 ' + item.name + ' ' + property + '</li>'
                }
                $dataBox.html(html);

                if(data.data.length>20){
                    // 滚动
                    jQuery(".list").slide({
                        mainCell: ".bd ul",
                        effect: "top",
                        interTime: 1500,
                        autoPlay: true,
                        autoPage: true
                    });
                    $('.tempWrap').css('height', 'auto')
                }
            }else{
                $dataBox.append('<li class="no-data">暂无数据</li>')
            }

        } else {
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class',
                content: data.data
            })
        }
    })

    // 抽奖记录
    $('.lottery-record').click(function () {
        showRecord(1)
    })

    // 抽奖轮盘
    $.getJSON(getAllLuckyDrawPresent, {}, function (data) {
        var $lotteryBox = $('.lotto-box')
        if (data.code === CODE) {
            var html = '';
            var lottoList = [];//奖品id列表
            $(data.data).each(function (i, dom) {
                html += '<div class="lotto-item">\
                            <img src="' + dom.imageUrl + '" alt=""/>\
                            <p>' + dom.title + '</p>\
                        </div>'
                lottoList.push(dom.id)
            })
/*            html += '<div class="lotto-btn">\
                        <h3>点击抽奖</h3>\
                        <p><span class="point-expend">请先登录</span></p>\
                    </div>'*/

/*            html += '<div class="lotto-mid-box">\
                        <a class="pression-rule" onclick="OpenLayer();">规则</a>\
                        <div class="mid-cont-box">\
                            <i class="zuanshi"></i>\
                            <div class="text-box">\
                                <h1>当前积分<span class="point-expend">请先登录</span></h1>\
                                <p>8000积分可以兑换一次抽奖机会</p>\
                                <p>剩余抽奖次数<span class="c=gold">【</span><span class="c-gold have-times"> 2 </span><span class="c-gold">】次</span></p>\
                            </div>\
                            <div class="lotto-btn"></div>\
                        </div>\
                    </div>'*/
            $lotteryBox.html(html)

            // 点击抽奖
            var flagVar
            $('.lotto-btn').click(function () {
                if (!isLogin()) return;// 未登录

                if (!flagVar) {//首次点击
                    flagVar = true;
                    var html = '<div class="goodNews-wrapper">\
                                    <div class="property-tip">每次抽奖将消耗 <span class="red">' + parseInt(expendPoints) + '</span> 积分</div>\
                                </div>'
                    layer.open({
                        title: ['温馨提示'],
                        skin: 'top-class integral',
                        btn: '知道了',
                        //area: ['400px', '150px'],
                        content: html
                    })
                    return
                }

                if (canPlayLotto()) {//积分不够
                    var html = '<div class="goodNews-wrapper">\
                                    <div class="property-tip">积分不够啦，快去存款赚积分吧</div>\
                                </div>'
                    layer.open({
                        title: ['温馨提示'],
                        skin: 'top-class integral',
                        btn: '打流水赚积分',
                        content: html,
                        yes: function () {
                            window.open('/slotGame.jsp')
                        }

                    })
                } else {

                    var $index = layer.load();
                    // $('.lucky-lottery').addClass('active-obj-box');//开启彩灯，需加背景彩灯
                    $.getJSON(luckyDraw, function (data) {
                        if (data.code === CODE) {
                            var start = 40; //间隔时间 (不能取值50,dom渲染机制)
                            var circleNum = 8; //圈数
                            var proNum = 8; //奖品数量
                            var resultArray = []; //中奖列表
                            for (var i = 0; i < proNum; i++) {
                                resultArray.push(start * proNum * circleNum + i * start)
                            }
                            var resultIdx = lottoList.indexOf(data.data.luckyDrawPresentId); //中奖结果索引
                            var result = resultArray[resultIdx]; //中奖结果
                            var type = data.data.luckyDrawPresentType; //奖品类型
                            var resultProperty = filterAttrVal(type, data.data.property); //奖品属性值转换
                            var idx = 0; //起始位置
                            var $lottos = $('.lotto-box .lotto-item');

                            // 启动游戏
                            var timer = setInterval(function () {
                                idx++;
                                if (idx === $lottos.length) {
                                    idx = 0;
                                }
                                // 视图更新
                                $lottos.eq(idx).addClass('active').siblings().removeClass('active')
                            }, start)

                            //指定时间结束游戏
                            var timer02 = setTimeout(function () {
                                clearInterval(timer); //游戏结束
                                timer02 = null;
                                layer.close($index);
                                // $('.lucky-lottery').removeClass('active-obj-box');

                                // 弹出中奖喜讯--完善中奖后的信息
                                var timer03 = setTimeout(function () {
                                    perfectionLottery(data.data.name, resultProperty, type, data.data.id, null, data.data.imgUrl)
                                    timer03 = null;
                                }, 500)

                                // 确保视图正确
                                var timer04 = setTimeout(function () {
                                    $lottos.eq(resultIdx).addClass('active').siblings().removeClass('active')
                                    timer04 = null;
                                }, start)

                                updatePoints($('.userPoint')); //更新我的积分
                                luckyDrawCost() //更新抽奖积分消耗
                            }, result)
                        } else {
                            layer.open({
                                title: ['温馨提示'],
                                skin: 'top-class',
                                content: data.data
                            })
                            layer.close($index);
                            $('.lucky-lottery').removeClass('active-obj-box');
                        }
                    })
                }

            })

            //抽奖积分消耗
            luckyDrawCost()

        }
    })

    // 商品列表
    $.getJSON(getAllPointsPresents, function (data) {
        if (data.code == CODE) {
            var goodList = data.data
            goodList.sort(function (a, b) {
                return a.order - b.order
            })
            // 模板渲染
            $('#goods-list-tpl').tmpl(goodList).appendTo('#goods-list');

            $('.lazy').lazyload(); //懒载

            // 点击商品
            $('#goods-list li').click(function () {
                var index = $(this).index();
                var id = goodList[index].id;
                window.open('./detail.jsp?id=' + id)
            });


            // 类型切换
            var $parent = $('#goods-list');
            $parent.append('<div class="empty">暂无符合条件</div>')
            var $physical = $parent.find('li[data-type="physical"]');
            var $other = $parent.find('li[data-type!="physical"]');

            $physical.hide();
            $('.goods-nav div').click(function () {
                $('.goods-tab span').eq(0).addClass('active').siblings().removeClass('active')
                $(this).addClass('active').siblings().removeClass('active')
                var type = $(this).attr('data-type')
                physicalTab(type)
            });

            // 价格分类
            // 非实物类的奖品价格
            var $lottery1WFalse = $parent.find('li[data-1w="false"][data-type!="physical"]');
            var $lottery5WFalse = $parent.find('li[data-5w="false"][data-type!="physical"]');
            var $lottery10WFalse = $parent.find('li[data-10w="false"][data-type!="physical"]');
            var $lottery1WSuccess = $parent.find('li[data-1w="true"][data-type!="physical"]');
            var $lottery5WSuccess = $parent.find('li[data-5w="true"][data-type!="physical"]');
            var $lottery10WSuccess = $parent.find('li[data-10w="true"][data-type!="physical"]');
            // 实物类的奖品价格
            var $lottery1WFalsePhy = $parent.find('li[data-1w="false"][data-type="physical"]');
            var $lottery5WFalsePhy = $parent.find('li[data-5w="false"][data-type="physical"]');
            var $lottery10WFalsePhy = $parent.find('li[data-10w="false"][data-type="physical"]');
            var $lottery1WSuccessPhy = $parent.find('li[data-1w="true"][data-type="physical"]');
            var $lottery5WSuccessPhy = $parent.find('li[data-5w="true"][data-type="physical"]');
            var $lottery10WSuccessPhy = $parent.find('li[data-10w="true"][data-type="physical"]');

            $('.goods-tab span').click(function () {
                $(this).addClass('active').siblings().removeClass('active')
                var price = $(this).attr('data-price')
                var type = $('.goods-nav div.active').attr('data-type');
                if (type === 'physical') {
                    if (price === '1w') {
                        $lottery1WFalsePhy.hide()
                        $lottery1WSuccessPhy.show()
                    } else if (price === '5w') {
                        $lottery5WFalsePhy.hide()
                        $lottery5WSuccessPhy.show()
                    } else if (price === '10w') {
                        $lottery10WFalsePhy.hide()
                        $lottery10WSuccessPhy.show()
                    } else {
                        physicalTab(type)
                    }
                } else {
                    if (price === '1w') {
                        $lottery1WFalse.hide()
                        $lottery1WSuccess.show()
                    } else if (price === '5w') {
                        $lottery5WFalse.hide()
                        $lottery5WSuccess.show()
                    } else if (price === '10w') {
                        $lottery10WFalse.hide()
                        $lottery10WSuccess.show()
                    } else {
                        physicalTab(type)
                    }
                }
                isEmpty()
            })
            function physicalTab(type) {// 实物类型切换
                if (type === 'physical') {
                    $physical.show();
                    $other.hide();
                } else {
                    $physical.hide();
                    $other.show();
                }
            }

            function isEmpty() {
                var res = [];
                $parent.find('li').each(function () {
                    if (!$(this).is(':hidden')) {//如果为显示
                        res.push($(this))
                    }
                })
                if (!res.length) {//全部隐藏
                    $('.empty').show()
                } else {
                    $('.empty').hide()
                }
            }


        } else {
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class',
                content: data.desc
            })
        }
    })

    // 我的积分
    updatePoints($('.userPoint'));

    // 抽奖积分消耗
    function luckyDrawCost() {
        if (!$('#checkUserIsLoad').val()) return
        $.get('/points/luckyDrawCost.aspx', function (data) {
            if (data.code === CODE) {
                var oldExpendPoints = parseInt(Number(data.data.luckyDrawCost)); //原消耗积分
                expendPoints = oldExpendPoints
                //expendPoints = oldExpendPoints * data.data.vipSave// 折扣积分消耗
                $('.point-expend').text(parseInt(expendPoints) + '分')
            }
        }, 'json')
    }

    // 中奖喜讯弹框
    function perfectionLottery(name, resultProperty, type, id, obj, imageUrl) {
        //中奖喜讯模板
        if (type == 'phoneFee' || type == 'phoneData') {
            var goodNewsTmpl = '<div class="goodNews-wrapper">\
                                    <h3>恭喜您，抽中了：</h3>\
                                    <p><span class="red">' + name + '</span></p>\
                                    <div>\
                                        手机号：<input class="phone" type="text" maxlength="11">\
                                        <div class="error-text"></div>\
                                    </div>\
                                </div>'
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class integral',
                content: goodNewsTmpl,
                btn: '领取',
                yes: function () {
                    var phone = $('.integral .phone').val();
                    var $error = $('.integral .phone').next('.error-text')
                    var reg = /^1[3-9][0-9]{9}$/;
                    if (!phone) {
                        $error.show().html('手机号不能为空');
                        return false;
                    } else if (!reg.test(phone)) {
                        $error.show().html('手机号格式有误');
                        return false;
                    }
                    var reqData = {
                        id: id,
                        phone: phone
                    }
                    var $index = layer.load()
                    $.post('/points/completeLuckyDrawRecord.aspx', {json: JSON.stringify(reqData)}, function (data) {
                        layer.close($index);
                        if (data.code == CODE) {
                            layer.closeAll()
                            // layer.msg('领取成功，谢谢参与');
                            layer.open({
                                title: '温馨提示',
                                skin: 'top-class integral',
                                content: '<span class="red">' + name + '</span>' + ' 领取成功，请稍后在手机上查收'
                            })
                            if (obj) {
                                obj.removeClass('madeChange').text('已领取')
                            }
                        } else {
                            layer.open({
                                title: ['温馨提示'],
                                skin: 'top-class',
                                content: data.data
                            })
                        }
                    }, 'json')

                }
            })

        } else if (type == 'physical') {
            var goodNewsTmpl = '<div class="goodNews-wrapper">\
                                    <h3>恭喜您，抽中了：</h3>\
                                    <p class="red">' + name + ' ' + resultProperty + '</p>\
                                </div>'
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class integral',
                btn: '领取',
                content: goodNewsTmpl,
                yes: function (index, layero) {
                    setLocalStorage('__luckyLotteryData__', {
                        id: id,
                        property: resultProperty,
                        name: name,
                        imageUrl: imageUrl
                    })
                    layer.closeAll()
                    window.open('./lucky-addr.jsp?id=' + id)

                }
            })
        } else if (type == 'coupon') {
            var goodNewsTmpl = '<div class="goodNews-wrapper">\
                                    <h3>恭喜您，抽中了：</h3>\
                                    <p><span class="red">' + resultProperty + '</span> 请查看站内信</p>\
                                </div>'
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class integral',
                btn: ['继续抽奖', '去查看'],
                content: goodNewsTmpl,
                btn2: function () {
                    window.open('/asp/payPage.aspx?#tab_letter')

                }

            })

        } else if (type == 'money') {
            var goodNewsTmpl = '<div class="goodNews-wrapper">\
                                    <h3>恭喜您，抽中了：</h3>\
                                    <p><span class="red">' + name + '</span> 请刷新余额查看</p>\
                                </div>'
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class integral',
                btn: '继续抽奖',
                content: goodNewsTmpl,

            })
        } else {
            var goodNewsTmpl = '<div class="goodNews-wrapper">\
                                    <h3>很遗憾，大奖与您擦肩而过</h3>\
                                    <div class="property-tip">大奖在后面，继续抽抽抽，我要抽到大奖出现！</div>\
                                </div>'
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class integral',
                btn: '再抽一次',
                area: ['400px'],
                content: goodNewsTmpl,

            })
        }
    }

    // 领取奖品
    $(document).on('click', '.madeChange', function () {
        var name = $(this).attr('data-name')
        var type = $(this).attr('data-type')
        var id = $(this).attr('data-id')
        var resultProperty = $(this).attr('data-property')
        var imageUrl = $(this).attr('data-imageUrl')
        perfectionLottery(name, resultProperty, type, id, $(this), imageUrl)
    })

})

