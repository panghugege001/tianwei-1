var modalRescue=$('#j-modal-rescue'), rescueId=modalRescue.find('.j-hd-id'), btnApplyRescue=modalRescue.find('.j-btn-apply');
$(function(){
	//判断是否已经绑定银行卡
	//查询积分
	queryPoint();
	//加载周周回馈
	losePromoRecord(1);
	btnApplyRescue.on('click',function(){
		btnApplyRescue.attr('disabled',true);
		var targetPlatform=modalRescue.find("input[name='targetRescuePlatform']:checked").val();
		$.ajax({
			type: "post",
			url: $(".j-hd-url").val()=="ws"?"/asp/optWeekSent.aspx":"/asp/optLosePromo.aspx",
			cache: false,
			beforeSend:function(){btnApplyRescue.val('加载中....');},
			data:{"jobPno":rescueId.val(), "proposalFlag":2, "platform":targetPlatform},
			success : function(data){
				alert(data);
				$(".j-hd-url").val()=="ws"?weekSentRecord(1):losePromoRecord();
			},
			error: function(){("系统错误");},
			complete: function(){
				btnApplyRescue.val('确定');
				btnApplyRescue.attr('disabled',false);
				modalRescue.modal('hide');
			}
		});
	});

	//优惠券专区绑定下拉列表
	/*$.post("/asp/getYouHuiConfig.aspx",function(data){
		$.each(data,function(index , ele){
			if(ele.title == '自助PT首存优惠'){
				shouCunConfig = ele ;
				$("#youhuiType option[value='590']").remove();
				$("#youhuiType").append("<option value='590'>"+shouCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助PT次存优惠'){
				ciCunCunConfig = ele ;
				$("#youhuiType option[value='591']").remove();
				$("#youhuiType").append("<option value='591'>"+ciCunCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助PT8元优惠'){
				tiYanJinConfig = ele ;
			}else if(ele.title == '自助EA次存优惠'){
				eaCiCunConfig = ele ;
				$("#youhuiType option[value='592']").remove();
				$("#youhuiType").append("<option value='592'>"+eaCiCunConfig.aliasTitle+"</option>");
			}
			else if(ele.title == '自助AG存送优惠'){
				agCiCunConfig = ele ;
				$("#youhuiType option[value='593']").remove();
				$("#youhuiType").append("<option value='593'>"+agCiCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助AGIN存送优惠'){
				aginCiCunConfig = ele ;
				$("#youhuiType option[value='594']").remove();
				$("#youhuiType").append("<option value='594'>"+aginCiCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助BBIN存送优惠'){
				bbinCiCunConfig = ele ;
				$("#youhuiType option[value='595']").remove();
				$("#youhuiType").append("<option value='595'>"+bbinCiCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助EBET首存优惠'){
				ebetShouCunConfig = ele ;
				$("#youhuiType option[value='596']").remove();
				$("#youhuiType").append("<option value='596'>"+ebetShouCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助EBET次存优惠'){
				ebetCiCunConfig = ele ;
				$("#youhuiType option[value='597']").remove();
				$("#youhuiType").append("<option value='597'>"+ebetCiCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助TTG首存优惠'){
				ttgShouCunConfig = ele ;
				$("#youhuiType option[value='598']").remove();
				$("#youhuiType").append("<option value='598'>"+ttgShouCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助TTG次存优惠'){
				ttgCiCunConfig = ele ;
				$("#youhuiType option[value='599']").remove();
				$("#youhuiType").append("<option value='599'>"+ttgCiCunConfig.aliasTitle+"</option>");
			}else if(ele.title == '自助GPI首存优惠'){
				FIRSTDEPOSITGPI = ele ;
				$("#youhuiType option[value='702']").remove();
				$("#youhuiType").append("<option value='702'>"+FIRSTDEPOSITGPI.aliasTitle+"</option>");
			}else if(ele.title == '自助GPI次存优惠'){
				TWICEDEPOSITGPI = ele ;
				$("#youhuiType option[value='703']").remove();
				$("#youhuiType").append("<option value='703'>"+TWICEDEPOSITGPI.aliasTitle+"</option>");
			}else if(ele.title == '自助GPI限时优惠'){
				GPITIMELIMITCONPON = ele ;
				$("#youhuiType option[value='704']").remove();
				$("#youhuiType").append("<option value='704'>"+GPITIMELIMITCONPON.aliasTitle+"</option>");
			}
			else if(ele.title == '自助PT限时优惠'){
				PTTIMELIMITCONPON = ele ;
				$("#youhuiType option[value='705']").remove();
				$("#youhuiType").append("<option value='705'>"+PTTIMELIMITCONPON.aliasTitle+"</option>");
			}else if(ele.title == '自助TTG限时优惠'){
				TTGTIMELIMITCONPON = ele ;
				$("#youhuiType option[value='706']").remove();
				$("#youhuiType").append("<option value='706'>"+TTGTIMELIMITCONPON.aliasTitle+"</option>");
			}else if(ele.title == '自助NT首存优惠'){
				NTFIRST = ele ;
				$("#youhuiType option[value='707']").remove();
				$("#youhuiType").append("<option value='707'>"+NTFIRST.aliasTitle+"</option>");
			}else if(ele.title == '自助NT次存优惠'){
				NTTWICE = ele ;
				$("#youhuiType option[value='708']").remove();
				$("#youhuiType").append("<option value='708'>"+NTTWICE.aliasTitle+"</option>");
			}else if(ele.title == '自助NT限时优惠'){
				NTSPEC = ele ;
				$("#youhuiType option[value='709']").remove();
				$("#youhuiType").append("<option value='709'>"+NTSPEC.aliasTitle+"</option>");
			}else if(ele.title == '自助QT首存优惠'){
				QTFIRST = ele ;
				$("#youhuiType option[value='710']").remove();
				$("#youhuiType").append("<option value='710'>"+QTFIRST.aliasTitle+"</option>");
			}else if(ele.title == '自助QT次存优惠'){
				QTTWICE = ele ;
				$("#youhuiType option[value='711']").remove();
				$("#youhuiType").append("<option value='711'>"+QTTWICE.aliasTitle+"</option>");
			}else if(ele.title == '自助QT限时优惠'){
				QTSPEC = ele ;
				$("#youhuiType option[value='712']").remove();
				$("#youhuiType").append("<option value='712'>"+QTSPEC.aliasTitle+"</option>");
			}else if(ele.title == '自助MG首存优惠'){
				MGFIRST = ele ;
				$("#youhuiType option[value='730']").remove();
				$("#youhuiType").append("<option value='730'>"+MGFIRST.aliasTitle+"</option>");
			}else if(ele.title == '自助MG次存优惠'){
				MGTWICE = ele ;
				$("#youhuiType option[value='731']").remove();
				$("#youhuiType").append("<option value='731'>"+MGTWICE.aliasTitle+"</option>");
			}else if(ele.title == '自助MG限时优惠'){
				MGSPEC = ele ;
				$("#youhuiType option[value='732']").remove();
				$("#youhuiType").append("<option value='732'>"+MGSPEC.aliasTitle+"</option>");
			} else if (ele.title === '自助DT首存优惠') {

				DTFIRST = ele;
				$("#youhuiType option[value='733']").remove();
				$("#youhuiType").append("<option value='733'>" + DTFIRST.aliasTitle + "</option>");
			} else if (ele.title === '自助DT次存优惠') {
				
				DTTWICE = ele;
				$("#youhuiType option[value='734']").remove();
				$("#youhuiType").append("<option value='734'>" + DTTWICE.aliasTitle + "</option>");
			} else if (ele.title === '自助DT限时优惠') {
				
				DTSPEC = ele;
				$("#youhuiType option[value='735']").remove();
				$("#youhuiType").append("<option value='735'>" + DTSPEC.aliasTitle + "</option>");
			}
		});
	});*/

	//自助返水
	/*if(target==='#tab_fs'){
	 getXimaEndTime($('#platform').val());
	 }*/
	/*$('#J_my_tab a[href="#tab_fs"]').on('click',function(){
	 getXimaEndTime($('#platform').val());
	 });*/
	$('#platform2').on('change',function(){
		getXimaEndTime($(this).val());
	});

	/* 根据标点显示tab
	 * ==============*/
	var target=window.location.hash;
	$('#j-coupons-tab a[href="'+target+'"]').trigger('click');

});

