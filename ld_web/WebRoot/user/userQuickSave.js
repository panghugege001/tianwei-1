var TEST_MODE = 0;

$(function(){
    setQuickSave();
});

function setQuickSave(){

    var quickSaveData = new Object();

//  $('#quick-save-type').change(function () {
//      if ( $(this).val() == '2' ) {
//          $('.j-mcContent > dl').eq(3).addClass('hidden');
//          $('.j-mcContent > dl').eq(4).addClass('hidden');
//      }
//      else{
//          $('.j-mcContent > dl').eq(3).removeClass('hidden');
//          $('.j-mcContent > dl').eq(4).removeClass('hidden');
//      }
//  })

    $('.j-maiocunClose').on('click',function () {
        $('.j-maiocunTips').addClass('hidden');
    })

    $('.j-maiocunRemove').on('click',function () {
        save(true);
        $('.j-maiocunClose').eq(0).click();
    })

    /**
    * 秒存轉帳 查看歷史紀錄按鈕
    */
    $('#chkbankhistory').on('click',function () {
        $('.j-mcContent').addClass('hidden');
        $('.j-mcContentRecord').removeClass('hidden');
        showBankHistory();
    })

    $('.j-bank-history-back').on('click',function () {
        $('.j-mcContent').removeClass('hidden');
        $('.j-mcContentRecord').addClass('hidden');
        $('.j-bank-confirm').addClass('hidden');
    })

    /**
     * 秒存轉帳 下一步
     */
    $("#quick-save-next").click(function(e){
        e.preventDefault();

        var msg = "";
        var saveType = $("#quick-save-type").val();
        var saveMoney = $("#quick-save-money").val();
        var saveCard = $("#quick-save-card").val();
        var saveBank = $("#quick-save-bank").find(":selected").val();
        var saveName = $("#quick-save-username").val();

        if(msg == "" && !saveType || saveType == ""){
            msg = "[提示]请选择存款方式!";
        }

        if(msg == "" && !saveName || saveName == ""){
            msg = "[提示]请填写您的存款姓名!";
        }

        var reg = /^[1-9]\d*$/;
        if(isNaN(saveMoney)){
            msg = "[提示]存款金额不得包含汉字或符号!";
        }

        //如果存款方式是支付宝转账
        if(saveType == "2"){
            if(msg == "" && !saveMoney || saveMoney == ""){
                msg = "[提示]请选择存款金额!";
            }
//      } else {
//
//          if(msg == "" && !saveBank || saveBank == ""){
//              msg = "[提示]请选择存款银行!";
//          }
//
//          if(msg == "" && !saveCard || saveCard == ""){
//              msg = "[提示]请选择存款卡号!";
//          }
//
//          if(!reg.test(saveCard)){
//              msg = "[提示]存款卡号不得包含汉字或符号!";
//          }
//
//          if(msg == "" && saveCard.length < 16 || saveCard.length > 20){
//              msg = "[提示]请输入您的存款卡号（16-19位数）!";
//          }
        }

        if(msg == ""){
            //调用保存订单
            save(false);
        } else{
            alert(msg);
            return false;
        }
    });

    function save(is){
        var banktype     = $("#quick-save-type").val();
        var saveMoney    = $("#quick-save-money").val();
        var ubankname    = $("#quick-save-bank").val();
        var saveCard     = $("#quick-save-card").val();
        var uaccountname = $("#quick-save-username").val();

        $('.j-mefuyan').removeClass('hidden');
        $('.j-quick-confirm-bank').removeClass('hidden');
        $('.j-quick-confirm-card').removeClass('hidden');

        if(banktype == 2){
            ubankname = "支付宝";
            $('.j-mefuyan').addClass('hidden');
            $('.j-quick-confirm-bank').addClass('hidden');
            $('.j-quick-confirm-card').addClass('hidden');

            $('.j-mcTips').text('*请务必在转账时按照您的存款信息转账，存款将在1分钟之内添加到您的游戏账号');
        }
        else{
            $('.j-mcTips').text('*请务必在转账汇款的备注中填写此汇款附言，存款将在1分钟之内添加到您的游戏账号中');
        }
        
        if(ubankname == "请选择"){
        	ubankname = "";
        }

        var jsonData = "";
        if(TEST_MODE == 1) {
            jsonData = '{"username":"汤书兵","id":4241,"type":1,"bankname":"招商银行","remark":"汤书兵 TSB(CMB) deposit acc.南昌","useable":0,"amount":-200.0,"vpnname":"","vpnpassword":"","accountno":"6214837906643162","userrole":"01","isshow":1,"zfbImgCode":"7pgu1m","paytype":0}';
            jsonData = JSON.parse(jsonData);
            console.log(jsonData);
            save2(jsonData);
        }
        else
        {
            if (is) {
                jsonData = qsAjaxPost("/asp/getNewdeposit.aspx", {"banktype":banktype,"uaccountname":uaccountname,"ubankname":ubankname,"ubankno":saveCard,"amount":saveMoney,"force":true});
            }
            else{
                jsonData = qsAjaxPost("/asp/getNewdeposit.aspx", {"banktype":banktype,"uaccountname":uaccountname,"ubankname":ubankname,"ubankno":saveCard,"amount":saveMoney});
            }
        }
    }

    function save2(jsonData) {
        if (jsonData.force) {
            $('.j-maiocunTips').removeClass('hidden'); 
            return;
        }

        if(jsonData != ""  && typeof jsonData != 'undefined') {
            var Timer = "", num = 0;
            var massage = jsonData.massage;
            if (massage == null || massage == '' || massage == undefined) {
                getQuickSaveUserInfo();
                setQuickSaveUserInfo();

                $('.j-mcContent').addClass('hidden');
                $('.j-bank-confirm').removeClass('hidden');


                $("#sbankname").val(jsonData.bankname);

                $("#saccountno").val(jsonData.accountno);
                $("#saccountname").val(jsonData.username);
                $("#mefuyan").val(jsonData.depositId);

                $("#saccountno").parents().children(".copy1").attr("data-clipboard-text", jsonData.accountno);
                $("#saccountname").parents().children(".copy1").attr("data-clipboard-text", jsonData.username);
                $("#mefuyan").parents().children(".copy1").attr("data-clipboard-text", jsonData.depositId);


                var clip = new ZeroClipboard($('.j-bank-confirm').find('.copy'));
                clip.on('aftercopy', function (e) {
                    var val = $.trim(e.data['text/plain']);
                    if (val === '' || val === undefined) return;
                    var target = $(e.target);
                    alert('复制成功');
                });
            }
            else {
                alert(massage);
            }
        }
    }


    function showBankHistory(){
        openProgressBar();
        var jsonData = "";
        if(TEST_MODE == 1)
        {
            jsonData = '{"pageNumber":1,"totalPages":12,"size":10,"pageContents":[{"depositId":"esqrbd","loginname":"james","accountname":"汤书兵","bankname":"招商银行","bankno":"6214837906643162","status":0,"createtime":"Feb 26, 2017 12:00:00 AM","ubankname":"中国交通银行","uaccountname":"123","ubankno":"12345678901234567","amount":123.0,"flag":1,"type":"1"},{"depositId":"sxg7xazi","loginname":"james","accountname":"徐安江","bankname":"招商银行","bankno":"6214832708697649","status":2,"createtime":"Feb 24, 2017 12:00:00 AM","updatetime":"Feb 24, 2017 12:00:00 AM","ubankname":"中国工商银行","uaccountname":"尼玛","ubankno":"1556451546481434","amount":10.0,"flag":1,"type":"0"},{"depositId":"68j78a97","loginname":"james","accountname":"苏德喜","bankname":"招商银行","bankno":"6214832017301412","status":2,"createtime":"Feb 24, 2017 12:00:00 AM","updatetime":"Feb 24, 2017 12:00:00 AM","ubankname":"支付宝","uaccountname":"1","ubankno":"","amount":2.0,"flag":1,"type":"2"},{"depositId":"mywkv","loginname":"James","accountname":"冷祥富","bankname":"支付宝","bankno":"lengfengzhong6@sina.com","status":2,"createtime":"Aug 9, 2016 12:00:00 AM","updatetime":"Feb 24, 2017 12:00:00 AM","flag":1}],"statics1":0.0,"statics2":0.0,"totalRecords":112,"numberOfRecordsShown":4,"jsPageCode":"共112条,每页10条,当前1/12\u0026nbsp;首页\u0026nbsp;上一页\u0026nbsp;\u003ca href\u003d\u0027javascript:gopage(2)\u0027\u003e下一页\u003c/a\u003e\u0026nbsp;\u003ca href\u003d\u0027javascript:gopage(12)\u0027\u003e末页\u003c/a\u003e\u0026nbsp;到第\u003cselect name\u003d\u0027page\u0027 onchange\u003d\u0027javascript:gopage(this.options[this.selectedIndex].value)\u0027\u003e\u003coption value\u003d\u00271\u0027 selected\u003e1\u003c/option\u003e\u003coption value\u003d\u00272\u0027\u003e2\u003c/option\u003e\u003coption value\u003d\u00273\u0027\u003e3\u003c/option\u003e\u003coption value\u003d\u00274\u0027\u003e4\u003c/option\u003e\u003coption value\u003d\u00275\u0027\u003e5\u003c/option\u003e\u003coption value\u003d\u00276\u0027\u003e6\u003c/option\u003e\u003coption value\u003d\u00277\u0027\u003e7\u003c/option\u003e\u003coption value\u003d\u00278\u0027\u003e8\u003c/option\u003e\u003coption value\u003d\u00279\u0027\u003e9\u003c/option\u003e\u003coption value\u003d\u002710\u0027\u003e10\u003c/option\u003e\u003coption value\u003d\u002711\u0027\u003e11\u003c/option\u003e\u003coption value\u003d\u002712\u0027\u003e12\u003c/option\u003e\u003c/select\u003e页"}';
            jsonData = JSON.parse(jsonData);

            showBankHistory2(jsonData);
        }
        else
        {
            qsAjaxPost2("/asp/queryDepositBank.php", "");
        }
    }

    function showBankHistory2(jsonData) {
        if(jsonData != "" && typeof jsonData != 'undefined') {
            var pageContents = jsonData.pageContents;

            var html = "";
            if (pageContents.length > 0) {
                var pageLength = (pageContents.length > 5) ? 5 : pageContents.length;
                for (var i = 0; i < pageLength; i++) {

                    var type = (pageContents[i].type) ? pageContents[i].type : "";

                    var ubankname = (pageContents[i].ubankname) ? pageContents[i].ubankname : "";
                    var uaccountname = (pageContents[i].uaccountname) ? pageContents[i].uaccountname : "";
                    var ubankno = (pageContents[i].ubankno) ? pageContents[i].ubankno : "";

                    var loginname = (pageContents[i].loginname) ? pageContents[i].loginname : "";
                    var depositid = (pageContents[i].depositId) ? pageContents[i].depositId : "";

                    html += "<tr>";

                    html += "<td>" + (i + 1) + "</td>";
                    html += "<td>" + ubankname + "</td>";
                    html += "<td>" + uaccountname + "</td>";
                    html += "<td>" + ubankno + "</td>";
                    html += "<td>";
                    html += "<input type=\"button\" value=\"选中\" class=\"quick-save-choose-btn\" data-type='" + type + "' data-name='" + uaccountname + "' data-bank='" + ubankname + "' data-card='" + ubankno + "'>";
                    html += "<input type=\"button\" value=\"删除\" class=\"quick-save-delete-btn\" data-depositid='" + depositid + "' data-loginname='" + loginname + "' data-card='" + ubankno + "'>";
                    html += "</td>";

                    html += "</tr>";
                }

                $("#tbody").empty().append(html);
                closeProgressBar();
            } else {
                $("#bank-history table").hide();
            }

            setChooseBtn();
            setDeleteBtn();
        }
    }

    /**
     * 歷史銀行 刪除按鈕
     */
    function setDeleteBtn(){
        var $qsdelbtn = $(".quick-save-delete-btn");
        $qsdelbtn.click(function(){
            var $that = $(this);
            var $parent = $that.parents("tr");

            var loginname = $(this).data("loginname");
            var card      = $(this).data("card");
            var depositid = $(this).data("depositid");

            var jsonData = "";
            if(TEST_MODE == 1)
            {
                jsonData = "删除成功";

                if(jsonData != "" && typeof jsonData != "undefined"){
                    $parent.remove();
                }
            }
            else
            {
                qsAjaxPost3("/asp/updateDepositBank.php", {"loginname": loginname, "ubankno":card, "depositId" :depositid},$parent);
            }
        });
    }

    /**
     * 歷史銀行 選中按鈕
     */
    function setChooseBtn(){
        $(".quick-save-choose-btn").click(function(){

            var type = $(this).data("type");
            var name = $(this).data("name");
            var bank = $(this).data("bank");
            var card = $(this).data("card");
            $("#quick-save-card").val(card);

            if(type == "2"){
                $("#quick-save-card").val("");
                $('.j-mcContent dl').eq(3).addClass('hidden');
                $('.j-mcContent dl').eq(4).addClass('hidden');
            } else {
                $('.j-mcContent dl').eq(3).removeClass('hidden');
                $('.j-mcContent dl').eq(4).removeClass('hidden');
            }

            $("#quick-save-type").val(type);
            $("#quick-save-username").val(name);
            // 下拉式選單
            $("#quick-save-bank option").each(function(){
                var text = $(this).text();
                if(text == bank){
                    $(this).attr("selected","selected");
                }
            });

            $('.j-bank-history-back').click();
        });
    }


    
    /**
     * 秒存轉帳 取得玩家填寫存款信息
     */
    function getQuickSaveUserInfo(){
        quickSaveData = {
            "type" : $("#quick-save-type").val(),
            "type_cn" : $("#quick-save-type option:selected").text(),
            "username": $("#quick-save-username").val(),
            "bank": $("#quick-save-bank").val(),
            "card": $("#quick-save-card").val(),
            "money": $("#quick-save-money").val()
        };
    }

    /**
     * 秒存轉帳 配置玩家填寫存款信息
     */
    function setQuickSaveUserInfo(){
        $("#quick-confirm-type").val(quickSaveData["type_cn"]);
        if(quickSaveData["type_cn"].indexOf("银行") >=0){
            $("#ckyh").show();
            $("#ckkh").show();
            $("#fyan, #fyantip").show();
        }
        else{
            $("#ckyh").hide();
            $("#ckkh").hide();
            $("#fyan, #fyantip").hide();
        }
        $("#quick-confirm-money").val(quickSaveData["money"]);
        $("#quick-confirm-username").val(quickSaveData["username"]);
        $("#quick-confirm-bank").val(quickSaveData["bank"]);
        $("#quick-confirm-card").val(quickSaveData["card"]);
    }

    /**
     * 秒存轉帳 重置
     */
    function resetQuickSave(){
        $('#quick-save-username').val('');
        $('#quick-save-card').val('');
        $('#quick-save-money').val('');
    }

    /**
     * 秒存轉帳 我已完成付款 按鈕
     */
    $("#bank-success").click(function(){
        resetQuickSave();
        $('.j-bank-history-back').click();

    });


    function qsAjaxPost(url, parm){

        openProgressBar();
        var RESULT;

        $.ajax({
            url      : url,
            type     : "post",
            dataType : "json",
            data     : parm,
            async    : false,
            cache    : false,
            error: function (response) {
                alert(response);
            },
            success: function(jsonData) {
                RESULT = jsonData;
                closeProgressBar();
            },
            fail:function(){
                closeProgressBar();
                alert("[提示」维护中！");
            }
        });

        save2(RESULT);
    }

    function qsAjaxPost2(url, parm){

        openProgressBar();
        var RESULT;

        $.ajax({
            url      : url,
            type     : "post",
            dataType : "json",
            data     : parm,
            async    : false,
            cache    : false,
            error: function (response) {
                alert(response);
            },
            success: function(jsonData) {
                RESULT = jsonData;
                closeProgressBar();
            },
            fail:function(){
                closeProgressBar();
                alert("[提示」维护中！");
            }
        });

        showBankHistory2(RESULT);
    }

    function qsAjaxPost3(url, parm,obj){

        openProgressBar();
        var RESULT;

        $.ajax({
            url      : url,
            type     : "post",
            dataType : "json",
            data     : parm,
            async    : false,
            cache    : false,
            error: function (response) {
                alert(response);
            },
            success: function(jsonData) {
                RESULT = jsonData;
                closeProgressBar();
            },
            fail:function(){
                closeProgressBar();
                alert("[提示」维护中！");
            }
        });

        if(RESULT != "" && typeof RESULT != "undefined"){
            obj.remove();
        }
    }
}





//addis提交订单
// function submitOrder(){
//     //收款卡信息
//     var bankname = $("#sbankname").val();
//     var username = $("#saccountname").val();
//     //存款卡信息
//     var banktype = $("#quick-confirm-type").val();
//     var money = $("#quick-save-money").val();
//     var uaccountname = $("#quick-confirm-username").val();
//     var depositId = $("#fy").val();
//     $.ajax({
//         type: 'POST',
//         async: false,
//         url: "/asp/saveOrder.php",
//         data: {"bankname":bankname,"username":username,"amount":money,"depositId":depositId,"banktype":banktype,"uaccountname":uaccountname },
//         error: function (response) {
//             alert(response);
//         },
//         success: function (response) {
//             alert(response);
//         },
//         fail: function(){
//             alert("维护中");
//         }
//     });
// }



