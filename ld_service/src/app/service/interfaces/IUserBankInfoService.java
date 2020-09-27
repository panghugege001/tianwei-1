package app.service.interfaces;

import java.util.List;

import app.model.vo.UserBankInfoVO;
import app.model.vo.UserbankVO;
import dfh.model.Userbankinfo;

public interface IUserBankInfoService {
	
	List<Userbankinfo> queryUserBankInfoList(UserBankInfoVO vo);
	
	Userbankinfo queryUserBankInfo(UserBankInfoVO vo);
	
	
	UserbankVO bankBinding(UserbankVO vo);
	
}