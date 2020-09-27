<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@page import="java.net.URL"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!-- ie 全国哀悼灰色
<style type="text/css">
html { filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1); }
</style>-->
<%
    response.setHeader("pragma", "no-cache");
    response.setHeader("cache-control", "no-cache");
    response.setDateHeader("expires", 0);
    String serverName=request.getServerName();
    if(serverName.startsWith("www")){
        serverName=serverName.substring(4);
    }

    String title="";
    if(Constants.titles.containsKey(serverName)){
        title=Constants.titles.get(serverName);
    }else{
        title=Constants.titles.get("168.tl"); // old title
    }

// 获取来源网址：
    try{
        String reqURL=request.getRequestURL().toString();
        if(session.getAttribute("referURL")==null){
            String refer = request.getHeader("referer");
            if(refer==null||refer.equals(""))
                refer=reqURL;
            URL url=new URL(refer);
            session.setAttribute("referURL",url.getProtocol()+"://"+url.getHost());

        }

    }catch(Exception e){
    }

%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="keywords" content=""/>
<meta name="description" content="天威娱乐城—电子游戏、11大平台供选择" />
<meta name="keywords" content="PT老虎机 TTG老虎机 NT老虎机 MG老虎机 QT老虎机 网上老虎机。" />
<base href="<%=request.getRequestURL()%>" />
<title>天威</title>
<title><%=title %></title>
<script>
		if(window.location.host.indexOf('lehu268.com')!=-1) {  // 百度统计代码 for SEO
			var _hmt = _hmt || [];
			(function () {var hm = document.createElement("script");hm.src = "//hm.baidu.com/hm.js?e2c663dd6a14578098a3a4c6821b51bf";var s = document.getElementsByTagName("script")[0];s.parentNode.insertBefore(hm, s);
			})();
		}
</script>

<link rel="stylesheet" href="${ctx}/css/util/reset.css?v=1219">
<link rel="stylesheet" href="${ctx}/css/util/iconfont.css?v=1219">
<link rel="stylesheet" href="${ctx}/css/util/animation.css?v=1218">
<link rel="stylesheet" href="${ctx}/css/util/common.css?v=01123">
<link rel="stylesheet" href="${ctx}/css/base.css?v=0169992"/>
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />

<!--[if lt IE 9]>
<script src="//apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
<script src="//apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script type="text/javascript" src="${ctx}/js/lib/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/base.js?v=12342"></script>
<script type="text/javascript" src="${ctx}/js/jquery.pagination.js"></script>