$(function(){

	/*//发送验证码
	$("#sendPhoneCodeBtn").on("click",function(){
		*//*********触点**********//*
		  var is_checked = false;
		        window.TouClick.Start({
		            website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
		            position_code: 0,
		            args: { 'this_form': $("#phoneCheckValid")[0] },
		            captcha_style: { 'left': '50%', 'top': '60%' },
		            onSuccess: function (args, check_obj)
		            {
		            	//console.log(args);
		            	//console.log(check_obj);
		                is_checked = true;
		                var this_form = args.this_form;
		                var hidden_input_key = document.createElement('input');
		                hidden_input_key.name = 'check_key';
		                hidden_input_key.value = check_obj.check_key;
		                hidden_input_key.type = 'hidden';
		                //将二次验证口令赋值到隐藏域
		                this_form.appendChild(hidden_input_key);
		                var hidden_input_address = document.createElement('input');
		                hidden_input_address.name = 'check_address';
		                hidden_input_address.value = check_obj.check_address;
		                hidden_input_address.type = 'hidden';
		                //将二次验证地址赋值到隐藏域
		                this_form.appendChild(hidden_input_address);
		                //再次执行 tou_submit 函数
		                //$("#btn").attr("disabled","");
		                //$("#btn").css("background","#EAE1E1");
		                //this_form.submit();
		                
		                openProgressBar();
		  			  var check_address = $("input[name='check_address']").eq(0).val();
		  			  var check_key = $("input[name='check_key']").eq(0).val();
		  			$.post("/asp/sendPhoneSmsCode.aspx",{"check_address":check_address,"check_key":check_key},function(data){
		  				alert(data);
		  				closeProgressBar();
		  			});
		            },
		            onError: function (args)
		            {
		                //启用备用方案
		            }
		        });
		  *//*********触点**********//*
		
	});
	
	$("#sendPhoneTwoCodeBtn").on("click",function(){
		openProgressBar();
		$.post("/asp/sendPhoneSmsTwoCode.aspx",{},function(data){
			alert(data);
			closeProgressBar();
		});
	});
	
	$("#sendPhoneVoiceCodeBtn").on("click",function(){
		*//*********触点**********//*
		  var is_checked = false;
		        window.TouClick.Start({
		            website_key: '0304e3d8-6d75-4bce-946a-06ada1cc5f4e',
		            position_code: 0,
		            args: { 'this_form': $("#phoneCheckValid")[0] },
		            captcha_style: { 'left': '50%', 'top': '60%' },
		            onSuccess: function (args, check_obj)
		            {
		            	//console.log(args);
		            	//console.log(check_obj);
		                is_checked = true;
		                var this_form = args.this_form;
		                var hidden_input_key = document.createElement('input');
		                hidden_input_key.name = 'check_key';
		                hidden_input_key.value = check_obj.check_key;
		                hidden_input_key.type = 'hidden';
		                //将二次验证口令赋值到隐藏域
		                this_form.appendChild(hidden_input_key);
		                var hidden_input_address = document.createElement('input');
		                hidden_input_address.name = 'check_address';
		                hidden_input_address.value = check_obj.check_address;
		                hidden_input_address.type = 'hidden';
		                //将二次验证地址赋值到隐藏域
		                this_form.appendChild(hidden_input_address);
		                
		                openProgressBar();
		  			  var check_address = $("input[name='check_address']").eq(0).val();
		  			  var check_key = $("input[name='check_key']").eq(0).val();
		  			$.post("/asp/sendPhoneVoiceCode.aspx",{"check_address":check_address,"check_key":check_key},function(data){
		  				alert(data);
		  				closeProgressBar();
		  			});
		            },
		            onError: function (args)
		            {
		                //启用备用方案
		            }
		        });
		  *//*********触点**********//*
	});*/
	
	//发送验证码
	$("#sendPhoneCodeBtn_bak").on("click",function(){
		/*********触点**********/
		  var is_checked = false;
		  TouClick().start({
			    modal:true,
			       position:"center",
			       fit:true, 
			       checkCode:"123",
				   onSuccess: function (obj)
				{
					is_checked = true;
					var check_address =obj.checkAddress;
					var token =obj.token;
					var this_form = $("#phoneCheckValid")[0];
		                var hidden_input_key = document.createElement('input');
		                hidden_input_key.name = 'check_key';
		                hidden_input_key.value = token;
		                hidden_input_key.type = 'hidden';
		                //将二次验证口令赋值到隐藏域
		                this_form.appendChild(hidden_input_key);
		                var hidden_input_address = document.createElement('input');
		                hidden_input_address.name = 'check_address';
		                hidden_input_address.value = check_address;
		                hidden_input_address.type = 'hidden';
		                //将二次验证地址赋值到隐藏域
		                this_form.appendChild(hidden_input_address);
		                //再次执行 tou_submit 函数
		                //$("#btn").attr("disabled","");
		                //$("#btn").css("background","#EAE1E1");
		                //this_form.submit();
		                
		                openProgressBar();
		  			  var check_address = $("input[name='check_address']").eq(0).val();
		  			  var check_key = $("input[name='check_key']").eq(0).val();
		  			$.post("/asp/sendPhoneSmsCode.aspx",{"check_address":check_address,"check_key":token},function(data){
		  				alert(data);
		  				closeProgressBar();
		  			});
		            },
		            onError: function (args)
		            {
		                //启用备用方案
		            }
		        });
		  /*********触点**********/
		
	});
	
	//发送验证码
	$("#sendPhoneCodeBtn").on("click",function(){
		/*********触点**********/
		  var is_checked = false;
		  TouClick('captcha-target',{
	            onSuccess : function(obj){
	             /*    $("token").value = obj.token;
	                $("checkAddress").value = obj.checkAddress;
	                $("sid").value = obj.sid; */
	                $.ajax({
	                    url : "/asp/sendPhoneSmsCode.aspx",
	                    type : "post", // 请求方式
	                    dataType : "text", // 响应的数据类型
	                    data : "check_address="+ obj.checkAddress+"&check_key="+obj.token+"&sid="+obj.sid,
	                    async : false, // 异步
	                    success : function(msg) {
	                        closeProgressBar();
	                        alert(msg);
	                    },
	                });
	            }
	        });
		  /*********触点**********/
		
	});
	
	$("#sendPhoneTwoCodeBtn").on("click",function(){
		openProgressBar();
		$.post("/asp/sendPhoneSmsTwoCode.aspx",{},function(data){
			alert(data);
			closeProgressBar();
		});
	});
	
	$("#sendPhoneVoiceCodeBtn_bak").on("click",function(){
		/*********触点**********/
		  var is_checked = false;
			TouClick().start({
			    modal:true,
			       position:"center",
			       fit:true, 
			       checkCode:"123",
				   onSuccess: function (obj)
				{
					is_checked = true;
					var check_address =obj.checkAddress;
					var token =obj.token;
					var this_form = $("#phoneCheckValid")[0];
		                var hidden_input_key = document.createElement('input');
		                hidden_input_key.name = 'check_key';
		                hidden_input_key.value = token;
		                hidden_input_key.type = 'hidden';
		                //将二次验证口令赋值到隐藏域
		                this_form.appendChild(hidden_input_key);
		                var hidden_input_address = document.createElement('input');
		                hidden_input_address.name = 'check_address';
		                hidden_input_address.value = check_address;
		                hidden_input_address.type = 'hidden';
		                //将二次验证地址赋值到隐藏域
		                this_form.appendChild(hidden_input_address);
		                
		                openProgressBar();
		  			  var check_address = $("input[name='check_address']").eq(0).val();
		  			  var check_key = $("input[name='check_key']").eq(0).val();
		  			$.post("/asp/sendPhoneVoiceCode.aspx",{"check_address":check_address,"check_key":token},function(data){
		  				alert(data);
		  				closeProgressBar();
		  			});
		            },
		            onError: function (args)
		            {
		                //启用备用方案
		            }
		        });
		  /*********触点**********/
	});
	
	$("#sendPhoneVoiceCodeBtn").on("click",function(){
		/*********触点**********/
		  var is_checked = false;
		  TouClick('captcha-target',{
	            onSuccess : function(obj){
	             /*    $("token").value = obj.token;
	                $("checkAddress").value = obj.checkAddress;
	                $("sid").value = obj.sid; */
	                $.ajax({
	                    url : "/asp/sendPhoneVoiceCode.aspx",
	                    type : "post", // 请求方式
	                    dataType : "text", // 响应的数据类型
	                    data : "check_address="+ obj.checkAddress+"&check_key="+obj.token+"&sid="+obj.sid,
	                    async : false, // 异步
	                    success : function(msg) {
	                        closeProgressBar();
	                        alert(msg);
	                    },
	                });
	            }
	        });
		  /*********触点**********/
	});
	
	//点击下一步时切换事件
	$(".pt8next").click(function(e){
		var me = $(this);
		var n_p = me.attr("data");
		if(n_p == 1){
			//是否开启PT8元优惠
			openProgressBar();
			$.post("/asp/haveSameInfo.aspx",{},function(data){
				 closeProgressBar();
				 if(data == 'success'){
					 toNext(me);
                	 $(".next-box1").show();
                	 $(".next-box2").hide();
                 	 var display = $(".disFlag1").css("display");
                     if(display == "none"){
                        $(".disFlag1").css("display","inline-block");
                     }
                 }else if(data == 'successSms'){
                	 toNext(me);
                	 $(".next-box2").show();
                	 $(".next-box1").hide();
                 	$.post("/asp/getPhoneAndCode.aspx",{},function(data){
                     	var str = $("#_valideteTextId").text().replace("#(1)",data.code).replace("#(2)",data.phone);//"短信安全验证(请发送短信"+data.code+"到"+data.phone+")"
                     	$("#_valideteTextId").text(str);
                     	smsSafeValidate = true;
                     });
                 }else{
                 	alert(data);
                 }
			});
		} else if (n_p == 2){
			var phoneCode = $("#phoneCode").val();
			if(checkPhoneCode(phoneCode)){
				toNext(me);
				//切换到银行卡页面时调用一次onchange事件
				$('#pt8_bank').trigger("onchange");
			}
		} else if (n_p == 3){
			openProgressBar();
			if(repeatBankCards()){
				toNext(me);
			}
			closeProgressBar();
		}
	});
	
	$(".pt8sub").click(function(e){
		var me = $(this);
		openProgressBar();
		$.post("/asp/commitPT8Self.aspx",{"code":$("#phoneCode").val(),"platform":$("input[name='myslotchoice']:checked").val()},function(data){
			alert(data);
			closeProgressBar();
			//防止玩家重复点击,完成后返回步骤1
			var par_ent = me.parents(".tab-panel:first");
			par_ent.removeClass("active");
			par_ent.prevAll().last().addClass("active");
			$($('#status>li')[3]).removeClass("active");//切换上方导航显示
			$($('#status>li')[0]).attr("class","active");
		});
	});
	
});

