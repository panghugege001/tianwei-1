<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>代理流量统计</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	</head>
	<script>
	function formfunction0(){
	    $("#mainform0").submit();
	}
	function formfunction1(){
	    $("#mainform1").submit();
	}
	function formfunction2(){
	    $("#mainform2").submit();
	}
	function formfunction3(){
	    $("#mainform3").submit();
	}
	function formfunction4(){
	    $("#mainform4").submit();
	}
	$(function() {
	tableSort($('table'));
})

function tableSort(jqTableObj) {
	jqTableObj.find('thead th').click(
		function(){
			var dataType = $(this).attr('dataType');
			var tableObj = $(this).closest('table');
			var index = tableObj.find('thead th').index(this) + 1;
			var arr = [];
			var row = tableObj.find('tbody tr');
			
			$.each(row, function(i){arr[i] = row[i]});
			
			if($(this).hasClass('current')){
				arr.reverse();
			} else {
				arr.sort(Utils.sortStr(index, dataType))
				
				tableObj.find('thead th').removeClass('current');
				$(this).addClass('current');
			}
			
			var fragment = document.createDocumentFragment();
			
			$.each(arr, function(i){
				fragment.appendChild(arr[i]);
			});
			tableObj.find('tbody').append(fragment);
		}
	);	
	
	var Utils = (function() {
		function sortStr(index, dataType){
			return function(a, b){
				var aText=$(a).find('td:nth-child(' + index + ')').attr('_order') || $(a).find('td:nth-child(' + index + ')').text();
				var bText=$(b).find('td:nth-child(' + index + ')').attr('_order') || $(b).find('td:nth-child(' + index + ')').text();
		
				if(dataType != 'text'){
					aText=parseNonText(aText, dataType);
					bText=parseNonText(bText, dataType);
					
					return aText > bText ? -1 : bText > aText ? 1 : 0;
				} else {
					return aText.localeCompare(bText)
				}
			}
		}
		
		function parseNonText(data, dataType){
			switch(dataType){
				case 'int':
					return parseInt(data) || 0
				case 'float':
					return parseFloat(data) || 0
				default :
				return filterStr(data)
			}
		}
		
		//过滤中文字符和$
		function filterStr(data){
			if (!data) {
				return 0;
			}
			
			return parseFloat(data.replace(/^[\$a-zA-z\u4e00-\u9fa5 ]*(.*?)[a-zA-z\u4e00-\u9fa5 ]*$/,'$1'));
		}
		
		return {'sortStr' : sortStr};
	})();
}
	</script>
	<body>
		<div>
			账户 --&gt; 代理流量
		</div>
		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">
							<table width="1350px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<s:form action="getProxyFlowList" namespace="/office"
									name="mainform" id="mainform" theme="simple">
									<tr>
										<td align="right" width="60px">
											代理账号:
										</td>
										<td align="left" width="100px">
											<s:textfield name="loginname" size="30"></s:textfield>
										</td>
										<td align="right" width="60px">
											开始时间:
										</td>
										<td width="110px" align="left">
											<s:textfield name="start" size="18"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												My97Mark="false" value="%{startTime}" cssClass="Wdate" />
										</td>
										
										<td colspan="8" width="60px">
											<input type="hidden" name="currentPage"
												value="${currentPage}" />
											<input type="hidden" name="countOfPage" id="countOfPage"
												value="${countOfPage}" />
											<s:submit cssStyle="width:60px; height:30px;" value="查询"></s:submit>
										</td>
									</tr>
									<tr>
										<td align="right" width="60px">
											代理推荐码:
										</td>
										<td align="left" width="100px">
											<s:textfield name="partner" size="30"></s:textfield>
										</td>
										<td align="right" width="60px">
											结束时间:
										</td>
										<td width="110px" align="left">
											<s:textfield name="end" size="18"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												My97Mark="false" value="%{endTime}" cssClass="Wdate" />
										</td>
									</tr>
								</s:form>
							</table>
							<table width="1350px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<thead>
									<tr>
										<th bgcolor="#0084ff" align="center" height="40px;"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											序号
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											代理账号
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											活跃会员量
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											会员注册量
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											开户存款转化率
										</th>

										<th bgcolor="#0084ff" align="center" datatype="float"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											赢利额
										</th>

										<th bgcolor="#0084ff" align="center" datatype="float"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											投注额
										</th>

										<th bgcolor="#0084ff" align="center" datatype="float"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											佣金
										</th>

										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											IP
										</th>

										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											PV
										</th>

										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											注册时间
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											流量最终
										</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="s" varStatus="itemIndex">
										<tr>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${itemIndex.count}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.agent}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.agentActiveCount}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.agentCount}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.registerDeposit}
											</td>

											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.amountProfit}
											</td>

											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.bettotal}
											</td>

											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.commission}
											</td>

											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.ipAccess}
											</td>

											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.pvAccess}
											</td>

											<td align="center" bgcolor="#F0FFF0" align="center">
												${s.createtime}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												<a
													href="${ctx}/office/queryIplist.do?agent_website=${s.referWebsite}&start=2007-01-01 00:00:00&end=2030-01-01 00:00:00"
													target="_blank">${s.referWebsite}</a>
											</td>
										</tr>

										<c:set var="agentActiveCount" value="${s.agentActiveCount}"
											scope="request"></c:set>
										<c:set var="agentActiveCountSum"
											value="${agentActiveCountSum+agentActiveCount}"
											scope="request"></c:set>

										<c:set var="agentCount" value="${s.agentCount}"
											scope="request"></c:set>
										<c:set var="agentCountSum" value="${agentCountSum+agentCount}"
											scope="request"></c:set>

										<c:set var="amountProfit" value="${s.amountProfit}"
											scope="request"></c:set>
										<c:set var="amountProfitSum"
											value="${amountProfitSum+amountProfit}" scope="request"></c:set>


										<c:set var="bettotal" value="${s.bettotal}" scope="request"></c:set>
										<c:set var="bettotalSum" value="${bettotalSum+bettotal}"
											scope="request"></c:set>

										<c:set var="commission" value="${s.commission}"
											scope="request"></c:set>
										<c:set var="commissionSum" value="${commissionSum+commission}"
											scope="request"></c:set>

										<c:set var="ipAccess" value="${s.ipAccess}" scope="request"></c:set>
										<c:set var="ipAccessSum" value="${ipAccessSum+ipAccess}"
											scope="request"></c:set>

										<c:set var="pvAccess" value="${s.pvAccess}" scope="request"></c:set>
										<c:set var="pvAccessSum" value="${pvAccessSum+pvAccess}"
											scope="request"></c:set>

									</c:forEach>
								</tbody>
							</table>
							<table width="1350px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<tr>
									<td colspan="18" bgcolor="66b5ff" align="right">
										<s:form action="getProxyFlowList" namespace="/office"
											name="mainform0" id="mainform0" theme="simple">
											<input type="hidden" value="${startTime}" name="start" />
											<input type="hidden" value="${endTime}" name="end" />
											<input type="hidden" name="loginname" value="${loginname}" />
											<input type="hidden" name="currentPage" value="1" />
											<input type="hidden" name="countOfPage" id="countOfPage"
												value="${countOfPage}" />
										</s:form>
										<s:form action="getProxyFlowList" namespace="/office"
											name="mainform1" id="mainform1" theme="simple">
											<input type="hidden" value="${startTime}" name="start" />
											<input type="hidden" value="${endTime}" name="end" />
											<input type="hidden" name="loginname" value="${loginname}" />
											<input type="hidden" name="currentPage"
												value="${currentPage-1}" />
											<input type="hidden" name="countOfPage" id="countOfPage"
												value="${countOfPage}" />
										</s:form>
										<s:form action="getProxyFlowList" namespace="/office"
											name="mainform2" id="mainform2" theme="simple">
											<input type="hidden" value="${startTime}" name="start" />
											<input type="hidden" value="${endTime}" name="end" />
											<input type="hidden" name="loginname" value="${loginname}" />
											<input type="hidden" name="currentPage"
												value="${currentPage+1}" />
											<input type="hidden" name="countOfPage" id="countOfPage"
												value="${countOfPage}" />
										</s:form>
										<s:form action="getProxyFlowList" namespace="/office"
											name="mainform4" id="mainform4" theme="simple">
											<input type="hidden" value="${startTime}" name="start" />
											<input type="hidden" value="${endTime}" name="end" />
											<input type="hidden" name="loginname" value="${loginname}" />
											<input type="hidden" name="currentPage"
												value="${currentPageAll}" />
											<input type="hidden" name="countOfPage" id="countOfPage"
												value="${countOfPage}" />
										</s:form>
										<s:form action="getProxyFlowList" namespace="/office"
											name="mainform3" id="mainform3" theme="simple">
											活跃会员量:${agentActiveCountSum}&nbsp;
										会员注册量:${agentCountSum}&nbsp;
										赢利额:
										<fmt:formatNumber value="${amountProfitSum}" pattern="##.##"
												minFractionDigits="2"></fmt:formatNumber>  
										&nbsp;
										投注额:
										<fmt:formatNumber value="${bettotalSum}" pattern="##.##"
												minFractionDigits="2"></fmt:formatNumber>  
										&nbsp;
										佣金:
										<fmt:formatNumber value="${commissionSum}" pattern="##.##"
												minFractionDigits="2"></fmt:formatNumber>  
										&nbsp;
										IP:${ipAccessSum}&nbsp;
										PV:${ipAccessSum}&nbsp;
											<input type="hidden" value="${startTime}" name="start" />
											<input type="hidden" value="${endTime}" name="end" />
											<input type="hidden" name="loginname" value="${loginname}" />
											<input type="hidden" name="currentPage"
												value="${currentPage}" />
											当前第${currentPage}页 <a href="javaScript:void();"
												onclick="formfunction0();">首页</a>
											<a href="javaScript:void();" onclick="formfunction1();">上一页</a>
											<a href="javaScript:void();" onclick="formfunction2();">下一页</a>
											<a href="javaScript:void();" onclick="formfunction4();">尾页</a>
											共${currentPageAll}页&nbsp;共${countOfPageAll}条
											<select onchange="formfunction3();" name="countOfPage"
												id="countOfPageoption">
												<c:if test="${countOfPage==null}">
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="50">
														50
													</option>
													<option value="100">
														100
													</option>
													<option value="500">
														500
													</option>
													<option value="1000">
														1000
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==10}">
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="50">
														50
													</option>
													<option value="100">
														100
													</option>
													<option value="500">
														500
													</option>
													<option value="1000">
														1000
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==20}">
													<option value="20">
														20
													</option>
													<option value="10">
														10
													</option>
													<option value="50">
														50
													</option>
													<option value="100">
														100
													</option>
													<option value="500">
														500
													</option>
													<option value="1000">
														1000
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==50}">
													<option value="50">
														50
													</option>
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="100">
														100
													</option>
													<option value="500">
														500
													</option>
													<option value="1000">
														1000
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==100}">
													<option value="100">
														100
													</option>
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="50">
														50
													</option>
													<option value="500">
														500
													</option>
													<option value="1000">
														1000
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==500}">
													<option value="500">
														500
													</option>
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="50">
														50
													</option>
													<option value="100">
														100
													</option>
													<option value="1000">
														1000
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==1000}">
													<option value="1000">
														1000
													</option>
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="50">
														50
													</option>
													<option value="100">
														100
													</option>
													<option value="500">
														500
													</option>
													<option value="2000">
														2000
													</option>
												</c:if>
												<c:if test="${countOfPage==2000}">
													<option value="2000">
														2000
													</option>
													<option value="500">
														500
													</option>
													<option value="100">
														100
													</option>
													<option value="50">
														50
													</option>
													<option value="10">
														10
													</option>
													<option value="20">
														20
													</option>
													<option value="1000">
														1000
													</option>
												</c:if>
											</select>
										</s:form>
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




