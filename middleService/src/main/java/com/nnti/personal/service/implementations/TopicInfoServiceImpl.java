package com.nnti.personal.service.implementations;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.constants.Constant;
import com.nnti.common.page.Page;
import com.nnti.common.page.PageHelper;
import com.nnti.common.utils.DateUtil;
import com.nnti.personal.dao.master.IMasterReplyInfoDao;
import com.nnti.personal.dao.master.IMasterTopicInfoDao;
import com.nnti.personal.dao.master.IMasterTopicStatusDao;
import com.nnti.personal.dao.slave.ISlaveTopicInfoDao;
import com.nnti.personal.dao.slave.ISlaveTopicStatusDao;
import com.nnti.personal.model.dto.TopicInfoDTO;
import com.nnti.personal.model.vo.ReplyInfo;
import com.nnti.personal.model.vo.TopicInfo;
import com.nnti.personal.model.vo.TopicStatus;
import com.nnti.personal.service.interfaces.ITopicInfoService;

@Service
public class TopicInfoServiceImpl implements ITopicInfoService {
	
	@Autowired
	private IMasterTopicInfoDao masterTopicInfoDao;
	@Autowired
	private ISlaveTopicInfoDao slaveTopicInfoDao;
	@Autowired
	private IMasterTopicStatusDao masterTopicStatusDao;
	@Autowired
	private ISlaveTopicStatusDao slaveTopicStatusDao;
	@Autowired
	private IMasterReplyInfoDao masterReplyInfoDao;

	/**
	 * 根据id查询站内信
	 */
	public Page<TopicInfo> findTopicInfoByIdPage(Integer pageNum, Integer pageSize, Map<String, Object> params) throws Exception {
		
		// 修改站内信已读状态 // 已读1和未读0
		params.put("isUserRead", Constant.TOPIC_STATUS_IS_READ_YES);
		
		masterTopicStatusDao.update(params);
		
		PageHelper.startPage(pageNum, pageSize, true);
		
		return slaveTopicInfoDao.findTopicInfoByIdPage(Integer.parseInt(String.valueOf(params.get("statusId"))));
	}
	
	/**
	 * 新增玩家发帖
	 */
	public String saveTopicInfo(TopicInfoDTO dto) throws Exception {
		
		String title = dto.getTitle();
		String content = dto.getContent();
		String ipAddress = dto.getIpAddress();
		String loginName = dto.getLoginName();
		Date currentDate = DateUtil.getCurrentTimestamp();
		
		if (StringUtils.isBlank(title)) {
			
			return "[提示]主题不能为空！";
		}
		
		if (StringUtils.isBlank(content)) {
			
			return "[提示]内容不能为空！";
		}
		
		if (StringUtils.isBlank(ipAddress)) {
			
			return "[提示]ip地址不能为空！";
		}
		
		if (StringUtils.isBlank(loginName)) {
			
			return "[提示]登录用户不能为空！";
		}
		
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setFlag(Constant.TOPIC_INFO_FLAG_APPROVAL);
		topicInfo.setIpAddress(ipAddress);
		topicInfo.setCreateTime(currentDate);
		topicInfo.setTitle(title);
		topicInfo.setContent(content);
		topicInfo.setTopicType(Constant.TOPIC_TOPIC_TYPE_ADMIN);
		topicInfo.setUserNameType(Constant.TOPIC_SEND_TYPE_ADMIN);
		topicInfo.setTopicStatus(Constant.TOPIC_STATUS_PERSONAL);
		topicInfo.setIsAdminRead(Constant.TOPIC_STATUS_IS_READ_NO);
		topicInfo.setCreateUname(loginName);
		
		masterTopicInfoDao.insert(topicInfo);
		
		TopicStatus topicStatus = new TopicStatus();
		topicStatus.setCreateTime(currentDate);
		topicStatus.setTopicId(topicInfo.getId());
		topicStatus.setIpAddress(ipAddress);
		topicStatus.setIsUserRead(Constant.TOPIC_STATUS_IS_READ_YES);
		topicStatus.setReceiveUname("客服管理员");
		
		masterTopicStatusDao.insert(topicStatus);
		
		return null;
	}
	
