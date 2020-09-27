/**
 * Created by michael on 2017/3/3.
 */
initI18n();

function initI18n() {
    var language = getLanguage();
    if(language != 'zh-CN' && language != 'zh') {
        localStorage.setItem("i18n_language","en");
        i18n("en");
    }
}

function getLanguage(){
    var language = localStorage.getItem("i18n_language");
    if(language != null) {
        return language;
    }else if (navigator.languages != undefined) {
        return navigator.languages[0];
    }else if(navigator.browserLanguage != undefined) {
        return navigator.browserLanguage;
    }else if(navigator.systemLanguage != undefined) {
        return navigator.systemLanguage;
    }else {
        return navigator.language;
    }
}

function i18n(language) {
    var i18nKeyArray = "";
    $("[i18n_key]").each(function() {
        i18nKey = $(this).attr("i18n_key");
        if( i18nKey != null && i18nKey != "" && i18nKey != "null") {
            i18nKeyArray = i18nKeyArray + i18nKey + ",";
        }
    });

    if(i18nKeyArray == "") {
        return;
    }
    i18nKeyArray = i18nKeyArray.substr(0,i18nKeyArray.length-1);

    $.ajax({
        type: "GET",
        async: false,
        url: "/i18n/getI18nMap.do",
        data: {"language":language,"i18nKeyArray":i18nKeyArray},
        dataType: "json", //表示返回值类型，不必须
        error: function(response) {
            alert(response.code);
        },
        success: function(response) {
            var code  = response.code;
            if(code == "0000") {
                var i18nList = response.data;
                if(i18nList == null || i18nList.length == 0) {
                    return;
                }
                for (var index in i18nList) {
                    $("[i18n_key="+ i18nList[index].key +"]").text(i18nList[index].value);
                }

            }else {
                alert(response.desc);
            }
        }
    });
}