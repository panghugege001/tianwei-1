class com.mosesSupposes.fuse.FuseFMP
{
    static var _classes, _shortcuts, _getter, _setter;
    function FuseFMP()
    {
    } // End of the function
    static function simpleSetup()
    {
        com.mosesSupposes.fuse.FuseFMP.initialize(MovieClip.prototype, Button.prototype, TextField.prototype);
        _global.FuseFMP = com.mosesSupposes.fuse.FuseFMP;
        for (var _loc2 in com.mosesSupposes.fuse.FuseFMP._classes)
        {
            _global[_loc2] = com.mosesSupposes.fuse.FuseFMP._classes[_loc2];
        } // end of for...in
    } // End of the function
    static function initialize(target)
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            _shortcuts = {getFilterName: function (f)
            {
                return (com.mosesSupposes.fuse.FuseFMP.getFilterName(f));
            }, getFilterIndex: function (f)
            {
                return (com.mosesSupposes.fuse.FuseFMP.getFilterIndex(this, f));
            }, getFilter: function (f, createNew)
            {
                return (com.mosesSupposes.fuse.FuseFMP.getFilter(this, f, createNew));
            }, writeFilter: function (f, pObj)
            {
                return (com.mosesSupposes.fuse.FuseFMP.writeFilter(this, f, pObj));
            }, removeFilter: function (f)
            {
                return (com.mosesSupposes.fuse.FuseFMP.removeFilter(this, f));
            }, getFilterProp: function (prop, createNew)
            {
                return (com.mosesSupposes.fuse.FuseFMP.getFilterProp(this, prop, createNew));
            }, setFilterProp: function (prop, v)
            {
                com.mosesSupposes.fuse.FuseFMP.setFilterProp(this, prop, v);
            }, setFilterProps: function (fOrPObj, pObj)
            {
                com.mosesSupposes.fuse.FuseFMP.setFilterProps(this, fOrPObj, pObj);
            }, traceAllFilters: function ()
            {
                com.mosesSupposes.fuse.FuseFMP.traceAllFilters();
            }};
            _classes = {BevelFilter: flash.filters.BevelFilter, BlurFilter: flash.filters.BlurFilter, ColorMatrixFilter: flash.filters.ColorMatrixFilter, ConvolutionFilter: flash.filters.ConvolutionFilter, DisplacementMapFilter: flash.filters.DisplacementMapFilter, DropShadowFilter: flash.filters.DropShadowFilter, GlowFilter: flash.filters.GlowFilter, GradientBevelFilter: flash.filters.GradientBevelFilter, GradientGlowFilter: flash.filters.GradientGlowFilter};
            _getter = {__resolve: function (name)
            {
                var _loc4 = function ()
                {
                    var _loc3 = this;
                    if (_loc3.filters != undefined)
                    {
                        var _loc2 = name.split("_");
                        if (_loc2[1] == "blur")
                        {
                            _loc2[1] = "blurX";
                        } // end if
                        return (com.mosesSupposes.fuse.FuseFMP.getFilter(this, _loc2[0] + "Filter", false)[_loc2[1]]);
                    } // end if
                };
                return (_loc4);
            }};
            _setter = {__resolve: function (name)
            {
                var _loc3 = function (val)
                {
                    var _loc2 = this;
                    if (_loc2.filters != undefined)
                    {
                        com.mosesSupposes.fuse.FuseFMP.setFilterProp(this, name, val);
                    } // end if
                };
                return (_loc3);
            }};
        } // end if
        if (arguments[0] == null)
        {
            return;
        } // end if
        var _loc6 = [MovieClip, Button, TextField];
        for (var _loc13 in arguments)
        {
            var _loc7 = false;
            for (var _loc10 in _loc6)
            {
                if (arguments[_loc13] instanceof _loc6[_loc10] || arguments[_loc13] == Function(_loc6[_loc10]).prototype)
                {
                    _loc7 = true;
                    break;
                } // end if
            } // end of for...in
            if (!_loc7)
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("201", _loc13);
                continue;
            } // end if
            for (var _loc11 in com.mosesSupposes.fuse.FuseFMP._classes)
            {
                var _loc5 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc11]();
                for (var _loc8 in _loc5)
                {
                    if (typeof(_loc5[_loc8]) == "function")
                    {
                        continue;
                    } // end if
                    var _loc4 = _loc11.substr(0, -6) + "_" + _loc8;
                    arguments[_loc13].addProperty(_loc4, com.mosesSupposes.fuse.FuseFMP._getter[_loc4], com.mosesSupposes.fuse.FuseFMP._setter[_loc4]);
                    _global.ASSetPropFlags(arguments[_loc13], _loc4, 3, 1);
                    if (_loc8 == "blurX")
                    {
                        _loc4 = _loc4.slice(0, -1);
                        arguments[_loc13].addProperty(_loc4, com.mosesSupposes.fuse.FuseFMP._getter[_loc4], com.mosesSupposes.fuse.FuseFMP._setter[_loc4]);
                        _global.ASSetPropFlags(arguments[_loc13], _loc4, 3, 1);
                    } // end if
                } // end of for...in
            } // end of for...in
            for (var _loc9 in com.mosesSupposes.fuse.FuseFMP._shortcuts)
            {
                arguments[_loc13][_loc9] = com.mosesSupposes.fuse.FuseFMP._shortcuts[_loc9];
                _global.ASSetPropFlags(arguments[_loc13], _loc9, 7, 1);
            } // end of for...in
        } // end of for...in
    } // End of the function
    static function deinitialize()
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            return;
        } // end if
        if (arguments.length == 0)
        {
            arguments.push(MovieClip.prototype, Button.prototype, TextField.prototype);
        } // end if
        for (var _loc8 in arguments)
        {
            for (var _loc7 in com.mosesSupposes.fuse.FuseFMP._classes)
            {
                var _loc4 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc7]();
                for (var _loc5 in _loc4)
                {
                    if (typeof(_loc4[_loc5]) == "function")
                    {
                        continue;
                    } // end if
                    var _loc3 = _loc7.substr(0, -6) + "_" + _loc5;
                    _global.ASSetPropFlags(arguments[_loc8], _loc3, 0, 2);
                    arguments[_loc8].addProperty(_loc3, null, null);
                    delete arguments[_loc8][_loc3];
                } // end of for...in
            } // end of for...in
            for (var _loc6 in com.mosesSupposes.fuse.FuseFMP._shortcuts)
            {
                _global.ASSetPropFlags(arguments[_loc8], _loc6, 0, 2);
                delete arguments[_loc8][_loc6];
            } // end of for...in
        } // end of for...in
    } // End of the function
    static function getFilterName(instance)
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        for (var _loc1 in com.mosesSupposes.fuse.FuseFMP._classes)
        {
            if (instance.__proto__ == Function(com.mosesSupposes.fuse.FuseFMP._classes[_loc1]).prototype)
            {
                return (_loc1);
            } // end if
        } // end of for...in
        return (null);
    } // End of the function
    static function getFilterIndex(target, filter)
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        filter = com.mosesSupposes.fuse.FuseFMP.getInstance(filter);
        if (filter === null)
        {
            return (-1);
        } // end if
        var _loc2 = target.filters;
        for (var _loc1 = 0; _loc1 < _loc2.length; ++_loc1)
        {
            if (_loc2[_loc1].__proto__ == filter.__proto__)
            {
                return (_loc1);
            } // end if
        } // end of for
        return (-1);
    } // End of the function
    static function getFilter(target, filter, createNew)
    {
        var _loc1 = com.mosesSupposes.fuse.FuseFMP.getFilterIndex(target, filter);
        if (_loc1 == -1)
        {
            if (createNew != true)
            {
                return (null);
            } // end if
            _loc1 = com.mosesSupposes.fuse.FuseFMP.writeFilter(target, filter);
            if (_loc1 == -1)
            {
                return (null);
            } // end if
        } // end if
        return (target.filters[_loc1]);
    } // End of the function
    static function writeFilter(target, filter, propsObj)
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        filter = com.mosesSupposes.fuse.FuseFMP.getInstance(filter);
        if (filter === null)
        {
            return (-1);
        } // end if
        var _loc4 = target.filters;
        var _loc2 = com.mosesSupposes.fuse.FuseFMP.getFilterIndex(target, filter);
        if (_loc2 == -1)
        {
            _loc4.push(filter);
        }
        else
        {
            _loc4[_loc2] = filter;
        } // end else if
        target.filters = _loc4;
        if (typeof(propsObj) == "object")
        {
            com.mosesSupposes.fuse.FuseFMP.setFilterProps(target, filter, propsObj);
        } // end if
        _loc2 = com.mosesSupposes.fuse.FuseFMP.getFilterIndex(target, filter);
        return (_loc2);
    } // End of the function
    static function removeFilter(target, filter)
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        filter = com.mosesSupposes.fuse.FuseFMP.getInstance(filter);
        var _loc2 = target.filters;
        var _loc1 = com.mosesSupposes.fuse.FuseFMP.getFilterIndex(target, filter);
        if (_loc1 == -1)
        {
            return (false);
        } // end if
        _loc2.splice(_loc1, 1);
        target.filters = _loc2;
        return (true);
    } // End of the function
    static function getFilterProp(target, propname, createNew)
    {
        var _loc1 = propname.split("_");
        if (_loc1[1] == "blur")
        {
            _loc1[1] = "blurX";
        } // end if
        return (com.mosesSupposes.fuse.FuseFMP.getFilter(target, _loc1[0] + "Filter", createNew)[_loc1[1]]);
    } // End of the function
    static function setFilterProp(target, propname, value)
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        var _loc8 = propname.split("_");
        var _loc5 = _loc8[0] + "Filter";
        if (com.mosesSupposes.fuse.FuseFMP._classes[_loc5] == undefined)
        {
            return;
        } // end if
        var _loc2 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc5]();
        if (com.mosesSupposes.fuse.FuseFMP.BLUR_ZERO == true && _loc5 == "BlurFilter")
        {
            _loc2.blurX = _loc2.blurY = 0;
        } // end if
        var _loc6 = _loc8[1];
        var _loc1 = target.filters.length || 0;
        while (--_loc1 > -1)
        {
            if (target.filters[_loc1].__proto__ == _loc2.__proto__)
            {
                _loc2 = target.filters[_loc1];
                break;
            } // end if
        } // end while
        if (_loc2 == null)
        {
            com.mosesSupposes.fuse.FuseKitCommon.error("202", _loc5, target);
        } // end if
        if (_loc6 == "blur")
        {
            _loc2.blurX = value;
            _loc2.blurY = value;
        }
        else
        {
            if (typeof(value) == "string" && _loc6.toLowerCase().indexOf("color") > -1)
            {
                if (value.charAt(0) == "#")
                {
                    value = value.slice(1);
                } // end if
                value = value.charAt(1).toLowerCase() != "x" ? (Number("0x" + value)) : (Number(value));
            } // end if
            _loc2[_loc6] = value;
        } // end else if
        var _loc7 = target.filters;
        if (_loc1 == -1)
        {
            _loc7.push(_loc2);
        }
        else
        {
            _loc7[_loc1] = _loc2;
        } // end else if
        target.filters = _loc7;
    } // End of the function
    static function setFilterProps(target, filterOrPropsObj, propsObj)
    {
        if (arguments.length < 2)
        {
            com.mosesSupposes.fuse.FuseKitCommon.error("203", arguments.length);
            return;
        } // end if
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        var _loc10 = new Object();
        var _loc11 = arguments.length == 2;
        if (_loc11 == false)
        {
            var _loc14 = com.mosesSupposes.fuse.FuseFMP.getFilterName(com.mosesSupposes.fuse.FuseFMP.getInstance(filterOrPropsObj));
            if (com.mosesSupposes.fuse.FuseFMP._classes[_loc14] == undefined)
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("204", filterOrPropsObj);
                return;
            } // end if
            _loc10[_loc14] = 1;
        }
        else
        {
            propsObj = filterOrPropsObj;
            for (var _loc12 in propsObj)
            {
                _loc14 = _loc12.split("_")[0] + "Filter";
                if (com.mosesSupposes.fuse.FuseFMP._classes[_loc14] != undefined && _loc10[_loc14] == undefined)
                {
                    _loc10[_loc14] = 1;
                } // end if
            } // end of for...in
        } // end else if
        if (!(target instanceof Array))
        {
            target = [target];
        } // end if
        for (var _loc15 in target)
        {
            var _loc5 = target[_loc15];
            for (var _loc14 in _loc10)
            {
                var _loc3 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc14]();
                if (com.mosesSupposes.fuse.FuseFMP.BLUR_ZERO == true && _loc14 == "BlurFilter")
                {
                    _loc3.blurX = _loc3.blurY = 0;
                } // end if
                var _loc4 = _loc5.filters.length || 0;
                while (--_loc4 > -1)
                {
                    if (_loc5.filters[_loc4].__proto__ == _loc3.__proto__)
                    {
                        _loc3 = _loc5.filters[_loc4];
                        break;
                    } // end if
                } // end while
                if (_loc3 == null)
                {
                    com.mosesSupposes.fuse.FuseKitCommon.error("202", _loc14, _loc5);
                    continue;
                } // end if
                var _loc7 = String(_loc14).slice(0, -6) + "_";
                for (var _loc12 in propsObj)
                {
                    var _loc6 = _loc12.indexOf(_loc7) == 0;
                    if (_loc11 == true && _loc6 == false)
                    {
                        continue;
                    } // end if
                    var _loc2 = propsObj[_loc12];
                    if (_loc6 == true)
                    {
                        _loc12 = _loc12.slice(_loc7.length);
                    } // end if
                    if (_loc12 == "blur")
                    {
                        _loc3.blurX = _loc2;
                        _loc3.blurY = _loc2;
                        continue;
                    } // end if
                    if (typeof(_loc2) == "string" && _loc12.toLowerCase().indexOf("color") > -1)
                    {
                        if (_loc2.charAt(0) == "#")
                        {
                            _loc2 = _loc2.slice(1);
                        } // end if
                        _loc2 = _loc2.charAt(1).toLowerCase() != "x" ? (Number("0x" + _loc2)) : (Number(_loc2));
                    } // end if
                    _loc3[_loc12] = _loc2;
                } // end of for...in
                var _loc8 = _loc5.filters;
                if (_loc4 == -1)
                {
                    _loc8.push(_loc3);
                }
                else
                {
                    _loc8[_loc4] = _loc3;
                } // end else if
                _loc5.filters = _loc8;
            } // end of for...in
        } // end of for...in
    } // End of the function
    static function getAllShortcuts()
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        var _loc2 = [];
        for (var _loc4 in com.mosesSupposes.fuse.FuseFMP._classes)
        {
            var _loc1 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc4]();
            for (var _loc3 in _loc1)
            {
                if (typeof(_loc1[_loc3]) == "function")
                {
                    continue;
                } // end if
                _loc2.push(_loc4.substr(0, -6) + "_" + _loc3);
                if (_loc3 == "blurX")
                {
                    _loc2.push(_loc4.substr(0, -6) + "_blur");
                } // end if
            } // end of for...in
        } // end of for...in
        return (_loc2);
    } // End of the function
    static function traceAllFilters()
    {
        if (com.mosesSupposes.fuse.FuseFMP._classes == undefined)
        {
            com.mosesSupposes.fuse.FuseFMP.initialize(null);
        } // end if
        var _loc1 = "------ FuseFMP filter properties ------\n";
        for (var _loc4 in com.mosesSupposes.fuse.FuseFMP._classes)
        {
            _loc1 = _loc1 + _loc4;
            var _loc2 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc4]();
            for (var _loc3 in _loc2)
            {
                if (typeof(_loc2[_loc3]) == "function")
                {
                    continue;
                } // end if
                _loc1 = _loc1 + ("\t- " + _loc4.substr(0, -6) + "_" + _loc3);
                if (_loc3 == "blurX")
                {
                    _loc1 = _loc1 + ("\t- " + _loc4.substr(0, -6) + "_blur");
                } // end if
            } // end of for...in
            _loc1 = _loc1 + "\n";
        } // end of for...in
        com.mosesSupposes.fuse.FuseKitCommon.output(_loc1);
    } // End of the function
    static function getInstance(filter)
    {
        if (filter instanceof flash.filters.BitmapFilter)
        {
            return ((flash.filters.BitmapFilter)(filter));
        } // end if
        if (typeof(filter) == "function")
        {
            for (var _loc4 in com.mosesSupposes.fuse.FuseFMP._classes)
            {
                if (filter == com.mosesSupposes.fuse.FuseFMP._classes[_loc4])
                {
                    var _loc1 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc4]();
                    if (com.mosesSupposes.fuse.FuseFMP.BLUR_ZERO == true && _loc4 == "BlurFilter")
                    {
                        _loc1.blurX = _loc1.blurY = 0;
                    } // end if
                    return (_loc1);
                } // end if
            } // end of for...in
        } // end if
        if (typeof(filter) == "string")
        {
            var _loc3 = String(filter);
            if (_loc3.substr(-6) != "Filter")
            {
                _loc3 = _loc3 + "Filter";
            } // end if
            for (var _loc4 in com.mosesSupposes.fuse.FuseFMP._classes)
            {
                if (_loc4 == _loc3)
                {
                    _loc1 = new com.mosesSupposes.fuse.FuseFMP._classes[_loc4]();
                    if (com.mosesSupposes.fuse.FuseFMP.BLUR_ZERO == true && _loc4 == "BlurFilter")
                    {
                        _loc1.blurX = _loc1.blurY = 0;
                    } // end if
                    return (_loc1);
                } // end if
            } // end of for...in
        } // end if
        return (null);
    } // End of the function
    static var registryKey = "fuseFMP";
    static var VERSION = com.mosesSupposes.fuse.FuseKitCommon.VERSION;
    static var BLUR_ZERO = true;
} // End of Class