function loadIframe(obj){
	var $this=$(obj);
	var url=$this.attr('data-url');
	var target=$this.attr('href');
	$(target).find('.j-iframe').load(url);
}

//整数判断
function checkNum(val){
	return /^[1-9]\d*$/.test(val);
}
function clearNoNum(obj) {
	//只能输入大于0的整数
	obj.value = obj.value.replace(/\D|^0/g, "");
}


function toNext(me){
	var n_p = me.attr("data");
	var par_ent = me.parents(".tab-panel:first");
	par_ent.removeClass("active");//移除掉当前div的显示
	$(par_ent.parent().find(".tab-panel")[n_p]).removeClass("fade").addClass("active");//显示下一个div
	
	$($('#status>li')[n_p-1]).removeClass("active");//切换上方导航显示
	$($('#status>li')[n_p]).attr("class","active");
}

function checkPhoneCode(phoneCode){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "/asp/checkPhoneCode.aspx",
         data : "code="+phoneCode,
         async : false,
         success : function(data){
        	 if(!(data == 'right')){
     			alert(data);
     		}else{
     			flag = true ;
     		}
         }
    });
	return flag ;
}
function repeatBankCards(){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "/asp/repeatBankCards.aspx",
         async : false,
         success : function(data){
        	 if(!(data == 'success')){
     			alert(data);
     		}else{
     			flag = true ;
     		}
         }
    });
	return flag ;
}
function getbankno(_bankname){
	var flag = false ;
	$.ajax({
        type : "post",
         url : "/asp/searchBankno.aspx",
         data : "bankname="+_bankname+"&r="+Math.random(),
         async : false,
         success : function(data){
        	if(data == 1){
       			$("#accountNo").val("");
       			$("#yhBankNo").val(""); 
       		}else{
       			$("#accountNo").val(data.split("|||")[0]);
       			$("#yhBankNo").val(data.split("|||")[0]);
       		}
         }
    });
	return flag ;
}

