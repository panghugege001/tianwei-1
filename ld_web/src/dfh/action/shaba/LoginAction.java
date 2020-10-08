package dfh.action.shaba;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;
import dfh.utils.StringUtil;

/**
 * 沙巴体育登录接口
 * 
 * @author jalen
 *
 */
public class LoginAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginAction.class);

	public String token;

	public String website;

	private String gameUrl;

	public String sbLogin() {
		try {
			Users user = getCustomerFromSession();
			if (user == null) {
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}

			gameUrl = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"sbSportLogin", new Object[] { user.getLoginname(), "pc" }, String.class);

			log.info("gameUrl:" + gameUrl);
			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl == null)
				return INPUT;

			return SUCCESS;

		} catch (Exception e) {
			log.info(e.getMessage());
			return ERROR;
		}
	}
}
