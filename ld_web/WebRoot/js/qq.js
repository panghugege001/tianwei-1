function show() {
    var t = getCookie("connectionname")
    if (t == "connectionname") {
    }
    else {
        setCookie("connectionname", "connectionname", "h168");
        popwin = window.location.href = 'tencent://message/?Menu=yes&uin=800134430&Service=58&SigT=A7F6FEA02730C988FD8B033516CE6674E502E573DD2A24D3479EA3737C59AA630F364B82AE4CB0233845F6341AC54F933CB1EF0CAB1151043518DEC630449BE155A3261F3801A057CE84156CCFC8D431020909C890EF4595425816B739E85EA46287EB8FC80478A1049FDE811907E68B3FC55D863FA67509';
    }
};
setTimeout("show()", 10000);
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}
function setCookie(name, value, time) {
    var strsec = getsec(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec * 1);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
function getsec(str) {
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 == "s") {
        return str1 * 1000;
    }
    else if (str2 == "h") {
        return str1 * 60 * 60 * 1000;
    }
    else if (str2 == "d") {
        return str1 * 24 * 60 * 60 * 1000;
    }
}



