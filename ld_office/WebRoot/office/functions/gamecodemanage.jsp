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
<title>游戏管理</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript">
function check(){
	var gameplatform = document.getElementById('gameplatformSelect').value;
	if(gameplatform=='') {
		alert('请选择类型');
		return false;
	}
}
function deleteGame(id){
	if(confirm("你确认要执行此操作么？")){
		 var xmlhttp = new Ajax.Request(    
				"/office/deleteGame.do",
		        {    
		            method: 'post',
		            parameters:"id="+id+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}
}

function responseMethod(data){
	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}
</script>
</head>
<body>
<p>
操作 --&gt; 游戏管理 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>

<s:form action="queryGameByPlatform" namespace="/office" name="mainform" id="mainform" theme="simple" onsubmit="return check();">
<div id="excel_menu" style="text-align:center;">

类型:<s:select list="%{#application.GamePlatform}" listKey="code" listValue="text" emptyOption="true" name="type" id="gameplatformSelect"></s:select>					
游戏编码:<s:textfield name="code" size="32" />
游戏名:<s:textfield name="gamename" size="32" />
<s:submit value="查询"></s:submit>
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
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >编码</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >类型</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >游戏名</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold;">操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.list}">
            <tr bgcolor="${bgcolor}">
              <td align="center" style="font-size:13px;"><s:property value="#fc.code"/></td>
              <td align="center" style="font-size:13px;">
				  <s:property value="@dfh.model.enums.GamePlatform@getText(#fc.type)"/>
			  </td>
			  <td align="center" style="font-size:13px;"><s:property value="#fc.chineseName"/></td>
			  <td align="center" style="font-size:13px;"><s:property value="#fc.remark"/></td> 	 	
           	  <td align="center" style="font-size:13px;">
           	  	  <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'">
           	  	  	<input type="button" value="删除" onclick="deleteGame(<s:property value="#fc.id"/>)"/>
           	  	  </s:if>
           	  </td>
            </tr>
  	 	 </s:iterator>
            <tr>
              <td colspan="11" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
				共  ${fn:length(list)} 条     
              </td>
            </tr>
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
</s:form>
</body>
</html>

