<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>

<script type="text/javascript">
function create(){
	var str="";
	$("input[name='viplevel']:checked").each(function(){   
	     str+=$(this).val();   
	 });
	$("#level").val(str);
    var s="";
    $("input[name='game']:checked").each(function(){
        s+=$(this).val()+",";
    });
    $("#game").val(s.substring(0,s.length-1));

	var state = $("input[name='state']:checked").val();
	$("#state").val(state);
	if(confirm("确定？")){
		var data = $("#mainform").serialize();
		var action = "/office/insertGuild.do";
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
	window.close();
}

function displayTr(value){
	if(value == '自助PT8元优惠'){
		$(".tiyanjin").css("display","block");
		$(".cunsong").css("display","none");
	}else{
		$(".tiyanjin").css("display","none");
		$(".cunsong").css("display","block");
	}
	console.log(value);
}

$(function() {

	var value = $("#title").val();
	
	displayTr(value);
});
</script>

<div id="excel_menu">
	<s:form name="mainform" id="mainform" theme="simple">
		
		<table >
<tr>
	<td>工会分组</td>
	<td>
		<input type="text" name="guild.part" required="required"/><span style="color: red;">*</span>
	</td>
</tr>
<tr></tr>
<tr>
	<td>工会名称 </td>
	<td>
		<input type="text" name="guild.name" required="required"/><span style="color: red;">*</span>
	</td>
</tr>
			<tr></tr>
			<tr>
				<td>工会会长 </td>
				<td>
					<input type="text" name="guild.president" />
				</td>
			</tr>

</table>
<table >
	<tr><td>报名开始时间</td><td><s:textfield name="guild.startTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
	<tr><td>报名结束时间</td><td><s:textfield name="guild.endTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" /></td></tr>
	<tr><td>用户等级</td><td>
		<label><input type="checkbox" name="viplevel" value="0" ><s:property value="@dfh.model.enums.VipLevel@getText(0)"/></label>
		<label><input type="checkbox" name="viplevel" value="1" ><s:property value="@dfh.model.enums.VipLevel@getText(1)"/></label>
		<label><input type="checkbox" name="viplevel" value="2" ><s:property value="@dfh.model.enums.VipLevel@getText(2)"/></label>
		<label><input type="checkbox" name="viplevel" value="3" ><s:property value="@dfh.model.enums.VipLevel@getText(3)"/></label>
		<label><input type="checkbox" name="viplevel" value="4" ><s:property value="@dfh.model.enums.VipLevel@getText(4)"/></label>
		<label><input type="checkbox" name="viplevel" value="5" ><s:property value="@dfh.model.enums.VipLevel@getText(5)"/></label>
		<label><input type="checkbox" name="viplevel" value="6" ><s:property value="@dfh.model.enums.VipLevel@getText(6)"/></label>
		<label><input type="checkbox" name="viplevel" value="7" ><s:property value="@dfh.model.enums.VipLevel@getText(7)"/></label>
		<label><input type="checkbox" name="viplevel" value="8" ><s:property value="@dfh.model.enums.VipLevel@getText(8)"/></label>
		<input type="hidden" id="level" name="guild.level"/>
		<input type="hidden" id="game" name="guild.game"/>
		<input type="hidden" id="state" name="guild.state"/>
	</td></tr>
</table>

<table >
	<tr>
		<td>工会上限人数 </td>
		<td>
			<input type="text" name="guild.max" required="required"/><span style="color: red;">*</span>
		</td>
	</tr>

	<tr><td>游戏平台</td><td>
		<label><input type="checkbox" name="game" value="newpt" >PT</label>
		<label><input type="checkbox" name="game" value="agin" >AG百家乐</label>
		<label><input type="checkbox" name="game" value="aginslot" >AG老虎机</label>
		<label><input type="checkbox" name="game" value="aginfish" >AG捕鱼</label>
		<label><input type="checkbox" name="game" value="mg" >MG</label>
		<label><input type="checkbox" name="game" value="dt" >DT</label>
		<label><input type="checkbox" name="game" value="qt" >QT</label>
		<label><input type="checkbox" name="game" value="nt" >NT</label>
		<label><input type="checkbox" name="game" value="png" >PNG</label>
		<label><input type="checkbox" name="game" value="ttg" >TTG</label>
		<label><input type="checkbox" name="game" value="ea" >EA</label>


	</td></tr>

	<tr><td>开启状态</td><td>
		<label><input type="checkbox" name="state" value="1" >开启</label>
		<label><input type="checkbox" name="state" value="0" >关闭</label>


	</td></tr>

	<tr>
		<td>备注 </td>
		<td>
			<input type="text" name="guild.remark" /></span>
		</td>
	</tr>



<tr><td align="center"><input type="button" value="创建" onclick="create()"/></td><td></td></tr>

</table>
	</s:form>
</div>