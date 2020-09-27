<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/user.css?v=1122"/>
</head>
<body>

<div class="index-bg">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>

    <div class="container">
        <jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>

        <ul class="user-nav tab-nav">
            <li class="active">
                <a href="#tab-one" data-toggle="tab"><i class="iconfont icon-email"></i>历史记录</a>
            </li>
            <li><a href="#tab-two" data-toggle="tab"><i class="iconfont icon-write"></i>投诉建议</a></li>
        </ul>
        <div class="user-main tab-bd">
            <div id="tab-one" class="tab-panel active">
                <ul class="post-list" id="j-suggestionList"> </ul>
            </div>
            <div id="tab-two" class="tab-panel">
                <div class="user-tab-box ui-form">
                    <div class="ui-form-item">
                        <label class="ui-label">您的问题：</label>
                        <select id="suggestion-type" name="suggestion-type" title="问题" class="ui-ipt">
                            <option selected="selected" value="">请选择</option>
                            <option value="咨询存款和提款">咨询存款和提款</option>
                            <option value="咨询账户问题">咨询账户问题</option>
                            <option value="咨询红利及优惠">咨询红利及优惠</option>
                            <option value="咨询网站登录异常">咨询网站登录异常</option>
                            <option value="咨询代理业务">咨询代理业务</option>
                            <option value="咨询开户">咨询开户</option>
                            <option value="投诉和建议">投诉和建议</option>
                        </select>
                    </div>
                    <div class="ui-form-item">
                        <label class="ui-label">问题具体描述：</label>
                        <textarea id="suggestion-content" name="suggestion-content" class="ui-ipt"  rich="0" style="width: 400px; height: 200px" title="描述"></textarea>

                    </div>
                    <div class="ui-form-item">
                        <button class="btn btn-danger" id="btn-submit" onclick="saveSuggestion();">创建</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>

<!--收件箱详情{-->
<div class="modal fade" id="modal-suggestion" role="dialog" data-backdrop="static" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-hd">
                <h2 class="modal-title">投诉建议详情</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
            </div>
            <div class="modal-bd letter-cnt">
                <%-- <h1 class="j-tit tit"></h1>
                 <div class="j-time time"></div>
                 <div class="j-content article"></div>--%>
                <div class="j-content content">加载中...</div>
            </div>
        </div>

    </div>
</div>
<!--}收件箱详情-->

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

<script src="/js/userLetter.js"></script>
<script>
    !function(){suggestionService(1);}();
</script>
</body>
</html>