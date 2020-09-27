<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
if(user==null){
response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
}else if("AGENT".equals(user.getRole())){
response.sendRedirect(request.getContextPath()+"/mobile/agent.jsp");
}
%>
<!doctype html>
<html>
<head>
    <jsp:include page="commons/back.jsp"/>
    <link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=9"/>
    <script type="text/javascript" src="mobile/js/MobileGrid.js"></script>
    <script type="text/javascript" src="mobile/js/ModeControl.js"></script>
</head>
<body>
<style>
    .c-red {
        margin: 0px !important;
    }

    .nocss {
        background: none;
        border: none;
        outline: none;
    }
</style>
<div class="main-wrap">
    <div class="header-margin"></div>
    <div class="content">
        <div class="transfer-content ui-form">
            <div class="ui-input-row zf-sele mt2">
                <label class="ui-label">来源账户：</label>
                <select id="redRainOut" onchange="redRainMoneryOut(this.value);" class="input">
                    <option value="redrain" selected="selected"> 红包账户</option>
                    <option value="self">天威账户</option>
                </select>
            </div>
            <div class="ui-input-row black">
                <label class="ui-label">来源余额：</label>
                <div class="text">
                    <input id="redRainOutDiv" type="text" class="c-red nocss" disabled readonly
                           value="0.00">
                    </input>
                </div>
            </div>
            <!--<div class="change-button flaticon-exchange1" id="transfer-change"></div>-->
            <div class="ui-input-row zf-sele mt2">
                <label class="ui-label">目标账户：</label>
                <select id="redRainIn" onchange="redRainMoneryIn(this.value);" class="input">
                </select>
            </div>
            <div class="ui-input-row black">
                <label class="ui-label">目标余额：</label>
                <div class="text">
                    <input id="redRainInDiv" type="text" class="c-red nocss" disabled readonly
                           value="0.00">
                    </input>
                </div>
            </div>
            <div class="ui-input-row mt2">
                <label class="ui-label">转账金额：</label>
                <div class="text">
                    <input id="redRainMoney" class="ui-ipt" type="text" placeholder="请输入需要转账的金额">
                </div>
            </div>

            <div class="ui-button-row center">
                <div class="btn-login block" onclick="return redRainMonery();">确认</div>
            </div>
            <div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-5 tishi">
                <div class="space-2"></div>
                <div class="h3"><strong>温馨提示：</strong></div>
                <ol>
                    <li>存款红包一天可领取一次。</li>
                    <li>满10元可以转到任意游戏平台，1倍流水即可提款。</li>
                </ol>
                <div class="space-2"></div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    /****/
    //显示游戏金额
    $(function () {
        var redRainOut = $("#redRainOut").val();
        var redRainIn = $("#redRainIn").val();
        redRainMoneryOut(redRainOut);
        redRainMoneryIn(redRainIn);
    })

    //获取游戏金额
    function redRainMoneryOut(gameCode) {
        $("#redRainOutDiv").val('');
        $("#redRainInDiv").val('0.00');
        if (gameCode == "self") {
            $.post("/asp/getGameMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainOutDiv").val(returnedData);
                        $("#redRainIn").html('<option value="">请选择目标帐户</option>' + '<option value="redrain">红包账户</option>')
                    }
                });
        }
        else if (gameCode == "redrain") {
            $.post("/redrain/queryRedRainMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainOutDiv").val(returnedData);
                        $("#redRainIn").html(
                            '<option value="">请选择目标帐户</option>' +
                            '<option value="newpt"> PT账户</option>' +
                            '<option value="ttg"> TTG账户</option>' +
                            '<option value="slot"> 老虎机钱包(SW,MG,DT,PNG,QT,NT)</option>'
                        )
                    }
                });
        }
    }

    //获取游戏金额
    function redRainMoneryIn(gameCode) {
        $("#redRainInDiv").val('');
        if (gameCode == "redrain") {
            $.post("/redrain/queryRedRainMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainInDiv").val(returnedData);
                    }
                });
        }
        else {
            $.post("/asp/getGameMoney.aspx",
                {
                    "gameCode": gameCode
                }, function (returnedData, status) {
                    if ("success" == status) {
                        $("#redRainInDiv").val(returnedData);
                    }
                });
        }
    }

    //游戏转账
    function redRainMonery() {
        var redRainOut = $("#redRainOut").val();
        var redRainIn = $("#redRainIn").val();
        var redRainMoney = $("#redRainMoney").val();
        var rex = /^[0-9]+$/;
        if (redRainIn == "") {
            return alert("请选择目标账户!")
        }
        if (redRainOut && redRainIn && redRainMoney) {
            if (redRainMoney < 10) {
                alert("转帐金额不能少于10元!")
            }
            else if (!rex.test(redRainMoney)) {
                alert("转账金额只能是整数哦。");
            }
            else {
                if (redRainOut == 'redrain') {
                    $.post("/redrain/transferInforRedRain.aspx", {
                        "signType": redRainIn,
                        "redRainRemit": redRainMoney
                    }, function (returnedData, status) {
                        if ("success" == status) {
                            redRainMoneryOut(redRainOut);
                            redRainMoneryIn(redRainIn);
                            alert(returnedData);
                        }
                    })
                }
                else {
                    //主账户转红包雨账户
                    $.post("/asp/updateGameMoney.aspx", {
                        "transferGameIn": 'redrain',
                        "transferGameMoney": redRainMoney
                    }, function (returnedData, status) {
                        if ("success" == status) {
                            redRainMoneryOut(redRainOut);
                            redRainMoneryIn(redRainIn);
                            alert(returnedData);
                        }
                    })
                }
            }
            //红包账户转游戏平台

        } else {
            alert("请输入正确金额!")
        }
    }

</script>
<script type="text/javascript">
    headerBar.setTitle('红包雨');
</script>
</body>
</html>