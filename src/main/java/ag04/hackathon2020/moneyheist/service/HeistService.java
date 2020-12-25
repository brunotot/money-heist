package ag04.hackathon2020.moneyheist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;
import ag04.hackathon2020.moneyheist.mapper.SkillMapper;
import ag04.hackathon2020.moneyheist.validation.HeistValidator;

@Service
public class HeistService {

	private HeistMapper heistMapper;
	
	private SkillMapper skillMapper;
	
	private HeistValidator heistValidator;
	
	public HeistService(HeistMapper heistMapper, SkillMapper skillMapper, HeistValidator heistValidator) {
		this.heistValidator = heistValidator;
		this.heistMapper = heistMapper;
		this.skillMapper = skillMapper;
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

}
