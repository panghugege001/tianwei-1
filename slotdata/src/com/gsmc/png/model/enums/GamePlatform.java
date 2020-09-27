package com.gsmc.png.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum GamePlatform {
	
	PT("PT老虎机",GamePlatform.SLOT),     
	PTOTHER("PT非老虎机",GamePlatform.SLOT),
	QT("QT老虎机",GamePlatform.SLOT),
	DT("DT老虎机",GamePlatform.SLOT),
	SW("SW老虎机",GamePlatform.SLOT),
	TTG("TTG老虎机",GamePlatform.SLOT),
	NT("NT老虎机",GamePlatform.SLOT),
	MG("MG老虎机",GamePlatform.SLOT),
	CQ9("CQ9老虎机",GamePlatform.SLOT),
	PG("PG老虎机",GamePlatform.SLOT),
	AGIN("AG游戏",GamePlatform.SLOT),
	AGIN_LIVE("AGIN真人游戏",GamePlatform.LIVE),
	AGINfish("AG捕鱼",GamePlatform.LIVE),
	DG("DG彩票",GamePlatform.LIVE),
	PNG("PNG老虎机", GamePlatform.SLOT),
	EBET_LIVE("EBET真人游戏",GamePlatform.LIVE),
	SB("SB沙巴体育", GamePlatform.SPORT),
	BBIN("BBIN老虎机", GamePlatform.SLOT),//bbin（真人娱乐，电子游戏，体育竞技，彩票，捕鱼大师，捕鱼达人）
	BBIN_SPORT("BBIN体育", GamePlatform.SPORT),//bbin（真人娱乐，电子游戏，体育竞技，彩票，捕鱼大师，捕鱼达人）
	BBIN_LOTTERY("BBIN彩票", GamePlatform.LIVE),//bbin（真人娱乐，电子游戏，体育竞技，彩票，捕鱼大师，捕鱼达人）
	BBIN_LIVE("BBIN真人", GamePlatform.LIVE),//bbin（真人娱乐，电子游戏，体育竞技，彩票，捕鱼大师，捕鱼达人）
	;

	public static final int SLOT = 1;
	public static final int LIVE = 2;
	public static final int SPORT = 3;//体育
	public static final int EXCLUDE = -1;
	public static final int ALL = 0;
	private String desc;
	private int type;
	
	private GamePlatform(String desc,int type){
		this.desc = desc;
		this.type = type;
	}

	public static GamePlatform getByCode(String code) {
		GamePlatform[] enums = GamePlatform.values();
		for(GamePlatform platform : enums){
			if(platform.name().equalsIgnoreCase(code)){
				return platform;
			}
		}
		throw new IllegalArgumentException("不存在的平台代码");
	}

    public String getDesc() {
		return desc;
	}
    public int getType() {
		return type;
	}
	public static String[] getAllPlatforms(){
		GamePlatform[] enums = GamePlatform.values();
		String[] platforms = new String[enums.length];
		for(int i=0; i<enums.length; i++){
			platforms[i] = enums[i].name();
		}
		return platforms;
	}
	
	public static List<String> getLiveGamePlatform(){
		return getGamePlatforms(GamePlatform.LIVE);
	}
	
	public static List<String> getSlotGamePlatform(){
		return getGamePlatforms(GamePlatform.SLOT);
	}
	
	public static List<String> getExcludeGamePlatform(){
		return getGamePlatforms(GamePlatform.EXCLUDE);
	}
	
	
	private static List<String> getGamePlatforms(int platformType){
		List<String> platforms = new ArrayList<String>();
		GamePlatform[] enums = GamePlatform.values();
		for(int i=0; i<enums.length; i++){
			if(enums[i].getType()==platformType){
				platforms.add(enums[i].name());
			}
		}
		return platforms;
	}
	
	
	public static String getPlatformsForSql(int platformType){
		GamePlatform[] enums = GamePlatform.values();
		String platforms = "";
		for(int i=0; i<enums.length; i++){
			String platformName = enums[i].name();
			if("AGIN".equals(platformName))platformName+="fish";
			if(platformType==GamePlatform.ALL||enums[i].getType()==platformType){
				if(platforms.length() == 0){
					platforms += "'" + platformName + "'";
				}else{
					platforms += ",'" + platformName + "'";
				}
			}
		}
		return platforms;
	}
	
	public static String getAllPlatformsForSql(){
		GamePlatform[] enums = GamePlatform.values();
		String platforms = "";
		for(int i=0; i<enums.length; i++){
			if(i == 0){
				platforms += "'" + enums[i].name() + "'";
			}else{
				platforms += ",'" + enums[i].name() + "'";
			}
		}
		return platforms;
	}
	
	/**
	 * for in query
	 * @return
	 */
	public static String getAllUsedInCondition(){
		StringBuilder builder = new StringBuilder();
		for(GamePlatform pf : GamePlatform.values()){
			if( !pf.equals(GamePlatform.PTOTHER) ){
				if(builder.length() == 0){
					builder.append("'").append(pf.name()).append("'");
				}else{
					builder.append(",'").append(pf.name()).append("'");
				}
			}
		}
		return builder.toString();
	}
	
	/**
	 * for in query
	 * @return
	 */
	public static String getAllExceptPTUsedInCondition(){
		StringBuilder builder = new StringBuilder();
		for(GamePlatform pf : GamePlatform.values()){
			if( !pf.equals(GamePlatform.PTOTHER) && !pf.equals(GamePlatform.PT)){
				if(builder.length() == 0){
					builder.append("'").append(pf.name()).append("'");
				}else{
					builder.append(",'").append(pf.name()).append("'");
				}
			}
		}
		return builder.toString();
	}
	
	public static void main(String[] args){
//		List<String> list = getSlotGamePlatform();
//		for(String a:list){
//			System.out.println(a);
//		}
		System.out.println(getPlatformsForSql(GamePlatform.LIVE));
		System.out.println(getPlatformsForSql(GamePlatform.SLOT));
		System.out.println(getPlatformsForSql(GamePlatform.EXCLUDE));
		System.out.println(getPlatformsForSql(GamePlatform.ALL));
		System.out.println(getExcludeGamePlatform().size());
//		System.out.println(getAllUsedInCondition());
//		System.out.println(getAllExceptPTUsedInCondition());
	}
}
