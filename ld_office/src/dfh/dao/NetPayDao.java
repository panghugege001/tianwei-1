package dfh.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.action.vo.NetPayVO;
import dfh.model.New_NetPay;

public class NetPayDao extends HibernateDaoSupport {
	
	@SuppressWarnings("unchecked")
	public List getAgentReferralsDetailAtPayorder(String agentName,Date start,Date end,int offset,int length)throws Exception{
		String sql="select p.loginname,sum(money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.flag=0 group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		q.setFirstResult(offset).setMaxResults(length);
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List getAgentReferralsCountAtPayorder(String agentName,Date start,Date end)throws Exception{
		/*
		String sql="select count(distinct p.loginname),sum(p.money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and flag=0";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		return (Object[]) q.uniqueResult();
		*/
		
		String sql="select p.loginname,sum(money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.flag=0 group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		return q.list();
	}
	
	public New_NetPay findByInner_serialNo(String _inner_serialNo){
		Criteria c = this.getSession().createCriteria(New_NetPay.class);
		c.add(Restrictions.eq("inner_serialNo", _inner_serialNo));
		Object object=c.uniqueResult();
		if (object!=null) {
			return (New_NetPay) object;
		}
		return null;
	}
	
	
	public void save(NetPayVO vo){
		
		Date paydate=null;
		try {
			paydate=yyyy_MM_dd_HH_mm_ss.parse(vo.getPayDate());
		} catch (Exception e) {
			// TODO: handle exception
			paydate=new Date();
		}
		New_NetPay netpay=new New_NetPay( vo.getUsername(), vo.getInner_serialNo(),
				vo.getPayPlatform_serialNo(), vo.getPayPlatform_Name(),
				vo.getBankOrderNo(), Double.parseDouble(vo.getAmount()),
				paydate,this.formatDate(vo.getInformDate()),
				vo.getThirdpartyID(), Integer.parseInt(vo.getPayType()),
				Integer.parseInt(vo.getPayResult()),vo.getBankName());
		
		this.getSession().save(netpay);
	}
	
	
	private static SimpleDateFormat yyyyMMddHHmmss=new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat yyyy_MM_dd_HH_mm_ss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private  Date formatDate(String date){
		try {
			return yyyy_MM_dd_HH_mm_ss.parse(yyyy_MM_dd_HH_mm_ss.format(yyyyMMddHHmmss.parse(date)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return new Date();
		}
	}
	
	
	

}
