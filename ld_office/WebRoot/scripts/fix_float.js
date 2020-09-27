// 
autoAdAdjust = true; // if true then must give rightAdWidth;
rightAdWidth = 156;

// begin allfly

function liumeiti_FSCommand(command,args){
    liumeiti_DoFSCommand(command,args);
}


isIE     = (document.all)?true:false;
isNS     = (document.layers)?true:false;
isGecko = (document.getElementById)?true:false;

screen.width>800?leftsuspentl=30:leftsuspentl=leftmargin1;
//screen.width>800?rightsuspentl=885:rightsuspentl=rightmargin1;
screen.width>800?rightsuspentl=845:rightsuspentl=rightmargin1;

function flashcode(idname,flashurl,flshlocation){
    if (eval(idname.substring(0,idname.length-"suspent".length)+"flytransparency") == "yes"){
        flytransparency1="<param name=wmode value=transparent>";
        flytransparency2=" wmode=transparent ";}
    else{
        flytransparency1="";flytransparency2="";
    }    
    suspendcode="<DIV width=130 id=" + idname + " style='LEFT:" + flshlocation + "px; POSITION: absolute; TOP: 170px; z-index:2'>";
    suspendcode=suspendcode + "<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0' width='130' height='40' id='" + idname + "1'>";
    suspendcode=suspendcode + "<param name=movie value='" + flashurl + "'>";
    suspendcode=suspendcode + "<param name=quality value=autohigh>";
    suspendcode=suspendcode + flytransparency1;
    suspendcode=suspendcode + "<embed src='" + flashurl + "' quality=autohigh pluginspage='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash' type='application/x-shockwave-flash' width='130' height='40'>";
    suspendcode=suspendcode + "</embed></object></div>";
    nsuspentcode="<layer id=" + idname + " left=" + flshlocation + " top=170 width=130 height=40>";
    nsuspentcode=nsuspentcode + "<EMBED src='" + flashurl + "' quality=autohigh  WIDTH=130 HEIGHT=40 TYPE='application/x-shockwave-flash' " + flytransparency2 + "></EMBED></layer>";
    if(isIE || isGecko){            //Explorer 4,5,6 || Netscape 6,7,Gecko.
        document.write(suspendcode); 
    }else if(isNS){        //Netscape 4
        document.write(nsuspentcode);  
    }
}

function gifcode(idname,giflink,gifurl,giflocation){
    suspendcode="<DIV width=156 id=" + idname + " style='LEFT:" + giflocation + "px; POSITION: absolute; TOP: 170px;'>";
    suspendcode=suspendcode + "<a href='" + giflink + "' target=_blank>";
    suspendcode=suspendcode + "<img border=0  src='" + gifurl + "' width='156' height='60'></a></DIV>";
    nsuspentcode="<layer id=" + idname + " left=" + giflocation + " top=170 width=156 height=60>";
    nsuspentcode=nsuspentcode + "<a href='" + giflink + "' target=_blank>";
    nsuspentcode=nsuspentcode + "<img border=0  src='" + gifurl + "' width='156' height='60'></a></layer>";
    if(isIE || isGecko){             //Explorer 4,5,6 || Netscape 6,7,Gecko.
        document.write(suspendcode); 
    }else  if(isNS){        //Netscape 4
        document.write(nsuspentcode);
    }
}

function liumeiticode(liumeitiurl,liumeitiformat){
    if(lmtransparency=="yes"){
      lmtransparency1="<param name=wmode value=transparent>";
      lmtransparency2=" wmode=transparent ";
    }else{
    lmtransparency1="";lmtransparency2="";
    }
    if(typeof(lmtleft1024) == "undefined")
    {
        lmtleft1024="540";
    }

    screen.width>800?lmtleft1=lmtleft1024:lmtleft1=lmtleft;
    screen.width>800?lmttop1=lmttop:lmttop1=lmttop;
     suspendcode="<div id=liumeitidiv style='POSITION: absolute; LEFT:" + lmtleft1 + "px; TOP:" + lmttop1 + "px; '>";
     suspendcode=suspendcode + "<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0'  width='" + lmtwidth + "' height='" + lmtheight + "'>";
     suspendcode=suspendcode + "<param name=movie value='" + liumeitiurl + "'>";
     suspendcode=suspendcode + "<param name=quality value=autohigh>";
     suspendcode=suspendcode + lmtransparency1;
     suspendcode=suspendcode + "<embed src='" + liumeitiurl + "' id=liumeiti quality=autohigh pluginspage='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash' type='application/x-shockwave-flash' width='" + lmtwidth + "' height='" + lmtheight + "'>";
     suspendcode=suspendcode + "</embed></object></div>";
     nsuspentcode="<layer id=liumeitidiv left=" + lmtleft + " top=" + lmttop + ">";
     nsuspentcode=nsuspentcode + "<EMBED src='" + liumeitiurl + "' id=liumeiti quality=autohigh  TYPE='application/x-shockwave-flash' " + lmtransparency2 + " width='" + lmtwidth + "' height='" + lmtheight + "'></EMBED></layer>";
     if(isIE || isGecko){            //Explorer 4,5,6 || Netscape 6,7,Gecko.
         document.write(suspendcode); 
         }else if(isNS){        //Netscape 4
             document.write(nsuspentcode);  
     }

     if(liumeitiformat=="right" && rightformat !="" ){ 
         if(isIE || isGecko){            //Explorer 4,5,6 || Netscape 6,7,Gecko.
          id_rightsuspent = document.getElementById("rightsuspent");
           id_rightsuspent.style.display="none";  
         }else if(isNS){        //Netscape 4
           document.leftsuspent.visibility="hide";
         }
         }         
     else if(liumeitiformat=="left" && leftformat !=""){
         if(isIE || isGecko){            //Explorer 4,5,6 || Netscape 6,7,Gecko.
          id_leftsuspent  = document.getElementById("leftsuspent");
           id_leftsuspent.style.display="none";  
         }else if(isNS){        //Netscape 4
           document.leftsuspent.visibility="hide";
         }
     }
     else{}
}

