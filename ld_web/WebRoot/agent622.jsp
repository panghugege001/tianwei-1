<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@page import="dfh.model.Users"%> 
<%@page import="dfh.utils.Constants"%>
<!DOCTYPE html>
<html>
<head>
    <%
        HttpSession chksession=request.getSession(true);
        Users user =(Users)chksession.getValue("customer");
        if(user == null){
    %>
    <script language="javascript">
        alert("你的登录已过期，请从首页重新登录");
    </script>
    <%
            response.sendRedirect("index.asp");
        }
    %>

    <meta name="renderer" content="webkit">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <base href="<%=request.getRequestURL()%>" />
    <s:include value="/title.jsp"></s:include>
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />

    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/user.css?v=6"/>
    <link rel="stylesheet" href="${ctx}/css/agentUser.css?v=12"/>
    <script type="text/javascript">
        if(top.location != self.location){
            top.location = self.location;
        }
    </script>
<style>
	.zhanghu{height: 70px !important;background: none !important;margin-bottom: 10px !important;}
	.zhanghu a{display: inline-block;background: none !important;}
	.ui-ipt{width: 350px; height: 35px; line-height: 35px; border-radius: initial;box-shadow: none;}
</style>
</head>
<body class="agentUser_body">

<div class="index-bg agent-page">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
	<div class="daili_bannar"></div>
    <div class="container">
    	<div class="daili_header">
    		<div class="daili_w_25">
    			<div class="daili_user">
                        <c:if test="${session.AGENTVIP ==null || session.AGENTVIP eq '0'}">
                            <span>代理</span>
                        </c:if>
                        <c:if test="${session.AGENTVIP!=null && session.AGENTVIP eq '1'}">
                            <span>VIP代理</span>
                        </c:if>  				
    			</div>
    			<div class="daili_btnan">
    				<a class="daili_geren" data-toggle="tab" href="#tab-personal" aria-expanded="false">个人中心</a>
    				<a class="daili_qukuang" data-toggle="tab" href="#tab-tk" aria-expanded="false">我要取款</a>			
    			</div>
    			<div class="daili_foot">
    				<ul>
    					<li>
    						<label class="datatype"></label>
    						<span class="daili_name">${customer.loginname }</span>
			    			<input type="hidden" value="${session.slotAccount}" class="laohuji" />
			    			<input type="hidden" value="${session.slotAccount}" class="qita" />    						
    						<span>欢迎回来</span>
    					</li>
    					<li>
    						<p>
    							上次登入时间：<span class="user_out_time"></span>
    						</p>
    					</li>
    				</ul>
    			</div>
    		</div>
    		<div class="daili_w_75">
  
    			<ul class="daili_listui">
    				<li>
    					<div class="daili_tiele">
							<a data-toggle="tab" href="#tab-collect">
	    						<i><img src="images/agent/daili_yue.png"></i>
	    						<span>账户余额</span>
	    						<span class="jia_icon">+</span>								
							</a>
    					</div>
    					<div class="daili_list1">
    						<ul>
    							<li>
    								<span>老虎机余额</span>
    								<span class="c-huangse">${session.slotAccount}</span>
    								<span>元</span> 								
    							</li>
    							<li>
    								<span>真人余额</span>
    								<span class="c-huangse">${session.customer.credit}</span>
    								<span>元</span>
    							</li>
    						</ul>
    					</div>
    				</li>
    				<li>
    					<div class="daili_tiele">
							<a data-toggle="tab" href="#tab-collect">
	    						<i><img src="images/agent/daili_shuju.png"></i>
	    						<span>本月数据</span>
		    					<span class="jia_icon">+</span>	  								
							</a>  						
    					</div>
    					<div class="daili_list2">
    						<ul>
    							<li>
    								<span>总输赢</span>
    								<span class="c-huangse">${report.profitall==null?0:report.profitall }</span>
    								<span>元</span>
    							</li>
    							<li>
    								<span>总反水</span>
    								<span class="c-huangse">${report.ximafee==null?0:report.ximafee }</span>
    								<span>元</span>
    							</li>
    							<li>
    								<span>总优惠</span>
    								<span class="c-huangse">${report.couponfee==null?0:report.couponfee }</span>
    								<span>元</span>
    							</li>
    						</ul>    						
    					</div>    					
    				</li>
    				<li>
    					<div class="daili_tiele">
							<a data-toggle="tab" href="#tab-collect">
	    						<i><img src="images/agent/daili_lianjie.png"></i>
	    						<span>推广链接</span>
	    						<span class="jia_icon">+</span>									
							</a>
    					</div>
    					<div class="daili_list3">
    						
    					</div>    					
    				</li>    				
    			</ul>
    		</div>
    	</div>
        <div class="cfx about-main" id="daili_dody">
            <div class="gb-sidenav">
                <div class="user-info mb20 mt20" style="display: none;">
                    <p class="user-name mb">
                        用 户 名：<span class="cee6">${customer.loginname }</span>
                    </p>
                    <input type="button"/>
                    <p class="mb20">代理等级：<em class="cee6">
                        <c:if test="${session.AGENTVIP ==null || session.AGENTVIP eq '0'}">
                            <span>代理</span>
                        </c:if>
                        <c:if test="${session.AGENTVIP!=null && session.AGENTVIP eq '1'}">
                            <span>VIP代理</span>
                        </c:if>
                    </em></p>
                    <div class="agent-btn-toolbar text-center">
                        <a data-toggle="tab" href="#tab-personal" class="btn sm btn-edit">编辑资料</a>
                        <a href="javascript:;" onclick="return logout();" class="btn sm btn-logout">退出</a>
                    </div>
                </div>
                <ul class="navlist daili_mull" id="j-userNav">
                	<li class="zhanghu"><a data-toggle="tab" href="#tab-personal"><img src="images/zhanghuguanli.png"></a></li>
                	<li class="active"><i class="daili_dh daili_ml5"></i><a data-toggle="tab" href="#tab-collect">数据汇总</a></li>
                	<li><i class="daili_dh daili_ml7"></i><a data-toggle="tab" href="#tab-personal">个人中心</a></li>
                	<li><i class="daili_dh daili_ml9"></i><a data-toggle="tab" href="#tab-tk">我要提款</a></li>
                	<li><i class="daili_dh daili_ml8"></i><a data-toggle="tab" href="#tab-letter" onclick="letterService(1);">站内信</a></li>
                	<li class="zhanghu"><a data-toggle="tab" href="#tab-collect"><img src="images/shujuguanli.png"></a></li>
                    <li><i class="daili_dh daili_ml1"></i><a onclick="ptCommissionsRecord(1);" data-toggle="tab" href="#tab-report">佣金报表</a></li>
                    <li><i class="daili_dh daili_ml4"></i><a onclick="agentAmountRecord();" data-toggle="tab" href="#tab-limit">额度记录</a></li>
                    <li><i class="daili_dh daili_ml2"></i><a onclick="agentOfflineUserRecord();" data-toggle="tab" href="#tab-user-list">会员列表</a></li>
                    <li><i class="daili_dh daili_ml3"></i><a onclick="agentOfflineRecord();" data-toggle="tab" href="#tab-finance">会员账务</a></li>
            <!--        <li><i class="daili_dh daili_ml6"></i><a data-toggle="tab" href="#tab-vip">VIP中心</a></li>-->
          
          

                <%--<li><a data-toggle="tab" href="#tab-transfer" onclick="queryGameUser();">游戏转账</a></li>--%>
                </ul>
            </div>
            <div class="gb-main-r tab-bd user-main">

                <div id="tab-collect" class="tab-panel user-tab-box active">
                	<h1 class="tab-tit"><span class="cee6">数据汇总</span></h1>
                    <div class="box-border mb20">
						<ul class="user-nav tab-nav">
	                        <li class="active"><a href="#tab-collect" data-toggle="tab" aria-expanded="true">数据汇总</a></li>
	                    </ul>                        
                        <!--<table class="table data-table">
                            <thead>
                            <tr>
                                <th>本月总输赢（元）</th>
                                <th>本月总返水（元）</th>
                                <th>本月总优惠（元）</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="num c-red">${report.profitall==null?0:report.profitall }</td>
                                <td class="num c-strong">${report.ximafee==null?0:report.ximafee }</td>
                                <td class="num c-blue">${report.couponfee==null?0:report.couponfee }</td>
                            </tr>
                            </tbody>
                            <tr>
                                <td> <p>${report.reg==null?0:report.reg }</p>
                                    <p>会员总人数</p></td>
                                <td>
                                    <p>${report.monthly_reg==null?0:report.monthly_reg }</p>
                                    <p>本月注册量</p>
                                </td>
                                <td>
                                    <p>${report.betall==null?0:report.betall }</p>
                                    <p>本月投注额</p>
                                </td>
                            </tr>
                        </table>-->
                        
                        <div class="dl_table_box">
                        	<div class="w_266">
                        		<div class="w_266_title">帐户余额</div>
								<div class="w_266_box">
	                        		<ul>
		    							<li>
		    								<span>老虎机余额</span>
		    								<span class="c-huangse">${session.slotAccount}</span>
		    								<span>元</span>
		    							</li>
		    							<li>
		    								<span>真人余额</span>
		    								<span class="c-huangse">${session.customer.credit}</span>
		    								<span>元</span>
		    							</li>
	                        		</ul>									
								</div>
                        	</div>
                        	<div class="w_552">
                        		<div class="w_552_title">数据统计</div>
                        		<div class="w_552_box">
									<div class="w_552_box_left">
			    						<ul>
			    							<li>
			    								<span>本月总输赢</span>
			    								<span class="c-huangse">${report.profitall==null?0:report.profitall }</span>
			    								<span>元</span>
			    							</li>
			    							<li>
			    								<span>本月总反水</span>
			    								<span class="c-huangse">${report.ximafee==null?0:report.ximafee }</span>
			    								<span>元</span>
			    							</li>
			    							<li>
			    								<span>本月总优惠</span>
			    								<span class="c-huangse">${report.couponfee==null?0:report.couponfee }</span>
			    								<span>元</span>
			    							</li>
			    						</ul> 
									</div>
									<div class="w_552_box_right">
			    						<ul>
			    							<li>
			    								<span>会员总人数</span>
			    								<span class="c-huangse">${report.reg==null?0:report.reg }</span>
			    								<span>人</span>
			    							</li>
			    							<li>
			    								<span>本月注册量</span>
			    								<span class="c-huangse">${report.monthly_reg==null?0:report.monthly_reg }</span>
			    								<span>人</span>
			    							</li>
			    							<li>
			    								<span>本月投注额</span>
			    								<span class="c-huangse">${report.betall==null?0:report.betall }</span>
			    								<span>元</span>
			    							</li>
			    						</ul> 										
									</div>
                        		</div>
                        	</div>
                        </div>
                        <div class="daili_like_box">
                        	<div class="daili_like_box_tlet">推广链接</div>
                        	<div class="daili_like"></div>
                        </div>
                        <div class="daili_vip">
                        	<table cellpadding="0" cellspacing="0">
                        		<tr>
                        			<th style="border-top-left-radius: 15px;">代理等级</th>
                        			<th>累计佣金</th>
                        			<th style="border-top-right-radius: 15px;">代理福利</th>
                        		</tr>
                        		<tr>
                        			<td>普通代理</td>
                        			<td>3000</td>
                        			<td>生日礼金188</td>
                        		</tr>
                        		<tr>
                        			<td>尊贵VIP</td>
                        			<td>50.000—150.000</td>
                        			<td>生日礼金888</td>
                        		</tr>  
                        		<tr>
                        			<td>特级VIP</td>
                        			<td>150.001—1.000.000</td>
                        			<td>生日礼金1888</td>
                        		</tr>     
                        		<tr>
                        			<td>特邀合伙人</td>
                        			<td>佣金累积100万</td>
                        			<td>生日礼金8888</td>
                        		</tr>                         		
                        	</table>
                        </div>
                    </div>

                    <div class="user-tab-box">
                        <div class="m-content">
                            <p>温馨提示</p>
                            <ol>
                                <li>当月汇总时间段是按照代理下线会员上月最后一天到当月倒数第二天，比如8月份的数据时间段为7月31日00:00—8月30日23:59.
                                </li>
                                <li>每天下午日结佣金结束之后数据会更新。</li>
                                <li>数据可能会出现延迟，如果出现误差，请联系市场专员，以正式后台为准。</li>
                            </ol>
                        </div>
                    </div>
                </div>
                <div id="tab-user-list" class="tab-panel user-tab-box">
					<ul class="user-nav tab-nav">
	                    <li class="active"><a href="javascript:;" data-toggle="tab" aria-expanded="true">会员报表</a></li>
	                </ul>
                    <div class="ipt-group ipt-group-md mt72">
                        <label for="" class="label">开始时间</label>
                        <input type="text" class="ipt-txt" id="offlineUserStarttime" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" />

                        <label for="" class="label">结束时间</label>
                        <input type="text" class="ipt-txt" id="offlineUserEndtime" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false" />

                        <input type="button" class="btn btn-danger" value="搜索" onclick="return agentOfflineUserRecordTwo(1);"/>

                    </div>
                    <div class="list" id="offlineUserRecordDiv"></div>
                </div>
                <div id="tab-finance" class="tab-panel user-tab-box">
                	<h1 class="tab-tit"><span class="cee6">会员账务</span></h1>
                    <ul class="user-nav tab-nav">
                        <li class="active"><a onclick="agentOfflineRecord();" href="#tab-deposit" data-toggle="tab">会员存款</a></li>
                        <li><a onclick="agentOfflineRecordtk();" href="#tab-withdraw" data-toggle="tab">会员提款</a></li>
                        <li><a onclick="agentOfflineRecordfs();" href="#tab-return" data-toggle="tab">会员返水</a></li>
                        <li><a onclick="agentOfflineRecordyh();" href="#tab-coupons" data-toggle="tab">会员优惠</a></li>
                        <li><a onclick="agentPlatformRecord();" href="#tab-win" data-toggle="tab">会员输赢</a></li>
                    </ul>
                    <div class="tab-bd">
                        <div id="tab-deposit" class="tab-panel active">
                            <div class="ipt-group ipt-group-md pt72">
                                <label for="" class="label">开始时间</label>
                                <input type="text" class="ipt-txt" id="offlineStarttime" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                                <label for="" class="label">结束时间</label>
                                <input type="text" class="ipt-txt" id="offlineEndtime" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                            </div>
                            <div class="ipt-group ipt-group-md">
                                <label for="" class="label">会员帐号</label>
                                <input type="text" class="ipt-txt" id="offlineUsername" maxlength="15"/>
                                <label for="" class="label">存款类型</label>
                                <select id="offlineProposalType" class="ipt-txt"  style="width: 158px;" >
                                    <s:iterator value="%{#application.ProposalType}" var="pt">
                                        <s:if test="#pt.code == 502 || #pt.code == 1000">
                                            <option
                                                    <s:if test="#pt.code==502 && #request.proposalType==null">selected=selected</s:if>
                                                    <s:elseif test="#pt.code==#request.proposalType">selected=selected</s:elseif>
                                                    value=<s:property value="#pt.code"/>>
                                                <s:property value="#pt.text" />
                                            </option>
                                        </s:if>
                                    </s:iterator>
                                </select>

                                <input type="button" class="btn btn-danger" value="搜索" onclick="return agentOfflineRecordTwo(1);"/>

                            </div>
                            <div id="offlineRecordDiv">
    
                            </div>
                        </div>
                        <div id="tab-withdraw" class="tab-panel">
                            <div class="ipt-group ipt-group-md pt72">
                                <label for="" class="label">开始时间</label>
                                <input type="text" class="ipt-txt" id="offlineStarttimetk" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                                <label for="" class="label">结束时间</label>
                                <input type="text" class="ipt-txt" id="offlineEndtimetk" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
                            </div>
                            <div class="ipt-group ipt-group-md">
                                <label for="" class="label">会员帐号</label>
                                <input type="text" class="ipt-txt" id="offlineUsernametk" maxlength="15"/>
                                <input type="hidden" id="offlineProposalTypetk" class="input" value="503" />
                                <input type="button" class="btn btn-danger" value="搜索" onclick="return agentOfflineRecordTwotk(1);"/>

                            </div>
                            <div id="offlineRecordDivtk">
    
                            </div>
                        </div>
                        <div id="tab-return" class="tab-panel ">
                            <div class="ipt-group ipt-group-md pt72">
                                <label for="" class="label">开始时间</label>
                                <input type="text" class="ipt-txt" id="offlineStarttimefs" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
                                <label for="" class="label">结束时间</label>
                                <input type="text" class="ipt-txt" id="offlineEndtimefs" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
                            </div>
                            <div class="ipt-group ipt-group-md">
                                <label for="" class="label">会员帐号</label>
                                <input type="text" class="ipt-txt" id="offlineUsernamefs" maxlength="15"/>
                                <label for="" class="label">返水类型</label>
                                <select id="offlineProposalTypefs" class="ipt-txt"  style="width: 158px;" >
                                    <s:iterator value="%{#application.ProposalType}" var="pt">
                                        <s:if test="#pt.code == 507 || #pt.code == 517 || #pt.code == 611 || #pt.code == 612 || #pt.code == 613 || #pt.code == 614 || #pt.code == 615 || #pt.code == 616 || #pt.code == 618 || #pt.code == 619
											|| #pt.code == 620 || #pt.code == 621 || #pt.code == 622 || #pt.code == 623 || #pt.code == 624 || #pt.code == 625 || #pt.code == 561">
                                            <option
                                                    <s:if test="#pt.code==507 && #request.proposalType==null">selected=selected</s:if>
                                                    <s:elseif test="#pt.code==#request.proposalType">selected=selected</s:elseif>
                                                    value=<s:property value="#pt.code"/>>
                                                <s:property value="#pt.text" />
                                            </option>
                                        </s:if>
                                    </s:iterator>
                                </select>

                                <input type="button" class="btn btn-danger" value="搜索" onclick="return agentOfflineRecordTwofs(1);"/>


                            </div>
                            <div id="offlineRecordDivfs">

                            </div>
                        </div>
                        <div id="tab-coupons" class="tab-panel">
                            <div class="ipt-group ipt-group-md pt72">
                                <label for="" class="label">开始时间</label>
                                <input type="text" class="ipt-txt" id="offlineStarttimeyh" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
                                <label for="" class="label">结束时间</label>
                                <input type="text" class="ipt-txt" id="offlineEndtimeyh" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                            </div>
                            <div class="ipt-group ipt-group-md">
                                <label for="" class="label">会员帐号</label>
                                <input type="text" class="ipt-txt" id="offlineUsernameyh" maxlength="15"/>

                                <label for="" class="label">优惠类型</label>
                                <select id="offlineProposalTypeyh" class="ipt-txt"  style="width: 158px;" >
                                    <s:iterator value="%{#application.ProposalType}" var="pt">
                                        <s:if test="#pt.code == 506 || #pt.code == 509 || #pt.code == 701 || #pt.code == 518 || #pt.code == 519 || #pt.code == 537 || #pt.code == 590 || #pt.code == 591 || #pt.code == 592 || #pt.code == 593 || #pt.code == 594 || #pt.code == 595 || #pt.code == 531 || #pt.code == 532 || #pt.code == 533 || #pt.code == 534 || #pt.code == 535
											|| #pt.code == 513 || #pt.code == 596 || #pt.code == 597 || #pt.code == 598 || #pt.code == 599 || #pt.code == 702 || #pt.code == 703 || #pt.code == 555
											|| #pt.code == 705 || #pt.code == 706 || #pt.code == 707 || #pt.code == 708 || #pt.code == 709 || #pt.code == 710 || #pt.code == 711 || #pt.code == 712 || #pt.code == 420 || #pt.code == 421">
                                            <option
                                                    <s:if test="#pt.code==505 && #request.proposalType==null">selected=selected</s:if>
                                                    <s:elseif test="#pt.code==#request.proposalType">selected=selected</s:elseif>
                                                    value=<s:property value="#pt.code"/>>
                                                <s:property value="#pt.text" />
                                            </option>
                                        </s:if>
                                    </s:iterator>
                                </select>
                                <input type="button" class="btn btn-danger" value="搜索" onclick="return agentOfflineRecordTwoyh(1);"/>

                            </div>
                            <div id="offlineRecordDivyh">

                            </div>
                        </div>
                        <div id="tab-win" class="tab-panel user-tab-box" style="padding-top: 40px;">
                            <div class="ipt-group ipt-group-md">
                                <label for="" class="label">开始时间</label>
                                <input type="text" class="ipt-txt" id="platformStarttime" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                                <label for="" class="label">结束时间</label>
                                <input type="text" class="ipt-txt" id="platformEndtime" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                            </div>
                            <div class="ipt-group row ipt-group-md">
                                <label for="" class="label">会员帐号</label>
                                <input type="text" class="ipt-txt" id="platformUsername" maxlength="15"/>

                                <label for="" class="label">平台类型</label>
                                <select id="platformPlatform" class="ipt-txt">
                                    <option value="">
                                        全部
                                    </option>
                                    <option value="ea">
                                        EA
                                    </option>
                                    <option value="bbin">
                                        BBIN
                                    </option>
                                    <option value="keno">
                                        KENO
                                    </option>
                                    <option value="sb">
                                        SB
                                    </option>
                                    <%-- <option value="sixlottery">
                                       六合彩
                                   </option>--%>
                                    <option value="newpt">
                                        PT
                                    </option>
                                    <option value="ttg">
                                        TTG
                                    </option>
                                    <option value="qt">QT</option>
                                    <option value="nt">NT</option>
                                    <%--<option value="gpi">
                                        GPI
                                    </option>--%>
                                    <option value="ebet">
                                        EBET
                                    </option>
                                    <option value="jc">
                                        时时彩
                                    </option>
                                </select>
                                <input type="button" class="btn btn-danger" value="搜索" onclick="return agentPlatformRecordTwo(1);"/>

                            </div>
                            <div id="platformRecordDiv">
                                <!-- <table>
                                    <tbody>
                                    <tr>
                                        <th>序号</th>
                                        <th>会员账号</th>
                                        <th>更新时间</th>
                                        <th>会员投注额</th>
                                        <th>平台输赢</th>
                                        <th>会员返水</th>
                                        <th>会员优惠</th>
                                        <th>平台类型</th>
                                    </tr>
                                    <tr>
                                        <td>1</td>
                                        <td>1</td>
                                        <td>1</td>
                                        <td>1</td>
                                        <td>1</td>
                                        <td>1</td>
                                        <td>1</td>
                                    </tr>
                                    </tbody>
                                </table> -->
                            </div>
                        </div>
                    </div>

                </div>
                <div id="tab-report" class="tab-panel user-tab-box">
					<ul class="user-nav tab-nav">
	                    <li class="active"><a href="javascript:;" data-toggle="tab" aria-expanded="true">佣金报表</a></li>
	                </ul>
                    <div class="ipt-group ipt-group-md mt72">
                        <label for="" class="label">开始时间</label>
                        <input type="text" class="ipt-txt" id="startptCommDate" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})" My97Mark="false"/>
                        <label for="" class="label">结束时间</label>
                        <input type="text" class="ipt-txt" id="endptCommDate" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>

                        <input type="button" class="btn btn-danger" value="搜索" onclick="ptCommissionsRecord(1);"/>
                    </div>
                    <div class="ipt-group  ipt-group-md">
                        <label for="" class="label">提款类型</label>
                        <select name="" class="ipt-txt">
                            <option value="">老虎机佣金</option>
                            <option value="">其他平台佣金</option>
                        </select>
                        <a class="btn btn-danger" data-toggle="tab" href="#tab-tk">提款</a>

                    </div>
                    <div id="agentPtCommissionRecordDiv">
                        <!-- <table>
                            <tbody>
                            <tr>
                                <th>日期</th>
                                <th>平台输赢</th>
                                <th>返水</th>
                                <th>优惠</th>
                                <th>佣金</th>
                                <th>提款状态</th>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                            </tr>
                            </tbody>
                        </table> -->
                    </div>
                    <div class="m-content">
                        <h2>佣金日结提款标准</h2>
                        <ul>
                            <li class="c-strong">重要提示：禁止合作加盟本人和下线玩家同IP，或者注册游戏账号到自己的下线，一经发现立刻永久关闭代理账号，佣金不予发放。
                            </li>
                        </ul>
                    </div>
                </div>

                <div id="tab-tk" class="tab-panel user-tab-box">
                   <ul class="user-nav tab-nav">
                        <li class="active"><a href="#tikuang" data-toggle="tab" aria-expanded="true">我要提款</a></li>
                        <li><a href="#tab-lock" data-toggle="tab" aria-expanded="true">银行卡绑定</a></li>
                    </ul>
					<div class="tab-bd">
						<div id="tikuang" class="tab-panel user-tab-box active">
		                    <form method="post" name="form5" class="ui-form">
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">老虎机佣金余额：</label>
		                            <span class="c-red">${session.slotAccount}</span>
		                        </div>
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">其他佣金余额：</label>
		                            <span class="c-red">${session.customer.credit}</span>
		                        </div>
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">密码：</label>
		                            <input type="password"  maxlength="15" id="tkPassword" class="ui-ipt" />
		                        </div>
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">银行名称：</label>
		                            <select id="tkBank" name="bank" class="ui-ipt" onchange="getbankno(this.value)">
		                                <option value="">请选择银行</option>
		                                <option value="工商银行">工商银行</option>
		                                <option value="招商银行">招商银行</option>
		                                <!-- <option value="城市商业银行">城市商业银行</option>
		                                    <option value="农村商业银行">农村商业银行</option> -->
		                                <option value="上海农村商业银行">上海农村商业银行</option>
		                                <option value="农业银行">农业银行</option>
		                                <option value="建设银行">建设银行</option>
		                                <option value="邮政银行">邮政银行</option>
		                                <option value="交通银行">交通银行</option>
		                                <option value="民生银行">民生银行</option>
		                                <option value="光大银行">光大银行</option>
		                                <option value="兴业银行">兴业银行</option>
		                                <option value="上海浦东银行">上海浦东银行</option>
		                                <option value="广东发展银行">广东发展银行</option>
		                                <option value="深圳发展银行">深圳发展银行</option>
		                                <option value="中国银行">中国银行</option>
		                                <option value="中信银行">中信银行</option>
		                            </select>
		                            <a class="c-red" data-toggle="tab" href="#tab-lock"> 绑定卡/折号？</a>
		                        </div>
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">卡/折号：</label>
		                            <input type="text" id="tkAccountNo" class="ui-ipt" maxlength="20"/>
		                        </div>
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">提款类型：</label>
		                            <select id="tkType"  class="ui-ipt" >
		                                <option value="slotmachine">老虎机</option>
		                                <option value="liveall">其他</option>
		                            </select>
		                            <input  type="hidden" id="tkBankAddress" />
		                        </div>
		                        <div class="ui-form-item">
		                            <label for="" class="ui-label">提款金额：</label>
		                            <input type="text" maxlength="6" id="tkAmount" name="tkAmount"  class="ui-ipt" />
		                        </div>
                                <p id="tkTip" style="color:crimson;"></p>
		                        <div class="ui-form-item">
		                            <input type="checkbox" name="tkAgree" id="tkAgree" class="check" />　我已读过&lt;<a href="/aboutus.jsp#tab-agreement" target="_blank" class="c-red">提款须知</a>&gt;，并已清楚了解其规则。
		                        </div>
		                        <div class="ui-form-item">
		                            <input type="button" class="btn btn-pay" value="提交" onclick="return tkWithdrawal();"/>
		                            <input type="reset" class="btn btn-pay" value="重置" onclick="clearTkWithdrawal();"/>
		                        </div>
		                    </form>
		                    <div class="m-content">
		                    	<p>【温馨提示】</p>
		                        <p class="c-red">提款后风控审核，请合作伙伴耐心等待，12小时内到账</p>
		
		                    </div>						
						</div>
						
                <div id="tab-lock" class="tab-panel user-tab-box">
                    <h2 class="tab-tit">绑定银行卡/折号</h2>

                    <form method="post" class="ui-form">
                        <div class="ui-form-item">
                            <label for="" class="ui-label">银行账户：</label>
                            <select class="ui-ipt"  id="bdbank">
                                <option value="">请选择银行</option>
                                <s:iterator value="%{#application.IssuingBankEnum}" var="bk">
                                    <s:if test="#bk.issuingBank != '支付宝'">
                                        <option value=<s:property value="#bk.issuingBankCode"/>>
                                            <s:property value="#bk.issuingBank" />
                                        </option>
                                    </s:if>
                                </s:iterator>
                            </select>
                        </div>
                        <div class="ui-form-item">
                            <label for="" class="ui-label">卡/折号：</label>
                            <input type="text" id="bdbankno" maxlength="30"  class="ui-ipt" />
                        </div>
                        <div class="ui-form-item">
                            <label for="" class="ui-label">登录密码：</label>
                            <input type="password" id="bdpassword" class="ui-ipt" maxlength="15" />
                        </div>
                        <div class="ui-form-item">
                            <input type="button" class="btn btn-danger" value="提交"  onclick="return checkbandingform();"/>
                            <input type="reset" class="btn btn-danger" value="重置"  onclick="clearBandingform();"/>
                        </div>
                    </form>
                    <div class="m-content">
                        <p>绑定银行卡/折号，可以免去您重复输入卡/折号的繁琐步骤 </p>
                        <p class="c-red">注：只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。银行卡号绑定位数为15到20位。</p>
                        <%--<p class="c-red">注：只可以绑定三个银行卡/折号，且每个银行只可绑定一个卡号。如须解绑，请与在线客服联系。银行卡号和支付宝帐号绑定位数为10到30位。</p>--%>
                        <%--<p>支付宝为独立的第三方支付系统，不同于传统的现金流，不受银行监控，您的游戏资金更加安全有保障</p>--%>
                    </div>
                </div>						
						
					</div>
                </div>


                <div id="tab-limit" class="tab-panel user-tab-box">
					<ul class="user-nav tab-nav">
	                    <li class="active"><a href="javascript:;" data-toggle="tab" aria-expanded="true">额度记录</a></li>
	                </ul>
                   <div class="ipt-group ipt-group-md mt72">
                       <label class="label">起点时间：</label><input type="text" id="amountStarttime" class="ipt-txt" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
                       <label class="label">结束时间：</label><input type="text" id="amountEndtime" class="ipt-txt" readonly onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" My97Mark="false"/>
                       <input type="submit" class="btn btn-danger" value="提交查询" onclick="return agentAmountRecordTwo(1);"/>
                   </div>
                    <div id="amountRecordDiv"></div>

                </div>
                <div id="tab-vip" class="tab-panel user-tab-box">
                   <h1 class="tab-tit"><span class="cee6">VIP中心</span></h1>
                    <table class="table" style="margin-top:72px;">
                        <tbody>
                        <tr>
                            <th>代理等级</th>
                            <th>累计佣金</th>
                            <th>代理福利</th>
                        </tr>
                        <tr>
                            <td>普通代理</td>
                            <td>3000</td>
                            <td>生日礼金188</td>
                        </tr>
                        <tr>
                            <td>尊贵VIP</td>
                            <td>50.000—150.000</td>
                            <td>生日礼金888</td></tr>
                        <tr>
                            <td>特级VIP</td>
                            <td>150.001—1.000.000</td>
                            <td>生日礼金1888</td>
                        </tr>
                        <tr>
                            <td>特邀合伙人</td>
                            <td>佣金累积100万</td>
                            <td>生日礼金8888</td>
                        </tr>
                        </tbody>
                    </table>

                    <div class="m-content">
                        <ol>
                            <li>生日礼金，以合作代理身份证出生日期为准。</li>
                            <li>累计佣金每年3月份清零，重新累计。</li>
                            <li>尊贵VIP代理以上级别如果连续两月未发展一位有效客户，代理将不享有礼品。</li>
                            <li>生日礼金需代理生日当天联系代理专员申请，如未按时申请，视为代理自动放弃领取！</li>
                        </ol>
                    </div>
                </div>
                <div id="tab-notice" class="tab-panel user-tab-box">
                    <table class="table">
                        <tbody>
                        <tr>
                            <th>消息类型</th>
                            <th width="45%">主题</th>
                            <th>消息发送时间</th>
                            <th>读取状态</th>
                        </tr>
                        <tr>
                            <td>公告</td>
                            <td><a href="#">如何正确发展会员方式</a></td>
                            <td>2015-06-20  20:30:59</td>
                            <td>已读</td>
                        </tr>
                        <tr>
                            <td>公告</td>
                            <td><a href="#">6月最新优惠活动</a></td>
                            <td>2015-06-03  09:20:10</td>
                            <td>未读</td>
                        </tr>
                        <tr>
                            <td>公告</td>
                            <td><a href="#">6月份佣金结算流程以及公式</a></td>
                            <td>2015-06-01  10:35:09</td>
                            <td>已读</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div id="tab-personal" class="tab-panel user-tab-box">
                	<h1 class="tab-tit"><span class="cee6">个人中心</span></h1>
                    <ul class="user-nav tab-nav">
                        <li class="active"><a href="#tab-personal-data" data-toggle="tab" aria-expanded="true">个人资料</a></li>
                        <li><a href="#tab-password" data-toggle="tab" aria-expanded="true">密码修改</a></li>
                    </ul>
                    <div class="tab-bd">
                        <div id="tab-personal-data" class="tab-panel user-tab-box active">
                            <div class="ui-form">
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">代理账号</label>
                                    ${session.customer.loginname}
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">邮箱地址</label>
                                    ${session.customer.email}
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">真实姓名</label>
                                    ${session.customer.accountName}
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">手机号码</label>
                                    ${session.customer.phone}
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">QQ</label>
                                    <input type="text" class="ui-ipt" id="updateQq" value="<s:property value="'*******'+@dfh.utils.StringUtil@subStrLast(#session.customer.qq, 3)" />" maxlength="15">
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">微信号</label>
                                    <input type="text" class="ui-ipt" id="updateWechat" value="<s:property value="'*******'+@dfh.utils.StringUtil@subStrLast(#session.customer.microchannel, 3)" />" maxlength="15">
                                </div>
                                <%-- <div class="ui-form-item">
                                    <label for="" class="ui-label">邮寄地址</label>
                                    <textarea name="address" class="text"  id="updateMailaddress" value="${session.customer.mailaddress}" />${session.customer.mailaddress}</textarea>
                                </div> --%>
                                <!-- <div class="ui-form-item">
                                    <label for="" class="ui-label">银行类型</label>
                                    <select name="">
                                        <option value=""></option>
                                    </select>
                                    <a href="#">绑定银行卡</a>
                                </div> -->
                                <div class="ui-form-item">
                                    <input type="button" class="btn btn-pay" value="保存" onclick="return updateUser();"/>
                                </div>
                            </div>

                        </div>
                        <div id="tab-password" class="tab-panel user-tab-box">
                            <form method="post" class="ui-form">
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">旧密码</label>
                                    <input type="password" class="ui-ipt" id="updatePassword" maxlength="12">
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">新密码</label>
                                    <input type="password" class="ui-ipt" id="updateNew_password" maxlength="12">
                                </div>
                                <div class="ui-form-item">
                                    <label for="" class="ui-label">确认密码</label>
                                    <input type="password" class="ui-ipt" id="updateSpass2"  maxlength="12">
                                </div>
                                <div class="ui-form-item">
                                    <input type="reset" class="btn btn-pay" value="取消">
                                    <input type="button" class="btn btn-pay" value="保存" onclick="return updateDatePassword();">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div id="tab-transfer" class="tab-panel user-tab-box">
                    <h2 class="tab-tit">转入游戏</h2>
                    <form method="post" name="form5" class="ui-form">
                        <div class="ui-form-item">
                            <label for="" class="ui-label">游戏帐号：</label>
                            <input type="text"  maxlength="15"  class="ui-ipt"  readonly="readonly" id="gameUser"/>
                            <%--<a class="c-blue link" href="javascript:;" data-toggle="modal" data-target="#j-modal-bind">绑定帐号</a>--%>
                        </div>
                        <div class="ui-form-item">
                            <label for="" class="ui-label">转入金额：</label>
                            <input type="text"  maxlength="15" id="remit" name="remit"  class="ui-ipt" />
                        </div>
                        <div class="ui-form-item">
                            <label for="" class="ui-label">代理密码：</label>
                            <input type="password" maxlength="20" id="transPassword" name="transPassword" class="ui-ipt" />
                        </div>
                        <div class="ui-form-item">
                            <input type="button" class="btn btn-danger" value="提交" onclick="transferInGameUser();"/>
                        </div>
                    </form>
                </div>

                <div id="tab-letter" class="tab-panel user-tab-box">
                	<h1 class="tab-tit"><span class="cee6">站内信</span></h1>
                    <ul class="user-nav tab-nav">
                        <li class="active">
                            <a href="#tab-one" data-toggle="tab"><i class="iconfont icon-email"></i>收件箱(<span class="j-letter c-strong">0</span>)</a>
                        </li>
                    </ul>
                    <div class="tab-bd user-tab-box">
                        <div id="tab-one" class="tab-panel active">
                            <ul class="post-list" id="j-letterList"> </ul>
                        </div>
                    </div>
                </div>

                <div id="tab-link" class="tab-panel user-tab-box">
                	<h1 class="tab-tit"><span class="cee6">推广链接</span></h1>
                    <div id="j-url"></div>
                </div>

            </div>
    </div>

</div>



<!--登录{-->
<div class="modal fade" id="j-modal-bind" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-hd">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="clearText();"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">绑定帐号</h4>
            </div>
            <div class="modal-bd">
                <form action="" method="post" >
                    <div class="ipt-group">
                        <label for="" class="label rq-value">游戏帐号：</label>
                        <input type="text" class="ipt-txt"  maxlength="15" id="gameUserToBind"/>
                    </div>
                    <div class="ipt-group">
                        <label for="" class="label rq-value">代理密码：</label>
                        <input type="password" class="ipt-txt" id="agentPassword"/>
                    </div>
                    <div class="ipt-group">
                        <label for="" class="label">&nbsp;</label>
                        <input type="button" class="btn btn-danger" id="j-login-agent" value="确定" onclick="agentBindGameUser();"/>
                    </div>
                </form>

                <div class="m-content">
                    <h3>温馨提示：</h3>
                    <ol>
                        <li>游戏账号必须和代理账号开户名字一致</li>
                        <li>绑定游戏账号主要是为了更好的防止代理套用情况，真正实现日结，代理只要有点佣金就会转入游戏账号进行游戏</li>
                        <li>只针对老虎机佣金，让老虎机佣金日结真正的是日结了，只要有佣金就可以转入游戏账号进行游戏，（转入游戏账号不能低于10元，）对真人暂时保持不变</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</div>
<!--}登录-->

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

    <input id="moneyAccount" type="hidden" value="${session.slotAccount}" data-liveall="${session.customer.credit}" />


    <jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

<s:url value="/scripts/My97DatePicker/WdatePicker.js" var="WdatePickerUrl"></s:url>
<script type="text/javascript" src="${WdatePickerUrl}"></script>
<script src="/js/userLetter.js"></script>
<script>
    $(function(){
    	var lhj=parseInt($(".laohuji").val());
    	var qt=parseInt($(".qita").val());
        function getQueryString(name, url) {
            if (!url) url = window.location.href;
            name = name.replace(/[\[\]]/g, "\\$&");
            var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
                    results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, " "));
        };
        var modalId='#'+getQueryString('showid');
        var $useNav=$('#j-userNav').find('a[href="'+modalId+'"]').first();
        $useNav.trigger('click'); 
        
        $(".daili_money").text(lhj+qt);
        
        function datatype(){
        	var now=new Date();
        	var hour=now.getHours();
			if(hour < 6){$('.datatype').text("凌晨好，")} 
			else if (hour < 9){$('.datatype').text("早上好，")} 
			else if (hour < 12){$('.datatype').text("上午好，")} 
			else if (hour < 14){$('.datatype').text("中午好，")} 
			else if (hour < 17){$('.datatype').text("下午好，")} 
			else if (hour < 19){$('.datatype').text("傍晚好，")} 
			else if (hour < 22){$('.datatype').text("晚上好，")} 
			else {$('.datatype').text("夜间好，")} 
        
        }  
        
	function user_time(){
		$.get('/asp/queryLastLoginDate.aspx',function(data){
			$(".user_out_time").text(data)
		})
	}
	
	
	$.get("/asp/queryAgentAddress.aspx",function(data){
		$(".daili_list3").append(data);
		$(".daili_like").append(data);
	})
	
    user_time()        
        
	datatype();        
        
    });

    //获取银行账号
    function getbankno(bankname){
        if(bankname==''){
            return ;
        }
        openProgressBar();
        $.post("${ctx}/asp/searchBankno.aspx", {
            "bankname":bankname
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                if(returnedData==1){
                    $("#tkAccountNo").val("");
                    $("#tkBankAddress").val("none");
                }else{
                    var recvData=returnedData.split("|||");
                    $("#tkAccountNo").val(recvData[0]);
                    $("#tkBankAddress").val(recvData[1]);
                }
            }
        });
    }



    var maxTkMoney = 0; // 最大可提款
    //提交提款
    function tkWithdrawal(){
        var tkAgree=$("#tkAgree");
        if(!tkAgree.is(":checked")){
            alert("未选中提款须知！");
            return false;
        }
        var tkPassword=$("#tkPassword").val();
        if (tkPassword==''){
            alert("[提示]密码不可为空！");
            return false;
        }
        var tkBank=$("#tkBank").val();
        if(tkBank==""){
            alert("[提示]请选择银行！");
            return false;
        }
        var tkAccountNo=$("#tkAccountNo").val();
        if (tkAccountNo==''){
            alert("[提示]卡折号不可为空！");
            return false;
        }
        if (tkAccountNo.length > 20){
            alert("[提示]卡折号长度不能大于20！")
            return false;
        }
        var tkBankAddress=$("#tkBankAddress").val();
        if (tkBankAddress==""){
            alert("[提示]开户网点不可为空！");
            return false;
        }



        var tkAmount=$("#tkAmount").val();
        var tkType=$("#tkType").val();

        if(maxTkMoney < 100) {
            alert("[提示]老虎机/其它类佣金综合余额不足100无法提款");
            return false;
        }

        if(tkType == 'slotmachine') {
            var slotMoney = Number($('#moneyAccount').val()); //老虎机佣金余额
            if(tkAmount > slotMoney) {
                alert("[提示]最大提款金额为" + slotMoney + "元");
                return false;
            }
        }

        if(tkAmount - maxTkMoney > 0) {
            alert("[提示]最大提款金额为" + maxTkMoney + "元");
            return false;
        }

        if (tkAmount==''){
            alert("[提示]提款金额不可为空！");
            return false;
        }
        if (tkAmount<100||tkAmount>190000){
            alert("[提示]单次提款金额只能在100至190000之间");
            return false;
        }
        if(isNaN(tkAmount)){
            alert("提款金额只能是数字!");
            return false;
        }

        if (tkType==''){
            alert("[提示]提款类型不可为空！");
            return false;
        }


        openProgressBar();
        $.post("${ctx}/asp/withdraw.aspx", {
            "password":tkPassword,
            "bank":tkBank,
            "accountNo":tkAccountNo,
            "bankAddress":tkBankAddress,
            "amount":tkAmount,
            "msflag":0,
            "tkType":tkType
        }, function (returnedData, status) {
            if ("success" == status) {
                if(returnedData=="SUCCESS"){
                    alert("提款成功！");
                    window.location.href="${ctx}/agentManage.aspx"
                }else{
                    closeProgressBar();
                    alert(returnedData);
                }
            }
        });
    }

    // 提款平账需求
    $(function() {
        $("#tkAmount").focus(function() {
            var moneyAccount = $('#moneyAccount');
            var tkType = $("#tkType").val();
            var slotmachine = Number(moneyAccount.val());
            var liveall = Number(moneyAccount.attr('data-liveall'));
            maxTkMoney = (slotmachine + liveall).toFixed(2);
            $.get('/asp/agentWithdrawpz.aspx', {
                tkType: tkType,
                slotmachine: Number(slotmachine).toFixed(2),
                liveall: Number(liveall).toFixed(2)
            }, function(data) {
                $('#tkTip').html(data)
            })
        })
    });


    //重置提款
    function clearTkWithdrawal(){
        $("#tkPassword").val("");
        $("#tkAccountNo").val("");
        $("#tkAmount").val("");
    }
    //绑定银行卡
    function checkbandingform(){
        if(!window.confirm("确定吗？")){
            return false;
        }
        var bdbankno=$("#bdbankno").val();
        if(bdbankno==""){
            alert("[提示]卡/折号不可为空！");
            return false;
        }
        if(bdbankno.length>20||bdbankno.length<15){
            alert("[提示]卡/折号长度只能在15-20位之间");
            return false;
        }
        var bdbank=$("#bdbank").val();
        if(bdbank==""){
            alert("[提示]银行不能为空！");
            return false;
        }
        var bdpassword=$("#bdpassword").val();
        if(bdpassword==""){
            alert("[提示]登录密码不可以为空");
            return false;
        }
        openProgressBar();
        $.post("${ctx}/asp/mainbandingBankno.aspx", {
            "password":bdpassword,
            "bankname":bdbank,
            "bankno":bdbankno,
            "bankaddress":"none"
        }, function (returnedData, status) {
            if ("success" == status) {
                if(returnedData=="SUCCESS"){
                    alert("绑定成功！");
                    window.location.href="${ctx}/agentManage.aspx"
                }else{
                    closeProgressBar();
                    alert(returnedData);
                }
            }
        });
    }
    //重置绑定
    function clearBandingform(){
        $("#bdbankno").val("");
        $("#bdpassword").val("");
    }
    //额度记录
    function agentAmountRecord(){
        var amountStarttime=getNowDateOne();
        var amountEndtime=getNowDate();
        $("#amountStarttime").val(amountStarttime);
        $("#amountEndtime").val(amountEndtime);
        openProgressBar();
        $.post("${ctx}/asp/searchCreditlogs.aspx", {
            "pageno":1,
            "maxpageno":6,
            "starttime":amountStarttime,
            "endtime":amountEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#amountRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //额度记录
    function agentAmountRecordTwo(pageIndex){
        var amountStarttime=$("#amountStarttime").val();
        if(amountStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var amountEndtime=$("#amountEndtime").val();
        if(amountEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        openProgressBar();
        $.post("${ctx}/asp/searchCreditlogs.aspx", {
            "pageno":pageIndex,
            "maxpageno":6,
            "starttime":amountStarttime,
            "endtime":amountEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#amountRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //优惠提案
    function agentOfflineRecordyh(){
        var offlineStarttime=getNowDateOne();
        var offlineEndtime=getNowDate();
        $("#offlineStarttimeyh").val(offlineStarttime);
        $("#offlineEndtimeyh").val(offlineEndtime);
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":505,
            "loginname":"",
            "pageIndex":1,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime,
            "tail":"yh"
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#offlineRecordDivyh").html(returnedData);
            }
        });
        return false;
    }
    //优惠提案
    function agentOfflineRecordTwoyh(pageIndex){
        var offlineStarttime=$("#offlineStarttimeyh").val();
        if(offlineStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var offlineEndtime=$("#offlineEndtimeyh").val();
        if(offlineEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        var offlineUsername=$("#offlineUsernameyh").val();
        var offlineProposalType=$("#offlineProposalTypeyh").val();
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":offlineProposalType,
            "loginname":offlineUsername,
            "pageIndex":pageIndex,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime,
            "tail":"yh"
        }, function (returnedData, status) {
            closeProgressBar();
            if ("success" == status) {
                $("#offlineRecordDivyh").html(returnedData);
            }
        });
        return false;
    }
    //返水提案
    function agentOfflineRecordfs(){
        var offlineStarttime=getNowDateOne();
        var offlineEndtime=getNowDate();
        $("#offlineStarttimefs").val(offlineStarttime);
        $("#offlineEndtimefs").val(offlineEndtime);
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":507,
            "loginname":"",
            "pageIndex":1,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime,
            "tail":"fs"
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#offlineRecordDivfs").html("");
                $("#offlineRecordDivfs").html(returnedData);
            }
        });
        return false;
    }
    //返水提案
    function agentOfflineRecordTwofs(pageIndex){
        var offlineStarttime=$("#offlineStarttimefs").val();
        if(offlineStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var offlineEndtime=$("#offlineEndtimefs").val();
        if(offlineEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        var offlineUsername=$("#offlineUsernamefs").val();
        var offlineProposalType=$("#offlineProposalTypefs").val();
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":offlineProposalType,
            "loginname":offlineUsername,
            "pageIndex":pageIndex,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime,
            "tail":"fs"
        }, function (returnedData, status) {
            closeProgressBar();
            if ("success" == status) {
                $("#offlineRecordDivfs").html("");
                $("#offlineRecordDivfs").html(returnedData);
            }
        });
        return false;
    }
    //提款提案
    function agentOfflineRecordtk(){
        var offlineStarttime=getNowDateOne();
        var offlineEndtime=getNowDate();
        $("#offlineStarttimetk").val(offlineStarttime);
        $("#offlineEndtimetk").val(offlineEndtime);
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":503,
            "loginname":"",
            "pageIndex":1,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime,
            "tail":"tk"
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#offlineRecordDivtk").html(returnedData);
            }
        });
        return false;
    }
    //提款提案
    function agentOfflineRecordTwotk(pageIndex){
        var offlineStarttime=$("#offlineStarttimetk").val();
        if(offlineStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var offlineEndtime=$("#offlineEndtimetk").val();
        if(offlineEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        var offlineUsername=$("#offlineUsernametk").val();
        var offlineProposalType=$("#offlineProposalTypetk").val();
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":offlineProposalType,
            "loginname":offlineUsername,
            "pageIndex":pageIndex,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime,
            "tail":"tk"
        }, function (returnedData, status) {
            closeProgressBar();
            if ("success" == status) {
                $("#offlineRecordDivtk").html(returnedData);
            }
        });
        return false;
    }
    //下线提案
    function agentOfflineRecord(){
        var offlineStarttime=getNowDateOne();
        var offlineEndtime=getNowDate();
        $("#offlineStarttime").val(offlineStarttime);
        $("#offlineEndtime").val(offlineEndtime);
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":502,
            "loginname":"",
            "pageIndex":1,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#offlineRecordDiv").html("");
                $("#offlineRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //下线提案
    function agentOfflineRecordTwo(pageIndex){
        var offlineStarttime=$("#offlineStarttime").val();
        if(offlineStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var offlineEndtime=$("#offlineEndtime").val();
        if(offlineEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        var offlineUsername=$("#offlineUsername").val();
        var offlineProposalType=$("#offlineProposalType").val();
        openProgressBar();
        $.post("${ctx}/asp/searchsubuserProposal.aspx", {
            "proposalType":offlineProposalType,
            "loginname":offlineUsername,
            "pageIndex":pageIndex,
            "size":4,
            "starttime":offlineStarttime,
            "endtime":offlineEndtime
        }, function (returnedData, status) {
            closeProgressBar();
            if ("success" == status) {
                $("#offlineRecordDiv").html("");
                $("#offlineRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //获取当前日期：fmt-->2014-08-13 11:03:11
    function getNowDate(){
        var d = new Date(),
                vYear = d.getFullYear(),
                vMon = d.getMonth() + 1,
                vDay = d.getDate(),
                h = d.getHours(),
                m = d.getMinutes(),
                se = d.getSeconds(),
                s=vYear+"-"+(vMon<10 ? "0" + vMon : vMon)+"-"+(vDay<10 ? "0"+ vDay : vDay)+" "+(h<10 ? "0"+ h : h)+":"+(m<10 ? "0" + m : m)+":"+(se<10 ? "0" +se : se);
        return s ;
    }
    //获取本月的第一天
    function getNowDateOne(){
        var d = new Date(),
                vYear = d.getFullYear(),
                vMon = d.getMonth() + 1;
        return vYear+"-"+(vMon<10 ? "0" + vMon : vMon)+"-01 00:00:00";
    }


    //平台输赢
    function agentPlatformRecord(){
        var platformStarttime=getNowDateOne();
        var platformEndtime=getNowDate();
        /*       var platformStarttime="2008-08-08 00:00:00";
         var platformEndtime="2100-08-08 00:00:00"; */
        $("#platformStarttime").val(platformStarttime);
        $("#platformEndtime").val(platformEndtime);
        openProgressBar();
        $.post("${ctx}/asp/searchagprofit.aspx", {
            "platform":"",  //默认查询全部
            "loginname":"",
            "pageIndex":1,
            "size":10,
            "starttime":platformStarttime,
            "endtime":platformEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#platformRecordDiv").html("");
                $("#platformRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //平台输赢
    function agentPlatformRecordTwo(pageIndex){
        var platformStarttime=$("#platformStarttime").val();
        if(platformStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var platformEndtime=$("#platformEndtime").val();
        if(platformEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        var platformUsername=$("#platformUsername").val();
        var platformPlatform=$("#platformPlatform").val();
        openProgressBar();
        $.post("${ctx}/asp/searchagprofit.aspx", {
            "platform":platformPlatform,
            "loginname":platformUsername,
            "pageIndex":pageIndex,
            "size":10,
            "starttime":platformStarttime,
            "endtime":platformEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#platformRecordDiv").html("");
                $("#platformRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //下线会员记录
    function agentOfflineUserRecord(){
        var offlineUserStarttime=getNowDateOne();
        var offlineUserEndtime=getNowDate();
        $("#offlineUserStarttime").val(offlineUserStarttime);
        $("#offlineUserEndtime").val(offlineUserEndtime);
        openProgressBar();
        $.post("${ctx}/asp/queryAgentSubUserInfo.aspx", {
            "pageIndex":1,
            "size":6,
            "start":offlineUserStarttime,
            "end":offlineUserEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#offlineUserRecordDiv").html("");
                $("#offlineUserRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //下线会员记录
    function agentOfflineUserRecordTwo(pageIndex){
        var offlineUserStarttime=$("#offlineUserStarttime").val();
        if(offlineUserStarttime==""){
            alert("开始时间不能为空！");
            return false;
        }
        var offlineUserEndtime=$("#offlineUserEndtime").val();
        if(offlineUserEndtime==""){
            alert("结束时间不能为空！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        openProgressBar();
        $.post("${ctx}/asp/queryAgentSubUserInfo.aspx", {
            "pageIndex":pageIndex,
            "size":6,
            "start":offlineUserStarttime,
            "end":offlineUserEndtime
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#offlineUserRecordDiv").html("");
                $("#offlineUserRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //佣金明细
    function agentCommissionRecord(){
        var commissionYear=$("#commissionYear").val();
        if(commissionYear==""){
            alert("请选择年！");
            return false;
        }
        var commissionMonth=$("#commissionMonth").val();
        if(commissionMonth==""){
            alert("请选择月！");
            return false
        }
        openProgressBar();
        $.post("${ctx}/asp/queryCommissionrecords.aspx", {
            "pageIndex":1,
            "size":4,
            "year":commissionYear,
            "month":commissionMonth
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#commissionRecordDiv").html("");
                $("#commissionRecordDiv").html(returnedData);
            }
        });
        return false;
    }
    //更新信息
    function updateUser(){
        openProgressBar();
        var updateAliasName=$("#updateAliasName").val();
        var updateQq=$("#updateQq").val();
        var microchannel = $("#updateWechat").val();
        //var updateMailaddress=$("#updateMailaddress").val();
        $.post("${ctx}/asp/change_infoAjax.aspx", {
            "aliasName":updateAliasName,
            "qq":updateQq,
            "microchannel":microchannel,
            "mailaddress":""
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                alert(returnedData);
                //window.location.href="${ctx}/agentManage.aspx"
            }
        });
        return false;
    }
    //更新密码
    function updateDatePassword(){
        var updatePassword=$("#updatePassword").val();
        var updateNew_password=$("#updateNew_password").val();
        var updateSpass2=$("#updateSpass2").val();
        if (updatePassword==''){
            alert("[提示]用户旧密码不可为空！");
            return false;
        }
        if (updateNew_password==''){
            alert("[提示]用户新密码不可为空！");
            return false;
        }
        if (updateSpass2==''){
            alert("[提示]用户确认新密码不可为空！");
            return false;
        }
        if (updateSpass2 != "" && (updateSpass2 < 8 || updateSpass2 >12)){
            alert("[提示]密码的长度请介于8-12字符之间！")
            return false;
        }
        if (updateNew_password!=updateSpass2){
            alert("[提示]两次输入的密码不一致，请核对后重新输入！");
            return false;
        }
        openProgressBar();
        $.post("${ctx}/asp/change_pwsAjax.aspx", {
            "password":updatePassword,
            "new_password":updateNew_password,
            "sPass2":updateSpass2
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                alert(returnedData);
                //window.location.href="${ctx}/agentManage.aspx"
            }
        });
        return false;
    }

    //佣金明细
    function agentCommissionRecordTwo(pageIndex){
        var commissionYear=$("#commissionYear").val();
        if(commissionYear==""){
            alert("请选择年！");
            return false;
        }
        var commissionMonth=$("#commissionMonth").val();
        if(commissionMonth==""){
            alert("请选择月！");
            return false
        }
        if(pageIndex<=1){
            pageIndex=1;
        }
        openProgressBar();
        $.post("${ctx}/asp/queryCommissionrecords.aspx", {
            "pageIndex":pageIndex,
            "size":4,
            "year":commissionYear,
            "month":commissionMonth
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#commissionRecordDiv").html("");
                $("#commissionRecordDiv").html(returnedData);
            }
        });
        return false;
    }

    //点击
    function showOnclick(type){
        if(type=="0"){
            $('#one').css('display','block');
            $('#two').css('display','none');
        }else{
            $('#one').css('display','none');
            $('#two').css('display','block');
        }
    }




    //实时投注额度，输赢记录
    function agentBetProfitRecord(pageIndex){
        var startDate= $("#startDate").val();
        var endDate= $("#endDate").val();
        var platform = $("#gameType").val();
        var username = $("#betUsername").val();

        if(startDate == "" || endDate == ""){
            alert("请输入起始和截止时间");
            return ;
        }
        openProgressBar();
        $.post("${ctx}/asp/queryAgentBetProfit.aspx", {
            "pageIndex":pageIndex,
            "size":10,
            "platform":platform,
            "loginname":username,
            "starttime":startDate,
            "endtime":endDate
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#gamePlatBetRecordDiv").html("");
                $("#gamePlatBetRecordDiv").html(returnedData);
                createFixedable() ;
            }
        });
        return false;
    }

    //老虎机日结佣金记录
    function ptCommissionsRecord(pageIndex){
        if(pageIndex<1){
            alert("已经是第一页");
            return false;
        }
       var v=$('#totalpage').val();
        if(pageIndex>v){
            alert("已经是最后一页");
            return false;
        }
        var startDate=$("#startptCommDate").val();
        var endDate=$("#endptCommDate").val();

        if(startDate == "" || endDate == ""){
            startDate=getNowDateOne();
            endDate=getNowDate();
            $("#startptCommDate").val(startDate);
            $("#endptCommDate").val(endDate);
        }
        openProgressBar();
        $.post("${ctx}/asp/searchPtCommissions.aspx", {
            "pageIndex":pageIndex,
            "size":10,
            "starttime":startDate,
            "endtime":endDate
        }, function (returnedData, status) {
            if ("success" == status) {
                closeProgressBar();
                $("#agentPtCommissionRecordDiv").html("");
                $("#agentPtCommissionRecordDiv").html(returnedData);
            }
        });
        return false;
    }
 
</script>

<script>

function changeTextArea(imgStr){

	var imgurl = "${session.customer.referWebsite}"+"/images/"+imgStr;
	var html = "<a href='${session.customer.referWebsite}' target='_blank'> <img src='"+imgurl+"'/></a>";
	$("#remarksWord").val(html);
}
	$(function(){		
		ptCommissionsRecord(1);//佣金报表
    });
	
	//获取推广链接
	function queryAgentAddress(){
	  $.post("/asp/queryAgentAddress.aspx",
       function (returnedData, status) {
          if ("success" == status) {
              var $url=$('#j-url');
              $url.html(returnedData).hide();
              $url.find('a[href*="e68.me"]').append('<span class="pl10">网址已经被墙请勿使用！！！ </span>');
              $url.show();
          }
      });
	  return false;
	}

//重置绑定
  function clearBandingform(){
     $("#bdbankno").val("");
     $("#bdpassword").val("");
  }
  //查询绑定游戏账号
  function queryGameUser(){
	   $.post("${ctx}/asp/getBindGameUserByAgent.aspx", {
	     }, function (data) {
	    	 
	         if(data == "请先登录" || data.indexOf("请登录") != -1){
	             alert(data);
	             window.location.href="${ctx}/agentManage.aspx";
	           } else {
	             $("#gameUser").val(data);
	           }
	    	 
	     });
  }
  

  
   //代理绑定游戏账号
	function agentBindGameUser(){
		var gameUserToBind = $.trim($("#gameUserToBind").val());
		var agentPassword = $("#agentPassword").val();
		
		if(gameUserToBind == ''){
			alert('要绑定的账号不能为空,请重新输入');
			return;
		}
		if(agentPassword == ''){
			alert("清输入您的密码");
			return;
		}
		if(!confirm("您确定要绑定游戏账号：" + gameUserToBind + "吗？")){
			return;
		}
		openProgressBar();
		$.post("${ctx}/asp/agentUserBindGameUser.aspx", {
		   "loginnameGame":gameUserToBind,
		   "password":agentPassword
		  
		}, function (data) {
			
			if(data.success == true){
				alert('与游戏账号绑定成功');
				$("#gameUser").val(gameUserToBind);
				$('#j-modal-bind').modal('hide');
				clearText();
				closeProgressBar();
			} else {
				alert("异常：" + data.message);
				closeProgressBar();
			}
		
		});
	}
	 //清空绑定信息
	  function clearText(){
	    $("#gameUserToBind").val('');
	    $("#agentPassword").val('');
	  }
	  //转入游戏账号
	  function transferInGameUser(){
	    var gameUser = $.trim($("#gameUser").val());
	    var remit = $.trim($("#remit").val());
	    var transPassword = $("#transPassword").val();
	    if(gameUser == ''){
	      alert("您未绑定游戏账号或刷新页面重新操作！");
	      return;
	    }
	    if(remit == ''){
	      alert("转入金额不能为空");
	      return;
	    }
	    if(transPassword == ''){
	      alert("请输入代理密码");
	      return;
	    }
	    var reg = /^[1-9][0-9]+$/;
	    if(!reg.test(remit)){
	      alert("[提示]请输入大于等于10的整数！");
	      return;
	    }

	    openProgressBar();
	    $.post("${ctx}/asp/transferToGameUserFromAgentUser.aspx", {
	      "remit":remit,
	      "password":transPassword
	    }, function (data) {

	      if(data.success == true){
	        alert('转入游戏账号成功');
	        window.location.href="${ctx}/agentManage.aspx";
	      } else {
	        alert("异常：" + data.message);
	        closeProgressBar();
	        if(data.message == "请先登录" || data.message.indexOf("请登录") != -1){
	          window.location.href="${ctx}/agentManage.aspx";
	        }
	      }

	    });
	  }
</script>

		<script>
			window.onload=function(){
				var url=window.location.search;
				if(url=="?tab-tk"){
					$("#j-userNav").find("li").eq(3).find("a").click()
				}

				if(url=="?tab-personal"){
					$("#j-userNav").find("li").eq(2).find("a").click()
				}
				
				
			}
		</script>

</body>
</html>