// 签到记录
function doSignRecord(){
	openProgressBar();
	$.ajax({
		url : "/asp/doSignRecord.aspx",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data :"",
		async : true, // 异步
		success : function(msg) {
			closeProgressBar();
			alert(msg);
			if(msg.indexOf("不满足")>0){
				return;
			}
			querySignAmount();
			/* changeImage();
			 $("#name").val(''); */
		}
	});
}

//查询奖金
function querySignAmount(){
	openProgressBar();
	$.ajax({
		url : "/asp/querySignAmount.aspx",
		type : "post", // 请求方式
		dataType : "text", // 响应的数据类型
		data :"",
		async : true, // 异步
		success : function(msg) {
			closeProgressBar();
			$("#qdmoney1").html("");
			$("#qdmoney1").html(msg+"元");
		}
	});
}
//获取全民闯关奖金余额
function getEmigratedMoeny(){{
		$("#emigratedMoney").html("<img src='/images/waiting.gif'>");
		$.post("/asp/getEmigratedMoney.aspx",
			{
			}, function (returnedData, status)
			{
				if ("success" == status) {
					$("#emigratedMoney").html(""+returnedData+"元");
				}
			});
	}
	getEmigratedApply();
}

//全民闯关
function submitEmigratedRemit(){
	var emigratedType=$("#emigratedType").val();
	var emigratedRemit=$("#emigratedRemit").val();
	if(emigratedType==""){
		alert("请选择平台！");
		return  false;
	}
	if(emigratedMoney!=""){
		if(isNaN(emigratedRemit)){
			alert("转账金额非有效数字！");
			return false;
		}
		if(emigratedRemit<10){
			alert("转账金额必须大于10！");
			return false;
		}
	}
	openProgressBar();
	$.post("/asp/submitEmigratedRemit.aspx", {
		"type":emigratedType,
		"transferGameIn":emigratedRemit
	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			getEmigratedMoeny();
			alert(returnedData);
		}
	});
	return false;
}


