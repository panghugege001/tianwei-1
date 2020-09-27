<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="dfh.service.interfaces.AnnouncementService"%>
<%@page import="dfh.model.Announcement"%>
<%@include file="/office/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>公告管理</title>

<style type="text/css" media="all">
   @import url("/css/maven-base.css"); 
   @import url("/css/maven-theme.css"); 
   @import url("/css/site.css"); 
   @import url("/css/screen.css");
   @import url("/css/announcement.css");
</style>
<link rel="stylesheet" href="/css/print.css" type="text/css" media="print" />
<style type="text/css" >
table {
	border: 1px solid #666;
	width: 1000px;
	margin: 20px 0 20px 0 !important;
	font-size:11px;
}

span.pagebanner {
	background-color: #eee;
	border: 1px dotted #999;
	padding: 2px 4px 2px 4px;
	width: 1000px;
	margin-top: 10px;
	display: block;
	border-bottom: none;
	text-align:right;
	font-size:11px;
}
span.pagelinks {
	background-color: #eee;
	border: 1px dotted #999;
	padding: 2px 4px 2px 4px;
	width: 1000px;
	display: block;
	border-top: none;
	margin-bottom: -5px;
	text-align:right;
	font-size:11px;
}
</style>
</head>
<body>

<div class="announcement_titleDiv">
其他 --&gt; 公告管理 
</div>

<div class="announcement_showMessageDiv">

	<display:table name="pageList" requestURI="/announcement/query.do" id="fc"  decorator="dfh.displaytag.util.NewsFormat">
   
    	<display:column title="序号" style="width:30px;text-align:center">${fc_rowNum}</display:column>
    	<display:column property="type" title="公告类型" style="width:100px;text-align:center"></display:column>
    	<display:column property="createtime" title="公告时间" style="width:150px;text-align:center" ></display:column>
    	<display:column property="title" title="公告标题"></display:column>
    	<display:column title="操作" style="width:70px;text-align:center">
    		<a href="JavaScript:delInfo('${fc.id}')">删除</a>|
    		<a href="JavaScript:editInfo('${fc.id}')">编辑</a>
    	</display:column>
    </display:table>

</div>



<script language="javascript" type="text/javascript">

function editInfo(_aid){
	window.location="/announcement/byid.do?id="+_aid;

}
function delInfo(_aid){
	if(window.confirm("确定要删除该条公告吗？")){
		window.location="/announcement/del.do?id="+_aid;
	}
}

</script>



</body>
</html>

