<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.model.Users"%>
<%@ page import="dfh.utils.Constants"%>
<%
	Users user = (Users)session.getAttribute(Constants.SESSION_CUSTOMERID);
	if(user==null){
		response.sendRedirect(request.getContextPath()+"/mobile/new/index.jsp");
	}
%>
<!DOCTYPE>
<html>

	<head>
		<title>天威</title>
		<jsp:include page="/mobile/commons/header.jsp">
			<jsp:param name="Title" value="问题建议" />
		</jsp:include>
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/account.css" />
		<link rel="stylesheet" type="text/css" href="/mobile/css/new/gongdan.css?v=1" />
	</head>

	<body class="email-page" style="background: #eeeeee;">
		<div class="page-inner"> 
			<div class="pt40"></div>
			<div id="j-container" class="layout-item-list tab_guggestion" style="padding: 10px;">
		            <div class="user-main tab-bd" style="padding: 5px;background-color: #2c8ba3;margin-bottom: 20px;">
		                <div id="tab-one" class="tab-panel active">
		                    <ul class="post-list" id="j-suggestionList"> </ul>
		                </div>
		                <div id="tab-qustion">
		                    <div class="user-tab-box ui-form">
		                        <h1 class="suggestion-title">提交问题<span class="suggestionclose-btn" onclick="colseSuggestionForm()">&times;</span></h1>
		                        <div class="ui-form-item">
		                            <label class="ui-label">您的问题：</label>
		                            <div class="ui-inputrow" style="margin-top: 10px;">
		                                <select id="suggestion-type" name="suggestion-type" title="问题" class="ui-ipt">
		                                    <option selected="selected" value="">请选择</option>
		                                    <option value="账户问题">账户问题</option>
		                                    <option value="存款及提款问题">存款及提款问题</option>
		                                    <option value="优惠活动问题">优惠活动问题</option>
		                                    <option value="网站及APP问题">网站及APP问题</option>
		                                    <option value="代理合作问题">代理合作问题</option> 
		                                    <option value="平台游戏问题">平台游戏问题</option>
		                                    <option value="投诉及建议问题">投诉及建议问题</option>
		                                </select>
		                            </div>
		                        </div>
		                        <div class="ui-form-item">
		                            <label class="ui-label">问题具体描述：</label>
		                            <textarea id="suggestion-content" name="suggestion-content" class="ui-ipt"  rich="0" title="描述"></textarea>

		                        </div>
		                        <div class="ui-form-item" style="text-align: center;">
		                            <button class="guggestion-btn" id="btn-submit" onclick="saveSuggestion();">创建</button>
		                        </div>
		                    </div>
		                </div>
		                <div id="suggestion-mark"></div>
		            </div>
		            <ul class="user-nav tab-nav">
		                <li class="active">
		                    <a href="javascript:;" data-toggle="tab" class="guggestion-btn" onclick="slideSuggestionForm()">
		                提出问题</a>
		                </li>
		            </ul>
		            <div id="oaGongDan" style="display: none;"></div>
		    </div>
		</div>
		<jsp:include page="/mobile/commons/menu.jsp" />
		<script type="text/javascript" src="/mobile/js/UserManage.js?v=1020"></script>
		<script type="text/javascript" src="/mobile/js/MobileManage.js?v=1210"></script>
		<script>
		    var $suggestionList=$('#j-suggestionList');

		    /* S1 投诉建议 取得列表
		     ========================= */
		    function suggestionService(index) {

		        var num     = 5;     // 取得几笔
		        var index   = index;  // 第几页

		        var dataArr = {
		            "pageIndex"  : index,
		            "size" : num
		        };

		        $.ajax({
		            url      : "/asp/qrySuggestion.aspx",
		            type     : "GET",
		            dataType : "json",
		            data     : dataArr,
		            cache    : false,
		            success  : function (data) {
		                if(data) {

		                    data = data[0];

		                    // 历史纪录

		                    var html = '<li class="sugges-item list-head">'+'<span class="list-item">问题类型</span>'+'<span class="list-item">提交时间</span></li>';
		                    var total = data.count;
		                    var sugList = data.sugList;

		                    for(var i = 0; i < data.sugList.length; i++) {

		                        var id              = sugList[i].id;
		                        var run_id          = sugList[i].run_id;
		                        var flow_id         = sugList[i].flow_id;
		                        var type            = sugList[i].type;
		                        var loginName       = sugList[i].loginName;
		                        var tempCreateTime  = sugList[i].tempCreateTime;
		                        var ctime           = sugList[i].createTime;

		                        // console.log(id, run_id, flow_id, type, loginName, tempCreateTime, ctime)               
		                        // data-runid="+run_id+" data-flowid="+flow_id+"

		                        // html += "<li class='item'><a href='javascript:void(0);' data-suggestionid="+id+">";
		                        // html += id + type + "<span class='fr'>"+tempCreateTime+"</span>";
		                        // html += "</a></li>";

		                        html += '<li class="sugges-item">'+
		                            '<div class="clearfix">'+
		                                '<span class="list-item c-red">'+id + type +'</span>'+
		                                '<span class="list-item">'+tempCreateTime+'</span>'+
		                                '<a href="javascript:void(0);" data-suggestionid="'+id+'" class="list-item c-yellow text-r">查看</a>'+
		                                '</div>'+
		                            '</li>';
		                    }

		                    // 分页记录

		                    var paginationHtml = "";
		                    var page_max = Math.ceil(total / num);
		                    var page_next = index+1;
		                    var page_past = index-1;

		                    paginationHtml += '<div class="pagination">';
		                    paginationHtml += '每页&nbsp;' + num + '&nbsp;条记录&nbsp;&nbsp;';

		                    paginationHtml += '第&nbsp;' + index + '/' + page_max;
		                    paginationHtml += '&nbsp;页&nbsp;&nbsp;&nbsp;';

		                    paginationHtml += '<a href="javascript:void(0);" onclick="suggestionService(1);">首页</a>&nbsp;';

		                    if(index != 1) {
		                        paginationHtml += '<a href="javascript:void(0);" onclick="suggestionService('+page_past+');">上一页</a>&nbsp;&nbsp;';
		                    }

		                    if(index != page_max ) {
		                        paginationHtml += '<a href="javascript:void(0);" onclick="suggestionService('+page_next+');">下一页</a>&nbsp;&nbsp;';
		                    }

		                    paginationHtml += '<a href="javascript:void(0);" onclick="suggestionService('+page_max+');">尾页</a>&nbsp;';
		                    paginationHtml += '</div>';

		                    // append 第一个：邮件列表, 第二个：分页记录
		                    $suggestionList.empty().append(html).append(paginationHtml).show();

		                    // 投诉建议弹窗初始化
		                    setSuggestionDetail();
		                } else {
		                    alert("无内容返回！")
		                }
		            }
		        });
		    }

		    /* S2 投诉建议 详细信息 弹窗
		     ========================= */
		    function setSuggestionDetail(url) {
		        $("li.sugges-item a").on('click', function(){
		            console.log(1)
		            var $modalLetter=$('#modal-suggestion');
		            var id = $(this).data("suggestionid");
		            var html = '';
		            var $this = $(this);
		            var $thisSugges = $this.parents('.sugges-item');
		            var result = '';
		                if($this.attr('data-block') == 'true'){
		                    $thisSugges.find('.detail-wp').slideUp(300);
		                    $this.attr('data-block','false').html('查看');
		                    return false;
		                }
		                if($this.attr('data-block') == 'false'){
		                    $thisSugges.find('.detail-wp').slideDown(300);
		                    $this.attr('data-block','true').html('收起');
		                    return false;
		                }
		            mobileManage.getLoader().open('数据读取中');
		            // $modalLetter.modal('show');
		            $.ajax({
		                url      : "/asp/viewSugguestionProgress.aspx",
		                type     : "POST",
		                cache    : false,
		                dataType : "text", 
		                data     : "sid=" + id,
		                success  : function (data) {
		                    mobileManage.getLoader().close();
		                    if(data) {
		                        data = data.replace(/\r\n/g, "<br />").replace(/\n/g, "<br />");
		                        $('#oaGongDan').html(data);
		                        // $modalLetter.find(".j-content").html(data);
		                        // result = $('[name=DATA_65]').val();
		                        result = '如果有处理结果会显示在这里！！';

		                        if(result){
		                            html = '<div class="detail-wp">'+
		                                '<h3 class="result-title">尊敬的天威会员：</h3>'+
		                               '<p class="oaResult"></p>'+
		                               // ' <p>'+data+'</p>'+
		                                '<p> - 天威客服部 - </p>'+
		                            '</div>';
		                            $thisSugges.append(html);
		                            $thisSugges.find('.oaResult').html(result);
		                            $this.attr('data-block','true').html('收起');
		                        }else {
		                            alert('暂无回复，请您耐心等待!');
		                        }
		                        result = '';
		                    } else {
		                        alert("无内容返回！")
		                    }
		                }
		            });
		        });
		    }


		    /* S3 投诉建议 创建
		     ========================= */
		    function saveSuggestion() {
		        var type    = $("#suggestion-type").val();
		        var content = $("#suggestion-content").val();

		        if(type == "") {
		            alert("问题种类不能为空！");
		            return false;
		        }

		        if(content == "") {
		            alert("问题具体描述不能为空！");
		            return false;
		        }

		        if(content != "" && content.length > 255) {
		            alert("问题具体描述过长！");
		            return false;
		        }
		        mobileManage.getLoader().open('数据读取中');
		        $.ajax(
		            {
		                url      : "/asp/submitSuggestion.aspx",
		                type     : "POST",
		                cache    : false,
		                data     : "type="+type+"&content="+content,
		                complete : function(data) {
		                    if("200" == data.status) {
		                        mobileManage.getLoader().close();
		                        alert(data.responseText); // 提交成功
		                        $("#suggestion-type, #suggestion-content").val("");
		                        suggestionService(1);
		                        $('.suggestionclose-btn').trigger('click');
		                    } else {
		                        mobileManage.getLoader().close();
		                        alert("系统繁忙，请稍后重试！")
		                    }
		                }
		            });

		        return true;
		    }    
		            
		    function slideSuggestionForm () {
		        $('#suggestion-mark').show();
		        $('#tab-qustion').show().animate({
		            marginTop: -150,
		            opacity:1},
		            500, function() {

		        });
		    }
		    function colseSuggestionForm () {
		        $('#tab-qustion').animate({
		            marginTop: -680,
		            opacity:0},
		            500, function() {
		            $('#suggestion-mark').hide();
		        });
		    }
		</script>

		<script>
		    !function(){suggestionService(1);}();
		</script>
	</body>

</html>