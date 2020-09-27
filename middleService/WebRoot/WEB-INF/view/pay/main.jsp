<%@ page language="java" pageEncoding="UTF-8" %>
<jsp:include page="${ctx}/pages/taglibs.jsp"></jsp:include>
<!DOCTYPE HTML>
<html>
<head>
    <base href="${ctx}">
    <title>支付分发平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <jsp:include page="/pages/common.jsp"></jsp:include>
    <script>
        $.ajax({
            type: "POST",
            url: '/pay/pay_way4',
            success: function (data) {
                var select1 = $("#platformId1");
                var select11 = $("#platformId11");
                var select2 = $("#platformId2");
                var select3 = $("#platformId3");
                var select4 = $("#platformId4");
                var select5 = $("#platformId5");
                var select7 = $("#platformId7");
                var select10 = $("#platformId10");
                var select12 = $("#platformId12");
                var select13 = $("#platformId13");

                data.forEach(function (vo) {
                    if (vo.payWay == 1) {
                        $(select1).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    }
                    if (vo.payWay == 11) {
                        $(select11).val(vo.id);
                    } else if (vo.payWay == 2) {
                        $(select2).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    } else if (vo.payWay == 3) {
                        $(select3).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    } else if (vo.payWay == 4) {
                        $(select4).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    } else if (vo.payWay == 5) {
                        $(select5).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    } else if (vo.payWay == 7) {
                        $(select7).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    } else if (vo.payWay == 10) {
                        $(select10).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    } else if (vo.payWay == 12) {
                        $(select12).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    }else if (vo.payWay == 13) {
                        $(select13).append("<option value=" + vo.id + ">" + vo.payName + "</option>");
                    }
                });
            }
        });
    </script>
</head>
<body>
<jsp:include page="/pages/navbar.jsp"></jsp:include>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="#zfb" data-toggle="tab">支付宝支付</a></li>
    <li role="presentation"><a href="#weixi" data-toggle="tab">微信支付</a></li>
    <li role="presentation"><a href="#qq" data-toggle="tab">QQ支付</a></li>
    <li role="presentation"><a href="#online" data-toggle="tab">在线支付</a></li>
    <li role="presentation"><a href="#online_fast" data-toggle="tab">快捷支付</a></li>
    <li role="presentation"><a href="#point_card" data-toggle="tab">点卡支付</a></li>
    <li role="presentation"><a href="#jd" data-toggle="tab">京东支付</a></li>
    <li role="presentation"><a href="#yl" data-toggle="tab">银联支付</a></li>
    <li role="presentation"><a href="#coin" data-toggle="tab">代币支付</a></li>
</ul>
<div class="tab-content" style="margin-bottom: 10px">
    <div id="zfb" class="tab-pane fade in active">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype1">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype1">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId1">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId1"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
    <div id="weixi" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype2">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype2">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId2">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId2"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>

    <div id="qq" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype2">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype7">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId7">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId7"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>

    <div id="online" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype3">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype3">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId3">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId3"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">网银支付银行</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="bankCode" id="bankCode" value="ABC">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>

    <div id="online_fast" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype4">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype4">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId4">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId4"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
    <div id="point_card" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype5">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype5">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId4">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId5"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId5">点卡类型</label>
                <div class="col-sm-2">
                    <input class="form-control" name="cardCode" id="cardCode" value="100005">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="cardNo">点卡卡号</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="cardNo" id="cardNo" placeholder="输入点卡卡号">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="cardPassword">点卡密码</label>
                <div class="col-sm-2">
                    <input type="password" class="form-control" name="cardPassword" id="cardPassword"
                           placeholder="输入点卡密码">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
    
    <div id="jd" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype2">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype2">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId10">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId10"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
    
    <div id="yl" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype2">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype2">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId13">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId13"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
    
    

    <div id="coin" class="tab-pane fade">
        <form action="/pay/centerPay" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label" for="usetype12">使用类型</label>
                <div class="col-sm-2">
                    <select class="form-control" name="usetype" id="usetype12">
                        <option value="1">web</option>
                        <option value="2">pc</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="platformId10">支付平台</label>
                <div class="col-sm-2">
                    <select class="form-control" name="platformId" id="platformId12"></select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="loginName">用户名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="loginName" id="loginName" value="dytest01">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="orderAmount">充值金额</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" name="orderAmount" id="orderAmount" value="1.00">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
