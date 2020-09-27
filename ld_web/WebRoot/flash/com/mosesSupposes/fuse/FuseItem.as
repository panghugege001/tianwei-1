class com.mosesSupposes.fuse.FuseItem
{
    var _nItemID, _nFuseID, _initObj, _aProfiles, _oElements, _oTemps, _sImage, _aTweens, _oTwBeingAdded, caught, __get__label;
    static var _ZigoEngine, _aInstances;
    function FuseItem(id, o, fuseID)
    {
        _ZigoEngine = _global.com.mosesSupposes.fuse.ZigoEngine;
        _nItemID = id;
        _nFuseID = fuseID;
        _initObj = o;
        _aProfiles = [];
        _oElements = {aEvents: []};
        _oTemps = {};
        if (!(o instanceof Array))
        {
            o = [o];
        } // end if
        var _loc19 = _global.com.mosesSupposes.fuse.Fuse;
        _oTemps.outputLevel = _loc19 != undefined ? (_loc19.OUTPUT_LEVEL) : (_global.com.mosesSupposes.fuse.ZigoEngine.OUTPUT_LEVEL);
        if (o.length == 1)
        {
            var _loc18 = o[0];
            var _loc12 = _loc18.action != undefined ? (_loc18.action) : (_loc18);
            if (_loc12.__buildMode != true && _loc12.command != undefined)
            {
                _oElements.command = _loc12.command;
                _oElements.scope = _loc12.scope;
                _oElements.args = _loc12.args;
                _sImage = " Elements:[" + ("command" + (typeof(_loc12.command) == "string" ? (":\"" + _loc12.command + "\", ") : (", ")));
                if (_loc12.delay != undefined)
                {
                    _sImage = _sImage + "delay, ";
                    _oElements.delay = _loc12.delay;
                } // end if
                _sImage = _sImage.slice(0, -2) + "]";
                if (_loc12.func != undefined && _oTemps.outputLevel > 0)
                {
                    com.mosesSupposes.fuse.FuseKitCommon.error("113");
                } // end if
                return;
            } // end if
        } // end if
        _oTemps.sImgS = "";
        _oTemps.sImgE = "";
        _oTemps.sImgB = "";
        _oTemps.afl = 0;
        _oTemps.ael = 0;
        _oTemps.twDelayFlag = false;
        _oTemps.nActions = o.length;
        _oTemps.fuseProps = com.mosesSupposes.fuse.FuseKitCommon._fuseprops();
        _oTemps.cbProps = com.mosesSupposes.fuse.FuseKitCommon._cbprops();
        _oTemps.sUP = com.mosesSupposes.fuse.FuseKitCommon._underscoreable();
        _oTemps.sCT = com.mosesSupposes.fuse.FuseKitCommon._cts();
        _oTemps.bTriggerFound = false;
        for (var _loc17 in o)
        {
            var _loc3 = o[_loc17];
            if (_loc3.label != undefined && typeof(_loc3.label) == "string")
            {
                _oElements.label = _loc3.label;
            } // end if
            var _loc4;
            var _loc8 = Boolean(typeof(_loc3.action) == "object");
            if (_loc8 == true)
            {
                var _loc7 = _loc3.action instanceof Array ? (_loc3.action) : ([_loc3.action]);
                _loc4 = {delay: _loc3.delay, target: _loc3.target, addTarget: _loc3.addTarget, label: _loc3.label, trigger: _loc3.trigger};
                for (var _loc15 in _loc7)
                {
                    var _loc5 = this.parseProfile(_loc7[_loc15], _loc4);
                    if (_loc5 != undefined)
                    {
                        _aProfiles.unshift(_loc5);
                    } // end if
                } // end of for...in
                continue;
            } // end if
            _loc7 = _loc3;
            _loc5 = this.parseProfile(_loc7, _loc4);
            if (_loc5 != undefined)
            {
                _aProfiles.unshift(_loc5);
            } // end if
        } // end of for...in
        _sImage = "";
        var _loc16 = "";
        if (_oTemps.afl > 0)
        {
            _loc16 = _loc16 + (_oTemps.afl > 1 ? (_oTemps.afl + " callbacks, ") : ("callback, "));
        } // end if
        if (_oElements.delay != undefined || _oTemps.twDelayFlag == true)
        {
            _loc16 = _loc16 + "delay, ";
        } // end if
        if (_oTemps.bTriggerFound == true)
        {
            _loc16 = _loc16 + "trigger, ";
        } // end if
        if (_oTemps.ael > 0)
        {
            _loc16 = _loc16 + (_oTemps.ael > 1 ? (_oTemps.ael + " events, ") : ("event, "));
        } // end if
        if (_loc16 != "")
        {
            _sImage = _sImage + (" Elements:[" + _loc16.slice(0, -2) + "]");
        } // end if
        if (_oTemps.sImgS != "")
        {
            _sImage = _sImage + (" StartProps:[" + _oTemps.sImgS.slice(0, -2) + "]");
        } // end if
        if (_oTemps.sImgE != "")
        {
            _sImage = _sImage + (" Props:[" + _oTemps.sImgE.slice(0, -2) + "]");
        } // end if
        if (_oTemps.sImgB != "")
        {
            _sImage = _sImage + (" Simple Syntax Props:[" + _oTemps.sImgB.slice(0, -1) + "]");
        } // end if
        if (_sImage.slice(-2) == ", ")
        {
            _sImage = _sImage.slice(0, -2);
        } // end if
        delete this._oTemps;
    } // End of the function
    static function doTween()
    {
        for (var _loc3 in arguments)
        {
            if (typeof(arguments[_loc3]) == "object")
            {
                if (com.mosesSupposes.fuse.FuseItem._aInstances == undefined)
                {
                    _aInstances = new Array();
                } // end if
                var _loc2 = new com.mosesSupposes.fuse.FuseItem(com.mosesSupposes.fuse.FuseItem._aInstances.length, arguments[_loc3], -1);
                return (_loc2.startItem());
            } // end if
        } // end of for...in
    } // End of the function
    function get label()
    {
        return (_oElements.label);
    } // End of the function
    function hasTriggerFired()
    {
        return (_bTrigger == true);
    } // End of the function
    function getInitObj()
    {
        return (_initObj);
    } // End of the function
    function getActiveTargets(targetList)
    {
        if (_aTweens.length <= 0)
        {
            return (targetList);
        } // end if
        var _loc3 = false;
        for (var _loc5 in _aTweens)
        {
            for (var _loc4 in targetList)
            {
                if (targetList[_loc4] == _aTweens[_loc5].targ)
                {
                    _loc3 = true;
                    break;
                } // end if
            } // end of for...in
            if (_loc3 == false)
            {
                targetList.unshift(_aTweens[_loc5].targ);
            } // end if
        } // end of for...in
        return (targetList);
    } // End of the function
    function toString()
    {
        return (String(this._sID() + ":" + _sImage));
    } // End of the function
    function evalDelay(scope)
    {
        var _loc3 = _oElements.delay;
        if (_loc3 instanceof Function)
        {
            _loc3 = _loc3.apply(_oElements.delayscope != undefined ? (_oElements.delayscope) : (scope));
        } // end if
        if (typeof(_loc3) == "string")
        {
            _loc3 = this.parseClock(String(_loc3));
        } // end if
        if (_global.isNaN(Number(_loc3)) == true)
        {
            return (0);
        } // end if
        return (Number(_loc3));
    } // End of the function
    function startItem(targs, scope, duration, easing)
    {
        _ZigoEngine = _global.com.mosesSupposes.fuse.ZigoEngine;
        var _loc11 = _global.com.mosesSupposes.fuse.Fuse;
        var _loc5 = _loc11 != undefined ? (_loc11.OUTPUT_LEVEL) : (com.mosesSupposes.fuse.FuseItem._ZigoEngine.OUTPUT_LEVEL);
        if (_oElements.command != null)
        {
            var _loc12 = _oElements.scope || scope;
            var _loc10 = _oElements.command instanceof Function ? (String(_oElements.command.apply(_loc12))) : (String(_oElements.command));
            var _loc6 = _oElements.args instanceof Function ? (_oElements.args.apply(_loc12)) : (_oElements.args);
            var _loc13 = com.mosesSupposes.fuse.FuseKitCommon._validateFuseCommand(_loc10, _aProfiles.length > 0, _loc6 != null && !(_loc6 instanceof Array && _loc6.length == 0), _loc5, false);
            if (_loc13 == true)
            {
                _nPlaying = 1;
                if (!(_loc6 instanceof Array))
                {
                    _loc6 = _loc6 == null ? ([]) : ([_loc6]);
                } // end if
                this.dispatchRequest(String(_loc10), _loc6);
            } // end if
            if (_loc13 == false || _loc10 == "setStartProps")
            {
                this.complete();
            } // end if
            return (null);
        } // end if
        if (_aTweens.length > 0)
        {
            this.stop();
        } // end if
        com.mosesSupposes.fuse.FuseItem._ZigoEngine.addListener(this);
        _nPlaying = 2;
        var _loc4 = null;
        if (_aProfiles.length > 0)
        {
            if (com.mosesSupposes.fuse.FuseItem._ZigoEngine == undefined)
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("112");
            }
            else
            {
                _loc4 = this.doTweens(targs, scope, duration, easing, false, false);
            } // end if
        } // end else if
        _nPlaying = 1;
        var _loc3 = _oElements.aEvents;
        for (var _loc9 in _loc3)
        {
            if (_loc4 == null && _aTweens.length > 0 && _loc3[_loc9].skipLevel == 2)
            {
                continue;
            } // end if
            this.fireEvents(_loc3[_loc9], scope, _loc5, targs);
        } // end of for...in
        if (_loc4 == null && _aTweens.length <= 0 && _nPlaying == 1)
        {
            if (_loc5 == 3)
            {
                com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " no tweens added - item done. [getTimer()=" + getTimer() + "]");
            } // end if
            this.complete();
        } // end if
        return (_loc4);
    } // End of the function
    function stop()
    {
        var _loc2 = _nPlaying > -1;
        _nPlaying = -1;
        if (_loc2 == true)
        {
            this.onStop();
        } // end if
        com.mosesSupposes.fuse.FuseItem._ZigoEngine.removeListener(this);
    } // End of the function
    static function removeInstance(id)
    {
        (com.mosesSupposes.fuse.FuseItem)(com.mosesSupposes.fuse.FuseItem._aInstances[id]).destroy();
        delete com.mosesSupposes.fuse.FuseItem._aInstances[id];
    } // End of the function
    function onStop()
    {
        _bStartSet = false;
        for (var _loc3 in _aTweens)
        {
            var _loc2 = _aTweens[_loc3];
            _loc2.targ.removeListener(this);
            com.mosesSupposes.fuse.FuseItem._ZigoEngine.removeTween(_loc2.targ, _loc2.props);
            delete _aTweens[_loc3];
        } // end of for...in
        delete this._aTweens;
        _bTrigger = false;
    } // End of the function
    function evtSetStart(o)
    {
        if (_sImage.indexOf("StartProps:") == -1 || o.curIndex == _nItemID)
        {
            return;
        } // end if
        if (o.all != true)
        {
            var _loc3 = false;
            for (var _loc4 in o.filter)
            {
                if (Number(o.filter[_loc4]) == _nItemID || String(o.filter[_loc4]) == _oElements.label)
                {
                    _loc3 = true;
                } // end if
            } // end of for...in
            if (_loc3 == false)
            {
                return;
            } // end if
        } // end if
        _nPlaying = 2;
        this.doTweens(o.targs, o.scope, null, null, true, false);
        _nPlaying = -1;
        _bStartSet = true;
    } // End of the function
    function pause(resume)
    {
        if (_nPlaying == -1)
        {
            return;
        } // end if
        _nPlaying = resume == true ? (1) : (0);
        for (var _loc12 in _aTweens)
        {
            var _loc4 = _aTweens[_loc12];
            var _loc2 = _loc4.targ;
            var _loc3 = _loc4.props;
            if (resume == true)
            {
                var _loc5 = [];
                var _loc6 = _aTweens.length;
                for (var _loc8 in _loc3)
                {
                    if (com.mosesSupposes.fuse.FuseItem._ZigoEngine.isTweenPaused(_loc2, _loc3[_loc8]) == false)
                    {
                        _loc5.push(_loc3[_loc8]);
                    } // end if
                } // end of for...in
                if (_loc5.length > 0)
                {
                    this.onTweenEnd({__zigoID__: _loc4.targZID, props: _loc5, isResume: true});
                } // end if
                if (_aTweens.length == _loc6)
                {
                    _loc2.addListener(this);
                    com.mosesSupposes.fuse.FuseItem._ZigoEngine.unpauseTween(_loc2, _loc4.props);
                } // end if
                continue;
            } // end if
            _loc2.removeListener(this);
            com.mosesSupposes.fuse.FuseItem._ZigoEngine.pauseTween(_loc2, _loc4.props);
        } // end of for...in
        if (resume == true && _aTweens.length <= 0)
        {
            this.complete();
        }
        else if (resume == true)
        {
            com.mosesSupposes.fuse.FuseItem._ZigoEngine.addListener(this);
        }
        else
        {
            com.mosesSupposes.fuse.FuseItem._ZigoEngine.removeListener(this);
        } // end else if
    } // End of the function
    function fastForward(ignore, targs, scope)
    {
        if (_nPlaying == 1)
        {
            for (var _loc4 in _aTweens)
            {
                var _loc3 = _aTweens[_loc4];
                var _loc2 = _loc3.targ;
                _loc2.removeListener(this);
                com.mosesSupposes.fuse.FuseItem._ZigoEngine.ffTween(_loc2, _loc3.props, true);
            } // end of for...in
            return;
        } // end if
        if (_nPlaying == 2)
        {
            com.mosesSupposes.fuse.FuseKitCommon.error("125", _nItemID);
        } // end if
        _nPlaying = 2;
        this.doTweens(targs, scope, null, null, false, true);
        stop ();
    } // End of the function
    function destroy()
    {
        var _loc3 = _nPlaying > -1;
        _nPlaying = -1;
        for (var _loc5 in _aTweens)
        {
            var _loc2 = _aTweens[_loc5];
            _loc2.targ.removeListener(this);
            if (_loc3 == true)
            {
                com.mosesSupposes.fuse.FuseItem._ZigoEngine.removeTween(_loc2.targ, _loc2.props);
            } // end if
            delete _aTweens[_loc5];
        } // end of for...in
        for (var _loc4 in this)
        {
            delete this[_loc4];
        } // end of for...in
    } // End of the function
    function dispatchRequest(type, args)
    {
        var _loc4 = _global.com.mosesSupposes.fuse.Fuse.getInstance(_nFuseID);
        if (!(args instanceof Array) && args != null)
        {
            args = new Array(args);
        } // end if
        Function(_loc4[type]).apply(_loc4, args);
    } // End of the function
    function _sID()
    {
        var _loc3;
        if (_nFuseID == -1)
        {
            _loc3 = "-One-off tween ";
        }
        else
        {
            _loc3 = _global.com.mosesSupposes.fuse.Fuse.getInstance(_nFuseID).getHandle();
        } // end else if
        _loc3 = _loc3 + (">Item #" + String(_nItemID));
        if (_oElements.label != undefined)
        {
            _loc3 = _loc3 + (" \"" + _oElements.label + "\"");
        } // end if
        return (_loc3);
    } // End of the function
    function parseProfile(obj, aap)
    {
        var _loc40;
        var _loc2;
        var _loc8;
        if (obj.__buildMode == true)
        {
            if (obj.command != undefined)
            {
                if (obj.command == "delay")
                {
                    _oElements.delay = obj.commandargs;
                }
                else if (obj.command == "trigger")
                {
                    if (_oTemps.bTriggerFound == false)
                    {
                        _oTemps.bTriggerFound = true;
                        return ({trigger: obj.commandargs, _doTimer: true});
                    }
                    else if (_oTemps.outputLevel > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("124", this._sID(), obj.commandargs);
                    } // end else if
                }
                else
                {
                    _oElements.command = obj.command;
                    _oElements.args = obj.commandargs;
                } // end else if
            } // end else if
            if (obj.func != undefined)
            {
                ++_oTemps.afl;
                _oElements.aEvents.unshift({scope: obj.scope, func: obj.func, args: obj.args});
            } // end if
            if (obj.tweenargs != undefined)
            {
                _oTemps.sImgB = _oTemps.sImgB + (obj.tweenargs[1].toString() + ",");
                return (obj);
            } // end if
            return (null);
        } // end if
        var _loc4 = {delay: aap.delay != undefined ? (aap.delay) : (obj.delay), ease: obj.ease, seconds: obj.seconds, event: obj.event, eventparams: obj.eventparams, skipLevel: typeof(obj.skipLevel) == "number" && obj.skipLevel >= 0 && obj.skipLevel <= 2 ? (obj.skipLevel) : (com.mosesSupposes.fuse.FuseItem._ZigoEngine.SKIP_LEVEL), roundResults: obj.roundResults, oSP: {}, oEP: {}, oAFV: {}};
        var _loc22 = aap.trigger != undefined ? (aap.trigger) : (obj.trigger);
        if (_loc22 != undefined)
        {
            if (_oTemps.bTriggerFound == false)
            {
                _loc4.trigger = _loc22;
                _oTemps.bTriggerFound = true;
            }
            else if (_oTemps.outputLevel > 0)
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("124", this._sID(), _loc22);
            } // end if
        } // end else if
        if (_loc4.delay == undefined)
        {
            _loc4.delay = obj.startAt;
        } // end if
        if (_loc4.ease == undefined)
        {
            _loc4.ease = obj.easing;
        } // end if
        if (_loc4.seconds == undefined)
        {
            _loc4.seconds = obj.duration != undefined ? (obj.duration) : (obj.time);
        } // end if
        if (aap.target != undefined)
        {
            _loc4.target = aap.target instanceof Array ? (aap.target) : ([aap.target]);
        }
        else if (obj.target != undefined)
        {
            _loc4.target = obj.target instanceof Array ? (obj.target) : ([obj.target]);
        } // end else if
        if (obj.addTarget != undefined)
        {
            _loc4.addTarget = obj.addTarget instanceof Array ? (obj.addTarget) : ([obj.addTarget]);
        } // end if
        if (aap.addTarget != undefined)
        {
            if (_loc4.addTarget == undefined)
            {
                _loc4.addTarget = aap.addTarget instanceof Array ? (aap.addTarget) : ([aap.addTarget]);
            }
            else
            {
                _loc4.addTarget = _loc4.addTarget instanceof Array ? (_loc4.addTarget.concat(aap.addTarget)) : (new Array(_loc4.addTarget).concat(aap.addTarget));
            } // end if
        } // end else if
        var _loc15 = false;
        for (var _loc2 in obj)
        {
            var _loc11 = obj[_loc2];
            if (_oTemps.cbProps.indexOf("|" + _loc2 + "|") > -1)
            {
                if (_loc2 != "skipLevel")
                {
                    _loc4[_loc2] = _loc11;
                } // end if
                continue;
            } // end if
            if (_oTemps.fuseProps.indexOf("|" + _loc2 + "|") > -1)
            {
                if (_loc2 == "command" && _oTemps.nActions > 1 && _oTemps.outputLevel > 0)
                {
                    com.mosesSupposes.fuse.FuseKitCommon.error("109", String(_loc11), true);
                } // end if
                continue;
            } // end if
            if (typeof(_loc11) == "object")
            {
                var _loc10 = _loc11 instanceof Array ? ([]) : ({});
                for (var _loc8 in _loc11)
                {
                    _loc10[_loc8] = _loc11[_loc8];
                } // end of for...in
                _loc11 = _loc10;
            } // end if
            var _loc3;
            var _loc21;
            if (_loc2.indexOf("start_") == 0)
            {
                if (_loc2 == "start_controlX" || _loc2 == "start_controlY" || _loc2.indexOf("_bezier_") > -1)
                {
                    if (_oTemps.outputLevel > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("110", this._sID(), _loc2);
                    } // end if
                    continue;
                } // end if
                _loc2 = _loc2.slice(6);
                _loc3 = _loc4.oSP;
            }
            else
            {
                _loc3 = _loc4.oEP;
            } // end else if
            if (com.mosesSupposes.fuse.FuseItem.ADD_UNDERSCORES == true && _oTemps.sUP.indexOf("|_" + _loc2 + "|") > -1)
            {
                _loc2 = "_" + _loc2;
            } // end if
            if (_oTemps.sCT.indexOf("|" + _loc2 + "|") > -1)
            {
                var _loc13 = _loc2 == "_tintPercent" && _loc3.colorProp.p == "_tint";
                var _loc12 = _loc2 == "_tint" && _loc3.colorProp.p == "_tintPercent";
                if (_loc3.colorProp == undefined || _loc13 == true || _loc12 == true)
                {
                    if (_loc13 == true)
                    {
                        _loc3.colorProp = {p: "_tint", v: {tint: _loc3.colorProp.v, percent: _loc11}};
                    }
                    else if (_loc12 == true)
                    {
                        _loc3.colorProp = {p: "_tint", v: {tint: _loc11, percent: _loc3.colorProp.v}};
                    }
                    else
                    {
                        _loc3.colorProp = {p: _loc2, v: _loc11};
                    } // end else if
                    _loc15 = true;
                }
                else if (_oTemps.outputLevel > 0)
                {
                    com.mosesSupposes.fuse.FuseKitCommon.error("115", this._sID(), _loc2);
                } // end else if
                continue;
            } // end if
            if (_loc11 != null)
            {
                _loc3[_loc2] = _loc11;
                _loc15 = true;
            } // end if
        } // end of for...in
        if (_loc15 == false && (_loc4.trigger != undefined || (_loc4.delay != undefined || _loc4.seconds != undefined) && (_loc4.startfunc != undefined || _loc4.updfunc != undefined || _loc4.func != undefined && _oTemps.nActions > 1)))
        {
            if (com.mosesSupposes.fuse.FuseItem._ZigoEngine == undefined)
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("116");
            }
            else
            {
                if (_loc4.func != undefined)
                {
                    ++_oTemps.afl;
                } // end if
                if (_loc4.event != undefined)
                {
                    ++_oTemps.ael;
                } // end if
                _loc4._doTimer = true;
                if (_loc4.delay != undefined)
                {
                    _oTemps.twDelayFlag = true;
                } // end if
                return (_loc4);
            } // end if
        } // end else if
        if (_loc15 == true)
        {
            var _loc17 = _loc4.oEP.colorProp != undefined;
            for (var _loc7 = 0; _loc7 < 2; ++_loc7)
            {
                _loc3 = _loc7 == 0 ? (_loc4.oSP) : (_loc4.oEP);
                var _loc6 = _loc7 == 0 ? (_oTemps.sImgS) : (_oTemps.sImgE);
                var _loc9 = _loc3.colorProp.p;
                if (_loc9 != undefined)
                {
                    _loc3[_loc9] = _loc3.colorProp.v;
                    delete _loc3.colorProp;
                } // end if
                if ((_loc3._xscale != undefined || _loc3._scale != undefined) && (_loc3._width != undefined || _loc3._size != undefined))
                {
                    var _loc14 = _loc3._xscale != undefined ? ("_xscale") : ("_scale");
                    delete _loc3[_loc14];
                    if (_oTemps.outputLevel > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("115", this._sID(), _loc14);
                    } // end if
                } // end if
                if ((_loc3._yscale != undefined || _loc3._scale != undefined) && (_loc3._height != undefined || _loc3._size != undefined))
                {
                    _loc14 = _loc3._yscale != undefined ? ("_yscale") : ("_scale");
                    delete _loc3[_loc14];
                    if (_oTemps.outputLevel > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("115", this._sID(), _loc14);
                    } // end if
                } // end if
                if (_loc3._fade != undefined && _loc3._alpha != undefined)
                {
                    delete _loc3._alpha;
                    if (_oTemps.outputLevel > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("115", this._sID(), "_alpha");
                    } // end if
                } // end if
                for (var _loc2 in _loc3)
                {
                    if (_loc6.indexOf(_loc2 + ", ") == -1)
                    {
                        _loc6 = _loc6 + (_loc2 + ", ");
                    } // end if
                    if (_loc3 == _loc4.oSP)
                    {
                        if (_loc4.oEP[_loc2] == undefined && !(_loc2 == _loc9 && _loc17 == true))
                        {
                            _loc4.oAFV[_loc2] = true;
                            _loc4.oEP[_loc2] = [];
                        } // end if
                    } // end if
                } // end of for...in
                _loc7 == 0 ? (_oTemps.sImgS = _loc6) : (_oTemps.sImgE = _loc6);
            } // end of for
            return (_loc4);
        } // end if
        if (_loc4.delay != undefined && _oTemps.nActions == 1)
        {
            _oElements.delay = _loc4.delay;
            _oElements.delayscope = _loc4.scope;
        } // end if
        if (_loc4.event != undefined)
        {
            ++_oTemps.ael;
            _oElements.aEvents.unshift({scope: _loc4.scope, e: _loc4.event, ep: _loc4.eventparams, skipLevel: _loc4.skipLevel});
        } // end if
        var _loc23 = _oElements.aEvents.length;
        if (_loc4.func != undefined)
        {
            _oElements.aEvents.push({func: _loc4.func, scope: _loc4.scope, args: _loc4.args, skipLevel: _loc4.skipLevel});
        } // end if
        _oTemps.afl = _oTemps.afl + (_oElements.aEvents.length - _loc23);
        false;
        return;
    } // End of the function
    function doTweens(targs, defaultScope, defaultSeconds, defaultEase, setStart, isFF)
    {
        if (_aTweens == null)
        {
            _aTweens = [];
        } // end if
        var tba = _oTwBeingAdded = {};
        var ZE = com.mosesSupposes.fuse.FuseItem._ZigoEngine;
        var _loc35 = function (target, props, endvals, seconds, ease, delay, callback)
        {
            if (target.__zigoID__ == null)
            {
                ZE.initializeTargets(target);
            } // end if
            tba[target.__zigoID__] = true;
            var _loc3 = ZE.doTween.apply(ZE, arguments);
            tba[target.__zigoID__] = false;
            return (_loc3 == null ? ([]) : (_loc3.split(",")));
        };
        var _loc74 = _global.com.mosesSupposes.fuse.Fuse;
        var _loc19 = _loc74 != undefined ? (_loc74.OUTPUT_LEVEL) : (com.mosesSupposes.fuse.FuseItem._ZigoEngine.OUTPUT_LEVEL);
        var _loc24 = "";
        var _loc66 = 0;
        var _loc8;
        var _loc5;
        var _loc6;
        var _loc75 = _bStartSet != true && (setStart == true || _sImage.indexOf("StartProps:") > -1);
        for (var _loc51 = 0; _loc51 < _aProfiles.length; ++_loc51)
        {
            if (_nPlaying < 2)
            {
                return (null);
            } // end if
            var _loc4 = _aProfiles[_loc51];
            if (_loc4.__buildMode == true)
            {
                var _loc10 = _aProfiles[_loc51].tweenargs;
                var _loc65 = _loc24;
                if (_loc10[6].cycles === 0 || _loc10[6].cycles.toUpperCase() == "LOOP")
                {
                    delete _loc10[6].cycles;
                    if (_loc19 > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("117", this._sID());
                    } // end if
                } // end if
                var _loc60 = com.mosesSupposes.fuse.FuseKitCommon.parseCallback(_loc10[6]);
                if (!(_loc10[0] instanceof Array))
                {
                    _loc10[0] = [_loc10[0]];
                } // end if
                for (var _loc8 in _loc10[0])
                {
                    if (isFF == true)
                    {
                        _loc35(_loc10[0][_loc8], _loc10[1], _loc10[2], 0, null, 0, {skipLevel: 0});
                        if (_loc19 == 3)
                        {
                            com.mosesSupposes.fuse.FuseKitCommon.output("\n-" + this._sID() + " FF(simple syntax)\ttargets:[" + _loc10[0][_loc8] + "]\tprops:[" + _loc10[1] + "]");
                        } // end if
                        continue;
                    } // end if
                    var _loc17 = _loc35(_loc10[0][_loc8], _loc10[1], _loc10[2], _loc10[3], _loc10[4], _loc10[5], _loc60);
                    if (_loc17.length > 0)
                    {
                        _aTweens.push({targ: _loc10[0][_loc8], props: _loc17, targZID: _loc10[0][_loc8].__zigoID__});
                        _loc10[0][_loc8].addListener(this);
                        for (var _loc5 in _loc17)
                        {
                            if (_loc24.indexOf(_loc17[_loc5] + ",") == -1)
                            {
                                _loc24 = _loc24 + (_loc17[_loc5] + ",");
                            } // end if
                        } // end of for...in
                    } // end if
                    if (_loc19 == 3)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.output("\n-" + this._sID() + " TWEEN(simple syntax)\ttargets:[" + _loc10[0][_loc8] + "]\tprops tweened:[" + _loc17.toString() + "]");
                    } // end if
                } // end of for...in
                if (isFF == false && (_loc65 == _loc24 || _loc24 == ""))
                {
                    ++_loc66;
                } // end if
                continue;
            } // end if
            var _loc23 = defaultScope;
            var _loc16 = [];
            var _loc52 = _loc4.target == undefined ? (targs) : (_loc4.target);
            var _loc26 = [];
            var _loc54 = false;
            for (var _loc8 in _loc52)
            {
                var _loc7 = _loc52[_loc8];
                _loc26 = _loc26.concat(_loc7 instanceof Function ? (_loc7.apply(_loc23)) : (_loc7));
            } // end of for...in
            for (var _loc8 in _loc4.addTarget)
            {
                _loc7 = _loc4.addTarget[_loc8];
                _loc26 = _loc26.concat(_loc7 instanceof Function ? (_loc7.apply(_loc23)) : (_loc7));
            } // end of for...in
            for (var _loc8 in _loc26)
            {
                _loc7 = _loc26[_loc8];
                if (_loc7 != null)
                {
                    var _loc40 = false;
                    for (var _loc5 in _loc16)
                    {
                        if (_loc16[_loc5] == _loc7)
                        {
                            _loc40 = true;
                            break;
                        } // end if
                    } // end of for...in
                    if (_loc40 == false)
                    {
                        _loc16.unshift(_loc7);
                    } // end if
                    continue;
                } // end if
                _loc54 = true;
            } // end of for...in
            var _loc56 = _loc4._doTimer == true && _loc16.length == 0 && isFF == false;
            if (_loc54 == true || _loc16.length == 0 && _loc4._doTimer != true)
            {
                ++_loc66;
            } // end if
            if (_loc75 == true)
            {
                for (var _loc8 in _loc16)
                {
                    if (_nPlaying < 2)
                    {
                        return (null);
                    } // end if
                    var _loc36 = _loc16[_loc8];
                    var _loc33 = [];
                    var _loc27 = [];
                    if (setStart == true)
                    {
                        for (var _loc63 in _loc4.oEP)
                        {
                            _global.com.mosesSupposes.fuse.FuseFMP.getFilterProp(_loc36, _loc63, true);
                        } // end of for...in
                    } // end if
                    for (var _loc64 in _loc4.oSP)
                    {
                        _loc7 = _loc4.oSP[_loc64];
                        if (_loc7 instanceof Function)
                        {
                            _loc7 = _loc7.apply(_loc23);
                        } // end if
                        if (_loc7 === true || _loc7 === false)
                        {
                            _loc36[_loc64] = _loc7;
                            if (_loc4.oAFV[_loc64] == true)
                            {
                                for (var _loc6 in _loc4.oEP[_loc64])
                                {
                                    if (_loc4.oEP[_loc64][_loc6].targ == _loc36)
                                    {
                                        _loc4.oEP[_loc64].splice(Number(_loc6), 1);
                                    } // end if
                                } // end of for...in
                                _loc4.oEP[_loc64].push({targ: _loc36, val: "IGNORE", _isAF: true});
                            } // end if
                            continue;
                        } // end if
                        if (_loc4.oAFV[_loc64] == true && !(_loc64 == "_colorReset" && _loc7 == 100) && !(_loc64 == "_tintPercent" && _loc7 == 0))
                        {
                            var _loc20;
                            if (_loc64 == "_tint" || _loc64 == "_colorTransform")
                            {
                                _loc20 = com.mosesSupposes.fuse.FuseItem._ZigoEngine.getColorTransObj();
                            }
                            else if (String(com.mosesSupposes.fuse.FuseKitCommon._resetTo100()).indexOf("|" + _loc64 + "|") > -1 || _loc64 == "_fade" && _loc7 < 50)
                            {
                                _loc20 = 100;
                            }
                            else if (String(com.mosesSupposes.fuse.FuseKitCommon._resetTo0()).indexOf("|" + _loc64 + "|") > -1 || _loc64 == "_fade")
                            {
                                _loc20 = 0;
                            }
                            else
                            {
                                var _loc29 = _global.com.mosesSupposes.fuse.FuseFMP.getFilterProp(_loc36, _loc64, true);
                                if (_loc29 != null)
                                {
                                    _loc20 = _loc29;
                                }
                                else
                                {
                                    _loc20 = _global.isNaN(_loc36[_loc64]) == false ? (_loc36[_loc64]) : (0);
                                } // end else if
                            } // end else if
                            for (var _loc6 in _loc4.oEP[_loc64])
                            {
                                if (_loc4.oEP[_loc64][_loc6].targ == _loc36)
                                {
                                    _loc4.oEP[_loc64].splice(Number(_loc6), 1);
                                } // end if
                            } // end of for...in
                            _loc4.oEP[_loc64].push({targ: _loc36, val: _loc20, _isAF: true});
                        } // end if
                        if (typeof(_loc7) == "object")
                        {
                            var _loc28 = _loc7 instanceof Array ? ([]) : ({});
                            for (var _loc6 in _loc7)
                            {
                                _loc28[_loc6] = _loc7[_loc6] instanceof Function ? (Function(_loc7[_loc6]).apply(_loc23)) : (_loc7[_loc6]);
                            } // end of for...in
                            _loc7 = _loc28;
                        } // end if
                        _loc33.push(_loc64);
                        _loc27.push(_loc7);
                    } // end of for...in
                    if (_loc27.length > 0)
                    {
                        if (_loc19 == 3)
                        {
                            com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " " + _loc36 + " SET STARTS: " + ["[" + _loc33 + "]", "[" + _loc27 + "]"]);
                        } // end if
                        _loc35(_loc36, _loc33, _loc27, 0);
                    } // end if
                } // end of for...in
            } // end if
            if (setStart == true)
            {
                continue;
            } // end if
            var _loc25;
            var _loc50;
            var _loc32;
            var _loc12;
            var _loc49;
            var _loc21;
            var _loc43 = "";
            if (isFF == false)
            {
                if (_loc4.scope != undefined)
                {
                    _loc23 = _loc4.scope;
                } // end if
                _loc50 = _loc4.skipLevel instanceof Function ? (_loc4.skipLevel.apply(_loc23)) : (_loc4.skipLevel);
                var _loc71 = _loc4.extra1 instanceof Function ? (_loc4.extra1.apply(_loc23)) : (_loc4.extra1);
                var _loc70 = _loc4.extra2 instanceof Function ? (_loc4.extra2.apply(_loc23)) : (_loc4.extra2);
                var _loc72 = _loc4.roundResults instanceof Function ? (_loc4.roundResults.apply(_loc23)) : (_loc4.roundResults);
                _loc32 = {skipLevel: _loc50, extra1: _loc71, extra2: _loc70, roundResults: _loc72};
                _loc12 = {skipLevel: _loc50, extra1: _loc71, extra2: _loc70, roundResults: _loc72};
                if (_loc4.cycles != undefined)
                {
                    var _loc55 = _loc4.cycles instanceof Function ? (_loc4.cycles.apply(_loc23)) : (_loc4.cycles);
                    if ((Number(_loc55) == 0 || String(_loc55).toUpperCase() == "LOOP") && _loc74 != undefined)
                    {
                        delete _loc4.cycles;
                        if (_loc19 > 0)
                        {
                            com.mosesSupposes.fuse.FuseKitCommon.error("117", this._sID());
                        } // end if
                    }
                    else
                    {
                        _loc32.cycles = _loc12.cycles = _loc55;
                    } // end if
                } // end else if
                if (_loc4.func != undefined || _loc4.startfunc != undefined || _loc4.updfunc != undefined)
                {
                    for (var _loc8 in _loc4)
                    {
                        if (_loc8.indexOf("func") > -1)
                        {
                            _loc12[_loc8] = _loc4[_loc8];
                            continue;
                        } // end if
                        if (_loc8 == "startscope" || _loc8 == "updscope" || _loc8.indexOf("args") > -1)
                        {
                            _loc12[_loc8] = _loc4[_loc8];
                        } // end if
                    } // end of for...in
                    if (_loc23 != undefined)
                    {
                        if (_loc12.func != undefined && _loc12.scope == undefined)
                        {
                            _loc12.scope = _loc23;
                        } // end if
                        if (_loc12.updfunc != undefined && _loc12.updscope == undefined)
                        {
                            _loc12.updscope = _loc23;
                        } // end if
                        if (_loc12.startfunc != undefined && _loc12.startscope == undefined)
                        {
                            _loc12.startscope = _loc23;
                        } // end if
                    } // end if
                } // end if
                for (var _loc5 in _loc12)
                {
                    _loc43 = _loc43 + (_loc5 + ":" + _loc12[_loc5] + "|");
                } // end of for...in
                if (_loc4.event != undefined)
                {
                    _loc25 = {scope: _loc4.scope, e: _loc4.event, ep: _loc4.eventparams, skipLevel: _loc50};
                } // end if
                _loc49 = _loc4.trigger === true;
                _loc21 = undefined;
                if (_loc49 == false && _loc4.trigger != undefined)
                {
                    _loc21 = _loc4.trigger instanceof Function ? (_loc4.trigger.apply(_loc23)) : (_loc4.trigger);
                    if (typeof(_loc21) == "string")
                    {
                        _loc21 = String(_loc21).charAt(0) == "-" ? (-this.parseClock(String(_loc21).slice(1))) : (this.parseClock(String(_loc21)));
                    } // end if
                    if (_global.isNaN(_loc21) == true)
                    {
                        _loc21 = undefined;
                    } // end if
                } // end if
            } // end if
            var _loc18;
            var _loc13;
            var _loc22;
            var _loc15;
            var _loc42 = false;
            var _loc53 = _loc56 == false ? (_loc16) : ([0]);
            var _loc34 = -1;
            for (var _loc8 in _loc53)
            {
                if (_nPlaying < 2)
                {
                    return (null);
                } // end if
                if (isFF == false)
                {
                    if (_loc4.ease != null)
                    {
                        _loc22 = _loc4.ease;
                        if (_loc22 instanceof Function)
                        {
                            var _loc44 = Function(_loc22);
                            if (typeof(_loc44(1, 1, 1, 1)) != "number")
                            {
                                _loc22 = _loc44.apply(_loc23);
                            } // end if
                        } // end if
                    } // end if
                    if (_loc22 == null)
                    {
                        _loc22 = defaultEase;
                    } // end if
                    _loc13 = _loc4.seconds instanceof Function ? (_loc4.seconds.apply(_loc23)) : (_loc4.seconds);
                    if (_loc13 != undefined)
                    {
                        if (typeof(_loc13) == "string")
                        {
                            _loc13 = this.parseClock(String(_loc13));
                        } // end if
                        if (_global.isNaN(_loc13) == true)
                        {
                            _loc13 = com.mosesSupposes.fuse.FuseItem._ZigoEngine.DURATION || 0;
                        } // end if
                    } // end if
                    if (_loc13 == null)
                    {
                        _loc13 = defaultSeconds;
                    } // end if
                    _loc18 = _loc4.delay instanceof Function ? (_loc4.delay.apply(_loc23)) : (_loc4.delay);
                    if (typeof(_loc18) == "string")
                    {
                        _loc18 = this.parseClock(String(_loc18));
                    } // end if
                    if (_loc18 == null || _global.isNaN(_loc18) == true)
                    {
                        _loc18 = 0;
                    } // end if
                    if (_loc56 == true)
                    {
                        continue;
                    } // end if
                } // end if
                _loc36 = _loc53[_loc8];
                var _loc9 = [];
                var _loc11 = [];
                var _loc46 = 0;
                var _loc14 = -2;
                for (var _loc64 in _loc4.oEP)
                {
                    _loc7 = _loc4.oEP[_loc64];
                    if (_loc7 instanceof Function)
                    {
                        _loc7 = _loc7.apply(_loc23);
                    } // end if
                    if (_loc7 === true || _loc7 === false)
                    {
                        if (_loc15 == undefined)
                        {
                            _loc15 = {};
                        } // end if
                        _loc15[_loc64] = _loc7;
                        ++_loc46;
                        continue;
                    } // end if
                    if (typeof(_loc7) == "object")
                    {
                        if (_loc7[0]._isAF == true)
                        {
                            for (var _loc6 in _loc7)
                            {
                                if (_loc7[_loc6].targ == _loc36)
                                {
                                    _loc7 = _loc7[_loc6].val;
                                    break;
                                } // end if
                            } // end of for...in
                        }
                        else
                        {
                            _loc28 = _loc7 instanceof Array ? ([]) : ({});
                            for (var _loc6 in _loc7)
                            {
                                _loc28[_loc6] = _loc7[_loc6] instanceof Function ? (Function(_loc7[_loc6]).apply(_loc23)) : (_loc7[_loc6]);
                            } // end of for...in
                            _loc7 = _loc28;
                        } // end if
                    } // end else if
                    if (_loc7 != "IGNORE")
                    {
                        if (_loc64 == "_bezier_")
                        {
                            _loc14 = _loc9.length;
                        }
                        else if (_loc14 == -2 && (_loc64 == "controlX" || _loc64 == "controlY"))
                        {
                            _loc14 = -1;
                        } // end else if
                        _loc9.push(_loc64);
                        _loc11.push(_loc7);
                    } // end if
                } // end of for...in
                if (_loc11.length > 0)
                {
                    if (_loc14 > -2)
                    {
                        if (_loc14 == -1)
                        {
                            _loc14 = _loc9.length;
                        } // end if
                        _loc9[_loc14] = "_bezier_";
                        if (typeof(_loc11[_loc14]) != "object")
                        {
                            _loc11[_loc14] = {};
                        } // end if
                        var _loc37 = _loc11[_loc14];
                        for (var _loc5 in _loc9)
                        {
                            if ("|x|y|_x|_y|controlX|controlY|".indexOf("|" + _loc9[_loc5] + "|") > -1)
                            {
                                if (_loc9[_loc5].charAt(0) == "_")
                                {
                                    _loc9[_loc5] = _loc9[_loc5].slice(-1);
                                } // end if
                                if (typeof(_loc37[_loc9[_loc5]]) == "number")
                                {
                                    if (_loc19 > 0 && (_loc34 == -1 || _loc34 == _loc8))
                                    {
                                        com.mosesSupposes.fuse.FuseKitCommon.error("115", this._sID(), _loc9[_loc5]);
                                        _loc34 = Number(_loc8);
                                    } // end if
                                }
                                else
                                {
                                    _loc37[_loc9[_loc5]] = _loc11[_loc5];
                                } // end else if
                                _loc9.splice(Number(_loc5), 1);
                                _loc11.splice(Number(_loc5), 1);
                            } // end if
                        } // end of for...in
                    } // end if
                    if (isFF == true)
                    {
                        if (_loc19 == 3)
                        {
                            com.mosesSupposes.fuse.FuseKitCommon.output("\n-" + this._sID() + " FF\ttargets:[" + _loc36 + "]\tprops:[" + _loc9.toString() + "]");
                        } // end if
                        _loc35(_loc36, _loc9, _loc11, 0, null, 0, {skipLevel: 0});
                        continue;
                    } // end if
                    var _loc31 = {caught: false, onTweenEnd: function (evto)
                    {
                        caught = true;
                    }};
                    _loc36.addListener(_loc31);
                    _loc17 = _loc35(_loc36, _loc9, _loc11, _loc13, _loc22, _loc18, _loc12);
                    _loc36.removeListener(_loc31);
                    if (_loc17.length == 0)
                    {
                        if (_loc31.caught == true)
                        {
                            _loc12 = _loc32;
                        } // end if
                    }
                    else
                    {
                        if (_loc17.length > 0)
                        {
                            var _loc38 = {targ: _loc36, props: _loc17, bools: _loc15, targZID: _loc36.__zigoID__};
                            if (_loc42 == false)
                            {
                                _loc12 = _loc32;
                                _loc38.event = _loc25;
                                _loc15 = undefined;
                                _loc25 = undefined;
                                _loc38.trigger = _loc49;
                            } // end if
                            _aTweens.push(_loc38);
                            _loc36.addListener(this);
                            _loc42 = true;
                            for (var _loc5 in _loc17)
                            {
                                if (_loc24.indexOf(_loc17[_loc5] + ",") == -1)
                                {
                                    _loc24 = _loc24 + (_loc17[_loc5] + ",");
                                } // end if
                            } // end of for...in
                        } // end if
                        if (_loc19 == 3)
                        {
                            var _loc45 = _loc9.toString();
                            if (_loc17.length > _loc9.length)
                            {
                                _loc45 = _loc45 + ("\n\t[NO-CHANGE PROPS DISCARDED (disregard this for double props like _scale). KEPT:" + _loc17.toString() + "]");
                            } // end if
                            var _loc30 = "";
                            for (var _loc5 in _loc11)
                            {
                                _loc30 = (typeof(_loc11[_loc5]) == "string" ? ("\"" + _loc11[_loc5] + "\"") : (_loc11[_loc5])) + ", " + _loc30;
                            } // end of for...in
                            com.mosesSupposes.fuse.FuseKitCommon.output("\n-" + this._sID() + " TWEEN:\n" + ["\t[getTimer():" + getTimer() + "] ", "targ: " + _loc36, "props: " + _loc45, "endVals: " + _loc30, "time: " + (_loc13 == undefined ? (com.mosesSupposes.fuse.FuseItem._ZigoEngine.DURATION) : (_loc13)), "easing: " + (_loc22 == undefined ? (com.mosesSupposes.fuse.FuseItem._ZigoEngine.EASING) : (_loc22)), "delay: " + (_loc18 == undefined ? (0) : (_loc18)), "callbacks: " + (_loc43 == "" ? ("(none)") : (_loc43))].join("\n\t"));
                        } // end if
                    } // end else if
                    _loc31 = undefined;
                } // end if
            } // end of for...in
            if (_global.isNaN(_loc13) == true || _loc4.seconds == null)
            {
                _loc13 = 0;
            } // end if
            var _loc39 = _loc18 + _loc13;
            if (_loc21 != undefined)
            {
                if (_loc21 < 0)
                {
                    _loc21 = _loc21 + _loc39;
                } // end if
                if (_loc21 > 0 && (_loc39 == 0 || _loc21 < _loc39))
                {
                    if (_loc39 == 0)
                    {
                        if (_loc19 == 3)
                        {
                            com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " graft a timed trigger (" + _loc21 + " sec). [has callback:" + (_loc12 != _loc32) + ", has event:" + (_loc25 != undefined) + ", has booleans:" + (_loc15 != undefined) + "]");
                        } // end if
                        this.doTimerTween(null, _loc21, 0, true, _loc15, _loc12, _loc25);
                        _loc42 = true;
                    }
                    else
                    {
                        if (_loc19 == 3)
                        {
                            com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " graft a timed trigger (" + _loc21 + " sec).");
                        } // end if
                        this.doTimerTween(null, _loc21, 0, true);
                    } // end else if
                }
                else if (_loc19 == 3)
                {
                    com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " timed trigger discarded: out of range. [" + _loc21 + "/" + _loc39 + "]");
                } // end if
            } // end else if
            if (_loc42 == false && (_loc12 != _loc32 || _loc25 != undefined || _loc15 != undefined))
            {
                if (_loc50 == 0 && _loc39 > 0)
                {
                    if (_loc19 == 3)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " no props tweened - graft a delay (" + _loc39 + " sec). [has callback:" + (_loc12 != _loc32) + ", has event:" + (_loc25 != undefined) + ", has booleans:" + (_loc15 != undefined) + "]");
                    } // end if
                    this.doTimerTween(_loc16, _loc13, _loc18, _loc49, _loc15, _loc12, _loc25);
                    continue;
                } // end if
                if (_loc19 == 3)
                {
                    com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " no props tweened, executing nontween items. [has callback:" + (_loc12 != _loc32) + ", has event:" + (_loc25 != undefined) + ", has booleans:" + (_loc15 != undefined) + "]");
                } // end if
                for (var _loc8 in _loc16)
                {
                    for (var _loc5 in _loc15)
                    {
                        _loc16[_loc8][_loc5] = _loc15[_loc5];
                    } // end of for...in
                } // end of for...in
                if (_loc50 < 2)
                {
                    if (_loc12 != undefined)
                    {
                        this.fireEvents(_loc12, _loc23, _loc19, _loc16);
                    } // end if
                    if (_loc25 != undefined)
                    {
                        this.fireEvents(_loc25, _loc23, _loc19);
                    } // end if
                } // end if
            } // end if
        } // end of for
        if (_loc66 > 0 && _loc19 > 0)
        {
            if (_loc66 == _aProfiles.length && _loc24 == "")
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("118", this._sID(), setStart);
            }
            else
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("119", this._sID(), _loc66, _loc75);
            } // end if
        } // end else if
        tba = undefined;
        _oTwBeingAdded = undefined;
        return (_loc24 == "" ? (null) : (_loc24.slice(0, -1)));
    } // End of the function
    function doTimerTween(actualTargets, duration, delay, trigger, booleans, callback, event)
    {
        var _loc2 = {__TweenedDelay: 0};
        com.mosesSupposes.fuse.FuseItem._ZigoEngine.initializeTargets(_loc2);
        _aTweens.push({targ: _loc2, props: ["__TweenedDelay"], trigger: trigger, bools: booleans, event: event, actualTargs: actualTargets, targZID: _loc2.__zigoID__});
        var _loc3 = com.mosesSupposes.fuse.FuseItem._ZigoEngine.doTween(_loc2, "__TweenedDelay", 1, duration, null, delay, callback) == null;
        if (_loc3 == true)
        {
            this.onTweenEnd({target: _loc2, props: ["__TweenedDelay"]});
        }
        else
        {
            _loc2.addListener(this);
        } // end else if
    } // End of the function
    function onTweenEnd(o, doAutoStop)
    {
        if (_nPlaying < 1)
        {
            return;
        } // end if
        var _loc12 = _global.com.mosesSupposes.fuse.Fuse;
        var _loc8 = _loc12 != undefined ? (_loc12.OUTPUT_LEVEL) : (com.mosesSupposes.fuse.FuseItem._ZigoEngine.OUTPUT_LEVEL);
        var _loc11 = o.__zigoID__ !== undefined ? (o.__zigoID__) : (o.target.__zigoID__);
        for (var _loc19 in _aTweens)
        {
            var _loc3 = _aTweens[_loc19];
            if (_loc3.targZID == _loc11)
            {
                for (var _loc18 in o.props)
                {
                    var _loc7 = _loc3.props;
                    for (var _loc16 in _loc7)
                    {
                        var _loc5 = _loc7[_loc16];
                        if (_loc5 == o.props[_loc18])
                        {
                            _loc7.splice(Number(_loc16), 1);
                            if (doAutoStop == true)
                            {
                                var _loc6 = _loc12.getInstance(_nFuseID);
                                var _loc10 = _bTrigger == true && o.during == "add" && _loc6[_loc6.currentIndex]._oTwBeingAdded[_loc11] === true && _loc6.state == "playing";
                                if (_loc10 == false)
                                {
                                    _loc3.targ.removeListener(this);
                                    for (var _loc19 in _aTweens)
                                    {
                                        if (_aTweens[_loc19].targZID == _loc11)
                                        {
                                            for (var _loc18 in o.props)
                                            {
                                                for (var _loc16 in _aTweens[_loc19].props)
                                                {
                                                    if (_aTweens[_loc19].props[_loc16] == o.props[_loc18])
                                                    {
                                                        _aTweens[_loc19].props.splice(Number(_loc16), 1);
                                                    } // end if
                                                } // end of for...in
                                            } // end of for...in
                                            if (_aTweens[_loc19].props.length == 0)
                                            {
                                                _aTweens.splice(Number(_loc19), 1);
                                            } // end if
                                        } // end if
                                    } // end of for...in
                                    if (_loc8 == 3)
                                    {
                                        com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " triggering auto-stop due to interruption");
                                    } // end if
                                    if (_loc6.autoClear == true || _loc6.autoClear !== false && _loc12.AUTOCLEAR == true)
                                    {
                                        this.dispatchRequest("destroy");
                                    }
                                    else
                                    {
                                        this.dispatchRequest("stop");
                                    } // end else if
                                    return;
                                }
                                else if (_loc8 == 3)
                                {
                                    com.mosesSupposes.fuse.FuseKitCommon.output("note -" + this._sID() + " interrupted one of its own properties \"" + _loc5 + "\". (Autostop not triggered.)");
                                } // end if
                            } // end else if
                            if (_nPlaying == 2 && _loc5 != "__TweenedDelay")
                            {
                                if (_loc8 > 0)
                                {
                                    com.mosesSupposes.fuse.FuseKitCommon.error("120", this._sID(), _loc5);
                                } // end if
                            } // end if
                            if (_loc7.length == 0)
                            {
                                if (_loc3.event != undefined)
                                {
                                    this.fireEvents(_loc3.event, null, _loc8);
                                } // end if
                                if (_loc5 == "__TweenedDelay")
                                {
                                    com.mosesSupposes.fuse.FuseItem._ZigoEngine.deinitializeTargets(_loc3.targ);
                                    delete _loc3.targ;
                                    for (var _loc14 in _loc3.bools)
                                    {
                                        for (var _loc13 in _loc3.actualTargs)
                                        {
                                            _loc3.actualTargs[_loc13][_loc14] = _loc3.bools[_loc14];
                                        } // end of for...in
                                    } // end of for...in
                                }
                                else
                                {
                                    var _loc9 = false;
                                    for (var _loc14 in _loc3.bools)
                                    {
                                        _loc3.targ[_loc14] = _loc3.bools[_loc14];
                                    } // end of for...in
                                    for (var _loc15 in _aTweens)
                                    {
                                        if (_loc15 != _loc19 && _aTweens[_loc15].targ == _loc3.targ)
                                        {
                                            _loc9 = true;
                                        } // end if
                                    } // end of for...in
                                    if (_loc9 == false)
                                    {
                                        _loc3.targ.removeListener(this);
                                    } // end if
                                } // end else if
                                if (_loc3.trigger == true)
                                {
                                    if (_bTrigger == false && o.isResume != true && _aTweens.length > 1)
                                    {
                                        _bTrigger = true;
                                        if (_loc8 == 3)
                                        {
                                            com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " trigger fired!");
                                        } // end if
                                        var breakChainInt;
                                        breakChainInt = setInterval(function (fi)
                                        {
                                            clearInterval(breakChainInt);
                                            fi.dispatchRequest("advance", [false, false, false]);
                                        }, 1, this);
                                    } // end if
                                } // end if
                                _aTweens.splice(Number(_loc19), 1);
                            } // end if
                        } // end if
                    } // end of for...in
                } // end of for...in
            } // end if
        } // end of for...in
        if (_aTweens.length == 0 && _nPlaying == 1 && o.isResume != true)
        {
            this.complete(_loc8);
        } // end if
    } // End of the function
    function onTweenInterrupt(o)
    {
        if (_nPlaying == -1)
        {
            return;
        } // end if
        var _loc4 = _global.com.mosesSupposes.fuse.Fuse;
        var _loc6 = _loc4.getInstance(_nFuseID);
        var _loc8 = _loc6.autoStop == true || _loc6.autoStop !== false && _loc4.AUTOSTOP == true;
        var _loc3 = o.__zigoID__;
        var _loc9 = _loc4 != undefined ? (_loc4.OUTPUT_LEVEL) : (com.mosesSupposes.fuse.FuseItem._ZigoEngine.OUTPUT_LEVEL);
        if (_loc8 == true || _loc8 !== false && _loc4.AUTOSTOP == true)
        {
            this.onTweenEnd(o, true);
            return;
        } // end if
        if (typeof(o.target) != "string")
        {
            this.onTweenEnd(o);
            return;
        } // end if
        for (var _loc5 in _aTweens)
        {
            if (_aTweens[_loc5].targZID == _loc3)
            {
                _aTweens.splice(Number(_loc5), 1);
            } // end if
        } // end of for...in
        if (_aTweens.length == 0 && _nPlaying == 1)
        {
            this.complete(_loc9);
        } // end if
    } // End of the function
    function complete(outputLevel)
    {
        if (outputLevel == 3)
        {
            com.mosesSupposes.fuse.FuseKitCommon.output(this._sID() + " complete.");
        } // end if
        var breakChainInt;
        breakChainInt = setInterval(function (fi, trigger)
        {
            clearInterval(breakChainInt);
            var _loc1 = fi._nPlaying;
            if (trigger != true)
            {
                if (_loc1 < 1)
                {
                    return;
                } // end if
            } // end if
            fi.stop();
            if (_loc1 > 0)
            {
                fi.dispatchRequest("advance", [trigger, false, false]);
            } // end if
        }, 1, this, _bTrigger);
    } // End of the function
    function parseClock(str)
    {
        if (str.indexOf(":") != 2)
        {
            com.mosesSupposes.fuse.FuseKitCommon.error("121");
            return (com.mosesSupposes.fuse.FuseItem._ZigoEngine.DURATION || 0);
        } // end if
        var _loc4 = 0;
        var _loc3 = str.split(":");
        _loc3.reverse();
        var _loc2;
        _loc2 = Math.abs(Number(_loc3[0]));
        if (String(_loc3[0]).length == 2 && _global.isNaN(Math.abs(Number(_loc3[0]))) == false)
        {
            _loc4 = _loc4 + _loc2 / 100;
        } // end if
        _loc2 = Math.abs(Number(_loc3[1]));
        if (String(_loc3[1]).length == 2 && _global.isNaN(Math.abs(Number(_loc3[1]))) == false && _loc2 < 60)
        {
            _loc4 = _loc4 + _loc2;
        } // end if
        _loc2 = Math.abs(Number(_loc3[2]));
        if (String(_loc3[2]).length == 2 && _global.isNaN(Math.abs(Number(_loc3[2]))) == false && _loc2 < 60)
        {
            _loc4 = _loc4 + _loc2 * 60;
        } // end if
        _loc2 = Math.abs(Number(_loc3[3]));
        if (String(_loc3[3]).length == 2 && _global.isNaN(Math.abs(Number(_loc3[3]))) == false && _loc2 < 24)
        {
            _loc4 = _loc4 + _loc2 * 3600;
        } // end if
        return (_loc4);
    } // End of the function
    function fireEvents(o, scope, outputLevel, targets)
    {
        if (o.scope == undefined)
        {
            o.scope = scope;
        } // end if
        if (o.e == undefined)
        {
            var _loc3 = com.mosesSupposes.fuse.FuseKitCommon.parseCallback(o, targets, outputLevel, false);
            if (_loc3.start.f != null)
            {
                _loc3.start.f.apply(_loc3.start.s, _loc3.start.a);
            } // end if
            if (_loc3.upd.f != null)
            {
                _loc3.upd.f.apply(_loc3.upd.s, _loc3.upd.a);
            } // end if
            if (_loc3.end.f != null)
            {
                _loc3.end.f.apply(_loc3.end.s, _loc3.end.a);
            } // end if
        }
        else
        {
            var _loc6 = o.e instanceof Function ? (String(o.e.apply(scope))) : (String(o.e));
            if (_loc6 != "undefined" && _loc6.length > 0)
            {
                if (String(com.mosesSupposes.fuse.FuseKitCommon._fuseEvents()).indexOf("|" + _loc6 + "|") > -1)
                {
                    if (outputLevel > 0)
                    {
                        com.mosesSupposes.fuse.FuseKitCommon.error("122", _loc6);
                    } // end if
                }
                else
                {
                    var _loc7 = _global.com.mosesSupposes.fuse.Fuse.getInstance(_nFuseID);
                    var _loc5 = o.ep instanceof Function ? (o.ep.apply(scope)) : (o.ep);
                    if (_loc5 == null || typeof(_loc5) != "object")
                    {
                        _loc5 = {};
                    } // end if
                    _loc5.target = _loc7;
                    _loc5.type = _loc6;
                    _loc7.dispatchEvent.call(_loc7, _loc5);
                } // end else if
            }
            else if (outputLevel > 0)
            {
                com.mosesSupposes.fuse.FuseKitCommon.error("123", this._sID());
            } // end else if
        } // end else if
    } // End of the function
    static var registryKey = "fuseItem";
    static var ADD_UNDERSCORES = true;
    var _nPlaying = -1;
    var _bStartSet = false;
    var _bTrigger = false;
} // End of Class
