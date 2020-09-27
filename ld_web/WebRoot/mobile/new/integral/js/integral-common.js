/**
 * Created by Randy on 2017/11/29.
 */
/******test*****/
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
/******end-test*****/

const CODE = '0000'
var myTotalPonits = 0; // 我的积分
var expendPoints = 0; // 抽奖积分消耗

// 取数据
function getLocalStorage(key) {
    var res = window.localStorage.getItem(key) || '[]'
    return JSON.parse(res)
}
// 存数据
function setLocalStorage(key, value) {
    window.localStorage.setItem(key, JSON.stringify(value))
}
// 获取url查询参数值
function getQueryString(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

// 验证
$(document).on('input', '.integral input,.integral textarea', function () {
    if ($(this).val()) {
        $(this).next('.error-text').hide();
    }
});

// 判断是否登录
function isLogin() {
    var loginname = $('#checkUserIsLoad').val()
    if (!loginname) {
        var timer = setTimeout(function(){
            layer.open({
                title: '溫馨提示',
                skin: 'top-class integral login',
                content: '<p class="center">请先登录游戏</p>',
                yes: function (idx) {
                    // var mobileorApp = window.location.href;
                    // if (mobileorApp.indexOf('openMobile') === -1) {
                    //     goBackApp()
                    // }else{
                    //     window.location.href = '/mobile/login.jsp'
                    // }
                    // window.location.href = '/mobile/login.jsp'
/*                    if (window.localStorage.__platform__ === 'webapp') {
                        window.location.href = '/mobile/login.jsp'
                    } else {
                        goBackApp()
                    }*/
                    window.location.href = '/mobile/new/login.jsp'
                    layer.close(idx);
                }
            })
            timer = null
        })
        return false;
    }
    return true
}

//判断是否达到积分抽奖条件
function canPlayLotto() {
    return expendPoints > myTotalPonits
}

// 我的积分
function updatePoints($dom) {
    if (!isLogin()) return
    $.post("/asp/queryPoints.aspx",
        function (returnedData, status) {
            if ("success" == status) {
                var strs=returnedData.split("#");
                myTotalPonits = strs[0];
                $dom.html(myTotalPonits);
            }
        });
}

// 兑换成功弹框
function openSuccessTip(name, action, type, phoneLayer, isDetailPage) {
    if (action == '兑换') {
        var btn = '<a class="btnClass" href="./record.jsp?flag=2">查看兑换记录</a>'
    } else {
        var btn = '<a class="btnClass" href="./record.jsp?flag=1">查看抽奖记录</a>'
    }
    if (phoneLayer) {//如果有输入手机号弹框，关闭之
        layer.close(phoneLayer)
    }
    if (type === 'money') {
        var tips = '，请刷新余额查看'
    } else if (type === 'phoneFee' || type === 'phoneData') {
        var tips = '，请稍后在手机上查收'
    } else if (type === 'coupon') {
        var tips = '，请查看站内信。'
        var btn = '<a class="btnClass" href="/mobile/userManage/email.jsp">去站内信查看</a>'
    } else {
        var tips = '，发货时会以站内信通知，将于一周内发货，请注意查收。'
    }

    var url_query = '';
    if (window.localStorage.__platform__ === 'webapp') {
        url_query = '?openMobile'
    }

    var html = '<div class="suc-wrapper success">\
                            <div class="s-tit">\
                                <i class="iconfont icon-duigou11"></i>\
                            </div>\
                            <p class="phy-tip">您成功' + action + '了<span class="red">' + name + '</span>' + tips + '</p>\
                            <div class="btn-group">\
                                <a class="btnClass goToIntegral" href="./integral.jsp' + url_query + '">再去逛逛商城</a>\
                                ' + btn + '\
                            </div>\
                         </div>';
    layer.open({
        title: ['温馨提示'],
        skin: 'top-class integral',
        type: 1,
        content: html,
        cancel: function () {
            if (!isDetailPage) {// 详情页不跳转
                window.location.href = './integral.jsp' + url_query
            }
        }
    })
}

// 属性值过滤
function filterAttrVal(type, remark) {
    var resultProperty;
    if (type == 'coupon') {
        var res = remark.split('-');
        resultProperty = res[0].toLocaleUpperCase() + ' ' + res[1] + '%' + ' 存送优惠券'
    } else if (type == 'phoneData') {
        resultProperty = remark + 'M'
    } else if (type == 'phoneFee' || type == 'money') {
        resultProperty = remark + '元'
    } else {
        resultProperty = remark;
    }
    return resultProperty;
}

//记录展示弹框
function showRecord(flag) {
    if (!isLogin()) return
    var html = '<section>\
                        <div class="data-container"></div>\
                        <div id="pagination-lottery-record"></div>\
                    </section>';
    if (flag == 1) {
        var title = '我的抽奖记录'
    } else {
        var title = '积分兑换记录'
    }
    layer.open({
        title: [title],
        skin: 'top-class integral',
        area: ['600px', ''],
        type: 1,
        offset: ['200px'],
        content: html,
        success: function () {
            $.getJSON('/points/pointsRecord.aspx', {flag: flag}, function (data) {
                if (data.code === CODE) {
                    var container = $('#pagination-lottery-record')
                    if (data.data.length) {
                        container.pagination({
                            dataSource: data.data,
                            className: 'paginationjs-theme-red',
                            callback: function (response, pagination) {
                                var dataHtml = '<table>\
                                                <thead>\
                                                <tr>\
                                                <th>时间</th>\
                                                <th>积分</th>\
                                                <th>备注</th>\
                                                </tr>\
                                                </thead>\
                                                <tbody>'
                                $.each(response, function (index, item) {
                                    var property = '';
                                    var status = item.status;
                                    if (status == '已抽奖') {
                                        status = '<span class="madeChange changeStatus" data-imageUrl="' + item.imgUrl + '" data-name="' + item.name + '" data-type="' + item.luckyDrawPresentType + '" data-property="' + item.property + '" data-id="' + item.id + '">领取</span>'
                                    } else {
                                        status === '已完成' && (status = '已领取')
                                        status = '<span class="changeStatus">' + status + '</span>'
                                    }
                                    if (flag == 1) {//抽奖记录
                                        property = filterAttrVal(item.luckyDrawPresentType, item.property)
                                        if (item.luckyDrawPresentType == 'money' || item.luckyDrawPresentType == 'phoneFee' || item.luckyDrawPresentType == 'phoneData') {
                                            property = ''
                                        }
                                    } else {
                                        property = filterAttrVal(item.type, item.property)
                                    }
                                    dataHtml += '<tr>\
                                                <td>' + item.createTime + '</td>\
                                                <td class="red">' + parseInt(item.points) + '</td>\
                                                <td>' + item.name + ' ' + property + status + '</td>\
                                             </tr>';
                                });

                                dataHtml += '</tbody></table>';

                                container.prev().html(dataHtml);
                            }
                        })
                    } else {
                        container.prev().html('<div class="no-data">暂无记录</div>')
                    }
                } else {
                    layer.open({
                        title: ['温馨提示'],
                        skin: 'top-class',
                        content: data.data
                    })
                }
            })
        }
    })
}

// 字符串处理
function formatStr(str, startStrNumber, endStrNumber, starNumber) {
    for (var i = 0, star = ''; i < starNumber; i++) {
        star += '*'
    }
    var reg = new RegExp('^(\\w{' + startStrNumber + '})\\w+(\\w{' + endStrNumber + '})$')
    return str.replace(reg, ' $1 ' + star + ' $2 ')
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
                window.open('./lucky-addr.jsp?id=' + id, '_self')

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
                window.open('/mobile/userManage/email.jsp', '_self')

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
            content: goodNewsTmpl,

        })
    }
}

