package com.nnti.common.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * 会员类型(等级)
 * 
 * @author
 *
 */
public class VipLevel {
	
	private static Map<String , HashMap<String, String >> classmap = new HashMap<String ,HashMap<String , String>>();
	static{
		HashMap<String , String > map = new HashMap<String,String>();
		HashMap<String , String > map1 = new HashMap<String,String>();
		
		map = new HashMap<String ,String>();
		map.put("0", "新会员");
		map.put("1", "白金VIP");
		map.put("2", "黄金VIP");
		map.put("3", "钻石VIP");
		map.put("4", "荣耀VIP");
		map.put("5", "至尊VIP");
		map.put("6", "特邀VIP");
		//大运会员等级
		classmap.put("dy", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "天兵");
		map.put("1", "天将");
		map.put("2", "天王");
		map.put("3", "星君");
		map.put("4", "真君");
		map.put("5", "仙君");
		map.put("6", "帝君");						
		map.put("7", "天尊");						
		map.put("8", "天帝");						
		//龙都会员等级
		classmap.put("tw", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "士兵");
		map.put("1", "少尉");
		map.put("2", "中尉");
		map.put("3", "上尉");
		map.put("4", "少校");
		map.put("5", "中校");
		map.put("6", "上校");
		map.put("7", "少将");
		map.put("8", "中将");
		map.put("9", "上将");
		map.put("10", "三星上将");
		map.put("11", "五星上将");
		map.put("12", "统帅");
		//优发会员等级
		classmap.put("ufa", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "平民百姓");
		map.put("1", "秀才及第");
		map.put("2", "举人老爷");
		map.put("3", "举人老爷");
		map.put("4", "七品知县");
		map.put("5", "六品知州");
		map.put("6", "五品知府");
		map.put("7", "四品道台");
		map.put("8", "三品藩台");
		map.put("9", "二品抚台");
		map.put("10", "一品制台");
		map.put("11", "亲贵王公");
		map.put("12", "万乘之尊");
		//优乐会员等级
		classmap.put("ul", map);
		
		
		map = new HashMap<String ,String>();
		map.put("0", "新会员");
		map.put("1", "玄铁VIP");
		map.put("2", "青铜VIP");
		map.put("3", "白银VIP");
		map.put("4", "黄金VIP");
		map.put("5", "白金VIP");
		map.put("6", "钻石VIP");		
		//齐发会员等级
		classmap.put("ql", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "新会员");
		map.put("1", "忠实会员");
		map.put("2", "铜牌VIP");
		map.put("3", "银牌VIP");
		map.put("4", "金牌VIP");
		map.put("5", "桂冠VIP");
		map.put("6", "特邀VIP");		
		//梦之城会员等级
		classmap.put("mzc", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "关内侯");
		map.put("1", "乡侯");
		map.put("2", "县侯");
		map.put("3", "男爵");
		map.put("4", "子爵");
		map.put("5", "伯爵");
		map.put("6", "侯爵");		
		map.put("7", "公爵");		
		map.put("8", "王爵");				
		//武松会员等级
		classmap.put("ws", map);
		
		
		map = new HashMap<String ,String>();
		map.put("0", "普通会员");
		map.put("1", "青铜VIP");
		map.put("2", "白银VIP");
		map.put("3", "黄金VIP");
		map.put("4", "白金VIP");
		map.put("5", "钻石VIP");
		map.put("6", "王者VIP");						
		//尊宝会员等级
		classmap.put("zb", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "普通会员");
		map.put("1", "青铜VIP");
		map.put("2", "白银VIP");
		map.put("3", "黄金VIP");
		map.put("4", "白金VIP");
		map.put("5", "钻石VIP");
		map.put("6", "王者VIP");						
		//尊宝会员等级
		classmap.put("zb", map);
		
		map = new HashMap<String ,String>();
		map.put("0", "初入江湖");
		map.put("1", "武林新秀");
		map.put("2", "江湖少侠");
		map.put("3", "武林豪杰");
		map.put("4", "一派掌门");
		map.put("5", "一代宗师");
		map.put("6", "武林至尊");
		//龙虎会员等级
		classmap.put("loh", map);
		
		map1 = new HashMap<String ,String>();
		map1.put("code", "1");
		//限定会员等级提款
		classmap.put("levelCode", map1);
	}
	
	
	
	public static Map<String, HashMap<String, String>> getClassmap() {
		return classmap;
	}
	public static void setClassmap(Map<String, HashMap<String, String>> classmap) {
		VipLevel.classmap = classmap;
	}
	
	
	
	
	
	
}