$(document).ready(function(){
	//public公告滚动
	$('.public ul').carouFredSel({
		auto : true,
		direction : "top",
		prev : ".public .pre",
		next : ".public .next",
		scroll: {
			timeoutDuration: 5000
		}
	});

	//滚动到banner
	setTimeout(function(){
		$('html,body').stop(true).animate({scrollTop:700},600);
	},600);


	getTTg();//登录tt 获取
	firstGamePage(); // 最新游戏
	secondGamePage(); // 热门游戏
	thirdGamePage(); // 纸牌游戏
	fourthGamePage(); //桌面游戏
	fifthGamePage(); // 电子扑克
	sixthGamePage(); // 老虎机
	firstchangeGameid();//第一次进入该页面，默认加载一个游戏。
	
	
	oneGame();//1或3条赔付线
	nineGame();//9条赔付线
	tenGame();//10或15条赔付线
	twentyGame();//20条赔付线
	twentyfiveGame();//25条赔付线
	thirtyGame();//30条赔付线
	fourtyGame();//40或50条赔付线
	onehunGame();//100条赔付线
	onetwentyGame();//1024种方式游戏

});	
var TTplayerhandle="";
function getTTg(){
    TTplayerhandle = $("#hander").val();
    if(null==TTplayerhandle||TTplayerhandle==''||TTplayerhandle=='null'||TTplayerhandle.length<1){
   	 $('#accountDiv').attr("style", "display: none;");	
   }else{ 
   $('#accountDiv').attr("style", "display:");	
	$('#j_account_btn').attr("href", "http://ams-games.ttms.co/userinfo/servlet/TaskServlet?taskId=5001&jTransform=true&seq=1&formatType=cmahtml&playerHandle="+TTplayerhandle+"&lang=zh-cn&devicetype=web");
}
}

