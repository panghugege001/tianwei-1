<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>日结佣金记录</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.poshytip.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tip-skyblue/tip-skyblue.css" type="text/css" />
		
		<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}
function _commitPtCommissions(btn){
	var date = $("#_executeTime").val();
	if(date == "" || date == undefined ){
		alert("更新时间不能为空");
		return ;
	}
   var result = confirm("请确认昨天的输赢记录正确。您确定要提交日结佣金?") ;
   if (result) {
   		btn.disabled=true;
     	var action = "/batchxima/addPtCommissions.do";
		$.post(action , {"executetime":date} , function(data){
			btn.disabled=false;
			alert(data);
		});
  }
}
function _executePtCommissions(btn){
	var result = new Array();
	var ids ;
	$("[name = item][checked]:checkbox").each(function(){
		result.push($(this).attr("value"));
	});
	var len = result.length ;
	if(len>0){
		if(confirm("共选中"+len+"条数据，确认执行？")){
			btn.disabled=true;
    		var ids = result.join(",") ;
    		$.post("/batchxima/executePtCommissions.do",{"ids":ids},function(data){
    			btn.disabled=false;
    			alert(data);
    		});
    	}
	}else{
		alert("请选择需要执行的数据");
	}
	
}
$(function () {
    $("#checkAllBox").bind("click", function () {
    	if($(this).attr("checked") == "checked"){
        	$("[name = item]:checkbox").attr("checked", true);
    	}else{
    		$("[name = item]:checkbox").attr("checked", false);
    	}
    	_changeColor();
    });
    $("[name = item]:checkbox").bind("click", function () {
    	if($(this).attr("checked") != "checked"){
    		$("#checkAllBox").attr("checked", false);
    	}
    	var flag = true ;
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == undefined){
    			flag = false ;
    		}else{
    			flag = flag && $(this).attr("checked");
    		}
    	});
    	if(flag){
    		$("#checkAllBox").attr("checked", true);
    	}
    	_changeColor();
    });
    
    function _changeColor(){
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == "checked"){
    			$(this).parent().parent().find("td").css('background-color','rgb(226, 104, 104)');
    		}else{
    			$(this).parent().parent().find("td").css('background-color','white');
    		}
    	});
    }
    
/*     var flickrFeedsCache = {};
    $(".hoverMouse").poshytip({
		className: 'tip-skyblue',
		bgImageFrameSize: 3,
		offsetX: 0,
		offsetY: 10,
		content: function(updateCallback){
			var rel = $(this).attr('rel');
			if (flickrFeedsCache[rel] && flickrFeedsCache[rel].container)
				return flickrFeedsCache[rel].container;
			if (!flickrFeedsCache[rel]) {
				flickrFeedsCache[rel] = { container: null };
				var date = rel.split("@")[0];
				var agent = rel.split("@")[1];
				var platform = rel.split("@")[2];
				$.ajax({url : "${ctx}/office/getCommissionsDetail.do",
					type : "post", // 请求方式
					data : {"agent":agent,"createdate":date,"platform":platform},
					async : true,
					success : function(data) {
						console.log(data);
						var container = $('<div/>').addClass('flickr-thumbs');
						var table = $("<table/>");
						table.append("<tr><th>账号</th><th>平台</th><th>输赢</th><th>洗码</th><th>优惠</th></tr>");
						$.each(data, function(i, item) {
							table.append("<tr>"+"<td>"+item.id.agent+"</td><td>"+item.id.platform+"</td><td>"+item.profitall+"</td><td>"+item.ximafee+"</td><td>"+item.couponfee+"</td></tr>");
						});
						table.appendTo(container);
						updateCallback(flickrFeedsCache[rel].container = container);
					}
				});
			}
			return 'Loading datas...';
		}
	}); */
});

