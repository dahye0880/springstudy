package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;
	
		/*http://localhost:8090/springstudy/cart/cartAdd?id=8&quantity=1
		Cart : session에 저장하자고 해서 session을 가지고 온 것임
		매개변수랑 필요한것들 Integer id, Integer quantity,HttpSession session
									상품번호
		같은 상품을 다시 추가 하는 경우 수량만 증가하도록 프로그램을 수정하자
		*/
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity,HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		//1. id에 해당하는 Item 데이터를 저장
		Item item = service.getItem(id);
		//2.session에 저장된 Cart 객체를 조회
		Cart cart = (Cart)session.getAttribute("CART"); //카트는 세션에 등록되어 있다.
		if(cart == null) {//session에 "CART"인 객체가 없다. // 저장된 장바구니 객체가 없다.
			cart = new Cart();//빈껍데기 객체를 만든것. 비어있는 Array
			session.setAttribute("CART", cart);//session에 등록 // 위와 아래 cart객체는 똑같은것입
		}
		//item 은 파라미터 id 값에 해당하는 Item 정보, 수량을 ItemSet 객체로 저장 => Cart 객체에 추가
		cart.push(new ItemSet(item,quantity));//여기 푸쉬하면 session에도 들어간다. //똑같은게 있으면 push에서 이름은 빼고 수량만 증가시켜야 된다.
		mav.addObject("message",item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
	}
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {//int를 Integer로 하면 지워지지않는다      //뷰는 cart/cart이다. 매개변수 index
		/*
		 * 매개변수가 : int index => Object ArrayList.remove(index) => 순서에 해당하는 객체를 제거하고 제거된 객체를 리턴
		 * 매개변수가 : Integer index => void ArrayList.remove(index) => 그냥 index 객체를 제거
		 */
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet del = cart.getItemSetList().remove(index);//List에서 삭제하라는 명령 remove
		mav.addObject("message",del.getItem().getName() + "이(가) 장바구니에서 삭제됨");
		mav.addObject("cart",cart);//삭제된걸 뷰단으로 전달
		return mav;
	}
	//cartView 요청 구현하기.
	@RequestMapping("cartView")//장바구니 정보
		public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		mav.addObject("cart",cart);
		return mav;		
	}
	//checkout 요청
	@RequestMapping("checkout")
	public String checkOut(HttpSession session) {//id값을 안받으니 전혀 상관없이 그냥 이렇게 받으면 된다.
		return null;
	}
	/*
	 * 주문 확정 : end로 요청이 들어옴
	 * 1. 로그인 검증, 장바구니에 상품있는지 여부 검증 필요 => aop로 필요.
	 * 2. 장바구니 상품을 saleitem 테이블에 저장하기
	 * 3. 로그인 정보로 주문 정보(sale)테이블에 저장.
	 * 4. 장바구니 상품 제거
	 * 5. 주문 정보를 end.jsp(뷰단) 페이지로 출력을 함 
	 */
	@RequestMapping("checkend")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");//장바구니 상품 가져오는것
		User loginUser = (User)session.getAttribute("loginUser");//로그인 정보 가져오는것
		Sale sale = service.checkend(loginUser,cart);//sale 정보를 db단에서 가져오는것.
		mav.addObject("sale",sale);
		return mav;
	}
	
		
	
}
