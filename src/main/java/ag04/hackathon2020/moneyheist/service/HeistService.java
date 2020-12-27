package ag04.hackathon2020.moneyheist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;
import ag04.hackathon2020.moneyheist.mapper.MemberMapper;
import ag04.hackathon2020.moneyheist.mapper.SkillMapper;
import ag04.hackathon2020.moneyheist.runnable.EndHeistRunnableTask;
import ag04.hackathon2020.moneyheist.runnable.StartHeistRunnableTask;
import ag04.hackathon2020.moneyheist.validation.HeistValidator;

@Service
public class HeistService {

	private HeistMapper heistMapper;
	
	private SkillMapper skillMapper;
	
	private MemberMapper memberMapper;
	
	private HeistValidator heistValidator;
	
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	private MailingService mailingService;
	
	@Value("${levelUpTime}")
	private Integer levelUpTime;
	
	public HeistService(HeistMapper heistMapper, SkillMapper skillMapper, HeistValidator heistValidator, MemberMapper memberMapper, ThreadPoolTaskScheduler threadPoolTaskScheduler, MailingService mailingService) {
		this.heistValidator = heistValidator;
		this.heistMapper = heistMapper;
		this.skillMapper = skillMapper;
		this.memberMapper = memberMapper;
		this.threadPoolTaskScheduler = threadPoolTaskScheduler;
		this.mailingService = mailingService;
	}
	
	@Transactional
	public Heist create(Heist heist) {
		heistValidator.validateIfProperDates(heist);
		heistValidator.validateIfSkillsUniqueByNameAndLevel(heist);
		heistValidator.validateIfNotExists(heistMapper.findByName(heist.getName()));
		Heist newHeist = saveHeistAndSkills(heist);
		threadPoolTaskScheduler.schedule(new StartHeistRunnableTask(heistMapper, heist.getId(), mailingService), heist.getStartTime().toInstant());
		threadPoolTaskScheduler.schedule(new EndHeistRunnableTask(heistMapper, heist, mailingService, levelUpTime), heist.getEndTime().toInstant());	
		return newHeist;
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
		heistValidator.validateIfProperHeistStatus(heist, List.of(HeistStatus.FINISHED, HeistStatus.PLANNING, HeistStatus.READY));
		return saveHeistAndSkills(heist);
	}

	public List<Member> findEligibleMembers(Heist heist) {
		heistValidator.validateIfMembersNotAlreadyConfirmed(heist);
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
		Heist newHeist = heistMapper.save(heist);
		newHeist.getHeistMembers().forEach(m -> mailingService.sendMail(m.getEmail(), "Heist started", "Heist with ID: " + newHeist.getId() + " has started."));
	}

	public Heist save(Heist heist) {
		return heistMapper.save(heist);
	}

}
