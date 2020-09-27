<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
    String language=request.getParameter("language");
    language=  language == null ? "" : language;

    String gameCode=request.getParameter("gameCode");
    gameCode=  gameCode == null ? "" : gameCode;
%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="/title.jsp"></jsp:include>
    <script type="text/javascript" src="${ctx}/js/lib/jquery-1.11.2.min.js"></script>
</head>
<body>


<input type="hidden" id="j-gameurl" value="${session.gameurl}">
<input type="hidden" id="j-slotKey" value="${session.slotKey}">
<input type="hidden" id="j-referWebsite" value="${session.referWebsite}">
<script>
    $(function () {
        var gameurl = $('#j-gameurl').val();
        var slotKey = $('#j-slotKey').val();
        var referWebsite = $('#j-referWebsite').val();
        var gameCode='<%=gameCode%>';
        var language='<%=language%>';
        if ( !slotKey || !referWebsite||!gameCode){
            alert("进入DT游戏失败，请您从DT页面进入吧");
            window.location.href='${ctx}/';
            return;
        }

        //http://play.dreamtech8.info/publishr/gamestart.php?slotKey=e578b06ab9fcfbd27ea40c1f36b9ff6a&language=zh_CN&gameCode=fantasyforest3x1&isfun=0&closeUrl=http://10.0.0.202
        //var a ="http://playgame.dreamtech8.info/dtGames.aspx/dtGames.aspx?slotKey="+slotKey+"&language=zh_CN&gameCode="+gameCode+"&isfun=0&clientType=0&closeUrl="+referWebsite;
                                         //gameurl+"/publishr/gamestart.php?slotKey="+slotKey+"&language=zh_CN&gameCode="+gameCode+"&isfun=0&closeUrl="+referWebsite;
        //console.log(a);
        var a =gameurl+"?slotKey="+slotKey+"&language=zh_CN&gameCode="+gameCode+"&isfun=0&closeUrl="+referWebsite;
        window.location.href=a;
    });
</script>

</body>
</html>