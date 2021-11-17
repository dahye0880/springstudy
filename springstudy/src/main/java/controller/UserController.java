package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;



		@GetMapping("*")
		public ModelAndView getUser() {
			ModelAndView mav = new ModelAndView();
			mav.addObject(new User());
			return mav;
		}
		/* 회원등록 - POST 방식으로 요청이 들어온것.
		 * 1. 유효성검증
		 * 	    User객체에 저장(스프링이 알아서 해줌)된 파라미터 값을 이용하여 유효성 검증을 하면됨.
		 * 2. 입력받은 User 정보를 useraccount 테이블에 저장만 하면된다.
		 * 3. 저장 완료 후 login 페이지를(redirect) 요청
		 */
		@PostMapping("userEntry")
		public ModelAndView userEntry(@Valid User user, BindingResult bresult) {//유효성 검증이 된다. @Valid
			ModelAndView mav = new ModelAndView();
			if(bresult.hasErrors()) {
				mav.getModel().putAll(bresult.getModel());
				bresult.reject("error.input.user");
				return mav;
			}
			try {
				service.userInsert(user);
				mav.addObject("user",user);
				//DataIntegrityViolationException : 중복오류.
			}catch(DataIntegrityViolationException e) {
				e.printStackTrace();
				bresult.reject("error.duplicate.user");
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
			mav.setViewName("redirect:login");
			return mav;
		}
		/* 로그인 - POST 방식으로 요청이 들어온것.
		 * 1. 유효성검증
		 * 	    User객체에 저장(스프링이 알아서 해줌)된 파라미터 값을 이용하여 유효성 검증을 하면됨.
		 * 2. 입력받은 userid,password 로 db에서 해당 정보를 읽어와야된다.
		 *      - userid가 없는 경우 
		 *      - password가 틀린 경우
		 *      - 정상적인 사용자인 경우
		 *        session에 로그인 정보 등록하기
		 */
		@PostMapping("login")
		public ModelAndView login(@Valid User user, BindingResult bresult,HttpSession session) {//유효성 검증이 된다. @Valid
			ModelAndView mav = new ModelAndView();
			if(bresult.hasErrors()) {
				mav.getModel().putAll(bresult.getModel());
				bresult.reject("error.input.login");
				return mav;
			}/////////4교시는 아래
			try {
			User dbUser = service.userSelectOne(user.getUserid());
			//비밀번호 비교
			if(user.getPassword().equals(dbUser.getPassword())) {
				session.setAttribute("loginUser", dbUser);
				mav.setViewName("redirect:main");
			}else {
				bresult.reject("error.login.password"); //비밀번호가 틀립니다.
				mav.getModel().putAll(bresult.getModel());
			}
			//EmptyResultDataAccessException : 해당 레코드 없음
			}catch(EmptyResultDataAccessException e) {
				bresult.reject("error.login.id"); //해당 아이디가 없습니다.
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
			return mav;
		}///////////5교시 (로그아웃을 시키면서 session을 다 지워주는것)
		//loginCheck : 로그인이 필요한 기능앞에 이걸 붙여준다.
		@RequestMapping("logout")
		public String loginChecklogout(HttpSession session) {//=>PointCut에 의해 설정된 핵심메서드 //session 한브라우저에 대한 객체 저장소라고 일단 생각~!
			session.invalidate();
			return "redirect:login";
		}
		@RequestMapping("main")
		public String loginCheckmain(HttpSession session) {
			return null;
		}
		//{url}search : {url} : 지정하지 않은것. *search 인 요청인 경우 호출되는 메서드
		//@PathVariable String url : {url} 매개변수로 전달
		/*
		 * 2021-11-12 문제
		 * 비밀번호 찾기 완성하기
		 * 1. layout 제외하기
		 * 2. search.hsp 페이지로 결곽밧을 넘겼을때(전송시) 비밀번호 앞 두자리는 **  나머지 비밀번호3번째부터  뷰단으로 전송.
		 * 		1234=>**34
		 */
		@PostMapping("{url}search")
		public ModelAndView search(User user, BindingResult bresult, @PathVariable String url) {
			ModelAndView mav = new ModelAndView();
			String code = "error.userid.search";
			String title = "아이디";
			
			//간단한 유효성 검증
			if(user.getEmail() == null || user.getEmail().equals("")) {
				bresult.rejectValue("email", "error.required"); //@Valid에서 처리해주는 방식.
			}
			if(user.getPhoneno() == null || user.getPhoneno().equals("")) {
				bresult.rejectValue("phoneno", "error.required");
			}
			if(url.equals("pw")) {
				title="비밀번호";
				code = "error.password.search";
				if(user.getUserid()==null || user.getUserid().equals("")) {
					bresult.rejectValue("userid", "error.required");
				}
			}
			if(bresult.hasErrors()) {
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
			String result = null;
			try {
				result = service.getSearch(user,url);	//이 결과가 아이디값이다.
				//result = result.substring(0,result.length()-2)+"**"; //이렇게 아이디 뒤에를 *로 해줄수 있다. //이건 많이 쓰지 않도록 하자
			}catch(EmptyResultDataAccessException e) {
				bresult.reject(code);
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
			mav.addObject("result",result);
			mav.addObject("title",title);
			mav.setViewName("search");
			return mav;
		}
		/*
		 * AOP 설정하기
		 * 1. pointcut : UserController 클래스의 idCheck로 시작하는 메서드의 매개변수가 id,session인 경우
		 * 2. 로그인이 안된경우 : 로그인하세요. 메세지 출력. login 페이지로 이동
		 * 3. 로그인이 된경우 : 
		 *               admin이 아니고, 로그인한 아이디가 아닌 경우 '본인정보만 가능합니다.' 메세지 출력 후, main 페이지로 이동.
		 *               관리자인 경우는 타인정보에 대한 거래 가능하도록.
		 */
		@RequestMapping("mypage")
		public ModelAndView idCheckMypage(String id, HttpSession session) {//조회해야 되는 id값
			ModelAndView mav = new ModelAndView();
			//1. db에서 id에 해당하는 User 객체를 조회해서 
			User user = service.userSelectOne(id);
			mav.addObject("user", user);
			return mav;
		}
		//UserLoginAspect.userIdCheck() 메서드가 먼저 실행이 되겠구나 라는 것을 알 수 있음 : 로그인여부, 본인정보부분 검증
		@GetMapping({"update","delete"})
		public ModelAndView idCheckUpdate(String id, HttpSession session) {
			ModelAndView mav = new ModelAndView();
			User user = service.userSelectOne(id);
			mav.addObject("user", user);
			return mav;
		}
		/*
		 * 1. 유효성 검증하기.
		 * 2. 비밀번호 검증
		 * 		- 비밀번호 오류 : error.login.password 코드를 입력하여, update.jsp 페이지로 이동.
		 * 	3. 비밀번호 맞음 : userid에 해당하는 고객정보를 수정하기
		 * 4. 수정 성공 :session의 로그인 정보 수정 
		 * 					main 페이지로 이동
		 * 		수정 실패 : 수정실패 메세지 출력. update로 페이지를 이동하기
		 */
		/**
		 * 
		 * @param user		update 폼에서 입력된 유저 정보
		 * @param bresult		form:form에서 modelAttribute(=user)를 사용할 때 유효성 검사를 위해 생성되는 정보
		 * @param session	세션 ("loginUser") 객체 포함
		 * @return
		 */
		@PostMapping("update")
		public ModelAndView update(@Valid User user, BindingResult bresult,HttpSession session) {//유효성,결과
			ModelAndView mav = new ModelAndView();
			// 1. 유효성 검사
			if(bresult.hasErrors()) {
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
			
			// 2. 비밀번호 검사 1) 관리자의 경우 2) 일반사용자의 경우
			User loginUser = (User)session.getAttribute("loginUser");
			String pw = null;
			if (loginUser.getUserid().equals("admin")) { // 접속자가 관리자
				pw = loginUser.getPassword();
			} else { // 접속자가 사용자
				if (!loginUser.getUserid().equals(user.getUserid())) {
					throw new LoginException("본인의 경우만 수정 가능합니다.", "login");
				}
				pw = loginUser.getPassword();
			}
			if(!loginUser.getPassword().equals(user.getPassword())) {	// 비밀번호 검사
				bresult.reject("error.login.password");
				mav.getModel().putAll(bresult.getModel());				
				return mav;
			}
		
			// 관리자가 아니며, 로그인 유저 = 입력 유저 아이디
//			User dbUser = service.userSelectOne(user.getUserid());//userid의 비번.......			
//			
//			if(!dbUser.getPassword().equals(user.getPassword())) {// 비밀번호가 틀린경우
//				bresult.reject("error.login.password");
//				mav.getModel().putAll(bresult.getModel());				
//				return mav;
//			}
			
			//비밀번호가 정상인 경우
			try {					
					service.userUpdate(user); //수정 성공
					//session 정보 수정하기
					//User loginUser = (User)session.getAttribute("loginUser");					
					if(user.getUserid().equals(loginUser.getUserid())){//본인정보 수정시에만 로그인 정보를 수정하자.
							session.setAttribute("loginUser", user);
					
					mav.setViewName("redirect:main");
					}
					else if(loginUser.getUserid().equals("admin")) {						
						mav.setViewName("redirect:../admin/list");
					}
					
				}catch(Exception e) {
					e.printStackTrace(); //수정 실패
					throw new LoginException("고객정보 수정 실패","update");
				}
				return mav;
		}
		@GetMapping("password")
		public String loginCheckPasswordGet(HttpSession session) {
			return null;
		}
		//로그인한 사용자의 비밀번호만 변경 가능
		@PostMapping("password")
		public ModelAndView loginCheckPassword(@RequestParam Map<String,String> req,HttpSession session) {
			ModelAndView mav = new ModelAndView();
			User loginUser = (User)session.getAttribute("loginUser");
			//입력된 비밀번호와 로그인정보의 비밀번호가 비교
			if(!req.get("password").equals(loginUser.getPassword())) {
				throw new LoginException("비밀번호 오류입니다.","password");
			}
			//1.db에 비밀번호변경.			
			//2.session에 있는 유저정보도 변경해야됨
			try {
				service.userPassword(loginUser.getUserid(),req.get("chgpass"));
				loginUser.setPassword(req.get("chgpass")); //session의 loginUser 객체의 비밀번호를 변경한다.
				mav.setViewName("redirect:main");
			}catch(Exception e) {
				throw new LoginException("비밀번호 수정시 오류가 있습니다.","password");
			}
			
			return mav;
		}
		/*
		 * 회원탈퇴
		 * 1. 파라미터 정보 저장.(userid와 password의 2개의 파라미터)
		 * 		- 관리자인 경우 탈퇴 불가
		 * 2. 비밀번호를 검증
		 * 		본인탈퇴 : 본인 비밀번호
		 * 		관리자가 타인 탈퇴 : 관리자 비밀번호
		 * 3. 비밀번호 불일치
		 * 		메세지 출력 후 delete 페이지로 이동
		 * 4. 비밀번호 일치
		 * 		db에서 해당 사용자정보 삭제하기
		 * 		본인탈퇴 : 로그아웃, login 페이지로 이동
		 * 		관리자가 타인탈퇴 : admin/list로 페이지 이동		
		 */
		
		@PostMapping("delete")
		public ModelAndView idCheckdelete(String userid, HttpSession session, String password ){
			ModelAndView mav = new ModelAndView();					
			if(userid.equals("admin"))
					throw new LoginException("관리자 탈퇴는 불가합니다.", "main");
			User loginUser = (User)session.getAttribute("loginUser");
			if(!password.equals(loginUser.getPassword())) {
				throw new LoginException("비밀번호를 확인하세요.", "delete?id="+userid);
			}
			try {
				service.userDelete(userid);				
			}catch(Exception e) {
				e.printStackTrace();
				throw new LoginException("탈퇴시 오류발생.", "delete?id="+userid);
			}
			if(loginUser.getUserid().equals("admin")) {//관리자의 탈퇴
				mav.setViewName("redirect:../admin/list");
			}else{//일반사용자의 탈퇴
				mav.setViewName("redirect:login");
				session.invalidate();//탈퇴하면 세션도 모두 없애준다.
			}
			return mav;
		}
		
}