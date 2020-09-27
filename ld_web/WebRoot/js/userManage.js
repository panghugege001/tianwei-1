var bankData={};
$(function(){
    //bankInfos();

    //获取绑定的支付宝帐号
    getAlipayAccount();
    //获取绑定的支付宝帐号
    getAlipayAccount1();

    var target=window.location.hash;
    changeMenu(target);

    // 顶部提款，取款，转账按钮
    $('#j-top-action,#j-user-action,#j-ct').find('a').on('click',function(){
        $('.j-userNavList > li').eq(0).addClass('active');
        changeMenu($(this).data('action'));
        return false;
    });

});
function changeMenu(target){
    $('#j-user-menu a[href="'+target+'"]').trigger('click');
}
//获取银行账号
function getbankno(bankname){
    if(bankname==''){
        return ;
    }
    openProgressBar();
    $.post("/asp/searchBankno.php", {
        "bankname":bankname
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            if(returnedData==1){
                $("#tkAccountNo").val("");
                $("#tkBankAddress").val("none");
            }else{
                var recvData=returnedData.split("|||");
                $("#tkAccountNo").val(recvData[0]);
                $("#tkBankAddress").val(recvData[1]);
            }
        }
    });
    /* if(bankname == "支付宝"){
     $(".j_questionflag").css("display","");
     }else{
     $(".j_questionflag").css("display","none");
     } */
}

//获取提款银行的状态
function getWithDrawBankStatus(bankname) {
	if(bankname==''){ 
		return;
	}
	openProgressBar(); 
	
	var status = "ERROR";
	$.ajax({
      type: "POST",
      async: false,
      url: "/asp/getWithDrawBankStatus.php",
      data: {"bankname":bankname},
      error: function (response) {
      	closeProgressBar();
          alert(response);
      },
      success: function (response) {
      	closeProgressBar();
      	status = response;
      }
  });
	return status;
}

//获取提款银行的账号信息
function getWithDrawBankNo(bankname) {
	if(bankname==''){ 
		return;
	}
	var status = getWithDrawBankStatus(bankname);
	if(status == "MAINTENANCE") {
		$("#tkAccountNo").val("");
        $("#tkBankAddress").val("");
		alert("银行系统维护中,请选择其他银行或稍后再试");
		return;
	}
	getbankno(bankname);
}

//提交提款确认
function tkConfirm(){
    var tkAgree=$("#tkAgree");
    if(!tkAgree.is(":checked")){
        alert("未选中提款须知！");
        return false;
    }
    var tkPassword=$("#tkPassword").val();
    if (tkPassword==''){
        alert("[提示]密码不可为空！");
        return false;
    }
    var tkBank=$("#tkBank").val();
    if(tkBank==""){
        alert("[提示]请选择银行！");
        return false;
    }
    var tkAccountNo=$("#tkAccountNo").val();
    if (tkAccountNo==''){
        alert("[提示]卡折号/支付宝不可为空！");
        return false;
    }
    if (tkAccountNo.length > 20){
        alert("[提示]卡折号/支付宝长度不能大于20！")
        return false;
    }
    var tkBankAddress=$("#tkBankAddress").val();
    if (tkBankAddress==""){
        alert("[提示]开户网点不可为空！");
        return false;
    }
    var tkAmount=$("#tkAmount").val();
    if (tkAmount==''){
        alert("[提示]提款金额不可为空！");
        return false;
    }
    if (tkAmount<1){
        alert("[提示]单次提款金额最低1元");
        return false;
    }
    if(isNaN(tkAmount)){
        alert("[提示]提款金额只能是数字!");
        return false;
    }
    if(!(/^(\+|-)?\d+$/.test(tkAmount))){
        alert("[提示]提款金额必须是整数!");
        return false;
    }
    var questionid = $("#mar_questionid").val();
    var answer = $("#mar_answer").val();
    if(answer == ''){
        alert("提款需要填写密保答案");
        return ;
    }
    var html="";
    html+='<table border="1" style="line-height: 30px;font-size:14px;width:95%;margin: 10px auto;">';
    html+='<tr>';
    html+='<td>账户姓名:'+$("#accountName").val()+'</td>';
    html+='</tr>';
    html+='<tr>';
    html+='<td>银行名称:'+tkBank+'</td>';
    html+='</tr>';
    html+='<tr>';
    html+='<td>银行账号:'+tkAccountNo+'</td>';
    html+='</tr>';
    html+='<tr>';
    html+='<td>金额:'+tkAmount+'</td>';
    html+='</tr>';
    html+='<tr>';
    html+='<td style="text-align:center;"><input class="btn" type="button" value="提交" onclick="return tkWithdrawal();" /></td>';
    html+='</tr>';
    html+='<tr>';
    html+='<td><font color="red">温馨提示:如您的注册姓名与您的收款账户姓名不一致,将导致提款失败!请您联系在线客服!</font></td>';
    html+='</tr>';
    html+='</table>';

    layer.open({
        title : [ '收款人资料', 'text-align:center;' ],
        type : 1,
        area : [ '600px', '360px' ],
        shadeClose : false, // 点击遮罩关闭
        content : html
    });

}

