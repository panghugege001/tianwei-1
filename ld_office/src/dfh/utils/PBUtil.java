package dfh.utils;

import com.alibaba.fastjson.JSONArray;
import dfh.model.PBData;
import dfh.model.bean.Bean4Xima;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.ProposalService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PBUtil {
    private static Logger logger = Logger.getLogger(PBUtil.class);
    private static final String ALGORITHM = "AES";
    private static final String INIT_VECTOR = "RandomInitVector";
    public static final String ENCODE_TYPE = "UTF-8";
    public static final String PB_URL = "http://api.ps3838.com/b2b/";
    public static final String PB_LOGIN_URL = "player/loginV2";
    public static final String PB_BALANCE_URL = "player/info";
    public static final String PB_RECHARGE_URL = "player/deposit";
    public static final String PB_WITHDRAWAL_URL = "player/withdraw";
    public static final String PB_ALL_WAGERS_URL = "report/all-wagers";
    public static final String LANGUAGE = "zh-cn";
    public static final String AGENT_CODE = "B053701";
    public static final String AGENT_KEY = "a2e6021f-21af-47d2-adb9-10cacb4db59d";
    public static final String SECRET_KEY = "yOjRlo8x2Ri1xsEz";
    public static final String SUFFIX = ".pingbo";
    public static final String SETTLE_DATE = "settle_date";
    public static final String SETTLE = "1";
    public static final String SETTLED = "SETTLED";
    public static final String DRAW = "DRAW";
    public static final String PRODUCT_CD = "ld";
    private ProposalService proposalService;

    private static String generateToken(String agentCode, String agentKey, String secretKey) throws Exception {
        String sTimestamp = String.valueOf(System.currentTimeMillis());
        logger.info("时间戳:"+sTimestamp);
        String hashToken = EncryptionUtil.encryptPassword(agentCode + sTimestamp + agentKey);
        String tokenPayLoad = String.format("%s|%s|%s", agentCode, sTimestamp, hashToken);
        String token = encryptAES(secretKey, tokenPayLoad);
        return token;
    }
    private static String encryptAES(String secretKey, String tokenPayLoad) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(tokenPayLoad.getBytes());
            String encrypt = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encrypted));
            return encrypt;
        } catch (Exception ex) {
            logger.info("平博体育加密失败");
            logger.error(ex);
        }
        return null;
    }

    /**
     * 用户登录平博体育接口，如果用户不存在则创建用户，成功后返回URL等信息
     * @param loginName
     * @return
     * */
    public static String PBUserLogin(String loginName){
        try{
            String token = generateToken(AGENT_CODE,AGENT_KEY,SECRET_KEY);
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("loginId", loginName+SUFFIX);
            baseParams.put("locale", LANGUAGE);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("userCode",AGENT_CODE);
            headerParams.put("token",token);
            String url = PB_URL+PB_LOGIN_URL;
            String response = HttpUtils.httpPostRequest(url,baseParams,headerParams);
            JSONObject json = JSONObject.fromObject(response);
            if(null !=json && json.has("loginUrl")){
                String loginUrl = json.getString("loginUrl");
                logger.info("返回loginUrl:"+loginUrl);
                return loginUrl;
            }else if(null !=json && json.has("code")){
                String code = json.getString("code");
                String message = json.getString("message");
                logger.info("PBUserLogin is Fail");
                logger.info(code);
                logger.info(message);
            }
        } catch (Exception ex) {
            logger.info("平博体育用户登录失败");
            logger.error(ex);
        }
        return null;
    }

    /**
     * 根据loginName获取平博userCode
     * @param loginName,token
     * @return
     * */
    public static String getuserCode(String loginName,String token){
        try{
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("loginId", PRODUCT_CD+loginName+SUFFIX);
            baseParams.put("locale", LANGUAGE);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("userCode",AGENT_CODE);
            headerParams.put("token",token);
            String url = PB_URL+PB_LOGIN_URL;
            String response = HttpUtils.httpPostRequest(url,baseParams,headerParams);
            JSONObject json = JSONObject.fromObject(response);
            if(null !=json && json.has("userCode")){
                String userCode = json.getString("userCode");
                logger.info("返回userCode:"+userCode);
                return userCode;
            }else if(null !=json && json.has("code")){
                String code = json.getString("code");
                String message = json.getString("message");
                logger.info("getuserCode is Fail");
                logger.info(code);
                logger.info(message);
            }
        } catch (Exception ex) {
            logger.info("获取平博userCode失败");
            logger.error(ex);
        }
        return null;
    }
    /**
     * 获取用户平博余额
     * @param loginName
     * @return
     * */
    public static Double getBalance(String loginName){
        try{
            String token = generateToken(AGENT_CODE,AGENT_KEY,SECRET_KEY);
            Map<String, String> baseParams = new HashMap<String, String>();
            String userCode = getuserCode(loginName,token);
            if(StringUtils.isEmpty(userCode)){
                logger.info("根据loginName获取平博userCode失败");
                logger.info("loginName:"+loginName);
                return null;
            }
            baseParams.put("userCode", userCode);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("userCode",AGENT_CODE);
            headerParams.put("token",token);
            String url = PB_URL+PB_BALANCE_URL;
            String response = HttpUtils.httpGetRequest(url,baseParams,headerParams);
            JSONObject json = JSONObject.fromObject(response);
            if(null !=json && json.has("availableBalance")){
                Double availableBalance = json.getDouble("availableBalance");
                logger.info("返回availableBalance:"+availableBalance);
                return availableBalance;
            }else if(null !=json && json.has("code")){
                String code = json.getString("code");
                String message = json.getString("message");
                logger.info("getBalance is Fail");
                logger.info(code);
                logger.info(message);
            }
        } catch (Exception ex) {
            logger.info("获取用户平博余额失败");
            logger.error(ex);
        }
        return null;
    }

    /**
     * 转入平博账户
     * @param loginName
     * @param amount
     * @return
     * */
    public static Boolean transferToPB(String product,String loginName, Double amount){
        try{
            String token = generateToken(AGENT_CODE,AGENT_KEY,SECRET_KEY);
            Map<String, Object> baseParams = new HashMap<String, Object>();
            String userCode = getuserCode(loginName,token);
            if(StringUtils.isEmpty(userCode)){
                logger.info("根据loginName获取平博userCode失败");
                logger.info("loginName:"+loginName);
                return null;
            }
            baseParams.put("userCode", userCode);
            baseParams.put("amount", amount);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("userCode",AGENT_CODE);
            headerParams.put("token",token);
            String url = PB_URL+PB_RECHARGE_URL;
            String response = HttpUtils.httpPostRequest(url,baseParams,headerParams);
            JSONObject json = JSONObject.fromObject(response);
            if(null !=json && json.has("availableBalance")){
                Double availableBalance = json.getDouble("availableBalance");
                logger.info("平博充值"+amount+"成功，余额availableBalance:"+availableBalance);
                return true;
            }else if(null !=json && json.has("code")){
                String code = json.getString("code");
                String message = json.getString("message");
                logger.info("transferToChess is Fail");
                logger.info(code);
                logger.info(message);
            }
        } catch (Exception ex) {
            logger.info("转入平博账户失败");
            logger.error(ex);
        }
        return false;
    }

    /**
     * 转出平博账户
     * @param loginName
     * @param amount
     * @return
     * */
    public static Boolean transferFromPB(String product,String loginName, Double amount){
        try{
            String token = generateToken(AGENT_CODE,AGENT_KEY,SECRET_KEY);
            Map<String, Object> baseParams = new HashMap<String, Object>();
            String userCode = getuserCode(loginName,token);
            if(StringUtils.isEmpty(userCode)){
                logger.info("根据loginName获取平博userCode失败");
                logger.info("loginName:"+loginName);
                return null;
            }
            baseParams.put("userCode", userCode);
            baseParams.put("amount", amount);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("userCode",AGENT_CODE);
            headerParams.put("token",token);
            String url = PB_URL+PB_WITHDRAWAL_URL;
            String response = HttpUtils.httpPostRequest(url,baseParams,headerParams);
            JSONObject json = JSONObject.fromObject(response);
            if(null !=json && json.has("availableBalance")){
                Double availableBalance = json.getDouble("availableBalance");
                logger.info("平博转出"+amount+"成功，余额availableBalance:"+availableBalance);
                return true;
            }else if(null !=json && json.has("code")){
                String code = json.getString("code");
                String message = json.getString("message");
                logger.info("transferFromPB is Fail");
                logger.info(code);
                logger.info(message);
            }
        } catch (Exception ex) {
            logger.info("转出平博账户失败");
            logger.error(ex);
        }
        return false;
    }

    /**
     * 获取所有游戏记录
     * @param stime
     * @param etime
     * @return
     * */
    public List<Bean4Xima> getAllWagers(String stime, String etime){
        logger.info("获取所有游戏记录开始");
        logger.info("getAllWagers Start");
        try{
            String token = generateToken(AGENT_CODE,AGENT_KEY,SECRET_KEY);
            Map<String, String> baseParams = new HashMap<String, String>();

            baseParams.put("filterBy", SETTLE_DATE);
            baseParams.put("settle", SETTLE);
            baseParams.put("dateFrom", stime);
            baseParams.put("dateTo", etime);
            Map<String, String> headerParams = new HashMap<String, String>();
            headerParams.put("userCode",AGENT_CODE);
            headerParams.put("token",token);
            String url = PB_URL+PB_ALL_WAGERS_URL;
            String response = HttpUtils.httpGetRequest(url,baseParams,headerParams);
            if(StringUtils.isEmpty(response) || "[]".equals(response)){
                return null;
            }
            JSONArray jsonlist = JSONArray.parseArray(response);
            if(null == jsonlist){
                return null;
            }
            List<Bean4Xima> list = new ArrayList<Bean4Xima>();
            List<PBData> resultList = new ArrayList<PBData>();
            for(int i=0;i<jsonlist.size();i++) {
                Object jsonObject = jsonlist.get(i);
                JSONObject json = JSONObject.fromObject(jsonObject);
                if (StringUtils.isNotEmpty(json.getString("loginId"))){
                    logger.info("getAllWagers Start loginId:" + json.get("loginId"));
                }
                if (StringUtils.isNotEmpty(json.getString("status"))){
                    logger.info("getAllWagers Start status:" + json.get("status"));
                }
//                Map<String,Object> HashMap = new HashMap<>();
//                HashMap.put("stake",json.get("stake"));  //下注金额
//                HashMap.put("turnover",json.get("turnover")); //有效投注
//                HashMap.put("winLoss",json.get("winLoss")); //净输赢
//                HashMap.put("loginName",json.get("loginId").toString().substring(0,json.get("loginId").toString().indexOf("."))); //用户名
//                HashMap.put("wagerDateFm",json.get("wagerDateFm"));  //用户投注时间
                //只有status等于settled才是表示真正已经结算的投注
                if (StringUtils.isNotEmpty(json.getString("status")) && SETTLED.equals(json.getString("status"))
                        && !DRAW.equals(json.getString("result"))){
                    //判断是否有二次派彩情况 result 为true则存在，直接跳出
                    boolean result = checkPBData(json.getString("wagerId"));
                    if(result){
                        continue;
                    }
                    Bean4Xima ximaData = new Bean4Xima();
                    ximaData.setUserName(json.getString("loginId").substring(0, json.getString("loginId").indexOf(".")));  //用户名
                    ximaData.setBetAmount(Double.valueOf(json.getString("turnover"))); //下注金额
                    ximaData.setProfit(Double.valueOf(json.getString("winLoss")));  //净输赢
                    list.add(ximaData);
                    //保存用户投注记录
                    PBData pbdata = new PBData();
                    pbdata.setPlayerId(json.getString("loginId").substring(0, json.getString("loginId").indexOf(".")));
                    pbdata.setBetId(json.getString("wagerId"));
                    pbdata.setCreateTime(DateUtil.parseDateForStandard(json.getString("wagerDateFm")));
                    pbdata.setStakeAmount(json.getString("turnover"));
                    pbdata.setWinloss(json.getString("winLoss"));
                    pbdata.setIsSettle(json.getString("result"));
                    pbdata.setSettleTime(DateUtil.parseDateForStandard(json.getString("settleDateFm")));
                    resultList.add(pbdata);
                }
            }
            //统计一个用户一天所有的投注记录
            for  (int i=0 ;i<list.size()- 1 ; i ++ )  {
                for  (int j=list.size()-1;j>i;j--)  {
                    if  (list.get(j).getUserName().equals(list.get(i).getUserName()))  {
                        list.get(i).setBetAmount(list.get(i).getBetAmount()+list.get(j).getBetAmount());
                        list.get(i).setProfit(list.get(i).getProfit()+ list.get(j).getProfit());
                        list.remove(j);
                    }
                }
            }
            //落库用户投注记录
            proposalService.insertPBData(resultList);
            logger.info("获取所有游戏记录结束");
            logger.info("getAllWagers End");
            return list;
        } catch (Exception ex) {
            logger.info("获取所有游戏记录异常");
            logger.error(ex);
        }
        return null;
    }

    public boolean checkPBData(String betId){
        if(StringUtils.isEmpty(betId)){
            return true;
        }
        List<PBData> list = proposalService.selectPBData();
        if(null != list && list.size() > 0){
            for(PBData pbData: list){
                if(betId.equals(pbData.getBetId())){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
//        String sdate = DateUtil.getchangedDate(-1)+" 00:00:00";
//        String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
//        System.out.println(getAllWagers(sdate,edate));
        System.out.println(PBUserLogin("ljc002"));
//        Double toAmout = new Double(1000.0);
//        Double FromAmout = new Double(10.0);
//        System.out.println(transferToPB("123","ljc002",toAmout));
//        System.out.println(getBalance("ljc002"));
//        System.out.println(transferFromPB("123","ljc002",FromAmout));
//        System.out.println(getBalance("ljc002"));
    }

    public ProposalService getProposalService() {
        return proposalService;
    }

    public void setProposalService(ProposalService proposalService) {
        this.proposalService = proposalService;
    }
}
