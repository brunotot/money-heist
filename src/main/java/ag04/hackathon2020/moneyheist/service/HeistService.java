package ag04.hackathon2020.moneyheist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.Skill;
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
		heistValidator.validateIfProperDates(heist);
		heistValidator.validateIfSkillsUniqueByNameAndLevel(heist);
		heistValidator.validateIfNotExists(heistMapper.findByName(heist.getName()));
		return saveHeistAndSkills(heist);
	}
	
	@Transactional
	private Heist saveHeistAndSkills(Heist heist) {
		return heistMapper.save(saveSkills(heist));
	}
	
	@Transactional
	private Heist saveSkills(Heist heist) {
		List<Skill> skills = heist.getSkills();
		List<Skill> createdSkills = skillMapper.save(skills);
		heist.getHeistSkills().forEach(hs -> hs.setSkill(Skill.find(hs.getSkill().getName(), createdSkills)));
		return heist;
	}
	
	public Heist findById(Long id) {
		Heist heist = heistMapper.findById(id);
		heistValidator.validateIfExists(heist);
		return heist;
	}

	@Transactional
	public Heist update(Heist heist) {
		heistValidator.validateIfSkillsUniqueByNameAndLevel(heist);
		return saveHeistAndSkills(heist);
	}

	public List<Member> findEligibleMembers(Heist heist) {
		return memberMapper.findEligibleMembers(heist);
	}

	public void confirmHeistMembers(Heist heist, List<Member> members) {
		heistValidator.validateIfMembersNotEmpty(members);
		heistValidator.validateIfProperHeistStatus(heist, List.of(HeistStatus.PLANNING));
		heistValidator.validateIfMembersEligibleForConfirmation(heist, members, findEligibleMembers(heist));
		heist.setHeistStatus(HeistStatus.READY);
		heist.setHeistMembers(members);
		heistMapper.save(heist);
	}

	public void startHeist(Heist heist) {
		heistValidator.validateIfProperHeistStatus(heist, List.of(HeistStatus.READY));
		heist.setHeistStatus(HeistStatus.IN_PROGRESS);
		heistMapper.save(heist);
	}

}
