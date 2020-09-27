<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<s:set name="start" value="" />
<s:set name="end" value="" />
<s:set name="startTime" value="" />
<s:set name="endTime" value="" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>活动修改配置</title>
	<style type="text/css">
		table {
			border-collapse: separate;
			border-spacing: 10px;
		}
	</style>
</head>
<body>
<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple" action="/office/editSaveActivity" namespace="/office">
		<table align="center" border='0'>
			<tr>
				<td>标题</td>
				<td>
					<input type="hidden" value="${data.id}" name="activityConfig.id">
					<input type="text" name="activityConfig.title" id='title' value="${data.title}" style="width: 157px;" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>英文标题(方便前台操作,同一类型活动确保一致)</td>
				<td>
					<input type="text" name="activityConfig.englishtitle" value="${data.englishtitle}" id='englishTitle' style="width: 157px;" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>金额</td>
				<td>
					<input type="text" name="activityConfig.amount"  value="${data.amount}" style="width: 157px;" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>等级</td>
				<td>
					<label><input  type="checkbox" name="viplevel" <c:if test="${data.level==0}">checked</c:if> value="0"><s:property value="@dfh.model.enums.VipLevel@getText(0)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==1}">checked</c:if> value="1"><s:property value="@dfh.model.enums.VipLevel@getText(1)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==2}">checked</c:if> value="2"><s:property value="@dfh.model.enums.VipLevel@getText(2)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==3}">checked</c:if> value="3"><s:property value="@dfh.model.enums.VipLevel@getText(3)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==4}">checked</c:if> value="4"><s:property value="@dfh.model.enums.VipLevel@getText(4)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==5}">checked</c:if> value="5"><s:property value="@dfh.model.enums.VipLevel@getText(5)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==6}">checked</c:if> value="6"><s:property value="@dfh.model.enums.VipLevel@getText(6)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==7}">checked</c:if> value="7"><s:property value="@dfh.model.enums.VipLevel@getText(7)" /></label>
					<label><input type="checkbox" name="viplevel" <c:if test="${data.level==8}">checked</c:if> value="8"><s:property value="@dfh.model.enums.VipLevel@getText(8)" /></label>
				</td>
			</tr>

			<tr>
				<td>活动区间</td>
				<td>
					<select name="activityConfig.scope">
						<option  value="">-请选择-</option>
						<option <c:if test="${data.scope=='day'}">selected</c:if> value="day">每日</option>
						<option <c:if test="${data.scope=='week'}">selected</c:if> value="week">每周</option>
						<option <c:if test="${data.scope=='month'}">selected</c:if> value="month">每月</option>
					</select>
					<span style="color: red;">*</span>
				</td>
			</tr>

			<tr>
				<td>可领取次数</td>
				<td>
					<input type="text" name="activityConfig.times"  value="${data.times}" />
					<span style="color: red;">*</span>

				</td>
			</tr>

			<tr>
				<td>选择平台通道</td>
				<td>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.entrance, 'pc')}">checked</c:if> name="entrance" value="pc">官网</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.entrance, 'android')}">checked</c:if> name="entrance" value="android">安卓端</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.entrance, 'apple')}">checked</c:if> name="entrance" value="apple">苹果端</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.entrance, 'html5')}">checked</c:if> name="entrance" value="html5">html5</label>
				</td>
			</tr>

			<tr>
				<td id="td1">存款额要求</td>
				<td>
					<input type="text" id="deposit" name="activityConfig.deposit" value="${data.deposit}" style="width: 157px;" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td id="td2">开始时间（存款）</td>
				<td>
					<s:textfield id="depositstarttime"  name="activityConfig.depositstarttime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" cssClass="Wdate" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td id="td3">结束时间（存款）</td>
				<td>
					<s:textfield id="depositendtime" name="activityConfig.depositendtime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" cssClass="Wdate" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td >投注额要求</td>
				<td>
					<input type="text"  name="activityConfig.bet" value="${session.bet}" style="width: 157px;" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td >开始时间（投注）</td>
				<td>
					<s:textfield  id="betstarttime" name="activityConfig.betstarttime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" cssClass="Wdate" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td >结束时间（投注）</td>
				<td>
					<s:textfield id="betendtime" name="activityConfig.betendtime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" cssClass="Wdate" />
					<span style="color: red;">*</span>
				</td>
			</tr>

			<tr>
				<td >转入倍数</td>
				<td>
					<input type="text" value="${data.multiple}" name="activityConfig.multiple">
				</td>
			</tr>
			<tr>
				<td >倍数开关(注意:倍数关闭的话直接转入主账户)</td>
				<td>
					<label><input type="radio" <c:if test="${data.multiplestatus==0}">checked</c:if> name="activityConfig.multiplestatus" value="0">关闭</label>
					<label><input type="radio" <c:if test="${data.multiplestatus==1}">checked</c:if> name="activityConfig.multiplestatus" value="1">开启</label>
				</td>
			</tr>
			<tr>
				<td>转入平台</td>
				<td>
