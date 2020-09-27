<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>优惠券配置</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<p>操作&nbsp;--&gt;&nbsp;优惠券配置&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
			<s:form action="queryCouponConfigList" namespace="/office" name="mainform" id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>优惠券类型：<s:select list="#{}" name="couponConfig.couponType" id="couponType" listKey="key" listValue="value" cssStyle="width: 110px;"></s:select></td>
									<td id="cs1">游戏平台：<s:select list="#{}" name="couponConfig.platformId" id="platformId" listKey="key" listValue="value" cssStyle="width: 90px;"></s:select></td>
									<td>优惠券代码：<s:textfield name="couponConfig.couponCode" id="couponCode"></s:textfield></td>
									<td>领取账号：<s:textfield name="couponConfig.loginName" id="loginName"></s:textfield></td>
									<td>创建人：<s:textfield name="couponConfig.createUser" id="createUser"></s:textfield></td>
									<td rowspan="2"><s:submit cssStyle="width: 65px; height: 55px;" value="查询"></s:submit></td>
								</tr>
								<tr align="left">
									<td>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：<s:select list="#{ '': '', '0': '待审核', '1': '已审核', '2': '已领取' }" name="couponConfig.status" id="status" listKey="key" listValue="value" cssStyle="width: 110px;"></s:select></td>
									<td>每页记录：<s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select></td>
									<td colspan="3">创建时间：<s:textfield name="start" id="start" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" cssClass="Wdate" /> - <s:textfield name="end" id="end" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" cssClass="Wdate" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
				<s:hidden name="pageIndex" />
			</s:form>
		</div>
		<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='sale' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager'">
			<div style="position: absolute; top: 115px; left: 0px">
				<a href="/office/functions/addCouponConfig.jsp" target="_blank" style="color: red; font-size: 14px; margin-left: 10px;">新增优惠券配置</a>
			</div>
		</s:if>	
		<br />
		<div style="position: absolute; top: 140px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<input type="button" value="审 核" id="audit" />&nbsp; 
							<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager'">
								<input type="button" value="删 除" id="delete" />&nbsp;
							</s:if>
							<s:if test="#session.operator.username=='boots'">
								<input type="button" value="修复数据(2018-07-12)" id="checkTopicStatusData" />&nbsp;
							</s:if>
							<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;"><input type="checkbox" id="checkAllBox" /></td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 60px;">优惠券类型</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 50px;">游戏平台</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">优惠券代码</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">赠送百分比</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">赠送金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">流水倍数</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">最低转账金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">最高转账金额</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">赠送金额上限</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">状态</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">领取账号</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">创建时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">创建人</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">审核时间</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">审核人</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">备注</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center"><input type="checkbox" name="item" value="<s:property value="#fc.id"/>#<s:property value="#fc.status"/>" /></td>
									<td align="left">
										<s:if test="#fc.couponType=='419'">红包优惠券</s:if>
										<s:if test="#fc.couponType=='319'">存送优惠券</s:if>
									</td>
									<td align="center"><s:property value="#fc.platformName" /></td>
									<td align="left"><s:property value="#fc.couponCode" /></td>
									<td align="center"><s:property value="#fc.percent" /></td>
									<td align="center"><s:property value="#fc.giftAmount" /></td>
									<td align="center"><s:property value="#fc.betMultiples" /></td>
									<td align="center"><s:property value="#fc.minAmount" /></td>
									<td align="center"><s:property value="#fc.maxAmount" /></td>
									<td align="center"><s:property value="#fc.limitMoney" /></td>
									<td align="center" width="50px">
										<s:if test="#fc.status=='0'.toString()">待审核</s:if> 
										<s:if test="#fc.status=='1'.toString()">已审核</s:if> 
										<s:if test="#fc.status=='2'.toString()">已领取</s:if>
									</td>
									<td align="left"><s:property value="#fc.loginName" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" /></td>
									<td align="left"><s:property value="#fc.createUser" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.auditTime" /></td>
									<td align="left"><s:property value="#fc.auditUser" /></td>
									<td align="left"><s:property value="#fc.remark" /></td>
								</tr>
								</s:iterator>
								<tr>
									<td colspan="17" align="right" bgcolor="66b5ff">${page.jsPageCode}</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="${ctx}/office/script.jsp" />
		<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
		<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
		<script type="text/javascript" src="${ctx}/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
		
		$(document).ready(function() {

			var coupon = $("#couponType");
			coupon.empty();

			for (var i = 0, len = coupon_type.length; i < len; i++) {

				coupon.append("<option value='" + coupon_type[i].value + "'>" + coupon_type[i].text + "</option>");
			}

			coupon.val("${couponConfig.couponType}");

			var platform = $("#platformId");
			platform.empty();
			platform.append("<option value=''></option>");

			for (var i = 0, len = coupon_platform.length; i < len; i++) {

				platform.append("<option value='" + coupon_platform[i].value + "'>" + coupon_platform[i].text + "</option>");
			}

			platform.val("${couponConfig.platformId}");

			$("#checkAllBox").bind("click", function() {

				$("[name='item']:checkbox").attr("checked", $(this).is(':checked'));
			});

			$("#delete").bind("click", function() {

				remove();
			});

            $("#checkTopicStatusData").bind("click", function() {

                checkTopicStatusData();
            });

			$("#audit").bind("click", function() {

				audit();
			});

			$("#couponType").bind("change", platformChange);

			platformChange();
		});

		function gopage(val) {

			document.mainform.pageIndex.value = val;
			document.mainform.submit();
		};

		function orderby(by) {

			if (document.mainform.order.value == "desc") {

				document.mainform.order.value = "asc";
			} else {

				document.mainform.order.value = "desc";
			}

			document.mainform.by.value = by;
			document.mainform.submit();
		};

		function platformChange() {

			if ($("#couponType").val() == "419") {

				$("td[id^='cs']").hide();
				$("#platformId").val("");
			} else {

				$("td[id^='cs']").show();
			}
		}

		function remove() {

			if (window.confirm('确认要删除所选中的数据吗？')) {

				var arr = [];
				var data = [];

				$("input[name='item']:checked").each(function() {

					var str = $(this).val();
					var id = str.split("#")[0];
					var status = str.split("#")[1];

					if (status == "0") {

						arr.push(id);
					}

					data.push(str);
				});

				if (data.length == 0) {

					alert("未选中任何数据，请选择需要删除的数据！");
					return;
				}

				if (arr.length == 0) {

					alert("只允许删除状态为“待审核”的数据！");
					return;
				}

				var ids = arr.join(',');

				var action = "/office/batchDeleteCouponConfig.do";

				var data = "ids=" + ids;

				var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete : function(result) {

					var text = result.responseText;

					alert(text);

					if (text.indexOf('成功') != -1) {

						document.mainform.submit();
					}
				}});
			}
		};

		function audit() {
	
			var arr = [];
			var data = [];
			var loginName = "";
	
			$("input[name='item']:checked").each(function() {
	
				var str = $(this).val();
				var id = str.split("#")[0];
				var status = str.split("#")[1];
	
				if (status == "0") {
	
					arr.push(id);
				}
	
				data.push(str);
			});
	
			if (data.length == 0) {
	
				alert("未选中任何数据，请选择需要审核的数据！");
				return;
			}
	
			if (arr.length == 0) {
	
				alert("只允许审核状态为“待审核”的数据！");
				return;
			}
	
			if (arr.length == 1) {
	
				loginName = window.prompt("请填写会员账号", "");
			}
	
			var ids = arr.join(',');
	
			var action = "/office/batchAuditCouponConfig.do";
	
			var data = "ids=" + ids + "&loginname=" + loginName;
	
			var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {
	
				var text = result.responseText;
		
				alert(text);
		
				if (text.indexOf('成功') != -1) {
		
					document.mainform.submit();
				}
			}});
		};

        function checkTopicStatusData() {
            var data = [];
            var action = "/office/checkTopicStatusData.do";
            var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete : function(result) {
                    var text = result.responseText;
                    alert(text);
                    if (text.indexOf('成功') != -1) {
                        document.mainform.submit();
                    }
                }});
        };
		</script>
	</body>
</html>