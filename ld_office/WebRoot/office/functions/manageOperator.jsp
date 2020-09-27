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


// 变为编辑页面
function editinfo(id,username,authority,loginTimes,createTime,lastLoginTime,lastLoginIp,phonenoGX,phonenoBL,cs,type,partner,agent,blServerUrl,employeeNo){
	if(authority=='boss'){
		authority='boss'; 
	}else if(authority=='boss'){
		authority='boss'; 
	}else if(authority=='admin'){
		authority='admin'; 
	}else if(authority=='sale_manager'){
		authority='客服管理员'; 
	}else if(authority=='sale'){
		authority='客服专员'; 
	}else if(authority=='market_manager'){
		authority='市场管理员'; 
	}else if(authority=='market'){
		authority='市场专员'; 
	}else if(authority=='finance_manager'){
		authority='财务管理员'; 
	}else if(authority=='finance'){
		authority='财务专员'; 
	}else if(authority=='om'){
		authority='运维工程师';
	}
	var id1=id+"username";
	var id2=id+"authority";
	var id3=id+"loginTimes";
	var id4=id+"createTime";
	var id5=id+"lastLoginTime";
	var id6=id+"lastLoginIp";
	var id7=id+"phonenoGX";
	var id8=id+"phonenoBL";
	var id9=id+"cs";
	var id91=id+"type";
	var id92=id+"partner";
	var id93=id+"agent";
	var id94 = id+"blServerUrl";
	var id95 = id+"employeeNo";
	var str ='#'+id;
	$(str+' td').remove();
	 var str1="<td align='center' width='80px' ><div id='"+id1+"'>"+username+"</div></td>";
	 var str2="<td align='center' width='80px' ><div id='"+id2+"'>"+authority+"</div></td>";
	 var str3="<td align='center' width='80px' ><div id='"+id3+"'>"+loginTimes+"</div></td>";
	 var str4="<td align='center' width='80px' ><div id='"+id4+"'>"+createTime+"</div></td>";
	 var str5="<td align='center' width='80px' ><div id='"+id5+"'>"+lastLoginTime+"</div></td>";
	 var str6="<td align='center' width='80px' ><div id='"+id6+"'>"+lastLoginIp+"</div></td>";
	 var str7="<td align='center' width='80px' ><textarea id='"+id7+"'  style=\"height: 17px;width: 80px\" >"+phonenoGX+"</textarea></td>";
	 var str8="<td align='center' width='80px' ><textarea id='"+id8+"'  style=\"height: 17px;width: 80px\" >"+phonenoBL+"</textarea></td>";
	 var str9="<td align='center' width='80px' ><textarea id='"+id9+"'  style=\"height: 17px;width: 80px\" >"+cs+"</textarea></td>";
	 var str91="<td align='center' width='80px' ><textarea id='"+id91+"'  style=\"height: 17px;width: 80px\" >"+type+"</textarea></td>";
	 var str92="<td align='center' width='80px' ><textarea id='"+id92+"'  style=\"height: 17px;width: 80px\" >"+partner+"</textarea></td>";
	 var str93="<td align='center' width='80px' ><textarea id='"+id93+"'  style=\"height: 17px;width: 80px\" >"+agent+"</textarea></td>";
	 var str94="<td align='center' width='80px' ><textarea id='"+id94+"'  style=\"height: 17px;width: 80px\" >"+blServerUrl+"</textarea></td>";
	 var str95="<td align='center' width='80px' ><textarea id='"+id95+"'  style=\"height: 17px;width: 80px\" >"+employeeNo+"</textarea></td>";
	 var str10="<td align='center'><input type=\"button\"  value=\"保存\"  onclick=\"saveInfo('"+id+"');\" />  <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+id+"');\" /></td>";
	 $(str).append(str1+str2+str3+str4+str5+str6+str7+str8+str9+str91+str92+str93+str94+str95+str10);
	}

