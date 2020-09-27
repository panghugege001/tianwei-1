<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
	</head>
	<body>
		<div style="width: 1000px;">
			<fieldset>
				<legend>新增维护记录</legend>
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<table border="0" cellpadding="0" cellspacing="0">
								<tr align="left">
									<td>维护内容：</td>
									<td><input type="text" name="content" id="content" size="120px" /></td>
									<td>&nbsp;</td>
									<td><input type="button" value="新 增" onclick="create()" /></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
		<div id="createtable" style="margin-top: 20px;"></div>
	</body>

	<script>
	
	var afterUrl = window.location.search.substring(1);
	var loginname = afterUrl.substring(afterUrl.indexOf('=') + 1);

	$(document).ready(function() {

		createTable();
	});

	function create() {
		
		var content = $("#content").val();
		
		if (content == "" || content == null) {
			
			alert("维护内容不能为空！");
			return;
		}
		
		$.ajax({
			url: '/office/addUserMaintainLog.do',
			data: { "loginname": loginname, 'content': content },
			cache: false,
			async: false,
			type: "POST",
			success: function(result) {
				
				if (result["code"] == "10000") {
					
					alert(result["message"]);
					createTable();
				} else {
					
					alert(result["message"]);
				}
			}
		});
	}

	function createTable() {
		
		$("#createtable").empty();
		
		var table = $("<table id=\"table\" border=\"1\" width=\"1000px\">");
		
		table.appendTo($("#createtable"));
		
		var tr = $("<tr></tr>");
		tr.appendTo(table);
		
		var td1 = $("<td width='18%'>加入时间</td>");
		td1.appendTo(tr);
		var td2 = $("<td width='15%'>最后操作人</td>");
		td2.appendTo(tr);
		var td3 = $("<td>维护内容</td>");
		td3.appendTo(tr);
		var td4 = $("<td width='5%'>操作</td>");
		td4.appendTo(tr);

		$.ajax({
			url: '/office/findUserMaintainLogList.do',
			data: { "loginname": loginname },
			cache: false,
			async: false,
			type: "POST",
			success: function(result) {

				if (result && result.length > 0) {
							
					for (var i = 0, len = result.length; i < len; i++) {
								
						var tr = $("<tr></tr>");
						tr.appendTo(table);
						
						var obj = result[i];
								
						var td5 = $("<td>" + obj.createTime + "</td>");
						td5.appendTo(tr);
						var td6 = $("<td>" + obj.updateUser + "</td>");
						td6.appendTo(tr);
						var td7 = $("<td>" + obj.content + "</td>");
						td7.appendTo(tr);
						var td8 = $("<td><input type='button' value='修改' id='btn" + i + "' onclick='edit(" + i + ",this," + obj.id + ")'></td>");
						td8.appendTo(tr);
					}
				}
				
				$("#createtable").append("</table>");
			}
		});
	}

	function edit(x, obj, id) {
		
		x = x + 1;
		
		var table = document.getElementById("table");
		var text = table.rows[x].cells[2].innerHTML;
		table.rows[x].cells[2].innerHTML = '<textarea id="textarea' + x + '" style="width: 100%;"></textarea><input type="hidden" id="hidden'+x+'" value="'+id+'">';
		var textarea = document.getElementById("textarea" + x);
		textarea.value = text;
		textarea.focus();
		textarea.select();
		obj.value = "确定";
		obj.onclick = function onclick(event) {
			update_success(this, x);
		};
	}

	function update_success(obj, x) {
		
		var textarea = document.getElementById("textarea" + x);
		var content = textarea.value;
		var hidden = document.getElementById("hidden" + x);
		var id = hidden.value;
		
		$.ajax({
			url: '/office/updateUserMaintainLog.do',
			data: { "content": content, "id": id },
			cache: false,
			async: false,
			type: "POST",
			success: function(result) {
				
				if (result["code"] == "10000") {
					
					alert(result["message"]);
					createTable();
				} else {
					
					alert(result["message"]);
				}
			}
		});
	}
	</script>
</html>