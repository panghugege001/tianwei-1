package dfh.action.office;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.notdb.DataImportBL;
import dfh.service.interfaces.OperatorService;
import dfh.utils.GsonUtil;

public class BLAction extends SubActionSupport {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BLAction.class);
	
	private OperatorService operatorService;
	private String phones;
	private Date start;
	private Date end;
	private String intro;
	private String isdeposit;
	private Integer level;
	private String agent;
	private String by;
	private String order;
	private Integer size;
	private Integer pageIndex;
	private String errormsg;
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String queryImportData(){

		try {
			if(start==null || end==null){
				setErrormsg("时间不允许为空!");
				return INPUT;
			}
			
			List<DataImportBL> resultList = operatorService.queryPreImportData(start, end,intro,isdeposit,level, agent,order, by,state);
			getRequest().setAttribute("resultList", resultList);
		} catch (Exception e) {
			log.info(""+e.getMessage());
			e.printStackTrace();
			setErrormsg("返回消息:" + e.getMessage());
		}
		return INPUT;
	}
	
	public void importData(){
		if(StringUtils.isBlank(phones)){
			GsonUtil.GsonObject("没有可导入的数据!");
			return;
		}
		String result = operatorService.importData(phones);
		GsonUtil.GsonObject(result);
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		BLAction.log = log;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
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

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIsdeposit() {
		return isdeposit;
	}

	public void setIsdeposit(String isdeposit) {
		this.isdeposit = isdeposit;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}
}
