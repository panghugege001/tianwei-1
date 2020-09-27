/**
 * Created by Randy on 2017/11/30.
 */
$(function () {
    var pageId = getQueryString('id')
    if (!pageId) {
        var url_query = '';
        if (window.localStorage.__platform__ === 'webapp') {
            url_query = '?openMobile'
        }
        window.location.href = './integral.jsp' + url_query
    }

    var isAddressPage = window.location.href.indexOf('exchange.jsp') !== -1;
    var exchangeApi; //兑换接口
    var addrData; // 商品数据
    var action;//动作
    var addrList = [];//地址列表

    // 页面判断
    if (isAddressPage) {
        exchangeApi = '/points/pointsExchange.aspx' //兑换
        addrData = getLocalStorage('addr-data')
        action = '兑换'
    } else {
        exchangeApi = '/points/completeLuckyDrawRecord.aspx' //领取
        addrData = getLocalStorage('__luckyLotteryData__')
        action = '领取'
    }

    // 商品信息模板渲染
    $('#confirm-exchange-tpl').tmpl(addrData).appendTo('.confirm-exchange');

    // 获取地址
    $.getJSON('/points/handleAddress.aspx', {state: 4}, function (data) {
        if (data.code === CODE) {
            addrList = data.data;
            if (!data.data.length) {
                var html = '<p>您还没有设置收货地址，请点击添加</p><img width="240" src="img/addr.jpg" />'
                layer.open({
                    title: '温馨提示',
                    skin: 'top-class integral',
                    btn: ['添加', '取消'],
                    content: html,
                    offset: ['100px'],
                    yes: function () {
                        window.location.href = './new-addr.jsp'
                    },
                    btn2: function () {
                        history.back()
                    },
                    cancel: function () {
                        history.back()
                    }
                })
                return
            }

            // 地址数量
            setLocalStorage('__addrNumber__', data.data.length);

            // 地址模板渲染
            $('#point-addr-tpl').tmpl(data.data).appendTo('.point-addr');

            // 至少展示一条地址
            if (!$('.addr-item.active').length) {
                $('.addr-item').eq(0).addClass('active')
            }


            // 地址列表模板渲染
            $('#point-addr-list-tpl').tmpl(data.data).appendTo('.addr-list-padding ul');


            //点击地址
            var $addrList = $('.addr-list-wrapper');
            $('.addr-item').click(function () {
                $addrList.fadeIn(150)
            });

            //返回
            $('.close-addr-list').click(function () {
                $addrList.fadeOut(150)
            })

            // 选择地址
            var $items = $('.addr-list li');
            $items.click(function () {
                $(this).addClass('active').siblings().removeClass('active');
                var idx = $(this).index();
                $('.point-addr .addr-item').eq(idx).addClass('active').siblings().removeClass('active')
                $addrList.fadeOut(150)
            });
            // 至少有一条地址高亮
            if (!$('.addr-list li.active').length) {
                $items.eq(0).addClass('active')
            }

            //修改地址
            $('.edit').click(function (e) {
                e.stopPropagation();
                var id = $(this).attr('data-id')
                //当前点击地址
                var currAddrData = data.data.find(function (item) {
                    return item.id == id
                })
                setLocalStorage('__currAddrData__', currAddrData)
                window.location.href = './new-addr.jsp?id=' + id
            });

            //兑换
            $('.sure-exchange').click(function () {

                var $activeItem = $('.addr-item.active')
                var reIdx = $activeItem.index();
                var addr = addrList[reIdx] || {}
                var addressId = $activeItem.attr('data-id')
                if (!addressId) {
                    layer.msg('请先选择地址哦')
                    return false;
                }

                layer.open({
                    title: '请确认收货地址',
                    skin: 'integral top-class',
                    btn: ['确定', '取消'],
                    content: '<div class="con-addr-info">\
                        <p class="con-addr-tit">您领取了<span class="red">' + addrData.name + '</span>，将于一周内发货，收货信息无法修改，请确认如下：</p>\
                        <p><span>姓名：</span><span>' + addr.name + '</span></p>\
                        <p><span>手机：</span><span>' + addr.phone + '</span></p>\
                        <p><span>地址：</span><span>' + addr.area + addr.address + '</span></p>\
                     </div>',
                    yes: function () {
                        var phone = $activeItem.find('.exchange-phone').attr('data-phone');
                        var reqData = {
                            addressId: addressId,
                            json: JSON.stringify({
                                id: pageId,
                                phone: phone,
                                property: addrData.property
                            })
                        }
                        var index = layer.load()
                        $.post(exchangeApi, reqData, function (data) {
                            layer.closeAll();
                            if (data.code === CODE) {
                                openSuccessTip(addrData.name, action, addrData.type)
                            } else {
                                layer.open({
                                    title: ['温馨提示'],
                                    skin: 'top-class',
                                    content: '<div class="center">' + data.data + '</div>'
                                })
                            }
                        }, 'json')
                    }
                })


            })


        } else {
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class',
                content: data.data
            })
        }
    })
})
