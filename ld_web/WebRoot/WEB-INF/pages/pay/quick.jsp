<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/title.jsp"></jsp:include>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <title>龙都国际娱乐城--手机投注</title>

</head>
<body>
<div class="pay-page">
    <jsp:include page="/tpl/header.jsp"></jsp:include>

    <div class="pay-online-wp">
        <jsp:include page="${ctx}/tpl/newsIndex.jsp"></jsp:include>
        <form id="hcRedirect" action="${ctx}/asp/pay_api.aspx" method="post">
            <input type="hidden" name="platformId" id="platformId" value="${platformId}"/>
            <input type="hidden" name="loginName" id="loginName" value="${session.customer.loginname}"/>
            <input type="hidden" name="payUrl" id="payUrl" value="${payUrl}"/>
            <input type="hidden" name="usetype" value="2"/>

            <table class="account-info table-pay">
                <tr>
                    <th>账户名:</th>
                    <td>${session.customer.loginname}</td>
                </tr>
                <tr>
                    <th>账户余额：</th>
                    <td class="c-red">${session.customer.credit}元</td>
                </tr>
                <tr>
                    <th> 存款额度：</th>
                    <td>
                        <div class="ipt-group">
                            <input name="orderAmount" id="amount" type="text" maxlength="10" class="ipt-txt" />
                            <span class="c-red">* 必填</span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>&nbsp;</th>
                    <td>
                        <div class="ipt-group">
                            <input name="Submit" type="button" class="btn btn-danger" onclick="return btnOK_hc_onclick();" value="确定支付" />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">温馨提示：为了避免掉单情况的发生，请您在支付完成后，需等“支付成功”页面跳转出来，点<input name="Submit22" type="button" class="pay-tip warning" value="商城取货" />或<input name="Submit2" type="button" class="pay-tip danger" value="通知客户" />，再关闭页面,以免掉单！感谢配合！！！</td>
                </tr>
            </table>
        </form>
    </div>

</div><jsp:include page="/tpl/footer.jsp"></jsp:include>
<script type="text/javascript">
    function btnOK_hc_onclick(){
        var amount = $("#amount").val();
        var loginName = $("#loginName").val();

        if(isNaN(amount)){
            alert("[提示]存款额度非有效数字！");
            return false;
        }
        if(amount<1){
            alert("[提示]1元以上或者1元才能存款！");
            return false;
        }

        if(amount>5000){
            alert("[提示]存款金额不能超过5000！");
            return false;
        }

        if(loginName==null ||loginName==""){
            alert("[提示]系统检测你已经掉线,请重新登录！");
            return false;
        }
        $("#hcRedirect").submit();
        return true;
    }
</script>
</body>
</html>