<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<html>
	<head>
		<title>全部邮件</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/css/base.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/skin/admin.css" />
		<script type="text/javascript"
			src="${ctx}/js/jquery/jquery-1.7.2.min.js"></script>
		<script type="text/javascript"
			src="${ctx}/js/jquery/jquery-ui-1.8.21.custom.min.js"></script>
		<style type="text/css">
/*.search_margin{*/ /*margin-left:8px;*/ /*}*/
.label_search_td_play {
	font-family: Tahoma;
	font-size: 15px;
	/*font-size: 11px;*/
	line-height: 28px;
	font-weight: bold;
	/* text-align: center;*/
	text-transform: capitalize;
	color: #FFFFFF;
	text-decoration: none;
	padding-right: 1px;
}

.input {
	font-family: Tahoma;
	font-size: 18px;
	/*font-size: 11px;*/
	font-weight: normal;
	/*text-transform: capitalize;*/
	text-decoration: none;
	background-color: #FFFFFF;
	border: 1px solid #336699;
	line-height: 16px;
	height: 22px;
	float: left;
	margin-top: 2px;
}
</style>
		<script>
        function saveBook() {
            var content=$("#content").val();
            if(content==""){
               alert("回复信息不能为空！");
               return false;
            }
            if(content.length>255){
               alert("回复信息过长！");
               return false;
            }
            $("#mainform").submit();
            return true;
        }
        $(function () {
            $(".button_wxz").hover(function () {
                $(this).removeClass("button_wxz").addClass("button_xz");
            }, function () {
                $(this).removeClass("button_xz").addClass("button_wxz");
            });
        });
    </script>
	</head>
	<body leftmargin="8" topmargin='8'>
		<table width="98%" align="center" border="0" cellpadding="4"
			cellspacing="1" bgcolor="#CBD8AC"
			style="margin-bottom: 8px; margin-top: 8px;">
			<tr>
				<td background="${ctx}/skin/images/frame/wbg.gif" bgcolor="#EEF4EA"
					colspan="2" class='title' style="text-align: left;">
					<span></span>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td height="20" valign="top" style="text-align: left;">
					<table width="98%" align="center" border="0" cellpadding="4"
						cellspacing="1" bgcolor="#CBD8AC" style="margin-bottom: 8px">
						<tr bgcolor="#FFFFFF">
							<td rowspan="2" width="165px;" valign="top">
								<c:if test="${guestbook.isadmin==0}">
									<div style="margin-top: 4px;">
										<img src="${ctx}/images/admin/2010621194621928.png"
											height="180" width="165" />
									</div>
									<div style="margin-top: 4px;">
										账户：客服管理员
									</div>
									<div style="margin-top: 10px;">
										时间：${guestbook.createdate}
									</div>
								</c:if>
								<c:if test="${guestbook.isadmin==1}">
									<div style="margin-top: 4px;">
										<img src="${ctx}/images/admin/5bce21d8dde0161fba0380c55bd1f512.png"
											height="180" width="165" />
									</div>
									<div style="margin-top: 4px;">
										账户：${guestbook.username}
									</div>
									<div style="margin-top: 10px;">
										时间：${guestbook.createdate}
									</div>
								</c:if>
							</td>
							<td height="30" colspan="2" valign="middle">
								&nbsp;&nbsp;&nbsp;&nbsp;${guestbook.title}
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td colspan="2" valign="top">
								&nbsp;&nbsp;&nbsp;&nbsp;${guestbook.content}
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<c:if test="${list!=null}">
				<c:forEach items="${list}" var="s" varStatus="itemIndex">
					<c:if test="${s.id!=null}">
						<tr bgcolor="#FFFFFF">
							<td height="20" valign="top" style="text-align: left;">
								<table width="98%" align="center" border="0" cellpadding="4"
									cellspacing="1" bgcolor="#CBD8AC" style="margin-bottom: 8px">
									<tr bgcolor="#FFFFFF">
										<td rowspan="2" width="165px;" valign="top">
											<c:if test="${s.isadmin==0}">
												<div style="margin-top: 4px;">
													<img
														src="${ctx}/images/admin/2010621194621928.png"
														height="180" width="165" />
												</div>
												<div style="margin-top: 4px;">
													账户：客服管理员
												</div>
												<div style="margin-top: 10px;">
													时间：${s.createdate}
												</div>
											</c:if>
											<c:if test="${s.isadmin==1}">
												<div style="margin-top: 4px;">
													<img
														src="${ctx}/images/admin/5bce21d8dde0161fba0380c55bd1f512.png"
														height="180" width="165" />
												</div>
												<div style="margin-top: 4px;">
													账户：${s.username}
												</div>
												<div style="margin-top: 10px;">
													时间：${s.createdate}
												</div>
											</c:if>
										</td>
										<td height="30" colspan="2" valign="middle">
											&nbsp;&nbsp;&nbsp;&nbsp;${s.title}
										</td>
									</tr>
									<tr bgcolor="#FFFFFF">
										<td colspan="2" valign="top">
											&nbsp;&nbsp;&nbsp;&nbsp;${s.content}
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>
		</table>
	</body>
</html>