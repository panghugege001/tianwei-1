package dfh.mgs.vo.validateApiUser;

import javax.xml.bind.annotation.XmlElement;


public class OrionInt {

	private int[] oInt;

	@XmlElement(name = "int", namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays")
	public int[] getoInt() {
		return oInt;
	}

	public void setoInt(int oInt[]) {
		this.oInt = oInt;
	}
	 
}
