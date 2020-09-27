<%@page import="java.util.GregorianCalendar" %>
<%@page import="java.util.Calendar" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dfh.model.Users" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/title.jsp"></jsp:include>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/user.css?v=6356"/>
    <script type="text/javascript" src="${ctx}/js/lib/layer/layer.js"></script>
</head>
<body class="user_body">

<div class="index-bg about-bj">
    <jsp:include page="${ctx}/tpl/header.jsp?v=1"></jsp:include>
    <div class="user_center"></div> 
    <div class="container w_357">
        <jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
        <div class="cfx about-main">
            <div class="gb-sidenav">
                <jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>
                <!--转账{-->
                <div id="tab_transfer" class="user-tab-box tab-panel">
					<div class="zhuanzhang">
	        			<ul>
	        				<li class="active">
	        					<a href="#tab-one" data-toggle="tab" aria-expanded="true">转账</a>
	        				</li>    					        					
	        			</ul>
	        		</div>
                    <form method="post" name="form1" class="ui-form">
                        <div class="ui-form-item">
                            <label class="ui-label" for="">来源账户：</label>
                            <select name="source_acc" id="transferGameOut" class="ui-ipt"
                                    onchange="transferMoneryOut(this.value);">
                                <option value="self" selected="selected">天威账户</option>
                                <option value="newpt">PT账户</option>
                                <option value="dt">DT账户</option>
                                <option value="mg">MG账户</option>
                                <option value="png">PNG账户</option>
                                <option value="agin">AG账户</option>
                                <option value="ttg">TTG账户</option>
                                <option value="qt">QT账户</option>
                                <option value="nt">NT账户</option>
                                <option value="qd">签到余额</option>
                                <option value="sba">沙巴体育账户</option>
                                <option value="ky">开元棋牌</option>
                                
                                <%--          <option value="n2live">N2Live账户</option>
                                              <option value="ebetapp">EBet真人账户</option>--%>
                            </select>
                            <span id="transferMoneryOutDiv" class="c-red">加载中..</span>
                        </div>
                        <div class="ui-form-item">
                            <label class="ui-label" for="">目标账户：</label>
                            <select name="target_acc" id="transferGameIn" class="ui-ipt"
                                    onchange="transferMoneryIn(this.value);">
                                <option value="newpt" selected>PT账户</option>
                                <option value="mg">MG账户</option>
                                <option value="png">PNG账户</option>
                                <option value="self">天威账户</option>
                                <option value="dt">DT账户</option>
                                <option value="agin">AG账户</option>
                                <option value="ttg">TTG账户</option>
                                <option value="qt">QT账户</option>
                                <option value="nt">NT账户</option>
                                <option value="qd">签到余额</option>
                                <option value="sba">沙巴体育账户</option>
                                <option value="ky">开元棋牌</option>

                                <%--            <option value="n2live">N2Live账户</option>
                                                <option value="ebetapp">EBet真人账户</option>--%>
                            </select>
                            <span id="transferMoneryInDiv" class="c-red">加载中..</span>
                        </div>
                        <div class="ui-form-item">
                            <label class="ui-label" for="">转账金额：</label>
                            <input id="transferGameMoney" class="ui-ipt" maxlength="10" type="text"/>
                        </div>
                        <div class="ui-form-item">
                            <input type="button" class="btn btn-pay" onclick="return transferMonery();" value="提交" style="margin:0"/>
							<input type="button" class="btn btn-pay" value="一键额度回归" onclick="onekeyMonery();" style="margin:0 0 0 26px">
							  
                        </div>
                    </form>
                    <div class="prompt-info">
                        <h3 class="tit">温馨提示：</h3>
                        <p>1.请在户内转账前进行平台激活方可转账成功。</p>
                        <p>2.登录PT游戏客户端时请加前缀K，例如，游戏账号为abc，请在登录PT游戏客户端时，需在账号处填写Kabc，方可正常登录进入游戏。</p>
                        <p>3.户内转账只支持整数转账</p>
                    </div>
                </div>
                <!--}转账-->
            </div>
 		</div>
 	</div>
