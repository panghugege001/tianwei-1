<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>用户中心</title>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="${ctx}/css/user.css?v=5"/>
  <base href="<%=request.getRequestURL()%>"/>
<style type="text/css">
.unis{ display:none;}
</style>
</head>
<body class="user_body">
<div class="index-bg about-bj">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
    <div class="user_center"></div>
    <div class="container w_357">
        <jsp:include page="${ctx}/tpl/userTop.jsp"></jsp:include>
		<div class="cfx about-main" >
			<div class="gb-sidenav">
               <jsp:include page="${ctx}/tpl/userleft.jsp"></jsp:include>
			</div>
			<div class="gb-main-r tab-bd user-main">
        		<div class="youhui_mull">
        			<ul>
        				<li class="active">
        					<a data-toggle="tab" href="#tab-persent">自助存送</a>
        				</li>
        				<li>
        					<a data-toggle="tab" href="#tab-help" onclick="losePromoRecord(1);">救援金</a>
        				</li>    
        				<li>
        					<a data-toggle="tab" href="#tab-return" onclick="getXimaEndTime($('#platform').val());">自助返水</a>
        				</li>   
        				<li>
        					<a data-toggle="tab" href="#tab-level" data-url="/asp/queryBetOfPlatform.aspx" onclick="loadIframe(this);">自助晋级</a>
        				</li>   
        				<li>
        					<a data-toggle="tab" href="#tab-coupon">优惠券专区</a>
        				</li>   
        				<li>
        					<a data-toggle="tab" href="#tab-chouma">免费筹码</a>
        				</li>           				
        			</ul>
        		</div>				
				<div id="tab-weeksent" class="user-tab-box tab-panel">
					<div id="weekSentRecordDiv" class="new"></div>
				</div>
				<!--救援金{-->
				<div id="tab-help" class="user-tab-box tab-panel">
					<div id="losepromoRecordDiv" class="new"></div>
					<div class="prompt-info">
						<p>温馨提示</p> 
						<p class="c-red">救援金有效时间为60天（从派发当天开始计算），规定时间内未领取会自动取消。</p>
					</div>
				</div>
				<!--}救援金-->
				<!--体验金{-->
				<div id="tab-pt2" class="user-tab-box tab-panel">
					<div class="pay-step-info">
						<ol id="status" class="pay-step">
							<li class="active"><em></em> 
								<p class="step-tit">注册信息</p></li>
							<li> <em></em>
								<p class="step-tit">填写电话号码</p></li>
							<li> <em></em>
								<p class="step-tit">银行卡号验证</p></li>
							<li><em></em>
								<p class="step-tit">完成</p></li>
						</ol>
					</div>
					<div class="tab-bd">
						<div class="tab-panel active">
							<div class="ui-form-item">
								<label class="ui-label">用户昵称：</label> <input type="text" class="ui-ipt"
																			 value="${customer.loginname}" disabled="disabled">
							</div>
							<div class="ui-form-item">
								<label class="ui-label">真实姓名：</label> <input type="text" class="ui-ipt"
																			 value='<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.accountName, 1)+'**'" />'
																			 disabled="disabled">
							</div>
							<div class="ui-form-item">
								<label class="ui-label">手机号：</label> <input type="text" class="ui-ipt"
																			value='<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.phone, 6)+'*****'" />'
																			disabled="disabled">
							</div>
							<div class="ui-form-item">
								<label class="ui-label">邮箱：</label> <input type="text" class="ui-ipt"
																		   value='<s:property value="'****'+@dfh.utils.StringUtil@subStrLast(#session.customer.email, 10)" />'
																		   disabled="disabled">
							</div>
							<div class="ui-form-item">
								<label class="ui-label">注册IP：</label> <input type="text" class="ui-ipt"
																			 value="${customer.registerIp}"
																			 disabled="disabled">
							</div>
							<div class="ui-form-item">
								<label class="ui-label">此次登录IP：</label> <input type="text" class="ui-ipt"
																			   value="${customer.lastLoginIp}"
																			   disabled="disabled">
							</div>
							<div class="ui-form-item">
								<a href="javascript:void(0);" class="btn btn-pay pt8next" data="1" title="下一步">下一步</a>
							</div>
						</div>
						<div class="tab-panel fade">

							<form id="phoneCheckValid">

								<div class="next-box1 ui-form">
									<div class="ui-form-item">
										<label for="" class="ui-label">电话号码：</label>
										<input type="text" name="aliasName" class="ui-ipt"
											   value='<s:property value="@dfh.utils.StringUtil@subStrBefore(#session.customer.phone, 6)+'*****'" />'
											   disabled="disabled">
									</div>
									<div class="ui-form-item">
										<a href="javascript:;" class="btn btn-danger" id="sendPhoneVoiceCodeBtn" style="">语音验证</a>
										<a href="javascript:;" class="btn btn-danger" id="sendPhoneCodeBtn" style="">短信验证</a>
									</div>
									<div class="ui-form-item">
										<label for="" class="ui-label">输入验证码：</label>
										<input type="text" id="phoneCode" class="ui-ipt">
									</div>
									<div class="ui-form-item">
										<a href="javascript:void(0);" class="btn btn-danger pt8next" data="2"
										   title="下一步">下一步</a>
									</div>
								</div>
								<div class="next-box2 unis prompt-info">
									<h3 class="tit c-red">温馨提示</h3>
									<p id="_valideteTextId">1.短信安全验证(请用您注册时的手机号码发送短信 #(1)到#(2))</p>
									<p> 2.短信发送成功,<span class="c-red">请于15秒后点击"下一步"</span></p>
									<p>3.必须使用<span class="c-red">注册时填写的手机号码</span>进行发送短信!</p>
									<div class="ui-form-item">
										<a href="javascript:void(0);" class="btn btn-pay pt8next" data="2"
										   title="下一步">下一步</a>
									</div>
								</div>
								<!-- 验证码嵌入位置 -->
							      <div class="wrap" >
							             <div id="captcha-target" style="display: inline-block;width: 339px;"></div>
							      </div>
							</form>
						</div>
						<div class="tab-panel fade">
							<div class="ui-form">
								<div class="ui-form-item">
									<label class="ui-label">开户行：</label>
									<select id="pt8_bank" name="" style="height: 38px;" onchange="getbankno(this.value);">
										<option value="">请选择</option>
										<option value="工商银行" selected="selected">工商银行</option>
										<option value="招商银行">招商银行</option>
										<option value="上海农村商业银行">上海农村商业银行</option>
										<option value="农业银行">农业银行</option>
										<option value="建设银行">建设银行</option>
										<option value="交通银行">交通银行</option>
										<option value="民生银行">民生银行</option>
										<option value="光大银行">光大银行</option>
										<option value="兴业银行">兴业银行</option>
										<option value="上海浦东银行">上海浦东银行</option>
										<option value="广东发展银行">广东发展银行</option>
										<option value="深圳发展银行">深圳发展银行</option>
										<option value="中国银行">中国银行</option>
										<option value="中信银行">中信银行</option>
										<option value="邮政银行">邮政银行</option>
									</select>
								</div>

								<div class="ui-form-item">
									<label class="ui-label">银行卡号：</label>
									<input type="text" id="yhBankNo" name="yhBankNo" class="ui-ipt" disabled="disabled">
								</div>
								<div class="ui-form-item">
									<a href="javascript:void(0);" class="btn btn-pay pt8next" data="3" title="下一步">下一步</a>
								</div>
							</div>

							<p class="c-red">温馨提示：此处只为查看银行绑定信息，不用填写，直接点击下一步即可！</p>
						</div>

						<div class="tab-panel fade">
							<div class="ui-form">
								<div class="ui-form-item">
									<lable class="ui-lable">请选择您要转入的老虎机游戏平台：</lable>
									<label><input type="radio" name="myslotchoice" value="PT" checked="checked"><span>PT</span></label>
									<label><input type="radio" name="myslotchoice" value="MG"><span>MG</span></label>
									<label><input type="radio" name="myslotchoice" value="DT"><span>DT</span></label>
									<label><input type="radio" name="myslotchoice" value="TTG"><span>TTG</span></label>
									<label><input type="radio" name="myslotchoice" value="NT"><span>NT</span></label>
									<label><input type="radio" name="myslotchoice" value="QT"><span>QT</span></label>
									<a href="javascript:void(0);" class="btn btn-pay pt8sub" id="sub" title="确定">确定</a>
								</div>
							</div>

						</div>

					</div>

				</div>
				<!--}体验金-->
				<!--
                <div id="tab-coupon" class="user-tab-box tab-panel">
                  <div>
                      <h1 class="tab-tit">优惠券专区</h1>
                  </div>
                  <form class="userform" action="transferInforCoupon" id="transferform"
                        name="transferInForm"  method="post" onsubmit="return submitRemit()">
                    <div class="ui-form-item">
                      <label class="ui-label">从天威账户转账到</label>
                      <select class="ui-ipt" id="couponType" name="couponType">
                        <option>--请选择平台--</option>
                         <option value="ttg">TTG</option>
                         <option value="pt">PT</option>
                         <option value="nt">NT</option>
                         <option value="qt">QT</option>
                      </select>
                    </div>

                    <div class="ui-form-item">
                      <label class="ui-label">存款金额：</label>
                      <input type="text" class="ui-ipt" style="font-weight:bold;" name="couponRemit" id="couponRemit"/>
                    </div>

                    <div class="ui-form-item">
                      <label class="ui-label">优惠代码：</label>
                      <input type="text" class="ui-ipt" name="couponCode" id="couponCode"/>
                    </div>
                    <div class="ui-form-item">
                      <input type="button" class="btn btn-danger" value="提交" onclick="submitRemit();"/>
                    </div>
                  </form>
                  <div class="prompt-info">
                    <h2 class="tit">优惠券说明</h2>

                    <p>优惠券使用最低金额为100元,请选择正确的游戏平台，填写红利代码，确认提交，红利金额会自动添加到您转到的游戏平台里。</p>

                    <p>PT/TTG/NT/QT平台优惠券，需PT/TTG/NT/QT游戏账户低于5元才能使用存送优惠券。达到相应的有效投注额要求或PT/TTG/NT/QT游戏账户低于5元，才能再次进行PT/TTG/NT/QT户内转账。</p>
                    <p>优惠券有效期为30天，请您在有效期内进行使用。</p>
                    <p>如何得到优惠券，请留意天威最新的相关优惠信息。</p>
                  </div>
                </div>
                 -->

				<div id="tab-coupon" class="tab-panel">
					<div class="tab-bd">
						<div id="tab-coupon1" class="tab-panel active">
							<div class="user-tab-box" style="margin:0;">
							<div class="zizhucunsog_box">
								<form class="userform" action="transferInforCoupon" id="transferform"
									  name="transferInForm"  method="post" onsubmit="return submitRemit()">
									<div class="ui-form-item">
										<label class="ui-label">从天威账户转账到</label>
										<select class="ui-ipt" id="couponType" name="couponType">
											<option>--请选择平台--</option>
											<option value="ttg">TTG</option>
											<option value="pt">PT</option>
                                            <option value="png">PNG</option>
											<option value="nt">NT</option>
											<option value="qt">QT</option>
											<option value="mg">MG</option>
											<option value="dt">DT</option>
										</select>
									</div>

									<div class="ui-form-item">
										<label class="ui-label">存款金额：</label>
										<input type="text" class="ui-ipt" style="font-weight:bold;" name="couponRemit" id="couponRemit"/>
									</div>

									<div class="ui-form-item">
										<label class="ui-label">优惠代码：</label>
										<input type="text" class="ui-ipt" name="couponCode" id="couponCode"/>
									</div>
									<div class="user_tijiao">
										<input type="button" class="btn" value="提交" onclick="submitRemit();"/>
									</div> 
								</form>
								<div class="prompt-info p_120">
									<h2 class="tit c-huangse">优惠券说明</h2>

									<p>100％存送优惠券：最低存款金额50元，流水倍数需15倍，上限588。</p>
                                    <p>88％存送优惠券：最低存款金额50元，流水倍数需13倍，上限888。</p>
                                    <p>68％存送优惠券：最低存款金额50元，流水倍数需10倍，上限1888。</p>
                                    <p>1.只限老虎机平台使用，需游戏账户低于5元才能使用存送优惠券，填写红利代码，确认游戏平台，提交后在相关的游戏里面会自动得到优惠礼金。</p>
                                    <p>2.达到流水倍数或游戏账户低于5元即可进行转入转出。</p>
                                    <p>3.优惠券为30天有效期限，逾时未使用恕不进行补发。</p>
								</div>
							</div>
							</div>
						</div>

						<div id="tab-coupon2" class="tab-panel">
							<div class="user-tab-box">
							<div class="zizhucunsog_box">
								<form class="userform" class="ui-form" action="transferInforRedCoupon" id="transferRedform"
									  name="transferInRedForm"  method="post" onsubmit="return submitRedCouponRemit()">

									<div class="ui-form-item">
										<label class="ui-label">优惠代码：</label>
										<input type="text" class="ui-ipt" name="redcouponCode" id="redcouponCode"/>
									</div>

									<div class="ui-form-item">
										<label class="ui-label">从天威账户转账到</label>
										<select class="ui-ipt" id="redcouponType" name="redcouponType">
											<option>--请选择平台--</option>
											<option value="ttg">TTG</option>
											<option value="pt">PT</option>
											<option value="nt">NT</option>
											<option value="qt">QT</option>
											<option value="mg">MG</option>
											<option value="dt">DT</option>
										</select>
									</div>

									<div class="user_tijiao">
										<input type="button" class="btn" value="提交" onclick="submitRedCouponRemit();"/>
									</div>
								</form>
								<div class="prompt-info p_120">
									<h2 class="tit c-huangse">红包优惠说明</h2>
									<p>1.不限平台使用，请选择正确的游戏平台。填写红利代码，确认提交， 红利金额会自动添加到您转到的游戏平台里。</p>
									<p>2.PT/TTG/NT/QT红包优惠券，需PT/TTG/NT/QT游戏账户低于5元才能使用红包优惠券。达到相应的有效投注额要求或PT/TTG/NT/QT游戏账户低于5元，才能再次进行PT/TTG/NT/QT户内转账。</p>
									<p>3.红包优惠券有效期为30天，请您在有效期内进行使用。</p>
									<p>4.如何得到红包优惠券，请留意天威最新的相关优惠信息。</p>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
				
<!--免费筹码		-->
                <div id="tab-chouma" class="user-tab-box tab-panel">
                  <div>
                      <!--<h1 class="tab-tit">自助优惠 ><span class="cee6">免费筹码</span></h1>
												<ul id="j-pay-couma" class="user-nav tab-nav" style="margin:0;">
													<li class="active"><a href="#tab-chouma" data-toggle="tab">免费筹码</a></li>
													<%--<li><a href="#tab-coupon2" data-toggle="tab">红包优惠券</a></li>--%>
												</ul>                      -->
                  </div>
           	<div class="zizhucunsog_box">       
					<form class="ui-form" action="" method="post">
						<div class="ui-form-item">
							<label class="ui-label">会员等级：</label>
							<input type="text" class="ui-ipt" readonly
							value="<s:property value="@dfh.model.enums.VipLevel@getText(#session.customer.level)"/>">
						</div>
						
						<div class="ui-form-item need-hide" style="display: none;">
							<label class="ui-label">目标账户：</label>
							<select id="j-chipPaltform" class="ui-ipt">
<!-- <option value="" selected="">请选择游戏平台</option>
<option value="PT">PT</option>
<option value="TTG">TTG</option>
<option value="QT">QT</option>
<option value="NT">NT</option>
<option value="MG">MG</option>
<option value="DT">DT</option>
<option value="PNG">PNG</option> -->
							</select>
						</div>						
						
						<div class="ui-form-item">
							<label class="ui-label">筹码金额：</label>
							<input id="moneyVal" type="text" class="ui-ipt" readonly
								   value="">
						</div>
						<div class="user_tijiao">
							<input type="button" class="btn" value="立即领取" onclick="getFreeChip()">
						</div>
						<div class="prompt-info p_120">
							<h3 class="tit c-huangse">温馨提示：</h3>
							<p>1.忠实会员及以上会员，每月可领取一次VIP免费筹码。</p>
							<p>2.免费筹码以您领取时的VIP等级来进行派发。</p>
							<p>3.免费筹码领取后，直接添加到天威主帐户。</p>
							<p>4.免费筹码无需流水即可提款。</p>
							<p>5.领取过程中有任何疑问，请咨询24小时在线客服。</p>
						</div>
					</form>
				</div>	
        </div>				
				
				
				<!--自助存送{-->
				<div id="tab-persent" class="user-tab-box active tab-panel">
					<h1 class="tab-tit">自助优惠 ><span class="cee6"> 自助存送</span></h1>
					<s:url action="execXima" namespace="/asp" var="execXimaUrl"></s:url>
						<div class="zizhucunsog_box">
							<input type="button" class="btn" value="笔存笔送" onclick="showBcbsTable();"/>
							<form class="ui-form" id="selform" action="${execXimaUrl }" method="post" onsubmit="return checkSubmit()">
								<div class="ui-form-item">
									<label class="ui-label">选择平台：</label>
									<select class="ui-ipt" onchange="youHuiNameChange(this.value);" id="youhuiName">
										<option value="">---请选择平台---</option>
										<option value="6001">PT存送优惠</option>
		                                <option value="6002">MG存送优惠</option>
		                                <option value="6003">DT存送优惠</option>
		                                <option value="6004">QT存送优惠</option>
		                                <option value="6005">NT存送优惠</option>
		                                <option value="6006">TTG存送优惠</option>
		                                <option value="6007">PNG存送优惠</option>
		                                <option value="6008">AG真人存送优惠</option>
									</select>
								</div>						
						
						<div class="ui-form-item">
							<label class="ui-label">优惠类型：</label>
							<select name="youhuiType" id="youhuiType" class="ui-ipt"
									onchange="youHuiTypeChange(this.value);">
								<option value="">---请选择存送类型---</option>
								<!-- <option value="590">PT首存优惠</option>
								<option value="591">PT次存优惠</option>
								<option value="592">EA次存优惠</option> -->
								<%--<option value="593">AG次存优惠</option>
                                <option value="594">AGIN次存优惠</option>--%>
								<%--<option value="595">BBIN次存优惠</option>
                                <option value="596">EBET首存优惠</option>
                                <option value="597">EBET次存优惠</option>--%>
								<!-- <option value="598">TTG首存优惠</option>
								<option value="599">TTG次存优惠</option> -->
							</select>
						</div>

						<div class="ui-form-item">
							<label class="ui-label">转账金额：</label>
							<input type="text" class="ui-ipt" name="transferMoney" id="transferMoney"
								   onkeyup="clearNoNum(this);" onblur="getSelfYouhuiAmount(this.value);"/>
						</div>

						<div class="ui-form-item">
							<label class="ui-label">红利金额：</label>
							<input type="text" class="ui-ipt" name="giftMoney" id="giftMoney" readonly/>
						</div>

						<div class="ui-form-item">
							<label class="ui-label">流水倍数：</label>
							<input type="text" class="ui-ipt" style="margin-top:5px;" name="waterTimes" id="waterTimes"
								   readonly/>
						</div>
						<div class="user_tijiao">
							<input type="button" class="btn" value="提交"
								   onclick="return checkSelfYouHuiSubmit();"/>
						</div>
					</form>
					<div class="prompt-info p_120">
						<h3 class="tit c-huangse">温馨提示</h3>
						<p>1.每天 00:00 - 01:00 系统结算时间,短暂时间无法使用自助存送</p>
						<p>2.申请存送后彩金会自动添加到相应平台，您直接进入游戏即可</p>
						<p>3.老虎机存送优惠，存款10元后方可自助操作</p>
						<p>4.DT老虎机仅支持整数转账</p>
					</div>
					<div class="bcbs-table">
						<table class="table">
							<tbody>
							<tr>
								<th>单笔存款金额</th>
								<th>存送比率</th>
								<th>PT/TTG/老虎机钱包</th>
								<th>流水倍数</th>
							</tr>
							<tr>
								<td>10 - 499.99</td>
								<td>15%</td>
								<td>各可5次</td>
								<td rowspan="4">18倍</td>
							</tr>
							<tr>
								<td>500-4999.99</td>
								<td>20%</td>
								<td>各可6次</td>
							</tr>				
							<tr>
								<td>5000-9999.99</td>
								<td>25%</td>
								<td>各可7次</td>
							</tr>				
							<tr>
								<td>10000及以上</td>
								<td>30%</td>
								<td>各可8次</td>
							</tr>
							</tbody>
						</table>
					</div>
				</div>
				</div>
				<!--}自助存送-->
				<!--自助反水{-->
				<div id="tab-return" class="user-tab-box tab-panel">
					<h1 class="tab-tit">自助返水</h1>
					<div class="zizhucunsog_box">
					<s:url action="execXima" namespace="/asp" var="execXimaUrl"></s:url>
					<form class="ui-form" id="selform" action="${execXimaUrl }" method="post"
						  onsubmit="return checkSubmit()">
						<div class="ui-form-item">
							<label class="ui-label">游戏平台：</label>
							<select class="ui-ipt" id="platform">
<!--								<option value="agin" selected>AG</option> -->
								<%--<option value="gpi">GPI</option>--%>
								<%--<option value="bbin">BBIN</option>--%>
								<%--<option value="sb">SB</option>--%>
								<%--<option value="keno">KENO</option>--%>
								<%--<option value="kg">KENO2</option>--%>
								<option value="pttiger">PT</option>
								<!--<option value="ptother">PT其他类</option>-->
								<%--<option value="sixlottery">六合彩</option>--%>
								<%--<option value="ebet">EBET</option>--%>
								<option value="ttg">TTG</option>
								<option value="qt">QT</option>
								<option value="nt">NT</option>
								<option value="mg">MG</option>
								<option value="dt">DT</option>
							</select>
						</div>

						<div class="ui-form-item">
							<label class="ui-label" for="startTime">起点时间：</label>
							<input type="text" class="ui-ipt" readonly name="startTime" id="startTime"/>
						</div>

						<div class="ui-form-item">
							<label class="ui-label" for="endTime">截止时间：</label>
							<input type="text" class="ui-ipt" name="endTime" id="endTime"/>
							<!-- 										<input type="text" class="ui-ipt" name="endTime" id="endTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" onchange="getAutoXimaObject();"/> -->
						</div>

						<div class="ui-form-item">
							<label class="ui-label" for="validAmount">有效投注额：</label>
							<input type="text" class="ui-ipt" name="validAmount" id="validAmount" readonly/>
						</div>

						<div class="ui-form-item">
							<label class="ui-label" for="rate">返水比率：</label>
							<input type="text" class="ui-ipt" name="rate" id="rate" readonly/>
						</div>

						<div class="ui-form-item">
							<label class="ui-label" for="ximaAmount">返水金额：</label>
							<input type="text" class="ui-ipt" name="ximaAmount" id="ximaAmount" readonly/>
						</div>
						<div class="user_tijiao">
							<input type="button" class="btn" value="提交" onclick="return checkEaSubmit();"/>
						</div>
						<div class="prompt-info p_120">
							<h3 class="tit c-huangse">注意事项：</h3>
							<p>1.自助返水结算数据时各平台统计时间为:AG结算的是昨天中午12点至今天中午12 点的有效投注额，PT、TTG、NT、QT结算的是前一天0点到23点59分59(上述为北京时间)。</p> 
                            <p>2.自动返水无平台以及无次数限制，仅能返当天的投注额度，返水额度最低需10元才可申请，申请提交后，会在5分钟内通过审核，返水金额会自动添加到你的天威账号中。</p> 
                            <p>3.自助返水系统维护时间 0:00-3:00、12:00-15:00。 </p>
                            <p>4.系统结算方式：最后一次结算的时间起至该平台系统结算，所产生的有效投注额*该金额对应的比例。</p>
						</div>
					</form>
				</div>
				</div>
				<!--}自助反水-->
				<div id="tab-level" class="user-tab-box tab-panel">
					<h1 class="tab-tit">自助优惠 ><span class="cee6"> 自助晋级</span></h1>
					<div class="j-iframe"></div>
					<div class="prompt-info" style="padding-top: 120px;">
						<h2 class="tit c-huangse">温馨提示：</h2>
						<p>1.当月只可晋级一次，如当月15号达到星级赌神流水要求选择自助晋级，30号达到钻石赌神流水要求将无法晋级</p>
					</div>
				</div>
				<div id="tab-ptcrazy" class="user-tab-box tab-panel">
					<h1 class="tab-tit">自助优惠 ><span class="cee6"> PT疯狂礼金领取</span></h1>
					<div id="ptBigBangDiv" class="new"></div>
				</div>
				<!--签到{-->
				<div id="tab-checkin" class="user-tab-box tab-panel">
					<h1 class="tab-tit">自助优惠 ><span class="cee6"> 每日签到</span></h1>
					<div class="ui-form">
						<div class="ui-form-item">
							<label for="" class="ui-label">签到奖金账户余额：</label>
							<span class="c-red" id="qdmoney1"></span>
						</div>
						<div class="ui-form-item">
							<label for="" class="ui-label">签到奖金账户：</label>
							<select class="ui-ipt" id="signType">
								<option value=""> 请选择</option>
								<option value="ttg"> TTG</option>
								<option value="pt"> PT</option>
								<option value="nt"> NT</option>
								<option value="qt"> QT</option>
								<option value="mg"> MG</option>
                    			<option value="dt"> DT</option>
							</select>
						</div>
						<div class="ui-form-item">
							<label for="" class="ui-label">签到金额：</label>
							<input type="text" class="ui-ipt" id="signRemit">
						</div>
						<div class="ui-form-item">
							<a href="javascript:;" class="btn btn-pay" onclick="return submitSignRemit();">提交</a>
						</div>
					</div>

					<div class="prompt-info">
						<h3 class="tit">温馨提示</h3>
						<p>1.每日存款10元以上，便会激活签到系统，每日仅能签到一次。</p>
						<p>2.每次签到，签到彩金会自动加总，彩金达到10元以上，便可选择转入PT/MG/DT/TTG/NT/QT平台进行游戏。</p>
						<p>3.签到彩金无流水限制。</p>
					</div>
				</div>
				<!--}签到-->
				<!--积分中心{-->
				<div id="tab-point" class="user-tab-box tab-panel">
					<h1 class="tab-tit">自助优惠 ><span class="cee6"> 积分中心</span></h1>
					<div class="ui-form">
						<div class="ui-form-item">
							<label for="" class="ui-label">可用积分：</label>
							<span class="c-red" id="friendPoint"></span>
						</div>
						<div class="ui-form-item">
							<label for="" class="ui-label">历史总积分：</label>
							<span class="c-red" id="totalfriendPoint"></span>
						</div>
						<div class="ui-form-item">
							<label for="" class="ui-label">当前兑换比率：</label>
               <span class="c-red">
                        	  <c:if test="${session.customer.level==0}">500积分兑换1元</c:if>
                <c:if
						test="${session.customer.level==1}">400积分兑换1元</c:if>
                <c:if
						test="${session.customer.level==2}">325积分兑换1元</c:if>
                <c:if
						test="${session.customer.level==3}">280积分兑换1元</c:if>
                <c:if
						test="${session.customer.level==4}">245积分兑换1元</c:if>
                <c:if
						test="${session.customer.level==5}">220积分兑换1元</c:if>
                <c:if
						test="${session.customer.level==6}">100积分兑换1元</c:if>
                                    ;可兑换奖金为：<span id='moneypoint'></span>
              </span>
						</div>
						<div class="ui-form-item">
							<label for="" class="ui-label">积分兑换奖金到天威账户：</label>
							<input type="text" id="pointRemit" class="ui-ipt"><span class="c-red">请输入兑换金额</span>
						</div>
						<div class="ui-form-item">
							<a href="javascript:;" class="btn btn-pay" onclick="return submitPointRemit();">提交</a>
						</div>
					</div>

					<div class="prompt-info">
						<h3 class="tit">温馨提示:</h3>
						<p>1.188体育平台投注不计算在积分内. </p>
						<p>2.每天下午17:00-18:00派发前一天投注额所产生的积分。</p>
						<p>3.积分兑换奖金处输入您对换的金额。</p>
					</div>
				</div>
				<!--}积分中心-->
				<%--<div id="tab-task" class="user-tab-box tab-panel checkin-info">
                  <h1 class="tab-tit">摇摇乐</h1>
                  <div class="ui-form-item">
                    <label for="" class="ui-label">摇摇乐任务奖池：</label>
                    <span class="c-red" id="j-task-balance"></span>
                  </div>
                  <div class="ui-form-item">
                    <label for="" class="ui-label">转到E68账户：</label>
                    <input type="text" class="ui-ipt" id="j-task-amount" >
                  </div>
                  <div class="ui-form-item">
                    <a href="javascript:;"  class="btn btn-danger" onclick="return transferTaskAmount();" >领取</a>
                  </div>
                  <div class="prompt-info">
                    <h3 class="tit">温馨提示</h3>
                    <p>【任务奖池】彩金累计必须大于10元。</p>
                    <p>详细记录请点击【账户清单】--【摇摇乐记录】查看</p>
                  </div>
                </div>--%>


				<%--<div id="tab-friends" class="user-tab-box tab-panel">--%>
					<%--<h1 class="tab-tit">推荐好友</h1>--%>

					<%--<div class="ui-form">--%>
						<%--<div class="ui-form-item">--%>
							<%--<label class="ui-label">您的推荐链接为：</label>--%>
							<%--<a href="javascript:;" class="link" id='friendurl'></a>--%>
							<%--<a href="javascript:;" class="btn-sm btn-copy j-copy">复制</a>--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label for="" class="ui-label">推荐奖金账户余额：</label>--%>
							<%--<span class="c-red" id="friendmoney"></span>--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label for="" class="ui-label">从推荐奖金账户转到：</label>--%>
							<%--<select class="ui-ipt" id="friendType">--%>
								<%--<option value=""> 请选择 </option>--%>
								<%--<option value="ttg"> TTG </option>--%>
								<%--<option value="pt"> PT </option>--%>
								<%--<option value="nt"> NT </option>--%>
								<%--<option value="qt"> QT </option>--%>
								<%--<option value="mg"> MG </option>--%>
								<%--<option value="dt"> DT </option>--%>
							<%--</select>--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label for="" class="ui-label">转账金额：</label>--%>
							<%--<input type="text" class="ui-ipt" id="friendRemit" >--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label for="" class="ui-label"></label>--%>
							<%--<a href="javascript:;"  class="btn btn-danger" onclick="return submitFriendRemit();" >提交</a>--%>
						<%--</div>--%>
					<%--</div>--%>
					<%--<div class="prompt-info">--%>
						<%--<h3 class="tit">温馨提示:</h3>--%>
						<%--<p>1.您的好友必须从您的<span class="c-red">专属链接</span>进来<span class="c-red">注册</span>。</p>--%>
						<%--<p>2.您的好友必须成功<span class="c-red">领取体验金</span>才能成为您介绍的朋友，<span class="c-red">这是永久性的</span>！</p>--%>
						<%--<p>3.被推荐人当日产生的负盈利30%，将会在次日18点前派发予推荐人！</p>--%>
						<%--<p>4.如有任何疑问请及时联系24小时在线客服 </p>--%>
					<%--</div>--%>
				<%--</div>--%>
				<!-- 守护女神 -->
				<%--<div id="tab-goddess" class="user-tab-box tab-panel">--%>
					<%--<h1 class="tab-tit">守护女神</h1>--%>
	                <%--<div class="ui-form" id="goddessapply">--%>
	                    <%--<label class="label" for="" class="ui-label">女神报名：</label>--%>
	                    <%--<select id="applygoddess" name="applygoddess"  class="ipt-txt" >--%>
	                        <%--<option value="0" selected="selected"> 请选择 </option>--%>
	                        <%--<option value="BAISHIMOLINAI">白石茉莉奈</option>--%>
	                        <%--<option value="DONGYUEFENG">冬月枫</option>--%>
	                        <%--<option value="SHENGUANSHIZHI">神关诗织</option>--%>
	                        <%--<option value="CHONGTIANXINGLI">冲田杏梨</option>--%>
	                        <%--<option value="SANSHANGYOUYA">三上悠亚</option>--%>
	                    <%--</select>--%>
	                    <%--<input type="button" class="btn btn-danger" value="报名" onclick="return goddessApply();" >--%>
	                <%--</div>--%>

					<%--<div class="prompt-info">--%>
						<%--<h3 class="tit">温馨提示:</h3>--%>
						<%--<p>1.您的好友必须从您的<span class="c-red">专属链接</span>进来<span class="c-red">注册</span>。</p>--%>
						<%--<p>2.您的好友必须成功<span class="c-red">领取体验金</span>才能成为您介绍的朋友，<span class="c-red">这是永久性的</span>！</p>--%>
						<%--<p>3.您的好友申请存送优惠您才有彩金，您推荐的好友每申请一笔存送优惠，您就有一笔彩金，您推荐的朋友越多，拿到的彩金也就越多哦！</p>--%>
						<%--<p>4.如有任何疑问请及时联系24小时在线客服 </p>--%>
					<%--</div>--%>
				<%--</div>--%>
				
				<!-- 全民闯关 -->
				<%--<div id="tab-qmcg" class="tab-panel user-tab-box">--%>
					<%--<h1 class="tab-tit">天威闯关</h1>--%>
					<%--<div class="ui-form">--%>
						<%--<div class="ui-form-item">--%>
							<%--<label class="ui-label" for="">闯关报名：</label>--%>
							<%--<select id="applytype" name="applytype"  class="ui-ipt" >--%>
								<%--<option value="0" selected="selected"> 请选择 </option>--%>
								<%--<option value="1">天威-不屈白银</option>--%>
								<%--<option value="2">天威-荣耀黄金</option>--%>
								<%--<option value="3">天威-华贵铂金</option>--%>
								<%--<option value="4">天威-璀璨钻石</option>--%>
								<%--<option value="5">天威-最强王者</option>--%>
							<%--</select>--%>
							<%--<input type="button" class="btn btn-danger" value="今日报名" onclick="return EmigratedApply();" >--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label class="ui-label">闯关奖金余额：</label>--%>
							<%--<span id="emigratedMoney" class="red ml10" > <img src="${ctx}/images/waiting.gif"/></span>--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label class="ui-label">目标账户：</label>--%>
							<%--<select id="emigratedType" name="emigratedType"  class="ui-ipt" >--%>
								<%--<option value="0" selected="selected"> 请选择 </option>--%>
								<%--<option value="pt"> PT账户 </option>--%>
								<%--<option value="ttg"> TTG账户 </option>--%>
								<%--<option value="nt"> NT账户 </option>--%>
								<%--<option value="qt"> QT账户 </option>--%>
							<%--</select>--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label class="ui-label" >转账金额：</label>--%>
							<%--<input  type="text" id="emigratedRemit" maxlength="10"  class="ui-ipt" />--%>
						<%--</div>--%>
						<%--<div class="ui-form-item">--%>
							<%--<label class="ui-label"></label>--%>
							<%--<input type="button" class="btn btn-danger" value="提交" onclick="return submitEmigratedRemit();"/>--%>
							<%--<input type="button" class="btn btn-danger" value="领取昨日奖励" onclick="return doEmigrated();"/>--%>
						<%--</div>--%>
					<%--</div>--%>

					<%--<div class="prompt-info">--%>
						<%--<h3 class="tit">活动规则</h3>--%>

						<%--<p>闯关分为5个等级。当天存款100及以上就可以选择当日的任务级别进行报名。</p>--%>

						<%--<p>每个平台达到相应流水就可以领取奖金，全部通关再送通关奖金，领取以后会在闯关奖金账户，满10元可以转到任意的平台，需20倍流水。</p>--%>

						<%--<p>奖金与其他活动共享，当天达到条件后，次日10:00-24:00之前请到每日任务-天威闯关领取，逾期不能领取</p>--%>


					<%--</div>--%>
				<%--</div>--%>
			</div>
		</div>
    </div>
</div>

<div class="modal fade" id="j-modal-rescue" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-hd">
        <h2 class="modal-title">选择老虎机平台</h2>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-bd">
        <input type="hidden" class="j-hd-id"/><input type="hidden" class="j-hd-url"/>
        <div class="ui-form">
			<div class="ui-form-item" style="padding-left:50px;">
				<label for="" class="ui-label" style="width:100%;text-align:center;margin-left: 0;" >请选择您喜欢的老虎机平台, 确定后我们不接受任何重新转至其他老虎机平台的申请</label>
				&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="pttiger" checked="checked">PT老虎机
				&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="slot" checked="checked">老虎机钱包救援金
				&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="ttg">TTG老虎机
<!-- 				&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="nt">NT老虎机 -->
<!-- 				&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="qt">QT老虎机 -->
<!-- 				&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="mg">MG老虎机 -->
				<%--&nbsp;&nbsp;&nbsp;<input type="radio" name="targetRescuePlatform" value="dt">DT老虎机--%>
			</div>
			<div class="ui-form-item">
				<input type="button" class="btn btn-danger j-btn-apply" value="确定">
			</div>
		</div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<!-- <script type='text/javascript' src='http://js.touclick.com/js.touclick?b=0304e3d8-6d75-4bce-946a-06ada1cc5f4e&pf=api&v=v2-2' async></script> -->
<!-- <script type='text/javascript' src='https://cdnjs.touclick.com/0304e3d8-6d75-4bce-946a-06ada1cc5f4e.js' async></script> -->
<script src="//js.touclick.com/js.touclick?b=68aca137-f3c5-457b-87a4-8a46880b1e66" ></script>
<script type="text/javascript" src="${ctx}/js/manageCoupons.js?v=11"></script>
<script type="text/javascript" src="${ctx}/js/self.js"></script>
<script src="${ctx}/js/lib/ZeroClipboard.min.js"></script>
</body>
</html>
