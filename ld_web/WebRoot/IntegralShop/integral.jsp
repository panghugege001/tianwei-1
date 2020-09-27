<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="css/pagination.css">
		<link rel="stylesheet" href="/css/index-new.css?v=09558">
		<link rel="stylesheet" href="/IntegralShop/css/integral.css">
		<link href="css/index.min.css?v=77781" rel="stylesheet">
	</head>

	<body class="jfsc-bg">
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
			<div class="jfsc-banner"></div>
			<div class="jfsc-container">
				<div class="jfsc-con-left">
					<div class="jfsc-login">
						<c:if test="${session.customer!=null}">
				            <div class="login">
				              <div class="level split">
				                <!-- <i class="sp-level sp-level-${session.customer.level}"></i> -->
				                <i class="sp-level sp-lv-${session.customer.level}"></i>
				                <p>游戏帐号：<span>${session.customer.loginname}</span></p>
				                <p class="level-text-${session.customer.level}">会员等级：</p>
				                <p>有效积分：<span class="userPoint"></span> 分</p>
				              </div>
				              <p class="btn-row">
				                <a class="btnClass lottery-record" href="javascript:;">
				                  <i class="iconfont icon-jilu"></i> 我的抽奖记录
				                </a>
				              </p> 
				              <div class="user-record">
				                      <div>
				                        <div class="record-title"> 
				                          <h2>幸运名单</h2>
				                          <!-- <span class="lottery-record">我的抽奖记录<i class="jiantou iconfont icon-jiantou"></i></span> -->
				                        </div>
				                        <div class="list">
				                          <div class="bd">
				                            <ul></ul>
				                          </div>
				                        </div>
				                      </div>
				                      <!--           <c:if test="${session.customer!=null}">
				                        <p class="rule">
				                          抽奖规则：每次抽奖使用<span class="point-expend"></span>，每天抽奖次数不限。
				                        </p>
				                    </c:if> -->
				               </div>

				            </div> 
				          </c:if>
				          <c:if test="${session.customer==null}">
				            <div class="login login-out">
				           	<p>登录后可查看会员详细信息</p>
				              <div id="loginBtn" class="spanBtnStyle" data-toggle="modal" data-target="#modal-login">
				                  点击登录
				              </div>

				              <!-- <a class="btnClass" id="loginNow" href="javascript:void(0)" data-toggle="modal" data-target="#modal-login">马上登陆</a> -->
				            </div>
				          </c:if>
					</div>
					<div class="jfsc-hyqy">
						<div class="spantop"></div>
						<div class="hyqy-title">
							<span>会员权益</span>
							<span class="c-dhgz" onclick="OpenLayer()">兑换规则</span>
						</div>
						<div class="hyqy-box">
							<ul>
								<li><i class="dysy-icon"></i>兑一送一</li>
								<li><i class="sbjf-icon"></i>双倍积分</li>
								<li><i class="dhzk-icon"></i>兑换折扣</li>
							</ul>
						</div>
						<div class="spanbottom"></div>
					</div>
					<div class="jfsc-zjxx">
			              <div class="user-record">
			                      <div>
			                        <div class="record-title">
			                          <h2><i class="zjxx-icon"></i>会员中奖信息</h2>
			                          <!-- <span class="lottery-record">我的抽奖记录<i class="jiantou iconfont icon-jiantou"></i></span> -->
			                        </div>
			                        <div class="list">
			                          <div class="bd">
			                            <ul></ul>
			                          </div>
			                        </div>
			                      </div>
			                      <!--           <c:if test="${session.customer!=null}">
			                        <p class="rule">
			                          抽奖规则：每次抽奖使用<span class="point-expend"></span>，每天抽奖次数不限。
			                        </p>
			                    </c:if> -->
			               </div>
					</div>
					<div class="jfsc-dhjl">
							<a href="javascript:;" class="jfdh-jl open-exchange-record"><i class="jfdh-icon"></i>我的兑换记录</a>
							<h3>抽奖规则：</h3>
							<p>每次抽奖使用<span class="point-expend"></span>，每天抽奖次数不限。</p>
						</div>
					</div>
				<div class="jfsc-con-right">
					<div class="jfsc-cjlb">
						<div class="lucky-lottery clearfix">
				          <div class="lotto-mid-box">
				              <!-- <a class="pression-rule" onclick="OpenLayer();">规则</a> -->
				              <div class="mid-cont-box">
				                  <i class="zuanshi"></i>
				                  <div class="text-box">
				                      <h1>还可抽奖  &nbsp;<span class="have-times c-gold"></span>  &nbsp;次</h1>
				                      <p>8000积分可以兑换一次抽奖机会</p>
				                      <p>当前有效积分 &nbsp;<span class="c-gold userPoint"> 0 </span></p>
				                  </div>
				                  <div class="lotto-btn"></div>
				              </div>
				          </div> 
				          <div class="lotto-box">
				          </div>
				        </div>
					</div>
					<div class="jfsc-dhlb">
						<div class="main-goods clearfix">
					      <div class="goods-wp">
					          <div class="goods-nav">
					            <div data-type="other" class="active" style="border-radius:20px 0 0 0">彩金福利兑换</div>
					            <div data-type="physical" style="border-radius:0 20px 0 0">礼品兑换</div>
					          </div>

					          <div class="goods-title">

					          <div class="left">
					            <span>会员积分兑换值</span>
					            <div class="goods-tab">
					              <span class="active">全部</span>
					              <span data-price="1w">10000以上</span>
					              <span data-price="5w">50000以上</span>
					              <span data-price="10w">100000以上</span>
					            </div>
					          </div>

					          </div>
					      <!--商品列表-->
					      <ul id="goods-list" class="clearfix">
					      </ul>
					      </div>
