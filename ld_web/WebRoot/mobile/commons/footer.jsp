<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<script type="text/javascript" src="mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="mobile/js/lib/jquery/imagesloaded.pkgd.min.js"></script>
	<script type="text/javascript" src="mobile/js/lib/mui-0.2.1/mui.js"></script>
	<script type="text/javascript" src="mobile/js/util.js"></script>
	<script type="text/javascript" src="mobile/js/String.js"></script>
	<script type="text/javascript" src="mobile/js/Date.js?v=1"></script>
	<script type="text/javascript" src="mobile/js/MUIDate.js?v=1"></script>
	
	<script type="text/javascript" src="mobile/js/MUIModel.js?v=1210"></script>
	<script type="text/javascript" src="mobile/js/Loader.js?v=1019"></script>
	<script type="text/javascript" src="mobile/js/UserManage.js?v=1020"></script> 
	<script type="text/javascript" src="mobile/js/BankManage.js?v=11"></script>
	<script type="text/javascript" src="mobile/js/AgentManage.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/TPPManage.js?v=18"></script>
	<script type="text/javascript" src="mobile/js/SelfGetManage.js?v=12"></script>
	<script type="text/javascript" src="mobile/js/SignManage.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/MobileManage.js?v=1034"></script>
	<script type="text/javascript" src="mobile/js/MobileGrid.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/MobileComboBox.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/CSSMarquee.js?v=10"></script>
	<script type="text/javascript" src="mobile/js/self/ExperienceManage.js?v=11"></script>
	<script type="text/javascript" src="mobile/js/self/EmigratedManage.js?v=10"></script>
	
	<script type="text/javascript" src="mobile/js/common/FooterBar.js?v=12"></script> 
	<script type="text/javascript" src="mobile/js/common/HeaderBar.js?v=10"></script>
	
	<script type="text/javascript" src="mobile/js/WebApp.js?v=1019"></script>
	<script type="text/javascript" src="mobile/js/FooterContent.js?v=9"></script> 
    <script type="text/javascript">
		$('.common-screen').css('opacity','0');
			window.mobileManage = new MobileManage('${ctx}/','${imgCode}');
			window.webapp = new WebApp();
			//mobileManage.getLoader().open('载入中');
			var headerBar = new HeaderBar({role:'${session.customer.role}'});
			var footerBar = new FooterBar({role:'${session.customer.role}'});
			var _pageId = mobileManage.getSessionStorage('common').pageId||'index';
			//开启关闭 联系我们
			$('.common-screen').click(otherButtonClick);
			headerBar.$el.find('#comm-other-button').click(otherButtonClick);
			
			headerBar.$el.find('#comm-login-button').click(function(){
				mobileManage.getModel().open('login',['index']);
			});
			headerBar.$el.find('#comm-register-button').click(function(){
				var reflag = ${session.agentflag} + '';
				if(reflag == 'true'){
					alert('您好,请您先从手机网页版进行注册!');
				} else {
					mobileManage.redirect('register');
				}
			});
			headerBar.$el.find('#comm-logout-button').click(function(){
				mobileManage.getModel().open('logout');
			});
			$(window).load(function(){
				var $toolbar=$('#toolbar');
				$toolbar.find('.tool-close').on('click',function () {
					otherButtonClick();
				});
				
				
	 			//下方浏览方式切换
				window.footerContent = new FooterContent('.main-wrap');
				/*if(webapp.isWebApp()&&webapp.getVersion()){
					var $el1 = $([
						'<div class="item">',
						'	<i class="iconfont icon-voice" ></i>',
						'	<span class="text" >语音客服</span>',
						'</div>'
					].join(''));
					
					$el1.bind('click',function(){
						var name = '${session.customer.loginname}'||'游客';
						window.webapp.openAppKeFu(name);
					});
					
					$('.common-contact .menu-side').prepend($el1);
				} */
				var NEEDMODIFY = '${session.NEEDMODIFY}';
			    if(NEEDMODIFY == "1" ){
			      	alert("您的密码安全指数较低，请修改以保障资金安全");
			      	mobileManage.getModel().open('modifyPassword');
			    }
			  // mobileManage.getLoader().close();
			  
			});
			/**
			 * 取得设备ID
			 */
		    function getDeviceId(deviceId, platform, version){
		    	/*if('${session.mobileDeviceID==null?true:false}'=='true'){
		    		$.post("${ctx}/asp/signDevice.aspx", {"cpuid":deviceId,"platform":platform
			        }, function (returnedData, status) {
			            if ("success" == status) {
			    			if('${session.customer==null?true:false}'=='true'){addRegister();}
			            }
			        });
		    	}else{
		    		if('${session.customer==null?true:false}'=='true'){addRegister();}
		    	}*/
		    };
			//开启关闭 联系我们
			function otherButtonClick(){
				if($('body').hasClass('show-contact')){
					$('body').removeClass('show-contact');
				}else{
					$('body').addClass('show-contact');
				}
			}
			//电话回播
			function makeCall(){
				otherButtonClick();
				mobileManage.getModel().open('makeCall');
			}
			
			//开启QQ
			function openQQ(){
				var download = getMobileKind()=='Android'?'http://gdown.baidu.com/data/wisegame/dc429998555b7d4d/QQ_398.apk':'https://itunes.apple.com/cn/app/qq-2011/id444934666?mt=8';
				webapp.redirect('mqq://im/chat?chat_type=wpa&uin=800134430','尚未安装QQ！',download);
// 				window.location.href = 'mqq://im/chat?chat_type=wpa&uin=ke800134430';
			}
			//开启email
			function openEmail(){
				//safri 无法用window.open开启
				webapp.redirect('mailto:tianwei661@gmail.com');
 
			}
		</script>
 
  <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');   ga('create', 'UA-96378690-1', 'auto');
  ga('send', 'pageview'); </script>
  <script>
 
