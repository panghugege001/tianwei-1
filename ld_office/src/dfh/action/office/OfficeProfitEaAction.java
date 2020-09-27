package dfh.action.office;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nnti.office.model.auth.Operator;

import dfh.action.SubActionSupport;
import dfh.dao.LogDao;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.notdb.TaskUserRecordVo;
import dfh.service.interfaces.OperatorService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.PagenationUtil;
import dfh.utils.StringUtil;
import dfh.utils.TelCrmUtil;

public class OfficeProfitEaAction extends SubActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OfficeProfitEaAction.class);

	private String loginname;
	private String by;
	private String order;
	private Date startPt;
	private Date endPt;
	private Integer size;
	private Integer pageIndex;
	private Date startTime;
	private Date endTime;
	private String platform;
	private OperatorService operatorService;
	
	private String pno;


	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	private String part;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private String bankcard;
	private String bankname;
	private String realname;
	private String type;
	private String amount;
	private String notes;
	private HBConfig hbConfig ;

	public HBConfig getHbConfig() {
		return hbConfig;
	}

	public void setHbConfig(HBConfig hbConfig) {
		this.hbConfig = hbConfig;
	}
	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	private String isused;
	private Integer id;
	private Double limitStartMoney;
	private Double limitEndMoney;
	private String title;
	private Double percent;
	private Integer betMultiples;
	private Double limitMoney;
	private Date createtime;
	private Date updatetime;
	
	private String starttime;
	private String endtime;
	private Integer times;
	private Integer timesflag;
	private String vip;
	private String aliasTitle;
	
	private YouHuiConfig youhuiConfig;
	private String uuid;
	private Double bet;
	private Double profit;
	private PlatformData platformDataxx;
	
	private String ids;
	private String ivr;
	private LogDao logDao;
	private Integer isAdd;
	private String intro;
	private String address;
	private String oldaddress;
	private Integer deleteflag;
	private String alipayAccount;
	
	// 是否启用机器码验证，0:否/1:是
	private Integer machineCodeEnabled;
	// 机器码使用次数
	private Integer machineCodeTimes;
	// 申请通道 1官网、2WEB、3安卓APP、4苹果APP
	private String isPhone;
	// 游戏平台
	private String platformName;

	private Double maxMoney;
		
	private Double minMoney;
	
	private ExperienceGoldConfig experienceGoldConfig;
		
	
	
	public Integer getMachineCodeEnabled() {
		return machineCodeEnabled;
	}

	public void setMachineCodeEnabled(Integer machineCodeEnabled) {
		this.machineCodeEnabled = machineCodeEnabled;
	}

	public Integer getMachineCodeTimes() {
		return machineCodeTimes;
	}

	public void setMachineCodeTimes(Integer machineCodeTimes) {
		this.machineCodeTimes = machineCodeTimes;
	}

	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public Double getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
	}

	public Double getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(Double minMoney) {
		this.minMoney = minMoney;
	}

	public ExperienceGoldConfig getExperienceGoldConfig() {
		return experienceGoldConfig;
	}

	public void setExperienceGoldConfig(ExperienceGoldConfig experienceGoldConfig) {
		this.experienceGoldConfig = experienceGoldConfig;
	}

	public String getExperienceGoldInfo(){
		experienceGoldConfig = (ExperienceGoldConfig) operatorService.get(ExperienceGoldConfig.class, id) ;
		return SUCCESS ;
	}
	
public String modifyExperienceGold(){
		ExperienceGoldConfig config = (ExperienceGoldConfig) operatorService.get(ExperienceGoldConfig.class, id);
		if(null == config){
			GsonUtil.GsonObject("error");
		}
		config.setIsUsed(Integer.parseInt(isused));
		config.setUpdateTime(new Date());
		operatorService.update(config);
		GsonUtil.GsonObject("SUCCESS");
		return null ;
	}
	
	
	public String getExperienceGold(){
		DetachedCriteria dc = DetachedCriteria.forClass(ExperienceGoldConfig.class);

		if (StringUtils.isNotEmpty(isused)) {
			dc.add(Restrictions.eq("isUsed", Integer.parseInt(isused)));
		}
		
		if (StringUtils.isNotEmpty(type)) {
			dc.add(Restrictions.eq("title", type));
		}
		
		Order o = null;
		
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
		}else{
			o = Order.desc("createTime");
		}
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createTime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createTime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
        List<ExperienceGoldConfig> list = page.getPageContents();
        if(null != list && list.size()>0){
        	for (ExperienceGoldConfig yyy : list) {
				if (null != yyy.getEndTime()) {
					if(yyy.getEndTime().getTime() < new Date().getTime()){
						yyy.setIsUsed(0);
						operatorService.update(yyy);
					}
				}
			}
        }
  		getRequest().setAttribute("page", page);
		return INPUT;
	}