//保存编辑后的数据
  function saveInfo(id){
	  var str ='#'+id;
	  var username=$("#"+id+"username").html();
	  var authority=$("#"+id+"authority").html();
	  var loginTimes=$("#"+id+"loginTimes").html();
	  var createTime=$("#"+id+"createTime").html();
	  var lastLoginTime=$("#"+id+"lastLoginTime").html();
	  var lastLoginIp=$("#"+id+"lastLoginIp").html();
	  var phonenoGX=$("#"+id+"phonenoGX").val();
	  var phonenoBL=$("#"+id+"phonenoBL").val();
	  var cs=$("#"+id+"cs").val();
	  var type=$("#"+id+"type").val();
	  var partner=$("#"+id+"partner").val();
	  var agent=$("#"+id+"agent").val();
	  var blServerUrl=$("#"+id+"blServerUrl").val().replace(/\n/g,'');
	  var employeeNo=$("#"+id+"employeeNo").val();
	  $.ajax({
		  url:"/office/updateOrSaveOperator.do",
		  type:"post",
		  dataType:"text",
		  data:"phonenoGX="+phonenoGX+"&phonenoBL="+phonenoBL+"&cs="+cs+"&username="+username+"&type="+type+"&partner="+partner+"&agent="+agent+"&blServerUrl="+blServerUrl+"&employeeNo="+employeeNo,
		  async:false,
		  success : function(msg){
			  var strs=msg.split('##');
			  alert(strs[0]);
			  $(str+' td').remove();
			  	 var s1="<td align='center' width='80px' >"+username+"</td>";
				 var s2="<td align='center' width='80px' >"+authority+"</td>";
				 var s3="<td align='center' width='80px' >"+loginTimes+"</td>";
				 var s4="<td align='center' width='80px' >"+createTime+"</td>";
				 var s5="<td align='center' width='80px' >"+lastLoginTime+"</td>";
				 var s6="<td align='center' width='80px' >"+lastLoginIp+"</td>";
				 var s7="<td align='center' width='80px' >"+phonenoGX+"</td>";
				 var s8="<td align='center' width='80px' >"+phonenoBL+"</td>";
				 var s9="<td align='center' width='80px' >"+cs+"</td>";
				 var s91="<td align='center' width='80px' >"+type+"</td>";
				 var s92="<td align='center' width='80px' >"+partner+"</td>";
				 var s93="<td align='center' width='80px' >"+agent+"</td>";
				 var s94="<td align='center' width='80px' >"+blServerUrl+"</td>";
				 var s95="<td align='center' width='80px' >"+employeeNo+"</td>";
				 var s10="<td align='center'><input type=\"button\"  value=\"编辑\"  onclick=\"editinfo('"+id+"','"+username+"','"+authority+"','"+loginTimes+"','"+createTime+"','"+lastLoginTime+"','"+lastLoginIp+"','"+phonenoGX+"','"+phonenoBL+"','"+cs+"','"+type+"','"+partner+"','"+agent+"','"+blServerUrl+"','"+employeeNo+"')\" /> <input type=\"button\" value=\"删除\"  onclick=\"deleteinfo('"+id+"');\" /></td>";
				 $(str).append(s1+s2+s3+s4+s5+s6+s7+s8+s9+s91+s92+s93+s94+s95+s10); 
			  
		  }
	  });
  }
  
  //删除数据
  function deleteinfo(ids){
	  var choice=confirm("您确认要删除吗？", function() { }, null);
	 if(choice){
	  $.ajax({
		  url:"/office/deleteOperator.do",
		  type:"post",
		  dataType:"text",
		  data:"username="+ids,
		  async:false,
		  success : function(msg){
			  if(msg==1){
				  alert('boss或admin权限账号，请联系系统管理员删除！');
				  return ;
			  }
			  alert(msg);
			  $('#'+ids).remove();
			  $('#ids'+ids).remove();
		  }
		  
	  })
  }
  }
  
  
  function checkInfo(){
	  var username=$('#username').val();
	  var authority=$('#authority').val();
	  $.ajax({
		  url:"/office/queryOperators.do",
		  type:"post",
		  dataType:"text",
		  data:"username="+username+"&authority="+authority,
		  async:false,
		  success : function(msg){
		  }
		  
	  })
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
							<table border="0" cellpadding="0" cellspacing="0" width="600px">
								<tr align="left">
									<td>
										员工账号:
										<s:textfield id="username" name='username' size="15" />
									</td>
									<td>
									<p align="left">
						部门:&nbsp;
						<s:select cssStyle="width:150px"
							list="#{'':'全部','boss':'boss','admin':'admin','sale_manager':'客服管理员','sale':'客服专员','market_manager':'市场管理员','market':'市场专员','finance_manager':'财务管理员','finance':'财务专员','om':'运维工程师'}"
							key="key" value="value" name="authority" id='authority'></s:select>
					</p>
									</td>
									<td>
										<button onclick="checkInfo()">查询</button>
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
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">员工账号</td>
              <td  align="center" width="200px" style="color: #FFFFFF;font-weight: bold;">部门</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">登录次数</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">创建时间</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">最后登录时间</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">最后登录IP</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">国信坐席号码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">比邻坐席号码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">客服维护代码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">电销代码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">代理代码</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">代理账号</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">比邻IP</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">员工编号</td>
              <td  align="center" width="160px" style="color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="#e4f2ff" id='ids${username}'>
              <td align="center" width="80px" ><s:property value="#fc.username"/></td>
              <td align="center" width="80px" >
              <s:if test="#fc.authority==\"boss\"">
              boss
              </s:if>
               <s:if test="#fc.authority==\"admin\"">
              admin
              </s:if>
               <s:if test="#fc.authority==\"sale_manager\"">
              	客服管理员
              </s:if>
               <s:if test="#fc.authority==\"sale\"">
              	客服专员
              </s:if>
              <s:if test="#fc.authority==\"market_manager\"">
              	市场管理员
              </s:if>
                <s:if test="#fc.authority==\"market\"">
              	市场专员
              </s:if>
               <s:if test="#fc.authority==\"finance_manager\"">
              	财务管理员
              </s:if>
              <s:if test="#fc.authority==\"finance\"">
              	财务专员
              </s:if>
				  <s:if test="#fc.authority==\"om\"">
					  运维工程师
				  </s:if>
              </td>
              <td align="center" width="80px" ><s:property value="#fc.loginTimes"/></td>
              <td align="center" width="80px" ><s:property value="#fc.createTime"/></td>
              <td align="center" width="80px" ><s:property value="#fc.lastLoginTime"/></td>
              <td align="center" width="80px" ><s:property value="#fc.lastLoginIp"/></td>
              <td align="center" width="80px" ><s:property value="#fc.phonenoGX"/></td>
              <td align="center" width="80px" ><s:property value="#fc.phonenoBL"/></td>
              <td align="center" width="80px" ><s:property value="#fc.cs"/></td>
              <td align="center" width="80px" ><s:property value="#fc.type"/></td>
              <td align="center" width="80px" ><s:property value="#fc.partner"/></td>
              <td align="center" width="80px" ><s:property value="#fc.agent"/></td>
              <td align="center" width="80px" ><s:property value="#fc.blServerUrl"/></td>
              <td align="center" width="80px" ><s:property value="#fc.employeeNo"/></td>
              <td align="center" width="35px">
			  <input type="button" value="编辑"
			  onclick="editinfo('ids${username}','${username}','${authority}','${loginTimes}','${createTime}','${lastLoginTime}','${lastLoginIp}','${phonenoGX}','${phonenoBL}','${cs}','${type}','${partner}','${agent}','${blServerUrl}','${employeeNo}');" />
			   <input type="button" value="删除"
			  onclick="deleteinfo('${username}');" />
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

