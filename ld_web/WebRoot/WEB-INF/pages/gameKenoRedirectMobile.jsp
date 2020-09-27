<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.remote.bean.KenoResponseBean"%>
<%@page import="dfh.remote.DocumentParser"%>
<html>
  <head>
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body>
    <%
    	//用户名
		HttpSession chksession=request.getSession(true);
		Users user =(Users)chksession.getValue("customer");
		String loginname="";
		String url="";
		String loginid = "";
		if(user != null){
			loginname = user.getLoginname();
			loginid =  AxisSecurityEncryptUtil.generateLoginKenoID(loginname);
			String xml=AxisSecurityEncryptUtil.kenoCheckOrCreateGameAccount(loginname,user.getAliasName(),request.getRemoteAddr(),loginid,"");
			KenoResponseBean bean= DocumentParser.parseKenologinResponseRequest(xml);
			System.out.print(bean);
			if(bean!=null){
				if(bean.getName()!=null && bean.getName().equals("Link")){
					url="http://"+bean.getValue();
				}else{
					url="/mobile/index.jsp";
				}
			}
		}
		System.out.println(url);
     %>
      <form action="<%=url%>" method="post" id="frm1">
     	 <input type="hidden" name="loginname" id="loginname" value="<%=loginname%>" />
     </form>
     <script language="javascript">
      var loginname = document.getElementById("loginname").value;
	  if(loginname==null || loginname == ""){ 
		alert("请先登录龙都帐号再登录游戏");
		window.location.href="index.asp";
	  }else{
	  		window.location.href="<%=url%>";
	  }
	  window.location.href="<%=url%>";
    </script>
  </body>
</html>
