package dfh.action.weCustomer;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.ReturnInfo;
import dfh.model.enums.WeCustomerErrEnum;
import dfh.model.weCustomer.UserInfo;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;
import dfh.utils.MD5;
import dfh.utils.StringUtil;

/**
 * 微客服接口调用action
 * 
 * @author jalen
 *
 */
public class WeCustomerAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(WeCustomerAction.class);
	public static String MD5KEY = "5mkko!@#ABOJOLOtiibiacb9g";

	private String username;
	private String md5str;

	public void getUserInfo() {
		ReturnInfo ri = new ReturnInfo();
		if (StringUtil.isEmpty(username)) {
			ri.setCode(WeCustomerErrEnum.C00001.getCode());
			ri.setMsg(WeCustomerErrEnum.C00001.getMsg());
			ri.setData(null);
			GsonUtil.GsonObject(ri);
			return;
		} else {
			String str = username + MD5KEY;
			String encryptStr = MD5.getMD5Str(str);
			if(!encryptStr.equals(md5str)){
				ri.setCode(WeCustomerErrEnum.C00004.getCode());
				ri.setMsg(WeCustomerErrEnum.C00004.getMsg());
				ri.setData(null);
				GsonUtil.GsonObject(ri);
				return;
			}
		}
		try {
			UserInfo user = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
							+ "UserWebService", false), AxisUtil.NAMESPACE,
					"getWeUserInfo", new Object[] { username }, UserInfo.class);
			if (user == null) {
				ri.setCode(WeCustomerErrEnum.C00002.getCode());
				ri.setMsg(WeCustomerErrEnum.C00002.getMsg());
				ri.setData(null);
			} else {
				ri.setCode(WeCustomerErrEnum.C00000.getCode());
				ri.setMsg(WeCustomerErrEnum.C00000.getMsg());
				ri.setData(user);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ri.setCode(WeCustomerErrEnum.C00003.getCode());
			ri.setMsg(WeCustomerErrEnum.C00003.getMsg());
			ri.setData(null);
		}
		GsonUtil.GsonObject(ri);
	}

	public String getMd5str() {
		return md5str;
	}

	public void setMd5str(String md5str) {
		this.md5str = md5str;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
