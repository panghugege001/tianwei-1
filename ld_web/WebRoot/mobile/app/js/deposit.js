var _TEST_MODE = 0;
var _TEST_JUAN_MODE = 0;
var _SUBMIT_FLAG = false;
var _FORM_DATA = "";
var _ERROR_MSG = '请重新登录!';

var v = $('#j-level').val();
if (_TEST_MODE == 0) {
    var _payWayUrl = '/asp/pay_way.aspx';
} else {
    var _payWayUrl = '/data/deposit/payWay99.json';
}


$(function () {

    localStorage.removeItem('userinfo');

    var loginty = $('#j-loginName').val();

    //支付开关
    if (loginty == '') {
        var currentUrl = window.location.href;
        window.location.href = window.location.origin + "/mobile/new/login.jsp?callback=" + currentUrl;
        return false;
    } else {
        DepositPage();
    }
});


//各种支付
function DepositPage() {

    var _$view = $('.content');
    var _deposit = {};
    var _dep = {};
    var _payUrl = '/asp/pay_api.aspx';
    var _formHtml = '<form action="{0}" method="post" target="_blank">{1}</form>';
    var _inputHtml = '<input type="hidden" name="{0}" value="{1}"/>';

    var _$titles = $('.pay-tabctrl .subtitle-btn a');
    var _$subTitles = $('.pay-content .subtitle-btn a');

    _$titles.bind('click', _titleClickEvent);
    _$subTitles.bind('click', _subTitleClickEvent);

    // 默认通道
    $("#alipay-title").trigger("click");

    function _titleClickEvent() {

        var $that = $(this);
        $that.addClass('active').siblings().removeClass('active');

        if (!_deposit[this.id]) {

            switch (this.id) {
                case 'jiuan-title':
                    $(".jiuan-page").show().siblings().hide();
                    break;
                case 'bank-title':
                    $(".bank-page").show().siblings().hide();
                    break;
                case 'alipay-title':
                    $(".alipay-page").show().siblings().hide();
                    break;
                case 'wechat-title':
                    $(".wechat-page").show().siblings().hide();
                    break;
                case 'qq-title':
                    $(".qq-page").show().siblings().hide();
                    break;
                case 'other-title':
                    $(".other-page").show().siblings().hide();
                    break;
            }
        }

        setTimeout(function () {
            $(".pay-content").find(".subtitle-btn a:visible").eq(0).trigger("click");
        }, 200);

        resetPaymc();
    }

    function _subTitleClickEvent() {

        var $that = $(this);
        var $target = $that.data("target");

        if (!_dep[this.id]) {
            switch (this.id) {
                // 微信
                case 'wexinQR-subtitle':
                    _dep[this.id] = new _WeixinManage();
                    break;
                case 'wexinSpeed-subtitle':
                    _dep[this.id] = new _WeixinSpeedManage();
                    break;
                // 支付宝
                case 'zfbQR-subtitle':
                    _dep[this.id] = new _ZFBManage();
                    break;
                // 银行支付
                case 'speedpay-subtitle':
                    _dep[this.id] = new _SpeedPayManage();
                    break;
                case 'yinlian-subtitle':
                    _dep[this.id] = new _YinlianManage();
                    break;
                case 'thirdpay-subtitle':
                    _dep[this.id] = new _ThirdPayManage();
                    break;
                // 久安支付
                case 'jiuan-subtitle':
                    _dep[this.id] = new _JIUANManage();
                    break;
                // qq支付
                case 'qq-subtitle':
                    _dep[this.id] = new _QQManage();
                    break;
                // 其他支付
                case 'jd-subtitle':
                    _dep[this.id] = new _JDManage();
                    break;
                case 'dcard-subtitle':
                    _dep[this.id] = new _DCardManage();
                    break;
                default:
            }
        }


        $that.addClass('active').siblings().removeClass('active');
        $("#" + $target).show().siblings('.tabdiv').hide();

        resetPaymc();
    }

    function _buildHtml(o) {

        var payWayHtml = "";

        var payWayTpl = '<span class="payway-show j-payway-show" data-url="{{payCenterUrl}}" ' +
            'data-id="{{id}}"  ' +
            'data-max={{maxPay}} ' +
            'data-min="{{minPay}}" ' +
            'data-fee="{{fee}}" ' +
            'data-platform="{{platform}}" ' +
            'value="{{showName}}" ><span class="text {{style}}">{{showName}}</span><dd>{{hot}}</dd></span>';

        if (!o) {

            payWayHtml = payWayTpl.replace(/\{\{payCenterUrl\}\}/g, "")
                .replace(/\{\{id\}\}/g, "")
                .replace(/\{\{maxPay\}\}/g, "")
                .replace(/\{\{minPay\}\}/g, "")
                .replace(/\{\{fee\}\}/g, "")
                .replace(/\{\{hot\}\}/g, "")
                .replace(/\{\{style\}\}/g, "left0")
                .replace(/\{\{showName\}\}/g, "维护中");

        } else {
            $.each(o, function (i, vo) {
                var o = vo;


                if (o.payPlatform == "tyjfzfb") {

                    payWayHtml += payWayTpl.replace(/\{\{payCenterUrl\}\}/g, o.payCenterUrl)
                        .replace(/\{\{id\}\}/g, o.id)
                        .replace(/\{\{maxPay\}\}/g, o.maxPay)
                        .replace(/\{\{minPay\}\}/g, o.minPay)
                        .replace(/\{\{fee\}\}/g, o.fee)
                        .replace(/\{\{style\}\}/g, "")
                        .replace(/\{\{hot\}\}/g, "推荐")
                        .replace(/\{\{platform\}\}/g, o.payPlatform)
                        .replace(/\{\{showName\}\}/g, o.showName);
                    return payWayHtml;

                } else {

                    payWayHtml += payWayTpl.replace(/\{\{payCenterUrl\}\}/g, o.payCenterUrl)
                        .replace(/\{\{id\}\}/g, o.id)
                        .replace(/\{\{maxPay\}\}/g, o.maxPay)
                        .replace(/\{\{minPay\}\}/g, o.minPay)
                        .replace(/\{\{fee\}\}/g, o.fee)
                        .replace(/\{\{style\}\}/g, "")
                        .replace(/\{\{hot\}\}/g, "")
                        .replace(/\{\{platform\}\}/g, o.payPlatform)
                        .replace(/\{\{showName\}\}/g, o.showName);
                    return payWayHtml;

                }
            });
        }

        return payWayHtml;
    }


    _init();

    /**
     * 初始化
     */
    function _init() {

        var jsonData = ajaxPost(_payWayUrl, {"usetype": "1"});

        if (jsonData.desc == '成功') {

            /**
             * 放入deposit
             */
            for (var i in jsonData.data) {

                var o = jsonData.data[i];

                if ($.isArray(_deposit[o.payWay])) {
                    _deposit[o.payWay].push(o);
                } else {
                    _deposit[o.payWay] = new Array();
                    _deposit[o.payWay].push(o);
                }

                /**
                 * 开启通道
                 */
                if (jsonData.data[i].payWay == 1) {
                    //支付宝
                    $("#zfbQR-subtitle").removeClass('stop');
                }
                if (jsonData.data[i].payWay == 2) {
                    //微信
                    $("#wechat-title").removeClass('stop');
                    $('#wexinQR-subtitle').removeClass('stop');
                }

                if (jsonData.data[i].payWay == 3) {
                    //第三方支付
                    $('#bank-title').removeClass('stop');
                    $('#thirdpay-subtitle').removeClass('stop');
                }
                if (jsonData.data[i].payWay == 4) {
                    //快捷支付
                    $('#speedpay-subtitle').removeClass('stop');
                }
                if (jsonData.data[i].payWay == 5) {
                    //点卡支付
                    $("#other-title").removeClass('stop');
                    $('#dcard-subtitle').removeClass('stop');
                    $('#dcard-subtitle').attr('data-url', jsonData.data[i].payCenterUrl);
                    $('#dcard-subtitle').attr('data-id', jsonData.data[i].id);
                    $('#dcard-subtitle').attr('data-type', true);
                }

                if (jsonData.data[i].payWay == 7) {
                    //QQ支付
                    $("#qq-title").removeClass('stop');
                    $('#qq-subtitle').removeClass('stop');
                }

                if (jsonData.data[i].payWay == 10) {
                    //京东支付
                    $("#other-title").removeClass('stop');
                    $('#jd-subtitle').removeClass('stop');
                }
                if (jsonData.data[i].payWay == 12) {
                    //久安支付
                    $("#jiuan-title").removeClass('stop');
                    $('#jiuan-subtitle').removeClass('stop');
                }
                if (jsonData.data[i].payWay == 13) {
                    //银联支付
                    $('#yinlian-subtitle').removeClass('stop');
                }
                if (jsonData.data[i].payWay == 15) {
                    //微信快捷
                    $("#wechat-title").removeClass('stop');
                    $('#wexinSpeed-subtitle').removeClass('stop');
                }
            }
        }
        else {
            _showLayer('系统繁忙，请稍后再试');
        }


        _hideLayer();
        _fastPayManage();
        _cloudPayManage();

    }


    function _initPayWay(target, payway) {
        var $parent = $(target);
        var payWayHtml = _buildHtml(_deposit[payway]);
        $parent.find(".payway-list").html(payWayHtml);
        $parent.find(".payway-show").eq(0).trigger("click");
    }

    function _getFormData() {
        return _FORM_DATA;
    }

    function _setFormData(data) {
        _FORM_DATA = data;
    }

    function _cloudPayManage() {

        var saveCard = "", saveMoney = "";
        var $parent = $("#cloud-page");

        var $saveCard = "", $saveMoney = "";
        var $saveSubmitBtn = $(".cloud-submit");
        var $saveRestartBtn = $(".cloud-restart");
        var $saveSuccessBtn = $(".cloud-success");

        var $quota = $(".quota");
        var $saveCheck = $("#j-cloudpay-checked");
        var $saveCheck1 = $("#j-cloudpay-checked1");
        var $agreeSubmitBtn = $("#j-cloudpay-agree");

        function _init() {

            $saveSubmitBtn.click(function () {
                _cloudPay();
            });

            $saveRestartBtn.click(function (e) {
                e.preventDefault();
                layer.closeAll();
                _resetCloudPay();
            });

            $saveSuccessBtn.click(function (e) {

                layer.closeAll();
                _resetCloudPay();
            });

        }

        function _chkInfo() {

            var msg = "";

            $saveCard = $parent.find(".cloud-card");
            $saveMoney = $parent.find(".money-input");

            saveCard = $saveCard.val().trim();
            saveMoney = $saveMoney.val().trim();

            if (saveCard == '' || saveCard == null || !saveCard) {
                msg = "请填写您的银行卡号";
                $parent.find('.error-tips').html(msg);
                return false;
            }

            if (saveCard.length != 4) {
                msg = "请输入银行卡号四位数";
                $parent.find('.error-tips').html(msg);
                return false;
            }

            if (saveMoney == '' || saveMoney == null || !saveMoney) {
                msg = "请输入存款金额";
                $parent.find('.error-tips').html(msg);
                return false;
            }
            if (saveMoney < 10 || saveMoney > 3000000) {
                msg = "请输入10元到300万的金额";
                $parent.find('.error-tips').html(msg);
                return false;
            }

            if (isNaN(saveMoney)) {
                msg = "存款金额不得包含汉字";
                $parent.find('.error-tips').html(msg);
                return false;
            }

            var reg = /^[1-9]\d*$/;
            if (!reg.test(saveMoney)) {
                msg = "请输入整数";
                $parent.find('.error-tips').html(msg);
                return false;
            }

            return true;
        }

        function _cloudPay() {

            var chkInfo = _chkInfo();

            if (!chkInfo) {
                return false;
            }

            $saveSubmitBtn.attr("disabled", true);

            $quota.html(saveMoney);

            var formData = {
                banktype: '5',
                uaccountname: "云闪付",
                ubankno: saveCard,
                amount: saveMoney
            };

            _deposit(formData);
        }


        function _deposit(formData) {

            document.documentElement.scrollTop = 0;
            document.body.scrollTop = 0;

            var newDepositUrl = "";

            if (_TEST_MODE == 1) {
                newDepositUrl = "/data/deposit/newDeposit1.json";
            } else {
                newDepositUrl = "/asp/getNewdeposit.aspx";
            }

            var jsonData = ajaxPost(newDepositUrl, formData);
            var massage = jsonData.massage;

            if (!massage) {
                _showDepositInfo(jsonData)
            } else {

                if (jsonData['force'] == true) {

                    layer.closeAll('loading');
                    //layer询问框
                    layer.open({
                        content: [
                            '您上一笔订单未支付，可点击作废订单，系统将生成新的存款信息'
                        ],
                        btn: ['作废上笔订单，生成新订单', '取消'],
                        yes: function () {

                            _hideLayer();

                            formData['force'] = true;

                            if (_TEST_MODE == 1) {
                                newDepositUrl = "/data/deposit/newDeposit2.json";
                            } else {
                                newDepositUrl = "/asp/getNewdeposit.aspx";
                            }

                            var jsonData = ajaxPost(newDepositUrl, formData);
                            if (!jsonData.massage) {
                                _showDepositInfo(jsonData);
                            } else {
                                _hideLayer();
                                _showLayer(jsonData.massage);
                                _resetCloudPay();
                                return false;
                            }
                        }

                    });
                } else {
                    _hideLayer();
                    _showLayer(jsonData.massage);
                    _resetCloudPay();
                    return false;
                }

            }

        }


        function _showDepositInfo(response) {

            var zfbImgCode = response.zfbImgCode;

            if (zfbImgCode != "") {

                var _account = $("#j-loginName").val();
                var _money = response["amount"];
                var _feedback = calcBonus(_money, "1.5%");
                var _amount = parseFloat(_money) + parseFloat(_feedback);

                // 配置玩家填寫存款信息
                $(".quick-confirm-account").html(_account);
                $(".quick-confirm-money").html(_money);
                $(".quick-confirm-feedback").html(_feedback);
                $(".quick-confirm-amount").html(_amount);

                $("#cImgCode").attr("src", zfbImgCode);
                $("#cloud-page").show().siblings().hide();
                $("#cloud-page .step2").show().siblings().hide();
            } else {
                _showLayer("二维码获取错误，请刷新再试！");
            }
        }


        // 重置秒存
        function _resetCloudPay() {
            $saveCard.val('');
            $saveMoney.val('');
            $quota.val('');
            $saveCheck.prop('checked', false);
            $saveCheck1.prop('checked', false);
            $saveSubmitBtn.attr('disabled', false);
            $("#cloud-page .step1").show().siblings().hide();
        }

        _init();
    }

    function _fastPayManage() {

        var $parent = "", saveName = "", saveMoney = "", saveType = "", saveBank = "", saveDepositId = "";
        var quickSaveData = "", renewData = "";
        var $mcSumbit = $(".quick-submit");

        $mcSumbit.click(function (e) {

            e.preventDefault();


            $parent = $(this).parentsUntil(".tabdiv");

            saveName = $parent.find(".quick-name").val();
            saveMoney = $parent.find(".quick-money").val().trim();

            saveType = $parent.find(".payway-list .j-payway-show.active").data("value");


            if (!saveType || saveType == "") {
                saveType = $(this).parentsUntil(".pay-content").find(".subtitle-btn a.active").data("value");
            }

            // 同略云
            if (saveType == 7) {
                var id = $(this).parents(".tabdiv").attr("id");
                if (id == "zfbmc-page") {
                    saveBank = "ZFB_ABC";
                } else if (id == "wechat-page") {
                    saveBank = "WX_CCB";
                } else if (id == "bank-page") {
                    saveBank = "BANK_ICBC";
                }
            }

            var msg = "";

            if (msg == "" && (saveName == '' || saveName == null || !saveName)) {
                msg = "请填写您的存款姓名";
            }
            if (msg == "" && !/^[\u4e00-\u9fa5]+$/.test(saveName)) {
                msg = "姓名只允许为汉字";
            }
            if (msg == "" && (saveMoney == '' || saveMoney == null || !saveMoney)) {
                msg = "请输入存款金额";
            }
            if (msg == "" && (saveMoney < 10 || saveMoney > 3000000)) {
                msg = "请输入10元到300万的金额";
            }
            if (msg == "" && isNaN(saveMoney)) {
                msg = "存款金额不得包含汉字";
            }

            if (msg != "") {
                showLayerTimeTips(msg);
                $parent.find('.error-tips').html(msg);
                return false;
            } else {

                $(".quick-submit").attr("disabled", true);

                var $quotaSubmitBtn = $parent.find(".j-fastpay-agree");
                var $saveCheck = $parent.find(".must-checked");
                var $saveCheck2 = $parent.find(".must-checked1");

                if (saveType == 4 || saveType == 6 || saveType == 7 || saveType == 22) {

                    var quotaUrl = "";

                    if (_TEST_MODE == "1") {
                        quotaUrl = "/data/deposit/getWxZzQuota.json";
                    } else {
                        quotaUrl = "/asp/getWxZzQuota.aspx";
                    }

                    var _layerTpl = '<p class="font2 my-input-title red">您需要存入金额：</p> <p class="font4 my-input-title red quota">{{quota}}</p> <div class="space-2"></div> <div class="gray-tips red font2">请您存入该金额，否则存款无法到账，本订单有效时间为2小时</div> <div class="space-2"></div> <div class="tips"><input type="checkbox" class="must-checked1" id="checked-w1"><labelfor="checked-w1">本人已同意，如未转账 <span class="quota red">{{quota}}</span> 元，导致系统无法匹配存款，天威概不负责！</label></div> <div class="space-2"></div>';
                    var quota = ajaxPost(quotaUrl, {'amount': saveMoney});

                    if (quota > 0) {

                        _layerTpl = _layerTpl.replace(/\{\{quota\}\}/g, quota);

                        layer.open({
                            content: _layerTpl,
                            title: "存款须知",
                            skin: "deposit-style text-left",
                            btn: ["下一步", "取消"],
                            yes: function () {

                                var $layer = $(".layui-m-layer-deposit-style");

                                if ($layer.find('.must-checked1').prop('checked')) {

                                    nextStep();
                                    $(".quota").html(quota);

                                } else {
                                    _showLayer('请同意天威存款条例！');
                                    resetPaymc();
                                    return false;
                                }

                            }
                        });

                    } else {
                        _showLayer(_ERROR_MSG);
                        $(".quick-submit").attr("disabled", false);
                        return false;
                    }


                } else {
                    nextStep();
                }
            }
        });


        function nextStep() {

            document.documentElement.scrollTop = 0;
            document.body.scrollTop = 0;

            var formData = {
                "banktype": saveType,
                "uaccountname": saveName,
                "amount": saveMoney
            };

            if (saveType == 7) {
                formData['ubankname'] = saveBank;
            }

            layer.open({type: 2, content: '处理中'});

            // 重新获取
            renewData = formData;

            var newDepositUrl = "";

            if (_TEST_MODE == 1) {
                newDepositUrl = "/data/deposit/newDeposit2.json";
            } else {
                newDepositUrl = "/asp/getNewdeposit.aspx";
            }

            var response = ajaxPost(newDepositUrl, formData);
            var massage = response.massage;

            layer.closeAll();

            if (!massage) {

                _hideLayer();
                _creatDepositInfo(response);
                _setDepositId(response.depositId);

            } else {
                if (response['force'] === true) {

                    _setDepositId(response.depositId);

                    layer.open({
                        content: [
                            '您上一笔订单未支付，可点击作废订单，系统将生成新的存款信息'
                        ],
                        btn: ['作废上笔订单，生成新订单', '取消'],
                        yes: function (index) {

                            layer.open({type: 2, content: '处理中'});

                            if (_TEST_MODE == 1) {
                                newDepositUrl = "/data/deposit/newDeposit1.json";
                            } else {
                                newDepositUrl = "/asp/getNewdeposit.aspx";
                            }

                            formData['force'] = true;

                            var response = ajaxPost(newDepositUrl, formData);

                            if (!response.massage) {

                                _hideLayer();
                                _creatDepositInfo(response);

                            } else {
                                layer.open({content: massage, btn: '关闭'});
                            }

                            layer.closeAll();
                        }
                    });
                }
                else {
                    layer.closeAll();
                    _showLayer(massage);
                    return false;
                }
            }
        }

        function _creatDepositInfo(jsonData) {

            $(".quick-submit").hide().attr("disabled");

            var massage = jsonData.massage;
            if (massage == null || massage == '' || massage == undefined) {

                $(".step1-title").hide();
                $(".j-subtitle").hide();
                $(".fast-how").hide();

                // 取得玩家填寫存款信息
                quickSaveData = {
                    "type": saveType,
                    "type_cn": "",
                    "username": saveName,
                    "money": saveMoney,
                    "depositCode": $("#quick-save-depositCode").val()
                };

                // 微信转帐 教程
                if (saveType == 4) {
                    $(".fast-how").show();
                } else if (saveType == 6) {
                    $("#yinhangImgCode").attr("src", jsonData["zfbImgCode"]);
                }

                if (saveType < 5) {
                    $(".quick-renew").show();
                } else {
                    $(".quick-renew").hide();
                }

                if (jsonData.area == "" || typeof jsonData.area == 'undefined') {
                    $("#fastpay-area").hide();
                } else {
                    if (jsonData.amount > 49999) {
                        $("#fastpay-area").show();
                    } else {
                        $("#fastpay-area").hide();
                    }
                }

                _setDepositInfo(jsonData);

            } else {
                $(".step2").hide();
                $(".step1").show();
                $(".step1-title").show();
                showLayerTips(massage);
            }
        }

        function _setDepositInfo(jsonData) {

            _setDepositId(jsonData.depositId);

            var _account = $("#j-loginName").val();
            var _money = jsonData["amount"];
            var _feedback = calcBonus(_money, "1.5%");
            var _amount = parseFloat(_money) + parseFloat(_feedback);

            // 配置玩家填寫存款信息
            $(".quick-confirm-type").html(quickSaveData["type_cn"]);
            $(".quick-confirm-account").html(_account);
            $(".quick-confirm-name").html(quickSaveData["username"]);
            $(".quick-confirm-money").html(_money);
            $(".quick-confirm-feedback").html(_feedback);
            $(".quick-confirm-amount").html(_amount);

            $(".qqbankname").val(jsonData.bankname);
            $(".qqaccountno").val(jsonData.accountno);
            $(".qqaccountname").val(jsonData.username);
            $(".qqarea").val(jsonData.area);
            $(".qqmefuyan").val(jsonData.depositId);

            $(".step2").show().siblings().hide();
        }

        function _setDepositId(id) {
            saveDepositId = id;
        }

        function _getDepositId() {
            return saveDepositId;
        }

        $(".quick-success").click(function () {
            resetPaymc();
        });

        $(".fast-how").click(function () {
            $(".wechat-page").hide();
            $(".full-page").hide();
            $("#wechat-desc-page").show();
        });

        $(".quick-renew").click(function (e) {
            e.preventDefault();
            layer.open({type: 2, content: '处理中'});

            var newDepositUrl = "";

            if (_TEST_MODE == 1) {
                newDepositUrl = "/data/deposit/newDeposit1.json";
            } else {
                newDepositUrl = "/asp/getNewdeposit.aspx";
            }

            layer.open({
                content: [
                    "此操作将作废当前订单，页面会获取新的银行卡，请重新存款。"
                ],
                btn: ["换卡", "取消"],
                yes: function () {

                    renewData['force'] = true;
                    renewData['depositId'] = _getDepositId();

                    var jsonData = ajaxPost(newDepositUrl, renewData);

                    _hideLayer();

                    if (jsonData.massage && jsonData.massage != "") {
                        _showLayer(jsonData.massage);
                        return false;
                    } else {
                        _setDepositInfo(jsonData);
                    }
                },
                no: function () {
                    _hideLayer();
                }
            });
        });
    }

    /***
     * 第三方支付支付
     */
    function _ThirdPayManage() {

        var $parent = $('#thirdpay-page');

        function _doCheck() {

            var _thirdPayFlag = 0;

            var $active = $parent.find('.payway-list .payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find(".money-input").val(),
                loginName: $('#j-loginName').val(),
                payUrl: $active.attr("data-url"),
                bankCode: $parent.find('#bank-list .payWayBtn.on').attr("data-value"),
                usetype: 1,
                fee: _fee
            };


            if (formData.bankCode == "" || typeof formData.bankCode == 'undefined') {
                $parent.find(".bank-tips").html('请选择银行').removeClass("hide");
                _thirdPayFlag = 1;
            } else {
                $parent.find(".bank-tips").addClass("hide");
            }

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
                $parent.find(".error-tips").text(msg).removeClass("hide");
                _thirdPayFlag = 1;
            } else {
                $parent.find(".error-tips").addClass("hide");
            }

            if (_thirdPayFlag == 0) {
                _setFormData(formData);
                _doConfirm();
            }
        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "在线支付",
                skin: "deposit-style",
                // closeBtn: true,
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            _payTo(formData);

        }


        function _init() {
            $("#bank-list, #third-list").html("");
            _initPayWay("#thirdpay-page", 3);
            _queryPayBank();
        }


        function _queryPayBank() {
            var payBankUrl = "";

            if (_TEST_MODE == 0) {
                payBankUrl = "/api/pay/thirdPaymentBank/list";
            } else {
                payBankUrl = "/data/deposit/payBank2.json";
            }

            var _dataUrl = $parent.find(".payway-show.active").attr("data-url");
            var _platformIdArr = _dataUrl.split("/");
            var _platformId = _platformIdArr[1] + _platformIdArr[2];

            if (_platformId == "" || typeof _platformId === 'undefined') {
                return false;
            }

            $.ajax({
                url: payBankUrl,
                type: "POST",
                data: JSON.stringify({payType: _platformId}), //dbonline_pay
                dataType: "JSON",
                contentType: "application/json",
                async: 'true',
                success: function (response) {
                    if (response.code == "10000") {
                        _buildBankHtml(response);
                    } else {
                        alert("请稍后重试");
                    }
                }
            });
        }


        var bankIconList = ajaxPost("/data/deposit/bankIconList.json", "");

        function _buildBankHtml(jsonData) {

            var dataList = jsonData.data;

            var bankHtml = "";
            var bankTpl = '<li class="payWayBtn num-btn"' + 'data-value="{{dictValue}}" value="{{dictName}}"><i class="bank-sprite {{icon}}"></i><span class="text">{{dictName}}</span></li>';

            $.each(dataList, function (i, vo) {
                var o = dataList[i];
                var icon = bankIconList[o.bankZHName];

                bankHtml += bankTpl.replace(/\{\{dictValue\}\}/g, o.bankCode)
                    .replace(/\{\{dictName\}\}/g, o.bankZHName)
                    .replace(/\{\{icon\}\}/g, icon);
            });

            $("#bank-list").html(bankHtml);
            $(".money-input").text("");

        }

        _init();
        $(document).on("click", "#thirdpay-confirm", _doCheck);
        $(document).on("click", "#thirdpay-page .j-payway-show", _queryPayBank);
    }

    /***
     * 微信扫码 _WeixinManage
     */
    function _WeixinManage() {

        var $parent = $("#wexinQR-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }

            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "微信扫码",
                skin: "deposit-style",
                // closeBtn: true,
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            var platform = $parent.find(".payway-show.active").data("platform");

            if (platform == "hxfwx") {

                if (formData.orderAmount % 100 != 0) {
                    _showLayer("此通道仅支持100倍数的金额!");
                } else {
                    _payTo(formData);
                }

            } else {
                _payTo(formData);
            }
        }


        _initPayWay("#wexinQR-page", 2);
        $(document).on("click", "#wexinQR-confirm", _doCheck);
    }

    /***
     * 微信快捷 _WeixinSpeedManage
     */
    function _WeixinSpeedManage() {

        var $parent = $("#wexinSpeed-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "微信快捷",
                // closeBtn: true,
                skin: "deposit-style",
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();

            var platform = $parent.find(".payway-show.active").data("platform");

            if (platform.indexOf('vpwx') > -1) {

                var vpmsg = _doMoneyCheck(platform, formData);

                if (vpmsg != "") {
                    _showLayer(vpmsg);
                } else {
                    _payTo(formData);
                }

            } else {
                _payTo(formData);
            }

        }


        _initPayWay("#wexinSpeed-page", 15);
        $(document).on("click", "#wexinSpeed-confirm", _doCheck);
    }

    /***
     * 支付宝扫码 _ZFBManage
     */
    function _ZFBManage() {

        var $parent = $("#zfbQR-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "支付宝扫码",
                // closeBtn: true,
                skin: "deposit-style",
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            var platform = $parent.find(".payway-show.active").data("platform");

            if (platform == "vpzfbsm" ) {

                var vpmsg = _doMoneyCheck(platform, formData);
                if (vpmsg != "") {
                    _showLayer(vpmsg);
                } else {
                    _payTo(formData);
                }

            } else {
                _payTo(formData);
            }
        }


        _initPayWay("#zfbQR-page", 1);
        $(document).on("click", "#zfbQR-confirm", _doCheck);
    }

    /***
     * 久安支付 _JIUANManage
     */
    function _JIUANManage() {

        var $parent = $('#jiuan-deposit-page');
        var $confirm = $("#jiuan-confirm-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                bank_code: "ZF_WX",
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }
        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _feedback = calcBonus(_money, "2%");
            var _amount = parseFloat(_money) + parseFloat(_feedback);

            $confirm.find(".d-account").text(_login);
            $confirm.find(".d-money").text(_money);
            $confirm.find(".d-feedback").text(_feedback);
            $confirm.find(".d-amount").text(_amount);

            $confirm.show().siblings().hide();
        }

        function _doPay() {

            if (_SUBMIT_FLAG == false) {
                _formJiuan();

                _SUBMIT_FLAG = true;
            } else {
                _showLayer("请勿重复提交订单!");
            }

            setTimeout(function () {
                _SUBMIT_FLAG = false;
            }, 5000);

            function _formJiuan() {

                var formData = _getFormData();
                var _JiuanUrl = '', html = '';

                $('#payToJiuan').remove();


                layer.open({type: 2});

                $.ajax({
                    "url": "/asp/getInitInterfaceData.aspx",
                    "dataType": "json",
                    "data": formData,
                    "async": false,
                    success: function (returnedData, status) {

                        layer.closeAll();

                        if (status == 'success') {

                            var data = returnedData.data;

                            if (returnedData.code == '10000') {

                                if (_TEST_JUAN_MODE == 0) {
                                    _JiuanUrl = 'https://www.9security.com/cash?';
                                } else {
                                    _JiuanUrl = 'http://47.52.202.233/cash?';
                                }

                                var junaData = {
                                    "amount": data.amount,
                                    "assetCode": data.assetCode,
                                    "bindAreacode": data.bindAreacode,
                                    "bindName": data.bindName,
                                    "bindPhone": data.bindPhone,
                                    "bindUserLevel": data.bindUserLevel,
                                    "bindUserid": data.bindUserid,
                                    "merchantCallbackurl": data.merchantCallbackurl,
                                    "merchantId": data.merchantId,
                                    "merchantOrderid": data.merchantOrderid,
                                    "nodeId": data.nodeId,
                                    "sign": data.sign,
                                    "spareFields": data.spareFields,
                                    "notifyUrl": "https://" + window.location.host + "/mobile/app/fundsManage.jsp?openMobile"
                                };


                                var i = 0;
                                for (var key in junaData) {
                                    _JiuanUrl += (i == 0) ? "" : "&";
                                    _JiuanUrl += key + "=" + encodeURIComponent(junaData[key]);
                                    i++;
                                }

                                $('body').append('<a href="' + _JiuanUrl + '" target="_blank" id="payToJiuan"></a>');
                                $("#payToJiuan")[0].click();

                            } else {
                                alert(data.message)
                            }
                        }
                    },
                    fail: function () {
                        layer.closeAll();
                    }
                });
            }
        }

        _initPayWay("#jiuan-deposit-page", 12);
        $(document).on("click", "#jiuan-confirm", _doCheck);
        $(document).on("click", "#jiuan-submit", _doPay);
    }

    /***
     * QQ钱包 _QQManage
     */
    function _QQManage() {

        var $parent = $("#qq-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "QQ钱包",
                // closeBtn: true,
                skin: "deposit-style",
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            _payTo(formData);

        }


        _initPayWay("#qq-page", 7);
        $(document).on("click", "#qq-confirm", _doCheck);
    }

    /***
     * 京东支付 _JDManage
     */
    function _JDManage() {

        var $parent = $("#jd-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "京东支付",
                skin: "deposit-style",
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            _payTo(formData);

        }


        _initPayWay("#jd-page", 10);
        $(document).on("click", "#jd-confirm", _doCheck);
    }

    /***
     * 银联支付 _YinlianManage
     */
    function _YinlianManage() {

        var $parent = $("#yinlian-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "银联支付",
                // closeBtn: true,
                skin: "deposit-style",
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            _payTo(formData);

        }


        _initPayWay("#yinlian-page", 13);
        $(document).on("click", "#yinlian-confirm", _doCheck);
    }

    /***
     * 快捷支付 _SpeedPayManage
     */
    function _SpeedPayManage() {

        var $parent = $("#speedpay-page");

        function _doCheck() {

            var $active = $parent.find('.payway-show.active');
            var _max = $active.data('max');
            var _min = $active.data('min');
            var _fee = $active.data('fee');

            var formData = {
                platformId: $active.attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#j-loginName").val(),
                payUrl: $active.attr("data-url"),
                usetype: 1,
                fee: _fee
            };

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {

                var _maxStr = "";
                if (_max > 9999) {
                    _maxStr = (_max / 10000) + "万";
                } else {
                    _maxStr = _max;
                }

                var _placeholder = _min + "-" + _maxStr;

                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".error-tips").html(msg);
                return false;
            } else {
                $parent.find(".error-tips").html('');

                _setFormData(formData);
                _doConfirm();

            }

        }

        function _doConfirm() {

            var formData = _getFormData();

            var _login = $("#j-loginName").val();
            var _money = formData.orderAmount;
            var _fee = formData.fee;
            var _amount = parseFloat(_money);

            var _layerTpl = '<p class="font16 my-input-title text-left">存款信息</p><div class="text-center"><table class="confirm-table">' +
                '<tr><td>游戏账号</td><td class="d-account">{{account}}</td></tr>' +
                '<tr><td>存款金额</td><td class="d-money">{{money}}</td></tr>' +
                '{{fee}}' +
                '<tr><td>所获得</td><td class="d-amount">{{amount}}</td></tr>' +
                '</table></div>';


            if (_fee == 0 || !_fee) {

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "")
                    .replace(/\{\{amount\}\}/g, _amount);

            } else {

                _amount = _amount - calcBonus(_money, _fee);

                _layerTpl = _layerTpl.replace(/\{\{account\}\}/g, _login)
                    .replace(/\{\{money\}\}/g, _money)
                    .replace(/\{\{fee\}\}/g, "<tr><td>手续费</td><td class='d-fee'>" + _fee + "%</td></tr>")
                    .replace(/\{\{amount\}\}/g, _amount);
            }

            layer.open({
                content: _layerTpl,
                title: "快捷支付",
                // closeBtn: true,
                skin: "deposit-style",
                btn: ["确认", "取消"],
                yes: function () {
                    layer.closeAll();
                    _doPay();
                }
            });
        }

        function _doPay() {

            var formData = _getFormData();
            _payTo(formData);

        }


        _initPayWay("#speedpay-page", 4);
        $(document).on("click", "#speedpay-confirm", _doCheck);
    }

    /***
     * 点卡 _DCardManage
     */
    function _DCardManage() {

        var $parent = $("#dcard-page");
        var _cardCredit = {};

        function _init() {
            $parent.find("#cardTable, .money-list").html("");
            $parent.find("#card-no, #card-password").val("");
            $parent.find("#display-dcard-pay").text("");
            $parent.find(".j-dcard-num").hide();
            _queryPayWay();
            _initComponet();
        }

        function _initComponet() {

            $(document).on("click", "#cardTable .payWayBtn", function () {
                var $that = $(this);
                var id = $that.attr("data-id");
                var moneyList = _cardCredit[id].split(",");
                var moneyHtml = "";

                if (moneyList.length > 0) {
                    $.each(moneyList, function (i, vo) {
                        moneyHtml += "<input type='button' value=" + moneyList[i] + " />";
                    });

                    $parent.find(".j-dcard-num").show();
                    $parent.find(".money-list").html(moneyHtml);
                    $parent.find(".dcard-choose-value").removeClass("hidden");
                }

                $parent.find("#display-dcard-pay").text("");
                $parent.find(".dcard-form").addClass("hidden");
            });

            $(document).on("click", $parent.find(".money-list input[type=button], #cardTable .payWayBtn"), function () {
                var result = "";
                var money = $parent.find(".money-list input[type=button].on").val();
                var fee = $parent.find("#cardTable .payWayBtn.on").attr("data-fee");
                if (money > 0 && fee > 0) {
                    result = parseInt(money) * (1 - fee);
                    if (result > 0) {
                        $("#display-dcard-pay").text('实际到账 ' + result.toFixed(2));
                        $parent.find(".j-pay-dcard-inputs").removeClass("hide");
                    }
                }
            });
        }

        function _queryPayWay() {
            var result = ajaxPost(_payWayUrl);

            if (result.desc == '成功') {
                for (var i in result.data) {
                    var o = result.data[i];
                    if (o.payWay == 5) {
                        $("#dcard-subtitle").attr('data-url', o.payCenterUrl);
                        $("#dcard-subtitle").attr('data-id', o.id);
                        $('#dcard-subtitle').attr('data-type', true);
                        _queryPayBank();
                        return false;
                    }
                }
            }

        }

        /**
         * 查询点卡
         */
        function _queryPayBank() {

            if (_TEST_MODE == 0) {
                var _platformId = $("#dcard-subtitle").attr('data-id');
                $.post("/asp/pay_bank.aspx", {platformId: _platformId}, function (response) {
                    var jsonData = $.parseJSON(response);
                    _queryCardCredit(jsonData);
                });
            } else {
                var jsonData = '{"code":"10000","desc":"成功","data":[{"dictName":"100011","dictValue":"0.13","dictShow":"5,10,20,15,30,50","dictDesc":"网易一卡通"},{"dictName":"100010","dictValue":"0.06","dictShow":"20,30,50,100,300,500","dictDesc":"联通充值卡"},{"dictName":"100009","dictValue":"0.12","dictShow":"15,30,50,100","dictDesc":"完美一卡通"},{"dictName":"100008","dictValue":"0.06","dictShow":"50,100","dictDesc":"中国电信卡"},{"dictName":"100007","dictValue":"0.13","dictShow":"5,10,15,30,60,100,200","dictDesc":"腾讯Q币卡"},{"dictName":"100006","dictValue":"0.16","dictShow":"5,10,15,20,25,30,50,100","dictDesc":"久游一卡通"},{"dictName":"100005","dictValue":"0.16","dictShow":"5,6,9,10,14,15,20,30,50,100,200,300,500,1000","dictDesc":"骏网一卡通"},{"dictName":"100004","dictValue":"0.13","dictShow":"5,10,15,25,30,35,45,50,100,300,350,1000","dictDesc":"盛大一卡通"},{"dictName":"100003","dictValue":"0.13","dictShow":"10,15,20,25,30,50,60,100,300,468,500","dictDesc":"征途游戏卡"},{"dictName":"100002","dictValue":"0.05","dictShow":"10,20,30,50,100,300,500","dictDesc":"神州行充值卡"},{"dictName":"100001","dictValue":"0.16","dictShow":"10,15,30,50,100","dictDesc":"纵游一卡通"}]}';
                jsonData = JSON.parse(jsonData);
                _queryCardCredit(jsonData);
            }

        }

        /**
         * 查询点卡面额
         */
        function _queryCardCredit(jsonData) {

            var $cardTable = $("#cardTable");

            var cardHtml = "";
            var cardTpl = '<div class="payWayBtn num-btn"' +
                'data-id="{{dictName}}"' +
                'data-fee="{{dictValue}}"' +
                'value="{{dictDesc}}"><span class="text">{{dictDesc}}</span></div>';

            $.each(jsonData.data, function (index, vo) {
                var dictName = vo.dictName;
                var dictShow = vo.dictShow;
                var dictValue = vo.dictValue;
                var dictDesc = vo.dictDesc;
                _cardCredit[dictName] = dictShow;
                cardHtml += cardTpl.replace(/\{\{dictValue\}\}/g, dictValue)
                    .replace(/\{\{dictDesc\}\}/g, dictDesc)
                    .replace(/\{\{dictName\}\}/g, dictName);
            });

            $cardTable.html(cardHtml);
        }

        function _doPay() {

            var _dcardflag = 0;

            if (!$("#cardTable .payWayBtn.on").exists()) {
                $(".dcard-error-message").removeClass("hidden");
                showLayerTimeTips('请选择充值金额')

                _dcardflag = 1;
            } else {
                $(".dcard-error-message").addClass("hidden");
            }

            if (!$("#dcard-page .money-list input[type=button].on").exists()) {
                $(".dcard-value-error-message").removeClass("hidden");
                showLayerTimeTips('请选择充值金额')
                _dcardflag = 1;
            } else {
                $(".dcard-value-error-message").addClass("hidden");
            }

            if ($("#card-password").val() == '' || $("#card-no").val() == '') {
                showLayerTimeTips('点卡账号以及密码不能为空')
                $(".dcard-pwd-error-message").html("点卡账号以及密码不能为空");
                $(".dcard-pwd-error-message").removeClass("hidden");
                _dcardflag = 1;
            } else {
                $(".dcard-pwd-error-message").addClass("hidden");
            }

            if (_dcardflag == 0) {
                var formData = {
                    platformId: $("#dcard-subtitle").attr('data-id'),
                    orderAmount: $('#dcard-page .money-list input[type=button].on').val(),
                    loginName: $("#j-loginName").val(),
                    payUrl: $("#dcard-subtitle").attr('data-url'),
                    cardCode: $("#cardTable .payWayBtn.on").attr('data-id'),
                    cardNo: $("#card-no").val(),
                    cardPassword: $("#card-password").val(),
                    usetype: 1
                };

                _payTo(formData);
            }
        }


        $(".dcardFeeBtn").click(function () {
            $("#dcardfee-page").show().siblings().hide();
        });


        $('.card-submit').click(_doPay);
        _init();
    }

    function _payTo(formData) {

        var err = _payDataCheck(formData);
        if (err) {
            _showLayer(err);
            return;
        }

        if (_TEST_MODE == 1) {
            console.log(formData);
        } else {

            if (_SUBMIT_FLAG == false) {

                _formSubmit(formData);
                _SUBMIT_FLAG = true;

                setTimeout(function () {
                    _SUBMIT_FLAG = false;
                }, 5000);
            } else {
                _showLayer("请勿重复提交订单!");
            }

        }
    }

    function _formSubmit(formData) {
        try {
            var inputs = '';
            for (var name in formData) {
                inputs += String.format(_inputHtml, name, formData[name]);
            }
            var $form = $(String.format(_formHtml, _payUrl, inputs));
            $('body').append($form);
            $form.submit();
        } catch (e) {
        }

        setTimeout(function () {
            $form.remove();
            inputs = $form = null;
        }, 1000);

    }
}


