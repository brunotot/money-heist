package ag04.hackathon2020.moneyheist.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ag04.hackathon2020.moneyheist.dto.HeistDto;
import ag04.hackathon2020.moneyheist.entity.Heist;
import ag04.hackathon2020.moneyheist.service.HeistService;

@Controller
@RequestMapping("/heist")
public class HeistController {

	private HeistService heistService;
	
	public HeistController(HeistService heistService) {
		this.heistService = heistService;
	}
	
	@PostMapping
	public ResponseEntity<Void> createHeist(@RequestBody HeistDto dto) {
		Heist entity = HeistDto.toEntity(dto);
		Heist createdHeist = heistService.create(entity);
		String heistPath = "/heist/" + createdHeist.getId();
		URI locationURI = URI.create(heistPath);
		return ResponseEntity.created(locationURI).build();
	}
	
}