//提交提款
function tkWithdrawal(){
    var tkAgree=$("#tkAgree");
    var tkPassword=$("#tkPassword").val();
    var tkBank=$("#tkBank").val();
    var tkAccountNo=$("#tkAccountNo").val();
    var tkBankAddress=$("#tkBankAddress").val();
    var tkAmount=$("#tkAmount").val();
    var questionid = $("#mar_questionid").val();
    var answer = $("#mar_answer").val();
    openProgressBar();
    $.post("/asp/withdraw.php", {
        "password":tkPassword,
        "bank":tkBank,
        "accountNo":tkAccountNo,
        "bankAddress":tkBankAddress,
        "amount":tkAmount,
        //"msflag":tkMsflagradio,
        "questionid" : questionid,
        "answer":answer
    }, function (returnedData, status) {
        layer.closeAll();
        if ("success" == status) {
            if(returnedData=="SUCCESS"){
                alert("提款成功！");
                window.location.href="/userManage.php"
            }else if(returnedData == "bindingPlease"){
                alert("请绑定密保问题");
                window.location.href="/bindValidAnswer.jsp";
            }else{
                closeProgressBar();
                alert(returnedData);
            }
        }
    });
}
//重置提款
function clearTkWithdrawal(){
    $("#tkPassword").val("");
    $("#tkAccountNo").val("");
    $("#tkAmount").val("");
}
//绑定银行卡
function checkbandingform(){
    if(!window.confirm("确定吗？")){
        return false;
    }
    var bdbankno=$("#bdbankno").val();
    if(bdbankno==""){
        alert("[提示]卡/折号/支付宝不可为空！");
        return false;
    }
    // if(bdbankno.length>30||bdbankno.length<10){
    //     alert("[提示]卡/折号/支付宝长度只能在10-30位之间");
    //     return false;
    // }
    var bdbank=$("#bdbank").val();
    if(bdbank==""){
        alert("[提示]银行不能为空！");
        return false;
    }
    var bdpassword=$("#bdpassword").val();
    if(bdpassword==""){
        alert("[提示]登录密码不可以为空");
        return false;
    }
    var bindingCode = $("#bindingCode").val();
    openProgressBar();
    $.post("/asp/mainbandingBankno.php", {
        "password":bdpassword,
        "bankname":bdbank,
        "bankno":bdbankno,
        "bankaddress":"none",
        "bindingCode":bindingCode
    }, function (returnedData, status) {
        if ("success" == status) {
            if(returnedData=="SUCCESS"){
                alert("绑定成功！");
                window.location.href="/userManage.php"
            }else{
                closeProgressBar();
                alert(returnedData);
            }
        }
    });
}

//账户设置--卡折号绑定

//绑定银行卡
function checkbandingform1(){
    if(!window.confirm("确定吗？")){
        return false;
    }
    var bdbankno=$("#bdbankno1").val();
    if(bdbankno==""){
        alert("[提示]卡/折号不可为空！");
        return false;
    }
    // if(bdbankno.length>30||bdbankno.length<10){
    //     alert("[提示]卡/折号长度只能在10-30位之间");
    //     return false;
    // }
    var bdbank=$("#bdbank1").val();
    if(bdbank==""){
        alert("[提示]银行不能为空！");
        return false;
    }
    var bdpassword=$("#bdpassword1").val();
    if(bdpassword==""){
        alert("[提示]登录密码不可以为空");
        return false;
    }
    var bindingCode = $("#bindingCode1").val();
    openProgressBar();
    $.post("/asp/mainbandingBankno.php", {
        "password":bdpassword,
        "bankname":bdbank,
        "bankno":bdbankno,
        "bankaddress":"none",
        "bindingCode":bindingCode
    }, function (returnedData, status) {
        if ("success" == status) {
            if(returnedData=="SUCCESS"){
                alert("绑定成功！");
                window.location.href="/userManage.php"
            }else{
                closeProgressBar();
                alert(returnedData);
            }
        }
    });
}
//重置绑定
function clearBandingform(){
    $("#bdbankno").val("");
    $("#bdpassword").val("");
}
//显示游戏金额
function transferMoneryShow(){
    var transferGameOut=$("#transferGameOut").val();
    var transferGameIn=$("#transferGameIn").val();
    transferMoneryOut(transferGameOut);
    transferMoneryIn(transferGameIn);
}
//获取游戏金额
function transferMoneryOut(gameCode){
    if(gameCode!=""){
        $("#transferMoneryOutDiv").html("<img src='/images/20121212661146573498.gif'></img>");
        $.post("/asp/getGameMoney.php", {
            "gameCode":gameCode
        }, function (returnedData, status) {
            if ("success" == status) {
                $("#transferMoneryOutDiv").html(returnedData);
            }
        });
    }
}
//获取游戏金额
function transferMoneryIn(gameCode){
    if(gameCode!=""){
        $(".transferMoneryInDiv").html("<img src='/images/20121212661146573498.gif'></img>");
        $.post("/asp/getGameMoney.php", {
            "gameCode":gameCode
        }, function (returnedData, status) {
            if ("success" == status) {
                $(".transferMoneryInDiv").html(returnedData);
            }
        });
    }
}
//重置
function clearTransferMonery(){
    $("#transferGameMoney").val("");
}

function isNull(value) {

    if (null == value || "" === value || "" === $.trim(value)) {

        return true;
    }

    return false;
};

// 游戏转账
function transferMonery() {

    var transferGameOut = $("#transferGameOut").val();

    if (isNull(transferGameOut)) {

        alert("来源账户不能为空！");
        return false;
    }

    var transferGameIn = $("#transferGameIn").val();

    if (isNull(transferGameIn)) {

        alert("目标账户不能为空！");
        return false;
    }

    var transferGameMoney = $("#transferGameMoney").val();

    /*if(transferGameIn == 'newpt'){
     if (transferGameMoney < 20) {
     alert('PT转入金额最低20元！');
     return false;
     }
     }*/

    if (isNull(transferGameMoney)) {

        alert("转账金额不能为空！");
        return false;
    }

    if (isNaN(transferGameMoney)) {

        alert("转账金额只能是数字！");
        return false;
    }

    var reg = /^\+?[1-9][0-9]*$/;
    var result = reg.test(transferGameMoney);

    if (!result) {

        alert('转账金额必须为整数！');
        return false;
    }

    if (transferGameMoney < 0) {

        alert('转账金额必须大于0元！');
        return false;
    }

    if (transferGameOut == "ul" && transferGameIn == "ul") {

        alert("U乐账户不能转账到U乐账户！");
        return false;
    }

    if (transferGameOut == "ul" || transferGameIn == "ul") {

        openProgressBar();

        $.post("/asp/updateGameMoney.php", { "transferGameOut": transferGameOut, "transferGameIn": transferGameIn, "transferGameMoney": transferGameMoney }, function(returnedData, status) {

            if ("success" == status) {

                closeProgressBar();

                transferMoneryOut(transferGameOut);
                transferMoneryIn(transferGameIn);

                alert(returnedData);
            }
        });
    } else {

        alert("游戏账户不能进行转换操作！");
        return false;
    }
};

