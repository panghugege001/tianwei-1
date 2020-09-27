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
    <base href="<%=basePath%>">
    
    <title>老虎机周周回馈</title>
    
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
<s:form action="queryWeekSentRecords" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 周周回馈 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance'">
	<!-- <input type="button" id="excWeekSentBtn" onclick="excWeekSent()" value="派发周周回馈" style="margin-right:180px;color:red;font-weight:bold"> -->
</s:if>	
状态:<s:select name="status" list="#{'':'全部','0':'未领取','1':'已领取','2':'已处理','3':'已取消'}" listKey="key" listValue="value"/>
会员帐号:<s:textfield name="loginname" size="15" />
推荐码:<s:textfield name="intro" size="10" />
开始时间: <s:textfield name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}"  />
结束时间:<s:textfield name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'creattime'" />
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >推荐码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >上周日期</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('amount');">上周投注额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('promo');">赠送金额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">平台</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('creattime');">派发时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('gettime');">领取时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="${bgcolor}">
              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc[1]"/>"><s:property value="#fc[1]"/></a></td>
              <td align="center"  style="font-size:13px;"><s:property value="#fc[2]"/></td>
              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd" name="#fc[3]"/>|<s:date format="yyyy-MM-dd" name="#fc[4]"/></td>
              <td align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[5])"/></td>
              <td align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[6])"/></td>
              <td align="center"  style="font-size:13px;">
				 <s:if test="#fc[7]=='0'">未领取</s:if>
				 <s:elseif test="#fc[7]=='1'">已领取</s:elseif>
				 <s:elseif test="#fc[7]=='2'">已处理</s:elseif>
				 <s:elseif test="#fc[7]=='3'">已取消</s:elseif>
              </td>
              <td align="center"  style="font-size:13px;"><s:property value="#fc[8]"/>   </td> 
              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[9]"/>   </td>
              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[10]"/>   </td>
              <td align="center"  style="font-size:13px;"><s:property value="#fc[11]"/>   </td> 
              <td align="center"  style="font-size:13px;">
              	   <s:if test="#fc[7]=='1'">
              	   	   <input type="button" class="removeBetLimitBtn" onclick="removeBetLimit('<s:property value="#fc[0]"/>')" value="解除流水限制">
              	   </s:if>
              	   <s:if test="#fc[7]=='0'">
              	   	   <input type="button" class="cancelWeekSentBtn" onclick="cancelWeekSent('<s:property value="#fc[0]"/>')" value="取消周周回馈">
              	   </s:if>
              </td>    		
            </tr>
  	 	 </s:iterator>
            <tr>
              <td colspan="11" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
	function excWeekSent(){
		if (confirm("是否确认派发周周回馈？")){
			$("#excWeekSentBtn").attr("disabled", true);
			$.ajax({
		          type: "post",
		          url: "/office/excWeekSent.do",
		          cache: false,
		          async: false,
		          timeout:600000,
		          success : function(data){alert(data);},
		          error: function(){alert("系统错误");},
				  complete: function(){$("#excWeekSentBtn").attr("disabled", false);}
	        });
		}
	}
	function removeBetLimit(pno){
		if (confirm("是否确认解除限制？")){
			$(".removeBetLimitBtn").attr("disabled", true);
			$.ajax({ 
		          type: "post", 
		          url: "/office/removeWeekSentTransferLimit.do", 
		          cache: false,
		          async: false,
		          data:{"pno":pno},
		          success : function(data){
		          	 alert("操作成功");
		          	 var frm=document.getElementById("mainform");
					 frm.submit();
		          },
		          error: function(){alert("系统错误");},
				  complete: function(){$(".removeBetLimitBtn").attr("disabled", false);}
	        });
		}
	}
	function cancelWeekSent(pno){
		if (confirm("是否确认取消？")){
			$(".cancelWeekSentBtn").attr("disabled", true);
			$.ajax({ 
		          type: "post", 
		          url: "/office/cancelWeekSent.do", 
		          cache: false,
		          async: false,
		          data:{"pno":pno},
		          success : function(data){
		          	 alert("操作成功");
		          	 var frm=document.getElementById("mainform");
					 frm.submit();
		          },
		          error: function(){alert("系统错误");},
				  complete: function(){$(".cancelWeekSentBtn").attr("disabled", false);}
	        });
		}
	}
</script>
</body>
</html>