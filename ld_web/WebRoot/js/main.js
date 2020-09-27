/*!
 // Sticky Plugin v1.0.3 for jQuery  滚动固定
 // =============
 // Website: http://stickyjs.com/
 */
!function(a){"function"==typeof define&&define.amd?define(["jquery"],a):"object"==typeof module&&module.exports?module.exports=a(require("jquery")):a(jQuery)}(function(a){var b=Array.prototype.slice,c=Array.prototype.splice,d={topSpacing:0,bottomSpacing:0,className:"is-sticky",wrapperClassName:"sticky-wrapper",center:!1,getWidthFrom:"",widthFromWrapper:!0,responsiveWidth:!1},e=a(window),f=a(document),g=[],h=e.height(),i=function(){var j,k,l,m,n,o,p,q,r,b=e.scrollTop(),c=f.height(),d=c-h,i=b>d?d-b:0;for(j=0,k=g.length;k>j;j++)l=g[j],m=l.stickyWrapper.offset().top,n=m-l.topSpacing-i,l.stickyWrapper.css("height",l.stickyElement.outerHeight()),n>=b?null!==l.currentTop&&(l.stickyElement.css({width:"",position:"",top:""}),l.stickyElement.parent().removeClass(l.className),l.stickyElement.trigger("sticky-end",[l]),l.currentTop=null):(o=c-l.stickyElement.outerHeight()-l.topSpacing-l.bottomSpacing-b-i,0>o?o+=l.topSpacing:o=l.topSpacing,l.currentTop!==o&&(l.getWidthFrom?p=a(l.getWidthFrom).width()||null:l.widthFromWrapper&&(p=l.stickyWrapper.width()),null==p&&(p=l.stickyElement.width()),l.stickyElement.css("width",p).css("position","fixed").css("top",o),l.stickyElement.parent().addClass(l.className),null===l.currentTop?l.stickyElement.trigger("sticky-start",[l]):l.stickyElement.trigger("sticky-update",[l]),l.currentTop===l.topSpacing&&l.currentTop>o||null===l.currentTop&&o<l.topSpacing?l.stickyElement.trigger("sticky-bottom-reached",[l]):null!==l.currentTop&&o===l.topSpacing&&l.currentTop<o&&l.stickyElement.trigger("sticky-bottom-unreached",[l]),l.currentTop=o),q=l.stickyWrapper.parent(),r=l.stickyElement.offset().top+l.stickyElement.outerHeight()>=q.offset().top+q.outerHeight()&&l.stickyElement.offset().top<=l.topSpacing,r?l.stickyElement.css("position","absolute").css("top","").css("bottom",0):l.stickyElement.css("position","fixed").css("top",o).css("bottom",""))},j=function(){var b,c,d,f;for(h=e.height(),b=0,c=g.length;c>b;b++)d=g[b],f=null,d.getWidthFrom?d.responsiveWidth&&(f=a(d.getWidthFrom).width()):d.widthFromWrapper&&(f=d.stickyWrapper.width()),null!=f&&d.stickyElement.css("width",f)},k={init:function(b){var c=a.extend({},d,b);return this.each(function(){var i,b=a(this),e=b.attr("id"),f=e?e+"-"+d.wrapperClassName:d.wrapperClassName,h=a("<div></div>").attr("id",f).addClass(c.wrapperClassName);b.wrapAll(h),i=b.parent(),c.center&&i.css({width:b.outerWidth(),marginLeft:"auto",marginRight:"auto"}),"right"===b.css("float")&&b.css({"float":"none"}).parent().css({"float":"right"}),c.stickyElement=b,c.stickyWrapper=i,c.currentTop=null,g.push(c),k.setWrapperHeight(this),k.setupChangeListeners(this)})},setWrapperHeight:function(b){var c=a(b),d=c.parent();d&&d.css("height",c.outerHeight())},setupChangeListeners:function(a){if(window.MutationObserver){var b=new window.MutationObserver(function(b){(b[0].addedNodes.length||b[0].removedNodes.length)&&k.setWrapperHeight(a)});b.observe(a,{subtree:!0,childList:!0})}else a.addEventListener("DOMNodeInserted",function(){k.setWrapperHeight(a)},!1),a.addEventListener("DOMNodeRemoved",function(){k.setWrapperHeight(a)},!1)},update:i,unstick:function(){return this.each(function(){for(var b=this,d=a(b),e=-1,f=g.length;f-->0;)g[f].stickyElement.get(0)===b&&(c.call(g,f,1),e=f);-1!==e&&(d.unwrap(),d.css({width:"",position:"",top:"","float":""}))})}};window.addEventListener?(window.addEventListener("scroll",i,!1),window.addEventListener("resize",j,!1)):window.attachEvent&&(window.attachEvent("onscroll",i),window.attachEvent("onresize",j)),a.fn.sticky=function(c){return k[c]?k[c].apply(this,b.call(arguments,1)):"object"!=typeof c&&c?(a.error("Method "+c+" does not exist on jQuery.sticky"),void 0):k.init.apply(this,arguments)},a.fn.unstick=function(c){return k[c]?k[c].apply(this,b.call(arguments,1)):"object"!=typeof c&&c?(a.error("Method "+c+" does not exist on jQuery.sticky"),void 0):k.unstick.apply(this,arguments)},a(function(){setTimeout(i,0)})});

