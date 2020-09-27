<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}else if("AGENT".equals(user.getRole())){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}
%>
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/selfGet.css?v=11" />
		
		<script type="text/javascript" src="mobile/js/ModeControl.js"></script>
	</head> 
<body>
	<div class="tab-bd">
		<div id="page-index" data-page-index class="tab-panel active">	
			<div class="main-wrap">
				<div class="content">
					<div class="header-margin"></div>
					<ul>
						<%--<li>
							<div class="title" id="selfGet-wenjuan" onclick="window.location.href='https://wj.qq.com/s/1361490/0582/'"><a data-toggle="tab" href="#" aria-expanded="false">问卷调查拿彩金</a></div>
						</li>--%> 
						<%--<li id="appPreferentialTag"> 
								<div class="title" id="selfGet-appPreferential"><a data-toggle="tab" href="#yh-app" aria-expanded="false">APP下载彩金</a></div>
						</li>--%> 
						<%--<li>
							<div class="title"><a href="https://wj.qq.com/s/1743558/3f3f" aria-expanded="false">填写问卷领彩金</a></div>
						</li>--%> 
						<li>
							<div class="title" id="selfGet-caijin"><a data-toggle="tab" href="#yh-caijin" aria-expanded="false">开户彩金</a></div>
						</li>						
						<li>
							<div class="title" id="selfGet-youhui"><a data-toggle="tab" href="#yh-cs" aria-expanded="false">自助存送</a></div>
						</li>						
						<%--<li>
