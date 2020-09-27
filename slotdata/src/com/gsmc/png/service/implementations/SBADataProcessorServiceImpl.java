package com.gsmc.png.service.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gsmc.png.model.shaba.SBAData4OracleVO;
import com.gsmc.png.model.shaba.SBAData777;
import com.gsmc.png.model.shaba.SBADataE68;
import com.gsmc.png.model.shaba.SBADataHU;
import com.gsmc.png.model.shaba.SBADataL8;
import com.gsmc.png.model.shaba.SBADataLD;
import com.gsmc.png.model.shaba.SBADataMZC;
import com.gsmc.png.model.shaba.SBADataQLE;
import com.gsmc.png.model.shaba.SBADataQY;
import com.gsmc.png.model.shaba.SBADataUFA;
import com.gsmc.png.model.shaba.SBADataULE;
import com.gsmc.png.model.shaba.SBADataWS;
import com.gsmc.png.model.shaba.SBADataZB;
import com.gsmc.png.service.interfaces.ISBADataProcessorService;
import com.gsmc.png.utils.DateUtil;
import com.gsmc.png.utils.ShaBaUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

public class SBADataProcessorServiceImpl extends UniversalServiceImpl implements
		ISBADataProcessorService {
	
	private static Logger log = Logger.getLogger(SBADataProcessorServiceImpl.class);

	@Override
	public String processData(Long version_key,Map map) {
		String last_version_key =  map.get("last_version_key")+"";
		List<SBAData4OracleVO> list = (List<SBAData4OracleVO>) map.get("BetDetails");
		if(list !=null && !list.isEmpty()){
			List relist = createVos(list);
			saveOrUpdateAll(relist);
		}
		if("0".equals(last_version_key) || last_version_key == null){
			save(version_key+"");
		}else{
			save(last_version_key);
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List createVos(List<SBAData4OracleVO> list){
		List relist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			SBAData4OracleVO vo = list.get(i);
			String loginname = vo.getVendor_member_id();
			SBAData4OracleVO revo = null;
			if(loginname != null && loginname.startsWith("q_")){
				revo = new SBADataQY();
			} else if(loginname != null && loginname.startsWith("e_")){
				revo = new SBADataE68();
			} else if(loginname != null && loginname.startsWith("l_")){
				revo = new SBADataL8();
			} else if(loginname != null && loginname.startsWith("g_")){
				revo = new SBAData777();
			} else if(loginname != null && loginname.startsWith("b_")){
				revo = new SBADataUFA();
			} else if(loginname != null && loginname.startsWith("a_")){
				revo = new SBADataULE();
			} else if(loginname != null && loginname.startsWith("c_")){
				revo = new SBADataQLE();
			} else if(loginname != null && loginname.startsWith("d_")){
				revo = new SBADataMZC();
			} else if(loginname != null && loginname.startsWith("w_")){
				revo = new SBADataWS();
			} else if(loginname != null && loginname.startsWith("zb_")){
				revo = new SBADataZB();
			} else if(loginname != null && loginname.startsWith("t_")){
				revo = new SBADataHU();
			} else if(loginname != null && loginname.startsWith("k_")){
				revo = new SBADataLD();
			} else {
				log.error("错误用户前缀：" + loginname);
				continue;
			}
			revo.setTrans_id(vo.getTrans_id());
			revo.setVendor_member_id(vo.getVendor_member_id());
			revo.setOperator_id(vo.getOperator_id());
			revo.setMatch_datetime(vo.getMatch_datetime());
			revo.setStake(vo.getStake());
			revo.setTransaction_time(vo.getTransaction_time());
			revo.setTicket_status(vo.getTicket_status());
			revo.setWinlost_amount(vo.getWinlost_amount());
			revo.setAfter_amount(vo.getAfter_amount());
			revo.setCurrency(vo.getCurrency());
			revo.setWinlost_datetime(vo.getWinlost_datetime());
			revo.setOdds_type(vo.getOdds_type());
			revo.setVersion_key(vo.getVersion_key());
			relist.add(revo);
		}
		return relist;
	}
	@SuppressWarnings("rawtypes")
	private void save(String last_version_key){
		String querysql = "select version_key from api_call_record where version_key =:version_key";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("version_key", last_version_key);
		List list1 = excuteQuerySql(querysql, param);
		if(list1 !=null && !list1.isEmpty()){
			String updatesql = "update api_call_record set frequency = frequency + 1,updatetime = to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') where version_key =:version_key";
			excuteSql(updatesql, param);
		}else{
			String insertsql = "insert into api_call_record values(:version_key,1,to_char(sysdate,'yyyy-MM-dd HH24:mi:ss'),to_char(sysdate,'yyyy-MM-dd HH24:mi:ss')) ";
			excuteSql(insertsql, param);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Long getVersionKey() {
		String sql = "select version_key from api_call_record order by createtime desc ";
		List list = excuteQuerySql(sql, new HashMap());
		if(list !=null && !list.isEmpty()){
			String version_key =  (String)list.get(0);
			if(version_key != null){
				return Long.parseLong(version_key);
			}
		}
		return null;
	}

	@Override
	public List getSbaXimaData(String startTime, String endTime,String productdb) {
		String querysql = "select vendor_member_id , sum(stake),sum(winlost_amount)  from "+productdb+" where winlost_datetime >=:startTime and winlost_datetime <=:endTime group by vendor_member_id";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		List list = excuteQuerySql(querysql, param);
		List resultList = new ArrayList();
		if(list != null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				Map map = new HashMap<String, String>();
				map.put("loginname", obj[0]);
				map.put("bet", obj[1]);
				map.put("amount", obj[2]);
				resultList.add(map);
			}
		}
		return resultList;
	}

	/**
	 * 重新获取前一天之后的沙巴投注额
	 */
	@Override
	public void reacquireSBAData() {
		// 先删除错误数据
		String beforeDate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String sql = "delete from api_call_record where  createTime >='"+beforeDate+"'";
		excuteSql(sql, new HashMap());
		
		String sql1 = "delete from tablename where winlost_datetime >='"+beforeDate+"'";
		String[] strArr = { "sba_data_qy", "sba_data_l8", "sba_data_e68",
				"sba_data_777", "sba_data_ule", "sba_data_ufa", "sba_data_mzc",
				"sba_data_qle", "sba_data_ws", "sba_data_hu", "sba_data_ld",
				"sba_data_zb" };
		List list = Arrays.asList(strArr);
		for (int i = 0; i < list.size(); i++) {
			String exeSql = sql1.replace("tablename", list.get(i)+"");
			excuteSql(exeSql, new HashMap());
		}
		//重新执行100次，去抓取沙巴投注额()
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			execute();
		}
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		log.info("执行耗时："+Float.toString(seconds));
	}
	
	public void execute() {
		try {
			Long version_key = getVersionKey();
			Map map = null;
			if (version_key != null) {
				if(version_key.longValue() != 0){
					map = ShaBaUtils.GetBetDetail(version_key, "");
					if(map != null){
						processData(version_key,map);
					}
				}else{
					//如果抓到最后一笔，记录调用次数
					map = new HashMap();
					map.put("last_version_key", version_key);
					map.put("BetDetails", new ArrayList());
					processData(version_key,map);
				}
			}else{
				map = ShaBaUtils.GetBetDetail(0L, "");
				if(map != null){
					processData(version_key,map);
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
}
