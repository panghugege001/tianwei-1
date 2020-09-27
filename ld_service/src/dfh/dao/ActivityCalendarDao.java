package dfh.dao;

import dfh.model.ActivityCalendar;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by wander on 2016/12/20.
 */
public class ActivityCalendarDao extends UniversalDao {

    public List queryTopActivity(Integer top) {
        Criteria c = this.getSession().createCriteria(ActivityCalendar.class);
        c.add(Restrictions.eq("flag", 1));
        return c.addOrder(Order.desc("createtime")).setMaxResults(top).list();
    }
}
