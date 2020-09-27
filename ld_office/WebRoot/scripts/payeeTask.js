$(document).ready(function() {
	$('.form').submit(function(){		
		var $element = this.elements;
		var count = 0;
		var total = 0;
			for(var i = 0; i < $element.length; i = 1 + i) {
				if($($element[i]).hasClass('checkBox')) {
					//check if checkbox is uncheck,null or check if select box is blank
					if($element[i].checked != true || $element[i].value == null || $($element[i]).closest('tr').find('#list').val() == "" ) {
						if($($element[i]).closest('tr').find('#list').val() != "" && $element[i].checked == false){
						count = count + 1;
						}else if($($element[i]).closest('tr').find('#list').val() == "" && $element[i].checked == true){
							count = count + 1;
						}else if($($element[i]).closest('tr').find('#list').val() == "" && $element[i].checked == false){
							total += 1;
						}
						//this is for struts injection, removing the name to prevent it from passing parameter
						$($element[i]).removeAttr('name');
						$($element[i]).attr('name','none');
						var $selectBox = $($element[i]).parents('tr').find('.mandatory');
						$($selectBox[0]).removeAttr('name');
						$($selectBox[0]).attr('name','blank');
					}
					else{
						$($element[i]).removeAttr('name');
						$($element[i]).attr('name','pnos');
						var $selectBox = $($element[i]).parents('tr').find('.mandatory');
						$($selectBox[0]).removeAttr('name');
						$($selectBox[0]).attr('name','payeeAccount');
					}
				}
			}
			if(count > 0 || total == 20){
					$('#spanError').html('Please select the corresponding payee and select the checkbox.').addClass('Error-Message');
					return false;
			}
		    $.blockUI({ message: '<h2>Please Wait ...</h2>' });
		    return true;	
	});
});

$(document).ready(function() {
	$('#discover-all').click(function() {
	var $checkboxes = $(this).parents('tr:second')
	.find(':checkbox');
	if (this.checked) {
	$checkboxes.attr('checked', true);
	} else {
	$checkboxes.attr('checked', '');
	}
	});
});


$(document).ready(function() {
$('#listAll').change(function() {
 var $selectbox = $(this).parents('tr:first').find(':select.mandatory');
 for(var i = 1; i <$selectbox.length; i = 1 + i) {
	 		if($selectbox[i].value=="" || $($selectbox[i]).hasClass('token')) {
				$selectbox[i].value = $selectbox[0].value;
				$($selectbox[i]).addClass('token');
			}
	}
 });
}); 

$(document).ready(function() {
	$('.form :select').blur(function() {
	 if($(this).hasClass('mandatory')){
		 $(this).removeClass('token');
		}
	 });
	}); 