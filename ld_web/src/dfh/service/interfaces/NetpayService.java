package dfh.service.interfaces;

import java.util.List;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Netpay;

public interface NetpayService  extends UniversalService{

	String addNetpayOrder(String billno, String loginname, String password, String netpayName, boolean newAccount, String partner, Double amount, String aliasName, String phone, String email,
			String attach, String ip) throws GenericDfhRuntimeException;

	String excuteNetpayOrder(String billno, Double amount, Boolean isSucc, String message, String md5Info,String ip) throws GenericDfhRuntimeException;

	List getAllNetpay();

	Netpay getNetpayByMerno(String merno);

	String repairNetpayOrder(String operator, String billno, Double amount,String ip) throws GenericDfhRuntimeException;

	String updateNetpay(String billno, Boolean enabled, Integer next) throws GenericDfhRuntimeException;

}