package com.nnti.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.security.DigestUtils;

/***
 * 全局工具类
 *
 */
public class MyUtils {

    private static Logger log = Logger.getLogger(MyUtils.class);

    /**
     * 去掉任何空白字符
     *
     * @param content
     * @return
     */
    public static String getTrimStr(String content) {
        if (content == null) {
            return "";
        }
        return content.replaceAll("[\\s\\p{Zs}]+", "");
    }

    /**
     * 判断是否包含内容
     */
    public final static <T extends Object> boolean isNotEmpty(T str) {
        return str != null && !str.toString().trim().equals("");
    }

    /**
     * 判断Map集合是否为空
     *
     * @param map
     * @return
     */
    public final static <T extends Map<?, ?>> boolean isNotEmpty(T map) {
        return map != null && map.size() > 0;
    }

    /**
     * 判断数字是否大于零
     */
    public static <T extends Number> boolean isGtZero(T ls) {
        return ls != null && ls.doubleValue() > 0;
    }

    /**
     * 根据键值对得到map<String,String>对象
     *
     * @param ag
     * @return
     */
    public static LinkedHashMap<String, String> getStrValueMap(String... ag) {
        LinkedHashMap<String, String> mp = new LinkedHashMap<String, String>();
        if (ag != null && ag.length > 0 && ag.length % 2 == 0) {
            for (int i = 0; i < ag.length; i++) {
                mp.put(ag[i], ag[++i]);
                if (i == ag.length) {
                    break;
                }

            }
        }
        return mp;
    }

    private final static Pattern pt = Pattern.compile("^[1-9]+[0-9]*[.]?\\d*$");

    /**
     * 判断是否为数字
     *
     * @param tg
     * @return
     */
    public static Boolean isNumber(String tg) {
        return tg == null ? false : pt.matcher(tg.trim()).matches();
    }

    /**
     * 判断集合是否不为空
     *
     * @param list
     * @return
     */
    public static <T> Boolean isNotEmpty(Collection<T> list) {
        return list != null && list.size() > 0;
    }

    /**
     * 判断对象数组是否不为空
     *
     * @param arr
     * @return
     */
    public static <T> Boolean isNotEmpty(T[] arr) {
        return arr != null && arr.length > 0;
    }

