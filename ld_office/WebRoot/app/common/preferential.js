$(function() {

	var platform = $("#platformId");
	platform.empty();

	for ( var i in preferential_platform) {

		platform.append("<option value='" + preferential_platform[i].value + "'>" + preferential_platform[i].text + "</option>");
	}

	var times = $("#timesFlag");
	times.empty();

	for (var k = 0, len = preferential_times_type.length; k < len; k++) {

		times.append("<option value='" + preferential_times_type[k].value + "'>" + preferential_times_type[k].text + "</option>");
	}

	var vip = $("#vipTD");
	vip.empty();

	var str1 = "<label><input type='checkbox' onclick='checkAll(this)' />全选</label>";

	for (var n = 0, len = preferential_grade.length; n < len; n++) {

		str1 += "<label><input type='checkbox' name='viplevel' value='" + preferential_grade[n].value + "'/>" + preferential_grade[n].text + "</label>";
	}

	vip.html(str1);

	var passage = $("#passageTD");
	passage.empty();

	var str2 = "<label><input type='checkbox' onclick='checkAllPassage(this)' />全选</label>";

	for (var m = 0, len = preferential_passage.length; m < len; m++) {

		str2 += "<label><input type='checkbox' name='passage' value='" + preferential_passage[m].value + "'/>" + preferential_passage[m].text + "</label>";
	}

	passage.html(str2);

//	$("#limit").hide();

	platform.change(platformChange);
	$("#titleId").change(titleChange);
	$("#submitBtn").click(submitPreferential);

	var param = location.search.substring(1);

	if (!isNull(param)) {

		var id = param.split("=")[1];

		$.ajax({
			url: "/app/queryPreferentialConfig.do?preferentialConfig.id=" + id + "&r=" + Math.random(),
			data: null,
			async: false,
			success: function(msg) {

				var data = JSON.parse(msg);

				if (!isNull(data.message)) {

					alert(data.message);
				} else {

					$("#id").val(data.id);
					$("#platformId").val(data.platformId);
					$("#platformName").val(data.platformName);
					$("#titleId").val(data.titleId);
					$("#titleName").val(data.titleName);
					$("#aliasTitle").val(data.aliasTitle);
					$("#percent").val(data.percent);
					$("#betMultiples").val(data.betMultiples);
					$("#limitMoney").val(data.limitMoney);
					$("#depositAmount").val(data.depositAmount == 0 ? "" : data.depositAmount);
					$("#depositStartTime").val(data.depositStartTimeStr);
					$("#depositEndTime").val(data.depositEndTimeStr);
					$("#betAmount").val(data.betAmount == 0 ? "" : data.betAmount);
					$("#betStartTime").val(data.betStartTimeStr);
					$("#betEndTime").val(data.betEndTimeStr);
					$("#startTime").val(data.startTimeStr);
					$("#endTime").val(data.endTimeStr);
					$("#times").val(data.times);
					$("#timesFlag").val(data.timesFlag);
					$("#machineCodeTimes").val(data.machineCodeTimes == 0 ? "" : data.machineCodeTimes);
					$("#lowestAmount").val(data.lowestAmount == 0 ? "" : data.lowestAmount);
                    $("#highestAmount").val(data.highestAmount == 0 ? "" : data.highestAmount);
                    $("#groupId").val(data.groupId);
					$("#mutexTimes").val(data.mutexTimes == 0 ? "" : data.mutexTimes);
					$("#vip").val(data.vip);
					$("#isPhone").val(data.isPhone);
					$("#isPassSms").val(data.isPassSms);
					$("#machineCodeEnabled").val(data.machineCodeEnabled);

					platformChange(data.titleId);

					titleChange();

					if (!isNull(data.vip)) {

						var s = (data.vip).split(",");
						var len = s.length;

						$("input[name='viplevel']").each(function() {

							var str = $(this).val();

							for (var i = 0; i < len; i++) {

								var temp = s[i];

								if (temp == str) {

									$(this).attr("checked", "true");
								}
							}
						});
					}

					if (!isNull(data.isPhone)) {

						var value = data.isPhone;
						var arr = value.split(",");

						$("input[name='passage']").each(function() {

							var str = $(this).val();

							for (var i = 0, len = arr.length; i < len; i++) {

								if (arr[i] == str) {

									$(this).attr("checked", "true");
								}
							}
						});
					}

					$("input[name='sms']").each(function() {

						var str = $(this).val();

						if ((data.isPassSms) == str) {

							$(this).attr("checked", "true");
						}
					});

					$("input[name='machineEnabled']").each(function() {

						var str = $(this).val();

						if ((data.machineCodeEnabled) == str) {

							$(this).attr("checked", "true");
						}
					});
				}
			}
		});
	} else {

		platformChange();
	}
});

function platformChange(titleId) {

	var value = $("#platformId").val();

	var title = $("#titleId");
	title.empty();

	var type = preferential_platform[value].type;

	for (var j = 0, len = type.length; j < len; j++) {

		title.append("<option value='" + type[j].value + "'>" + type[j].text + "</option>");
	}

	if (!isNull(titleId)) {

		title.val(titleId);
	}

	titleChange();
};

