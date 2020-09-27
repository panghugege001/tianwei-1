package dfh.model.enums;
  
/**
 * 会员登陆记录、登陆后操作记录标志
 * @author 
 *
 */
public enum AgErrorType {

	SUCCESS("SUCCESS", "获取成功","00000"),
	ERROR00001("ERROR00001", "缺少輸入參數","00001"),
	ERROR00002("ERROR00002", "輸入參數 fromTime 格式錯誤","00002"),
	ERROR00003("ERROR00003", "輸入參數 toTime 格式錯誤","00003"),
	ERROR00004("ERROR00004", "輸入參數 pageSize 大于 2000","00004"),
	ERROR00008("ERROR00008", "錯誤的 輸入參數 productId","00008"),
	ERROR10009("ERROR10009", "輸入參數 pageSize 小于 1, 或是非整數類","10009"),
	ERROR40002("ERROR40002", "錯誤的 API 驗證 Key","40002"),
	ERROR40003("ERROR40003", "URL 包含了錯誤的 API 名稱","40003"),
	ERROR10001("ERROR10001", "無法連接 Database, 或輸入了錯誤的platformType","10001");
	

	public static String getText(String code) {
		AgErrorType p[] = values();
		for (int i = 0; i < p.length; i++) {
			AgErrorType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	public static String getErrorMsg(String erromsg) {
		AgErrorType p[] = values();
		for (int i = 0; i < p.length; i++) {
			AgErrorType type = p[i];
			if (type.getErrormsg().equals(erromsg))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;
	
	private String errormsg;

	private AgErrorType(String code, String text,String errormsg) {
		this.code = code;
		this.text = text;
		this.errormsg =errormsg;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	
}
