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
			<jsp:param name="Title" value="存款红包" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="form-warp txt-form">
			<div class="form-group">
				<label class="form-label">红包余额：</label>
				<div id="hbMoney" class="form-control">0<span>元</span></div>
			</div>
			<div class="form-group zf-sele">
				<label class="form-label">红包类型：</label>
				<select id="hbSelect" name="hbSelect" class="form-control">
				</select>
			</div>
			<div class="btn-submit" onclick="doHB()">领取红包</div>
		</div>
		<div class="form-warp txt-form">
			<div class="form-group zf-sele">
				<label class="form-label">红包使用：</label>
				<select id="hbType" name="hbType" class="form-control">
					<option value="0" selected="selected"> 请选择</option>
					<option value="pt"> PT账户</option>
					<option value="ttg"> TTG账户</option>
					<!--<option value="nt"> NT账户</option>
                        <option value="qt"> QT账户</option>
                        <option value="mg"> MG账户</option>
                        <option value="dt"> DT账户</option>-->
					<option value="slot">老虎机账户(SW,MG,DT,PNG,QT,NT)</option>
				</select>
			</div>
			<div class="form-group zf-sele">
				<label class="form-label">转账金额：</label>
				<input id="hbRemit" class="form-control" type="text">
			</div>
			<div class="btn-submit " onclick="return submitHBRemit();">提交</div>
			<div class="text-tips">
				<div class="h3 bold">红包优惠说明 </div>
				<ol>
					<li>同时段红包只能领取一类红包</li>
					<li>满10元可以转到任意的平台，需5倍流水</li>
				</ol>
			</div>
		</div>

		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/SelfGetManage.js"></script>
		<script type="text/javascript">
			//---存提款红包

			//查询玩家红包余额
			function getHBMoney(type) {
				$("#hbMoney").html("<img src='/images/waiting.gif'>");
				$.post("/asp/getHBMoney.aspx", {
					"type": 0
				}, function(returnedData, status) {
					if("success" == status && returnedData != '') {
						$("#hbMoney").html("" + returnedData + "元");
					}
				});
				queryHBSelect();
			}
			// 获取玩家可领红包

			function queryHBSelect() {
				$.post("/asp/queryHBSelect.aspx", function(returnedData, status) {
					console.log(returnedData);
					if("success" == status) {
						$("#hbSelect").html("");
						$("#hbSelect").html(returnedData);
					}
				});
			}

			function doHB() {
				var hbSelect = $("#hbSelect").val();
				if(hbSelect == null || hbSelect == '') {
					alert("请选择红包类型！");
					return;
				}
				$.post("/asp/doHB.aspx", {
					"sid": hbSelect
				}, function(returnedData, status) {
					if("success" == status) {
						getHBMoney();
						alert(returnedData);
					}
				});
				return false;

			}

			function submitHBRemit() {
				var hbType = $("#hbType").val();
				var hbRemit = $("#hbRemit").val();
				if(hbType == "") {
					alert("请选择平台！");
					return false;
				}
				if(hbMoney != "") {
					if(isNaN(hbRemit)) {
						alert("转账金额非有效数字！");
						return false;
					}
					if(hbRemit < 10) {
						alert("转账金额必须大于10！");
						return false;
					}
				}

				$.post("/asp/submitHBRemit.aspx", {
					"type": hbType,
					"transferGameIn": hbRemit
				}, function(returnedData, status) {
					if("success" == status) {
						getHBMoney();
						alert(returnedData);
					}
				});
				return false;
			}

			getHBMoney();
			// ---存提款红包结束
		</script>
	</body>

</html>