<!--							<div class="title" id="selfGet-experience" ><a data-toggle="tab" href="#yh-tyj" aria-expanded="false">体验金</a></div>-->
							<div class="title" id="selfGet-experience" ><a  href="#yh-tyj" aria-expanded="false" onclick="javascript:alert('请下载天威APP进行体验金申请步骤'); return false;">体验金</a></div>
						</li>--%> 
						<li>
							<div class="title" id="selfGet-xima"><a data-toggle="tab" href="#yh-fs" aria-expanded="false">自助返水</a></div>
						</li>
						<li>
							<div class="title" id="selfGet-help"><a data-toggle="tab" href="#yh-help" aria-expanded="false">救援金</a></div>
						</li>
						<li>
							<div class="title" id="selfGet-upgrade"><a data-toggle="tab" href="#yh-jinji" aria-expanded="false">自助晋级</a></div>
						</li>
						<li>
							<div class="title" id="selfGet-coupon"><a data-toggle="tab" href="#yh-yhj" aria-expanded="false">优惠卷专区</a></div>
						</li>
						<li>
							<div class="title" id="selfGet-redCoupon"><a data-toggle="tab" href="#yh-hb" aria-expanded="false">红包优惠卷</a></div>
						</li>	
						<li>
							<div class="title" id="selfGet-chouma"><a data-toggle="tab" href="#yh-cm" aria-expanded="false">免费筹码</a></div>
						</li>							
						<li>
							<div class="title" id="selfGet-qd"><a data-toggle="tab" href="#yh-qd" aria-expanded="false">每日签到</a></div>
						</li>
						<li>
							<div class="title" id="selfGet-hong"><a data-toggle="tab" href="#yh-hong" aria-expanded="false">存款红包</a></div>
						</li>		
						<li>
							<div class="title" id="selfGet-cunsong"><a data-toggle="tab" href="#yh-cunsong" aria-expanded="false">每日存送</a></div>
						</li>							
					</ul>
				</div>
				<div class="footer-margin" ></div>
			</div>
		</div>
		<!--下载app-->
		<div id="yh-app" class="page tab-panel" > 
			<jsp:include page="selfGet/appPreferential.jsp" />
		</div> 
		<!--开户彩金-->  
		<div id="yh-caijin" class="page tab-panel" >
			<jsp:include page="selfGet/caijin.jsp" />
		</div>		
		<!--自助存送-->  
		<div id="yh-cs" class="page tab-panel" >
			<jsp:include page="selfGet/youhui.jsp" />
		</div>
		<!--体验金-->
		<div id="yh-tyj" class="page tab-panel" >
			<jsp:include page="selfGet/experience.jsp" />
		</div>
		<!--自助反水-->
		<div id="yh-fs" class="page tab-panel" >
			<jsp:include page="selfGet/xima.jsp" />
		</div>	
		<!--救援金-->
		<div id="yh-help" class="page tab-panel" >
			<jsp:include page="selfGet/help.jsp" />
		</div> 
		<!--自助晋级-->
		<div id="yh-jinji" class="page tab-panel" >
			<jsp:include page="selfGet/upgrade.jsp" />
		</div>
		<!--优惠劵专区-->
		<div id="yh-yhj" class="page tab-panel" >
			<jsp:include page="selfGet/coupon.jsp" /> 
		</div>
		<!--红包优惠劵-->
		<div id="yh-hb" class="page tab-panel" >
			<jsp:include page="selfGet/redCoupon.jsp?v=1" />
		</div>
		<!--免费筹码-->
		<div id="yh-cm" class="page tab-panel" >
			<jsp:include page="selfGet/chouma.jsp?v=3" />
		</div>		
		
		<!--好友推荐-->
		<div id="yh-qd" class="page tab-panel" >
			<jsp:include page="selfGet/newsign.jsp" />
		</div>
		<!--每日红包-->
		<div id="yh-hong" class="page tab-panel" >
			<jsp:include page="selfGet/hongbao.jsp" />
		</div>	
		<div id="yh-cunsong" class="page tab-panel" >
			<jsp:include page="selfGet/cunsong.jsp" />
		</div>			
	</div>		
	<jsp:include page="commons/footer1.jsp" />
	<script type="text/javascript">
		headerBar.setTitle('自助优惠');
		footerBar.active('selfGet');
		
		var selfGets = {};
		var titles = $('ul>li>.title');

		titles.bind('click',_titleClickEvent);
		if(sessionStorage['webapp']==='true'){
		}else{
			$("#appPreferentialTag").remove();
		}
		function _titleClickEvent(){
			
			var title = $(this);
			if(title.parent().hasClass('active')){
				
				 title.parent().removeClass('active');
			}else{
				//关闭其他打开的
				 $('ul>li.active').removeClass('active');
				 title.parent().addClass('active');
			}
			
			
			if(!selfGets[this.id]){
				switch(this.id){
					case 'selfGet-caijin':
						selfGets[this.id] = new caijin();
						break;					
					case 'selfGet-youhui':
						selfGets[this.id] = new YouhuiPage();
						break;
					case 'selfGet-xima':
						selfGets[this.id] = new XimaPage();
						break;
					case 'selfGet-help':
						selfGets[this.id] = new HelpPage();
						break;
					case 'selfGet-upgrade':
						selfGets[this.id] = new UpgradePage();
						break;
					case 'selfGet-bigbang':
						selfGets[this.id] = new BigBangPage();
						break;
					case 'selfGet-coupon':
						selfGets[this.id] = new CouponPage();
						break;
					case 'selfGet-week':
						selfGets[this.id] = new WeekPage();
						break;
					case 'selfGet-sign':
						selfGets[this.id] = new SignPage();
						break;
					case 'selfGet-point':
						selfGets[this.id] = new PointPage();
						break;
					case 'selfGet-friend':
						selfGets[this.id] = new FriendPage();
						break;
					case 'selfGet-redCoupon':
						selfGets[this.id] = new RedCouponPage();
						break;					
					case 'selfGet-experience':
						selfGets[this.id] = new ExperiencePage();
						break;
					case 'selfGet-emigrated':
						selfGets[this.id] = new EmigratedPage();
						break;
					case 'selfGet-appPreferential':
						selfGets[this.id] = new AppPreferentialPage();
						break; 
					default:
				}
			}
		}
		
	</script>
</body>
</html>