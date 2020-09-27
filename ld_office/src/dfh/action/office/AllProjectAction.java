package dfh.action.office;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionSupport;

import dfh.model.QqInfFromNet;
import dfh.model.Users;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.AllProjectService;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.SlaveService;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.MatchDateUtil;
import dfh.utils.Page;

/**
 * 1,该action只处理各种链接其他平台的单独功能
 * 2,待增加
 * 
 * @author chaoren 
 */
public class AllProjectAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(AllProjectAction.class); 
	private CustomerService customerService;
	private AllProjectService allProjectService;
	
	private Integer size;
	private Integer pageIndex;
	private String queryTime_s;
	private String queryTime_e;
	private String by;
	private String order;
	
	private QqInfFromNet qif;
	
	/**
	 * 处理.net传递过来的qq以及关联参数
	 * @return
	 */
	public String forAboutQq(){
		try {
			/* 获取参数并判断,参数不为空则直接返回成功 */
			Gson g = new Gson();
			JsonParser parser = new JsonParser();
			HttpServletRequest request = this.getRequest();
			Map<String, String[]> pars = request.getParameterMap();
			if (null==pars || pars.isEmpty()){
				GsonUtil.GsonObject("失败,参数为空,处理失败");
				return null;
			}
			JsonObject jo = parser.parse(pars.get("data")[0]).getAsJsonObject();
			JsonArray ja = jo.get("data").getAsJsonArray();
			if (null==ja || ja.size()==0){
				GsonUtil.GsonObject("失败,接收到的数据转换失败");
			} else {
				GsonUtil.GsonObject("成功,接收数据成功");
			}
			List<String> exsitqq = new ArrayList<String>();
			for (int i=0; i<ja.size(); i++) {
				JsonObject join = ja.get(i).getAsJsonObject();
				List<Object[]> uls = customerService.queryUserByQQ(join.get("number").getAsString());
				if (null==uls || uls.isEmpty()){
					if (exsitqq.contains(join.get("number").getAsString())) { //该qq号码已经记录过
						continue;
					}
					QqInfFromNet qif = new QqInfFromNet();
					qif.setCreateTime(new Date());
					qif.setBetSum(0.0);
					qif.setDepositSum(0.0);
					qif.setWin_los(0.0);
					qif.setQq(join.get("number").getAsString());
					qif.setFromCsQq(join.get("sqq").getAsString());
					qif.setCollectTime(DateUtil.parseDateForStandard(join.get("createtime").getAsString()));
					qif.setLastVisitTime(DateUtil.parseDateForStandard(join.get("updatetime").getAsString()));
					boolean sou = allProjectService.saveOrUpdateQq(qif);
					if (sou)
						exsitqq.add(join.get("number").getAsString());
				} else {
					for (Object[] obj : uls) {
						Map<String, Double> bill = allProjectService.queryUserBill(obj[0].toString());
						String loginname = obj[0].toString();
						if (UserRole.AGENT.getCode().equals(obj[2])){ /*如果是代理账号跳过循环*/
							continue;
						}
						QqInfFromNet qif = new QqInfFromNet();
						qif.setLoginname(loginname);
						qif.setAgentname(obj[1]==null?null:obj[1].toString());
						qif.setCreateTime(new Date());
						qif.setBetSum(bill.get("betSum"));
						qif.setDepositSum(bill.get("depositSum"));
						qif.setWin_los(bill.get("win_los"));
						qif.setQq(join.get("number").getAsString());
						qif.setEmail(obj[3]==null?null:obj[3].toString());
						qif.setFromCsQq(join.get("sqq").getAsString());
						qif.setCollectTime(DateUtil.parseDateForStandard(join.get("createtime").getAsString()));
						qif.setLastVisitTime(DateUtil.parseDateForStandard(join.get("updatetime").getAsString()));
						boolean sou = allProjectService.saveOrUpdateQq(qif);
					}
				}
			}
			
		} catch (Exception e) {
			log.error("forAboutQq error :",e);
		}
		
		return null;
	}
	
	/**
	 * 查询.net传递过来的QQ以及关联信息
	 * @param 存储在request
	 * @return 具体页面
	 */
	public String queryQQInf(){
		try {
			DetachedCriteria cri = DetachedCriteria.forClass(QqInfFromNet.class);
			if (StringUtils.isNotEmpty(qif.getQq()))
				cri.add(Restrictions.eq("qq", qif.getQq()));
			if (StringUtils.isNotEmpty(qif.getFromCsQq()))
				cri.add(Restrictions.eq("fromCsQq", qif.getFromCsQq()));
			if (StringUtils.isNotEmpty(qif.getLoginname()))
				cri.add(Restrictions.eq("loginname", qif.getLoginname()));
			if (StringUtils.isNotEmpty(queryTime_e))
				cri.add(Restrictions.ge("collectTime", MatchDateUtil.parseDatetime(queryTime_s)));
			if (StringUtils.isNotEmpty(queryTime_e))
				cri.add(Restrictions.le("collectTime", MatchDateUtil.parseDatetime(queryTime_e)));
			if (StringUtils.isNotEmpty(by)){
				if (order.equals("desc"))
					cri.addOrder(Order.desc(by));
				else
					cri.addOrder(Order.asc(by));
			} else {
				cri.addOrder(Order.desc("collectTime"));
			}
			Page page = allProjectService.queryQQInf(cri, size, pageIndex);
			
			getRequest().setAttribute("page", page);
			//getRequest().setAttribute("qif", qif);
		} catch (Exception e) {
			log.error("queryQQInf error:",e);
			getRequest().setAttribute("errorMsg", "查询qq信息错误,请联系技术");
		}
		return SUCCESS;
	}
	
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	public void setAllProjectService(AllProjectService allProjectService) {
		this.allProjectService = allProjectService;
	}
	public void setQif(QqInfFromNet qif) {
		this.qif = qif;
	}
	public QqInfFromNet getQif() {
		return qif;
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
	public String getQueryTime_s() {
		return queryTime_s;
	}
	public void setQueryTime_s(String queryTime_s) {
		this.queryTime_s = queryTime_s;
	}
	public String getQueryTime_e() {
		return queryTime_e;
	}
	public void setQueryTime_e(String queryTime_e) {
		this.queryTime_e = queryTime_e;
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
}
