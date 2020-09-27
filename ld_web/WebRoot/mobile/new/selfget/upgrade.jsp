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
			<jsp:param name="Title" value="自助晋级" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="content mt5">
			<div class="mui-col-xs32-12 mui-col-xs48-10 mui-col-xs48-offset-1 mui-col-xs64-8 mui-col-xs64-offset-2 ">
				<div class="panel mui-tabs__pane mui--is-active" id="pane-help-month">
					<table id="upgrade-month-grid" width="100%"></table>
				</div>
			</div>
			<div class="form-warp">
				<div class="btn-submit" id="upgrade-submit">检测升级</div>
			</div>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/MobileGrid.js"></script>
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			UpgradePage()

			function UpgradePage() {
				var that = this;
				that.type = 'month';
				that.upgradeMonthGrid = false;

				that.submit = $('#upgrade-submit');
				that.submit.click(_checkUpgrade);

				initMonthGrid();

				//本月
				function initMonthGrid() {
					that.upgradeMonthGrid = new MobileGrid({
						appendId: 'upgrade-month-grid',
						dataUrl: 'mobi/queryBetOfPlatform.aspx',
						showIndex: true,
						showMore: false,
						showLoadMore: false,
						columnModel: {
							recordColumns: [
								new MobileGridColumn({
									value: 'platform',
									name: '游戏平台',
									cls: 'm-grid-text',
									width: '30%',
									align: 'center'
								}),
								new MobileGridColumn({
									value: 'bet',
									name: '投注额',
									cls: 'm-grid-text',
									width: '30%',
									align: 'center'
								})
							],
							contentColumns: []
						},
						onLoad: function(result) {
							if(!result.success) {
								alert(result.message);
							}
						}
					});
					that.upgradeMonthGrid.init();
					that.upgradeMonthGrid.load();
				}

				//检测升级
				function _checkUpgrade() {
					mobileManage.getLoader().open('检测中');
					mobileManage.getUserManage().userUpgrade({
						helpType: that.type
					}, function(result) {
						if(result.success) {
							alert(result.message);
						} else {
							alert(result.message);
						}
						that.upgradeMonthGrid.load();
						mobileManage.getLoader().close();
					});
				}
			}
		</script>
	</body>

</html>