/* ========================================================================
 * Bootstrap: transition.js v3.3.5
 * http://getbootstrap.com/javascript/#transitions
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
  'use strict';

  // CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
  // ============================================================

  function transitionEnd() {
    var el = document.createElement('bootstrap')

    var transEndEventNames = {
      WebkitTransition : 'webkitTransitionEnd',
      MozTransition    : 'transitionend',
      OTransition      : 'oTransitionEnd otransitionend',
      transition       : 'transitionend'
    }

    for (var name in transEndEventNames) {
      if (el.style[name] !== undefined) {
        return { end: transEndEventNames[name] }
      }
    }

    return false // explicit for ie8 (  ._.)
  }

  // http://blog.alexmaccaw.com/css-transitions
  $.fn.emulateTransitionEnd = function (duration) {
    var called = false
    var $el = this
    $(this).one('bsTransitionEnd', function () { called = true })
    var callback = function () { if (!called) $($el).trigger($.support.transition.end) }
    setTimeout(callback, duration)
    return this
  }

  $(function () {
    $.support.transition = transitionEnd()

    if (!$.support.transition) return

    $.event.special.bsTransitionEnd = {
      bindType: $.support.transition.end,
      delegateType: $.support.transition.end,
      handle: function (e) {
        if ($(e.target).is(this)) return e.handleObj.handler.apply(this, arguments)
      }
    }
  })

}(jQuery);


/* ========================================================================
 * Bootstrap: tab.js v3.3.5
 * http://getbootstrap.com/javascript/#tabs
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
  'use strict';

  // TAB CLASS DEFINITION
  // ====================

  var Tab = function (element) {
    // jscs:disable requireDollarBeforejQueryAssignment
    this.element = $(element)
    // jscs:enable requireDollarBeforejQueryAssignment
  }

  Tab.VERSION = '3.3.5'

  Tab.TRANSITION_DURATION = 150

  Tab.prototype.show = function () {
    var $this    = this.element
    var $ul      = $this.closest('ul:not(.dropdown-menu)')
    var selector = $this.data('target')

    if (!selector) {
      selector = $this.attr('href')
      selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
    }

    if ($this.parent('li').hasClass('active')) return

    var $previous = $ul.find('.active:last a')
    var hideEvent = $.Event('hide.bs.tab', {
      relatedTarget: $this[0]
    })
    var showEvent = $.Event('show.bs.tab', {
      relatedTarget: $previous[0]
    })

    $previous.trigger(hideEvent)
    $this.trigger(showEvent)

    if (showEvent.isDefaultPrevented() || hideEvent.isDefaultPrevented()) return

    var $target = $(selector)

    this.activate($this.closest('li'), $ul)
    this.activate($target, $target.parent(), function () {
      $previous.trigger({
        type: 'hidden.bs.tab',
        relatedTarget: $this[0]
      })
      $this.trigger({
        type: 'shown.bs.tab',
        relatedTarget: $previous[0]
      })
    })
  }

  Tab.prototype.activate = function (element, container, callback) {
    var $active    = container.find('> .active')
    var transition = callback
        && $.support.transition
        && ($active.length && $active.hasClass('fade') || !!container.find('> .fade').length)

    function next() {
      $active
          .removeClass('active')
          .find('> .dropdown-menu > .active')
          .removeClass('active')
          .end()
          .find('[data-toggle="tab"]')
          .attr('aria-expanded', false)

      element
          .addClass('active')
          .find('[data-toggle="tab"]')
          .attr('aria-expanded', true)

      if (transition) {
        element[0].offsetWidth // reflow for transition
        element.addClass('in')
      } else {
        element.removeClass('fade')
      }

      if (element.parent('.dropdown-menu').length) {
        element
            .closest('li.dropdown')
            .addClass('active')
            .end()
            .find('[data-toggle="tab"]')
            .attr('aria-expanded', true)
      }

      callback && callback()
    }

    $active.length && transition ?
        $active
            .one('bsTransitionEnd', next)
            .emulateTransitionEnd(Tab.TRANSITION_DURATION) :
        next()

    $active.removeClass('in')
  }


  // TAB PLUGIN DEFINITION
  // =====================

  function Plugin(option) {
    return this.each(function () {
      var $this = $(this)
      var data  = $this.data('bs.tab')

      if (!data) $this.data('bs.tab', (data = new Tab(this)))
      if (typeof option == 'string') data[option]()
    })
  }

  var old = $.fn.tab

  $.fn.tab             = Plugin
  $.fn.tab.Constructor = Tab


  // TAB NO CONFLICT
  // ===============

  $.fn.tab.noConflict = function () {
    $.fn.tab = old
    return this
  }


  // TAB DATA-API
  // ============

  var clickHandler = function (e) {
    e.preventDefault()
    Plugin.call($(this), 'show')
  }

  $(document)
      .on('click.bs.tab.data-api', '[data-toggle="tab"]', clickHandler)
      .on('click.bs.tab.data-api', '[data-toggle="pill"]', clickHandler)

}(jQuery);


/* ========================================================================
 * Bootstrap: modal.js v3.3.5
 * http://getbootstrap.com/javascript/#modals
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
  'use strict';

  // MODAL CLASS DEFINITION
  // ======================

  var Modal = function (element, options) {
    this.options             = options
    this.$body               = $(document.body)
    this.$element            = $(element)
    this.$dialog             = this.$element.find('.modal-dialog')
    this.$backdrop           = null
    this.isShown             = null
    this.originalBodyPad     = null
    this.scrollbarWidth      = 0
    this.ignoreBackdropClick = false

    if (this.options.remote) {
      this.$element
          .find('.modal-content')
          .load(this.options.remote, $.proxy(function () {
            this.$element.trigger('loaded.bs.modal')
          }, this))
    }
  }

  Modal.VERSION  = '3.3.5'

  Modal.TRANSITION_DURATION = 300
  Modal.BACKDROP_TRANSITION_DURATION = 150

  Modal.DEFAULTS = {
    backdrop: true,
    keyboard: true,
    show: true
  }

  Modal.prototype.toggle = function (_relatedTarget) {
    return this.isShown ? this.hide() : this.show(_relatedTarget)
  }

  Modal.prototype.show = function (_relatedTarget) {
    var that = this
    var e    = $.Event('show.bs.modal', { relatedTarget: _relatedTarget })

    this.$element.trigger(e)

    if (this.isShown || e.isDefaultPrevented()) return

    this.isShown = true

    this.checkScrollbar()
    this.setScrollbar()
    this.$body.addClass('modal-open')

    this.escape()
    this.resize()

    this.$element.on('click.dismiss.bs.modal', '[data-dismiss="modal"]', $.proxy(this.hide, this))

    this.$dialog.on('mousedown.dismiss.bs.modal', function () {
      that.$element.one('mouseup.dismiss.bs.modal', function (e) {
        if ($(e.target).is(that.$element)) that.ignoreBackdropClick = true
      })
    })

    this.backdrop(function () {
      var transition = $.support.transition && that.$element.hasClass('fade')

      if (!that.$element.parent().length) {
        that.$element.appendTo(that.$body) // don't move modals dom position
      }

      that.$element
          .show()
          .scrollTop(0)

      that.adjustDialog()

      if (transition) {
        that.$element[0].offsetWidth // force reflow
      }

      that.$element.addClass('in')

      that.enforceFocus()

      var e = $.Event('shown.bs.modal', { relatedTarget: _relatedTarget })

      transition ?
          that.$dialog // wait for modal to slide in
              .one('bsTransitionEnd', function () {
                that.$element.trigger('focus').trigger(e)
              })
              .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
          that.$element.trigger('focus').trigger(e)
    })
  }

  Modal.prototype.hide = function (e) {
    if (e) e.preventDefault()

    e = $.Event('hide.bs.modal')

    this.$element.trigger(e)

    if (!this.isShown || e.isDefaultPrevented()) return

    this.isShown = false

    this.escape()
    this.resize()

    $(document).off('focusin.bs.modal')

    this.$element
        .removeClass('in')
        .off('click.dismiss.bs.modal')
        .off('mouseup.dismiss.bs.modal')

    this.$dialog.off('mousedown.dismiss.bs.modal')

    $.support.transition && this.$element.hasClass('fade') ?
        this.$element
            .one('bsTransitionEnd', $.proxy(this.hideModal, this))
            .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
        this.hideModal()
  }

  Modal.prototype.enforceFocus = function () {
    $(document)
        .off('focusin.bs.modal') // guard against infinite focus loop
        .on('focusin.bs.modal', $.proxy(function (e) {
          if (this.$element[0] !== e.target && !this.$element.has(e.target).length) {
            this.$element.trigger('focus')
          }
        }, this))
  }

  Modal.prototype.escape = function () {
    if (this.isShown && this.options.keyboard) {
      this.$element.on('keydown.dismiss.bs.modal', $.proxy(function (e) {
        e.which == 27 && this.hide()
      }, this))
    } else if (!this.isShown) {
      this.$element.off('keydown.dismiss.bs.modal')
    }
  }

  Modal.prototype.resize = function () {
    if (this.isShown) {
      $(window).on('resize.bs.modal', $.proxy(this.handleUpdate, this))
    } else {
      $(window).off('resize.bs.modal')
    }
  }

  Modal.prototype.hideModal = function () {
    var that = this
    this.$element.hide()
    this.backdrop(function () {
      that.$body.removeClass('modal-open')
      that.resetAdjustments()
      that.resetScrollbar()
      that.$element.trigger('hidden.bs.modal')
    })
  }

  Modal.prototype.removeBackdrop = function () {
    this.$backdrop && this.$backdrop.remove()
    this.$backdrop = null
  }

  Modal.prototype.backdrop = function (callback) {
    var that = this
    var animate = this.$element.hasClass('fade') ? 'fade' : ''

    if (this.isShown && this.options.backdrop) {
      var doAnimate = $.support.transition && animate

      this.$backdrop = $(document.createElement('div'))
          .addClass('modal-backdrop ' + animate)
          .appendTo(this.$body)

      this.$element.on('click.dismiss.bs.modal', $.proxy(function (e) {
        if (this.ignoreBackdropClick) {
          this.ignoreBackdropClick = false
          return
        }
        if (e.target !== e.currentTarget) return
        this.options.backdrop == 'static'
            ? this.$element[0].focus()
            : this.hide()
      }, this))

      if (doAnimate) this.$backdrop[0].offsetWidth // force reflow

      this.$backdrop.addClass('in')

      if (!callback) return

      doAnimate ?
          this.$backdrop
              .one('bsTransitionEnd', callback)
              .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
          callback()

    } else if (!this.isShown && this.$backdrop) {
      this.$backdrop.removeClass('in')

      var callbackRemove = function () {
        that.removeBackdrop()
        callback && callback()
      }
      $.support.transition && this.$element.hasClass('fade') ?
          this.$backdrop
              .one('bsTransitionEnd', callbackRemove)
              .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
          callbackRemove()

    } else if (callback) {
      callback()
    }
  }

  // these following methods are used to handle overflowing modals

  Modal.prototype.handleUpdate = function () {
    this.adjustDialog()
  }

  Modal.prototype.adjustDialog = function () {
    var modalIsOverflowing = this.$element[0].scrollHeight > document.documentElement.clientHeight

    this.$element.css({
      paddingLeft:  !this.bodyIsOverflowing && modalIsOverflowing ? this.scrollbarWidth : '',
      paddingRight: this.bodyIsOverflowing && !modalIsOverflowing ? this.scrollbarWidth : ''
    })
  }

  Modal.prototype.resetAdjustments = function () {
    this.$element.css({
      paddingLeft: '',
      paddingRight: ''
    })
  }

  Modal.prototype.checkScrollbar = function () {
    var fullWindowWidth = window.innerWidth
    if (!fullWindowWidth) { // workaround for missing window.innerWidth in IE8
      var documentElementRect = document.documentElement.getBoundingClientRect()
      fullWindowWidth = documentElementRect.right - Math.abs(documentElementRect.left)
    }
    this.bodyIsOverflowing = document.body.clientWidth < fullWindowWidth
    this.scrollbarWidth = this.measureScrollbar()
  }

  Modal.prototype.setScrollbar = function () {
    var bodyPad = parseInt((this.$body.css('padding-right') || 0), 10)
    this.originalBodyPad = document.body.style.paddingRight || ''
    if (this.bodyIsOverflowing) this.$body.css('padding-right', bodyPad + this.scrollbarWidth)
  }

  Modal.prototype.resetScrollbar = function () {
    this.$body.css('padding-right', this.originalBodyPad)
  }

  Modal.prototype.measureScrollbar = function () { // thx walsh
    var scrollDiv = document.createElement('div')
    scrollDiv.className = 'modal-scrollbar-measure'
    this.$body.append(scrollDiv)
    var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth
    this.$body[0].removeChild(scrollDiv)
    return scrollbarWidth
  }


  // MODAL PLUGIN DEFINITION
  // =======================

  function Plugin(option, _relatedTarget) {
    return this.each(function () {
      var $this   = $(this)
      var data    = $this.data('bs.modal')
      var options = $.extend({}, Modal.DEFAULTS, $this.data(), typeof option == 'object' && option)

      if (!data) $this.data('bs.modal', (data = new Modal(this, options)))
      if (typeof option == 'string') data[option](_relatedTarget)
      else if (options.show) data.show(_relatedTarget)
    })
  }

  var old = $.fn.modal

  $.fn.modal             = Plugin
  $.fn.modal.Constructor = Modal


  // MODAL NO CONFLICT
  // =================

  $.fn.modal.noConflict = function () {
    $.fn.modal = old
    return this
  }


  // MODAL DATA-API
  // ==============

  $(document).on('click.bs.modal.data-api', '[data-toggle="modal"]', function (e) {
    var $this   = $(this)
    var href    = $this.attr('href')
    var $target = $($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) // strip for ie7
    var option  = $target.data('bs.modal') ? 'toggle' : $.extend({ remote: !/#/.test(href) && href }, $target.data(), $this.data())

    if ($this.is('a')) e.preventDefault()

    $target.one('show.bs.modal', function (showEvent) {
      if (showEvent.isDefaultPrevented()) return // only register focus restorer if modal will actually get shown
      $target.one('hidden.bs.modal', function () {
        $this.is(':visible') && $this.trigger('focus')
      })
    })
    Plugin.call($target, option, this)
  })

}(jQuery);


/* ========================================================================
 * Bootstrap: carousel.js v3.3.5
 * http://getbootstrap.com/javascript/#carousel
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
  'use strict';

  // CAROUSEL CLASS DEFINITION
  // =========================

  var Carousel = function (element, options) {
    this.$element    = $(element)
    this.$indicators = this.$element.find('.carousel-indicators')
    this.options     = options
    this.paused      = null
    this.sliding     = null
    this.interval    = null
    this.$active     = null
    this.$items      = null

    this.options.keyboard && this.$element.on('keydown.bs.carousel', $.proxy(this.keydown, this))

    this.options.pause == 'hover' && !('ontouchstart' in document.documentElement) && this.$element
        .on('mouseenter.bs.carousel', $.proxy(this.pause, this))
        .on('mouseleave.bs.carousel', $.proxy(this.cycle, this))
  }

  Carousel.VERSION  = '3.3.5'

  Carousel.TRANSITION_DURATION = 600

  Carousel.DEFAULTS = {
    interval: 5000,
    pause: 'hover',
    wrap: true,
    keyboard: true
  }

  Carousel.prototype.keydown = function (e) {
    if (/input|textarea/i.test(e.target.tagName)) return
    switch (e.which) {
      case 37: this.prev(); break
      case 39: this.next(); break
      default: return
    }

    e.preventDefault()
  }

  Carousel.prototype.cycle = function (e) {
    e || (this.paused = false)

    this.interval && clearInterval(this.interval)

    this.options.interval
    && !this.paused
    && (this.interval = setInterval($.proxy(this.next, this), this.options.interval))

    return this
  }

  Carousel.prototype.getItemIndex = function (item) {
    this.$items = item.parent().children('.item')
    return this.$items.index(item || this.$active)
  }

  Carousel.prototype.getItemForDirection = function (direction, active) {
    var activeIndex = this.getItemIndex(active)
    var willWrap = (direction == 'prev' && activeIndex === 0)
        || (direction == 'next' && activeIndex == (this.$items.length - 1))
    if (willWrap && !this.options.wrap) return active
    var delta = direction == 'prev' ? -1 : 1
    var itemIndex = (activeIndex + delta) % this.$items.length
    return this.$items.eq(itemIndex)
  }

  Carousel.prototype.to = function (pos) {
    var that        = this
    var activeIndex = this.getItemIndex(this.$active = this.$element.find('.item.active'))

    if (pos > (this.$items.length - 1) || pos < 0) return

    if (this.sliding)       return this.$element.one('slid.bs.carousel', function () { that.to(pos) }) // yes, "slid"
    if (activeIndex == pos) return this.pause().cycle()

    return this.slide(pos > activeIndex ? 'next' : 'prev', this.$items.eq(pos))
  }

  Carousel.prototype.pause = function (e) {
    e || (this.paused = true)

    if (this.$element.find('.next, .prev').length && $.support.transition) {
      this.$element.trigger($.support.transition.end)
      this.cycle(true)
    }

    this.interval = clearInterval(this.interval)

    return this
  }

  Carousel.prototype.next = function () {
    if (this.sliding) return
    return this.slide('next')
  }

  Carousel.prototype.prev = function () {
    if (this.sliding) return
    return this.slide('prev')
  }

  Carousel.prototype.slide = function (type, next) {
    var $active   = this.$element.find('.item.active')
    var $next     = next || this.getItemForDirection(type, $active)
    var isCycling = this.interval
    var direction = type == 'next' ? 'left' : 'right'
    var that      = this

    if ($next.hasClass('active')) return (this.sliding = false)

    var relatedTarget = $next[0]
    var slideEvent = $.Event('slide.bs.carousel', {
      relatedTarget: relatedTarget,
      direction: direction
    })
    this.$element.trigger(slideEvent)
    if (slideEvent.isDefaultPrevented()) return

    this.sliding = true

    isCycling && this.pause()

    if (this.$indicators.length) {
      this.$indicators.find('.active').removeClass('active')
      var $nextIndicator = $(this.$indicators.children()[this.getItemIndex($next)])
      $nextIndicator && $nextIndicator.addClass('active')
    }

    var slidEvent = $.Event('slid.bs.carousel', { relatedTarget: relatedTarget, direction: direction }) // yes, "slid"
    if ($.support.transition && this.$element.hasClass('slide')) {
      $next.addClass(type)
      $next[0].offsetWidth // force reflow
      $active.addClass(direction)
      $next.addClass(direction)
      $active
          .one('bsTransitionEnd', function () {
            $next.removeClass([type, direction].join(' ')).addClass('active')
            $active.removeClass(['active', direction].join(' '))
            that.sliding = false
            setTimeout(function () {
              that.$element.trigger(slidEvent)
            }, 0)
          })
          .emulateTransitionEnd(Carousel.TRANSITION_DURATION)
    } else {
      $active.removeClass('active')
      $next.addClass('active')
      this.sliding = false
      this.$element.trigger(slidEvent)
    }

    isCycling && this.cycle()

    return this
  }


  // CAROUSEL PLUGIN DEFINITION
  // ==========================

  function Plugin(option) {
    return this.each(function () {
      var $this   = $(this)
      var data    = $this.data('bs.carousel')
      var options = $.extend({}, Carousel.DEFAULTS, $this.data(), typeof option == 'object' && option)
      var action  = typeof option == 'string' ? option : options.slide

      if (!data) $this.data('bs.carousel', (data = new Carousel(this, options)))
      if (typeof option == 'number') data.to(option)
      else if (action) data[action]()
      else if (options.interval) data.pause().cycle()
    })
  }

  var old = $.fn.carousel

  $.fn.carousel             = Plugin
  $.fn.carousel.Constructor = Carousel


  // CAROUSEL NO CONFLICT
  // ====================

  $.fn.carousel.noConflict = function () {
    $.fn.carousel = old
    return this
  }


  // CAROUSEL DATA-API
  // =================

  var clickHandler = function (e) {
    var href
    var $this   = $(this)
    var $target = $($this.attr('data-target') || (href = $this.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, '')) // strip for ie7
    if (!$target.hasClass('carousel')) return
    var options = $.extend({}, $target.data(), $this.data())
    var slideIndex = $this.attr('data-slide-to')
    if (slideIndex) options.interval = false

    Plugin.call($target, options)

    if (slideIndex) {
      $target.data('bs.carousel').to(slideIndex)
    }

    e.preventDefault()
  }

  $(document)
      .on('click.bs.carousel.data-api', '[data-slide]', clickHandler)
      .on('click.bs.carousel.data-api', '[data-slide-to]', clickHandler)

  $(window).on('load', function () {
    $('[data-ride="carousel"]').each(function () {
      var $carousel = $(this)
      Plugin.call($carousel, $carousel.data())
    })
  })

}(jQuery);

/**
 * jquery cookie 插件
 * 设置一个名称为asp，值为www.angelweb.cn的cookie，设置过期时间（expires属性）为7天，同时设置cookie的path属性为”/admin”
 * set:    $.cookie("asp", "www.angelweb.cn", { path: '/admin', expires: 7 });
 *
 * read:   $.cookie("blog");
 * delete: $.cookie("example", null);
 *
 */