// 优惠劵
function submitCouponRemit(){
    var couponType=$("#couponType").val();
    var couponRemit=$("#couponRemit").val();
    var couponCode=$("#couponCode").val();
    if(couponType==""){
        alert("请选择平台！");
        return  false;
    }
    if(couponRemit!=""){
        if(isNaN(couponRemit)){
            alert("存款金额非有效数字！");
            return false;
        }
        /*  if(couponType!="ttg"){
         alert("只能是EA平台或者是PT平台才能使用存送优惠劵！");
         return false;
         } */
        if(couponRemit<100){
            alert("存款金额必须大于等于100！");
            return false;
        }
    }
    if(couponCode==""){
        alert("优惠代码不能为空！");
        return  false;
    }
    openProgressBar();
    $.post("/asp/transferInforCoupon.php", {
        "couponType":couponType,
        "couponRemit":couponRemit,
        "couponCode":couponCode
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData);
        }
    });
    return false;
}


//签到转账
function submitSignRemit(){
    var signType=$("#signType").val();
    var signRemit=$("#signRemit").val();
    if(signType==""){
        alert("请选择平台！");
        return  false;
    }
    if(signRemit!=""){
        if(isNaN(signRemit)){
            alert("存款金额非有效数字！");
            return false;
        }
        if(signRemit<10){
            alert("存款金额必须大于等于10！");
            return false;
        }
    }
    openProgressBar();
    $.post("/asp/transferInforSign.php", {
        "signType":signType,
        "signRemit":signRemit,
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData);
        }
    });
    return false;
}


//积分转账
function submitPointRemit(){
    var signRemit=$("#pointRemit").val();
    if(signRemit!=""){
        if(isNaN(signRemit)){
            alert("存款金额非有效数字！");
            return false;
        }
        if(signRemit<1){
            alert("存款金额必须大于等于1！");
            return false;
        }
    }
    openProgressBar();
    $.post("/asp/transferInforPoint.php", {
        "signRemit":signRemit,
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData);
        }
    });
    return false;
}

//在线支付记录
function payRecord(){
    openProgressBar();
    $.post("/asp/depositRecords.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#payRecordDiv").html("");
            $("#payRecordDiv").html(returnedData);
        }
    });
    return false;
}
//在线支付记录
function payRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/depositRecords.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#payRecordDiv").html("");
            $("#payRecordDiv").html(returnedData);
        }
    });
    return false;
}
//存款记录
function depositRecord(){
    openProgressBar();
    $.post("/asp/cashinRecords.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#depositRecordDiv").html("");
            $("#depositRecordDiv").html(returnedData);
        }
    });
    return false;
}
//存款记录
function depositRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/cashinRecords.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#depositRecordDiv").html("");
            $("#depositRecordDiv").html(returnedData);
        }
    });
    return false;
}


//玩家签到记录
function signOrderRecord(){
    openProgressBar();
    $.post("/asp/signOrderRecord.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#signOrderRecordDiv").html("");
            $("#signOrderRecordDiv").html(returnedData);
        }
    });
    return false;
}

//玩家签到记录
function signOrderRecordTwo(pageIndex){
    openProgressBar();
    if(pageIndex<1){
        pageIndex=1;
    }
    $.post("/asp/signOrderRecord.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#signOrderRecordDiv").html("");
            $("#signOrderRecordDiv").html(returnedData);
        }
    });
    return false;
}


//玩家积分记录
function querypointRecord(){
    openProgressBar();
    $.post("/asp/querypointRecord.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#pointRecordDiv").html("");
            $("#pointRecordDiv").html(returnedData);
        }
    });
    return false;
}

//玩家积分记录
function querypointRecordTwo(pageIndex){
    openProgressBar();
    if(pageIndex<1){
        pageIndex=1;
    }
    $.post("/asp/querypointRecord.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#pointRecordDiv").html("");
            $("#pointRecordDiv").html(returnedData);
        }
    });
    return false;
}



//存款附言记录
function depositOrderRecord(){
    openProgressBar();
    $.post("/asp/depositOrderRecord.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#depositOrderRecordDiv").html("");
            $("#depositOrderRecordDiv").html(returnedData);
        }
    });
    return false;
}

//存款附言记录
function depositOrderRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/depositOrderRecord.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#depositOrderRecordDiv").html("");
            $("#depositOrderRecordDiv").html(returnedData);
        }
    });
    return false;
}

