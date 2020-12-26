package ag04.hackathon2020.moneyheist.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import ag04.hackathon2020.moneyheist.MoneyheistApplication;

@Configuration
@ComponentScan(basePackages = "ag04.hackathon2020.moneyheist", basePackageClasses = { MoneyheistApplication.class })
public class ThreadPoolTaskSchedulerConfig {

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		return threadPoolTaskScheduler;
	}
	
}