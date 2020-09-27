package dfh.mgs;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

import dfh.exception.GenericDfhRuntimeException;
import dfh.mgs.vo.CompleteGameRequest;
import dfh.mgs.vo.FlushPendingCashinRequest;
import dfh.mgs.vo.GetCommitQueueDataRoot;
import dfh.mgs.vo.GetFailedEndGameQueueRoot;
import dfh.mgs.vo.GetProgressiveWinCashinReportRoot;
import dfh.mgs.vo.GetRollbackQueueDataRoot;
import dfh.mgs.vo.ManuallyCompleteGameRoot;
import dfh.mgs.vo.OrionResponse;
import dfh.mgs.vo.ProgressiveWinCashinReportRequest;
import dfh.mgs.vo.ValidteBetRequest;
import dfh.mgs.vo.freegame.AddPlayersToFreegameRoot;
import dfh.mgs.vo.freegame.AddPlayersToOfferRequest;
import dfh.mgs.vo.freegame.GetFreegamesByPlayerRoot;
import dfh.mgs.vo.freegame.GetFreegamesPlayerDetailsRoot;
import dfh.mgs.vo.freegame.GetFreegamesRoot;
import dfh.mgs.vo.freegame.GetOffersByPlayerOfferStatusRequest;
import dfh.mgs.vo.freegame.GetOffersByPlayerRequest;
import dfh.mgs.vo.freegame.GetOffersPlayerDetailsRequest;
import dfh.mgs.vo.freegame.GetPlayersByFreegameRoot;
import dfh.mgs.vo.freegame.GetPlayersByOfferRequest;
import dfh.mgs.vo.freegame.GetPlayersByOfferResponse;
import dfh.mgs.vo.freegame.Offer;
import dfh.mgs.vo.freegame.OfferStatusId;
import dfh.mgs.vo.freegame.PlayerAction;
import dfh.mgs.vo.freegame.RemovePlayersFromOfferRequest;
import dfh.mgs.vo.freegame.RemovePlayersFromOfferRoot;
import dfh.mgs.vo.validateApiUser.ValidateApiUserResult;

public class OrionUtil {

	private static Log log = LogFactory.getLog(OrionUtil.class);
	
	public final static int serverId = 18256;
	protected final static String userName = "lehudotcom";
	protected final static String  password = "l3huDo7c0m";
	protected final static String serviceHost = "https://orionapi22.gameassists.co.uk";
	
	private static final SimpleDateFormat sdf4MG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static{
		System.setProperty("javax.net.ssl.trustStore", Thread.currentThread().getContextClassLoader().getResource("").getPath() + "orion.cer");
		log.info(">>>>>>>>>>>> Orion.cer:" + System.getProperty("javax.net.ssl.trustStore"));
	}
	
	public static String sendRequst(String url, String soapAction, String xmlInfo){
		HttpClient httpclient = new HttpClient();
		PostMethod httpRequst = new PostMethod(url);
		String auth = userName + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
		String authHeader = "Basic " + new String(encodedAuth);
		//System.out.println(authHeader);
		httpRequst.addRequestHeader(HttpHeaders.AUTHORIZATION, authHeader);
		httpRequst.addRequestHeader("content-type", "text/xml;charset=UTF-8");
		httpRequst.addRequestHeader("SOAPAction", soapAction);
		httpRequst.addRequestHeader("Request-Id", UUID.randomUUID().toString());
		httpRequst.setRequestEntity(new StringRequestEntity(xmlInfo));
		try {
			httpclient.executeMethod(httpRequst);
			String responseBody = httpRequst.getResponseBodyAsString();
			return responseBody;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(httpRequst != null){
				httpRequst.releaseConnection();
			}
		}	
		return null;
	}
	
	/**
	 * 将xml报文转为java bean
	 * @param xmlStr
	 * @param cls
	 * @return
	 */
	private static OrionResponse xml2Bean(String xmlStr, Class cls){
		return null;
	}
	
	private static Element getBodyElement(String documentText) throws DocumentException{
		Document doc = DocumentHelper.parseText(documentText);
		Map<String, String> namespaceMap = new HashMap<String, String>();
		namespaceMap.put("s", "http://schemas.xmlsoap.org/soap/envelope/");
		XPath xpath = doc.createXPath("//s:Body"); // 要获取的节点
		xpath.setNamespaceURIs(namespaceMap);
		return (Element)xpath.selectSingleNode(doc);
	}
	
