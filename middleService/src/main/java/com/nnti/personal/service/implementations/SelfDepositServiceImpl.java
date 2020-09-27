package com.nnti.personal.service.implementations;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.extend.ApplicationContextProvider;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.BalanceUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.personal.model.dto.SelfDepositDTO;
import com.nnti.personal.service.interfaces.IPlatformDepositService;
import com.nnti.personal.service.interfaces.ISelfDepositService;

@Service("selfDepositService")
public class SelfDepositServiceImpl extends BaseService implements ISelfDepositService {

	private static Logger log = Logger.getLogger(SelfDepositServiceImpl.class);

	private static Map<String, HashMap<String, String>> classMap = new HashMap<String, HashMap<String, String>>();
	private static ObjectMapper mapper = new ObjectMapper();

	static {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("beanId", "ptDepositService");
		map.put(ProposalType.SELF_590.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_591.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_705.getCode().toString(), "limitedTime");
		map.put("type", Constant.PT);
		// 6001->PT存送优惠
		classMap.put("6001", map);

		map = new HashMap<String, String>();
		map.put("beanId", "mgDepositService");
		map.put(ProposalType.SELF_730.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_731.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_732.getCode().toString(), "limitedTime");
		map.put("type", Constant.MG);
		// 6002->MG存送优惠
		classMap.put("6002", map);

		map = new HashMap<String, String>();
		map.put("beanId", "dtDepositService");
		map.put(ProposalType.SELF_733.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_734.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_735.getCode().toString(), "limitedTime");
		map.put("type", Constant.DT);
		// 6003->DT存送优惠
		classMap.put("6003", map);

		map = new HashMap<String, String>();
		map.put("beanId", "qtDepositService");
		map.put(ProposalType.SELF_710.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_711.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_712.getCode().toString(), "limitedTime");
		map.put("type", Constant.QT);
		// 6004->QT存送优惠
		classMap.put("6004", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntDepositService");
		map.put(ProposalType.SELF_707.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_708.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_709.getCode().toString(), "limitedTime");
		map.put("type", Constant.NT);
		// 6005->NT存送优惠
		classMap.put("6005", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ttgDepositService");
		map.put(ProposalType.SELF_598.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_599.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_706.getCode().toString(), "limitedTime");
		map.put("type", Constant.TTG);
		// 6006->TTG存送优惠
		classMap.put("6006", map);

		map = new HashMap<String, String>();
		map.put("beanId", "pngDepositService");
		map.put(ProposalType.SELF_740.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_741.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_742.getCode().toString(), "limitedTime");
		map.put("type", Constant.PNG);
		// 6007->PNG存送优惠
		classMap.put("6007", map);

		map = new HashMap<String, String>();
		map.put("beanId", "aginDepositService");
		map.put(ProposalType.SELF_743.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_744.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_745.getCode().toString(), "limitedTime");
		map.put("type", Constant.AGIN);
		// 6008->AG真人存送优惠
		classMap.put("6008", map);

		map = new HashMap<String, String>();
		map.put("beanId", "slotDepositService");
		map.put(ProposalType.SELF_791.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_792.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_793.getCode().toString(), "limitedTime");
		map.put("type", Constant.SLOT);
		// 6009->老虎机存送优惠
		classMap.put("6009", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "swDepositService");
		map.put(ProposalType.SELF_794.getCode().toString(), "firstDeposit");
		map.put(ProposalType.SELF_795.getCode().toString(), "timesDeposit");
		map.put(ProposalType.SELF_796.getCode().toString(), "limitedTime");
		map.put("type", Constant.SW);
		// 6010->SW存送优惠
		classMap.put("6010", map);
	}

	@Autowired
	IUserService userService;
	@Autowired
	IConstService constService;

	// 个人中心->自助优惠->自助存送->提交
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String submit(SelfDepositDTO dto) throws Exception {

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String platform = dto.getPlatform();
		String id = dto.getId();
		String type = dto.getType();
		Double amount = dto.getAmount();
		String channel = dto.getChannel();

		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}

		if (StringUtils.isBlank(loginName)) {

			return "玩家账号不能为空！";
		}

		if (StringUtils.isBlank(platform)) {

			return "存送优惠平台不能为空！";
		}

		if (StringUtils.isBlank(id)) {

			return "存送优惠编号不能为空！";
		}

		if (StringUtils.isBlank(type)) {

			return "存送优惠类型不能为空！";
		}

		if (null == amount) {

			return "转账金额不能为空！";
		}

		if (!NumericUtil.isNumeric(String.valueOf(amount))) {

			return "转账金额只能为正整数！";
		}

		if (StringUtils.isBlank(channel)) {

			return "申请渠道不能为空！";
		}

		if (!ProposalType.getCodeList().contains(type)) {

			return "不存在该种优惠！";
		}

		// 每天凌晨0点到1点不允许自助存送优惠
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int hour = c.get(Calendar.HOUR_OF_DAY);

		if (hour >= 0 && hour < 1) {

			return "每天的0点至1点是自助优惠系统维护时间！";
		}

		String str = ProposalType.getText(Integer.parseInt(type));
		str = str.substring(2);

		Const st = constService.get(str);

		if (null != st && "0".equals(st.getValue())) {

			return str + "正在维护中！";
		}

		// 判断是否老虎机存送，并限制最低10元
		if (Arrays.asList(new String[] { "590", "591", "598", "599", "702", "703", "704", "705", "706", "707", "708",
				"709", "710", "711", "712", "730", "731", "732", "733", "734", "735", "740", "741", "742", "743", "744",
				"745", "791", "792", "793", "794", "795", "796" }).contains(type)) {

			if (amount < 10) {

				return "转账金额不能少于10元！";
			}
		} else {

			if (amount < 100) {

				return "转账金额不能少于100元！";
			}
		}

		HashMap<String, String> map = classMap.get(platform);

		if (null == map) {

			return "没有找到平台编码为[" + platform + "]的存送优惠平台，请联系在线客服处理！";
		}

		String beanId = map.get("beanId");
		String method = map.get(type);

		if (StringUtils.isBlank(beanId) || StringUtils.isBlank(method)) {

			return "没有找到平台编码为[" + platform + "]的对应服务类或者执行方法，请联系在线客服处理！";
		}

		IPlatformDepositService service = ApplicationContextProvider.getApplicationContext().getBean(beanId, IPlatformDepositService.class);

		Method m = null;
		Object obj = null;
		String message = null;

		try {

			Class cls = service.getClass();
			m = cls.getMethod(method, new Class[] { SelfDepositDTO.class });
			obj = m.invoke(service, new Object[] { dto });
		} catch (Exception e) {

			e.printStackTrace();
			log.error("SelfDepositServiceImpl类的submit方法利用反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}

		if (StringUtils.isNotBlank(message)) {

			return "系统繁忙，请稍后重试！";
		}

		Map<String, String> resultMap = null;

		try {

			resultMap = mapper.readValue(String.valueOf(obj), Map.class);
		} catch (Exception e1) {

			e1.printStackTrace();
			log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e1.getMessage());
		}

		if (null == resultMap) {

			return "系统繁忙，请稍后重试！！";
		}

		if (null == resultMap.get("giftMoney")) {

			return resultMap.get("message");
		}

		// 优惠红利
		Double giftMoney = Double.parseDouble(String.valueOf(resultMap.get("giftMoney")));
		//
		String referenceId = resultMap.get("referenceId");
		// 游戏平台类型
		String platformType = map.get("type");

		User user = userService.get(loginName);
		// 转入金额至游戏账户
		message = BalanceUtil.transferInAccount(product, platformType, loginName,user.getPassword(), giftMoney, referenceId);

		if (StringUtils.isNotBlank(message)) {

			return message;
		}

		return resultMap.get("message");
	}
}