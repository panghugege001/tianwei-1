<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-5-13
  Time: 下午12:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery/jquery-ui-1.8.21.custom.min.js"></script>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
    <style type="text/css">
        body {
            margin: 0px;
        }

        .STYLE1 {
            color: #43860c;
            font-size: 12px;
        }
    </style>
</head>
<script type="text/javascript">
    $(document).ready(function () {
        var today = new Date();
        var weekday = new Array(7)
        weekday[1] = "星期一"
        weekday[2] = "星期二"
        weekday[3] = "星期三"
        weekday[4] = "星期四"
        weekday[5] = "星期五"
        weekday[6] = "星期六"
        weekday[0] = "星期日"
        var y = today.getFullYear() + "年";
        var month = (today.getMonth()+1) + "月";
        var td = today.getDate() + "日";
        var d = weekday[today.getDay()];
        var date=y+month+td+" "+d;
        $("#date").html(date);
    })
</script>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:fixed;">
    <tr>
        <td height="47" background="${ctx}/images/admin/main_09.gif">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="38" height="47" >&nbsp;</td>
                    <td width="59">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="29" ><span style="color:#2B5314;font-size: 14px; ">欢迎您</span> </td>
                            </tr>
                            <tr>
                                <td height="18" background="${ctx}/images/admin/main_14.gif">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0"
                                           style="table-layout:fixed;">
                                        <tr>
                                            <td style="width:1px;">&nbsp;</td>
                                            <td><span class="STYLE1">${session.customer.loginname}</span></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td width="155">&nbsp;</td>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="23" valign="bottom"></td>
                            </tr>
                        </table>
                    </td>
                    <td width="200">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="11%" height="23">&nbsp;</td>
                                <td width="89%" valign="bottom"><span class="STYLE1">日期： <span id="date"></span></span></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td height="5" style="line-height:5px; background-image:url(${ctx}/images/admin/main_18.gif)">
        </td>
    </tr>
</table>
</body>
</html>

