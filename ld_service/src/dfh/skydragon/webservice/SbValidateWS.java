package dfh.skydragon.webservice;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class SbValidateWS {
	private final static String url = "http://10.0.1.10:8082/Test_background/service/sbcheck";
	
	public static boolean checkMember(String username ,String token){
		String result = null;
		QName qname = new QName("http://ws.apache.org/axis2", "checkMember");
		Object param[] = new Object[] { username, token };
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = new Options();
			options.setTo(new EndpointReference(url));
			options.setAction("urn:checkMember");
			client.setOptions(options);
			OMElement element = client.invokeBlocking(qname, param);
			result = element.getFirstElement().getText();

		} catch (AxisFault e) {
			e.printStackTrace();
		}
		return result.equals("true") ? true : false;
	}
	
	public static void main(String[] args) {
		System.out.println(checkMember("RMB520", "6087492")); ;
	}

}
