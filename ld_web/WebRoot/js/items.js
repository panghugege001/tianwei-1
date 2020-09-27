$(function () {
    // $("#header .register,#banner .register").click(function (e) {
    //     var loginname = $("#checkUserIsLoad").val();
    //     if (loginname != "" && !(typeof("loginname") == "undefined")) {
    //     }
    //     $("body").append('<div id="screen"></div>');
    //
    //     $("#screen").show().css({"width": $(document).width() + 20, "height": $(document).height() + 20});
    //     $("#registerbox").show().css({
    //         "left": $(window).scrollLeft() + $(window).width() / 2 - 303,
    //         "top": $(window).height()
    //     }).animate({"top": $(window).scrollTop() + $(window).height() / 2 - 232}, 700, "easeOutQuint");
    //     $("html,body").css({"overflow": "hidden"});
    //     drag("drag", "registerbox");
    //     e.preventDefault()
    // });
    $("#xieyi").click(function (e) {
        $("body").append('<div id="screen"></div>');
        $("#screen").show().css({"width": $(document).width() + 20, "height": $(document).height() + 20});
        $("#agentbox").show().css({
            "left": $(window).scrollLeft() + $(window).width() / 2 - 303,
            "top": $(window).height()
        }).animate({"top": $(window).scrollTop() + $(window).height() / 2 - 232}, 700, "easeOutQuint");
        $("html,body").css({"overflow": "hidden"});
        drag("drag", "centerbox1");
        e.preventDefault()
    });
    $("#registerbox .closed").click(function (e) {
        var that = this;
        $("#registerbox").animate({"top": $(window).scrollTop() + $(window).height()}, 550, "easeInOutQuad", function () {
            $(that).parents("#registerbox").hide();
            $("#screen").fadeOut();
            $("html,body").css({"overflow": "auto"})
        });
        e.preventDefault()
    });
    $("#agentbox .closed").click(function (e) {
        var that = this;
        $("#agentbox").animate({"top": $(window).scrollTop() + $(window).height()}, 550, "easeInOutQuad", function () {
            $(that).parents("#agentbox").hide();
            $("#screen").fadeOut();
            $("html,body").css({"overflow": "auto"})
        });
        e.preventDefault()
    });
    $("#live_in").click(function (e) {
        $("body").append('<div id="screen"></div>');
        $("#screen").show().css({"width": $(document).width() + 20, "height": $(document).height() + 20});
        $("#livebox").show().css({
            "left": $(window).scrollLeft() + $(window).width() / 2 - 303,
            "top": $(window).height()
        }).animate({"top": $(window).scrollTop() + $(window).height() / 2 - 232}, 700, "easeOutQuint");
        $("html,body").css({"overflow": "hidden"});
        drag("drag", "liveDrag");
        e.preventDefault()
    });
    $("#phone_in").click(function (e) {
        $("body").append('<div id="screen"></div>');
        $("#screen").show().css({"width": $(document).width() + 20, "height": $(document).height() + 20});
        $("#phone-box").show().css({
            "left": $(window).scrollLeft() + $(window).width() / 2 - 303,
            "top": $(window).height()
        }).animate({"top": $(window).scrollTop() + $(window).height() / 2 - 232}, 700, "easeOutQuint");
        $("html,body").css({"overflow": "hidden"});
        drag("drag", "phoneDrag");
        e.preventDefault()
    });
    $("#reg-inster").click(function (e) {
        $("body").append('<div id="screen"></div>');
        $("#screen").show().css({"width": $(document).width() + 20, "height": $(document).height() + 20});
        $("#registerbox").show().css({
            "left": $(window).scrollLeft() + $(window).width() / 2 - 303,
            "top": $(window).height()
        }).animate({"top": $(window).scrollTop() + $(window).height() / 2 - 232}, 700, "easeOutQuint");
        $("html,body").css({"overflow": "hidden"});
        drag("drag", "phoneDrag");
        e.preventDefault()
    });
    $("#phone-box .closed").click(function (e) {
        var that = this;
        $("#phone-box").animate({"top": $(window).scrollTop() + $(window).height()}, 550, "easeInOutQuad", function () {
            $(that).parents(".pub-close-box").hide();
            $("#screen").fadeOut();
            $("html,body").css({"overflow": "auto"})
        });
        e.preventDefault()
    });
    $("#livebox .closed").click(function (e) {
        var that = this;
        $("#livebox").animate({"top": $(window).scrollTop() + $(window).height()}, 550, "easeInOutQuad", function () {
            $(that).parents("#livebox").hide();
            $("#screen").fadeOut();
            $("html,body").css({"overflow": "auto"})
        });
        e.preventDefault()
    });
    $("#flip_menu1 .downlist").hover(function () {
        var arr = ($(this).attr("class")).split(" ");
        var classStr = arr[1];
        $("#games .list").hide();
        $("#games").find("." + classStr).show();
        $("#games").stop(true).animate({"height": "173px"})
    }, function () {
        $("#games").stop(true).animate({"height": "0px"})
    });
    $("#games").hover(function () {
        $("#games").stop(true).animate({"height": "173px"})
    }, function () {
        $("#games").stop(true).animate({"height": "0px"})
    });
    Date.prototype.pattern = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12,
            "H+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        };
        var week = {
            "0": "/u65e5",
            "1": "/u4e00",
            "2": "/u4e8c",
            "3": "/u4e09",
            "4": "/u56db",
            "5": "/u4e94",
            "6": "/u516d"
        };
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length))
        }
        if (/(E+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""])
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)))
            }
        }
        return fmt
    }
});

