package dfh.security;

import dfh.utils.DomOperator;
import org.dom4j.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Ipconfine {
	public static Map<String, String> getAuthorites(String ipconfineXml) {
		Map<String, String> ipconfineMap = new HashMap<String, String>();
		Document doc = DomOperator.string2Document(ipconfineXml);
		if (doc != null) {
			ipconfineMap.put("allow", doc.getRootElement().element("allow").getText());
			ipconfineMap.put("deny", doc.getRootElement().element("deny").getText());
		}
		return ipconfineMap;
	}

	public static Set<String> getDenyArea(){
		HashSet<String> area = new HashSet<String>();
		area.add("菲律宾");
		area.add("香港");
		area.add("台湾");
		return area;
	}

	public static void main(String[] args) {
		Ipconfine.getAuthorites("<?xml version='1.0' encoding='UTF-8'?><config><!-- 通过的(ip allow of ip) 以,号分隔(, compart)--><allow>110.165.43.9,58.69.130.90</allow><!-- 拒绝的ip deny of ip 以,号分隔(,compart)--><deny>111.111.111.111,222.222.222.222</deny></config>");
	}
}
