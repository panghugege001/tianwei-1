package dfh.action.shaba;


import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.ReturnInfo;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;

/**
 * 沙巴体育登录接口
 * 
 * @author jalen
 *
 */
public class LoginAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginAction.class);

	private static final String PREFIX = "k_";
	
	public String token;
	
	public String website;
	
	public String sbLogin() {
		ReturnInfo ri = new ReturnInfo();
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				ri.setCode("0");
				ri.setMsg("请登录后再操作!");
				ri.setData(null);
				GsonUtil.GsonObject(ri);
				return ERROR;
			}

			String youHuiFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { "转账SBA" }, String.class);
			if (!youHuiFlag.equals("1")) {
				ri.setCode("-1");
				ri.setMsg("沙巴体育正在维护!");
				ri.setData(null);
				GsonUtil.GsonObject(ri);
				return ERROR;
			}
			String loginname = customer.getLoginname();
			token = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
					"saveOrUpdateSBToken", new Object[] { PREFIX , loginname }, String.class);
			if (token == null) {
				ri.setCode("-1");
				ri.setMsg("token生成失败！");
				ri.setData(null);
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ri.setCode("-1");
			ri.setMsg("系统异常");
			ri.setData(null);
			GsonUtil.GsonObject(ri);
			return ERROR;
		}
	}
	
	public String sbMobileLogin() {
		ReturnInfo ri = new ReturnInfo();
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				ri.setCode("0");
				ri.setMsg("请登录后再操作!");
				ri.setData(null);
				GsonUtil.GsonObject(ri);
				return ERROR;
			}

			String youHuiFlag = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE,
					"checkGameIsProtect", new Object[] { "转账SBA" }, String.class);
			if (!youHuiFlag.equals("1")) {
				ri.setCode("-1");
				ri.setMsg("沙巴体育正在维护!");
				ri.setData(null);
				GsonUtil.GsonObject(ri);
				return ERROR;
			}
			String loginname = customer.getLoginname();
			token = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
							"saveOrUpdateSBToken", new Object[] { PREFIX , loginname }, String.class);
			if (token == null) {
				ri.setCode("-1");
				ri.setMsg("token生成失败！");
				ri.setData(null);
				return ERROR;
			}
			this.website=this.getRequest().getScheme()+"://"+this.getRequest().getServerName()+"/mobile/new/index.jsp";
			return SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ri.setCode("-1");
			ri.setMsg("系统异常");
			ri.setData(null);
			GsonUtil.GsonObject(ri);
			return ERROR;
		}
	}
	
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
