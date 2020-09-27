<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/office/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>交连克支付记录</title>
        <link href="<c:url value='${ctx}/css/excel.css' />" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="/js/prototype_1.6.js"></script>
        <script type="text/javascript" src="/js/jquery-1.2.6.pack.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>
        <script type="text/javascript">

        function gopage(val) {

            document.mainform.pageIndex.value = val;
            document.mainform.submit();
        }

        function orderby(by) {

            if (document.mainform.order.value == "desc") {

                document.mainform.order.value = "asc";
            }

            else {

                document.mainform.order.value = "desc";
            }

            document.mainform.by.value = by;
            document.mainform.submit();
        }

        function orderAction(btn, billno) {

            $("#orderBtn").attr("disabled", true);

            var xmlhttp = new Ajax.Request("/office/queryJLKOrder.do", {method: 'post', parameters: "billno=" + billno, onComplete: responseMethod1});
        }

        function responseMethod1(data) {

            var da = data.responseJSON;

            if (null == da) {

                $("#orderBtn").removeAttr("disabled");

                alert("查询订单信息失败！");
                return;
            }

            if (da.status == 0) {

                $("#orderBtn").removeAttr("disabled");

                alert("该笔订单在第三方后台是待支付状态，充值金额为：" + da.coin_total);
                return;
            } else {

                if (window.confirm('该笔订单在第三方后台是已支付状态，确定要自动补单吗？')) {

                    var out_trade_no = da.out_trade_no;
                    var coin_total = da.coin_total;
                    var status = da.status;
                    var trade_no = da.trade_no;
                    var coin_type = da.coin_type;

                    var params = "out_trade_no=" + out_trade_no + "&coin_total=" + coin_total + "&status=" + status + "&trade_no=" + trade_no + "&coin_type=" + coin_type + "&r=" + Math.random();

                    var xmlhttp = new Ajax.Request("/office/remedyJLKOrder.do", {method: 'post', parameters: params, onComplete: responseMethod2});
                } else {

                    $("#orderBtn").removeAttr("disabled");
                }
            }
        }

        function responseMethod2(data) {

            $("#orderBtn").removeAttr("disabled");

            var da = JSON.parse(data.responseJSON);

            if ("1" == da.return_code && "success" == da.return_msg) {

                alert("补发成功！");
            } else {

                alert("补发失败！");
            }
        }
        </script>
    </head>
    <body>
        <p>记录&nbsp;--&gt;&nbsp;交连克支付记录&nbsp;--&gt;&nbsp;<a href="javascript:history.back();"><font color="red">上一步</font></a></p>
        <div id="excel_menu" style="position: absolute; top: 35px; left: 0px;">
            <s:form action="queryJLKList" namespace="/office" name="mainform" id="mainform" theme="simple">
                <table border="0" cellspacing="0" cellpadding="0" bgcolor="#99c8d7">
                    <tr>
                        <td>
                            <table border="0" cellpadding="0" cellspacing="0">
                                <tr align="left">
                                    <td>订单状态：<s:select list="#{'':'','1':'待处理','-1':'已取消','0':'成功','2':'未支付'}" name="payOrderFlag" id="payOrderFlag" cssStyle="width: 110px;"></s:select></td>
                                    <td>开始时间:<s:textfield name="startTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" value="%{startTime}"/></td>
                                    <td>结束时间:<s:textfield name="endTime" size="18" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" cssClass="Wdate" value="%{endTime}"/></td>
                                    <td>支付单号:<s:textfield name="billno" size="30"/></td>
                                    <td>会员帐号:<s:textfield name="loginname" size="15"/></td>
                                    <td>每页记录：<s:select cssStyle="width: 90px" list="%{#application.PageSizes}" name="size"></s:select></td>
                                    <td><s:submit cssStyle="width:60px; height:60px;" value="查询"></s:submit></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <s:set name="by" value="'createTime'"/>
                <s:set name="order" value="'desc'"/>
                <s:hidden name="order" value="%{order}"/>
                <s:hidden name="by" value="%{by}"/>
                <s:hidden name="pageIndex"/>
            </s:form>
        </div>
        <br/>
        <div style="position: absolute; top: 140px; left: 0px">
            <div id="right">
                <div id="right_01">
                    <div id="right_001">
                        <div id="right_02">
                            <div id="right_03"></div>
                        </div>
                        <div id="right_04">
                            <table width="1180px" border="0" cellpadding="0" cellspacing="0" bgcolor="#99c8d7">
                                <tr bgcolor="#0084ff">
                                    <td>支付单号</td>
                                    <td>支付平台</td>
                                    <td>状态</td>
                                    <td>支付类型</td>
                                    <td>会员帐号</td>
                                    <td>会员姓名</td>
                                    <td>金额</td>
                                    <td>来源IP</td>
                                    <td>加入时间</td>
                                    <td>支付时间</td>
                                    <td>说明</td>
                                    <td>操作</td>
                                </tr>
                                <c:set var="amountSum" value="0" scope="request"></c:set>
                                <s:iterator var="fc" value="%{#request.page.pageContents}">
                                <tr>
                                    <td><s:property value="#fc.billno"/></td>
                                    <td><s:property value="#fc.payPlatform"/></td>
                                    <td><s:property value="@dfh.model.enums.PayType@getText(#fc.type)"/></td>
                                    <td>充值</td>
                                    <td><s:property value="#fc.loginname"/></td>
                                    <td><s:property value="#fc.aliasName"/></td>
                                    <td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.money)"/></td>
                                    <td><s:property value="#fc.ip"/></td>
                                    <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
                                    <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.returnTime"/></td>
                                    <td><s:property value="#fc.msg"/></td>
                                    <td>
                                        <s:if test="#fc.type!=0">
                                            <c:if test="${sessionScope.operator.authority eq 'finance' or sessionScope.operator.authority eq 'finance_leader' or sessionScope.operator.authority eq 'finance_manager' or sessionScope.operator.authority eq 'boss'}">
                                                <input type="button" value="订单状态" id="orderBtn" onclick="orderAction(this,'${fc.billno}');"/>
                                            </c:if>
                                        </s:if>
                                    </td>
                                </tr>
                                <s:set var="amountValue" value="#fc.money" scope="request"></s:set>
                                <c:set var="amountSum" value="${amountSum+amountValue}" scope="request"></c:set>
                                </s:iterator>
                                <s:if test="#session.operator.authority=='boss' || #session.operator.authority=='finance_leader' ||#session.operator.authority=='finance' || #session.operator.authority=='finance_manager' || #session.operator.authority=='sale_manager' || #session.operator.authority=='market_manager'">
                                <tr>
                                    <td bgcolor="#e4f2ff" align="right" colspan="6">当页小计:</td>
                                    <td bgcolor="#e4f2ff" align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.amountSum)"/></td>
                                    <td bgcolor="#e4f2ff" align="center" colspan="5"></td>
                                </tr>
                                <tr>
                                    <td bgcolor="#e4f2ff" align="right" colspan="6">总计:</td>
                                    <td bgcolor="#e4f2ff" align="right"><s:property value="@dfh.utils.NumericUtil@double2String(#request.page.statics1)"/></td>
                                    <td bgcolor="#e4f2ff" align="center" colspan="5"></td>
                                </tr>
                                </s:if>
                                <tr>
                                    <td colspan="12" align="right" bgcolor="66b5ff" align="center">${page.jsPageCode}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