//提款记录
function withdrawalRecord(){
    openProgressBar();
    $.post("/asp/withdrawRecords.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#withdrawalRecordDiv").html("");
            $("#withdrawalRecordDiv").html(returnedData);
        }
    });
    return false;
}
//提款记录
function withdrawalRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/withdrawRecords.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#withdrawalRecordDiv").html("");
            $("#withdrawalRecordDiv").html(returnedData);
        }
    });
    return false;
}
//转账记录
function transferRecord(){
    openProgressBar();
    $.post("/asp/transferRecords.php", {
        "starttime":$("#startTime1").val(),
        "endtime":$("#endTime1").val(),
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#transferRecordDiv").html("");
            $("#transferRecordDiv").html(returnedData);
        }
    });
    return false;
}
//转账记录
function transferRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/transferRecords.php", {
        "starttime":$("#startTime1").val(),
        "endtime":$("#endTime1").val(),
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#transferRecordDiv").html("");
            $("#transferRecordDiv").html(returnedData);
        }
    });
    return false;
}
//优惠记录
function offersferRecord(){
    openProgressBar();
    $.post("/asp/consRecords.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#offersRecordDiv").html("");
            $("#offersRecordDiv").html(returnedData);
        }
    });
    return false;
}
//优惠记录
function offersferRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/consRecords.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#offersRecordDiv").html("");
            $("#offersRecordDiv").html(returnedData);
        }
    });
    return false;
}
//游戏平台投注额
function queryBets(type){
    openProgressBar();
    $.post("/asp/queryBetOfPlatform.php", {
        "type":'month'
    }, function (returnedData, status) {
        closeProgressBar();
        if ("success" == status) {
            $("#monthbetsDivContent").html("");
            $("#monthbetsDivContent").html(returnedData);
        }
    });
    return false;
}
//处理升级
function checkUpgrade(type){
    openProgressBar();
    $.ajax({
        type: "post",
        url: "/asp/checkUpgrade.php",
        cache: false,
        data:{"type":type},
        success : function(data){
            alert(data);
        },
        error: function(){alert("系统错误");},
        complete: function(){closeProgressBar();}
    });
}
//周周回馈
function weekSentRecord(pageIndex){
    //选择老虎机窗口共用，区分周周回馈|负盈利反赠
    $(".j-hd-url").val("ws");

    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/queryWeekSentReccords.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#weekSentRecordDiv").html("");
            $("#weekSentRecordDiv").html(returnedData);
        }
    });
    return false;
}
//领取周周回馈
function getWeekSent(pno){
    rescueId.val(pno);
    modalRescue.modal('show');
}
//负盈利反赠记录
function losePromoRecord(){
    //选择老虎机窗口共用，区分周周回馈|负盈利反赠
    $(".j-hd-url").val("lp");

    openProgressBar();
    $.post("/asp/queryPTLosePromoReccords.php", {
        "pageIndex":1,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#losepromoRecordDiv").html("");
            $("#losepromoRecordDiv").html(returnedData);
        }
    });
    return false;
}
function losePromoRecordTwo(pageIndex){
    //选择老虎机窗口共用，区分周周回馈|负盈利反赠
    $(".j-hd-url").val("lp");

    if(pageIndex<=1){pageIndex=1;}
    openProgressBar();
    $.post("/asp/queryPTLosePromoReccords.php", {
        "pageIndex":pageIndex,
        "size":8
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#losepromoRecordDiv").html("");
            $("#losepromoRecordDiv").html(returnedData);
        }
    });
    return false;
}
//领取/取消负盈利反赠
function cancelLosePromo(pno){
    if(window.confirm('是否确定取消该笔返赠？')){
    }else{
        return false;
    }
    $.ajax({
        type: "post",
        url: "/asp/optLosePromo.php",
        cache: false,
        beforeSend:openProgressBar,
        data:{"jobPno":pno, "proposalFlag":-1},
        success : function(data){
            alert(data);
            losePromoRecord();
        },
        error: function(){("系统错误");},
        complete: function(){closeProgressBar();}
    });
}
//领取负盈利反赠
function getLosePromo(pno){
    rescueId.val(pno);
    modalRescue.modal('show');
}
//PT大爆炸
function queryPTBigBang(){
    openProgressBar();
    $.post("/asp/queryPTBigBang.php",
        function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#ptBigBangDiv").html("");
                $("#ptBigBangDiv").html(returnedData);
            }
        });
    return false;
}

//签到余额
function querysignMoney(){
    openProgressBar();
    $.post("/asp/querysignMoney.php",
        function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#qdmoney").html("");
                $("#qdmoney").html(returnedData+"元");
                $("#qdtime").html("");
                $("#qdtime").html(returnedData*2.5+"次");
            }
        });
    return false;
}



//积分余额
function queryPoint(){
    openProgressBar();
    $.post("/asp/queryPoints.php",
        function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#pointmoney").html("");
                $("#pointmoney").html(returnedData+"分");
            }
        });
    return false;
}

//领取PT大爆炸礼金
function getPTBigBangBonus(id){
    $.ajax({
        type: "post",
        url: "/asp/getPTBigBangBonus.php",
        cache: false,
        beforeSend:openProgressBar,
        data:{"ptBigBangId":id},
        success : function(data){
            alert(data);
            queryPTBigBang();
        },
        error: function(){("系统错误");},
        complete: function(){closeProgressBar();}
    });
}


