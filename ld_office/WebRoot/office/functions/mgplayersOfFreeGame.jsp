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
    
    <title>MG免费游戏玩家</title>
    
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

<div id="excel_menu" style="text-align:center;">
<div style="text-align: center;color: red;margin-top: 20px;margin-bottom: 20px;font-weight: bold;">
	注：时间均为MG平台所处时区时间
</div>
<s:form action="getPlayersByFreegame" namespace="/mgs" name="mainform" id="mainform" theme="simple">
	游戏:<s:textfield name="description" readonly="true"/>
	每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
	<s:submit value="查询"></s:submit>
	<s:hidden name="pageIndex"></s:hidden>
	<s:hidden name="freeGameID" id="freeGameID"></s:hidden>
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
	              <td bgcolor="#0084ff" align="center" style="display: none;" >账号</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >账号</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >奖励旋转次数</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">剩余旋转次数</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">起始时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">失效时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">派发时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">赢得</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">操作</td>
	            </tr>
	            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="display: none;"><s:property value="#fc.instanceId"/></td>
	              <td align="center"  style="font-size:13px;"><s:property value="#fc.loginName"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.gamesAwarded"/></td>
				  <td  align="center"  style="font-size:13px;"><s:property value="#fc.gamesLeft"/></td> 
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startDate"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endDate"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.dateAwarded"/></td>
	              <td align="center"  style="font-size:13px;"><s:property value="#fc.totalWinnings"/></td>
	              <td align="center"  style="font-size:13px;">
					  <input type="button" value="移除免费游戏优惠" onclick="removePlayersFromFreegame(this, '${fc.loginName}', ${fc.instanceId})" />
				  </td>
	            </tr>
	  	 	 </s:iterator>
	            <tr>
	              <td colspan="8" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
	function removePlayersFromFreegame(btn, loginName, instanceId){
	    var freeGameID = $('#freeGameID').val();
		btn.disabled=true;
		$.ajax({ 
	          type: "post", 
	          url: "/mgs/removePlayersFromFreegame.do", 
	          data: {"freeGameID":freeGameID, "players":loginName, "instanceId":instanceId},
	          cache: false,  
	          success: function(data){alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){btn.disabled=false;}
        });
	}
</script>
</body>
</html>