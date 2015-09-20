package at.jku.tk.hiesmair.gv.parliament.entities.club;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import at.jku.tk.hiesmair.gv.parliament.db.DBConstants;
import at.jku.tk.hiesmair.gv.parliament.db.NativeQueries;

@Entity
@Table(name = DBConstants.TAB_NAME_PARLIAMENT_CLUB)
@NamedNativeQueries({ @NamedNativeQuery(name = "ParliamentClub.countSessionAbsencesPerClub", resultSetMapping = "absenceResultMapper", query = NativeQueries.COUNT_SESSION_ABSENCES_PER_CLUB_QUERY) })
public class ParliamentClub implements Serializable {

	private static final long serialVersionUID = 5594894950661535139L;

	@Id
	private String shortName;

	private String longName;

	public ParliamentClub() {
		super();
	}

	public ParliamentClub(String shortName, String longName) {
		super();
		this.shortName = shortName;
		this.longName = longName;
	}

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

	@Override
	public String toString() {
		return "ParliamentClub [shortName=" + shortName + ", longName=" + longName + "]";
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
		ParliamentClub other = (ParliamentClub) obj;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		}
		else if (!shortName.equals(other.shortName))
			return false;
		return true;
	}

}
