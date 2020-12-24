package ag04.hackathon2020.moneyheist.exception;

import java.util.Objects;

public abstract class ProblemException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final Problem problem;

	protected ProblemException(Problem problem, Throwable cause) {
        super(cause);
        Objects.requireNonNull(problem, "problem must not be null");
        this.problem = problem;
    }
	
	public Problem getProblem() {
		return this.problem;
	}

}