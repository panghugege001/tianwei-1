<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<base href="<%=basePath%>">
		<%@page import="dfh.remote.RemoteCaller"%>
		<%@page import="dfh.utils.Constants"%>
		<%@page import="dfh.model.Users"%>
		<%@page import="java.util.Date"%>
		<%@page import="dfh.utils.DateUtil"%>
		<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
		<title>存入资金</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<link type="text/css" rel="stylesheet" href="yilufa.css" />
		<style type="text/css">
<!--
body {
	background-color: #0d0213;
}
-->
</style>
	</head>
<script type="text/javascript">
function radioShow(){
	var myradio=document.getElementsByName("myradio");
	var div=document.getElementById("c").getElementsByTagName("div");
	for(i=0;i<div.length;i++){
		if(myradio[i].checked){
			div[i].style.display="block";
			}
			else{
			div[i].style.display="none";
			}
		}
	}
</script>
	<body>
		<%
			HttpSession chksession = request.getSession(true);
			Users user = (Users) chksession.getValue("customer");

			if (user != null) {
				user = AxisSecurityEncryptUtil.getUser(user.getLoginname());
				if (user != null) {
					session.setAttribute(Constants.SESSION_CUSTOMERID, user);
				}
			}

			if (user == null) {
		%>
		<script language="javascript">
			alert("你的登录已过期，请从首页重新登录");
		</script>
		<%
			response.sendRedirect("login.jsp");
			}
		%>
		<%-- <div id="managementcontent_s" style="width: 800px;">
			<s:include value="cwtop.jsp"></s:include>
			<!--header-->
			<div style="margin-bottom: 20px;margin-top: 10px;margin-left: 40px;">
				<fieldset>
					<label for="r1" class="red">
						<input name="myradio" id="r1" type="radio" value=""
							checked="checked" onclick="radioShow();" />
						<img src="../images/bank_gs.jpg" />
					</label>
					<label for="r2" class="red">
						<input name="myradio" id="r2" type="radio" value=""
							onclick="radioShow();" />
						<img src="../images/bank_zs.jpg" />
					</label>
					<label for="r3" class="red">
						<input name="myradio" id="r3" type="radio" value=""
							onclick="radioShow();" />
						<img src="../images/bank_js.jpg" />
					</label>
					<label for="r4" class="red">
						<input name="myradio" id="r4" type="radio" value=""
							onclick="radioShow();" />
						<img src="images/bank_nh.jpg" />
					</label>
					<div id="c">
						<c:forEach items="${bankinfo}" var="s" varStatus="itemIndex">
							<c:if test="${s.bankname eq '工商银行'}">
								<div class="c1">
									账户名：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.username}</span>
									EMAIL帐号：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.accountno}</span>
									用户帐号:
									<span
										style="font-size: 26px; color: red; text-decoration: none">${session.customer.loginname}</span>
									<p>
										操作步骤：
										<br />
										1 请您登录工行网银
										<a href="http://www.icbc.com.cn" target="_blank">www.icbc.com.cn</a>，点击左侧个人网银用户，输入帐号和密码进行登录，点击转账汇款按钮，选择EMAIL汇款。输入e68的收款卡姓名和账户EMAIL，您存款的金额以及验证码及在附言输入
										<span
											style="font-size: 20px; color: red; text-decoration: none">${session.customer.loginname}</span>，确定，完成支付，稍等两分钟您就可以查到您的存款记录了。
										<br />
										<br />
										2
										不接受跨行转账。建议您去办理一张工行卡，存款取款都会非常方便快捷。如果您暂时还没有开通工行网银，建议您去工行柜台或者工行ATM机进行转账。
										<br />
										<br />
										3
										附言写错游戏账户或忘了写游戏账户，请您把你的游戏账户及该笔存款的回单或相关信息，提交给我们的客服人员，我们财务人员将进行补单或改单处理，处理后我们将第一时间通知到您。
										<br />
										<br />
										<br />
									<p>
								</div>
							</c:if>
							<c:if test="${s.bankname eq '招商银行'}">
								<div class="c2" style="display: none;">
									账户名：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.username}</span>
									银行帐号：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.accountno}</span>
									<br />
									用户帐号:
									<span
										style="font-size: 26px; color: red; text-decoration: none">${session.customer.loginname}</span>
									<p>
										操作步骤：
										<br />
										1 请您登录招行网银
										<a href="http://www.cmbchina.com" target="_blank">www.cmbchina.com</a>，点击个人网银用户，输入帐号和密码进行登录，点击转账汇款菜单，选择&nbsp;招行同城转账或者招行异地汇款。输入e68的收款卡姓名和银行帐号，您存款的金额以及验证码及在备注输入
										<span
											style="font-size: 20px; color: red; text-decoration: none">${session.customer.loginname}</span>，确定，完成支付，稍等两分钟您就可以查到您的存款记录了。
										<br />
										<br />
										2
										不接受跨行转账。建议您去办理一张招行卡，存款取款都会非常方便快捷。如果您暂时还没有开通招行网银，建议您去招行柜台或者招行ATM机进行转账。
										<br />
										<br />
										3
										附言写错游戏账户或忘了写游戏账户，请您把你的游戏账户及该笔存款的回单或相关信息，提交给我们的客服人员，我们财务人员将进行补单或改单处理，处理后我们将第一时间通知到您。
										<br />
										<br />
										<br />
									<p>
								</div>
								<div class="c3" style="display: none;">
									账户名：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.username}</span>
									银行帐号：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.accountno}</span>
									<br />
									开户行:
									<span
										style="font-size: 26px; color: red; text-decoration: none">招商银行</span>&nbsp;&nbsp;&nbsp;用户帐号:
									<span
										style="font-size: 26px; color: red; text-decoration: none">${session.customer.loginname}</span>
									<p>
										操作步骤：
										<br />
										1 请您登录建行网银
										<a href="http://www.ccb.com" target="_blank">www.ccb.com</a>，点击个人网银用户，输入帐号和密码进行登录，点击转账汇款菜单，选择
										<span
											style="font-size: 20px; color: red; text-decoration: none">建行(加急)</span>汇款。输入e68的收款卡姓名和银行帐号以及
										<span
											style="font-size: 20px; color: red; text-decoration: none">收款账户开户行(招商银行)</span>，您存款的金额以及验证码及在附言输入
										<span
											style="font-size: 20px; color: red; text-decoration: none">${session.customer.loginname}</span>，确定，完成支付，稍等两分钟您就可以查到您的存款记录了。
										<br />
										<br />
										2
										不接受跨行转账。建议您去办理一张建行卡，存款取款都会非常方便快捷。如果您暂时还没有开通建行网银，建议您去建行柜台或者建行ATM机进行转账。
										<br />
										<br />
										3
										附言写错游戏账户或忘了写游戏账户，请您把你的游戏账户及该笔存款的回单或相关信息，提交给我们的客服人员，我们财务人员将进行补单或改单处理，处理后我们将第一时间通知到您。
										<br />
										<br />
										<br />
									<p>
								</div>
							</c:if>
							<c:if test="${s.bankname eq '农业银行'}">
								<div class="c4" style="display: none;">
									账户名：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.username}</span>
									银行帐号：
									<span
										style="font-size: 16px; color: red; text-decoration: none">${s.accountno}</span>
									<br />
									开户网点:
									<span
										style="font-size: 26px; color: red; text-decoration: none">中国农业银行</span>&nbsp;&nbsp;&nbsp;4位随机码:
									<span
										style="font-size: 26px; color: red; text-decoration: none">${session.customer.randnum}</span>
									<p>
										操作步骤：
										<br />
										1 请您登录农行网银
										<a href="http://www.abchina.com" target="_blank">www.abchina.com</a>，点击左侧的个人网上银行登录，选择证书登录或者用户登录，（选择证书登陆：输入K令密码；选择用户名登陆：输入登陆用户名和密码；）成功登录后，点击转账汇款菜单，选择单笔转账
										。如果是首次转账，需要点击新增收款方（输入e68的收款卡姓名和银行帐号以及
										<br />
										<span
											style="font-size: 20px; color: red; text-decoration: none">收款账户开户行：中国农业银行<br />账户性质：借记卡
											<br />收款人帐号所在省：广东省</span>
										<br />
										），填写交易信息后,点击提交保存。请您输入存款的金额及在转账用途中输入4位随机码：
										<span
											style="font-size: 20px; color: red; text-decoration: none">${session.customer.randnum}</span>，确定，完成支付，稍等两分钟您就可以查到您的存款记录了。
										<br />
										<br />
										2
										不接受跨行转账。建议您去办理一张农行银行卡，存款取款都会非常方便快捷。如果您暂时还没有开通农行网银，建议您去农行柜台或者农行ATM机进行转账。
										<br />
										<br />
										3
										附言写错游戏账户或忘了写游戏账户，请您把你的游戏账户及该笔存款的回单或相关信息，提交给我们的客服人员，我们财务人员将进行补单或改单处理，处理后我们将第一时间通知到您。
										<br />
										<br />
										<br />
									<p>
								</div>

							</c:if>
						</c:forEach>
					</div>
				</fieldset>
			</div>
		</div> --%>
	</body>
</html>
