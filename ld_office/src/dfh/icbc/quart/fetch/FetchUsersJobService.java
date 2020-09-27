package dfh.icbc.quart.fetch;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.Users;
import dfh.service.interfaces.CustomerService;

public class FetchUsersJobService {
	private Logger log=Logger.getLogger(FetchUsersJobService.class);
	private CustomerService customerService;
	
	/**
	 * 客服的维护的客户分配
	 */
	public void execute(){
		customerService.updateIntroForCS();
	}
	

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	

}