$(function () {
    var $H = $(window).outerHeight() + 200;
    var $W = $(window).outerWidth();
    var $redpacket = $(".redpacket");
    $(".redpacket-form .closesss,.redpacket-form .btnss").click(function () {
        $("#redpacket-form").hide();
    })
    var over=false;
    function redpacket() {
        if(over)return;
        var randomnum = Math.ceil(Math.random() * 15);
        var imgdon = $('<img style="position: fixed; cursor: pointer;z-index: 10;" src="/images/ahongbao/' + randomnum + '.png" >');
        $(imgdon).css({
            left: Math.ceil(Math.random() * $W),
            top: Math.ceil(Math.random() * 100 - 100)
        })
        imgdon.hover(function () {
            $(imgdon).stop();
        }, function () {
            $(imgdon).animate({top: $H}, 10000, function () {
                imgdon.remove()
            });
        })
        imgdon.click(function () {
            $.ajax({
                url: '/redrain/receiveCoupon.aspx',
                data: {
                    title: 'redrain',
                    platform: 'html5'
                },
                type: 'post',
                success: function (data) {
                    if (data.code == '204') {
                        $('#redpacket-form').show();
                        $('.tips-txt').html('可惜了<br>红包是空的')
                        $('.btnss').text("谢谢参与")
                    } else if (data.code == '203') {
                        over=true;
                        $('#redpacket-form').show();
                        $('#redpacket-form .over-txt').css({visibility:''});
                        $('.btnss').html("<a style='color:#542e10' href='/mobile/redpacket.jsp'>进入红包账户</a>")
                        $('.tips-txt').html('你总共获得'+data.times+'个红包<br>累计获得红包金额'+data.depositAmount);
                    } else if (data.code == '200') {
                        $('#redpacket-form').show();
                        $('.btnss').text("继续参与");
                        $('.tips-txt').html('成功抢到 1 个红包<br>'+data.msg);
                    } else if (data.code == '500' || data.code == '400') {
                        alert(data.msg);
                    } else {
                        alert(data.msg||data);
                    }
                },
                error: function (errTxt) {
                    alert(errTxt)
                }
            })
        })
        $("body").append(imgdon);
        setTimeout(function () {
            $(imgdon).animate({top: $H}, 6000, function () {
                imgdon.remove()
            });
        })
        setTimeout(redpacket, Math.random() * 1000)
    }
    $.post('/redrain/getRainSwitch.aspx',{title: 'redrain',platform:"html5"},function(data){
        if (data.code == '200') {
            redpacket()
        }
    })
})
  </script>
<div class="modal fade in" id="redpacket-form">
	<div class="redpacket-form">
		<div class="over-txt">活动结束</div>
		<div class="tips-txt">
		</div>
		<div class="btnss"></div>
		<div class="closesss">
		</div>
	</div>
</div>
<style>
	.modal{ position: fixed; z-index: 999 !important; top: 0; right: 0; bottom: 0; left: 0; display: none; overflow: hidden; outline: 0; -webkit-overflow-scrolling: touch;}
	.modal {
		background: rgba(0, 0, 0, .8);
	}
	.redpacket-form {
		background: url("/images/ahongbao/model.png") no-repeat;
		position: absolute;
		top: 50%;
		left: 50%;
		margin: -229px 0 0 -157px;
		padding-top:157px;
		width: 315px;
		height: 458px;
	}

	.redpacket-form input {
		width: 90%;
		display: block;
		margin: 0 auto;
		height: 48px;
		border: 1px solid #ccc;
		font-size: 16px;
		text-indent: 0.5em;
	}
	.redpacket-form .over-txt{
		height: 57px;
		color: #f0d23a;
		font-size: 36px;
		visibility:hidden;
		text-align: center;
		font-weight: bold;
	}
	.redpacket-form .btnss {
		width: 209px;
		height: 60px;
		margin: 8px auto 0;
		cursor: pointer;
		font-size: 28px;
		text-align: center;
		line-height: 60px;
		color: #542e10;
		font-weight: bold;
	}

	.redpacket-form .closesss {
		position: absolute;
		bottom: 11px;
		cursor: pointer;
		width: 20px;
		height: 20px;
		left: 148px;
	}
	.tips-txt{
		height:106px;line-height:48px;text-align: center;
		color: #f0d23a;
		font-size: 22px;
		white-space: nowrap;
	}
</style>

 


