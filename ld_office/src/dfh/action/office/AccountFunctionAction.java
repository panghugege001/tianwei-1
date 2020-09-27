package dfh.action.office;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nnti.office.model.auth.Operator;

import dfh.action.SubActionSupport;
import dfh.model.ArbitrageData;
import dfh.model.Users;
import dfh.model.UsersBackup;
import dfh.model.commons.Dictionary;
import dfh.service.interfaces.ArbitrageService;
import dfh.service.interfaces.CustomerService;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;
import dfh.utils.HttpUtils;
import dfh.utils.IPSeeker;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import dfh.utils.StringUtil;

//账户功能模块对应action
public class AccountFunctionAction extends SubActionSupport {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AccountFunctionAction.class);
	private static final int BUFFER_SIZE = 16 * 1024;
	private Sheet sheet = null;
	private Workbook wb = null;
	private InputStream stream = null;
	
	private CustomerService customerService;
	private ArbitrageService arbitrageService;
	
	private ArbitrageData arbitrageData;
	private String type;
	private Date regEndTime ;	//注册截止时间
	private Date regStartTime ;//注册起始时间
	private Date lastloginEndTime ;	//最后登录截止时间
	private Date lastloginStartTime ;//最后登录起始时间
	private Date operateEndTime;	//操作截止时间
	private Date operateStartTime;//操作起始时间
	private String errormsg;
	private Integer size;
	private Integer pageIndex;
	private String order;
	private String by;
	
	private File myFile;
	private String myFileFileName;
	private String myFileContentType;
	private String excelFileName;
	

	
	@SuppressWarnings("unchecked")
	public void queryArbitrageShortInfo(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			GsonUtil.GsonObject("请登录");
			return ; 
		}
		String loginname = arbitrageData.getLoginname();
		if(StringUtils.isEmpty(loginname)){
			GsonUtil.GsonObject("账号不能为空");
			return ;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		List<Users> list = customerService.findByCriteria(dc);
		if(list != null && list.size()>0){
			Users user = list.get(0);
			String accountName = user.getAccountName();
			//String phone = user.getPhone();
			//String email = user.getEmail();
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("accountName", accountName);
			//map.put("phone", phone);
			//map.put("email", email);
			
			GsonUtil.GsonObject(map);
		}else{
			DetachedCriteria dc1 = DetachedCriteria.forClass(UsersBackup.class);
			dc1 = dc1.add(Restrictions.eq("loginname", loginname));
			List<UsersBackup> listB = customerService.findByCriteria(dc1);
			if(listB != null && listB.size()>0){
				UsersBackup user = listB.get(0);
				String accountName = user.getAccountName();
				String phone = user.getPhone();
				//String email = user.getEmail();
				
				Map<String,String> map = new HashMap<String,String>();
				map.put("accountName", accountName);
				//map.put("phone", phone);
				//map.put("email", email);
				
				GsonUtil.GsonObject(map);
			}else{
				GsonUtil.GsonObject("账号不存在！");
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	public void queryDictionary(){
		if(StringUtil.isBlank(type)){
			GsonUtil.GsonObject("字典类型不能为空！");
			return;
		}
		String sql = "select dict_name,dict_value,dict_desc,dict_show from dictionary where dict_type =:dict_type and useable = 1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dict_type", type);
		List resultList = customerService.list(sql, params);
		List<Dictionary> returnList = new ArrayList<Dictionary>();
		if(resultList !=null && resultList.size() > 0){
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Dictionary dict = new Dictionary();
				dict.setDictName((String)obj[0]);
				dict.setDictValue((String)obj[1]);
				returnList.add(dict);
			}
		}
		GsonUtil.GsonObject(returnList);
		return;
	}
	
	@SuppressWarnings("unchecked")
	public void addArbitrageInfo(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			GsonUtil.GsonObject("请登录");
			return ; 
		}
		String operator = op.getUsername();
		String loginname = arbitrageData.getLoginname();
		String remark = arbitrageData.getRemark();
		if(StringUtils.isEmpty(loginname)){
			GsonUtil.GsonObject("账号不能为空");
			return ;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		List<Users> list = customerService.findByCriteria(dc);
		if(list != null && list.size()>0){
			Users user = list.get(0);
			String err = addArbitrageInfo(user, type, remark, operator);
			if(err != null){
				GsonUtil.GsonObject(err);
				return;
			}
		}else{
			DetachedCriteria dc1 = DetachedCriteria.forClass(UsersBackup.class);
			dc1 = dc1.add(Restrictions.eq("loginname", loginname));
			List<UsersBackup> listB = customerService.findByCriteria(dc1);
			if(listB != null && listB.size()>0){
				UsersBackup user = listB.get(0);
				String err = addArbitrageInfo(user, type, remark, operator);
				if(err != null){
					GsonUtil.GsonObject(err);
				}
			}else{
				GsonUtil.GsonObject("账号不存在！");
			}
		}
	}
	
	private String addArbitrageInfo(Object u,String arbitrage_project,String remark,String operator){
		String loginname = null;
		String accountName = null;
		String phone = null;
		String email = null;
		String Microchannel = null;
		String RegisterIp = null;
		Timestamp Createtime = null;
		Timestamp LastLoginTime = null;
		String Agent = null;
		
		if(u instanceof Users){
			Users user = (Users)u;
			loginname = user.getLoginname();
			accountName = user.getAccountName();
			phone = user.getPhone();
			email = user.getEmail();
			Microchannel = user.getMicrochannel();
			RegisterIp = user.getRegisterIp();
			Createtime = user.getCreatetime();
			LastLoginTime = user.getLastLoginTime();
			Agent = user.getAgent();
		}
		if(u instanceof UsersBackup){
			UsersBackup user  = (UsersBackup)u;
			loginname = user.getLoginname();
			accountName = user.getAccountName();
			phone = user.getPhone();
			email = user.getEmail();
			Microchannel = user.getMicrochannel();
			RegisterIp = user.getRegisterIp();
			Createtime = user.getCreatetime();
			LastLoginTime = user.getLastLoginTime();
			Agent = user.getAgent();
		}
		ArbitrageData data = new ArbitrageData();
		data.setLoginname(loginname);
		data.setArbitrage_project(arbitrage_project);
		data.setAccount_name(accountName);
		data.setPhone(getphoneInfo(phone));
		data.setEmail(email);
		data.setWechart(Microchannel);
		data.setRegister_ip(getIPInfo(RegisterIp));
		data.setRegister_time(Createtime);
		data.setLogin_ip(getLogin_ip(loginname));
		data.setLogin_time(LastLoginTime);
		data.setPhone_id(getPhone_id(loginname));
		data.setBind_bankcard(getBind_bankcard(loginname)) ;
		data.setDeposit_bankcard(getDeposit_bankcard(loginname));
		data.setAgent(Agent);
		data.setOperator(operator);
		data.setOperate_time(new Date());
		data.setSource("ld");
		data.setRemark(remark);
		try {
			arbitrageService.save(data);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return "插入数据库失败";
		}
		return null;
	}
	
	private String getIPInfo(String ip){
		IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext().getAttribute("ipSeeker");
		String ipinfo = "";
		if(StringUtil.isNotBlank(ip)){
			ipinfo = seeker.getAddress(ip);
		}
		return ip+ipinfo;
	}
	
	
	@SuppressWarnings("rawtypes")
	private String getLogin_ip(String loginname){
		String sql = "select DISTINCT remark from actionlogs where action='LOGIN' and loginname=:loginname order by createtime desc limit 100";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		List resultList = customerService.list(sql, params);
		String result = "";
		List<String> cache = new ArrayList<String>();
		if(resultList !=null && resultList.size() > 0){
			for (int i = 0; i < resultList.size(); i++) {
				String one = (String) resultList.get(i);
				if(one !=null){
					String pre = one.substring(0,16);
					if(!cache.contains(pre)){
						result += ("null".equals(one)?"":one);
						cache.add(pre);
					}
				}
			}
		}
		result = result.replaceAll("最后登录地址：", "").replaceAll("客户操作系统：", "");
		return result;
	}
	
	private String getPhone_id(String loginname){
		String sql = "select CONCAT(sid,' (',os,' ',mobileModel,') ') phone_id from user_sid_name where loginname=:loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		return queryData(sql, params);
	}
	
	private String getBind_bankcard(String loginname){
		String sql = "select CONCAT(bankno,'-',bankname,' ') bind_bankcard from userbankinfo where loginname=:loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		return queryData(sql, params);
	}
	
	private String getDeposit_bankcard(String loginname){
		String sql = "select CONCAT(ubankno,'-',ubankname,'-',uaccountname,' ') deposit_bankcard from deposit_order where type =0 and loginname=:loginname ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		return queryData(sql, params);
	}
	
	@SuppressWarnings("rawtypes")
	private String queryData(String sql,Map<String, Object> params){
		List resultList = customerService.list(sql, params);
		String result = "";
		if(resultList !=null && resultList.size() > 0){
			for (int i = 0; i < resultList.size(); i++) {
				String one = (String) resultList.get(i);
				if(one !=null){
					if("null".equals(one)){
						result += "";
					}else{
						result += "@@"+one;
					}
				}
			}
		}
		return result;
	}
	
	
	private  String getphoneInfo(String phoneNum) {
		String apiUrl = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+phoneNum;
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(apiUrl);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			result = result.substring(result.indexOf("{"),result.length());
			JSONObject obj = JSONObject.fromObject(result);
			String province = obj.getString("province");
			String catName = obj.getString("catName");
			String carrier = obj.getString("carrier");
			return phoneNum+province+" "+catName+" "+carrier;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return "";
	}
	
	public String queryArbitrageDataEdit(){
		Integer id = arbitrageData.getId();
		arbitrageData  =  (ArbitrageData) arbitrageService.get(ArbitrageData.class, id);
		return INPUT ;
	}
	
	public void updateArbitrageData(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			GsonUtil.GsonObject("请登录");
			return ; 
		}
		Integer id = arbitrageData.getId();
		String arbitrage_project = arbitrageData.getArbitrage_project();
		arbitrageData  =  (ArbitrageData) arbitrageService.get(ArbitrageData.class, id);
		arbitrageData.setArbitrage_project(arbitrage_project.replaceAll(",", ""));
		arbitrageData.setOperate_time(new Date());
		arbitrageData.setOperator(op.getUsername());
		arbitrageService.saveOrUpdate(arbitrageData);
		GsonUtil.GsonObject("更新成功");
	}
	
	public void deleteData(){
		Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
		if(null == op){
			GsonUtil.GsonObject("请登录");
			return ; 
		}
		Integer id = arbitrageData.getId();
		arbitrageService.delete(ArbitrageData.class, id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String queryArbitrageInfo(){

		try {
			Operator op = (Operator)this.getRequest().getSession().getAttribute(Constants.SESSION_OPERATORID);
			if(null == op){
				GsonUtil.GsonObject("请登录");
				return null;
			}
			String loginname = arbitrageData.getLoginname();
			String aquery = arbitrageData.getArbitrage_project();
			String account_name = arbitrageData.getAccount_name();
			String phoneInfo = arbitrageData.getPhone();
			String email = arbitrageData.getEmail();
			String wechart = arbitrageData.getWechart();
			String register_ip = arbitrageData.getRegister_ip();
			String login_ip = arbitrageData.getLogin_ip();
			String phone_id = arbitrageData.getPhone_id();
			String bind_bankcard = arbitrageData.getBind_bankcard();
			String deposit_bankcard = arbitrageData.getDeposit_bankcard();
			String agent = arbitrageData.getAgent();
			String source = arbitrageData.getSource();
			
			DetachedCriteria dc=DetachedCriteria.forClass(ArbitrageData.class);
			if(StringUtil.isNotBlank(loginname)){
				dc.add(Restrictions.eq("loginname", loginname));
			}
			if(StringUtil.isNotBlank(aquery)){
				if(!aquery.contains(",")){
					dc.add(Restrictions.like("arbitrage_project", aquery,MatchMode.ANYWHERE));
				}else{
					String[] str = aquery.split(",");
					for (int i = 0; i < str.length; i++) {
						dc.add(Restrictions.like("arbitrage_project", str[i],MatchMode.ANYWHERE));
					}
				}
			}
			
			if(StringUtil.isNotBlank(account_name)){
				dc.add(Restrictions.eq("account_name", account_name));
			}
			if(StringUtil.isNotBlank(phoneInfo)){
				dc.add(Restrictions.like("phone", phoneInfo, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(email)){
				dc.add(Restrictions.like("email", email, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(wechart)){
				dc.add(Restrictions.eq("wechart", wechart));
			}
			if (null != regStartTime) {
				dc.add(Restrictions.ge("register_time", regStartTime));
			}
			if (null != regEndTime) {
				dc.add(Restrictions.lt("register_time", regEndTime));
			}
			if (null != lastloginStartTime) {
				dc.add(Restrictions.ge("login_time", lastloginStartTime));
			}
			if (null != lastloginEndTime) {
				dc.add(Restrictions.lt("login_time", lastloginEndTime));
			}
			if (null != operateStartTime) {
				dc.add(Restrictions.ge("operate_time", operateStartTime));
			}
			if (null != operateEndTime) {
				dc.add(Restrictions.lt("operate_time", operateEndTime));
			}
			
			if(StringUtil.isNotBlank(register_ip)){
				dc.add(Restrictions.like("register_ip", register_ip, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(login_ip)){
				dc.add(Restrictions.like("login_ip", login_ip, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(phone_id)){
				dc.add(Restrictions.like("phone_id", phone_id, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(bind_bankcard)){
				dc.add(Restrictions.like("bind_bankcard", bind_bankcard, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(deposit_bankcard)){
				dc.add(Restrictions.like("deposit_bankcard", deposit_bankcard, MatchMode.ANYWHERE));
			}
			if(StringUtil.isNotBlank(agent)){
				dc.add(Restrictions.eq("agent", agent));
			}
			if(StringUtil.isNotBlank(source)){
				dc.add(Restrictions.eq("source", source));
			}
			
			Order o = null;
			if (StringUtils.isNotEmpty(by)) {
				o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			}else{
				o = Order.desc("operate_time");
			}
			Page  page = PageQuery.queryForPagenation(arbitrageService.getHibernateTemplate(), dc, pageIndex, size, o);
			Map DictionaryMap =  getDictionaryMap();
			List list = page.getPageContents();
			List returnList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				ArbitrageData one = (ArbitrageData) list.get(i);
				String a = one.getArbitrage_project();
				String arbitrage_project = "";
				for (int j = 0; j < a.length(); j++) {
					String b = (String) DictionaryMap.get(a.charAt(j)+"");
					arbitrage_project += (b==null?"":b)+"、";
				}
				if(StringUtil.isBlank(phoneInfo)){
					String p= one.getPhone();
					if(StringUtil.isNotBlank(p)){
						one.setPhone(p.substring(0,1)+"****"+p.substring(10));
					}
				}
				if(StringUtil.isBlank(email)){
					String e = one.getEmail();
					if(StringUtil.isNotBlank(e)){
						one.setEmail(e.substring(0,1)+"****"+e.substring(e.length()-1,e.length()));
					}
				}
				if(StringUtil.isBlank(wechart)){
					String w = one.getWechart();
					if(StringUtil.isNotBlank(w)){
						one.setWechart(w.substring(0,1)+"****"+w.substring(w.length()-1,w.length()));
					}
				}
				
				
				if(!"".equals(arbitrage_project)){
					one.setArbitrage_project(arbitrage_project.substring(0,arbitrage_project.length()-1));
				}
				String login_ipShow = one.getLogin_ip();
				if(StringUtil.isNotBlank(login_ipShow)){
					login_ipShow = login_ipShow.replaceAll("ip:", "</p><p style=\"margin-top: 0px;margin-bottom: 0px;\">ip:");
					one.setLogin_ip("<p style=\"margin-top: 0px;margin-bottom: 0px;\">"+login_ipShow+"</p>");
				}
				String phone_idshow = one.getPhone_id();
				if(StringUtil.isNotBlank(phone_idshow)){
					phone_idshow = phone_idshow.replaceAll("@@", "</p><p style=\"margin-top: 0px;margin-bottom: 0px;\">");
					one.setPhone_id("<p style=\"margin-top: 0px;margin-bottom: 0px;\">"+phone_idshow+"</p>");
				}
				String bind_bankcardshow = one.getBind_bankcard();
				if(StringUtil.isNotBlank(bind_bankcardshow)){
					bind_bankcardshow = bind_bankcardshow.replaceAll("@@", "</p><p style=\"margin-top: 0px;margin-bottom: 0px;\">");
					one.setBind_bankcard("<p style=\"margin-top: 0px;margin-bottom: 0px;\">"+bind_bankcardshow+"</p>");
				}
				String deposit_bankcardShow = one.getDeposit_bankcard();
				if(StringUtil.isNotBlank(deposit_bankcardShow)){
					deposit_bankcardShow = deposit_bankcardShow.replaceAll("@@", "</p><p style=\"margin-top: 0px;margin-bottom: 0px;\">");
					one.setDeposit_bankcard("<p style=\"margin-top: 0px;margin-bottom: 0px;\">"+deposit_bankcardShow+"</p>");
				}
				returnList.add(one);
			}
			page.setPageContents(returnList);
	  		getRequest().setAttribute("page", page);
	  		return INPUT;
		} catch (Exception e) {
			log.error("查询套利数据出错：",e);
			GsonUtil.GsonObject("操作出现异常:" + e.getMessage());
		}
		return null;
	}
	
	public String importArbitrageInfo(){
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
		excelFileName = new Date().getTime() + getExtention(myFileFileName);
		
		File uplodaFiles = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles"));
		
		if (!uplodaFiles.exists()) {
			
			uplodaFiles.mkdir();
		}
		
		File file = new File(ServletActionContext.getServletContext().getRealPath("/UploadFiles") + "/" + excelFileName);

		copy(myFile, file);
		importData(file,operator);
		return INPUT;
	}
	
	public void importData(File file,String operator) {
		StringBuffer errMsg = new StringBuffer(); 
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (wb == null) {
			log.info("打开文件失败");
			setErrormsg("打开文件失败");
			return ;
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		try {
			for (int i = 1; i < rows; i++) {//第一行是标题行
				String loginname=this.getStringValue(i, 0).trim();
				Users user = (Users) this.customerService.get(Users.class,loginname);
				String arbitrage_project = this.getStringValue(i, 1).trim();
				String remark = this.getStringValue(i, 2).trim();
				if (user==null) {
					UsersBackup userb = (UsersBackup) this.customerService.get(UsersBackup.class,loginname);
					if(userb != null){
						String err = addArbitrageInfo(userb, arbitrage_project, remark, operator);
						if(err != null){
							errMsg.append("第"+(i+1)+"行数据"+loginname+err+"！\\n");
						}
					}else{
						log.info("用户："+loginname+"不存在");
						errMsg.append("第"+(i+1)+"行数据"+loginname+"不存在！\\n");
						continue;
					}
				}else{
					String err = addArbitrageInfo(user, arbitrage_project, remark, operator);
					if(err != null){
						errMsg.append("第"+(i+1)+"行数据"+loginname+err+"！\\n");
					}
				}
				
			}
		} catch (Exception e) {
			log.error("EXCEL转换错误：", e);
			setErrormsg("EXCEL转换错误");
			return;
		} finally {
			this.closeFile();
			file.delete();
		}
		if(errMsg.length()>0){
			setErrormsg("导入完毕：\\n"+errMsg.toString());
		}else{
			setErrormsg("数据全部导入成功！");
		}
		return;
	}
	
	public String getStringValue(int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		
		String s = c.getContents();
		if (c.getType() == CellType.LABEL) {
			LabelCell labelc00 = (LabelCell) c;
			s = labelc00.getString();
		}
		
		return s;
	}
	
	public double getNumberValue(int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		
		NumberCell nc = (NumberCell) c;
		double s = nc.getValue();
		if (c.getType() == CellType.NUMBER) {
			NumberCell labelc00 = (NumberCell) c;
			s = labelc00.getValue();
		}
		// System.out.println(s);
		return s;
	}
	
	
	public void closeFile() {
		try {
			wb.close();
			// wb_writeable.close();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件关闭------------>error");
		}
	}
	
	private static void copy(File src, File dst) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}
	
	private Map<Object,Object> getDictionaryMap(){
		String sql = "select dict_name,dict_value from dictionary where dict_type = 'arbitrage_type' and useable = 1";
		return customerService.getResultMap(sql);
	}
	
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public ArbitrageService getArbitrageService() {
		return arbitrageService;
	}

	public void setArbitrageService(ArbitrageService arbitrageService) {
		this.arbitrageService = arbitrageService;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public ArbitrageData getArbitrageData() {
		return arbitrageData;
	}

	public void setArbitrageData(ArbitrageData arbitrageData) {
		this.arbitrageData = arbitrageData;
	}

	public Date getRegEndTime() {
		return regEndTime;
	}

	public void setRegEndTime(Date regEndTime) {
		this.regEndTime = regEndTime;
	}

	public Date getRegStartTime() {
		return regStartTime;
	}

	public void setRegStartTime(Date regStartTime) {
		this.regStartTime = regStartTime;
	}

	public Date getLastloginEndTime() {
		return lastloginEndTime;
	}

	public void setLastloginEndTime(Date lastloginEndTime) {
		this.lastloginEndTime = lastloginEndTime;
	}

	public Date getLastloginStartTime() {
		return lastloginStartTime;
	}

	public void setLastloginStartTime(Date lastloginStartTime) {
		this.lastloginStartTime = lastloginStartTime;
	}

	public Date getOperateEndTime() {
		return operateEndTime;
	}

	public void setOperateEndTime(Date operateEndTime) {
		this.operateEndTime = operateEndTime;
	}

	public Date getOperateStartTime() {
		return operateStartTime;
	}

	public void setOperateStartTime(Date operateStartTime) {
		this.operateStartTime = operateStartTime;
	}
	
	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public String getMyFileContentType() {
		return myFileContentType;
	}

	public void setMyFileContentType(String contentType) {
		this.myFileContentType = contentType;
	}
	
}
