<!--
	function swapimg(myimgNum,secNum,folderimg){
	    if (secNum.className=="off")
		{
			secNum.className="on";
	     	myimgNum.src="pics/tplus.gif";
	     	folderimg.src="pics/icon_folder.gif"
		}
	    else
		{
			secNum.className="off";
	     	myimgNum.src="pics/tminus.gif";
	     	folderimg.src="pics/icon_folderopen.gif"
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
			imgColl(i).src = "pics/icon_folder.gif"
			}
			else{
			imgColl(i).src = "pics/tplus.gif"
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
			imgColl(i).src = "pics/icon_folderopen.gif"
			}
			else{
			imgColl(i).src = "pics/tminus.gif"
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
menutool.style.display="block";


}
function hidemenu(){
menutool.style.display="none";
}
document.onclick = hidemenu;
document.onmousedown = doDown;
function load_page(link){
//parent.eimmanageframe.location.href=link
}

function load_Href(url,sign){
//parent.mailtreeframe.nav_Title.innerText=sign
parent.display.location.href=url
parent.messageframe.from.innerText=""
parent.messageframe.subject.innerText=""
parent.messageframe.mailmsgArea.location.href="about:blank"
parent.main_display.cols="*,12,140";
}

function js_openpage(url) {
	var newwin=window.open(url,"��Ϣ��ʾ","toolbar=no,resizable=yes,location=no,directories=no,status=no,menubar=no,scrollbars=yes,top=220,left=220,width=500,height=230");
	return false;
}

function onCoolInfo(){
	js_openpage("showmsg.jsp");
} 

function onCoolExit(){
	parent.display.location.href="logout.jsp";
}
-->