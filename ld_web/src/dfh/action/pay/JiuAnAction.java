package dfh.action.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.utils.AESUtil;
import dfh.utils.Configuration;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


public class JiuAnAction extends SubActionSupport{

    private static Logger log = Logger.getLogger(JiuAnAction.class);

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取商户id与回调地址
     * @throws Exception
     */
    public void bindWalletInfo() throws Exception{
    	return ;

       /* Map<String , Object> resultMap = new HashMap<>();

        try{

            //获取登录session
            Users users = getCustomerFromSession();

            validUsers(users , resultMap);

            Map<String , Object> paramMap = new HashMap<>();

            paramMap.put("product" , "ld");
            paramMap.put("loginName" , users.getLoginname());

            Map<String, String> tempMap = execute(paramMap , "/jiuan/bindWalletInfo");

            validTempMap(tempMap , resultMap);

            resultMap.put("code", "10000");
            resultMap.put("data", tempMap);

            writeText(mapper.writeValueAsString(resultMap));

        }   catch (Exception e) {

            e.printStackTrace();

            resultMap.put("code", "20000");
            resultMap.put("message", "接口调用异常！");
        }

        writeText(mapper.writeValueAsString(resultMap));*/
    }

    /**
     * 获取久安绑定信息
     * @throws Exception
     */
    public void getWalletInfo() throws Exception{

        Map<String ,Object> resultMap = new HashMap<>();

        try {

            //获取用户session
            Users users = getCustomerFromSession();

            validUsers(users , resultMap);

            Map<String , Object> paramMap = new HashMap<>();
            paramMap.put("product" , "ld");
            paramMap.put("loginName" , users.getLoginname());

            Map<String , String> tempMap = execute(paramMap , "/jiuan/getWalletInfo");

            validTempMap(tempMap , resultMap);

            resultMap.put("code" , "10000");
            resultMap.put("data" , tempMap);

            writeText(mapper.writeValueAsString(resultMap));

        } catch(Exception e){

            e.printStackTrace();

            resultMap.put("code" , "20000");
            resultMap.put("message" , "接口调用异常！");
        }

        writeText(mapper.writeValueAsString(resultMap));
    }

    /**
     * 充值接口
     * @throws Exception
     */
    public void confirmPayment() throws Exception{
        Map<String , Object> resultMap = new HashMap<>();

        try {

            HttpServletRequest request = getRequest();

            Users users = getCustomerFromSession();

            validUsers(users , resultMap);

            String orderAmount = request.getParameter("orderAmount");
            String usetype = request.getParameter("usetype");

            Map<String, Object> paramMap = new HashMap<String, Object>();

            paramMap.put("product", "ld");
            paramMap.put("loginName", users.getLoginname());
            paramMap.put("orderAmount", orderAmount);
            paramMap.put("usetype", usetype);

            Map<String, String> tempMap = execute(paramMap, "/jiuan/cp");

            validTempMap(tempMap , resultMap);

            resultMap.put("code", "10000");
            resultMap.put("data", tempMap);

            writeText(mapper.writeValueAsString(resultMap));

        } catch(Exception e){

            e.printStackTrace();

            resultMap.put("code" , "20000");
            resultMap.put("message" , "接口调用异常！");
        }

        writeText(mapper.writeValueAsString(resultMap));
    }

    /**
     * 跳转收银台之前调用，获取商户信息
     * @throws Exception
     */
    public void getInitInterfaceData() throws Exception{

        Map<String , Object> resultMap = new HashMap<>();

        try {

            HttpServletRequest request = getRequest();

            Users users = getCustomerFromSession();

            validUsers(users , resultMap);

            String orderAmount = request.getParameter("orderAmount");
            String usetype = request.getParameter("usetype");
            String platformId = request.getParameter("platformId");

            Map<String, Object> paramMap = new HashMap<String, Object>();

            paramMap.put("product", "ld");
            paramMap.put("loginName", users.getLoginname());
            paramMap.put("orderAmount", orderAmount);
            paramMap.put("usetype", usetype);
            paramMap.put("platformId" , platformId);

            Map<String, String> tempMap = execute(paramMap, "/jiuan/getInitInterfaceData");

            validTempMap(tempMap , resultMap);

            resultMap.put("code", "10000");
            resultMap.put("data", tempMap);

            writeText(mapper.writeValueAsString(resultMap));

        } catch(Exception e){

            e.printStackTrace();

            resultMap.put("code", "20000");
            resultMap.put("message", "接口调用异常！");
        }

        writeText(mapper.writeValueAsString(resultMap));
    }


    public void validUsers(Users users , Map<String ,Object> resultMap) throws Exception{

        if (null == users) {

            resultMap.put("code", "20000");
            resultMap.put("message", "登录会话超时或未登录，请登录后再进行操作！");

            writeText(mapper.writeValueAsString(resultMap));
        }
    }

    public void validTempMap(Map<String, String> tempMap , Map<String , Object> resultMap) throws Exception{

        if (null == tempMap) {

            resultMap.put("code", "20000");
            resultMap.put("message", "获取绑定信息异常！");

            writeText(mapper.writeValueAsString(resultMap));
        }
    }

    public Map<String , String> execute(Map<String , Object> paramMap , String uri){

       /* try{

            String str = mapper.writeValueAsString(paramMap);

            String requestJson = AESUtil.encrypt(str);

            String url = SpecialEnvironmentStringPBEConfig.getPlainText(Configuration.getInstance().getValue("middleservice.url.pay"));

            url = url + uri;

            log.info("久安调用中间件url：" + url);

            HttpClientParams params = new HttpClientParams();

            params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
            params.setParameter("http.protocol.content-charset", "UTF-8");
            params.setParameter("http.socket.timeout", 50000);

            HttpClient httpClient = new HttpClient();

            httpClient.setParams(params);

            PostMethod method = new PostMethod(url);

            method.setRequestHeader("Connection", "close");
            method.setParameter("requestData", requestJson);

            int statusCode = httpClient.executeMethod(method);

            if (statusCode == HttpStatus.SC_OK) {

                byte[] responseBody = method.getResponseBody();
                String responseString = new String(responseBody);

                Map<String, String> tempMap = mapper.readValue(responseString, Map.class);
                log.info("tempMap：" + tempMap);

                String decrypt = AESUtil.decrypt(tempMap.get("data"));

                Map<String, String> resultMap = mapper.readValue(decrypt, Map.class);

                return resultMap;
            } else {

                log.info("statusCode：" + statusCode + "，content：" + method.getStatusLine());
            }

        } catch(Exception e){

            e.printStackTrace();
        }*/
        return null;
    }

}
