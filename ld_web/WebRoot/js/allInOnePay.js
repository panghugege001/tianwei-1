var _TEST_MODE = 0;
var _RAND_MODE = 0;
var _TEST_JUAN_MODE = 0;
var _SUBMIT_FLAG = false;

if (_TEST_MODE == 0) {
    var _payWay = '/asp/pay_way.aspx';
} else {
    var _payWay = '/data/deposit/payWay99.json?v=1';
}

$(document).on("click", ".layui-layer-shade", function () {
    if ($("body").hasClass("layer-open")) {
        _hideLayer();
    }
});


$(function () {

    var action = getStr(window.location.href);
    if (action == "tab_deposit") {
        // $("html,body").animate({scrollTop: $(".topnav").offset().top}, 1000);
        if (!document['deposit']) {
            document['deposit'] = DepositPage();
        }
    }

    $(document).on("click", ".deposit", function () {
        if (!document['deposit']) {
            document['deposit'] = DepositPage();
        }
    });

    /**
     * 点二级存款方式
     */
    $(document).on("click", ".pay-box-list .pay-box", function () {

        var $that = $(this);
        var $target = $that.attr("data-target");

        // 默认通道已关闭，不予显示
        if ($that.hasClass('stop')) {
            return false;
        }

        // 变更按钮状态
        if (!$that.hasClass('active')) {
            $that.addClass('active').siblings().removeClass('active');
        }

        // 点第三方，显示连银查询资讯
        if ($target == "deposit-thirdpay-page" || $target == "deposit-speedpay-page") {
            $("#deposit-unionpay").removeClass("unvisible");
        } else {
            $("#deposit-unionpay").addClass("unvisible");
        }
    });

    /**
     * 点按钮加状况及边诓
     */

    $(document).on("click", ".btn-list input[type=button]", function () {
        var $that = $(this);
        if (!$that.hasClass('active') && !$that.hasClass('noactive')) {
            $that.addClass('active').siblings().removeClass('active');
        }
    });

    $(document).on("click", ".payWayBtn", function () {
        var $that = $(this);
        if (!$that.hasClass('active') && !$that.hasClass('noactive')) {
            $that.addClass('active').siblings().removeClass('active');
        }

        resetPaymc();

        if ($that.val() == "" || typeof $that.val() == 'undefined') {
            return false;
        }

        var _max = $that.data("max");
        var _min = $that.data("min");
        var _fee = $that.data("fee");

        var _moneyLimit = "";

        if (_max > 9999) {
            _moneyLimit = _min + "元-" + _max / 10000 + "万";
        } else {
            _moneyLimit = _min + "元-" + _max + "元";
        }

        $that.parents(".item").find(".max").text(_max);
        $that.parents(".item").find(".min").text(_min);
        $that.parents(".item").find(".payway-fee").text(_fee);
        $that.parents(".item").find(".money-input").attr("placeholder", _moneyLimit);
        $that.parents(".item").find(".ui-message").text(_moneyLimit);

        var $paybox = $that.parents(".item").find(".pay-box.active");
        $paybox.data("min", _min);
        $paybox.data("max", _max);
        if (_fee > 0) {
            $paybox.data("fee", _fee);
        }

        var payway = $that.data("payway");
        if (payway == 4) {
            var id = $('#deposit-speedpay-page .payWayBtn.active').data("id");
            var platform = $that.data("platform");
            if (platform && typeof platform != "undefined") {
                if (platform == "mbkj") {
                    $(".mbkj").removeClass("hidden");
                } else {
                    $(".mbkj").addClass("hidden");
                }

                if (platform.indexOf("mifkj") > -1) {
                    $(".mifkj").removeClass("hidden");
                    getPayBank(id, "#mifkj-bankname");
                } else {
                    $(".mifkj").addClass("hidden");
                }

                if (platform.indexOf("dbkj") > -1) {
                    $(".dbkj").removeClass("hidden");
                    getPayBank(id, "#dbkj-bankname");
                } else {
                    $(".dbkj").addClass("hidden");
                }
            }
        }

        calcFee($that);
    });


    /**
     * 输入存款金额时
     */
    $(document).on("click", ".money-input", function () {
        $(this).val("");
        $(".money-list input[type=button]").removeClass('active');
        $(".money-list input[type=button][data-value=other]").addClass('active');
        $(".money-pay").html("0元");
    });

    /**
     * 点金额放进输入诓，以及点其他金额时
     */
    $(document).on("click", ".money-list input[type=button]", function () {
        var $that = $(this);
        var value = $that.val();

        if (isNaN(value)) {
            $that.parents("table").find(".money-input").val("").focus();
            $(".money-pay").html("0元");
        } else {
            $that.parents("table").find(".money-input").val(value);
        }

        calcFee($that);
    });


    /**
     * 实际到账
     */
    $(document).on("keyup", ".money-input", function () {
        var $that = $(this);
        calcFee($that);
        calcPayment($that)
    });
});

function calcFee($that) {

    var $parent = $that.parents(".item.on");
    var $panel = $parent.find(".tab-panel.active");
    var $paybox = $parent.find(".pay-box.active");
    var bonus = $paybox.data("bonus"), fee = '';
    if ($panel.length > 0) {
        fee = $panel.find(".payWayBtn.active").data("fee")
    } else {
        fee = $parent.find('.payway-list .payWayBtn.active').data("fee");
    }

    // from Panel
    var value = parseFloat($.trim($panel.find(".money-input").val()));

    // from Parent
    if (isNaN(value) || value == "") {
        value = parseFloat($.trim($parent.find(".money-input").val()));
    }

    if (value != "") {

        var pay = "";

        if (!fee) {
            fee = 0;
        }

        if (bonus == "1.5%") {
            pay = value + (value / 100) * 1.5;
        } else if (bonus == "2%") {
            pay = value + (value / 100) * 2;
        } else {
            pay = value - (value / 100) * fee;
        }
        if (isFloat(pay)) {
            pay = pay.toFixed(2);
        }

        if (isNaN(pay)) {
            return false;
        }

        $parent.find(".money-pay").html(pay + "元");
    } else {
        $parent.find(".money-pay").html("0元");
    }
}

function isFloat(x) {
    return !!(x % 1);
}

function calcPayment($that) {

    var $parent = $that.parents(".item.on");
    var $panel = $parent.find(".tab-panel.active");
    var $paybox = $parent.find(".pay-box.active");

    var min = $paybox.data("min");
    var max = $paybox.data("max");

    // from Panel
    var value = $.trim($panel.find(".money-input").val());

    // from Parent
    if (isNaN(value) || value == "") {
        value = $.trim($parent.find(".money-input").val());
    }

    if (value != 0) {
        value = parseInt(value);
    }

    if (value != "" && min != "" && max != "") {

        var msg = "";

        if (value < min || value > max) {

            if (max == '3000000') {
                max = '300万';
            }

            msg = "请输入" + min + "元-" + max + "元之间的金额";
        }

        if (msg != "") {
            $parent.find(".money-error-message").text(msg).removeClass("hidden");
        } else {
            $parent.find(".money-error-message").text("").addClass("hidden");
        }
    }
}


