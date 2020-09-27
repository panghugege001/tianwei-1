package dfh.service.interfaces;

public interface SeqService extends UniversalService {

	String generateNetpayBillno();

	String generateTransferID();
	
	String generateLoginID(String loginname);
	
	String generateLoginKenoID(String loginname);
	
	String generateLoginBbinID(String loginname);
	/**
	 * 游客参观ID
	 * @author sun
	 * @return
	 */
	String generateVisitorID();

}