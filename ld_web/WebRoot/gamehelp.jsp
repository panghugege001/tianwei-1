<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
     
    <s:include value="title.jsp"></s:include>
    <meta http-equiv="description" content="游戏玩法"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/> 
	<link type="text/css" rel="stylesheet" href="yilufa.css" />
	<style type="text/css">
	<!--
	body {
		background-color: #0d0213;
	}
	-->
	</style>

  </head>
  
<body>
<div id="containercon">
     <div id="container">
             <div id="header">
                  <div id="logincontainer">
                      <div id="bulletincon">
                         	<s:include value="headnews.jsp"></s:include>
                      </div>
                      <!--bulletincon-->
                     	<s:include value="headlogin.jsp"></s:include>
                  </div>
                  <!--logincontainer--> 
                  <div id="navcontainer">
                      <s:include value="headmenu2.jsp"></s:include>
                  </div>
                  <!--navcontainer-->
             </div>
             <!--header-->
             <div id="contenthelp">
                  <div id="helptop">
                           
                            <table cellpadding="0" cellspacing="0" border="0" width="580px">
	                       		<tr>
	                       			<td align="left" width="50%"><div class="passwordtitle"><img src="images/helptitle_bg.jpg"/></div></td><td align="right" valign="bottom" width="50%"><a href="javascript:history.back()" style="color:#b20047;">返回</a></td>
	                       		</tr>
	                       </table>
                           <p class="ifpassword">一、注册用户：</p>
                           <p class="passwordtxt">请点击“免费开户”进入用户注册页面，按要求提示进行用户注册。</p>
                           <p class="ifpassword">二、客户端下载安装步骤：</p>
                           <p class="passwordtxt">1 点击“软件下载”按钮，弹出下面的对话框，点击“保存”，下载天威真人娱乐场客户端安装程序。</p>
                           <div class="helpimg"><img src="images/help_01.jpg" /></div> 
                   </div>
                   <!--helptop-->         
                   <div id="helpmidtxt">
                           <p class="passwordtxt setup">2 下载完毕后点击“运行”，进行客户端的安装。安装过程中连续点击“下一步”，最后点击完成即可。</p>
                           <div class="helpimgtxt"><img src="images/help_02.jpg" /></div>
                           <div class="helpimg"><img src="images/help_03.jpg" /></div>
                           <p class="passwordtxt setup">3．安装完成后，桌面会出现elufa casino 的图标。双击图标打开客户端，填入您在天威的用户名及密码“登陆”即可。请先详细阅读用户协议书，“用户协议书”右侧的方框须打勾，否则会被拒绝登陆。如还没有注册用户，请先点击 “新用户注册请按此”。</p>
                           <div class="helpimgtxt"><img src="images/help_04.jpg" /></div>
                           <p class="ifpassword">三．游戏流程：</p>
                           <p class="passwordtxt">1.登陆成功后，将会进入“选择游戏台”主界面。左边是游戏的选择列表。</p> 
                           <div class="helpimgtxt"><img src="images/help_05.jpg" /></div>
                           <div class="helpimg"><img src="images/help_06.jpg" /></div>
                           <p class="passwordtxt setup">3.游戏主界面</p> 
                           <div class="helpimgtxt"><img src="images/help_08.jpg" /></div>
                           <p class="interface">A、当进入真人游戏后，用户会在画面上见到本桌的荷官;视频下面的“闲”及“庄”会清楚显示当局闲与庄的点数。</p>
<p class="interface">B、“下注剩余时间”当每一局结束后，系统会自动倒数，客户需要在倒数时间内投注，倒数完毕将不接受投注。</p>
<p class="interface">C、在这“信息框”内会清楚显示本桌可投注的上、下限：上限（最大限红）、下限（最小投注额）、荷官的名字、局数（当次游戏的编号）、玩家（客户名称）、座号（客户在本桌中的座位）、游戏状态（分别会有：请投注、请等待、游戏结束、荷官发牌、本局结束） 。</p>
<p class="interface">D、在“牌路”内分别设有珠盘、大路、大眼仔、小路、曱甴路等，供用户参考，各项均记载每局的结果。客户可以选择拉宽查看或点击Θ隐藏。为增加游戏趣味性，在下面“请选择聊天表情”内可让客户自由选择不同的语句来表达。</p>
<p class="interface">E、“投注位置”表示客户可分别选择自己喜欢投注“和”或“庄”及“闲”的位置。</p>
<p class="interface">F、当每一局结束后，结果会在前出现“闲赢”或者“庄赢”,如遇“和”局会有声音提示，及取回其它投注在庄或闲位置之金额。</p>
<p class="interface">G、是本桌的座位,将会显示客户在游戏中的座号;客户投注时必须将筹码放在与座号相应的位置。</p>
<p class="interface">H、是客户当前可用的投注金额。右边是不同额度的筹码，分别在客户的上限及下限之间选择。在图右下方分别有：确定、取消、重复、记录、设置项。客户点击筹码投在闲、庄或和便按“确定”项，如想取消当前投注，可按“取消”项：如客户想投注上一局的金额与闲、庄、和，可按“重复”项，按“记录”是客户在本台登陆后的投注记录表，按“设置”可调教音乐及音效的大小开关。</p>
<p class="interface">I、“碎筹码”是当客户账户金额不足下限投注金额时，点击“碎筹码”便可投注。但必须在本次游戏中使用，若退出再登入，点击“碎筹码”系统将会提示“你当前余额不足本次投注”。</p>
                           </p>
                   
                   </div>
                   <!--helpmidtxt--> 
                   
                   <div id="helpbottom"></div>    
             </div>
             <!--contenthelp-->
             <div id="footer">
               <s:include value="/tpl/footer.jsp"></s:include>
               <!--footermenu-->
             </div>
             <!--footer-->  
  </div>
        <!--container--> 
</div>
<!--containercon-->
</body>
</html>
