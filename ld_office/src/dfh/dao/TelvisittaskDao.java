package dfh.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Telvisittask;

public class TelvisittaskDao extends BaseDao<Telvisittask, Integer> {
	
	public void taskComplete(int taskid)throws Exception{
		Telvisittask entity = super.getEntity(Telvisittask.class, new Integer(taskid));
		Timestamp endtime = entity.getEndtime();
		Timestamp finishtime=new Timestamp(new Date().getTime());
		if (finishtime.before(endtime)) {
			entity.setIsdelay(0);
			entity.setDelaytime(new Long(0));
		}else{
			long delaytime=finishtime.getTime()-endtime.getTime();
			
			entity.setIsdelay(1);
			entity.setDelaytime(delaytime);
		}
		
		entity.setFinishtime(finishtime);
		entity.setTaskstatus(1);

		super.saveorupdate(entity);
	}

	public int getTaskTotalCount(String taskname,int taskstatus,Date start,Date end)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.eq("taskname", taskname)).add(Restrictions.eq("taskstatus", taskstatus));
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		List list = c.list();
		if (list==null||list.size()<=0) {
			return 0;
		}
		return list.size();
	}
	public int getTaskTotalCount(String taskname,Date start,Date end)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.eq("taskname", taskname));
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		List list = c.list();
		if (list==null||list.size()<=0) {
			return 0;
		}
		return list.size();
	}
	public int getTaskTotalCount(int taskstatus,Date start,Date end)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.eq("taskstatus", taskstatus));
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		List list = c.list();
		if (list==null||list.size()<=0) {
			return 0;
		}
		return list.size();
	}
	public int getTaskTotalCount(Date start,Date end)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		List list = c.list();
		if (list==null||list.size()<=0) {
			return 0;
		}
		return list.size();
	}
	
	public int getTaskTotalCountByLocalSql(String sql)throws Exception{
		
		
		List list = this.getSession().createSQLQuery(sql).list();
		if (list==null||list.size()<=0) {
			return 0;
		}
		return list.size();
	}
	
	public List getListByLocalSql(String sql,int offset,int length)throws Exception{
		List list = this.getSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(length).list();
		return list;
	}
	
	public List getListByLocalSql(String sql){
		List list = this.getSession().createSQLQuery(sql).list();
		return list;
	}

	public List<Telvisittask> getTaskList(String taskname,int taskstatus,Date start,Date end,int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.eq("taskname", taskname)).add(Restrictions.eq("taskstatus", taskstatus));
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		c.addOrder(Order.asc("starttime"));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public List<Telvisittask> getTaskList(int taskstatus,Date start,Date end,int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.eq("taskstatus", taskstatus));
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		c.addOrder(Order.asc("starttime"));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public List<Telvisittask> getTaskList(String taskname,Date start,Date end,int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.eq("taskname", taskname)).add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		c.addOrder(Order.asc("starttime"));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public List<Telvisittask> getTaskList(Date start,Date end,int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Telvisittask.class);
		c.add(Restrictions.ge("addtime", start)).add(Restrictions.lt("addtime", end));
		c.addOrder(Order.asc("starttime"));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
}
