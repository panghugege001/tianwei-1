<%@ page language="java" pageEncoding="UTF-8"
         contentType="text/html; charset=UTF-8"%>
<%@page import="dfh.utils.Constants"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
    String cpuid=(String) request.getSession(true).getValue("cpuid");
    //String ourdeviceid=(String) request.getSession(true).getValue("device4web");
%>
<script>
    if (top.location != self.location) {top.location = self.location;}
</script>
<%--<script src="${ctx}/js/main.js"></script>--%>

<script type="text/javascript" src="${ctx}/cpu2.js"></script>
<script type="text/javascript" src="${ctx}/cpu3.js"></script>
<script type="text/javascript">

	var addr = window.location.href;
	var addrs =["www.lehu18mobile.com","www.lehu8mobile.com","www.lehu66mobile.com","www.lehu6mobile.com","www.lehu88mobile.com","www.lehumobile.com","www.lehu188mobile.com","www.lehu11mobile.com"];
	if(addr.indexOf("www.e68mobile.com")>=0 || addr.indexOf("www.lehu11.com")>=0 || addr.indexOf("www.lehu688.com")>=0){
		var index = Math.floor(Math.random()*(addrs.length)) ;
		window.location.href = "http://"+addrs[index] ;
	}

    if(/AppleWebKit.*Mobile/i.test(navigator.userAgent)
        || /Android/i.test(navigator.userAgent)
        || /BlackBerry/i.test(navigator.userAgent)
        || /IEMobile/i.test(navigator.userAgent)
        || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))){
    	var addr = window.location.href;
    	if(addr.indexOf("/mobi")>=0){

        }else if(sessionStorage['browseWay']=='mobile'){
            window.location.href="/mobile/new/index.jsp"+window.location.search;
        }else if(sessionStorage['browseWay']){
    		var $el = $('<div style="position: fixed;width: 2.5em;height: 2.5em;z-index: 99999;background-color: #424242;color: #fb9b08;line-height: 2.5em;text-align: center;font-size: 5em;border-radius: 50%;right: 0;bottom: 0;box-shadow: 1px 1px 10px #525151;">返回</div>');
    		$el.click(function(){
    			sessionStorage['browseWay']='mobile';
                window.location.href="/mobile/new/index.jsp"+window.location.search;
    		});
    		$('body').append($el);
    	}else{
            window.location.href="/mobile/new/index.jsp"+window.location.search;
    	}
    }
</script>


<script type="text/javascript">
    ///禁用右键
    function stopFuntion(){
        return true;
    }
    document.oncontextmenu=stopFuntion;
</script>
<script>
    var fp_bbout_element_id = 'fpBB';
    var io_bbout_element_id = 'ioBB';
    var io_install_stm = false;
    var io_exclude_stm = 12; // don't run ActiveX on any platform if it is already installed (to avoid security warnings)
    var io_install_flash = false;
    //var io_flash_needs_update_handler = "";
    var io_enable_rip = true; // enable Real IP collection

    /*function done(toFlashVars)
     {
     var attributes = {allowScriptAccess:"always"};
     swfobject.embedSWF("CpuCheck.swf", "core", "1", "1", "11.0.0","playerProductInstall.swf",{"var1":toFlashVars,"var2":"http://device.168.tl"},attributes);
     // swfobject.getObjectById("core").sendtoFlash(result);
     } */
    function showkeys(){
        var ioBBVal = $("#ioBB").val();
        $.post("${ctx}/asp/addcpuid.aspx", {"cpuid":ioBBVal
        }, function (returnedData, status) {
            if ("success" == status) {

            }
        });
    }
    //站内信
    $(document).ready(function () {
        var addr = window.location.href;
        var NEEDMODIFY = '${session.NEEDMODIFY}';
        if(NEEDMODIFY == "1" && addr.indexOf("updatePassword")<=0){
            alert("您的密码安全指数较低，请修改以保障资金安全");
            window.location.href="${ctx}/updatePassword.aspx";
            return ;
        }

        var cpuVal = $.trim($('#cpuid').val());
        if(typeof(cpuVal)=="undefined"||cpuVal==null||''==cpuVal||cpuVal=='null'||!cpuVal||cpuVal.length<1){
            showkeys();
        }
        /*var ourdevice = $.trim($('#ourdeviceid').val());
         if(typeof(ourdevice)=="undefined"||ourdevice==null||''==ourdevice||ourdevice=='null'||!ourdevice||ourdevice.length<1){
         getCpuKey(done);
         }*/
        $("#ioBBTwo").val($("#ioBB").val());
    });

</script>
<%--不需要添加,引用base.js方法即可以调用openProgressBar方法
<div class="overlay"></div>
<div id="AjaxLoading" class="showbox" style="margin-top: -60px;display: none;">
    <div class="loadingWord">
        <img src="${ctx}/images/waiting.gif">
        加载中，请稍候...
    </div>
</div>--%>

<div id="core" style="visible:false"><textarea id='cpuid' style="display: none;"><%=cpuid %></textarea></div>
<div style="display:none;">
    <input class="text medium" tabindex="2" TYPE="text" NAME="ioBB" id="ioBB" width="100" readonly>
    <input class="text medium" TYPE="text" NAME="browsertype" id="browsertype" readonly>
</div>
<input type="hidden" id="j-username" value="${session.customer.loginname}"/>





