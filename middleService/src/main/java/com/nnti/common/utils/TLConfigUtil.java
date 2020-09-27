package com.nnti.common.utils;

import java.util.HashMap;
import java.util.Map;

public class TLConfigUtil {

	private static Map<String, HashMap<String, String>> configMap = new HashMap<String, HashMap<String, String>>();

	static {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("apikey", "bb84b96421104dc696e16165e26c92bc");
		map.put("secretkey", "utmpq09k04iamk1gmuo1jcfkxsttargk41xzrioar1ahouhuj13f260ep6qinl2a");
		// 通联测试配置
		configMap.put("text", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "c7db258b80a84a73986f93ef5e8f7c15");
		map.put("secretkey", "7zeaszaral103zsz26ukj1wn5kr4gwo522wbj3894x1jt82mnka27ihc82093flb");
		// 通联亚虎配置
		configMap.put("dy", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "e928360154e9400697c43a5eeaac842a");
		map.put("secretkey", "e4tc4q9p0mfb3jrol9teodqym1lo19ufhnf0bereofsg6zg4tju1b251evq25lnr");
		// 通联千亿配置
		configMap.put("qy", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "44e5f377fbea41fcafe34baf3e70b6fe");
		map.put("secretkey", "gisz5mapihl2qt6pnw6lvk10yanjerg615lyjir7xu32fzt2d5pj9bfvhr0uyqbq");
		// 通联龙八配置
		configMap.put("yl", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "7bb3af13dcf5498ab2c9ec46ab1a6160");
		map.put("secretkey", "8njdi35gpovtsb2if15iemwddu1m05qw5uxbaoyoso7oxftcqlrs39vlr6zo1igj");
		// 通联e68配置
		configMap.put("lh", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "4c13b92da14c4ead9aabdd27ee6a55f0");
		map.put("secretkey", "4nfz7yrzsye3h5z4kll3ntrb1rn5pu0dz9v2d9ubuexmwelmduqggp9bv4yfalay");
		// 通联优发配置
		configMap.put("ufa", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "d87e028322aa4594bb802f507956a64d");
		map.put("secretkey", "ymiv3277ef8h3p6tjp0vep2cetz8hbjknfvir5fvke0vbdhlphd80dh8k6h92zp2");
		// 通联ule配置
		configMap.put("ul", map);
		
		
		map = new HashMap<String, String>();
		map.put("apikey", "302012e44c0940daa32f23d4371a7df6");
		map.put("secretkey", "sk2optq9u05rwt210gvhuybkf44uto9p5m1pmzfjl93vqcqqnpdmo0zu8i8kowf6");
		// 通联齐乐配置
		configMap.put("ql", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "4d8f5a8e50bb47d0ba5c4bf4d03ca8a4");
		map.put("secretkey", "0hkpz794ytlhylrsp9l7wpza4ujk3l5a0wqueskclvtrz5i19lwp0z8ahal4op0c");
		// 通联梦之城配置
		configMap.put("mzc", map);
		
		
		map = new HashMap<String, String>();
		map.put("apikey", "f67d4d659edb4d44947207ea938f0fde");
		map.put("secretkey", "9qaveah84ljeqzyjfeea95f8rt0wdlp9arr4cottba1gpp8t2c0j78a3a24k9lvi");
		// 通联武松配置
		configMap.put("ws", map);
		
		
		map = new HashMap<String, String>();
		map.put("apikey", "63d930091d084ff2ba7cb02b45f65687");
		map.put("secretkey", "1gm5egzzbhgzq26mw4ij4t3xn77olmjbe5hw2j62ram5sv8kzbanjwg7dph68aeo");
		// 通联尊宝配置
		configMap.put("zb", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "e8807ff1b1d44713a7beeb8822e0e802");
		map.put("secretkey", "8rpj8eqiwslzryu7qx14bqcdj6j9cp393htd6ytzu17bxy44o290ldtiec02vh57");
		// 通联龙都配置
		configMap.put("ld", map);
		
		map = new HashMap<String, String>();
		map.put("apikey", "314825e9317d400f9f6952694a27613c");
		map.put("secretkey", "zsz3c5s2izermxkuj5ntp46lr7bp6g1da75pv6dw0yoxxvj83u5k2gvulec4t3v1");
		// 通联龙虎配置
		configMap.put("loh", map);
		
		
	}

	public static Map<String, String> get(String prodcut) {

		return configMap.get(prodcut);
	}
}