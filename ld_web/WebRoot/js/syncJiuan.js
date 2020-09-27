/**
 *
 * 绑定久安钱包
 *
 **/

var _TEST_JUAN_MODE = 0;

$(function () {
    getWalletInfo();
});

//查询当前玩家是否绑定久安账号

function getWalletInfo(){
    $.post("/asp/getWalletInfo.aspx", function (returnedData, status) {
        //判断字串还是JSON
        var depositData = '';
        if (typeof(returnedData) != 'object') {
            depositData = JSON.parse(returnedData);
        } else {
            depositData = returnedData;
        }
        if (status == 'success') {
            if (depositData.code == '10000') {
                if (depositData.data.bindJiuAn == 'true') {

                    $('#lobbyToken').val(depositData.data.data.token);
                    $.post("/asp/bindWalletInfo.aspx", function (returnedData, status) {
                        var data = '';
                        if (typeof(returnedData) != 'object') {
                            data = JSON.parse(returnedData);
                        } else {
                            data = returnedData;
                        }
                        if (status == 'success') {
                            if (data.code == '10000') {
                                $('#lobbyId').val(data.data.merchantId);
                            }
                        }
                    });

                    //PC
                    $('.junaShow').css('display', 'none');
                    $('.junaShow2').css('display', 'block');

                    //H5
                    $('.accountJuan').css('display', 'none');
                    $('#junaLobby').css('display', 'block');

                } else if (depositData.data.bindJiuAn == 'false') {

                    $.post("/asp/bindWalletInfo.aspx", function (returnedData, status) {
                        var data = '';
                        if (typeof(returnedData) != 'object') {
                            data = JSON.parse(returnedData);
                        } else {
                            data = returnedData;
                        }
                        if (status == 'success') {
                            if (data.code == '10000') {
                                $('#bindId, #syncId').val(data.data.merchantId);
                                $('#bindUrl, #syncUrl').val(data.data.notifyUrl);
                                $('#bindName, #syncName').val(data.data.merchantUserName);
                                $('#bindcallBack,#synccallBack').val('https://' + window.location.host);
                            }
                        }
                    });

                    //PC
                    $('.junaShow').css('display', 'block');
                    $('.junaShow2').css('display', 'none');

                    //H5
                    $('.accountJuan').css('display', 'block');
                    $('#junaLobby').css('display', 'none');
                }
            }
        }
    });
}


function bindingJuan() {
    setTimeout(function () {
        $("#bindJuanForm").submit();
    }, 1000);
}

function syncJuan() {
    setTimeout(function () {
        $("#syncJuanForm").submit();
    }, 1000);

}

function junaLobby() {
    setTimeout(function () {
        $("#junaLobbyForm").submit();
    }, 1000);
}
