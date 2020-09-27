package dfh.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Telvisit;
import dfh.model.Telvisitremark;

public class TelvisitremarkDao extends BaseDao<Telvisitremark, Integer> {
	
	
	@SuppressWarnings("unchecked")
	public List<Telvisitremark> getTelvisitRemark(int telvisitid, int offset,
			int length) throws Exception {
		Criteria c = this.getSession().createCriteria(Telvisitremark.class);
		c.add(Restrictions.eq("telvisitid", telvisitid));
		c.addOrder(Order.desc("addtime"));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public int getNumberCount(int taskid,int execstatus){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+"  ");
		//sql.append(" and telvisitremark.execstatus = "+ execstatus);
		//sql.append(" group by operator ");
		if(execstatus == -1){
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}else{
			sql.append(" and telvisitremark.execstatus = "+execstatus);
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}
	}
	
	public int getNumberCountWithOperator(int taskid,int execstatus,String operator){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" and telvisitremark.operator='"+operator+"' ");
		//sql.append(" and telvisitremark.execstatus = "+ execstatus);
		//sql.append(" group by operator ");
		if(execstatus == -1){
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}else{
			sql.append(" and telvisitremark.execstatus = "+execstatus);
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}
		
	}
	
	public int getSuccessNumberCount(int taskid,int execstatus){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" ");
		//sql.append(" and telvisitremark.execstatus = "+ execstatus);
		//sql.append(" group by operator ");
		
		if(execstatus ==-1 || execstatus==0){
			sql.append(" and telvisitremark.execstatus = 1");
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	public int getSuccessNumberCountWithOperator(int taskid,int execstatus,String operator){
		int number =0;
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" and telvisitremark.operator='"+operator+"' ");
		//sql.append(" and telvisitremark.execstatus = "+ execstatus);
		//sql.append(" group by operator ");
		if(execstatus ==-1 || execstatus==1){
			sql.append(" and telvisitremark.execstatus = 1");
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	public int getFailNumberCount(int taskid,int execstatus){
		int number =0;
//		Criteria c= this.getSession().createCriteria(Telvisit.class);
//		c.add(Restrictions.eq("taskid", taskid));
//		List<Telvisit> list = c.list();
//		for (Telvisit telvisit : list) {
//			Criteria cc = this.getSession().createCriteria(Telvisitremark.class);
//			cc.add(Restrictions.eq("telvisitid", telvisit.getId()));
//			if(execstatus ==-1 || execstatus==0){
//				number += cc.add(Restrictions.eq("execstatus", 0)).list().size();
//			}else{
//				number += 0;
//			}
//		}
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" ");
		//sql.append(" and telvisitremark.execstatus = "+ execstatus);
		//sql.append(" group by operator ");
		
		if(execstatus ==-1 || execstatus==0){
			sql.append(" and telvisitremark.execstatus = 0");
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	public int getFailNumberCountWithOperator(int taskid,int execstatus,String operator){
		int number =0;
//		Criteria c= this.getSession().createCriteria(Telvisit.class);
//		c.add(Restrictions.eq("taskid", taskid));
//		List<Telvisit> list = c.list();
//		for (Telvisit telvisit : list) {
//			Criteria cc = this.getSession().createCriteria(Telvisitremark.class);
//			cc.add(Restrictions.eq("telvisitid", telvisit.getId()));
//			cc.add(Restrictions.eq("operator", operator));
//			if(execstatus ==-1 || execstatus==0){
//				number += cc.add(Restrictions.eq("execstatus", 0)).list().size();
//			}else{
//				number += 0;
//			}
//		}
		StringBuilder sql = new StringBuilder();
		sql.append("select count(operator) from telvisitremark ");
		sql.append(" left join  telvisit");
		sql.append(" on telvisitremark.telvisitid = telvisit.id ");
		sql.append(" left join  telvisittask ");
		sql.append(" on telvisittask.id=telvisit.taskid ");
		sql.append(" where telvisittask.id = "+taskid+" and telvisitremark.operator='"+operator+"' ");
		//sql.append(" and telvisitremark.execstatus = "+ execstatus);
		//sql.append(" group by operator ");
		if(execstatus ==-1 || execstatus==0){
			sql.append(" and telvisitremark.execstatus = 0");
			List numbers = this.getSession().createSQLQuery(sql.toString()).list();
			if(numbers.size() !=0 ){
				return ((BigInteger )numbers.get(0)).intValue();
			}else{
				return 0;
			}
		}else{
			return 0;
		}

	}
	 

}
