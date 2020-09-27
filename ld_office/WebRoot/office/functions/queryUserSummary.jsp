<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户数据汇总</title>
<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
</head>
<body>
<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
<script type="text/javascript" src="/js/prototype_1.6.js"></script>
<script type="text/javascript" src="/js/jquery-1.4.4.min.js"></script>

<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
     document.mainform.submit();
}
</script>


<%-- <s:form action="gatherUserSummary" namespace="/office" name="mainform" id="mainform" theme="simple">


<td><s:text name="玩家账号"/>:<s:textfield name="loginname" size="10"/></td>

<td>
<s:text name="开始时间 "/>: <s:textfield name="start" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
</td>
<td>
<s:text name="结束时间"/>:<s:textfield name="end" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
</td>

<td><s:text name="每页"/>:<s:select list="%{#application.PageSizes}" name="size"></s:select></td>

<td>状态:<s:select name="flag" list="#{'0':'启用','1':'禁用'}"  emptyOption="true"/></td>
<td>状态:<s:select name="dataType" list="#{'0':'最后登入','1':'创建时间'}"  emptyOption="true"/></td>
<td>状态:<s:select name="roleCode" list="#{'MONEY_CUSTOMER':'真钱玩家入','AGENT':'代理'}"  emptyOption="true"/></td>
<td>是:<s:select name="validType" list="#{0:'游戏交易',1:'获取所有'}"/></td>
<td><s:submit key="查询" value="提交" name=""/></td>
</s:form> --%>

<s:form action="queryUserSummary" namespace="/office" name="mainform" id="mainform" theme="simple">

<table>
<tr align="left">
<td><s:text name="玩家账号"/>:<s:textfield name="loginname" size="10"/></td>

<td>
<s:text name="开始时间 "/>: <s:textfield name="start" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
</td>
<td>
<s:text name="结束时间"/>:<s:textfield name="end" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
</td>

<td><s:text name="每页"/>:<s:select list="%{#application.PageSizes}" name="size"></s:select></td>
<td><s:submit key="查询" value ="查询" name=""/></td>

</tr>


<tr align="left">
</tr>

</table>

<s:hidden name="pageIndex" value="1"></s:hidden>
<s:hidden name="order" value="desc"></s:hidden>
<s:hidden name="by" value="createtime"></s:hidden>
</div>

<div id="middle">
  <div id="right">
    <div id="right_01">
	<div id="right_001">
	  <div id="right_02">
	    <div id="right_03"></div>
	  </div>
	  <div id="right_04">
		  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold"><input type="checkbox" id="checkAllBox" /></td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">登入账号</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">主账余额</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">前账户余额</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">状态</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">类型</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">老虎额度(代理)</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">老虎机钱包</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">PT</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">沙巴体育</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">YSB体育</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">IM体育</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">平博体育 </td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">MWG</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">AG</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">TTG</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">EBET</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">N2live</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">BBIN</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">棋乐游</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">捕鱼钱包</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">泛亚</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">秒赢</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">VR彩票</td>
              <td bgcolor="#0084ff" align="center" style="font-size:13px;;color: #FFFFFF;font-weight: bold">开元棋牌</td>

             
            </tr>
            
            <s:iterator var="fc" value="%{#request.page.pageContents}">
            <tr>
                 <td>
					<input type="checkbox" name="item" value="<s:property value="#fc.loginname"/>" />
       			</td>      
       			      	
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
                 <s:url action="getUserhavinginfo" namespace="/office" var="getUserhavinginfourl"><s:param name="loginname" value="%{#fc.loginname}"/></s:url>
                 <a target="_blank" href='<s:property value="%{getUserhavinginfourl}"/>' title="<s:text name=''/>"> <s:property value="#fc.loginname"/></a>
                 </td>
                  
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.credit"/></td>
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.money"/></td>
                    
                  <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
              		<s:if test="#fc.flag==@dfh.utils.Constants@ENABLE">
              					<s:text name="启用"/>
              		</s:if>
              		<s:if test="#fc.flag==@dfh.utils.Constants@DISABLE">
              					<s:text name="禁用"/>
              		</s:if>
              	</td>
                    
                <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;">
              		<s:if test="#fc.role=='MONEY_CUSTOMER'">
              					<s:text name="玩家"/>
              		</s:if>
              		<s:if test="#fc.role=='AGENT'">
              					<s:text name="代理"/>
              		</s:if>
              	</td>
              	 
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.slotaccount"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.pt"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.slot"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.sbaSport"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.ysbSport"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.imSport"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.pbSport"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.mwg"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.ag"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.ttg"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.ebet"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.n2live"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.bbin"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.chess"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.fish"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.fanya"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.minibit"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.vr"/></td>
                 <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#fc.kyqp"/></td>
                                 
            </tr>
           
  	 	 </s:iterator>
            <tr>
              <td colspan="24" align="right" bgcolor="66b5ff" align="center" style="font-size:13px;">
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

