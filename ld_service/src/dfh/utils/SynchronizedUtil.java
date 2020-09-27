package dfh.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dfh.model.*;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.enums.Goddesses;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.UserRole;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.AnnouncementService;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ISelfYouHuiService;
import dfh.service.interfaces.IUserLotteryService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.TransferService;
import net.sf.json.JSONObject;

/**
 * 处理同步工具类 每处理一个同步功能，需要一个Obj对象，一个带同步块的方法，同步块获取obj对象的锁
 * 
 */
public class SynchronizedUtil {

	private static Logger log = Logger.getLogger(SynchronizedUtil.class);
	private static final SynchronizedUtil instance = new SynchronizedUtil();

	/**
	 * 防止外部实例化
	 */
	private SynchronizedUtil() {
	};

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static SynchronizedUtil getInstance() {
		return instance;
	}

	/**
	 * IPS
	 */
	private Object IPSPayOrder = new Object();

	public String addIPSPayorder(AnnouncementService service, String billno, Double money, String loginname, String msg,
			String date, String type) {
		try {
			synchronized (IPSPayOrder) {
				return service.addPayorder(billno, money, loginname, msg, date, type);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	private Object transferInforHBO = new Object();

	public String transferInforHB(ISelfYouHuiService selfYouHuiService, String loginname, Double remit, String type,
			String betMultiples, Integer isdeposit) throws Exception {
		Users user = (Users) selfYouHuiService.get(Users.class, loginname);
		String seqid = DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
		synchronized (transferInforHBO) {
			if (type.equals("ttg")) {
				String str = selfYouHuiService.selfTransferTtgHB(loginname, remit, betMultiples, isdeposit);
				if (null == str) {
					Boolean b = PtUtil1.addPlayerAccountPraper(loginname, remit.intValue());
					if (null != b && b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("pt")) {
				String str = selfYouHuiService.selfTransferPtHB(loginname, remit, betMultiples, isdeposit);
				if (null == str) {
					Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
					if (deposit != null && deposit) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("nt")) {
				String str = selfYouHuiService.selfTransferNTHB(loginname, remit, betMultiples, isdeposit);
				if (null == str) {
					JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, remit));
					if (ntm.getBoolean("result")) {
						return null;
					} else {
						return "系统繁忙，请稍后再试，code" + ntm.getString("error");
					}
				} else {
					return str;
				}
			} else if (type.equals("qt")) {
				String str = selfYouHuiService.selfTransferQtHB(loginname, remit, betMultiples, isdeposit);
				if (null == str) {
					String b = QtUtil.getDepositPlayerMoney(loginname, remit, "");
					if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("mg")) {
				String str = selfYouHuiService.selfTransferMgHB(loginname, remit, betMultiples, isdeposit);

				if (null == str) {
					String b = MGSUtil.transferToMG(loginname, user.getPassword(), remit, seqid);
					if (null == b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("dt")) {
				String str = selfYouHuiService.selfTransferDtHB(loginname, remit, betMultiples, isdeposit);

				if (null == str) {
					String b = DtUtil.withdrawordeposit(loginname, remit);
					if (null != b && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("slot")) {
				String str = selfYouHuiService.selfTransferSlotHB(loginname, remit, betMultiples, isdeposit);
				if (null == str) {
					String b = SlotUtil.transferToSlot(loginname, remit);
					if (null != b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			}
			return "请选择游戏平台";
		}
	}

	/*
	 * 全民闯关奖励
	 */
	private Object transferInforEmigratedO = new Object();

	public String transferInforEmigrated(ISelfYouHuiService selfYouHuiService, String loginname, Double remit,
			String type) throws Exception {
		synchronized (transferInforEmigratedO) {
			System.out.println("===================================");
			if (type.equals("ttg")) {
				String str = selfYouHuiService.selfTransferTtgEmigrated(loginname, remit);
				if (null == str) {
					Boolean b = PtUtil1.addPlayerAccountPraper(loginname, remit.intValue());
					if (null != b && b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("pt")) {
				String str = selfYouHuiService.selfTransferPtEmigrated(loginname, remit);
				if (null == str) {
					Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
					if (deposit != null && deposit) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("nt")) {
				String str = selfYouHuiService.selfTransferNTEmigrated(loginname, remit);
				if (null == str) {
					JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, remit));
					if (ntm.getBoolean("result")) {
						return null;
					} else {
						return "系统繁忙，请稍后再试，code" + ntm.getString("error");
					}
				} else {
					return str;
				}
			} else if (type.equals("qt")) {
				String str = selfYouHuiService.selfTransferQtEmigrated(loginname, remit);
				if (null == str) {
					String b = QtUtil.getDepositPlayerMoney(loginname, remit, "");
					if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			}
			return "请选择游戏平台";
		}
	}

	/*
	 * 全民闯关报名
	 */
	private Object doEmigratedApplyO = new Object();

	public String doEmigratedApply(TransferService transferService, String loginname, String type) throws Exception {
		synchronized (doEmigratedApplyO) {
			DetachedCriteria dcapply = DetachedCriteria.forClass(Emigratedapply.class);
			dcapply.add(Restrictions.eq("username", loginname));
			dcapply.add(Restrictions.le("updatetime", DateUtil.getTomorrow()));
			dcapply.add(Restrictions.ge("updatetime", DateUtil.getToday()));

			List<Emigratedapply> listpply = transferService.findByCriteria(dcapply);
			if (null != listpply && listpply.size() > 0) {
				String typeapply = listpply.get(0).getType();
				String strapply = "";
				if (typeapply.equals("1")) {
					strapply = "龙都-不屈白银!";
				} else if (typeapply.equals("2")) {
					strapply = "龙都-荣耀黄金!";
				} else if (typeapply.equals("3")) {
					strapply = "龙都-华贵铂金!";
				} else if (typeapply.equals("4")) {
					strapply = "龙都-璀璨钻石!";
				} else if (typeapply.equals("5")) {
					strapply = "龙都-最强王者!";
				}
				return "已成功报名闯关等级：" + strapply;
			}
			Emigratedapply ea = new Emigratedapply();
			ea.setUpdatetime(new Date());
			System.out.println(ea.getUpdatetime());
			ea.setType(type);
			System.out.println(ea.getType() + "----------------");
			ea.setUsername(loginname);
			System.out.println(ea.getUsername() + "===============");
			transferService.save(ea);
			String str = "";
			if (type.equals("1")) {
				str = "龙都-不屈白银!";
			} else if (type.equals("2")) {
				str = "龙都-荣耀黄金!";
			} else if (type.equals("3")) {
				str = "龙都-华贵铂金!";
			} else if (type.equals("4")) {
				str = "龙都-璀璨钻石!";
			} else if (type.equals("5")) {
				str = "龙都-最强王者!";
			}
			return "报名成功,闯关等级：" + str;
		}
	}

	/*
	 * 全民闯关奖金领取
	 */
	private Object doEmigratedO = new Object();

	public String doEmigrated(TransferService transferService, String loginname) throws Exception {
		synchronized (doEmigratedO) {
			return transferService.doEmigrated(loginname);
		}
	}

	private Object weekSentObj = new Object();

	public String optWeekSent(ProposalService proposalService, String pno, Integer targetFlag, String ip,
			String target) {
		synchronized (weekSentObj) {
			if (targetFlag.equals(ProposalFlagType.EXCUTED.getCode())) {
				// 领取
				return proposalService.excuteWeekSent(pno, ip, "", target);
			}
			return "非法操作";
		}
	}

	private Object losePromoObj = new Object();

	public String optPTLosePromo(ProposalService proposalService, String pno, Integer targetFlag, String ip,
			String target, String seqId) {
		synchronized (losePromoObj) {
			if (targetFlag.equals(ProposalFlagType.CANCLED.getCode())) {
				// 取消
				return proposalService.cancelLosePromo(pno, ip, "");
			} else if (targetFlag.equals(ProposalFlagType.EXCUTED.getCode())) {
				// 领取
				Proposal proposal = (Proposal) proposalService.get(Proposal.class, pno);
				String drawResult = null;
				try {
					drawResult = proposalService.excuteLosePromo(proposal, ip, "", target, seqId);
				} catch (Exception e) {
					e.printStackTrace();
					return "领取失败";
				}
				Users user = (Users) proposalService.get(Users.class, proposal.getLoginname());
				if (drawResult == null) {
					boolean flag = false;
					try {
						if (target.equalsIgnoreCase("pttiger")) {
							flag = PtUtil.getDepositPlayerMoney(proposal.getLoginname(), proposal.getAmount());
						} else if (target.equalsIgnoreCase("ttg")) {
							flag = PtUtil1.addPlayerAccountPraper(proposal.getLoginname(), proposal.getAmount());
						} else if (target.equalsIgnoreCase("gpi")) {
							String resultCode = GPIUtil.credit(proposal.getLoginname(), proposal.getAmount(), seqId);
							if (resultCode != null && resultCode.equals(GPIUtil.GPI_SUCCESS_CODE)) {
								flag = true;
							}
						} else if (target.equalsIgnoreCase("nt")) {
							JSONObject ntm = JSONObject
									.fromObject(NTUtils.changeMoney(proposal.getLoginname(), proposal.getAmount()));
							flag = ntm.getBoolean("result") ? true : null;
						} else if (target.equalsIgnoreCase("qt")) {
							String rtnStr = QtUtil.getDepositPlayerMoney(proposal.getLoginname(), proposal.getAmount(),
									seqId);
							if (rtnStr != null && QtUtil.RESULT_SUCC.equals(rtnStr)) {
								flag = true;
							}
						} else if (target.equalsIgnoreCase("mg")) {
							String rtnStr = null;
							try {
								rtnStr = MGSUtil.transferToMG(proposal.getLoginname(), user.getPassword(),
										proposal.getAmount(), seqId);
							} catch (Exception e) {
								e.printStackTrace();
								log.info("MG api 领取/取消负盈利反赠 转入错误");
							}
							if (rtnStr == null) {
								flag = true;
							}
						} else if (target.equalsIgnoreCase("dt")) {
							String rtnStr = DtUtil.withdrawordeposit(proposal.getLoginname(), proposal.getAmount());
							if (rtnStr != null && rtnStr.equals("success")) {
								flag = true;
							}
						} else if (target.equalsIgnoreCase("png")) {
							String pngMsg = PNGUtil.transferToPNG(proposal.getLoginname(), proposal.getAmount());
							if (pngMsg != null && pngMsg.equals("success")) {
								flag = true;
							}
						} else if (target.equalsIgnoreCase("slot")) {
							String slotMsg = SlotUtil.transferToSlot(proposal.getLoginname(), proposal.getAmount());
							if (slotMsg != null && slotMsg.equals(SlotUtil.RESULT_SUCC)) {
								flag = true;
							}
						}
						if (flag == true) {
							return null;
						} else {
							return "领取失败，请联系客服";
						}
					} catch (Exception e) {
						log.error(e.getMessage());
						return "领取失败，请联系客服";
					}
				} else {
					return drawResult;
				}
			}
			return "非法操作";
		}
	}

	private Object hfAllObj = new Object();

	/**
	 * 
	 * @param announcementService
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param msg
	 * @param flag
	 *            区分第三方支付回调与平台自动补单 0：第三方支付 1：平台补单
	 * @param merchantcode
	 *            商户号
	 * @return
	 */
	public String execute1(AnnouncementService announcementService, String orderNo, Double OrdAmt, String loginname,
			String msg, String flag, String merchantcode) {
		synchronized (hfAllObj) {
			return announcementService.addPayorderHf1(orderNo, OrdAmt, loginname, msg, flag, merchantcode);
		}
	}

	/**
	 * ebet转账
	 */
	private Object ebetTransferObj = new Object();

	public String ebetTransfer(TransferService transferService, String loginname, Integer credit, String type,
			String platform, String catalog, String transferID) {
		synchronized (ebetTransferObj) {
			return transferService.transfer4Ebet(loginname, transferID, credit, platform, catalog, type);
		}
	}

	/**
	 * ebet转账验证流水
	 */
	private Object ebetTransferVerifyBetObj = new Object();

	public String transfer4EbetOutVerifyBet(TransferService transferService, String loginname, Integer credit,
			String type, String platform, String catalog, String transferID) {
		synchronized (ebetTransferVerifyBetObj) {
			return transferService.transfer4EbetOutVerifyBet(loginname, transferID, credit);
		}
	}

	/**
	 * 支付宝
	 */
	private Object ZfbPayorder = new Object();

	public String addPayorderZfb(AnnouncementService service, String outtradeno, Double money, String loginname,
			String tradeno, String date, String flag) {
		synchronized (ZfbPayorder) {
			return service.addPayorderZfb(outtradeno, money, loginname, tradeno, date, flag);
		}
	}

	/**
	 * 海尔
	 */
	private Object HaierPayorder = new Object();

	public String addPayorderHaier(AnnouncementService service, String outtradeno, Double money, String loginname,
			String merchant_code, String flag) {
		synchronized (HaierPayorder) {
			return service.addPayorderHaier(outtradeno, money, loginname, merchant_code, flag);
		}
	}

	/*
	 * GPI转入
	 */
	private Object gpiTransferObj = new Object();

	public String gpiTransferIn(TransferService transferService, String seqId, String loginname, Double remit) {
		synchronized (gpiTransferObj) {
			return transferService.transferInGPI(seqId, loginname, remit);
		}
	}

	/*
	 * GPI转出
	 */
	private Object gpiTransferOutObj = new Object();

	public String gpiTransferOut(TransferService transferService, String seqId, String loginname, Double remit) {
		synchronized (gpiTransferOutObj) {
			return transferService.transferOutGPI(seqId, loginname, remit);
		}
	}

	/**
	 * 领取PT大爆炸活动金
	 */
	private Object ptBigBangObj = new Object();

	public String getPTBigBangBonus(ProposalService proposalService, Integer id, String ip) {
		synchronized (ptBigBangObj) {
			return proposalService.drawPTBigBangBonus(id, ip);
		}
	}

	public Object saveUserTaskObj = new Object();

	public String saveUserTask(ProposalService proposalService, String loginname, Integer taskId) {
		synchronized (saveUserTaskObj) {
			try {
				return proposalService.saveTask(loginname, taskId);
			} catch (Exception e) {
				e.printStackTrace();
				return "saveUserTask:" + e.getMessage();
			}
		}
	}

	public Object yaoyaoTask = new Object();

	public String updateyaoYaoTask(ProposalService proposalService, String loginname) {
		synchronized (yaoyaoTask) {
			try {
				return proposalService.updateAndAddUsertaskAmount(loginname);
			} catch (Exception e) {
				e.printStackTrace();
				return "updateyaoYaoTask:" + e.getMessage();
			}
		}
	}

	/**
	 * 摇摇乐礼金转到主账户
	 */
	private Object yaoyaoLe = new Object();

	public String yaoyaoLeTransfer(ProposalService proposalService, Users user, Double remeit) {
		synchronized (yaoyaoLe) {
			return proposalService.transfertaskAmountInAccount(user, remeit);
		}
	}

	/*
	 * 签到操作
	 */
	private Object doSignRecordO = new Object();

	public String doSignRecord(TransferService transferService, String loginname, String device, String level)
			throws Exception {
		synchronized (doSignRecordO) {
			return transferService.doSignRecord(loginname, device, level);
		}
	}

	/*
	 * 主账户余额操作
	 */
	private Object activity = new Object();

	public String transferToMain(TransferService transferService, Users users, Integer amout) throws Exception {
		synchronized (activity) {
			return transferService.transferToMain(transferService, users, amout);
		}
	}

	/*
	 * 活动奖金转入游戏平台
	 */
	private Object activity2 = new Object();

	public String transferActivity(ISelfYouHuiService selfYouHuiService, String loginname, Double remit, String type,
			ActivityConfig config) throws Exception {
		Users user = (Users) selfYouHuiService.get(Users.class, loginname);
		String seqid = DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
		synchronized (activity2) {
			if (type.equalsIgnoreCase("ttg")) {
				String str = selfYouHuiService.selfTransferTtgActivity(loginname, remit, config);
				if (null == str) {
					Boolean b = PtUtil1.addPlayerAccountPraper(loginname, remit.intValue());
					if (null != b && b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("pt")) {
				String str = selfYouHuiService.selfTransferPtActivity(loginname, remit, config);
				if (null == str) {
					Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
					if (deposit != null && deposit) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("nt")) {
				String str = selfYouHuiService.selfTransferNTActivity(loginname, remit, config);
				if (null == str) {
					JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, remit));
					if (ntm.getBoolean("result")) {
						return null;
					} else {
						return "系统繁忙，请稍后再试，code" + ntm.getString("error");
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("qt")) {
				String str = selfYouHuiService.selfTransferQTActivity(loginname, remit, config);
				if (null == str) {
					String b = QtUtil.getDepositPlayerMoney(loginname, remit, "");
					if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("mg")) {
				String str = selfYouHuiService.selfTransferMgActivity(loginname, remit, config);
				if (null == str) {
					String b = MGSUtil.transferToMG(loginname, user.getPassword(), remit, seqid);
					if (null == b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("dt")) {
				String str = selfYouHuiService.selfTransferDtActivity(loginname, remit, config);
				if (null == str) {
					String b = DtUtil.withdrawordeposit(loginname, remit);
					if (null != b && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("png")) {
				String str = selfYouHuiService.selfTransferPngActivity(loginname, remit, config);
				if (null == str) {
					String b = PNGUtil.transferToPNG(loginname, remit);
					if (null != b && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equalsIgnoreCase("ag")) {
				return selfYouHuiService.selfTransferAgActivity(loginname, remit, config);
			} else if (type.equalsIgnoreCase("ea")) {
				return selfYouHuiService.selfTransferEaActivity(loginname, remit, config);
			}
			return "请选择游戏平台";
		}
	}

	private Object checkinfo = new Object();

	public ActivityConfig checkActivityInfo(ISelfYouHuiService service, String title, Integer level) {
		try {
			synchronized (checkinfo) {
				List<ActivityConfig> activityConfigs = MiddleServiceUtil.checkStatus(title, "", level);
				if (activityConfigs == null || activityConfigs.size() < 1) {
					return null;
				}
				return activityConfigs.get(0);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	private Object checkRecord = new Object();

	public Boolean checkRecord(TransferService transferService, String loginname, String device, String level)
			throws Exception {
		synchronized (checkRecord) {
			return transferService.checkRecord(loginname, device, level);
		}
	}

	/*
	 * 签到奖金转入游戏平台
	 */
	private Object transferSignO = new Object();

	public String transferSign(ISelfYouHuiService selfYouHuiService, String loginname, Double remit, String type)
			throws Exception {
		Users user = (Users) selfYouHuiService.get(Users.class, loginname);
		String seqid = DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
		synchronized (transferSignO) {
			if (type.equals("ttg")) {
				String str = selfYouHuiService.selfTransferTtgSign(loginname, remit);
				if (null == str) {
					Boolean b = PtUtil1.addPlayerAccountPraper(loginname, remit.intValue());
					if (null != b && b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("newpt")) {
				String str = selfYouHuiService.selfTransferPtSign(loginname, remit);
				if (null == str) {
					Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
					if (deposit != null && deposit) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("nt")) {
				String str = selfYouHuiService.selfTransferNTSign(loginname, remit);
				if (null == str) {
					JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, remit));
					if (ntm.getBoolean("result")) {
						return null;
					} else {
						return "系统繁忙，请稍后再试，code" + ntm.getString("error");
					}
				} else {
					return str;
				}
			} else if (type.equals("qt")) {
				String str = selfYouHuiService.selfTransferQTSign(loginname, remit);
				if (null == str) {
					String b = QtUtil.getDepositPlayerMoney(loginname, remit, "");
					if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("mg")) {
				String str = selfYouHuiService.selfTransferMgSign(loginname, remit);
				if (null == str) {
					String b = MGSUtil.transferToMG(loginname, user.getPassword(), remit, seqid);
					if (null == b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("dt")) {
				String str = selfYouHuiService.selfTransferDtSign(loginname, remit);
				if (null == str) {
					String b = DtUtil.withdrawordeposit(loginname, remit);
					if (null != b && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("slot")) {
				String str = selfYouHuiService.selfTransferSlotSign(loginname, remit);
				if (null == str) {
					String b = SlotUtil.transferToSlot(loginname, remit);
					if (null != b && "SUCCESS".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("agin")) {
				return "AG游戏不支持签到奖金转入";
			} else if (type.equals("png")) {
				return "PNG游戏不支持签到奖金转入";
			}

			return "请选择游戏平台";
		}
	}

	private Object weixinpayreturnO = new Object();

	/**
	 * 
	 * @param announcementService
	 * @param orderNo
	 * @param OrdAmt
	 * @param loginname
	 * @param msg
	 * @param flag
	 *            区分第三方支付回调与平台自动补单 0：第三方支付 1：平台补单
	 * @param merchantcode
	 *            商户号
	 * @return
	 */
	public String weixinpayreturn(AnnouncementService announcementService, String out_trade_no, String flag,
			String OrdAmt) {
		synchronized (weixinpayreturnO) {
			return announcementService.weixinpayreturn(out_trade_no, flag, OrdAmt);
		}
	}

	private Object addPayLfWxzfO = new Object();

	/**
	 * 
	 * @param announcementService
	 * @param orderid
	 * @param money
	 * @param loginname
	 * @param flag
	 * @return
	 */
	public String addPayLfWxzf(AnnouncementService announcementService, String orderid, Double money, String loginname,
			String flag) {
		synchronized (addPayLfWxzfO) {
			return announcementService.addPayLfWxzf(orderid, money, loginname, flag);
		}
	}

	/*
	 * 推荐礼金转入游戏平台
	 */
	private Object transferFriendO = new Object();

	public String transferFriend(ISelfYouHuiService selfYouHuiService, String loginname, Double remit, String type)
			throws Exception {
		Users user = (Users) selfYouHuiService.get(Users.class, loginname);
		String seqid = DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
		remit = Math.abs(remit);
		synchronized (transferFriendO) {
			if (type.equals("ttg")) {
				String str = selfYouHuiService.selfTransferTtgFriend(loginname, remit);
				if (null == str) {
					Boolean b = PtUtil1.addPlayerAccountPraper(loginname, remit.intValue());
					if (null != b && b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("pt")) {
				String str = selfYouHuiService.selfTransferPtFriend(loginname, remit);
				if (null == str) {
					Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
					if (deposit != null && deposit) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("nt")) {
				String str = selfYouHuiService.selfTransferNTFriend(loginname, remit);
				if (null == str) {
					JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, remit));
					if (ntm.getBoolean("result")) {
						return null;
					} else {
						return "系统繁忙，请稍后再试，code" + ntm.getString("error");
					}
				} else {
					return str;
				}
			} else if (type.equals("qt")) {
				String str = selfYouHuiService.selfTransferQtFriend(loginname, remit);
				if (null == str) {
					String b = QtUtil.getDepositPlayerMoney(loginname, remit, "");
					if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("mg")) {
				String str = selfYouHuiService.selfTransferMgFriend(loginname, remit);
				if (null == str) {
					String b = MGSUtil.transferToMG(loginname, user.getPassword(), remit, seqid);
					if (null == b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("dt")) {
				String str = selfYouHuiService.selfTransferDtFriend(loginname, remit);
				if (null == str) {
					String b = DtUtil.withdrawordeposit(loginname, remit);
					if (null != b && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			}
			return "请选择游戏平台";
		}
	}

	private Object agentUserBindGameUserO = new Object();

	public String agentUserBindGameUser(CustomerService cs, String loginnameAgent, String loginnameGame,
			String password) {
		Users agentUser = cs.getUsers(loginnameAgent);
		if (agentUser == null) {
			return "未找到该代理账号！";
		}
		if (StringUtils.isBlank(agentUser.getAccountName())) {
			return "代理账号用户名有问题！";
		}
		if (!agentUser.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			return "代理账号密码输入错误，清重新输入！";
		}
		// if(agentUser.getFlag()==1){
		// return "该代理账号已经禁用！请联系工作人员";
		// }

		Users gameUser = cs.getUsers(loginnameGame);
		if (gameUser == null) {
			return "未查找到该游戏账号！";
		}
		if (StringUtils.isBlank(gameUser.getAccountName())) {
			return "游戏账号用户名有问题！";
		}
		// if(gameUser.getFlag()==1){
		// return "该游戏账号已经禁用！请联系工作人员";
		// }
		//
		// if(agentUser.getWarnflag() == 2){
		// return "代理账号是危险账号，不能进行绑定操作！请联系工作人员";
		// }
		// if(gameUser.getWarnflag() == 2){
		// return "要绑定的游戏账号是危险账号，不能进行绑定操作！请联系工作人员";
		// }

		if (!agentUser.getAccountName().equals(gameUser.getAccountName())) {
			return "代理账号名与游戏账号名字不同，不能绑定！";
		}
		if (!UserRole.MONEY_CUSTOMER.getCode().equals(gameUser.getRole())) {
			return "您所绑定的游戏账号角色有问题，不能绑定！";
		}

		synchronized (agentUserBindGameUserO) {
			DetachedCriteria dcAgent = DetachedCriteria.forClass(UsersAgentGame.class);
			dcAgent.add(Restrictions.eq("loginnameAgent", loginnameAgent));
			dcAgent.add(Restrictions.ne("deleteFlag", 1));
			List<UsersAgentGame> listAgent = cs.findByCriteria(dcAgent);

			if (listAgent != null && listAgent.size() > 0) {
				return "该代理账号已经绑定游戏账号,请先解绑！";
			}

			DetachedCriteria dcGame = DetachedCriteria.forClass(UsersAgentGame.class);
			dcGame.add(Restrictions.eq("loginnameGame", loginnameGame));
			dcGame.add(Restrictions.ne("deleteFlag", 1));
			List<UsersAgentGame> listGame = cs.findByCriteria(dcGame);
			if (listGame != null && listGame.size() > 0) {
				return "该游戏账号已经被其他代理绑定，请先解绑！";
			}

			UsersAgentGame usersAgentGame = new UsersAgentGame();
			usersAgentGame.setCreateTime(new Date());
			usersAgentGame.setDeleteFlag(0);
			usersAgentGame.setLoginnameAgent(loginnameAgent);
			usersAgentGame.setLoginnameGame(loginnameGame);
			cs.save(usersAgentGame);
		}
		return "与游戏账号绑定成功！";
	}

	public synchronized void updateSingleParty(ProposalService proposalService, Date agStart, Date agEnd, Date ptStart,
			Date ptEnd, String rankdate) {
		String countQ = " SELECT count(*) FROM singleparty WHERE TO_DAYS(rankdate) >= TO_DAYS(:rankdate) ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rankdate", rankdate);
		Session session = null;
		List<Object[]> listS = null;
		try {
			session = proposalService.getHibernateTemplate().getSessionFactory().openSession();
			Query queryC = session.createSQLQuery(countQ).setProperties(param);
			List q = queryC.list();
			if (q.get(0) != null && Integer.parseInt(q.get(0).toString()) > 0) {
				if (session != null && session.isOpen()) {
					session.close();
				}
				return;
			}

			String selectS = " SELECT s3.loginname as loginname, SUM(s3.bettotal) as bettotal FROM "
					+ " ((SELECT SUM(a.bettotal) AS bettotal, u.loginname AS loginname FROM agprofit a LEFT JOIN users u ON (u.loginname = a.loginname) "
					+ " WHERE a.createTime >=:agStart AND a.createTime <=:agEnd AND a.platform IN('nt','qt','ttg') AND a.bettotal > 0.0 GROUP BY u.loginname ) "
					+ " UNION (SELECT SUM(p.BETS_TIGER) AS bettotal, u.loginname AS loginname FROM pt_data_new p LEFT JOIN users u ON ( u.loginname = SUBSTR(p.playername, 2)) "
					+ " WHERE p.STARTTIME >=:ptStart AND p.STARTTIME <=:ptEnd AND p.BETS_TIGER > 0.0 GROUP BY u.loginname)) AS s3 GROUP BY s3.loginname "
					+ " ORDER BY bettotal desc ";
			param.put("agStart", agStart);
			param.put("agEnd", agEnd);
			param.put("ptStart", ptStart);
			param.put("ptEnd", ptEnd);

			Query queryS = session.createSQLQuery(selectS).setProperties(param);
			listS = queryS.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		Iterator<Object[]> it = listS.iterator();
		List<SingleParty> singles = new ArrayList<SingleParty>();
		while (it.hasNext()) {
			Object[] obj = it.next();
			if (obj[0] == null || obj[1] == null) {
				continue;
			}
			SingleParty sp = new SingleParty();
			sp.setLoginname(obj[0].toString());
			sp.setBettotal(Double.parseDouble(obj[1].toString()));
			sp.setRankdate(rankdate);
			singles.add(sp);
		}
		this.ranksingle(singles);
		Iterator<SingleParty> it_s = singles.iterator();
		while (it_s.hasNext()) {
			proposalService.saveOrUpdate(it_s.next());
		}
	}

	private void ranksingle(List<SingleParty> singles) {
		if (singles != null && singles.size() > 0) {

			Double bettotal = -1.0;
			int ranking = 1;
			for (int i = 0; i < singles.size(); i++) {

				SingleParty sp = singles.get(i);
				if (i == 0) {
					sp.setRanking(ranking);
					bettotal = sp.getBettotal() == null ? 0.0 : sp.getBettotal();
				} else {
					if (sp.getBettotal() != null && sp.getBettotal().equals(bettotal)) {
						sp.setRanking(ranking);
					} else {
						ranking = i + 1;
						sp.setRanking(ranking);
						bettotal = sp.getBettotal() == null ? 0 : sp.getBettotal();
					}
				}
			}
		}
	}

	/**
	 * 守护女神报名
	 */
	public synchronized String doGoddessApply(TransferService transferService, String loginname, String goddess) {

		DetachedCriteria dcapply = DetachedCriteria.forClass(Goddessrecord.class);
		dcapply.add(Restrictions.eq("loginname", loginname));
		List<Goddessrecord> list = transferService.findByCriteria(dcapply);
		if (list != null && list.size() > 0) {
			return "您已经报名" + Goddesses.getName(list.get(0).getGoddessname()) + ",不能重复报名！";
		}

		Goddessrecord goddessrecord = new Goddessrecord();
		goddessrecord.setLoginname(loginname);
		goddessrecord.setGoddessname(goddess);
		goddessrecord.setCreatetime(DateUtil.now());
		transferService.save(goddessrecord);
		return "报名成功";
	}

	/**
	 * 获得手机连续签到抽奖
	 */
	private Object winningLotteryO = new Object();

	public void winningLottery(IUserLotteryService userLotteryService, String loginname, String itemName)
			throws Exception {
		synchronized (winningLotteryO) {
			userLotteryService.winningLottery(loginname, itemName);
		}
	}

	/*
	 * 存款奖金操作
	 */
	private Object doRechargeRecordO = new Object();

	public String doRechargeRecord(TransferService transferService, String loginname) throws Exception {
		synchronized (doRechargeRecordO) {
			return transferService.doRechargeRecord(loginname);
		}
	}

	/*
	 * 流水奖金操作
	 */
	private Object doStreamRecordO = new Object();

	public String doStreamRecord(TransferService transferService, String loginname) throws Exception {
		synchronized (doStreamRecordO) {
			return transferService.doStreamRecord(loginname);
		}
	}

	/**
	 * 红包雨账户转入游戏账号
	 * 
	 * @param selfYouHuiService
	 * @param loginname
	 * @param remit
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private Object transferInforRedRainO = new Object();

	public String transferInforRedRain(ISelfYouHuiService selfYouHuiService, String loginname, Double remit,
			String type) throws Exception {
		Users user = (Users) selfYouHuiService.get(Users.class, loginname);
		String seqid = DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
		synchronized (transferInforRedRainO) {
			if (type.equals("mg")) {
				String str = selfYouHuiService.selfTransferMgRedRain(loginname, remit);
				if (null == str) {
					String b = MGSUtil.transferInAndOutMG("tw", loginname, user.getPassword(), remit, seqid, "IN");
					if (null == b) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("dt")) {
				String str = selfYouHuiService.selfTransferDtRedRain(loginname, remit);
				if (null == str) {
					String b = DtUtil.withdrawordeposit(loginname, remit);
					if (null != b && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else if (type.equals("cq9")) {
				String str = selfYouHuiService.selfTransferCq9RedRain(loginname, remit);
				if (null == str) {
					String b = CQ9Util.transferIn(loginname, user.getPassword(), remit);
					if (StringUtils.isNotEmpty(b) && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			}else if (type.equals("pg")) {
				String str = selfYouHuiService.selfTransferPgRedRain(loginname, remit);
				if (null == str) {
					String b = PGUtil.transferIn(loginname, remit);
					if (StringUtils.isNotEmpty(b) && "success".equals(b)) {
						return null;
					} else {
						return "系统繁忙，请稍后再试";
					}
				} else {
					return str;
				}
			} else {
				return "暂不支持转入此平台";
			}

		}
	}

}
