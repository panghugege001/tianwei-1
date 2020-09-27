var User=function(){
};
/*用户请求地址*/
User.Url={
  LOGIN_URL:'/asp/login.php', //登录
  LOGINOUT_URL:'/asp/logout.php', //退出
  AGENTREG_URL:'/asp/addAgent.aspx',　//注册代理
  GETLETTERCOUNT_URL:'/asp/getGuestbookCount.php',　//获取未读消息
  GETREMARK:'/asp/getRemarkAgain.aspx' //获取附言
};
/**
 * 用户登录
 * @constructor
 */
User.prototype.Login=function(){

};
User.prototype.Logout=function(){

};
/**
 * 代理注册
 * @constructor
 */
User.prototype.AgentReg=function(form,data,callback){
  $(form).validate({
    submitHandler:function(){
      $.ajaxForm('post',User.Url.AGENTREG_URL,data,callback);
    }
  });
};

User.prototype.GetRemark=function(bankName,depositCode,userName,callback){
  $.post(User.Url.GETREMARK,
    {"bankname":bankName,"depositCode":depositCode,"username":userName},
    callback);
};


$(function(){
  var $doc=$(document);

  var user=new User();

  /*获取附言*/
  $doc.on('click','[data-remark]',function(){
    var $this=$(this),
      bank=$this.data('remark'),
      $remarkValue=$this.siblings('.remark-value');
      user.GetRemark('','','',function(data){
        if(data.length == 5 || data.length == 4){
          alert("存款订单已生成，请用新的附言进行付款");
          $(".leaveMsg"+bankname).eq(0).html(respData);
          //$(".leaveMsg"+bankname).siblings('.fyvalue').html(respData).siblings('.copy1').attr('data-clipboard-text',respData);
          $(obj).siblings('.fyvalue').html(respData).siblings('.copy').attr('data-clipboard-text',respData);
        }else{
          alert(respData);
        }
        $('.depositCode').attr('src','/asp/depositValidateCode.aspx?r='+Math.random());
      });
  });



  var agent_form=$('#j_agent_form'),
    agent_data=agent_form.serialize();

  user.AgentReg(agent_form,agent_data,function(data){
    if(data=='SUCCESS'){
      alert("注册成功！");
      window.location.href="/";
    }else if(data=="帐号已禁用 :联系市场部门！"){
      alert(data);
      window.location.href="/";
    }else{
      var $code= $('#j_validatecode');
      $code.val('');
      $('#ipt_password').val('');
      $('#ipt_confirmpassword').val('');
      //form.confirmpassword.value='';
      //Homepage.getCode($code, $code.attr('src'));

      alert(data);
    }
  });
});
