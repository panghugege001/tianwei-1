<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!-- <script type="text/javascript" src="/js/prototype_1.6.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>


<script type="text/javascript">
$(document).ready(function(){
	queryDictionary();
});

function queryDictionary(){
	$.ajax({
		url: "${ctx}/office/queryDictionary.do",
		type: "post",
		async : true,
		data:"type=arbitrage_type",
		success: function (data) {
			var projectCheckbox = $("#projectCheckbox");
			projectCheckbox.empty();
			var arbitrage_project = "${arbitrageData.arbitrage_project}";
			for(var i=0; i<data.length; i++){
				if(arbitrage_project.indexOf(data[i].dictName) >= 0){
					projectCheckbox.append("<input type='checkbox' name='arbitrageProject' value='"+data[i].dictName+"' title='"+data[i].dictValue+"' checked='checked'>"+data[i].dictValue);
				}else{
					projectCheckbox.append("<input type='checkbox' name='arbitrageProject' value='"+data[i].dictName+"' title='"+data[i].dictValue+"'>"+data[i].dictValue);
				}
			}
		},
		error: function () {
			alert("操作超时导致结果未知，请联系技术查看情况....");
		}
	});
}

function chooseProject(val){
	if(val==1){
		$("#arbitrageTypeSelect").show();
		$("#arbitrage_project_show").val("");
		$("#arbitrage_project").val("");
	}else{
		$("#arbitrageTypeSelect").hide();
        var obj = document.getElementsByName("arbitrageProject");
        var check_val = [];
        var check_title = [];
        for(k in obj){
            if(obj[k].checked){
                check_val.push(obj[k].value);
            	check_title.push(obj[k].title);
            }
        }
        $("#arbitrage_project_show").val(check_title);
        $("#arbitrage_project").val(check_val);
	}
}
function updateArbitrageData(){
	var str="";
	$("input[name='arbitrageProject']:checked").each(function(){   
	     str+=$(this).val();   
	 });
	if(str == ""){
		alert("请选择套利方式！");
		return;
	}
	$("#arbitrage_project").val(str);
	var data = $("#mainform").serialize();
	var action = "/office/updateArbitrageData.do";
	$.ajax({
		  url:action,
		  type:"POST",
		  data:data,
		  contentType:"application/x-www-form-urlencoded; charset=utf-8",
		  success: function(resp){
			  alert(resp);
		  },
		  complete:responseMethod
	});
}
function onCancel(){
	window.close();
}
function responseMethod(data){
	var _parentWin = window.opener;
	 _parentWin.mainform.submit();
	window.close();
}
</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		<input name="arbitrageData.id" value="${arbitrageData.id}" type="hidden"/>
		<table>
			<tr>
				<td>账号：</td>
				<td>${arbitrageData.loginname}</td>
			</tr>
			<tr>
				<td>姓名：</td>
				<td>${arbitrageData.account_name}</td>
			</tr>
			<tr>
				<td>手机：</td>
				<td>${arbitrageData.phone}</td>
			</tr>
			<tr>
				<td>邮箱：</td>
				<td>${arbitrageData.email}</td>
			</tr>
			<tr>
				<td>套利项目：</td>
				<td>
					<div id="projectCheckbox"></div>
					<input type="hidden" id="arbitrage_project" name="arbitrageData.arbitrage_project"/>
				</td>
			</tr>
			<tr >
				<td align="center" colspan="2">
					<input type="button" value="提交" onclick="updateArbitrageData();" />
					<input type="button" value="取消" onclick="onCancel();" />
				</td>
			</tr>

		</table>
	</s:form>
</div>