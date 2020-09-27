package com.nnti.personal.service.implementations;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.common.constants.Constant;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.AESUtil;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.DateUtil;
import com.nnti.personal.dao.master.IMasterExperienceConfigDao;
import com.nnti.personal.model.vo.AppPreferential;
import com.nnti.personal.model.vo.ExperienceGoldConfig;
import com.nnti.personal.service.interfaces.IAppPreferentialService;
import com.nnti.personal.service.interfaces.IExperienceConfigService;

@Service
public class ExperienceConfigServiceImpl extends BaseService implements IExperienceConfigService {

	public static final String KEY = "AUHS&aSs9a89KJ121";

	@Autowired
	private IMasterExperienceConfigDao experienceConfigDao;
	@Autowired
	private IConstService constService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private IProposalExtendService proposalExtendService;
	@Autowired
	private IAppPreferentialService appPreferentialService;

	private static Logger log = Logger.getLogger(ExperienceConfigServiceImpl.class);

	public ExperienceGoldConfig get(Integer id) throws Exception {

		return experienceConfigDao.get(id);
	}

	public List<ExperienceGoldConfig> findList(Map<String, Object> paramsMap) throws Exception {

		return experienceConfigDao.findList(paramsMap);
	}

	public Map<String, Object> haveSameInfo(String loginName, String type, String sid, String channel, String product)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		User user = userService.get(loginName);
		if (null == user) {
			resultMap.put("message", "玩家不存在！");
			return resultMap;
		}
		if (user.getFlag() == 1) {
			resultMap.put("message", "玩家被冻结！");
			return resultMap;
		}

		String[] productArr = new String[] { "lh", "dy", "ql","ul","loh" ,"zb" };
		List<String> productList = Arrays.asList(productArr);

		if (productList.contains(product)) {
			if (null == user.getBirthday() || StringUtils.isBlank(user.getAccountName())) {
				resultMap.put("message", "请完善您的姓名，QQ，微信，生日，再申请体验金！");
				return resultMap;
			}
		}

		String[] productTempArr = new String[] { "qy","ufa"};
		List<String> productTempList = Arrays.asList(productTempArr);

