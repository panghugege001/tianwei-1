package dfh.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;


public class GameinfoDao extends UniversalDao {
	
	public void updateXimaStatus(String loginname, Date startTime, Date endTime) throws Exception {
		String hql="update GGameinfo set flag=1 where loginname=? and starttime>=? and starttime<?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, loginname).setParameter(1, startTime).setParameter(2, endTime);
		query.executeUpdate();
	}
	
	public void updateXimaStatus(Date startTime, Date endTime) throws Exception {
//		String hql="update GGameinfo set flag=1 where starttime>=? and starttime<?";
		String hql="delete from GGameinfo where starttime < ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, endTime);
		query.executeUpdate();
	}
	
	/**
	 * 每周一12点至00点之前执行；
	 * 获取一周之内的所有会员的有效投注额及会员账号集合;
	 * 执行时间是否为每周的周一，由外部程序控制
	 * @param startTime
	 * @param endTime
	 * @return List
	 * @throws Exception
	 */
	public List<Object[]> getXimaObject(Date startTime,Date endTime)throws Exception{
		String hql="select loginname,sum(validbetamount) from g_gameinfo where flag=0 and starttime>=? and starttime<? group by loginname";
		Query query = this.getSession().createSQLQuery(hql);
		query.setParameter(0, startTime).setParameter(1, endTime);
		List list = query.list();
		if (list==null||list.size()<=0) {
			return null;
		}
		return list;
	}

}
