<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>代理佣金查询</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
</head>
<body>
<script type="text/javascript">

function submitForNewAction(btn,action,loginname,year,month){
	btn.disabled=true;
	var remark=window.prompt("您是否要提交，并填写备注(可以默认为空),否则请点取消:","");
	if(remark || remark==""){
		 var xmlhttp = new Ajax.Request(    
				action,
		        {    
		            method: 'post',
		            parameters:"remark="+remark+"&loginname="+loginname+"&year="+year+"&month="+month+"&r="+Math.random(),
		            onComplete: responseMethod  
		        }
	    	);

	}else{
		btn.disabled=false;
	}	
}

function responseMethod(data){

	alert(data.responseText);
	var frm=document.getElementById("mainform");
	frm.submit();
}

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
<s:form action="queryCommissions" namespace="/office" name="mainform" id="mainform" theme="simple">
<div id="excel_menu_left">
结算 --> 代理佣金查询--><a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>

<div id="excel_menu">
开始时间:<s:textfield name="startYyyyMM" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM'})" My97Mark="false" value="%{startYyyyMM}" />
结束时间:<s:textfield name="endYyyyMM" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM'})" My97Mark="false" value="%{endYyyyMM}" />
代理推荐码:<s:textfield name="partner"></s:textfield>
代理注册起始时间:<s:textfield name="startDate"  size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" value="%{startDate}"/>
代理注册结束时间:<s:textfield name="endDate"  size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate" value="%{endDate}"/>
代理账号:<s:textfield name="agent"></s:textfield>
每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
<s:submit value="查 询"></s:submit>
<s:hidden name="pageIndex" value="1"></s:hidden>
<s:set name="order" value="'desc'" />
<s:set name="by" value="'amount'" />
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
                          <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold" onclick="orderby('createTime');">年份</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold" onclick="orderby('createTime');">月份</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">代理账号</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">下线会员个数</td>
               <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">活跃会员个数</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold" title="点击查看明细"  onclick="orderby('amount');">佣金</td>
			  <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">备注</td>

            </tr>
            <s:set var="sumAmount" value="0.0" scope="request" />
            <s:iterator var="fc" value="%{#request.page.pageContents}" >
            <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.id.year"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.id.month"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:url namespace="/office" action="queryCommissionrecords" var="queryCommissionrecordsUrl" escapeAmp="false"><s:param name="agent" value="#fc.id.loginname" /><s:param name="year" value="#fc.id.year" /><s:param name="month" value="#fc.id.month" /><s:param name="eaProfitAmount" value="#fc.eaProfitAmount" /><s:param name="crate" value="#fc.crate" /></s:url><s:a target="_blank" href="%{queryCommissionrecordsUrl}" ><s:property value="#fc.id.loginname"/></s:a></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.subCount"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.activeuser"/></td>
              <s:set var="loginname" value="#fc.id.loginname" scope="request"/>
              <s:set var="year" value="#fc.id.year" scope="request"/>
              <s:set var="month" value="#fc.id.month" scope="request"/>
              <s:if test="#fc.flag==@dfh.model.enums.CommisionType@INIT.code">
              		<c:url var="action" value="/office/excuteCommission.do" scope="request" />
              	
              		 
              		<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
              		<s:property value="@dfh.model.enums.CommisionType@getText(#fc.flag)"/><input type="button" value="执行" onclick="submitForNewAction(this,'${action}','<s:property value="#fc.id.loginname"/>','<s:property value="#fc.id.year"/>','<s:property value="#fc.id.month"/>');"/>
              		</td>
              </s:if>
              <s:else>
              		<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.CommisionType@getText(#fc.flag)"/></td>
              </s:else>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.amount"/></td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
            </tr>            
            <s:set var="sumAmount" value="%{#request.sumAmount+#fc.amount}" scope="request" />
  	 	 </s:iterator>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6">当页小计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">${sumAmount}</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"></td>
            </tr>
  	 	    <tr>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" colspan="6">总计:</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">${page.statics1}</td>
              <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"></td>
            </tr> 
               <tr>
              <td bgcolor="#e4f2ff" align="right"  style="font-size:13px;" colspan="8">${page.jsPageCode}</td>
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

