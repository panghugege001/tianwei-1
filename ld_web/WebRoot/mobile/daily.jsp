<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
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
		<jsp:include page="commons/common.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/selfGet.css?v=9" />
	</head>
<body>
	<div class="main-wrap"> 
		<div class="content">
			<div class="header-margin" ></div>
			<ul>
				<li>
					<div class="title flaticon-right133" id="daily-youhui">每日存送</div>
					<div class="page" >
						<jsp:include page="daily/youhui.jsp" />
					</div>
				</li>
				<li >
					<div class="title flaticon-right133" id="daily-sign">每日签到</div>
					<div class="page" >
						<jsp:include page="selfGet/sign.jsp" />
					</div>
				</li>
				<li>
					<div class="title flaticon-right133" id="daily-bigbang">PT疯狂幸运转</div>
					<div class="page" >
						<jsp:include page="selfGet/bigbang.jsp" />
					</div>
				</li>
				<li>
					<div class="title flaticon-right133" id="daily-point">积分中心</div>
					<div class="page" >
						<jsp:include page="selfGet/point.jsp" />
					</div>
				</li>
<!-- 				<li> -->
<!-- 					<div class="title flaticon-right133" id="daily-emigrated">全民闯关</div> -->
<!-- 					<div class="page" > -->
<%-- 						<jsp:include page="daily/emigrated.jsp" /> --%>
<!-- 					</div> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<div class="title flaticon-right133" id="daily-fightTeam">全民团战</div> -->
<!-- 					<div class="page" > -->
<%-- 						<jsp:include page="daily/fightTeam.jsp" /> --%>
<!-- 					</div> -->
<!-- 				</li> -->
			</ul>
		</div>
		<div class="footer-margin" ></div>
	</div>
	<jsp:include page="commons/footer.jsp" />
	<script type="text/javascript">
		headerBar.setTitle("每日任务");
		footerBar.active("daily");
		
		var dailys = {};
		var titles = $('ul>li>.title');

		titles.bind('click',_titleClickEvent);
		
		function _titleClickEvent(){
			
			var title = $(this);
			if(title.parent().hasClass('active')){
				
				 title.parent().removeClass('active');
			}else{
				//关闭其他打开的
				 $('ul>li.active').removeClass('active');
				 title.parent().addClass('active');
			}
			
			if(!dailys[this.id]){
				switch(this.id){
					case 'daily-youhui':
						dailys[this.id] = new YouhuiPage();
						break;
					case 'daily-pt':
						dailys[this.id] = new PTPage();
						break;
					case 'daily-sign':
						dailys[this.id] = new SignPage();
						break;
					case 'daily-emigrated':
						dailys[this.id] = new EmigratedPage();
						break;
					case 'daily-fightTeam':
						dailys[this.id] = new FightTeamPage();
						break;
					case 'daily-point':
						dailys[this.id] = new PointPage();
						break;
					case 'daily-bigbang':
						dailys[this.id] = new BigBangPage();
						break;
					default:
				}
			}
		}
		
	</script>
</body>
</html>