package app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dfh.exception.PostFailedException;
import dfh.exception.ResponseFailedException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import java.util.Map;

public class HttpClientUtil implements java.io.Serializable {

    static Logger log = Logger.getLogger(HttpClientUtil.class);
    private static final long serialVersionUID = -176092625883595547L;
    private static ObjectMapper mapper = new ObjectMapper();
    public static String ENCODE_TYPE = "UTF-8";
    private static Integer TIME_OUT = 20000;

    private final static boolean DEBUG = false;

    public static org.apache.commons.httpclient.HttpClient createHttpClient() {
        org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();
        HttpClientParams params = new HttpClientParams();
        params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
        params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
        params.setParameter("http.socket.timeout", TIME_OUT);
        httpclient.setParams(params);
        return httpclient;
    }

    public static Map<String, String> postByAES(String action, Map<String, Object> paramMap) throws Exception {
        PostMethod post = new PostMethod(action);
        post.setRequestHeader("Connection", "close");
        org.apache.commons.httpclient.HttpClient httpclient = createHttpClient();
        String str = mapper.writeValueAsString(paramMap);
        String requestJson = AESUtil.getInstance().encrypt(str);
        try {
            log.info("POST URL:" + post.getURI());
            post.setRequestHeader("Connection", "close");
            post.setParameter("requestData", requestJson);
            int statusCode = httpclient.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
                throw new PostFailedException("the post result status is not OK" + post.getURI());
            } else {
                ObjectMapper mapper = new ObjectMapper();
                byte[] responseBody = post.getResponseBody();
                String responseString = new String(responseBody);
                Map<String, String> resultMap = mapper.readValue(responseString, Map.class);
                responseString = resultMap.get("responseData");
                responseString = AESUtil.getInstance().decrypt(responseString);
                resultMap = mapper.readValue(responseString, Map.class);
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseFailedException(e.getMessage());
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
            if (httpclient != null) {
                ((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
            }
        }
    }

    /**
     * log调试
     */
    private static void log(String message) {
        if (DEBUG) {
            log.debug(message);
        }
    }

}
