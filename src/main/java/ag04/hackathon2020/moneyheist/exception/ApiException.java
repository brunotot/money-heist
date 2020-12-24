package ag04.hackathon2020.moneyheist.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends ProblemException {

	private static final long serialVersionUID = 1L;

	public ApiException(HttpStatus status, String title, String details, Throwable cause) {
        super(new Problem(status.value(), title, details), cause);
    }

}