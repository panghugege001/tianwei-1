<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="dfh.model.enums.CardType"%>

<table class="table data-table">
	<tr>
		<th>序号</th>
		<th>存款人姓名</th>
		<th>存款方式</th>
		<th>订单号</th>
		<th>昵称</th>
		<th>存款时间</th>
		<th>金额</th>
		<%--<th>存款截图</th>--%>
		<th>点卡类型</th>
		<th>点卡卡号</th>
		<th>状态</th>
		<th>创建时间</th>
		<th>更新时间</th>
		<th>备注</th>
        
        
        <%--
        <td width="30"><strong>序号</strong></td>
		<td width="70"><strong>存款人姓名</strong></td>
		<td width="56"><strong>存款方式</strong></td>
		<td width="70"><strong>订单号</strong></td>
		<td width="30"><strong>昵称</strong></td>
		<td width="140"><strong>存款时间</strong></td>
		<td width="90"><strong>金额</strong></td>
		<!-- <td><strong>存款截图</strong></td> --> 
		<td width="56"><strong>点卡类型</strong></td>
		<td width="56"><strong>点卡卡号</strong></td>
		<td width="100"><strong>状态</strong></td>
		<td width="140"><strong>创建时间</strong></td>
		<td width="140"><strong>更新时间</strong></td>
		<td width="30"><strong>备注</strong></td>
        --%>
	</tr>
	<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
	<tr>
		<td><s:property value="#st.index+1" /></td>
		<td><s:property value="#fc.accountName"/></td>
		<td>
			<s:if test='#fc.type=="1"'>在线支付宝扫描</s:if>
			<s:elseif test='#fc.type=="2"'>支付宝扫描</s:elseif>
			<s:elseif test='#fc.type=="3"'>支付宝附言</s:elseif>
			<s:elseif test='#fc.type=="4"'>微信扫描</s:elseif>
			<s:elseif test='#fc.type=="5"'>微信额度验证</s:elseif>
			<s:elseif test='#fc.type=="6"'>在线支付</s:elseif>
			<s:elseif test='#fc.type=="7"'>工行附言</s:elseif>
			<s:elseif test='#fc.type=="8"'>招行附言</s:elseif>
			<s:elseif test='#fc.type=="9"'>点卡支付</s:elseif>
		</td>
		<td><s:property value="#fc.thirdOrder"/></td>
		<td><s:property value="#fc.nickname"/></td>
		<td><s:property value="#fc.tempDepositTime"/></td>
		<td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)"/></td>
		<td><s:property value="@dfh.model.enums.CardType@getText(#fc.cardtype)" /></td>
		<td><s:property value="#fc.cardno"/></td>
		<td>
			<s:if test="#fc.status==0">待处理</s:if>
			<s:elseif test="#fc.status==1">处理成功</s:elseif>
			<s:elseif test="#fc.status==2">处理失败</s:elseif>
		</td>
		<td><s:property value="#fc.tempCreateTime"/></td>
		<td><s:property value="#fc.tempUpdateTime"/></td>
		<s:if test="#fc.status==1">
			<td><s:property value=""/></td>
		</s:if>
		<s:else>
			<td><s:property value="#fc.remark"/></td>
        </s:else>
	</tr>
	</s:iterator>
</table>
<table class="pagination">
<s:if test="pageIndex>=#request.page.totalPages">
	<tr>
		<td>
			共${page.totalRecords}条 每页${size}条 当前${page.totalPages}/${page.totalPages}
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(1);">首页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(${pageIndex-1});">上一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" style="">下一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(${page.totalPages});">尾页</a>
		</td>
	</tr>
</s:if>
<s:else>
	<tr>
		<td>
			共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(1);">首页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(${pageIndex-1});">上一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(${pageIndex+1});">下一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="urgeOrderRecord(${page.totalPages});">尾页</a>
		</td>
	</tr>
</s:else>
	
</table>