var dtCtr=1;
/*新游戏*/
function firstGamePage() {
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	var gameNames = ["惹火的自由旋转", "疯狂的猴子", "西游记", "快抓钱2", "银狮奖",
					"金海豚","十二生肖","福星高照","招财进宝", "捕蝇大赛", "火辣金砖",
					"失落的神庙"];
	var gameId = ["1015","1016","1004","1008","1007","1003",
					"1002","1001","1000","526","533","484"]

	var gameCodes = ["RedHotFreeSpins&gameType=0", "MadMonkey&gameType=0", "JourneyWest&gameType=0", "CashGrabII&gameType=0", "SilverLion&gameType=0",
					"DolphinGold&gameType=0", "ZodiacWilds&gameType=0","FuStar&gameType=0", "FortunePays&gameType=0", "FrogsNFlies&gameType=0",
					"ChilliGold&gameType=0", "LostTemple&gameType=0"];
					
					
					
	appendItems("tab_new_games", gameNames, gameId, gameCodes);
}
// 热门游戏
function secondGamePage() {
	// Game names and game code index are the same 132items
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	var gameNames2 = ["天使的触摸","爱情博士","疯狂的猴子","酒吧门铃"," 终极德州扑克",
					  "龙8","鳄鱼老虎机","水果狂欢","外道姬"];
					  
	var gameId = ["477","492","527","473","479","423",
					"490","411","515"]
					
	var gameCodes2 = ["AngelsTouch&gameType=0", "DoctorLove&gameType=0", "MadMadMonkey&gameType=0", "BarsAndBellsSlots&gameType=0", "UltimateTexasHoldem&gameType=0", 
					"Dragon8sSlots&gameType=0", "Crocodopolis&gameType=0","FruitParty&gameType=0", "SamuraiPrincess&gameType=0"];
	appendItems("tab_hot_games", gameNames2, gameId, gameCodes2);
}
// 纸牌游戏
function thirdGamePage() {
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	var gameNames3 = ["百家乐","21点","加勒比梭哈扑克","欧洲21点","扑克摊牌",
					  "幸运7的21点","牌九扑克","红狗","单副牌21点","三张牌扑克",
					  "赌场大战","无佣金百家乐","德州扑克奖金","完美对子21点","加勒比海扑克"];

	var gameId = ["13","5","6","38","436","25",
					"14","44","58","32","34",
					"448","457","454","469"]
					
	var gameCodes3 = ["Baccarat&gameType=0", "Blackjack&gameType=0", "CasinoStudPoker&gameType=0", "EuroBlackjack&gameType=0", "HoldemShowdown&gameType=0",
					"Lucky7Blackjack&gameType=0", "PaigowPoker&gameType=0","RedDog&gameType=0", "SDBlackjack&gameType=0", "ThreeCardPoker&gameType=0",
					"CasinoWar&gameType=0", "NoCommissionBaccarat&gameType=0", "TexasHoldEmBonus&gameType=0", "PerfectPairsBlackjack&gameType=0", "CaribbeanStudPoker&gameType=0"];
	appendItems("tab_card_games", gameNames3, gameId, gameCodes3);
}
// 桌面游戏
function fourthGamePage() {
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	var gameNames4 = ["大众轮盘赌", "骰子", "欧式轮盘（单零轮盘）", "轮盘"];

	var gameId = ["400","7","23","8"]
					
	var gameCodes4 = ["CommonDrawSingleZeroRoulette&gameType=0", "Craps&gameType=0", "EuroRoulette&gameType=0", "Roulette&gameType=0"];
	
	appendItems("tab_desktop_games", gameNames4, gameId, gameCodes4);
}
// 电子扑克 
function fifthGamePage() {
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	var gameNames5 = ["全美模式", "狂野派", "杰克高手","王牌扑克","扑克挑战赛",
					"Play4全美模式", "Play4狂野派","Play4杰克高手", "Play4小丑扑克"];

	var gameId = ["4","2","1","3","410","408",
					"406","405","407"]
					
	var gameCodes5 = ["VideoPoker&gameType=4", "VideoPoker&gameType=2", "VideoPoker&gameType=1","VideoPoker&gameType=3", "ChallengeVideoPoker&gameType=0",
					  "Play4_All_American&gameType=0", "Play4_Deuces_Wild&gameType=0", "Play4_Jacks_or_Better&gameType=0","Play4_Joker_Poker&gameType=0"];

	appendItems("tab_elect_porker_games", gameNames5, gameId, gameCodes5);
}
// 老虎机
function sixthGamePage() {
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	// Game names and game code index are the same
	// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
	//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
	var gameNames6 = ["快抓钱","奔向黄金","幸运樱桃","海盗宝库","金罐子",
					  "太空入侵","幸运大转盘","狂野西部","足球赛事",
					  "亚瑟寻宝之旅I","亚瑟寻宝之旅II","牛眼钞票","现金地狱","赛车快道",
					  "水果狂欢","一杆进洞","好莱坞明星","啤酒节","亚马逊奇遇",
					  "奇妙嬴现金","头彩假日","预言家","冲浪","大卡西尼",
					  "魔法森林","法老的宝藏","阿拉丁神迹","小丑杰斯特","酒吧门铃",
					  "樱花奖","爱情博士","蝴蝶","独角兽传奇","万恶旋转",
					  "天神宙斯","吸血鬼大战狼人","龙8","淘金决战","淑女魅力",
					  "猴恋","海王星的黄金","极地财宝","维多利山脊","威尼斯万岁",
					  "疯狂木乃伊","福星高照","罗马角斗场的呼唤","独角兽传奇","警匪追逐",
					  "炽焰火1山","鳄鱼老虎机","威尼斯玫瑰","爱尔兰之眼","耍蛇者",
					  "塞伦盖提之钻","计程车!","最大现金特工奖","浆果炸弹","吉李：赏金猎人",
					  "三重红利幸运大转盘","天使的触摸","金海豚","疯狂的猴子","招财进宝",
					  "失落的神庙","火辣金砖","外道姬","幕府摊牌","天龙火焰",
					  "疯狂的亚马逊","快抓钱2","招财猫8","捕蝇大赛"];

	var gameId = ["391","57","17","9","12",
					"16","300","11","401","63",
					"462","64","40","10","411",
					"18","15","65","414","437",
					"413","446","449","453","452",
					"481","525","518","473","489",
					"492","519","482","474","486",
					"480","423","472","439","421",
					"416","424","468","438","428",
					"1001","495","482","506","528",
					"490","493","487","524","478",
					"516","466","444","440","447",
					"477","1003","527","1000","484",
					"533","515","483","475","467",
					"1008","540","526"]
					
					
	var gameCodes6 = ["GenericSlots&gameType=21","GenericSlots&gameType=10","GenericSlots&gameType=11","GenericSlots&gameType=17","GenericSlots&gameType=19",
					  "GenericSlots&gameType=20","GenericSlots&gameType=18","GenericSlots&gameType=16","GoooalSlots&gameType=0","ArthursQuest&gameType=0",
					  "ArthursQuestIISlots&gameType=0","BullsEyeBucks&gameType=0","FiveReelSlots&gameType=0","FastTrack&gameType=0","FruitParty&gameType=0",
					  "HoleInOne&gameType=0","HollywoodReels&gameType=0","Oktoberfest&gameType=0","AmazonAdventureSlots&gameType=0","FanCashticSlots&gameType=0",
					  "JackpotHolidaySlots&gameType=0","FortuneTellerSlots&gameType=0","SurfsUpSlots&gameType=0","TheGreatCasiniSlots&gameType=0","MagicalGroveSlots&gameType=0",
					  "RamessesRiches&gameType=0","AladdinsLegacy&gameType=0","JokerJester&gameType=0","BarsAndBellsSlots&gameType=0","CherryBlossoms&gameType=0",
					  "DoctorLove&gameType=0","Butterflies&gameType=0","UnicornLegend&gameType=0","SinfulSpinsSlots&gameType=0","ThunderingZeus&gameType=0",
					  "VampiresVsWerewolves&gameType=0","Dragon8sSlots&gameType=0","GoldRushShowdown&gameType=0","LadysCharmsSlots&gameType=0","MonkeyLoveSlots&gameType=0",
					  "NeptunesGoldSlots&gameType=0","PolarRichesSlots&gameType=0","VictoryRidgeSlots&gameType=0","VivaVeneziaSlots&gameType=0","WildMummySlots&gameType=0",
					  "FuStar&gameType=0","CallOfTheColosseum&gameType=0","UnicornLegend&gameType=0","Bobby7s&gameType=0","HotHotVolcano&gameType=0",
					  "Crocodopolis&gameType=0","VenetianRose&gameType=0","IrishEyes&gameType=0","TheSnakeCharmer&gameType=0","SerengetiDiamonds&gameType=0",
					  "Taxi&gameType=0","AgentMaxCashSlots&gameType=0","BerryBlastSlots&gameType=0","KatLeeSlots&gameType=0","TBSpinNWinSlots&gameType=0",
					  "AngelsTouch&gameType=0","DolphinGold&gameType=0","MadMadMonkey&gameType=0","FortunePays&gameType=0","LostTemple&gameType=0",
					  "ChilliGold&gameType=0","SamuraiPrincess&gameType=0","ShogunShowdown&gameType=0","DracosFire&gameType=0","AmazonWild&gameType=0",
					  "CashGrabII&gameType=0","Fortune8Cat&gameType=0","FrogsNFlies&gameType=0"];
	appendItems("tab_slot_games", gameNames6, gameId, gameCodes6);
}



