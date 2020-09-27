package dfh.service.implementations;

import dfh.model.*;
import dfh.model.enums.ActionLogType;
import dfh.service.interfaces.*;
import dfh.skydragon.webservice.model.LoginInfo;
import dfh.utils.*;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserWebServiceImpl implements IUserWebService {
	private CustomerService cs;
	private ILogin login;
	private IMemberSignrecord memberService;
	private LoginTokenService tokenService;
	private SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Integer MAX_LOGIN_ERROR_TIMES = 5;
	private static Logger log = Logger.getLogger(UserWebServiceImpl.class);
	@Override
	public LoginInfo userLogin(String loginname, String password, String remoteip, String city,String clientos, String ioBB,Boolean loginPt) {
		ioBB="";
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setView("input");
		String regcity = "";
		try {
			Userstatus userstatus = null;
			Users olduser = null;
			if(loginname!=null){
				
				DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
				dc.add(Restrictions.eq("loginname", loginname));
				List<Users> oldusers = cs.findByCriteria(dc);
				if(oldusers == null || oldusers.size() == 0 || !oldusers.get(0).getLoginname().toLowerCase().equals(loginname.toLowerCase())){
					loginInfo.setMsg("该会员不存在");
					return loginInfo;
				}
				olduser = oldusers.get(0);
				
				userstatus = (Userstatus)cs.get(Userstatus.class,loginname);
//				olduser = (Users)cs.get(Users.class, loginname);
				
				ServletContext sc = (ServletContext)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
				IPSeeker seeker = (IPSeeker) sc.getAttribute("ipSeeker");
				String temp="";
				try {
					temp = olduser.getRegisterIp()!=null && !"".equals(olduser.getRegisterIp()) ? seeker.getAddress(olduser.getRegisterIp()) : "";
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null != temp && !"".equals(temp) && !"CZ88.NET".equals(temp)) {
					regcity = temp;
				}
			}
			if(null == userstatus || null == userstatus.getLoginerrornum()||userstatus.getLoginerrornum()<5){
				Users user = login.login(olduser,loginname, password, remoteip,city,regcity,clientos,ioBB);
				if (user==null) {
					loginInfo.setMsg(login.getErrorMessage());
					if("密码错误".equals(login.getErrorMessage())){
						if(userstatus  == null){
							userstatus = new Userstatus(loginname,1,1);
							userstatus.setCashinwrong(0);
						}else{
							if(null == userstatus.getLoginerrornum()){
								userstatus.setLoginerrornum(1);
							}else{
								userstatus.setLoginerrornum(userstatus.getLoginerrornum()+1);
							}
						}
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(remoteip);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.WRONG_PWD.getCode());
						//actionlog.setAction(ActionLogType.getText("WRONG_PWD"));
						cs.modifyUserstatus(userstatus,actionlog);
					}
				}else{
					//同步账号到pt
//					if(!loginPt){
						/*if(user.getRole().equals("MONEY_CUSTOMER") && user.getPtlogin()==0){
						PtThread ptThread=new PtThread(memberService,user,loginname,password,"CREATE");
						ptThread.start();	
					}else if(user.getRole().equals("MONEY_CUSTOMER") && user.getPtlogin()==1){
						PtThread ptThread=new PtThread(memberService,user,loginname,password,"UPDATE");
						ptThread.start();	
					}*/
					
						if(user.getRole().equals("MONEY_CUSTOMER") && user.getCreatetime().compareTo(DateUtil.parseDateForStandard("2017-01-11 09:00:00")) < 0){
							PtThread ptThread=new PtThread(cs,user,loginname,password,"CREATE");
							ThreadPoolUtil.getInstance().add(ptThread);
							
							/*DtThread dtThread= new DtThread(loginname, password);
	                        ThreadPoolUtil.getInstance().add(dtThread);*/
						}
//					}
					if(password.length()<8){
						loginInfo.setMsg("你的密码长度小于8位，为保证安全，请你及时修改密码");
					}
					if(userstatus != null){
						userstatus.setLoginerrornum(0);
						cs.modifyUserstatus(userstatus,null);
					}
					//memberService.login(user.getLoginname());
					
					user.setTempCreateTime(sf.format(user.getCreatetime()));
					user.setTempLastLoginTime(sf.format(user.getLastLoginTime()));
					loginInfo.setSucFlag(1);
					loginInfo.setUser(user);
					
					//代理地址www.处理
					if(user.getRole().equals("AGENT")){
						DetachedCriteria dc = DetachedCriteria.forClass(AgentAddress.class);
						dc = dc.add(Restrictions.eq("loginname",loginname));
						dc = dc.add(Restrictions.eq("deleteflag",0));
						List<AgentAddress> list = cs.findByCriteria(dc);
						String tmpA;String tmpS;
						for(AgentAddress aa : list){
							tmpA = aa.getAddress();
							if(tmpA.contains("www.")){
								tmpS = tmpA.replace("www.", "");
							}else{
								tmpS = tmpA.replace("http://", "http://www.");
							}
							AgentAddress adrtmp = (AgentAddress)cs.get(AgentAddress.class, tmpS);
							if(adrtmp == null){
								AgentAddress agentAddressW = new AgentAddress();
								agentAddressW.setLoginname(loginname);
								agentAddressW.setAddress(tmpS);
								agentAddressW.setDeleteflag(0);
								agentAddressW.setCreatetime(new Date());
								cs.save(agentAddressW);
							}
						}
					}
					/*if(null != ourDeviceID && !ourDeviceID.equals("")){
						CpuCompareUtil cpuCompareUtil = new CpuCompareUtil(cs, loginname, remoteip, null, ourDeviceID, "登录");
						cpuCompareUtil.start();
					}*/
					//新会员存款满三笔，升为忠实会员
					/*if(user.getLevel().equals(VipLevel.NEWMEMBER.getCode())){
						AutoUpdateLevel autoUpdateLevel = new AutoUpdateLevel(user, memberService);
						autoUpdateLevel.start();
					}*/
					//iesnare
					if(ioBB != null && !ioBB.equals("")){
//						IESnareUtil iesnare = new IESnareUtil(cs, loginname, remoteip, ioBB);
//				    	iesnare.start();
				    	//根据cpuid获取对应的所有账号，把除了当前登录账号以外的其它账号都冻结了
			/*	    	if(getCpuDjzhkg()){
				    	String result=IESnareUtil.getDevice(loginname, remoteip, ioBB);
				    	List<IESnare> iesnares = cs.getAllIESanre(result);
				    	if(null!=iesnares&&iesnares.size()>0){
				    		for(IESnare ies :iesnares){
				    			String  loginNameDj=ies.getLoginname();
				    			if(!loginname.equals(loginNameDj)){
				    				cs.lockUser(loginNameDj);
				    				log.info(result+"------因为cpuID重复，导致用户"+loginNameDj+"被冻结！");
				    			}
				    		}
				    	}
					}*/
					}
				}
			}else{
				cs.lockUser(loginname);
				loginInfo.setMsg("抱歉的通知你，您的账户由于多次输入密码错误已被锁 ,请使用重置密码并解除锁定功能 ！");
				loginInfo.setView("reLogin");
				//记入会员事件记录
				Actionlogs actionlog = new Actionlogs();
				actionlog.setLoginname(loginname);
				actionlog.setRemark(remoteip);
				actionlog.setCreatetime(DateUtil.now());
				actionlog.setAction(ActionLogType.PWD_ERROR_5.getCode());
				cs.save(actionlog);
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginInfo.setMsg(e.getMessage());
		}
		return loginInfo;
	}
	public void setCs(CustomerService cs) {
		this.cs = cs;
	}
	public void setLogin(ILogin login) {
		this.login = login;
	}
	public void setMemberService(IMemberSignrecord memberService) {
		this.memberService = memberService;
	}
	public void setTokenService(LoginTokenService tokenService) {
		this.tokenService = tokenService;
	}
	//检查用户是否存在
	@Override
	public Boolean checkUserIsExit(String loginname) {
		if(null!=cs.get(Users.class, loginname)){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public LoginInfo userRegister(String howToKnow, Integer sms, String loginname,
			String pwd, String accountName, String aliasName, String phone,
			String email, String qq, String referWebsite, String ipaddress,String city,String birthday,
			String intro ,String agentcode,String ioBB, String microchannel) {
		String msg = cs.register(howToKnow,sms,loginname,pwd,accountName,
				aliasName,phone,email,qq, referWebsite, ipaddress,city,birthday,intro,agentcode,ioBB,microchannel);
		LoginInfo loginInfo = new LoginInfo();
		if(null==msg){
			loginInfo.setMsg("注册成功");
			loginInfo.setView("success");
		}else{
			loginInfo.setMsg("抱歉注册失败 ," + msg);
			loginInfo.setView("input");
		}
		return loginInfo;
	}
	
	public LoginInfo userRegisterTwo(String howToKnow, Integer sms, String loginname,
			String pwd, String accountName, String aliasName, String phone,
			String email, String qq, String referWebsite, String ipaddress,String city,String birthday,
			String intro,Double gifTamount,String agentStr,String type,String ioBB,String microchannel) {
		String msg = cs.registerTwo(howToKnow,sms,loginname,pwd,accountName,
				aliasName,phone,email,qq, referWebsite, ipaddress,city,birthday,intro,gifTamount,agentStr,type,ioBB,microchannel);
		LoginInfo loginInfo = new LoginInfo();
		if(null==msg){
			loginInfo.setMsg("注册成功");
			loginInfo.setView("success");
		}else{
			loginInfo.setMsg("抱歉注册失败 ," + msg);
			loginInfo.setView("input");
		}
		return loginInfo;
	}
	
	@Override
	public void saveBbs(String loginname) {
		try {
			memberService.login(loginname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void logout(String loginname) {
		try {
			memberService.logout(loginname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 登录时   根据CPU是否重复来冻结账号
	 * @return
	 */
	private Boolean getCpuDjzhkg() {
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		dc = dc.add(Restrictions.eq("id","cpudjzh"));
		dc = dc.add(Restrictions.eq("value","1"));
		List<Const> list =cs.findByCriteria(dc);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public String loginJc(String loginname, String password) {
		try {
			String rbody = JCUtil.regAndGetLoginKeyJC(loginname, password);
			//JSONObject json = JSONObject.parseObject(rbody);
			String uuid = rbody.substring(rbody.indexOf("+")+1);
			if (!rbody.startsWith("j10000") || StringUtils.isEmpty(uuid)){
				return null;
			}
			return uuid;
		} catch (Exception e) {
			log.error("UserWebServiceImpl-->loginJc:",e);
		}
		return null;
	}
	
	@Override
	public String loginNT(String loginname) {
		return NTUtils.loginNTGame(loginname);
	}
	
	@Override
	public String loginGWNT(String loginname, String gameCode) {
		return NTUtils.loginNTGame(loginname, gameCode);
	}
	
	@Override
	public LoginInfo nTwoTicketlogin(String loginname, String token, String remoteip, String city,String clientos) {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setView("input");
		String regcity = "";
		try {
			Userstatus userstatus = null;
			Users olduser = null;
			if (!tokenService.isValidToken(NTwoUtil.PLATFORM, token,loginname)) {//token is not work
				loginInfo.setMsg("token验证失败 请重新确认");
				return loginInfo;
			}
			if(loginname!=null){
				userstatus = (Userstatus)cs.get(Userstatus.class,loginname);
				olduser = (Users)cs.get(Users.class, loginname);
				
				ServletContext sc = (ServletContext)MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
				IPSeeker seeker = (IPSeeker) sc.getAttribute("ipSeeker");
				String temp="";
				try {
					temp = olduser.getRegisterIp()!=null && !"".equals(olduser.getRegisterIp())
							?seeker.getAddress(olduser.getRegisterIp()):"";
				} catch (Exception e) {}
				if (null != temp && !"".equals(temp) && !"CZ88.NET".equals(temp)) {
					regcity = temp;
				}
			}
			if(null == userstatus || null == userstatus.getLoginerrornum()||userstatus.getLoginerrornum()<5){
				Users user = login.nTwoTicketlogin(olduser,loginname, remoteip,city,regcity,clientos);
				if (user==null) {
					loginInfo.setMsg(login.getErrorMessage());
					if("密码错误".equals(login.getErrorMessage())){
						if(userstatus  == null){
							userstatus = new Userstatus(loginname,1,1);
							userstatus.setCashinwrong(0);
						}else{
							if(null == userstatus.getLoginerrornum()){
								userstatus.setLoginerrornum(1);
							}else{
								userstatus.setLoginerrornum(userstatus.getLoginerrornum()+1);
							}
						}
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(remoteip);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.WRONG_PWD.getCode());
						//actionlog.setAction(ActionLogType.getText("WRONG_PWD"));
						cs.modifyUserstatus(userstatus,actionlog);
					}
				}else{
					if(userstatus != null){
						userstatus.setLoginerrornum(0);
						cs.modifyUserstatus(userstatus,null);
					}
					
					user.setTempCreateTime(sf.format(user.getCreatetime()));
					user.setTempLastLoginTime(sf.format(user.getLastLoginTime()));
					loginInfo.setSucFlag(1);
					loginInfo.setUser(user);
					
					//代理地址www.处理
					if(user.getRole().equals("AGENT")){
						DetachedCriteria dc = DetachedCriteria.forClass(AgentAddress.class);
						dc = dc.add(Restrictions.eq("loginname",loginname));
						dc = dc.add(Restrictions.eq("deleteflag",0));
						List<AgentAddress> list = cs.findByCriteria(dc);
						String tmpA;String tmpS;
						for(AgentAddress aa : list){
							tmpA = aa.getAddress();
							if(tmpA.contains("www.")){
								tmpS = tmpA.replace("www.", "");
							}else{
								tmpS = tmpA.replace("http://", "http://www.");
							}
							AgentAddress adrtmp = (AgentAddress)cs.get(AgentAddress.class, tmpS);
							if(adrtmp == null){
								AgentAddress agentAddressW = new AgentAddress();
								agentAddressW.setLoginname(loginname);
								agentAddressW.setAddress(tmpS);
								agentAddressW.setDeleteflag(0);
								agentAddressW.setCreatetime(new Date());
								cs.save(agentAddressW);
							}
						}
					}
				}
			}else{
				cs.lockUser(loginname);
				loginInfo.setMsg("抱歉的通知你，您的账户由于多次输入密码错误已被锁 ,请使用重置密码并解除锁定功能 ！");
				loginInfo.setView("reLogin");
				//记入会员事件记录
				Actionlogs actionlog = new Actionlogs();
				actionlog.setLoginname(loginname);
				actionlog.setRemark(remoteip);
				actionlog.setCreatetime(DateUtil.now());
				actionlog.setAction(ActionLogType.PWD_ERROR_5.getCode());
				cs.save(actionlog);
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginInfo.setMsg(e.getMessage());
		}
		return loginInfo;
	}

	@Override
	public LoginInfo loginByAccessToken(String platform, String accessToken, String remoteIp, String city, String clientOs,String ioBb) {
		log.info("login from EBetApp , accessToken = " + accessToken + ", remoteIp = " + remoteIp + ", city  = " + city + ", clientOS = " + clientOs);
		String loginName = "";
		try {
			if (!tokenService.isValidToken(platform,accessToken,null)) {
				throw new IllegalAccessException("login by Token fail-- 无效Token : accessToken = " + accessToken + " , loginName  = " + loginName);
			}
			TokenInfo tokenInfo = tokenService.getInfo(accessToken);
			loginName = tokenInfo.getLoginname();
			Users userData = (Users) cs.get(Users.class, loginName);
			return loginByEncryptPassword(loginName, userData.getPassword(), remoteIp, city, clientOs, ioBb);
		} catch (IllegalAccessException e) {
			log.warn(e.getMessage());
			return buildFailInfo(e.getMessage(), "input");
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailInfo(e.getMessage(), "input");
		}
	}

	@Override
	public LoginInfo loginByEncryptPassword(String loginName, String password, String remoteIp, String city, String clientOs,String ioBb) {
		try {
			Userstatus userStatus = (Userstatus) cs.get(Userstatus.class, loginName);
			Users userData = (Users) cs.get(Users.class, loginName);
			String regCity = getRegisterCity(userData);

			validateRequest(loginName, password, userData);
			if (LoginTooMuch(userStatus)) {
				lockAccount(loginName, remoteIp);
				return buildFailInfo("账号锁定 , 抱歉的通知你，您的账户由于多次输入密码错误已被锁 ,请使用重置密码并解除锁定功能 ！", "reLogin");
			}

			Users user = login.loginByEncryptPassword(userData, loginName, password, remoteIp, city, regCity, clientOs, ioBb);

			if (user == null) {
				if ("密码错误".equals(login.getErrorMessage())) {
					addLoginErrorNum(loginName, userStatus, remoteIp);
				}
				return buildFailInfo(login.getErrorMessage(), "index");
			}
			updateLoginSuccess(userStatus);
			user.setTempCreateTime(sf.format(user.getCreatetime()));
			user.setTempLastLoginTime(sf.format(user.getLastLoginTime()));
			return buildSuccessInfo(user, "input");
		}  catch (Exception e) {
			e.printStackTrace();
			return buildFailInfo(e.getMessage(), "input");
		}
	}

	private void updateLoginSuccess(Userstatus userStatus) {
		if (userStatus != null) {
			userStatus.setLoginerrornum(0);
			cs.modifyUserstatus(userStatus, null);
		}
	}

	@Override
	public LoginInfo loginByPassword(String loginName, String password, String remoteIp, String city, String clientOs,String ioBb) {
		try {
			Userstatus userStatus = (Userstatus) cs.get(Userstatus.class, loginName);
			Users userData = (Users) cs.get(Users.class, loginName);
			String regCity = getRegisterCity(userData);

			validateRequest(loginName, password, userData);
			if (LoginTooMuch(userStatus)) {
				lockAccount(loginName, remoteIp);
				return buildFailInfo("抱歉的通知你，您的账户由于多次输入密码错误已被锁 ,请使用重置密码并解除锁定功能 ！", "reLogin");
			}

			Users user = login.login(userData, loginName, password, remoteIp, city, regCity, clientOs, ioBb);

			if (user == null) {
				if ("密码错误".equals(login.getErrorMessage())) {
					addLoginErrorNum(loginName, userStatus, remoteIp);
				}
				return buildFailInfo(login.getErrorMessage(), "index");
			}
			updateLoginSuccess(userStatus);
			user.setTempCreateTime(sf.format(user.getCreatetime()));
			user.setTempLastLoginTime(sf.format(user.getLastLoginTime()));
			return buildSuccessInfo(user, "input");

		} catch (Exception e) {
			e.printStackTrace();
			return buildFailInfo(e.getMessage(), "index");
		}
	}

	private void validateRequest(String loginName, String password, Users userData) throws IllegalArgumentException {
		if (loginName == null || loginName.isEmpty()) {
			throw new IllegalArgumentException("Illegal loginName :" + loginName);
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("无效的密码");
		}
		if (userData == null) {
			throw new IllegalArgumentException("账号不存在");
		}
	}

	private boolean LoginTooMuch(Userstatus userstatus) {
		return null != userstatus
				&& null != userstatus.getLoginerrornum()
				&& userstatus.getLoginerrornum() > MAX_LOGIN_ERROR_TIMES;
	}

	private void lockAccount(String loginName, String remoteIp) {
		cs.lockUser(loginName);
		//记入会员事件记录
		Actionlogs logs = new Actionlogs();
		logs.setLoginname(loginName);
		logs.setRemark(remoteIp);
		logs.setCreatetime(DateUtil.now());
		logs.setAction(ActionLogType.PWD_ERROR_5.getCode());
		cs.save(logs);
	}

	private LoginInfo buildFailInfo(String errorMessage, String view) {
		LoginInfo info = new LoginInfo();
		info.setMsg(errorMessage);
		info.setSucFlag(0);
		info.setView(view);
		return info;
	}

	private LoginInfo buildSuccessInfo(Users user, String view) {
		LoginInfo info = new LoginInfo();
		info.setView(view);
		info.setSucFlag(1);
		info.setUser(user);
		return info;
	}

	public String getRegisterCity(Users userData) {
		IPSeeker ipSeeker = getIpSeeker();
		String registerIp = userData.getRegisterIp() != null
				&& !"".equals(userData.getRegisterIp()) ? ipSeeker.getAddress(userData.getRegisterIp()) : "";
		return null != registerIp && !"".equals(registerIp) && !"CZ88.NET".equals(registerIp) ? registerIp : "";
	}

	private IPSeeker getIpSeeker() {
		ServletContext context = (ServletContext) MessageContext.getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
		return (IPSeeker) context.getAttribute("ipSeeker");
	}

	public void addLoginErrorNum(String loginName, Userstatus userstatus, String remoteIp) {
		if (userstatus == null) {
			userstatus = new Userstatus(loginName, 1, 1);
			userstatus.setCashinwrong(0);
		} else {
			if (null == userstatus.getLoginerrornum()) {
				userstatus.setLoginerrornum(1);
			} else {
				userstatus.setLoginerrornum(userstatus.getLoginerrornum() + 1);
			}
		}
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginName);
		actionlog.setRemark(remoteIp);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.WRONG_PWD.getCode());
		cs.modifyUserstatus(userstatus, actionlog);
	}

}