function show_time() {
    var time_start = new Date().getTime();
    var time_end = new Date("2015/11/01 00:00:00").getTime();
    var time_distance = time_end - time_start;
    var int_day = Math.floor(time_distance / 86400000);
    time_distance -= int_day * 86400000;
    var int_hour = Math.floor(time_distance / 3600000);
    time_distance -= int_hour * 3600000;
    var int_minute = Math.floor(time_distance / 60000);
    time_distance -= int_minute * 60000;
    var int_second = Math.floor(time_distance / 1000);
    if (int_day < 10) {
        int_day = "0" + int_day
    }
    if (int_hour < 10) {
        int_hour = "0" + int_hour
    }
    if (int_minute < 10) {
        int_minute = "0" + int_minute
    }
    if (int_second < 10) {
        int_second = "0" + int_second
    }
    $("#time_d").val(int_day);
    $("#time_h").val(int_hour);
    $("#time_m").val(int_minute);
    $("#time_s").val(int_second);
    setTimeout("show_time()", 1000)
}

function updatedate() {
    var dd1 = new Date();
    var hours = dd1.getHours();
    var year = dd1.getFullYear();
    var month = dd1.getMonth() + 1;
    var date = dd1.getDate();
    var minutes = dd1.getMinutes();
    var seconds = dd1.getSeconds();
    var curDateTime = "北京时间: 　" + year;
    if (month > 9) {
        curDateTime = curDateTime + "-" + month
    } else {
        curDateTime = curDateTime + "-0" + month
    }
    if (date > 9) {
        curDateTime = curDateTime + "-" + date
    } else {
        curDateTime = curDateTime + "-0" + date
    }
    if (hours > 9) {
        curDateTime = curDateTime + "　" + hours
    } else {
        curDateTime = curDateTime + "　0" + hours
    }
    if (minutes > 9) {
        curDateTime = curDateTime + ":" + minutes
    } else {
        curDateTime = curDateTime + ":0" + minutes
    }
    if (seconds > 9) {
        curDateTime = curDateTime + ":" + seconds
    } else {
        curDateTime = curDateTime + ":0" + seconds
    }
    $("#headerbg .time p").html(curDateTime);
    var t = setTimeout("updatedate()", 1000)
}

function drag(dragDiv, tagDiv) {
    var dragDiv = document.getElementById(dragDiv);
    var tagDiv = document.getElementById(tagDiv);
    var tagContainer = document;
    var e, offsetT, offsetL, downX, downY, moveX, moveY;
    dragDiv.onMouseover = function (e) {
        this.style.cursor = "move"
    };
    dragDiv.onmousedown = function (e) {
        e = e || window.event;
        offsetT = tagDiv.offsetTop;
        offsetL = tagDiv.offsetLeft;
        downX = e.clientX;
        downY = e.clientY;
        dragDiv.onmouseup = function (e) {
            e = e || window.event;
            tagContainer.onmousemove = function () {
                return false
            }
        };
        tagContainer.onmousemove = function (e) {
            e = e || window.event;
            moveX = e.clientX;
            moveY = e.clientY;
            tagDiv.style.top = offsetT + (moveY - downY) + "px";
            tagDiv.style.left = offsetL + (moveX - downX) + "px"
        }
    }
}

function game() {
    $("#items dl").hover(function () {
        $(this).find(".btn-name").stop(true, true).fadeIn("show");
        $(this).find("i").fadeIn("show")
    }, function () {
        $(this).find(".btn-name").fadeOut(500);
        $(this).find("i").fadeOut(500)
    })
};