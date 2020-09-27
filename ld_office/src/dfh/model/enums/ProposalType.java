package dfh.model.enums;

/**
 * 提案类型
 * 
 *
 */
public enum ProposalType
{
  /*NEWACCOUNT(501, "开户"),*/
  CASHIN(502, "存款"),
  JANPAY(222, "久安支付"),
  CASHOUT(503, "提款"),
  /*REBANKINFO(504, "银改"), */
  /*CONCESSIONS(505, "开户优惠"),*/
  PRIZE(506, "幸运抽奖"), 
  XIMA(507, "系统洗码"),
  /*BANKTRANSFERCONS(508, "转账优惠"),*/ 
  OFFER(509,"再存优惠"),
  BIRTHDAY(513, "生日礼金"),
  SELFXIMA(517,"自助洗码"),
  AGSELFXIMA(611,"ag自助洗码"),
  AGINSELFXIMA(612,"agin自助洗码"),
  BBINSELFXIMA(613,"bbin自助洗码"),
  KGSELFXIMA(614,"kg自助洗码"),
  SBSELFXIMA(615,"sb自助洗码"),
  PTTIGERSELFXIMA(616,"pttiger自助洗码"),
  PTOTHERSELFXIMA(617,"ptother自助洗码"),
  /*KENOSELFXIMA(618,"keno自助洗码"),*/
  /*SIXLOTTERYSELFXIMA(619,"sixlottery自助洗码"),*/
  EBETSELFXIMA(620,"ebet自助洗码"),
  JCSELFXIMA(621,"jc自助洗码"),
  TTGSELFXIMA(622,"ttg自助洗码"),
  QTSELFXIMA(624,"qt自助洗码"),
  NTSELFXIMA(625,"nt自助洗码"),
  /*GPISELFXIMA(623,"gpi自助洗码"),*/
  NTWOSELFXIMA(627,"n2live自助洗码"),
  DTSELFXIMA(628,"dt自助洗码"),
  MGSELFXIMA(629,"mg自助洗码"),
  PNGSELFXIMA(630,"png自助洗码"),
  PTSKYSELFXIMA(632,"ptsky自助洗码"),
  
  ACTIVITY442(442 , "活动礼金"),
  SELFPT90(590 , "自助PT首存优惠"),
  SELFPT91(591 , "自助PT次存优惠"),
  SELFPT92(592 , "自助EA次存优惠"),
  /*SELFPT93(593 , "自助AG存送优惠"),
  SELFPT94(594 , "自助AGIN存送优惠"),
  SELFPT95(595 , "自助BBIN存送优惠"),
  SELFEBET96(596 , "自助EBET首存优惠"),
  SELFEBET97(597 , "自助EBET次存优惠"),*/
  SELFEBET98(598 , "自助TTG首存优惠"),
  SELFEBET99(599 , "自助TTG次存优惠"),
  EMIGRATED391(391 , "龙都闯关奖励转入游戏"),
  SELFPT8(701 , "自助PT8元优惠"),
  SELFPTZZTYJ771(771 , "自助PT体验金"),
  SELFMGZZTYJ772(772 , "自助MG体验金"),
  SELFDTZZTYJ773(773 , "自助DT体验金"),
  SELFQTZZTYJ774(774 , "自助QT体验金"),
  SELFNTZZTYJ775(775 , "自助NT体验金"),
  SELFTTGZZTYJ776(776 , "自助TTG体验金"),
  SELFPNGZZTYJ777(777 , "自助PNG体验金"),
  SELFNTWOZZTYJ778(778 , "自助N2体验金"),
  SELFSLOTZZTYJ779(779 , "自助老虎机体验金"),
    
  /*SELFGPIFIRST(702 , "自助GPI首存优惠"),*/
  /*SELFGPITWICE(703 , "自助GPI次存优惠"),*/
  /*SELFGPI704(704 , "自助GPI限时优惠"),*/
  SELFPT705(705 , "自助PT限时优惠"),
  SELFTTG706(706 , "自助TTG限时优惠"),
  SELFNTFIRST(707 , "自助NT首存优惠"),
  SELFNTTWICE(708 , "自助NT次存优惠"),
  SELFNTSPEC(709 , "自助NT限时优惠"),
  SELFQTFIRST(710 , "自助QT首存优惠"),
  SELFQTTWICE(711 , "自助QT次存优惠"),
  SELFQTSPEC(712 , "自助QT限时优惠"),
  
  SELFMGFIRST(730 , "自助MG首存优惠"),
  SELFMGTWICE(731 , "自助MG次存优惠"),
  SELFMGSPEC(732 , "自助MG限时优惠"),
  
  SELFDTFIRST(733 , "自助DT首存优惠"),
  SELFDTTWICE(734 , "自助DT次存优惠"),
  SELFDTSPEC(735 , "自助DT限时优惠"),
  
  SELFAGFIRST(736 , "自助AG首存优惠"),
  SELFAGTWICE(737 , "自助AG次存优惠"),
  SELFAGSPEC(738 , "自助AG限时优惠"),
  SELFPNGFIRST(740 , "自助PNG首存优惠"),
  SELFPNGTWICE(741 , "自助PNG次存优惠"),
  SELFPNGSPEC(742 , "自助PNG限时优惠"),
  
	SELFAGINFIRST(743, "自助AG真人首存优惠"),
	SELFAGINTWICE(744, "自助AG真人次存优惠"),
	SELFAGINSPEC(745, "自助AG真人限时优惠"),
	
