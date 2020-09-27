package dfh.mgs.vo;

import java.io.Serializable;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import dfh.mgs.vo.freegame.AddPlayersToFreegameRoot;
import dfh.mgs.vo.freegame.GetFreegamesRoot;

public class OrionResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static void unmarshal() throws JAXBException{
		String abc = "<GetCommitQueueDataResponse><GetCommitQueueDataResult xmlns:a=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><a:QueueDataResponse><a:LoginName>test</a:LoginName><a:UserId>30458</a:UserId><a:ChangeAmount>30472840</a:ChangeAmount><a:TransactionCurrency>CNY</a:TransactionCurrency><a:Status>Unknown</a:Status><a:RowId>-5455</a:RowId><a:TransactionNumber>28</a:TransactionNumber><a:GameName>MGS_MegaMoolah</a:GameName><a:DateCreated>04/21/2016 11:04:04 AM</a:DateCreated><a:MgsReferenceNumber>9000005454</a:MgsReferenceNumber><a:ServerId>5060</a:ServerId><a:MgsPayoutReferenceNumber>0</a:MgsPayoutReferenceNumber><a:PayoutAmount>0</a:PayoutAmount><a:ProgressiveWin>true</a:ProgressiveWin><a:ProgressiveWinDesc>Jackpot[2]</a:ProgressiveWinDesc><a:FreeGameOfferName/><a:TournamentId>0</a:TournamentId><a:Description i:nil='true'/><a:ExtInfo i:nil='true'/><a:RowIdLong>-5455</a:RowIdLong></a:QueueDataResponse></GetCommitQueueDataResult></GetCommitQueueDataResponse>";
		
		JAXBContext jaxbContext = JAXBContext.newInstance(GetCommitQueueDataRoot.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		GetCommitQueueDataRoot root = (GetCommitQueueDataRoot)unmarshaller.unmarshal(new StringReader(abc));
		for (QueueDataResponse response : root.getGetCommitQueueDataResult())
			System.out.printf("%s, %d, %d%n", response.getLoginName(),
			                  response.getUserId(), response.getChangeAmount());
		
		
        System.out.println("###");
	}
	
