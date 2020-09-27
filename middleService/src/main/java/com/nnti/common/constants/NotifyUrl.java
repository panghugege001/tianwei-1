package com.nnti.common.constants;


import com.nnti.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 回调url
 *
 * @author
 */
public class NotifyUrl {

    private static Map<String, HashMap<String, String>> classmap = new HashMap<String, HashMap<String, String>>();

    static {
        HashMap<String, String> map = new HashMap<String, String>();
        map = new HashMap<String, String>();
        map.put("lh", "www.mfbtobacke68.com");
        map.put("yl", "www.mfbtobacklong8.com");
        map.put("dy", "www.mfbtobackyh.com");
        map.put("qy", "www.mfbtobackqy.com");
        map.put("ufa", "www.mfbtobackufa.com");
        map.put("ul", "www.mfbtobackul.com");
        map.put("ql", "www.mfbtobackqle.com");
        map.put("ws", "www.mfbtobackws.com");
        map.put("mzc", "www.mfbtobackdream.com");
        map.put("ld", "www.mfbtobacklongdu.com");
        map.put("loh", "www.mfbtobacklonghu.com");
        map.put("zb", "www.mfbtobackzb.com");
        map.put("text", "119.28.11.28");   
        classmap.put("notifyUrl", map);
    }

    //获取产品名称
    public static String getProduct(String notifyUrl) throws Exception {
        for (Iterator iterator = classmap.get("notifyUrl").entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            if (notifyUrl.contains(entry.getValue())) {
                return entry.getKey();
            }
        }
        throw new BusinessException(ErrorCode.SC_10001.getCode(), "解析域名，获取产品名称发生异常");
    }


}