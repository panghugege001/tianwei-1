package com.gsmc.png.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BBinReqBody implements Serializable {

	private static final long serialVersionUID = 77847743247089594L;

	/**
	 * website 网站名称
	 */
	private String website;

	/**
	 * username 会员帐号(请输入4‐20个字元, 仅可输入英文字母以及数字的组合)
	 */
	private String username;

	/**
	 * uppername 上层帐号
	 */
	private String uppername;

	/**
	 * password 密码(须为6~12码英文或数字且符合0~9及a~z字元)
	 */
	private String password;

	/**
	 * key 验证码(需全小写)
	 */
	private String key;

	/**** 登录 ****/
	/**
	 * lang 语系：zh‐cn(简中);zh‐tw(繁中);en‐us(英文);euc‐jp(日文); ko(韩文);th(泰文)
	 * ;es(西班牙文);vi(越南文);khm(柬埔寨);lao(寮国文)
	 */
	private String lang;

	/**
	 * page_site BB体育：ball、视讯：live、 机率：game、彩票：Ltlottery，若为空白则导入整合页
	 */
	@JsonProperty(value = "page_site")
	private String pageSite;

	/**
	 * page_present 视讯：live，导入视讯大厅页面，需同时带入page_site
	 */
	@JsonProperty(value = "page_present")
	private String pagePresent;

	/**** 转帐 ****/
	/**
	 * remitno 转帐序号(唯一值)，可用贵公司转帐纪录的流水号， 以避免重覆转帐< 请用int(19)(
	 * 1~9223372036854775806)来做设定 >，别名transid
	 */
	private String remitno;

	/**
	 * action IN(转入额度) OUT(转出额度)
	 */
	private String action;

	/**
	 * remit 转帐额度(正整数)
	 */
	private Integer remit;

	/**** 查询会员转帐是否成功 ****/
	/**
	 * transid 转帐序号，对应Transfer API中的remitno
	 */
	private String transid;

	/**** 查询会员额度 ****/
	/**
	 * page 查询页数
	 */
	private int page;

	/**
	 * pagelimit 每页数量
	 */
	private int pagelimit;

	/**** BetRecord ****/
	/**
	 * rounddate 日期ex：2012/03/21、2012‐03‐21
	 */
	private String rounddate;

	/**
	 * starttime 开始时间ex：00:00:00(BB体育游戏无效)
	 */
	private String starttime;

	/**
	 * endtime 结束时间ex：23:59:59(BB体育游戏无效)
	 */
	private String endtime;

	/**
	 * gamekind 游戏种类(1：BB体育、3：视讯、5：机率、12：彩票、15：3D厅)
	 */
	private String gamekind;

	/**
	 * subgamekind 请详查附件五(gamekind=5时，值:1、2、3，预设为1)
	 */
	@JsonProperty(value = "subgamekind")
	private String gameSubkind;

	/**
	 * gametype 否请详查附件二(gamekind=12时，需强制带入)
	 */
	private String gametype;

	private String wagersid;

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUppername() {
		return uppername;
	}

	public void setUppername(String uppername) {
		this.uppername = uppername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getPageSite() {
		return pageSite;
	}

	public void setPageSite(String pageSite) {
		this.pageSite = pageSite;
	}

	public String getPagePresent() {
		return pagePresent;
	}

	public void setPagePresent(String pagePresent) {
		this.pagePresent = pagePresent;
	}

	public String getRemitno() {
		return remitno;
	}

	public void setRemitno(String remitno) {
		this.remitno = remitno;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getRemit() {
		return remit;
	}

	public void setRemit(Integer remit) {
		this.remit = remit;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagelimit() {
		return pagelimit;
	}

	public void setPagelimit(int pagelimit) {
		this.pagelimit = pagelimit;
	}

	public String getRounddate() {
		return rounddate;
	}

	public void setRounddate(String rounddate) {
		this.rounddate = rounddate;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getGamekind() {
		return gamekind;
	}

	public void setGamekind(String gamekind) {
		this.gamekind = gamekind;
	}

	public String getGameSubkind() {
		return gameSubkind;
	}

	public void setGameSubkind(String gameSubkind) {
		this.gameSubkind = gameSubkind;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public String getWagersid() {
		return wagersid;
	}

	public void setWagersid(String wagersid) {
		this.wagersid = wagersid;
	}

	@Override
	public String toString() {
		return "BBinReqBody{" +
				"website='" + website + '\'' +
				", username='" + username + '\'' +
				", uppername='" + uppername + '\'' +
				", password='" + password + '\'' +
				", key='" + key + '\'' +
				", lang='" + lang + '\'' +
				", pageSite='" + pageSite + '\'' +
				", pagePresent='" + pagePresent + '\'' +
				", remitno='" + remitno + '\'' +
				", action='" + action + '\'' +
				", remit=" + remit +
				", transid='" + transid + '\'' +
				", page=" + page +
				", pagelimit=" + pagelimit +
				", rounddate='" + rounddate + '\'' +
				", starttime='" + starttime + '\'' +
				", endtime='" + endtime + '\'' +
				", gamekind='" + gamekind + '\'' +
				", gameSubkind='" + gameSubkind + '\'' +
				", gametype='" + gametype + '\'' +
				", wagersid='" + wagersid + '\'' +
				'}';
	}
}
