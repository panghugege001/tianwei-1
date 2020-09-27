
package app.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import app.request.BatchPakRequestBean;
import app.request.PakRequestBean;
import app.request.PakRequestDataBean;
import app.response.ResponseBean;
import com.google.gson.Gson;

import app.model.po.AppPackageVersion;
import app.model.po.AppPackageVersionCustom;
import app.util.DateUtil;
import bsh.StringUtil;
import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.service.interfaces.AppPakService;
import dfh.utils.Configuration;
import dfh.utils.GsonUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

/**
 * 
 * @author stan
 *
 */
public class AppPakAction extends SubActionSupport {
	
	private static final long serialVersionUID = 1L;
	private static final String COMMON_WEBAPI_BASEURL_KEY = "common_webapi_baseurl"; 
	private static final String PRODUCT_KEY = "ld";
	
	private static Logger log = Logger.getLogger(AppPakAction.class);
	private static ExecutorService threadPoolExecutor =  Executors.newFixedThreadPool(2);
	
	private Integer size;
	private Integer pageIndex;
	private String order;
	private String by;
	
	private AppPackageVersion appPackageVersion;
	private AppPackageVersionCustom appPackageVersionCustom;
	private Users user;
	private List<Users> userList;
	private List<AppPackageVersionCustom> appPackageVersionCustomList;

	private String ids; 
	private String versionId; 
	private String plat; 

	private AppPakService appPakService;
	
	
	/**
	 * 查询版本信息
	 */
	public String queryAppPackageVersionList() {
		DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersion.class);
		if (null != appPackageVersion && StringUtils.isNotBlank(appPackageVersion.getVersionCode())) {
			dc.add(Restrictions.like("versionCode", appPackageVersion.getVersionCode().trim(), MatchMode.START));
		}
		if (null != appPackageVersion && StringUtils.isNotBlank(appPackageVersion.getPlat())) {
			dc.add(Restrictions.eq("plat", appPackageVersion.getPlat()));
		}
		if (null != appPackageVersion && null != appPackageVersion.getStatus()) {

			dc.add(Restrictions.eq("status", appPackageVersion.getStatus()));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			o = Order.desc("id");
		}
		Page page = PageQuery.queryForPagenation(appPakService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);

