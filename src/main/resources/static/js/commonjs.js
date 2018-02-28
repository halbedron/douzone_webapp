/*<![CDATA[*/
function checkOpenModal() {
	if($(".modal").length != 0 && $(".modal").hasClass("in")) //모달이 떠있을때 
	{
		$(".modal").modal("hide");
	} else {
		location.href="app://cmd;back"; 
	}
}

function encodedJSON(obj) {
	return encodeURI(JSON.stringify(obj));
}

function setTitle(text) {
	$("#darby_title").text("");
	$("#darby_title").text(text);
}

function setTwinNum(num) {
	if(num < 10) {
		return "0"+num;
	} else {
		return num;
	}
}

function format(num){
    var len, point, str; 
       
    num = num + ""; 
    point = num.length % 3 ;
    len = num.length; 
   
    str = num.substring(0, point); 
    while (point < len) { 
        if (str != "") str += ","; 
        str += num.substring(point, point + 3); 
        point += 3; 
    } 
     
    return str;
}

function headerInfo() {
	var headerinfo = sessionStorage.getItem("headerInfo");
	if(headerinfo != null && headerinfo != "undefined") {
		return JSON.parse(headerinfo);
	} else {
		return "";
	}
}

function userInfo() {
	var userinfo = sessionStorage.getItem("userInfo");
	if(userinfo != null && userinfo != "undefined") {
		return JSON.parse(userinfo);
	} else {
		return "";
	}
}

function getSession(sessionNM) {
	var session = sessionStorage.getItem(sessionNM);
	if(session != null && session != "undefined") {
		return session;
	} else {
		return "";
	}
}

function getUrl(urlNm) {
	var apiList = JSON.parse(getSession("api"));
	var hostUrl = getSession("hostUrl");
	var url = "";
	for(var i in  apiList) {
		if(apiList[i].CD_API == urlNm) {
			url = hostUrl + "/" + apiList[i].NM_API;
		}
	}
	
	return url;
}

function bootalert(text) {
	return BootstrapDialog.alert( text);;
}

function logOutAlert(text) {
	return BootstrapDialog.show({
		 message: text,
		 buttons: [{
			 label: '닫기',
			 action: function() {
				 sessionStorage.removeItem("api");
				 sessionStorage.removeItem("headerInfo");
				 sessionStorage.removeItem("hostUrl");
				 sessionStorage.removeItem("userInfo");
				 location.href = "/home?web";
			 }
		 }]
	 });
}

Base64 = {

	// private property
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

	// public method for encoding
	encode : function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;

		while (i < input.length) {

		  chr1 = input.charCodeAt(i++);
		  chr2 = input.charCodeAt(i++);
		  chr3 = input.charCodeAt(i++);

		  enc1 = chr1 >> 2;
		  enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
		  enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
		  enc4 = chr3 & 63;

		  if (isNaN(chr2)) {
			  enc3 = enc4 = 64;
		  } else if (isNaN(chr3)) {
			  enc4 = 64;
		  }

		  output = output +
			  this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			  this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

		}

		return output;
	},

	// public method for decoding
	decode : function (input)
	{
	    var output = "";
	    var chr1, chr2, chr3;
	    var enc1, enc2, enc3, enc4;
	    var i = 0;

	    input = input.replace(/[^A-Za-z0-9+/=]/g, "");

	    while (i < input.length)
	    {
	        enc1 = this._keyStr.indexOf(input.charAt(i++));
	        enc2 = this._keyStr.indexOf(input.charAt(i++));
	        enc3 = this._keyStr.indexOf(input.charAt(i++));
	        enc4 = this._keyStr.indexOf(input.charAt(i++));

	        chr1 = (enc1 << 2) | (enc2 >> 4);
	        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	        chr3 = ((enc3 & 3) << 6) | enc4;

	        output = output + String.fromCharCode(chr1);

	        if (enc3 != 64) {
	            output = output + String.fromCharCode(chr2);
	        }
	        if (enc4 != 64) {
	            output = output + String.fromCharCode(chr3);
	        }
	    }

	    return output;
	}
}

function commonSelectInit(cd_field, selector, select_callback, option_0) {
	var bodyObj = new Object();
	bodyObj.FunctionID = "UP_CG_MOB_CODE_S";
	bodyObj.P_CD_FIELD = cd_field;
	var option = null;
	$.get( "/api/getRest?restUrl=" + getUrl("RequestNtx") + "&header=" + encodedJSON(headerInfo()) + "&body=" + encodedJSON(bodyObj), function( data ) {
		if(JSON.parse(data).resultCode == "1000") {
			option = JSON.parse(data).result.List.UP_CG_MOB_CODE_S;
			console.log(option)
			$(selector).empty();
			if(option_0 == "" || option_0 == null) {
				$(selector).append('<option value="'+ 0 +'">없음</option>');
			} else {
				$(selector).append('<option value="'+ 0 +'">' + option_0 + '</option>');
			}
			for(var i in option){
				$(selector).append('<option value="'+ option[i].CODE +'">' + option[i].NM_CODE + '</option>');
			}
			$(selector).selectpicker('refresh');
			
		} else if(JSON.parse(data).resultCode == "2000") {
			logOutAlert(JSON.parse(data).resultMessage);
		}
	}).done(function(e){
		$(selector).val(select_callback);
	    $(selector).selectpicker('render');
	}).fail(function(err) {
		console.log(err)
	});
}

function addComma(val) {
	var value = val.trim();
	var len, point, str; 
    if(value != "" && value != null) {
	    	if(value.indexOf(".") ==  -1) {
	    		point = value.length % 3 ;
	    		len = value.length; 
	    		str = value.substring(0, point); 
	    		
	    		while (point < len) { 
	    			if (str != "") str += ","; 
	    			str += value.substring(point, point + 3); 
	    			point += 3; 
	    		} 
	    		return str;
	    	} else {
	    		var valueList = value.split(".");
	    		point = valueList[0].length % 3 ;
	    		len = valueList[0].length; 
	    		str = valueList[0].substring(0, point); 
	    		
	    		while (point < len) { 
	    			if (str != "") str += ","; 
	    			str += valueList[0].substring(point, point + 3); 
	    			point += 3; 
	    		} 
	    		return str + "." + valueList[1];
	    	}
    }
    
}

function commonAjaxForGetData(bodyObj, callBackFunction) {
	$.get( "/api/getRest?restUrl=" + getUrl("RequestNtx") + "&header=" + encodedJSON(headerInfo()) + "&body=" + encodedJSON(bodyObj), function( data ) {
		if(JSON.parse(data).resultCode == "2000") {
			logOutAlert(JSON.parse(data).resultMessage);
		} else if(JSON.parse(data).resultCode != "1000" && JSON.parse(data).resultCode != "2000") {
			bootalert(JSON.parse(data).resultMessage);
			return false
		}
	}).done(function(data){
		if(JSON.parse(data).resultCode == "1000") {
			callBackFunction(JSON.parse(data).result.List);
		}
	}).fail(function(err) {
		console.log(err);
		bootalert(JSON.parse(data).resultMessage);
		return false;
	});
}

function makeDateFormat(val) {
	var dtList = [val.substring(0, 4), val.substring(4, 6), val.substring(6, 8)];
    return dtList[0] + "-" + dtList[1] + "-" + dtList[2];
}

function Num(val) {
	if(val == "" ) {
		return 0;
	} else {
		return val*1;
	}
}
/*]]>*/
