<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改流水数据</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
			
		<s:head />
		<script type="text/javascript">
			function checkConcertData(e){
				var rn = false;
				var bet = $('#fee').val();
				if (isNaN(bet)||bet==""){
					alert("流水金额必须填写数字!");
					return false;
				} else {
					rn = window.confirm("确认数据都已输入并提交?");
				}
				return rn;
			}		
			
		</script>
	</head>
	<body style="background: #b6d9e4; font-size: 11px">
		<div id="excel_menu_left">
			操作 --> 修改流水
			<a href="javascript:history.back();"><font color="red">上一步</font>
			</a>
		</div>

		<div id="excel_menu">
			<s:form action="editConcertBet" onsubmit="return checkConcertData(this);" namespace="/office" theme="simple">
			  <s:hidden name="id" value="" id="ids"></s:hidden>
				<table align="left">
					<tr>
						<td colspan="2">
							<s:fielderror />
						</td>
					</tr>
					<tr>
						<td colspan="2" height="10px">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<font color="blue">以下填写内容，请确保全部填写</font>
						</td>
					</tr>
					<tr>
						<td>
							流水金额:
						</td>
						<td>
							<s:textfield name="amount" size="15"  id="fee"/>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="2">
							<s:submit value="提  交" />
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
		
		<script type="text/javascript">
		
			var reg = new RegExp("(^|&)id=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			    
			if (r != null){
				 $("#ids").val(unescape(r[2]));
			}
					  	
		</script>
		
		<c:import url="/office/script.jsp" />
	</body>

</html>

