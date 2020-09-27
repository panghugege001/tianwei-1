$(function () {
    // 返回处理
    var mobileorApp = window.location.href;
    var $back = $('#j-common-header a');
    if (mobileorApp.indexOf('openMobile') === -1) {//app
        window.localStorage.__platform__ = 'app';
        $back.attr('href', 'javascript:goBackApp();')
    } else {//webapp
        window.localStorage.__platform__ = 'webapp';
    }

    // 等级图
    $('.level-image').attr('src', '/images/youhui/le-' + (levelNum) + '.png');

    // icon
    var $icon = $('.p-welfare a');
    if (!levelNum) {
        $icon.find('div').css('opacity', '.5')
    }
    if (levelNum < 2) {
        $icon.eq(0).find('div').css('opacity', '.5')
    }
    if (levelNum < 1) {
        $icon.eq(1).find('div').css('opacity', '.5')
    }
    $icon.click(function () {
        $(this).find('.tip').slideToggle(150)
    })

    // 会员中奖信息
    $.getJSON('/points/pointsRecord.aspx', {flag: 3}, function (data) {
        var $dataBox = $('.p-marquee ul');
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
                    html += '<li class="g-item ellipsis">恭喜会员 ' + formatStr(item.loginName, 2, 2, 4) + ' 抽中 <span class="red">' + item.name + '</span> ' + property + '</li>'
                }
                $dataBox.html(html);

                // 滚动
                jQuery(".list").slide({
                    mainCell: ".bd ul",
                    effect: "top",
                    interTime: 2000,
                    autoPlay: true,
                    autoPage: true
                });
            } else {
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
                window.open('./detail.jsp?id=' + id, '_self')
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
                isEmpty();
                $('.p-search').trigger('click');
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

    //筛选
    var $sub = $('.nav-tab .sub');
    $('.p-search').click(function () {
        $sub.slideToggle(100)
    })

    // 我的积分
    updatePoints($('.user-point'));

});

