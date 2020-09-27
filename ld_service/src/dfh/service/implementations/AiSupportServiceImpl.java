package dfh.service.implementations;


import dfh.dao.SlaveDao;
import dfh.model.Actionlogs;
import dfh.model.Userbankinfo;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.ActionLogType;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.AiSupportService;
import dfh.service.interfaces.CustomerService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;
import dfh.utils.sendemail.AESUtil;
import dfh.utils.sendemail.SendEmailWs;
import dfh.utils.sendsms.SendPhoneMsgUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * 找回密码
 * @author ck
 *
 */
public class AiSupportServiceImpl implements AiSupportService {
	
	private static Logger log = Logger.getLogger(AiSupportServiceImpl.class);


	private SlaveDao slaveDao;

    private CustomerService cs;
    
    @Override
	public String unlockAccountByPassword(String loginname, String password) {
    	loginname= StringUtil.lowerCase(loginname);
    	Users users=cs.getUsers(loginname);
		if (users==null) {
			return "账号提供错误,请再输入,谢谢";
		}
		if(users.getFlag() != 2){
			return "该账户状态不能使用该功能";
		}
		
		if(!StringUtil.equals(users.getPassword(), EncryptionUtil.encryptPassword(password))){
			return "密码错误";
		}
		this.unlockAccount(users);
		return "success";
	}

	@Override
	public String unlockAccountByInfo(String loginname,String accoutName,String email,String phone) {
		
		loginname= StringUtil.lowerCase(loginname);
		Users users=cs.getUsers(loginname);
		
		if (users==null) {
			return "账号提供错误,请再输入,谢谢";
		}
		
		if(users.getFlag() != 2){
			return "该账户状态不能使用该功能";
		}
		
		if (!StringUtil.equals(accoutName, users.getAccountName())) {
			return "注册姓名错误";
		}
		
		try {
			if (!StringUtil.equals(AESUtil.aesEncrypt(email, AESUtil.KEY), users.getEmail())) {
				return "邮件输入错误";
			}
		} catch (Exception e) {
			return "邮件输入错误";
		}
		
		try {
			if (!StringUtil.equals(AESUtil.aesEncrypt(phone, AESUtil.KEY), users.getPhone())) {
				return "电话输入错误";
			}
		} catch (Exception e) {
			return "电话输入错误";
		}
		
		this.unlockAccount(users);
		return "success";
	}
	
	private void unlockAccount(Users users) {
		users.setFlag(0);
		Userstatus userstatus=(Userstatus) cs.get(Userstatus.class, users.getLoginname());
		userstatus.setLoginerrornum(0);
		Actionlogs actionlog = new Actionlogs(users.getLoginname(), ActionLogType.ACC_UNLOCK.getCode(), DateUtil.now(), "根据用户信息自助解冻账号");
		cs.save(actionlog);
		cs.save(users);
		cs.save(userstatus);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(AESUtil.aesEncrypt("15011111111", AESUtil.KEY));
		System.out.println(AESUtil.aesEncrypt("test@gmail.com", AESUtil.KEY));
	}
	
