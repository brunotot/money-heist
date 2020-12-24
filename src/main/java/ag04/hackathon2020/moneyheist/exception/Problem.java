package ag04.hackathon2020.moneyheist.exception;

import java.io.Serializable;

public final class Problem implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Integer status;
	
	private final String title;

	private final String details;

	Problem(Integer status, String title, String details) {
		this.title = title;
		this.status = status;
		this.details = details;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getStatus() {
		return this.status;
	}

	public String getDetails() {
		return this.details;
	}

}