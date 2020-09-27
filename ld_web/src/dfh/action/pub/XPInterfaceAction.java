package dfh.action.pub;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.service.interfaces.CustomerService;

public class XPInterfaceAction extends SubActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(XPInterfaceAction.class);
	
	private CustomerService customerService;
	private String cmd;
	private String loginname;
	private String password;
	
	@Override
	public String execute() throws Exception {
		log.info("receive request for command:"+cmd);
		
		return SUCCESS;
	}

	public String getCmd() {
		return cmd;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public String getLoginname() {
		return loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
