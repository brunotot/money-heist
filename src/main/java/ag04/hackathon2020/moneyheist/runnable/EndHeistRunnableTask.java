package ag04.hackathon2020.moneyheist.runnable;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistOutcome;
import ag04.hackathon2020.moneyheist.entity.HeistSkill;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberSkill;
import ag04.hackathon2020.moneyheist.entity.MemberStatus;
import ag04.hackathon2020.moneyheist.entity.Skill;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;
import ag04.hackathon2020.moneyheist.service.MailingService;

public class EndHeistRunnableTask implements Runnable {
	
	private HeistMapper heistMapper;
	
	private MailingService mailingService;
	
	private Heist heistTemp;
	
	private Integer levelUpTime;
	
	public EndHeistRunnableTask(HeistMapper heistMapper, Heist heist, MailingService mailingService, Integer levelUpTime) {
		this.heistMapper = heistMapper;
		this.heistTemp = heist;
		this.mailingService = mailingService;
		this.levelUpTime = levelUpTime;
	}

	@Override
	public void run() {
		Heist heist = heistMapper.findById(heistTemp.getId());
		if (heist != null && HeistStatus.IN_PROGRESS.equals(heist.getHeistStatus())) {
			heist.setHeistStatus(HeistStatus.FINISHED);
			List<HeistSkill> heistSkills = heistTemp.getHeistSkills();
			Random random = new Random();
			int totalMembersRequired = heistSkills.stream().mapToInt(hs -> hs.getMembers()).sum();
			List<Member> heistMembers = heist.getHeistMembers();
			int totalMembersUsed = heistMembers.size();
			float percentage = ((float) totalMembersUsed / totalMembersRequired) * 100;
			HeistOutcome outcome;
			int amountOfMembersToUpdate = 0;
			boolean incarceratedOnly = false;
			if (percentage < 50) {
				outcome = HeistOutcome.FAILED;
				amountOfMembersToUpdate = totalMembersUsed;
			} else if (percentage < 75) {
				outcome = random.nextInt() % 2 == 0 ? HeistOutcome.FAILED : HeistOutcome.SUCCEEDED;
				if (HeistOutcome.SUCCEEDED.equals(outcome)) {
					amountOfMembersToUpdate = (int) Math.ceil((float) 0.33 * totalMembersUsed);
				} else {
					amountOfMembersToUpdate = (int) Math.ceil((float) 0.66 * totalMembersUsed);
				}
			} else if (percentage < 100) {
				outcome = HeistOutcome.SUCCEEDED;
				amountOfMembersToUpdate = (int) Math.ceil((float) 0.33 * totalMembersUsed);
				incarceratedOnly = true;
			} else {
				outcome = HeistOutcome.SUCCEEDED;
			}
			for (int i = 0; i < amountOfMembersToUpdate; i++) {
				MemberStatus memberStatus;
				if (incarceratedOnly) {
					memberStatus = MemberStatus.INCARCERATED;
				} else {
					memberStatus = random.nextInt() % 2 == 0 ? MemberStatus.EXPIRED : MemberStatus.INCARCERATED;
				}
				heistMembers.get(i).setStatus(memberStatus);
			}

			for (HeistSkill hs : heistSkills) {
				Skill s = hs.getSkill();
				List<Member> membersToUpdate = heistMembers.stream().filter(m -> m.getMemberSkills().stream()
						.map(ms -> ms.getSkill().getName()).collect(Collectors.toList())
						.contains(s.getName())).collect(Collectors.toList());
				for (Member memberToUpdate : membersToUpdate) {
					List<MemberSkill> memberSkills = memberToUpdate.getMemberSkills();
					MemberSkill wantedSkill = memberSkills.stream().filter(ms -> ms.getSkill().getName().equals(s.getName())).findFirst().get();
					ZonedDateTime start = heist.getStartTime();
					ZonedDateTime end = heist.getEndTime();
					int currentExperience = wantedSkill.getExperience() + (int) ChronoUnit.SECONDS.between(start, end);
					String currentSkillLevel = wantedSkill.getLevel();
					int levelUpgrade = (int) ((float) currentExperience / levelUpTime);
					if (levelUpgrade != 0 && currentSkillLevel.length() < 10) {
						while (levelUpgrade > 0 && currentSkillLevel.length() < 10) {
							currentSkillLevel += "*";
							levelUpgrade--;
							currentExperience -= levelUpTime;
						}
					}
					int wantedSkillIndex = memberSkills.indexOf(wantedSkill);
					wantedSkill.setLevel(currentSkillLevel);
					wantedSkill.setExperience(currentExperience);
					memberSkills.remove(wantedSkillIndex);
					memberSkills.add(wantedSkill);
					memberToUpdate.setMemberSkills(memberSkills);
				}
				for (Member hm : heistMembers) {
					final Long hmId = hm.getId();
					Member m = membersToUpdate.stream().filter(mem -> mem.getId().equals(hmId)).findFirst().orElse(null);
					if (m != null) {
						hm = m;
					}
				}
			}
			heist.setHeistMembers(heistMembers);
			heist.setHeistOutcome(outcome);
			Heist newHeist = heistMapper.save(heist);
			newHeist.getHeistMembers().forEach(m -> mailingService.sendMail(m.getEmail(), "Heist finished", "Heist with ID: " + heist.getId() + " has finished. Its outcome was: " + newHeist.getHeistOutcome()));
		}
	}
	
}