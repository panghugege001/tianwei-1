package dfh.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.dao.LogDao;
import dfh.model.enums.ActionLogType;


/**
 * 自动升级会员等级。新会员存款满三笔，即自动升级为忠实会员
 * 2015-02-24
 */
public class CpuDeviceUtil extends Thread {
	private final Log log = LogFactory.getLog(CpuDeviceUtil.class);
	
	private String loginname;
	private String ioBB;
	private String ip;
	private String city;
	private String clientos;
	
	private LogDao logDao;
	
	public CpuDeviceUtil(String loginname, String ioBB, String ip, String city,
			String clientos, LogDao logDao) {
		super();
		this.loginname = loginname;
		this.ioBB = ioBB;
		this.ip = ip;
		this.city = city;
		this.clientos = clientos;
		this.logDao = logDao;
	}



	@Override
	public void run() {
		//String device = IESnareUtil.getDevice(loginname, ip, ioBB);
		String device = null;
		if(StringUtils.isBlank(device)){
			log.info(ioBB);
		}
		logDao.insertActionLog(loginname, ActionLogType.LOGIN, "ip:"+ ip+";deviceID:"+ioBB+";最后登录地址："+city+";客户操作系统："+clientos);
	}
}
