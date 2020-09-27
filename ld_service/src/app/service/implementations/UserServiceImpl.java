package app.service.implementations;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import app.dao.BaseDao;
import app.model.po.AppPackageVersion;
import app.model.po.AppPackageVersionCustom;
import app.model.po.AppVersion;
import app.model.po.LatestPreferential;
import app.model.po.SpecialTopic;
import app.model.vo.AgentSettingVO;
import app.model.vo.AnnouncementForAppVO;
import app.model.vo.AppCustomVersionVO;
import app.model.vo.AutoXimaVO;
import app.model.vo.BasicInfoVO;
import app.model.vo.BasicRequestVO;
import app.model.vo.ChangePasswordVO;
import app.model.vo.HotGameLoginVO;
import app.model.vo.IndexSpecialTopicVO;
import app.model.vo.LatestPreferentialVO;
import app.model.vo.LoginInfoVO;
import app.model.vo.OnlinePayRecordVO;
import app.model.vo.OnlinePayVO;
import app.model.vo.PopAnnouncementVO;
import app.model.vo.SmsCustomizationRequestDataVO;
import app.model.vo.UnReadVO;
import app.model.vo.UserBankInfoVO;
import app.model.vo.UserbankVO;
import app.model.vo.UsersVO;
import app.model.vo.WithdrawalTipsVO;
import app.service.interfaces.IQuestionService;
import app.service.interfaces.ISequenceService;
import app.service.interfaces.IUserBankInfoService;
import app.service.interfaces.IUserService;
import app.util.DateUtil;
import app.util.FTPUtil;
import dfh.action.vo.AutoXima;
import dfh.action.vo.DtVO;
import dfh.model.Actionlogs;
import dfh.model.AgentAddress;
import dfh.model.Announcement;
import dfh.model.Const;
import dfh.model.DepositOrder;
import dfh.model.DetailPoint;
import dfh.model.Friendbonusrecord;
import dfh.model.Friendintroduce;
import dfh.model.OnlineToken;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.SystemConfig;
import dfh.model.TotalPoint;
import dfh.model.Transfer;
import dfh.model.UserSidName;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.NTErrorCode;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.model.enums.WarnLevel;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.IGGameinfoService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SlaveService;
import dfh.service.interfaces.TransferService;
import dfh.skydragon.webservice.UserWebServiceWS;
import dfh.utils.Constants;
import dfh.utils.DtUtil;
import dfh.utils.MGSUtil;
import dfh.utils.NTUtils;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.PagenationUtil;
import dfh.utils.QtUtil;
import dfh.utils.QtUtilForTp;
import dfh.utils.SpringFactoryHepler;
import dfh.utils.StringUtil;
import dfh.utils.sendemail.AESUtil;

public class UserServiceImpl implements IUserService {
	
	private Logger log = Logger.getLogger(UserServiceImpl.class);
    
	private BaseDao baseDao;
	
	private ISequenceService sequenceService;
	
	private StandardPBEStringEncryptor configurationEncryptor;
	
	private IGGameinfoService gameinfoService;
	
	private TransferService transferService;
	
	private SlaveService slaveService;

