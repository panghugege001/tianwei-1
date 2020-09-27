package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Proposal;

public interface ProposalService extends UniversalService {

	/*
	 * String addCashout(String proposer, String loginname, String title, String from, Double money, String accountName, String accountNo, String
	 * accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip, String remark, String notifyNote,
	 * String notifyPhone) throws GenericDfhRuntimeException;
	 * 
	 * 
	 * 
	 * String addCashout(String proposer, String loginname, String title, String from, Double money, String accountName, String accountNo, String
	 * accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip, String remark) throws
	 * GenericDfhRuntimeException;
	 * 
	 * String addCashout(String proposer, String loginname, String title, String from, Double money, String ip, String remark) throws
	 * GenericDfhRuntimeException;
	 */
	/**
	 * elf
	 */
	
	String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone, String ip,
			String remark,String msflag) throws GenericDfhRuntimeException;

	String addCashin(String proposer, String loginname, String aliasName, String title, String from, Double money,
			String corpBankName, String remark, String accountNo,String bankaccount,String saveway,String cashintime) throws GenericDfhRuntimeException;

	String addConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark) throws GenericDfhRuntimeException;

	String addBankTransferCons(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark);

	String addNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone, String email,
			String role, String remark);

	String addReBankInfo(String proposer, String loginname, String title, String from, String accountName, String accountNo, String accountType,
			String bank, String accountCity, String bankAddress, String ip, String remark) throws GenericDfhRuntimeException;

	String addXima(String proposer, String loginname, String title, String from, Date startTime, Date endTime, Double firstCash, Double rate,
			String payType, String remark) throws GenericDfhRuntimeException;

	String addPrize(String proposer, String loginname, String title, String from, Double amount, String remark) throws GenericDfhRuntimeException;

	String audit(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;

	String cancle(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;

	/**
	 * web使用
	 * 
	 * @author sun
	 */
	String clientCancle(String pno, String loginname, String ip, String remark) throws GenericDfhRuntimeException;

	String excute(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException;

	/**
	 * office使用
	 */
	String operatorAddNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone,
			String email, String role, String remark, String ipaddress);

	/**
	 * 前台使用
	 * 
	 * @author sun
	 */
	String addUserConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark, String ipaddress) throws GenericDfhRuntimeException;

	/**
	 * 促销优惠
	 */
	String addOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money, String remark)
			throws GenericDfhRuntimeException;

	Proposal getLastSuccCashout(String loginname,Date before);
	
	public Integer totalProposals(Date starttime,Date endtime,String loginname,Integer type,String username);
	
	public Integer totalPayorder(Date starttime,Date endtime,String loginname,String username);
	
	public Integer totalCounts(Date starttime,Date endtime,String loginname,String username);
	
	public List<Proposal> searchSubTotal(Date starttime,Date endtime,String loginname,String username,int pageno,int length);
	
	public List<Proposal> searchSubProposalamount(Date starttime,Date endtime,String loginname,Integer type,String username,int pageno,int length);
	
	public List<Proposal> searchSubPayorderamount(Date starttime,Date endtime,String loginname,String username,int pageno,int length);
	
	public Double totalProposalamount(Date starttime,Date endtime,String loginname,Integer type,String username);
	
	public Double totalPayorderamount(Date starttime,Date endtime,String loginname,String username);
	
	public Double totalamount(Date starttime,Date endtime,String loginname,String username);
	
}