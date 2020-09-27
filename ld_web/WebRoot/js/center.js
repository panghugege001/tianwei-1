$(document).ready(function() {
			//Basic Information Percentage
			var updateAliasName=$("#updateAliasName").val();
       		var updateQq=$("#updateQq").val();
       		var updateMailaddress=$("#updateMailaddress").val();
       		var fullInfoCount = 10;
       		var incompleteInfoCount = 7;
       		var percentage = 100.0;
       		var appendHTML="";
       		if(updateAliasName != null && updateAliasName != "") {
       			incompleteInfoCount += 1;
       		}
       		
       		if(updateQq != null && updateQq != "") {
       			incompleteInfoCount += 1;
       		}
       		
       		if(updateMailaddress != null && updateMailaddress != "") {
       			incompleteInfoCount += 1;
       		}
       		
       		percentage = Number((incompleteInfoCount/fullInfoCount)*100).toFixed(1);
       		$("#percentage").html(percentage + "%");
});

function submit_deposit(){
	var depositAmount = $("#depositAmount").val();
	var attach = $("#attach").val();
	var selectedBank = $('input[name=bank_code]').filter(':checked').val();
  	if(isNaN(depositAmount)){
   		alert("[提示]存款额度非有效数字！");
   		return false;
   	}
   	if(depositAmount<100){
	   alert("[提示]100元以上或者100元才能存款！");
   	   return false;
   	}
   	if(depositAmount>5000){
	   alert("[提示]存款金额不能超过5000！");
   	   return false;
   	}
   	if(selectedBank==null ||selectedBank==""){
	   alert("[提示]支付银行不能为空！");
   	   return false;
   	}
    if(attach==null ||attach==""){
	   alert("[提示]系统检测你已经掉线,请重新登录！");
   	   return false;
   	}
  	$("#dinpayRedirect").submit(); 
  	return true;   
  }
  
	function displayDiv(value) {
		$('.items .item').hide();
		$('#money_in, .pub').hide();
		$('.items div.lock div:eq(' + value + ')').show();
	}
	
	function hideDiv(value) {
		$('.items .item1').show();
		$('#money_in, .pub').show();
		$('.items div.lock div:eq(' + value + ')').hide();
		$('.items .item').css("display", "");
		$('#money_in').css("display", "");
	}
	
	function percentageUpdate(updateAliasName,updateQq,updateMailaddress) {
   		var fullInfoCount = 10;
   		var incompleteInfoCount = 7;
   		var percentage = 100.0;
   		var appendHTML="";
   		if(updateAliasName != null && updateAliasName != "") {
   			incompleteInfoCount += 1;
   		}
   		
   		if(updateQq != null && updateQq != "") {
   			incompleteInfoCount += 1;
   		}
   		
   		if(updateMailaddress != null && updateMailaddress != "") {
   			incompleteInfoCount += 1;
  		}
    		
   		percentage = Number((incompleteInfoCount/fullInfoCount)*100).toFixed(1);
   		$("#percentage").html(percentage + "%");
	}
	
