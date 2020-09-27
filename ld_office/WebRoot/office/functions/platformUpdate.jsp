<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!-- <script type="text/javascript" src="/js/prototype_1.6.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>


<script type="text/javascript">
function update(){
	if(confirm("确定？")){
		var data = $("#mainform").serialize();
		var action = "/office/updatePlatformData.do";
		$.ajax({
			  url:action,
			  type:"POST",
			  data:data,
			  contentType:"application/x-www-form-urlencoded; charset=utf-8",
			  success: function(resp){
				  alert(resp);
			  }
			});
	}
}
</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple" >
	<input type="hidden" name="uuid" value="${platformDataxx.uuid}">
	<table >
		<tr><td>玩家账号</td><td><input type="text" readonly="readonly"  value="${platformDataxx.loginname}" /></td></tr>
		<tr><td>开始时间</td><td>${platformDataxx.starttime}</td></tr>
		<tr><td>结束时间</td><td>${platformDataxx.endtime}</td></tr>
		<tr><td>投注额</td><td><input type="text" name="bet" value="${platformDataxx.bet}"> </td></tr>
		<tr><td>输赢</td><td><input type="text" name="profit" value="${platformDataxx.profit}"></td></tr>
		<tr><td><input type="button" value="更新" onclick="update();"></td></tr>
	</table>
	</s:form>
</div>