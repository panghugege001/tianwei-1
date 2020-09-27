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
  <div class="footer-hd">
	  <div class="container" style="padding: 0;">
	    <div class="footer-bd">
	      <div class="partner-link" style="height: 70px; background: url(/images/footer/LONGdu.png?v=2) center no-repeat;">
	         
	      </div>
	    </div>
	  </div>
  </div>
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
  <div class="footer-sub text-center"></div>
</div>
 

			<div id="asidebar">
				<div class="item">
					<a href="https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19" target="_blank"  class="chat-service"> <i class="iconfont"><span class="icon_img1"></span></i>
						<div class="txt chat-service">在线客服</div>
					</a>
				</div>
        <!-- <div class="item">
          <a href="javascript:;" id="ck_weixin"> <i class="iconfont"><span class="icon_img4"></span></i>
            <div class="weixin-kefu-pc" id="weixin-kefu" onclick="return false">
              <h2>微信客服</h2>
              <div class="mb10"> <img src="/images/appxiazai/weixin.jpg" width="110" height="110" alt=""> </div>
            </div>
          </a>
        </div> -->
				<!--<div class="item"><a href="http://b.qq.com/webc.htm?new=0&sid=800157062&o=www.tianwei4.com/&q=7" target="_blank"> <i class="iconfont icon-qq"></i>
    <div class="txt">QQ客服</div>
    </a></div>-->
				<!-- <div class="item">
					<a href="javascript:;" data-toggle="modal" data-target="#j-modal-tel"> <i class="iconfont"><span class="icon_img2"></span></i>
						<div class="txt">电话回拨</div>
					</a>
				</div> -->
				<div class="item">
					<a href="javascript:;"> <i class="iconfont"><span class="icon_img3"></span></i>
						<div class="txt">tianwei661@gmail.com</div>
					</a>
				</div>
				 
<!-- 				<div class="item qr">
					<a href="javascript:;" id="ck_app"> <i class="iconfont"><span class="icon_img5"></span></i>
						<div class="" id="APPxiazai" onclick="return false">
							<h2>APP下载</h2>
							<div class="mb10"> <img src="/images/appxiazai/longduapp.png" width="110" height="110" alt=""> </div>
						</div>
					</a>
				</div> -->
				<div class="item" style="position: relative;z-index: 10;">
					<i class="iconfont iconfont_no2" id="iconfont_no2"><span class="icon_img6"></span>
				</div>
			</div>


 
<!--电话回拨{-->
<div class="modal fade" id="j-modal-tel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">免费热线拨打</h4>
      </div>
      <div class="modal-bd ui-form">
        <div class="ui-form-item">
          <label for="" class="ui-label">联系电话:</label>
          <input type="text" id="phonenum" data-rule-digits class="ui-ipt" placeholder="请输入您的联系电话"/>
        </div>
        <div class="ui-form-item">
          <input type="button" id="j-reback-call" class="btn btn-danger" value="号码回拨"/>
        </div>
        <p class="pt20 c-red" style="text-align: center; padding-bottom: 20px;">若注册电话停止使用，请输入您最新有效电话进行拨打热线，谢谢！</p>
      </div>
    </div>
  </div>
</div>
<!--}电话回拨-->

<!--在线客服{-->
<div class="modal fade" id="j-server" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      <div>
        <iframe data-src="/live800.jsp" width="600" height="400" frameborder="0"></iframe>
      </div>
    </div>
  </div>
</div>
<!--}在线客服-->

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
        	<div style="width: 100%; height: 50px;"></div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">用户名：</label>
            <input type="text" name="address" class="ui-ipt"  id="j-name" maxlength="16" placeholder="用户名"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">密码：</label>
            <input type="password" class="ui-ipt" name="addressee" id="j-pwd" placeholder="请输入密码"/>
          </div>
          <%--<div class="ui-form-item">
            <label for="" class="ui-label rq-value">验证码：</label>
            <input type="text" class="ui-ipt" name="cellphoneNo" style="width:118px;"  id="j-code" maxlength="8" placeholder="验证码"/>
            <img width="78" height="34" id="j-codeimg" data-src="/asp/validateCodeForIndex.aspx" onclick="this.src='/asp/validateCodeForIndex.aspx?'+Math.random();" alt="">
          </div>--%>
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
<!--注册弹框{-->
<div class="modal fade in" id="modal-reg" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog lg" role="document">
    <div class="modal-content" style=" height: 532px;">
      <div class="modal-logo"></div>
      <div class="modal-hd">
        <h2 class="modal-title text-center">轻松注册</h2>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">×</span></button>
      </div>
      <div class="modal-bd mt20" style="padding:0">
        <iframe data-src="/register.jsp" width="100%" height="500" scrolling="no" frameborder="0" src=""></iframe>
      </div>
    </div>
  </div>
</div>
<!--}注册弹框-->

<!--注册成功弹框{-->
<div class="modal fade" id="modal-success" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document" >
    <div class="logo"></div>
    <div class="modal-content">
      <img src="/images/register/reg-ren.png" alt="" class="tiger-ren">
      <div class="modal-hd">
        <img src="/images/register/success-icon.png" alt="" class="tiger-ico">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" id="r_close"><span aria-hidden="true">×</span></button>
      </div>
      <div class="modal-bd" style="padding: 38px 0 0 0">
        <h3><img src="/images/register/success.png" alt="" class="reg-suc"></h3>
        <div class="modal-info cfx">
          <dl class="text-box">

            <dd> <strong class="tips">1、天威8～88体验金：</strong> <span>下载天威app→自助优惠→自助最高88体验金</span></dd>
            <dd> <strong class="tips">2、每日任务彩金</strong> <span>完成每日任务领取大量红包、彩金。</span><span>请至帐户管理→自助优惠→每日任务</span></dd>
            <dd> <strong class="tips">3、每日高额限时存送</strong> <span>关注天威官网公告、站内信、下载app，可即时获得彩金资讯。</span> </dd>

          </dl>
          <div class="btn-box"><a href="${ctx}/asp/payPage.aspx" target="_blank"><img src="/images/register/btn1.jpg" alt="" class="btn-suss"></a> <a href="/manageuser.jsp"  target="_blank"> <img src="/images/register/btn2.jpg"></a></div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--}注册成功弹框-->
