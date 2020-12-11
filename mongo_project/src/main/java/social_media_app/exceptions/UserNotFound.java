package social_media_app.exceptions;

public class UserNotFound extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2990703270646387561L;

	public UserNotFound(String msg) {
		super(msg);
	}
}
