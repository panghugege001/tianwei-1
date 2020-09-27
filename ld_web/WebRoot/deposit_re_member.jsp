<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
    <base href="<%=request.getRequestURL()%>" />
    <jsp:include page="/tpl/checkUser.jsp"></jsp:include>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <style type="text/css">
	.data-list { padding-top:10px; }
	.table { width: 98%; margin: 0 10px 10px; border: 1px solid #bababa; }
	.table tr th { color:#fff; }
	.table tr:nth-child(even) { background-color:#f0f0f0; }
	.table td { background:none; border:none; color: #342923; }
	</style>
    </head>

    <body>
<div class="data-list">
      <form action="${depositRecordsUrl}" method="post" name="mainform">
    <table class="table data-table">
          <thead>
        <tr>
              <th>编号</th>
              <th>编号</th>
              <th>存款金额</th>
              <th>存款时间</th>
            </tr>
      </thead>
          <tbody>
        <s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
              <tr>
            <td><s:property value="#st.index+1"/></td>
            <td><s:property value="#fc.billno"/></td>
            <td class="c-red"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.money)"/></td>
            <td class="type"><s:property value="#fc.tempCreateTime"/></td>
          </tr>
            </s:iterator>
      </tbody>
        </table>
    <div class="pagination" >
          <input type="hidden" name="pageIndex" value="1" id="pageIndex"/>
          <input type="hidden" name="size" value="10" />
          ${page.jsPageCode} </div>
  </form>
    </div>
<script type="text/javascript">
	function gopage(val) {
		document.mainform.pageIndex.value = val;
		document.mainform.submit();
	}
</script>
</body>
</html>