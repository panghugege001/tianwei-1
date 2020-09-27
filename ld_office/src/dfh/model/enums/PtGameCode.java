package dfh.model.enums;
public enum PtGameCode {

	RQ(1, "洛奇"),
	GTX2(2, "钢铁侠2"), 
	JGL(3, "金刚狼"), 
	GTX50X(4, "钢铁侠 50线"),
	GTX3(5, "钢铁侠3"),
	SE(6, "索尔"),
	FCZLM(7, "复仇者联盟"),
	GTX(9, "钢铁侠"),
	SQSX(10, "神奇四侠"),
	SQSX50X(11, "神奇四侠50线"),
	SQLJR(12, "神奇绿巨人"),
	SQLJR50X(13, "神奇绿巨人50线"),
	CDX(14, "超胆侠"),
	ALK(15, "艾丽卡"),
	ELQS(16, "恶灵骑士"),
	DFZS(17, "刀锋战士"),
	XZJ(18, "X战警"),
	JDS(19, "角斗士"),
	JG(20, "金刚"),
	HDTB(21, "海底探宝"),
	FHB(23, "粉红豹"),
	KXSGNC(24, "酷炫水果农场"),
	HTQS(27, "火腿骑士"),
	ZZX(28, "蜘蛛侠"),
	WSJCF(30, "万圣节财富"),
	XZJ50X(31, "X战警 50线"),
	CASHBACK(32, "Cashback先生"),
	SZXZYHTL(33, "三只小猪与大灰狼"),
	SDJLRJX(34, "圣诞节老人惊喜"),
	DTLCQ(35, "德托里传奇"),
	
	ALIENROBOTS(200,"机器外星人"),
	ATTRACTION(201,"引力"),
	BEACHLIFE(202,"阳光海滩"),
	BIGBANG(203,"宇宙大爆炸"),
	BLOODSUCKERS(204,"吸血鬼"),
	DEMOLITIONSQUAD(205,"破坏小组"),
	DRAGONISLAND(206,"神龙岛"),
	EGYPTIANHEROES(207,"埃及英雄"),
	GONZOSQUEST(208,"贡左的探索"),
	LIGHTS(209,"萤光点点"),
	FISTICUFFS(210,"狂热拳击"),
	FLOWERS(211,"能量霸王花"),
	FRUITCASE(212,"水果盘"),
	JACKANDBEANSTALK(213,"杰克与魔豆"),
	JACKHAMMER2(214,"杰克哈默垂"),
	LOSTISLAND(215,"失落岛"),
	LUCKYANGLER(216,"幸运垂钓者"),
	MAGICPORTAL(217,"魔法之门"),
	GOBANANAS(218,"狂取香蕉"),
	MUSE(219,"女神谬斯"),
	MYTHICMAIDEN(220,"神话少女"),
	WONKYWABBITS(221,"菜园觅食"),
	SPACEWARS(224,"星际大战"),
	SOUTHPARKREELCHAOS(225,"南方公园2"),
	STARBURST(226,"闪耀星空"),
	SUBTOPIA(227,"海底工业城"),
	THIEF(228,"神偷"),
	WILDTURKEY(229,"野生火鸡"),
	WISHMASTER(230,"神灯精灵"),
	ZOMBIES(231,"僵尸"),
	DEADORALIVE(232,"生或死"),
	REELSEAL(233,"卷偷"),
	PIGGYRICHES(234,"富贵猪"),
	VICTORIOUS(235,"辉煌胜利"),
	TWINSPIN(236,"双轴旋转"),
	KINGOFCHICAGO(237,"芝加哥之王"),
	SECRETOFTHESTONES(223,"神秘之石"),
	TORNADO(239,"飓风"),
	HIT2SPLIT(240,"分裂"),
	STEAMTOWER(241,"蒸汽巨塔"),
	FRUITSHOP(242,"水果铺"),
	GHOSTPIRATES(243,"幽灵海盗"),
	SOUTHPARK(245,"南方公园 "),
	EGGOMATIC(247,"小鸡"),
	REELRUSH(248,"狂热卷轴"),
	DRACULA(186,"吸血僵尸"),
	VEGASPARTY(195,"维加斯派对"),
	SPINATAGRANDE(196,"西班牙斗牛"),
	COSMICFORTUNE(238,"宇宙财富"),
	CRIMESCENE(191,"犯罪现场"),
	WILDROCKETS(193,"疯狂火箭"),
	HALLOFGODS(174,"神殿"),
	FRANKENSTEIN(194,"科学怪人"),
	CREATUREOFTHEBLACKLAGOON(188,"黑湖怪人"),
	THUNDERFIST(190,"雷霆之拳"),
	WILDWATER(164,"冲浪"),
	VOODOO(163,"巫术"),
	GEISHAWONDER(184,"艺伎"),
	HORUS(169,"荷鲁斯"),
	ICYWONDER(182,"冰冷的奇迹"),
	ARABIANNIGHTS(189,"一千零一夜"),
	EXCALIBUR(172,"神剑"),
	MEGAFORTUNE(160,"超级财富"),
	SUPERLUCKYFROG(159,"幸运青蛙"),
	THRILLSPIN(165,"刺激旋转"),
	GROOVY60(167,"怀旧时代"),
	VIKING(173,"维京时代"),
	TIKIWONDER(177,"夏威夷风情"),
	TROLL(171,"巨魔"),
	SIMSALABIM(244,"simsalabim"),
	WILDWITCHES(170,"女巫"),
	GOLDENSHAMROCK(183,"金色三叶草"),
	TALESOFKRAKOW(161,"克拉科夫的故事"),
	FORTUNETELLER(162,"算命先生"),
	DIAMONDDOGS(246,"钻石狗"),
	SUPER80(168,"超级80"),
	FUNKY70(166,"时髦70"),
	SPELLCAST(175,"法师"),
	STARBURSTMINI(249,"闪耀星空迷你");
	
	public static String getText(Integer code) {
		PtGameCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PtGameCode type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private PtGameCode(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
}
