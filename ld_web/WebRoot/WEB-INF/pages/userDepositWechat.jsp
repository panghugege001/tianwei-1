<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
	<title>龙都娱乐城--手机投注</title>
</head>
<body>
<div class="pay-page">
	<jsp:include page="/tpl/header.jsp"></jsp:include>

	<div class="pay-online-wp">
    	 <jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>
		<form id="dinpayRedirect" action="${ctx}/asp/XlbRedirect.aspx" method=post name="dinpayRedirect" id="dinpayRedirect">
			<input type="hidden" name="attach" id="attach" value="${session.customer.loginname}" />
			<input type="hidden" name="payType" id="payType" value="微信支付" />
			
	     <c:if test="${not  empty wxValidaTeAmout}">
                <table class="account-info table-pay">
                 <tr>
                  <td width="168" height="40">账户 名: ${session.customer.loginname} </td>
                  <td width="460" height="40">账户余额：${session.customer.credit}元</td>
                  <td width="133" height="40"></td>
                  <td width="100" height="40"><input type="button" class="btn btn-danger" onclick="destroyPayOrder(this,'${wxValidaId}');"  value="作废此订单" /> </td>
                  
                </tr>
                  <tr class="showImg" >
                    <td colspan="5" align="center"><p id="depositResult" style="padding:9px 0;">
                       <span style="font-size:28px;">当前支付订单，请您存入&nbsp;&nbsp;<span class="red" style="font-size:42px;">${wxValidaTeAmout}&nbsp;元</span>，否则无法到账!</span> </p>
                     </td>
                  </tr>
                  <tr class="showImg" > 
                    <c:if test="${ not empty wxBank.zfbImgCode}">
                      <td align="right" width="130" >微信二维码：</td>
                      <td  colspan="4" align="left" style="padding-left: 25px;"><span id="id_zfb_msg"><img src="${wxBank.zfbImgCode}" /></span></td>
                    </c:if>
                    <c:if test="${ empty wxBank.zfbImgCode}">
                      <td align="right"> 微信账号： </td>
                      <td colspan="4" align="left" height="40"><input class="text" name="optEmail" readonly value="${wxBank.accountno}" type="text" /></td>
                    </c:if>
                  </tr>
                    <tr>
                      <td height="40" align="right"></td>
                      <td height="90" colspan="4" class="wenxin"><p>温馨提示：</p>
                          <p>1.【微信额度支付】单笔最低10元，最高5000.并且<span class="c-red">存款额度必须输入大于10的含2位小数的数字。如10.11  100.05  1000.38</span></p>
                          <p>2.请您一定按照您输入的存款额度进行存款否则将无法到帐。<span class="c-red">如您输入10.11那么您支付的时候必须支付10.11</span>.如未按照要求存款，请您提供账号，存款金额，时间，存款成功的截图联系在线客服及时处理。</p>
                          <p>3.如您获取的订单，您已按照要求成功支付，请您耐心等待系统处理。<span class="c-red">同一笔订单只能支付一次，不可二次支付。（微信额度支付有1-5分钟的延迟）</span></p>
                          <p>4.如果您未支付订单或想重新获取新的额度的订单，请您点击右上角的<span class="c-red">“作废此订单”</span>即可。如有疑问请及时咨询在线客服。</p>
                      </td>
                    </tr>
                </table>
                
           </c:if>
			
		<c:if test="${empty wxValidaTeAmout}">
			<table class="account-info table-pay">
				<tr>
					<th>账户名:</th>
					<td>${session.customer.loginname}</td>    
				</tr>
				<tr>
					<th>账户余额：</th>
					<td class="c-red">${session.customer.credit}元</td>
				</tr>
				<tr>
					<th> 存款额度：</th>
					<td>
						<div class="ipt-group">
							<input  name="depositAmountInput" id="depositAmountInput" onkeypress="return mykeypress(this, event);" onblur="clearNoNum(this);" type="text" maxlength="10" class="ipt-txt" />
							<span class="c-red">* (必填)只能输入小数</span>
						</div>
					</td>

				</tr>
				<tr>
					<th>支付银行：</th>
					<td>
						<div class="ipt-group">
			                        <select name="payCode" id="bankco" class="input" readonly>
			                            <option value="100040" readonly>微信支付</option>
			                        </select>*必选
							<span class="c-red">* 必填</span>
						</div></td>
				</tr>
				<tr>
					<th>&nbsp;</th>
					<td>
						<div class="ipt-group">
							<input name="Submit" type="button" class="btn btn-danger" onclick="return createValidatedPayOrder();" value="确定支付" />
						</div>
					</td>
				</tr>
                <tr class="showImg" style="display:none;">
                    <td colspan="2" align="center">
                        <p id="depositResult" style="margin-top: 40px;"></p>
                    </td>
                </tr>
				
			    <tr class="showImg" style="display:none;"> 
                     <c:if test="${ not empty wxBank.zfbImgCode}">
                       <td align="right" width="130" >微信二维码：</td>
                       <td align="left" style="padding-left: 25px;"><span id="id_zfb_msg"><img src="${wxBank.zfbImgCode}" /></span></td>
                     </c:if>
                     <c:if test="${ empty wxBank.zfbImgCode}">
                       <td align="right"> 微信账号： </td>
                       <td align="left" height="40"><input class="text" name="optEmail" readonly value="${wxBank.accountno}" type="text" /></td>
                     </c:if>
                   </tr>
	           		
	           		<tr>
	           			<td colspan="2">
                            <p>温馨提示：</p>
                            <p>1.【微信额度支付】单笔最低10元，最高5000.并且<span class="c-red">存款额度必须输入大于10的含2位小数的数字。如10.11  100.05  1000.38</span></p>
                            <p>2.请您一定按照您输入的存款额度进行存款否则将无法到帐。<span class="c-red">如您输入10.11那么您支付的时候必须支付10.11</span>.如未按照要求存款，请您提供账号，存款金额，时间，存款成功的截图联系在线客服及时处理。</p>
                            <p>3.如您获取的订单，您已按照要求成功支付，请您耐心等待系统处理。<span class="c-red">同一笔订单只能支付一次，不可二次支付。（微信额度支付有1-5分钟的延迟）</span></p>
                            <p>4.如果您未支付订单或想重新获取新的额度的订单，请您点击右上角的<span class="c-red">“作废此订单”</span>即可。如有疑问请及时咨询在线客服。</p>
                        </td>
	           		</tr>
			</table>
			</c:if>
		</form>
	</div>
