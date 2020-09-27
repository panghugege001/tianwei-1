package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 * Gmeplay Interactive 老虎机接口工具类
 * 1.使用HttpClient一直提示406错误，改用urlconnetion
 * 2.调用其他方法时，如果账号不存在，GPI平台会自动创建该账号
 * 
 */
public class GPIUtil {

	private static Logger log = Logger.getLogger(GPIUtil.class);
	
	private static final String walletURL = "https://club8api.w88.com";
	//create user
	private static final String createUserAction = "/op/createuser";
	//getbalance
	private static final String getbalanceAction = "/op/getbalance";
	//debit
	private static final String debitAction = "/op/debit";
	//credit
	private static final String creditAction = "/op/credit";
	//Check Transaction Result
	private static final String checkAction = "/op/check";
	//历史投注记录
	private static final String historyPulling = "http://slotservice.gpiops.com/integration.asmx/gettransactionhistory";	
	                                              
	//gpi 3D老虎机登入地址
	private static final String slotGameUrl = "http://slots.gpiops.com";
	//RSLots类老虎机游戏
	private static final String rslotsUrl = "http://rslots.gpiops.com";
	//png登入地址
	private static final String pngGameURl = "http://casino.gpiops.com/mini/PNG.html";
	//bs登入地址
	private static final String bsGameUrl = "http://casino.gpiops.com/mini/betSoft.html";
	//ctxm登入地址
	private static final String ctxmGameUrl = "http://casino.gpiops.com/mini/";
	
	//gpi mobile
	private static final String gpiMobileUrl = "http://mslots.gpiops.com/";
	//private static final String gpiMobileUrl = "http://mslots.globalintgames.com/";
	//png mobile
	private static final String pngMobileUrl = "http://casino.gpiops.com/mini/PNGMobile.html";
	
	private static final String merch_id = "E68";
	private static final String merch_pwd = "0F1C6065-1DEA-4A4F-88E3-AC130E0282B8";
	
	public static Map<String, String>  respCodeMap = new HashMap<String, String>();
	public static final String GPI_SUCCESS_CODE = "0";
	
	private static Map<String, String> rSlotMap = new HashMap<String, String>();
	
	static {
		respCodeMap.put("0", "成功");
		respCodeMap.put("1", "交易已存在");
		respCodeMap.put("-1", "未知错误");
		respCodeMap.put("-2", "玩家不存在");
		respCodeMap.put("-3", "令牌不存在或已失效");
		respCodeMap.put("-4", "余额不足");
		respCodeMap.put("-7", "玩家已被冻结");
		respCodeMap.put("-27", "参数类型不匹配");
		respCodeMap.put("-29", "货币不匹配");
		respCodeMap.put("-33", "无效的金额");
		respCodeMap.put("-48", "无效的密码");
		respCodeMap.put("-119", "缺少参数");
		respCodeMap.put("-203", "账号已存在");
		
		rSlotMap.put("lanternfestival", "闹花灯");
		rSlotMap.put("legendofnezha", "哪咤脑海");
		rSlotMap.put("deepblue", "深海探险");
		rSlotMap.put("goldeneggs", "金蛋鹅");
		rSlotMap.put("zeus", "宙斯-众神之王");
		rSlotMap.put("worldofwarlords", "英雄崛起");
		rSlotMap.put("pharaoh", "法老王");
		rSlotMap.put("qixi", "七夕情缘");
		rSlotMap.put("samuraisushi", "寿司武士");
		rSlotMap.put("fortunecat", "招财猫");
		rSlotMap.put("dimsumlicious", "舌尖上的点心");
		rSlotMap.put("genisys", "创世纪");
		rSlotMap.put("godofgamblers", "赌神");
	}
	
