<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=request.getRequestURL()%>" />
	<title>天威娱乐城--明细查询</title>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
           <style type="text/css">
		.data-list { padding-top:10px; }
		.table { width: 98%; margin: 0 10px 10px; border: 1px solid #bababa; }
		.table tr th { color:#fff; }
		.table tr:nth-child(even) { background-color:#f0f0f0; }
		.table td { background:none; border:none; color: #342923; }
		.ipt-group{ margin:30px 0 30px 20px; color: #342923;}
		</style>
</head>

<body>
<div class="data-list">
	<s:url action="searchXima" namespace="/asp" var="selfDetailUrl"></s:url>
	<form name="salfFrom" action="${selfDetailUrl }" method="post"
		  onsubmit="return checkselfform()">
		<div class="ipt-group">
			<label class="label">起点时间：</label>
			<input type="text" class="ipt-txt"
									   name="startTime" value="${requestScope.startTime }" id="startTime"
									   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									   My97Mark="false" />
			<label class="label">截止时间：</label>
			<input type="text" class="ipt-txt" name="endTime" value="${requestScope.endTime }"
			id="endTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" />
			<input type="submit" class="btn" value="查询" style="background: #dfa85a; color: #FFFFFF;" />
		</div>

		<table class="table data-table">
			<thead>
			<tr>
				<th>序号</th>
				<th>编号</th>
				<th>统计时间段</th>
				<th>有效投注额</th>
				<th>结算金额</th>
				<th>洗码类型</th>
				<th>洗码率</th>
				<th>状态</th>
			</tr>
			</thead>
			<tbody>
			<s:if test="ximaList!=null&&ximaList.size()>0">
				<s:iterator value="ximaList" var="x" status="st">
					<tr>
						<td><s:property value="#st.index+1" /></td>
						<td><s:property value="pno" /></td>
						<td><s:property value="statisticsTimeRange" /></td>
						<td class="c-red"><s:property
								value="@dfh.utils.NumericUtil@double2String(#request.validAmount)" /></td>
						<td class="c-red"><s:property
								value="@dfh.utils.NumericUtil@double2String(#request.ximaAmount)" /></td>
						<td><s:property value="ximaType" /></td>
						<td><s:property value="rate" /></td>
						<td><s:property value="ximaStatus" /></td>
					</tr>
				</s:iterator>
			</s:if>
			</tbody>

		</table>
		<input type="hidden" name="maxRowsno" value="10"/>
		<div class="pagination">
			共&nbsp;
			<s:property value="totalRowsno" />
			&nbsp;条记录&nbsp;&nbsp; 每页&nbsp;
			<s:property value="maxRowsno" />
			&nbsp;条记录&nbsp;&nbsp; 共&nbsp;
			<s:property value="totalPageno" />
			&nbsp;页&nbsp;&nbsp; 第&nbsp;
			<s:property value="pageno" />
			/
			<s:property value="totalPageno" />
			&nbsp;页&nbsp;&nbsp;&nbsp;
			<s:else>
				<a
						href="${selfDetailUrl }?pageno=1&startTime=${startTime}&endTime=${endTime}&maxRowsno=${maxRowsno}">首页</a>&nbsp;&nbsp;
				<a
						href="${selfDetailUrl }?pageno=${pageno-1 }&startTime=${startTime}&endTime=${endTime}&maxRowsno=${maxRowsno}">上一页</a>&nbsp;&nbsp;
				<a
						href="${selfDetailUrl }?pageno=${pageno+1}&startTime=${startTime}&endTime=${endTime}&maxRowsno=${maxRowsno}">下一页</a>&nbsp;&nbsp;
				<a
						href="${selfDetailUrl }?pageno=${totalPageno }&startTime=${startTime}&endTime=${endTime}&maxRowsno=${maxRowsno}">尾页</a>&nbsp;
			</s:else>

		</div>
	</form>
</div>

	<script type="text/javascript">
		function dataLoad(url) {
			document.location.href = url;
		}

		function checkselfform() {
			if (document.salfFrom.startTime.value == "") {
				alert("起始时间不能为空");
				return false;
			} else if (document.salfFrom.endTime.value == "") {
				alert("截止时间不能为空");
				return false;
			}
			return true;

		}
	</script>
	<s:url value="/scripts/My97DatePicker/WdatePicker.js"
		var="WdatePickerUrl"></s:url>
	<script type="text/javascript" src="${WdatePickerUrl}"></script>
</body>
</html>