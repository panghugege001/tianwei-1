<%@ page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
String infoValue=(String)session.getAttribute("infoValue4Live800");
if(infoValue==null)infoValue="";
%>
<!DOCTYPE>
<html>
<head>
	<jsp:include page="commons/common.jsp" />
	<link rel="stylesheet" type="text/css" href="mobile/css/lib/swiper/swiper.css" />
	<link rel="stylesheet" type="text/css" href="mobile/css/index.css?v=22" />
	<link rel="stylesheet" type="text/css" href="mobile/css/appdown.css?v=9" />
</head>
<body >
<div class="tab-bd">
	<div id="page-index" data-page-index class="tab-panel active">
		<div class="main-wrap" style="margin-bottom: 2em;">
			<div class="header-margin"></div>
			<div class="content setTabGroup">
				<section class="banner-section" >
					<div class="swiper-container">
						<div class="swiper-wrapper">
							<div class="swiper-slide"><a href="/activety/zhoumo/index.jsp" target="_self"><img src="mobile/images/banner/zhoumo_web.jpg" style="width:100%"/></a></div>
						</div>
						<div class="swiper-pagination">
							<span class="swiper-pagination-bullet swiper-pagination-bullet-active" tabindex="0" role="button" aria-label="Go to slide 1"></span>
						</div>
					</div>
					<div id="index-news-marquee" class="common-marquee">
						<div class="txt">天威公告：</div>
						<a href="mobile/news.jsp" class="marquee-content" ></a>
					</div>
				</section>
				<div class="tab-block tab-index">
					<div data-tag="" class="tab-button mui-col-xs32-6 setTabMenu active">网页版游戏</div>
					<div data-tag="" class="tab-button mui-col-xs32-6 setTabMenu">手机客户端</div>
				</div>
				<div class="tab-index-cont">
					<div class="setTabCon">
						<ul class="ul-game">
							<li><a href="/mobile/app/slotGame.jsp?platform=PT&openMobile" class=""><img src="mobile/images/pt.png" width="" height="" /></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=PTSW&openMobile" class=""><img src="mobile/images/PTSW.png" width="" height="" /></a></li>
							<li><a href="javascript:;" class="dtgame"><img src="mobile/images/dt.png" width="" height="" /></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=MG&openMobile" ><img src="mobile/images/mg.png" width="" height=""/></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=PNG&openMobile"><img src="mobile/images/png.png" width="" height=""/></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=TTG&openMobile"><img src="mobile/images/ttg.png" width="" height=""/></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=QT&openMobile"><img src="mobile/images/qt.png" width="" height=""/></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=NT&openMobile"  ><img src="mobile/images/nt.png" width="" height=""/></a></li>
							<li><a href="javascript:;" class="aggame2"><img src="mobile/images/ag-c.png" width="" height="" class="ag" /></a></li>
							<li><a href="/mobi/gameNTwoRedirect.aspx" class="j-check"><img src="mobile/images/N2Live.png" width="" height="" class="" /></a></li>
							<li><a href="javascript:;" class="aggameFish"><img src="mobile/images/ag-fish.png" width="" height="" class="ag"/></a></li>
							<li><a href="/mobile/app/slotGame.jsp?platform=AG&openMobile" class=""><img src="mobile/images/ag-slot-c.png" width="" height="" class="ag"/></a></li>
							<li><a href="javascript:;" class="sbMobileLogin"><img src="mobile/images/sb.png" width="" height="" style="height: 45%;"/></a></li>
							<li><a href="/asp/ttLogin.aspx?gameName=EGIGame&gameType=0&gameId=14900&lang=zh-cn&deviceType=web/asp/ttLogin.php?gameName=EGIGame&gameType=0&gameId=14900&lang=zh-cn&deviceType=mobile" class="GG_game j-play"><img src="mobile/images/GG.png" width="" height="" style="height: 45%;"/></a></li>
							<li><a href="javascript:;" class="MW_buyu"><img src="mobile/images/MW.png" width="" height="" style="height: 45%;"/></a></li>
						</ul>
					</div>

					<div class="setTabCon unis">
						<ul class="ul-game">
							<li><a href="javascript:;" class="ptgame"><img src="mobile/images/pt-c.png" width="" height="" class="ag"/></a></li>
							<li><a href="javascript:;" class="dtgame"><img src="mobile/images/dt-c.png" width="" height="" class="ag"/></a></li>
							<li><a href="javascript:;" class="aggame2"><img src="mobile/images/ag-c.png" width="" height="" class="ag"/></a></li>
							<li><a href="javascript:;" class="aggameFish"><img src="mobile/images/ag-fish.png" width="" height="" class="ag"/></a></li>
							<li><a href="javascript:;" class="aggame"><img src="mobile/images/ag-slot-c.png" width="" height="" class="ag"/></a></li>
						</ul>
					</div>
					<div class="yh-list center">
						<a href="/mobile/preferential.jsp?lottery"><img src="/images/promotion/lottery.jpg"></a>
						<a href="/activety/redpacket/index.jsp"><img src="/images/promotion/redpacket.jpg"></a>
						<a href="/activety/zhoumo/index.jsp"><img src="/images/promotion/new_promotion/zhoumo_yh.jpg?v=0101"></a>
					</div>
				</div>

				<div class="pro-header"><a href="/mobile/preferential.jsp" class="fl">最新优惠</a><a href="javascript:;" class="fr" id="pro-more">更多>></a></div>
				<div class="selfGet-boxs" id="j-youhui">
				</div>
			</div>
			<div class="footer-margin"></div>
		</div>
	</div>
	<div id="page-detail" class="tab-panel">
		<div class="page-inner">
			<div class="space-2"></div>
			<div class="content-close"> <a href="mobile/preferential.jsp">X</a></div>
			<div class="j-content">

			</div>
		</div>

	</div>
