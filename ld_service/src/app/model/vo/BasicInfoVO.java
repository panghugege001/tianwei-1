package app.model.vo;

import java.util.List;

import app.model.po.AppVersion;
public class BasicInfoVO implements java.io.Serializable{
	private static final long serialVersionUID = -3132726349238147359L;
	private PopAnnouncementVO popAnnouncement;//弹窗公告（从系统参数里面读取）
	private List<AnnouncementForAppVO> announcementList;//跑马灯公告列表
	private List<IndexSpecialTopicVO> indexSpecialTopicList;//首页专题列表
	private List<LatestPreferentialVO>latestPreferentialList;//首页最新优惠
	private GamePlatScoreVO gamePlatScore;//游戏平台评分
	private AppVersion versionInfo;//版本信息
	
	public AppVersion getVersionInfo() {
		return versionInfo;
	}
	public void setVersionInfo(AppVersion versionInfo) {
		this.versionInfo = versionInfo;
	}
	public PopAnnouncementVO getPopAnnouncement() {
		return popAnnouncement;
	}
	public void setPopAnnouncement(PopAnnouncementVO popAnnouncement) {
		this.popAnnouncement = popAnnouncement;
	}
	public List<AnnouncementForAppVO> getAnnouncementList() {
		return announcementList;
	}
	public void setAnnouncementList(List<AnnouncementForAppVO> announcementList) {
		this.announcementList = announcementList;
	}
	public GamePlatScoreVO getGamePlatScore() {
		return gamePlatScore;
	}
	public void setGamePlatScore(GamePlatScoreVO gamePlatScore) {
		this.gamePlatScore = gamePlatScore;
	}
	public List<IndexSpecialTopicVO> getIndexSpecialTopicList() {
		return indexSpecialTopicList;
	}
	public void setIndexSpecialTopicList(
			List<IndexSpecialTopicVO> indexSpecialTopicList) {
		this.indexSpecialTopicList = indexSpecialTopicList;
	}
	public List<LatestPreferentialVO> getLatestPreferentialList() {
		return latestPreferentialList;
	}
	public void setLatestPreferentialList(List<LatestPreferentialVO> latestPreferentialList) {
		this.latestPreferentialList = latestPreferentialList;
	}
	
	
}
