<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
       
	<script type="text/javascript" src="/scripts/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
</head>
<body>


<script type="text/javascript">

function modifyMGPlatform(){
	if(confirm("请确定在操作!!!")){
		var date=document.getElementById("date").value; 
	 	$.post("/mgs/repairMGDataToKPI.do",{"date":date},function(respData){
		alert(respData);
	  });
	}
}


</script>


<div id="excel_menu_left">
操作  --> MG kpi 数据导入 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<!-- onsubmit="submitonce(this);" -->

	<div id="JqAlert" title="温馨提示" style="background-image: url(${pageContext.request.contextPath}/images/ylfw_63.jpg);"></div>
        
	      <p  align="left">日期:<s:textfield name="date" size="18" id="date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" My97Mark="true" value="%{yesterday}" /></p>
          <p align="left"><input type="button"  value="MG数据修复" align="left" onclick="modifyMGPlatform()"/></p>
            <br> <br> <br>              
          
 
	
	</div>
	<br/>
	<br/>
	<br/>
	


<c:import url="/office/script.jsp" />
</body>
</html>

