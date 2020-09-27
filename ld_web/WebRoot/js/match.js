
!function($){
    var objM1={}, // 抢票活动一对象
        objM2={}, // 抢票活动二对象
        //currentDate = new Date($('#j-date').val()),
        currentDate = new Date($('#j-date').val()),
        $btnLogin=$('#j-login'), //登录按钮
        $btnTicket1=$('#j-btn-ticket1'), //抢票一按钮
        $btnTicket2=$('#j-btn-ticket2'),//抢票二按钮
        $teamHtml1=$('#j-team1'),
        $teamHtml2=$('#j-team2'),
        $formAddr=$('#j-form-addr'),
        $ticketModal=$('#j-modal-ticket'); //抢票信息填写弹框

    objM1={
        startDate:new Date('2016-06-22 00:00:00'),
        endDate:new Date('2016-06-26 00:00:00'),
        diff:function(){
            return objM1.startDate.getTime()/1000-currentDate.getTime()/1000;
        },
        state:0,
        getState:function(){
            /** 活动状态
             1.未开始，2.已经开始,3.停止,4.结束
             */
            //var _diff= m.endDate.getTime()/1000-m.startDate.getTime()/1000;
            if(currentDate<objM1.startDate){
                $btnTicket1.prop('disabled',true).val('未开始！');
                objM1.state=1;
            }else if(currentDate>objM1.endDate){
                $btnTicket1.prop('disabled',true).val('已经结束！');
                objM1.state=4;
            } else if (objM1.startDate <= currentDate < objM1.endDate) {
                objM1.state=2;
            }
        }
    };

    objM2={
        startDate:new Date('2016-06-27 00:00:00'),
        endDate:new Date('2016-06-30 00:00:00'),
        diff:function(){
            return objM2.startDate.getTime()/1000-currentDate.getTime()/1000;
        },
        state:0,
        getState:function(){
            if(currentDate < objM2.startDate){
                $btnTicket2.prop('disabled',true).val('未开始！');
                objM2.state = 1;
            }else if(currentDate > objM2.endDate){
                $btnTicket2.prop('disabled',true).val('已经结束！');
                objM2.state = 4;
            } else if (objM2.startDate <= currentDate < objM2.endDate) {
                objM2.state = 2;
            }
        }
    };

    var match=match||{};

    match.checkState=function(){
        objM1.getState();
        objM2.getState();

        if(objM1.state==2){
            $.get('/asp/getGiftId.aspx?v='+Math.random(),function (data) {
                data==5?$btnTicket1.prop('disabled',false).val('立即抢票(60张)'):
                    $btnTicket1.prop('disabled',true).val('已经抢完！');
            });
        }else if(objM2.state==2){
            $btnTicket2.prop('disabled',false).val('立即抢票(60张)');
            // $.get('/asp/getGiftId.aspx?v='+Math.random(),function (data) {
            //     data==6?$btnTicket2.prop('disabled',false).val('立即抢票(60张)'):
            //         $btnTicket2.prop('disabled',true).val('已经抢完！');
            // });
        }

    };

    match.countDown=function(){
        var clock,
            clockInit=function(diff,callback){
                if(diff<=0) diff=0;
                clock = $('#j-clock').FlipClock(diff, {
                    clockFace: 'DailyCounter',
                    countdown: true,
                    callbacks:{
                        stop:callback
                    }
                });
            };

        if(objM1.state==1){ //活动一未开始
            clockInit(objM1.diff(),function(){
                $btnTicket1.prop('disabled',false).val('立即抢票(60张)');
            });
        }else if(objM1.state==4 && objM2.state==1){ //活动一结束，活动二还没开始
            clockInit(objM2.diff(),function(){
                $btnTicket2.prop('disabled',false).val('立即抢票(60张)');
            });
        }else {
            clockInit(0,function(){
                $btnTicket2.prop('disabled',false).val('立即抢票(60张)');
            });
        }

    };
    match.slide=function(){
        $('#banner').on('slide.bs.carousel', function (e) {
            var index=$(e.relatedTarget).index();
            if(index==0){
                $teamHtml1.show();
                $teamHtml2.hide();
            }else if(index==1){
                $teamHtml2.show();
                $teamHtml1.hide();
            }
        });
    };

    // 获取礼品id
    match.isExistGift=function(){
        $.ajax({
            url     : "/asp/isExistGift.aspx",
            type    : "post",
            dataType: "json",
            cache   : false,
            success : function (data) {
                $('#j-giftid').val(data.id);
                $('#j-road').val(data.title);
                if(data.reach){
                    if(data.order){  //已经领取
                        //alert('您已经参与该活动！');
                        match.setMsg(data.id,'您已经参与该活动！');
                    }else{  //还没有领取
                        match.setMsg(data.id,'',true);
                    }
                }else{ //还没有达到活动资格
                    //alert('您还没有达到活动资格！');
                    match.setMsg(data.id,'您还没有达到活动资格！');
                }
            }
        })
            .fail(function(){
                $btnTicket1.attr('data-msg','无相关活动');
                $btnTicket2.attr('data-msg','无相关活动');
            });
    };
    match.setMsg=function (giftId,msg,isOpen) {
        if(giftId==5){  //富力抢票活动
            $btnTicket1.attr('data-msg',msg);
            isOpen&&$btnTicket1.on('click',function(){
                $ticketModal.modal('show');
            });
        }else if(giftId==6){ //恒大抢票活动
            $btnTicket2.attr('data-msg',msg);
            isOpen&&$btnTicket2.on('click',function(){
                $ticketModal.modal('show');
            });
        }
    };
    match.saveData=function(){
        $formAddr.validate({
            submitHandler:function(form){
                $.post('/asp/applyGift.aspx',$(form).serialize(),function(data){
                    alert(data);
                    window.location.reload();
                })
            }
        });
    };
    /**
     * 登录
     * @returns {boolean}
     */
    match.login=function(){
        var loginname=$('#j-username').val(),
            password=$('#j-pwd').val(),
            code=$('#j-code').val();

        $btnLogin.prop('disabled',true);

        if(loginname==""||loginname=="帐 号"){
            alert("账号不能为空！");
            return false;
        }
        if(password==""||password=="密 码"){
            alert("密码不能为空！");
            return false;
        }
        if(code==""||code=="验证码"){
            alert("验证码不能为空！");
            return false;
        }
        $.post("/asp/login.aspx", {
            "loginname":loginname, "password":password,"imageCode":code
        }, function (returnedData) {
            $btnLogin.prop('disabled',false);
            if(returnedData=="SUCCESS"){
                window.location.reload();
            }else{
                $('#j-codeimg').attr('src','/asp/validateCodeForIndex.aspx?r='+Math.random());
                alert(returnedData);
                var str2='已被锁';
                if(returnedData.indexOf(str2)>-1){
                    window.location="/forgotPassword.jsp";
                }
            }
        });
    };
    match.init=function(){
        match.checkState();
        match.countDown();
        match.slide();

        $btnLogin.on('click',match.login);

        var c=function(){
            var msg=$(this).data('msg');
            msg&&alert(msg);
        };
        $btnTicket1.on('click',c);
        $btnTicket2.on('click',c);
    };

    $(function(){
        match.init();
    });

    window.match=match;
    window.objM1=objM1;
    window.objM2=objM2;
}(jQuery);
