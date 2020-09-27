//-----------------------------------------------------------------------------
// JavaGuy@Sohu.Com
//    2005-01-01
//
function DigitalSigMsg()
{
	var commDN = document.all("CommDN");
	var commValue = document.all("CommValue");
	var commObj = document.all("CommObject");
	var cfcaObj = document.getElementById("CryptoAgency1");
	var space = document.all("space");
	var rankey = document.all("ran");

	var value = "";

	try {
		cfcaObj.CFCA_Initialize("CFCALicense.dll"); 
		cfcaObj.CFCA_ClearUseCerts(true);
		if( false == cfcaObj.CFCA_AddUseCertBySubject(true,commDN.value) )
		{
			alert("设置签名证书失败!");
			return false;
		}

		if( (null == commObj) || ("" == commObj.value) )
		{
			value = commValue.value;
		}
		else
		{
			value = ComposeData(commObj.value);
		}           
                value=value+","+rankey.value;
                
                var re=new RegExp(space.value,"g");
                value=value.replace(re,"");
                document.all("CommValue").value = value;
		cfcaObj.CFCA_SetAlgorithm(0,"1.3.14.3.2.26");
		value = cfcaObj.CFCA_SignMessage(value);

		document.all("DSData").value = value;

	} catch(e) {

		GotoErrTx(e.number,e.name,e.message,commDN.value);
		return false;
	}
	return true;
}
function init(){
		try
		{
			document.getElementById("CryptoAgency1").CFCA_Initialize("CFCALicense.dll");
			document.getElementById("CryptoAgency1").CFCA_InitKeyDetector();
			return updateCustInfoList();
		}
		catch(e)
		{
			alert(e.message);
		}
}
function updateCustInfoList()
{
	var cfcaObj = document.getElementById("CryptoAgency1");
	var dncspList = "";

	try
	{
		dncspList = getalldncsp_new(cfcaObj);//cfcaObj.CFCA_GetAllCertDNCSP();
	}
	catch(e)
	{
		dncspList = "";
		alert(e.message);
	}
	var DNList = splitDNList(dncspList);
	if(DNList){
		return DNList;
	}
}
function getalldncsp_new(cfcaObj)
{
	try{
		var strresult="",stry,strm,strd;
		var success = false;
		var strdn,strcsp,strtime,strsn;
		var i=0,n=0,j=0;
		var da1;
		var strtmp,strtmpcert;
		//在这里调用原有方法获得不规则时间的证书综合信息
		var strall = cfcaObj.CFCA_GetAllCertDNCSP();
		if(strall.length<1)
			return strall;
		//将证书综合信息切割成单个证书信息数组
		var strtmpinfo = strall.split("\\");
		n = strtmpinfo.length;
		if(n == 0)
			return "format_err";
		//开始循环处理每个证书
		for(i=0;i<n;i++)
		{
			strtmp = strtmpinfo[i];
			j = strtmp.indexOf("*");
			if(j==-1)
				continue;
			//获得证书dn
			strdn = strtmp.substring(0,j);
			strtmp = strtmp.substring(j+1,strtmp.length);

			j = strtmp.indexOf("#");
			if(j==-1)
				continue;
			//获得证书csp名称
			strcsp = strtmp.substring(0,j);
			strtmp = strtmp.substring(j+1,strtmp.length);

			j = strtmp.indexOf("$");
			if(j==-1)
				continue;
			//获得证书时间
			strtime = strtmp.substring(0,j);
			strtmp = strtmp.substring(j+1,strtmp.length);
			
			//最后获得证书SN
			strsn = strtmp;
			//先清理一遍证书句柄
			cfcaObj.CFCA_ClearUseCerts(true);

			//隐式选择证书
			success = cfcaObj.CFCA_AddUseCertBySubject(true,strdn);
				
			if(success != false)
			{
				//获得公钥证书内容
				strtmpcert = cfcaObj.CFCA_GetCertContent(true,0);
				//获得证书截止日期
				da1 =new Date( cfcaObj.CFCA_GetCertValidDateTo(strtmpcert));
				//将证书截止日期规范化处理
				stry = ""+da1.getYear();
				//如果是个位数就加0
				if((da1.getMonth()+1)<10)
					strm = "0"+(da1.getMonth()+1);
				else strm = ""+(da1.getMonth()+1);
				if(da1.getDate()<10)
					strd = "0"+da1.getDate();
				else strd = ""+da1.getDate();
				strtime = stry+""+strm+""+strd;
			}
			//如果不是最后一张证书，就添加切割符：\
			if(i!=n-1)
			{
				strsn = strsn+"\\";
			}
			//拼接新的证书综合信息字符串
			strresult+=strdn+"*"+strcsp+"#"+strtime+"$"+strsn;
		}
	}catch(e)
	{
		strresult="";
	}finally{
		cfcaObj.CFCA_ClearUseCerts(true);
	}
	//返回新的综合信息字符串
	return strresult;
}
function splitDNList(DNList)
{
	var startIndex = 0;
	var endIndex = 0;
	var index = 0;
	var DNArray = new Array();

	/*拆分的字符串格式:DN1*CSP1#DATE1$REF1\\*/
	while( endIndex < DNList.length && -1 != (endIndex = DNList.indexOf("*",endIndex)) )
	{
		DNArray[index] = new Array();
		
		DNArray[index][0] = DNList.substring(startIndex,endIndex);

		startIndex = endIndex+1;
		endIndex = DNList.indexOf("#",endIndex);
		if( -1 == endIndex )
		{
			return null;
		}
		DNArray[index][1] = DNList.substring(startIndex,endIndex);

		startIndex = endIndex+1;
		endIndex = DNList.indexOf("$",endIndex);
		if( -1 == endIndex )
		{
			return null;
		}
		DNArray[index][2] = DNList.substring(startIndex,endIndex);

		startIndex = endIndex+1;
		endIndex = DNList.indexOf("\\",endIndex);
		if( -1 == endIndex )
		{
			DNArray[index][3] = DNList.substring(startIndex,DNList.length);
			break;
		}
		else
		{
			DNArray[index][3] = DNList.substring(startIndex,endIndex);
			startIndex = endIndex+1;
		}
		
		index++;
	}
	
	return DNArray;
}
function DigitalSigMsg2()
{
	var commDN = document.all("CommDN");
	var commValue = document.all("CommValue");
	var commObj = document.all("CommObject");
	var cfcaObj = document.getElementById("CryptoAgency1");
	var space = document.all("space");
	var rankey = document.all("ran");

	var value = "";
	
	try
	{
		cfcaObj.CFCA_Initialize("CFCALicense.dll"); 

		cfcaObj.CFCA_ClearUseCerts(true);
		if( false == cfcaObj.CFCA_AddUseCertBySubject(true,commDN.value) )
		{
			alert("设置签名证书失败!");
			return false;
		}

		if( (null == commObj) || ("" == commObj.value) )
		{
			value = commValue.value;
		}
		else
		{
			value = ComposeData(commObj.value);
		}

                var re=new RegExp(space.value,"g");
                value=value.replace(re,"");
                
                document.all("CommValue").value = value;

		cfcaObj.CFCA_SetAlgorithm(0,"1.3.14.3.2.26");
		value = cfcaObj.CFCA_SignMessageDetached(value,1);

		document.all("DSData").value = value;
	}
	catch(e)
	{
		alert("签名失败，签名控件返回错误信息:\n"+e.number+":"+e.message);
		return false;
	}
	return true;
}




