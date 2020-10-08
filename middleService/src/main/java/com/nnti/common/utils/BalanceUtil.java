package com.nnti.common.utils;

import org.apache.commons.lang3.StringUtils;
import com.nnti.common.constants.Constant;

public class BalanceUtil {

	public static String transferInAccount(String product, String platform, String loginName, String password,
			Double giftMoney, String referenceId) {

		String msg = "";

		if (Constant.PT.equalsIgnoreCase(platform)) {

			Boolean flag = PTUtil.getDepositPlayerMoney(product, loginName, giftMoney);

			if (!flag) {

				msg = "转入" + Constant.PT + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.QT.equalsIgnoreCase(platform)) {

			String result = QTUtil.getDepositPlayerMoney(product, loginName, giftMoney, referenceId);

			if (!QTUtil.RESULT_SUCC.equals(result)) {

				msg = "转入" + Constant.QT + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.NT.equalsIgnoreCase(platform)) {

			String result = NTUtil.changeMoney(product, loginName, giftMoney);

			if (StringUtils.isNotBlank(result)) {

				msg = "转入" + Constant.NT + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.MG.equalsIgnoreCase(platform)) {

			String result = MGSUtil.transferInAndOutMG(product, loginName, password, giftMoney, referenceId, "IN");

			if (StringUtils.isNotBlank(result)) {

				msg = "转入" + Constant.MG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.CQ9.equalsIgnoreCase(platform)) {

			String result = CQ9Util.transferIn(loginName, password, giftMoney);

			if (StringUtils.isNotBlank(result)) {

				msg = "转入" + Constant.CQ9 + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.PG.equalsIgnoreCase(platform)) {

			String result = PGUtil.transferIn(loginName, giftMoney);

			if (StringUtils.isEmpty(result)) {

				msg = "转入" + Constant.PG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.BG.equalsIgnoreCase(platform)) {

			String result = BGUtil.transfer(loginName, Math.abs(giftMoney));

			if (StringUtils.isEmpty(result)) {

				msg = "转入" + Constant.BG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.SW.equalsIgnoreCase(platform)) {

			Boolean b = SWUtil.transferToSW(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入" + Constant.SW + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.DT.equalsIgnoreCase(platform)) {

			String result = DTUtil.withdrawOrDeposit(product, loginName, giftMoney);

			if (StringUtils.isBlank(result) || !"success".equalsIgnoreCase(result)) {

				msg = "转入" + Constant.DT + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.TTG.equalsIgnoreCase(platform)) {

			Boolean b = TTGUtil.addPlayerAccountPraper(product, loginName, giftMoney);

			if (!b) {

				msg = "转入" + Constant.TTG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.PNG.equalsIgnoreCase(platform)) {

			String result = PNGUtil.transferToPNG(product, loginName, giftMoney);

			if (!"success".equals(result)) {

				msg = "转入" + Constant.PNG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.N2LIVE.equalsIgnoreCase(platform)) {

			Boolean b = NTwoUtil.getDepositPlayerMoney(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入" + Constant.N2LIVE + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.AGIN.equalsIgnoreCase(platform)) {

			Boolean b = AGINUtil.transferToAG(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入" + Constant.AGIN + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.SBA.equalsIgnoreCase(platform)) {

			Boolean b;
			try {
				b = SbSportUtil.transfer(loginName, giftMoney, referenceId);
				if (!b) {
					msg = "转入" + Constant.SBA + "账户出现错误，请稍后重试！";
				}
			} catch (Exception e) {
				msg = "转入" + Constant.SBA + "账户出现错误，请稍后重试！";
			}

		} else if (Constant.MWG.equalsIgnoreCase(platform)) {

			Boolean b = MWGUtils.transferInAccount(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入" + Constant.MWG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.OG.equalsIgnoreCase(platform)) {

			Boolean b = OGUtils.transferOpt(product, referenceId, loginName, giftMoney);

			if (!b) {

				msg = "转入" + Constant.OG + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.EA.equalsIgnoreCase(platform)) {

			Boolean b = EAUtil.getDepositPlayerMoney(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入" + Constant.EA + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.EBETAPP.equalsIgnoreCase(platform)) {

			Boolean b = EBETUtil.transferInAccount(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入" + Constant.EBETAPP + "账户出现错误，请稍后重试！";
			}
		} else if (Constant.SLOT.equalsIgnoreCase(platform)) {

			String result = SlotUtil.transferToSlot(product, loginName, giftMoney);

			if (!SlotUtil.RESULT_SUCC.equals(result)) {

				msg = "转入老虎机钱包账户出现错误，请稍后重试！";
			}
		} else if (Constant.CHESS.equalsIgnoreCase(platform)) {

			Boolean b = ChessUtil.transferToChess(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入棋乐游账户出现错误，请稍后重试！";
			}
		} else if (Constant.BBIN.equalsIgnoreCase(platform)) {

			Boolean b = BBinUtils.transferToBbin(loginName, giftMoney, referenceId);

			if (!b) {

				msg = "转入BBIN账户出现错误，请稍后重试！";
			}
		}

		return msg;
	}

	public static String transferOutAccount(String product, String platform, String loginName, String password,
			Double giftMoney, String referenceId) {

		String msg = "";

		if (Constant.PT.equalsIgnoreCase(platform)) {

			Boolean flag = PTUtil.getWithdrawPlayerMoney(product, loginName, giftMoney);

			if (!flag) {

				msg = Constant.PT + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.QT.equalsIgnoreCase(platform)) {

			String result = QTUtil.getWithdrawPlayerMoney(product, loginName, giftMoney, referenceId);

			if (!QTUtil.RESULT_SUCC.equals(result)) {

				msg = Constant.QT + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.NT.equalsIgnoreCase(platform)) {

			String result = NTUtil.changeMoney(product, loginName, giftMoney * -1);

			if (StringUtils.isNotBlank(result)) {

				msg = Constant.NT + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.MG.equalsIgnoreCase(platform)) {

			String result = MGSUtil.transferInAndOutMG(product, loginName, password, giftMoney, referenceId, "OUT");

			if (StringUtils.isNotBlank(result)) {

				msg = Constant.MG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.SW.equalsIgnoreCase(platform)) {

			Boolean b = SWUtil.tranferFromSW(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = Constant.SW + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.DT.equalsIgnoreCase(platform)) {

			String result = DTUtil.withdrawOrDeposit(product, loginName, giftMoney * -1);

			if (StringUtils.isBlank(result) || !result.contains("success")) {

				msg = Constant.DT + "账户转出出现错误，请稍后重试！";
			} else {

				msg = result;
			}
		} else if (Constant.CQ9.equalsIgnoreCase(platform)) {

			String result = CQ9Util.transferOut(loginName, password, giftMoney);

			if (StringUtils.isNotBlank(result)) {

				msg = Constant.CQ9 + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.PG.equalsIgnoreCase(platform)) {

			String result = PGUtil.transferOut(loginName, giftMoney);

			if (StringUtils.isEmpty(result)) {

				msg = Constant.PG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.BG.equalsIgnoreCase(platform)) {

			String result = BGUtil.transfer(loginName, -Math.abs(giftMoney));

			if (StringUtils.isEmpty(result)) {

				msg = Constant.PG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.TTG.equalsIgnoreCase(platform)) {

			Boolean b = TTGUtil.addPlayerAccountPraper(product, loginName, giftMoney * -1);

			if (!b) {

				msg = Constant.TTG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.PNG.equalsIgnoreCase(platform)) {

			String result = PNGUtil.tranferFromPNG(product, loginName, giftMoney);

			if (!"success".equals(result)) {

				msg = Constant.PNG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.N2LIVE.equalsIgnoreCase(platform)) {

			Boolean b = NTwoUtil.getWithdrawPlayerMoney(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = Constant.N2LIVE + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.AGIN.equalsIgnoreCase(platform)) {

			Boolean b = AGINUtil.transferFromAG(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = Constant.AGIN + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.SBA.equalsIgnoreCase(platform)) {

			Boolean b;
			try {
				b = SbSportUtil.transfer(loginName, -giftMoney, referenceId);
				if (!b) {
					msg = Constant.SBA + "账户转出出现错误，请稍后重试！";
				}
			} catch (Exception e) {
				msg = Constant.SBA + "账户转出出现错误，请稍后重试！";
			}

		} else if (Constant.MWG.equalsIgnoreCase(platform)) {

			Boolean b = MWGUtils.transferOutAccount(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = Constant.MWG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.OG.equalsIgnoreCase(platform)) {

			Boolean b = OGUtils.transferOpt(product, referenceId, loginName, giftMoney * -1);

			if (!b) {

				msg = Constant.OG + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.EA.equalsIgnoreCase(platform)) {

			Boolean b = EAUtil.getWithdrawPlayerMoney(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = Constant.EA + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.EBETAPP.equalsIgnoreCase(platform)) {

			Boolean b = EBETUtil.transferOutAccount(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = Constant.EBETAPP + "账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.SLOT.equalsIgnoreCase(platform)) {

			String result = SlotUtil.transferFromSlot(product, loginName, giftMoney);

			if (!SlotUtil.RESULT_SUCC.equals(result)) {

				msg = "老虎机钱包账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.CHESS.equalsIgnoreCase(platform)) {

			Boolean b = ChessUtil.transferFromChess(product, loginName, giftMoney, referenceId);

			if (!b) {

				msg = "棋乐游账户转出出现错误，请稍后重试！";
			}
		} else if (Constant.BBIN.equalsIgnoreCase(platform)) {

			Boolean b = BBinUtils.transferFromBbin(loginName, giftMoney, referenceId);

			if (!b) {

				msg = "BBIN账户转出出现错误，请稍后重试！";
			}
		}

		return msg;
	}
}