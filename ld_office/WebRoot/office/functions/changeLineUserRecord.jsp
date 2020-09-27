<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>换线记录</title>
		<script type="text/javascript"
			src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
		<script type="text/javascript"
			src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
				<script type="text/javascript" src="/js/prototype_1.6.js"></script>
		<script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
		<script type="text/javascript" src="/js/jquery-1.4.4.min.js"></script>
	</head>
	<body>
		<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}
function orderby(by)
{
	if(document.mainform.order.value=="desc")
		document.mainform.order.value="asc";
	else
		document.mainform.order.value="desc";
	document.mainform.by.value=by;
	document.mainform.submit();
}
$(function () {
    $("#checkAllBox").bind("click", function () {
    	if($(this).attr("checked") == "checked" || $(this).attr("checked") == true){
        	$("[name = item]:checkbox").attr("checked", true);
    	}else{
    		$("[name = item]:checkbox").attr("checked", false);
    	}
    });
    $("[name = item]:checkbox").bind("click", function () {
    	if($(this).attr("checked") != "checked"){
    		$("#checkAllBox").attr("checked", false);
    	}
    	var flag = true ;
    	$("[name = item]:checkbox").each(function(){
    		if($(this).attr("checked") == undefined){
    			flag = false ;
    		}else{
    			flag = flag && $(this).attr("checked");
    		}
    	});
    	if(flag){
    		$("#checkAllBox").attr("checked", true);
    	}
    });
    
    $("#phoneQunHu").unbind().bind("click",function(){
    	
		var result = new Array();
        $("[name = item]:checkbox").each(function () {
            if ($(this).is(":checked")) {
                result.push($(this).attr("value"));
            }
        });
        var len = result.length ;
        if(len > 0){
        	if(confirm("共选中"+len+"个手机号，确认群呼？")){
        		var ids = result.join(",") ;
        		$.ajax({  
          	         type : "post",  
          	          url : "/office/executeCallDataAnalysis.do",  
          	          data : "ids=" + ids,  
          	          success : function(data){  
          	        	  alert(data);
          	          }  
          	       }); 
        	}
        }else{
        	alert("请选择您要发送的数据。");
        }	
    });
    
});

function submitChangeLineUserRecord(){
	return true;
 }
 
