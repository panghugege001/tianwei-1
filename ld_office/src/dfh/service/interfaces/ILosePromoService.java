package dfh.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import dfh.model.PTBigBang;

public interface ILosePromoService {

	public void distributeLosePromo(String loginName, Double winlose, Date startTime, Date endTime);
	
	@SuppressWarnings("unchecked")
	public List getListBySql(String sql, Map<String, Object> params) throws Exception ;
	
	/**
	 * 计算PT大爆炸礼金
	 * @param item
	 */
    public void calculatePTBigBangGifgMoney(PTBigBang item);
    
    /**
     * 获取用户推荐码
     * @param userName
     * @return
     */
    public String getIntroCode(String userName);
}
