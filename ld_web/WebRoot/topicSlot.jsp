<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE>
<html>
<head >
  <title>天威娱乐城-摇摇乐</title>
  <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
  <link rel="stylesheet" href="/css/topicSlot.css?v=5"/>
</head>
<body class="section-body">


<div class="banner" style="margin-bottom: -120px;">
  <img src="images/topic/slotgame-banner.jpg" width="1920" alt=""/>
</div>

<div class="main">
  <div class="ele hand-one"></div>
  <div class="ele hand-two"></div>
  <div class="section" id="j-top">
    <div class="section-hd text-center"><span class="ele sectiontop"></span></div>
    <div class="section-inner">
      <h1>天威每日摇摇乐  任务彩金轻松得</h1>
      <p>活动时间：2016.01.01 - 01.31</p>
      <p>活动对象：天威所有玩家</p>
      <p>活动内容：</p>
      <p>每天游戏E68，完成每日任务领彩金，【每日任务彩金】会直接转入【任务奖池】，【任务奖池】大于10元便可以点击【领取】，选择领至E68账户进行游戏。
        <span class="c-red">(每日完成的任务难度不同，任务奖金也不相同)</span></p>
      <div class="slot-wrapper">
        <div id="j-slotmachine-one" class="slot-col">
          <div class="slot-item icon-slot-wechat"></div>
          <div class="slot-item icon-slot-pt"></div>
          <div class="slot-item icon-slot-ttg"></div>
          <div class="slot-item icon-slot-deposit"></div>
          <div class="slot-item icon-slot-ttg"></div>
        </div>
        <div id="j-slotmachine-two" class="slot-col">
          <div class="slot-item icon-slot-wechat"></div>
          <div class="slot-item icon-slot-pt"></div>
          <div class="slot-item icon-slot-ttg"></div>
          <div class="slot-item icon-slot-deposit"></div>
          <div class="slot-item icon-slot-wechat"></div>
        </div>
        <div id="j-slotmachine-three" class="slot-col">
          <div class="slot-item icon-slot-wechat"></div>
          <div class="slot-item icon-slot-pt"></div>
          <div class="slot-item icon-slot-ttg"></div>
          <div class="slot-item icon-slot-deposit"></div>
          <div class="slot-item icon-slot-pt"></div>
        </div>
        <a href="javascript:;" id="j-start"><div class="gamepad-wrapper">
          <div class="ball"></div>
        </div></a>
        <span class="click-me"></span>
      </div>
    </div>
  </div>
  <div class="section">
    <div class="section-hd text-center"><span class="ele sectiontop"></span></div>
    <div class="section-inner">
      <p>活动规则：</p>
      <p>1.每日摇摇乐任务需玩家先进入活动页面摇取任务进行完成，完成任务后该笔任务彩金会自动转入【任务奖池】。如当天00点之后未完成任务，任务将会自动作废，每日摇摇乐任务每天一个玩家只能摇取一次，任务随机显示，放弃将自动作废。
        <br/>
        <span class="c-red">（如您当天8点摇取的任务，任务为完成一次PT次次存次次送，那您当天8点之后如有申请PT次次存次次送即为完成任务，如您当天23点59分59秒之前还未完成任务将自动作废,
        如您抽取是的PT老虎机流水达1万，那么流水计算是从您摇取了任务之后开始计算您PT老虎机里所产生的流水，之前所产生的流水以及PT其它游戏流水一率不计算在内。）</span></p>

      <p>2.提款要求：彩金无需流水可提款。</p>
      <p>3.申请方式：奖金无需申请，摇取任务完成后彩金将会自动添加到【任务奖池】。</p>
      <p>4.领取方式：【任务奖池】彩金累计大于10元，即可点击【领取】转账到天威账户中。</p>
      <p>5.任务奖金仅限游戏PT、MG /TTG、NT、QT经典老虎机、电动吃角子游戏。其他所有21点游戏，轮盘游戏、百家乐游戏、骰宝游戏、视频扑克游戏、刮刮乐游戏、纸牌游戏、桌面游戏、电子扑克、多旋转老虎机和老虎机奖金翻倍投注皆不包含在此优惠内。</p>
      <p>6.此项优惠活动只针对娱乐性质的会员，若系统检测同一人有多个帐号或不正当的投注，天威娱乐城有权无需通知玩家，直接收回红利和非法盈利额，禁用所有游戏帐号。</p>
      <p>7.天威娱乐城保留对本次活动的修改、修订和最终解释权，以及在无通知情况下修改本次活动的权利。</p>
      <p class="c-red">温馨提示：所有任务完成所需要的条件，都是在您摇取到任务后开始进行统计</p>
    </div>
  </div>

