<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>删除返水数据</title>
        <link href="<c:url value='/css/excel.css' />" rel="stylesheet"
			type="text/css" />
		<link
			href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.21.custom.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-ui-1.8.21.custom.min.js"></script>
	</head>
<body>


<script type="text/javascript">

function modifyPlatformCommit(){
	if(confirm("数据删了不能恢复 ,确定？")){
		var a=document.getElementById("selectValueDelete").value; 
	 	$.post("/office/modifyPlatformRecord.do",{"platform":a},function(respData){
		alert(respData);
	  });
	}
}


function modifySinglePlatform(){
	if(confirm("数据删了不能恢复 ,确定？")){
		var a=document.getElementById("selectValue").value; 
	 	$.post("/office/modifySinglePlatform.do",{"platform":a},function(respData){
		alert(respData);
	  });
	}
}

function modifyAgSlotPlatform(){
	if(confirm("收录只能收录一小时的数据")){
		var startTime=document.getElementById("startTime").value; 
	 	$.post("/office/modifyAgSlotPlatform.do",{"startTime":startTime},function(respData){
		alert(respData);
	  });
	}
}


</script>


<div id="excel_menu_left">
操作  -->  修改返水数据 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<!-- onsubmit="submitonce(this);" -->

	<div id="JqAlert" title="温馨提示" style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);"></div>
          <p align="left">功能1:删除所有 提交的 提案返水记录</p>
          <p align="left">游戏平台:<s:select id="selectValueDelete" cssStyle="width:115px" list="#{'':'','EA':'ea','EBET':'ebt','AGIN':'agin','AGINFISH':'aginfish','AGINSLOT':'aginslot','PT':'pt','TTG':'ttg','QT':'qt','NT':'nt','EBETAPP':'ebetapp','SB':'sb','MG':'mg','DT':'dt','PNG':'png','SBA':'sba','n2live':'n2live','MWG':'mwg','PTSKY':'ptsky','SWFISH':'swfish','HYG':'hyg','761':'761','BBINELE':'bbin电子','BBINVID':'bbin真人','pb':'pb体育','fanya':'fanya','bit':'bit游戏','kyqp':'开元棋牌','vr':'vr官方彩','vrlive':'vr彩'}"
          name="platform" listKey="key" listValue="value" emptyOption="false"></s:select></p>
          <p align="left"><input type="button"  value="删除返水数据" align="left" onclick="modifyPlatformCommit()"/></p>
          <br> <br> <br>
          
           <p align="left">功能2:删除不洗码平台</p>
           <p align="left">游戏平台:<s:select id="selectValue" cssStyle="width:115px" list="#{'':'','ea':'ea','ag':'ag','agin':'agin','aginfish':'aginfish','aginslot':'aginslot','bbin':'bbin','pttiger':'pttiger','ptall':'ptall','kg':'kg','ssc':'ssc','pk10':'pk10','hk49':'hk49','sba':'sba','sixlottery':'sixlottery','ttg':'ttg','gpi':'gpi_3D','rslot':'gpi_rslot','png':'gpi_png','bs':'gpi_bs','ctxm':'gpi_ctxm','gpi_all':'gpi_all','bc':'bc','qt':'qt','ebetapp':'ebetapp','dt':'dt','mg':'mg','png':'png','n2live':'n2live','mwg':'mwg','PTSKY':'ptsky','swfish':'swfish','hyg':'hyg','761':'761','bbinele':'bbin电子','BBINVID':'bbin真人','pb':'pb体育','fanya':'fanya','bit':'bit游戏','kyqp':'开元棋牌','vr':'vr官方彩','vrlive':'vr彩'}"
           name="platform" listKey="key" listValue="value" emptyOption="false"></s:select></p>
          <p align="left"><input type="button"  value="删除返水数据" align="left" onclick="modifySinglePlatform()"/></p>
            <br> <br> <br>
          
          
          <p align="left">功能3:AG Slot 补录时间</p>
	      <p  align="left">开始时间:<s:textfield name="start" size="18" id="startTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="" /></p>
          <p align="left"><input type="button"  value="收录AG老虎机" align="left" onclick="modifyAgSlotPlatform()"/></p>
            <br> <br> <br>
          
          
          <p align="left">功能4:AG收录时间查询</p>
          <s:form action="getPlatformUpdateTime" namespace="/office" name="mainform" id="mainform" theme="simple">
	      <div id="excel_menu" style="text-align:center;">
	  <p align="left"><s:submit value="AG收录时间查询"></s:submit></p>
	
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
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >平台</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >创建时间</td>
					<td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >最后更新时间</td>
				</tr>
				<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
				<tr>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.platform"/></a></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.createTime"/></td>
					<td align="center"  style="font-size:13px;"><s:property value="#fc.lastupdateTime"/></td>
					
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

</div>

<c:import url="/office/script.jsp" />
</body>
</html>

