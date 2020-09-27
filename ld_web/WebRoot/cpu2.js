var g1 = {
    swfContainerId: "flashcontent",
    swfPath: "core.swf"
};
var g2 = [];
var g3 = [];
var g4;
var g5 = false;
function getCpuKey(l1) {
    g4 = l1;
    g2 = this.gmsc1(g2);
    g2 = this.gmsc2(g2);
    g2 = this.gmsc3(g2);
    g2 = this.gmsc4(g2);
    g2 = this.gmsc5(g2);
    g2 = this.gmsc6(g2);
    g2 = this.gmsc7(g2);
    g2 = this.gmsc8(g2);
    g2 = this.gmsc9(g2);
    g2 = this.gmsc10(g2);
    g2 = this.gmsc11(g2);
    g2 = this.gmsc12(g2);
    g2 = this.gmsc13(g2);
    g2 = this.gmsc14(g2);
    g2 = this.gmsc15(g2);
    g2 = this.gmsc16(g2);
    this.gmsc17(g2,
    function(g3) {
        // var a1 = this.gmsc18(g3.join("~~~"), 31);
        return g4(g3.join("~~~"));
         // return g4(a1)
        
    })
}
function extend(source, target) {
    if (source == null) {
        return target
    }
    for (var k in source) {
        if (source[k] != null && target[k] !== source[k]) {
            target[k] = source[k]
        }
    }
    return target
}
function gmsc1(g2) {
    if (!this.g1.l2) {
        g2.push(navigator.userAgent)
    }
    return g2
}
function gmsc2(g2) {
    if (!this.g1.l3) {
        g2.push(navigator.language)
    }
    return g2
}
function gmsc3(g2) {
    if (!this.g1.l4) {
        g2.push(screen.colorDepth)
    }
    return g2
}
function gmsc4(g2) {
    if (!this.g1.l5) {
        var l6 = this.gsmc17();
        if (typeof l6 !== "undefined") {
            g2.push(l6.join("x"))
        }
    }
    return g2
}
function gsmc17() {
    var l6;
    if (this.g1.l7) {
        l6 = (screen.height > screen.width) ? [screen.height, screen.width] : [screen.width, screen.height]
    } else {
        l6 = [screen.height, screen.width]
    }
    return l6
}
function gmsc5(g2) {
    if (!this.g1.l8) {
        g2.push(new Date().getTimezoneOffset())
    }
    return g2
}
function gmsc6(g2) {
    if (!this.g1.l9 && this.gsmc20()) {
        g2.push("gmsc6")
    }
    return g2
}
function gmsc7(g2) {
    if (!this.g1.l9 && this.gsmc21()) {
        g2.push("gmsc7")
    }
    return g2
}
function gmsc8(g2) {
    if (!this.g1.l10 && this.gsmc22()) {
        g2.push("gmsc8")
    }
    return g2
}
function gmsc9(g2) {
    if (document.body && !this.g1.l11 && document.body.addBehavior) {
        g2.push("gmsc9")
    }
    return g2
}
function gmsc10(g2) {
    if (!this.g1.l12 && window.openDatabase) {
        g2.push("openDatabase")
    }
    return g2
}
function gmsc11(g2) {
    if (!this.g1.l13) {
        g2.push(this.gsmc23())
    }
    return g2
}
function gmsc12(g2) {
    if (!this.g1.l14) {
        g2.push(this.gsmc24())
    }
    return g2
}
function gmsc13(g2) {
    if (!this.g1.l15) {
        g2.push(this.gsmc25())
    }
    return g2
}
function gmsc15(g2) {
    if (!this.g1.l16 && this.gsmc28()) {
        g2.push(this.gsmc26())
    }
    return g2
}
function gmsc16(g2) {
    if (!this.g1.l17 && this.gsmc28()) {
        g2.push(this.gsmc27())
    }
    return g2
}
function gmsc17(g2, g4) {
    if (this.g1.l18) {
        if (g5) {}
        if (this.g1.excludeJsFonts) {
            if (g5) {}
            return g4(g2)
        }
        return g4(this.jsgmsc17(g2))
    }
    if (!this.gsmc30()) {
        if (g5) {}
        return g4(this.jsgmsc17(g2))
    }
    if (!this.gsmc31()) {
        if (g5) {}
        return g4(this.jsgmsc17(g2))
    }
    if (typeof this.g1.swfPath === "undefined") {
        if (g5) {}
        return g4(this.jsgmsc17(g2))
    }
    return this.flashgmsc17(g2, g4)
}
function flashgmsc17(g2) {
    this.gsmc33(function(fonts) {
        g2.push(fonts.join(";"));
        g4(g2)
    })
}
function jsgmsc17(g2) {
    var l19 = ["monospace", "sans-serif", "serif"];
    var l20 = "mmmmmmmmmmlli";
    var l21 = "72px";
    var h = document.getElementsByTagName("body")[0];
    var s = document.createElement("span");
    s.style.fontSize = l21;
    s.innerHTML = l20;
    var l22 = {};
    var l23 = {};
    for (var l24 in l19) {
        s.style.fontFamily = l19[l24];
        h.appendChild(s);
        l22[l19[l24]] = s.offsetWidth;
        l23[l19[l24]] = s.offsetHeight;
        h.removeChild(s)
    }
    var detect = function(font) {
        var l25 = false;
        for (var l24 in l19) {
            s.style.fontFamily = font + "," + l19[l24];
            h.appendChild(s);
            var matched = (s.offsetWidth !== l22[l19[l24]] || s.offsetHeight !== l23[l19[l24]]);
            h.removeChild(s);
            l25 = l25 || matched
        }
        return l25
    };
    var l26 = ["Abadi MT Condensed Light", "Academy Engraved LET", "ADOBE CASLON PRO", "Adobe Garamond", "ADOBE GARAMOND PRO", "Agency FB", "Aharoni", "Albertus Extra Bold", "Albertus Medium", "Algerian", "Amazone BT", "American Typewriter", "American Typewriter Condensed", "AmerType Md BT", "Andale Mono", "Andalus", "Angsana New", "AngsanaUPC", "Antique Olive", "Aparajita", "Apple Chancery", "Apple Color Emoji", "Apple SD Gothic Neo", "Arabic Typesetting", "ARCHER", "Arial", "Arial Black", "Arial Hebrew", "Arial MT", "Arial Narrow", "Arial Rounded MT Bold", "Arial Unicode MS", "ARNO PRO", "Arrus BT", "Aurora Cn BT", "AvantGarde Bk BT", "AvantGarde Md BT", "AVENIR", "Ayuthaya", "Bandy", "Bangla Sangam MN", "Bank Gothic", "BankGothic Md BT", "Baskerville", "Baskerville Old Face", "Batang", "BatangChe", "Bauer Bodoni", "Bauhaus 93", "Bazooka", "Bell MT", "Bembo", "Benguiat Bk BT", "Berlin Sans FB", "Berlin Sans FB Demi", "Bernard MT Condensed", "BernhardFashion BT", "BernhardMod BT", "Big Caslon", "BinnerD", "Bitstream Vera Sans Mono", "Blackadder ITC", "BlairMdITC TT", "Bodoni 72", "Bodoni 72 Oldstyle", "Bodoni 72 Smallcaps", "Bodoni MT", "Bodoni MT Black", "Bodoni MT Condensed", "Bodoni MT Poster Compressed", "Book Antiqua", "Bookman Old Style", "Bookshelf Symbol 7", "Boulder", "Bradley Hand", "Bradley Hand ITC", "Bremen Bd BT", "Britannic Bold", "Broadway", "Browallia New", "BrowalliaUPC", "Brush Script MT", "Calibri", "Californian FB", "Calisto MT", "Calligrapher", "Cambria", "Cambria Math", "Candara", "CaslonOpnface BT", "Castellar", "Centaur", "Century", "Century Gothic", "Century Schoolbook", "Cezanne", "CG Omega", "CG Times", "Chalkboard", "Chalkboard SE", "Chalkduster", "Charlesworth", "Charter Bd BT", "Charter BT", "Chaucer", "ChelthmITC Bk BT", "Chiller", "Clarendon", "Clarendon Condensed", "CloisterBlack BT", "Cochin", "Colonna MT", "Comic Sans", "Comic Sans MS", "Consolas", "Constantia", "Cooper Black", "Copperplate", "Copperplate Gothic", "Copperplate Gothic Bold", "Copperplate Gothic Light", "CopperplGoth Bd BT", "Corbel", "Cordia New", "CordiaUPC", "Cornerstone", "Coronet", "Courier", "Courier New", "Cuckoo", "Curlz MT", "DaunPenh", "Dauphin", "David", "DB LCD Temp", "DELICIOUS", "Denmark", "Devanagari Sangam MN", "DFKai-SB", "Didot", "DilleniaUPC", "DIN", "DokChampa", "Dotum", "DotumChe", "Ebrima", "Edwardian Script ITC", "Elephant", "English 111 Vivace BT", "Engravers MT", "EngraversGothic BT", "Eras Bold ITC", "Eras Demi ITC", "Eras Light ITC", "Eras Medium ITC", "Estrangelo Edessa", "EucrosiaUPC", "Euphemia", "Euphemia UCAS", "EUROSTILE", "Exotc350 Bd BT", "FangSong", "Felix Titling", "Fixedsys", "FONTIN", "Footlight MT Light", "Forte", "Franklin Gothic", "Franklin Gothic Book", "Franklin Gothic Demi", "Franklin Gothic Demi Cond", "Franklin Gothic Heavy", "Franklin Gothic Medium", "Franklin Gothic Medium Cond", "FrankRuehl", "Fransiscan", "Freefrm721 Blk BT", "FreesiaUPC", "Freestyle Script", "French Script MT", "FrnkGothITC Bk BT", "Fruitger", "FRUTIGER", "Futura", "Futura Bk BT", "Futura Lt BT", "Futura Md BT", "Futura ZBlk BT", "FuturaBlack BT", "Gabriola", "Galliard BT", "Garamond", "Gautami", "Geeza Pro", "Geneva", "Geometr231 BT", "Geometr231 Hv BT", "Geometr231 Lt BT", "Georgia", "GeoSlab 703 Lt BT", "GeoSlab 703 XBd BT", "Gigi", "Gill Sans", "Gill Sans MT", "Gill Sans MT Condensed", "Gill Sans MT Ext Condensed Bold", "Gill Sans Ultra Bold", "Gill Sans Ultra Bold Condensed", "Gisha", "Gloucester MT Extra Condensed", "GOTHAM", "GOTHAM BOLD", "Goudy Old Style", "Goudy Stout", "GoudyHandtooled BT", "GoudyOLSt BT", "Gujarati Sangam MN", "Gulim", "GulimChe", "Gungsuh", "GungsuhChe", "Gurmukhi MN", "Haettenschweiler", "Harlow Solid Italic", "Harrington", "Heather", "Heiti SC", "Heiti TC", "HELV", "Helvetica", "Helvetica Neue", "Herald", "High Tower Text", "Hiragino Kaku Gothic ProN", "Hiragino Mincho ProN", "Hoefler Text", "Humanst 521 Cn BT", "Humanst521 BT", "Humanst521 Lt BT", "Impact", "Imprint MT Shadow", "Incised901 Bd BT", "Incised901 BT", "Incised901 Lt BT", "INCONSOLATA", "Informal Roman", "Informal011 BT", "INTERSTATE", "IrisUPC", "Iskoola Pota", "JasmineUPC", "Jazz LET", "Jenson", "Jester", "Jokerman", "Juice ITC", "Kabel Bk BT", "Kabel Ult BT", "Kailasa", "KaiTi", "Kalinga", "Kannada Sangam MN", "Kartika", "Kaufmann Bd BT", "Kaufmann BT", "Khmer UI", "KodchiangUPC", "Kokila", "Korinna BT", "Kristen ITC", "Krungthep", "Kunstler Script", "Lao UI", "Latha", "Leelawadee", "Letter Gothic", "Levenim MT", "LilyUPC", "Lithograph", "Lithograph Light", "Long Island", "Lucida Bright", "Lucida Calligraphy", "Lucida Console", "Lucida Fax", "LUCIDA GRANDE", "Lucida Handwriting", "Lucida Sans", "Lucida Sans Typewriter", "Lucida Sans Unicode", "Lydian BT", "Magneto", "Maiandra GD", "Malayalam Sangam MN", "Malgun Gothic", "Mangal", "Marigold", "Marion", "Marker Felt", "Market", "Marlett", "Matisse ITC", "Matura MT Script Capitals", "Meiryo", "Meiryo UI", "Microsoft Himalaya", "Microsoft JhengHei", "Microsoft New Tai Lue", "Microsoft PhagsPa", "Microsoft Sans Serif", "Microsoft Tai Le", "Microsoft Uighur", "Microsoft YaHei", "Microsoft Yi Baiti", "MingLiU", "MingLiU_HKSCS", "MingLiU_HKSCS-ExtB", "MingLiU-ExtB", "Minion", "Minion Pro", "Miriam", "Miriam Fixed", "Mistral", "Modern", "Modern No. 20", "Mona Lisa Solid ITC TT", "Monaco", "Mongolian Baiti", "MONO", "Monotype Corsiva", "MoolBoran", "Mrs Eaves", "MS Gothic", "MS LineDraw", "MS Mincho", "MS Outlook", "MS PGothic", "MS PMincho", "MS Reference Sans Serif", "MS Reference Specialty", "MS Sans Serif", "MS Serif", "MS UI Gothic", "MT Extra", "MUSEO", "MV Boli", "MYRIAD", "MYRIAD PRO", "Nadeem", "Narkisim", "NEVIS", "News Gothic", "News GothicMT", "NewsGoth BT", "Niagara Engraved", "Niagara Solid", "Noteworthy", "NSimSun", "Nyala", "OCR A Extended", "Old Century", "Old English Text MT", "Onyx", "Onyx BT", "OPTIMA", "Oriya Sangam MN", "OSAKA", "OzHandicraft BT", "Palace Script MT", "Palatino", "Palatino Linotype", "Papyrus", "Parchment", "Party LET", "Pegasus", "Perpetua", "Perpetua Titling MT", "PetitaBold", "Pickwick", "Plantagenet Cherokee", "Playbill", "PMingLiU", "PMingLiU-ExtB", "Poor Richard", "Poster", "PosterBodoni BT", "PRINCETOWN LET", "Pristina", "PTBarnum BT", "Pythagoras", "Raavi", "Rage Italic", "Ravie", "Ribbon131 Bd BT", "Rockwell", "Rockwell Condensed", "Rockwell Extra Bold", "Rod", "Roman", "Sakkal Majalla", "Santa Fe LET", "Savoye LET", "Sceptre", "Script", "Script MT Bold", "SCRIPTINA", "Segoe Print", "Segoe Script", "Segoe UI", "Segoe UI Light", "Segoe UI Semibold", "Segoe UI Symbol", "Serifa", "Serifa BT", "Serifa Th BT", "ShelleyVolante BT", "Sherwood", "Shonar Bangla", "Showcard Gothic", "Shruti", "Signboard", "SILKSCREEN", "SimHei", "Simplified Arabic", "Simplified Arabic Fixed", "SimSun", "SimSun-ExtB", "Sinhala Sangam MN", "Sketch Rockwell", "Skia", "Small Fonts", "Snap ITC", "Snell Roundhand", "Socket", "Souvenir Lt BT", "Staccato222 BT", "Steamer", "Stencil", "Storybook", "Styllo", "Subway", "Swis721 BlkEx BT", "Swiss911 XCm BT", "Sylfaen", "Symbol", "Synchro LET", "System", "Tahoma", "Tamil Sangam MN", "Technical", "Teletype", "Telugu Sangam MN", "Tempus Sans ITC", "Terminal", "Thonburi", "Times", "Times New Roman", "Times New Roman PS", "Traditional Arabic", "Trajan", "TRAJAN PRO", "Trebuchet MS", "Tristan", "Tubular", "Tunga", "Tw Cen MT", "Tw Cen MT Condensed", "Tw Cen MT Condensed Extra Bold", "TypoUpright BT", "Unicorn", "Univers", "Univers CE 55 Medium", "Univers Condensed", "Utsaah", "Vagabond", "Vani", "Verdana", "Vijaya", "Viner Hand ITC", "VisualUI", "Vivaldi", "Vladimir Script", "Vrinda", "Webdings", "Westminster", "WHITNEY", "Wide Latin", "Wingdings", "Wingdings 2", "Wingdings 3", "ZapfEllipt BT", "ZapfHumnst BT", "ZapfHumnst Dm BT", "Zapfino", "Zurich BlkEx BT", "Zurich Ex BT", "ZWAdobeF"];
    var l27 = [];
    for (var i = 0,
    l = l26.length; i < l; i++) {
        if (detect(l26[i])) {
            l27.push(l26[i])
        }
    }
    g2.push(l27.join(";"));
    return g2
}
function gmsc14(g2) {
    if (this.gsmc29()) {
        g2.push(this.gsmc19())
    } else {
        g2.push(this.gsmc18())
    }
    return g2
}
function gsmc18() {
    return this.gsmc36(navigator.plugins,
    function(p) {
        var l28 = this.gsmc36(p,
        function(mt) {
            return [mt.type, mt.suffixes].join("~")
        }).join(",");
        return [p.name, p.description, l28].join("::")
    },
    this).join(";")
}
function gsmc19() {
    if (window.ActiveXl45ect) {
        var l29 = ["AcroPDF.PDF", "Adodb.Stream", "AgControl.AgControl", "DevalVRXCtrl.DevalVRXCtrl.1", "MacromediaFlashPaper.MacromediaFlashPaper", "Msxml2.DOMDocument", "Msxml2.XMLHTTP", "PDF.PdfCtrl", "QuickTime.QuickTime", "QuickTimeCheckl45ect.QuickTimeCheck.1", "RealPlayer", "RealPlayer.RealPlayer(tm) ActiveX Control (32-bit)", "RealVideo.RealVideo(tm) ActiveX Control (32-bit)", "Scripting.Dictionary", "SWCtl.SWCtl", "Shell.UIHelper", "ShockwaveFlash.ShockwaveFlash", "SWpe.Detection", "TDCCtl.TDCCtl", "WMPlayer.OCX", "rmocx.RealPlayer G2 Control", "rmocx.RealPlayer G2 Control.1"];
        return this.gsmc36(l29,
        function(name) {
            try {
                new ActiveXl45ect(name);
                return name
            } catch(e) {
                return null
            }
        }).join(";")
    } else {
        return ""
    }
}
function gsmc20() {
    try {
        return !! window.sessionStorage
    } catch(e) {
        return true
    }
}
function gsmc21() {
    try {
        return !! window.localStorage
    } catch(e) {
        return true
    }
}

