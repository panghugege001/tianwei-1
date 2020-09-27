package dfh.action.bankinfo;

import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.BankStatus;
import dfh.service.interfaces.BankStatusService;

public class BankStatusAction extends SubActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3680631790132908491L;
	private static Logger logger = Logger.getLogger(BankStatusAction.class);
	
	
	private BankStatusService bankStatusService;
	private String status;
	private Integer id;
	
	public BankStatusService getBankStatusService() {
		return bankStatusService;
	}

	public void setBankStatusService(BankStatusService bankStatusService) {
		this.bankStatusService = bankStatusService;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * get bank status list
	 * @return
	 */
	public String getBankStatusList(){
		List<BankStatus> bankStatusList = bankStatusService.getBankStatusList();
		getRequest().setAttribute("list", bankStatusList);
		return INPUT;
	}
	
	public void updateBankStatus(){
		try {
			bankStatusService.updateBankStatus(id, status);
			writeText("SUCCESS");
		}catch(Exception e) {
			logger.error("修改提款银行状态失败", e);
			writeText("ERROR");
		}
	}
	
}
