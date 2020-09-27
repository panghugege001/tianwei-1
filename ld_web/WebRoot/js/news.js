function AutoScroll(obj){
$(obj).find("ul:first").animate({
marginTop:"-20px"
},500,function(){
$(this).css({marginTop:"0px"}).find("li:first").appendTo(this);
});
}
var s = true;
var t=0;
function startli()
{
if(s) t = setInterval('AutoScroll(".news-list")',3000);
}
 
$(document).ready(function(){
	startli();
	$(".news-list").hover(function(){
	clearInterval(t);
	var s = false;
	},function(){
	s = true;
	startli();
	});
})

