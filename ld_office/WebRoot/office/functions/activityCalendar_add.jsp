<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/office/include.jsp" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>添加活动日历</title>
    <style type="text/css" media="all">
        @import url("/css/announcement.css");
    </style>
    <s:head/>
</head>

<body>
<div class="announcement_titleDiv">
    其他 --> 添加活动日历
</div>
<div>
    <br/>
    <div align="center"><s:fielderror/></div>
    <s:form action="add" namespace="/activityCalendar" theme="simple">
        <table align="center" border="0" cellpadding="0" cellspacing="0" style="font-size:13px" width="100%">
            <tr align="left">
                <td width="100px">活动日历名称:</td>
                <td>
                    <s:textfield name="name" id="name" size="100" theme="simple"></s:textfield>
                </td>
            </tr>
            <tr align="left">
                <td width="100px">活动日历日期:</td>
                <td>
                    <s:textfield size="100" name="activityDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                                 cssClass="Wdate"></s:textfield>
                    <span style="color: red;">* 空代表长期有效</span>
                </td>
            </tr>
            <tr align="left">
                <td width="100px">是否禁用:</td>
                <td>
                    <select name="flag">
                        <option value="1" selected="selected">启用</option>
                        <option value="2">禁止</option>
                    </select>
                </td>
            </tr>
            <tr align="left">
                <td width="100px">排序:</td>
                <td>
                    <s:textfield name="orderBy" id="orderBy" size="10" theme="simple"></s:textfield>
                </td>
            </tr>
            <tr>
                <td width="100px">活动日历内容:</td>
                <td>
                    <s:textarea name="content" id="content" theme="simple" cols="60" rows="8"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td align="center">
                    <s:submit value="提交" style="font-size:15px; width:80px; height:30px" theme="simple"/>
                    <s:reset value="重新填写" style="font-size:15px; width:80px; height:30px" theme="simple"></s:reset>
                </td>
            </tr>
        </table>
    </s:form>
</div>
<script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
</body>
</html>
