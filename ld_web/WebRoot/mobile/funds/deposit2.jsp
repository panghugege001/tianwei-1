<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

	<!--内容-->  
						<ul>
							<li>
								<div class="title fast_find_a" id="deposit-faster"><a data-header-tab="" data-toggle="tab" data-title="秒存转账" href="#deposit-fast1">秒存转账 支付宝，网银加赠<span class="c-red">0.5%</span><i class="list-arrow iconfont icon-arrow-right"></i></a></div> 
							</li>
							<li>
								<div class="title" id="deposit-zfbQR2"><a data-toggle="tab" href="#deposit-zfbpay" aria-expanded="false">支付宝在线支付<i class="list-arrow iconfont icon-arrow-right"></i></a></div> 
								
							</li>

							<li>
								<div class="title" id="deposit-weixin"><a data-toggle="tab" href="#deposit-weixin1" aria-expanded="false">微信支付<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
								
							</li>
							<li>  
								<div class="title" id="deposit-WXValidate" ><a data-toggle="tab" href="#deposit-wxdeposit" aria-expanded="false">微信存款额度验证<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
								
							</li>
							<li>
								<div class="title" id="deposit-speedPay" ><a data-toggle="tab" href="#deposit-quick" aria-expanded="false">快捷支付<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
								
							</li>
 
							<li>
								<div class="title" id="deposit-thirdPay"><a data-toggle="tab" href="#deposit-three" aria-expanded="false">第三方支付<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
								
							</li>
							<li>
								<div class="title" id="deposit-card"><a data-toggle="tab" href="#deposit-card1" aria-expanded="false">点卡支付<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="deposit-fast1" class="page tab-panel tab-top">

		<div class="tab-bd">
			<div class="tab-panel active" id="deposit-fast-page-1">
				<div class="ui-form">
					<h2 class="h2 c-red center">支付宝，网银存款加赠0.5%</h2>
					<div class="ui-input-row zf-sele">
						<label class="ui-label">银行种类</label>
						<div id="deposit-fast-type"></div>
					</div>
					<div class="ui-input-row">
						<label class="ui-label">存款姓名</label>
						<input class="ui-ipt" id="deposit-fast-name" type="text" placeholder="请输入您的存款姓名">
					</div>
					<!--<div id="selectpay" class="ui-input-row zf-sele">
						<label class="ui-label">存款银行</label>
						<input class="ui-ipt" id="deposit-fast-bank" type="text">
					</div>
					<div id="card" class="ui-input-row">
						<label class="ui-label">存款卡号</label>
						<input class="ui-ipt" id="deposit-fast-card" type="text"  data-rule-digits="true" minlength="16" maxlength="20" placeholder="请输入您的存款卡号...">
					</div>-->
					<div class="ui-input-row">
						<label class="ui-label">存款金额</label>
						<input class="ui-ipt" id="deposit-fast-money" type="text" data-rule-digits="true" maxlength="10"  placeholder="存款金额额度为1元-300万">
					</div>
					<div class="ui-input-row">
						<label class="ui-label"><b style="color:red">请您输入小数点，以确保实时到账！（如10.11）</b></label>
					</div>
					<div class="ui-button-row center">
						<!--<a data-toggle="tab" href="#deposit-fast-history" id="btn-history" class="btn btn-info mui-btn mui-btn--raised mui-btn--danger">历史银行记录</a>-->
						<button id="deposit-fast-submit" class="btn mui-btn mui-btn--raised mui-btn--danger" >下一步</button>
					</div>
					<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
						<div class="h3"><strong>温馨提示：</strong></div>
						<ol>
							<li>选择存款方式，填写存款信息，填完点击下一步。</li>
							<li>存款资料仅需填写一次，系统会自动保存供您下次选择。</li>
							<li>支付宝转账招商23：00-凌晨01：00有延迟到帐现象，期间建议您使用其他支付方式！</li>
						</ol>
					</div>

				</div>
			</div>

			<div id="deposit-fast-page-2" class="page tab-panel tab-top">
				<!--极速转账-->
				<div class="tab-content" >
					<div class="ui-form">
						<div class="h2 c-red mb40 center">我们的收款账户</div>
						<div class="ui-input-row">
							<label class="ui-label">收款银行</label>
							<input class="ui-ipt" type="text" readonly disabled="disabled"  id="sbankname" >
						</div>
						<div class="ui-input-row">
							<label class="ui-label">收款账号</label>
							<input class="ui-ipt" type="text"  id="saccountno" readonly>
							<button class="btn_copy mui-btn mui-btn--raised mui-btn--danger" onclick='copyToClipboard(document.getElementById("saccountno"))'>复制</button>
						</div>
						<div class="ui-input-row">
							<label class="ui-label">收款人姓名</label>
							<input class="ui-ipt" type="text" id="saccountname" readonly >
							<button class="btn_copy mui-btn mui-btn--raised mui-btn--danger" onclick='copyToClipboard(document.getElementById("saccountname"))'>复制</button>
						</div>
						<div id="fyan" class="ui-input-row">
							<label class="ui-label">附言</label>
							<input class="ui-ipt" id="mefuyan" type="text" readonly>
							<div id="fyantip" class="ui-tip">请在“转账用途”或“附言”处填写您的附言，款项才能实时到账！</div>
							<button class="btn_copy mui-btn mui-btn--raised mui-btn--danger" onclick='copyToClipboard(document.getElementById("mefuyan"))'>复制</button>
						</div>
						<div class="space-2"></div>
					</div>
					<div class="border-bottom"></div>
					<div class="ui-form">
						<h2 class="h2 mb40 c-red center">您的存款信息</h2>
						<div class="ui-input-row">
							<label class="ui-label">存款方式</label>
							<input class="ui-ipt" type="text" id="quick-confirm-type" readonly disabled="disabled">
						</div>
						<div class="ui-input-row">
							<label class="ui-label">存款姓名</label>
							<input class="ui-ipt" type="text" id="quick-confirm-username" readonly disabled="disabled">
						</div>
						<!--<div id="ckyh" class="ui-input-row">
							<label class="ui-label">存款银行</label>
							<input class="ui-ipt" type="text" id="quick-confirm-bank" readonly disabled="disabled">
						</div>
						<div id="ckkh" class="ui-input-row">
							<label class="ui-label">存款卡号</label>
							<input class="ui-ipt" type="text" id="quick-confirm-card" readonly disabled="disabled">
						</div>-->
						<div class="ui-input-row">
							<label class="ui-label">存款金额</label>
							<input class="ui-ipt" type="text" id="quick-confirm-money" readonly disabled="disabled">
						</div>

						<div class="ui-button-row center">
							<button id="deposit-fast-success" class="btn btn-login btn-block" >我已成功付款</button>
						</div>

						<div class="hint-info">
							<h3>温馨提示：</h3>
							<ol>
								<li>请务必按照系统提示消息进行存款，银行卡转账“附言”必须填写，支付宝转账无需附言完成之后请点击“我已成功存款”，否则您的款项将无法及时到账</li>
								<li>如果您的款项10分钟未能到账，请联系24小时在线客服！</li>
							</ol>
						</div>
					</div>
				</div>

			</div>
		</div>

	</div>
		<div id="deposit-zfbpay" class="page tab-panel tab-top" >
			<!--支付宝在线-->
			<div class="tab-content ui-form">
				<div class="space-2"></div>
					<div class="ui-input-row zf-sele">
						<label class="ui-label">选择通道：</label>	
						<div id="deposit-zfbQR2-pay" ></div>
						 
					</div>
					<div class="ui-input-row">
						<label class="ui-label">存款金额：</label>	
						<input id="deposit-zfbQR2-money" class="ui-ipt" type="text" placeholder="0">
					 
					</div>
					<div class="ui-button-row center">
						<div class="block btn-login" id="deposit-zfbQR2-submit">开始充值</div>
					</div>
					<div class="space-2"></div>
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
					<div class="h3"><strong>温馨提示：</strong></div>
					<ol>
						<li>最低存款额度<font color="red">1</font>元，最高<font color="red">3000</font> 元。</li>
						<li>在线支付存款需承担<font color="red">0.9%</font>的手续费，手续费由第三方收取。</li>
					</ol>
					<div class="space-2"></div>
				</div>
			</div>
		</div>
		<div id="deposit-weixin1" class="page tab-panel tab-top" >
			<!--微信在线-->
			<div class="tab-content ui-form" >
				<div class="space-2"></div>
					<div class="ui-input-row zf-sele">
						<label class="ui-label">选择通道：</label>	
						<div id="deposit-weixin-pay"></div>
					</div>
					<div class="ui-input-row">
						<label class="ui-label">存款金额：</label>	
						<input id="deposit-weixin-money" class="ui-ipt" type="text" placeholder="0.00"> 
					</div>
					<div class="ui-button-row center">
						<div class="block btn-login" id="deposit-weixin-submit">确定支付</div>
					</div>
					<div id="deposit-weixin-message" class="weixin-message"></div>
				 
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
					<div class="h3"><strong>温馨提示：</strong></div>
					<ol id="deposit-weixin-tips">
						<li>单笔最低存款<font color="red">2</font>元，最高存款<font color="red">3000</font> 元。</li> 
						<li>使用极速微信支付需要承担2.4%的手续费，其他微信通道需承担0.8%手续费，手续费由第三方收取。</li>
						<li>请不要使用微信绑定的信用卡进行微信支付！如果使用信用卡支付，导致金额不能及时到账，我方恕不负责！</li>
						<li>若支付成功未及时到账，请联系在线客服处理！</li>
						
					</ol>
					<div class="space-2"></div>
				</div>
			</div>
		</div>
		<div id="deposit-wxdeposit" class="page tab-panel tab-top" >
			<div class="tab-content ui-form">
				<div class="space-2"></div>
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-5" id="deposit_WXValidate_error_msg"></div>
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-5" id="deposit_WXValidate_pay_content">
					<div class="space-3"></div> 
					<div class="ui-input-row" id="WXValidate-input-area">
						<label class="ui-label">存款金额：</label>	
						<input id="deposit-WXValidate-amount" onkeypress="return mykeypress(this, event);" class="ui-ipt" maxlength="7" type="text" placeholder="只能输入两位小数" >
						 
						 
					</div>
					<div class="ui-button-row center">
						<div class="block btn-login" id="deposit-WXValidate-submit">提交</div>	
						<div class="block btn-login" id="deposit-WXValidate-discard" style="display:none">废除订单</div>						  	
					</div>
					<div id="depositResult"></div>
					<div id="deposit-WXValidate-payInfo" style="text-align:center;margin:10px;"></div>
				</div>
				<div class="space-2"></div>
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-6 tishi">
					<div class="h3"><strong>温馨提示：</strong></div>
					<ol>
						<li><font color="red">此通道不收手续费。</font></li>
						<li>单笔最低存款10元，最高存款5000。</li>
						<li>生成订单如果金额有误，请自行作废此订单重新提交。</li>
						<li>请务必输入2小位数金额存款，举例您希望存10元，填写10.88(两小位数)后，在您转账时也必须输入10.88元支付才可以到账，否则无法到账。</li>
						<li>请您按照官网提示的额度进行存款否则无法快速到帐，如有疑问可以随时联系在线客服进行谘询。</li>
						<li><font color="red">图表存款说明</font><img style="width:100%;" src="/mobile/images/wxvalidate.png?v=1"></li>
					</ol>
					<div class="space-2"></div>
				</div>
				
			</div>
		</div>
		<div id="deposit-quick" class="page tab-panel tab-top" >
			<!--快捷存款-->
			<div class="tab-content ui-form">
				<div class="space-2"></div>
					<div class="ui-input-row">
						<label class="ui-label">充值金额：</label>		
						<input id="deposit-speedPay-money" class="ui-ipt" type="text" placeholder="0">
					</div>
					<div class="ui-button-row center">
						<div class="block btn-login" id="deposit-speedPay-submit">开始充值</div>
					</div>
					<div class="space-2"></div>
			 
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs64-5 tishi">
					<div class="space-2"></div>
					<div class="h3"><strong>温馨提示：</strong></div>
					<ol>
						<li>
							单笔存款最低额度为1元，最高5000元。
						</li>
						<li>
							如有疑问请联系QQ客服： 800134430。
						</li>
					</ol>
					<div class="space-2"></div>
				</div>
			</div>
		</div>
	 
		<div id="deposit-three" class="page tab-panel tab-top" >
			<!--第三方-->
			<div class="tab-content ui-form">
				<div class="space-2"></div>
				 
					<div class="ui-input-row">
						<label class="ui-label">充值金额：</label>	
						<input id="deposit-thirdPay-money" type="text" class="ui-ipt" placeholder="0.00"> 
					</div>
					<div class="ui-input-row zf-sele">
						<label class="ui-label">在线支付：</label>		
						<div id="deposit-thirdPay-pay"></div>
					</div>
					<div class="ui-input-row zf-sele">
						<label class="ui-label">银行种类：</label>	
						<div id="deposit-thirdPay-bank"></div>
					</div>
					<div class="ui-button-row center">
						<div class="btn-login block" id="deposit-thirdPay-submit">开始充值</div>
					</div>
					<div class="space-2"></div>
				 
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-6 mui-col-xs64-offset-0 tishi">
					<div class="space-2"></div>
					<div class="h3"><strong>温馨提示：</strong></div>
					<font color="red" >通过第三方支付进行存款的会员，如果出现掉单的情况，请及时联系在线客服，并提供订单号，存款金额，存款时间，以便我们尽快为您补单。单笔存款最低额度为1元，最高50000元。</font>
					<div class="space-2"></div>
				</div>
			</div>
		</div>
		<div id="deposit-card1" class="page tab-panel tab-top" >
			<div class="tab-content ui-form" >
				<div class="space-2"></div>
				<div id="deposit-card-form" style="text-align:center;color:red;"></div>
				
				<div class="mui-col-xs32-10 mui-col-xs32-offset-1  mui-col-xs64-6 mui-col-xs64-offset-0">
					<div class="space-2"></div>
					<div class="h3"><strong>温馨提示：</strong></div>
					<font color="red" >建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的。到账金额比实际存款金额少。到账比例 请查看费率表。</font>
					<div class="space-2"></div>
					<table >
							<tbody>
								<tr><th colspan="2">点卡支付业务费率</th></tr>
								<tr>
								<th>点卡卡类型</th>
								<th>费率</th>
								</tr>
								<tr>
									<td align="center">移动神州行</td>
								<td align="center">0.05</td>  
								</tr>
								<tr>
									<td align="center">电信国卡</td>
								<td align="center">0.05</td>
								</tr>
								<tr>
									<td align="center">QQ币充值卡</td>
									<td align="center">0.14</td>
								</tr>
								<tr>
									<td align="center">联通一卡通</td>
									<td align="center">0.05</td>
								</tr>
								<tr>
									<td align="center">骏网一卡通</td>
								<td align="center">0.16</td>
								</tr>                               
								<tr>
									<td align="center">完美一卡通</td>
								<td align="center">0.14</td>
								</tr>
								<tr>
									<td align="center">征途一卡通</td>
								<td align="center">0.13</td>
								</tr>
								<tr>
									<td align="center">网易一卡通</td>
								<td align="center">0.14</td>
								</tr>
								<tr>
									<td align="center">搜狐一卡通</td>
								<td align="center">0.16</td>
								</tr>
								<tr>
									<td align="center">久游一卡通</td>
								<td align="center">0.2</td>
								</tr>
								<tr>
									<td align="center">天宏一卡通</td>
								<td align="center">0.17</td>
								</tr>	
								<tr>
									<td align="center">天下一卡通</td>
								<td align="center">0.18</td>
								</tr>
								<tr>
									<td align="center">天下一卡通专项</td>
								<td align="center">0.19</td>			 
								</tr>
								<tr>
									<td align="center">盛付通一卡通</td>
								<td align="center">0.14</td>		 
								</tr>                               																
						</tbody>
					</table>
					<div class="space-2"></div>
				</div> 
			</div> 
	 
	 
	<script type="text/javascript">

	/*复制*/   
		function copyToClipboard(elem) {
			// create hidden text element, if it doesn't already exist
			var targetId = "_hiddenCopyText_";
			var isInput = elem.tagName === "INPUT" || elem.tagName === "TEXTAREA";
			var origSelectionStart, origSelectionEnd;
			if(isInput) {
				// can just use the original source element for the selection and copy
				target = elem;
				origSelectionStart = elem.selectionStart;
				origSelectionEnd = elem.selectionEnd;
			} else {
				// must use a temporary form element for the selection and copy
				target = document.getElementById(targetId);
				if(!target) {
					var target = document.createElement("textarea");
					target.style.position = "absolute";
					target.style.left = "-9999px";
					target.style.top = "0";
					target.id = targetId;
					document.body.appendChild(target);
				}
				target.textContent = elem.textContent;
			}
			// select the content
			var currentFocus = document.activeElement;
			target.focus();
			target.setSelectionRange(0, target.value.length);
			// copy the selection
			var succeed;
			try {
				succeed = document.execCommand("copy");
			} catch(e) {
				succeed = false;
			}
			// restore original focus
			if(currentFocus && typeof currentFocus.focus === "function") {
				currentFocus.focus();
			}
			if(isInput) {
				// restore prior selection
				elem.setSelectionRange(origSelectionStart, origSelectionEnd);
			} else {
				// clear temporary content
				target.textContent = "";
			}
			alert("复制成功请粘贴")
			return succeed;
			
			
		}    

		function DepositPage(){
			//设定只能输入数字
			NumberInput('deposit-thirdPay-money');
			//取得上次存款模式
			var depositMode = mobileManage.getSessionStorage('fundsManage').depositMode||'0';
			
			var _$view = $('.deposit-content');
			var _deposit = {};
			
			var _$titles = $('ul>li>.title');
			 
			_$titles.bind('click',_titleClickEvent);
		 
			function _titleClickEvent(){
				 $(".fast_find_a").find("a").click(function(){
					$("#page-index").removeClass("active");
					$("#deposit-fast-history").removeClass("active")
					$("#deposit-fast-page-1").addClass("active")
				})

				var _$titles = $(this);
				if(_$titles.parent().hasClass('active')){
					_$titles.parent().removeClass('active');
				}else{
					//关闭其他打开的
					_$view.find('ul>li.active').removeClass('active');
					_$titles.parent().addClass('active');
				}
				
				if(!_deposit[this.id]){
					switch(this.id){
						case 'deposit-zfbQR1':
							_deposit[this.id] = new _ZFBQR1Manage();
							break;
						case 'deposit-faster':
							_deposit[this.id] = new _FastManage();
							break;
						case 'deposit-thirdPay':
							_deposit[this.id] = new _ThirdPayManage();
							break;
						case 'deposit-weixin':
							_deposit[this.id] = new _WeixinManage();
							break;
						case 'deposit-card':
							_deposit[this.id] = new _DCardManage();
							break;
						case 'deposit-zfbRemark':
							_deposit[this.id] = new _ZFBRemarkManage();
							break;
						case 'deposit-zfbQR2':
							_deposit[this.id] = new _ZFBQR2Manage();
							break;
						case 'deposit-speedPay':
							_deposit[this.id] = new _SpeedPayManage();
							break;
						case 'deposit-WXValidate':
							_deposit[this.id] = new _WXValidateManage();
							break;
						default:
					}
				}
			}
			
			_init();
			/**
			 * 初始化
			 */
			function _init(){
				mobileManage.getLoader().open('初始化');
				mobileManage.getTPPManage().getAllPayments(function(result){
					mobileManage.getLoader().close();
					if(!result.success)return;
					
					filterPayPage(result.data);
					
				});
			}

			/**
			 * 支付方式开关
			 */ 
			function filterPayPage(data){
				/**
				 * 支付方式开关
				 */ 
				var payPageArr = {
//	 				'deposit-zfbQR1':['汇潮'],
//	 				'deposit-zfbRemark':['汇潮'],
//	 				'deposit-thirdPay':['汇潮'],
					'deposit-zfbQR2':['口袋支付','优付支付宝','新贝支付宝','银宝支付宝','千网支付宝','口袋支付宝2','迅联宝支付宝'],
					'deposit-WXValidate':['微信额度验证'],
					'deposit-weixin':['口袋微信支付','新贝微信','乐富微信','智付微信','智付微信1','口袋微信支付2','口袋微信支付3','迅联宝','优付微信','千网微信'],
					'deposit-card':['智付点卡1'],
					'deposit-speedPay':['汇潮']
				};
				
				var open = {};
				for(var i in payPageArr){
					open[i] = false;
					for(var j in data){
						if(payPageArr[i].indexOf(data[j])!=-1){
							open[i] = true;
							break;
						}
					}
				}
				//移除没有使用的
				for(var i in open){
					if(!open[i]&&_$view.find('#'+i).length>0){
						_$view.find('#'+i).parent().remove();
					}
				}
			}
			
			/*
			*极速转帐Manage
			*/
	 function _FastManage() {
        var _fast = this;
        var _bankInfos;
        var _nextFlag = false;


        var _testMode = 0;

        _fast.$btnHistory=$('#btn-history');
        _fast.$btnFast=$("#deposit-fast-submit");
        _fast.$pageOne=$('#deposit-fast-page-1');
        _fast.$pageTwo=$('#deposit-fast-page-2');
        _fast.$pageHistory=$('#deposit-fast-history');

        // 存款方式
        _fast.fastBank = new MobileComboBox({
            appendId: 'deposit-fast-type',
            cls: 'ui-select',
            valueName: 'value',
            displayName: 'name',
            datas: [
                {value: '2', name: '支付宝转账', bankType: '支付宝转账'},
                {value: '0', name: '手机银行转账', bankType: '手机银行转账'},
                {value: '1', name: '网上银行转账', bankType: '网上银行转账'}
            ],
            onChange: function (e) {
                var $that = $("#m-deposit-fast-type");
                var value = $that.find("option:selected").val();

                if (value == "2") {
                    $("#card").hide();
                    $("#selectpay").hide();
                } else {
                    $("#card").show();
                    $("#selectpay").show();
                }
            }
        });



        _fast.init=function () {
            //设置默认选中的方式为 支付宝
            _fast.fastBank.setValue('2');
            $("#card").hide();
            $("#selectpay").hide();

            _fast.$btnHistory.click(bankHistory);
        };

        _fast.setPageActive=function ($ele) {
            $ele.addClass('active').siblings().removeClass('active')
        }

        // 存款銀行
       /* _fast.fastBank = new MobileComboBox({
            appendId: 'deposit-fast-bank',
            cls: '',
            valueName: 'value',
            displayName: 'name',
            datas: [
                {value: '', name: '请选择', bankType: '请选择'},
                {value: '中国工商银行', name: '中国工商银行', bankType: '中国工商银行'},
                {value: '中国建设银行', name: '中国建设银行', bankType: '中国建设银行'},
                {value: '中国交通银行', name: '中国交通银行', bankType: '中国交通银行'},
                {value: '中国招商银行', name: '中国招商银行', bankType: '中国招商银行'},
                {value: '中国农业银行', name: '中国农业银行', bankType: '中国农业银行'},
                {value: '中国民生银行', name: '中国民生银行', bankType: '中国民生银行'},
                {value: '中国银行', name: '中国银行', bankType: '中国银行'},
                {value: '中国邮政', name: '中国邮政', bankType: '中国邮政'},
                {value: '光大银行', name: '光大银行', bankType: '光大银行'},
                {value: '广发银行', name: '广发银行', bankType: '广发银行'},
                {value: '兴业银行', name: '兴业银行', bankType: '兴业银行'}
            ],
            onChange: function (e) {

            }
        });*/


        // 下一步
        _fast.$btnFast.click(function (e) {
            e.preventDefault();

            var msg = "";
            var saveType = $("#m-deposit-fast-type").val();
            var saveName = $("#deposit-fast-name").val();
            //var saveCard = $("#deposit-fast-card").val();
            //var saveBank = $("#m-deposit-fast-bank").find(":selected").val();
            var saveMoney = $("#deposit-fast-money").val();

            if (msg == "" && !saveType || saveType == "") {
                msg = "[提示]请选择存款方式!";
                alert(msg)
                return false;
            }

            if (msg == "" && !saveName || saveName == "") {
                msg = "[提示]请填写您的存款姓名!";
                alert(msg)
                return false;
            }

            var reg = /^[1-9]\d*$/;
            if (isNaN(saveMoney)){
                msg = "[提示]请输入正确的存款金额!";
            }

            // 晚上11-凌晨1.提示招商银行
            var objDate = new Date();
            var nowTime = objDate.getHours();
            if (saveType == 2 && nowTime > 22 || saveType == 2 && nowTime < 2) {
            //if (true) {
                var d = dialog({
                    title: '温馨提示',
                    content: '温馨提示：23:00-01:00为招行每日清算时间，该时段转账至招行可能会有延迟，清算完毕即可到账，或使用其他存款方式，是否确认继续存款。',
                    width:320,
                    okValue: '继续支付',
                    ok: function () {
                        nextStep(e)
                    },
                    cancelValue: '选择其他方式',
                    cancel: function () {
                        window.location.reload();
                    }
                });
                d.showModal();

            } else {
                nextStep(e)
            }

        });
        function nextStep(e) {
            e.preventDefault();

            var msg = "";
            var saveType = $("#m-deposit-fast-type").val();
            var saveName = $("#deposit-fast-name").val();
            //var saveCard = $("#deposit-fast-card").val();
            //var saveBank = $("#m-deposit-fast-bank").find(":selected").val();
            var saveMoney = $("#deposit-fast-money").val();

            if (msg == "" && !saveType || saveType == "") {
                msg = "[提示]请选择存款方式!";
                alert(msg)
                return false;
            }

            if (msg == "" && !saveName || saveName == "") {
                msg = "[提示]请填写您的存款姓名!";
                alert(msg)
                return false;
            }

            var reg = /^[1-9]\d*$/;
            if (isNaN(saveMoney)) {
                msg = "[提示]请输入正确的存款金额!";
            }

            //如果存款方式是支付宝转账
            if (saveType == "2") {
                if (msg == "" && !saveMoney || saveMoney == "") {
                    msg = "[提示]请选择存款金额!";
                }
            } else {

               /* if (msg == "" && !saveBank || saveBank == "") {
                    msg = "[提示]请选择存款银行!";
                }

                if (msg == "" && !saveCard || saveCard == "") {
                    msg = "[提示]请选择存款卡号!";
                }

                if (!reg.test(saveCard)) {
                    msg = "[提示]存款卡号不得包含汉字或符号!";
                }

                if (msg == "" && saveCard.length < 16 || saveCard.length > 20) {
                    msg = "[提示]存款卡号长度错误!";
                }*/
            }

            if (msg != "") {
                alert(msg);
                return false;
            }
            else {
                /*if (saveType == 2) {
                    saveBank = "支付宝";
                }

                if (saveBank == "请选择") {
                    saveBank = "";
                }*/

                var formData={
                    "banktype": saveType,
                    "uaccountname": saveName,
                    /*"ubankname": saveBank,
                    "ubankno": saveCard,*/
                    "amount": saveMoney
                };

                if(_nextFlag == false){
                    mobileManage.getLoader().open('处理中');
                    $.ajax({
                        type: 'POST',
                        url: "/asp/getNewdeposit.aspx",
                        //url: "/data/getNewdeposit.json",
                        dataType: "json",
                        data: formData,
                        error: function (response) {
                            alert(response);
                        },
                        success: function (response) {
                            mobileManage.getLoader().close();

                            var massage = response.massage;
                            if(!massage){
                                _creatDepositInfo(response);

                            }else{
                                if (response['force'] === true) {

                                    var d = dialog({
                                        title: '温馨提示',
                                        content: '您有订单未完成支付，请您先核实。若未支付，需要重新建立订单，请先作废之前订单!请您按照您输入的存款信息进行存款，方可实时到账！！！',
                                        width:320,
                                        okValue: '作废订单',
                                        ok: function () {
                                            formData['force'] = true;
                                            mobileManage.getLoader().open('处理中');
                                            $.post('/asp/getNewdeposit.aspx', formData, function (response) {
                                                mobileManage.getLoader().close();
                                                if (!response.massage) {
                                                    _creatDepositInfo(response);
                                                } else {
                                                   alert(massage);
                                                }
                                            }).fail(function () {
                                                mobileManage.getLoader().close();
                                                //showTips('获取信息异常');
                                            });
                                        },
                                        cancelValue: '取消',
                                        cancel: function () {

                                        }
                                    });
                                    d.showModal();

                                }
                                else {
                                    alert(massage);
                                    mobileManage.getLoader().close();
                                    return;
                                }

                            }
                        },
                        fail: function(response){
                            alert("维护中");
                        }
                    });


                    function _creatDepositInfo(jsonData){
                        $("#deposit-fast-submit").hide().attr("disabled","disabled");
                        _nextFlag = true;

                        var massage = jsonData.massage;
                        if (massage == null || massage == '' || massage == undefined) {

                            $("#deposit-fast-page-1").hide();
                            $("#deposit-fast-page-2").show();

                            getQuickSaveUserInfo();
                            setQuickSaveUserInfo();

                            $("#sbankname").val(jsonData.bankname);
                            $("#saccountno").val(jsonData.accountno);
                            $("#saccountname").val(jsonData.username);
                            $("#mefuyan").val(jsonData.zfbImgCode);

                        }
                        else {
                            $("#deposit-fast-page-1").show();
                            $("#deposit-fast-page-2").hide();

                            alert(massage);
                        }
                    };
                }


            }
        };

        // 取得玩家填寫存款信息
        function getQuickSaveUserInfo(){

            quickSaveData = {
                "type" : $("#m-deposit-fast-type").val(),
                "type_cn" : $("#m-deposit-fast-type option:selected").text(),
                "username": $("#deposit-fast-name").val(),
                //"bank": $("#m-deposit-fast-bank").val(),
               // "card": $("#deposit-fast-card").val(),
                "money": $("#deposit-fast-money").val(),
                "depositCode": $("#quick-save-depositCode").val()
            };
        }

        // 配置玩家填寫存款信息
        function setQuickSaveUserInfo(){
            $("#quick-confirm-type").val(quickSaveData["type_cn"]);
            if(quickSaveData["type_cn"].indexOf("银行") >=0){
                //$("#quick-confirm-depositCode").val(quickSaveData["depositCode"]);
                $("#ckyh").show();
                $("#ckkh").show();
                $("#fyan").show();
            }
            else{
                $("#ckyh").hide();
                $("#ckkh").hide();
                $("#fyan").hide();
            }
            $("#quick-confirm-money").val(quickSaveData["money"]);
            $("#quick-confirm-username").val(quickSaveData["username"]);
            $("#quick-confirm-bank").val(quickSaveData["bank"]);
            $("#quick-confirm-card").val(quickSaveData["card"]);
        }

        function bankHistory() {

            var _$historyBankModal=$("#historyBankList");
            var _testMode = 0;


            if (_testMode == 1) {
                var jsonData = '{"pageNumber":1,"totalPages":12,"size":10,"pageContents":[{"depositId":"esqrbd","loginname":"james","accountname":"汤书兵","bankname":"招商银行","bankno":"6214837906643162","status":0,"createtime":"Feb 26, 2017 12:00:00 AM","ubankname":"中国交通银行","uaccountname":"123","ubankno":"12345678901234567","amount":123.0,"flag":1,"type":"1"},{"depositId":"sxg7xazi","loginname":"james","accountname":"徐安江","bankname":"招商银行","bankno":"6214832708697649","status":2,"createtime":"Feb 24, 2017 12:00:00 AM","updatetime":"Feb 24, 2017 12:00:00 AM","ubankname":"中国工商银行","uaccountname":"尼玛","ubankno":"1556451546481434","amount":10.0,"flag":1,"type":"0"},{"depositId":"68j78a97","loginname":"james","accountname":"苏德喜","bankname":"招商银行","bankno":"6214832017301412","status":2,"createtime":"Feb 24, 2017 12:00:00 AM","updatetime":"Feb 24, 2017 12:00:00 AM","ubankname":"支付宝","uaccountname":"1","ubankno":"","amount":2.0,"flag":1,"type":"2"},{"depositId":"mywkv","loginname":"James","accountname":"冷祥富","bankname":"支付宝","bankno":"lengfengzhong6@sina.com","status":2,"createtime":"Aug 9, 2016 12:00:00 AM","updatetime":"Feb 24, 2017 12:00:00 AM","flag":1}],"statics1":0.0,"statics2":0.0,"totalRecords":112,"numberOfRecordsShown":4,"jsPageCode":"共112条,每页10条,当前1/12\u0026nbsp;首页\u0026nbsp;上一页\u0026nbsp;\u003ca href\u003d\u0027javascript:gopage(2)\u0027\u003e下一页\u003c/a\u003e\u0026nbsp;\u003ca href\u003d\u0027javascript:gopage(12)\u0027\u003e末页\u003c/a\u003e\u0026nbsp;到第\u003cselect name\u003d\u0027page\u0027 onchange\u003d\u0027javascript:gopage(this.options[this.selectedIndex].value)\u0027\u003e\u003coption value\u003d\u00271\u0027 selected\u003e1\u003c/option\u003e\u003coption value\u003d\u00272\u0027\u003e2\u003c/option\u003e\u003coption value\u003d\u00273\u0027\u003e3\u003c/option\u003e\u003coption value\u003d\u00274\u0027\u003e4\u003c/option\u003e\u003coption value\u003d\u00275\u0027\u003e5\u003c/option\u003e\u003coption value\u003d\u00276\u0027\u003e6\u003c/option\u003e\u003coption value\u003d\u00277\u0027\u003e7\u003c/option\u003e\u003coption value\u003d\u00278\u0027\u003e8\u003c/option\u003e\u003coption value\u003d\u00279\u0027\u003e9\u003c/option\u003e\u003coption value\u003d\u002710\u0027\u003e10\u003c/option\u003e\u003coption value\u003d\u002711\u0027\u003e11\u003c/option\u003e\u003coption value\u003d\u002712\u0027\u003e12\u003c/option\u003e\u003c/select\u003e页"}';
                _callback(jsonData);
            } else {

                mobileManage.ajax({
                    url:'/asp/queryDepositBank.aspx',
                    param:'',
                    callback:_callback
                });


            }

            function _callback(data) {

                if (data && typeof data != 'undefined') {
                    var pageContents = data.pageContents;

                    var html = "";
                    if (pageContents.length > 0) {
                        var pageLength = (pageContents.length > 5) ? 5 : pageContents.length;
                        for (var i = 0; i < pageLength; i++) {

                            var type = (pageContents[i].type) ? pageContents[i].type : "";
                            // var bankname = (pageContents[i].bankname) ? pageContents[i].bankname : "";
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
                            html += "<input type=\"button\" value=\"选中\" class=\"btn quick-save-choose-btn small\" data-type='" + type + "' data-name='" + uaccountname + "' data-bank='" + ubankname + "' data-card='" + ubankno + "'>";
                            html += "<input type=\"button\" value=\"删除\" class=\"btn quick-save-delete-btn small\" data-depositid='" + depositid + "' data-loginname='" + loginname + "' data-card='" + ubankno + "'>";
                            html += "</td>";

                            html += "</tr>";
                        }
                    }


                    _$historyBankModal=$("#historyBankList");
                    _$historyBankModal.find('tbody').html(html);
                    setChooseBtn();
                    setDeleteBtn();



                    function setChooseBtn(){
                        _$historyBankModal.find(".quick-save-choose-btn").click(function () {

                            var type = $(this).data("type");
                            var name = $(this).data("name");
                            var bank = $(this).data("bank");
                            var card = $(this).data("card");
                            $("#deposit-fast-card").val(card);

                            if (type == "2") {
                                $("#deposit-fast-card").hide();
                                $("#deposit-fast-card").val("");
                                $("#card").hide();
                                $("#selectpay").hide();
                            } else {
                                $("#deposit-fast-card").show();
                                $("#selectpay").show();
                                $("#card").show();
                            }

                            $("#m-deposit-fast-type").val(type);
                            $("#deposit-fast-name").val(name);

                            // 下拉式選單
                            $("#m-deposit-fast-bank option").each(function () {
                                var text = $(this).text();
                                if (text == bank) {
                                    $(this).attr("selected", "selected");
                                }
                            });

                            _fast.setPageActive(_fast.$pageOne);
                        });
                    }

                    function setDeleteBtn(){
                        _$historyBankModal.find(".quick-save-delete-btn").click(function () {
                            var $that = $(this);
                            var $parent = $that.parents("tr");

                            var card = $(this).data("card");
                            var loginname = $(this).data("loginname");
                            var depositid = $(this).data("depositid");

                            var formData = {
                                "loginname": loginname,
                                "ubankno": card,
                                "depositId": depositid
                            };
                            mobileManage.ajax({
                                url:'/asp/updateDepositBank.aspx',
                                param:formData,
                                callback:function(data){
                                    if(data){
                                        $parent.remove();
                                    }
                                }
                            });

                        });
                    }

                }
            }


        }

        $("#deposit-fast-success").click(function(e){
            e.preventDefault();
            window.location.reload();
        });

        _fast.init();
    }
 
			
			//第三方支付Manage
			function _ThirdPayManage(){
				var _thridPay = this;
				
				//支付方式
				_thridPay.thirdPayPay = new MobileComboBox({
					appendId:'deposit-thirdPay-pay',
					cls:'',
					valueName:'value',
					displayName:'name',
					datas:[{value:'',name:'请选择'}],
					onChange:function(e){
						_queryPayBank();
					}
				});
				
				//银行
				_thridPay.thirdPayBank = new MobileComboBox({
					appendId:'deposit-thirdPay-bank',
					cls:'',
					valueName:'value',
					displayName:'name',
					datas:[{value:'',name:'请选择'}],
					onChange:function(e){
						
					}
				});
				
				//查詢支付方式下拉資料
				function queryPay(){
					var filterPay = ['汇潮'];
					mobileManage.getLoader().open('载入中');
					mobileManage.getTPPManage().queryPayDatas(function(result){
						if(result.success){
							var data = [{name: "请选择", value: ""}];
							for(var i in result.data){
								if(filterPay.indexOf(result.data[i].value)==-1){
									data.push(result.data[i]);
								}
							}
							_thridPay.thirdPayPay.loadData(data);
							_thridPay.thirdPayPay.setValue(_thridPay.thirdPayPay.getValue());
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}
				
				
				//第三方支付
				function _doThirdPay(){
					var formData = {
						payId:_thridPay.thirdPayPay.getValue(),
						money:$('#deposit-thirdPay-money').val(),
						bankName:_thridPay.thirdPayBank.getValue()
					};
					mobileManage.getLoader().open("处理中");
					mobileManage.getTPPManage().pay(formData, function(result){
						if(result.success){
							
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}

				//取得银行
				function _queryPayBank(){
					mobileManage.getTPPManage().queryBankDataByPayId(_thridPay.thirdPayPay.getValue(),function(result){
						if(result.success){
							_thridPay.thirdPayBank.loadData(result.data);
						}
					});
				}

				$('#deposit-thirdPay-submit').click(_doThirdPay);
				queryPay();
			}
			
			//额度验证Manage
			function _ATMManage(){
				var _atm = this;
				var _rowHtml = [
					'<div class="mui-textfield">',
				  	'  	<input id="deposit-atm-money" type="text" value="{1}" readonly>',
				    '	<label>{0}</label>',
				  	'</div>',
				].join('');
				_atm.doATM=function(){
					var formData = {
						type:'atm',
						money:$('#deposit-atm-money').val()
					};
					mobileManage.getLoader().open("处理中");
					mobileManage.getBankManage().deposit(formData, function(result){
						if(result.success){
							$('#validateAmountDepositResult').text('我们能够为您处理的金额为：'+result.data.amount+' 元。 请确保存入该指定金额，否则会导致存款无法到帐。');
							
							var html = String.format(_rowHtml,'账户名',result.data.userName);
							html+=String.format(_rowHtml,'开户行',result.data.bankName);
							html+=String.format(_rowHtml,'账号',result.data.accountNo);
							html+=String.format(_rowHtml,'存款验证QQ',result.data.QQ);
							$("#depositBankInfoDiv").html(html);
							html = null;
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				};
			}
			
			//支付宝
			function _ZFBQR1Manage(){
				var that = this;
				that.account = $('#deposit-zfbQR1-account');
				that.image = $('#deposit-zfbQR1-image');
				that.button = $('#deposit-zfbQR1-bind');
				that.button.click(function(){
					mobileManage.getModel().open('zfbBind');
				});

				//查询支付宝绑定
				mobileManage.getLoader().open('载入中');
			    mobileManage.getBankManage().getZFBQR(function(result){
		    		if(result.success){
		    			if(result.data.bind){
			    			that.account.val(result.data.account);
			    			if(result.data.auth){
				    			that.image.append('<img style="width:200px;" src="'+result.data.image+'"/>');
			    			}else{
			    				that.image.append('<div style="color:red;text-align:left;padding:5px;">'+result.message+'</div>');
			    			}
			    			that.button.html('修改绑定');
		    			}else{
			    			that.button.html('绑定');
		    			}
		    		}else{
		    			alert(result.message)
		    		}
					mobileManage.getLoader().close();
			    });
			}

			//支付宝在线支付
			function _ZFBQR2Manage(){
				var _zfbqr2 = this;
				_zfbqr2.$money = $('#deposit-zfbQR2-money');
				_zfbqr2.$submit = $('#deposit-zfbQR2-submit');				
				_zfbqr2.$submit.click(_submit);
				
				//支付方式
				_zfbqr2.zfbqr2Pay = new MobileComboBox({
					appendId:'deposit-zfbQR2-pay',
					cls:'',
					valueName:'value',
					displayName:'name',
					datas:[{value:'',name:'请选择'}],
					onChange:function(e){
					}
				});
				
				//查詢支付方式下拉資料
				function _queryPay(){
					mobileManage.getLoader().open('载入中');
					mobileManage.getTPPManage().queryZfbqr2(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							if(result.data.length>0){
								result.data.unshift({value:'',name:'请选择'});
								_zfbqr2.zfbqr2Pay.loadData(result.data);
							}else{
								_zfbqr2.zfbqr2Pay.loadData([{value:'',name:'维护中'}]);
							}
						}else{
							alert(result.message);
						}
					});
				}
				
				function _submit(){
					var formData = {
						payId:_zfbqr2.zfbqr2Pay.getValue(),
						money:_zfbqr2.$money.val(),
						bankName:_zfbqr2.zfbqr2Pay.getValue()
					};
					/* if('新贝支付宝'==_zfbqr2.zfbqr2Pay.getValue()){
						if(!checkWXZFBMoney(_zfbqr2.$money.val())){
							alert('请存款两位小数金额。如10.26、5.30、6.02');
							return;
						}
					} */
					mobileManage.getLoader().open("处理中");
					mobileManage.getTPPManage().pay(formData, function(result){
						if(result.success){
							
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}
				
				_queryPay();
			}
			
			//微信Manage
			function _WeixinManage(){
				var _weixin = this;
				var _$money = $('#deposit-weixin-money');
				
				$('#deposit-weixin-submit').click(_doPay);
				
				//支付方式
				_weixin.weixinPay = new MobileComboBox({
					appendId:'deposit-weixin-pay',
					cls:'',
					valueName:'value',
					displayName:'name',
					datas:[{value:'',name:'请选择'}],
					onChange:function(e){
					}
				});
				
				//查詢支付方式下拉資料
				function _queryPay(){
					mobileManage.getLoader().open('载入中');
					mobileManage.getTPPManage().queryWeixin(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							if(result.data.length>0){
								result.data.unshift({value:'',name:'请选择'});
								_weixin.weixinPay.loadData(result.data);
							}else{
								_weixin.weixinPay.loadData([{value:'',name:'维护中'}]);
							}
						}else{
							alert(result.message);
						}
					});
				}
	 			
				//乐富微信
				function _doPay(){
					
					if(_weixin.weixinPay.getValue().indexOf('zfwx')!=-1){
						var formData = {
							payType:_weixin.weixinPay.getValue(),
							order_amount:_$money.val(),
							bank_code:'ZF_WX'
						};
						mobileManage.getTPPManage().formSubmit('asp/ZfWxRedirect.aspx',formData);
					}else{
						var formData = {
							payId:_weixin.weixinPay.getValue(),
							money:_$money.val(),
							bankName:_weixin.weixinPay.getValue(),
						};
						mobileManage.getLoader().open("处理中");
						mobileManage.getTPPManage().pay(formData, function(result){
							if(result.success){
								
							}else{
								alert(result.message);
							}
							mobileManage.getLoader().close();
						});
					}
				}
				_queryPay();
			}
			
			/**
			 * 点卡支付
			 *
			 */
			function _DCardManage(){
				var _dCard = this;
				var _$form = $('#deposit-card-form');
				var _cardCredit = {};

				/**
				 * 初始化
				 */
				function _init(){
					_dCard.$cardNo = $('#deposit-card-no');
					_dCard.$cardPassword = $('#deposit-card-password');


					//点卡类型
					_dCard.cardCode = new MobileComboBox({
						appendId:'deposit-card-code',
						cls:'',
						valueName:'value',
						displayName:'name',
						datas:[
							{"value":"","name":"请选择点卡"},
							{"value":"YDSZX","name":"移动神州行"},
							{"value":"DXGK","name":"电信国卡"},
							{"value":"LTYKT","name":"联通一卡通"},
							{"value":"QBCZK","name":"QQ币充值卡"},
							{"value":"JWYKT","name":"骏网一卡通"},
							{"value":"WMYKT","name":"完美一卡通"},
							{"value":"ZTYKT","name":"征途一卡通"},
							{"value":"WYYKT","name":"网易一卡通"},
							{"value":"SFYKT","name":"盛付通一卡通"},
							{"value":"SHYKT","name":"搜狐一卡通"},
							{"value":"JYYKT","name":"九游一卡通"},
							{"value":"THYKT","name":"天宏一卡通"},
							{"value":"TXYKT","name":"天下一卡通"},
							{"value":"TXYKTZX","name":"天下一卡通专项"}],
						onChange:function(e){
							_changeCardCredit();
							_dCard.$cardNo.val('');
							_dCard.$cardPassword.val('');
						}
					});
					
					_dCard.cardCredit = new MobileComboBox({
						appendId:'deposit-card-money',
						cls:'',
						valueName:'value',
						displayName:'name',
						datas:[{"value":"","name":"请选择"}],
						onChange:function(e){
						}
					});
					
					$('#deposit-card-submit').click(_dCardPay);
				}
				
				/**
				 * 查询点卡面额
				 */
				function _queryCardCredit(callback){
					mobileManage.ajax({
						url:'mobile/json/card.json',
						callback:function(result){
							if(result.success){
								_cardCredit = result.data;
							}else{
								alert(result.message);
							}
							callback();
						}
					})
				}
				
				/**
				 * 建立点卡面额选单
				 */
				function _changeCardCredit(){
					var arr = _cardCredit[_dCard.cardCode.getValue()];
					var datas = [{"value":"","name":"请选择"}];
					for(var i in arr){
						datas.push({"value": arr[i],"name": arr[i]});
					}
					_dCard.cardCredit.loadData(datas);
				}
				
				/**
				 * 检查点卡支付是否开启
				 */
				function _chekcWork(){
					mobileManage.getLoader().open("处理中");
					mobileManage.getTPPManage().getDCardPayWork(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							_$form.replaceWith([
								'	<div class="ui-input-row zf-sele">',
								'	<label class="ui-label">点卡类型：</label>',
								'		<div id="deposit-card-code"></div>',
								'	</div>',
								'	<div class="ui-input-row zf-sele">',
								'	<label class="ui-label">存款额度：</label>',
								'		<div id="deposit-card-money"></div>',
								'	</div>',
								'	<div class="ui-input-row">',
								'	<label class="ui-label">卡号：</label>',
								'		<input id="deposit-card-no" type="text" class="ui-ipt" >',
								'	</div>',
								'	<div class="ui-input-row">',
								'	<label class="ui-label">密码：</label>',
								'		<input id="deposit-card-password" type="text" class="ui-ipt"  >',
								'		</div>',
								'<div class="ui-button-row center">',		
								'	<div class="btn-login block" id="deposit-card-submit">确定支付</div>',
								'</div>',
								'	<div class="space-2"></div>',
								].join(''));
							
							//先查询点卡余额再初始化物件
							_queryCardCredit(_init);
						}else{
							_$form.html(result.message);
						}
					});
				}

				//支付宝附言存款
				function _ZFBRemarkManage(){
					var _zfbRemark = this;
					_zfbRemark.$loginName = $('#deposit-zfbRemark-loginName');
					_zfbRemark.$userName = $('#deposit-zfbRemark-userName');
					_zfbRemark.$accountNo = $('#deposit-zfbRemark-accountNo');
					_zfbRemark.$remark = $('#deposit-zfbRemark-remark');
					
					$('#deposit-zfbRemark-submit').click(_submitClick);
					
					mobileManage.getLoader().open("载入资料中");
					mobileManage.getBankManage().getZFBBankInfo(function(result){
						if(result.success){
							_zfbRemark.$loginName.val(result.data.loginName);
							_zfbRemark.$userName.val(result.data.userName);
							_zfbRemark.$accountNo.val(result.data.accountNo);
							_zfbRemark.$remark.val(result.data.vpnpassword);
						}else{
							alert('请重新刷新网页！');
						}
						mobileManage.getLoader().close();
					});
					
					
					/**
					 * 支付宝跳转
					 *
					 */
					function _submitClick(){
						window.location.href = 'https://shenghuo.alipay.com/send/payment/fill.htm';
					}
				}
				
				/**
				 * 点卡支付
				 */
				function _dCardPay(){
					var formData = {
						card_code:_dCard.cardCode.getValue(),
						card_no:_dCard.$cardNo.val(),
						card_password:_dCard.$cardPassword.val(),
						money:_dCard.cardCredit.getValue()
					};
					mobileManage.getLoader().open("处理中");
					mobileManage.getTPPManage().dCardPay(formData, function(result){
						mobileManage.getLoader().close();
						alert(result.message);
						if(result.success){
							_dCard.$cardNo.val('');
							_dCard.$cardPassword.val('');
							_dCard.cardCredit.setValue('');
						}
					});
				}
				
				_chekcWork();
			}

			//支付宝附言存款
			function _ZFBRemarkManage(){
				var _zfbRemark = this;
				_zfbRemark.$loginName = $('#deposit-zfbRemark-loginName');
				_zfbRemark.$userName = $('#deposit-zfbRemark-userName');
				_zfbRemark.$accountNo = $('#deposit-zfbRemark-accountNo');
				_zfbRemark.$remark = $('#deposit-zfbRemark-remark');
				
				$('#deposit-zfbRemark-submit').click(_submitClick);
				
				mobileManage.getLoader().open("载入资料中");
				mobileManage.getBankManage().getZFBBankInfo(function(result){
					if(result.success){
						_zfbRemark.$loginName.val(result.data.loginName);
						_zfbRemark.$userName.val(result.data.userName);
						_zfbRemark.$accountNo.val(result.data.accountNo);
						_zfbRemark.$remark.val(result.data.vpnpassword);
					}else{
						alert('请重新刷新网页！');
					}
					mobileManage.getLoader().close();
				});
				
				
				/**
				 * 支付宝跳转
				 *
				 */
				function _submitClick(){
					window.location.href = 'https://shenghuo.alipay.com/send/payment/fill.htm';
				}
			}
			
			/**
			 * 快捷存款
			 */
			function _SpeedPayManage(){
				var that = this;
				var _$money = $('#deposit-speedPay-money');
				$('#deposit-speedPay-submit').click(_doPay);
				
				function _doPay(){
					var formData = {
						payId:'汇潮',
						money:_$money.val(),
						bankName:'NOCARD'
					};
					mobileManage.getLoader().open("处理中");
					mobileManage.getTPPManage().pay(formData, function(result){
						if(result.success){
							
						}else{
							alert(result.message);
						}
						mobileManage.getLoader().close();
					});
				}
			}			
			function _WXValidateManage(){
				var that = this;
				that.amount = $('#deposit-WXValidate-amount');
				that.amountArea = $('#WXValidate-input-area');
				that.payInfo = $('#deposit-WXValidate-payInfo');
				that.button = $('#deposit-WXValidate-submit');
				that.discardButton = $('#deposit-WXValidate-discard');
				that.errorMsg = $("#deposit_WXValidate_error_msg");
				var orderId = '';
				var payInfoHtml = '';

				//查询支付宝绑定
				mobileManage.getLoader().open('载入中');
			    mobileManage.getBankManage().getWXValidatePayInfo(function(result){
		    		if(result.success){
		    			if(result.data.wxBank.zfbImgCode){
		    				payInfoHtml = '<img style="width:250px;" src="'+result.data.wxBank.zfbImgCode+'"/>';		    				
		    			}else{
		    				payInfoHtml = '<span style="font-size:20px;">微信账号：<font color="red">'+result.data.wxBank.accountno+'<red></span>';
		    			}
		    			if(result.data.wxValidaTeAmout!=undefined){
		    				console.log("有订单!");
		    				orderId = result.data.wxValidaId;
		    				existOrderContent(result.data.wxValidaTeAmout,payInfoHtml);
		    			}else{
		    				console.log("无订单!");
		    			}
		    			that.errorMsg.css('display', 'none');
		    		}else{
		    			that.errorMsg.html('<span style="font-size:20px;"><font color="red">'+result.message+'<red></span>');
		    			that.errorMsg.css('display', '');
		    			that.amountArea.remove();
				        that.button.remove();
		    			alert(result.message);
		    		}
					mobileManage.getLoader().close();
// 					mobileManage.getModel().open('confirm',[{
// 						title:'使用说明',
// 						message:[
// 							'<img style="width:100%;" src="/mobile/images/wxvalidate.png?v=1">'
// 						]
// 					}]);
			    });
			    
			    that.button.click(function(){
					var depositAmount = that.amount.val();
					if(clearNoNum(depositAmount))return;
					if(depositAmount=='' || $.trim(depositAmount)==''){return;}
				    if(depositAmount < 10){alert('最低存款10元！');return;}
				    if(depositAmount > 5000){alert('不能超过5000元！');return;}
					mobileManage.getLoader().open("处理中");
					$.post("asp/createValidateAmountPayOrderTwo.aspx", {amount:that.amount.val(),type:"1"}, function(data){
					    if(data.code=='1') {
					    	existOrderContent(data.amount,payInfoHtml);
					    	mobileManage.getBankManage().getWXValidatePayInfo(function(result){
						    		if(result.success){
						    			orderId = result.data.wxValidaId;
						    		}
							    });
					    } else {
					        alert(data.msg);
					    }
					}).fail(function(){
					    alert("生成订单失败");
					}).always(function() {
						mobileManage.getLoader().close();
					});
				});
			    
			    that.discardButton.click(function(){
					mobileManage.getLoader().open("处理中");
					 $.post("${ctx}/asp/discardDepositOrder.aspx", {id:orderId}, function(data){
				            if(data.code=='1') {
				            	 location.href = "/mobile/fundsManage.jsp";
				            }
				        }).fail(function(){
				        	alert("废除订单失败");
						}).always(function() {
							mobileManage.getLoader().close();
						});
				});
			    
			    function existOrderContent(wxValidaTeAmout,payInfoHtml){
			    	that.discardButton.css('display', '');
			    	var str = '<span style="font-size:20px;">请您存入&nbsp;&nbsp;<span style="font-size:28px; color:red">'+wxValidaTeAmout+'&nbsp;元</span>，否则无法到账!</span>'
			        $('#depositResult').html(str);
    				that.payInfo.append(payInfoHtml+'<div><b><font color="red">如果您已支付 请等待系统处理此笔订单,如果您未支付,请进行支付，如有疑问可以随时联系在线客服进行谘询。<font><b></div>');
    				that.amountArea.remove();
			        that.button.remove();
			    }
			    
			}
			
		}
		
		//验证输入input
		function clearNoNum(_value){
		    // 事件中进行完整字符串检测
		    var patt = /^((?!0)\d+\.[0-9][1-9])$/g;
		        if (!patt.test(_value)) {
		            // 错误提示相关代码，边框变红、气泡提示什么的
		            alert("输入金额格式错误");
		            return true;
		        }
		   return false;
		}


		function mykeypress(obj, e){
		    // 在 keyup 事件中拦截错误输入
		    var keynum;
		    if(window.event) { // IE                    
		      keynum = e.keyCode;
		    } else if(e.which){ // Netscape/Firefox/Opera                   
		      keynum = e.which;
		    }
		    var sCharCode = String.fromCharCode(keynum);
		    var sValue = obj.value;
		    if (/[^0-9.]/g.test(sCharCode) || __getRegex(sCharCode).test(sValue)) {
		        return false;
		    }

		    /**
		     * 根据用户输入的字符获取相关的正则表达式
		     * @param  {string} sCharCode 用户输入的字符，如 'a'，'1'，'.' 等等
		     * @return {regexp} patt 正则表达式
		     */
		    function __getRegex (sCharCode) {
		        var patt;
		        if (/[0]/g.test(sCharCode)) {
		            // 判断是否为空
		            patt = /^$/g;
		        } else if (/[.]/g.test(sCharCode)) {
		            // 判断是否已经包含 . 字符或者为空
		            patt = /((\.)|(^$))/g;
		        } else if (/[0-9]/g.test(sCharCode)) {
		            // 判断是否已经到达小数点后两位
		            patt = /\.\d{2}$/g;
		        }
		        return patt;
		    }
		}
		
		function checkWXZFBMoney(money){
			var rex=/\d+[.]\d{2}$/g;
			if(rex.test(money)) return true;
			else return false;
		}
	</script>
 