var _TEST_MODE = 0;

var JAbindAccountUrl = "";
var JASyncAccount = "";

if (_TEST_MODE == 1) {
    JAbindAccountUrl = "/data/jiuan/JAbindAccount.json";
    JASyncAccount = "/data/jiuan/JASyncAccount.json";
} else {
    JAbindAccountUrl = "/asp/JAbindAccount.aspx";
    JASyncAccount = "/asp/JASyncAccount.aspx";
}


/**绑定久安钱包**/

//钱包绑定条款
function bindingJuan() {
    layer.confirm(
        ['将天威账户绑定久安钱包：',
            '您是否同意久安钱包获取您在天威上的相关非敏感类信息？',
            '天威承诺：<span class="c-red">久安钱包无权从天威获取您的联系方式、余额、身份等敏感类信息。</span>'].join('</br>'),
        {
            title: ['钱包绑定条款'],
            btn: ['同意并绑定', '取消'] //按钮
        }, function () {
            layer.closeAll();
            //绑定久安钱包
            accountJuan();
        });
}

//绑定久安钱包->输入久安账号
function accountJuan() {
    layer.open({
        title: '绑定久安钱包',
        area: ['500px', '242px'],
        closeBtn: 0,
        content: '<div>'
        + '<p>久安账号：</p>'
        + '<br>'
        + '<input type="text" id="inputJuan" placeholder="请输入您的久安账号或手机号或邮箱" style="width: 460px;height: 40px;border: 1px solid #eeeeee">'
        + '</div>',
        btn: ['确认', '取消'],
        btn1: function () {
            layer.closeAll();
            var inputJuan = $('#inputJuan').val();
            var status = 'account';//绑定
            if (inputJuan == '') {
                runerrorJuan('请输入您的久安账号或手机号或邮箱');
            } else {
                $.post(JAbindAccountUrl, {
                    "loginId": inputJuan
                }, function (returnedData) {
                    if (returnedData.code == "10000") {
                        //数据同步成功
                        runProgressJuan();
                    } else {
                        //数据同步失败
                        var errorMsg = returnedData.msg;
                        runerrorJuan(status, errorMsg);
                    }
                });
            }

        },
        btn2: function (index) {
            layer.close(index);
        }
    });

    // alert("功能维护中!");
}

//[绑定&同步]数据同步中->数据同步成功！
function runProgressJuan(status, userName, password) {
    layer.open({
        title: false,
        area: ['500px', '216px'],
        closeBtn: 0,
        content:
        '<p id="downloadJuanTitle" style="margin-top: 40px">数据同步中，请耐心等待........</p>'
        + '<div id="j-juanList">'
        + '<div class="juan-line"></div>'
        + '<div class="juan-active-line"></div>'
        + '</div>'
        + '<div></div>',
        btn: ['确定'],
        btn1: function () {
            layer.closeAll();
            if (status == 'syncJuan') {
                syncJuanTips(userName, password);
            }
        }
    });
    $('.layui-layer-btn').css('display', 'none');
    setTimeout(function () {
        getLevel();
    }, 500);
    setTimeout(function () {
        $('#downloadJuanTitle').html('数据同步成功！');
        $('.layui-layer-btn').css('display', 'block');
    }, 3500);
}

//[绑定]数据同步中->数据同步失败！
function runerrorJuan(status, errorMsg) {
    //同步btn
    if (status == 'syncJuan') {
        var btn1 = '确定';
    }
    //绑定btn
    else {
        var btn1 = '返回重新绑定';
    }

    layer.open({
        title: false,
        area: ['500px', '216px'],
        closeBtn: 0,
        content:
        '<p id="downloadJuanTitle" class="c-red" style="margin-top: 40px">绑定失败！<span></span></p>'
        + '<div id="j-juanList">'
        + '<div class="juan-error-line"></div>'
        + '<div class="juan-error-active-line"><img src="/images/deposit/juanActive2.png"></div>'
        + '</div>'
        + '<div></div>',
        btn: [btn1, '取消'],
        btn1: function () {
            layer.closeAll();
            if (status == 'syncJuan') {
                //同步btn
                syncJuanTipsError();
            }
            else {
                //绑定btn
                accountJuan();
            }
        },
        btn2: function (index) {
            layer.close(index);
        }
    });
    $('#downloadJuanTitle span').html(errorMsg);
}

function getLevel() {
    var $juanList = $('#j-juanList'),
        item = $juanList.find('.juan-item'),
        line = $juanList.find('.juan-active-line');
    line.animate({'width': '100%'}, 3000, function () {
        item.addClass('current active');
        //$('#juan-num').html(item);
        item.prevAll('.juan-item').addClass('active');
    });
}

/**同步久安钱包**/
function syncJuan() {
    var status = 'syncJuan';
    layer.confirm(
        ['用天威账户同步久安钱包：',
            '<br>',
            '同步后您可以用天威录久安钱包。您是否同意久安钱包获取您在天威上的相关<span class="c-red">非敏感类信息。</span>',
            '<br>',
            '天威承诺：久安钱包<span class="c-red">无权从天威获取您的联系方式、余额、身份等敏感类信息。</span>'].join('</br>'),
        {
            title: ['钱包账户同步条款'],
            area: ['500px', '360px'],
            btn: ['同意并绑定', '取消'] //按钮
        }, function () {
            layer.closeAll();
            $.post(JASyncAccount, function (returnedData) {

                if (returnedData.code == "10000") {
                    //数据同步成功
                    var userName = returnedData.data.userName;
                    var password = returnedData.data.password;

                    runProgressJuan(status, userName, password);
                } else {
                    //数据同步失败
                    var errorMsg = returnedData.msg;
                    runerrorJuan(status, errorMsg)
                }
            });
        });

    // alert("功能维护中!");
}

//[同步]数据同步失败
function syncJuanTipsError() {
    layer.open({
        title: false,
        area: ['500px', '305px'],
        closeBtn: 0,
        content:
        '<p  class="c-red ft18">数据同步失败！</p>'
        + '<br>'
        + '<p>您的天威账号 <span class="c-red customerJuan"></span> 已经被同步久安钱包，不可重复同步！！</p>'
        + '<br>'
        + '<br>'
        + '<div class="c-gray">'
        + '<p>若该同步非您本人操作，请联系久安客服找回！</p>'
        + '<p>久安客服：开启久安APP-我的-在线客服询问找回。</p>'
        + '</div>',
        btn: ['我知道了'],
        btn1: function () {
            layer.closeAll();
        }
    });

    var customerJuan = $('#j-loginName').val();
    $('.customerJuan').html(customerJuan);
}

//[同步]数据同步成功
function syncJuanTips(userName, password) {
    layer.open({
        title: ['温馨提示'],
        area: ['500px', '298px'],
        closeBtn: 0,
        content:
        '<p class="ft18" >数据同步成功！</p>'
        + '<br>'
        + '<p>您的久安账号为“天威前缀+您的天威账号”：</p>'
        + '<p class="c-red"><span class="customerJuan"></span></p>'
        + '<br>'
        + '<div class="c-gray">'
        + '<p> 您的久安密码为：</p>'
        + '<p class="c-red"><span class="passwordJuan"></span></p>'
        + '</div>',
        btn: ['我知道了'] //按钮
    }, function () {
        layer.closeAll();
    });

    $('.customerJuan').html(userName);
    $('.passwordJuan').html(password);

}