//优惠券记录
function couponRecord(){
    openProgressBar();
    $.post("/asp/couponRecords.php", {
        "pageIndex":1,
        "size":5
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#couponRecordDiv").html("");
            $("#couponRecordDiv").html(returnedData);
        }
    });
    return false;
}
//优惠券记录
function couponRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/couponRecords.php", {
        "pageIndex":pageIndex,
        "size":5
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#couponRecordDiv").html("");
            $("#couponRecordDiv").html(returnedData);
        }
    });
    return false;
}
//自助反水优惠记录
function selfDefectionRecord(){
    openProgressBar();
    $.post("/asp/searchXima.php", {
        "pageno":1,
        "maxRowsno":4
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#selfDefectionRecordDiv").html("");
            $("#selfDefectionRecordDiv").html(returnedData);
        }
    });
    return false;
}
//自助反水优惠记录
function selfDefectionRecordTwo(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/searchXima.php", {
        "pageno":pageIndex,
        "maxRowsno":4
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#selfDefectionRecordDiv").html("");
            $("#selfDefectionRecordDiv").html(returnedData);
        }
    });
    return false;
}
////反水
//function getAutoXimaObject(){
//  var startTime=$("#startTime").val();
//  var platform=$("#platForm").val();
//  if(startTime==""){
//      return false;
//  }
//  var endTime=$("#endTime").val();
//  if(endTime==""){
//      return false;
//  }
//  openProgressBar();
//  $.post("/asp/getAutoXimaObjectData.php", {
//      "startTime":startTime,
//      "endTime":endTime,
//      "platform":platform
//  }, function (returnedData, status) {
//      if ("success" == status) {
//          closeProgressBar();
//          var ximaBean=returnedData;
//          var validAmount=ximaBean.validAmount;
//          var ximaAmount=ximaBean.ximaAmount;
//          var rate=ximaBean.rate;
//          var message=ximaBean.message;
//          if(message==null || message==""){
//              $("#validAmount").val(validAmount);
//              $("#ximaAmount").val(ximaAmount);
//              $("#rate").val(rate);
//          }else{
//              alert(message);
//          }
//      }
//  });
//  return false;
//}
////打开反水
//function getXimaEndTime(platform){
//  openProgressBar();
//  $("#validAmount").val("");
//  $("#rate").val("");
//  $("#ximaAmount").val("");
//  $("#endTime").val("");
//  $.post("/asp/getXimaEndTime.php", {"platform":platform
//  }, function (returnedData, status) {
//      if ("success" == status) {
//          closeProgressBar();
//          if(returnedData.indexOf("-")>=0){
//              $("#startTime").val(returnedData.split(",")[0]);
//              $("#endTime").val(returnedData.split(",")[1]);
//              getAutoXimaObject();
//          }else{
//              alert(returnedData);
//          }
//      }
//  });
//}
////提交反水
//var flag = true ;
//function checkEaSubmit(){
//  if(!window.confirm("确定吗？"))
//      return ;
//  var startTime=$("#startTime").val();
//  var endTime=$("#endTime").val();
//  var validAmount=$("#validAmount").val();
//  var rate=$("#rate").val();
//  var ximaAmount=$("#ximaAmount").val();
//  var platform=$("#platForm").val();
//  if(startTime==""||endTime==""||validAmount==""||rate==""||ximaAmount==""||platform==""){
//      alert("所有项都为必填项\n请重新选择[截止时间]，以让系统为您自动填写其他栏目");
//      return ;
//  }
//  openProgressBar();
//  $("#submit_fanshui").attr("disabled" ,true);
//  if(!flag){
//      return ;
//  }else{
//      flag = false ;
//  }
//  $.post("/asp/execXima.php", {
//      "startTime":startTime,
//      "endTime":endTime,
//      "validAmount":validAmount,
//      "rate":rate,
//      "ximaAmount":ximaAmount,
//      "platform":platform
//  }, function (returnedData, status) {
//      if ("success" == status) {
//          getXimaEndTime(platform);
//          closeProgressBar();
//          alert(returnedData);
//          $("#submit_fanshui").attr("disabled" ,false);
//          flag = true ;
//      }
//  });
//  return false;
//}


//短信服务
function smsServie(){
    var str="";
    $("input[name='service']:checkbox").each(function(){
        if($(this).attr("checked")){
            str += $(this).val()+","
        }
    })
    if(str.length<=0){
        alert("未选中数据！");
        return false;
    }
    openProgressBar();
    $.post("/asp/chooseservice.php", {
        "service":str
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData);
        }
    });
    return false;
}
//更新信息
function updateUser(){
    openProgressBar();
    var updateAliasName=$("#updateAliasName").val();
    var updateQq=$("#updateQq").val();
    var updateMailaddress=$("#updateMailaddress").val();
    percentageUpdate(updateAliasName,updateQq,updateMailaddress);
    $.post("/asp/change_info.php", {
        "aliasName":updateAliasName,
        "qq":updateQq,
        "mailaddress":updateMailaddress
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData);
        }
    });
    return false;
}
//更新密码
function updateDatePassword(){
    var updatePassword=$("#updatePassword").val();
    var updateNew_password=$("#updateNew_password").val();
    var updateSpass2=$("#updateSpass2").val();
    if (updatePassword==''){
        alert("[提示]用户旧密码不可为空！");
        return false;
    }
    if (updateNew_password==''){
        alert("[提示]用户新密码不可为空！");
        return false;
    }
    if (updateSpass2==''){
        alert("[提示]用户确认新密码不可为空！");
        return false;
    }
    if (updateSpass2 != "" && (updateSpass2 < 8 || updateSpass2 >12)){
        alert("[提示]密码的长度请介于8-12字符之间！")
        return false;
    }
    if (updateNew_password!=updateSpass2){
        alert("[提示]两次输入的密码不一致，请核对后重新输入！");
        return false;
    }
    openProgressBar();
    $.post("/asp/change_pws.php", {
        "password":updatePassword,
        "new_password":updateNew_password,
        "sPass2":updateSpass2
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData);
        }
    });
    return false;
}
//点击
function showOnclick(type){

    if(type=="0"){

        $('#one').css('display','block');
        $('#two').css('display','none');
        $('#three').css('display','none');
        $(".ol-yhj li:eq(0)").addClass("this");
        $(".ol-yhj li:eq(1)").removeClass("this");

    }else if(type=="1"){
        $('#one').css('display','none');
        $('#two').css('display','block');
        $('#three').css('display','none');
        $(".ol-yhj li:eq(0)").removeClass("this");
        $(".ol-yhj li:eq(1)").addClass("this");

    }
}
//邀请码
function checkTransferInEa(){
    openProgressBar();
    $.post("/asp/transferInEa.php", {
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            if(returnedData=="SUCCESS"){
                alert("获取成功！金额已转入EA游戏");
                window.location.href="/userManage.php";
            }else{
                alert(returnedData);
            }
        }
    });
}
/*获取银行帐号信息*/
function bankInfos(){
    $.ajax({
        type: "post",
        url: "/bankInfos.php",
        cache: false,
        async: false,
        success : function(data){
            var jsonData = eval(data);
            $.each(jsonData,function(index,obj){
                if(obj.bankname == "工商银行") {
                    bankData['icbc']=obj;
                }
                else if(obj.bankname == "招商银行"){
                    bankData['cmb']=obj;
                }
            });
            bankBtnInit();
            $('#j-bank-list').find('a:first').trigger('click');
        }
    });
}
function copyInit(){
    var clip = new ZeroClipboard($('#j-bank-info .j-btn-copy'));
    clip.on('aftercopy',function(e){
        var val= $.trim(e.data['text/plain']);
        if(val===''||val===undefined) return;
        var target=$(e.target);
        target.html('复制成功');
        setTimeout(function(){target.html('复制')},1000);
    });
}
/*动态获取银行信息详情*/
function bankBtnInit(bankurl){
    $('#j-bank-list a').on('click',function(){
        var user=$('#j-loginname').val();
        var $bankInfo=$('#j-bank-info').html('');
        var url=$(this).attr('data-url');
        $.get(url+'?v='+Math.random(),function(data){
            if(!data||!bankData) return;
            var tmpBank=url=='/tpl/bank/bank-icbc.html' ?bankData['icbc']:bankData['cmb'];
            if(tmpBank){
                var tmp=data.replace(/\{\{user\}\}/g,user)
                    .replace(/\{\{bankname\}\}/g,tmpBank.bankname)
                    .replace(/\{\{username\}\}/g,tmpBank.username)
                    .replace(/\{\{accountno\}\}/g,tmpBank.accountno)
                    .replace(/\{\{url\}\}/g,'/asp/depositValidateCode.php?r='+Math.random());
                $bankInfo.html(tmp);
                copyInit();
            }else{
                $bankInfo.html('<p class="c-red">未开启！</p>')
            }

        })
    });
}

