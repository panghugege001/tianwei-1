package app.service.implementations;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Userbankinfo;
import dfh.model.Users;
import app.dao.BaseDao;
import app.dao.QueryDao;
import app.model.vo.UserBankInfoVO;
import app.model.vo.UserbankVO;
import app.service.interfaces.IUserBankInfoService;
import app.util.DateUtil;

public class UserBankInfoServiceImpl implements IUserBankInfoService {

	private Logger log = Logger.getLogger(UserBankInfoServiceImpl.class);
	
	private BaseDao baseDao;
	private QueryDao queryDao;
	
	@SuppressWarnings("unchecked")
	public List<Userbankinfo> queryUserBankInfoList(UserBankInfoVO vo) {
		
		String loginName = vo.getLoginName();
		
		log.info("queryUserBankInfoList方法的参数为：【loginName=" + loginName + "】");
		
		DetachedCriteria dc = DetachedCriteria.forClass(Userbankinfo.class);
		
		dc.add(Restrictions.eq("flag", 0));
		
		if (StringUtils.isNotBlank(loginName)) {
			
			dc.add(Restrictions.eq("loginname", loginName));
		}
		
		dc.addOrder(Order.desc("addtime"));
		
		return queryDao.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public Userbankinfo queryUserBankInfo(UserBankInfoVO vo) {
		
		String loginName = vo.getLoginName();
		String bankName = vo.getBankName();
		
		log.info("queryUserBankInfo方法的参数为：【loginName=" + loginName + "，bankName=" + bankName + "】");
		
		Userbankinfo result = null;
		
		DetachedCriteria dc = DetachedCriteria.forClass(Userbankinfo.class);
		
		dc.add(Restrictions.eq("flag", 0));
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("bankname", bankName));
		
		List<Userbankinfo> list = queryDao.findByCriteria(dc);
		
		if (null != list && !list.isEmpty()) {
			
			result = list.get(0);
		}
		
		return result;
	}
	
	public UserbankVO bankBinding(UserbankVO vo) {

		UserBankInfoVO userBankInfoVO = new UserBankInfoVO();
		userBankInfoVO.setLoginName(vo.getLoginname());
		
		List<Userbankinfo> list = queryUserBankInfoList(userBankInfoVO);

		if (list != null && list.size() < 3) {
			
			
			//一个银行只能绑定一张卡
			
			for(Userbankinfo obj : list){
				
				if(StringUtils.equals(obj.getBankname(),vo.getBankname())){
					
					vo.setMessage("绑定失败：你已经绑定了一张"+vo.getBankname() + "的银行卡");
					return vo;
				}
			}
			
			
			Userbankinfo info = new Userbankinfo();
			
			info.setAddtime(DateUtil.getCurrentDate());
			info.setBankaddress("none");
			info.setBankname(vo.getBankname());
			info.setBankno(vo.getBankno());
			info.setFlag(0);
			info.setLoginname(vo.getLoginname());
			baseDao.save(info);
			
			Users userFromDb = (Users) baseDao.get(Users.class,vo.getLoginname().toLowerCase());
			
			if(StringUtils.isEmpty(userFromDb.getAccountName()) && StringUtils.isNotEmpty(vo.getFamilyName())){
				
				userFromDb.setAccountName(vo.getFamilyName());
				baseDao.update(userFromDb);
			}
			
		} else {
			
			vo.setMessage("最多只能绑定三张银行卡");
		}
		
		return vo;
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}