    /***
     * 自动属性赋值
     *
     * @param clazz
     * @param propertys
     * @return
     */
    public static <T> List<T> packageObject(Class<T> clazz, List<Object> vlist, String... propertys) {
        try {
            List<T> list = new ArrayList<T>();
            if (propertys.length == 1) {
                for (Object ov : vlist) {
                    Object obj = clazz.newInstance();
                    Field fd = clazz.getDeclaredField(propertys[0]);
                    fd.setAccessible(true);
                    if (ov != null) {

                        if (ov instanceof BigDecimal) {
                            ov = ((BigDecimal) ov).doubleValue();
                        } else if (ov instanceof BigInteger) {
                            ov = ((BigInteger) ov).longValue();
                        }

                        if (fd.getType().isEnum()) {
                            Class<Enum> cls = (Class<Enum>) fd.getType();
                            if (ov instanceof Number) {
                                Enum[] ccs = (Enum[]) fd.getType().getEnumConstants();
                                fd.set(obj, Enum.valueOf(cls, ccs[Number.class.cast(ov).intValue()].name()));
                            } else {
                                fd.set(obj, Enum.valueOf(cls, ov.toString()));
                            }

                        } else {
                            fd.set(obj, ov);
                        }
                    }
                    list.add((T) obj);

                }

            } else {
                for (Object o : vlist) {
                    Object[] ov = (Object[]) o;
                    Object obj = clazz.newInstance();
                    for (int i = 0; i < propertys.length; i++) {
                        Field fd = clazz.getDeclaredField(propertys[i]);
                        fd.setAccessible(true);
                        if (ov[i] != null) {
                            if (ov[i] instanceof BigDecimal) {
                                ov[i] = ((BigDecimal) ov[i]).doubleValue();
                            } else if (ov[i] instanceof BigInteger) {
                                ov[i] = ((BigInteger) ov[i]).longValue();
                            }

                            if (fd.getType().isEnum()) {
                                Class<Enum> cls = (Class<Enum>) fd.getType();

                                if (ov[i] instanceof Number) {

                                    Enum[] ccs = (Enum[]) fd.getType().getEnumConstants();
                                    fd.set(obj, Enum.valueOf(cls, ccs[Number.class.cast(ov[i]).intValue()].name()));
                                } else {
                                    fd.set(obj, Enum.valueOf(cls, ov[i].toString()));
                                }

                            } else {
                                fd.set(obj, ov[i]);
                            }
                        }
                    }
                    list.add((T) obj);
                }
            }
            return list;
        } catch (Exception e) {
            log.error("异常:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将对象转换成Map
     */
    public static Map describe(Object bean, String... args) throws BusinessException {
        Map description = new TreeMap();
        try {
            if (bean != null) {
                Class clazz = bean.getClass();
                Field[] fields = clazz.getDeclaredFields();
                Method[] methods = clazz.getDeclaredMethods();
                List<Method> methods2 = new ArrayList<Method>();
                if (args.length > 0) {
                    for (int i = 0; i < args.length; i++) {
                        for (Method method : methods) {
                            if (("get" + args[i].substring(0, 1).toUpperCase() + args[i].substring(1))
                                    .equals(method.getName())) {
                                methods2.add(method);
                                break;
                            }
                        }
                    }
                } else {
                    methods2 = Arrays.asList(methods);
                }
                for (Method method : methods2) {
                    String m_name = method.getName();
                    for (Field field : fields) {
                        if (m_name.equals(
                                "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1))) {
                            description.put(field.getName(), method.invoke(bean));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("对象转换成Map异常:", e);
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "对象转换成Map异常！");
        }
        return description;
    }

    /**
     * 将字符串用某字符链接起来，如 a=b&c=d
     */
    public static String join(String f, String g, String... args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(f);
            if (++i < args.length) {
                sb.append(args[i]).append(g);
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 把对象转换成URL想要的参数拼接 链接符号是& isEmpty 是否把空值拼接上 args 去掉不需要拼接的属性，如getName
     */
    public static <T> String obj2UrlParam(T obj, boolean isEmpty, String... args) throws BusinessException {
        StringBuilder and = new StringBuilder();
        try {
            Class clazz = obj.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (int i = 0; i < args.length; i++) {
                for (Method method : methods) {
                    String name = method.getName();
                    if (("get" + args[i].substring(0, 1).toUpperCase() + args[i].substring(1)).equals(name)) {
                        Object ob = method.invoke(obj);
                        if (!isEmpty) {
                            if (MyUtils.isNotEmpty(ob)) {
                                and.append(args[i]).append("=").append(ob).append("&");
                            }
                        } else {
                            and.append(args[i]).append("=").append(ob == null ? "" : ob).append("&");
                        }
                        break;
                    }
                }
            }
            if (and.length() > 1) {
                return and.substring(0, and.length() - 1);
            }
        } catch (Exception e) {
            log.error("对象转换成URL参数异常：", e);
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "对象转换成URL参数异常");
        }
        return and.toString();
    }

    /**
     * 无序的
     * @param isEmpty
     * @param map
     * @return
     * @throws BusinessException
     */
    public static <T> String obj2UrlParam(boolean isEmpty, Map map) throws BusinessException {
        StringBuilder and = new StringBuilder();

        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            if (!isEmpty) {
                if (MyUtils.isNotEmpty(entry.getValue())) {
                    and.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            } else {
                and.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        if (and.length() > 1) {
            return and.substring(0, and.length() - 1);
        }

        return and.toString();
    }
    
    /**
     * ASCII码递增排序（字母升序排序）
     * @param isEmpty
     * @param map
     * @return
     * @throws BusinessException
     */
    public static <T> String obj2UrlParamSorted(boolean isEmpty,Map paramMap) throws BusinessException {
    	StringBuilder and = new StringBuilder();
		SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
		for (Iterator iterator = smap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            if (!isEmpty) {
                if (MyUtils.isNotEmpty(entry.getValue())) {
                    and.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            } else {
                and.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
       if (and.length() > 1) {
            return and.substring(0, and.length() - 1);
        }

       return and.toString();
    }
    
    /**
     * ASCII码递增排序（字母升序排序），不要key
     * @param isEmpty
     * @param map
     * @return
     * @throws BusinessException
     */
    public static <T> String obj2UrlParamNoKey(boolean isEmpty,Map paramMap) throws BusinessException {
    	StringBuilder and = new StringBuilder();
		for (Iterator iterator = paramMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            if (!isEmpty) {
                if (MyUtils.isNotEmpty(entry.getValue())) {
                    and.append(entry.getValue());
                }
            } else {
                and.append(entry.getValue());
            }
        }
       return and.toString();
    }


    public static String randomByLength(Integer length) {
        return RandomStringUtils.random(length,
                new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't',
                        'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
    }

    public static String matcher(String pattern, String verifyData) {

        verifyData = verifyData.replaceAll("[\r\n]", "");

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(verifyData);
        String result = "";
        while (m.find()) {
            result += m.group(1) + ",";
        }
        if (result.length() > 1) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static String getSign(Map<String, String> map, String key) {
        map.remove("sign");
        map = sortMapByKey(map);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        String signStr = sb.append("key=" + key).toString();
        try {
            return DigestUtils.signByMD5(signStr);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            System.out.println(DigestUtils.signByMD5(
                    "merchant_code=11399527&order_amount=1.00&order_no=qle_ht_felix518_4000049&order_time=2017-09-05 11:30:53&return_params=felix518&trade_no=1095270905930671&trade_status=success&trade_time=2017-09-05 11:30:53&key=e6e9aa5cbb0e716a4080cad58c0cbafb"));

        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());

        sortMap.putAll(map);
        return sortMap;
    }

}