// 方法扩展
Array.prototype.find = function (compare) {
    for (i = 0; i < this.length; i++) {
        if (compare(this[i])) {
            return this[i]
        }
    }
}

// 返回首页
var $goTopBtn = $('.go-top');
$goTopBtn.click(goTop)
function goTop() {
    $('html,body').animate({'scrollTop': 0}, 150); //滚回顶部的时间，越小滚的速度越快~
}

var $header = $('.common-header.integral-index');

window.onscroll = function () {
    var t = document.documentElement.scrollTop || document.body.scrollTop;  //获取距离页面顶部的距离
    if (t === 0) {
        $goTopBtn.fadeOut(150)
    } else {
        $goTopBtn.fadeIn(150)
    }
    // if (t > 45) {
    //     $header.css('background', '#191a1e')
    // } else {
    //     $header.css('background', 'none')
    // }
}

// 跳app
function goBackApp() {
    if (getMobileKind() == 'Android') {
        window.location.href = 'lehuwebapp://Home'
    } else {
        window.location.href = 'lehuwebapp://Home'
    }
}
// 判断设备
function getMobileKind() {
    if (navigator.userAgent.match(/Android/i))
        return 'Android';
    if (navigator.userAgent.match(/iPhone/i) ||
        navigator.userAgent.match(/iPad/i) ||
        navigator.userAgent.match(/iPod/i))
        return 'IOS';
    if (navigator.userAgent.match(/Windows Phone/i))
        return 'Windows Phone';
    return 'other';
}
//获取会员等级
getLevel = function () {
        var level = parseInt($('#j-level').val()),
            $userLevel = $('#userLevel'),
            $levelIcon = $('#levelIcon'),
            levelArr = ['新会员','忠实VIP','星级VIP','金牌VIP','白金VIP','钻石VIP','至尊VIP'];
            $levelIcon.attr('src','../images/user/level/' + level + '.png').animate({opacity: 1},1000, function() {
            });;
            $userLevel.text(levelArr[level]);

    };
getLevel();