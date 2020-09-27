<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<div class="manage_page">
	<span class="mp_left">显示：<em>${page.size }条/页</em></span>	
	 	<input type="hidden" name="pageIndex" value="1" id="pageIndex"/>
		<input type="hidden" name="size" value="10" />
		<span class="mp_right">
			<span>共<em>${page.totalRecords }条</em> 第<em>${page.pageNumber }</em>/${page.totalPages }页</span>	
			<s:if test="#request.page.pageNumber > 1">
				<a href="javascript:gopage(1)">首页</a>
			</s:if>
			<s:else>
				<a>首页</a>
			</s:else>
			<s:if test="#request.page.pageNumber > 1">
				<a href="javascript:gopage(${page.pageNumber-1})">上页</a>
			</s:if>
			<s:else>
				<a>上页</a>
			</s:else>
			<s:if test="#request.page.pageNumber < #request.page.totalPages">
				<a href="javascript:gopage(${page.pageNumber+1})">下页</a>
			</s:if>
			<s:else>
				<a>下页</a>
			</s:else>
			
			<s:if test="#request.page.pageNumber < #request.page.totalPages">
				<a href="javascript:gopage(${page.totalPages})">尾页</a>
			</s:if> 
			<s:else>
				<a>尾页</a>
			</s:else>
			
			<span>到第</span>
			<input type="text" class="num" name="page" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')" value="${page.pageNumber }"/>
			<span>页</span>
			<input type="submit" class="go" value="GO" />
		</span> 
</div> 
<script>
	function gopage(page) {
		$("#pageIndex").val(page);
		document.forms[0].submit();
	}
</script>