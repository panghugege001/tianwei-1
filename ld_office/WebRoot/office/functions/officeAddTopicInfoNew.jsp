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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.4.4.min.js"></script>
<style type="text/css">
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
		function checkdate(start, end) {
			if (null == start || null == end) {
				alert("时间选项不能为空");
				return 0;
			}
			return 1;
			if (end <= start) {
				alert("结束时间需要大于开始时间");
				return 0;
			}
			var newStart = new Date(start);
			var newEnd = new Date(end);
			if ((newEnd.getTime() - newStart.getTime() > 15 * 24 * 60 * 60 * 1000)) {
				alert("结束时间和开始时间相差不能超过15天");
				return 0;
			}
			return 1;
		}
		
		function showAndHidden(eid) {
			if (eid == "a1") {
				document.getElementById("a1").style.display = "block";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "none";
			} else if (eid == "a2") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "block";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "none";
			} else if (eid == "a3") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "block";
				document.getElementById("a4").style.display = "none";
			} else if (eid == "a4") {
				document.getElementById("a1").style.display = "none";
				document.getElementById("a2").style.display = "none";
				document.getElementById("a3").style.display = "none";
				document.getElementById("a4").style.display = "block";
			}
		}
         function submitFrom1(){
        	 var username=$("#username").val();
         	 if(username==""){
              	alert("账号不能为空！");
		          	return false;
         	 }
        	 var title=$("#title1").val();
             if(title==""){
                  alert("主题不能为空!");
   		          return false;
             }
             var content=$("#content1").val();
             if(content==""){
                  alert("内容不能为空!");
   		          return false;
             }
             $("#form1").submit();
          }
         function submitFrom2(){
             var startTime = $("#startTime").val();
        	 var endTime = $("#endTime").val();
        	 if(startTime=="" || endTime==""){
        		 alert("客服代码群发时，时间不能为空");
        		 return false ;
        	 }
             var title=$("#title2").val();
             if(title==""){
                  alert("主题不能为空!");
   		          return false;
             }
             var content=$("#content2").val();
             if(content==""){
                  alert("内容不能为空!");
   		          return false;
             }
             $("#form2").submit();
          }
         function submitFrom3(){
             var title=$("#title3").val();
             if(title==""){
                  alert("主题不能为空!");
   		          return false;
             }
             var content=$("#content3").val();
             if(content==""){
                  alert("内容不能为空!");
   		          return false;
             }
             $("#form3").submit();
          }
         function submitFrom4(){
             var title=$("#title4").val();
             if(title==""){
                  alert("主题不能为空!");
   		          return false;
             }
             var content=$("#content4").val();
             if(content==""){
                  alert("内容不能为空!");
   		          return false;
             }
             $("#form4").submit();
          }
	</script>
	</head>
	<body>
		<div id="excel_menu_left" style="margin-top: 20px;">
			站内信 --&gt; 发信箱
			<s:url action="queryTopicInfoList" namespace="/office"
				var="queryWordsForBackUrl">
				<s:param name="start" value="%{startTime}"></s:param>
				<s:param name="end" value="%{endTime}"></s:param>
			</s:url>
			<s:a href="%{queryWordsForBackUrl}">
				<font color="red">返回</font>
			</s:a>
		</div>
		<div style="position: absolute; top: 50px; left: 50px; height: 25px; width: 100px; background: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a1');"><b>个人</b> </a>
			</div>
		</div>
		<div style="position: absolute; top: 50px; height: 25px; left: 152px; width: 150px; background-color: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a2');"><b>按会员等级群发</b> </a>
			</div>
		</div>
		<div style="position: absolute; top: 50px; height: 25px; left: 304px; width: 150px; background-color: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a3');"><b>按客服代码群发</b> </a>
			</div>
		</div>
		<div style="position: absolute; top: 50px; height: 25px; left: 456px; width: 150px; background-color: #09F">
			<div align=center style="margin-top: 4px;">
				<a style="text-decoration: none; text-decoration: none"
					href="javascript:showAndHidden('a4');"><b>按代理账户群发</b> </a>
			</div>
		</div>
		<div id="a1"
			style="border: solid; border-width: 1px; position: absolute; display: block; height: 475px; top: 76px; left: 50px; width: 810px; background-color: #CCC">
			<s:form action="saveTopicInfo" onsubmit="submitonce(this);" namespace="/office" name="form1" id="form1" theme="simple">
				<input name="type" value="0" type="text" style="display:none"/>
			<table align="left" border="0"
				style="margin-top: 10px; width: 780px;">
				<tr>
					<td style="text-align: right;">
						收件人:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
					    <div id="typeDiv0">
					        <!-- <input name="username" style="width: 200px;" class="input" id="username" /> -->
					        <textarea id="username" name="username" cols="50" rows="6" id="username" class="input" style="height:80px;" onfocus="addDh()"></textarea>
					    </div>
					</td>
					<td>多个指定账户格式为：A,B(英文状态下的逗号)</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						主题:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
						<input name="title" style="width: 200px;" class="input" id="title1" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td width="120px;" style="text-align: right;">
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
						<textarea name="content" class="input" id="content1"
							style="width: 467px; height: 150px;"></textarea>
					</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="2">
					     <s:submit value="提交" onclick="return submitFrom1();" />
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		
		<div id="a2"
			style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 76px; left: 50px; width: 810px; background-color: #CCC">
			<s:form action="saveTopicInfo" onsubmit="submitonce(this);" namespace="/office" name="form2" id="form2" theme="simple">
			<input name="type" value="1" type="text" style="display:none"/>
			<table align="left" border="0"
				style="margin-top: 10px; width: 700px;">
				<tr>
					<td style="text-align: right;">
						收件人:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
						<div id="typeDiv1">
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
								<option value="100">全部会员</option>
								<option value="101">全部代理</option>
							</select>
					    </div>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
					<div  class="timeDiv">
						开始时间:
						<span style="color: red">*</span>
					</div>
					</td>
					<td width="210px;">
					<div  class="timeDiv">
						<s:textfield id="startTime" name="start" size="16" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
					</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td style="text-align: right;">
					<div  class="timeDiv">
						结束时间:
						<span style="color: red">*</span>
					</div>
					</td>
					<td width="210px;">
					<div  class="timeDiv">
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
						<input name="title" style="width: 200px;" class="input" id="title2" />
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
						<textarea name="content" class="input" id="content2"
							style="width: 467px; height: 150px;"></textarea>
					</td>
				</tr>
				<tr>
					<td>

					</td>
					<td colspan="2">
					     <s:submit value="提交" onclick="return submitFrom2();" />
					</td>
				</tr>
			</table>
		</s:form>
		</div>
		<div id="a3"
			style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 76px; left: 50px; width: 810px; background-color: #CCC">
			<s:form action="saveTopicInfo" onsubmit="submitonce(this);" namespace="/office" name="form3" id="form3" theme="simple">
			<input name="type" value="2" type="text" style="display:none"/>
			<table align="left" border="0"
				style="margin-top: 10px; width: 700px;">
				<tr>
					<td style="text-align: right;">
						收件人:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
					    <div id="typeDiv2">
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
								<option value="cs7">
									CS7
								</option>
								<option value="cs8">
									CS8
								</option>
								<option value="cs9">
									CS9
								</option>
								<option value="cs10">
									CS10
								</option>
								<option value="cs11">
									CS11
								</option>
								<option value="cs12">
									CS12
								</option>
								<option value="ts1">
									TS1
								</option>
								<option value="ts2">
									TS2
								</option>
								<option value="ts3">
									TS3
								</option>
								<option value="ts4">
									TS4
								</option>
								<option value="ts5">
									TS5
								</option>
								<option value="ts6">
									TS6
								</option>
								<option value="ts7">
									TS7
								</option>
								<option value="ts8">
									TS8
								</option>
								<option value="ts9">
									TS9
								</option>
								<option value="ts10">
									TS10
								</option>
								<option value="ts11">
									TS11
								</option>
								<option value="ts12">
									TS12
								</option>
							</select>
					    </div>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
					<div  class="timeDiv">
						开始时间:
						<span style="color: red">*</span>
					</div>
					</td>
					<td width="210px;">
					<div  class="timeDiv">
						<s:textfield id="startTime" name="start" size="16" class="input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{startTime}" />
					</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<td style="text-align: right;">
					<div  class="timeDiv">
						结束时间:
						<span style="color: red">*</span>
					</div>
					</td>
					<td width="210px;">
					<div  class="timeDiv">
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
						<input name="title" style="width: 200px;" class="input" id="title3" />
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
						<textarea name="content" class="input" id="content3"
							style="width: 467px; height: 150px;"></textarea>
					</td>
				</tr>
				<tr>
					<td>

					</td>
					<td colspan="2">
					     <s:submit value="提交" onclick="return submitFrom3();" />
					</td>
				</tr>
			</table>
		</s:form>
		</div>
		<div id="a4"
			style="border: solid; border-width: 1px; position: absolute; display: none; height: 475px; top: 76px; left: 50px; width: 810px; background-color: #CCC">
			<s:form action="saveTopicInfo" onsubmit="submitonce(this);" namespace="/office" name="form4" id="form4" theme="simple">
				<input name="type" value="3" type="text" style="display:none"/>
			<table align="left" border="0"
				style="margin-top: 10px; width: 780px;">
				<tr>
					<td style="text-align: right;">
						代理账号:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
					    <div id="typeDiv0">
					        <textarea id="username" name="username" cols="50" rows="6" id="username" class="input" style="height:80px;" onfocus="addDh()"></textarea>
					    </div>
					</td>
					<td>多个指定账户格式为：A,B(英文状态下的逗号)</td>
				</tr>
				<tr>
					<td style="text-align: right;">会员等级</td>
					<td width="210px;">
						<div id="typeDiv1">
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
								<option value="100">全部会员</option>
							</select>
					    </div>
					</td>
				</tr>
				<tr>
					<td width="120px;" style="text-align: right;">
							是否存款:
					</td>
					<td width="210px;">
						<select style="width: 200px; height: 28px;" class="input"
							name="isdeposit" id="isdeposit">
							<option value="">
								全部
							</option>
							<option value="N">
								否
							</option>
							<option value="Y">
								是
							</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">
						主题:
						<span style="color: red">*</span>
					</td>
					<td width="210px;">
						<input name="title" style="width: 200px;" class="input" id="title4" />
					</td>
					<td></td>
				</tr>
				<tr>
					<td width="120px;" style="text-align: right;">
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
					<td>
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
						<textarea name="content" class="input" id="content4"
							style="width: 467px; height: 150px;"></textarea>
					</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="2">
					     <s:submit value="提交" onclick="return submitFrom4();" />
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
</html>
