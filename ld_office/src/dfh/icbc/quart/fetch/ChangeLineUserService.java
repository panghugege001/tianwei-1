package dfh.icbc.quart.fetch;

import java.util.Date;
import com.ibm.icu.text.SimpleDateFormat;
import dfh.service.interfaces.IchangeLineUserService;

public class ChangeLineUserService  {

	private  IchangeLineUserService changeLineUserService;
	
	
	public IchangeLineUserService getChangeLineUserService() {
		return changeLineUserService;
	}


	public void setChangeLineUserService(IchangeLineUserService changeLineUserService) {
		this.changeLineUserService = changeLineUserService;
	}

	
	

	public void execute(){
		System.out.println("处理时间开始：" + getCurrentDate() );
		try {
			
			changeLineUserService.changeLineUser();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("处理时间结束：" + getCurrentDate() );
		
	}
	
	
	public String  getCurrentDate() {
		 Date currentTime = new Date();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return formatter.format(currentTime);
	}
	


	
	
}