//领取昨日奖励
function doEmigrated(){
	openProgressBar();
	$.post("/asp/doEmigrated.aspx", {

	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			getEmigratedMoeny();
			alert(returnedData);
		}
	});
	return false;
}


//报名
function EmigratedApply(){
	var applytype=$("#applytype").val();
	if(applytype==0){
		alert("请选择参与等级");
		return ;
	}
	openProgressBar();
	$.post("/asp/EmigratedApply.aspx", {
		"type":applytype
	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			alert(returnedData);
			getEmigratedApply();
		}
	});
	return false;
}

//获取报名信息
function getEmigratedApply(){
	$.post("/asp/getEmigratedApply.aspx", {
	}, function (returnedData, status) {
		if ("success" == status) {
			if(null!=returnedData&&''!=returnedData){alert(returnedData);
				//$("#selecttype").attr("style","display: none;");
				$("#selecttype").html(returnedData);
				$("#selectbutton").html("");
			}
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
	$.post("/asp/transferSign.aspx", {
		"signType":signType,
		"signRemit":signRemit,
	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			alert(returnedData);
			querySignAmount();
		}
	});
	return false;
}
//查询摇摇乐奖金
/* function queryTaskAmount(){
 openProgressBar();
 $.post('/asp/getTaskAmount.aspx',function(data){
 closeProgressBar();
 $("#j-task-balance").html(data+'元');
 });
 }*/
//摇摇乐奖金转账
/* function transferTaskAmount(){
 var amount=$('#j-task-amount').val();
 if(!amount){
 alert('转账金额不能为空！');
 return false;
 }
 if(!checkNum(amount)){
 alert('转账金额只能为整数！');
 return false;
 }
 openProgressBar();
 $.post('/asp/transferTaskAmount.aspx',{'amount':amount},function(data){
 closeProgressBar();
 alert(data);
 });
 return false;
 }*/
 

function isBlindingCard() {
	$.ajax({
		type   : "post",
		url    : "/asp/isBlindingCard.aspx",
		async  : true,
		success: function (data) {
			if (!(data == 'pass')) {
				alert('请您先在账号设置——银行卡绑定选择绑定银行卡在进行操作');
				$("#j-user-nav li").find('a[href="#user-vip"]').tab('show');
				$('#user-vip').find('a[href="#tab-card-binding"]').trigger('click');
//				window.location.href="/index.jsp"; 
			}
		}
	});
}

/* 优惠券
 * ======*/
function submitRemit(){
	var couponType=$("#couponType").val();
	var couponRemit=$("#couponRemit").val();
	var couponCode=$("#couponCode").val();
	console.log("1")
	if(couponType==""){
		alert("请选择平台！");
		return  false;
	}
	if(couponRemit!=""){
		if(isNaN(couponRemit)){
			alert("存款金额非有效数字！");
			return ;
		}
		if(couponRemit<50){
			alert("存款金额不能小于50！");
			return ;
		}
	}
	if(couponCode==""){
		alert("优惠代码不能为空！");
		return  ;
	}
		console.log("2")
	$.post('/asp/transferInforCoupon.aspx',{"couponType":couponType,"couponRemit":couponRemit,"couponCode":couponCode},
		function(data){
		alert(data);
		console.log(data)
	});

	/*openProgressBar() ;
	 $.post("/asp/transferInforCoupon.aspx",{"couponType":couponType,"couponRemit":couponRemit,"couponCode":couponCode},

	 (data){
	 closeProgressBar();
	 alert(data);
	 });*/
	console.log("4")
}




/* 自助返水
 * =======*/

//反水
function getAutoXimaObject(){
	var startTime=$("#startTime").val();
	if(startTime==""){
		return false;
	}
	var endTime=$("#endTime").val();
	var platform=$("#platform2").val();

	if(endTime==""){
		return false;
	}
	if(platform==""){
		return false;
	}
	openProgressBar();
	$.post("/asp/getAutoXimaObjectData.aspx", {
		"startTime":startTime,
		"endTime":endTime,
		"platform":platform
	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			var ximaBean=returnedData;
			var validAmount=ximaBean.validAmount;
			var ximaAmount=ximaBean.ximaAmount;
			var rate=ximaBean.rate;
			var message=ximaBean.message;
			if(message==null || message==""){
				$("#validAmount").val(validAmount);
				$("#ximaAmount").val(ximaAmount);
				$("#rate").val(rate);
			}else{
				alert(message);
			}
		}
	});
	return false;
}
//打开反水
function getXimaEndTime(platform){
	
	if(platform != "slot"){
    $('#otherList').show();
    $('#slotList').hide();		
	$("#validAmount").val("");
	$("#rate").val("");
	$("#ximaAmount").val("");
	$("#endTime").val("");
	openProgressBar();
	$.post("/asp/getXimaEndTime.aspx", {"platform":platform
	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			if(returnedData.indexOf("-")>=0){
				$("#startTime").val(returnedData.split(",")[0]);
				$("#endTime").val(returnedData.split(",")[1]);
				getAutoXimaObject();
			}else{
				alert(returnedData);
			}
		}
	});			
	}else{
		soltSelfGetEvent();
	}
	
}

