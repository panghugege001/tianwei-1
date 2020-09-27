package dfh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class Axis1Util {
	private static final String ENDPOINT = "http://192.168.0.169:8080/msWS/services/msBankService";
	private static final String QNAME = "http://service.webservice.ms/";
	public static Call getAxisCall_AddOrder(){
		try{
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINT));
			QName qn = new QName(QNAME, "addOrder");
			call.setOperationName(qn);
			call.addParameter("orderNo",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("toname",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("toaccoutNo",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("bankCode",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("toamount",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.addParameter("remark",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.setReturnType(qn,Integer.class);// 设置返回类型
			return call;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static Call getAxisCall_SelectOrderStatus(){
		try{
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINT));
			QName qn = new QName(QNAME, "selectOrderStatus");
			call.setOperationName(qn);
			call.setReturnType(qn,String.class);// 设置返回类型
			return call;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static Call getAxisCall_processToOrdered(){
		try{
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(ENDPOINT));
			QName qn = new QName(QNAME, "processToOrdered");
			call.setOperationName(qn);
			call.addParameter("orderNo",
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);// 操作的参数
			call.setReturnType(qn,Integer.class);// 设置返回类型
			return call;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 网络连接检查
	 * @param string 
	 * @return Boolean
	 */
	public static Boolean getConnection(String string) {
		InputStream is = null;
		try {
			URL url = new URL(string);
			is = url.openStream();
			return true;
		} catch (Exception e) {
			return false;
		}finally{
			try {
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		if(getConnection(ENDPOINT+"?wsdl")){
			Object[] obj = new Object[] {"5031211190188","woody", "6222022102009685373", "icbc", "0.01","备注" };
			Integer result = (Integer) getAxisCall_AddOrder().invoke(obj);
			System.out.println(result);
		}
	}

}
