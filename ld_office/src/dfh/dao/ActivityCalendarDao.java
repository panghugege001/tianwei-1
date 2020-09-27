package dfh.dao;

import dfh.model.ActivityCalendar;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import java.util.List;

public class ActivityCalendarDao extends UniversalDao {

    public List findAll(int offset, int length) throws Exception {
        Criteria c = this.getSession().createCriteria(ActivityCalendar.class);
        c.addOrder(Order.asc("createtime"));
        return c.setFirstResult(offset).setMaxResults(length).list();
    }

    public int getCount() throws Exception {
        return super.getCount(ActivityCalendar.class).intValue();
    }

}