+(function($){
  $.cookie = function(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
      options = options || {};
      if (value === null) {
        value = '';
        options.expires = -1;
      }
      var expires = '';
      if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
        var date;
        if (typeof options.expires == 'number') {
          date = new Date();
          date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
        } else {
          date = options.expires;
        }
        expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
      }
      var path = options.path ? '; path=' + options.path : '';
      var domain = options.domain ? '; domain=' + options.domain : '';
      var secure = options.secure ? '; secure' : '';
      document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
      var cookieValue = null;
      if (document.cookie && document.cookie != '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
          var cookie = jQuery.trim(cookies[i]);
          // Does this cookie string begin with the name we want?
          if (cookie.substring(0, name.length + 1) == (name + '=')) {
            cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
            break;
          }
        }
      }
      return cookieValue;
    }
  };
}(jQuery));


/**
 * 全局通用对象
 * @type {{}}
 */
var Global = {};

Global.Init = function () {
  this.InputValidate();
};
/**
 * 设置为主页
 * @param ele
 * @param url
 * @constructor
 */
Global.SetHome=function(url){
    if(document.all) {
      document.body.style.behavior = 'url(#default#homepage)';
      document.body.setHomePage(url);
    }else if (window.netscape) {
      try {
        netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect")
      } catch (err) {
        alert("此操作被浏览器拒绝！")
      }
      Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefBranch).setCharPref("browser.startup.homepage", url)
    }

};
/**
 * 收藏网页
 * @param url
 * @param title
 * @constructor
 */
