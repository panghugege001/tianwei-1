$(function() {
	
	
	validate();
	
	var vip = $("#vipTD");
	vip.empty();

	var str1 = "<label><input type='checkbox' onclick='checkAll(this)' />全选</label>";

	for (var n = 0, len = preferential_grade.length; n < len; n++) {

		str1 += "<label><input type='checkbox' name='viplevel' value='" + preferential_grade[n].value + "'/>" + preferential_grade[n].text + "</label>";
	}

	vip.html(str1);

	$("#startTime").val($("#starttimeTmp").val());
	$("#endTime").val($("#endtimeTmp").val());
	
	$("input[name='machineEnabled']").each(function() {
		
		var str = $(this).val();
		
		if (($("#machineCodeEnabled").val()) == str) {
			
			$(this).attr("checked", "true");
		}
	});
	var vipTmp = $("#vipTmp").val();
	if(!isNull(vipTmp)){
		$("input[name='viplevel']").each(function() {
			var str = $(this).val();
			var vipAry = vipTmp.split(",");
			for(var i=0;i<vipAry.length;i++)
			{
				if (vipAry[i] == str) {
					$(this).attr("checked", "true");
				}
			}
			
		});
	}
});

function update(){
	var arr = [];
	
	$("input[name='viplevel']:checked").each(function() {

		arr.push($(this).val());
	});
	
	$("#vip").val(arr.join(","));
	
	var arrPassage = [];
	$("input[name='isPhoneData']:checked").each(function() {

		arrPassage.push($(this).val());
	});
	
	$("#isPhone").val(arrPassage.join(","));
	
	var date1 = $("#startTime").val();
	var date2 = $("#endTime").val();
	
	var machineEnabledChecked = $("input[name='machineEnabled']:checked").val();
	$("#machineCodeEnabled").val(machineEnabledChecked);
	
	//验证数据有效性
	var aliasTitle = $("#aliasTitle").val();
	var amount = $("#amount").val();
	var times = $("#times").val();
	var maxMoney = $("#maxMoney").val();
	var minMoney = $("#minMoney").val();
	
	if(confirm("确定？")){
		if(isNull(aliasTitle))
		{
			alert("标题不能为空！");
			return ;
		}
		if (isNull(amount)) {

			alert("体验金额度不能为空！");
			return;
		}

//		if (!isInteger(amount)) {
//			alert("体验金额度只能为有效整数！");
//			return;
//		}
		if(isNull(maxMoney))
		{
			alert("限制最大金额不能为空！");
			return ;
		}
		if(isNull(minMoney))
		{
			alert("限制最小金额不能为空！");
			return ;
		}
		
		if (isNull(date1)) {

			alert("启用开始时间不能为空！");
			return;
		}
		if (isNull(date2)) {

			alert("启用结束时间不能为空！");
			return;
		}
		
		var t1 = new Date(date1.replace(/\-/g, "\/"));
		var t2 = new Date(date2.replace(/\-/g, "\/"));
			
		if (t1 >= t2) {
			alert("启用开始时间不能大于启用结束时间！");
			return;
		}
		
		if (isNull(times)) {

			alert("次数不能为空！");
			return;
		}

		if (!isInteger(times)) {
			alert("次数只能为有效整数！");
			return;
		}
		
		if (arr.length == 0) 
		{
			alert("请选择等级！");
			return;
		}
		if (arrPassage.length == 0) 
		{
			alert("请选择申请通道！");
			return;
		}
		
		var data = $("#mainform").serialize();
		var action = "/office/updateExperienceGold.do";
		$.ajax({
		  url:action,
		  type:"POST",
		  data:data,
		  contentType:"application/x-www-form-urlencoded; charset=utf-8",
		  success: function(resp){
			  alert(resp);
			  window.location.href="/office/functions/experienceGold.jsp";
		  }
		});
	}
}