<!-- 					      <div class="myrecord-list-box list">
					        <h2 style="margin-bottom: 20px;">兑换记录</h2>
					        <div class="bd">
					          <ul>
					          </ul>
					        </div>
					      </div> -->
					    </div>
					</div>
				</div>
			</div>
		
		    <!--登录{-->
		    <div class="modal fade" id="modal-login" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
		        <div class="modal-dialog" role="document">
		            <div class="modal-content">
		                <div class="modal-logo"><img src="/images/logo.png" alt=""></div>
		                <div class="modal-hd">
		                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		                    <h4 class="modal-title">会员登录</h4>
		                </div>
		                <div class="modal-bd">
		                    <form action="" method="post" class="ui-form">
		                        <div class="ui-form-item topic-page">
		                            <label for="" class="ui-label rq-value">用户名：</label>
		                            <input type="text" name="address" class="ui-ipt"  id="j-name-modal" maxlength="20"/>
		                        </div>
		                        <div class="ui-form-item topic-page">
		                            <label for="" class="ui-label rq-value">密码：</label>
		                            <input type="password" class="ui-ipt" name="addressee" id="j-pwd-modal"/>
		                        </div>
		                        <div class="ui-form-item">
		                            <input type="submit" class="btn btn-danger btn-block" id="j-login-mod" value="确定" onclick="modalLogin(this,'modal');"/>
		                        </div>
		                    </form>
		                </div>
		                <div class="modal-ft"><a href="https://chat.l8servicelehu.com/chat/chatClient/chatbox.jsp?companyID=454&jid=&" class="c-red link">如有问题请联系24小时在线客服</a></div>
		            </div>
		        </div>
		    </div>
		    <input type="hidden" id="j-isLogin" value="${session.customer!=null}">
		    <!--}登录-->

		    <!--登录状态-->
		    <input type="hidden" value="${session.customer.loginname}" id="checkUserIsLoad" />
		    <script src="/js/jquery18.js"></script>
		    <script src="${ctx}/js/superslide.2.1.js"></script>
		    <script src="${ctx}/js/items.js?v=1"></script>

		    <!--积分商城-->
		    <script src="/js/jquery-tmpl.js"></script>
		    <script src="/js/layer/layer.js"></script>
		    <script src="js/pagination.min.js"></script>
		    <script src="/js/jquery.lazyload-v1.9.1.min.js"></script>
		    <script src="js/integral-common.js?v=3"></script>
		    <script>
		      // 等级
		      levelNum = parseInt('${session.customer.level}');
		    </script>
		 <script src="js/newIntegral.js?v=1"></script>
		    <!--模板-->
		    <script id="goods-list-tpl" type="text/x-jquery-tmpl">
		      <li
		        data-type="{{= type}}"
		        data-1w="{{= !(parseInt(vipSaveRange)<10000)}}"
		        data-5w="{{= !(parseInt(vipSaveRange)<50000)}}"
		        data-10w="{{= !(parseInt(vipSaveRange)<100000)}}"
		      >
		          <div class="pic">
		            <img class="lazy" data-original="{{= imageUrl}}" alt="" />
		            <div class="border-line"></div>
		          </div>

		          <div class="bottom ">
		            <p class="name"><span class="c-a1a1a1"></span>{{= name}}</p>
		            <p class="range c-red"><span class="c-a1a1a1">积分：</span>{{= range}}</p>
		            <a class="start-duihuan" href="javascript:;">立即兑换</a>

		          </div>
		      </li>
		    </script>
		    <script>
		        var OpenLayer = function (){
		        layer.open({
		            title: ['温馨提示'],
		            skin: 'top-class',
		            area:['600px',''],
		            content: '<div>\
		                          <p><span class="c-darkblue">兑一送一：</span>白金及以上VIP会员，指定日期开启兑换；</p>\
		                          <p><span class="c-darkblue">双倍积分：</span>全员可享受，指定日期开启存款送双倍积分；</p>\
		                          <p><span class="c-darkblue">兑换折扣：</span>根据会员等级，享受不同比例折扣，等级越高折扣越高。</p>\
		                          <p> <span class="c-red">注：</span>后期会开放更多会员权益，请多多关注</p>\
		                        </div>'
		        })
		    }
		    </script>
		<input type="hidden" id="j-isLogin" value="${session.customer!=null}">

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
	</body>
</html>