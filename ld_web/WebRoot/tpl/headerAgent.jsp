<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<jsp:include page="${ctx}/tpl/headerCommon.jsp"></jsp:include>

<div id="header" class="gb-header">
    <!--顶部{-->
    <div class="header-top-wp">
        <div class="container">
            <div class="link-info fl">
                <a data-toggle="modal" data-target="#modal-license" href="javascript:void(0);" class="item">牌照展示</a>
                <a href="/sitemap.jsp" target="_blank" class="item">天威导航</a>
                <a href="javascript:alert('敬请期待')" class="item">天威公会</a>
            </div>
            
            <c:if test="${session.customer!=null && session.customer.role ne 'MONEY_CUSTOMER'}">
                <ul class="fr link-info">
                    <li class="item">欢迎回来:<span class="c-strong">${customer.loginname }</span></li>
                    <li class="item">等级: <c:if test="${session.AGENTVIP ==null || session.AGENTVIP eq '0'}">
                        <span class="c-strong">代理</span>
                    </c:if>
                        <c:if test="${session.AGENTVIP!=null && session.AGENTVIP eq '1'}">
                            <span class="c-strong">VIP代理</span>
                        </c:if></li>
                    <li class="item"><a class="balance-info" href="javascript:;">老虎机账户结余:<span
                            class="c-strong">${session.slotAccount}</span><i class="iconfont icon-refresh"></i> </a></li>
                    <li class="item"><a class="balance-info" href="javascript:;">其它账户结余:<span
                            class="c-strong">${customer.credit}</span><i class="iconfont icon-refresh"></i> </a></li>
                    <li class="item"><a href="/manageLetter.jsp">站内信:<span class="j-letter c-strong">0</span></a>
                    </li>
                    <s:url action="agentManage" namespace="/" var="agentManage"></s:url>
                    <li class="item"><a href="${agentManage}">账户管理</a></li>
                    <li class="item"><a href="javascript:void(0);" onclick="return logout();">退出</a></li>
                </ul>
            </c:if>

        </div>
    </div>
    <!--}顶部-->
    <!--菜单{-->
	<div class="nav-wp">
		<div class="container">
			<ul id="j-topmenu" class="gb-nav">
				<li class="active"><a href="/" target="_blank">官网首页</a></li>
				<li><a href="javascript:scroller('tab-brand', 800);" >品牌介绍</a></li>
				<li><a href="javascript:scroller('tab-plan', 800);" >佣金计划</a></li>
				<li class="center text-center"><img src="${ctx}/images/logo.png" class="logo"></li> 
				<li><a href="javascript:scroller('tab-dream', 800);">带你圆梦</a></li>
				<li><a class="last" href="javascript:scroller('tab-contact', 800);"  >联系我们</li>
				<li><a data-toggle="modal" data-target="#j-modal-agentreg" href="javascript:;">加盟注册</a></li> 
			</ul>
		</div>
	</div>
    <!--}菜单-->
</div>

