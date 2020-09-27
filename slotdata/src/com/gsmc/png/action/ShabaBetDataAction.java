package com.gsmc.png.action;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.gsmc.png.service.interfaces.ISBADataProcessorService;

import edu.emory.mathcs.backport.java.util.Arrays;


public class ShabaBetDataAction extends SubActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(ShabaBetDataAction.class);
	
	private ISBADataProcessorService sbaService;
	public static String APIKEY = "2!@%!sdfJaaShj56SV@AWEx67a";

	private String productdb;
	private String startTime;
	private String endTime;
	private String signature;
	
	
	/**
	 * 发送post请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	public void getSbaXimaData() {
		if (signature != null && !"".equals(signature)) {
			String signatureKey = DigestUtils.md5Hex(productdb + APIKEY);
			if (!signatureKey.equals(signature)) {
				outPut("fail");
				return;
			}
		} else {
			outPut("fail");
			return;
		}
		try {
			List list = sbaService.getSbaXimaData(startTime, endTime, productdb);
			JSONArray result = JSONArray.fromObject(list);
			outPut(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重新获取前一天以后的沙巴投注
	 */
	public void reacquireSBAData() {
		try {
			/*String ip = getIp();
			String[] ips = {"172.16.36.101", "172.16.36.105","172.16.36.106","172.16.35.25","172.16.35.18"};
			System.out.println(ip);
			if(!Arrays.asList(ips).contains(ip)){
				outPut("操作失败：无权进行此操作！");
				return ;
			}*/
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if(hour<15){
				outPut("提示：请您每天的下午3点后操作") ;
				return ;
			}
			int minute = Calendar.getInstance().get(Calendar.MINUTE);
			if((minute >27 && minute < 30) || minute > 57 ){
				outPut("提示：请不要在半点前3分钟执行") ;
				return ;
			}
			sbaService.reacquireSBAData();
			outPut("操作成功，请重新上传反水，在核对！");
		} catch (Exception e) {
			e.printStackTrace();
			outPut("操作失败，请联系技术解决！");
		}
	}
	

	public static <T> T outPut(Object object) {
		PrintWriter out = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html; charset=utf-8"); 
			response.setCharacterEncoding("utf-8");        
            response.setHeader("Cache-Control", "no-cache"); // 取消浏览器缓存
            out = response.getWriter();
            out.print(object);
            out.flush();
			return (T) (new Boolean(true));
		} catch (Exception e) {
			System.out.println(e.toString());
			return (T) (new Boolean(false));
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	

	public String getProductdb() {
		return productdb;
	}

	public void setProductdb(String productdb) {
		this.productdb = productdb;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}

	public ISBADataProcessorService getSbaService() {
		return sbaService;
	}

	public void setSbaService(ISBADataProcessorService sbaService) {
		this.sbaService = sbaService;
	}
	
}
