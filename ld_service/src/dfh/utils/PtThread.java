package dfh.utils;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import dfh.model.PTExport;
import dfh.model.PTTransfer2NewHistory;
import dfh.model.Users;
import dfh.service.interfaces.CustomerService;

public class PtThread extends Thread{
	
	private static Logger log = Logger.getLogger(PtThread.class);
	private CustomerService cs;
	private Users user;
	private String loginname;
	private String password;
	private String methods;
	
	public PtThread(CustomerService cs,Users user,String loginname,String password,String methods){
		this.cs=cs;
		this.user=user;
		this.loginname=loginname;
	    this.password=password;
	    this.methods=methods;
	}
	
	public void run(){
		/*log.info(PtUtil.getPlayerLoginOut(loginname));
		String phpHtml = PtUtil.createPlayerName(loginname, password);
		JSONObject jsonObj = JSONObject.fromObject(phpHtml);
		if (jsonObj.containsKey("result")) {
			//user.setPtlogin(1);
			//memberService.update(user);
			log.info(loginname + " 创建PT成功1：" + loginname);
		}else if(jsonObj.containsKey("errorcode")){
			String code=jsonObj.getString("errorcode");
			if(code!=null && !code.equals("")){
				//user.setPtlogin(1);
				//memberService.update(user);
				log.info(loginname + " 已经存在2：" + loginname);
			}else{
				log.info(loginname + " 创建失败3：" + jsonObj);
			}
		}else{
			log.info(loginname + " 创建失败4：" + jsonObj);
		}*/
//		log.info(PtUtil.updatePlayerPassword(loginname, password));
		PTTransfer2NewHistory ptth = (PTTransfer2NewHistory) cs.get(PTTransfer2NewHistory.class, (PtUtil.PALY_START + loginname).toUpperCase());
		if(ptth == null){
			String phpHtml = PtUtil.createPlayerName(loginname, password);
			JSONObject jsonObj = JSONObject.fromObject(phpHtml);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " ************************登录官网创建PT成功：" + loginname);
			}else{
				log.info(loginname + " ************************登录官网创建失败：" + jsonObj);
			}
			PTTransfer2NewHistory ptthNew = new PTTransfer2NewHistory((PtUtil.PALY_START + loginname).toUpperCase(), new Date());
			Double balance = PtUtil.getPlayerOldBalance(loginname);
			if(null != balance){
				ptthNew.setMoney(balance);
				ptthNew.setCreateTime(new Date());
				if(balance > 0){
					Boolean flag = PtUtil.getDepositPlayerMoney(loginname, balance);
					Boolean flagold = PtUtil.withdrawOldPlayerMoney(loginname, balance);
					if(flag){
						ptthNew.setRemark("success,oldwithdraw:"+flagold);
						cs.save(ptthNew);
					}
				}else{
					ptthNew.setRemark("success");
					cs.save(ptthNew);
				}
				
			}
		}
		
		/*PTExport exportInfo = (PTExport) cs.get(PTExport.class, ("E"+loginname).toUpperCase());
		if(null != exportInfo && exportInfo.getIsdelete() != 1){
			exportInfo.setIsdelete(1);
			exportInfo.setUpdatetime(new Date());
			cs.update(exportInfo);
			Boolean flag = PtUtil.getDepositPlayerMoney(loginname, exportInfo.getRealbalance());
			exportInfo.setRemark(flag == true?"success":"error");
			cs.update(exportInfo);
		}*/
	}

}
