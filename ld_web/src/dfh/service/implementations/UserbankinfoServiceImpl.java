package dfh.service.implementations;

import java.util.Date;
import java.util.List;

import dfh.dao.UserbankinfoDao;
import dfh.model.Userbankinfo;
import dfh.service.interfaces.IUserbankinfoService;
import dfh.utils.DateUtil;

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

	 

}
