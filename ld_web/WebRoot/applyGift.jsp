<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="${ctx}/tpl/linkResource.jsp"></jsp:include>
    <style>
        .ui-form-item label.error-tip{
            position: absolute;
            left: 396px;
            top:4px;
        }
		.unis{display:none;font-size:16px; color:#333; text-align:center;}
    </style>
    <c:if test="${session.customer==null}">
        <script>
            alert('请先登录！！');
            document.write('请先登录！！');
        </script>
    </c:if>
</head>
<body>
<div>
    <div style="font-size: 20px;display: none;" class="mb20 c-red" id="j-ret" >
        <p>芷晴宝宝提醒您：您还没有达到《情人节礼物》的存款要求，请您多多存款哦！！</p>
    </div>
    <form action="" method="post" class="ui-form" id="j-form-addr" style="display: none;">
        <div class="ui-form-item">
            <label for="" class="ui-label rq-value">所在地区：</label>
            <select name="province" id="j-select-province" class="ui-ipt" style="width: 80px" required>
            </select>
            <select name="city" id="j-select-city" class="ui-ipt" style="width: 80px" required>
            </select>
            <select name="" id="j-select-district" class="ui-ipt" style="width: 80px">
            </select>
        </div>
        <div class="ui-form-item">
            <label for="" class="ui-label rq-value">街道：</label>
            <input type="text" name="road" class="ui-ipt" required id="j-road"/>
        </div>
        <input type="hidden"  id="j-giftid" name="giftID"/>
        <input type="hidden" id="j-hd-addr" name="address"/>
        <div class="ui-form-item">
            <label for="" class="ui-label rq-value">玩家姓名：</label>
            <input type="text" class="ui-ipt" name="addressee" required/>
        </div>
        <div class="ui-form-item">
            <label for="" class="ui-label rq-value">手机号码：</label>
            <input type="text" class="ui-ipt" name="cellphoneNo" data-rule-phone="true" required maxlength="11"/>
        </div>
        <div class="ui-form-item">
            <label for="" class="ui-label">&nbsp;</label>
            <input type="submit" class="btn btn-danger" value="确定"/>
        </div>
    </form>
	<div id="start1" class="unis">芷晴宝宝温馨提醒：情人节活动未开启，请耐心稍等！</div>
</div>
<script id="tpl" type="text/x-handlebars-template">
    <h2 style="font-size: 20px" class="mb20">活动名称：{{title}}</h2>

    <div class="ui-form">
        <div class="ui-form-item">
            <label for="" class="ui-label">详细地址：</label>
            <span>{{address}}</span>
        </div>
        <div class="ui-form-item">
            <label for="" class="ui-label">玩家姓名：</label>
            <input type="text" class="ui-ipt"  disabled value="{{realname}}"/>
        </div>
        <div class="ui-form-item">
            <label for="" class="ui-label">手机号码：</label>
            <input type="text" class="ui-ipt" disabled value="{{cellphoneNo}}" />
        </div>
		<div class="ui-form-item" style="color:#333;">芷晴宝宝温馨提醒:  如需修改申请礼品信息,请发送原信息以及更改后的信息到客服邮箱:vip@lehu.ph.祝您鸡年大吉</div>
    </div>
</script>

<script src="/js/json/addrData.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.min.js"></script>
<script src="${ctx}/js/plugins/jquery.validate.config.js"></script>
<c:if test="${session.customer!=null}">
    <script>
        $(function(){
            // 获取礼品id
            ApplyGift.isExistGift();
        });
        var ApplyGift=(function(){
            var $province=$('#j-select-province'),
                    $city=$('#j-select-city'),
                    $district=$('#j-select-district'),
					$start1=$('#start1'),
                    $formAddr=$('#j-form-addr');


            // 获取礼品id
            function isExistGift(){
                $.ajax({
                    url     : "${ctx}/asp/isExistGift.aspx",
                    type    : "post",
                    dataType: "json",
                    cache   : false,
                    success : function (data) {
                        if(data){
                            if(data.reach){
                                if(data.order){  //已经领取
                                    $formAddr.hide();
                                    getData(data.title, data.order);
                                } else {  //还没有领取
                                    $formAddr.show();
                                    init();
                                    $('#j-giftid').val(data.id);
                                }
                            }else{ //还没有达到活动资格
                                $formAddr.hide();
                                //提示信息
                                $('#j-ret').show();
                            }
                        }else {
                            alert('活动未开启!');
							$start1.show();
                        }
                    }
                }).fail(function(){
                     alert('活动未开启!');
					 $start1.show();
                });
            }
            //没有获取礼品时初始化
            function init(){
                $province.html(appendOption('省份',getProvince()));
                $province.on('change',function(){
                    var value=$(this).val(),
                            id=$(this).find('[value="'+value+'"]').attr('data-pid');
                    var tmpData=getChild(id,getCity());
                    $city.html(appendOption('城市',tmpData));
                    $district.html('');
                    getAddr();
                });
                $city.on('change',function(){
                    var value=$(this).val(),
                            id=$(this).find('[value="'+value+'"]').attr('data-pid');
                    var	tmpData=getChild(id,getCity());
                    $district.html(appendOption('区/县',tmpData));
                    getAddr();
                });
                $district.on('change',function(){
                    getAddr();
                });

                $formAddr.validate({
                    submitHandler:function(form){
                        getAddr();
                        $.post('${ctx}/asp/applyGift.aspx',$(form).serialize(),function(data){
                            alert(data);
                            window.location.reload();
                        })
                    }
                });
            }

            //拼接收货地址的option
            function appendOption(tit,data){
                var html='<option data-pid="{{id}}" value="{{name}}">{{name}}</option>',
                        ret='<option data-pid="" value="" selected>'+tit+'</option>',
                        obj;
                for(var i=0;i<data.length;i++){
                    obj=data[i];
                    ret +=html.replace(/\{\{id\}\}/g,obj[0])
                            .replace(/\{\{name\}\}/g,obj[1][0]);
                }
                return ret;

            }
            //获取子元素
            function getChild(pid,data){
                var ret=[],
                        obj;
                for(var i=0;i<data.length;i++){
                    obj=data[i];
                    if(obj[2]==pid){
                        ret.push(obj);
                    }
                }
                return ret;
            }

            //获取地址详情
            function getAddr(){
                var prov=$province.val()||'',
                        city=$city.val()||'',
                        district=$district.val()||'',
                        road=$('#j-road').val()||'',
                        addr='';
                if(prov==city){
                    addr=prov+'市'+district+road;
                }else{
                    addr=prov+'省'+city+'市'+district+road;
                }
                $('#j-hd-addr').val(addr);
            }

            function getData(title, data){
                var html=$('#tpl').html();
                html=html.replace(/\{\{title\}\}/g,title)
                        .replace(/\{\{address\}\}/g,data.address)
                        .replace(/\{\{realname\}\}/g,data.realname)
                        .replace(/\{\{cellphoneNo\}\}/g,data.cellphoneNo);
                $('body').append(html);
            }

            return {
                'isExistGift':isExistGift
            }
        })();
    </script>
</c:if>

</body>
</html>