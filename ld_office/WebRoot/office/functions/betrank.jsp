<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>bank online transfer</title>
    
	<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="./js/jquery-1.7.2.min.js"></script>

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

function excute(id,amount,note,loginname){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open('http://'+window.location.host+'/office/functions/updatebetrank.jsp?id='+id+'&amount='+amount+'&loginname='+loginname+'&note='+note+'&r='+Math.random(),'','height=250, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}
function updateInfo(){
	var yesterday =$("#yesterday").val();
	  $.ajax({
		  url:"/office/updateSumBet.do",
		  type:"post",
		  dataType:"text",
		  data:{"yesterday":yesterday},
		  async:false,
		  success : function(msg){
			  alert(msg);
		  }
	  });
}

function excute(id,amount,note,loginname){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open('http://'+window.location.host+'/office/functions/updatebetrank.jsp?id='+id+'&amount='+amount+'&loginname='+loginname+'&note='+note+'&r='+Math.random(),'','height=250, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}
</script>
<s:form action="queryBetrank2" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
记录 --> 老虎机排名 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
活动开始时间：<s:textfield name="startTime" id="startTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{startTime}" cssClass="Wdate" />
活动结束时间：<s:textfield name="endTime" id="endTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="true" value="%{endTime}" cssClass="Wdate" />
玩家账号:<s:textfield name="loginname" size="10"></s:textfield>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<%--老虎机类别:<s:select name="type" list="#{'ttg':'ttg','pttiger':'pttiger','gpi':'gpi','all':'TTG/GPI/PT','updatedate':'数据修改'}" listKey="key" listValue="value"/>--%>
<s:submit value="查询"></s:submit>
<s:hidden name="pageIndex" value="1"></s:hidden>
<s:set name="by" value="'payDate'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
</div>

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
             <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('no');">排名</td> 
             <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginname');">玩家账号</td>
             <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('amount');">金额</td>
             <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('address');">地区</td>
            <%-- <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">老虎机类别</td>--%>
             <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">操作</td>
            </tr>
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="${bgcolor}">
            <td  align="center"  style="font-size:13px;"><s:property value="#fc.no"/></td>
            <td  align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/></td>
            <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
            <td  align="center"  style="font-size:13px;"><s:property value="#fc.address"/></td>
          <%--  <td  align="center"  style="font-size:13px;"><s:property value="#fc.platform"/></td>--%>
            <td  align="center"  style="font-size:13px;">
             <s:if test='#fc.no==0 || #fc.no==""'> 
              (<a onclick="excute('<s:property value="#fc.id"/>','<s:property value="#fc.amount"/>','<s:property value="#fc.note"/>','<s:property value="#fc.loginname"/>' )" style="cursor:pointer" title="修改">修改</a>)
              </s:if>
              <s:else>
              </s:else>
            </td>
            </tr>
  	 	 </s:iterator>
            <tr>
              <td colspan="10" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
</body>
</html>
