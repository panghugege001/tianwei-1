
$(function(){
    var tmp={};
    $("#selectpay").hide();
    $("#card").hide();

    $('#j-pay-way').find('a').on('click',function () {
        var target=$(this).attr('href');
        if(target){
            target=target.replace('#', '');
        }
        switch (target){
            case 'tab_pay_bank2':
                $('#form-payBank2').find(':reset').trigger('click');
                break;
            default:
                break;
        }

    });

    tmp['tab_pay_bank2']=new setQuickSave();

});

function setQuickSave(){
    $("#selectpay").hide();
    $("#card").hide();
    $(".show-for-none").hide();//默认
    $(".show-for-alipay").show();//默认xianshi支付宝的提示信息
    $(".hide-for-alipay").hide();
    var that=this;

    this.$nextBtn=$('#quick-save-next');
    this.$hisitoryBtn=$('#chkbankhistory');
    this.$bankSuccessBtn=$('#bank-success');

    this.$formIndex=$('#tab_bank_index');
    this.$formConfirm=$('#tab_bank_next');
    this.$formHistory=$('#tab_bank_hisotry');

    this.formData = {};

    //存款方式选择
    this.$quickSaveType=$('#quick-save-type');
    //您的存款姓名
    this.$qSaveUserName = $("#quick-save-username");
    //选择存款银行
    //this.$qSaveBank= $("#quick-save-bank");
    //选择存款卡号
    //this.$qSaveCard = $("#quick-save-card");
    //存款金额
    this.$qSaveMoney = $("#quick-save-money");


   this.init=function(){
       var $card=$("#card");
       var $selectpay=$("#selectpay");
       that.$quickSaveType.change(function(){
           var value = $(this).val();
           if(value == "2"){
               that.$qSaveCard.val("");

               $card.hide();
               $selectpay.hide();
           } else {
              //that.$qSaveCard.attr("disabled", false);
               //that.$qSaveBank.attr("disabled", false);

               $card.show();
               $selectpay.show();
           }
       });

       that.$bankSuccessBtn.on('click',that.resetQuickSave);
       that.$hisitoryBtn.on('click',that.showBankHistory);

       that.resetQuickSave();
   };



    /**
     * 获取表单数据
     */
    this.getFormData=function(){
        return {
            //存款方式
            banktype    : that.$quickSaveType.val(),
            //您的存款姓名
            uaccountname: that.$qSaveUserName.val(),
            //选择存款银行
            //ubankname    : that.$qSaveBank.val(),
            //选择存款卡号
            //ubankno    : that.$qSaveCard.val(),
            //存款金额
            amount   : that.$qSaveMoney.val()
        };
    };

    this.resetQuickSave=function(){
        $("#quick-save input[type=text], #quick-save select").show().val("");

        that.setActivePage(that.$formIndex);
    };

    /**
     * 检测表单
     * @param formData
     * @returns {string}
     */
    this.checkForm=function(formData){
        var msg='';

        if(!formData.banktype){
            msg = "[提示]请选择存款方式!";
            alert(msg);
            return false;
        }

        if(!formData.uaccountname){
            msg = "[提示]请填写您的存款姓名!";
            alert(msg);
            return false;
        }

        var reg = /^[1-9]\d*$/;
        if(!reg.test(formData.amount)){
            msg = "[提示]存款金额不得包含汉字或符号!";
            alert(msg);
            return false;
        }

        //如果存款方式是支付宝转账
        if(formData.banktype == "2"){
            if(!formData.amount){
                msg = "[提示]请输入存款金额!";
                alert(msg);
                return false;
            }
        } else {

          /*  if(!formData.ubankname){
                msg = "[提示]请选择存款银行!";
            }

            if(!formData.ubankno){
                msg = "[提示]请选择存款卡号!";
            }

            if(!reg.test(formData.ubankno)){
                msg = "[提示]存款卡号不得包含汉字或符号!";
            }

            if(formData.ubankno.length < 16 || formData.ubankno.length > 20){
                msg = "[提示]存款卡号长度错误!";
            }*/
        }

        return msg;
    };

    /**
     * 设置tab 激活页面
     * @param $ele
     * @returns {boolean}
     */
    this.setActivePage=function($ele){
        if(!$ele) return false;

        $ele.addClass('active').siblings().removeClass('active');
    };



    /*
     秒存转帐 下一步
     */
    this.$nextBtn.click(function(e){
        e.preventDefault();
        // 晚上11-凌晨1.提示招商银行
        var objDate=new Date();
        var nowTime=objDate.getHours();

        that.formData=that.getFormData();
        var formData=that.formData;
        var msg='';
        if(!formData.banktype){
            msg = "[提示]请选择存款方式!";
            alert(msg);
            return false;
        }

        if(!formData.uaccountname){
            msg = "[提示]请填写您的存款姓名!";
            alert(msg);
            return false;
        }

        var reg = /^[1-9]\d*$/;
        if (isNaN(formData.amount)) {
            msg = "[提示]存款金额不得包含汉字!";
            alert(msg);
            return false;
        }



        //如果存款方式是支付宝转账
        if(formData.banktype == "2"){
            if(!formData.amount){
                msg = "[提示]请输入存款金额!";
                alert(msg);
                return false;
            }
        } else {

            /*if(!formData.ubankname){
                msg = "[提示]请选择存款银行!";
                alert(msg);
                return false;
            }

            if(!formData.ubankno){
                msg = "[提示]请选择存款卡号!";
                alert(msg);
                return false;
            }

            if(!reg.test(formData.ubankno)){
                msg = "[提示]存款卡号不得包含汉字或符号!";
                alert(msg);
                return false;
            }

            if(formData.ubankno.length < 16 || formData.ubankno.length > 20){
                msg = "[提示]存款卡号长度错误!";
                alert(msg);
                return false;
            }*/
        }

        var saveType = $("#quick-save-type").val();
        if(saveType=='2' && nowTime>22 || saveType=='2' && nowTime<2){
            layer.confirm('尊敬的玩家您好：由于该时间段（23:00~01:00）为招行每日清算时间，该时间段转账至招商银行将无法实时到账，建议使用其他存款方式进行存款。', {
                title: '温馨提示', //按钮
                btn: ['继续支付','其他支付方式'], //按钮
                offset: ['36%', '38%']
            }, function(){
                that.save(that.formData);
            }, function(){
                that.resetQuickSave()
            });
        }else{
            that.save(that.formData);
        }
        // if(msg){
        //     alert(msg);
        // } else{
        //     //调用保存订单
        //     that.save(that.formData);
        //
        // }
        return false;
    });


    /**
     * 保存订单
     */
    this.save=function(formData){
        if(formData.banktype == 2){
            formData.ubankname = "支付宝";
        }
        if(formData.ubankname == "请选择"){
            formData.ubankname = "";
        }


        that.$nextBtn.prop('disabled',true);
        $.ajax({
            type: 'POST',
            url: "/asp/getNewdeposit.aspx",
            data: formData,
            //async: false, //注释掉 已经加了防止二次提交处理
            error: function (response) {
                that.$nextBtn.prop('disabled',false);
                alert('获取信息异常');
            },
            success: function (response) {
                that.$nextBtn.prop('disabled',false);
                var massage = response.massage;
                if(!massage){
                   // that.getQuickSaveUserInfo();
                    _creatDepositInfo(response);
                }
                else {
                    if(response['force']===true){// 有订单
                        layer.confirm(response.massage+'<p>请您按照您输入的存款信息进行存款，方可实时到账！！！</p>', {
                                title: '温馨提示', //按钮
                                btn: ['作废订单','取消'], //按钮
                                offset: ['36%', '38%']
                            },
                            function(){
                                formData['force']=true;

                                $.post('/asp/getNewdeposit.aspx',formData,function(response){
                                    layer.closeAll();
                                    $('#quick-save-next').removeAttr('disabled');
                                    if(!response.massage){
                                        _creatDepositInfo(response);
                                    }else{
                                        alert(massage);
                                    }
                                }).fail(function () {
                                    alert('获取信息异常');
                                    layer.closeAll();
                                });
                            }, function(){

                            });




                        // var ret= confirm(response.massage);
                        // if(ret){
                        //     formData['force']=true;
                        //     that.$nextBtn.prop('disabled',true);
                        //     $.post('/asp/getNewdeposit.aspx',formData,function(response){
                        //         that.$nextBtn.prop('disabled',false);
                        //         if(!response.massage){
                        //             _creatDepositInfo(response);
                        //         }else{
                        //             alert(massage);
                        //         }
                        //
                        //     }).fail(function () {
                        //         that.$nextBtn.prop('disabled',false);
                        //         alert('获取信息异常');
                        //     });
                        // }
                    }else{
                        alert(massage);//
                        window.location.href='/manageRecords.jsp?showOrderFuyan'
                    }

                }
            }
        });

        function _creatDepositInfo(response){
            that.setQuickSaveUserInfo(that.getQuickSaveUserInfo());

            that.setActivePage(that.$formConfirm);

            $("#sbankname").val(response.bankname);
            $("#saccountno").val(response.accountno).siblings('.btn-copy').attr("data-clipboard-text",response.accountno);
            $("#saccountname").val(response.username).siblings('.btn-copy').attr("data-clipboard-text", response.username);
            $("#mefuyan").val(response.zfbImgCode).siblings('.btn-copy').attr("data-clipboard-text", response.zfbImgCode);


            var clip = new ZeroClipboard(that.$formConfirm.find('.btn-copy'));
            clip.on('aftercopy',
                function(e) {
                    var val = $.trim(e.data['text/plain']);
                    if (val === '' || val === undefined) return;
                    var target = $(e.target);
                    alert('复制成功');
                    // setTimeout(function() {
                    //         target.html('复制');
                    //     },
                    //     1000);
                });
        }
    };


    /**
     * 取得玩家填寫存款信息
     * @returns formData
     */
    this.getQuickSaveUserInfo=function(){
        return {
            "type" : that.$quickSaveType.val(),
            "type_cn" :that.$quickSaveType.find("option:selected").text(),
            "username": that.$qSaveUserName.val(),
           // "bank": that.$qSaveBank.val(),
           // "card": that.$qSaveCard.val(), 
            "money": that.$qSaveMoney.val(),
            "depositCode": $("#quick-save-depositCode").val()
        };
    };

    /**
     * 配置玩家填寫存款信息
     * @param checkData
     */
    this.setQuickSaveUserInfo=function(checkData){
        $("#quick-confirm-type").val(checkData["type_cn"]);
        if(checkData["type_cn"].indexOf("银行") >=0){
            //$("#quick-confirm-depositCode").val(checkData["depositCode"]);
            $("#ckyh").show();
            $("#ckkh").show();
            $("#fyan").show();
        }
        else{
            $("#ckyh").hide();
            $("#ckkh").hide();
            $("#fyan").hide();
        }
        $("#quick-confirm-money").val(checkData["money"]);
        $("#quick-confirm-username").val(checkData["username"]);
        $("#quick-confirm-bank").val(checkData["bank"]);
        $("#quick-confirm-card").val(checkData["card"]);
    };


    /**
     * 查看历史记录
     */
    this.showBankHistory=function(){

        $.post("/asp/queryDepositBank.aspx",function(jsonData){
            if(jsonData && typeof jsonData != 'undefined'){
                var pageContents = jsonData.pageContents;

                var html = "";
                if(pageContents.length > 0){
                    var pageLength = (pageContents.length > 5) ? 5 :pageContents.length;
                    for(var i = 0; i < pageLength; i++){

                        var type = (pageContents[i].type) ? pageContents[i].type : "";
                        // var bankname = (pageContents[i].bankname) ? pageContents[i].bankname : "";
                        var ubankname = (pageContents[i].ubankname) ? pageContents[i].ubankname : "";
                        var uaccountname = (pageContents[i].uaccountname) ? pageContents[i].uaccountname : "";
                        var ubankno = (pageContents[i].ubankno) ? pageContents[i].ubankno : "";

                        var loginname = (pageContents[i].loginname) ? pageContents[i].loginname : "";
                        var depositid = (pageContents[i].depositId) ? pageContents[i].depositId : "";

                        html += "<tr>";

                        html += "<td>"+(i+1)+"</td>";
                        html +=	"<td>"+ubankname+"</td>";
                        html += "<td>"+uaccountname+"</td>";
                        html += "<td>"+ubankno+"</td>";
                        html += "<td>";
                        html += "<input type=\"button\" value=\"选中\" class=\"btn btn-sm quick-save-choose-btn\" data-type='"+type+"' data-name='"+uaccountname+"' data-bank='"+ubankname+"' data-card='"+ubankno+"'>";
                        html += "<input type=\"button\" value=\"删除\" class=\"btn btn-sm quick-save-delete-btn\" data-depositid='"+depositid+"' data-loginname='"+loginname+"' data-card='"+ubankno+"'>";
                        html += "</td>";

                        html += "</tr>";
                    }

                    that.$formHistory.find('.table tbody').html(html);

                } else {
                    that.$formHistory.find('.table tbody').html('<tr><td>暂无历史记录</td></tr>');
                }

                that.setChooseBtn();
                that.setDeleteBtn();
            }
        });
    };

    /**
     * 删除历史记录
     */
    this.setDeleteBtn=function(){
        var $qsdelbtn =that.$formHistory.find(".quick-save-delete-btn");
        $qsdelbtn.click(function(){
            var $that = $(this);
            var $parent = $that.closest("tr");

            var data={
                loginname :$that.data("loginname"),
                ubankno :$that.data("card"),
                depositId : $that.data("depositid")
            };

            $.post("/asp/updateDepositBank.aspx",data,function (jsonData) {
                if(jsonData != "" && typeof jsonData != "undefined"){
                    $parent.remove();
                }
            });

        });
    };


    /**
     * 选择历史记录
     */
    this.setChooseBtn=function(){
        var $chooseBtn =that.$formHistory.find(".quick-save-choose-btn");
        // 選中
        $chooseBtn.click(function(){

            var type = $(this).data("type");
            var name =$(this).data("name");
            var bank =$(this).data("bank");
            var card =$(this).data("card");
            var $saveCard=that.$qSaveCard;
            $saveCard.val(card);

           /* if(type == "2"){
                $saveCard.hide();
            } else {
                $saveCard.show();
            }*/

            if(type == "2"){
                $saveCard.val("");
                //$saveCard.attr("disabled", "disabled");
                $("#card").hide();
                $("#quick-money").show();
                $("#selectpay").hide();
            } else {
                $saveCard.attr("disabled", false);
                $("#quick-money").show();
                that.$qSaveBank.attr("disabled", false);
                $("#selectpay").show();
                $("#card").show();
            }

            that.$quickSaveType.val(type);

            // 賦予
            that.$qSaveUserName.val(name);
            // 下拉式選單
            that.$qSaveBank.find('option').each(function(){
                var text = $(this).text();
                if(text == bank){
                    $(this).attr("selected","selected");
                }
            });

            that.setActivePage(that.$formIndex);
        });
    };


    this.init();
}

//addis提交订单
function submitOrder(){

    var formData = {
        //收款卡信息
        bankname    : $("#sbankname").val(),
        username    : $("#saccountname").val(),
        //存款卡信息
        banktype    : $("#quick-confirm-type").val(),
        amount      : $("#quick-save-money").val(),
        uaccountname: $("#quick-confirm-username").val(),
        depositId   : $("#fy").val()
    };

    $.ajax({
        type: 'POST',
        async: false,
        url: "/asp/saveOrder.aspx",
        data: formData,
        error: function (response) {
            alert(response);
        },
        success: function (response) {
            alert(response);
        }
    });
}


