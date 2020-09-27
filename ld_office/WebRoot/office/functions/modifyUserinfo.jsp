<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="dfh.security.EncryptionUtil"%>
<%@include file="/office/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看用户基本信息</title>
<link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/jquery/jquery-ui-1.8.21.custom.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.8.21.custom.min.js"></script>

	<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
<script type="text/javascript">
	function changeE68Exten(num) {
		var href = document.getElementById("_call2").href;
		if (href) {
			var replaceStr = href.substring(0, href.length - 4) + num;
			document.getElementById("_call2").href = replaceStr;
		}
	}
	function changeQyExten(num) {
		if (num > 800000) {
			var hrefTwo = document.getElementById("_callTwo").href;

			var hrefThree = document.getElementById("_callThree").href;
			var hrefFour = document.getElementById("_callFour").href;

			var hrefFive = document.getElementById("_callFive").href;
			var hrefSix = document.getElementById("_callSix").href;

			var hrefSev = document.getElementById("_callSev").href;
			
			var hrefEig = document.getElementById("_callEig").href;

			if (hrefTwo) {
				var arr = hrefTwo.split("ext_no=");
				var replaceStr = arr[0] + "ext_no=" + num;
				document.getElementById("_callTwo").href = replaceStr;
			}

			if (hrefThree) {
				var arr = hrefThree.split("ext_no=");
				var replaceStrThree = arr[0] + "ext_no=" + num;
				document.getElementById("_callThree").href = replaceStrThree;
			}

			if (hrefFour) {
				var arr = hrefFour.split("ext_no=");
				var replaceStrFour = arr[0] + "ext_no=" + num;
				document.getElementById("_callFour").href = replaceStrFour;
			}

			if (hrefFive) {
				var arr = hrefFive.split("ext_no=");
				var replaceStrFive = arr[0] + "ext_no=" + num;
				document.getElementById("_callFive").href = replaceStrFive;
			}
			if (hrefSix) {
				var arr = hrefSix.split("ext_no=");
				var replaceStrSix = arr[0] + "ext_no=" + num;
				document.getElementById("_callSix").href = replaceStrSix;
			}

			if (hrefSev) {
				var arr = hrefSev.split("ext_no=");
				var replaceStrSev = arr[0] + "ext_no=" + num;
				document.getElementById("_callSev").href = replaceStrSev;
			}
			
			if (hrefEig) {
				var arr = hrefEig.split("ext_no=");
				var replaceStrEig = arr[0] + "ext_no=" + num;
				document.getElementById("_callEig").href = replaceStrEig;
			}

			return;
		} else if (num > 800 && num < 800000) {
			var hrefTwo = document.getElementById("_callTwo").href;

			var hrefThree = document.getElementById("_callThree").href;
			var hrefFour = document.getElementById("_callFour").href;
			var hrefFive = document.getElementById("_callFive").href;
			var hrefSix = document.getElementById("_callSix").href;
			var hrefSev = document.getElementById("_callSev").href;
			var hrefEig = document.getElementById("_callEig").href;
			if (hrefTwo) {
				var arr = hrefTwo.split("ext_no=");
				var replaceStr = arr[0] + "ext_no=" + num;
				document.getElementById("_callTwo").href = replaceStr;
			}

			if (hrefThree) {
				var arr = hrefThree.split("ext_no=");
				var replaceStrThree = arr[0] + "ext_no=" + num;
				document.getElementById("_callThree").href = replaceStrThree;
			}

			if (hrefFour) {
				var arr = hrefFour.split("ext_no=");
				var replaceStrFour = arr[0] + "ext_no=" + num;
				document.getElementById("_callFour").href = replaceStrFour;
			}

			if (hrefFive) {
				var arr = hrefFive.split("ext_no=");
				var replaceStrFive = arr[0] + "ext_no=" + num;
				document.getElementById("_callFive").href = replaceStrFive;
			}
			if (hrefSix) {
				var arr = hrefSix.split("ext_no=");
				var replaceStrSix = arr[0] + "ext_no=" + num;
				document.getElementById("_callSix").href = replaceStrSix;
			}
			if (hrefSev) {
				var arr = hrefSev.split("ext_no=");
				var replaceStrSev = arr[0] + "ext_no=" + num;
				document.getElementById("_callSev").href = replaceStrSev;
			}
			
			if (hrefEig) {
				var arr = hrefEig.split("ext_no=");
				var replaceStrEig = arr[0] + "ext_no=" + num;
				document.getElementById("_callEig").href = replaceStrEig;
			}

			// return ; 
		}
		///var href = document.getElementById("_call").href;
		///if(href){
		///var replaceStr = href.substring(0,href.length-3)+num;
		///document.getElementById("_call").href = replaceStr;
		///}
		var hrefOne = document.getElementById("_callOne").href;
		if (hrefOne) {
			var replaceStr = hrefOne.substring(0, hrefOne.length - 3) + num;
			document.getElementById("_callOne").href = replaceStr;
		}
	}
