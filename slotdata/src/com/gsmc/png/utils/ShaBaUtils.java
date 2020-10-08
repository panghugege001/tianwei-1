package com.gsmc.png.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.gsmc.png.model.shaba.SBAData4OracleVO;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ShaBaUtils {

	private static Logger log = Logger.getLogger(ShaBaUtils.class);

	private static final String VENDOR_ID = "l5jo1y1pql";
	private static final String API_URL = "http://tsa.l0030.ig128.com/api/";

	/**
	 * 发送post请求
	 * 
	 * @param method
	 *            api名称
	 * @param parameters
	 *            参数
	 * @return
	 */
	private static String sendPost(String method, String versionKey) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod med = new PostMethod(API_URL + method);
		med.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("vendor_id", VENDOR_ID), new NameValuePair("version_key", versionKey),
				new NameValuePair("options", "") };
		med.setRequestBody(value);
		BufferedReader reader = null;
		try {
			httpClient.executeMethod(med);
			reader = new BufferedReader(new InputStreamReader(med.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = reader.readLine()) != null) {
				stringBuffer.append(str);
			}
			String result = stringBuffer.toString();
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + API_URL + method);
			log.info("响应代码:" + responseCode);
			return result;
		} catch (Exception e) {
			log.error("请求中转服务超时！" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (med != null) {
				med.releaseConnection();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "fail";
	}

	/**
	 * 获取投注明细
	 * 
	 * @param version_key
	 * @param options
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map GetBetDetail(String version_key) {
		String result = sendPost("GetBetDetail", version_key);
		if ("fail".equals(result)) {
			return null;
		}
		Map map = new HashMap();
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			List<SBAData4OracleVO> dataList = new ArrayList<SBAData4OracleVO>();
			if (0 == error_code.intValue()) {
				Object obj = a.get("Data");
				a = JSONObject.fromObject(obj);
				Long last_version_key = Long.parseLong(a.get("last_version_key") + "");
				map.put("last_version_key", last_version_key);
				if (a.get("BetDetails") == null) {
					map.put("BetDetails", dataList);
					return map;
				}
				List list = JSONArray.fromObject(a.get("BetDetails"));
				List listND = JSONArray.fromObject(a.get("BetNumberDetails"));
				List listVSD = JSONArray.fromObject(a.get("BetVirtualSportDetails"));

				String[] strArr = { "HALF WON", "HALF LOSE", "WON", "LOSE" };
				if (list != null && !list.isEmpty()) {
					for (int j = 0; j < list.size(); j++) {
						JSONObject jobj = (JSONObject) list.get(j);
						if (jobj == null || jobj.isNullObject()) {
							continue;
						}
						String ticket_status = jobj.getString("ticket_status");
						if (ticket_status == null) {
							continue;
						}
						if (Arrays.asList(strArr).contains(ticket_status.toUpperCase())) {
							SBAData4OracleVO vo = new SBAData4OracleVO();
							vo.setTrans_id(jobj.getString("trans_id"));
							vo.setVendor_member_id(jobj.getString("vendor_member_id"));
							vo.setOperator_id(jobj.getString("operator_id"));
							vo.setMatch_datetime(DateUtil.getStandardFmtTime(jobj.getString("match_datetime")));
							vo.setStake(jobj.getString("stake"));
							vo.setTransaction_time(DateUtil.getStandardFmtTime(jobj.getString("transaction_time")));
							vo.setTicket_status(ticket_status);
							vo.setWinlost_amount(jobj.getString("winlost_amount"));
							vo.setAfter_amount(jobj.getString("after_amount"));
							vo.setCurrency(jobj.getString("currency"));
							vo.setWinlost_datetime(DateUtil.getStandardFmtTime(jobj.getString("winlost_datetime")));
							vo.setOdds_type(jobj.getString("odds_type"));
							vo.setVersion_key(jobj.getString("version_key"));
							dataList.add(vo);
						}
					}
				}
				if (listND != null && !listND.isEmpty()) {
					for (int j = 0; j < listND.size(); j++) {
						JSONObject jobj = (JSONObject) listND.get(j);
						if (jobj == null || jobj.isNullObject()) {
							continue;
						}
						String ticket_status = jobj.getString("ticket_status");
						if (ticket_status == null) {
							continue;
						}
						if (Arrays.asList(strArr).contains(ticket_status.toUpperCase())) {
							SBAData4OracleVO vo = new SBAData4OracleVO();
							vo.setTrans_id(jobj.getString("trans_id"));
							vo.setVendor_member_id(jobj.getString("vendor_member_id"));
							vo.setOperator_id(jobj.getString("operator_id"));
							vo.setMatch_datetime(null);
							vo.setStake(jobj.getString("stake"));
							vo.setTransaction_time(DateUtil.getStandardFmtTime(jobj.getString("transaction_time")));
							vo.setTicket_status(ticket_status);
							vo.setWinlost_amount(jobj.getString("winlost_amount"));
							vo.setAfter_amount(jobj.getString("after_amount"));
							vo.setCurrency(jobj.getString("currency"));
							vo.setWinlost_datetime(DateUtil.getStandardFmtTime(jobj.getString("winlost_datetime")));
							vo.setOdds_type(jobj.getString("odds_type"));
							vo.setVersion_key(jobj.getString("version_key"));
							dataList.add(vo);
						}
					}
				}
				if (listVSD != null && !listVSD.isEmpty()) {
					for (int j = 0; j < listVSD.size(); j++) {
						JSONObject jobj = (JSONObject) listVSD.get(j);
						if (jobj == null || jobj.isNullObject()) {
							continue;
						}
						String ticket_status = jobj.getString("ticket_status");
						if (ticket_status == null) {
							continue;
						}
						if (Arrays.asList(strArr).contains(ticket_status.toUpperCase())) {
							SBAData4OracleVO vo = new SBAData4OracleVO();
							vo.setTrans_id(jobj.getString("trans_id"));
							vo.setVendor_member_id(jobj.getString("vendor_member_id"));
							vo.setOperator_id(jobj.getString("operator_id"));
							vo.setMatch_datetime(null);
							vo.setStake(jobj.getString("stake"));
							vo.setTransaction_time(DateUtil.getStandardFmtTime(jobj.getString("transaction_time")));
							vo.setTicket_status(ticket_status);
							vo.setWinlost_amount(jobj.getString("winlost_amount"));
							vo.setAfter_amount(jobj.getString("after_amount"));
							vo.setCurrency(jobj.getString("currency"));
							vo.setWinlost_datetime(DateUtil.getStandardFmtTime(jobj.getString("winlost_datetime")));
							vo.setOdds_type(jobj.getString("odds_type"));
							vo.setVersion_key(jobj.getString("version_key"));
							dataList.add(vo);
						}
					}
				}
			}
			map.put("BetDetails", dataList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(GetBetDetail("0"));
		// sendPost("", "");
	}
}