</div>
<!--首页公告弹框{-->
<div class="modal fade" id="modal-ret" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="line"></div>
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
            aria-hidden="true">&times;</span></button>
    <div class="modal-cnt">
      <p>太棒了 恭喜你获得任务：</p>
      <p class="j-ret"></p>
    </div>
  </div>
</div>

<!--}首页公告弹框-->
<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/lib/jquery.slotmachine.min.js"></script>
<script src="/js/main.js"></script>
<script>
  $(function(){
    // 自动滚屏
    var top=$('#j-top').offset().top;
    setTimeout(function(){
      $("html, body").animate({
        scrollTop: top-114
      }, 600);
    },1200);
  });

</script>
<c:choose>
  <c:when test="${session.customer==null}">
    <script>
      !function(){
        $('#j-start').click(function(){
          alert('请先登录！！！');
        });
      }();
    </script>
  </c:when>
  <c:otherwise>
    <script>
      $(document).ready(function(){
        // 获取任务列表
        slotTask.getTask();
        var tkList=slotTask.taskList;
        if(tkList.length==0){
          alert('获取任务失败');
          return;
        }

        //随机获取任务
        var winTask=tkList[getRandom(0,tkList.length,true)];
        var taskTitle=winTask.title.toLowerCase();
        winTask['taskType']='';
        if(taskTitle.indexOf('存款')!=-1){
          winTask.taskType='deposit';
        }
        if(taskTitle.indexOf('微信')!=-1){
          winTask.taskType='wechat';
        }
        if(taskTitle.indexOf('pt')!=-1){ //pt
          winTask.taskType='pt';
        }
        if(taskTitle.indexOf('ttg')!=-1){ //ttg
          winTask.taskType='ttg';
        }

        // 配置老虎机
        var $slotMachine1=$("#j-slotmachine-one"),
                $slotMachine2=$("#j-slotmachine-two"),
                $slotMachine3=$("#j-slotmachine-three"),
                index1= getIndex($slotMachine1,winTask.taskType),
                index2= getIndex($slotMachine2,winTask.taskType),
                index3= getIndex($slotMachine3,winTask.taskType),
                $modalRet=$('#modal-ret'), //结果弹框
                $retContent=$modalRet.find('.j-ret'); //弹框内容

        var option={
          active : 1,
          delay	: 450
        };

        var machine1= $slotMachine1.slotMachine(option),
                machine2= $slotMachine2.slotMachine(option),
                machine3= $slotMachine3.slotMachine({
                  active	: 1,
                  delay	: 450,
                  complete:showRet
                });

        // 开始滚动
        var $start=$('#j-start'),
                $gamepad=$start.find('.gamepad-wrapper');
        $start.click(function(){
          $gamepad.addClass('active');

          machine1.setRandomize(index1);
          machine2.setRandomize(index2);
          machine3.setRandomize(index3);

          machine1.shuffle(6);

          setTimeout(function(){
            machine2.shuffle(8);
          },350);

          setTimeout(function(){
            machine3.shuffle(10);
          },700);

          setTimeout(function(){
            $gamepad.removeClass('active');
          },350);
        });


        function getIndex(parent,type){
          return parent.find('.icon-slot-'+type).first().index();
        }
        function showRet(){
          slotTask.saveTask(winTask.id,function(data){
            if(data==='SUCCESS'){
              $retContent.html(winTask.title+'<br/>'+'任务彩金【'+winTask.giftmoney+'】元');
              $modalRet.modal('show');
              //alert('恭喜你抽到任务：【'+winTask.title+'】')
            }else{
              alert(data);
            }
          });
        }
        function getRandom(min, max,int) {
          var ret= Math.random() * (max - min) + min;
          int=int||false;
          if(int){
            ret= Math.floor(ret);
          }
          return ret;
        }
      });

      var slotTask={
        taskList:[],
        getTask:function(callback){
          $.ajax({
            url : "/asp/getTaskList.aspx",
            type : "get", // 请求方式
            dataType : "text", // 响应的数据类型
            async:false, // 异步
            success : function(data){
              var list=JSON.parse(data);
              if(Object.prototype.toString.call(list) === '[object Array]'){
                slotTask.taskList= list;
              }
            }
          });
        },
        saveTask:function(taskid,callback){
          $.post('/asp/saveUserTask.aspx',{'taskId':taskid},callback);
        }
      }
    </script>
  </c:otherwise>
</c:choose>

</body>
</html>