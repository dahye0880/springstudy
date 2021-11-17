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
	@RequestMapping("detail") 
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
	
	
}
