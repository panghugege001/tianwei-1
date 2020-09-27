package dfh.service.interfaces;

public interface IIntransferService{
	
	String addIntransfer(String from,String to,String operator,Double amount,Double fee,String remark,Integer transferflag) throws Exception;

}
