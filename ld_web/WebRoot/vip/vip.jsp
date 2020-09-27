<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/vheaderCommon.jsp"></jsp:include>

<!DOCTYPE html>
<html>
    <head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes, minimum-scale=1, maximum-scale=1.0"> 
		<title>VIP召集令</title>
		<link rel="stylesheet" href="css/index.css?v=333" />
		<link rel="stylesheet" href="css/bootstrap.min.css?v=1" /> 
			<jsp:include page="${ctx}/tpl/linkResource.jsp?v=1"></jsp:include> 
    </head>
	<body class="center_body"> 
		<jsp:include page="${ctx}/tpl/activety_header.jsp"></jsp:include>
		<div class="">
			<div class="null"></div>
			<div class="vip_beijing">
				<div class="container container_clovoe" style="margin-top: 30px;">	
					<div class="text_box">
						<p>
							活动对象：天威所有会员
						</p>
						<br />
						<p>
							活动时间:2019年1月1日起
						</p>
						<br />
						<p>
							活动内容:当日存款满足以下条件，即可联系24小时在线客服申请晋级，让您快速体验尊贵VIP。						
						</p>
					</div>
					<div class="table_box">
						<table cellpadding="0" cellspacing="0" border="1">
							<tr>
								<td>存款要求</td>
								<td>投注额要求</td>
								<td>当周累计存款</td>
							</tr>
							<tr>
								<td>单日1万以上</td>
								<td>5万</td>
								<td>天将</td>
							</tr>
							<tr>
								<td>单日3万以上</td>
								<td>15万</td>
								<td>天王</td>
							</tr>
							<tr>
								<td>单日8万以上</td>
								<td>40万</td>
								<td>星君</td>
							</tr>
							<tr>
								<td>单日20万以上</td>
								<td>100万</td>
								<td>真君</td>
							</tr>
							<tr>
								<td>单日50万以上</td>
								<td>250万</td>
								<td>仙君</td>
							</tr>
							<tr>
								<td>单日100万以上</td>
								<td>500万</td>
								<td>帝君</td>
							</tr>							
						</table>
						<br />
						<p>活动规则</p>
						<p>
							１.VIP晋级后立即享有当前等级之福利。<br />
							２.本优惠活动只针对娱乐性质的会员，如发现用户拥有超过一个账户，包括同一姓名，同一邮箱，同一/相似IP地址，同一住址，同一银行卡，同一电脑等其他任何不正常行为，我们将有权冻结账号并收回盈利。<br />
							３.本活动天威娱乐享有最终解释权。
						</p>
					</div>
				</div>				
			</div>

		</div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	</body>
</html>