function _doMoneyCheck(payWay, formData) {

    var msg = "";
    var limitList = {
        "xxlbzfb1h5": ["30", "50", "100", "200", "500", "1000"],
        "vpzfbsm": ["5", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100", "200", "300", "500", "600", "700", "800", "900", "1000", "1500", "2000", "3000", "5000", "8000", "10000", "15000"],
        "vpwx1h5": ["100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"]
    };

    var limit = limitList[payWay];
    var amount = formData.orderAmount;

    for (var i = 0; i < limit.length; i++) {
        if (limit[i] == amount) {
            msg = "";
            return msg;
        }
    }

    msg += '此存款通道支持';
    for (var j = 0; j < limit.length; j++) {
        if (j == 0) {
            msg += limit[j] + "元";
        } else {
            msg += "、" + limit[j] + "元";
        }
    }

    return msg;

}

//在線支付資料驗證
function _payMoneyCheck(formData) {

    var result = "";

    if (!formData.orderAmount) {
        result = "金额不可为空";
    }
    if (isNaN(formData.orderAmount)) {
        result = "金额只能为数字";
    }

    return result;
}

//在線支付資料驗證
function _payDataCheck(formData) {

    if (formData.fee) {
        delete formData.fee;
    }

    if (!formData.platformId) {
        return "支付方式不可为空";
    }
    if (!formData.loginName) {
        return "用户名不可为空";
    }
    return false;
}


//重置支付秒存
function resetPaymc() {
    _FORM_DATA = "";

    $(".full-page:visible .pay-group .pay-content").eq(0).show().siblings().hide();

    $("footer, .massaage_box").hide();
    $(".step1").show().siblings().hide();
    $(".step1-title").show();
    $(".quick-name, #fast-zfb-name").val('');
    $(".quick-money, #fast-zfb-acount").val('');
    $(".j-subtitle").show();
    $(".money-input").val('');
    $('.fast-amount .num-btn').removeClass("on");
    $(".quick-submit").attr("disabled", false).show();
    $("#must-checked, #must-checked1").prop('checked', false);
}

function returnWechatPage() {
    $(".wechat-page").show().siblings().hide();
}

function returnDcardPage() {
    $(".other-page").show().siblings().hide();
}


/**
 * 选择银行
 *
 */
function checkBank() {
    var bankName = $('.j-bank-list').find('.payWayBtn.on span').html();
    $('.j-bank-box').hide();
    $('.j-bank-box2').hide();
    $('.j-bank-list-txt').html(bankName);

}

/**
 * 选择通道
 *
 */
$(document).on("click", ".j-payway-show", function () {

    var $that = $(this);
    var $parent = $that.parentsUntil(".tab-contents")

    $that.addClass('active').siblings().removeClass('active');

    var $active = $parent.find('.payway-show.active');
    var _max = $active.data('max');
    var _min = $active.data('min');

    var _maxStr = "";
    if (_max > 9999) {
        _maxStr = (_max / 10000) + "万";
    } else {
        _maxStr = _max;
    }

    var _placeholder = _min + "-" + _maxStr;

    $parent.find(".money-input").attr("placeholder", _placeholder);

    var $amountBtn = $parent.find('.fast-amount .num-btn');
    $amountBtn.show().removeClass('hide').css("display", "inline-block").filter(function () {
        return (this.innerHTML > _max || this.innerHTML < _min);
    }).hide();

});


function _showLayer(msg, btn) {
    if (btn == "" || btn == null || btn == undefined) {
        btn = '关闭';
    }
    layer.open({
        content: msg
        , btn: btn
    });
}

//验证输入input
function clearNoNum(_value) {
    // 事件中进行完整字符串检测
    var patt = /^((?!0)\d+\.[0-9][1-9])$/g;
    if (!patt.test(_value)) {
        // 错误提示相关代码，边框变红、气泡提示什么的
        showTips("输入金额格式错误");
        return true;
    }
    return false;
}


/**
 * 点按钮加状况及边诓
 */
$(document).on("click", ".num-btn,.money-list input", function () {
    var $that = $(this);
    if (!$that.hasClass('on') && !$that.hasClass('noon')) {
        $that.addClass('on').siblings().removeClass('on');
    }
});
$(document).on("click", ".j-bank-list .num-btnt", function () {
    var $that = $(this);
    if (!$that.hasClass('on') && !$that.hasClass('noon')) {
        $that.addClass('on').siblings().removeClass('on');
        $('.j-bank-list-txt').html($that.innerHTML)
    }
});
/**
 * 点金额放进输入诓，以及点其他金额时
 */
$(document).on("click", ".fast-amount .num-btn", function () {
    var $that = $(this);
    var value = $that.data('value');
    if (isNaN(value)) {
        $that.parents(".tabdiv").find(".money-input").val("").focus();
    } else {
        $that.parents(".tabdiv").find(".money-input").val(value);
    }
});

/**
 * 输入存款金额时
 */
$(document).on("click", ".money-input", function () {
    $(this).val("");
    $(".fast-amount .num-btn").removeClass('on');
    $(".fast-amount .num-btn.otherbtn").addClass('on');
});
/**
 * 点击银行列表之外的空白处关闭
 */
$(".j-bank-box").click(function (e) {
    if (!$(e.target).closest(".j-bank-list").length) {
        $(".j-bank-box").hide()
    }
});


function showLayerTips(msg, btn) {
    if (btn == "") {
        btn = '关闭';
    }
    layer.open({
        content: msg
        , btn: btn
    });
}

function showLayerTimeTips(msg, time) {
    if (time == "" || time == null || time == undefined) {
        time = 1.5;
    }
    //提示
    layer.open({
        content: msg
        , skin: 'msg'
        , time: time //2秒后自动关闭
    });
}

function _hideLayer() {
    layer.closeAll();
}


//详情返回存款首页
function closeContent() {
    $('.j-title-content').show();
    $('.pay-tabctrl ul li').removeClass('active');
    $('.j-page-content .full-page').hide();
    resetPaymc();//重置秒存
    $('.j-subtitle').css('display', 'block');
    $('.wechatpay01').css('display', 'block');
    $('.wechatpay02').css('display', 'none');
    //重置微信支付秒存
}

function ajaxPost(url, parm) {
    var RESULT;
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: parm,
        async: false,
        success: function (jsonData) {
            RESULT = jsonData;
            return RESULT;
        }
    });

    return RESULT;
}

