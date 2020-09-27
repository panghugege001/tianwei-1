<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<table class="table">
	<tr>
		<th>编号</th>
		<th>金额</th>
		<th>状态</th>
		<th>发放时间</th>
		<th>备注</th>
		<th>操作</th>
	</tr>
	
	<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
		<tr>
			<td>
				<s:property value="#fc.pno" />
			</td>
			<td>
				<s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.promo)" />
			</td>
			<td>
				<s:if test="#fc.status==1 || #fc.status==2">已领取</s:if>
				<s:elseif test="#fc.status==3">已取消</s:elseif>
				<s:elseif test="#fc.status==0">未领取</s:elseif>
			</td>
			<td>
				<s:property value="#fc.tempCreateTime" />
			</td>
			<td>
				<s:property value="#fc.remark" />
			</td>
			<td>
				<s:if test="#fc.status==0">
					<a href="javascript:getLosePromo('<s:property value="#fc.pno"/>')">领取</a>
					<!-- <a href="javascript:cancelLosePromo('<s:property value="#fc.pno"/>')">取消</a> -->
				</s:if>
			</td>
		</tr>
	</s:iterator>
</table>

<div class="pagination" style="text-align: center;">
    共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages} &nbsp;&nbsp;
  <!--<a href="javaScript:void(0);" onclick="turnPage('P',${pageIndex-1});" class="pageA">上一页</a>&nbsp;&nbsp;
  <a href="javaScript:void(0);" onclick="turnPage('N',${pageIndex+1},${page.totalPages});" class="pageA">下一页</a>&nbsp;&nbsp; 
  <a href="javaScript:void(0);" onclick="turnPage('L',${page.totalPages});" class="pageA">尾页</a>-->
  <s:if test="#request.pageIndex>1">
  	 <a href="javaScript:void(0);" onclick="turnPage(${pageIndex-1});" class="pageA">上一页</a>&nbsp;&nbsp;
  </s:if>
  <s:else>
  	 上一页&nbsp;&nbsp;
  </s:else>
  <s:if test="#request.pageIndex<#request.page.totalPages">
  	 <a href="javaScript:void(0);" onclick="turnPage(${pageIndex+1});" class="pageA">下一页</a>&nbsp;&nbsp; 
  </s:if>
  <s:else>
  	 下一页&nbsp;&nbsp;
  </s:else>
  <s:if test="(#request.pageIndex).equals(#request.page.totalPages)">
  	 尾页
  </s:if>
  <s:else>
  	 <a href="javaScript:void(0);" onclick="turnPage(${page.totalPages});" class="pageA">尾页</a>
  </s:else>
</div>