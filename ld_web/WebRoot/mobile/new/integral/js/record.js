/**
 * Created by Randy on 2017/12/7.
 */

$(function () {
    var flag = getQueryString('flag');

    $.getJSON('/points/pointsRecord.aspx', {flag: flag}, function (data) {
        if (data.code === CODE) {
            var container = $('#pagination-lottery-record')
            if (data.data.length) {
                container.pagination({
                    dataSource: data.data,
                    className: 'paginationjs-theme-red',
                    pageSize: 6,
                    pageRange: 1,
                    callback: function (response, pagination) {
                        var dataHtml = '<div class="goods-list record-list"><ul>'
                        $.each(response, function (index, item) {
                            var property = '';
                            var status = item.status;
                            if (status == '已抽奖') {
                                status = '<span class="madeChange changeStatus" data-imageUrl="' + item.imgUrl + '" data-name="' + item.name + '" data-type="' + item.luckyDrawPresentType + '" data-property="' + item.property + '" data-id="' + item.id + '">领取</span>'
                            } else {
                                status === '已完成' && (status = '已领取')
                                status = '<span class="changeStatus">' + status + '</span>'
                            }
                            if (flag == 1) {//抽奖记录
                                property = filterAttrVal(item.luckyDrawPresentType, item.property)
                                if (item.luckyDrawPresentType == 'money' || item.luckyDrawPresentType == 'phoneFee' || item.luckyDrawPresentType == 'phoneData') {
                                    property = ''
                                }
                            } else {
                                property = filterAttrVal(item.type, item.property)
                            }
                            dataHtml += '<li>\
                                                <div class="pic">\
                                                    <img src="'+item.imgUrl+'" alt="" />\
                                                </div>\
                                                <div class="descrip">\
                                                    <h3 class="ellipsis">' + item.name + '</h3>\
                                                    <p class="red ellipsis">' + parseInt(item.points) + '分</p>\
                                                    <p class="timer ellipsis">' + item.createTime + '分</p>\
                                                </div>\
                                                ' + status + '\
                                             </li>';
                        });

                        dataHtml += '</ul></div>';

                        container.prev().html(dataHtml);
                    }
                })
            } else {
                container.prev().html('<div class="no-data">暂无记录</div>')
            }
        } else {
            layer.open({
                title: ['温馨提示'],
                skin: 'top-class',
                content: data.data
            })
        }
    })
    $('.header-title').html(flag == 1 ? '我的抽奖记录' : '积分兑换记录')


    // 领取奖品
    $(document).on('click', '.madeChange', function () {
        var name = $(this).attr('data-name')
        var type = $(this).attr('data-type')
        var id = $(this).attr('data-id')
        var resultProperty = $(this).attr('data-property')
        var imageUrl = $(this).attr('data-imageUrl')
        perfectionLottery(name, resultProperty, type, id, $(this), imageUrl)
    })

    // $.getJSON('/points/pointsRecord.aspx',{flag:flag},function(data){
    //     if(data.code){
    //         // 模板渲染
    //         $('#record-list-tmpl').tmpl(data.data.slice(0,15)).appendTo('.record-list ul');
    //
    //         var container = $('#pagination-lottery-record')
    //         container.pagination({
    //             dataSource:$('.record-list ul li'),
    //             className: 'paginationjs-theme-red'
    //         })
    //     }
    // })
})