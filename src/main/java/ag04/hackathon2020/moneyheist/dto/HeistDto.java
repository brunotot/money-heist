package ag04.hackathon2020.moneyheist.dto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistOutcome;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;

public class HeistDto {

	private String name;
	
	private String location;

    private String startTime;

    private String endTime;
	
	private HeistStatus heistStatus;
	
	private HeistOutcome heistOutcome;
	
	public HeistDto() {
		super();
	}

	public HeistDto(String name, String location, String startTime, String endTime, List<HeistSkillDto> heistSkillDtos, HeistStatus heistStatus, HeistOutcome heistOutcome) {
		super();
		this.name = name;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.heistSkillDtos = heistSkillDtos;
		this.heistStatus = heistStatus;
		this.heistOutcome = heistOutcome;
	}

	@JsonProperty("skills")
	private List<HeistSkillDto> heistSkillDtos;

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public List<HeistSkillDto> getHeistSkillDtos() {
		return heistSkillDtos;
	}
	
	public HeistStatus getHeistStatus() {
		return this.heistStatus;
	}
	
	public HeistOutcome getHeistOutcome() {
		return this.heistOutcome;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setHeistSkillDtos(List<HeistSkillDto> heistSkillDtos) {
		this.heistSkillDtos = heistSkillDtos;
	}
	
	public void setHeistStatus(HeistStatus heistStatus) {
		this.heistStatus = heistStatus;
	}
	
	public void setHeistOutcome(HeistOutcome heistOutcome) {
		this.heistOutcome = heistOutcome;
	}

	public static Heist toEntity(HeistDto dto) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		Heist heist = new Heist();
		heist.setName(dto.getName());
		heist.setLocation(dto.getLocation());
		heist.setStartTime(ZonedDateTime.parse(dto.getStartTime(), formatter));
		heist.setEndTime(ZonedDateTime.parse(dto.getEndTime(), formatter));
		heist.setHeistSkills(dto.getHeistSkillDtos().stream().map(hsd -> HeistSkillDto.toEntity(hsd)).collect(Collectors.toList()));
		heist.setHeistStatus(dto.getHeistStatus() == null ? HeistStatus.PLANNING : dto.getHeistStatus());
		return heist;
	}

	public static HeistDto toDto(Heist entity) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		HeistDto dto = new HeistDto();
		dto.setName(entity.getName());
		dto.setLocation(entity.getLocation());
		dto.setStartTime(entity.getStartTime().format(formatter));
		dto.setEndTime(entity.getEndTime().format(formatter));
		dto.setHeistSkillDtos(entity.getHeistSkills().stream().map(hs -> HeistSkillDto.toDto(hs)).collect(Collectors.toList()));
		dto.setHeistStatus(entity.getHeistStatus() == null ? HeistStatus.PLANNING : entity.getHeistStatus());
		dto.setHeistOutcome(entity.getHeistOutcome() == null ? null : entity.getHeistOutcome());
		return dto;
	}
	
}
