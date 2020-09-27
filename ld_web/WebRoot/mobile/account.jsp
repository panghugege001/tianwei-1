<%@page import="dfh.utils.StringUtil"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	if(session.getAttribute(Constants.SESSION_CUSTOMERID)==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}
%>
<!DOCTYPE >
<html>
	<head>
		<base href="${ctx}/"/>
		<jsp:include page="commons/back.jsp" />
		
		<link rel="stylesheet" type="text/css" href="mobile/css/fundsManage.css?v=9" />
	</head>
	<body>
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
					 <div class="list-group">
						<div class="list-item"><a href="mobile/deposit.jsp">存款<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
						<div class="list-item"><a href="mobile/withdrawal.jsp">提款<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
						<div class="list-item"><a href="mobile/transfer.jsp">户内转账<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
						<div class="list-item"><a href="mobile/redpacket.jsp">红包转账<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
					</div>
					<div class="list-group">
						<div class="list-item"><a href="mobile/selfGet.jsp">自助优惠<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
					</div>
					<div class="list-group">
						<div class="list-item"><a href="mobile/accountHistory.jsp">账户清单<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
					</div>
					<div class="list-group">
						<div class="list-item"><a href="asp/bbsIndex.aspx">会员社区<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
						<div class="list-item"><a href="mobile/email.jsp">站内信<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
						<div class="list-item"><a href="http://ui.easeye.com.cn/Eventmail/UserRegisterPage.aspx?type=reg&guid=xSE1KVklJSE5O">订阅邮件<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
					</div>
					 
					<div class="list-group">
						<div class="list-item"><a href="javascript:;" id="logout-button">退出<i class="list-arrow iconfont icon-arrow-right"></i></a></div>
					</div>
			</div>
			<div class="footer-margin"></div>
		</div>
		<jsp:include page="commons/footer1.jsp" />
		<script type="text/javascript">
		footerBar.active('account');
    		$(function () {
				    // loginout
					$('#logout-button').on('click',function () {
						mobileManage.getLoader().open('登出中');
						mobileManage.getUserManage().logout( function(data){
							mobileManage.getLoader().close();
							if(data.success){
								sessionStorage.setItem('permission',0);
								sessionStorage.setItem('notice',1);
								mobileManage.getLoader().close();
								window.location = '/mobile/index.jsp'

							}else{
								alert(data.message);
							}
						});
					});
			})
			
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