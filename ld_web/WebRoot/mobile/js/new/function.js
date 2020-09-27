$(function() {

	//输入框表达式验证 开始
	$('.form-group .ipt-clear').click(function() {
		$(this).hide().prev().val('')
	})
	$(".form-group .form-control").focus(function() {
		var reg = $(this).data("pattern")
		var val = $(this).val();
		if(val != '') {
			$(this).next('.ipt-clear').removeClass('done').show()
		} else {
			$(this).next('.ipt-clear').hide()
		}
	}).keyup(function() {
		var reg = $(this).data("pattern")
		var val = $(this).val();
		if(val != '') {
			$(this).next('.ipt-clear').show()
		} else {
			$(this).next('.ipt-clear').hide()
		}
	}).blur(function() {
		var reg = $(this).data("pattern")
		var val = $(this).val();
		var parent = $(this).parent()
		console.log(reg, new RegExp(reg).test(val))
		if(reg) {
			if(new RegExp(reg).test(val)) {
				parent.next('.control-msg').hide()
				$(this).next('.ipt-clear').addClass("done").removeClass('undone')
			} else {
				parent.next('.control-msg').show()
			}
		} else {
//			$(this).next('.ipt-clear').hide()
		}
	})
	//输入框表达式验证 结束
})

function MobileComboBox(conf) {
	var _this = this;
	var _conf = {
		appendId: '',
		cls: 'form-control',
		valueName: '',
		displayName: '',
		datas: [],
		onChange: function() {}
	};
	$.extend(_conf, conf);
	var _records;
	var dom = $("#" + _conf.appendId);
	if(dom.length == 0) {
		throw '{0} element not exit'.format("#" + _conf.appendId)
	}
	dom.addClass(_conf.cls);
	var _cboOptionHtml = '<option value="{0}">{1}</option>';
	var monthed = {
		onChange: function() {},
		getRecord: function() {
			return _records[dom.val()];
		},
		loadData: function(data) {
			_records = {};
			var str = []
			$.each(data, function(i, item) {
				str.push(_cboOptionHtml.format(item[_conf.valueName], item[_conf.displayName]))
				_records[item[_conf.valueName]] = item;
			});
			console.log(_records)
			dom.html(str.join(''));
		},
		setValue: function(x) {
			return dom.val(x);
		},
		getValue: function() {
			return dom.val();
		},
		getEl: function() {
			return dom.get(0);
		},
		getJq: function() {
			return dom;
		},
		_init: function() {
			monthed.loadData(_conf.datas);
			dom.change(function(e) {
				_conf.onChange.apply(this, e);
			})
		}
	}
	monthed._init();
	return monthed;
}