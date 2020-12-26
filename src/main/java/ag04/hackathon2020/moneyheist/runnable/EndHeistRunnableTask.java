package ag04.hackathon2020.moneyheist.runnable;

import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.entity.HeistStatus;
import ag04.hackathon2020.moneyheist.mapper.HeistMapper;

public class EndHeistRunnableTask implements Runnable {
	
	private HeistMapper heistMapper;
	
	private Long heistId;
	
	public EndHeistRunnableTask(HeistMapper heistMapper, Long heistId) {
		this.heistMapper = heistMapper;
		this.heistId = heistId;
	}

	@Override
	public void run() {
		Heist heist = heistMapper.findById(heistId);
		if (heist != null && HeistStatus.IN_PROGRESS.equals(heist.getHeistStatus())) {
			heist.setHeistStatus(HeistStatus.FINISHED);
			heistMapper.save(heist);
		}
	}
	
}