	private static Object unmarshalXml2Bean(String xml, Class cls) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(cls);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		return unmarshaller.unmarshal(new StringReader(xml));
	}
	
	
	/**
	 * This method returns all the failed bet transactions that Vanguard is unable to process, so that you
	 * can reconcile them in your system.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void getRollbackQueueData() throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/GetRollbackQueueData";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetRollbackQueueData>\n" +
		"         <!--Optional:-->\n" +
		"         <adm:serverIds>\n" +
		"            <!--Zero or more repetitions:-->\n" +
		"            <arr:int>" + serverId + "</arr:int>\n" +
		"         </adm:serverIds>\n" +
		"      </adm:GetRollbackQueueData>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";

		String response = sendRequst(url, soapAction, xmlInfo);
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetRollbackQueueDataResponse");
		if(null != resultEle){
			GetRollbackQueueDataRoot rollbackQueue = (GetRollbackQueueDataRoot) unmarshalXml2Bean(resultEle.asXML(), GetRollbackQueueDataRoot.class);
			 
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method returns all the failed win transactions that Vanguard is unable to process, 
	 * so that you can reconcile them in your system.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 * 
	 * 给玩家加上赢取的金额
	 */
	public static void getCommitQueueData() throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/GetCommitQueueData";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetCommitQueueData>\n" +
		"         <adm:serverIds>\n" +
		"            <arr:int>" + serverId + "</arr:int>\n" +
		"         </adm:serverIds>\n" +
		"      </adm:GetCommitQueueData>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";

		String response = sendRequst(url, soapAction, xmlInfo);
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetCommitQueueDataResponse");
		if(null != resultEle){
			GetCommitQueueDataRoot commitQueue = (GetCommitQueueDataRoot) unmarshalXml2Bean(resultEle.asXML(), GetCommitQueueDataRoot.class);
			System.out.println(commitQueue.getGetCommitQueueDataResult().size());
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method enables you to manually unlock a player from the Vanguard Commit or Rollback queue.
	 * 
	 * 将玩家从 Commit or Rollback 队列中解锁
	 */
	public static void manuallyValidateBet(List<ValidteBetRequest> validateBetReqList){
		if(validateBetReqList != null && validateBetReqList.size() > 0){
			String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
			String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/ManuallyValidateBet";
			StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures\">" +
			"   <soapenv:Header/>" +
			"   <soapenv:Body>" +
			"      <adm:ManuallyValidateBet>" +
			"         <adm:validateRequests>");
			for (ValidteBetRequest item : validateBetReqList) {
				xmlInfo.append("<ori:ValidteBetRequest>").append("<ori:ExternalReference>").append(item.getExternalReference()).append("</ori:ExternalReference>");
				xmlInfo.append("<ori:RowId>").append(item.getRowId()).append("</ori:RowId>");
				xmlInfo.append("<ori:ServerId>").append(item.getServerId()).append("</ori:ServerId>");
				xmlInfo.append("<ori:UnlockType>").append(item.getUnlockType()).append("</ori:UnlockType>");
				xmlInfo.append("<ori:UserId>").append(item.getUserId()).append("</ori:UserId>");
				xmlInfo.append("</ori:ValidteBetRequest>");
			}
			xmlInfo.append("</adm:validateRequests>" +
			"      </adm:ManuallyValidateBet>" +
			"   </soapenv:Body>" +
			"</soapenv:Envelope>");

			String response = sendRequst(url, soapAction, xmlInfo.toString());
			System.out.println(response);
		}
	}
	
	/**
	 * This method enables you to retrieve a list of games that have completed in the Microgaming system,
	 * so that you can reconcile them in your system.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void getFailedEndGameQueue() throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/GetFailedEndGameQueue";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetFailedEndGameQueue>\n" +
		"         <!--Optional:-->\n" +
		"         <adm:serverIds>\n" +
		"            <!--Zero or more repetitions:-->\n" +
		"            <arr:int>" + serverId + "</arr:int>\n" +
		"         </adm:serverIds>\n" +
		"      </adm:GetFailedEndGameQueue>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";

		String response = sendRequst(url, soapAction, xmlInfo);
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetFailedEndGameQueueResponse");
		if(null != resultEle){
			GetFailedEndGameQueueRoot failedEndQueue = (GetFailedEndGameQueueRoot) unmarshalXml2Bean(resultEle.asXML(), GetFailedEndGameQueueRoot.class);
			System.out.println(failedEndQueue.getGetFailedEndGameQueueResult().size());
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method enables you to mark items in the Vanguard Complete Game queue as complete.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void manuallyCompleteGame(List<CompleteGameRequest> completeGameReq) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/ManuallyCompleteGame";
		StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:ManuallyCompleteGame>\n");
		if(completeGameReq.size() > 0){
			xmlInfo.append("<!--Optional:-->\n").append("<adm:requests>");
			for (CompleteGameRequest item : completeGameReq) {
				xmlInfo.append("<ori:CompleteGameRequest>");
				if(item.getRowId() != null)
					xmlInfo.append("<ori:RowId>" + item.getRowId() + "</ori:RowId>");
				if(item.getRowIdLong() != null)
					xmlInfo.append("<ori:RowIdLong>" + item.getRowIdLong() + "</ori:RowIdLong>");
				if(item.getServerId() != null)
					xmlInfo.append("<ori:ServerId>" + item.getServerId() + "</ori:ServerId>");
				xmlInfo.append("</ori:CompleteGameRequest>");
			}
			xmlInfo.append("</adm:requests>");
		}
		
		xmlInfo.append("</adm:ManuallyCompleteGame></soapenv:Body></soapenv:Envelope>");
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("ManuallyCompleteGameResponse");
		if(null != resultEle){
			ManuallyCompleteGameRoot completeGameResult = (ManuallyCompleteGameRoot) unmarshalXml2Bean(resultEle.asXML(), ManuallyCompleteGameRoot.class);
			 
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method validates an API user and returns all the Server ID’s that the API user is authorised on. 
	 * This method is not required if you already know the Server ID’s you are assigned to.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void validateApiUser() throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/ValidateApiUser";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:ValidateApiUser>\n" +
		"         <!--Optional:-->\n" +
		"         <adm:username>" + userName + "</adm:username>\n" +
		"         <!--Optional:-->\n" +
		"         <adm:password>" + password + "</adm:password>\n" +
		"      </adm:ValidateApiUser>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());   
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("ValidateApiUserResponse");
		if(null != resultEle){
			ValidateApiUserResult apiUserResult = (ValidateApiUserResult) unmarshalXml2Bean(resultEle.asXML(), ValidateApiUserResult.class);
			
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method moves a pending cash-in for a given LoginName, ServerId and TransactionNumber
     * from the pending cash-in table to the cash-in log table, and removes the entry from the pending cash-in table.
	 */
	public static void flushPendingCashin(List<FlushPendingCashinRequest> reqList){
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/FlushPendingCashin";
		StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:FlushPendingCashin>");
		if(reqList.size() > 0){
			xmlInfo.append("<!--Optional:-->");
			xmlInfo.append("<adm:requests>");
			xmlInfo.append("<!--Zero or more repetitions:-->");
			for (FlushPendingCashinRequest item : reqList) {
				xmlInfo.append("<ori:FlushPendingCashinRequest>");
				xmlInfo.append("<!--Optional:-->");
				xmlInfo.append("<ori:LoginName>").append(item.getLoginName()).append("</ori:LoginName>");
				xmlInfo.append("<ori:ServerId>").append(item.getServerId()).append("</ori:LoginName>");
				xmlInfo.append("<ori:TransactionNumber>").append(item.getTransactionNumber()).append("</ori:TransactionNumber>");
				xmlInfo.append("</ori:FlushPendingCashinRequest>");
			}
			xmlInfo.append("</adm:requests>");
		}
		xmlInfo.append("</adm:FlushPendingCashin>").append("</soapenv:Body>").append("</soapenv:Envelope>");
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
	}
	
	/**
	 * This method provides a list of pending cash-ins based on the ServerId and LoginName specified. 
	 * The list can show all pending cash-ins, or be limited to those within a specified StartDate and EndDate. 
	 * By selecting all cash-ins, you retrieve a list of pending and processed cash-ins. 
	 * By using the PendingCashinOnly attribute, you retrieve only pending cash-ins.
	 * 
	 * 获取累计奖池中奖结果
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void getProgressiveWinCashinReport(List<ProgressiveWinCashinReportRequest> reqList) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/VanguardAdmin/SOAP2";
		String soapAction = "http://mgsops.net/AdminAPI_Admin/IVanguardAdmin2/GetProgressiveWinCashinReport";
		StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Admin\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures\">\n" +
				"   <soapenv:Header/>\n" +
				"   <soapenv:Body>\n" +
				"      <adm:GetProgressiveWinCashinReport>");
		if(reqList.size() > 0){
			xmlInfo.append("<adm:requests>");
			for (ProgressiveWinCashinReportRequest item : reqList) {
				xmlInfo.append("<ori:ProgressiveWinCashinReportRequest>");
				xmlInfo.append("<ori:EndDate>").append(item.getEndDate()).append("</ori:EndDate>");
				xmlInfo.append("<ori:PendingCashinOnly>").append(item.getPendingCashinOnly()).append("</ori:PendingCashinOnly>");
				xmlInfo.append("<ori:ServerId>").append(item.getServerId()).append("</ori:ServerId>");
				xmlInfo.append("<ori:StartDate>").append(item.getStartDate()).append("</ori:StartDate>");
				xmlInfo.append("<ori:ProgressiveWinCashinReportRequest>");
			}
			xmlInfo.append("</adm:requests>");
		}
		xmlInfo.append("</adm:GetProgressiveWinCashinReport></soapenv:Body></soapenv:Envelope>");
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetProgressiveWinCashinReportResponse");
		if(null != resultEle){
			GetProgressiveWinCashinReportRoot progressiveWinCashinReportResult = (GetProgressiveWinCashinReportRoot) unmarshalXml2Bean(resultEle.asXML(), GetProgressiveWinCashinReportRoot.class);
			
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	
	
	
	//   ###################     Free Games      ###################
	/**
	 * The Free Games system is a player acquisition and retention tool that enables you to award free wagers or games to real account players. 
	 * These offers are pre-configured in the Microgaming system and variables such as number of paylines, coin size and number of bets is already assessed. 
	 * If you have any questions about new offers, please contact your Service Manager.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	
	public static List<Offer> getFreegames() throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/GetFreegames";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">\n" +
		"   <soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:Action>http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/GetFreegames</wsa:Action></soapenv:Header>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetFreegames>\n" +
		"         <!--Optional:-->\n" +
		"         <adm:request>\n" +
		"            <!--Optional:-->\n" +
		"            <ori:ServerId>" + serverId + "</ori:ServerId>\n" +
		"         </adm:request>\n" +
		"      </adm:GetFreegames>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetFreegamesResponse");
		if(null != resultEle){
			GetFreegamesRoot freegames = (GetFreegamesRoot) unmarshalXml2Bean(resultEle.asXML(), GetFreegamesRoot.class);
			return freegames.getOffers().getOfferList();
		}else{
			//异常
			log.error(ele.asXML());
			throw new GenericDfhRuntimeException("Orion接口返回异常");
		}
	}
	
	/**
	 * This method enables you to view the players assigned to a specific free games offer. 
	 * Using this method you can see how many instances of the offer a player is awarded, 
	 * the number of games a player has left over, and a player’s current cumulative winnings for that instance of the offer.
	 * 
	 * 该方法可以查看指定免费游戏Offer下的玩家数量。 使用这个方法， 你可以知道一个玩家在被派发了多少个指定免费游戏的offer， 还剩多少次免费游戏、 当前累计赢得
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static GetPlayersByOfferResponse getPlayersByFreegame(GetPlayersByOfferRequest req) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/GetPlayersByFreegame";
		StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">\n" +
		"   <soapenv:Header/>" +
		"   <soapenv:Body>" +
		"      <adm:GetPlayersByFreegame>" +
		"         <adm:request>" +
		"            <ori:OfferId>" + req.getOfferId() + "</ori:OfferId>");
		if(null != req.getOfferStatusId()){
			xmlInfo.append("<ori:OfferStatusId>" + req.getOfferStatusId() + "</ori:OfferStatusId>");
		}else{
			xmlInfo.append("<ori:OfferStatusId>" + OfferStatusId.ALL.getCode() + "</ori:OfferStatusId>");
		}
		xmlInfo.append("<ori:PageNumber>" + req.getPageNumber() + "</ori:PageNumber>" +
		"            <ori:PageSize>" + req.getPageSize() + "</ori:PageSize>" +
		"            <ori:ServerId>" + req.getServerId() + "</ori:ServerId>" +
		"         </adm:request>" +
		"      </adm:GetPlayersByFreegame>" +
		"   </soapenv:Body>" +
		"</soapenv:Envelope>");
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetPlayersByFreegameResponse");
		if(null != resultEle){
			GetPlayersByFreegameRoot playersOfFreegame = (GetPlayersByFreegameRoot) unmarshalXml2Bean(resultEle.asXML(), GetPlayersByFreegameRoot.class);
			return playersOfFreegame.getPlayersOfFreegame();
		}else{
			//异常
			log.error(ele.asXML());
			throw new GenericDfhRuntimeException("Orion接口返回异常");
		}
	}
	
	/**
	 * This method enables you to add players to a single free games offer or to multiple offers. 
	 * If you add a player to an offer multiple times, the player receives multiple instances of the offer.
	 * 
	 * 给玩家派发免费游戏
	 * @throws DocumentException 
	 * @throws JAXBException 
	 * @return 是否有错误
	 */
	public static Boolean addPlayersToFreegame(AddPlayersToOfferRequest req) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/AddPlayersToFreegame";
		StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">" +
		"<soapenv:Header/><soapenv:Body><adm:AddPlayersToFreegame><adm:request><ori:PlayerActions>");
		for (PlayerAction playerAction : req.getPlayerActions()) {
			xmlInfo.append("<ori:PlayerAction>");
			//xmlInfo.append("<ori:Error>" + playerAction.getError() + "</ori:Error>");
			xmlInfo.append("<ori:ISOCurrencyCode>" + playerAction.getISOCurrencyCode() + "</ori:ISOCurrencyCode>");
			xmlInfo.append("<ori:LoginName>" + playerAction.getLoginName() + "</ori:LoginName>");
			xmlInfo.append("<ori:PlayerStartDate>" + formatDate4Soap(playerAction.getPlayerStartDate()) + "</ori:PlayerStartDate>");
			//xmlInfo.append("<ori:Success>" + playerAction.getSuccess() + "</ori:Success>");
			//xmlInfo.append("<ori:InstanceId>" + playerAction.getInstanceId() + "</ori:InstanceId>");
			xmlInfo.append("<ori:OfferId>" + playerAction.getOfferId() + "</ori:OfferId>");
			xmlInfo.append("</ori:PlayerAction>");
		}
		xmlInfo.append("</ori:PlayerActions>");
		xmlInfo.append("<ori:Sequence>" + req.getSequence() + "</ori:Sequence>");
		xmlInfo.append("<ori:ServerId>" + req.getServerId() + "</ori:ServerId>");
		xmlInfo.append("</adm:request></adm:AddPlayersToFreegame></soapenv:Body></soapenv:Envelope>");
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("AddPlayersToFreegameResponse");
		if(null != resultEle){
			AddPlayersToFreegameRoot addPlayersToFreegameRoot = (AddPlayersToFreegameRoot) unmarshalXml2Bean(resultEle.asXML(), AddPlayersToFreegameRoot.class);
			return addPlayersToFreegameRoot.getAddPlayersToFreegameResult().getHasErrors();
		}else{
			//异常
			log.error(ele.asXML());
			throw new GenericDfhRuntimeException("Orion接口返回异常");
		}
	}
	
	/**
	 * This method enables you to view the active free game offers that a specific player has been assigned to, filtered by offer status.
	 * 
	 * 获取玩家被分配的有效免费游戏优惠 根据优惠状态区分
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void getFreegamesByPlayer(GetOffersByPlayerRequest req) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/GetFreegamesByPlayer";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetFreegamesByPlayer>\n" +
		"         <adm:request>\n" +
		"            <ori:OfferStatusId>" + req.getOfferStatusId() + "</ori:OfferStatusId>\n" +
		"            <ori:PageNumber>" + req.getPageNumber() + "</ori:PageNumber>\n" +
		"            <ori:PageSize>" + req.getPageSize() + "</ori:PageSize>\n" +
		"            <ori:PlayerName>" + req.getPlayerName() + "</ori:PlayerName>\n" +
		"            <ori:ServerId>" + req.getServerId() + "</ori:ServerId>\n" +
		"         </adm:request>\n" +
		"      </adm:GetFreegamesByPlayer>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";
		
		String response = sendRequst(url, soapAction, xmlInfo);
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetFreegamesByPlayerResponse");
		if(null != resultEle){
			GetFreegamesByPlayerRoot offersOfPlayer = (GetFreegamesByPlayerRoot) unmarshalXml2Bean(resultEle.asXML(), GetFreegamesByPlayerRoot.class);
			
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method enables you to view the active free game offers for the selected casino, filtered by the player level offer status.
	 * The GetFreegamesByPlayerOfferStatus response format is the same as the GetFreegames method, but is filter by player level offer status.
	 * 
	 * 
	 * 和获取免费游戏接口相同， 但多一个优惠状态的过滤条件
	 * 
	 * @param req
	 * @throws DocumentException 
	 * @throws JAXBException 
	 */
	public static void getFreegamesByPlayerOfferStatusField(GetOffersByPlayerOfferStatusRequest req) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/GetFreegamesByPlayerOfferStatus";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetFreegamesByPlayerOfferStatus>\n" +
		"         <adm:request>\n" +
		"            <ori:OfferStatusId>" + req.getOfferStatusId() + "</ori:OfferStatusId>\n" +
		"            <ori:PageNumber>" + req.getPageNumber() + "</ori:PageNumber>\n" +
		"            <ori:PageSize>" + req.getPageSize() + "</ori:PageSize>\n" +
		"            <ori:ServerId>" + req.getServerId() + "</ori:ServerId>\n" +
		"         </adm:request>\n" +
		"      </adm:GetFreegamesByPlayerOfferStatus>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";
		
		String response = sendRequst(url, soapAction, xmlInfo);
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetFreegamesByPlayerOfferStatusResponse");
		if(null != resultEle){
			GetFreegamesByPlayerRoot offersOfPlayer = (GetFreegamesByPlayerRoot) unmarshalXml2Bean(resultEle.asXML(), GetFreegamesByPlayerRoot.class);
			
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method enables you to view offer and player information of players assigned to a specific free games offer. 
	 * Using this method you can see the offer description, how many instances of the offer a player is awarded, 
	 * the number of games a player has remaining, and a player’s current cumulative winnings for that instance of the offer
	 * @throws DocumentException 
	 * @throws JAXBException 
	 * 
	 * 
	 */
	public static void getFreegamesPlayerDetails(GetOffersPlayerDetailsRequest req) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/GetFreegamesPlayerDetails";
		String xmlInfo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">\n" +
		"   <soapenv:Header/>\n" +
		"   <soapenv:Body>\n" +
		"      <adm:GetFreegamesPlayerDetails>\n" +
		"         <adm:request>\n" +
		"            <ori:OfferId>" + req.getOfferId() + "</ori:OfferId>\n" +
		"            <ori:PageNumber>" + req.getPageNumber() + "</ori:PageNumber>\n" +
		"            <ori:PageSize>" + req.getPageSize() + "</ori:PageSize>\n" +
		"            <ori:ServerId>" + req.getServerId() + "</ori:ServerId>\n" +
		"         </adm:request>\n" +
		"      </adm:GetFreegamesPlayerDetails>\n" +
		"   </soapenv:Body>\n" +
		"</soapenv:Envelope>";
		
		String response = sendRequst(url, soapAction, xmlInfo);
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("GetFreegamesPlayerDetailsResponse");
		if(null != resultEle){
			GetFreegamesPlayerDetailsRoot offersPlayerDetails = (GetFreegamesPlayerDetailsRoot) unmarshalXml2Bean(resultEle.asXML(), GetFreegamesPlayerDetailsRoot.class);
			
		}else{
			//异常
			log.error(ele.asXML());
		}
	}
	
	/**
	 * This method enables you to remove players from a single free games offer or from multiple offers.
	 * @throws DocumentException 
	 * @throws JAXBException 
	 * 
	 * 必须参数 serverid, instanceid , offerid, userid/loginname
	 */
	public static Boolean removePlayersFromFreegame(RemovePlayersFromOfferRequest req) throws DocumentException, JAXBException{
		String url = serviceHost + "/Orion/FreegameAdmin/SOAP";
		String soapAction = "http://mgsops.net/AdminAPI_Freegame/IFreegameAdmin/RemovePlayersFromFreegame";
		StringBuilder xmlInfo = new StringBuilder("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:adm=\"http://mgsops.net/AdminAPI_Freegame\" xmlns:ori=\"http://schemas.datacontract.org/2004/07/Orion.Contracts.FreegameAdmin.DataStructures\">" +
		"<soapenv:Header/><soapenv:Body><adm:RemovePlayersFromFreegame><adm:request><ori:PlayerActions>");
		for (PlayerAction playerAction : req.getPlayerActions()) {
			xmlInfo.append("<ori:PlayerAction>");
			//xmlInfo.append("<ori:Error>" + playerAction.getError() + "</ori:Error>");
			//xmlInfo.append("<ori:ISOCurrencyCode>" + playerAction.getISOCurrencyCode() + "</ori:ISOCurrencyCode>");
			xmlInfo.append("<ori:LoginName>" + playerAction.getLoginName() + "</ori:LoginName>");
			//xmlInfo.append("<ori:PlayerStartDate>" + formatDate4Soap(playerAction.getPlayerStartDate()) + "</ori:PlayerStartDate>");
			//xmlInfo.append("<ori:Success>" + playerAction.getSuccess() + "</ori:Success>");
			xmlInfo.append("<ori:InstanceId>" + playerAction.getInstanceId() + "</ori:InstanceId>");
			xmlInfo.append("<ori:OfferId>" + playerAction.getOfferId() + "</ori:OfferId>");
			xmlInfo.append("</ori:PlayerAction>");
		}
		xmlInfo.append("</ori:PlayerActions>");
		xmlInfo.append("<ori:Sequence>" + req.getSequence() + "</ori:Sequence>");
		xmlInfo.append("<ori:ServerId>" + req.getServerId() + "</ori:ServerId>");
		xmlInfo.append("</adm:request></adm:RemovePlayersFromFreegame></soapenv:Body></soapenv:Envelope>");
		
		String response = sendRequst(url, soapAction, xmlInfo.toString());
		Element ele = getBodyElement(response);
		Element resultEle = ele.element("RemovePlayersFromFreegameResponse");
		if(null != resultEle){
			RemovePlayersFromOfferRoot removePlayersToFreegameRoot = (RemovePlayersFromOfferRoot) unmarshalXml2Bean(resultEle.asXML(), RemovePlayersFromOfferRoot.class);
			return removePlayersToFreegameRoot.getRemovePlayersFromOffer().getHasErrors();
		}else{
			//异常
			log.error(ele.asXML());
			throw new GenericDfhRuntimeException("Orion接口返回异常");
		}
	}
	
	// ##########################  Orion Achievements ######################
	/**
	 * The Achievements interface enables you to view a list of players who have triggered achievements in the last 24 hours.
	 * 24小时内，触发成就系统的玩家
	 */
	 
	/**
	 * 
	 */
	public static void getServerAchievements(){
		
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate4Soap(Date date){
		return sdf4MG.format(date).replace(" ", "T");
	}
	
	public static void main(String[] args) throws DocumentException, JAXBException {
		/*RemovePlayersFromOfferRequest req = new RemovePlayersFromOfferRequest();
		req.setServerId(serverId);
		req.setSequence("201606016" + RandomStringUtils.randomAlphanumeric(6));
		List<PlayerAction> paList = new ArrayList<PlayerAction>();
		PlayerAction pa = new PlayerAction();
		pa.setInstanceId(4);
		pa.setLoginName("woodytest");
		pa.setISOCurrencyCode("CNY");
		pa.setOfferId(150);
		pa.setPlayerStartDate(new Date());
		
		req.setPlayerActions(paList);
		removePlayersFromFreegame(req);*/
		
		//validateApiUser();
		//getCommitQueueData();
		/*List<ValidteBetRequest> list = new ArrayList<ValidteBetRequest>();
		ValidteBetRequest req = new ValidteBetRequest();
		req.setServerId(serverId);
		req.setRowId(Long.parseLong(String.valueOf(-1274)));
		req.setUnlockType("CommitQueue");
		req.setUserId(32315);
		list.add(req);
		manuallyValidateBet(list);
		getFailedEndGameQueue();
		List<CompleteGameRequest> list = new ArrayList<CompleteGameRequest>();
		CompleteGameRequest req = new CompleteGameRequest();
		req.setServerId(serverId);
		req.setRowId(-1275);
		list.add(req);
		manuallyCompleteGame(list);*/
		getFailedEndGameQueue();
	}
}
