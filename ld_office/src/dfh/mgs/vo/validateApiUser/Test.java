package dfh.mgs.vo.validateApiUser;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class Test {

	public static void main(String[] args) throws JAXBException {
		String abc = "<ValidateApiUserResponse xmlns=\"http://mgsops.net/AdminAPI_Admin\">\n" +
				"         <ValidateApiUserResult xmlns:a=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
				"            <a:Claims xmlns:b=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">\n" +
				"               <b:string>AdminAPI_Admin</b:string>\n" +
				"               <b:string>AdminAPI_Gaming</b:string>\n" +
				"               <b:string>ThirdParty_Admin</b:string>\n" +
				"               <b:string>AdminAPI_ItalyPlayer</b:string>\n" +
				"               <b:string>AdminAPI_ItalyGlobal</b:string>\n" +
				"               <b:string>AdminAPI_Freegame</b:string>\n" +
				"               <b:string>AdminAPI_PlayCheck</b:string>\n" +
				"            </a:Claims>\n" +
				"            <a:IsValid>true</a:IsValid>\n" +
				"            <a:ValidServerIds xmlns:b=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">\n" +
				"               <b:int>5081</b:int>\n" +
				"            </a:ValidServerIds>\n" +
				"         </ValidateApiUserResult>\n" +
				"      </ValidateApiUserResponse>";
		
		JAXBContext jaxbContext = JAXBContext.newInstance(ValidateApiUserResult.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		ValidateApiUserResult root = (ValidateApiUserResult)unmarshaller.unmarshal(new StringReader(abc));
				
        System.out.println("###");
	}

}
