package dfh.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.IESnare;
import dfh.model.Users;
import dfh.service.interfaces.TransferService;

public class CpuValidateUtil extends Thread {

	private static Logger log = Logger.getLogger(CpuValidateUtil.class);

	private TransferService transferService;
	private Users user;
	private String ioBB;
	private String ip;

	public CpuValidateUtil(TransferService transferService, Users user, String ioBB, String ip) {
		super();
		this.transferService = transferService;
		this.user = user;
		this.ioBB = ioBB;
		this.ip = ip;
	}

	public void run() {
		String device = IESnareUtil.getDevice(user.getLoginname(), ip, ioBB);
		if(StringUtils.isNotBlank(device)){
			DetachedCriteria dc = DetachedCriteria.forClass(IESnare.class);
			dc.add(Restrictions.eq("device", device)) ;
			List<IESnare> list = transferService.findByCriteria(dc) ;
			if(null != list && list.size()>0){
				user.setFlag(1);
				user.setWarnflag(2);
				user.setRemark(user.getRemark()+" ，由于cpu信息重复，冻结该玩家("+device+")");
				transferService.update(user);
			}
		}else{
			user.setFlag(1);
			user.setWarnflag(2);
			user.setRemark(user.getRemark()+" ，由于cpu信息为空，冻结该玩家("+device+")");
			transferService.update(user);
		}
	}
}
