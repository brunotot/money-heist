package ag04.hackathon2020.moneyheist.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.exception.ApiException;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;
import ag04.hackathon2020.moneyheist.mapper.SkillMapper;
import ag04.hackathon2020.moneyheist.validation.HeistValidator;

@Service
public class HeistService {

	private HeistMapper heistMapper;
	
	private SkillMapper skillMapper;
	
	private MemberMapper memberMapper;
	
	private HeistValidator heistValidator;
	
	public HeistService(HeistMapper heistMapper, SkillMapper skillMapper, HeistValidator heistValidator, MemberMapper memberMapper) {
		this.heistValidator = heistValidator;
		this.heistMapper = heistMapper;
		this.skillMapper = skillMapper;
		this.memberMapper = memberMapper;
	}
	
	@Transactional
	public Heist create(Heist heist) {
		heistValidator.validateDates(heist);
		heistValidator.validateDuplicateSkills(heist);
		heistValidator.validateName(heist);
		return saveHeistAndSkills(heist);
	}
	
	@Transactional
	private Heist saveHeistAndSkills(Heist heist) {
		heist = saveSkills(heist);
		return heistMapper.save(heist);
	}
	
	@Transactional
	private Heist saveSkills(Heist heist) {
		List<Skill> skills = heist.getSkills();
		List<Skill> createdSkills = skillMapper.save(skills);
		heist.getHeistSkills().forEach(hs -> {
			Skill s = hs.getSkill();
			String name = s.getName();
			hs.setSkill(Skill.find(name, createdSkills));
		});
		return heist;
	}
	
	public Heist findById(Long id) {
		Heist heist = heistMapper.findById(id);
		if (heist == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "Heist not found", "Heist with id: " + id + " not found", null);
		}
		return heist;
	}

	@Transactional
	public Heist update(Heist heist) {
		heistValidator.validateDuplicateSkills(heist);
		return saveHeistAndSkills(heist);
	}

	public List<Member> findEligibleMembers(Heist heist) {
		return memberMapper.findEligibleMembers(heist);
	}

	public void confirmHeistMembers(Heist heist, List<Member> members) {
		if (members.isEmpty()) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid members array", "Empty arrays are not allowed", null);
		}
		heistValidator.validateHeistStatus(heist);
		List<Member> eligibleMembers = findEligibleMembers(heist);
		heistValidator.validateEligibleMembersForConfirmation(heist, members, eligibleMembers);
		heist.setHeistStatus(HeistStatus.READY);
		heistMapper.save(heist);
	}

}
