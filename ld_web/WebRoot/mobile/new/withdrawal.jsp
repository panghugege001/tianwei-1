<%@page import="dfh.model.Users" %>
<%@ page import="dfh.utils.Constants" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%

    Users user = (Users) session.getAttribute(Constants.SESSION_CUSTOMERID);
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/mobile/new/index.jsp");
    } else if ("AGENT".equals(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/mobile/new/agent.jsp");
    }
%>
<!DOCTYPE >

<html>

<head>
    <title>天威</title>
    <jsp:include page="/mobile/commons/header.jsp">
        <jsp:param name="Title" value="我要提款"/>
    </jsp:include>
    <link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css"/>
</head>
<body>
<div class="balance-top"><img src="/mobile/img/icon/money.png" alt=""/>主账户(元)：
    <span class="c-ylow" id="credit">${session.customer.credit}</span>
    <i class="fr c-ylow iconfont icon-icon-refresh " onclick="refresh()"></i>
</div>
<div class="form-warp no-icon">
    <div class="form-tips">登录密码</div>
    <div class="form-group">
        <input id="withdrawal-password" class="form-control" type="password" required placeholder="游戏账户密码">
    </div>
    <div id='nobank' class="hidden">
        <div class="form-tips">提款银行
            <small>（每个账户可绑定三张银行卡）</small>
        </div>
        <div class="form-group">
            <input class="form-control" type="text" value="请先绑定银行卡" disabled="" readonly>
            <a href="/mobile/new/mybank.jsp" id='bind-bank' class="form-code">绑定卡号</a>
        </div>
    </div>
    <div id="donebank" class="hidden">
        <div class="form-tips">取款银行
            <small id="account-bankcard"></small>
        </div>
        <div class="form-group zf-sele">
            <select id="withdrawal-bankName" class="form-control">
                <option value="">请选择</option>
            </select>
        </div>

        <div class="form-tips">银行卡号</div>
        <div class="form-group">
            <input id="withdrawal-cardno" value="请选择银行卡" class="form-control" type="text" disabled="" readonly>
            <a id='bankBtn' href="/mobile/new/addbank.jsp" class="form-code">绑定银行卡</a>
        </div>
    </div>
    <div class="form-tips">取款金额</div>
    <div class="form-group">
        <input id="withdrawal-money" class="form-control" type="text" placeholder="0.00" required>
        <div id='all-money' class="form-code">全额取款</div>
    </div>
    <!--
                <div id="nosecret" class="hidden">
                    <div class="form-tips">密保问题</div>
                    <div class="form-group">
                        <input class="form-control" value="请先设定密保问题" type="text" disabled="" readonly>
                        <a href="/mobile/new/secret.jsp" id='bind-mibao' class="form-code">设定密保</a>
                    </div>
                </div> -->
    <div id="donesecret" class="hidden">
        <div class="form-group zf-sele" style="display: none;">
            <select id='withdrawal-question' class="form-control">
                <option value="">请选择</option>
            </select>

        </div>
        <div class="form-tips">提款密码</div>
        <div class="form-group">
            <input id="withdrawal-answer" placeholder="请输入提款密码" class="form-control" type="password" required>
            <div id='set-pas' class="form-code"><a href="/mobile/new/upPayPassword.jsp" style="display:block;width:100%;height:100%;">设置取款密码</a></div>
        </div>
    </div>
    <div class="btn-submit " id="withdrawal-submit">确定提款</div>
    <div class="text-tips">
        <div class="h3"><strong>温馨提示：</strong></div>
        <ol>
            <li>
                金牌赌神以下提款100-50000金牌赌神及以上100-200000
            </li>
            <li>
                中午11:50 - 14:00 后台结算时间提款会顺延到 14:00 后审批出款，谢谢配合。
            </li>
        </ol>
    </div>
    <br/>
</div>
<script type="text/javascript">
    // $(function () {
    //     WithdrawalPage();
    //     $("#all-money").click(function () {
    //         $(this).prev().val(parseFloat($("#credit").text()))
    //     })
    //     mobileManage.getUserManage().getQuestion(function (result) {
    //         var questionNames = {
    //             '1': true,
    //             '2': true,
    //             '3': true,
    //             '4': true,
    //             '5': true,
    //             '6': true
    //         };
    //         $("#donesecret").show()
    //     });
    //     if (!sessionStorage.getItem('withrawal')) {
    //         layer.open({
    //             content: '<div style="text-align:center"><h2 style="font-size:18px;font-weight: bold;">建议使用【久安钱包】进行提款采用区块链技术，资金更便利更安全，去中心化，不受监控绝对安全保密，久安钱包你的安全提款选择</h2><br><img width="150" src="/images/jiuan.jpg" /></div>',
    //             title: false,
    //             shadeClose: true,
    //             time: 0, //不自动关闭
    //             btn: ['了解久安', '我知道了'],
    //             yes: function (index) {
    //                 window.location = 'http://www.longdobbs.com/forum.php?mod=viewthread&tid=2732&extra=page%3D1'

    //             },
    //             no: function () {
    //                 layer.closeAll();
    //             }
    //         });
    //     }
    //     sessionStorage.setItem('withrawal', true)
    // })


    function WithdrawalPage() {
        var that = this;
        //设定只能输入数字
        NumberInput('withdrawal-money');
        //查询银行卡数量
        $.get('/asp/queryBankAll.aspx', function (data) {
            if (data.length > 0) {
                $('#account-bankcard').text('(已绑定' + data.length + '张银行卡)');
                $('#donebank').show()
                for (var i = 0; i < data.length; i++) {
                    var $html = $("<option value='" + data[i].bankname + "' data-json='" + JSON.stringify(data[i]) + "'>" + data[i].bankname + "</option>")
                    $("#withdrawal-bankName").append($html)
                }
                if (data.length >= 3) {
                    $("#bankBtn").hide()
                }
            } else {
                $('#nobank').show();
            }
            $("#withdrawal-bankName").change(function () {
                var jston = $('option:selected', this).data('json');
                if (!jston) return;
                $('#withdrawal-cardno').val(jston.bankno);
            })

        })
        var withdrawal_question = $("#withdrawal-question")
        $.each([{
            value: '1',
            name: '您最喜欢的明星名字？'
        },
            {
                value: '2',
                name: '您最喜欢的职业？'
            },
            {
                value: '3',
                name: '您最喜欢的城市名称？'
            },
            {
                value: '4',
                name: '对您影响最大的人名字是？'
            },
            {
                value: '5',
                name: '您就读的小学名称？'
            },
            {
                value: '6',
                name: '您最熟悉的童年好友名字是？'
            }
        ], function (i, item) {
            withdrawal_question.append('<option value="' + item.value + '">' + item.name + '</option>')
        });
        //提交
        $('#withdrawal-submit').click(function () {
            var money_ = $('#withdrawal-money').val();



            if ($("#donebank").is(":hidden")) {
                return alert('请先绑一样定银行卡');
            }

            if (money_ < 100) {
                alert('提款金额不能低于100元');
                return;
            }

            if($('#withdrawal-password').val() == ""){
                alert('请输入提款密码!');
                return;
            }

            var formData = {
                password: $('#withdrawal-password').val(),
                bankName: $("#withdrawal-bankName").val(),
                cardNo: $('#withdrawal-cardno').val(),
                addr: 'none',
                money: $('#withdrawal-money').val(),
                //withdrawlWay:that.wayCbo.getValue(),
                questionId: 7,
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
    }

    //重新整理余额
    function refresh() {
        $('.refre').addClass('credit-query');
        //先查询优发平台余额
        mobileManage.getUserManage().getCredit(
            function (result) {
                if (result.success) {
                    $("#credit").html(parseFloat(result.message));
                } else {
                    $("#credit").html('系统繁忙中');
                    alert(result.message);
                }
                $('.refre').removeClass('credit-query');
            }
        );
    }

    function BankOnChange(dom) {

    }
</script>
<jsp:include page="/mobile/commons/menu.jsp"/>
<script type="text/javascript" src="/mobile/app/js/layer/mobile/layer.js"></script>
<script>
    //信息框-例2
    if ('${session.customer.accountName}' == '') {
        layer.open({
            content: '请先完善个人信息!',
            btn: ['<span class="bule">确认</span>', '<span class="orange">取消</span>'],
            yes: function (index) {
                window.location.href = '/mobile/new/myaccount.jsp'
                layer.close(index);
            },
            no: function () {
                window.history.go(-1)
            }
        });
    }
</script>
</body>

</html>