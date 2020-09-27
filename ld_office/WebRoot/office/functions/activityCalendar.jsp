<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@include file="/office/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>活动日历管理</title>

    <style type="text/css" media="all">
        @import url("/css/maven-base.css");
        @import url("/css/maven-theme.css");
        @import url("/css/site.css");
        @import url("/css/screen.css");
        @import url("/css/announcement.css");
    </style>
    <link rel="stylesheet" href="/css/print.css" type="text/css" media="print"/>
    <style type="text/css">
        table {
            border: 1px solid #666;
            width: 1000px;
            margin: 20px 0 20px 0 !important;
            font-size: 11px;
        }

        span.pagebanner {
            background-color: #eee;
            border: 1px dotted #999;
            padding: 2px 4px 2px 4px;
            width: 1000px;
            margin-top: 10px;
            display: block;
            border-bottom: none;
            text-align: right;
            font-size: 11px;
        }

        span.pagelinks {
            background-color: #eee;
            border: 1px dotted #999;
            padding: 2px 4px 2px 4px;
            width: 1000px;
            display: block;
            border-top: none;
            margin-bottom: -5px;
            text-align: right;
            font-size: 11px;
        }
    </style>
</head>
<body>

<div class="announcement_titleDiv">
    其他 --&gt; 活动日历管理
</div>
<div class="announcement_showMessageDiv">
    <h4>10分钟后自动更新到前端网页</h4>
    <input type="button" value="添加" onclick="add_view()" style="font-size:15px; width:80px; height:30px"
           theme="simple"/>
    <input type="button" value="查询" onclick="query()" style="font-size:15px; width:80px; height:30px" theme="simple"/>

    <display:table name="pageList" requestURI="/activityCalendar/query.do" id="fc"
                   decorator="dfh.displaytag.util.ActivityCalendarFormat">
        <display:column title="序号" style="width:30px;text-align:center">${fc_rowNum}</display:column>
        <display:column property="name" title="活动日历名称" style="width:150px;text-align:center"></display:column>
        <display:column property="activityDate" title="活动日历日期" style="width:150px;text-align:center"></display:column>
        <display:column property="creator" title="创建人" style="width:100px;text-align:center"></display:column>
        <display:column property="flag" title="是否禁用" style="width:60px;text-align:center"></display:column>
        <display:column property="content" title="活动日历内容"></display:column>
        <display:column property="orderBy" title="排序"></display:column>
        <display:column property="operator" title="操作" style="width:70px;text-align:center"></display:column>
    </display:table>
</div>


<script language="javascript" type="text/javascript">

    function query() {
        window.location = "/activityCalendar/query.do";
    }

    function add_view() {
        window.location = "/activityCalendar/add_view.do";
    }

    function editInfo(_aid) {
        window.location = "/activityCalendar/byid.do?id=" + _aid;
    }

    function switchFlag(_id, _flag) {
        if (window.confirm("确定要切换该条活动日历的状态吗？")) {
            window.location = "/activityCalendar/switchFlag.do?id=" + _id + "&flag=" + _flag;
        }
    }

</script>


</body>
</html>

