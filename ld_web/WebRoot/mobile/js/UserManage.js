/**
 * 负责所有帐户相关的操作
 */

function UserManage(base_url){
	var that = this;
	var _base_url = base_url;
	var _ajaxObj = {};
	var _urlObj = {
		login:'mobi/login.aspx',
		logout:'mobi/logout.aspx',
		register:'mobi/register.aspx',
		registerAgent:'mobi/registerAgent.aspx',
		changePWD:'mobi/modifyPassword.aspx',
		modifyInfo:'mobi/modifyInfo.aspx',
		findPasswordByEmail:'mobi/findPasswordByEmail.aspx',
		findPasswordByPhone:'mobi/findPasswordByPhone.aspx',
		checkUpgrade:'mobi/checkUpgrade.aspx',
		checkSecurityUser:'mobi/checkSecurityUser.aspx',
		getQuestion:'mobi/getQuestion.aspx',
		saveQuestion:'mobi/mSaveQuestion.aspx',
		makeCall:'mobi/makeCall.aspx',
		getAgentReport:'mobi/agentReport.aspx',
		readEmail:'mobi/readEmail.aspx',
		unreadEmailCount:'mobi/unreadEmailCount.aspx',
	    credit:'mobi/credit.aspx',
	    sendVoiceCode:'mobi/sendVoiceCode.aspx',
		sendSmsCode:'mobi/sendSmsCode.aspx',
		checkSameInfo:'mobi/checkSameInfo.aspx',
	    checkPhoneCode:'mobi/checkPhoneCode.aspx',
	    checkValidCode:'mobi/checkValidCode.aspx',
	    checkRepeatBankCards:'mobi/checkRepeatBankCards.aspx',
	    ticketLogin:'mobi/ticketLogin.aspx',      
	    checkForAppPreferential:'mobi/checkForAppPreferential.aspx'
	};
	
	
	
	/**
	 * login
	 */
	that.login = function(formData,callback){
		var _formData = {
			account:'',
			password:''
			/*imageCode:''*/
		};
		$.extend(_formData,formData);

		//check form data
		var err = _loginDataCheck(_formData);
		if(err){
			callback({success:false,message:err});
			_formData = null;
			return;
		}
		mobileManage.ajax({
			url:getUrl('login'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * logout
	 */
	that.logout = function(callback){
		mobileManage.ajax({
			url:getUrl('logout'),
			callback:callback
		});
	};
	
	/**
	 * change password
	 */
	that.changePassword=function(formData,callback){
		var _formData = {
			password:'',
			newPassword:'',
			confirmPassword:''
		};
		$.extend(_formData,formData);
		//check
		var err = _changePWSDataCheck(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		mobileManage.ajax({
			url:getUrl('changePWD'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * 依郵件找回密碼
	 */
	that.findPasswordByEmail = function(formData,callback){
		var _formData = {
			account:'',
			email:'',
			imageCode:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _findPasswordByEmailCheck(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		mobileManage.ajax({
			url:getUrl('findPasswordByEmail'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * 手機發送簡訊找回密碼
	 */
	/*that.findPasswordByPhone = function(formData,callback){
		
		var _formData = {
			account:'',
			phone:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _findPasswordByPhoneCheck(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		//点触验证
		mobileManage.openTouClick(function(tResult){
			if(tResult.success){
				mobileManage.getLoader().open("发送中");
				_formData['checkAddress'] = tResult.data.checkAddress.toString();
				_formData['checkKey'] = tResult.data.token;
				_formData['sid'] = tResult.data.sid;
				mobileManage.ajax({
					url:getUrl('findPasswordByPhone'),
					param:_formData,
					callback:function(result){
						mobileManage.getLoader().close();
						if(typeof callback === 'function'){
							callback(result);
						}
					}
  				});
			}else{
				alert(tResult.message);
			}
		});
	};*/

    /**
     * 手機發送簡訊找回密碼
     */
    that.findPasswordByPhone = function(formData,callback){

        var _formData = {
            account:'',
            email:'',
            imageCode:''
        };
        $.extend(_formData,formData);
        //檢查
        var err = _findPasswordByPhoneCheck(_formData);
        if(err){
            callback({success:false,message:err});
            return;
        }

        mobileManage.getLoader().open("发送中");

        mobileManage.ajax({
            url:getUrl('findPasswordByPhone'),
            param:_formData,
            callback:callback
        });
        mobileManage.getLoader().close();
    };
	
	/**
	 * 一般注册
	 */
	that.register = function(formData,callback){
		var _formData = {
			account:'',
			password:'',
			confirmPassword:'',
			name:'',
			aliasName:'',
			email:'',
			birthDate:'',
			intro:'',
			phone:'',
			qq:'',
			imageCode:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _registerDataCheck(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		mobileManage.ajax({
			url:getUrl('register'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * 代理注册
	 */
	that.registerAgent = function(formData,callback){
		var _formData = {
			account:'',
			password:'',
			confirmPassword:'',
			name:'',
			url:'',
			email:'',
			phone:'',
			qq:'',
			wechat:'',
			partner:'',
			imageCode:'',
			check1:'',
			check2:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _registerAgentDataCheck(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		mobileManage.ajax({
			url:getUrl('registerAgent'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * 修改账户资料
	 */
	that.modifyInfo = function(formData,callback){
		var _formData = {
			aliasName:'',
			qq:'',
			address:''
		};
		$.extend(_formData,formData);
		//檢查
		// return;
		var err = _modifyInfoDataCheck(formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		$.extend(formData,formData);
		mobileManage.ajax({
			url:'/asp/change_infoAjax.aspx',
			param:formData,
			callback:callback
		});
	};
	
	
	/**
	 * 账户升级
	 */
	that.userUpgrade = function(formData,callback){
		var _formData = {
			helpType:''
		};
		$.extend(_formData,formData);
		mobileManage.ajax({
			url:getUrl('checkUpgrade'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * 检查是否为安全用户
	 */
	that.checkSecurityUser = function(callback){
		mobileManage.ajax({
			url:getUrl('checkSecurityUser'),
			param:_formData,
			callback:callback
		});
	};

	/**
	 * 设定密保问题
	 */
	that.saveQuestion = function(formData,callback){
		var _formData = {
			password:'',
			answer:'',
			questionId:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _questionDataCheck(_formData);
		if(err){
			callback({success:false,message:err});
			return;
		}
		mobileManage.ajax({
			url:getUrl('saveQuestion'),
			param:_formData,
			callback:callback
		});
	};
	

	/**
	 * 查询密保问题
	 */
	that.getQuestion = function(callback){
		mobileManage.ajax({
			url:getUrl('getQuestion'),
			callback:callback
		});
	};
	
	/**
	 * 电话回波
	 */
	that.makeCall = function(formData,callback){
		var _formData = {
			phone:''
		};
		$.extend(_formData,formData);

		mobileManage.ajax({
			url:getUrl('makeCall'),
			param:_formData,
			callback:callback
		});
	};

	/**
	 * 查询代理用户统计资讯
	 */
	that.getAgentReport = function(callback){
		mobileManage.ajax({
			url:getUrl('getAgentReport'),
			callback:callback
		});
	};
	
	/**
	 * 阅读站内信
	 * @param {object} 提交资料
	 * @param {function} 回调
	 */
	that.readEmail = function(formData,callback){
		var _formData = {
			emailId:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _readEmailDataCheck(_formData);
		if(err){
			if(typeof callback === 'function'){
				callback({success:false,message:err});
			}
			return;
		}
		mobileManage.ajax({
			url:getUrl('readEmail'),
			param:_formData,
			callback:callback
		});
	};
	
	/**
	 * 未读站内信数量
	 * @param {function} 回调
	 */
	that.unreadEmailCount = function(callback){
		mobileManage.ajax({
			url:getUrl('unreadEmailCount'),
			callback:callback
		});
	};
	
	/**
	 * 查詢帳戶餘額
	 */
	that.getCredit = function(callback){
		mobileManage.ajax({
			url:getUrl('credit'),
	    	callback:callback
	    });
	};


	/**
	 * 发送语音验证码到用户注册手机
	 * @param {Function} callback 回调
	 * @public
	 */
	that.sendVoiceCodeToPhone = function(callback){
		mobileManage.openTouClick(function(tResult){
			if(tResult.success){
				mobileManage.getLoader().open("发送中");
				mobileManage.ajax({
					url:getUrl('sendVoiceCode'),
					param:{
						checkAddress:tResult.data.checkAddress.toString(),
						checkKey: tResult.data.token,
						sid: tResult.data.sid
					},
					callback:function(result){
						mobileManage.getLoader().close();
						callback(result);
					}
				});
			}else{
				alert(tResult.message);
			}
		});
	};
	
	/**
	 * 发送简讯验证码到用户注册手机
	 * @param {Function} callback 回调
	 * @public
	 */
	that.sendSmsCodeToPhone = function(callback){
		mobileManage.openTouClick(function(tResult){
			if(tResult.success){
				mobileManage.getLoader().open("发送中");
				mobileManage.ajax({
					url:getUrl('sendSmsCode'),
					param:{
						checkAddress:tResult.data.checkAddress.toString(),
						checkKey: tResult.data.token,
						sid: tResult.data.sid
					},
					callback:function(result){
						mobileManage.getLoader().close();
						callback(result);
					}
				});
			}else{
				alert(tResult.message);
			}
		});
	};
	

	/**
	 * 检查是否有重复信息
	 * @public
	 * @param {Function} callback回调
	 * 
	 */
	that.checkSameInfo = function(callback){
		mobileManage.ajax({
			url:getUrl('checkSameInfo'),
			callback:callback
		});
	};
	
	/**
	 * 检查短信回传
	 * @public
	 */
	that.checkPhoneCode = function(callback){
		mobileManage.ajax({
			url:getUrl('checkPhoneCode'),
	    	callback:callback
	    });
	};
	

	/**
	 * 检查语音短信验证码
	 * @public
	 */
	that.checkValidCode = function(formData,callback){
		var _formData = {
			imageCode:''
		};
		$.extend(_formData,formData);
		//檢查
		var err = _validCodeCheck(_formData);
		if(err){
			if(typeof callback === 'function'){
				callback({success:false,message:err});
			}
			return;
		}
		mobileManage.ajax({
			url:getUrl('checkValidCode'),
			param:_formData,
	    	callback:callback
	    });
	};
	

	/**
	 * 检查是否有重复银行卡
	 * @public
	 */
	that.checkRepeatBankCards = function(callback){
		mobileManage.ajax({
			url:getUrl('checkRepeatBankCards'),
	    	callback:callback
	    });
	};

	/**
	 * 利用token登入
	 * @public
	 */
	that.ticketLogin = function(formData,callback){
		mobileManage.ajax({
			url:getUrl('ticketLogin'),
			param:formData,
	    	callback:callback
	    });
	};
	
	//驗證資料
	function _modifyInfoDataCheck(formData){
		// console.log(formData)
		if(formData.accountName!=undefined && !/^[\u4e00-\u9fa5,\·]+$/.test(formData.accountName)){
			return '[提示]真实姓名只能为汉字！'
		}
		if(formData.email!=undefined && !formData.email){
			return '[提示]电子邮箱不可为空！'
		}
		if(formData.birthday!=undefined&& !formData.birthday){
			return '[提示]生日不可为空！'
		}
		if(formData.birthday!=undefined && !/^\d{4}\-\d{1,2}\-\d{1,2}$/.test(formData.birthday)){
			return '[提示]生日格式错误！'
		}	
		if(formData.qq!=undefined && formData.qq!=''&&!/^\d+$/.test(formData.qq)){
			return '[提示]qq只能为数字！'
		}
		return undefined;
	}
	
	//驗證資料
	function _findPasswordByEmailCheck(formData){
		if(!formData.account){
			return '[提示]账号不可为空！'
		}
		if(!formData.email){
			return '[提示]电子邮箱不可为空！'
		}

		return undefined;
	}

	//驗證資料
	function _findPasswordByPhoneCheck(formData){
		if(!formData.account){
			return '[提示]账号不可为空！'
		}
		if(!formData.phone){
			return '[提示]手机号码不可为空！'
		}
		if(/^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$/.test(formData.phone)){
			return '[提示]手机号码格式错误！'
		}
		return undefined;
	}
	
	//驗證登入資料
	function _loginDataCheck(formData){
		if(!formData.account){
			return '[提示]账号不可为空！'
		}
		if(!formData.password){
			return '[提示]密码不可为空！'
		}
		/*if(!formData.imageCode){
			return '[提示]验证码不可为空！'
		}*/
		return undefined;
	}
	
	//驗證修改密碼資料
	function _changePWSDataCheck(formData){
		if(!formData.password){
			return '[提示]用户旧密码不可为空！';
		}
		if(!formData.newPassword){
			return '[提示]用户新密码不可为空！';
		}
		if(!formData.confirmPassword){
			return '[提示]用户确认新密码不可为空！';
		}
		if(formData.newPassword!=formData.confirmPassword){
			return '[提示]两次输入的密码不一致，请核对后重新输入！';
		}
		if(!/^[a-zA-Z][a-zA-Z0-9]{5,15}$/.test(formData.newPassword)){
			return '[提示]密码为6-16位数字或英文字母，英文字母开头！';
		}
		return undefined;
	}
	
	//驗證註冊資料
	function _registerDataCheck(formData){
		if(!formData.account){
			return '[提示]登入账号不可为空！';
		}
		if(!formData.password){
			return '[提示]登入密码不可为空！';
		}
		if(!formData.confirmPassword){
			return '[提示]确认密码不可为空！';
		}
//		if(!formData.name){
//			return '[提示]姓名不可为空！';
//		}
//		if (!/^[\u4e00-\u9fa5]+$/.test(formData.name)) {
//          return "[提示]姓名只允许为汉字";
//      }
		if(!formData.phone){
			return '[提示]电话号码不可为空！';
		}
/*		if(!formData.email){
			return '[提示]电子邮箱不可为空！';
		}*/
		/*if(!formData.qq){
			return '[提示]QQ不可为空！';
		}*/ 
		if(!formData.imageCode){
			return '[提示]验证码不可为空！'
		}
		if(formData.account==formData.password){
			return '[提示]登入账号与登录密码不可为相同！';
		}
		if(formData.password!=formData.confirmPassword){
			return '[提示]两次输入的密码不一致，请核对后重新输入！';
		}
		if(!/^[a-zA-Z0-9]{6,10}$/.test(formData.account)){
			return '[提示]帐号由6-10个数字或英文字母组成！';
		}
		if(!/^[a-z][a-z0-9_]{5,16}/.test(formData.password)){
			return '[提示]密码为6-16位数字或英文字母，英文字母开头！';
		}
//		if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(formData.email)){
//			return '[提示]您输入的电子邮件地址格式有误，请核对后重新输入！';
//		}  
//		if(formData.aliasName&&formData.aliasName.length>10){
//			return '[提示]昵称格式错误！请填写10个以内的汉字、英文字母或数字！';
//		}
		if(formData.intro&&formData.intro.length){
			return '[提示]邀请码长度不允许！';
		}
		return undefined;
	}
	
	//驗證代理註冊資料
	function _registerAgentDataCheck(formData){
		if(!formData.account){
			return '[提示]登入账号不可为空！';
		}
		if(!formData.password){
			return '[提示]登录密码不可为空！';
		}
		if(!formData.confirmPassword){
			return '[提示]确认密码不可为空！';
		}
		if(!formData.name){
			return '[提示]姓名不可为空！';
		}
		if(!formData.url){
			return '[提示]代理网址不可为空！'
		}
		var regUrl = /^[0-9a-zA-Z]+$/;
		if(!regUrl.test(formData.url)){
			return '[提示]代理推广网址只能输入数字或者字母！';
		}
		if(!formData.email){
			return '[提示]电子邮箱不可为空！';
		}
		if(!formData.phone){
			return '[提示]联系电话不可为空！';
		}
		if(!formData.qq){
			return '[提示]QQ不可为空！';
		}
		if(!formData.imageCode){
			return '[提示]验证码不可为空！';
		}
		if(formData.account==formData.password){
			return '[提示]登入账号与登录密码不可为相同！';
		}
		if(formData.password!=formData.confirmPassword){
			return '[提示]两次输入的密码不一致，请核对后重新输入！';
		}
		if(!/^a\_[a-z0-9]{1,13}$/.test(formData.account)){
			return '[提示]账号以a_开头（a后面是下划线_）由3-15个数字或字母组成！';
		}
		if(!/^[a-zA-Z][a-zA-Z0-9]{5,15}$/.test(formData.password)){
			return '[提示]密码为6-16位数字或英文字母，英文字母开头！';
		}
		if(formData.phone.length != 11){
			return '[提示]输入的手机号码长度有误！';
		}
		if(!(/^1[3,4,5,7,8]\d{9}$/.test(formData.phone))){
			return '[提示]输入的手机号码有误！只支持13、14、15、17、18开头的电话号码！';
		}
		if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(formData.email)){
			return '[提示]您输入的电子邮件地址格式有误，请核对后重新输入！';
		}
		if(formData.wechat&&formData.wechat.length>20){
			return '[提示]微信长度错误！';
		}
		
		return undefined;
	}
	
	//驗證问题資料
	function _questionDataCheck(formData){
		if(!formData.password){
			return '[提示]密码不可为空！'
		}
		if(!formData.questionId){
			return '[提示]请选择密保问题！'
		}
		if(!formData.answer){
			return '[提示]请填写你的回答！'
		}
		return undefined;
	}

	//驗證问题資料
	function _readEmailDataCheck(formData){
		if(!formData.emailId){
			return '[提示]信件不存在！';
		}
		return false;
	}

	//验证码检查
	function _validCodeCheck(formData){
		if(!formData.imageCode){
			return '[提示]请输入验证码！';
		}
		return undefined;
	}
	
	//判断密码是必须是英文數组合
	function _isNumAndStr(str){
	     var regexpStr=/[a-zA-Z]+/;
	     var regexpNum=/\d+/;
	     var strFlag = regexpStr.test(str);
	     var numFlag = regexpNum.test(str);
	     if(strFlag&&numFlag)
	        return true;
	     return false;
    }
	
	/**
	 * get action url
	 */
	function getUrl(name){
		if(!_urlObj[name]){
			alert(name+' 路径不存在！');
			return;
		}
		return _base_url+_urlObj[name];
	};
	
	/**
	 * 检查app下载彩金是否符合资格
	 * @public
	 * @param {Function} callback回调
	 *  
	 */
	that.checkForAppPreferential = function(callback){
		mobileManage.ajax({
			url:getUrl('checkForAppPreferential'),
			callback:callback
		});
	};
}
