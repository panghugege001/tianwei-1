package dfh.model.bean;

public class TransferResource {
	private String orderNo;

	  public TransferResource() {

	  }

	  public TransferResource(String orderNo) {
	    this.orderNo = orderNo;
	  }

	  public String getOrderNo() {
	    return orderNo;
	  }

	  public void setOrderNo(String orderNo) {
	    this.orderNo = orderNo;
	  }
}
