package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Mail;
import logic.ShopService;
import logic.User;
/*
 * AdminController의 모든 메서드들은 반드시 관리자로 로그인 해야만 실행되도록
 * AOP 메서드를 설정해야 한다.
 * 1. 로그아웃 상태? : 로그인하세요. login페이지로 이동
 * 2. 관리자가 아닌 경우 : 관리자만 거래 가능합니다. main 페이지로 이동
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> userlist = service.userList();
		mav.addObject("list",userlist);
		return mav;
	}
	@RequestMapping("mailForm")
	public ModelAndView mailForm(String[] idchks,HttpSession session) {//idchks=선택된 user정보들
		ModelAndView mav = new ModelAndView("admin/mail"); //view 를 설정하자.
		/*
		 * if(true && 문장1) => 문장1 실행
		 * if(false && 문장2) => 문장2 실행X. 문장2의 결과와 상관없이 전체 명체 False
		 * if(true || 문장3) => 문장3 실행X. 문장3의 결과와 상관없이 전체 명체 True
		 * if(false || 문장4) => 문장4 실행O. 문장4에 의해서 결과과 정해진다.
		 */
		if(idchks == null || idchks.length==0) {//shortcut //조건문일때 앞에 있는거 순서 바꾸면 안된다 
			throw new LoginException("메일 전송 대상자를 선택하세요","list");
		}
		//idchks : 회원목록에서 선택된 userid 에 회원 정보 목록
		List<User> userlist = service.userList(idchks);
		mav.addObject("list",userlist);
		return mav;
	}
	@RequestMapping("mail")//Get, POST  방식 모두 가능
	public ModelAndView mail(Mail mail, HttpSession session) {
		ModelAndView mav = new ModelAndView("alert");//뷰이름
		mailSend(mail);
		mav.addObject("message","메일 전송이 완료 되었습니다.");
		mav.addObject("url","list");
		return mav;
	}
	private final class MyAuthenticator extends Authenticator{//내부클래스
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id,pw);
		}
	}
	private void mailSend(Mail mail) {
		//메일을 전송할때 사용할 인증 객체
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(),mail.getNaverpw());
		//Properties(클래스) : Map 객체이다 : <키, 값> 쌍인 객체다.
		//                           Hashtable의 하위클래스이다.
		//                           <String,String>형태인 Map 객체   
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream
		("D:\\kic\\example\\springstudy\\src\\main\\resources\\mail.properties");//왼쪽은 키값 = 오른쪽은 밸류값 //("mail.properties")이건 절대경로로 해야된다.
			prop.load(fis);//mail.properties 안에있는 파일의 내용을 키=값 형태로 저장
			prop.put("mail.smtp.user", mail.getNaverid());//인증에 필요한거
		}catch(IOException e) {
			e.printStackTrace();
		}
		//prop : 메일 전송을 위한 환경 부분을 설정
		//auth : 인증 객체(인증서)
		//session : 메일 전송을 위한 연결 객체
		Session session = Session.getInstance(prop,auth);//이세션은 javax.mail이다. //smtp 전송프로토콜
		//mimeMsg : 전송을 위한 메일 객체
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			//보내는 메일 주소를 사용하는 InternetAddress 객체
			mimeMsg.setFrom(new InternetAddress(mail.getNaverid()+"@naver.com"));
			//
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				try {
					//한글처리를 위해 인코딩 부분 설정
					addrs.add(new InternetAddress(new String(email.getBytes("UTF-8"),"8859_1")));
				}catch(UnsupportedEncodingException ue) {//UnsupportedEncodingException 한글처리 때문에 이걸로 예외처리 // 자바예외처리는 필수다
					ue.printStackTrace();
				}
			}
			//arr : 수신 메일 주소 들
			InternetAddress[] arr = new InternetAddress[emails.length];//이메일이 몇개가 될지 몰라서 email.length로 배열로 만든것
			for(int i=0;i<addrs.size();i++) {
				arr[i] = addrs.get(i);
			}
			mimeMsg.setSentDate(new Date());//보낸일자
			mimeMsg.setRecipients(Message.RecipientType.TO, arr);//수신메일 주소 설정
			mimeMsg.setSubject(mail.getTitle());//메일의 제목부분 설정
			//MimeMultipart : 내용부분 처리
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();//내용, 첨부파일 분리 부분
			message.setContent(mail.getContents(),mail.getMtype());//내용부분 설정 //받는쪽에서 html parse로 받고 text로 받으면 그대로 보여줘
			multipart.addBodyPart(message);//내용 추가.
			//첨부파일부분추가(getFile1 이게 갖고 있음)
			for(MultipartFile mf : mail.getFile1()) {
				if((mf != null) && (!mf.isEmpty())) {//첨부파일 존재여부
					multipart.addBodyPart(bodyPart(mf));//첨부파일을 메일에 추가
				}
			}
			mimeMsg.setContent(multipart);//메일, 첨부파일 저장
			Transport.send(mimeMsg);//메일 전송.
		}catch(MessagingException me) {
			me.printStackTrace();
		}
	}
	
	private BodyPart bodyPart(MultipartFile mf) {//MultipartFile mf 데이터를 가지고 있는 파일
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();
		String path = "d:/mailupload/";
		File f = new File(path);
		//해당폴더가 없으면if(!f.exists()) 폴더를 생성하라(f.mkdirs();)
		if(!f.exists()) f.mkdirs();
		File f1 = new File(path + orgFile);//업로드할 파일을 설정
		try {
			mf.transferTo(f1);//파일을 f1이라는 이름으로 저장(업로드하는곳)
			body.attachFile(f1);//메일에 첨부파일로 추가
			//한글 처리 설정
			body.setFileName(new String(orgFile.getBytes("UTF-8"),"8859_1"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return body;
	}
}
