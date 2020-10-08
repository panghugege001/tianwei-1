package com.gsmc.png.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gsmc.png.exception.LiveException;
import com.gsmc.png.exception.LiveNullBodyException;
import com.gsmc.png.model.BBData;
import com.gsmc.png.model.BBinParam;
import com.gsmc.png.model.BbinData;
import com.gsmc.png.response.BBinBetRecordRep;
import com.gsmc.png.response.BBinReqBody;
import com.gsmc.png.response.BBinRespBody;

public class BBinUtils {
	private static Logger log = Logger.getLogger(BBinUtils.class);
	private static String dspurl = "http://linkapi.tcy789.net";
	private static String website = "avia";
	private static String uppername = "dtianweiyule";
	private static String createBetKeyB = "6kqBB1";

	private static  int SLEEP_TIME = 5000;
	private static  int WHILE_COUNT = 3;

	/**
	 * 获取投注记录
	 * @throws Exception
	 */
	public static BBinRespBody<ArrayList<BBinBetRecordRep>>
	getBetRecord(BBinParam param) throws Exception {
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(dspurl);
		reqUrl.append("/app/WebService/JSON/display.php/BetRecord");

		/**
		 * 验证码(需全小写)，组成方式如下:
		 * key=A+B+C(验证码组合方式)
		 * A= 无意义字串长度9码
		 * B=MD5(website+ username + KeyB + YYYYMMDD)
		 * C=无意义字串长度8码
		 * YYYYMMDD为美东时间(GMT‐4)(20160712)
		 */
		String key = StringUtil.getRandomString(1)
				+ EncryptionUtil.encryptPassword(website  + createBetKeyB + getUsEastTime())
				+ StringUtil.getRandomString(8);

		BBinReqBody reqBody = new BBinReqBody();
		reqBody.setWebsite(website);
		reqBody.setUppername(uppername);
		reqBody.setRounddate(param.getRoundDate());
		reqBody.setStarttime(param.getStartTime());
		reqBody.setEndtime(param.getEndTime());
		reqBody.setGamekind(param.getGameKind());
		//reqBody.setGameSubkind(param.getGameSubkind());
		//reqBody.setGametype(param.getGameType());
		reqBody.setPage(1);
		reqBody.setPagelimit(1000);
		reqBody.setKey(key);
		String reqJson = JsonUtil.toJson(reqBody);
		return record(reqUrl.toString(), reqJson);
	}
	
	private static String doPost(String api, String paramsJson) throws Exception {
		String resp = HttpClientUtils.doPost(api, paramsJson);
		if(StringUtils.isEmpty(resp)){
			throw new LiveNullBodyException(api, paramsJson, resp);
		}
		BBinRespBody respBody = JsonUtil.toObject(resp, BBinRespBody.class);
		String dataStr = JsonUtil.toJson(respBody.getData());
		if(respBody.isResult()) {
			return dataStr;
		}
		BBData data = JsonUtil.toObject(dataStr, BBData.class);
		String errorCode = data.getCode();
		throw new LiveException(errorCode, data.getMessage(),api, paramsJson, resp);
	}
	
	public static BBinRespBody<ArrayList<BBinBetRecordRep>> record(String reqUrl, String reqJson)throws Exception{
		String resp = null;
		int count = 0;
		while (true){
			try{
				resp = doPost(reqUrl, reqJson);
				TypeReference<BBinRespBody<ArrayList<BBinBetRecordRep>>> typeRef =
						new TypeReference<BBinRespBody<ArrayList<BBinBetRecordRep>>>(){};
				return JsonUtil.toObject(resp, typeRef);
			}catch (LiveException ex){
				log.error("[BBIN]获取下注记录，异常：", ex);
				throw ex;
			}catch(Exception ex){
				log.error("[BBIN]获取下注记录，异常：", ex);
				count ++;
				if(count >WHILE_COUNT){
					throw ex;
				}
				Thread.sleep(SLEEP_TIME);
			}
		}
	}

	private static String getUsEastTime() {// 美东时间yyyyMMdd
		long time = new Date().getTime() - 12 * 3600 * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}
	public static void main(String[] args) {
		BBinParam param = new BBinParam();
		String startTime = "2020-10-04 18:37:11";
		String endTime = "2020-10-04 18:59:11";
		param.setStartTime(DateUtil.dateToStr(DateUtil.parseDateForStandard(startTime), "HH:mm:ss"));
		param.setRoundDate(DateUtil.dateToStr(DateUtil.parseDateForStandard(startTime), DateUtil.YYYY_MM_DD));
		param.setEndTime(DateUtil.dateToStr(DateUtil.parseDateForStandard(endTime), "HH:mm:ss"));
		try {
			System.out.println(getBetRecord(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
