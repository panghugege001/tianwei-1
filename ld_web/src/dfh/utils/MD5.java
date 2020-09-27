package dfh.utils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;  
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  

/**
 * MD5概要说明：MD5加密
 * <br>@author zj
 */
public class MD5 {
	
	public static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
        try {
            messageDigest = MessageDigest.getInstance("MD5");  
            messageDigest.reset();  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        byte[] byteArray = messageDigest.digest();  
        StringBuffer md5StrBuff = new StringBuffer();  
        for (int i = 0; i < byteArray.length; i++) {  
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
        return md5StrBuff.toString();  
    }  
	
	
	 private static final String hexDigits[] = {
	        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
	        "a", "b", "c", "d", "e", "f"
	    };
	    
	    private static String byteArrayToHexString(byte b[])
	    {
	        StringBuffer resultSb = new StringBuffer();
	        for(int i = 0; i < b.length; i++)
	            resultSb.append(byteToHexString(b[i]));

	        return resultSb.toString();
	    }

	    private static String byteToHexString(byte b)
	    {
	        int n = b;
	        if(n < 0)
	            n += 256;
	        int d1 = n / 16;
	        int d2 = n % 16;
	        return hexDigits[d1] + hexDigits[d2];
	    }

	    public static String MD5Encode(String origin)
	    {
	        String resultString = null;
	        try
	        {
	            resultString = new String(origin);
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
	        }
	        catch(Exception exception) { }
	        return resultString;
	    }
	    
		public static String EkaPayBankMd5Sign(String type,String parter,String value,String orderid,String callbackurl,String md5key){
			StringBuffer sendsb = new StringBuffer();
			sendsb.append("parter="+parter);
			sendsb.append("&type="+type);
			sendsb.append("&value="+value);
			sendsb.append("&orderid="+orderid);
			sendsb.append("&callbackurl="+callbackurl);
			return MD5.MD5Encode(sendsb + md5key);
	}
		
	public static String EkaPayCardBackMd5Sign(String orderid,String opstate,String ovalue,String key){
		
		StringBuffer sendsb = new StringBuffer();
		sendsb.append("orderid="+orderid);
		sendsb.append("&opstate="+opstate);
		sendsb.append("&ovalue="+ovalue);
		return MD5.MD5Encode(sendsb + key);
	}
	
	/**
	 * 发送请求到服务器，并接收返回信息
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public  static String receiveBySend(String urlStr, String encoding)
			throws Exception {
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		HttpURLConnection conn = null;
		try {
			StringBuffer sb = new StringBuffer();
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			isr = new InputStreamReader(is, encoding);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			String xml = sb.toString();
			return xml;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				br.close();
				isr.close();
				is.close();
				conn.disconnect();
			} catch (Exception e) {
			}
		}
	}
	
	
}