<!--忘记密码-->

<div class="modal fade in" id="modal-forget" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog for" role="document">
    <div class="modal-content" style=" height:330px; border:none;">
      <div class="modal-hd">
        <h2 class="modal-title text-center">找回密码</h2>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">×</span></button>
      </div>
      <div class="modal-bd mt20" style="padding:0">
        <iframe data-src="/forgotPassword.jsp" width="100%" height="355" scrolling="no" frameborder="0" src=""  id="forgetiframe"></iframe>
      </div>
    </div>
  </div>
</div>

<!--忘记密码-->
<!--牌照-->
<div class="modal fade in" id="modal-license" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content" style="height:588px; border:2px solid #342923;">
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        <h4 class="modal-title text-center">牌照展示</h4>
      </div>
      <div class="modal-bd" style="padding:0">
        <iframe data-src="/license.jsp" width="100%" height="533" scrolling="no" frameborder="0" src=""></iframe>
      </div>
    </div>
  </div>
</div>
<!--牌照-->
<!--网站地图-->
<div class="modal fade in" id="modal-map" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document" style=" width:810px; margin: 140px auto;">
    <div class="modal-content" style="height:738px; border:2px solid #342923;">
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
        <h4 class="modal-title text-center">天威地图</h4>
      </div>
      <div class="modal-bd" style="padding:0">
        <iframe data-src="/map.jsp" width="100%" height="680" scrolling="no" frameborder="0" src=""></iframe>
      </div>
    </div>
  </div>
</div>


<!--未读信息弹窗-->
<input type="text" value="${session.customer==null}" hidden="hidden" id="massage" />
<div class="massaage_box">
	<h3>温馨提示</h3>
	<span class="close_gb massage_close">X</span>
	<div class="massaage_div">
		<p>您有<span class="j-letter c-strong"></span>封未读邮件请注意查收</p>
		<div>
			<a class="clove1" href="/userManage.jsp?tab-meir">查看详情</a>
			<a href="javascript:;" class="massage_close clove2">取消</a>
		</div>
	</div>
</div>

<!--网站地图-->

<c:choose>
  <c:when test="${session.customer==null}">
    <script>
        $(function () {
            $('#j-gift-btn').on('click',function(){
                alert('请先登录！');
            });
        });
    </script>
  </c:when>
  <c:otherwise>
    <script>
        $(function () {
            $('#j-gift-btn').on('click',function(){
                $('#j-modal-gift').modal('show');
            });
        });
    </script>
  </c:otherwise>
</c:choose>
<%--google统计--%>
<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');   ga('create', 'UA-96378690-1', 'auto');
    ga('send', 'pageview'); </script>

<%--seo统计--%>
 

<script>
    $(function(){
		var loginname=$('#j-username').val();
		   if(loginname){
				message1();
			}
		
        $("#r_close").click(function(){
            window.location.reload();
        });

				$("#ck_app").mouseenter(function() {
					$.ajax({
						url: "/asp/checkAgentURLogin.aspx",
						type: "post",
						dataType: "text",
						success: function(data) {
							if('false' == data) {
								alert('您好，请先登录游戏账号，若无账号，请先进入网页注册!');
								$("#ck_app").find("#APPxiazai").removeClass("txt")
								$("#APPxiazai").find("h2").hide();
								$("#APPxiazai").find(".mb10").hide();
								return false;
							} else {
							$("#APPxiazai").find("h2").show();
							$("#APPxiazai").find(".mb10").show();
							$("#ck_app").find("#APPxiazai").addClass("txt");
							}
						}
					});
				})
        $("#ck_weixin").mouseenter(function() {
          $("#weixin-kefu").show();
          $("#ck_weixin").find("#weixin-kefu").addClass("txt");
        })
        $("#ck_weixin").mouseleave(function() {
          $("#weixin-kefu").hide();
          $("#ck_weixin").find("#weixin-kefu").removeClass("txt");
        })
     
				
				$("#iconfont_no2").click(function () {
						var speed=200;//滑动的速度
						$('body,html').animate({ scrollTop: 0 }, speed);
						return false;
				 }); 
		
				

    })
	function message1(){
		

		var msg = sessionStorage.getItem('showmassage')
		$.get("/asp/getGuestbookCountNew.aspx",function(data){

			if($("#massage").val()==="true"){
				$(".massaage_box").hide();
				return false;
			}

        if(msg==="0" || msg===null){

        $(".massaage_box").show();

	        }else{

	        $(".massaage_box").hide();
	        return false;

        }

			if(data==="0"){
				$(".massaage_box").hide();
				return false;
			}else{
				$(".massaage_box").show();
			}

		})

		 
		$(".massage_close").click(function(){
			$(".massaage_box").hide();
			  sessionStorage.setItem('showmassage', 1);
		})

		$(".clove1").click(function(){
			window.location.href="/userManage.jsp#tab-meir"
			$(".massaage_box").hide();
			sessionStorage.setItem('showmassage', 1);
		})
 
		
   }
</script>

 
 