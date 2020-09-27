/**
 * Created by Randy on 2017/12/7.
 */

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
        html += '<div class="lotto-btn">\
                        <h3>点击抽奖</h3>\
                        <p><span class="point-expend">请先登录</span></p>\
                    </div>'
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
                                    <div class="property-tip">积分不够啦，快去充值赚积分吧</div>\
                                </div>'
                layer.open({
                    title: ['温馨提示'],
                    skin: 'top-class integral',
                    btn: '存款赚积分',
                    content: html,
                    yes: function () {
                        window.open('/mobile/app/fundsManage.jsp?openMobile')
                    }

                })
            } else {
                var $index = layer.load()
                $.getJSON(luckyDraw, function (data) {

                    if (data.code === CODE) {
                        var start = 40; //间隔时间 (不能取值50,dom渲染机制)
                        var circleNum = 4; //圈数
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

                            updatePoints($('#userPoint')); //更新我的积分
                            luckyDrawCost() //更新抽奖积分消耗
                        }, result)
                    } else {
                        layer.open({
                            title: ['温馨提示'],
                            skin: 'top-class',
                            content: data.data
                        })
                        layer.close($index);
                    }
                })
            }

        })

        //抽奖积分消耗
        luckyDrawCost()

    }
})

// 我的积分
updatePoints($('.user-point'));

// 抽奖积分消耗
function luckyDrawCost() {
    if (!$('#checkUserIsLoad').val()) return
    $.get('/points/luckyDrawCost.aspx', function (data) {
        if (data.code === CODE) {
            var oldExpendPoints = parseInt(Number(data.data.luckyDrawCost)); //原消耗积分
            //expendPoints = oldExpendPoints * data.data.vipSave// 折扣积分消耗
            expendPoints = oldExpendPoints
            $('.point-expend').text(parseInt(expendPoints) + '分')
        }
    }, 'json')
}



// 我的抽奖记录
$('.lottery-record').click(function(){
    if(isLogin()){
        window.location.href = './record.jsp?flag=1'
    }
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
                html += '<li class="g-item">恭喜会员 ' + formatStr(item.loginName, 1, 1, 6) + ' 抽中 ' + item.name + ' ' + property + '</li>'
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
            skin: 'top-class integral',
            content: data.data
        })
    }
})
