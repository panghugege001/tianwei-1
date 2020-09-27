<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>增加银行账户</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
function loadInfo(){
var frm=document.getElementById("mainform");
frm.action="<c:url value='/office/getUserInfo.do' />";
frm.submit();
}
function divType(){
	var type=$("#type").val();
	 var typeText=$("#type option:selected").text();
	  if(type==1 || type==9){
	      $('#div1').css('display','block');
	      $('#div2').css('display','block');
	      $('#div3').css('display','block');
	      $('#div4').css('display','block');
	      $('#div5').css('display','block');
	      $('#div6').css('display','block');
	      $('#div7').css('display','block');
	      $('#div8').css('display','block');
	      $('#div17').css('display','block');
	      $('#div18').css('display','block');
	      $('#div19').css('display','block');
	      $('#div20').css('display','block');
	      
	      $(".samebankCss").css('display','block');
	      $(".crossbankCss").css('display','block');
	      $(".transfermoneyCss").css('display','block');
	      $(".remoteipCss").css('display','block');
	      $(".autopayCss").css('display','none');
	      $('.feeCss').css('display','none');
	  }else if(type==8 || type==2){
		  $('#div17').css('display','block');
	      $('#div18').css('display','block');
	      $('#div19').css('display','block');
	      $('#div20').css('display','block');
	      
	      $('#div1').css('display','none');
	      $('#div2').css('display','none');
	      $('#div3').css('display','none');
	      $('#div4').css('display','none');
	      $('#div5').css('display','none');
	      $('#div6').css('display','none');
	      $(".samebankCss").css('display','none');
	      $(".crossbankCss").css('display','none');
	      $(".remoteipCss").css('display','block');
	      if(type==2){
	          $('.autopayCss').css('display','block');
	          $('.feeCss').css('display','block');
	          $(".transfermoneyCss").css('display','none');
	      }else if(type==8){
	          $('.autopayCss').css('display','none');
	          $('.feeCss').css('display','none');
	          $(".transfermoneyCss").css('display','block');
	      }
	  }else{
	      $('#div1').css('display','none');
	      $('#div2').css('display','none');
	      $('#div3').css('display','none');
	      $('#div4').css('display','none');
	      $('#div5').css('display','none');
	      $('#div6').css('display','none');
	      $('#div7').css('display','none');
	      $('#div8').css('display','none');
	      $('#div17').css('display','none');
	      $('#div18').css('display','none');
	      $('#div19').css('display','none');
	      $('#div20').css('display','none');
	      $(".samebankCss").css('display','none');
	      $(".crossbankCss").css('display','none');
	      $(".transfermoneyCss").css('display','none');
	      $(".autopayCss").css('display','none');
	      $(".remoteipCss").css('display','none');
	      $('.feeCss').css('display','none');
	  }
	  if(typeText.indexOf("在线账户")>=0){
		  $(".transfermoneyCss").css('display','block');
		  $(".remoteipCss").css('display','block');
		  $('#div17').css('display','block');
	      $('#div18').css('display','block');
	      $('#div19').css('display','block');
	      $('#div20').css('display','block');
	      
	  }
	  
	  if($("#bankname").val() == '支付宝' || $("#bankname").val() == '微信'  || $("#bankname").val() == '通联转账'  || $("#bankname").val() == '云闪付' || $("#bankname").val() == '银行二维码' || $("#bankname").val() == '同略云银行' || $("#bankname").val() == '微信二维码收款' || $("#bankname").val() == '支付宝二维码收款'){
	      $('.zfbCodeCss').css('display','block');
	  }else{
	      $('.zfbCodeCss').css('display','none');
	  }
	  
	  if(($("#bankname").val() == '青岛银行' || $("#bankname").val() == '招商银行' || $("#bankname").val() == '兴业银行' || $("#bankname").val() == '民生银行'  || $("#bankname").val() == '平安银行' || $("#bankname").val() == '中信银行' || $("#bankname").val() == '华夏银行' || $("#bankname").val() == '浦发银行' || $("#bankname").val() == '农业银行' || $("#bankname").val() == '中国银行'|| $("#bankname").val() == '交通银行'|| $("#bankname").val() == '工商银行' || $("#bankname").val() == '建设银行' || $("#bankname").val() == '云闪付' || $("#bankname").val() == '银行二维码' || $("#bankname").val() == '同略云银行' || $("#bankname").val() == '微信二维码收款' || $("#bankname").val() == '支付宝二维码收款') && type==1 ){     
		  $('.paytypeCss').css('display','block');
	      var paytype = $('#paytype').val();
	      if(paytype == 0){
	    	  paytype="网银";    
	      }
	      else if(paytype == 1){
	    	  paytype="支付宝";
	      }
	      else if(paytype == 5){
	    	  paytype="云闪付";
	      }
	      else if(paytype == 6){
	    	  paytype="银行二维码";
	      }
	      else if(paytype == 7){
	    	  paytype="同略云";
	      }
	      else if(paytype == 8){
	    	  paytype="微信二维码收款";
	      }
	      else if(paytype == 9){
	    	  paytype="支付宝二维码收款";
	      }
	      $('#vpnpassword').val(paytype);
	  }else{
	      $('.paytypeCss').css('display','none');
	  }
	  
}
$(document).ready(function () {
	var str="";
	$("#checkAllBox").bind("click", function () {
    	if($(this).attr("checked") == "checked"){
        	$("[name = item]:checkbox").attr("checked", true);
    	}else{
    		$("[name = item]:checkbox").attr("checked", false);
    	}
    	//加入userrole
    	$("#vip").val("");str="";
    	$("input[name='item']:checked").each(function(){   
   	     str+=$(this).val();
   		});
   		$("#vip").val(str);
    });
    $("[name = item]:checkbox").bind("click", function () {
    	if($(this).attr("checked") != "checked"){
    		$("#checkAllBox").attr("checked", false);
    	}
    	var flag = true ;
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == undefined){
    			flag = false ;
    		}else{
    			flag = flag && $(this).attr("checked");
    		}
    	});
    	if(flag){
    		$("#checkAllBox").attr("checked", true);
    	}
    	//加入userrole
    	$("#vip").val("");str="";
    	$("input[name='item']:checked").each(function(){   
   	     str+=$(this).val();
   		});
   		$("#vip").val(str);
    });
    
	divType();
});
function functionCheckbox(id,userrole){
      var xmlhttp = new Ajax.Request(    
			"/bankinfo/updageuserrole.do",
		    {    
		         method: 'post',
		         parameters:"jobPno="+id+"&userrole="+userrole+"&r="+Math.random(),
		         onComplete: responseMethod  
		    }
	  );
}

