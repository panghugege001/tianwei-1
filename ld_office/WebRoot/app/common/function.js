function isNull(v) {

	if (null == v || "" == v || " " == v || "null" == v || "NULL" == v || undefined == v) {
		
		return true;
	}
	
	return false;
};

function isInteger(str) {
	
	var r = /^\+?[1-9][0-9]*$/;
	
	return r.test(str);
};

Array.prototype.contains = function(item) {
	
	for (i = 0, len = this.length; i < len; i++) {
		
		if (this[i] == item) {
			
			return true;
		}
	}
	
	return false;
};

String.prototype.contains = function(str) {
	
	return new RegExp(str).test(this);
};