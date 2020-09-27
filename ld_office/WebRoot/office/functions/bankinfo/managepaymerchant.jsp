<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<head>
<title>支付开关</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}

function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}

function submitForNewAction(btn,action,pno,useable){
	btn.disabled=true;
	if(confirm("你确认要执行此操作么？")){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"jobPno="+pno+"&useable="+useable+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function responseMethod(data){

	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}
function editmerchantinfo(merid,constid,merchantcode,note){
	var id1=merid+"constid";
	var id2=merid+"merchantcode";
	var id3=merid+"note";
	var str ='#'+merid;
	$(str+' td').remove();
	 var str1="<td align='center' width='80px' ><textarea id='"+id1+"' style=\"height: 20px;width: 170px\" >"+constid+"</textarea></td>";
	 var str2="<td align='center' width='80px' ><textarea id='"+id2+"'  style=\"height: 20px;width: 170px\" >"+merchantcode+"</textarea></td>";
	 var str3="<td align='center' width='80px' ><textarea id='"+id3+"'  style=\"height: 20px;width: 170px\" >"+note+"</textarea></td>";
	 var str4="<td align='center'><input type=\"button\"  value=\"保存\"  onclick=\"saveInfo('"+merid+"');\" />  <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+merid+"');\" /></td>";
	$(str).append(str1+str2+str3+str4);
	}

  function saveInfo(merid){
	  var str ='#'+merid;
	  var str1=$("#"+merid+"constid").val();
	  if(null==str1){
		  alert('内容不允许为空');
		   return ; 
	  }
	  var str2=$("#"+merid+"merchantcode").val();
	  if(null==str2){
		  alert('商户号不允许为空');
		   return ; 
	  }
	  var str3=$("#"+merid+"note").val();
	 
	  $.ajax({
		  url:"/bankinfo/updatepaymerchant.do",
		  type:"post",
		  dataType:"text",
		  data:"merid="+merid+"&constid="+str1+"&merchantcode="+str2+"&note="+str3,
		  async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  alert(strs[0]);
			  $(str+' td').remove();
			  	 var s1="<td align='center' width='80px' >"+str1+"</td>";
				 var s2="<td align='center' width='80px' >"+str2+"</td>";
				 var s3="<td align='center' width='80px' >"+str3+"</td>";
				 var s4="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editmerchantinfo('"+merid+"','"+str1+"','"+str2+"','"+str3+"')\" /> <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+merid+"');\" /></td>";
				$(str).append(s1+s2+s3+s4); 
			  
		  }
	  });
  }
  
  function deleteinfo(ids){
	  $.ajax({
		  url:"/bankinfo/deletepaymerchant.do",
		  type:"post",
		  dataType:"text",
		  data:"merid="+ids,
		  async:false,
		  success : function(msg){
			  alert(msg);
			  $('#'+ids).remove();
		  }
		  
	  })
	  $('#'+ids).remove();
  }
  
  function savechantinfo(){
	  var str1=$("#nr").val();
	  if(null==str1||''==str1){
		  alert('内容不允许为空');
		   return ; 
	  }
	  var str2=$("#shh").val();  
	  if(null==str2||''==str2){
		  alert('商户号不允许为空');
		   return ; 
	  }
	  var str3=$("#bz").val();
	  $.ajax({
		  url:"/bankinfo/updatepaymerchant.do",
		  type:"post",
		  dataType:"text",
		  data:"constid="+str1+"&merchantcode="+str2+"&note="+str3,
		  async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  alert(strs[0]);
			  var merid=$.trim('ids'+strs[1]);
			  	 var s1="<tr bgcolor=\"#e4f2ff\" id='"+merid+"'><td align='center' width='80px' >"+str1+"</td>";
				 var s2="<td align='center' width='80px' >"+str2+"</td>";
				 var s3="<td align='center' width='80px' >"+str3+"</td>";
				 var s4="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editmerchantinfo('"+merid+"','"+str1+"','"+str2+"','"+str3+"')\" /> <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+merid+"')\" /></td></tr>";
				$("#table").append(s1+s2+s3+s4); 
			  
		  }
	  });
  }
</script>
</head>
<body>
<p>
账户 --&gt; 管理支付商户 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>

</br>
</br>
<div id="excel_menu" style="position: absolute; top: 45px; left: 0px;">
<s:form  theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="900px">
								<tr align="left">
									<td>
										内容:
										<s:textfield id="nr" size="30" />
									</td>
									<td>
										商户号:
										<s:textfield id="shh" size="30" />
									</td>
									<td>
										备注:
										<s:textfield id="bz" size="30" />
									</td>
									<td>
										<input type="button" value="新增"
			  onclick="savechantinfo();" />
									</td>

								</tr>
							</table>
						</td>
					</tr>
				</table>
				</s:form>
		</div>

<div id="middle" style="position:absolute; top:125px;left:0px">
  <div id="right">
	  <div id="right_04">
		  <table width="960px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" id='table' >
            <tr bgcolor="#0084ff">
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">内容</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">商户号</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <s:set var="merid" value="#fc.id" scope="request" />
            <s:set var="constid" value="#fc.constid" scope="request" />
            <s:set var="merchantcode" value="#fc.merchantcode" scope="request" />
            <s:set var="note" value="#fc.note" scope="request" />
            <tr bgcolor="#e4f2ff" id='ids${merid}'>
              <td align="center" width="80px" ><s:property value="#fc.constid"/></td>
              <td align="center" width="80px" ><s:property value="#fc.merchantcode"/></td>
              <td align="center" width="80px" ><s:property value="#fc.note"/></td>
              <td align="center" width="35px">
			  <input type="button" value="编辑"
			  onclick="editmerchantinfo('ids${merid}','${constid}','${merchantcode}','${note}');" />
			   <input type="button" value="删除"
			  onclick="deleteinfo('ids${merid}');" />
				</td>
            </tr>
  	 	 </s:iterator>
          </table>
	  </div>
	
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

