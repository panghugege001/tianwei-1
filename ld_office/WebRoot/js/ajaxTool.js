/*
* ajax�첽ͨѶ���ú���
*/
var ajax_lastErr = "";

/*
* ��������ajax_createXMLHttpRequest
* ������
*  ��ȡͨѶ����
* ������
* ���أ�
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
				ajax_lastErr = "AJAXERR01:���������������ActiveX�ؼ�;";
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
		ajax_lastErr = "AJAXERR02:����ʹ�õ��������֧�ִ˷���;";
		ajax_xmlHttp = null;
	}

	return ajax_xmlHttp;
}

/*
* ��������ajax_timeout
* ������
*  ��ʱ����
* ������
*  ajax_xmlHttp��  XMLHttpRequest����
* ���أ�
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
* ��������ajax_removeTimeout
* ������
*  ɾ����ʱ�����
* ������
*  ajax_timer��   ʱ�Ӷ���
* ���أ�
*/
function ajax_removeTimeout(ajax_timer)
{
	if( ajax_timer )
	{
		window.clearTimeout(ajax_timer);
	}
}

/*
* ��������ajax_setRequestTimeout
* ������
*  ���ó�ʱ
* ������
*  ajax_xmlHttp�� XMLHttpRequest����
*  ajax_time��    ��ʱʱ�䣬��λ����
* ���أ�
*/
function ajax_setRequestTimeout(ajax_xmlHttp,ajax_time,ajax_callBackFunction)
{
	return window.setTimeout(function(){ajax_timeout(ajax_xmlHttp,ajax_callBackFunction);},ajax_time);
}

/*
* ��������ajax_sendRequest
* ������
*  �����첽ͨѶ
* ������
*  ajax_type��    ͨѶ��ʽ��ֻ����д["GET"��"POST"]
*  ajax_url��     ����URL
*  ajax_recvTimeout����ʱʱ�䣬��λ����
*  ajax_body��    �����壬ͨѶ��ʽΪ"POST"�Ǳ��"GET"ʱ���
*  ajax_callBackFunction��ͨѶ״̬֪ͨ�ص�����
* ���أ�
*/
function ajax_sendRequest(ajax_type,ajax_url,ajax_recvTimeout,ajax_body,ajax_callBackFunction)
{
	ajax_lastErr = "";
	var ajax_xmlHttp = null;
	var ajax_timer = null;
	
	if( "GET" != ajax_type && "POST" != ajax_type )
	{
		ajax_lastErr = "AJAXERR03:ͨѶ���ʹ���!";
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
* ��������ajax_callBack
* ������
*  �����ص�����
* ������
*  ajax_xmlHttp�� XMLHttpRequest����
*  ajax_timer��   ��ʱ����ʱ�Ӷ���
*  ajax_callBackFunction��ͨѶ״̬֪ͨ�ص�����
* ���أ�
*/
function ajax_callBack(ajax_xmlHttp,ajax_timer,ajax_callBackFunction)
{
	ajax_lastErr = "";
	if( 4 == ajax_xmlHttp.readyState )
	{
		ajax_removeTimeout(ajax_timer);

		if( 200 == ajax_xmlHttp.status )
		{
			/*״̬,����ʱ��,�Ƿ�����,��������,����dom*/
			ajax_callBackFunction(ajax_xmlHttp.status,"",ajax_xmlHttp.responseXML);
		}
		else
		{
			ajax_callBackFunction(ajax_xmlHttp.status,null,null);
		}
	}
}