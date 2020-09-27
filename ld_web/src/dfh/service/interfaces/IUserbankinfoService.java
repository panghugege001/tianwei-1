package dfh.service.interfaces;

import java.util.List;

import dfh.model.Userbankinfo;

public interface IUserbankinfoService {
	
	public List<Userbankinfo> getUserbankinfoList(String loginname)throws Exception;
	
	public void banding(String loginname,String bankno,String bankname,String bankaddress)throws Exception;
	
	public Userbankinfo getUserbankinfo(String loginname,String bankname)throws Exception;
	
	public List getBankinfo(String sql,String username,String bankname)throws Exception;
}
