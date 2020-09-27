package dfh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.model.Privilege;
import dfh.service.interfaces.ProposalService;

public class BatchImpPrivileges {

	private static Logger log = Logger.getLogger(BatchImpPrivileges.class);
	
	private static final String bankDepositSqlStr = "select sum(amount) from proposal where loginname=:loginname and type=:pType and flag=:optFlag and createTime >= :startTime and createTime <= :endTime";
	private static final String thirdPaymentDepositSqlStr = "select sum(money) from payorder where loginname=:loginname and type=:orderType and createTime >= :startTime and createTime <= :endTime";
	private static final String betAmountSqlStr = "select sum(bettotal) from agprofit where loginname=:loginname and createTime >= :betStartTime and createTime <= :betEndTime and platform in(:slotplatform)";
	
	public static synchronized String impPrivileges(ProposalService proposalService, File file) throws BiffException, IOException, ParseException{
		//检查是否存在当日导入的待执行数据
		String sql = "select count(*) from privilege where TO_DAYS(createtime) = TO_DAYS(:date) and status=:unexec";
		Map<String, Object> cPrams = new HashMap<String, Object>();
		cPrams.put("date", new Date());
		cPrams.put("unexec", 0);  //待派发
		if(proposalService.getCount(sql, cPrams) > 0){
			log.info("当天存在待执行的优惠，执行后才能继续导入");
			return "当天存在待执行的优惠，执行后才能继续导入";
		}
		
		InputStream stream = new FileInputStream(file.toString());
		Workbook wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		Sheet sheet = wb.getSheet(0); // 取得工作表
		
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		if(cols < 7){
			return "文件不符合要求：列数不够";
		}

		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
		String distributeMonth = yyyyMM.format(new Date());
		
		Map<String, Object> params = new HashMap<String, Object>();
		/*params.put("pt", "newpt");
		params.put("ttg", "ttg");
		params.put("qt", "qt");
		params.put("nt", "nt");
		params.put("mg", "mg");*/
		String[] slotplatformArr = {"newpt", "ttg", "qt", "nt", "mg","dt"};
		params.put("slotplatform", slotplatformArr);
		
		params.put("orderType", 0);  //支付成功的订单
		params.put("pType", 502);   //存款提案
		params.put("optFlag", 2);   //已执行
		
		//遍历
		for (int i = 1; i < rows; i++) {
			String loginName = getStringValue(sheet, i, 0);
			if(StringUtils.isBlank(loginName)){
				//空行跳过
				continue;
			}
			loginName = loginName.trim();
			Double amount = getNumberValue(sheet, i, 1);
			if(amount == 0.0){
				continue;
			}
			
			String remark = getStringValue(sheet, i, 6);
			if(StringUtils.isNotBlank(remark)){
				remark = remark.trim();
			}else{
				//备注为空的不导入，remark是数据库唯一约束
				continue;
			}
			
			Double minDeposit = getNumberValue(sheet, i, 2);
			Double minBet = getNumberValue(sheet, i, 3);
			
			Date startTime = null;
			Date endTime = null;
			Double depositAmount = 0.0;
			Double betAmount = 0.0;
			
			int status = 0;
			if((minDeposit != null && minDeposit > 0) || (minBet != null && minBet > 0)){
				
				startTime = getDateCellValue(sheet, i, 4);
				endTime = getDateCellValue(sheet, i, 5);
				
				params.put("loginname", loginName);
				params.put("startTime", startTime);
				params.put("endTime", endTime);
				
				params.put("betStartTime", getNextDay(startTime));
				params.put("betEndTime", getNextDay(endTime));
				
				if(minDeposit != null && minDeposit > 0){
					//检查存款
					depositAmount = Arith.add(proposalService.getDoubleValueBySql(bankDepositSqlStr, params), proposalService.getDoubleValueBySql(thirdPaymentDepositSqlStr, params));
					if(depositAmount < minDeposit){
						status = 2;
						remark += "  存款未达到要求，自动取消";
					}
				}
				
				if(minBet != null && minBet > 0){
					//检查流水
					betAmount = proposalService.getDoubleValueBySql(betAmountSqlStr, params);
					if(betAmount < minBet){
						status = 2;
						remark += "  流水未达到要求，自动取消";
					}
				}
			}

			//保存
			Privilege privilege = new Privilege(loginName, new Date(), amount, status, distributeMonth, 
					minDeposit, minBet, startTime, endTime, depositAmount, betAmount, remark);
			try {
				proposalService.save(privilege);
			} catch (Exception e) {
				log.error(e.getMessage());
				continue;
			}
		}
		return "导入成功";
	}
	
	public static String getStringValue(Sheet sheet, int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		if(c.getType() == CellType.EMPTY){
			return "";
		}
		String s = c.getContents();
		if (c.getType() == CellType.LABEL) {
			LabelCell labelc00 = (LabelCell) c;
			s = labelc00.getString().trim();
		}
		return s;
	}
	
	public static double getNumberValue(Sheet sheet, int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		if(c.getType() == CellType.EMPTY){
			return 0.0;
		}
		NumberCell nc = (NumberCell) c;
		double s = nc.getValue();
		if (c.getType() == CellType.NUMBER) {
			NumberCell labelc00 = (NumberCell) c;
			s = labelc00.getValue();
		}
		return s;
	}
	
	/**
	 * 获取时间
	 * 
	 * @param sheet
	 * @param rows
	 * @param cols
	 * @return
	 * @throws ParseException 
	 */
	private static Date getDateCellValue(Sheet sheet, int rows, int cols) throws ParseException{		
		Cell c = sheet.getCell(cols, rows);
		if(c.getType() == CellType.DATE){
			DateCell dc = (DateCell) c;
			//jxl获取的时间，比实际的时间多8个小时，需要特殊处理
			TimeZone gmt = TimeZone.getTimeZone("GMT");
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        dateFormat.setTimeZone(gmt);
	        String dateStr = dateFormat.format(dc.getDate());
	        TimeZone local = TimeZone.getDefault();
	        dateFormat.setTimeZone(local);
	        
	        return dateFormat.parse(dateStr);
		}else{
			LabelCell labelc00 = (LabelCell) c;
			return DateUtil.parseDateForStandard(labelc00.getString());
		}
	}
	
	private static Date getNextDay(Date date){
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.add(Calendar.DATE, 1);
		return cld.getTime();
	}
	
}
