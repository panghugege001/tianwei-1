package dfh.service.interfaces;

import java.io.Serializable;
import java.util.List;

import dfh.model.Userbankinfo;

public interface IUserbankinfoService {
	
	public List<Userbankinfo> getUserbankinfoList(Userbankinfo userbankinfo,int pageno,int length)throws Exception;
	public List<Userbankinfo> getUserbankinfoList(String username,String bankname,Integer flag,int pageno,int length)throws Exception;
	public int getTotalCount(String username,String bankname,Integer flag)throws Exception;
	
	public boolean unbanding(int id)throws Exception;
	
	public int getTotalCount(Userbankinfo userbankinfo)throws Exception;
	
	public Object save(Object obj);
	
	public Object get(Class clazz, Serializable id);
}
