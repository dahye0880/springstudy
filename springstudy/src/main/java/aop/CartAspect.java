package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.CartException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component
@Aspect
public class CartAspect {

	@Around("execution(* controller.Cart*.check*(..)) && args(..,session)")//CartController.java에서 public String checkOut(HttpSession session)
	//																											가 check.. 이기 때문에 execution에 저렇게 준것이다.
	public Object checkOut(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");//로그인 정보는 User객체이다.
		if(loginUser == null) {
			throw new LoginException("회원만 주문 가능합니다. 로그인 후 거래하세요","../user/login");
		}
		Cart cart = (Cart)session.getAttribute("CART");//카트라는 객체를 세션으로부터 가져온다.
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new CartException("장바구니에 주문할 상품이 없습니다.","../item/list");
		}
		return joinPoint.proceed();
	}
	
	
	
}
