package dfh.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.opensymphony.oscache.util.StringUtil;

import dfh.model.Telvisit;
import dfh.utils.DateUtil;

public class TelvisitDao extends BaseDao<Telvisit, Integer> {

	public void endvisit(int visitid,int execstatus)throws Exception{
		String hql="update Telvisit set islock=0,execstatus=? where id=?";
		Query q = this.getSession().createQuery(hql);
		q.setParameter(0, execstatus).setParameter(1, visitid);
		q.executeUpdate();
	}
	
	public void startvisit(int visitid,String operator)throws Exception{
		String hql="update Telvisit set locker=?,islock=1,execstatus=3 where id=?";
		Query q = this.getSession().createQuery(hql);
		q.setParameter(0, operator).setParameter(1, visitid);
		q.executeUpdate();
	}
	
	public Object getTaskDetailList(int taskid,int offset,int length)throws Exception{
		String sql="select v.id,v.loginname,v.taskid,v.islock,v.execstatus,v.locker,u.aliasname,u.accountname,u.email,u.phone,u.createtime,u.lastlogintime,ROUND((UNIX_TIMESTAMP()-UNIX_TIMESTAMP(lastLoginTime))/60/60/24,0),u.intro,u.qq from users u inner join telvisit v on v.loginname=u.loginname where v.taskid=? order by u.lastlogintime desc";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, taskid);
		q.setFirstResult(offset).setMaxResults(length);
		return q.list();
	}
	
	public Object getTaskDetailList(String loginname,String phone,String email,int islock,int visitResult,Date start,Date end,int taskid,String intro,int offset,int length)throws Exception{
		StringBuffer sql=new StringBuffer("select v.id,v.loginname,v.taskid,v.islock,v.execstatus,v.locker,u.aliasname,u.accountname,u.email,u.phone,u.createtime,u.lastlogintime,ROUND((UNIX_TIMESTAMP()-UNIX_TIMESTAMP(lastLoginTime))/60/60/24,0),u.intro,u.qq from users u inner join telvisit v on v.loginname=u.loginname where v.taskid=? ");
		if (!StringUtil.isEmpty(loginname)) {
			sql.append(" and v.loginname='"+loginname+"'");
		}
		if (!StringUtil.isEmpty(phone)) {
			sql.append(" and u.phone='"+phone+"'");
		}
		if (!StringUtil.isEmpty(email)) {
			sql.append(" and u.email='"+email+"'");
		}
		if (!StringUtil.isEmpty(intro)) {
			sql.append(" and u.intro='"+intro+"'");
		}
		if (start!=null && end!=null ) {
			sql.append(" and u.createtime >='"+DateUtil.formatDateForStandard(start)+"'");
			sql.append(" and u.createtime < '"+DateUtil.formatDateForStandard(end)+"'");
		}
		if (islock!=-1) {
			sql.append(" and v.islock="+islock);
		}
		if(visitResult!=-1) {
			sql.append(" and v.execstatus="+visitResult);
		}
		
		sql.append(" order by u.lastlogintime desc");
		Query q = this.getSession().createSQLQuery(sql.toString()).setParameter(0, taskid);
		q.setFirstResult(offset).setMaxResults(length);
		return q.list();
	}
	
	
	public int getTaskDetailListCount(String loginname,String phone,String email,int islock,int visitResult,Date start,Date end,int taskid,String intro)throws Exception{
		StringBuffer sql=new StringBuffer("select v.id,v.loginname,v.taskid,v.islock,v.execstatus,v.locker,u.aliasname,u.accountname,u.email,u.phone,u.createtime,u.lastlogintime,ROUND((UNIX_TIMESTAMP()-UNIX_TIMESTAMP(lastLoginTime))/60/60/24,0),u.intro,u.qq from users u inner join telvisit v on v.loginname=u.loginname where v.taskid=? ");
		if (!StringUtil.isEmpty(loginname)) {
			sql.append(" and v.loginname='"+loginname+"'");
		}
		if (!StringUtil.isEmpty(phone)) {
			sql.append(" and u.phone='"+phone+"'");
		}
		if (!StringUtil.isEmpty(email)) {
			sql.append(" and u.email='"+email+"'");
		}
		if (!StringUtil.isEmpty(intro)) {
			sql.append(" and u.intro='"+intro+"'");
		}
		if (start!=null && end!=null ) {
			sql.append(" and u.createtime >='"+DateUtil.formatDateForStandard(start)+"'");
			sql.append(" and u.createtime < '"+DateUtil.formatDateForStandard(end)+"'");
		}
		if (islock!=-1) {
			sql.append(" and v.islock="+islock);
		}
		if(visitResult!=-1) {
			sql.append(" and v.execstatus="+visitResult);
		}
		
		sql.append(" order by u.lastlogintime desc");
		Query q = this.getSession().createSQLQuery(sql.toString()).setParameter(0, taskid);
		List list = q.list();
		if (list==null||list.size()<=0) {
			return 0;
		}
		return list.size();
	}

}