	private static void unmarshal2() throws JAXBException{
		String xml = "<GetFreegamesResponse xmlns=\"http://mgsops.net/AdminAPI_Freegame\">\n" +
		"         <GetFreegamesResult xmlns:a=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
		"            <a:Offers>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>30</a:Cost>\n" +
		"                  <a:Description>SantasWildRideDEF</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>1</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2012-11-22T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>15</a:Cost>\n" +
		"                  <a:Description>RivieraRichesDEF</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>2</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2012-11-22T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>1500</a:Cost>\n" +
		"                  <a:Description>BigBreak100x</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>79</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2013-01-23T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>1500</a:Cost>\n" +
		"                  <a:Description>BigKahuna100x</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>80</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2013-01-23T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>5000</a:Cost>\n" +
		"                  <a:Description>HotInk100x</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>82</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2013-01-23T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>2500</a:Cost>\n" +
		"                  <a:Description>EaglesWings100x</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>83</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2013-01-23T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>3000</a:Cost>\n" +
		"                  <a:Description>AsianBeauty100x</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>84</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2013-01-23T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"               <a:Offer>\n" +
		"                  <a:Cost>5000</a:Cost>\n" +
		"                  <a:Description>GoldFactory100x</a:Description>\n" +
		"                  <a:EndDate>2017-11-22T00:00:00</a:EndDate>\n" +
		"                  <a:Id>85</a:Id>\n" +
		"                  <a:PlayerCount>0</a:PlayerCount>\n" +
		"                  <a:StartDate>2013-01-23T00:00:00</a:StartDate>\n" +
		"               </a:Offer>\n" +
		"			 </a:Offers>\n" +
		"         </GetFreegamesResult>\n" +
		"      </GetFreegamesResponse>";
		
		JAXBContext jaxbContext = JAXBContext.newInstance(GetFreegamesRoot.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		GetFreegamesRoot root = (GetFreegamesRoot)unmarshaller.unmarshal(new StringReader(xml));
		
		System.out.println(root.getOffers().getOfferList().size());
	}
	
	
	private static void unmarshal3() throws JAXBException{
		String xml = "<AddPlayersToFreegameResponse\n" +
		"    xmlns=\"http://mgsops.net/AdminAPI_Freegame\">\n" +
		"    <AddPlayersToFreegameResult\n" +
		"        xmlns:a=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\"\n" +
		"        xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
		"        <a:PlayerActions>\n" +
		"            <a:PlayerAction>\n" +
		"                <a:Error i:nil=\"true\"/>\n" +
		"                <a:ISOCurrencyCode>CNY</a:ISOCurrencyCode>\n" +
		"                <a:LoginName>woodytest</a:LoginName>\n" +
		"                <a:PlayerStartDate>2016-06-16T10:25:34</a:PlayerStartDate>\n" +
		"                <a:Success>true</a:Success>\n" +
		"                <a:InstanceId>1</a:InstanceId>\n" +
		"                <a:OfferId>113</a:OfferId>\n" +
		"                <a:Sequence i:nil=\"true\"/>\n" +
		"            </a:PlayerAction>\n" +
		"            <a:PlayerAction>\n" +
		"                <a:Error i:nil=\"true\"/>\n" +
		"                <a:ISOCurrencyCode>CNY</a:ISOCurrencyCode>\n" +
		"                <a:LoginName>woodytest</a:LoginName>\n" +
		"                <a:PlayerStartDate>2016-06-16T10:25:34</a:PlayerStartDate>\n" +
		"                <a:Success>true</a:Success>\n" +
		"                <a:InstanceId>2</a:InstanceId>\n" +
		"                <a:OfferId>113</a:OfferId>\n" +
		"                <a:Sequence i:nil=\"true\"/>\n" +
		"            </a:PlayerAction>\n" +
		"            <a:PlayerAction>\n" +
		"                <a:Error i:nil=\"true\"/>\n" +
		"                <a:ISOCurrencyCode>CNY</a:ISOCurrencyCode>\n" +
		"                <a:LoginName>woodytest</a:LoginName>\n" +
		"                <a:PlayerStartDate>2016-06-16T10:25:34</a:PlayerStartDate>\n" +
		"                <a:Success>true</a:Success>\n" +
		"                <a:InstanceId>1</a:InstanceId>\n" +
		"                <a:OfferId>150</a:OfferId>\n" +
		"                <a:Sequence i:nil=\"true\"/>\n" +
		"            </a:PlayerAction>\n" +
		"        </a:PlayerActions>\n" +
		"        <a:Sequence i:nil=\"true\"/>\n" +
		"        <a:HasErrors>false</a:HasErrors>\n" +
		"    </AddPlayersToFreegameResult>\n" +
		"</AddPlayersToFreegameResponse>";
		
		JAXBContext jaxbContext = JAXBContext.newInstance(AddPlayersToFreegameRoot.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		AddPlayersToFreegameRoot root = (AddPlayersToFreegameRoot)unmarshaller.unmarshal(new StringReader(xml));
		
		System.out.println("RRRRRRRRRRRRR");
	}
	
	
	private static void marshal() throws JAXBException{
		/*JAXBContext jaxbContext = JAXBContext.newInstance(GetFreegamesRoot.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		GetFreegamesRoot root = new GetFreegamesRoot();
		Offers offers = new Offers();
		Offer offer = new Offer(30, "GoldFactory100x", new Date(), 123, 100, new Date());
		offers.getOfferList().add(offer);
		offer = new Offer(30, "GoldFactory100x", new Date(), 123, 100, new Date());
		offers.getOfferList().add(offer);
		root.setOffers(offers);
		
		
		marshaller.marshal(root, System.out);*/
		
	}
	
	
	public static void main(String[] args) throws JAXBException {
		unmarshal3();
	}
}