public String insertExperienceGold() {

		if (StringUtils.isNotBlank(title)) {

			DetachedCriteria dc = DetachedCriteria.forClass(ExperienceGoldConfig.class);
			dc.add(Restrictions.eq("title", title));
			dc.add(Restrictions.eq("platformName", platformName));
			Order o = null;
			Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
			List<ExperienceGoldConfig> list = page.getPageContents();
			if (null != list && list.size() > 0) {
				GsonUtil.GsonObject("该优惠类型已经存在！");
				return null;
			}

			ExperienceGoldConfig config = new ExperienceGoldConfig();
			config.setTitle(title);
			config.setIsUsed(0);
			config.setCreateTime(new Date());
			if (StringUtils.isNotBlank(aliasTitle)) {
				config.setAliasTitle(aliasTitle);
			}
			// 体验金
			config.setAmount(Double.parseDouble(amount));
			config.setStartTime(DateUtil.parseDateForStandard(starttime.replace("+", " ")));
			config.setEndTime(DateUtil.parseDateForStandard(endtime.replace("+", " ")));
			config.setTimes(times);
			config.setTimesFlag(timesflag);
			config.setVip(vip);
			config.setMaxMoney(maxMoney);
			config.setMinMoney(minMoney);
			config.setPlatformName(platformName);
			config.setIsPhone(isPhone);
			config.setMachineCodeEnabled(machineCodeEnabled);
			config.setMachineCodeTimes(machineCodeTimes);
			operatorService.save(config);
			GsonUtil.GsonObject("success");
		} else {
			GsonUtil.GsonObject("参数错误t");
		}
		return null;
	}
	
	public String updateExperienceGold(){
		
		DetachedCriteria dc = DetachedCriteria.forClass(ExperienceGoldConfig.class);
		dc.add(Restrictions.eq("title", title));
		dc.add(Restrictions.eq("platformName", platformName));
		Order o = null;
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<ExperienceGoldConfig> list = page.getPageContents();
		if (null != list && list.size() > 1) {
			GsonUtil.GsonObject("该优惠类型已经存在！");
			return null;
		}
		
		experienceGoldConfig = (ExperienceGoldConfig) operatorService.get(ExperienceGoldConfig.class, id) ;
		experienceGoldConfig.setStartTime(DateUtil.parseDateForStandard(starttime.replace("+", " ")));
		experienceGoldConfig.setEndTime(DateUtil.parseDateForStandard(endtime.replace("+", " ")));
		experienceGoldConfig.setTimes(times);
		experienceGoldConfig.setTimesFlag(timesflag);
		experienceGoldConfig.setVip(vip);
		if("APP下载彩金".equals(experienceGoldConfig.getAliasTitle())&&Double.valueOf(amount)<50){  //50元限制防止恶意修改
			experienceGoldConfig.setAmount(Double.valueOf(amount));			
		} else {
			experienceGoldConfig.setAmount(Double.valueOf(amount));
		}
		experienceGoldConfig.setAliasTitle(aliasTitle);
		experienceGoldConfig.setMachineCodeEnabled(machineCodeEnabled);
		experienceGoldConfig.setMachineCodeTimes(machineCodeTimes);
		experienceGoldConfig.setMaxMoney(maxMoney);
		experienceGoldConfig.setMinMoney(minMoney);
		experienceGoldConfig.setPlatformName(platformName);
		experienceGoldConfig.setIsPhone(isPhone);
		operatorService.update(experienceGoldConfig);
		GsonUtil.GsonObject("修改成功");
		return null ;
	}
	public String getAgentWebsiteRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(AgentAddress.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if (StringUtils.isNotEmpty(address)) {
			dc.add(Restrictions.like("address", address, MatchMode.ANYWHERE));
		}
		if (null != deleteflag) {
			dc.add(Restrictions.eq("deleteflag", deleteflag));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String getHBConfigs(){
		DetachedCriteria dc = DetachedCriteria.forClass(HBConfig.class);

		if (StringUtils.isNotEmpty(isused)) {
			dc.add(Restrictions.eq("isused", Integer.parseInt(isused)));
		}
		if (StringUtils.isNotEmpty(vip)) {
			dc.add(Restrictions.eq("vip",vip));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}

		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<HBConfig> list = page.getPageContents();
		if(null != list && list.size()>0){
			for (HBConfig yyy : list) {
				if(yyy.getEndtime().getTime() < new Date().getTime()){
					yyy.setIsused(0);
					operatorService.update(yyy);
				}
			}
		}
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	//后台user表 电销电话
	public String executeEditRemark(){
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == operator){
			GsonUtil.GsonObject("请先登录");
			return null ;
		}

		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer username = new StringBuffer();
			for (String loginname : loginnames) {
				username.append("'"+loginname+"',");
			}
			String sql = "";
			if (StringUtils.isNotBlank(by)) {
				sql = "update users set warnremark='' where agent='"+by+"' and loginname in ("+username.substring(0, username.length()-1)+")";
			}else if (StringUtils.isNotBlank(intro)) {
				sql = "update users set warnremark='' where intro='"+intro+"' and loginname in ("+username.substring(0, username.length()-1)+")";
			}


			Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createSQLQuery(sql);
			query.executeUpdate();
			operatorService.insertOperatorLog(operator,"操作删除用户备注");
			if(null!=session){
				session.close();
			}
		}

		GsonUtil.GsonObject("提交成功");
		return null;
	}

	public String executeCallDataAnalysis(){
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == operator){
			GsonUtil.GsonObject("请先登录");
			return null ;
		}
		if(StringUtils.isBlank(operator.getCs())&&StringUtils.isBlank(operator.getType())){
			GsonUtil.GsonObject("您的电销标识码为空，找下ivan");
			return null ;
		}
		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer phones = new StringBuffer();
			for (String loginname : loginnames) {
				try {
					
				Users users = (Users) operatorService.get(Users.class,loginname);				
					phones.append("8"+users.getPhone()+";");
				} catch (Exception e) {
					System.out.println("function=executeCallDataAnalysis "+loginname+":解密失败");
				}
				
			}
			phones = new StringBuffer(phones.toString().substring(0, phones.length()-1));
			TelCrmUtil.qunHu(operator.getBlServerUrl(), TelCrmUtil.BL_TM_URl,phones.toString(), operator.getPhonenoBL(), null==ivr?"":ivr,operator.getPhonenoBL());
		}
		GsonUtil.GsonObject("提交成功");
		return null;
	}
	
	public String modifyHBConfig(){
		HBConfig config = (HBConfig) operatorService.get(HBConfig.class, id);
		if(null == config){
			GsonUtil.GsonObject("error");
		}
		config.setIsused(Integer.parseInt(isused));
		config.setUpdatetime(new Date());
		operatorService.update(config);
		GsonUtil.GsonObject("SUCCESS");
		return null ;
	}

	public String getHBConfig(){
		hbConfig  = (HBConfig) operatorService.get(HBConfig.class, id) ;
		return SUCCESS ;
	}

	public void updateHBConfig(){
		hbConfig = (HBConfig) operatorService.get(HBConfig.class, id) ;
		hbConfig.setLimitStartMoney(limitStartMoney);
		hbConfig.setLimitEndMoney(limitEndMoney);
		hbConfig.setStarttime(DateUtil.parseDateForStandard(starttime.replace("+", " ")));
		hbConfig.setEndtime(DateUtil.parseDateForStandard(endtime.replace("+", " ")));
		hbConfig.setTitle(title);
		hbConfig.setTimes(times);
		hbConfig.setBetMultiples(betMultiples);
		hbConfig.setAmount(Double.parseDouble(amount));
		hbConfig.setVip(vip);
		hbConfig.setTimesflag(timesflag);
		operatorService.update(hbConfig);
		GsonUtil.GsonObject("修改成功");
	}

	public Double getLimitStartMoney() {
		return limitStartMoney;
	}

	public void setLimitStartMoney(Double limitStartMoney) {
		this.limitStartMoney = limitStartMoney;
	}

	public Double getLimitEndMoney() {
		return limitEndMoney;
	}

	public void setLimitEndMoney(Double limitEndMoney) {
		this.limitEndMoney = limitEndMoney;
	}

	public void insertHBConfig(){
		if(StringUtils.isBlank(title)){
			GsonUtil.GsonObject("请输入标题");
			return;
		}

		HBConfig config = new HBConfig();
		config.setTitle(title);
		config.setIsused(0);
		config.setCreatetime(new Date());
		config.setUpdatetime(new Date());
		config.setLimitStartMoney(limitStartMoney);
		config.setLimitEndMoney(limitEndMoney);
		config.setBetMultiples(betMultiples);
		config.setType(Integer.valueOf(type));
		config.setAmount(Double.parseDouble(amount));
		config.setStarttime(DateUtil.parseDateForStandard(starttime.replace("+", " ")));
		config.setEndtime(DateUtil.parseDateForStandard(endtime.replace("+", " ")));

		config.setTimes(times);
		config.setTimesflag(timesflag);
		config.setVip(vip);
		operatorService.save(config);
		GsonUtil.GsonObject("创建成功！");
	}
	
	public String getAlipayAccounts(){
		DetachedCriteria dc = DetachedCriteria.forClass(AlipayAccount.class);
		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		if (StringUtils.isNotEmpty(alipayAccount)) {
			dc.add(Restrictions.like("alipayAccount", alipayAccount, MatchMode.ANYWHERE));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String addAlipayAccount(){
		if(StringUtils.isBlank(loginname) || StringUtils.isBlank(alipayAccount)){
			GsonUtil.GsonObject("参数不完整");
			return null;
		}
		Users agent = (Users) operatorService.get(Users.class, loginname);
		if(null == agent || !agent.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())){
			GsonUtil.GsonObject("玩家不存在");
			return null;
		}
		AlipayAccount alipay = null;
		DetachedCriteria dc = DetachedCriteria.forClass(AlipayAccount.class);
		dc.add(Restrictions.eq("alipayAccount", alipayAccount));
		List<AlipayAccount> list = operatorService.findByCriteria(dc);
		if(null != list && list.size()>0 && null != list.get(0)){
			alipay = list.get(0);
		}
		if(null != alipay){
			GsonUtil.GsonObject(alipayAccount+"已被"+alipay.getLoginname()+"使用");
			return null ;
		}
		
		try {
			alipay = new AlipayAccount(alipayAccount, loginname, 0, new Date(),getOperatorLoginname()+";");
			operatorService.save(alipay);
			GsonUtil.GsonObject("添加成功");
			return null ;
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("添加失败:输入数据被占用");
			return null;
		}
	}
	
	public String updateAlipayAccountState(){
		AlipayAccount alipay = null;
		DetachedCriteria dc = DetachedCriteria.forClass(AlipayAccount.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List<AlipayAccount> list = operatorService.findByCriteria(dc);
		if(null != list && list.size()>0 && null != list.get(0)){
			alipay = list.get(0);
			alipay.setUpdatetime(new Date());
			if(alipay.getDisable()==0){
				alipay.setDisable(1);
			}else if(alipay.getDisable()==1){
				alipay.setDisable(0);
			}
			alipay.setRemark(null==alipay.getRemark()?(getOperatorLoginname()+alipay.getDisable()+"upS;"):(alipay.getRemark()+getOperatorLoginname()+alipay.getDisable()+"upS;"));
			operatorService.update(alipay);
		}
		GsonUtil.GsonObject("更新成功");
		return null;
	}
	
	public String addAgentAddress(){
		if(StringUtils.isBlank(loginname) || StringUtils.isBlank(address)){
			GsonUtil.GsonObject("参数不完整");
			return null;
		}
		Users agent = (Users) operatorService.get(Users.class, loginname);
		if(null == agent || !agent.getRole().equals(UserRole.AGENT.getCode())){
			GsonUtil.GsonObject("代理不存在");
			return null;
		}
		AgentAddress agent1 = (AgentAddress) operatorService.get(AgentAddress.class, address);
		if(null != agent1){
			GsonUtil.GsonObject(address+"已被"+agent1.getLoginname()+"使用");
			return null ;
		}
		try {
			AgentAddress agentAddress = new AgentAddress(address, loginname, 0, new Date());
			operatorService.save(agentAddress);
			GsonUtil.GsonObject("添加成功");
			return null ;
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject("添加失败:"+e.getMessage());
			return null;
		}
	}
	
	public String modifyAgentSite() {

		if (StringUtils.isBlank(oldaddress)) {
			GsonUtil.GsonObject("oldaddress 为空");
			return null;
		}
		AgentAddress _agent = (AgentAddress) operatorService.get(AgentAddress.class, address);
		if (null != _agent) {
			GsonUtil.GsonObject(address + "被代理" + _agent.getLoginname() + "占用");
			return null;
		}

		AgentAddress agentAddress = (AgentAddress) operatorService.get(AgentAddress.class, oldaddress);
		if (null == agentAddress) {
			GsonUtil.GsonObject("未找到改地址");
			return null;
		}
		Users agent = (Users) operatorService.get(Users.class, agentAddress.getLoginname());
		if (null == agent || !agent.getRole().equals(UserRole.AGENT.getCode())) {
			GsonUtil.GsonObject("代理账号不存在");
			return null;
		}
		operatorService.delete(AgentAddress.class, oldaddress);
		agentAddress.setAddress(address);
		agentAddress.setCreatetime(new Date());
		operatorService.save(agentAddress);

		agent.setReferWebsite(address);
		operatorService.update(agent);
		GsonUtil.GsonObject("修改成功");
		return null;
	}

	public String getEaList(){
		DetachedCriteria dc = DetachedCriteria.forClass(EaData.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", "E6801_"+loginname));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("date");
//			dc = dc.addOrder(Order.desc("date"));
		}
		if (startPt != null)
			dc = dc.add(Restrictions.ge("starttime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("endtime", endPt));

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "ztze", "zsy" ,null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String getGongHuiConfigs(){
		DetachedCriteria dc = DetachedCriteria.forClass(Guild.class);

		if (StringUtils.isNotEmpty(part)) {
			dc.add(Restrictions.eq("part", part));
		}

		if (StringUtils.isNotEmpty(name)) {
			dc.add(Restrictions.eq("name", name));
		}

		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createTime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}

		/*if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));*/

		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
		List<Guild> list = page.getPageContents();

		getRequest().setAttribute("page", page);
		return INPUT;
	}
	/**
	 * @Description: 批量修改域名
	 * @param @return    参数  
	 * @return String    返回类型  
	 * @date  2017年12月1日下午4:47:15
	 */
	public String batchModifyAgentSite() {
		
		if (StringUtils.isBlank(loginname)) {
			
			GsonUtil.GsonObject("代理账号不能为空！");
			return null;
		}
		
		if (StringUtils.isBlank(address)) {
			
			GsonUtil.GsonObject("新的代理域名前缀不能为空！");
			return null;
		}
		
		Users agent = (Users) operatorService.get(Users.class, loginname);
		if (null == agent) {
		
			GsonUtil.GsonObject("代理账号不存在！");
			return null;
		}
		
		AgentAddress agent1 = (AgentAddress) operatorService.get(AgentAddress.class, address);
		if(null != agent1){
			GsonUtil.GsonObject(address+"已被"+agent1.getLoginname()+"使用");
			return null ;
		}
		
		if (!UserRole.AGENT.getCode().equals(agent.getRole())) {
		
			GsonUtil.GsonObject("【" + loginname + "】不是代理账号！");
			return null;
		}
		
		String sql = "select address,loginname from agent_address where loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("loginname", loginname);
		
		List list = operatorService.list(sql, params);
		
		if (null == list || list.isEmpty()) {
			
			GsonUtil.GsonObject("未找到代理【" + loginname + "】的代理网址！");
			return null;
		}
		
		String deleteSql = "delete from agent_address where loginname = :loginname";
		
		operatorService.executeUpdate(deleteSql, params);
		
		List<AgentAddress> addList = new ArrayList<AgentAddress>();
		String prefix1 = "http://";
		String prefix2 = "http://www.";
		String prefix3 = "https://";
		String prefix4 = "https://www.";
		Date date = new Date();
		String value = "";
		
		for (int i = 0, len = list.size(); i < len; i++) {
		
			Object[] obj = (Object[]) list.get(i);
			String as = String.valueOf(obj[0]);
			String ls = String.valueOf(obj[1]);
			AgentAddress aa = new AgentAddress();
			
			if (as.startsWith(prefix2)) {
				
				value = prefix2 + address + as.substring(as.indexOf(".", prefix2.length()));
			} else if (as.startsWith(prefix1)) {
				
				value = prefix1 + address + as.substring(as.indexOf("."));
			} else{
				value = address;
			}
			
			aa.setAddress(value);
			aa.setLoginname(ls);
			aa.setDeleteflag(0);
			aa.setCreatetime(date);
			
			addList.add(aa);
		}
		
		if (!addList.isEmpty()) {
			
			operatorService.saveAll(addList);
		}
		
		if (StringUtils.isNotBlank(agent.getReferWebsite())) {
			
			if (agent.getReferWebsite().startsWith(prefix4)) {
				
				value = prefix4;
			} else if (agent.getReferWebsite().startsWith(prefix2)) {
				
				value = prefix2;
			} else if (agent.getReferWebsite().startsWith(prefix3)) {
				
				value = prefix3;
			} else if (agent.getReferWebsite().startsWith(prefix1)) {
				
				value = prefix1;
			}
			
			agent.setReferWebsite(value + address + agent.getReferWebsite().substring(agent.getReferWebsite().indexOf(".", value.length())));
			
			operatorService.update(agent);
		}
		
		GsonUtil.GsonObject("修改成功！");
		
		return null;
	}

	public String getAgList(){
		DetachedCriteria dc = DetachedCriteria.forClass(AgData.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", "e_"+loginname));
		}

		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("date");
//			dc = dc.addOrder(Order.desc("date"));
		}
		if (startTime != null)
			dc = dc.add(Restrictions.ge("starttime", startTime));
		if (endTime != null)
			dc = dc.add(Restrictions.le("endtime", endTime));

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "tzje", "yxtze" ,null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String changeStates(){
		Guild guild = (Guild) operatorService.get(Guild.class, id);
		if(null == guild){
			GsonUtil.GsonObject("没有此工会记录");
		}
		guild.setState(state);
		guild.setUpdateTime(new Date());
		operatorService.update(guild);
		GsonUtil.GsonObject("状态切换成功");
		return null ;
	}
	
	public String getPlatformDataList(){
		DetachedCriteria dc = DetachedCriteria.forClass(PlatformData.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if (StringUtils.isNotEmpty(platform)) {
			if(platform.equalsIgnoreCase("gpi_all")){
				dc.add(Restrictions.in("platform", new String[]{"gpi", "rslot", "png", "bs", "ctxm"}));
			}else{
				dc.add(Restrictions.eq("platform", platform));
			}
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("starttime");
//			dc = dc.addOrder(Order.desc("starttime"));
		}		
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("starttime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("endtime", endPt));
		
        Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "bet", "profit" ,null, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String deletePlatformData(){
		PlatformData platformData = (PlatformData) operatorService.get(PlatformData.class, uuid);
		if(null != platformData){
			operatorService.delete(PlatformData.class, uuid);
		}
		return INPUT;
	}
	
	public String updatePlatformData(){
		PlatformData platformData = (PlatformData) operatorService.get(PlatformData.class, uuid);
		if(null != platformData){
			platformData.setBet(bet);
			platformData.setProfit(profit);
			platformData.setUpdatetime(new Date());
			operatorService.update(platformData);
		}
		GsonUtil.GsonObject("更新成功");
		return null;
	}
	
	public String getPlatformData(){
		platformDataxx = (PlatformData) operatorService.get(PlatformData.class, uuid);
		return INPUT;
	}
	
	public String getDepositOrderRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if (StringUtils.isNotEmpty(platform)) {
			dc.add(Restrictions.eq("bankname", platform));
		}
		dc.add(Restrictions.isNull("type"));
		dc.add(Restrictions.isNotNull("spare"));
		if (StringUtils.isNotEmpty(notes)) {
			dc.add(Restrictions.eq("depositId", notes));
		}
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("realname", username));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	public String deleteGongHuiDataAll(){
		DetachedCriteria dc = DetachedCriteria.forClass(GuildStaff.class);
		List<GuildStaff> list = operatorService.findByCriteria(dc);
		operatorService.deleteAll(list);
		GsonUtil.GsonObject("数据清空成功");
		return null ;
	}
	
	public String getTaskList(){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskList.class);
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, 100, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String getUserTaskRecords(){
		Session  session = operatorService.getHibernateTemplate().getSessionFactory().openSession();
		try {
			StringBuffer sbf = new StringBuffer();
			sbf.append("select t.username , t.taskId , t.type , t.historyBet , t.isAdd , t.createtime , t.updatetime , a.amount , t.title from task_user_record t left join task_amount a on t.username = a.loginname ");
			
			if(StringUtils.isNotBlank(intro)){
				String sql = "select u.loginname from users u where u.intro = '" + intro + "'  and u.role='MONEY_CUSTOMER'";
				sbf.append(" inner join ("+sql+") uu on t.username = uu.loginname");
			}
			sbf.append(" where 1=1 ");
			if (StringUtils.isNotEmpty(loginname)) {
				sbf.append(" and t.username = '"+loginname+"'");
			}
			if (null != isAdd) {
				sbf.append(" and t.isAdd = "+isAdd+" ");
			}
			if (StringUtils.isNotEmpty(type)) {
				sbf.append(" and t.type = "+type+"  ");
			}
			if (startPt != null){
				sbf.append(" and t.createtime>='"+DateUtil.formatDateForStandard(startPt)+"'");
			}
			if (endPt != null){
				sbf.append(" and t.createtime<='"+DateUtil.formatDateForStandard(endPt)+"'");
			}
			
			if (StringUtils.isNotEmpty(by)) {
				sbf.append(" order by t."+by+" "+order);
			}else{
				sbf.append(" order by t.createTime desc ");
			}
			
			Query query = session.createSQLQuery(sbf.toString());
			List<TaskUserRecordVo> list = query.list();
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			
			query.setFirstResult((pageIndex.intValue() - 1) * size.intValue());
			query.setMaxResults(size.intValue());
			List<TaskUserRecordVo> contentList = query.list();
			Page page = new Page();
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(list.size());
			int pages = PagenationUtil.computeTotalPages(list.size(), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex.intValue() > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(contentList);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			getRequest().setAttribute("page", page);
		} catch (Exception e) {
			log.error("server error", e);
		} finally {
			session.close();
		}
		return INPUT;
	}
	
	public String changeState(){
		TaskList task = (TaskList) operatorService.get(TaskList.class, id);
		if(null != task){
			if(task.getDisable()==0){
				task.setDisable(1);
			}else if(task.getDisable()==1){
				task.setDisable(0);
			}
			task.setUpdatetime(new Date());
			operatorService.update(task);
			GsonUtil.GsonObject("成功");
		}else{
			GsonUtil.GsonObject("不存在该任务");
		}
		return null ;
	}
	
	public String getBankCard(){
		
		if(StringUtils.isNotBlank(type)){
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc.add(Restrictions.eq("type", 1));  
			dc.add(Restrictions.eq("bankname", type));  
			dc.add(Restrictions.eq("isshow", 1));
			dc.add(Restrictions.eq("useable", 0));
			List<Bankinfo> list = operatorService.findByCriteria(dc);
			
			if(list!=null&&list.size()>0&&list.get(0)!=null){
				GsonUtil.GsonObject(list);
			}else{
				GsonUtil.GsonObject(null);
			}
		}
		return null ;
	}
	
	public String createDepositOrder(){
		try {
			if(StringUtils.isBlank(loginname) || StringUtils.isBlank(bankname) || StringUtils.isBlank(bankcard) || StringUtils.isBlank(realname)|| StringUtils.isBlank(amount)){
				writeText("所有均为必填项！");
				return null;
			}
			Users user=(Users)operatorService.get(Users.class, StringUtil.trim(loginname));
			if(null == user){
				writeText("玩家不存在");
				return null;
			}
			
			
			DepositOrder depositOrder = new DepositOrder();
			depositOrder.setLoginname(loginname.trim());
			depositOrder.setBankname(bankname);
			String depositId = "";
			if(bankname.equals("农业银行")){
				depositId = RandomStringUtils.randomAlphanumeric(4).toLowerCase();
			}else {
				depositId = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
			}
			depositOrder.setDepositId(depositId);
			depositOrder.setCreatetime(new Date());
			depositOrder.setStatus(0);
			depositOrder.setSpare(amount);
			depositOrder.setRemark(getOperatorLoginname()+"创建附言订单");
			depositOrder.setRealname(user.getAccountName().trim());
			depositOrder.setAccountname(realname);
			depositOrder.setBankno(bankcard);
			operatorService.save(depositOrder);
			
			logDao.insertOperationLog(getOperatorLoginname(), OperationLogType.CREATE_DEPOSIT_ORDER, depositId+"--"+loginname+"--"+bankname+"--"+bankcard+"--"+realname);
			
			writeText("创建成功！");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			writeText("创建失败！");
			return null;
		}
		
	}
	
	public String getPreferentialRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(PreferentialRecord.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if (StringUtils.isNotEmpty(platform)) {
			dc.add(Restrictions.eq("platform", platform));
		}
		
		if (StringUtils.isNotEmpty(pno)) {
			dc.add(Restrictions.eq("pno", pno));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String modifyCustomerBet(){
		if(StringUtils.isNotBlank(pno)){
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("pno", pno));
			List<Proposal> list = operatorService.findByCriteria(dc) ;
			if(null == list || list.size() != 1 || null == list.get(0)){
				writeText(pno+"提案有问题");
				return null ;
			}else{
				Proposal proposal = list.get(0) ;
				DetachedCriteria dcPre = DetachedCriteria.forClass(PreferentialRecord.class);
				dcPre = dcPre.add(Restrictions.eq("pno", pno));
				List<PreferentialRecord> listPre = operatorService.findByCriteria(dcPre) ;
				if(null == listPre || 1 != listPre.size() || null == listPre.get(0)){
					writeText("自助优惠有问题。");
					return null ;
				}else{
					PreferentialRecord record = listPre.get(0) ;
					boolean flag = false ;
					if(record.getType()==0){
						record.setType(1);
						flag = true ;
					}else if(record.getType()==1){
						record.setType(0);
					}
					operatorService.update(record);
					
					operatorService.insertSelfYouHuiLog(getOperatorLoginname(), record.getLoginname(), flag, OperationLogType.RELEASE_SELF_YOUHUI, null);
					writeText("执行成功");
					return null ;
				}
			}
		}else {
			writeText("提案号不能为空");
			return null ;
		}
	}
	
	public String getPreferTransferRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(PreferTransferRecord.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		if (StringUtils.isNotEmpty(platform)) {
			dc.add(Restrictions.eq("platform", platform));
		}
		
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}


	public String getBbinList(){
		DetachedCriteria dc = DetachedCriteria.forClass(BbinData.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", "eb"+loginname));
		}

		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("day");
//			dc = dc.addOrder(Order.desc("day"));
		}
		if (startTime != null)
			dc = dc.add(Restrictions.ge("startday", startTime));
		if (endTime != null)
			dc = dc.add(Restrictions.le("endday", endTime));

		Page page = PageQuery.queryForPagenationWithStatistics(operatorService.getHibernateTemplate(), dc, pageIndex, size, "sy", "tze" ,null, o);
		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public String getYouHuiConfigs(){
		DetachedCriteria dc = DetachedCriteria.forClass(YouHuiConfig.class);

		if (StringUtils.isNotEmpty(isused)) {
			dc.add(Restrictions.eq("isused", Integer.parseInt(isused)));
		}
		
		if (StringUtils.isNotEmpty(type)) {
			dc.add(Restrictions.eq("title", type));
		}
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
		if (startPt != null)
			dc = dc.add(Restrictions.ge("createtime", startPt));
		if (endPt != null)
			dc = dc.add(Restrictions.le("createtime", endPt));
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
        List<YouHuiConfig> list = page.getPageContents();
        if(null != list && list.size()>0){
        	for (YouHuiConfig yyy : list) {
				if(yyy.getEndtime() != null && (yyy.getEndtime().getTime() < new Date().getTime())){
					yyy.setIsused(0);
					operatorService.update(yyy);
				}
			}
        }
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public String modifyYouHuiConfig(){
		YouHuiConfig config = (YouHuiConfig) operatorService.get(YouHuiConfig.class, id);
		if(null == config){
			GsonUtil.GsonObject("error");
		}
		config.setIsused(Integer.parseInt(isused));
		config.setUpdatetime(new Date());
		operatorService.update(config);
		GsonUtil.GsonObject("SUCCESS");
		return null ;
	}
	
	public String insertYouHuiConfig(){
		if(StringUtils.isNotBlank(title)){
			YouHuiConfig config = new YouHuiConfig();
			config.setTitle(title);
			config.setIsused(0);
			config.setCreatetime(new Date());
			if(StringUtils.isNotBlank(aliasTitle)){
				config.setAliasTitle(aliasTitle);
			}
			config.setStarttime(DateUtil.parseDateForStandard(starttime));
			config.setEndtime(DateUtil.parseDateForStandard(endtime));
			config.setTimes(times);
			config.setTimesflag(timesflag);
			config.setVip(vip);
			config.setLimitMoney(limitMoney);
			config.setBetMultiples(betMultiples);
			config.setPercent(percent);
			operatorService.save(config);
			GsonUtil.GsonObject("新增成功");
		}else{
			GsonUtil.GsonObject("参数错误t");
		}
		return null ;
	}
	
	public String getConfig(){
		youhuiConfig = (YouHuiConfig) operatorService.get(YouHuiConfig.class, id) ;
		return SUCCESS ;
	}
	
	public String updateYouHuiConfig(){
		youhuiConfig = (YouHuiConfig) operatorService.get(YouHuiConfig.class, id) ;
		youhuiConfig.setStarttime(DateUtil.parseDateForStandard(starttime.replace("+", " ")));
		youhuiConfig.setEndtime(DateUtil.parseDateForStandard(endtime.replace("+", " ")));
		youhuiConfig.setTimes(times);
		youhuiConfig.setTimesflag(timesflag);
		youhuiConfig.setVip(vip);
		if("APP下载彩金".equals(youhuiConfig.getAliasTitle())&&Double.valueOf(amount)<50){  //50元限制防止恶意修改
			youhuiConfig.setAmount(Double.valueOf(amount));			
		}   
		youhuiConfig.setAliasTitle(aliasTitle);
		operatorService.update(youhuiConfig);
		GsonUtil.GsonObject("修改成功");
		return null ;
	}
	
	public String getUserSafeRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(Question.class);

		if (StringUtils.isNotEmpty(loginname)) {
			dc.add(Restrictions.eq("loginname", loginname));
		}
		
		dc.add(Restrictions.eq("delflag", 0));
		Order o = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
//			dc = dc.addOrder(Order.desc("createtime"));
		}
		
        Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
  		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getSignRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		DetachedCriteria dc1 = DetachedCriteria.forClass(SignAmount.class);
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("username", username));
			dc1.add(Restrictions.eq("username", username));
		}
		if (StringUtils.isNotEmpty(type)) {
					dc.add(Restrictions.eq("type", type));
		}
		if (StringUtils.isNotEmpty(isused)&&(!isused.equals("2"))) {
			dc.add(Restrictions.eq("isused", isused));
		}
		if (startTime != null){
			dc = dc.add(Restrictions.ge("createtime", startTime));
			dc1 = dc1.add(Restrictions.ge("updatetime", startTime));
		}
		if (endTime != null){
			dc = dc.add(Restrictions.le("createtime", endTime));
			dc1 = dc1.add(Restrictions.le("updatetime", endTime));
		}
		dc.add(Restrictions.eq("isdelete", "0"));
		Order o = null;
		Order o1 = null;
		if (StringUtils.isNotEmpty(by)) {
			o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
//			dc = dc.addOrder(o);
		}else{
			o = Order.desc("createtime");
			o1 = Order.desc("updatetime");
//			dc = dc.addOrder(Order.desc("createtime"));
//			dc1 = dc.addOrder(Order.desc("updatetime"));
		}
		 Page page=null;
		if("money".equals(type)){//查询的类型是 去查询奖金表
			  page= PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc1, pageIndex, size, o1);
			 List<SignAmount> listSingAmount= page.getPageContents();
			 List<SignRecord> listSignRecord= new ArrayList<SignRecord>();
			 if(null!=listSingAmount&&listSingAmount.size()>0){
				 for(SignAmount sa:listSingAmount){
					 SignRecordAppend sr = new SignRecordAppend();
					 sr.setAmount(sa.getAmountbalane());
					 sr.setUpdatetime(sa.getUpdatetime());
					 sr.setUsername(sa.getUsername());
					 sr.setContinuesigncount(sa.getContinuesigncount());
					 listSignRecord.add(sr);
				 }
			 }
			  page.setPageContents(listSignRecord);
		}else {
			List<SignRecord> content=new ArrayList<SignRecord>();
			  page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, o);
			List<SignRecord> contents = page.getPageContents();
			for (SignRecord s:contents){
				Integer level = operatorService.getFindUser(s.getUsername()).getLevel();
				s.setIsused(getLevelString(level));
				content.add(s);
			}
			page.setPageContents(content);
		}
  		getRequest().setAttribute("page", page);
		return INPUT;
	}

	public static String getLevelString(Integer level){
		switch (level){
			case 0:return "天兵";
			case 1:return "天将";
			case 2:return "天王";
			case 3:return "星君";
			case 4:return "真君";
			case 5:return "仙君";
			case 6:return "帝君";
			case 7:return "天尊";
			case 8:return "天帝";
			default:return "";
		}
	}
	
	public String clearErrorTimes(){
		if (StringUtils.isNotEmpty(loginname)) {
			QuestionStatus status = (QuestionStatus) operatorService.get(QuestionStatus.class, loginname);
			if(null != status){
				status.setErrortimes(0);
				status.setUpdatetime(new Date());
				operatorService.update(status);
			}
			DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
			dc.add(Restrictions.eq("loginname", loginname));
			dc.add(Restrictions.eq("delflag", 0));
			List<Question> questions = operatorService.findByCriteria(dc);
			if(null != questions && questions.size()>0){
				for (Question question : questions) {
					question.setDelflag(1);
					question.setUpdatetime(new Date());
					question.setRemark("后台清除密保绑定记录"+getOperatorLoginname());
					operatorService.update(question);
				}
			}
			operatorService.EnableUser(loginname, true, getOperatorLoginname(), "密保解绑");
			GsonUtil.GsonObject("更新成功");
		}
		return null ;
	}
	
	//后台维护电话
	public String executeCall(){
		log.error("qunhu action entered");
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == operator){
			GsonUtil.GsonObject("请先登录");
			return null ;
		}
		if(StringUtils.isBlank(operator.getCs())){
			GsonUtil.GsonObject("您的维护标识码为空，找下产品经理");
			return null ;
		}
		
		String msg = "提交失败！";
		
		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer phones = new StringBuffer();
			for (String loginname : loginnames) {
				Users user = (Users) operatorService.get(Users.class, loginname);
				phones.append("8"+user.getPhone()+";");
			}
			phones = new StringBuffer(phones.toString().substring(0, phones.length()-1));
			System.out.println("比邻群呼账号：" + operator.getUsername() + "，群呼URL：" + operator.getBlServerUrl() + "，群呼PhonenoBL：" + operator.getPhonenoBL());
			msg = TelCrmUtil.qunHu(operator.getBlServerUrl(),TelCrmUtil.BL_CS_URl, phones.toString(), operator.getPhonenoBL(), null == ivr ? "" : ivr,
					operator.getPhonenoBL());
		}
		GsonUtil.GsonObject(msg);
		return null;
	}
	
	//后台user表 电销电话
	public String executeCallUserTM(){
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == operator){
			GsonUtil.GsonObject("请先登录");
			return null ;
		}
		if(StringUtils.isBlank(operator.getType()) && StringUtils.isBlank(operator.getCs())){
			GsonUtil.GsonObject("您的电销标识码为空，找下ivan");
			return null ;
		}
		
		String msg = "提交失败！";
		
		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer phones = new StringBuffer();
			for (String loginname : loginnames) {
				Users user = (Users) operatorService.get(Users.class, loginname);
				phones.append("8"+user.getPhone()+";");
			}
			phones = new StringBuffer(phones.toString().substring(0, phones.length()-1));
			System.out.println("比邻群呼账号：" + operator.getUsername() + "，群呼URL：" + operator.getBlServerUrl() + "，群呼PhonenoBL：" + operator.getPhonenoBL());
			msg = TelCrmUtil.qunHu(operator.getBlServerUrl(),TelCrmUtil.BL_TM_URl , phones.toString(), operator.getPhonenoBL(), null==ivr?"":ivr,operator.getPhonenoBL());
		}
		GsonUtil.GsonObject(msg);
		return null;
	}
	
	//后台电销电话
	public String executeCallTM(){
		Operator operator = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == operator){
			GsonUtil.GsonObject("请先登录");
			return null ;
		}
		if(StringUtils.isBlank(operator.getType()) && StringUtils.isBlank(operator.getCs())){
			GsonUtil.GsonObject("您的电销标识码为空，找下ivan");
			return null ;
		}
		
		String msg = "提交失败！";
		
		if(StringUtils.isNotBlank(ids)){
			String[] loginnames = ids.split(",");
			if(loginnames.length<1){
				GsonUtil.GsonObject("请选中要执行的条目");
				return null;
			}
			StringBuffer phones = new StringBuffer();
			for (String loginname : loginnames) {
				Customer customer = (Customer) operatorService.get(Customer.class, Integer.parseInt(loginname));
				phones.append("8"+customer.getPhone()+";");
			}
			phones = new StringBuffer(phones.toString().substring(0, phones.length()-1));
			System.out.println("比邻群呼账号：" + operator.getUsername() + "，群呼URL：" + operator.getBlServerUrl() + "，群呼PhonenoBL：" + operator.getPhonenoBL());
			msg = TelCrmUtil.qunHu(operator.getBlServerUrl(),TelCrmUtil.BL_TM_URl, phones.toString(), operator.getPhonenoBL(), null == ivr ? "" : ivr,
					operator.getPhonenoBL());
		}
		GsonUtil.GsonObject(msg);
		return null;
	}
	
	public String synchBbsUserInfo(){
		if(StringUtils.isBlank(loginname)){
			GsonUtil.GsonObject("账号不能为空");
			return null ;
		}
		Users user = (Users) operatorService.get(Users.class, loginname);
		if(null == user){
			GsonUtil.GsonObject("不存在该玩家");
			return null ;
		}
		try {
			String msg = operatorService.synMemberInfo(loginname, user.getPassword());
			GsonUtil.GsonObject(msg);
			return null ;
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(e.getMessage());
			return null ;
		}
	}


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}


	public Date getStartPt() {
		return startPt;
	}

	public void setStartPt(Date startPt) {
		this.startPt = startPt;
	}

	public Date getEndPt() {
		return endPt;
	}

	public void setEndPt(Date endPt) {
		this.endPt = endPt;
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
	
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}


	public OperatorService getOperatorService() {
		return operatorService;
	}


	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}


	public String getPno() {
		return pno;
	}


	public void setPno(String pno) {
		this.pno = pno;
	}


	public String getBankcard() {
		return bankcard;
	}


	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}


	public String getBankname() {
		return bankname;
	}


	public void setBankname(String bankname) {
		this.bankname = bankname;
	}


	public String getRealname() {
		return realname;
	}


	public void setRealname(String realname) {
		this.realname = realname;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public LogDao getLogDao() {
		return logDao;
	}


	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getIsused() {
		return isused;
	}


	public void setIsused(String isused) {
		this.isused = isused;
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


	public Double getPercent() {
		return percent;
	}


	public void setPercent(Double percent) {
		this.percent = percent;
	}


	public Integer getBetMultiples() {
		return betMultiples;
	}


	public void setBetMultiples(Integer betMultiples) {
		this.betMultiples = betMultiples;
	}


	public Double getLimitMoney() {
		return limitMoney;
	}


	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public Date getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}


	public String getStarttime() {
		return starttime;
	}


	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public Integer getTimes() {
		return times;
	}


	public void setTimes(Integer times) {
		this.times = times;
	}


	public Integer getTimesflag() {
		return timesflag;
	}


	public void setTimesflag(Integer timesflag) {
		this.timesflag = timesflag;
	}


	public String getVip() {
		return vip;
	}


	public void setVip(String vip) {
		this.vip = vip;
	}


	public YouHuiConfig getYouhuiConfig() {
		return youhuiConfig;
	}


	public void setYouhuiConfig(YouHuiConfig youhuiConfig) {
		this.youhuiConfig = youhuiConfig;
	}


	public String getAliasTitle() {
		return aliasTitle;
	}


	public void setAliasTitle(String aliasTitle) {
		this.aliasTitle = aliasTitle;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public Double getBet() {
		return bet;
	}


	public void setBet(Double bet) {
		this.bet = bet;
	}


	public Double getProfit() {
		return profit;
	}


	public void setProfit(Double profit) {
		this.profit = profit;
	}



	public PlatformData getPlatformDataxx() {
		return platformDataxx;
	}


	public void setPlatformDataxx(PlatformData platformDataxx) {
		this.platformDataxx = platformDataxx;
	}


	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getIvr() {
		return ivr;
	}


	public void setIvr(String ivr) {
		this.ivr = ivr;
	}


	public Integer getIsAdd() {
		return isAdd;
	}


	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}


	public String getIntro() {
		return intro;
	}


	public void setIntro(String intro) {
		this.intro = intro;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getDeleteflag() {
		return deleteflag;
	}


	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getOldaddress() {

		return oldaddress;
	}

	public void setOldaddress(String oldaddress) {

		this.oldaddress = oldaddress;
	}
	
	private String yesterday;
	
	
	public String getYesterday() {
		return yesterday;
	}

	public void setYesterday(String yesterday) {
		this.yesterday = yesterday;
	}
	
	

	/**
	 * 派发昨日积分
	 */
	public void payYesterdayPoint(){
		String str ="";
		try {
			str = DateUtil.fmtYYYY_MM_DD(DateUtil.getYYYY_MM_DD());
			DetachedCriteria dc = DetachedCriteria.forClass(DetailPoint.class);
			dc = dc.add(Restrictions.eq("createday", str));
			List<DetailPoint> listDt = operatorService.findByCriteria(dc) ;
			if(null!=listDt&&listDt.size()>0){
				GsonUtil.GsonObject("昨日奖金已派发过，不能重复派发");
				return;
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		String begin=DateUtil.getchangedDateStr(-1, str);
		String end=DateUtil.getchangedDateStr(0, str);
		StringBuffer sbfsql =new StringBuffer("select b.money,b.loginname,u.level from (select SUM(a.money) as money,a.loginname as loginname from (select p1.loginname as loginname,  p1.amount as money from proposal p1 where p1.type='502' and p1.flag='2' and p1.createTime>'");
		sbfsql.append(begin+"'  and p1.createTime<'"+end+"' UNION ALL ");
		sbfsql.append("select p.loginname as loginname,p.money as money from payorder p where p.type='0' and p.createTime>'");
		sbfsql.append(begin+"' and p.createTime<'"+end+"' ) a GROUP BY a.loginname) b,users u where b.loginname=u.loginname ");
		try {
			operatorService.payYesterdayPoint(sbfsql.toString());
		} catch (Exception e) {
			GsonUtil.GsonObject("派发失败，请核对是否已派发过！");
		}
		GsonUtil.GsonObject("派发成功");
	}
	
	/**
	 * 查询积分记录
	 * @return
	 */
	public String getPointRecords(){
		DetachedCriteria dc = DetachedCriteria.forClass(DetailPoint.class);
		DetachedCriteria dcTotal = DetachedCriteria.forClass(TotalPoint.class);
		if (StringUtils.isNotEmpty(username)) {
			dc.add(Restrictions.eq("username", username));
			dcTotal.add(Restrictions.eq("username", username));
		}
		if (StringUtils.isNotEmpty(type)) {
			if(!type.equals("2")){
			dc.add(Restrictions.eq("type", type));
			}
		}
		if (StringUtils.isNotEmpty(platform)) {
			dc.add(Restrictions.eq("platform", platform));
			}
		if (startTime != null){
			dc = dc.add(Restrictions.ge("createtime", startTime));
			dcTotal.add(Restrictions.ge("updatetime", startTime));
		}
		if (endTime != null){
			dc = dc.add(Restrictions.le("createtime", endTime));
			dcTotal.add(Restrictions.le("updatetime", endTime));
		}
		/*if (StringUtils.isNotEmpty(by)) {
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc = dc.addOrder(o);
			dcTotal = dcTotal.addOrder(o);
		}else{
			dc = dc.addOrder(Order.desc("createtime"));
			dcTotal = dcTotal.addOrder(Order.desc("updatetime"));
		}*/
		Page page =null;
		if(!type.equals("2")){
         page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
		}else{
		 page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dcTotal, pageIndex, size, null);
		}
  		getRequest().setAttribute("page", page);
		return INPUT;
	}	
	

}
