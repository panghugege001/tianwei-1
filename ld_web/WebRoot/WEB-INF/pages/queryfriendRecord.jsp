<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=request.getRequestURL()%>" />
<title>龙都娱乐城--明细查询</title>
<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
       <style type="text/css">
		.data-list { padding-top:10px; }
		.table { width: 98%; margin: 0 10px 10px; border: 1px solid #bababa; }
		.table tr th { color:#fff; }
		.table tr:nth-child(even) { background-color:#f0f0f0; }
		.table td { background:none; border:none; color: #342923; }
		.time-box{margin:30px 0 30px 20px; color: #342923;}
		.title-h2 {width: 150px;height: 50px;font-size: 18px;text-align: center;color: #fff;font-weight: bold;line-height: 50px;margin-bottom: 20px;background-color: #3e4883;/* border-radius: 20px; */margin-left: 40px;margin-top: 20px;}
		</style>
</head>

<body>
	<div class="data-list">
		<form action="${ctx}/asp/queryfriendRecord.aspx" method="post" data-dataform
			name="mainform">
<!-- 			<div class="time-box">
起始时间：
<s:select cssStyle="border:1px solid #ddd; height:27px;"
	list="#{0:'新增注册成功玩家'}" value="%{friendtype}"
	onchange="queryfriendRecord();" name='friendtype'
	id="friendtype"></s:select>
		</div> -->
			<!-- <h2 class="title-h2">新增推荐好友</h2> -->
			<table class="table data-table">
				<thead>
					<tr>
						<th>序号</th>
						<th>玩家账号</th>
						<!-- <th>金额</th> -->
						<th>时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator var="fc" value="%{#request.page.pageContents}"
						status="st">
						<tr>
							<td><s:property value="#st.index+1" /></td>
							<td><s:property value="#fc.downlineuser" /></td>
							<!-- <td><s:property value="#fc.money" /></td> -->
							<td><s:property value="#fc.createtime" /></td>
							<td><s:property value="#fc.type" /></td>
						</tr>
					</s:iterator>
				</tbody>

			</table>
			<div class="pagination">
				<input type="hidden" name="pageIndex" value="1" id="pageIndex" /> <input
					type="hidden" name="size" value="10" /> ${page.jsPageCode}
			</div>
		</form>
	</div>
	<script type="text/javascript">
		function gopage(val) {
			document.mainform.pageIndex.value = val;
			document.mainform.submit();
		}
		function queryfriendRecord(){
			//document.mainform.pageIndex.value = val;
			document.mainform.submit();
		}
	</script>
</body>
</html>

