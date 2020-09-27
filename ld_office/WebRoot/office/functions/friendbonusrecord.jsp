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
    <title>好友推荐金</title>
    
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
<s:form action="queryFriendRecordsByDate" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 好友推荐金<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<h3 style="display: inline;">派发日期:</h3>
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance' || #session.operator.authority=='sale_manager'" >
	<s:textfield id="distributeDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
	<input type="button" id="distributeBtn" onclick="calculateFriendMoney()" value="派发好友推荐金" style="margin-right:1px;color:red;font-weight:bold">
</s:if>	

上线玩家号:<s:textfield name="topLineUser" size="15" />
下线玩家号:<s:textfield name="downLineUser" size="15" />
派发日期: <s:textfield name="distributeDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'createtime'" />
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >上线玩家</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >下线玩家</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">备注</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('createtime');">创建时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('distributeDate');">派发日期</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('money');">反馈金额</td>
              
            </tr> 
            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.toplineuser"/>"><s:property value="#fc.toplineuser"/></a></td>
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.downlineuser"/>"><s:property value="#fc.downlineuser"/></a></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.distributeDate"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.money)"/></td>
	            </tr>
	            <s:set var="amountValue" value="#fc.money" scope="request"></s:set>
            	<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
	  	 	 </s:iterator>
	  	 	 <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="5">当页小计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td></td>
             </tr>
  	 	     <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="5">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"><s:property value="%{#request.page.statics1}"/></td></td>
             </tr>
             <tr>
              <td colspan="13" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
	function calculateFriendMoney(type){
	    $("#distributeBtn").attr("disabled", true);
	    var distributeDate = $("#distributeDate").val();
	    if(distributeDate == null || distributeDate == ''){
	    	alert("派发日期不能为空");
	    	$("#distributeBtn").attr("disabled", false);
	    	return;
	    }
	    
	    if(!confirm("您确定要派发" + distributeDate + "日期的好友推荐金吗？")){
	    	$("#distributeBtn").attr("disabled", false);
	    	return;
	    }
		$.ajax({ 
	          type: "post", 
	          url: "/office/calculateFriendMoney.do", 
	          cache: false, 
	          async: false,
	          data:{
	        	  "distributeDate" : distributeDate
	          },
	          timeout:600000, 
	          
	          success : function(data){console.log(data);alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){$("#distributeBtn").attr("disabled", false);}
        });
	}
	
	
	
</script>
</body>
</html>