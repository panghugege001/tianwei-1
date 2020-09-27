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
			<jsp:param name="Title" value="生日礼金" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>
	<body>
		<div class="form-warp birthday txt-form">
			<div class="form-group">
				<label class="form-label">您的生日 :</label>
				<span class="birthday-text"></span>
			</div>
			<div class="form-group">
				<label class="form-label">会员等级 :</label>
				<span class="birthday-grade"></span>
			</div>
			<div class="form-group">
				<label class="form-label">生日礼金 :</label>
				<span class="birthday-moeny"></span>
			</div>
			<div class="btn-submit block gray" id="redCoupon-submit">领取</div>
			<table class="birthday-table">
				<tr>
					<td>VIP等级</td>
					<td>生日礼金</td>
				</tr>
				<tr>
					<td>天兵</td>
					<td>X</td>
				</tr>
				<tr>
					<td>天王</td>
					<td>18</td>
				</tr>
				<tr>
					<td>天将</td>
					<td>88</td>
				</tr>
				<tr>
					<td>星君</td>
					<td>188</td>
				</tr>
				<tr>
					<td>真君</td>
					<td>388</td>
				</tr>
				<tr>
					<td>仙君</td>
					<td>888</td>
				</tr>
				<tr>
					<td>帝君</td>
					<td>1888</td>
				</tr>
				<tr>
					<td>天尊</td>
					<td>2888</td>
				</tr>
				<tr>
					<td>天帝</td>
					<td>3888</td>
				</tr>
			</table>
			<div class="text-tips">
				<div class="h3">温馨提示  </div>
				<ol>
					<li>
						请注意每年您<span class='h3'>生日前后三天</span>	可以进行自助领取礼金，例如5月12日生日，可在5月9-15日领取，逾期无法进行补发。</li>
					<li>生日礼金无需流水直接派发至您的主账户里。</li>
					<li>若有任何疑问请咨询在线客服。</li>
				</ol>
			</div>
		</div>
		

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			RedCouponPage()
			console.log(11111)
			function RedCouponPage() {
				var that = this;
				var _state =false;
				var _id='';
				 var _isDraw = false;
				var vip =['新会员','忠实VIP','星级VIP','黄金VIP','白金VIP','钻石VIP','至尊VIP']
				that.platform = $('#redCoupon-platform');
				that.code = $('#redCoupon-code');
				that.submit = $('#redCoupon-submit');

				that.submit.click(_submit);


				//领取生日 礼金
				function _submit() {
					if(!_state || _isDraw) return;
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().getBirthdayMoeny({id:_id},function(result) {
						mobileManage.getLoader().close();
						_inquiry();
						alert(result.message)
					});
				}
				//查询生日   礼金
				function _inquiry() {
					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().queryBirthdayMoeny(function(result) {
						mobileManage.getLoader().close();
						$('.birthday-text').html(result.birthday)
						$('.birthday-grade').html(vip[result.level])
						$('.birthday-moeny').html(result.amount)
						_state = result.state;
						_isDraw = result.isDraw;
						_id=result.id
						if(_state){
							$('#redCoupon-submit').removeClass('gray') 
						}else{
							$('#redCoupon-submit').addClass('gray') 
							// $('#redCoupon-submit').html('还未到领取时间领取')
						}
						if(_isDraw){
							$('#redCoupon-submit').addClass('gray')
							$('#redCoupon-submit').html('已领取')
						}else{
							$('#redCoupon-submit').removeClass('gray') 
							$('#redCoupon-submit').html('领取')
						}
						if(!_state || _isDraw){
							$('#redCoupon-submit').addClass('gray')
						}


						
					});
				}
				_inquiry();
			}
		</script>
	</body>

</html>