/**
 * 依赖MUI 设计的Model
 */

function MUIModel(manage,muiObj){
	if(typeof manage !=='object'){
		alert('mobileManage error');
		return;
	}
	if(typeof muiObj !=='object'){
		alert('mui error');
		return;
	}
	
	var that = this;
	//当前的model
	var _$currentModel = false;
	var _$currentId = false;

	//动作对应方法
	var _actionFn = {
		'login':_getLogin,
		'logout':_getLogout,
		'makeCall':_getMakeCall,
		'modifyPassword':_getModifyPassword,
		'question':_getQuestion,
		'news':_getNews,
		'download':_getDownload,
		'bankBind':_getBankBind,
		'goGame':_getGoGame,
		'goOrDownload':_getGoOrDownloadGame,
		'sign':_getSign,
		'zfbBind':_getZfbBind,
		'confirm':_getConfirm,
		'emigrated':_getEmigrated,
		'goGameOrFunGame':_getGoGameOrFunGame
	};
	
	var _$loginModel = false;
	var _$logoutModel = false;
	var _$makeCallModel = false;
	var _$modifyPasswordModel = false;
	var _$questionModel = false;
	var _$newsModel = false;
	var _$downloadModel = false;
	var _$bankBindModel = false;
	var _$goGameModel = false;
	var _$goOrDownloadGameModel = false;
	var _$signModel = false;
	var _$zfbDepositBindModel = false;
	var _$confirmModel = false;
	var _$emigratedModel = false;
	var _$goGameOrFunGameModel = false;

	//视窗改变时，去变更top位置
	$(window).resize(function(){
		if(_$currentModel){
			_checkHeight(_$currentModel);
		}
	});
	
	
	//登录 html 
	var _loginModelHtml = [
			'<div id="mui-login-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
	  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-arrow221">会员登陆</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
     			'<div class="mui-error-message"></div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
     		    	'<input id="mui-login-account" type="text" required>',
     		    	'<label>账号</label>',
     		    	'<div class="message"></div>',
     		 	'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
     		  	  	'<input id="mui-login-password" type="password" required>',
     		    	'<label>密码</label>',
     		  	'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
     		  	  	'<input id="mui-login-code" type="text" required>',
     		    	'<label>验证码</label>',
     		    	'<img id="mui-login-img" title="如果看不清验证码，请点图片刷新" />',
     		    	'<div class="message">如果看不清验证码，请点图片刷新</div>',
     		  	'</div>',
     		  	'<div class="mui-buttons right">',
 		  			'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-login-submit">登录</div>',
     		  		'<div class="mui-btn mui-btn--raised mui-btn--danger small" onclick="mobileManage.redirect(\'forgotPassword\');">忘记密码</div>',
     		  	'</div>',
//     		  	'<div class="mui-buttons">',
// 		  			'<div class="mui-btn mui-btn--raised mui-btn--primary " id="mui-login-submit">登录</div>',
//     		  		'<div class="mui-btn mui-btn--raised mui-btn--danger " onclick="mobileManage.redirect(\'forgotPassword\');">立即注册</div>',
//     		  	'</div>',
//     		  	'<div class="mui-buttons right">',
//	 		  		'<div onclick="manage.redirect(\'forgotPassword\');">忘记密码?</div>',
//	 		  	'</div>',
     		'</div>',
     	'</div>'
    ].join('');
	
	//登出 html 
	var _logoutModelHtml = [
		'<div id="mui-logout-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-logout13">会员登出</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
    		'<div class="mui-panel">',
    			'<div class="mui-overlay-message">确定要登出？</div>',
    		  	'<div class="mui-btn mui-btn--raised mui-btn--danger" id="mui-logout-cancel">取消</div>',
    		  	'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-logout-submit">确定</div>',
    		'</div>',
    	'</div>'
   ].join('');

	
	//电话回播 html 
	var _makeCallModelHtml = [
			'<div id="mui-makeCall-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
	  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-phone41">电话回播</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel ui-form">',
     			'<div class="mui-overlay-message"></div>',
     		  	'<div class="ui-input-row">', 
					'<label class="ui-label">您的联系号码:</label>',
     		    	'<input id="mui-makeCall-phone" type="text" class="ui-ipt" placeholder="这里输入联系号码">',
     		 	'</div>',
				'<div class="message">回拨为注册电话，点击号码回拨即可，如果回拨不是注册电话，请输入最新电话 ，点击回拨</div>',
     			'<div class="mui-overlay-message"></div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--danger" id="mui-makeCall-cancel">取消</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-makeCall-submit">电话回播</div>',
     		'</div>',
     	'</div>'
    ].join('');
	

	//修改密码 html 
	var _modifyPasswordModelHtml = [
			'<div id="mui-modifyPassword-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
	  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-refresh57">修改密码</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
     			'<div class="mui-error-message"></div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
     		    	'<input id="mui-modifyPassword-password" type="password" required>',
     		    	'<label>旧密码</label>',
     		    	'<div class="message"></div>',
     		 	'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
     		  	  	'<input id="mui-modifyPassword-newPassword" type="password" required>',
     		    	'<label>新密码</label>',
     		    	'<div class="message">密码为6-16位数字或英文字母，英文字母开头且不能和账号相同</div>',
     		  	'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
	 		  	  	'<input id="mui-modifyPassword-confirmPassword" type="password" required>',
	 		    	'<label>确认密码</label>',
     		    	'<div class="message">再次输入密码，确认新密码无误</div>',
	 		  	'</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--danger right" id="mui-modifyPassword-cancel">取消</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary right" id="mui-modifyPassword-submit">修改</div>',
     		'</div>',
     	'</div>'
    ].join('');
	
	
	//密保问题 html 
	var _questionModelHtml = [
			'<div id="mui-question-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
	  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-closed">设定密保</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
     			'<div class="mui-error-message"></div>',
     		  	'<div class="mui-select">',
     		    	'<select id="mui-question-question"></select>',
     		    	'<label>密保问题</label>',
     		 	'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
     		  	  	'<input id="mui-question-answer" type="text" required>',
     		    	'<label>你的回答</label>',
     		  	'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
	 		  	  	'<input id="mui-question-password" type="password" required>',
	 		    	'<label>登录密码</label>',
	 		  	'</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--danger right" id="mui-question-cancel">取消</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary right" id="mui-question-submit">设定</div>',
     		'</div>',
     	'</div>'
    ].join('');
	
	//新闻动态 html
	var _newsModelHtml = [
   		'<div id="mui-news-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
     		'<div class="mui-overlay-title"><div id="mui-news-title" class="mui-overlay-title-text flaticon-speechbubble96"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
       		'<div class="mui-panel">',
       			'<div id="mui-news-message" class="mui-overlay-message"></div>',
       		  	'<div class="mui-btn mui-btn--raised mui-btn--primary right" id="mui-news-next">下一条</div>',
       		  	'<div class="mui-btn mui-btn--raised mui-btn--danger right" id="mui-news-last">上一条</div>',
       		'</div>',
       	'</div>'
   	].join('');
	
	//檔案下载 html
	var _downloadModelHtml = [
     	'<div id="mui-download-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
   			'<div class="mui-overlay-title"><div id="mui-download-title" class="mui-overlay-title-text"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
     			'<div id="mui-download-message" class="mui-overlay-message"></div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--danger right" id="mui-download-cancel">取消</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary right" id="mui-download-submit">继续下载</div>',
     		'</div>',
     	'</div>'
 	].join('');
	
	//进入游戏或下载游戏客户端 html
	var _goGameOrdownloadModelHtml = [
	    '<div id="mui-download-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
   			'<div class="mui-overlay-title"><div id="mui-download-title" class="mui-overlay-title-text"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
     			'<div id="mui-download-message" class="mui-overlay-message"></div>',
     			'<div class="mui-buttons">',
	     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary small" id="mui-goGame-submit">网页游戏</div>',
	     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary small" id="mui-download-submit">继续下载</div>',
	     		'</div>',
     		'</div>',
     	'</div>'
 	].join('');
	
	//银行卡/支付宝绑定 html 
	var _bankBindModelHtml = [
		'<div id="mui-bankBind-model" class="mui-overlay-model mui-col-xs32-12 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
	  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-closed">银行卡绑定</div><div class="mui-overlay-close flaticon-symbol49">X</div></div>',
     		'<div class="mui-panel ui-form">',
 				'<div class="mui-message"><font color="red" style="font-size:80%;">注：只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。</font></div>',
     			'<div class="mui-error-message">注：只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。</div>',
     		  	'<div class="ui-input-row">',
					'<label class="ui-label">游戏账户密码：</label>',
	 		  	  	'<input id="mui-bankBind-password" type="password" required class="ui-ipt">',
	 		  	'</div>',
     		  	'<div class="ui-input-row zf-sele">',
					'<label class="ui-label">银行：</label>',
     		    	'<select id="mui-bankBind-bankName"></select>',
     		    	 
     		 	'</div>',
     		  	'<div class="ui-input-row">',
					'<label class="ui-label">银行卡号：</label>',
     		  	  	'<input id="mui-bankBind-cardNo" type="text" required class="ui-ipt"  >',
     		    	 
     		  	'</div>',
     		  	'<div class="ui-input-row" style="display:none">',
					'<label class="ui-label">验证码：</label>',	
	 		  	  	'<input id="mui-bankBind-bindingCode" type="text" required class="ui-ipt">',
	 		  	'</div>',
				'<div class="ui-button-row center">',
     		  		'<div class="btn-login block" id="mui-bankBind-submit">绑定</div>',
				'</div>',
     		'</div>',
     	'</div>'
    ].join('');

	//进入游戏 html
	var _goGameModelHtml = [
     	'<div id="mui-goGame-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
   			'<div class="mui-overlay-title"><div id="mui-goGame-title" class="mui-overlay-title-text"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
 				'<div class="mui-error-message"></div>',
     			'<div id="mui-goGame-message" class="mui-overlay-message"></div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--danger right" id="mui-goGame-cancel">取消</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary right" id="mui-goGame-submit">进入游戏</div>',
     		'</div>',
     	'</div>'
 	].join('');

	//签到 html 
	var _signModelHtml = [ 
		'<div id="mui-sign-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text">玩转天威，签到彩金天天拿</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
    		'<div class="mui-panel">',
    			'<div class="mui-overlay-message">',
	    			'签到余额：<font id="mui-sign-signMoney" color="red"></font>元 <br>',
	    			'连续签到：<font id="mui-sign-count" color="red"></font>天<font id="mui-sign-continue" color="red"></font><br>',
	    			'签到条件：', 
		            '<p style="font-size:80%">每日存款<font color="red">10</font>元以上，便会激活签到系统，每日仅能签到一次。</p><br>',
		            '签到方式：',
		            '<p style="font-size:80%">1.由下方"签到"按钮直接签到。</p><br>',
		            '<p style="font-size:80%">2.请您到每日任务里进行签到操作。</p><br>',
		            '详细活动内容，请至 <div style="color:#2C6AE0;text-decoration: underline;display: inline-block;" onclick="mobileManage.redirect(\'preferential\')">最新优惠</div>查询。',
    			'</div>',
    			'<div class="mui-buttons">',
    		  		'<div class="mui-btn mui-btn--raised mui-btn--danger" id="mui-sign-submit">签到</div>',
    			'</div>',
    		'</div>',
    	'</div>'
   ].join('');
	
	
	//支付宝扫描账号绑定
	var _zfbBindModelHtml = [
		'<div id="mui-zfbBind-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
	  		'<div class="mui-overlay-title"><div class="mui-overlay-title-text flaticon-closed">支付宝扫描账号绑定</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
     		'<div class="mui-panel">',
				'<div class="mui-error-message"></div>',
 				'<div class="mui-message"><font color="red" style="font-size:80%;">注：支付宝“二维码”扫描存款，必须用您绑定的支付宝账号进行存款，否则无法实时到账;每位会员只能绑定一个支付宝帐号</font></div>',
     			'<div class="mui-textfield mui-textfield--float-label">',
     			'<input id="mui-zfbBind-alipayAccount" type="text" required>',
     			'<label>支付宝存款账号</label>',
     			'</div>',
     		  	'<div class="mui-textfield mui-textfield--float-label">',
	 		  	  	'<input id="mui-zfbBind-password" type="password" required>',
	 		    	'<label>游戏账户密码</label>',
	 		  	'</div>',
     		  	'<div class="mui-btn mui-btn--raised mui-btn--primary block" id="mui-zfbBind-submit">绑定</div>',
     		'</div>',
     	'</div>'
    ].join('');

	//确认html 
	var _confirmModelHtml = [
		'<div id="mui-confirm-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
  		'<div class="mui-overlay-title"><div id="mui-confirm-title" class="mui-overlay-title-text"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
    		'<div class="mui-panel">',
    			'<div id="mui-confirm-message" class="mui-overlay-message" ></div>',
    			'<div class="mui-buttons">',
		  			'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-confirm-submit">确定</div>',
    		  		'<div class="mui-btn mui-btn--raised mui-btn--danger" id="mui-confirm-cancel">取消</div>',
    		  	'</div>',
    		'</div>',
    	'</div>'
   ].join('');
	
	//闯关报名
	var _emigratedModelHtml = [
		'<div id="mui-emigrated-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-8 mui-col-xs64-offset-2 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
			'<div class="mui-overlay-title"><div class="mui-overlay-title-text">全民闯关报名</div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
			'<div class="mui-panel">',
				'<div class="mui-error-message"></div>',
			  	'<div class="mui-select">',
			    	'<select id="mui-emigrated-level"></select>',
			    	'<label>闯关等级</label>',
			 	'</div>',
			 	'<div class="mui-buttons">',
			  		'<div class="mui-btn mui-btn--raised mui-btn--danger " id="mui-emigrated-submit">报名</div>',
			  	'</div>',
			'</div>',
		'</div>'
	].join('');

	//進入遊戲或試玩遊戲html 
	var _goGameOrFunGameModelHtml = [
		'<div id="mui-confirm-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
  		'<div class="mui-overlay-title"><div id="mui-goGameOrFunGame-title" class="mui-overlay-title-text"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
    		'<div class="mui-panel">',
    			'<div id="mui-goGameOrFunGame-message" class="mui-overlay-message" ></div>',
    			'<div class="mui-buttons">',
		  			'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-goGameOrFunGame-go">进入游戏</div>',
    		  		'<div class="mui-btn mui-btn--raised mui-btn--danger" id="mui-goGameOrFunGame-fun">试玩游戏</div>',
    		  	'</div>',
    		'</div>',
    	'</div>'
   ].join('');

	//进入游戏或下载游戏客户端 html
	var _goGameOrdownloadModelHtml = [
		'<div id="mui-download-model" class="mui-overlay-model mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-3 mui-col-sm-6 mui-col-sm-offset-3 mui-col-md-4 mui-col-md-offset-4">',
		'<div class="mui-overlay-title"><div id="mui-download-title" class="mui-overlay-title-text"></div><div class="mui-overlay-close flaticon-symbol49"></div></div>',
			'<div class="mui-panel">',
				'<div id="mui-download-message" class="mui-overlay-message"></div>',
				'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-download-submit">下载客户端</div>',
				'<div class="mui-btn mui-btn--raised mui-btn--primary" id="mui-goGame-submit">网页游戏</div>',
			'</div>',
		'</div>'
	].join('');
	
	/**
	 * 开启弹窗
	 * @param {string} actionName
	 * @param {array or function} argsArray
	 * @param {function} callback
	 */
	that.open = function(name,argsArray,callback){
		if(typeof _actionFn[name] !=='function'){
			alert(name+' model 不存在！')
			return ;
		}
		//不重复生成
		if(_$currentId==name){
			return;
		}
		if(argsArray&&typeof argsArray ==='object'){
			_$currentModel = _actionFn[name].apply(null,argsArray);
			_$currentModel.callback = callback;
		}else if(argsArray&&typeof argsArray ==='function'){
			_$currentModel = _actionFn[name].apply(null,[]);
			_$currentModel.callback = argsArray;
		}else{
			_$currentModel = _actionFn[name].apply(null,[]);
			_$currentModel.callback = false;
		}
		_$currentId = name;

		_$currentModel.find('.mui-overlay-close').one('click',that.close);

		muiObj.overlay('on',_$currentModel[0],{onclose:function(){
			_$currentModel.find('.mui-overlay-close').unbind('click',that.close);
			_$currentModel = false;
			_$currentId = false;
		}});
		
		_checkHeight(_$currentModel);
	};
	/**
	 * 关闭
	 */
	that.close = function(){
		muiObj.overlay('off');
	};
	
	/**
	 * 取得 登录视窗 物件
	 * @param {string} redirect 登录后，转址Key，使用mobileManage.redirect
	 */
	 function _getLogin(redirect,param){
		if(!_$loginModel){
			initLoginModel();
		}
		_$loginModel.find('input').removeClass('mui--is-dirty');
		_$loginModel.find('input').removeClass('mui--is-not-empty');
		_$loginModel.find('input').addClass('mui--is-empty');
		_$loginModel.redirect = redirect;
		_$loginModel.param = param;
		_$loginModel.$account.val('');
		_$loginModel.$password.val('');
		_$loginModel.$code.val('');
		_$loginModel.$errorMessage.html('');
		_$loginModel.$image.attr('src',manage.getSecurityCodeUrl()+'?'+Math.random());
		
		return _$loginModel;
	};
	
	/**
	 * 开启登出视窗
	 * 
	 */
	function _getLogout(){
		if(!_$logoutModel){
			initLogoutModel();
		}
		return _$logoutModel;
	};
	

	/**
	 * 开启电话回波
	 * 
	 */
	function _getMakeCall(){
		if(!_$makeCallModel){
			initMakeCallModel();
		}
		_$makeCallModel.$phone.val('');
		return _$makeCallModel;
	};
	

	/**
	 * 修改密码视窗
	 */
	function _getModifyPassword(){
		if(!_$modifyPasswordModel){
			initModifyPasswordModel();
		}
		_$modifyPasswordModel.find('input').removeClass('mui--is-dirty');
		_$modifyPasswordModel.find('input').removeClass('mui--is-not-empty');
		_$modifyPasswordModel.find('input').addClass('mui--is-empty');
		_$modifyPasswordModel.$password.val('');
		_$modifyPasswordModel.$newPassword.val('');
		_$modifyPasswordModel.$confirmPassword.val('');
		_$modifyPasswordModel.$errorMessage.html('');
		
		return _$modifyPasswordModel;
	};
	
	/**
	 * 设定密保问题
	 */
	function _getQuestion(){
		if(!_$questionModel){
			initQuestionModel();
		}
		_$questionModel.find('input').removeClass('mui--is-dirty');
		_$questionModel.find('input').removeClass('mui--is-not-empty');
		_$questionModel.find('input').addClass('mui--is-empty');
		_$questionModel.$question.val('1');
		_$questionModel.$answer.val('');
		_$questionModel.$password.val('');
		_$questionModel.$errorMessage.html('');

		return _$questionModel;
	};
	

	/**
	 * 开启公告视窗
	 * @param {object} data 传入公告资料
	 * @param {integer} active 显示该则公告
	 * 
	 */
	function _getNews(data,active){
		if(!_$newsModel){
			initNewsModel();
		}
		_$newsModel.data = data;
		_$newsModel.active = active;
		_$newsModel.$title.html(_$newsModel.data[_$newsModel.active].title);
		var content = _$newsModel.data[_$newsModel.active].content;
		content = content?content.replace(/\n/g,"<br/>"):'';
		_$newsModel.$message.html(content);
		
		return _$newsModel;
	};
	
	/**
	 * 檔案下载
	 */
	function _getDownload(data){
		if(!_$downloadModel){
			initDownloadModel();
		}
		
		_$downloadModel.data = data;
		_$downloadModel.$title.html(_$downloadModel.data.title);
		_$downloadModel.$message.html(_$downloadModel.data.content);
		
		return _$downloadModel;
	};
	
	/**
	 * 绑定银行卡/支付宝
	 */
	function _getBankBind(){
		if(!_$bankBindModel){
			initBankBindModel();
		}
		_$bankBindModel.find('input').removeClass('mui--is-dirty');
		_$bankBindModel.find('input').removeClass('mui--is-not-empty');
		_$bankBindModel.find('input').addClass('mui--is-empty');
		_$bankBindModel.$password.val('');
		_$bankBindModel.$bankName.val('');
		_$bankBindModel.$cardNo.val('');
		_$bankBindModel.$bindingCode.val('');
		_$bankBindModel.$errorMessage.html('');

		return _$bankBindModel;
	};
	
	function _getZfbBind(){
		if(!_$zfbDepositBindModel){
			initZfbBindModel();
		}
		_$zfbDepositBindModel.find('input').removeClass('mui--is-dirty');
		_$zfbDepositBindModel.find('input').removeClass('mui--is-not-empty');
		_$zfbDepositBindModel.find('input').addClass('mui--is-empty');
		_$zfbDepositBindModel.$alipayAccount.val('');
		_$zfbDepositBindModel.$password.val('');
		
		return _$zfbDepositBindModel;
	}

	/**
	 * 进入游戏
	 */
	function _getGoGame(data){
		if(!_$goGameModel){
			initGoGameModel();
		}
		
		_$goGameModel.data = data;
		_$goGameModel.$title.html(_$goGameModel.data.title);
		_$goGameModel.$message.html(_$goGameModel.data.content);
		_$goGameModel.$error.html(_$goGameModel.data.error);
		
		return _$goGameModel;
	};

	
	/**
	 * 取得 签到 物件
	 * @param {Object} param 参数
	 */
	 function _getSign(param){
		if(!_$signModel){
			initSignModel();
		}
		_$signModel.$money.html(param.money);
		_$signModel.$signMoney.html(param.signMoney);
		_$signModel.$signCount.html(param.continueSignCount);
		if('false'==param.signContinue){
			_$signModel.$signContinue.html("(连续签到已中断)");			
		}
		return _$signModel;
	};
	
	/**
	 * 取得 确认 物件
	 * @param {Object} param 参数
	 */
	 function _getConfirm(config){
		if(!_$confirmModel){
			initConfirmModel();
		}
		var _config = {
			title:'输入标题',
			message:'内容',
			callback:false
		};
		$.extend(_config,config);
		_$confirmModel.config = _config;
		_$confirmModel.$title.html(_config.title);
		_$confirmModel.$message.html(_config.message);
		return _$confirmModel;
	};
	
	function _getEmigrated(config){
		if(!_$emigratedModel){
			initEmigratedModel();
		}
		_$emigratedModel.$level.val('');
		return _$emigratedModel;
	}
	

	/**
	 * 取得 进入游戏或测试游戏 物件
	 * @param {Object} param 参数
	 */
	 function _getGoGameOrFunGame(config){
		if(!_$goGameOrFunGameModel){
			initGoGameOrFunGameModel();
		}
		var _config = {
			title:'输入标题',
			message:'内容',
			goGame:false,
			goFun:false
		};
		
		$.extend(_config,config);
		_$goGameOrFunGameModel.config = _config;
		_$goGameOrFunGameModel.$title.html(_config.title);
		_$goGameOrFunGameModel.$message.html(_config.message);
		return _$goGameOrFunGameModel;
	};
	
	
	/**
	 * 确认model height 没有超过荧幕高度，超过则不使用置中
	 * @param {object} $model 要检查的对象 
	 */
	function _checkHeight($model){
		if(($('#mui-overlay').height()-100)<$model.height()){
			$model.addClass('top');
		}else{
			$model.removeClass('top');
		}
	}
	
	/**
	 * 登录 Model 初始化
	 */
	function initLoginModel(){
		_$loginModel = $(_loginModelHtml);
		_$loginModel.$account = _$loginModel.find('#mui-login-account');
		_$loginModel.$password = _$loginModel.find('#mui-login-password');
		_$loginModel.$code = _$loginModel.find('#mui-login-code');
		_$loginModel.$image = _$loginModel.find('#mui-login-img');
		_$loginModel.$errorMessage = _$loginModel.find('.mui-error-message');
		
		_$loginModel.$image.click(function(){
			_$loginModel.$image.attr('src',manage.getSecurityCodeUrl()+'?'+Math.random());
		});
		
		_$loginModel.$submit = _$loginModel.find('#mui-login-submit');
		
		_$loginModel.$submit.click(function(){
			var formData = {
				account:_$loginModel.$account.val(),
				password:_$loginModel.$password.val(),
				imageCode:_$loginModel.$code.val()
			};
			manage.getLoader().open('验证中');
			manage.getUserManage().login(formData, function(result){
				if(result.success){
//					alert(result.message);
//					that.close();
					manage.redirect(_$loginModel.redirect?_$loginModel.redirect :'index',_$loginModel.param);
				}else{
					_$loginModel.$code.val('');
					_$loginModel.$image.attr('src',manage.getSecurityCodeUrl()+'?'+Math.random());
					_$loginModel.$errorMessage.html(result.message);
					_checkHeight(_$loginModel);
//					alert(result.message);
				}
				manage.getLoader().close();
				formData = null;
			});
		});
		
		_$loginModel.bind("keyup",function(e){  
            if(e.which=='13'&&_$loginModel.find('input').is(":focus")){
            	_$loginModel.$submit.click();
            }
        }); 
		
	}
	
	/**
	 * 登出 Model 初始化
	 */
	function initLogoutModel(){
		_$logoutModel = $(_logoutModelHtml);
		
		_$logoutModel.find('#mui-logout-cancel').click(function(){
			that.close();
		});
		_$logoutModel.find('#mui-logout-submit').click(function(){
			manage.getLoader().open('登出中');
			manage.getUserManage().logout( function(data){
				if(data.success){
					that.close();
					manage.redirect('index');
				}else{
					alert(data.message);
					manage.getLoader().close();
				}
			});
		});
	}
	
	/**
	 * 电话回播 Model 初始化
	 */
	function initMakeCallModel(){
		_$makeCallModel = $(_makeCallModelHtml);
		_$makeCallModel.$phone = _$makeCallModel.find('#mui-makeCall-phone');
		
		_$makeCallModel.find('#mui-makeCall-cancel').click(function(){
			that.close();
		});
		_$makeCallModel.$submit = _$makeCallModel.find('#mui-makeCall-submit');
		_$makeCallModel.$submit.click(function(){
			var formData = {
				phone:_$makeCallModel.$phone.val()
			};
			manage.getLoader().open('处理中');
			manage.getUserManage().makeCall(formData, function(result){
				if(result.success){
					alert(result.message);
				}else{
					alert(result.message);
				}
				manage.getLoader().close();
				formData = null;
			});
		});
		
		_$makeCallModel.bind("keyup",function(e){  
            if(e.which=='13'&&_$makeCallModel.find('input').is(":focus")){
            	_$makeCallModel.$submit.click();
            }
        }); 
	}
	
	/**
	 * 修改密码 Model 初始化
	 */
	function initModifyPasswordModel(){
		_$modifyPasswordModel = $(_modifyPasswordModelHtml);
		_$modifyPasswordModel.$password = _$modifyPasswordModel.find('#mui-modifyPassword-password');
		_$modifyPasswordModel.$newPassword = _$modifyPasswordModel.find('#mui-modifyPassword-newPassword');
		_$modifyPasswordModel.$confirmPassword = _$modifyPasswordModel.find('#mui-modifyPassword-confirmPassword');
		_$modifyPasswordModel.$errorMessage = _$modifyPasswordModel.find('.mui-error-message');
		
		_$modifyPasswordModel.find('#mui-modifyPassword-cancel').click(function(){
			that.close();
		});
		_$modifyPasswordModel.$submit = _$modifyPasswordModel.find('#mui-modifyPassword-submit');
		_$modifyPasswordModel.$submit.click(function(){
			var formData = {
				password:_$modifyPasswordModel.$password.val(),
				newPassword:_$modifyPasswordModel.$newPassword.val(),
				confirmPassword:_$modifyPasswordModel.$confirmPassword.val()
			};
			manage.getLoader().open("修改中");
			manage.getUserManage().changePassword(formData, function(result){
				manage.getLoader().close();
				if(result.success){
					alert(result.message);
					that.close();
					window.location.href='/mobile';
				}else{
					_$modifyPasswordModel.$errorMessage.html(result.message);
					_checkHeight(_$modifyPasswordModel);
					alert(result.message);
				}
			});
		});
		
		_$modifyPasswordModel.bind("keyup",function(e){  
            if(e.which=='13'&&_$modifyPasswordModel.find('input').is(":focus")){
            	_$modifyPasswordModel.$submit.click();
            }
        });
	}
	
	/**
	 * 密保问题 Model 初始化
	 */
	function initQuestionModel(){
		var questionData = [
			{value:'1',name:'您最喜欢的明星名字？'},
            {value:'2',name:'您最喜欢的职业？'},
            {value:'3',name:'您最喜欢的城市名称？'},
            {value:'4',name:'对您影响最大的人名字是？'},
            {value:'5',name:'您就读的小学名称？'},
            {value:'6',name:'您最熟悉的童年好友名字是？'}
        ];
		var optionHtml = '<option value="{0}">{1}</option>';
		var options = new Array();
		
		_$questionModel = $(_questionModelHtml);
		_$questionModel.$question = _$questionModel.find('#mui-question-question');
		_$questionModel.$answer = _$questionModel.find('#mui-question-answer');
		_$questionModel.$password = _$questionModel.find('#mui-question-password');
		_$questionModel.$errorMessage = _$questionModel.find('.mui-error-message');
		
		for(var i =0;i<questionData.length;i++){
			options.push(String.format(optionHtml,questionData[i]['value'],questionData[i]['name']));
		}
		_$questionModel.$question.append(options);
		
		_$questionModel.find('#mui-question-cancel').click(function(){
			that.close();
		});
		_$questionModel.$submit = _$questionModel.find('#mui-question-submit');
		_$questionModel.find('#mui-question-submit').click(function(){
			var formData = {
				password:_$questionModel.$password.val(),
				answer:_$questionModel.$answer.val(),
				questionId:_$questionModel.$question.val()
			};
			manage.getLoader().open("设置中");
			manage.getUserManage().saveQuestion(formData, function(result){
				manage.getLoader().close();
				if(result.success){
					alert(result.message);
					that.close();
				}else{
					alert(result.message);
					_$questionModel.$errorMessage.html(result.message);
					_checkHeight(_$questionModel);
				}
			});
		});

		_$questionModel.bind("keyup",function(e){  
            if(e.which=='13'&&_$questionModel.find('input').is(":focus")){
            	_$questionModel.$submit.click();
            }
        });
		optionHtml = options = questionData = null;
	}
	
	/**
	 * 公告信息 Model 初始化
	 */
	function initNewsModel(){
		_$newsModel = $(_newsModelHtml);
		_$newsModel.$title = _$newsModel.find('#mui-news-title');
		_$newsModel.$message = _$newsModel.find('#mui-news-message');
		
		_$newsModel.find('#mui-news-last').click(function(){
			if(_$newsModel.active>=0){
				_$newsModel.active--;
			}
	    	if(!_$newsModel.data[_$newsModel.active]){
	    		_$newsModel.$title.html('公告');
	    		_$newsModel.$message.html('无上一条公告');
	    	}else{
	    		_$newsModel.$title.html(_$newsModel.data[_$newsModel.active].title);
	    		var content = _$newsModel.data[_$newsModel.active].content;
	    		content = content?content.replace(/\n/g,"<br/>"):'';
	    		_$newsModel.$message.html(content);
	    	}
			_checkHeight(_$newsModel);
		});
		
		_$newsModel.find('#mui-news-next').click(function(){
			if(_$newsModel.active<_$newsModel.data.length){
				_$newsModel.active++;
			}
	    	if(!_$newsModel.data[_$newsModel.active]){
	    		_$newsModel.$title.html('公告');
	    		_$newsModel.$message.html('无下一条公告');
	    	}else{
	    		_$newsModel.$title.html(_$newsModel.data[_$newsModel.active].title);
	    		var content = _$newsModel.data[_$newsModel.active].content;
	    		content = content?content.replace(/\n/g,"<br/>"):'';
	    		_$newsModel.$message.html(content);
	    	}
			_checkHeight(_$newsModel);
		});
	}

	/**
	 * 檔案下载 Model 初始化
	 */
	function initDownloadModel(){

		_$downloadModel = $(_downloadModelHtml);
		_$downloadModel.$title = _$downloadModel.find('#mui-download-title');
		_$downloadModel.$message = _$downloadModel.find('#mui-download-message');

		_$downloadModel.find('#mui-download-cancel').click(function(){
			that.close();
		});
		_$downloadModel.find('#mui-download-submit').click(function(e){
			if($.isFunction(_$downloadModel.data.handler)){
				_$downloadModel.data.handler(null,[e,_$downloadModel]);
			}else{
				window.location.href = _$downloadModel.data.url;
				that.close();
			}
		});
	}
	
	/**
	 * 绑定支付宝 Model 初始化
	 */
	function initBankBindModel(){
		var bankBindData = [
		    {value:'',name:'请选择'},
			{value:'工商银行',name:'工商银行'},
			{value:'招商银行',name:'招商银行'},
			{value:'上海农村商业银行',name:'上海农村商业银行'},
			{value:'农业银行',name:'农业银行'},
			{value:'建设银行',name:'建设银行'},
			{value:'交通银行',name:'交通银行'},
			{value:'民生银行',name:'民生银行'},
			{value:'光大银行',name:'光大银行'},
			{value:'兴业银行',name:'兴业银行'},
			{value:'上海浦东银行',name:'上海浦东银行'},
			{value:'广东发展银行',name:'广东发展银行'},
			{value:'深圳发展银行',name:'深圳发展银行'},
			{value:'中国银行',name:'中国银行'},
			{value:'中信银行',name:'中信银行'},
			{value:'邮政银行',name:'邮政银行'}
        ];
		var optionHtml = '<option value="{0}">{1}</option>';
		var options = new Array();
		
		_$bankBindModel = $(_bankBindModelHtml);
		_$bankBindModel.$bankName = _$bankBindModel.find('#mui-bankBind-bankName');
		_$bankBindModel.$cardNo = _$bankBindModel.find('#mui-bankBind-cardNo');
		_$bankBindModel.$password = _$bankBindModel.find('#mui-bankBind-password');
		_$bankBindModel.$bindingCode = _$bankBindModel.find('#mui-bankBind-bindingCode');
		_$bankBindModel.$errorMessage = _$bankBindModel.find('.mui-error-message');
		_$bankBindModel.$submit = _$bankBindModel.find('#mui-bankBind-submit');
		
		for(var i =0;i<bankBindData.length;i++){
			options.push(String.format(optionHtml,bankBindData[i]['value'],bankBindData[i]['name']));
		}
		_$bankBindModel.$bankName.append(options);

		_$bankBindModel.find('#mui-bankBind-cancel').click(function(){
			that.close();
		});
		
		_$bankBindModel.$submit.click(function(){
			 var bdbankno=_$bankBindModel.$cardNo.val();
			if(bdbankno==""){
				alert("[提示]银行卡号不可为空！");
				return false;
			}
			var regUrl = /^\d+$/;
			// if(!regUrl.test(bdbankno)){
			// 	alert( '[提示]银行卡号只能输入数字！');
			// 	return false;
			// }
			// if(bdbankno.length>30||bdbankno.length<10){
			// 	alert("[提示]银行卡号长度只能在10-30位之间");
			// 	return false;
			// }
			
			var formData = {
				cardNo:_$bankBindModel.$cardNo.val(),
				bankName:_$bankBindModel.$bankName.val(),
				password:_$bankBindModel.$password.val(),
				addr:'none',
				bindingCode:_$bankBindModel.$bindingCode.val()
			};
			manage.getLoader().open("绑定中");
			manage.getBankManage().bindBankNo(formData, function(result){
				manage.getLoader().close();
				if(result.success){
					alert(result.message);
					that.close();
				}else{
					alert(result.message);
					_$bankBindModel.$errorMessage.html(result.message);
					_checkHeight(_$bankBindModel);
				}
			});
		});
		
		_$bankBindModel.bind("keyup",function(e){  
            if(e.which=='13'&&_$bankBindModel.find('input').is(":focus")){
            	_$bankBindModel.$submit.click();
            }
        });
		optionHtml = options = bankBindData = null;
	}
	
	/**
	 * 进入游戏 Model 初始化
	 */
	function initGoGameModel(){

		_$goGameModel = $(_goGameModelHtml);
		_$goGameModel.$title = _$goGameModel.find('#mui-goGame-title');
		_$goGameModel.$message = _$goGameModel.find('#mui-goGame-message');
		_$goGameModel.$error = _$goGameModel.find('.mui-error-message');

		_$goGameModel.find('#mui-goGame-cancel').click(function(){
			that.close();
		});
		
		_$goGameModel.find('#mui-goGame-submit').click(function(e){

			if(_$goGameModel.data.onSubmit){
				_$goGameModel.data.onSubmit.apply(_$goGameModel,[e,_$goGameModel]);
			}else{
				if(!_$goGameModel.data||!_$goGameModel.data.url){
					_$goGameModel.$error.html('游戏路径不存在！');
					alert('游戏路径不存在！')
					return;
				}
				window.location.href = _$goGameModel.data.url;
				that.close();
			}
		});
	}
	

	/**
	 * 签到 Model 初始化
	 */
	function initSignModel(){
		_$signModel = $(_signModelHtml);
		_$signModel.$money = _$signModel.find('#mui-sign-money');
		_$signModel.$signMoney = _$signModel.find('#mui-sign-signMoney');
		_$signModel.$signCount = _$signModel.find('#mui-sign-count');
		_$signModel.$signContinue = _$signModel.find('#mui-sign-continue');
		_$signModel.find('#mui-sign-cancel').click(function(){
			that.close();
		});
		_$signModel.find('#mui-sign-submit').click(function(){
			manage.getLoader().open('处理中');
			manage.getSignManage().doSign(function(result){
				if(result.success){
					alert(result.message);
					that.close();
				}else{
					alert(result.message);
				}
				manage.getLoader().close();
			});
		});
	}

	/**
	 * 支付宝扫马 Model 初始化
	 */
	function initZfbBindModel(){
		_$zfbDepositBindModel = $(_zfbBindModelHtml);
		_$zfbDepositBindModel.$errorMessage = _$zfbDepositBindModel.find('.mui-error-message');
		_$zfbDepositBindModel.$alipayAccount = _$zfbDepositBindModel.find("#mui-zfbBind-alipayAccount");
		_$zfbDepositBindModel.$password = _$zfbDepositBindModel.find("#mui-zfbBind-password");
		_$zfbDepositBindModel.$submit = _$zfbDepositBindModel.find("#mui-zfbBind-submit");
		
		_$zfbDepositBindModel.find('#mui-zfbBind-cancel').click(function(){
			that.close();
		});

		_$zfbDepositBindModel.$submit.click(function(){
			manage.getLoader().open("绑定中");
			manage.getBankManage().bindZFBQR({
					account:_$zfbDepositBindModel.$alipayAccount.val(),
					password:_$zfbDepositBindModel.$password.val()
			},function(result){
				manage.getLoader().close();
				_$zfbDepositBindModel.$errorMessage.html(result.message);
				_checkHeight(_$zfbDepositBindModel);
				alert(result.message);
			});
		});
		
		_$zfbDepositBindModel.bind("keyup",function(e){  
            if(e.which=='13'&&_$zfbDepositBindModel.find('input').is(":focus")){
            	_$zfbDepositBindModel.$submit.click();
            }
        }); 
	}

	/**
	 * 确认 Model 初始化
	 */
	function initConfirmModel(){
		_$confirmModel = $(_confirmModelHtml);
		_$confirmModel.$title = _$confirmModel.find('#mui-confirm-title');
		_$confirmModel.$message = _$confirmModel.find('#mui-confirm-message');
		
		_$confirmModel.find('#mui-confirm-cancel').click(function(){
			that.close();
			if(typeof _$confirmModel.config.callback === 'function'){
				_$confirmModel.config.callback(false);
			}
		});
		
		_$confirmModel.find('#mui-confirm-submit').click(function(){
			that.close();
			if(typeof _$confirmModel.config.callback === 'function'){
				_$confirmModel.config.callback(true);
			}
		});
	}
	/**
	 * 闯关 Model 初始化
	 */
	function initEmigratedModel(){
		_$emigratedModel = $(_emigratedModelHtml);
		_$emigratedModel.$errorMessage = _$emigratedModel.find('.mui-error-message');
		_$emigratedModel.find('#mui-emigrated-submit').click(function(){
			mobileManage.getLoader().open('处理中');
			mobileManage.getEmigratedManage().apply({emigratedeLevel:_$emigratedModel.$level.val()},function(result){
				mobileManage.getLoader().close();
				_$emigratedModel.$errorMessage.html(result.message); 
				alert(result.message);
			});
		});
		
		var data = [
			{value:'',name:'请选择'},
			{value:'1',name:'天威-不屈白银'},
			{value:'2',name:'天威-荣耀黄金'},
			{value:'3',name:'天威-华贵铂金'},
			{value:'4',name:'天威-璀璨钻石'},
			{value:'5',name:'天威-最强王者'}
		]
		var optionHtml = '<option value="{0}">{1}</option>';
		var options = new Array();
		
		_$emigratedModel.$level = _$emigratedModel.find('#mui-emigrated-level');
		for(var i =0;i<data.length;i++){
			options.push(String.format(optionHtml,data[i]['value'],data[i]['name']));
		}
		_$emigratedModel.$level.append(options);
		data = optionHtml = options = null;
	}
	

	/**
	 * 确认 Model 初始化
	 */
	function initGoGameOrFunGameModel(){
		_$goGameOrFunGameModel = $(_goGameOrFunGameModelHtml);
		_$goGameOrFunGameModel.$title = _$goGameOrFunGameModel.find('#mui-goGameOrFunGame-title');
		_$goGameOrFunGameModel.$message = _$goGameOrFunGameModel.find('#mui-goGameOrFunGame-message');

		_$goGameOrFunGameModel.find('#mui-goGameOrFunGame-go').click(function(){
			that.close();
			if(typeof _$goGameOrFunGameModel.config.goGame === 'function'){
				_$goGameOrFunGameModel.config.goGame();
			}
		});

		_$goGameOrFunGameModel.find('#mui-goGameOrFunGame-fun').click(function(){
			that.close();
			if(typeof _$goGameOrFunGameModel.config.goFun === 'function'){
				_$goGameOrFunGameModel.config.goFun();
			}
		});
	}

	/**
	 * 进入游戏或下载客户端
	 */
	function _getGoOrDownloadGame(data){
		if(!_$goOrDownloadGameModel){
			initGoOrDownloadGameModel();
		}

		_$goOrDownloadGameModel.data = data;
		_$goOrDownloadGameModel.$title.html(_$goOrDownloadGameModel.data.title);
		_$goOrDownloadGameModel.$message.html(_$goOrDownloadGameModel.data.content);
		_$goOrDownloadGameModel.$goGame.html(_$goOrDownloadGameModel.data.goGameText||'网页游戏');
		_$goOrDownloadGameModel.$download.html(_$goOrDownloadGameModel.data.downloadText||'下载客户端');
		return _$goOrDownloadGameModel;
	}

	/**
	 * 初始化 进入游戏或下载APP Model
	 */
	function initGoOrDownloadGameModel(){
		_$goOrDownloadGameModel = $(_goGameOrdownloadModelHtml);
		_$goOrDownloadGameModel.$title = _$goOrDownloadGameModel.find('#mui-download-title');
		_$goOrDownloadGameModel.$message = _$goOrDownloadGameModel.find('#mui-download-message');
		_$goOrDownloadGameModel.$goGame = _$goOrDownloadGameModel.find('#mui-goGame-submit');
		_$goOrDownloadGameModel.$download = _$goOrDownloadGameModel.find('#mui-download-submit');

		_$goOrDownloadGameModel.$download.click(function(e){
			if($.isFunction(_$goOrDownloadGameModel.data.goDownloadFn)){
				_$goOrDownloadGameModel.data.goDownloadFn.apply(null,[e,_$goOrDownloadGameModel]);
			}else{
				window.location.href = _$goOrDownloadGameModel.data.download_url;
				that.close();
			}
		});
		_$goOrDownloadGameModel.$goGame.click(function(e){
			if($.isFunction(_$goOrDownloadGameModel.data.goGameFn)){
				_$goOrDownloadGameModel.data.goGameFn.apply(null,[e,_$goOrDownloadGameModel]);
			}else{
				window.location.href = _$goOrDownloadGameModel.data.game_url;
				that.close();
			}
		});
	}
}