<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<table>
	<tr>
		<td width="5%">
			<strong>序号</strong>
		</td>
		<td width="10%">
			<strong>年-月</strong>
		</td>
		<td width="15%">
			<strong>会员账号</strong>
		</td>
		<td width="15%">
			<strong>洗码优惠</strong>
		</td>
		<td width="15%">
			<strong>首存优惠</strong>
		</td>
		<td width="15%">
			<strong>EA/BBIN/KG1/KG2/SB/六合彩/PT/EBET</strong>
		</td>
		<td width="10%">
			<strong>其他优惠</strong>
		</td>
		<td width="5%">
			<strong>备注</strong>
		</td>
	</tr>
	<s:set var="sumXimaAmount" value="0.0" scope="request" />
	<s:set var="sumFirstDepositAmount" value="0" scope="request" />
	<s:set var="sumOtherAmount" value="0" scope="request" />
	<s:set var="sumAgAmount" value="0" scope="request" />
	<s:iterator var="fc" value="%{#request.page.pageContents}" status="st">
		<tr>
			<td>
				<s:property value="#st.index+1" />
			</td>
			<td>
				<s:property value="#fc.id.year" />
				-
				<s:property value="#fc.id.month" />
			</td>
			<td>
				<s:property value="#fc.id.loginname" />
			</td>
			<td>
				<s:property value="#fc.ximaAmount" />
			</td>
			<td>
				<s:property value="#fc.firstDepositAmount" />
			</td>
			<td>
				<s:property value="#fc.agAmount" />
			</td>
			<td>
				<s:property value="#fc.otherAmount" />
			</td>
			<td>
				<s:property value="#fc.remark" />
			</td>
		</tr>
		<s:set var="sumXimaAmount"
			value="%{#request.sumXimaAmount+#fc.ximaAmount}" scope="request" />
		<s:set var="sumFirstDepositAmount"
			value="%{#request.sumFirstDepositAmount+#fc.firstDepositAmount}"
			scope="request" />
		<s:set var="sumOtherAmount"
			value="%{#request.sumOtherAmount+#fc.otherAmount}" scope="request" />
	</s:iterator>

	<tr>
		<td colspan="3">
			当页小计：
		</td>
		<td>
			<s:property
				value="@dfh.utils.NumericUtil@formatDouble(#request.sumXimaAmount)" />
		</td>
		<td>
			<s:property
				value="@dfh.utils.NumericUtil@formatDouble(#request.sumFirstDepositAmount)" />
		</td>
		<td></td>
		<td>
			<s:property
				value="@dfh.utils.NumericUtil@formatDouble(#request.sumOtherAmount)" />
		</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="3">
			总计：
		</td>
		<td>
			${page.statics1}
		</td>
		<td>
			${page.statics2}
		</td>
		<td></td>
		<td>
			${page.statics3}
		</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="3">
			共${page.totalRecords}条 每页${size}条 当前${pageIndex}/${page.totalPages}
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentCommissionRecordTwo(1);"  class="pageA">首页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentCommissionRecordTwo(${pageIndex-1});"  class="pageA">上一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentCommissionRecordTwo(${pageIndex+1});"  class="pageA">下一页</a>
		</td>
		<td>
			<a href="javaScript:void(0);" onclick="agentCommissionRecordTwo(${page.totalPages});"  class="pageA">尾页</a>
		</td>
	</tr>
	<s:if test="#request._eareport!=1">
		<tr>
			<td colspan="5">
				EA报表利润：
			</td>
			<td colspan="3">
				<font color="red"><s:property
						value="@dfh.utils.NumericUtil@formatDouble(#request.cmm.eaProfitAmount)" />
				</font>
			</td>
		</tr>
		<tr>
			<td colspan="5">
				佣金(pt/ttg/mg平台输赢x85%+其他平台输赢x90%-优惠-反水)=纯利润纯利润x佣金比例=佣金
			</td>
			<td colspan="3">
				<font color="red"><s:property
						value="@dfh.utils.NumericUtil@formatDouble(#request.cmm.amount)" />
				</font>
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr>
			<td colspan="5">

				佣金=会员净利润x佣金比例={老虎机平台输赢x85%+其他平台输赢x90%-洗码优惠-其它优惠}x佣金比例(

				<s:property
					value="@dfh.utils.NumericUtil@formatDouble(#request.cmm.crate)*100" />
				%)：
			</td>
			<td colspan="3">
				<font color="red"><s:property
						value="@dfh.utils.NumericUtil@formatDouble(#request.cmm.amount)" />
				</font>
			</td>
		</tr>
	</s:else>
	<tr>
		<td colspan="8">
			<div class="content">
				<h2>佣金日结标准：</h2>

				<h3>①电子类：（包含pt、ttg、mg）</h3>
				<p>日结标准：所有老虎机合营均可享受日结。</p>
				<p>提款要求：老虎机平台佣金额度大于1000即可提款。<span style="color:red">备注: (1-5号老虎机佣金将自动开启月结模式，大于100元即可提款，此时间段佣金不更新，于6号统一执行！)</span></p>
				<p>注:享受日结的代理如果连续两个月没有新增一位活跃会员，将在第三个月的时候取消其日结模式，如果在第三个月有开发会员会员了，会在次月开启日结。</p>
				<h3>②其他类：（包含真人视讯、体育、六合彩、快乐彩）</h3>
				<p>1．老代理：历史佣金累计15万以上，且连续6个月有佣金记录，可升级为VIP代理，享有佣金日结。</p>
				<p>2．新代理：累计佣金30万以上，每月满足活跃会员要求，才能晋级VIP代理，享有日结佣金。</p>
				<p>提款要求：每月月初1-5号为其他平台佣金额度提款时间，请在这个时间段内申请提款，如果规定时间内未提款，佣金将累计至下月。</p>
				<p>注：最终综合评定方可晋级，佣金负值累计。若合作伙伴自己的游戏账号注册到自己代理下线，取消代理资格，佣金不予发放。</p>
			</div>
		</td>
	</tr>
</table>



