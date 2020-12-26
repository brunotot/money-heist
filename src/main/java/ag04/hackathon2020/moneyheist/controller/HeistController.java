package ag04.hackathon2020.moneyheist.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ag04.hackathon2020.moneyheist.dto.EligibleMembersDto;
import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.HeistSkillDto;
import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistOutcome;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.service.HeistService;
import ag04.hackathon2020.moneyheist.service.MemberService;
import ag04.hackathon2020.moneyheist.validation.HeistValidator;

@Controller
@RequestMapping("/heist")
public class HeistController {

	private HeistService heistService;

	private MemberService memberService;
	
	private HeistValidator heistValidator;
	
	public HeistController(HeistService heistService, MemberService memberService, HeistValidator heistValidator) {
		this.heistService = heistService;
		this.memberService = memberService;
		this.heistValidator = heistValidator;
	}

	@PostMapping
	public ResponseEntity<Void> createHeist(@RequestBody HeistDto dto) {
		Heist entity = HeistDto.toEntity(dto);
		Heist createdHeist = heistService.create(entity);
		String heistPath = "/heist/" + createdHeist.getId();
		URI locationURI = URI.create(heistPath);
		return ResponseEntity.created(locationURI).build();
	}
	
	@PatchMapping("/{heistId}/skills")
	public ResponseEntity<Void> updateHeist(@PathVariable Long heistId, @RequestBody HeistSkillArrayDto skills) {
		Heist heist = heistService.findById(heistId);
		heist.setHeistSkills(skills);
		heistService.update(heist);
		String contentLocation = "/heist/" + heistId + "/skills";
		return ResponseEntity.noContent().header("Content-Location", contentLocation).build();
	}
	
	@GetMapping("/{heistId}/eligible_members")
	public ResponseEntity<EligibleMembersDto> viewEligibleMembers(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		List<Member> eligibleMembers = heistService.findEligibleMembers(heist);
		EligibleMembersDto dto = EligibleMembersDto.toDto(heist, eligibleMembers);
		return ResponseEntity.ok(dto);
	}
	
	@PutMapping("/{heistId}/members")
	public ResponseEntity<Void> confirmHeistMembers(@PathVariable Long heistId, @RequestBody Map<String, Object> json) {
		Heist heist = heistService.findById(heistId);
		@SuppressWarnings("unchecked")
		List<String> memberNames = (List<String>) json.get("members");
		List<Member> members = memberService.findByNames(memberNames);
		heistService.confirmHeistMembers(heist, members);
		String contentLocation = "/heist/" + heistId + "/members";
		return ResponseEntity.noContent().header("Content-Location", contentLocation).build();
	}
	
	@PutMapping("/{heistId}/start")
	public ResponseEntity<Void> startHeist(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		heistService.startHeist(heist);
		String heistPath = "/heist/" + heistId + "/status";
		URI locationURI = URI.create(heistPath);
		return ResponseEntity.ok().location(locationURI).build();
	}
	
	@GetMapping("/{heistId}")
	public ResponseEntity<HeistDto> getHeist(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		HeistDto heistDto = HeistDto.toDto(heist);
		return ResponseEntity.ok(heistDto);
	}
	
	@GetMapping("/{heistId}/members")
	public ResponseEntity<List<MemberDto>> getHeistMembers(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		heistValidator.validateIfProperHeistStatus(heist, List.of(HeistStatus.READY, HeistStatus.IN_PROGRESS, HeistStatus.FINISHED));
		List<Member> heistMembers = heist.getHeistMembers();
		List<MemberDto> heistMemberDtos = heistMembers.stream().map(m -> MemberDto.toDto(m)).collect(Collectors.toList());
		return ResponseEntity.ok(heistMemberDtos);
	}
	
	@GetMapping(path = "/{heistId}/status", produces = "application/json")
	public ResponseEntity<String> getHeistStatus(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		String jsonResponse = "{\"status\": \"" + heist.getHeistStatus() + "\"}";
		return ResponseEntity.ok(jsonResponse);
	}
	
	@GetMapping("/{heistId}/skills")
	public ResponseEntity<List<HeistSkillDto>> getHeistSkills(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		List<HeistSkillDto> heistSkillDtos = heist.getHeistSkills().stream().map(hs -> HeistSkillDto.toDto(hs)).collect(Collectors.toList());
		return ResponseEntity.ok(heistSkillDtos);
	}
	
	@GetMapping(path = "/{heistId}/outcome", produces = "application/json")
	public ResponseEntity<String> getHeistOutcome(@PathVariable Long heistId) {
		Heist heist = heistService.findById(heistId);
		heistValidator.validateIfProperHeistStatus(heist, List.of(HeistStatus.FINISHED));
		HeistOutcome outcome = heist.getHeistOutcome();
		String jsonResponse = "{\"outcome\": \"" + outcome + "\"}";
		return ResponseEntity.ok(jsonResponse);
	}
	
}
