<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/office/include.jsp" %>
<%@page import="dfh.model.enums.CardType"%>
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
    <title>催账记录</title>
    
	<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>

  </head>
  
  <body>
	<s:form action="queryUrgeOrderPage" namespace="/office" name="mainform" id="mainform" theme="simple">
	<div id="excel_menu_left">
	操作 --> 催账信息<a href="javascript:history.back();"><font color="red">上一步</font></a>
	</div>
	
	<div id="excel_menu" style="text-align:center;">
	<s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='finance' || #session.operator.authority=='sale_manager'" >
		开始时间(含)<s:textfield id="startDate" name="startDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
		结束时间(含)<s:textfield id="endDate" name="endDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
	</s:if>	
	
	状态:<s:select name="status" list="#{'':'全部','0':'待处理','1':'处理成功','2':'处理失败'}" listKey="key" listValue="value"/>
	存款类型:<s:select name="type" list="#{'':'全部','1':'在线支付宝扫描','2':'支付宝扫描','3':'支付宝附言','4':'微信扫描','5':'微信额度验证','6':'在线支付','7':'工行附言','8':'招行附言','9':'点卡支付'}" listKey="key" listValue="value"/>
	会员帐号:<s:textfield name="loginname" size="15" />
	每页:<s:select list="%{#application.PageSizes}" name="size"></s:select>
	<s:submit value="查询"></s:submit>
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
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('loginname');" >账号</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >存款人姓名</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >存款方式</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >支付宝订单号</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >昵称</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >存款时间</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >金额</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >存款截图</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >点卡类型</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >点卡卡号</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >状态</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >创建时间</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >更新时间</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >处理人员</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >备注</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >操作</td>
				</tr>
				<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td align="center"  style="font-size:13px;"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#fc.loginname"/>"><s:property value="#fc.loginname"/></a></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.accountName"/></td>
					<td align="center"  style="font-size:13px;">
						<s:if test='#fc.type=="1"'>在线支付宝扫描</s:if>
						<s:elseif test='#fc.type=="2"'>支付宝扫描</s:elseif>
						<s:elseif test='#fc.type=="3"'>支付宝附言</s:elseif>
						<s:elseif test='#fc.type=="4"'>微信扫描</s:elseif>
						<s:elseif test='#fc.type=="5"'>微信额度验证</s:elseif>
						<s:elseif test='#fc.type=="6"'>在线支付</s:elseif>
						<s:elseif test='#fc.type=="7"'>工行附言</s:elseif>
						<s:elseif test='#fc.type=="8"'>招行附言</s:elseif>
						<s:elseif test='#fc.type=="9"'>点卡支付</s:elseif>
					</td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.thirdOrder"/></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.nickname"/></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.depositTime"/></td>
					<td align="center"  style="font-size:13px;"><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
					<td align="center"  style="font-size:13px;">
						<s:if test="#fc.picture!=''">
							<a href="ftp://payimg2016ftp:Ns-2BcdmK8[7Y*Q0Ml007@47.90.81.197:8688<s:property value="#fc.picture"/>" target="_blank">
								<img src="ftp://payimg2016ftp:Ns-2BcdmK8[7Y*Q0Ml007@47.90.81.197:8688<s:property value="#fc.picture"/>" width="200" height="150">
							</a>
						</s:if>
					</td>
					<td align="center" width="13px">
						<s:property value="@dfh.model.enums.CardType@getText(#fc.cardtype)" />
					</td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.cardno"/></td>
					<td align="center"  style="font-size:13px;">
						<s:if test="#fc.status==0">待处理</s:if>
						<s:elseif test="#fc.status==1">处理成功</s:elseif>
						<s:elseif test="#fc.status==2">处理失败</s:elseif>
					</td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.createtime"/></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.updatetime"/></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.operator"/></td>
					<td align="center"  style="font-size:13px;" id="remark"><s:property value="#fc.remark"/></td>
					<td  align="center"  style="font-size:13px;">
						<s:if test="#fc.status==0">
							<input type="button"  onclick='handleUrgeOrder("Y","<s:property value="#fc.id"/>")' value="处理成功">
				   	   	   	<input type="button"  onclick='handleUrgeOrder("F","<s:property value="#fc.id"/>")' value="处理失败">
						</s:if>
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

function handleUrgeOrder(value,id){
	var remark = null;
	if(value=="F"){
		remark = window.prompt("请填写失败原因！","");
		if(remark){
			$.ajax({ 
		          type: "post", 
		          url: "/office/handleUrgeOrder.do", 
		          cache: false,  
		          async: false,
		          data:{"value":value,"id":id,"remark":remark},
		          success : function(data){
		          	 alert("操作成功");
		          	 var frm=document.getElementById("mainform");
					 frm.submit();
		          },
		          error: function(){alert("系统错误");},
		    });
		}
	}else{
		if(confirm("你确认要执行此操作么？")){
			$.ajax({ 
		          type: "post", 
		          url: "/office/handleUrgeOrder.do", 
		          cache: false,  
		          async: false,
		          data:{"value":value,"id":id,"remark":remark},
		          success : function(data){
		          	 alert("操作成功");
		          	 var frm=document.getElementById("mainform");
					 frm.submit();
		          },
		          error: function(){alert("系统错误");},
		    });
		}
	}
}

function showPic(path){
	$.ajax({ 
        type: "post", 
        url: "/office/showPicture.do", 
        cache: false,  
        async: false,
        data:{"path":path},
        success : function(data){
        },
        error: function(){alert("系统错误");},
  });
}
</script>
</body>
</html>