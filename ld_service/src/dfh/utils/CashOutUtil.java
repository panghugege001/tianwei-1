package dfh.utils;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.AgentVip;
import dfh.model.Users;
import dfh.model.enums.VipLevel;
import dfh.service.interfaces.ProposalService;
import dfh.utils.sendsms.SendPhoneMsgUtil;

public class CashOutUtil {
	
	private static Logger log = Logger.getLogger(CashOutUtil.class);

	public synchronized String execute(ProposalService proposalService, String proposer, String loginname, String pwd,
			String title, String from, Double money, String accountName, String accountNo, String accountType,
			String bank, String accountCity, String bankAddress, String email, String phone, String ip, String remark,
			String msflag, Users user, Double splitAmount , String tkType , Double gameTolMoney) {
		String msg;
		if(user.getRole().equals("AGENT") && user.getWarnflag()==2){
			msg = "请您联系代理专员，谢谢！";
			return msg ;
		}
		if (user.getLevel() >= VipLevel.TIANJIANG.getCode() && msflag.equals("1") && money > splitAmount && user.getRole().equals("MONEY_CUSTOMER")) {
			msg = proposalService.addCashoutNew(proposer, loginname, pwd, title, from, money, accountName, accountNo,
					accountType, bank, accountCity, bankAddress, email, phone, ip, remark, msflag,gameTolMoney);
		} else {
			if(StringUtils.isNotBlank(tkType) && tkType.equals("slotmachine") && user.getRole().equals("AGENT")){
				msg = proposalService.addCashoutForAgentSlot(proposer, loginname, pwd, title, from, money, accountName, accountNo,
						accountType, bank, accountCity, bankAddress, email, phone, ip, "代理老虎机佣金提款", msflag);
			}else{
				if(StringUtils.isNotBlank(tkType) && tkType.equals("liveall") && user.getRole().equals("AGENT")){
					int date = Calendar.getInstance().get(Calendar.DATE);
					DetachedCriteria dc = DetachedCriteria.forClass(AgentVip.class);
					dc.add(Restrictions.eq("id.agent", user.getLoginname())) ;
					dc.addOrder(Order.desc("createtime")) ;
					List<AgentVip> vips = proposalService.findByCriteria(dc);
					if(!(date>=1 && date<=5)){
						if(null == vips || vips.size()==0){
							msg = "因代理等级不够不能提款，请多多努力";
							return msg ;
						}
					}
					if(!(date>=1 && date<=5) && vips.get(0).getLevel()==0){
						msg = "代理提款其他类型的佣金需要在每个月的1号到5号";
						return msg ;
					}
					remark = "代理真人佣金提款";
				}
				msg = proposalService.addCashout(proposer, loginname, pwd, title, from, money, accountName, accountNo,
						accountType, bank, accountCity, bankAddress, email, phone, ip, remark, msflag,gameTolMoney);
				
			}
		}
		if (msg == null) {
			Users user1 =(Users)proposalService.get(Users.class, loginname) ;
			if(user.getCredit().equals(user1.getCredit()) && !(null != tkType && tkType.equals("slotmachine") && user.getRole().equals("AGENT"))){
				String info="额度错误！危险提款前:"+user.getCredit()+"提款后:"+user1.getCredit();
				log.info(info);
				user1.setWarnflag(2);
				proposalService.update(user1);
			}else{
				//提款成功，发送短信 进行提示  先判断玩家是否开通这个服务
				String str= user1.getAddress();
				if(!StringUtil.isEmpty(str)&&str.contains("5")){
				SendPhoneMsgUtil spm = new SendPhoneMsgUtil();
				String userphone=user1.getPhone();
				if(!StringUtil.isEmpty(userphone) ){
				try {
					userphone= AESUtil.aesDecrypt(userphone, AESUtil.KEY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				spm.setPhone(userphone);
				String strName = user1.getLoginname().substring(0, 2)+"***";
				spm.setMsg("亲爱的玩家"+strName+"，您在"+dfh.utils.sendemail.DateUtil.getNow()+"进行提款操作，若有疑问请及时联系在线客服，我们将为您处理，谢谢。");
				spm.start();
				}
				}
			
			}
		}
		return msg;
	}

}
