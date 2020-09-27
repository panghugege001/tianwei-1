$(function() {
	var signFun = function() {

		$(".clearfix").find("li").eq(0).addClass("active")

		var dateArray = [];

		var $dateBox = $("#js-qiandao-list"),
			$currentDate = $(".current-date"),
			$qiandaoBnt = $("#j-qdA"),
			_html = '',
			_handle = true,
			myDate = new Date();
		$currentDate.text(parseInt(myDate.getMonth() + 1) + '月份签到');

		var monthFirst = new Date(myDate.getFullYear(), parseInt(myDate.getMonth()), 1).getDay();

		var d = new Date(myDate.getFullYear(), parseInt(myDate.getMonth() + 1), 0);
		var totalDay = d.getDate();

		for(var i = 0; i < 32; i++) {
			_html += ' <li><div class="qiandao-icon"></div></li>'
		}
		$dateBox.html(_html)

		var $dateLi = $dateBox.find("li");
		$.ajax({
			type: "get",
			url: "/asp/findSignrecord.aspx",
			data: "",
			cache: false,
			async: true,
			success: function(data) {
				var obj = eval(data);
				for(var i = 0; i < obj.length; i++) {
					var day = new Date(obj[i].timeStr).getDate() - 1;
					dateArray.push(day);

				}
				for(var i = 0; i < totalDay; i++) {
					$dateLi.eq(i + monthFirst).addClass("date" + parseInt(i + 1));
					for(var j = 0; j < dateArray.length; j++) {
						if(i == dateArray[j]) {
							$dateLi.eq(i + monthFirst).addClass("qiandao");
						}
					}
				}
			},
			error: function() {

			}
		});

		$(".date" + myDate.getDate()).addClass('able-qiandao');

		$dateBox.on("click", "li", function() {
			if($(this).hasClass('able-qiandao') && _handle) {
				$(this).addClass('qiandao');

			}
		})

		function qiandaoFun() {
			$qiandaoBnt.html('已签到');
		}

		function qianDao() {
			$(".date" + myDate.getDate()).addClass('qiandao');
		}
	}();

	function openLayer(a, Fun) {
		$('.' + a).fadeIn(Fun)
	}

	var closeLayer = function() {
		$("body").on("click", ".close-qiandao-layer", function() {
			$(this).parents(".qiandao-layer").fadeOut()
		})
	}();

	$("#js-qiandao-history").on("click", function() {
		openLayer("qiandao-history-layer", myFun);

		function myFun() {
			console.log(1)
		}
	})

	$.get('asp/checkSignRecord.aspx', function(data) {
		if(data === true) {
			$("#j-qdA").html("已签到")
		} else {
			$("#j-qdA").html("未签到")
		}
	})

	$(".btn_qiandao").click(function() {
		$.ajax({
			url: "/asp/doSignRecord.aspx",
			type: "post", // 请求方式
			dataType: "text", // 响应的数据类型
			data: "",
			async: true, // 异步
			success: function(msg) {

				alert(msg);
				if(msg.indexOf("不满足") > 0) {
					return;
				} else {
					$("#j-qdA").html("已签到")
					checkInSignAmount();

				}
			},
		});
	})
	
	//查询签到总余额
		function checkInSignAmount(){
			openProgressBar();
			$.ajax({
				url : "/asp/querySignAmount.aspx",
				type : "post", // 请求方式
				dataType : "text", // 响应的数据类型
				data :"",
				async : true, // 异步
				success : function(msg) {
					closeProgressBar();
					$("#todayGet").html("");
					$("#todayGet").html(Math.floor(msg)+"元");
//					$("#j-check_in2").html("");
//					$("#j-check_in2").html(msg+"元");
				},
			});
		}
		checkInSignAmount();
})