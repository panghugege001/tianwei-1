package dfh.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.http.params.CoreConnectionPNames;

import sun.security.action.GetBooleanAction;
import dfh.model.PtStatistical;

public class NTUtils {

	//注释内容为测试环境,请勿删除
	static Integer group_id=3464;//787;
	static String startWith="K";
	static String dspurl="http://api5.totemcasino.biz:8383";//"http://api5.totemcasino.biz:8383";//"http://api5.dljj99.com:8383";
	static String skyToken="ntlongdu";//"test";
	static String skySecretkey="afbeHmDw6jxxT6wpStWWl0u4HbjzpOEh";//"202cb962ac59075b964b07152d234b70";

	public static String CreateGameGroup(){ //可用
		String result = "";
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl + "/api/create_group?token=" + skyToken + "&secret_key=" + skySecretkey + "&format=json&currency=8";
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String loginNTGame(String loginname){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl +"/api/open_session?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+startWith+loginname+"&software=netent&balance=0";
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return convertResp(result);
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String getNTMoney(String loginname){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/get_balance?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&software=netent&user_id="+startWith+loginname;
			httpClient = HttpUtils.createHttpClientShort();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				JSONObject json = JSONObject.fromObject(convertResp(result));
				if (json.getBoolean("result")){
					json.put("balance", json.getDouble("balance")/100);
				}
				//NT金额按比例计算,处理余额/100
				return json.toString();
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String balanceClear(String loginname){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/withdraw_balance?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&software=netent&user_id="+startWith+loginname;
			httpClient = HttpUtils.createHttpClientShort();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return convertResp(result);
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String changeMoney(String loginname,Double amount){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		//NT的金额是按比例计算,所有转入转出的金额乘以100
		amount=amount*100;
		Integer iamount = amount.intValue();
		try {
			String url = dspurl+"/api/change_balance?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+startWith+loginname+"&software=netent&amount="+iamount;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return convertResp(result);
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String getEffectiveBets(String loginname, Date startTime,Date endTime){
		SimpleDateFormat yyyyMMdd= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat HH = new SimpleDateFormat("HH");
		SimpleDateFormat mm = new SimpleDateFormat("mm");
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/get_stat?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+startWith+loginname+"&software=netent&date_from="+yyyyMMdd.format(startTime)+"&h_from="+HH.format(startTime)+"&m_from="+mm.format(startTime)+"&date_to="+yyyyMMdd.format(endTime)+"&h_to="+HH.format(endTime)+"&m_to="+mm.format(endTime);
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return convertResp(result);
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String closeSession(String loginname){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/close_session?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+startWith+loginname+"&software=netent&reset_balance=0";
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return convertResp(result);
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String convertResp(String resp){
		JSONObject json = JSONObject.fromObject(resp);
		JSONObject rj = new JSONObject();
		if (null==json.get("error")){
			json.put("result", true);
		} else {
			json.put("result", false);
		}
		return json.toString();
	}
	
	
	public static String getGroupBets(Date startTime, Date endTime){
		SimpleDateFormat yyyyMMdd= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat HH = new SimpleDateFormat("HH");
		SimpleDateFormat mm = new SimpleDateFormat("mm");
		//NT时间为GMT时间,所以减去8小时
		Calendar c=Calendar.getInstance();
		c.setTime(startTime);
		c.add(Calendar.HOUR_OF_DAY, -8);
		startTime=c.getTime();
		c.setTime(endTime);
		c.add(Calendar.HOUR_OF_DAY, -8);
		endTime=c.getTime();
		
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/get_report_by_user?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&software=netent&date_from="+yyyyMMdd.format(startTime)+"&h_from="+HH.format(startTime)+"&m_from="+mm.format(startTime)+"&date_to="+yyyyMMdd.format(endTime)+"&h_to="+HH.format(endTime)+"&m_to="+mm.format(endTime);
			httpClient = HttpUtils.createHttpClient();
			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.socket.timeout", 60000);
			httpClient.setParams(params);
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return convertResp(result);
				//System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		//System.out.println(getEffectiveBets("benny",DateUtil.parseDateForStandard("2015-11-25 00:00:00"), DateUtil.parseDateForStandard("2015-11-25 23:59:59")));
		//Date s=MatchDateUtil.parseDatetime("2015-12-1 00:00:00");
		/*Calendar cs = Calendar.getInstance();
		cs.setTime(s);
		cs.add(Calendar.HOUR_OF_DAY, -8);
		s=cs.getTime();
		System.out.println(MatchDateUtil.formatDatetime(s));*/
		//Date e=MatchDateUtil.parseDatetime("2015-12-1 23:59:59");
		/*Calendar ce = Calendar.getInstance();
		ce.setTime(e);
		ce.add(Calendar.HOUR_OF_DAY, -8);
		e=ce.getTime();
		System.out.println(MatchDateUtil.formatDatetime(e));*/
		//JSONObject groups = JSONObject.fromObject(getEffectiveBets("benny", s, e));
		//System.out.println(groups.toString());
		/*JSONArray bets=groups.getJSONArray("report");
		double sum=0.0;
		for (int i = 0; i < bets.size(); i++) {
			JSONArray o=bets.getJSONArray(i);
			sum+=(o.getDouble(1)/100);
		}
		System.out.println(sum);*/
		Date sdate = MatchDateUtil.parseDatetime("2016-02-15 00:00:00");
		Date edate = MatchDateUtil.parseDatetime("2016-02-15 23:59:59");
		System.out.println(MatchDateUtil.formatDatetime(sdate) +"------------"+ MatchDateUtil.formatDatetime(edate));
		System.out.println(getGroupBets(sdate, edate));
	}
}
