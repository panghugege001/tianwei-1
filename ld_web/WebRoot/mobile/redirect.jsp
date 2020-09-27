<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/common.jsp" />
	</head>
<body>
	<script type="text/javascript">
		new RedirectView();
	
		function RedirectView(){
			var that = this;
			var _urlParamValue = {};
			
			
			
			_init();
			
			/**
			 * 初始化
			 */
			function _init(){
				mobileManage.getLoader().open('初始化');
				//取得GET Param
				_initUrlParamValue();
			
				 
				_doAction();
				mobileManage.getLoader().close();
			}
			
			/**
			 * 初始化
			 */
			function _doAction(){ 
				var page = _urlParamValue['page']||'';
				var username = _urlParamValue['username']||'';
				var isNotLogin = '${session.customer==null}'=='true' || '${session.customer.loginname}' != username; 
				switch(page){
					case 'fundsManage':
						if(isNotLogin){
							var lang = _urlParamValue['lang']||'';
							var ticket = _urlParamValue['ticket']||'';
							mobileManage.getUserManage().ticketLogin({account:username,ticket:ticket},function(result){
								if(result.success){
									mobileManage.redirect(page);
								}else{
									alert(result.message);//token验证失败
									mobileManage.redirect('index');
								}
							});
						}else{
							mobileManage.redirect(page);
						}
						break;
					case 'register':
						if (isNotLogin) {
							mobileManage.redirect(page);
						} else {
							mobileManage.redirect('index');
						}
						break;
					case 'preferential':
						mobileManage.redirect(page);
						break;
					case 'forgotPassword':
						if(isNotLogin){
							mobileManage.redirect(page);		
						}else{
							mobileManage.redirect('index');	
						}
						break;
					default:
						mobileManage.redirect('index');
						break;
				}
			}
		
			
			
			/**
			 * 解析Url param内容
			 */
			function _initUrlParamValue() {
				_urlParamValue = {};
				var query = window.location.search.substring(1);
				if(query.length==0)return;
				var vars = query.split("&"),pair;
				for (var i=0;i<vars.length;i++) {
					pair = vars[i].split("=");
					_urlParamValue[pair[0]] = pair[1];
				}
				query = vars = pair = null;
			}
			
			/**
			 * 产生get参数值
			 * @returns {String}
			 */
			function _getLocationSearch(){
				var search = '?';
				var param = '{0}={1}&';
				for (var i in _urlParamValue) {
					search+=String.format(param,i,_urlParamValue[i]);
				}
				search = search.length==1?'':search.slice(0,search.length-1);
				return search; 
			}
			
		}
	</script>
</body>
</html>