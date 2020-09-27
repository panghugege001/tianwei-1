/**
 * Created by Randy on 2017/12/7.
 */


//地区组件初始化
var area2 = new LArea();
area2.init({
    'trigger': '#area',
    'valueTo': '#area-value',
    'keys': {
        id: 'value',
        name: 'text'
    },
    'type': 2,
    'data': [provs_data, citys_data, dists_data]
});

$('#area').click(function(){
    //关闭输入法
    document.activeElement.blur();
})

var $header = $('.header-title');
var $delete = $('.delete-tit')

// ==== 保存地址

// 1.创建 2.删除 3.修改 4.查

var pageId = getQueryString('id');
var __addrNumber__ = getLocalStorage('__addrNumber__');//地址数量
var state, __currAddrData__;
if (pageId) {//改
    state = 3;
    __currAddrData__ = getLocalStorage('__currAddrData__')
    $header.html('编辑地址')
    if(__addrNumber__ == 1){
        $delete.hide()
    }
} else {
    state = 1;
    $delete.hide()
    $header.html('新增地址')
}

var $name = $('#name');
var $phone = $('#phone');
var $area = $('#area');
var $address = $('#address');
var $flag = $('#flag');

if (__currAddrData__) {
    $name.val(__currAddrData__.name)
    $phone.val(__currAddrData__.phone)
    $area.val(__currAddrData__.area)
    $address.val(__currAddrData__.address)
    if (__currAddrData__.flag) {
        $flag.attr('checked', 'true')
    }
}

$('.save-addr-btn').click(function () {

    var name = $name.val(),
        phone = $phone.val(),
        area = $area.val(),
        address = $address.val(),
        flag = $flag.is(':checked') ? 1 : 0;


    if (!name) {
        layer.msg('请填写姓名!')
        return
    }
    var reg = /^1[3-9][0-9]{9}$/;
    if (!reg.test(phone)) {
        layer.msg('手机格式有误!')
        return
    }
    if (!area) {
        layer.msg('请选择所在地区!')
        return
    }
    if (!address) {
        layer.msg('请填写详细地址!')
        return
    }
    var regAddr = /^.{3,50}$/;
    if (!regAddr.test(address)) {
        $address.siblings('.error-text').show()
        return
    }
    var areaArr = area.split(',')
    var province = areaArr[0],
        city = areaArr[1],
        district = areaArr[2];

    var reqData = {
        province: province,
        city: city,
        district: district,
        area: area.replace(/,/g,''),
        address: address,
        flag: flag,
        name: name,
        phone: phone,
        state: state
    }
    if (__currAddrData__) {
        reqData.id = __currAddrData__.id
    }
    var index = layer.load()
    $.post('/points/handleAddress.aspx', reqData, function (data) {
        layer.close(index);
        if (data.code === CODE) {
            self.location=document.referrer

        } else {
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class',
                content: data.data
            })
        }

    }, 'json')

})

// 删除地址
$delete.click(function (e) {
    e.stopPropagation()
    layer.open({
        title: '温馨提示',
        skin: 'top-class integral',
        content: '您将永久删除该地址信息，您确定了？',
        btn: ['知道了', '后悔了'],
        yes: function () {
            var reqData = {
                id: pageId,
                state: 2
            }
            var $index = layer.load()
            $.post('/points/handleAddress.aspx', reqData, function (data) {
                layer.close($index);
                if (data.code === CODE) {
                    self.location=document.referrer
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