function titleChange() {

	/*var text = $("#titleId option:selected").text();

	if (text.indexOf('限时优惠') != -1) {

		$("#limit").show();
	} else {

		$("#limit").hide();
	}*/
};

function checkAll(self) {

	$("[name='viplevel']:checkbox").attr("checked", $(self).is(':checked'));
};

function checkAllPassage(self) {

	$("[name='passage']:checkbox").attr("checked", $(self).is(':checked'));
};

function submitPreferential() {

	var self = this;

	var platformName = $("#platformId").find("option:selected").text();
	$("#platformName").val(platformName);

	var titleName = $("#titleId").find("option:selected").text();
	$("#titleName").val(titleName);

	var arr = [];

	$("input[name='viplevel']:checked").each(function() {

		arr.push($(this).val());
	});

	$("#vip").val(arr.join(","));

	var arrPassage = [];

	$("input[name='passage']:checked").each(function() {

		arrPassage.push($(this).val());
	});

	$("#isPhone").val(arrPassage.join(","));

	var smsChecked = $("input[name='sms']:checked").val();
	$("#isPassSms").val(smsChecked);

	var machineEnabledChecked = $("input[name='machineEnabled']:checked").val();
	$("#machineCodeEnabled").val(machineEnabledChecked);

	var flag = window.confirm("确定创建该记录吗？");

	if (flag) {

		if (isNull($("#aliasTitle").val())) {

			alert("优惠标题不能为空！");
			return;
		}

		if (isNull($("#percent").val())) {

			alert("存送百分比不能为空！");
			return;
		}

		if (isNaN($("#percent").val())) {

			alert("存送百分比只能为有效数字！");
			return;
		}

		if (isNull($("#betMultiples").val())) {

			alert("流水倍数不能为空！");
			return;
		}

		if (!isInteger($("#betMultiples").val())) {

			alert("流水倍数只能为有效整数！");
			return;
		}

		if (isNull($("#limitMoney").val())) {

			alert("最大额度不能为空！");
			return;
		}

		if (isNaN($("#limitMoney").val())) {

			alert("最大额度只能为有效数字！");
			return;
		}

		var beginDate = $("#startTime").val();
		var endDate = $("#endTime").val();

		if (isNull(beginDate)) {

			alert("启用开始时间不能为空！");
			return;
		}

		if (isNull(endDate)) {

			alert("启用结束时间不能为空！");
			return;
		}

		var d1 = new Date(beginDate.replace(/\-/g, "\/"));
		var d2 = new Date(endDate.replace(/\-/g, "\/"));

		if (d1 >= d2) {

			alert("启用开始时间不能大于启用结束时间！");
			return;
		}

		if (isNull($("#times").val())) {

			alert("次数不能为空！");
			return;
		}

		if (!isInteger($("#times").val())) {

			alert("次数只能为有效整数！");
			return;
		}

		if (arr.length == 0) {

			alert("请选择等级！");
			return;
		}

		if (arrPassage.length == 0) {

			alert("请选择申请通道！");
			return;
		}

		if (isNull(smsChecked)) {

			alert("请选择是否让未通过短信反向验证的玩家使用！");
			return;
		}

		if (isNull(machineEnabledChecked)) {

			alert("请选择是否启用机器码验证！");
			return;
		}

		if (machineEnabledChecked == 1) {

			var machineCodeTimes = $("#machineCodeTimes").val();

			if (isNull(machineCodeTimes)) {

				alert("请输入机器码使用次数！");
				return;
			}

			if (!isInteger(machineCodeTimes)) {

				alert("机器码使用次数只能为有效整数！");
				return;
			}
		}

		if (!isNull($("#depositAmount").val())) {

			var date1 = $("#depositStartTime").val();
			var date2 = $("#depositEndTime").val();

			if (isNull(date1)) {

				alert("开始时间（存款）不能为空！");
				return;
			}

			if (isNull(date2)) {

				alert("结束时间（存款）不能为空！");
				return;
			}

			var t1 = new Date(date1.replace(/\-/g, "\/"));
			var t2 = new Date(date2.replace(/\-/g, "\/"));

			if (t1 >= t2) {

				alert("存款开始时间不能大于存款结束时间！");
				return;
			}
		}

		if (!isNull($("#betAmount").val())) {

			var date3 = $("#betStartTime").val();
			var date4 = $("#betEndTime").val();

			if (isNull(date3)) {

				alert("开始时间（输赢）不能为空！");
				return;
			}

			if (isNull(date4)) {

				alert("结束时间（输赢）不能为空！");
				return;
			}

			var t3 = new Date(date3.replace(/\-/g, "\/"));
			var t4 = new Date(date4.replace(/\-/g, "\/"));

			if (t3 >= t4) {

				alert("输赢开始时间不能大于输赢结束时间！");
				return;
			}
		}

		if (!isNull($("#mutexTimes").val())) {

			if (!isInteger($("#mutexTimes").val())) {

				alert("互斥组别申请次数只能为有效整数！");
				return;
			}
		}
		
		$(self).attr("disabled", "disabled");

		$('#mainform').ajaxSubmit(function(data) {

			$(self).removeAttr("disabled");

			alert(data);

			if (data.indexOf('成功') != -1) {

				var _parentWin = window.opener;
				_parentWin.mainform.submit();
				window.close();
			}
		});
	}
};