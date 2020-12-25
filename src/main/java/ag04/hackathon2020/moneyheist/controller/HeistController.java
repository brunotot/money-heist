package ag04.hackathon2020.moneyheist.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

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
import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.service.HeistService;
import ag04.hackathon2020.moneyheist.service.MemberService;

@Controller
@RequestMapping("/heist")
public class HeistController {

	private HeistService heistService;

	private MemberService memberService;
	
	public HeistController(HeistService heistService, MemberService memberService) {
		this.heistService = heistService;
		this.memberService = memberService;
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
	
}