Global.AddFavorite=function(url,title){
  try {
    window.external.addFavorite(url, title)
  } catch (c) {
    try {
      window.sidebar.addPanel(title, url, "")
    } catch (d) {
      alert("加入收藏失败，请使用Ctrl+D进行添加");
    }
  }
};

Global.Logout=function(){
  $.post("/asp/logout.aspx", {
  }, function () {
      window.location.href="/";
  });
};
Global.Login=function(name,pwd,code,callback){
  $.post("${ctx}/asp/login.aspx",
    {"loginname":name, "password":pwd,"imageCode":code},
    callback);
};


/**
 * 无缝滚动
 * @param target 操作id
 * @param sp 速度
 * @constructor
 */
Global.Marquee=function(target,sp,direction){
  var $container = (typeof target === 'string') ? $(target) : target,
      container=$container[0],
      $marqueeItem = $container.find('[data-marquee-item]'),
      $last = $container.find('[data-marquee-item]:last'),

      speed = sp || 10,
      dir=direction || 'top';

  if($last.length){
    var last=$last[0],
        rolling;

    if(dir=='top'){
      $marqueeItem.clone().appendTo($container);
      var height=last.offsetTop+last.offsetHeight;
      rolling = function(){
        if(container.scrollTop == height){
          container.scrollTop = 0;
        }else{
          container.scrollTop++;
        }
      };
    }else if(dir=='right'){
      $marqueeItem.clone().appendTo($container.find('ul'));
      rolling = function(){
        if(container.scrollLeft == last.offsetLeft){
          container.scrollLeft = 0;
        }else{
          container.scrollLeft++;
        }
      }
    }


    var timer = setInterval(rolling,speed);//设置定时器
    $container.on('mouseenter',function(){
      clearInterval(timer);
    });
    $container.on('mouseleave',function() {
      timer=setInterval(rolling,speed);//鼠标移开时重设定时器
    });
  }
};

