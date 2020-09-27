<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>黄金七天</title>
  <base href="<%=request.getRequestURL()%>" />
  <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
  <link href="/css/util/reset.css" rel="stylesheet"/>
  <link href="/css/util/common.css" rel="stylesheet"/>
  <link href="css/filpclock.css" rel="stylesheet"/>
  <link href="css/home.css?v=1112" rel="stylesheet"/>
</head>
<body>
<div id="header">
  <div class="container">
    <div class="fl">
      <a href="/" class="fl">返回首页</a>
      <a href="/asp/queryGoldBet.aspx" class="item">感恩50g黄金</a>
      <a href="/topic/live.jsp" class="item">天威宝贝秀</a>
      <a href="/topic/christmas/home.jsp" class="item">圣诞老人的礼物</a>

    </div>
    <c:if test="${session.customer==null}">
      <div class="fr">
        <a href="javascript:;" data-toggle="modal" data-target="#modal-login">登录</a>
        <span>|</span>
        <a href="javascript:;" data-toggle="modal" data-target="#modal-reg">注册</a>
      </div>
    </c:if>
  </div>
</div>
<div class="page-bg">
  <div class="text-center top-banner">
    <div class="caption"></div>
    <%--<div class="c-strong f20">距离<span class="j-w"></span>活动开始还有</div>
    <div id="j-clock"></div>--%>
    <div class="date-time j-date-time" >
      <div class="item">11月01日 00:00 — 11月07日 23:59:59</div>
      <div class="item">11月08日 00:00 — 11月14日 23:59:59</div>
      <div class="item">11月15日 00:00 — 11月21日 23:59:59</div>
      <div class="item">11月22日 00:00 — 11月28日 23:59:59</div>
      <div class="item">11月29日 00:00 — 12月04日 23:59:59</div>
    </div>
    <div class="btn-apply">PT、TTG、NT、QT、MG、DT、AG 老虎机平台</div>
    <div class="line"></div>
    <div class="gift-box container">
      <div class="tit mb30">奖品排名</div>
      <div class="cfx">
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-top.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">金条50g</div>
          <div class="gift-text">终极流水王 （第1名）</div>
        </div>
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-1.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">金如意套盒15g</div>
          <div class="gift-text">一等奖（第2名）</div>
        </div>
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-2.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">黄金手链8.9g </div>
          <div class="gift-text">二等奖（第3名）</div>
        </div>
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-3.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">DW手表</div>
          <div class="gift-text">三等奖（第4~6名）</div>
        </div>
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-4.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">行车记录仪</div>
          <div class="gift-text">四等奖（第7~20名）</div>
        </div>
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-5.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">彩金188元</div>
          <div class="gift-text">五等奖（第21~50名）</div>
        </div>
        <div class="gift-item">
          <div class="gift-pic"><img src="images/lv-6.png" width="129" height="129" alt=""></div>
          <div class="gift-name c-strong">彩金88元</div>
          <div class="gift-text">六等奖（第51~100名）</div>
        </div>
      </div>
      <div class="mb40">
        <button id="j-btn-get" class="btn-get">我要抢金条</button>
      </div>
    </div>
    <div class="line mb50"></div>
  </div>

  <div class="w1000">
    <ul class="tab-hd" id="j-tabnav">
      <li class="active"><a href="#tab-one" data-toggle="tab">当周比赛详情</a></li>
      <li class="fr"><a href="#tab-two" data-toggle="tab">历史获奖资讯</a></li>
    </ul>
    <div class="tab-bd sm-pd">
      <div id="tab-one" class="tab-panel active">
        <div class="tb-tit">黄金七天 <span class="j-w-2"></span></div>
        <div class="cfx">
           <div class="fl c-strong">我的排名： <s:property value="#request.rankNow"/></div>
          <div class="fr j-w-r"></div>
         
        </div>
        <div class="m-content">
          <form action="${ctx }/asp/queryGoldBet.aspx" name="form0" id="mainform" method="post">
                <input type="hidden" name="pageIndex" value="1" />
                <input type="hidden" name="size" value="${size }" />
          <table class="table">
            <tr>
              <th>名次</th>
              <th>玩家账号</th>
              <th>流水</th>
            </tr>
            <s:iterator value="#request.page.pageContents" id="slots" status="st">
                        <tr >
                          <td><s:if test="#request.searchname == null || #request.searchname == ''">
                              <s:property value="#st.index + (10 * (#request.pageIndex-1)) + 1"/>
                            </s:if></td>
                          <td><s:property value="loginname"/></td>
                          <td><s:number name="bet" groupingUsed="true" maximumFractionDigits="2" minimumFractionDigits="2"/></td>
                          <%--<td><s:date name="startTime" format="yyyy-MM-dd"/>--%>
                            <%--~--%>
                            <%--<s:date name="endTime" format="yyyy-MM-dd"/></td>--%>
                        </tr>
               </s:iterator>
          </table>
          <div class="text-center">${page.jsPageCode}</div>
          </form>
        </div>
      </div>
      <div id="tab-two" class="tab-panel">
        <div class="weekend-list text-center mb10 j-date-time-two">
          <button data-sid="1" class="item">第一周获奖名单 </button>
          <button data-sid="2" class="item">第二周获奖名单 </button>
          <button data-sid="3" class="item">第三周获奖名单 </button>
          <button data-sid="4" class="item">第四周获奖名单 </button>
          <button data-sid="5" class="item">第五周获奖名单 </button>
        </div>
        <%--<div class="cfx">
          <div class="fl c-strong">我的排名：109</div>
          <div class="fr j-w-r">11月 1 日 00:00 — 11月 7 日 23:59:59</div>
        </div>--%>
       <%-- <div id="j-data-history" class="data-history">

        </div>--%>
       <%-- <iframe id="j-data-history"  scrolling="no"  width="100%" frameborder="0"></iframe>--%>
        <table class="table" id="j-data-history"></table>

        <ul class="pagination" id="pagination"></ul>
      </div>
    </div>

    <div class="m-content sm-pd">
      <p class="c-strong">礼品申请方式：</p>
      <p>方式一：获得实物奖励的会员我们会在次日致电通知，届时请您注意接听电话</p>
      <p>方式二：于活动结束后三日内，发送邮件至客服邮箱vip@lehu.ph申请，逾期视为放弃</p>
      <p>邮件标题：齐聚天威拼流水</p>
      <p>邮件内容：游戏账号，邮寄地址，收件人电话，收件人姓名，您的排名</p>
      <p class="c-strong">彩金申请方式：</p>
      <p>活动彩金无需申请，会在当周活动结束后24小时内派发至您的游戏账户，彩金无需流水即可提款。</p>
      <p class="c-strong">活动规则：</p>
      <p>1、活动以每七天为一个周期，依照此周期内玩家投注额计算排名，当日投注额将于次日18:00之前更新至官网，投注额最高者将获得终极流水王称号！</p>
      <p>2、此活动每周期投注额无法累积，将自动于该周期结束后清零，并重新计算，如您想参加本周期活动需点击（我要抢金条）进行报名，报名成功后将会计算投注额。</p>
      <p>3、实物奖品如需折现，将以官方价格的70%兑换，兑换彩金无需流水即可提款。</p>
      <p>4、活动期间只计算玩家在PT、TTG、NT、QT、MG、DT、AG老虎机平台投注额；其中PT平台只计算“经典老虎机”和“电动吃角子老虎机”。PT中累积奖池游戏，百家乐游戏，21点游戏，轮盘游戏，骰宝游戏，视频扑克游戏，Pontoon游戏，Craps游戏，赌场战争游戏，娱乐场Hold'em游戏，牌九扑克游戏将不计算在内。MG、DT、NT、TTG、QT、AG内所有的老虎机奖金翻倍游戏的投注都不计算在该优惠内。</p>
      <p>5、此项优惠活动只针对娱乐性质的会员，相同信息或相同玩家只能领取一次礼品或奖金。</p>
      <p>6、天威娱乐城拥有本活动的最终解释权以及随时更改活动的权利</p>
    </div>

  </div>
