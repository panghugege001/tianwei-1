package dfh.dao;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class NetPayDao extends HibernateDaoSupport {
	
	@SuppressWarnings("unchecked")
	public List getAgentReferralsCountAtPayorder(String agentName,Date start,Date end)throws Exception{
		String sql="select p.loginname,sum(money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.flag=0 group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		return q.list();
	}
}
