package dfh.service.interfaces;

import dfh.model.enums.ProposalType;

public interface SeqService extends UniversalService {

	String generateNetpayBillno();

	String generateTransferID();
	
	String generateLoginID(String loginname);
	
	String generatePt8TransferID(Integer userID);
	
	String generateLoginKenoID(String loginname);
	
	String generateLoginBbinID(String loginname);
	/**
	 * 游客参观ID
	 * @author sun
	 * @return
	 */
	String generateVisitorID();
	
	public String generateHCYmdOrderNoID(String loginname) ;
	
	public String generateAgTryGameID();
	
	public String generateZhiFuOrderNoID(String loginname);
	
	public String generateProposalPno(ProposalType type);
	
	public String generateYhjID();
	
	public String generateZlxID();
	
	public String generateHfOrderNoID();
	
	public String generateHCOrderNoID(String loginname) ;

	public String generateZfbOrderNoID();
	
	public String generateBfbOrderNoID();
	
	public String generateGfbOrderNoID(String loginname);
	
	public String generateWeiXinOrderNoID(); 
	
	public String generateHaierOrderNoID(String loginname);  
	
	public String generateLfWxOrderNoID(String loginname);
	
	public String generateXinBOrderNoID(String loginname) ;
	
	public String generateKdZfOrderNoID(String loginname) ;

	public String generateHhbZfOrderNoID(String loginname);
	
	public String generateKdWxZfOrderNoID(String loginname);
	
	public String generateKdWxZfOrderNoID2(String loginname);
	
	public String generateKdWxZfsOrderNoID(String loginname);
	
	public String generateHhbWxZfOrderNoID(String loginname);
	
	public String generateJubZfbOrderNoID(String loginname);
	
	public String generateXlbOrderNoID(String loginname) ;
	
	public String generateXlbWyOrderNoID(String loginname) ;
	
	public String generateYfZfOrderNoID(String loginname) ;
	
	public String generateYbZfbOrderNoID(String loginname) ;
	
	public String generateXinBZfbOrderNoID(String loginname) ;
	
	public String generateQwZfOrderNoID(String loginname) ;
	
	public String generateXlbZfbOrderNoID(String loginname) ;
}

