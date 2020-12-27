package ag04.hackathon2020.moneyheist.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillArrayDto;
import ag04.hackathon2020.moneyheist.dto.MemberSkillDto;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

	private MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@PostMapping
	public ResponseEntity<Void> createMember(@RequestBody MemberDto dto) {
		Member entity = MemberDto.toEntity(dto);
		Member createdMember = memberService.create(entity);
		String memberPath = "/member/" + createdMember.getId();
		URI locationURI = URI.create(memberPath);
		return ResponseEntity.created(locationURI).build();
	}
	
	@PutMapping("/{memberId}/skills")
	public ResponseEntity<Void> updateMemberSkills(@RequestBody MemberSkillArrayDto dto, @PathVariable Long memberId) {
		Member member = memberService.findById(memberId);
		member.setMemberSkills(dto);
		memberService.update(member);
		String contentLocation = "/member/" + memberId + "/skills";
		return ResponseEntity.noContent().header("Content-Location", contentLocation).build();
	}
	
	@DeleteMapping("/{memberId}/skills/{skillName}")
	public ResponseEntity<Void> deleteMemberSkill(@PathVariable Long memberId, @PathVariable String skillName) {
		Member member = memberService.findById(memberId);
		memberService.deleteMemberSkill(member, skillName);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{memberId}")
	public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) {
		Member member = memberService.findById(memberId);
		MemberDto dto = MemberDto.toDto(member);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{memberId}/skills")
	public ResponseEntity<MemberSkillArrayDto> getMemberSkills(@PathVariable Long memberId) {
		Member member = memberService.findById(memberId);
		List<MemberSkillDto> memberSkillDtos = member.getMemberSkills().stream().map(ms -> MemberSkillDto.toDto(ms)).collect(Collectors.toList());
		Skill mainSkill = member.getMainSkill();
		String mainSkillString = mainSkill == null ? null : mainSkill.getName();
		MemberSkillArrayDto memberSkillArrayDto = new MemberSkillArrayDto(memberSkillDtos, mainSkillString);
		return ResponseEntity.ok(memberSkillArrayDto);
	}
	
}