</script>
		<s:form action="queryChangeLineUserRecord" onsubmit="return submitChangeLineUserRecord();" namespace="/office" name="mainform"
			id="mainform" theme="simple">
			<div>
				换线分析 --&gt; 换线用户记录
				<a href="javascript:history.back();"><font color="red">上一步</font>
				</a>
			</div>

			<div id="excel_menu"
				style="position: absolute; top: 25px; left: 0px;">
				<s:hidden name="pageIndex" value="1"></s:hidden>
				<s:set name="by" value="'createTime'" />
				<s:set name="order" value="'desc'" />
				<s:hidden name="order" value="%{order}" />
				<s:hidden name="by" value="%{by}" />
			</div>
			<br />
			<div id="middle">
				<div id="right">
					<div id="right_01">
						<div id="right_001">
							<div id="right_02">
								<div id="right_03"></div>
							</div>
							<div id='textshow' align='left'><font color='gray'>&nbsp; &nbsp; 本页仅筛选有过变线的用户换线前存款，换线前输赢值为筛选初始时间到换线时的情况。<br />例如筛选“换线时间”从2018-3-6 0:0:0 至2018-3-8 0:0:0，用户换线时间为2018-3-7 0:0:0，则该用户的换线前存款和换线前输赢值计算范围为2018-3-6 0:0:0至2018-3-7 0:0:0。</font></div>
							<br />
							<div id="right_04">
							<table   border="0" align="center" cellpadding="0"
									cellspacing="1" bgcolor="#99c8d7">
									<tr align="left">
										<td colspan="9" align="left">
								<table  border="0" align="left" cellpadding="0"
									cellspacing="1" bgcolor="#99c8d7">
								<tr align="left" width="1450px">
							<td>
								筛选时间:
							    <s:textfield name="start" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"   My97Mark="false" value="%{startTime}" />
							</td>
							<td> <span style="margin-left:6px;">——</span><s:textfield  style="margin-left:5px;" name="end" size="15" value="%{endTime}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  />
							</td>
							<td>
								注册时间:
								<s:textfield name="startcreateTime" size="15" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  />
							</td>
							<td><span style="margin-left:-3px;">——</span><s:textfield name="endcreateTime" size="15" style="margin-left:4px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"  />
							</td>
							
						
							</tr>
							<tr align="left"   width="1450px">
							<td>
							        存款:
								<s:textfield name="startDeposit" size="15"></s:textfield>
							</td>
							<td>
							 <span style="margin-left:-27px;">——</span>
								<s:textfield name="endDeposit" size="15" style="margin-right:-10px;"></s:textfield>
							</td>
							<td>
							推荐码(换线后):
								<s:textfield name="codeAfter" size="10"></s:textfield>
							</td>
							<td>
							推荐码(换线前):
								<s:textfield name="codeBefore" size="10"></s:textfield>
							</td>
							
							
							</tr>
							
							<tr align="left"  width="1450px">
							<td>
							用户名:
								<s:textfield name="userName"  size="10"></s:textfield>
							</td>
							<td>
							玩家输赢:
								<s:textfield name="startWinOrLose"  size="10"></s:textfield>
							</td>
							<td>
							<span style="margin-left:-21px;">——</span><s:textfield name="endWinOrLose"  style="margin-left:3px;" size="10"></s:textfield>
							</td>
							<td>
								每页记录:
								<s:select cssStyle="width:59px"
									list="%{#application.PageSizes}"  name="size"></s:select>
							</td>
							</tr>
							<tr  align="left"  width="1450px">
							<td>电话状态:<s:select name="sms" list="#{'0':'未拨打','1':'已接通','2':'未接通','3':'被挂断','4':'空号'}"  emptyOption="true"/></td>
								<td>最近未拨打天数:
							 <s:select list="%{#application.CallDaysNumber}" listKey="code" listValue="text" name="callDayNum"  emptyOption="true"/></td>
							<td>
							   未登陆时间:
							   <s:select list="%{#application.DaysNumber}" listKey="code" listValue="text" name="dayNumflag"  emptyOption="true"/>
							</td>						
							<td>
								 &nbsp;&nbsp;<input type="button"  id="phoneQunHu" value="群呼" /> 
							</td>
						  
							</tr>
							<tralign="left">
							 <td>
							</td>
							 <td>
							</td>
							   <td rowspan="2">
								   <s:submit  onclick="" cssStyle="width:65px; height:65px; position: absolute;margin-left:500px;margin-top: -70px;" value="查询"></s:submit>
							   </td>
							</tr>
						</table>
										</td>
									</tr>
									
								   <tr >
             <td bgcolor="#0084ff" align="center"> <input type="checkbox" id="checkAllBox"></td>
               <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:20%;">用户名</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:20%;cursor: pointer;"  title="点击排序" onclick="orderby('changeLineTime');">换线时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:10%;">推荐码(换线后)</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:10%;">推荐码(换线前)</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:20%;">注册时间</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:10%;">换线前存款</td>
              <td bgcolor="#0084ff" align="center"  style="font-size:14px;color: #FFFFFF;font-weight: bold;width:10%;" >换线前输赢值</td>
            </tr>	
									<s:iterator var="fc" value="%{#request.page.pageContents}">
										<%
												request.setAttribute("bgcolorValue", "#e4f2ff");
										%>
										<s:if test="#fc.deposit>=50000">
											<%
												request.setAttribute("bgcolorValue", "#33FFFF");
											%>
										</s:if>
										<tr>
											<td bgcolor="${bgcolorValue }" align="center">
													<input type="checkbox" name="item" value="<s:property value="#fc.loginname"/>" />
											</td>
											
											<td bgcolor="${bgcolorValue }" align="center">
												<s:url action="getUserhavinginfo" namespace="/office" var="getUserhavinginfourl"><s:param name="loginname" value="%{#fc.userName}"/></s:url>
                                               <a target="_blank" href='<s:property value="%{getUserhavinginfourl}"/>' title="<s:text name='Account.list.usernametip'/>"> <s:property value="#fc.userName"/></a>
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.changeLineTime" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property   value="#fc.codeAfter" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property   value="#fc.codeBefore" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
										     <s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
												<s:property   value="#fc.deposit" />
											</td>
											<td bgcolor="${bgcolorValue }" align="center">
											   <s:property   value="#fc.winorlose" />
											</td>
											
											
										</tr>
									</s:iterator>
									<tr>
										<td bgcolor="#e4f2ff" align="right" colspan="6">
											总计:
										</td>
										<td bgcolor="#e4f2ff" align="right">
											<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)" />
										</td>
										<td bgcolor="#e4f2ff" align="right" >
										<s:property
												value="@dfh.utils.NumericUtil@double2String(#request.page.statics2)" />
										</td>
									</tr>
									<tr>
										<td colspan="11" align="right" bgcolor="66b5ff" align="center">
											${page.jsPageCode}
										</td>
									</tr>
								
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</s:form>
			
		<c:import url="/office/script.jsp" />
	</body>
</html>