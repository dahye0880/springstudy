package exception;

public class CartException extends RuntimeException {
	private String url;
	public CartException (String msg,String url) {//생성자로 만든것 // Exception만들때 이거말고 또 해줘야 되는게 있다. 그게뭐냐???? spring-mvc.xml에 구문 추가해줘야된다.
		super(msg);//생성자로써의 super // super. 으로 하면 다른거
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
