package com.nnti.personal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.controller.BaseController;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.HttpUtil;
import com.nnti.personal.model.dto.SelfDepositDTO;
import com.nnti.personal.model.vo.PreferentialConfig;
import com.nnti.personal.service.interfaces.IPreferentialConfigService;
import com.nnti.personal.service.interfaces.ISelfDepositService;

@RestController
@RequestMapping("/self")
public class SelfDepositController extends BaseController {

	private static Logger log = Logger.getLogger(SelfDepositController.class);

	@Autowired
	ISelfDepositService selfDepositService;
	@Autowired
	IUserService userService;
	@Autowired
	IPreferentialConfigService preferentialConfigService;
	
	@RequestMapping(value = "/deposit/list", method = { RequestMethod.POST })
	public DataTransferDTO findPreferentialConfigList(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		SelfDepositDTO dto = gson.fromJson(requestJsonData, SelfDepositDTO.class);
		
		String loginName = dto.getLoginName();
		
		User user = userService.get(loginName);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("conditionTime", DateUtil.getCurrentDate());
		params.put("vip", user.getLevel());
		params.put("conditionType", dto.getConditionType());
		params.put("platformId", dto.getPlatform());
		
		List<PreferentialConfig> list = preferentialConfigService.findList(params);
		
		/**因等级为数字，后台根据等级做条件查询时使用的是like方式，有可能把不是自身等级的优惠也查询出来了，所以需要特殊处理**/
		if (null != list && !list.isEmpty()) {
		
			List<PreferentialConfig> returnList = new ArrayList<PreferentialConfig>();
			
			for (PreferentialConfig temp : list) {
				
				String vip = temp.getVip();
				
				if (StringUtils.isNotBlank(vip)) {
					
					String[] arr = vip.split(",");
					List<String> vipList = Arrays.asList(arr);
					
					if (vipList.contains(String.valueOf(user.getLevel()))) {
						
						returnList.add(temp);
					}
				}
			}
			
			list = returnList;
		}
		
		return success(list);
	}
	
	@RequestMapping(value = "/deposit/submit", method = { RequestMethod.POST })
	public DataTransferDTO submit(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		SelfDepositDTO dto = gson.fromJson(requestJsonData, SelfDepositDTO.class);
		
		dto.setIp(HttpUtil.getIp(request));
		
		String msg = "";
		Boolean lockFlag = false;
		String loginName = dto.getLoginName();
		String time = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));
		
		try {
			
			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error("玩家：" + loginName + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			
			if (lockFlag) {
				
				log.info("正在处理玩家" + loginName + "的请求，执行时间：" + time);
				
				msg = selfDepositService.submit(dto);
				
				log.info("处理完成玩家" + loginName + "的请求，执行时间：" + time + "，结束时间：" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date()));
			} else {
				
				log.error("玩家：" + loginName + "未能获取锁，无法执行请求对应的方法....");
				
				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			lockFlag = true;
			log.error("执行玩家：" + loginName + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {
			
			if (lockFlag) {
				
				try {
					
					lock.release();
				} catch (Exception e) {
					
					e.printStackTrace();
					log.error("玩家：" + loginName + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		
		if (StringUtils.isNotBlank(msg) && !msg.contains("成功")) {

			failure(FunctionCode.SC_20000_101.getCode(), msg);
		}
		
		return success(FunctionCode.SC_10000.getCode(), msg, null);
	}
}