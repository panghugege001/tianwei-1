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
    <title>比邻导入数据</title>
    
	<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/css/DivCover.css' />" rel="stylesheet" type="text/css" />
	<link href="<c:url value='/css/styleoverlay.css' />" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<style>
        tdstyle{align="center";font-size:13px;}
    </style>
  </head>
  
  <body>

    <div class="overlay"></div>
      <div id="AjaxLoading" class="showbox">
        <div class="loadingWord">
            <img alt="waiting" src="/images/waiting.gif" />加载中，请稍候...</div>
    </div>
    
	<s:form action="queryImportData" namespace="/office" name="mainform" id="mainform" theme="simple">
		<div id="excel_menu_left">
			操作 --> 比邻导入数据<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</div>
		<div id="excel_menu" style="text-align:center;">
			数据开始时间(含):<s:textfield id="_startTime" name="start" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
			数据结束时间(含):<s:textfield id="_endTime" name="end" size="20" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{endTime}" />
			客服推荐码:<s:select name="intro" list="#{'':'全部','cs1':'cs1','cs2':'cs2','cs3':'cs3','cs4':'cs4','cs5':'cs5','cs6':'cs6','ts1':'ts1','ts2':'ts2','ts3':'ts3','ts4':'ts4','ts5':'ts5','ts6':'ts6'}" listKey="key" listValue="value"/>
			是否存款:<s:select name="isdeposit" list="#{'':'全部','Y':'是','N':'否'}" listKey="key" listValue="value"/>
			电销账号:<s:textfield name="agent" ></s:textfield>
			会员等级:<s:select name="level" list="%{#application.VipLevel}" listKey="code" listValue="text" emptyOption="true"/>
			<input type="checkbox" name="state" value="1" />非电服线下会员
			<s:submit value="查询"></s:submit>
			<input type="button" value="导入数据" onclick="importData()"/>
			<s:set name="by" value="'createtime'" />
			<s:set name="order" value="'desc'" />
			<s:hidden name="order" value="%{order}"/>
			<s:hidden name="by" value="%{by}"/>
		</div>
	</s:form>
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
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员账号</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员姓名</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">会员等级</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">上级代理</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">推荐码</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">用户注册时间</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold">用户最后登录时间</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('intvalday');">时间间隔(天)</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序" onclick="orderby('loginTimes');">用户登录次数</th>
	              <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序"  onclick="orderby('profitall');">总盈利额</th>
	              <!-- <th bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" title="点击排序"  onclick="orderby('profitall');">回调情况</th> -->
	            </tr>
				<s:iterator var="dataImportBL" value="%{#request.resultList}" status="st">
				<tr>
					<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;" class="loginnameTd"><a target="_blank" href="/office/getUserhavinginfo.do?loginname=<s:property value="#dataImportBL.loginname"/>"><s:property value="#dataImportBL.loginname"/></a></td>
					<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.accountName"/></td>
					<td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="@dfh.model.enums.VipLevel@getText(#dataImportBL.level)"/></td>
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.agent"/></td>
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.intro"/></td>
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.regTime"/></td>
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.lastLoginTime"/></td>
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.day"/></td>
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"><s:property value="#dataImportBL.loginTimes"/></td>
		            <td <s:if test="#dataImportBL.profitAllAmount>=100000">bgcolor="red"</s:if><s:else>bgcolor="#e4f2ff"</s:else> align="center"  style="font-size:13px;">
		            		<s:property value="@dfh.utils.NumericUtil@formatDouble(#dataImportBL.profitAllAmount)"/>
		            </td>
		            <!-- <td bgcolor="#e4f2ff" align="center"  style="font-size:13px;"></td> -->
		            <td bgcolor="#e4f2ff" align="center"  style="font-size:13px; display: none" class="phoneTd"><s:property value="#dataImportBL.phone"/></td>
				</tr>
				</s:iterator>
			</table>
		  </div>
		</div>
		</div>
	  </div>
	</div>
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

function importData(){
	setTimeout(function(){
		var phones = $(".phoneTd").text();
		if(phones == ""){
			alert("没有可以导入的数据！");
			return;
		}
		$.ajax({
	        type: "post", 
	        url: "/office/importData.do", 
	        cache: false,  
	        async: false,
	        data:{"phones":phones},
	        beforeSend: function () {
	            openProgressBar();
	        },
	        complete: function () {
	            closeProgressBar();
	        },
	        success : function(data){
	        	 alert(data);
	        },
	        error: function(){alert("系统错误");},
	  	});
	}, 200);
}

//打开进度条
function openProgressBar() {
	console.log('openProgressBar')
    var h = $(document).height();
    $(".showbox").css({ "z-index": "99999" });
    $(".overlay").css({ "height": h });
    $(".overlay").css({ 'display': 'block', 'opacity': '0.8' });
    $(".showbox").stop(true).animate({ 'margin-top': '300px', 'opacity': '1' }, 200);
}
//关闭进度条
function closeProgressBar() {
	console.log('closeProgressBar')
    $(".showbox").css({ "z-index": "-99999" });
    $(".showbox").stop(true).animate({ 'margin-top': '250px', 'opacity': '0' }, 400);
    $(".overlay").css({ 'display': 'none', 'opacity': '0' });
}
</script>
</body>
</html>