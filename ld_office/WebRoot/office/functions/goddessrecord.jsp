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
    <title>守护女神</title>
    
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
<s:form action="queryGoddessRecords" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 守护女神<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance' || #session.operator.authority=='sale_manager'" >
	<input type="button" id="distributeFlowerBtn" onclick="calculateFlower()" value="派发鲜花" style="margin-right:1px;color:red;font-weight:bold">
	
	<input type="button" id="rankBtn" onclick="calculateRankingAndCoupon()" value="根据鲜花生成红包" style="margin-right:1px;color:red;font-weight:bold">
</s:if>	

玩家账号:<s:textfield name="loginname" size="15" />
守护女神名:<s:textfield name="goddessName" size="15" />
红包代码:<s:textfield name="couponnum" size="15" />
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查询" id="sub"></s:submit>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('loginname');" >玩家账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('goddessname');" >守护女神名</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">红包代码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('createtime');">创建时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('ranking');">排名</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('flowernum');">鲜花数</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">派发过的日期</td>
              <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance' || #session.operator.authority=='sale_manager'" >
              	<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('bettotal');">总投注额</td>
              	<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">更新总投注额</td>
              </s:if>
            </tr> 
            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.loginname"/>"><s:property value="#fc.loginname"/></a></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.goddessname"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.couponnum"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.ranking"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.flowernum"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.distributeMonth"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc.bettotal"/></td>
	              <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance' || #session.operator.authority=='sale_manager'" >
	              	<td  align="center"  style="font-size:13px;"><input type="text" id="bet_<s:property value="#fc.loginname"/>" ><input type="button" id="<s:property value="#fc.loginname"/>" value="修改" onclick="changeBettotal(this.id);"></td>
	              </s:if>
	            </tr>
	            <s:set var="amountValue" value="#fc.bettotal" scope="request"></s:set>
            	<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
	  	 	 </s:iterator>
	  	 	 <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="7">当页小计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ></td>
             </tr>
  	 	     <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="7">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ><s:property value="%{#request.page.statics1}"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" ></td>
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
<div id="ranking">
	<table>
		<tr>
           <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">女神名</td>
           <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">鲜花总数</td>
           <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">总投注额</td>
           <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">守护人数</td>
        </tr>
        <s:iterator var="god" value="%{#request.goddess}" status="index">
        	<tr bgcolor="${bgcolor}">
        		<td align="center"  style="font-size:13px;"><s:property value="#god[0]"/></td>
        		<td align="center"  style="font-size:13px;"><s:property value="#god[1]"/></td>
        		<td align="center"  style="font-size:13px;"><s:property value="#god[2]"/></td>
        		<td align="center"  style="font-size:13px;"><s:property value="#god[3]"/></td>
        	</tr>
        </s:iterator>
	</table>
</div>
</s:form>
<c:import url="/office/script.jsp" />
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	function calculateFlower(){
		
	    $("#distributeFlowerBtn").attr("disabled", true);
	    $("#rankBtn").attr("disabled", true);
	    
	    if(!confirm("您确定要派发昨日的鲜花吗？")){
	    	$("#distributeBtn").attr("disabled", false);
	    	$("#rankBtn").attr("disabled", false);
	    	return;
	    }
		$.ajax({ 
	          type: "post", 
	          url: "/office/calculateFlower.do", 
	          cache: false, 
	          async: false,
	          data:{
	          },
	          timeout:600000, 
	          
	          success : function(data){console.log(data);alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){
				  $("#distributeFlowerBtn").attr("disabled", false);
				  $("#rankBtn").attr("disabled", false);
			  }
        });
	}
	
	function calculateRankingAndCoupon(){
	    $("#rankBtn").attr("disabled", true);
	    $("#distributeFlowerBtn").attr("disabled", true);
	    if(!confirm("您确定要为守护女神活动进行最终排名以及派发红包吗？")){
	    	$("#rankBtn").attr("disabled", false);
	    	$("#distributeFlowerBtn").attr("disabled", false);
	    	return;
	    }
		$.ajax({ 
	          type: "post", 
	          url: "/office/calculateRankingAndCoupon.do", 
	          cache: false, 
	          async: false,
	          timeout:600000, 
	          
	          success : function(data){console.log(data);alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){
				  $("#rankBtn").attr("disabled", false);
			  	  $("#distributeFlowerBtn").attr("disabled", false);
			  }
        });
	}
	
	function changeBettotal(id){
		
		var betid = 'bet_' + id;
		var betnew = $("#" + betid).val();
		if($.trim(betnew) == ""){
			alert("更新数据不能为空");
			return;
		}
		if(isNaN(betnew)){
 		     alert("更改数据非有效数字！");
 		     return;
       }
  	   if(betnew < 0){
   		 alert("不能小于0！");
   		 return false;
   	   }
  	   $("#"+id).attr("disabled", true);
		if(!confirm("您确定要将" + id + "的投注额修改为:"+ betnew + "？")){
	    	$("#"+id).attr("disabled", false);
	    	return;
	    }
		 
		$.ajax({ 
	          type: "post", 
	          url: "/office/updateBettotal.do", 
	          cache: false, 
	          async: false,
	          timeout:600000, 
	          data :{
	        	  "loginname" : id,
	        	  "newbet" : betnew
	          },
	          success : function(data){
	        	  
	        	  if(data == '更新成功'){
	        		  $("#sub").click();
	        	  }
	        	  console.log(data);
	        	  alert(data);
	          },
	          error: function(){alert("系统错误");},
			  complete: function(){$("#"+id).attr("disabled", false);}
      });
	}
	
</script>
</body>
</html>