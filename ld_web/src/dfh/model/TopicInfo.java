package dfh.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * topicinfo entity. by boots.
 * 
 * @author MyEclipse Persistence Tools
 */
public class TopicInfo implements java.io.Serializable {

	public TopicInfo() {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725122327776327839L;

	private Integer topicId;

	private Integer id;
	// 主题
	private String title;
	// 内容
	private String content;
	// 创建时间
	private Date createTime;
	// 创建人
	private String createUname;
	// 创建人ip地址
	private String ipAddress;
	// 是否审批
	private Integer flag;
	// 回复数量
	private Integer reCount;
	// 发帖类型
	private Integer topicType;
	// 发帖对象
	private Integer userNameType;
	// 群发
	private Integer topicStatus;

	private Integer isUserRead;

	private Integer isAdminRead;
	
	private Integer statusId;
	
	// 创建时间
	private String createTimeStr;

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIsAdminRead() {
		return isAdminRead;
	}

	public void setIsAdminRead(Integer isAdminRead) {
		this.isAdminRead = isAdminRead;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer getIsUserRead() {
		return isUserRead;
	}

	public void setIsUserRead(Integer isUserRead) {
		this.isUserRead = isUserRead;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUname() {
		return createUname;
	}

	public void setCreateUname(String createUname) {
		this.createUname = createUname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getReCount() {
		return reCount;
	}

	public void setReCount(Integer reCount) {
		this.reCount = reCount;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public Integer getUserNameType() {
		return userNameType;
	}

	public void setUserNameType(Integer userNameType) {
		this.userNameType = userNameType;
	}

	public Integer getTopicStatus() {
		return topicStatus;
	}

	public void setTopicStatus(Integer status) {
		this.topicStatus = status;
	}

	/**
	 * 
	 * @param flag
	 *            0 已审批 1 未审批
	 * @param reCount
	 *            回复数
	 * @param ipAddress
	 *            ip地址
	 * @param username
	 *            创建人
	 * @param createTime
	 *            创建时间
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param topicType
	 *            0发帖给玩家 1发帖给管理员
	 * @param userNameType
	 *            100 全部会员 101全部代理 102个人群发 103代表是按照客服推荐码群发104代表是按照代理账号群发
	 *            105代表管理员 大于0对应玩家等级
	 */
	public TopicInfo(Integer flag, Integer reCount, String ipAddress, String username, Timestamp createTime,
			String title, String content, Integer topicType, Integer userNameType) {
		this.flag = flag;
		this.reCount = reCount;
		this.ipAddress = ipAddress;
		this.createUname = username;
		this.createTime = createTime;
		this.title = title;
		this.content = content;
		this.topicType = topicType;
		this.userNameType = userNameType;
	}
}