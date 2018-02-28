function commonPartnerGetData(partner, city, joined, area) {
	var returnObj = new Object();
	returnObj.FunctionID = "UP_CG_MOB_PARTNER_POP_S";
	returnObj.P_CD_COMPANY = userInfo().CD_COMPANY;
	returnObj.P_LN_PARTNER = partner;
	if(city != null && city != "") {
		returnObj.P_NM_CITYGU = city;
	} 
	
	if(joined != null && joined != "") {
		returnObj.P_NM_GROUP = joined;
	}
	
	if(area != null && area != "") {
		returnObj.P_ID_BIZ_PERSON = area;
	}
	return returnObj;
}

function commonAjaxForPatnerSerach(bodyObj, callBackFunction) {
	$.get( "/api/getRest?restUrl=" + getUrl("RequestNtx") + "&header=" + encodedJSON(headerInfo()) + "&body=" + encodedJSON(bodyObj), function( data ) {
		if(JSON.parse(data).resultCode == "2000") {
			logOutAlert(JSON.parse(data).resultMessage);
		} else if(JSON.parse(data).resultCode != "1000" && JSON.parse(data).resultCode != "2000") {
			bootalert(JSON.parse(data).resultMessage);
			return false
		}
	}).done(function(data){
		callBackFunction(data);
	}).fail(function(err) {
		console.log(err)
		bootalert(JSON.parse(data).resultMessage);
		return false;
	});
}