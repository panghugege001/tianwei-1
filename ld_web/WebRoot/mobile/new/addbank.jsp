<%@page import="dfh.utils.StringUtil"%>
<%@ page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
	if(session.getAttribute(Constants.SESSION_CUSTOMERID)==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE html>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="添加银行卡" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
	</head>

	<body>
		<div class="form-warp txt-form" id='bind-bank'>
			
			<div class="form-group">
				<label class="form-label">银行卡号</label>
				<input id="mui-bankBind-cardNo" name='' data-pattern="^\d{16,21}$" type="text" required="" class="form-control">
				<div class="ipt-clear"></div>
			</div>
			<!--<div class="c-red text-center" style="margin: 0.333333rem 0;" id='bank-name'></div>-->
			<div class="form-group zf-sele"><label class="form-label">卡类型</label>
				<div  id='bank-name' class="form-control c-red">
				</div>
				<!--<select id="mui-bankBind-bankName" class="form-control">
					<option value="">请选择</option>
					<option value="工商银行">工商银行</option>
					<option value="招商银行">招商银行</option>
					<option value="上海农村商业银行">上海农村商业银行</option>
					<option value="农业银行">农业银行</option>
					<option value="建设银行">建设银行</option>
					<option value="交通银行">交通银行</option>
					<option value="民生银行">民生银行</option>
					<option value="光大银行">光大银行</option>
					<option value="兴业银行">兴业银行</option>
					<option value="上海浦东银行">上海浦东银行</option>
					<option value="广东发展银行">广东发展银行</option>
					<option value="深圳发展银行">深圳发展银行</option>
					<option value="中国银行">中国银行</option>
					<option value="中信银行">中信银行</option>
					<option value="邮政银行">邮政银行</option>
				</select>-->
			</div>
			<!--<div class="form-group">
				<label class="form-label">姓名</label> 
				<input id="mui-bankBind-name" name='' data-pattern="\S+" type="text" required="" class="form-control">
				<div class="ipt-clear"></div>
			</div>-->
			<div class="form-group hidden">
				<label class="form-label">验证码</label>
				<input id="mui-bankBind-bindingCode" name='' type="password" required="" class="form-control">
				<div class="ipt-clear"></div>
			</div>
			<div class="form-group">
				<label class="form-label">登录密码</label>
				<input id="mui-bankBind-password" type="password" data-pattern="\S+" required="" class="form-control">
				<div class="ipt-clear"></div>
			</div>
			<div class="btn-submit " id="mui-bankBind-submit">绑定</div>
			<div class="mui-error-message c-red txt-center" style="text-align: center;">
			</div>
			<div class="text-tips">
				<div class="h3"><strong>温馨提示：</strong></div>
				<p>1、真实姓名绑定之后将无法修改，且真实姓名需与银行卡姓 名一致才可进行提款。</p>
				<p>2、绑定银行卡号，可以免去您重复输入卡号的繁琐步骤。</p>
				<p>3、每个账号只可以绑定三个不同银行的银行卡号。如须解绑， 请与在线客服联系。</p>
			</div>

		</div>
		<script>
			window.manage = new MobileManage('${ctx}/', '${imgCode}');
			var _$bankBindModel = $('#bind-bank');
			_$bankBindModel.$bankName = _$bankBindModel.find('#mui-bankBind-bankName');
			_$bankBindModel.$cardNo = _$bankBindModel.find('#mui-bankBind-cardNo');
			_$bankBindModel.$bindingCode = _$bankBindModel.find('#mui-bankBind-bindingCode');
			_$bankBindModel.$password = _$bankBindModel.find('#mui-bankBind-password');
			_$bankBindModel.$errorMessage = _$bankBindModel.find('.mui-error-message');
			_$bankBindModel.$submit = _$bankBindModel.find('#mui-bankBind-submit');

			_$bankBindModel.$cardNo.blur(function() {
				if(!/^\d{16,19}$/.test($(this).val())) {
					$("#bank-name").text('请输入正确的银行卡号')
					return;
				}
				$.post("/asp/getBankInfo.aspx", {
					bankno: $(this).val()
				}, function(data) {
					if(data && data.issuebankname) {

						$("#bank-name").text(data.issuebankname)
					} else {
						$("#bank-name").text(data)
					}
				})
			})

			_$bankBindModel.$submit.click(function() {
				var bdbankno = _$bankBindModel.$cardNo.val();
//				if(_$bankBindModel.$bankName.val() == '') {
//					alert("[提示]请选择卡类型！");
//					return false;
//				}
				if(bdbankno == "") {
					alert("[提示]银行卡号不可为空！");
					return false;
				}
				var regUrl = /^\d+$/;
//				if(!regUrl.test(bdbankno)) {
//					alert('[提示]银行卡号只能输入数字！');
//					return false;
//				}
//				if(bdbankno.length > 21 || bdbankno.length < 16) {
//					alert("[提示]银行卡号长度只能在16-21位之间");
//					return false;
//				}
				var bdbank = $("#bank-name").text().trim();
				if(bdbank == "" || bdbank.toString().indexOf("不支持") > -1) {
					alert('正在检查银行卡或不支持此银行卡');
					return false;
				}
				var formData = {
					cardNo: _$bankBindModel.$cardNo.val(),
					bankName: bdbank,
					password: _$bankBindModel.$password.val(),
					addr: 'none',
					bindingCode: _$bankBindModel.$bindingCode.val()
				};
				manage.getLoader().open("绑定中");
				manage.getBankManage().bindBankNo(formData, function(result) {
					manage.getLoader().close();
					if(result.success) {
						alert("绑定成功")
						that.close();
						window.history.back();
					} else {
						alert(result.message);
						_$bankBindModel.$errorMessage.html(result.message);
						_checkHeight(_$bankBindModel);
					}
				});
				return
			});
		</script>
	</body>

</html>