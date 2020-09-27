package dfh.action.office;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ibm.icu.text.SimpleDateFormat;
import com.nnti.office.model.auth.Operator;

import dfh.action.SubActionSupport;
import dfh.icbc.quart.fetch.MGDataToKPIService;
import dfh.mgs.OrionUtil;
import dfh.mgs.vo.freegame.GetPlayersByOfferRequest;
import dfh.mgs.vo.freegame.GetPlayersByOfferResponse;
import dfh.mgs.vo.freegame.Offer;
import dfh.mgs.vo.freegame.PlayerAction;
import dfh.mgs.vo.freegame.RemovePlayersFromOfferRequest;
import dfh.model.MGFreeGameOffer;
import dfh.service.interfaces.MGSService;
import dfh.service.interfaces.OperatorService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.Page;
import dfh.utils.PageQuery;

public class MGSAction extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(MGSAction.class);
	
	private OperatorService operatorService;
	private MGSService mgsService;
	
	private Integer size;
	private Integer pageIndex;
	private String description;
	private Integer freeGameID;
	private String players;
	private Integer offerStatusId;
	private Integer instanceId;
	private Date date;
	
	public void updateMGFreeGames() throws IOException{
		this.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter out = this.getResponse().getWriter();
		String msg = null;
		try {
			List<Offer> offers = OrionUtil.getFreegames();
			operatorService.updateMGFreeGames(offers);
			msg = "MG免费游戏更新完毕";
		} catch (Exception e) {
			msg = "MG免费游戏更新异常";
			e.printStackTrace();
		}finally{
			out.println(msg);
			out.flush();
			if(null != out){
				out.close();
			}
		}
	}

	public String queryMGFreeGames(){
		DetachedCriteria dc = DetachedCriteria.forClass(MGFreeGameOffer.class);

		if (StringUtils.isNotBlank(description)) {
			dc.add(Restrictions.eq("description", description));
		}
		Page page = PageQuery.queryForPagenation(operatorService.getHibernateTemplate(), dc, pageIndex, size, null);
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	
	public void addPlayersToFreegame() throws DocumentException, JAXBException, IOException{
		this.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter out = this.getResponse().getWriter();
		String msg = null;
		String[] playerArr = players.trim().split("#");
		String operator = ((Operator)getHttpSession().getAttribute(Constants.SESSION_OPERATORID)).getUsername();
		if(playerArr.length > 0){
			try {
				if(operatorService.addPlayersToFreegame(playerArr, freeGameID, operator)){   //是否存在错误
					msg = "派发免费游戏出现错误，请查询该免费游戏下的玩家进行确认";
				}else{
					msg = "派发免费游戏成功";
				}
			} catch (Exception e) {
				msg = "派发免费游戏异常";
				e.printStackTrace();
			} finally {
				out.println(msg);
				out.flush();
				if(null != out){
					out.close();
				}
			}
		}
	}

	public String getPlayersByFreegame() throws IOException, DocumentException, JAXBException{
		GetPlayersByOfferRequest req = new GetPlayersByOfferRequest();
		req.setServerId(OrionUtil.serverId);
		req.setPageNumber(pageIndex);
		req.setPageSize(size);
		req.setOfferId(freeGameID);
		if(null != offerStatusId){
			req.setOfferStatusId(offerStatusId);
		}
		GetPlayersByOfferResponse result = OrionUtil.getPlayersByFreegame(req);
		//getRequest().setAttribute("freeGameID", freeGameID);
		Page page = new Page(result.getPlayersInOffer());
		page.setSize(size);
		page.setPageNumber(pageIndex);
		page.setTotalPages(result.getTotalPages());
		getRequest().setAttribute("page", page);
		return INPUT;
	}
	
	public void removePlayersFromFreegame() throws DocumentException, JAXBException, IOException{
		this.getResponse().setCharacterEncoding("UTF-8");
		PrintWriter out = this.getResponse().getWriter();
		RemovePlayersFromOfferRequest orioinReq = new RemovePlayersFromOfferRequest();
		orioinReq.setServerId(OrionUtil.serverId);
		orioinReq.setSequence(DateUtil.fmtyyyyMMdd(new Date()) + RandomStringUtils.randomAlphanumeric(8));
		PlayerAction pa = new PlayerAction();
		pa.setInstanceId(instanceId);
		pa.setLoginName(players);
		pa.setOfferId(freeGameID);
		orioinReq.getPlayerActions().add(pa);
		try {
			if(OrionUtil.removePlayersFromFreegame(orioinReq)){
				out.println("移除免费游戏优惠发生异常，请确认");
			}else{
				out.println("免费游戏优惠已移除");
			}
		} catch (Exception e) {
			out.println("移除免费游戏优惠发生异常，请确认");
			log.error("移除免费游戏优惠发生异常", e);
		}finally {
			out.flush();
			if(null != out){
				out.close();
			}
		}
	}
	
	public  void repairMGDataToKPI(){
		
		if (date!=null) {
			SimpleDateFormat dfHH = new SimpleDateFormat("HH");
			Date date2 = new Date();
			Integer hh = Integer.parseInt(dfHH.format(date2));
			if (hh < 6) 
				GsonUtil.GsonObject("0-6点间不允许操作");
			
			MGDataToKPIService mgDataToKPIService=new MGDataToKPIService();
			String msg=mgDataToKPIService.repairMGData2KPI(mgsService,date);
			GsonUtil.GsonObject(">>>>>>>>:"+msg);
		}else 
			GsonUtil.GsonObject("时间不能为空");
	}
	
	public OperatorService getOperatorService() {
		return operatorService;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public Integer getSize() {
		return size;
	}


	public void setSize(Integer size) {
		this.size = size;
	}


	public Integer getPageIndex() {
		return pageIndex;
	}


	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFreeGameID() {
		return freeGameID;
	}

	public void setFreeGameID(Integer freeGameID) {
		this.freeGameID = freeGameID;
	}

	public String getPlayers() {
		return players;
	}

	public void setPlayers(String players) {
		this.players = players;
	}

	public Integer getOfferStatusId() {
		return offerStatusId;
	}

	public void setOfferStatusId(Integer offerStatusId) {
		this.offerStatusId = offerStatusId;
	}

	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	public MGSService getMgsService() {
		return mgsService;
	}

	public void setMgsService(MGSService mgsService) {
		this.mgsService = mgsService;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
