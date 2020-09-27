package dfh.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nnti.office.model.auth.Operator;

// Referenced classes of package dfh.dao:
//			UniversalDao

public class OperatorDao extends UniversalDao {

	private static Logger log = Logger.getLogger(OperatorDao.class);

	public OperatorDao() {
	}

	public String checkOperator(Operator operator) {
		String msg = null;
		try {
			if (operator == null) {
				msg = "该操作员不存在";
				log.warn(msg);
			} else if (operator.getEnabled().equals("false")) {
				msg = "该操作员已经被禁用";
				log.warn(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}
	
	public List getEmployees(String role,int offset,int length){
		Criteria c = this.getSession().createCriteria(Operator.class);
		if (role!=null&&!role.equals("")) {
			c.add(Restrictions.like("authority", role));
		}
		c.setFirstResult(offset).setMaxResults(length).add(Restrictions.ne("username", "admin")).addOrder(Order.desc("createTime"));
		return c.list();
	}
	
	public List getEmployees(int offset,int length){
		return this.getEmployees("", offset, length);
	}

}
