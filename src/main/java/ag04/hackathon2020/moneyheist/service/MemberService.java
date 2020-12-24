package ag04.hackathon2020.moneyheist.service;

import org.springframework.stereotype.Service;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;
import ag04.hackathon2020.moneyheist.validation.MemberValidator;

@Service
public class MemberService {

	private MemberMapper memberMapper;
	
	private MemberValidator memberValidator;
	
	public MemberService(MemberMapper memberMapper, MemberValidator memberValidator) {
		this.memberMapper = memberMapper;
		this.memberValidator = memberValidator;
	}
	
	public Member create(Member member) {
		memberValidator.validate(member);
		return memberMapper.save(member);
	}
	
}
