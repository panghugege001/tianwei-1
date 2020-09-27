package dfh.mgs.vo.validateApiUser;

import javax.xml.bind.annotation.XmlElement;

public class OrionString {

	private String[] oString;

	@XmlElement(name = "string", namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays")
	public String[] getoString() {
		return oString;
	}

	public void setoString(String[] oString) {
		this.oString = oString;
	}

}