//1或3条赔付线
	function oneGame(){

		var gameNames7 = ["快抓钱","奔向黄金","幸运樱桃","海盗宝库","金罐子",
					  "太空入侵","幸运大转盘","狂野西部","足球赛事"];
	
		var gameId = ["391","57","17","9","12",
					  "16","300","11","401"]
						
		var gameCodes7 = ["GenericSlots&gameType=21","GenericSlots&gameType=10","GenericSlots&gameType=11","GenericSlots&gameType=17","GenericSlots&gameType=19",
					  "GenericSlots&gameType=20","GenericSlots&gameType=18","GenericSlots&gameType=16","GoooalSlots&gameType=0"];
	
		appendItems("tab_onePay", gameNames7, gameId, gameCodes7);
	}	
//9条赔付线			
	function nineGame(){
		var gameNames8 = [ "亚瑟寻宝之旅I","亚瑟寻宝之旅II","牛眼钞票","现金地狱","赛车快道",
					       "水果狂欢","一杆进洞","好莱坞明星","啤酒节"];
	
		var gameId = ["63","462","64","40","10","411",
					  "18","15","65"]
						
		var gameCodes8 = ["ArthursQuest&gameType=0","ArthursQuestIISlots&gameType=0","BullsEyeBucks&gameType=0","FiveReelSlots&gameType=0","FastTrack&gameType=0","FruitParty&gameType=0",
					      "HoleInOne&gameType=0","HollywoodReels&gameType=0","Oktoberfest&gameType=0"];
	
		appendItems("tab_ninePay", gameNames8, gameId, gameCodes8);
		
		
		
	}
//10或15条赔付线		
	function tenGame(){

		var gameNames9 = [ "中子星","亚马逊奇遇","奇妙嬴现金","头彩假日","预言家","冲浪",
						   "大卡西尼","魔法森林"];
	
		var gameId = ["1031","414","437","413","446","449","453","452"]
						
		var gameCodes9 = ["NeutronStar&gameType=0","AmazonAdventureSlots&gameType=0","FanCashticSlots&gameType=0","JackpotHolidaySlots&gameType=0","FortuneTellerSlots&gameType=0","SurfsUpSlots&gameType=0",
			 			  "TheGreatCasiniSlots&gameType=0","MagicalGroveSlots&gameType=0",];
	
		appendItems("tab_tenPay", gameNames9, gameId, gameCodes9);
		
		
	}
