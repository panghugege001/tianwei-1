<%@page pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Elufa Office(<s:property value="#session.operator.username"/>--<s:property value="#session.operator.authority"/>)</title>
<style type="text/css">
.navPoint {
	COLOR: white;
	CURSOR: hand;
	FONT-FAMILY: Webdings;
	FONT-SIZE: 9pt
}
</style>
<script type="text/javascript" src="/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
<script type="text/javascript" src="/js/jquery.messager.js"></script>
<script type="text/javascript">

var isClick=false;
function switchSysBar(){
  if (isClick){
  		isClick=false;
         switchPoint.src="/img/right_button.gif";
          document.all("frmTitle").style.display="none"
  }
  else{
  		isClick=true;
          switchPoint.src="/img/left_button.gif";
          document.all("frmTitle").style.display=""
  }
}

var $j = jQuery.noConflict();
var node ;
function gtDepositAmountAlert(){
    var adminUser="${sessionScope.operator.authority}";
    if(adminUser==""){
        return;
    }
    if(adminUser=="finance" || adminUser=="finance_manager"){
        $.ajax({
		url:"/bankinfo/gtDepositAmount.do?time="+new Date(),
		success:function(response){
		
			if(""!=response){
				$j.messager.lays(400, 200);
				
				if(0==$("#kaiguanhome").val()){
				$j.messager.show(0,response
				+"<input type='button' value='stop music' onclick='stopMusic()'>",15000);
				}else if(1==$("#kaiguanhome").val()){
				$j.messager.show(0,response
				+"<input type='button' value='start music' onclick='startMusic()'>",15000);
				}
				
	  			node=document.getElementById("tixinghome");
	  			var kaiguan=$("#kaiguanhome").val();
	  			if(node!=null && kaiguan==0)
	  			{  
	     			node.Play();  
	  			}
			}
		}
	  });
    }
}
function stopMusic(){
	var kaiguan = $("#kaiguanhome").val();
	if(node && kaiguan==0){
	    $("#kaiguanhome").val(1);
		node.stop();
	}
}
function startMusic(){
	var kaiguan = $("#kaiguanhome").val();
	if(node && kaiguan==1){
	    $("#kaiguanhome").val(0);
	}
}
window.setInterval("gtDepositAmountAlert()",30000);
</script>
</head>

<body scroll="no" style="MARGIN: 0px;background:#b6d9e4;">
<s:hidden id="kaiguanhome" name="kaiguan" value="0"></s:hidden>
<embed src="/img/alert2.mp3" autostart=false  volume=99  id="tixinghome" width="0" height="0" style="display: block;" ></embed>
		<table border="0" cellPadding="0" cellSpacing="0" height="100%" width="100%">
			<tbody>
				<tr>
				 <td align="center" id="frmTitle" name="fmTitle">
				   <iframe frameBorder="0" id="left" name="leftFrame" src="<c:url value='/office/left.jsp' />"
							style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 240px; Z-INDEX: 2"></iframe>
				 </td>
				 <td style="width: 10pt">
					<table bgColor="#425bb8" border="0" cellPadding="0" cellSpacing="0" height="100%">
								<tbody>
									<tr>
										<td onclick=switchSysBar() style="HEIGHT: 100%">
											<img id="switchPoint" title="隐藏显示" width="10px"  src="/img/left_button.gif"/>
										</td>
									</tr>
								</tbody>
							</table>
				   </td>
				   <td style="width:100%">
						<iframe frameBorder="0" id="frmright" name="mainFrame"
								src="<c:url value='/office/welcome.jsp' />"
								style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 1">
							</iframe>
				   </td>
				</tr>
			</tbody>
		</table>
	</body>
	<c:import url="/office/script.jsp" />
</html>


