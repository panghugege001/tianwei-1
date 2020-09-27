package app.request;

/**
 * 
 * @author stan
 *
 */
public abstract class BaseBean {
	private String version;
	private String plat;
	private String sid;
	private String product;
	private String agcode;
	private String token;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getAgcode() {
		return agcode;
	}
	public void setAgcode(String agcode) {
		this.agcode = agcode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}