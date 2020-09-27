package dfh.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SeoKeyUtil {
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> getTitles() throws DocumentException{
		SAXReader reader=new SAXReader();
		Map<String, String> titles=new HashMap<String, String>();
		try {
			Document seokeys = reader.read(SeoKeyUtil.class.getResourceAsStream("/seokey.xml"));
			List<Element> seoTitles = seokeys.selectNodes("/titles/title");
			if (seoTitles!=null&&seoTitles.size()>0) {
				for (int i = 0; i < seoTitles.size(); i++) {
					Element title = seoTitles.get(i);
					titles.put(title.attributeValue("key"),title.attributeValue("value"));
				}
			}else{
				return titles;
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		
		return titles;
	}
	
	

}
