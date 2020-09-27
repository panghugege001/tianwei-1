package dfh.security;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mars on 2016/6/7.
 */
public class IpHelper {

    /**
     * 取得Ip , Forwarded 资料优先
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String forwaredFor = request.getHeader("X-Forwarded-For");
        String remoteAddr = request.getRemoteAddr();
        String ip;
        if (StringUtils.isNotEmpty(forwaredFor))
            ip = forwaredFor.split(",")[0];
        else {
            ip = remoteAddr;
        }
        return isIp(ip) ? ip : "";
    }

    /**
     * 判断是不是ip
     *
     * @param addr
     * @return
     */
    public static boolean isIp(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }


    /**
     * Ip to BClass Ip or CClass IP<br/>
     * 127.0.0.1 => b => 127.0.***.*** <br/>
     * 127.0.0.1 => c => 127.0.0.***
     *
     * @param classLayer b or c Class
     * @param ip
     * @return
     */
    public static String getClassIp(String classLayer, String ip) {
        String[] ips = ip.replace('.', ',').split(",");//如果ip是127.0.0.1
        if (classLayer.equalsIgnoreCase("b")) {
            return ips[0] + "." + ips[1] + "." + "***" + "." + "***"; //转化成127.0.***.***
        }
        if (classLayer.equalsIgnoreCase("c")) {
            return ips[0] + "." + ips[1] + "." + ips[2] + "." + "***";//转化成127.0.0.***
        }
        return ip;
    }

}
