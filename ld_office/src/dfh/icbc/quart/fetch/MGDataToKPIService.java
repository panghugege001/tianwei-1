package dfh.icbc.quart.fetch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import dfh.model.notdb.MGPlaycheckVo;
import dfh.service.interfaces.MGSService;
import dfh.utils.AESUtil;
import dfh.utils.Arith;
import dfh.utils.DateUtil;
import dfh.utils.MatchDateUtil;

public class MGDataToKPIService {

	private static Logger log = Logger.getLogger(MGDataToKPIService.class);
	
	private MGSService mgsService ;

	private final String kpiDBUrl = "vq+rhEYtIf1BMbkgv941TYcupBnrZ0PUF51JfUEEzM53w+lGAJ/jFDD7nJGzsTnj";
	private final String kpiDBDrive = "oracle.jdbc.OracleDriver";
	private final String dbuser = "bV2sXhQHHw2sD1mNQTHJeA==";
	private final String dbpassord = "hrHxGkq8CrV7Dit9Hl4llA==";
	private final String sql = "insert into win_report(UUID, GAMEACCOUNT, GAMEBET, GAMEPROFIT, GAMEBETCOUNT, STARTTIME, ENDTIME, UPDATETIME, PRODUCTTYPE, PLATFORMTYPE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final String pName = "ld";
	
	public void submitMGData2KPI(){
		log.info(">>>>>>>>>>>>>>>>>>>>>>MG数据导入KPI后台");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = initKPIDBConnection();
			ps = conn.prepareStatement(sql);
			String startTime = DateUtil.formatDateForStandard(new DateTime().minusDays(1).withTimeAtStartOfDay().toDate());
			String endTime = DateUtil.formatDateForStandard(new DateTime().withTimeAtStartOfDay().minusSeconds(1).toDate());
			List<MGPlaycheckVo> list = getMGSDataofLastDay(startTime, endTime);
			int BATCH_SIZE = 1000;
			int count = 0;
			String now = DateUtil.formatDateForStandard(new Date());
			for (MGPlaycheckVo item : list) {
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, item.getLoginname());
				ps.setDouble(3, item.getBet());
				ps.setDouble(4, item.getNet());
				ps.setString(5, "1");
				ps.setString(6, startTime);
				ps.setString(7, endTime);
				ps.setString(8, now);
				ps.setString(9, pName);
				ps.setString(10, "mg");
				
				ps.addBatch();
			    if(++count % BATCH_SIZE == 0) {
			        ps.executeBatch();
			    }
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
			conn.close();
			log.info(">>>>>>>>>>>>>>>>>>>>>>MG数据导入KPI后台结束<<<<<<<<<<<<<<<<<<");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		} catch (SQLException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	
	
	private Connection initKPIDBConnection() throws Exception{
		Connection conn = null;
		Class.forName(kpiDBDrive);
		conn = DriverManager.getConnection(AESUtil.aesDecrypt(kpiDBUrl, AESUtil.KEY), AESUtil.aesDecrypt(dbuser, AESUtil.KEY), AESUtil.aesDecrypt(dbpassord, AESUtil.KEY));
		return conn;
	}

	private List<MGPlaycheckVo> getMGSDataofLastDay(String startTime, String endTime){
		//前一天的MG投注及输赢数据
		StringBuilder sql = new StringBuilder("select loginname, SUM(amount) as amount, actiontype from mglog where actiontime>=:startTime and actiontime<=:endTime GROUP BY loginname, actiontype ORDER BY loginname");
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		List playList = mgsService.getListBySql(sql.toString(), params);
        Map<String, MGPlaycheckVo> palyCheckVoMap = new HashMap<String, MGPlaycheckVo>();
        for (Object obj : playList) {
        	Object[] item = (Object[]) obj;
        	MGPlaycheckVo playVo;
        	String userName = item[0].toString();
        	if(palyCheckVoMap.containsKey(userName)){
        		playVo = palyCheckVoMap.get(userName);
        	}else{
        		playVo = new MGPlaycheckVo();
        		playVo.setLoginname(userName);
        		palyCheckVoMap.put(userName, playVo);
        	}
        	
        	String type = item[2].toString();
        	Double amount = Arith.div(Double.parseDouble(item[1].toString()), 100);
        	if(type.equalsIgnoreCase("bet")){
        		playVo.setBet(amount);
        	}else if(type.equalsIgnoreCase("refund")){
        		playVo.setRefund(amount);
        	}else if(type.equalsIgnoreCase("win")){
        		playVo.setWin(amount);
        	}else if(type.equalsIgnoreCase("progressivewin")){
        		playVo.setProgressivewin(amount);
        	}
        	playVo.setNet(Arith.add(playVo.getNet(), amount));
		}
        List<MGPlaycheckVo> list = new ArrayList<MGPlaycheckVo>();
        for (String key: palyCheckVoMap.keySet()) {
        	MGPlaycheckVo playVo = palyCheckVoMap.get(key);
        	playVo.setBet(Math.abs(Arith.add(playVo.getBet(), playVo.getRefund())));
        	playVo.setNet(Arith.sub(playVo.getBet(), Arith.add(playVo.getWin(), playVo.getProgressivewin())));
        	list.add(playVo);
		}
        return list;
	}
	
	public  String repairMGData2KPI(MGSService mgsServices ,Date date){
		log.info(">>>>>>>>>>>>>>>>>>>>>>MG数据导入KPI后台");
		this.mgsService=mgsServices;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = initKPIDBConnection();
			ps = conn.prepareStatement(sql);
			Date sdate = MatchDateUtil.parseDatetime(MatchDateUtil.formatDate(date)+" 00:00:00");
			Date edate = MatchDateUtil.parseDatetime(MatchDateUtil.formatDate(date)+" 23:59:59");
			String startTime = DateUtil.formatDateForStandard(sdate);
			String endTime = DateUtil.formatDateForStandard(edate);

			List<MGPlaycheckVo> list = getMGSDataofLastDay(startTime, endTime);
			int BATCH_SIZE = 1000;
			int count = 0;
			String now = DateUtil.formatDateForStandard(new Date());
			for (MGPlaycheckVo item : list) {
				ps.setString(1, UUID.randomUUID().toString());
				ps.setString(2, item.getLoginname());
				ps.setDouble(3, item.getBet());
				ps.setDouble(4, item.getNet());
				ps.setString(5, "1");
				ps.setString(6, startTime);
				ps.setString(7, endTime);
				ps.setString(8, now);
				ps.setString(9, pName);
				ps.setString(10, "mg");
				
				ps.addBatch();
			    if(++count % BATCH_SIZE == 0) {
			        ps.executeBatch();
			    }
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
			conn.close();
			log.info(">>>>>>>>>>>>>>>>>>>>>>MG数据导入KPI后台结束<<<<<<<<<<<<<<<<<<");
			return "导入数据成功";
		} catch (Exception e) {
			log.error(e.getMessage());
			return "导入数据错误，联系技术";
		}
	}

	public MGSService getMgsService() {
		return mgsService;
	}


	public void setMgsService(MGSService mgsService) {
		this.mgsService = mgsService;
	}
}
