<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
    String game=request.getParameter("game");
    game=  game == null ? "" : game;
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/title.jsp"></jsp:include>
    <script type="text/javascript" src="${ctx}/js/lib/jquery-1.11.2.min.js"></script>
</head>
<body>
<input id='j-hander' type="hidden" value='<%=session.getAttribute("nt_session") %>'>
<input id='j-game' type="hidden" value='<%=request.getAttribute("nt_game") %>'>
<input id='j-gameid' type="hidden" value='<%=game %>'>
<script>
    $(function () {
        var handleVal = $('#j-hander').val();
        var game = $('#j-gameid').val();
        if (handleVal == '' || handleVal == 'null' || handleVal == null || game == '' || game == null || game == 'null'){
        	alert("进入NT游戏失败，请您从NT页面进入吧");
            window.location.href='${ctx}/';
            return;
        }
        //key='92ad95acfc8e1cf0d6284d042af76d';
		//window.location.href="http://load.sdjdlc.com/nt/?game="+game+"&key="+handleVal+"&language=cn";


        window.location.href="http://load.sdjdlc.com/disk2/netent/?game=<%=game%>&key="+handleVal+"&language=cn";

    });
</script>

</body>
</html>