	public static String createUser(String username){
		log.info("gpi注册：" + username);

		Map<String, String> params = new HashMap<String, String>();
		params.put("merch_id", merch_id);
		params.put("merch_pwd", merch_pwd);
		params.put("cust_id", username);
		params.put("cust_name", username);
		params.put("currency", "RMB");
		params.put("test_user", "false");
		try {
			String result = sendPost(walletURL + createUserAction, params);
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/resp");
			if(!node.selectSingleNode("error_code").getStringValue().equals("0")){
				log.info("GPI用户注册失败：" + node.selectSingleNode("error_msg").getStringValue() + "      ###" + username);
				return "GPI用户注册失败";
			}
		} catch (IOException e) {
			log.error("GPI用户注册: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("GPI用户注册: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("GPI用户注册: Exception");
			log.error(e.getMessage());
		}
		return null;
	}
	
	
	public static Double getBalance(String username){
		log.info("gpi查询余额：" + username);

		Map<String, String> params = new HashMap<String, String>();
		params.put("merch_id", merch_id);
		params.put("merch_pwd", merch_pwd);
		params.put("cust_id", username);
		params.put("cust_name", username);
		params.put("currency", "RMB");
		params.put("test_user", "false");   //Is test user( true or false, default false ), used to create player if not exists
		try {
			String result = sendPost(walletURL + getbalanceAction, params);
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/resp");
			if(node.selectSingleNode("error_code").getStringValue().equals("0")){
				return Double.parseDouble(node.selectSingleNode("balance").getStringValue());
			}else{
				log.info("GPI查询余额失败：" + node.selectSingleNode("error_msg").getStringValue() + "      ###" + username);
				return null;
			}
		} catch (IOException e) {
			log.error("GPI查询余额: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("GPI查询余额: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("GPI查询余额: Exception");
			log.error(e.getMessage());
		}
		return null;
	}
	
	//转出
	public static String debit(String username, Double amount, String trx_id){
		log.info("gpi转出：" + username);

		Map<String, String> params = new HashMap<String, String>();
		params.put("merch_id", merch_id);
		params.put("merch_pwd", merch_pwd);
		params.put("cust_id", username);
		params.put("currency", "RMB");
		params.put("amount", amount + "");
		params.put("trx_id", trx_id);
		try {
			String result = sendPost(walletURL + debitAction, params);
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/resp");
			return node.selectSingleNode("error_code").getStringValue();
		} catch (IOException e) {
			log.error("GPI转出异常: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("GPI转出异常: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("GPI转出异常: Exception");
			log.error(e.getMessage());
		}
		return null;
	}
	
	//转入
	public static String credit(String username, Double amount, String trx_id){
		log.info("gpi转入：" + username);

		Map<String, String> params = new HashMap<String, String>();
		params.put("merch_id", merch_id);
		params.put("merch_pwd", merch_pwd);
		params.put("cust_id", username);
		params.put("currency", "RMB");
		params.put("amount", amount + "");
		params.put("trx_id", trx_id);
		params.put("test_cust", "false"); 
		try {
			String result = sendPost(walletURL + creditAction, params);
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/resp");
			return node.selectSingleNode("error_code").getStringValue();
		} catch (IOException e) {
			log.error("GPI转入异常: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("GPI转入异常: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("GPI转入异常: Exception");
			log.error(e.getMessage());
		}
		return null;
	}
	
	//查询转账结果
	public static String checkTransaction(String trx_id){
		log.info("gpi转账检查：" + trx_id);

		Map<String, String> params = new HashMap<String, String>();
		params.put("merch_id", merch_id);
		params.put("merch_pwd", merch_pwd);
		params.put("trx_id", trx_id);
		try {
			String result = sendPost(walletURL + checkAction, params);
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			Node node = document.selectSingleNode( "/resp");
			return node.selectSingleNode("error_code").getStringValue();
		} catch (IOException e) {
			log.error("GPI转账: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("GPI转账: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("GPI转账: Exception");
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 投注记录
	 * 时间格式dd-MM-yyyy,HHmmss
	 **/
	public static void qryBetRecords(String startTime, String endTime, int pageNo, int pageSize){
		log.info("gpi查询投注记录：");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("startDate", startTime);
		params.put("endDate", endTime);
		params.put("pageNum", pageNo+"");
		params.put("pageSize", pageSize+"");
		params.put("operator", merch_id);
		params.put("pwd", "c6ORIn");
		try {
			String result = sendPost(historyPulling, params);
			log.info(result);
			Document document = DocumentHelper.parseText(result);
			/*Node node = document.selectSingleNode( "/resp");
			if(node.selectSingleNode("error_code").getStringValue().equals("0")){
				
			}else{
				log.info("GPI查询投注记录：" + node.selectSingleNode("error_msg").getStringValue());
			}*/
		} catch (IOException e) {
			log.error("GPI查询投注记录: IOException");
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error("GPI查询投注记录: DocumentException");
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error("GPI查询投注记录: Exception");
			log.error(e.getMessage());
		}
	}
	
	
	/**
	 * 登入游戏
	 **/
	public static String login(String token, String gameid, int isfun){
		if(rSlotMap.containsKey(gameid)){
			return loginRSlot(token, gameid, isfun);
		}
		StringBuilder strBuilder = new StringBuilder(slotGameUrl);
		strBuilder.append("?");
		strBuilder.append("token=").append(token);
		strBuilder.append("&gameid=").append(gameid);
		strBuilder.append("&fun=").append(isfun);    //0 (real play) or 1 (fun play)
		strBuilder.append("&op=").append(merch_id);
		strBuilder.append("&lang=").append("zh-cn");
		strBuilder.append("&info=").append("credit");   //rate (show chip price) or credit (show balance)
		strBuilder.append("&switch=").append(1);        //是否可以切換到試玩模式 0 = allow switching 1 = disable switching
		strBuilder.append("&hidelobby=").append(1);     //是否隱藏遊戲大廳 0= show 1= hide
		
		log.info("GPI游戏登入：" + strBuilder.toString());
		return strBuilder.toString();
	}
	
	/**
	 * GPI mobile游戏登入
	 * @param token
	 * @param gameid
	 * @param isfun
	 * @return
	 */
	public static String loginMobile(String token, String gameid, int isfun, String domain){
		StringBuilder strBuilder = new StringBuilder(gpiMobileUrl);
		strBuilder.append(gameid).append("/?");
		strBuilder.append("fun=").append(isfun);    //0 (real play) or 1 (fun play)
		strBuilder.append("&token=").append(token);
		strBuilder.append("&op=").append(merch_id);
		strBuilder.append("&lang=").append("zh-cn");
		strBuilder.append("&info=").append("credit");   //rate (show chip price) or credit (show balance)
		strBuilder.append("&switch=").append(1);        //是否可以切換到試玩模式 0 = allow switching 1 = disable switching
		strBuilder.append("&hidelobby=").append(1);     //是否隱藏遊戲大廳 0= show 1= hide
		strBuilder.append("&homeURL=").append(domain + "/mobile/");
		strBuilder.append("&lobbyURL=").append(domain + "/mobile/gpi.jsp");   //大厅
		strBuilder.append("&fundsURL=").append(domain + "/mobile/fundsManage.jsp");   //账户

		log.info("GPI Mobile游戏登入：" + strBuilder.toString());
		return strBuilder.toString();
	}
	
	/**
	 * 登入游戏
	 **/
	public static String loginRSlot(String token, String gameid, int isfun){
		StringBuilder strBuilder = new StringBuilder(rslotsUrl);
		strBuilder.append("/").append(gameid);
		strBuilder.append("?");
		strBuilder.append("token=").append(token);
		strBuilder.append("&fun=").append(isfun);    //0 (real play) or 1 (fun play)
		strBuilder.append("&op=").append(merch_id);
		strBuilder.append("&lang=").append("zh-cn");
		
		log.info("GPI RSlots登入" + strBuilder);
		return strBuilder.toString();
	}
	
	/** 
	 * PNG游戏登入
	 */
	public static String pngLogin(String token, String gameid, int isfun){
		//?op=[merch_id]&token=[operator token]&mode=real&lang=[en_GB | zh_CN]&gameId=[refer to list]
		String mode = isfun==1?"fun":"real";
		StringBuilder strBuilder = new StringBuilder(pngGameURl);
		strBuilder.append("?");
		strBuilder.append("op=").append(merch_id);
		strBuilder.append("&token=").append(token);
		strBuilder.append("&mode=").append(mode);
		strBuilder.append("&lang=").append("zh_CN");
		strBuilder.append("&gameId=").append(gameid);
		//strBuilder.append("&switch=").append(1);        //是否可以切換到試玩模式 0 = allow switching 1 = disable switching
		//strBuilder.append("&hidelobby=").append(1);     //是否隱藏遊戲大廳 0= show 1= hide
		
		log.info("PNG游戏登入：" + strBuilder.toString());
		return strBuilder.toString();
	}
	
	/** 
	 * PNG Mobile 游戏登入
	 */
	public static String pngMobileLogin(String token, String gameid, int isfun){
		//?op=[merch_id]&token=[operator token]&mode=real&lang=[en_GB | zh_CN]&gameId=[refer to list]
		String mode = isfun==1?"fun":"real";
		StringBuilder strBuilder = new StringBuilder(pngMobileUrl);
		strBuilder.append("?");
		strBuilder.append("op=").append(merch_id);
		strBuilder.append("&token=").append(token);
		strBuilder.append("&mode=").append(mode);
		strBuilder.append("&lang=").append("zh_CN");
		strBuilder.append("&gameId=").append(gameid);
		//strBuilder.append("&switch=").append(1);        //是否可以切換到試玩模式 0 = allow switching 1 = disable switching
		//strBuilder.append("&hidelobby=").append(1);     //是否隱藏遊戲大廳 0= show 1= hide
		
		log.info("PNG游戏登入：" + strBuilder.toString());
		return strBuilder.toString();
	}
	
	/**
	 * BS游戏登入
	 */
	public static String bsLogin(String token, String gameid, int isfun){
		//?op=[merch_id]&token=&mode=[real |  fun]&lang=[en | zh]&gameId= [refer list]"
		String mode = isfun==1?"fun":"real";
		StringBuilder strBuilder = new StringBuilder(bsGameUrl);
		strBuilder.append("?");
		strBuilder.append("op=").append(merch_id);
		strBuilder.append("&token=").append(token);
		strBuilder.append("&mode=").append(mode);
		strBuilder.append("&lang=").append("zh");
		strBuilder.append("&gameId=").append(gameid);
		
		log.info("BetSoft游戏登入：" + strBuilder.toString());
		return strBuilder.toString();
	}
	
	/**
	 * CTXM游戏登入
	 */
	public static String ctxmLogin(String token, String gameid, int isfun){
		//?op=[merch_id]&game_code=[refer list]&language=[en | zh]&ticket=[operator token]&playmode=real
		String mode = isfun==1?"fun":"real";
		StringBuilder strBuilder = new StringBuilder(ctxmGameUrl);
		strBuilder.append("?");
		strBuilder.append("op=").append(merch_id);
		strBuilder.append("&game_code=").append(gameid);
		strBuilder.append("&language=").append("zh");
		strBuilder.append("&ticket=").append(token);
		strBuilder.append("&playmode=").append(mode);
		
		log.info("CTXM游戏登入：" + strBuilder.toString());
		return strBuilder.toString();
	}
	
	public static String sendPost(String url, Map<String, String> params) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            StringBuilder psb = new StringBuilder();
            Iterator<String> iterator = params.keySet().iterator(); 
            while (iterator.hasNext()) {
            	String k = (String) iterator.next();
            	if(!psb.toString().equals("")){
            		psb.append("&");
            	}
            	psb.append(k).append("=").append(URLEncoder.encode(params.get(k), "UTF-8"));
    		}
            out.print(psb.toString());
            //flush输出流的缓冲
            out.flush();
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } finally{
        	//关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  
	
	public static void main(String[] args) {
		//Users user = new Users();
		//user.setLoginname("woodytest");
		createUser("lyxian718");
		//debit(user, 100.00, "1123456789");
		//credit("benny", 1.00, "bennytest123");
		//checkTransaction("deantest121228888888");
		//qryBetRecords("01-07-2015,000000", "20-07-2015,000000", 1, 100);
		//getBalance("woodytest");
		//credit("woodytest", 100.0, "deantest121228888888", "false");
		//getBalance("woodytest");
	}
}