function DepositPage() {

    var _dep = new Array();
    var _deposit = new Array();
    var _payUrl = '/asp/pay_api.aspx';
    var _formHtml = '<form action="{0}" method="post" target="_blank">{1}</form>';
    var _inputHtml = '<input type="hidden" name="{0}" value="{1}"/>';

    _init();

    /**
     * 初始化
     */
    function _init() {

        layer.load(2, {shade: [0.7, '#000'], offset: ['46%', '45%']});

        for (var i = 0; i < 20; i++) {
            _deposit[i] = new Array();
        }

        var payWayData = ajaxPost(_payWay, {"usetype": "2"});
        // if(_TEST_MODE == 0){
        //     payWayData = $.parseJSON(payWayData);
        // }
        if (payWayData && payWayData.desc == '成功') {
            for (var i in payWayData.data) {
                var o = payWayData.data[i];
                if (o) {
                    _deposit[o.payWay].push(o);
                }
            }
        }

        _filterPayPage(payWayData);
        _hideLayer();
        $("#money_in li").bind('click', _titleClickEvent);
        $(".pay-box").bind('click', _subTitleClickEvent);
        _fastPayManage();
        _cloudPayManage();
    }

    function _filterPayPage(jsonData) {

        var _dataList = "";

        var payPage = {
            "data": [
                {
                    "title": "#deposit-zfb",
                    "subtitle": "#deposit-zfbfast-subtitle",
                    "payWay": "",
                    "name": "支付宝转账",
                    "state": 1,
                    "submit": ".deposit-fast-submit"
                }, {
                    "title": "#deposit-zfb",
                    "subtitle": "#deposit-zfbQR-subtitle",
                    "payWay": "1",
                    "name": "支付宝扫码",
                    "state": 0,
                    "submit": "#deposit-zfbQR2-submit"
                }, {
                    "title": "#deposit-wexin",
                    "subtitle": "#deposit-wexinQR-subtitle",
                    "payWay": "2",
                    "name": "微信扫码支付",
                    "state": 0,
                    "submit": "#deposit-wexinQR-submit"
                }, {
                    "title": "#deposit-bank",
                    "subtitle": "#deposit-thirdpay-subtitle",
                    "payWay": "3",
                    "name": "在线支付",
                    "state": 0,
                    "submit": "#deposit-thirdpay-submit"
                }, {
                    "title": "#deposit-bank",
                    "subtitle": "#deposit-yinlian-subtitle",
                    "payWay": "13",
                    "name": "银联支付",
                    "state": 0,
                    "submit": "#deposit-yinlian-submit"
                }, {
                    "title": "#deposit-bank",
                    "subtitle": "#deposit-speedpay-subtitle",
                    "payWay": "4",
                    "name": "快捷支付",
                    "state": 0,
                    "submit": "#deposit-speedpay-submit"
                }, {
                    "title": "#deposit-dcard",
                    "payWay": "5",
                    "name": "点卡",
                    "state": 0,
                    "submit": "#deposit-card-submit"
                }, {
                    "title": "#deposit-qq",
                    "payWay": "7",
                    "name": "QQ支付",
                    "state": 0,
                    "submit": "#deposit-qq-submit"
                }, {
                    "title": "#deposit-gather",
                    "payWay": "8",
                    "name": "万能支付",
                    "state": 0,
                    "submit": "#deposit-gather-submit"
                }, {
                    "title": "#deposit-other",
                    "subtitle": "#deposit-meituan-subtitle",
                    "payWay": "14",
                    "name": "美团支付",
                    "state": 0,
                    "submit": "#deposit-meituan-submit"
                }, {
                    "title": "#deposit-other",
                    "subtitle": "#deposit-jd-subtitle",
                    "payWay": "10",
                    "name": "京东支付",
                    "state": 0,
                    "submit": "#deposit-jd-submit"
                }, {
                    "title": "#deposit-wexin",
                    "subtitle": "#deposit-wexinfast-subtitle",
                    "payWay": "",
                    "name": "微信转账",
                    "state": 1,
                    "submit": ".deposit-fast-submit"
                }, {
                    "title": "#deposit-jiuan",
                    "payWay": "12",
                    "name": "久安支付",
                    "state": 0,
                    "submit": "#deposit-jiuan-submit"
                }, {
                    "title": "#deposit-bank",
                    "subtitle": "#deposit-fastpay-subtitle",
                    "payWay": "",
                    "name": "网银转账",
                    "state": 1,
                    "submit": ".deposit-fast-submit"
                }]
        };

        // 随机取值测试
        if (_TEST_MODE == 1 && _RAND_MODE == 1) {
            var _dataLst = jsonData.data;
            var _randDataList = new Array();
            for (i = 0; i < 3; i++) {
                var rand = _dataLst[Math.floor(Math.random() * _dataLst.length)];
                _randDataList.push(rand);
            }
            jsonData.data = _randDataList;
        }

        // 判断开启
        if (jsonData) {
            for (var i in jsonData.data) {
                var payWay = jsonData.data[i].payWay;
                for (var j = 0; j < payPage.data.length; j++) {
                    _dataList = payPage.data;
                    if (payWay == _dataList[j].payWay) {
                        var obj = _dataList[j];
                        obj.state = 1; // 開啟
                    }
                }
            }
        }


        if (_dataList.length > 0) {

            // 关闭通道，并添加维护中
            for (var i = 0; i < _dataList.length; i++) {
                var obj = _dataList[i];
                if (obj.state != 1) {
                    $(obj.title).addClass('stop');
                    $(obj.subtitle).addClass('stop');
                    if (!$(obj.title).find("a").exists()) {
                        $(obj.title).find(".wrap-box").wrap("<a href='javascript:;' title='维护中'></a>");
                    }
                    if (!$(obj.subtitle).find("a").exists()) {
                        $(obj.subtitle).find(".wrap-box").wrap("<a href='javascript:;' title='维护中'></a>");
                    }
                    $(obj.parent).addClass('stop');
                    $(obj.submit).attr("disabled", "disabled").hide();
                }
            }

            var open_meituan_flag = false;

            // 开启通道
            for (var i = 0; i < _dataList.length; i++) {
                var obj = _dataList[i];
                if (obj.state == 1) {
                    $(obj.title).removeClass('stop');
                    $(obj.title).find("a").contents().unwrap();
                    $(obj.subtitle).removeClass('stop');
                    $(obj.subtitle).find("a").contents().unwrap();
                    $(obj.parent).removeClass('stop');
                    $(obj.submit).removeAttr("disabled").show();

                    $(obj.subtitle).addClass('active').siblings().removeClass("active");

                    if (obj.subtitle == "#deposit-jd-subtitle") {
                        open_meituan_flag = true;
                    }
                }
            }

            var $meituanSubtitle = $("#deposit-meituan-subtitle");
            if (open_meituan_flag == false) {
                var $target = $meituanSubtitle.data("target");
                $("#" + $target).addClass("active").siblings().removeClass("active");
            }

            setTimeout(function () {
                _openDefaultTag(_dataList);
            }, 200);
        }

    }

    // 开启默认选项卡
    function _openDefaultTag(_dataList) {

        for (var i = 0; i < _dataList.length; i++) {
            var obj = _dataList[i];
            if (obj.state == 1) {
                $(obj.title).removeClass("stop").addClass("on");
                if ($(obj.subtitle).exists() && !$(obj.subtitle).hasClass("stop")) {
                    $(obj.title).trigger("click");
                    $(obj.subtitle).trigger("click");
                } else {
                    $(obj.title).trigger("click");
                }
                return false;
            }
        }
    }


    $('.money_in1 li').click(function (e) {
        e.preventDefault();

        if ($(this).hasClass('stop')) return false;

        var ind = $(this).index();
        if (!$(this).hasClass('on')) {
            $(this).addClass('on').siblings().removeClass('on');
        }
        $(this).parent().siblings('.items').find('.item').eq(ind).addClass('on').siblings().removeClass('on');
    });


    function _titleClickEvent() {

        if (!_dep[this.id]) {
            switch (this.id) {

                case 'deposit-jiuan':
                    _dep[this.id] = new _JIUANManage();
                    break;
                case 'deposit-qq':
                    _dep[this.id] = new _QQManage();
                    break;
                default:
            }
        }

        resetPaymc();

        setTimeout(function () {
            var $that = $(".item.on");
            $that.find(".pay-box:not(.stop)").eq(0).trigger("click");
        }, 200);
    }


    function _subTitleClickEvent() {
        var $that = $(this);
        var $target = $that.attr("data-target");

        // 默认通道已关闭，不予显示
        if ($that.hasClass('stop')) {
            return false;
        }


        if (!_dep[$target]) {
            switch ($target) {
                case 'deposit-zfbQR-page':
                    _dep[$target] = new _ZFBManage();
                    break;
                case 'deposit-wexinQR-page':
                    _dep[$target] = new _WeixinManage();
                    break;
                case 'deposit-thirdpay-page':
                    _dep[$target] = new _ThirdPayManage();
                    break;
                case 'deposit-speedpay-page':
                    _dep[$target] = new _SpeedPayManage();
                    break;
                case 'deposit-meituan-page':
                    _dep[$target] = new _MeituanManage();
                    break;

                case 'deposit-yinlian-page':
                    _dep[$target] = new _YinlianManage();
                    break;
                case 'deposit-jd-page':
                    _dep[$target] = new _JDManage();
                    break;
                case 'deposit-dcard-page':
                    _dep[$target] = new _DCardManage();
                    break;

                default:
            }
        }

        $(".money-pay").html("0元");
        $(".money-input").val("");
        $("#" + $target).addClass("active").siblings().removeClass("active");
    }

    function _buildHtml(o) {

        var payWayHtml = "";

        var payWayTpl = '<div class="payWayBtn" data-url="{{payCenterUrl}}" ' +
            'data-id="{{id}}"  ' +
            'data-payway="{{payway}}"  ' +
            'data-max={{maxPay}} ' +
            'data-min="{{minPay}}" ' +
            'data-fee="{{fee}}" ' +
            'data-platform="{{payPlatform}}" ' +
            'value="{{showName}}" ><span class="text {{style}}">{{showName}}</span><dd>{{hot}}</dd></div>';

        if (!o) {

            payWayHtml = payWayTpl.replace(/\{\{payCenterUrl\}\}/g, "")
                .replace(/\{\{id\}\}/g, "")
                .replace(/\{\{maxPay\}\}/g, "")
                .replace(/\{\{minPay\}\}/g, "")
                .replace(/\{\{payway\}\}/g, "")
                .replace(/\{\{fee\}\}/g, "")
                .replace(/\{\{hot\}\}/g, "")
                .replace(/\{\{style\}\}/g, "left0")
                .replace(/\{\{showName\}\}/g, "维护中");

        } else {

            if (o.payPlatform == "tyjfzfb" || o.payPlatform == "jlkwxsm" || o.payPlatform == "ztzfb" || o.payPlatform == "vpzfbsm" || o.payPlatform == "syfzfbsm") {
                payWayHtml = payWayTpl.replace(/\{\{payCenterUrl\}\}/g, o.payCenterUrl)
                    .replace(/\{\{id\}\}/g, o.id)
                    .replace(/\{\{payway\}\}/g, o.payWay)
                    .replace(/\{\{maxPay\}\}/g, o.maxPay)
                    .replace(/\{\{minPay\}\}/g, o.minPay)
                    .replace(/\{\{payPlatform\}\}/g, o.payPlatform)
                    .replace(/\{\{fee\}\}/g, o.fee)
                    .replace(/\{\{style\}\}/g, "")
                    .replace(/\{\{hot\}\}/g, "推荐")
                    .replace(/\{\{showName\}\}/g, o.showName);
            } else {

                payWayHtml = payWayTpl.replace(/\{\{payCenterUrl\}\}/g, o.payCenterUrl)
                    .replace(/\{\{id\}\}/g, o.id)
                    .replace(/\{\{payway\}\}/g, o.payWay)
                    .replace(/\{\{maxPay\}\}/g, o.maxPay)
                    .replace(/\{\{minPay\}\}/g, o.minPay)
                    .replace(/\{\{payPlatform\}\}/g, o.payPlatform)
                    .replace(/\{\{fee\}\}/g, o.fee)
                    .replace(/\{\{style\}\}/g, "")
                    .replace(/\{\{hot\}\}/g, "")
                    .replace(/\{\{showName\}\}/g, o.showName);
            }
        }

        return payWayHtml;
    }

    /***
     * 一码付
     */
    function _GatherManage() {

        $("#deposit-gather").addClass("stop");
        $("#deposit-gather").find(".wrap-box").wrap("<a href='javascript:;' title='维护中'></a>");

        // var _gatherPayWay = "";
        // if(_TEST_MODE == 0) {
        // 	_gatherPayWay = "/mobi/getGatherDeposti.aspx";
        // } else {
        // 	_gatherPayWay = "/data/deposit/gather_false.json";
        // }
        //
        // var gatherData = ajaxPost(_gatherPayWay, { "platformID": "tlzf" });
        // if(gatherData.success == true) {
        // 	$("#money_in li").eq(0).trigger("click");
        // 	$("#money_in li").eq(0).addClass("on");
        // 	$('#j-gather-codeimg').attr('src', gatherData.data.bank.zfbImgCode);
        // 	return false;
        // } else {
        // 	$("#money_in li").eq(1).trigger("click");
        // 	$("#money_in li").eq(1).addClass("on");
        // 	$("#deposit-gather").addClass("stop");
        // 	$("#deposit-gather").find(".wrap-box").wrap("<a href='javascript:;' title='维护中'></a>");
        // 	return true;
        // }

    }

    /***
     * 支付宝扫码
     */
    function _ZFBManage() {

        var $parent = $("#deposit-zfbQR-page");
        var _payway = 1;
        var _zfbPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {

            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }

            var obj = _deposit[_payway][_zfbPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }

                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {
            var formData = {
                platformId: $parent.find('.payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find('.payWayBtn.active').attr("data-url"),
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");

                var platform = $parent.find(".payWayBtn.active").data("platform");

                if (platform == "vpzfbsm") {

                    var vpmsg = _vpAlipayMoneyCheck(formData);
                    if (vpmsg != "") {
                        _showLayer(vpmsg);
                    } else {
                        _payTo(formData);
                    }

                } else {
                    _payTo(formData);
                }
            }

        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;
            }

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
        }

        _queryPayWay();
        $(document).on("click", "#deposit-zfbQR2-submit", _doPay);
        $(document).on("click", ".deposit-zfbQR2-reload .payWayBtn", _reload);
    }

    /***
     * 微信扫码
     */
    function _WeixinManage() {

        var $parent = $("#deposit-wexinQR-page");
        var _payway = 2;
        var _weixinPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {

            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }

            var obj = _deposit[_payway][_weixinPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;
                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }

                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);
            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {

            var formData = {
                platformId: $parent.find(".payWayBtn.active").attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find(".payWayBtn.active").attr("data-url"),
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");

                var platform = $parent.find(".payWayBtn.active").data("platform");

                if (platform == "vpwxsm") {

                    var vpmsg = _vpMoneyCheck(formData);
                    if (vpmsg != "") {
                        _showLayer(vpmsg);
                    } else {
                        _payTo(formData);
                    }

                } else {
                    _payTo(formData);
                }
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
            $parent.find('.payway-fee').html(_fee)
        }

        _queryPayWay();
        $(document).on("click", "#deposit-wexinQR-submit", _doPay);
        $(document).on("click", ".deposit-wexinQR-reload .payWayBtn", _reload);
    }

    /***
     * 久安支付
     */
    function _JIUANManage() {

        var $parent = $('#deposit-jiuan-page');
        var _payway = 12;
        var _qqPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {

            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }

            var obj = _deposit[_payway][_qqPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {
            var formData = {
                platformId: $parent.find('.payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find('.payWayBtn.active').attr("data-url"),
                bank_code: "ZF_WX",
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");

                if (_SUBMIT_FLAG == false) {

                    _formJiuan(formData);

                    _SUBMIT_FLAG = true;

                    setTimeout(function () {
                        _SUBMIT_FLAG = false;
                    }, 5000);
                } else {
                    _showLayer("请勿重复提交订单!");
                }

            }
        }

        function _formJiuan(formData) {

            var _JiuanUrl = '', html = '';
            $('#payToJiuan').remove();

            layer.load(2, {shade: [0.7, '#000'], offset: ['46%', '45%']});

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
                                "notifyUrl": "https://" + window.location.host + "/asp/payPage.aspx?tab_deposit"
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


        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
            $parent.find('.payway-fee').html(_fee)
        }

        _queryPayWay();
        $(document).on("click", "#deposit-jiuan-submit", _doPay);
        $(document).on("click", ".deposit-jiuan-reload .payWayBtn", _reload);
    }

    /***
     * QQ钱包
     */
    function _QQManage() {

        var $parent = $('#deposit-qq-page');
        var _payway = 7;
        var _qqPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {

            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }

            var obj = _deposit[_payway][_qqPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);
        }

        function _doPay() {
            var formData = {
                platformId: $parent.find('.payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find('.payWayBtn.active').attr("data-url"),
                bank_code: "ZF_WX",
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
                _payTo(formData);
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
            $parent.find('.payway-fee').html(_fee)
        }

        _queryPayWay();
        $(document).on("click", "#deposit-qq-submit", _doPay);
        $(document).on("click", ".deposit-qq-reload .payWayBtn", _reload);
    }

    /***
     * 美团支付
     */
    function _MeituanManage() {

        var $parent = $('#deposit-meituan-page');
        var _payway = 14;
        var _qqPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {

            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }

            var obj = _deposit[_payway][_qqPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {
            var formData = {
                platformId: $parent.find('.payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find('.payWayBtn.active').attr("data-url"),
                bank_code: "ZF_WX",
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
                _payTo(formData);
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
        }

        _queryPayWay();
        $(document).on("click", "#deposit-meituan-submit", _doPay);
        $(document).on("click", ".deposit-meituan-reload .payWayBtn", _reload);
    }

    /***
     * 银联支付
     */
    function _YinlianManage() {

        var $parent = $('#deposit-yinlian-page');
        var _payway = 13;
        var _qqPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {

            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }

            var obj = _deposit[_payway][_qqPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {
            var formData = {
                platformId: $parent.find('.payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find('.payWayBtn.active').attr("data-url"),
                bank_code: "ZF_WX",
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
                _payTo(formData);
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
        }

        _queryPayWay();
        $(document).on("click", "#deposit-yinlian-submit", _doPay);
        $(document).on("click", ".deposit-yinlian-reload .payWayBtn", _reload);
    }

    /***
     * 京东支付
     */
    function _JDManage() {

        var $parent = $('#deposit-jd-page');
        var _payway = 10;
        var _qqPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _queryPayWay() {
            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }
            var obj = _deposit[_payway][_qqPayWayIndex];
            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {
            var formData = {
                platformId: $parent.find('.payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $("#deposit-loginname").val(),
                payUrl: $parent.find('.payWayBtn.active').attr("data-url"),
                bank_code: "ZF_WX",
                usetype: 2
            };
            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
                _payTo(formData);
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
        }

        _queryPayWay();
        $(document).on("click", "#deposit-jd-submit", _doPay);
        $(document).on("click", ".deposit-jd-reload .payWayBtn", _reload);
    }

    /***
     * 支付
     */
    function _ThirdPayManage() {

        var $parent = $('#deposit-thirdpay-page');
        var _payway = 3;
        var _thirdPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _init() {
            $("#bank-list, #third-list").html("");
            _queryPayWay();
        }

        function _queryPayWay() {
            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }
            var obj = _deposit[_payway][_thirdPayWayIndex];

            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);
            $parent.find(".payway-list .payWayBtn").eq(0).addClass('active')

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

            _queryPayBank();

        }

        function _queryPayBank() {
            var payBankUrl = "";

            if (_TEST_MODE == 0) {
                payBankUrl = "/api/pay/thirdPaymentBank/list";
            } else {
                payBankUrl = "/data/deposit/payBank2.json";
            }

            var _dataUrl = $parent.find(".payWayBtn.active").attr("data-url");
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
            var bankTpl = '<div class="payWayBtn "' + 'data-value="{{dictValue}}" value="{{dictName}}"><i class="bank-sprite {{icon}}"></i><span class="text">{{dictName}}</span></div>';

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

        //第三方支付
        function _doPay() {

            var _thirdPayFlag = 0;

            var formData = {
                platformId: $parent.find('.payway-list .payWayBtn.active').attr("data-id"),
                orderAmount: $parent.find(".money-input").val(),
                loginName: $('#deposit-loginname').val(),
                payUrl: $parent.find('.payway-list .payWayBtn.active').attr("data-url"),
                bankCode: $parent.find('#bank-list .payWayBtn.active').attr("data-value"),
                usetype: 2
            };


            if (formData.bankCode == "" || typeof formData.bankCode == 'undefined') {
                $parent.find(".bank-error-message").removeClass("hidden");
                _thirdPayFlag = 1;
            } else {
                $parent.find(".bank-error-message").addClass("hidden");
            }

            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
                _thirdPayFlag = 1;
            } else {
                $parent.find(".money-error-message").addClass("hidden");
            }

            if (_thirdPayFlag == 0) {
                _payTo(formData);
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");
            _queryPayBank();

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
        }

        _init();
        $(document).on("click", "#deposit-thirdpay-submit", _doPay);
        $(document).on("click", ".deposit-thirdPay-reload .payWayBtn", _reload);
    }

    /***
     * 快捷支付
     */
    function _SpeedPayManage() {

        var $parent = $("#deposit-speedpay-page");
        var _payway = 4;
        var _speedPayWayIndex = 0;
        var _max = 0,
            _min = 0,
            _fee = 0,
            _placeholder = "";

        function _init() {
            _queryPayWay();
        }

        function _queryPayWay() {
            for (var i = 0, payWay = ''; i < _deposit[_payway].length; i++) {
                payWay += _buildHtml(_deposit[_payway][i]);
            }
            var obj = _deposit[_payway][_speedPayWayIndex];

            var count = _deposit[_payway].length;

            if (obj) {
                _max = obj.maxPay;
                _min = obj.minPay;
                _fee = obj.fee;

                if (_max > 10000) {
                    var _maxstr = (_max / 10000);
                    _placeholder = _min + "元-" + _maxstr + "万";
                } else {
                    _placeholder = _min + "-" + _max;
                }
                $parent.find('.money-input').attr("placeholder", _placeholder);
            }

            $parent.find(".payway-list").html(payWay);

            if (count < 1 || count == 1) {
                $parent.find(".payway-list-box").hide();
                $parent.find(".payway-list .payWayBtn .text").css("left", "auto");
            }

            setTimeout(function () {
                $parent.find(".payway-list .payWayBtn").eq(0).trigger("click");
            }, 200);

        }

        function _doPay() {

            var payway = $parent.find(".payWayBtn.active").data('payway');
            var platform = $parent.find(".payWayBtn.active").data('platform');


            if (platform == "mbkj") {
                var $bankcard = $("#speedpay-bankcard").val();
                var $bankname = $("#speedpay-bankname").val();
                var $phoneNumber = $("#speedpay-phoneNumber").val();

                if ($bankcard == "") {
                    _showLayer("[提示]请填写银行卡!");
                    return false;
                }

                if ($bankname == "") {
                    _showLayer("[提示]请填写银行卡户名!");
                    return false;
                }

                if ($phoneNumber == "") {
                    _showLayer("[提示]请填写手机号!");
                    return false;
                }
            }

            if (platform.indexOf("mifkj") > -1) {
                var $bankcard = $("#mifkj-bankcard").val();
                var $bankname = $("#mifkj-bankname option:selected").val();

                if ($bankcard == "") {
                    _showLayer("[提示]请填写银行卡!");
                    return false;
                }

                if ($bankname == "") {
                    _showLayer("[提示]请填写银行卡户名!");
                    return false;
                }

            }

            if (platform.indexOf("dbkj") > -1) {

                var $bankname = $("#dbkj-bankname option:selected").val();

                if ($bankname == "") {
                    _showLayer("[提示]请填写银行卡户名!");
                    return false;
                }

            }


            var formData = {
                platformId: $parent.find(".payWayBtn.active").attr('data-id'),
                orderAmount: $parent.find('.money-input').val(),
                loginName: $('#deposit-loginname').val(),
                payUrl: $parent.find(".payWayBtn.active").attr('data-url'),
                usetype: 2,

                bankcard: $bankcard,
                bankname: $bankname,
                phoneNumber: $phoneNumber,

                bankCode: $bankname
            };


            var msg = _payMoneyCheck(formData);
            if (formData.orderAmount < _min || formData.orderAmount > _max) {
                msg = "请输入" + _placeholder + "之间的金额";
            }
            if (msg != "") {
                $parent.find(".money-error-message").text(msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
                _payTo(formData);
            }
        }

        function _reload() {
            $(this).addClass('active').siblings().removeClass('active')
            $parent.find(".money-error-message").text("").addClass("hidden");

            //			更新金额上下限
            var obj = _deposit[_payway][$(this).index()];
            _max = obj.maxPay;
            _min = obj.minPay;
            _fee = obj.fee;

            if (_max > 10000) {
                var _maxstr = (_max / 10000);
                _placeholder = _min + "元-" + _maxstr + "万";
            } else {
                _placeholder = _min + "-" + _max;
            }

            var $that = $parent.find('.money-list input[type=button]');
            $that.show().removeClass('hide').css("display", "inline-block").filter(function () {
                return ($(this).val() > _max || $(this).val() < _min);
            }).hide();

            $parent.find('.money-input').attr("placeholder", _placeholder);
        }

        _init();
        $(document).on("click", "#deposit-speedpay-submit", _doPay);
        $(document).on("click", ".deposit-speedPay-reload .payWayBtn", _reload);
    }

    /***
     * 点卡
     */
    function _DCardManage() {

        var $parent = $("#user_dianka");
        var _cardCredit = {};

        function _init() {
            $parent.find("#cardTable, .money-list").html("");
            $parent.find("#deposit-card-no, #deposit-card-password").val("");
            $parent.find("#display-dcard-pay").text("");
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

                    $parent.find(".money-list").html(moneyHtml);
                    $parent.find(".dcard-choose-value").removeClass("hidden");
                }

                $parent.find("#display-dcard-pay").text("");
                $parent.find(".dcard-form").addClass("hidden");
            });

            $(document).on("click", $parent.find(".money-list input[type=button], #cardTable .payWayBtn"), function () {
                var result = "";
                var money = $parent.find(".money-list input[type=button].active").val();
                var fee = $parent.find("#cardTable .payWayBtn.active").attr("data-fee");

                if (money > 0 && fee > 0) {
                    result = parseInt(money) * (1 - fee);
                    if (result > 0) {
                        $("#display-dcard-pay").text(result.toFixed(2));
                        $parent.find(".dcard-form").removeClass("hidden");
                    }
                }
            });
        }

        function _queryPayWay() {
            var jsonData = ajaxPost(_payWay, "");
            var dataList = jsonData.data
            for (var i = 0; i < dataList.length; i++) {
                var o = dataList[i];
                if (o.payWay == 5) {
                    $("#deposit-dcard").attr('data-url', o.payCenterUrl);
                    $("#deposit-dcard").attr('data-id', o.id);
                    $('#deposit-dcard').attr('data-type', true);
                    _queryPayBank();
                    return false;
                }
            }
        }


        /**
         * 查询点卡
         */
        function _queryPayBank() {
            if (_TEST_MODE == 0) {
                var _platformId = $("#deposit-dcard").attr('data-id');
                var jsonData = ajaxPost("/asp/pay_bank.aspx", {platformId: _platformId});
                _queryCardCredit(jsonData);
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
            var cardTpl = '<div class="payWayBtn"' +
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

            if (!$("#cardTable .payWayBtn.active").exists()) {
                $(".dcard-error-message").removeClass("hidden");
                _dcardflag = 1;
            } else {
                $(".dcard-error-message").addClass("hidden");
            }

            if (!$(".item5.on .money-list input[type=button].active").exists()) {
                $(".dcard-value-error-message").removeClass("hidden");
                _dcardflag = 1;
            } else {
                $(".dcard-value-error-message").addClass("hidden");
            }

            if ($("#deposit-card-password").val() == '' || $("#deposit-card-no").val() == '') {
                $(".dcard-pwd-error-message").removeClass("hidden");
                _dcardflag = 1;
            } else {
                $(".dcard-pwd-error-message").addClass("hidden");
            }

            if (_dcardflag == 0) {
                var formData = {
                    platformId: $("#deposit-dcard").attr('data-id'),
                    orderAmount: $('.item5.on .money-list input[type=button].active').val(),
                    loginName: $("#deposit-loginname").val(),
                    payUrl: $("#deposit-dcard").attr('data-url'),
                    cardCode: $("#cardTable .payWayBtn.active").attr('data-id'),
                    cardNo: $("#deposit-card-no").val(),
                    cardPassword: $("#deposit-card-password").val(),
                    usetype: 2
                };

                _payTo(formData);
            }
        }

        $('#deposit-card-submit').click(_doPay);
        _init();
    }

    // 云闪付
    function _cloudPayManage() {

        var saveCard = "", saveMoney = "";
        var $parent = $("#deposit-cloud-page");

        var $saveCard = "", $saveMoney = "";
        var $saveSubmitBtn = $(".cloudpay-next-btn");
        var $saveRestartBtn = $(".cloudpay-return-btn");
        var $saveSuccessBtn = $(".cloudpay-success-btn");

        var $quota = $(".quota");
        var $saveCheck = $("#j-cloudpay-checked");
        var $saveCheck1 = $("#j-cloudpay-checked1");
        var $agreeSubmitBtn = $("#j-cloud-agree");

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
                e.preventDefault();
                layer.closeAll();
                _resetCloudPay();
            });

        }

        function _chkInfo() {

            var card_msg = "", money_msg = "";

            $saveCard = $parent.find(".cloud-card");
            $saveMoney = $parent.find(".money-input");

            saveCard = $saveCard.val();
            saveMoney = $saveMoney.val();

            if (saveCard == '' || saveCard == null || !saveCard) {
                card_msg = "请填写您的银行卡号";
            }

            if (saveCard.length != 4) {
                card_msg = "请输入银行卡号四位数";
            }

            if (card_msg != "") {
                $parent.find(".cloud-card-error-message").text(card_msg).removeClass("hidden");
                return false;
            } else {
                $parent.find(".cloud-card-error-message").text("").addClass("hidden");
            }

            if (saveMoney == '' || saveMoney == null || !saveMoney) {
                money_msg = "请输入存款金额";
            }
            if (saveMoney < 1 || saveMoney > 3000000) {
                money_msg = "请输入1元到300万的金额";
            }

            if (isNaN(saveMoney)) {
                money_msg = "存款金额不得包含汉字";
            }

            var reg = /^[1-9]\d*$/;
            if (!reg.test(saveMoney)) {
                money_msg = "请输入整数";
            }

            if (money_msg != "") {
                $parent.find(".money-error-message").text(money_msg).removeClass("hidden");
                return false;
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
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


            //  创建订单
            _deposit(formData);

            // 同意书页面
            // $(".deposit-cloudpay-2").removeClass("hidden").siblings().addClass("hidden");
            //
            // 同意书
            // $agreeSubmitBtn.off('click').on("click", function (e) {
            //     e.preventDefault();
            //     if ($saveCheck.prop('checked') && $saveCheck1.prop('checked')) {

            // return true;
            // } else {
            //     _showLayer("请同意天威存款条例！");
            //     return false;
            // }
            // });
        }


        function _deposit(formData) {

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
                    layer.confirm('您上一笔订单未支付，可点击作废订单，系统将生成新的存款信息，按存款信息存款即可到账。', {
                        title: '温馨提示', //按钮
                        btn: ['作废上笔订单，生成新订单', '取消'], //按钮
                        offset: ['36%', '38%']
                    }, function () {

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
                            _showLayer(jsonData.massage);
                            _resetCloudPay();
                            return false;
                        }
                    });

                } else {
                    _showLayer(jsonData.massage);
                    _resetCloudPay();
                    return false;
                }

            }

        }


        function _showDepositInfo(response) {

            var zfbImgCode = response.zfbImgCode;

            _hideLayer();

            if (zfbImgCode != "") {
                $("#cImgCode").attr("src", zfbImgCode);
                $(".deposit-cloudpay-3").removeClass("hidden").siblings().addClass("hidden");
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
            $(".deposit-cloudpay-1").removeClass("hidden").siblings().addClass("hidden");
            $(".money-pay").html("0元");
        }

        _init();
    }


    /***
     * 支付宝/网银/手机 秒存
     */
    var TIMER = "";

    function _fastPayManage() {

        var $parent = "", $panel = "";

        var _fastData = "", _bankType = "", _bankname = "", _card = "", saveDepositId = "",
            renewData = "";

        function _init() {

            _stopTimer();

            $(".deposit-fastpay-1").removeClass("hidden").siblings().addClass("hidden");

            $(document).find('.fastpay-next-btn').click(function () {
                _chkInfo($(this));
            });

            $(document).on('click', '.arrow-left, .arrow-right, .arrow-teach', function () {
                var href = $(this).data("href");
                if (href != "") {
                    $parent.find(href).removeClass("hidden").siblings().addClass("hidden");
                }
            });

            $(document).on("click", ".fastpay-success-btn", function () {
                $parent.find(".deposit-fastpay-3").removeClass("hidden").siblings().addClass("hidden");
                _stopTimer();
                _countDown("600", $('.fastpay-time'));
                renewData = "";
            });

            $(document).on("click", ".fastpay-return-btn", function () {
                _stopTimer();
                resetPaymc();
                renewData = "";
            });

            $(document).on("click", ".fastpay-renew-btn", function () {

                layer.load(2, {shade: [0.7, '#000'], offset: ['46%', '45%']});

                var newDepositUrl = "";

                if (_TEST_MODE == 1) {
                    newDepositUrl = "/data/deposit/newDeposit1.json";
                } else {
                    newDepositUrl = "/asp/getNewdeposit.aspx";
                }

                layer.confirm('此操作将作废当前订单，页面会获取新的银行卡，请重新存款。', {
                    title: '温馨提示',
                    btn: ['换卡', '取消'], //按钮
                    offset: ['36%', '38%']
                }, function () {

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

                }, function () {
                    _hideLayer();
                });
            });

        }

        function _chkInfo(e) {

            var $up = $(e).parents(".pay-list");
            $parent = $up.children(".item.on");
            $panel = $parent.find(".tab-panel.active");

            _bankType = $parent.find('.pay-box.active').data("value");

            if (!_bankType || _bankType == "") {
                _bankType = $panel.find(".payway-list .payWayBtn.active").data("value");
            }

            var name_msg = "", money_msg = "", care_msg = "";

            if ($panel.hasClass("active")) {
                var _money = $panel.find(".money-input").val();
                var _name = $panel.find(".fastpay-username").val();
            }

            if (name_msg == "" && (_name == '' || _name == null || !_name)) {
                name_msg = "请填写您的存款姓名";
            }
            var myReg = /^[\u4e00-\u9fa5]+$/;
            if (name_msg == "" && !myReg.test(_name)) {
                name_msg = "只能填写汉字，不能输入其他字符。";
            }
            if (name_msg != "") {
                $parent.find(".fastpay-name-error-message").text(name_msg).removeClass("hidden");
            } else {
                $parent.find(".fastpay-name-error-message").text("").addClass("hidden");
            }

            if (money_msg == "" && (_money == '' || _money == null || !_money)) {
                money_msg = "请输入存款金额";
            }
            if (money_msg == "" && isNaN(_money)) {
                money_msg = "存款金额不得包含汉字!";
            }

            if (money_msg == "" && (_money < 1 || _money > 3000000)) {
                money_msg = "请输入1元-300万元之间的金额";
            }

            if (money_msg != "") {
                $parent.find(".money-error-message").text(money_msg).removeClass("hidden");
            } else {
                $parent.find(".money-error-message").text("").addClass("hidden");
            }

            if (name_msg == "" && money_msg == "") {

                layer.load(2, {shade: [0.7, '#000'], offset: ['46%', '45%']});
                $parent.find(".fastpay-next-btn").prop("disabled", true);

                if (_bankType == 7) {
                    // 超级转帐
                    if ($(".pay-list .item.on #deposit-zfbfast-page").hasClass("active")) {
                        _bankname = "ZFB_ABC";
                    } else if ($(".pay-list .item.on #deposit-wexinfast-page").hasClass("active")) {
                        _bankname = "WX_CCB";
                    } else if ($(".pay-list .item.on #deposit-fastpay-page").hasClass("active")) {
                        _bankname = "BANK_ICBC";
                    }
                }

                var formData = {
                    banktype: _bankType,
                    uaccountname: _name,
                    amount: _money
                };

                if (_bankType == 7) {
                    formData['ubankname'] = _bankname;
                }

                if (_bankType == 4 || _bankType == 6 || _bankType == 7 || _bankType == 22) {

                    var quotaAmount = $parent.find('.tab-panel.active .money-input').val().trim();

                    var quotaUrl = "";
                    if (_TEST_MODE == "1") {
                        quotaUrl = "/data/deposit/getWxZzQuota.json";
                    } else {
                        quotaUrl = "/asp/getWxZzQuota.aspx";
                    }

                    $.post(quotaUrl, {'amount': quotaAmount}, function (response) {
                        var amount = response;
                        if (amount > 0) {
                            $parent.find("#fastpay-real-money").val(amount);
                            $parent.find(".real-save-money, .save-money").text(amount);
                            _getAccount(formData);
                        } else {
                            _hideLayer();
                            _showLayer('请重新登录!');
                            $parent.find(".fastpay-next-btn").prop("disabled", false);
                        }
                    });

                } else {
                    _getAccount(formData);
                }
            }
        }

        function _getAccount(formData) {

            // 重新获取
            renewData = formData;

            var jsonData = "";

            var getNewdepositUrl = "";

            if (_TEST_MODE == 1) {
                getNewdepositUrl = "/data/deposit/newDeposit1.json";
            } else {
                getNewdepositUrl = "/asp/getNewdeposit.aspx";
            }

            jsonData = ajaxPost(getNewdepositUrl, formData);

            if (jsonData != "" && typeof jsonData != 'undefined') {


                var massage = jsonData.massage;
                if (!massage) {

                    _hideLayer();
                    _creatDepositInfo(jsonData);
                    _setDepositId(jsonData.depositId);

                } else {

                    _hideLayer();
                    _setDepositId(jsonData.depositId);

                    if (jsonData['force'] === true) {

                        layer.confirm('您上一笔订单未支付，可点击作废订单，系统将生成新的存款信息，按存款信息存款即可到账。', {
                            title: '温馨提示', //按钮
                            btn: ['作废上笔订单，生成新订单', '取消'], //按钮
                            offset: ['36%', '38%']
                        }, function () {
                            formData['force'] = true;
                            $.post(getNewdepositUrl, formData, function (response) {

                                _hideLayer();

                                if (!response.massage) {
                                    _creatDepositInfo(response);
                                } else {
                                    _showLayer(massage);
                                }

                            });
                        });
                    } else {
                        _showLayer(massage);
                    }
                }
            }

        }

        function _creatDepositInfo(response) {
            $(".fastpay-next-btn").hide().attr("disabled", "disabled");
            var massage = response.massage;
            if (massage == null || massage == '' || massage == undefined) {

                if (_bankType == 4 || _bankType == 6 || _bankType == 7) {
                    $(".deposit-fastpay-10").removeClass("hidden").siblings().addClass("hidden");
                } else {
                    $(".deposit-fastpay-2").removeClass("hidden").siblings().addClass("hidden");
                }

                _getInfo(response);
            } else {
                $(".deposit-fastpay-1").removeClass("hidden").siblings().addClass("hidden");
                _showLayer(massage);
            }
        }

        // 取得玩家填寫存款response信息
        function _getInfo(response) {

            var r = response;


            _fastData = {
                "username": $parent.find(".fastpay-username").val().trim(),
                "money": $parent.find(".money-input").val().trim(),
                "accountno": r.accountno,
                "depositId": r.depositId
            };

            // 支付宝 去转帐
            if (_bankType == 2) {
                $(".gozfb").show()
            } else {
                $(".gozfb").hide();
            }

            // 微信转帐 教程
            if (_bankType == 4) {
                $(".arrow-teach").show()
            } else {
                $(".arrow-teach").hide()
            }

            if (_bankType < 5) {
                $(".fastpay-renew-btn").show();
            } else {
                $(".fastpay-renew-btn").hide();
            }

            if (response.area == "" || typeof response.area == 'undefined') {
                $(".fastpay-area").hide();
            } else {
                if (response.amount > 49999) {
                    $(".fastpay-area").show();
                } else {
                    $(".fastpay-area").hide();
                }
            }

            _setDepositInfo(r);
        }

        function _setDepositInfo(r) {

            var _bankTypeCN = $parent.find(".pay-box.active").find('h4').text();

            $(".fastpay-confirm-money").html(r["amount"]);
            $(".fastpay-confirm-username").html(_fastData["username"]);
            $(".fastpay-confirm-type").html(_bankTypeCN);

            var accountno = addBlank(r.accountno);
            $(".sbankname").text(r.bankname);
            $(".saccountno").text(accountno);
            $(".saccountname").text(r.username);
            $(".mefuyan").text(r.depositId);
            $(".sacarea").text(r.area);

            $(".saccountno").parents().children(".copy1").attr("data-clipboard-text", r.accountno);
            $(".saccountname").parents().children(".copy1").attr("data-clipboard-text", r.username);
            $(".mefuyan").parents().children(".copy1").attr("data-clipboard-text", r.depositId);

            var clipboard = new Clipboard('.copy1');
            clipboard.on('success', function (e) {
                layer.open({
                    skin: 'top-class',
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: ['200px', '100px'],
                    time: 600,
                    content: '<p style="text-align: center; font-size:16px; padding:30px;">复制成功</p>',
                    success: function () {
                        $("body").addClass("layer-open");
                    }
                });
            });
        }


        function _setDepositId(id) {
            saveDepositId = id;
        }

        function _getDepositId() {
            return saveDepositId;
        }

        function _countDown(duration, display) {
            _stopTimer();
            display.text("10:00");

            var timer = duration,
                minutes, seconds;
            TIMER = setInterval(function () {
                minutes = parseInt(timer / 60, 10)
                seconds = parseInt(timer % 60, 10);

                minutes = minutes < 10 ? "0" + minutes : minutes;
                seconds = seconds < 10 ? "0" + seconds : seconds;

                if (minutes < 10 && seconds == 30) {
                    _chkMoneyArrived(_fastData["depositId"])
                }

                display.text(minutes + ":" + seconds);

                if (--timer < 0) {
                    _stopTimer();
                    _redirectFailPage();
                }
            }, 1000);
        }

        function _redirectSuccessPage() {
            _stopTimer();
            $parent.find(".deposit-fastpay-4").removeClass("hidden").siblings().addClass("hidden");
        }

        function _redirectFailPage() {
            _stopTimer();
            $parent.find(".j-count-table").addClass("hidden");
            $parent.find(".j-cs-table").removeClass("hidden");
        }

        function _stopTimer() {
            clearInterval(TIMER);
            TIMER = "";
        }

        function _chkMoneyArrived(depositId) {
            if (depositId == "") return false;
            $.post("/asp/pay_order.aspx", {"orderNo": depositId}, function (response) {
                var jsonData = $.parseJSON(response);
                if (jsonData.data) {
                    if (jsonData.data.status == "1") {
                        _redirectSuccessPage()
                    }
                }
            });
        }

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

    function _vpAlipayMoneyCheck(formData) {

        var result = "此通道仅支持5元、10元、20元、30元、40元、50元、60元、70元、80元、90元、100元、200元、300元、500元、600元、700元、800元、900元、1000元、1500元、2000元、3000元、5000元、8000元、10000元及15000元!";
        var vpArray = ["5", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100", "200", "300", "500", "600", "700", "800", "900", "1000", "1500", "2000", "3000", "5000", "8000", "10000", "15000"];

        var amount = formData.orderAmount;

        for (var i = 0; i < vpArray.length; i++) {

            if (vpArray[i] == amount) {
                result = "";
                return result;
            }
        }

        return result;
    }


    function _vpMoneyCheck(formData) {

        var result = "此通道仅支持10元、20元、30元、50元、100元及200元!";
        var vpArray = ["10", "20", "30", "50", "100", "200"];

        var amount = formData.orderAmount;

        for (var i = 0; i < vpArray.length; i++) {

            if (vpArray[i] == amount) {
                result = "";
                return result;
            }
        }

        return result;
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
        if (!formData.platformId) {
            return "[提示]支付方式不可为空";
        }
        if (!formData.loginName) {
            return "[提示]用户名不可为空";
        }
        return false;
    }

    return true;

}

/**
 *
 */

function getPayBank(id, target) {
    if (_TEST_MODE == 0) {
        var payWayUrl = "/asp/pay_bank.aspx";
    } else {
        var payWayUrl = "/data/deposit/payBank.json";
    }

    $.post(payWayUrl, {platformId: id}, function (result) {

        if (_TEST_MODE == 0) {
            result = $.parseJSON(result)
        }
        if (result.desc == '成功') {
            var str = '<option value="">请选择</option>';

            for (var i in result.data) {
                str += '<option value="' + result.data[i].dictValue + '">' + result.data[i].dictName + '</option>'
            }

            $(target).html(str);
        }
    });
}


//重置支付秒存
function resetPaymc() {
    $(".money-input").val("");
    $(".deposit-fastpay-1").removeClass("hidden").siblings().addClass("hidden");
    $(".fastpay-next-btn").show().attr("disabled", false);
    $(".fastpay-username, .money-input").val("");
    $(".money-pay").html("0元");
    $(".must-checked, .must-checked1").prop('checked', false);
    $(".btn-list input[type=button]").removeClass("active");
}

function _showLayer(msg, btn) {

    if (btn == "") {
        btn = '关闭';
    }

    layer.open({
        skin: 'top-class',
        content: msg,
        btn: btn
    });

    $("body").addClass("layer-open");
}

function _hideLayer() {
    layer.closeAll();
    $("body").removeClass("layer-open")
}

function addBlank(v) {
    var result = "";
    v = v.split("");
    for (var i = 0; i < v.length; i++) {
        if (i % 4 == 0) {
            result += " ";
        }
        result += v[i];
    }

    return result;
}

//验证输入input
function clearNoNum(_value) {
    // 事件中进行完整字符串检测
    var patt = /^((?!0)\d+\.[0-9][1-9])$/g;
    if (!patt.test(_value)) {
        // 错误提示相关代码，边框变红、气泡提示什么的
        // alert("输入金额格式错误");
        _showLayer("输入金额格式错误");
        return true;
    }
    return false;
}

function mykeypress(obj, e) {
    // 在 keyup 事件中拦截错误输入
    var keynum;
    if (window.event) { // IE
        keynum = e.keyCode;
    } else if (e.which) { // Netscape/Firefox/Opera
        keynum = e.which;
    }
    var sCharCode = String.fromCharCode(keynum);
    var sValue = obj.value;
    if (/[^0-9.]/g.test(sCharCode) || __getRegex(sCharCode).test(sValue)) {
        return false;
    }

    /**
     * 根据用户输入的字符获取相关的正则表达式
     * @param  {string} sCharCode 用户输入的字符，如 'a'，'1'，'.' 等等
     * @return {regexp} patt 正则表达式
     */
    function __getRegex(sCharCode) {
        var patt;
        if (/[0]/g.test(sCharCode)) {
            // 判断是否为空
            patt = /^$/g;
        } else if (/[.]/g.test(sCharCode)) {
            // 判断是否已经包含 . 字符或者为空
            patt = /((\.)|(^$))/g;
        } else if (/[0-9]/g.test(sCharCode)) {
            // 判断是否已经到达小数点后两位
            patt = /\.\d{2}$/g;
        }
        return patt;
    }
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

getStr = function (url) {
    if (!url) return false;
    return url.split("?")[1];
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

function jumpNext(e) {
    var $parent = $(e).parents(".item.on");

    if ($parent.find('.must-checked').prop('checked') && $parent.find('.must-checked1').prop('checked')) {
        $parent.find(".deposit-fastpay-2").removeClass("hidden").siblings().addClass("hidden");
    } else {
        _showLayer('请同意天威存款条例！');
    }
}


$(".diankatable").click(function () {
    $("#user_dianka").find(".one").hide();
    $(".diankabiao").show();
})

$("#deposit-dcard").click(function () {
    $("#user_dianka").find(".one").show();
    $(".diankabiao").hide();
})

$("#money_in >li").click(function () {
    $(".diankabiao").hide();
})

// $("#deposit-wexin").click(function () {
//     $("#deposit-wexinQR-subtitle").click()
// })

function getCsOnDuty() {
    return window.open("https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19");
}