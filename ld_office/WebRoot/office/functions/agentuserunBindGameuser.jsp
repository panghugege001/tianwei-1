<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>代理账号解绑游戏账号</title>
    
	<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>

  </head>
  
  <body>
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
</script>
<s:form action="queryAgentBindGameUsersRecords.do" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 代理账号解绑游戏账号<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">

状态:<s:select name="deleteFlag" list="#{'':'全部','0':'未解绑','1':'已解绑'}" listKey="key" listValue="value"/>
代理帐号:<s:textfield name="agentUserName" size="15" />
游戏帐号:<s:textfield name="gameUserName" size="15" />
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'createTime'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>

</div>
<br/>
<br/>
<br/>
<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" onclick="orderby('loginnameAgent');">代理账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" onclick="orderby('loginnameGame');">游戏账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">创建时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">备注</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" >是否解绑</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >操作</td>
            </tr> 
            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.loginnameAgent"/>"><s:property value="#fc.loginnameAgent"/></a></td>
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.loginnameGame"/>"><s:property value="#fc.loginnameGame"/></a></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
	              <td  align="center"  style="font-size:13px;">
					 <s:if test="#fc.deleteFlag==1">已解绑</s:if>
					 <s:elseif test="#fc.deleteFlag==0">已绑定</s:elseif>
	              </td>
	              <td  align="center"  style="font-size:13px;">
	              	   <s:if test="#fc.deleteFlag==0">
	              	   	   <input type="button" id="unbindBtn" onclick="unbindAgent('<s:property value="#fc.loginnameAgent"/>','<s:property value="#fc.id"/>')" value="解除绑定">
	              	   </s:if>
	              	    <s:if test="#fc.deleteFlag==1">
	              	   	   <input type="button" id="disabledBtn" disabled="disabled" value="不能操作">
	              	   </s:if>
	              </td>
	            </tr>
	  	 	 </s:iterator>
             <tr>
              <td colspan="6" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
				${page.jsPageCode} 
              </td>
            </tr>
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
</s:form>
<c:import url="/office/script.jsp" />
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">

	function unbindAgent(agentUserName,id){
		if(!confirm("您确定要将代理账号：" + agentUserName + "进行解绑操作？")){
			return;
		}
		$("#unbindBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/unbindAgentGameUsers.do", 
	          cache: false,  
	          async: false,
	          data:{"id":id},
	          success : function(data){
	        	  if(data == "操作成功"){
	        		  alert("操作成功");
	        	  } else {
	        		  alert(data);
	        	  }
	        	  var frm=document.getElementById("mainform");
 				  frm.submit();
	          },
	          error: function(){alert("系统错误");},
			  complete: function(){$("#removeBetLimitBtn").attr("disabled", false);}
        });
	}
	
</script>
</body>
</html>