package at.jku.tk.hiesmair.gv.parliament.web.dto.graph;

public class D3Node {

	private String id;
	private String label;
	private String color;
	private String communityColor;

	public D3Node(String id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public D3Node(String id, String label, String color) {
		super();
		this.id = id;
		this.label = label;
		this.color = color;
	}

	public D3Node(String id, String label, String color, String communityColor) {
		super();
		this.id = id;
		this.label = label;
		this.color = color;
		this.communityColor = communityColor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCommunityColor() {
		return communityColor;
	}

	public void setCommunityColor(String communityColor) {
		this.communityColor = communityColor;
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
		D3Node other = (D3Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