//新老虎机反水
function soltSelfGetEvent () { 
    $('#otherList').hide();
    $('#slotList').show();
    openProgressBar();
    $.post("/asp/getAutoXimaSlotObject.aspx", {"platform":'slot'
        }, function (returnedData, status) {
            if ("success" == status) { 
                closeProgressBar();
                var html,message = returnedData.message; 
                if(message == 'success'){
                    var jsonData = eval('(' + returnedData.jsonResult + ')');
                    var totalCount = 0;
                    for (var key in jsonData){
                        if(key != 'message'){
                            html += '<tr>'+'<td>' +key+ '</td>'+'<td>'+jsonData[key].startTimeStr+'</td>'+'<td>'+jsonData[key].endTimeStr+'</td>'+'<td>'+jsonData[key].validAmount+'</td>'+'<td>'+jsonData[key].rate+'</td>'+'<td>'+jsonData[key].ximaAmount+'</td>'+'</tr>'
                        }
                        totalCount += jsonData[key].ximaAmount
                    }
                    $('.totalCount').html(totalCount.toFixed(2))
                    $('#slotList table tbody').html(html);
                }else {
                    alert(message)
                }
/*                if(returnedData){
                    $("#startTime").val(returnedData.split(",")[0]);
                    $("#endTime").val(returnedData.split(",")[1]);
                    getAutoXimaObject();
                }else{
                    alert(returnedData);
                }*/
            }
        });

}
function newExecXimaSubmit () {
    if(!window.confirm("确定吗？"))
        return ;
    openProgressBar();
    $.post("/asp/execSlotXima.aspx", {
        "platform":'slot'
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            alert(returnedData.message);
            $("#submit_fanshui").attr("disabled" ,false);
            flag = true ;
        }
    });
    return false;
}


//提交反水
function checkEaSubmit(){
	if(!window.confirm("确定吗？"))
		return ;
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	var validAmount=$("#validAmount").val();
	var rate=$("#rate").val();
	var ximaAmount=$("#ximaAmount").val();
	var platform=$("#platform2").val();
	if(startTime==""||endTime==""||validAmount==""||rate==""||ximaAmount=="" || platform ==""){
		alert("所有项都为必填项\n请重新选择[截止时间]，以让系统为您自动填写其他栏目");
		return ;
	}
	openProgressBar();
	$.post("/asp/execXima.aspx", {
		"startTime":startTime,
		"endTime":endTime,
		"validAmount":validAmount,
		"rate":rate,
		"platform":platform,
		"ximaAmount":ximaAmount
	}, function (returnedData, status) {
		if ("success" == status) {
			getXimaEndTime(platform);
			closeProgressBar();
			alert(returnedData);
		}
	});
	return false;
}

