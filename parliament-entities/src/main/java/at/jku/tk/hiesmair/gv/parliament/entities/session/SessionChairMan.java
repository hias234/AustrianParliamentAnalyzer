package at.jku.tk.hiesmair.gv.parliament.entities.session;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
public class SessionChairMan implements Serializable {

	private static final long serialVersionUID = -2773729654909781365L;

	@Embeddable
	public static class SessionChairManId implements Serializable {

		private static final long serialVersionUID = -1644746776601455788L;

		@ManyToOne(optional = false)
		private Session session;

		private Integer position;

		public SessionChairManId() {
			super();
		}

		public SessionChairManId(Session session, Integer position) {
			super();
			this.session = session;
			this.position = position;
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
			SessionChairManId other = (SessionChairManId) obj;
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

	@EmbeddedId
	private SessionChairManId id;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	private Politician politician;

	public SessionChairMan() {
		id = new SessionChairManId();
	}

	public SessionChairMan(Integer position, Politician politician, Session session) {
		this.id = new SessionChairManId(session, position);
		this.politician = politician;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
	}

	public Session getSession() {
		return id.getSession();
	}

	public void setSession(Session session) {
		this.id.setSession(session);
	}

	public Integer getPosition() {
		return id.getPosition();
	}

	public void setPosition(Integer position) {
		this.id.setPosition(position);
	}
	
	@Override
	public String toString() {
		return "SessionChairMan [position=" + id.getPosition() + ", politician=" + politician + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
