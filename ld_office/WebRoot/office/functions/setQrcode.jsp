<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>设置二维码记录</title>
    <link href="<c:url value='/css/error.css' />" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<c:url value='/scripts/someUserfulFunc.js' />"></script>
    <script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
    <s:if test="#request.errormsg != null">
        <script type="text/javascript">
            alert("<s:property value='#request.errormsg'/>");
        </script>
    </s:if>

</head>
<body>
<div id="excel_menu_left">
    操作 -->设置二维码记录 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
    <p align="left"><s:fielderror/><s:actionerror/></p>

    <s:form action="addQrcode" namespace="/office" method ="post" theme="simple">
        <table>
            <tr><td>代理名称:</td><td><s:textfield value="" name="agent" size="20" /></td></tr>
            <tr><td>推荐码:</td><td><s:textfield value="" name="recommendCode" size="10" /></td></tr>
            <tr><td>地址:</td><td><s:textfield value="" name="address" size="100" /></td></tr>
            <tr><td>微信号:</td><td><s:textfield value="" name="qrcode" size="100" /></td></tr>
            <tr><td>备注:</td><td><s:textfield value="" name="remark" size="100" /></td></tr>
            <tr><td><input type="submit" value="提交" />
            </td></tr>
        </table>
    </s:form>

</div>

</body>
</html>
