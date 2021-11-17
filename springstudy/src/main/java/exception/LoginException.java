package exception;

public class LoginException extends RuntimeException {
	private String url;
	public LoginException (String msg,String url) { //생성자(객체생성을 만들때 반드시 호출해야되는것, 생성자 없이는 객체생성 불가능)
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
