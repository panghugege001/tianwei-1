package dfh.service.implementations;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.dao.Pt8CouponDao;
import dfh.dao.SeqDao;
import dfh.model.PtCoupon;
import dfh.service.interfaces.IPt8CouponService;

public class Pt8CouponServiceImpl implements IPt8CouponService {
	
	private Pt8CouponDao pt8CouponDao ;
	

	@Override
	public PtCoupon queryPtCoupon(String code) {
		DetachedCriteria dc = DetachedCriteria.forClass(PtCoupon.class);
		dc.add(Restrictions.eq("code", code));
		List<PtCoupon> list = pt8CouponDao.findByCriteria(dc);
		if(list!=null && list.size()>0 && list.get(0) !=null ){
			return (PtCoupon) list.get(0) ;
		}else{
			return null ;
		}
	}

	@Override
	public void updatePtCoupon(PtCoupon ptCoupon) {
		pt8CouponDao.saveOrUpdate(ptCoupon);;
	}
	
	public Pt8CouponDao getPt8CouponDao() {
		return pt8CouponDao;
	}
	
	public void setPt8CouponDao(Pt8CouponDao pt8CouponDao) {
		this.pt8CouponDao = pt8CouponDao;
	}
}
