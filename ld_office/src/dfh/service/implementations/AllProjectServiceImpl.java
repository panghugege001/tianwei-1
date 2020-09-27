package dfh.service.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.dao.QqInfFromNetDao;
import dfh.dao.SlaveDao;
import dfh.dao.UniversalDao;
import dfh.model.QqInfFromNet;
import dfh.service.interfaces.AllProjectService;
import dfh.utils.Page;
import dfh.utils.PageQuery;

public class AllProjectServiceImpl extends UniversalServiceImpl implements AllProjectService {

	private QqInfFromNetDao qifnDao;
	private SlaveDao slaveDao;
	private Logger log = Logger.getLogger(AllProjectServiceImpl.class);
	
	public void setSlaveDao(SlaveDao slaveDao){
		this.slaveDao = slaveDao;
	}
	public void setQifnDao(QqInfFromNetDao qifnDao) {
		this.qifnDao = qifnDao;
	}
	public void setUniversalDao(UniversalDao universalDao){
		super.setUniversalDao(universalDao);;
	}
	
	@Override
	public boolean saveOrUpdateQq(QqInfFromNet qif) throws Exception{
		DetachedCriteria cri = DetachedCriteria.forClass(QqInfFromNet.class);
		//cri.setProjection(Projections.projectionList().add(Projections.property("id")));
		cri.add(Restrictions.eq("qq", qif.getQq()));
		cri.add(Restrictions.eq("loginname", qif.getLoginname()));
		List ols = qifnDao.findByCriteria(cri);
		boolean res=false;
		if (null==ols || ols.isEmpty()){
			qifnDao.save(qif);
			res=true;
		} else {
			QqInfFromNet u_qif=(QqInfFromNet)ols.get(0);
			u_qif.setFromCsQq(qif.getFromCsQq());
			u_qif.setCollectTime(qif.getCollectTime());
			u_qif.setLastVisitTime(qif.getLastVisitTime());
			u_qif.setDepositSum(qif.getDepositSum());
			u_qif.setBetSum(qif.getBetSum());
			u_qif.setWin_los(qif.getWin_los());
			qifnDao.update(u_qif);
			res=true;
		}
		return res;
		//QqInfFromNet qif2 = obj==null?null:(QqInfFromNet)obj;
		/*String c_sql = "select qi.id from qq_inf_fromnet qi where qi.qq="+qif.getQq()+" and qi.loginname="+qif.getLoginname();
		Session hs = qifnDao.getHibernateTemplate().getSessionFactory().getCurrentSession();*/
	}
	@Override
	public Map<String, Double> queryUserBill(String loginname) {
		Map<String, Double> bill = new HashMap<String, Double>();
		String sql = "select sum(a1.bettotal) as betSum,sum(a1.amount) as win_los from AgProfit a1 where a1.loginname='"+loginname+"'";
		List play = slaveDao.getHibernateTemplate().find(sql);
		Double betSum=0.0,win_los=0.0;
		Object[] o = (Object[]) play.get(0);
		if (o[0]!=null || o[1]!=null){
			betSum=Double.valueOf(o[0].toString());
			win_los=Double.valueOf(o[1].toString());
		}
		bill.put("betSum", betSum);
		bill.put("win_los", win_los);
		
		sql = "select sum(p1.money) as money from Payorder p1 where p1.loginname='"+ loginname +"' and p1.flag=0 and p1.type=0";
		Double online=0.0,transfer=0.0;
		Object oo = slaveDao.getHibernateTemplate().find(sql).listIterator().next();
		if (null!=oo){
			online=Double.valueOf(oo.toString());
		}
		sql = "select sum(p1.amount) as amount from Proposal p1 where p1.loginname='"+ loginname +"' and p1.type=502 and p1.flag=2";
		Object ot = slaveDao.getHibernateTemplate().find(sql).listIterator().next();
		if (null!=ot){
			transfer=Double.valueOf(ot.toString());
		}
		bill.put("depositSum", online+transfer);
		return bill;
	}
	
	@Override
	public Page queryQQInf(DetachedCriteria criteria, Integer size, Integer pageIndex) {
		Page page = new Page();
		try {
			if ((size == null) || (size.intValue() == 0))
				size = Page.PAGE_DEFAULT_SIZE;
			if (pageIndex == null)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page = PageQuery.queryForPagenation(getHibernateTemplate(), criteria, pageIndex, size, null);
		} catch (Exception e) {
			log.error("queryQQInf error:",e);
		} finally {
			return page;
		}
	}

}
