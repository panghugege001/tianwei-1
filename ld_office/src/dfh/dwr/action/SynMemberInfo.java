package dfh.dwr.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import dfh.model.Users;
import dfh.service.interfaces.ISynMemberInfoService;

public class SynMemberInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1768776684875637118L;
	private ISynMemberInfoService synMemberService;
	private Logger log=Logger.getLogger(SynMemberInfo.class);
	public ISynMemberInfoService getSynMemberService() {
		return synMemberService;
	}
	public void setSynMemberService(ISynMemberInfoService synMemberService) {
		this.synMemberService = synMemberService;
	}
	
	
	public synchronized String synMemberInfo(String loginname){
		if (loginname==null||loginname.trim().equals("")) {
			return "会员账号不可为空，请重新输入";
		}
		try {
			Users user = this.synMemberService.getUserObject(loginname);
			if (user==null) {
				return "输入的用户不存在";
			}else{
				int status = this.synMemberService.synMemberInfo(user.getLoginname(), user.getPassword());
				if (status == 300) {
					return "该用户已经同步过了";
				} else if (status == 200) {
					return "成功";
				} else {
					return "失败";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			return "系统异常；"+e.getMessage();
		}
		
	}
	

}
