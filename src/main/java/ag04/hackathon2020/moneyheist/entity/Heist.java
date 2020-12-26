package ag04.hackathon2020.moneyheist.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.dto.HeistSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;

public class Heist {

	private Long id;
	
	private String name;
	
	private String location;
	
	private ZonedDateTime startTime;
	
	private ZonedDateTime endTime;
	
	private List<HeistSkill> heistSkills;

	private HeistStatus heistStatus;
	
	List<Member> heistMembers;
	
	public Heist() {
		super();
	}

	public Heist(Long id, String name, String location, ZonedDateTime startTime, ZonedDateTime endTime, List<HeistSkill> heistSkills, HeistStatus heistStatus, List<Member> heistMembers) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.heistSkills = heistSkills;
		this.heistStatus = heistStatus;
		this.heistMembers = heistMembers;
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

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public HeistStatus getHeistStatus() {
		return heistStatus;
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
	
	public List<Member> getHeistMembers() {
		return this.heistMembers;
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

	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	
	public void setHeistStatus(HeistStatus heistStatus) {
		this.heistStatus = heistStatus;
	}

	public void setHeistSkills(List<HeistSkill> heistSkills) {
		this.heistSkills = heistSkills;
	}

	public void setHeistSkills(HeistSkillArrayDto heistSkillArrayDto) {
		List<HeistSkillDto> heistSkillDtos = heistSkillArrayDto.getHeistSkillDtos();
		List<HeistSkill> heistSkills = heistSkillDtos.stream().map(hsd -> HeistSkillDto.toEntity(hsd)).collect(Collectors.toList());
		this.heistSkills.removeIf(hs -> {
			String level = hs.getLevel();
			String name = hs.getSkill().getName();
			for (HeistSkillDto hsd : heistSkillDtos) {
				String dtoLevel = hsd.getLevel();
				String dtoName = hsd.getName();
				if (level.equalsIgnoreCase(dtoLevel) && name.equalsIgnoreCase(dtoName)) {
					return true;
				}
			}
			return false;
		});
		heistSkills.forEach(hs -> this.heistSkills.add(hs));
	}
	
	public void setHeistMembers(List<Member> heistMembers) {
		this.heistMembers = heistMembers;
	}
	
}