</div>
<input id="j-isLogin" type="hidden" value="${session.customer!=null}">
<jsp:include page="commons/footer.jsp" />
<script type="text/javascript" src="mobile/js/lib/swiper/swiper.js"></script>
<script type="text/javascript" src="mobile/js/lib/jquery/qrcode.js"></script>
<script type="text/javascript" src="mobile/js/lib/jquery/jquery.qrcode.js"></script>
<script type="text/javascript" src="mobile/js/lib/rotate/jquery.rotate.min.js"></script>
<script type="text/javascript" src="mobile/js/index.js?v=17"></script>
<script type="text/javascript">
    footerBar.active("index");

    /*function queryAgentCode(){
        window.agentCode = '';
        $.ajax({
            url : "${ctx}/asp/getAgentCode.aspx",
            type : "post",
            dataType : "text",
            success : function(data) {
                window.agentCode = data;
            }
        });
    }*/
    //苹果手机不显示
    if(getMobileKind()=='IOS'){
        //$('.ptgame').remove();
        $('.ptgame').click(function(){
//  					mobileManage.getModel().open('goGame',[{
//  						title:'PT老虎机',
//  						content:'${session.customer==null}'=='true'?'登录您的账号，才可进入游戏。':'您已登录，可直接进行游戏',
//  						onSubmit:function(e,model){
//  							mobileManage.getLoader().open('进入中');
//  							window.location = 'mobile/ptMobileLogin.jsp';
//  						}
//  					}]);
            if('${session.customer==null}'=='true'){
                alert('[提示]请登录游戏账号之后，进行游戏！');
                return;
            }
// 					window.location.href = window.location.origin+'/mobile/ptMobileLogin.jsp';
            webapp.redirect(window.location.origin+'/mobile/ptMobileLogin.jsp');
        });

    }else{
        //PT老虎机下载弹窗
        $('.ptgame').click(function(){
            if('${session.customer==null}'=='true'){
                alert('[提示]请登录游戏账号之后，下载进入游戏！');
                return;
            }
            mobileManage.getModel().open('goOrDownload',[{
                title:'PT老虎机',
                content:[
                    '1.请您务必先使用手机网页版登录激活PT账号后，再下载客户端游戏，登录客户端时，账号前面请加上“K”',
                    '2.网页版游戏请直接进入'
                ].join('<br>'),
                download_url:'https://o46aaoh6w.qnssl.com/v3/new/newClient.apk',
                goGameFn: function(e,model){
//  							window.location.href = window.location.origin+'/mobile/ptMobileLogin.jsp';
                    webapp.redirect(window.location.origin+'/mobile/ptMobileLogin.jsp');
                }
            }]);
        });
    }
    //AG下载弹窗
    $('.aggame').click(function(){
        mobileManage.getModel().open('goOrDownload',[{
            title:'AGIN国际厅',
            content:[
                '客户端游戏方式：',
                '1.首先必须在电脑上，透过官网"手机投注"中，设置密码。',
                '2.下载AGIN手机客户端。',
                '3.登录账户前面加上ki_，接着输入您设置的密码即可。'
            ].join('<br>'),
            download_url:'http://agin.cc/',
            goGameFn: function(e,model){
                mobileManage.getLoader().open('进入中');
                mobileManage.ajax({
                    url:'mobi/gameAginRedirect.aspx',
                    callback:function(result){
                        if(result.success){
                            window.location.href = result.data.url;
                        }else{
                            model.$message.html(result.message);
                        }
                        mobileManage.getLoader().close();
                    }
                });
                return false;
            }
        }]);
    });


    //AG下载弹窗
    $('.aggame2').click(function(){
        mobileManage.getModel().open('goOrDownload',[{
            title:'AGIN国际厅',
            content:[
                '客户端游戏方式：',
                '1.首先必须在电脑上，透过官网"手机投注"中，设置密码。',
                '2.下载AGIN手机客户端。',
                '3.登录账户前面加上ki_，接着输入您设置的密码即可。'
            ].join('<br>'),
            download_url:'http://agin.cc/',
            goGameFn: function(e,model){
                mobileManage.getLoader().open('进入中');
                mobileManage.ajax({
                    url:'mobi/gameAginRedirect.aspx',
                    callback:function(result){
                        if(result.success){
                            window.location.href = result.data.url;
                        }else{
                            model.$message.html(result.message);
                        }
                        mobileManage.getLoader().close();
                    }
                });
                return false;
            }
        }]);
    });

    //TTG老虎机
    $('#ttggame').click(function(){
        var url = 'https://ams-games.ttms.co/casino/longfa_lh/lobby/index.html?playerHandle={0}&account={1}&lang=zh-cn&platformUrl={2}';
        mobileManage.getModel().open('goGameOrFunGame',[{
            title:'TTG老虎机',
            message:('${session.customer==null}'=='true'?'登录您的账号，才可进入游戏。':'您已登录，可直接进行游戏。')+'<br><div class="space-1"></div><span style="color:red;">更换游戏时，请返回官网在进入！</span>',
            goFun:function(){
                window.location.href = String.format(url,'999999','FunAcct',window.location.href);
            },
            goGame:function(){
                window.TTplayerhandle = '${session.TTplayerhandle}';
                if(TTplayerhandle){
                    window.location.href = String.format(url,TTplayerhandle,'CNY',window.location.href);
                }else{
                    mobileManage.getLoader().open("进入游戏中");
                    mobileManage.ajax({
                        url:'mobi/loginTTG.aspx',
                        callback:function(result){
                            mobileManage.getLoader().close();
                            if(result.success){
                                window.TTplayerhandle = result.message;
                                window.location.href = String.format(url,TTplayerhandle,'CNY',window.location.href);
                            }else{
                                alert(result.message);
                            }
                        }
                    });
                }
            }
        }]);
    });

    //DT
    $('.dtgame').click(function(){
        var downloadUrl = getMobileKind()=='Android'?'http://down.dreamtech.asia/LONGDU/android.html':'http://down.dreamtech.asia/LONGDU/ios.html';
        mobileManage.getModel().open('goOrDownload',[{
            title:'DT老虎机',
            content:[
                'DT老虎机也有客户端啰，立即下载体验！'
            ].join('<br>'),
            goGameText:'进入游戏',
            goDownloadFn: function(e,model){
                webapp.redirect(downloadUrl);
            },
            goGameFn: function(e,model){
                mobileManage.redirect('DTGame');
            }
        }]);
    });

    $('.aggameFish').click(function(){
        mobileManage.getModel().open('goGame',[{
            title:'AG捕鱼游戏',
            content:'${session.customer==null}'=='true'?'登录您的账号，才可进入游戏。':'您已登录，可直接进行游戏',
            onSubmit:function(e,model){
                mobileManage.getLoader().open('进入中');
                mobileManage.ajax({
                    url:'mobi/gameAginRedirect.aspx?agFish=1',
                    callback:function(result){
                        if(result.success){
                            window.location.href = result.data.url;
                        }else{
                            model.$message.html(result.message);
                        }
                        mobileManage.getLoader().close();
                    }
                });
                return false;
            }
        }]);
    });

</script>

<script>
    $(function(){
        $(".sbMobileLogin").click(function(){
            $.get("/asp/sbMobileLogin.aspx",function(data){
                if(data.code==0){
                    alert(data.msg)
                }else if(data.code==-1){
                    alert(data.msg)
                }else{
                    window.location.href="/asp/sbMobileLogin.aspx";
                }
            })
        })

        $(".MW_buyu").click(function(){
            if ($("#j-isLogin").val()) {
                $.get("/mobi/mwgLogin.aspx",function (response) {
                    if (response.code == 0) {
                        window.location.href=response.data;
                    } else {
                        alert(response.msg);
                    }
                })
            } else {
                alert('您好，请先登录！');
            }
        })
    })
</script>

<script>
    window.onload=function(){
        $("#j-youhui").children("div").eq(6).find("a").attr("href","mobile/mb_dianying/index.html")
    }
</script>

<c:if test="${session.customer==null}">
	<script>
        $(function(){
            $(document).on('click','.j-play,.j-check', function (){
                alert('请先登录！！！');
                return false;
            });
        });
	</script>
</c:if>
</body>
</html>