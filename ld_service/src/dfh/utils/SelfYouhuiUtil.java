package dfh.utils;

import net.sf.json.JSONObject;
import dfh.action.vo.AutoYouHuiVo;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.ISelfYouHuiService;

public class SelfYouhuiUtil {
	
	public synchronized AutoYouHuiVo execute(ISelfYouHuiService selfYouHuiService ,String loginname , Integer youhuiType , Double remit) {
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		if(youhuiType.equals(ProposalType.SELFPT90.getCode())){
			vo = selfYouHuiService.selfYouHui90(loginname , remit) ; //pt首存
			if(null != vo && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()){
				Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney());
				if (deposit != null && deposit) {
					vo.setMessage("自助PT首存优惠成功");
				} else {
					vo.setMessage("自助PT首存优惠失败，请联系客服！");
				}
			}
		}else if(youhuiType.equals(ProposalType.SELFPT91.getCode())){
			vo = selfYouHuiService.selfYouHui91(loginname , remit) ; //pt此存
			if(null != vo && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()){
				Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney());
				if (deposit != null && deposit) {
					vo.setMessage("自助PT次存优惠成功");
				} else {
					vo.setMessage("自助PT次存优惠失败，请联系客服！");
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFPT92.getCode())){
			vo = selfYouHuiService.selfYouHui92(loginname , remit) ; //pt此存
		}
		else if(youhuiType.equals(ProposalType.SELFPT93.getCode())){
			vo = selfYouHuiService.selfYouHui93(loginname , remit) ;//ag
		}
		else if(youhuiType.equals(ProposalType.SELFPT94.getCode())){
			vo = selfYouHuiService.selfYouHui94(loginname , remit) ;//agin
		}
		else if(youhuiType.equals(ProposalType.SELFPT95.getCode())){
			vo = selfYouHuiService.selfYouHui95(loginname , remit) ;//bbin
		}
		else if(youhuiType.equals(ProposalType.SELFEBET96.getCode())){
			vo = selfYouHuiService.selfYouHui96(loginname , remit) ;//ebet
		}
		else if(youhuiType.equals(ProposalType.SELFEBET97.getCode())){
			vo = selfYouHuiService.selfYouHui97(loginname , remit) ;//ebet
		}
		else if(youhuiType.equals(ProposalType.SELFEBET98.getCode())){
			vo = selfYouHuiService.selfYouHui98(loginname , remit) ;//TTG
		}
		else if(youhuiType.equals(ProposalType.SELFEBET99.getCode())){
			vo = selfYouHuiService.selfYouHui99(loginname , remit) ;//ttg
			if(null != vo && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()){
				Boolean deposit = PtUtil1.addPlayerAccountPraper(loginname, vo.getGiftMoney().intValue());
				if (deposit != null && deposit) {
					vo.setMessage("自助TTG次存优惠成功");
				} else {
					vo.setMessage("自助TTG次存优惠失败，请联系客服！");
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFGPI702.getCode())){
			vo = selfYouHuiService.selfYouHui4GPI702(loginname, remit) ;//gpi首存
		}
		else if(youhuiType.equals(ProposalType.SELFGPI703.getCode())){
			vo = selfYouHuiService.selfYouHui4GPI703(loginname, remit) ;//gpi次存
		}
		else if(youhuiType.equals(ProposalType.SELFGPI704.getCode())){
			vo = selfYouHuiService.selfYouHui4GPI704(loginname, remit) ;//gpi限时存送优惠
		}
		else if(youhuiType.equals(ProposalType.SELFPT705.getCode())){
			vo = selfYouHuiService.selfYouHui4PT705(loginname, remit) ;//pt限时存送优惠
			if(null != vo && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()){
				Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney());
				if (deposit != null && deposit) {
					vo.setMessage("自助PT限时优惠成功");
				} else {
					vo.setMessage("自助PT限时优惠失败，请联系客服！");
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFTTG706.getCode())){
			vo = selfYouHuiService.selfYouHui4TTG706(loginname, remit) ;//ttg限时存送优惠
			if(null != vo && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()){
				Boolean deposit = PtUtil1.addPlayerAccountPraper(loginname, vo.getGiftMoney().intValue());
				if (deposit != null && deposit) {
					vo.setMessage("自助TTG限时优惠成功");
				} else {
					vo.setMessage("自助TTG限时优惠失败，请联系客服！");
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFNTFIRST.getCode())){ //NT首存
			vo = selfYouHuiService.selfYouHuiNTFirst(loginname, remit);
			if (vo != null && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()) {
				JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, vo.getGiftMoney()));
				if(ntm.getBoolean("result")){
					vo.setMessage("NT自助首存优惠成功");
				}else{
					vo.setMessage("NT自助首存优惠失败，请联系客服！code:"+ntm.getString("error"));
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFNTTWICE.getCode())){ //NT次存
			vo = selfYouHuiService.selfYouHuiNTTwice(loginname, remit);
			if (vo != null && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()) {
				JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, vo.getGiftMoney()));
				if(ntm.getBoolean("result")){
					vo.setMessage("NT自助次存优惠成功");
				}else{
					vo.setMessage("NT自助次存优惠失败，请联系客服！code:"+ntm.getString("error"));
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFNTSPEC.getCode())){ //NT限时
			vo = selfYouHuiService.selfYouHuiNTSpec(loginname, remit);
			if (vo != null && vo.getMessage().contains("SUCCESS") && null != vo.getGiftMoney()) {
				JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, vo.getGiftMoney()));
				if(ntm.getBoolean("result")){
					vo.setMessage("NT自助限时优惠成功");
				}else{
					vo.setMessage("NT自助限时优惠失败，请联系客服！code:"+ntm.getString("error"));
				}
			}
		}
		else if(youhuiType.equals(ProposalType.SELFQTFIRST.getCode())){ //QT首存
			vo = selfYouHuiService.selfYouHuiQTFirst(loginname, remit);
		}
		else if(youhuiType.equals(ProposalType.SELFQTTWICE.getCode())){ //QT次存
			vo = selfYouHuiService.selfYouHuiQTTwice(loginname, remit);
		}
		else if(youhuiType.equals(ProposalType.SELFQTSPEC.getCode())){ //QT限时
			vo = selfYouHuiService.selfYouHuiQTSpec(loginname, remit);
		}
		
		return vo ;
	}

}