//20条赔付线		
	function twentyGame(){

		var gameNames10 = ["法老的宝藏","阿拉丁神迹","小丑杰斯特","酒吧门铃",
					  "樱花奖","爱情博士","蝴蝶","独角兽传奇","万恶旋转",
					  "天神宙斯","吸血鬼大战狼人","龙8","淘金决战","淑女魅力",
					  "猴恋","海王星的黄金","极地财宝","维多利山脊","威尼斯万岁",
					  "疯狂木乃伊"];
	
		var gameId = ["481","525","518","473","489",
					  "492","519","482","474","486",
					  "480","423","472","439","421",
					  "416","424","468","438","428"]
						
		var gameCodes10 = ["RamessesRiches&gameType=0","AladdinsLegacy&gameType=0","JokerJester&gameType=0","BarsAndBellsSlots&gameType=0","CherryBlossoms&gameType=0",
       					   "DoctorLove&gameType=0","Butterflies&gameType=0","UnicornLegend&gameType=0","SinfulSpinsSlots&gameType=0","ThunderingZeus&gameType=0",
       					   "VampiresVsWerewolves&gameType=0","Dragon8sSlots&gameType=0","GoldRushShowdown&gameType=0","LadysCharmsSlots&gameType=0","MonkeyLoveSlots&gameType=0",
       					   "NeptunesGoldSlots&gameType=0","PolarRichesSlots&gameType=0","VictoryRidgeSlots&gameType=0","VivaVeneziaSlots&gameType=0","WildMummySlots&gameType=0"];
	
		appendItems("tab_twentyPay", gameNames10, gameId, gameCodes10);
		
		
	}	
//25条赔付线	
	function twentyfiveGame(){

		var gameNames11 = ["福星高照","罗马角斗场的呼唤","独角兽传奇","警匪追逐",
					  "炽焰1火山","鳄鱼老虎机","威尼斯玫瑰","爱尔兰之眼","耍蛇者",
					  "塞伦盖提之钻","计程车!","最大现金特工奖"];
	
		var gameId = ["1001","495","482","506","528",
     				  "490","493","487","524","478",
      				  "516","466"]
						
		var gameCodes11 = ["FuStar&gameType=0","CallOfTheColosseum&gameType=0","UnicornLegend&gameType=0","Bobby7s&gameType=0","HotHotVolcano&gameType=0",
      					   "Crocodopolis&gameType=0","VenetianRose&gameType=0","IrishEyes&gameType=0","TheSnakeCharmer&gameType=0","SerengetiDiamonds&gameType=0",
       					   "Taxi&gameType=0","AgentMaxCashSlots&gameType=0" ];
	
		appendItems("tab_twentyfivePay", gameNames11, gameId, gameCodes11);
		
		
	}
		
//30条赔付线		
	function thirtyGame(){

		var gameNames12 = ["浆果炸弹","吉李：赏金猎人","三重红利幸运大转盘"];
	
		var gameId = ["444","440","447"]
						
		var gameCodes12 = ["BerryBlastSlots&gameType=0","KatLeeSlots&gameType=0","TBSpinNWinSlots&gameType=0"];
	
		appendItems("tab_thirtyPay", gameNames12, gameId, gameCodes12);
		
	}
//40或50条赔付线		
	function fourtyGame(){
		// Game names and game code index are the same
		// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
		//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
		// Game names and game code index are the same
		// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
		//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
		var gameNames13 = ["天使的触摸","金海豚","疯狂的猴子","招财进宝",
       						"失落的神庙","火辣金砖","外道姬","幕府摊牌","天龙火焰"];
	
		var gameId = ["477","1003","527","1000","484",
     				  "533","515","483","475"]
						
		var gameCodes13 = ["AngelsTouch&gameType=0","DolphinGold&gameType=0","MadMadMonkey&gameType=0","FortunePays&gameType=0","LostTemple&gameType=0",
       					   "ChilliGold&gameType=0","SamuraiPrincess&gameType=0","ShogunShowdown&gameType=0","DracosFire&gameType=0"];
	
		appendItems("tab_fourtyPay", gameNames13, gameId, gameCodes13);
		
	}
