package dfh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Announcement;
import dfh.model.enums.AnnouncementType;

public class AnnouncementDao extends UniversalDao {
	
	public List findAll(int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Announcement.class);
		c.addOrder(Order.desc("createtime"));
		return c.setFirstResult(offset).setMaxResults(length).list();
	}
	
	public int getCount()throws Exception{
		return super.getCount(Announcement.class).intValue();
	}

}
