<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
          <style type="text/css">
		.data-list { padding-top:10px; }
		.table { width: 98%; margin: 0 10px 10px; border: 1px solid #bababa; }
		.table tr th { color:#fff; }
		.table tr:nth-child(even) { background-color:#f0f0f0; }
		.table td { background:none; border:none; color: #342923; } 
		</style>
<table class="table">
	<tr>
		<th>序号</th>
		<th>操作类型</th>
		<th>额度变量</th>
		<th>改变前额度</th>
		<th>改变后额度</th>
		<th>加入时间</th>
	</tr>
	<s:if test="list!=null&&list.size()>0">
		<s:iterator value="list" var="x" status="st">
			<tr>
				<td>
					<s:property value="#st.index+1" />
				</td>
				<td>
					<s:property
						value="@dfh.model.enums.CreditChangeType@getText(#request.type)" />
				</td>
				<td>
					<s:property
						value="@dfh.utils.NumericUtil@double2String(#request.remit)" />
				</td>
				<td>
					<s:property
						value="@dfh.utils.NumericUtil@double2String(#request.credit)" />
				</td>
				<td>
					<s:property
						value="@dfh.utils.NumericUtil@double2String(#request.newCredit)" />
				</td>
				<td>
					<s:property value="#request.tempCreateTime" />
				</td>
			</tr>
		</s:iterator>
	</s:if>
</table>
<div class="pagination">
	<span class="page-info">
		共<s:property value="totalrowsno" />条 每页<s:property value="maxpageno" />条 当前<s:property value="pageno" />/<s:property value="totalpageno" />
	</span>
	<a class="first-page" href="javaScript:void(0);" onclick="agentAmountRecordTwo(1);">首页</a>
	<a class="prev-page" href="javaScript:void(0);"
		onclick="agentAmountRecordTwo(${pageno-1});">上一页</a>
	<a class="next-page" href="javaScript:void(0);"
		onclick="agentAmountRecordTwo(${pageno+1});">下一页</a>
	<a class="last-page" href="javaScript:void(0);"
		onclick="agentAmountRecordTwo(${totalpageno});">尾页</a>
</div>
									