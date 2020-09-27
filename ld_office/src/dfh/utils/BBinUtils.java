package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import dfh.security.EncryptionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class BBinUtils {

	private static Logger log = Logger.getLogger(BBinUtils.class);

	private static String  dspurl="http://linkapi.longdu610.com";
	private static String  website="avia";
	private static String  uppername="dlongdu";
	private static String  checkUsrBalanceKeyB="7pxyd9c0a";
	private static String  TransferRecordKeyB="5Jr57Ya8c7";
	private static String  prefix="k";

	/**
	 * 发送post请求
	 * @param url
	 * @return
	 */
	private static String sendPost(String url) {

		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod med = new PostMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		try {
			httpClient.executeMethod(med);
			String result = med.getResponseBodyAsString();
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			if(responseCode != 200) {
				return null;
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			log.info(""+e.getMessage());
		}
		return null;
	}
	public static List TransferRecord(String loginname, String transtype, String starttime, String endtime, Integer page){
		String[] sarr = starttime.split(" ");
		String date_start = sarr[0];
		String start_hhmmss = sarr[1];

		String[] earr = endtime.split(" ");
		String date_end = earr[0];
		String end_hhmmss = earr[1];

		String key = StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website +prefix+loginname
				+ TransferRecordKeyB + getUsEastTime())
				+ StringUtil.getRandomString(8);

		String params = "?website=" + website + "&username=" +prefix+loginname
				+ "&uppername=" + uppername +"&transtype=" + transtype
				+"&date_start=" + date_start +"&date_end=" + date_end
				+"&start_hhmmss=" + start_hhmmss +"&end_hhmmss=" + end_hhmmss
				+"&page="+page+"&key=" + key;
		String url=dspurl+"/app/WebService/JSON/display.php/TransferRecord"+params;
		String result = sendPost(url);
		if(result == null) {
			return null;
		}
		try {
			JSONObject a = JSONObject.fromObject(result);
			Boolean flag = a.getBoolean("result");
			if(flag) {
				List data = a.getJSONArray("data");
				JSONObject pagination = (JSONObject) a.get("pagination");
				Integer TotalPage = (Integer) pagination.get("TotalPage");
				if(TotalPage > page) {
					List seList = TransferRecord(loginname, transtype, starttime, endtime, page+1);
					if(seList != null) {
						data.add(seList);
					}
				}
				return data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Double GetBalance(String loginname){
		String key = StringUtil.getRandomString(4)
				+ EncryptionUtil.encryptPassword(website +prefix+loginname
				+ checkUsrBalanceKeyB + getUsEastTime())
				+ StringUtil.getRandomString(7);

		String params = "?website=" + website + "&username=" +prefix+loginname
				+ "&uppername=" + uppername +"&key=" + key;
		String url=dspurl+"/app/WebService/JSON/display.php/CheckUsrBalance"+params;
		String result = sendPost(url);
		if(result == null) {
			return 0.0;
		}
		try {
			JSONObject a = JSONObject.fromObject(result);
			Boolean flag = a.getBoolean("result");
			if(flag) {
				JSONArray data = a.getJSONArray("data");
				JSONObject b = (JSONObject) data.get(0);
				return Double.valueOf(b.get("Balance")+"");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getUsEastTime(){//美东时间yyyyMMdd
		long time = new Date().getTime()-12*3600*1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}

	//MWG_DATA_777,MWG_DATA_E68,MWG_DATA_HU,MWG_DATA_L8,MWG_DATA_LD,MWG_DATA_MZC
	//MWG_DATA_QLE,MWG_DATA_QY,MWG_DATA_UFA,MWG_DATA_ULE,MWG_DATA_WS,MWG_DATA_ZB
	private static final String PRODUCTDB = "BBIN_DATA_LD";
	private static final String DATAURL = "http://pngback.rocks/bbindata/";
	private static final String APIKEY = "2!@%!sdfJafadf6SV@AWEx67a";

	//调用pngdatafeed工程获取洗码数据
	public static String getBbinXimaData(String startTime,String endTime,String gamekind){
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(DATAURL+"getBbinXimaData.do");
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("productdb", PRODUCTDB);
		method.setParameter("startTime", startTime);
		method.setParameter("endTime", endTime);
		method.setParameter("gamekind", gamekind);
		String signatureKey = DigestUtils.md5Hex(PRODUCTDB + APIKEY);
		method.setParameter("signature", signatureKey);
		BufferedReader reader = null;
		try {
			httpClient.executeMethod(method);

			reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while((str = reader.readLine())!=null){
				stringBuffer.append(str);
			}
			String result = stringBuffer.toString();
			if("fail".equals(result)){
				log.error("调用获取投注额服务加密异常！");
				return null;
			}
			reader.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	//调用pngdatafeed工程获取游戏数据
	public static List queryBbinData(String loginname,String startTime,String endTime,String gamekind,String serialID){
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(DATAURL+"queryBbinData.do");
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("productdb", PRODUCTDB);
		method.setParameter("loginname", prefix+loginname);
		method.setParameter("startTime", startTime);
		method.setParameter("endTime", endTime);
		method.setParameter("gamekind", gamekind);
		method.setParameter("serialID", serialID);
		String signatureKey = DigestUtils.md5Hex(PRODUCTDB + APIKEY);
		method.setParameter("signature", signatureKey);
		BufferedReader reader = null;
		try {
			httpClient.executeMethod(method);

			reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while((str = reader.readLine())!=null){
				stringBuffer.append(str);
			}
			String result = stringBuffer.toString();
			if("fail".equals(result)){
				log.error("调用获取游戏记录服务加密异常！");
				return null;
			}
			reader.close();
			return JSONArray.fromObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String loginname = "devtest888";
		System.out.println(GetBalance(loginname));

		System.out.println(getBbinXimaData("2018-05-22 00:00:00", "2018-05-22 23:59:59", "3"));
	}
}
