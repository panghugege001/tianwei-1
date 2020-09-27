package dfh.action.office;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.OperatorService;
import dfh.utils.NumericUtil;

public class AjaxQueryAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(AjaxQueryAction.class);
	private OperatorService operatorService;
	private CustomerService customerService;

	private String loginname;
	private Date start;
	private Date end;

	public AjaxQueryAction() {
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public Date getEnd() {
		return end;
	}

	public String getLoginname() {
		return loginname;
	}

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public Date getStart() {
		return start;
	}

	public String queryLastXimaTime() {
		String msg = "";
		try {
			msg = customerService.queryUserLastXimaEndTime(loginname);
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String queryRemoteCredit() {
		String msg = "";
		try {
			msg = NumericUtil.formatDouble(customerService.getRemoteCredit(loginname));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String queryValidBetAmount() {
		String msg = "";
		try {
			msg = NumericUtil.formatDouble(customerService.queryValidBetAmount(loginname, start, end));
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public void setStart(Date start) {
		this.start = start;
	}

}
