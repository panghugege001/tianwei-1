package dfh.service.implementations;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import dfh.dao.UserbankinfoDao;
import dfh.model.Actionlogs;
import dfh.model.Userbankinfo;
import dfh.model.enums.ActionLogType;
import dfh.service.interfaces.IUserbankinfoService;
import dfh.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.CollectionUtils;

public class UserbankinfoServiceImpl implements IUserbankinfoService {
	
	private UserbankinfoDao userbankinfoDao;

	public UserbankinfoDao getUserbankinfoDao() {
		return userbankinfoDao;
	}

	public void setUserbankinfoDao(UserbankinfoDao userbankinfoDao) {
		this.userbankinfoDao = userbankinfoDao;
	}

	@Override
	public List<Userbankinfo> getUserbankinfoList(String loginname)
			throws Exception {
		// TODO Auto-generated method stub
		return this.userbankinfoDao.getUserbankinfoList(loginname);
	}

	@Override
	public void banding(String loginname, String bankno,String bankname,String bankaddress) throws Exception {
		// TODO Auto-generated method stub
		Userbankinfo userbankinfo=new Userbankinfo(loginname, bankno, bankname,bankaddress, DateUtil.convertToTimestamp(new Date()), 0);
		this.userbankinfoDao.save(userbankinfo);
	}

	@Override
	public Userbankinfo getUserbankinfo(String loginname, String bankname)
			throws Exception {
		// TODO Auto-generated method stub
		return this.userbankinfoDao.getUserbankinfo(loginname, bankname);
	}

	@Override
	public List getBankinfo(String sql,String username,String bankname) throws Exception {
		return userbankinfoDao.getList(sql,username,bankname);
	}

	@Override
	public String unBindBankinfo(String loginname, String bankno) {

		if(StringUtils.isBlank(loginname) || StringUtils.isBlank(bankno)){
			return "参数异常，请重新操作！";
		}

		bankno = bankno.replace(" ", "");

		DetachedCriteria dc = DetachedCriteria.forClass(Userbankinfo.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("bankno", bankno));
		dc.add(Restrictions.eq("flag", 0));
		List<Userbankinfo> list = this.userbankinfoDao.getHibernateTemplate().findByCriteria(dc);
		if(CollectionUtils.isEmpty(list)){
			return "未查找到该银行卡号，或者该银行卡已经解绑，请刷新页面重新操作！";
		}

		for(Userbankinfo userbankinfo : list){
			userbankinfo.setFlag(1);
			this.userbankinfoDao.getHibernateTemplate().update(userbankinfo);
		}

		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(new Timestamp(System.currentTimeMillis()));
		actionlog.setAction(ActionLogType.MODIFY_BANK_INFO.getCode());
		actionlog.setRemark("用户解绑银行卡卡号：" + bankno);
		this.userbankinfoDao.getHibernateTemplate().save(actionlog);

		return "解绑成功！";
	}

	 

}
