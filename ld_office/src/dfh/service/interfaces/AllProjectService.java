package dfh.service.interfaces;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import dfh.model.QqInfFromNet;
import dfh.utils.Page;


/**
 * 处理所有链接其他平台的功能
 * @author chaoren
 */
public interface AllProjectService extends UniversalService{
	
	/**
	 * 新增或者更新数据
	 * @return boolean 当前数据是否成功
	 */
	public boolean saveOrUpdateQq(QqInfFromNet qif) throws Exception; 
	
	/**
	 * 查询用户所有账单,包括用户存款量,投注量,输赢
	 * @param loginname
	 * @return map集合
	 */
	public Map<String, Double> queryUserBill(String loginname);
	
	/**
	 * 查询.net传递的QQ信息
	 * @param qif
	 * @return
	 */
	public Page queryQQInf(DetachedCriteria cri, Integer size, Integer pageIndex);
}
