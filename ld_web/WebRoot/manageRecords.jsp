<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>

	<head lang="en">
		<base href="<%=request.getRequestURL()%>" />
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/user.css?v=7" />

		<body class="user_body">
			<div class="index-bg about-bj">
				<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
				<div class="user_center"></div>
				<div class="container w_357">
					<jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
					<div class="cfx about-main">
						<div class="gb-sidenav">
							<jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>
						</div>
						<div id="j-record-form" class="tab-bd user-main zhangdan">
							<ul id="j-records-action" class="ul-sidebar">
								<li class="active">
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/depositRecords.aspx">在线存款</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/transferRecords.aspx">户内转账</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/cashinRecords.aspx">手工存款</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/withdrawRecords.aspx">提款记录</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/consRecords.aspx">优惠活动</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/couponRecords.aspx">优惠券</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/selfDetail_member.jsp">自助返水</a>
								</li>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/depositOrderRecord.aspx">存款附言</a>
								</li>
								<%-- <li><a href="#j-record-form" data-toggle=tab data-href="/asp/queryTaskRecords.aspx"></a></li>--%>
								<li>
									<a href="#j-record-form" data-toggle="tab" data-href="/asp/queryfriendRecord.aspx">好友推荐</a>
								</li>
								<%--<li><a href="#j-record-form" data-toggle="tab" data-href="/asp/querypointRecord.aspx">积分中心</a></li>--%>
								<%-- <li><a href="#j-record-form" data-toggle="tab" data-val="girl" data-href="/asp/queryFlowerRanking.aspx">守护女神</a></li>--%>
							</ul>
							<div class="user-tab-box">
								<iframe src="" width="100%" height="780" border="0" scrolling="no" frameborder="0" allowtransparency="true" class="if-record">                  	
                    </iframe>
								<div id="j-ret"></div>
							</div>
						</div>
					</div>

				</div>
			</div>

			<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
			<script src="${ctx}/js/userRecords.js"></script>
			<script>
				var pageInfo = {
					'pageIndex': 1,
					'size': 10
				};
				var $recordsIframe = $('#j-record-form'),
					$recordsAction = $('#j-records-action'),
					$iframe = $recordsIframe.find('iframe'),
					$recordTitle = $recordsIframe.find('.tab-tit1');
				$(function() {
					// console.log('test');
					$recordsAction.find('a').on('click', (function() {
						var $this = $(this);
						$recordTitle.text($this.text());
						if($this.data('val') == 'girl') {
							$iframe.hide();
							$.post($this.attr('data-href'), pageInfo, function(data) {
								$('#j-ret').html(data);
							})
						} else {
							$iframe.show();
							$('#j-ret').html('');
							$iframe.attr('src', '');
							$iframe.attr('src', $this.attr('data-href'));

						}

					}));

					$recordsAction.find('a').first().trigger('click');
				});
			</script>
			<%--女神鲜花查询--%>
			<script type="text/javascript">
				function gopage(val) {
					$iframe.hide();
					pageInfo.pageIndex = val || 1;
					$.post('/asp/queryFlowerRanking.aspx', pageInfo, function(data) {
						$('#j-ret').html(data);
					})
				}

				function queryFlowerRecord() {
					gopage(1);
				}

				function queryFlowerRecordTwo(val) {
					gopage(val);
				}
			</script>
		</body>

</html>