function submitonce(theform){
if (document.all||document.getElementById){
for (i=0;i<theform.length;i++){
	var tempobj=theform.elements[i];
	if(tempobj.type.toLowerCase()=="submit"){
			tempobj.disabled=true;
		}
}
}
}

function formatNumber(srcStr,nAfterDot){
	　　var srcStr,nAfterDot;
	　　var resultStr,nTen;
	　　srcStr = ""+srcStr+"";
	　　strLen = srcStr.length;
	　　dotPos = srcStr.indexOf(".",0);
	　　if (dotPos == -1){
	　　　　resultStr = srcStr+".";
	　　　　for (i=0;i<nAfterDot;i++){
	　　　　　　resultStr = resultStr+"0";
	　　　　}
	　　　　return resultStr;
	　　}
	　　else{
	　　　　if ((strLen - dotPos - 1) >= nAfterDot){
	　　　　　　nAfter = dotPos + nAfterDot + 1;
	　　　　　　nTen =1;
	　　　　　　for(j=0;j<nAfterDot;j++){
	　　　　　　　　nTen = nTen*10;
	　　　　　　}
	　　　　　　resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen;
	　　　　　　return resultStr;
	　　　　}
	　　　　else{
	　　　　　　resultStr = srcStr;
	　　　　　　for (i=0;i<(nAfterDot - strLen + dotPos + 1);i++){
	　　　　　　　　resultStr = resultStr+"0";
	　　　　　　}
	　　　　　　return resultStr;
	　　　　}
	　　}
	}
