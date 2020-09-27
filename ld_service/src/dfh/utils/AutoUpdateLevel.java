package dfh.utils;

import dfh.model.Users;
import dfh.model.enums.VipLevel;
import dfh.service.interfaces.IMemberSignrecord;


/**
 * 自动升级会员等级。新会员存款满三笔，即自动升级为忠实会员
 * 2015-02-24
 */
public class AutoUpdateLevel extends Thread {
	
	private Users user;
	private IMemberSignrecord memberService;
	
	public AutoUpdateLevel(Users user, IMemberSignrecord memberService){
		this.user = user;
		this.memberService = memberService;
	}

	private void updateUserLevel(){
		int count = 0;
		//获取在线支付笔数 type=0支付成功
		count += memberService.getPayOrderCountByUser(user);
		if(count >= 3){
			optUpdateLevel();
		}else{
			//获取秒存笔数
			count += memberService.getDepositProposalCountByUser(user);
			if(count >= 3){
				optUpdateLevel();
			}else{
				//额度验证存款笔数（暂未开放）
			}
		}
	}
	
	private void optUpdateLevel(){
		user.setLevel(VipLevel.TIANJIANG.getCode());
		memberService.updateUserCreditSqlVip(user);
	}
	
	@Override
	public void run() {
		updateUserLevel();
	}
}
