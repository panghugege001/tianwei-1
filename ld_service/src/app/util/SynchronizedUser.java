package app.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import dfh.utils.DtUtil;
import dfh.utils.MGSUtil;
import dfh.utils.NTUtils;
import dfh.utils.PtUtil;
import dfh.utils.PtUtil1;
import dfh.utils.QtUtil;
import app.model.vo.PlatformDepositVO;
import app.service.interfaces.IPlatformDepositService;

public class SynchronizedUser {

	private static Logger log = Logger.getLogger(SynchronizedUser.class);
	
	private static Map<String, HashMap<String, String>> classMap = new HashMap<String, HashMap<String, String>>();
	private static List<Integer> ptList = Arrays.asList(new Integer[] { 590, 591, 705 });
	private static List<Integer> mgList = Arrays.asList(new Integer[] { 730, 731, 732 });
	private static List<Integer> dtList = Arrays.asList(new Integer[] { 733, 734, 735 });
	private static List<Integer> qtList = Arrays.asList(new Integer[] { 710, 711, 712 });
	private static List<Integer> ntList = Arrays.asList(new Integer[] { 707, 708, 709 });
	private static List<Integer> ttgList = Arrays.asList(new Integer[] { 598, 599, 706 });
	
	public SynchronizedUser() {}
	
	static {
		
		HashMap<String, String> ptMap = new HashMap<String, String>();
		ptMap.put("beanId", "ptDepositService");
		ptMap.put("590", "firstDeposit");
		ptMap.put("591", "timesDeposit");
		ptMap.put("705", "limitedTime");
		
		// 6001->PT存送优惠
		classMap.put("6001", ptMap);
		
		HashMap<String, String> mgMap = new HashMap<String, String>();
		mgMap.put("beanId", "mgDepositService");
		mgMap.put("730", "firstDeposit");
		mgMap.put("731", "timesDeposit");
		mgMap.put("732", "limitedTime");
		
		// 6002->MG存送优惠
		classMap.put("6002", mgMap);
		
		HashMap<String, String> dtMap = new HashMap<String, String>();
		dtMap.put("beanId", "dtDepositService");
		dtMap.put("733", "firstDeposit");
		dtMap.put("734", "timesDeposit");
		dtMap.put("735", "limitedTime");		
		
		// 6003->DT存送优惠
		classMap.put("6003", dtMap);
		
		HashMap<String, String> qtMap = new HashMap<String, String>();
		qtMap.put("beanId", "qtDepositService");
		qtMap.put("710", "firstDeposit");
		qtMap.put("711", "timesDeposit");
		qtMap.put("712", "limitedTime");	
		
		// 6004->QT存送优惠
		classMap.put("6004", qtMap);
		
		HashMap<String, String> ntMap = new HashMap<String, String>();
		ntMap.put("beanId", "ntDepositService");
		ntMap.put("707", "firstDeposit");
		ntMap.put("708", "timesDeposit");
		ntMap.put("709", "limitedTime");	
		
		// 6005->NT存送优惠
		classMap.put("6005", ntMap);
		
		HashMap<String, String> ttgMap = new HashMap<String, String>();
		ttgMap.put("beanId", "ttgDepositService");
		ttgMap.put("598", "firstDeposit");
		ttgMap.put("599", "timesDeposit");
		ttgMap.put("706", "limitedTime");
		
		// 6006->TTG存送优惠
		classMap.put("6006", ttgMap);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized String selfDeposit(PlatformDepositVO vo) {
		
		String loginName = vo.getLoginName();
		String platform = vo.getPlatform();
		Integer youhuiType = vo.getYouhuiType();
		
		log.info("SynchronizedUser类的selfDeposit方法参数为：【loginName=" + loginName + ",platform=" + platform + ",youhuiType=" + youhuiType + "】");
		
		HashMap<String, String> map = classMap.get(platform);
		
		if (null == map) {
			
			return "没有找到编码为[" + platform + "]的存送优惠平台！";
		}
		
		String beanId = map.get("beanId");
		String method = map.get(String.valueOf(youhuiType));
		
		if (StringUtils.isBlank(beanId) || StringUtils.isBlank(method)) {
		
			return "没有找到平台编码为[" + platform + "]的对应服务类或者执行方法，请联系管理员！";
		}
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		IPlatformDepositService platformDepositService = (IPlatformDepositService) wac.getBean(beanId);
		
		Method m = null;
		Object obj = null;
		String message = null;
		
		try {
			
			Class c = IPlatformDepositService.class;
			m = c.getMethod(method, new Class[] { PlatformDepositVO.class });
			obj = m.invoke(platformDepositService, new Object[] { vo });
		} catch (Exception e) {
			
			log.error("反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}
		
		if (StringUtils.isNotBlank(message)) {
		
			return message;
		}
		
		if (obj instanceof PlatformDepositVO) {
			
			PlatformDepositVO temp = (PlatformDepositVO) obj;
			message = temp.getMessage();
			
			// 如果giftMoney的值不为空，则说明自助优惠成功，将对游戏平台账号进行操作
			if (null != temp.getGiftMoney()) {
				// PT平台
				if (ptList.contains(youhuiType)) {
				
					Boolean flag = PtUtil.getDepositPlayerMoney(loginName, temp.getGiftMoney());
					
					if (!flag) {
						
						message = "转入PT账户失败，请联系客服！";
					}
				}
				// MG平台
				else if (mgList.contains(youhuiType)) {
					
					try {
						
						String msg = MGSUtil.transferToMG(loginName,vo.getPassword(), temp.getGiftMoney(),temp.getTransferId());
					
						if (StringUtils.isNotBlank(msg)) {
							
							message = "转入MG账户失败，请联系客服！";
						}
					} catch (Exception e) {
						
						message = "转入MG账户失败，请稍后重试！";
					}
				}
				// DT平台
				else if (dtList.contains(youhuiType)) {
					
					String msg = DtUtil.withdrawordeposit(loginName, temp.getGiftMoney());
					
					if (!msg.equalsIgnoreCase("success")) {
						
						message = "转入DT账户失败，请联系客服！";
					}
				}
				// QT平台
				else if (qtList.contains(youhuiType)) {
					
					String msg = QtUtil.getDepositPlayerMoney(loginName, temp.getGiftMoney(), temp.getTransferId());
					
					if (StringUtils.isBlank(msg) || !QtUtil.RESULT_SUCC.equals(msg)) {
					
						message = "转入QT账户失败，请联系客服！";
					}
				}
				// NT平台
				else if (ntList.contains(youhuiType)) {
					
					JSONObject jsonObject = JSONObject.fromObject(NTUtils.changeMoney(loginName, temp.getGiftMoney()));
					
					if (!jsonObject.getBoolean("result")) {

						message = "转入NT账户失败，请联系客服！";
					}
				}
				// TTG平台
				else if (ttgList.contains(youhuiType)) {
					
					Boolean deposit = PtUtil1.addPlayerAccountPraper(loginName, temp.getGiftMoney());
					
					if (!deposit) {
						
						message = "转入TTG账户失败，请联系客服！";
					}
				}
			}
		}
		
		return message;
	}
	
}