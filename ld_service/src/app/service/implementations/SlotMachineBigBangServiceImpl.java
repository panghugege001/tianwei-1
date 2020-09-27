package app.service.implementations;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import app.dao.QueryDao;
import app.model.vo.SlotMachineBigBangVO;
import app.service.interfaces.ISlotMachineBigBangService;

public class SlotMachineBigBangServiceImpl implements ISlotMachineBigBangService {

	private QueryDao queryDao;
	
	@SuppressWarnings("rawtypes")
	public List<SlotMachineBigBangVO> querySlotMachineBigBangList(SlotMachineBigBangVO vo) {
	
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date d = calendar.getTime();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String distributeTime = df.format(d);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'pt' as platform, a.netwin_lose, a.bonus, a.giftmoney, a.distributeTime, a.id, a.status FROM ptbigbang a where a.username = :username and a.status in (:status) and a.distributeTime >= :distributeTime");
//		sql.append(" UNION ");
//		sql.append("SELECT b.platform, b.netwin_lose, b.bonus, b.giftmoney, b.distributeTime, b.id, b.status FROM qtbigbang b where b.username = :username and b.status in (:status) and b.distributeTime >= :distributeTime");
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", vo.getLoginName());
		params.put("status", new String[] { "1", "2" });
		params.put("distributeTime", distributeTime);
		
		List list = queryDao.list(sql.toString(), params);
		
		List<SlotMachineBigBangVO> returnList = new ArrayList<SlotMachineBigBangVO>();
		
		if (null != list && !list.isEmpty()) {
			
			for (int i = 0, len = list.size(); i < len; i++) {
				
				Object[] obj = (Object[]) list.get(i);
				
				SlotMachineBigBangVO bean = new SlotMachineBigBangVO();
				
				bean.setPlatform(null == obj[0] ? "" : String.valueOf(obj[0]));
				bean.setNetWinOrLose(null == obj[1] ? 0.00 : Double.valueOf(String.valueOf(obj[1])));
				bean.setBonus(null == obj[2] ? 0.00 : Double.valueOf(String.valueOf(obj[2])));
				bean.setGiftMoney(null == obj[3] ? 0.00 : Double.valueOf(String.valueOf(obj[3])));
				Timestamp timestamp = (Timestamp)obj[4];
				if (null != timestamp) {
					String s = String.valueOf(timestamp);
					if (StringUtils.isNotBlank(s)) {
						s = s.substring(0, s.length() - 2);
						bean.setDistributeTime(s);
					}
				}
				bean.setId(Integer.valueOf(String.valueOf(obj[5])));
				bean.setStatus(String.valueOf(obj[6]));
				
				returnList.add(bean);
			}
		}
	
		return returnList;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}