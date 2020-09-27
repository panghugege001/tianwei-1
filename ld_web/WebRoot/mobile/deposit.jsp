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
		
	</head>
	<body>
		<jsp:include page="commons/back.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=9" />
		
		<script type="text/javascript" src="mobile/js/MobileGrid.js"></script>
		<script type="text/javascript" src="mobile/js/ModeControl.js"></script>
		<div class="tab-bd">
		<div id="page-index" data-page-index="" class="tab-panel active">
		<div class="main-wrap">
			<div class="header-margin"></div>
			<div class="content">
				<div class="account-info">
						<div class="user-photo sp-level sp-level-${session.customer.level}"></div> 
						<div class="info">
							<strong>账号:</strong><span class="value cde">${session.customer.loginname}</span> <br />
							<strong>等级:</strong><span class="value cde"><s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)"/></span><br /> 
							
							<c:choose>
								<c:when test="${session.customer!=null&&session.customer.role!='AGENT'}">
									<div class="banlce"><strong>余额:</strong><span class="value cde" id="credit">${session.customer.credit} 元</span><span class="refre iconfont icon-shuaxin" onClick="refresh()" title="刷新余额"></span></div><br />
								</c:when>
								<c:otherwise>
									<strong>老虎机佣金余额:</strong><span class="value cde">${session.slotAccount} 元</span><br />
									<strong>其他佣金余额:</strong><span class="value cde">${session.customer.credit} 元</span><br />
								</c:otherwise>
							</c:choose>
                             <div class="banlce">
                             <a href="javascript:void(0)" class="btn-member center"  onclick="onekeyMonery();" style="position: static;" >一键额度回归</a>
                            </div>
						</div>
						<a href="mobile/myaccount.jsp" class="btn-member" ><i class="iconfont icon-iconfontwodecopy"></i>会员信息</a>
					</div>
				
				<div class="page-block deposit-content">
					<div id="funds-deposit-page" style="display: none;">
						<jsp:include page="funds/deposit.jsp"/>
					
		<jsp:include page="commons/footer1.jsp" />    
					</div>

		<script type="text/javascript">
			headerBar.setTitle('存款');
			footerBar.active('depost');
			
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
				
 
			});
			
			modeControl.on('change', function(){
				mode = modeControl.getActiveKey();
				mobileManage.setSessionStorage('fundsManage',{mode:mode});
			});
			
			
			//显示对应的Page
			modeControl.activeMode(mode);
			//重新整理余额
			function refresh(){
				$('.refre').addClass('credit-query');
				
				//先查询优发平台余额
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
		</script>
	</body>
</html>