<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/style/basic.css" />
<script type="text/javascript" src="${ctx}/js/jquery18.js"></script>
<script type="text/javascript" src="${ctx}/js/superslide.2.1.js"></script>
</head>
<style type="text/css">
#banner .game-left { width:10%; float:left; margin-top:20px;} 
#banner .main { width:80%; float:left; margin-top:20px; margin-left:20px;}
.game-left ul { display: block; width: 100%; position: relative; }
.game-left ul li { padding:10px 0; margin-bottom:1px; background-color:#ccc; background-clip:  -webkit-transition: all 0.35s ease;
transition: all 0.35s ease; position:relative; font-size:16px; color:#333; text-align:center; cursor:pointer; z-index: 2; position: relative; }
.game-left li.on, .game-left li:hover { background-color: #f60; color: #fff; }
#banner .main .item { display: none; }
#banner .main .on { display: block; }
#banner .main table tr { height:30px; }
#banner .main table td { border: 1px solid #462922; font-size:16px; color: grey; }
</style>
<script type="text/javascript">
$(function(){
	$('#ul-menu li').click(function(){
		var ind = $(this).index();
		if(!$(this).hasClass('on')){$(this).addClass('on').siblings().removeClass('on');}
		$('#items .item').eq(ind).addClass('on').siblings().removeClass('on');
	});		
})
</script>
<body style="background: #fff;">
<div id="banner">
   
    
    <!--左快-->
    <div class="game-left">
      <ul class="items" id="ul-menu">
        <li class="on">基本信息</li>
        <li>在线支付</li>
        <li>存款</li>
        <li>提款</li>
        <li>优惠</li>
        <li>户内转账</li>
      </ul>
    </div>
    <!--右快-->
    <div class="main" id="items">
      <div class="item on">
        <table style="width:90%;text-align: center;">
          <tr>
            <td>账号：</td>
            <td>${playerInfo.loginname}</td>
          </tr>
          <tr>
            <td>类型：</td>
            <td><s:if test="#fc.role=='AGENT'">代理</s:if>
              <s:else>
                <s:property value="@dfh.model.enums.VipLevel@getText(#request.playerInfo.level)"/>
              </s:else></td>
          </tr>
          <tr>
            <td>余额：</td>
            <td>${playerInfo.credit}</td>
          </tr>
          <tr>
            <td>警告级别：</td>
            <td><s:property value="@dfh.model.enums.WarnLevel@getText(#request.playerInfo.warnflag)"/></td>
          </tr>
          <tr>
            <td>代理：</td>
            <td>${playerInfo.agent}</td>
          </tr>
          <tr>
            <td>注册时间：</td>
            <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#request.playerInfo.createtime"/></td>
          </tr>
        </table>
      </div>
      <div class="item">
        <table style="width: 100%;text-align: center;">
          <tr>
            <td>序号</td>
            <td>编号</td>
            <td>存款金额</td>
            <td>存款时间</td>
          </tr>
          <s:iterator var="fc" value="%{#request.payOrder}" status="st">
            <tr>
              <td><s:property value="#st.index+1" /></td>
              <td><s:property value="#fc.billno" /></td>
              <td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.money)" /></td>
              <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
            </tr>
          </s:iterator>
        </table>
      </div>
      <div class="item">
        <table style="width: 100%;text-align: center;">
          <tr>
            <td>序号</td>
            <td>编号</td>
            <td>存款金额</td>
            <td>存款时间</td>
            <td>状态</td>
          </tr>
          <s:iterator var="fc" value="%{#request.deposit}" status="st">
            <tr>
              <td><s:property value="#st.index+1" /></td>
              <td><s:property value="#fc.pno" /></td>
              <td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" /></td>
              <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
              <td><s:property value="@dfh.model.enums.ProposalFlagType@getText(#fc.flag)" /></td>
            </tr>
          </s:iterator>
        </table>
      </div>
      <div class="item">
        <table style="width: 100%;text-align: center;">
          <tr>
            <td>序号</td>
            <td>编号</td>
            <td>提款金额</td>
            <td>提款时间</td>
            <td>状态</td>
            <td>备注</td>
          </tr>
          <s:iterator var="fc" value="%{#request.withdrawal}" status="st">
            <tr>
              <td><s:property value="#st.index+1" /></td>
              <td><s:property value="#fc.pno" /></td>
              <td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" /></td>
              <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
              <td><s:property value="@dfh.model.enums.ProposalFlagType@getText(#fc.flag)" /></td>
              <td><s:property value="#fc.remark" /></td>
            </tr>
          </s:iterator>
        </table>
      </div>
      <div class="item">
        <table style="width: 100%; text-align: center;">
          <tr>
            <td>序号</td>
            <td>编号</td>
            <td>优惠类型</td>
            <td>赠送金额</td>
            <td>时间</td>
          </tr>
          <s:iterator var="fc" value="%{#request.coupon}" status="st">
            <tr>
              <td><s:property value="#st.index+1" /></td>
              <td><s:property value="#fc.pno" /></td>
              <td><s:property value="@dfh.model.enums.ProposalType@getText(#fc.type)" /></td>
              <td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.amount)" /></td>
              <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createTime"/></td>
            </tr>
          </s:iterator>
        </table>
      </div>
      <div class="item">
      	<table style="width: 100%; text-align: center;">
			<tr>
				<td>序号</td>
				<td>编号</td>
				<td>转账金额</td>
				<td>转账时间</td>
				<td>转账类型</td>
			</tr>
			<s:iterator var="fc" value="%{#request.transfer}" status="st">
				<tr>
					<td><s:property value="#st.index+1" /></td>
					<td><s:property value="#fc.id" /></td>
					<td><s:property value="@dfh.utils.NumericUtil@formatDouble(#fc.remit)" /></td>
					<td><s:date format="yyyy-MM-dd HH:mm:ss" name="#fc.createtime"/></td>
					<td><s:property value="#fc.source" /> → <s:property value="#fc.target" /></td>
				</tr>
			</s:iterator>
		</table>	
      </div>
      
    </div>
  
</div>
</body>
</html>