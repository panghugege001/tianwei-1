package com.gsmc.png.spider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gsmc.png.utils.DateUtil;

/**
 * 对于代理老虎机日结佣金，扣除奖池部分而单独抓取奖池progressive wins数据
 * 对应方法： dfh.action.office.OfficeAction.updatePTSlotDataOfYesterday()
 * 
 * @author Pony
 *
 */
public class PTSpider2 {

	private static Logger log = Logger.getLogger(PTSpider2.class);

	public static final String platform_data = "platform_data";   //实时老虎机流水 platform_data
	public static final String pt_data_new = "pt_data_new";     //PT反水数据，老虎机和累计奖池老虎机 pt_data_new

	public static final String username = "DY8ADMIN1";
	public static final String password = "QAZqazQAZ";
	public static final String LOGIN_URL = "https://kiosk.kd-games.com/login.php";
	public static final String REPORT_URL = "https://kiosk.kd-games.com/game_stats_report.php";

	private static Map<String, String> cookies = null;

	static{
		try {
			login();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PT扫描程序登入失败");
		}
	}

	static void login() throws IOException, ParseException{
		Map<String, String> loginParam = new HashMap<String, String>();
		loginParam.put("username", username);
		loginParam.put("password", password);
		loginParam.put("Submit", "Login");
		Response response = Jsoup.connect(LOGIN_URL)
				.data(loginParam)
				.method(Method.POST)
				.timeout(20000)
				.execute();
		//System.out.println(response.statusCode());
		if (response.statusCode() == 200) {
			cookies = response.cookies();
		}else {
			cookies = null;
		}
	}

	public static void writ2File(String txt) {
		try {
			//Whatever the file path is.
			File statText = new File("D:/PT/ptSpider.html");
			if(!statText.exists()){
				statText.mkdirs();
			}
			FileOutputStream is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			w.write(txt);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Problem writing to the file statsTest.txt");
		}
	}

	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		/*login();
		Thread.sleep(20000);
		spider();*/
	}

	public static List<PTBetVO> spider(Date date, String type) throws IOException, ParseException{
		log.info("爬取PT后台投注数据[" + type + "]>>>>>>>>>>>>>>>>>>>>");

		Map<String, String> queryParam = new HashMap<String, String>();
		queryParam.put("submit", "Show stats");
		queryParam.put("game_type", "regular");
		
		//queryParam.put("period", date);
		queryParam.put("startdate", DateUtil.fmtYYYY_MM_DD(date));
		queryParam.put("enddate", DateUtil.fmtYYYY_MM_DD(date));
		queryParam.put("start_hours", "00");
		queryParam.put("end_hours", "23");
		
		if(PTSpider.Type.ProgressiveWins.getCode().equalsIgnoreCase(type)){
			//queryParam.put("description", type);
		}else{
			queryParam.put("description", type);
		}
		queryParam.put("reportby", "username");
		queryParam.put("sortby", "games");
		queryParam.put("sortorder", "1");
		queryParam.put("currency", "native");
		queryParam.put("includecurrenthour", "1");
		queryParam.put("showjackpot", "1");	//显示累积奖池
//		if("Progressive Slot Machines".equalsIgnoreCase(type)){
//		}
		if (cookies == null) {
			cookies = new HashMap<String, String>();
		}


		Document document = Jsoup.connect(REPORT_URL).
				data(queryParam).
				maxBodySize(0).
				timeout(60000).
				cookies(cookies)  //设置cookies
				.post();

//		Document document = Jsoup.parse(HttpRequest.post(REPORT_URL, queryParam, true).body());

		//writ2File(document.html());

		if(document.title().equals("Retail Admin - Login") || document.toString().contains("index.php")){
			log.warn("掉线了，重新登入");
			//log.warn(document.html());
			login();
			return spider(date, type);
		}else{
			List<PTBetVO> betList = new ArrayList<PTBetVO>();
			Elements eles = document.select("table.result");
			if( eles != null && eles.size() > 0){
				Element table = document.select("table.result").get(0); //获取class=result的table
				Elements rows = table.select("tr");
				for (int i = 1; i < rows.size() - 1; i++) {    //first row is the col names so skip it. 最后一行总计也跨过
					Element row = rows.get(i);
					Elements cols = row.select("td");
					if(PTSpider.Type.Progressive_Slot_Machines.getCode().equalsIgnoreCase(type)){
						betList.add(new PTBetVO(cols.get(0).text(), cols.get(5).text(), cols.get(7).text(), cols.get(10).text()));
					}
					else if(PTSpider.Type.ProgressiveWins.getCode().equalsIgnoreCase(type)){//单独获取progressive wins （代理日结佣金扣除奖池额度）
						// 玩家中奖的奖池额度 , 对于商户是费用要支出 , 目前使用报表中的 progressiveWin作为费用.
	                    betList.add(new PTBetVO(cols.get(0).text(), cols.get(10).text()));
					}
					else{
						betList.add(new PTBetVO(cols.get(0).text(), cols.get(5).text(), cols.get(7).text()));
					}
				}
				log.info("数据获取结束 , 共"+betList.size()+"笔");
			}
			return betList;
		}
	}

}
