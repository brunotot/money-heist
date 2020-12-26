package ag04.hackathon2020.moneyheist.runnable;

import java.util.List;
import java.util.Random;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistOutcome;
import ag04.hackathon2020.moneyheist.entity.HeistSkill;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.entity.Member;
import ag04.hackathon2020.moneyheist.entity.MemberStatus;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;
import ag04.hackathon2020.moneyheist.service.MailingService;

public class EndHeistRunnableTask implements Runnable {
	
	private HeistMapper heistMapper;
	
	private MailingService mailingService;
	
	private Long heistId;
	
	public EndHeistRunnableTask(HeistMapper heistMapper, Long heistId, MailingService mailingService) {
		this.heistMapper = heistMapper;
		this.heistId = heistId;
		this.mailingService = mailingService;
	}

	@Override
	public void run() {
		Heist heist = heistMapper.findById(heistId);
		if (heist != null && HeistStatus.IN_PROGRESS.equals(heist.getHeistStatus())) {
			heist.setHeistStatus(HeistStatus.FINISHED);
			List<HeistSkill> heistSkills = heist.getHeistSkills();
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
			heist.setHeistMembers(heistMembers);
			heist.setHeistOutcome(outcome);
			Heist newHeist = heistMapper.save(heist);
			newHeist.getHeistMembers().forEach(m -> mailingService.sendMail(m.getEmail(), "Heist finished", "Heist with ID: " + heistId + " has finished. Its outcome was: " + newHeist.getHeistOutcome()));
		}
	}
	
}