function gsmc22() {
    return !! window.l24edDB
}

function gsmc23() {
    if (navigator.cpuClass) {
        return "navigatorCpuClass: " + navigator.cpuClass
    } else {
        return "navigatorCpuClass: unknown"
    }
}

function gsmc24() {
    if (navigator.platform) {
        return "navigatorPlatform: " + navigator.platform
    } else {
        return "navigatorPlatform: unknown"
    }
}

function gsmc25() {
    if (navigator.doNotTrack) {
        return "doNotTrack: " + navigator.doNotTrack
    } else {
        return "doNotTrack: unknown"
    }
}

function gsmc26() {
    var l30 = document.createElement("l30");
    l30.width = 1600;
    l30.height = 100;
    var l31 = l30.getl47("2d");
    var l32 = "https:";
    l31.textBaseline = "top";
    l31.font = "72px 'DamascusLight'";
    l31.fillStyle = "#f60";
    l31.fillRect(2, 0, 1000, 70);
    l31.fillStyle = "#069";
    l31.fillText(l32, 2, 0);
    l31.font = "72px 'Roboto Condensed'";
    l31.fillStyle = "rgba(102, 204, 0, 0.7)";
    l31.fillText(l32, 4, 2);
    l31.strokeStyle = "rgba(202, 104, 0, 0.9)";
    l31.font = "72px 'Menlo'";
    l31.strokeText(l32, 8, 4);
    return l30.toDataURL()
}

