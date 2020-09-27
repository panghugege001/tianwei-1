package com.gsmc.png.service.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.gsmc.png.model.PNGData4OracleVO;
import com.gsmc.png.model.PNGDataDY;
import com.gsmc.png.service.interfaces.IPNGDataProcessorService;
import com.gsmc.png.utils.DateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PNGDataProcessorServiceImpl extends UniversalServiceImpl implements
		IPNGDataProcessorService {
	
	private static Logger log = Logger.getLogger(PNGDataProcessorServiceImpl.class);
	
	@Override
	public String processData(String msg) {
		
		List<PNGData4OracleVO> pngVOList = this.analysisPNGMsg(msg);
		int i = 0;
		if(pngVOList != null && pngVOList.size() > 0){
			
			//this.validData(pngVOList);//校验数据。暂时不校验。
			log.info(Thread.currentThread().getName() + "----png本次解析数据条数：" + pngVOList.size());
			try {
				i = this.updatePngVOList(pngVOList);
			} catch (Exception e) {
				log.error(Thread.currentThread().getName() + "----批处理出错", e);
			}
			
		} else {
			log.warn(Thread.currentThread().getName() + "----png解析条数0:" + msg);
		}
		return "插入数据库条数：" + i;
	}
	
	private int updatePngVOList(List<PNGData4OracleVO> pngVOList) {
		
		int i = 0;
		if(pngVOList != null && pngVOList.size() > 0){
			for(PNGData4OracleVO vo : pngVOList){
				try {
					
					this.save(vo);
					i++;
				} catch (Exception e) {
					log.error("PNGData4OracleVO SAVE ERROR:", e);
				}
				
			}
		}
		return i;
	}

	//将字符串解析成VOLIST
	private List<PNGData4OracleVO> analysisPNGMsg(String jsonStr){
		
		List<PNGData4OracleVO> list = new ArrayList<PNGData4OracleVO>();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		Object messages = jsonObject.get("Messages");
		JSONArray jsonArray = JSONArray.fromObject(messages);
		Iterator<JSONObject> it =  jsonArray.iterator();
		
		while(it.hasNext()){
			
			JSONObject msg = it.next();
			Object messageType = msg.getString("MessageType");
			
			if(messageType != null && "4".equals(messageType)){
				PNGData4OracleVO o = this.createBean(msg);
				if(o != null){
					list.add(o);
				}
			}
		}
		return list;
	}

	//将字符串封装成BEAN
	private PNGData4OracleVO createBean(JSONObject msg) {
		
		/*PNGData data = (PNGData) msg.toBean(msg, PNGData.class);
		log.info(data.getExternalUserId());*/
		
		String externalUserId = msg.getString("ExternalUserId");
		Object transactionId = msg.get("TransactionId");
		Object status = msg.get("Status");
		Object roundId = msg.get("RoundId");
		Object roundLoss = msg.get("RoundLoss");
		Object jackpotLoss = msg.get("JackpotLoss");
		Object jackpotGain = msg.get("JackpotGain");
		Object balance = msg.get("Balance");
		Object totalLoss = msg.get("TotalLoss");
		Object totalGain = msg.get("TotalGain");
		Object externalFreegameId = msg.get("ExternalFreegameId");
		Object messageType = msg.get("MessageType");
		Object timeTransaction = msg.get("Time");
		Object messageTimestamp = msg.get("MessageTimestamp");
		
		PNGData4OracleVO pngData4OracleVO = this.createVO(externalUserId);
		if(pngData4OracleVO == null){
			return null;
		}
		
		try {
			
			pngData4OracleVO.setExternalUserId(externalUserId);
			pngData4OracleVO.setTransactionId(transactionId == null? null : transactionId.toString());
			pngData4OracleVO.setStatus(status == null? null : status.toString());
			pngData4OracleVO.setRoundId(roundId == null? null : roundId.toString());
			pngData4OracleVO.setRoundLoss(roundLoss == null? null : Double.parseDouble(roundLoss.toString()));
			pngData4OracleVO.setJackpotLoss(jackpotLoss == null? null : Double.parseDouble(jackpotLoss.toString()));
			pngData4OracleVO.setJackpotGain(jackpotGain == null? null : Double.parseDouble(jackpotGain.toString()));
			pngData4OracleVO.setBalance(balance == null? null : Double.parseDouble(balance.toString()));
			pngData4OracleVO.setTotalLoss(totalLoss == null? null : Double.parseDouble(totalLoss.toString()));
			pngData4OracleVO.setTotalGain(totalGain == null? null : Double.parseDouble(totalGain.toString()));
			pngData4OracleVO.setExternalFreegameId(externalFreegameId == null? null : externalFreegameId.toString());
			pngData4OracleVO.setMessageType(messageType == null? null : messageType.toString());
			pngData4OracleVO.setTimeTransaction(timeTransaction == null ? null : timeTransaction.toString().replace("T", " ").substring(0,19));
			pngData4OracleVO.setMessageTimestamp(messageTimestamp == null ? null : messageTimestamp.toString().replace("T", " ").substring(0,19));
			pngData4OracleVO.setCreatetime(DateUtil.fmtDate(new Date()));
			return pngData4OracleVO;
			
		} catch (Exception e) {
			log.error(Thread.currentThread().getName() + "----error JSONObject" + msg);
			log.error(Thread.currentThread().getName() + "----error PNGDataVO" + pngData4OracleVO.toString());
			log.error(Thread.currentThread().getName() + "----JSON转BEAN出错", e);
		}
		
		return null;
	}
	
	private PNGData4OracleVO createVO(String externalUserId) {
		PNGData4OracleVO pngData4OracleVO = null;
		if(externalUserId != null && externalUserId.startsWith("DY8")){
			pngData4OracleVO = new PNGDataDY();
		}else {
			log.error("错误用户前缀：" + externalUserId);
		}
		return pngData4OracleVO;
	}

	@Override
	public Map<String, Object> getPlayerBetsByDate(String timeStart, String timeEnd,
			String loginname) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(timeStart) || StringUtils.isBlank(loginname)){
			
			result.put("success", false);
			result.put("message", "参数不全");
		}
		
		String time_start = null;
		String time_end = null;
		try {
			time_start = DateUtil.convertUTC8toUTC(timeStart);
			if(StringUtils.isNotBlank(timeEnd)){
				time_end = DateUtil.convertUTC8toUTC(timeEnd);
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
		dc.add(Restrictions.eq("externalUserId", loginname));
		dc.add(Restrictions.ge("timeTransaction", time_start));
		if(time_end != null){
			dc.add(Restrictions.le("timeTransaction", time_end));
		}
		dc.add(Restrictions.eq("status", "1"));
		dc.setProjection(Projections.sum("roundLoss"));
		Double bets = (Double) this.findByCriteria(dc).get(0);
		
		result.put("success", true);
		result.put("message", "查询成功");
		result.put("bets", bets == null? 0.0: bets);
		return result;
	}

	private DetachedCriteria createDcByLoginname(String loginname) {
		
		DetachedCriteria dc = null;
		if(loginname != null && loginname.startsWith("DY8")){
			
			dc = DetachedCriteria.forClass(PNGDataDY.class);
		}else {
			log.error("错误用户前缀：" + loginname);
		}
		return dc;
	}
}
