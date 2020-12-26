package ag04.hackathon2020.moneyheist.runnable;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;
import ag04.hackathon2020.moneyheist.service.MailingService;

public class StartHeistRunnableTask implements Runnable {

	private HeistMapper heistMapper;
	
	private MailingService mailingService;
	
	private Long heistId;
	
	public StartHeistRunnableTask(HeistMapper heistMapper, Long heistId, MailingService mailingService) {
		this.heistMapper = heistMapper;
		this.heistId = heistId;
		this.mailingService = mailingService;
	}

	@Override
	public void run() {
		Heist heist = heistMapper.findById(heistId);
		if (heist != null && HeistStatus.READY.equals(heist.getHeistStatus())) {
			heist.setHeistStatus(HeistStatus.IN_PROGRESS);
			Heist newHeist = heistMapper.save(heist);
			newHeist.getHeistMembers().forEach(m -> mailingService.sendMail(m.getEmail(), "Heist started", "Heist with ID: " + heistId + " has started."));
		}
	}

}