package dfh.action.customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dfh.action.SubActionSupport;
import dfh.model.RaincouponRecord;
import dfh.model.RedRainRes;
import dfh.model.Users;
import dfh.model.bean.RainCouponBean;
import dfh.model.enums.UserRole;
import dfh.utils.AxisUtil;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.HttpUtils;

import java.util.HashMap;
import java.util.List;

public class RedRainAction extends SubActionSupport {
	public static final String SERVER_IP = Configuration.getInstance().getValue("redrain.middle.url") + "/redrain/";;
	private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private Integer typeId;
	private String signType;
	private String title;
	private String platform;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getRedRainRemit() {
		return redRainRemit;
	}

	public void setRedRainRemit(String redRainRemit) {
		this.redRainRemit = redRainRemit;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	private String redRainRemit;
	private String loginname;

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	// 红包雨转入游戏平台
	public String transferInforRedRain() {
		try {
			Users customer = getCustomerFromSession();
			// 判断是否登录
			if (customer == null) {
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if (Double.valueOf(redRainRemit) < 10.0) {
				GsonUtil.GsonObject("[提示]转账金额不能少于10元");
				return null;
			}
			if (customer.getRole().equals(UserRole.AGENT.getCode())) {
				GsonUtil.GsonObject("[提示]代理不能转账");
				return null;
			}
			String transferGameIn = getRequest().getParameter("signType");
			String msg = AxisUtil.getObjectOne(
					AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE,
					"transferInforRedRain", new Object[] { customer.getLoginname(), redRainRemit, transferGameIn }, String.class);
			if (org.apache.commons.lang.StringUtils.isEmpty(msg)) {
				GsonUtil.GsonObject("转账成功");
				return null;
			} else {
				GsonUtil.GsonObject("转账失败:" + msg);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("服务异常，请稍再试");
			return null;
		}
	}

	// 红包雨转给玩家
	public String transferInforRedRaintoUser() {
		try {
			Users customer = getCustomerFromSession();
			// 判断是否登录
			if (customer == null) {
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if (customer.getLoginname().equals(loginname)) {
				GsonUtil.GsonObject("红包不能转给自己");
				return null;
			}
			if (Double.valueOf(redRainRemit) < 10.0) {
				GsonUtil.GsonObject("转账金额不能少于10元");
				return null;
			}
			if (customer.getRole().equals(UserRole.AGENT.getCode())) {
				GsonUtil.GsonObject("[提示]代理不能转账");
				return null;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginname", customer.getLoginname());
			map.put("remit", redRainRemit);
			map.put("toUser", loginname);
			String msg = HttpUtils.post(SERVER_IP + "transferInforRedRaintoUser", map);
			GsonUtil.GsonObject(msg);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("服务异常，请稍再试");
			return null;
		}
	}

	// 查询红包余额
	public String queryRedRainMoney() {
		String msg = null;
		try {
			Users customer = getCustomerFromSession();
			if (customer == null) {
				msg = "请先登陆!";
				GsonUtil.GsonObject(msg);
				return null;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginname", customer.getLoginname());
			msg = HttpUtils.post(SERVER_IP + "queryRedRainMoney", map);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "查询失败";
		}
		GsonUtil.GsonObject(msg);
		return null;
	}

	// 查询具体红包的记录
	public void queryRedRainHistory() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return;
		}
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginname", user.getLoginname());
			map.put("typeId", typeId.toString());
			String res = HttpUtils.post(SERVER_IP + "queryRedRainHistory", map);
			if (org.apache.commons.lang.StringUtils.isEmpty(res)) {
				GsonUtil.GsonObject(null);
			}
			List<RaincouponRecord> list = gson.fromJson(res, new TypeToken<List<RaincouponRecord>>() {
			}.getType());
			GsonUtil.GsonObject(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject(null);
		}
		return;
	}

	// 查询所有红包的总记录
	public void queryRedRainCount() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return;
		}
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginname", user.getLoginname());
			String res = HttpUtils.post(SERVER_IP + "queryRedRainCount", map);
			if (org.apache.commons.lang.StringUtils.isEmpty(res)) {
				GsonUtil.GsonObject(null);
			}
			List<RedRainRes> list = gson.fromJson(res, new TypeToken<List<RedRainRes>>() {
			}.getType());
			GsonUtil.GsonObject(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GsonUtil.GsonObject(null);
		}
		return;
	}

	// 领取红包雨
	public String receiveCoupon() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		RainCouponBean bean = null;
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginname", user.getLoginname());
			map.put("platform", platform);
			String res = HttpUtils.post(SERVER_IP + "receiveRainCoupon", map);
			if (org.apache.commons.lang.StringUtils.isEmpty(res)) {
				bean = new RainCouponBean();
				bean.setCode(400);
				bean.setMsg("ERROR");
				GsonUtil.GsonObject(bean);
				return null;
			}
			bean = gson.fromJson(res, RainCouponBean.class);
			GsonUtil.GsonObject(bean);
			return null;
		} catch (Exception e) {
			bean = new RainCouponBean();
			bean.setCode(500);
			bean.setMsg("ERROR");
			GsonUtil.GsonObject(bean);
			return null;
		}
	}

	/**
	 * 主账户金额转入红包雨账户
	 * 
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public static String transferSelfToRedRain(String loginname, String remit) {

		String msg = null;
		try {
			return AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false),
					AxisUtil.NAMESPACE, "transferSelfToRedRain", new Object[] { loginname, remit }, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "转账失败";
		}
		return msg;
	}

	// 查询活动是否开启

	public String getRainSwitch() {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		if (user == null) {
			GsonUtil.GsonObject("请登录后重试");
			return null;
		}
		if (user.getRole().equals(UserRole.AGENT.getCode())) {
			GsonUtil.GsonObject("[提示]代理不参与");
			return null;
		}
		RainCouponBean bean = null;
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("loginname", user.getLoginname());
			map.put("platform", platform);
			String res = HttpUtils.post(SERVER_IP + "getRainSwitch", map);
			if (org.apache.commons.lang.StringUtils.isEmpty(res)) {
				bean = new RainCouponBean();
				bean.setCode(400);
				bean.setMsg("ERROR");
				GsonUtil.GsonObject(bean);
				return null;
			}
			bean = gson.fromJson(res, RainCouponBean.class);
			GsonUtil.GsonObject(bean);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			bean = new RainCouponBean();
			bean.setMsg("系统出错");
			bean.setCode(500);
		}
		GsonUtil.GsonObject(bean);
		return null;
	}

}