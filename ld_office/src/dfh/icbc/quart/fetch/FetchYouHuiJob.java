package dfh.icbc.quart.fetch;

import org.apache.log4j.Logger;

import dfh.service.interfaces.ProposalService;

public class FetchYouHuiJob {
	private static Logger log = Logger.getLogger(FetchYouHuiJob.class);
	private ProposalService proposalService ;
	
	public void execute(){
		try {
			proposalService.dealSelfYouHuiData();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}
	

}
