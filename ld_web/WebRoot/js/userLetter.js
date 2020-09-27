
$(function() {
    
});

var $letterList=$('#j-letterList');
var $modalLetter=$('#modal-letter');

/* L1 站内信 取得邮件列表
 ========================= */
function letterService(index) {

    // 获取未读信息 in mainheader.jsp
    // getLetterCount(); 

    var num = 5;         // 取得几笔
    var index = index;   // 第几页

    var dataArr = {
        "page"  : index,
        "count" : num
    }

    $.ajax({
        url      : "/asp/getMessageByUser.aspx",
        type     : "GET",
        dataType : "json",
        data     : dataArr,
        cache    : false,
        success  : function (data){
            // 信件列表
            if(data) {

                var html = "";
                var total = data.count;
                var msgList = data.msgList;

                for(var i = 0; i < msgList.length; i++) {

                    var id    = msgList[i].id;
                    var title = msgList[i].title;
                    var ctime = msgList[i].createDate;
                    var read  = msgList[i].read;
                    var priv  = msgList[i].private;

                    html += "<li class='item'>";
                    html+="<a href='javascript:void(0);' data-letterid="+id+" data-read="+read+" data-private="+priv+">";

                    if(true == read) { // 已读
                        html += "<span class='square '>"+title+"</span>";
                    } else { // 未读
                        html += "<span class='square org'>"+title+"</span>";
                    }
                    

                    html +="<span class='fr'>"+ctime+"</span></a>";     
                    html += "</li>";

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

                paginationHtml += '<a href="javascript:void(0);" onclick="letterService(1);">首页</a>&nbsp;';

                if(index != 1) {
                    paginationHtml += '<a href="javascript:void(0);" onclick="letterService('+page_past+');">上一页</a>&nbsp;&nbsp;';
                }

                if(index != page_max ) {
                    paginationHtml += '<a href="javascript:void(0);" onclick="letterService('+page_next+');">下一页</a>&nbsp;&nbsp;';
                }

                paginationHtml += '<a href="javascript:void(0);" onclick="letterService('+page_max+');">尾页</a>&nbsp;';
                paginationHtml += '</div>';

                // append 第一个：邮件列表, 第二个：分页记录
                $letterList.empty().append(html).append(paginationHtml).show();

                // 站内信弹窗初始化
                //getLetterCount();
                setLetterDetail();
            } else {
                alert("无内容返回！")
            }
        }
    });
}

/* L2 站内信 详细信息 弹窗
 ========================= */
function setLetterDetail(url)  {
	var liststyle=$(".post-list >li").find(".square");
    $letterList.find("li.item a").on('click', function() {
    	$(this).find(".square").removeClass("org");
        var id = $(this).data("letterid");
        var priv = $(this).data("private");
        $modalLetter.modal('show');
        $.ajax({
            url      : "/asp/readMsg.aspx?msgID=" + id,
            type     : "get",
            cache    : false,
            success  : function (data) {
                if(data) {
                    var title   = data.title;
                    var ctime   = data.createDate;
                    var content = data.content;

                    content = content.replace(/\r\n/g, "<br />").replace(/\n/g, "<br />");

                    $modalLetter.data("letterid", id);
                    $modalLetter.find(".j-tit").text(title);
                    $modalLetter.find(".j-time").text("时间：" + ctime);
                    $modalLetter.find(".j-content").html(content);

                    $modalLetter.find(".del").remove();

                    if(priv == true) {
                        //$modalLetter.find(".time").after('<div class="del"><a>删除消息</a></div>');
              
                    }
                    setLetterDel();
                } else {
                    alert("无内容返回！")
                }
            }
        });
    });
}

/* L3 站内信删除 
 ========================= */
function setLetterDel() {
    $(".del a").on('click', function() {
        var id = $modalLetter.data("letterid");

        var priv = $letterList.find("a[data-letterid="+id+"]").data("private");

        if(false == priv) {
            alert("站内信不可删除！");
            return false;
        }

        $.ajax({
            url      : "/asp/deleteMsg.aspx?msgID=" + id,
            type     : "get",
            cache    : false,
            complete : function(data){
                if("200" == data.status) {
                    alert(data.responseText);
                    letterService(1);
                } else {
                    alert("无内容返回！")
                }
            }
        });
    });
}

/* L4 站内信 发信
 ========================= */
function saveLetter() {
    var title   = $("#letter-title").val();
    var content = $("#letter-content").val();

    if(title == ""){
        alert("标题不能为空！");
        return false;
    }

    if(title != "" && title.length > 25){
        alert("标题过长！");
        return false;
    }

    if(content == ""){
        alert("回复信息不能为空！");
        return false;
    }

    if(content != "" && content.length>255){
        alert("回复信息过长！");
        return false;
    }

    $.ajax({
        url      : "/asp/saveBookDate.aspx",
        type     : "POST",
        cache    : false,
        data     : "guestbook.title="+title+"&guestbook.content="+content,
        complete : function(data) {
            if("200" == data.status) {
                $("#letter-title, #letter-content").val("");
                alert("站内信已发送！");
                letterService(1);
            } else {
                alert("无内容返回！")
            }
        }
    });

    return true;
}


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

                var html = '<li class="sugges-item list-head">'+'<span class="list-item">类型</span>'+'<span class="list-item">提交时间</span></li>';
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
        openProgressBar();
        // $modalLetter.modal('show');
        $.ajax({
            url      : "/asp/viewSugguestionProgress.aspx",
            type     : "POST",
            cache    : false,
            dataType : "text",
            data     : "sid=" + id,
            success  : function (data) {
                closeProgressBar();
                if(data) {
                    data = data.replace(/\r\n/g, "<br />").replace(/\n/g, "<br />");
                    html = '<div class="detail-wp"'+
                            '<h3>尊敬的天威会员</h3>'+
                           '<p>您好！您反映的问题，我们经过调查核实，您由于同一IP多次登录不同帐号，造成...您反映的问题，我们经过调查核实，您由于同一IP多次登录不同帐号，您反映的问题，我们经过调查核实，您由于同一IP多次登录不同帐号，您反映的问题，我们经过调查核实，您由于同一IP多次登录不同帐号，您反映的问题，我们经过调查核实，您由于同一IP多次登录不同帐号，您反映的问题，我们经过调查核实，您由于同一IP多次登录不同帐号，</p>'+
                           // ' <p>'+data+'</p>'+
                            '<p>天威客服部</p>'+
                        '</div>';
                    // $modalLetter.find(".j-content").html(data);
                    $thisSugges.append(html);
                    $this.attr('data-block','true').html('收起');
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
    openProgressBar();
    $.ajax(
        {
            url      : "/asp/submitSuggestion.aspx",
            type     : "POST",
            cache    : false,
            data     : "type="+type+"&content="+content,
            complete : function(data) {
                if("200" == data.status) {
                    closeProgressBar();
                    alert(data.responseText); // 提交成功
                    $("#suggestion-type, #suggestion-content").val("");
                    suggestionService(1);
                    $('.suggestionclose-btn').trigger('click');
                } else {
                    closeProgressBar();
                    alert("系统繁忙，请稍后重试！")
                }
            }
        });

    return true;
}    
        
function slideSuggestionForm () {
    $('#suggestion-mark').show();
    $('#tab-gongdan').show().animate({
        marginTop: -282,
        opacity:1},
        500, function() {

    });
}
function colseSuggestionForm () {
    $('#tab-gongdan').animate({
        marginTop: -880,
        opacity:0},
        500, function() {
        $('#suggestion-mark').hide();
    });
}
	
