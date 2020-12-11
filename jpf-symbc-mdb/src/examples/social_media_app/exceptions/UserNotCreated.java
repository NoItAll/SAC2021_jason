package social_media_app.exceptions;

public class UserNotCreated extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8399735015334127977L;

	public UserNotCreated(String msg) {
		super(msg);
	}
}