if(leftformat=="gif"){
   gifcode("leftsuspent",leftlinksuspent,leftpicsuspent,leftsuspentl);   }
else if(leftformat=="flash"){
   flashcode("leftsuspent",leftflashsuspent,leftsuspentl);  }
else{   }   
if(rightformat=="gif"){
   gifcode("rightsuspent",rightlinksuspent,rightpicsuspent,rightsuspentl)   }
else if(rightformat=="flash"){
   flashcode("rightsuspent",rightflashsuspent,rightsuspentl);  }   
else{    }  


if(liumeiti!=""){ liumeiticode(liumeiti,liumeitiformat) }
function liumeiti_DoFSCommand() {
   if(isIE || isGecko){            //Explorer 4,5,6 ||Netscape 6,7,Gecko.
    id_liumeitidiv = document.getElementById("liumeitidiv");
       id_liumeitidiv.style.visibility="hidden";
       id_special = document.getElementById("special");
       if(id_special){
           id_special.style.display="block"; 
               }
   }else if(isNS){        //Netscape 4
       document.liumeitidiv.visibility="hide"; 
   }
   if(liumeitiformat=="right" && rightformat !=""){       
       
       if(isIE || isGecko){        //Explorer 4,5,6 || Netscape 6,7,Gecko.
           id_rightsuspent = document.getElementById("rightsuspent");
           id_rightsuspent.style.display="block"; 
           
           }else if(isNS){        //Netscape 4
        document.rightsuspent.visibility="show";
    }
    }
   else if(liumeitiformat=="left" && leftformat !=""){ 
       if(isIE || isGecko){        //Explorer 4,5,6 || Netscape 6,7,Gecko.
           id_leftsuspent = document.getElementById("leftsuspent");
           id_leftsuspent.style.display="block"; 
    }else if(isNS){        //Netscape 4
        document.leftsuspent.visibility="show";
    }
   }
   else{   }
}

if(liumeititime==""){liumeititime=10000;}
if((liumeitiformat =="left" || liumeitiformat =="right"||liumeitiformat=="")&&liumeiti!=""){ setTimeout("liumeiti_DoFSCommand()",liumeititime);  }

lastScrollX = 0; 
lastScrollY = 0;
mm=0;
function heartBeat() {
    if(document.all || document.getElementById ) {                 //Explorer 4,5,6 || Netscape 6,7,Gecko.
        diffY =  parseInt(document.body.scrollTop) + parseInt(document.body.clientHeight) - parseInt(topleft);
        diffYr=  parseInt(document.body.scrollTop) + parseInt(document.body.clientHeight) - parseInt(topright);

    }else if(isNS) {            //Netscape 4
        diffY = self.pageYOffset+130; 
        diffX = self.pageXOffset; 
    }

    if(diffY != lastScrollY) {
      if(document.all || document.getElementById){    //Explorer 4,5,6 && Netscape 6,7,Gecko.
        if(leftformat !=""){
          id_leftsuspent = document.getElementById("leftsuspent");
          id_leftsuspent.style.top = diffY+"px";
      }
      if(rightformat !=""){
          id_rightsuspent = document.getElementById("rightsuspent");
          id_rightsuspent.style.top = diffY+"px";
        if(autoAdAdjust)    id_rightsuspent.style.left = parseInt(document.body.clientWidth)-parseInt(rightAdWidth)+"px";


      }
    }else if(isNS){            //Netscape 4
        if(leftformat !=""){
          document.leftsuspent.top = diffY; 
      }
      if(rightformat !=""){
        document.rightsuspent.top = diffY;
      }
    }
    }
}

action = window.setInterval("heartBeat()",500);
