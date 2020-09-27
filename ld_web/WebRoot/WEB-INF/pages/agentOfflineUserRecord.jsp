<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<table>
	<tr>
		<td>
			<strong>序号</strong>
		</td>
		<td>
			<strong>会员账户</strong>
		</td>
		<td>
			<strong>状态</strong>
		</td>
		<td>
			<strong>账户额度</strong>
		</td>
		<td>
			<strong>开户日期</strong>
		</td>
		<td>
			<strong>来源网址</strong>
		</td>
	</tr>
	<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
		<tr>
			<td>
				<s:property value="#st.index+1" />
			</td>
			<td>
				<s:property value="#fc.loginname" />
			</td>
			<td>
				<s:if test="#fc.flag==@dfh.utils.Constants@ENABLE">启用</s:if>
				<s:else>禁用</s:else>
			</td>
			<td>
				<s:property value="#fc.credit" />
			</td>
			<td>
				<s:property value="#fc.tempCreateTime" />
			</td>
			<td>
				<s:property value="#fc.howToKnow" />
			</td>
		</tr>
	</s:iterator>
</table>
<table style="width: 550px; text-align: right;">
	<tr>
		<td>
			共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(1);"  class="pageA">首页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex-1});"  class="pageA">上一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex+1});"  class="pageA">下一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${page.totalPages});"  class="pageA">尾页</a>
		</td>
		 <td>活跃会员:${activeUsers}个 注册总量：${subUsers}个</td>
	</tr>
</table>



