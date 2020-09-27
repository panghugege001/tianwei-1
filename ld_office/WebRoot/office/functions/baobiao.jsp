<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.model.enums.ProposalType"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
response.setHeader("pragma", "no-cache");
response.setHeader("cache-control", "no-cache");
response.setDateHeader("expires", 0);
%>
<head>
<title>盈利报表</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
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

function submitForCashOut(pno,proposalType){
	//btn.disabled=true;
	//var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/bankinfo/proposalforcashout.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=350, width=400,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function submitForNewAction(btn,action,pno){
	btn.disabled=true;
	var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
	if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&jobPno="+pno+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function openproposaldetail(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/proposal_newdetail.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
}

function cashoutaudit(pno,proposalType){
	var height = window.screen.height;
	var width =window.screen.width; 
	window.open ('<%=basePath%>/office/functions/cashoutaudit.jsp?pno='+pno+"&r="+Math.random()+"&proposalType="+proposalType,'','height=650, width=500,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top='+(height-400)/2+',left='+(width-300)/2 ) ;
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
统计 --&gt; 盈利报表 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</p>
<div  id="excel_menu" style="position:absolute; top:25px;left:0px;">
<s:form action="querybaobiao" namespace="/office" name="mainform" id="mainform" theme="simple">
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="600px">
				<tr align="left">
					
					<td>年份:<s:textfield name="year" size="20" onfocus="WdatePicker({dateFmt:'yyyy'})" My97Mark="false" value="%{vyear}" /></td>
					<td>月份:<s:textfield name="month" size="20" onfocus="WdatePicker({dateFmt:'MM'})" My97Mark="false" value="%{vmonth}" /></td>
					<td>peso汇率:<s:textfield name="pesoRate" value="%{pesoRate==null?6.6:pesoRate}" size="4"/></td>
					<td><s:submit cssStyle="width:65px; height:25px;" value="查询"></s:submit></td>
				</tr>
				
			
		</table>
	</td>
</tr>

</table>

<s:hidden name="pageIndex"/>
<s:set name="by" value="'createTime'" />
<s:set name="order" value="'desc'" />
<s:hidden name="order" value="%{order}"/>
<s:hidden name="by" value="%{by}"/>
<s:hidden name="jobPno"></s:hidden>
<s:hidden name="remark"></s:hidden>
</s:form>
</div>
<br/><br/><br/><br/>
<div id="middle" style="position:absolute; top:155px;left:0px">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="1110px"  border="0" cellpadding="0" cellspacing="1" bgcolor="#99c8d7" >
            <tr bgcolor="#0084ff">
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >项目</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >金额</td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">币种</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">备注</td>
             
            </tr>
             
            <tr >
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >后台总存款</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">上个月1号零点到下个月1号零点已执行 email 及手工存款的总和</td>
             
            </tr>
            
        
             <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >后台总提款(会员)</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.memberpage.statics1)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">上个月1号零点到下个月1号零点已执行的会员类型为真钱会员的提款</td>
             
            </tr>
            
             <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >代理信用预支(代理)</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.agentAmount)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">上个月1号零点到下个月1号零点已执行的代理</td>
             
            </tr>
            
            <c:if test="${requestScope._newProfit ne 1}">
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >提款手续费</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.memberpage.totalRecords*1.2)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">每笔手续费1.2元</td>
             
            </tr>
            </c:if>
             <c:if test="${requestScope._newProfit eq 1}">
              <tr >
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >户内转账手续费</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.infeepage.statics1)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">户内转账手续费</td>
             
            </tr>
            </c:if>
             <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >在线支付</td>
              	<c:choose>
            		<c:when test="${requestScope._newProfit eq 1}">
            			<td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >
              			<s:property value="@dfh.utils.NumericUtil@double2String(#request.payorderpage.statics1)"/></td>
              			<td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
             			<td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">在线支付总额,已扣除手续费</td>
              		</c:when>
              		<c:otherwise>
              			<td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >
              			<s:property value="@dfh.utils.NumericUtil@double2String(#request.payorderpage.statics1*0.99)"/></td>
              			<td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
             			<td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">在线支付总额的*99%,其中1%为手续费</td>
              		</c:otherwise>
              	</c:choose>
              
            </tr>
            
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >原始结余</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >
              	<c:choose>
            		<c:when test="${requestScope._newProfit eq 1}">
            			<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1-#request.memberpage.statics1+#request.payorderpage.statics1-#request.infeepage.statics1)"/>
            		</c:when>
            		<c:otherwise>
            			<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1-#request.memberpage.statics1-#request.memberpage.totalRecords*1.2+#request.payorderpage.statics1*0.99)"/>
            		</c:otherwise>
            	</c:choose>
              	</td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">原始结余=总存-总提-手续费</td>
             
            </tr>
            
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >&nbsp;</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >&nbsp;</td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">&nbsp;</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">&nbsp;</td>
             
            </tr>
            
            <tr bgcolor="#0084ff">
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >应扣开支项目</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >金额</td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">币种</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">备注</td>
             
            </tr>
             <c:if test="${vyear le 2013  && vmonth le 9}">
            <tr>
          
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >EA平台费(8%)</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.amount*0.08)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">平台费=总赢*8%</td>
             
            </tr>
            </c:if>
            <c:if test="${requestScope._newProfit ne 1}">
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >代理佣金</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.agentpage.statics1)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;"></td>
             
            </tr>
            </c:if>
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >中国国内开支</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.businessproposalpage.statics1)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">包括各种市场推广、域名、广告、主机、客服群发软件等费用</td>
             
            </tr>
            
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >菲律宾事务开支</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >
              	<c:if test="${requestScope._newProfit ne 1}">
              		<s:property value="@dfh.utils.NumericUtil@double2String(#request.businessproposalpesopage.statics1/6.6)"/></td>
              	</c:if>
              	<c:if test="${requestScope._newProfit eq 1}">
              		 <s:property value="@dfh.utils.NumericUtil@double2String(#request.businessproposalpesopage.statics1/#request.pRate)"/>
              	</c:if>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">包括政府税费、牌照律师费、员工工资福利、办公场地、用具、伙食、交通、通讯等开销</td>
             
            </tr>
           
             <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >行政总后台支出</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" ><s:property value="@dfh.utils.NumericUtil@double2String(#request.xingzheng)"/></td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">包括各种主机,域名,平台等费用</td>
             
            </tr>
            
            <tr>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >&nbsp;</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >&nbsp;</td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">&nbsp;</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">&nbsp;</td>
             
            </tr>
            
            <tr bgcolor="#0084ff">
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >净盈利</td>
              <td  align="center" width="80px" style="color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" >
               <c:if test="${vyear lt 2013 or (vyear le 2013  && vmonth le 9)}">
              
              		<c:if test="${requestScope._newProfit ne 1}"> 
              			<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1-#request.memberpage.statics1-#request.memberpage.totalRecords*1.2+#request.payorderpage.statics1*0.99-#request.amount*0.08-#request.xingzheng-#request.agentpage.statics1-#request.businessproposalpage.statics1-#request.businessproposalpesopage.statics1/6.6)"/>  
              		</c:if>
              		<c:if test="${requestScope._newProfit eq 1}">
	               	 	<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1-#request.memberpage.statics1-#request.infeepage.statics1+#request.payorderpage.statics1-#request.amount*0.08-#request.xingzheng-#request.businessproposalpage.statics1-#request.businessproposalpesopage.statics1/#request.pRate)"/>
	               	 </c:if>
	           </c:if>
	           
	            <c:if test="${vyear gt 2013 or (vyear ge 2013  && vmonth ge 10)}">
	            
	            <c:if test="${requestScope._newProfit ne 1}"> 
              			<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1-#request.memberpage.statics1-#request.memberpage.totalRecords*1.2+#request.payorderpage.statics1*0.99-#request.xingzheng-#request.agentpage.statics1-#request.businessproposalpage.statics1-#request.businessproposalpesopage.statics1/6.6)-#request.agentAmount"/>  
              		</c:if>
              		<c:if test="${requestScope._newProfit eq 1}">
	               	 	<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1-#request.memberpage.statics1-#request.infeepage.statics1+#request.payorderpage.statics1-#request.xingzheng-#request.businessproposalpage.statics1-#request.businessproposalpesopage.statics1/#request.pRate-#request.agentAmount)"/>
	               	 </c:if>
	            </c:if>
              </td>
              <td  align="center" width="70px" style="color: #FFFFFF;font-weight: bold;">人民币</td>
            
              <td  align="center" width="140px" style="color: #FFFFFF;font-weight: bold;">净盈利=原始结余-应扣开支项目</td>
             
            </tr>
            
          </table>
	  </div>
	</div>
	</div>
  </div>
</div>
<c:import url="/office/script.jsp" />
</body>
</html>