<%--
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'ALL')}">checked</c:if> name="platform" value="ALL">全部</label>
--%>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'PT')}">checked</c:if> name="platform" value="PT">PT</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'DT')}">checked</c:if> name="platform" value="DT">DT</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'MG')}">checked</c:if> name="platform" value="MG">MG</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'TTG')}">checked</c:if> name="platform" value="TTG">TTG</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'QT')}">checked</c:if> name="platform" value="QT">QT</label>
					<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'NT')}">checked</c:if> name="platform" value="NT">NT</label>
	<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'AG')}">checked</c:if> name="platform" value="AG">AG</label>
	<label><input type="checkbox" <c:if test="${fn:containsIgnoreCase (data.platform, 'EA')}">checked</c:if> name="platform" value="EA">EA</label>
		<%--<label><input type="checkbox" name="platform" value="PNG">PNG</label>
					<label><input type="checkbox" name="platform" value="EA">EA</label>
					<label><input type="checkbox" name="platform" value="AG">AG</label>
					<label><input type="checkbox" name="platform" value="SPORT">体育</label>
					<label><input type="checkbox" name="platform" value="EBET">EBET</label>--%>

				</td>
			</tr>
			<tr>
				<td>活动开始时间</td>
				<td>
					<s:textfield id="activitystarttime" name="activityConfig.activitystarttime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" cssClass="Wdate" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>活动结束时间</td>
				<td>
					<s:textfield id="activityendtime" name="activityConfig.activityendtime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="false" cssClass="Wdate" />
					<span style="color: red;">*</span>
				</td>
			</tr>
			<tr>
				<td>验证机器码</td>
				<td>
					<label><input type="radio" <c:if test="${data.machinecode==0}">checked</c:if> name="activityConfig.machinecode" value="0">关闭</label>
					<label><input type="radio" <c:if test="${data.machinecode==1}">checked</c:if> name="activityConfig.machinecode" value="1">开启</label>
				</td>
			</tr>
			<tr>
				<td>机器码允许次数(当开启验证机器码时,此参数生效)</td>
				<td>
					<label><input type="text" name="activityConfig.sidcount" value="${data.sidcount}"></label>
				</td>
			</tr>

			<tr>
				<td>备注</td>
				<td>
					<textarea rows="10" id="remark"  cols="19" name="activityConfig.remark"></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<button type="reset">重置</button>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="button" value="保存" id="saveConfig" style="margin-left: 160px;"/>
				</td>
			</tr>
		</table>
		<%--<input type="hidden" id="depositAmount" name="depositAmount" />
        <input type="hidden" id="startTimeDeposit" name="startTimeDeposit" />
        <input type="hidden" id="endTimeDeposit" name="endTimeDeposit" />
        <input type="hidden" id="betAmount" name="betAmount" />
        <input type="hidden" id="startTimeBet" name="startTimeBet" />
        <input type="hidden" id="endTimeBet" name="endTimeBet" />--%>
		<input type="hidden" id="vip" name="activityConfig.level" />
		<input type="hidden" id="platform" name="activityConfig.platform" />
		<input type="hidden" id="entrance" name="activityConfig.entrance" />


	</s:form>
</div>
</body>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript">

    $("#remark").val("${data.remark}");
    $("#depositstarttime").val("${data.depositstarttime}");

    $("#depositendtime").val("${data.depositendtime}");


    $("#betstarttime").val("${data.betstarttime}");

    $("#betstarttime").val("${data.betstarttime}");

    $("#activitystarttime").val("${data.activitystarttime}");

    $("#activityendtime").val("${data.activityendtime}");













    $('#saveConfig').click(function () {
        if (confirm("确定要修改此活动吗？")) {
            if (($('#title').val()=='')) {
                alert('标题不能为空！');
                return ;
            }
            var str = "";
            var st="";
            var entrance="";

            $("input[name='entrance']:checked").each(function() {

                entrance += $(this).val()+",";
            });
            if (entrance=='') {

                alert('平台通道不能为空！');
                return;
            }
            entrance=entrance.substring(0,entrance.length-1);
            $('#entrance').val(entrance);

            $("input[name='viplevel']:checked").each(function() {

                str += $(this).val();
            });

            if (str=='') {

                alert('活动等级不能为空！');
                return;
            }



            $('#vip').val(str);

            $("input[name='platform']:checked").each(function() {

                st += $(this).val()+",";
            });
			/*if (st=='') {

			 alert('转入平台不能为空！');
			 return;
			 }*/
            st=st.substring(0,st.length-1);
            $('#platform').val(st);
            var data = $("#mainform").serialize();

            var action = "/office/editSaveActivity.do";

            var xmlhttp = new Ajax.Request(action, { method: 'post', parameters: data + "&r=" + Math.random(), onComplete: function(result) {

                var text = result.responseText;

                alert(text);

                if (text.indexOf('SUCCESS') != -1) {

                    var _parent = window.opener;
                    _parent.mainform.submit();
                    window.close();
                }
            }});
        };
    })







</script>
</html>