package com.nnti.common.constants;

import java.util.HashMap;
import java.util.Map;

public final class StaticData {

	// 户内转账
	public static Map<String, HashMap<String, String>> transferMap = new HashMap<String, HashMap<String, String>>();
	// 优惠
	public static Map<String, HashMap<String, String>> preferentialMap = new HashMap<String, HashMap<String, String>>();
	// 额度类型
	public static Map<String, String> creditMap = new HashMap<String, String>();

	static {

		/******************************户内转账配置开始处******************************/

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("beanId", "dtTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.DT);
		map.put("type", "IN");
		// 主账户转DT
		transferMap.put("SELFDT", map);

		map = new HashMap<String, String>();
		map.put("beanId", "dtTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.DT);
		map.put("type", "OUT");
		// DT转主账户
		transferMap.put("DTSELF", map);
		
		map.put("beanId", "cq9TransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.CQ9);
		map.put("type", "IN");
		// 主账户转CQ9
		transferMap.put("SELFCQ9", map);

		map = new HashMap<String, String>();
		map.put("beanId", "cq9TransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.CQ9);
		map.put("type", "OUT");
		//CQ9转主账户
		transferMap.put("CQ9SELF", map);
		
		map.put("beanId", "pgTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.PG);
		map.put("type", "IN");
		// 主账户转PG
		transferMap.put("SELFPG", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "pgTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.PG);
		map.put("type", "OUT");
		//PG转主账户
		transferMap.put("PGSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntwoLiveTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.N2LIVE);
		map.put("type", "IN");
		// 主账户转N2LIVE
		transferMap.put("SELFN2LIVE", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntwoLiveTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.N2LIVE);
		map.put("type", "OUT");
		// N2LIVE转主账户
		transferMap.put("N2LIVESELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "mgTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.MG);
		map.put("type", "IN");
		// 主账户转MG
		transferMap.put("SELFMG", map);

		map = new HashMap<String, String>();
		map.put("beanId", "mgTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.MG);
		map.put("type", "OUT");
		// MG转主账户
		transferMap.put("MGSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "qtTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.QT);
		map.put("type", "IN");
		// 主账户转QT
		transferMap.put("SELFQT", map);

		map = new HashMap<String, String>();
		map.put("beanId", "qtTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.QT);
		map.put("type", "OUT");
		// QT转主账户
		transferMap.put("QTSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ttgTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.TTG);
		map.put("type", "IN");
		// 主账户转TTG
		transferMap.put("SELFTTG", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ttgTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.TTG);
		map.put("type", "OUT");
		// TTG转主账户
		transferMap.put("TTGSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ptTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.PT);
		map.put("type", "IN");
		// 主账户转PT
		transferMap.put("SELFNEWPT", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ptTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.PT);
		map.put("type", "OUT");
		// PT转主账户
		transferMap.put("NEWPTSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.NT);
		map.put("type", "IN");
		// 主账户转NT
		transferMap.put("SELFNT", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.NT);
		map.put("type", "OUT");
		// NT转主账户
		transferMap.put("NTSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "aginTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.AGIN);
		map.put("type", "IN");
		// 主账户转AGIN
		transferMap.put("SELFAGIN", map);

		map = new HashMap<String, String>();
		map.put("beanId", "aginTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.AGIN);
		map.put("type", "OUT");
		// AGIN转主账户
		transferMap.put("AGINSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "pngTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.PNG);
		map.put("type", "IN");
		// 主账户转PNG
		transferMap.put("SELFPNG", map);

		map = new HashMap<String, String>();
		map.put("beanId", "pngTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.PNG);
		map.put("type", "OUT");
		// PNG转主账户
		transferMap.put("PNGSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "shabaTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.SBA);
		map.put("type", "IN");
		// 主账户转SBA
		transferMap.put("SELFSBA", map);

		map = new HashMap<String, String>();
		map.put("beanId", "shabaTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.SBA);
		map.put("type", "OUT");
		// SBA转主账户
		transferMap.put("SBASELF", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "chessTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.CHESS);
		map.put("type", "IN");
		// 主账户转CHESS
		transferMap.put("SELFCHESS", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "chessTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.CHESS);
		map.put("type", "OUT");
		// CHESS转主账户
		transferMap.put("CHESSSELF", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "swTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.SW);
		map.put("type", "IN");
		// 主账户转SW
		transferMap.put("SELFSW", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "swTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.SW);
		map.put("type", "OUT");
		// SW转主账户
		transferMap.put("SWSELF", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "bbinTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.BBIN);
		map.put("type", "IN");
		// 主账户转BBIN
		transferMap.put("SELFBBIN", map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "bbinTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.BBIN);
		map.put("type", "OUT");
		//BBIN转主账户
		transferMap.put("BBINSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "mwgTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.MWG);
		map.put("type", "IN");
		// 主账户转MWG
		transferMap.put("SELFMWG", map);

		map = new HashMap<String, String>();
		map.put("beanId", "mwgTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.MWG);
		map.put("type", "OUT");
		// MWG转主账户
		transferMap.put("MWGSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ogTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.OG);
		map.put("type", "IN");
		// 主账户转OG
		transferMap.put("SELFOG", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ogTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.OG);
		map.put("type", "OUT");
		// OG转主账户
		transferMap.put("OGSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "eaTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.EA);
		map.put("type", "IN");
		// 主账户转EA
		transferMap.put("SELFEA", map);

		map = new HashMap<String, String>();
		map.put("beanId", "eaTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.EA);
		map.put("type", "OUT");
		// EA转主账户
		transferMap.put("EASELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ebetTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.EBETAPP);
		map.put("type", "IN");
		// 主账户转EBET
		transferMap.put("SELFEBETAPP", map);

		map = new HashMap<String, String>();
		map.put("beanId", "ebetTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.EBETAPP);
		map.put("type", "OUT");
		// EBET转主账户
		transferMap.put("EBETAPPSELF", map);

		map = new HashMap<String, String>();
		map.put("beanId", "slotTransferService");
		map.put("method", "transferIn");
		map.put("platform", Constant.SLOT);
		map.put("type", "IN");
	
		// 主账户转老虎机钱包
		transferMap.put("SELFSLOT", map);

		map = new HashMap<String, String>();
		map.put("beanId", "slotTransferService");
		map.put("method", "transferOut");
		map.put("platform", Constant.SLOT);
		map.put("type", "OUT");
		// 老虎机钱包转主账户
		transferMap.put("SLOTSELF", map);

		/******************************户内转账配置结束处******************************/

		/******************************优惠活动配置开始处******************************/

		map = new HashMap<String, String>();
		map.put("beanId", "dtTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// DT
		preferentialMap.put(Constant.DT, map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "cq9TransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// CQ9
		preferentialMap.put(Constant.CQ9, map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "pgTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// PG
		preferentialMap.put(Constant.PG, map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntwoLiveTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// N2LIVE
		preferentialMap.put(Constant.N2LIVE, map);

		map = new HashMap<String, String>();
		map.put("beanId", "mgTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// MG
		preferentialMap.put(Constant.MG, map);

		map = new HashMap<String, String>();
		map.put("beanId", "qtTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// QT
		preferentialMap.put(Constant.QT, map);

		map = new HashMap<String, String>();
		map.put("beanId", "ttgTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// TTG
		preferentialMap.put(Constant.TTG, map);

		map = new HashMap<String, String>();
		map.put("beanId", "ptTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// PT
		preferentialMap.put(Constant.PT, map);

		map = new HashMap<String, String>();
		map.put("beanId", "ntTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// NT
		preferentialMap.put(Constant.NT, map);

		map = new HashMap<String, String>();
		map.put("beanId", "aginTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// AGIN
		preferentialMap.put(Constant.AGIN, map);

		map = new HashMap<String, String>();
		map.put("beanId", "pngTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// PNG
		preferentialMap.put(Constant.PNG, map);

		map = new HashMap<String, String>();
		map.put("beanId", "slotTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// SLOT
		preferentialMap.put(Constant.SLOT, map);
		
		map = new HashMap<String, String>();
		map.put("beanId", "swTransferService");
		map.put("redEnvelopeMethod", "redEnvelopeCoupon");
		map.put("depositMethod", "depositCoupon");
		map.put("experienceMethod", "experienceGold");
		map.put("christmasActivitiesMethd", "christmasActivities");
		// SW
		preferentialMap.put(Constant.SW, map);

		/******************************优惠活动配置结束处******************************/

		/******************************额度类型配置开始处******************************/

		// DT
		creditMap.put("SELFDT", CreditChangeType.TRANSFER_DT_IN);
		// CQ9
		creditMap.put("SELFCQ9", CreditChangeType.TRANSFER_CQ9_IN);
		// PG
		creditMap.put("SELFPG", CreditChangeType.TRANSFER_PG_IN);
		// N2LIVE
		creditMap.put("SELFN2LIVE", CreditChangeType.TRANSFER_N2LIVE_IN);
		// MG
		creditMap.put("SELFMG", CreditChangeType.TRANSFER_MG_IN);
		// SW
		creditMap.put("SELFSW", CreditChangeType.TRANSFER_SW_IN);
		// QT
		creditMap.put("SELFQT", CreditChangeType.TRANSFER_QT_IN);
		// TTG
		creditMap.put("SELFTTG", CreditChangeType.TRANSFER_TTG_IN);
		// PT
		creditMap.put("SELFNEWPT", CreditChangeType.TRANSFER_PT_IN);
		// NT
		creditMap.put("SELFNT", CreditChangeType.TRANSFER_NT_IN);
		// AGIN
		creditMap.put("SELFAGIN", CreditChangeType.TRANSFER_AGIN_IN);
		// PNG
		creditMap.put("SELFPNG", CreditChangeType.TRANSFER_PNG_IN);
		// SBA
		creditMap.put("SELFSBA", CreditChangeType.TRANSFER_SBA_IN);
		// CHESS
		creditMap.put("SELFCHESS", CreditChangeType.TRANSFER_CHESS_IN);
		// BBIN
		creditMap.put("SELFBBIN", CreditChangeType.TRANSFER_BBIN_IN);
		// EA
		creditMap.put("SELFEA", CreditChangeType.TRANSFER_IN);
		// EBETAPP
		creditMap.put("SELFEBETAPP", CreditChangeType.TRANSFER_EBETAPP_IN);
		// MWG
		creditMap.put("SELFMWG", CreditChangeType.TRANSFER_MWG_IN);
		// OG
		creditMap.put("SELFOG", CreditChangeType.TRANSFER_OG_IN);
		// 老虎机钱包
		creditMap.put("SELFSLOT", CreditChangeType.TRANSFER_SLOT_IN);

		/******************************额度类型配置结束处******************************/
	}
}