</script>
</head>
<body>
<div id="JqAlert" title="温馨提示"></div>

	<div id="excel_menu_left">操作 --&gt; 查看用户基本信息</div>
	<table align="left" border="0">
		<tr>
			<td>当前会员账号:</td>
			<td><s:property value="%{#request.user.loginname}" /></td>
		</tr>

		<tr>
			<td>用户ID:</td>
			<td><s:property value="%{#request.user.id}" /></td>
		</tr>
		<s:if test="%{#request.user.role == 'AGENT'}">
			<tr>
				<td>首存数</td>
				<td><s:property value="%{#request.countProxyFirst}" /></td>
			</tr>
		</s:if>
		<tr>
			<td>用户随机数:</td>
			<td><s:property value="%{#request.user.randnum}" /></td>
		</tr>

		<tr>
			<td>昵称:</td>
			<td><s:property value="%{#request.user.aliasName}" /></td>
		</tr>
		<tr>
			<td>真实姓名:</td>
			<td>
				<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
					<s:property value="%{#request.user.accountName}" />
				</s:if>
				<s:else>
					<s:property value="%{#request.user.accountName.substring(0,1)+'**'}" />
				</s:else> --%> <s:property value="%{#request.user.accountName}" />
			</td>
		</tr>
		<c:if test="${sessionScope.operator.username=='luis' or ( sessionScope.operator.cs eq requestScope.user.intro && fn:contains(sessionScope.operator.cs, 'ts'.toString()))}">
			<tr>
				<td>手机:</td>
				<td><s:property value="%{#request.user.phone}" /></td>
			</tr>
		</c:if>
		<c:if test="${sessionScope.operator.username=='luis' or (sessionScope.operator.agent ne null  && not empty requestScope.user.agent  && fn:contains(sessionScope.operator.agent, requestScope.user.agent)) or ( sessionScope.operator.cs eq requestScope.user.intro && fn:contains(sessionScope.operator.cs, 'ts'.toString()))}">
			<tr>
				<td>Email:</td>
				<td><s:property value="%{#request.user.email}" /></td>
			</tr>
			<tr>
				<td>微信:</td>
				<td><s:property value="%{#request.user.microchannel}" /></td>
			</tr>
			<tr>
				<td>QQ:</td>
				<td><s:property value="%{#request.user.qq}" /></td>
			</tr>
		</c:if>
		<c:if
			test="${sessionScope.operator.authority ne 'sale' 
					and sessionScope.operator.authority ne 'finance' and sessionScope.operator.authority ne 'finance_manager'
					and sessionScope.operator.authority ne 'market' and sessionScope.operator.authority ne 'qc' and sessionScope.operator.authority ne 'market_manager'
				}">
			
			<tr>
				<td>邮寄地址:</td>
				<td><s:property value="%{#request.user.mailaddress}" /></td>
			</tr>
		</c:if>
		<tr>
			<td>座机号1： <c:if
					test="${sessionScope.operator.phonenoBL eq null || sessionScope.operator.phonenoBL eq ''
				}">

					<select onchange="changeQyExten(this.value);">
						<option value="801">801</option>
						<option value="802">802</option>
						<option value="803" selected="selected">803</option>
						<option value="804">804</option>
						<option value="805">805</option>
						<option value="806">806</option>
						<option value="807">807</option>
						<option value="808">808</option>
						<option value="809">809</option>
						<option value="810">810</option>
						<option value="811">811</option>
						<option value="812">812</option>
						<option value="813">813</option>

						<option value="814">814</option>
						<option value="815">815</option>
						<option value="816">816</option>
						<option value="817">817</option>
						<option value="818">818</option>
						<option value="819">819</option>
						<option value="820">820</option>
						<option value="821">821</option>
						<option value="822">822</option>

						<c:forEach var="i" begin="333101" end="333140" step="1">
							<option value="${i}">${i}</option>
						</c:forEach>

						<c:forEach var="i" begin="888101" end="888130" step="1">
							<option value="${i}">${i}</option>
						</c:forEach>
						<option value=801>比邻801</option>
						<option value=802>比邻802</option>
						<option value=803>比邻803</option>
						<option value=804>比邻804</option>
						<option value=805>比邻805</option>
						<option value=806>比邻806</option>
						<option value=807>比邻807</option>
						<option value=808>比邻808</option>
						<option value=809>国信809</option>
						<option value=810>国信810</option>
						<option value=812>国信812</option>
					</select>
					&nbsp;&nbsp;
				</td>
			<td>
				<%
					dfh.model.Users user = (dfh.model.Users) request.getAttribute("user");
						String phone = user.getPhone();
						if (phone != null && !"".equals(phone)) {
							try {
								long l = Long.parseLong(phone);
								String p = "1" + String.valueOf(l);
								l = Long.parseLong(p) * 11 + 159753;
								request.setAttribute("encryptPhone", l);
				%> <!-- <a id="_call"
						href="http://192.168.0.8:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
						target="_blank">呼叫该用户(内)</a>
						<a id="_call"
						href="http://115.29.227.189:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
						target="_blank">呼叫该用户(外)</a>
						
						 
				--> 
				<%-- <a id="_callSix"
				href="http://210.51.190.17/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank">CALL(比邻-电销888)</a>  
				<a id="_callSev"
				href="http://203.177.51.163/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank">CALL(比邻-电销333)</a>
				<a id="_callFive"
				href="http://220.229.225.21:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank"> CALL(比邻-8XX)</a> 
				<a id="_callEig"
				href="http://220.229.225.20/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank">CALL(比邻-666)</a> --%>
					<a id="_call15"
					   href="http://209.9.53.206:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
					   target="_blank">CALL(新电服)(官网)</a>
					<a id="_call15"
					   href="http://69.172.81.117:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
					   target="_blank">CALL(新电服)</a>
					<a id="_call15"
					   href="http://69.172.81.111:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
					   target="_blank">CALL(电销1)</a>
					<a id="_call15"
					   href="http://69.172.81.113:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
					   target="_blank">CALL(电销2)</a>
				
				
				<a id="_callOne"
				style="visibility: hidden;"
				href="http://47.90.12.131:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
				target="_blank">CALL(国信)</a>
				<a id="_callThree"
				style="visibility: hidden;"
				href="http://123.1.186.200/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank">CALL(比邻-外网)</a>
				<a id="_callTwo" style="display: none;"
				href="http://203.88.165.54/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank">CALL(已废弃)</a> 	
				<a id="_callFour"
				style="display: none;"
				href="http://192.168.100.5/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
				target="_blank">CALL(比邻-内网)</a>
				 <%
 	} catch (Exception e) {
 %> 该用户电话号码格式异常! <%
 	}
 		} else {
 %> 无该用户电话信息 <%
 	}
 %> </c:if> <c:if
					test="${sessionScope.operator.phonenoBL ne  null && sessionScope.operator.phonenoBL ne  ''
				}">
					<td>
						比邻:${sessionScope.operator.phonenoBL};国信:${sessionScope.operator.phonenoGX}
						<%
							dfh.model.Users user = (dfh.model.Users) request.getAttribute("user");
								String phone = user.getPhone();
								if (phone != null && !"".equals(phone)) {
									try {
										long l = Long.parseLong(phone);
										String p = "1" + String.valueOf(l);
										l = Long.parseLong(p) * 11 + 159753;
										request.setAttribute("encryptPhone", l);
						%> 
						<!-- 
						<a id="_call" href="http://192.168.0.10:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=903"
						target="_blank">呼叫该用户(内)</a> --> 
						<!--<a id="_call" href="http://115.29.227.189:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=603"
						target="_blank">呼叫该用户(外)</a>
						--> 
						<%-- <a id="_callSix" href="http://210.51.190.17/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(比邻-电销888)</a>
						<a id="_callSev" href="http://203.177.51.163/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(比邻-电销333)</a>
						<a id="_callNine"
						href="http://192.168.81.112:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(电服1)</a>
						<a id="_callTen"
						href="http://192.168.81.111:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(电销1)</a>
						<a id="_callEleven"
						href="http://192.168.81.113:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(电销2)</a> --%>
			<a id="_call15"
			   href="http://209.9.53.206:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
			   target="_blank">CALL(新电服)(官网)</a>
			<a id="_call15"
			   href="http://69.172.81.117:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
			   target="_blank">CALL(新电服)</a>
			<a id="_call15"
			   href="http://69.172.81.111:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
			   target="_blank">CALL(电销1)</a>
			<a id="_call15"
			   href="http://69.172.81.113:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
			   target="_blank">CALL(电销2)</a>
						
						<a id="_callFive"href="http://220.229.225.21:8099/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(比邻-8XX)</a> 
						<a id="_callEig" href="http://220.229.225.20/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(比邻-666)</a>
						
						<a id="_callOne" style="visibility: hidden;" href="http://47.90.12.131:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=${sessionScope.operator.phonenoGX}"
						target="_blank">CALL(国信)</a> 
						<a id="_callThree" style="visibility: hidden;" href="http://123.1.186.200/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(比邻-外网)</a>
						<a id="_callTwo" style="display: none;" href="http://203.88.165.54/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(已废弃)</a>
						<a id="_callFour" style="display: none;"href="http://192.168.100.5/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phonenoBL}"
						target="_blank">CALL(比邻-内网)</a>
						<%
 	} catch (Exception e) {
 %> 该用户电话号码格式异常! <%
 	}
 		} else {
 %> 无该用户电话信息 <%
 	}
 %>
					
				</c:if>
			</td>
		</tr>
		<!--
			<tr>
				<td>
					座机号2：
					<select onchange="changeE68Exten(this.value);">
						<option value="8001" selected="selected">
							8001
						</option>
						<option value="8002" selected="selected">
							8002
						</option>
						<option value="8003" selected="selected">
							8003
						</option>
						<option value="8004">
							8004
						</option>
						<option value="8005">
							8005
						</option>
						<option value="8006">
							8006
						</option>
						<option value="8007">
							8007
						</option>
						<option value="8008">
							8008
						</option>
						<option value="8009">
							8009
						</option>
						<option value="8010">
							8010
						</option>
					</select>
					&nbsp;&nbsp;
				</td>
				<td>
					<%dfh.model.Users user2 = (dfh.model.Users) request.getAttribute("user");
			String phone2 = user2.getPhone();
			if (phone2 != null && !"".equals(phone2)) {
				try {
					phone2 = EncryptionUtil.encryptBASE64(phone2.getBytes());
					request.setAttribute("encryptPhone2", phone2);%>
					<a id="_call2"
						href="http://192.168.0.160/cc/api/webdial/?call=${requestScope.encryptPhone2}&encrypt=yes&prefix=&exten=8003"
						target="_blank">呼叫该用户</a>
					<%} catch (Exception e) {%>
					该用户电话号码格式异常!
					<%}
			} else {%>
					无该用户电话信息
					<%}%>
				</td>
			</tr> -->
		<tr>
			<td>生日:</td>
			<td><s:property value="%{#request.user.birthday}" /></td>
		</tr>
		<tr>
			<td>代理网址:</td>
			<td><s:property value="%{#request.user.referWebsite}" /></td>
		</tr>
		
		<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'market_manager'|| sessionScope.operator.authority eq 'sale_manager'}">
			<tr>
				<td>代理agcode:</td>
				<td><s:property value="%{#request.user.agcode}" /></td>
			</tr>
			<tr>
				<td>来源网址:</td>
				<td><s:property value="%{#request.user.howToKnow}" /></td>
			</tr>
		</c:if>
		
		<tr>
			<td>备注:</td>
			<td><s:property value="%{#request.user.remark}" /></td>
		</tr>
		<tr>
			<td>客户操作系统:</td>
			<td><s:property value="%{#request.user.clientos}" /></td>
		</tr>
		<c:if test="${requestScope.user.role eq 'MONEY_CUSTOMER'}">
				<c:if test="${sessionScope.operator.authority eq 'boss' || sessionScope.operator.authority eq 'market_manager' || sessionScope.operator.username eq  'yan751028'}">
					<tr style="font-weight: bold">
						<td>
							修改上级代理为:
						</td>
						<td>
							<input type="text" id="_modifyAgent" value="<s:property value="%{#request.user.agent}"/>"/>
							<input type="button" value="提交" id="_modifyCommit"/>
						</td>
					</tr>
				</c:if>
			</c:if>
		<tr style="color: red; font-weight: bold">
			<td>操作记录:</td>
			<td>
				<s:form action="queryOperationLog" namespace="/office"
						name="mainform" id="mainform" theme="simple" target="_blank">
					<input name="start" type="hidden" value="2017-01-01 00:00:00" />
					<input name="type" type="hidden" value="SETWARNLEVEL" />
					<input name="end" type="hidden" value="${endTime}" />
					<input name="size" type="hidden" value="20" />
					<input name="remark" type="hidden" value="将会员${loginname}的警告等级从" />
					<input name="loginname" type="hidden" value="" />
					<s:hidden name="pageIndex" value="1"></s:hidden>
					<s:submit value="警告操作记录"></s:submit>
				</s:form>
				<s:form action="queryActionLog" namespace="/office"
						name="mainform" id="mainform" theme="simple" target="_blank">
					<input name="type" type="hidden" value="LOGIN" />
					<input name="start" type="hidden" value="2017-01-01 00:00:00" />
					<input name="end" type="hidden" value="${endTime}" />
					<input name="size" type="hidden" value="20" />
					<s:hidden name="pageIndex" value="1"></s:hidden>
					<input name="loginname" type="hidden" value="${loginname}" />
					<s:submit value="IP登录地址记录"></s:submit>
				</s:form>
			</td>
		</tr>
		<tr style="color: red; font-weight: bold">
			<td>警告备注:</td>
			<td><s:property value="%{#request.user.warnremark}" /></td>
		</tr>
	</table>
<div class="clear" style="clear:both;"></div>
<div>
	<p>维护信息</p>
	<table>
		<tr>
			<td class='label_search_td_play' valign='top' width='77px'>玩家属性备注：</td>
			<td><textarea name='remark' class='input' id='remark' style='width: 400px; height: 105px;'></textarea></td>
		</tr>
		<tr>
			<td style='text-align: right;' colspan='2'>
				<input type='button' value='保存' onclick='saveRemark(this);'>
			</td>
		</tr>
	</table>
</div>
<div>
	<div style="width: 100px;">
		<fieldset>
			<legend>新增维护记录</legend>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0">
							<tr align="left">
								<td>维护内容：</td>
								<td><input type="text" name="content" id="content" size="120px" /></td>
								<td>&nbsp;</td>
								<td><input type="button" value="新 增" onclick="create()" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
	</div>
	<div id="createtable" style="margin-top: 20px;"></div>
</div>
<!-- <a href="#" onclick="maintainLog()">维护日志>></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="maintainRemark()">维护玩家备注>></a> -->


<script type="text/javascript">
			$("#_modifyCommit").on("click",function(){
				if(confirm("确认更新？")){
					var agent = $("#_modifyAgent").val();
					var loginname = "${requestScope.user.loginname}";
					console.log(agent+"   "+loginname);
					$.post("/office/updateUserAgent.do",{"agent":agent , "loginname":loginname},function(data){
						alert(data);
					});
				}
			});
            var loginname = "${requestScope.user.loginname}";
            var remark = "${requestScope.user.userRemark}";
            $("#remark").val(remark);



            function JAlert_Progressbar_Dialog(divId) {
                buttonJAlertStyle();
                $("#" + divId).dialog({ resizable: false, height: 220, width: 520, modal: false });
            }

            function buttonJAlertStyle() {
                $(".button_wxz").hover(function() {
                    $(this).removeClass("button_wxz").addClass("button_xz");
                }, function() {
                    $(this).removeClass("button_xz").addClass("button_wxz");
                });
            }

            function saveRemark() {
                var remark1 = $("#remark").val();
                $.post("/office/updateUserRemark.do",{"remark":remark1,"loginname":loginname},function(data){
                    alert(data["message"]);
                    $("#JqAlert").dialog("close");
                    $("#JqAlert").hide();
                    window.location.reload();
                });
            }

            function cancelRemark() {
                $("#JqAlert").dialog("close");
                $("#JqAlert").hide();
            }
            /*增加  */
            var afterUrl = window.location.search.substring(1);
            var loginname = afterUrl.substring(afterUrl.indexOf('=') + 1);

            $(document).ready(function() {

                createTable();
            });

            function create() {

                var content = $("#content").val();

                if (content == "" || content == null) {

                    alert("维护内容不能为空！");
                    return;
                }

                $.ajax({
                    url: '/office/addUserMaintainLog.do',
                    data: { "loginname": loginname, 'content': content },
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function(result) {

                        if (result["code"] == "10000") {

                            alert(result["message"]);
                            createTable();
                        } else {

                            alert(result["message"]);
                        }
                    }
                });
            }

            function createTable() {

                $("#createtable").empty();

                var table = $("<table id=\"table\" border=\"1\" width=\"1000px\">");

                table.appendTo($("#createtable"));

                var tr = $("<tr></tr>");
                tr.appendTo(table);

                var td1 = $("<td width='18%'>加入时间</td>");
                td1.appendTo(tr);
                var td2 = $("<td width='15%'>最后操作人</td>");
                td2.appendTo(tr);
                var td3 = $("<td>维护内容</td>");
                td3.appendTo(tr);
                var td5 = $("<td>类型</td>");
                td5.appendTo(tr);
                var td4 = $("<td width='5%'>操作</td>");
                td4.appendTo(tr);

                $.ajax({
                    url: '/office/findUserMaintainLogList.do',
                    data: { "loginname": loginname },
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function(result) {
                        if (result && result.length > 0) {

                            for (var i = 0, len = result.length; i < len; i++) {

                                var tr = $("<tr></tr>");
                                tr.appendTo(table);

                                var obj = result[i];

                                var td5 = $("<td>" + obj.createTime + "</td>");
                                td5.appendTo(tr);
                                var td6 = $("<td>" + obj.updateUser + "</td>");
                                td6.appendTo(tr);
                                var td7 = $("<td>" + obj.content + "</td>");
                                td7.appendTo(tr);
                                var td8;
                                if(obj.type==0){
                                    td8 = $("<td>系统 </td>");
                                    td8.appendTo(tr);
                                } else {
                                    td8 = $("<td>人工 </td>");
                                    td8.appendTo(tr);
                                    var auth =	"${sessionScope.operator.authority}";
                                    //不知道谁的代码，权限不对，标注一下
                                    if(auth=="boss" || auth=="om" || auth=="sale_manager" || auth=="finance_manager" || auth=="market_manager" || auth=="admin"){
                                        var td9 = $("<td><input type='button' value='修改' id='btn" + i + "' onclick='edit(" + i + ",this," + obj.id + ")'><input type='button' value='删除' id='btn" + i + "' onclick='delet(" + i + ",this," + obj.id + ")'></td>");
                                        td9.appendTo(tr);
                                    } else {
                                        var td9 = $("<td><input type='button' value='修改' id='btn" + i + "' onclick='edit(" + i + ",this," + obj.id + ")'></td>");
                                        td9.appendTo(tr);
                                    }
                                }

                            }
                        }

                        $("#createtable").append("</table>");
                    }
                });
            }

            function edit(x, obj, id) {

                x = x + 1;

                var table = document.getElementById("table");
                var text = table.rows[x].cells[2].innerHTML;
                table.rows[x].cells[2].innerHTML = '<textarea id="textarea' + x + '" style="width: 100%;"></textarea><input type="hidden" id="hidden'+x+'" value="'+id+'">';
                var textarea = document.getElementById("textarea" + x);
                textarea.value = text;
                textarea.focus();
                textarea.select();
                obj.value = "确定";
                obj.onclick = function onclick(event) {
                    update_success(this, x);
                };
            }
            function delet(x, obj, id) {

                obj.value = "确定";
                obj.onclick = function onclick(event) {
                    delete_success(this, id);
                };
            }


            function update_success(obj, x) {

                var textarea = document.getElementById("textarea" + x);
                var content = textarea.value;
                var hidden = document.getElementById("hidden" + x);
                var id = hidden.value;

                $.ajax({
                    url: '/office/updateUserMaintainLog.do',
                    data: { "content": content, "id": id },
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function(result) {

                        if (result["code"] == "10000") {

                            alert(result["message"]);
                            createTable();
                        } else {

                            alert(result["message"]);
                        }
                    }
                });
            }
            function delete_success(obj, id) {

                $.ajax({
                    url: '/office/deleteUserMaintainLog.do',
                    data: {"id": id },
                    cache: false,
                    async: false,
                    type: "POST",
                    success: function(result) {

                        if (result["code"] == "10000") {

                            alert(result["message"]);
                            createTable();
                        } else {

                            alert(result["message"]);
                        }
                    }
                });
            }
</script>
	<c:import url="/office/script.jsp" />
</body>
</html>
