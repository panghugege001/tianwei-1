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
    
    <title>MG免费游戏</title>
    
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
function remind(){
	return confirm("Hey，确认要更新MG免费游戏吗？ 确定将删除本地数据库存储的MG免费游戏列表，重新从MG处获取免费游戏");
}
</script>

<div id="excel_menu_left">
操作 --> MG免费游戏 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<div style="margin-left:80px;font-weight:bold;float:left;">
<s:if test="#session.operator.authority=='boss'">
	&nbsp;&nbsp;&nbsp;<input type="button" value="更新MG免费游戏" style="color: red; font-weight: bold;" id="updateMGFreeGamesBtn" onclick="updateMGFreeGames()"/>
</s:if>
</div>
<s:form action="queryMGFreeGames" namespace="/mgs" name="mainform" id="mainform" theme="simple">
	游戏:<s:textfield name="description" size="32" />
	每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
	<s:submit value="查询"></s:submit>
	<s:hidden name="pageIndex"></s:hidden>
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
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >免费游戏优惠ID</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >游戏</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">金额</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">开始时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">结束时间</td>
	              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">操作</td>
	            </tr>
	            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><s:property value="#fc.id"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.description"/></td>
				  <td  align="center"  style="font-size:13px;"><s:property value="#fc.cost"/></td> 
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.startDate"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.endDate"/></td>
	              <td  align="center"  style="font-size:13px;">
	                 <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='sale_manager'">
					 	<input type="button" value="玩家列表" onclick="getPlayersByFreegame(${fc.id}, '${fc.description}')" />
					 	<input type="button" value="派发免费游戏" onclick="addPlayersToFreegame(this, ${fc.id})" />
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
	function updateMGFreeGames(){
		$("#updateMGFreeGamesBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/mgs/updateMGFreeGames.do", 
	          cache: false,  
	          success: function(data){alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){$("#updateMGFreeGamesBtn").attr("disabled", false);}
        });
	}

	function addPlayersToFreegame(btn, id){
	   btn.disabled=true;
	   var players=window.prompt("请填写玩家帐号，多个账号使用#分割","");
	   if($.trim(players)!=""){
		   $.ajax({ 
	           type: "post", 
	           url: "/mgs/addPlayersToFreegame.do", 
	           data: {"freeGameID":id, "players":players},
	           cache: false,
	           success: function(data){alert(data);},
	           error: function(){alert("服务异常");},
			   complete: function(){btn.disabled=false}
           });
		}	
	}
	
	function getPlayersByFreegame(id, description){
		var height = window.screen.height;
		var width =window.screen.width; 
		window.open ('<%=basePath%>/mgs/getPlayersByFreegame.do?freeGameID='+id+"&description="+description+"&pageIndex="+1+"&size="+20,'','height=800, width=1200,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top=100,left=200') ;
	}
</script>
</body>
</html>