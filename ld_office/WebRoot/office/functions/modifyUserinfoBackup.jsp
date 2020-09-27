<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="dfh.security.EncryptionUtil"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看用户基本信息</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript">
				function changeE68Exten(num){
					var href = document.getElementById("_call2").href;
					if(href){
						var replaceStr = href.substring(0,href.length-4)+num;
						document.getElementById("_call2").href = replaceStr;
					}
				}
				function changeQyExten(num){
					if(num >800000){
				    	 var hrefTwo = document.getElementById("_callTwo").href;
				    	 
				    	 var hrefThree = document.getElementById("_callThree").href;
				    	 var hrefFour = document.getElementById("_callFour").href;
				    	 
					     if(hrefTwo){
					    	 var arr = hrefTwo.split("ext_no=");
					    	 var replaceStr = arr[0]+"ext_no="+num;
						     document.getElementById("_callTwo").href = replaceStr;
					     }
					     
					     if(hrefThree){
					    	 var arr = hrefThree.split("ext_no=");
					    	 var replaceStrThree = arr[0]+"ext_no="+num;
						     document.getElementById("_callThree").href = replaceStrThree;
					     }
					     
					     if(hrefFour){
					    	 var arr = hrefFour.split("ext_no=");
					    	 var replaceStrFour = arr[0]+"ext_no="+num;
						     document.getElementById("_callFour").href = replaceStrFour;
					     }
					     
					     return ;
				     }else if(num>800){
 						 var hrefTwo = document.getElementById("_callTwo").href;
				    	 
				    	 var hrefThree = document.getElementById("_callThree").href;
				    	 var hrefFour = document.getElementById("_callFour").href;
				    	 
					     if(hrefTwo){
					    	 var arr = hrefTwo.split("ext_no=");
					    	 var replaceStr = arr[0]+"ext_no="+num;
						     document.getElementById("_callTwo").href = replaceStr;
					     }
					     
					     if(hrefThree){
					    	 var arr = hrefThree.split("ext_no=");
					    	 var replaceStrThree = arr[0]+"ext_no="+num;
						     document.getElementById("_callThree").href = replaceStrThree;
					     }
					     
					     if(hrefFour){
					    	 var arr = hrefFour.split("ext_no=");
					    	 var replaceStrFour = arr[0]+"ext_no="+num;
						     document.getElementById("_callFour").href = replaceStrFour;
					     }
					      
					    // return ; 
				     }
					///var href = document.getElementById("_call").href;
					///if(href){
						///var replaceStr = href.substring(0,href.length-3)+num;
						///document.getElementById("_call").href = replaceStr;
					///}
					var hrefOne = document.getElementById("_callOne").href;
					if(hrefOne){
						var replaceStr = hrefOne.substring(0,hrefOne.length-3)+num;
						document.getElementById("_callOne").href = replaceStr;
					}
				}
			</script>
	</head>
	<body>
		<div id="excel_menu_left">
			操作 --&gt; 查看用户基本信息
		</div>
		<table align="left" border="0">
			<tr>
				<td>
					当前会员账号:
				</td>
				<td>
					<s:property value="%{#request.user.loginname}" />
				</td>
			</tr>

			<tr>
				<td>
					用户ID:
				</td>
				<td>
					<s:property value="%{#request.user.id}" />
				</td>
			</tr>
			
			<tr>
				<td>
					用户随机数:
				</td>
				<td>
					<s:property value="%{#request.user.randnum}" />
				</td>
			</tr>

			<tr>
				<td>
					昵称:
				</td>
				<td>
					<s:property value="%{#request.user.aliasName}" />
				</td>
			</tr>
			<tr>
				<td>
					真实姓名:
				</td>
				<td>
					<%-- <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='admin' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager'  || #session.operator.authority=='finance'" >
						<s:property value="%{#request.user.accountName}" />
					</s:if>
					<s:else>
						<s:property value="%{#request.user.accountName.substring(0,1)+'**'}" />
					</s:else> --%>
					<s:property value="%{#request.user.accountName}" />
				</td>
			</tr>
			<c:if
				test="${sessionScope.operator.authority ne 'sale' 
					and sessionScope.operator.authority ne 'finance' and sessionScope.operator.authority ne 'finance_manager'
					and sessionScope.operator.authority ne 'market'
				}">
				<tr>
					<td>
						Email:
					</td>
					<td>
						<s:property value="%{#request.user.email}" />
					</td>
				</tr>

				<tr>
					<td>
						联系电话:
					</td>
					<td>
						<s:property value="%{#request.user.phone}" />
					</td>
				</tr>
			</c:if>
			<tr>
				<td>
					座机号1：
					<c:if
				test="${sessionScope.operator.phoneno eq null || sessionScope.operator.phoneno eq ''
				}">
			
					<select onchange="changeQyExten(this.value);">
						<option value="801">
							801
						</option>
						<option value="802">
							802
						</option>
						<option value="803" selected="selected">
							803
						</option>
						<option value="804">
							804
						</option>
						<option value="805">
							805
						</option>
						<option value="806">
							806
						</option>
						<option value="807">
							807
						</option>
						<option value="808">
							808
						</option>
						<option value="809">
							809
						</option>
						<option value="810">
							810
						</option>
						<option value="811">
							811
						</option>
						<option value="812">
							812
						</option>
						<option value="813">
							813
						</option>
						
						<option value="814">
							814
						</option>
						<option value="815">
							815
						</option>
						<option value="816">
							816
						</option>
						<option value="817">
							817
						</option>
						<option value="818">
							818
						</option>
						<option value="819">
							819
						</option>
						<option value="820">
							820
						</option>
						<option value="821">
							821
						</option>
						<option value="822">
							822
						</option>
						
						<c:forEach var="i" begin="888101" end="888130" step="1">
						  <option value="${i}">
								${i}
							</option>
						</c:forEach>
						<option value=801>
							比邻801
						</option>
						<option value=802>
							比邻802
						</option>
						<option value=803>
							比邻803
						</option>
						<option value=804>
							比邻804
						</option>
						<option value=805>
							比邻805
						</option>
						<option value=806>
							比邻806
						</option>
						<option value=807>
							比邻807
						</option>
						<option value=808>
							比邻808
						</option>
					</select>
					&nbsp;&nbsp;
				</td>
				<td>
					<%
						dfh.model.UsersBackup user = (dfh.model.UsersBackup) request
								.getAttribute("user");
						String phone = user.getPhone();
						if (phone != null && !"".equals(phone)) {
							try {
								long l = Long.parseLong(phone);
								String p = "1" + String.valueOf(l);
								l = Long.parseLong(p) * 11 + 159753;
								request.setAttribute("encryptPhone", l);
					%>
					<!-- <a id="_call"
						href="http://192.168.0.8:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
						target="_blank">呼叫该用户(内)</a>
						<a id="_call"
						href="http://115.29.227.189:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
						target="_blank">呼叫该用户(外)</a>-->
						<a id="_callOne" href="http://47.90.12.131:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=803"
						target="_blank">呼叫该用户(外新)</a>
						
						<a id="_callTwo"
						href="http://203.88.165.54/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
						target="_blank">CALL</a>
						<a id="_callThree"
						href="http://123.1.186.200/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
						target="_blank">CALL(外)</a>
						<a id="_callFour"
						href="http://192.168.100.5/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=888101"
						target="_blank">CALL(内)</a>
					<%
						} catch (Exception e) {
					%>
					该用户电话号码格式异常!
					<%
						}
						} else {
					%>
					无该用户电话信息
					<%
						}
					%>