//100条赔付线		
	function onehunGame(){
		// Game names and game code index are the same
		// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
		//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
		// Game names and game code index are the same
		// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
		//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
		var gameNames14 = ["疯狂的亚马逊"];
	
		var gameId = ["467"]
						
		var gameCodes14 = ["AmazonWild&gameType=0"];
	
		appendItems("tab_onehundrenPay", gameNames14, gameId, gameCodes14);
		
		
	}
//1024种方式游戏		
 	function onetwentyGame(){
		// Game names and game code index are the same
		// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
		//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
		// Game names and game code index are the same
		// (ex. gameNames[0] = "2 Ways Royal", gameCodes[0] = hljb
		//		gameNames[1] = "21 Duel Blackjack", gameCodes[1] = bj21d_mh)
		var gameNames15 = ["快抓钱2","招财猫8","捕蝇大赛"];
	
		var gameId = ["1008","540","526"]
						
		var gameCodes15 = ["CashGrabII&gameType=0","Fortune8Cat&gameType=0","FrogsNFlies&gameType=0"];
	
		appendItems("tab_onetwentyPay", gameNames15, gameId, gameCodes15);

	 }




 
var timestamp="";
function appendItems(id, gameNames,gameId,gameCodes) {
	timestamp = (new Date()).valueOf();
	var appendHTML = "";
	var html="";
	var divId = "#" + id;
	for (var ctr = dtCtr ; ctr < gameNames.length+dtCtr ; ctr++){
		/*if( ctr == 162 ) {
		continue ;	
		}*/
			appendHTML = '<dl id="dt'+ (ctr) +'">' +
							'<dt><img src="../../images/ttggames/'+ (ctr) +'.png" width="175" height="202" /></dt>' +
							'<dd class="gamename ttg_gamename">' +
								'<h3>《' + gameNames[ctr-dtCtr] + '》</h3>' +
								'<p class="btn_wp"><a href="javascript:;" class="play" onclick=changeGameId("http://ams-games.ttms.co/casino/generic/game/game.html?playerHandle='+TTplayerhandle+'&account=CNY&gameName='+gameCodes[ctr-dtCtr]+'&gameId='+gameId[ctr-dtCtr] +'&lang=zh-cn&t='+timestamp+'")   target="_blank" >进入游戏</a></p>' +
							'</dd>' +
						'</dl>';
						
			$(divId).append(appendHTML);
	}
	dtCtr= dtCtr+gameNames.length;
}
function defaultgame(){
	
	$("#PageContent").attr("src","http://ams-games.ttms.co/casino/generic/game/game.html?playerHandle=999999&account=FunAcct&gameName=CashGrabII&gameType=0&gameId=1008&lang=zh-cn&t=1436345584976");	
	
	
	}
function changeGameId(value){
	if(null==TTplayerhandle||TTplayerhandle==''||TTplayerhandle=='null'||TTplayerhandle.length<1){
		alert("请您先登录");
		return ;
	}
	/*$('#iframeid').attr("src",value);
	$('#hrefid').attr("href",value);
	$('#hrefid').attr("style", "display:");	*/
	window.open(value);
	}
	/*
	//我的账户点击事件
	$('#j_account_btn').on('click',function(){
			$('#j_account_btn').attr("href", "http://ams-games.stg.ttms.co/userinfo/servlet/TaskServlet?taskId=5001&jTransform=true&seq=1&formatType=cmahtml&playerHandle="+TTplayerhandle+"&lang=zh-cn&devicetype=web");
	});
	*/

function firstchangeGameid(){

	    	$('#hrefid').attr("style", "display: none;");	

}

function appendItemsOther(id, gameNames, gameCodes) {
	var appendHTML = "";
	var html="";
	var divId = "#" + id;
	for (var ctr = dtCtr ; ctr < gameNames.length+dtCtr ; ctr++){
			appendHTML = '<dl id="dt'+ (ctr) +'">' +
							'<dt id="dt'+ (ctr) +'"></dt>' +
							'<dd>' +
								'《' + gameNames[ctr-dtCtr] + '》' +
							'</dd>' +
							'<dd>' +
								'<a class="play" href="/gamePtPlay.php?gameCode=' + gameCodes[ctr-dtCtr] + '" target="_blank">进入游戏</a>' +
							'</dd>' +
						'</dl>';
						
			$(divId).append(appendHTML);
	}
	dtCtr= dtCtr+gameNames.length;
}

var tews='http://ams-games.ttms.co/casino/generic/game/game.html?playerHandle={{TTplayerhandle}}&account=CNY&gameName={{gamecode}}&gameId={{gameid}}&lang=zh-cn&t={{timestamp}}';