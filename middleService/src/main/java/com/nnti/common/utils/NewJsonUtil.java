package com.nnti.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

public class NewJsonUtil {
	 private static final ObjectMapper mapper = new ObjectMapper();
	    private static final ObjectMapper mapperForHtml = new ObjectMapper();

	    private NewJsonUtil() {
	    }

	    public static String toJson(Object obj) {
	        if (null != obj) {
	            try {
	                return mapper.writeValueAsString(obj);
	            } catch (IOException var2) {
	                System.out.println("toJson ERROR: " + var2.getMessage());
	                return null;
	            }
	        } else {
	            return null;
	        }
	    }

	    public static String toJsonForHtml(Object obj) throws Exception {
	        return mapperForHtml.writeValueAsString(obj);
	    }

	    public static <T> T toObject(String json, Class<T> type) {
	        if (StringUtil.isNotBlank(json)) {
	            try {
	                return mapper.readValue(json, type);
	            } catch (Exception var3) {
	                System.out.println("toJson ERROR: " + var3.getMessage());
	                return null;
	            }
	        } else {
	            return null;
	        }
	    }

	    public static <T> T toObject(String json, TypeReference<T> type) {
	        if (StringUtil.isNotBlank(json)) {
	            try {
	                return mapper.readValue(json, type);
	            } catch (Exception var3) {
	                System.out.println("toJson ERROR: " + var3.getMessage());
	                return null;
	            }
	        } else {
	            return null;
	        }
	    }

	    public static <T> T toMapObject(String json) {
	        if (StringUtil.isNotBlank(json)) {
	            try {
	                MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
	                return mapper.readValue(json, type);
	            } catch (Exception var2) {
	                System.out.println("toJson ERROR: " + var2.getMessage());
	                return null;
	            }
	        } else {
	            return null;
	        }
	    }

	    public static void main(String[] args) {
	        String ss = "{\"data\":{\"list\":[{\"USERNAME\":\"聂玮inaccs\",\"URL\":\"http://oa1.zj.chinaccs.com:80/zjgs/zjgsswcy.nsf/0/4825777D0033CA8248257864001AEC55/?OpenDocument&Login&UserId=1528&Read=True\",\"TITLE\":\"关于同意对浙江沸蓝通信工程监理有限公司增资的批复\",\"DDSJ\":\"2011-03-31 16:49:36.0\",\"SSMK\":\"传阅\",\"JJCD\":\"0\"}]},\"status\":\"1\",\"decription\":\"成功\"}";
	        Map<String, Object> map = (Map)toMapObject(ss);
	        System.out.println(map.get("decription"));
	        Map<String, Object> listMap = (Map)map.get("data");
	        System.out.println(listMap.get("list"));
	        List<Map<String, Object>> list = (List)listMap.get("list");
	        System.out.println(((Map)list.get(0)).get("USERNAME"));
	    }

	    static {
	        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        mapperForHtml.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	    }
}
