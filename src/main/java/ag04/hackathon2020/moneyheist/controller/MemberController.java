package ag04.hackathon2020.moneyheist.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ag04.hackathon2020.moneyheist.dto.MemberDto;
import ag04.hackathon2020.moneyheist.entity.Member;
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
	
}
