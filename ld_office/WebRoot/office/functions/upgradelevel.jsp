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
    
    <title>会员升降级</title>
    
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
function excute(transfeId, amount){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/bankonline/supplementPayOrder.jsp?transfeId='+transfeId+"&amount="+amount+"&r="+Math.random(),'','height=250, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}
</script>

<s:form action="queryUpgradeLog" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
操作 --> 会员升降级 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu" style="text-align:center;">
<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='sale_manager'">
	<input type="button" id="autoUpgradeBtn" onclick="autoUpgrade()" value="执行升降级" style="margin-right:280px;color:red;font-weight:bold;">
</s:if>	
状态:<s:select name="status" list="#{'':'全部','1':'已处理','2':'待确认','0':'已取消'}" listKey="key" listValue="value"/>
会员帐号:<s:textfield name="loginname" size="15" />
处理月份:<s:textfield size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM'})" My97Mark="false" name="upgradeYyyyMm" value="%{upgradeYyyyMm}"/>
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
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('bet');" >投注额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('ptBet');">PT投注额</td>
              <!-- <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('betOfWeek');">周投注额</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('ptBetOfWeek');">周PT投注额</td> -->
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">处理时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >原级别</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >新级别</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >状态</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >投注月</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >操作</td>
            </tr>
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr bgcolor="${bgcolor}">
              <td align="center"  style="font-size:13px;"><s:property value="#fc.username"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.bet)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.ptBet)"/></td>
              <!-- <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.betOfWeek)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.ptBetOfWeek)"/></td> -->
              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/>   </td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.VipLevel@getText(#fc.oldlevel)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.VipLevel@getText(#fc.newlevel)"/></td>
              <td align="center"  style="font-size:13px;">
				 <s:if test="#fc.status==1">已处理</s:if>
				 <s:elseif test="#fc.status==2">待确认</s:elseif>
				 <s:elseif test="#fc.status==0">已取消</s:elseif>
              </td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.optmonth"/>   </td> 
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.remark"/>   </td> 
              <td  align="center"  style="font-size:13px;">
              	   <s:if test="#fc.status==2">
              	   	   <input type="button" onclick="excuteUpgrade(<s:property value="#fc.id"/>, 1)" value="执行">
              	   	   <input type="button" onclick="excuteUpgrade(<s:property value="#fc.id"/>, 0)" value="取消">
              	   </s:if>
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
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	function autoUpgrade(){
		$("#autoUpgradeBtn").attr("disabled", true);
		$.ajax({ 
	          type: "post", 
	          url: "/office/autoUpgrade.do", 
	          cache: false,  
	          //async: false,
	          timeout: 600000, 
	          success: function(data){alert(data);},
	          error: function(){alert("系统错误");},
			  complete: function(){$("#autoUpgradeBtn").attr("disabled", false);}
        });
        alert("自动升降级已启动...");
	}
	function excuteUpgrade(id, target){
		$.ajax({ 
	          type: "post", 
	          url: "/office/optUpgrade.do", 
	          cache: false,  
	          async: false,
	          data:{"fldid":id, "status":target},
	          success : function(data){
	          	 alert("操作成功");
	          	 var frm=document.getElementById("mainform");
				 frm.submit();
	          },
	          error: function(){alert("系统错误");},
        });
	}
</script>
</body>
</html>