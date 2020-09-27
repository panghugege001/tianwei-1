<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<style type="text/css">
 
body{font-family: "Microsoft Yahei";font-size: 14px;color: #8f6a3e;} 
*{ margin:0; padding:0;}
 .w224{ width:224px; height:80px; float:left; }
 .w224 h3{ color:#fff; text-align:center; font-size:18px; font-weight:700;margin-top:14px;}
 .w224 .num{ width:193px; height:26px; line-height:26px; padding-left:2px; letter-spacing:3px; margin:10px auto 0; margin-left:15px; font-size:18px; color:#ffba00;font-family: "Microsoft Yahei"; border:1px solid #473e37; border-radius:5px; background-color:#000; text-align:center;}
</style>
 
	  <script type="text/javascript" src="http://tickers.playtech.com/jackpots/new_jackpotjs.js"></script>
      <body style="background-color:transparent">
                <div class="jiangchibox">
                	<div class="w224">
                    	<h3>海滨嘉年华</h3>
                         <div class="num">¥15<span id="jackpot-goldrallyjackpot"></span></div>
                    </div>
                    <div class="w224">
                    	<h3>水果大战</h3>
                        <div class="num">¥4<span id="jackpot-magicslots3"></span></div>
                    </div>
                    <div class="w224">
                    	<h3>玩转华尔街</h3>
                        <div class="num">¥2<span id="jackpot-wallstfever"></span></div>
                    </div>
                    <div class="w224">
                    	<h3>绿巨人</h3>
                         <div class="num">¥55<span id="jackpot-jackpotdarts4"></span></span></div>
                    </div>
                </div>
      
      
      <div>
				 
			</div>
<script type="text/javascript">
    var ticker5 = new Ticker({info: 1, local: 0, casino: 'asian', game: 'grel'}); 
        ticker5.attachToTextBox('jackpot-goldrallyjackpot');
        ticker5.SetCurrencySign('');
        ticker5.tick();
    var ticker15 = new Ticker({info: 1, local: 0, casino: 'asian', game: 'ms3'});
        ticker15.attachToTextBox('jackpot-magicslots3');
        ticker15.SetCurrencySign('');
        ticker15.tick();
    var ticker3 = new Ticker({info: 1, local: 0, casino: 'asian', game: 'wsffr'}); 
        ticker3.attachToTextBox('jackpot-wallstfever');
        ticker3.SetCurrencySign('');
        ticker3.tick();
    var ticker8 = new Ticker({info: 1, local: 0, casino: 'asian', game: 'drts4'});
        ticker8.attachToTextBox('jackpot-jackpotdarts4');
        ticker8.SetCurrencySign('');
        ticker8.tick();
</script>   
 </body>