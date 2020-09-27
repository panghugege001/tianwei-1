<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<base href="<%=request.getRequestURL()%>"/>
<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
<link rel="stylesheet" href="/css/index.css?v=0113">
<style>
	.sideNav-about{
    min-height: 500px;
    background: #eeeeee;		
	}
	.sideNav-about .tit-about{
	font-size: 24px;
    background-color: #2c8ba3;
    height: 64px;
    line-height: 64px;
    text-align: center;
    color: #fff;		
	}
.sideNav-about ul .active a{
    display: block;
    background-color: #e0ac62;
    color: #fff;	
}
.cont-about{
    float: right;
    width: 830px;
    background-color: #eeeeee;
    border: none;
    color: #967748;
    min-height: 500px;	
}
.cont-about .tit-item{
	font-size: 28px;
    height: 80px;
    line-height: 80px;
    padding-left: 40px;
    background: #2c8ba3;
    color: #fff;	
}	
.cont-about .midTex{
	color:#474747;
}
.about-main{
    margin-top: 30px;	
}
</style>
</head>

<body>
<div id="top-banner"> <a href="/topic/represent/home.jsp" target="_blank">
  <div class="container">
    <div class="close">&times;</div>
  </div>
  </a> </div>
<div class="index-bg">
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
  <div class="container cfx">
    <div class="about-main">
        <div class="sideNav-about">
          <div class="tit-about">天威</div>
          <ul>
            <li class="active"><a href="/activityCalendar.jsp" target="_self">网站活动日历<span class="icon-arrowR iconR"></span></a></li>
          </ul>
        </div>
        <div class="cont-about">
          <div class="item" style="display: block;">
            <div class="tit-item"><em>${actVo.name}</em></div>
            <div class="midTex">
              <p id="j-content" style='font-family:"Microsoft Yahei";'>${actVo.content}</p>
            </div>
          </div>
        </div> 
    </div>
  </div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
</body>
</html>
