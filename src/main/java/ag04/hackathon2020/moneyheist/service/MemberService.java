package ag04.hackathon2020.moneyheist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.exception.ApiException;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;
import ag04.hackathon2020.moneyheist.mapper.SkillMapper;
import ag04.hackathon2020.moneyheist.validation.MemberValidator;

@Service
public class MemberService {

	private MemberMapper memberMapper;
	
	private MemberValidator memberValidator;
	
	private SkillMapper skillMapper;
	
	public MemberService(MemberMapper memberMapper, MemberValidator memberValidator, SkillMapper skillMapper) {
		this.memberMapper = memberMapper;
		this.memberValidator = memberValidator;
		this.skillMapper = skillMapper;
	}
	
	@Transactional
	public Member update(Member member) {
		memberValidator.validateDuplicateSkillNames(member);
		memberValidator.validateMainSkillReference(member);
		memberValidator.validateSkillLevels(member);
		return saveMemberAndSkills(member);
	}
	
	@Transactional
	public Member create(Member member) {
		memberValidator.validateMemberAlreadyExists(member);
		memberValidator.validateDuplicateSkillNames(member);
		memberValidator.validateMainSkillReference(member);
		memberValidator.validateSkillLevels(member);
		return saveMemberAndSkills(member);
	}
	
	public Member findById(Long id) {
		Member member = memberMapper.findById(id);
		if (member == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "Member not found", "Member with id: " + id + " not found", null);
		}
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
		memberValidator.validateSkillExists(member, skillName);
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
