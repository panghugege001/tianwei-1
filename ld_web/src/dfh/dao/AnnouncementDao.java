package dfh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.model.Announcement;
import dfh.model.enums.AnnouncementType;

public class AnnouncementDao extends UniversalDao {
	
	public List findAll(int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Announcement.class);
		c.add(Restrictions.eq("type", AnnouncementType.INDEX.getCode())).addOrder(Order.desc("createtime"));
		return c.setFirstResult(offset).setMaxResults(length).list();
	}
	
	public int getCount()throws Exception{
		return super.getCount(Announcement.class).intValue();
	}
	
	public List query() {
		// TODO Auto-generated method stub
		Criteria c = this.getSession().createCriteria(Announcement.class);
		return c.addOrder(Order.desc("createtime")).setMaxResults(7).list();
		
	}
	
	public List queryTopNews(){
		Criteria c = this.getSession().createCriteria(Announcement.class);
		c.add(Restrictions.eq("type", AnnouncementType.INDEX_TOP.getCode()));
		return c.addOrder(Order.desc("createtime")).setMaxResults(5).list();
	}
	

	public List queryAll(int offset, int length) {
		// TODO Auto-generated method stub
		Criteria c = this.getSession().createCriteria(Announcement.class);
		c.addOrder(Order.desc("createtime"));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public int totalCount() {
		// TODO Auto-generated method stub
		Criteria c = this.getSession().createCriteria(Announcement.class);
		c.setProjection(Projections.rowCount());
		return ((Integer)c.uniqueResult()).intValue();
	}

}
