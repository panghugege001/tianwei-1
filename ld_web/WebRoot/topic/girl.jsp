<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE>
<html>
<head>
  <meta charset="UTF-8">
  <title>守护女神</title>
  <link href="/css/util/reset.css" rel="stylesheet" />
  <link href="/css/util/animation.css" rel="stylesheet" />
  <link href="/css/topic/girlstyle.css" rel="stylesheet" />
</head>
<body>
<div class="page">
  <div class="top">
    <div class="nav">
      <div class="logo">
        <a href="javascript:;"><img src="/images/topic/girl/logo.png"></a>
      </div>
      <div class="menu">
        <ul>
          <li><a href="http://chat.l8servicee68.com/chat/chatClient/chatbox.jsp?companyID=454&configID=23&lan=zh&jid=&info=">在线客服</a></li>
          <!-- <li><a href="/asp/bbsIndex.aspx">官方论坛</a></li> -->
          <li><a href="/">返回官网</a></li>
        </ul>
      </div>
    </div>
    <div class="banner">
      <div class="title"><img src="/images/topic/girl/title.png"></div>
      <div class="text">
        <p>天威女神<br>相信她的意中人是一个盖世英雄，总有一天他<br>会穿著金甲圣衣，脚踏七色云彩前來守护她！<br>她的盖世英雄会是你吗？</p>
        <h1 class="a-bounceinR">5888大奖等你来拿</h1>
      </div>
      <div class="btn">
        <button class="button1"><a href="#j-girls">守护女神 >></a></button>
        <button class="button2"><a target="_blank" href="/topic/girlsContent.html">活动详情 >></a></button>
      </div>
      <div class="img a-fadeinT"><img style="margin-left:-146px;" src="/images/topic/girl/girlimg1-1.png"></div>
    </div>
  </div>
  <div class="center">
    <div class="list">
      <ul id="j-girls">
        <li data-val="BAISHIMOLINAI">
          <div class="img">
            <img class="cover" src="/images/topic/girl/girlimg1-2cover.png">
            <img src="/images/topic/girl/girlimg1-2.png"></div>
          <div class="name">白石茉莉奈</div>
          <div class="text">生地：日本东京府<br>血型：A型 <br>身高：154cm <br>三围：35 - 24 - 35 G罩杯 <br>兴趣：料理、高尔夫、棒球 <br>出道：2013</div>
          <div class="tip">人气：<s:property value="%{#request.baishi}"/></div>
        </li>
        <li data-val="CHONGTIANXINGLI">
          <div class="img">
            <img class="cover" src="/images/topic/girl/girlimg2-2cover.png">
            <img src="/images/topic/girl/girlimg2-2.png">
          </div>
          <div class="name">冲田杏梨</div>
          <div class="text">生地：英国<br>血型：B型<br>身高：168cm<br>三围：40 - 23 - 36 K罩杯<br>兴趣：料理、武术、中文<br>出道：2011</div>
          <div class="tip">人气：<s:property value="%{#request.chongtian}"/></div>
        </li>
        <li data-val="MINGRIHUAQILUO">
          <div class="img">
            <img class="cover" src="/images/topic/girl/girlimg3-2cover.png">
            <img src="/images/topic/girl/girlimg3-2.png">
          </div>
          <div class="name">明日花綺羅</div>
          <div class="text">生地：日本东京都<br>血型：A型<br>身高：162cm<br>三围：35 - 23 - 33 G罩杯<br>兴趣：唱歌、跳舞、购物、遛狗<br>出道：2007</div>
          <div class="tip">人气：<s:property value="%{#request.mingri}"/></div>
        </li>
        <li data-val="SHENXIAOSHIZHI">
          <div class="img">
            <img class="cover" src="/images/topic/girl/girlimg4-2cover.png">
            <img src="/images/topic/girl/girlimg4-2.png">
          </div>
          <div class="name">神咲诗织</div>
          <div class="text">生地：日本京都府<br>血型：A型<br>身高：161cm<br>三围：36 - 23 - 34 G罩杯<br>兴趣：看电影<br>出道：2011</div>
          <div class="tip">人气：<s:property value="%{#request.shenxiao}"/></div>
        </li>
        <li data-val="CHAIXIAOSHENG">
          <div class="img">
            <img class="cover" src="/images/topic/girl/girlimg5-2cover.png">
            <img src="/images/topic/girl/girlimg5-2.png">
          </div>
          <div class="name">柴小圣</div>
          <div class="text">生地：日本名古屋<br>血型：A型<br>身高：160cm<br>三围：85 - 60 - 88 D罩杯<br>兴趣：唱歌、跳舞、购物<br>出道：2012</div>
          <div class="tip">人气：<s:property value="%{#request.chaixiao}"/></div>
        </li>
      </ul>
      <div class="checktip">点击上面 女神头像 进行投票</div>
    </div>
    <div class="tiptable">
      <div class="title"></div>
      
      
      <div class="list">
      
       <s:if test="#request.single != null && #request.single[0] != null">
   		<table class="table page1">
          <tbody>
          
          <tr>
            <th>守护女神名字</th>
			<th>鲜花数量</th>
			<th>总投注额</th>
			<th>排名</th>
			<th>红包优惠码</th>
			
          </tr>
          <tr>
	          <td><s:property value="%{#request.single[0].goddessname}"/></td>
	          <td><s:property value="%{#request.single[0].flowernum}"/></td>
	          <td><s:property value="%{#request.single[0].bettotal}"/></td>
	          <td><s:property value="%{#request.single[0].ranking}"/></td>
	          <td><s:property value="%{#request.single[0].couponnum}"/></td>
	         
          </tr>
          </tbody>
        </table>
       </s:if>
       
         <form name="mainform" action="${ctx}/asp/queryFlowerRanking.aspx" method="post" data-dataform>
           <table class="table page1">
             <tbody>
             <tr>
               <th>排名</th>
               <th>游戏账号</th>
               <th>贡献鲜花数</th>
             </tr>
          <s:iterator var="fc" value="%{#request.page.pageContents}"
                       status="st">
             <tr>
               <td>
                 <s:property value="#fc.ranking" />
               </td>
               <td>
                 <s:property value="#fc.loginname" />
               </td>
               <td>
                 <s:property value="#fc.flowernum" />
               </td>
             </tr>
             </s:iterator>
             </tbody>
             </table>
           <div class="pagination" >
             <input type="hidden" name="pageIndex" value="1" id="pageIndex"/>
             <input type="hidden" name="size" value="10" />
             ${page.jsPageCode}
           </div>
           </form>
      </div>
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

<script src="/js/lib/jquery-1.11.2.min.js"></script>
<script src="/js/base.js"></script>
<script>
  var pageInfo= {
    'pageIndex':1,
    'size':10
  };

  $(function(){
    var $girls=$('#j-girls');
    var isLogin='${session.customer!=null}'==='true';

    $girls.find('li').on('click',apply);

    function apply(e){
      if(isLogin){
        var name =$(e.currentTarget).data('val');
        if(!name) return;
        $.post("/asp/goddessApply.aspx", {
          "goddess":name
        }, function (returnedData, status) {
          if ("success" == status) {
            alert(returnedData);
          }
        });
      }else{
        $('#modal-login').modal('show');
      }

      return false;
    }

    if(isLogin){
      $.post("/asp/getGoddessApply.aspx", function (data) {
        $girls.find('.name').each(function(i,e){
          var _target=$(e);
          if(data.indexOf(_target.text())!=-1){
            _target.closest('li').addClass('active');
            _target.siblings('.img').addClass('a-swing');
          }
        });
        // alert(data);

      });
    }

  });


  function gopage(val) {
    document.mainform.pageIndex.value = val;
    document.mainform.submit();
  }
</script>

</body>
</html>