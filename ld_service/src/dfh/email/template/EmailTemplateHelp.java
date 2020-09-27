package dfh.email.template;

import java.text.MessageFormat;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import dfh.utils.Constants;

public class EmailTemplateHelp {

	public static void readerEmailTemplate() throws Exception {
		
		SAXReader reader = new SAXReader();
		Document doc = reader.read(EmailTemplateHelp.class.getResourceAsStream("/dfh/email/template/emailnobody.xml"));
		Element e = doc.getRootElement();
		Constants.EMAIL_NOBODY_HTML = e.asXML();

		doc = reader.read(EmailTemplateHelp.class.getResourceAsStream("/dfh/email/template/registerbody.xml"));
		e = doc.getRootElement();
		Constants.EMAIL_REGISTER_BODY_HTML = e.asXML();
	}
	
	public synchronized static String toHTML(String bodyHtml, Object o) {
		
		MessageFormat format = new MessageFormat(bodyHtml, Locale.CHINA);
		bodyHtml = format.format(o);

		return Constants.EMAIL_NOBODY_HTML.replace("<replaceme/>", bodyHtml);
	}
}