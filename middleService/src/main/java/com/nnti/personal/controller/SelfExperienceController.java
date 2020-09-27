package com.nnti.personal.controller;

import java.util.Date;
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
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.HttpUtil;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.service.interfaces.IAccountTransferService;
import com.nnti.personal.service.interfaces.IExperienceConfigService;

/**
 * 自助体验金
 * 
 * @author Boots
 */
@RestController
@RequestMapping("/self")
public class SelfExperienceController extends BaseController {

	private static Logger log = Logger.getLogger(SelfExperienceController.class);

	@Autowired
	IAccountTransferService accountTransferService;
	
	@Autowired
	IExperienceConfigService experienceConfigService;

	@RequestMapping(value = "/experience/submit", method = { RequestMethod.POST })
	public DataTransferDTO submit(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();
		SelfExperienceDTO dto = gson.fromJson(requestJsonData, SelfExperienceDTO.class);

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

				msg = accountTransferService.experienceGold(dto);

				log.info("处理完成玩家" + loginName + "的请求，执行时间：" + time + "，结束时间：" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date()));
			} else {

				log.error("玩家：" + loginName + "未能获取锁，无法执行请求对应的方法....");

				msg = "系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {

			e.printStackTrace();
			lockFlag = true;
			log.error("执行玩家：" + loginName + "请求对应的方法发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
			msg = "系统繁忙，请稍后重试！";
		} finally {

			if (lockFlag) {

				try {

					lock.release();
				} catch (Exception e) {

					e.printStackTrace();
					log.error("玩家：" + loginName + "释放锁发生异常，执行时间：" + time + "，异常信息：" + e.getMessage());
					msg = "系统繁忙，请稍后重试！";
				}
			}
		}

		if (StringUtils.isNotBlank(msg) && !msg.contains("成功")) {

			failure(FunctionCode.SC_20000_103.getCode(), msg);
		}

		return success(FunctionCode.SC_10000.getCode(), msg, null);
	}
	
	
	/**
	 * 自助体验金基本信息检查
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/experience/haveSameInfo", method = { RequestMethod.POST })
	public DataTransferDTO haveSameInfo(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();
		SelfExperienceDTO dto = gson.fromJson(requestJsonData, SelfExperienceDTO.class);

		dto.setIp(HttpUtil.getIp(request));

		String loginName = dto.getLoginName();
		String type = dto.getType();
		String sid = dto.getSid();
		String channel = dto.getChannel();
		String product = dto.getProduct();
		
		Map<String,Object> resultMap = experienceConfigService.haveSameInfo(loginName, type, sid, channel,product);
		
		return success(resultMap);
	}	
}