</div>

<!--登录{-->
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-logo"><img src="/images/logo.png" alt=""></div>
      <div class="modal-hd">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">会员登录</h4>
      </div>
      <div class="modal-bd">
        <form action="" method="post" class="ui-form">
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">用户名：</label>
            <input type="text" name="address" class="ui-ipt"  id="j-name" maxlength="20"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">密码：</label>
            <input type="password" class="ui-ipt" name="addressee" id="j-pwd"/>
          </div>
          <div class="ui-form-item">
            <label for="" class="ui-label rq-value">验证码：</label>
            <input type="text" class="ui-ipt" name="cellphoneNo" style="width: 168px;"  id="j-code" maxlength="8"/>
            <img width="78" height="34" id="j-codeimg" src="/asp/validateCodeForIndex.aspx" onclick="this.src='/asp/validateCodeForIndex.aspx?'+Math.random();" alt="">
          </div>
          <div class="ui-form-item">
            <input type="submit" class="btn btn-danger btn-block" id="j-login" value="确定" onclick="Page.login(this);"/>
          </div>
        </form>
      </div>
      <div class="modal-ft">登录时遇到任何问题，请及时联系在线客服获取帮助。 <a href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=" class="c-red link">24小时在线客服</a></div>
    </div>
  </div>
</div>
<!--}登录-->

