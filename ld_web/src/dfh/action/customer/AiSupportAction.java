package dfh.action.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import dfh.action.SubActionSupport;
import dfh.captcha_dc.model.Status;
import dfh.captcha_dc.service.TouClick;
import dfh.model.Userbankinfo;
import dfh.model.Users;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ILogin;
import dfh.service.interfaces.IMemberSignrecord;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.RequestLimitUtil;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.StringUtil;
/**
 * 智能客服需求可接口页面
 * @author ck
 *
 */
public class AiSupportAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(AiSupportAction.class);
	private static final long serialVersionUID = 1L;
	private CustomerService cs;
	private Map<String, Object> session;
	private String code;
	private String name;
	private String accountName;
	private String bankCard;
	private String bankName;
	private String loginname;
	private String errormsg;
	private String email;
	private String phone;
	private String password;
	private Integer id;
	private String content;
	private String new_content;
	private String bankno;
	private Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);


	/**
	 * 临时处理缓存短信验证密码找回
	 * @return
	 */
	private static Map<String  , Date> cacheDx = new HashMap<String, Date>();





	/**
	 * 手机号码验证
	 *
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}



	/**
	 * 账号解锁（记得原密码）
	 * @return
	 */
	public String unlockAccountByPassword(){
		try {

			if(!RequestLimitUtil.getInstance().checkRequestLimit("unlockAccountByPassword:" + this.getIp())){
				log.warn("unlockAccountByPassword:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}
			if (StringUtil.isEmpty(password)) {
				GsonUtil.GsonObject("密码不能为空!!!");
				return null;
			}

			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "Axis2WebService", false), AxisUtil.NAMESPACE, "unlockAccountByPassword",
					new Object[] {loginname,password}, String.class);
			if("success".equals(msg)){
				msg = "操作成功";
			}
			GsonUtil.GsonObject(msg);

		}catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系系统繁忙,请稍后再试，或者直接与客服联系！");
		}
		return null ;
	}

	/**
	 * 微信登录授权
	 */
	public void wechatAuth(){
		try {

			/*if (StringUtil.isEmpty(password)) {
				GsonUtil.GsonObject("密码不能为空!!!");
				return;
			}

			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "Axis2WebService", false), AxisUtil.NAMESPACE, "unlockAccountByPassword",
					     new Object[] {loginname,password}, String.class);
			if("success".equals(msg)){
				msg = "操作成功";
			}
			GsonUtil.GsonObject(msg);*/

		}catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系系统繁忙,请稍后再试，或者直接与客服联系！");
		}
	}

	/**
	 * 账号解锁（记得原密码）
	 * @return
	 */
	public void webAuth(){
		try {

			/*if (StringUtil.isEmpty(password)) {
				GsonUtil.GsonObject("密码不能为空!!!");
				return null;
			}

			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1+ "Axis2WebService", false), AxisUtil.NAMESPACE, "unlockAccountByPassword",
					     new Object[] {loginname,password}, String.class);
			if("success".equals(msg)){
				msg = "操作成功";
			}
			GsonUtil.GsonObject(msg);*/

		}catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系系统繁忙,请稍后再试，或者直接与客服联系！");
		}
	}

	/**
	 * 解绑密保
	 * @return
	 */
	public String unbindQuestion() {
		try {
			if(!RequestLimitUtil.getInstance().checkRequestLimit("unbindQuestion:" + this.getIp())){
				log.warn("unbindQuestion:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}
			refreshUserInSession();
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if(StringUtils.isEmpty(accountName)){
				GsonUtil.GsonObject("姓名不可");
				return null;
			}
			if(StringUtils.isEmpty(phone)){
				GsonUtil.GsonObject("手机号不能为空");
				return null;
			}
			if(StringUtils.isEmpty(email)){
				GsonUtil.GsonObject("邮箱不能为空");
				return null;
			}
			if(StringUtils.isEmpty(password)){
				GsonUtil.GsonObject("密码不能为空");
				return null;
			}

			String msg  = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"unbindQuestion", new Object[]{customer.getLoginname(),accountName,phone,email,password}, String.class);
			refreshUserInSession();
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙，请稍后操作");
		}
		// 判断是否登录
		return null;
	}

	/**
	 * 解绑密保
	 * @return
	 */
	public String unbindQuestionPassword() {
		try {
			if(!RequestLimitUtil.getInstance().checkRequestLimit("unbindQuestionPassword:" + this.getIp())){
				log.warn("unbindQuestionPassword:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}
			refreshUserInSession();
			Users customer = getCustomerFromSession();
			if (customer == null) {
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if(StringUtils.isEmpty(accountName)){
				GsonUtil.GsonObject("姓名不可");
				return null;
			}
			if(StringUtils.isEmpty(phone)){
				GsonUtil.GsonObject("手机号不能为空");
				return null;
			}
			if(StringUtils.isEmpty(email)){
				GsonUtil.GsonObject("邮箱不能为空");
				return null;
			}


			String msg  = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"unbindQuestion", new Object[]{customer.getLoginname(),accountName,phone,email,password}, String.class);
			refreshUserInSession();
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断是否登录
		return null;
	}



	/**
	 * 解绑银行卡
	 * @return
	 */
	/*public String unbindBankCard() {
		try {
			if(!RequestLimitUtil.getInstance().checkRequestLimit("unbindBankCard:" + this.getIp())){
				log.warn("unbindBankCard:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}

			if(StringUtils.isEmpty(bankCard)){
				GsonUtil.GsonObject("银行卡不能为空");
				return null;
			}

			if(StringUtils.isEmpty(accountName)){
				GsonUtil.GsonObject("姓名不能为空");
				return null;
			}
			if(StringUtils.isEmpty(phone)){
				GsonUtil.GsonObject("手机号不能为空");
				return null;
			}
			if(StringUtils.isEmpty(email)){
				GsonUtil.GsonObject("邮箱不能为空");
				return null;
			}

			String msg  = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"unbindBankCard", new Object[]{bankCard,bankName,accountName,phone,email,password}, String.class);
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断是否登录
		return null;
	}*/


	/**
	 * 账号解锁v2
	 * @return
	 */
	public String unlockAccountByInfo() {
		try {

			if(!RequestLimitUtil.getInstance().checkRequestLimit("unlockAccountByInfo:" + this.getIp())){
				log.warn("unlockAccountByInfo:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}
			if(StringUtils.isEmpty(loginname)){
				GsonUtil.GsonObject("账号不可为空");
				return null;
			}
			if(StringUtils.isEmpty(accountName)){
				GsonUtil.GsonObject("姓名不可为空");
				return null;
			}
			if(StringUtils.isEmpty(phone)){
				GsonUtil.GsonObject("手机号不能为空");
				return null;
			}
			if(StringUtils.isEmpty(email)){
				GsonUtil.GsonObject("邮箱不能为空");
				return null;
			}

			String msg  = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"unlockAccountByInfo", new Object[]{loginname,accountName,email,phone}, String.class);
			if("success".equals(msg)){
				msg = "操作成功";
			}
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙");
		}
		// 判断是否登录
		return null;
	}



	/**
	 * 忘记账号找回-短信
	 * @return
	 */
	public String getForgetAccbySms() {
		try {

			if(!RequestLimitUtil.getInstance().checkRequestLimit("getForgetAccbySms:" + this.getIp())){
				log.warn("getForgetAccbySms:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}
			if(StringUtils.isEmpty(phone)){
				GsonUtil.GsonObject("手机号不能为空");
				return null;
			}
			if(!isMobileNO(phone)){
				GsonUtil.GsonObject("手机号格式异常");
				return null;
			}

			String msg  = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"getForgetAccbySms", new Object[]{phone,this.getIp()}, String.class);

			GsonUtil.GsonObject(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断是否登录
		return null;
	}


	/**
	 * 忘记账号找回-邮件
	 */
	public String getForgetAccbyEmail() {
		try {

			if(!RequestLimitUtil.getInstance().checkRequestLimit("getForgetAccbyEmail:" + this.getIp())){
				log.warn("getForgetAccbyEmail:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return null;
			}
			if(StringUtils.isEmpty(email)){
				GsonUtil.GsonObject("邮件不能不为空");
				return null;
			}

			if (!p_email.matcher(email).matches()) {
				GsonUtil.GsonObject("电子邮箱格式错误");
				return null;
			}


			String msg  = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false), AxisUtil.NAMESPACE,
					"getForgetAccbyEmail", new Object[]{email,getIp()}, String.class);

			GsonUtil.GsonObject(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断是否登录
		return null;
	}



	/**
	 * 银行卡解绑
	 *//*
	public void unbundleBankno() {
			ReturnInfo ri = new ReturnInfo();
			Users user = (Users) session.get(Constants.SESSION_CUSTOMERID);
			if (user == null) {
				ri.setCode("-1");
				ri.setMsg("请登录后再操作!");
				GsonUtil.GsonObject(ri);
				return;
			}

			if (StringUtil.isBlank(accountName)) {
				ri.setCode("-2");
				ri.setMsg("真实姓名不能为空!");
				GsonUtil.GsonObject(ri);
				return;
			}
			if (StringUtil.isBlank(email)) {
				ri.setCode("-2");
				ri.setMsg("邮箱不能为空!");
				GsonUtil.GsonObject(ri);
				return;
			}
			if (StringUtil.isBlank(phone)) {
				ri.setCode("-2");
				ri.setMsg("手机号不能为空!");
				GsonUtil.GsonObject(ri);
				return;
			}
			if (id == null) {
				ri.setCode("-2");
				ri.setMsg("id不能为空!");
				GsonUtil.GsonObject(ri);
				return;
			}

			try {

				String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "Axis2WebService", false), AxisUtil.NAMESPACE,"checkSystemConfig",
						new Object[] { "type505", "001", "否" }, String.class);
				if (StringUtil.isEmpty(info)) {
					ri.setCode("-2");
					ri.setMsg("自助解绑银行卡维护中，请联系在线客服");
					GsonUtil.GsonObject(ri);
					return;
				}
				String remoteIp = getIp();
				String oldAccountName = user.getAccountName();
				String oldEmail = user.getEmail();
				String oldPhone = user.getPhone();
				if(!accountName.equals(oldAccountName)|| !email.equals(oldEmail) || !phone.equals(oldPhone)){
					ri.setCode("-2");
					ri.setMsg("操作不成功，您提交的信息与注册信息不匹配，请联系客服核实。");
					GsonUtil.GsonObject(ri);
					return;
				}

				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "Axis2WebService", false), AxisUtil.NAMESPACE,"unbundleBankno",
						new Object[] { id,user.getLoginname(),remoteIp}, String.class);

				if (null != result) {
					ri.setCode("-2");
					ri.setMsg(result);
					GsonUtil.GsonObject(ri);
					return;
				} else {
					ri.setCode("0");
					ri.setMsg("自助解绑成功");
					GsonUtil.GsonObject(ri);
					return;
				}
			} catch (Exception e) {
				log.error(e);
				ri.setCode("-2");
				ri.setMsg("系统繁忙,请稍后再试，或者直接与客服联系！");
				GsonUtil.GsonObject(ri);
				return;
			}

		}


		*//**
	 * 查询银行卡信息
	 * @return
	 *//*
		public String queryBankno(){
			ReturnInfo ri = new ReturnInfo();
			try {
			Users user=getCustomerFromSession();
			if (user==null ) {
				ri.setCode("-1");
				ri.setMsg("请登录后再操作!");
				GsonUtil.GsonObject(ri);
				return null;
			}



				String info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
						+ "UserWebService", false), AxisUtil.NAMESPACE, "checkSystemConfig", new Object[]{"type505","001","否"}, String.class);
				if(StringUtil.isEmpty(info)){
					ri.setCode("-2");
					ri.setMsg("自助解绑银行卡维护中，请联系在线客服");
					GsonUtil.GsonObject(ri);
					return null;
				}
				List<Userbankinfo> userbankinfoList = AxisUtil.getListObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL+ "Axis2WebService", false), AxisUtil.NAMESPACE, "getUserbankinfoList", new Object[] {
						user.getLoginname()}, Userbankinfo.class);
				if(null==userbankinfoList){
					ri.setCode("-2");
					ri.setMsg("系统繁忙,请稍后再试，或者直接与客服联系！");
					GsonUtil.GsonObject(ri);
					return null;
				}else{
					List resultList = new ArrayList();
					for (int i = 0; i < userbankinfoList.size(); i++) {
						Userbankinfo ui = userbankinfoList.get(i);
						Map<String, String> one = new HashMap<String, String>();
						one.put("id", ui.getId()+"");
						one.put("bankno", StringUtil.stringFormat(ui.getBankno(),4,4));
						one.put("bankname",ui.getBankname());
						resultList.add(one);
					}
					ri.setCode("0");
					ri.setData(resultList);
					GsonUtil.GsonObject(ri);
					return null;
				}
			} catch (Exception e) {
				log.error(e);
				ri.setCode("-2");
				ri.setMsg("系统繁忙,请稍后再试，或者直接与客服联系！");
				GsonUtil.GsonObject(ri);
				return null;
			}
		}


	*//**
	 * 自助支付密码
	 * @return
	 * @throws Exception
	 *//*
	public String modifyPasswordPayAjax() throws Exception {
		Users customer = null;
		try {
			customer = getCustomerFromSession();
		} catch (Exception e) {
			this.setErrormsg(e.getMessage().toString());
			GsonUtil.GsonObject(errormsg);
			return null;
		}
		if (!StringUtil.regPayPwd(new_content)) {
			setErrormsg("支付密码为6位纯数字");
			GsonUtil.GsonObject(errormsg);
			return null;
		}
		String msg = null;
		if (StringUtil.isEmpty(content)) {
			// 原登录密码
			if (StringUtils.isBlank(password)) {
				setErrormsg("登录密码不能为空");
				GsonUtil.GsonObject(errormsg);
				return null;
			}
			// setErrormsg("原支付密码不能为空");
			// GsonUtil.GsonObject(errormsg);
			// return null ;

			try {
				msg = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false),
						AxisUtil.NAMESPACE, "modifyPayPassword",
						new Object[] { customer.getLoginname(), password, new_content, 0 }, String.class);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("修改支付密码失败");
				GsonUtil.GsonObject(errormsg);
				return null;
			}

			setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);
			if ("success".equals(msg) == true) {
				getHttpSession().setAttribute(Constants.SESSION_PAYPASSWORD,
						EncryptionUtil.encryptPassword(new_content));
			}
		}
		if (StringUtil.isEmpty(password)) {
			// 原支付密码为6位纯数字
			if (!StringUtil.regPayPwd(content)) {
				setErrormsg("支付密码为6位纯数字");
				GsonUtil.GsonObject(errormsg);
				return null;
			}
			try {
				msg = AxisUtil.getObjectOne(
						AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "Axis2WebService", false),
						AxisUtil.NAMESPACE, "modifyPayPassword",
						new Object[] { customer.getLoginname(), content, new_content, 1 }, String.class);
			} catch (Exception e) {
				e.printStackTrace();
				setErrormsg("修改支付密码失败");
				GsonUtil.GsonObject(errormsg);
				return null;
			}

			setErrormsg(null != msg && "success".equals(msg) ? "修改成功" : msg);
			if ("success".equals(msg) == true) {
				getHttpSession().setAttribute(Constants.SESSION_PAYPASSWORD,
						EncryptionUtil.encryptPassword(new_content));
			}
		}
		GsonUtil.GsonObject(errormsg);
		return null;
	}


	*//**
	 * 游客重置取款密码
	 * @return
	 * @throws Exception
	 *//*
	public String resetVisitorPayPassword_Ajax() throws Exception {


		if(StringUtils.isEmpty(loginname)){
			GsonUtil.GsonObject("账号不可！");
			return null;
		}
		if(StringUtils.isEmpty(accountName)){
			GsonUtil.GsonObject("姓名不可");
			return null;
		}
		if(StringUtils.isEmpty(phone)){
			GsonUtil.GsonObject("手机号不能为空");
			return null;
		}
		if(StringUtils.isEmpty(email)){
			GsonUtil.GsonObject("邮箱不能为空");
			return null;
		}
		if(StringUtils.isEmpty(bankCard)){
			GsonUtil.GsonObject("密码不能为空");
			return null;
		}
		String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "Axis2WebService", false), AxisUtil.NAMESPACE, "resetVisitorPayPassword", new Object[] { loginname,accountName,phone,email,bankCard,getIp()}, String.class);

		GsonUtil.GsonObject(msg);
		return null;
	}*/


	/**
	 * 解绑银行卡
	 *
	 * */
	public void unBindBankinfo(){

		try {
			if(!RequestLimitUtil.getInstance().checkRequestLimit("unBindBankinfo:" + this.getIp())){
				log.warn("unBindBankinfo:" + this.getIp() + "  请求过于频繁");
				GsonUtil.GsonObject("您操作过于频繁，请稍后操作");
				return;
			}
			Users user = getCustomerFromSession();
			if (user==null) {
				GsonUtil.GsonObject("请您先登录！");
				return;
			}
			if(StringUtils.isBlank(this.bankno)){
				GsonUtil.GsonObject("请您输入正确银行卡号！");
				return;
			}

			String msg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "Axis2WebService", false), AxisUtil.NAMESPACE, "unBindBankinfo", new Object[] { user.getLoginname(), this.bankno }, String.class);
			GsonUtil.GsonObject(msg);
		} catch (Exception e) {
			log.error("unBindBankinfo error:", e);
			GsonUtil.GsonObject("系统繁忙，请您稍后再试！");
		}

	}


	public CustomerService getCs() {
		return cs;
	}



	public void setCs(CustomerService cs) {
		this.cs = cs;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	public Pattern getP_email() {
		return p_email;
	}



	public void setP_email(Pattern p_email) {
		this.p_email = p_email;
	}




	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getLoginname() {
		return loginname;
	}



	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}



	public String getBankCard() {
		return bankCard;
	}



	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}



	public Map<String, Object> getSession() {
		return session;
	}



	public void setSession(Map<String, Object> session) {
		this.session = session;
	}



	public String getAccountName() {
		return accountName;
	}



	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}



	public String getBankName() {
		return bankName;
	}



	public void setBankName(String bankName) {
		this.bankName = bankName;
	}



	public String getErrormsg() {
		return errormsg;
	}



	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getNew_content() {
		return new_content;
	}



	public void setNew_content(String new_content) {
		this.new_content = new_content;
	}



	public String getBankno() {
		return bankno;
	}



	public void setBankno(String bankno) {
		this.bankno = bankno;
	}



}
