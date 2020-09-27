package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.model.Payorder;
import dfh.model.Proposal;

public interface NotifyService extends UniversalService {

	String sendEmail(String email, String title, String msg);

	String sendSms(String phoneNo, String msg);
	
	String sendSmsNew(String phoneNo, String msg);
	
	String sendSmsList(String msg,String type,Date start,Date end);
	
	public String sendSmsList45(String msg, List phones, Date start, Date end);
	
	String sendSmsListByLevel(String msg,Integer level,Date start,Date end); 
	
	public String sendSMSByProposal(Proposal proposal);

	public String sendSMSByPayorder(Payorder payorder);

	public String sendSMSByBillno(String billno, String string);

}