		if (productTempList.contains(product)) {
			if (StringUtils.isBlank(user.getAccountName())) {
				resultMap.put("message", "请完善您的姓名，再申请体验金！");
				return resultMap;
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("conditionTime", DateUtil.format(new Date()));
		params.put("vip", user.getLevel());
		// params.put("platformName", platformName);
		params.put("channel", channel);
		params.put("sid", sid);
		params.put("postCode", user.getPostCode());
		params.put("loginName", user.getLoginName());
		params.put("type", type);

		// Users user = cs.getUsers(loginName);

		// UserWebServiceWS userWebserviceWS = (UserWebServiceWS)
		// SpringFactoryHepler.getInstance("userWebServiceWS");
		Const stSms = constService.get("短信反转验证");
		String smsFlag = stSms.getValue();

		if ("1".equals(type)) {// 下载彩金

			Const st = constService.get("app下载彩金");
			String youHuiFlag = st.getValue();
			if (!"1".equals(youHuiFlag)) {
				resultMap.put("message", "APP下载彩金维护中...");
				return resultMap;
			}

			Integer statusCode = new Integer(0);

			statusCode = checkForAppPreferential(user.getAccountName(), user.getRegisterIp(), user.getLoginName());

			String message = getMessage(statusCode);

			if (StringUtils.isNotEmpty(message)) {
				log.info("APP下载彩金验证结果：" + message);
				resultMap.put("message", message);
				return resultMap;
			}

		} else {

			Const st = constService.get("自助体验金");
			String youHuiFlag = st.getValue();
			if (!"1".equals(youHuiFlag)) {
				resultMap.put("message", "自助体验金优惠维护中...");
				return resultMap;
			}

			Map<String, Object> userParams = new HashMap<String, Object>();
			userParams.put("phone", user.getPhone());
			List<User> userList = userService.findUserList(userParams);
			if (null != userList && userList.size() > 1) {
				resultMap.put("message", "您的注册资讯重复，无法申请体验金，还请参考我们其他的优惠活动哦～");
				return resultMap;
			}

			List<ExperienceGoldConfig> list = findExperienceConfigList(params);
			if (null == list || list.isEmpty()) {
				resultMap.put("message", "优惠配置未开启！");
				return resultMap;
			}

			ExperienceGoldConfig config = list.get(0);

			// app是否已经领取过
			if (null != params.get("sid")) {
				if (config.getMachineCodeEnabled() != null && config.getMachineCodeEnabled() == 1
						&& config.getMachineCodeTimes() != null) {
					Map<String, Object> proposalMap = new HashMap<String, Object>();
					proposalMap.put("platform", "experience");
					proposalMap.put("sid", params.get("sid").toString());
					Integer count = proposalExtendService.count(proposalMap);
					if (count != null && count >= config.getMachineCodeTimes().intValue()) {
						resultMap.put("message", "该设备已领取过体验金！");
						return resultMap;
					}
				}
			}

			// 判断领取次数 只能领取一次判断
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("loginName", loginName);
			paramMap.put("type", "701");
			Integer times = proposalService.experienceGoldTimes(paramMap);

			if (null != times && times > 0) {
				resultMap.put("message", "您已经使用过体验金，请参考其他优惠！");
				return resultMap;
			}

			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("loginName", loginName);
			paramsMap.put("platform", "experience");

			proposalService.preferentialTimes(paramsMap);

			if (null != times && times > 0) {
				resultMap.put("message", "您已经使用过体验金，请参考其他优惠！");
				return resultMap;
			}

			// 判断该玩家是否使用过8元pt优惠
			boolean useFlag = isUsePtYouHui(loginName, user.getAccountName(), user.getRegisterIp());
			if (useFlag) {
				resultMap.put("message", "信息重复，未通过审核！");
				return resultMap;
			}
		}

		if ("1".equals(smsFlag)) {// 短信反转验证
			resultMap.put("verificationType", "smsReverse");
		} else {// 语音验证与短信验证
			resultMap.put("verificationType", "sms");
		}

		resultMap.put("phone", AESUtil.aesDecrypt(user.getPhone(), KEY));

		return resultMap;

	}

	/**
	 * 检查验证资料[app下载彩金]
	 */
	public Integer checkForAppPreferential(String accountName, String registeIp, String loginname) throws Exception {
		boolean isUserIpOrNameDuplicate = isUserIpOrNameDuplicate(accountName, registeIp);
		if (isUserIpOrNameDuplicate) {
			return 1;
		}
		boolean hadAppliedAppPreferential = isApplyAppPreferential(loginname);
		if (hadAppliedAppPreferential) {
			return 2;
		}
		return 0;
	}

	/**
	 * 检查玩家[ip]与[真实姓名]是否跟其他注册玩家重覆
	 */
	public boolean isUserIpOrNameDuplicate(String accountName, String registeIp) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accountName", accountName);
		params.put("registerIp", registeIp);
		List<User> userList = userService.findUserList(params);

		if (userList != null && userList.size() > 1) {
			return true;
		}
		return false;
	}

	/**
	 * 检查玩家是否已申请过[app下载彩金]
	 */
	public boolean isApplyAppPreferential(String loginName) throws Exception {

		// 查询该玩家是否领过，如果领过 则不能再领
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("loginName", loginName);
		paramsMap.put("version", Constant.APPREWARDVERSION);
		List<AppPreferential> appPreferentialList = appPreferentialService.findAppPreferentialList(paramsMap);
		if (appPreferentialList != null && appPreferentialList.size() > 0) {
			log.info("[已申请过app下载彩金!]");
			return true;
		}
		log.info("[尚未申请app下载彩金!]");
		return false;
	}

	// 判断讯息类别
	public String getMessage(int status) {

		String message = "";
		switch (status) {
		case 1:
			message = "[提示]重覆的玩家注册ip地址或姓名";
			break;
		case 2:
			message = "[提示]已申请过app下载彩金";
			break;
		case 3:
			message = "[提示]请绑定姓名";
			break;
		default:
			message = null;// [提示]用户信息没问题!
			break;
		}
		return message;

	}

}