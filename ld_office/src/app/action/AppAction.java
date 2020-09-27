package app.action;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nnti.office.model.auth.Operator;
import dfh.utils.*;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.config.FTPProperties;
import app.enums.CacheEnums;
import app.enums.LatestPreferentialTypeEnums;
import app.model.po.AppPackageVersion;
import app.model.po.AppPackageVersionCustom;
import app.model.po.ConstraintAddressConfig;
import app.model.po.LatestPreferential;
import app.model.po.LatestWinInfo;
import app.model.po.PreferentialComment;
import app.model.po.PreferentialConfig;
import app.model.po.SpecialTopic;
import app.model.po.UserSidName;
import app.model.vo.AppCustomVersionVO;
import app.model.vo.IndexLatestWinInfoVO;
import app.model.vo.IndexSpecialTopicVO;
import app.model.vo.LatestPreferentialVO;
import app.model.vo.StyleVO;
import app.util.AESUtil;
import app.util.DateUtil;
import app.util.FTPClientUtilX;
import app.util.PreferentialClientUtil;
import app.util.RedisUtil;
import app.util.TopicClientUtil;
import dfh.action.SubActionSupport;
import dfh.model.AgentAddress;
import dfh.model.Userstatus;
import dfh.service.interfaces.ProposalService;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppAction extends SubActionSupport {
	
private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(AppAction.class);
	
	private PreferentialConfig preferentialConfig;
	private ConstraintAddressConfig constraintAddressConfig;
	
	private ProposalService proposalService;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Integer size;
	private Integer pageIndex;
	private String order;
	private String by;
	private String cacheMethodName;
	private File activityImage;
	private String activityImageFileName;
	private File newImage;
	private String newImageFileName;
	private File topicImage;
	private String topicImageFileName;
	private Date startTime;
	private String errormsg;
	private String myFileFileName;
	private String excelFileName;
	private File myFile;
	public String getStartTimestr() {
		return startTimestr;
	}

	public void setStartTimestr(String startTimestr) {
		this.startTimestr = startTimestr;
	}

	public String getEndTimestr() {
		return endTimestr;
	}

	public void setEndTimestr(String endTimestr) {
		this.endTimestr = endTimestr;
	}

	private Date endTime;
	private LatestPreferential latestPreferential;
	private PreferentialComment preferentialComment;
	private UserSidName userSidName;
	private SpecialTopic specialTopic;
	private Userstatus userStatus;
	private LatestWinInfo latestWinInfo;
	private File gameIcon;
	private String gameIconFileName;
	private String startTimestr;
	private  String endTimestr;
	
	// 查询全部自助优惠配置记录信息

	public String queryPreferentialConfigList() {
		
		DetachedCriteria dc = DetachedCriteria.forClass(PreferentialConfig.class);
			
		if (null != preferentialConfig) {
			
			if (StringUtils.isNotBlank(preferentialConfig.getPlatformId())) {
				
				dc.add(Restrictions.eq("platformId", preferentialConfig.getPlatformId()));
			}
			
			if (StringUtils.isNotBlank(preferentialConfig.getTitleId())) {
				
				dc.add(Restrictions.eq("titleId", preferentialConfig.getTitleId()));
			}
			
			if (StringUtils.isNotBlank(preferentialConfig.getAliasTitle())) {
				
				dc.add(Restrictions.ilike("aliasTitle", preferentialConfig.getAliasTitle(), MatchMode.ANYWHERE));
			}
			
			if (null != preferentialConfig.getIsUsed()) {
				
				dc.add(Restrictions.eq("isUsed", preferentialConfig.getIsUsed()));
			}
			
			if (null != preferentialConfig.getMachineCodeEnabled()) {
				
				dc.add(Restrictions.eq("machineCodeEnabled", preferentialConfig.getMachineCodeEnabled()));
			}
			
			if (null != preferentialConfig.getIsPhone()) {
				
				dc.add(Restrictions.ilike("isPhone", preferentialConfig.getIsPhone(), MatchMode.ANYWHERE));
			}
		}
			
		dc.add(Restrictions.eq("deleteFlag", 1));
			
		Order o = null;
			
		if (StringUtils.isNotEmpty(by)) {
			
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			
			o = Order.desc("createTime");
		}
			
		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	// 开启/禁用单个自助优惠配置记录信息
	public void activePreferentialConfig() {
		
		if (null == preferentialConfig || StringUtils.isBlank(preferentialConfig.getIds())) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		
		String sql = "UPDATE preferential_config set is_used = :isUsed, update_time= :updateTime WHERE id in (:ids)";
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("isUsed", preferentialConfig.getIsUsed());
		paramsMap.put("updateTime", new Date());
		paramsMap.put("ids", Arrays.asList(preferentialConfig.getIds().split(",")));
		
		Session session = null;
		
		try {
			
			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();
			
			Query query = session.createSQLQuery(sql);
			query.setProperties(paramsMap);
			
			query.executeUpdate();
			
			GsonUtil.GsonObject("[提示]数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据更新发生异常，请稍后重试！");
		} finally {
			
			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}
	public String importConstraintAddressConfig(){
		log.info("开始导入约束地址配置excel");
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			setErrormsg("请先登录，在操作");
			return INPUT;
		}
		String operator = op.getUsername();
		if (null == myFileFileName || myFileFileName.equals("")) {
			setErrormsg("请先提交文件");
			return INPUT;
		}

		String filehouzhui = getExtention(myFileFileName);

		if (!filehouzhui.equals(".xls")) {

			setErrormsg("文件格式必须是.xls结尾的excel");
			return INPUT;
		}

		try {
			String errormsg = proposalService.importData(myFile,operator);
			setErrormsg(errormsg);
		} catch (Exception e) {
			setErrormsg(e.getMessage());
			e.printStackTrace();
		}
		return INPUT;
	}



	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
	// 删除单个自助优惠配置记录信息
	public void deletePreferentialConfig() {
		
		if (null == preferentialConfig || StringUtils.isBlank(preferentialConfig.getIds())) {
			
			GsonUtil.GsonObject("[提示]数据删除失败，失败原因：数据传输错误，请联系管理员！");
			return;
		}
		
		String sql = "UPDATE preferential_config SET delete_flag = :deleteFlag, update_time= :updateTime WHERE id in (:ids)";
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("deleteFlag", 0);
		paramsMap.put("updateTime", new Date());
		paramsMap.put("ids", Arrays.asList(preferentialConfig.getIds().split(",")));
		
		Session session = null;
		
		try {
			
			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();
			
			Query query = session.createSQLQuery(sql);
			query.setProperties(paramsMap);
			
			query.executeUpdate();
			
			GsonUtil.GsonObject("[提示]数据删除成功！");
		} catch(Exception e) {
			
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据删除发生异常，请稍后重试！");
		} finally {
			
			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}
	
	// 查询单个自助优惠配置记录信息
	public String queryPreferentialConfig() {
		
		if (null == preferentialConfig || null == preferentialConfig.getId() || 0 == preferentialConfig.getId()) {
			
			String result = "{ \"status\": \"404\", \"message\": \"数据查询失败，失败原因：参数值错误！\"}";
			GsonUtil.GsonObject(result);
			return INPUT;
		}
		
		PreferentialConfig obj = (PreferentialConfig) proposalService.get(PreferentialConfig.class, preferentialConfig.getId());
		
		if (null == obj) {
			
			String result = "{ \"status\": \"404\", \"message\": \"数据查询失败，失败原因：查询的记录不存在！\"}";
			GsonUtil.GsonObject(result);
			return INPUT;
		}
		
		String data = JSONObject.fromObject(obj).toString();
		GsonUtil.GsonObject(data);
		return SUCCESS;
	}
	
	// 修改单个自助优惠配置记录信息
	public void updatePreferentialConfig() {
		
		if (null == preferentialConfig) {
			
			GsonUtil.GsonObject("[提示]数据更新失败，失败原因：数据传输错误，请联系管理员！");
			return;
		}
		
		PreferentialConfig obj = (PreferentialConfig) proposalService.get(PreferentialConfig.class, preferentialConfig.getId());
		
		if (null == obj) {
		
			GsonUtil.GsonObject("[提示]数据更新失败，失败原因：修改的记录不存在！");
			return;
		}
		
		try {
			
			preferentialConfig.setIsUsed(obj.getIsUsed());
			preferentialConfig.setDeleteFlag(obj.getDeleteFlag());
			preferentialConfig.setCreateTime(obj.getCreateTime());
			preferentialConfig.setUpdateTime(DateUtil.getCurrentDate());
			
			proposalService.update(preferentialConfig);
			
			GsonUtil.GsonObject("[提示]数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据更新发生异常，请稍后重试！");
		}
	}
	
	// 新增单个自助优惠配置记录信息
	public void addPreferentialConfig() {
	
		if (null == preferentialConfig) {
			
			GsonUtil.GsonObject("[提示]数据新增失败，失败原因：数据传输错误，请联系管理员！");
			return;
		}
		
		try {
			preferentialConfig.setStartTime(DateUtil.getStringFormat(startTimestr));
			preferentialConfig.setEndTime(DateUtil.getStringFormat(endTimestr));
			preferentialConfig.setIsUsed(1);
			preferentialConfig.setDeleteFlag(1);
			preferentialConfig.setCreateTime(DateUtil.getCurrentDate());
			preferentialConfig.setUpdateTime(DateUtil.getCurrentDate());
			
			proposalService.save(preferentialConfig);
			
			GsonUtil.GsonObject("[提示]数据新增成功！");
		} catch(Exception e) {
			
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据新增发生异常，请稍后重试！");
		}
	}
	
	// 查询约束地址配置记录信息
	public String queryConstraintAddressConfigList() {
	
		DetachedCriteria dc = DetachedCriteria.forClass(ConstraintAddressConfig.class);
		
		if (null != constraintAddressConfig) {
			
			if (StringUtils.isNotBlank(constraintAddressConfig.getTypeId())) {
				
				dc.add(Restrictions.eq("typeId", constraintAddressConfig.getTypeId()));
			}
			
			if (StringUtils.isNotBlank(constraintAddressConfig.getValue())) {
				
				dc.add(Restrictions.like("value", constraintAddressConfig.getValue(), MatchMode.ANYWHERE));
			}
		}
		
		dc.add(Restrictions.eq("deleteFlag", "1"));
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			
			o = Order.desc("id");
		}
		
		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	// 新增约束地址配置记录信息
	@SuppressWarnings("rawtypes")
	public void addConstraintAddressConfig() {
		
		String loginName = getOperatorLoginname();
		Date currentDate = new Date();
		
		if (StringUtils.isBlank(loginName)) {
		
			GsonUtil.GsonObject("[提示]请登录后再进行操作！");
			return;
		}
		
		if (null == constraintAddressConfig) {
		
			GsonUtil.GsonObject("[提示]数据传输错误，请联系管理员！");
			return;
		}
		
		if (StringUtils.isBlank(constraintAddressConfig.getValue())) {
		
			GsonUtil.GsonObject("[提示]配置的约束项值不允许为空！");
			return;
		}
		
		if (StringUtils.isNotBlank(constraintAddressConfig.getTypeId())) {
		
			List<String> list = Arrays.asList(new String[] { "1", "2" });
			
			if (list.contains(constraintAddressConfig.getTypeId())) {
			
				if (!isIP(constraintAddressConfig.getValue())) {
					
					GsonUtil.GsonObject("[提示]配置的约束项值不是一个有效的IP地址！");
					return;
				}
			}
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(ConstraintAddressConfig.class);
		
		dc.add(Restrictions.eq("typeId", constraintAddressConfig.getTypeId()));
		dc.add(Restrictions.eq("value", constraintAddressConfig.getValue()));
		dc.add(Restrictions.eq("deleteFlag", "1"));
		
		List list = proposalService.findByCriteria(dc);
		
		if (null != list && !list.isEmpty()) {
		
			GsonUtil.GsonObject("[提示]已存在该配置，请勿重复配置！");
			return;
		}
		
		try {
			
			constraintAddressConfig.setDeleteFlag("1");
			constraintAddressConfig.setCreatedUser(loginName);
			constraintAddressConfig.setCreateTime(currentDate);
			constraintAddressConfig.setUpdatedUser(loginName);
			constraintAddressConfig.setUpdateTime(currentDate);
			constraintAddressConfig.setRemark(constraintAddressConfig.getRemark());

			proposalService.save(constraintAddressConfig);
			
			GsonUtil.GsonObject("[提示]数据新增成功！");
		} catch(Exception e) {
			
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据新增发生异常，请稍后重试！");
		}
	}
	
	// 删除约束地址配置记录信息
	public void deleteConstraintAddressConfig() {
	
		String loginName = getOperatorLoginname();
		
		if (StringUtils.isBlank(loginName)) {
		
			GsonUtil.GsonObject("请登录后再进行操作！");
			return;
		}
		
		if (null == constraintAddressConfig || null == constraintAddressConfig.getId()) {
			
			GsonUtil.GsonObject("[提示]数据查询失败，失败原因：数据传输错误，请联系管理员！");
			return;
		}
		
		ConstraintAddressConfig obj = (ConstraintAddressConfig) proposalService.get(ConstraintAddressConfig.class, constraintAddressConfig.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("[提示]数据删除失败，失败原因：删除的记录不存在！");
			return;
		}
		
		try {
			
			obj.setDeleteFlag("0");
			obj.setUpdatedUser(loginName);
			obj.setUpdateTime(new Date());
			
			proposalService.update(obj);
			
			GsonUtil.GsonObject("[提示]数据删除成功！");
		} catch(Exception e) {
			
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据新增发生异常，请稍后重试！");
		}
	}
	
	// 查询单个约束地址配置记录信息
	public String queryConstraintAddressConfig() {
	
		HttpServletRequest request = getRequest();
		
		if (null == constraintAddressConfig || null == constraintAddressConfig.getId()  || 0 == constraintAddressConfig.getId()) {
			
			request.setAttribute("tipMessage", "[提示]数据查询失败，失败原因：数据传输错误，请联系管理员！");
			return INPUT;
		}
		
		ConstraintAddressConfig obj = (ConstraintAddressConfig) proposalService.get(ConstraintAddressConfig.class, constraintAddressConfig.getId());
		
		if (null == obj) {
			
			request.setAttribute("tipMessage", "[提示]数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		
		request.setAttribute("constraintAddressConfig", obj);
		
		return SUCCESS;
	}
	
	// 修改约束地址配置记录信息
	@SuppressWarnings("rawtypes")
	public void updateConstraintAddressConfig() {
	
		String loginName = getOperatorLoginname();
		
		if (StringUtils.isBlank(loginName)) {
		
			GsonUtil.GsonObject("[提示]请登录后再进行操作！");
			return;
		}
		
		if (null == constraintAddressConfig || null == constraintAddressConfig.getId()) {
			
			GsonUtil.GsonObject("[提示]数据查询失败，失败原因：数据传输错误，请联系管理员！");
			return;
		}
		
		if (StringUtils.isBlank(constraintAddressConfig.getValue())) {
			
			GsonUtil.GsonObject("[提示]配置的项值不允许为空！");
			return;
		}
		
		if (StringUtils.isNotBlank(constraintAddressConfig.getTypeId())) {
			
			List<String> list = Arrays.asList(new String[] { "1", "2" });
			
			if (list.contains(constraintAddressConfig.getTypeId())) {
			
				if (!isIP(constraintAddressConfig.getValue())) {
					
					GsonUtil.GsonObject("[提示]配置的项值不是一个有效的IP地址！");
					return;
				}
			}
		}
		
		ConstraintAddressConfig obj = (ConstraintAddressConfig) proposalService.get(ConstraintAddressConfig.class, constraintAddressConfig.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("[提示]数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		
		if (!StringUtils.equals(constraintAddressConfig.getValue(), obj.getValue())) {
			
			DetachedCriteria dc = DetachedCriteria.forClass(ConstraintAddressConfig.class);
			
			dc.add(Restrictions.eq("typeId", constraintAddressConfig.getTypeId()));
			dc.add(Restrictions.eq("value", constraintAddressConfig.getValue()));
			dc.add(Restrictions.eq("deleteFlag", "1"));
			
			List list = proposalService.findByCriteria(dc);
			
			if (null != list && !list.isEmpty()) {
			
				GsonUtil.GsonObject("[提示]已存在该配置，请勿重复配置！");
				return;
			}
		}
		
		try {
			
			obj.setValue(constraintAddressConfig.getValue());
			obj.setUpdatedUser(loginName);
			obj.setUpdateTime(new Date());
			
			proposalService.update(obj);
			
			GsonUtil.GsonObject("[提示]数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据修改失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("[提示]数据新增发生异常，请稍后重试！");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reloadCache() {
	
		try {

			Class cls = Class.forName("app.action.AppAction");
			Method method = cls.getDeclaredMethod(cacheMethodName, new Class[] {});
			Object obj = method.invoke(cls.newInstance(), new Object[] {});
			GsonUtil.GsonObject(obj);
		} catch (Exception e) {

			log.error("reloadCache方法执行反射发生异常，异常信息：" + e.getMessage());
			GsonUtil.GsonObject("执行反射发生异常，请联系管理员！");
		}
	}
	
	@SuppressWarnings("rawtypes")
//	public String reloadConstraintAddressCache() {
//		
//		String message = "缓存数据更新成功！";
//		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
//		
//		try {
//			
//			// 清除缓存数据
//			RedisUtil.delete(CacheEnums.CACHEENUMS_IPLIMIT01.getCode());
//			RedisUtil.delete(CacheEnums.CACHEENUMS_IPLIMIT02.getCode());
//			RedisUtil.delete(CacheEnums.CACHEENUMS_IPLIMIT03.getCode());
//		} catch (Exception e) {
//
//			log.error("删除缓存数据发生异常，异常信息：" + e.getMessage());
//		}
//		
//		log.info("==================连接缓存服务器成功==================");
//		
//		String sql = "select type_id,type_name,value from constraint_address_config where delete_flag = :deleteFlag";
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("deleteFlag", "1");
//		
//		List list = null;
//		
//		try {
//			
//			ProposalService proposalService = (ProposalService) SpringFactoryHepler.getInstance("proposalService");
//			
//			list = proposalService.getListBySql(sql, params);
//		} catch (Exception e) {
//			
//			log.error("reloadConstraintAddressCache方法调用proposalService.getListBySql方法发生异常，异常信息：" + e.getMessage());
//		}
//		
//		try {
//			
//			if (null != list && !list.isEmpty()) {
//				
//				for (int i = 0, len = list.size(); i < len; i++) {
//					
//					Object[] obj = (Object[]) list.get(i);
//			
//					String id = String.valueOf(obj[0]);
//					String value = String.valueOf(obj[2]);
//					
//					ArrayList<String> valueList = map.get(id);
//					
//					if (valueList == null) {
//						
//						valueList = new ArrayList<String>();
//					}
//					
//					valueList.add(value);
//					
//					map.put(id, valueList);
//				}
//				
//				Iterator<Entry<String, ArrayList<String>>> it = map.entrySet().iterator();
//				
//				while (it.hasNext()) {
//					
//					Entry<String, ArrayList<String>> entry = it.next();
//					String key = entry.getKey();
//					ArrayList<String> valueList = entry.getValue();
//					String cacheKey = "";
//					String name = "";
//					
//					if ("1".equals(key)) {
//						
//						cacheKey = CacheEnums.CACHEENUMS_IPLIMIT01.getCode();
//						name = "通过ＩＰ";
//					} else if ("2".equals(key)) {
//						
//						cacheKey = CacheEnums.CACHEENUMS_IPLIMIT02.getCode();
//						name = "拒绝ＩＰ";
//					} else if ("3".equals(key)) {
//						
//						cacheKey = CacheEnums.CACHEENUMS_IPLIMIT03.getCode();
//						name = "拒绝地区";
//					}
//					
//					String value = "{ \"id\": \"" + key + "\", \"name\": \"" + name + "\", \"value\": \"" + dfh.utils.StringUtil.listToString(valueList) + "\" }";
//					
//					RedisUtil.set(cacheKey, value);
//				}
//			}
//			
//			log.info("==================更新缓存服务器成功==================");
//			log.info("==================" + CacheEnums.CACHEENUMS_IPLIMIT01.getCode() + "==================" + RedisUtil.get(CacheEnums.CACHEENUMS_IPLIMIT01.getCode()));
//			log.info("==================" + CacheEnums.CACHEENUMS_IPLIMIT02.getCode() + "==================" + RedisUtil.get(CacheEnums.CACHEENUMS_IPLIMIT02.getCode()));
//			log.info("==================" + CacheEnums.CACHEENUMS_IPLIMIT03.getCode() + "==================" + RedisUtil.get(CacheEnums.CACHEENUMS_IPLIMIT03.getCode()));
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			log.error("reloadConstraintAddressCache方法调用CacheUtil.setCache方法发生异常，异常信息：" + e.getMessage());
//		}
//		
//		return message;
//	}
	
	public boolean isIP(String ip) {
		
		if (ip.length() < 7 || ip.length() > 15 || StringUtils.isBlank(ip)) {
			
			return false;
		}
		
		String reg = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				   + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		
        Pattern pattern = Pattern.compile(reg);  
        Matcher matcher = pattern.matcher(ip);
        
        return matcher.matches(); 
	}
	
	// 查询全部最新优惠记录信息
	@SuppressWarnings("unchecked")
	public String queryLatestPreferentialList() {
		
		DetachedCriteria dc = DetachedCriteria.forClass(LatestPreferential.class);
		
		if (StringUtils.isNotBlank(latestPreferential.getType()) && !"000".equals(latestPreferential.getType())) {
			
			dc.add(Restrictions.eq("type", latestPreferential.getType()));
		}
		
		if (StringUtils.isNotEmpty(latestPreferential.getActivityTitle())) {
			
			dc.add(Restrictions.like("activityTitle", latestPreferential.getActivityTitle(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(latestPreferential.getIsNew())) {
			
			dc.add(Restrictions.eq("isNew", latestPreferential.getIsNew()));
		}
		
		if (StringUtils.isNotEmpty(latestPreferential.getIsActive())) {
			
			dc.add(Restrictions.eq("isActive", latestPreferential.getIsActive()));
		}
		
		if (StringUtils.isNotEmpty(latestPreferential.getIsPhone())) {
			
			dc.add(Restrictions.eq("isPhone", latestPreferential.getIsPhone()));
		}
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			
			o = Order.desc("createTime");
		}

		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		if(page != null && page.getPageContents() != null && !page.getPageContents().isEmpty()){
			
			for(LatestPreferential obj : (List<LatestPreferential>)page.getPageContents()){
				
				obj.setTypeName(LatestPreferentialTypeEnums.getValueByCode(obj.getType()));
			}
			
		}
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	// 新增单个最新优惠记录信息
	public void addLatestPreferential() {
	
		if (null != activityImage) {
			
			try {
				
				String activityImageUrl = PreferentialClientUtil.uploadFile(activityImageFileName, activityImage);
				
				latestPreferential.setActivityImageUrl(activityImageUrl);
			} catch (Exception e) {
				
				log.error("活动图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("活动图片上传失败，请稍后再试！");
				return;
			}
		}
		
		if (null != newImage) {
			
			try {
				
				String newImageUrl = PreferentialClientUtil.uploadFile(newImageFileName, newImage);
				
				latestPreferential.setNewImageUrl(newImageUrl);
			} catch (Exception e) {
				
				log.error("最新优惠图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("最新优惠图片上传失败，请稍后再试！");
				return;
			}
		}
		
		latestPreferential.setCreateTime(new Date());
		latestPreferential.setCreatedUser(getOperatorLoginname());
		latestPreferential.setUpdateTime(new Date());
		latestPreferential.setUpdatedUser(getOperatorLoginname());
		
		try {
			
			proposalService.save(latestPreferential);
			
			GsonUtil.GsonObject("数据新增成功！");
		} catch(Exception e) {
			
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
		}
	}
	
	// 开启/禁用单个优惠记录信息
	public void activeLatestPreferential() {
	
		if (null == latestPreferential.getId() || 0 == latestPreferential.getId()) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		
		LatestPreferential obj = (LatestPreferential) proposalService.get(LatestPreferential.class, latestPreferential.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		
		try {
			
			obj.setIsActive(latestPreferential.getIsActive());
			obj.setUpdateTime(new Date());
			obj.setUpdatedUser(getOperatorLoginname());
			proposalService.update(obj);
			
			GsonUtil.GsonObject("数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	// 删除单个优惠记录信息
	public void deleteLatestPreferential() {
	
		if (null == latestPreferential.getId() || 0 == latestPreferential.getId()) {
			
			GsonUtil.GsonObject("数据删除失败，失败原因：参数值错误！");
			return;
		}
		
		LatestPreferential obj = (LatestPreferential) proposalService.get(LatestPreferential.class, latestPreferential.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("数据删除失败，失败原因：删除的记录不存在！");
			return;
		}
		
		try {
			
			proposalService.delete(LatestPreferential.class, latestPreferential.getId());
			
			GsonUtil.GsonObject("数据删除成功！");
		} catch(Exception e) {
			
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		}
	}
	
	// 查询单个最新优惠记录信息
	public String singleLatestPreferential() {
	
		HttpServletRequest request = getRequest();
		
		if (null == latestPreferential.getId() || 0 == latestPreferential.getId()) {
			
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		
		LatestPreferential obj = (LatestPreferential) proposalService.get(LatestPreferential.class, latestPreferential.getId());
		
		if (null == obj) {
			
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		
		if (StringUtils.isNotEmpty(obj.getActivityImageUrl())) {
		
			String imageName = obj.getActivityImageUrl().substring(obj.getActivityImageUrl().lastIndexOf("/") + 1);
			
			obj.setActivityImageUrl(FTPProperties.getInstance().getValue("preferential_image_download_url") + imageName);
		}
		
		if (StringUtils.isNotEmpty(obj.getNewImageUrl())) {
			
			String imageName = obj.getNewImageUrl().substring(obj.getNewImageUrl().lastIndexOf("/") + 1);
			
			obj.setNewImageUrl(FTPProperties.getInstance().getValue("preferential_image_download_url") + imageName);
		}
		
		request.setAttribute("latestPreferential", obj);
		
		return SUCCESS;
	}
	
	// 修改单个最新优惠记录信息
	public void updateLatestPreferential() {
	
		if (null == latestPreferential.getId() || 0 == latestPreferential.getId()) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		
		LatestPreferential obj = (LatestPreferential) proposalService.get(LatestPreferential.class, latestPreferential.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		
		latestPreferential.setActivityImageUrl(obj.getActivityImageUrl());
		latestPreferential.setNewImageUrl(obj.getNewImageUrl());
		
		if (null != activityImage) {
			
			try {
				
				String activityImageUrl = PreferentialClientUtil.uploadFile(activityImageFileName, activityImage);
				
				latestPreferential.setActivityImageUrl(activityImageUrl);
			} catch (Exception e) {
				
				log.error("活动图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("活动图片上传失败，请稍后再试！");
				return;
			}
		}
		
		if (null != newImage) {
			
			try {
				
				String newImageUrl = PreferentialClientUtil.uploadFile(newImageFileName, newImage);
				
				latestPreferential.setNewImageUrl(newImageUrl);
			} catch (Exception e) {
				
				log.error("最新优惠图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("最新优惠图片上传失败，请稍后再试！");
				return;
			}
		}
		
		latestPreferential.setCreateTime(obj.getCreateTime());
		latestPreferential.setCreatedUser(obj.getCreatedUser());
		latestPreferential.setUpdateTime(new Date());
		latestPreferential.setUpdatedUser(getOperatorLoginname());
		
		try {
			
			proposalService.update(latestPreferential);
			
			GsonUtil.GsonObject("数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/** Android:
	 *  1 检测到模拟器上的qemud或qemu_pipe通道 
		2 检测到模拟器上的驱动文件内容
		3 检测到模拟器上特有的几个文件
		4 检测到模拟器上的默认电话号码
		5 检测到设备ID是 "000000000000000"
		6 检测到IMSI是"310260000000000"
		7 检测到模拟器上的一些硬件信息参数
		8 检测到模拟器手机运营商家
		9 检测到模拟器CPU型号
		10 检测到bluestacks模拟器
		11 检测到没有后置摄像头
		12 检测到没有前置摄像头
		
		
		如果 有 9和10 基本上判定就是模拟器
		
		其他：疑似模拟器
		
	    iOS的判断mobileModel，如果是“Simulator”就表示是模拟器
	 * @return
	 */
		public String queryUserDeviceInfoList() {
		
			StringBuilder totalSql = new StringBuilder("select count(1) from user_sid_name where 1 = 1");
			StringBuilder recordSql = new StringBuilder("");
			recordSql.append("select sid, loginname, os, osversion, mobilemodel,emulator from user_sid_name where 1 = 1");
			
			StringBuilder conditionSql = new StringBuilder();
			
			if (StringUtils.isNotEmpty(userSidName.getSid())) {
			
				conditionSql.append(" and sid like '%" + userSidName.getSid() + "%'");
			}
			
			if (StringUtils.isNotEmpty(userSidName.getLoginname())) {
				
				conditionSql.append(" and loginname like '%" + userSidName.getLoginname() + "%'");
			}

			if (StringUtils.isNotEmpty(userSidName.getOs())) {
				
				conditionSql.append(" and os like '%" + userSidName.getOs() + "%'");
			}

			if (StringUtils.isNotEmpty(userSidName.getOsVersion())) {
				
				conditionSql.append(" and osversion like '%" + userSidName.getOsVersion() + "%'");
			}
			
			if (StringUtils.isNotEmpty(userSidName.getMobileModel())) {
				
				conditionSql.append(" and mobilemodel like '%" + userSidName.getMobileModel() + "%'");
			}
			
			totalSql.append(conditionSql);
			
			if (StringUtils.isNotEmpty(by)) {
				
				conditionSql.append(" order by " + by + " " + order);
				
			} else {
				
				conditionSql.append(" order by reply_time desc");
			}
			
			if (size == null || size.intValue() == 0) {
				
				size = Page.PAGE_DEFAULT_SIZE;
			}
			
			if (pageIndex == null) {
				
				pageIndex = Page.PAGE_BEGIN_INDEX;
			}
			
			conditionSql.append(" LIMIT " + ((pageIndex - 1) * size) + ", " + size);
			
			recordSql.append(conditionSql);
			
			log.info("totalSql:" + totalSql.toString());
			log.info("recordSql:" + recordSql.toString());
			
			Page page = proposalService.queryForPagenation(recordSql.toString(), totalSql.toString(), pageIndex, size);
			
			
			String model1 = "模拟器";
			String model2 = "疑似模拟器";
			String model3 = "真机";
			
			
			if(page != null && page.getPageContents() != null  && !page.getPageContents().isEmpty()){
				
				List<Object[]> resultFromDb = page.getPageContents();
				List<Object[]> resultForJsp = new ArrayList<Object[]>(resultFromDb.size());
				
				for(Object[] obj : resultFromDb){
					
					if(StringUtils.equals("ios",obj[2] == null ? "" : obj[2].toString()) 
							&& StringUtils.equalsIgnoreCase("Simulator", obj[4] == null ? "" : obj[4].toString())){
						
						obj = new Object[]{obj[0],obj[1],obj[2],obj[3],obj[4],model1};
						resultForJsp.add(obj);
						continue;
					}
					
					
					if(StringUtils.equals("android",obj[2] == null ? "" : obj[2].toString()) && obj[5] != null && StringUtils.isNotEmpty(obj[5].toString())){
						
						if(obj[5].toString().contains("9") || obj[5].toString().contains("10")){
							
							obj = new Object[]{obj[0],obj[1],obj[2],obj[3],obj[4],model1};
						}else{
							
							obj = new Object[]{obj[0],obj[1],obj[2],obj[3],obj[4],model2};
						}
						resultForJsp.add(obj);
						continue;
						
					}
					
					obj = new Object[]{obj[0],obj[1],obj[2],obj[3],obj[4],model3};
					
					
					resultForJsp.add(obj);
					
				}
				
				page.setPageContents(resultForJsp);
			}
			
			getRequest().setAttribute("page", page);
			
			return INPUT;
		}	
	
	// 查询最新优惠详情评论记录信息
	public String queryPreferentialCommentList() {
	
		StringBuilder totalSql = new StringBuilder("select count(1) from preferential_comment where 1 = 1");
		StringBuilder recordSql = new StringBuilder("");
		recordSql.append("select c.id, c.p_id, c.login_name, c.nick_name, c.content, c.reply_time, f.activity_title from");
		recordSql.append("(");
		recordSql.append("select id, p_id, login_name, nick_name, content, reply_time from preferential_comment where 1 = 1");
		
		StringBuilder conditionSql = new StringBuilder();
		
		if (StringUtils.isNotEmpty(preferentialComment.getActivityTitle())) {
		
			conditionSql.append(" and p_id in (select id from latest_preferential t where t.activity_title like '%" + preferentialComment.getActivityTitle() + "%')");
		}
		
		if (StringUtils.isNotEmpty(preferentialComment.getLoginName())) {
			
			conditionSql.append(" and login_name like '%" + preferentialComment.getLoginName() + "%'");
		}

		if (StringUtils.isNotEmpty(preferentialComment.getNickName())) {
			
			conditionSql.append(" and nick_name like '%" + preferentialComment.getNickName() + "%'");
		}

		if (StringUtils.isNotEmpty(preferentialComment.getContent())) {
			
			conditionSql.append(" and content like '%" + preferentialComment.getContent() + "%'");
		}
		
		totalSql.append(conditionSql);
		
		if (StringUtils.isNotEmpty(by)) {
			
			conditionSql.append(" order by " + by + " " + order);
			
		} else {
			
			conditionSql.append(" order by reply_time desc");
		}
		
		if (size == null || size.intValue() == 0) {
			
			size = Page.PAGE_DEFAULT_SIZE;
		}
		
		if (pageIndex == null) {
			
			pageIndex = Page.PAGE_BEGIN_INDEX;
		}
		
		conditionSql.append(" LIMIT " + ((pageIndex - 1) * size) + ", " + size);
		
		recordSql.append(conditionSql);
		recordSql.append(") c, latest_preferential f where c.p_id = f.id");
		
		log.info("totalSql:" + totalSql.toString());
		log.info("recordSql:" + recordSql.toString());
		
		Page page = proposalService.queryForPagenation(recordSql.toString(), totalSql.toString(), pageIndex, size);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	// 查询最新优惠详情评论详情信息
	public String singlePreferentialComment() {
	
		HttpServletRequest request = getRequest();
		
		if (null == preferentialComment.getId() || 0 == preferentialComment.getId()) {
			
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		
		PreferentialComment obj = (PreferentialComment) proposalService.get(PreferentialComment.class, preferentialComment.getId());
		
		if (null == obj) {
			
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		
		request.setAttribute("preferentialComment", obj);
		
		return SUCCESS;
	}
	
	// 删除最新优惠详情评论记录信息
	public void deletePreferentialComment() {
	
		if (null == preferentialComment || StringUtils.isBlank(preferentialComment.getIds())) {
			
			GsonUtil.GsonObject("数据删除失败，失败原因：参数值错误！");
			return;
		}
		
		String sql = "DELETE from preferential_comment WHERE id in (:ids)";
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("ids", Arrays.asList(preferentialComment.getIds().split(",")));
		
		Session session = null;
		
		try {
			
			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();
			
			Query query = session.createSQLQuery(sql);
			query.setProperties(paramsMap);
			
			query.executeUpdate();
			
			GsonUtil.GsonObject("数据删除成功！");
		} catch(Exception e) {
			
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		} finally {
			
			if (null != session && session.isOpen()) {

				session.close();
			}
		}
	}
	
	// 查询专题记录信息
	public String querySpecialTopicList() {
	
		DetachedCriteria dc = DetachedCriteria.forClass(SpecialTopic.class);
		
		if (null != specialTopic && null != specialTopic.getTitle()) {
		
			dc.add(Restrictions.like("title", specialTopic.getTitle(), MatchMode.ANYWHERE));
		}
		
		if (null != specialTopic && null != specialTopic.getStatus()) {
		
			dc.add(Restrictions.eq("status", specialTopic.getStatus()));
		}
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			
			o = Order.desc("id");
		}

		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
		
	// 新增专题记录信息
	public void addSpecialTopic() {
		
		if (null != topicImage) {
			
			try {
				
				String pictureUrl = TopicClientUtil.uploadFile(topicImageFileName, topicImage);
				
				specialTopic.setPictureUrl(pictureUrl);
			} catch (Exception e) {
				
				log.error("专题图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("专题图片上传失败，请稍后再试！");
				return;
			}
		}
		
		specialTopic.setCreateTime(DateUtil.getCurrentDate());
		
		try {
			
			proposalService.save(specialTopic);
			
			GsonUtil.GsonObject("数据新增成功！");
		} catch(Exception e) {
			
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
		}
	}
		
	// 开启/禁用单个专题记录信息
	public void activeSpecialTopic() {
		
		if (null == specialTopic || null == specialTopic.getId() || 0 == specialTopic.getId()) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		
		SpecialTopic obj = (SpecialTopic) proposalService.get(SpecialTopic.class, specialTopic.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		
		try {
			
			obj.setStatus(specialTopic.getStatus());
			
			proposalService.update(obj);
			
			GsonUtil.GsonObject("数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
		
	// 删除单个专题记录信息
	public void deleteSpecialTopic() {
	
		if (null == specialTopic || null == specialTopic.getId() || 0 == specialTopic.getId()) {
			
			GsonUtil.GsonObject("数据删除失败，失败原因：参数值错误！");
			return;
		}
		
		SpecialTopic obj = (SpecialTopic) proposalService.get(SpecialTopic.class, specialTopic.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("数据删除失败，失败原因：删除的记录不存在！");
			return;
		}
		
		try {
			
			proposalService.delete(SpecialTopic.class, specialTopic.getId());
			
			GsonUtil.GsonObject("数据删除成功！");
		} catch(Exception e) {
			
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		}
	}
	
	// 查询单个专题记录信息
	public String singleSpecialTopic() {
	
		HttpServletRequest request = getRequest();
		
		if (null == specialTopic.getId() || 0 == specialTopic.getId()) {
			
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		
		SpecialTopic obj = (SpecialTopic) proposalService.get(SpecialTopic.class, specialTopic.getId());
		
		if (null == obj) {
			
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		
		if (StringUtils.isNotEmpty(obj.getPictureUrl())) {
		
			String imageName = obj.getPictureUrl().substring(obj.getPictureUrl().lastIndexOf("/") + 1);
			
			obj.setPictureUrl(FTPProperties.getInstance().getValue("topic_image_download_url") + imageName);
		}
		
		request.setAttribute("specialTopic", obj);
		
		return SUCCESS;
	}
	
	// 修改单个专题记录信息
	public void updateSpecialTopic() {
	
		if (null == specialTopic.getId() || 0 == specialTopic.getId()) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		
		SpecialTopic obj = (SpecialTopic) proposalService.get(SpecialTopic.class, specialTopic.getId());
		
		if (null == obj) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		
		if (null != topicImage) {
			
			try {
				
				String pictureUrl = TopicClientUtil.uploadFile(topicImageFileName, topicImage);
				
				specialTopic.setPictureUrl(pictureUrl);
			} catch (Exception e) {
				
				log.error("专题图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("专题图片上传失败，请稍后再试！");
				return;
			}
		} else {
			
			specialTopic.setPictureUrl(obj.getPictureUrl());
		}
		
		try {
			
			specialTopic.setCreateTime(obj.getCreateTime());
			
			proposalService.update(specialTopic);
			
			GsonUtil.GsonObject("数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	// 查询用户状态信息
	public String queryUserStatusList() {
	
		StringBuilder totalSql = new StringBuilder("select count(1)");
		StringBuilder recordSql = new StringBuilder("select s.loginname,s.discussflag");
		StringBuilder sb = new StringBuilder();
		
		sb.append(" from users u left join userstatus s on u.loginname = s.loginname where u.loginname is not null");
		
		if (null != userStatus && StringUtils.isNotBlank(userStatus.getLoginname())) {
			
			sb.append(" and u.loginname like '%" + userStatus.getLoginname() + "%'");
		}
		
		if (null != userStatus && null != userStatus.getDiscussflag()) {
			
			if (1 == userStatus.getDiscussflag()) {
				
				sb.append(" and (s.discussflag = 1 or s.discussflag is null)");
			} else {
			
				sb.append(" and s.discussflag = " + userStatus.getDiscussflag() + "");
			}
		}
		
		if (null != startTime) {

			sb.append(" and u.createtime >= '" + DateUtil.getDateFormat(startTime) + "'");
		}
		
		if (null != endTime) {
		
			sb.append(" and u.createtime <= '" + DateUtil.getDateFormat(endTime) + "'");
		}
		
		if (StringUtils.isNotEmpty(by)) {
		
			sb.append(" order by u." + by + " " + order + "");
		}
		
		totalSql = totalSql.append(sb);
		recordSql = recordSql.append(sb);
		
		log.info("recordSql=" + recordSql.toString());
		
		Page page = queryForPagenation(proposalService.getHibernateTemplate(), totalSql.toString(), recordSql.toString(), pageIndex, size);
		
		getRequest().setAttribute("page", page);
		
		return INPUT;
	}
	
	// 开启/禁用用户评论
	public void activeUserDiscuss() {
	
		if (null == userStatus || StringUtils.isBlank(userStatus.getLoginname())) {
		
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		
		Userstatus us = (Userstatus) proposalService.get(Userstatus.class, userStatus.getLoginname());
		
		if (null == us) {
			
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		
		try {
			
			us.setDiscussflag(userStatus.getDiscussflag());
			
			proposalService.update(us);
			
			GsonUtil.GsonObject("数据更新成功！");
		} catch(Exception e) {
			
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String reloadSpecialTopicCache() {
	
		String message = "缓存数据更新成功！";
		
		DetachedCriteria dc = DetachedCriteria.forClass(SpecialTopic.class);
		
		dc.add(Restrictions.eq("status",1));
			
		Order o = Order.desc("createTime");

		ProposalService proposalServiceTemp = (ProposalService) SpringFactoryHepler.getInstance("proposalService");
		
		Page page = PageQuery.queryForPagenation(proposalServiceTemp.getHibernateTemplate(), dc, 1, 5, o);
		
		List<SpecialTopic> topicListFromDb = page.getPageContents();
		
		List<IndexSpecialTopicVO> indextopicList = new ArrayList<IndexSpecialTopicVO>();
		
		if(topicListFromDb != null && topicListFromDb.size() > 0){
			
			for(SpecialTopic topic : topicListFromDb){
				
				IndexSpecialTopicVO vo = new IndexSpecialTopicVO();
				
				String pictureUrl = topic.getPictureUrl();
				String imageName  = pictureUrl.substring(pictureUrl.lastIndexOf("/") + 1);
				
				vo.setPictureUrl(FTPProperties.getInstance().getValue("topic_image_download_url") + imageName);
				vo.setUrl(topic.getTopicUrl());
				vo.setActionUrl(topic.getActionUrl());
				indextopicList.add(vo);
			}
		}
		
		Gson gson = new GsonBuilder().create();
		
		String json = gson.toJson(indextopicList);
		
		log.info("==================缓存数据更新成功:reloadSpecialTopicCache==================");
		log.info("=================="+ json + "==================");
		
		RedisUtil.set(CacheEnums.CACHEENUMS_TOPIC.getCode(),json);
		
		return message;
	}
	
	@SuppressWarnings("rawtypes")
	public Page queryForPagenation(HibernateTemplate hibernateTemplate, String totalSql, String recordSql, Integer pageIndex, Integer size) {
	
		Session session = null;
		Integer totalResults = 0;
		
		if (size == null || size.intValue() == 0) {
			
			size = Page.PAGE_DEFAULT_SIZE;
		}
		
		if (pageIndex == null || pageIndex.intValue() == 0) {
			
			pageIndex = Page.PAGE_BEGIN_INDEX;
		}
		
		Page page = new Page();
		page.setSize(size);
		
		try {
		
			session = hibernateTemplate.getSessionFactory().openSession();
			
			Object obj = session.createSQLQuery(totalSql).uniqueResult();
			
			if (null != obj) {
				
				totalResults = Integer.parseInt(String.valueOf(obj));
			}
			
			Integer pages = PagenationUtil.computeTotalPages(totalResults, size);
			
			page.setTotalRecords(totalResults);
			page.setTotalPages(pages);
			
			if (pageIndex > pages) {
				
				pageIndex = Page.PAGE_BEGIN_INDEX;
			}
			
			page.setPageNumber(pageIndex);
			
			List list = session.createSQLQuery(recordSql).setFirstResult((pageIndex -1) * size).setMaxResults(size).list();
			
			if (null == list) {
			
				list = new ArrayList();
			}
			
			if (null != list && !list.isEmpty()) {
			
				List<Userstatus> statusList = new ArrayList<Userstatus>();
				
				for (int i = 0, len = list.size(); i < len; i++) {
					
					Object[] o = (Object[]) list.get(i);
					
					Userstatus us = new Userstatus();
					us.setLoginname(String.valueOf(o[0]));
					us.setDiscussflag(null == o[1] ? 1 : Integer.parseInt(String.valueOf(o[1])));
					
					statusList.add(us);
				}
				
				page.setPageContents(statusList);
			} else {
				
				page.setPageContents(list);
			}
			
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
		} catch (Exception e) {
			
		} finally {
			
			if (null != session && session.isOpen()) {
				
				session.close();
			}
		}
		
		return page;
	}
	
		
	
	
	
	
	
	
	// 查询中奖信息
	public String queryLatestWinInfoList() {

		DetachedCriteria dc = DetachedCriteria.forClass(LatestWinInfo.class);

		if (null != latestWinInfo && null != latestWinInfo.getLoginName()) {

			dc.add(Restrictions.like("loginName", latestWinInfo.getLoginName(), MatchMode.ANYWHERE));
		}
		
		if (null != latestWinInfo && null != latestWinInfo.getGameTitle()) {

			dc.add(Restrictions.like("gameTitle", latestWinInfo.getGameTitle(), MatchMode.ANYWHERE));
		}

		if (null != latestWinInfo && null != latestWinInfo.getStatus()) {

			dc.add(Restrictions.eq("status", latestWinInfo.getStatus()));
		}

		Order o = null;

		if (StringUtils.isNotEmpty(by)) {

			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {

			o = Order.desc("id");
		}

		Page page = PageQuery.queryForPagenation(proposalService.getHibernateTemplate(), dc, pageIndex, size, o);

		getRequest().setAttribute("page", page);

		return INPUT;
	}

	// 新增中奖信息
	public void addLatestWinInfo() {

		if (null != gameIcon) {

			try {

				String iconUrl = FTPClientUtilX.uploadFile(gameIconFileName, gameIcon,"win_image_upload_url");

				latestWinInfo.setGameIcon(iconUrl);
			} catch (Exception e) {

				log.error("游戏图标上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("游戏图标上传失败，请稍后再试！");
				return;
			}
		}

		latestWinInfo.setCreateTime(DateUtil.getCurrentDate());
		latestWinInfo.setModifyTime(DateUtil.getCurrentDate());
		try {

			proposalService.save(latestWinInfo);

			GsonUtil.GsonObject("数据新增成功！");
		} catch (Exception e) {

			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
		}
	}

	// 开启/禁用单个中奖信息
	public void activeLatestWinInfo() {

		if (null == latestWinInfo || null == latestWinInfo.getId() || 0 == latestWinInfo.getId()) {

			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}

		LatestWinInfo obj = (LatestWinInfo) proposalService.get(LatestWinInfo.class, latestWinInfo.getId());

		if (null == obj) {

			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}

		try {

			obj.setStatus(latestWinInfo.getStatus());

			proposalService.update(obj);

			GsonUtil.GsonObject("数据更新成功！");
		} catch (Exception e) {

			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}

	// 删除单个中奖信息
	public void deleteLatestWinInfo() {

		if (null == latestWinInfo || null == latestWinInfo.getId() || 0 == latestWinInfo.getId()) {

			GsonUtil.GsonObject("数据删除失败，失败原因：参数值错误！");
			return;
		}

		LatestWinInfo obj = (LatestWinInfo) proposalService.get(LatestWinInfo.class, latestWinInfo.getId());

		if (null == obj) {

			GsonUtil.GsonObject("数据删除失败，失败原因：删除的记录不存在！");
			return;
		}

		try {

			proposalService.delete(LatestWinInfo.class, latestWinInfo.getId());

			GsonUtil.GsonObject("数据删除成功！");
		} catch (Exception e) {

			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		}
	}

	// 查询单个中奖信息
	public String singleLatestWinInfo() {

		HttpServletRequest request = getRequest();

		if (null == latestWinInfo.getId() || 0 == latestWinInfo.getId()) {

			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}

		LatestWinInfo obj = (LatestWinInfo) proposalService.get(LatestWinInfo.class, latestWinInfo.getId());

		if (null == obj) {

			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}

		if (StringUtils.isNotEmpty(obj.getGameIcon())) {

			String imageName = obj.getGameIcon().substring(obj.getGameIcon().lastIndexOf("/") + 1);

			obj.setGameIcon(FTPProperties.getInstance().getValue("win_image_download_url") + imageName);
		}

		request.setAttribute("latestWinInfo", obj);

		return SUCCESS;
	}
	
	
	// 修改单个中奖信息
	public void updateLatestWinInfo() {

		if (null == latestWinInfo.getId() || 0 == latestWinInfo.getId()) {

			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}

		LatestWinInfo obj = (LatestWinInfo) proposalService.get(LatestWinInfo.class, latestWinInfo.getId());

		if (null == obj) {

			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}

		if (null != gameIcon) {

			try {

				String iconUrl = FTPClientUtilX.uploadFile(gameIconFileName, gameIcon,"win_image_upload_url");;

				latestWinInfo.setGameIcon(iconUrl);
			} catch (Exception e) {

				log.error("专题图片上传发生异常，异常信息：" + e.getMessage());
				GsonUtil.GsonObject("专题图片上传失败，请稍后再试！");
				return;
			}
		} else {

			latestWinInfo.setGameIcon(obj.getGameIcon());
		}

		try {

			latestWinInfo.setModifyTime(DateUtil.getCurrentDate());
			latestWinInfo.setCreateTime(obj.getCreateTime());
			proposalService.update(latestWinInfo);

			GsonUtil.GsonObject("数据更新成功！");
		} catch (Exception e) {

			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}

	@SuppressWarnings("unchecked")
	public String reloadLatestWinInfoCache() {

		String message = "缓存数据更新成功！";

		DetachedCriteria dc = DetachedCriteria.forClass(LatestWinInfo.class);

		dc.add(Restrictions.eq("status", 1));

		Order o = Order.desc("modifyTime");

		ProposalService proposalServiceTemp = (ProposalService) SpringFactoryHepler.getInstance("proposalService");

		Page page = PageQuery.queryForPagenation(proposalServiceTemp.getHibernateTemplate(), dc, 1, 500, o);

		List<LatestWinInfo> winInfoes = page.getPageContents();

		List<IndexLatestWinInfoVO> winInfoVOes = new ArrayList<IndexLatestWinInfoVO>();

		if (winInfoes != null && winInfoes.size() > 0) {

			for (LatestWinInfo winInfo : winInfoes) {

				IndexLatestWinInfoVO vo = new IndexLatestWinInfoVO();
                
				vo.setLoginName(winInfo.getLoginName());
				vo.setBetAmount(String.format("%.2f",winInfo.getBetAmount()));
				vo.setWinAmount(String.format("%.2f",winInfo.getWinAmount()));
				vo.setGameTitle(winInfo.getGameTitle());
				String iconUrl = winInfo.getGameIcon();
				String imageName = iconUrl.substring(iconUrl.lastIndexOf("/") + 1);
				vo.setGameIcon(FTPProperties.getInstance().getValue("win_image_download_url") + imageName);
				vo.setForumUrl(winInfo.getForumUrl());
				
				winInfoVOes.add(vo);
			}
		}

		Gson gson = new GsonBuilder().create();

		String json = gson.toJson(winInfoVOes);

		RedisUtil.set(CacheEnums.CACHEENUMS_WIN.getCode(), json);

		return message;
	}
	
	// -------------------------------中奖信息 End -------------------------------	


	
	/**
	 * 更新首页热门优惠
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String reloadHotPreferentialCache() {

		String message = "缓存数据更新成功！";

		DetachedCriteria dc = DetachedCriteria.forClass(LatestPreferential.class);

		dc.add(Restrictions.eq("isNew", "1"));
		dc.add(Restrictions.eq("isActive", "1"));
		dc.add(Restrictions.eq("isPhone", "1"));

		Order o = Order.desc("receiveNumber");

		ProposalService proposalServiceTemp = (ProposalService) SpringFactoryHepler.getInstance("proposalService");

		Page page = PageQuery.queryForPagenation(proposalServiceTemp.getHibernateTemplate(), dc, 1, 4, o);

		List<LatestPreferential> hotInfoes = page.getPageContents();

		List<LatestPreferentialVO> hotInfoesForCache = new ArrayList<LatestPreferentialVO>();

		if (hotInfoes != null && !hotInfoes.isEmpty()) {

			String baseUrl = FTPProperties.getInstance().getValue("preferential_image_download_url");

			for (LatestPreferential obj : hotInfoes) {

				LatestPreferentialVO vo = new LatestPreferentialVO();

				vo.setId(obj.getId());
				vo.setType(obj.getType());
				vo.setOpenUrl(obj.getOpenUrl());

				if (obj.getActivityStartTime() != null) {

					vo.setActivityStartTime(DateUtil.getDateFormat(obj.getActivityStartTime()));
				}
				if (obj.getActivityEndTime() != null) {

					vo.setActivityEndTime(DateUtil.getDateFormat(obj.getActivityEndTime()));
				}

				if (StringUtils.isNotEmpty(obj.getActivityImageUrl())) {

					String activityImageName = obj.getActivityImageUrl()
							.substring(obj.getActivityImageUrl().lastIndexOf("/") + 1);
					vo.setActivityImageUrl(baseUrl + activityImageName);

				}
				if (StringUtils.isNotEmpty(obj.getNewImageUrl())) {

					String newImageName = obj.getNewImageUrl().substring(obj.getNewImageUrl().lastIndexOf("/") + 1);
					vo.setNewImageUrl(baseUrl + newImageName);

				}

				hotInfoesForCache.add(vo);
			}
		}

		Gson gson = new GsonBuilder().create();

		String json = gson.toJson(hotInfoesForCache);

		log.info("reloadHotPreferentialCache json = " + json);

		RedisUtil.set(CacheEnums.CACHEENUMS_HOT_PREFERENTIAL.getCode(), json);

		return message;
	}

	/**
	 * 更新U乐风采缓存数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String reloadStyle() {

		String message = "缓存数据更新成功！";

		DetachedCriteria dc = DetachedCriteria.forClass(LatestPreferential.class);

		dc.add(Restrictions.eq("isPhone", "1"));
		dc.add(Restrictions.eq("type", "001"));
		dc.add(Restrictions.eq("isQyStyle", "1"));

		Order o = Order.desc("activityStartTime");

		ProposalService proposalServiceTemp = (ProposalService) SpringFactoryHepler.getInstance("proposalService");

		Page page = PageQuery.queryForPagenation(proposalServiceTemp.getHibernateTemplate(), dc, 1, 1000, o);

		List<LatestPreferential> styleList = page.getPageContents();

		List<StyleVO> ulStyleForCache = new ArrayList<StyleVO>();

		if (styleList != null && !styleList.isEmpty()) {

			String baseUrl = FTPProperties.getInstance().getValue("preferential_image_download_url");

			for (LatestPreferential obj : styleList) {

				StyleVO vo = new StyleVO();

				vo.setId(obj.getId());
				vo.setOpenUrl(obj.getOpenUrl());
				vo.setOpenType(StringUtils.isNotEmpty(obj.getOpenUrl()) ? "browser" : "");

				if (obj.getActivityStartTime() != null) {

					vo.setYear(DateUtil.getDateFormat(obj.getActivityStartTime(), "yyyy"));
					vo.setYear_month(DateUtil.getDateFormat(obj.getActivityStartTime(), "yyyy-MM"));

				} else {

					continue;
				}

				if (StringUtils.isNotEmpty(obj.getActivityImageUrl())) {

					String activityImageName = obj.getActivityImageUrl()
							.substring(obj.getActivityImageUrl().lastIndexOf("/") + 1);
					vo.setActivityImageUrl(baseUrl + activityImageName);

				}

				vo.setActivityTitle(obj.getActivityTitle());
				vo.setActivitySummary(obj.getActivitySummary());
				ulStyleForCache.add(vo);
			}
		}

		Gson gson = new GsonBuilder().create();

		String json = gson.toJson(ulStyleForCache);

		log.info("reloadQyStyle json = " + json);

		RedisUtil.set(CacheEnums.CACHEENUMS_LD_STYLE.getCode(), json);

		return message;
	}
	
	/**
	 * 更新定制打包缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String reloadAppPakCustomCache() {
		String message = "缓存数据更新成功！";
		Boolean existflag = false;
		ProposalService proposalService = (ProposalService) SpringFactoryHepler.getInstance("proposalService");
		List<AppCustomVersionVO> versionCustomVoList = new ArrayList<AppCustomVersionVO>();
		List<AppPackageVersion> versionList = new ArrayList<AppPackageVersion>();
		List<AppPackageVersion> versionHisList = new ArrayList<AppPackageVersion>();
		DetachedCriteria versionIosDc = DetachedCriteria.forClass(AppPackageVersion.class);
		versionIosDc.add(Restrictions.eq("status",1)); //启用
		versionIosDc.add(Restrictions.eq("plat","iOS"));
		versionIosDc.addOrder(Order.desc("createTime"));
		List<AppPackageVersion> versionIosList = proposalService.findByCriteria(versionIosDc);
		if(null != versionIosList && !versionIosList.isEmpty()){
			versionList.add(versionIosList.get(0));
			if(versionIosList.size()>1){
				versionHisList.add(versionIosList.get(1));
			}
		}
		DetachedCriteria versionAndroidDc = DetachedCriteria.forClass(AppPackageVersion.class);
		versionAndroidDc.add(Restrictions.eq("status",1)); //启用
		versionAndroidDc.add(Restrictions.eq("plat","android"));
		versionAndroidDc.addOrder(Order.desc("createTime"));
		List<AppPackageVersion> versionAndroidList = proposalService.findByCriteria(versionAndroidDc);
		if(null != versionAndroidList && !versionAndroidList.isEmpty()){
			versionList.add(versionAndroidList.get(0));
			if(versionAndroidList.size()>1){
				versionHisList.add(versionAndroidList.get(1));
			}
		}
		List<String> agentAccountList = new ArrayList<String>();
		for(AppPackageVersion version:versionList){
			DetachedCriteria versionCustomDc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			versionCustomDc.add(Restrictions.eq("versionId", version.getId())); 
			versionCustomDc.add(Restrictions.eq("status", 1)); //启用
			versionCustomDc.add(Restrictions.eq("pakStatus", 2)); //打包完成
			List<AppPackageVersionCustom> versionCustomList = proposalService.findByCriteria(versionCustomDc);
			if(null !=versionCustomList && !versionCustomList.isEmpty()){
				for(AppPackageVersionCustom versionCustom: versionCustomList){
					agentAccountList.add(versionCustom.getAgentAccount());
					AppCustomVersionVO vo = new AppCustomVersionVO();
					vo.setAgentAccount(versionCustom.getAgentAccount());
					vo.setAgentCode(versionCustom.getAgentCode());
					vo.setPlat(versionCustom.getPlat());
					vo.setTitle(version.getVersionTitle());
					vo.setVersionCode(versionCustom.getVersionCode());
					vo.setUpgradeLog(version.getUpgradeLog());
					vo.setIsForceUpgrade(versionCustom.getIsForceUpgrade());
					if(StringUtils.equalsIgnoreCase(versionCustom.getPlat(), "ios")){
						vo.setPackageUrl(String.format("%s%s", "itms-services:///?action=download-manifest&url=",versionCustom.getPackageUrl()));
					}else{
						vo.setPackageUrl(versionCustom.getPackageUrl());
					}
					vo.setQrCodeUrl(versionCustom.getQrCodeUrl());
					vo.setModifyTime(versionCustom.getModifyTime());
					
					versionCustomVoList.add(vo);
				}
			}
		}
		//加入当前版本缺少的历史版本代理定制信息
		for(AppPackageVersion version:versionHisList){
			DetachedCriteria versionCustomDc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			versionCustomDc.add(Restrictions.eq("versionId", version.getId())); 
			versionCustomDc.add(Restrictions.eq("status", 1)); //启用
			versionCustomDc.add(Restrictions.eq("pakStatus", 2)); //打包完成
			List<AppPackageVersionCustom> versionCustomList = proposalService.findByCriteria(versionCustomDc);
			if(null !=versionCustomList && !versionCustomList.isEmpty()){
				for(AppPackageVersionCustom versionCustom: versionCustomList){
					existflag = false;
					for(AppCustomVersionVO versionCustomVO: versionCustomVoList){
						if(versionCustom.getPlat().equalsIgnoreCase(versionCustomVO.getPlat()) && 
								versionCustom.getAgentCode().equalsIgnoreCase(versionCustomVO.getAgentCode())){
							existflag = true;
							break;
						}
					}
					if(!existflag){
						agentAccountList.add(versionCustom.getAgentAccount());
						AppCustomVersionVO vo = new AppCustomVersionVO();
						vo.setAgentAccount(versionCustom.getAgentAccount());
						vo.setAgentCode(versionCustom.getAgentCode());
						vo.setPlat(versionCustom.getPlat());
						vo.setTitle(version.getVersionTitle());
						vo.setVersionCode(versionCustom.getVersionCode());
						vo.setUpgradeLog(version.getUpgradeLog());
						vo.setIsForceUpgrade(versionCustom.getIsForceUpgrade());
						if(StringUtils.equalsIgnoreCase(versionCustom.getPlat(), "ios")){
							vo.setPackageUrl(String.format("%s%s", "itms-services:///?action=download-manifest&url=",versionCustom.getPackageUrl()));
						}else{
							vo.setPackageUrl(versionCustom.getPackageUrl());
						}
						vo.setQrCodeUrl(versionCustom.getQrCodeUrl());
						vo.setModifyTime(versionCustom.getModifyTime());
						
						versionCustomVoList.add(vo);
					}
				}
			}
		}
		Set<String> agentAccountSet = new HashSet<String>(agentAccountList); 
		if(!agentAccountSet.isEmpty()){
			DetachedCriteria agentAddressDc = DetachedCriteria.forClass(AgentAddress.class);
			agentAddressDc.add(Restrictions.in("loginname",agentAccountSet));
			agentAddressDc.add(Restrictions.eq("deleteflag",0));
			List<AgentAddress> agentAddressList = proposalService.findByCriteria(agentAddressDc);
			if(null !=agentAddressList && !agentAddressList.isEmpty()){
				List<String> addressList=new ArrayList<String>();
				for(AppCustomVersionVO versionCustomVO: versionCustomVoList){
					addressList.clear();
					for(AgentAddress agentAddress: agentAddressList){
						if(versionCustomVO.getAgentAccount().equalsIgnoreCase(agentAddress.getLoginname())){
							addressList.add(agentAddress.getAddress());
						}
					}
					versionCustomVO.setAgentAddresses(addressList.toArray(new String[addressList.size()]));
				}
				addressList.clear();
			}	
		}
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(versionCustomVoList);
		RedisUtil.set(CacheEnums.CACHEENUMS_APPPAK.getCode(), json);

		return message;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public String reloadConstraintAddressCache() {

		String message = "缓存数据更新成功！";
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		log.info("==================连接缓存服务器成功==================");

		String sql = "select type_id,type_name,value from constraint_address_config where delete_flag = :deleteFlag";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deleteFlag", "1");

		List list = null;

		try {

			ProposalService proposalService = (ProposalService) SpringFactoryHepler.getInstance("proposalService");

			list = proposalService.getListBySql(sql, params);
		} catch (Exception e) {

			log.error("reloadConstraintAddressCache方法调用proposalService.getListBySql方法发生异常，异常信息：" + e.getMessage());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {

			if (null != list && !list.isEmpty()) {

				for (int i = 0, len = list.size(); i < len; i++) {

					Object[] obj = (Object[]) list.get(i);

					String id = String.valueOf(obj[0]);
					String value = String.valueOf(obj[2]);

					ArrayList<String> valueList = map.get(id);

					if (valueList == null) {

						valueList = new ArrayList<String>();
					}

					valueList.add(value);

					map.put(id, valueList);
				}

				Iterator<Entry<String, ArrayList<String>>> it = map.entrySet().iterator();
				while (it.hasNext()) {

					Entry<String, ArrayList<String>> entry = it.next();
					String key = entry.getKey();
					ArrayList<String> valueList = entry.getValue();
					String cacheKey = "";
					String cacheValue = dfh.utils.StringUtil.listToString(valueList);
					if ("1".equals(key)) {
						cacheKey = CacheEnums.CACHEENUMS_IPLIMIT01.getCode();
						log.info("接受ＩＰ：" + cacheValue);
						paramMap.put("ipAllow", cacheKey);
						paramMap.put("ipAllowValue", cacheValue);
					} else if ("2".equals(key)) {
						cacheKey = CacheEnums.CACHEENUMS_IPLIMIT02.getCode();
						log.info("拒绝ＩＰ：" + cacheValue);
						paramMap.put("ipDeny", cacheKey);
						paramMap.put("ipDenyValue", cacheValue);
					} else if ("3".equals(key)) {
						cacheKey = CacheEnums.CACHEENUMS_IPLIMIT03.getCode();
						log.info("拒绝地区：" + cacheValue);
						paramMap.put("areaDeny", cacheKey);
						paramMap.put("areaDenyValue", cacheValue);
					}
				}
			}

			Boolean flag = false;
			flag = RedisUtil.reloadCache(paramMap);
			if(flag)
			{
				message = "缓存数据更新成功！";
				log.info("==================更新缓存服务器成功==================");
			}else
			{
				message = "缓存数据更新失败！";
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("reloadConstraintAddressCache方法方法发生异常，异常信息：" + e.getMessage());
		}

		return message;
	}
	
	public ConstraintAddressConfig getConstraintAddressConfig() {
		return constraintAddressConfig;
	}

	public void setConstraintAddressConfig(ConstraintAddressConfig constraintAddressConfig) {
		this.constraintAddressConfig = constraintAddressConfig;
	}
	
	
	public PreferentialConfig getPreferentialConfig() {
		return preferentialConfig;
	}

	public void setPreferentialConfig(PreferentialConfig preferentialConfig) {
		this.preferentialConfig = preferentialConfig;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}
	
	public File getActivityImage() {
		return activityImage;
	}

	public void setActivityImage(File activityImage) {
		this.activityImage = activityImage;
	}

	public String getActivityImageFileName() {
		return activityImageFileName;
	}

	public void setActivityImageFileName(String activityImageFileName) {
		this.activityImageFileName = activityImageFileName;
	}

	public File getNewImage() {
		return newImage;
	}

	public void setNewImage(File newImage) {
		this.newImage = newImage;
	}

	public String getNewImageFileName() {
		return newImageFileName;
	}

	public void setNewImageFileName(String newImageFileName) {
		this.newImageFileName = newImageFileName;
	}

	public File getTopicImage() {
		return topicImage;
	}

	public void setTopicImage(File topicImage) {
		this.topicImage = topicImage;
	}

	public String getTopicImageFileName() {
		return topicImageFileName;
	}

	public void setTopicImageFileName(String topicImageFileName) {
		this.topicImageFileName = topicImageFileName;
	}
	
	public String getCacheMethodName() {
		return cacheMethodName;
	}
	
	public void setCacheMethodName(String cacheMethodName) {
		this.cacheMethodName = cacheMethodName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public LatestPreferential getLatestPreferential() {
		return latestPreferential;
	}

	public void setLatestPreferential(LatestPreferential latestPreferential) {
		this.latestPreferential = latestPreferential;
	}

	public PreferentialComment getPreferentialComment() {
		return preferentialComment;
	}

	public void setPreferentialComment(PreferentialComment preferentialComment) {
		this.preferentialComment = preferentialComment;
	}

	public UserSidName getUserSidName() {
		return userSidName;
	}

	public void setUserSidName(UserSidName userSidName) {
		this.userSidName = userSidName;
	}

	public SpecialTopic getSpecialTopic() {
		return specialTopic;
	}

	public void setSpecialTopic(SpecialTopic specialTopic) {
		this.specialTopic = specialTopic;
	}

	public Userstatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Userstatus userStatus) {
		this.userStatus = userStatus;
	}

	public LatestWinInfo getLatestWinInfo() {
		return latestWinInfo;
	}

	public void setLatestWinInfo(LatestWinInfo latestWinInfo) {
		this.latestWinInfo = latestWinInfo;
	}

	public File getGameIcon() {
		return gameIcon;
	}

	public void setGameIcon(File gameIcon) {
		this.gameIcon = gameIcon;
	}

	public String getGameIconFileName() {
		return gameIconFileName;
	}

	public void setGameIconFileName(String gameIconFileName) {
		this.gameIconFileName = gameIconFileName;
	}
	
}