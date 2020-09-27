<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/new/agent.jsp");
	}
%>
<!DOCTYPE>
<html>
		<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="体验金" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<style>
			.form-tips {
				font-size: 15px;
				font-weight: bold;
				margin-bottom: 0!important;
				margin-left: 0.333333rem;
			}
			.form-tips small {
				font-weight: lighter;
				display: block;
				line-height: 0.533333rem;
				color: #666!important;
			}
			.img{height: 2.08rem;width: 100%;}
			.down-qrcode{line-height: 0.8rem; background: #fff;border-radius: 0.133333rem; text-align: center; padding: 0.333333rem 0;}
			.down-qrcode img{width: 2.426666rem;height: 2.426666rem;}
			.down-qrcode .save{display:block; margin: 0 auto; height: 0.933333rem;line-height:0.933333rem;text-align: center;
				border-radius: 0.133333rem;background: #dfa85a; width: 5.173333rem;background:#dfa85a;color:#fff}
			.content{text-align: center;margin: 0 auto; width: 80%;}
		</style>
	</head>
	<body>
		<div class="content mt5">
			<div class="section-area">
				<section>
					<div class="h3 tyjsty">注册信息</div>
					<div class="space-2"></div>
					<div class="ui-form">
						<div class="ui-input-row">
							<label class="ui-label">用户昵称：</label>	
					  	  	<input type="text" readonly  class="ui-ipt" value="${customer.loginname}" />
					    	 
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">真实姓名：</label>	
					  	  	<input type="text" readonly  class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1)"/>" />
					    	  
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">手机号：</label>	
					  	  	<input type="text" readonly  class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.phone, 0.5,1)"/>" />
					    	 
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">邮箱：</label>	
					  	  	<input type="text" readonly  class="ui-ipt" value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.email, 0.7,1)"/>"/>
					    	 
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">注册IP：</label>	
					  	  	<input type="text" readonly  class="ui-ipt" value="${customer.registerIp}"/>
					    	 
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">此次登录IP：</label>	
					  	  	<input type="text" readonly  class="ui-ipt" value="${customer.lastLoginIp}"/>
					    	 
					  	</div>
					</div>
					<div class="space-2"></div>
				</section>
			</div>
			<div class="ui-button-row center">
				<button class="btn-login block" id="experience-next-btn">下一步</button>
			</div>
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
				<div class="h3" style="font-size: 16px; margin-top: 10px; margin-bottom: 6px;">温馨提示：</div>
				<ol>
	              	<li style=" font-size: 16px ;color:red;line-height: 24px;">体验金提款需核实，请留意您的注册手机号，若未接听则取消提款，扣除额度</li>
	               
				</ol>
			</div>
			
		</div>
		<script type="text/javascript">
			function ExperiencePage(){
				var that = this;
				var _$nextBtn = $('#experience-next-btn');
				var _$sectionArea = $('.section-area');
				var _sectionHtml = '<section>{0}<div class="space-2"></div></section>';
				
				//阶段
				var _stage = 0;
				var _stageHtml = [false,
				    [
				     	[
							'<div class="h3 bold center">二、填写电话号码</div>',
							'<div class="space-2"></div>',
							'<div class="ui-form">',
							'	<div class="ui-input-row">',
						    '		<label class="ui-label">验证码：</label>',							
						  	'  		<input type="text" class="ui-ipt" id="experience-phoneCode"/>',
						  	'	</div>',
						  	'</div>',
							'<div class="ui-form">',
							'	<div class="mui-buttons">',
							'		<button class="mui-btn mui-btn--raised mui-btn--danger" id="experience-voice-btn">语音验证</button>',
							'		<button class="mui-btn mui-btn--raised mui-btn--danger" id="experience-sms-btn">短信验证</button>',
							'	</div>',
							'</div>'
				     	].join(''),
				     	[
							'<div class="h3 bold center">二、填写电话号码</div>',
							'<div class="space-2"></div>',
							'<ol style="margin-left:30px;　line-height: 200%;">',
							'  	<li>短信安全验证(请用您注册时的手机号码发送短信 <span id= "experience-phoneCode" style="color:red;"></span>。</li>',
							'  	<li>短信发送成功,请于<span style="color:red;">15</span>秒后点击<span style="color:red;">"下一步"</span>。 </li>',
							'  	<li>必须使用<span style="color:red;">注册时填写的手机号码</span>进行发送短信!。 </li>',
							'</ol>'
				    	].join('')
				    ],
					[
						'<div class="h3 bold center">三、银行卡号验证</div>',
						'<div class="space-2"></div>',
						'<div class="ui-form">',
						'	<div class="ui-input-row zf-sele">',
						'		<label class="ui-label">取款银行：</label>',						
						'		<div id="experience-bankName"></div>',
						'	</div>',
						'	<div class="ui-input-row">',
						'		<label class="ui-label">银行卡号：</label>',						
						'		<input id="experience-cardno" class="ui-ipt" type="text" readonly>',
						'	</div>',
						'</div>',
						'<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">',
						'	<div class="h3 bold">温馨提示</div>',
						'   <ol>',
						' 	 	<li>此处只为查看银行绑定信息，不用填写，直接点击下一步即可。</li>',
						'	</ol>',
						'</div>'
					].join(''),
					[
						'<div class="h3 bold center">四、领取</div>',
						'<div class="space-2"></div>',
						'<div class="ui-form">',
						'	<div class="ui-input-row zf-sele">',
						'  	  	<div id="experience-platform"></div>',
						'    	<label>请选择您要转入的老虎机游戏平台</label>',
						'  	</div>',
						'</div>'
					].join(''),
					
				];
				
				//阶段2的类型
				var _stage2Type = 0;

				_init();
				
				/**
				 * 初始化
				 * @private
				 */
				function _init(){
					_$nextBtn.click(_nextEvent);
				}
				
				/**
				 * 下一步按钮事件
				 * @private
				 */
				function _nextEvent(){
					_doAction(_doActionCallback);
				}
				
				/**
				 * 执行该接段动作
				 * @private
				 * @param {Function} callback 回调
				 */
				function _doAction(callback){
					var next = false;
					switch(_stage){
						case 0:
							_checkSameInfo(callback);
							break;
						case 1:
							_checkCode(callback);
							break;
						case 2:
							_checkRepeatBankCards(callback);
							break;
						case 3:
							_getExperience();
							break;
						default:
					}
				}
				 
				/**
				 * 执行动作回调
				 * @private
				 * @param {Object} result 是否继续执行
				 */
				function _doActionCallback(result){
					if(result.success){
						_stage++;
						alert(result.message);
						if(_stage==0)return;
						_addNextSection(_addNextSectionCallback);
					}else{
						alert(result.message);
					}
				}
				
				/**
				 * 增加下一个区块
				 * @private
				 * @param {Function} callback 回调
				 */
				function _addNextSection(callback){
					if(_stageHtml[_stage]){
						if(_stage==1){//因为短信验证有两种方式
							_$sectionArea.append(String.format(_sectionHtml,_stageHtml[_stage][_stage2Type]));
						}else{
							_$sectionArea.append(String.format(_sectionHtml,_stageHtml[_stage]));
						}
						callback(true);
					}else{
						callback(false);
					}
				}

				
				/**
				 * 增加下一个区块回调
				 * @private
				 * @param {Boolean} status 成功/失败
				 */
				function _addNextSectionCallback(status){
					if(!status)return;
					switch(_stage){
						case 1:
							_sectionFu1();
							break;
						case 2:
							_sectionFu2();
							break;
						case 3:
							_sectionFu3();
							break;
						default:
					}
				}

				 
				/**
				 *  检查是否有重复讯息
				 * @param {Function} 回调
				 * @private
	             */
				function _checkSameInfo(callback) {
					mobileManage.getLoader().open('检查用户信息');
					mobileManage.getUserManage().checkSameInfo(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							_stage2Type = result.message == 'success'?0:1;
							result.message = '用户信息没问题！';
							callback(result);
						}else{
							callback(result);
						}
					});
				}
				
				/**
				 * 检查手机验证码
				 * @param {Function} 回调
				 * @private
                 */
				function _checkCode(callback){
					if(_stage2Type==0){
						mobileManage.getLoader().open('检查验证码');
						mobileManage.getUserManage().checkValidCode({imageCode:$('#experience-phoneCode').val()},function(result){
							mobileManage.getLoader().close();
							callback(result);
						});
					}else if(_stage2Type==1){
						mobileManage.getLoader().open('检查验证码');
						mobileManage.getUserManage().checkPhoneCode(function(result){
							mobileManage.getLoader().close();
							callback(result);
						});
					}
				}
				
				/**
				 * 检查是否有重复银行卡
				 * @param {Function} 回调
				 * @private
                 */
				function _checkRepeatBankCards(callback){
					mobileManage.getLoader().open('检查是否有重复银行卡');
					mobileManage.getUserManage().checkRepeatBankCards(function(result){
						mobileManage.getLoader().close();
						callback(result);
					});
				}
				
				/**
				 * 检查是否有重复银行卡
				 * @private
                 */
				function _getExperience(){
					mobileManage.getLoader().open('领取');
					mobileManage.getExperienceManage().getExperience({imageCode:$('#experience-phoneCode').val(),platform:that.platform.getValue()},function(result){
						mobileManage.getLoader().close();
						alert(result.message);
					});
				}
				 
				/**
				 * 步骤二显示
				 * @private
                 */
				function _sectionFu1(){
					if(_stage2Type==0){
						$('#experience-voice-btn').click(_sendVoiceCode);
						$('#experience-sms-btn').click(_sendSmsCode);
					}else if(_stage2Type==1){
						mobileManage.getLoader().open('产生验证码');
						mobileManage.getExperienceManage().getPhoneAndCode(function(result){
							mobileManage.getLoader().close();
							if(result.success){
								var data = JSON.parse(result.data);
								$('#experience-phoneCode').html(String.format('{0}到{1}',data.code,data.phone))
							}else{
								alert(result.message);
							}
						});
					}
				}

				/**
				 * 步骤三显示
				 * @private
                 */
				function _sectionFu2(){
					$('#experience-voice-btn').remove();
					$('#experience-sms-btn').remove();
					$('#experience-phoneCode').attr('readonly',true);
					
					//银行资料
					var bankData = [
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

					//银行下拉
					var bankNameCbo = new MobileComboBox({
						appendId:'experience-bankName',
						valueName:'value',
						displayName:'name',
						datas:bankData,
						onChange:function(e){
							if(!bankNameCbo.getValue())return;
							mobileManage.getLoader().open("载入中");
							mobileManage.getBankManage().getBankDataByName({bankName:bankNameCbo.getValue()},function(result){
								if(result.success){
									$('#experience-cardno').val(result.data.number);
								}else{
									alert(result.message);
								}
								mobileManage.getLoader().close();
							});
						}
					});
					
				}

				/**
				 * 步骤四显示
				 * @private
                 */
				function _sectionFu3(){
					$('#m-experience-bankName').attr('disabled',true);
					
					that.platform = new MobileComboBox({
						appendId:'experience-platform',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'PT',name:'PT'},
							{value:'MG',name:'MG'},
							{value:'DT',name:'DT'},
							{value:'TTG',name:'TTG'},
							{value:'NT',name:'NT'},
							{value:'QT',name:'QT'}
						],
						onChange:function(e){
							
						}
					});
					_$nextBtn.html('领取');
					
				}
				
				/**
				 * 发送语音验证码
				 * @private
                 */
				function _sendVoiceCode(){
					mobileManage.getUserManage().sendVoiceCodeToPhone(function(result){
						alert(result.message);
					});
				}
				
				/**
				 * 体验金 发送短信验证码
				 * @private
                 */
				function _sendSmsCode(){
					mobileManage.getExperienceManage().sendSmsCodeToPhone(function(result){
						alert(result.message);
					});
				}
			}
		</script>
	</body>
</html>