/**
 * 回到顶部
 * @param target
 * @constructor
 */
Global.GoTop=function(target,num){
  var $window=$(window),
      $this=(typeof target === 'string') ? $(target) : target,
      top=num||$this.offset().top;
  function checkPosition(){
    $window.scrollTop() > top ? $this.addClass("active") : $this.removeClass("active")
  }
  $this.on('click',function(e){
    e.preventDefault();
    $("html, body").animate({
      scrollTop: 0
    }, 500);

  });
  checkPosition();

  $window.on("scroll", checkPosition);
  $window.on("resize", checkPosition);
};
/**
 * 限制输入
 * @constructor
 */
Global.InputValidate=function(){
  var $doc=$(document);

  /**
   * 验证码的API
   */
  $doc.on('click.global', '[data-toggle="refreshcode"]', function () {
    var $this = $(this);
    Homepage.getCode($this, $this.attr('src'));
  });

  /*限制只能输入整数
   * =============*/
  $doc.on('keyup','input[data-rule-digits]',function(){
    this.value = this.value.replace(/\D|^0/g, "")
  });
  /*限制只能输入数字，可以为小数
   * =============*/
  $doc.on('keyup','input[data-rule-num]',function(){

    if (!/^\d+[.]?\d*$/.test(this.value)){
      this.value = /^\d+[.]?\d*/.exec(this.value);
    }
    return false;
  });
};

