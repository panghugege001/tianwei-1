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
			<jsp:param name="Title" value="银行卡" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<link href="/js/layer/mobile/need/layer.css?2.0" type="text/css" rel="styleSheet" id="layermcss">
	</head>

	<body>
		<div class="bank-list" id='bank-warp'>
			<!--<div class="bank-item ccb">
				<div class="name">建设银行</div>
				<div class="number">尾号8888 储蓄卡</div><i class="iconfont icon-downjiantou"></i>
			</div>-->
			<div class="bank-list-box">
				
			</div>

			<a class="bank-item add" href="/mobile/new/addbank.jsp" id='add'>
				添加银行卡<i class="iconfont icon-downjiantou"></i>
			</a>
			<div class="text-tips">
				<div class="h3"><strong>温馨提示：</strong></div>
				<p>1、真实姓名绑定之后将无法修改，且真实姓名需与银行卡姓 名一致才可进行提款。</p>
				<p>2、绑定银行卡号，可以免去您重复输入卡号的繁琐步骤。</p>
				<p>3、每个账号只可以绑定三个不同银行的银行卡号。如须解绑， 请与在线客服联系。</p>
			</div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/js/layer/mobile/layer.js"></script>
		<script>
			var htmls = [
				'<div class="bank-item ccb {{bankaddress}}">',
				'	<div class="name">{{bankname}}</div>',
				'	<div class="number">{{bankno}}</div>',
				'	<i class="iconfont icon-downjiantou"></i>',
				'	<a class="untied" carnber="{{bankno}}">解绑</a>',
				'</div>'
			].join('')
			//查询绑定银行列表
			function queryBankAll(){
				$.get('/asp/queryBankAll.aspx', function(data) {
					$(".bank-list-box").html('');
					var htmlary = []
					if(data.length>=3)$("#add").hide()
					for(var i = 0; i < data.length; i++) {
						var $html = $("<option value='" + JSON.stringify(data[i]) + "'>" + data[i].bankname + "</option>")
						htmlary.push(htmls.replace(/{{(\w+)}}/gm, function(x, $1) {
							return data[i][$1] || '';
						}))
					}
					$(".bank-list-box").prepend(htmlary.join(''));
				})
			}
			queryBankAll()

			$(document).on("click",'.untied',function(){
				var _this = this;
				var _thisNber = $(_this).attr('carnber');
				var nuber  =  _thisNber.substr(_thisNber.length-4);
				untied(nuber)
			});

			function untied(data){
	        var _html ='<div class="untied-alert">'
	                +  '<p class="warning">您是否确认解绑此银行卡(尾号'+data+')?</p>'
	                +  '<div class="tips">请输入您的卡号</div>'
	                +  '<div class="car"><span>银行卡号</span><input  type="text" data="carNber"></div>'    
	                +  '</div>' 
		        layer.open({
		            title: [
		      			'解绑银行卡',
		      			'background-color: #2c8ba3; color:#fff;'
		    		],
		            content:_html,
		            btn:['确定','取消'],
		            yes:function(index, layero){
		                // index, layero
		                var nber = $("[data='carNber']").val()
		                RemoverbackCar(nber,index)
		                // 
		            },
		            no:function(){

		            }
		        })
		    }

		    //解除绑定银行卡
		    function RemoverbackCar(bankno,index){
		        if(bankno.length == ''){
		        	alert('不能为空')
		            return;
		        }
		        $.post("/asp/unBindBankinfo.aspx", {
		            "bankno": bankno
		        }, function (result) {
		           layer.open({
		   				content:result
		   				,skin: 'msg'
		   				,time: 2 //2秒后自动关闭
		  			});
		           queryBankAll();
		           layer.close(index);
		        });
		    }

			
		</script>
	</body>

</html>