  WEEKSENT(560, "周周回馈"),
  SELFHELP_APP_PREFERENTIAL(512, "自助APP下载彩金"),  
  SELFXIMAPT(561,"PT自助洗码"),
  LEVELPRIZE(518, "晋级礼金"),//516已经在别处使用,请不要再用
  PROFIT(519, "负盈利反赠"),//516已经在别处使用,请不要再用
  YAOYAOLE(520,"摇摇乐礼金"),
  COUPONCSYHJ(537, "邀请码"),
//  COUPONCHBYHJ(536,"红包优惠券"),
  COUPONCSYHYHJ1(531, "10%存送优惠券"),
  COUPONCSYHYHJ2(532, "20%存送优惠券"),
  COUPONCSYHYHJ3(533, "30%存送优惠券"),
  COUPONCSYHYHJ4(534, "40%存送优惠券"),
  COUPONCSYHYHJ5(535, "50%存送优惠券"),
  /*COUPONCSYHYHJ71(571, "PT红包优惠券"),
  COUPONCSYHYHJ72(572, "PT68%存送优惠券"),
  COUPONCSYHYHJ73(573, "PT100%存送优惠券"),*/
  /*COUPONCSYHYHJ81(581, "188体育红包优惠券"),
  COUPONCSYHYHJ82(582, "188体育存10万送2万"),
  COUPONCSYHYHJ83(583, "188体育存20万送5万"),
  COUPONCSYHYHJ84(584, "188体育20%存送优惠劵"),*/
  SELF_101(101, "红包雨"),
  COUPONCSYHYHJ401(401, "TTG100%存送优惠券15倍流水"),
  COUPONCSYHYHJ402(402, "TTG88%存送优惠券"),
  COUPONCSYHYHJ403(403, "TTG68%存送优惠券"),
  COUPONCSYHYHJ404(404, "TTG100%存送优惠券20倍流水"),
  /*COUPONCSYHYHJ405(405, "GPI100%存送优惠券15倍流水"),
  COUPONCSYHYHJ406(406, "GPI88%存送优惠券"),
  COUPONCSYHYHJ407(407, "GPI68%存送优惠券"),
  COUPONCSYHYHJ408(408, "GPI100%存送优惠券20倍流水"),*/
  COUPONCSYHYHJ409(409, "PT100%存送优惠券15倍流水"),
  COUPONCSYHYHJ410(410, "PT88%存送优惠券"),
  COUPONCSYHYHJ411(411, "PT68%存送优惠券"),
  COUPONCSYHYHJ412(412, "PT100%存送优惠券20倍流水"),
  REDCOUPON419(419,"红包优惠券"),
  REDCOUPON319(319,"存送优惠券"),
  SELF_791(791, "自助老虎机首存优惠"),
  SELF_792(792, "自助老虎机次存优惠"),
  SELF_793(793, "自助老虎机限时优惠"),
  HB499(499,"自助存提款红包优惠"),
//COUPONCSYHYHJ413(413, "保留"),long8TTG已占用
//COUPONCSYHYHJ414(414, "保留"),long8TTG已占用
  /*COUPONPTGIFT415(415, "PT自助红包优惠"),*/
  COUPONCSYHYHJ425(425, "NT100%存送优惠券15倍流水"),
  COUPONCSYHYHJ422(422, "NT88%存送优惠券"),
  COUPONCSYHYHJ423(423, "NT68%存送优惠券"),
  COUPONCSYHYHJ424(424, "NT100%存送优惠券20倍流水"),
  COUPONCSYHYHJ426(426, "QT100%存送优惠券15倍流水"),
  COUPONCSYHYHJ427(427, "QT88%存送优惠券"),
  COUPONCSYHYHJ428(428, "QT68%存送优惠券"),
  COUPONCSYHYHJ429(429, "QT100%存送优惠券20倍流水"),
  
  COUPONCSYHYHJ430(430, "MG100%存送优惠券15倍流水"),
  COUPONCSYHYHJ431(431, "MG88%存送优惠券"),
  COUPONCSYHYHJ432(432, "MG68%存送优惠券"),
  COUPONCSYHYHJ433(433, "MG100%存送优惠券20倍流水"),
  COUPONCSYHYHJ434(434, "DT100%存送优惠券15倍流水"),
  COUPONCSYHYHJ435(435, "DT88%存送优惠券"),
  COUPONCSYHYHJ436(436, "DT68%存送优惠券"),
  COUPONCSYHYHJ437(437, "DT100%存送优惠券20倍流水"),
  
  FRIEND390(390 , "好友推荐奖励转入游戏"),
  
  SIGNDEPOSIT420(420 , "签到礼金"),
  POINTDEPOSIT421(421 , "积分兑换奖金"),
  
  PTBIGBANG(555, "PT大爆炸"),
  PROXYADVANCE(550, "代理信用预支");
//  PT8COUPON(575,"PT8元红包优惠劵");
  
	public static String getText(Integer code) {
	    ProposalType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      ProposalType type = p[i];
	      if (type.getCode().intValue() == code.intValue())
	        return type.getText();
	    }
	    return null;
	  }

	private Integer code;


  private String text;

  private ProposalType(Integer code, String text) {
	this.code = code;
	this.text = text;
}

  public Integer getCode()
  {
    return this.code;
  }

  public String getText() {
    return this.text;
  }
}

