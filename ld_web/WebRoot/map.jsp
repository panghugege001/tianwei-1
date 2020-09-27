<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="dfh.action.vo.ActivityCalendarVO" %>
<%@ page import="dfh.utils.AxisSecurityEncryptUtil" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
 

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <style>html,body{height: 100%;} body{ background:#eae9e8;} </style>
</head>
<body>
	 <ul class="ul-map">
		<li>
			<dl>
				<dt>官方优惠</dt>
				<dd><a href="/newsList.jsp" target="_blank">官方公告</a></dd>
				<dd><a href="www.longdobbs.com" target="_blank">官方论坛</a></dd>
				<dd><a href="/promotion.jsp?showid=qun" target="_blank">关注微信</a></dd>
			</dl>
		</li>
		 <li>
			<dl>
				<dt>最新优惠</dt>
				<dd><a href="/vip.jsp" target="_blank">赌神堂</a></dd>
				 
				<dd><a href="/promotion.jsp?showid=tyj" target="_blank">8-88 体验金</a></dd>
				<dd><a href="/promotion.jsp?showid=cicun" target="_blank">次次存送 2888</a></dd>
				<dd><a href="/promotion.jsp?showid=qun" target="_blank">群发好友获 28</a></dd>
				<dd><a href="/promotion.jsp?showid=mail" target="_blank">订阅邮件领 18</a></dd>
				<dd><a href="/promotion.jsp?showid=return" target="_blank">天天返水 1%</a></dd>
				 
			</dl>
		</li>
		<li>
			<dl>
				<dt>老虎机</dt>
				<dd><a href="/slotGame.jsp?showtype=PT" target="_blank">PT</a></dd>
				<dd><a href="/slotGame.jsp?showtype=DT" target="_blank">DT</a></dd>
				<dd><a href="/slotGame.jsp?showtype=MGS" target="_blank">MG</a></dd>
				<dd><a href="/slotGame.jsp?showtype=PNG" target="_blank">PNG</a></dd>
				<dd><a href="/slotGame.jsp?showtype=TTG" target="_blank">TTG</a></dd>
				<dd><a href="/slotGame.jsp?showtype=NT" target="_blank">NT</a></dd>
				<dd><a href="/slotGame.jsp?showtype=QT" target="_blank">QT</a></dd>
				<c:choose>
					<c:when test="${session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER'}">
						<dd><a href="javascript:alert('您好，请先登录！');">AG</a></dd> 
					</c:when>
					<c:otherwise>
						<dd><a href="http://www.ag8game.com/gameAgPlatformLd.jsp" onclick="alert('请先在游戏帐号面前加ki_');" target="_blank">AG</a></dd> 
					</c:otherwise>
				</c:choose>
			</dl>
		</li>
		<li>
			<dl>
				<dt>新手教学</dt>
					<c:choose>
						<c:when test="${session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER'}">
						<dd><a href="/asp/payPage.aspx?showid=tab_deposit" target="_blank">我要存款</a></dd>
						<dd><a href="/asp/payPage.aspx?showid=tab_transfer" target="_blank">户内转账</a></dd>
						<dd><a href="/asp/payPage.aspx?showid=tab_withdraw" target="_blank">我要提款</a></dd>
						<dd><a href="/asp/payPage.aspx?showid=tab_card_binding" target="_blank">绑定银行卡</a></dd>
						<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">查询金额记录</a></dd>
						<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">查询游戏记录</a></dd>
						<dd><a href="javascript:;" onClick="alert('开始进行缓存清除，完毕后将会刷新网站！');window.location.reload(true);">清除缓存</a></dd>
						</c:when>
						<c:otherwise>
						<dd><a href="javascript:alert('您好，请先登录！');">我要存款</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">户内转账</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">我要提款</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">绑定银行卡</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">查询金额记录</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">查询游戏记录</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">清除缓存</a></dd>
						</c:otherwise>
					</c:choose>
			</dl>
			
		</li>
		<li>
			<dl>
				<dt>帐户清单</dt>
				<c:choose>
					<c:when test="${session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER'}">
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">提款记录</a></dd>
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">优惠记录</a></dd>
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">优惠券记录</a></dd>
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">在线支付记录</a></dd>
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">手工存款记录</a></dd> 
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">秒存附言记录</a></dd>
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">户内转账记录</a></dd>
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">自助返水记录</a></dd>
					 
					<dd><a href="/manageRecords.jsp?showid=j-record-form" target="_blank">好友推荐记录</a></dd>
					</c:when>
					<c:otherwise>
					<dd><a href="javascript:alert('您好，请先登录！');">提款记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">优惠记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">优惠券记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">在线支付记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">手工存款记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">秒存附言记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">户内转账记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">自助返水记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">自助优惠记录</a></dd>
					<dd><a href="javascript:alert('您好，请先登录！');">好友推荐记录</a></dd>
					</c:otherwise>
				</c:choose>
			</dl>
		</li> 
		<li>
			<dl>
				<dt>关于我们</dt>
				<dd><a href="/aboutus.jsp#tab-agreement" target="_blank">用户协议</a></dd>
				<dd><a href="/aboutus.jsp#tab-disclaimer" target="_blank">免责条款</a></dd>
				<dd><a href="/aboutus.jsp#tab-responsibility" target="_blank">博彩责任</a></dd>
				<dd><a href="/aboutus.jsp#tab-privacy" target="_blank">绝对隐私</a></dd>
			</dl>
		</li> 
		<li>
			<dl>
				<dt>下载中心</dt>
				<dd><a href="/phone.jsp" target="_blank">安卓APP</a></dd>
				<dd><a href="/phone.jsp" target="_blank">苹果APP</a></dd>
				<dd><a href="/phone.jsp#tab-pt" target="_blank">PT电脑客户端</a></dd>
				<dd><a href="/phone.jsp#tab-pt" target="_blank">PT手机客户端</a></dd>
				<dd><a href="javascript:alert('敬请期待');">DT手机客户端</a></dd>
				<dd><a href="/phone.jsp#tab-ag" target="_blank">AG手机客户端</a></dd>
				<dd><a href="/" target="_blank">天威微信公众号</a></dd>
				<dd><a href="https://get.adobe.com/cn/flashplayer/" target="_blank">FLASH插件</a></dd>
				<dd><a href="https://www.google.com/chrome/browser/desktop/index.html" target="_blank">谷歌浏览器</a></dd>

			</dl>
		</li> 
		<li>
			<dl>
				<dt>自助领取</dt>
				<c:choose>
					<c:when test="${session.customer!=null && session.customer.role eq 'MONEY_CUSTOMER'}">
						<dd><a href="/userCoupons.aspx?showid=tab-pt2" target="_blank">领取体验金</a></dd> 
					 
						<dd><a href="/userCoupons.aspx?showid=tab-persent" target="_blank">自助存送</a></dd>
						<dd><a href="/userCoupons.aspx?showid=tab-return" target="_blank">自助返水</a></dd>
						<dd><a href="/userCoupons.aspx?showid=tab-level" target="_blank">自助晋级</a></dd>
						<dd><a href="/userCoupons.aspx?showid=tab-help" target="_blank">领取救援金</a></dd>
						<dd><a href="/userCoupons.aspx?showid=tab-coupon" target="_blank">使用优惠券</a></dd>
					</c:when>
					<c:otherwise>
						<dd><a href="javascript:alert('您好，请先登录！');">领取体验金</a></dd>
						 
						<dd><a href="javascript:alert('您好，请先登录！');">自助存送</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">自助返水</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">自助晋级</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">领取救援金</a></dd>
						<dd><a href="javascript:alert('您好，请先登录！');">使用优惠券</a></dd>
					</c:otherwise>
				</c:choose>
				
			</dl>
		</li> 
	 </ul>
</body>
</html>
