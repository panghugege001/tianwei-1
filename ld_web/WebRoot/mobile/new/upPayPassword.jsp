<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="dfh.model.Users" %>
<%@ page import="dfh.utils.Constants" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
    Users user = (Users) session.getAttribute(Constants.SESSION_CUSTOMERID);
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/mobile/new/index.jsp");
    }
%>
<!DOCTYPE html>
<html>

<head>
    <title>天威</title>
    <jsp:include page="/mobile/commons/header.jsp">
        <jsp:param name="Title" value="修改密码"/>
    </jsp:include>
    <link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css"/>
</head>

<body>
<div id="page-cashpassword" class="form-warp no-icon">
    <input type="text" id="noPayPassword" hidden value="${session.payPassword == null}">
    <div id="passwordPayform" class="ui-form">
        <c:if test="${session.payPassword == null}">
            <div class="form-tips">登录密码：</div>
            <div class="form-group">

                <input type="password" class="form-control" name="password" id="loginPassWord" placeholder="请输入登录密码"
                       autocomplete="off" required/>
            </div>
            <div class="form-tips">取款密码：</div>
            <div class="form-group">
                <input type="password" class="form-control" id="newPayPassWord" autocomplete="off" name="new_content"
                       placeholder="请输入取款密码" required/>
            </div>
        </c:if>
        <c:if test="${session.payPassword != null}">
            <div class="form-tips">提款密码：</div>
            <div class="form-group">

                <input type="password" class="form-control" name="content" id="oldPayPassWord" placeholder="请输入当前取款密码"
                       autocomplete="off" required/>
            </div>
            <div class="form-tips">新取款密码：</div>
            <div class="form-group">

                <input type="password" class="form-control" id="newPayPassWord2" autocomplete="off" name="new_content"
                       placeholder="请输入新取款密码" required/>
            </div>
        </c:if>
        <div class="ui-button-row">
            <input class="btn-submit block" type="button" value="提交" onclick="updateDatePayPassword()">
        </div>
    </div>
    <div class="text-tips">
        <div class="h3">
            <strong>温馨提示：</strong>
        </div>
        <p>1.取款密码为6位纯数字。例如：123456。</p>
        <p>2.已经设置取款密码，需要修改，请输入原始的取款密码。</p>
        <p>3.若忘记取款密码，请联系在线客服核实身份后重置取款密码。</p>
        <p>4.重置提款密码后，在原取款密码中任意输入6位数字，然后输入新取款密码即可完成绑定。</p>
    </div>
</div>
<jsp:include page="/mobile/commons/menu.jsp"/>
<script>
    //新版修改支付密码
    function updateDatePayPassword() {
        var newPayPassWord = $("#newPayPassWord").val() ? $("#newPayPassWord").val() : $("#newPayPassWord2").val(),
            data = {};
        if ($("#loginPassWord").val()) {
            data.password = $("#loginPassWord").val();
        } else {
            data.content = $("#oldPayPassWord").val()
        }
        data.new_content = newPayPassWord;
        mobileManage.getLoader().open("修改中");
        $.post("/asp/change_pwsPayAjax.aspx", data, function (returnedData, status) {
            if ("success" == status) {
                mobileManage.getLoader().close();
                alert(returnedData);
                data = null;
            }
        });
    }
</script>
</body>

</html>