var youHuiFlag = true ;
function checkSelfYouHuiSubmit(){
    var type = $("#youhuiType").val();
    var money = $("#transferMoney").val()
    if(type == "" || money == ""){
        alert("请选择或者输入");
        return  ;
    }
    if(!youHuiFlag){
        return ;
    }else{
        youHuiFlag = false ;
    }
    openProgressBar();
    var ioBB = $("#ioBB").val();
    $.post("/asp/getSelfYouHuiObject.php",{"youHuiType":type , "remit":money,"ioBB":ioBB},function(respData){
        alert(respData);
        closeProgressBar() ;
        youHuiFlag = true ;
    });
}

//获取优惠配置
var shouCunConfig ;
var ciCunCunConfig ;
var tiYanJinConfig ;

var eaCiCunConfig ;
var agCiCunConfig ;
var aginCiCunConfig ;
var ebetShouCunConfig ;
var ebetCiCunConfig ;

var shouCunConfigTTG ;
var ciCunCunConfigTTG ;

var FIRSTDEPOSITGPI;
var TWICEDEPOSITGPI;

var GPITIMELIMITCONPON;
var PTTIMELIMITCONPON;
var TTGTIMELIMITCONPON;

var NTFIRST;
var NTTWICE;
var NTSPEC;

function youHuiTypeChange(type){
    if(type == "590"){
        if(shouCunConfig == undefined){
            alert("PT首存未开始");
            return ;
        }
        $("#waterTimes").val(shouCunConfig.betMultiples);
    }else if(type == "591"){
        if(ciCunCunConfig == undefined){
            alert("PT次存未开始");
            return ;
        }
        $("#waterTimes").val(ciCunCunConfig.betMultiples);
    }else if(type == "592"){
        if(eaCiCunConfig == undefined){
            alert("EA次存未开始");
            return ;
        }
        $("#waterTimes").val(eaCiCunConfig.betMultiples);


    }else if(type == "593"){
        if(agCiCunConfig == undefined){
            alert("AG次存未开始");
            return ;
        }
        $("#waterTimes").val(agCiCunConfig.betMultiples);

    }else if(type == "594"){
        if(aginCiCunConfig == undefined){
            alert("AGIN次存未开始");
            return ;
        }
        $("#waterTimes").val(aginCiCunConfig.betMultiples);

    }else if(type == "596"){
        if(ebetShouCunConfig == undefined){
            alert("EBET首存未开始");
            return ;
        }
        $("#waterTimes").val(ebetShouCunConfig.betMultiples);

    }else if(type == "597"){
        if(ebetCiCunConfig == undefined){
            alert("EBET次存未开始");
            return ;
        }
        $("#waterTimes").val(ebetCiCunConfig.betMultiples);
    }else if(type == "598"){
        if(shouCunConfigTTG == undefined){
            alert("TTG首存未开始");
            return ;
        }
        $("#waterTimes").val(shouCunConfigTTG.betMultiples);
    }else if(type == "599"){
        if(ciCunCunConfigTTG == undefined){
            alert("TTG次存未开始");
            return ;
        }
        $("#waterTimes").val(ciCunCunConfigTTG.betMultiples);
    }else if(type == "702"){
        if(FIRSTDEPOSITGPI == undefined){
            alert("GPI首存未开始");
            return ;
        }
        $("#waterTimes").val(FIRSTDEPOSITGPI.betMultiples);
    }else if(type == "703"){
        if(TWICEDEPOSITGPI == undefined){
            alert("GPI次存未开始");
            return ;
        }
        $("#waterTimes").val(TWICEDEPOSITGPI.betMultiples);
    }else if(type == "704"){
        if(GPITIMELIMITCONPON == undefined){
            alert("GPI限时存送优惠未开始");
            return ;
        }
        $("#waterTimes").val(GPITIMELIMITCONPON.betMultiples);
    }else if(type == "705"){
        if(PTTIMELIMITCONPON == undefined){
            alert("PT限时存送优惠未开始");
            return ;
        }
        $("#waterTimes").val(PTTIMELIMITCONPON.betMultiples);
    }else if(type == "706"){
        if(TTGTIMELIMITCONPON == undefined){
            alert("TTG限时存送优惠未开始");
            return ;
        }
        $("#waterTimes").val(TTGTIMELIMITCONPON.betMultiples);
    }else if(type == "707"){
        if(NTFIRST == undefined){
            alert("NT首存未开始");
            return ;
        }
        $("#waterTimes").val(NTFIRST.betMultiples);
    }else if(type == "708"){
        if(NTTWICE == undefined){
            alert("NT次存未开始");
            return ;
        }
        $("#waterTimes").val(NTTWICE.betMultiples);
    }else if(type == "709"){
        if(NTSPEC == undefined){
            alert("NT限时存送优惠未开始");
            return ;
        }
        $("#waterTimes").val(NTSPEC.betMultiples);
    }
    getSelfYouhuiAmount($("#transferMoney").val()) ;
}

