<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>红包雨活动配置</title>
		<link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css" />
	</head>
	<body>
	<p>操作&nbsp;--&gt;&nbsp;红包雨活动配置&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
	<div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
		<s:form action="queryRedEnvelopeActivityList" namespace="/office" name="mainform" id="mainform" theme="simple">
			<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr align="left">
								<td>活动标题：</td>
								<td><s:textfield name="redEnvelopeActivity.title" id="title"></s:textfield></td>
								<td>转入平台：</td>
								<td><s:select list="#{}" name="redEnvelopeActivity.platformId" id="platformId" listKey="key" listValue="value" cssStyle="width: 130px;"></s:select></td>
								<td>每页记录：</td>
								<td><s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select></td>
								<td><s:submit value="查询"></s:submit></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<s:set name="by" value="'createTime'" />
			<s:set name="order" value="'desc'" />
			<s:hidden name="order" value="%{order}" />
			<s:hidden name="by" value="%{by}" />
			<s:hidden name="pageIndex" />
		</s:form>
	</div>
	<div style="position: absolute; top: 87px; left: 0px">
		<a href="/office/functions/addRedEnvelopeActivity.jsp" target="_blank" style="color: red; font-size: 14px; margin-left: 10px;">新增红包雨配置</a>
	</div>


	</div>
	<br />
	<div style="position: absolute; top: 115px; left: 0px">
		<div id="right">
			<div id="right_01">
				<div id="right_001">
					<div id="right_02">
						<div id="right_03"></div>
					</div>
					<div id="right_04">
						批量修改类型：
						<select id="updateType" style="height: 24px; width: 115px;">
							<option value="1">存款开始时间</option>
							<option value="2">存款结束时间</option>
							<option value="3">投注开始时间</option>
							<option value="4">投注结束时间</option>
							<option value="5">活动开始时间</option>
							<option value="6">活动结束时间</option>
						</select>
						批量修改时间：
						<s:textfield id="updateTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" theme="simple" />
						<input type="button" value="批量修改" id="update" />
						<input type="button" value="一键清零(慎用)" id="deleteAll" /><br/>
						<input type="button" value="批量删除" id="delete" />&nbsp;&nbsp;&nbsp;
						当前主账户转红包雨账户限额:<input type="text" style="width: 40px" id="limitvalue"/><input type="button" value="修改" id="editLimitRedRain" onclick="editLimitRedRain()"/>
						<table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
							<tr bgcolor="#0084ff">
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;"><input type="checkbox" id="checkAllBox" /></td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">红包ID</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">活动标题</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">活动最小红利</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">活动最大红利</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">领取次数</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">转入平台</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">流水倍数</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">存款额</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">存款开始时间</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">存款结束时间</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">红利总额</td>
								<%--<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">投注额</td>
                                <td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">投注开始时间</td>
                                <td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">投注结束时间</td>--%>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">活动开始时间</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">活动结束时间</td>
								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">平台通道</td>

								<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer;">等级</td>
							</tr>
							<s:iterator var="fc" value="%{#request.page.pageContents}">
								<tr>
									<td align="center"><input type="checkbox" name="item" value="<s:property value="#fc.id"/>" /></td>
									<td align="left"><s:property value="#fc.id" /></td>
									<td align="center"><a href="/office/functions/updateRedEnvelopeActivity.jsp?id=<s:property value='#fc.id' />" target="_blank"><s:property value="#fc.title" /></a></td>
									<td align="left"><s:property value="#fc.minBonus" /></td>
									<td align="left"><s:property value="#fc.maxBonus" /></td>
									<td align="left"><s:property value="#fc.times" /></td>
									<td align="left">红包雨账户</td>
									<td align="left"><s:property value="#fc.multiples" /></td>
									<td align="left"><s:property value="#fc.depositAmount" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.depositStartTime" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.depositEndTime" /></td>
									<td align="left"><s:property value="#fc.betAmount" /></td>
										<%--<td align="left"><s:property value="#fc.betAmount" /></td>
                                        <td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.betStartTime" /></td>
                                        <td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.betEndTime" /></td>--%>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startTime" /></td>
									<td align="center"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endTime" /></td>
									<td align="left"><s:property value="#fc.platformName" /></td>
									<td align="left"><s:property value="@dfh.model.enums.VipLevel@getTextStr(#fc.vip)" /></td>
								</tr>
							</s:iterator>
							<tr>
								<td colspan="16" align="right" bgcolor="66b5ff">${page.jsPageCode}</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:import url="${ctx}/office/script.jsp" />
	<script type="text/javascript" src="${ctx}/scripts/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/app/common/data.js"></script>
	<script type="text/javascript" src="${ctx}/app/common/function.js"></script>
	<script type="text/javascript" src="${ctx}/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">

        $(document).ready(function() {
            var action = "/office/queryLimitRedRain.do";
            var xmlhttp = new Ajax.Request(action, { method: 'get', onComplete: function(result) {

                var text = result.responseText;
                if(text=="ERRO"){
                    alert("查询额度配置失败");
                    return false;
                }
                $('#limitvalue').val("").val(text);





            }
            });
            var platform = $("#platformId");
            platform.empty();
            platform.append("<option value=''></option>");

            for (var i = 0, len = turn_platform.length; i < len; i++) {

                platform.append("<option value='" + turn_platform[i].value + "'>" + turn_platform[i].text + "</option>");
            }

            platform.val("${redEnvelopeActivity.platformId}");


            $("#checkAllBox").bind("click", function() {

                $("[name='item']:checkbox").attr("checked", $(this).is(':checked'));
            });



            $("#delete").bind("click", function() {

                remove();
            });
            $("#deleteAll").bind("click", function() {

                removeAll();
            });

            $("#update").bind('click', function() {

                batchUpdate();
            });
        });

        function gopage(val) {

            document.mainform.pageIndex.value = val;
            document.mainform.submit();
        };
        function editLimitRedRain() {
            var value=$('#limitvalue').val();


            var action = "/office/editLimitRedRain.do?value="+value;


            var xmlhttp = new Ajax.Request(action, { method: 'post', onComplete: function(result) {

                var text = result.responseText;
                alert(text);




            }
            });


        };


        function orderby(by) {

            if (document.mainform.order.value == "desc") {

                document.mainform.order.value = "asc";
            } else {

                document.mainform.order.value = "desc";
            }

            document.mainform.by.value = by;
            document.mainform.submit();
        };

        function remove() {

            if (window.confirm('确认要删除所选中的数据吗？')) {

                var arr = [];

                $("input[name='item']:checked").each(function() {

                    arr.push($(this).val());
                });

                if (arr.length == 0) {

                    alert("未选中任何数据，请选择需要删除的数据！");
                    return;
                }

                var ids = arr.join(',');

                var action = "/office/deleteRedEnvelopeActivity.do";

                var data = "ids=" + ids;

                var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {

                    var text = result.responseText;

                    alert(text);

                    if (text.indexOf('成功') != -1) {

                        document.mainform.submit();
                    }
                }
                });
            }
        };


        function removeAll() {

            if (window.confirm('确认要清空所有玩家金额数据吗？')) {



                var action = "/office/deleteRedRainDataAll.do";


                var xmlhttp = new Ajax.Request(action, { method: 'post', onComplete: function(result) {

                    var text = result.responseText;

                    alert(text);


                }
                });
            }
        };



        function batchUpdate() {

            if (window.confirm('确认要修改所选中的数据吗？')) {

                var arr = [];

                $("input[name='item']:checked").each(function() {

                    arr.push($(this).val());
                });

                if (arr.length == 0) {

                    alert("未选中任何数据，请选择需要修改的数据！");
                    return;
                }

                var ids = arr.join(',');

                var updateType = $("#updateType").val();
                var updateTime = $('#updateTime').val();

                if (isNull(updateType)) {

                    alert('请选择需要修改的类型！');
                    return;
                }

                if (isNull(updateTime)) {

                    alert('请选择需要修改的时间！');
                    return;
                }

                var action = "/office/batchUpdateRedEnvelopeActivity.do";

                var data = "ids=" + ids + "&type=" + updateType + "&executeTime=" + updateTime;

                var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {

                    var text = result.responseText;

                    alert(text);

                    if (text.indexOf('成功') != -1) {

                        document.mainform.submit();
                    }
                }
                });
            }
        };
	</script>
	</body>
</html>