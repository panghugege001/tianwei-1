package dfh.action.points;

import app.util.AESUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dfh.action.SubActionSupport;
import dfh.model.*;
import dfh.model.common.Response;
import dfh.model.common.ResponseEnum;
import dfh.utils.*;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qcon on 2017/9/7.
 */
public class PointsAction extends SubActionSupport {
    public static final String SERVER_IP = Configuration.getInstance().getValue("redrain.middle.url")+"/point/";


    private Logger log = Logger.getLogger(PointsAction.class);
    private Response response = new Response();
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final JsonParser jsonParser = new JsonParser();

    public static final Map<Integer, Double> levelConfig;
    public static final Map<Integer, String> levelInfoConfig;
    private String middleServiceUrl = AxisUtil.PAY_CENTER_URL;

    static {
        levelConfig = new HashMap<Integer, Double>();
        levelInfoConfig = new HashMap<Integer, String>();
        levelConfig.put(0, 1.0);
        levelInfoConfig.put(0, "【您是新会员，享受折扣：无】");
        levelConfig.put(1, 0.9);
        levelInfoConfig.put(1, "【您是忠实VIP，享受折扣：10%】");
        levelConfig.put(2, 0.8);
        levelInfoConfig.put(2, "【您是青龙VIP，享受折扣：20%】");
        levelConfig.put(3, 0.7);
        levelInfoConfig.put(3, "【您是银龙VIP，享受折扣：30%】");
        levelConfig.put(4, 0.6);
        levelInfoConfig.put(4, "【您是金龙VIP，享受折扣：40%】");
        levelConfig.put(5, 0.5);
        levelInfoConfig.put(5, "【您是御龙VIP，享受折扣：50%】");
        levelInfoConfig.put(-1, "请登录");
    }

    public void pointsLevelInfo (){
        Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
        response.setResponseEnum(ResponseEnum.SUCCESS);
        if (user != null){
            response.setData(levelInfoConfig.get(user.getLevel()));
        } else {
            response.setData(levelInfoConfig.get(-1));
        }
        GsonUtil.GsonObject(response);
    }

    public void getLevelSave (){
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                map.put("flag",flag.toString());
                String res = HttpUtils.post(SERVER_IP + "getLevelSave", map);
                List<SystemConfig> list=gson.fromJson(res, new TypeToken<List<SystemConfig>>() {}.getType());
                response.setData(list);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }

    public SystemConfig getSave (String loginname){
        HashMap<String, String> map = new HashMap<String, String>();
        try {
                map.put("loginname",loginname);
                map.put("flag","1");
                String res = HttpUtils.post(SERVER_IP + "getLevelSave", map);
                List<SystemConfig> list=gson.fromJson(res, new TypeToken<List<SystemConfig>>() {}.getType());
                if(list.size()>0){
                    return list.get(0);
                }
                return null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
               return null;
            }

    }


private Integer flag=0;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    private Integer state=0;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    private int id=-1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void handleAddress (){
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                UserAddress userAddress = new UserAddress();
                if(state!=5) {
                    String area = getRequest().getParameter("area");
                    String address = getRequest().getParameter("address");
                    String name = getRequest().getParameter("name");
                    String phone = getRequest().getParameter("phone");
                    String province = getRequest().getParameter("province");
                    String city = getRequest().getParameter("city");
                    String district = getRequest().getParameter("district");
                    userAddress.setAddress(address);
                    userAddress.setArea(area);
                    userAddress.setName(name);
                    userAddress.setPhone(phone);
                    userAddress.setPhone(phone);
                    userAddress.setProvince(province);
                    userAddress.setCity(city);
                    userAddress.setDistrict(district);

                }
                userAddress.setFlag(flag);
                userAddress.setLoginName(customer.getLoginname());
                if(id!=-1){
                    userAddress.setId(id);
                }
                map.put("userAddress",gson.toJson(userAddress));
                map.put("state",state.toString());
                String res = HttpUtils.post(SERVER_IP + "handleAddress", map);
                if(res.contains("添加")||res.contains("删除")||res.contains("修改")||res.contains("查询")||res.contains("设置")) {
                    response.setData(res);
                }
                else {
                    List<UserAddress> list=gson.fromJson(res, new TypeToken<List<UserAddress>>() {}.getType());
                    response.setData(list);
                }
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }




