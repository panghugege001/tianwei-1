package com.gsmc.png.service.implementations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.gsmc.png.model.ag.AGData4OracleVO;
import com.gsmc.png.model.ag.AgConfig;
import com.gsmc.png.model.ag.AgData777;
import com.gsmc.png.model.ag.AgDataE68;
import com.gsmc.png.model.ag.AgDataHU;
import com.gsmc.png.model.ag.AgDataL8;
import com.gsmc.png.model.ag.AgDataLD;
import com.gsmc.png.model.ag.AgDataMZC;
import com.gsmc.png.model.ag.AgDataQLE;
import com.gsmc.png.model.ag.AgDataQY;
import com.gsmc.png.model.ag.AgDataUFA;
import com.gsmc.png.model.ag.AgDataULE;
import com.gsmc.png.model.ag.AgDataWS;
import com.gsmc.png.model.ag.AgDataZB;
import com.gsmc.png.quart.thread.AGSlotThread;
import com.gsmc.png.service.interfaces.IAGDataProcessorService;
import com.gsmc.png.utils.AgUtils;
import com.gsmc.png.utils.DateUtil;


public class AGDataProcessorServiceImpl extends UniversalServiceImpl implements IAGDataProcessorService {
	
	private static Logger log = Logger.getLogger(AGDataProcessorServiceImpl.class);

