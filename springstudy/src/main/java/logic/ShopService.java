package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.UserDao;

@Service //이게 없으면 실행이 안된다. //설정에 의한 객체화 + 기능을 추가(서비스기능:컨트롤러와  모델의중간역할을 해주는것). @Component로 대체 가능
public class ShopService {
	@Autowired
	BoardDao boardDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ItemDao itemDao;
	
	public void boardwrite(Board board, HttpServletRequest request) {
		//업로드된 파일 서버에 저장
		if(board.getFile1() != null && !board.getFile1().isEmpty()) { //업로드된 파일이 존재하면
			uploadFileCreate(board.getFile1(),request);				  //파일로 저장
			board.setFileurl(board.getFile1().getOriginalFilename()); //업로드된 파일의 이름 setFilrurl
		}		
		//db에 내용 추가
		boardDao.write(board);
	}

	private void uploadFileCreate(MultipartFile file, HttpServletRequest request) {//void반환 , 매개변수를 봐야된다.
		String orgFile = file.getOriginalFilename(); //업로드된 파일의 실제 파일명.
		//파일 업로드 위치
		//request.getServletContext().getRealPath("/") + "board/file/" : 실제 웹어플리케이션 서버의 폴더 위치값을 알려주는것
		String uploadPath = request.getServletContext().getRealPath("/") + "board/file/";
		File fpath = new File(uploadPath); 
		//fpath : 파일을 업로드 하는 위치 정보를 저장하고 있는 File 클래스의 객체가 된다.
		//파일 업로드는 폴더가 만들어져 있지 않으면 안된다.
		//fpath.exists() : 폴더가 존재하는지 여부확인 . 존재하면 :True가 되고 , 없으면 False가 된다.
		if(!fpath.exists()) fpath.mkdirs(); //mkdirs는 폴더를 생성하라. 파일 업로드시 폴더가 없으면 오류가 발생한다.
		try {
			file.transferTo(new File(uploadPath + orgFile)); //서버에 해당파일에 업로드된 파일을 저장. 이걸로 업로드가 완성된다.
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int boardcount() {
		return boardDao.count();
	}

	public List<Board> boardlist(Integer pageNum, int limit) {
		return boardDao.list(pageNum,limit);
	}

	public Board getBoard(Integer num) {
		return boardDao.selectOne(num);
	}

	public void readcntadd(Integer num) {
		boardDao.readcntadd(num);
	}

	public void boardUpdate(Board board, HttpServletRequest request) {
		//업로드된 파일 서버에 저장
				if(board.getFile1() != null && !board.getFile1().isEmpty()) { //업로드된 파일이 존재하면
					uploadFileCreate(board.getFile1(),request);				  //파일로 저장
					board.setFileurl(board.getFile1().getOriginalFilename()); //업로드된 파일의 이름 setFilrurl
				}		
				//db에 내용 추가
				boardDao.update(board);
	}

	public void boardDelete(int num) {
		boardDao.delete(num);
	}

	//1.기존의 답글의 grpstep 값을 +1로 해서 수정
	//2. 등록된 답글 정보를 객체에다가 insert를 해야된다.
	public void boardReply(Board board) {
		boardDao.updateGrpStep(board);
		boardDao.reply(board);
	}
	
	//여기부터는 회원가입 부분
	public void userInsert(User user) {
		userDao.insert(user);
	}///////4교시 코딩한거 아래

	public User userSelectOne(String userid) {
		return userDao.selectOne(userid);
	}

	public String getSearch(User user, String url) {
		return userDao.search(user,url);
	}

	public void userUpdate(User user) {
		userDao.update(user);
	}

	public void userPassword(String userid, String chgpass) {
		userDao.passwordUpdate(userid,chgpass);
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
	}

	public List<User> userList() {
		return userDao.list();
	}

	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}

	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.selectOne(id);//id라는 매개변수 정확히 해줘야 Dao에서 받을수 있다
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { //업로드된 파일이 존재하면
			uploadPictureCreate(item.getPicture(),request);				  //파일로 저장
			item.setPictureUrl(item.getPicture().getOriginalFilename()); //업로드된 파일의 이름 setFilrurl
		}		
		//db에 내용 추가
		int maxid = itemDao.maxNo();
		item.setId(maxid+1);
		itemDao.insert(item);
		
	}
	//1. 상품수정전 화면을 출력하기
	//2.uploadPictureCreate / uploadPictureCreate 두개를 하나의 함수로 수정하기	
	//uploadPictureCreate 제거해도 uploadPictureCreate 함수로 모든 파일 업로드 처리하기
	private void uploadPictureCreate(MultipartFile picture, HttpServletRequest request) {
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + "img/"; //wepapp 쪽이다.
		File fpath = new File(uploadPath);
		try {
			picture.transferTo(new File(uploadPath + orgFile));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { //업로드된 파일이 존재하면
			uploadPictureCreate(item.getPicture(),request);				  //파일로 저장
			item.setPictureUrl(item.getPicture().getOriginalFilename()); //업로드된 파일의 이름 setFilrurl
		}		
		//db에 내용 추가		
		itemDao.update(item);
	}

	public void itemDelete(int id) {
		itemDao.delete(id);
		
	}
	/*
	 * Sale객체를 전달할것이다.
	 * sale,saleitem 테이블에 저장하기->궁극적 목적
	 * 1. sale 테이블의 saleid값의 최대값을 조회 : 최대값+1
	 * 2. sale 정보 저장 : userid(로그인정보에 있는거), sysdate
	 * 3. Cart데이터에서 saleitem 데이터를 추출해서 insert 해주면 됨
	 * 4. saleitem 정보를 sale 데이터에 List 형태로 저장
	 * 5. sale 데이터를 return.
	 * Dao2개
	 */
	public Sale checkend(User loginUser, Cart cart) {
		
		return null;
	}

	

	
	
	

	
	


	
	
	

}
