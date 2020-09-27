<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="dfh.action.vo.AnnouncementVO"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<s:include value="/title.jsp"></s:include>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
</head>
<body>
<div class="ui-form">
	<div class="ui-form-item">
		<!-- <label for="j-nt-date" class="label">时间段:</label>
        <select name="" id="j_nt_date" class="ipt-txt">
            <option value="0">今天</option>
            <option value="-1">昨天</option>
            <option value="-3">最近三天</option>
        </select> -->
		<label for="j-nt-date" class="ui-label">开始时间：</label>
		<input type="text" readonly class="ui-ipt" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false">
		<label for="" class="ui-label">结束时间：</label>
		<input type="text" readonly class="ui-ipt" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false">
		<input type="button" class="btn btn-primary" value="查询">
	</div>
</div>

<div class="data-list">
	<table class="table data-table">
		<thead>
		<tr>
			<th>列1</th>
			<th>列2</th>
			<th>列3</th>
			<th>列4</th>
			<th>列5</th>
			<th>列6</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td>值1</td>
			<td>值2</td>
			<td>值3</td>
			<td>值4</td>
			<td>值5</td>
			<td>值6</td>
		</tr>
		<tr>
			<td>值1</td>
			<td>值2</td>
			<td>值3</td>
			<td>值4</td>
			<td>值5</td>
			<td>值6</td>
		</tr>
		</tbody>
	</table>
</div>

<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
</body>
</html>
