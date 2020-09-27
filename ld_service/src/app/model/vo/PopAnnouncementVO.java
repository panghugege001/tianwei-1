package app.model.vo;


public class PopAnnouncementVO  implements java.io.Serializable {
	 private static final long serialVersionUID = -3132726349012147359L;
     private String typeName;//公告标题
     private String itemNo;//001 :文本公告   002：图片公告
     private String value;//文本公告时为文本  ，图片公告时为图片地址
     private String note ;//文本公告时没啥意义，图片公告时图片点击跳转的URL
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}