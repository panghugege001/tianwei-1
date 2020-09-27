package dfh.utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.skydragon.webservice.model.RecordInfo;

public class AxisUtil {
	 private static Log log = LogFactory.getLog(AxisUtil.class);
	 //在线支付key
	 private static final String PAY_ONLINE_KEY=SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("PAY_ONLINE_KEY"));
	 private static final String AXIS2PATH = WebServiceConfig.axis2XmlPath;
	 public static final String NAMESPACE="http://webservice.skydragon.dfh";
	 public static final String APPSERVICE_NAMESPACE="http://webservice.app";
	 //默认webservice
	 public static final String PUBLICWEBSERVICEURL = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("webservice.publicWSurl"));
	 //资金变动、游戏转账、优惠操作专用
	 public static final String PUBLICWEBSERVICEURL_1 = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("webservice.publicWSurl_1"));
	 //常用查询专用
	 public static final String PUBLICWEBSERVICEURL_2 = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("webservice.publicWSurl_2"));
	 
	 public static final String PAY_CENTER_URL = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("middleservice.url.pay"));
	 
	 private static  ConfigurationContext ctx = null;
	 static {
		 try {
			ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
					AXIS2PATH+"/WEB-INF",
					AXIS2PATH+"/WEB-INF/client.axis2.xml");
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("client.axis2.xml.....异常...");
		}
	 }
	 /**
	 * @param serviceAddress
	 *            service端地址
	 * @param manageSession
	 *            是否开启服务器端的Session true开启 flase关闭
	 * @return RPCServiceClient
	 * @throws AxisFault 
	 */
	public static RPCServiceClient getClient(String serviceAddress,Boolean manageSession) throws AxisFault {
//			ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
//					AXIS2PATH+"/WEB-INF",
//					AXIS2PATH+"/WEB-INF/client.axis2.xml");
			//RPCServiceClient client = new RPCServiceClient(ctx,null);
			RPCServiceClient client = new RPCServiceClient();
			Options option = client.getOptions();
			option.setProperty(HTTPConstants.REUSE_HTTP_CLIENT,true);
			option.setTimeOutInMilliSeconds(600000L);
			option.setManageSession(manageSession);
			EndpointReference erf = new EndpointReference(serviceAddress);
			option.setTo(erf);
			return client;
	}
	/**
	 * @param client 获取客户端
	 * @param namespace 命名空间
	 * @param method 服务器端的方法名
	 * @param object 传入的对象
	 * @param classOne 返回类型
	 * @return T 失败返回unll
	 * @throws AxisFault 
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getObjectOne(RPCServiceClient client,String namespace,
			String method, Object object, Class classOne) throws AxisFault {
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(httpConnectionManager);
			client.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT,httpClient);
			
			QName qname = new QName(namespace, method);
			Object[] objectArray = new Object[]{object};
			OMElement tt = client.invokeBlocking(qname, objectArray);
			
			
			Iterator elemetns = tt.getChildElements();
			T t=null;
			while (elemetns.hasNext()) {
				OMElement tmp = (OMElement) elemetns.next();
				t = (T) BeanUtil.processObject(tmp,classOne, null, true, new DefaultObjectSupplier(), null);
			}
			if (client != null) {
				client.cleanupTransport();
				client.cleanup();
			}
			httpConnectionManager.closeIdleConnections(0);
			httpConnectionManager.shutdown();
			return t;
	}

	public static Page getPageInList(RPCServiceClient client,String namespace,
			String method, Object object, Class classOne,Class classTwo) throws AxisFault {
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(httpConnectionManager);
			client.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT,httpClient);
		
			QName qname = new QName(namespace, method);
			Object[] objectArray = new Object[]{object};
			OMElement tt = client.invokeBlocking(qname, objectArray);
			
			
			Iterator elemetns = tt.getChildElements();
			Page t=null;
			List list = new ArrayList();
			while (elemetns.hasNext()) {
				OMElement tmp = (OMElement) elemetns.next();
				Iterator ele = tmp.getChildElements();
				while (ele.hasNext()) {
					OMElement ttt = (OMElement) ele.next();
					if("pageContents".equals(ttt.getLocalName()) && null!=ttt.getFirstElement() 
							&& !"empty".equals(ttt.getFirstElement().getLocalName())
							&& !"true".equals(ttt.getFirstElement().getText())){
						Object otherObject = BeanUtil.processObject(ttt, classTwo, null, true, new DefaultObjectSupplier(), null);
						list.add(otherObject);
					}
				}
				t = (Page) BeanUtil.processObject(tmp,classOne, null, true, new DefaultObjectSupplier(), null);
				t.setPageContents(list);
			}
			if (client != null) {
				client.cleanupTransport();
				client.cleanup();
			}
			httpConnectionManager.closeIdleConnections(0);
			httpConnectionManager.shutdown();
			return t;
	}
	
	public static RecordInfo getRecordInList(RPCServiceClient client,String namespace,
			String method, Object object, Class classOne,Class classTwo) throws AxisFault {
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(httpConnectionManager);
			client.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT,httpClient);
		
			QName qname = new QName(namespace, method);
			Object[] objectArray = new Object[]{object};
			OMElement tt = client.invokeBlocking(qname, objectArray);
			
			
			
			Iterator elemetns = tt.getChildElements();
			RecordInfo t=null;
			List list = new ArrayList();
			while (elemetns.hasNext()) {
				OMElement tmp = (OMElement) elemetns.next();
				Iterator ele = tmp.getChildElements();
				while (ele.hasNext()) {
					OMElement ttt = (OMElement) ele.next();
					if("dataList".equals(ttt.getLocalName()) && null!=ttt.getFirstElement() 
						&& !"empty".equals(ttt.getFirstElement().getLocalName())
							&& !"true".equals(ttt.getFirstElement().getText())){
						Object otherObject = BeanUtil.processObject(ttt, classTwo, null, true, new DefaultObjectSupplier(), null);
						list.add(otherObject);
					}
				}
				t = (RecordInfo) BeanUtil.processObject(tmp,classOne, null, true, new DefaultObjectSupplier(), null);
				t.setDataList(list);
			}
			if (client != null) {
				client.cleanupTransport();
				client.cleanup();
			}
			httpConnectionManager.closeIdleConnections(0);
			httpConnectionManager.shutdown();
			return t;
	}
	/**
	 * @param client 获取客户端
	 * @param namespace 命名空间
	 * @param method 服务器端的方法名
	 * @param object 传入的对象
	 * @param classOne 返回类型
	 * @return List<Object> 失败返回unll
	 * @throws AxisFault 
	 */
	public static <T> List<T> getListObjectOne(RPCServiceClient client,String namespace,
			 String method, Object object, Class classOne) throws AxisFault {
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(httpConnectionManager);
			client.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT,httpClient);
		
			List<T> list = new ArrayList<T>();
			QName qname = new QName(namespace, method);
			Object[] objectArray = new Object[] { object };
			OMElement tt = client.invokeBlocking(qname, objectArray);
			
			
			
			Iterator elemetns = tt.getChildElements();
			while (elemetns.hasNext()) {
				OMElement tmp = (OMElement) elemetns.next();
				T otherObject = (T) BeanUtil.processObject(tmp, classOne, null, true, new DefaultObjectSupplier(), null);
				list.add(otherObject);
			}
			if (client != null) {
				client.cleanupTransport();
				client.cleanup();
			}
			httpConnectionManager.closeIdleConnections(0);
			httpConnectionManager.shutdown();
			return list;
	}

	/**
	 * 调用webService服务，返回值为数组方法
	 * @param <T>
	 * @param client 获取客户端
	 * @param namespace 命名空间
	 * @param method 服务器端的方法名
	 * @param object 服务器端方法的参数
	 * @param classOne 数组返回值class类型,如byte[].class
	 * @return 返回数组
	 * @throws AxisFault 
	 */
	public static <T> T getArrayObjectOne(RPCServiceClient client,String namespace,
			 String method, Object object, Class<T> classOne) throws AxisFault{
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(httpConnectionManager);
			client.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT,httpClient);
			
			T t = null;
			QName qname = new QName(namespace, method);
			Object[] objectArray = new Object[] {object };
			Class[] classes = new Class[] {classOne };
			t = (T) client.invokeBlocking(qname, objectArray,classes)[0];
			
			if (client != null) {
				client.cleanupTransport();
				client.cleanup();
			}
			httpConnectionManager.closeIdleConnections(0);
			httpConnectionManager.shutdown();
			return t;
	}
	/**
	 * @param client 获取客户端 
	 * @param namespace 命名空间
	 * @param method 服务器端的方法名
	 * @param object 传入的对象
	 * @return Iterator
	 * @throws AxisFault 
	 */
	public static Iterator getObjectIterator(RPCServiceClient client,String namespace,
			 String method, Object object) throws AxisFault {
			MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(httpConnectionManager);
			client.getOptions().setProperty(HTTPConstants.CACHED_HTTP_CLIENT,httpClient);
		
			QName qname = new QName(namespace, method);
			Object[] objectArray = new Object[] { object };
			OMElement tt = client.invokeBlocking(qname, objectArray);
			
			if (client != null) {
				client.cleanupTransport();
				client.cleanup();
			}
			httpConnectionManager.closeIdleConnections(0);
			httpConnectionManager.shutdown();
			
			return tt.getChildElements();
	}
	public static String getPAY_ONLINE_KEY() {
		return PAY_ONLINE_KEY;
	}
}
