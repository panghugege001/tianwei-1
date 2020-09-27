!(function(){
    $('#j-record-menu a').on('click',function(){
        $('#j-url').val($(this).attr('data-href'));
        chagepage();
    });
})();

function chagepage(pageIndex) {
    pageIndex=pageIndex||1;
    var url=$('#j-url').val();
    if (pageIndex <= 1) {
        pageIndex = 1;
    }
    $.post(url, {
        "pageIndex" : pageIndex,
        "size" : 8
    }, function(returnedData, status) {
        if ("success" == status) {
            $("#j-data").html(returnedData);
        }
    });
    return false;
}
//好友推荐记录
function queryfriendRecord(type){
    openProgressBar();
    $.post("/asp/queryfriendRecord.php", {
        "pageIndex":1,
        "size":8,
        "friendtype":type
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#j-data").html("");
            $("#j-data").html(returnedData);
        }
    });
    return false;
}

//好友推荐记录
function queryfriendRecordTwo(pageIndex){
    var type=$("#friendtype").val();
    openProgressBar();
    if(pageIndex<1){
        pageIndex=1;
    }
    $.post("/asp/queryfriendRecord.php", {
        "pageIndex":pageIndex,
        "size":8,
        "friendtype":type
    }, function (returnedData, status) {
        if ("success" == status) {
            closeProgressBar();
            $("#j-data").html("");
            $("#j-data").html(returnedData);
        }
    });
    return false;
}