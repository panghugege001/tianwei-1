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
<html>
  <head>
    <title></title>
  </head>
  <body style="text-align: center;">
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
	 	String  dspurl="https://gci.sunrise88.net";
	 	String url="";
	 	String loginid = ""; 
	 	if(user != null){
	 		loginname = user.getLoginname();
	 		if(user.getRole().equals("MONEY_CUSTOMER")){
	 			DspResponseBean dspResponseBean =DocumentParser.parseDspResponseRequest( AxisSecurityEncryptUtil.aginIsCustomerExist(loginname));
	 			if(dspResponseBean !=null && dspResponseBean.getInfo().equals("0")){
	 				//检测是否有帐号，如果帐号不存在，则创建DSP帐号,0表示帐号不存在
	 				DspResponseBean createaccount=DocumentParser.parseDspResponseRequest( AxisSecurityEncryptUtil.aginCheckOrCreateGameAccount(loginname,loginname));
	 				if(createaccount !=null && createaccount.getInfo().equals("0")){ //表示创建成功
	 					loginid =  AxisSecurityEncryptUtil.generateLoginID(loginname);
	 					if("".equals(loginid)){
	 						result = "系统繁忙，稍后重试！";
	 						return;
	 					}
						params="cagent="+cagent+"/\\\\/loginname=ki_"+loginname+"/\\\\/dm="+"152.101.114.206/"+"/\\\\/actype="+APInUtils.getActype(loginname)+"/\\\\/password=ki_"+loginname+"/\\\\/sid="+cagent+loginid+ "/\\\\/gameType=6";
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
					params="cagent="+cagent+"/\\\\/loginname=ki_"+loginname+"/\\\\/dm="+"152.101.114.206/"+"/\\\\/actype="+APInUtils.getActype(loginname)+"/\\\\/password=ki_"+loginname+"/\\\\/sid="+cagent+loginid+ "/\\\\/gameType=6";
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
		alert("请先登录龙都帐号再登录游戏");
		window.location.href="/index.asp";
	  }else if(result !=""){
	  	alert("登录游戏失败，"+result);
		window.location.href="/index.asp";
	  }else{
	  		document.getElementById("frm1").submit();
	  }
    </script>
  </body>
</html>