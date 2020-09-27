<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<s:if test="#session.customer.role=='MONEY_CUSTOMER'">
	<ul id="welcomelist" style="margin-left: 0px;width: 750px;">
	     <li><a href="inventory_member.asp" style="color:#b1aa9e;">账户清单</a></li>
	     <li><a href="${pageContext.request.contextPath}/asp/payPage.aspx" style="color:#b1aa9e;">存入资金</a></li>
	     <li><a href="transfer_member.asp" style="color:#b1aa9e;">户内转账</a></li>
	     <li><a href="request_member.asp" style="color:#b1aa9e;">申请提款</a></li>
	     <li><a href="self_member.asp" style="color:#b1aa9e;">EA自助反水</a></li>
	     <!--<li><a href="self_member_pt.asp" style="color:#b1aa9e;">PT自助反水</a></li>  -->
	     <li><a href="changepassword_member.asp" style="color:#b1aa9e;">修改密码</a></li>
	     <li><a href="modify_member.asp" style="color:#b1aa9e;">修改资料</a></li>
	     <li><a href="customizedservice.asp" style="color:#b1aa9e;">短信通知</a></li>
	     <li><a href="transfer_membercoupon.asp" style="color:#b1aa9e;">优惠劵</a></li>
         <li><a href="transfer_membercouponsb.asp" style="color:#b1aa9e;">体育平台优惠劵</a></li>
	     <li><div class="clear"></div></li>
	</ul>
</s:if>
<s:else>
	<s:url action="querySubUsers" namespace="/asp" var="querySubUsersUrl"></s:url>
	<ul id="welcomelist" >
		 <li><a href="searchcreditlogs_member.asp" style="color:#b1aa9e;">额度记录</a></li>
		 <li><a href="searchsubuserproposal.asp" style="color:#b1aa9e;">下线提案</a></li>
		 <li><a href="agprofit.asp" style="color:#b1aa9e;">平台输赢</a></li>
		 <li><a href="${querySubUsersUrl }" style="color:#b1aa9e;">下线会员</a></li>
	     <li><a href="commissionrecords_member.asp" style="color:#b1aa9e;">佣金明细</a></li>
	     <li><a href="request_member.asp" style="color:#b1aa9e;">申请提款</a></li>
	     <li><a href="link_member.asp" style="color:#b1aa9e;">推广链接</a></li>
	     <li><a href="changepassword_member.asp" style="color:#b1aa9e;">修改密码</a></li>
	     <li><a href="modify_member.asp" style="color:#b1aa9e;">修改资料</a></li>
	     
	     <li><a href="accountAmount_member.asp" style="color:#b1aa9e;">账户结余</a></li>
	     <li><div class="clear"></div></li>
	</ul>
</s:else>
<script type="text/javascript">
function gopage(val)
{
    document.mainform.pageIndex.value=val;
    document.mainform.submit();
}
</script>