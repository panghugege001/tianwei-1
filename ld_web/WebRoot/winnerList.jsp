<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta charset="UTF-8">
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="/css/winnerList.css?v=1230">
</head>
<body>

<div class="winner-bg">
  <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
  <div class="container">
    <div class="time-line"></div>
    <div class="aside-nav fl" id="myScrollspy">
      <ul class="nav nav-tabs nav-year" id="j-nav">
      	<li><a href="#20173">2017年3月</a></li>
        <li><a href="#20172">2月</a></li>
        <li class="active"><a href="#20171">1月</a></li>
        <li><a href="#201612">2016年12月</a></li>
        <li><a href="#201611">11月</a></li>
        <li><a href="#201610">10月</a></li>
        <li><a href="#201609">9月</a></li>
        <li><a href="#201608">8月</a></li>
        <li><a href="#201607">7月</a></li>
       
      </ul>
    </div>
    <div class="winner-list" id="j-container"></div>
  </div>
</div>

<script id="tpl" type="text/html">
  <a class="winner-item" {{link}} target="_blank">
    <div class="winner-hd">
      <div class="winner-pic">
	  	<span class="pic"></span>
        <div class="winner-pic-wp"><div class="pic-box"><img src="{{srcshot1}}" alt=""></div></div>
      </div>
      <div class="winner-brief">
        <p class="text">恭喜{{location}}玩家{{winner}}在{{type}}平台《{{gamename}}》</p>
        <h2>喜获<strong>￥{{win}}</strong></h2>
        <div class="fund-pic" data-toggle="modal" data-target="#modal-show">
          <img src="{{srcshot2}}" class="w" alt="">
          <div class="fund-overlay"></div>
          <div class="fund-text"><i class="iconfont icon-search"></i> <p>查看出款详情</p></div>
        </div>
      </div>
    </div>
    <div class="winner-bd">
      中奖回顾： <br>
      {{desc}}
    </div>
  </a>
</script>

<div class="modal fade" id="modal-show" tabindex="-1" role="dialog"  style="display: none;">
  <div class="modal-dialog" role="document" style="margin-top: 80px;">
    <div class="modal-content">
      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      <div style="padding-top: 20px" class="text-center">  <img src="" style="max-width: 100%"  alt=""></div>
    </div>
  </div>
</div>


<script>
  !function(){
    var htmlArr=[],
            tpl=$('#tpl').html(),
            $container=$('#j-container'),
            cacheDate={},
            imgUrl='/images/winnerList/srcshot/',
            isRow;

    var getData=!function(){
      $.getJSON('/data/winner.json?v=28',function(data){
        for (var i = 0; i < data.length; i++) {
          var obj=data[i],
                  date=new Date(Date.parse(obj.date)),
                  dateStr=date.getFullYear().toString()+(date.getMonth()+1).toString(),
                  isRow=false;

          if(!cacheDate[dateStr]) {
            isRow=true;
            cacheDate[dateStr]=dateStr;
            htmlArr.push('<div id="'+dateStr+'">');
          }
          obj.srcshot1='';
          obj.srcshot2='';
          if(obj.srcshot[0]){
            obj.srcshot1=obj.srcshot[0].indexOf('://')!=-1?obj.srcshot[0]:imgUrl+obj.srcshot[0];
          }
          if(obj.srcshot[1]){
            obj.srcshot2=obj.srcshot[1].indexOf('://')!=-1?obj.srcshot[1]:imgUrl+obj.srcshot[1];
          }

          obj.link=obj.link?' href="'+obj.link+'" ':'';
          htmlArr.push(tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
            return obj&&obj[$1]||'';
          }));

          htmlArr.push(checkNext(data[i+1]));
        }
        $container.html(htmlArr.join(''));
      })
    }();

    function checkNext(obj){
      if(obj){
        var date=new Date(Date.parse(obj.date));
        var dateStr=date.getFullYear().toString()+(date.getMonth()+1).toString();
        if(cacheDate[dateStr]){
          return ''
        }else{
          return '</div>';
        }
      }
      return '</div>';
    }

    var showModal=!function(){
      var $modal=$('#modal-show'),$img=$modal.find('img');
      $(document).on('click','.fund-pic',function(e){
        var $target=$(e.currentTarget);
        $img.attr('src',$target.find('img').attr('src'));
        //$modal.show();
        return false;
      });
    }();

    var stick=!function(){
      var $sidebar=$('#myScrollspy');
      var offset = $sidebar.offset();
      $(window).scroll(function() {
        if ($(window).scrollTop() > offset.top) {
          $sidebar.css({position: 'fixed',top:'80'});
        } else {
          $sidebar.css({position: 'static'});
        }
      });
    }();

    $('body').scrollspy({ target: '#myScrollspy' });

  }();

  function NumberUpperFormat(input) {
    // num - 位数
    // 简单理解后面要有三个0，则是千，4个零，则是万。当然不一定是零，位数到了就行，反正都会省略掉（未做四舍五入）
    // 可以随意的增删改units内容实现单位的配置
    const units = [
      {num: 3, unit: '千'},
      {num: 4, unit: '万'},
      {num: 6, unit: '百万'},
      {num: 7, unit: '千万'},
      {num: 8, unit: '亿'}
    ];
    // 精确到整数，直接用js自带方法input.toFixed(0)也可以
    const num = input.toFixed(0);
    if (num.length <= 4) {
      // 项目相关需求，万以下不加单位
      return num;
    }
    // 保证前面至少留一位
    const len = num.length - 1;
    var isFind = false;
    for (var i = 0, length = units.length; i < length; i++) {
      var item = units[i];
      if (len >= item.num && len < units[i + 1].num) {
        isFind = true;
      } else if (i === (length - 2)) {
        isFind = true;
        item = units[++i];
      }
      if (isFind) {
        // 确认区间后，返回前几位加上单位
       // return ;
        return num.slice(0,num.length-item.num)+item.unit;
      }
    }
  }
</script>

</body>
</html>