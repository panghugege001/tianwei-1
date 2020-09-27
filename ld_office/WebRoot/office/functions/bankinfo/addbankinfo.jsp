<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>增加银行账户</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.2.js"></script>
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
		  //email账号
	      $('#div1').css('display','block');
	      $('#div2').css('display','block');
	      //VPN账号
	      $('#div3').css('display','block');
	      $('#div4').css('display','block');
	      //VPN密码
	      $('#div5').css('display','block');
	      $('#div6').css('display','block');
	      //银行登录账号
	      $('#div17').css('display','block');
	      $('#div18').css('display','block');
	      //银行登录账号
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
}
$(document).ready(function () {
	divType();
});
</script>
</head>
<body>
<div id="excel_menu_left">
账户 --> 增加银行账户 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
<p align="left" style="color: red"><s:fielderror/></p>
<s:form action="addbankinfo" onsubmit="submitonce(this);" namespace="/bankinfo" name="mainform" id="mainform" theme="simple">
<table align="left" >
<tr><td>帐户名称:<span style="color:red">*</span></td><td><s:textfield name="username" size="30" /></td></tr>
<tr><td>账户性质:<span style="color:red">*</span></td><td><s:select onchange="divType();" name="type" list="%{#application.Banktype}" id="type" emptyOption="true" listKey="code" listValue="text" /></td></tr>
<tr><td>账户类型:<span style="color:red">*</span></td><td><s:select name="bankInfoType" list="%{#application.BankInfoType}" id="type" emptyOption="true" listKey="code" listValue="text" onchange="bankInfoType2(this);"/></td></tr>
<tr><td>银行名称:</td><td><s:select name="bankname" list="%{#application.IssuingBankEnum}" emptyOption="true" listKey="issuingBank" listValue="issuingBankCode" /></td></tr>
<tr><td>银行开户人:<span style="color:red">*</span></td><td><s:textfield name="realname" size="30" /></td></tr>
<tr><td>银行卡号:<span style="color:red">*</span></td><td><s:textfield name="bankcard" size="30" /></td></tr>
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='card'" ><tr><td>U盾密码:</td><td>
									<s:textfield name="usb" size="30" />
								</td></tr></s:if>
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
								<s:textfield name="vpnpassword" size="30" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div id="div17" style="display: none;">
								银行登录账号：<span style="color:red">*</span>
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
							<div class="depositMax">
								存款大于：
							</div>
						</td>
						<td>
							<div class="depositMax" >
								<s:textfield name="depositMax" size="15" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="depositMin" >
								存款小于：
							</div>
						</td>
						<td>
							<div class="depositMin">
								<s:textfield name="depositMin" size="15" />
							</div>
						</td>
					</tr>
		        	<tr>
						<td>
							<div class="paytypeCss" >类别：</div>
						</td>
						<td>
							<div class="paytypeCss" >
								<s:select name="paytype" list="#{'0':'网银','1':'支付宝','22':'支付宝新版','4':'微信','5':'云闪付','6':'银行二维码','7':'同略云银行','8':'微信二维码收款','9':'支付宝二维码收款'}" emptyOption="true"/>
							</div>
						</td>
					</tr>
					<tr>
						<td valign="top">备注:</td>
						<td><s:textarea name="remark" cols="30" rows="5"/></td>
					</tr>
					<tr>
						<td align="center"><s:submit value="提交" /></td>
						<td></td>
					</tr>
				</table>
			</s:form>
		</div>