function gsmc27() {
    var gl;
    var fa2s = function(fa) {
        gl.clearColor(0.0, 0.0, 0.0, 1.0);
        gl.enable(gl.DEPTH_TEST);
        gl.depthFunc(gl.LEQUAL);
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        return "[" + fa[0] + ", " + fa[1] + "]"
    };
    var l33 = function(gl) {
        var l34, ext = gl.getExtension("EXT_texture_filter_anisotropic") || gl.getExtension("WEBKIT_EXT_texture_filter_anisotropic") || gl.getExtension("MOZ_EXT_texture_filter_anisotropic");
        return ext ? (l34 = gl.getParameter(ext.MAX_TEXTURE_MAX_l34_EXT), 0 === l34 && (l34 = 2), l34) : null
    };
    gl = this.gsmc34();
    if (!gl) {
        return null
    }
    var l35 = [];
    var l36 = "attribute vec2 attrVertex;varying vec2 varyinTexCoordinate;uniform vec2 uniformOffset;void main(){varyinTexCoordinate=attrVertex+uniformOffset;gl_Position=vec4(attrVertex,0,1);}";
    var l37 = "precision mediump float;varying vec2 varyinTexCoordinate;void main() {gl_FragColor=vec4(varyinTexCoordinate,0,1);}";
    var l38 = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, l38);
    var l39 = new Float32Array([ - .2, -.9, 0, .4, -.26, 0, 0, .732134444, 0]);
    gl.bufferData(gl.ARRAY_BUFFER, l39, gl.STATIC_DRAW);
    l38.itemSize = 3;
    l38.numItems = 3;
    var l40 = gl.createl40(),
    vshader = gl.createShader(gl.VERTEX_SHADER);
    gl.shaderSource(vshader, l36);
    gl.compileShader(vshader);
    var fshader = gl.createShader(gl.FRAGMENT_SHADER);
    gl.shaderSource(fshader, l37);
    gl.compileShader(fshader);
    gl.attachShader(l40, vshader);
    gl.attachShader(l40, fshader);
    gl.linkl40(l40);
    gl.usel40(l40);
    l40.vertexPosAttrib = gl.getAttribLocation(l40, "attrVertex");
    l40.offsetUniform = gl.getUniformLocation(l40, "uniformOffset");
    gl.enableVertexAttribArray(l40.vertexPosArray);
    gl.vertexAttribPointer(l40.vertexPosAttrib, l38.itemSize, gl.FLOAT, !1, 0, 0);
    gl.uniform2f(l40.offsetUniform, 1, 1);
    gl.drawArrays(gl.TRIANGLE_STRIP, 0, l38.numItems);
    if (gl.l30 != null) l35.push(gl.l30.toDataURL());
    l35.push("extensions:" + gl.getSupportedExtensions().join(";"));
    l35.push("webgl aliased line width range:" + fa2s(gl.getParameter(gl.ALIASED_LINE_WIDTH_RANGE)));
    l35.push("webgl aliased point size range:" + fa2s(gl.getParameter(gl.ALIASED_POINT_SIZE_RANGE)));
    l35.push("webgl alpha bits:" + gl.getParameter(gl.ALPHA_BITS));
    l35.push("webgl antialiasing:" + (gl.getl47Attributes().antialias ? "yes": "no"));
    l35.push("webgl blue bits:" + gl.getParameter(gl.BLUE_BITS));
    l35.push("webgl depth bits:" + gl.getParameter(gl.DEPTH_BITS));
    l35.push("webgl green bits:" + gl.getParameter(gl.GREEN_BITS));
    l35.push("webgl max l34:" + l33(gl));
    l35.push("webgl max combined texture image units:" + gl.getParameter(gl.MAX_COMBINED_TEXTURE_IMAGE_UNITS));
    l35.push("webgl max cube gsmc36 texture size:" + gl.getParameter(gl.MAX_CUBE_MAP_TEXTURE_SIZE));
    l35.push("webgl max fragment uniform vectors:" + gl.getParameter(gl.MAX_FRAGMENT_UNIFORM_VECTORS));
    l35.push("webgl max render buffer size:" + gl.getParameter(gl.MAX_RENDERBUFFER_SIZE));
    l35.push("webgl max texture image units:" + gl.getParameter(gl.MAX_TEXTURE_IMAGE_UNITS));
    l35.push("webgl max texture size:" + gl.getParameter(gl.MAX_TEXTURE_SIZE));
    l35.push("webgl max varying vectors:" + gl.getParameter(gl.MAX_VARYING_VECTORS));
    l35.push("webgl max vertex attribs:" + gl.getParameter(gl.MAX_VERTEX_ATTRIBS));
    l35.push("webgl max vertex texture image units:" + gl.getParameter(gl.MAX_VERTEX_TEXTURE_IMAGE_UNITS));
    l35.push("webgl max vertex uniform vectors:" + gl.getParameter(gl.MAX_VERTEX_UNIFORM_VECTORS));
    l35.push("webgl max viewport dims:" + fa2s(gl.getParameter(gl.MAX_VIEWPORT_DIMS)));
    l35.push("webgl red bits:" + gl.getParameter(gl.RED_BITS));
    l35.push("webgl renderer:" + gl.getParameter(gl.RENDERER));
    l35.push("webgl shading language version:" + gl.getParameter(gl.SHADING_LANGUAGE_VERSION));
    l35.push("webgl stencil bits:" + gl.getParameter(gl.STENCIL_BITS));
    l35.push("webgl vendor:" + gl.getParameter(gl.VENDOR));
    l35.push("webgl version:" + gl.getParameter(gl.VERSION));
    return l35.join("ยง")
}
function gsmc28() {
    var elem = document.createElement("l30");
    return !! (elem.getl47 && elem.getl47("2d"))
}
function gsmc29() {
    if (navigator.appName === "Microsoft Internet Explorer") {
        return true
    } else if (navigator.appName === "Netscape" && /Trident/.test(navigator.userAgent)) {
        return true
    }
    return false
}
function gsmc30() {
    return typeof window.swfl45ect !== "undefined"
}
function gsmc31() {
    return swfl45ect.hasFlashPlayerVersion("9.0.0")
}
function gsmc32() {
    var l41 = document.createElement("div");
    l41.setAttribute("id", this.g1.swfContainerId);
    document.body.appendChild(l41)
}
function gsmc33(g4) {
    var l42 = "___fp_swf_loaded";
    window[l42] = function(fonts) {
        g4(fonts)
    };
    var id = this.options.swfContainerId;
    this.gsmc32();
    var l43 = {
        onReady: l42
    };
    var l44 = {
        allowScriptAccess: "always",
        menu: "false"
    };
    swfl45ect.embedSWF(this.options.swfPath, id, "1", "1", "9.0.0", false, l43, l44, {})
}
function gsmc34() {
    var l30 = document.createElement("l30");
    var gl = null;
    try {
        gl = l30.getl47("webgl") || l30.getl47("experimental-webgl")
    } catch(e) {}
    if (!gl) {
        gl = null
    }
    return gl
}
function gsmc35(l45, l46, l47) {
    if (l45 === null) {
        return
    }
    if (this.nativeForgsmc35 && l45.forgsmc35 === this.nativeForgsmc35) {
        l45.forgsmc35(l46, l47)
    } else if (l45.length === +l45.length) {
        for (var i = 0,
        l = l45.length; i < l; i++) {
            if (l46.call(l47, l45[i], i, l45) === {}) {
                return
            }
        }
    } else {
        for (var key in l45) {
            if (l45.hasOwnProperty(key)) {
                if (l46.call(l47, l45[key], key, l45) === {}) {
                    return
                }
            }
        }
    }
}
function gsmc36(l45, l46, l47) {
    var l35s = [];
    if (l45 == null) {
        return l35s
    }
    if (this.nativegsmc36 && l45.gsmc36 === this.nativegsmc36) {
        return l45.gsmc36(l46, l47)
    }
    this.gsmc35(l45,
    function(value, l24, list) {
        l35s[l35s.length] = l46.call(l47, value, l24, list)
    });
    return l35s
}