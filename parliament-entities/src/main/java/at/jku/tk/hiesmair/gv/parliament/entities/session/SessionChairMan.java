package at.jku.tk.hiesmair.gv.parliament.entities.session;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
public class SessionChairMan {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer position;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Politician politician;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Session session;

	public SessionChairMan() {
		super();
	}

	public SessionChairMan(Integer position, Politician politician, Session session) {
		super();
		this.position = position;
		this.politician = politician;
		this.session = session;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
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

}
