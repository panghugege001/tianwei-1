<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<table class="w mb20 ml15">
    <tr>
        <td>会员总人数：<em class="c-strong">${subUsers}</em></td>
        <td>当前活跃人数：<em class="c-strong">${activeUsers}</em> </td>
    </tr>
</table>
<div class="table data-table">
    <table>
        <tbody>
        <tr>
            <th>会员帐号</th>
            <th>状态</th>
            <th>开户日期</th>
            <th>龙都帐号额度</th>
            <th>来源网址</th>
        </tr>
        <s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
        <tr>
            <td><s:property value="#fc.loginname" /></td>
            <td>
            	<s:if test="#fc.flag==@dfh.utils.Constants@ENABLE">启用</s:if>
				<s:else>禁用</s:else>
			</td>
            <td><s:property value="#fc.tempCreateTime" /></td>
            <td><s:property value="#fc.credit" /></td>
            <td><s:property value="#fc.howToKnow" /></td>
        </tr>
        </s:iterator>
        </tbody>
    </table>
    <div class="pagination">
        <span class="page-info">
            共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
        </span>
        <a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(1);"  class="first-page">首页</a>
        <a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex-1});"  class="prev-page">上一页</a>
        <a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${pageIndex+1});"  class="next-page">下一页</a>
        <a href="javaScript:void(0);" onclick="agentOfflineUserRecordTwo(${page.totalPages});"  class="last-page">尾页</a>
	</div>
</div>



