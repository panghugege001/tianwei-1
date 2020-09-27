package dfh.utils;

public class IPEntry {
	public String getBeginIp() {
		return beginIp;
	}

	public void setBeginIp(String beginIp) {
		this.beginIp = beginIp;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String beginIp;
	public String endIp;
	public String country;
	public String area;
	public Integer index;

	/**
	 * 构造函数 author:sun
	 */
	public IPEntry() {
		beginIp = endIp = country = area = "";
	}

	@Override
	public String toString() {
		return "IPEntry [area=" + area + ", beginIp=" + beginIp + ", country=" + country + ", endIp=" + endIp + ", index=" + index + "]";
	}

	

}
