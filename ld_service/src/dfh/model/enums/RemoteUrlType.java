package dfh.model.enums;

public enum RemoteUrlType
{
	URL_COMMON_MONEY("URL_COMMON_MONEY", "普通真钱会员远程接口地址"), 
	URL_VIP_ZUANSHI("URL_VIP_ZUANSHI", "钻石VIP远程接口地址"), 
	URL_VIP_BAIJIN("URL_VIP_BAIJIN", "白金VIP远程接口地址"), 
	URL_FREE("URL_FREE", " 试玩会员远程接口地址"), 
	URL_OFFICE("URL_OFFICE", "后台使用远程接口地址");

	public static String getText(String code) {
	    RemoteUrlType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      RemoteUrlType type = p[i];
	      if (type.getCode().equals(code))
	        return type.getText();
	    }
	    return null;
	  }
	private String code;

	private String text;

	public static void main(String[] args) {

	}
  private RemoteUrlType(String code, String text) {
	this.code = code;
	this.text = text;
}

  public String getCode()
  {
    return this.code;
  }

  public String getText() {
    return this.text;
  }
}
