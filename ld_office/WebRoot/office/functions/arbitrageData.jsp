<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>套利用户数据</title>
		<link href="<c:url value='/css/excel.css' />" rel="stylesheet" type="text/css" />
		<style type="text/css">
			.td-wp {
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
				width: 100%;
				height: 32px;
			}
			.td-wp-auto {
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
				width: 100%;
				height: auto;
			}
		</style>
	</head>
	 <body>
	 	<s:hidden value="%{#session.operator.authority}" id="authorityText"></s:hidden>
		<p>账户&nbsp;--&gt;&nbsp;套利用户数据&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
		<c:choose>
         	<c:when test="${sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'boss'}">
			    <s:form action="importArbitrageInfo" namespace="/office" method ="post" enctype="multipart/form-data">
					<table>
						<tr>
							<td>
								<input id="addBtn" type="button" value="添加数据" onclick="addData(this);" />
							</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td align="right">
								<%-- <p align="left"><s:fielderror/></p> --%>
								文件:<input type="file" id="myFile" name="myFile"   size="20"/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="批量导入" />
							</td>
						</tr>
					</table>
				</s:form>
         	</c:when>
	    </c:choose>
		 <div id="excel_menu" style="height:auto;display:none">
			<div style=" left: 50px" align="left">
				账号：<input id="accountNameEdit" type="text" onchange="onQueryData()"/>
				<table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7" style="100%;">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr bgcolor="#0084ff">
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 80px;">姓名</td>
									<!-- <td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 80px;">手机</td>
									<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 80px;">邮箱</td> -->
								</tr>
								<tr>
									<td id="accountName" align="center"></td>
									<td id="phone" align="center"></td>
									<td id="email" align="center"></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</br>
			<div style="margin-left: 5px" >
				选择套利方式</br>
				<div id = "arbitrageTypeDiv"></div>
				</br>
			</div>
				备注:</br>
			<div>
				<textarea name="remark" class="input" id="remark"
							style="width: 467px; height: 150px;"></textarea>
			</div>
			</br>
			<div style=" margin-left: 5px" >
					<input  type="button" value="确定" onclick="onConfirm(this);" />
					<input  type="button" value="取消" onclick="onCancel(this);" />
				</div>
			</div>
		</div>
		<div class="clear"></div>
		</br>
		<div style="left: 0px">
		<s:form action="queryArbitrageInfo" namespace="/office" name="mainform" id="mainform" theme="simple">
			<table>
				<tr align="left">
					<td>账号:</br><s:textfield id="loginname" name="arbitrageData.loginname"  /></td>
					<td>套利项目:</br>
						<s:textfield id="arbitrage_project_show" name="arbitrageData.arbitrage_project_show"  onclick="chooseProject(1)" readonly="readonly" />
						<s:textfield id="arbitrage_project" name="arbitrageData.arbitrage_project" hidden="true"/>
						<div id="arbitrageTypeSelect" style="height:auto;display:none">
							<div id="projectCheckbox"></div>
							<input  type="button" value="确定" onclick="chooseProject(2);" />
						</div>
					</td>
					<td>姓名:</br><s:textfield id="accountNameinfo" name="arbitrageData.account_name"  /></td>
					<td>手机号、地址、运营商:</br><s:textfield id="phoneinfo" name="arbitrageData.phone"  /></td>
					<td>邮箱:</br><s:textfield id="emailinfo" name="arbitrageData.email"  /></td>
					<td>微信:</br><s:textfield id="wechart" name="arbitrageData.wechart"  /></td>
				</tr>
				<tr align="left">
					<td>注册时间（起）:</br>
						<s:textfield id="regStartTime" name="regStartTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate"/>
					</td>
					<td>注册时间（止）:</br>
						<s:textfield id="regEndTime" name="regEndTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate"/>
					</td>
					<td>最后登录时间（起）:</br>
						<s:textfield id="lastloginStartTime" name="lastloginStartTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate"/>
					</td>
					<td>最后登录时间（止）:</br>
						<s:textfield id="lastloginEndTime" name="lastloginEndTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  cssClass="Wdate"/>
					</td>
					<td>操作时间（起）:</br>
						<s:textfield id="operateStartTime" name="operateStartTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{operateStartTime}" cssClass="Wdate"/>
					</td>
					<td>操作时间（止）:</br>
						<s:textfield id="operateEndTime" name="operateEndTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" value="%{operateEndTime}" cssClass="Wdate"/>
					</td>
					<td>
						每页记录:</br>
						<s:select cssStyle="width:173px"
								  list="%{#application.PageSizes}" name="size"></s:select>
					</td>
				</tr>
				<tr align="left">
					<td>注册IP、省份、城市:</br><s:textfield id="register_ip" name="arbitrageData.register_ip"/></td>
					<td>手机ID:</br><s:textfield id="phone_id" name="arbitrageData.phone_id"/></td>
					<td>绑定银行卡号、银行、姓名:</br><s:textfield id="bind_bankcard" name="arbitrageData.bind_bankcard"/></td>
					<td>存款银行卡号、银行、姓名:</br><s:textfield id="deposit_bankcard" name="arbitrageData.deposit_bankcard"/></td>
					<td>上级代理:</br><s:textfield id="agent" name="arbitrageData.agent"/></td>
					<td>所属产品：</br><s:select style="height:21px; width: 173px;" id="source" name="arbitrageData.source" list="#{'qy':'qy','l8':'l8','e68':'e68','777':'777','ule':'ule','ufa':'ufa','qle':'qle','mzc':'mzc','ws':'ws','zb':'zb','hu':'hu','ld':'ld'}"  emptyOption="true"/></td>
					<td>登录IP、省份、城市:</br><s:textfield id="login_ip" name="arbitrageData.login_ip"/></td>
				</tr>
				<tr align="center">
					<td colspan="15">
						<s:submit value="查询"></s:submit>
						<input id="resetBtn" type="button" value="重置" onclick="onReset();" />
					</td>	
				</tr>
			</table>
			<s:set name="order" value="'desc'" />
			<s:hidden name="order" value="%{order}" />
			<s:hidden name="by" value="%{by}" />
			<s:hidden name="pageIndex" />
			
			<table width="1600px" border="1" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
				<tr bgcolor="#0084ff">
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;" onclick="orderby('loginname');">账号</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">套利项目</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;" onclick="orderby('account_name');">姓名</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">手机</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">邮箱</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">微信</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">注册IP</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">注册时间</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">登录IP(最近100条)</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">最后登录</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">手机ID</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">绑定银行卡</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">存款银行卡</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">上级代理</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">所属产品</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">备注</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">操作信息</td>
					<td align="center" style="color: #FFFFFF; font-weight: bold; cursor: pointer; width: 40px;">操作</td>
				</tr>
				<s:iterator var="fc" value="%{#request.page.pageContents}">
				<tr>
					<td style="display: none">
						<s:property value="#fc.id"/>
					</td>
					<td align="center">
						<s:property value="#fc.loginname" />
					</td>
					<td align="left" style="width: 80px;">
						<s:property value="#fc.arbitrage_project" />
					</td>
					<td align="center">
						<s:property value="#fc.account_name" />
					</td>
					<td align="center">
						<s:property value="#fc.phone" />
					</td>
					<td align="center">
						<s:property value="#fc.email" />
					</td>
					<td align="center">
						<s:property value="#fc.wechart" />
					</td>
					<td align="center">
						<s:property value="#fc.register_ip" />
					</td>
					<td align="center">
						<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.register_time" />
					</td>
					<td align="left" style="width: 460px;">
						<div class='<s:property value="#fc.id" /> td-wp'><s:property value="#fc.login_ip" escape="false"/></div>
						<s:if test="#fc.login_ip !=null && #fc.login_ip != ''">
							<input class='<s:property value="#fc.id" />btn1' type="button" value="显示更多" onclick="changeCSS('<s:property value="#fc.id" />','xs');" />
							<input class='<s:property value="#fc.id" />btn2' type="button" value="收起" onclick="changeCSS('<s:property value="#fc.id" />','yc');" hidden="true"/>
						</s:if>
					</td>
					<td align="left">
						<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.login_time" />
					</td>
					<td align="left">
						<div class='<s:property value="#fc.id" /> td-wp'><s:property value="#fc.phone_id" escape="false"/></div>
						<s:if test="#fc.phone_id!=null && #fc.phone_id != ''">
							<input class='<s:property value="#fc.id" />btn1' type="button" value="显示更多" onclick="changeCSS('<s:property value="#fc.id" />','xs');" />
							<input class='<s:property value="#fc.id" />btn2' type="button" value="收起" onclick="changeCSS('<s:property value="#fc.id" />','yc');" hidden="true"/>
						</s:if>
					</td>
					<td align="left">
						<div class='<s:property value="#fc.id" /> td-wp'><s:property value="#fc.bind_bankcard" escape="false"/></div>
						<s:if test="#fc.bind_bankcard !=null && #fc.bind_bankcard != ''">
							<input class='<s:property value="#fc.id" />btn1' type="button" value="显示更多" onclick="changeCSS('<s:property value="#fc.id" />','xs');" />
							<input class='<s:property value="#fc.id" />btn2' type="button" value="收起" onclick="changeCSS('<s:property value="#fc.id" />','yc');" hidden="true"/>
						</s:if>
					</td>
					<td align="center">
						<s:property value="#fc.deposit_bankcard" escape="false"/>
					</td>
					<td align="center">
						<s:property value="#fc.agent" />
					</td>
					<td align="center">
						<s:property value="#fc.source" />
					</td>
					<td align="center">
						<s:property value="#fc.remark" />
					</td>
					<td align="center">
						<s:property value="#fc.operate_time" />
						<s:property value="#fc.operator" />
					</td>
					<td align="center">
						<c:choose>
	         				<c:when test="${(sessionScope.operator.authority eq 'sale_manager' || sessionScope.operator.authority eq 'boss')&& fc.source eq 'ld'}">
								<input id="addBtn" type="button" value="修改" onclick="editData('<s:property value="#fc.id" />');" />
								<input id="addBtn" type="button" value="删除" onclick="deleteData('<s:property value="#fc.id" />');" />
	         				</c:when>
	         			</c:choose>
					</td>
				</tr>
				</s:iterator>
				<tr>
					<td colspan="18" align="right" bgcolor="66b5ff" align="center">
						${page.jsPageCode}
					</td>
				</tr>
			</table>
		</s:form>
		</div>
		<c:import url="/office/script.jsp" />
	</body>
	<script type="text/javascript" src="/js/prototype_1.6.js"></script>
	<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
	<script type="text/javascript">
		$(function(){
			var authorityText =$('#authorityText').val();
			if(authorityText=='boss'||authorityText=='admin' || authorityText=='sale_manager' || authorityText=='market_manager' ){
				
			}else{
				alert('您没有权限查看此页面');
				history.back();
			}
			queryDictionary();
		});
		function queryArbitrageInfo(val){
			document.mainform.loginname.value = val
			document.mainform.submit();
		};
		
		function gopage(val) {
			document.mainform.pageIndex.value = val;
			document.mainform.submit();
		};

		function orderby(by) {
			if (document.mainform.order.value == "desc") {
				
				document.mainform.order.value = "asc";
			} else {
				
				document.mainform.order.value = "desc";
			}
			
			document.mainform.by.value = by;
			document.mainform.submit();
		};
		function editData(val){
			var height = window.screen.height;
			var width =window.screen.width; 
			window.open("<%=basePath%>office/functions/queryArbitrageDataEdit.do?arbitrageData.id="+val+"","","height=650, width=920,toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,top=75,left=300");
		}
		function deleteData(val){
			if(confirm("你确认要执行此操作么？")){
				$.ajax({
					url: "${ctx}/office/deleteData.do",
					type: "post",
					async : true,
					data:"arbitrageData.id="+val,
					timeout: 0,
					success: function (data) {
						document.mainform.submit();
						alert("删除成功！");
					},
					error: function () {
						alert("操作超时导致结果未知，请联系技术查看情况....");
					}
				});
			}
		}
		function chooseProject(val){
			if(val==1){
				$("#arbitrageTypeSelect").show();
				$("#arbitrage_project_show").val("");
				$("#arbitrage_project").val("");
			}else{
				$("#arbitrageTypeSelect").hide();
		        var obj = document.getElementsByName("arbitrageProject");
		        var check_val = [];
		        var check_title = [];
		        for(k in obj){
		            if(obj[k].checked){
		                check_val.push(obj[k].value);
		            	check_title.push(obj[k].title);
		            }
		        }
		        $("#arbitrage_project_show").val(check_title);
		        $("#arbitrage_project").val(check_val);
			}
		}
		function queryDictionary(){
			$.ajax({
				url: "${ctx}/office/queryDictionary.do",
				type: "post",
				async : true,
				data:"type=arbitrage_type",
				timeout: 0,
				success: function (data) {
					var arbitrageType = $("#arbitrageTypeDiv");
					arbitrageType.empty();
					var projectCheckbox = $("#projectCheckbox");
					projectCheckbox.empty();
					
					for(var i=0; i<data.length; i++){
						arbitrageType.append("<input type='checkbox' name='arbitrageType' value='"+data[i].dictName+"'>"+data[i].dictValue);
						projectCheckbox.append("<input type='checkbox' name='arbitrageProject' value='"+data[i].dictName+"' title='"+data[i].dictValue+"'>"+data[i].dictValue);
					}
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
				}
			});
		}
		function addData(th)
		{
		    var self = $(th);
			self.attr("disabled", true);
		    $("#excel_menu").show();
		}
		function onQueryData()
		{
			var loginname = $("#accountNameEdit").val();
			if(loginname == ''){
				alert("请输入账号");
				return
			}
			
			$.ajax({
				url: "${ctx}/office/queryArbitrageShortInfo.do",
				type: "post",
				async : true,
				timeout: 0,
				data:"arbitrageData.loginname="+loginname,
				success: function (data) {
					if (data == "账号不存在！"){ 
						alert(data);
						return;
					}
					var accountName = data.accountName;
					var phone = data.phone;
					var email = data.email;
					$("#accountName").html(accountName);
				    $("#phone").html(phone);
				    $("#email").html(email);
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
				}
			});
		}
		function onConfirm(th)
		{
			var str="";
			$("input[name='arbitrageType']:checked").each(function(){   
			     str+=$(this).val();   
			 });
			var loginname = $("#accountNameEdit").val();
			var remark = $("#remark").val();
			if(loginname ==null || loginname == ""){
				alert("请输入账号！");
				return;
			}
			if(str == ""){
				alert("请选择套利方式！");
				return;
			}
			
			$.ajax({
				url: "${ctx}/office/addArbitrageInfo.do",
				type: "post",
				async : true,
				timeout: 0,
				data:"arbitrageData.loginname="+loginname+"&type="+str+"&arbitrageData.remark="+remark,
				success: function (data) {
					queryArbitrageInfo(loginname);
				},
				error: function () {
					alert("操作超时导致结果未知，请联系技术查看情况....");
				}
			});
			onCancel();
		}
		function onCancel(){
			$("#addBtn").removeAttr("disabled");
		    $("#excel_menu").hide();
		    $("#accountNameEdit").val("");
		    $("#accountName").html("");
		    $("#phone").html("");
		    $("#email").html("");
		}
		
		function onReset(){
			$("#loginname").val("");
			$("#accountNameinfo").val("");
			$("#phoneinfo").val("");
			$("#emailinfo").val("");
			$("#wechart").val("");
			$("#regStartTime").val("");
			$("#regEndTime").val("");
			$("#lastloginStartTime").val("");
			$("#lastloginEndTime").val("");
			$("#operateStartTime").val("");
			$("#operateEndTime").val("");
			$("#register_ip").val("");
			$("#phone_id").val("");
			$("#bind_bankcard").val("");
			$("#deposit_bankcard").val("");
			$("#agent").val("");
			$("#arbitrage_project_show").val("");
			$("#arbitrage_project").val("");
			$("#source").html("");
			$("#login_ip").val("");
			
		}
		function changeCSS(v1,v2){
			if(v2=='xs'){
			  $("."+v1).addClass("td-wp-auto");
			  $("."+v1+"btn1").hide();
			  $("."+v1+"btn2").show();
			}
			if(v2=='yc'){
			  $("."+v1).removeClass("td-wp-auto");
			  $("."+v1+"btn2").hide();
			  $("."+v1+"btn1").show();
			}
		}
		
	</script>
</html>