<br/>
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
<script>
	function bankInfoType2(t) {
		if (t.value == 1) {
			$("#type").html(""); 
			$("#type").append("<option value=''></option>");
			$("#type").append("<option value='5'>事务账户(人民币)</option>");
			$("#type").append("<option value='6'>事务账户(比索)</option>");
		} else {
			$("#type").html(""); 
			$("#type").append("<option value=''></option>");
		    $("#type").append("<option value='1'>存款账户</option>");
		    $("#type").append("<option value='2'>支付账户</option>");
		    $("#type").append("<option value='3'>存储账户</option>");
            $("#type").append("<option value='10'>付款储备</option>");
            $("#type").append("<option value='111'>下发储备</option>");
            $("#type").append("<option value='4'>在线账户</option>");
		    $("#type").append("<option value='40'>在线账户1[智付(2030028882)]</option>");
		    $("#type").append("<option value='41'>在线账户2[智付1(2030000006)]</option>");
		    $("#type").append("<option value='42'>在线账户3[智付2(2030020118)]</option>");
		    $("#type").append("<option value='43'>在线账户4[智付3(2030020119)]</option>");
		    $("#type").append("<option value='411'>在线账户[通用智付1(2000299843)]</option>");
		    $("#type").append("<option value='412'>在线账户[通用智付2]</option>");
		    $("#type").append("<option value='413'>在线账户[智付微信]</option>");
		    $("#type").append("<option value='414'>在线账户[智付微信1(2000299861)]</option>");
		    
		    $("#type").append("<option value='4000'>在线账户[智付点卡]</option>");
		    $("#type").append("<option value='4001'>在线账户[智付点卡1]</option>");
		    $("#type").append("<option value='4002'>在线账户[智付点卡2]</option>");
		    $("#type").append("<option value='4003'>在线账户[智付点卡3]</option>");
		    $("#type").append("<option value='4010'>在线账户[智付点卡(2000295555)]</option>");
		    $("#type").append("<option value='4011'>在线账户[智付点卡1(2000295566)]</option>");
		    $("#type").append("<option value='450'>在线账户[乐富微信]</option>");
		    $("#type").append("<option value='451'>在线账户[微信支付直连(暂时不用)]</option>");
		    $("#type").append("<option value='460'>在线账户[新贝微信]</option>");
		    $("#type").append("<option value='470'>口袋支付宝</option>");
		    $("#type").append("<option value='471'>口袋微信支付</option>");
		    $("#type").append("<option value='474'>口袋微信支付2</option>");
		    $("#type").append("<option value='478'>口袋微信支付3</option>");
		    $("#type").append("<option value='494'>口袋支付宝2</option>");
		    $("#type").append("<option value='492'>优付微信</option>");
		    $("#type").append("<option value='495'>千网微信</option>");
		    $("#type").append("<option value='472'>海尔支付</option>");
		    $("#type").append("<option value='481'>汇付宝微信</option>");
		    $("#type").append("<option value='473'>聚宝支付宝</option>");
		    $("#type").append("<option value='485'>迅联宝</option>");
		    $("#type").append("<option value='486'>迅联宝网银</option>");
		    $("#type").append("<option value='497'>迅联宝支付宝</option>");
		    $("#type").append("<option value='488'>优付支付宝</option>");  
		    $("#type").append("<option value='489'>新贝支付宝</option>");
		    $("#type").append("<option value='491'>银宝支付宝</option>");
		    $("#type").append("<option value='493'>千网支付宝</option>");
		    $("#type").append("<option value='51'>汇潮网银</option>");
		    
		    $("#type").append("<option value='44'>在线账户[汇付]</option>");
		    $("#type").append("<option value='441'>在线账户[汇付1]</option>");
		    $("#type").append("<option value='442'>在线账户[汇付2]</option>");
		    $("#type").append("<option value='443'>在线账户[汇付3]</option>");
		    $("#type").append("<option value='444'>在线账户[汇付4]</option>");
		    $("#type").append("<option value='445'>在线账户[汇付5]</option>");
		    $("#type").append("<option value='446'>在线账户[汇付6]</option>");
		    $("#type").append("<option value='447'>在线账户[汇付7]</option>");
		    $("#type").append("<option value='50'>在线账户[汇潮]</option>");
		    $("#type").append("<option value='60'>在线账户[支付宝]</option>");
		    $("#type").append("<option value='61'>在线账户[币付宝]</option>");
		    $("#type").append("<option value='62'>在线账户[国付宝1]</option>");
		    $("#type").append("<option value='5'>事务账户(人民币)</option>");
		    $("#type").append("<option value='6'>事务账户(比索)</option>");
		    $("#type").append("<option value='7'>VIP存款账户</option>");
		    $("#type").append("<option value='8'>中转账户</option>");
		    $("#type").append("<option value='9'>额度验证存款账户</option>");
		}
	}
</script>
</body>
</html>
