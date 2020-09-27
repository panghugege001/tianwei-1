package dfh.utils;

import dfh.exception.PostFailedException;
import dfh.exception.ResponseFailedException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class JLKUtil {

    // 正式商户秘钥
    public static final String PRO_MCH_SECRET = "694634b2f65cf8c66a642b0dd0e64444";
    // 正式商户公钥
    private static final String PRO_PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAz3Jaxwt0lUOnLJaR7KmI+Oi6O85shi3mUnR5eiykmWtanhv8RzAp0KnWtgDH8e5VY5n1JGZgd779x79MjdtC8JNtQNntsxDxLlGyQ8SFbOwMsEV69m0fH1wY8n9TXfO1YaR/jyGpAHrG7mzvKmqdrmW6JmYeEgo11lUpnFkKnE26pE+nx50Lphn2L4FOwrCYnC8DvxOLMOLz1YkFiHDGu0b2XZxa8zbUstm4Sx+MD7rkVSblkMt2zEjZkmt+3inmn5/P2xGIwEZG+Yt5/1sJw5SY2oKF2d/mQWojdyMnp0PXgNsLzL5wMZRu0OuXEBW3UOy/FfWNsj7gFdN6W98POrKooiyeUic+j/Cxn4PDLNHp+D6lx2ryqbGuPCU+vdtLRHzf1FQH5tha/FJfCZ/KIs+oAYGiYRFEr4XIqwxQfBQu7lhD7nbYIJZb0lb5+9Jr86mHSXZT7il2lGHxOfmq2FhvqAZdIPADxz428dNySRoqp9cx/o2GTOUCqc5EFKL/cutfw9GhHF6rwi18QqAIeOOtMAQCYCL+Nih9SeRhB8HFROKHPqPK4HFmGYhG8dZkiDfMXY9NU7pIEA+6GsAptSmAKn17HPAiP2v0oCRhyhNdbpwHMakI6vsYspvzdd1hkp2rNxx0nzC4vRiNQ+MLy0kmthkewaxnkQPzCHkJJPkCAwEAAQ==";
    // 正式查询订单地址
    private static final String PRO_QUERY_API = "https://app.tccpay.com/cashier/payquery";
    //
    private static Boolean setTimeOut = true;
    //
    private static int socketTimeout = 3000;
    //
    private static int connectTimeout = 3000;
    //
    private static String ENCODE_TYPE = "UTF-8";
    //
    private static Integer TIME_OUT = 20000;

    public static String queryOrder(String out_trade_no, String trade_no) throws Exception {

        String iv16 = getRandomString(16);
        String sc32 = getRandomString(32);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("rand", iv16);
        map.put("out_trade_no", out_trade_no);
        map.put("trade_no", trade_no);

        String asciiStr = JLKSHA256.unionpayASCII(map) + "&secret=" + PRO_MCH_SECRET;

        String sign = JLKSHA256.sha256_HMAC(asciiStr, PRO_MCH_SECRET).toUpperCase();

        map.put("sign", sign);

        JSONObject jsonObject = JSONObject.fromObject(map);
        String jsonString = jsonObject.toString();

        JLKRSAEncrypt.loadPublicKey(PRO_PUBLIC_KEY);

        String data = JLKAES.encrypt(jsonString.getBytes(), sc32.getBytes(), iv16).replaceAll("\r|\n", "");
        String sc = JLKRSAEncrypt.encrypt(sc32.getBytes()).replaceAll("\r|\n", "");

        Map<String, String> mapPost = new HashMap<String, String>();

        mapPost.put("sc", sc);
        mapPost.put("iv", iv16);
        mapPost.put("mch_secret", PRO_MCH_SECRET);
        mapPost.put("data", data);

        String returnResult = post(PRO_QUERY_API, null, mapPost, null);
        System.out.println("returnResult的值为：" + returnResult);

        return returnResult;
    }

    public static String getRandomString(int length) {

        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";

        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {

            int number = random.nextInt(62);

            sb.append(str.charAt(number));
        }

        return sb.toString();
    }

    private static CloseableHttpClient getHttpClient() {

        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();

        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();

        registryBuilder.register("http", plainSF);

        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            TrustStrategy anyTrustStrategy = new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    return true;
                }
            };

            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();

            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {

            throw new RuntimeException(e);
        } catch (KeyManagementException e) {

            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }

        Registry<ConnectionSocketFactory> registry = registryBuilder.build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);

        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    public static String post(String url, Map<String, String> queries, Map<String, String> params, Map<String, String> headers) throws IOException {

        String responseBody = "";

        CloseableHttpClient httpClient = getHttpClient();

        StringBuilder sb = new StringBuilder(url);

        if (queries != null && queries.keySet().size() > 0) {

            boolean firstFlag = true;

            Iterator iterator = queries.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry<String, String>) iterator.next();

                if (firstFlag) {

                    sb.append("?" + (String) entry.getKey() + "=" + (String) entry.getValue());

                    firstFlag = false;
                } else {

                    sb.append("&" + (String) entry.getKey() + "=" + (String) entry.getValue());
                }
            }
        }

        HttpPost httpPost = new HttpPost(sb.toString());

        if (setTimeOut) {

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

            httpPost.setConfig(requestConfig);
        }

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        if (params != null && params.keySet().size() > 0) {

            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();

                nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }

        if (headers != null && headers.keySet().size() > 0) {

            for (String key : headers.keySet()) {

                httpPost.setHeader(key, headers.get(key));
            }
        }

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {

                HttpEntity entity = response.getEntity();

                responseBody = EntityUtils.toString(entity);
            } else {

                System.out.println("http return status error:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            response.close();
        }

        return responseBody;
    }

    public static HttpClient createHttpClient() {

        HttpClient httpclient = new HttpClient();

        HttpClientParams params = new HttpClientParams();

        params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
        params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
        params.setParameter("http.socket.timeout", TIME_OUT);

        httpclient.setParams(params);

        return httpclient;
    }

    public static String post(String action, Map<String, Object> params) throws PostFailedException, ResponseFailedException {

        int statusCode = -999;

        PostMethod post = new PostMethod(action);

        post.setRequestHeader("Connection", "close");

        HttpClient httpclient = createHttpClient();

        if (params != null) {

            Iterator it = params.keySet().iterator();

            while (it.hasNext()) {

                String key = (String) it.next();
                String value = (String) params.get(key);

                post.setParameter(key, value);
            }
        }

        try {

            statusCode = httpclient.executeMethod(post);

            if (statusCode != 200) {

                throw new PostFailedException("the post result status is not OK");
            }

            String responseBody = post.getResponseBodyAsString();

            return responseBody;
        } catch (Exception e) {

            e.printStackTrace();

            throw new PostFailedException(e.getMessage());
        } finally {

            if (post != null) {

                post.releaseConnection();
            }
        }
    }
}
