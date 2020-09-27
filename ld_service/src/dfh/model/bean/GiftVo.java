package dfh.model.bean;

import dfh.model.GiftOrder;

public class GiftVo {

	private Integer id;
	private String title;
	private boolean reach;
	private GiftOrder order;
	
	public GiftVo(){}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isReach() {
		return reach;
	}
	public void setReach(boolean reach) {
		this.reach = reach;
	}
	public GiftOrder getOrder() {
		return order;
	}
	public void setOrder(GiftOrder order) {
		this.order = order;
	}
	
}