function create(){
	var arr = [];
	
	$("input[name='viplevel']:checked").each(function() {

		arr.push($(this).val());
	});
	
	$("#vip").val(arr.join(","));
	
	var arrPassage = [];
	$("input[name='isPhoneData']:checked").each(function() {

		arrPassage.push($(this).val());
	});
	
	$("#isPhone").val(arrPassage.join(","));
	
	var date1 = $("input[name='starttime']").val();
	var date2 = $("input[name='endtime']").val();
	
	var machineEnabledChecked = $("input[name='machineEnabled']:checked").val();
	$("#machineCodeEnabled").val(machineEnabledChecked);
	
	//验证数据有效性
	var aliasTitle = $("#aliasTitle").val();
	var amount = $("#amount").val();
	var times = $("#times").val();
	var maxMoney = $("#maxMoney").val();
	var minMoney = $("#minMoney").val();
	
	if(confirm("确定？")){
		if(isNull(aliasTitle))
		{
			alert("标题不能为空！");
			return ;
		}
		if (isNull(amount)) {

			alert("体验金额度不能为空！");
			return;
		}

//		if (!isInteger(amount)) {
//			alert("体验金额度只能为有效整数！");
//			return;
//		}
		if(isNull(maxMoney))
		{
			alert("限制最大金额不能为空！");
			return ;
		}
		if(isNull(minMoney))
		{
			alert("限制最小金额不能为空！");
			return ;
		}
		
		if (isNull(date1)) {

			alert("启用开始时间不能为空！");
			return;
		}
		if (isNull(date2)) {

			alert("启用结束时间不能为空！");
			return;
		}
		
		var t1 = new Date(date1.replace(/\-/g, "\/"));
		var t2 = new Date(date2.replace(/\-/g, "\/"));
			
		if (t1 >= t2) {
			alert("启用开始时间不能大于启用结束时间！");
			return;
		}
		
		if (isNull(times)) {

			alert("次数不能为空！");
			return;
		}

		if (!isInteger(times)) {
			alert("次数只能为有效整数！");
			return;
		}
		
		if (arr.length == 0) 
		{
			alert("请选择等级！");
			return;
		}
		if (arrPassage.length == 0) 
		{
			alert("请选择申请通道！");
			return;
		}
		
		var data = $("#mainform").serialize();
		var action = "/office/insertExperienceGold.do";
		var xmlhttp = new Ajax.Request(
				action,
		        {
		            method: 'post',
		            parameters:data+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);
		
	}
}

function responseMethod(data){

	alert(data.responseText);
	var _parentWin = window.opener;
	 _parentWin.mainform.submit();
	window.close();
}

function validate(value) {
	if (null == value || "" === value) {
	
		value = $("#title").val();
	}
	displayTr(value);	
};

function displayTr(value){
	var platform = $("#platformName");
	var platformTmp = $("#platformNameTmp").val();
	platform.empty();
	if(value == '自助体验金'){
		for (var k = 0, len = tyj_type.length; k < len; k++) {
			if (platformTmp == tyj_type[k].text) {
				platform.append("<option value='" + tyj_type[k].value + "' selected >"
						+ tyj_type[k].text + "</option>");
			} else {
				platform.append("<option value='" + tyj_type[k].value + "'>"
						+ tyj_type[k].text + "</option>");
			}

		}
	}else{
		for (var k = 0, len = app_type.length; k < len; k++) {
			if (platformTmp == tyj_type[k].text) {
				platform.append("<option value='" + app_type[k].value + "' selected >" + app_type[k].text + "</option>");
			} else {
				platform.append("<option value='" + app_type[k].value + "'>" + app_type[k].text + "</option>");
			}
			
		}
	}
}

function checkAllPassage(self) {
	$("[name='isPhoneData']:checkbox").attr("checked", $(self).is(':checked'));
};
function checkAll(self) {
	$("[name='viplevel']:checkbox").attr("checked", $(self).is(':checked'));
};