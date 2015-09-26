function getClubsFromPolitician(p) {
	var distinctClubs = [];
	var distinctClubNames = [];
	for (var i = 0; i < p.mandates.length; i++){
		if (p.mandates[i].club != null){
			if (distinctClubNames.indexOf(p.mandates[i].club.shortName) == -1){
				distinctClubs.push(p.mandates[i].club);
				distinctClubNames.push(p.mandates[i].club.shortName);
			}
		}
	}
	
	return distinctClubs;
}