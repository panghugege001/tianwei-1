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
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="红包优惠券" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>
	<body>
		<div class="form-warp txt-form">
			<div class="form-group">
				<label class="form-label">优惠代码：</label>
				<input id="redCoupon-code" class="form-control" type="text">
			</div>
			<div class="form-group zf-sele">
				<label class="form-label">转帐到：</label>
				<select id="redCoupon-platform" class="form-control" ></select>
			</div>
			<div class="btn-submit block" id="redCoupon-submit">提交</div>
			<div class="text-tips">
				<div class="h3">红包优惠说明 </div>
				<ol>
					<li>请选择正确的游戏平台。填写红利代码，确认提交， 红利金额会自动添加到您转到的游戏平台里。</li>
					<li>PT/DT/MG/QT/TTG/NT红包优惠券，需PT/DT/MG/QT/TTG/NT游戏账户低于5元才能使用红包优惠券。达到相应的有效投注额要求或PT/DT/MG/QT/TTG/NTT游戏账户低于5元，才能再次进行PT/DT/MG/QT/TTG/NT户内转账。</li>
					<li>红包优惠券有效期为30天，请您在有效期内进行使用。</li>
					<li>如何得到优惠券，请留意天威最新的相关优惠信息。</li>
				</ol>
			</div>

		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			RedCouponPage()
			function RedCouponPage() {
				var that = this;
				that.platform = $('#redCoupon-platform');
				that.code = $('#redCoupon-code');
				that.submit = $('#redCoupon-submit');

				that.submit.click(_submit);

				if($('#redCoupon-platform').get(0)) {
					that.platform = new MobileComboBox({
						appendId: 'redCoupon-platform',
						cls: '',
						valueName: 'value',
						displayName: 'name',
						datas: [{
								value: '',
								name: '请选择'
							},
							{
								value: 'ttg',
								name: 'TTG'
							},
							{
								value: 'pt',
								name: 'PT'
							},
							{value:'agin',name:'AGIN'}, 
							{
								value: 'slot',
								name: '老虎机账户(SW,MG,DT,PNG,QT,NT)'
							}
						],
						onChange: function(e) {

						}
					});
				}

				//优惠卷转帐
				function _submit() {

					mobileManage.getLoader().open('执行中');
					mobileManage.getSelfGetManage().transferInforRedCoupon({
						platform: that.platform.getValue(),
						couponCode: that.code.val()
					}, function(result) {
						mobileManage.getLoader().close();
						if(result.success) {
							alert(result.message);
						} else {
							alert(result.message);
						}
					});
				}
			}
		</script>
	</body>

</html>