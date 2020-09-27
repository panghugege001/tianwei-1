package dfh.action.customer;

import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;
import dfh.action.SubActionSupport;
import dfh.action.vo.EBetRegisterOrLoginReq;
import dfh.action.vo.EBetRegisterOrLoginResp;
import dfh.action.vo.builder.EBetRegisterOrLoginRespBuilder;
import dfh.security.IpTableHelper;
import dfh.skydragon.webservice.model.LoginInfo;
import dfh.utils.AxisUtil;
import dfh.utils.ClientInfo;
import dfh.utils.IPSeeker;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mars on 2016/6/4.
 */
public class EBetAppAction extends SubActionSupport {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(EBetAppAction.class);
    final Gson gson = new Gson();
    static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    static final int DEFAULT_SUB_CHANNEL_ID = 0;

    public void loginGameEBetApp() throws Exception {
        String httpMethod = getRequest().getMethod();
        HttpServletResponse response = ServletActionContext.getResponse();
        EBetRegisterOrLoginReq eBetReq = new EBetRegisterOrLoginReq();
        LoginInfo loginInfo = null;
        if ("POST".equalsIgnoreCase(httpMethod)) {
            try {
                eBetReq = getLoginReq(getRequest());
                if (eBetReq != null && eBetReq.isValidRequest()) {
                    if (eBetReq.isUserInfo()) {
                        reciveUserinfo();
                        returnSuccess(response);
                    }
                    if (eBetReq.isRegisterOrLoginReq()) {
                        loginInfo = login(eBetReq);
                        String token = getToken(eBetReq);
                        log.info("login Success , userName = " + loginInfo.getUser().getLoginname() + ", token = " + token);
                        returnSuccess(response, loginInfo.getUser().getLoginname(), token);
                    }
                } else {
                    throw new IllegalAccessException("401");
                }
            } catch (IllegalAccessException iae) {
                EBetRegisterOrLoginResp eBetResponse;
                if (iae.getMessage() != null && !iae.getMessage().isEmpty() && iae.getMessage().matches("\\d+")) {
                    eBetResponse = getErrorResponse(eBetReq, iae.getMessage());
                } else {
                    eBetResponse = getErrorResponse(eBetReq, "4026");
                }
                log.warn("login fail , msg =" + iae.getMessage());
                response.setStatus(200);
                writeText(gson.toJson(eBetResponse));
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("登入失败 ,  Message = " + e.getMessage() + " , login userName = " + eBetReq.getUsername() + " ,  timestamp  = " + new DateTime(eBetReq.getTimestamp()).toString(DATE_PATTERN));
                response.setStatus(200);
                EBetRegisterOrLoginResp eBetResponse = getErrorResponse(eBetReq, "4026");
                writeText(gson.toJson(eBetResponse));
            }
        } else {
            response.setStatus(200);
            EBetRegisterOrLoginResp eBetResponse = getErrorResponse(eBetReq, "4026");
            writeText(gson.toJson(eBetResponse));
        }
    }

