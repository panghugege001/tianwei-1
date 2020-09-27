<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style type="text/css">
#reg-welcome-box { display: none; position: absolute; left: 50%; top: 15%; margin-left: -273px; width: 546px; background: #fff; border-radius: 5px; padding: 15px 0; color: #fff; z-index: 99999; }
.tc { margin-top:20px; text-align:center; color:#737373; font-size:24px; }
.reg-box h2, .reg-box h3, .reg-box p { margin-left:50px; }
.reg-box h2, .reg-box h3 { font-size:21px; font-weight:700; line-height:31px; }
.reg-box h3 { margin-top:20px; font-size:16px; line-height:27px;color:#c05a00; }
.reg-box p { line-height:35px; font-size:18px; color:#c05a00; }
.reg-box .t { text-indent:17px; font-weight: 700; font-size:14px; color:#737373; }
.reg-box .btn { width:100px; height:35px; margin-top:20px; line-height:35px; display:inline-block; color:#fff; text-align:center; font-size:16px; background: #c05a00; }
.reg-box .btn:hover { background:#cb7326; }
.reg-box .h{ width:100%; height:11px; display:block;background:url(../images/heng.png) repeat-x 0 0; position:absolute;top:0;right:0;} 
.reg-box .s{ width:5px; height:100%; display:block;background:url(../images/shu.png) repeat-x 0 0; position:absolute;top:0;left:0;} 

#screen { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: #000; z-index: 9998; filter: alpha(opacity=80); opacity: 0.8; display: none; }
</style>
<script type="text/javascript">
	$(function(){
		$("#reg-welcome-box").show();
		$('#screen').show().css({'width':$(document).width()+20,'height':$(document).height()+20});
	})
</script>
<div class="reg-box" id="reg-welcome-box">
  <span class="h"></span>
  <span class="s"></span>
  <h2 class="tc">注册成功，请您登录</h2>
  <h3>天威欢迎您!</h3>
  <p>1.免费赠送PT老虎机8-88 元体验金</p>
  <p class="t">请您进入" 账户管理 " -> " PT体验金 " 自主领取</p>
  <p>2.PT老虎机首存68%,可送888元!</p>
  <p class="t">请您进入" 账户管理 " -> " 自助存送 " 自主提交</p>
  <p>3.在登陆PT客户端时需在账户前面填加大写字母 "E"</p>
  <p class="t">网站支持在线版游戏、客户端、手机投注!</p>
  <p style="text-align:center;"><a class="btn" href="/">确定</a></p>
</div>
<div id="screen" ></div>
