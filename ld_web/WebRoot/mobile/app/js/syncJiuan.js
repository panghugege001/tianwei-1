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

/****
 * 久安账户通*
 * * ****/
//绑定久安账号
$('#deposit-bindingJuan').click(function () {
    $('#deposit-bindingJuan-page').show().siblings().hide();

});
//同步久安账号
$('#deposit-syncJuan').click(function () {
    $('#deposit-syncJuan-page').show().siblings().hide();
});


/**绑定久安钱包**/

//绑定久安钱包->输入久安账号
function accountJuan() {
    var inputJuan = $('#inputJuan').val();
    var status = 'account';
    if (inputJuan != '') {
        //钱包绑定条款
        if ($('#bindingJuan-checked').prop('checked')) {
            $.post(JAbindAccountUrl, {
                "loginId": inputJuan
            }, function (returnedData) {
                if (returnedData.code == "10000") {
                    runProgressJuan();
                } else {
                    var errorMsg = returnedData.msg;
                    runerrorJuan(status, errorMsg)
                }
            });
        } else {
            alert('请同意久安钱包绑定条款！');
        }
    } else {
        alert('请输入您的久安账号或手机号或邮箱！');
    }

    // alert("功能维护中!");
}

//[绑定&同步]数据同步中->数据同步成功！
function runProgressJuan(status, userName, password) {
    layer.open({
        title: false,
        area: ['500px', '216px'],
        closeBtn: 0,
        content: '<p id="downloadJuanTitle">数据同步中，请耐心等待........</p>'
        + '<div id="j-juanList">'
        + '<div class="juan-line"></div>'
        + '<div class="juan-active-line"></div>'
        + '</div>'
        + '<div></div>',
        btn: ['确定'],
        yes: function () {
            layer.closeAll();
            //返回久安页面
            $('#deposit-jiuan-page').show().siblings().hide();
            if (status == 'syncJuan') {
                syncJuanTips(userName, password);
            }
        }
    });
    $('.layui-m-layerbtn').css('display', 'none');
    setTimeout(function () {
        getLevel();
    }, 500);
    setTimeout(function () {
        $('#downloadJuanTitle').html('数据同步成功！');
        $('.layui-m-layerbtn').css('display', 'block');
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
        content: '<p id="downloadJuanTitle" class="c-red">绑定失败！<span></span></p>'
        + '<div id="j-juanList">'
        + '<div class="juan-error-line"></div>'
        // + '<div class="juan-error-active-line"><img src="/images/deposit/juanActive2.png" style="width: 85%;height: 20px;" ></div>'
        + '</div>'
        + '<div></div>',
        btn: [btn1],
        yes: function () {
            layer.closeAll();
            if (status == 'syncJuan') {
                syncJuanTipsError();
            }
            else {
                //返回绑定btn 直接关闭弹窗
            }
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
    //钱包绑定条款
    if ($('#syncJuan-checked').prop('checked')) {
        $.post(JASyncAccount, function (returnedData) {
            if (returnedData.code == "10000") {
                var userName = returnedData.data.userName;
                var password = returnedData.data.password;
                runProgressJuan(status, userName, password)
            } else {
                var errorMsg = returnedData.msg;
                runerrorJuan(status, errorMsg)
            }
        });
    } else {
        alert('请同意久安钱包绑定条款！');
    }

    // alert("功能维护中!");
}

//[同步]数据同步失败
function syncJuanTipsError() {
    layer.open({
        title: false,
        area: ['500px', '305px'],
        closeBtn: 0,
        content: '<p  class="c-red ft18">数据同步失败！</p>'
        + '<br>'
        + '<p>您的天威账号 <span class="c-red customerJuan"></span> 已经被同步久安钱包，不可重复同步！！</p>'
        + '<br>'
        + '<br>'
        + '<div class="c-gray">'
        + '<p>若该同步非您本人操作，请联系久安客服找回！</p>'
        + '<p>久安客服：开启久安APP-我的-在线客服询问找回。</p>'
        + '</div>',
        btn: ['我知道了'],
        yes: function () {
            layer.closeAll();
            //返回久安页面
            $('#deposit-jiuan-page').show().siblings().hide();
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
        content: '<p class="ft18" >数据同步成功！</p>'
        + '<br>'
        + '<p>您的久安账号为“天威前缀+您的天威账号”：</p>'
        + '<p class="c-red"><span class="customerJuan"></span></p>'
        + '<br>'
        + '<br>'
        + '<div class="c-gray">'
        + '<p>您的久安密码为：</p>'
        + '<p class="c-red"><span class="passwordJuan"></span></p>'
        + '</div>',
        btn: ['我知道了'] //按钮
    }, function () {
        layer.closeAll();
        //返回久安页面
        $('#deposit-jiuan-page').show().siblings().hide();
    });
    $('.customerJuan').html(userName);
    $('.passwordJuan').html(password);
}


function backJuan() {
    //返回久安页面
    $('#deposit-jiuan-page').show().siblings().hide();
}