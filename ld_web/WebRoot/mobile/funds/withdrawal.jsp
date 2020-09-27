<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
    <script type="text/javascript" src="mobile/js/ModeControl.js"></script>
</head>
<body>
<!--内容-->
<div class="withdrawal-content ui-form">


    <div class="ui-input-row">
        <label class="ui-label">游戏账户密码：</label>
        <input id="withdrawal-password" class="ui-ipt" type="password" required>

    </div>
    <div class="ui-input-row zf-sele">
        <label class="ui-label">取款银行：</label>
        <select id="withdrawal-bankName" onchange="BankOnChange(this)">
            <option value="">请选择</option>
        </select>

    </div>
    <div class="ui-input-row">
        <label class="ui-label">银行卡号：</label>
        <input id="withdrawal-cardno" class="ui-ipt" type="text" readonly>

        <div class="mui-btn mui-btn--raised mui-btn--danger small" onClick="mobileManage.getModel().open('bankBind')">
            绑定银行卡
        </div>
    </div>
    <div class="ui-input-row">
        <label class="ui-label">取款金额：</label>
        <input id="withdrawal-money" class="ui-ipt" type="text" placeholder="0.00" required>

    </div>
    <div class="ui-input-row zf-sele">
        <label class="ui-label">密保问题：</label>
        <div id="withdrawal-question"></div>

    </div>
    <div class="ui-input-row">
        <label class="ui-label">密保答案：</label>
        <input id="withdrawal-answer" class="ui-ipt" type="text" required>

    </div>
    <!-- <div class="mui-select">
            <div id="withdrawal-way"></div>
        <label>取款时间</label>
      </div> -->
    <div class="ui-button-row center">
        <div class="btn-login block" id="withdrawal-submit">确定提款</div>
    </div>
    <div class="space-2"></div>
    <div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-5 tishi">
        <div class="space-2"></div>
        <div class="h3"><strong>温馨提示：</strong></div>
        <ol>
            <li>
                金牌赌神以下提款100-50000金牌赌神及以上100-200000
            </li>
            <li>
                中午11:50 - 14:00 后台结算时间提款会顺延到 14:00 后审批出款，谢谢配合。
            </li>
        </ol>
        <div class="space-2"></div>
    </div>
</div>
<script type="text/javascript">
    function WithdrawalPage() {
        var that = this;
        //设定只能输入数字
        NumberInput('withdrawal-money');
        $.get('/asp/queryBankAll.aspx', function (data) {
            for (var i = 0; i < data.length; i++) {
                var $html = $("<option value='" + JSON.stringify(data[i]) + "'>" + data[i].bankname + "</option>")
                $("#withdrawal-bankName").append($html)
            }
        })
        that.questionCbo = new MobileComboBox({
            appendId: 'withdrawal-question',
            cls: '',
            valueName: 'value',
            displayName: 'name',
            datas: [
                {value: '1', name: '您最喜欢的明星名字？'},
                {value: '2', name: '您最喜欢的职业？'},
                {value: '3', name: '您最喜欢的城市名称？'},
                {value: '4', name: '对您影响最大的人名字是？'},
                {value: '5', name: '您就读的小学名称？'},
                {value: '6', name: '您最熟悉的童年好友名字是？'}
            ],
            onChange: function (e) {

            }
        });

        //提交
        $('#withdrawal-submit').click(function () {
            var money_ = $('#withdrawal-money').val();
            if (money_ < 100) {
                alert('提款金额不能低于100元');
                return;
            }
            var JSONBANK= JSON.parse( $("#withdrawal-bankName").val())
            var formData = {
                password: $('#withdrawal-password').val(),
                bankName: JSONBANK.bankname,
                cardNo: $('#withdrawal-cardno').val(),
                addr: 'none',
                money: $('#withdrawal-money').val(),
                //withdrawlWay:that.wayCbo.getValue(),
                questionId: that.questionCbo.getValue(),
                answer: $('#withdrawal-answer').val()
            };

            //提款
            mobileManage.getLoader().open('处理中');
            mobileManage.getBankManage().withdrawal(formData, function (result) {
                if (result.success) {
                    alert(result.message);
                    $('#withdrawal-money').val('');
                } else {
                    alert(result.message);
                }
                mobileManage.getLoader().close();
            });

        });
        //查询密保问题
        mobileManage.getUserManage().getQuestion(function (result) {
            var questionNames = {
                '1': true,
                '2': true,
                '3': true,
                '4': true,
                '5': true,
                '6': true,
            };
            if (result.success && questionNames[result.data.questionId]) {
// 					$('#question').val('已设置');
            } else {
                $('#question').val('尚未设置');
                $('#withdrawal-answer').parent().append('<div class="mui-btn mui-btn--raised mui-btn--primary small" ><a data-toggle="tab" href="#page-question"  class="set"  aria-expanded="false">设定密保</a></div>');
            }
        });
    }
    function BankOnChange(dom) {
        var jston = $(dom).val();
        if (!jston) return;
        jston = JSON.parse(jston);
        $('#withdrawal-cardno').val(jston.bankno);
    }
</script>
</body>
</html>