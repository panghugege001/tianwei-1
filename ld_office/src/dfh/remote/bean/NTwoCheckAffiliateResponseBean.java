package dfh.remote.bean;

import java.util.HashMap;
import java.util.List;

public class NTwoCheckAffiliateResponseBean {

	private String id;
	private String vendorid;
	private HashMap<String,String> datemaps;//fromdate 和 todate 元素
	private String acode;
	private String date;
	private HashMap<String,String> acodemaps;
	private List<HashMap<String,String>> playerList;
	private String errdesc;
	
	
	public NTwoCheckAffiliateResponseBean(String id, String vendorid,
			HashMap<String, String> datemaps, String acode, String date,
			HashMap<String, String> acodemaps,
			List<HashMap<String, String>> playerList, String errdesc) {
		super();
		this.id = id;
		this.vendorid = vendorid;
		this.datemaps = datemaps;
		this.acode = acode;
		this.date = date;
		this.acodemaps = acodemaps;
		this.playerList = playerList;
		this.errdesc = errdesc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVendorid() {
		return vendorid;
	}
	public void setVendorid(String vendorid) {
		this.vendorid = vendorid;
	}
	public HashMap<String, String> getDatemaps() {
		return datemaps;
	}
	public void setDatemaps(HashMap<String, String> datemaps) {
		this.datemaps = datemaps;
	}
	public String getAcode() {
		return acode;
	}
	public void setAcode(String acode) {
		this.acode = acode;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public HashMap<String, String> getAcodemaps() {
		return acodemaps;
	}
	public void setAcodemaps(HashMap<String, String> acodemaps) {
		this.acodemaps = acodemaps;
	}
	public List<HashMap<String, String>> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(List<HashMap<String, String>> playerList) {
		this.playerList = playerList;
	}
	public String getErrdesc() {
		return errdesc;
	}
	public void setErrdesc(String errdesc) {
		this.errdesc = errdesc;
	}

}
