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
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">

// 变为编辑页面
function editinfo(id,title,type,minvalue,disable,remark){
	var id1=id+"title";
	var id5=id+"type";
	var id2=id+"minvalue";
	var id3=id+"disable";
	var id4=id+"remark";
	var str ='#'+id;
	$(str+' td').remove();
	 var str1="<td align='center' width='80px' ><textarea id='"+id1+"' style=\"height: 20px;width: 170px\" disabled>"+title+"</textarea></td>";
	 var str5="<td align='center' width='80px' ><textarea id='"+id5+"' style=\"height: 20px;width: 170px\" disabled>"+type+"</textarea></td>";
	 var str2="<td align='center' width='80px' ><textarea id='"+id2+"'  style=\"height: 20px;width: 170px\" >"+minvalue+"</textarea></td>";
	 var str3="<td align='center' width='80px' ><textarea id='"+id3+"'  style=\"height: 20px;width: 170px\" >"+disable+"</textarea></td>";
	 var str4="<td align='center' width='80px' ><textarea id='"+id4+"'  style=\"height: 20px;width: 170px\" >"+remark+"</textarea></td>";
	 var str6="<td align='center'><input type=\"button\"  value=\"保存\"  onclick=\"saveInfo('"+id+"');\" /></td>";
	$(str).append(str1+str5+str2+str3+str4+str6);
	}

//保存编辑后的数据
  function saveInfo(id){
	  var str ='#'+id;
	  var str1=$("#"+id+"title").val();
	  if(null==str1||''==str1){
		  alert('短信开关名称不允许为空');
		   return ; 
	  }
	  
	  var str2=$("#"+id+"minvalue").val();
	  if(str2 != '' && isNaN(str2)){
		  alert('触发值必须是数字');
		  return;
	  }
	  var reg = /^[1-9]\d*$/;
	  if(str2 != '' && !reg.test(str2)){
		  alert("触发值必须是整数！");
		  return;
	  }
	  var str3=$("#"+id+"disable").val();
	  if(null==str3||''==str3){
		  alert('是否禁用不允许为空');
		   return ; 
	  }
	  if(!(str3=='是'||str3=='否')){
		  alert('是否禁用的值仅允许填写是或者否');  
		  return ; 
	  }
	  
	  var str4=$("#"+id+"remark").val();
	  var str5=$("#"+id+"type").val();
	  if(null==str5||''==str5){
		  alert('类型不允许为空');
		  return ; 
	  }
	  $.ajax({
		  url:"/office/saveOrUpdatSMSConfig.do",
		  type:"post",
		  data:{"title":str1,"type":str5,"minvalue":str2,"disable":str3,"remark":str4,"id":id.replace("_","")},
		  async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  if('success' == strs[0]){
				alert('操作成功');
				$(str+' td').remove();
				var s1="<td align='center' width='80px' >"+str1+"</td>";
				var s5="<td align='center' width='80px' >"+str5+"</td>";
				var s2="<td align='center' width='80px' >"+str2+"</td>";
				var s3="<td align='center' width='80px' >"+str3+"</td>";
				var s4="<td align='center' width='80px' >"+str4+"</td>";
				var s7="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editinfo('"+id+"','"+str1+"','"+str5+"','"+str2+"','"+str3+"','"+str4+"')\" /> </td>";
				$(str).append(s1+s5+s2+s3+s4+s7); 
			  } else {
				alert('操作失败，请刷新页面重新操作');
			  }
			  
			  
		  }
	  });
  }
  
  //新增一条数据
  function savenewinfo(){
	  var str1=$("#title").val();
	  if(null==str1||''==str1){
		  alert('短信开关名称不允许为空');
		   return ; 
	  }
	  
	  var str2=$("#minvalue").val();
	  if(isNaN(str2)){
		  alert('触发值必须是数字');
		  return;
	  }
	  var reg = /^[1-9]\d*$/;
	  if(str2 != '' && !reg.test(str2)){
		  alert("触发值必须是整数！");
		  return;
	  }
	  var str3=$("#disable").val();
	  if(null==str3||''==str3){
		  alert('是否禁用不允许为空');
		   return ; 
	  }
	  if(!(str3=='是'||str3=='否')){
		  alert('是否禁用的值仅允许填写是或者否');  
		  return ; 
	  }
	  
	  var str4=$("#remark").val();
	  var str5=$("#type").val();
	  if(null==str5||''==str5){
		  alert('类型不允许为空');
		   return ; 
	  }
	  $.ajax({
		  url:"/office/saveOrUpdatSMSConfig.do",
		  type:"post",
		  dataType:"text",
		  data:"title="+str1+"&type="+str5+"&minvalue="+str2+"&disable="+str3+"&remark="+str4,
		  async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  if('success'==strs[0]){
				  alert("操作成功")
				  var merid=$.trim('_'+strs[1]);
			  	  var s1="<tr bgcolor=\"#e4f2ff\" id='"+merid+"'><td align='center' width='80px' >"+str1+"</td>";
			  	  var s5="<td align='center' width='80px' >"+str5+"</td>";
				  var s2="<td align='center' width='80px' >"+str2+"</td>";
				  var s3="<td align='center' width='80px' >"+str3+"</td>";
				  var s4="<td align='center' width='80px' >"+str4+"</td>";
				  var s6="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editinfo('"+merid+"','"+str1+"','"+str5+"','"+str2+"','"+str3+"','"+str4+"')\" /> </td></tr>";
				  $("#table").append(s1+s5+s2+s3+s4+s6); 
			  } else {
				  alert("操作异常，请刷新页面重新操作！")
			  }
			  
		  }
	  });
  }
</script>
</head>
<body>
<p>
账户 --&gt; 短信参数配置 <a href="javascript:history.back();"><font color="red">上一步</font></a>
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
										开关名称:
										<s:textfield id="title" size="25" />
									</td>
									<td>
										类型:
										<s:textfield id="type" size="25" />
									</td>
									<td>
										最小触发值:
										<s:textfield id="minvalue" size="25" />
									</td>
									<td>
										是否禁用(是/否):
										<s:textfield id="disable" size="15" value="否" />
									</td>
									<td>
										备注:
										<s:textfield id="remark" size="30" />
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
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">开关名称</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">类型</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">最小触发值</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">是否禁用(是/否)</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <s:set var="id" value="#fc.id" scope="request" />
            <s:set var="title" value="#fc.title" scope="request" />
            <s:set var="minvalue" value="#fc.minvalue" scope="request" />
            <s:set var="disable" value="#fc.disable" scope="request" />
            <s:set var="remark" value="#fc.remark" scope="request" />
            <tr bgcolor="#e4f2ff" id='_${id}'>
              <td align="center" width="80px" ><s:property value="#fc.title"/></td>
              <td align="center" width="80px" ><s:property value="#fc.type"/></td>
              <td align="center" width="80px" ><s:property value="#fc.minvalue"/></td>
              <td align="center" width="80px" ><s:property value="#fc.disable"/></td>
              <td align="center" width="80px" ><s:property value="#fc.remark"/></td>
              <td align="center" width="35px">
			  <input type="button" value="编辑"
			  onclick="editinfo('_${id}','${title}','${type}','${minvalue}','${disable}','${remark}');" />
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

