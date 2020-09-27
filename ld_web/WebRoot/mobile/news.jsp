<%@page import="dfh.utils.Constants"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%

	//禁止访问
// 	response.sendRedirect(request.getContextPath()+"/mobile/");

// 	if(session.getAttribute(Constants.mobileDeviceID)==null){
// 		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
// 	}
String cpuid=(String) request.getSession(true).getValue("cpuid");
%>
<!DOCTYPE >
<html>
<head>
	<jsp:include page="commons/back.jsp" />
	 
	
	<script type="text/javascript">
		//io_bbout_element_id should refer to the hidden field in your form that contains the blackbox
	    var fp_bbout_element_id = 'fpBB';
	    var io_bbout_element_id = 'ioBB';
	
	    // io_install_stm indicates whether the ActiveX object should be downloaded. The io_stm_cab_url
	    // should reference your signed local copy of the ActiveX object
	    var io_install_stm = false;
	    var io_exclude_stm = 12; // don't run ActiveX on any platform if it is already installed (to avoid security warnings)
	    // make sure you change the cab URL to the location of your signed copy before releasing
	    //var io_stm_cab_url               ="http://www.reallybigcasino.com/downloads/StmOCX.cab";
	    // uncomment any of the below to signal an error when ActiveX or Flash is not present
	    //var io_install_stm_error_handler = "";
	
	    var io_install_flash = false;
	    //var io_flash_needs_update_handler = "";
	    var io_enable_rip = true; // enable Real IP collection
	    $(document).ready(function () {
	    	var cpuVal = '<%=cpuid %>';
		    if(typeof(cpuVal)=="undefined"||cpuVal==null||''==cpuVal||cpuVal=='null'||!cpuVal||cpuVal.length<1){
		        var ioBBVal = $("#ioBB").val();
		        $.post("${ctx}/asp/addcpuid.aspx", {"cpuid":ioBBVal
		        }, function (returnedData, status) {
		            if ("success" == status) {}
		        });
		    }
	    });
	</script>
	
</head>
<body>
	<div class="main-wrap">
		<div class="header-margin"></div>
		  <div class="content notice-page">
			 <div class="pt40"></div>
			 <div id="j-container" class="layout-item-list"></div>

			 
		</div>
		 
	</div>

	<jsp:include page="commons/footer1.jsp" />
	
	<script>
		window.mobileManage = new MobileManage('/','/mobi/mobileValidateCode.aspx');
	</script>
	 
	<script type="text/javascript">

		headerBar.setTitle('公告中心');
		 $(function () {
			new NewsPage();
		});
		function NewsPage(){
			var that=this;
			this.tpl=[
				'<div class="layout-item">',
				'	<div class="notice-hd">',
				'		<h3>{{title}}</h3>',
				'		<p>{{time}}</p>',
				'		<i class="list-arrow iconfont icon-arrow-down"></i>',
				'	</div>',
				'	<div class="notice-bd" style="display: none;">{{content}}</div>',
				'</div>'
			].join('');

			this.$container=$('#j-container');

			this.getData=function () {
				return $.getJSON('/mobi/getAllNews.aspx');
			};
			this.buildHtml=function(data){
				var htmlArr=[];
				for (var i = 0; i < data.length; i++) {
					var obj=data[i];
					htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
						return obj&&obj[$1]||'';
					}));
				}
				return htmlArr.join('');
			};

			this.eventHandle=function () {
				$(".layout-item .notice-hd").click(function () {
					var $that=$(this);
					var $parent=$that.closest('.layout-item');
					var $content=$parent.find('.notice-bd');

					if($parent.hasClass('active')){
						$content.slideUp();
						$parent.removeClass('active');
					}else{
						$content.slideDown();
						$parent.addClass('active');
					}

				});
			};

			this.init=function () {
				var def=that.getData();

				def.done(function (data) {
					if(data.success){
						that.$container.html(that.buildHtml(data.data));

						that.eventHandle();
					}
				});
			};

			this.init();

		}

	</script>
	<!-- <script language="javascript" src="https://mpsnare.iesnare.com/snare.js"></script> -->
</body>
</html>