Global.GetNotice=function(callback){
  $.post('/asp/checkConfigSystem.aspx',
      {'typeNo':'type002','itemNo':'001'},
      callback);
};

$(function () {

  Global.Init();

  var $indexMarquee=$('#j-marquee-aside'), //首页中奖消息无缝滚动
      $goTop=$('#gotop'),  //返回顶部
      $sideBar=$('#sidebar'),//侧边栏
      $rebackCall=$('#j-reback-call'), //侧边栏电话回拨
      $tabAboutUs=$('#j-tab-aboutus'), //关于我们标签
      $indexModal=$('#modal-index'), //首页弹框
      $winnerMarquee=$('#j-marquee-winner-list');


  //首页中奖消息无缝滚动
  //$indexMarquee.length && Global.Marquee($indexMarquee,20,'top');

  //老虎机中奖消息无缝滚动
  $winnerMarquee.length && Global.Marquee($winnerMarquee,30,'right');
  //返回顶部
  $goTop.length && Global.GoTop($goTop,100);
  //首页顶部滚动消息
  $("#j-top-scroll .carousel-inner div:first").addClass('active');

  //关于我们标签选中
  $tabAboutUs.length && $tabAboutUs.find('a[href="'+window.location.hash+'"]').tab('show');

  //侧边栏电话回拨
  $rebackCall.length && $rebackCall.on('click',function(){
    var phonenum = $("#phonenum").val();
    $.post("/asp/makeCall.aspx", {'phonenum':phonenum}, function (data) {
      alert(data);
    });
  });

  //
  $(".j-sticker").sticky({topSpacing:0}).on('sticky-start', function() { $(this).css({'z-index':'9'}) });

  marqueeInit();

  function marqueeInit(){
    $('marquee[data-marquee]').on({
      'mouseenter':function(){
        this.stop();
      },
      'mouseleave':function(){
        this.start();
      }
    });
  }

});

/**
 * 工具函数
 */
;!function(window){
  var Util=window.Util||{};

  /**
   * 获取url参数值
   * @param name
   * @param url
   * @returns {*}
   */
  Util.getQueryString=function (name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
  };

  root = typeof exports !== "undefined" && exports !== null ? exports : window;

  root.Util = Util;
}(window);


