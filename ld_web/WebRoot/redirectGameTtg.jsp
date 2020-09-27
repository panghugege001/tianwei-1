<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
    String gameid = request.getParameter("gameid");
    gameid = gameid == null ? "" : gameid;

    String gamecode = request.getParameter("gamecode");
    gamecode = gamecode == null ? "" : gamecode;

    String deviceType = request.getParameter("deviceType");
    deviceType = deviceType == null ? "" : deviceType;
%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
</head>
<body>
<input id='j-hander' type="hidden" value='<%=session.getAttribute("TTplayerhandle") %>'>
<input id='j-game' type="hidden" value='<%=request.getParameter("gameName") %>'>
<input id='j-id' type="hidden" value='<%=request.getParameter("gameId") %>'>
<input id='j-type' type="hidden" value='<%=request.getParameter("ttg_gameType") %>'>
<script>
    $(function () {
        var handleVal = $('#j-hander').val();
        var gameName = $('#j-game').val();
        var gameId = $('#j-id').val();
        var gameType = $('#j-type').val()==='null'? 0: $('#j-type').val();
        if (!handleVal || handleVal==='null' ){
            alert("进入TTG游戏失败，请您从TTG页面进入吧");
            window.location.href='${ctx}/';
            //return;
        }
//        window.location.href="https://ams-games.stg.ttms.co/casino/generic/game/game.html?playerHandle="+handleVal+"&account=CNY&gameName="+gameName+"&gameType="+gameType+"&gameId="+gameId+"&lang=zh-cn&t="+new Date();
        if ('<%=deviceType%>' == 'mobile') {
            window.location.href = "https://ams5-games.ttms.co/casino/default/game/casino5.html?playerHandle=" + handleVal + "&account=CNY&gameName=" + gameName + "&gameId=" + gameId + "&deviceType=mobile&gameType=0&lang=zh-cn&t=" + new Date();
        } else {
            window.location.href = "https://ams5-games.ttms.co/casino/default/game/game.html?playerHandle=" + handleVal + "&account=CNY&gameName=" + gameName + "&gameType=" + gameType + "&gameId=" + gameId + "&lang=zh-cn&t=" + new Date();
        }
    });
</script>

</body>
</html>