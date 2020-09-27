package dfh.action.customer;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;
import dfh.utils.StringUtil;

public class GameAction extends SubActionSupport {

	private static final long serialVersionUID = 765605592803504705L;
	private Logger log = Logger.getLogger(GameAction.class);
    
	private String gameCode;
	private String mode;
	private String lobby ;
	private String gameUrl;
	private String errormsg;
	private String gameKind;
	public String getGameKind() {
		return gameKind;
	}

	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}


	/**
	 * 登录泛亚电竞游戏验证
	 *
	 * @return
	 */
	public String fanyaLogin() {

		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "fanyaLogin",
					new Object[] {user.getLoginname()}, String.class);
			System.out.println(gameUrl+"gameUrl");
			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl==null)
				return INPUT;

			return SUCCESS;

		} catch (Exception e) {
			log.info(e.getMessage());
			return ERROR;
		}
	}

	//bbin  pc
	public String bbinLogin() {
		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
			if(!"game".equals(gameKind) && !"live".equals(gameKind)) {
				gameKind = "live";//参数为空默认到真人
			}

			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "bbinLogin",
					new Object[] {user.getLoginname(),gameKind,gameCode,mode}, String.class);

			log.info("gameUrl:"+gameUrl);
			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl==null)
				return INPUT;

			return SUCCESS;

		} catch (Exception e) {
			log.info(e.getMessage());
			return ERROR;
		}
	}
	//bbin web
	public String bbinMobiLogin() {
		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
			if(!"game".equals(gameKind) && !"live".equals(gameKind)) {
				gameKind = "live";//参数为空默认到真人
			}

			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "bbinMobiLogin",
					new Object[] {user.getLoginname(),gameCode}, String.class);

			log.info("gameUrl:"+gameUrl);
			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl==null)
				return INPUT;

			return SUCCESS;

		} catch (Exception e) {
			log.info(e.getMessage());
			return ERROR;
		}
	}


	/**
	 * 登录平博体育
	 *
	 * @return
	 */
	public String PBUserLogin() {
		log.info("web登录平博体育开始");
		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
			// 控制游戏开关
			String flag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "PBUserLoginFlag",
					new Object[] {user.getLoginname()}, String.class);

			log.info("flag:"+flag);
			if (StringUtil.equals("MAINTAIN", flag)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}

			MemberAction memberAction = new MemberAction();
			gameUrl = memberAction.PBUserLogin();

			log.info("gameUrl:"+gameUrl);
			if (gameUrl==null)
				return INPUT;
			log.info("web登录平博体育成功");
			return SUCCESS;

		} catch (Exception e) {
			log.info("web登录平博体育失败");
			log.info(e.getMessage());
			return ERROR;
		}
	}
    
    
   
	public String validateParam(String gamecode,String mode) {
        if (StringUtil.isBlank(gamecode)&StringUtil.isBlank(mode)) {
           return "请求的字段存在字段未空!";
        }
		return null;
    }



	public String getGameCode() {
		return gameCode;
	}


	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}



	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getGameUrl() {
		return gameUrl;
	}


	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}





	public String getLobby() {
		return lobby;
	}





	public void setLobby(String lobby) {
		this.lobby = lobby;
	}



	public String getErrormsg() {
		return errormsg;
	}



	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}


    
	 /**
     * 登录DT游戏验证
     *
     * @return
     */
    public String reqGameRedirectDT() {
        try {
        	
            if (validateParam(gameCode, mode)!=null) {
            	 return INPUT;
			}
            
            Users user = getCustomerFromSession();
            
 
        	int port = this.getRequest().getServerPort();
			String reloadUrl = this.getRequest().getScheme()+"://"+this.getRequest().getServerName() + ((port == 0 || port == 80 || port == 443)? "":":" + port);
			
   			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getDtGameUrl",
 					   new Object[] {user,gameCode,reloadUrl,mode,getIp() }, String.class);
   			System.out.print("打印gameUrl========"+gameUrl);
 
   			if (StringUtil.equals("MAINTAIN", gameUrl)) {
   				  GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
			      return INPUT;
   			}
   			if (gameUrl==null) 
			      return INPUT;
   			
            return SUCCESS;
           
        } catch (Exception e) {
        	log.info(e.getMessage());
            return ERROR;
        }
    }
    
    
    /**
     * 登录DT捕鱼
     *
     * @return
     */
    public String reqGameRedirectDTFish() {
        try {
        	
            Users user = getCustomerFromSession();
            
 
        	int port = this.getRequest().getServerPort();
			String reloadUrl = this.getRequest().getScheme()+"://"+this.getRequest().getServerName() + ((port == 0 || port == 80 || port == 443)? "":":" + port);
			
   			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getDtFishGameUrl",
 					   new Object[] {user}, String.class);
 
   			if (StringUtil.equals("MAINTAIN", gameUrl)) {
   				  GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
			      return INPUT;
   			}
   			if (gameUrl==null) 
			      return INPUT;
            return SUCCESS;
           
        } catch (Exception e) {
        	log.info(e.getMessage());
            return ERROR;
        }
    }
    
    /**
     * 登录cg761棋牌游戏验证
     *
     * @return
     */
    public String cg761Login() {
    	try {
    		Users user = getCustomerFromSession();
    		if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
    		
    		gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "cg761Login",
    				new Object[] {user.getLoginname(),gameCode}, String.class);
    		
    		log.info("gameCode:"+gameCode+"#######################gameUrl:"+gameUrl);
    		if (StringUtil.equals("MAINTAIN", gameUrl)) {
    			GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
    			return INPUT;
    		}
    		if (gameUrl==null) 
    			return INPUT;
    		
    		return SUCCESS;
    		
    	} catch (Exception e) {
    		log.info(e.getMessage());
    		return ERROR;
    	}
    }
	/**
	 * 登录开元棋牌游戏验证
	 *
	 * @return
	 */
	public String kyqpLogin() {
		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "kyqpLogin",
					new Object[] {user.getLoginname(),gameCode,getIp()}, String.class);

			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl==null)
				return INPUT;

			return SUCCESS;

		} catch (Exception e) {
			log.info(e.getMessage());
			return ERROR;
		}
	}

	/**
	 * 登录VR游戏验证
	 *
	 * @return
	 */
	public String vrLogin() {
		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}
			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "vrLogin",
					new Object[] {user.getLoginname(),getIp()}, String.class);

			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl==null)
				return INPUT;

			return SUCCESS;

		} catch (Exception e) {
			log.info(e.getMessage());
			return ERROR;
		}
	}

	/**
	 * 登陆比特游戏
	 *
	 * @return
	 */
	public String bitGameUserLogin() {
		log.info("登陆比特游戏开始");
		try {
			Users user = getCustomerFromSession();
			if( user == null){
				GsonUtil.GsonObject("请登录后，在进行操作");
				return ERROR;
			}

			gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "bitGameUserLogin",
					new Object[] {user.getLoginname(),gameCode}, String.class);

			log.info("gameUrl:"+gameUrl);
			if (StringUtil.equals("MAINTAIN", gameUrl)) {
				log.info("比特游戏维护中");
				GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
				return INPUT;
			}
			if (gameUrl==null)
				return INPUT;
			log.info("登陆比特游戏成功");
			return SUCCESS;

		} catch (Exception e) {
			log.info("登陆比特游戏异常");
			log.info(e.getMessage());
			return ERROR;
		}
	}
	
    /**
     * 登录SW
     * mode fun 试玩  real 真钱
     * @return
     */
    public String gameLoginPtSW() {
    	try {
    		
    		Users user = getCustomerFromSession();
    		if (user == null) {
    			GsonUtil.GsonObjectNoReturn("尚未登录！请重新登录！");
    			return ERROR;
    		}
    		String loginname = user.getLoginname();
    		if(loginname.startsWith("a_")){
    			GsonUtil.GsonObjectNoReturn("代理账号不可玩游戏！");
    			return ERROR;
    		}
    		gameUrl = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "swLogin",
    				new Object[] {user.getLoginname(),gameCode,mode}, String.class);
    		
    		if (StringUtil.equals("MAINTAIN", gameUrl)) {
    			GsonUtil.GsonObjectNoReturn("游戏维护中,稍后再试");
    			return ERROR;
    		}
    		if (gameUrl==null){
    			GsonUtil.GsonObjectNoReturn("获取游戏链接地址异常,稍后再试");
    			return ERROR;
    		}
    		return SUCCESS;
    		
    	} catch (Exception e) {
    		log.info(e.getMessage());
    		return ERROR;
    	}
    }
}
