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
    <title>手机连续签到奖品几率管理</title>
    
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
<s:form action="queryLotteryPrizes" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 手机连续签到奖品几率管理<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">

每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>

<input type="button" value="查询" id="queryButton" onclick="disableBut();">
<s:hidden name="pageIndex"></s:hidden>
<s:set name="by" value="'ranking'" />
<s:set name="order" value="'asc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
<br/><br/><span>目前奖项几率总和:<font id="note"></font></span>
</div>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('loginname');" >编号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('ranking');">奖项</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">获奖几率</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('bettotal');">提交</td>
            </tr> 
            <s:iterator var="fc" value="%{#request.page.pageContents}" status="listStatus">
	            <tr bgcolor="${bgcolor}">
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.type"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.itemName"/></td>
	              <td  align="center"  style="font-size:13px;"><input type="text" class="allPercentColumns" id="percent<s:property value="#listStatus.index"/>" value="<s:property value="#fc.percent"/>"/>%</td>
	              <td  align="center"  style="font-size:13px;">
		              <input type="button" value="修改" id="submitButton<s:property value="#listStatus.index"/>" onclick="updatePrize('<s:property value="#fc.id"/>','<s:property value="#listStatus.index"/>');">
	              </td>
	            </tr>
	  	 	 </s:iterator>
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
	function disableBut(){
		window.mainform.submit();
		$("#queryButton").attr("disabled","true");
		
	}
	function updatePrize(id,index){
		var percentNum = $("#percent"+index).val();
		if(!checkPercentNum(percentNum)){
			alert("限制输入小数后1位 如:10.0,10.2,0.8");
			return;
		};
		$.post("/office/updatePrize.do",{prizeId:id,prizePercent:percentNum},function(result){
		  alert(result);
		});
	}
	
	function checkPercentNum(percentNum){
		var rex=/\d+[.]\d{1}$/g;
		if(rex.test(percentNum)) return true;
		else return false;
	}
	
	function onPercentChange(){
		var totalPercent = 0;
		$(".allPercentColumns").each(function() {
			totalPercent+=parseFloat($(this).val());
		});
		totalPercent = totalPercent.toFixed(4);
		if(totalPercent>100){
			$("#note").html(totalPercent+"%(已超过100%)");
			$("#note").css("color","red");
		}else{
			$("#note").html(totalPercent+"%");
			$("#note").css("color","black");
		}
	}
	
	$(".allPercentColumns").keyup(function(){
		 onPercentChange()
    });
</script>
</body>
</html>