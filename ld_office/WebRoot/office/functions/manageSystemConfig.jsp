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
<title></title>
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
function editinfo(id,typeno,typename,itemno,value,note,flag){
	var id1=id+"typeno";
	var id2=id+"typename";
	var id3=id+"itemno";
	var id4=id+"value";
	var id5=id+"note";
	var id6=id+"flag";
	var str ='#'+id;
	$(str+' td').remove();
	 var str1="<td align='center' width='80px' ><textarea id='"+id1+"' style=\"height: 20px;width: 170px\" >"+typeno+"</textarea></td>";
	 var str2="<td align='center' width='80px' ><textarea id='"+id2+"'  style=\"height: 20px;width: 170px\" >"+typename+"</textarea></td>";
	 var str3="<td align='center' width='80px' ><textarea id='"+id3+"'  style=\"height: 20px;width: 170px\" >"+itemno+"</textarea></td>";
	 var str4="<td align='center' width='80px' ><textarea id='"+id4+"'  style=\"height: 20px;width: 170px\" >"+value+"</textarea></td>";
	 var str5="<td align='center' width='80px' ><textarea id='"+id6+"'  style=\"height: 20px;width: 170px\" >"+flag+"</textarea></td>";
	 var str6="<td align='center' width='80px' ><textarea id='"+id5+"'  style=\"height: 20px;width: 170px\" >"+note+"</textarea></td>";
	 var str7="<td align='center'><input type=\"button\"  value=\"保存\"  onclick=\"saveInfo('"+id+"');\" />  <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+id+"');\" /></td>";
	$(str).append(str1+str2+str3+str4+str5+str6+str7);
	}

  function saveInfo(id){
	  var str ='#'+id;
	  var str1=$("#"+id+"typeno").val();
	  if(null==str1||''==str1){
		  alert('配置项编码不允许为空');
		   return ; 
	  }
	  
	  var str2=$("#"+id+"typename").val();
	  if(null==str2||''==str2){
		  alert('配置项名称不允许为空');
		   return ; 
	  }
	  
	  var str3=$("#"+id+"itemno").val();
	  if(null==str3||''==str3){
		  alert('配置项值代码不允许为空');
		   return ; 
	  }
	  
	  var str4=$("#"+id+"value").val();
	  if(null==str4||''==str4){
		  alert('配置项值不允许为空');
		   return ; 
	  }
	  
	  var str6=$("#"+id+"flag").val();
	  if(null==str6||''==str6){
		  alert('是否禁用不允许为空');
		   return ; 
	  }
	  if(!(str6=='是'||str6=='否')){
		  alert('是否禁用的值仅允许填写是或者否');  
		  return ; 
	  }
	
	
	  var str5=$("#"+id+"note").val();
	 
	  $.ajax({
		  url:"/bankinfo/updateSystemConfig.do",
		  type:"post",
		  dataType:"text",
		 // data:"typeno="+str1+"&typename="+str2+"&itemno="+str3+"&value="+str4+"&note="+str5+"&id="+id+"&flag="+str6,
		 data:{"typeno":str1,"typename":str2,"itemno":str3,"value":str4,"note":str5,"id":id,"flag":str6},  
		 async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  alert(strs[0]);
			  $(str+' td').remove();
			  	 var s1="<td align='center' width='80px' >"+str1+"</td>";
				 var s2="<td align='center' width='80px' >"+str2+"</td>";
				 var s3="<td align='center' width='80px' >"+str3+"</td>";
				 var s4="<td align='center' width='80px' >"+str4+"</td>";
				 var s5="<td align='center' width='80px' >"+str6+"</td>";
				 var s6="<td align='center' width='80px' >"+str5+"</td>";
				 var s7="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editinfo('"+id+"','"+str1+"','"+str2+"','"+str3+"','"+str4+"','"+str5+"','"+str6+"')\" /> <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+id+"');\" /></td>";
				$(str).append(s1+s2+s3+s4+s5+s6+s7); 
			  
		  }
	  });
  }
  
  function deleteinfo(ids){
	  var choice=confirm("您确认要删除吗？", function() { }, null);
	 if(choice){
	  $.ajax({
		  url:"/bankinfo/deletesystemConfig.do",
		  type:"post",
		  dataType:"text",
		  data:"id="+ids,
		  async:false,
		  success : function(msg){
			  alert(msg);
			  $('#'+ids).remove();
		  }
		  
	  })
	  $('#'+ids).remove();
  }
  }
  
  function savenewinfo(){
	  var str1=$("#typeno").val();
	  if(null==str1||''==str1){
		  alert('配置项编码不允许为空');
		   return ; 
	  }
	  var str2=$("#typename").val();  
	  if(null==str2||''==str2){
		  alert('配置项名称不允许为空');
		   return ; 
	  }
	  var str3=$("#itemno").val();  
	  if(null==str3||''==str3){
		  alert('配置项值代码不允许为空');
		   return ; 
	  }
	  var str4=$("#value").val();  
	  if(null==str4||''==str4){
		  alert('配置项值不允许为空');
		   return ; 
	  }
	  var str6=$("#flag").val();  
	  if(null==str6||''==str6){
		  alert('是否禁用不允许为空');
		   return ; 
	  }
	  if(!('是'==str6||'否'==str6)){
		  alert('是否禁用的值仅能为是或者否');
		   return ; 
	  }
	  var str5=$("#note").val();
	  $.ajax({
		  url:"/bankinfo/updateSystemConfig.do",
		  type:"post",
		  dataType:"text",
		 // data:"typeno="+str1+"&typename="+str2+"&itemno="+str3+"&value="+str4+"&note="+str5+"&flag="+str6,
		    data:{"typeno":str1,"typename":str2,"itemno":str3,"value":str4,"note":str5,"flag":str6}, 
		 async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  alert(strs[0]);
			  var merid=$.trim('ids'+strs[1]);
			  	 var s1="<tr bgcolor=\"#e4f2ff\" id='"+merid+"'><td align='center' width='80px' >"+str1+"</td>";
				 var s2="<td align='center' width='80px' >"+str2+"</td>";
				 var s3="<td align='center' width='80px' >"+str3+"</td>";
				 var s4="<td align='center' width='80px' >"+str4+"</td>";
				 var s7="<td align='center' width='80px' >"+str6+"</td>";
				 var s5="<td align='center' width='80px' >"+str5+"</td>";
				 var s6="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editinfo('"+merid+"','"+str1+"','"+str2+"','"+str3+"','"+str4+"','"+str5+"','"+str6+"')\" /> <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+merid+"')\" /></td></tr>";
				$("#table").append(s1+s2+s3+s4+s7+s5+s6); 
			  
		  }
	  });
  }