<!--注册弹框{-->
<div class="modal fade in" id="modal-reg" tabindex="-1" role="dialog" data-modal-load aria-labelledby="myModalLabel"
     style="display: none;">
  <div class="modal-dialog lg" role="document" style="margin-top: 10px;">
    <div class="modal-content">
      <div class="modal-hd">
        <h2 class="modal-title">轻松注册</h2>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">×</span></button>
      </div>
      <div class="modal-bd" style="padding: 38px 0 0 0">
        <iframe data-src="/register.jsp" width="100%" height="500" scrolling="no" frameborder="0"
                src=""></iframe>
      </div>
    </div>
  </div>
</div>
<!--}注册弹框-->

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/base.js"></script>
<script src="js/flipclock.min.js"></script>
<script src="js/jquery.twbsPagination.min.js"></script>
<script>

  !function($,w){

    var LevelConfig={ // 日期设置
      1:['2016-11-01 00:00:00','2016-11-07 23:59:59'],
      2:['2016-11-08 00:00:00','2016-11-14 23:59:59'],
      3:['2016-11-15 00:00:00','2016-11-21 23:59:59'],
      4:['2016-11-22 00:00:00','2016-11-28 23:59:59'],
      5:['2016-11-29 00:00:00','2016-12-04 23:59:59']
    };
    var chWeek={
      1:'第一周',
      2:'第二周',
      3:'第三周',
      4:'第四周',
      5:'第五周'
    };

    var historyData=[];

    var pageObj;

    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.Format = function (fmt) { //author: meizz
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      return fmt;
    };

    function Active(){
      this.currentDate=new Date();
      //this.currentDate=new Date(Date.parse('2016-11-10 03:00:00'));

      var self=this;
      var $hDateTime=$('.j-date-time-two');


      //历史记录
      $hDateTime.find('.item').on('click',function (e) {
        var sid=$(e.currentTarget).data('sid');
        $(e.currentTarget).addClass('active').siblings('.item').removeClass('active');
        self.getHistory(sid);
      });

      var $tabNav=$('#j-tabnav');
      $tabNav.find('a').on('click',function(){
        if($(this).attr('href')=='#tab-two'){
          $hDateTime.find('.item.active').trigger('click');
        }
      });

      $('#j-data-history').on('load',function(){
        this.style.height = 0;
        this.style.height = this.contentWindow.document.body.scrollHeight + 'px';
      });

      var wkObj=this.getWeek(this.currentDate);

      if(wkObj.next){
        this.countDown(wkObj.next.startDate.getTime()/1000-this.currentDate.getTime()/1000);
      }

      this.setState(wkObj);
      this.apply();
    }

    Active.prototype={
      constructor:Active,
      /**
       * 获取活动时间信息
       */
      getWeek:function(date){
        var startDate,endDate;
        var indexNum= (function (date){
          var i=0;
          for(var p in LevelConfig){
            var rangeArr=LevelConfig[p],
                    sD=new Date(Date.parse(rangeArr[0])),
                    eD=new Date(Date.parse(rangeArr[1]));
            if(date>=sD && date<=eD){
              startDate=sD;
              endDate=eD;
              i=parseInt(p);
              break;
            }
          }
          return i;
        })(date);

        var nextObj=LevelConfig[indexNum+1];
        if(nextObj){
          return{
            indexNum:indexNum,
            startDate:startDate,
            endDate:endDate,
            next:{
              indexNum:indexNum+1,
              startDate:new Date(Date.parse(nextObj[0])),
              endDate:new Date(Date.parse(nextObj[1]))
            }
          };
        }else{
          return{
            indexNum:indexNum,
            startDate:startDate,
            endDate:endDate,
            next:null
          };
        }

      },
      setState:function(obj){
        var i=obj.indexNum;

        if(i==0){
          $.extend(obj,obj.next);
          obj.indexNum=0;
          i++;
        }

        var $w=$('.j-w'),$w2=$('.j-w-2'),$wR=$('.j-w-r'),$dateTime=$('.j-date-time')
                ,$dateTimeTwo=$('.j-date-time-two');

        // 设置第几周
        chWeek[obj.indexNum+1]&& $w.html(chWeek[obj.indexNum+1]);
        chWeek[obj.indexNum]&& $w2.html(chWeek[obj.indexNum]);
        // 设置时间段
        $wR.html(obj.startDate.Format('MM-dd hh:mm:ss')+'到'+obj.endDate.Format('MM-dd hh:mm:ss'));

        $dateTime.find('.item:eq('+(i-1)+')').addClass('active');

        // 设置历史记录
        /*if(obj.indexNum-2<0){
          $dateTimeTwo.find('.item').addClass('disabled').prop('disabled',true);
        }else{
          var tmpcurrent= $dateTimeTwo.find('.item:eq('+(obj.indexNum-2)+')').addClass('active');
          tmpcurrent.nextAll().addClass('disabled').prop('disabled',true);
        }*/
      },

      /**
       * 获取历史记录
       * @param sid 周数序号
       */
      getHistory:function(sid){
        var self=this;
        var url='';
       /* $.get('/asp/queryGoldBetData.aspx',{'sid':sid},function(data){
          $data.html(data);
        });*/

        //$data.attr('src','/asp/queryGoldBetData.aspx?sid='+sid);

        if(sid<5){
          url='data/weekend-'+sid+'.json?v=1130';
        }else{
          url='/asp/queryGoldBetByJson.aspx?sid='+sid;
        }

        $.getJSON(url,function(data){
          historyData=data;
          self.pagination();
          self.getDataByPage(1);
        });
      },
      pagination:function(){
        var self=this;
        if(pageObj){
          pageObj.twbsPagination('destroy');
        }
        pageObj = $('#pagination').twbsPagination({
          first:'第一页',
          last:'最后一页',
          prev:'上一页',
          next:'下一页',
          startPage:1,
          totalPages: 10,
          visiblePages: 10,
          onPageClick: function (event, page) {
            //console.info(page + ' (from options)');
            self.getDataByPage(page);
          }
        });
      },
      getDataByPage:function(indexPage){
        indexPage=indexPage||1;
        var startIndex=(indexPage-1)*10;
        this.builHtml(historyData,startIndex);
      },
      builHtml:function (data,startIndex) {
        var $container=$('#j-data-history');
        var thHtml='<tr><th>名次</th><th>玩家账号</th><th>流水</th><th>奖品</th><th>快递单号</th> </tr>';
        var tpl='<tr><td>{{index}}</td><td>{{loginname}}</td><td>{{money}}</td><td>{{gift}}</td><td>{{no}}</td></tr>';
        var htmlArr=[];

        var i=0,index=0;
        for (i; i < 10; i++) {
          var obj = data[startIndex+i];
          index =startIndex+i+1;
          if(!obj) break;
          htmlArr.push(tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
            if($0=='{{index}}'){
              return index;
            }
            if($0=='{{loginname}}'){
                return a(obj[$1]);
            }
            if(obj[$1]==0){
                return 0;
            }
            return obj[$1]||'';
          }));

        }
        function a(str){
            var l=str.length;
            var ret='';
            if(l-2>0){
                for (var i = 0; i < l-2; i++) {
                    ret+='*';
                }
            }

           return str.substr(0,2)+ret;
        }

        $container.html(thHtml+htmlArr.join(''));
      },
      /**
       * 申请活动
       */
      apply:function(){
        var isLogin='${session.customer!=null}'==='true';
        var $btn=$('#j-btn-get');

        $btn.on('click',function(){
          if(isLogin){
            $.ajax({
              url:"${ctx}/asp/isExistConcertRecord.aspx",
              type:"post",
              data:{},
              beforeSend:function(){
                $btn.prop("disabled",true);
              },
              success:function(data){
                if(data==null||data==""){
                  alert("报名成功");
                }else{
                  alert(data);
                }

                $btn.prop("disabled",false);
              },
              error:function(){
                alert("操作超时导致结果未知,请稍后点击");
                $btn.prop("disabled",false);
              }
            });
          }else{
            $('#modal-login').modal('show');
          }

        });
      },
      /**
       * 倒计时
       */
      countDown:function(diff,callback){
        var clock = $('#j-clock').FlipClock(diff, {
          clockFace: 'DailyCounter',
          countdown: true,
          callbacks:{
            stop:callback
          }
        });
      },
      init:function(){
        //currentDate=new Date(Date.parse(''));
      }

    };

    $(function(){
      new Active();
    });
  }(jQuery,window);


  $(function () {

  });
</script>
</body>

<script type="text/javascript">

function gopage(val) {
    document.form0.pageIndex.value=val;
    document.form0.submit();
}



</script>
</html>