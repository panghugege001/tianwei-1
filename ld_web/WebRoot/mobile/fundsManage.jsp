<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/agent.jsp");
	}
%>
<!DOCTYPE >

<html>
	<head>
		<jsp:include page="commons/common.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=66" />
		
		<script type="text/javascript" src="mobile/js/MobileGrid.js"></script>
		<script type="text/javascript" src="mobile/js/ModeControl.js"></script>
	</head>
	<body>
		<div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">
 
				<div class="account-info">
					<div class="user-photo flaticon-user7"></div>
					<div class="info">
						<strong>欢迎您</strong>：${session.customer.loginname}<br/>
						<strong>账户余额</strong>：<span id="credit">${session.customer.credit} 元</span ><br/>
						<strong>等级</strong>：<s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)"/>
					</div>
					<div class="refre flaticon-refresh57" onclick="refresh()"></div>
				</div>
				
				<div class="page-block">
					<div id="funds-deposit-page" style="display: none;">
						<jsp:include page="funds/deposit.jsp"/>
					</div>
					
					<div id="funds-withdrawal-page" style="display: none;">
						<jsp:include page="funds/withdrawal.jsp"/>
					</div>
					
					<div id="funds-transfer-page" style="display: none;">
						<jsp:include page="funds/transfer.jsp" />
					</div>
				</div>
			</div>
			
			<div class="footer-margin"></div>
		</div>
		<jsp:include page="commons/footer.jsp" />
		<script type="text/javascript">
			headerBar.setTitle('存款');
			footerBar.active('fundsManage');
			
			headerBar.scrollHide(true);
			headerBar.bind('hide',function(){
				$('.main-wrap .tab-block').css('top',0);
			});
			headerBar.bind('show',function(){
				$('.main-wrap .tab-block').css('top',headerBar.defHeight);
			});
			
			var mode = mobileManage.getSessionStorage('fundsManage').mode||0;
			//切换存款、提款、转账和交易明细
			var modeControl = new ModeControl();
			modeControl.putMode(0,{id:'funds-deposit',button:$('#funds-deposit-button'),page:$('#funds-deposit-page'),manage:undefined,getManage:DepositPage});
			modeControl.putMode(1,{id:'funds-withdrawal',button:$('#funds-withdrawal-button'),page:$('#funds-withdrawal-page'),manage:undefined,getManage:WithdrawalPage});
			modeControl.putMode(2,{id:'funds-transfer',button:$('#funds-transfer-button'),page:$('#funds-transfer-page'),manage:undefined,getManage:TransferPage});
			
			modeControl.on('beforChange',function(){
				var nextMode = modeControl.getNextMode();
				var activeMode = modeControl.getActiveMode();
				if(nextMode){
					nextMode.page.css('display','block');
					nextMode.button.addClass('active');
					if(!nextMode.manage&&nextMode.getManage){
						if(nextMode.getManage){
							//切換到該頁面才執行js 
							nextMode.manage = new nextMode.getManage();
						}
					}

					//切換缺角背景顏色
					if(nextMode.id=='funds-deposit'){
						var depositMode = mobileManage.getSessionStorage('fundsManage').depositMode||0;
						if(depositMode==0){
							$(".m-jj").removeClass("ds").addClass('d');
						}else{
							//白
							$(".m-jj").removeClass("d").addClass('ds');
						}
					}else{
						//灰
						$(".m-jj").removeClass("ds").addClass('d');
					}
				}
				
				if(activeMode){
					activeMode.page.css('display','none');
					activeMode.button.removeClass('active');
				}
			});
			
			modeControl.on('change', function(){
				mode = modeControl.getActiveKey();
				mobileManage.setSessionStorage('fundsManage',{mode:mode});
			});
			
			
			//显示对应的Page
			modeControl.activeMode(mode);
			
			$('#funds-deposit-button').click(function(){
				modeControl.activeMode(0);
			});
			$('#funds-withdrawal-button').click(function(){
				modeControl.activeMode(1);
			});
			$('#funds-transfer-button').click(function(){
				modeControl.activeMode(2);
			});
			
			//重新整理余额
			function refresh(){
				$('.refre').addClass('credit-query');
				
				//先查询天威平台余额
				mobileManage.getUserManage().getCredit(
					function(result){
			    		if(result.success){
							$("#credit").html(result.message+" 元");
						}else{
							$("#credit").html('系统繁忙中');
							alert(result.message);
						}
						$('.refre').removeClass('credit-query');
					}
			    );
			}
			refresh();
		</script>
	</body>
</html>