function getSelfYouhuiAmount(value){
    var type = $("#youhuiType").val();
    var money  ;
    if(type == "590"){
        money = (value*(shouCunConfig.percent)>(shouCunConfig.limitMoney))?(shouCunConfig.limitMoney):value*(shouCunConfig.percent) ;
    }else if(type == "591"){
        money = (value*(ciCunCunConfig.percent)>(ciCunCunConfig.limitMoney))?(ciCunCunConfig.limitMoney):value*(ciCunCunConfig.percent) ;
    }else if(type == "592"){
        money = (value*(eaCiCunConfig.percent)>(eaCiCunConfig.limitMoney))?(eaCiCunConfig.limitMoney):value*(eaCiCunConfig.percent) ;
    }else if(type == "593"){
        money = (value*(agCiCunConfig.percent)>(agCiCunConfig.limitMoney))?(agCiCunConfig.limitMoney):value*(agCiCunConfig.percent) ;
    }else if(type == "594"){
        money = (value*(aginCiCunConfig.percent)>(aginCiCunConfig.limitMoney))?(aginCiCunConfig.limitMoney):value*(aginCiCunConfig.percent) ;
    }else if(type == "596"){
        money = (value*(ebetShouCunConfig.percent)>(ebetShouCunConfig.limitMoney))?(ebetShouCunConfig.limitMoney):value*(ebetShouCunConfig.percent) ;
    }else if(type == "597"){
        money = (value*(ebetCiCunConfig.percent)>(ebetCiCunConfig.limitMoney))?(ebetCiCunConfig.limitMoney):value*(ebetCiCunConfig.percent) ;
    }else if(type == "598"){
        money = (value*(shouCunConfigTTG.percent)>(shouCunConfigTTG.limitMoney))?(shouCunConfigTTG.limitMoney):value*(shouCunConfigTTG.percent) ;
    }else if(type == "599"){
        money = (value*(ciCunCunConfigTTG.percent)>(ciCunCunConfigTTG.limitMoney))?(ciCunCunConfigTTG.limitMoney):value*(ciCunCunConfigTTG.percent) ;
    }else if(type == "702"){
        money = (value*(FIRSTDEPOSITGPI.percent)>(FIRSTDEPOSITGPI.limitMoney))?(FIRSTDEPOSITGPI.limitMoney):value*(FIRSTDEPOSITGPI.percent) ;
    }else if(type == "703"){
        money = (value*(TWICEDEPOSITGPI.percent)>(TWICEDEPOSITGPI.limitMoney))?(TWICEDEPOSITGPI.limitMoney):value*(TWICEDEPOSITGPI.percent) ;
    }else if(type == "704"){
        money = (value*(GPITIMELIMITCONPON.percent)>(GPITIMELIMITCONPON.limitMoney))?(GPITIMELIMITCONPON.limitMoney):value*(GPITIMELIMITCONPON.percent);
    }else if(type == "705"){
        money = (value*(PTTIMELIMITCONPON.percent)>(PTTIMELIMITCONPON.limitMoney))?(PTTIMELIMITCONPON.limitMoney):value*(PTTIMELIMITCONPON.percent);
    }else if(type == "706"){
        money = (value*(TTGTIMELIMITCONPON.percent)>(TTGTIMELIMITCONPON.limitMoney))?(TTGTIMELIMITCONPON.limitMoney):value*(TTGTIMELIMITCONPON.percent);
    }else if(type == "707"){
        money = (value*(NTFIRST.percent)>(NTFIRST.limitMoney))?(NTFIRST.limitMoney):value*(NTFIRST.percent);
    }else if(type == "708"){
        money = (value*(NTTWICE.percent)>(NTTWICE.limitMoney))?(NTTWICE.limitMoney):value*(NTTWICE.percent);
    }else if(type == "709"){
        money = (value*(NTSPEC.percent)>(NTSPEC.limitMoney))?(NTSPEC.limitMoney):value*(NTSPEC.percent);
    }
    $("#giftMoney").val(money.toFixed(2));
}



