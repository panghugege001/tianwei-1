<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<html>
<head>
<script type="text/javascript" src="${ctx}/mobile/js/lib/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
$(function(){
	<!-- Deeplink URL for existing users with app already installed on their device -->
	window.location.href = '<s:property value="gameUrl" escapeHtml="false"/>'; 
	<!-- Downlaod URL (MAT link) for new users to download the app -->
	
	setTimeout(function(){
		var mobileKind = '<s:property value="mobileKind"/>';
		var downloadurl = "";
		if ("Android" == mobileKind) {
			//downloadurl = "https://stgstaticcontent.azuriteapp.com/downloads/android/appPhone.apk";//測試站
			downloadurl = "https://download.angelnlive.com/android/AppPhone.apk";
		} else if ("IOS" == mobileKind) {
			//downloadurl = "itms-services://?action=download-manifest&url=https://stgstaticcontent.azuriteapp.com/Downloads/iOS/N2LiveMobile.plist";//測試站
			downloadurl = "itms-services://?action=download-manifest&url=https://download.angelnlive.com/ios/N2Live_Mobile.plist";
		}
		window.location = downloadurl;
    }, 1000);
	
});

</script>
</head>
<body>
</body>
</html>