<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%@page import="dfh.model.Users"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <base href="<%=request.getRequestURL()%>"/>
    <s:include value="title.jsp"></s:include>
    <meta http-equiv="description" content="账户管理-下线提案"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
<link type="text/css" rel="stylesheet" href="yilufa.css" />
<style type="text/css">
<!--
body {
	background-color: #0d0213;
}
-->
</style>
<script type="text/javascript" language="javascript">
	
	
	function $(ele){
		return document.getElementById(ele);
	}

	
</script>
</head>

<body>
<div id="aftercontainercon">
     <div id="container">
            	<div id="header">
                  <div id="logincontainer">
                      <div id="bulletincon">
                         	<s:include value="headnews.jsp"></s:include>
                      </div>
                      <!--bulletincon-->
                     	<s:include value="headlogin.jsp"></s:include>
                  </div>
                  <!--logincontainer--> 
                  <div id="navcontainer">
                      <s:include value="headmenu2.jsp"></s:include>
                  </div>
                  <!--navcontainer-->
             </div>
             <!--header-->
             <div id="requstcontent">
             
                    <div class="managementitle"><img src="images/management01.jpg" /></div>
                    		<s:include value="memberFunctionList_member.jsp"></s:include>
                    		<s:url action="searchagprofit" namespace="/asp" var="searchsubuserProposalUrl"></s:url>
                        <table border="0" cellpadding="0" cellspacing="0" width="760px" class="mytable" align="left" >
                        	<tr align="left">
                        		<td valign="bottom" colspan="8">
                        		<form style="margin-top:10px;padding-left:42px" action="${searchsubuserProposalUrl }" method="post" name="mainform">
                        			<input type="hidden" name="pageIndex" value="1"/>
										<input type="hidden" name="size" value="10" />
                        			<label style="margin-left:0px;">起点时间：</label>
			                        <input type="text" class="searchproposal_input" value="${requestScope.starttime}" name="starttime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
			                        <label style="margin-left:100px;">截止时间：</label>
			                        <input type="text" class="searchproposal_input" value="${requestScope.endtime}" name="endtime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
			                        <br />
			                        <label style="margin-left:0px;">会员账号：</label>
			                        <input type="text" class="searchuserproposal_input" name="loginname" value="${requestScope.loginname }" />
			                        <label style="margin-left:100px;">游戏平台
			                        <select name="platform" id="platform"  style="margin-left:0px;">
			                        	<option value="" selected="selected"></option>
			                        	<option value="ea" <c:if test="${platform eq 'ea' }">selected="selected"</c:if>>ea</option>
			                        	<option value="ag" <c:if test="${platform eq 'ag' }">selected="selected"</c:if>>ag</option>
			                        	<option value="agin" <c:if test="${platform eq 'agin' }">selected="selected"</c:if>>agin</option>
			                        	<option value="bbin" <c:if test="${platform eq 'bbin' }">selected="selected"</c:if>>bbin</option>
			                        	<option value="keno" <c:if test="${platform eq 'keno' }">selected="selected"</c:if>>keno</option>
			                        	<option value="sb" <c:if test="${platform eq 'sb' }">selected="selected"</c:if>>sb</option>
			                        	<option value="pt" <c:if test="${platform eq 'pt' }">selected="selected"</c:if>>pt</option>
			                        </select></label>
			                        
			                        <input style="margin-left:5px;" type="submit" class="searchDedail_button" value="" />
			                     </form>
                        		</td>
                        	</tr>
                        	<tr><td colspan="8" height="5px">&nbsp;</td></tr>
                        	<tr align="center">
                        		<td width="50px">序号</td>
                        		<td width="80px" colspan="2">会员帐号</td>
                        		<td width="120px" colspan="2">创建时间</td>
                        		<td width="40px">平台</td>
                        		<td>投注总额</td>
                        		<td width="150px">输赢值</td>
                        	</tr>
                       <c:set var="amountSum" value="0" scope="request"></c:set>
            		   <s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
                       		
                        		<tr>
                        			<td align="center"><s:property value="#st.index+1"/></td>
                        			<td align="center" colspan="2"><s:property value="#fc.loginname"/></td>
                        			<td align="center" colspan="2"><s:property value="#fc.tempCreateTime"/></td>
                        			<td align="right"><s:property value="#fc.platform"/></td>
                        			<td align="center"><s:property value="#fc.bettotal"/></td>
                        			<td align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#fc.amount)"/></td>
                        			
                        			
                        			
                        		</tr>
                        	<s:set var="amountValue" value="#fc.amount" scope="request"></s:set>
            				<c:set var="amountSum" value="${amountSum+amountValue}"  scope="request"></c:set>
                        	</s:iterator>
                        	
                        	<tr><td colspan="7" height="5px">&nbsp;</td>
                        	<td  height="5px">当页总计:&nbsp;<s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)"/></td>
                        	<td  height="5px" colspan="2">&nbsp;</td></tr>
                        	<tr><td colspan="7" height="5px">&nbsp;</td>
                        	<td  height="5px">总计:&nbsp;&nbsp;&nbsp;<s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)"/></td>
                        	<td  height="5px" colspan="2">&nbsp;</td></tr>
                        	
                       		 <tr valign="top" >
							                          <td height="31" align="right" colspan=8>${page.jsPageCode}</td>
							 </tr>
                        </table>
                   
             </div>
             <!--requstcontent-->
             <div id="footer">
               			<s:include value="/tpl/footer.jsp"></s:include>
               <!--footermenu-->
             </div>
             <!--footer-->  
  </div>
        <!--container--> 
</div>
<!--containercon-->
<s:url value="/scripts/My97DatePicker/WdatePicker.js" var="WdatePickerUrl"></s:url>
<script type="text/javascript" src="${WdatePickerUrl}"></script>
</body>
</html>
