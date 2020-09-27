<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.remote.bean.DspResponseBean"%>
<%@page import="dfh.remote.DocumentParser"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <%
   		//用户名
		HttpSession chksession=request.getSession(true);
		Users user =(Users)chksession.getValue("customer");
		String loginname="";
		String result="";
	 	String url="";
	 	if(user != null){
	 		 loginname = user.getLoginname();
		 	 if(user.getRole().equals("MONEY_CUSTOMER")){
		 		DspResponseBean createaccount=DocumentParser.parseBBinDspResponseRequest( AxisSecurityEncryptUtil.bbinCheckOrCreateGameAccount(loginname));
		 		
		 		if(createaccount !=null && createaccount.getInfo().equals("21100")){ //表示创建成功
		 			//处理字符串,开始登录游戏
					url = AxisSecurityEncryptUtil.bbinForwardGame(loginname); 
					//System.out.println(url); 
				}else if(createaccount !=null && createaccount.getInfo().equals("21001")){//表示已经存在该帐号
					url = AxisSecurityEncryptUtil.bbinForwardGame(loginname);
				}else{
					if(createaccount.getInfo()!=null){
						result=createaccount.getInfo();
					}
				}
		 	}else{
		 		result="代理不可以登录游戏";
		 	}
	 	}
	 	if(loginname==null || "".equals(loginname)){
	 		out.print("<script>alert('请先登录龙都帐号再登录游戏');window.close();</script>");
	 		out.flush();
	 	}else if(result!=null && !"".equals(result)){
	 		out.print("<script>alert('登录游戏过程中出现问题**"+result+"**,请联系在线客服');window.close();</script>");
	 		out.flush();
	 	}else{
	 		response.sendRedirect(url);
	 	}
    %>
  </body>
</html>