    /**
     * 查询积分总余额
     */
    public void queryPoints() {
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                String res = HttpUtils.post(SERVER_IP + "queryPoints", map);
                response.setData(res);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }
    /**
     * 扭蛋抽奖   flag 1存款 2流水
     */
    public void eggDraw() {
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                map.put("product","at");
                map.put("flag",flag.toString());
                String res = HttpUtils.post(SERVER_IP + "eggDraw", map);
                PointsPresentRecord record = gson.fromJson(res, PointsPresentRecord.class);
                response.setData(record);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }
    /**
     * 查询通关状态
     */
    public void queryState() {
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                String res = HttpUtils.post(SERVER_IP + "queryState", map);
                List<RemarkDetail> list=gson.fromJson(res, new TypeToken<List<RemarkDetail>>() {}.getType());
                response.setData(list);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }

    /**
     * 领取全部通关奖金
     */
    public void eggDrawBonusAll() {
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                String res = HttpUtils.post(SERVER_IP + "eggDrawBonusAll", map);
                response.setData(res);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }

    /**
     * 领取扭蛋记录
     */
    public void queryEggDrawHistory() {
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                String res = HttpUtils.post(SERVER_IP + "queryEggDrawHistory", map);
                if(StringUtils.isNotEmpty(res)) {
                    List<Activity> list = gson.fromJson(res, new TypeToken<List<Activity>>() {
                    }.getType());

                    response.setData(list);

                }
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }



    /**
     * 扭蛋完成领取彩金   flag 1存款奖金 2流水奖金
     */
    public void eggDrawBonus() {
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                map.put("product","at");
                map.put("flag",flag.toString());
                String res = HttpUtils.post(SERVER_IP + "eggDrawBonus", map);
                response.setData(res);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }





    /** 获取所有的积分奖品列表 */
    public void getAllPointsPresents (){
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
            try {
                Double save=1.0;
                String res = HttpUtils.post(SERVER_IP + "getAllPointsPresents", map);
                List<PointsPresent> list = gson.fromJson(res, new TypeToken<List<PointsPresent>>() {
                }.getType());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(customer!=null) {
                    SystemConfig systemConfig = getSave(customer.getLoginname());
                    if (null != systemConfig) {
                         save = Double.valueOf(systemConfig.getValue()) / 100.0;

                    }
                }
                for (PointsPresent pointsPresent : list) {
                    String[] strings = pointsPresent.getRange().split("~");
                    String s = strings[0] + "分~" + strings[1] + "分";
                    pointsPresent.setRange(s);
                    String strings1 = (String) strings[0];
                    String strings2 = (String) strings[1];
                    BigDecimal bigDecimal1 = new BigDecimal(strings1);
                    BigDecimal bigDecimal2 = new BigDecimal(strings2);
                    BigDecimal bigDecimal3 = new BigDecimal(Double.toString(save));
                    String s1 = bigDecimal1.multiply(bigDecimal3) + "分~" + bigDecimal2.multiply(bigDecimal3) + "分";
                    pointsPresent.setVipSaveRange(s1);
                    pointsPresent.setVipSave(save);
                    pointsPresent.setCreateTime(format.format(new Date(Long.valueOf(pointsPresent.getCreateTime()))));
                    if (StringUtils.isNotEmpty(pointsPresent.getUpdateTime())) {
                        pointsPresent.setUpdateTime(format.format(new Date(Long.valueOf(pointsPresent.getUpdateTime()))));
                    }
                    pointsPresent.setEndTime(format.format(new Date(Long.valueOf(pointsPresent.getEndTime()))));
                    pointsPresent.setStartTime(format.format(new Date(Long.valueOf(pointsPresent.getStartTime()))));
                }

                response.setData(list);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
    }

    /** 获取积分抽奖的奖品列表 */
    public void getAllLuckyDrawPresent (){
        HashMap<String, String> map = new HashMap<String, String>();
            try {
                String res = HttpUtils.post(SERVER_IP + "getAllLuckyDrawPresent", map);
                List<LuckyDrawPresent> list=gson.fromJson(res, new TypeToken<List<LuckyDrawPresent>>() {}.getType());
                response.setData(list);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }

    }

    /** 积分抽奖所需要消耗的积分，这个东西每次都变，需要多次查询 需要登录校验 */
    public void luckyDrawCost (){
        HashMap<String, String> map = new HashMap<String, String>();
        Double save=1.0;
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                String res = HttpUtils.post(SERVER_IP + "luckyDrawCost", map);
                SystemConfig systemConfig = getSave(customer.getLoginname());
                if(systemConfig!=null){
                     save = Double.valueOf(systemConfig.getValue())/100.0;
                }
                RecordRes recordRes = new RecordRes();
                recordRes.setLuckyDrawCost(res);
                recordRes.setVipSave(save);
                response.setData(recordRes);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
            }
        }
    }