</script>
</head>
<body>
<p>
账户 --&gt; 系统参数配置 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>

</br>
</br>
<div id="excel_menu" style="position: absolute; top: 45px; left: 0px;">
<s:form  theme="simple">
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="1300px">
								<tr align="left">
									<td>
										配置项编码:
										<s:textfield id="typeno" size="25" />
									</td>
									<td>
										配置项名称:
										<s:textfield id="typename" size="25" />
									</td>
									<td>
										配置值代码:
										<s:textfield id="itemno" size="25" />
									</td>
									<td>
										配置值:
										<s:textfield id="value" size="25" />
									</td>
									<td>
										是否禁用(是/否):
										<s:textfield id="flag" size="15" value="否" />
									</td>
									<td>
										备注:
										<s:textfield id="note" size="30" />
									</td>
									<td>
										<input type="button" value="新增"
			  onclick="savenewinfo();" />
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
		  <table width="1300px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" id='table' >
            <tr bgcolor="#0084ff">
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;">配置项编码</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">配置项名称</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">配置值代码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;word-break:break-all;">配置值</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">是否禁用(是/否)</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <s:set var="id" value="#fc.id" scope="request" />
            <s:set var="typeno" value="#fc.typeNo" scope="request" />
            <s:set var="typename" value="#fc.typeName" scope="request" />
             <s:set var="flag" value="#fc.flag" scope="request" />
            <s:set var="itemno" value="#fc.itemNo" scope="request" />
            <s:set var="value" value="#fc.value" scope="request" />
            <s:set var="note" value="#fc.note" scope="request" />
            <tr bgcolor="#e4f2ff" id='ids${id}'>
              <td align="center" width="80px" ><s:property value="#fc.typeNo"/></td>
              <td align="center" width="80px" ><s:property value="#fc.typeName"/></td>
              <td align="center" width="80px" ><s:property value="#fc.itemNo"/></td>
              <td align="center" width="80px" style="word-break:break-all"><s:property value="#fc.value"/></td>
              <td align="center" width="80px" ><s:property value="#fc.flag"/></td>
              <td align="center" width="80px" ><s:property value="#fc.note"/></td>
              <td align="center" width="35px">
			  <input type="button" value="编辑"
			  onclick="editinfo('ids${id}','${typeno}','${typename}','${itemno}','${value}','${note}','${flag}');" />
			   <input type="button" value="删除"
			  onclick="deleteinfo('ids${id}');" />
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

