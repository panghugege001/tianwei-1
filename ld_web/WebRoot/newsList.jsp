<%@ page language="java" pageEncoding="UTF-8" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<%
    String infoValue=(String)request.getSession(true).getValue("infoValue4Live800");
    if(infoValue==null)infoValue="";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=request.getRequestURL()%>" />
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>

</head>
<style>
	.P_btn{
    background: #dfa85a;
    color: #fff;
    border-radius: 15px;
    height: 40px;
    line-height: 40px;		
	}
	.gb-title{
    height: 40px;
    line-height: 40px;
    padding-left: 15px;
    padding-right: 12px;
    text-align: left;
    color: #fff;
    color: #7e7e7e;
    width: 76%;
    margin-left: 16px;
    border-radius: 15px;
    text-align: center;
    /* margin-top: 20px; */
    background: #cccccc;	
	}
.bread-crumb{
	background: #FFFFFF;
    height: 40px;
    border-bottom: 2px solid #2c8ba3;
    margin-left: 45px;
    margin-top: 38px;    	
}
.bread-crumb ul li{
    background: #2c8ba3;
    color: #FFFFFF;
    float: left;
    width: 150px;
    height: 40px;
    line-height: 40px;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;	
	padding-left: 16px;    
    
}
#j-result ul li{
	list-style: none;
    border-bottom: 1px solid #ccc;
}	

.pagination a {
    display: inline-block;
    font-size: 14px;
    padding: 0 15px;
    background: #dfa85a;
    color: #fff;
    border-radius: 6px;
}

.m-content font{
	font-size: 16px !important;
}	
</style>
<body>

<div class="index-bg">
    <jsp:include page="${ctx}/tpl/header.jsp"></jsp:include>

    <div class="container">
		<div class="about-main">
			<div class="gb-sidenav"  style="padding-top: 36px;">
				<div class="mb20" style="">
					<h1 class="gb-title mb">联系方式</h1>
					<ul style="padding-left: 43px; color: #747474;">
						<li class="mb">在线客服<a class="link c-strong chat-service" href='https://chatai.l8servicelongdu.com/chat/chatClient/chatbox.jsp?companyID=9044&configID=19'>点击咨询</a></li>
						<li class="mb"><p>天威邮箱:</p>tianwei661@gmail.com</li>
						 
					</ul>
				</div>
				<div class="" style="">
					<h1 class="gb-title mb">关注我们</h1>
					<div class="text-center" style="width: 80%; padding-left: 23px;">
						<img src="/images/appxiazai/longduapp.png" alt="" class="w mb" width="138" height="138">
						<p class="P_btn">APP下载</p>
					</div>
				</div>
			</div>
			<div class="gb-main-r">
			<div style="padding-left: 50px;">
				<div class="bread-crumb">
					<ul>
						<li class="active">
							<a>新闻公告</a>
						</li>
					</ul>
				</div>					
			</div>			
				<div class="m-content" style="font-size: 16px;">
						<div id="j-result"></div>
						<div id="j-pagination"></div>
				</div>
			</div>
		</div>
    </div>
</div>


<jsp:include page="${ctx}/tpl/footer.jsp"></jsp:include>
<script src="/js/jquery.pagination.js"></script>
<script>
    $(function () {
        var pageIndex = 0;     //页面索引初始值
        var pageSize = 10;     //每页显示条数初始化，修改显示条数，修改这里即可

        var totleNo;
        $.ajax({
            type: "POST",
            dataType: "text",
            url: '/queryRecod/queryVoTotleNo.aspx',      //提交到一般处理程序请求数据
            async:false,
            success: function(data) {
                totleNo=data;
            }
        });

        InitTable(0);    //Load事件，初始化表格数据，页面索引为0（第一页）
        //分页，PageCount是总条目数，这是必选参数，其它参数都是可选
        $("#j-pagination").pagination(totleNo, {
            callback: PageCallback,  //PageCallback() 为翻页调用次函数。
            prev_text: "« 上一页",
            next_text: "下一页 »",
            items_per_page:pageSize,
            num_edge_entries: 2,       //两侧首尾分页条目数
            num_display_entries: 6,    //连续分页主体部分分页条目数
            current_page: pageIndex   //当前页索引
        });

        //翻页调用
        function PageCallback(index, jq) {
            InitTable(index);
        }


        //请求数据
        function InitTable(pageIndex) {
            var begin =(pageIndex)*pageSize;
            $.ajax({
                type: "POST",
                dataType: "text",
                url: '/queryRecod/queryVoByPage.aspx',      //提交到一般处理程序请求数据
                data: "begin=" + begin + "&end=" + pageSize,          //提交两个参数：pageIndex(页面索引)，pageSize(显示条数)
                async:false,
                success: function(data) {
                    $('#j-result').html(data);             //将返回的数据追加到表格
                    show();
                }
            });
        }

        function show(){
            var a=$('#j-result').find('.item a');
            a.on('click',function(){
                $(this).find('.content').slideToggle();
            })
        }
    });


</script>
</body>
</html>