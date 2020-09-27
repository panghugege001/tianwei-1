package dfh.utils.bitGame;

import com.alibaba.fastjson.JSONArray;
import dfh.model.bean.Bean4Xima;
import dfh.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class BitGameUtil {
    private static Logger logger = Logger.getLogger(BitGameUtil.class);
    public static final String BIT_API_URL = "http://api.dou.ph:8083/bomb/DoBusiness";
    public static final String BIT_URL = "http://longdu33.bitga.me/?";
    public static final String BIT_END_URL = "/#/core/token/";
    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLW8MeK89Ld4TGKtN7gbShPQlI7/q4TGf6M+jR/hzGyjg3ycRdpXg4WNb6kZOMNA19pIudJlItbV8wCQZK7wmuY4badUPVcguMOH2BBYCBZHJmgEhBUwvFC8vI/vima3v5Iie6/SGepAR3HK3024ENpfZw3+WKVFwdQlGmLgLZ0wIDAQAB"  ;
    public static final String WID_NAME = "longdu33";
    public static final String PASSWORD = "123456789";
    public static final String CHECK_USER_NAME = "checkAccountIsExist";
    public static final String CREATE_USER = "checkAndCreateAccount";
    public static final String LOGIN_USER = "login";
    public static final String CHECK_BALANCE = "getBalance";
    public static final String GET_BETTING = "getBettingRecord";
    public static final String TRANSFER_ACCOUNT = "prepareTransferCredit";
    public static final String CONFIRM_ACCOUNT = "confirmTransferCredit";
    public static final String PRODUCT_CD = "k";
    /**
     * 查询转账结果接口
     * @param loginName
     * @return
     * */
    public static Boolean bitconfirmTransfer(String loginName, Double amount,String transferType,String orderNo){
        logger.info("查询转账结果开始");
        try {
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", CONFIRM_ACCOUNT);
            baseParams.put("wid",WID_NAME);
            JSONObject json = new JSONObject();
            json.put("username", PRODUCT_CD+loginName);
            json.put("password", PASSWORD);
            json.put("billno", orderNo);
            json.put("credit", amount);
            json.put("type", transferType);
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("查询转账结果code:"+jsonObject.getString("code"));
                if("1".equals(jsonObject.getString("code"))){
                    logger.info("查询转账结果成功");
                    return true;
                }
            }
        } catch (Exception e) {
            logger.info("查询转账结果异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("查询转账结果结束");
        return false;
    }
    /**
     * 转账接口
     * @param loginName
     * @return
     * */
    public static Boolean bitTransferAccounts(String loginName, Double amount,String transferType,String orderNo){
        logger.info("比特游戏转账开始");
        try {
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", TRANSFER_ACCOUNT);
            baseParams.put("wid",WID_NAME);
            JSONObject json = new JSONObject();
            json.put("username", PRODUCT_CD+loginName);
            json.put("password", PASSWORD);
            json.put("billno", orderNo);
            json.put("credit", amount);
            json.put("type", transferType);
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("比特游戏转账code:"+jsonObject.getString("code"));
                if("1".equals(jsonObject.getString("code"))){
                    logger.info("比特游戏转账成功");
                    return true;
                }
            }
        } catch (Exception e) {
            logger.info("比特游戏转账异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("比特游戏转账结束");
        return false;
    }

    /**
     * 获取用户投注记录
     * @param startTime
     * @param endTime
     * @return
     * */
    public static List<Bean4Xima> bitGetBetting(String startTime, String endTime){
        logger.info("获取用户投注记录开始");
        logger.info("bitGetBetting start");
        try {
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", GET_BETTING);
            baseParams.put("wid",WID_NAME);
            JSONObject json = new JSONObject();
            json.put("startTime", startTime);
            json.put("endTime", endTime);
            json.put("queryflag", "true");
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("获取用户投注记录返回code:"+jsonObject.getString("code"));
                if("1".equals(jsonObject.getString("code"))){
                    JSONObject data =jsonObject.getJSONObject("data");
                    if(null == data){
                        return null;
                    }
                    String pageContent =data.getString("pageContent");
                    if(StringUtils.isEmpty(pageContent) || "[]".equals(pageContent)){
                        return null;
                    }
                    JSONArray jsonlist = JSONArray.parseArray(pageContent);
                    if(null == jsonlist){
                        return null;
                    }
                    List<Bean4Xima> list = new ArrayList<Bean4Xima>();
                    for(int i=0;i<jsonlist.size();i++) {
                        Object obejct = jsonlist.get(i);
                        JSONObject jsonobj = JSONObject.fromObject(obejct);
                        logger.info("bitGetBetting username:"+ jsonobj.get("username"));
                        logger.info("bitGetBetting amount:"+ jsonobj.get("amount"));
                        logger.info("bitGetBetting bonusAmount:"+ jsonobj.get("bonusAmount"));
                        if (StringUtils.isNotEmpty(jsonobj.getString("status")) &&("1".equals(jsonobj.getString("status")) || "2".equals(jsonobj.getString("status")))){
                            Bean4Xima ximaData = new Bean4Xima();
                            ximaData.setUserName(jsonobj.getString("username"));  //用户名
                            ximaData.setBetAmount(Double.valueOf(jsonobj.getString("amount"))); //下注金额
                            ximaData.setProfit(Double.valueOf(jsonobj.getString("bonusAmount"))-Double.valueOf(jsonobj.getString("amount")));  //净输赢
                            list.add(ximaData);
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
                    logger.info("获取用户投注记录成功");
                    logger.info("bitGetBetting End");
                    return list;
                }
            }
        } catch (Exception e) {
            logger.info("获取用户投注记录异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("获取用户投注记录结束");
        return null;
    }
    /**
     * 查询用户比特账户余额接口
     * @param loginName
     * @return
     * */
    public static Double bitUserBalance(String loginName){
        logger.info("查询用户比特账户余额开始");
        logger.info("loginName:"+loginName);
        try {
            //查询之前先查询用户信息，确保用户是存在的
            if(StringUtils.isEmpty(bitUserLogin(loginName))){
                logger.info("查询用户比特账户余额检查用户信息失败:"+loginName);
                return null;
            }
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", CHECK_BALANCE);
            baseParams.put("wid",WID_NAME);
            JSONObject json = new JSONObject();
            json.put("username", PRODUCT_CD+loginName);
            json.put("password", PASSWORD);
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("查询用户比特账户余额返回code:"+jsonObject.getString("code"));
                if("1".equals(jsonObject.getString("code")) && jsonObject.has("amount")){
                    logger.info("查询用户比特账户余额成功");
                    logger.info("amount:"+jsonObject.getString("amount"));
                    return jsonObject.getDouble("amount");
                }
            }
        } catch (Exception e) {
            logger.info("查询用户比特账户余额异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("查询用户比特账户余额结束");
        return null;
    }

    /**
     * 用户登录比特游戏接口
     * @param loginName
     * @return
     * */
    public static String bitUserLogin(String loginName){
        logger.info("用户登陆比特游戏开始");
        logger.info("loginName:"+loginName);
        try {
            //1.检查用户是否存在，存在直接登陆，不存在先创建在登陆
            String bitCheckCode =  bitCheckUserName(loginName);
            logger.info("检查用户是否存在,返回CODE:"+bitCheckCode);
            if("1".equals(bitCheckCode)){
                String createUserCode = bitCreateUser(loginName);
                logger.info("创建用户,返回CODE:"+createUserCode);
                if(!"1".equals(createUserCode)){
                    return null;
                }
            }else if(!"0305".equals(bitCheckCode)){
                return null;
            }
            //2.用户登陆
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", LOGIN_USER);
            baseParams.put("wid",WID_NAME);
            JSONObject json = new JSONObject();
            json.put("username", PRODUCT_CD+loginName);
            json.put("password", PASSWORD);
            json.put("timestamp", new Date().getTime());
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("用户登陆比特游戏返回code:"+jsonObject.getString("code"));
                if("1".equals(jsonObject.getString("code")) && jsonObject.has("token")){
                    logger.info("用户登陆比特游戏成功");
                    logger.info("bitUserLoginURL:"+BIT_URL+jsonObject.getString("token")+BIT_END_URL);
                    return BIT_URL+jsonObject.getString("token")+BIT_END_URL;
                }
            }
        } catch (Exception e) {
            logger.info("用户登陆比特游戏异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("用户登陆比特游戏结束");
        return null;
    }

    /**
     * 用户注册比特游戏接口
     * @param loginName
     * @return
     * */
    public static String bitCreateUser(String loginName){
        logger.info("比特游戏注册用户开始");
        try {
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", CREATE_USER);
            baseParams.put("wid",WID_NAME);
            JSONObject json = new JSONObject();
            json.put("username", PRODUCT_CD+loginName);
            json.put("password", PASSWORD);
            json.put("agent","");
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("比特游戏注册用户返回code:"+jsonObject.getString("code"));
                return jsonObject.getString("code");
            }
        } catch (Exception e) {
            logger.info("比特游戏注册用户异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("比特游戏注册用户结束");
        return null;
    }

    /**
     * 比特游戏检查用户名是否存在
     * @param loginName
     * @return
     * */
    public static String bitCheckUserName(String loginName){
        logger.info("比特游戏检查用户名是否存在开始");
        try {
            Map<String, Object> baseParams = new HashMap<String, Object>();
            baseParams.put("do", CHECK_USER_NAME);
            baseParams.put("wid",WID_NAME);
            JSONObject json=new JSONObject();
            json.put("username", PRODUCT_CD+loginName);
            baseParams.put("data",encodeRequest(json,DEFAULT_PUBLIC_KEY));
            Map<String, String> headerParams = new HashMap<String, String>();
            String response = HttpUtils.httpPostRequest(BIT_API_URL,baseParams,headerParams);
            JSONObject jsonObject = JSONObject.fromObject(response);
            if(null !=jsonObject && jsonObject.has("code")){
                logger.info("比特游戏检查用户名返回code:"+jsonObject.getString("code"));
                return jsonObject.getString("code");
            }
        } catch (Exception e) {
            logger.info("比特游戏检查用户名是否存在异常");
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("比特游戏检查用户名是否存在结束");
        return null;
    }

    public static String encodeRequest(JSONObject json, String PUBLIC_KEY) throws Exception{
        if(PUBLIC_KEY==null||PUBLIC_KEY.equals("")){
            System.err.println("Warning:The PUBLIC_KEY is null!");
            return null;
        }
        byte[] data = json.toString().getBytes("UTF-8");
        byte[] encodedData = RSAUtils.encryptByPublicKey(data, PUBLIC_KEY);
		/*System.out.println("加密后文字：\r\n" + new String(encodedData));
		System.out.println(TAG+"网络传输的字符串:"+Base64Utils.encode(encodedData));*/
        return Base64Utils.encode(encodedData);
    }
    /**
     * 生成交易单号
     * @return
     * */
    public static String generateTransferId() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 5);

        SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");
        String str = yyMMddHHmmssSSS.format(calendar.getTime());

        return str + RandomStringUtils.randomNumeric(4);
    }


    public static void main(String[] args) {
//        bitCheckUserName("ljctest001");
//        bitCreateUser("ljctest006");
//        bitUserBalance("ljctest002");
        bitUserLogin("qycs3");
//        String sdate = DateUtil.getchangedDate(-1)+" 00:00:00";
//        String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
//        bitGetBetting(sdate,edate);
//        bitTransferAccounts("ljctest104",new Double("100"),"IN","987654327");
//        bitconfirmTransfer("ljctest104",new Double("50"),"OUT","987654328");
    }
}