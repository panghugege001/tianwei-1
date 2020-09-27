<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<html>
	<head>
		<title>全部邮件</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/css/base.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/css/main.css" />
		<script type="text/javascript"
			src="${ctx}/js/jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/js/jquery/jquery-ui-1.8.21.custom.min.js"></script>
		<script>
        function findAllName() {
            $("#formMovieName").submit();
        }
        function functionHomeInfo() {
            $("#homeInfo").submit();
            return false;
        }
        function functionPreviousInfo() {
            $("#previousInfo").submit();
            return false;
        }
        function functionNextInfo() {
            $("#nextInfo").submit();
            return false;
        }
        function functionEndInfo() {
            $("#endInfo").submit();
            return false;
        }
        function functionUpdateInfo(count) {
            $("#updateInfo" + count).submit();
            return false;
        }
        function functionDeleteInfo(count) {
            $("#deleteInfo" + count).submit();
            return false;
        }
    </script>
	</head>
	<body leftmargin="8" topmargin='8'>
		<table width="98%" align="center" border="0" cellpadding="4"
			cellspacing="1" bgcolor="#CBD8AC"
			style="margin-bottom: 8px; margin-top: 8px;">
			<tr>
				<td background="${ctx}/skin/images/frame/wbg.gif" bgcolor="#EEF4EA"
					class='title' style="text-align: center">
					<span>序号</span>
				</td>
				<td background="${ctx}/skin/images/frame/wbg.gif" bgcolor="#EEF4EA"
					class='title' style="text-align: center">
					<span>标题</span>
				</td>
				<td background="${ctx}/skin/images/frame/wbg.gif" bgcolor="#EEF4EA"
					class='title' style="text-align: center">
					发信人
				</td>
				<td background="${ctx}/skin/images/frame/wbg.gif" bgcolor="#EEF4EA"
					class='title' style="text-align: center">
					<span>时间</span>
				</td>
				<td background="${ctx}/skin/images/frame/wbg.gif" bgcolor="#EEF4EA"
					class='title' style="text-align: center">
					<span>详情</span>
				</td>
			</tr>
			<c:forEach items="${list}" var="s" varStatus="itemIndex">
				<tr bgcolor="#FFFFFF">
					<td height="20" valign="middle" align="center">
						${itemIndex.count}
					</td>
					<td height="20" valign="middle" align="center">
						<a href="${ctx}/asp/bookRead.aspx?guestbook.id=${s.id}"
							target="I1">${s.title}</a>
					</td>
					<td height="20" valign="middle" align="center">
						<c:if test="${s.isadmin eq 1}">
						   ${s.username}
						</c:if>
						<c:if test="${s.isadmin eq 0}">
							客服管理员
						</c:if>
					</td>
					<td height="20" valign="middle" align="center">
						${s.createdate}
					</td>
					<td height="20" valign="middle" align="center">
						<a href="${ctx}/asp/bookMessageRead.aspx?guestbook.id=${s.id}"
							target="I1">查看</a>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="10" background="${ctx}/skin/images/frame/wbg.gif"
					bgcolor="#EEF4EA" class='title' align="center">
					<table align="center" width="350px">
						<tr>
							<td height="20px;">
								<c:if test="${page>1}">
									<form action="${ctx}/asp/bookfindAll.aspx" method="get"
										name="previousInfo" id="previousInfo">
										<input type="hidden" class="input" style="width: 400px;"
											name="page" value="${page-1}" />
									</form>
									<a href="javaScript:void(0);"
										onclick="return functionPreviousInfo();" title="上一页"><font
										color="#00000">上一页</font> </a>
								</c:if>
								<c:if test="${page<=1}">
									<font color="#00000">上一页</font>
								</c:if>
							</td>
							<td height="20px;">
								<c:if test="${page < countPage}">
									<form action="${ctx}/asp/bookfindAll.aspx" method="get"
										name="nextInfo" id="nextInfo">
										<input type="hidden" class="input" style="width: 400px;"
											name="page" value="${page+1}" />
									</form>
									<a href="javaScript:void(0);"
										onclick="return functionNextInfo();" title="下一页"><font
										color="#00000">下一页</font> </a>
								</c:if>
								<c:if test="${page >=  countPage}">
									<font color="#00000">下一页</font>
								</c:if>
							</td>
							<td height="20px;">
								<form action="${ctx}/asp/bookfindAll.aspx" method="get"
									name="homeInfo" id="homeInfo">
									<input type="hidden" class="input" style="width: 400px;"
										name="page" value="1" />
								</form>
								<a href="javaScript:void(0);"
									onclick="return functionHomeInfo();" title="首页"><font
									color="#00000">首页</font> </a>
							</td>
							<td height="20px;">
								<form action="${ctx}/asp/bookfindAll.aspx" method="get"
									name="endInfo" id="endInfo">
									<input type="hidden" class="input" style="width: 400px;"
										name="page" value="${countPage}" />
								</form>
								<a href="javaScript:void(0);"
									onclick="return functionEndInfo();" title="尾页"><font
									color="#00000">尾页</font> </a>
							</td>
							<td height="20px;">
								<font color="#00000">第${page}页</font>
							</td>
							<td height="20px;">
								<font color="#00000">共${countPage}页</font>
							</td>
							<td height="20px;">
								<font color="#00000">共${countAllTwo}条</font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>