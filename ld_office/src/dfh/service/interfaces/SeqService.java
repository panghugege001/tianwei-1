package dfh.service.interfaces;

public interface SeqService extends UniversalService {

	String generateNetpayBillno();

	String generateTransferID();
	/**
	 * 游客参观ID
	 * @author sun
	 * @return
	 */
	String generateVisitorID();

}