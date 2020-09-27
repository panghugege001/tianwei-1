package dfh.service.interfaces;

import java.util.List;

import dfh.model.Commissionrecords;
import dfh.model.Users;

public interface AgentService extends CustomerService {

  void excuteCommisionRecords(String loginname,Integer year,Integer month);
	
	

}