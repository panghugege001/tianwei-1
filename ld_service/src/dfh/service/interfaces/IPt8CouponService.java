package dfh.service.interfaces;

import dfh.model.PtCoupon;


public interface IPt8CouponService {
	
	public PtCoupon queryPtCoupon(String code);
	
	public void updatePtCoupon(PtCoupon ptCoupon) ;
}
