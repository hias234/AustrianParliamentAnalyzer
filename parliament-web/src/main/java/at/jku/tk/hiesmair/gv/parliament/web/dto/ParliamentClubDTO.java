package at.jku.tk.hiesmair.gv.parliament.web.dto;

import org.modelmapper.ModelMapper;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;

public class ParliamentClubDTO {

	private String shortName;
	private String longName;
	private String color;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParliamentClubDTO other = (ParliamentClubDTO) obj;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		}
		else if (!shortName.equals(other.shortName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return shortName;
	}

	public static ParliamentClubDTO fromParliamentClub(ParliamentClub club, ModelMapper mapper) {
		return mapper.map(club, ParliamentClubDTO.class);
	}
}
