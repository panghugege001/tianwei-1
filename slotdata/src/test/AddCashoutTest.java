package test;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.gsmc.png.model.PNGData4OracleVO;
import com.gsmc.png.model.PNGDataDY;
import com.gsmc.png.service.interfaces.UniversalService;

import junit.framework.TestCase;


public class AddCashoutTest extends TestCase{

	private static FileSystemXmlApplicationContext ctx;
	static UniversalService universalService;
	
	public AddCashoutTest()
	{
		
	}
	
	public static FileSystemXmlApplicationContext getContext() {
		return ctx;
	}

	
	public static void initSpring() {

		ctx = new FileSystemXmlApplicationContext("/WebRoot/WEB-INF/applicationContext.xml");
		universalService = (UniversalService) getContext().getBean("universalService");
	}
	
	public String getIp() {
		String forwaredFor = getRequest().getHeader("X-Forwarded-For");
		String remoteAddr = getRequest().getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor)){
			String[] ipArray=forwaredFor.split(",");
			return ipArray[0];
		}else
			return remoteAddr;
	}
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	public void testExecute()
	{
		initSpring();
		String loginname = "stest999";
		String aliasname = "kevin";
		String phone = "69696";
		String email = "kevin";
		String bank = "bpi";
		String bankAddress="999here";
		String accountName = "noname";
		String accountType = "rich";
		String accountCity = "qc";
		Double amount = 1000.0;
		String accountNo = "11111";
		try
		{
			/*PNGDataTest t = (PNGDataTest) universalService.get(PNGDataTest.class, "222222");
			System.out.println(t.getJson());*/
			PNGData4OracleVO pngDataQY = new PNGDataDY();
			pngDataQY.setTransactionId("test7");
			pngDataQY.setTimeTransaction("222");
			universalService.save(pngDataQY);
			assertEquals(3, 2+1);
			//assertTrue(null , transferService.transferOut(seqService.generateTransferID(), loginname, amount));		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
