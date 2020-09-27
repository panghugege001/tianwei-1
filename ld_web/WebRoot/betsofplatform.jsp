<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<s:url action="queryBetOfPlatform" namespace="/asp" var="betOfPlatformUrl"></s:url>
<s:url action="queryBetOfPlatformWeek" namespace="/asp" var="betOfPlatformWeekUrl"></s:url>

<jsp:include page="/tpl/checkUser.jsp"></jsp:include>

<%--<div class="dialog-cnt">
    <ul class="tab-hd">
        <li class="active"><a href="${betOfPlatformUrl}">本月投注额及累计存款额</a></li>
        <!-- <li><a href="${betOfPlatformWeekUrl}">本周投注额</a></li> -->
    </ul>
</div>--%>
<jsp:include page="/tpl/checkUser.jsp"></jsp:include>
<div class="data-list">
    <table class="table data-table">
        <thead>
        <tr>
            <th>编号</th>
            <th>游戏平台</th>
            <th>投注额</th>
        </tr>
        </thead>
        <tbody>
        <s:iterator var="fc" value="%{#request.bets}" status="st">
            <tr>
                <td><s:property value="#st.index+1"/></td>
                <td><s:property value="#fc.platform"/></td>
                <td><s:property
                        value="@dfh.utils.NumericUtil@formatDouble(#fc.bet)"/></td>
            </tr>
        </s:iterator>
        </tbody>

    </table>
    <div class="user_tijiao" style="margin-top: 20px;">
        <input type="button" class="btn" onclick="checkUpgrade('month')" value="检测升级"/>
    </div>

</div>
<%--<script src="/js/lib/jquery-1.11.2.min.js"></script>--%>
<script type="text/javascript">
    //处理升级
    function checkUpgrade(type) {
        //closeProgressBar();
        $.ajax({
            type    : "post",
            url     : "${ctx}/asp/checkUpgrade.aspx",
            cache   : false,
            data    : {"type": type},
            success : function (data) {
                alert(data);
            },
            error   : function () {
                alert("系统错误");
            },
            complete: function () {
                //closeProgressBar();
            }
        });
    }
</script>