		return INPUT;
	}
	
	/**
	 * 单个版本信息
	 * @return
	 */
	public String singleAppPackageVersion() {
		HttpServletRequest request = getRequest();
		if (null == appPackageVersion.getId() || 0 == appPackageVersion.getId()) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		AppPackageVersion obj = (AppPackageVersion) appPakService.get(AppPackageVersion.class, appPackageVersion.getId());
		if (null == obj) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		request.setAttribute("appPackageVersion", obj);
		return SUCCESS;
	}
	
	/**
	 * 单个版本信息
	 * @return
	 */
	public String singleCopyAppPackageVersion() {
		HttpServletRequest request = getRequest();
		if (null == appPackageVersion.getId() || 0 == appPackageVersion.getId()) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		AppPackageVersion obj = (AppPackageVersion) appPakService.get(AppPackageVersion.class, appPackageVersion.getId());
		if (null == obj) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		request.setAttribute("appPackageVersion", obj);
		return SUCCESS;
	}
	
	/**
	 * 新增版本信息
	 */
	public void addAppPackageVersion() {
		appPackageVersion.setCreateTime(DateUtil.getCurrentDate());
		appPackageVersion.setModifyTime(DateUtil.getCurrentDate());
		try {
			appPakService.save(appPackageVersion);
			GsonUtil.GsonObject("数据新增成功！");
		} catch (Exception e) {
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 新增版本信息
	 */
	public void addCopyAppPackageVersion() {
		appPackageVersion.setCreateTime(DateUtil.getCurrentDate());
		appPackageVersion.setModifyTime(DateUtil.getCurrentDate());
		try {
			Integer currentId = appPackageVersion.getId();
			appPackageVersion.setId(null);
			Integer id = (Integer)appPakService.save(appPackageVersion);
			if(!(id>0)){
				log.error("数据新增失败，失败原因：版本信息新增失败！");
				GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
				return;
			}
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.eq("versionId", currentId));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> appVersionCustomList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != appVersionCustomList && !appVersionCustomList.isEmpty()){
				for(AppPackageVersionCustom appVersionCustom: appVersionCustomList){
					appVersionCustom.setId(null);
					appVersionCustom.setCreateTime(DateUtil.getCurrentDate());
					appVersionCustom.setModifyTime(DateUtil.getCurrentDate());
					appVersionCustom.setVersionId(id);
					appVersionCustom.setVersionCode(appPackageVersion.getVersionCode());
					appVersionCustom.setPlat(appPackageVersion.getPlat());
					appVersionCustom.setPackageName("");
					appVersionCustom.setIsUpgrade(true);
					appVersionCustom.setIsForceUpgrade(false);
					appVersionCustom.setStatus(1);
					appVersionCustom.setPakStatus(0);
					appVersionCustom.setPackageUrl("");
					appVersionCustom.setPublishUrl("");
					appVersionCustom.setQrCodeUrl("");
					appVersionCustom.setOperator("");
				}
				appPakService.saveAll(appVersionCustomList);
			}
			GsonUtil.GsonObject("数据新增成功！");
		} catch (Exception e) {
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
		}
	}

	/**
	 * 删除版本信息, 将先删除定制包信息
	 */
	public void deleteAppPackageVersion() {
		if (null == appPackageVersion || null == appPackageVersion.getId() || 0 == appPackageVersion.getId()) {
			GsonUtil.GsonObject("数据删除失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersion obj = (AppPackageVersion) appPakService.get(AppPackageVersion.class, appPackageVersion.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据删除失败，失败原因：要删除的记录不存在！");
			return;
		}		
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.eq("versionId", appPackageVersion.getId()));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> appPackageInfoList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != appPackageInfoList && !appPackageInfoList.isEmpty()){
		        for(AppPackageVersionCustom pakCustom: appPackageInfoList){
					if(pakCustom.getPakStatus() == 1){
						GsonUtil.GsonObject("数据删除失败，失败原因：存在定制打包运行中任务，不能删除！");
						return;
					}
		        }
		        appPakService.deleteAll(appPackageInfoList);
			}
			appPakService.delete(AppPackageVersion.class, appPackageVersion.getId());
			GsonUtil.GsonObject("数据删除成功！");
			if(null != appPackageInfoList && !appPackageInfoList.isEmpty()){
				List<AppPackageVersionCustom> forDeleteQiniuList=new ArrayList<AppPackageVersionCustom>();
		        for(AppPackageVersionCustom pakCustom: appPackageInfoList){
					if(pakCustom.getPakStatus() == 2 || pakCustom.getPakStatus() == 11){ //打包成功和失败的才删除，条件还需细化
						forDeleteQiniuList.add(pakCustom);
					}
		        }
		        if(!forDeleteQiniuList.isEmpty()){
			        log.info("删除七牛文件...");
			        final AppPackageVersionCustom[] forDeleteQiniuArray =  forDeleteQiniuList.toArray(new AppPackageVersionCustom[forDeleteQiniuList.size()]);
			        threadPoolExecutor.execute(new Runnable() {
			        	@Override
			        	public void run() {
				        	try {
				        		batchDeletePak(forDeleteQiniuArray);
				        	} 
				        	catch (Exception e) {
				        		e.printStackTrace();
				        	}
			        	}
			        });
		        }
			}
			
		} catch (Exception e) {
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 修改版本信息
	 */
	public void updateAppPackageVersion() {
		if (null == appPackageVersion.getId() || 0 == appPackageVersion.getId()) {
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersion obj = (AppPackageVersion) appPakService.get(AppPackageVersion.class, appPackageVersion.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		try {
			appPackageVersion.setModifyTime(DateUtil.getCurrentDate());
			appPackageVersion.setCreateTime(obj.getCreateTime());
			if(null == appPackageVersion.getOperator()) appPackageVersion.setOperator(StringUtils.EMPTY);
			if(null == appPackageVersion.getUpgradeLog()) appPackageVersion.setUpgradeLog(StringUtils.EMPTY);
			appPakService.update(appPackageVersion);

			GsonUtil.GsonObject("数据更新成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 开启/禁用版本信息
	 */
	public void activeAppPackageVersion() {
		if (null == appPackageVersion || null ==appPackageVersion.getId() || 0 == appPackageVersion.getId()) {
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersion obj = (AppPackageVersion) appPakService.get(AppPackageVersion.class, appPackageVersion.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		try {
			obj.setStatus(appPackageVersion.getStatus());
			appPakService.update(obj);
			GsonUtil.GsonObject("数据更新成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 查询App定制版本信息
	 */
	public String queryAppPackageVersionCustomList() {
		DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
		if (StringUtils.isNotBlank(versionId)) {
			dc.add(Restrictions.eq("versionId", Integer.parseInt(versionId)));
		}
		if (null != appPackageVersionCustom && StringUtils.isNotBlank(appPackageVersionCustom.getAgentAccount())) {
			dc.add(Restrictions.like("agentAccount", appPackageVersionCustom.getAgentAccount().trim(), MatchMode.START));
		}
		if (null != appPackageVersionCustom && StringUtils.isNotBlank(appPackageVersionCustom.getAgentCode())) {
			dc.add(Restrictions.like("agentCode", appPackageVersionCustom.getAgentCode().trim(), MatchMode.START));
		}
		if (null != appPackageVersionCustom && null != appPackageVersionCustom.getStatus()) {
			dc.add(Restrictions.eq("status", appPackageVersionCustom.getStatus()));
		}
		if (null != appPackageVersionCustom && null != appPackageVersionCustom.getPakStatus()) {
			dc.add(Restrictions.eq("pakStatus", appPackageVersionCustom.getPakStatus()));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			o = Order.desc("id");
		}
		Page page = PageQuery.queryForPagenation(appPakService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("pageSub", page);

		return INPUT;
	}
	
	/**
	 * 单个定制版本信息
	 * @return
	 */
	public String singleAppPackageVersionCustom() {
		HttpServletRequest request = getRequest();
		if (null == appPackageVersionCustom.getId() || 0 == appPackageVersionCustom.getId()) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		AppPackageVersionCustom obj = (AppPackageVersionCustom) appPakService.get(AppPackageVersionCustom.class, appPackageVersionCustom.getId());
		if (null == obj) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		request.setAttribute("appPackageVersionCustom", obj);
		return SUCCESS;
	}
	
	/**
	 * 定制打包代理选择
	 * @return
	 */
	public String selectPackageVersionAgent() {
		HttpServletRequest request = getRequest();
		if (StringUtils.isBlank(versionId)) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：参数值错误！");
			return INPUT;
		}
		AppPackageVersion obj = (AppPackageVersion) appPakService.get(AppPackageVersion.class, Integer.parseInt(versionId));
		if (null == obj) {
			request.setAttribute("tipMessage", "数据查询失败，失败原因：查询的记录不存在！");
			return INPUT;
		}
		request.setAttribute("appPackageVersion", obj);
		return SUCCESS;
	}
	
	/**
	 * 批量新增定制版本信息
	 */
	public void addAppPackageVersionCustomList() {
		String[] loginnames = null;
		if(StringUtils.isBlank(ids) || null == appPackageVersion || appPackageVersion.getId() == 0 || 
				StringUtils.isBlank(appPackageVersion.getVersionCode()) || StringUtils.isBlank(appPackageVersion.getPlat())){
			GsonUtil.GsonObject("数据批量增加失败，失败原因：数据请求错误！");
			return;
		}
	    loginnames = ids.split(",");
		if(loginnames.length<1){
			GsonUtil.GsonObject("数据批量增加失败，失败原因：没有选择记录！");
			return;
		}
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.in("loginname",loginnames));
			@SuppressWarnings("unchecked")
			List<Users> userList = (List<Users>)appPakService.findByCriteria(dc);
			List<AppPackageVersionCustom> addList = new ArrayList<AppPackageVersionCustom>();
			for(int i=0;i<userList.size();i++){
	        	AppPackageVersionCustom custom = new AppPackageVersionCustom();
	        	custom.setVersionId(appPackageVersion.getId());
	        	custom.setVersionCode(appPackageVersion.getVersionCode());
	        	custom.setPlat(appPackageVersion.getPlat());
	        	custom.setAgentAccount(userList.get(i).getLoginname());
	        	custom.setAgentCode(userList.get(i).getId().toString());
	        	custom.setStatus(1); //默认启用
	        	custom.setPakStatus(0); //未打包
	        	custom.setIsUpgrade(true);
	        	custom.setIsForceUpgrade(false);
	        	custom.setCreateTime(DateUtil.getCurrentDate());
	        	custom.setModifyTime(DateUtil.getCurrentDate());
	        	addList.add(custom);
	        }
			if(!addList.isEmpty()){
				appPakService.saveAll(addList);	
			}
			GsonUtil.GsonObject("数据批量增加成功！");
		} catch (Exception e) {
			log.error("数据新增失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据新增发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 批量删除定制版本信息
	 */
	public void deleteAppPackageVersionCustomList() {
		String[] customIds = null;
		if(StringUtils.isBlank(ids)){
			GsonUtil.GsonObject("数据批量删除失败，失败原因：数据请求错误！");
			return;
		}
		customIds = ids.split(",");
		if(customIds.length<1){
		    GsonUtil.GsonObject("数据批量删除失败，失败原因：没有选择记录！");
		    return;
	    }
		Integer[] intCustomIds = new Integer[customIds.length];
		for(int i=0;i<customIds.length;i++){
			intCustomIds[i] = (Integer.parseInt(customIds[i]));
		}
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.in("id",intCustomIds));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> customList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != customList && !customList.isEmpty()){
				for(AppPackageVersionCustom pakCustom: customList){
					if(pakCustom.getPakStatus() == 1){
						GsonUtil.GsonObject("数据删除失败，失败原因：定制打包运行中，不能删除！");
						return;
					}
				}
				appPakService.deleteAll(customList);	
			}
	        GsonUtil.GsonObject("数据批量删除成功！");
			List<AppPackageVersionCustom> forDeleteQiniuList = new ArrayList<AppPackageVersionCustom>();
	        for(AppPackageVersionCustom pakCustom: customList){
				if(pakCustom.getPakStatus() == 2 || pakCustom.getPakStatus() == 11){ //打包成功和失败的才删除，条件还需细化
					forDeleteQiniuList.add(pakCustom);
				}
	        }
	        if(!forDeleteQiniuList.isEmpty()){
	        	log.info("删除七牛文件...");
		        final AppPackageVersionCustom[] forDeleteQiniuArray =  forDeleteQiniuList.toArray(new AppPackageVersionCustom[forDeleteQiniuList.size()]);
		        threadPoolExecutor.execute(new Runnable() {
		        	@Override
		        	public void run() {
			        	try {
			        		batchDeletePak(forDeleteQiniuArray);
			        	} 
			        	catch (Exception e) {
			        		e.printStackTrace();
			        	}
		        	}
		        });
	        }
		} catch (Exception e) {
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 删除定制版本信息
	 */
	public void deleteAppPackageVersionCustom() {
		if (null == appPackageVersionCustom || null == appPackageVersionCustom.getId() || 0 == appPackageVersionCustom.getId()) {
			GsonUtil.GsonObject("数据删除失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersionCustom obj = (AppPackageVersionCustom) appPakService.get(AppPackageVersionCustom.class, appPackageVersionCustom.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据删除失败，失败原因：要删除的记录不存在！");
			return;
		}
		if(obj.getPakStatus()==1){
			GsonUtil.GsonObject("数据删除失败，失败原因：定制打包运行中，不能删除！");
			return;
		}
		try {
			appPakService.delete(AppPackageVersionCustom.class, appPackageVersionCustom.getId());
			GsonUtil.GsonObject("数据删除成功！");
			if(obj.getPakStatus() == 2 || obj.getPakStatus() == 11){ //打包成功和失败的才删除，条件还需细化
				log.info("删除七牛信息...");
				deletePak(obj);	
			}
		} catch (Exception e) {
			log.error("数据删除失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据删除发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 修改App定制版本信息
	 */
	public void updateAppPackageVersionCustom() {
		if (null == appPackageVersionCustom.getId() || 0 == appPackageVersionCustom.getId()) {
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersionCustom obj = (AppPackageVersionCustom) appPakService.get(AppPackageVersionCustom.class, appPackageVersionCustom.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		try {
			appPackageVersionCustom.setModifyTime(DateUtil.getCurrentDate());
			appPackageVersionCustom.setCreateTime(obj.getCreateTime());
			if(null == appPackageVersionCustom.getPackageName()) appPackageVersionCustom.setPackageName(StringUtils.EMPTY);
			if(null == appPackageVersionCustom.getPackageUrl()) appPackageVersionCustom.setPackageUrl(StringUtils.EMPTY);
			if(null == appPackageVersionCustom.getQrCodeUrl()) appPackageVersionCustom.setQrCodeUrl(StringUtils.EMPTY);
			if(null == appPackageVersionCustom.getPublishUrl()) appPackageVersionCustom.setPublishUrl(StringUtils.EMPTY);
			if(null == appPackageVersionCustom.getOperator()) appPackageVersionCustom.setOperator(StringUtils.EMPTY);
			appPakService.update(appPackageVersionCustom);

			GsonUtil.GsonObject("数据更新成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/**
	 *  发行定制版
	 */
	public void activeAppPackageVersionCustom() {
		if (null == appPackageVersionCustom || null ==appPackageVersionCustom.getId() || 0 == appPackageVersionCustom.getId()) {
			GsonUtil.GsonObject("数据更新失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersionCustom obj = (AppPackageVersionCustom) appPakService.get(AppPackageVersionCustom.class, appPackageVersionCustom.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据更新失败，失败原因：更新的记录不存在！");
			return;
		}
		try {
			obj.setStatus(appPackageVersionCustom.getStatus());
			appPakService.update(obj);
			GsonUtil.GsonObject("数据更新成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 查询代理信息
	 */
	public String queryAppPackageAgentList() {
		if(null == appPackageVersion || appPackageVersion.getId() == 0 || 
				StringUtils.isBlank(appPackageVersion.getVersionCode()) || StringUtils.isBlank(appPackageVersion.getPlat())){
			GsonUtil.GsonObject("数据查询失败，失败原因：数据请求错误！");
			return INPUT;
		}
		DetachedCriteria dcV = DetachedCriteria.forClass(AppPackageVersionCustom.class);
		dcV.add(Restrictions.eq("versionId", appPackageVersion.getId()));
		@SuppressWarnings("unchecked")
		List<AppPackageVersionCustom> customList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dcV);
        List<String> loginnameList = new ArrayList<String>();
        for(AppPackageVersionCustom custom: customList){
        	loginnameList.add(custom.getAgentAccount());
        }
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		if (null != user && StringUtils.isNotBlank(user.getLoginname())) {
			String[] loginnames = user.getLoginname().trim().split("\\s*[\\,\\;]+\\s*|\\s*[\\,\\;]*\\s*\\r*\\n+\\s*[\\,\\;]*\\s*|\\s+");
			if(loginnames.length>1){
				dc.add(Restrictions.in("loginname", loginnames));
			}else{
				dc.add(Restrictions.like("loginname", user.getLoginname().trim(), MatchMode.START));
			}
		}
		if (null != user && null != user.getId() && user.getId() != 0) {
			dc.add(Restrictions.eq("id", user.getId()));
		}
		dc.add(Restrictions.eq("flag",0));
		dc.add(Restrictions.eq("role", "AGENT"));
		dc.add(Restrictions.not(Restrictions.eq("agcode","")));
		if(!loginnameList.isEmpty()){
			dc.add(Restrictions.not(Restrictions.in("loginname", loginnameList)));	
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		} else {
			o = Order.desc("id");
		}
		Page page = PageQuery.queryForPagenation(appPakService.getHibernateTemplate(), dc, pageIndex, size, o);
		getRequest().setAttribute("page", page);
		getRequest().setAttribute("appPackageVersion", appPackageVersion);

		return INPUT;
	}
	
	/**
	 * 处理版本定制
	 */
	public void processAppPackageVersionCustomList() {
		String[] customIds = null;
		if(StringUtils.isBlank(ids)){
			GsonUtil.GsonObject("数据处理失败，失败原因：数据请求错误！");
			return;
		}
		customIds = ids.split(",");
		if(customIds.length<1){
		    GsonUtil.GsonObject("数据处理失败，失败原因：没有选择记录！");
		    return;
	    }
		Integer[] intCustomIds = new Integer[customIds.length];
		for(int i=0;i<customIds.length;i++){
			intCustomIds[i] = (Integer.parseInt(customIds[i]));
		}
		try {
			DetachedCriteria dcA = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dcA.add(Restrictions.eq("pakStatus",1));
			dcA.add(Restrictions.in("id",intCustomIds));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> customProcessingList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dcA);
			if(null !=customProcessingList && !customProcessingList.isEmpty()){
				GsonUtil.GsonObject("数据处理失败，失败原因：存在打包进行中的任务，请重新选择！");
				return;
			}
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.in("id",intCustomIds));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> customList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != customList && !customList.isEmpty()){
		        for(AppPackageVersionCustom custom: customList){
		        	custom.setPakStatus(1); 
		        }
		        appPakService.saveAll(customList);
		        final AppPackageVersionCustom[] customArray =  customList.toArray(new AppPackageVersionCustom[customList.size()]);
		        threadPoolExecutor.execute(new Runnable() {
		        	@Override
		        	public void run() {
			        	try {
			        		batchExecuteAppPack(customArray);
			        	} 
			        	catch (Exception e) {
			        		e.printStackTrace();
			        	}
		        	}
		        });
			}
	        GsonUtil.GsonObject("批量打包已成功执行！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	
	/**
	 * 处理版本定制
	 */
	public void processAppPackageVersionCustom() {
		if (null == appPackageVersionCustom || null ==appPackageVersionCustom.getId() || 0 == appPackageVersionCustom.getId()
				|| StringUtils.isBlank(appPackageVersionCustom.getPlat())) {
			GsonUtil.GsonObject("数据处理失败，失败原因：参数值错误！");
			return;
		}
		AppPackageVersionCustom obj = (AppPackageVersionCustom) appPakService.get(AppPackageVersionCustom.class, appPackageVersionCustom.getId());
		if (null == obj) {
			GsonUtil.GsonObject("数据处理失败，失败原因：对应记录不存在！");
			return;
		}
		if(obj.getPakStatus()==1){
			GsonUtil.GsonObject("数据处理失败，失败原因：该记录打包进行中，请等待！");
			return;
		}
		try {
			obj.setPakStatus(1); 
        	appPakService.update(obj);
        	GsonUtil.GsonObject("打包已成功执行！");
        	executeAppPack(obj);
		} catch (Exception e) {
			log.error("处理定制版失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("处理定制版发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 
	 * @param pakCustom
	 */
	private void executeAppPack(AppPackageVersionCustom pakCustom){
		Gson jsonObj = new Gson();
		String apiUrl = Configuration.getInstance().getValue(COMMON_WEBAPI_BASEURL_KEY);
		//apiUrl = SpecialEnvironmentStringPBEConfig.getPlainText(apiUrl);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(apiUrl+"/app/v1/pakExecute");
		PakRequestBean bean = new PakRequestBean();
		bean.setPlat(pakCustom.getPlat());
		bean.setProduct(PRODUCT_KEY);
		PakRequestDataBean data = new PakRequestDataBean();
		data.setAgentCode(pakCustom.getAgentCode());
		data.setVersionCode(pakCustom.getVersionCode());
		data.setId(pakCustom.getId());
		bean.setData(data);
		
		String requestJson = jsonObj.toJson(bean);
		System.out.println("请求参数明文json:" + requestJson);
		//requestJson = AESUtil.getInstance().encrypt(requestJson);
		//System.out.println("请求参数密文json:" + requestJson);
		method.setParameter("requestData", requestJson);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);
			System.out.println("responseString = " + responseString);
			ResponseBean responseBean = jsonObj.fromJson(responseString, ResponseBean.class);
			System.out.println("返回的密文：" + responseBean.getResponseData());
			//responseString = AESUtil.getInstance().decrypt(responseBean.getResponseData());
			System.out.println("返回的明文json数据：" + responseString);
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 
	 * @param pakCustom
	 */
	private void batchExecuteAppPack(AppPackageVersionCustom[] pakCustoms){
		Gson jsonObj = new Gson();
		String apiUrl = Configuration.getInstance().getValue(COMMON_WEBAPI_BASEURL_KEY);
		//apiUrl = SpecialEnvironmentStringPBEConfig.getPlainText(apiUrl);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(apiUrl+"/app/v1/batchPakExecute");
		BatchPakRequestBean bean = new BatchPakRequestBean();
		bean.setProduct(PRODUCT_KEY);
		List<PakRequestDataBean> data = new ArrayList<PakRequestDataBean>(); 
		for(int i=0;i<pakCustoms.length;i++){
			if(i==0){
				bean.setPlat(pakCustoms[i].getPlat());
			}
			PakRequestDataBean dataBean = new PakRequestDataBean();
			dataBean.setAgentCode(pakCustoms[i].getAgentCode());
			dataBean.setVersionCode(pakCustoms[i].getVersionCode());
			dataBean.setId(pakCustoms[i].getId());
			data.add(dataBean);
		}
		bean.setData(data);
		
		String requestJson = jsonObj.toJson(bean);
		System.out.println("请求参数明文json:" + requestJson);
		//requestJson = AESUtil.getInstance().encrypt(requestJson);
		//System.out.println("请求参数密文json:" + requestJson);
		method.setParameter("requestData", requestJson);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);
			System.out.println("responseString = " + responseString);
			ResponseBean responseBean = jsonObj.fromJson(responseString, ResponseBean.class);
			System.out.println("返回的密文：" + responseBean.getResponseData());
			//responseString = AESUtil.getInstance().decrypt(responseBean.getResponseData());
			System.out.println("返回的明文json数据：" + responseString);
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 
	 * @param pakCustom
	 */
	private void deletePak(AppPackageVersionCustom pakCustom){
		Gson jsonObj = new Gson();
		String apiUrl = Configuration.getInstance().getValue(COMMON_WEBAPI_BASEURL_KEY);
		//apiUrl = SpecialEnvironmentStringPBEConfig.getPlainText(apiUrl);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(apiUrl+"/app/v1/deletePak");
		PakRequestBean bean = new PakRequestBean();
		bean.setPlat(pakCustom.getPlat());
		bean.setProduct(PRODUCT_KEY);
		PakRequestDataBean data = new PakRequestDataBean();
		data.setAgentCode(pakCustom.getAgentCode());
		data.setVersionCode(pakCustom.getVersionCode());
		bean.setData(data);
		
		String requestJson = jsonObj.toJson(bean);
		System.out.println("请求参数明文json:" + requestJson);
		//requestJson = AESUtil.getInstance().encrypt(requestJson);
		//System.out.println("请求参数密文json:" + requestJson);
		method.setParameter("requestData", requestJson);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);
			System.out.println("responseString = " + responseString);
			ResponseBean responseBean = jsonObj.fromJson(responseString, ResponseBean.class);
			System.out.println("返回的密文：" + responseBean.getResponseData());
			//responseString = AESUtil.getInstance().decrypt(responseBean.getResponseData());
			System.out.println("返回的明文json数据：" + responseString);
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 
	 * @param pakCustom
	 */
	private void batchDeletePak(AppPackageVersionCustom[] pakCustomes){
		Gson jsonObj = new Gson();
		String apiUrl = Configuration.getInstance().getValue(COMMON_WEBAPI_BASEURL_KEY);
		//apiUrl = SpecialEnvironmentStringPBEConfig.getPlainText(apiUrl);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(apiUrl+"/app/v1/batchDeletePak");
		BatchPakRequestBean bean = new BatchPakRequestBean();
		bean.setProduct(PRODUCT_KEY);
		List<PakRequestDataBean> data = new ArrayList<PakRequestDataBean>(); 
		for(int i=0;i<pakCustomes.length;i++){
			if(i==0){
				bean.setPlat(pakCustomes[i].getPlat());
			}
			PakRequestDataBean dataBean = new PakRequestDataBean();
			dataBean.setAgentCode(pakCustomes[i].getAgentCode());
			dataBean.setVersionCode(pakCustomes[i].getVersionCode());
			data.add(dataBean);
	    }
		bean.setData(data);
		
		String requestJson = jsonObj.toJson(bean);
		System.out.println("请求参数明文json:" + requestJson);
		//requestJson = AESUtil.getInstance().encrypt(requestJson);
		//System.out.println("请求参数密文json:" + requestJson);
		method.setParameter("requestData", requestJson);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);
			System.out.println("responseString = " + responseString);
			ResponseBean responseBean = jsonObj.fromJson(responseString, ResponseBean.class);
			System.out.println("返回的密文：" + responseBean.getResponseData());
			//responseString = AESUtil.getInstance().decrypt(responseBean.getResponseData());
			System.out.println("返回的明文json数据：" + responseString);
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 重置打包中状态记录 
	 */
	public void resetPakingVersionCustomList() {
		String[] customIds = null;
		if(StringUtils.isBlank(ids)){
			GsonUtil.GsonObject("数据更新失败，失败原因：数据请求错误！");
			return;
		}
		customIds = ids.split(",");
		if(customIds.length<1){
		    GsonUtil.GsonObject("数据更新失败，失败原因：没有选择记录！");
		    return;
	    }
		Integer[] intCustomIds = new Integer[customIds.length];
		for(int i=0;i<customIds.length;i++){
			intCustomIds[i] = (Integer.parseInt(customIds[i]));
		}
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.eq("pakStatus",1)); 
			dc.add(Restrictions.in("id",intCustomIds));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> customList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != customList && !customList.isEmpty()){
		        for(AppPackageVersionCustom custom: customList){
		        	custom.setPakStatus(0);  
		        }
		        appPakService.saveAll(customList);
			}
	        GsonUtil.GsonObject("打包中状态重置成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 启用强制升级 
	 */
	public void enableForceUpgradeVersionCustomList() {
		String[] customIds = null;
		if(StringUtils.isBlank(ids)){
			GsonUtil.GsonObject("数据更新失败，失败原因：数据请求错误！");
			return;
		}
		customIds = ids.split(",");
		if(customIds.length<1){
		    GsonUtil.GsonObject("数据更新失败，失败原因：没有选择记录！");
		    return;
	    }
		Integer[] intCustomIds = new Integer[customIds.length];
		for(int i=0;i<customIds.length;i++){
			intCustomIds[i] = (Integer.parseInt(customIds[i]));
		}
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.eq("isForceUpgrade",false)); 
			dc.add(Restrictions.in("id",intCustomIds));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> customList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != customList && !customList.isEmpty()){
		        for(AppPackageVersionCustom custom: customList){
		        	custom.setIsForceUpgrade(true);  
		        }
		        appPakService.saveAll(customList);
			}
	        GsonUtil.GsonObject("启用强制升级成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
	}
	
	/**
	 * 禁用强制升级
	 */
	public void disableForceUpgradeVersionCustomList() {
		String[] customIds = null;
		if(StringUtils.isBlank(ids)){
			GsonUtil.GsonObject("数据更新失败，失败原因：数据请求错误！");
			return;
		}
		customIds = ids.split(",");
		if(customIds.length<1){
		    GsonUtil.GsonObject("数据更新失败，失败原因：没有选择记录！");
		    return;
	    }
		Integer[] intCustomIds = new Integer[customIds.length];
		for(int i=0;i<customIds.length;i++){
			intCustomIds[i] = (Integer.parseInt(customIds[i]));
		}
		try {
			DetachedCriteria dc = DetachedCriteria.forClass(AppPackageVersionCustom.class);
			dc.add(Restrictions.eq("isForceUpgrade",true)); 
			dc.add(Restrictions.in("id",intCustomIds));
			@SuppressWarnings("unchecked")
			List<AppPackageVersionCustom> customList = (List<AppPackageVersionCustom>)appPakService.findByCriteria(dc);
			if(null != customList && !customList.isEmpty()){
		        for(AppPackageVersionCustom custom: customList){
		        	custom.setIsForceUpgrade(false);  
		        }
		        appPakService.saveAll(customList);
			}
	        GsonUtil.GsonObject("禁用强制升级成功！");
		} catch (Exception e) {
			log.error("数据更新失败，失败原因：" + e.getMessage());
			GsonUtil.GsonObject("数据更新发生异常，请稍后再试！");
		}
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

	public AppPakService getAppPakService() {
		return appPakService;
	}

	public void setAppPakService(AppPakService appPakService) {
		this.appPakService = appPakService;
	}

	public AppPackageVersion getAppPackageVersion() {
		return appPackageVersion;
	}

	public void setAppPackageVersion(AppPackageVersion appPackageVersion) {
		this.appPackageVersion = appPackageVersion;
	}

	public AppPackageVersionCustom getAppPackageVersionCustom() {
		return appPackageVersionCustom;
	}

	public void setAppPackageVersionCustom(AppPackageVersionCustom appPackageVersionCustom) {
		this.appPackageVersionCustom = appPackageVersionCustom;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<Users> getUserList() {
		return userList;
	}

	public void setUserList(List<Users> userList) {
		this.userList = userList;
	}

	public List<AppPackageVersionCustom> getAppPackageVersionCustomList() {
		return appPackageVersionCustomList;
	}

	public void setAppPackageVersionCustomList(List<AppPackageVersionCustom> appPackageVersionCustomList) {
		this.appPackageVersionCustomList = appPackageVersionCustomList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getPlat() {
		return plat;
	}

	public void setPlat(String plat) {
		this.plat = plat;
	}

	
}