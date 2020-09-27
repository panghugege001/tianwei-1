<%@page pageEncoding="UTF-8" %>
<%@page import="dfh.model.Operator"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="dfh.security.Authorities"%>
<%@include file="/office/include.jsp" %>
<html>
  <head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/lefttree.css" />">
	<script type="text/javascript" >
	
	function swapimg(myimgNum,secNum,folderimg){
	    if (secNum.className=="off")
		{
			secNum.className="on";
	     	myimgNum.src="<c:url value='/images' />/pics/tplus.gif";
	     	folderimg.src="<c:url value='/images' />/pics/icon_folder.gif"
		}
	    else
		{
			secNum.className="off";
	     	myimgNum.src="<c:url value='/images' />/pics/tminus.gif";
	     	folderimg.src="<c:url value='/images' />/pics/icon_folderopen.gif"
		}
	  }
	function statu()
	{
		window.defaultStatus=' ';
	}
	//window.setInterval("statu()",10)
	
	function AllClose(){
	tempColl = document.all.tags("div");
	
		for (i=0; i<tempColl.length; i++) {
			if (tempColl(i).className == "off") {
			tempColl(i).className = "on"
			}
		}
	imgColl = document.all.tags("img");
		for (i=0; i<imgColl.length; i++) {
		
			if (imgColl(i).id != "") {
			if(imgColl(i).className == "havechild") {
			imgColl(i).src = "<c:url value='/images' />/pics/icon_folder.gif"
			}
			else{
			imgColl(i).src = "<c:url value='/images' />/pics/tplus.gif"
			}
			}
		}
	}
	
	function AllOpen(){
	tempColl = document.all.tags("div");
		for (i=0; i<tempColl.length; i++) {
			if (tempColl(i).className == "on") {
			tempColl(i).className = "off"
			}
		}
	imgColl = document.all.tags("IMG");	
		for (i=0; i<imgColl.length; i++) {
		
			if (imgColl(i).id != "") {
				if(imgColl(i).className == "havechild") {
					imgColl(i).src = "<c:url value='/images' />/pics/icon_folderopen.gif"
				}
				else{
					imgColl(i).src = "<c:url value='/images' />/pics/tminus.gif"
				}
			}
		}
	}

var Xpos = 1;
var Ypos = 1;
function doDown() {
Xpos = document.body.scrollLeft+event.x;
Ypos = document.body.scrollTop+event.y;
}

function showmenu(){
menutool.style.left=Xpos;
menutool.style.top=Ypos;
menutool.style.display="block"


}
function hidemenu(){
menutool.style.display="none"
}
document.onclick = hidemenu;
document.onmousedown = doDown;
function load_page(link){
//parent.eimmanageframe.location.href=link
}

function load_Href(url,sign){
//parent.mailtreeframe.nav_Title.innerText=sign
parent.display.location.href=url;
parent.messageframe.from.innerText="";
parent.messageframe.subject.innerText="";
parent.messageframe.mailmsgArea.location.href="about:blank";
parent.main_display.cols="*,12,140";
}

function js_openpage(url) {
	var newwin=window.open(url,"新窗口","toolbar=no,resizable=yes,location=no,directories=no,status=no,menubar=no,scrollbars=yes,top=220,left=220,width=500,height=230");
	return false;
}

function onCoolInfo(){
	js_openpage("showmsg.jsp");
} 

function onCoolExit(){
	parent.display.location.href="<c:url value='/office/functions/logout.jsp' />";
}
</script> 
</head>

<!-- 
<BODY  class=menubar leftMargin=4 
oncontextmenu=self.event.returnValue=false;showmenu() 
ondragstart=self.event.returnValue=false 
onselectstart=self.event.returnValue=false rightMargin=0 topMargin=8 onload="AllOpen();" style="background: #B6D9E4;">
 -->
<BODY  class=menubar leftMargin=4  rightMargin=0 topMargin=8 onload="AllOpen();" style="background: #FFCCCC;">
	<font style="font-weight: bold;">推荐码：</font><% dfh.model.Operator op = (dfh.model.Operator)session.getAttribute("operator");
			out.print(op.getUsername());
			out.flush();
		%>
<DIV><IMG src="<c:url value='/images' />/pics/icon_unctitle.gif"></DIV>
<c:url var="imgPath" value="/images" scope="request"></c:url>
<%
	Operator operator=(Operator)session.getAttribute(Constants.SESSION_OPERATORID);
	String html=Authorities.translateToHTMLCode(application.getAttribute("authoritiesXml").toString(),request.getContextPath(),operator.getAuthority());
	out.println(html);
%>
</BODY>
</HTML>
