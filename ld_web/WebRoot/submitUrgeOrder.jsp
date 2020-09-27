<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</head>

<body>
<div >
	<form action="/asp/submitUrgeOrder.aspx" name="urgeOrderForm" method="post" enctype="multipart/form-data">
		<div class="ui-form">
			<div class="ui-form-item">
				<label for="" class="ui-label">存款类型</label>
				<select id="type" onchange="onChangeType(this.value);" name="type" class="input ui-ipt" required="required">
					<option value="">请选择</option>
					<option value=""  disabled="disabled">==支付宝==</option>
					<option value="1">在线支付宝扫描</option>
					<option value="2">支付宝扫描</option>
					<option value="3">支付宝附言</option>
					<option value=""  disabled="disabled">==微信支付==</option>
					<option value="4">微信扫描</option>
					<option value="5">微信额度验证</option>
					<option value=""  disabled="disabled">==在线支付==</option>
					<option value="6">在线支付</option>
					<option value=""  disabled="disabled">==秒存支付==</option>
					<option value="7">工行附言</option>
					<option value="8">招行附言</option>
					<option value=""  disabled="disabled">==点卡支付==</option>
					<option value="9">点卡支付</option>
				</select>
			</div>
			<div  class="ui-form-item accountNameTd" style="display: none">
				<label for="" class="ui-label">存款人姓名</label>
				<input id="accountName" name="accountName" type="text" maxlength="32" class="ui-ipt txt">
			</div>
			<div class="ui-form-item thirdOrderTd" style="display: none">
				<label id="thirdOrderLbl" for="" class="ui-label">订单号</label>
				<input id="thirdOrder" name="thirdOrder" type="text" maxlength="32" class="ui-ipt txt">
			</div>
			<div class="ui-form-item nicknameTd" style="display: none">
				<label id="nicknameLbl" for="" class="ui-label">昵称</label>
				<input id="nickname" name="nickname" type="text" maxlength="32" class="ui-ipt txt">
			</div>
			<div class="ui-form-item">
				<label for="" class="ui-label">存款时间</label>
				<input type="text" name="depositDate" value="" id="depositDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" required="required" onchange="checkthirdOrder()" class="ui-ipt txt" style="width:122px;margin: 3px 0px; font-size: 14px;"/>-<input type="text" name="depositTime" value="" id="depositTime" onfocus="WdatePicker({dateFmt:'H:mm:ss'})" required="required" class="ui-ipt txt" style="width:122px;margin: 3px 0px; font-size: 14px;"/>
			</div>
			<div class="ui-form-item">
				<label for="" class="ui-label">存款金额</label>
				<input id="amount" name="amount" type="text" maxlength="10" required="required" class="ui-ipt txt">
			</div>
			<div id="cardTr" style="display: none">
				<div class="ui-form-item">
					<label for="" class="ui-label">点卡类型</label>
					<select name="cardtype" id="cardtype" class="ui-ipt txt">
						<option value="">
							－ 请选择点卡 －
						</option>

						<option value=0>
							移动神州行
						</option>
						<option value=1>
							电信国卡
						</option>
						<option value=2>
							QQ币充值卡
						</option>
						<option value=3>
							联通一卡通
						</option>
						<option value=4>
							骏网一卡通
						</option>
						<option value=5>
							盛大一卡通
						</option>
						<option value=6>
							完美一卡通
						</option>
						<option value=7>
							征途一卡通
						</option>
						<option value=8>
							网易一卡通
						</option>
						<option value=9>
							盛付通一卡通
						</option>
						<option value=10>
							搜狐一卡通
						</option>
						<option value=11>
							九游一卡通
						</option>
						<option value=12>
							天宏一卡通
						</option>
						<option value=13>
							天下一卡通
						</option>
						<option value=14>
							纵游一卡通
						</option>
						<option value=15>
							天下一卡通专项
						</option>
					</select>
				</div>
				<div class="ui-form-item">
					<label for="" class="ui-label">点卡卡号</label>
					<input id="cardno" name="cardno" type="text" maxlength="32" class="ui-ipt txt">
				</div>
			</div>
			<div class="ui-form-item" id="pictureTr">
				<label for="" class="ui-label">上传存款截图</label>
				<input type="file" id="picture" name="picture" class="ui-ipt txt"/>
			</div>
			<div class="ui-form-item">
				<label for="" class="ui-label">备注</label>
				<input id="remark" name="remark" type="text" class="ui-ipt txt">
			</div>
			<div class="ui-form-item">
				<input id="submitUrgeOrder" class="btn btn-danger" type="submit" value="提交"/>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	function onChangeType(value){
		$(".txt").val("");
		if(value == "2"){
			var value="支付宝订单号";
			$("#thirdOrderLbl").text(value);
			$("#thirdOrder").attr("required",'required');
			$(".thirdOrderTd").css("display", "");

			$("#accountName").attr("required",'required');
			$(".accountNameTd").css("display", "");

			$("#nickname").removeAttr("required");
			$(".nicknameTd").css("display", "none");

			$("#cardno").removeAttr("required");
			$("#cardTr").css("display", "none");

			$("#picture").removeAttr("required");
			$("#cardtype").removeAttr("required");
		}else if(value=="3"){
			var value="支付宝昵称";
			$("#nicknameLbl").text(value);
			var value="支付宝订单号";
			$("#thirdOrderLbl").text(value);

			$("#accountName").attr("required",'required');
			$(".accountNameTd").css("display", "");

			$("#thirdOrder").attr("required",'required');
			$(".thirdOrderTd").css("display", "");

			$("#nickname").removeAttr("required");
			$(".nicknameTd").css("display", "");

			$("#cardno").removeAttr("required");
			$("#cardTr").css("display", "none");

			$("#picture").removeAttr("required");
			$("#cardtype").removeAttr("required");
		}else if(value=="5"){
			var value="微信昵称";
			$("#nicknameLbl").text(value);

			$("#accountName").removeAttr("required");
			$(".accountNameTd").css("display", "none");

			$("#nickname").attr("required",'required');
			$(".nicknameTd").css("display", "");

			$("#thirdOrder").removeAttr("required");
			$(".thirdOrderTd").css("display", "none");

			$("#cardno").removeAttr("required");
			$("#cardTr").css("display", "none");

			$("#picture").removeAttr("required");
			$("#cardtype").removeAttr("required");
		}else if(value == "6" || value == "1" || value=="4"){
			$("#picture").attr("required",'required');

			$("#accountName").attr("required",'required');
			$(".accountNameTd").css("display", "");

			$("#nickname").removeAttr("required");
			$(".nicknameTd").css("display", "none");

			$("#thirdOrder").removeAttr("required");
			$(".thirdOrderTd").css("display", "none");

			$("#cardno").removeAttr("required");
			$("#cardTr").css("display", "none");

			$("#cardtype").removeAttr("required");
		}else if(value=="9"){
			var value="点卡订单号";
			$("#thirdOrderLbl").text(value);

			$("#accountName").removeAttr("required");
			$(".accountNameTd").css("display", "none");

			$("#thirdOrder").removeAttr("required");
			$(".thirdOrderTd").css("display", "");

			$("#nickname").removeAttr("required");
			$(".nicknameTd").css("display", "none");

			$("#cardno").attr("required","required");
			$("#cardtype").attr("required","required");
			$("#cardTr").css("display", "");
		}else{
			$("#accountName").attr("required",'required');
			$(".accountNameTd").css("display", "");

			$("#nickname").removeAttr("required");
			$(".nicknameTd").css("display", "none");

			$("#thirdOrder").removeAttr("required");
			$(".thirdOrderTd").css("display", "none");

			$("#cardno").removeAttr("required");
			$("#cardTr").css("display", "none");

			$("#picture").removeAttr("required");
			$("#cardtype").removeAttr("required");
		}
	}

	function checkthirdOrder(){
		var thirdOrder = $("#thirdOrder").val();
		var type = jQuery("#type option:selected").val();
		var depositDate = $("#depositDate").val();
		//alert(depositDate);
		if(depositDate !="" && (type=="2" || type=="3")){
			if(thirdOrder ==""){
				alert("请填写支付宝订单号！");
				$("#depositDate").val("");
				return;
			} 
			
			$.ajax({
				type: "post",
				url: "/asp/checkthirdOrder.aspx",
				cache: false,
				async: false,
				data:{"thirdOrder":thirdOrder,"depositDate":depositDate},
				success : function(data){
					if(data!=null && data!=""){
						alert(data);
						$("#depositDate").val("");
					}
				},
				error: function(){alert("系统错误");},
			});
		}
	}

</script>

<c:if test="${not empty errormsg}">
	<script type="text/javascript">
		alert("${errormsg}");
	</script>
</c:if>
</body>
</html>
