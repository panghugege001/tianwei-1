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
		<jsp:include page="commons/back.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=9" />
		
		<script type="text/javascript" src="mobile/js/MobileGrid.js"></script>
		<script type="text/javascript" src="mobile/js/ModeControl.js"></script>
	</head>
	<body>
		<div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">

				<div class="page-block">
					 
					
					<div id="funds-transfer-page" style="display: none;">
						<jsp:include page="funds/transfer.jsp" />
					</div>
				</div>
			</div>
			
			<div class="footer-margin"></div>
		</div>
		<jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
			headerBar.setTitle('转账');
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
			 
			 
			modeControl.putMode(0,{id:'funds-transfer',button:$('#funds-transfer-button'),page:$('#funds-transfer-page'),manage:undefined,getManage:TransferPage});
			
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
			
		 
		</script>
	</body>
</html>