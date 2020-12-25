package ag04.hackathon2020.moneyheist.entity;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Heist {

	private Long id;
	
	private String name;
	
	private String location;
	
	private Date startTime;
	
	private Date endTime;
	
	private List<HeistSkill> heistSkills;

	public Heist() {
		super();
	}

	public Heist(Long id, String name, String location, Date startTime, Date endTime, List<HeistSkill> heistSkills) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.heistSkills = heistSkills;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<HeistSkill> getHeistSkills() {
		return heistSkills;
	}
	
	public List<Skill> getSkills() {
		return heistSkills.stream()
				.filter(hs -> hs != null)
				.map(hs -> hs.getSkill())
				.collect(Collectors.toList());

	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setHeistSkills(List<HeistSkill> heistSkills) {
		this.heistSkills = heistSkills;
	}
	
}
