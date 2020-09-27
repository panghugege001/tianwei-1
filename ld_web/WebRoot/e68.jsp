<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<link rel ="stylesheet" type="text/css" href ="${ctx}/css/ceshi.css" />

<script type="text/javascript">
	tim=1
	setInterval("tim++",100)
	b=1
	var autourl=new Array()
	autourl[1]="www.e68.ph"
	autourl[2]="www.e68.cc"
	autourl[3]="www.e6866.com"
	autourl[4]="www.e68ph.net"
	autourl[5]="www.168.tl"
	autourl[6]="www.e68win.com"
	autourl[7]="www.e68.me"
          
		  
	function butt(){
		document.write("<div class='wp'><h3>域名线路测试：</h3><form name=autof>")
		for(var i=1;i<autourl.length;i++)document.write("<input type=text class='ceshi' name=txt"+i+" size=10 value=测试中……> =》<input type=text class='ceshi' name=url"+i+" size=40> =》	<input type=button value='前往网址' class='button' onclick=window.open(this.form.url"+i+".value)><br>")
		document.write("<input type=submit  class='button button1'  value=刷新></form></div>")
	}
	butt()
	function auto(url){
		document.forms[0]["url"+b].value=url
		if(tim>200){
			document.forms[0]["txt"+b].value="链接超时"
		}
		else{
			document.forms[0]["txt"+b].value="时间"+tim/10+"秒"
		}
			b++
		}
			function run(){
				for(var i=1;i<autourl.length;i++)document.write("<img src=http://"+autourl[i]+"/"+Math.random()+" width=1 height=1 onerror=auto('http://"+autourl[i]+"')>")
		}
		run()

</script>



<body>
<div class="">


</div>
</body>
</html>