/*获取备注**/
function getRemarkAgain(obj,bankname ){
    //入参 {"bankname":bank,"depositCode":code,"username":username}
    if(confirm("确定重新生成附言？")){
        $.post("/asp/getRemarkAgain.php",$('#j-bank-info').serialize(),function(respData){
            if(respData.length == 5){
                alert("存款订单已生成，请用新的附言进行付款");
                $(obj).siblings('.j-remark').html(respData).siblings('.j-btn-remark').attr('data-clipboard-text',respData);
            }else{
                alert(respData);
            }
            $('.deposit-code').attr('src','/asp/depositValidateCode.php?r='+Math.random());
        });
    }
    return false;
}
var ifvalidateAmountDeposit;
function validateDepositBankCardInfo(){
    $.ajax({
        type: "post",
        url: "/asp/getValidateDepositBankInfo.php",
        cache: false,
        async: false,
        success : function(data){
            if(data!=null){
                $("#validateAmountDepositBankInfoTable").append("<tr><td style='width:100px;'><span>账户名：</span></td><td  style='width:180px;'><span style='color:red;'>" + data.username + "</td><td style='width:100px;'><span>开户行：</span></td><td style='width:180px;'><span style='color:red;'>" + data.bankname + "</span></td></tr>");
                $("#validateAmountDepositBankInfoTable").append("<tr><td><span>帐号：</span></td><td><span style='color:red;'>" + data.accountno + "</td><td><span>存款验证QQ：</span></td><td><span style='color:red;'>800112058</span></td></tr>");
                ifvalidateAmountDeposit = true;
            }else{
                ifvalidateAmountDeposit = false;
            }
        },
        error: function(){
            ifvalidateAmountDeposit = false;
        }
    });
}
function clearNoNum(obj){obj.value = obj.value.replace(/\D|^0/g,"");}
function createValidatedPayOrder(){
    var depositAmount = $("#depositAmountInput").val();
    if(depositAmount=='' || $.trim(depositAmount)==''){return;}
    if(depositAmount < 1){alert('最低存款1元！');return;}
    openProgressBar();
    $("#validateAmountDepositBankInfoTable").empty();
    validateDepositBankCardInfo();
    if(ifvalidateAmountDeposit){
        $.post("/asp/createValidatedPayOrder.php", {amount:depositAmount}, function(data){
            if(data.code=='1'){
                //displayDiv(7);
                $('#validatedAmountInfo').text('我们能够为您处理的金额为：'+data.amount+'RMB。 请确保存入该指定金额，否则会导致存款无法到帐。');
            }else{
                alert(data.msg);
            }
        }).fail(function(){
            alert("生成订单失败");
        }).always(function() {
            closeProgressBar();
        });
    }else{
        closeProgressBar();
        alert("当前系统无法处理额度验证存款，对此给您带来的不便我们深表歉意");
    }
}
//在线支付
var p=0;
function addHF(){
    if(p<1){
        $.ajax({
            url:"/thirdPartyLoad2.php",
            type:"post",
            dataType:"text",
            data:"",
            async:true,
            success:function(msg){
                var errorInfo1=$('#errorInfo').val();
                if(null!=errorInfo1&&errorInfo1.length>0&&msg.length>0){
                    /* $('#errorInfoA').html(''); */
                }
                $("#addhf").after(msg);
            }
        });
    }
    p++;
}

//保存支付宝绑定帐号
function saveAlipayAccount(){
    var ac = $(".j-zfb-msg").data("ac");
    var str = "",
        $account = $("#j-zfb-account"),
        $password = $("#j-zfb-password");
    if(!$account.val()){
        alert('支付宝帐号不能为空,请重新输入！');
        return false;
    }

    if(!$password.val()){
        alert('密码不能为空,请重新输入！');
        return false;
    }
    if(/.*[\u4e00-\u9fa5]+.*$/.test($account.val())){
        alert('支付宝帐号不能有中文,请重新输入！');
        return false;
    }

    if(ac == "1") {
        str = "请您确认是否更换支付宝帐号";
    } else {
        str = "请您再次确认支付宝帐号是否正确";
    }

    if(confirm(str)) {
        openProgressBar();
        $.post("/asp/saveAlipayAccount.php",{"account":$account.val(),"password":$password.val()},function(data){
            getAlipayAccount();
            alert(data);
            closeProgressBar();
            $account.val('');
            $password.val('');
            if(data==='更新成功'){
                $('#j-modal-zfb').modal('hide');
            }
        });
    }
}

function saveAlipayAccount1(){
    openProgressBar();
    var account = $("#id_zfb_account").val();
    var password = $("#id_zfb_password").val();
    $.post("/asp/saveAlipayAccount.php",{"account":account,"password":password},function(data){
        getAlipayAccount();
        alert(data);
        closeProgressBar();
    });
}

function getAlipayAccount1(){
    $.post("/asp/getAlipayAccount.php",function(data){
        if(null != data){
            $("#id_zfb_msg").html("您目前绑定的存款账号是："+data.alipayAccount);
        }else{
            $("#id_zfb_msg").html("请绑定您的支付宝存款账号");
        }
    });
}

//获取支付宝绑定的帐号
function getAlipayAccount(){
    var zfbMsg=$('.j-zfb-msg'),
        zfbMsg2=$('.j-zfb-msg2'),
        account=zfbMsg.find('.j-account-ret'),
        zfbAction=$('#j-zfb-action');
    $.post("/asp/getAlipayAccount.php",function(data){
        //console.log(data);
        if(data==='empty'){
            zfbMsg.data("ac", "0");
            zfbMsg.html("请先绑定您的支付宝存款账号：  "+'<a href="javascript:;" class="btn btn-danger" data-toggle="modal" data-target="#j-modal-zfb">绑定</a>');
            zfbMsg2.html('请先绑定您的支付宝存款账号');
            zfbAction.on('click',modalZfb);

        }else if(data&& data!=='empty'){
            var alipayAccount = retAlipayAccount(data.alipayAccount);

            zfbMsg.data("ac", "1");
            zfbMsg.html('您目前绑定的存款账号是：'+alipayAccount+'&nbsp;&nbsp;<a href="javascript:;" class="btn btn-danger" data-toggle="modal" data-target="#j-modal-zfb">修改绑定</a>');
            zfbMsg2.html('您目前绑定的存款账号是：'+alipayAccount);
            zfbAction.off('click');
        }
    });
}

// 星号显示支付宝帐号
function retAlipayAccount(alipayAccount){
    if(!alipayAccount) return;
    var length = alipayAccount.length;
    var str = "";
    for(var i = 0; i < length; i++) {
        if(i < (length/2)){
            str += alipayAccount[i];
        }else{
            str += "*";
        }
    }
    return str;
}
function modalZfb(){
    alert('请先绑定支付宝帐号');
    $('#j-modal-zfb').modal('show');
    return false;
}

$(function(){
    bankInfos();
});

//催账记录
function urgeOrderRecord(pageIndex){
    if(pageIndex<=1){
        pageIndex=1;
    }
    openProgressBar();
    $.post("/asp/queryUrgeOrderPage.php", {
        "pageIndex":pageIndex,
        "size":5
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#czDiv").html("");
            $("#czDiv").html(returnedData);
        }
    });
    return false;
}

