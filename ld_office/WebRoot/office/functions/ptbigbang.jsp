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
    <title>PT大爆炸</title>
    
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
<s:form action="queryPTBigBangRecords" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> PT大爆炸<a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance' || #session.operator.authority=='sale_manager'" >
	<input type="button" id="distributeBtn" onclick="calculatePTBigBangGifgMoney('-1')" value="输钱返赠" style="margin-right:80px;color:red;font-weight:bold">
	<input type="button" id="distributeBtn" onclick="calculatePTBigBangGifgMoney('1')" value="赢钱返赠" style="margin-right:80px;color:red;font-weight:bold">
</s:if>	

状态:<s:select name="status" list="#{'':'全部','0':'未派发','1':'已派发','2':'已领取','3':'已处理'}" listKey="key" listValue="value"/>
会员帐号:<s:textfield name="loginname" size="15" />
推荐码:<s:textfield name="intro" size="10" />
派发日期: <s:textfield name="distributeDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
类型:<s:select name="type" list="#{'':'全部','1':'玩家正盈利','2':'玩家负盈利'}" listKey="key" listValue="value"/>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >推荐码</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('netwin_lose');">盈利金额</td>
               <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">活动时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">红利</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('giftmoney');">活动礼金</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">流水倍数</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('distributeTime');">派发时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('getTime');">领取时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >操作</td>
            </tr> 
            <s:iterator var="fc" value="%{#request.page.pageContents}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc[0]"/>"><s:property value="#fc[0]"/></a></td>
	              <td align="center"  style="font-size:13px;"><s:property value="#fc[11]"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[1])"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[2]"/>-<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[3]"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[4])"/></td>
	              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc[5])"/></td>
	              <td  align="center"  style="font-size:13px;">
					 <s:if test="#fc[6]=='0'">未派发</s:if>
					 <s:elseif test="#fc[6]=='1'">已派发</s:elseif>
					 <s:elseif test="#fc[6]=='2'">已领取</s:elseif>
					 <s:elseif test="#fc[6]=='3'">已处理</s:elseif>
	              </td>
	              <td align="center"  style="font-size:13px;"><s:property value="#fc[7]"/></td>
	              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[8]"/></td>
	              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc[9]"/></td>     		
	              <td  align="center"  style="font-size:13px;"><s:property value="#fc[10]"/>   </td> 
	              <td  align="center"  style="font-size:13px;">
	              	   <s:if test="#fc[6]=='2'">
	              	   	   <input type="button" id="removeBetLimitBtn" onclick="removeBetLimit('<s:property value="#fc[0]"/>')" value="解除流水限制">
	              	   </s:if>
	              	    <s:if test="#fc[6]=='4'">
	              	   	   <input type="button" id="updateStateBtn" onclick="updateState('<s:property value="#fc[0]"/>')" value="允许领取奖金">
	              	   </s:if>
	              </td>
	            </tr>
	            <s:set var="amountValue" value="#fc[5]" scope="request"></s:set>
            	<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
	  	 	 </s:iterator>
	  	 	 <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="5">当页小计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"></td>
             </tr>
  	 	     <tr>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="5">总计:</td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="%{#request.page.statics1}"/></td>
               <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6"></td>
             </tr>
             <tr>
              <td colspan="12" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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
	function calculatePTBigBangGifgMoney(type){
	    $("#distributeBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/calculatePTBigBangGifgMoney.do", 
	          cache: false,  
	          async: false,
	          data:{"type":type},
	          timeout:600000, 
	          success : function(data){alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){$("#distributeBtn").attr("disabled", false);}
        });
	}
	function removeBetLimit(username){
		$("#removeBetLimitBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/removePTBigBangTransferLimit.do", 
	          cache: false,  
	          async: false,
	          data:{"loginname":username},
	          success : function(data){
	          	 alert("操作成功");
	          	 var frm=document.getElementById("mainform");
				 frm.submit();
	          },
	          error: function(){alert("系统错误");},
			  complete: function(){$("#removeBetLimitBtn").attr("disabled", false);}
        });
	}
	
	function updateState(username){
		$("#updateStateBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/updatePTBigBangstate.do", 
	          cache: false,  
	          async: false,
	          data:{"loginname":username},
	          success : function(data){
	          	 alert("操作成功");
	          	 var frm=document.getElementById("mainform");
				 frm.submit();
	          },
	          error: function(){alert("系统错误");},
			  complete: function(){$("#updateStateBtn").attr("disabled", false);}
        });
	}
	
	
	
</script>
</body>
</html>