	@Override
	public String forgetAccount(String accoutName, String email, String phone) {
		// TODO Auto-generated method stub
         
		
		String emailStr="";
		String phoneStr="";
		
		String choiceWay="NAN";
		
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		
		
		if (StringUtil.isNotEmpty(email)) {
			try {
				emailStr = AESUtil.aesEncrypt(email, AESUtil.KEY);
			} catch (Exception e) {
				emailStr = email;
			}
			
			dc.add(Restrictions.eq("email", email));
			
			choiceWay="email";
		}
		
		if (StringUtil.isNotEmpty(phone)) {
			try {
				phoneStr = AESUtil.aesEncrypt(phone, AESUtil.KEY);
			} catch (Exception e) {
				phoneStr = phone;
			}
			
			dc.add(Restrictions.eq("phone", phone));
		}
		
		Integer size = (Integer) cs.getHibernateTemplate().findByCriteria(dc.setProjection(Projections.rowCount())).get(0);
		if(null != size && size>0 ){
		   if (StringUtil.equals(choiceWay, "email")) {
			  
		   }else {
			
		   }
		}
		
		
		return null;
	}
	
	
	@Override
	public String forgetAccbySms(String phone,String ip) {
         
		String phoneStr="";
		String msg="";
		
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		
		if (StringUtil.isNotEmpty(phone)) {
			try {
				phoneStr = AESUtil.aesEncrypt(phone, AESUtil.KEY);
			} catch (Exception e) {
				phoneStr = phone;
			}
			
			dc.add(Restrictions.eq("phone", phoneStr));
		}
		
		List<Users> userList = cs.getHibernateTemplate().findByCriteria(dc);
        
		if (userList == null || userList.size() == 0) 
			 return "用户信息错误，请核查，或直接与客服联系！";
		if (userList.size() > 1)
			return "手机号有重复，请联系客服！！！";
		
		log.info("忘记账号找回:"+ip);
		
		
		Users users = userList.get(0);
		msg = SendPhoneMsgUtil.callfour(phone, Constants.SEND_GETACC.replace("@username", users.getLoginname()));
		if(msg.equals("发送成功")){
			
			Actionlogs actionlog = new Actionlogs();
			actionlog.setLoginname(users.getLoginname());
			actionlog.setRemark("账号短信找回--"+ip);
			actionlog.setCreatetime(DateUtil.now());
			actionlog.setAction(ActionLogType.ACC_RETRIEVE_SMS.getCode());
			cs.save(actionlog);
			
			return "发送成功，请注意查收短信";
		}
		return msg;
	}
	
	
	@Override
	public String sendForgetAccbyEmail(String email,String ip) {
		// TODO Auto-generated method stub
         
		String emailStr="";
		String msg="";
		
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		
		if (StringUtil.isNotEmpty(email)) {
			try {
				emailStr = AESUtil.aesEncrypt(email, AESUtil.KEY);
			} catch (Exception e) {
				emailStr = email;
			}
			
			dc.add(Restrictions.eq("email", emailStr));
		}
		
		List<Users> list= cs.getHibernateTemplate().findByCriteria(dc);
        
		if (list==null||list.size()==0) 
			 return "用户信息错误，请核查，或直接与客服联系！";
		
		if(list.size() > 1){
			return "email信息有重复，请联系客服！！！";
		}
		
	    Users users=list.get(0);
		
		log.info("忘记账号找回:"+ip);
		
		msg = SendEmailWs.sendEmailsByApiGetAcc(email, Constants.SEND_GETACC.replace("@username", users.getLoginname()));
		if(!msg.contains("失败")){

			Actionlogs actionlog = new Actionlogs();
			actionlog.setLoginname(users.getLoginname());
			actionlog.setRemark("邮箱找回账号--"+ip);
			actionlog.setCreatetime(DateUtil.now());
			actionlog.setAction(ActionLogType.PWD_RETRIEVE.getCode());
			cs.save(actionlog);
		}		
		return msg;
	}
	
	
	
	@Override
	public String withdrawPassForget(String loginname,String accoutName,String email,String phone) {
		// TODO Auto-generated method stub
		
		Users users=cs.getUsers(loginname);
		
		if (users==null) {
			return "账号提供错误,请再输入,谢谢";
		}
		
		if (StringUtil.equals(loginname, users.getLoginname())) {
			return "注册账号错误";
		}
		
		if (StringUtil.equals(accoutName, users.getAccountName())) {
			return "注册姓名错误";
		}
		
		try {
			if (StringUtil.equals(AESUtil.aesEncrypt(email, AESUtil.KEY), users.getEmail())) {
				return "邮件输入错误";
			}
		} catch (Exception e) {
			return "邮件输入错误";
		}

		
		try {
			if (StringUtil.equals(AESUtil.aesEncrypt(phone, AESUtil.KEY), users.getPhone())) {
				return "电话输入错误";
			}
		} catch (Exception e) {
			return "电话输入错误";
		}
		
		users.setFlag(0);
		Userstatus userstatus=(Userstatus) cs.get(Userstatus.class, users.getLoginname());
		userstatus.setLoginerrornum(0);
		cs.save(users);
		cs.save(userstatus);
		
		return "解除账号成功";
	}
	
	
	/**
	 * 检查银行卡号是否存在
	 * @param loginname
	 * @param bankno
	 * @return
	 * 
	 */
	public boolean getUserbankinfoList(String loginname,String bankno){
		DetachedCriteria dc = DetachedCriteria.forClass(Userbankinfo.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("bankno", bankno));
		dc.add(Restrictions.eq("flag", 0));
		Integer size = (Integer) cs.getHibernateTemplate().findByCriteria(dc.setProjection(Projections.rowCount())).get(0);
		if (size>0) {
			return true;
		}
		return false;
	}

	public SlaveDao getSlaveDao() {
		return slaveDao;
	}

	public void setSlaveDao(SlaveDao slaveDao) {
		this.slaveDao = slaveDao;
	}

	public CustomerService getCs() {
		return cs;
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}
	
	
}
