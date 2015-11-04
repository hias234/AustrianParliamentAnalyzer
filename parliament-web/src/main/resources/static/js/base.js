function getClubsFromPolitician(p) {
	var distinctClubs = [];
	var distinctClubNames = [];
	for (var i = 0; i < p.mandates.length; i++){
		if (p.mandates[i].type == 'NationalCouncilMember'){
			if (distinctClubNames.indexOf(p.mandates[i].club.shortName) == -1){
				distinctClubs.push(p.mandates[i].club);
				distinctClubNames.push(p.mandates[i].club.shortName);
			}
		}
	}
	
	return distinctClubs;
}

// formatted yyyy-MM-dd
function calcAgeFromDateString(birthdayStr) {
	birthday = toDate(birthdayStr);
	
	return calcAge(birthday);
}

function toDate(dateStr) {
	var parts = dateStr.split("-");
	return new Date(parts[0], parts[1] - 1, parts[2]);
}

function calcAge(birthday) { // birthday is a date
    var ageDifMs = Date.now() - birthday.getTime();
    var ageDate = new Date(ageDifMs); // miliseconds from epoch
    return Math.abs(ageDate.getUTCFullYear() - 1970);
}

function getClubsFromPoliticianByPeriod(p, period) {
	var distinctClubs = [];
	var distinctClubNames = [];
	for (var i = 0; i < p.mandates.length; i++){
		if (p.mandates[i].type == 'NationalCouncilMember'){
			var rightPeriod = isRightPeriod(p.mandates[i].periods, period);
			
			if (rightPeriod && distinctClubNames.indexOf(p.mandates[i].club.shortName) == -1){
				distinctClubs.push(p.mandates[i].club);
				distinctClubNames.push(p.mandates[i].club.shortName);
			}
		}
	}
	
	return distinctClubs;
}

function isRightPeriod(mandatePeriods, period) {
	for (var i = 0; i < mandatePeriods.length; i++){
		if (mandatePeriods[i].period == period){
			return true;
		}
	}
	return false;
}