<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="${ctx}/css/agent.css5?v=1220"/>
	<script src="${ctx}/js/area.js"></script>
</head>
<body>
<div class="agent-bg">
  <jsp:include page="${ctx}/tpl/headerAgent.jsp"></jsp:include>
	<div class="container agent-main">
		<div class="agent-center fr text-center">
			<img src="/images/agent/tit.png" class="a-flipinX"><div class="btn-mod mt30 text-center"><a href="javascript:void(0);" data-toggle="modal" data-target="#modal-login" class="login a-ring" ></a><a href="javascript:void(0);" data-toggle="modal" data-target="#j-modal-agentreg" class="reg a-ring"></a></div>
		</div> 
		<div class="cl"></div>
		<div class="vip-box" id="tab-brand">
			<ul class="agent-bar">
				<li><span class="cee6">★&nbsp;45%</span>日结佣金</li><li><span class="cee6">★&nbsp;秒</span>存、提、返水</li><li><span class="cee6">★&nbsp;多元</span>平台、优惠</li>
			</ul>
			<div class="m-content">
			<h2>品牌介绍</h2>
			<h3>品牌介绍</h3>
			<p>天威创建2017年，隶属于总公司LUCKY STAR ENTERTAINMENT LIMITED，总公司注册创立于2005年，2009年获得Firstcagayan正规博彩牌照NO.020-A，公司历经10年之久，全球领先秒提技术迅速占领亚洲市场份额，凭其雄厚的实力及业界优质关系网，致力打造亚洲最佳在线娱乐平台。</p>
			<h3>公司文化</h3>
			<p>天威城秉承以人为本，科技立身为宗旨，为业界研发打造10秒到账「秒提、秒存、秒返水」技术，致力为博彩界打造提款最迅速的公司。</p>
			<h3>品牌优势</h3>
			<p>① 提款：20万以下提款10秒到账，全球首发。 <br>
			  ② 服务：7x24小时在线服务，提供优质服务。 <br>
			  ③ 安全：采用128位ssl高标准加密技术。 <br>
			  ④ 佣金结算：采用全球最优质的结算方式日结佣金。 <br>
			  ⑤ 产品丰富：PT、DT、MG、PNG、TTG、NT、QT、AG、沙巴体育、N2。 <br>
			  ⑥ 强大的品牌优势：多年业界运营实践经验，咨询丰富。 <br>
			  ⑦ 平台自动化：自助优惠，自助洗码，独立研发。</p>
		  </div>
	</div>
	<div class="vip-box mt40" id="tab-plan">
		<div class="m-content">
			<h2>佣金计划</h2>
			<h3>佣金结算</h3>
			<p>无需任何费用，0成本拓展你的财富之路，一圆你的boss梦。<br />您只需要推广网络推广或者是身边朋友介绍成为我们的玩家，天威会根据代理每月游戏的净利润收入按照佣金比例计算佣金。 </p>
			<table class="table">
			  <thead>
			  <tr>
				<th>等级</th>
				<th>当月活跃会员数</th>
				<th>当月净利润(RMB)</th>
				<th>佣金比例</th>
			  </tr>
			  </thead>
			  <tbody>
			  <tr>
				<td>1</td>
				<td>≥3</td>
				<td>0-300.000</td>
				<td>30%</td>
			  </tr>
			  <tr>
				<td>2</td>
				<td>≥15</td>
				<td>300.001-1.000.000</td>
				<td>35%</td>
			  </tr>
			  <tr>
				<td>3</td>
				<td>≥40</td>
				<td>1.000.001-2.000.000</td>
				<td>40%</td>
			  </tr>
			  <tr>
				<td>4</td>
				<td>≥100</td>
				<td>≥2.000.001</td>
				<td>45%</td>
			  </tr>
			  <tr>
				<td>结算方式</td>
				<td colspan="3">佣金计算</td>
			  </tr>
			   <tr>
				<td>日结佣金</td>
				<td colspan="3">老虎机佣金（PT/DT/MG/PNG/TTG/NT/QT/AG老虎机/AGIN捕鱼平台输赢*85％-返水-优惠）*佣金比例<br>其他平台佣金:(AG/SB/N2等吉他平台输赢X90%-返水-优惠）*佣金比例</td>
			  </tr>
			  </tbody>
			</table>
			<h3>佣金日结标准</h3>
			<ul>
			  <li>电子类:（PT/DT/MG/PNG/TTG/NT/QT/AG老虎机/AGIN捕鱼）
				<p class="c-yellow-d">所有老虎机平台包括捕鱼，均可享受日结，每日下午5点之后结算。</p>
				<p class="c-yellow-d">提款要求：佣金≥100元可随时进行提款</p>
				<p class="c-yellow-d">注：每月1-5号是月结算时间，此时间段内日结算佣金不更新，于6号统一执行。</p>
			  </li>
			 <li>其他类：（包含真人，体育）
				<ol>
				  <li class="c-yellow-d">老代理：历史佣金累计15万以上，且连续6个月有佣金记录，可升级为VIP代理，享有佣金日结。</li>
				  <li class="c-yellow-d">新代理：累计佣金30万以上，每月满足活跃会员要求，才能晋级VIP代理，享有日结佣金&nbsp;
					<p>提款要求：每月月初1-5号为其他平台佣金额度提款时间，请在这个时间段内申请提款，如果规定时间内未提款，佣金将累计至下月</p>
				  </li>
				</ol>
			  </li>
			</ul>
			<h3>注意事项：</h3>
			<ul>
				<li>
				<p class="c-yellow-d">1.享受日结的代理如果连续一个月没有新增一位活跃会员，平台第二个月将其取消日结模式，如果在第二个月有开发会员了，会在次月开启日结。</p>
				<p class="c-yellow-d">2.若合作伙伴自己的游戏账号注册到自己代理线下，取消代理资格且佣金不予发放。</p>
				<p class="c-yellow-d">3.代理许谨慎填写代理信息，如有填写错误信息，导致无法出款，恕天威概不负责。</p>
				<p class="c-yellow-d">4.若代理下线出现套取佣金或优惠的会员有权力拒绝服务该用户，并在不通知的情况下冻结，关闭相关游戏账号与代理账号，不退换任何的款项。</p>
				<p class="c-yellow-d">5.绑定游戏账户必须与代理账号的姓名、银行卡相同，且不能是在自己代理下线注册的游戏账号</p>
				<p class="c-yellow-d">6.加盟发展三月以上代理，如当月没有新增三个活跃会员，佣金不予出款。</p>
				</li>
			</ul>
			<div style="text-align: center;">
			  <a class="btn-panel" data-toggle="modal" data-target="#j-dialog-panel" href="javascript:;">合作协议</a>
			</div>
		  </div>
		</div>
		<div class="vip-box mt40" id="tab-dream">
			<div class="m-content">
				<h2>热情招募合伙人，百万圆您BOSS梦</h2>
				<p>“天高任鸟飞，海阔凭鱼跃”为了让您有更广阔的“天空”，为了让您有更好的“舞台”，针对每日辛苦的代理合作商，公司特举办——2017年“热情招募合伙人，百万圆您BOSS梦”活动，</p>
				<h3>具体活动内容如下：</h3>
				<p>单月创造净利润达到100万，或一年内累加净利润达到500万，即可申请——“圆梦”资助。</p>
				<p>公司为您量身打造“专属品牌”。</p>
				<p>公司将为您提供技术、客服、财务（现金流）等相应配套模式，助您无忧无虑当老板！</p>
				<p>加盟“天威”，跨越“财富之颠”</p>
				<p class="mt40">只要您有梦想、野心、信心！天威将圆您boss梦！</p>
				<p>只要您有梦想、野心、信心！天威将圆您boss梦！</p>
			</div>
		</div>
		<ul class="contact-list mt30" id="tab-contact">
			<li>
			  <i class="sp sp-qq"></i>
			  <p>QQ：3002864181</p>
			</li>
			<li>
			  <i class="sp sp-weichar"></i>
			  <p>微信：longdu000</p>
			</li>			 
			<li>
			 <i class="sp sp-SWpe"></i>
			 <p>SWpe：longdu168</p> 
			</li>
			<li>
			  <i class="sp sp-mail"></i>
			  <p>邮箱：mk@longdu6.com</p> 
			</li>
		  </ul>
	</div>
</div>
<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>

<div class="modal fade dialog-agentreg" id="j-dialog-panel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" style="display: none;">
  <div class="modal-dialog" style="margin-top: 0;">
    <div class="modal-content">
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">合作协议书</h4>
      </div>
      <div class="modal-bd m-content" style="overflow:auto;height:760px">
        <h2>一、协议双方</h2>
        <div>
          <p>甲方（授权方）天威。</p>
          <p>乙方（获权方）代理方。</p>
        </div>

        <h2>二、协议双方说明</h2>
        <ol>
          <li>当您要注册合作账户时，请您详情的看下我们的合作协议书，再考虑是否愿意合作。</li>
          <li>当您注册完账户之后，市场部会为您审核是否通过，以邮件或QQ以及站内信的形式告知乙方。</li>
          <li>本协议在甲方乙方同意下立即生效(注册合作账户审核通过后默认生效)。</li>
        </ol>

        <h2>三、关于天威</h2>
        <div>
          <p>天威创建2017年，隶属于总公司LUCKY STAR ENTERTAINMENT LIMITED， 总公司注册创立于2005年， 2009年获得Firstcagayan正规博彩牌照NO.020-A，公司历经10年之久，
全球领先秒提技术迅速占领亚洲市场份额，凭其雄厚的实力及业界优质关系网，致力打造亚洲最佳在线娱乐平台。</p>
          <h3>公司文化:</h3>
          <p>天威秉承以人为本，科技立身为宗旨，为业界研发打造10秒到账「秒提、秒存、秒返水」技术，致力为博彩界打造提款最迅速的公司。</p>
          <h3>品牌优势:</h3>
          <p>① 提款：20万以下提款10秒到账，全球首发。</p>
          <p>② 服务：7x24小时在线服务，提供优质服务。</p>
          <p>③ 安全：天威采用128位ssl高标准加密技术。</p>
          <p>④ 佣金结算：天威采用全球最优质的结算方式日结佣金。</p>
          <p>⑤ 产品丰富：PT、DT、MG、PNG、TTG、NT、QT、AG、沙巴体育、N2。</p>
          <p>⑥ 强大的品牌优势：多年业界运营实践经验，咨询丰富。</p>
          <p>⑦ 平台自动化：自助优惠，自助实时返水，独立研发。</p>
        </div>

        <h2>四、佣金结算时间和方式</h2>
        <div>
		  <p>1.佣金方案</p>
          <p>"无需任何费用，0成本拓展你的财富之路，一圆你的boss梦。您只需要推广网络推广或者是身边朋友介绍成为我们的玩家，天威会根据代理每月游戏的净利润收入按照佣金比例计算佣金。"</p>
          <table class="table">
            <tr>
              <th>等级</th>
              <th>当月活跃会员数</th>
              <th>当月净利润(RMB)</th>
              <th>佣金比例</th>
            </tr>
            <tr>
              <td>1</td>
              <td>≥3</td>
              <td>0-300，000</td>
              <td>30%</td>
            </tr>
            <tr>
              <td>2</td>
              <td>≥15</td>
              <td>300，001-1，000，000</td>
              <td>35%</td>
            </tr>
            <tr>
              <td>3</td>
              <td>≥40</td>
              <td>1，000，001-2，000，000</td>
              <td>40%</td>
            </tr>
            <tr>
              <td>4</td>
              <td>≥100</td>
              <td>≥2，000，001</td>
              <td>45%</td>
            </tr>
			<tr>
				<td>结算方式</td>
				<td colspan="3">佣金计算</td>
			</tr>
			<tr>
				<td>结算方式</td>
				<td colspan="3">老虎机佣金（PT/DT/MG/PNG/TTG/NT/QT/AG老虎机/AGIN捕鱼平台输赢*85％-返水-优惠）*佣金比例<br>其他平台佣金:(AG/SB/N2等吉他平台输赢X90%-返水-优惠）*佣金比例</td>
			</tr>
          </table>
   

          <h3>2、佣金日结方式</h3>
          <ul>
            <li>电子类：（PT/DT/MG/PNG/TTG/QT/NT/AG老虎机/AGIN捕鱼）
              <p>所有老虎机平台包括捕鱼，均可享受日结，每日下午5点之后结算。</p>
              <p>提款要求：佣金≥100元可随时进行提款</p>
			  <p>注：每月1-5号是月结算时间，此时间段内日结算佣金不更新，于6号统一执行。</p>	
            </li>
           <li>其他类：（包含真人，体育）
				<ol>
				  <li class="c-yellow-d">老代理：历史佣金累计15万以上，且连续6个月有佣金记录，可升级为VIP代理，享有佣金日结。</li>
				  <li class="c-yellow-d">新代理：累计佣金30万以上，每月满足活跃会员要求，才能晋级VIP代理，享有日结佣金&nbsp;
					<p>提款要求：每月月初1-5号为其他平台佣金额度提款时间，请在这个时间段内申请提款，如果规定时间内未提款，佣金将累计至下月</p>
				  </li>
				</ol>
			  </li>
          </ul>
          <p class="c-red">注意事项:</p>
          <ol>
            <li>享受日结的代理如果连续一个月没有新增一位活跃会员，平台第二个月将其取消日结模式，如果在第二个月有开发会员了，会在次月开启日结。</li>
            <li>若合作伙伴自己的游戏账号注册到自己代理线下，取消代理资格且佣金不予发放。</li>
            <li>代理许谨慎填写代理信息，如有填写错误信息，导致无法出款，恕天威概不负责。</li>
            <li>若代理下线出现套取佣金或优惠的会员起了有权拒绝服务该用户，并在不通知的情况下冻结，关闭相关游戏账号与代理账号，不退换任何的款项。</li>
            <li>绑定游戏账户必须与代理账号的姓名、银行卡相同，且不能是在自己代理下线注册的游戏账号</li>
            <li>加盟发展三月以上代理，如当月没有新增三个活跃会员，佣金不予出款。</li>
          </ol>
          <h3>3、佣金月结标准</h3>
          <div>
            <p>1. 针对普通代理佣金发放日为每月5号，（结算时间为自然月：上月1号：00:00点至本月1号00:00）。</p>
            <p>2. 当月达到3位有效会员且佣金大于100元。</p>
            <p>3. 有效会员标准，当月存款大于500元，投注额大于1000元。</p>
            <p>4. 若当月未达到发放标准，佣金会累计至某月达到要求后一起发放。</p>
            <p>5. 若当月代理佣金为负值，会累计至下月，等至累计正数且要求达到后佣金发放。</p>
            <p>6. 若合作伙伴自己的游戏账号注册到自己代理下线，取消代理资格，佣金不予发放。</p>
          </div>

          <h3>4、佣金计算标准</h3>
          <div>
            <p>计算公式：</p>
            <div>
              <p>(PT/DT/MG/TTG/QT/NT/PNG/AG老虎机/AGIN捕鱼平台输赢x85%+其他平台输赢x90%-优惠-反水）=纯利润。</p>
              <p>纯利润x佣金比例=佣金。</p>
            </div>
            <p>玩家输赢里面包含了平台给玩家的所有优惠红利  以及公司向平台方支付的平台费用，天威与合作伙伴需共同承担相应比例的费用包括：</p>
            <div>
              <p>1.优惠——天威给予玩家的现金红利都计算在输赢里面。</p>
              <p>2.反水——天威每日会给玩家相应的反水，反水计算在玩家输赢里面。</p>
              <p>3.平台费用——天威需提款游戏平台供应商10%-15%的平台费用。</p>
            </div>
            <p>合作伙伴必须严格遵守天威相关条款条例，以免造成不必要的误会。</p>
          </div>
        </div>
        <h3>五、责任与条款</h3>
        <div>
          <p>1、甲乙双方应严格遵守天威娱乐城《合作协议》以及其他协议相关条款条例。</p>
          <p>2、乙方在甲方公司代理推广过程中，如有不诚实，违规操作恶意毁损天威声誉，没有遵守《合作协议》甲方有权拒绝向乙方支付佣金的权利，参与天威娱乐城推广的同时，已确认同意接受天威娱乐城相关合作条款。</p>
          <p>3、乙方同意本协议项下的合作均为天威代理后台为准，该数据也将作为解决争端时的有效判断依据，最终解释权归甲方所有。</p>
          <p>4、甲方不能将自己的游戏账号以任意形式注册到自己的代理下线，如有发现，自己的游戏账号输赢不计算在佣金内，如有恶意注册，佣金不予发放，且永久关闭合作账号。</p>
          <p>5、如有玩家在乙方推荐的过程中，乙方或玩家输入错误合作连接注册进来的玩家为不计算在乙方下线玩家，公司概不负责。</p>
          <p>6、一人只有一个合作代理账户，若乙方多个代理账户，所有账号输赢将会叠加计算。</p>
          <p>7、甲方有权修改佣金的计算方式以及佣金比例。</p>
          <p>8、乙方注册合作账户之后，此协议生效，有任何争议此协议为最终解释权。</p>
          <p>9、在没有任何预先通知下，甲方有权更改以上条款和条例，解释权归甲方所有。</p>
          <p>10、请详细阅读以上条款条例，根据自身实际情况定夺是否能胜任我们的合作伙伴。</p>
          <p>11、已方在合营账号审核通过之后2个月内未开发一个下线会员，甲方有权收回合营代理链接。</p>
          <p>12、代理信息无法修改，请准确填写自己的真实信息，如果由于信息错误导致佣金无法提款的，天威将不负任何责任。</p>
          <p>13、禁止代理本人和下线同IP，代理本人和下线多次同IP，此玩家将不计算在佣金范围内，发现一次为警告，多次发展将关闭代理账号，佣金不予返还。</p>
        </div>

        <h3>六、法律监督和仲裁</h3>
        <div>
          <p>1、无论是在任何法律条例的管制下，此协议都将受制于菲律宾政府的法律。任何与此协议相关的法律行为必须被带到菲律宾政府。您认可并认真阅读了此协议并同意其所有的规则及条款，在任何时候你确认您已经自主评估了参与的意愿并不依赖于任何在此协议之外的言论、保证和声明。</p>
          <p>2、若乙方起诉甲方，根据以上规定需要菲律宾当地法庭做出最后的裁决其仲裁语音为英语。</p>
          <p>3、天威《合作协议》请仔细阅读，出现合作争议本合作协议为最终解释权。</p>
        </div>

      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal -->
</div>

<!-- 注册弹框 -->
<div class="modal fade reg-page" id="j-modal-agentreg"   tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" style="display: none;">
  <div class="modal-dialog lg" style="margin-top:150px;">
    <div class="modal-content">
	  <div class="modal-logo"></div>
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title text-center">
          代理注册
        </h4>
      </div>
      <div class="modal-bd"  style="padding:18px 0 0 0">
		<div class="mb20 text-center">备注：标记有<span class="c-red">*</span>为必填项</div>
        <form action="${ctx}/asp/addAgent.aspx" method="post" id="j_agent_form" class="reg-form">
          <div class="m-row">
            <div class="cell col-6">
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">用户名：</label>
                <input type="text" class="ui-ipt" placeholder="开头(a后面是下划线_)，由3-15个数字或英文字母组成" name="loginname" data-rule-username="true" maxlength="15" required/>
                <span class="ipt-tip">账号以 a_ 开头(a后面是下划线_)，由3-15个数字或英文字母组成</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">密码：</label>
                <input id="ipt_password" type="password" name="password" class="ui-ipt" placeholder="由8-12个数字或英文字母组成" required data-rule-rangelength="8,12"/>
                <span class="ipt-tip">由8-12个数字或英文字母组成</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">确认密码：</label>
                <input type="password" name="confirmpassword" id="ipt_confirmpassword" class="ui-ipt" placeholder="请再次填写登录密码" required data-rule-equalto="#ipt_password"/>
                <span class="ipt-tip">请再次填写登录密码</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">真实姓名：</label>
                <input type="text" class="ui-ipt" placeholder="必须与您的银行帐户名称相同，否则不能出款" name="accountName" required/>
                <span class="ipt-tip">必须与您的银行帐户名称相同，否则不能出款</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">手机：</label>
                <input type="text" name="phone" class="ui-ipt" placeholder="请输入你的常用联系电话" required/>
                <span class="ipt-tip">请输入你的常用联系电话</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">验证码：</label>
                <input type="text" style="width: 170px;" class="ui-ipt" placeholder="验证码" name="validateCode" required/>
                <img height="30" width="100" alt="验证码" id="j_validatecode" class="validatecode" src="/asp/agTryValidateCodeForIndex.aspx" onClick="this.src='/asp/agTryValidateCodeForIndex.aspx?v='+Math.random();">
              </div>
            </div>
            <div class="cell col-6">
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">QQ/SWpe：</label>
                <input type="text" name="qq" class="ui-ipt" placeholder="玩家注册时必须填写QQ或SWpe帐号" maxlength="20" required/> 
                <span class="ipt-tip">玩家注册时必须填写QQ或SWpe帐号</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">邮箱：</label>
                <input type="text" class="ui-ipt" placeholder="建议@google.com邮箱" name="email" required data-rule-email="true"/>
                <span class="ipt-tip">建议@google.com邮箱</span>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">微信：</label>
                <input type="text" name="microchannel" class="ui-ipt" required placeholder="请输入微信号" data-msg-required="请输入微信号"/>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label">代理推荐码：</label>
                <input type="text" class="ui-ipt" placeholder="" name="partner"/>
              </div>
              <div class="ui-form-item">
                <label for="" class="ui-label rq-value">推广网址：</label>
                <span>http://</span>
                <input type="text" class="ui-ipt" placeholder="代理专属链接，自定义填写申请" required style="width: 140px" name="referWebsite"/>
                <span>.longdu95.com</span>
                <span class="ipt-tip">(代理专属链接，自定义填写申请)</span>
              </div>
            </div>
          </div>
          <div class="form-ft">
            <div class="text agree">
              <label for="">
                <input type="checkbox" name="dfd" required  data-msg-required="请选择是否同意协议"/>
                本人已经超过合法年龄以及本人在此网站的所有活动并没有抵触本人所身在的国家所管辖的法律，<br/>
                同时接受 <a class="c-red" href="down/AgentAgreement-2019.doc">《天威娱乐城合作协议》</a>。
              </label>
            </div>
            <div class="text-center mb20">
              <input type="submit" class="btn btn-primary btn-reg" value="立即登录"/>
              <input type="reset" class="btn j-resetBtn" value="重置" style="display: none;">
            </div>
            <div class="text-center c-red">* 请代理加盟商注册好及时联系我司相关工作人员 QQ: 3002886077，3002815923 , 3002897461，3002839349; SWpe：longdu_mk邮箱：mk@longdu6.com</div>
          </div>

        </form>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal -->
</div>

<script src="${ctx}/js/plugins/jquery.validate.min.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.config.js"></script>
<script>
    $(function(){
      var $form=$('#j_agent_form');
      $form.validate({
            submitHandler:function(form){
            if(confirm("请确认资料是否填写正确，如有乱填将不审核，此账号不能进行游戏，确认提交后，我们的市场专员会24小时内会联系您。")){
                $.post('${ctx}/asp/addAgent.aspx',$(form).serialize(),function(data){
                    if(data=='SUCCESS'){
                        alert("注册成功！");
                        window.location.href="${ctx}/";
                    }else if(data=="帐号已禁用 :联系市场部门！"){
                        alert("代理账号审核中......");
                        window.location.href="${ctx}/";
                    }else{
                        var $code= $('#j_validatecode');
                        $code.val('');
                        $('#ipt_password').val('');
                        $('#ipt_confirmpassword').val('');
                        //form.confirmpassword.value='';
                        //$code.attr('src',$code.attr('src')); 
                        alert(data);
                    }
                });
            	} 
            }
        });

      $('#j-modal-agentreg').on('show.bs.modal',function(){
        $form.find('.j-resetBtn').trigger('click');
        $form.find('label.error-tip').hide();
        $form.find('.ui-ipt.error-tip').removeClass('error-tip');
      });

        var $slide=$('#agent-slide'),
	        $inddcators=$slide.find('.carousel-indicators>li'),
	        $slideList=$slide.find('.carousel-inner>.item');
		$slide.on('slide.bs.carousel',function(e){
		    var index=$slideList.index(e.relatedTarget);
		    $inddcators.removeClass('active').eq(index).addClass('active');
		});
    });
</script>
</body>
</html>