getQueryString = function (name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

String.format = function () {
    var theString = arguments[0];

    for (var i = 1; i < arguments.length; i++) {
        var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
        theString = theString.replace(regEx, arguments[i]);
    }
    return theString;
};

jQuery.fn.exists = function () {
    return this.length > 0;
}

// <%--复制--%>
var clipboard = new Clipboard('.btn-copy');
clipboard.on('success', function (e) {
    showLayerTimeTips('复制成功')
});
clipboard.on('error', function (e) {
});


/******* 实际到账 *******/
function calcBonus(value, bonus) {

    var pay = "";
    value = parseInt(value);

    if (bonus == "1.5%") {
        pay = (value / 100) * 1.5;
    } else if (bonus == "2%") {
        pay = (value / 100) * 2;
    } else {
        pay = (value / 100) * bonus;
    }

    if (isFloat(pay)) {
        pay = pay.toFixed(2);
    }

    return pay;
}

function isFloat(x) {
    return !!(x % 1);
}

function calcPayment($that) {

    var min = "", max = "", value = "", msg = "";

    var $parent = $that.parents(".full-page:visible");
    var $panel = $parent.find(".tabdiv.active");
    var $paybox = $panel.find(".j-payway-show.active");

    if ($paybox.length > 0) {
        min = $paybox.data("min");
        max = $paybox.data("max");
    }

    if (min == "" && max == "") {
        var $payCont = $parent.find(".pay-content ul li.active");
        min = $payCont.data("min");
        max = $payCont.data("max");
    }

    value = $.trim($panel.find(".money-input").val());
    value = parseInt(value);


    if (value != "" && min != "" && max != "") {

        if (value < min || value > max) {

            if (max > 9999) {
                max = max / 10000 + '万';
                msg = min + "元-" + max;
            } else {
                msg = min + "元-" + max + "元";
            }

        }

        if (msg != "") {
            $parent.find('.error-tips').html(msg);
        } else {
            $parent.find('.error-tips').html("");
        }
    }
}


/**
 * 支付宝跳转
 *
 */
function _GoAlipay() {
    var alipayUrl = 'https://shenghuo.alipay.com/send/payment/fill.htm';
    //window.location.href='alipayqr://platformapi/startapp?saId=09999988'
    // 通过iframe的方式试图打开APP，如果能正常打开，会直接切换到APP，并自动阻止js其他行为
    var ifr = document.createElement('iframe');
    if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
        window.location.href = 'alipayqr://platformapi/startapp?saId=09999988';
    } else if (navigator.userAgent.match(/android/i)) {
        ifr.src = 'alipayqr://platformapi/startapp?saId=09999988';
    }

    ifr.style.display = 'none';
    document.body.appendChild(ifr);
    window.setTimeout(function () {
        document.body.removeChild(ifr);
        var newwindow;
        newwindow = window.open();
        newwindow.location.href = alipayUrl;
        //$('#goAlipayurl').click()
        //window.location.href = alipayUrl;//打开app下载地址
    }, 1600)


}
