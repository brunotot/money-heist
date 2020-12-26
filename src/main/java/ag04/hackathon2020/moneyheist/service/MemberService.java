package ag04.hackathon2020.moneyheist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;
import ag04.hackathon2020.moneyheist.mapper.SkillMapper;
import ag04.hackathon2020.moneyheist.validation.MemberValidator;

@Service
public class MemberService {

	private MemberMapper memberMapper;
	
	private MemberValidator memberValidator;
	
	private SkillMapper skillMapper;
	
	private MailingService mailingService;
	
	public MemberService(MemberMapper memberMapper, MemberValidator memberValidator, SkillMapper skillMapper, MailingService mailingService) {
		this.memberMapper = memberMapper;
		this.memberValidator = memberValidator;
		this.skillMapper = skillMapper;
		this.mailingService = mailingService;
	}
	
	@Transactional
	public Member update(Member member) {
		memberValidator.validateIfSkillsUniqueByName(member);
		memberValidator.validateIfMainSkillProperlyReferenced(member);
		memberValidator.validateIfProperSkillLevels(member);
		return saveMemberAndSkills(member);
	}
	
	@Transactional
	public Member create(Member member) {
		memberValidator.validateIfNotExists(memberMapper.findByEmail(member.getEmail()));
		memberValidator.validateIfSkillsUniqueByName(member);
		memberValidator.validateIfMainSkillProperlyReferenced(member);
		memberValidator.validateIfProperSkillLevels(member);
		Member newMember = saveMemberAndSkills(member);
		mailingService.sendMail(newMember.getEmail(), "Added to a heist", "You have been added as a member. Your ID is: " + newMember.getId());
		return newMember;
	}
	
	public Member findById(Long id) {
		Member member = memberMapper.findById(id);
		memberValidator.validateIfExists(member);
		return member;
	}

	public List<Member> findByNames(List<String> names) {
		return names.stream()
				.map(name -> findByName(name))
				.collect(Collectors.toList());
	}
	
	public Member findByName(String name) {
		Member member = memberMapper.findByName(name);
		memberValidator.validateIfExists(member);
		return member;
	}
	
	@Transactional
	private Member saveMemberAndSkills(Member member) {
		member = saveSkills(member);
		return memberMapper.save(member);
	}
	
	@Transactional
	private Member saveSkills(Member member) {
		Skill mainSkill = member.getMainSkill();
		List<Skill> skills = member.getSkills();
		List<Skill> createdSkills = skillMapper.save(skills);
		member.getMemberSkills().forEach(ms -> {
			Skill s = ms.getSkill();
			String name = s.getName();
			ms.setSkill(Skill.find(name, createdSkills));
		});
		if (mainSkill != null) {
			String name = mainSkill.getName();
			member.setMainSkill(Skill.find(name, createdSkills));
		}
		return member;
	}

	public void deleteMemberSkill(Member member, String skillName) {
		memberValidator.validateIfSkillExistsOnMember(member, skillName);
		List<MemberSkill> newMemberSkills = member.getMemberSkills().stream()
				.filter(ms -> !ms.getSkill().getName().equalsIgnoreCase(skillName))
				.collect(Collectors.toList());
		member.setMemberSkills(newMemberSkills);
		Skill mainSkill = member.getMainSkill();
		if (mainSkill != null && mainSkill.getName().equalsIgnoreCase(skillName)) {
			member.setMainSkill(null);
		}
		update(member);
	}
	
}
