<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/index.jsp");
	}
%>
<!DOCTYPE>
<html>
	<head>
		<jsp:include page="commons/back.jsp" />
		<link rel="stylesheet" type="text/css" href="mobile/css/email.css?v=10" />
	</head>
<body>
	<div class="main-wrap">
		<div class="header-margin" ></div>
		<div class="content notice-page">
			 <div id="j-container" class="layout-item-list"></div>

		</div>
		<div class="footer-margin" ></div>
	</div>
	<jsp:include page="commons/footer1.jsp" />
	<script type="text/javascript">
		headerBar.setTitle('站内信');
		footerBar.active('email');
		
		headerBar.scrollHide(true);
		headerBar.bind('hide',function(){
			$('.main-wrap .tab-block').css('top',0);
		});
		headerBar.bind('show',function(){
			$('.main-wrap .tab-block').css('top',headerBar.defHeight);
		});
		$(function () {
			new EmailPage();
		});
	function EmailPage(){
        var that=this;
        this.tpl=[
            '<div class="layout-item{{isRead}}" data-id="{{id}}" data-read="{{isRead}}">',
            '	<div class="notice-hd">',
            '		<h3>{{title}}</h3>',
            '		<p>{{createDate}}</p>',
            '		<i class="list-arrow iconfont icon-arrow-down"></i>',
            '	</div>',
            '	<div class="notice-bd" style="display: none;">{{content}}</div>',
            '</div>'
        ].join('');

        this.$container=$('#j-container');

        this.getData=function () {
            var formData={
                pageIndex:1,
                total:0,
                size:20
			};
            return $.getJSON('/mobi/queryEmail.aspx',formData);
        };
        
        this.buildHtml=function(data){
            var htmlArr=[];

            for (var i = 0; i < data.length; i++) {
                var obj=data[i];

                htmlArr.push(that.tpl.replace(/{{\s*?(\w+)\s*?}}/gm,function($0,$1){
 
                    if(obj){
                        if($1==='isRead'){
                          return obj[$1]===true?' read':'';
						}
                        return obj[$1]||'';
					}
                    return '';
                }));
            }
            return htmlArr.join('');
   				
        };
	
		
        this.eventHandle=function () {
           $(".layout-item .notice-hd").click(function () {
			$(this).find("h3").css({
				"color":"#4c4436"
			})
                var $that=$(this);
                var $parent=$that.closest('.layout-item');
                var $content=$parent.find('.notice-bd');
                //var id=$parent.data('id');

                if($parent.hasClass('active')){
                    $content.slideUp();
                    $parent.removeClass('active');
                }else{
                    $content.slideDown();
                    $parent.addClass('active');
                    if(!$parent.hasClass('load')){
                        mobileManage.getUserManage().readEmail({emailId:$parent.data('id')},function(result){
                            if(result.success){
                                $content.html(result.data.content.replace(/\r\n/g,'<br>'));
                                $parent.addClass('load read');
                                $('#email-unreadCount').html(result.data.unreadCount);
                            }
                        });
                    }
                }

            });
        };

        this.init=function () {
            var def=that.getData();

            def.done(function (data) {
                if(data.success){
                    that.$container.html(that.buildHtml(data.data.records));
                    that.eventHandle();
				}

            });
            
        };

        this.init();
        
        function hehe(){
	            $(".read").children().find("h3").css({
            	"color":"#4c4436"
            })
        }
        
		hehe()
    }

	  
	 	
	 
	</script>
<script>

</script>	
</body>
</html>