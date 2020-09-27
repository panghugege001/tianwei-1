
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.model.enums.ProposalType"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("pragma", "no-cache");
		response.setHeader("cache-control", "no-cache");
		response.setDateHeader("expires", 0);
	%>
	<head>
		<title>提款银行状态配置</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.2.js"></script>
	</head>
	<body>
		<p>
			账户 --&gt; 提款银行状态配置
			<a href="javascript:history.back();"><font color="red">上一步</font></a>
		</p>
		
		<div id="right_04">
		  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
            <tr>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >ID</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >银行名称</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >状态</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('distributeTime');">创建时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;cursor: pointer;" onclick="orderby('getTime');">更新时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:13px;;color: #FFFFFF;font-weight: bold;" >操作</td>
            </tr>
            <s:iterator var="bankstatus" value="%{#request.list}">
	            <tr bgcolor="${bgcolor}">
	              <td align="center"  style="font-size:13px;"><s:property value="#bankstatus.id"/></td>
	              <td align="center"  style="font-size:13px;"><s:property value="#bankstatus.bankname"/></td>
	              <td align="center"  style="font-size:13px;" name="td_status_<s:property value="#bankstatus.id"/>">
					<s:if test="#bankstatus.status=='OK'">正常</s:if>
					<s:elseif test="#bankstatus.status=='MAINTENANCE'"><font color="red">维护中</font></s:elseif>
	              </td>
	              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#bankstatus.createTime"/></td>
	              <td align="center"  style="font-size:13px;"><s:date format="yyyy-MM-dd HH:mm:ss" name="#bankstatus.updateTime"/></td>     		
	              <td align="center"  style="font-size:13px;" name="td_setStatus_<s:property value="#bankstatus.id"/>">
	              	   <s:if test="#bankstatus.status=='OK'">
	              	   	   <input style="color: red;" type="button" onclick="setBankStatusToMaintenance('<s:property value="#bankstatus.id"/>')" value="设为维护状态" />
	              	   </s:if>
	              	   <s:if test="#bankstatus.status=='MAINTENANCE'">
	              	   	   <input type="button" onclick="setBankStatusToOK('<s:property value="#bankstatus.id"/>')" value="设为正常状态" />
	              	   </s:if>
	              </td>
	            </tr>
	  	 	 </s:iterator>
          </table>
	  </div>
		
		
	<script>
		function setBankStatusToMaintenance(id) {
			var status = "ERROR";
			$.ajax({
	            type: "POST",
	            async: false,
	            url: "/bankinfo/updateBankStatus.do",
	            data: {"id":id,"status":"MAINTENANCE"},
	            error: function (response) {
	                alert(response);
	            },
	            success: function (response) {
	            	status = response;
	            }
	        });
	        if(status == "SUCCESS") {
	        	$("[name=td_status_" + id + "]").html('<font color="red">维护中</font>');
	        	$("[name=td_setStatus_" + id + "]").html('<input type="button" onclick="setBankStatusToOK('+ id +')" value="设为正常状态" />');
	        	alert("修改成功");
	        }else {
	        	alert("修改失败");
	        }
		}

		function setBankStatusToOK(id) {
			var status = "ERROR";
			$.ajax({
	            type: "POST",
	            async: false,
	            url: "/bankinfo/updateBankStatus.do",
	            data: {"id":id,"status":"OK"},
	            error: function (response) {
	                alert(response);
	            },
	            success: function (response) {
	            	status = response;
	            }
	        });
	        if(status == "SUCCESS") {
	        	$("[name=td_status_" + id + "]").html('正常');
	        	$("[name=td_setStatus_" + id + "]").html('<input type="button" style="color: red;" onclick="setBankStatusToMaintenance('+ id +')" value="设为维护状态" />');
	        	alert("修改成功");
	        }else {
	        	alert("修改失败");
	        }
		}
	</script>
	
	</body>
</html>

