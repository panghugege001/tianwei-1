﻿<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>

<html>
<head>
  <meta charset="UTF-8">
  <title>单身狗</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link rel="stylesheet" href="/css/topic/dogstyle.css">
</head>
<body>
<div class="page">
  <div class="top">
    <div class="logo">
      <a href="javascript:;"><img src="/images/topic/dog/logo.png"></a>
    </div>
    <div class="banner">
      <div class="position"><img src="/images/topic/dog/dogpicture.png"></div>
    </div>
  </div>
  <div class="center">
    <div class="activity">
      <div class="date">
        <ul>
          <li><div class="title">活动对象</div><div class="text">天威全体会员</div></li>
          <li><div class="title">活动时间</div><div class="text">8月1号 到 8月9号 七夕情人节</div></li>
          <li><a class="btn-more" href="#j-rank">流水排名>></a></li>
        </ul>
      </div>
      <div class="list">
        <div class="title"><i></i>活动<span>1</span></div>
        <div class="text">
          <table class="table">
            <tbody>
            <tr><th>名次</th><th>彩金</th></tr>
            <tr><td><span style="color:red;">冠军</span></td><td><span style="color:red;">1588</span></td></tr>
            <tr><td><span>亚军</span></td><td><span>888</span></td></tr>
            <tr><td><span>季军</span></td><td><span>588</span></td></tr>
            <tr><td><span>第 4-10 名</span></td><td><span>188</span></td></tr>
            <tr><td><span>第 11-50 名</span></td><td><span>128</span></td></tr>
            <tr><td><span>第 51-100 名</span></td><td><span>68</span></td></tr>
            <tr><td><span>第 101-300 名</span></td><td><span>38</span></td></tr>
            </tbody>
          </table>
          <p>8月1到8月9号七夕情人节老虎机累积有效流水进入排名，次日，系统将自动派发流水奖</p> <br> <br>
          <p>活动规则：</p>
          <p>1.彩金无需申请，无流水要求。活动结束后，次日18:00之前，系统自动派发到账。</p>
          <p>2.有效投注额计算时间：8月1号0:00到8月9号23:59:59期间的所有老虎机平台的有效投注额。</p>
          <p>3.此活动只限PT经典老虎机、电动吃角子游戏（不包含累积部分），pt/TTG/NT/QT老虎机。所有21点游戏，所有轮盘游戏，所有百家乐游戏，所有骰宝游戏，所有视频扑克游戏及刮刮乐游戏等，多旋转老虎机和老虎机奖金翻倍投注将不计算在内。若发现不正当投注天威有权收回红利和非法盈利额。</p>
          <p>4.此活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</p>
          <p>5.天威娱乐城保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
        </div>
      </div>
      <div class="list">
        <div class="title"><i></i>活动<span>2</span></div>
        <div class="text">
          <p>七夕天威带你一起嗨起来<br><br></p>
          <p>8月9号七夕情人节活动当天累计存款达到1111元以上的会员可联系客服申请七夕单身派对111元红包。</p>
          <p>活动规则：</p>
          <p>1.活动红包需联系在线客服提供游戏账号申请，达到要求后直接派发。</p>
          <p>2.红包需25倍流水。即可提款。</p>
          <p>3.红包使用方法：点击账户管理——自助优惠——优惠券专区——红包优惠券。输入代码转账即可。</p>
          <p>4.红包优惠券需PT/TTG/NT/QT游戏账户低于5元才能使用红包优惠券。达到相应的有效投注额要求或PT/TTG/NT/T游戏账户低于5元，才能再次进行PT/TTG/NT/QT户内转账。</p>
          <p>5.此项优惠活动限投注PT/TTG/NT/QT游戏，经典老虎机，电动吃角子游戏。其他21点，轮盘，百家乐，骰宝，视频扑克，刮刮乐，街机等游戏皆不得使用，多旋转老虎机和老虎机奖金翻倍的投注将不计算在要求的流水额度内。若发现违反活动要求，天威娱乐城有权收回所有红利及奖金。</p>
          <p>6.此项优惠活动只针对娱乐性质的会员，同一家庭，同一住址，同一电子邮件地址，同一支付账号（相同借记卡/信用卡/银行账户号码等）以及同一IP地址只有申请相关优惠一次。</p>
          <p>7.天威娱乐城保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
          <p>8.天威娱乐享有最终解释权。</p>
        </div>
      </div>
      <div class="list" id="j-rank">
        <div class="title"><i></i>流水排名<span>3</span></div>
        <div class="text">
	        <form name="mainform" action="${ctx}/asp/queryNineDaysBets.aspx" method="post" data-dataform>
	          <table class="table">
	            <tbody>
		            	
		            <s:iterator var="fcg" value="%{#request.list}"
									status="st">
									<tr><th>个人排名</th><th>排名日期</th><th>总投注额</th></tr>
									<tr>
										<td>
											<s:property value="#fcg.ranking" />
										</td>
										<td>
											<s:property value="#fcg.rankdate" />
										</td>
										<td>
											<s:property value="#fcg.bettotal" />
										</td>
									</tr>
					</s:iterator>
		            
	            </tbody>
	            <tbody>
		            <tr><th>玩家账号</th><th>排名</th><th>排名日期</th><th>总投注额</th></tr>
		            <s:iterator var="fc" value="%{#request.page.pageContents}"
							status="st">
							<tr>
								<td>
									<s:property value="#fc.loginname" />
								</td>
								<td>
									<s:property value="#fc.ranking" />
								</td>
								<td>
									<s:property value="#fc.rankdate" />
								</td>
								<td>
									<s:property value="#fc.bettotal" />
								</td>
							</tr>
					</s:iterator>
					
	            </tbody>
	          </table>
	          <div class="pagination" >
						<input type="hidden" name="pageIndex" value="1" id="pageIndex"/>
						<input type="hidden" name="size" value="10" />
						${page.jsPageCode}
			  </div>
			</form>
        </div>
      </div>
    </div>
  </div>
</div>


<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script>
  function gopage(val) {
    document.mainform.pageIndex.value = val;
    document.mainform.submit();
  }
</script>

</body>
</html>