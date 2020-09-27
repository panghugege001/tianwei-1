package com.gsmc.png.quart.fetch;

public class FetchMwgDataJob {/*

	private static Logger log = Logger.getLogger(FetchMwgDataJob.class);

	private IMWGDataProcessorService mwgService;

	public void execute() {
		for (MWGConfigType enums : MWGConfigType.values()) {
			 String product = enums.getProduct();
			 String siteId = enums.getSiteId();
			 
			 String beginTime = DateUtil.getchangedDate(-1)+" 00:00:00";
			 String endTime = DateUtil.getchangedDate(-1)+" 23:59:59";
			 
			 try {
				 List<MWGData4OracleVO> result= MWGUtils.getbetBySite(siteId,product, beginTime, endTime);
				 if(result != null && result.size() > 0){
					 mwgService.saveAll(result);
				 }
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		  
		} 
		
	}

	public IMWGDataProcessorService getMwgService() {
		return mwgService;
	}

	public void setMwgService(IMWGDataProcessorService mwgService) {
		this.mwgService = mwgService;
	}

	public static void main(String[] args) {
		new FetchMwgDataJob().execute();
	}
*/}
