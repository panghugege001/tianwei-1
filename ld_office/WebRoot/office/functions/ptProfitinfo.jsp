<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.PtGameCode"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>NT游戏记录</title>
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
	<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
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
								<s:form action="getPtListInfo" namespace="/office"
									name="mainform" id="mainform" theme="simple">
									<tr>
										<td align="right" width="60px">
											玩家账号:
										</td>
										<td align="left" width="200px">
											<s:textfield name="username" size="30"></s:textfield>
										</td>
										<td align="right" width="60px">
											开始时间:
										</td>
										<td width="110px" align="left">
											<s:textfield name="start" size="18"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												My97Mark="false" value="%{start}" cssClass="Wdate" />
										</td>
										<td align="right" width="60px">
											结束时间:
										</td>
										<td width="110px" align="left">
											<s:textfield name="end" size="18"
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												My97Mark="false" value="%{end}" cssClass="Wdate" />
										</td>
										<td width="20px;">

										</td>
										<td>
											<s:submit cssStyle="width:60px; height:30px;" value="查询"></s:submit>
										</td>
										<td width="300px">
											<s:hidden name="pageIndex" value="1"></s:hidden>
											<s:set name="by" value="'playtime'" />
											<s:set name="order" value="'desc'" />
											<s:hidden name="order" value="%{order}" />
											<s:hidden name="by" value="%{by}" />
										</td>
									</tr>
								</s:form>
							</table>
							<table width="1350px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<thead>
									<tr>
										<th bgcolor="#0084ff" align="center" height="20px;"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											序号
										</th>
										<th bgcolor="#0084ff" align="center" height="40px;"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold">
											NT平台账号
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('loginname');">
											龙都账号
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('amount');">
											余额
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('bet');">
											线注
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('line');">
											线数
										</th>

										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('multiplier');">
											倍数
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('gameCode');">
											游戏代号
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('payOut');">
											赔付
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('playtime');">
											游戏时间
										</th>
										<th bgcolor="#0084ff" align="center"
											style="font-size: 13px;; color: #FFFFFF; font-weight: bold"
											title="点击排序" onclick="orderby('type');">
											操作类型
										</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="fc" varStatus="itemIndex">
										<tr>
											<td align="center" bgcolor="#F0FFF0" align="center">
											    ${itemIndex.count }
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${fc.ntName}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${fc.loginname}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${fc.amount}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${fc.multiplier}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${fc.line}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												${fc.bet}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												<%
												dfh.model.PtStatistical pt=(dfh.model.PtStatistical)pageContext.getAttribute("fc");
												out.print(PtGameCode.getText(pt.getGameCode()));
												%>
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center}">
												${fc.payOut}
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												<fmt:formatDate value="${fc.playtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
											</td>
											<td align="center" bgcolor="#F0FFF0" align="center">
												<c:if test="${fc.type==0}">旋转</c:if>
												<c:if test="${fc.type==1}">赌博</c:if>
												<c:if test="${fc.type==2}">免费旋转</c:if>
												<c:if test="${fc.type==3}">奖池</c:if>
												<c:if test="${fc.type==4}">奖金游戏</c:if>
											</td>
										</tr>
										<c:set var="payOutValue" value="${fc.payOut}" scope="request"></c:set>
										<c:set var="payOutSum" value="${payOutSum+payOutValue}"
											scope="request"></c:set>
										
										<c:set var="payBetValue" value="${(fc.multiplier*fc.line*fc.bet)/100}" scope="request"></c:set>
										<c:set var="payBetSum" value="${payBetSum+payBetValue}"
											scope="request"></c:set>
												
									</c:forEach>
								</tbody>
							</table>
							<table width="1350px" border="0" align="center" cellpadding="0"
								cellspacing="1" bgcolor="#99c8d7">
								<tr>
								    <td bgcolor="#e4f2ff" align="right" colspan="8">
										输赢:<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.payBetSum-#request.payOutSum)" />
											
										投注额:<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.payBetSum)" />	
											
										赔付:<s:property
											value="@dfh.utils.NumericUtil@double2String(#request.payOutSum)" />	
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