function GotoErrTx(numErrCode,strErrName,strErrMessage,strDN)
{
	var strTxcode=document.all["txcode"].value;
	//document.all["txcode"].value="ERR100";
	parent.setTXCode("ERR100");
	strURL	="MainService?txcode=ERR100"
		+"&amp;ShowStyleData="+window.parent.document.all("ShowStyleData").value
		+"&amp;userid=@&amp;authCateTxcode="+strTxcode	
		+"&amp;code="+numErrCode			//
		+"&amp;name="+strErrName			//
		+"&amp;msgText="+strErrMessage			
		+"&amp;DN="+strDN;				//
	window.parent.frames['txmain'].location=strURL;
}
//20080306

function AssertError( strErrName, strErrMsg) {
	var strErrInfo;
	strErrInfo = ":" + strErrName + "\n?:" + strErrMsg;
	window.alert( strErrInfo);
	return true;
}

function InitCertKitAx()
{
	var strHTML = "";
	strHTML += "<INPUT type=hidden value='' name='DSData'>";
	strHTML += "<OBJECT id='CryptoAgency1' style='VISIBILITY:hidden' classid='clsid:E110BC2B-C768-4c9c-87EB-3A2228F7C4BF' VIEWASTEXT></OBJECT>";
	document.write( strHTML);

	return true;
}

function ComposeData( strObjectList) {
	var strRetComposeData;
	var strList = [];

	if( strObjectList == "") {
		return "";
	}strList = strObjectList.split(",");
	strRetComposeData = "";
	for( var i = 0; i < strList.length; i++) {
		strRetComposeData +=  GetValueFromName( strList[i]) + ",";
	}
	return strRetComposeData.substring(0,strRetComposeData.lastIndexOf(','));
}

//-----------------------------------------------------------------------------
// JavaGuy@Sohu.Com
//    2005-01-01
//
function GetValueFromName( strObjectName) {
	var oTmp;
	var strRet;
	var i;
	
	if( strObjectName == "") {
		return "";
	}
	oTmp = document.all( strObjectName);
	if( oTmp.length > 0) {
		switch( oTmp[0].type) {
			case "radio": {
				for( i = 0; i < oTmp.length; i++) {
					if( oTmp[i].checked == true) {
						strRet = oTmp[i].value;
						break;
					}
				}
				break;
			}
			case "checkbox": {
				strRet = null;
				for( i = 0; i < oTmp.length; i++) {
					if( oTmp[i].checked == true) {
						if( ( i > 0)&&( strRet != null)) strRet += "|"; 
						if( strRet == null ) strRet = "";
						strRet += oTmp[i].value;
						continue;
					}
				}		
				break;
			}
			default: {
				strRet = null;
				if( oTmp.type == "select-one") {
					strRet = oTmp.value;
				}
				break;
			}
		}
	} else {
		switch( oTmp.type) {
			case "text": {
				strRet = oTmp.value;
				break;
			}
			case "hidden":
				strRet = oTmp.value;
				break;
			case "radio":
			case "checkbox": {
				if( oTmp.checked == true) {
					strRet = oTmp.value;
				} else {
					strRet = null;
				}
				break;
			}
			default: {
				strRet = null;
				break;
			}
		}
	}

	return strRet;
}
//
//    2005-01-01
// JavaGuy@Sohu.Com
//=============================================================================
