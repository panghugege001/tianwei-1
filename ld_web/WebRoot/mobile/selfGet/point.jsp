<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<div class="content">
			<div class="mui-col-xs32-10 mui-col-xs32-offset-1 mui-col-xs48-8 mui-col-xs48-offset-2 mui-col-xs64-6 mui-col-xs64-offset-0">
				
				<div class="mui-textfield">
			  	  	<input id="point-balance1" type="text" value="0" style="color:red;" readonly />
			    	<label>可用积分</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="point-balance2" type="text" value="0" style="color:red;" readonly />
			    	<label>历史总积分</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="point-message" type="text" value="" style="color:red;" readonly />
			    	<label>当前兑换比率</label>
			  	</div>
				<div class="mui-textfield">
			  	  	<input id="point-money" type="text" placeholder="请输入你的兑换金额"/>
			    	<label>积分兑换奖金到天威账户</label>
			    	<div class="message">消耗积分：<font color="red">0</font></div>
			  	</div>
				<div class="mui-buttons">
					<button class="mui-btn mui-btn--raised mui-btn--primary" id="point-submit">提交</button>
				</div>
			</div>
			<div class="panel mui-col-xs32-12 mui-col-xs64-6" >
				<div class="h3">温馨提示</div>
				<ol>
	              	<li>188体育平台投注不计算在积分内。</li>
	              	<li>每天下午17:00-18:00派发前一天投注额所产生的积分。 </li>
	              	<li><span style="color:red;">积分兑换奖金处输入您对换的金额。</span> </li>
				</ol>
			</div>
		</div>
		<script type="text/javascript">
			function PointPage(){
				var that = this;
				var _ratio = 500;
				var _message = "{0}积分兑换1元 ;可兑换奖金为：{1}元";
				
				that.$balance1 = $('#point-balance1');
				that.$balance2 = $('#point-balance2');
				that.$message = $('#point-message');
				that.$money = $('#point-money');
				that.$usePoint = that.$money.parent().find('.message font');
				that.$submit = $('#point-submit');
				
				that.$submit.click(_submit);

				NumberInput(that.$money[0]);
				
				//输入金额立即计算消耗积分
				that.$money.keyup(function(e){
					//延迟不重复处理同样的动作
					delayAction('point-money',300,function(){
						that.$usePoint.html(that.$money.val()*_ratio);
					});
				});
				
				
				/**
				 * 查詢餘額
				 */
				function _getPoints(){
					that.$balance1.val(0);
					that.$balance2.val(0);
					that.$usePoint.html('');
					that.$message.val(String.format(_message,_ratio,0));
					mobileManage.getLoader().open('查询中');
					mobileManage.getSelfGetManage().getPoints(function(result){
						mobileManage.getLoader().close();
						if(result.success){
							_ratio = result.data.ratio;
							that.$balance1.val(result.data.nowPoint);
							that.$balance2.val(result.data.oldPoint);
							that.$message.val(String.format(_message,_ratio,Math.floor(result.data.nowPoint/_ratio)));
						}else{
							alert(result.message);
						}
					});
				}
				
				//转帐
				function _submit(){
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().transferPoints({
						money:that.$money.val()
					},function(result){
						mobileManage.getLoader().close();
						if(result.success){
							that.$money.val('');
							alert(result.message);
						}else{
							alert(result.message);
						}
						_getPoints();
					});
				}
				
				_getPoints();
			}
		</script>
	</body>
</html>