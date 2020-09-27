<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>创建子管理员</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
	</head>
	<script>
		function divType(){
		 var validType=$("#validType").val();
            if(validType==1){
               $('#pphoneNo').css('display','block');
               $('#pemployeeNo').css('display','none');
            }else if(validType==2){
           	 $('#pemployeeNo').css('display','block');
                $('#pphoneNo').css('display','none'); 
            }else if(validType==3){
           	 $('#pphoneNo').css('display','none');
                $('#pemployeeNo').css('display','none'); 
            }
        	}
	</script>
	<body>
		<div id="excel_menu_left">
			其他 --> 创建子管理员
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>
		<div id="excel_menu">
			<s:fielderror />
				<c:if test="${sessionScope.operator.authority ne 'boss'}">
				<s:form action="createSubOperator" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
					<p align="left">
						子管理员帐号:&nbsp;
						<s:textfield name="newOperator" size="30" />
					</p>
					<p align="left" style="display: none;">
						安全认证方式:&nbsp;
							<select style="width:150px;" name="validType" id="validType">
								<!-- <option value=1>
									短信验证
								</option> -->
								<option value=2 selected="selected">
									打卡验证
								</option>
							</select>
						</p>
						<p align="left" id="pemployeeNo">
							员工编号:&nbsp;&nbsp;&nbsp;
							<s:textfield name="employeeNo" size="30" />
						</p>
						<p align="left" id="pphoneNo" style="display: none;">
							手机号:&nbsp;&nbsp;&nbsp;&nbsp;
							<s:textfield name="cellphoneNo" size="30" />
						</p>
					<c:if test="${sessionScope.operator.authority eq 'yan751028'}">
						<p align="left">
							专员权限:&nbsp;
							<s:select cssStyle="width:150px" list="#{'sale':'客服专员','market':'市场专员','qc':'质检专员'}" key="key"
									  value="value" name="authority"></s:select>
						</p>
					</c:if>
						<p align="left">
						子管理员邮箱:&nbsp;
						<s:textfield name="email" size="30" />
					</p>
					<c:choose>
						<c:when test="${sessionScope.operator.authority eq 'finance_manager' || sessionScope.operator.authority eq 'finance_leader'}">
							<input name="authority" value="finance" type="hidden"/>
						</c:when>
						<c:otherwise>
							<p align="left">
								专员权限:&nbsp;&nbsp;&nbsp;
								<s:select cssStyle="width:150px"
										  list="#{'sale':'客服专员','market':'市场专员'}"
										  key="key" value="value" name="authority"></s:select>
							</p>
						</c:otherwise>
					</c:choose>
					<p align="left">
						密码:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:password name="password" size="30" />
					</p>
					<p align="left">
						重复密码:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:password name="retypePassword" size="30" />
					</p>
					<p align="left">
						代理账号:&nbsp;
						<s:textfield name="agent" size="30" /> 多账号使用,分隔.范例: a_aaaa,a_bbbb,a_cccc 
					</p>
					<p align="left">
						<s:submit value="创建子管理员" align="left" />
					</p>
				</s:form>
			</c:if>
			<c:if test="${sessionScope.operator.authority eq 'boss'}">
				<s:form action="createSubOperatorTwo" onsubmit="submitonce(this);" namespace="/office" name="mainform" id="mainform" theme="simple">
					<p align="left">
						专员帐号:&nbsp;
						<s:textfield name="newOperator" size="30" />
					</p>
					<p align="left" style="display: none;">
						安全认证方式:&nbsp;
						<select style="width:150px;" name="validType" id="validType">
							<!-- <option value=1>
								短信验证
							</option> -->
							<option value=2 selected="selected">
								打卡验证
							</option>
						</select>
					</p>
					<p align="left" id="pemployeeNo">
						员工编号:&nbsp;
						<s:textfield name="employeeNo" size="30" />
					</p>
					<p align="left">
						专员邮箱:&nbsp;
						<s:textfield name="email" size="30" />
					</p>
					<p align="left">
						专员权限:&nbsp;
						<s:select cssStyle="width:150px" list="#{'boss':'boss','admin':'admin','sale_manager':'客服管理员','sale':'客服专员','market_manager':'市场管理员','market':'市场专员','finance_manager':'财务管理员','finance':'财务专员','qc':'质检专员','om':'运维工程师'}" key="key"
								value="value" name="authority"></s:select>
					</p>
					<p align="left">
						密码:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<s:password name="password" size="30" />
					</p>
					<p align="left">
						重复密码:&nbsp;
						<s:password name="retypePassword" size="30" />
					</p>
					<p align="left">
						代理账号:&nbsp;
						<s:textfield name="agent" size="30" /> 多账号使用,分隔.范例: a_aaaa,a_bbbb,a_cccc 
					</p>
					<p align="left">
						<s:submit value="创建子帐号" align="left" />
					</p>
				</s:form>
			</c:if>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>

