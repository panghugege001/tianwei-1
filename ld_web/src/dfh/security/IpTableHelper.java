package dfh.security;

import dfh.utils.IPSeeker;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by mars on 2016/6/7.
 */
public class IpTableHelper {

    private static Logger log = Logger.getLogger(IpTableHelper.class);

    public static boolean isAllow(HttpServletRequest req) {
        String ip = IpHelper.getIp(req);
        if (ip.isEmpty()) {
            return false;
        }
        String bClassIp = IpHelper.getClassIp("b", ip);
        String cClassIp = IpHelper.getClassIp("c", ip);
        String allow = getAllowString(req);
        String deny = getDenyString(req);

        if (deny.indexOf(ip) >= 0 || deny.indexOf(bClassIp) >= 0 || deny.indexOf(cClassIp) >= 0) {
            log.info("deny ip:" + ip);
            return false;
        }

        if (allow.indexOf(ip) >= 0 || allow.indexOf(bClassIp) >= 0 || allow.indexOf(cClassIp) >= 0) {
            log.info("allow ip:" + ip);
            return true;
        }
        IPSeeker ipSeeker = getIpSeeker(req);
        //ip="120.28.191.170";
        String area = ipSeeker.getCountry(ip);
        log.info("login request area = " + area);
        if (Ipconfine.getDenyArea().contains(area)) {
            log.info("Deny Request ip:" + ip + "屏蔽地区:" + area);
            return false;
        }
        log.info("ip not in deny or allow ipconfig.xml , allow to login , ip = " + ip);
        return true;
    }

    private static IPSeeker getIpSeeker(HttpServletRequest request) {
        return (IPSeeker) request.getSession().getServletContext().getAttribute("ipSeeker");
    }


    public static String getAllowString(HttpServletRequest req) {
        return getConfigMap(req).get("allow");
    }

    public static String getDenyString(HttpServletRequest req) {
        return getConfigMap(req).get("deny");
    }

    private static Map<String, String> getConfigMap(HttpServletRequest request) {
        return (Map<String, String>) request.getSession().getServletContext().getAttribute("ipconfineMap");
    }


}
