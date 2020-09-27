package dfh.mgs.vo.freegame;


/**
 * The unique identifier of the offer status in the Microgaming system.
 *  
 * MG 中offer status 唯一标识符
 */
public enum OfferStatusId {
	
	ALL(-1, "全部"),
	UserRejected(0, "玩家已拒绝"),
	ValidOffers(1, "有效的Offer"),
	PendingCancellation(2, "待取消"),
	OperatorCancelled(3, "已取消"),    
	ReachedLimit(4, "已达到游戏数量限制"),
	OfferExpired(5, "优惠已过期"),
	OperatorReactivated(6, "商户激活"),
	OfferPendingSuspension(7, "待暂停"),
	OfferSuspended(8, "已暂停优惠");
	
	private Integer code;
	private String desc;

	private OfferStatusId(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public static String getDesc(String code) {
		OfferStatusId[] p = values();
		for (int i = 0; i < p.length; ++i) {
			OfferStatusId type = p[i];
			if (type.getCode().equals(code))
				return type.getDesc();
		}
		return null;
	}
}
