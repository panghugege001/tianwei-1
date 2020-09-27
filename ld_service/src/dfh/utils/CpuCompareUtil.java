package dfh.utils;

import java.util.Date;

import dfh.model.CpuCompare;
import dfh.service.interfaces.CustomerService;

/**
 * 保存iesnare 采集计算的的设备ID和我们自己采集计算出的设备ID
 *
 */
public class CpuCompareUtil extends Thread {

	private CustomerService cs;
    private String username;
    private String ip;
    private String iesnare;
    private String ourdevice;
    private String reamrk;
    
    public CpuCompareUtil(CustomerService cs, String username, String ip, String iesnare, String ourdevice, String remark){
    	this.cs = cs;
    	this.username = username;
    	this.ip = ip;
    	this.iesnare = iesnare;
    	this.ourdevice = ourdevice;
    	this.reamrk = remark;
    }
    
    public void saveCpuCompareInfo(){
    	CpuCompare cpuCompare = new CpuCompare(username, ip, iesnare, ourdevice, new Date(), reamrk);
    	cs.save(cpuCompare);
    }
    
    public CustomerService getCs() {
		return cs;
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIesnare() {
		return iesnare;
	}

	public void setIesnare(String iesnare) {
		this.iesnare = iesnare;
	}

	public String getOurdevice() {
		return ourdevice;
	}

	public void setOurdevice(String ourdevice) {
		this.ourdevice = ourdevice;
	}

	public String getReamrk() {
		return reamrk;
	}

	public void setReamrk(String reamrk) {
		this.reamrk = reamrk;
	}

	@Override
    public void run() {
		saveCpuCompareInfo();
    }
}
