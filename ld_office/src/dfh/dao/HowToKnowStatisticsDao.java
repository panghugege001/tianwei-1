package dfh.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

import dfh.model.Users;

public class HowToKnowStatisticsDao extends UniversalDao {
	
	
	/**
	 * 根据时间范围、来源网址，获取用户记录集合，根据来源网址进行分组；暂未提供明细查询接口。
	 * @param start
	 * @param end
	 * @param url  来源网址
	 * @param offset
	 * @param length
	 * @return List<k>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object searchUserRecord(Date start,Date end,int offset,int length)throws Exception{
		String sql="select howToKnow,count(*) count from users where createtime >=? and createtime < ? and howToKnow <> '' group by howToKnow order by count desc ";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end);
		q.setFirstResult(offset).setMaxResults(length);
		return q.list();
	}
	
	/**
	 * 根据时间范围、来源网址，获取用户记录集合，根据来源网址进行分组；暂未提供明细查询接口。
	 * @param start
	 * @param end
	 * @param url  来源网址
	 * @param offset
	 * @param length
	 * @return List<Users>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object searchUserRecord(Date start,Date end,String url,int offset,int length)throws Exception{
		if (url==null||url.equals("")) {
			return this.searchUserRecord(start, end, offset, length);
		}
		String sql="select howToKnow,count(*) count from users where createtime >=? and createtime < ? and howToKnow = ? and howToKnow <> '' order by count desc ";
		SQLQuery q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, start).setParameter(1, end).setParameter(2, url);
		q.setFirstResult(offset).setMaxResults(length);
		return q.list();
	}
	
	
	
	/**
	 * 根据时间范围、来源URL获取总用户记录数
	 * @param start
	 * @param end
	 * @param url 来源网址
	 * @return int 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int getTotalRecordCount(Date start,Date end,String url)throws Exception{
		Criteria c = this.getSession().createCriteria(Users.class);
		c.add(Restrictions.ge("createtime", new Timestamp(start.getTime()))).add(Restrictions.lt("createtime", new Timestamp(end.getTime())));
		c.add(Restrictions.ne("howToKnow", ""));
		if (url!=null&&!url.equals("")) {
			c.add(Restrictions.eq("howToKnow", url));
		}
		List list = c.list();
		if (list!=null&&list.size()>0) {
			return list.size();
		}
		return 0;
	}
	

}