	/**
	 * 新增玩家回帖
	 */
	public String saveReplyInfo(TopicInfoDTO dto) throws Exception {
		
		Integer topicId = dto.getTopicId();
		String title = dto.getTitle();
		String content = dto.getContent();
		String ipAddress = dto.getIpAddress();
		String loginName = dto.getLoginName();
		
		if (null == topicId) {
			
			return "[提示]站内信ID不能为空！";
		}
		
		if (StringUtils.isBlank(title)) {
			
			return "[提示]主题不能为空！";
		}
		
		if (StringUtils.isBlank(content)) {
			
			return "[提示]内容不能为空！";
		}
		
		if (StringUtils.isBlank(ipAddress)) {
			
			return "[提示]ip地址不能为空！";
		}
		
		if (StringUtils.isBlank(loginName)) {
			
			return "[提示]登录用户不能为空！";
		}
		
		ReplyInfo replyInfo = new ReplyInfo();
		replyInfo.setIpAddress(ipAddress);
		replyInfo.setCreateTime(DateUtil.getCurrentTimestamp());
		replyInfo.setContent(content);
		replyInfo.setReplyType(Constant.TOPIC_TOPIC_TYPE_ADMIN);
		replyInfo.setCreateUname(loginName);
		
		masterReplyInfoDao.insert(replyInfo);
		
		return null;
	}
	
	/**
	 * 获取未读站内信数据
	 */
	public Integer getUnReadTopicCount(Map<String, Object> params) throws Exception {
		
		List<TopicInfo> topicList = slaveTopicInfoDao.findPublicMsg(params);
		
		if (topicList != null && !topicList.isEmpty()) {
			
			Date currentDate = DateUtil.getCurrentTimestamp();
			
			for (TopicInfo topicInfo : topicList) {
				
				TopicStatus topicStatus = new TopicStatus();
				topicStatus.setCreateTime(currentDate);
				topicStatus.setTopicId(topicInfo.getId());
				topicStatus.setIpAddress(String.valueOf(params.get("ipAddress")));
				topicStatus.setIsUserRead(Constant.TOPIC_STATUS_IS_READ_NO);
				topicStatus.setReceiveUname(String.valueOf(params.get("loginName")));
				
				masterTopicStatusDao.insert(topicStatus);
			}
		}
		
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DAY_OF_MONTH, -16);
		
		TopicStatus topicStatus = new TopicStatus();
		topicStatus.setCreateTime(cd.getTime());
		topicStatus.setReceiveUname(String.valueOf(params.get("loginName")));
		
		return slaveTopicStatusDao.findUnReadNum(topicStatus);
	}
	
	/**
	 * 查询玩家的站内信
	 */
	public Page<TopicInfo> findTopicByUserNamePage(int pageNum, int pageSize, String loginName) throws Exception {
		
		PageHelper.startPage(pageNum, pageSize, true);
		
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setCreateUname(loginName);
		
		return slaveTopicInfoDao.findTopicByUserNamePage(topicInfo);
	}
	
	/**
	 * 查询玩家自己发的站内信
	 */
	public Page<TopicInfo> findMySelfTopicPage(int pageNum, int pageSize, String loginName) throws Exception {
		
		PageHelper.startPage(pageNum, pageSize, true);
		
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setCreateUname(loginName);
		

		return slaveTopicInfoDao.findMySelfTopicPage(topicInfo);
	}
	
	/**
	 * 删除topicId
	 */
	public String deleteTopicInfo(Map<String, Object> params) throws Exception {
		//进行逻辑删除修改是否有效 0 有效1无效
		params.put("isValid", Constant.TOPIC_IS_VALID_NO);
		masterTopicStatusDao.update(params);
		
		return "删除成功";
	}
	/**
	 * 批量删除topicId
	 */
	public String deleteTopic(Map<String, Object> params) throws Exception {
		//进行逻辑删除修改是否有效 0 有效1无效
		params.put("isValid", Constant.TOPIC_IS_VALID_NO);
		masterTopicStatusDao.deleteTopic(params);
		
		return "删除成功";
	}
	
	/**
	 * 修改未读状态
	 */
	public String updateTopicUnRead(Map<String, Object> params) throws Exception {
		
		Integer statusId = Integer.parseInt(String.valueOf(params.get("statusId")));
		
		TopicStatus topicStatus = slaveTopicStatusDao.findTopicStatusById(statusId);
		
		if (null == topicStatus) {
			
			return "id = " + statusId + " 的站内信不存在！";
		}
		
		// 修改站内信已读状态 // 已读1和未读0
		params.put("isUserRead", Constant.TOPIC_STATUS_IS_READ_YES);
		
		masterTopicStatusDao.update(params);
		
		return "修改成功";
	}
}