    private LoginInfo login(EBetRegisterOrLoginReq eBetReq) throws Exception {
        //檢查ip
//        if (!IpTableHelper.isAllow(getRequest())) {
//            throw new IllegalAccessException("4027");
//        }
        String city = getCity();
        String remoteIp = getIpAddr();
        int eventType = eBetReq.getEventType();
        LoginInfo info;
        //eventType =3 with Token or eventType=4
        if (eventType == 4) {
            info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
                            + "UserWebService", false), AxisUtil.NAMESPACE, "loginEBetAppByToken",
                    new Object[]{eBetReq.getDecodeToken(), remoteIp, city, ClientInfo.getOSName(getRequest().getHeader("user-agent")), eBetReq.getEventType()}, LoginInfo.class);
        } else {
            info = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
                    + "UserWebService", false), AxisUtil.NAMESPACE, "loginEBetApp", new Object[]{eBetReq.getUsername()
                    , eBetReq.getPassword(), remoteIp, city, ClientInfo.getOSName(getRequest().getHeader("user-agent")), eBetReq.getEventType()}, LoginInfo.class);
        }
        if (info != null && info.getSucFlag().equals(1)) {
            return info;
        } else {
            String msg = info.getMsg();
            if (msg.contains("无效Token")) {
                throw new IllegalAccessException("410");
            }
            if (msg.contains("密码错误")) {
                throw new IllegalAccessException("401");
            }
            throw new IllegalAccessException(info.getMsg());
        }
    }

    private EBetRegisterOrLoginReq getLoginReq(HttpServletRequest request) throws IllegalAccessException {
        // get the HTTP incoming post stream
        StringBuilder buffer = new StringBuilder();
        try {
            String charsetName = request.getCharacterEncoding();
            if (charsetName == null) {
                charsetName = "UTF-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), charsetName));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            log.info("Login from EBet App , request data = " + buffer.toString());
            return gson.fromJson(buffer.toString(), EBetRegisterOrLoginReq.class);
        } catch (MalformedJsonException je) {
            log.warn("convert to EBetAppRequest Object fail , message  = " + je.getMessage());
            throw new IllegalAccessError("401");
        } catch (IOException e1) {
            log.warn("convert to EBetAppRequest Object fail , message  = " + e1.getMessage());
            throw new IllegalAccessError("401");
        }
    }

    /**
     * eBetApp Server验证登入后会回传User资料一次 , 目前不做动作
     *
     * @return
     */
    private boolean reciveUserinfo() {
        return true;
    }

    private void returnSuccess(HttpServletResponse response) {
        response.setStatus(200);
        writeText("Success");
    }

    private void returnSuccess(HttpServletResponse response, String userName, String token) throws Exception {
        EBetRegisterOrLoginResp resp = EBetRegisterOrLoginRespBuilder.anResp()
                .withStatus(200)
                .withSubChannelId(DEFAULT_SUB_CHANNEL_ID)
                .withAccessToken(token)
                .withUsername(userName)
                .build();
        response.setStatus(200);
        writeText(gson.toJson(resp));
    }

    /**
     * Token format : URLEncoder.encode(AES256.encoding(userName + timestamp + md5(userName+key)))
     *
     * @param
     * @return
     * @throws Exception
     */
    private String getToken(EBetRegisterOrLoginReq req) throws Exception {
        try {
            String token;
            if ((req.getAccessToken() == null || req.getAccessToken().isEmpty())
                    && req.getUsername() != null && !req.getUsername().isEmpty()) {
                token = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
                        + "TokenWebService", false), AxisUtil.NAMESPACE, "createLoginToken", new Object[]{"ebetapp", req.getUsername()}, String.class);
            } else {
                token = req.getAccessToken();
            }
            return token;
        } catch (Exception e) {
            log.error("Get Token fail , username = " + req.getUsername());
            throw e;
        }
    }

    public String getCity() {
        IPSeeker seeker = (IPSeeker) getHttpSession().getServletContext()
                .getAttribute("ipSeeker");
        String remoteIp = getIpAddr();
        String city = "";
        String temp = remoteIp != null ? seeker.getAddress(remoteIp) : "";
        if (null != temp && !"CZ88.NET".equals(temp)) {
            city = temp;
        }
        return city;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        EBetAppAction.log = log;
    }

    public EBetRegisterOrLoginResp getErrorResponse(EBetRegisterOrLoginReq eBetReq, String errorCode) {
        String userName = "";
        if (eBetReq != null && eBetReq.getUsername() != null) {
            userName = eBetReq.getUsername();
        }
        return EBetRegisterOrLoginRespBuilder
                .anResp()
                .withStatus(Integer.parseInt(errorCode))
                .withUsername(userName)
                .withSubChannelId(DEFAULT_SUB_CHANNEL_ID)
                .withAccessToken("")
                .build();
    }

}