private Integer addressId=-1;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    /** 积分兑换奖品,不包括抽奖 需要登录校验 */
    public void pointsExchange (){
        try {
            String requestJson = getRequestPostDataAsJson(getRequest());

            HashMap<String, String> map = new HashMap<String, String>();

            Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
            if (user == null) {
                response.setResponseEnum(ResponseEnum.LOGIN_OVER);
                GsonUtil.GsonObject(response);
                return;
            } else if (user.getRole().equals("AGENT")) {
                response.setResponseEnum(ResponseEnum.AGENT_03);
                GsonUtil.GsonObject(response);
                return;
            } else {
                map.put("loginname",user.getLoginname());
                map.put("pointsPresentRecord",requestJson);
                if(addressId!=-1){
                    map.put("addressId",addressId.toString());
                }
                String res = HttpUtils.post(SERVER_IP + "pointsExchange", map);
                if ("SUCCESS".equals(res)){
                    response.setResponseEnum(ResponseEnum.SUCCESS);
                    response.setData("兑换成功");
                } else {
                    response.setResponseEnum(ResponseEnum.SC_10002);
                    response.setData(res);
                }

            }
        } catch (Exception axisFault) {
            axisFault.printStackTrace();
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
        }
        GsonUtil.GsonObject(response);
    }

    /** 积分抽奖 需要登录校验 */
    public void luckyDraw (){
        String res="";
        HashMap<String, String> map = new HashMap<String, String>();
        Users customer = null;
        try {
            customer = getCustomerFromSession();
        } catch (Exception e) {
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
            GsonUtil.GsonObject(response);
        }
        if (customer == null){
            response.setData("请先登录");
            response.setResponseEnum(ResponseEnum.LOGIN_OVER);
            GsonUtil.GsonObject(response);
        }else {
            try {
                map.put("loginname",customer.getLoginname());
                 res = HttpUtils.post(SERVER_IP + "luckyDraw", map);
                PointsPresentRecord record = gson.fromJson(res, PointsPresentRecord.class);
                response.setData(record);
                response.setResponseEnum(ResponseEnum.SUCCESS);
                GsonUtil.GsonObject(response);
                return;
            }catch (JsonParseException jsonParseException){
                response.setData(res);
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
                return;
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.setData("系统异常");
                response.setResponseEnum(ResponseEnum.SC_10001);
                GsonUtil.GsonObject(response);
                return;
            }
        }
    }

    /** 完善抽奖之后的中奖信息 */
    public void completeLuckyDrawRecord (){
        try {
            String requestJson = getRequestPostDataAsJson(getRequest());

            HashMap<String, String> map = new HashMap<String, String>();
            Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
            if (user == null) {
                response.setResponseEnum(ResponseEnum.LOGIN_OVER);
                GsonUtil.GsonObject(response);
                return;
            } else if (user.getRole().equals("AGENT")) {
                response.setResponseEnum(ResponseEnum.AGENT_03);
                GsonUtil.GsonObject(response);
                return;
            } else {
                map.put("loginname",user.getLoginname());
                map.put("pointsPresentRecord",requestJson);
                if(addressId!=-1){
                    map.put("addressId",addressId.toString());
                }
                String res = HttpUtils.post(SERVER_IP + "completeLuckyDrawRecord", map);
                System.out.println(res);
                if (res.equals("SUCCESS")){
                    response.setResponseEnum(ResponseEnum.SUCCESS);
                } else {
                    response.setResponseEnum(ResponseEnum.SC_10001);
                    response.setData(res);
                }
            }
        } catch (Exception axisFault) {
            axisFault.printStackTrace();
            response.setData("系统出错");
            response.setResponseEnum(ResponseEnum.SC_10001);
        }
        GsonUtil.GsonObject(response);
    }

    /** 积分兑换记录 需要登录校验 */
    public void pointsRecord (){
        try {

            HashMap<String, String> map = new HashMap<String, String>();
            Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
            Integer flag = Integer.parseInt(getRequest().getParameter("flag"));
            if (user == null&&flag!=3) {
                response.setResponseEnum(ResponseEnum.LOGIN_OVER);
                GsonUtil.GsonObject(response);
                return;

            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(user==null) {
                    map.put("loginname", "");
                }else {
                    map.put("loginname",user.getLoginname());
                }
                map.put("flag",flag.toString());
                String res = HttpUtils.post(SERVER_IP + "pointsRecord", map);
                if(StringUtils.isEmpty(res)){
                    response.setResponseEnum(ResponseEnum.SUCCESS);
                    response.setData(res);
                    GsonUtil.GsonObject(response);
                    return;
                }
                List<PointsPresentRecord> list=gson.fromJson(res, new TypeToken<List<PointsPresentRecord>>() {}.getType());
                for (PointsPresentRecord pointsPresentRecord:list){

                    pointsPresentRecord.setCreateTime(format.format(new Date(Long.valueOf(pointsPresentRecord.getCreateTime()))));
                    if(StringUtils.isNotEmpty(pointsPresentRecord.getUpdateTime())) {
                        pointsPresentRecord.setUpdateTime(format.format(new Date(Long.valueOf(pointsPresentRecord.getUpdateTime()))));
                    }

                }
                response.setResponseEnum(ResponseEnum.SUCCESS);
                response.setData(list);
            }
        } catch (Exception axisFault) {
            axisFault.printStackTrace();
            response.setData("系统异常");
            response.setResponseEnum(ResponseEnum.SC_10001);
        }
        GsonUtil.GsonObject(response);
    }

    public String enterPointsHall () throws Exception {
        String token = getRequest().getParameter("token");
        log.info("enterPointsHall方法获取的参数值为：【token=" + token + "】");
        String params;
        if(StringUtils.isEmpty(token)){
            getHttpSession().setAttribute("msg", "身份验证失败");
            return "error" ;
        }
        try {
            token = token.replaceAll("%5R", "\r").replaceAll("%5N", "\n");
            params = AESUtil.getInstance().decrypt(token);
        } catch (Exception e) {
            log.error("enterPointsHall方法解密参数发生异常，异常信息为：" + e.getMessage());
            getHttpSession().setAttribute("msg", "身份验证失败");
            return "error" ;
        }
        JSONObject json = JSONObject.fromObject(params);

        String loginName = String.valueOf(json.get("loginName"));
        String password = String.valueOf(json.get("password"));
        String timestamp = String.valueOf(json.get("timestamp"));

        long timestampSub = (new Date().getTime() - Long.parseLong(timestamp));
        if( timestampSub >= 1000 * 60 * 3){
            getHttpSession().setAttribute("msg", "身份验证失败");
            return "error" ;
        }

        Users user = null;
        try {
            user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { loginName }, Users.class);
        } catch (AxisFault e) {
            log.error("enterPointsHall方法调用webservice方法getUser发生异常，异常信息为：" + e.getMessage());
            throw e;
        }
        getHttpSession().setAttribute(Constants.PT_SESSION_USER, password);
        getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);

        return INPUT;
    }

    /** 解析请求的Json数据 */
    private String getRequestPostDataAsJson(HttpServletRequest request){
        String json = getRequest().getParameter("json");
        return json;
    }

}
