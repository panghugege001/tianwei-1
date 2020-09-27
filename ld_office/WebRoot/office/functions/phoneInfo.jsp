<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
	</head>
	<script type="text/javascript">
	     function submitFrom(){
             var phonestatus=$("#phonestatus").val();
             if(phonestatus==""){
                  alert("手机状态不能为空!");
   		          return false;
             }
             if(phonestatus=="4"){
                 alert("手机状态不能为可用!");
  		          return false;
            }
             var isreg=$("#isreg").val();
             if(isreg==""){
                  alert("客户状态不能为空!");
   		          return false;
             }
             $("#mainform").submit();
          }		
        $(document).ready(function () {
            var errormsg="${errormsg}";
            if(errormsg==""){
            
            }else{
               alert(errormsg);
            }
        });
	</script>
	<body>
		<s:form action="updatePhone" onsubmit="submitonce(this);"
			namespace="/office" name="mainform" id="mainform" theme="simple">
			<div style="margin-left: 20px;">
				<br />
				<br />
				手机状态:
				<input type="hidden" name="id" value="${iphone.id}"/>
				<s:select cssStyle="width:150px" name="phonestatus" id="phonestatus"
					list="%{#application.PhoneStatus}" listKey="code" listValue="text"
					emptyOption="true" value="%{iphone.phonestatus}" />
				<br />
				<br />
				客户状态:
				<s:select cssStyle="width:150px" name="isreg" id="isreg"
					list="%{#application.UserRegisterStatus}" listKey="code"
					listValue="text" emptyOption="true" value="%{iphone.userstatus}" />
				<br />
				<br />
				&nbsp;&nbsp;&nbsp;&nbsp;备注:
				<textarea name="remark" class="remark" id="content"
					 style="width: 467px; height: 150px;">${iphone.remark}</textarea>
				<br />
				<br />
				<input type="submit" value="保存" onclick="return submitFrom();" />
			</div>
		</s:form>
	</body>
</html>
