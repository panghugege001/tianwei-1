+function ($) {
  /*validator 全局配置
   * ================*/
  $.validator.setDefaults({
    debug: false,
    errorClass:"error-tip"
/*    errorPlacement:function(error,element){
      //console.log(error)
      //error.appendTo(element.parent());
    }*/
  })

  /*validator 错误自定义信息
   * =====================*/
  $.extend($.validator.messages, {
    required   : '请输入值',
    remote     : '请修正该字段',
    email      : '请输入正确格式的电子邮件',
    url        : '请输入合法的网址',
    date       : '请输入合法的日期',
    dateISO    : '请输入合法的日期(ISO).',
    number     : '请输入合法的数字',
    digits     : '只能输入数字',
    creditcard : '请输入合法的信用卡号',
    equalTo    : '请再次输入相同的值',
    accept     : '请输入拥有合法后缀名的值',
    maxlength  : jQuery.validator.format('请输入一个 长度最多是 {0} 的值'),
    minlength  : jQuery.validator.format('请输入一个 长度最少是 {0} 的值'),
    rangelength: jQuery.validator.format('请输入 一个长度介于 {0} 和 {1} 之间的值'),
    range      : jQuery.validator.format('请输入一个介于 {0} 和 {1} 之间的值'),
    max        : jQuery.validator.format('请输入一个最大为{0} 的值'),
    min        : jQuery.validator.format('请输入一个最小为{0} 的值')
  })

  /*自定义验证规则
   * ===========*/


  //验证密码，同时为大小写字母和数字的密码
  $.validator.addMethod('password1', function (value, element) {
    var reg = /^(?![^a-zA-Z]+$)(?!\D+$).{3,16}$/
    return this.optional(element) || reg.test(value)
  }, '必须同时包含字母和数字')

  //验证密码，字母头同时为大小写字母和数字的密码
  $.validator.addMethod('password2', function (value, element) {
    var reg = /^[a-zA-Z][a-zA-Z0-9]{5,15}$/
    return this.optional(element) || reg.test(value)
  }, '由字母开头，6-16个数字或英文字母组成')

  //验证手机号码，仅仅13|15|18|14开头的11位手机号码
  $.validator.addMethod("phone", function (value, element) {
    var reg = /^\d{11}$/
    return this.optional(element) || (reg.test(value))
  }, '请正确填写您的手机号码')

  //验证邮编，6位数字
  $.validator.addMethod("zipcode", function (value, element) {
    var reg = /^\d{6}$/
    return this.optional(element) || (reg.test(value))
  }, '请正确填写正确的邮编号码')

  //验证用户名，用户名必须以a_开头，由3-15个数字或英文字母组成
  $.validator.addMethod('username',function(value,element){
    return this.optional(element)||/^a_[a-z0-9]{1,13}$/.test(value)
  },'用户名必须以a_开头，由3-15个数字或英文字母组成')

  /*
   * register.jsp
   */

  //验证用户名，小写字母与数字的组合，字母开头，6-10字符之间
  $.validator.addMethod('register-username',function(value,element) {
    return this.optional(element)||/^[a-z][a-z0-9_]{5,15}$/.test(value);
  },'由6-10个数字或英文字母组成')

  //中文名
  $.validator.addMethod('chinese-name',function(value,element) {
    return this.optional(element)||/^[\u4e00-\u9fa5,\·]+$/.test(value);
  },'只能为汉字')


  //验证手机号码
  $.validator.addMethod('register-phone',function(value,element) {
    return this.optional(element)||/^1\d{10}/.test(value);
  },'请输入正确的手机号码')

  /*data api
  * ========*/
  $(function(){
    $(document).find('form[data-validate]').each(function(){
      var validate= $(this).validate({
        submitHandler:function(form) {
          var target=$(form).attr('action')
          $.ajaxForm('post',target,$(form).serialize(),function(data){
            validate.resetForm()
            alert(data)
          })
        }
      })
    })

  })


}(jQuery);
 