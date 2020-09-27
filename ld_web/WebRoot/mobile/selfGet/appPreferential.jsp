<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	</head>
	<body>
		<div class="content mt5">  
			<div class="appPreferential_section-area"> 
				<section>
					<div class="h3 bold center">一、帐户资料确认</div>
					<div class="space-2"></div>
					<div class="ui-form">
						<div class="ui-input-row">
							<label class="ui-label">用户帐户：</label>	
					  	  	<input type="text" readonly value="${customer.loginname}" class="ui-ipt" />
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">真实姓名：</label>	
					  	  	<input type="text" class="ui-ipt" readonly value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.accountName, 0.7,1)"/>" />
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">手机号：</label>	
					  	  	<input type="text" class="ui-ipt"  readonly value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.phone, 0.5,1)"/>" />
					    	 
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">邮箱：</label>	
					  	  	<input type="text" class="ui-ipt" readonly value="<s:property value="@dfh.utils.StringUtil@formatStar(#session.customer.email, 0.7,1)"/>"/>
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">注册IP：</label>	
					  	  	<input type="text" class="ui-ipt" readonly value="${customer.registerIp}"/>
					  	</div>
						<div class="ui-input-row">
							<label class="ui-label">此次登录IP：</label>	
					  	  	<input type="text" class="ui-ipt" readonly value="${customer.lastLoginIp}"/>
					  	</div>
					</div>
					
					<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi" style="margin-bottom:0;">
						<div class="h3 bold">温馨提示</div>
						<ol style="line-height: 200%;">
			              	<li>赢到388元即可进行提款，彩金提款是会由电话客服进行核实正确就会进行出款，如玩家非本人接听，我们将扣除红利及相关盈利。 </li>
							<li>每人只可使用一个账户参与活动，使用多个账户我们将有权收回红利以及盈利部分</li>
							<li>此次优惠每位玩家﹑每户﹑每一住址 ﹑每一电子邮箱地址﹑每一电话号码﹑相同支付方式(相同借记卡/信用卡/银行帐户姓名及号码) 及IP地址只能享有一次优惠,一旦确认为套利玩家，立即没收盈利和本金。</li>
						</ol>
					</div>
					<div class="space-2"></div>
				</section>
				
			</div>
			<div class="ui-button-row center">
					<button class="btn-login block" id="appPreferential-next-btn">下一步</button> 
			</div>
			
		</div>
		<script type="text/javascript">
			function AppPreferentialPage(){
				var that = this;
				var _$nextBtn = $('#appPreferential-next-btn');
				var _$sectionArea = $('.appPreferential_section-area');
				var _sectionHtml = '<section>{0}<div class="space-2"></div></section>';
				
				//阶段
				var _stage = 0;
				var _stageHtml = [false,
				    [
				     	[
							'<div class="h3 bold center">二、短信验证</div>',
							'<div class="space-2"></div>',
							'<div class="ui-form">',
							'	<div class="ui-input-row">',
						    '		<label class="ui-label">验证码</label>',							
						  	'  		<input type="text" id="appPreferential-phoneCode"/>',
						  	'	</div>',
						  	'</div>',
							'<div class="mui-buttons">',
							'	<button class="mui-btn mui-btn--raised mui-btn--pink" id="appPreferential-voice-btn">语音验证</button>',
							'	<button class="mui-btn mui-btn--raised mui-btn--pink" id="appPreferential-sms-btn">短信验证</button>',
							'</div>'
				     	].join(''),
				     	[
							'<div class="h3 bold center">二、短信验证</div>',
							'<div class="space-2"></div>',
							'<ol style="margin-left:30px;　line-height: 200%;">',
							'  	<li>短信安全验证(请用您注册时的手机号码发送短信 <span id= "appPreferential-phoneCode" style="color:red;"></span>。</li>',
							'  	<li>短信发送成功,请于<span style="color:red;">15</span>秒后点击<span style="color:red;">"下一步"</span>。 </li>',
							'  	<li>必须使用<span style="color:red;">注册时填写的手机号码</span>进行发送短信!。 </li>',
							'</ol>'
				    	].join('')
				    ],
					[
						'<div class="h3 bold center">三、	</div>',
						'<div class="space-2"></div>',
						'<div class="ui-form" style="margin-bottom:5rem;">',
						'	<div class="ui-input-row zf-sele">',
						'  	  	<div id="appPreferential-platform"></div>',

						'  	</div>',
						'   <div class="ui-input-row">请选择您要转入的老虎机游戏平台</div>',  
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
							_checkSameInfoAppPreferential(callback);
							break;
						case 1:
							_checkCodeAppPreferential(callback);
							break;
						case 2:
							_getAppPreferential();
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
						default:
					}
				}

				 
				/**
				 *  检查是否有重复讯息
				 * @param {Function} 回调
				 * @private
	             */
				function _checkSameInfoAppPreferential(callback) {
					mobileManage.getLoader().open('检查用户信息');
					mobileManage.getUserManage().checkForAppPreferential(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							_stage2Type = result.data.isAntiSms == 'true'?1:0;
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
				function _checkCodeAppPreferential(callback){
					if(_stage2Type==0){
						mobileManage.getLoader().open('检查验证码');
						mobileManage.getUserManage().checkValidCode({imageCode:$('#appPreferential-phoneCode').val()},function(result){
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
				
				
				function _getAppPreferential(){
					mobileManage.getLoader().open('领取');
					mobileManage.getSelfGetManage().getAppPreferential({imageCode:$('#appPreferential-phoneCode').val(),platform:that.platform.getValue()},function(result){
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
						$('#appPreferential-voice-btn').click(_sendVoiceCode);
						$('#appPreferential-sms-btn').click(_sendSmsCode);
					}else if(_stage2Type==1){
						mobileManage.getLoader().open('产生验证码');
						mobileManage.getExperienceManage().getPhoneAndCode(function(result){
							mobileManage.getLoader().close();
							if(result.success){
								var data = JSON.parse(result.data);
								$('#appPreferential-phoneCode').html(String.format('{0}到{1}',data.code,data.phone));
							}else{
								alert(result.message);
							}
						});
					}
				}


				/**PT/TTG/QT/NT/MG/DT

				 * 步骤四显示
				 * @private
                 */
				function _sectionFu2(){
					$('#m-appPreferential-bankName').attr('disabled',true);
					
					that.platform = new MobileComboBox({
						appendId:'appPreferential-platform',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'PT',name:'PT'},
							{value:'TTG',name:'TTG'},
							{value:'QT',name:'QT'},
 							{value:'NT',name:'NT'},
							{value:'MG',name:'MG'},
							{value:'DT',name:'DT'}
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