	private IQuestionService questionService;
	
	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public ISequenceService getSequenceService() {
		return sequenceService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public StandardPBEStringEncryptor getConfigurationEncryptor() {
		return configurationEncryptor;
	}

	public void setConfigurationEncryptor(StandardPBEStringEncryptor configurationEncryptor) {
		this.configurationEncryptor = configurationEncryptor;
	}

	public IGGameinfoService getGameinfoService() {
		return gameinfoService;
	}

	public void setGameinfoService(IGGameinfoService gameinfoService) {
		this.gameinfoService = gameinfoService;
	}

	public TransferService getTransferService() {
		return transferService;
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	public SlaveService getSlaveService() {
		return slaveService;
	}

	public void setSlaveService(SlaveService slaveService) {
		this.slaveService = slaveService;
	}
	
	public IQuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(IQuestionService questionService) {
		this.questionService = questionService;
	}

	@SuppressWarnings("rawtypes")
	public Object get(Class entityClass, Serializable id) {
		
		return baseDao.get(entityClass, id);
	}
	
	public Serializable save(Object entity) {
		
		return baseDao.save(entity);
	}	
	
    public LoginInfoVO userRegister(UsersVO userVo) {
    	
    	LoginInfoVO loginInfo = new LoginInfoVO();
    	String loginname = userVo.getLoginname().toLowerCase();
    	
    	Users userInDb = (Users) get(Users.class,loginname);
    	if(userInDb != null){
    		loginInfo.setCode("10001");
    		loginInfo.setMsg("账号已经存在");
    		return loginInfo;
    	}
    	
		Users newUser = new Users();
		newUser.setWarnflag(WarnLevel.WEIZHI.getCode());
		newUser.setRole(UserRole.MONEY_CUSTOMER.getCode());
		newUser.setLoginname(loginname);
		newUser.setPassword(EncryptionUtil.encryptPassword(userVo.getPassword()));
		newUser.setLevel(VipLevel.TIANBING.getCode());
		newUser.setAccountName(userVo.getAccountName());
		newUser.setAliasName(userVo.getAliasName());
		newUser.setQq(userVo.getQq());
		newUser.setMicrochannel(userVo.getWechat());
		newUser.setMd5str(StringUtil.getRandomString(8));
		newUser.setReferWebsite(userVo.getReferWebsite());
		newUser.setPwdinfo(configurationEncryptor.encrypt(userVo.getPassword()));
		newUser.setCredit(Constants.CREDIT_ZERO);
		newUser.setCreatetime(DateUtil.getCurrentTime());
		newUser.setRegisterIp(userVo.getRegisterIp());
		newUser.setArea(userVo.getArea());
		newUser.setFlag(userVo.getFlag());
		newUser.setRemark(userVo.getRemark());
		newUser.setSms(0);
		newUser.setPasswdflag(1);
		newUser.setInvitecode(userVo.getInvitecode());
		newUser.setHowToKnow(userVo.getHowToKnow());
		newUser.setId(Integer.parseInt(sequenceService.generateUserId()));
		newUser.setPostcode(StringUtil.getRandomString(10));
		if(StringUtils.isNotBlank(userVo.getBirthday())){
			newUser.setBirthday(DateUtil.getDateByDay(userVo.getBirthday()));
		}
		try {
			newUser.setPhone(AESUtil.aesEncrypt(userVo.getPhone(),AESUtil.KEY));
			newUser.setEmail(AESUtil.aesEncrypt(userVo.getEmail(),AESUtil.KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 新增业务逻辑：使用同一个IP注册的账号，除第一个账号（未知状态）外，其他的账号状态都设置为危险状态
		String sql = "select count(1) from users where registerIp = :registerIp";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("registerIp", userVo.getRegisterIp());
		
		Object obj = baseDao.uniqueResult(sql, params);
		
		if (null != obj) {
		
			newUser.setWarnflag(WarnLevel.WEIXIAN.getCode());
		}
		
		baseDao.save(newUser);
		Userstatus status = new Userstatus();
		status.setLoginname(loginname);
		status.setCashinwrong(0);
		status.setLoginerrornum(0);
		baseDao.save(status);
		
		loginInfo.setUser(newUser);
		loginInfo.setCode("10000");
    	return loginInfo;
    }
    
	public LoginInfoVO userLogin(UsersVO userVo) {
    	
    	LoginInfoVO loginInfo = null;
    	UserSidName userSidName = null;
    	
    	if(StringUtils.isBlank(userVo.getLoginname())){
			loginInfo = new LoginInfoVO("10001","该会员不存在");
			log.warn(loginInfo.getMsg());
			return loginInfo;
    	}
    	Users user = (Users) baseDao.get(Users.class, userVo.getLoginname().toLowerCase());
		if (user == null) {
			loginInfo = new LoginInfoVO("10001","该会员不存在");
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		if (user.getFlag().intValue() == Constants.DISABLE.intValue() && user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
			loginInfo = new LoginInfoVO("10002","亲爱的玩家，该帐号已被禁用,请联系在线客服处理您的问题");
			log.warn(loginInfo.getMsg());
			return loginInfo;
		} 
		if (user.getFlag().intValue() == Constants.DISABLE.intValue() && user.getRole().equals(UserRole.AGENT.getCode())) {
			loginInfo = new LoginInfoVO("10003","代理账号核实中......");
			log.warn(loginInfo.getMsg());
			return loginInfo;
		} 
		if (user.getFlag().intValue() == 2) {
			loginInfo = new LoginInfoVO("10004","密码输入错误次数过多，账号被锁!请使用找回密码功能!");
			log.warn(loginInfo.getMsg());
			return loginInfo;
		} 
		user.setLastLoginIp(userVo.getLastLoginIp());
		
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(userVo.getLoginname().toLowerCase());
		actionlog.setCreatetime(DateUtil.getCurrentTime());
		actionlog.setAction(ActionLogType.LOGIN.getCode());
		actionlog.setRemark("ip:" + userVo.getLastLoginIp() + ";最后登录地址：" + userVo.getLastarea() + ";客户操作系统：" + userVo.getOs());
		
		if (!user.getPassword().equals(EncryptionUtil.encryptPassword(userVo.getPassword()))) {
			
            actionlog.setRemark(userVo.getRegisterIp());
            actionlog.setAction(ActionLogType.WRONG_PWD.getCode());
            
			
            Userstatus userstatus = (Userstatus) baseDao.get(Userstatus.class, userVo.getLoginname().toLowerCase());
            
            if(userstatus.getLoginerrornum() == null){
            	userstatus.setLoginerrornum(0);
            }
            
            userstatus.setLoginerrornum(userstatus.getLoginerrornum() + 1);
            
            baseDao.update(userstatus);
            
            if(userstatus.getLoginerrornum() == 5){
            	actionlog.setAction(ActionLogType.PWD_ERROR_5.getCode());
            	user.setFlag(2);
            	user.setRemark(ActionLogType.PWD_ERROR_5.getText());
            	baseDao.update(user);
            }
            
            baseDao.save(actionlog);
            
			loginInfo = new LoginInfoVO("10005","密码错误,你还有" + (5 - userstatus.getLoginerrornum()) + "次机会");
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		
		Userstatus userstatus = (Userstatus) baseDao.get(Userstatus.class, userVo.getLoginname().toLowerCase());
		if(userstatus.getLoginerrornum() != null && userstatus.getLoginerrornum() > 0){
			userstatus.setLoginerrornum(0);
			baseDao.update(userstatus);
		}
		//防止一账号多处登录，转账，提款操作时判断此postcode是否与session中的一致
		user.setPostcode(getPostCode(userVo.getSid()));
		user.setLastLoginTime(new Date());
		user.setLoginTimes(user.getLoginTimes() + 1);
		user.setLastarea(userVo.getLastarea());
		baseDao.update(user);
		
		baseDao.save(actionlog);
		
		try {
			
			DetachedCriteria userSidNameDc = DetachedCriteria.forClass(UserSidName.class);
			userSidNameDc.add(Restrictions.eq("loginname",userVo.getLoginname()));
			userSidNameDc.add(Restrictions.eq("sid",userVo.getSid()));
			
			List<UserSidName> listUserSid = baseDao.findByCriteria(userSidNameDc,0,1);
			
			if(listUserSid == null || listUserSid.isEmpty()){
				
				userSidName = new UserSidName();
				userSidName.setLoginname(userVo.getLoginname());
				userSidName.setSid(userVo.getSid());
				userSidName.setOs(StringUtils.isEmpty(userVo.getOs()) ? "" : userVo.getOs());
				userSidName.setOsVersion(StringUtils.isEmpty(userVo.getOsVersion()) ? "" : userVo.getOsVersion());
				userSidName.setMobileModel(StringUtils.isEmpty(userVo.getMobileModel()) ? "" : userVo.getMobileModel());
				userSidName.setImei(userVo.getImei());
				userSidName.setImsi(userVo.getImsi());
				userSidName.setMacAddress(userVo.getMacAddress());
				userSidName.setEmulator(userVo.getEmulator());
				baseDao.save(userSidName);
				
			}else{
				
				userSidName = listUserSid.get(0);
				userSidName.setOs(StringUtils.isEmpty(userVo.getOs()) ? "" : userVo.getOs());
				userSidName.setOsVersion(StringUtils.isEmpty(userVo.getOsVersion()) ? "" : userVo.getOsVersion());
				userSidName.setMobileModel(StringUtils.isEmpty(userVo.getMobileModel()) ? "" : userVo.getMobileModel());
				userSidName.setImei(userVo.getImei());
				userSidName.setImsi(userVo.getImsi());
				userSidName.setMacAddress(userVo.getMacAddress());
				userSidName.setEmulator(userVo.getEmulator());
				
				baseDao.update(userSidName);
			}
			
		} catch (Exception e) {
			log.info("sid和loginname联合主键相同，不能insert进数据库表user_sid_name");
		} finally{
			loginInfo = new LoginInfoVO("10000","登录成功");
			loginInfo.setUser(user);
		}
        return loginInfo;
    }

	public LoginInfoVO loginByToken(UsersVO userVo) {

		LoginInfoVO loginInfo = null;

		String message = "账号异常，请重新登录";

		if (StringUtils.isBlank(userVo.getLoginname())) {
			loginInfo = new LoginInfoVO("10001", message);
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		Users user = (Users) baseDao.get(Users.class, userVo.getLoginname()
				.toLowerCase());
		if (user == null) {
			loginInfo = new LoginInfoVO("10001", message);
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		if (user.getFlag().intValue() == Constants.DISABLE.intValue()
				&& user.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
			loginInfo = new LoginInfoVO("10002", message);
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		if (user.getFlag().intValue() == Constants.DISABLE.intValue()
				&& user.getRole().equals(UserRole.AGENT.getCode())) {
			loginInfo = new LoginInfoVO("10003", message);
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		if (user.getFlag().intValue() == 2) {
			loginInfo = new LoginInfoVO("10004", message);
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}
		if (!user.getPassword().equals(
				EncryptionUtil.encryptPassword(userVo.getPassword()))) {

			loginInfo = new LoginInfoVO("10005", "账号异常，请重新登录");
			log.warn(loginInfo.getMsg());
			return loginInfo;
		}

		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(userVo.getLoginname().toLowerCase());
		actionlog.setCreatetime(DateUtil.getCurrentTime());
		actionlog.setAction(ActionLogType.LOGIN.getCode());
		actionlog.setRemark("ip:" + userVo.getLastLoginIp() + ";最后登录地址："
				+ userVo.getLastarea() + ";客户操作系统：" + userVo.getOs());

		// 防止一账号多处登录，转账，提款操作时判断此postcode是否与session中的一致
		user.setLastLoginIp(userVo.getLastLoginIp());
		user.setPostcode(getPostCode(userVo.getSid()));
		user.setLastLoginTime(new Date());
		user.setLoginTimes(user.getLoginTimes() + 1);
		user.setLastarea(userVo.getLastarea());

		baseDao.update(user);
		baseDao.save(actionlog);

		loginInfo = new LoginInfoVO("10000", "登录成功");
		loginInfo.setUser(user);
		return loginInfo;
	}
	
	
	@Override
	public BasicInfoVO getBasicInfo(BasicRequestVO requestVO) {
		
		BasicInfoVO vo = new BasicInfoVO();
		
		//获取系统公告
		DetachedCriteria AnnouncementDc = DetachedCriteria.forClass(Announcement.class);
		AnnouncementDc.add(Restrictions.eq("type", "INDEX_TOP"));
		AnnouncementDc.addOrder(Order.desc("createtime"));
		List<Announcement> listDb = baseDao.findByCriteria(AnnouncementDc,0,requestVO.getNum());
		List<AnnouncementForAppVO> listBack = null;
		if(listDb != null && listDb.size() > 0 ){
			listBack = new ArrayList<AnnouncementForAppVO>(listDb.size());
			for(Announcement obj : listDb){
				AnnouncementForAppVO announcementForAppVO = new AnnouncementForAppVO();
				announcementForAppVO.setId(obj.getId());
				announcementForAppVO.setTitle(obj.getTitle());
				announcementForAppVO.setType(obj.getType());
				announcementForAppVO.setContent(obj.getContent());
				announcementForAppVO.setCreatetime(DateUtil.getDateFormat(obj.getCreatetime()));
				listBack.add(announcementForAppVO);
			}
		}
		
		//获取弹窗公告
		DetachedCriteria systemConfigDc = DetachedCriteria.forClass(SystemConfig.class);
		systemConfigDc.add(Restrictions.eq("typeNo", "type199"));
		systemConfigDc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> configList = baseDao.findByCriteria(systemConfigDc,0,1);
		if(configList != null && configList.size() > 0 ){
		   SystemConfig popAnnouncement = (SystemConfig)configList.get(0);
		   PopAnnouncementVO popAnnouncementVO = new PopAnnouncementVO();
		   popAnnouncementVO.setItemNo(popAnnouncement.getItemNo());
		   popAnnouncementVO.setNote(popAnnouncement.getNote());
		   popAnnouncementVO.setTypeName(popAnnouncement.getTypeName());
		   popAnnouncementVO.setValue(popAnnouncement.getValue());
           vo.setPopAnnouncement(popAnnouncementVO);
		}
		vo.setAnnouncementList(listBack);
		
		//获取专题 ,已改为从缓存中读取，注释掉
		
/*		String topicSql = "select t.picture_url,t.topic_url from app_specialTopic t where t.status = 1 order by t.createTime desc";
		
		List<Object[]> topicList = baseDao.list(topicSql, null);
		
		List<IndexSpecialTopicVO> indextopicList = new ArrayList<IndexSpecialTopicVO>();
		if(topicList != null && topicList.size() > 0){
			
			for(Object[] obj : topicList){
				
				IndexSpecialTopicVO indexSpecialTopicVO = new IndexSpecialTopicVO();
				
				String url = String.valueOf(obj[0]);
				String imageName = url.substring(url.lastIndexOf("/") + 1);
				String pictureUrl = FTPUtil.getValue("topic_image_download_url") + imageName;
				
				indexSpecialTopicVO.setPictureUrl(pictureUrl);
				indexSpecialTopicVO.setUrl((String)obj[1]);
				indextopicList.add(indexSpecialTopicVO);
			}
			vo.setIndexSpecialTopicList(indextopicList);
		}*/
		
		// 获取专题 ,因为缓存出现严重问题 故 加判断逻辑后从数据库中查出
		
		String topicSql  = "SELECT  id,  LEFT(picture_url, 256),  LEFT(topic_url, 256),  status,  title,  createTime,  LEFT(action_url, 256) FROM app_specialTopic where status=1 order by createTime desc LIMIT 5";
		List<Object[]> topicListFromDb = baseDao.list(topicSql, null);
		
		List<IndexSpecialTopicVO> indextopicList = new ArrayList<IndexSpecialTopicVO>();
		
		if(topicListFromDb != null && topicListFromDb.size() > 0){
			
			for(Object[] topic : topicListFromDb){
				
				IndexSpecialTopicVO ivo = new IndexSpecialTopicVO();
				
				String pictureUrl = String.valueOf(topic[1]);
				String imageName  = pictureUrl.substring(pictureUrl.lastIndexOf("/") + 1);
				
				ivo.setPictureUrl(FTPUtil.getValue("topic_image_download_url") + imageName);
				ivo.setUrl(String.valueOf(topic[2]));
				ivo.setActionUrl(String.valueOf(topic[6]));
				indextopicList.add(ivo);
			}
		}
		vo.setIndexSpecialTopicList(indextopicList);
		
		
		//首页热门优惠,因为缓存出现严重问题 故 加判断逻辑后从数据库中查出
//		String hotInfoesSql  = "SELECT  id,  type,  activity_title,  LEFT(activity_summary, 256),  LEFT(activity_content, 256),  activity_image_url,  activity_start_time,  activity_end_time,  is_new,  new_image_url,  is_active,  is_phone,  receive_number,  create_time,  created_user,  update_time,  updated_user,  LEFT(openUrl, 256),  isQyStyle "
//										+ "FROM latest_preferential "
//										+"where is_new =1 and is_active = 1 and is_phone =1 "
//										+ "order by receive_number desc "
//										+ "LIMIT 4";
//		List<Object[]> hotInfoesListFromDb = baseDao.list(hotInfoesSql, null);
//
//		List<LatestPreferentialVO> hotInfoesForCache = new ArrayList<LatestPreferentialVO>();
//
//		if (hotInfoesListFromDb != null && !hotInfoesListFromDb.isEmpty()) {
//
//			String baseUrl = FTPUtil.getValue("preferential_image_download_url");
//
//			for (Object[] obj : hotInfoesListFromDb) {
//
//				LatestPreferentialVO lvo = new LatestPreferentialVO();
//
//				lvo.setId(Integer.valueOf(String.valueOf(obj[0])));
//				lvo.setType(String.valueOf(obj[1]));
//				lvo.setOpenUrl(String.valueOf(obj[17]));
//
//				if (obj[6] != null) {
//
//					lvo.setActivityStartTime(String.valueOf(obj[6]));
//				}
//				if (obj[7] != null) {
//
//					lvo.setActivityEndTime(String.valueOf(obj[7]));
//				}
//
//				if (StringUtils.isNotEmpty(String.valueOf(obj[5]))) {
//
//					String activityImageName = String.valueOf(obj[5])
//							.substring(String.valueOf(obj[5]).lastIndexOf("/") + 1);
//					lvo.setActivityImageUrl(baseUrl + activityImageName);
//
//				}
//				if (StringUtils.isNotEmpty(String.valueOf(obj[9]))) {
//
//					String newImageName = String.valueOf(obj[9]).substring(String.valueOf(obj[9]).lastIndexOf("/") + 1);
//					lvo.setNewImageUrl(baseUrl + newImageName);
//
//				}
//
//				hotInfoesForCache.add(lvo);
//			}
//			vo.setLatestPreferentialList(hotInfoesForCache);
//		}
//		
		//获取版本检测信息(版本格式   version = 1.0.0 )
		if(StringUtils.isNotBlank(requestVO.getAgcode()) && !(requestVO.getAgcode().equalsIgnoreCase("official"))){
			DetachedCriteria versionDc = DetachedCriteria.forClass(AppPackageVersion.class);
			versionDc.add(Restrictions.eq("status", 1)); //启用
			versionDc.add(Restrictions.eq("plat", requestVO.getPlat()));
			versionDc.add(Restrictions.gt("versionCode", requestVO.getVersion())); 
			versionDc.addOrder(Order.desc("createTime"));
			List<AppPackageVersion> versionList = slaveService.findByCriteria(versionDc);
			if(versionList != null && !versionList.isEmpty()){
				AppPackageVersion version = versionList.get(0);
				DetachedCriteria versionCustomDc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
				versionCustomDc.add(Restrictions.eq("versionId", version.getId())); 
				versionCustomDc.add(Restrictions.eq("status", 1)); 
				versionCustomDc.add(Restrictions.eq("agentCode", requestVO.getAgcode()));
				versionCustomDc.add(Restrictions.eq("pakStatus", 2)); //打包完成
				List<AppPackageVersionCustom> versionCustomList = slaveService.findByCriteria(versionCustomDc);
				if(versionCustomList != null && !versionCustomList.isEmpty()){
					AppPackageVersionCustom versionCustom = versionCustomList.get(0);
					AppVersion appVersion = new AppVersion();
					appVersion.setPlat(versionCustom.getPlat());
					appVersion.setTitle(version.getVersionTitle());
					appVersion.setVersion(versionCustom.getVersionCode());
					appVersion.setUpgrade_context(version.getUpgradeLog());
					appVersion.setForce(versionCustom.getIsForceUpgrade() ? "Y":"N");
					appVersion.setDownload_url(versionCustom.getPackageUrl());
					appVersion.setCreate_time(versionCustom.getModifyTime());
					vo.setVersionInfo(appVersion);
				}
			}
		}
		else{
			DetachedCriteria versionDc = DetachedCriteria.forClass(AppVersion.class);
			versionDc.add(Restrictions.eq("plat", requestVO.getPlat()));
			versionDc.addOrder(Order.desc("create_time"));
			List<AppVersion> versionList = slaveService.findByCriteria(versionDc);
			
			if(versionList != null && versionList.size() > 0){
				
				AppVersion version = versionList.get(0);
						
					if(requestVO.getVersion().compareTo(version.getVersion()) < 0){//app版本小于服务器版本，有版本升级信息
						
						vo.setVersionInfo(version);
						
					}
			}
		}
		
		
		ComboPooledDataSource dataSource = (ComboPooledDataSource)SpringFactoryHepler.getInstance("dataSource");
		ComboPooledDataSource dataSourceSlave = (ComboPooledDataSource)SpringFactoryHepler.getInstance("dataSourceSlave");
		try {
			log.info("dataSource.getNumConnections() = " + dataSource.getNumConnections());
			log.info("dataSourceSlave.getNumConnections() = " + dataSourceSlave.getNumConnections());
			//tail -f   qytomcat01/logs/catalina.out | grep '.getNumConnections()' 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return vo;
	}

	@Override
	public UnReadVO getUnReadInfo(String loginname) {
		UnReadVO vo = new UnReadVO();
		vo.setUnReadStationletters(0);
		return vo;
	}
	@Override
	public UsersVO getUserInfo(String loginname,IUserBankInfoService userBankInfoService) {
		UsersVO vo = new UsersVO();
		Users userFromDb = (Users) baseDao.get(Users.class,loginname.toLowerCase());
		if(userFromDb != null){
			vo.setLoginname(userFromDb.getLoginname());
			vo.setAccountName(userFromDb.getAccountName() == null ? "" : userFromDb.getAccountName());
			vo.setAddress(userFromDb.getAddress());
			if(userFromDb.getBirthday() != null){
				vo.setBirthday(DateUtil.getDateFormat(userFromDb.getBirthday(),"yyyy-MM-dd"));
			}
			vo.setRole(UserRole.getText(userFromDb.getRole()));
			vo.setLevel(VipLevel.getText(userFromDb.getLevel()));
			vo.setLevelCode(userFromDb.getLevel() + "");
			vo.setCreatetime(DateUtil.getDateFormat(userFromDb.getCreatetime()));
			vo.setCredit(userFromDb.getCredit() == null ? 0 : Math.round(userFromDb.getCredit()*100)/100.0);
			vo.setAliasName(userFromDb.getAliasName());
			vo.setRegisterIp(userFromDb.getRegisterIp());
			vo.setLastLoginIp(userFromDb.getLastLoginIp());
			vo.setQq(userFromDb.getQq());
			vo.setWechat(userFromDb.getMicrochannel());
			vo.setBirthday(DateUtil.getDateFormatYYYY_MM_DD(userFromDb.getBirthday()));
			vo.setLastLoginTime(DateUtil.getDateFormat(userFromDb.getLastLoginTime()));
			try {
				vo.setPhone(AESUtil.aesDecrypt(userFromDb.getPhone(),AESUtil.KEY));
				vo.setEmail(AESUtil.aesDecrypt(userFromDb.getEmail(),AESUtil.KEY));
			} catch (Exception e) {
			}
		}
		
		
		UserBankInfoVO userBankInfoVO = new UserBankInfoVO();
		userBankInfoVO.setLoginName(loginname);
		List<dfh.model.Userbankinfo> list = userBankInfoService.queryUserBankInfoList(userBankInfoVO);
		
		List<UserbankVO> availableBankList = new ArrayList<UserbankVO>();
		
		if(list != null && list.size() > 0 ){
			
			for (dfh.model.Userbankinfo info : list) {
				UserbankVO userbankVO = new UserbankVO();
				userbankVO.setAddtime(DateUtil.getDateFormat(info.getAddtime(),"yyyy-MM-dd"));
				userbankVO.setBankaddress(info.getBankaddress());
				userbankVO.setBankname(info.getBankname());
				userbankVO.setBankno(info.getBankno());
				userbankVO.setFamilyName(userFromDb.getAccountName());
				availableBankList.add(userbankVO);
			}
			
			vo.setAvailableBankList(availableBankList);
		}
		
		Integer questionNumber = questionService.countQuestion(loginname);
		vo.setQuestionNumber(questionNumber);
		
		//获取积分余额
		
		DetachedCriteria dc = DetachedCriteria.forClass(TotalPoint.class);
		dc.add(Restrictions.eq("username", loginname));
		
		List<TotalPoint> PointList = this.slaveService.findByCriteria(dc);
		
		if(PointList != null && PointList.size() > 0){
			
			TotalPoint totalPoint = PointList.get(0);
			
			Double mul=500.0;//默认新会元
			if(userFromDb.getLevel()==1){
				mul=400.0;	
			}else if(userFromDb.getLevel()==2){
				mul=335.0;	
			}else if(userFromDb.getLevel()==3){
				mul=285.0;	
			}else if(userFromDb.getLevel()==4){
				mul=250.0;	
			}else if(userFromDb.getLevel()==5){
				mul=220.0;	
			}else if(userFromDb.getLevel()==6){
				mul=200.0;	
			}
			
		    //int totalmoney=(int) (totalPoint.getTotalpoints()/mul);
			Double totalmoney = totalPoint.getTotalpoints();
			
			vo.setPointBalance(totalmoney + "");
			
		}else{
			
			vo.setPointBalance("0.00");
		}
		
		
		
		//短信定制初始化数据
		
		String address = userFromDb.getAddress();
		SmsCustomizationRequestDataVO smsCustomizationRequestDataVO = new SmsCustomizationRequestDataVO();
		
		smsCustomizationRequestDataVO.setChangePassword("N");
		smsCustomizationRequestDataVO.setDepositExecut("N");
		smsCustomizationRequestDataVO.setSelfPromotion("N");
		smsCustomizationRequestDataVO.setWithdrawalAppliy("N");
		
		if(StringUtils.isEmpty(address)){
			
			address = "nothing";
		}
		
		if(address.contains("3")){
			
			smsCustomizationRequestDataVO.setChangePassword("Y");
			
		}
		if(address.contains("9")){
			
			smsCustomizationRequestDataVO.setDepositExecut("Y");
			
		}
		if(address.contains("2")){
			
			smsCustomizationRequestDataVO.setSelfPromotion("Y");
			
		}
		if(address.contains("5")){
			
			smsCustomizationRequestDataVO.setWithdrawalAppliy("Y");
			
		}
		
		vo.setSmsCustomizationInit(smsCustomizationRequestDataVO);
		
		
		//提款提示信息
		
		DetachedCriteria systemConfigDc = DetachedCriteria.forClass(SystemConfig.class);
		systemConfigDc.add(Restrictions.eq("typeNo", "type333"));
		systemConfigDc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> configList = slaveService.findByCriteria(systemConfigDc,0,1);
		
		if(configList != null && !configList.isEmpty()){
			
			SystemConfig config = configList.get(0);
			
			WithdrawalTipsVO withdrawalTipsVO = new WithdrawalTipsVO();
			withdrawalTipsVO.setTitle(config.getTypeName());
			withdrawalTipsVO.setContext(config.getValue());
			
			vo.setWithdrawalTips(withdrawalTipsVO);
		}
		
		
		return vo;
	}

	@Override
	public ChangePasswordVO changePassword(ChangePasswordVO vo) {
		
    	Users userInDb = (Users) get(Users.class,vo.getLoginname().toLowerCase());
    	if(userInDb != null){
    		//也可以直接在web处通过token验证旧密码是否正确，提高效率
    		if(!StringUtils.equals(userInDb.getPassword(), EncryptionUtil.encryptPassword(vo.getOriginalPassword()) )){
    			vo.setMsg("旧密码不正确");
    			return vo;
    		}
    		
    		userInDb.setPassword(EncryptionUtil.encryptPassword(vo.getNewPassword()));
    		baseDao.update(userInDb);
    		return vo;
    	}else{
    		vo.setMsg("用户不存在");
    		return vo;
    	}
		
	}

	@Override
	public OnlinePayVO getOnlinePayUrl(UsersVO vo) {
		
		OnlinePayVO onlinePayVO = new OnlinePayVO();
		String token = RandomStringUtils.randomAlphanumeric(60);
		OnlineToken onlineToken = new OnlineToken();
		onlineToken.setLoginname(vo.getLoginname());
		onlineToken.setCreatetime(new Date());
		onlineToken.setToken(token);
		
		Session session = null;
		try {
			session = baseDao.getHibernateTemplate()
					.getSessionFactory().openSession();//.setFlushMode(FlushMode.COMMIT);
			session.setFlushMode(FlushMode.COMMIT);
			session.saveOrUpdate(onlineToken);
			session.flush();
		} catch (Exception e) {
			
		}finally{
			
			if (session != null) {
				session.close();
			}
			
		}
		DetachedCriteria systemConfigDc = DetachedCriteria.forClass(SystemConfig.class);
		systemConfigDc.add(Restrictions.eq("typeNo", "type300"));
		systemConfigDc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> configList = baseDao.findByCriteria(systemConfigDc,0,1);
		if(configList != null && configList.size() > 0 ){
		   SystemConfig domainConfig = (SystemConfig)configList.get(0);
		   onlinePayVO.setDomain(domainConfig.getValue());
		   onlinePayVO.setTempToken(token);
		}
		return onlinePayVO;
	}
	@Override
	public OnlinePayVO getWebDomain() {
		OnlinePayVO onlinePayVO = new OnlinePayVO();
		DetachedCriteria systemConfigDc = DetachedCriteria.forClass(SystemConfig.class);
		systemConfigDc.add(Restrictions.eq("typeNo", "type200"));
		systemConfigDc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> configList = baseDao.findByCriteria(systemConfigDc,0,1);
		if(configList != null && configList.size() > 0 ){
			SystemConfig domainConfig = (SystemConfig)configList.get(0);
			onlinePayVO.setDomain(domainConfig.getValue());
		}
		return onlinePayVO;
	}
	@Override
	public Page getOnlinePayRecords(OnlinePayRecordVO vo) {
		DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		dc = dc.add(Restrictions.eq("type", PayOrderFlagType.SUCESS.getCode()));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		List<Payorder> list = page.getPageContents();
		for (Payorder p : list) {
			p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
		}
		
		return page;
	}
	@Override
	public Page getCashinRecords(OnlinePayRecordVO vo) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode()));
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		List<Proposal> list = page.getPageContents();
		for (Proposal p : list) {
			p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
		}
		
		return page;
	}
	@Override
	public Page getWithdrawRecords(OnlinePayRecordVO vo) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode()));
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		List<Proposal> list = page.getPageContents();
		for (Proposal p : list) {
			p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
		}
		
		return page;
	}
	@Override
	public Page getTransferRecords(OnlinePayRecordVO vo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		dc = dc.add(Restrictions.eq("flag", Constants.FLAG_TRUE));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenationForApp(baseDao.getHibernateTemplate(), dc,vo.getPageIndex(), vo.getPageSize(), o);
		List<Transfer> list = page.getPageContents();
		for (Transfer t : list) {
			t.setTempCreateTime(DateUtil.getDateFormat(t.getCreatetime()));
		}
		
		return page;
	}
	@Override
	public Page getConcessionRecords(OnlinePayRecordVO vo) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		dc = dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		dc = dc.add(Restrictions.in("type", new Object[] { 
				ProposalType.BIRTHDAY.getCode(), 
				ProposalType.LEVELPRIZE.getCode(), ProposalType.BANKTRANSFERCONS.getCode(), 
				ProposalType.CONCESSIONS.getCode(), ProposalType.XIMA.getCode(), 
				ProposalType.OFFER.getCode(), ProposalType.PRIZE.getCode(), 
				ProposalType.PROFIT.getCode(), ProposalType.WEEKSENT.getCode(),
				ProposalType.SELFHELP_APP_PREFERENTIAL.getCode(),ProposalType.ACTIVEAWARD.getCode(),
				 ProposalType.SIGNDEPOSIT420.getCode()}));
		Order o = Order.desc("createTime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc,vo.getPageIndex(), vo.getPageSize(), o);
		List<Proposal> list = page.getPageContents();
		for (Proposal p : list) {
			p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
		}
		
		return page;
	}
	@Override
	public Page getCouponRecords(OnlinePayRecordVO vo) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		dc = dc.add(Restrictions.in("type", new Object[]{531,532,533,534,535,536,537,571,572,573,574,581,582,583,584,590,591,592,593,594,595,701,702,703,704,705,706,707,708,709,710,711,712,401,402,403,404,405,406,407,408,409,410,411,412,419,730,731,732,733,734,735,598,599}));
		dc = dc.add(Restrictions.eq("flag", 2));
		Order o = Order.desc("executeTime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		List<Proposal> list = page.getPageContents();
		for(Proposal t:list){
			t.setTempCreateTime(DateUtil.getDateFormat(t.getExecuteTime()));
		}
		return page;
	}
	@Override
	public Page getXimaRecords(OnlinePayRecordVO vo) throws Exception {
		Page page = new Page();
		
		Date startTime = DateUtil.getDateByDay("2008-01-01");
		Date endTime = DateUtil.getDateByDay("2020-01-01");
		
		AutoXima AutoXima = gameinfoService.getTotalCount(vo.getLoginName(), startTime, endTime);
		// 取得总行数
		int totalRowsno = AutoXima.getTotalCount();
		if (totalRowsno<=0) {
			page.setTotalPages(0);
			page.setTotalRecords(0);
			return page;
		}else{
			if(totalRowsno%vo.getPageSize()==0){
				page.setTotalPages(totalRowsno/vo.getPageSize());
			}else{
				page.setTotalPages(totalRowsno/vo.getPageSize()+1);
			}
		}
		page.setTotalRecords(totalRowsno);
		
		page.setStatics1(AutoXima.getTotalValidAmount());
		page.setStatics2(AutoXima.getTotalXimaAmount());
		List<AutoXima> list = gameinfoService.searchXimaDetail(vo.getLoginName(), startTime, endTime, vo.getPageIndex().intValue(), vo.getPageSize().intValue());
		
		List<AutoXimaVO> listBack = new ArrayList<AutoXimaVO>();
		
		if(list != null && list.size() > 0){
			for(AutoXima autoXima : list){
				AutoXimaVO autoXimaVO = new AutoXimaVO();
				autoXimaVO.setEndTime(autoXima.getEndTime());
				autoXimaVO.setMessage(autoXima.getMessage());
				autoXimaVO.setPno(autoXima.getPno());
				autoXimaVO.setRate(autoXima.getRate());
				autoXimaVO.setStartTime(autoXima.getStartTime());
				autoXimaVO.setStatisticsTimeRange(autoXima.getStatisticsTimeRange());
				autoXimaVO.setValidAmount(autoXima.getValidAmount());
				autoXimaVO.setXimaAmount(autoXima.getXimaAmount());
				autoXimaVO.setXimaStatus(autoXima.getXimaStatus());
				autoXimaVO.setXimaType(autoXima.getXimaType());
				listBack.add(autoXimaVO);
			}
		}
		
		page.setPageContents(listBack);
		
		return page;
	}
	@Override
	public Page getDepositOrderRecords(OnlinePayRecordVO vo){

		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		dc = dc.add(Restrictions.eq("loginname", vo.getLoginName()));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.pageIndex, vo.getPageSize(), o);
		List<DepositOrder> list = page.getPageContents();
		for (DepositOrder p : list) {
			p.setTempCreateTime(DateUtil.getDateFormat(p.getCreatetime()));
		}
		
		return page;
		
	}
	@Override
	public Page getPointRecords(OnlinePayRecordVO vo){
		
		DetachedCriteria dc = DetachedCriteria.forClass(DetailPoint.class);
		dc.add(Restrictions.eq("username", vo.getLoginName()));
		dc.add(Restrictions.in("type",new String[]{"0","1"}));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		List<DetailPoint> list = page.getPageContents();
		for (DetailPoint p : list) {
			p.setCreateday(DateUtil.getDateFormat(p.getCreatetime()));
			if(p.getType().equals("0")){
				p.setType("积分收入");
			}else{
				p.setType("积分支出");
			}
		}
		return page;
		
	}
	@Override
	public Page getFriendRecords(OnlinePayRecordVO vo){
		//成功玩家
		if("0".equals(vo.getFriendtype())){
			DetachedCriteria dc = DetachedCriteria.forClass(Friendintroduce.class);
			dc.add(Restrictions.eq("toplineuser", vo.getLoginName()));
			Order o = Order.desc("createtime");
			Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
			List<Friendintroduce> list = page.getPageContents();
			for (Friendintroduce p : list) {
				if(!StringUtil.isEmpty(p.getDownlineuser())){
					p.setDownlineuser(p.getDownlineuser().substring(0,3)+"****");
				}
			}
			return page;
		}else{
			
		/*<select name="friendtype" id="friendtype" style="width:150px" onchange="queryfriendRecord(this.value);">
	    <option value="0">推荐注册成功玩家</option>
	    <option value="1" selected="selected">推荐奖金收入</option>
	    <option value="2">推荐奖金支出</option>
	    </select>*/
			
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonusrecord.class);
		dc.add(Restrictions.eq("toplineuser", vo.getLoginName()));
		dc.add(Restrictions.eq("type", vo.getFriendtype()));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenationForApp(slaveService.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		List<Friendbonusrecord> list = page.getPageContents();
		for (Friendbonusrecord p : list) {
			if(!StringUtil.isEmpty(p.getDownlineuser())){
			p.setDownlineuser(p.getDownlineuser().substring(0,3)+"****");
			}
			if(p.getType().equals("1")){
				p.setType("收入");
			}else{
				p.setDownlineuser(p.getToplineuser().substring(0,3)+"****");
				p.setType("支出");
			}
		}
		return page;
		}
	}

	@Override
	public HotGameLoginVO hotGameLogin(HotGameLoginVO vo) {
		
		UserWebServiceWS userWebServiceWS = (UserWebServiceWS) SpringFactoryHepler.getInstance("userWebServiceWS");
		
		try {
			
			if(StringUtils.isNotEmpty(vo.getLoginName())){
				
				Users userInDb = (Users) get(Users.class,vo.getLoginName());
				
				if(userInDb != null && "AGENT".equals(userInDb.getRole())){
					
					vo.setMsg("代理玩家不可游戏");
					return vo;
				}
			}
			
			if("DT".equals(vo.getPlatCode())){
				
				DtVO dtVO = DtUtil.login(vo.getLoginName(),vo.getPassword());
				String gameUrl = "?slotKey={0}&language={1}&gameCode={2}&isfun=0&type=undefined&closeUrl={3}";
				gameUrl = (dtVO.getGameurl() + MessageFormat.format(gameUrl, new String[]{dtVO.getSlotKey(),"zh_CN",vo.getGameCode(),""}));
				vo.setGameLoginUrl(gameUrl);
				vo.setOpenType("webview");
				return vo;
			}
			if("MG".equals(vo.getPlatCode())){
				
				Const ct = transferService.getConsts("MG");
				if (null == ct || ct.getValue().equals("0")) {
					vo.setMsg("该游戏维护中");
					return vo;
				}
			    //String gameUrl = MGSUtil.gameLogin4H5(vo.getLoginName(), vo.getGameCode(), "","");
			    //String gameUrl = MGSUtil.launchGameUrl(vo.getLoginName(),vo.getPassword(), vo.getGameCode(),"false","");
				String gameUrl = MGSUtil.launchGameUrl(vo.getLoginName(),vo.getPassword(), "","","","","");
				vo.setGameLoginUrl(gameUrl);
				vo.setOpenType("browser");
				return vo;
			}
			if("QT".equals(vo.getPlatCode())){
				
				String mode = "1".equals(vo.getIsfun())? QtUtil.MODE_DEMO : QtUtil.MODE_REAL;
				String clientType = "1".equals(0)? QtUtil.CLIENTTYPE_FLASH : QtUtil.CLIENTTYPE_HTML5;
				String gameUrl = QtUtilForTp.getGameUrl(vo.getGameCode(),"1".equals(vo.getIsfun())?"DEMOPLAY":vo.getLoginName(), mode, clientType, "127.0.0.1");
				vo.setGameLoginUrl(gameUrl);
				vo.setOpenType("webview");
				return vo;
			}
			if("NT".equals(vo.getPlatCode())){
				
//				String gameUrl = "http://load.sdjdlc.com/nt/?game={0}&language=cn&lobbyUrl={1}&key={2}";
				
//				String jsonText = NTUtils.loginNTGame(vo.getLoginName());
				
				String gameUrl = "http://load.sdjdlc.com/disk2/netent/?game={0}&language=cn&lobbyUrl={1}&key={2}";
				
				String jsonText = NTUtils.loginNTGame(vo.getLoginName(), vo.getGameCode());
				
				if (StringUtils.isNotEmpty(jsonText)) {
					JSONObject json = JSONObject.fromObject(jsonText); 
					if (json.getBoolean("result")) {
						String key = json.getString("key");
						gameUrl = MessageFormat.format(gameUrl, new String[]{vo.getGameCode(),"127.0.0.1",key});
					} else {
						String msg = NTErrorCode.compare(json.getString("error"));
						log.error("loginGameNT错误, 错误消息: " + msg);
						vo.setMsg(msg);
						return vo;
					}
				}else{
					vo.setMsg("进入游戏失败");
					return vo;
				}
				vo.setGameLoginUrl(gameUrl);
				vo.setOpenType("webview");
				return vo;
			}
			
			if("PNG".equals(vo.getPlatCode())){
				
				String rootUrl = "https://bsicw.playngonetwork.com";
				String result = userWebServiceWS.getPNGLoadingTicket(vo.getLoginName(), vo.getGameCode());
			    JSONObject resultObj = JSONObject.fromObject(result);
				
				if(!StringUtils.equals(resultObj.getString("code"),"10000")){
					
					vo.setMsg("进入游戏失败");
					return vo;
				}
				Map<String, String> dataMap = (Map<String, String>) resultObj.get("data");
				
				String gameUrl = rootUrl + "/casino/PlayMobile" + "?pid=365" + "&gid=" + vo.getGameCode() + "&lang=" + "zh_CN" + 
						"&practice=0&user=" + dataMap.get("ticket") + "&lobby=";
				
				vo.setGameLoginUrl(gameUrl);
				vo.setOpenType("webview");
				return vo;
			}
			
		} catch (Exception e) {
			log.info("error hotGameLogin, error message:" + e.getMessage());
			vo.setMsg("进入游戏失败，请稍后再试");
			e.printStackTrace();
		}
		return vo;
	}
	
	private boolean gamecheck(String type, String gameid) {
		return slaveService.gamecheck(type, gameid);
	}	
	
	public void update(Users users) {
	
		baseDao.update(users);
	}
	
	
	public AgentSettingVO updateAgentInfo(AgentSettingVO vo){
		
		Users userInDb = (Users) get(Users.class,vo.getLoginName());
		
		if(userInDb == null){
			
			vo.setMessage("账号不存在");
			return vo;
		}
		userInDb.setQq(StringUtils.isEmpty(vo.getQq()) ? "" : vo.getQq());
			
		userInDb.setMicrochannel(StringUtils.isEmpty(vo.getWechat()) ? "" : vo.getWechat());
		
		
		if(StringUtils.isNotEmpty(vo.getAliasName())){
			
			userInDb.setAliasName(vo.getAliasName());
			
		}
		
		if(StringUtils.isEmpty(userInDb.getAccountName()) && StringUtils.isNotEmpty(vo.getAccountName()) && !vo.getAccountName().contains("*")){
			userInDb.setAccountName(vo.getAccountName());
		}
			
		if(StringUtils.isNotEmpty(vo.getBirthday())){
			userInDb.setBirthday(DateUtil.getDateByDay((vo.getBirthday())));
		}
		if(StringUtils.isNotEmpty(vo.getEmail())){
			try {
				userInDb.setEmail( AESUtil.aesEncrypt(vo.getEmail(), AESUtil.KEY));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		baseDao.update(userInDb);
		return vo;
	}
	
	public String getPostCode(String sid){
		
		if(sid.length() < 10 ){
			return sid;
		}
		
		StringBuffer sb = new StringBuffer(sid);  
		sb = sb.reverse();
		
		return sb.substring(0,8);
		
	}	

	   /**
  * 获取App代理定制版数据
  * @param hostAddress
  * @return
  */
	public AppCustomVersionVO[] getAppCustomVersionData(String hostAddress) {
		List<AppCustomVersionVO> result = new ArrayList<AppCustomVersionVO>();
		AgentAddress agentAddress = (AgentAddress) baseDao.get(AgentAddress.class, hostAddress);
		if (agentAddress != null) {
			DetachedCriteria versionDc = DetachedCriteria.forClass(AppPackageVersion.class);
			versionDc.add(Restrictions.eq("status", 1));
			versionDc.addOrder(Order.desc("createTime"));
			List<AppPackageVersion> versionList = baseDao.findByCriteria(versionDc);
			List<Integer> versionIdList = new ArrayList<Integer>();
			if (versionList != null && !versionList.isEmpty()) {
				List<AppPackageVersion> versionIosList = new ArrayList<AppPackageVersion>();
				List<AppPackageVersion> versionAndroidList = new ArrayList<AppPackageVersion>();
				for (AppPackageVersion version : versionList) {
					if (version.getPlat().equalsIgnoreCase("ios")) {
						versionIosList.add(version);
					} else if (version.getPlat().equalsIgnoreCase("android")) {
						versionAndroidList.add(version);
					}
				}
				if (!versionIosList.isEmpty()) {
					AppPackageVersion version = versionIosList.get(0);
					versionIdList.add(version.getId());
					if (versionIosList.size() > 1) { // 上一个版本，防止当前版本缺少代理包
						AppPackageVersion preVersion = versionIosList.get(1);
						versionIdList.add(preVersion.getId());
					}
					if (!versionIdList.isEmpty()) {
						DetachedCriteria versionCustomIosDc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
						versionCustomIosDc.add(Restrictions.eq("agentAccount", agentAddress.getLoginname()));
						versionCustomIosDc.add(Restrictions.eq("status", 1));
						versionCustomIosDc.add(Restrictions.eq("pakStatus", 2));
						versionCustomIosDc.add(Restrictions.eq("plat", "iOS"));
						versionCustomIosDc.add(Restrictions.in("versionId", versionIdList));
						versionCustomIosDc.addOrder(Order.desc("createTime"));
						List<AppPackageVersionCustom> versionCustomIosList = baseDao.findByCriteria(versionCustomIosDc);
						if (versionCustomIosList != null && !versionCustomIosList.isEmpty()) {
							AppPackageVersionCustom versionCustom = versionCustomIosList.get(0);
							AppCustomVersionVO vo = new AppCustomVersionVO();
							vo.setPlat(versionCustom.getPlat());
							vo.setVersionCode(versionCustom.getVersionCode());
							vo.setPackageUrl(String.format("%s%s", "itms-services:///?action=download-manifest&url=",versionCustom.getPackageUrl()));
							vo.setQrCodeUrl(versionCustom.getQrCodeUrl());
							vo.setModifyTime(versionCustom.getModifyTime());

							result.add(vo);
						}
					}
				}
				if (!versionAndroidList.isEmpty()) {
					versionIdList.clear();
					AppPackageVersion version = versionAndroidList.get(0);
					versionIdList.add(version.getId());
					if (versionAndroidList.size() > 1) { // 上一个版本，防止当前版本缺少代理包
						AppPackageVersion preVersion = versionAndroidList.get(1);
						versionIdList.add(preVersion.getId());
					}
					if (!versionIdList.isEmpty()) {
						DetachedCriteria versionCustomAndroidDc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
						versionCustomAndroidDc.add(Restrictions.eq("agentAccount", agentAddress.getLoginname()));
						versionCustomAndroidDc.add(Restrictions.eq("status", 1));
						versionCustomAndroidDc.add(Restrictions.eq("pakStatus", 2));
						versionCustomAndroidDc.add(Restrictions.eq("plat", "android"));
						versionCustomAndroidDc.add(Restrictions.in("versionId", versionIdList));
						versionCustomAndroidDc.addOrder(Order.desc("createTime"));
						List<AppPackageVersionCustom> versionCustomAndroidList = baseDao.findByCriteria(versionCustomAndroidDc);
						if (versionCustomAndroidList != null && !versionCustomAndroidList.isEmpty()) {
							AppPackageVersionCustom versionCustom = versionCustomAndroidList.get(0);
							AppCustomVersionVO vo = new AppCustomVersionVO();
							vo.setPlat(versionCustom.getPlat());
							vo.setVersionCode(versionCustom.getVersionCode());
							vo.setPackageUrl(versionCustom.getPackageUrl());
							vo.setQrCodeUrl(versionCustom.getQrCodeUrl());
							vo.setModifyTime(versionCustom.getModifyTime());

							result.add(vo);
						}
					}

				}
			}

		}
		
		return result.toArray(new AppCustomVersionVO[result.size()]);
	}
	
	
}