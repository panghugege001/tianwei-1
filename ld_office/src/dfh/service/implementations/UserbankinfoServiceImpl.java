package dfh.service.implementations;

import java.io.Serializable;
import java.util.List;

import dfh.dao.UserbankinfoDao;
import dfh.model.Userbankinfo;
import dfh.service.interfaces.IUserbankinfoService;


public class UserbankinfoServiceImpl implements IUserbankinfoService {
	
	private UserbankinfoDao userbankinfoDao;

	public UserbankinfoDao getUserbankinfoDao() {
		return userbankinfoDao;
	}

	public void setUserbankinfoDao(UserbankinfoDao userbankinfoDao) {
		this.userbankinfoDao = userbankinfoDao;
	}

	@Override
	public List<Userbankinfo> getUserbankinfoList(Userbankinfo userbankinfo,int pageno,int length)
			throws Exception {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		return this.userbankinfoDao.getUserbankinfoList(userbankinfo, offset, length);
	}

	@Override
	public boolean unbanding(int id) throws Exception {
		// TODO Auto-generated method stub
		return this.userbankinfoDao.update(id);
	}

	@Override
	public int getTotalCount(Userbankinfo userbankinfo) throws Exception {
		// TODO Auto-generated method stub
		List<Userbankinfo> userbankinfoList = this.userbankinfoDao.getUserbankinfoList(userbankinfo);
		return userbankinfoList==null||userbankinfoList.size()<=0?0:userbankinfoList.size();
	}

	@Override
	public List<Userbankinfo> getUserbankinfoList(String username,
			String bankname, Integer flag, int pageno, int length)
			throws Exception {
		// TODO Auto-generated method stub
		String hql="from Userbankinfo where 1=1";
		if (username!=null&&!username.trim().equals("")) {
			hql+=" and loginname='"+username+"'";
		}
		if (bankname!=null&&!bankname.equals("")) {
			hql+=" and bankname='"+bankname+"'";
		}
		if (flag!=null) {
			hql+=" and flag="+flag;
		}
		hql+="order by addtime desc";
		int offset=(pageno-1)*length;
		return this.userbankinfoDao.getUserbankinfoList(hql, offset, length);
	}

	@Override
	public int getTotalCount(String username,
			String bankname, Integer flag) throws Exception {
		// TODO Auto-generated method stub
		String hql="from Userbankinfo where 1=1";
		if (username!=null&&!username.trim().equals("")) {
			hql+=" and loginname='"+username+"'";
		}
		if (bankname!=null&&!bankname.equals("")) {
			hql+=" and bankname='"+bankname+"'";
		}
		if (flag!=null) {
			hql+=" and flag="+flag;
		}
		hql+="order by addtime desc";
		List<Userbankinfo> list = this.userbankinfoDao.getUserbankinfoList(hql);
		return list==null||list.size()<=0?0:list.size();
	}

	public Object save(Object obj){
		return userbankinfoDao.save(obj);
	}

	public Object get(Class clazz, Serializable id) {
		return userbankinfoDao.get(clazz, id);
	} 

}
