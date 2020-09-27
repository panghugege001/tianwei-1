<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/user.css?v=1128"/>
</head>
<body class="user_body">

<div class="index-bg about-bj"> 
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
	<div class="user_center"></div>
    <div class="container w_357">
        <jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
 		<div class="cfx about-main">
        	<div class="gb-sidenav">
                <jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>
        	</div>
        	<div class="gb-main-r tab-bd user-main leff_50">
        		<div class="ul_sidebar">
        			<ul>
        				<li class="active">
        					<a href="#tab-one" data-toggle="tab" aria-expanded="true">收件箱</a>
        				</li>
        				<li>
        					<a href="#tab-two" data-toggle="tab" aria-expanded="true">发件箱</a>
        				</li>        					        					
        			</ul>
        		</div>
                <div id="tab-one" class="tab-panel active letter-b">
                    <ul class="post-list" id="j-letterList"> </ul>
                </div>
                <div id="tab-two" class="tab-panel letter-b">
                    <div class="user-tab-box ui-form" style="margin:0">
                        <div class="ui-form-item">
                            <label class="ui-label">标题：</label>
                            <input type="text" name="guestbook.title" id="letter-title" autocomplete="off" class="ui-ipt" style="width: 400px;">
                        </div>
                        <div class="ui-form-item">
                            <label class="ui-label">内容：</label>
                            <textarea name="guestbook.content" id="letter-content" class="ui-ipt" style="width: 400px;height: 200px;"></textarea>
                        </div>
                        <div class="massage_tijiao">
                            <input type="button" class="btn" id="btn-submit" onclick="saveLetter();" value="确认发送">
                        </div>
                    </div>
                </div>
            </div>
        
        
        </div>
        
        
        

    </div>

</div>

<!--收件箱详情{-->
<div class="modal fade" id="modal-letter" role="dialog" data-backdrop="static" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">收件箱详情</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
            </div>
            <div class="modal-bd letter-cnt">
                <h1 class="j-tit tit"></h1>
                <div class="j-time time"></div>
                <div class="j-content article">加载中...</div>
            </div>
        </div>

    </div>
</div>
<!--}收件箱详情-->

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

<script src="/js/userLetter.js?v=10"></script>
<script>
    !function(){letterService(1);}();
</script>
</body>
</html>