</script>
	</head>
	<body>
		<p>
			记录 --&gt; 日结佣金记录
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</p>
		
		<div id="excel_menu" style="position: absolute; top: 25px; left: 0px;">
			<s:form action="getPtCommissions" namespace="/office" name="mainform"
				id="mainform" theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1280px">
								<tr align="left">
									<td>
										代理账号:
										<s:textfield cssStyle="width:115px" name="loginname" size="20" />
									</td>
									<td>
										游戏平台:
										<s:select list="#{'':'','slotmachine':'slotmachine' , 'liveall':'liveall', 'sports':'sports', 'lottery':'lottery' }" listKey="key" listValue="value" name="platform"></s:select>
									</td>
									<td>
										起始时间:
										<s:textfield name="startTime"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{startTime}"/>
									</td>
									<td>
										vip:<s:select list="#{'':'','1':'VIP' , '0':'普通' }" listKey="key" listValue="value" name="vip"></s:select>
									</td>
									
									<td>
										代理注册起始时间:
										<s:textfield name="startDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{startDate}"/>
									</td>
									<td>代理类型:<s:select name="agentType" list="#{-1:'代理',1:'SEO',2:'电销',3:'推广',4:'广告'}" emptyOption="true"></s:select></td>
									<td rowspan="2">
										<s:submit cssStyle="width:65px; height:65px;" value="查询"></s:submit>
									</td>
								</tr>
								<tr align="left">
									<td>
										是否派发:
										<s:select list="#{'':'','0':'未派发','1':'已派发'}" listKey="key" listValue="value" name="flag"></s:select>
									</td>
									<td>
									每页记录:
										<s:select cssStyle="width:90px"
											list="%{#application.PageSizes}" name="size"></s:select>
									</td>
									<td>
										结束时间:
										<s:textfield name="endTime"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{endTime}"/>
										<s:hidden name="pageIndex" />
										<s:set name="by" value="'createTime'" />
										<s:set name="order" value="'desc'" />
										<s:hidden name="order" value="%{order}" />
										<s:hidden name="by" value="%{by}" />
									</td>
                                    <td>
                                    	代理推荐码<s:textfield cssStyle="width:65px" name="intro" size="20" />
									</td>
									
									<td>
										代理注册结束时间:
										<s:textfield name="endDate"  size="18"
											onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											My97Mark="false"  cssClass="Wdate" value="%{endDate}"/>
									</td>
									<td>危险级别<s:select list="#{'':'','0':'普通','2':'危险'}" listKey="key" listValue="value" name="warnflag"></s:select></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="left">
					<td>
						更新时间:<s:textfield  id="_executeTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false"  cssClass="Wdate"  />
						<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'market' || sessionScope.operator.authority eq 'sale_manager'}">
							<input type="button" value="提交" onclick="_commitPtCommissions(this);"/>
							<input type="button" value="批量执行" onclick="_executePtCommissions(this);"/>
						</c:if>
					</td>
					</tr>
				</table>
			</s:form>
		</div>
		<br />
		<br />
		<br />
		<br />
		<div id="middle" style="position: absolute; top: 155px; left: 0px">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1100px" border="0" cellpadding="0" cellspacing="1"
								bgcolor="#99c8d7">
								<tr bgcolor="#0084ff">
									<td bgcolor="#0084ff" align="center">
										<input type="checkbox" id="checkAllBox" />
									</td>
									
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('agent');" width="80px">
										代理账号
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('createdate');" width="90px">
										数据日期
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('platform');" width="130px">
										游戏平台
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('percent');" width="130px">
										比例
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('profitall');" width="130px">
										输赢总额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('platformfee');" width="130px">
										平台费
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('couponfee');" width="130px">
										优惠
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('depositfee');" width="130px">
										存款优惠
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('ximafee');" width="130px">
										反水
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('progressive_bets');" width="130px">
										奖池
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('amount');" width="90px">
										日佣金
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('flag');" width="130px">
										是否派发
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('slotaccount');" width="90px">
										老虎机账户余额
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="gd" onclick="orderby('createTime');" width="130px">
										创建时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('excuteTime');" width="130px">
										执行时间
									</td>
									<td align="center"
										style="color: #FFFFFF; font-weight: bold; cursor: pointer;"
										title="点击排序" onclick="orderby('remark');" width="130px">
										备注
									</td>
								</tr>
								<s:iterator var="fc" value="%{#request.page.pageContents}">
									<tr bgcolor="${bgcolor}">
										<td align="center" >
											<s:if test="#fc[7] == 0">
												<input type="checkbox" name="item" value="<s:property value="#fc[0]" />@<s:property value="#fc[2]" />@<s:date name="#fc[1]" format="yyyy-MM-dd HH:mm:ss" />" >
											</s:if>
										</td>
										
										<td align="center" bgcolor="#F0FFF0" align="center"
											width="60px" style="font-weight: bold; cursor: pointer;">
											${fc[0]}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[1]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[2]}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[4]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[12]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[13]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[14]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[19]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[15]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[18]}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[3]}
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											<s:if test="#fc[7] == 0">未派发</s:if>
											<s:if test="#fc[7] == 1">已派发</s:if>
										</td>
										<td align="center" width="90px"  bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[17]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[8]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[9]}
										</td>
										<td align="center" width="90px" bgcolor="#F0FFF0" style="font-weight: bold; cursor: pointer;">
											${fc[10]}<a rel="${fc[1]}@${fc[0]}@${fc[2]}" 
										href="${ctx}/office/getCommissionsDetail1.do?createdate=${fc[1]}&agent=${fc[0]}&platform=${fc[2]}" target="_blank">详情</a>
										</td>
									</tr>
									<s:set var="amountValue" value="#fc[3]" scope="request"></s:set>
									<c:set var="amountSum" value="${amountSum+amountValue}"
										scope="request"></c:set>
										
									<s:set var="platformValue" value="#fc[13]" scope="request"></s:set>
									<c:set var="platformSum" value="${platformSum+platformValue}"
										scope="request"></c:set>
										
									<s:set var="profitallValue" value="#fc[12]" scope="request"></s:set>
									<c:set var="profitallSum" value="${profitallSum+profitallValue}"
										scope="request"></c:set>
										
									<s:set var="couponfeeValue" value="#fc[14]" scope="request"></s:set>
									<c:set var="couponfeeSum" value="${couponfeeSum+couponfeeValue}"
										scope="request"></c:set>
										
									<s:set var="depositfeeValue" value="#fc[19]" scope="request"></s:set>
									<c:set var="depositfeeSum" value="${depositfeeSum+depositfeeValue}" scope="request"></c:set>
										
									<s:set var="ximafeeValue" value="#fc[15]" scope="request"></s:set>
									<c:set var="ximafeeSum" value="${ximafeeSum+ximafeeValue}"
										scope="request"></c:set>
										
									<s:set var="progressive_betsValue" value="#fc[18]" scope="request"></s:set>
									<c:set var="progressive_betsSum" value="${progressive_betsSum+progressive_betsValue}"
										scope="request"></c:set>
										
									<s:set var="slotValue" value="#fc[17]" scope="request"></s:set>
									<c:set var="slotSum" value="${slotSum+slotValue}"
										scope="request"></c:set>
								</s:iterator>

								<tr bgcolor="#e4f2ff">
									<td align="right" colspan="5">
										当页小计:
									</td>
									<td align="center">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.profitallSum)" />
									</td>
									<td align="center">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.platformSum)" />
									</td>
									
									<td align="center">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.couponfeeSum)" />
									</td>
									
									<td align="center">
									    <s:property value="@dfh.utils.NumericUtil@double2String(#request.depositfeeSum)" />
									</td>
									
									<td align="center">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.ximafeeSum)" />
									</td>
									
									<td align="center">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.progressive_betsSum)" />
									</td>
									
									<td align="center">
									    <s:property
											value="@dfh.utils.NumericUtil@double2String(#request.amountSum)" />
									</td>
									<td></td>
									<%-- <td><s:property value="@dfh.utils.NumericUtil@double2String(#request.slotSum)" /></td> --%>
									<td align="center" colspan="6">
									</td>
								</tr>

								<tr bgcolor="#e4f2ff">
								<td align="right" colspan="5">
										总计:
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics4)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics7)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics5)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics6)" />
									</td>
									<td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics3)" />
									</td>
									<td></td> 
									<%-- <td align="center">
										<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.page.statics6)" />
									</td> --%>
									<td align="center" colspan="6">
									</td>
								</tr>

								<tr>
									<td colspan="21" align="right" bgcolor="66b5ff" align="center">
										${page.jsPageCode}
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>

