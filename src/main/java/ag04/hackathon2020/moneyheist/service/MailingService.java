package ag04.hackathon2020.moneyheist.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailingService {

	private JavaMailSender javaMailSender;
	
	public MailingService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("brunotot10000temp@gmail.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(body);
        javaMailSender.send(message);
	}
	
}