	@Override
	public String processData(String xml, Date createtime, Date fromTime, Date toTime, List<AGData4OracleVO> listData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAgLastTime(String platform) {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sfs = new SimpleDateFormat("yyyy-MM-dd");
		AgConfig agConfig=(AgConfig) get(AgConfig.class, platform,LockMode.UPGRADE);
		if (agConfig==null) {
          Calendar calendarLastTime=Calendar.getInstance();		
          calendarLastTime.add(Calendar.DAY_OF_MONTH, -1);
	     return sfs.format(calendarLastTime.getTime())+" 00:00:00";
		}else {
			return sf.format(agConfig.getLastupdateTime());
		}
	
	}

	@Override
	public String processInsertAgData(List<AGData4OracleVO> data, Date lastTime, String platformType) {
		// TODO Auto-generated method stub
		log.info("total 录入数据 size:"+data.size());
		for(AGData4OracleVO vo : data){
			  save(vo);
		}
		
		AgConfig agConfig=(AgConfig) get(AgConfig.class, platformType,LockMode.UPGRADE);
		if (agConfig==null) 
			save(new AgConfig(platformType, 0, new Date(), lastTime));
		else {
			agConfig.setLastupdateTime(lastTime);
      	    update(agConfig);
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getPlayerBetsByDate(String timeStart, String timeEnd, String loginname) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(timeStart) || StringUtils.isBlank(loginname)){
			
			result.put("success", false);
			result.put("message", "参数不全");
		}
		
		String time_start = null;
		String time_end = null;
		try {
			time_start = DateUtil.convertUTC12toUTC(timeStart);
			if(StringUtils.isNotBlank(timeEnd)){
				time_end = DateUtil.convertUTC12toUTC(timeEnd);
			}
		} catch (Exception e) {
			log.error("异常时间：time_start----" + time_start + ",time_end----" + time_end);
			result.put("success", false);
			result.put("message", "日期转换异常！");
			return result;
		}
		
		DetachedCriteria dc = this.createDcByLoginname(loginname);
		if(dc == null){
			result.put("success", false);
			result.put("message", "loginname前缀异常！");
		}

		dc.add(Restrictions.eq("playerName",loginname));
		dc.add(Restrictions.ge("betTime", DateUtil.parseDateForStandard(time_start)));
		if(timeEnd != null){
			dc.add(Restrictions.le("betTime",  DateUtil.parseDateForStandard(time_end)));
		}
		dc.setProjection(Projections.sum("validbetamount"));
		Double bets = (Double) this.findByCriteria(dc).get(0);
		
		result.put("success", true);
		result.put("message", "查询成功");
		result.put("bets", bets == null? 0.0: bets);
		return result;
	}
	
	
	

	
     private DetachedCriteria createDcByLoginname(String loginname) {
		
		DetachedCriteria dc = null;
		if(loginname != null && loginname.startsWith("qi_")){
			
			dc = DetachedCriteria.forClass(AgDataQY.class);
		} else if(loginname != null && loginname.startsWith("ei_")){
			
			dc = DetachedCriteria.forClass(AgDataE68.class);
		} else if(loginname != null && loginname.startsWith("li_")){
			
			dc = DetachedCriteria.forClass(AgDataL8.class);
		} else if(loginname != null && loginname.startsWith("gi_")){
			
			dc = DetachedCriteria.forClass(AgData777.class);
		} else if(loginname != null && loginname.startsWith("bi_")){
			
			dc = DetachedCriteria.forClass(AgDataUFA.class);
		} else if(loginname != null && loginname.startsWith("ai_")){
			
			dc = DetachedCriteria.forClass(AgDataULE.class);
		} else if(loginname != null && loginname.startsWith("ci_")){
			
			dc = DetachedCriteria.forClass(AgDataQLE.class);
		} else if(loginname != null && loginname.startsWith("di_")){
			
			dc = DetachedCriteria.forClass(AgDataMZC.class);
		} else if(loginname != null && loginname.startsWith("wi_")){
			
			dc = DetachedCriteria.forClass(AgDataWS.class);
		} else if(loginname != null && loginname.startsWith("zb_")){
			
			dc = DetachedCriteria.forClass(AgDataZB.class);
		} else if(loginname != null && loginname.startsWith("ti_")){
			
			dc = DetachedCriteria.forClass(AgDataHU.class);
		} else if(loginname != null && loginname.startsWith("ki_")){
			
			dc = DetachedCriteria.forClass(AgDataLD.class);
		} else {
			log.error("错误用户前缀：" + loginname);
		}
		return dc;
	}

	
	@Override
	 public String repairAgData(List<AGData4OracleVO>list){
		int countData=0;
		try {
				  
			for(AGData4OracleVO vo : list){
				if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("qi_")){
					
					AgDataQY  data=(AgDataQY) get(AgDataQY.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("ei_")){
					AgDataE68  data=(AgDataE68) get(AgDataE68.class, vo.getBillNo());
					if (data==null){
						save(vo);
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("li_")){
					AgDataL8  data=(AgDataL8) get(AgDataL8.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
		
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("gi_")){
					AgData777  data=(AgData777) get(AgData777.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("bi_")){
					AgDataUFA  data=(AgDataUFA) get(AgDataUFA.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("ai_")){
					AgDataULE  data=(AgDataULE) get(AgDataULE.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("ci_")){
					AgDataQLE  data=(AgDataQLE) get(AgDataQLE.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null &&vo.getPlayerName().startsWith("di_")){
					AgDataMZC  data=(AgDataMZC) get(AgDataMZC.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("wi_")){
					AgDataWS  data=(AgDataWS) get(AgDataWS.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
	
				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("zb_")){
					AgDataZB  data=(AgDataZB) get(AgDataZB.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}

				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("ti_")){
					AgDataHU  data=(AgDataHU) get(AgDataHU.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}

				} else if(vo.getPlayerName() != null && vo.getPlayerName().startsWith("ki_")){
					AgDataLD  data=(AgDataLD) get(AgDataLD.class, vo.getBillNo());
					if (data==null){
						save(vo);
						countData++;
						//log.info("补录单号:"+vo.getBillNo()+"  用户:"+vo.getPlayerName()+"  时间:"+vo.getBetTime());
					}
				} else {
					log.error("错误用户前缀：" + vo.getPlayerName());
				}
				
			}
			
			log.info("*************repairAgData 补录数据 : "+countData);

			  } catch (Exception e) {
				  e.printStackTrace();
				  log.info("*************repairAgData error ********");
			  }
	
			  return null;
	}
	

	

	 public List<AGData4OracleVO> organizedata(String date){
			List<AGData4OracleVO>rtnData=new ArrayList<AGData4OracleVO>();
			  String cagent="B20";
			  Integer pageSize=500;
			
			  try {
				
			  Calendar calendarStartTime=Calendar.getInstance();
			  calendarStartTime.setTime(DateUtil.parseDateForStandard(date));
			  Date startTime=calendarStartTime.getTime();
			  String  beforeTime= getAgLastTime("AGIN");
			  Calendar calendarLastTime=Calendar.getInstance();
			
			
			  if (calendarStartTime.getTime().getDate()==calendarLastTime.getTime().getDate()) {
				  calendarLastTime.setTime(DateUtil.parseDateForStandard(beforeTime));
				  calendarLastTime.set(Calendar.SECOND, -60*5);
			  }else{
				  calendarLastTime.setTime(calendarStartTime.getTime());
				  calendarLastTime.add(Calendar.HOUR, 23); 
				  calendarLastTime.add(Calendar.MINUTE, 59);
				  calendarLastTime.add(Calendar.SECOND, 59);
			  }
			  
			 Date endTime=calendarLastTime.getTime();
		     String xml=AgUtils.getBetRecordByDate("AGIN",startTime, endTime, cagent, pageSize,1);
		     rtnData=AGSlotThread.getReRecordData(xml, startTime,endTime,cagent,"AGIN",pageSize,1);
			  } catch (Exception e) {
				  e.printStackTrace();
				  return rtnData;
			  }
			return rtnData;
		}
	 

}
