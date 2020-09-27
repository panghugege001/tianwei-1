package com.nnti;

/**
 * Created by wander on 2017/3/7.
 */

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付Demo
 */
public class PayDemo {
    static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//    static String merNo = "Mer201703011668";// 商户号
//    static String key = "C45FCD2513F2E0F272BB08C71D04F95C";// 密钥,24位
//    static String reqUrl = "http://zfb.h8pay.com/api/pay.action";

    static String merNo = "Mer000001";// 商户号
    static String key = "Pp3GWZ6RR1MOeB5frsqbWQr8";// 密钥,24位
    static String reqUrl = "http://139.199.195.194:8080/api/pay.action";

     public static void main(String[] args) throws Throwable {
        pay();
    }

    /**
     * 支付结果处理
     *
     * @throws Throwable
     */
    public static void result(HttpServletRequest request,
                              HttpServletResponse response) throws Throwable {
        String data = request.getParameter("data");
        JSONObject jsonObj = JSONObject.fromObject(data);
        Map<String, String> metaSignMap = new TreeMap<String, String>();
        metaSignMap.put("merNo", jsonObj.getString("merNo"));
        metaSignMap.put("netway", jsonObj.getString("netway"));
        metaSignMap.put("orderNum", jsonObj.getString("orderNum"));
        metaSignMap.put("amount", jsonObj.getString("amount"));
        metaSignMap.put("goodsName", jsonObj.getString("goodsName"));
        metaSignMap.put("payResult", jsonObj.getString("payResult"));// 支付状态
        metaSignMap.put("payDate", jsonObj.getString("payDate"));// yyyy-MM-dd
        // HH:mm:ss
        String jsonStr = mapToJson(metaSignMap);
        String sign = MD5(jsonStr.toString() + key, "UTF-8");
        if (!sign.equals(jsonObj.getString("sign"))) {
            return;
        }
        System.out.println("签名校验成功");
        response.getOutputStream().write("0".getBytes());
    }

    /**
     * 支付方法
     */
    public static void pay() {
        Map<String, String> metaSignMap = new TreeMap<String, String>();
        metaSignMap.put("merNo", merNo);
        metaSignMap.put("netway", "WX");// WX:微信支付,ZFB:支付宝支付
        metaSignMap.put("random", randomStr(4));// 4位随机数
        String orderNum = new SimpleDateFormat("yyyyMMddHHmmssSSS")
                .format(new Date()); // 20位
        orderNum += randomStr(3);
        metaSignMap.put("orderNum", orderNum);
        metaSignMap.put("amount", "500");// 单位:分
        metaSignMap.put("goodsName", "笔");// 商品名称：20位
        metaSignMap.put("callBackUrl", "http://127.0.0.1/");// 回调地址
        metaSignMap.put("callBackViewUrl", "http://localhost/view");// 回显地址
        String metaSignJsonStr = mapToJson(metaSignMap);
        String sign = MD5(metaSignJsonStr + key, "UTF-8");// 32位
        System.out.println("sign=" + sign); // 英文字母大写
        metaSignMap.put("sign", sign);
        String reqParam = "data=" + mapToJson(metaSignMap);
        String resultJsonStr = request(reqUrl, reqParam);

        // 检查状态
        JSONObject resultJsonObj = JSONObject.fromObject(resultJsonStr);
        String stateCode = resultJsonObj.getString("stateCode");
        if (!stateCode.equals("00")) {
            return;
        }
        String resultSign = resultJsonObj.getString("sign");
        resultJsonObj.remove("sign");
        String targetString = MD5(resultJsonObj.toString() + key, "UTF-8");
        if (targetString.equals(resultSign)) {
            System.out.println("签名校验成功");
        }
    }

    private static String getResponseBodyAsString(InputStream in) {
        try {
            BufferedInputStream buf = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            StringBuffer data = new StringBuffer();
            int readDataLen;
            while ((readDataLen = buf.read(buffer)) != -1) {
                data.append(new String(buffer, 0, readDataLen, "UTF-8"));
            }
            System.out.println("响应报文=" + data);
            return data.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String request(String url, String params) {
        try {
            System.out.println("请求报文:" + params);
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(1000 * 5);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(params.length()));
            OutputStream outStream = conn.getOutputStream();
            outStream.write(params.toString().getBytes("UTF-8"));
            outStream.flush();
            outStream.close();
            return getResponseBodyAsString(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static String MD5(String s, String encoding) {
        try {
            byte[] btInput = s.getBytes(encoding);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String mapToJson(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        StringBuffer json = new StringBuffer();
        json.append("{");
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            json.append("\"").append(key).append("\"");
            json.append(":");
            json.append("\"").append(value).append("\"");
            if (it.hasNext()) {
                json.append(",");
            }
        }
        json.append("}");
        System.out.println("mapToJson=" + json.toString());
        return json.toString();
    }

    public static String randomStr(int num) {
        char[] randomMetaData = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g',
                'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
                'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
                '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        String tNonceStr = "";
        for (int i = 0; i < num; i++) {
            tNonceStr += (randomMetaData[random
                    .nextInt(randomMetaData.length - 1)]);
        }
        return tNonceStr;
    }
}

