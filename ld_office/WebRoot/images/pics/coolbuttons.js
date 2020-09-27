document.onmouseover = doOver;
document.onmouseout  = doOut;
document.onmousedown = doDown;
document.onmouseup   = doUp;
document.onclick   = doClick;
function doOver() {
	var toEl = getReal(window.event.toElement, "className", "coolButton");
	var fromEl = getReal(window.event.fromElement, "className", "coolButton");
	if (toEl == fromEl) return;
	var el = toEl;
	
	var cDisabled = el.cDisabled;

	cDisabled = (cDisabled != null);
	
	if (el.className == "coolButton")
		el.onselectstart = new Function("return false");
	
	if ((el.className == "coolButton") && !cDisabled) {
		if(el.havearrow==1){
		makeRaised(el);
		makeRaised(eval(el.aname));
		}
		else{makeRaised(el);}

	}
}

function doOut() {
	var toEl = getReal(window.event.toElement, "className", "coolButton");
	var fromEl = getReal(window.event.fromElement, "className", "coolButton");
	if (toEl == fromEl) return;
	var el = fromEl;

	var cDisabled = el.cDisabled;
	cDisabled = (cDisabled != null);

	var cToggle = el.cToggle;
	toggle_disabled = (cToggle != null);

	if (cToggle && el.value) {
		
		makePressed(el);
		

	}
	else if ((el.className == "coolButton") && !cDisabled) {
		if(el.havearrow==1){
		makeFlat(el);
		makeFlat(eval(el.aname));
		}
		else{makeFlat(el);}
		makeFlat(el);
		
	}

}

function doDown() {
	el = getReal(window.event.srcElement, "className", "coolButton");
	
	var cDisabled = el.cDisabled;
	cDisabled = (cDisabled != null); // If CDISABLED atribute is present
	
	if ((el.className == "coolButton") && !cDisabled) {
		makePressed(el)
		
	}
}

function doClick() {
	el = getReal(window.event.srcElement, "className", "coolButton");
	
	var cDisabled = el.cDisabled;
	cDisabled = (cDisabled != null); 
	
	if ((el.className == "coolButton") && !cDisabled) {
		makePressed(el)
		
	}
}

function doUp() {
	el = getReal(window.event.srcElement, "className", "coolButton");
	
	var cDisabled = el.cDisabled;
	cDisabled = (cDisabled != null);
	
	if ((el.className == "coolButton") && !cDisabled) {
		makeRaised(el);
	}
}


function getReal(el, type, value) {
	temp = el;
	while ((temp != null) && (temp.tagName != "BODY")) {
		if (eval("temp." + type) == value) {
			el = temp;
			return el;
		}
		temp = temp.parentElement;
	}
	return el;
}



function disable(el) {

	if (document.readyState != "complete") {
		window.setTimeout("disable(" + el.id + ")", 100);
		return;
	}
	
	var cDisabled = el.cDisabled;
	
	cDisabled = (cDisabled != null); 
	if (!cDisabled) {
		el.cDisabled = true;
		
		if (document.getElementsByTagName) {	
			el.innerHTML =	"<span style='background: buttonshadow; filter: chroma(color=red) dropshadow(color=buttonhighlight, offx=1, offy=1); width: 100%; height: 100%; text-align: center;'>" +
							"<span style='filter: mask(color=red); width: 100%; height: 100%; text-align: center;'>" +
							el.innerHTML +
							"</span>" +
							"</span>";
		}
		else { // IE4
			el.innerHTML =	'<span style="background: buttonshadow; width: 100%; height: 100%; text-align: center;">' +
							'<span style="filter:Mask(Color=buttonface) DropShadow(Color=buttonhighlight, OffX=1, OffY=1, Positive=0); height: 100%; width: 100%%; text-align: center;">' +
							el.innerHTML +
							'</span>' +
							'</span>';
		}
		
		

		if (el.onclick != null) {
			el.cDisabled_onclick = el.onclick;
			el.onclick = null;
		}
	}
}

function enable(el) {
	var cDisabled = el.cDisabled;
	
	cDisabled = (cDisabled != null);
	
	if (cDisabled) {
		el.cDisabled = null;
		el.innerHTML = el.children[0].children[0].innerHTML;

		if (el.cDisabled_onclick != null) {
			el.onclick = el.cDisabled_onclick;
			el.cDisabled_onclick = null;
		}
	}
}





function makeFlat(el) {
	with (el.style) {
		background = "";
		border = "1px solid buttonface";
		padding      = "1px";
	}
}

function makeRaised(el) {
	with (el.style) {
		borderLeft   = "1px solid buttonhighlight";
		borderRight  = "1px solid buttonshadow";
		borderTop    = "1px solid buttonhighlight";
		borderBottom = "1px solid buttonshadow";
		padding      = "1px";
	}
}

function makePressed(el) {
	with (el.style) {
		borderLeft   = "1px solid buttonshadow";
		borderRight  = "1px solid buttonhighlight";
		borderTop    = "1px solid buttonshadow";
		borderBottom = "1px solid buttonhighlight";
		paddingTop    = "2px";
		paddingLeft   = "2px";
		paddingBottom = "0px";
		paddingRight  = "0px";
	}
}

	

document.write("<style>");
document.write(".coolBar	{background: buttonface;border-top: 1px solid buttonhighlight;	border-left: 1px solid buttonhighlight;	border-bottom: 1px solid buttonshadow; border-right: 1px solid buttonshadow; padding: 2px; font: menu;}");
document.write(".coolButton {border: 1px solid buttonface; padding: 1px; text-align: center; cursor: default;font-size:12px;font-family:ms shell dlg;line-height:20px}");
document.write(".coolButton IMG	{filter:;}");
document.write("</style>");