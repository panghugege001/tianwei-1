<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/sports.css?v=0133" /> 
	</head>

	<body>
		<jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>
		<div class="sports_bannar">
            <div class="ren1_box">
                <div class="game_shaba">
                    <!--<div class="game_shaba_box">
                        <a href="#" class="sbLogin">进入游戏</a> 
                    </div>-->
                    <div class="shaba_btn">
                        <ul>
                            <li><a href="javascript:;" class="pc_btn sbLogin"></a></li>
                            <li><a href="javascript:;" class="wb_btn"></a></li>
                        </ul>
                    </div>
                    <div class="shaba_code">
                        <img src="/images/appxiazai/longduapp.png" />
                        <span>扫码下载手机APP</span>					
                    </div>
                </div>
                <!-- <div class="game_pb"> -->
                    <!--<div class="game_shaba_box">
                        <a href="#" class="sbLogin">进入游戏</a> 
                    </div>-->
                    <!-- <div class="shaba_btn shaba_btn1" >
                        <ul>
                            <li><a href="${ctx}/game/PBUserLogin.aspx "  target="_blank" class="pc_btn pc_btn1 j-play"></a></li>
                            <li><a href="javascript:;" class="wb_btn2 wb_btn1" id="wb_btn2"></a></li>
                        </ul>
                    </div>
                    <div class="shaba_code1">
                        <img src="/images/appxiazai/longduapp.png" />
                        <span>扫码下载手机APP</span>					
                    </div>
                </div> -->
            </div>	
        </div>
		<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>		
	</body>
	<script>
		$(".game_shaba").on('click',function(e){
			e.stopPropagation();
			$(".shaba_code").hide();
		})
		
		$(".wb_btn").on('click',function(e){
			e.stopPropagation();
			$(".shaba_code").show();
		})
		$("#wb_btn2").on('click',function(e){
			e.stopPropagation();
			$(".shaba_code1").show();
		})
	</script>
</html>
