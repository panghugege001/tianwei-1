<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<!doctype html>
<html lang="en">
<head>
    <jsp:include page="/tpl/link.jsp"></jsp:include>

<link rel="stylesheet" href="css/aquarium.css">
<link rel="stylesheet" href="css/new_login.css">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1, maximum-scale=1.0" />
</head>
<body>

<div class="nl">
	<div class="nl_content">
		<div class="slider">
			<div id="login-banner" class="carousel slide carousel-fade" data-ride="carousel" data-interval="5000">
				<!-- 轮播（Carousel）指标 -->
				<ol class="carousel-indicators" style="bottom:10px;">
					<li data-target="#login-banner" data-slide-to="0" class="active"></li>
					<li data-target="#login-banner" data-slide-to="1"></li>
					<li data-target="#login-banner" data-slide-to="2"></li>
				</ol>
				<!-- 轮播（Carousel）项目 -->

				<div class="carousel-inner">

					<div class="item active">
						<a href="javascript:;" title="" target="_blank" style="background-color:#284a8b">
							<img src="images/banner/login-b1.jpg" alt="">
						</a>
					</div>
					<div class="item">
						<a href="javascript:;" title="" target="_blank" style="background-color:#284a8b">
							<img src="images/banner/login-b2.jpg" alt="">
						</a>
					</div>
					<div class="item">
						<a href="javascript:;" title="" target="_blank" style="background-color:#284a8b">
							<img src="images/banner/login-b3.jpg" alt="">
						</a>
					</div>
				</div>
			</div>
		</div>
		<div class="login_bar">
			<h4>登陆会员账号</h4>
			<div class="item">
				账号：
				<input name="in1" type="text" id="loginnames" maxlength="15" placeholder="账号" class="txt" />
			</div>
			<div class="item">
				密码：
				<input type="password" placeholder="密码" name="password"  id="password_s" class="txt">
				<input type="hidden" id="codes" />
			</div>
			<input type="button" onClick="logins()" value="立即登录" id="submits" class="sub">
		</div>
	</div>
</div>

</body>
<script src="/js/lib/jquery.lazyload-v1.9.1.min.js"></script>
<script>

	/* function getValidateCodes(){
		$.ajax({
			method: "GET",
			url: "/asp/generateVerificationCode.php?r=" + Math.random(),
			success: function(data) {
				$('#codes').val(data);
			}
		})
	}
	getValidateCodes(); */
	//玩家登陆
	function logins() {

		var loginname = $("#loginnames").val();
		
		if (loginname == "" || loginname == "账号") {
			alert("账号不能为空！");
			return false;
		}

		var password = $("#password_s").val();
		if (password == "" || password == "密码") {
			alert("密码不能为空！");
			return false;
		}

		/* var code = $("#codes").val();
		if (code == "" || code == "验证码") {
			alert("验证码不能为空！");
			return false;
		} */

		
		$.ajax({
			method: "GET",
			url: "/asp/generateVerificationCode.php?r=" + Math.random(),
			success: function(data) {
				
				$.post("${ctx}/asp/login.php", {
					"loginname": loginname,
					"password": password,
					"imageCode": data
				}, function (returnedData, status) {
					if ("success" == status)  {
						// 玩家登入成功
						if (returnedData == "SUCCESS") {
							var _data = {};
				            _data = {
				                type: 0,
				                userId: loginname,
				                name: loginname,
				                loginName: loginname
				            };
				            window.top.postMessage(JSON.stringify(_data), '*');
						} else {
							alert(returnedData);
						}
					}
				});
			}
		})

	}

</script>
</html>