function responseMethod(data){

	//alert(data.responseText);
	
}
</script>
	</head>
	<body>
		<div id="excel_menu_left">
			账户 --> 增加银行账户
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<p align="left" style="color: red">
				<s:fielderror />
			</p>
			<s:form action="editbankinfo" onsubmit="submitonce(this);" namespace="/bankinfo" name="mainform" id="mainform" theme="simple">
				<table align="left">
					<s:hidden name="id"></s:hidden>
					<tr>
						<td>
							帐户名称:
						</td>
						<td>
							<s:textfield name="username" size="30" />
						</td>
					</tr>
					<tr>
						<td>
							账户性质:
						</td>
						<td>
							<s:select onchange="divType();" name="type"
								list="%{#application.Banktype}" id="type" emptyOption="true"
								listKey="code" listValue="text" />
						</td>
					</tr>
					<tr>
						<td>
							银行名称:
						</td>
						<td>
							<s:select onchange="divType();" id="bankname" name="bankname" list="%{#application.IssuingBankEnum}"
								emptyOption="true" listKey="issuingBank"
								listValue="issuingBankCode" />
						</td>
					</tr>
					<tr>
						<td>
							账户类型:
						</td>
						<td>
							<s:select name="bankInfoType" list="%{#application.BankInfoType}"
								id="type" emptyOption="true" listKey="code" listValue="text" />
						</td>
					</tr>
					<tr>
						<td>
							银行开户人:
						</td>
						<td>
                           <s:textfield name="realname" size="30" />
						</td>
					</tr>
					<tr>
						<td>
							银行卡号:
						</td>
						<td>
							<s:textfield name="bankcard" size="30" />
						</td>
					</tr>
							<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='card'" >
				   <tr>
						<td>
							U盾密码:
						</td>
						<td>
									<s:textfield name="usb" size="30" />
						</td>
					</tr></s:if>
					<tr>
						<td>
							<div id="div1" style="display: none;">
								email账号：
							</div>
						</td>
						<td>
							<div id="div2" style="display: none;">
								<s:textfield name="accountno" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="div3" style="display: none;">
								VPN账号：
							</div>
						</td>
						<td>
							<div id="div4" style="display: none;">
								<s:textfield name="vpnname" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="div5" style="display: none;">
								VPN密码：
							</div>
						</td>
						<td>
							<div id="div6" style="display: none;">
								<s:textfield name="vpnpassword" id="vpnpassword" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="div17" style="display: none;">
								银行登录账号：
							</div>
						</td>
						<td>
							<div id="div18" style="display: none;">
								<s:textfield name="loginname" size="30" />
							</div>
						</td>
					</tr>
								<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='card'" >
					<tr>
						<td>
							<div id="div19" style="display: none;">
								银行登录密码：
							</div>
						</td>
						<td>
							<div id="div20" style="display: none;">
									<s:textfield name="password" size="30" />
							</div>
						</td>
					</tr></s:if>
					<tr>
						<td>
							<div id="div7" style="display: none;">
								玩家权限：
							</div>
						</td>
						<td>
							<div id="div8" style="display: none;">
							<label><input type="checkbox" id="checkAllBox" />全选</label>
							
								<label><input type="checkbox" name="item" value="0" <s:if test="userrole.contains(\"0\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(0)"/></label>
								<label><input type="checkbox" name="item" value="1" <s:if test="userrole.contains(\"1\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(1)"/></label>
								<label><input type="checkbox" name="item" value="2" <s:if test="userrole.contains(\"2\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(2)"/></label>
								<label><input type="checkbox" name="item" value="3" <s:if test="userrole.contains(\"3\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(3)"/></label>
								<label><input type="checkbox" name="item" value="4" <s:if test="userrole.contains(\"4\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(4)"/></label>
								<label><input type="checkbox" name="item" value="5" <s:if test="userrole.contains(\"5\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(5)"/></label>
								<label><input type="checkbox" name="item" value="6" <s:if test="userrole.contains(\"6\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(6)"/></label>
								<label><input type="checkbox" name="item" value="7" <s:if test="userrole.contains(\"7\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(7)"/></label>
								<label><input type="checkbox" name="item" value="8" <s:if test="userrole.contains(\"8\")">checked="checked"</s:if> /><s:property value="@dfh.model.enums.VipLevel@getText(8)"/></label>
								<input type="hidden" id="vip" name="userrole"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="remoteipCss" style="display: none;">
								银行远程IP：
							</div>
						</td>
						<td>
							<div class="remoteipCss" style="display: none;">
								<s:textfield name="remoteip" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="autopayCss" style="display: none;">
								自动付款：
							</div>
						</td>
						<td>
							<div class="autopayCss" style="display: none;">
								<s:select name="autopay" list="#{'0':'否','1':'是'}" emptyOption="true"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="samebankCss" style="display: none;">
								同行银行：
							</div>
						</td>
						<td>
							<div class="samebankCss" style="display: none;">
								<s:textfield name="samebank" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="crossbankCss" style="display: none;">
								跨行银行：
							</div>
						</td>
						<td>
							<div class="crossbankCss" style="display: none;">
								<s:textfield name="crossbank" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="transfermoneyCss" style="display: none;">
								转账金额：
							</div>
						</td>
						<td>
							<div class="transfermoneyCss" style="display: none;">
								<s:textfield name="transfermoney" size="10" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="transfermoneyCss" style="display: none;">
								存款大于：
							</div>
						</td>
						<td>
							<div class="transfermoneyCss" style="display: none;">
								<s:textfield name="depositMax" size="10" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="transfermoneyCss" style="display: none;">
								存款小于：
							</div>
						</td>
						<td>
							<div class="transfermoneyCss" style="display: none;">
								<s:textfield name="depositMin" size="10" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="paytypeCss" >类别：</div>
						</td>
						<td>
							<div class="paytypeCss" style="display: none;">
								 <s:select name="paytype"  list="#{'0':'网银','1':'支付宝','22':'支付宝新版','4':'微信','5':'云闪付','6':'银行二维码','7':'同略云银行','8':'微信二维码收款','9':'支付宝二维码收款'}" id="paytype" listKey="key"  listValue="value" emptyOption="false" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="feeCss" style="display: none;">
								手续费：
							</div>
						</td>
						<td>
							<div class="feeCss" style="display: none;">
								<s:textfield name="fee" size="10" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="zfbCodeCss" style="display: none;">
								是否是扫描账号：
							</div>
						</td>
						<td>
							<div class="zfbCodeCss" style="display: none;">
								<s:select name="scanAccount" list="#{'0':'否','1':'是'}" emptyOption="true"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="zfbCodeCss" style="display: none;">
								二维码图片地址：
							</div>
						</td>
						<td>
							<div class="zfbCodeCss" style="display: none;">
								<s:textfield name="zfbImgCode" />
							</div>
						</td>
					</tr>
					<tr>
						<td valign="top">
							备注:
						</td>
						<td>
							<s:textarea name="remark" cols="30" rows="5" />
						</td>
					</tr>
					<tr>
						<td align="center">
							<s:submit value="提交" />
						</td>
						<td></td>
					</tr>
					<tr>
						<td colspan="2"><img src="${zfbImgCode}"/></td>
					</tr>
				</table>
			</s:form>
		</div>

		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">

						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>
