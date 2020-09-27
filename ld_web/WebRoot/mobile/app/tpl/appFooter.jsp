<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%
	String infoValue=(String)session.getAttribute("infoValue4Live800");
    if(infoValue==null)infoValue="";
%>

	</body>
</html>
<script type="text/javascript" src="mobile/js/util.js"></script>
<script type="text/javascript" src="mobile/js/String.js"></script>
<script type="text/javascript" src="mobile/js/Date.js?v=1"></script>
<script type="text/javascript" src="mobile/js/MUIDate.js?v=1"></script>

<script type="text/javascript" src="mobile/js/MUIModel.js?v=10"></script>
<script type="text/javascript" src="mobile/js/Loader.js?v=5"></script>
<script type="text/javascript" src="mobile/js/UserManage.js?v=3"></script>
<script type="text/javascript" src="mobile/js/BankManage.js?v=6"></script>
<script type="text/javascript" src="mobile/js/TPPManage.js?v=20"></script>
<script type="text/javascript" src="mobile/js/SelfGetManage.js?v=2"></script>
<script type="text/javascript" src="mobile/js/SignManage.js"></script>
<script type="text/javascript" src="mobile/js/MobileManage.js?v=1202"></script>
<script type="text/javascript" src="mobile/js/MobileComboBox.js"></script>
<script type="text/javascript" src="mobile/js/MobileGrid.js?v=1"></script>
<script type="text/javascript" src="mobile/js/CSSMarquee.js?v=1"></script>
<%--<script type="text/javascript" src="mobile/js/TipsCommon.js?v=2"></script>--%>
<script type="text/javascript" src="mobile/js/self/ExperienceManage.js?v=5"></script>

<script type="text/javascript" src="mobile/js/WebApp.js?v=1202"></script>
<script type="text/javascript" src="mobile/js/FooterContent.js?v=3"></script>

<script type="text/javascript">
	window.mobileManage = new MobileManage('${ctx}/','${imgCode}');

	<%--app版提示 ios webview内不支持alert--%>
	var tipsBox=$('.j-alert1');
	var loadingBox=$('.j-alert2');
	var tipsTxt=$('.my-alert-con');
	function showTips(text){
		tipsBox.css('display','block')
		tipsTxt.html(text);
	}
	function closeTips(){
		tipsBox.css('display','none');
		tipsTxt.html('');
	}
	function showLoading(text){
		loadingBox.css('display','block')
		tipsTxt.html(text);
	}
	function closeLoading(){
		loadingBox.css('display','none');
		tipsTxt.html('');
	}
</script>
