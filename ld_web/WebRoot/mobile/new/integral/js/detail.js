$(function () {
    var pageId = getQueryString('id')
    if (!pageId) {
        var url_query = '';
        if (window.localStorage.__platform__ === 'webapp') {
            url_query = '?openMobile'
        }
        window.location.href = './integral.jsp' + url_query

    }
    // 获取数据
    $.get('/points/getAllPointsPresents.aspx', {}, function (reqData) {
        if (reqData.code === CODE) {
            var goodList = reqData.data;
            var data = goodList.find(function (item) {
                return item.id == pageId
            })
            data.property = data.property || '[{}]'
            data.property = JSON.parse(data.property)

            // 模板渲染
            $('#detail-tpl').tmpl(data).appendTo('.detail-wrapper');

            // 等级图
            $('.level-image').attr('src', '../images/user/level/' + (levelNum) + '.png');

            // 会员优惠
            $('.promotion').click(function () {
                var index = layer.load()
                $.getJSON('/points/getLevelSave.aspx', function (data) {
                    layer.close(index);
                    if (data.code === CODE) {
                        var html = ''
                        for (var i = 0; i < data.data.length; i++) {
                            var item = data.data[i];
                            var value = item.value / 10 === 10 ? '无折扣' : item.value / 10 + '折'
                            html += '<div class="promotion-list"><span class="level">' + item.note + '</span><span>' + value + '</span></div>'
                        }
                        layer.open({
                            title: '会员优惠',
                            skin: 'top-class integral',
                            content: html
                        })
                    } else {
                        layer.open({
                            title: '温馨提示',
                            skin: 'top-class integral',
                            content: '<div class="center">' + data.data + '</div>'
                        })
                    }

                })

            })

            // 我的积分
            updatePoints($('.user-points'));

            // 选择类型切换
            var $spans = $('.properties span')
            var $currPoint = $('.curr-point')
            var $oldPoint = $('.old-point');
            $spans.eq(0).addClass('active');
            $spans.click(function () {
                $(this).addClass('active').siblings().removeClass('active')
                var point = $(this).attr('data-point')
                var oldPoint = $(this).attr('data-oldPoint');
                $currPoint.text(point + '分')
                $oldPoint.text(oldPoint + '分')

            })
            // 兑换
            $('.exchange').click(function () {
                var point = parseFloat($('.properties span.active').attr('data-point'));
                if (!isLogin()) return;
                console.log(point)
                console.log(myTotalPonits)
                if (point > myTotalPonits){//积分不够
                    console.log(point > myTotalPonits)
                    var html = '<div class="goodNews-wrapper">\
                                    <div class="property-tip">积分不够啦，快去存款赚积分吧</div>\
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
                    return
                }
                var id = data.id;
                var type = data.type
                var property = $('.properties span.active').attr('data-property')
                var oldPoint = $('.properties span.active').attr('data-oldPoint') || 0
                var name = data.name
                var promotion = oldPoint - point;
                var imageUrl = data.imageUrl

                var addrData = {
                    imageUrl: imageUrl,
                    promotion: promotion,
                    property: property,
                    name: name,
                    point: point,
                    id: id,
                    vipSave: data.vipSave,
                    type: type,
                    oldPoint: oldPoint
                }

                setLocalStorage('addr-data', addrData) //设置地址所需数据

                if (type === 'physical') {//实物
                    window.open('./exchange.jsp?id=' + id, '_self')
                } else if (type === 'phoneData' || type === 'phoneFee') {//手机
                    var html = '<div>\
                                <p>请输入您将要充值的的手机号</p>\
                                手机号：<input maxlength="11" class="phone" type="text" value=""/>\
                                <div class="error-text">手机格式有误</div>\
                            </div>'
                    layer.open({
                        title: ['温馨提示'],
                        skin: 'top-class integral',
                        type: 0,
                        btn: ['确定', '取消'],
                        content: html,
                        yes: function (phoneLayer) {
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
                                type: type,
                                property: property,
                                phone: phone
                            }
                            var index = layer.load()
                            $.post('/points/pointsExchange.aspx', {json: JSON.stringify(reqData)}, function (data) {
                                if (data.code === CODE) {
                                    updatePoints($('.user-points'));//刷新积分
                                    openSuccessTip(name, '兑换', type, phoneLayer, true)
                                } else {
                                    layer.open({
                                        title: ['温馨提示'],
                                        skin: 'top-class',
                                        content: data.data
                                    })
                                }
                                layer.close(index);
                            })
                        }
                    })

                } else {//彩金
                    var reqData = {
                        id: id,
                        type: type,
                        property: property
                    }
                    var index = layer.load()
                    $.post('/points/pointsExchange.aspx', {json: JSON.stringify(reqData)}, function (data) {
                        if (data.code == CODE) {
                            updatePoints($('.user-points'));//刷新积分
                            openSuccessTip(name, '兑换', type, false, true)
                        } else {
                            layer.open({
                                title: ['温馨提示'],
                                skin: 'top-class',
                                content: data.data
                            })
                        }
                        layer.close(index);
                    })
                }

            })
        }
    }, 'json')
})