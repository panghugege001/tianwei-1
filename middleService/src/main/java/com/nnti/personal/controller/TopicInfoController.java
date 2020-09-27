package com.nnti.personal.controller;

import java.util.Date;
import java.util.HashMap;
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
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.controller.BaseController;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.model.vo.User;
import com.nnti.common.page.Page;
import com.nnti.common.page.PageHelper;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.HttpUtil;
import com.nnti.personal.model.dto.TopicInfoDTO;
import com.nnti.personal.model.vo.TopicInfo;
import com.nnti.personal.service.interfaces.ITopicInfoService;

@RestController
@RequestMapping("/topic")
public class TopicInfoController extends BaseController {
	
	private static Object lock = new Object();
	
	@Autowired
	private ITopicInfoService topicInfoService;
	@Autowired
	IUserService userService;
	
	private static Logger log = Logger.getLogger(TopicInfoController.class);
	
	@RequestMapping(value = "/saveTopic", method = { RequestMethod.POST })
	public DataTransferDTO saveTopic(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		dto.setIpAddress(HttpUtil.getIp(request));
		
		String msg = "";
		
		try {
			
			msg = topicInfoService.saveTopicInfo(dto);
		} catch (Exception e) {
			
			e.printStackTrace();
			msg = "系统繁忙";
		}
		
		if (StringUtils.isNotBlank(msg)) {
			
			failure(FunctionCode.SC_20000_111.getCode(), msg);
		}
		
		return success(FunctionCode.SC_10000.getCode(), msg, null);
	}
	
	@RequestMapping(value = "/saveReply", method = { RequestMethod.POST })
	public DataTransferDTO saveReply(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		dto.setIpAddress(HttpUtil.getIp(request));
		
		String msg = "";
		
		try {
			
			msg = topicInfoService.saveReplyInfo(dto);
		} catch (Exception e) {
			
			e.printStackTrace();
			msg = "系统繁忙";
		}
		
		if (StringUtils.isNotBlank(msg)) {
			
			failure(FunctionCode.SC_20000_111.getCode(), msg);
		}
		
		return success(FunctionCode.SC_10000.getCode(), msg, null);
	}
	
	@RequestMapping(value = "/queryList", method = { RequestMethod.POST })
	public DataTransferDTO queryList(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		Integer pageNum = dto.getPageNum();
		Integer pageSize = dto.getPageSize();
		String loginName = dto.getLoginName();
		
		if (null == pageNum) {
			
			pageNum = Constant.PAGE_INDEX;
		}
		
		if (null == pageSize) {
			
			pageSize = Constant.PAGE_SIZE;
		}
		
		Page<TopicInfo> topicInfo = topicInfoService.findTopicByUserNamePage(pageNum, pageSize, loginName);

		return success(PageHelper.getPageMap(topicInfo));
	}
	
	@RequestMapping(value = "/queryMySelfList", method = { RequestMethod.POST })
	public DataTransferDTO queryMySelfList(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		Integer pageNum = dto.getPageNum();
		Integer pageSize = dto.getPageSize();
		String loginName = dto.getLoginName();
		
		if (null == pageNum) {
			
			pageNum = Constant.PAGE_INDEX;
		}
		
		if (null == pageSize) {
			
			pageSize = Constant.PAGE_SIZE;
		}
		
		Page<TopicInfo> topicInfo = topicInfoService.findMySelfTopicPage(pageNum, pageSize, loginName);
		
		return success(PageHelper.getPageMap(topicInfo));
	}
	
	@RequestMapping(value = "/readedTopic", method = { RequestMethod.POST })
	public DataTransferDTO readedTopic(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		String loginName = dto.getLoginName();
		Integer topicId = dto.getTopicId();
		
		if (null == topicId) {
			
			failure(FunctionCode.SC_20000_111.getCode(), "[提示]站内信ID不能为空！");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("statusId", topicId);
		
		String msg = topicInfoService.updateTopicUnRead(params);
		
		return success(msg);
	}
	
	@RequestMapping(value = "/queryTopicById", method = { RequestMethod.POST })
	public DataTransferDTO queryTopicById(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		Integer pageNum = dto.getPageNum();
		String loginName = dto.getLoginName();
		Integer pageSize = dto.getPageSize();
		Integer topicId = dto.getTopicId();
		
		if (null == topicId) {
			
			failure(FunctionCode.SC_20000_111.getCode(), "[提示]站内信ID不能为空！");
		}
		
		if (null == pageNum) {
			
			pageNum = Constant.PAGE_INDEX;
		}
		
		if (null == pageSize) {
			
			pageSize = Constant.PAGE_SIZE;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("statusId", topicId);
		
		Page<TopicInfo> topicInfo = topicInfoService.findTopicInfoByIdPage(pageNum, pageSize, params);
		
		return success(PageHelper.getPageMap(topicInfo));
	}
	
	@RequestMapping(value = "/deleteTopicById", method = { RequestMethod.POST })
	public DataTransferDTO deleteTopicById(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		String loginName = dto.getLoginName();
		Integer topicId = dto.getTopicId();
		
		if (StringUtils.isBlank(loginName)) {
			
			failure(FunctionCode.SC_20000_111.getCode(), "[提示]登录用户名不能为空！");
		}
		
		if (null == topicId) {
			
			failure(FunctionCode.SC_20000_111.getCode(), "[提示]站内信ID不能为空！");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("statusId", topicId);
		
		String msg = topicInfoService.deleteTopicInfo(params);
		
		return success(msg);
	}
	
	@RequestMapping(value = "/queryUnReadNum", method = { RequestMethod.POST })
	public DataTransferDTO queryUnReadNum(@RequestParam(value = "requestData", defaultValue = "") String requestData,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();

		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		dto.setIpAddress(HttpUtil.getIp(request));
		String loginName = dto.getLoginName();
		Integer level = dto.getUserNameType();

		// 新增不在系统中的数据
		Map<String, Object> params = new HashMap<String, Object>();

		if (level.equals(Constant.TOPIC_SEND_TYPE_ALL_AGENT)) {

			params.put("type", Constant.TOPIC_SEND_TYPE_ALL_AGENT);
		} else {

			params.put("type", Constant.TOPIC_SEND_TYPE_ALL_MEMBER);
		}
		User user = userService.get(loginName);

		params.put("level", level);
		params.put("loginName", loginName);
		params.put("createTime", user.getCreateTime());
		params.put("ipAddress", dto.getIpAddress());

		Integer count = 0;
		synchronized(lock) {
			count = topicInfoService.getUnReadTopicCount(params);
		}
		return success(String.valueOf(count));
	}
	
	
	@RequestMapping(value = "/deleteTopic", method = { RequestMethod.POST })
	public DataTransferDTO deleteTopic(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();
		
		TopicInfoDTO dto = gson.fromJson(requestJsonData, TopicInfoDTO.class);
		String loginName = dto.getLoginName();
		String ids = dto.getIds();
		
		if (StringUtils.isBlank(loginName)) {
			
			failure(FunctionCode.SC_20000_111.getCode(), "[提示]登录用户名不能为空！");
		}
		
		if (StringUtils.isBlank(ids)) {
			
			failure(FunctionCode.SC_20000_111.getCode(), "[提示]站内信ID不能为空！");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		String[] idAry = ids.split(",");
		params.put("ids", idAry);
		
		String msg = topicInfoService.deleteTopic(params);
		
		return success(msg);
	}
}