/**
 * Created by Randy on 2017/11/30.
 */
$(function () {
    var pageId = getQueryString('id')
    if (!pageId) {
        window.location.href = './integral.jsp'
    }

    var isAddressPage = window.location.href.indexOf('address.jsp') !== -1;
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
            // 地址模板渲染 
            $('#point-addr-tpl').tmpl(data).appendTo('.point-addr');

            // 新增地址 
            $('.add-addr').click(function () {
                changeAddr('创建地址', 1)
            });

            //选择地址
            var $items = $('.addr-list .item');
            $items.click(function () {
                $(this).addClass('active').siblings().removeClass('active');
            });

            //修改地址
            $('.change-addr').click(function (e) {
                e.stopPropagation()
                var id = $(this).parents('.item').attr('data-id')
                var options = data.data.find(function (item) {
                    return item.id == id
                })
                changeAddr('修改地址', 3, options)
            })

            //删除地址
            $('.delete').click(function (e) {
                e.stopPropagation()

                var _this = $(this)
                layer.open({
                    title: '温馨提示',
                    skin: 'top-class integral',
                    content: '您将永久删除该地址信息，您确定了？',
                    btn: ['知道了', '后悔了'],
                    yes: function () {
                        var id = _this.parents('.item').attr('data-id')
                        var reqData = {
                            id: id,
                            state: 2
                        }
                        var $index = layer.load()
                        $.post('/points/handleAddress.aspx', reqData, function (data) {
                            layer.close($index);
                            if (data.code === CODE) {
                                window.location.reload()
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

            })
        } else {
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class',
                content: data.data
            })
        }
    })

    //创建-修改地址
    function changeAddr(title, state, options) {
        var html = '<div>\
                                  <div class="input-group">\
                                    <label for="" class="label rq-value">所在地区</label>\
                                    <select id="j-select-province" class="ipt-txt" required>\
                                    </select>\
                                    <select id="j-select-city" class="ipt-txt" required>\
                                    </select>\
                                    <select id="j-select-district" class="ipt-txt">\
                                    </select>\
                                    <div class="error-text">请完善地区</div>\
                                  </div>\
                                  <div class="input-group">\
                                    <label for="address">详细地址</label><textarea minlength="3" maxlength="50" class="text" id="address"></textarea>\
                                    <div class="error-text">5-100个字符，一个汉字为两个字符</div>\
                                  </div>\
                                  <div class="input-group">\
                                    <label for="name">收款人姓名</label><input class="text" id="addName" type="text" value="" />\
                                    <div class="error-text">收款人不能为空</div>\
                                  </div>\
                                  <div class="input-group">\
                                    <label for="phone">手机号</label><input maxlength="11" class="text" id="addPhone" type="text" value="" />\
                                    <div class="error-text">手机号格式有误</div>\
                                  </div>\
                                  <div class="input-group">\
                                    <label >&nbsp;</label><input class="checkbox" id="flag" type="checkbox" value="" />设置为默认收货地址\
                                  </div>\
                                  <div class="input-group">\
                                    <label for="j-road">&nbsp;</label><input class="btnClass sava-addr" id="j-road" type="button" value="保存" />\
                                  </div>\
                             </div>'
        layer.open({
            skin: 'top-class integral address',
            title: [title],
            area: ['600px', ''],
            type: 1,
            content: html,
            success: function () {
                var $province = $('#j-select-province'),
                    $city = $('#j-select-city'),
                    $district = $('#j-select-district'),
                    $address = $('#address'),
                    $name = $('#addName'),
                    $phone = $('#addPhone'),
                    $flag = $('#flag');

                // 初始化组件
                addressComponent($province, $city, $district)

                if (options) { //修改
                    $province.find('option[value=' + options.province + ']').attr('selected', 'selected')
                    $city.append('<option selected>' + options.city + '</option>')
                    $district.append('<option selected>' + options.district + '</option>')
                    $address.val(options.address)
                    $name.val(options.name)
                    $phone.val(options.phone)
                    if (options.flag) {
                        $flag.attr('checked', 'true')
                    }
                }

                $('.sava-addr').click(function () {

                    var area = getAddr($province, $city, $district)
                    var province = $province.val();
                    var city = $city.val();
                    var district = $district.val();
                    var address = $address.val();
                    var name = $name.val()
                    var phone = $phone.val()
                    var flag = $flag.attr('checked') ? 1 : 0

                    if (!district) {
                        $district.siblings('.error-text').show()
                        return
                    }
                    var regAddr = /^.{3,50}$/;
                    if (!regAddr.test(address)) {
                        $address.siblings('.error-text').show()
                        return
                    }
                    if (!name) {
                        $name.siblings('.error-text').show()
                        return
                    }
                    var reg = /^1[3-9][0-9]{9}$/;
                    if (!reg.test(phone)) {
                        $phone.siblings('.error-text').show()
                        return
                    }

                    var reqData = {
                        province: province,
                        city: city,
                        district: district,
                        area: area,
                        address: address,
                        flag: flag,
                        name: name,
                        phone: phone,
                        state: state
                    }
                    if (options) {
                        reqData.id = options.id
                    }
                    var index = layer.load()
                    $.post('/points/handleAddress.aspx', reqData, function (data) {
                        layer.close(index);
                        if (data.code === CODE) {
                            window.location.reload()
                        } else {
                            layer.open({
                                title: ['温馨提示'],
                                skin: 'top-class',
                                content: data.data
                            })
                        }

                    }, 'json')

                })

            }
        })
    }

    // 兑换
    $('.sure-exchange').click(function () {

        var $activeItem = $('.addr-list .item.active')
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
                            content: '<div class="center">' + data.desc + '</div>'
                        })
                    }
                }, 'json')

            }
        })
    })

})