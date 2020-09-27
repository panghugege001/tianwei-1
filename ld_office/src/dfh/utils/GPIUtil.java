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
	//getbalance
	private static final String getbalanceAction = "/op/getbalance";
	
	private static final String merch_id = "ld";
	private static final String merch_pwd = "0F1C6065-1DEA-4A4F-88E3-AC130E0282B8";
	
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
}
