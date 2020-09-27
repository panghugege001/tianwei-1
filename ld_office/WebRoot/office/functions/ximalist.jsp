<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>洗码列表</title>
    <link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
    
<script type="text/javascript" src = "<c:url value="/scripts/jquery-1.3.2.min.js"/>"></script>
<script type="text/javascript">
$(document).ready(function(){
   //define the chkall with checkbox named 'chkall'
   var $chkall = $('#chkall');
   var $chkarry = $('input[type="checkbox"]').not($('#chkall'));
   //the onclick of chkall;
   $chkall.click(function(){
    var b = $(this).attr('checked');
    $chkarry.each(function(){ $(this).attr('checked', b); });
   });
   //each onclick of checkboxs without chkall;
   $chkarry.each(function(){
    $(this).click(function(){
     //put the 'checked' attribute of this clicked checkbox to the 'checkboxall'
     $chkall.attr('checked', $(this).attr('checked'));
     //traversing all the checkboxs without chkall and compare each 'checked' attribute and chkall's 'checked' attribute,
     //if they are all true, return the true to chkall; else return false to the chkall.
     $chkarry.each(function(index){ $chkall.attr('checked', ($chkall.attr('checked') && $chkarry.eq(index).attr('checked'))? true:false); });
    });
   });
});
</script>

<script type="text/javascript">
function mylove()
{
   var falg = 0;
   $(":checkbox").each(function(){
    if($(this).attr("checked"))
    {
     falg +=1;
    }
   })
   if(falg >0)
    return true;
   else
    return false;
}
</script>
    <script type="text/javascript">
    function onSubmitExecute(){
         if(!mylove()){
           alert("您未选中任何数据!");
           return false;
             }
        
        if(window.confirm("确定执行选中的洗码列表?")){
        	document.getElementById('sub_bottom').disabled=true;
        	return true;
         }
        return false;
     }
    </script>
       
  </head>
  <body>
  <s:form action="autoExcuteXima" onsubmit="return onSubmitExecute()" namespace="/office" name="mainform" id="mainform" theme="simple">
   <div id="excel_menu_left">
           结算 --> 自动结算洗码-->待执行洗码列表 <a href="javascript:history.back();"><font color="red">上一步</font></a>
   </div>
   
   <p align="left" >洗码已生成完毕，请确认后执行(执行需要较长的时间，请耐心在该页面内等待) </p>
   
   <!-- 显示生成的洗码记录 -->
<table width="98%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr bgcolor="#0084ff">
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">提案号</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">提案人</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">用户类型</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">提案类型</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">提案状态</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">涉及帐号</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">涉及额度</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">加入时间</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">会员等级</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">备注</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;">提案信息</td>
              <td  align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;"><input type="checkbox" id="chkall"/></td>
            </tr>
            
            <c:set var="amountSum" value="0" scope="request"></c:set>
            <s:iterator var="fc" value="%{#request.page}">
            <s:if test="#fc.amount>=500000"><c:set var="bgcolor" value="#FF9999"/></s:if>
            <s:elseif test="#fc.amount>=50000"><c:set var="bgcolor" value="#D20000"/></s:elseif>
            <s:elseif test="#fc.amount>=5000"><c:set var="bgcolor" value="#FFABCE"/></s:elseif>
            <s:else><c:set var="bgcolor" value="#e4f2ff"/></s:else>
            <tr bgcolor="${bgcolor}">
              <td align="center"  style="font-size:13px;"><s:url namespace="/office" action="queryTaskDetail" var="queryTaskDetailUrl" escapeAmp="false"><s:param name="pno" value="#fc.pno" /></s:url><s:a cssStyle="color:#000000;" target="_blank" href="%{queryTaskDetailUrl}"><s:property value="#fc.pno"/></s:a></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.proposer"/></td>
               <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.Constants@getTitleText(#fc.loginname)"/> </td>
               <td  align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.ProposalType@getText(#fc.type)"/> </td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.ProposalFlagType@getText(#fc.flag)"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.loginname"/></td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
              <td  align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
              <td  align="center"  style="font-size:13px;"><s:if test="#fc.type!=@dfh.model.enums.ProposalType@NEWACCOUNT.code"><s:property value="@dfh.model.enums.VipLevel@getText(#fc.quickly)"/></s:if></td>
              <td  align="center"  style="font-size:13px;"><s:property value="#fc.remark"/></td>
              <td  align="center"  style="font-size:13px;"><s:url id="toquery" escapeAmp="false" action="queryProposalDetail" namespace="/office"><s:param name="pno" value="#fc.pno" /><s:param name="proposalType" value="#fc.type" /></s:url><a style="color:#000000;" target="_blank" href="<s:property value='%{toquery}'/>" title="点击查看提案的明细信息">查询</a></td>
              <s:set var="jobPno" value="#fc.pno" scope="request"/>
 
             
             <td  align="center"  style="font-size:13px;">
             <input type="checkbox" name="pnos" value="${fc.pno}" class="checkBox" />
             </td>
             
            </tr>
           	<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            <c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
  	 	 </s:iterator>
  	 	   	 <tr bgcolor="#e4f2ff">
              <td  align="center"  style="font-size:13px;" colspan="6">总计:</td>
              <td  align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#request.amountSum)"/></td>
              <td  align="right"  style="font-size:13px;" colspan="5"><s:submit id="sub_bottom" value="执  行"></s:submit></td>
            </tr>
      </table>
   </s:form>
   <c:import url="/office/script.jsp" />
  </body>
</html>
