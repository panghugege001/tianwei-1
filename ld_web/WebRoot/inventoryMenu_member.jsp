<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
	<s:url action="depositRecords" namespace="/asp" var="depositRecordsUrl"></s:url>
	<s:url action="cashinRecords" namespace="/asp" var="cashinRecordsUrl"></s:url>
	<s:url action="withdrawRecords" namespace="/asp" var="withdrawRecordsUrl"></s:url>
	<s:url action="transferRecords" namespace="/asp" var="transferRecordsUrl"></s:url>
	<s:url action="consRecords" namespace="/asp" var="consRecordsUrl"></s:url>
	<s:url action="couponRecords" namespace="/asp" var="couponRecordsUrl"></s:url>
<div id="inventorylist">
    <ul>
       <li><a href="${depositRecordsUrl}"  style="color:#b1aa9e;">在线存款记录</a>|</li>
       <li><a href="${cashinRecordsUrl}" style="color:#b1aa9e;">手工存款记录</a>|</li>
       <li><a href="${withdrawRecordsUrl}" style="color:#b1aa9e;">提款记录</a>|</li>
       <li><a href="${transferRecordsUrl}" style="color:#b1aa9e;">户内转账记录</a>|</li>
       <li><a href="${consRecordsUrl}" style="color:#b1aa9e;">优惠活动记录</a>|</li>
       <li><a href="logininfo.asp" style="color:#b1aa9e;">上次登录信息</a>|</li>
        <li><a href="${couponRecordsUrl}" style="color:#b1aa9e;">优惠劵</a></li>
    </ul>
</div>
