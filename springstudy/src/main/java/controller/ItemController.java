package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Board;
import logic.Item;
import logic.ShopService;

@Controller
@RequestMapping("item")
public class ItemController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<Item> itemlist = service.itemList();
		mav.addObject("itemList",itemlist);//list.jsp에서 forEach문에서items가 itemList여서 이렇게 만든것이다. 
		return mav;
	}
	@RequestMapping({"detail","confirm"}) 
	public ModelAndView detail(Integer id) {
		ModelAndView mav = new ModelAndView();		
			Item item = service.getItem(id);
			mav.addObject("item", item); 		
		return mav;
	}
	@RequestMapping("create")//GET,POST 상관없이
	public ModelAndView add(Integer id) {
		ModelAndView mav = new ModelAndView("item/add");//create는 add.jsp이다 뷰단하고 이름이 다르다. 그래서 add가 어디있는지 표기를 해준것임
		mav.addObject(new Item());
		return mav;
	}
	@RequestMapping("register")//GET,POST 상관없이
	public ModelAndView add(@Valid Item item, BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add");//입력한 오류 발생시 화면
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//item 테이블에 insert, picture 업로드파일을 파일로 생성
		service.itemCreate(item,request);
		mav.setViewName("redirect:list");
		return mav;
	}
	@RequestMapping("edit") //detail과 비슷한 코딩ㅋㅋㅋㅋㅋㅋㅋ
	public ModelAndView edit(Integer id) {
		ModelAndView mav = new ModelAndView();		
			Item item = service.getItem(id);
			mav.addObject("item", item); 		
		return mav;
	}
	/*
	 * 1. 수정 입력값을 유효성 검증
	 * 2. db에 내용 등록하기
	 * 3. 등록 후 list로 페이지 이동
	 */
	@RequestMapping("update")//뷰단의 이름이 다 다르기 때문에 RequestMapping으로 해주는 것이다. Get이나 Post 가 다르기 때문에
	public ModelAndView update(@Valid Item item, BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");//입력값 오류 발생시 화면
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemUpdate(item,request);//db에 내용 등록하는 구문 //upload된 내용들도 하라는게 request
		mav.setViewName("redirect:list");//등록 후 list 페이지로 이동시키는거
		return mav;
	}
	//상품 삭제하기
	//삭제 완료 후 list로 페이지 이동
	//http://localhost:8090/springstudy/item/delete?id=1
	@RequestMapping("delete")
	public ModelAndView delete(Item item, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		int id = item.getId();
		try {
			Item dbitem = service.getItem(id);
			service.itemDelete(id);
			mav.setViewName("redirect:list");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
/*
 * 이렇게도 할 수 있다// 굉장히 간단하게 할 수 있다 삭제는
 * @RequestMapping("delete")
 * public String delete(String id){// => request.getParameter("id");
 * service.itemDelete(id);
 * return redirect:list
 * }
 */
}
