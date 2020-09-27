<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<div class="content mt5">
			<div class="ui-form">
				<div class="ui-input-row">
<!-- 			  	  	<input id="friend-url" type="text" style="color:red;" readonly /> -->
			    	<label class="ui-label">您的<span style="color:red;">推荐链接</span>为：</label>
			  	</div>
				<div class="ui-input-row ui-input-row2">
					<div id="friend-url" class="message" style="color:red;"></div>
				</div>
				<div class="ui-input-row">
					<label class="ui-label">推荐奖金账户余额：</label>		
			  	  	<input id="friend-balance" type="text" class="ui-ipt" value="0" style="color:red;" readonly />
			  	</div>
				<div class="ui-input-row zf-sele zf-sele2">  
					<label class="ui-label">从推荐奖金账户转账到：</label>	
			  	  	<select class="input" id="friend-platform" style="width:50%">
                    	<option value=""> 请选择 </option>
                    </select>
			  	</div>
				<div class="ui-input-row">
					<label class="ui-label">转账金额：</label>	
			  	  	<input id="friend-money" type="text" class="ui-ipt" placeholder="0" />
			  	</div>
				<div class="ui-button-row center">
					<button class="btn-login block" id="friend-submit">提交</button>
				</div>
			</div>
			<div class="panel mui-col-xs32-12 mui-col-xs64-6 tishi" >
				<div class="h3">温馨提示</div>
				<ol>
					<li>您的好友必须从您的<span style="color:red;">专属链接</span>进行<span style="color:red;">注册</span>。</li>
					<li>被推荐人当日产生的负盈利30%，将会在次日18点前派发予推荐人！</li>
					<li>彩金派发在推荐好友专属账户，转入对应平台即可游戏。</li>
					<li>如有任何疑问请及时联系24小时在线客服。</li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function FriendPage(){
				var that = this;
				var _ratio = 2000;
				
				that.$balance = $('#friend-balance');
				that.$money = $('#friend-money');
				that.$url = $('#friend-url');
				that.$submit = $('#friend-submit');
				
				that.$submit.click(_submit);

				NumberInput(that.$money[0]);
				
				if($('#friend-platform').get(0)){
					that.$platform = new MobileComboBox({
						appendId:'friend-platform',
						valueName:'value',
						displayName:'name',
						datas:[
							{value:'pt',name:'PT'},
							{value:'ttg',name:'TTG'},
 							{value:'nt',name:'NT'},
							{value:'qt',name:'QT'},
							{value:'mg',name:'MG'},
							{value:'dt',name:'DT'}
						],
						onChange:function(e){
							
						}
					});
				}
				
				
				/**
				 * 查詢餘額
				 */
				function _queryFriendBonue(){
					that.$balance.val(0);
					that.$money.val('');
					mobileManage.getLoader().open('查询中');
					mobileManage.getSelfGetManage().queryFriendBonue(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							that.$balance.val(result.data.money);
							that.$url.html(window.location.origin+"?friendcode="+result.data.url);
						}else{
							alert(result.message);
						}
					});
				}
				
				//转帐
				function _submit(){
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().transferFriend({
						platform:that.$platform.getValue(),
						money:that.$money.val()
					},function(result){
						mobileManage.getLoader().close();
						if(result.success){
							that.$money.val('');
							alert(result.message);
						}else{
							alert(result.message);
						}
						_queryFriendBonue();
					});
				}
				
				_queryFriendBonue();
			}
		</script>
	</body>
</html>