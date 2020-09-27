<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<!-- <script type="text/javascript" src="/js/prototype_1.6.js"></script> -->
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
        src="<c:url value='/scripts/My97DatePicker/WdatePicker.js' />"></script>


<script type="text/javascript">


    function update(){



        if(confirm("确定？")){
            var data = $("#mainform").serialize();
            var action = "/office/updateQrcodeConfig.do";
            $.ajax({
                url:action,
                type:"POST",
                data:data,
                contentType:"application/x-www-form-urlencoded; charset=utf-8",
                success: function(resp){
                    alert(resp);
                }
            });
        }
    }

</script>

<div id="excel_menu">
    <s:form name="mainform" id="mainform" theme="simple">
        <input type="hidden" name="id" value="${weiXcode.id}">

        <table >
            <tr class="cunsong"><td>代理名称:</td><td><input type="text" name="agent" value="${weiXcode.agent}"  /></td></tr>
            <tr></tr>
            <tr class="cunsong"><td>推荐码:</td><td><input type="text" name="recommendCode"  value="${weiXcode.recommendCode}" /></td></tr>
            <tr></tr>
            <tr class="cunsong"><td>地址:</td><td><input type="text" name="address"  value="${weiXcode.address}" /></td></tr>
            <tr></tr>

            <tr class="cunsong"><td>微信号:</td><td><input type="text" name="qrcode"  value="${weiXcode.qrcode}" /></td></tr>
            <tr></tr>
            <tr class="cunsong"><td>备注:</td><td><input type="text" name="remark"  value="${weiXcode.remark}" /></td></tr>
        </table>
    </s:form>
    <input type="button" value="更新" onclick="update();"/>

</div>