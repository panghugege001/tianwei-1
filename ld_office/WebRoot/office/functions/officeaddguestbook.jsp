<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>发信箱</title>
		<link href="<c:url value='/css/error.css' />" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
		<style type="text/css">
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
		 function divType(){
			 var type=$("#type").val();
             if(type=="0"){
                $('#typeDiv0').css('display','block');
                $('#typeDiv1').css('display','none');
                $('.typeDiv2').css('display','none');
             }else if(type=="1"){
                $('#typeDiv0').css('display','none');
                $('#typeDiv1').css('display','block');  
                $('.typeDiv2').css('display','none');  
             }else if(type=="2"){
                 $('#typeDiv0').css('display','none');
                 $('#typeDiv1').css('display','none');  
                 $('.typeDiv2').css('display','block');  
              }
         }
         function submitFrom(){
             var type=$("#type").val();
             if(type=="0"){
                 var username=$("#username").val();
             	 if(username==""){
                  	alert("账号不能为空！");
   		          	return false;
             	 }
             } 
             if(type=="2"){
            	 var startTime = $("#startTime").val();
            	 var endTime = $("#endTime").val();
            	 if(startTime=="" || endTime==""){
            		 alert("客服代码群发时，时间不能为空");
            		 return false ;
            	 }
             }
             var title=$("#title").val();
             if(title==""){
                  alert("主题不能为空!");
   		          return false;
             }
             var content=$("#content").val();
             if(content==""){
                  alert("内容不能为空!");
   		          return false;
             }
             $("#mainform").submit();
          }		
	</script>
	</head>
	<body>
		<div id="excel_menu_left" style="margin-top: 20px;">
			站内信 --&gt; 发信箱
			<s:url action="queryWordsForBack" namespace="/office"
				var="queryWordsForBackUrl">
				<s:param name="start" value="%{startTime}"></s:param>
				<s:param name="end" value="%{endTime}"></s:param>
			</s:url>
			<s:a href="%{queryWordsForBackUrl}">
				<font color="red">返回</font>
			</s:a>
		</div>
		<s:fielderror></s:fielderror>
		<s:form action="saveLeaveWords" onsubmit="submitonce(this);"
				namespace="/office" name="mainform" id="mainform" theme="simple">
			<table align="left" border="0"
				style="margin-top: 10px; width: 700px;">
				<tr>
					<td style="text-align: right;">
						类型:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
						<select style="width: 200px; height: 28px;" class="input"
							name="type" id="type" onchange="divType();">
							<option value="0">
								个人
							</option>
							<option value="1">
								会员等级群发
							</option>
							<option value="2">
								客服代码群发
							</option>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td style="text-align: right;">
						收件人:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
					    <div id="typeDiv0">
					        <input name="username" style="width: 200px;" class="input" id="username" />
					    </div>
						<div id="typeDiv1" style="display: none;">
					        <select style="width: 200px; height: 28px;" class="input" name="usernameType" id="usernameType">
							    <option value="0">天兵</option>
								<option value="1">天将</option>
								<option value="2">天王</option>
								<option value="3">星君</option>
								<option value="4">真君</option>
								<option value="5">仙君</option>
								<option value="6">帝君</option>
								<option value="7">天尊</option>
								<option value="8">天帝</option>
								<option value="7">全部会员</option>
								<option value="8">全部代理</option>
							</select>
					    </div>
					    <div class="typeDiv2" style="display: none;">
					        <select style="width: 200px; height: 28px;" class="input" name="csType" id="csType">
								 <option value="cs1">
								     CS1
							   	</option>
								<option value="cs2">
									CS2
								</option>
								<option value="cs3">
									CS3
								</option>
								<option value="cs4">
									CS4
								</option>
								<option value="cs5">
									CS5
								</option>
								<option value="cs6">
									CS6
								</option>
							</select>
					    </div>
					</td>
					<td>多个指定账户格式为：A,B(英文状态下的逗号)</td>
				</tr>
				<tr>
					<td style="text-align: right;">
					<div  class="typeDiv2" style="display: none;">
						开始时间:
						<span style="color: red">*</span>
					</div>
					</td>
					<td width="210px;">
					<div  class="typeDiv2" style="display: none;">
						<s:textfield id="startTime" name="start" size="16" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
					</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td style="text-align: right;">
					<div  class="typeDiv2" style="display: none;">
						结束时间:
						<span style="color: red">*</span>
					</div>
					</td>
					<td width="210px;">
					<div  class="typeDiv2" style="display: none;">
						<s:textfield id="endTime" name="end" size="16" class="input"	onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"	My97Mark="false" value="%{endTime}" />
					</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td style="text-align: right;">
						主题:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
						<input name="title" style="width: 200px;" class="input" id="title" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td style="text-align: right;">
						是否需要审核:
					</td>
					<td width="210px;">
						<select style="width: 200px; height: 28px;" class="input"
							name="flag" id="flag">
							<option value="0">
								否
							</option>
							<option value="1">
								是
							</option>
						</select>
					</td>
					<td style="text-align: left;">
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						发贴时间:
					</td>
					<td width="210px;">
						<input name="createdate" style="width: 200px;" class="input"
							id="createdate"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							My97Mark="false" />
					</td>
					<td style="text-align: left;">
						如果不输入系统将自动得到当前时间
					</td>
				</tr>
				<tr>
					<td style="text-align: right;" valign="top">
						内容:
						<span style="color: red">*</span>
					</td>
					<td colspan="2">
						<textarea name="content" class="input" id="content"
							style="width: 467px; height: 150px;"></textarea>
					</td>
				</tr>
				<tr>
					<td>

					</td>
					<td colspan="2">
					     <s:submit value="提交" onclick="return submitFrom();" />
					</td>
				</tr>
			</table>
		</s:form>
		<c:import url="/office/script.jsp" />
	</body>
</html>
