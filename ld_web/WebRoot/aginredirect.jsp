<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.utils.APInUtils"%>
<%@page import="dfh.remote.bean.DspResponseBean"%>
<%@page import="dfh.remote.DocumentParser"%>
<%@page import="dfh.service.interfaces.SeqService"%>
<%@page import="dfh.utils.SpringFactoryHepler"%>
<%@page import="dfh.security.EncryptionUtil"%>
<%@page import="dfh.security.DESEncrypt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>" />
    
    <title>My JSP 'agredirect.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />   
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
	<meta http-equiv="description" content="This is my page" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <%
   		//用户名
		HttpSession chksession=request.getSession(true);
		Users user =(Users)chksession.getValue("customer");
		String loginname="";
		String result="";
		String params="";
		String  encrypt_key="njvs90z1";
	 	String  md5encrypt_key="sdf7&^#gfas";
	 	String  cagent="B20_AGIN";
	 	String  dspurl="http://gi.sunrise88.net:81";
	 	String url="";
	 	String loginid = "";
	 	if(user != null){
	 		loginname = user.getLoginname();
	 		if(user.getRole().equals("MONEY_CUSTOMER")){
	 			DspResponseBean dspResponseBean =DocumentParser.parseDspResponseRequest( APInUtils.isCustomerExist(loginname));
	 			if(dspResponseBean !=null && dspResponseBean.getInfo().equals("0")){
	 				//检测是否有帐号，如果帐号不存在，则创建DSP帐号,0表示帐号不存在
	 				DspResponseBean createaccount=DocumentParser.parseDspResponseRequest( APInUtils.CheckOrCreateGameAccount(loginname,loginname));
	 				if(createaccount !=null && createaccount.getInfo().equals("0")){ //表示创建成功
	 				
	 					loginid =  AxisSecurityEncryptUtil.generateLoginID(loginname);
	 					if("".equals(loginid)){
	 						result = "系统繁忙，稍后重试！";
	 						return;
	 					}
						params="cagent="+cagent+"/\\\\/loginname=ki_"+loginname+"/\\\\/dm="+"152.101.114.206/"+"/\\\\/actype="+APInUtils.getActype(loginname)+"/\\\\/password=ki_"+loginname+"/\\\\/sid="+cagent+loginid;
						//System.out.println(params);
						DESEncrypt  des=new DESEncrypt(encrypt_key);
						String targetParams=des.encrypt(params);
						String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
						url=dspurl+"/forwardGame.do?params="+targetParams+"&key="+key;
	 				}else{
						result=createaccount.getInfo();
					}
	 			}else if(dspResponseBean !=null && dspResponseBean.getInfo().equals("1")){//表示已经存在改帐号
	 					loginid =  AxisSecurityEncryptUtil.generateLoginID(loginname);
	 					if("".equals(loginid)){
	 						result = "系统繁忙，稍后重试！";
	 						return;
	 					}
						params="cagent="+cagent+"/\\\\/loginname=ki_"+loginname+"/\\\\/dm="+"152.101.114.206/"+"/\\\\/actype="+APInUtils.getActype(loginname)+"/\\\\/password=ki_"+loginname+"/\\\\/sid="+cagent+loginid;
						//System.out.println(params);
						DESEncrypt  des=new DESEncrypt(encrypt_key);
						String targetParams=des.encrypt(params);
						String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
						url=dspurl+"/forwardGame.do?params="+targetParams+"&key="+key;
					}else{
					result=dspResponseBean.getInfo();
				}
	 		}else{
	 			result="代理不可以登录游戏";
	 		}
	 		
			//System.out.println(url);
	 	}
    %>
    
     <form action="<%=url%>" method="post" id="frm1">
     	 <input type="hidden" name="loginname" id="loginname" value="<%=loginname%>" />
     	 <input type="hidden" name="result" id="result" value="<%=result%>" />
     </form>
     <script language="javascript">
      var loginname = document.getElementById("loginname").value;
      var result = document.getElementById("result").value;
	  if(loginname==null || loginname == ""){ 
		alert("请先登录天威帐号再登录游戏");
		window.location.href="index.asp";
	  }else if(result !=""){
	  	alert("登录游戏失败，"+result);
		window.location.href="index.asp";
	  }else{
	  		document.getElementById("frm1").submit();
	  }
    </script>
  </body>
</html>
