package ag04.hackathon2020.moneyheist.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(ProblemException.class)
	public ResponseEntity<Problem> handleProblem(ProblemException ex) {
		return buildResponse(ex.getProblem());
	}

	private static ResponseEntity<Problem> buildResponse(Problem problem) {
        return ResponseEntity.status(problem.getStatus()) 
                .contentType(MediaType.APPLICATION_JSON)
                .body(problem);
    }
	
}