package exception;
//RuntimeException : 예외처리 생략가능
public class BoardException extends RuntimeException {//RuntimeException 이건 예외처리를 생략해도 된다.
	private String url;
	public BoardException (String msg,String url) { //생성자(객체생성을 만들때 반드시 호출해야되는것, 생성자 없이는 객체생성 불가능)
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
/*
 *                     Throwable
 * Exception(예외)           Error(오류)
 * 			|
 *   IOException
 *   InterruptedException
 *   RuntimeException -> getMessage() 메서드를 멤버로 갖고 있다.
 *              .....
 *              => 예외는 RuntimeException과 그외 Exception으로 나눈다.
 *              		RuntimeException : 예외 생략 가능
 */
