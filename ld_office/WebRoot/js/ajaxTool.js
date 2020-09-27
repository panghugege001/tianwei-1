/*
* ajax异步通讯公用函数
*/
var ajax_lastErr = "";

/*
* 函数名：ajax_createXMLHttpRequest
* 描述：
*  获取通讯对象
* 参数：
* 返回：
*/
function ajax_createXMLHttpRequest()
{
	ajax_lastErr = "";
	var ajax_i = 0;
	var ajax_xmlHttp = null;

	if( window.ActiveXObject )
	{
		var ajax_version = ["MSXML2.XMLHttp.5.0",
				"MSXML2.XMLHttp.4.0",
				"MSXML2.XMLHttp.3.0",
				"MSXML2.XMLHttp",
				"Microsoft.XMLHttp"];

		for( ajax_i=0; ajax_i < ajax_version.length; ajax_i++ )
		{
			try
			{
				ajax_xmlHttp = new ActiveXObject(ajax_version[ajax_i]);
				ajax_lastErr = "";
				break;
			}
			catch(ajax_e)
			{
				ajax_lastErr = "AJAXERR01:您的浏览器禁用了ActiveX控件;";
				ajax_xmlHttp = null;
			}      
		}
	}
	else if( window.XMLHttpRequest )
	{
		ajax_xmlHttp = new XMLHttpRequest();
	}
	else
	{
		ajax_lastErr = "AJAXERR02:您所使用的浏览器不支持此服务;";
		ajax_xmlHttp = null;
	}

	return ajax_xmlHttp;
}

/*
* 函数名：ajax_timeout
* 描述：
*  超时处理
* 参数：
*  ajax_xmlHttp：  XMLHttpRequest对象
* 返回：
*/
function ajax_timeout(ajax_xmlHttp,ajax_callBackFunction)
{
	if( ajax_xmlHttp )
	{
		ajax_xmlHttp.abort();
		ajax_callBackFunction(500,null,null);
	}
}

/*
* 函数名：ajax_removeTimeout
* 描述：
*  删除超时监控器
* 参数：
*  ajax_timer：   时钟对象
* 返回：
*/
function ajax_removeTimeout(ajax_timer)
{
	if( ajax_timer )
	{
		window.clearTimeout(ajax_timer);
	}
}

/*
* 函数名：ajax_setRequestTimeout
* 描述：
*  设置超时
* 参数：
*  ajax_xmlHttp： XMLHttpRequest对象
*  ajax_time：    超时时间，单位毫秒
* 返回：
*/
function ajax_setRequestTimeout(ajax_xmlHttp,ajax_time,ajax_callBackFunction)
{
	return window.setTimeout(function(){ajax_timeout(ajax_xmlHttp,ajax_callBackFunction);},ajax_time);
}

/*
* 函数名：ajax_sendRequest
* 描述：
*  申请异步通讯
* 参数：
*  ajax_type：    通讯方式，只能填写["GET"，"POST"]
*  ajax_url：     请求URL
*  ajax_recvTimeout：超时时间，单位毫秒
*  ajax_body：    请求体，通讯方式为"POST"是必填、"GET"时填空
*  ajax_callBackFunction：通讯状态通知回调函数
* 返回：
*/
function ajax_sendRequest(ajax_type,ajax_url,ajax_recvTimeout,ajax_body,ajax_callBackFunction)
{
	ajax_lastErr = "";
	var ajax_xmlHttp = null;
	var ajax_timer = null;
	
	if( "GET" != ajax_type && "POST" != ajax_type )
	{
		ajax_lastErr = "AJAXERR03:通讯类型错误!";
		return false;
	}

	ajax_xmlHttp = ajax_createXMLHttpRequest();
	if( null == ajax_xmlHttp )
	{
		return false;
	}

	ajax_timer = ajax_setRequestTimeout(ajax_xmlHttp,ajax_recvTimeout,ajax_callBackFunction);

	ajax_xmlHttp.open(ajax_type,ajax_url,true);
	ajax_xmlHttp.onreadystatechange = function(){ajax_callBack(ajax_xmlHttp,ajax_timer,ajax_callBackFunction);};
	if( "GET" == ajax_type )
	{
		ajax_xmlHttp.send(null);
	}
	else
	{
		ajax_xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
		ajax_xmlHttp.send(ajax_body);
	}

	return true;
}

/*
* 函数名：ajax_callBack
* 描述：
*  基本回调函数
* 参数：
*  ajax_xmlHttp： XMLHttpRequest对象
*  ajax_timer：   超时处理时钟对象
*  ajax_callBackFunction：通讯状态通知回调函数
* 返回：
*/
function ajax_callBack(ajax_xmlHttp,ajax_timer,ajax_callBackFunction)
{
	ajax_lastErr = "";
	if( 4 == ajax_xmlHttp.readyState )
	{
		ajax_removeTimeout(ajax_timer);

		if( 200 == ajax_xmlHttp.status )
		{
			/*状态,发送时间,是否最新,返回数据,返回dom*/
			ajax_callBackFunction(ajax_xmlHttp.status,"",ajax_xmlHttp.responseXML);
		}
		else
		{
			ajax_callBackFunction(ajax_xmlHttp.status,null,null);
		}
	}
}