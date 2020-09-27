/**
 * Created by Randy on 2017/11/29.
 */
var CODE = "0000" //响应成功状态码
var myTotalPonits = 0; // 我的积分
var expendPoints = 0; // 抽奖积分消耗
var levelNum = null;//用户等级

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
$(document).on('change', '.integral select', function () {
    $(this).siblings('.error-text').hide();
});
 
// 判断是否登录
function isLogin() { 
    var loginname = $('#checkUserIsLoad').val()
    if (!loginname) {
        $('#loginBtn').trigger('click'); 
        return false;
    }
    return true
}
function modalLogin (ele,frommodal){
        var loginname=$('#j-name-modal').val(),
            password=$('#j-pwd-modal').val(),
            $target=$(ele),
            $form=$target.closest("form.ui-form");
        if($form.length>0){
            loginname =$form.find('input[id="j-name-modal"]').val();
            password=$form.find('input[id="j-pwd-modal"]').val();
            console.log($form.find('input[id="j-name-modal"]').val())
        }
        if(loginname==""||loginname=="帐 号"){
            alert("账号不能为空！");
            return false;
        }
        if(password==""||password=="密 码"){
            alert("密码不能为空！");
            return false;
        }
        /*if(code==""||code=="验证码"){
            alert("验证码不能为空！");
            return false;
        }*/
        $target.prop('disabled',true);
        
         $.ajax({
             
             url: "/asp/generateVerificationCode.aspx?r=" + Math.random(),
            type: "post", // 请求方式
            async: false,
            success: function (response) {
                
                $.post("/asp/login.aspx", {
                "loginname":loginname, "password":password/*,"imageCode":code*/},
            function (returnedData) {
                $target.prop('disabled',false);
                if(returnedData=="SUCCESS"){
                    if(frommodal == 'modal'){
                        window.location.reload();
                        return false;
                    }
                    if(loginname.substr(0, 2) == "a_"){
                        window.location.href="/agentManage.aspx";
                    }else{
                        window.location.reload();
                    }
                }else{
                    $('#j-codeimg').attr('src','/asp/validateCodeForIndex.aspx?r='+Math.random());
                    alert(returnedData);
                    var str2='已被锁';
                    if(returnedData.indexOf(str2)>-1){
                        $('#modal-forget').modal('show');
                    }
                }
            }).fail(function(){
                $target.prop('disabled',false);
                alert('系统繁忙!');
            });
                
                
            }
         })
        
        
        

        return false;
    };

// 我的积分
function updatePoints($dom) {
    if (!isLogin()) return
    $.post("/points/queryPoints.aspx",
        function (data) {
            if (data.code === CODE) {
                myTotalPonits = parseInt(Number(data.data));
                $dom.html(myTotalPonits);
                var times = parseInt(myTotalPonits/8000);
                $('.have-times').html(times);
            }
        });
}

// 兑换成功弹框
function openSuccessTip(name, action, type, phoneLayer, isDetailPage) {
    if (action == '兑换') {
        var btn = '<a class="btnClass" href="javascript:;" onclick="showRecord(2)">查看兑换记录</a>'
    } else {
        var btn = '<a class="btnClass" href="javascript:;" onclick="showRecord(1)">查看抽奖记录</a>'
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
        var btn = '<a class="btnClass" href="/asp/payPage.aspx?#tab_letter" target="_blank">去站内信查看</a>'
    } else {
        var tips = '，发货时会以站内信通知，将于一周内发货，请注意查收。'
    }
    var userLv = $('#getLv').attr('val');
    var html = '<div class="suc-wrapper">\
                            <i class="sp-level sp-lv-'+userLv+'"></i>\
                            <p class="phy-tip">您成功' + action + '了<span class="red">' + name + '</span>' + tips + '</p>\
                            <div class="btn-group">\
                                <a class="btnClass goToIntegral" href="./integral.jsp">再去逛逛商城</a>\
                                ' + btn + '\
                            </div>\
                            <a href="/promotion.jsp?action=point" target="_blank">\
                            </a>\
                         </div>';
    layer.open({
        title: ['温馨提示'],
        skin: 'top-class integral',
        type: 1,
        area: ['800px', ''],
        offset: ['200px', ''],
        content: html,
        cancel: function () {
            if (!isDetailPage) {// 详情页不跳转
                window.location.href = './integral.jsp'
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
        var title = '积分明细'
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
                    console.log(data)
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

//判断是否达到积分抽奖条件
function canPlayLotto() {
    return expendPoints > myTotalPonits
}

// 字符串处理
function formatStr(str, startStrNumber, endStrNumber, starNumber) {
    for (var i = 0, star = ''; i < starNumber; i++) {
        star += '*'
    }
    var reg = new RegExp('^(\\w{' + startStrNumber + '})\\w+(\\w{' + endStrNumber + '})$')
    return str.replace(reg, ' $1 ' + star + ' $2 ')
}

// 方法扩展
Array.prototype.find = function (compare) {
    for (i = 0; i < this.length; i++) {
        if (compare(this[i])) {
            return this[i]
        }
    }
}