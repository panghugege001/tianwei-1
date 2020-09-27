<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
  String infoValue=(String)request.getSession(true).getValue("infoValue4Live800");
  if(infoValue==null)infoValue="";
%>
<%--<script src='//kefu.easemob.com/webim/easemob.js?tenantId=36253&hide=false&sat=false' async='async'></script>--%>
 <style>
  #APPxiazai h2{
    display: none;
  }
  #APPxiazai .mb10{
    display: none;
  }
  #cvd-open{display: none;}
 </style>
<div class="gb-footer">
  <div class="user_game">
    <div class="container">
      <ul class="footer-nav">
         
        <li><a href="/aboutus.jsp#tab-aboutus">关于天威</a></li>
        <li>|</li>
        <li><a href="/aboutus.jsp#tab-agreement">用户协议</a></li>
        <li>|</li>
        <li><a href="/aboutus.jsp#tab-responsibility">博彩责任</a></li>
        <li>|</li>
        <li><a href="/aboutus.jsp#tab-disclaimer">免责条款</a></li>
        <li>|</li>
        <li><a href="/aboutus.jsp#tab-contact">联系我们</a></li>
        <li>|</li>
        <li><a href="/aboutus.jsp#tab-privacy">用户隐私</a></li>
        <!--<li>|</li>
        <li><a href="javascript:void(0);" data-toggle="modal" data-target="#modal-map">天威地图</a></li>
        <li>|</li>
        <li><a href="javascript:alert('敬请期待');">新手指南</a></li>-->
      </ul>
    </div>
  </div>
  <div class="footer-sub text-center">Copyright © 2016-2018 天威 版权所有 侵权必究。</div>
</div>
 
<!--登录{-->
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-logo"></div>
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title text-center">用户登录</h4>
      </div>
      <div class="modal-bd">
        <form action="" method="post" class="ui-form">
          <div style="width: 100%; height: 50px;" class="linediv"></div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">用户名：</label>
            <input type="text" name="address" class="ui-ipt"  id="j-name" maxlength="16" placeholder="用户名"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">密码：</label>
            <input type="password" class="ui-ipt" name="addressee" id="j-pwd" placeholder="请输入密码"/>
          </div>
          <div class="ui-form-item">
            <input type="button" class="btn btn-danger btn-block" id="j-login" value="登 录" onclick="Page.login(this,'modal');"/>
          </div>
          <div class="ui-form-item"> <a data-toggle="modal" data-target="#modal-forget" href="javascript:void(0);" class="forget">忘记密码</a> </div>
        </form>
      </div>
      <div class="modal-ft">登录时遇到任何问题，请及时联系在线客服获取帮助。 <a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank"  class="c-218 link">24小时在线客服</a></div>
    </div>
  </div>
</div>
 <input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad" />
<div id="loginBtn" class="spanBtnStyle" data-toggle="modal" data-target="#modal-login" style="display: none;">点击登录</div>
<!--}登录-->
<script>
    window.easemobim = window.easemobim || {};
    easemobim.config = {
        //是否隐藏小的悬浮按钮
        hide: true,
        //自动连接
        autoConnect: true
    };
</script>
<script>
  // 判断是否登录
function isLogin() {
    var loginname = $('#checkUserIsLoad').val()
    if (!loginname) {
        return false;
    }
    return true
}
</script>

 