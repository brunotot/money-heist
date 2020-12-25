package ag04.hackathon2020.moneyheist.entity;

import java.util.List;

public class Skill {

	private Long id;
	
	private String name;

	public Skill() {
		super();
	}

	public Skill(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Skill find(String name, List<Skill> skills) {
		return skills.stream()
			.filter(s -> s != null && s.getName().toUpperCase().equals(name.toUpperCase()))
			.findAny()
			.orElse(null);
	}
	
}
