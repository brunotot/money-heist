package ag04.hackathon2020.moneyheist.entity;

public class Role {

	private Long id;
	
	private String value;

	public Role(Long id, String string) {
		super();
		this.id = id;
		this.value = string;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
