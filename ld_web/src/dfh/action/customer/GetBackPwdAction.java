package dfh.action.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
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
import dfh.captcha_dc.exception.TouclickException;
import dfh.captcha_dc.model.Status;
import dfh.captcha_dc.service.TouClick;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ILogin;
import dfh.service.interfaces.IMemberSignrecord;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.SendPhoneMsgUtil;
import dfh.utils.Touclick;
/**
 * 取回密码
 * @author jad
 *
 */
public class GetBackPwdAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(GetBackPwdAction.class);
	private CustomerService cs;
	private IMemberSignrecord memberService;
	private final String website_key = "68aca137-f3c5-457b-87a4-8a46880b1e66";
	private final String private_key = "60ccd51f-5df3-4f49-bdeb-5c36eed2329c";
	private ILogin login;
	private static final long serialVersionUID = 1L;
	private String code;
	private String yxdz;
	private String name;
	private String sid;
	private String check_key;
	private String check_address;
	private String phone;
	private Pattern p_email = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
	private String checkCode = "123";
	private static Map<String  , Date> cacheDx = new HashMap<String, Date>();

	/**
	 * 通过邮箱获取密码
	 */
	public void getbackPwdByEmail() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (!p_email.matcher(yxdz).matches()) {
				out.write("电子邮箱格式错误!");
				// 重新生成验证码 防止重复提交
				getHttpSession().setAttribute(Constants.SESSION_AG_TRY_RANDID, null);
				return;
			}
			
			String rand = (String) getHttpSession().getAttribute(Constants.SESSION_AG_TRY_RANDID);
			if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(rand)) {
				out.write("验证码错误！");
				return ;
			}
			try {
			} catch (Exception e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
			Object result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getPwdByYx", new Object[] { name, yxdz}, Object.class);
			OMTextImpl b = (OMTextImpl)result;
			out.write(b.getText());
		
		} catch (IOException e) {
			out.write("邮件发送失败，请重试，或者直接与客服联系！");
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
			
		// 重新生成验证码 防止重复提交
		getHttpSession().setAttribute(Constants.SESSION_RANDID, null);
		return ;
		
	}

	
	
	/**
	 * 手机号码验证
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	  
	/**
	 * 通过短信获得密码
	 * @param name
	 * @param phone
	 * @return
	 */
	public String getbackPwdByDx(){
		try {
		/*	if(!isMobileNO(phone)){
				GsonUtil.GsonObject("手机号码格式错误！");
				return null;
			}*/
			
			/*//触点
			Boolean result = Touclick.check(website_key, private_key, check_key, check_address, null, null, null);
			if(!result){
				GsonUtil.GsonObject("点触验证码错误");
				return null;
			}*/
			
			//触点
			/*TouClick touclick = new TouClick();
			System.out.println("====点触验证码验证开始======");
			Status status = touclick.check(check_address,sid, check_key, website_key, private_key);
			System.out.println("====点触验证码验证结束======");
			System.out.println("点触验证码code :"+status.getCode() + ",message:" + status.getMessage());
			if(status == null || status.getCode()!=0){
				GsonUtil.GsonObject("点触验证码错误");    
				return null;
			}*/
			/*String rand = (String) getHttpSession().getAttribute(Constants.SESSION_RANDID);
			if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(rand)) {
				GsonUtil.GsonObject("验证码错误！");
				return null;
			}*/
			if(!isMobileNO(phone)){
				GsonUtil.GsonObject("手机号码格式错误！");
				return null;
			}
			if(cacheDx.containsKey(name+"_dx")){
				Date date = cacheDx.get(name+"_dx");
				if((new Date().getTime() - date.getTime()) < 1000*60){
					GsonUtil.GsonObject("有正在处理的请,1分钟后再试");
					cacheDx.put(name+"_dx", new Date());
					return null;
				}
			}
			cacheDx.put(name+"_dx", new Date());

			String pwd =getCharAndNumr(8);
			//验证用户和手机号是否匹配 如果匹配则修改为随机密码 如果不匹配则返回 不匹配            0 :匹配       1：不匹配
			Object obj = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "getPwdByDx", new Object[] { name, phone,pwd}, Object.class);
			OMTextImpl b = (OMTextImpl)obj;
			if(b.getText().equals("2")){
				GsonUtil.GsonObject("该账号已冻结!不能使用找回密码！");
				return null;
			}else if(!b.getText().equals("0")){
		    	GsonUtil.GsonObject("用户信息错误，请核对后再次尝试！");
		    	return null;
		    }
			String code = pwd;
			String msg = null ;
			
			msg = SendPhoneMsgUtil.callfour(phone, "您的新密码为：" + code);
			log.info("找回密码短信接口1："+msg);
			if(msg.equals("发送成功")){
				GsonUtil.GsonObject("发送成功，请注意查收短信");
				return null;
			} 
			msg = SendPhoneMsgUtil.sendSms(phone, code);
			log.info(msg);
			if(msg.equals("发送成功")){
				GsonUtil.GsonObject("发送成功，请注意查收短信");
				return null;
			}
			msg = SendPhoneMsgUtil.callMine(phone, code);
			log.info(msg);
			if(msg.equals("发送成功")){
					GsonUtil.GsonObject("发送成功，请注意查收短信");
					return null;
				}else{
				GsonUtil.GsonObject(msg);
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("系统繁忙,请稍后再试，或者直接与客服联系！");
		}finally{
			getHttpSession().setAttribute(Constants.SESSION_RANDID, null);
		}
		GsonUtil.GsonObject("系统繁忙,	请稍后再试，或者直接与客服联系！");
		return null ;
	}
	
	
	
	/**
	 * 获取随机数字和字母
	 * @param length
	 * @return
	 */
	 public static String getCharAndNumr(int length) {
		  String val = "";
		  Random random = new Random();
		  for (int i = 0; i < length; i++) {
		 /*  // 输出字母还是数字
		   String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
		   // 字符串
		   if ("char".equalsIgnoreCase(charOrNum)) {
		    // 取得大写字母还是小写字母
		    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
		    val += (char) (choice + random.nextInt(26));
		   } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
*/		   
			  val += String.valueOf(random.nextInt(10));
		 //  }
		  }
		  return val;
		 }



	public CustomerService getCs() {
		return cs;
	}



	public void setCs(CustomerService cs) {
		this.cs = cs;
	}



	public IMemberSignrecord getMemberService() {
		return memberService;
	}



	public void setMemberService(IMemberSignrecord memberService) {
		this.memberService = memberService;
	}



	public ILogin getLogin() {
		return login;
	}



	public void setLogin(ILogin login) {
		this.login = login;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getYxdz() {
		return yxdz;
	}



	public void setYxdz(String yxdz) {
		this.yxdz = yxdz;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCheck_key() {
		return check_key;
	}



	public void setCheck_key(String check_key) {
		this.check_key = check_key;
	}



	public String getCheck_address() {
		return check_address;
	}


	
	
	

	public String getSid() {
		return sid;
	}



	public void setSid(String sid) {
		this.sid = sid;
	}



	public void setCheck_address(String check_address) {
		this.check_address = check_address;
	}



	public Pattern getP_email() {
		return p_email;
	}



	public void setP_email(Pattern p_email) {
		this.p_email = p_email;
	}



	public String getWebsite_key() {
		return website_key;
	}



	public String getPrivate_key() {
		return private_key;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}
	 
	 
	 

}