</div>

<script>
 $(document).ready(function (){
    //获取游戏金额
        //初始化的时候 来源账户和目标账户的金额
        transferMoneryOut($("#transferGameOut").val());
        transferMoneryIn($("#transferGameIn").val());    
    function transferMoneryIn(gameCode) {
        if (gameCode != "") {
            $("#transferMoneryInDiv").html("加载中..");
            $.post("${ctx}/asp/getGameMoney.aspx", {
                "gameCode": gameCode
            }, function (returnedData, status) {
                if ("success" == status) {
                    $("#transferMoneryInDiv").html(returnedData);
                    //$('.j-balance').html(returnedData);
                    //updateBalance(gameCode,returnedData);
                }
            });
        }
    }

    //获取游戏金额
    function transferMoneryOut(gameCode) {
        if (gameCode != "") {
            $("#transferMoneryOutDiv").html("加载中..");
            $.post("${ctx}/asp/getGameMoney.aspx", {
                "gameCode": gameCode
            }, function (returnedData, status) {
                if ("success" == status) {
                    $("#transferMoneryOutDiv").html(returnedData);
                    //updateBalance(gameCode,returnedData);
                }
            });
        }
    }

    //更新余额
    function updateBalance(code, value) {
        if (code === 'e68') {
            $('.j-balance-total').html(value);
        } else if (code === $('#j-select-balance').val()) {
            $('#j-topBalance').html(value);
        }
    }

    //游戏转账
    function transferMonery() {
        var transferGameOut = $("#transferGameOut").val();
        /* if (transferGameOut == "") {
            alert("来源账号不能为空！");
            return false;
        } */
        var transferGameIn = $("#transferGameIn").val();
        /* if (transferGameIn == "") {
            alert("目标账号不能为空！");
            return false;
        } */
        var transferGameMoney = $("#transferGameMoney").val();
        /* if (transferGameMoney == "") {
            alert("转账金额不能为空！");
            return false;
        }
        if (isNaN(transferGameMoney)) {
            alert("转账金额只能是数字!");
            return false;
        }
        if (transferGameOut == "ld" && transferGameIn == "ld") {
            alert("天威账户不能转账到天威账户！");
            return false;
        }

        if (transferGameOut == "qd" && transferGameIn == "qd") {
            alert("签到账户不能转账到签到账户");
            return false;
        }
        if (transferGameOut == "qd" && transferGameIn == "ld") {
            alert("签到账户不能转账到主账户");
            return false;
        } */
        /* if(transferGameIn=='newpt' && parseInt(transferGameMoney)<20){
         alert('PT转入金额不能少于20元');
         return false;
         }*/
        //if (transferGameOut == "ld" || transferGameIn == "ld" || transferGameOut == "qd" || transferGameIn == "qd") {
            openProgressBar();
            $.post("${ctx}/asp/updateGameMoney.aspx", {
                "transferGameOut": transferGameOut,
                "transferGameIn": transferGameIn,
                "transferGameMoney": transferGameMoney
            }, function (returnedData, status) {
                if ("success" == status) {
                    transferMoneryOut(transferGameOut);
                    transferMoneryIn(transferGameIn);
                    closeProgressBar();
                    alert(returnedData);
                }
            });
        /* } else {
            alert("游戏之间不能对转！");
            return false;
        } */
    }
    function chageNav2(target) {
        var $nav = $('#j-userNav');
        if (target) {
            $nav.find('a[href="#' + target + '"]').trigger('click');
        }
    }
	function onekeyMonery() {
			var jsonData = ajaxPost("/asp/oneKeyGameMoney.aspx");
			if (jsonData == null || jsonData == "" || typeof jsonData == "undefined") {
				 
				alert("一键回归成功!");
				//window.location.reload();
				window.location.href = "/asp/payPage.aspx?showid=tab_transfer";
			}else {
				alert(jsonData);
			}
		}
		 	
 })
</script>
<script type='text/javascript' charset='utf-8'
        src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js'></script>

</body>
</html>