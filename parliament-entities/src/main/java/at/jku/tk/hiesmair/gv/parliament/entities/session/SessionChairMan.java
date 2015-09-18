package at.jku.tk.hiesmair.gv.parliament.entities.session;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import at.jku.tk.hiesmair.gv.parliament.db.DBConstants;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@Table(name = DBConstants.TAB_NAME_SESSION_CHAIR_MAN, uniqueConstraints = { @UniqueConstraint(columnNames = { "session_id", "position" }) })
public class SessionChairMan implements Serializable {

	private static final long serialVersionUID = -2773729654909781365L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JoinColumn(name = "session_id")
	@ManyToOne(optional = false)
	private Session session;

	private Integer position;

	@ManyToOne(optional = false)
	private Politician politician;

	public SessionChairMan() {
	}

	public SessionChairMan(Integer position, Politician politician, Session session) {
		this.position = position;
		this.session = session;
		this.politician = politician;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "SessionChairMan [position=" + getPosition() + ", politician=" + politician + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((session == null) ? 0 : session.hashCode());
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
		SessionChairMan other = (SessionChairMan) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		}
		else if (!position.equals(other.position))
			return false;
		if (session == null) {
			if (other.session != null)
				return false;
		}
		else if (!session.equals(other.session))
			return false;
		return true;
	}

}