/* 升级监测
 * ========*/
function checkUpgrade(type){
	closeProgressBar();
	$.ajax({
		type: "post",
		url: "/asp/checkUpgrade.aspx",
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
	$.post("/asp/queryWeekSentReccords.aspx", {
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

/* 加载数据
 * ========*/
function losePromoRecord(pageIndex){
	//选择老虎机窗口共用，区分周周回馈|负盈利反赠
	$(".j-hd-url").val("lp");

	$.post('/asp/queryPTLosePromoReccords.aspx',
		{'pageIndex':pageIndex, 'size':8},
		function(returnedData, status){
			if ("success" == status) {
				$("#losepromoRecordDiv").html(returnedData);
			}
		});

}

//取消负盈利反赠
function cancelLosePromo(pno){
	if(window.confirm('是否确定取消该笔返赠？')){
	}else{
		return;
	}
	$.ajax({
		type: "post",
		url: "/asp/optLosePromo.aspx",
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

//PT疯狂礼金
function queryPTBigBang(){
	$.post("/asp/queryPTBigBang.aspx",
		function (returnedData, status) {
			if ("success" == status) {
				$("#ptBigBangDiv").html("");
				$("#ptBigBangDiv").html(returnedData);
			}
		});
	return false;
}
//领取PT疯狂礼金
function getPTBigBangBonus(id){
	$.ajax({
		type: "post",
		url: "/asp/getPTBigBangBonus.aspx",
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

function turnPage(pageIndex){
	losePromoRecord(pageIndex);
}

//好友推荐奖金转账
function submitFriendRemit(){
	var signType=$("#friendType").val();
	var signRemit=$("#friendRemit").val();
	if(signType==""){
		alert("请选择平台！");
		return  false;
	}
	if(signRemit!=""){
		if(isNaN(signRemit)){
			alert("转账金额非有效数字！");
			return false;
		}
		if(signRemit<5){
			alert("转账金额不能小于5元！");
			return false;
		}
		var rex = /^[1-9][0-9]*$/;
		if (!rex.test(signRemit)){
			alert("抱歉，好友推荐金额只能是整数哦。");
			return false;
		}
	}else{
		alert("请输入金额！");
		return false;
	}
	openProgressBar();
	$.post("/asp/transferInforFriend.aspx", {
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

//好友推荐奖金余额
function queryfriendMoney(){
	openProgressBar();
	$.post("/asp/queryfriendBonue.aspx",
		function (returnedData, status) {
			if ("success" == status) {
				closeProgressBar();
				var strs=returnedData.split('#');
				$("#friendmoney").html(strs[0]+"元");
				$("#friendurl").html(document.domain+"?friendcode="+strs[1]);
				$(".j-copy").attr("data-clipboard-text",document.domain+"?friendcode="+strs[1]);

				var clip1 = new ZeroClipboard($('.j-copy'));
				clip1.on('aftercopy',function(e){
					var val= $.trim(e.data['text/plain']);
					if(val===''||val===undefined) return;
					var target=$(e.target);
					target.html('复制成功')
					setTimeout(function(){target.html('复制')},1000);
				});
			}
		});
	return false;
}



//积分余额
function queryPoint(){
	openProgressBar();
	$.post("/asp/queryPoints.aspx",
		function (returnedData, status) {
			if ("success" == status) {
				closeProgressBar();
				var strs=returnedData.split("#");
				$("#friendPoint, #friendPoint1").html(strs[0]);
				$("#totalfriendPoint").html(strs[1]);
				$("#moneypoint").html(strs[2]+"元 ");
			}
		});
	return false;
}

function changexhjf(){
	var signRemit=$("#pointRemit").val();
	$("#xhjf").html(signRemit*2000);
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
	$.post("/asp/transferInforPoint.aspx", {
		"signRemit":signRemit,
	}, function (returnedData, status) {
		if ("success" == status) {
			closeProgressBar();
			queryPoint();
			alert(returnedData);
		}
	});
	return false;
}


//玩家积分记录
function querypointRecord(){
	openProgressBar();
	$.post("/asp/querypointRecord.aspx", {
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
	$.post("/asp/querypointRecord.aspx", {
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

//红包优惠
function submitRedCouponRemit(){
	var couponType=$("#redcouponType").val();
	var couponCode=$("#redcouponCode").val();
	if(couponType==""){
		alert("请选择平台！");
		return  false;
	}
	if(couponCode==""){
		alert("优惠代码不能为空！");
		return  false;
	}
	$.post("/asp/transferInforRedCoupon.aspx", {
		"couponType":couponType,
		"couponCode":couponCode
	}, function (returnedData, status) {
		if ("success" == status) {
			alert(returnedData);
		}
	});
	return false;
}
//守护女神报名
function goddessApply(){
	 var goddess=$("#applygoddess").val();
	 if(goddess==0){
		 alert("请选择女神");
		 return ;
	 }
	   openProgressBar();
    $.post("/asp/goddessApply.aspx", {
     "goddess":goddess
    }, function (returnedData, status) {
       if ("success" == status) {
     	 closeProgressBar();
          alert(returnedData);
          getGoddessApply();
       }
    });
	   return false;
}

//获取守护女神报名信息
function getGoddessApply(){
    $.post("/asp/getGoddessApply.aspx", {
    }, function (returnedData, status) {
       if ("success" == status) {
     	 if(null!=returnedData&&''!=returnedData){
     		//$("#selecttype").attr("style","display: none;");
     		$("#goddessapply").html(returnedData);
//     		$("#selectbutton").html("");
     	 }
       }
    });
	   return false;
}

var result = [];

$.ajax({
	type: "post",  
	url: "/asp/getYouHuiConfig.aspx",  
	data: {},  
	async: false,  
	success: function (data) {
	
		result = data;
		xianShiYouHui(data);
	}
});
function xianShiYouHui(data){
	$.each(data,function(index , ele){
		if(ele.title == '自助PT首存优惠'){
			shouCunConfig = ele ;
		}else if(ele.title == '自助PT次存优惠'){
			ciCunCunConfig = ele ;
		}else if(ele.title == '自助PT8元优惠'){
			tiYanJinConfig = ele ;
		}else if(ele.title == '自助TTG首存优惠'){
			ttgshouCunConfig = ele ;
		}else if(ele.title == '自助TTG次存优惠'){
			ttgciCunCunConfig = ele ;
		}else if(ele.title == '自助PT限时优惠'){
   			PTTIMELIMITCONPON = ele ;
   			
   			$("#youhuiType1 option[value='705']").remove();
   			$("#youhuiType1").append("<option value='705'>"+PTTIMELIMITCONPON.aliasTitle+"</option>");
   		}else if(ele.title == '自助TTG限时优惠'){
   			TTGTIMELIMITCONPON = ele ;
   			
   			$("#youhuiType1 option[value='706']").remove();
   			$("#youhuiType1").append("<option value='706'>"+TTGTIMELIMITCONPON.aliasTitle+"</option>");
   		}
   		else if(ele.title == '自助NT首存优惠'){
   			NTFIRST = ele ;
   		}else if(ele.title == '自助NT次存优惠'){
   			NTTWICE = ele ;
   		}else if(ele.title == '自助NT限时优惠'){
   			NTSPEC = ele ;
   			
   			$("#youhuiType1 option[value='709']").remove();
   			$("#youhuiType1").append("<option value='709'>"+NTSPEC.aliasTitle+"</option>");
   		}
   		else if(ele.title == '自助QT首存优惠'){
   			QTFIRST = ele ;
   		}else if(ele.title == '自助QT次存优惠'){
   			QTTWICE = ele ;
   		}else if(ele.title == '自助QT限时优惠'){
   			QTSPEC = ele ;
   			
   			$("#youhuiType1 option[value='712']").remove();
   			$("#youhuiType1").append("<option value='712'>"+QTSPEC.aliasTitle+"</option>");
   		}
   		else if(ele.title == '自助MG首存优惠'){
   			MGFIRST = ele ;
   		}else if(ele.title == '自助MG次存优惠'){
   			MGTWICE = ele ;
   		}else if(ele.title == '自助MG限时优惠'){
   			MGSPEC = ele ;
   			
   			$("#youhuiType1 option[value='732']").remove();
   			$("#youhuiType1").append("<option value='732'>"+MGSPEC.aliasTitle+"</option>");
   		}else if(ele.title == '自助DT首存优惠'){
   			DTFIRST = ele ;
   		}else if(ele.title == '自助DT次存优惠'){
   			DTTWICE = ele ;
   		}else if(ele.title == '自助DT限时优惠'){
   			DTSPEC = ele ;
   			
   			$("#youhuiType1 option[value='735']").remove();
   			$("#youhuiType1").append("<option value='735'>"+DTSPEC.aliasTitle+"</option>");
   		}
	});
}

// 领取免费筹码
function getMoney(){ 
	$.ajax({
	type: "post",
	url: "/asp/checkActivityInfo.aspx",
	cache: false,
	beforeSend:function(){$('#moneyVal').add($('#theMultiple')).val('加载中');},
	data:{titleId:'vipmonthfree'},
	success : function(data){
			if(!data.amount){
			$('#moneyVal').val(0 + '元');
			}else {
			$('#moneyVal').val(data.amount + '元');
			}
			$('#theMultiple').val(data.multiple + '倍');
		var htmlarr = ['<option value="" selected="">请选择游戏平台</option>'],platformarr;
		if (!data.platform){
			$('.need-hide').hide(); 
		}
		else {
			platformarr = data.platform.split(',');
			for (var i = 0; i < platformarr.length; i++){
				htmlarr.push('<option value="' + platformarr[i] + '">' + platformarr[i] +'</option>');
			}
		}
		$('#j-chipPaltform').html(htmlarr.join(''));
	},
	error: function(){("系统错误");},
	complete: function(){
	}
});
}
getMoney();
function getFreeChip(){
    $.post('/asp/applyActivity.aspx',{titleId:'vipmonthfree',platform:$('#j-chipPaltform').val(),entrance:'pc'},function (data) {
        alert(data);
    })
}
