package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller //설정에 의한 객체화(@Component) + 기능을부여(컨트롤러의 기능을한다) //뷰를 반환하기 위해 사용//해당클래스가 Controller임을 나타내기 위한 어노테이션
@RequestMapping("board") //요청정보와 매핑한다. url에 board 라는 정보가 있으면 나야라고 하는거 //요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
public class BoardController {
	@Autowired //객체를 주입. ShopService라는 객체를 service에 저장해라고 하는것.  //이 어노테이션으로 자동으로 객체화
	
	/* DI와 @Autowired 설명 잘해놓은 곳 ->https://life-with-coding.tistory.com/433*/
	ShopService service; //BoardController클래스는 shopservice 객체가 필요하다  //ShopService를 service로 객체화해서 전체가 사용할 수 있게 한것.
	
	//http://localhost:8090/springstudy/board/write    .jsp로 실행하는게 아니다.
	//@GetMapping : Get 방식 요청정보를 매핑
	@GetMapping("write")
	public ModelAndView getBoard() {//이거 사용에 대해서 자세히 설명된곳->https://hongku.tistory.com/116
		ModelAndView mav = new ModelAndView(); //기본 뷰는 url로 부터 설정됨.
		Board board = new Board();//빈껍데기의 객체를 만든것임
		mav.addObject("board" , board); //board라고 하는 객체를 뷰로 전달을 해주자.
		return mav; //뷰의 이름과, 전달할 데이터를 리턴.
	}
	//Post 방식 요청 처리
	@PostMapping("write")
	//@Valid : 유효성 검사를함(입력검증). 결과 bresult 객체 전달.
	//board : 요청정보에 있는 파라미터 이름과 프로퍼티이름과 비교하여 파라미터값을 저장한다.
	//        board객체에 파라미터값을 저장상태로 매개변수로 전달.
	public ModelAndView write(@Valid Board board, BindingResult bresult,
			HttpServletRequest request) {//HttpServletRequest에 대해 자세이 설명한곳->https://hongku.tistory.com/118
		//@valid설명 잘 나와있는곳 ->https://wiper2019.tistory.com/247
		System.out.println(board);
		ModelAndView mav = new ModelAndView();
				//bresult 객체에 오류가 존재하는 경우
				if(bresult.hasErrors()) {
					mav.getModel().putAll(bresult.getModel());
					return mav;
				}
		//bresult 객체에 오류가 없는 경우. 정확하게 입력이 된경우.
		//board라는 객체에 파일 업로드된 파일을 서버에 저장을 해야됨. db에 파라미터값을 저장.
		service.boardwrite(board,request);//service는 컨트롤러랑 모델단에서 중간역할을 한다.
		// 뷰를 list로 재요청하기
		mav.setViewName("redirect:list"); //list 요청페이지를 브라우저가 재요청하도록 뷰선택(뷰리졸버랑 상관없다.) 
		return mav;                      //브라우저의 url 정보가 board/list 변경.
	}
	@RequestMapping("list") //GET,POST 상관없이 boar/list 요청이 들어온 경우 // 페이징처리
	public ModelAndView list(Integer pageNum) { //pageNum 이 파라미터를 받는다.
		ModelAndView mav = new ModelAndView();
		//pageNum 이라는 파라미터의 값이 없는 경우
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		int limit = 10; //한페이지에 보여질 게시물의 건수
		int listcount = service.boardcount(); //전체 게시물 등록 건수
		List<Board> boardlist = service.boardlist(pageNum,limit);//현재 페이지에 출력한 게시물 목록 부분 //최대10건만 가져온다.
		//최대 필요한 페이지 수
		/*
		 * 전체 게시물 건수   페이지수
		 * 10건            1  :  listcount10.0 / 10 + 0.95 => int(1.95) => 1페이지
		 * 11건            2  :  listcount11.0 / 10 + 0.95 => int(2.05) => 2페이지
		 * 111건           12 :  listcount111.0 / 10 + 0.95 => int(12.05) => 12페이지
		 * 301건           31 : listcount301.0 / 10 + 0.95 => int(31.05) => 31페이지
		 * 300건           30 : listcount300.0 / 10 + 0.95 => int(30.95) => 30페이지
		 * 이걸 계산한게 아래
		 */
		int maxpage = (int)((double)listcount/limit + 0.95);
		//화면에 표시할 페이지의 시작 번호
		/*
		 * 1.............31 화면에 표시될 페이지의 갯수 : 10개만 표시
		 * 화면에 표시될 시작 페이지 번호
		 * 현재페이지    시작페이지
		 *    2          1  : pageNum 2/10.0 => 0.2 + 0.9 =>1.1 - 1 =>int(0.1) *10 + 1 => 1    0.1을 int로 해주면 0이된다.
		 *   10          1  : pageNum 10/10.0 => 1.0 + 0.9 =>1.9 - 1 =>int(0.9) *10 + 1 => 1   0.9를 int로 해주면 0이된다.
		 *   20         11  : pageNum 20/10.0 => 2.0 + 0.9 =>2.9 - 1 =>int(1.9) *10 + 1 => 11
		 *   21         21  : pageNum 21/10.0 => 2.1 + 0.9 =>3.0 - 1 =>int(2.0) *10 + 1 => 21
		 *   22         21  : pageNum 22/10.0 => 2.2 + 0.9 =>3.1 - 1 =>int(2.1) *10 + 1 => 21
		 */
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		//화면에 표시할 페이지의 끝 번호
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage;
		//화면 표시될 게시물 번호. 의미없음
		int boardno = listcount - (pageNum - 1) * limit;
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);		
		return mav;
	}
	@GetMapping("detail") //*는 Get방식으로 요청이면서, detail도 여기서 실행
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		if(num != null) {
			Board board = service.getBoard(num);
			service.readcntadd(num); //조회된 레코드의 조회건수를 1증가 시키는 db
			mav.addObject("board", board); //board 객체가 필요하면 @GetMapping 을 사용하는 것 같다.
		}
		
		return mav; // board/detail 뷰로 설정.
	}
	@GetMapping("*") //Get방식 요청이 설정 되지 않은 경우 호출되는 메서드 //이름이 없는 애들은 여기서 처리해준다.
	public ModelAndView getBoard(Integer num) {
		ModelAndView mav = new ModelAndView();
		if(num != null) {
			Board board = service.getBoard(num);			
			mav.addObject("board", board); //board 객체가 필요하면 @GetMapping 을 사용하는 것 같다.
		}
		return mav; // board/update 뷰로 설정.
	}
	/*
	 * 1.파라미터 값 Board 객체 저장. 유효성 검증.
	 * 2. 입력된 비밀번호와db의 비밀번호를 비교
	 * 	- 비밀번호가 맞는 경우
	 * 	  수정정보를 db에 변경
	 * 	  첨부파일 변경 : 첨부파일 업로드, fileurl 정보 수정
	 * 	  detail 페이지 호출
	 * 
	 * 	- 비밀번호가 틀린 경우
	 * 	  예회발생하여  
	 *    '비밀번호가 틀립니다.', update Get방식으로 호출
	 */
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult bresult,HttpServletRequest request) {//request 업데이트할때 사용
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		Board dbBoard = service.getBoard(board.getNum());
		if(!board.getPass().equals(dbBoard.getPass())) {
			//board.getPass() : 화면에서 입력받은 비밀번호
			//dbBoard.getPass() : db에 등록된 비밀번호
			//예외를 없애는 쪽으로 제어가 된다.
			//throw : 예외를 강제로 발생.
			throw new BoardException("비밀번호가 틀립니다.","update?num="+board.getNum());//throw 예욀르 발생시켜서 예외강제발생 //throw 와 throws차이알기
		}
		try {
			board.setFileurl(request.getParameter("file2"));
			service.boardUpdate(board, request);
			mav.setViewName("redirect:detail?num="+board.getNum());
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시글 수정을 실패 했습니다.","update?num="+board.getNum());
		}
		return mav;
		}
	/*
	 * 1.num, pass 파라미터 저장.
	 * 2.db의 비밀번호와 입력된 비밀번호가 틀리면 error.login.password
	 *   코드 입력.=> 유효성 검증 내용 출력하기
	 * 3.db에서 해당 게시물 삭제.
	 *   삭제 실패 : 게시글 삭제 실패. delete 페이지로 이동
	 *   삭제 성공 : list 페이지 이동
	 */
	@PostMapping("delete")
	public ModelAndView delete(Board board,BindingResult bresult) {//@valid로 유효성 검증은 하지 않음
		ModelAndView mav = new ModelAndView();
		int num = board.getNum();//파라미터로 들어옴 num
		String pass = board.getPass();//파라미터로 들어옴 pass
		try {
			Board dbboard = service.getBoard(num);
			if(!pass.equals(dbboard.getPass())) {
				bresult.reject("error.login.password");//globalError 오류라고 보면 된다. //reject 로 오류등록 하고 뷰단으로 전달해준다.
				return mav;
			}
			service.boardDelete(num);
			mav.setViewName("redirect:list");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 삭제 실패","delete?num="+num);
		}
		return mav;
	}
	/*
	   1. 파라미터 값을 Board 객체에 저장하고 유효성 검증하기
	      원글정보 : num, grp, grplevel, grpstep  => hidden 정보
	      답글정보 : name, pass, subject, content => 등록정보
	   2. service.boardReply()
	      - 같은 grp 값을 사용하는 게시물들의 grpstep 값을 1 증가 하기.
	        boardDao.grpStepAdd(grp,grpstep)
	      - Board 객체를 db에 insert 하기.
	        num : maxnum + 1
	        grp : 원글과 동일.
	        grplevel : 원글 + 1
	        grpstep : 원글 + 1
	   4. 등록 성공시 :" list로 페이지 이동
	      등록 실패시 :" 답변등록시 오류발생"메시지 출력 후, reply로 페이지 이동하기 
	 */
	@PostMapping("reply")
	public ModelAndView reply (@Valid Board board,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			Board dbBoard = service.getBoard(board.getNum()); //원글에 대한 ㅈ어보를 가지고 와서
			Map<String, Object> map = bresult.getModel(); //bresult.getModel() : 오류 정보를 젖아하고 있는 객체 //여기에는 board객체가 있다.
			Board b = (Board)map.get("board");//화면에서 입력된 값 저장 // map이라는 거 안쪽에 board라는 객체를 꺼낼수 있다.
			b.setSubject(dbBoard.getSubject());
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("답글 등록에 실패 했습니다.","reply?num="+board.getNum());
		}
		return mav;
	}

	
	
	/*
	@GetMapping("test")
	public ModelAndView getTest() {
		ModelAndView mav = new ModelAndView(); //기본 뷰는 url로 부터 설정됨.
		Board board = new Board();
		mav.addObject("board" , board); //board라고 하는 객체를 뷰로 전달을 해주자.
		return mav; //뷰의 이름과, 전달할 데이터를 리턴.
	}
	@PostMapping("test")
	public ModelAndView test(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
				if(bresult.hasErrors()) {
					mav.getModel().putAll(bresult.getModel());
					return mav;
				}
		return mav;
	}
	*/
}
