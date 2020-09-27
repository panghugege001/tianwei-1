<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" scope="request"
	value="${pageContext.request.contextPath}" />
<%@include file="/office/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>活动时间</title>
			<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery.messager.js"></script>
		<script type="text/javascript">
function functionCheckbox(id,userrole){
      var xmlhttp = new Ajax.Request(    
			"/office/updateActivityRole.do",
		    {    
		         method: 'post',
		         parameters:"id="+id+"&type="+userrole+"&r="+Math.random(),
		         onComplete: responseMethod  
		    }
	  );
}

function responseMethod(data){

	//alert(data.responseText);
	
}
		</script>
	<body>
		活动管理 --&gt; 新增活动返水时间
		<a href="${pageContext.request.contextPath}/office/functions/activity.jsp"><font color="red">上一步</font>
		</a>
		<br />
		<br />
		<div id="excel_menu">
			<s:form action="addActivityTwo" onsubmit="submitonce(this);"
				namespace="/office" name="mainform" id="mainform" theme="simple">
				<table align="left">
					<tr>
						<td>
							活动名称:
						</td>
						<td>
						    <s:hidden name="id" value="%{activity.id}" />
							<s:textfield name="activityName" size="30"
								value="%{activity.activityName}" />
						</td>
					</tr>
					<tr>
						<td>
							开始时间:
						</td>
						<td>
							<s:textfield name="start" size="18"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{activity.activityStart}"
								cssClass="Wdate" />
						</td>
					</tr>
					<tr>
						<td>
							开始时间:
						</td>
						<td>
							<s:textfield name="end" size="18"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								My97Mark="false" value="%{activity.activityEnd}"
								cssClass="Wdate" />
						</td>
					</tr>
					<tr>
						<td>
							返水比例:
						</td>
						<td>
							<s:textfield name="percent" size="30"
								value="%{activity.activityPercent}" />
							填入如：0.008 0.015
						</td>
					</tr>
					<tr>
						<td>
							玩家角色
						</td>
						<td>
							<s:if test="%{activity.userrole.contains(\"0\")}">
								<input checked="checked" type="checkbox" value="0"
									id="checkboxA${activity.id}"
									onclick="functionCheckbox(${activity.id},0);" />新会员
										    </s:if>
							<s:else>
								<input type="checkbox" value="0" id="checkboxA${activity.id}"
									onchange="functionCheckbox(${activity.id},0);" />新会员
										    </s:else>
							<s:if test="%{activity.userrole.contains(\"1\")}">
								<input type="checkbox" value="1" checked="checked"
									id="checkboxB${activity.id}"
									onclick="functionCheckbox(${activity.id},1);" />忠实会员
										    </s:if>
							<s:else>
								<input type="checkbox" value="1" id="checkboxB${activity.id}"
									onclick="functionCheckbox(${activity.id},1);" />忠实会员
										    </s:else>
							<s:if test="%{activity.userrole.contains(\"2\")}">
								<input checked="checked" type="checkbox" value="2"
									id="checkboxC${activity.id}" checked="checked"
									onclick="functionCheckbox(${activity.id},2);" />星级会员
										    </s:if>
							<s:else>
								<input type="checkbox" value="2" id="checkboxC${activity.id}"
									onclick="functionCheckbox(${activity.id},2);" />星级会员
										    </s:else>

							<s:if test="%{activity.userrole.contains(\"3\")}">
								<input type="checkbox" value="3" checked="checked"
									id="checkboxD${activity.id}"
									onclick="functionCheckbox(${activity.id},3);" />黄金VIP
										    </s:if>
							<s:else>
								<input type="checkbox" value="3" id="checkboxD${activity.id}"
									onclick="functionCheckbox(${activity.id},3);" />黄金VIP
										    </s:else>

							<s:if test="%{activity.userrole.contains(\"4\")}">
								<input type="checkbox" value="4" checked="checked"
									id="checkboxE${activity.id}"
									onclick="functionCheckbox(${activity.id},4);" />白金VIP
										    </s:if>
							<s:else>
								<input type="checkbox" value="4" id="checkboxE${activity.id}"
									onclick="functionCheckbox(${activity.id},4);" />白金VIP
										    </s:else>

							<s:if test="%{activity.userrole.contains(\"5\")}">
								<input type="checkbox" value="5" checked="checked"
									id="checkboxF${activity.id}"
									onclick="functionCheckbox(${activity.id},5);" />钻石VIP
										    </s:if>
							<s:else>
								<input type="checkbox" value="5" id="checkboxF${activity.id}"
									onclick="functionCheckbox(${activity.id},5);" />钻石VIP
										    </s:else>

							<s:if test="%{activity.userrole.contains(\"6\")}">
								<input type="checkbox" value="6" checked="checked"
									id="checkboxG${activity.id}"
									onclick="functionCheckbox(${activity.id},6);" />至尊VIP
										    </s:if>
							<s:else>
								<input type="checkbox" id="checkboxG${activity.id}"
									onclick="functionCheckbox(${activity.id},6);" />至尊VIP
										    </s:else>
						</td>
					</tr>
					<tr>
						<td valign="top" style="text-align: right;">
							备注:
						</td>
						<td>
							<s:textarea name="remark" rows="8" cols="60"
								value="%{activity.remark}"></s:textarea>
						</td>
					</tr>
					<tr>
						<td align="center">
							<s:submit value="提交" onclick="return submitFrom();" />
						</td>
						<td></td>
					</tr>
				</table>
			</s:form>
		</div>
		<br />
		<div id="middle">
			<div id="right">
				<div id="right_01">
					<div id="right_001">
						<div id="right_02">
							<div id="right_03"></div>
						</div>
						<div id="right_04">

						</div>
					</div>
				</div>
			</div>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>
