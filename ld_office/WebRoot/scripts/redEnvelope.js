$(function() {


    var platform = $("#platformId");
    platform.empty();

    for (var i = 0, len = turn_platform.length; i < len; i++) {

        platform.append("<option value='" + turn_platform[i].value + "'>" + turn_platform[i].text + "</option>");
    }

    var vip = $("#vipTD");
    vip.empty();

    var str1 = "<label><input type='checkbox' onclick='checkAll(this)' />全选</label>";

    for (var n = 0, len = preferential_grade.length; n < len; n++) {

        str1 += "<label><input type='checkbox' name='viplevel' value='" + preferential_grade[n].value + "'/>" + preferential_grade[n].text + "</label>";
    }

    vip.html(str1);

    var platformName = $("#entranceTD");
    platformName.empty();

    var str2 = "<label><input type='checkbox' onclick='checkAll2(this)' />全选</label>";

    for (var n = 0, len = preferential_entrance.length; n < len; n++) {

        str2 += "<label><input type='checkbox' name='entrance' value='" + preferential_entrance[n].value + "'/>" + preferential_entrance[n].text + "</label>";
    }

    platformName.html(str2);




    $("#submitBtn").click(submitRedEnvelope);

    var param = location.search.substring(1);

    if (!isNull(param)) {

        var id = param.split("=")[1];

        $.ajax({
            url: "/office/queryRedEnvelopeActivity.do?redEnvelopeActivity.id=" + id + "&r=" + Math.random(),
            data: null,
            async: false,
            success: function(msg) {

                var data = JSON.parse(msg);

                if (!isNull(data.message)) {

                    alert(data.message);
                } else {

                    $("#id").val(data.id);
                    $("#title").val(data.title);
                    $("#minBonus").val(data.minBonus);
                    $("#maxBonus").val(data.maxBonus);
                    $("#times").val(data.times);
                    $("#platformId").val(data.platformId);
                    $("#platformName").val(data.platformName);
                    $("#multiples").val(data.multiples);
                    $("#depositAmount").val(data.depositAmount == 0 ? "" : data.depositAmount);
                    $("#depositStartTime").val(data.depositStartTimeStr);
                    $("#depositEndTime").val(data.depositEndTimeStr);
                    $("#betAmount").val(data.betAmount == 0 ? "" : data.betAmount);
                    $("#betStartTime").val(data.betStartTimeStr);
                    $("#betEndTime").val(data.betEndTimeStr);
                    $("#startTime").val(data.startTimeStr);
                    $("#endTime").val(data.endTimeStr);
                    $("#vip").val(data.vip);

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

                    if (!isNull(data.platformName)) {

                        var s = (data.platformName).split(",");
                        var len = s.length;

                        $("input[name='entrance']").each(function() {

                            var str = $(this).val();

                            for (var i = 0; i < len; i++) {

                                var temp = s[i];

                                if (temp == str) {

                                    $(this).attr("checked", "true");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
});

function checkAll(self) {

    $("[name='viplevel']:checkbox").attr("checked", $(self).is(':checked'));
};
function checkAll2(self) {

    $("[name='entrance']:checkbox").attr("checked", $(self).is(':checked'));
};
function submitRedEnvelope() {

    var self = this;

    var platformName = $("#platformId").find("option:selected").text();
    $("#platformName").val(platformName);

    var arr = [];

    $("input[name='viplevel']:checked").each(function() {

        arr.push($(this).val());
    });

    $("#vip").val(arr.join(","));

    var arr2 = [];
    $("input[name='entrance']:checked").each(function() {

        arr2.push($(this).val());
    });

    $("#platformName").val(arr2.join(","));

    var flag = window.confirm("确定提交该记录吗？");

    if (flag) {

        if (isNull($("#title").val())) {

            alert("活动标题不能为空！");
            return;
        }

        if (isNull($("#minBonus").val())) {

            alert("活动最小红利不能为空！");
            return;
        }

        if (isNull($("#maxBonus").val())) {

            alert("活动最大红利不能为空！");
            return;
        }

        if (isNull($("#times").val())) {

            alert("领取次数不能为空！");
            return;
        }

        if (!isInteger($("#times").val())) {

            alert("领取次数只能为有效整数！");
            return;
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

            if (t1 > t2) {

                alert("存款开始时间不能大于存款结束时间！");
                return;
            }
        }


        var beginDate = $("#startTime").val();
        var endDate = $("#endTime").val();

        if (isNull(beginDate)) {

            alert("活动开始时间不能为空！");
            return;
        }

        if (isNull(endDate)) {

            alert("活动结束时间不能为空！");
            return;
        }

        var d1 = new Date(beginDate.replace(/\-/g, "\/"));
        var d2 = new Date(endDate.replace(/\-/g, "\/"));

        if (d1 > d2) {

            alert("活动开始时间不能大于活动结束时间！");
            return;
        }

        if (arr.length == 0) {

            alert("请选择等级！");
            return;
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