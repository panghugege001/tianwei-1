<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<form name="mainform" action="${ctx}/asp/queryFlowerRanking.aspx" method="post" data-dataform>

	<div style="margin-left: 20px" class="dp-none">
		查询方式：
		<s:select cssStyle="border:1px solid #ddd; height:27px;"
				  list="#{0:'个人查询',1:'女神查询'}" value="%{querytype}"
				  onchange="queryFlowerRecord();" name='querytype'
				  id="querytype"></s:select>
	</div>
	<table class="table data-table page tb1">
		<thead>
		<tr>
			<th>守护女神名字</th>
			<th>鲜花数量</th>
			<th>总投注额</th>
			<th>排名</th>
			<th>红包优惠码</th>
			<th>创建时间</th>
		</tr>
		</thead>
		<tbody>

		<s:iterator var="fcg" value="%{#request.list}"
					status="st">
			<tr>
				<td>
					<s:property value="#fcg.goddessname" />
				</td>
				<td>
					<s:property value="#fcg.flowernum" />
				</td>
				<td>
					<s:property value="@dfh.utils.NumericUtil@double2String(#fcg.bettotal)" />
				</td>
				<td>
					<s:property value="#fcg.ranking" />
				</td>
				<td>
					<s:property value="#fcg.couponnum" />
				</td>
				<td>
					<s:date format="yyyy-MM-dd HH:mm:ss" name="#fcg.createtime"/>
				</td>
			</tr>
		</s:iterator>

		</tbody>
	</table>
	<s:if test="#request.querytype!=1">
		<h2 class="dp-none"></h2>
		<table class="table data-table page tb2">
			<thead>
			<tr>
				<th>排名</th>
				<th>玩家账号</th>
				<th>守护女神名字</th>
				<th>鲜花数量</th>
				<th>总投注额</th>
			</tr>
			</thead>
			<tbody>

			<s:iterator var="fc" value="%{#request.page.pageContents}"
						status="st">
				<tr>
					<td>
						<s:property value="#fc.ranking" />
					</td>
					<td>
						<s:property value="#fc.loginname" />
					</td>
					<td>
						<s:property value="#fc.goddessname" />
					</td>
					<td>
						<s:property value="#fc.flowernum" />
					</td>
					<td>
						<s:property value="@dfh.utils.NumericUtil@double2String(#fc.bettotal)" />
					</td>

				</tr>
			</s:iterator>

			</tbody>

		</table>

		<div class="pagination" >
			<input type="hidden" name="pageIndex" value="1" id="pageIndex"/>
			<input type="hidden" name="size" value="10" />
				${page.jsPageCode}
		</div>
	</s:if>
</form>

						
					
					