</div>
<script type="text/javascript">
var flag = true;
function createValidatedPayOrder(){
    var depositAmount = $("#depositAmountInput").val();
    if(depositAmount=='' || $.trim(depositAmount)==''){return;}
    if(depositAmount < 10){alert('最低存款10元！');return;}
    if(depositAmount > 5000){alert('不能超过5000元！');return;}
    if(!this.flag==true){
    	alert("输入金额格式不正确");
    	return false;
    }
    openProgressBar();
    $(".showImg").css("display","none");
    $.post("${ctx}/asp/createValidateAmountPayOrderTwo.aspx", {amount:depositAmount,type:"1"}, function(data){
        if(data.code=='1') {
        	window.location.href=window.location.href;
        } else {
            alert(data.msg);
        }
    }).fail(function(){
        alert("生成订单失败");
    }).always(function() {
        closeProgressBar();
    });
}


function checkDepositAmount(){
    var depositAmount = $("#depositAmountInput").val();
    if(depositAmount=='' || $.trim(depositAmount)==''){return;}
    if(depositAmount < 10){alert('最低存款10元！');return;}
    if(depositAmount > 5000){alert('不能超过5000元！');return;}
    $.post("${ctx}/asp/createValidateAmountPayOrderGj.aspx", {amount:depositAmount,type:"1"}, function(data){
        if(data.code=='0') {
        	 alert(data.msg);
        }
    })
}

//作废此订单
function destroyPayOrder(btn,id){
    $.post("${ctx}/asp/discardDepositOrder.aspx", {id:id}, function(data){
        if(data.code=='1') {
        	 location.href = "${ctx}/wechatDepositRedirect.aspx";
        }
    }).fail(function(){
    	   alert("废除订单失败");
    });
}

//验证输入input
function clearNoNum(obj){
  // 事件中进行完整字符串检测
  var _self = obj;
  var _value = obj.value;
  var patt = /^((?!0)\d+\.[0-9][1-9])$/g;
      if (!patt.test(_value)) {
          // 错误提示相关代码，边框变红、气泡提示什么的
          alert("输入金额格式错误");
          this.flag = false;
      } else {
    	  this.flag = true;
      	  checkDepositAmount();
      }
}


function mykeypress(obj, e){
    // 在 keyup 事件中拦截错误输入
    var keynum;
    if(window.event) { // IE                    
      keynum = e.keyCode;
    } else if(e.which){ // Netscape/Firefox/Opera                   
      keynum = e.which;
    }
    var sCharCode = String.fromCharCode(keynum);
    var sValue = obj.value;
    if (/[^0-9.]/g.test(sCharCode) || __getRegex(sCharCode).test(sValue)) {
    	this.flag =false;
        return false;
    }else{
    	this.flag =true;
    }
}
    /**
     * 根据用户输入的字符获取相关的正则表达式
     * @param  {string} sCharCode 用户输入的字符，如 'a'，'1'，'.' 等等
     * @return {regexp} patt 正则表达式
     */
    function __getRegex (sCharCode) {
        var patt;
        if (/[0]/g.test(sCharCode)) {
            // 判断是否为空
            patt = /^$/g;
        } else if (/[.]/g.test(sCharCode)) {
            // 判断是否已经包含 . 字符或者为空
            patt = /((\.)|(^$))/g;
        } else if (/[0-9]/g.test(sCharCode)) {
            // 判断是否已经到达小数点后两位
            patt = /\.\d{2}$/g;
        }
        return patt;
}
</script>
<jsp:include page="/tpl/footer.jsp"></jsp:include>

	
</body>
</html>