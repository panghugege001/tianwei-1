package dfh.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Area {

	private static final List PROVINCES;
	private static final List CITYS;
	private static final String PROVINCE = "省";
	private static final String CITY = "市";

	private static final Map chinaMap;
	private String province;
	private String city;

	static {
		PROVINCES = new ArrayList();
		CITYS = new ArrayList();
		PROVINCES.add("北京");
		PROVINCES.add("天津");
		PROVINCES.add("河北");
		PROVINCES.add("山西");
		PROVINCES.add("内蒙古");
		PROVINCES.add("辽宁");
		PROVINCES.add("吉林");
		PROVINCES.add("黑龙江");
		PROVINCES.add("上海");
		PROVINCES.add("江苏");
		PROVINCES.add("浙江");
		PROVINCES.add("安徽");
		PROVINCES.add("福建");
		PROVINCES.add("江西");
		PROVINCES.add("山东");
		PROVINCES.add("河南");
		PROVINCES.add("湖北");
		PROVINCES.add("湖南");
		PROVINCES.add("广东");
		PROVINCES.add("广西");
		PROVINCES.add("海南");
		PROVINCES.add("重庆");
		PROVINCES.add("四川");
		PROVINCES.add("贵州");
		PROVINCES.add("云南");
		PROVINCES.add("西藏");
		PROVINCES.add("陕西");
		PROVINCES.add("甘肃");
		PROVINCES.add("青海");
		PROVINCES.add("宁夏");
		PROVINCES.add("青海");
		PROVINCES.add("新疆");
		PROVINCES.add("香港");
		PROVINCES.add("澳门");
		PROVINCES.add("台湾");
		CITYS.add("北京");
		CITYS.add("天津");
		CITYS.add("香港");
		CITYS.add("上海");
		CITYS.add("澳门");
		CITYS.add("重庆");
		CITYS.add("天津");
		chinaMap = new HashMap();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(Area.class.getResourceAsStream("China_Provinces_Cities.xml"));
			Element element = doc.getDocumentElement();
			NodeList cities = element.getElementsByTagName("city");
			for (int i = 0; i < cities.getLength(); i++) {
				Node city = cities.item(i);
				Node province = city.getParentNode();
				String city_str = city.getAttributes().item(0).getNodeValue();
				String province_str = province.getAttributes().item(0).getNodeValue();
				if (city_str != null && !city_str.trim().equals("未设置"))
					chinaMap.put(city_str, province_str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(new File("C:\\Documents and Settings\\user\\桌面\\bank\\city.txt"));
			br = new BufferedReader(fr);
			do {
				String text = br.readLine();
				if (text == null)
					break;
				System.out.println((new StringBuilder("AREA:")).append(new Area(text)).toString());
			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public Area(String str) {
		if (str == null)
			return;
		for (Iterator it = chinaMap.keySet().iterator(); it.hasNext();) {
			String city = (String) it.next();
			if (str.contains(city.trim().replace("市", ""))) {
				setCity(city.replace("市", ""));
				String province = ((String) chinaMap.get(city)).toString().replace("省", "");
				setProvince(province);
				if (CITYS.contains(province.replace("市", "")))
					setCity(province);
			}
		}

		if (this.city == null && this.province == null) {
			System.err.println(str);
			String array[] = str.split("省");
			if (array.length > 1) {
				str = str.replace("市", "");
				setProvince(array[0]);
				setCity(array[1]);
			} else {
				str = str.replace("省", "");
				str = str.replace("市", "");
				for (Iterator it = PROVINCES.listIterator(); it.hasNext();) {
					String province = (String) it.next();
					if (str.startsWith(province)) {
						setProvince(province);
						String city = str.substring(province.length());
						if (city.trim().equals(""))
							setCity(province);
						else if (CITYS.contains(province))
							setCity((new StringBuilder(String.valueOf(province))).append(str.substring(province.length())).toString());
						else
							setCity(str.substring(province.length()));
					}
				}

			}
		}
	}

	public String getCity() {
		return city;
	}

	public String getProvince() {
		return province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return (new StringBuilder(String.valueOf(province))).append("-").append(city).toString();
	}
}