</c:if>
				<c:if
				test="${sessionScope.operator.phoneno ne  null && sessionScope.operator.phoneno ne  ''
				}">
				<td>
				${sessionScope.operator.phoneno}
					<%
						dfh.model.UsersBackup user = (dfh.model.UsersBackup) request.getAttribute("user");
						String phone = user.getPhone();
						if (phone != null && !"".equals(phone)) {
							try {
								long l = Long.parseLong(phone);
								String p = "1" + String.valueOf(l);
								l = Long.parseLong(p) * 11 + 159753;
								request.setAttribute("encryptPhone", l);
					%>
					<!-- <a id="_call"
						href="http://192.168.0.10:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=903"
						target="_blank">呼叫该用户(内)</a> -->
						<!--<a id="_call"
						href="http://115.29.227.189:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=603"
						target="_blank">呼叫该用户(外)</a> -->
						<a id="_callOne"
						href="http://47.90.12.131:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=${sessionScope.operator.phoneno}"
						target="_blank">呼叫该用户(外新)</a>
						
						<a id="_callTwo"
						href="http://203.88.165.54/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phoneno}"
						target="_blank">CALL</a>
						<a id="_callThree"
						href="http://123.1.186.200/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phoneno}"
						target="_blank">CALL(内)</a>
						
						<a id="_callFour"
						href="http://192.168.100.5/atstar/index.php/status-op?op=dialv2&dia_num=${requestScope.encryptPhone}&ext_no=${sessionScope.operator.phoneno}"
						target="_blank">CALL(外)</a>
					<%
						} catch (Exception e) {
					%>
					该用户电话号码格式异常!
					<%
						}
						} else {
					%>
					无该用户电话信息
					<%
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
					<%
						dfh.model.UsersBackup user2 = (dfh.model.UsersBackup) request
								.getAttribute("user");
						String phone2 = user2.getPhone();
						if (phone2 != null && !"".equals(phone2)) {
							try {
								phone2 = EncryptionUtil.encryptBASE64(phone2.getBytes());
								request.setAttribute("encryptPhone2", phone2);
					%>
					<a id="_call2"
						href="http://192.168.0.160/cc/api/webdial/?call=${requestScope.encryptPhone2}&encrypt=yes&prefix=&exten=8003"
						target="_blank">呼叫该用户</a>
					<%
						} catch (Exception e) {
					%>
					该用户电话号码格式异常!
					<%
						}
						} else {
					%>
					无该用户电话信息
					<%
						}
					%>
				</td>
			</tr> -->
			<tr>
				<td>
					生日:
				</td>
				<td>
					<s:property value="%{#request.user.birthday}" />
				</td>
			</tr>
			<c:if
				test="${sessionScope.operator.authority ne 'sale' 
					and sessionScope.operator.authority ne 'finance' and sessionScope.operator.authority ne 'finance_manager'
					and sessionScope.operator.authority ne 'market'
				}">
				<tr>
					<td>
						QQ:
					</td>
					<td>
						<s:property value="%{#request.user.qq}" />
					</td>
				</tr>
			</c:if>
			<tr>
				<td>
					代理网址:
				</td>
				<td>
					<s:property value="%{#request.user.referWebsite}" />
				</td>
			</tr>
			<tr>
				<td>
					代理agcode:
				</td>
				<td>
					<s:property value="%{#request.user.agcode}" />
				</td>
			</tr>
			<tr>
				<td>
					来源网址:
				</td>
				<td>
					<s:property value="%{#request.user.howToKnow}" />
				</td>
			</tr>
			<tr>
				<td>
					备注:
				</td>
				<td>
					<s:property value="%{#request.user.remark}" />
				</td>
			</tr>
			<tr>
				<td>
					邮寄地址:
				</td>
				<td>
					<s:property value="%{#request.user.mailaddress}" />
				</td>
			</tr>
			<tr>
				<td>
					客户操作系统:
				</td>
				<td>
					<s:property value="%{#request.user.clientos}" />
				</td>
			</tr>
			<tr style="color: red; font-weight: bold">
				<td>
					操作记录:
				</td>
				<td>
					<s:form action="queryOperationLog" namespace="/office"
						name="mainform" id="mainform" theme="simple" target="_blank">
						<input name="start" type="hidden" value="2017-01-01 00:00:00" />
						<%
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String end = sf.format(new Date());
							request.setAttribute("endTiem", end);
						%>
						<input name="type" type="hidden" value="SETWARNLEVEL" />
						<input name="end" type="hidden" value="${endTime}" />
						<input name="size" type="hidden" value="20" />
						<input name="remark" type="hidden" value="将会员${loginname}" />
						<input name="loginname" type="hidden" value="" />
						<s:submit value="警告操作记录"></s:submit>
						<s:hidden name="pageIndex" value="1"></s:hidden>
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
				<td>
					警告备注:
				</td>
				<td>
					<s:property value="%{#request.user.warnremark}"/>
				</td>
			</tr>
		</table>
		<c:import url="/office/script.jsp" />
	</body>
</html>
